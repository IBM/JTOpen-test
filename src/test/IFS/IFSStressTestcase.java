///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.IFS;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSKey;
import com.ibm.as400.access.IFSRandomAccessFile;

/**
Exercise various methods by requesting very demanding operations in terms
of size, number of requests, or use of system resources.
**/
public class IFSStressTestcase extends IFSGenericTestcase
{

/**
Constructor.
**/
  public IFSStressTestcase (AS400 systemObject,
      String userid,
      String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String   driveLetter,
                   AS400 pwrSys)//@A1C
    {
        super (systemObject, userid, password, "IFSStressTestcase",
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
Write and verify 8192 bytes using IFSFilOutputStream.write(int) and
IFSFileInputStream.read(int).
**/
  public void Var001()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      int i = 0;
      long start = System.currentTimeMillis();
      while (i < 8192)
      {
        os.write(i & 0xff);
        int value = is.read();
        if (value != (i & 0xff))
        {
          break;
        }
        i++;
      }
      long finish = System.currentTimeMillis();
      output_.println("Excecution time: " +
                      Long.toString((finish - start) / 1000) + " seconds");
      assertCondition(i == 8192);
      os.close();
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Write and verify 8192 bytes using IFSRandomAccessFile.write(int) and
IFSRandomAccessFile.read(int).
**/
  public void Var002()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      int i = 0;
      long start = System.currentTimeMillis();
      while (i < 8192)
      {
        os.write(i & 0xff);
        int value = is.read();
        if (value != (i & 0xff))
        {
          break;
        }
        i++;
      }
      long finish = System.currentTimeMillis();
      output_.println("Excecution time: " +
                      Long.toString((finish - start) / 1000) + " seconds");
      assertCondition(i == 8192);
      os.close();
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Perform a write request and a read request of 8Mb using
IFSFileOutputStream.write(byte[], int, int) and
IFSFileInputStream.read(byte[], int, int).
**/
  public void Var003()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long freeSpace = file.getFreeSpace();
      
      if (freeSpace < 0)                                                  //@A2A
      {                                                                   //@A2A
        // freeSpace should not return negative number... there was         @A2A
        // a bug fixed in IFSQuerySpaceRep.java for this problem.           @A2A
        failed("ERROR: file.getFreeSpace returned negative number.");     //@A2A
      }                                                                   //@A2A
      else
      {
        IFSFileOutputStream os =
          new IFSFileOutputStream(systemObject_, ifsPathName_);
        byte[] dataIn = new byte[8192000];
        output_.println("Initializing bytes ...");
        for (int i = 0; i < dataIn.length; i++)
          dataIn[i] = (byte) i;
        output_.println("Writing " + Integer.toString(dataIn.length) +
                        " bytes ...");
        long start = System.currentTimeMillis();
        os.write(dataIn, 0, dataIn.length);
        os.close();
        IFSFileInputStream is =
          new IFSFileInputStream(systemObject_, ifsPathName_);
        byte[] dataOut = new byte[8192000];
        output_.println("Reading " + Integer.toString(dataOut.length) +
                        " bytes ...");
        int bytesRead = is.read(dataOut);
        long finish = System.currentTimeMillis();
        output_.println("Excecution time: " +
                        Long.toString((finish - start) / 1000) + " seconds");
        output_.println("Verifying ...");
        assertCondition(bytesRead == dataOut.length && isEqual(dataIn, dataOut));
        is.close();
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Perform a write request and a read request of 8Mb using
IFSRandomAccessFile.write(byte[], int, int) and
IFSRandomAccessFile.read(byte[], int, int).
**/
  public void Var004()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      long freeSpace = file.getFreeSpace();
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      byte[] dataIn = new byte[8192000];
      output_.println("Initializing bytes ... freeSpace="+freeSpace);
      for (int i = 0; i < dataIn.length; i++)
        dataIn[i] = (byte) i;
      output_.println("Writing " + Integer.toString(dataIn.length) +
                      " bytes ...");
      long start = System.currentTimeMillis();
      raf.write(dataIn, 0, dataIn.length);
      raf.close();
      raf = new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      byte[] dataOut = new byte[8192000];
      output_.println("Reading " + Integer.toString(dataOut.length) +
                      " bytes ...");
      int bytesRead = raf.read(dataOut);
      long finish = System.currentTimeMillis();
      output_.println("Excecution time: " +
                      Long.toString((finish - start) / 1000) + " seconds");
      output_.println("Verifying ...");
      assertCondition(bytesRead == dataOut.length && isEqual(dataIn, dataOut));
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that at least 64 IFSFileInputStreams can access a file simultaneously.
**/
  public void Var005()
  {
    createFile(ifsPathName_, "abcdefghijklmnopqrstuvwxyz123456");
    try
    {
      IFSFileInputStream[] is = new IFSFileInputStream[64];
      output_.println("Constructing ...");
      int i = 0;
      while (i < is.length)
      {
        is[i] = new IFSFileInputStream(systemObject_, ifsPathName_);
        i++;
      }
      output_.println("Accessing ...");
      int j = 0;
      while (j < is.length)
      {
        if (is[j].read() == -1)
          break;
        j++;
      }
      output_.println("Closing ...");
      while(i > 0)
      {
        i--;
        is[i].close();
      }
      assertCondition(j == is.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that at least 64 IFSFileOutputStreams can access a file simultaneously.
**/
  public void Var006()
  {
    createFile(ifsPathName_, "abcdefghijklmnopqrstuvwxyz123456");
    try
    {
      IFSFileOutputStream[] os = new IFSFileOutputStream[64];
      output_.println("Constructing ...");
      int i = 0;
      while (i < os.length)
      {
        os[i] = new IFSFileOutputStream(systemObject_, ifsPathName_);
        i++;
      }
      output_.println("Accessing ...");
      int j = 0;
      while (j < os.length)
      {
        os[j].write(1);
        j++;
      }
      output_.println("Closing ...");
      while(i > 0)
      {
        i--;
        os[i].close();
      }
      assertCondition(j == os.length);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Place a separate lock on each byte of a 512 byte file using
IFSFileInputStream.lock(int).
**/
  public void Var007()
  {
    byte[] data = new byte[512];
    createFile(ifsPathName_, data);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      output_.println("Locking every byte ...");
      IFSKey[] key = new IFSKey[data.length];
      for (int i = 0; i < data.length; i++)
      {
        key[i] = is.lock(1);
        is.skip(1);
      }
      output_.println("Unlocking every byte ...");
      for (int i = 0; i < data.length; i++)
      {
        is.unlock(key[i]);
      }
      succeeded();
      is.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Place a separate lock on each byte of a 512 byte file using
IFSFileOutputStream.lock(int).
**/
  public void Var008()
  {
    byte[] data = new byte[512];
    createFile(ifsPathName_, data);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      output_.println("Locking every byte ...");
      IFSKey[] key = new IFSKey[data.length];
      for (int i = 0; i < data.length; i++)
      {
        key[i] = os.lock(1);
        os.write(1);
      }
      output_.println("Unlocking every byte ...");
      for (int i = 0; i < data.length; i++)
      {
        os.unlock(key[i]);
      }
      succeeded();
      os.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

/**
Ensure that IFSFile.list() returns the same directory entry names as
File.list() when run against /QSYS.LIB (usually contains hundreds of
entries).
**/
  public void Var009()
  {
    // Cannot run this variation on Linux or AIX since they use NFS         @A3A
    // for the Java I/O "File file2" object below.  Since most of the       @A3A
    // the setup on our Linux/AIX clients MOUNT over the root directory     @A3A
    // on the server, NFS does not allow a user to cross into other file    @A3A
    // systems.  Note from Chuck Emig (i.e. NFS developer):                 @A3A
    //    You can't mount / and then try to go into QDLS, QSYS, Qopensys... @A3A
    //    Each of those would need to be exported and mounted individually. @A3A
    //    Since you can mount over NFS, you can construct an identical      @A3A
    //    namespace if you mount over the correct subdirectories.           @A3A
    if (isApplet_ || linux_ || AIX_)                                      //@A3C
    {
      notApplicable();
      return;
    }
    try
    {
      // Use "pwrSys_" rather than "systemObject_"                   @A1A
      // Need higher auth to see USRPRF objects per IFS code change  @A1A
      IFSFile file1 = new IFSFile(pwrSys_, "/QSYS.LIB/QUSRSYS.LIB");    
      file1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);

      String[] names1 = file1.list();

      // File file2 = new File(convertToPCName(file1.getAbsolutePath()));
      // String[] names2 = file2.list();
      String[] names2 = listDirectory(file1.getAbsolutePath());


      boolean[] found2 = new boolean[names2.length]; 
      StringBuffer sb = new StringBuffer("\n");
      boolean passed = true;
      int jStart = 0; 
      for (int i = 0; i < names1.length; i++) {
	  boolean found = false; 
	  for (int j = jStart; j < names2.length && !found; j++) {
	      if (names1[i].equals(names2[j])) {
		  found = true;
		  found2[j] = true; 
		  if (j == jStart) {
		      jStart++; 
		  } 
	      }
	  } /* for j */  
	  if (!found) {
	      if (names1[i].indexOf("USRSPC") > 0) {
		      // ignore this
	      } else { 
		  passed =false;
		  sb.append("Did not find #"+i+" "+names1[i]+" in names2\n");
	      }
	  } 
      } /* for i */ 

      /* Check the other list */

	sb.append("items not found in names2\n");
	for (int j = 0; j < names2.length; j++) {
	    if (! found2[j]) {
		sb.append(names2[j]+"\n");
		passed = false; 
	    }
	} 

      assertCondition(passed, sb.toString()); 


    }
    catch(Exception e)
    {
      failed(e);
    }
  }

}



