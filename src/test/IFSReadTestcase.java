///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSReadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileReader;
import com.ibm.as400.access.IFSFileWriter;
import com.ibm.as400.access.IFSKey;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;

/**
Test read methods for IFSFileInputStream, IFSFileOutputStream, and
IFSRandomAccessFile.
**/
public class IFSReadTestcase extends IFSGenericTestcase
{

/**
Constructor.
**/
  public IFSReadTestcase (AS400 systemObject,
      String userid,
      String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String   driveLetter,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSReadTestcase",
            namesAndVars, runMode, fileOutputStream, driveLetter, pwrSys);
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.read()
if called after closed.
**/
  public void Var001()
  {
    deleteFile(ifsPathName_);
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read() returns -1 at the end of file.
**/
  public void Var002()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      assertCondition(is.read() == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSFileInputStream.read().
**/
  public void Var003()
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        int i1;
        int i2;
        do
        {
          i1 = is1.read();
          i2 = raf.read();
        }
        while(i1 == i2 && i2 != -1);
        assertCondition(i1 == i2);
        raf.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);
        int i1;
        int i2;
        do
        {
          i1 = is1.read();
          i2 = is2.read();
        }
        while(i1 == i2 && i2 != -1);
        assertCondition(i1 == i2);
        is2.close();
      }
      is1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown by IFSFileInputStream.read(byte[])
if argument one is null.
**/
  public void Var004()
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.read((byte[]) null);
      failed("Exception didn't occur");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "data"));
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSFileInputStream.read(byte[]) if called after closed.
**/
  public void Var005()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[]) returns zero if the length of
argument one is zero.
**/
  public void Var006()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      assertCondition(is.read(new byte[0]) == 0);
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[]) returns -1 if the read operation
starts at the end of file.
**/
  public void Var007()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      assertCondition(is.read(new byte[1]) == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[]) reads up to the end of file
successfully, returning the number of bytes read when the number of bytes
available to be read is less than the byte array length.
**/
  public void Var008()
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        byte[] data1 = new byte[data.length * 2];
        byte[] data2 = new byte[data.length * 2];
        int bytesRead1 = is1.read(data1);
        int bytesRead2 = raf.read(data2);
        assertCondition(bytesRead1 == bytesRead2);
        raf.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        byte[] data1 = new byte[data.length * 2];
        byte[] data2 = new byte[data.length * 2];
        int bytesRead1 = is1.read(data1);
        int bytesRead2 = is2.read(data2);
        assertCondition(bytesRead1 == bytesRead2);
        is2.close();
      }
      is1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSFileInputStream.read(byte[]).
**/
  public void Var009()
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        byte[] data1 = new byte[data.length];
        byte[] data2 = new byte[data.length];
        int bytesRead1 = is1.read(data1);
        int bytesRead2 = is2.read(data2);
        assertCondition(bytesRead1 == bytesRead2 && isEqual(data1, data2));
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        byte[] data1 = new byte[data.length];
        byte[] data2 = new byte[data.length];
        int bytesRead1 = is1.read(data1);
        int bytesRead2 = is2.read(data2);
        assertCondition(bytesRead1 == bytesRead2 && isEqual(data1, data2));
        is2.close();
      }
      is1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown by
IFSFileInputStream.read(byte[], int offset, int length) if argument one is null.
**/
  public void Var010()
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.read((byte[]) null, 0, 1);
      failed("Exception didn't occur");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "data"));
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by read(byte[], int, int) if argument two is < 0.
**/
  public void Var011()
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.read(new byte[1], -1, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by read(byte[], int, int) if argument three is < 0.
**/
  public void Var012()
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(systemObject_, ifsPathName_);
      is.read(new byte[1], 0, -1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { is.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSFileInputStream.read(byte[], int, int) if called after closed.
**/
  public void Var013()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[], int, int) returns zero if
argument three is zero.
**/
  public void Var014()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      assertCondition(is.read(new byte[1], 0, 0) == 0);
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[], int, int) returns -1 if the
read operation starts at the end of file.
**/
  public void Var015()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      assertCondition(is.read(new byte[1], 0, 1) == -1);
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[], int, int) reads up to the end
of file successfully, returning the number of bytes read when argument three
is less than the number of bytes available to be read.
**/
  public void Var016()
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        byte[] data1 = new byte[data.length * 2];
        byte[] data2 = new byte[data.length * 2];
        int bytesRead1 = is1.read(data1, 0, data1.length);
        int bytesRead2 = is2.read(data2, 0, data2.length);
        assertCondition(bytesRead1 == bytesRead2);
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        byte[] data1 = new byte[data.length * 2];
        byte[] data2 = new byte[data.length * 2];
        int bytesRead1 = is1.read(data1, 0, data1.length);
        int bytesRead2 = is2.read(data2, 0, data2.length);
        assertCondition(bytesRead1 == bytesRead2);
        is2.close();
      }
      is1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileInputStream.read(byte[], int, int) stores the data read
