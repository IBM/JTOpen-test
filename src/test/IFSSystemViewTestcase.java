///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSSystemViewTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import java.io.FileOutputStream;
import java.io.File;

import java.awt.Frame;
import java.util.Hashtable;
import javax.swing.JFileChooser;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSJavaFile;

import com.ibm.as400.access.IFSSystemView;


/**
Test write methods for IFSFileInputStream, IFSFileOutputStream, and
IFSRandomAccessFile.
**/
public class IFSSystemViewTestcase extends IFSGenericTestcase
{
  String collection_ = "COL"; 
  /**
   Constructor.
   **/
  public IFSSystemViewTestcase (AS400 systemObject,
      String userid,
      String password,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String   driveLetter,
                                    AS400    pwrSys)
  {
    super (systemObject, userid, password, "IFSSystemViewTestcase",
           namesAndVars, runMode, fileOutputStream, driveLetter, pwrSys);

    collection_ = IFSTests.COLLECTION;

    ifsDirName_ = "IFSViewTestDir"+collection_;
    ifsDirPath_ = FILE_SEPARATOR + ifsDirName_;
    ifsFileName_ = "TestFile";
    ifsFilePath_ = ifsDirPath_ + FILE_SEPARATOR + ifsFileName_;

  }


  /**
   Reset the file name during setup
   */
  protected void setup() throws Exception {
    super.setup();

    collection_ = IFSTests.COLLECTION;

    ifsDirName_ = "IFSViewTestDir"+collection_;
    ifsDirPath_ = FILE_SEPARATOR + ifsDirName_;
    ifsFileName_ = "TestFile";
    ifsFilePath_ = ifsDirPath_ + FILE_SEPARATOR + ifsFileName_;


  } 

  /**
   Constructor: IFSSystemView(AS400 system).
   Verify that it throws NullPointerException if arg is null.
   **/
  public void Var001()
  {

      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView((AS400)null);
      failed("Exception didn't occur for "+view);
    }
    catch(Exception e) {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }


