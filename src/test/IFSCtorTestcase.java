///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSCtorTestcase.java
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
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileDescriptor;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSJavaFile;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;
import com.ibm.as400.access.*; 

/**
Test constructors for IFSFile, IFSFileInputStream, IFSFileOutputStream,
and IFSRandomAccessFile.
**/
public class IFSCtorTestcase extends IFSGenericTestcase
{

 String filename = "File";  

/**
Constructor.
**/
  public IFSCtorTestcase (AS400 systemObject,
                   String userid,
                   String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String   driveLetter,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSCtorTestcase",
            namesAndVars, runMode, fileOutputStream, driveLetter, pwrSys);
	if (pwrSys == null) {
	    Exception e = new Exception("IFSCtorTestcase<init>: pwrSys is null");
	    e.printStackTrace(); 
	} 
    }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup();
    filename = "File"+collection_; 

  }



  /**
Ensure that a NullPointerException occurs if argument #1 of IFSFile(AS400, IFSFile, String) is null.
**/
  public void Var001()
  {
    try
    {
      IFSFile dir = new IFSFile((AS400) null,
                                new IFSFile(systemObject_, "/Dir"), filename);
      failed("Exception didn't occur."+dir);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that a NullPointerException occurs if argument #2 of IFSFile(AS400, IFSFile, String) is null.
**/
  public void Var002()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, (IFSFile) null, filename);
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "directory");
    }
  }

/**
Ensure that a NullPointerException occurs if argument #3 of IFSFile(AS400, IFSFile, String) is null.
**/
  public void Var003()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/Directory");
      IFSFile file = new IFSFile(systemObject_, dir, (String) null);
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Test IFSFile(AS400, IFSFile, String) with a directory of '/Directory' and
file of 'File'.
**/
  public void Var004()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/Directory");
      IFSFile file = new IFSFile(systemObject_, dir, filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getAbsolutePath().equals(file.getPath()) &&
             file.getName().equals(filename));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, IFSFile, String) with a directory of '/' and
file of 'File'.
**/
  public void Var005()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/");
      IFSFile file = new IFSFile(systemObject_, dir, filename);
      assertCondition(file.getAbsolutePath().equals("/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, IFSFile, String) with a directory of 'Directory' and
file of 'File'.
**/
  public void Var006()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "Directory");
      IFSFile file = new IFSFile(systemObject_, dir, filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getAbsolutePath().equals(file.getPath()) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, IFSFile, String) with a directory of '/Directory/' and
file of 'File'.
**/
  public void Var007()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/Directory/");
      IFSFile file = new IFSFile(systemObject_, dir, filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that a NullPointerException occurs if argument #1 of IFSFile(AS400, String) is null.
**/
  public void Var008()
  {
    try
    {
      IFSFile file = new IFSFile((AS400) null, "/Directory");
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that a NullPointerException occurs if argument #2 of IFSFile(AS400, String) is null.
**/
  public void Var009()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, (String) null);
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "path");
    }
  }

/**
Test IFSFile(AS400, String) with a path of '/Directory'.
**/
  public void Var010()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/Directory");
      assertCondition(file.getAbsolutePath().equals("/Directory") &&
             file.getName().equals("Directory") &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, String) with a path of '/'.
**/
  public void Var011()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      assertCondition(file.getAbsolutePath().equals("/") &&
             file.getName().equals("") &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, String) with a path of 'File'.
**/
  public void Var012()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, filename);
      assertCondition(file.getAbsolutePath().equals("/"+filename) &&
             file.getAbsolutePath().equals(file.getPath()) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that a NullPointerException occurs if argument #1 of IFSFile(AS400, String, String) is null.
**/
  public void Var013()
  {
    try
    {
      IFSFile file = new IFSFile((AS400) null, "/Directory", filename);
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that a NullPointerException occurs if argument #2 of IFSFile(AS400, String, String) is null.
**/
  public void Var014()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, (String) null, filename);
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "directory");
    }
  }

/**
Ensure that a NullPointerException occurs if argument #3 of IFSFile(AS400, String, String) is null.
**/
  public void Var015()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/Directory", (String) null);
      failed("Exception didn't occur."+file);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Test IFSFile(AS400, String, String) with a directory of '/Directory' and
file of 'File'.
**/
  public void Var016()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/Directory", filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, String, String) with a directory of '/' and
file of 'File'.
**/
  public void Var017()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/", filename);
      assertCondition(file.getAbsolutePath().equals("/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(AS400, String, String) with a directory of '/Directory/' and
file of 'File'.
**/
  public void Var018()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, "/Directory/", filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileInputStream(AS400, String) is null.
**/
  public void Var019()
  {
    try
    {
      IFSFileInputStream is = new IFSFileInputStream((AS400) null,
                                                     ifsPathName_);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSFileInputStream(AS400, String) is null.
**/
  public void Var020()
  {
    try
    {
      IFSFileInputStream is = new IFSFileInputStream(systemObject_,
                                                     (String) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSFileInputStream(AS400, String) if the file
doesn't exist.
**/
  public void Var021()
  {
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, "/NoDirectory/NoFile");
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", "/NoDirectory/NoFile");
    }
  }

/**
Test IFSFileInputStream(AS400, String) with existing file "/Directory/File".
**/
  public void Var022()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String) will treat relative path names as being relative to the root directory.
**/
  public void Var023()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_,
                               (ifsPathNameX).substring(1,(ifsPathNameX).length()));
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String) does share file access with
other readers and writers.
**/
  public void Var024()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);

      
      checkReadWriteAccess(ifsPathNameX);

        is1.close();
        succeeded();

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileInputStream(AS400, String, int) is invalid.
**/
  public void Var025()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFileInputStream is =
            new IFSFileInputStream(systemObject_, ifsPathNameX,
                                   IFSFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.."+is);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileInputStream(AS400, String, int) is null.
**/
  public void Var026()
  {
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream((AS400) null, ifsPathName_,
                               IFSFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSFileInputStream(AS400, String, int) is null.
**/
  public void Var027()
  {
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, (IFSFile) null,
                               IFSFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileInputStream(AS400, String, int) is invalid.
**/
  public void Var028()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream in =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur."+in);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFileInputStream in =
            new IFSFileInputStream(systemObject_, ifsPathNameX,
                                   IFSFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.."+in);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSFileInputStream(AS400, String, int) if the file
doesn't exist.
**/
  public void Var029()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_ALL);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Test IFSFileInputStream(AS400, String, int) with existing file
"/Directory/File".
**/
  public void Var030()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_ALL);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


/**
Ensure that IFSFileInputStream(AS400, String, int) will treat relative path names as being relative to the root directory.
**/
  public void Var031()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_,
                               (ifsPathNameX).substring(1, (ifsPathNameX).length()),
                               IFSFileInputStream.SHARE_ALL);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String, SHARE_ALL) does share file
access with other readers and writers.
**/
  public void Var032()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_ALL);
      checkReadWriteAccess(ifsPathNameX);
      
       is1.close();
       succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String, SHARE_NONE) doesn't share file
access with readers or writers.
**/
  public void Var033()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileInputStream is1 = null;
    IFSFileInputStream is2 = null; 
    IFSFileOutputStream os2 = null;
    try
    {
      is1 = new IFSFileInputStream(systemObject_, ifsPathNameX,
                                   IFSFileInputStream.SHARE_NONE);
      is2 = new IFSFileInputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur."+is1);
      is2.close();
      is1.close();
    }
    catch(Exception e1)
    {
      // IFSFileInputStream throws an  IOException with the
      // SHARING_VIOLATION return code as the detail message.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          os2 = new IFSFileOutputStream(systemObject_, ifsPathNameX);
          failed("Exception didn't occur..");
          os2.close();
          is1.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e1, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally 
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String, SHARE_READERS) does share file
access with other readers.
**/
  public void Var034()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_READERS);

      checkReadAccess(ifsPathNameX);
        is1.close();
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String, SHARE_READERS) doesn't share file
access with writers.
**/
  public void Var035()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileInputStream  is = null; 
    IFSFileOutputStream os = null; 
    try
    {
       is =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_READERS);
       os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur."+is+os);
      os.close();
      is.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared ExtendedIOException is thrown.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally 
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os != null) os.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String, SHARE_WRITERS) does share file
access with other writers.
**/
  public void Var036()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_WRITERS);

      checkWriteAccess(ifsPathNameX) ;
      is.close();
      succeeded();
    }

    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, String, SHARE_WRITERS) doesn't share file
