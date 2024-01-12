///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSConnectTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IFS;

import java.io.FileOutputStream;
import java.io.File;

import java.util.Hashtable;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;
import com.ibm.as400.access.IFSKey;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.ConnectionDroppedException;

public class IFSConnectTestcase extends IFSGenericTestcase
{

/**
Constructor.
**/
  public IFSConnectTestcase (AS400 systemObject,
                   String userid, 
                   String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String   driveLetter,
                   AS400    pwrSys
                   )
    {
        super (systemObject, userid, password, "IFSConnectTestcase", 
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.available() if called after closed.
**/
  public void Var001()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.available();
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
Ensure that no exception is thrown by IFSFileInputStream.getSystem() if
called after closed.
**/
  public void Var002()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.getSystem();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.lock() if called after closed.
**/
  public void Var003()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.lock(1);
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.read() if called after closed.
**/
  public void Var004()
  {
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.read(byte[]) if called after closed.
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.read(byte[], int, int) if called after closed.
**/
  public void Var006()
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.skip(long) if called after closed.
**/
  public void Var007()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      is.close();
      is.skip(1);
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
Ensure that ConnectionDroppedException is thrown by IFSFileInputStream.unlock(IFSKey) if called after closed.
**/
  public void Var008()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      IFSKey key = is.lock(1);
      is.close();
      is.unlock(key);
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
Ensure that ConnectionDroppedException is thrown by IFSFileOutputStream.flush() if called after closed.
**/
  public void Var009()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.flush();
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
Ensure that no exception is thrown by IFSFileOutputStream.getSystem() if
called after closed.
**/
  public void Var010()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.getSystem();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by IFSFileOutputStream.lock() if called after closed.
**/
  public void Var011()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.lock(1);
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
Ensure that ConnectionDroppedException is thrown by IFSFileOutputStream.unlock(IFSKey) if called after closed.
**/
  public void Var012()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSKey key = os.lock(1);
      os.close();
      os.unlock(key);
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
Ensure that ConnectionDroppedException is thrown by IFSFileOutputStream.write(int) if called after closed.
**/
  public void Var013()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.write(1);
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
Ensure that ConnectionDroppedException is thrown by IFSFileOutputStream.write(byte[]) if called after closed.
**/
  public void Var014()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.write(new byte[1]);
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
Ensure that ConnectionDroppedException is thrown by IFSFileOutputStream.write(byte[], int, int) if called after closed.
**/
  public void Var015()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.write(new byte[1], 0, 1);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.flush() if called after closed.
**/
  public void Var016()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.flush();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.getFilePointer() if called after closed.
**/
  public void Var017()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.getFilePointer();
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
Ensure that no exception is thrown by IFSRandomAccessFile.getSystem() if
called after closed.
**/
  public void Var018()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.getSystem();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.length() if called after closed.
**/
  public void Var019()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.length();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.lock(int, int) if called after closed.
**/
  public void Var020()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.lock(0, 1);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.read() if called after closed.
**/
  public void Var021()
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.read(byte[]) if called after closed.
**/
  public void Var022()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.read(byte[], int, int) if called after closed.
**/
  public void Var023()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readBoolean() if called after closed.
**/
  public void Var024()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readByte() if called after closed.
**/
  public void Var025()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readChar() if called after closed.
**/
  public void Var026()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readDouble() if called after closed.
**/
  public void Var027()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readFloat() if called after closed.
**/
  public void Var028()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readFully(byte[]) if called after closed.
**/
  public void Var029()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readFully(new byte[1]);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readFully(byte[], int, int) if called after closed.
**/
  public void Var030()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readInt() if called after closed.
**/
  public void Var031()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readInt();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readLine() if called after closed.
**/
  public void Var032()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readLine();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readLong() if called after closed.
**/
  public void Var033()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readLong();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readShort() if called after closed.
**/
  public void Var034()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readShort();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readUnsignedShort() if called after closed.
**/
  public void Var035()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readUnsignedShort();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.readUTF() if called after closed.
**/
  public void Var036()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.readUTF();
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.seek(long) if called after closed.
**/
  public void Var037()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.seek(0);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.skipBytes(int) if called after closed.
**/
  public void Var038()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.skipBytes(1);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.unlock(IFSKey) if called after closed.
**/
  public void Var039()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      IFSKey key = raf.lock(0, 1);
      raf.close();
      raf.unlock(key);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.write(int) if called after closed.
**/
  public void Var040()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.write(1);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.write(byte[]) if called after closed.
**/
  public void Var041()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.write(new byte[1]);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.write(byte[], int, int) if called after closed.
**/
  public void Var042()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.write(new byte[1], 0, 1);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeBoolean(boolean) if called after closed.
**/
  public void Var043()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeBoolean(true);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeByte(int) if called after closed.
**/
  public void Var044()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeByte(1);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeBytes(String) if called after closed.
**/
  public void Var045()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeBytes("hello");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeChar(int) if called after closed.
**/
  public void Var046()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeChar(65);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeChars(String) if called after closed.
**/
  public void Var047()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeChars("hello");
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeDouble(double) if called after closed.
**/
  public void Var048()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeDouble(0.0);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeFloat(float) if called after closed.
**/
  public void Var049()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeFloat(0.0F);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeInt(int) if called after closed.
**/
  public void Var050()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeInt(0);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeLong(long) if called after closed.
**/
  public void Var051()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeLong(0L);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeShort(int) if called after closed.
**/
  public void Var052()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeShort(0);
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
Ensure that ConnectionDroppedException is thrown by IFSRandomAccessFile.writeUTF(String) if called after closed.
**/
  public void Var053()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.close();
      raf.writeUTF("hello");
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
   Ensure that ConnectionDroppedException is thrown by IFSTextFileInputStream.read() if called after closed.
   **/
  public void Var054()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, ifsPathName_);
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
   Ensure that ConnectionDroppedException is thrown by IFSTextFileOutputStream.write(int) if called after closed.
   **/
  public void Var055()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifsPathName_);
      os.close();
      os.write("foobar");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ConnectionDroppedException",
                        ConnectionDroppedException.CONNECTION_NOT_ACTIVE);
    }
    deleteFile(ifsPathName_);
  }


}





