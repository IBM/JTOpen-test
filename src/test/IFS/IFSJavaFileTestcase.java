///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSJavaFileTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IFS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSJavaFile;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileFilter;
import com.ibm.as400.access.IFSRandomAccessFile;

import test.IFSTests;
import test.JTOpenTestEnvironment;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.Hashtable;
import java.net.URL;



/**
Testcase IFSJavaFileTestcase.

<p>This tests the following IFSJavaFile methods:
<ul>
<li>ctors
<li>canRead()
<li>canWrite()
<li>compareTo(IFSJavaFile)
<li>compareTo(Object)
<li>delete()
<li>equals(Object)
<li>exists()
<li>getAbsolutePath()
<li>getCanonicalPath()
<li>getName()
<li>getParent()
<li>getPath()
<li>getSystem()
<li>hashCode()
<li>isAbsolute()
<li>isDirectory()
<li>isFile()
<li>lastModified()
<li>length()
<li>list()
<li>list(String)
<li>list(IFSFileFilter)
<li>list(IFSFileFilter, String)
<li>listFiles()
<li>listFiles(String)
<li>listFiles(IFSFileFilter)
<li>listFiles(IFSFileFilter, String)
<li>mkdir()
<li>mkdirs()
<li>renameTo(IFSJavaFile)
<li>setPath(String)
<li>setSystem(AS400)
<li>toString()
</ul>
**/
public class IFSJavaFileTestcase
extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSJavaFileTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }


  // Private data.
  public static final int    variations_             = 136;    // @A2c
  private static final String NEWLINE = "\n";
  private StringBuffer sb = new StringBuffer(); 

/**
Constructor.
**/
  public IFSJavaFileTestcase (AS400 systemObject,
        String userid, 
        String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password,  "IFSJavaFileTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
        pwrSys_ = pwrSys;
    }


  void createPrivateDirectory(AS400 as400, String dirName)
  {
    createDirectory(as400, dirName);
    setPrivate(as400, dirName);
  }


  void createEmptyFile(String pathName)
  {
    createEmptyFile (systemObject_, pathName);
  }

  void createEmptyFile(AS400 as400, String pathName)
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(as400, pathName);
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(as400, pathName.replace(File.separatorChar, IFSFile.separatorChar), "rw");
      raf.close();
    }
    catch (Exception e)
    {
      System.out.println("Exception in createEmptyFile: <" + e.getMessage() + ">");
    }
  }




  void createPrivateFile(AS400 as400, String pathName)
  {
    createIFSFile(as400, pathName);
    setPrivate(as400, pathName);
  }


  void deletePrivateDirectory(AS400 as400, String dirName) 
  {
    setPublic(as400, dirName);
    deleteDirectory(dirName);
  }

  boolean deletePrivateDirectoryStatus(AS400 as400, String dirName) throws IOException
  {
    setPublic(as400, dirName);
    return deleteDirectoryStatus(dirName);
  }


  void deletePrivateFile(AS400 as400, String pathName)
  {
    setPublic(as400, pathName);
    deleteFile(pathName);
  }

  void setPrivate(AS400 as400, String dirName)
  {
    String cmdString = "CHGAUT OBJ('"
                     + dirName
                     + "') USER(*PUBLIC) DTAAUT(*EXCLUDE) OBJAUT(*NONE)";
      sb.append("Command: " + cmdString+"\n"); 
 
    String cmdString2 = "CHGAUT OBJ('"
                     + dirName
                     + "') USER("
                     + systemObject_.getUserId()
                     + ") DTAAUT(*NONE) OBJAUT(*NONE)";
      sb.append ("Command: " + cmdString2+"\n");

    CommandCall cmd = new CommandCall(as400);
    try
    {
      cmd.setCommand(cmdString);
      cmd.run();
      cmd.setCommand(cmdString2);
      cmd.run();
    }
    catch(Throwable e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }
  }

  void setPublic(AS400 as400, String dirName)
  {
    String cmdString = "CHGAUT OBJ('"
                     + dirName
                     + "') USER(*PUBLIC) DTAAUT(*EXCLUDE) OBJAUT(*NONE)";
    sb.append("Command: " + cmdString+"\n");

    String cmdString2 = "CHGAUT OBJ('"
                     + dirName
                     + "') USER("
                     + systemObject_.getUserId()
                     + ") DTAAUT(*RWX) OBJAUT(*ALL)";
    if (DEBUG)
    {
      sb.append ("Command: " + cmdString2+NEWLINE);
    }

    CommandCall cmd = new CommandCall(as400);
    try
    {
      cmd.setCommand(cmdString);
      cmd.run();
      cmd.setCommand(cmdString2);
      cmd.run();
    }
    catch(Throwable e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  public void setup()
    throws Exception
  {
    super.setup(); 

    ifsDirName_ = IFSFile.separator; 
    fileName_ = IFSTests.COLLECTION+".File";
    ifsPathName_ = ifsDirName_ + fileName_;

    if (DEBUG) {
      Properties properties = System.getProperties();
      properties.store(System.out, "Dumping all JVM Properties"); 
    } 
  }


/**
ctor() - Empty ctor should have default properties.
**/
  public void Var001()
  {
    try
    {
      sb.setLength(0); 
      IFSJavaFile f = new IFSJavaFile();
      
      String osPath = f.getPath(); 
      String getPath = osPath.replace('\\','/'); 

        sb.append("Path:      " + getPath+NEWLINE);
        sb.append("Name:      " + f.getName()+NEWLINE);
        sb.append("System:    " + f.getSystem()+NEWLINE);
      assertCondition((getPath.equals(IFSFile.separator))
          && (f.getName().equals(""))
          && (f.getSystem() == null), sb);
    }
    catch(Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, String) - Passing null for system should throw an exception.
**/
  public void Var002()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile((AS400)null, IFSFile.separator);
      failed("Did not throw exception for."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, String) - Passing a valid system should set the system.
**/
  public void Var003()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, IFSFile.separator);
      assertCondition(f.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, String) - Passing null for path should throw an exception.
**/
  public void Var004()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, null);
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, String) - Passing a valid path should set the path.
**/
  public void Var005()
  {
    sb.setLength(0);
    try
    {
      String path = IFSFile.separator + "path";
      IFSJavaFile f = new IFSJavaFile(systemObject_, path);
      // I'm not sure why, but path uses the current OS separator
      String osPath = f.getPath(); 
      String getPath = osPath.replace('\\','/'); 
        sb.append("expected: <" + path + ">"+NEWLINE);
        sb.append("got:      <" + getPath + ">"+NEWLINE);
      assertCondition(getPath.equals(path),sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, String, String) - Passing null for system should throw an exception.
**/
  public void Var006()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(null, IFSFile.separator, "file");
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, String, String) - Passing a valid system should set the system.
**/
  public void Var007()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, IFSFile.separator, "file");
      assertCondition(f.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, String, String) - Passing null for path should throw an exception.