access with readers.
**/
  public void Var037()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileInputStream  is1 = null; 
    IFSFileInputStream  is2 = null; 
    try
    {
       is1 =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_WRITERS);
       is2 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur."+is1+is2);
      is2.close();
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally 
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileInputStream(AS400, IFSFile, int) is null.
**/
  public void Var038()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileInputStream is =
        new IFSFileInputStream((AS400) null, file,
                               IFSFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSFileInputStream(AS400, IFSFile, int) is null.
**/
  public void Var039()
  {
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, (IFSFile) null,
                               IFSFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileInputStream(AS400, IFSFile, int) is invalid.
**/
  public void Var040()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream in =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur."+in);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException"))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSFileInputStream in =
            new IFSFileInputStream(systemObject_, file,
                                   IFSFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.."+in);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSFileInputStream(AS400, IFSFile, int) if the file
doesn't exist.
**/
  public void Var041()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_ALL);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Test IFSFileInputStream(AS400, IFSFile, int) with existing file.
**/
  public void Var042()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_ALL);
      is.close();
      assertCondition(true, "file="+file); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, IFSFile, int) will treat relative path names as being relative to the root directory.
**/
  public void Var043()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file =
        new IFSFile(systemObject_,
                    (ifsPathNameX).substring(1,(ifsPathNameX).length()));
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_ALL );
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, IFSFile, SHARE_ALL) does share file
access with other readers and writers.
**/
  public void Var044()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_ALL);
      checkReadWriteAccess(ifsPathNameX);
      is1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


/**
Ensure that IFSFileInputStream(AS400, IFSFile, SHARE_NONE) doesn't share file
access with readers or writers.
**/
  public void Var045()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileInputStream is1 = null;
    IFSFileInputStream is2 = null; 
    IFSFileOutputStream os2 = null; 
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is1 = new IFSFileInputStream(systemObject_, file,
                                   IFSFileInputStream.SHARE_NONE);
      is2 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur."+is1);
      is2.close();
      is1.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          is1.close();
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          is1 = new IFSFileInputStream(systemObject_, file,
                                       IFSFileInputStream.SHARE_NONE);
          os2 =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          failed("Exception didn't occur..");
          os2.close();
          is1.close();
        }
        catch(Exception e2)
        {
          // If a file can't be shared the mapped drive will treat it as if it
          // doesn't exist.
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally 
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, IFSFile, SHARE_READERS) does share file
access with other readers.
**/
  public void Var046()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathNameX,
                               IFSFileInputStream.SHARE_READERS);
      checkReadAccess(ifsPathNameX);
      is1.close();
      assertCondition(true, "file="+file); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, IFSFile, SHARE_READERS) doesn't share file
access with writers.
**/
  public void Var047()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream os = null;
    IFSFileInputStream  is = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_READERS);
      os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os.close();
      failed("Exception didn't occur.");
      is.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally 
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os != null) os.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, IFSFile, SHARE_WRITERS) does share file
access with other writers.
**/
  public void Var048()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_WRITERS);
      checkWriteAccess(ifsPathNameX);

      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(AS400, IFSFile, SHARE_WRITERS) doesn't share file
access with readers.
**/
  public void Var049()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileInputStream is1 = null;
    IFSFileInputStream is2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is1 =
        new IFSFileInputStream(systemObject_, file,
                               IFSFileInputStream.SHARE_WRITERS);
      is2 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is2.close();
      failed("Exception didn't occur.");
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }


/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileInputStream(IFSFileDescriptor) is null.
**/
  public void Var050()
  {
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream((IFSFileDescriptor) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "fd");
    }
  }

/**
Test IFSFileInputStream(IFSFileDescriptor).
**/
  public void Var051()
  {
    byte[] dataIn = { 0, 1, 2, 4, 8, 16, 32, 64, (byte) 128, (byte) 255 };
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, dataIn);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      IFSFileInputStream is2 =
        new IFSFileInputStream(is1.getFD());

      assertCondition(is1.read() == dataIn[0] &&
             is2.read() == dataIn[1] &&
             is1.read() == dataIn[2]);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileOutputStream(AS400, String) is null.
**/
  public void Var052()
  {
    try
    {
      IFSFileOutputStream os = new IFSFileOutputStream((AS400) null,
                                                     ifsPathName_);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSFileOutputStream(AS400, String) is null.
**/
  public void Var053()
  {
    try
    {
      IFSFileOutputStream os = new IFSFileOutputStream(systemObject_,
                                                     (String) null);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Ensure that IFSFileOutputStream(AS400, String) creates a file if it doesn't exist.
**/
  public void Var054()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os.close();
      assertCondition(checkFileExists(ifsPathNameX));
    }
    catch(Exception e)
    {
      failed(e);
    }
    ///System.out.println ("Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String) replaces an existing file.
**/
  public void Var055()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os.write(1);
      os.close();

      assertCondition(checkFileLength(ifsPathNameX) == 1) ;


    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String) replaces existing file where the file name is relative to the root directory.
**/
  public void Var056()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_,
                                (ifsPathNameX).substring(1,(ifsPathNameX).length()));
      os.write(1);
      os.close();
      assertCondition(checkFileLength(ifsPathNameX) == 1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String) does share file access with
other readers and writers.
**/
  public void Var057()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      checkReadWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileOutputStream(AS400, String, int, boolean) is invalid.
**/
  public void Var058()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                               IFSFileOutputStream.SHARE_NONE - 1, true);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFileOutputStream os =
            new IFSFileOutputStream(systemObject_, ifsPathNameX,
                                   IFSFileOutputStream.SHARE_ALL + 1, true);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileOutputStream(AS400, String, int, boolean) is null.
**/
  public void Var059()
  {
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream((AS400) null, ifsPathName_,
                               IFSFileOutputStream.SHARE_NONE, true);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSFileOutputStream(AS400, String, int, boolean) is null.
**/
  public void Var060()
  {
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, (IFSFile) null,
                               IFSFileOutputStream.SHARE_NONE, true);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileOutputStream(AS400, String, int, boolean) is invalid.
**/
  public void Var061()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                               IFSFileOutputStream.SHARE_NONE - 1, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException"))
      {
        try
        {
          IFSFileOutputStream os =
            new IFSFileOutputStream(systemObject_, ifsPathNameX,
                                   IFSFileOutputStream.SHARE_ALL + 1, true);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String, SHARE_ALL, boolean) does share
 file access with other readers and writers.
**/
  public void Var062()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                               IFSFileOutputStream.SHARE_ALL, true);
      checkReadWriteAccess(ifsPathNameX);


      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String, SHARE_NONE, boolean) doesn't
 share file access with readers or writers.
**/
  public void Var063()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream os1 = null;
    IFSFileOutputStream os2 = null;
    IFSFileInputStream  is  = null;
    try
    {
      os1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                                IFSFileOutputStream.SHARE_NONE, false);
      is =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception1 didn't occur.");
      os1.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          os1.close();
          os1 = new IFSFileOutputStream(systemObject_, ifsPathNameX,
                                       IFSFileOutputStream.SHARE_NONE, false);
          os2 =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          os2.close();
          failed("Exception2 didn't occur..");
          os1.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String, SHARE_READERS, boolean) does
 share file access with other readers.
**/
  public void Var064()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                              IFSFileOutputStream.SHARE_READERS, false);
      checkReadAccess(ifsPathNameX);
      os.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String, SHARE_READERS, boolean) doesn't
 share file access with writers.
**/
  public void Var065()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream os1 =null;
    IFSFileOutputStream os2 =null;
    try
    {
      os1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                               IFSFileOutputStream.SHARE_READERS, false);
      os2 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os2.close();
      os1.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String, SHARE_WRITERS, boolean) does
 share file access with other writers.
**/
  public void Var066()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                               IFSFileOutputStream.SHARE_WRITERS, false);

      checkWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, String, SHARE_WRITERS, boolean) doesn't share file
access with readers.
**/
  public void Var067()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream is1 = null;
    IFSFileInputStream is2 = null;
    try
    {
      is1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX,
                               IFSFileOutputStream.SHARE_WRITERS, false);
      is2 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is2.close();
      failed("Exception didn't occur.");
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileOutputStream(AS400, IFSFile, int, boolean) is invalid.
**/
  public void Var068()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_NONE - 1, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSFileOutputStream os =
            new IFSFileOutputStream(systemObject_, file,
                                   IFSFileOutputStream.SHARE_ALL + 1, false);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileOutputStream(AS400, IFSFile, int, boolean) is null.
