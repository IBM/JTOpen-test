///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSIFSTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.*;

import java.util.Vector;
import java.util.Random;
import com.ibm.as400.access.*;

import test.JTOpenTestEnvironment;
import test.Testcase;


/**
 *Testcase NLSIFSTestcase.  This test class verifies the use of DBCS Strings
 *in selected IFS testcase variations.
**/
public class NLSIFSTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSIFSTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  boolean failed;  // Keeps track of failure in multi-part tests.
  String msg;      // Keeps track of reason for failure in multi-part tests.
  CommandCall cmd = null;

  private static String ifsDirName_;
  
  private String operatingSystem_;
  private boolean DOS_;

  String ifs_dbcs_path = getResource("IFS_DBCS_PATH");
  String ifs_dbcs_dir = getResource("IFS_DBCS_DIR");
  String ifs_dbcs_file = getResource("IFS_DBCS_NAME");
  String ifs_dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String ifs_dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String ifs_dbcs_string50 = getResource("IFS_DBCS_STRING50");


  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public NLSIFSTestcase(AS400            systemObject,
                      Vector<String>          variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSIFSTestcase", 35,
          variationsToRun, runMode, fileOutputStream);

  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {

    // Determine operating system we're running under
    operatingSystem_ = System.getProperty("os.name");
    if (JTOpenTestEnvironment.isWindows)
    {
      DOS_ = true;
    }
    else
    {
      DOS_ = false;
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("DOS-based file structure: " + DOS_);

    ifsDirName_ = IFSFile.separator;

    // Check to see if we're connected properly
    try
    {
        IFSFile file = new IFSFile(systemObject_, ifsDirName_, "QSYS.lib");
        if (!file.exists())
        {
          output_.println("Unable to locate QSYS.lib in " + ifsDirName_ + " on " + systemObject_.getSystemName() + ".");
          throw new Exception("Unable to locate QSYS.lib");
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }



  void createDirectory(String dirName)
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, dirName);
      if (!dir.exists())
      {
        dir.mkdir();
        if (!dir.exists())
          System.out.println("Failure creating '" + dir.getPath() + "'");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
    }
  }

  void createFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, pathName, "rw");
      raf.close();
      if (!file.exists())
        System.out.println("The file '" + pathName + "' could not be " + 
                           "created.");
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
    }
  }

  void createFile(String pathName, String data)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, pathName, "rw");
      raf.writeChars(data);
      raf.close();
      if (!file.exists())
        System.out.println("The file '" + pathName + "' could not be " +
                           "created.");
    }
    catch(Exception e)
    {
    }
  }

  void createFile(String pathName, byte[] data)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, pathName, "rw");
      raf.write(data);
      raf.close();
      if (!file.exists())
        System.out.println("The file '" + pathName + "' could not be " +
                           "created.");
    }
    catch(Exception e)
    {
    }
  }

  void deleteFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
      if (file.exists())
        System.out.println("Unable to delete '" + file.getPath() +
                           "'.");
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
    }
  }

  class Collector implements IFSFileFilter
  {
    public Vector<IFSFile> entries = new Vector<IFSFile>();

    public boolean accept(IFSFile file)
    {
      entries.addElement(file);
      return false;
    }
  }
  
  @SuppressWarnings("rawtypes")
  boolean deleteDirectory(String pathName)
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, pathName);

      // Attempt to delete all the non-directories in this direcotry.
      IFSFile d = new IFSFile(systemObject_, pathName, "*");
      d.delete();
      d = new IFSFile(systemObject_, pathName, "*.*");
      d.delete();

      // Obtain a list of all entries left in this directory.
      Collector collector = new Collector();
      dir.list(collector);
      Vector entries = collector.entries;

      // Delete each directory entry.
      for (int i = 0; i < entries.size(); i++)
      {
        IFSFile entry = (IFSFile) entries.elementAt(i);
        if (entry.isDirectory())
        {
          // Delete this subdirectory.
          deleteDirectory(entry.getPath());
        }
        else
        {
          if (!entry.delete())
          {
            output_.println("Unable to delete '" + entry.getPath() + "'");
          }
        }
      }

      // Delete the specified directory.
      if (!dir.delete())
      {
        output_.println("Unable to delete '" + dir.getPath() + "'");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      return false;
    }

    return true;
  }

  String getUniqueName(String directory)
  {
    Random random = new Random();
    String name = null;
    try
    {
        do
        {
          name = directory + IFSFile.separator +
            Integer.toString(Math.abs(random.nextInt()));
        }
        while(new IFSFile(systemObject_, name).exists());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
    }

    return name;
  }


  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.FILE);
    }
    catch(Exception e)
    {
      output_.println("Unable to connect to the AS/400");
      e.printStackTrace();
      return;
    }

    try
    {
      setup();
    }
    catch (Exception e)
    {
      output_.println("Setup failed.");
      return;
    }

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }

    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }

    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }

    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }

    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }

    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }

    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }

    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }

    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }

    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }

    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }

    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }

    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }

    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }

    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }

    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }

    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }

    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }

    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }

    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }

    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }

    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }

    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }

    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }

    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }

    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }

    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }

    if ((allVariations || variationsToRun_.contains("35")) &&
        runMode_ != ATTENDED)
    {
      setVariation(35);
      Var035();
    }




    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.FILE);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }


/**
  IFSFile.mkdir() if a directory is created.

  <i>Taken from:</i> IFSCrtlDltTestcase::Var008
  **/
