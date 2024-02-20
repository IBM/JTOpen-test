///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSLockTestcase.java
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
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSKey;
import com.ibm.as400.access.IFSRandomAccessFile;

/**
Test locking for IFSFileInputStream, IFSFileOutputStream, and IFSRandomAccessFile.
**/
public class IFSLockTestcase extends IFSGenericTestcase
{

/**
Constructor.
**/
  public IFSLockTestcase (AS400 systemObject,
        String userid,
        String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSLockTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
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
Ensure that ExtendedIllegalArgumentException is thrown by IFSFileInputStream.lock(int)
if argument one is <= 0.
**/
  public void Var001()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };

    deleteFile(ifsPathName_);
    createFile(ifsPathName_, data);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.lock(0);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          is.lock(-1);
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
        failed(e1, "Incorrect Exception information.");
      }
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileInputStream.lock(int) to lock the first byte of a file.  Ensure that
others accessing this file cannot violate the lock.
**/
  public void Var002()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileInputStream is = null;
    IFSKey key = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      key = is.lock(1);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write(1);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileInputStream.lock(int) to lock the last byte of a file.  Ensure
that others accessing this file cannot violate the lock.
**/
  public void Var003()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileInputStream is = null;
    IFSKey key = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      int length = is.available();
      is.skip(length - 1);
      key = is.lock(1);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.seek(length - 1);
      raf.write(1);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileInputStream.lock(int) to lock the all the bytes of a file.  Ensure
that others accessing this file cannot violate the lock.
**/
  public void Var004()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileInputStream is = null;
    IFSKey key = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      int length = is.available();
      key = is.lock(length);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write(data, 0, length);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by IFSFileInputStream.unlock(IFSKey)