**/
  public void Var069()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileOutputStream os =
        new IFSFileOutputStream((AS400) null, file,
                               IFSFileOutputStream.SHARE_NONE, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSFileOutputStream(AS400, IFSFile, int, boolean) is null.
**/
  public void Var070()
  {
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, (IFSFile) null,
                               IFSFileOutputStream.SHARE_NONE, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSFileOutputStream(AS400, IFSFile, int, boolean) is invalid.
**/
  public void Var071()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_NONE - 1, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSFileOutputStream os =
            new IFSFileOutputStream(systemObject_, file,
                                   IFSFileOutputStream.SHARE_ALL + 1, false);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, IFSFile, SHARE_ALL, boolean) does share file
access with other readers and writers.
**/
  public void Var072()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_ALL, false);
      checkReadWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, IFSFile, SHARE_NONE, boolean) doesn't share file
access with readers or writers.
**/
  public void Var073()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream os1 = null;
    IFSFileInputStream is   = null;
    IFSFileOutputStream os2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      os1 = new IFSFileOutputStream(systemObject_, file,
                                   IFSFileOutputStream.SHARE_NONE, false);
      is =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      os1.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          os1.close();
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          os1 = new IFSFileOutputStream(systemObject_, file,
                                       IFSFileOutputStream.SHARE_NONE, false);
          os2 =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          os2.close();
          failed("Exception didn't occur..");
          os1.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, IFSFile, SHARE_READERS, boolean) does share file
access with other readers.
**/
  public void Var074()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_READERS, false);
      checkReadAccess(ifsPathNameX); 
      os.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, IFSFile, SHARE_READERS, boolean) doesn't share file access with writers.
**/
  public void Var075()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream os1 = null;
    IFSFileOutputStream os2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      os1 =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_READERS, false);
      os2 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os2.close();
      failed("Exception didn't occur.");
      os1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, IFSFile, SHARE_WRITERS, boolean) does share file
access with other writers.
**/
  public void Var076()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_WRITERS, false);

      checkWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileOutputStream(AS400, IFSFile, SHARE_WRITERS, boolean) doesn't share file access with readers.
**/
  public void Var077()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSFileOutputStream is1 = null;
    IFSFileInputStream is2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is1 =
        new IFSFileOutputStream(systemObject_, file,
                               IFSFileOutputStream.SHARE_WRITERS, false);
      is2 =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is2.close();
      failed("Exception didn't occur.");
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSFileOutputStream(IFSFileDescriptor) is null.
**/
  public void Var078()
  {
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream((IFSFileDescriptor) null);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "fd");
    }
  }

/**
Test IFSFileOutputStream(IFSFileDescriptor).
**/
  public void Var079()
  {
    byte[] dataIn = { 0, 1, 2, 4, 8, 16, 32, 64, (byte) 128, (byte) 255 };
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, dataIn);
    try
    {
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      IFSFileOutputStream os2 =
        new IFSFileOutputStream(os1.getFD());
      os1.write(23);
      os2.write(24);
      os1.close();
      os2.close();
      assertCondition(checkExpectedRead2(ifsPathNameX, 23, 24)); 
        }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSRandomAccessFile(AS400, String, Strng) is null.
**/
  public void Var080()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile((AS400) null, ifsPathName_, "rw");
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSRandomAccessFile(AS400, String, String) is null.
**/
  public void Var081()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, (String) null, "rw");
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #3 of IFSRandomAccessFile(AS400, String, String) is null.
**/
  public void Var082()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, (String) null);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "mode");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSRandomAccessFile(AS400, String, String) is invalid.
**/
  public void Var083()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "a");
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSRandomAccessFile(AS400, String, String) if the file
doesn't exist and the mode is "r".
**/
  public void Var084()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r");
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Ensure that a file cannot be written to if opened by
IFSRandomAccessFile(AS400, String, String) in read-only mode.
**/
 public void Var085()
 {
   String ifsPathNameX = ifsPathName_ + getVariation();
   createFile(ifsPathNameX);
   IFSRandomAccessFile raf=null;
   try
   {
     raf =
       new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r");
     raf.write(1);
     failed("Exception didn't occur.");
   }
   catch(Exception e)
   {
     assertExceptionIs(e, "ExtendedIOException", ExtendedIOException.ACCESS_DENIED);
   }
   finally
   {
     // Need this finally, otherwise the files may not get closed
     try 
     {
       if (raf != null) raf.close();
     }
     catch (Exception e3) {}
   }
   deleteFile(ifsPathNameX);
 }


/**
Ensure that a file is created by IFSRandomAccessFile(AS400, String, String)
if the file doesn't exist and the mode is "w".  Also, ensure that the file
cannot be read from.
**/
  public void Var086()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathNameX, "w");
      //if (isApplet_ || linux_)
      //{
        IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
        if (file.exists())
        {
          raf.read();
          //failed("Exception didn't occur..");
          notApplicable("Exception didn't occur."); // Bug in OS/400, will not be fixed
        }
        else
        {
          failed("File wasn't created.");
        }
      //}
      //else
      //{
      //  File file = new File(convertToPCName(ifsPathNameX));
      //  if (file.exists())
      //  {
      //    raf.read();
      //    //failed("Exception didn't occur..");
      //    notApplicable("Exception didn't occur."); // Bug in OS/400, will not be fixed
      //  }
      //  else
      //  {
      //    failed("File wasn't created.");
      //  }
      // }
      raf.close();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        "ExtendedIOException.ACCESS_DENIED");
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (raf != null) raf.close();
      }
      catch (Exception e3) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by IFSRandomAccessFile(AS400, String, String)
if the file doesn't exist and the mode is "rw".
**/
  public void Var087()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathNameX, "rw");
      raf.close();
      assertCondition(checkFileExists(ifsPathNameX));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String) does share file
access with other readers and writers.
**/
  public void Var088()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "rw");
      checkReadWriteAccess(ifsPathNameX);

      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSRandomAccessFile(AS400, String, String, int, int) is null.
**/
  public void Var089()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile((AS400) null, ifsPathName_, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSRandomAccessFile(AS400, String, String, int, int) is null.
**/
  public void Var090()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, (String) null, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #3 of IFSRandomAccessFile(AS400, String, String, int, int) is null.
**/
  public void Var091()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, (String) null,
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "mode");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSRandomAccessFile(AS400, String, String, int, int) is invalid.
**/
  public void Var092()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "a",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
  }

/**
Ensure that all valid values for argument #3 of IFSRandomAccessFile(AS400, String, String, int, int) are accepted.
**/
  public void Var093()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "w",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      IFSRandomAccessFile raf3 =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf1.close();
      raf2.close();
      raf3.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #4 of IFSRandomAccessFile(AS400, String, String, int, int) is invalid.
**/
  public void Var094()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_NONE - 1,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSRandomAccessFile raf =
            new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                    IFSFileOutputStream.SHARE_ALL + 1,
                                    IFSRandomAccessFile.OPEN_OR_CREATE);
          failed("Exception didn't occur.."+raf);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #5 of IFSRandomAccessFile(AS400, String, String, int, int) is invalid.
**/
  public void Var095()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE - 1);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSRandomAccessFile raf =
            new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                    IFSFileOutputStream.SHARE_ALL,
                                    IFSRandomAccessFile.REPLACE_OR_FAIL + 1);
          failed("Exception didn't occur.."+raf);
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
    deleteFile(ifsPathNameX);
  }


/**
Ensure that a file cannot be written to if opened by
IFSRandomAccessFile(AS400, String, String, int, int) in read-only mode.
**/
 public void Var096()
 {
   String ifsPathNameX = ifsPathName_ + getVariation();
   createFile(ifsPathNameX);
   IFSRandomAccessFile raf = null;
   try
   {
     raf =
       new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                               IFSRandomAccessFile.SHARE_ALL,
                               IFSRandomAccessFile.OPEN_OR_FAIL);
     raf.write(1);
     failed("Exception didn't occur.");
     raf.close();
   }
   catch(Exception e)
   {
     assertExceptionIs(e, "ExtendedIOException", ExtendedIOException.ACCESS_DENIED);
   }
   finally
   {
     // Need this finally, otherwise the files may not get closed
     try 
     {
       if (raf != null) raf.close();
     }
     catch (Exception e3) {}
   }

   deleteFile(ifsPathNameX);
 }

/**
Ensure that a file cannot be read from if created by
IFSRandomAccessFile(AS400, String, String, int, int) in write-only mode.
**/
  public void Var097()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    try
    {
      raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "w",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      //if (isApplet_ || linux_)
      //{
        IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
        if (file.exists())
        {
          raf.read();
          //failed("Exception didn't occur..");
          notApplicable("Exception didn't occur."); // Bug in OS/400, will not be fixed
        }
        else
        {
          failed("File wasn't created.");
        }
      //}
      //else
      // {
      //  File file = new File(convertToPCName(ifsPathNameX));
      //  if (file.exists())
      //  {
      //    raf.read();
          //failed("Exception didn't occur..");
      //    notApplicable("Exception didn't occur."); // Bug in OS/400, will not be fixed
      //  }
      //  else
      //  {
      //    failed("File wasn't created.");
      //  }
      // }
      raf.close();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException", ExtendedIOException.ACCESS_DENIED);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (raf != null) raf.close();
      }
      catch (Exception e3) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that an IOException is thrown by