public void Var001()
  {
    createDirectory(ifs_dbcs_dir);
    String nonExistentDir = getUniqueName(ifs_dbcs_dir);
    try
    {
      IFSFile file = new IFSFile(systemObject_, nonExistentDir);
      if (!file.mkdir())
      {
        failed("Unable to create directory '" + file.getPath() + "'");
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    if (!deleteDirectory(ifs_dbcs_dir))
    {
      output_.println("Unable to delete '" + ifs_dbcs_dir + "'.\n");
    }
  }

/**
  IFSFile.mkdirs() if directories are created.

 <i>Taken from:</i> IFSCrtDltTestcase::Var010
  **/
public void Var002()
  {
    String dirName = ifs_dbcs_dir;
    for (int i = 0; i < 4; i++)
    {
      dirName = getUniqueName(dirName);
    }
    try
    {
      IFSFile file = new IFSFile(systemObject_, dirName);
      if (!file.mkdirs())
      {
        failed("Unable to create '" + file.getPath() + "'");
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    if (!deleteDirectory(ifs_dbcs_dir))
    {
      output_.println("Unable to delete '" + ifs_dbcs_dir + "'");
    }
  }


/**
 IFSFile.getName() returns the new name.

  <i>Taken from:</i> IFSCrtDltTestcase::Var012
  **/
public void Var003()
  {
    String curPath = ifs_dbcs_dir + "/" + ifs_dbcs_file;
    String newName = ifs_dbcs_string10;

    createDirectory(ifs_dbcs_dir);
    createFile(curPath);
    try
    {
      IFSFile newFile = new IFSFile(systemObject_, ifs_dbcs_dir, newName);
      IFSFile file = new IFSFile(systemObject_, curPath);
      IFSFile oldFile = new IFSFile(systemObject_, curPath);
      if (!file.renameTo(newFile))
        failed("Failure renaming file.");
      else if (!file.getName().equals(newName))
        failed("Incorrect filename: '" + file.getName() + "' != '" + newName +
               ".\n");
      else if (oldFile.exists())
        failed("'" + oldFile.getPath() + "' still exists.\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }

    deleteDirectory(ifs_dbcs_dir);
  }



/**
  Test a directory of '/ifs_dbcs_dir' and file of 'ifs_dbcs_file' using
  IFSFile(AS400, IFSFile, String).

  <i>Taken from:</i> IFSCtorTestcase::Var004
  **/
public void Var004()
  {
    try
    {
      IFSFile dir = new IFSFile(systemObject_, ifs_dbcs_dir);
      IFSFile file = new IFSFile(systemObject_, dir, ifs_dbcs_file);
      String absPath = file.getAbsolutePath();
      if (!absPath.equals("/"+ifs_dbcs_dir+"/"+ifs_dbcs_file))
        failed("Mismatched absolute paths: "+absPath+" !=       "+"/"+ifs_dbcs_dir+"/"+ifs_dbcs_file+".\n");
      else if (!absPath.equals(file.getPath()))
        failed("Mismatched paths: "+absPath+" != "+file.getPath()+".\n");
      else if (!file.getName().equals(ifs_dbcs_file))
        failed("Mismatched filenames: "+file.getName()+" != "+ifs_dbcs_file+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  Test IFSFile(AS400, String) with a path of '/ifs_dbcs_dir'.

  <i>Taken from:</i> IFSCtorTestcase::Var010
  **/
public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_dir);
      String absPath = file.getAbsolutePath();
      if (!absPath.equals("/"+ifs_dbcs_dir))
        failMsg.append("Mismatched directories: "+absPath+" != "+ifs_dbcs_dir+".\n");
      if (!absPath.equals(file.getPath()))
        failMsg.append("Mismatched paths: "+absPath+" != "+file.getPath()+".\n");
      if (!file.getName().equals(ifs_dbcs_dir))
        failMsg.append("Mismatched names: "+file.getName()+" != "+ifs_dbcs_dir+".\n");
    }
    catch(Exception e)
    {
      failed(e);
    }

    if (failMsg.toString().length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


/**
  Test IFSFile(AS400, String, String) with a directory of '/ifs_dbcs_dir' and
  file of 'ifs_dbcs_file'.

  <i>Taken from:</i> IFSCtorTestcase::Var016
  **/
public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_dir, ifs_dbcs_file);
      String absPath = file.getAbsolutePath();
      if (!absPath.equals("/"+ifs_dbcs_dir+"/"+ifs_dbcs_file))
        failMsg.append("Mismatched absolute paths: "+absPath+" != "+"/"+ifs_dbcs_dir+"/"+ifs_dbcs_file+".\n");
      if (!absPath.equals(file.getPath()))
        failMsg.append("Mismatched paths: "+absPath+" != "+file.getPath()+".\n");
      if (!file.getName().equals(ifs_dbcs_file))
        failMsg.append("Mismatched filenames: "+file.getName()+" != "+ifs_dbcs_file+".\n");
    }
    catch(Exception e)
    {
      failed(e);
    }

    if (failMsg.toString().length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


/**
  Test IFSFileInputStream(AS400, String) with existing file "/ifs_dbcs_dir/ifs_dbcs_file".

  <i>Taken from:</i> IFSCtorTestcase::Var022
  **/
public void Var007()
  {
    createDirectory(ifs_dbcs_dir);
    String curPath = ifs_dbcs_dir + "/" + ifs_dbcs_file;
    createFile(curPath);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, curPath);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }

    if (!deleteDirectory(ifs_dbcs_dir))
    {
      output_.println("Unable to delete '" + ifs_dbcs_dir + "'");
    }
  }


/**
  Test IFSTextFileInputStream(AS400, String) with existing file "/ifs_dbcs_dir/ifs_dbcs_file".
  **/
public void Var008()
  {
    createDirectory(ifs_dbcs_dir);
    String curPath = ifs_dbcs_dir + "/" + ifs_dbcs_file;
    createFile(curPath);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, curPath);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }

    if (!deleteDirectory(ifs_dbcs_dir))
    {
      output_.println("Unable to delete '" + ifs_dbcs_dir + "'");
    }
  }


/**
  Test IFSFileInputStream(AS400, String, int) with existing file
  "/ifs_dbcs_dir/ifs_dbcs_file".
  **/
public void Var009()
  {
    createDirectory(ifs_dbcs_dir);
    String curPath = ifs_dbcs_dir + "/" + ifs_dbcs_file;
    createFile(curPath);
    try
    {
      IFSFileInputStream is =
        new IFSFileInputStream(systemObject_, curPath,
                               IFSFileInputStream.SHARE_ALL);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifs_dbcs_path);
  }


/**
  Test IFSTextFileInputStream(AS400, String, int) with existing file
  "/ifs_dbcs_dir/ifs_dbcs_file".

  <i>Taken from:</i> IFSCtorTestcase::Var030
  **/
public void Var010()
  {
    createDirectory(ifs_dbcs_dir);
    String curPath = ifs_dbcs_dir + "/" + ifs_dbcs_file;
    createFile(curPath);
    try
    {
      IFSTextFileInputStream is =
        new IFSTextFileInputStream(systemObject_, curPath,
                               IFSTextFileInputStream.SHARE_ALL);
      is.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifs_dbcs_path);
  }


  /**
  Ensure that IFSFileOutputStream(AS400, String) creates a file if it doesn't exist.

  <i>Taken from:</i> IFSCtorTestcase::Var054
  **/

public void Var011()
  {
    deleteFile(ifs_dbcs_file);
    try
    {
      IFSFileOutputStream os =
        new IFSFileOutputStream(systemObject_, ifs_dbcs_file);
      os.close();
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_file);
      assertCondition(file.exists());
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifs_dbcs_file);
  }


  /**
  Ensure that IFSTextFileOutputStream(AS400, String) creates a file if it doesn't exist.
  **/

public void Var012()
  {
    deleteFile(ifs_dbcs_file);
    try
    {
      IFSTextFileOutputStream os =
        new IFSTextFileOutputStream(systemObject_, ifs_dbcs_file);
      os.close();
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_file);
      assertCondition(file.exists());
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifs_dbcs_file);
  }



/**
  Ensure that a file is created by IFSRandomAccessFile(AS400, String, String).

  <i>Taken from:</i> IFSCtorTestcase::Var086
  **/
public void Var013()
  {
    deleteFile(ifs_dbcs_file);
    IFSRandomAccessFile raf = null;
    try
    {
      raf = new IFSRandomAccessFile(systemObject_, ifs_dbcs_file, "w");
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_file);
      if (!file.exists())
      {
        failed("File wasn't created.");
      }
      else
        succeeded();
      raf.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifs_dbcs_file);
  }


