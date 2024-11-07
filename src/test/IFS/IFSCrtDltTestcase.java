///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSCrtDltTestcase.java
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
import java.util.Random;
import com.ibm.as400.access.*;

import test.JDTestDriver;



/**
Test file and directory create/delete operations on IFSFile.
**/
public class IFSCrtDltTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSCrtDltTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }

/**
Constructor.
**/
  public IFSCrtDltTestcase (AS400 systemObject,
                   String userId, 
                   String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userId, password, "IFSCrtDltTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys); 
    }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {

    
    super.setup(); 
    
    // Make sure the user has access to '/QOpenSys/tmp' 
    
    CommandCall cc = new CommandCall(pwrSys_); 
    String command = "CHGAUT OBJ('/QOpenSys/tmp') USER("+systemObject_.getUserId()+") DTAAUT(*RWX)"; 
    cc.run(command); 
    
   
    dirName_ = "/"; 
  }





  String getUniqueName(String directory)
  {
    Random random = new Random();
    String name = null;
    String nnn = null;  //@A1A
    try
    {
        do
        {
          // Note: Filenames longer than 8 chars can confuse DOS tools
          // such as DELTREE.
          nnn = Integer.toString(Math.abs(random.nextInt()));  //@A1A
          if (nnn.length() > 8)  nnn = nnn.substring(0,8);     //@A1A
          name = directory + IFSFile.separator + nnn;          //@A1C
        }
        while(new IFSFile(systemObject_, name).exists());
    }
    catch(Exception e)
    {
      if (DEBUG)
        e.printStackTrace(output_);
    }

    return name;
  }

  void setPrivate(AS400 as400, String dirName)
  {
    String cmdString = "CHGAUT OBJ('" 
                     + dirName
                     + "') USER(*PUBLIC) DTAAUT(*EXCLUDE) OBJAUT(*NONE)";

    String cmdString2 = "CHGAUT OBJ('" 
                     + dirName
                     + "') USER("
                     + systemObject_.getUserId()
                     + ") DTAAUT(*NONE) OBJAUT(*NONE)";

    CommandCall cmd = new CommandCall(as400);
    try
    {
      cmd.setCommand(cmdString);
      cmd.run();
      cmd.setCommand(cmdString2);
      cmd.run();
    }
    catch(Exception e)
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

    String cmdString2 = "CHGAUT OBJ('" 
                     + dirName
                     + "') USER("
                     + systemObject_.getUserId()
                     + ") DTAAUT(*RWX) OBJAUT(*ALL)";

    CommandCall cmd = new CommandCall(as400);
    try
    {
      cmd.setCommand(cmdString);
      cmd.run();
      cmd.setCommand(cmdString2);
      cmd.run();
    }
    catch(Exception e)
    {
      if (DEBUG) e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }
  }