at the specified offset in the byte array.
**/
  public void Var017()
  {
    byte[] data = { 11,22,33,44,55,66,77 };
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };
      byte[] data2 = { 0,1,2,3,4,5,6,7,8,9 };
      is1.read(data1, 3, 4);
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        is2.read(data2, 3, 4);
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        is2.read(data2, 3, 4);
        is2.close();
      }
      assertCondition(isEqual(data1, data2));
      is1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSFileInputStream.read(byte[], int, int).
**/
  public void Var018()
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      IFSFileInputStream is1 =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      int bytesRead1 = is1.read(data1, 0, data1.length);
      int bytesRead2 = 0;
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        bytesRead2 = is2.read(data2, 0, data2.length);
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        bytesRead2 = is2.read(data2, 0, data2.length);
        is2.close();
      }
      assertCondition(bytesRead1 == bytesRead2 && isEqual(data1, data2));
      is1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.read()
if called after closed.
**/
  public void Var019()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read() returns -1 at the end of file.
**/
  public void Var020()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      assertCondition(raf.read() == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.read().
**/
  public void Var021()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);


      int i1;
      int i2;
      do
      {
        i1 = raf1.read();
        i2 = raf2.read();
      }
      while(i1 == i2 && i2 != -1);
      assertCondition(i1 == i2);
      raf1.close();
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown by IFSRandomAccessFile.read(byte[])
if argument one is null.
**/
  public void Var022()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.read((byte[]) null);
      failed("Exception didn't occur");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "data"));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.read(byte[]) if called after closed.
**/
  public void Var023()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.read(new byte[1]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[]) returns zero if the length of
argument one is zero.
**/
  public void Var024()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(raf.read(new byte[0]) == 0);
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[]) returns -1 if the read operation
starts at the end of file.
**/
  public void Var025()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(raf.read(new byte[1]) == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[]) reads up to the end of file
successfully, returning the number of bytes read when the number of bytes
available to be read is less than the byte array length.
**/
  public void Var026()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = new byte[data.length * 2];
      byte[] data2 = new byte[data.length * 2];
      int bytesRead1 = raf1.read(data1);
      int bytesRead2 = raf2.read(data2);
      assertCondition(bytesRead1 == bytesRead2);
      raf1.close();
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.read(byte[]).
**/
  public void Var027()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      int bytesRead1 = raf1.read(data1);
      int bytesRead2 = raf2.read(data2);
      assertCondition(bytesRead1 == bytesRead2 && isEqual(data1, data2));
      raf1.close();
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown by
IFSRandomAccessFile.read(byte[], int offset, int length) if argument one is
null.
**/
  public void Var028()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.read((byte[]) null, 0, 1);
      failed("Exception didn't occur");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "data"));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.read(byte[], int, int) if argument two is < 0.
**/
  public void Var029()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.read(new byte[1], -1, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.read(byte[], int, int) if argument three is < 0.
**/
  public void Var030()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.read(new byte[1], 0, -1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.read(byte[], int, int) if called after closed.
**/
  public void Var031()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.read(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[], int, int) returns zero if
argument three is zero.
**/
  public void Var032()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(raf.read(new byte[1], 0, 0) == 0);
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[], int, int) returns -1 if the
read operation starts at the end of file.
**/
  public void Var033()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(raf.read(new byte[1], 0, 1) == -1);
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[], int, int) reads up to the end
of file successfully, returning the number of bytes read when argument three
is greater than the number of bytes available to be read.
**/
  public void Var034()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = new byte[data.length * 2];
      byte[] data2 = new byte[data.length * 2];
      int bytesRead1 = raf1.read(data1, 0, data1.length);
      int bytesRead2 = raf2.read(data2, 0, data2.length);
      assertCondition(bytesRead1 == bytesRead2);
      raf1.close();
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.read(byte[], int, int) stores the data read
at the specified offset in the byte array.
**/
  public void Var035()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = { 11,22,33,44,55,66,77 };
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };
      byte[] data2 = { 0,1,2,3,4,5,6,7,8,9 };
      raf1.read(data1, 3, 4);
      raf2.read(data2, 3, 4);
      assertCondition(isEqual(data1, data2));
      raf1.close();
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.read(byte[], int, int).
**/
  public void Var036()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      int bytesRead1 = raf1.read(data1, 0, data1.length);
      int bytesRead2 = raf2.read(data2, 0, data2.length);
      assertCondition(bytesRead1 == bytesRead2 && isEqual(data1, data2));
      raf1.close();
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that ConnectionDroppedException is thrown by readBoolean() if called after closed.
**/
  public void Var037()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.readBoolean();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by readBoolean() if the end of file has been reached.
**/
  public void Var038()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readBoolean();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Read a file containing every possible byte using
IFSRandomAccessFile.readBoolean().
**/
  public void Var039()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");

      // RandomAccessFile raf2 =  new RandomAccessFile(convertToPCName(ifsPathName_), "r");

      DataInput raf2 = openDataInput(ifsPathName_, "r");   
        
      int i = 0;
      while(i < data.length && raf1.readBoolean() == raf2.readBoolean())
        i++;
      assertCondition(i == data.length);
      raf1.close();
      JDReflectionUtil.callMethod_V(raf2, "close"); 
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by readByte() if called after closed.
**/
  public void Var040()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.readByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by readByte() if the end of file has been reached.
**/
  public void Var041()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.readByte().