IFSRandomAccessFile(AS400, String, String, int, int) if the file exists and the
existence option is FAIL_OR_CREATE.
**/
  public void Var098()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    try
    {
      raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.FAIL_OR_CREATE);
      raf.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.DIR_ENTRY_EXISTS);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (raf != null) raf.close();
      }
      catch (Exception e3) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by
IFSRandomAccessFile(AS400, String, String, int, int) if the file does not
exist and the existence option is FAIL_OR_CREATE.
**/
  public void Var099()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.FAIL_OR_CREATE);
      raf.close();
      
      assertCondition(checkFileExists(ifsPathNameX));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that the file is opened by
IFSRandomAccessFile(AS400, String, String, int, int) if the file exists and the
existence option is OPEN_OR_CREATE.
**/
  public void Var100()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by
IFSRandomAccessFile(AS400, String, String, int, int) if the file does not
exist and the existence option is OPEN_OR_CREATE.
**/
  public void Var101()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf.close();
      if (isApplet_ || linux_)
      {
        IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
        assertCondition(file.exists());
      }
      else
      {
        
        assertCondition(checkFileExists(ifsPathNameX), "File "+ifsPathNameX+" does not exist");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that the file is opened by
IFSRandomAccessFile(AS400, String, String, int, int) if the file exists and the
existence option is OPEN_OR_FAIL.
**/
  public void Var102()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_FAIL);
      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSRandomAccessFile(AS400, String, String, int, int) if
the file doesn't exist and the existence option is OPEN_OR_FAIL.
**/
  public void Var103()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_FAIL);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Ensure that the file is replaced by
IFSRandomAccessFile(AS400, String, String, int, int) if the file exists and the
existence option is REPLACE_OR_CREATE.
**/
  public void Var104()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_CREATE);
      raf.close();
      if (isApplet_ || linux_)
      {
        IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
        assertCondition(file.length() == 0);
      }
      else
      {
        File file = new File(convertToPCName(ifsPathNameX));
        assertCondition(file.length() == 0);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by
IFSRandomAccessFile(AS400, String, String, int, int) if the file does not
exist and the existence option is REPLACE_OR_CREATE.
**/
  public void Var105()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_CREATE);
      raf.close();
      assertCondition(checkFileExists(ifsPathNameX));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that the file is replaced by
IFSRandomAccessFile(AS400, String, String, int, int) if the file exists and the
existence option is REPLACE_OR_CREATE.
**/
  public void Var106()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_FAIL);
      raf.close();
      if (isApplet_ || linux_)
      {
        IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
        assertCondition(file.length() == 0);
      }
      else
      {
        File file = new File(convertToPCName(ifsPathNameX));
        assertCondition(file.length() == 0);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSRandomAccessFile(AS400, String, String, int, int) if
the file doesn't exist, the mode is "r", and the existence option is
REPLACE_OR_FAIL.
**/
  public void Var107()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_FAIL);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String, int, int) does share file
access with other readers and writers.
**/
  public void Var108()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      checkReadWriteAccess(ifsPathNameX);

      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String, int, int) doesn't share file access with readers or writers if the share option is SHARE_NONE.
**/
  public void Var109()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    IFSFileInputStream is   = null;
    IFSFileOutputStream os  = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                    IFSFileOutputStream.SHARE_NONE,
                                    IFSRandomAccessFile.OPEN_OR_CREATE);
      is =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      raf.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          raf.close();
          raf = new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                        IFSFileOutputStream.SHARE_NONE,
                                        IFSRandomAccessFile.OPEN_OR_CREATE);
            os =
              new IFSFileOutputStream(systemObject_, ifsPathNameX);
            os.close();
          failed("Exception didn't occur..");
          raf.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os != null) os.close();
      }
      catch (Exception e4) {}
      try
      {
        if (raf != null) raf.close();
      }
      catch (Exception e4) {}
    }

    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String, int, int) does share file
access with other readers if the share option is SHARE_READERS.
**/
  public void Var110()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                               IFSFileOutputStream.SHARE_READERS,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      checkReadAccess(ifsPathNameX); 
      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String, int, int) doesn't share
file access with writers if the share option is SHARE_READERS
**/
  public void Var111()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    IFSFileOutputStream os  = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                    IFSFileOutputStream.SHARE_READERS,
                                    IFSRandomAccessFile.OPEN_OR_CREATE);
      os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (os != null) os.close();
      }
      catch (Exception e3) {}
      try
      {
        if (raf != null) raf.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String, int, int) does share file
access with other writers if the share option is SHARE_WRITERS.
**/
  public void Var112()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                               IFSFileOutputStream.SHARE_WRITERS,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      checkWriteAccess(ifsPathNameX);

      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, String, String, int, int) doesn't share
file access with readers if the share option is SHARE_WRITERS.
**/
  public void Var113()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    IFSFileInputStream is   = null;
    try
    {
      raf =
        new IFSRandomAccessFile(systemObject_, ifsPathNameX, "r",
                                IFSRandomAccessFile.SHARE_WRITERS,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      is =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      raf.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (raf != null) raf.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that NullPointerException is thrown if argument #1 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) is null.
**/
  public void Var114()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile((AS400) null, file, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #2 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) is null.
**/
  public void Var115()
  {
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, (IFSFile) null, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that NullPointerException is thrown if argument #3 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) is null.
**/
  public void Var116()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, (String) null,
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "mode");
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) is invalid.
**/
  public void Var117()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "a",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #4 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) is invalid.
**/
  public void Var118()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_NONE - 1,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSRandomAccessFile raf =
            new IFSRandomAccessFile(systemObject_, file, "r",
                                    IFSFileOutputStream.SHARE_ALL + 1,
                                    IFSRandomAccessFile.OPEN_OR_CREATE);
          failed("Exception didn't occur.."+raf);
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
    deleteFile(ifsPathNameX);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument #5 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) is invalid.
**/
  public void Var119()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE - 1);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSRandomAccessFile raf =
            new IFSRandomAccessFile(systemObject_, file, "r",
                                    IFSFileOutputStream.SHARE_ALL,
                                    IFSRandomAccessFile.REPLACE_OR_FAIL + 1);
          failed("Exception didn't occur.."+raf);
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
    deleteFile(ifsPathNameX);
  }


/**
Ensure that a file cannot be written to if opened by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) in read-only mode.
**/
 public void Var120()
 {
   String ifsPathNameX = ifsPathName_ + getVariation();
   createFile(ifsPathNameX);
   IFSRandomAccessFile raf = null;
   try
   {
     IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
     raf =
       new IFSRandomAccessFile(systemObject_, file, "r",
                               IFSRandomAccessFile.SHARE_ALL,
                               IFSRandomAccessFile.OPEN_OR_FAIL);
     raf.write(1);
     failed("Exception didn't occur.");
     raf.close();
   }
   catch(Exception e)
   {
     assertExceptionIs(e, "ExtendedIOException", ExtendedIOException.ACCESS_DENIED);
   }
   finally
   {
     // Need this finally, otherwise the files may not get closed
     try
     {
       if (raf != null) raf.close();
     }
     catch (Exception e4) {}
   }

   deleteFile(ifsPathNameX);
 }

/**
Ensure that a file cannot be read from if created by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) in write-only mode.
**/
  public void Var121()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathNameX);
      raf =
        new IFSRandomAccessFile(systemObject_, file1, "w",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      // if (isApplet_ || linux_)
      // {
        IFSFile file2 = new IFSFile(systemObject_, ifsPathNameX);
        if (file2.exists())
        {
          raf.read();
          //failed("Exception didn't occur..");
          notApplicable("Exception didn't occur."); // Bug in OS/400, will not be fixed
        }
        else
        {
          failed("File wasn't created.");
        }
      //}
      //else
      //{
      //  File file2 = new File(convertToPCName(ifsPathNameX));
      //  if (file2.exists())
      //  {
      //    raf.read();
          //failed("Exception didn't occur..");
      //    notApplicable("Exception didn't occur."); // Bug in OS/400, will not be fixed
      //  }
     //   else
     //   {
     //     failed("File wasn't created.");
     //   }
      // }
      raf.close();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException", ExtendedIOException.ACCESS_DENIED);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (raf != null) raf.close();
      }
      catch (Exception e3) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that an ExtendedIOException is thrown by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file exists and the
existence option is FAIL_OR_CREATE.
**/
  public void Var122()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.FAIL_OR_CREATE);
      raf.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.DIR_ENTRY_EXISTS);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (raf != null) raf.close();
      }
      catch (Exception e3) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file does not
