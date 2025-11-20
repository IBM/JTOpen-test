///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSFileDescriptorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IFS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileDescriptor;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;

import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;

/**
Test methods not covered by other testcases.
**/
public class IFSFileDescriptorTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSFileDescriptorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  private IFSFile ifsFile_ = null;
  private String fileName = "/JAVATEST/AFILE";
  private String mode_ = "rw";
  private int share_ = IFSRandomAccessFile.SHARE_ALL;
  private int existOption_ = IFSRandomAccessFile.OPEN_OR_CREATE;

/**
Constructor.
**/
  public IFSFileDescriptorTestcase (AS400 systemObject,
        String userid, 
          String password,
                   Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password,  "IFSFileDescriptorTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
        ifsFile_ = new IFSFile(systemObject_, fileName);
    }

/**
Testcase setup.
 @exception  Exception  If an exception occurs.
**/
  protected void setup()
    throws Exception
  {
    super.setup(); 
  }



/**
Ensure that the sync() method can be called from an IFSFileDescriptor
object instantiated by the constructor.
**/
  public void Var001()
  {
    ///setVariation(1);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSRandomAccessFile aFile = null;
    try
    {
       aFile = new IFSRandomAccessFile();
       aFile.setSystem(systemObject_);
       aFile.setPath(fileName);
       aFile.setMode(mode_);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

/**
Ensure that the sync() method can be called from an IFSFileDescriptor
object instantiated by IFSFileDescriptor(as400,name,mode).
**/
  public void Var002()
  {
    ///setVariation(2);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSRandomAccessFile aFile = null;
    try
    {
       aFile = new IFSRandomAccessFile(systemObject_, fileName, mode_);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

/**
Ensure that the sync() method can be called from an IFSFileDescriptor
object instantiated by IFSFileDescriptor(as400,name,mode,share,existOption).
**/
  public void Var003()
  {
    ///setVariation(3);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSRandomAccessFile aFile = null;
    try
    {
       aFile = new IFSRandomAccessFile(systemObject_, fileName, mode_, share_, existOption_);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

/**
Ensure that the sync() method can be called from an IFSFileDescriptor
object instantiated by IFSFileDescriptor(AS400,IFSFile,String,int,int).
**/
  @SuppressWarnings("deprecation")
  public void Var004()
  {
    ///setVariation(4);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSRandomAccessFile aFile = null;
    try
    {
       aFile = new IFSRandomAccessFile(systemObject_, ifsFile_, mode_, share_, existOption_);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

/**
 Ensure that the sync() method can be called from an IFSFileOutputStream
 object instantiated by IFSFileOutputStream constructor.
**/
  public void Var005()
  {
    ///setVariation(5);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSFileOutputStream aFile = null;
    try
    {
       aFile = new IFSFileOutputStream();
       aFile.setSystem(systemObject_);
       aFile.setPath(fileName);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

/**
 Ensure that the sync() method can be called from an IFSFileOutputStream
 object instantiated by IFSFileOutputStream(AS400,String,int,boolean)
**/
  public void Var006()
  {
    ///setVariation(6);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSFileOutputStream aFile = null;
    try
    {
       aFile = new IFSFileOutputStream(systemObject_, fileName, share_, true);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

/**
 Ensure that the sync() method can be called from an IFSFileOutputStream
 object instantiated by IFSFileOutputStream constructor.
**/
  @SuppressWarnings("deprecation")
  public void Var007()
  {
    ///setVariation(7);

    createFileInDirectory("JAVATEST", "/JAVATEST/AFILE");
    IFSFileOutputStream aFile = null;
    try
    {
       aFile = new IFSFileOutputStream(systemObject_, ifsFile_, share_, true);
       IFSFileDescriptor ahandle = aFile.getFD();
       ahandle.sync();

       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    try
    {
       aFile.close();
    } catch(Exception e) {}
    deleteIFSFile("/JAVATEST/AFILE");
  }

}