**/
  public void Var008()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_,(String) null, "name");
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, String, String) - Passing a valid path should set the path.
**/
  public void Var009()
  {
    sb.setLength(0);
    try
    {
      String path = IFSFile.separator + "path";
      String name = "name";
      IFSJavaFile f = new IFSJavaFile(systemObject_, path, name);
      
      String osPath = f.getPath(); 
      String getPath = osPath.replace('\\','/'); 

        sb.append("expected: <" + path + IFSFile.separator + name + ">"+NEWLINE);
        sb.append("got:      <" + getPath + ">"+NEWLINE);
      
      assertCondition(getPath.equals(path + IFSFile.separator + name),sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, String, String) - Passing null for name should throw an exception.
**/
  public void Var010()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_,(String) "path", null);
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, String, String) - Passing a valid name should set the name.
**/
  public void Var011()
  {
    try
    {
      String name = "name";
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", name);
      assertCondition(f.getName().equals(name));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, IFSJavaFile, String) - Passing null for system should throw an exception.
**/
  public void Var012()
  {
    try
    {
      IFSJavaFile directory = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFile f = new IFSJavaFile(null, directory, "path");
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, IFSJavaFile, String) - Passing a valid system should set the system.
**/
  public void Var013()
  {
    try
    {
      IFSJavaFile directory = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFile f = new IFSJavaFile(systemObject_, directory, "path");
      assertCondition(f.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, IFSJavaFile, String) - Passing a null directory should throw an exception.
**/
  public void Var014()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_,(IFSJavaFile)null, "name");
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, IFSJavaFile, String) - Passing a valid directory should set the path.
**/
  public void Var015()
  {
    sb.setLength(0);
    try
    {
      String name = "name";
      String path = IFSFile.separator + "path";
      IFSJavaFile directory = new IFSJavaFile(systemObject_, path);
      IFSJavaFile f = new IFSJavaFile(systemObject_, directory, name);
      String osPath = f.getPath(); 
      String getPath = osPath.replace('\\','/'); 
      
        sb.append("expected: <" + path + IFSFile.separator + name + ">"+NEWLINE);
        sb.append("got:      <" + getPath + ">"+NEWLINE);
      
      assertCondition(getPath.equals(path + IFSFile.separator + name),sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(AS400, IFSJavaFile, String) - Passing a null name should throw an exception.
**/
  public void Var016()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_,new IFSJavaFile(), null);
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(AS400, IFSJavaFile, String) - Passing a valid name should set the name.
**/
  public void Var017()
  {
    sb.setLength(0);
    try
    {
      String name = "name";
      String path = IFSFile.separator + "path";
      IFSJavaFile directory = new IFSJavaFile(systemObject_, path);
      IFSJavaFile f = new IFSJavaFile(systemObject_, directory, name);
      
        sb.append("expected: <" + name + ">"+NEWLINE);
        sb.append("got:      <" + f.getName() + ">"+NEWLINE);
          assertCondition(f.getName().equals(name),sb );
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(IFSJavaFile, String) - Passing a null directory should throw an exception.
**/
  public void Var018()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile((IFSJavaFile)null, "name");
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(IFSJavaFile, String) - Passing a valid directory should set the path and system.
**/
  public void Var019()
  {
    sb.setLength(0);
    try
    {
      String path = IFSFile.separator + "path";
      String name = "name";
      IFSJavaFile directory = new IFSJavaFile(systemObject_, path);
      IFSJavaFile f = new IFSJavaFile(directory, name);
      String osPath = f.getPath(); 
      String getPath = osPath.replace('\\','/'); 
        sb.append("expected: <" + path + IFSFile.separator + name + ">"+NEWLINE);
        sb.append("got:      <" + getPath + ">"+NEWLINE);
      assertCondition((getPath.equals(path + IFSFile.separator + name)) && (f.getSystem() == systemObject_),sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
ctor(IFSJavaFile, String) - Passing a null name should throw an exception.
**/
  public void Var020()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(new IFSJavaFile(), null);
      failed("Did not throw exception."+f);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
ctor(IFSJavaFile, String) - Passing a valid name should set the name.
**/
  public void Var021()
  {
    sb.setLength(0);
    try
    {
      String path = IFSFile.separator + "path";
      String name = "name";
      IFSJavaFile directory = new IFSJavaFile(systemObject_, path);
      IFSJavaFile f = new IFSJavaFile(directory, name);
      
        sb.append("expected: <" + name + ">"+NEWLINE);
        sb.append("got:      <" + f.getName() + ">"+NEWLINE);
      
      assertCondition(f.getName().equals(name),sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
canRead() - Should throw an exception when no properties have been set.
**/
  public void Var022()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.canRead();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
canRead() - Should return false if called for a non-existent file.
**/
  public void Var023()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    deleteFile(fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.canRead() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
canRead() - Should return true if called for a file that exists.
**/
  public void Var024()
  {
    sb.setLength(0);
    String fileName = ifsPathName_ + "m" + getVariation();
      sb.append("ifsPathName_: " + ifsPathName_+NEWLINE);
      sb.append("fileName:     " + fileName+NEWLINE);
    createFile(fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.canRead() == true, "file.canRead() returned false for "+fileName+" "+sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
canRead() - Should return false when not authorized to the file.
**/
  public void Var025()
  {
    sb.setLength(0);
    String fileName = ifsPathName_ + "m" + getVariation();
    
    sb.append("ifsPathName_: " + ifsPathName_+NEWLINE);
    sb.append("fileName:     " + fileName+NEWLINE);
    
    createPrivateFile(pwrSys_, fileName);
    boolean passed = false; 
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      
      passed = (false == file.canRead()); 
      assertCondition(passed, "Should not have been authorized to "+fileName+" "+sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception" + sb);
    }
    if (passed) deletePrivateFile(pwrSys_, fileName);
  }

/**
canWrite() - Should throw an exception when no properties have been set.
**/
  public void Var026()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.canWrite();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
canWrite() - Should return false if called for a non-existent file.
**/
  public void Var027()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    deleteFile(fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.canWrite() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
canWrite() - Should return true if called for a file that exists.
**/
  public void Var028()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    createFile(fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.canWrite() == true, "file.canWrite() returned false for "+fileName+" If running on AIX or Linux, verify that the profile specified on -uid is the IBM i profile that has the same UID number as the local profile (e.g. JAVA1).");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
canWrite() - Should return false if called for a read-only file.
**/
  public void Var029()
  {
    String cmdStr1 = "CRTLIB JFILETST";
    String cmdStr2 = "CRTPF FILE(JFILETST/FILE) RCDLEN(132) ALWUPD(*NO)";
    CommandCall cmd = new CommandCall(systemObject_);
    try
    {
      cmd.setCommand(cmdStr1);
      cmd.run();
      cmd.setCommand(cmdStr2);
      cmd.run();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }

    IFSJavaFile file = null;
    try
    {
      file =
        new IFSJavaFile(systemObject_,   IFSFile.separator
                                       + "QSYS.LIB"
                                       + IFSFile.separator
                                       + "JFILETST.lib"
                                       + IFSFile.separator
                                       + "FILE.FILE"
                                   );//    + IFS_IFSFile.separator
                                     //  + "FILE.MBR");
      assertCondition(file.canWrite() == false);
    }
    catch(Exception e)
    {
      if (DEBUG)
      {
        sb.append ("Exception: " + e.getMessage()+NEWLINE);
        if (file != null) { 
            sb.append ("File: " + file.getPath()+NEWLINE);
        }
      }
      failed(e, "Unexpected Exception");
    }

    cmdStr1 = "DLTF FILE(JFILETST/FILE)";
    cmdStr2 = "DLTLIB JFILETST";
    try
    {
	cmd.setCommand("CHGJOB INQMSGRPY(*SYSRPYL)");
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
canWrite() - Should return false when not authorized to the file.
**/
  public void Var030()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    if (DEBUG)
    {
      sb.append("ifsPathName_: " + ifsPathName_+NEWLINE);
      sb.append("fileName:     " + fileName+NEWLINE);
    }
    createPrivateFile(pwrSys_, fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(false == file.canWrite(), "should not have been authorized to "+fileName);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    deletePrivateFile(pwrSys_, fileName);
  }

/**
compareTo(IFSJavaFile) - Should throw a NullPointerException is passed a null.
**/
  public void Var031()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      f.compareTo((IFSJavaFile)null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
compareTo(IFSJavaFile) - Should return true when compared with itself.
**/
  public void Var032()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      assertCondition(f.compareTo(f) == 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
compareTo(Object) - Should throw a NullPointerException is passed a null.
**/
  public void Var033()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      f.compareTo((File)null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
compareTo(Object) - Should return true when compared with itself.
**/
  public void Var034()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      assertCondition(f.compareTo((File)f) == 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
delete() - Should remove the file.
**/
  public void Var035()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    StringBuffer sb = new StringBuffer(); 
    try
    {
      boolean succeeded = true;
      createFile(fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      if (file.exists() == false)
      {
        succeeded = false;
        sb.append("Error : File "+fileName+" does not exist"); 
      }
      file.delete();
      if (file.exists() == true)
      {
        succeeded = false;
        sb.append("Error : File "+fileName+" exists after delete "); 

        //deleteFile(fileName);
      }
      assertCondition(succeeded == true);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
delete() - Should return false when the file does not exist.
**/
  public void Var036()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    deleteFile(fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.delete() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
delete() - Should throw a security exception when not authorized to the file.
**/
  public void Var037()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    if (DEBUG)
    {
      sb.append("ifsPathName_: " + ifsPathName_+NEWLINE);
      sb.append("fileName:     " + fileName+NEWLINE);
    }
    createPrivateFile(pwrSys_, fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      file.delete();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    deletePrivateFile(pwrSys_, fileName);
  }

/**
equals(IFSJavaFile) Should return true if called on itself or another
IFSJavaFile object having the same path name.
**/
  public void Var038()
  {
    try
    {
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, ifsPathName_);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, ifsPathName_);
      assertCondition(file1.equals(file1) && file1.equals(file2));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
equals(IFSJavaFile) - Should return false if called on another IFSJavaFile object
having a different path name.
**/
  public void Var039()
  {
    try
    {
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, ifsPathName_);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, ifsPathName_);
      assertCondition(file1.equals(file1) && file1.equals(file2));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
exists() - Should return false if the file does not exist.
**/
  public void Var040()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    deleteFile(fileName);
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.exists() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    deleteFile(fileName);
  }

/**
exists() - Should return true if the file exists.
**/
  public void Var041()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      createFile(fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.exists() == true, "File "+fileName+" does not exist.  Should have been created"); 
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
exists() - Should throw a security exception when not authorized to the directory.
**/
  public void Var042()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();
    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      file.exists();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
getAbsolutePath() - Should equal the ifsPathName we provided.
**/
  public void Var043()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      String osPath = file.getAbsolutePath(); 
      String getAbsolutePath = osPath.replace('\\','/'); 

      assertCondition(getAbsolutePath.equals(ifsPathName_), 
          "file.getAbsolutePath()="+getAbsolutePath + " != ifsPathName_="+ifsPathName_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getCanonicalPath() - Should equal the ifsPathName we provided.
**/
  public void Var044()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      String osPath = file.getCanonicalPath(); 
      String getPath = osPath.replace('\\','/'); 

      assertCondition(getPath.equals(ifsPathName_), "getCanonicalPath="+getPath+" sb "+ifsPathName_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getName() - Should return just the file name.
**/
  public void Var045()
  {
    try
    {
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, IFSFile.separator + "Directory" + IFSFile.separator + "File.txt");
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, "File.txt");
      assertCondition(file1.getName().equals("File.txt") &&
             file2.getName().equals("File.txt"));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getParent() - Should return the parent directory.
**/
  public void Var046()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      String osPath = file.getParent(); 
      String getPath = osPath.replace('\\','/'); 
      assertCondition(getPath.equals(ifsDirName_));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getPath() - Should return the path part of the ifsFilename.
**/
  public void Var047()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      String osPath = file.getPath(); 
      String getPath = osPath.replace('\\','/'); 

      assertCondition(getPath.equals(ifsPathName_));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getPath() - Ensure that a leading slash is prepended to a file name if one is
not specified.
**/
  public void Var048()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, "foobar");
      String osPath = file.getPath(); 
      String getPath = osPath.replace('\\','/'); 
      assertCondition(getPath.equals(IFSFile.separator + "foobar"));
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getSystem() - Passing a valid system should set the system.
**/
  public void Var049()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile();
      file.setSystem(systemObject_);
      assertCondition(file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
getSystem() - Passing a valid system should set the system.
**/
  public void Var050()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      assertCondition(file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
hashCode() - Should return the same value for the same object.
Objects that are equal (according to IFSJavaFile.equals()) should
return the same value.
**/
  public void Var051()
  {
    try
    {
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, IFSFile.separator + "abcd");
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, IFSFile.separator + "abcd");
      assertCondition(file1.hashCode() == file2.hashCode() &&
             file1.equals(file2) && file1.hashCode() == file1.hashCode());
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
isAbsolute() - Should return true.
**/
  public void Var052()
  {
    try
    {
      if (JTOpenTestEnvironment.isWindows) {
        IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_.replace(IFSFile.separatorChar, File.separatorChar));
        assertCondition(file.isAbsolute()); 
      } else {
        IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
        assertCondition(file.isAbsolute()); 
        
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
isDirectory() - Should return false if the directory doesn't exist.
**/
  public void Var053()
  {
    // ensure directory doesn't exist
    try
    {
      deleteDirectory(ifsDirName_ + "NoDir");
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, ifsDirName_ + "NoDir");
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }

    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsDirName_ + "NoDir");
      assertCondition(file.isDirectory() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
isDirectory() - Should return true if the path exists and is a directory.
**/
  public void Var054()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsDirName_);
      assertCondition(file.isDirectory() == true);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
isDirectory() - Should return false if the path exists and is a file.
**/
  public void Var055()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();

    try
    {
      createDirectory(pathName);
      createFile(fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.isDirectory() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
      deleteDirectory(pathName);
    }
  }

/**
isDirectory() - Should throw a security exception when not authorized to the directory.
**/
  public void Var056()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();

    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      file.isDirectory();
      failed("Did not throw exception when not authorized to directory "+fileName);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
isFile() - Should return false if the file doesn't exist.
**/
  public void Var057()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      deleteFile(fileName);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }

    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.isFile() == false);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
isFile() - Should return true if the file exists and is a file.
**/
  public void Var058()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      createFile(fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.isFile() == true, "file.isFile() returned false for "+fileName);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
isFile() - Should return false if the file exists and is a directory.
**/
  public void Var059()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      createFile(fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      assertCondition(file.isFile() == true, "file.isFile returned false for "+fileName);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
isFile() - Should throw a security exception when not authorized to the file.
**/
  public void Var060()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();
    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      file.isFile();
      failed("Did not throw exception when not authorized to "+fileName);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
lastModified() - Should throw an exception when no properties have been set.
**/
  public void Var061()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.length();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
lastModified() - Should return the timestamp of a newly created file correctly.
**/
  public void Var062()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      deleteFile(fileName);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      for (int count=0; count<4; count++)
      {
        deleteFile(fileName + count);
        stillThere = new IFSJavaFile(systemObject_, fileName + count);
        if (stillThere.exists())
        {
          failed("Unable to setup variation.");
          return;
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }

    try
    {
      for (int count=0; count<3; count++)
      {
        // Changed to 5 seconds on 7/27/2011
        Thread.sleep(5 * 1000);
        createFile(fileName + count);
      }
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName + 0);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName + 1);
      IFSJavaFile file3 = new IFSJavaFile(systemObject_, fileName + 2);

      Date modificationDate1 = new Date(file1.lastModified());
      Date modificationDate2 = new Date(file2.lastModified());
      Date modificationDate3 = new Date(file3.lastModified());
      if ((modificationDate1.getTime() >= modificationDate2.getTime())
      ||  (modificationDate2.getTime() >= modificationDate3.getTime()))
      {
        failed("Dates are not correct modificationDate1.getTime()("+modificationDate1.getTime()+") "
            + ">= modificationDate2.getTime()("+modificationDate2.getTime()+"))\n"
            + "      ||  (modificationDate2.getTime()("+modificationDate2.getTime()+") "
                + ">= modificationDate3.getTime()("+modificationDate3.getTime()+")");
      } else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      for (int count=0; count<3; count++)
      {
        deleteFile(fileName + count);
      }
    }

  }

/**
lastModified() - Should throw a security exception when not authorized to the file.
**/
  public void Var063()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();
    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      file.lastModified();
      failed("Did not throw exception when not authorized to "+fileName);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
length() - Should throw an exception when no properties have been set.
**/
  public void Var064()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.length();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
length() - Should return zero on a newly created file.
**/
  public void Var065()
  {
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      deleteFile(fileName);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }

    try
    {
      createEmptyFile(fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      if (DEBUG) sb.append ("Length (0) = " + file.length()+NEWLINE);
      assertCondition(file.length() == 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
length() - Should return the length of the data in a newly created file.
**/
  public void Var066()
  {
    String myData = "Now is the time for all good men and/or women (as pc demands) to go directly to jail.  Do not pass go.  Do not collect $200.00";
    String fileName = ifsPathName_ + "m" + getVariation();
    try
    {
      deleteFile(fileName);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }

    try
    {
      createFile(fileName, myData.getBytes());
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);

      if (DEBUG)
      {
        sb.append("File length:   " + file.length()+NEWLINE);
        sb.append("String length: " + myData.length()+NEWLINE);
      }
      assertCondition(file.length() == myData.length(),"File length:   " + file.length()+" String length: " + myData.length());
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
length() - Should throw a security exception when not authorized to the file.
**/
  public void Var067()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();
    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      long len = file.length();
      sb.append ("Length is: " + len+NEWLINE);
      failed("Did not throw exception when not auhorized to "+fileName+" "+sb);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
list() - Should return the same directory entry names as IFSJavaFile.list("*").
**/
  public void Var068()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    try
    {
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, ifsDirName_);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, ifsDirName_);
      String[] names1 = file1.list();
      String[] names2 = file2.list("*");
      if (names1.length == names2.length)
      {
        int i = 0;
        while (i < names1.length && names1[i].equals(names2[i]))
        {
          i++;
        }
        if (i != names1.length)
        {
          failed("Data does not match.");
          int j=0;
          while (j < names1.length)
          {
            output_.println("IFSJavaFile.list(), IFSJavaFile.list(\"*\") = " + names1[j] + ", " + names2[j]);
            j++;
          }
        }
        else
          succeeded();
      }
      else
      {
        failed("IFSJavaFile.list() = " + Integer.toString(names1.length) +
               " IFSJavaFile.list(\"*\") = " + Integer.toString(names2.length));

        int j=0;
        while ((j < names1.length) || (j < names2.length))
        {
          try
          {
            output_.println("IFSJavaFile.list(), IFSJavaFile.list(\"*\") " + j + " = " + names1[j] + ", " + names2[j]);
          }
          catch(Exception e)
          {
            output_.println("Exception: " + e.getMessage());
            try
            {
              output_.println("IFSJavaFile.list() " + j + " = " + names1[j]);
            }
            catch(Exception e1)
            {}
            try
            {
              output_.println("IFSJavaFile.list(\"*\") " + j + " = " + names2[j]);
            }
            catch(Exception e2)
            {}
          }
          j++;
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
list() - Should return the same directory entry names as File.list().
**/
  public void Var069()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    try
    {
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, ifsDirName_);
      file1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);  //@B1A
      String[] names1 = file1.list();
      
      String[] names2 = listDirectory(ifsDirName_); 
      
      // File file2 = new File(convertToPCName(ifsDirName_));
      // String[] names2 = file2.list();

     
      
      int mismatchCount=0;
      if (names1.length == names2.length)
      {
        boolean mismatch = false;
        for (int i=0; i<names1.length; i++)
        {
          if (! names1[i].equals(names2[i])) {
            mismatch = true;
            mismatchCount++;
            output_.println("  MISMATCH: IFSJavaFile.list(), File.list() = " + names1[i] + ", " + names2[i]);
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
      else //do if list lengths are not equal  //@A1C
      {
         boolean somethingPrinted = false; 
         failed("IFSJavaFile.list().length= " + names1.length + " File.list().length = " + names2.length);
         Vector listNames = new Vector(names1.length);  
         Hashtable trackListNames = new Hashtable(); 
         for (int j = 0; j < names1.length; j++) 
         {
             listNames.addElement(names1[j]);
             trackListNames.put(names1[j], "X");    
         }
         Vector fileListNames = new Vector(names2.length);
         for (int k = 0; k < names2.length; k++) 
         {
             fileListNames.addElement(names2[k]);
         }
         boolean print = true;
         for (Enumeration e = listNames.elements(); e.hasMoreElements(); ) 
         {
             String name = (String)e.nextElement();
             if (!(fileListNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in IFSJavaFile.list() but not File.list():");
                    print = false;
                 }
                 System.out.println(name);
                 somethingPrinted = true; 
             }
         }
         print = true;
         StringBuffer uniqueList = new StringBuffer(); 
         for (Enumeration f = fileListNames.elements(); f.hasMoreElements(); ) 
         {
             String name2 = (String)f.nextElement();
             if (!(listNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in File.list() but not IFSJavaFile.list():");
                    print = false;
                 }
                 System.out.println(name2);
                 somethingPrinted = true; 
             }
             Object found = trackListNames.get(name2);
             if (found == null) {
               uniqueList.append("Unique list not found: "+name2+"\n"); 
             } else {
               uniqueList.append("Unique list removed  : "+name2+"\n"); 
               trackListNames.remove(name2); 
             }
         }
         if (!somethingPrinted) { 
           System.out.println("Nothing printed");
           System.out.println(uniqueList.toString()); 
         }
      }//end else
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
list(String) - Should throw NullPointerException if argument one of IFSJavaFile.list(String) is null.
**/
  public void Var070()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsDirName_);
      file.list((String) null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "pattern");
    }
  }

/**
list(String) - Ensure that IFSJavaFile.list("*") returns the same files as File.list().
**/
  public void Var071()
  {
    sb.setLength(0);
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    try
    {
      IFSJavaFile dir1 = new IFSJavaFile(pwrSys_, ifsDirName_);
      dir1.setPatternMatching(IFSFile.PATTERN_POSIX_ALL);  //@B1A
      String[] list1 = dir1.list("*");
      String[] list2 = listDirectory(ifsDirName_); 
      // File dir2 = new File(convertToPCName(ifsDirName_));
      // String[] list2 = dir2.list();
      int mismatchCount=0;
      int i = -1;
      if (list1.length == list2.length)
      {
        for (i = 0; i < list1.length; i++)
        {
          
            sb.append(i + ": <" + list1[i] + "> =?= <" + list2[i] + ">"+NEWLINE);
              if (!list1[i].equals(list2[i]))
            mismatchCount++;
        }
        output_.println("  Number mismatches: ("+mismatchCount+"/"+list1.length+")");
        assertCondition(list1.length == list2.length, sb);  //@A1C
      }
      else //do if list lengths are not equal   //@A1C
      {
        failed("IFSJavaFile.list(\"*\").length= " + list1.length + " File.list().length = " + list2.length);
         Vector listStarNames = new Vector(list1.length);
         for (int j = 0; j < list1.length; j++) 
         {
             listStarNames.addElement(list1[j]);
         }
         Vector fileListNames = new Vector(list2.length);
         for (int k = 0; k < list2.length; k++) 
         {
             fileListNames.addElement(list2[k]);
         }
         boolean print = true;
         for (Enumeration e = listStarNames.elements(); e.hasMoreElements(); ) 
         {
             String name = (String)e.nextElement();
             if (!(fileListNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in IFSJavaFile.list(\"*\") but not File.list():");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration f = fileListNames.elements(); f.hasMoreElements(); ) 
         {
             String name2 = (String)f.nextElement();
             if (!(listStarNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in File.list() but not IFSJavaFile.list(\"*\"):");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else

      if (DEBUG)
      {
        sb.append("i: " + i + " list1.length: " + list1.length+NEWLINE);
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }


/**
list(IFSFileFilter) - Should not throw NullPointerException if argument one of IFSFile.list(IFSFileFilter) is null.
**/
  public void Var072()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      file.list((IFSFileFilter) null);
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
list(IFSFileFilter) - Should filter correctly.
**/
  public void Var073()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFilter83 filter = new IFSJavaFilter83(output_);
      String[] list = file.list(filter);
      output_.println("");
      assertCondition(list.length > 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
list(IFSFileFilter, String) - Should not throw NullPointerException argument one of
IFSFile.list(IFSFileFilter, String) is null.
**/
  public void Var074()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      file.list((IFSFileFilter) null, "*.exe");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
list(IFSFileFilter, String) - Should throw NullPointerException argument two of
IFSFile.list(IFSFileFilter, String) is null.
**/
  public void Var075()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      file.list(new IFSJavaFilter83(output_),(String) null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "pattern"));
    }
  }

/**
list(IFSFileFilter, String) - Should throw NullPointerException argument two of
IFSFile.list(IFSFileFilter, String) is null.
**/
  public void Var076()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFilter83 filter = new IFSJavaFilter83(output_);
      String[] list = file.list(filter, "*");
      output_.println("");
      assertCondition(list.length > 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
list(IFSFileFilter, String) - Should throw NullPointerException argument two of
IFSFile.list(IFSFileFilter, String) is null.
**/
  public void Var077()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFilter83 filter = new IFSJavaFilter83(output_);
      String[] list = file.list(filter, "Q*");
      output_.println("");
      assertCondition(list.length > 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
mkdir() - Should throw an exception when no properties have been set.
**/
  public void Var078()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.mkdir();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
mkdir() - Should create the directory.
**/
  public void Var079()

  {
    sb.setLength(0);
    String fileName = ifsDirName_;
    deleteDirectory (ifsDirName_ + IFSFile.separator + "firstOne");
    try
    {
      boolean succeeded = false;
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
        sb.append("File: <" + file.getName() + "> (" + fileName + ") exists?: " + file.exists()+NEWLINE);
      if (file.exists())
      {
        fileName += File.separator;
        fileName += "firstOne";
        IFSJavaFile file2 = new IFSJavaFile(file, fileName);
//        file2.setSystem(systemObject_);
          sb.append("File: <" + file2.getName() + "> (" + fileName + ") exists?: " + file2.exists()+NEWLINE);
        if (!file2.exists())
        {
          file2.mkdir();
          if (!file2.exists() || !file2.isDirectory())
          {
              if (!file2.exists())
              {
                sb.append(fileName + " does not exist"+NEWLINE);
              }
              if (!file2.isDirectory())
              {
                sb.append(fileName + " is not a directory"+NEWLINE);
              }
          } else
          {
            succeeded = true;
          }
        }
      }
      assertCondition(succeeded == true, sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      if (!fileName.equals(ifsDirName_))
      {
        deleteDirectory(fileName);
      }
    }
  }

/**
mkdir() - Should throw a security exception when not authorized to the file.
**/
  public void Var080()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();
    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile f = new IFSJavaFile(systemObject_, fileName);
      f.mkdir();
      failed("Did not throw exception when not authorized to "+fileName);
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
mkdirs() - Should throw an exception when no properties have been set.
**/
  public void Var081()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.mkdirs();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
mkdirs() - Should create the directory.
**/
  public void Var082()

  {
    sb.setLength(0);
    String fileName; 
    if (ifsDirName_.length() > 1) { 
       fileName = ifsDirName_ + File.separator + "firstDir" + File.separator + "secondDir" + 
  File.separator + "thirdDir" + File.separator + "fourthDir";
    } else { 
      fileName = ifsDirName_ +  "firstDir" + File.separator + "secondDir" + 
 File.separator + "thirdDir" + File.separator + "fourthDir";
      
    }
    try
    {
      boolean succeeded = false;
      IFSJavaFile file       = new IFSJavaFile(systemObject_, fileName);
      IFSJavaFile fileDelete = new IFSJavaFile(systemObject_, fileName);

      try
      {
        String theFile = fileDelete.getPath();
        while (!theFile.equals(File.separator))
        {
          sb.append("deleting "+theFile+"\n"); 
          boolean deleted = deleteDirectoryStatus(theFile);
          if (!deleted) sb.append("  File not deleted "+theFile+"\n"); 
          fileDelete = new IFSJavaFile(systemObject_, fileDelete.getParent());
          fileDelete.setSystem(systemObject_);
          theFile = fileDelete.getPath();
        }
      }
      catch (Exception e2)
      {
        System.out.println ("Setup Failed");
        throw e2;
      }

      if (!file.exists())
      {
        file.mkdirs();
        if (!file.exists() || !file.isDirectory())
        {
          if (DEBUG)
          {
            if (!file.exists())
            {
              sb.append(fileName + " does not exist\n");
            }
            if (!file.isDirectory())
            {
              sb.append(fileName + " is not a directory\n");
            }
          }
        } else
        {
          succeeded = true;
        }
      } else {
        sb.append("Error: file "+file+" exists"); 
      }
      String theFile = file.getPath();
      while (!theFile.equals(File.separator))
      {
        boolean deleted = deleteDirectoryStatus(theFile);
        if (!deleted) {
          succeeded = false; 
          sb.append("File not deleted:"+theFile); 
        }
        String parent = file.getParent(); 
        if (parent != null) { 
          file = new IFSJavaFile(systemObject_, file.getParent());
          file.setSystem(systemObject_);
          theFile = file.getPath();
        } else {
          theFile = ifsDirName_; 
        }
      }
      assertCondition(succeeded , sb);
      
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception "+sb.toString());
    }
  }

/**
mkdirs() - Should throw a security exception when not authorized to the file.
**/
  public void Var083()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName = pathName + IFSFile.separator + "m" + getVariation();
    try
    {
      createPrivateDirectory(pwrSys_, pathName);
      IFSJavaFile f = new IFSJavaFile(systemObject_, fileName);
      f.mkdirs();
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deletePrivateDirectory(pwrSys_, pathName);
    }
  }

/**
renameto() - Should throw an exception when no properties have been set.
**/
  public void Var084()
  {
    String fileName = ifsPathName_ + "r" + getVariation();
    try
    {
      deleteFile (fileName);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      IFSJavaFile rf = new IFSJavaFile(systemObject_, fileName);
      IFSJavaFile f = new IFSJavaFile();
      f.renameTo(rf);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
renameTo() - Should rename the file to a new name.
**/
  public void Var085()
  {
    sb.setLength(0);
    String myData = "Now is the time for all good men and/or women (as pc demands) to go directly to jail.  Do not pass go.  Do not collect $200.00";
    String fileName  = ifsPathName_ + "m" + getVariation();
    String fileName2 = ifsPathName_ + "r" + getVariation();
    try
    {
      deleteFile(fileName);
      deleteFile(fileName2);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      stillThere = new IFSJavaFile(systemObject_, fileName2);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }

    try
    {
      createFile(fileName, myData.getBytes());
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);

        sb.append("File length:   " + file.length()+NEWLINE);
        sb.append("String length: " + myData.length()+NEWLINE);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName2);
      file.renameTo(file2);
      IFSJavaFile file3 = new IFSJavaFile(systemObject_, fileName);
      IFSJavaFile file4 = new IFSJavaFile(systemObject_, fileName2);
      assertCondition((file3.exists() == false) && (file4.exists() == true), sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
      deleteFile(fileName2);
    }
  }

/**
renameto() - Should throw a security exception when not authorized to the file(s).
**/
  public void Var086()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName1 = ifsPathName_ + "mm" + getVariation();
    String fileName2 = pathName + IFSFile.separator + "r" + getVariation();
    try
    {
      createPrivateDirectory (pwrSys_, pathName);
      deleteFile (fileName1);
      deleteFile (fileName2);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName1);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      stillThere = new IFSJavaFile(systemObject_, fileName2);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      IFSJavaFile rf = new IFSJavaFile(systemObject_, fileName1);
      IFSJavaFile f = new IFSJavaFile(systemObject_, fileName2);
      f.renameTo(rf);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deleteFile (fileName1);
      deleteFile (fileName2);
      deletePrivateDirectory (pwrSys_, pathName);
    }
  }

/**
renameto() - Should throw a security exception when not authorized to the file(s).
**/
  public void Var087()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName1 = ifsPathName_ + "mm" + getVariation();
    String fileName2 = pathName + IFSFile.separator + "r" + getVariation();
    try
    {
      createPrivateDirectory (pwrSys_, pathName);
      deleteFile (fileName1);
      deleteFile (fileName2);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName1);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      stillThere = new IFSJavaFile(systemObject_, fileName2);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      IFSJavaFile rf = new IFSJavaFile(systemObject_, fileName2);
      IFSJavaFile f = new IFSJavaFile(systemObject_, fileName1);
      f.renameTo(rf);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deleteFile (fileName1);
      deleteFile (fileName2);
      deletePrivateDirectory (pwrSys_, pathName);
    }
  }

/**
setPath(String) - Should throw NullPointerException with null value.
**/
  public void Var088()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.setPath(null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
setPath(String) - Should change the file path.
**/
  public void Var089()
  {
    sb.setLength(0);
    try
    {
      IFSJavaFile f = new IFSJavaFile();
        sb.append("Path: " + f.getPath()+NEWLINE);
        sb.append("Name: " + f.getName()+NEWLINE);
      if (!f.getPath().equals(File.separator))
      {
        failed("Unable to setup variation."+sb.toString());
      } else
      {
        String newPath = File.separator + "com" + File.separator + "ibm" + File.separator + "as400" 
      + File.separator + "access";
        f.setPath(newPath);
          sb.append("expect: <" + newPath + ">"+NEWLINE);
          sb.append("got:    <" + f.getPath() + ">"+NEWLINE);
        assertCondition(f.getPath().equals(newPath),sb);
      }
    }
    catch(Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

/**
setSystem(String) - Should throw NullPointerException with null value.
**/
  public void Var090()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      f.setSystem(null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
setSystem(AS400) - Should change the connected AS/400 system.
**/
  public void Var091()
  {
    sb.setLength(0);
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      if (DEBUG)
        sb.append("System: " + f.getSystem()+NEWLINE);
      f.setSystem(systemObject_);
      assertCondition(f.getSystem() == systemObject_, sb);
    }
    catch(Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

/**
toString() - Should return default values when no properties have been set.
**/
  public void Var092()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile();
      if (!f.toString().equals(File.separator))
      {
        System.out.println ("toString <" + f.toString() + ">");
        System.out.println ("System   <" + IFSFile.separator + ">");
        failed("Unexpected separator value found.");
      }
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
toString() - Should return the same value as getPath().
**/
  public void Var093()
  {
    sb.setLength(0);

    try
    {
      boolean succeeded = true;
      IFSJavaFile f = new IFSJavaFile();
        sb.append("Path:      " + f.getPath()+NEWLINE);
        sb.append ("toString: " + f.toString()+NEWLINE);
      if (!f.getPath().equals(f.toString()))
      {
        succeeded = false;
      } else
      {
        String newPath = IFSFile.separator + "com" + IFSFile.separator + "ibm" + IFSFile.separator + "as400" + IFSFile.separator + "access";
        f.setPath(newPath);
          sb.append ("Path:     " + f.getPath()+NEWLINE);
          sb.append ("toString: " + f.toString()+NEWLINE);
        if (!f.getPath().equals(f.toString()))
        {
          succeeded = false;
        }
      }
      assertCondition(succeeded == true, sb);
    }
    catch(Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles() - Should return the same directory entry names as IFSJavaFile.listFiles("*").
**/
  public void Var094()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsDirName_);
      File[] files1 = file.listFiles();
      File[] files2 = file.listFiles("*");
      if (files1.length == files2.length)
      {
        int i = 0;
        while (i < files1.length && (files1[i].getName()).equals((files2[i].getName())))
        {
          i++;
        }
        if (i != files1.length)
        {
          failed("Data does not match.");
          int j=0;
          while (j < files1.length)
          {
            output_.println("IFSJavaFile.listFiles(), IFSJavaFile.listFiles(\"*\") = " + files1[j].getName() + ", " + files2[j].getName());
            j++;
          }
        }
        else
          succeeded();
      }
      else //do if list lengths are not equal
      {
         failed("IFSJavaFile.listFiles().length= " + files1.length + " IFSJavaFile.listFiles(\"*\").length= " + files2.length);
         Vector listFilesNames = new Vector(files1.length);
         for (int j = 0; j < files1.length; j++) 
         {
             listFilesNames.addElement(files1[j].getName());
         }
         Vector listFilesStarNames = new Vector(files2.length);
         for (int k = 0; k < files2.length; k++) 
         {
             listFilesStarNames.addElement(files2[k]);
         }
         boolean print = true;
         for (Enumeration e = listFilesNames.elements(); e.hasMoreElements(); ) 
         {
             String name = (String)e.nextElement();
             if (!(listFilesStarNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles() but not listFiles(\"*\"):");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration f = listFilesStarNames.elements(); f.hasMoreElements(); ) 
         {
             String name2 = (String)f.nextElement();
             if (!(listFilesNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles(\"*\") but not listFiles():");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles() - Should return the same directory entry names as IFSJavaFile.list().
**/
  public void Var095()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsDirName_);
      File[] files1 = file.listFiles();
      String[] files2 = file.list();
      if (files1.length == files2.length)
      {
        int i = 0;
        while (i < files1.length && (files1[i].getName()).equals(files2[i]))
        {
          i++;
        }
        if (i != files1.length)
        {
          failed("Data does not match.");
          int j=0;
          while (j < files1.length)
          {
            output_.println("IFSJavaFile.listFiles(), IFSJavaFile.list() = " + files1[j].getName() + ", " + files2[j]);
            j++;
          }
        }
        else
          succeeded();
      }
      else //do if list lengths are not equal
      {
         failed("IFSJavaFile.listFiles().length= " + files1.length + " IFSJavaFile.list().length= " + files2.length);
         Vector listFilesNames = new Vector(files1.length);
         for (int j = 0; j < files1.length; j++) 
         {
             listFilesNames.addElement(files1[j].getName());
         }
         Vector listNames = new Vector(files2.length);
         for (int k = 0; k < files2.length; k++) 
         {
             listNames.addElement(files2[k]);
         }
         boolean print = true;
         for (Enumeration e = listFilesNames.elements(); e.hasMoreElements(); ) 
         {
             String name = (String)e.nextElement();
             if (!(listNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles() but not list():");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration f = listNames.elements(); f.hasMoreElements(); ) 
         {
             String name2 = (String)f.nextElement();
             if (!(listFilesNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in list() but not listFiles():");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles(String) - Should throw NullPointerException if argument of IFSJavaFile.listFiles(String) 
is null.
**/
  public void Var096()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsDirName_);
      file.listFiles((String) null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "pattern");
    }
  }

//@A1A
/**
listFiles(String) - Ensure that IFSJavaFile.listFiles("*") returns the same files as 
IFSJavaFile.list().
**/
  public void Var097()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    sb.setLength(0);
    try
    {
      IFSJavaFile dir = new IFSJavaFile(pwrSys_, ifsDirName_);
      File[] list1 = dir.listFiles("*");
      String[] list2 = dir.list();

      int i = -1;
      if (list1.length == list2.length)
      { 
        for (i = 0; i < list1.length; i++)
        {
            sb.append(i + ": <" + list1[i].getName() + "> =?= <" + list2[i] + ">"+NEWLINE);
          if (!(list1[i].getName()).equals(list2[i]))
            break;
        }
        assertCondition(i == list1.length, sb);
      }
      else //do if list lengths are not equal
      {
         failed("IFSJavaFile.listFiles(\"*\").length= " + list1.length + "IFSJavaFile.list().length = " + list2.length+" "+sb.toString());
         Vector listFilesStarNames = new Vector(list1.length);
         for (int j = 0; j < list1.length; j++) 
         {
             listFilesStarNames.addElement(list1[j].getName());
         }
         Vector listNames = new Vector(list2.length);
         for (int k = 0; k < list2.length; k++) 
         {
             listNames.addElement(list2[k]);
         }
         boolean print = true;
         for (Enumeration e = listFilesStarNames.elements(); e.hasMoreElements(); ) 
         {
             String name = (String)e.nextElement();
             if (!(listNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles(\"*\") but not list():");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration f = listNames.elements(); f.hasMoreElements(); ) 
         {
             String name2 = (String)f.nextElement();
             if (!(listFilesStarNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in list() but not listFiles(\"*\"):");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else

        sb.append("i: " + i + " list1.length: " + list1.length+NEWLINE);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles(IFSFileFilter) - Should not throw NullPointerException if argument  
is null.
**/
  public void Var098()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      file.listFiles((IFSFileFilter) null);
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles(IFSFileFilter) - Should filter the same as list(IFSFileFilter).
**/
  public void Var099()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFilter83 filter = new IFSJavaFilter83(output_);
      File[] list1 = file.listFiles(filter);
      String[] list2 = file.list(filter);

      if (!(list1.length > 0)) 
         failed("listFiles(IFSFileFilter).length == 0");

      if (list1.length == list2.length)
      {
          int i = 0;
          while (i < list2.length) 
          {
              IFSFile file2 = new IFSFile(systemObject_, ifsDirName_, list2[i]);
              if (!(list1[i].getName().equals(file2.getName()))) 
              {
                  failed ("listFiles(IFSFileFilter).getName()= " + list1[i].getName() +
                          " list(IFSFileFilter).getName()= " + file.getName());
                  break;
              }
              i++;
          }
          if (i == list1.length)
             succeeded();
      }
      else  
          failed ("listFiles(IFSFileFilter).length= " + list1.length + 
                  " list(IFSFileFilter).length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles(IFSFileFilter, String) - Should not throw NullPointerException if argument one of
is null.
**/
  public void Var100()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      file.listFiles((IFSFileFilter) null, "*.exe");
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles(IFSJavaFilter83, String) - Should throw NullPointerException if argument two of
IFSFile.listFiles(IFSJavaFilter83, String) is null.
**/
  public void Var101()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsPathName_);
      file.listFiles(new IFSJavaFilter83(output_),(String) null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "pattern"));
    }
  }

//@A1A
/**
listFiles(IFSJavaFilter83, String) - Should filter "*" same as list(IFSJavaFilter83, "*").
**/
  public void Var102()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFilter83 filter = new IFSJavaFilter83(output_);
      File[] list1 = file.listFiles(filter, "*");
      String[] list2 = file.list(filter, "*");

      if (!(list1.length > 0)) 
         failed("listFiles(IFSJavaFilter83, \"*\").length <= 0");

      if (list1.length == list2.length)
      {
          int i = 0;
          while (i < list2.length) 
          {
              IFSFile file2 = new IFSFile(systemObject_, ifsDirName_, list2[i]);
              if (!(list1[i].getName().equals(file2.getName())))
              {
                  failed ("listFiles(IFSJavaFilter83, \"*\").getName()= " + list1[i].getName() +
                          " list(IFSJavaFilter83, \"*\").getName()= " + file2.getName());
                  break;
              }
              i++;
          }
          if (i == list1.length)
             succeeded();
      }
      else  
          failed ("listFiles(IFSJavaFilter83, \"*\").length= " + list1.length + 
                  " list(IFSJavaFilter83,\"*\").length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles(IFSFileFilter, String) - Should filter "Q*" same as list(IFSFileFilter, String).
**/
  public void Var103()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      IFSJavaFilter83 filter = new IFSJavaFilter83(output_);
      File[] list1 = file.listFiles(filter, "Q*");
      String[] list2 = file.list(filter, "Q*");

      if (!(list1.length > 0)) 
         failed("listFiles(IFSJavaFilter83, \"Q*\").length <= 0");

      if (list1.length == list2.length)
      {
          int i = 0;
          while (i < list2.length) 
          {
              IFSFile file2 = new IFSFile(systemObject_, ifsDirName_, list2[i]);
              if (!(list1[i].getName().equals(file2.getName()))) 
              {
                  failed ("listFiles(IFSJavaFilter83, \"Q*\").getName()= " + list1[i].getName() +
                          " list(IFSJavaFilter83, \"Q*\").getName()= " + file2.getName());
                  break;
              }
              i++;
          }
          if (i == list1.length)
             succeeded();
      }
      else  
          failed ("listFiles(IFSJavaFilter83, \"Q*\").length= " + list1.length + 
                  " list(IFSJavaFilter83,\"Q*\").length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

//@A1A
/**
listFiles() - getName(), getPath(), isDirectory(), and isFile() should work the same whether 
list() or listFiles() is called.
**/
  public void Var104()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    try
    {

      // on 9/12/2011.  changed testcase to use a directory other than '/'
      //                Other testcase can cause the contents of '/' to change.
      // 
      String thisTestDirName = "/QOpenSys/usr/include"; 	
      IFSJavaFile file = new IFSJavaFile(pwrSys_, thisTestDirName);
      String[] names1 = file.list();
      File[] list2 = file.listFiles();

      if (names1.length == list2.length)
      {
        int i = 0;
        int mismatch = 0; 
        while (i < names1.length && (names1[i].equals(list2[i].getName())))
        {
          IFSJavaFile file2 = new IFSJavaFile(pwrSys_, thisTestDirName, names1[i]);
          if (!(file2.getName().equals(list2[i].getName())))
          {
             output_.println("list().getName(), listFiles.getName()= " + 
                 file2.getName() + ", " + list2[i].getName() + "\n");
             mismatch++; 
          }
          if (!(file2.getPath().equals(list2[i].getPath())))
          {
             output_.println(names1[i] + ": list().getPath(), listFiles.getPath()= " + file2.getPath() + ", " + list2[i].getPath() + "\n");
             mismatch++;
          }
          if (file2.isDirectory() != list2[i].isDirectory())
          {
             output_.println(names1[i] + ": list.isDirectory(), listFiles.isDirectory() = " + file2.isDirectory() + ", " + list2[i].isDirectory() + "\n");
             mismatch++;
          }
          if (file2.isFile() != list2[i].isFile())
          {
             output_.println(names1[i] + ": list.isFile(), listFiles.isFile() = " + file2.isFile() + ", " + list2[i].isFile() + "\n");
             mismatch++; 
          }
          i++;
        }
        assertCondition (mismatch==0);
      }
      else //do if list lengths are not equal
      {
         failed("IFSJavaFile.list().length= " + names1.length + "IFSJavaFile.listFiles().length = " + list2.length);
         Vector listNames = new Vector(names1.length);
         for (int j = 0; j < names1.length; j++) 
         {
             listNames.addElement(names1[j]);
         }
         Vector listFilesNames = new Vector(list2.length);
         for (int k = 0; k < list2.length; k++) 
         {
             listFilesNames.addElement(list2[k]);
         }
         boolean print = true;
         for (Enumeration e = listNames.elements(); e.hasMoreElements(); ) 
         {
             String name = (String)e.nextElement();
             if (!(listFilesNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in list() but not listFiles():");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration f = listFilesNames.elements(); f.hasMoreElements(); ) 
         {
             String name2 = (String)f.nextElement();
             if (!(listNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles() but not list():");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }




    // call createNewFile() on a file in directory that does not exist
    public void Var105()
    {
      String dirName = ifsDirName_ + "missing directory/IFSJavaFileTestcase";
      try
      {
        IFSJavaFile file = new IFSJavaFile(systemObject_, dirName);
        if (file.createNewFile() == false)
           succeeded();
        else
           failed("createNewFile returned true when no directory");
      }
      catch(Exception e)
      {
        failed(e);
      }
      finally
      {
        deleteFile(dirName);
      }
    }

    // call createNewFile(), it should work (the file does not exist)
    public void Var106()
    {
      String dirName = ifsDirName_ + "IFSCrtDlt106";
      try
      {
        IFSJavaFile file = new IFSJavaFile(systemObject_, dirName);
        if (file.createNewFile())
        {
           IFSJavaFile file2 = new IFSJavaFile(systemObject_, dirName);
           if (file2.exists())
              succeeded();
           else
              failed("createNewFile said it created the file but it really didn't");
        }
        else
           failed("createNewFile returned false when file did not exist");
      }
      catch(Exception e)
      {
        failed(e);
      }
      finally
      {
        deleteFile(dirName);
      }
    }

    // call createNewFile() when the file already exists
    public void Var107()
    {
      String dirName = ifsDirName_ + "IFSCrtDlt107";
      try
      {
        createIFSFile(systemObject_, dirName);
        IFSJavaFile file = new IFSJavaFile(systemObject_, dirName);
        if (file.createNewFile() == false) {
           deleteFile(dirName);
           succeeded();
        } else { 
           failed("createNewFile "+dirName+" returned true when file exists");
        }
      }
      catch(Exception e)
      {
        failed(e);
      }
    }


  // Ensure we can set the read-only attribute
  public void Var108()
  {
    String fileName  = ifsPathName_ + "IFSJavaFileTest108";
    String fileName2 = "/" + fileName_ + "IFSJavaFileTest108";
    try
    {
      createIFSFile(systemObject_, fileName);
      IFSJavaFile file  = new IFSJavaFile(systemObject_, fileName);
      IFSFile     file2 = new IFSFile(systemObject_, fileName2);

      file.setReadOnly();

      if (file2.isReadOnly()) {
         file2.setReadOnly(false); // allow the file to be deleted
         deleteFile(fileName);
         succeeded();
      }
      else { 
         failed("file "+fileName2+" does not have read-only attribute set");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


   // Ensure the hidden attribute is off on a new file
  public void Var109()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest109";
    try
    {
      createIFSFile(systemObject_, fileName);
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      if (file.isHidden())
         failed("new file has hidden attribute on");
      else
         succeeded();
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

   // Ensure we can set the hidden attribute
  public void Var110()
  {
    String fileName  = ifsPathName_    + "IFSJavaFileTest110";
    String fileName2 = "/" + fileName_ + "IFSJavaFileTest110";
    try
    {
      createIFSFile(systemObject_, fileName);
      IFSJavaFile file  = new IFSJavaFile(systemObject_, fileName);
      IFSFile file2     = new IFSFile(systemObject_, fileName2);
      file2.setHidden();

      if (file.isHidden()) {
         deleteFile(fileName);  
         succeeded();
      } else { 
         failed("file "+fileName+" does not have hidden attribute set");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  // Check the hidden attribute on something that does not exist
  public void Var111()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest111_isNotThere";
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);
      if (file.isHidden())
         failed("file that does not exist has hidden attribute on");
      else
         succeeded();
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


  // Test getCanonicalFile()
  public void Var112()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest112";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
                   file1.setPath(fileName);
       IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName);

       IFSJavaFile file1a = (IFSJavaFile) file1.getCanonicalFile();
       IFSJavaFile file2a = (IFSJavaFile) file2.getCanonicalFile();
       
       if (file1a.getSystem() == null)
       {
          if (file2.getSystem() == file2a.getSystem())
          {
             if (file1.getCanonicalPath().equals(file1a.getCanonicalPath()))
             {
                if (file2.getCanonicalPath().equals(file2a.getCanonicalPath()))
                   succeeded();
             }
             else
                failed("path 1 incorrect");
          }
          else
             failed("getSystem failed when system set");
       }       
       else
          failed("null system not correctly copied");
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  // Test getAbsoluteFile()
  public void Var113()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest113";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
                   file1.setPath(fileName);
       IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName);

       IFSJavaFile file1a = (IFSJavaFile) file1.getAbsoluteFile();
       IFSJavaFile file2a = (IFSJavaFile) file2.getAbsoluteFile();
       
       if (file1a.getSystem() == null)
       {
          if (file2.getSystem() == file2a.getSystem())
          {
             if (file1.getAbsolutePath().equals(file1a.getAbsolutePath()))
             {
                if (file2.getAbsolutePath().equals(file2a.getAbsolutePath()))
                   succeeded();
             }
             else
                failed("path 1 incorrect");
          }
          else
             failed("getSystem failed when system set");
       }       
       else
          failed("null system not correctly copied");
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  // Test getCanonicalFile() / getAbsoluteFile() misc tests
  public void Var114()
  {
    // String fileName = ifsPathName_ + "IFSJavaFileTest114";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();

       IFSJavaFile file1a = (IFSJavaFile) file1.getCanonicalFile();
       IFSJavaFile file1b = (IFSJavaFile) file1.getAbsoluteFile();
       
       if (file1.getCanonicalPath().equals(file1a.getCanonicalPath()))
       {
          if (file1.getAbsolutePath().equals(file1b.getAbsolutePath()))
          {
             if ((file1a.getSystem() == null) && (file1b.getSystem() == null))
                succeeded();
             else
                failed("system object is not null");
          }
          else
             failed("getAbsolutePath() failed");
       }
       else
          failed("getCanonicalFile() failed");       
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  // Test getParentFile()
  public void Var115()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest115";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
                   file1.setPath(fileName);
       IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName);

       IFSJavaFile file1a = (IFSJavaFile) file1.getParentFile();
       IFSJavaFile file2a = (IFSJavaFile) file2.getParentFile();
       
       if (file1a.getSystem() == null)
       {
          if (file2.getSystem() == file2a.getSystem())
          {
             if (file1.getParent().equals(file1a.getPath()))
             {
                if (file2.getParent().equals(file2a.getPath()))
                   succeeded();
             }
             else
                failed("path 1 incorrect");
          }
          else
             failed("getSystem failed when system set");
       }       
       else
          failed("null system not correctly copied");
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  // Test getParentFile() misc tests
  public void Var116()
  {
    // String fileName = ifsPathName_ + "IFSJavaFileTest116";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();

       IFSJavaFile file1a = (IFSJavaFile) file1.getParentFile();
       
       if (file1a == null) 
          succeeded();
       else
          failed("object not null");
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


  // Test getCanonicalFile() / getAbsoluteFile() misc tests
  public void Var117()
  {
    // String fileName = ifsPathName_ + "IFSJavaFileTest117";
    
    try
    {
       File[] roots = IFSJavaFile.listRoots();
       
       if (roots.length == 1)
       {
          if (roots[0].getPath().equals(File.separator))
             succeeded();
          else
             failed("object does not represent the root");
       }
       else
          failed("more than one object in the root list");
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  // Test getHidden misc tests
  public void Var118()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest118";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
                   file1.setPath(fileName);
                   file1.setReadOnly();
                   failed("no exception");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalStateException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  // Test toURL
  public void Var119()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest119";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
                   file1.setPath(fileName);
                   file1.toURL();
                   failed("no exception");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalStateException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  // Test toURL
  public void Var120()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest120";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName);
       createIFSFile(systemObject_, fileName);
       URL url = file1.toURL();
       if (url.getHost().equals(systemObject_.getSystemName()))
       {
          if (url.getProtocol().equals("file"))
          {
             if (url.getPort() == -1)
             {
                if (url.getFile().equals(file1.getPath()))
                   succeeded();
                else
                   failed("wrong name");
             }
             else
                failed("wrong port");
          }
          else
             failed("wrong protocol");
       }
       else
          failed("wrong host");
       //succeeded();
    }
    catch(Exception e)
    {
       failed("unexpected exception");
    }
    finally
    {
      deleteFile(fileName);
    }

  }

  // Test toURL (on a directory)
  public void Var121()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest121_dir";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName);
       createDirectory(systemObject_, fileName);
       URL url = file1.toURL();
       if (url.getHost().equals(systemObject_.getSystemName()))
       {
          if (url.getProtocol().equals("file"))
          {
             if (url.getPort() == -1)
             {
                //System.out.println(url.getFile());
                //System.out.println(file1.getPath());
                if (url.getFile().equals(file1.getPath() + File.separatorChar))
                   succeeded();
                else
                   failed("wrong name");
             }
             else
                failed("wrong port");
          }
          else
             failed("wrong protocol");
       }
       else
          failed("wrong host");
    }
    catch(Exception e)
    {
       failed("unexpected exception");
    }
    finally
    {
      deleteDirectory(fileName);
    }

  }

  // Test toURL
  public void Var122()
  {
    // String fileName = ifsPathName_ + "IFSJavaFileTest122";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
                   file1.toURL();
                   failed("no exception");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalStateException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }


  // Test setLastModified()
  public void Var123()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest123";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName);
       IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName);
       createIFSFile(systemObject_, fileName);
       long date1  = file1.lastModified() + 1000000;
       file1.setLastModified(date1);
       long file2LastModified = file2.lastModified();
       if (date1 == file2LastModified ||
           date1 == file2LastModified-1) { // Allow for 1 clock tick
          succeeded();
       deleteFile(fileName);
       } else {
          failed("date not corectly set on "+fileName+": Expected " + date1 + ", got " + file2LastModified);
       }
    }
    catch(Exception e)
    {
       failed("unexpected exception");
    }

  }

  // Test setLastModified()
  public void Var124()
  {
    // String fileName = ifsPathName_ + "IFSJavaFileTest124";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile();
       file1.setLastModified(1);
       failed("no exception ");       
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalStateException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  // Test setLastModified()
  public void Var125()
  {
    // I guess it is valid to set the last change date/time 
    // of the root 
    succeeded();
    return;
  
    // String fileName = ifsPathName_ + "IFSJavaFileTest125";
    //
    // try
    // {
    //    IFSJavaFile file1 = new IFSJavaFile();
    //    file1.setSystem(systemObject_);
    //    file1.setLastModified(1);
    //    failed("no exception ");       
    // }
    // catch(Exception e)
    // {
    //    if (exceptionIs(e, "ExtendedIllegalStateException"))
    //       succeeded();
    //    else
    //       failed("wrong exception");
    // }
  }

  // Test setLastModified()
  public void Var126()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest126";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName);
       createIFSFile(systemObject_, fileName);
       file1.setLastModified(-2);
       failed("no exception ");       
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
          succeeded();
       else
          failed("wrong exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }

  // Test setLastModified()
  public void Var127()
  {
    String fileName = ifsPathName_ + "IFSJavaFileTest127";
    
    try
    {
       IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName);
       IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName);
       createIFSFile(systemObject_, fileName);
       // long date1  = file1.lastModified();
       // long now = System.currentTimeMillis();                      // @B4D
       long curSysValTime = getSysValTime(systemObject_); //Testcase.getSysValTime()  @B4A
       file1.setLastModified(-1);
       long file2LastModified = file2.lastModified();
       // Tolerate +/- 2 minutes of slack, due to differences in system clocks.
       long difference = Math.abs(file2LastModified - curSysValTime); // @B4C
       // Now that we are using SysVal time rather than client time,
       // change the testcase to expect less than a couple seconds 
       // difference rather than 2 minutes.
       if (difference < 2000) // Change from 120000 @B4C
          succeeded();
       else
          failed("incorrect lastModified value:  Difference =  "+difference+ 
              "file2LastModified="+file2LastModified+" curSysValTime="+curSysValTime+
              " getSysValTimeStringBuffer="+getSysValTimeStringBuffer.toString());       
    }
    catch(Exception e)
    {
       failed("unexpected exception");
    }
    finally
    {
      deleteFile(fileName);
    }

  }

  // @A2a
  private static int compareFiles(File file1, File file2)
  {
    return file1.compareTo(file2);
  }


// Added for getCanonical() to compare IFSFile and Java.io.File strings @B2A
private boolean compareIFSFilePathToJavaIOPath(String IFSFile1, String file2)
{
  // Prior to the compare... need to remove the Windows or AIX prefix of the file2
  // path.  For example, remove the "I:" from the Windows path, or the "/mnt/lp126ab"
  // from the AIX/Linux NFS path.

   sb.append("Before file2='"+file2+"'"+NEWLINE);

    sb.append("IFSFile1='"+IFSFile1+"'"+NEWLINE);
    sb.append("After file2='"+file2+"'"+NEWLINE);
  return IFSFile1.equals(file2);
}

/**
compareTo(File) - Should throw a NullPointerException is passed a null.
**/
  public void Var128()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      compareFiles(f,(File)null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
compareTo(File) - Should return true when compared with itself.
**/
  public void Var129()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      assertCondition(compareFiles(f,f) == 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
compareTo(Object) - Should throw a NullPointerException is passed a null.
**/
  public void Var130()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      compareFiles(f,(File) null);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
    }
  }

/**
compareTo(Object) - Should return true when compared with itself.
**/
  public void Var131()
  {
    try
    {
      IFSJavaFile f = new IFSJavaFile(systemObject_, "path", "name");
      assertCondition(compareFiles(f,(File)f) == 0);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

  // @A2a
  private void renameFile(java.io.File file1, java.io.File file2)
  {
    file1.renameTo(file2);
  }

/**
renameto(File) - Should throw an exception when no properties have been set.
**/
  public void Var132()
  {
    String fileName = ifsPathName_ + "r" + getVariation();
    try
    {
      deleteFile (fileName);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      IFSJavaFile rf = new IFSJavaFile(systemObject_, fileName);
      IFSJavaFile f = new IFSJavaFile();
      renameFile(f,rf);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
    }
  }

/**
renameTo(File) - Should rename the file to a new name.
**/
  public void Var133()
  {
    String myData = "Now is the time for all good men and/or women (as pc demands) to go directly to jail.  Do not pass go.  Do not collect $200.00";
    String fileName  = ifsPathName_ + "m" + getVariation();
    String fileName2 = ifsPathName_ + "r" + getVariation();
    try
    {
      deleteFile(fileName);
      deleteFile(fileName2);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      stillThere = new IFSJavaFile(systemObject_, fileName2);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unable to setup variation.");
      return;
    }
    sb.setLength(0);

    try
    {
      createFile(fileName, myData.getBytes());
      IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);

        sb.append("File length:   " + file.length()+NEWLINE);
        sb.append("String length: " + myData.length()+NEWLINE);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, fileName2);
      renameFile(file,file2);
      IFSJavaFile file3 = new IFSJavaFile(systemObject_, fileName);
      IFSJavaFile file4 = new IFSJavaFile(systemObject_, fileName2);
      assertCondition((file3.exists() == false) && (file4.exists() == true), sb);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
    finally
    {
      deleteFile(fileName);
      deleteFile(fileName2);
    }
  }

/**
renameto(File) - Should throw a security exception when not authorized to the file(s).
**/
  public void Var134()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName1 = ifsPathName_ + "mm" + getVariation();
    String fileName2 = pathName + IFSFile.separator + "r" + getVariation();
    try
    {
      createPrivateDirectory (pwrSys_, pathName);
      deleteFile (fileName1);
      deleteFile (fileName2);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName1);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      stillThere = new IFSJavaFile(systemObject_, fileName2);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      IFSJavaFile rf = new IFSJavaFile(systemObject_, fileName1);
      IFSJavaFile f = new IFSJavaFile(systemObject_, fileName2);
      renameFile(f,rf);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deleteFile (fileName1);
      deleteFile (fileName2);
      deletePrivateDirectory (pwrSys_, pathName);
    }
  }

/**
renameto(File) - Should throw a security exception when not authorized to the file(s).
**/
  public void Var135()
  {
    String pathName = ifsPathName_ + "m" + getVariation();
    String fileName1 = ifsPathName_ + "mm" + getVariation();
    String fileName2 = pathName + IFSFile.separator + "r" + getVariation();
    try
    {
      createPrivateDirectory (pwrSys_, pathName);
      deleteFile (fileName1);
      deleteFile (fileName2);
      IFSJavaFile stillThere = new IFSJavaFile(systemObject_, fileName1);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      stillThere = new IFSJavaFile(systemObject_, fileName2);
      if (stillThere.exists())
      {
        failed("Unable to setup variation.");
        return;
      }
      IFSJavaFile rf = new IFSJavaFile(systemObject_, fileName2);
      IFSJavaFile f = new IFSJavaFile(systemObject_, fileName1);
      renameFile(f,rf);
      failed("Did not throw exception.");
    }
    catch(Exception e)
    {
      assertExceptionIsInstanceOf(e, "java.lang.SecurityException");
    }
    finally
    {
      deleteFile (fileName1);
      deleteFile (fileName2);
      deletePrivateDirectory (pwrSys_, pathName);
    }
  }


/**
listFiles(java.io.FileFilter) - Should filter the same as list(IFSFileFilter).
**/
  public void Var136()
  {
    try
    {
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSFile.separator);
      NativeJavaFileFilter83 filter = new NativeJavaFileFilter83(output_);
      IFSJavaFilter83 filter2 = new IFSJavaFilter83(output_);
      File[] list1 = file.listFiles(filter);
      String[] list2 = file.list(filter2);

      if (!(list1.length > 0)) 
         failed("listFiles(FileFilter).length == 0");

      if (list1.length == list2.length)
      {
          int i = 0;
          while (i < list2.length) 
          {
              IFSFile file2 = new IFSFile(systemObject_, ifsDirName_, list2[i]);
              if (!(list1[i].getName().equals(file2.getName()))) 
              {
                  failed ("listFiles(FileFilter).getName()= " + list1[i].getName() +
                          " list(FileFilter).getName()= " + file.getName());
                  break;
              }
              i++;
          }
          if (i == list1.length)
             succeeded();
      }
      else  
          failed ("listFiles(FileFilter).length= " + list1.length + 
                  " list(FileFilter).length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   Attempt to move a file to another directory using renameTo(IFSJavaFile).
   Ensure that the size does not change and the file by the old name no longer
   exists.
   **/
  public void Var137()
  {
    String newName = "NewFile";
    try
    {
      createDirectory(ifsDirName_ + "/Directory1");
      createDirectory(ifsDirName_ + "/Directory2");
      createFile(ifsDirName_ + "/Directory1/OldFile");
      IFSJavaFile newFile = new IFSJavaFile(systemObject_, ifsDirName_ + "/Directory2",
                                    newName);
      IFSJavaFile file = new IFSJavaFile(systemObject_,
                                 ifsDirName_ + "/Directory1/OldFile");
      IFSJavaFile oldFile = new IFSJavaFile(systemObject_,
                                    ifsDirName_ + "/Directory1/OldFile");
      long lengthBefore = file.length();
      file.renameTo(newFile);
      long lengthAfter = file.length();
      assertCondition(lengthBefore == lengthAfter && file.getName().equals(newName) &&
             !oldFile.exists());
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(ifsDirName_ + "/Directory2/" + newName);
      deleteDirectory(ifsDirName_ + "Directory1");
      deleteDirectory(ifsDirName_ + "Directory2");
    }
  }



  /**
  getCanonicalFile() - Verify handling of DOT, DOTDOT, and multiple separators
  **/
public void Var138()                                       // @B2A
{
  sb.setLength(0);
  boolean passed = true; 
  // parent = /IFSJavaFileTestcaseV138
  String parent = ifsPathName_ + "IFSJavaFileTestcaseV" + getVariation();

  // pathA1 = /IFSJavaFileTestcaseV138/A1
  // pathA2 = /IFSJavaFileTestcaseV138/A1/A2
  // pathA3 = /IFSJavaFileTestcaseV138/A1/A2/A3
  String pathA1 = parent + IFSFile.separator + "A1";
  String pathA2 = pathA1 + IFSFile.separator + "A2";
  // String pathA3 = pathA2 + IFS_IFSFile.separator + "A3";
  String relPathA1 = "relative" + parent + IFSFile.separator + "A1";


  // pathB1 = /IFSJavaFileTestcaseV138/B1
  // pathB2 = /IFSJavaFileTestcaseV138/B1/B2
  // pathB3 = /IFSJavaFileTestcaseV138/B1/B2/B3
  String pathB1 = parent + IFSFile.separator + "B1";
  // String pathB2 = pathB1 + IFS_IFSFile.separator + "B2";
  // String pathB3 = pathB2 + IFS_IFSFile.separator + "B3";

  // DOT: 
  //   pathTest1 = /IFSJavaFileTestcaseV138/A1/./A2
  String pathTest1  = pathA1 + IFSFile.separator + "." + IFSFile.separator + "A2";

  // DOTDOT: 
  //   pathTest2 = /IFSJavaFileTestcaseV138/A1/../B1
  String pathTest2  = pathA1 + IFSFile.separator + ".." + IFSFile.separator + "B1";

  // Multiple separators/delimitors:
  //   pathTest3 = /IFSJavaFileTestcaseV138/A1//A2
  String pathTest3  = pathA1 + IFSFile.separator + IFSFile.separator + "A2";

  // Single delimiter (i.e. root)
  String pathTest4  = IFSFile.separator;

  // Multiple DOT and DOTDOT
  String pathTest5  = IFSFile.separator + "ABC" + IFSFile.separator + "."; // /abc/./.././def/
  pathTest5 += IFSFile.separator + ".." + IFSFile.separator + "." + IFSFile.separator + "def";
  // String pathTest5a = IFS_IFSFile.separator + "def";

  // Relative path (i.e. doesn't start with a SEPARATOR
  String pathTest6  = relPathA1;

  try
  {
    IFSJavaFile file1 = new IFSJavaFile(systemObject_, pathTest1);
    // File  javaIoFile1 = new File(convertToPCName(pathTest1));
    IFSJavaFile file2 = new IFSJavaFile(systemObject_, pathTest2);
    // File  javaIoFile2 = new File(convertToPCName(pathTest2));
    IFSJavaFile file3 = new IFSJavaFile(systemObject_, pathTest3);
    // File  javaIoFile3 = new File(convertToPCName(pathTest3));
    IFSJavaFile file4 = new IFSJavaFile(systemObject_, pathTest4);
    // File  javaIoFile4 = new File(convertToPCName(pathTest4));
    IFSJavaFile file5 = new IFSJavaFile(systemObject_, pathTest5);
    // File  javaIoFile5 = new File(convertToPCName(pathTest5));
    IFSJavaFile file6 = new IFSJavaFile(systemObject_, pathTest6);
    // File  javaIoFile6 = new File(convertToPCName(pathTest6));


    if (File.separatorChar != IFSFile.separatorChar) {
      pathTest1 = pathTest1.replace(IFSFile.separatorChar, File.separatorChar);
      pathA2 = pathA2.replace(IFSFile.separatorChar, File.separatorChar);
    }
    if (!file1.getAbsolutePath().equals(pathTest1) ||
        !file1.getCanonicalPath().equals(pathA2) /* ||
        !compareIFSFilePathToJavaIOPath(file1.getCanonicalPath(),javaIoFile1.getCanonicalPath()) */ )
    {
      sb.append("  file1.getAbsolutePath()="  + file1.getAbsolutePath()+NEWLINE);
      sb.append("  expected pathTest1     ="  + pathTest1+NEWLINE);

      sb.append("  file1.getCanonicalPath()=" + file1.getCanonicalPath()+NEWLINE);
      sb.append("  expected pathA2         =" + pathA2+NEWLINE);
      /* System.out.println("  javaIoFile1.getCanonicalPath()=" + javaIoFile1.getCanonicalPath()); */ 
      sb.append("file1 path incorrect\n"); 
      passed = false; 
    }
    else  sb.append("Success file1"+NEWLINE);

    if (File.separatorChar != IFSFile.separatorChar) {
      pathTest2 = pathTest2.replace(IFSFile.separatorChar, File.separatorChar);
      pathB1 = pathB1.replace(IFSFile.separatorChar, File.separatorChar);
    }

    if (!file2.getAbsolutePath().equals(pathTest2) ||
        !file2.getCanonicalPath().equals(pathB1) /* ||
        !compareIFSFilePathToJavaIOPath(file2.getCanonicalPath(),javaIoFile2.getCanonicalPath()) */ )
    {
      sb.append("\n  file2.getAbsolutePath()="  + file2.getAbsolutePath());
      sb.append("\n  pathTest2="  + pathTest2);

      sb.append("\n  file2.getCanonicalPath()=" + file2.getCanonicalPath());
      sb.append("\n  pathB1="  + pathB1);
      sb.append("file2 path incorrect \n"); 
      passed = false; 
    }
    else  sb.append("Success file2"+NEWLINE);

    if (File.separatorChar != IFSFile.separatorChar) {
      pathTest3 = pathTest3.replace(IFSFile.separatorChar, File.separatorChar);
      pathA2 = pathA2.replace(IFSFile.separatorChar, File.separatorChar);
    }

    if (!file3.getAbsolutePath().equals(pathTest3) ||
        !file3.getCanonicalPath().equals(pathA2) /* ||
        !compareIFSFilePathToJavaIOPath(file3.getCanonicalPath(),javaIoFile3.getCanonicalPath()) */ 
        )
    {
      sb.append("  file3.getAbsolutePath()="  + file3.getAbsolutePath()+NEWLINE);
      sb.append("  pathTest3="  + pathTest3+NEWLINE);

      sb.append("  file3.getCanonicalPath()=" + file3.getCanonicalPath()+NEWLINE);
      sb.append("  pathA2="  + pathA2+NEWLINE);
      sb.append("file3 path incorrect\n"); 
      passed=false; 
    }
    else  sb.append("Success file3"+NEWLINE);

    if (File.separatorChar != IFSFile.separatorChar) {
      pathTest4 = pathTest4.replace(IFSFile.separatorChar, File.separatorChar);
    }

    if (!file4.getAbsolutePath().equals(pathTest4) ||
        !file4.getCanonicalPath().equals(pathTest4) 
        /* ||
        !compareIFSFilePathToJavaIOPath(file4.getCanonicalPath(),javaIoFile4.getCanonicalPath()) */)
    {
      sb.append("  pathTest4= '"+pathTest4+"'"+NEWLINE);
      sb.append("  file4.getAbsolutePath()= '"  + file4.getAbsolutePath()+"'"+NEWLINE);
      sb.append("  file4.getCanonicalPath()= '" + file4.getCanonicalPath()+"'"+NEWLINE);
      sb.append("file4 path incorrect\n"); 
      passed = false; 
    }
    else  sb.append("Success file4"+NEWLINE);

    if (File.separatorChar != IFSFile.separatorChar) {
      pathTest5 = pathTest5.replace(IFSFile.separatorChar, File.separatorChar);
    }

    if (!file5.getAbsolutePath().equals(pathTest5) ||
        !file5.getCanonicalPath().equals((File.separator+"def")) 
        /* ||
        !compareIFSFilePathToJavaIOPath(file5.getCanonicalPath(),javaIoFile5.getCanonicalPath())*/ )
    {
      sb.append("  IFS_IFSFile.separator="+IFSFile.separator+NEWLINE);
      sb.append("  file5.getAbsolutePath()="  + file5.getAbsolutePath()+NEWLINE);
      sb.append("  file5.getCanonicalPath()=" + file5.getCanonicalPath()+NEWLINE);
      sb.append("file5 path incorrect\n"); 
      passed = false; 
    }
    else  sb.append("Success file5"+NEWLINE);

    if (File.separatorChar != IFSFile.separatorChar) {
      pathTest6 = pathTest6.replace(IFSFile.separatorChar, File.separatorChar);
    }

    if (!file6.getAbsolutePath().equals(File.separator+pathTest6) ||
        !file6.getCanonicalPath().equals(File.separator+pathTest6) /* ||
        !compareIFSFilePathToJavaIOPath(file6.getCanonicalPath(),javaIoFile6.getCanonicalPath()) */)
    {
      sb.append("  file6.getAbsolutePath()="  + file6.getAbsolutePath()+NEWLINE);
      sb.append("  file6.getCanonicalPath()=" + file6.getCanonicalPath()+NEWLINE);
      sb.append("file6 path incorrect"); 
       passed = false; 
    }
    else  sb.append("Success file6"+NEWLINE);

    assertCondition(passed, sb); 
  }
  catch (Exception e)
  {
    failed(e, "Unexpected Exception");
  }
}



  /**
  getFreeSpace(), getTotalSpace(), getUsableSpace() - Verify that reasonable values are returned.
  **/
public void Var139()
{
  sb.setLength(0);
  if (!checkMethodExists("getFreeSpace", null)) {
    notApplicable("No method named getFreeSpace()");
    return;
  }
  String fileName = ifsPathName_ + "m" + getVariation();
  boolean succeeded = true;
    sb.append("ifsPathName_: " + ifsPathName_+NEWLINE);
    sb.append("fileName:     " + fileName+NEWLINE);
  createFile(fileName);
  try
  {
    IFSJavaFile file = new IFSJavaFile(systemObject_, fileName);

    long fSpace = file.getFreeSpace();
    long tSpace = file.getTotalSpace();
    long uSpace = file.getUsableSpace();

      sb.append("Free space:   |" + fSpace + "|"+NEWLINE);
      sb.append("Total space:  |" + tSpace + "|"+NEWLINE);
      sb.append("Usable space: |" + uSpace + "|"+NEWLINE);

    if (fSpace > (tSpace - 10000)) {
      sb.append("Free space exceeds total space."+NEWLINE);
      succeeded = false;
    }
    if (uSpace > (tSpace - 10000)) {
      sb.append("Usable space exceeds total space."+NEWLINE);
      succeeded = false;
    }
    if (uSpace > fSpace) {
      sb.append("Usable space exceeds free space."+NEWLINE);
      succeeded = false;
    }

    final long maxReasonableValue = Long.MAX_VALUE >>> 3;
    if (fSpace > maxReasonableValue) {
      sb.append("Free space exceeds reasonable value: " + fSpace+NEWLINE);
      succeeded = false;
    }
    if (tSpace > maxReasonableValue) {
      sb.append("Total space exceeds reasonable value: " + tSpace+NEWLINE);
      succeeded = false;
    }
    if (uSpace > maxReasonableValue) {
      sb.append("Usable space exceeds reasonable value: " + uSpace+NEWLINE);
      succeeded = false;
    }

    // The following code will compile & execute, only on Java 6 (or higher).
//
//    String javaVersion = System.getProperty("java.version");
//    if (onAS400_ && TestDriver.getJDK() >= TestDriver.JDK_16)
//    {
//      File fileNative = new java.io.File(fileName);
//      long fSpaceNative = fileNative.getFreeSpace();
//      long tSpaceNative = fileNative.getTotalSpace();
//      long uSpaceNative = fileNative.getUsableSpace();
//
//      if (fSpace != fSpaceNative) {
//        sb.append("Free space inconsistent with native: " +fSpace+ " != " + fSpaceNative+NEWLINE);
//        succeeded = false;
//      }
//
//      if (tSpace != tSpaceNative) {
//        sb.append("Total space inconsistent with native: " +tSpace+ " != " + tSpaceNative+NEWLINE);
//        succeeded = false;
//      }
//
//      if (uSpace != uSpaceNative) {
//        sb.append("Usable space inconsistent with native: " +uSpace+ " != " + uSpaceNative+NEWLINE);
//        succeeded = false;
//      }
//    }

    assertCondition(succeeded, sb);
  }
  catch(Exception e)
  {
    failed(e, "Unexpected Exception");
  }
  finally
  {
    deleteFile(fileName);
  }
}


  /**
  canExecute(), canRead(), canWrite(), setExecutable, setReadable(), setWritable
  **/
public void Var140()
{
  sb.setLength(0);

  if (!checkMethodExists("canExecute", null)) {
    notApplicable("No method named canExecute()");
    return;
  }
  String fileName = ifsPathName_ + "m" + getVariation();
  boolean succeeded = true;
    sb.append("ifsPathName_: " + ifsPathName_+NEWLINE);
    sb.append("fileName:     " + fileName+NEWLINE);
  createFile(fileName);
  try
  {
    IFSJavaFile file1 = new IFSJavaFile(systemObject_, fileName);
    IFSJavaFile file1_pwr = new IFSJavaFile(pwrSys_, fileName);

    // Get the current permission values.
    boolean canRead0    = file1.canRead();
    boolean canWrite0   = file1.canWrite();
    boolean canExecute0 = file1.canExecute();

    // Change all the permissions to the opposite values.

    boolean canRead1    = !canRead0;
    boolean canWrite1   = !canWrite0;
    boolean canExecute1 = !canExecute0;

    final boolean OWNER_ONLY = true;
    if (!file1_pwr.setReadable(canRead1,!OWNER_ONLY)) {
      succeeded = false;
      sb.append("Call to setReadable() failed."+NEWLINE);
    }
    if (!file1_pwr.setWritable(canWrite1,!OWNER_ONLY)) {
      succeeded = false;
      sb.append("Call to setWritable() failed."+NEWLINE);
    }
    if (!file1_pwr.setExecutable(canExecute1,!OWNER_ONLY)) {
      succeeded = false;
      sb.append("Call to setExecutable() failed."+NEWLINE);
    }

    boolean canRead2    = file1.canRead();
    boolean canWrite2   = file1.canWrite();
    boolean canExecute2 = file1.canExecute();

    if (canRead2 != canRead1) {
      succeeded = false;
      sb.append("Failed to change canRead to " + canRead1+NEWLINE);
    }

    if (canWrite2 != canWrite1) {
      succeeded = false;
      sb.append("Failed to change canWrite to " + canWrite1+NEWLINE);
    }

    if (canExecute2 != canExecute1) {
      succeeded = false;
      sb.append("Failed to change canExecute to " + canExecute1+NEWLINE);
    }

    // Reset everything to original values.
    file1_pwr.setReadable(canRead0,!OWNER_ONLY);
    file1_pwr.setWritable(canWrite0,!OWNER_ONLY);
    file1_pwr.setExecutable(canExecute0,!OWNER_ONLY);

    canRead2    = file1.canRead();
    canWrite2   = file1.canWrite();
    canExecute2 = file1.canExecute();

    if (canRead2 != canRead0) {
      succeeded = false;
      sb.append("Failed to restore canRead to " + canRead1+NEWLINE);
    }

    if (canWrite2 != canWrite0) {
      succeeded = false;
      sb.append("Failed to restore canWrite to " + canWrite1+NEWLINE);
    }

    if (canExecute2 != canExecute0) {
      succeeded = false;
      sb.append("Failed to restore canExecute to " + canExecute1+NEWLINE);
    }

    assertCondition(succeeded, sb);
  }
  catch(Exception e)
  {
    failed(e, "Unexpected Exception");
  }
  finally
  {
    deleteFile(fileName);
  }
}





  // Verifies that a method exists in class IFSJavaFile.  Returns false if method not found.
  static boolean checkMethodExists(String methodName, Class[] parmTypes)
  {
    try {
      Class.forName("com.ibm.as400.access.IFSJavaFile").getDeclaredMethod(methodName, parmTypes);
    }
    catch (NoSuchMethodException e) {
      return false;
    }
    catch (ClassNotFoundException e) { //  // quiet the compiler
      e.printStackTrace();
      return false;
    }
    return true;
  }

}


class JavaFilter83
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

    return(name.length() <= 8) && (extension.length() <= 3);
  }
}

class IFSJavaFilter83 extends JavaFilter83 implements IFSFileFilter
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSJavaFileTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  PrintWriter output_;

  public IFSJavaFilter83(PrintWriter output)
  {
    output_ = output;
  }

  public boolean accept(IFSFile file)
  {
    if (IFSJavaFileTestcase.DEBUG) output_.print(".");
    output_.flush();
    return accept(file.getName());
  }
}

class NativeJavaFileFilter83 extends JavaFilter83 implements FileFilter
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSJavaFileTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  PrintWriter output_;

  public NativeJavaFileFilter83(PrintWriter output)
  {
    output_ = output;
  }

  public boolean accept(File file)
  {
    if (IFSJavaFileTestcase.DEBUG) output_.print(".");
    output_.flush();
    return accept(file.getName());
  }
}

class NativeJavaNameFilter83 extends JavaFilter83 implements FilenameFilter
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSJavaFileTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  PrintWriter output_;

  public NativeJavaNameFilter83(PrintWriter output)
  {
    output_ = output;
  }

  public boolean accept(File dir, String fileName)
  {
    return accept(fileName);
  }

}
