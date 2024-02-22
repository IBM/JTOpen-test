///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSPropertyTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IFS;


import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileDescriptor;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileOutputStream;

public class IFSPropertyTestcase extends IFSGenericTestcase
{

/**
Constructor.
**/
  public IFSPropertyTestcase (AS400 systemObject,
        String userid,
        String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password,
            "IFSPropertyTestcase",
            namesAndVars, runMode, fileOutputStream, pwrSys);
    }

  class Filter implements com.ibm.as400.access.IFSFileFilter
  {
    public boolean accept(IFSFile file)
    {
      return true;
    }
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup(); 
  }






  /**
   Ensure that IFSFile.canRead() throws ExtendedIllegalStateException indicating
   PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var001()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.canRead();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.canRead() throws ExtendedIllegalStateException indicating
   PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var002()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.canRead();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.canWrite() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var003()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.canWrite();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.canWrite() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var004()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.canWrite();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.delete() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var005()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.delete();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.delete() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var006()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.delete();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.equals(Object) returns false if the system has not
   been set.
   **/
  public void Var007()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      assertCondition(!file.equals(new IFSFile(systemObject_, ifsPathName_)));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.equals(Object) returns false if the path has not
   been set.
   **/
  public void Var008()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      assertCondition(!file.equals(new IFSFile(systemObject_, ifsPathName_)));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.equals(Object) returns false if the target system has
   not been set.
   **/
  public void Var009()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFile targetFile = new IFSFile();
      targetFile.setPath(ifsPathName_);
      assertCondition(!file.equals(targetFile));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.equals(Object) returns false if the target path has
   not been set.
   **/
  public void Var010()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFile targetFile = new IFSFile();
      targetFile.setSystem(systemObject_);
      assertCondition(!file.equals(targetFile));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.exists() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var011()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.exists();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.exists() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var012()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.exists();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.getAbsolutePath() returns the path specified by
   IFSFile.setPath(String).
   **/
  public void Var013()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      assertCondition(file.getAbsolutePath().equals(ifsPathName_), 
          "file.getAbsolutePath="+file.getAbsolutePath()+" ifsPathName_="+ifsPathName_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.getCanonicalPath() returns the path specified by
   IFSFile.setPath(String).
   **/
  public void Var014()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      assertCondition(file.getCanonicalPath().equals(ifsPathName_), 
          "file.getAbsolutePath="+file.getCanonicalPath()+" ifsPathName_="+ifsPathName_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.getFreeSpace() throws ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var015()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.getFreeSpace();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.getName() returns the name part of the path specified by
   IFSFile.setPath(String).
   **/
  public void Var016()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath("/Directory/File");
      assertCondition(file.getName().equals("File"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.getParent() returns the parent part of the path
   specified by IFSFile.setPath(String).
   **/
  public void Var017()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath("/Directory/Name");
      assertCondition(file.getParent().equals("/Directory"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.getPath() returns the path specified by
   IFSFile.setPath(String).
   **/
  public void Var018()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath("/Directory/Name");
      assertCondition(file.getPath().equals("/Directory/Name"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.getSystem() returns the system specified by
   IFSFile.setSystem(AS400).
   **/
  public void Var019()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      assertCondition(file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.isAbsolute() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var020()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.isAbsolute();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.isDirectory() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var021()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.isDirectory();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.isDirectory() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var022()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.isDirectory();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.isFile() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var023()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.isFile();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.isFile() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var024()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.isFile();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.lastModified() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var025()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.lastModified();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.lastModified() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var026()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.lastModified();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.length() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var027()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.length();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.length() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var028()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.length();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var029()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.list();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var030()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.list();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list(IFSFileFilter) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var031()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.list(new Filter());
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list(IFSFileFilter) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var032()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.list(new Filter());
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list(IFSFileFilter, String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var033()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.list(new Filter(), "*");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list(IFSFileFilter, String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var034()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.list(new Filter(), "*");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list(String) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var035()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.list("*");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.list(String) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var036()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.list("*");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.mkdir() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var037()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.mkdir();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.mkdir() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var038()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.mkdir();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.mkdirs() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var039()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.mkdirs();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.mkdir() throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var040()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.mkdirs();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before path is set.
   **/
  public void Var041()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.renameTo(new IFSFile(systemObject_, ifsPathName_));
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before system is set.
   **/
  public void Var042()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.renameTo(new IFSFile(systemObject_, ifsPathName_));
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) throws ExtendedIllegalStateException
   indicating PROPERTY_NOT_SET if called before the target path is set.
   **/
  public void Var043()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFile targetFile = new IFSFile();
      targetFile.setSystem(systemObject_);
      file.renameTo(targetFile);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.setLastModified(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var044()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.setLastModified(0L);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.setLastModified(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var045()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifsPathName_);
      file.setLastModified(0L);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFile.toString() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var046()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setSystem(systemObject_);
      file.toString();
      notApplicable("Exception didn't occur.");
                      // We no longer expect an exception   @A1C
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.available() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var047()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.available();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.available() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var048()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath(ifsPathName_);
      file.available();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.getPath() returns the path specified by
   IFSFileInputStream.setPath(String).
   **/
  public void Var049()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath("/Directory/Name");
      assertCondition(file.getPath().equals("/Directory/Name"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.getSystem() returns the system specified by
   IFSFileInputStream.setSystem(AS400).
   **/
  public void Var050()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      assertCondition(file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var051()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.lock(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var052()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath(ifsPathName_);
      file.lock(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.read() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var053()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.read() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var054()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath(ifsPathName_);
      file.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.read(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var055()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.read(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var056()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath(ifsPathName_);
      file.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.read(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var057()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.read(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var058()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath(ifsPathName_);
      file.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.skip(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var059()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.skip(1L);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.skip(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var060()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath(ifsPathName_);
      file.skip(1L);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }


  /**
   Ensure that IFSFileInputStream.setFD(IFSFileDescriptor) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var061()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream(systemObject_,
                                                       ifsPathName_);
      file.available();
      IFSFileInputStream file1 = new IFSFileInputStream(systemObject_,
                                                        ifsPathName_);
      file.setFD(file1.getFD());
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "FD",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.setPath(String) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var062()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream(systemObject_,
                                                       ifsPathName_);
      file.available();
      file.setPath(file.getPath() + "1");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.setShareOption(int) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var063()
   {
     createFile(ifsPathName_);
     try
     {
       IFSFileInputStream file = new IFSFileInputStream(systemObject_,
                                                        ifsPathName_);
       file.available();
       file.setShareOption(IFSFileInputStream.SHARE_NONE);
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "shareOption",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSFileInputStream.setSystem(AS400) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var064()
   {
     createFile(ifsPathName_);
     try
     {
       IFSFileInputStream file = new IFSFileInputStream(systemObject_,
                                                        ifsPathName_);
       file.available();
       file.setSystem(new AS400());
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "system",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSFile.setSystem(AS400) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var065()
   {
     createFile(ifsPathName_);
     try
     {
       IFSFile file = new IFSFile(systemObject_,
                                  ifsPathName_);
       file.length();
       file.setSystem(new AS400());
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "system",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSFile.setPath(String) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var066()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_,
                                 ifsPathName_);
      file.length();
      file.setPath(file.getPath() + "1");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var067()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var068()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath(ifsPathName_);
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.getPath() returns the path specified by
   IFSFileOutputStream.setPath(String).
   **/
  public void Var069()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath("/Directory/Name");
      assertCondition(file.getPath().equals("/Directory/Name"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.getSystem() returns the system specified by
   IFSFileInputStream.setSystem(AS400).
   **/
  public void Var070()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      assertCondition(file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var071()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.lock(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var072()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath(ifsPathName_);
      file.lock(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setFD(IFSFileDescriptor) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var073()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream(systemObject_,
                                                       ifsPathName_);
      file.flush();
      IFSFileOutputStream file1 = new IFSFileOutputStream(systemObject_,
                                                        ifsPathName_);
      file.setFD(file1.getFD());
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "FD",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.setPath(String) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var074()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream(systemObject_,
                                                       ifsPathName_);
      file.flush();
      file.setPath(file.getPath() + "1");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.setShareOption(int) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var075()
   {
     createFile(ifsPathName_);
     try
     {
       IFSFileOutputStream file = new IFSFileOutputStream(systemObject_,
                                                        ifsPathName_);
       file.flush();
       file.setShareOption(IFSFileInputStream.SHARE_NONE);
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "shareOption",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSFileOutputStream.setSystem(AS400) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var076()
   {
     createFile(ifsPathName_);
     try
     {
       IFSFileOutputStream file = new IFSFileOutputStream(systemObject_,
                                                        ifsPathName_);
       file.flush();
       file.setSystem(new AS400());
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "system",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSFileOutputStream.write(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var077()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.write(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.write(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var078()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath(ifsPathName_);
      file.write(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.write(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var079()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.write(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.write(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var080()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath(ifsPathName_);
      file.write(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.write(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var081()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.write(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileOutputStream.write(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var082()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath(ifsPathName_);
      file.write(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var083()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var084()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var085()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.getFilePointer() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var086()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.getFilePointer();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.getFilePointer() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var087()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.getFilePointer();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.getFilePointer() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var088()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.getFilePointer();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.getPath() returns the path specified by
   IFSFileOutputStream.setPath(String).
   **/
  public void Var089()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath("/Directory/Name");
      assertCondition(file.getPath().equals("/Directory/Name"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.getSystem() returns the system specified by
   IFSRandomAccessFile.setSystem(AS400).
   **/
  public void Var090()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      assertCondition(file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.length() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var091()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.length();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.length() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var092()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.length();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.length() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var093()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.length();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var094()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.lock(0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var095()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.lock(0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.lock(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var096()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.lock(0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var097()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var098()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var099()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var100()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var101()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var102()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var103()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var104()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var105()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readBoolean() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var106()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readBoolean();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readBoolean() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var107()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readBoolean();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readBoolean() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var108()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readBoolean();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readByte() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var109()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readByte() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var110()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readByte() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var111()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readChar() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var112()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readChar();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readChar() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var113()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readChar();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readChar() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var114()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readChar();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readDouble() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var115()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readDouble();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readDouble() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var116()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readDouble();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readDouble() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var117()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readDouble();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFloat() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var118()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readFloat();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFloat() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var119()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readFloat();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFloat() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var120()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readDouble();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var121()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readFully(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var122()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readFully(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var123()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readFully(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var124()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readFully(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var125()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readFully(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var126()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readFully(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readInt() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var127()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readInt();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readInt() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var128()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readInt();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readInt() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var129()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readInt();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readLine() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var130()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readLine();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readLine() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var131()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readLine();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readLine() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var132()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readLine();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readLong() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var133()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readLong();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readLong() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var134()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readLong();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readLong() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var135()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readLong();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readShort() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var136()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readShort() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var137()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readShort() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var138()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedByte() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var139()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readUnsignedByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedByte() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var140()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readUnsignedByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedByte() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var141()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readUnsignedByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedShort() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var142()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readUnsignedShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedShort() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var143()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readUnsignedShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedShort() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var144()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readUnsignedShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUTF() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var145()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUTF() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var146()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.readUTF() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var147()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.seek(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var148()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.seek(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.seek(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var149()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.seek(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.seek(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var150()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setSystem(systemObject_);
      file.seek(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setExistenceOption(int) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var151()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_,
                                                       ifsPathName_, "rw");
      file.flush();
      file.setExistenceOption(IFSRandomAccessFile.REPLACE_OR_FAIL);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "existenceOption",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.setPath(String) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var152()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_,
                                                       ifsPathName_, "rw");
      file.flush();
      file.setPath(file.getPath() + "1");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.setMode(String) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
  public void Var153()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_,
                                                       ifsPathName_, "rw");
      file.flush();
      file.setMode("r");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                                ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.setShareOption(int) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var154()
   {
     createFile(ifsPathName_);
     try
     {
       IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_,
                                                        ifsPathName_, "rw");
       file.flush();
       file.setShareOption(IFSRandomAccessFile.SHARE_NONE);
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "shareOption",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSRandomAccessFile.setSystem(AS400) throws
   ExtendedIllegalStateException indicating
   ExtendedIllegalStateException.PROPERTY_NOT_CHANGED if called after
   the connection is established.
   **/
   public void Var155()
   {
     createFile(ifsPathName_);
     try
     {
       IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_,
                                                        ifsPathName_, "rw");
       file.flush();
       file.setSystem(new AS400());
       failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       assertExceptionStartsWith(e, "ExtendedIllegalStateException",
                                 "system",
                                 ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
     }
     deleteFile(ifsPathName_);
   }

  /**
   Ensure that IFSRandomAccessFile.skipBytes(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var156()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.skipBytes(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.skipBytes(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var157()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.skipBytes(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.skipBytes(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var158()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.skipBytes(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var159()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.write(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var160()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.write(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var161()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.write(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var162()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.write(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var163()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.write(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[]) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var164()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.write(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var165()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.write(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var166()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.write(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[], int, int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var167()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.write(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeBoolean(boolean) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var168()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeBoolean(true);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeBoolean(boolean) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var169()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeBoolean(true);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeBoolean(boolean) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var170()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeBoolean(true);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeByte(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var171()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeByte(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeByte(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var172()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeByte(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeByte(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var173()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeByte(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeBytes(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var174()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeBytes("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeBytes(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var175()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeBytes("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeBytes(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var176()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeBytes("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeChar(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var177()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeChar(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeChar(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var178()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeChar(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeChar(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var179()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeChar(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeChars(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var180()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeChars("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeChars(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var181()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeChars("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeChars(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var182()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeChars("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeDouble(double) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var183()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeDouble(0.0);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeDouble(double) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var184()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeDouble(0.0);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeDouble(double) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var185()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeDouble(0.0);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeFloat(float) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var186()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeFloat((float) 0.0);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeFloat(float) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var187()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeFloat((float) 0.0);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeFloat(float) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var188()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeFloat((float) 0.0);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeInt(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var189()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeInt(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeInt(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var190()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeInt(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeInt(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var191()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeInt(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeLong(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var192()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeLong(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeLong(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var193()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeLong(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeLong(long) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var194()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeLong(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeShort(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var195()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeShort(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeShort(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var196()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeShort(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeShort(int) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var197()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeShort(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeUTF(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before mode is set.
   **/
  public void Var198()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.writeUTF("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "mode",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeUTF(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var199()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setMode("rw");
      file.writeUTF("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.writeUTF(String) throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var200()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifsPathName_);
      file.setMode("rw");
      file.writeUTF("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSFileInputStream.setFD(null) throws NullPointerException.
   **/
  public void Var201()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setFD((IFSFileDescriptor) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFileInputStream.setPath(null) throws NullPointerException.
   **/
  public void Var202()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFileInputStream.setPath() prepends the leading slash
   if not specified.
   **/
  public void Var203()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setPath("foobar");
      assertCondition(file.getPath().equals("/foobar"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setShareOption(int) throws
   ExtendedIllegalArgumentException if argument one is invalid.
   **/
  public void Var204()
  {
    try
    {
      IFSFileInputStream in = new IFSFileInputStream();
      in.setShareOption(IFSFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFileInputStream in = new IFSFileInputStream();
          in.setShareOption(IFSFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.");
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIllegalArgumentException",
                            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      }
      else
      {
        failed(e1, "Incorrect exception.");
      }
    }
  }


  /**
   Ensure that IFSFileInputStream.setSystem(null) throws NullPointerException.
   **/
  public void Var205()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem((AS400) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFileOutputStream.setFD(null) throws NullPointerException.
   **/
  public void Var206()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setFD((IFSFileDescriptor) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFileOutputStream.setPath() prepends the leading slash
   if not specified.
   **/
  public void Var207()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath("foobar");
      assertCondition(file.getPath().equals("/foobar"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  /**
   Ensure that IFSFileOutputStream.setPath(null) throws NullPointerException.
   **/
  public void Var208()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setPath((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFileOutputStream.setShareOption(int) throws
   ExtendedIllegalArgumentException if argument one is invalid.
   **/
  public void Var209()
  {
    try
    {
      IFSFileOutputStream in = new IFSFileOutputStream();
      in.setShareOption(IFSFileOutputStream.SHARE_NONE - 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFileOutputStream in = new IFSFileOutputStream();
          in.setShareOption(IFSFileOutputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.");
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIllegalArgumentException",
                            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      }
      else
      {
        failed(e1, "Incorrect exception.");
      }
    }
  }

  /**
   Ensure that IFSFileOutputStream.setSystem(null) throws NullPointerException.
   **/
  public void Var210()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem((AS400) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSRandomAccessFile.getFD() returns a valid IFSFileDescriptor.
   **/
  public void Var211()
  {
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      IFSFileDescriptor fd = file.getFD();
      assertCondition(fd.valid());
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


  /**
   Ensure that IFSRandomAccessFile.setPath(null) throws NullPointerException.
   **/
  public void Var212()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setPath() prepends the leading slash
   if not specified.
   **/
  public void Var213()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath("foobar");
      assertCondition(file.getPath().equals("/foobar"));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setSystem(null) throws NullPointerException.
   **/
  public void Var214()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem((AS400) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }


  /**
   Ensure that IFSRandomAccessFile.setShareOption(int) throws
   ExtendedIllegalArgumentException if argument one is invalid.
   **/
  public void Var215()
  {
    try
    {
      IFSRandomAccessFile in = new IFSRandomAccessFile();
      in.setShareOption(IFSRandomAccessFile.SHARE_NONE - 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSRandomAccessFile in = new IFSRandomAccessFile();
          in.setShareOption(IFSRandomAccessFile.SHARE_ALL + 1);
          failed("Exception didn't occur.");
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIllegalArgumentException",
                            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      }
      else
      {
        failed(e1, "Incorrect exception.");
      }
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setShareOption(int) throws
   ExtendedIllegalArgumentException if argument one is invalid.
   **/
  public void Var216()
  {
    try
    {
      IFSRandomAccessFile in = new IFSRandomAccessFile();
      in.setExistenceOption(IFSRandomAccessFile.OPEN_OR_CREATE - 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSRandomAccessFile in = new IFSRandomAccessFile();
          in.setExistenceOption(IFSRandomAccessFile.REPLACE_OR_FAIL + 1);
          failed("Exception didn't occur.");
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIllegalArgumentException",
                            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      }
      else
      {
        failed(e1, "Incorrect exception.");
      }
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before path is set.
   **/
  public void Var217()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.flush() throws
   ExtendedIllegalStateException indicating PROPERTY_NOT_SET if called
   before system is set.
   **/
  public void Var218()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setPath(ifsPathName_);
      file.flush();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                                ExtendedIllegalStateException.PROPERTY_NOT_SET);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.getCCSID() returns the CCSID specified by
   IFSTextFileOutputStream.setCCSID(int).
   **/
  public void Var219()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setCCSID(1234);
      assertCondition(file.getCCSID() == 1234);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


}