**/
  public void Var042()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      // RandomAccessFile raf2 = new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 
        
      int i = 0;
      while (i < 256 && raf1.readByte() == raf2.readByte())
        i++;
      assertCondition(i == 256);
      raf1.close();
      JDReflectionUtil.callMethod_V(raf2, "close"); 
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readChar() if called after closed.
**/
  public void Var043()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.readChar();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readChar() if the
end of file has been reached.
**/
  public void Var044()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readChar();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify a file containing characters \u0000 through \u0080, \u7fff,
\u8000, and \uffff using IFSRandomAccessFile.readChar().
**/
  public void Var045()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    String s = "";
    for (int i = 0; i < 128; i++)
      s += (char) i;
    s += '\u7fff';
    s += '\u8000';
    s += '\uffff';
    createFileWriteChars(ifsPathName_, s);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      int i;
      if (linux_) {
        IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
        i = 0;
        while (i < s.length() && raf1.readChar() == raf2.readChar())
          i++;
        raf2.close();
      }
      else {
        // RandomAccessFile raf2 =   new RandomAccessFile(convertToPCName(ifsPathName_), "r");
        DataInput raf2 = openDataInput(ifsPathName_, "r"); 

        
        i = 0;
        while (i < s.length() && raf1.readChar() == raf2.readChar())
          i++;
        JDReflectionUtil.callMethod_V(raf2,"close");

      }
      assertCondition(i == s.length());
      raf1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readDouble() if called after closed.
**/
  public void Var046()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.readDouble();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readDouble() if
the end of file has been reached.
**/
  public void Var047()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readDouble();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Read from a file containing the minimum Double value, 0.0, and the maximum
Double value using IFSRandomAccessFile.readDouble().
**/
  public void Var048()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    createFile(ifsPathName_);
    try
    {
      {
        DataOutput file1 = openDataOutput(ifsPathName_, "rw");
      file1.writeDouble(Double.MIN_VALUE);
      file1.writeDouble(0.0);
      file1.writeDouble(Double.MAX_VALUE);
      JDReflectionUtil.callMethod_V(file1,"close");
      }
      DataInput file1 = openDataInput(ifsPathName_, "r"); 
      // file1 = new RandomAccessFile(convertToPCName(ifsPathName_), "rw");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file1.readDouble() == file2.readDouble() &&
             file1.readDouble() == file2.readDouble() &&
             file1.readDouble() == file2.readDouble());
      file2.close();
      JDReflectionUtil.callMethod_V(file1,"close");

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readFloat() if called after closed.
**/
  public void Var049()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.readFloat();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readFloat() if
the end of file has been reached.
**/
  public void Var050()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readFloat();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Read from a file containing the minimum Float value, 0.0, and the maximum
Float value using IFSRandomAccessFile.readFloat().
**/
  public void Var051()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    createFile(ifsPathName_);
    try
    {
      {
        DataOutput file1 = openDataOutput(ifsPathName_, "rw");

      file1.writeFloat(Float.MIN_VALUE);
      file1.writeFloat(0.0F);
      file1.writeFloat(Float.MAX_VALUE);
      JDReflectionUtil.callMethod_V(file1,"close");

      }
      DataInput file1 = openDataInput(ifsPathName_, "r"); 
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file1.readFloat() == file2.readFloat() &&
             file1.readFloat() == file2.readFloat() &&
             file1.readFloat() == file2.readFloat());
      file2.close();
      JDReflectionUtil.callMethod_V(file1,"close");

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by readFully(byte[]) if argument one is null.
**/
  public void Var052()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.readFully((byte[]) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "data");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }


/**
Ensure that ConnectionDroppedException is thrown by readFully(byte[]) if called after closed.
**/
  public void Var053()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.close();
      file.readFully(new byte[2]);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.readFully(byte[]) returns immediately if the
length of argument one is zero.
**/
  public void Var054()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readFully(new byte[0]);
      succeeded();
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.readFully(byte[]) blocks at end of file.
**/
  public void Var055()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "w");

      boolean done[] = new boolean[1];
      done[0] = false;
      byte[] oneByte = {1};
      int totalBytes = 0;

      // Start the readFully thread
      IFSReadFullyThread readThread = new IFSReadFullyThread(55, file, done);
      readThread.start();

      // Give the readFully thread time to get running
      Thread.sleep(2000);

      // Begin writing bytes to the file and quit when
      // the readFully thread has signaled it has finished its read,
      // or if it dies
      while (done[0] != true && readThread.isAlive())
      {
        output_.println("Starting loop.");
        file2.write(oneByte);
        totalBytes++;
        output_.println(totalBytes+" total bytes written to "+convertToPCName(ifsPathName_));
        Thread.sleep(1000); // Give readFully a chance to catch up
      }
      file2.close();
      file.close();
      if (done[0] != true)
      {
        failed("Unexpected thread failure.");
      }
      else
      {
        if (totalBytes > 2)
        {
          String failMsg = "Too many bytes written: "+totalBytes+"\n";
          failMsg += "It is possible the readFully thread was not given enough processor time.";
          failed(failMsg);
        }
        else
        {
          if (totalBytes < 2)
            failed("Too few bytes written.");
          else
            succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.readFully(byte[]).
**/
  public void Var056()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      // RandomAccessFile raf2 = new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      raf1.readFully(data1);
      raf2.readFully(data2);
      assertCondition(isEqual(data1, data2));
      raf1.close();
      JDReflectionUtil.callMethod_V(raf2,"close");
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that NullPointerException is thrown by
IFSRandomAccessFile.readFully(byte[], int offset, int length) if argument one
is null.
**/
  public void Var057()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readFully((byte[]) null, 0, 1);
      failed("Exception didn't occur");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "data"));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.readFully(byte[], int, int) if argument two is < 0.