/**
Ensure that false is returned if IFSFile.delete() is
called on a non-existent file.
**/
  public void Var001()
  {
    deleteFile(ifsPathName_);
    IFSFile file = null;
    boolean rc;
    try
    {
      file = new IFSFile(systemObject_, ifsPathName_);
      rc = file.delete();

      assertCondition(rc == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that false is returned if IFSFile.delete() is
called on a non-empty directory.
**/
  public void Var002()
  {
    IFSFile file = null;
    boolean rc;
    try
    {
      file = new IFSFile(systemObject_, ifsDirName_);
      rc = file.delete();
     assertCondition(rc == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that true is returned if IFSFile.delete() is
called on an existing file.
**/
  public void Var003()
  {
     String pathName = ifsPathName_+"003"; 
    IFSFile file = null;
    boolean rc;
    createFile(pathName);
    try
    {
      file = new IFSFile(systemObject_, pathName);
      rc = file.delete();
      assertCondition(rc == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Test wildcard '?' with IFSFile.delete().
**/
  public void Var004()
  {
    for (int i = 0; i < 5; i++)
      createFile(ifsPathName_ + Integer.toString(i));
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_ + "?");
      boolean rc = file.delete();
      assertCondition(rc == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
    for (int i = 0; i < 5; i++)
      deleteFile(ifsPathName_ + Integer.toString(i));
  }

/**
Test wildcard '*' with IFSFile.delete().
**/
  public void Var005()
  {
    for (int i = 0; i < 5; i++)
      createFile(ifsPathName_ + Integer.toString(i));
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_ + "*");
      boolean rc = file.delete();
      assertCondition(rc == true);
    }
    catch(Exception e)
    {
      failed(e);
    }
    for (int i = 0; i < 5; i++)
      deleteFile(ifsPathName_ + Integer.toString(i));
  }

/**
Ensure that false is returned by IFSFile.mkdir() if
 the directory already exists.
**/
  public void Var006()
  {
    createDirectory(ifsDirName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      boolean rc = file.mkdir();
      assertCondition(rc == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that false is returned by IFSFile.mkdir() if
an attempt is made to create a subdirectory in a non-existent directory.
**/
  public void Var007()
  {
    String nonExistentDir = getUniqueName(ifsDirName_ + "NoDir");
    try
    {
      IFSFile file =
        new IFSFile(systemObject_, nonExistentDir + "/SubDirectory");
      boolean rc = file.mkdir();
      assertCondition(rc == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

/**
Ensure that true is returned by
IFSFile.mkdir() if a directory is created.
**/
  public void Var008()
  {
    createDirectory(ifsDirName_ + "NoDir");
    String nonExistentDir = getUniqueName(ifsDirName_ + "NoDir");
    try
    {
      IFSFile file = new IFSFile(systemObject_, nonExistentDir);
      boolean rc = file.mkdir();
      assertCondition(rc == true && file.exists());
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteDirectory(ifsDirName_ + "NoDir");
  }

/**
Ensure that false is returned by IFSFile.mkdirs() if
 all directories already exist.
**/
  public void Var009()
  {
    String dirName = getUniqueName(ifsDirName_ + "BaseDir");
    createDirectory(dirName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, dirName);
      boolean rc = file.mkdirs();
      assertCondition(rc == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
    String dir = dirName.substring(0,
                                   dirName.indexOf(IFSFile.separatorChar,
                                   dirName.indexOf(IFSFile.separatorChar) +
                                   1));
    deleteDirectory(dir);
  }

/**
Ensure that true is returned by
IFSFile.mkdirs() if directories are created.
**/
  public void Var010()
  {
    String dirName = getUniqueName(ifsDirName_ + "BaseDir");

    try
    {
      IFSFile file = new IFSFile(systemObject_, dirName);
      boolean rc = file.mkdirs();
      assertCondition(rc == true && file.exists());
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteDirectory(dirName);
  }

/**
Ensure that NullPointerException is thrown by IFSFile.renameTo(IFSFile) if
argument one is null.
**/
  public void Var011()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.renameTo((IFSFile) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "file");
    }
  }

/**
Rename a file using IFSFile.renameTo(IFSFile).  Ensure that IFSFile.getName()
returns the new name.  Ensure that the size does not change and the file by
the old name no longer exists.
**/
  public void Var012()
  {
    createFile(ifsPathName_);
    String newName = "NewFile";
    try
    {
      IFSFile newFile = new IFSFile(systemObject_, ifsDirName_, newName);
      if (newFile.exists()) { 
    	  newFile.delete(); 
      }
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFile oldFile = new IFSFile(systemObject_, ifsPathName_);
      long lengthBefore = file.length();
      boolean result = file.renameTo(newFile);
      long lengthAfter = file.length();
      assertCondition(result && lengthBefore == lengthAfter && file.getName().equals(newName) &&
             !oldFile.exists(), "result="+result+" sb true  lengthAfter="+lengthAfter+" sb "+lengthBefore+" file.getName() is "+file.getName()+" sb  "+newName+" oldFile="+oldFile);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
    deleteFile(ifsDirName_ + newName);
  }

  /**
   Attempt to move a file to another directory using IFSFile.renameTo(IFSFile).
   Ensure that the size does not change and the file by the old name no longer
   exists.
   **/
  public void Var013()
  {
    String newName = "NewFile";
    createDirectory(ifsDirName_ + "/Directory1");
    createDirectory(ifsDirName_ + "/Directory2");
    createFile(ifsDirName_ + "/Directory1/OldFile");
    try
    {
      IFSFile newFile = new IFSFile(systemObject_, ifsDirName_ + "/Directory2",
                                    newName);
      IFSFile file = new IFSFile(systemObject_,
                                 ifsDirName_ + "/Directory1/OldFile");
      IFSFile oldFile = new IFSFile(systemObject_,
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
    deleteFile(ifsDirName_ + "/Directory2/" + newName);
    deleteDirectory(ifsDirName_ + "Directory1");
    deleteDirectory(ifsDirName_ + "Directory2");
  }

  /**
   Ensure that null is returned by IFSFile.renameTo(IFSFile) when attempting
   to rename a file to the name of an existing file.
   **/
   public void Var014()
   {
     createFile(ifsDirName_ + "File1");
     createFile(ifsDirName_ + "File2");
     try
     {
       IFSFile file1 = new IFSFile(systemObject_, ifsDirName_ + "File1");
       IFSFile file2 = new IFSFile(systemObject_, ifsDirName_ + "File2");
       assertCondition(!file1.renameTo(file2));
     }
     catch(Exception e)
     {
       failed(e);
     }
     deleteFile(ifsDirName_ + "File1");
     deleteFile(ifsDirName_ + "File2");
   }

   /**
   * Rename a file using IFSFile.renameTo(IFSFile) in the QOpenSys file system.
   * The target file name should only differ from the source file name by case.
   * Ensure that IFSFile.getName() returns the new name. Ensure that the size
   * does not change and the file by the old name no longer exists. QOpenSys is
   * the only AS/400 file system that is case sensitive.
   **/
  public void Var015() {
    String sourceName = "file";
    String targetName = sourceName.toUpperCase();
    createFile("/QOpenSys/tmp/" + sourceName);
    try {
      boolean passed = true;
      StringBuffer sb = new StringBuffer("Note: if this test fils, check the permissions for /QOpenSys/tmp");
      IFSFile newFile = new IFSFile(systemObject_, "/QOpenSys/tmp/",
          targetName);
      IFSFile file = new IFSFile(systemObject_, "/QOpenSys/tmp/" + sourceName);
      IFSFile oldFile = new IFSFile(systemObject_,
          "/QOpenSys/tmp/" + sourceName);
      long lengthBefore = file.length();
      passed = file.renameTo(newFile);
      long lengthAfter = file.length();
      if (lengthBefore != lengthAfter) {
        passed = false;
        sb.append("\nFailed: lengthBefore(" + lengthBefore + ") != lengthAfter("
            + lengthAfter + ")");
      }
      if (!file.getName().equals(targetName)) {
        passed = false;
        sb.append("\nFailed: test file.getName().equals(targetName) getName="
            + file.getName() + " targetName=" + targetName);
      }
      if (oldFile.exists()) {
        passed = false;
        sb.append("\noldFile.exists() where oldFile=" + oldFile);
      }

	assertCondition(passed, sb); 
      }
      catch(Exception e)
      {
        failed(e);
      }
      finally {
        deleteFile("/QOpenSys/tmp/" + sourceName);
        deleteFile("/QOpenSys/tmp/" + targetName);
      }
    }

    /**
     Ensure that true is returned if IFSFile.delete() is
     called on an empty directory.
     **/
    public void Var016()
    {
      String dirName = ifsDirName_ + "IFSCrtDltTestcase";
      deleteDirectory(dirName); // to make sure the directory is empty
      createDirectory(dirName);
      try
      {
        IFSFile file = new IFSFile(systemObject_, dirName);
        assertCondition(file.delete());
      }
      catch(Exception e)
      {
        failed(e);
      }
      deleteDirectory(dirName);
    }

    // call createNewFile() on a file in directory that does not exist
    public void Var017()
    {
      String dirName = ifsDirName_ + "missing directory/IFSCrtDltTestcase";
      try
      {
        IFSFile file = new IFSFile(systemObject_, dirName);
        if (file.createNewFile() == false)
           succeeded();
        else
           failed("createNewFile returned true when no directory");
      }
      catch(Exception e)
      {
        failed(e);
      }
    }

    // call createNewFile(), it should work (the file does not exist)
    public void Var018()
    {
      String dirName = ifsDirName_ + "IFSCrtDlt18";
      try
      {
        IFSFile file = new IFSFile(systemObject_, dirName);
        if (file.createNewFile())
        {
           IFSFile file2 = new IFSFile(systemObject_, dirName);
           if (file2.exists())
              succeeded();
           else
              failed("createNewFile said it created the file ("+dirName+") but it really didn't");
        }
        else
           failed("createNewFile returned false when ("+dirName+")file did not exist");
      }
      catch(Exception e)
      {
        failed(e);
      }
      deleteFile(dirName);
    }

    // call createNewFile() when the file already exists
    public void Var019()
    {
      String dirName = ifsDirName_ + "IFSCrtDlt19";
      try
      {
        createFile(dirName, "Hi Mom");
        IFSFile file = new IFSFile(systemObject_, dirName);
        if (file.createNewFile() == false)
           succeeded();
        else
           failed("createNewFile returned true when file exists");
      }
      catch(Exception e)
      {
        failed(e);
      }
      deleteFile(dirName);
    }

    // call createNewFile() when not authorized to the directory
    public void Var020()
    {
      if (pwrSys_ == null)
         failed("Power user not specified so could not run not-authorized test case");
      else
      {
         try
         {
            String dirName  = ifsDirName_ + "IFSCrtDlt20Dir";
            String fileName = dirName + "/IFSCrtDlt20";
            createDirectory(pwrSys_, dirName);
            setPrivate(pwrSys_, dirName);         
         
            try
            {
               IFSFile file = new IFSFile(systemObject_, fileName);
               file.createNewFile();
               failed("Didn't throw exception.");
            }
            catch (Exception e)
            {
              assertExceptionIs(e, "ExtendedIOException", ExtendedIOException.ACCESS_DENIED);
            }
            setPublic(pwrSys_, dirName);
            deleteFile(fileName);
            deleteDirectory(dirName);
         }     
         catch(Exception e)
         {
           failed(e);
         }  
      }
    }

    // call IFSFile.delete() on various QSYS Objects                @A2A
    // Test fix to IFSFileImplRemote.determineIsDirectory().  Prior to fix
    // some QSYS objects were incorrectly identified as directory objects.
    // The error caused the toolbox to incorrectly initiate a delete directory
    // operation to the file server rather than the correct delete file.
    public void Var021()                                          //@A2A
    {
      StringBuffer sb = new StringBuffer(); 
      try
      {
        String libName = "IFSCDv21";
        String objName = "IFSCDv21";
	CommandCall c = new CommandCall(systemObject_);
	c.run("CHGJOB INQMSGRPY(*SYSRPYL)");

        c = new CommandCall(systemObject_, "DLTLIB "+libName);
        c.run();
        c.run("CRTLIB "+libName);

        IFSFile lib1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB");
        sb.append("lib1 AbsPath: " + lib1.getAbsolutePath()+"\n");
        sb.append("lib1 Exists: "+lib1.exists()+"\n");
        sb.append("lib1 IsDirectory/IsFile = " + lib1.isDirectory()+"/"+ lib1.isFile()+"\n");

        c.run("CRTOUTQ "+libName+"/"+objName);
        IFSFile file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".OUTQ");
        sb.append("file1 AbsPath: " + file1.getAbsolutePath()+"\n");
        sb.append("file1 Exists: "+file1.exists()+"\n");
        sb.append("file1 IsDirectory/IsFile = " + file1.isDirectory()+"/"+ file1.isFile()+"\n");
        boolean deleted2 = file1.delete();
        sb.append("file1: deleted2 = " + deleted2+"\n");

        c.run("CRTSAVF "+libName+"/"+objName);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted3 = file1.delete();
        sb.append("file1: deleted3 = " + deleted3+"\n");

        c.run("CRTDSPF "+libName+"/"+objName);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted4 = file1.delete();
        sb.append("file1: deleted4 = " + deleted4+"\n");

        c.run("CRTSRCPF FILE("+libName+"/"+objName+")");
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted5 = file1.delete();
        sb.append("file1: deleted5 = " + deleted5+"\n");

        boolean deleted6 = true;
        /* After 7.5, IOSYSCFG special authority is need to create this file */ 
        if (getRelease() <= JDTestDriver.RELEASE_V7R5M0) {
          String command = "CRTTAPF FILE(" + libName + "/" + objName + ")";
          c.run(command);
          sb.append(".. ran " + command);
          file1 = new IFSFile(systemObject_, "/QSYS.LIB/" + libName + ".LIB/" + objName + ".FILE");
          deleted6 = file1.delete();
          sb.append("file1: deleted6 = " + deleted6 + "\n");
        }

        c.run("CRTPRTF "+libName+"/"+objName);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted7 = file1.delete();
        sb.append("file1: deleted7 = " + deleted7+"\n");

        // now delete the original library created at top of this var
        boolean deleted1 = lib1.delete();
        sb.append("lib1: deleted1 = " + deleted1+"\n");

        if (deleted1 == deleted2 == deleted3 == deleted4 == deleted5 == deleted6 == deleted7 == true)
        {
          succeeded();
        }
        else
        {
          failed("delete() of (at least one) QSYS object failed \n"+sb.toString());
        }
      }     
      catch(Exception e)
      {
        failed(e);
      }  
    }


    // Duplicate of VAR200 but with all authority. 
    public void Var022()                                          //@A2A
    {
      StringBuffer sb = new StringBuffer(); 
      try
      {
        String libName = "IFSCDv22";
        String objName = "IFSCDv22";
        CommandCall c = new CommandCall(pwrSys_);
        c.run("CHGJOB INQMSGRPY(*SYSRPYL)");

        c = new CommandCall(systemObject_, "DLTLIB "+libName);
        c.run();
        c.run("CRTLIB "+libName);

        IFSFile lib1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB");
        sb.append("lib1 AbsPath: " + lib1.getAbsolutePath()+"\n");
        sb.append("lib1 Exists: "+lib1.exists()+"\n");
        sb.append("lib1 IsDirectory/IsFile = " + lib1.isDirectory()+"/"+ lib1.isFile()+"\n");

        c.run("CRTOUTQ "+libName+"/"+objName);
        IFSFile file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".OUTQ");
        sb.append("file1 AbsPath: " + file1.getAbsolutePath()+"\n");
        sb.append("file1 Exists: "+file1.exists()+"\n");
        sb.append("file1 IsDirectory/IsFile = " + file1.isDirectory()+"/"+ file1.isFile()+"\n");
        boolean deleted2 = file1.delete();
        sb.append("file1: deleted2 = " + deleted2+"\n");

        c.run("CRTSAVF "+libName+"/"+objName);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted3 = file1.delete();
        sb.append("file1: deleted3 = " + deleted3+"\n");

        c.run("CRTDSPF "+libName+"/"+objName);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted4 = file1.delete();
        sb.append("file1: deleted4 = " + deleted4+"\n");

        c.run("CRTSRCPF FILE("+libName+"/"+objName+")");
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted5 = file1.delete();
        sb.append("file1: deleted5 = " + deleted5+"\n");

        /* After 7.5, IOSYSCFG special authority is need to create this file */ 
        String command = "CRTTAPF FILE("+libName+"/"+objName+")";
        c.run(command);
        sb.append(".. ran "+command);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted6 = file1.delete();
        sb.append("file1: deleted6 = " + deleted6+"\n");

        c.run("CRTPRTF "+libName+"/"+objName);
        file1 = new IFSFile(systemObject_, "/QSYS.LIB/"+libName+".LIB/"+objName+".FILE");
        boolean deleted7 = file1.delete();
        sb.append("file1: deleted7 = " + deleted7+"\n");

        // now delete the original library created at top of this var
        boolean deleted1 = false;
        if (deleted2 == deleted3 == deleted4 == deleted5 == deleted6 == deleted7 == true) {
          deleted1 = lib1.delete();
          sb.append("lib1: deleted1 = " + deleted1 + "\n");
        } else {
          sb.append("Did not delete library because testcase failed\n");
        }

        if (deleted1 == deleted2 == deleted3 == deleted4 == deleted5 == deleted6 == deleted7 == true)
        {
          succeeded();
        }
        else
        {
          failed("delete() of (at least one) QSYS object failed \n"+sb.toString());
        }
      }     
      catch(Exception e)
      {
        failed(e);
      }  
    }




}





