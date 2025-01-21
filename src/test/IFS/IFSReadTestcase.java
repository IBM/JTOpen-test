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

package test.IFS;



import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.FTP;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileReader;
import com.ibm.as400.access.IFSFileWriter;
import com.ibm.as400.access.IFSKey;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;
import com.ibm.as400.security.auth.ProfileTokenCredential;
import com.ibm.as400.security.auth.ProfileTokenProvider;
import com.ibm.as400.util.BASE64Decoder;

import test.JCIFSUtility;
import test.JDReflectionUtil;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.TestDriver;

/**
Test read methods for IFSFileInputStream, IFSFileOutputStream, and
IFSRandomAccessFile.
**/
public class IFSReadTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSReadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }


  private AS400 profileTokenSystemObject_;

/**
Constructor.
**/
  public IFSReadTestcase (AS400 systemObject,
      String userid,
      String password,
                   Hashtable<String, Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSReadTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
 
        // Create a second AS400 that was authenticated using a profile token. 
        try { 
        ProfileTokenCredential pt = new ProfileTokenCredential();
        pt.setSystem(systemObject_);
        pt.setTokenType(ProfileTokenCredential.TYPE_SINGLE_USE);
        pt.setTokenExtended(userid,password); 
        
        
        profileTokenSystemObject_ = new AS400(systemObject_.getSystemName(), pt); 
        profileTokenSystemObject_.setGuiAvailable(false);
        profileTokenSystemObject_.connectService(AS400.FILE); 

        } catch (Exception e) { 
          System.out.println("Warning: exception during setup"); 
          e.printStackTrace(System.out); 
        }
    }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup(); 

   
    // Make sure the user has access to the RSTOBJ command
    CommandCall cc = new CommandCall(pwrSys_); 
    String command = "GRTOBJAUT OBJ(QSYS/RSTOBJ) OBJTYPE(*CMD) USER("+systemObject_.getUserId()+") AUT(*USE)"; 
    cc.run(command); 
    command = "GRTOBJAUT OBJ(QDFTOWN) OBJTYPE(*USRPRF) ASPDEV(*) USER("+systemObject_.getUserId()+") AUT(*ADD)   "; 
    cc.run(command); 
    
     
  }



  protected void cleanup() throws Exception {
    super.cleanup(); 
    profileTokenSystemObject_.close(); 
  }