/**
  Ensure that all valid values for arguments are accepted using
  IFSRandomAccessFile(AS400, String, String, int, int0).

  <i>Taken from:</i> IFSCtorTestcase::Var093
  **/
public void Var014()
  {
    createFile(ifs_dbcs_file);
    try
    {
      IFSRandomAccessFile raf1 =
        new IFSRandomAccessFile(systemObject_, ifs_dbcs_file, "r",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      IFSRandomAccessFile raf2 =
        new IFSRandomAccessFile(systemObject_, ifs_dbcs_file, "w",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      IFSRandomAccessFile raf3 =
        new IFSRandomAccessFile(systemObject_, ifs_dbcs_file, "rw",
                                IFSRandomAccessFile.SHARE_ALL,
                                IFSRandomAccessFile.OPEN_OR_CREATE);
      raf1.close();
      raf2.close();
      raf3.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifs_dbcs_file);
  }


/**
  Ensure that IFSFileInputStream.available() equals the file length (for empty
  files and non-empty files).

  <i>Taken from:</i> IFSFileAttrTestcase::Var04
  **/
public void Var015()
  {
    createDirectory(ifs_dbcs_dir);
    String emptyName = ifs_dbcs_dir + "Empty";
    createFile(emptyName);
    String nonEmptyName = ifs_dbcs_dir + "NonEmpty";
    createFile(nonEmptyName, ifs_dbcs_string50);
    try
    {
      IFSFileInputStream emptyStream =
        new IFSFileInputStream(systemObject_, emptyName);
      IFSFileInputStream nonEmptyStream =
        new IFSFileInputStream(systemObject_, nonEmptyName);
      assertCondition(emptyStream.available() == 0 &&
             nonEmptyStream.available() == ifs_dbcs_string50.length() * 2);
      emptyStream.close();
      nonEmptyStream.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteDirectory(ifs_dbcs_dir);
  }


/**
  IFSFile.getName().

  <i>Taken from:</i> IFSMiscTestcase::Var018
  **/
public void Var016()
  {
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, ifs_dbcs_path);
      IFSFile file2 = new IFSFile(systemObject_, ifs_dbcs_file);
      assertCondition(file1.getName().equals(ifs_dbcs_file) &&
             file2.getName().equals(ifs_dbcs_file));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  getParent().

  <i>Taken from:</i> IFSMiscTestcase::Var019
  **/
public void Var017()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_path);
      if (file.getParent().equals(ifs_dbcs_dir))
        failed("Mismatched parent dirs: "+file.getParent()+" != "+ifs_dbcs_dir+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }



/**
  getPath().
  <i>Taken from:</i> IFSMiscTestcase::Var020
  **/
public void Var018()
   {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifs_dbcs_path);
      if (!file.getPath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getPath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
   Ensure that IFSFile.getAbsolutePath() returns the path specified by
   IFSFile.setPath(String).

  <i>Taken from:</i> IFSPropertyTestcase::Var013
  **/
public void Var019()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifs_dbcs_path);
      if (!file.getAbsolutePath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getAbsolutePath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  Ensure that IFSFile.getCanonicalPath() returns the path specified by
  IFSFile.setPath(String).

  <i>Taken from:</i> IFSPropertyTestcase::Var014
  **/
public void Var020()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifs_dbcs_path);
      if (!file.getCanonicalPath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getCanonicalPath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  Ensure that IFSFile.getName() returns the name part of the path specified by
   IFSFile.setPath(String).

  <i>Taken from:</i> IFSPropertyTestcase::Var016
  **/
public void Var021()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifs_dbcs_path);
      if (!file.getName().equals(ifs_dbcs_file))
        failed("Incorrect filename: "+file.getName()+" != "+ifs_dbcs_file+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  Ensure that IFSFile.getParent() returns the parent part of the path
   specified by IFSFile.setPath(String).
  <i>Taken from:</i> IFSPropertyTestcase::Var017
  **/
public void Var022()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifs_dbcs_dir+"/"+ifs_dbcs_file);
      if (!file.getParent().equals("/"+ifs_dbcs_dir))
        failed("Incorrect parent directory: "+file.getParent()+" != "+ifs_dbcs_dir+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  Ensure that IFSFile.getPath() returns the path specified by
   IFSFile.setPath(String).
  <i>Taken from:</i> IFSPropertyTestcase::Var018
  **/
public void Var023()
  {
    try
    {
      IFSFile file = new IFSFile();
      file.setPath(ifs_dbcs_path);
      if (!file.getPath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getPath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
 Ensure that IFSFileInputStream.getPath() returns the path specified by
   IFSFileInputStream.setPath(String).

  <i>Taken from:</i> IFSPropertyTestcase::Var049
  **/
public void Var024()
  {
  IFSFileInputStream file  = null; 
    try
    {
      file = new IFSFileInputStream();
      file.setPath(ifs_dbcs_path);
      if (!file.getPath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getPath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    } finally { 
    if (file != null)
      try {
        file.close();
      } catch (IOException e) {
      } 
    }
  }


/**
  Ensure that IFSFileOutputStream.getPath() returns the path specified by
   IFSFileOutputStream.setPath(String).

  <i>Taken from:</i> IFSPropertyTestcase::Var069
  **/
public void Var025()
  {
  IFSFileOutputStream file = null; 
    try
    {
      file = new IFSFileOutputStream();
      file.setPath(ifs_dbcs_path);
      if (!file.getPath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getPath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    } finally { 
      try {
        file.close();
      } catch (IOException e) {
      } 
    }
  }


/**
  Ensure that IFSRandomAccessFile.getPath() returns the path specified by
   IFSFileOutputStream.setPath(String).

  <i>Taken from:</i> IFSPropertyTestcase::Var089
  **/
public void Var026()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setPath(ifs_dbcs_path);
      if (!file.getPath().equals(ifs_dbcs_path))
        failed("Incorrect path: "+file.getPath()+" != "+ifs_dbcs_path+".\n");
      else
        succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
  Write a string containing a variety of unicode characters using
  IFSRandomAccessFile.writeBytes(String).

  <i>Taken from:</i> IFSWriteTestcase::Var022
  **/
public void Var027()
  {
    StringBuffer failMsg = new StringBuffer();
    String thePath = ifs_dbcs_dir+"/"+ifs_dbcs_file;
    createDirectory(ifs_dbcs_dir);
    createFile(thePath);
    try
    {
      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, thePath, "rw");
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
        new IFSRandomAccessFile(systemObject_, thePath, "r");
      byte[] data1 = new byte[s.length()];
      byte[] data2 = new byte[data1.length];
      for (int i = 0; i < data2.length; i++)
      {
        data2[i] = (byte) (s.charAt(i) & 0xff);
      }
      file2.read(data1);
      if (!areEqual(data1, data2))
        failMsg.append("Mismatched data: "+data1.toString()+" != "+data2.toString()+".\n");
      file2.close();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }
    if (!deleteDirectory(ifs_dbcs_dir))
      failMsg.append("Unable to delete "+ifs_dbcs_dir+".\n");

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


/**
  Verify NLS IFSFile directory and file operations in /QOpenSys.
  <ul>
  <li>Create directory/file
  <li>Delete directory/file
  <li>Rename directory/file
  <li>List contents of directory and subdirectory
  <li>Wildcard operations on files in directory
  </ul>
  **/
public void Var028()
  {
    StringBuffer failMsg = new StringBuffer();

    // setup
    String rootDir = "/QOpenSys/NLSTEST";
    createDirectory(rootDir);

    String thePath = rootDir + "/" + ifs_dbcs_dir;
    String renPath = rootDir + "/NLSREN1";

    // Directory operations
    try
    {
      IFSFile dir = new IFSFile(systemObject_, thePath);
      IFSFile oldDir = new IFSFile(systemObject_, thePath);
      IFSFile newDir = new IFSFile(systemObject_, renPath);

      if (!dir.mkdir())
        failMsg.append("Directory not created.\n");
      if (!dir.renameTo(newDir))
        failMsg.append("Directory not renamed.\n");

      if (!dir.delete())
        failMsg.append("Directory deletion failed.\n");
      if (oldDir.exists())
        failMsg.append("Directory still exists: " + oldDir.getAbsolutePath()+".\n");
      if (newDir.exists())
        failMsg.append("Directory still exists: " + newDir.getAbsolutePath()+".\n");
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception on directory operation.\n");
    }

    // File operations
    if (failMsg.length() == 0)
    {
      createDirectory(thePath);
      try
      {
        IFSFile file = new IFSFile(systemObject_, thePath, ifs_dbcs_file);
        IFSFile oldFile = new IFSFile(systemObject_, thePath, ifs_dbcs_file);
        file.mkdir();
        IFSFile newFile = new IFSFile(systemObject_, thePath, "Renamed");
        if (!file.renameTo(newFile))
          failMsg.append("File rename failed.\n");
        if (!file.delete())
          failMsg.append("File deletion failed.\n");
        if (oldFile.exists())
          failMsg.append("File still exists: " + oldFile.getAbsolutePath()+".\n");
        if (newFile.exists())
          failMsg.append("File still exists: " + newFile.getAbsolutePath()+".\n");
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception on file operation.\n");
      }
    }

    // List operations
    if (failMsg.length() == 0)
    {
      deleteDirectory(thePath);
      try
      {
        String subPath = thePath+"/SubDir";
        IFSFile dir = new IFSFile(systemObject_, thePath);
        IFSFile file = new IFSFile(systemObject_, thePath, ifs_dbcs_file);
        IFSFile file2 = new IFSFile(systemObject_, thePath, "1"+ifs_dbcs_file);
        IFSFile subDir = new IFSFile(systemObject_, subPath);
        IFSFile subFile = new IFSFile(systemObject_, subPath, "SubFile");
        dir.mkdir();
        file.mkdir();
        file2.mkdir();
        subDir.mkdir();
        subFile.mkdir();
        String[] compList = new String[3];
        compList[0] = file.getName();
        compList[1] = file2.getName();
        compList[2] = subDir.getName();
        String dirList[] = dir.list();
        int i = 0;
        for (; dirList != null && compList.length == dirList.length &&
          i < dirList.length; i++)
        {
          int j = 0;
          for (; j < compList.length; j++)
          {
            if (dirList[i].equals(compList[j]))
            {
              break;
            }
          }
          if (j == compList.length)
          {
            break;
          }
        }
        if (i < compList.length)
        {
          failMsg.append("Mismatched directory listing:\n");
        }

        compList = new String[1];
        compList[0] = subFile.getName();
        dirList = subDir.list();
        for (; dirList != null && compList.length == dirList.length &&
          i < dirList.length; i++)
        {
          int j = 0;
          for (; j < compList.length; j++)
          {
            if (dirList[i].equals(compList[j]))
            {
              break;
            }
          }
          if (j == compList.length)
          {
            break;
          }
        }
        if (i < compList.length)
        {
          failMsg.append("Mismatched directory listing:\n");
        }

        // Wildcards
        dirList = dir.list("1*");
        if (dirList == null || !dirList[0].equals("1"+ifs_dbcs_file) ||
            dirList.length != 1)
        {
          failMsg.append("Incorrect wildcard * directory listing: ");
          failMsg.append("1"+ifs_dbcs_file+" != "+dirList.toString()+".\n");
          failMsg.append(dirList==null ? 0 : dirList.length);
        }

        dirList = subDir.list("S?bFile");
        if (dirList == null || !dirList[0].equals("SubFile") ||
            dirList.length != 1)
        {
          failMsg.append("Incorrect wildcard ? directory listing: ");
          failMsg.append("SubFile != "+dirList.toString()+".\n");
          failMsg.append(dirList==null ? 0 : dirList.length);
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception on list operation.\n");
      }
    }


    // Cleanup
    deleteDirectory(rootDir);

    if (failMsg.length() != 0)
    {
      failed("QOpenSys operation(s) failed.\n"+failMsg.toString());
    }
    else
    {
      succeeded();
    }
  }


/**
  Verify NLS IFSFile directory and file operations in /.
  <ul>
  <li>Create directory/file
  <li>Delete directory/file
  <li>Rename directory/file
  <li>List contents of directory and subdirectory
  <li>Wildcard operations on files in directory
  </ul>
  **/
public void Var029()
  {
    StringBuffer failMsg = new StringBuffer();

    // setup
    String rootDir = "/NLSTEST";
    createDirectory(rootDir);

    String thePath = rootDir + "/" + ifs_dbcs_dir;
    String renPath = rootDir + "/NLSREN1";

    // Directory operations
    try
    {
      IFSFile dir = new IFSFile(systemObject_, thePath);
      IFSFile oldDir = new IFSFile(systemObject_, thePath);
      IFSFile newDir = new IFSFile(systemObject_, renPath);

      if (!dir.mkdir())
        failMsg.append("Directory not created.\n");
      if (!dir.renameTo(newDir))
        failMsg.append("Directory not renamed.\n");

      if (!dir.delete())
        failMsg.append("Directory deletion failed.\n");
      if (oldDir.exists())
        failMsg.append("Directory still exists: " + oldDir.getAbsolutePath()+".\n");
      if (newDir.exists())
        failMsg.append("Directory still exists: " + newDir.getAbsolutePath()+".\n");
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception on directory operation.\n");
    }

    // File operations
    if (failMsg.length() == 0)
    {
      createDirectory(thePath);
      try
      {
        IFSFile file = new IFSFile(systemObject_, thePath, ifs_dbcs_file);
        IFSFile oldFile = new IFSFile(systemObject_, thePath, ifs_dbcs_file);
        file.mkdir();
        IFSFile newFile = new IFSFile(systemObject_, thePath, "Renamed");
        if (!file.renameTo(newFile))
          failMsg.append("File rename failed.\n");
        if (!file.delete())
          failMsg.append("File deletion failed.\n");
        if (oldFile.exists())
          failMsg.append("File still exists: " + oldFile.getAbsolutePath()+".\n");
        if (newFile.exists())
          failMsg.append("File still exists: " + newFile.getAbsolutePath()+".\n");
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception on file operation.\n");
      }
    }

    // List operations
    if (failMsg.length() == 0)
    {
      deleteDirectory(thePath);
      try
      {
        String subPath = thePath+"/SubDir";
        IFSFile dir = new IFSFile(systemObject_, thePath);
        IFSFile file = new IFSFile(systemObject_, thePath, ifs_dbcs_file);
        IFSFile file2 = new IFSFile(systemObject_, thePath, "1"+ifs_dbcs_file);
        IFSFile subDir = new IFSFile(systemObject_, subPath);
        IFSFile subFile = new IFSFile(systemObject_, subPath, "SubFile");
        dir.mkdir();
        file.mkdir();
        file2.mkdir();
        subDir.mkdir();
        subFile.mkdir();
        String[] compList = new String[3];
        compList[0] = file.getName();
        compList[1] = file2.getName();
        compList[2] = subDir.getName();
        String dirList[] = dir.list();
        int i = 0;
        for (; dirList != null && compList.length == dirList.length &&
          i < dirList.length; i++)
        {
          int j = 0;
          for (; j < compList.length; j++)
          {
            if (dirList[i].equals(compList[j]))
            {
              break;
            }
          }
          if (j == compList.length)
          {
            break;
          }
        }
        if (i < compList.length)
        {
          failMsg.append("Mismatched directory listing:\n");
        }

        compList = new String[1];
        compList[0] = subFile.getName();
        dirList = subDir.list();
        for (; dirList != null && compList.length == dirList.length &&
          i < dirList.length; i++)
        {
          int j = 0;
          for (; j < compList.length; j++)
          {
            if (dirList[i].equals(compList[j]))
            {
              break;
            }
          }
          if (j == compList.length)
          {
            break;
          }
        }
        if (i < compList.length)
        {
          failMsg.append("Mismatched directory listing:\n");
        }

        // Wildcards
        dirList = dir.list("1*");
        if (dirList == null || !dirList[0].equals("1"+ifs_dbcs_file) ||
            dirList.length != 1)
        {
          failMsg.append("Incorrect wildcard * directory listing: ");
          failMsg.append("1"+ifs_dbcs_file+" != "+dirList.toString()+".\n");
          failMsg.append(dirList==null ? 0 : dirList.length);
        }

        dirList = subDir.list("S?bFile");
        if (dirList == null || !dirList[0].equals("SubFile") ||
            dirList.length != 1)
        {
          failMsg.append("Incorrect wildcard ? directory listing: ");
          failMsg.append("SubFile != "+dirList.toString()+".\n");
          failMsg.append(dirList==null ? 0 : dirList.length);
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception on list operation.\n");
      }
    }


    // Cleanup
    deleteDirectory(rootDir);

    if (failMsg.length() != 0)
    {
      failed("Root directory operation(s) failed.\n"+failMsg.toString());
    }
    else
    {
      succeeded();
    }
  }





/**
  Verify NLS bytes written to and read from a file
  using IFSFileOutputStream and IFSFileInputStream.
  **/
@SuppressWarnings("deprecation")
public void Var030()
  {
    StringBuffer failMsg = new StringBuffer();

    // setup
    String rootDir = "/NLSTEST";
    createDirectory(rootDir);
    String rootFile = rootDir + "/DBCSDATA";
    createFile(rootFile);

    byte[] dataOut = ifs_dbcs_file.getBytes();
    byte[] dataIn = null;
    int NLSLength = dataOut.length;

    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream(systemObject_, rootFile);
      IFSKey key = file.lock(NLSLength);
      file.write(dataOut, 0, NLSLength);
      file.unlock(key);
      file.close();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception on NLS write.");
    }

    if (failMsg.length() == 0)
    {
      try
      {
        IFSFileInputStream file = new IFSFileInputStream(systemObject_, rootFile);
        int curLength = file.available();
        if (curLength != NLSLength)
        {
          failMsg.append("Mismatched number of bytes in file: ");
          failMsg.append(curLength + " != " + NLSLength + ".\n");
        }
        IFSKey key = file.lock(curLength);
        dataIn = new byte[curLength];
        int bytesRead = file.read(dataIn, 0, curLength);
        if (bytesRead != curLength)
        {
          failMsg.append("Mismatched number of bytes read: ");
          failMsg.append(curLength+" in file, "+bytesRead+" actually read.\n");
        }
        file.unlock(key);
        file.close();
        boolean wrongBytes = false;
        for (int i=0; i<dataIn.length && i<dataOut.length; i++)
        {
          if (dataIn[i] != dataOut[i])
            wrongBytes = true;
        }
        if (wrongBytes==true)
        {
          failMsg.append("Incorrect data read from file:\n");
          failMsg.append("  Data written: "+dataOut.toString()+".\n");
          failMsg.append("  Data read   : "+dataIn.toString()+".\n");
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception on NLS read.");
      }
    }

    // Cleanup
    deleteFile(rootFile);
    deleteDirectory(rootDir);

    if (failMsg.length() != 0)
      failed("I/O of NLS bytes failed.\n"+failMsg.toString());
    else
      succeeded();
  }



/**
  Verify NLS bytes written to and read from a file
  using IFSTextFileOutputStream and IFSTextFileInputStream.
  **/
@SuppressWarnings("deprecation")
public void Var031()
  {
    StringBuffer failMsg = new StringBuffer();

    // setup
    String rootDir = "/NLSTEST";
    createDirectory(rootDir);
    String rootFile = rootDir + "/DBCSDATA";
    createFile(rootFile);

    byte[] dataOut = ifs_dbcs_file.getBytes();
    byte[] dataIn = null;
    int NLSLength = dataOut.length;

    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream(systemObject_, rootFile);
      IFSKey key = file.lock(NLSLength);
      file.write(dataOut, 0, NLSLength);
      file.unlock(key);
      file.close();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception on NLS write.");
    }

    if (failMsg.length() == 0)
    {
      try
      {
        IFSTextFileInputStream file = new IFSTextFileInputStream(systemObject_, rootFile);
        int curLength = file.available();
        if (curLength != NLSLength)
        {
          failMsg.append("Mismatched number of bytes in file: ");
          failMsg.append(curLength + " != " + NLSLength + ".\n");
        }
        IFSKey key = file.lock(curLength);
        dataIn = new byte[curLength];
        int bytesRead = file.read(dataIn, 0, curLength);
        if (bytesRead != curLength)
        {
          failMsg.append("Mismatched number of bytes read: ");
          failMsg.append(curLength+" in file, "+bytesRead+" actually read.\n");
        }
        file.unlock(key);
        file.close();
        boolean wrongBytes = false;
        for (int i=0; i<dataIn.length && i<dataOut.length; i++)
        {
          if (dataIn[i] != dataOut[i])
            wrongBytes = true;
        }
        if (wrongBytes==true)
        {
          failMsg.append("Incorrect data read from file:\n");
          failMsg.append("  Data written: "+dataOut.toString()+".\n");
          failMsg.append("  Data read   : "+dataIn.toString()+".\n");
        }
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception on NLS read.");
      }
    }

    // Cleanup
    deleteFile(rootFile);
    deleteDirectory(rootDir);

    if (failMsg.length() != 0)
      failed("I/O of NLS bytes failed.\n"+failMsg.toString());
    else
      succeeded();
  }


/**
  Verify NLS data written to and read from an IFSRandomAccessFile.
  **/
public void Var032()
  {
    StringBuffer failMsg = new StringBuffer();

    // setup
    String rootDir = "/NLSTEST";
    createDirectory(rootDir);
    String rootFile = rootDir + "/DBCSRAF";
    createFile(rootFile);
    String dbcs_string = ifs_dbcs_dir+"/"+ifs_dbcs_file;

    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile(systemObject_, rootFile, "w");
      file.writeChars(dbcs_string);
      file.flush();
      file.close();
      file = new IFSRandomAccessFile(systemObject_, rootFile, "r");
      long length = file.length()/2;
      StringBuffer read_string_buf = new StringBuffer();
      for (int j=0; j<length; j++)
        read_string_buf.append(file.readChar());
      file.close();
      String read_string = read_string_buf.toString();
      if (length != read_string.length())
        failMsg.append("Incorrect file length: "+length+" != "+read_string.length()+".\n");
      if (!read_string.equals(dbcs_string))
      {
        failMsg.append("Mismatched strings:\n");
        failMsg.append("  Data written: "+dbcs_string+".\n");
        failMsg.append("  Data read   : "+read_string+".\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // Cleanup
    deleteFile(rootFile);
    deleteDirectory(rootDir);

    if (failMsg.length() != 0)
      failed("I/O of NLS string failed.\n"+failMsg.toString());
    else
      succeeded();
  }


  /**
   Ensure that IFSTextFileInputStream.read() returns the String written by
   IFSTextFileOutputStream.write(String) using the CCSID of the user profile.
   Based on IFSReadTestcase variation91.
   **/
public void Var033()
   {
     String rootDir = "/NLSTEST";
     createDirectory(rootDir);
     String rootFile = rootDir + "/DBCSDATA";
     ///String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     String s = ifs_dbcs_string50 + ifs_dbcs_string10 + ifs_dbcs_string5;
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream();
       os.setSystem(systemObject_);
       os.setPath(rootFile);
       ///os.setCCSID(0x01b5);
       os.setCCSID(systemObject_.getCcsid());
       System.out.println("CCSID of AS/400 is: " + systemObject_.getCcsid());
       os.write(s);
       os.close();
       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, rootFile);
       assertCondition(is.read(s.length()).equals(s));
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }

     // Cleanup
     deleteFile(rootFile);
     deleteDirectory(rootDir);
   }


  /**
   Verify the correct function of the "append" option on the constructor
   for IFSFileOutputStream.
   **/
public void Var034()
   {
     notApplicable("Under construction");
     return;
/*
     String rootDir = "/NLSTEST";
     createDirectory(rootDir);
     String rootFile = rootDir + "/DBCSDATA";
     ///String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     String s = ifs_dbcs_string5 + ifs_dbcs_string50;
     try
     {
       IFSFileOutputStream os = new IFSFileOutputStream(systemObject_, rootFile,
                                        IFSFileOutputStream.SHARE_ALL,false);
       byte[] dbcs_bytes5 = new byte[5];
       new AS400Text (5, systemObject_.getCcsid(), systemObject_).toBytes( ifs_dbcs_string5, dbcs_bytes5, 0 );

       byte[] dbcs_bytes10 = new byte[10];
       new AS400Text (10, systemObject_.getCcsid(), systemObject_).toBytes( ifs_dbcs_string10, dbcs_bytes10, 0 );

       byte[] dbcs_bytes50 = new byte[50];
       new AS400Text (50, systemObject_.getCcsid(), systemObject_).toBytes( ifs_dbcs_string50, dbcs_bytes50, 0 );

       byte[] dbcs_bytes55 = new byte[55];
       new AS400Text (55, systemObject_.getCcsid(), systemObject_).toBytes( s, dbcs_bytes55, 0 );


       //os.setCCSID(systemObject_.getCcsid());
       ///os.write(ifs_dbcs_string10);
       os.write(dbcs_bytes10);
       os.close();
       // Overwrite what we just wrote.
       os = new IFSFileOutputStream(systemObject_, rootFile,
                                        IFSFileOutputStream.SHARE_ALL,false);
       //os.setCCSID(systemObject_.getCcsid());
       ///os.write(ifs_dbcs_string5);
       os.write(dbcs_bytes5);
       os.close();


       IFSFileInputStream is =
         new IFSFileInputStream(systemObject_, rootFile);
       int available1 = is.available();  // how many bytes are available on stream
       byte[] dataOut1 = new byte[available1];
       is.read(dataOut1);
       ///String got = is.read(s.length());
       is.close();
       ///String expected = ifs_dbcs_string5;
       if (!Testcase.isEqual(dataOut1, dbcs_bytes5))
       {
         System.out.println("After overwrite: Expected: \n" + dbcs_bytes5.toString() + "\nGot: \n" + dataOut1.toString());
       }
       String str1 = (String)(new AS400Text (available1, systemObject_.getCcsid(), systemObject_)).toObject( dbcs_bytes5 );
       System.out.println("Wrote string [" + str1 + "] to file: " + rootFile);
       //System.out.print("Press ENTER to continue");
       try { System.in.read(); } catch(Exception e) {}

       // Set up for appending the file.
       os = new IFSFileOutputStream(systemObject_,rootFile,
                                        IFSFileOutputStream.SHARE_ALL,true);
       //os.setCCSID(systemObject_.getCcsid());
       ///os.write(ifs_dbcs_string50); // append to existing file
       os.write(dbcs_bytes50); // append to existing file
       os.close();
       is =
         new IFSFileInputStream(systemObject_, rootFile);
       //assertCondition(is.read(s.length()).equals(ifs_dbcs_string5 + ifs_dbcs_string50));
       ///got = is.read(s.length());
       int available2 = is.available();  // how many bytes are available on stream
       byte[] dataOut2 = new byte[available2];
       is.read(dataOut2);
       is.close();
       ///expected = ifs_dbcs_string5 + ifs_dbcs_string50;
       if (Testcase.isEqual(dataOut2, dbcs_bytes55))
         succeeded();
       else
       {
         String exp = (String)(new AS400Text (available2, systemObject_.getCcsid(), systemObject_)).toObject( dbcs_bytes55 );
         String got = (String)(new AS400Text (available2, systemObject_.getCcsid(), systemObject_)).toObject( dataOut2 );
         System.out.println("Expected: \n" + exp + "\nGot: \n" + got);
         failed();
       }
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }

     // Cleanup
     deleteFile(rootFile);
     deleteDirectory(rootDir);
*/
   }


  /**
   Verify the correct function of the "append" option on the constructor
   for IFSTextFileOutputStream.
   **/
public void Var035()
   {
     notApplicable("Under construction");
     return;
/*
     String rootDir = "/NLSTEST";
     createDirectory(rootDir);
     String rootFile = rootDir + "/DBCSDATA";
     ///String s = "0123456789abcdefghijklmnopqrstuvwxyz)!@#$%^&*(-=_+[]{}|;':,./<>?";
     String s = ifs_dbcs_string50 + ifs_dbcs_string10 + ifs_dbcs_string5;
     try
     {
       IFSTextFileOutputStream os = new IFSTextFileOutputStream(systemObject_, rootFile,
                                        IFSTextFileOutputStream.SHARE_ALL,false);
       //os.setCCSID(systemObject_.getCcsid());
       os.write(ifs_dbcs_string10);
       os.close();
       // Overwrite what we just wrote.
       os = new IFSTextFileOutputStream(systemObject_, rootFile,
                                        IFSTextFileOutputStream.SHARE_ALL,false);
       //os.setCCSID(systemObject_.getCcsid());
       os.write(ifs_dbcs_string5);
       os.close();

       IFSTextFileInputStream is =
         new IFSTextFileInputStream(systemObject_, rootFile);
       String got = is.read(s.length());
       is.close();
       String expected = ifs_dbcs_string5;
       if (!got.equals(expected))
       {
         System.out.println("After overwrite: Expected: \n" + expected + "\nGot: \n" + got);
       }
       System.out.println("Wrote string [" + ifs_dbcs_string5 + "] to file: " + rootFile);
       //System.out.print("Press ENTER to continue");
       try { System.in.read(); } catch(Exception e) {}

       // Set up for appending the file.
       os = new IFSTextFileOutputStream(systemObject_,rootFile,
                                        IFSTextFileOutputStream.SHARE_ALL,true);
       //os.setCCSID(systemObject_.getCcsid());
       os.write(ifs_dbcs_string50); // append to existing file
       os.close();
       is =
         new IFSTextFileInputStream(systemObject_, rootFile);
       //assertCondition(is.read(s.length()).equals(ifs_dbcs_string5 + ifs_dbcs_string50));
       got = is.read(s.length());
       is.close();
       expected = ifs_dbcs_string5 + ifs_dbcs_string50;
       if (got.equals(expected))
         succeeded();
       else
       {
         System.out.println("Expected: \n" + expected + "\nGot: \n" + got);
         failed();
       }
       is.close();
     }
     catch(Exception e)
     {
       failed(e);
     }

     // Cleanup
     deleteFile(rootFile);
     deleteDirectory(rootDir);
*/
   }



}
