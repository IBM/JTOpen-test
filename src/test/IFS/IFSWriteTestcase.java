///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSWriteTestcase.java
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
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSFileReader;
import com.ibm.as400.access.IFSFileWriter;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;
import com.ibm.as400.access.NLS;


/**
Test write methods for IFSFileInputStream, IFSFileOutputStream, and
IFSRandomAccessFile.
**/
public class IFSWriteTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSWriteTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }

/**
Constructor.
**/
  public IFSWriteTestcase (AS400 systemObject,
String userid,
String password,
      Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSWriteTestcase",
            namesAndVars, runMode, fileOutputStream, pwrSys);
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
Write every possible byte to a file using IFSFileOutputStream.write(int).
**/
  public void Var001()
  {
	  IFSFileOutputStream os = null; 
    try
    {
      os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
        os.write(data[i]);
      }
      os.close();
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf.readByte() == data[i])
        i++;
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
	}
	if (os != null) {
		try {
			os.close();
		} catch (IOException e) {
		}
	}
	deleteFile(ifsPathName_);
  }

/**
Ensure the NullPointerException is thrown if argument one of
IFSFileOutputStream.write(byte[]) is null.
**/
  public void Var002()
  {
    IFSFileOutputStream os = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.write((byte[]) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "data");
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileOutputStream.write(byte[]) does not write anything if
argument one has length zero.
**/
  public void Var003()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long lengthBefore = file.length();
      os.write(new byte[0]);
      long lengthAfter = file.length();
      os.close(); 
      assertCondition(lengthBefore == lengthAfter);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Write every possible byte to a file using IFSFileOutputStream.write(byte[]).
**/
  public void Var004()
  {
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
      }
      os.write(data);
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf.readByte() == data[i])
        i++;
      os.close(); 
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure the NullPointerException is thrown if argument one of
IFSFileOutputStream.write(byte[], int, int) is null.
**/
  public void Var005()
  {
    IFSFileOutputStream os = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.write((byte[]) null, 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "data");
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument two of
IFSFileOutputStream.write(byte[], int, int) is < 0.
**/
  public void Var006()
  {
    IFSFileOutputStream os = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.write(new byte[20], -1, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument three of
IFSFileOutputStream.write(byte[], int, int) is < 0.
**/
  public void Var007()
  {
    IFSFileOutputStream os = null;
    try
    {
      os = new IFSFileOutputStream(systemObject_, ifsPathName_);
      os.write(new byte[20], 0, -1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFileOutputStream.write(byte[], int, int) does not write
anything if argument three is zero.
**/
  public void Var008()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long lengthBefore = file.length();
      os.write(new byte[1], 0, 0);
      os.close();
      long lengthAfter = file.length();
      assertCondition(lengthBefore == lengthAfter);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Write every possible byte to a file using
IFSFileOutputStream.write(byte[], int, int).
**/
  public void Var009()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
      }
      os.write(data, 0, data.length);
      os.close();
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf.readByte() == data[i])
        i++;
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write every possible byte to a file using IFSRandomAccessFile.write(int).
**/
  public void Var010()
  {
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
        raf1.write(data[i]);
      }
      raf1.close();
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf2.readByte() == data[i])
        i++;
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure the NullPointerException is thrown if argument one of
IFSRandomAccessFile.write(byte[]) is null.
**/
  public void Var011()
  {
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write((byte[]) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "data");
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.write(byte[]) does not write anything if
argument one has length zero.
**/
  public void Var012()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSRandomAccessFile os =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long lengthBefore = file.length();
      os.write(new byte[0]);
      long lengthAfter = file.length();
      assertCondition(lengthBefore == lengthAfter);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Write every possible byte to a file using IFSRandomAccessFile.write(byte[]).
**/
  public void Var013()
  {
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
      }
      raf1.write(data);
      raf1.close();
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf2.readByte() == data[i])
        i++;
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure the NullPointerException is thrown if argument one of
IFSRandomAccessFile.write(byte[], int, int) is null.
**/
  public void Var014()
  {
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write((byte[]) null, 0, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "data");
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument two of
IFSRandomAccessFile.write(byte[], int, int) is < 0.
**/
  public void Var015()
  {
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write(new byte[20], -1, 1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that ExtendedIllegalArgumentException is thrown if argument three of
IFSRandomAccessFile.write(byte[], int, int) is < 0.
**/
  public void Var016()
  {
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.write(new byte[20], 0, -1);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSRandomAccessFile.write(byte[], int, int) does not write
anything if argument three is zero.
**/
  public void Var017()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long lengthBefore = file.length();
      raf.write(new byte[1], 0, 0);
      raf.close();
      long lengthAfter = file.length();
      assertCondition(lengthBefore == lengthAfter);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Write every possible byte to a file using
IFSRandomAccessFile.write(byte[], int, int).
**/
  public void Var018()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
      }
      raf1.write(data, 0, data.length);
      raf1.close();
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf2.readByte() == data[i])
        i++;
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write true and false using IFSRandomAccessFile.writeBoolean(boolean).
**/
  public void Var019()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file1.writeBoolean(false);
      file1.writeBoolean(true);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file2.readBoolean() == false && file2.readBoolean() == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write every possible byte to a file using IFSRandomAccessFile.writeByte(int).
**/
  public void Var020()
  {
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      byte[] data = new byte[256];
      int i = 0;
      for (; i < data.length; i++)
      {
        data[i] = (byte) i;
        raf1.writeByte(data[i]);
      }
      raf1.close();
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      i = 0;
      while (i < data.length && raf2.readByte() == data[i])
        i++;
      assertCondition(i == data.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by
IFSRandomAccessFile.writeBytes(String) if argument one is null.
**/
  public void Var021()
  {
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.writeBytes((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "s");
    }
    deleteFile(ifsPathName_);
  }

/**
Write a string containing a variety of unicode characters using
IFSRandomAccessFile.writeBytes(String).
**/
  public void Var022()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      String s = "";
      for (int i = 0; i < 0x80; i++)
        s += (char) i;
      s += (char) 0x43f;
      s += (char) 0x7ff;
      s += (char) 0x800;
      s += (char) 0xffff;
      file1.writeBytes(s);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      byte[] data1 = new byte[s.length()];
      byte[] data2 = new byte[data1.length];
      for (int i = 0; i < data2.length; i++)
      {
        data2[i] = (byte) (s.charAt(i) & 0xff);
      }
      file2.read(data1);
      assertCondition(areEqual(data1, data2));
      file2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write characters \u0000 through \u0080, \u7fff, \u8000, and \uffff using
IFSRandomAccessFile.writeChar(int).
**/
  public void Var023()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      String s = "";
      for (int i = 0; i < 0x80; i++)
        s += (char) i;
      s += (char) 0x7fff;
      s += (char) 0x8000;
      s += (char) 0xffff;
      for (int i = 0; i < s.length(); i++)
        file1.writeChar((int) s.charAt(i));
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      int i = 0;
      while (i < s.length() && file2.readChar() == s.charAt(i))
        i++;
      assertCondition(i == s.length());
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by
IFSRandomAccessFile.writeChars(String) if argument one is null.
**/
  public void Var024()
  {
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.writeChars((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "s");
    }
    deleteFile(ifsPathName_);
  }

/**
Write characters \u0000 through \u0080, \u7fff, \u8000, and \uffff using
IFSRandomAccessFile.writeChars(String).
**/
  public void Var025()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      String s = "";
      for (int i = 0; i < 0x80; i++)
        s += (char) i;
      s += (char) 0x7fff;
      s += (char) 0x8000;
      s += (char) 0xffff;
      file1.writeChars(s);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      int i = 0;
      while (i < s.length() && file2.readChar() == s.charAt(i))
        i++;
      assertCondition(i == s.length());
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write the minimum Double value, 0.0, and the maximum Double value using
IFSRandomAccessFile.writeDouble(double).
**/
  public void Var026()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file1.writeDouble(Double.MIN_VALUE);
      file1.writeDouble(0.0);
      file1.writeDouble(Double.MAX_VALUE);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file2.readDouble() == Double.MIN_VALUE &&
             file2.readDouble() == 0.0 &&
             file2.readDouble() == Double.MAX_VALUE);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write the minimum Float value, 0.0, and the maximum Float value using
IFSRandomAccessFile.writeFloat(double).
**/
  public void Var027()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file1.writeFloat(Float.MIN_VALUE);
      file1.writeFloat(0.0F);
      file1.writeFloat(Float.MAX_VALUE);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file2.readFloat() == Float.MIN_VALUE &&
             file2.readFloat() == 0.0F &&
             file2.readFloat() == Float.MAX_VALUE);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


/**
Write the minimum Integer value, 0, and the maximum Integer value using
IFSRandomAccessFile.writeInt(int).
**/
  public void Var028()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file1.writeInt(Integer.MIN_VALUE);
      file1.writeInt(0);
      file1.writeInt(Integer.MAX_VALUE);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file2.readInt() == Integer.MIN_VALUE &&
             file2.readInt() == 0 &&
             file2.readInt() == Integer.MAX_VALUE);
      file2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write the minimum Integer value, 0, and the maximum Integer value using
IFSRandomAccessFile.writeLong(int).
**/
  public void Var029()
  {
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file1.writeLong(Long.MIN_VALUE);
      file1.writeLong(0L);
      file1.writeLong(Long.MAX_VALUE);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file2.readLong() == Long.MIN_VALUE &&
             file2.readLong() == 0L &&
             file2.readLong() == Long.MAX_VALUE);
      file2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write the minimum short value, 0, and the maximum short value using
IFSRandomAccessFile.writeShort(int).
**/
  public void Var030()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file1.writeShort(-32768);
      file1.writeShort(0);
      file1.writeShort(32767);
      file1.close();
      IFSRandomAccessFile file2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(file2.readShort() == -32768 && file2.readShort() == 0 &&
             file2.readShort() == 32767);
      file2.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that NullPointerException is thrown by
IFSRandomAccessFile.writeUTF(String) if argument one is null.
**/
  public void Var031()
  {
    IFSRandomAccessFile file = null;
    try
    {
      file = new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.writeUTF((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "s");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

/**
Write an empty string using IFSRandomAccessFile.writeUTF(String).
**/
  public void Var032()
  {
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf1.writeUTF("");
      raf1.close();
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      String s = raf2.readUTF();
      assertCondition(s.length() == 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write several strings containing a variety of unicode characters using
IFSRandomAccessFile.writeUTF(String).
**/
  public void Var033()
  {
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      String unicode = "";
      for (int i = 0; i < 0x80; i++)
        unicode += (char) i;
      unicode += (char) 0x43f;
      unicode += (char) 0x7ff;
//      unicode += (char) 0x800;
//      unicode += (char) 0xffff;
      String s1 = "Some unicode: " + unicode;
      String s2 = "Some more unicode: " + unicode;
      String s3 = "Still more unicode: " + unicode;
      raf1.writeUTF(s1);
      raf1.writeUTF(s2);
      raf1.writeUTF(s3);
      raf1.close();
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      assertCondition(raf2.readUTF().equals(s1) && raf2.readUTF().equals(s2) &&
             raf2.readUTF().equals(s3));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Test IFSFileOutputStream.flush().
**/
  public void Var034()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      os.write(1);
      os.flush();
      int readCount = is.read(); 
      os.close(); 
      is.close(); 
      assertCondition(readCount  == 1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


  /**
   Ensure that IFSRandomAccessFile.write(byte[], int, int) will write
   past the EOF.
   **/
  public void Var035()
  {
    byte[] data = { 1, 2, 3, 4 };

    String fileName = ifsPathName_ + "w35";
    createFile(fileName, data);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, fileName, "rw");
      file.seek(file.length() + 1);
      byte[] moreData = { 5 };
      file.write(moreData, 0, 1);
      byte[] dataOut = new byte[6];
      file.seek(0);
      assertCondition(file.read(dataOut) == 6 && file.length() == 6);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }


/**
Test IFSTextFileOutputStream.flush().
**/
  public void Var036()
  {
    String fileName = ifsPathName_ + "w36";
    String s = "The quick brown fox.";
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream();
      os.setSystem(systemObject_);
      os.setPath(fileName);
      int ccsid = 0x34b0;
      os.setCCSID(ccsid);
      os.write(s);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, fileName);
      os.flush();
      boolean isOk = true;
      if (!is.read(s.length()).equals(s))
      {
        output_.println("Wrong data was read back.");
        isOk = false;
      }
      os.close();
      is.close();
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.getCCSID() != ccsid)
      {
        output_.println("Wrong CCSID: " + file.getCCSID());
        isOk = false;
      }
      assertCondition(isOk);
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
   Ensure that IFSTextFileOutputStream.write(String) will write the data
   in the file CCSID if a CCSID is not specified on the
   IFSTextFileOutputStream class.
   **/
  public void Var037()
  {
    String s = "The quick brown fox.";
    String fileName = ifsPathName_ + "w37";
    createIFSFile(fileName);  // avoid UnsupportedEncodingException
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream();
      os.setSystem(systemObject_);
      os.setPath(fileName);
      os.write(s);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, fileName);
      os.flush();
      assertCondition(is.read(s.length()).equals(s));
      os.close();
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteIFSFile(fileName);
  }


  /**
   Ensure that IFSTextFileOutputStream.write(String) will write the data
   in the CCSID specified by the user.
   **/
  public void Var038()
  {
    if (isApplet_)
    {
      notApplicable("Deactivated until IFS classes use the new Converters");
      // The browser's JVM has no converter for Cp037.
      return;
    }
    String s = "The quick brown fox.";
    String fileName = ifsPathName_ + "w38";
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream();
      os.setSystem(systemObject_);
      os.setPath(fileName);
      int ccsid = 37;
      os.setCCSID(ccsid);
      os.write(s);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, fileName);
      os.flush();
      boolean isOk = true;
      if (!is.read(s.length()).equals(s))
      {
        output_.println("Wrong data was read back.");
        isOk = false;
      }
      os.close();
      is.close();
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.getCCSID() != ccsid)
      {
        output_.println("Wrong CCSID: " + file.getCCSID());
        isOk = false;
      }
      assertCondition(isOk);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  /**
   Ensure that IFSTextFileOutputStream.write(String) will write the data
   in the CCSID specified by the user.
   **/
  public void Var039()
  {
    if (isApplet_)
    {
      notApplicable("Deactivated until IFS classes use the new Converters");
      // The browser's JVM has no converter for Cp037.
      return;
    }
    String s = "The quick brown fox.";
    String fileName = ifsPathName_ + "w39";
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, fileName, 37);
      os.write(s);
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, fileName);
      os.flush();
      assertCondition(is.read(s.length()).equals(s));
      os.close();
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that a ExtendedIllegalStateException is thrown when setting
   the CCSID after the file is opened.
   **/
  public void Var040()
  {
    // String s = "The quick brown fox.";
    String fileName = ifsPathName_ + "w40";
    createIFSFile(fileName);  // avoid UnsupportedEncodingException

    StringBuffer failMsg = new StringBuffer();
    IFSTextFileOutputStream os = null;

    try {
      os = new IFSTextFileOutputStream(systemObject_, fileName);
      os.setCCSID(37);
      failMsg.append("Exception didn't occur.\n");
    }
    catch(ExtendedIllegalStateException e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException",
                               "CCSID", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED)) {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
      }
    }
    catch(Exception e1)
    {
      failMsg.append("Unexpected exception.\n");
      e1.printStackTrace(output_);
    }

    try {
      os.close();
    }
    catch (Exception e) { }

    deleteIFSFile(fileName);

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }

  }


/**
Test IFSFileWriter.flush().
**/
  public void Var041()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileWriter os = new IFSFileWriter(file);
      IFSFileReader is = new IFSFileReader(file);
      os.write('1');
      os.flush();
      char firstChar = (char)is.read();
      ///if (DEBUG) System.out.println("First char: " + firstChar);
      os.close(); 
      assertCondition(firstChar == '1');
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


/**
Test IFSFileWriter.getCCSID() and getEncoding(), using system default settings.
**/
  public void Var042()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileWriter os = new IFSFileWriter(file);
      if (DEBUG) {
        System.out.println("For IFSFileWriter: CCSID == " + os.getCCSID() + "; encoding == " + os.getEncoding());
        System.out.println("For IFSFile: CCSID == " + file.getCCSID());
        System.out.println("For AS400: CCSID == " + systemObject_.getCcsid() + "; encoding == " + systemObject_.getJobCCSIDEncoding());
      }
      assertCondition(os.getCCSID() == file.getCCSID() &&
                      os.getEncoding().equals(NLS.ccsidToEncoding(file.getCCSID())));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


/**
Test IFSFileWriter.getCCSID() and getEncoding(), after specifying CCSID.
**/
  public void Var043()
  {
    createFile(ifsPathName_, "abcdef");
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileWriter os = new IFSFileWriter(file, 13488);
      if (DEBUG) {
        System.out.println("For IFSFileWriter: CCSID == " + os.getCCSID() + "; encoding == " + os.getEncoding());
        System.out.println("For IFSFile: CCSID == " + file.getCCSID());
        System.out.println("For AS400: CCSID == " + systemObject_.getCcsid() + "; encoding == " + systemObject_.getJobCCSIDEncoding());
      }
      assertCondition(os.getCCSID() == 13488 &&
                      os.getEncoding().equals(NLS.ccsidToEncoding(13488)));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


}