exist and the existence option is FAIL_OR_CREATE.
**/
  public void Var123()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file1, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.FAIL_OR_CREATE);
      raf.close();
      assertCondition(checkFileExists(ifsPathNameX)); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that the file is opened by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file exists and the
existence option is OPEN_OR_CREATE.
**/
  public void Var124()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file does not
exist and the existence option is OPEN_OR_CREATE.
**/
  public void Var125()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file1, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf.close();
      assertCondition(checkFileExists(ifsPathNameX)); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that the file is opened by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file exists and the
existence option is OPEN_OR_FAIL.
**/
  public void Var126()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_FAIL);
      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSRandomAccessFile(AS400, IFSFile, String, int, int) if
the file doesn't exist and the existence option is OPEN_OR_FAIL.
**/
  public void Var127()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_FAIL);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Ensure that the file is replaced by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file exists and the
existence option is REPLACE_OR_CREATE.
**/
  public void Var128()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");   // ???  Was: ifsPathName_  - JPL
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file1, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_CREATE);
      raf.close();
      assertCondition(checkFileLength(ifsPathNameX) == 0); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a file is created by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file does not
exist and the existence option is REPLACE_OR_CREATE.
**/
  public void Var129()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file1, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_CREATE);
      raf.close();
      assertCondition(checkFileExists(ifsPathNameX)); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that the file is replaced by
IFSRandomAccessFile(AS400, IFSFile, String, int, int) if the file exists and the
existence option is REPLACE_OR_CREATE.
**/
  public void Var130()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file1, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_FAIL);
      raf.close();
     
       assertCondition(checkFileLength(ifsPathNameX) == 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSRandomAccessFile(AS400, IFSFile, String, int, int) if
the file doesn't exist, the mode is "r", and the existence option is
REPLACE_OR_FAIL.
**/
  public void Var131()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.REPLACE_OR_FAIL);
      failed("Exception didn't occur."+raf);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Ensure that IFSRandomAccessFile(AS400, IFSFile, String, int, int) does share file
access with other readers and writers.
**/
  public void Var132()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      checkReadWriteAccess(ifsPathNameX);

      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, IFSFile, String, int, int) doesn't share file access with readers or writers if the share option is SHARE_NONE.
**/
  public void Var133()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    IFSFileInputStream is = null;
    IFSFileOutputStream os = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      raf = new IFSRandomAccessFile(systemObject_, file, "r",
                                    IFSFileOutputStream.SHARE_NONE,
                                    IFSRandomAccessFile.OPEN_OR_CREATE);
      is =
        new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      raf.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          raf.close();
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          raf = new IFSRandomAccessFile(systemObject_, file, "r",
                                        IFSFileOutputStream.SHARE_NONE,
                                        IFSRandomAccessFile.OPEN_OR_CREATE);
          os =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          os.close();
          failed("Exception didn't occur..");
          raf.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os != null) os.close();
      }
      catch (Exception e4) {}
      try
      {
        if (raf != null) raf.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, IFSFile, String, int, int) does share file
access with other readers if the share option is SHARE_READERS.
**/
  public void Var134()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                               IFSFileOutputStream.SHARE_READERS,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      checkReadAccess(ifsPathNameX); 
      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, IFSFile, String, int, int) doesn't share
file access with writers if the share option is SHARE_READERS
**/
  public void Var135()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    IFSFileOutputStream os = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      raf = new IFSRandomAccessFile(systemObject_, file, "r",
                                    IFSFileOutputStream.SHARE_READERS,
                                    IFSRandomAccessFile.OPEN_OR_CREATE);
      os =
          new IFSFileOutputStream(systemObject_, ifsPathNameX);
        os.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (os != null) os.close();
      }
      catch (Exception e3) {}
      try
      {
        if (raf != null) raf.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, IFSFile, String, int, int) does share file
access with other writers if the share option is SHARE_WRITERS.
**/
  public void Var136()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                               IFSFileOutputStream.SHARE_WRITERS,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      checkWriteAccess(ifsPathNameX);


      raf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSRandomAccessFile(AS400, IFSFile, String, int, int) doesn't share
file access with readers if the share option is SHARE_WRITERS.
**/
  public void Var137()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSRandomAccessFile raf = null;
    IFSFileInputStream  is  = null;
    
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      raf =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_WRITERS,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      is =
          new IFSFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      raf.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (raf != null) raf.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that all valid values for argument #3 of IFSRandomAccessFile(AS400, IFSFile, String, int, int) are accepted.