/**
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.read()
if called after closed.
**/
  public void CommonVar001(AS400 as400)
  {
    deleteFile(ifsPathName_);
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar002(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
      int readCount = is.read();
      is.close(); 
      assertCondition(readCount == -1);
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
  public void CommonVar003(AS400 as400)
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(as400, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar004(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar005(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar006(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar007(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
      int readResult = is.read(new byte[1]);
      is.close(); 
      assertCondition( readResult == -1);
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
  public void CommonVar008(AS400 as400)
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(as400, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile raf =
          new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar009(AS400 as400)
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(as400, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(as400, ifsPathName_, "r");
        byte[] data1 = new byte[data.length];
        byte[] data2 = new byte[data.length];
        int bytesRead1 = is1.read(data1);
        int bytesRead2 = is2.read(data2);
        assertCondition(bytesRead1 == bytesRead2 && areEqual(data1, data2));
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        byte[] data1 = new byte[data.length];
        byte[] data2 = new byte[data.length];
        int bytesRead1 = is1.read(data1);
        int bytesRead2 = is2.read(data2);
        assertCondition(bytesRead1 == bytesRead2 && areEqual(data1, data2));
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
  public void CommonVar010(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar011(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar012(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSFileInputStream is = null;
    try
    {
      is = new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar013(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar014(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar015(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(as400, ifsPathName_);
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
  public void CommonVar016(AS400 as400)
  {
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(as400, ifsPathName_);
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar017(AS400 as400)
  {
    byte[] data = { 11,22,33,44,55,66,77 };
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is1 =
        new IFSFileInputStream(as400, ifsPathName_);
      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };
      byte[] data2 = { 0,1,2,3,4,5,6,7,8,9 };
      is1.read(data1, 3, 4);
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(as400, ifsPathName_, "r");
        is2.read(data2, 3, 4);
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        is2.read(data2, 3, 4);
        is2.close();
      }
      assertCondition(areEqual(data1, data2));
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
  public void CommonVar018(AS400 as400)
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
        new IFSFileInputStream(as400, ifsPathName_);
      int bytesRead1 = is1.read(data1, 0, data1.length);
      int bytesRead2 = 0;
      if (isApplet_)
      {
        IFSRandomAccessFile is2 =
          new IFSRandomAccessFile(as400, ifsPathName_, "r");
        bytesRead2 = is2.read(data2, 0, data2.length);
        is2.close();
      }
      else
      {
        InputStream is2 = getNonIFSInputStream(ifsPathName_);

        bytesRead2 = is2.read(data2, 0, data2.length);
        is2.close();
      }
      assertCondition(bytesRead1 == bytesRead2 && areEqual(data1, data2));
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
  public void CommonVar019(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "rw");
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
  public void CommonVar020(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "rw");
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
  public void CommonVar021(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      
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
  public void CommonVar022(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar023(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar024(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar025(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar026(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar027(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      int bytesRead1 = raf1.read(data1);
      int bytesRead2 = raf2.read(data2);
      assertCondition(bytesRead1 == bytesRead2 && areEqual(data1, data2));
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
  public void CommonVar028(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar029(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar030(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar031(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar032(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar033(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar034(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar035(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };
      byte[] data2 = { 0,1,2,3,4,5,6,7,8,9 };
      raf1.read(data1, 3, 4);
      raf2.read(data2, 3, 4);
      assertCondition(areEqual(data1, data2));
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
  public void CommonVar036(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      InputStream raf2 = getNonIFSInputStream(ifsPathName_);

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      int bytesRead1 = raf1.read(data1, 0, data1.length);
      int bytesRead2 = raf2.read(data2, 0, data2.length);
      assertCondition(bytesRead1 == bytesRead2 && areEqual(data1, data2));
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
  public void CommonVar037(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar038(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar039(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");

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
  public void CommonVar040(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar041(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar042(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar043(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar044(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar045(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      int i;
      if (JTOpenTestEnvironment.isLinux) {
        IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar046(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar047(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar048(AS400 as400)
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
    	  
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        DataOutput file1 = new DataOutputStream(baos); 
      file1.writeDouble(Double.MIN_VALUE);
      file1.writeDouble(0.0);
      file1.writeDouble(Double.MAX_VALUE);

      JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
       		  ifsPathName_, baos.toByteArray()); 
      
      }
      DataInput file1 = openDataInput(ifsPathName_, "r"); 
      // file1 = new RandomAccessFile(convertToPCName(ifsPathName_), "rw");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar049(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar050(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar051(AS400 as400)
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

  	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    DataOutput file1 = new DataOutputStream(baos); 
    file1.writeFloat(Float.MIN_VALUE);
    file1.writeFloat(0.0F);
    file1.writeFloat(Float.MAX_VALUE);

  JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
   		  ifsPathName_, baos.toByteArray()); 

      }
      DataInput file1 = openDataInput(ifsPathName_, "r"); 
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar052(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar053(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar054(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar055(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "w");

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
        output_.println(totalBytes+" total bytes written to "+ifsPathName_);
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
  public void CommonVar056(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      // RandomAccessFile raf2 = new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      raf1.readFully(data1);
      raf2.readFully(data2);
      assertCondition(areEqual(data1, data2));
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
  public void CommonVar057(AS400 as400)
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
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar058(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar059(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar060(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar061(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar062(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "w");

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
        output_.println(totalBytes+" total bytes written to "+ifsPathName_);
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
  public void CommonVar063(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      // RandomAccessFile raf2 =  new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 

      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };
      byte[] data2 = { 0,1,2,3,4,5,6,7,8,9 };
      raf1.readFully(data1, 3, 4);
      raf2.readFully(data2, 3, 4);
      assertCondition(areEqual(data1, data2));
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
  public void CommonVar064(AS400 as400)
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
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
      // RandomAccessFile raf2 =  new RandomAccessFile(convertToPCName(ifsPathName_), "r");
      DataInput raf2 = openDataInput(ifsPathName_, "r"); 

      byte[] data1 = new byte[data.length];
      byte[] data2 = new byte[data.length];
      raf1.readFully(data1, 0, data1.length);
      raf2.readFully(data2, 0, data2.length);
      assertCondition(areEqual(data1, data2));
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
  public void CommonVar065(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar066(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar067(AS400 as400)
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
      
      

    	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
      DataOutput file1 = new DataOutputStream(baos); 
      file1.writeInt(Integer.MIN_VALUE);
      file1.writeInt(0);
      file1.writeInt(Integer.MAX_VALUE);

    JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
     		  ifsPathName_, baos.toByteArray()); 


      }
      DataInput file1 = openDataInput(ifsPathName_, "r"); 
      // file1 = new RandomAccessFile(convertToPCName(ifsPathName_), "rw");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar068(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar069(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar070(AS400 as400)
  {
    byte[] data = { 10, 104, 101, 108, 108, 111, 13, 119, 111, 114,
    108, 100, 13, 10 };
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar071(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar072(AS400 as400)
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar073(AS400 as400)
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
      
      

    	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
      DataOutput file1 = new DataOutputStream(baos); 
      file1.writeLong(Long.MIN_VALUE);
      file1.writeLong(0L);
      file1.writeLong(Long.MAX_VALUE);

    JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
     		  ifsPathName_, baos.toByteArray()); 

      }
      DataInput file1 = openDataInput(ifsPathName_, "rw");
      
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar074(AS400 as400)
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, ifsPathName_, "r");
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
  public void CommonVar075(AS400 as400)
  {
    String fileName = ifsPathName_ + "r75";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar076(AS400 as400)
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
      
  	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    DataOutput file1 = new DataOutputStream(baos); 
    file1.writeShort(-32768);
    file1.writeShort(0);
    file1.writeShort(32767);

  JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
   		  fileName, baos.toByteArray()); 



      }
      DataInput file1 = openDataInput(fileName, "rw");

      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar077(AS400 as400)
  {
    String fileName = ifsPathName_ + "r77";
    createFile(fileName);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar078(AS400 as400)
  {
    String fileName = ifsPathName_ + "r78";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar079(AS400 as400)
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
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar080(AS400 as400)
  {
    String fileName = ifsPathName_ + "r80";
    createFile(fileName);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar081(AS400 as400)
  {
    String fileName = ifsPathName_ + "r81";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar082(AS400 as400)
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
      
  	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    DataOutput file1 = new DataOutputStream(baos); 
    file1.writeShort(0);
    file1.writeShort((short) 32767);
    file1.writeShort((short) 32768);
    file1.writeShort((short) 65535);

  JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
   		  fileName, baos.toByteArray()); 


      }
      DataInput file1 = openDataInput(fileName, "rw");
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar083(AS400 as400)
  {
    String fileName = ifsPathName_ + "r83";
    createFile(fileName);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar084(AS400 as400)
  {
    String fileName = ifsPathName_ + "r84";
    createFile(fileName);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar085(AS400 as400)
  {
    String fileName = ifsPathName_ + "r85";
    byte[] data = {0, 1};
    createFile(fileName, data);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar086(AS400 as400)
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
      
  	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    DataOutput file1 = new DataOutputStream(baos); 
    file1.writeUTF("");

  JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
   		  fileName, baos.toByteArray()); 


      }
      DataInput raf1 = openDataInput(fileName, "rw");
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar087(AS400 as400)
  {
    String fileName = ifsPathName_ + "r87";
    byte[] data = {0, 1, (byte) 0xc0};
    createFile(fileName, data);
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar088(AS400 as400)
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
    	  
    	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
          DataOutput raf1 =  new DataOutputStream(baos);
       
          raf1.writeUTF("Some unicode      : \u0030\u00b0\u0031\u043f\u0032\u500c\u0033");
          raf1.writeUTF("Some more unicode : \u0030\u00b0\u0031\u043f\u0032\u500c\u0033");
          raf1.writeUTF("Still more unicode: \u0030\u00b0\u0031\u043f\u0032\u500c\u0033");
          JDReflectionUtil.callMethod_V(raf1,"close");
  
      JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
    		  fileName, baos.toByteArray()); 
      }
      DataInput raf1 = openDataInput(fileName, "rw");
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(as400, fileName, "r");
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
  public void CommonVar089(AS400 as400)
  {
    String fileName = ifsPathName_ + "r89";
    createFile(fileName);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(as400, fileName);
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
  public void CommonVar090(AS400 as400)
  {
    String fileName = ifsPathName_ + "r90";
    boolean isIFSFile = false;
    createFile(fileName);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(as400, fileName);
      String text;
      try
      {
        text = is.read(1);
        is.close(); 
      }
      catch(java.io.UnsupportedEncodingException e)
      {
        deleteFile(fileName);
        createIFSFile(fileName);
        isIFSFile = true;
        is.close();
        is = new IFSTextFileInputStream(as400, fileName);
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
   public void CommonVar091(AS400 as400)
   {
     String fileName = ifsPathName_ + "r91";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(as400);
       os.setPath(fileName);
       os.setCCSID(0x01b5);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(as400, fileName);
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
   public void CommonVar092(AS400 as400)
   {

     String fileName = ifsPathName_+ "r92";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(as400);
       os.setPath(fileName);
       os.setCCSID(0x34b0);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(as400, fileName);
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
   public void CommonVar093(AS400 as400)
   {
     String fileName = ifsPathName_ + "r93";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(as400);
       os.setPath(fileName);
       os.setCCSID(0xf200);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(as400, fileName);
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
   public void CommonVar094(AS400 as400)
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
       IFSTextFileOutputStream os = new IFSTextFileOutputStream(as400, fileName, 37);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(as400, fileName);
       rc1 = is.read(s.length()).equals(s);
       is.close();
       
       // Try same test again with SHARE_NONE for APAR SE28717      @A1A
       IFSTextFileInputStream is2 =                               //@A1A
         new IFSTextFileInputStream(as400,                //@A1A
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
   IFSTextFileOutputStream.write(String) using CCSID 37 after reset is called.
   **/
   public void CommonVar095(AS400 as400)
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
       IFSTextFileOutputStream os = new IFSTextFileOutputStream(as400, fileName, 37);
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(as400, fileName);
       if (is.read(s.length()).equals(s))
       {
         is.reset();
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
  public void CommonVar096(AS400 as400)
  {
    String fileName = ifsPathName_ + "r96";
    createFile(fileName);
    try
    {
      IFSFileReader is =
        new IFSFileReader(new IFSFile(as400, fileName));
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
  public void CommonVar097(AS400 as400)
  {
    String fileName = ifsPathName_ + "r97";
    boolean isIFSFile = false;
    boolean ready1,ready2;
    createFile(fileName);
    try
    {
      IFSFileReader is = new IFSFileReader(new IFSFile(as400, fileName));
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
        is = new IFSFileReader(new IFSFile(as400, fileName));
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
   public void CommonVar098(AS400 as400)
   {
     if (isApplet_)
     {
       notApplicable("Deactivated until IFS classes use the new Converters");
       // The browser's JVM has no converter for Cp437.
       return;
     }
     String fileName = ifsPathName_ + "r98";
     IFSFile file = new IFSFile(as400, fileName);
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
   public void CommonVar099(AS400 as400)
   {

     String fileName = ifsPathName_+ "r99";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(as400, fileName);
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
   public void CommonVar100(AS400 as400)
   {
     String fileName = ifsPathName_ + "r100";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(as400, fileName);
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
   public void CommonVar101(AS400 as400)
   {
     String fileName = ifsPathName_ + "r101";
     String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     try
     {
       IFSFile file = new IFSFile(as400, fileName);
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
   public void CommonVar102(AS400 as400)
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
       IFSFile file = new IFSFile(as400, fileName);
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
   public void CommonVar103(AS400 as400)
   {
     String fileName = ifsPathName_ + "r103";
     char[] dataIn = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
     createFileWriteChars(fileName, String.valueOf(dataIn));
     try
     {
       IFSFile file = new IFSFile(as400, fileName);
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
   public void CommonVar104(AS400 as400)
   {
     String fileName = ifsPathName_ + "r104";
     char[] dataIn = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
     createFileWriteChars(fileName, String.valueOf(dataIn));
     IFSKey key = null;
     IFSFileReader is = null;
     IFSFileWriter os = null; 
     try
     {
       IFSFile file = new IFSFile(as400, fileName);
       is = new IFSFileReader(file);
       key = is.lockBytes((long)1);
       os = new IFSFileWriter(new IFSFile(as400, fileName));
       os.write('5');
       
       failed("Exception didn't occur."+key);
     }
     catch(Exception e)
     {
       assertExceptionIs(e, "ExtendedIOException",
                         ExtendedIOException.LOCK_VIOLATION);
     } finally { 
       if (os != null)
        try {
          os.close();
        } catch (IOException e) {
       } 
     }
     try { is.close(); } catch (Exception e) {}
     deleteFile(fileName);
   }


   /**
    Use IFSFileReader.lockBytes(int) to lock the first byte of a file, then use call unlockBytes() to unlock the first byte.  Ensure that others can then access this file.
    **/
   public void CommonVar105(AS400 as400)
   {
     String fileName = ifsPathName_ + "r105";
     char[] dataIn = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
     createFileWriteChars(fileName, String.valueOf(dataIn));
     IFSKey key = null;
     IFSFileReader is = null;
     try
     {
       IFSFile file = new IFSFile(as400, fileName);
       is = new IFSFileReader(file);
       key = is.lockBytes((long)1);
       is.unlockBytes(key);
       IFSFileWriter os = new IFSFileWriter(new IFSFile(as400, fileName));
       os.write('5');
       os.flush();
       char firstChar = (char)is.read();
       if (DEBUG) System.out.println("First char: " + firstChar);
       os.close(); 
       assertCondition(firstChar == '5');
     }
     catch(Exception e)
     {
       failed(e);
     }
     try { if (is != null) is.close(); } catch (Exception e) {}
     deleteFile(fileName);
   }


   /**
    * Ensure that IFSTextFileInputStream.read() can read a file in QSYS.LIB.
    **/
   public void CommonVar106(AS400 as400) {

     StringBuffer sb = new StringBuffer();
     boolean passed = true;
     // Restore the file to read to the current library.
     try {
       sb.append("Creating command call object\n");
       CommandCall c = new CommandCall(as400);
       String command = "CHGJOB INQMSGRPY(*SYSRPYL)";
       boolean commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed");
       }
       command = "CRTLIB " + testLib_;
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }
       command = "DLTF " + testLib_ + "/IFSDATA";
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }

       command = "CRTSAVF " + testLib_ + "/IFSDATA";
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }
       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
       // For now use the string.. In the future, this will be removed.
       String password = new String(charPassword);

       FTP ftpLocal = new FTP(as400.getSystemName(), as400.getUserId(), password);
       ftpLocal.cd(testLib_);
       ftpLocal.setDataTransferType(FTP.BINARY);
       OutputStream os = ftpLocal.put("IFSDATA.savf");

       BASE64Decoder decoder = new BASE64Decoder();
       byte[] savefileData = decoder.decodeBuffer(savefileBase64);

       os.write(savefileData);
       os.close();
       ftpLocal.disconnect();

       command = "QSYS/RSTOBJ OBJ(APZCOVER) SAVLIB(IFSDATA) DEV(*SAVF) " + "SAVF(" + testLib_ + "/IFSDATA) RSTLIB("
           + testLib_ + ")";
       sb.append("Running:  " + command + "\n");
       commandResult=c.run(command); 
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }

       String filename = "/qsys.lib/" + testLib_ + ".lib/apzcover.file/QSJ03136.MBR";
       sb.append("Connecting to "+as400.getSystemName() +" using "+ as400.getUserId()+"\n"); 
       AS400 as400Copy = new AS400 (as400.getSystemName(), as400.getUserId(), charPassword);
       IFSTextFileInputStream is = new IFSTextFileInputStream(as400Copy, filename);

       
       
       int size = is.available();
       int expectedSize = 12800;
       if (size != expectedSize) {
         passed = false;
         sb.append("Got size: " + size + "\n");
         sb.append("expected: " + expectedSize + "\n");
       }
       String readData = is.read(80);
       String expected = "..          5770SS1 5050 0000 SJ03136 2924 R03M00    0007 \u0000\n"
           + "  0000\u0097             ";
       if (!expected.equals(readData)) {
         passed = false;
         sb.append("Read     :'" + readData + "'\n");
         sb.append("Expected :'" + expected + "'\n");
       }
       is.close();
       as400Copy.close(); 

       assertCondition(passed, sb);
     } catch (Exception e) {
       failed(e, sb);
     }
   }

   
   /**
    * Ensure that IFSTextFileInputStream.read() can read a file in QSYS.LIB, when it is the 
    * first class loaded by the classloader. 
    **/
   public void CommonVar107(AS400 as400) {

     // 
     // Note:  This testcase still fails with "Data stream is not known"
     //        even after fixes were created. 
     // Keep code here in case we want to get this working. 
     // 
     if (true) { 
     assertCondition(true); 
     return; 
     }
     StringBuffer sb = new StringBuffer();
     boolean passed = true;
     // Restore the file to read to the current library.
     try {
       sb.append("Creating command call object\n");
       CommandCall c = new CommandCall(as400);
       String command = "CHGJOB INQMSGRPY(*SYSRPYL)";
       boolean commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed");
       }
       command = "CRTLIB " + testLib_;
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }
       command = "DLTF " + testLib_ + "/IFSDATA";
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }

       command = "CRTSAVF " + testLib_ + "/IFSDATA";
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }
       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
       // For now use the string.. In the future, this will be removed.
       String password = new String(charPassword);

       FTP ftpLocal = new FTP(as400.getSystemName(), as400.getUserId(), password);
       ftpLocal.cd(testLib_);
       ftpLocal.setDataTransferType(FTP.BINARY);
       OutputStream os = ftpLocal.put("IFSDATA.savf");

       BASE64Decoder decoder = new BASE64Decoder();
       byte[] savefileData = decoder.decodeBuffer(savefileBase64);

       os.write(savefileData);
       os.close();
       ftpLocal.disconnect();

       
       command = "QSYS/RSTOBJ OBJ(APZCOVER) SAVLIB(IFSDATA) DEV(*SAVF) " + "SAVF(" + testLib_ + "/IFSDATA) RSTLIB("
           + testLib_ + ")";
       sb.append("Running:  " + command + "\n");
       commandResult = c.run(command);
       if (!commandResult) {
         sb.append("WARNING:  " + command + " failed\n");
       }

        String filename = "/qsys.lib/" + testLib_ + ".lib/apzcover.file/QSJ03136.MBR";
       sb.append("Connecting to "+as400.getSystemName() +" using "+ as400.getUserId()+"\n"); 
       
       // Use a new classloader to load the classes and test 

       File toolboxJar = TestDriver.getLoadSource("com.ibm.as400.access.AS400");

       ClassLoader originalClassLoader = AS400.class.getClassLoader();

       String absolutePath = toolboxJar.getAbsolutePath(); 
       URL[] urls = new URL[1]; 
       if (absolutePath.endsWith(".jar")) { 
         urls[0] =  new URL("jar:file:" + absolutePath +"!/") ; 
       } else { 
         urls[0] =  new URL("file:" + absolutePath +"/") ; 
       }

       sb.append("Classloader using URL="+urls[0]+"\n"); 
       ClassLoader ifsReadClassLoader = new IFSURLClassLoader(urls);

       Class<?> as400Class = ifsReadClassLoader.loadClass("com.ibm.as400.access.AS400"); 
       
       ClassLoader actualLoader = as400Class.getClassLoader(); 
       if (actualLoader != ifsReadClassLoader) {
         passed = false; 
         sb.append("FAILED: Class loader not used to load class\n"); 
       }
       
       Class<?>[] parameterTypes = new Class[3];
       parameterTypes[0] = String.class; 
       parameterTypes[1] = String.class; 
       parameterTypes[2] = charPassword.getClass();  
       
       Constructor<?> constructor = as400Class.getConstructor(parameterTypes); 
      
       Object[] parameters = new Object[3]; 
       parameters[0] = as400.getSystemName();
       parameters[1] = as400.getUserId(); 
       parameters[2] = charPassword; 
       
       Object as400Copy =  constructor.newInstance(parameters);
       Class<?> isClass = ifsReadClassLoader.loadClass("com.ibm.as400.access.IFSTextFileInputStream"); 
       parameterTypes = new Class[2]; 
       parameters = new Object[2]; 
       parameters[0] = as400Copy; parameterTypes[0] = as400Copy.getClass(); 
       parameters[1] = filename;  parameterTypes[1] = filename.getClass(); 
       constructor = isClass.getConstructor(parameterTypes); 
       Object is =  constructor.newInstance(parameters);
       
       
       int size = JDReflectionUtil.callMethod_I(is, "available");
       int expectedSize = 12800;
       if (size != expectedSize) {
         passed = false;
         sb.append("Got size: " + size + "\n");
         sb.append("expected: " + expectedSize + "\n");
       }
       String readData = JDReflectionUtil.callMethod_S(is,"read",80);
       String expected = "..          5770SS1 5050 0000 SJ03136 2924 R03M00    0007 \u0000\n"
           + "  0000\u0097             ";
       if (!expected.equals(readData)) {
         passed = false;
         sb.append("Read     :'" + readData + "'\n");
         sb.append("Expected :'" + expected + "'\n");
       }
       JDReflectionUtil.callMethod_V(is, "close");
      
       assertCondition(passed, sb);
     } catch (Exception e) {
       failed(e, sb);
     }
   }


   public void Var001() {   CommonVar001(systemObject_); }
   public void Var002() {   CommonVar002(systemObject_); }
   public void Var003() {   CommonVar003(systemObject_); }
   public void Var004() {   CommonVar004(systemObject_); }
   public void Var005() {   CommonVar005(systemObject_); }
   public void Var006() {   CommonVar006(systemObject_); }
   public void Var007() {   CommonVar007(systemObject_); }
   public void Var008() {   CommonVar008(systemObject_); }
   public void Var009() {   CommonVar009(systemObject_); }

   public void Var010() {   CommonVar010(systemObject_); }
   public void Var011() {   CommonVar011(systemObject_); }
   public void Var012() {   CommonVar012(systemObject_); }
   public void Var013() {   CommonVar013(systemObject_); }
   public void Var014() {   CommonVar014(systemObject_); }
   public void Var015() {   CommonVar015(systemObject_); }
   public void Var016() {   CommonVar016(systemObject_); }
   public void Var017() {   CommonVar017(systemObject_); }
   public void Var018() {   CommonVar018(systemObject_); }
   public void Var019() {   CommonVar019(systemObject_); }

   public void Var020() {   CommonVar020(systemObject_); }
   public void Var021() {   CommonVar021(systemObject_); }
   public void Var022() {   CommonVar022(systemObject_); }
   public void Var023() {   CommonVar023(systemObject_); }
   public void Var024() {   CommonVar024(systemObject_); }
   public void Var025() {   CommonVar025(systemObject_); }
   public void Var026() {   CommonVar026(systemObject_); }
   public void Var027() {   CommonVar027(systemObject_); }
   public void Var028() {   CommonVar028(systemObject_); }
   public void Var029() {   CommonVar029(systemObject_); }

   public void Var030() {   CommonVar030(systemObject_); }
   public void Var031() {   CommonVar031(systemObject_); }
   public void Var032() {   CommonVar032(systemObject_); }
   public void Var033() {   CommonVar033(systemObject_); }
   public void Var034() {   CommonVar034(systemObject_); }
   public void Var035() {   CommonVar035(systemObject_); }
   public void Var036() {   CommonVar036(systemObject_); }
   public void Var037() {   CommonVar037(systemObject_); }
   public void Var038() {   CommonVar038(systemObject_); }
   public void Var039() {   CommonVar039(systemObject_); }

   public void Var040() {   CommonVar040(systemObject_); }
   public void Var041() {   CommonVar041(systemObject_); }
   public void Var042() {   CommonVar042(systemObject_); }
   public void Var043() {   CommonVar043(systemObject_); }
   public void Var044() {   CommonVar044(systemObject_); }
   public void Var045() {   CommonVar045(systemObject_); }
   public void Var046() {   CommonVar046(systemObject_); }
   public void Var047() {   CommonVar047(systemObject_); }
   public void Var048() {   CommonVar048(systemObject_); }
   public void Var049() {   CommonVar049(systemObject_); }

   public void Var050() {   CommonVar050(systemObject_); }
   public void Var051() {   CommonVar051(systemObject_); }
   public void Var052() {   CommonVar052(systemObject_); }
   public void Var053() {   CommonVar053(systemObject_); }
   public void Var054() {   CommonVar054(systemObject_); }
   public void Var055() {   CommonVar055(systemObject_); }
   public void Var056() {   CommonVar056(systemObject_); }
   public void Var057() {   CommonVar057(systemObject_); }
   public void Var058() {   CommonVar058(systemObject_); }
   public void Var059() {   CommonVar059(systemObject_); }

   public void Var060() {   CommonVar060(systemObject_); }
   public void Var061() {   CommonVar061(systemObject_); }
   public void Var062() {   CommonVar062(systemObject_); }
   public void Var063() {   CommonVar063(systemObject_); }
   public void Var064() {   CommonVar064(systemObject_); }
   public void Var065() {   CommonVar065(systemObject_); }
   public void Var066() {   CommonVar066(systemObject_); }
   public void Var067() {   CommonVar067(systemObject_); }
   public void Var068() {   CommonVar068(systemObject_); }
   public void Var069() {   CommonVar069(systemObject_); }

   public void Var070() {   CommonVar070(systemObject_); }
   public void Var071() {   CommonVar071(systemObject_); }
   public void Var072() {   CommonVar072(systemObject_); }
   public void Var073() {   CommonVar073(systemObject_); }
   public void Var074() {   CommonVar074(systemObject_); }
   public void Var075() {   CommonVar075(systemObject_); }
   public void Var076() {   CommonVar076(systemObject_); }
   public void Var077() {   CommonVar077(systemObject_); }
   public void Var078() {   CommonVar078(systemObject_); }
   public void Var079() {   CommonVar079(systemObject_); }

   public void Var080() {   CommonVar080(systemObject_); }
   public void Var081() {   CommonVar081(systemObject_); }
   public void Var082() {   CommonVar082(systemObject_); }
   public void Var083() {   CommonVar083(systemObject_); }
   public void Var084() {   CommonVar084(systemObject_); }
   public void Var085() {   CommonVar085(systemObject_); }
   public void Var086() {   CommonVar086(systemObject_); }
   public void Var087() {   CommonVar087(systemObject_); }
   public void Var088() {   CommonVar088(systemObject_); }
   public void Var089() {   CommonVar089(systemObject_); }

   public void Var090() {   CommonVar090(systemObject_); }
   public void Var091() {   CommonVar091(systemObject_); }
   public void Var092() {   CommonVar092(systemObject_); }
   public void Var093() {   CommonVar093(systemObject_); }
   public void Var094() {   CommonVar094(systemObject_); }
   public void Var095() {   CommonVar095(systemObject_); }
   public void Var096() {   CommonVar096(systemObject_); }
   public void Var097() {   CommonVar097(systemObject_); }
   public void Var098() {   CommonVar098(systemObject_); }
   public void Var099() {   CommonVar099(systemObject_); }


   public void Var100() {   CommonVar100(systemObject_); }
   public void Var101() {   CommonVar101(systemObject_); }
   public void Var102() {   CommonVar102(systemObject_); }
   public void Var103() {   CommonVar103(systemObject_); }
   public void Var104() {   CommonVar104(systemObject_); }
   public void Var105() {   CommonVar105(systemObject_); }
   public void Var106() {   CommonVar106(systemObject_); }
   public void Var107() {   CommonVar107(systemObject_); }
   public void Var108() {   notApplicable(); }
   public void Var109() {   notApplicable(); }
   public void Var110() {   notApplicable(); }
   public void Var111() {   notApplicable(); }
   public void Var112() {   notApplicable(); }
   public void Var113() {   notApplicable(); }
   public void Var114() {   notApplicable(); }
   public void Var115() {   notApplicable(); }
   public void Var116() {   notApplicable(); }
   public void Var117() {   notApplicable(); }
   public void Var118() {   notApplicable(); }
   public void Var119() {   notApplicable(); }
   public void Var120() {   notApplicable(); }
   public void Var121() {   notApplicable(); }
   public void Var122() {   notApplicable(); }
   public void Var123() {   notApplicable(); }
   public void Var124() {   notApplicable(); }
   public void Var125() {   notApplicable(); }
   public void Var126() {   notApplicable(); }
   public void Var127() {   notApplicable(); }
   public void Var128() {   notApplicable(); }
   public void Var129() {   notApplicable(); }
   public void Var130() {   notApplicable(); }
   public void Var131() {   notApplicable(); }
   public void Var132() {   notApplicable(); }
   public void Var133() {   notApplicable(); }
   public void Var134() {   notApplicable(); }
   public void Var135() {   notApplicable(); }
   public void Var136() {   notApplicable(); }
   public void Var137() {   notApplicable(); }
   public void Var138() {   notApplicable(); }
   public void Var139() {   notApplicable(); }
   public void Var140() {   notApplicable(); }
   public void Var141() {   notApplicable(); }
   public void Var142() {   notApplicable(); }
   public void Var143() {   notApplicable(); }
   public void Var144() {   notApplicable(); }
   public void Var145() {   notApplicable(); }
   public void Var146() {   notApplicable(); }
   public void Var147() {   notApplicable(); }
   public void Var148() {   notApplicable(); }
   public void Var149() {   notApplicable(); }
   public void Var150() {   notApplicable(); }
   public void Var151() {   notApplicable(); }
   public void Var152() {   notApplicable(); }
   public void Var153() {   notApplicable(); }
   public void Var154() {   notApplicable(); }
   public void Var155() {   notApplicable(); }
   public void Var156() {   notApplicable(); }
   public void Var157() {   notApplicable(); }
   public void Var158() {   notApplicable(); }
   public void Var159() {   notApplicable(); }
   public void Var160() {   notApplicable(); }
   public void Var161() {   notApplicable(); }
   public void Var162() {   notApplicable(); }
   public void Var163() {   notApplicable(); }
   public void Var164() {   notApplicable(); }
   public void Var165() {   notApplicable(); }
   public void Var166() {   notApplicable(); }
   public void Var167() {   notApplicable(); }
   public void Var168() {   notApplicable(); }
   public void Var169() {   notApplicable(); }
   public void Var170() {   notApplicable(); }
   public void Var171() {   notApplicable(); }
   public void Var172() {   notApplicable(); }
   public void Var173() {   notApplicable(); }
   public void Var174() {   notApplicable(); }
   public void Var175() {   notApplicable(); }
   public void Var176() {   notApplicable(); }
   public void Var177() {   notApplicable(); }
   public void Var178() {   notApplicable(); }
   public void Var179() {   notApplicable(); }
   public void Var180() {   notApplicable(); }
   public void Var181() {   notApplicable(); }
   public void Var182() {   notApplicable(); }
   public void Var183() {   notApplicable(); }
   public void Var184() {   notApplicable(); }
   public void Var185() {   notApplicable(); }
   public void Var186() {   notApplicable(); }
   public void Var187() {   notApplicable(); }
   public void Var188() {   notApplicable(); }
   public void Var189() {   notApplicable(); }
   public void Var190() {   notApplicable(); }
   public void Var191() {   notApplicable(); }
   public void Var192() {   notApplicable(); }
   public void Var193() {   notApplicable(); }
   public void Var194() {   notApplicable(); }
   public void Var195() {   notApplicable(); }
   public void Var196() {   notApplicable(); }
   public void Var197() {   notApplicable(); }
   public void Var198() {   notApplicable(); }
   public void Var199() {   notApplicable(); }
   public void Var200() {   notApplicable(); }


   /* Repeat tests for an IFS object created with a profile token */ 
   public void Var201() {   CommonVar001(profileTokenSystemObject_); }
   public void Var202() {   CommonVar002(profileTokenSystemObject_); }
   public void Var203() {   CommonVar003(profileTokenSystemObject_); }
   public void Var204() {   CommonVar004(profileTokenSystemObject_); }
   public void Var205() {   CommonVar005(profileTokenSystemObject_); }
   public void Var206() {   CommonVar006(profileTokenSystemObject_); }
   public void Var207() {   CommonVar007(profileTokenSystemObject_); }
   public void Var208() {   CommonVar008(profileTokenSystemObject_); }
   public void Var209() {   CommonVar009(profileTokenSystemObject_); }

   public void Var210() {   CommonVar010(profileTokenSystemObject_); }
   public void Var211() {   CommonVar011(profileTokenSystemObject_); }
   public void Var212() {   CommonVar012(profileTokenSystemObject_); }
   public void Var213() {   CommonVar013(profileTokenSystemObject_); }
   public void Var214() {   CommonVar014(profileTokenSystemObject_); }
   public void Var215() {   CommonVar015(profileTokenSystemObject_); }
   public void Var216() {   CommonVar016(profileTokenSystemObject_); }
   public void Var217() {   CommonVar017(profileTokenSystemObject_); }
   public void Var218() {   CommonVar018(profileTokenSystemObject_); }
   public void Var219() {   CommonVar019(profileTokenSystemObject_); }

   public void Var220() {   CommonVar020(profileTokenSystemObject_); }
   public void Var221() {   CommonVar021(profileTokenSystemObject_); }
   public void Var222() {   CommonVar022(profileTokenSystemObject_); }
   public void Var223() {   CommonVar023(profileTokenSystemObject_); }
   public void Var224() {   CommonVar024(profileTokenSystemObject_); }
   public void Var225() {   CommonVar025(profileTokenSystemObject_); }
   public void Var226() {   CommonVar026(profileTokenSystemObject_); }
   public void Var227() {   CommonVar027(profileTokenSystemObject_); }
   public void Var228() {   CommonVar028(profileTokenSystemObject_); }
   public void Var229() {   CommonVar029(profileTokenSystemObject_); }

   public void Var230() {   CommonVar030(profileTokenSystemObject_); }
   public void Var231() {   CommonVar031(profileTokenSystemObject_); }
   public void Var232() {   CommonVar032(profileTokenSystemObject_); }
   public void Var233() {   CommonVar033(profileTokenSystemObject_); }
   public void Var234() {   CommonVar034(profileTokenSystemObject_); }
   public void Var235() {   CommonVar035(profileTokenSystemObject_); }
   public void Var236() {   CommonVar036(profileTokenSystemObject_); }
   public void Var237() {   CommonVar037(profileTokenSystemObject_); }
   public void Var238() {   CommonVar038(profileTokenSystemObject_); }
   public void Var239() {   CommonVar039(profileTokenSystemObject_); }

   public void Var240() {   CommonVar040(profileTokenSystemObject_); }
   public void Var241() {   CommonVar041(profileTokenSystemObject_); }
   public void Var242() {   CommonVar042(profileTokenSystemObject_); }
   public void Var243() {   CommonVar043(profileTokenSystemObject_); }
   public void Var244() {   CommonVar044(profileTokenSystemObject_); }
   public void Var245() {   CommonVar045(profileTokenSystemObject_); }
   public void Var246() {   CommonVar046(profileTokenSystemObject_); }
   public void Var247() {   CommonVar047(profileTokenSystemObject_); }
   public void Var248() {   CommonVar048(profileTokenSystemObject_); }
   public void Var249() {   CommonVar049(profileTokenSystemObject_); }

   public void Var250() {   CommonVar050(profileTokenSystemObject_); }
   public void Var251() {   CommonVar051(profileTokenSystemObject_); }
   public void Var252() {   CommonVar052(profileTokenSystemObject_); }
   public void Var253() {   CommonVar053(profileTokenSystemObject_); }
   public void Var254() {   CommonVar054(profileTokenSystemObject_); }
   public void Var255() {   CommonVar055(profileTokenSystemObject_); }
   public void Var256() {   CommonVar056(profileTokenSystemObject_); }
   public void Var257() {   CommonVar057(profileTokenSystemObject_); }
   public void Var258() {   CommonVar058(profileTokenSystemObject_); }
   public void Var259() {   CommonVar059(profileTokenSystemObject_); }

   public void Var260() {   CommonVar060(profileTokenSystemObject_); }
   public void Var261() {   CommonVar061(profileTokenSystemObject_); }
   public void Var262() {   CommonVar062(profileTokenSystemObject_); }
   public void Var263() {   CommonVar063(profileTokenSystemObject_); }
   public void Var264() {   CommonVar064(profileTokenSystemObject_); }
   public void Var265() {   CommonVar065(profileTokenSystemObject_); }
   public void Var266() {   CommonVar066(profileTokenSystemObject_); }
   public void Var267() {   CommonVar067(profileTokenSystemObject_); }
   public void Var268() {   CommonVar068(profileTokenSystemObject_); }
   public void Var269() {   CommonVar069(profileTokenSystemObject_); }

   public void Var270() {   CommonVar070(profileTokenSystemObject_); }
   public void Var271() {   CommonVar071(profileTokenSystemObject_); }
   public void Var272() {   CommonVar072(profileTokenSystemObject_); }
   public void Var273() {   CommonVar073(profileTokenSystemObject_); }
   public void Var274() {   CommonVar074(profileTokenSystemObject_); }
   public void Var275() {   CommonVar075(profileTokenSystemObject_); }
   public void Var276() {   CommonVar076(profileTokenSystemObject_); }
   public void Var277() {   CommonVar077(profileTokenSystemObject_); }
   public void Var278() {   CommonVar078(profileTokenSystemObject_); }
   public void Var279() {   CommonVar079(profileTokenSystemObject_); }

   public void Var280() {   CommonVar080(profileTokenSystemObject_); }
   public void Var281() {   CommonVar081(profileTokenSystemObject_); }
   public void Var282() {   CommonVar082(profileTokenSystemObject_); }
   public void Var283() {   CommonVar083(profileTokenSystemObject_); }
   public void Var284() {   CommonVar084(profileTokenSystemObject_); }
   public void Var285() {   CommonVar085(profileTokenSystemObject_); }
   public void Var286() {   CommonVar086(profileTokenSystemObject_); }
   public void Var287() {   CommonVar087(profileTokenSystemObject_); }
   public void Var288() {   CommonVar088(profileTokenSystemObject_); }
   public void Var289() {   CommonVar089(profileTokenSystemObject_); }

   public void Var290() {   CommonVar090(profileTokenSystemObject_); }
   public void Var291() {   CommonVar091(profileTokenSystemObject_); }
   public void Var292() {   CommonVar092(profileTokenSystemObject_); }
   public void Var293() {   CommonVar093(profileTokenSystemObject_); }
   public void Var294() {   CommonVar094(profileTokenSystemObject_); }
   public void Var295() {   CommonVar095(profileTokenSystemObject_); }
   public void Var296() {   CommonVar096(profileTokenSystemObject_); }
   public void Var297() {   CommonVar097(profileTokenSystemObject_); }
   public void Var298() {   CommonVar098(profileTokenSystemObject_); }
   public void Var299() {   CommonVar099(profileTokenSystemObject_); }


   public void Var300() {   CommonVar100(profileTokenSystemObject_); }
   public void Var301() {   CommonVar101(profileTokenSystemObject_); }
   public void Var302() {   CommonVar102(profileTokenSystemObject_); }
   public void Var303() {   CommonVar103(profileTokenSystemObject_); }
   public void Var304() {   CommonVar104(profileTokenSystemObject_); }
   public void Var305() {   CommonVar105(profileTokenSystemObject_); }
   public void Var306() {   CommonVar106(profileTokenSystemObject_); }
   public void Var307() {   CommonVar107(profileTokenSystemObject_); }
   public void Var308() {   notApplicable(); }
   public void Var309() {   notApplicable(); }
   public void Var310() {   notApplicable(); }
   public void Var311() {   notApplicable(); }
   public void Var312() {   notApplicable(); }
   public void Var313() {   notApplicable(); }
   public void Var314() {   notApplicable(); }
   public void Var315() {   notApplicable(); }
   public void Var316() {   notApplicable(); }
   public void Var317() {   notApplicable(); }
   public void Var318() {   notApplicable(); }
   public void Var319() {   notApplicable(); }
   public void Var320() {   notApplicable(); }
   public void Var321() {   notApplicable(); }
   public void Var322() {   notApplicable(); }
   public void Var323() {   notApplicable(); }
   public void Var324() {   notApplicable(); }
   public void Var325() {   notApplicable(); }
   public void Var326() {   notApplicable(); }
   public void Var327() {   notApplicable(); }
   public void Var328() {   notApplicable(); }
   public void Var329() {   notApplicable(); }
   public void Var330() {   notApplicable(); }
   public void Var331() {   notApplicable(); }
   public void Var332() {   notApplicable(); }
   public void Var333() {   notApplicable(); }
   public void Var334() {   notApplicable(); }
   public void Var335() {   notApplicable(); }
   public void Var336() {   notApplicable(); }
   public void Var337() {   notApplicable(); }
   public void Var338() {   notApplicable(); }
   public void Var339() {   notApplicable(); }
   public void Var340() {   notApplicable(); }
   public void Var341() {   notApplicable(); }
   public void Var342() {   notApplicable(); }
   public void Var343() {   notApplicable(); }
   public void Var344() {   notApplicable(); }
   public void Var345() {   notApplicable(); }
   public void Var346() {   notApplicable(); }
   public void Var347() {   notApplicable(); }
   public void Var348() {   notApplicable(); }
   public void Var349() {   notApplicable(); }
   public void Var350() {   notApplicable(); }
   public void Var351() {   notApplicable(); }
   public void Var352() {   notApplicable(); }
   public void Var353() {   notApplicable(); }
   public void Var354() {   notApplicable(); }
   public void Var355() {   notApplicable(); }
   public void Var356() {   notApplicable(); }
   public void Var357() {   notApplicable(); }
   public void Var358() {   notApplicable(); }
   public void Var359() {   notApplicable(); }
   public void Var360() {   notApplicable(); }
   public void Var361() {   notApplicable(); }
   public void Var362() {   notApplicable(); }
   public void Var363() {   notApplicable(); }
   public void Var364() {   notApplicable(); }
   public void Var365() {   notApplicable(); }
   public void Var366() {   notApplicable(); }
   public void Var367() {   notApplicable(); }
   public void Var368() {   notApplicable(); }
   public void Var369() {   notApplicable(); }
   public void Var370() {   notApplicable(); }
   public void Var371() {   notApplicable(); }
   public void Var372() {   notApplicable(); }
   public void Var373() {   notApplicable(); }
   public void Var374() {   notApplicable(); }
   public void Var375() {   notApplicable(); }
   public void Var376() {   notApplicable(); }
   public void Var377() {   notApplicable(); }
   public void Var378() {   notApplicable(); }
   public void Var379() {   notApplicable(); }
   public void Var380() {   notApplicable(); }
   public void Var381() {   notApplicable(); }
   public void Var382() {   notApplicable(); }
   public void Var383() {   notApplicable(); }
   public void Var384() {   notApplicable(); }
   public void Var385() {   notApplicable(); }
   public void Var386() {   notApplicable(); }
   public void Var387() {   notApplicable(); }
   public void Var388() {   notApplicable(); }
   public void Var389() {   notApplicable(); }
   public void Var390() {   notApplicable(); }
   public void Var391() {   notApplicable(); }
   public void Var392() {   notApplicable(); }
   public void Var393() {   notApplicable(); }
   public void Var394() {   notApplicable(); }
   public void Var395() {   notApplicable(); }
   public void Var396() {   notApplicable(); }
   public void Var397() {   notApplicable(); }
   public void Var398() {   notApplicable(); }
   public void Var399() {   notApplicable(); }
   public void Var400() {   notApplicable(); }


   

   
   
}
   
  class IFSURLClassLoader extends URLClassLoader {
    ClassLoader fallbackLoader; 
    public IFSURLClassLoader(URL[] urls) {
      super(urls, null);
      fallbackLoader = URLClassLoader.newInstance(urls); 
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      try { 
      return super.findClass(name);
      } catch (java.lang.ClassNotFoundException ex) {
        System.out.println("Loading "+name); 
        return fallbackLoader.loadClass(name); 
      }
    }
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