**/
  public void Var058()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readFully(new byte[1], -1, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown by
IFSRandomAccessFile.readFully(byte[], int, int) if argument three is < 0.
**/
  public void Var059()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readFully(new byte[1], 0, -1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID));
    }
    try { raf.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readFully(byte[], int, int) if called after closed.
**/
  public void Var060()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.close();
      raf.readFully(new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.readFully(byte[], int, int) returns immediately
if argument three is zero.
**/
  public void Var061()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      raf.readFully(new byte[1], 0, 0);
      succeeded();
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.readFully(byte[], int, int) blocks at end of
file.
**/
  public void Var062()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "w");

      boolean done[] = new boolean[1];
      done[0] = false;
      byte[] oneByte = {1};
      int totalBytes = 0;

      // Start the readFully thread
      IFSReadFullyThread readThread = new IFSReadFullyThread(62, file, done);
      readThread.start();

      // Give the readFully thread time to get running
      Thread.sleep(2000);

      // Begin writing bytes to the file and quit when
      // the readFully thread has signaled it has finished its read,
      // or if it dies
      while (done[0] != true && readThread.isAlive())
      {
        output_.println("Starting loop.");
        file2.write(oneByte);
        totalBytes++;
        output_.println(totalBytes+" total bytes written to "+convertToPCName(ifsPathName_));
        Thread.sleep(1000); // Give readFully a chance to catch up
      }
      file2.close();
      file.close();
      if (done[0] != true)
      {
        failed("Unexpected thread failure.");
      }
      else
      {
        if (totalBytes > 2)
        {
          String failMsg = "Too many bytes written: "+totalBytes+"\n";
          failMsg += "It is possible the readFully thread was not given enough processor time.";
          failed(failMsg);
        }
        else
        {
          if (totalBytes < 2)
            failed("Too few bytes written.");
          else
            succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


/**
Ensure that IFSRandomAccessFile.readFully(byte[], int, int) stores the data
read at the specified offset in the byte array.
**/
  public void Var063()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = { 11,22,33,44,55,66,77 };
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      // RandomAccessFile raf2 =  new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 

      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };
      byte[] data2 = { 0,1,2,3,4,5,6,7,8,9 };
      raf1.readFully(data1, 3, 4);
      raf2.readFully(data2, 3, 4);
      assertCondition(isEqual(data1, data2));
      raf1.close();
      JDReflectionUtil.callMethod_V(raf2,"close");

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.readFully(byte[], int, int).
**/
  public void Var064()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      // RandomAccessFile raf2 =  new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      raf1.readFully(data1, 0, data1.length);
      raf2.readFully(data2, 0, data2.length);
      assertCondition(isEqual(data1, data2));
      raf1.close();
      JDReflectionUtil.callMethod_V(raf2,"close");

    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readInt() if called after closed.
**/
  public void Var065()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.close();
      file.readInt();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readInt() if
the end of file has been reached.
**/
  public void Var066()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.readInt();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Read from a file containing the minimum Integer value, 0, and the maximum
Integer value using IFSRandomAccessFile.readInt().
**/
  public void Var067()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    createFile(ifsPathName_);
    try
    {
      {
      DataOutput file1 = openDataOutput(ifsPathName_, "rw");

      file1.writeInt(Integer.MIN_VALUE);
      file1.writeInt(0);
      file1.writeInt(Integer.MAX_VALUE);
      JDReflectionUtil.callMethod_V(file1,"close");

      }
      DataInput file1 = openDataInput(ifsPathName_, "r"); 
      // file1 = new RandomAccessFile(convertToPCName(ifsPathName_), "rw");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file1.readInt() == file2.readInt() &&
             file1.readInt() == file2.readInt() &&
             file1.readInt() == file2.readInt());
      JDReflectionUtil.callMethod_V(file1,"close");

      file2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readLine() if called after closed.
**/
  public void Var068()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.close();
      file.readLine();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that null is returned by IFSRandomAccessFile.readLine() if
at end of file.
**/
  public void Var069()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file.readLine() == null);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Read a file containing a '\n', followed by "hello", followed by a '\r',
followed by "world", followed by a '\r', followed by a '\n' using
IFSRandomAccessFile.readLine().  Calling readLine() three times should
return the following strings: "\n", "hello\r", "world\r\n".
**/
  public void Var070()
  {
    byte[] data = { 10, 104, 101, 108, 108, 111, 13, 119, 111, 114,
    108, 100, 13, 10 };
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file.readLine().equals("\n") &&
             file.readLine().equals("hello\r") &&
             file.readLine().equals("world\r\n"));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readLong() if called after closed.
**/
  public void Var071()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.close();
      file.readLong();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readLong() if
the end of file has been reached.
**/
  public void Var072()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.readLong();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Read from a file containing the minimum Long value, 0, and the maximum
Long value using IFSRandomAccessFile.readLong().
**/
  public void Var073()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    createFile(ifsPathName_);
    try
    {
      {
      DataOutput file1 = openDataOutput(ifsPathName_, "rw");

      file1.writeLong(Long.MIN_VALUE);
      file1.writeLong(0L);
      file1.writeLong(Long.MAX_VALUE);
      JDReflectionUtil.callMethod_V(file1,"close");

      }
      DataInput file1 = openDataInput(ifsPathName_, "rw");
      
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file1.readLong() == file2.readLong() &&
             file1.readLong() == file2.readLong() &&
             file1.readLong() == file2.readLong());
      file2.close();
      JDReflectionUtil.callMethod_V(file1,"close");

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readShort() if called after closed.
**/
  public void Var074()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      file.close();
      file.readShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readShort() if the
end of file has been reached.
**/
  public void Var075()
  {
    String fileName = ifsPathName_ + "r75";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.readShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(fileName);
  }

/**
Read from a file containing -32768, 0, and 32767 using
IFSRandomAccessFile.readShort().
**/
  public void Var076()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    String fileName = ifsPathName_ + "r76";
    createFile(fileName);
    try
    {
      {
      DataOutput file1 = openDataOutput(fileName, "rw");

      file1.writeShort(-32768);
      file1.writeShort(0);
      file1.writeShort(32767);
      JDReflectionUtil.callMethod_V(file1,"close");

      }
      DataInput file1 = openDataInput(fileName, "rw");

      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      assertCondition(file1.readShort() == file2.readShort() &&
             file1.readShort() == file2.readShort() &&
             file1.readShort() == file2.readShort());
      file2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readUnsignedByte() if called after closed.
**/
  public void Var077()
  {
    String fileName = ifsPathName_ + "r77";
    createFile(fileName);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.close();
      file.readUnsignedByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(fileName);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readUnsignedByte()
if the end of file has been reached.
**/
  public void Var078()
  {
    String fileName = ifsPathName_ + "r78";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.readUnsignedByte();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(fileName);
  }

/**
Read and verify every byte of a file containing all possible byte values
using IFSRandomAccessFile.readUnsignedByte().
**/
  public void Var079()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;

    String fileName = ifsPathName_ + "r79";
    createFile(fileName, data);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      DataInput raf2 =
        openDataInput(fileName, "r");
      int i = 0;
      while (i < 256 && raf1.readUnsignedByte() == raf2.readUnsignedByte())
        i++;
      assertCondition(i == 256);
      raf1.close();
      JDReflectionUtil.callMethod_V(raf2,"close");

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readUnsignedShort() if called after closed.
**/
  public void Var080()
  {
    String fileName = ifsPathName_ + "r80";
    createFile(fileName);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.close();
      file.readUnsignedShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(fileName);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readUnsignedShort()
if the end of file has been reached.
**/
  public void Var081()
  {
    String fileName = ifsPathName_ + "r81";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.readUnsignedShort();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(fileName);
  }

/**
Read from a file containing 0, 32767, 32768, and 65535 using
IFSRandomAccessFile.readUnsignedShort().
**/
  public void Var082()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    String fileName = ifsPathName_ + "r82";
    createFile(fileName);
    try
    {
      {
      DataOutput file1 = openDataOutput(fileName, "rw");

      file1.writeShort(0);
      file1.writeShort((short) 32767);
      file1.writeShort((short) 32768);
      file1.writeShort((short) 65535);
      JDReflectionUtil.callMethod_V(file1,"close");
      }
      DataInput file1 = openDataInput(fileName, "rw");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      assertCondition(file1.readUnsignedShort() == file2.readUnsignedShort() &&
             file1.readUnsignedShort() == file2.readUnsignedShort() &&
             file1.readUnsignedShort() == file2.readUnsignedShort());
      file2.close();
      JDReflectionUtil.callMethod_V(file1,"close");
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that ConnectionDroppedException is thrown by
IFSRandomAccessFile.readUTF() if called after closed.
**/
  public void Var083()
  {
    String fileName = ifsPathName_ + "r83";
    createFile(fileName);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.close();
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(fileName);
  }

/**
Ensure that EOFException is thrown by readUTF() if the end of file has been
reached.
**/
  public void Var084()
  {
    String fileName = ifsPathName_ + "r84";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(fileName);
  }

/**
Ensure that EOFException is thrown by IFSRandomAccessFile.readUTF()
if the end of file is reached before the number of bytes specified in the
two byte header are read.
**/
  public void Var085()
  {
    String fileName = ifsPathName_ + "r85";
    byte[] data = {0, 1};
    createFile(fileName, data);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "EOFException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(fileName);
  }

/**
Read an empty UTF string using IFSRandomAccessFile.readUTF().
**/
  public void Var086()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    String fileName = ifsPathName_ + "r86";
    createFile(fileName);
    try
    {
      {
      DataOutput raf1 = openDataOutput(fileName, "rw");
      raf1.writeUTF("");
      JDReflectionUtil.callMethod_V(raf1,"close");
      }
      DataInput raf1 = openDataInput(fileName, "rw");
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      assertCondition(raf1.readUTF().equals(raf2.readUTF()));
      raf2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that UTFDataFormatException is thrown by IFSRandomAccessFile.readUTF()
when reading data that is not in UTF-8 encoding.
**/
  public void Var087()
  {
    String fileName = ifsPathName_ + "r87";
    byte[] data = {0, 1, (byte) 0xc0};
    createFile(fileName, data);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, fileName, "r");
      file.readUTF();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "UTFDataFormatException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(fileName);
  }

/**
Read a file containing several strings in UTF data format using
IFSRandomAccessFile.readUTF().  The strings should contain a wide variety
of unicode characters.
**/
  public void Var088()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    String fileName = ifsPathName_ + "r88";
    createFile(fileName);
    try
    {
      {
      DataOutput raf1 = openDataOutput(fileName, "rw");

      raf1.writeUTF("Some unicode      : \u0030\u00b0\u0031\u043f\u0032\u500c\u0033");
      raf1.writeUTF("Some more unicode : \u0030\u00b0\u0031\u043f\u0032\u500c\u0033");
      raf1.writeUTF("Still more unicode: \u0030\u00b0\u0031\u043f\u0032\u500c\u0033");
      JDReflectionUtil.callMethod_V(raf1,"close");

      }
      DataInput raf1 = openDataInput(fileName, "rw");
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      String line11 = raf1.readUTF();
      String line21 = raf2.readUTF(); 
      String line12 = raf1.readUTF();
      String line22 = raf2.readUTF(); 
      String line13 = raf1.readUTF();
      String line23 = raf2.readUTF(); 
      
      assertCondition(line11.equals(line21) &&
                      line12.equals(line22) &&
                      line13.equals(line23),"\n"+
                      "line11='"+line11+"' "+(line11.equals(line21))+ "\n"+
                      "line21='"+line21+"'\n"+
                      "line12='"+line12+"' "+(line12.equals(line22))+ "\n"+
                      "line22='"+line22+"'\n"+
                      "line13='"+line13+"' "+(line13.equals(line23))+ "\n"+
                      "line23='"+line23+"'\n"
                      );
      raf2.close();
      JDReflectionUtil.callMethod_V(raf1,"close");
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

/**
Ensure that ConnectionDroppedException is thrown by IFSTextFileInputStream.read()
if called after closed.
**/
  public void Var089()
  {
    String fileName = ifsPathName_ + "r89";
    createFile(fileName);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, fileName);
      is.close();
      is.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(fileName);
  }

/**
Ensure that IFSTextFileInputStream.read() returns empty String ""
at the end of file.
**/
  public void Var090()
  {
    String fileName = ifsPathName_ + "r90";
    boolean isIFSFile = false;
    createFile(fileName);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, fileName);
      String text;
      try
      {
        text = is.read(1);
      }
      catch(java.io.UnsupportedEncodingException e)
      {
        deleteFile(fileName);
        createIFSFile(fileName);
        isIFSFile = true;
        is.close();
        is = new IFSTextFileInputStream(systemObject_, fileName);
        text = is.read(1);
      }
      assertCondition(text.equals(""));
    }
    catch(Exception e)
    {
      failed(e);
    }
    if (isIFSFile) deleteIFSFile(fileName);
    else deleteFile(fileName);
  }



  /**
   Ensure that IFSTextFileInputStream.read() returns the String written by
   IFSTextFileOutputStream.write(String) using CCSID 0x01b5.
   **/
   public void Var091()
   {
     if (isApplet_)
     {
       notApplicable("Deactivated until IFS classes use the new Converters");
       // The browser's JVM has no converter for Cp437.
       return;
     }
     String fileName = ifsPathName_ + "r91";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(systemObject_);
       os.setPath(fileName);
       os.setCCSID(0x01b5);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, fileName);
       assertCondition(is.read(s.length()).equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }


  /**
   Ensure that IFSTextFileInputStream.read() returns the String written by
   IFSTextFileOutputStream.write(String) using CCSID 0x34b0.
   **/
   public void Var092()
   {

     String fileName = ifsPathName_+ "r92";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(systemObject_);
       os.setPath(fileName);
       os.setCCSID(0x34b0);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, fileName);
       assertCondition(is.read(s.length()).equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

  /**
   Ensure that IFSTextFileInputStream.read() returns the String written by
   IFSTextFileOutputStream.write(String) using CCSID 0xf200.  Write to a file
   in QSYS.LIB.
   **/
   public void Var093()
   {
     String fileName = ifsPathName_ + "r93";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(systemObject_);
       os.setPath(fileName);
       os.setCCSID(0xf200);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, fileName);
       assertCondition(is.read(s.length()).equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

  /**
   Ensure that IFSTextFileInputStream.read() returns the String written by
   IFSTextFileOutputStream.write(String) using CCSID 37.  Write to a file
   in QSYS.LIB.
   **/
   public void Var094()
   {
     if (isApplet_)
     {
       notApplicable("Deactivated until IFS classes use the new Converters");
       // The browser's JVM has no converter for Cp037.
       return;
     }
     String fileName = ifsPathName_ + "r94";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       boolean rc1, rc2;
       IFSTextFileOutputStream os = new IFSTextFileOutputStream(systemObject_, fileName, 37);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, fileName);
       rc1 = is.read(s.length()).equals(s);
       is.close();
       
       // Try same test again with SHARE_NONE for APAR SE28717      @A1A
       IFSTextFileInputStream is2 =                               //@A1A
         new IFSTextFileInputStream(systemObject_,                //@A1A
                                    fileName,IFSFileInputStream.SHARE_NONE);
       rc2 = is2.read(s.length()).equals(s);                      //@A1A
       is2.close();                                               //@A1A
       assertCondition((rc1 == true) && (rc2 == true));           //@A1A
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

  /**
   Ensure that IFSTextFileInputStream.read() returns the String written by
   IFSTextFileOutputStream.write(String) using CCSID 37 after rewind is called.
   **/
   public void Var095()
   {
     if (isApplet_)
     {
       notApplicable("Deactivated until IFS classes use the new Converters");
       // The browser's JVM has no converter for Cp037.
       return;
     }
     String fileName = ifsPathName_ + "r95";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream(systemObject_, fileName, 37);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, fileName);
       if (is.read(s.length()).equals(s))
       {
         is.rewind();
         assertCondition (is.read(s.length()).equals(s));
       } else
       {
         failed("Wrong data returned");
       }

       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }


   // Start of IFSFileReader variations


/**
Ensure that ConnectionDroppedException is thrown by IFSFileReader.read()
if called after closed.
**/
  public void Var096()
  {
    String fileName = ifsPathName_ + "r96";
    createFile(fileName);
    try
    {
      IFSFileReader is =
        new IFSFileReader(new IFSFile(systemObject_, fileName));
      is.close();
      is.read();
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "IOException");
    }
    deleteFile(fileName);
  }

/**
Ensure that IFSFileReader.read() returns -1 at the end of file.
**/
  public void Var097()
  {
    String fileName = ifsPathName_ + "r97";
    boolean isIFSFile = false;
    boolean ready1,ready2;
    createFile(fileName);
    try
    {
      IFSFileReader is = new IFSFileReader(new IFSFile(systemObject_, fileName));
      int singleChar;
      try
      {
        ready1 = is.ready();
        singleChar = is.read();
        ready2 = is.ready();
        if (DEBUG) System.out.println("singleChar == " + singleChar);
      }
      catch(java.io.UnsupportedEncodingException e)
      {
        deleteFile(fileName);
        createIFSFile(fileName);
        isIFSFile = true;
        is.close();
        is = new IFSFileReader(new IFSFile(systemObject_, fileName));
        ready1 = is.ready();
        singleChar = is.read();
        ready2 = is.ready();
      }
      assertCondition(singleChar == -1 && ready1 == false && ready2 == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
    if (isIFSFile) deleteIFSFile(fileName);
    else deleteFile(fileName);
  }



  /**
   Ensure that IFSFileReader.read() returns the String written by
   IFSFileWriter.write(String) using CCSID 0x01b5.
   **/
   public void Var098()
   {
     if (isApplet_)
     {
       notApplicable("Deactivated until IFS classes use the new Converters");
       // The browser's JVM has no converter for Cp437.
       return;
     }
     String fileName = ifsPathName_ + "r98";
     IFSFile file = new IFSFile(systemObject_, fileName);
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     boolean ready1,ready2;
     try
     {
       IFSFileWriter os = new IFSFileWriter(file, 0x01b5);
       os.write(s);
       os.close();
       IFSFileReader is = new IFSFileReader(file, 0x01b5);
       char[] charBuf = new char[s.length() + 100];
       ready1 = is.ready();
       int numCharsRead = is.read(charBuf);
       ready2 = is.ready();
       String str = String.valueOf(charBuf, 0, numCharsRead);
       if (DEBUG) System.out.println("str == |" + str + "|");
       assertCondition(numCharsRead == s.length() && str.equals(s) &&
                       ready1 == true && ready2 == false);
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }


  /**
   Ensure that IFSFileReader.read() returns the String written by
   IFSFileWriter.write(String) using CCSID 0x34b0.
   **/
   public void Var099()
   {

     String fileName = ifsPathName_+ "r99";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       IFSFileWriter os = new IFSFileWriter(file, 0x34b0);
       os.write(s);
       os.close();
       IFSFileReader is = new IFSFileReader(file, 0x34b0);
       char[] charBuf = new char[s.length() + 20];
       int numCharsRead = is.read(charBuf);
       String str = String.valueOf(charBuf, 0, numCharsRead);
       if (DEBUG) System.out.println("str == |" + str + "|");
       assertCondition(numCharsRead == s.length() &&
                       str.equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

  /**
   Ensure that IFSFileReader.read() returns the String written by
   IFSFileWriter.write(String) using CCSID 0xf200.  Write to a file
   in QSYS.LIB.
   **/
   public void Var100()
   {
     String fileName = ifsPathName_ + "r100";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       IFSFileWriter os = new IFSFileWriter(file, 0xf200);
       os.write(s);
       os.close();

       IFSFileReader is = new IFSFileReader(file, 0xf200);
       char[] charBuf = new char[s.length() + 20];
       int numCharsRead = is.read(charBuf);
       assertCondition(numCharsRead == s.length() &&
                       String.valueOf(charBuf, 0, numCharsRead).equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

  /**
   Ensure that IFSFileReader.read() returns the String written by
   IFSFileWriter.write(String) using CCSID 37.  Write to a file
   in QSYS.LIB.
   **/
   public void Var101()
   {
     String fileName = ifsPathName_ + "r101";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       IFSFileWriter os = new IFSFileWriter(file, 37);
       os.write(s);
       os.close();

       IFSFileReader is = new IFSFileReader(file, 37);
       char[] charBuf = new char[s.length() + 20];
       int numCharsRead = is.read(charBuf);
       assertCondition(numCharsRead == s.length() &&
                       String.valueOf(charBuf, 0, numCharsRead).equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

  /**
   Ensure that IFSFileReader.read() returns the String written by
   IFSFileWriter.write(String) using CCSID 37 after reset() is called.
   **/
   public void Var102()
   {
     if (isApplet_)
     {
       notApplicable("Deactivated until IFS classes use the new Converters");
       // The browser's JVM has no converter for Cp037.
       return;
     }
     String fileName = ifsPathName_ + "r102";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       IFSFileWriter os = new IFSFileWriter(file, 37);
       os.write(s);
       os.close();
       IFSFileReader is = new IFSFileReader(file, 37);
       char[] charBuf = new char[s.length() + 20];
       int numCharsRead = is.read(charBuf);
       if (numCharsRead == s.length() &&
           String.valueOf(charBuf, 0, numCharsRead).equals(s))
       {
         is.reset();
         numCharsRead = is.read(charBuf);
         assertCondition(numCharsRead == s.length() &&
                         String.valueOf(charBuf, 0, numCharsRead).equals(s));
       } else
       {
         failed("Wrong data returned");
       }

       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }

   /**
    Test IFSFileReader.skip(long).
    **/
   public void Var103()
   {
     String fileName = ifsPathName_ + "r103";
     char[] dataIn = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
     createFileWriteChars(fileName, String.valueOf(dataIn));
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       IFSFileReader is = new IFSFileReader(file);
       char firstChar = (char)is.read();
       assertCondition(is.skip(1) == 1 && is.read() == '2' &&
                       is.skip(2) == 2 && is.read() == '5' &&
                       is.skip(3) == 3 && is.read() == '9', "firstChar="+firstChar);
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(fileName);
   }


   /**
    Use IFSFileReader.lockBytes(int) to lock the first byte of a file.  Ensure that
    others accessing this file cannot violate the lock.
    **/
   public void Var104()
   {
     String fileName = ifsPathName_ + "r104";
     char[] dataIn = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
     createFileWriteChars(fileName, String.valueOf(dataIn));
     IFSKey key = null;
     IFSFileReader is = null;
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       is = new IFSFileReader(file);
       key = is.lockBytes(1);
       IFSFileWriter os = new IFSFileWriter(new IFSFile(systemObject_, fileName));
       os.write('5');
       failed("Exception didn't occur."+key);
     }
     catch(Exception e)
     {
       assertExceptionIs(e, "ExtendedIOException",
                         ExtendedIOException.LOCK_VIOLATION);
     }
     try { is.close(); } catch (Exception e) {}
     deleteFile(fileName);
   }


   /**
    Use IFSFileReader.lockBytes(int) to lock the first byte of a file, then use call unlockBytes() to unlock the first byte.  Ensure that others can then access this file.
    **/
   public void Var105()
   {
     String fileName = ifsPathName_ + "r105";
     char[] dataIn = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
     createFileWriteChars(fileName, String.valueOf(dataIn));
     IFSKey key = null;
     IFSFileReader is = null;
     try
     {
       IFSFile file = new IFSFile(systemObject_, fileName);
       is = new IFSFileReader(file);
       key = is.lockBytes(1);
       is.unlockBytes(key);
       IFSFileWriter os = new IFSFileWriter(new IFSFile(systemObject_, fileName));
       os.write('5');
       os.flush();
       char firstChar = (char)is.read();
       if (DEBUG) System.out.println("First char: " + firstChar);
       assertCondition(firstChar == '5');
     }
     catch(Exception e)
     {
       failed(e);
     }
     try { if (is != null) is.close(); } catch (Exception e) {}
     deleteFile(fileName);
   }




  class IFSReadFullyThread extends Thread
  {
    boolean[] done = null;
    IFSRandomAccessFile file = null;
    int varToRun;

    IFSReadFullyThread(int varToRun, IFSRandomAccessFile file, boolean[] done)
    {
      this.varToRun = varToRun;
      this.file = file;
      this.done = done;
    }

    public void run()
    {
      try
      {
        if (varToRun == 55)
        {
          file.readFully(new byte[2]);
          done[0] = true;
          return;
        }

        if (varToRun == 62)
        {
          file.readFully(new byte[2], 0, 2);
          done[0] = true;
          return;
        }

        done[0] = false;
      }
      catch(Exception e)
      {
        System.out.println("Unexpected exception.");
        done[0] = false;
      }
    }
  }
}