  /**
   createFileObject(File directory, String name).
   Verify that it doesn't throws exception if first arg is null.
   **/
  public void Var002()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile file = (IFSJavaFile)view.createFileObject((IFSJavaFile)null, ifsDirPath_+File.separator+"File1");
      IFSJavaFile expected = new IFSJavaFile(systemObject_, ifsDirPath_+File.separator+"File1");
      assertCondition(file.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   createFileObject(File directory, String name).
   Verify that it throws NullPointerException if second arg is null.
   **/
  public void Var003()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsDirPath_);
      view.createFileObject(dir, (String)null);
      failed("Exception didn't occur");
    }
    catch(Exception e) {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

  /**
   createFileObject(File directory, String name).
   Verify that it doesn't throw exception if first arg is not an IFSJavaFile.
   **/
  public void Var004()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File dir = new File(ifsDirPath_);
      IFSJavaFile file = (IFSJavaFile)view.createFileObject(dir, ifsFileName_);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, ifsDirPath_, ifsFileName_);
      assertCondition(file.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   createFileObject(File directory, String name).
   Verify that it returns a valid IFSJavaFile object.
   **/
  public void Var005()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsDirPath_);
      File file = view.createFileObject(dir, ifsFileName_);
      assertCondition(file instanceof IFSJavaFile &&
                      file.getAbsolutePath().equals(ifsFilePath_), "file.getAbolutePath="+file.getAbsolutePath()+" ifsFilePath_="+ifsFilePath_); 
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



  /**
   createFileObject(String name).
   Verify that it throws NullPointerException if arg is null.
   **/
  public void Var006()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      view.createFileObject((String)null);
      failed("Exception didn't occur");
    }
    catch(Exception e) {
      assertExceptionIs(e, "NullPointerException"/*, "name"*/);
    }
  }

  /**
   createFileObject(String name).
   Verify that it returns a valid IFSJavaFile object.
   **/
  public void Var007()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File file = view.createFileObject(ifsFilePath_);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, ifsFilePath_);
      assertCondition(file.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



  /**
   createNewFolder(File directory).
   Verify that it throws NullPointerException if arg is null.
   **/
  public void Var008()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      view.createNewFolder((IFSJavaFile)null);
      failed("Exception didn't occur");
    }
    catch(Exception e) {
      assertExceptionIs(e, "IOException");
    }
  }

  /**
   createNewFolder(File directory).
   Verify that it doesn't throw exception if arg is not an IFSJavaFile.
   **/
  public void Var009()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File parent = new File(ifsDirPath_);
      IFSJavaFile file = (IFSJavaFile)view.createNewFolder(parent);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, ifsDirPath_+File.separator+"NewFolder");
      assertCondition(file.equals(expected), "file="+file+" expected="+expected);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteDirectory(ifsDirPath_+File.separator+"NewFolder");
      deleteDirectory(ifsDirPath_);
    }
  }

  /**
   createNewFolder(File directory).
   Verify that it returns a valid IFSJavaFile object.
   **/
  public void Var010()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      createDirectory(ifsDirPath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile parent = new IFSJavaFile(systemObject_, ifsDirPath_);
      IFSJavaFile folder1 = (IFSJavaFile)view.createNewFolder(parent);
      IFSJavaFile expected1 = new IFSJavaFile(systemObject_, ifsDirPath_ + IFSJavaFile.separator + "NewFolder");
      IFSJavaFile folder2 = (IFSJavaFile)view.createNewFolder(parent);
      IFSJavaFile expected2 = new IFSJavaFile(systemObject_, ifsDirPath_ + IFSJavaFile.separator + "NewFolder.1");
      assertCondition(folder1.equals(expected1) && folder2.equals(expected2));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteFile(ifsFilePath_);
      deleteDirectory(ifsDirPath_);
    }
  }




  /**
   getFiles(File dir, boolean useFileHiding).
   Verify that it throws NullPointerException if first arg is null.
   **/
  public void Var011()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      view.getFiles((IFSJavaFile)null, true);
      failed("Exception didn't occur");
    }
    catch(Exception e) {
      assertExceptionIs(e, "NullPointerException"/*, "directory"*/);
    }
  }

  /**
   getFiles(File dir, boolean useFileHiding).
   Verify that it doesn't blow up if first arg is not an IFSJavaFile.
   **/
  public void Var012()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File dir = new File(ifsDirPath_);
      view.getFiles(dir, true);
      succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   getFiles(File dir, boolean useFileHiding).
   Verify that it returns valid IFSJavaFile objects.
   **/
  public void Var013()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      createDirectory(ifsDirPath_);
      createFile(ifsFilePath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsDirPath_);
      File[] files = view.getFiles(dir, true);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, ifsFilePath_);
      assertCondition(files.length == 1 &&
                      files[0].equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteFile(ifsFilePath_);
      deleteDirectory(ifsDirPath_);
    }
  }

  /**
   getFiles(File dir, boolean useFileHiding).
   Verify that it returns empty array when dir contains no files.
   **/
  public void Var014()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      createDirectory(ifsDirPath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsDirPath_);
      File[] files = view.getFiles(dir, true);
      assertCondition(files.length == 0);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteDirectory(ifsDirPath_);
    }
  }

  /**
   getFiles(File dir, boolean useFileHiding).
   Verify that it returns empty array when dir doesn't exist.
   **/
  public void Var015()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      try { deleteDirectory(ifsDirPath_); } catch (Exception e) {}
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsDirPath_);
      File[] files = view.getFiles(dir, true);
      assertCondition(files.length == 0);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



  /**
   getHomeDirectory().
   Verify that it returns an IFSJavaFile representing the "/" (root) directory.
   **/
  public void Var016()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File dir = view.getHomeDirectory();
      IFSJavaFile expected = new IFSJavaFile(systemObject_, IFSJavaFile.separator);
      assertCondition(dir.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



  /**
   getParentDirectory(File dir).
   Verify that it returns null if arg is null.
   **/
  public void Var017()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File dir = view.getParentDirectory((IFSJavaFile)null);
      assertCondition(dir == null);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   getParentDirectory(File dir).
   Verify that it doesn't blow up if arg is not an IFSJavaFile.
   **/
  public void Var018()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File dir = new File(ifsDirPath_);
      view.getParentDirectory(dir);
      succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   getParentDirectory(File dir).
   Verify that it returns a valid IFSJavaFile object when arg represents a directory.
   **/
  public void Var019()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      createDirectory(ifsDirPath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsDirPath_);
      File parent = view.getParentDirectory(dir);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, IFSJavaFile.separator);
      assertCondition(parent.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteDirectory(ifsDirPath_);
    }
  }

  /**
   getParentDirectory(File dir).
   Verify that it returns a valid IFSJavaFile object when arg represents a file.
   **/
  public void Var020()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      createDirectory(ifsDirPath_);
      createFile(ifsFilePath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, ifsFilePath_);
      File parent = view.getParentDirectory(dir);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, ifsDirPath_);
      assertCondition(parent.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteFile(ifsFilePath_);
      deleteDirectory(ifsDirPath_);
    }
  }

  /**
   getParentDirectory(File dir).
   Verify that it returns null when the arg is "/" directory.
   **/
  public void Var021()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, IFSJavaFile.separator);
      File parent = view.getParentDirectory(dir);
      assertCondition(parent == null);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   getParentDirectory(File dir).
   Verify that it returns null when the arg is "/" directory and is a java.io.File.
   **/
  public void Var022()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File dir = new File(IFSJavaFile.separator);
      File parent = view.getParentDirectory(dir);
      assertCondition(parent == null);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   getParentDirectory(File dir).
   Verify that it works OK when the arg specifies a nonexistent directory.
   **/
  public void Var023()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile dir = new IFSJavaFile(systemObject_, IFSJavaFile.separator+"Bogus999");
      File parent = view.getParentDirectory(dir);
      IFSJavaFile expected = new IFSJavaFile(systemObject_, IFSJavaFile.separator);
      assertCondition(parent.equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



  /**
   getRoots().
   Verify that it returns the "/" (root) directory.
   **/
  public void Var024()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }

    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File[] roots = view.getRoots();
      IFSJavaFile expected = new IFSJavaFile(systemObject_, IFSJavaFile.separator);
      assertCondition(roots.length == 1 &&
                      roots[0].equals(expected));
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



  /**
   isHiddenFile(File file).
   Verify that it throws NullPointerException if arg is null.
   **/
  public void Var025()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      view.isHiddenFile((IFSJavaFile)null);
      failed("Exception didn't occur");
    }
    catch(Exception e) {
      assertExceptionIs(e, "NullPointerException"/*, "directory"*/);
    }
  }

  /**
   isHiddenFile(File file).
   Verify that it doesn't throw exception when arg is not an IFSJavaFile.
   **/
  public void Var026()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File file = new File(ifsFilePath_);
      view.isHiddenFile(file);
      succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   isHiddenFile(File file).
   Verify that it returns false if arg specifies a nonexistent IFSJavaFile.
   **/
  public void Var027()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile file = new IFSJavaFile(systemObject_, IFSJavaFile.separator+"Bogus999");
      boolean result = view.isHiddenFile(file);
      assertCondition(result == false);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   isHiddenFile(File file).
   Verify that it reports correct status when arg is an IFSJavaFile.
   **/
  public void Var028()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      createDirectory(ifsDirPath_);
      createFile(ifsFilePath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile file = new IFSJavaFile(systemObject_, ifsFilePath_);
      boolean result = view.isHiddenFile(file);
      assertCondition(result == file.isHidden());
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteFile(ifsFilePath_);
      deleteDirectory(ifsDirPath_);
    }
  }



  /**
   isRoot(File file).
   Verify that it returns false if arg is null.
   **/
  public void Var029()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      boolean result = view.isRoot((IFSJavaFile)null);
      assertCondition(result == false);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   isRoot(File file).
   Verify that it doesn't throw exception when arg is not an IFSJavaFile.
   **/
  public void Var030()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      IFSSystemView view = new IFSSystemView(systemObject_);
      File file = new File(File.separator);
      boolean result = view.isRoot(file);
      if (DOS_) succeeded();  // the isRoot() fails because 'file' is not absolute
      else assertCondition(result == true);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

  /**
   isRoot(File file).
   Verify that it reports correct status when arg is an IFSJavaFile.
   **/
  public void Var031()
  {
      if (isClassic) {
	  notApplicable("Non Classic test");
	  return; 
      }
    try {
      createDirectory(ifsDirPath_);
      createFile(ifsFilePath_);
      IFSSystemView view = new IFSSystemView(systemObject_);
      IFSJavaFile file1 = new IFSJavaFile(systemObject_, ifsFilePath_);
      boolean result1 = view.isRoot(file1);
      IFSJavaFile file2 = new IFSJavaFile(systemObject_, IFSJavaFile.separator);
      boolean result2 = view.isRoot(file2);
      assertCondition(result1 == false && result2 == true);
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
    finally {
      deleteFile(ifsFilePath_);
      deleteDirectory(ifsDirPath_);
    }
  }


/**
Interactively test the IFSSystemView class.
**/
  public void Var032(int runMode)
  {
	  notApplicable("Attended testcase"); 
  }


/**
Interactively test the IFSSystemView class.
Call JFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
**/
  public void Var033(int runMode)
  {
	  notApplicable("Attended testcase"); 
  }





  private class TestFilter extends javax.swing.filechooser.FileFilter
  {
    private String description_;
    private String pattern_;

    /**
     Constructs a FileFilter object.
     **/
    public TestFilter()
    {
      super();
    }

    /**
     Constructs a FileFilter object.
     **/
    public TestFilter(String description, String pattern)
    {
      super();
      description_ = description;
      pattern_ = pattern;
    }

    public boolean accept(File f)
    {
      if (f.getName().endsWith(pattern_)) return true;
      else return false;
    }
    /**
     Returns the description.
     @return The description.
     **/
    public String getDescription()
    {
      return description_;
    }

    /**
     Returns the pattern.
     @return The pattern.
     **/
    public String getPattern()
    {
      return pattern_;
    }

    /**
     Sets the description.
     @param description The description.
     **/
    public void setDescription(String description)
    {
      description_ = description;
    }

    /**
     Sets the pattern.
     @param pattern The pattern.
     **/
    public void setPattern(String pattern)
    {
      pattern_ = pattern;
    }

  }


}