if argument one is null.
**/
  public void Var005()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.lock(1);
      is.unlock((IFSKey) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "key");
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if
IFSFileInputStream.unlock(IFSKey) is called with an IFSKey that was
generated by the lock method of another object.
**/
  public void Var006()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileInputStream is1 = null;
    IFSFileInputStream is2 = null;
    IFSKey key1 = null;
    try
    {
      is1 = new IFSFileInputStream(systemObject_, ifsPathName_);
      int length = is1.available();
      key1 = is1.lock(1);
      is2 = new IFSFileInputStream(systemObject_, ifsPathName_);
      is2.skip(1);
      IFSKey key2 = is2.lock(1);
      is1.unlock(key2);
      failed("Exception didn't occur."+key1+length);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    try { is1.close(); } catch(Exception e) {}
    try { is2.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by IFSFileOutputStream.lock(int)
if argument one is <= 0.
**/
  public void Var007()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileOutputStream os = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.lock(0);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          os.lock(-1);
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
        failed(e1, "Incorrect Exception information.");
      }
    }
    try { os.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileOutputStream.lock(int) to lock the first byte of a file.  Ensure
that others accessing this file cannot violate the lock.
**/
  public void Var008()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileOutputStream os = null;
    IFSKey key = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      key = os.lock(1);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write(1);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { os.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileOutputStream.lock(int) to lock the last byte of a file.  Ensure
that others accessing this file cannot violate the lock.
**/
  public void Var009()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileOutputStream os = null;
    IFSKey key = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.write(data, 0, data.length - 1);
      key = os.lock(1);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.seek(data.length - 1);
      raf.write(1);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { os.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileOutputStream.lock(int) to lock the all the bytes of a file.  Ensure
that others accessing this file cannot violate the lock.
**/
  public void Var010()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileOutputStream os = null;
    IFSKey key = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      key = os.lock(data.length);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write(data);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { os.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by IFSFileOutputStream.unlock(IFSKey)
if argument one is null.
**/
  public void Var011()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileOutputStream os = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.lock(1);
      os.unlock((IFSKey) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "key");
    }
    try { os.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if
IFSFileOutputStream.unlock(IFSKey) is called with an IFSKey that was
generated by the lock method of another object.
**/
  public void Var012()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileOutputStream os1 = null;
    IFSFileOutputStream os2 = null;
    IFSKey key1 = null;
    try
    {
      os1 = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os2 = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os2.write(1);
      key1 = os1.lock(1);
      IFSKey key2 = os2.lock(1);
      os1.unlock(key2);
      failed("Exception didn't occur."+key1);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    try { os1.close(); } catch(Exception e) {}
    try { os2.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.lock(int, int) if argument two is <= 0.
**/
  public void Var013()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.lock(0, 0);
      failed("Exception didn't occur.");
    }
    catch(Exception e1)
    {
      if (exceptionIs(e1, "ExtendedIllegalArgumentException",
                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        try
        {
          raf.lock(0, -1);
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
        failed(e1, "Incorrect Exception information.");
      }
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.lock(int, int) if argument one is < 0.
**/
  public void Var014()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.lock(-1, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSRandomAccessFile.lock(int, int) to lock the first byte of a file.  Ensure
that others accessing this file cannot violate the lock.
**/
  public void Var015()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf1 = null;
    IFSKey key = null;
    try
    {
      raf1 = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      key = raf1.lock(0, 1);
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf2.write(1);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { raf1.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSRandomAccessFile.lock(int, int) to lock the last byte of a file.
Ensure that others accessing this file cannot violate the lock.
**/
  public void Var016()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf1 = null;
    IFSKey key = null;
    try
    {
      raf1 = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      int length = (int) raf1.length();
      key = raf1.lock(length - 1, 1);
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf2.seek(length - 1);
      raf2.write(1);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { raf1.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSRandomAccessFile.lock(int, int) to lock the all the bytes of a file.
Ensure that others accessing this file cannot violate the lock.
**/
  public void Var017()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf1 = null;
    IFSKey key = null;
    try
    {
      raf1 = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      int length = (int) raf1.length();
      key = raf1.lock(0, length);
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf2.write(data, 0, length);
      failed("Exception didn't occur."+key);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try { raf1.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by IFSRandomAccessFile.unlock(IFSKey)
if argument one is null.
**/
  public void Var018()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.lock(0, 1);
      raf.unlock((IFSKey) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "key");
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if
IFSRandomAccessFile.unlock(IFSKey) is called with an IFSKey that was
generated by the lock method of another object.
**/
  public void Var019()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile raf1 = null;
    IFSRandomAccessFile raf2 = null;
    IFSKey key1 = null;
    try
    {
      raf1 = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      key1 = raf1.lock(0, 1);
      raf2 = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      IFSKey key2 = raf2.lock(1, 1);
      raf1.unlock(key2);
      failed("Exception didn't occur."+key1);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    try { raf1.close(); } catch(Exception e) {}
    try { raf2.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that we can lock a range of bytes completely beyond EOF.
   **/
  public void Var020()
  {
    byte[] data = { 1, 2, 3, 4 };
    createFile(ifsPathName_, data);
    IFSRandomAccessFile file = null;
    try
    {
      file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.lock(0x7ffffff, 4);
      IFSRandomAccessFile violator =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      violator.seek(0x7ffffff);
      violator.write(1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    try
    {
      file.close();
    }
    catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Use IFSFileInputStream.lock(int) to lock the all the bytes of a file.  Ensure
that others cannot lock any bytes in this file.
**/
  public void Var021()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    IFSFileInputStream is = null;
    IFSFileInputStream is2 = null;
    IFSKey key = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      int length = is.available();
      key = is.lock(length);

      is2 = new IFSFileInputStream(pwrSys_, ifsPathName_);
      IFSKey key2 = is.lock(1);
      failed("Exception didn't occur."+key+key2);
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIOException",
                        ExtendedIOException.LOCK_VIOLATION);
    }
    if (is != null) {
      try { is.close(); } catch(Exception e) {}
    }
    if (is2 != null) {
      try { is2.close(); } catch(Exception e) {}
    }
    deleteFile(ifsPathName_);
  }



}