**/
  public void Var138()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, file, "w",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      IFSRandomAccessFile raf3 =
        new IFSRandomAccessFile(systemObject_, file, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf1.close();
      raf2.close();
      raf3.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileInputStream(AS400, String) is null.
   **/
  public void Var139()
  {
    try
    {
      IFSTextFileInputStream is = new IFSTextFileInputStream((AS400) null,
                                                     ifsPathName_);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileInputStream(AS400, String) is null.
   **/
  public void Var140()
  {
    try
    {
      IFSTextFileInputStream is = new IFSTextFileInputStream(systemObject_,
                                                     (String) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileInputStream(AS400, String) is null.
   **/
  public void Var141()
  {
    try
    {
      IFSTextFileInputStream is = new IFSTextFileInputStream(systemObject_,
                                                     (String) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

  /**
   Ensure that a FileNotFoundException having the file name as the detail
   message is thrown by IFSTextFileInputStream(AS400, String) if the file
   doesn't exist.
   **/
  public void Var142()
  {
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, "/NoDirectory/NoFile");
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", "/NoDirectory/NoFile");
    }
  }

  /**
   Test IFSTextFileInputStream(AS400, String) with existing file "/Directory/File".
   **/
  public void Var143()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String) will treat relative path names as being relative to the root directory.
   **/
  public void Var144()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_,
                               (ifsPathNameX).substring(1,(ifsPathNameX).length()));
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String) does share file access with
   other readers and writers.
   **/
  public void Var145()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is1 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      checkReadWriteAccess(ifsPathNameX);

        is1.close();
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileInputStream(AS400, String, int) is invalid.
   **/
  public void Var146()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSTextFileInputStream is =
            new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                                   IFSTextFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.."+is);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileInputStream(AS400, String, int) is null.
   **/
  public void Var147()
  {
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream((AS400) null, ifsPathName_,
                               IFSTextFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileInputStream(AS400, String, int) is null.
   **/
  public void Var148()
  {
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, (IFSFile) null,
                               IFSTextFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileInputStream(AS400, String, int) is invalid.
   **/
  public void Var149()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream in =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur."+in);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSTextFileInputStream in =
            new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                                   IFSTextFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.."+in);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that a FileNotFoundException having the file name as the detail
   message is thrown by IFSTextFileInputStream(AS400, String, int) if the file
   doesn't exist.
   **/
  public void Var150()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_ALL);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

  /**
   Test IFSTextFileInputStream(AS400, String, int) with existing file
   "/Directory/File".
   **/
  public void Var151()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_ALL);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


  /**
   Ensure that IFSTextFileInputStream(AS400, String, int) will treat relative path names as being relative to the root directory.
   **/
  public void Var152()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_,
                               (ifsPathNameX).substring(1, (ifsPathNameX).length()),
                               IFSTextFileInputStream.SHARE_ALL);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String, SHARE_ALL) does share file
   access with other readers and writers.
   **/
  public void Var153()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is1 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_ALL);
      checkReadWriteAccess(ifsPathNameX);

        is1.close();
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String, SHARE_NONE) doesn't share file
   access with readers or writers.
   **/
  public void Var154()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is1 = null;
    IFSTextFileInputStream is2 = null;
    IFSFileOutputStream    os2 = null;  
    try
    {
      is1 = new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                                   IFSTextFileInputStream.SHARE_NONE);
      is2 = new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur.");
      is2.close();
      is1.close();
    }
    catch(Exception e1)
    {
      // IFSTextFileInputStream throws an  IOException with the
      // SHARING_VIOLATION return code as the detail message.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          os2 = new IFSFileOutputStream(systemObject_, ifsPathNameX);
          failed("Exception didn't occur..");
          os2.close();
          is1.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e1, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String, SHARE_READERS) does share file
   access with other readers.
   **/
  public void Var155()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileInputStream is1 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_READERS);
      checkReadAccess(ifsPathNameX); 
        is1.close();
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String, SHARE_READERS) doesn't share file
   access with writers.
   **/
  public void Var156()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is = null;
    IFSFileOutputStream    os = null; 
    
    try
    {
      is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_READERS);
      os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur.");
      os.close();
      is.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared ExtendedIOException is thrown.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os != null) os.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String, SHARE_WRITERS) does share file
   access with other writers.
   **/
  public void Var157()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is = null;
    try
    {
      is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_WRITERS);
      checkWriteAccess(ifsPathNameX);

      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, String, SHARE_WRITERS) doesn't share file
   access with readers.
   **/
  public void Var158()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is1 = null;
    IFSTextFileInputStream is2 = null;
    try
    {
      is1 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_WRITERS);
      is2 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur.");
      is2.close();
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileInputStream(AS400, IFSFile, int) is null.
   **/
  public void Var159()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream((AS400) null, file,
                               IFSTextFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileInputStream(AS400, IFSFile, int) is null.
   **/
  public void Var160()
  {
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, (IFSFile) null,
                               IFSTextFileInputStream.SHARE_NONE);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileInputStream(AS400, IFSFile, int) is invalid.
   **/
  public void Var161()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream in =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_NONE - 1);
      failed("Exception didn't occur."+in);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException"))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSTextFileInputStream in =
            new IFSTextFileInputStream(systemObject_, file,
                                   IFSTextFileInputStream.SHARE_ALL + 1);
          failed("Exception didn't occur.."+in);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that a FileNotFoundException having the file name as the detail
   message is thrown by IFSTextFileInputStream(AS400, IFSFile, int) if the file
   doesn't exist.
   **/
  public void Var162()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_ALL);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

  /**
   Test IFSTextFileInputStream(AS400, IFSFile, int) with existing file.
   **/
  public void Var163()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_ALL);
      is.close();
      assertCondition(true, "file="+file); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, int) will treat relative path names as being relative to the root directory.
   **/
  public void Var164()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file =
        new IFSFile(systemObject_,
                    (ifsPathNameX).substring(1,(ifsPathNameX).length()));
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_ALL );
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, SHARE_ALL) does share file
   access with other readers and writers.
   **/
  public void Var165()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is1 =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_ALL);
      checkReadWriteAccess(ifsPathNameX);

      is1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, SHARE_NONE) doesn't share file
   access with readers or writers.
   **/
  public void Var166()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is1 = null;
    IFSTextFileInputStream is2 = null;
    IFSFileOutputStream    os2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is1 = new IFSTextFileInputStream(systemObject_, file,
                                   IFSTextFileInputStream.SHARE_NONE);
      is2 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      failed("Exception didn't occur.");
      is2.close();
      is1.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          is1.close();
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          is1 = new IFSTextFileInputStream(systemObject_, file,
                                       IFSTextFileInputStream.SHARE_NONE);
          os2 =
            new IFSFileOutputStream(systemObject_, ifsPathNameX);
          failed("Exception didn't occur..");
          os2.close();
          is1.close();
        }
        catch(Exception e2)
        {
          // If a file can't be shared the mapped drive will treat it as if it
          // doesn't exist.
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, SHARE_READERS) does share file
   access with other readers.
   **/
  public void Var167()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is1 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX,
                               IFSTextFileInputStream.SHARE_READERS);
      checkReadAccess(ifsPathNameX); 
      is1.close();
      assertCondition(true, "file="+file); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, SHARE_READERS) doesn't share file
   access with writers.
   **/
  public void Var168()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is = null;
    IFSFileOutputStream os    = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_READERS);
      os =
        new IFSFileOutputStream(systemObject_, ifsPathNameX);
      os.close();
      is.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os != null) os.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, SHARE_WRITERS) does share file
   access with other writers.
   **/
  public void Var169()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_WRITERS);
      checkWriteAccess(ifsPathNameX);


      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileInputStream(AS400, IFSFile, SHARE_WRITERS) doesn't share file
   access with readers.
   **/
  public void Var170()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileInputStream is1 = null;
    IFSTextFileInputStream is2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is1 =
        new IFSTextFileInputStream(systemObject_, file,
                               IFSTextFileInputStream.SHARE_WRITERS);
      is2 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      is2.close();
      failed("Exception didn't occur.");
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }


  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileInputStream(IFSFileDescriptor) is null.
   **/
  public void Var171()
  {
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream((IFSFileDescriptor) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "fd");
    }
  }

  /**
   Test IFSTextFileInputStream(IFSFileDescriptor).
   **/
  public void Var172()
  {
    byte[] dataIn = { 0, 1, 2, 4, 8, 16, 32, 64, (byte) 128, (byte) 255 };
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, dataIn);
    try
    {
      IFSTextFileInputStream is1 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      IFSTextFileInputStream is2 =
        new IFSTextFileInputStream(is1.getFD());

      assertCondition(is1.read() == dataIn[0] &&
             is2.read() == dataIn[1] &&
             is1.read() == dataIn[2]);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileOutputStream(AS400, String) is null.
   **/
  public void Var173()
  {
    try
    {
      IFSTextFileOutputStream os = new IFSTextFileOutputStream((AS400) null,
                                                       ifsPathName_);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileOutputStream(AS400, String) is null.
   **/
  public void Var174()
  {
    try
    {
      IFSTextFileOutputStream os = new IFSTextFileOutputStream(systemObject_,
                                                       (String) null);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String) creates a file if it doesn't exist.
   **/
  public void Var175()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
      os.close();
      assertCondition(checkFileExists(ifsPathNameX)); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String) replaces an existing file.
   **/
  public void Var176()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
      os.write(1);
      os.close();
      assertCondition(checkFileLength(ifsPathNameX) == 1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String) replaces existing file where the file name is relative to the root directory.
   **/
  public void Var177()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, "abcdefg");
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_,
                                (ifsPathNameX).substring(1,(ifsPathNameX).length()));
      os.write(1);
      os.close();
      assertCondition(checkFileLength(ifsPathNameX) == 1); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String) does share file access with
   other readers and writers.
   **/
  public void Var178()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
      checkReadWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileOutputStream(AS400, String, int, boolean) is invalid.
   **/
  public void Var179()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_NONE - 1, true);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSTextFileOutputStream os =
            new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                    IFSTextFileOutputStream.SHARE_ALL + 1, true);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileOutputStream(AS400, String, int, boolean) is null.
   **/
  public void Var180()
  {
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream((AS400) null, ifsPathName_,
                                IFSTextFileOutputStream.SHARE_NONE, true);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileOutputStream(AS400, String, int, boolean) is null.
   **/
  public void Var181()
  {
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, (IFSFile) null,
                                IFSTextFileOutputStream.SHARE_NONE, true);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileOutputStream(AS400, String, int, boolean) is invalid.
   **/
  public void Var182()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_NONE - 1, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException"))
      {
        try
        {
          IFSTextFileOutputStream os =
            new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                    IFSTextFileOutputStream.SHARE_ALL + 1, true);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String, SHARE_ALL, boolean) does share
   file access with other readers and writers.
   **/
  public void Var183()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_ALL, true);
      checkReadWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String, SHARE_NONE, boolean) doesn't
   share file access with readers or writers.
   **/
  public void Var184()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileOutputStream os1 = null;
    IFSTextFileInputStream   is = null;
    IFSTextFileOutputStream os2 = null;
    try
    {
      os1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_NONE, false);
      is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      os1.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          os1.close();
          os1 = new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                        IFSTextFileOutputStream.SHARE_NONE, false);
          os2 =
            new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
          os2.close();
          failed("Exception didn't occur..");
          os1.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String, SHARE_READERS, boolean) does
   share file access with other readers.
   **/
  public void Var185()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_READERS, false);
      checkReadAccess(ifsPathNameX); 
      os.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String, SHARE_READERS, boolean) doesn't
   share file access with writers.
   **/
  public void Var186()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileOutputStream os1 = null;
    IFSTextFileOutputStream os2 = null;
    try
    {
      os1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_READERS, false);
      os2 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
      os2.close();
      os1.close();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String, SHARE_WRITERS, boolean) does
   share file access with other writers.
   **/
  public void Var187()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSTextFileOutputStream os1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_WRITERS, false);
      checkWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, String, SHARE_WRITERS, boolean) doesn't share file
   access with readers.
   **/
  public void Var188()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileOutputStream is1 = null;
    IFSTextFileInputStream  is2 = null;
    try
    {
      is1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX,
                                IFSTextFileOutputStream.SHARE_WRITERS, false);
      is2 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      is2.close();
      failed("Exception didn't occur.");
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileOutputStream(AS400, IFSFile, int, boolean) is invalid.
   **/
  public void Var189()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_NONE - 1, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSTextFileOutputStream os =
            new IFSTextFileOutputStream(systemObject_, file,
                                    IFSTextFileOutputStream.SHARE_ALL + 1, false);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileOutputStream(AS400, IFSFile, int, boolean) is null.
   **/
  public void Var190()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream((AS400) null, file,
                                IFSTextFileOutputStream.SHARE_NONE, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   Ensure that NullPointerException is thrown if argument #2 of IFSTextFileOutputStream(AS400, IFSFile, int, boolean) is null.
   **/
  public void Var191()
  {
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, (IFSFile) null,
                                IFSTextFileOutputStream.SHARE_NONE, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

  /**
   Ensure that ExtendedIllegalArgumentException is thrown if argument #3 of IFSTextFileOutputStream(AS400, IFSFile, int, boolean) is invalid.
   **/
  public void Var192()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_NONE - 1, false);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          IFSTextFileOutputStream os =
            new IFSTextFileOutputStream(systemObject_, file,
                                    IFSTextFileOutputStream.SHARE_ALL + 1, false);
          failed("Exception didn't occur.."+os);
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
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, IFSFile, SHARE_ALL, boolean) does share file
   access with other readers and writers.
   **/
  public void Var193()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileOutputStream os1 =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_ALL, false);
      checkReadWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, IFSFile, SHARE_NONE, boolean) doesn't share file
   access with readers or writers.
   **/
  public void Var194()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileOutputStream os1 = null;
    IFSTextFileInputStream  is  = null; 
    IFSTextFileOutputStream os2 = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      os1 = new IFSTextFileOutputStream(systemObject_, file,
                                    IFSTextFileOutputStream.SHARE_NONE, false);
      is =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      is.close();
      failed("Exception didn't occur.");
      os1.close();
    }
    catch(Exception e1)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      if (exceptionIs(e1, "ExtendedIOException",
                      ExtendedIOException.SHARING_VIOLATION))
      {
        try
        {
          os1.close();
          IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
          os1 = new IFSTextFileOutputStream(systemObject_, file,
                                        IFSTextFileOutputStream.SHARE_NONE, false);
          os2 =
            new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
          os2.close();
          failed("Exception didn't occur..");
          os1.close();
        }
        catch(Exception e2)
        {
          assertExceptionIs(e2, "ExtendedIOException",
                            ExtendedIOException.SHARING_VIOLATION);
        }
      }
      else
      {
        failed(e1, "Incorrect exception information.");
      }
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is != null) is.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e4) {}
      try
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, IFSFile, SHARE_READERS, boolean) does share file
   access with other readers.
   **/
  public void Var195()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_READERS, false);
      checkReadAccess(ifsPathNameX); 
      os.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, IFSFile, SHARE_READERS, boolean) doesn't share file access with writers.
   **/
  public void Var196()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileOutputStream os1 = null;
    IFSTextFileOutputStream os2  = null;
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      os1 =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_READERS, false);
      os2 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
      os2.close();
      failed("Exception didn't occur.");
      os1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (os2 != null) os2.close();
      }
      catch (Exception e3) {}
      try
      {
        if (os1 != null) os1.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, IFSFile, SHARE_WRITERS, boolean) does share file
   access with other writers.
   **/
  public void Var197()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSTextFileOutputStream os1 =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_WRITERS, false);
      checkWriteAccess(ifsPathNameX);
      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that IFSTextFileOutputStream(AS400, IFSFile, SHARE_WRITERS, boolean) doesn't share file access with readers.
   **/
  public void Var198()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    IFSTextFileOutputStream is1 = null;
    IFSTextFileInputStream  is2 = null; 
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      is1 =
        new IFSTextFileOutputStream(systemObject_, file,
                                IFSTextFileOutputStream.SHARE_WRITERS, false);
      is2 =
        new IFSTextFileInputStream(systemObject_, ifsPathNameX);
      is2.close();
      failed("Exception didn't occur.");
      is1.close();
    }
    catch(Exception e)
    {
      // If a file can't be shared the mapped drive will treat it as if it
      // doesn't exist.
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.SHARING_VIOLATION);
    }
    finally
    {
      // Need this finally, otherwise the files may not get closed
      try 
      {
        if (is1 != null) is1.close();
      }
      catch (Exception e3) {}
      try
      {
        if (is2 != null) is2.close();
      }
      catch (Exception e4) {}
    }
    deleteFile(ifsPathNameX);
  }

  /**
   Ensure that NullPointerException is thrown if argument #1 of IFSTextFileOutputStream(IFSFileDescriptor) is null.
   **/
  public void Var199()
  {
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream((IFSFileDescriptor) null);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "fd");
    }
  }

  /**
   Test IFSTextFileOutputStream(IFSFileDescriptor).
   **/
  public void Var200()
  {
    byte[] dataIn = { 0, 1, 2, 4, 8, 16, 32, 64, (byte) 128, (byte) 255 };
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX, dataIn);
    try
    {
      IFSTextFileOutputStream os1 =
        new IFSTextFileOutputStream(systemObject_, ifsPathNameX);
      IFSTextFileOutputStream os2 =
        new IFSTextFileOutputStream(os1.getFD());
      os1.write(23);
      os2.write(24);
      os1.close();
      os2.close();
      
      assertCondition(checkExpectedRead2(ifsPathNameX, 23, 24)); 
      
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


// @A1a: Added variations 201 through 212.


/**
Ensure that NullPointerException is thrown if argument of IFSFileInputStream(IFSFile) is null.
**/
  public void Var201()
  {
    try
    {
      IFSFileInputStream is = new IFSFileInputStream((IFSFile) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSFileInputStream(IFSFile) if the file
doesn't exist.
**/
  public void Var202()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is =
        new IFSFileInputStream(file);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Test IFSFileInputStream(IFSFile) with existing file.
**/
  public void Var203()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is =
        new IFSFileInputStream(file);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(IFSFile) does share file
access with other readers and writers.
**/
  public void Var204()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is1 =
        new IFSFileInputStream(file);
      checkReadWriteAccess(ifsPathNameX);

      is1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


/**
Ensure that NullPointerException is thrown if argument of IFSFileInputStream(IFSJavaFile) is null.
**/
  public void Var205()
  {
    try
    {
      IFSFileInputStream is = new IFSFileInputStream((IFSJavaFile) null);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that a FileNotFoundException having the file name as the detail
message is thrown by IFSFileInputStream(IFSJavaFile) if the file
doesn't exist.
**/
  public void Var206()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    deleteFile(ifsPathNameX);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is = new IFSFileInputStream(file);
      failed("Exception didn't occur."+is);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "FileNotFoundException", ifsPathNameX);
    }
  }

/**
Test IFSFileInputStream(IFSJavaFile) with existing file.
**/
  public void Var207()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is = new IFSFileInputStream(file);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/**
Ensure that IFSFileInputStream(IFSJavaFile) does share file
access with other readers and writers.
**/
  public void Var208()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathNameX);
      IFSFileInputStream is1 = new IFSFileInputStream(file);
      checkReadWriteAccess(ifsPathNameX);

      is1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


/**
Ensure that NullPointerException is thrown if argument of IFSFileOutputStream(IFSFile) is null.
**/
  public void Var209()
  {
    try
    {
      IFSFileOutputStream os = new IFSFileOutputStream((IFSFile)null);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that IFSFileOutputStream(IFSFile) does share file
access with other readers and writers.
**/
  public void Var210()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(file);
      checkReadWriteAccess(ifsPathNameX);

      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }


/**
Ensure that NullPointerException is thrown if argument of IFSFileOutputStream(IFSJavaFile) is null.
**/
  public void Var211()
  {
    try
    {
      IFSFileOutputStream os = new IFSFileOutputStream((IFSJavaFile)null);
      failed("Exception didn't occur."+os);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Ensure that IFSFileOutputStream(IFSJavaFile) does share file
access with other readers and writers.
**/
  public void Var212()
  {
    String ifsPathNameX = ifsPathName_ + getVariation();
    createFile(ifsPathNameX);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathNameX);
      IFSFileOutputStream os1 =
        new IFSFileOutputStream(file);
      checkReadWriteAccess(ifsPathNameX);
      os1.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathNameX);
  }

/////////////////////


/**
Ensure that a NullPointerException occurs if argument #1 of IFSFile(IFSFile, String) is null.
**/
  public void Var213()
  {
    try
    {
      IFSFile nullParm = null; 
      IFSFile dir = new IFSFile( nullParm,
                                filename);
      failed("Exception didn't occur."+dir);
    }
    catch(Exception e)
    {
	// Updated 04/05/2022 when IFSFile parm used as first parameter 
      assertExceptionIs(e, "NullPointerException", "directory");
    }
  }

/**
Ensure that a NullPointerException occurs if argument #2 of IFSFile(IFSFile, String) is null.
Updated 04/05/2022.  Does not throw NPE because code just does + string. 
**/
  public void Var214()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/Directory");
      IFSFile file = new IFSFile(dir, (String) null);

      assertCondition(true); 

   }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

/**
Test IFSFile(IFSFile, String) with a directory of '/Directory' and
file of 'File'.
**/
  public void Var215()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/Directory");
      IFSFile file = new IFSFile(dir, filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getAbsolutePath().equals(file.getPath()) &&
             file.getName().equals(filename));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(IFSFile, String) with a directory of '/' and
file of 'File'.
**/
  public void Var216()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/");
      IFSFile file = new IFSFile(dir, filename);
      assertCondition(file.getAbsolutePath().equals("/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(IFSFile, String) with a directory of 'Directory' and
file of 'File'.
**/
  public void Var217()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "Directory");
      IFSFile file = new IFSFile(dir, filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getAbsolutePath().equals(file.getPath()) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test IFSFile(IFSFile, String) with a directory of '/Directory/' and
file of 'File'.
**/
  public void Var218()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, "/Directory/");
      IFSFile file = new IFSFile(dir, filename);
      assertCondition(file.getAbsolutePath().equals("/Directory/"+filename) &&
             file.getName().equals(filename) &&
             file.getAbsolutePath().equals(file.getPath()));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
Test IFSTextFileOutputStream with an IFS file to verify that it can create a new file.   This tests an error found in Jan 2010 that was introduced on V7R1 with MF50001.

*/ 
  public void Var219()
  {
      String added = " -- Added 1/27/2011 to verify that IFS file can be created.  Detects error introduced by V7R1/MF50001"; 
    try
    {
	// Delete the member to make sure it does not exist.
	CommandCall cmd = new CommandCall(pwrSys_);
	boolean rc; 
	rc = cmd.run("RMVM FILE(QUSRSYS/QATMHINSTC) MBR(IFSCTORTST)"); 

	IFSTextFileOutputStream ifsStream =
	  new IFSTextFileOutputStream(
				      pwrSys_,
				      "/QSYS.LIB/QUSRSYS.LIB/QATMHINSTC.FILE/IFSCTORTST.MBR",
				      IFSFileOutputStream.SHARE_WRITERS,
				      false,
				      pwrSys_.getCcsid());

	// Drop the connection so that the file can be deleted
	pwrSys_.disconnectService(AS400.FILE); 
	try { 
	ifsStream.close(); 
	} catch (Exception e) {} 
	rc = 	cmd.run("RMVM FILE(QUSRSYS/QATMHINSTC) MBR(IFSCTORTST)"); 
	assertCondition(rc, "Unable to delete file that should have been created rc="+rc+added);	
    }
    catch(Exception e)
    {
      failed(e, added);
    }
  }


  String pad80(String input) {
    StringBuffer sb = new StringBuffer(input);
    int length = input.length(); 
    while (length < 80) {
      sb.append(' '); 
      length++; 
    }
    return sb.toString(); 
  }
  
/**
Test IFSTextFileInputStream where exit program prevents access.
This is to mimic CPS 8B5S2H and CPS 8KMAN8 where the problem
could not easily be determined because the retrun code was missing.

*/ 
  public void Var220()
  {
    if (true) {
      System.out.println("Skipping Var220 because it requires QSERVER subsystem to be ended and restarted\n");
      assertCondition(true);
      return; 
    }
      String added = " -- Added 8/24/2011 to verify exception when exit program disallows access.";
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      String command; 
    try
    {
	// Create the exit program for QIBM_QPWFS_FILE_SERV
	CommandCall cmd = new CommandCall(pwrSys_);
	boolean rc;

	command = "crtsrcpf qgpl/qclsrc";
	sb.append("RUNNING "+command+"\n"); 
	rc = cmd.run(command);
	sb.append("Returned "+rc+"\n");

	command = "ADDPFM FILE(QGPL/QCLSRC) MBR(PWFSEXIT)";
	sb.append("RUNNING "+command+"\n"); 
	rc = cmd.run(command);
	sb.append("Returned "+rc+"\n");

	String formattedUserId = systemObject_.getUserId();
	while (formattedUserId.length() < 10) { formattedUserId=formattedUserId+" "; };

	String[] exitProgram= {
"PGM PARM(&FLAG &REQUEST)",
"/******************************************************************/",
"/* Program call parameter declarations                            */",
"/******************************************************************/",
"DCL VAR(&FLAG) TYPE(*CHAR) LEN(1)",
"DCL VAR(&REQUEST) TYPE(*CHAR) LEN(1000)",
"/******************************************************************/",
"/* Parameter declares for Request Format                          */",
"/******************************************************************/",
"",
"DCL VAR(&USER) TYPE(*CHAR) LEN(10)        /* User profile     0-09*/",
"/******************************************************************/",
"/* Extract the various parameters from the structure.             */",
"/******************************************************************/",
"CHGVAR VAR(&USER)   VALUE(%SST(&REQUEST  1 10))",
"/******************************************************************/",
"/* Set return code to allow the request.                          */",
"/******************************************************************/",
"CHGVAR VAR(&FLAG) VALUE('1')",
"/******************************************************************/",
"/* If interface type is ODBC and User is 'GUEST' reject the       */",
"/* signon attempt.                                                */",
"/******************************************************************/",
"IF ((&USER = '"+formattedUserId+"'))  THEN(DO)",
"     /*************************************************************/",
"     /*     Set return code to NOT allow the request.             */",
"     /*************************************************************/",
"       CHGVAR VAR(&FLAG) VALUE('0')",
"ENDDO",
"ENDPGM",

	}; 



	IFSTextFileOutputStream ifsStream =
	  new IFSTextFileOutputStream(
				      pwrSys_,
				      "/QSYS.LIB/QGPL.LIB/QCLSRC.FILE/PWFSEXIT.MBR",
				      IFSFileOutputStream.SHARE_WRITERS,
				      false,
				      pwrSys_.getCcsid());


	for (int i = 0; i < exitProgram.length; i++) {
	    ifsStream.write(pad80(exitProgram[i]));
	} 
	ifsStream.close();


	command = "CRTBNDCL PGM(QGPL/PWFSEXIT) SRCFILE(QGPL/QCLSRC) DBGVIEW(*SOURCE) "; 

	sb.append("RUNNING "+command+"\n"); 
	rc = cmd.run(command);
	sb.append("Returned "+rc+"\n");


	command = "ADDEXITPGM EXITPNT(QIBM_QPWFS_FILE_SERV) FORMAT(PWFS0100)   PGMNBR(1) PGM(QGPL/PWFSEXIT)";
	sb.append("RUNNING "+command+"\n"); 
	rc = cmd.run(command);
	sb.append("Returned "+rc+"\n");


	// After changing the exit program the QSERVER subsystem must be restarted. 
	command = "endsbs qserver option(*immed)"; 
        sb.append("RUNNING "+command+"\n"); 
        rc = cmd.run(command);
        sb.append("Returned "+rc+"\n");

        try {
          Thread.sleep(10000); 
        } catch (Exception e) {
          
        }

        command = "strsbs qserver"; 
        sb.append("RUNNING "+command+"\n"); 
        rc = cmd.run(command);
        sb.append("Returned "+rc+"\n");
        try {
          Thread.sleep(10000); 
        } catch (Exception e) {
          
        }

	// Try to access a file using the normal connection.
	String ifsPathNameX = ifsPathName_ + getVariation();
	try { 
	    IFSFileInputStream is1 = new IFSFileInputStream(systemObject_, ifsPathNameX);
	    is1.close();
	    passed = false;
	    sb.append("Did not throw exception on connect "); 
	} catch (Exception e) {
	    String exceptionText = e.toString();
	    if (exceptionText.indexOf("4636") > 0) {
		// good
	      // Just to see it for now. 
	       e.printStackTrace(); 
	    }  else {
		passed = false;
		sb.append("Did not have 4636 return code in "+exceptionText+"\n"); 
		e.printStackTrace();
	    }
	} 


	// Remove exit program
	command = "RMVEXITPGM EXITPNT(QIBM_QPWFS_FILE_SERV) FORMAT(PWFS0100) PGMNBR(1)";
	sb.append("RUNNING "+command+"\n"); 
	rc = cmd.run(command);
        sb.append("Returned "+rc+"\n");

        // After changing the exit program the QSERVER subsystem must be restarted. 
        command = "endsbs qserver option(*immed)"; 
        sb.append("RUNNING "+command+"\n"); 
        rc = cmd.run(command);
        sb.append("Returned "+rc+"\n");

        try {
          Thread.sleep(10000); 
        } catch (Exception e) {
          
        }

        command = "strsbs qserver"; 
        sb.append("RUNNING "+command+"\n"); 
        rc = cmd.run(command);
        sb.append("Returned "+rc+"\n");
        try {
          Thread.sleep(10000); 
        } catch (Exception e) {
          
        }

	if (!rc) {
	    sb.append("ERROR: exit program not removed\n");
	    passed = false; 

	} 
	assertCondition(passed, sb.toString() + added); 
    }
    catch(Exception e)
    {
      failed(e, sb.toString() + added);
    }
  }




}



