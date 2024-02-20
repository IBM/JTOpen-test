///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import test.JM.JMAddlFiles;
import test.JM.JMBeans;
import test.JM.JMCcsid;
import test.JM.JMComp;
import test.JM.JMExtract;
import test.JM.JMLang;
import test.JM.JMListen;
import test.JM.JMMakeJar;
import test.JM.JMPackage;
import test.JM.JMParseArgs;
import test.JM.JMSetGet;
import test.JM.JMSplit;
import test.JM.JMTBMakeJar;
import test.JM.JMTBParseArgs;

import java.beans.PropertyVetoException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.StringTokenizer;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;



/**
Test driver for the JarMaker class.
**/
public class JMTest
extends TestDriver
{

  // Constants.
  public static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));
  public static final File   CWD = CURRENT_DIR;
  public static final String LANGUAGE_DIR_NAME = "Lang";
  public static final File   LANGUAGE_DIR = new File (CWD, LANGUAGE_DIR_NAME);
  public static final String ADDLFILE_DIR_NAME = "AddlFile";
  public static final File   ADDLFILE_DIR = new File (CWD, ADDLFILE_DIR_NAME);

  public static final String JUNGLE_JAR_NAME = "jungle.jar";
  public static final File   JUNGLE_JAR = new File (CWD, JUNGLE_JAR_NAME);
  public static final File   JUNGLE_JAR_SMALL = new File (CWD, "jungleSmall.jar");
  public static final String JUNGLE_PACKAGE_NAME = "jungle";

  public static final String TOOLBOX_JAR_NAME = "toolbox.jar";
  public static final File   TOOLBOX_JAR = new File (CWD, TOOLBOX_JAR_NAME);
  public static final File   TOOLBOX_JAR_SMALL = new File (CWD, "toolboxSmall.jar");

  public static final String CLASS_PATH = System.getProperty ("java.class.path");
  public static final String FILE_SEPARATOR_STRING = System.getProperty ("file.separator");
  public static final char FILE_SEPARATOR = FILE_SEPARATOR_STRING.charAt(0);
  public static final String PATH_SEPARATOR = System.getProperty ("path.separator");

  public static final String MANIFEST_DIR_NAME   = "META-INF";
  public static final String MANIFEST_ENTRY_NAME = "META-INF/MANIFEST.MF";
  public static final String MANIFEST_NAME_KEYWORD  = "Name:";

  public static final String ADDITIONAL_FILE_1_NAME = "jungle.txt";
  public static final String ADDITIONAL_FILE_2_NAME = "spider2.gif";
  public static final String ADDITIONAL_FILE_3_NAME = "sunelm.jpg";
  public static final String ADDITIONAL_FILE_4_NAME = "dolphins.jpg";

  public static final File ADDITIONAL_FILE_1 = new File (CWD, ADDITIONAL_FILE_1_NAME);
  public static final File ADDITIONAL_FILE_2 = new File (CWD, ADDITIONAL_FILE_2_NAME);
  public static final File ADDITIONAL_FILE_3 = new File ("Trees", ADDITIONAL_FILE_3_NAME);
  public static final File ADDITIONAL_FILE_4 = new File (ADDLFILE_DIR, ADDITIONAL_FILE_4_NAME);

  public static final String COPYRIGHT_ENTRY_NAME = "com/ibm/as400/access/Copyright.class";
  public static final String PKG = "test/";
  public static final String MRI_TEMPLATE = "JMMRI.class";

  // Default size for splitting jars.
  private static final int SPLIT_SIZE_KBYTES = 2*1024; // kilobytes

  public static final boolean DEBUG = false;
  public static Vector allJungleFiles_ = null;
  public static Vector toolboxFiles_ = null;
  public static boolean foundJarMakerClassesOnClasspath_;


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main (String args[])
        throws Exception
    {
        runApplication (new JMTest (args));
        System.exit(0);
    }



/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public JMTest ()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public JMTest (String[] args)
        throws Exception
    {
        super (args);
    }



/**
Performs setup needed before running testcases.
 @exception  Exception  If an exception occurs.
**/
    public void setup ()
        throws Exception
    {
      if (!foundJarMakerClassesOnClasspath_) return;

      // Purge the various testing files and directories.
      deleteTestingFilesFromLocalDirectory ();
      // Copy the various jarmaker testing files to the current directory.
      extractTestingFilesToLocalDirectory();

     // Expected list of jungle jar entries, for comparison with actual results.
     allJungleFiles_ = new Vector ();
     allJungleFiles_.add (MANIFEST_DIR_NAME + "/");
     allJungleFiles_.add (MANIFEST_ENTRY_NAME);
     allJungleFiles_.add ("jungle/");
     allJungleFiles_.add ("jungle/Animal$AnimalThread.class");
     allJungleFiles_.add ("jungle/Animal.class");
     allJungleFiles_.add ("jungle/Animal.java");
     allJungleFiles_.add ("jungle/JungleMRI.properties");
     allJungleFiles_.add ("jungle/animal.jpg");
     allJungleFiles_.add ("jungle/tree.jpg");
     allJungleFiles_.add ("jungle/predator/");
     allJungleFiles_.add ("jungle/predator/Predator.class");
     allJungleFiles_.add ("jungle/predator/Predator.java");
     allJungleFiles_.add ("jungle/predator/puma.jpg");
     allJungleFiles_.add ("jungle/prey/");
     allJungleFiles_.add ("jungle/prey/Prey.class");
     allJungleFiles_.add ("jungle/prey/Prey.java");

     // Expected list of toolbox jar entries, for comparison with actual results.
     toolboxFiles_ = new Vector ();
     toolboxFiles_.add (MANIFEST_DIR_NAME + "/");
     toolboxFiles_.add (MANIFEST_ENTRY_NAME);
     toolboxFiles_.add ("com/");
     toolboxFiles_.add ("com/ibm/");
     toolboxFiles_.add ("com/ibm/as400/");
     toolboxFiles_.add ("com/ibm/as400/access/");
     toolboxFiles_.add ("com/ibm/as400/vaccess/");
     toolboxFiles_.add ("com/ibm/as400/access/CommandCall.class");
    }



/**
Performs setup needed after running testcases.
 @exception  Exception  If an exception occurs.
**/
    public void cleanup ()
        throws Exception
    {
      // Purge the various testing files and directories.
      deleteTestingFilesFromLocalDirectory ();
    }



  /**
   Adds directory entries to a list of jar entry names.
   If the list contains com/ibm/product/foo.class, this method will
   add the following entries (if they aren't already there)
     com/
     com/ibm/
     com/ibm/product/

   If addAllDirs==false, it will only add:
     com/ibm/product/

   @param oldList The list of jar entry names (String's).  Assumed to be sorted.
   @return The augmented list.
   **/
  public static Vector addDirectoryEntries (Vector oldList)
  {
    return addDirectoryEntries(oldList, true);
  }


  /**
   Adds directory entries to a list of jar entry names.
   For example, if the list contains com/ibm/product/foo.class,
   and addAllDirs==true, this method will
   add the following entries (if they aren't already there)
     com/
     com/ibm/
     com/ibm/product/

   If addAllDirs==false, it will only add:
     com/ibm/product/

   @param oldList The list of jar entry names (String's).  Assumed to be sorted.
   @return The augmented list.
   **/
  public static Vector addDirectoryEntries (Vector oldList, boolean addAllDirs)
  {
    String priorPrefix = new String ("");
    Vector augmentedList = new Vector (oldList.size ());
    Enumeration enumeration = oldList.elements ();
    while (enumeration.hasMoreElements ())
    {
      String listEntry = (String)enumeration.nextElement ();
      String prefix = listEntry.substring (0, listEntry.lastIndexOf ("/")+1);
      if (!prefix.equals (priorPrefix))
      {
        priorPrefix = prefix;
        // Add the parent directory if not already in list.
        if (addAllDirs)
        {
          int slashPos = prefix.indexOf ("/");
          while (slashPos != -1)
          {
            String subPrefix = prefix.substring (0, slashPos+1);
            if (!(augmentedList.contains (subPrefix)) &&
                !(subPrefix.equals (listEntry)))
              augmentedList.add (subPrefix);
            slashPos = prefix.indexOf ("/",slashPos+1);
          }
        }
        else
        {
          if (!(prefix.length() == 0) &&
              !(augmentedList.contains (prefix)) &&
              !(prefix.equals (listEntry)))
            augmentedList.add (prefix);
        }
      }
      augmentedList.add (listEntry);
    }
    return augmentedList;
  }


/**
Reports whether or not two files are in fact the same file.
**/
    public static boolean areSameFile (File file1, File file2)
    {
      return (file1.getAbsolutePath().equals (file2.getAbsolutePath()));
      // Note: For some reason, "File.equals()" reports false, even
      // when file1 and file2 are describing the same file.
    }

  public static boolean compareJars (File actualJar, File expectedJar)
  {
    boolean listsMatch = true;
    try
    {
      // Get list of entries in first jar.
      Vector actualEntries = listJarContents (actualJar);

      // Get list of entries in second jar.
      Vector expectedEntries = listJarContents (expectedJar);

      // Compare the lists.
      if (!compareLists (actualEntries, expectedEntries,
                         "actualEntries", "expectedEntries"))
        listsMatch = false;
      else
      {
        // Get list of entries in manifest of first jar.
        Vector actualManifest = listManifestContents (actualJar);

        // Get list of entries in manifest of second jar.
        Vector expectedManifest = listManifestContents (expectedJar);

        // Compare the lists.
        if (DEBUG) System.out.println ("Comparing manifests");
        if (!compareLists (actualManifest, expectedManifest,
                           "actualManifest", "expectedManifest"))
          listsMatch = false;
      }
    }
    catch (Exception e) {
      e.printStackTrace ();
      listsMatch = false;
    }

    return listsMatch;
  }

  public static boolean compareLists (Vector list1, Vector list2)
  { return compareLists (list1, list2, "list1", "list2"); }

  public static boolean compareLists (Vector list1, Vector list2,
                                      String list1Name, String list2Name)
  {
    Vector extras = new Vector ();
    Vector missing = new Vector ();
    boolean listsMatch = true;

    // Identify elements of list1 that aren't in list2.
    Enumeration e1 = list1.elements ();
    while (e1.hasMoreElements ())
    {
      Object element = e1.nextElement ();
      if (!list2.contains (element))
        extras.add (element);
    }

    // Identify elements of list2 that aren't in list1.
    Enumeration e2 = list2.elements ();
    while (e2.hasMoreElements ())
    {
      Object element = e2.nextElement ();
      if (!list1.contains (element))
        missing.add (element);
    }

    if (missing.size () > 0)
    {
      listsMatch = false;
      System.err.println ("Entries in " + list2Name +
                          " but not in " + list1Name + ":");
      for (Enumeration e3 = missing.elements ();
           e3.hasMoreElements ();
           System.err.println (e3.nextElement ()));
    }

    if (extras.size () > 0)
    {
      listsMatch = false;
      System.err.println ("Entries in " + list1Name +
                          " but not in " + list2Name + ":");
      for (Enumeration e3 = extras.elements ();
           e3.hasMoreElements ();
           System.err.println (e3.nextElement ()));
    }

    return listsMatch;
  }


  /**
   Converts a jar entry name to a file pathname.
   For example, in a Windows environment, converts
   "com/ibm/as400/access/CommandCall.class" or
   "com.ibm.as400.access.CommandCall.class" to
   "com\ibm\as400\access\CommandCall.class".

   @param baseDirectory The base directory for the file.
   @param entryName The jar entry name.
   **/
  static String convertToPath (File baseDirectory, String entryName)
  {
    if (baseDirectory == null) throw new NullPointerException ("baseDirectory");
    return convertToPath (baseDirectory.getAbsolutePath (), entryName);
  }


  /**
   Converts a jar entry name to a file pathname.

   @param basePath The absolute path of the base directory for the file.
   @param entryName The jar entry name.
   **/
  static String convertToPath (String basePath, String entryName)
  {
    if (basePath == null) throw new NullPointerException ("basePath");
    if (entryName == null) throw new NullPointerException ("entryName");
    StringBuffer path = new StringBuffer (basePath.trim());
    if (path.charAt (path.length ()-1) != FILE_SEPARATOR)
      path.append (FILE_SEPARATOR);

    // Replace all but the final occurrence of "." with fileSeparator.
    // Also replace all occurrences of "/" with fileSeparator.
    StringBuffer convertedName =
      new StringBuffer (entryName.trim().replace ('.',FILE_SEPARATOR).replace ('/',FILE_SEPARATOR));
    int finalDotIndex = entryName.lastIndexOf('.');
    if (finalDotIndex >= 0)
      convertedName.setCharAt (finalDotIndex,'.');

    path.append (convertedName);
    return path.toString();
  }


  // Uses the system classloader to copy a file (specified by resource name, e.g. "test/jungle.txt") to a destination.  If the resource resides in a jar or zip file, then it is extracted to the destination.
  private static boolean extractFile(String resourceName, File destination) throws FileNotFoundException, IOException
  {
    boolean succeeded = false;
    InputStream is = ClassLoader.getSystemResourceAsStream(resourceName);
    if (is == null) {
      System.out.println("Resource not found: |" + resourceName + "|");
    }
    else
    {
      BufferedOutputStream bos = null;
      try
      {
        File parent = destination.getParentFile();
        if (parent != null) parent.mkdirs();  // create parent directory(s) if needed
        bos = new BufferedOutputStream(new FileOutputStream(destination));
        byte[] buffer = new byte[1024];
        while (true)
        {
          int bytesRead = is.read(buffer);
          if (bytesRead == -1) break;
          bos.write(buffer, 0, bytesRead);
        }
        succeeded = true;
      }
      finally {
        if (is != null) try { is.close(); } catch (Throwable t) {}
        if (bos != null) try { bos.close(); } catch (Throwable t) {}
      }
    }
    return succeeded;
  }


  // Returns the pathname of the Toolbox jar (probably either jt400.jar or jt400Native.jar) that's found on the classpath.
  private static File locateToolboxJarFile()
  {
    String toolboxJarPath = null;
    String toolboxEntryName = "com/ibm/as400/access/AS400.class";
    URL toolboxUrl = null;
    try {
      toolboxUrl = Class.forName("com.ibm.as400.access.AS400").getClassLoader().getResource(toolboxEntryName);
    }
    catch (ClassNotFoundException e) {
      System.out.println("ERROR: Toolbox class 'AS400' not found by classloader.");
      e.printStackTrace();
    }
    if (toolboxUrl != null)
    {
      String toolboxPath = toolboxUrl.getPath();
      if (toolboxPath.length() <= toolboxEntryName.length()) {
        toolboxJarPath = "";  // it's in the local directory
      }
      else
      {
        toolboxJarPath = toolboxPath.substring(0, toolboxPath.length() - toolboxEntryName.length() - 1);
        if (toolboxJarPath.endsWith("!")) {
          toolboxJarPath = toolboxJarPath.substring(0, toolboxJarPath.length() - 1);
        }
        if (toolboxJarPath.startsWith("file:")) {
          toolboxJarPath = toolboxJarPath.substring(5);  // remove the "file:" prefix
        }
        toolboxJarPath = convertToLocalFilePathSyntax(toolboxJarPath);
      }
    }

    if (toolboxJarPath == null) return null;
    else {
      if (DEBUG) System.out.println("JMTest.locateToolboxJarFile(): Located Toolbox jar at location: " + toolboxJarPath);
      if (!toolboxJarPath.endsWith(".jar")) {
        System.out.println("WARNING: Class 'com.ibm.as400.access.AS400' was not found in a JAR file.  It was found under directory " + toolboxJarPath);
      }
      return new File(toolboxJarPath);
    }
  }


  // Converts the path (which contains jar-style separators) to a path with local file-system separators.
  private static String convertToLocalFilePathSyntax(String path)
  {
    if (path == null ||
        path.length() == 0 ||
        File.separatorChar == '/') {} // nothing to change
    else
    {
      path = path.replace('/', File.separatorChar);
      // For Windows: Strip leading separator char if it precedes a drive letter.
      if (path.length() > 2 &&
          path.charAt(0) == File.separatorChar &&
          path.charAt(2) == ':')
      {
        path = path.substring(1);  // strip leading separator
      }
    }
    return path;
  }


/**
Copies a file onto another file (replacing it if it exists).
 @exception  Exception  If an exception occurs.
**/
    static void copyFile (File sourceFile, File destinationFile)
        throws Exception, IOException
    {
      if (sourceFile == null) throw new NullPointerException ("sourceFile");
      if (destinationFile == null)
        throw new NullPointerException ("destinationFile");
      BufferedInputStream source = null;
      BufferedOutputStream destination = null;
      int bufSize = 2048;
      try
      {
        String parentDirName = destinationFile.getParent ();
        File outFileDir = new File (parentDirName);
        if (!outFileDir.exists () && !outFileDir.mkdirs ())
          throw new IOException ("Cannot create directory: " +
                                 outFileDir.getAbsolutePath ());
        source =
          new BufferedInputStream (new FileInputStream (sourceFile), bufSize);
        destination =
          new BufferedOutputStream (new FileOutputStream (destinationFile), bufSize);
        byte[] buffer = new byte[bufSize];
        boolean done = false;
        while (!done)
        {
          int bytesRead = source.read (buffer);
          if (bytesRead == -1)
          {
            done = true;
            destination.flush ();
          }
          else destination.write (buffer, 0, bytesRead);
        }
      }
      catch (Exception e) {
        System.out.println ("Exception in copyFile: " + e.getMessage ());
        e.printStackTrace ();
      }
      finally {
        if (source != null) try {source.close ();} catch (IOException e) {};
        if (destination != null) try {destination.close ();} catch (IOException e) {};
      }
    }


  public static void copyList (Vector newKids, Vector theBlock)
  { // Copy first list into second list.
    Enumeration e = newKids.elements ();
    while (e.hasMoreElements ())
      theBlock.add (e.nextElement ());
  }


/**
Extracts the various jarmaker testing files to the current directory.
This method is able to extract testing files directly from the jt400Test.jar file.
 @exception  Exception  If an exception occurs.
**/
    static void extractTestingFilesToLocalDirectory ()
        throws Exception
    {
      if (DEBUG)
        System.out.println ("Extracting JarMaker testing files to current directory.");

      File mri = new File(MRI_TEMPLATE);
      extractFile ("com/ibm/as400/access/MRI.class", mri);

      // Copy the various testing files to current directory.
      extractFile (PKG+JUNGLE_JAR_NAME,  JUNGLE_JAR);
      File originalToolboxJarFile = locateToolboxJarFile();
      if (originalToolboxJarFile != null) {
        copyFile(originalToolboxJarFile, TOOLBOX_JAR);
      }
      extractFile (PKG+ADDITIONAL_FILE_1_NAME,  ADDITIONAL_FILE_1);
      extractFile (PKG+ADDITIONAL_FILE_2_NAME,  ADDITIONAL_FILE_2);
      extractFile (PKG+ADDITIONAL_FILE_3_NAME,  ADDITIONAL_FILE_3);
      extractFile (PKG+ADDITIONAL_FILE_4_NAME,  ADDITIONAL_FILE_4);

      String s = System.getProperty ("file.separator");
      String accessDirName  = LANGUAGE_DIR_NAME+s+"com"+s+"ibm"+s+"as400"+s+"access";
      String dataDirName    = LANGUAGE_DIR_NAME+s+"com"+s+"ibm"+s+"as400"+s+"data";
      String resourceDirName= LANGUAGE_DIR_NAME+s+"com"+s+"ibm"+s+"as400"+s+"resource";
      String securityDirName= LANGUAGE_DIR_NAME+s+"com"+s+"ibm"+s+"as400"+s+"security";
      String vaccessDirName = LANGUAGE_DIR_NAME+s+"com"+s+"ibm"+s+"as400"+s+"vaccess";
      File accessDir   = new File (CURRENT_DIR, accessDirName);
      File dataDir     = new File (CURRENT_DIR, dataDirName);
      File resourceDir = new File (CURRENT_DIR, resourceDirName);
      File securityDir = new File (CURRENT_DIR, securityDirName);
      File vaccessDir  = new File (CURRENT_DIR, vaccessDirName);

      // English MRI's
      copyFile (mri, new File (accessDir,  "JDMRI.class"));
      copyFile (mri, new File (accessDir,  "JDMRI2.class"));
      copyFile (mri, new File (accessDir,  "MRI.class"));
      copyFile (mri, new File (accessDir,  "MRI2.class"));
      copyFile (mri, new File (accessDir,  "CoreMRI.class"));
      copyFile (mri, new File (accessDir,  "SVMRI.class"));
      copyFile (mri, new File (dataDir,    "DAMRI.class"));
      copyFile (mri, new File (resourceDir,"ResourceMRI.class"));
      copyFile (mri, new File (securityDir,"SecurityMRI.class"));
      copyFile (mri, new File (vaccessDir, "VMRI.class"));
      copyFile (mri, new File (vaccessDir, "VNPMRI.class"));
      copyFile (mri, new File (vaccessDir, "VQRYMRI.class"));

      // Canadian French MRI's (pretend)
      copyFile (mri, new File (accessDir,  "JDMRI_fr_CA.class"));
      copyFile (mri, new File (accessDir,  "JDMRI2_fr_CA.class"));
      copyFile (mri, new File (accessDir,  "MRI_fr_CA.class"));
      copyFile (mri, new File (accessDir,  "MRI2_fr_CA.class"));
      copyFile (mri, new File (accessDir,  "CoreMRI_fr_CA.class"));
      copyFile (mri, new File (accessDir,  "SVMRI_fr_CA.class"));
      copyFile (mri, new File (dataDir,    "DAMRI_fr_CA.class"));
      copyFile (mri, new File (resourceDir,"ResourceMRI_fr_CA.class"));
      copyFile (mri, new File (securityDir,"SecurityMRI_fr_CA.class"));
      copyFile (mri, new File (vaccessDir, "VMRI_fr_CA.class"));
      copyFile (mri, new File (vaccessDir, "VNPMRI_fr_CA.class"));
      copyFile (mri, new File (vaccessDir, "VQRYMRI_fr_CA.class"));

      // Taiwan Chinese MRI's (pretend)
      copyFile (mri, new File (accessDir,  "JDMRI_zh_TW.class"));
      copyFile (mri, new File (accessDir,  "JDMRI2_zh_TW.class"));
      copyFile (mri, new File (accessDir,  "MRI_zh_TW.class"));
      copyFile (mri, new File (accessDir,  "MRI2_zh_TW.class"));
      copyFile (mri, new File (accessDir,  "CoreMRI_zh_TW.class"));
      copyFile (mri, new File (accessDir,  "SVMRI_zh_TW.class"));
      copyFile (mri, new File (dataDir,    "DAMRI_zh_TW.class"));
      copyFile (mri, new File (resourceDir,"ResourceMRI_zh_TW.class"));
      copyFile (mri, new File (securityDir,"SecurityMRI_zh_TW.class"));
      copyFile (mri, new File (vaccessDir, "VMRI_zh_TW.class"));
      copyFile (mri, new File (vaccessDir, "VNPMRI_zh_TW.class"));
      copyFile (mri, new File (vaccessDir, "VQRYMRI_zh_TW.class"));

      // Japanese MRI's (pretend)
      copyFile (mri, new File (accessDir,  "JDMRI_ja.class"));
      copyFile (mri, new File (accessDir,  "JDMRI2_ja.class"));
      copyFile (mri, new File (accessDir,  "MRI_ja.class"));
      copyFile (mri, new File (accessDir,  "MRI2_ja.class"));
      copyFile (mri, new File (accessDir,  "CoreMRI_ja.class"));
      copyFile (mri, new File (accessDir,  "SVMRI_ja.class"));
      copyFile (mri, new File (dataDir,    "DAMRI_ja.class"));
      copyFile (mri, new File (resourceDir,"ResourceMRI_ja.class"));
      copyFile (mri, new File (securityDir,"SecurityMRI_ja.class"));
      copyFile (mri, new File (vaccessDir, "VMRI_ja.class"));
      copyFile (mri, new File (vaccessDir, "VNPMRI_ja.class"));
      copyFile (mri, new File (vaccessDir, "VQRYMRI_ja.class"));
    }

/**
Create a file.
**/
  public static boolean createFile (File file)
  {
    boolean succeeded = true;
    try
    {
      if (file.exists ())
        file.delete();
      RandomAccessFile raf = new RandomAccessFile (file, "rw");
      raf.close();
    }
    catch (Exception e) {
      succeeded = false;
      System.err.println (e.toString());
      e.printStackTrace ();
    }
    return succeeded;
  }


/**
Delete a file.
**/
    public static boolean deleteFile (File file)
    {
      if (file.exists ())
      {
        try { file.delete (); }
        catch (Exception e) {
          System.err.println ("Exception in deleteFile: " +
                              e.getMessage ());
          e.printStackTrace ();
        }
        if (file.exists ())
          System.err.println ("JMTest.deleteFile(): Failed to delete file " +
                              file.getAbsolutePath ());
      }
      return (!file.exists ());
    }
    public static boolean deleteFile (String fileName)
    {
      return deleteFile (new File (fileName));
    }

    // In the specified directory, deletes all files that have names
    // of the form <prefix>XXX<suffix>, where XXX is a integer number.
    public static boolean deleteSplitFiles (File directory, String prefix, String suffix)
    {
      boolean succeeded = true;
      String[] fileNames = directory.list ();  // all files in directory
      for (int i=0; i<fileNames.length; i++)
      {
        String fileName = fileNames[i];
        File thisFile = new File (fileName);
        if (thisFile.isFile () &&
            fileName.startsWith (prefix) &&
            fileName.endsWith (suffix))
        {
          int beginIndex = prefix.length ();
          int endIndex = fileName.length () - suffix.length ();
          String middlePart = fileName.substring (beginIndex, endIndex);
          if (DEBUG)
            System.out.println ("Middle part of " + fileName +
                                " is " + middlePart);
          boolean isNumber = true;
          try { Integer.parseInt (middlePart); }
          catch (NumberFormatException e) { isNumber = false; }
          if (isNumber) {
            if (DEBUG)
              System.out.println ("Deleting file " +
                                  thisFile.getAbsolutePath ());
            if (!deleteFile (thisFile)) succeeded = false;
          }
        }
      }
      return succeeded;
    }



/**
Deletes the various jarmaker testing files from the current directory.
 @exception  Exception  If an exception occurs.
**/
    public static void deleteTestingFilesFromLocalDirectory ()
        throws Exception
    {
      if (DEBUG)
        System.out.println ("Deleting jarmaker testing files from current directory.");
      deleteFile (JUNGLE_JAR);         // jungle.jar
      deleteFile (JUNGLE_JAR_SMALL);   // jungleSmall.jar
      deleteFile (TOOLBOX_JAR);        // toolbox.jar
      deleteFile (TOOLBOX_JAR_SMALL);  // toolbox.jar
      deleteFile (ADDITIONAL_FILE_1);  // jungle.txt
      deleteFile (ADDITIONAL_FILE_2);  // jungle.gif
      deleteFile (ADDITIONAL_FILE_3);  // Trees/sunelm.jpg
      deleteFile (ADDITIONAL_FILE_4);  // AddlFile/dolphins.jpg
      deleteDirectory (JUNGLE_PACKAGE_NAME);  // directory "jungle"
      deleteDirectory ("Trees");       // directory "Trees"
      deleteDirectory (LANGUAGE_DIR);
      deleteDirectory (ADDLFILE_DIR);
      deleteDirectory (MANIFEST_DIR_NAME); // META-INF directory

      deleteFile ("jt400.jar");
      deleteFile ("Var17.jar");
      deleteFile ("Var18.jar");
      deleteFile ("jt400Small.jar");
      deleteFile (MRI_TEMPLATE);
    }


/**
Delete a directory and all its subdirectories.
**/
    public static boolean deleteDirectory ( File dir )
    {
      try {
        if (DEBUG && dir.exists () && !dir.isDirectory ())
          System.err.println ("Not a directory: " + dir.getAbsolutePath ());
      }
      catch (Exception e)
      {
        if (DEBUG) {
          System.err.println ("Exception in deleteDirectory: " +
                              e.getMessage());
          e.printStackTrace ();
        }
      }
      deleteFileOrDir (dir);
      return (!dir.exists ());
    }
    public static boolean deleteDirectory ( String dirName )
    {
      return deleteDirectory (new File (dirName));
    }



/**
Delete a directory or file, and all its subdirectories (if it's a directory).
**/
    public static void deleteFileOrDir ( File fileOrDir )
    {
      boolean isDirectory = false;
      boolean isFile = false;
      try {
        if (fileOrDir.isDirectory ())
          isDirectory = true;
        else if (fileOrDir.isFile ())
          isFile = true;
      }
      catch (Exception e)
      {
        if (DEBUG) {
          System.err.println ("Exception in deleteFileOrDir: " +
                              e.getMessage());
          e.printStackTrace ();
        }
      }

      if (isFile) deleteFile (fileOrDir );
      else if (isDirectory)
      {
        String[] entriesInDir = null;
        try { entriesInDir = fileOrDir.list (); }
        catch (Exception e)
        {
          if (DEBUG) {
            System.err.println ("Exception in deleteFileOrDir(2): " +
                                e.getMessage());
            e.printStackTrace ();
          }
        }
        if (entriesInDir != null)
          for (int i=0; i<entriesInDir.length; i++)
          {
            String name = entriesInDir[i];
            File thisFile = new File (fileOrDir, name);
            deleteFileOrDir (thisFile);
          }
        try { fileOrDir.delete (); } // Now delete the empty directory
        catch (Exception e)
        {
          if (DEBUG) {
            System.err.println ("Exception in deleteFileOrDir(3): " +
                                e.getMessage());
            e.printStackTrace ();
          }
        }
      }
    }



  /**
   Generates the jar entry name for a given file path name.

   @param filePath A file path name.
   @return         The derived jar entry name.
   **/
  public static String generateJarEntryName (File file, String basePath)
  {
    // Strip off the base path, if it matches the beginning of the file path.
    String filePath = file.getAbsolutePath ();
    if (basePath != null && filePath.startsWith (basePath))
      filePath = filePath.substring (1 + basePath.length ());
/*  else
    {
      System.err.println ("Warning: File path does not begin with " +
                          "base path for additional files.");
      System.err.println ("File path: " + filePath);
      System.err.println ("Base path: " + basePath);
    }*/

    // Replace all filepath separators with "/".
    String entryName = filePath.replace (FILE_SEPARATOR, '/');

    // Remove leading filepath separator if present.
    if (entryName.startsWith ("/"))
      entryName = entryName.substring (1);

    if (DEBUG) System.out.println ("Generated entry name: "+entryName);

    return entryName;
  }


  /**
   Generates the jar entry name for a given file path name.

   @param fileList A Hashtable of (File, String) pairs, where the String
                   is the "base path" for the file.
   @return         The derived jar entry names (String's).
   **/
  public static Vector generateJarEntryNames (Hashtable fileList)
  {
    Vector entryNames = new Vector (fileList.size ());
    Enumeration e = fileList.keys ();
    while (e.hasMoreElements ())
    {
      File file = (File)e.nextElement ();
      String basePath = (String)fileList.get (file);
      entryNames.add (generateJarEntryName (file, basePath));
    }
    return entryNames;
  }


/**
Lists the entry names in the specified jar file, including the Manifest entry.
In the returned list, files are represented as jar entrynames.
 @exception  IOException  If an exception occurs.
**/
  public static Vector listJarContents (File jarFile)
    throws IOException
  {
    Vector contentsList = new Vector ();
    if (jarFile == null)
      System.err.println ("JMTest.listJarContents: Argument is null.");
    else if (!jarFile.exists ())
      System.err.println ("JMTest.listJarContents: File does not exist: " +
                          jarFile.getAbsolutePath ());
    else if (!jarFile.isFile ())
      System.err.println ("JMTest.listJarContents: Not a file: " +
                          jarFile.getAbsolutePath ());
    else
    {
      ZipFile zipFile = null;
      try
      {
        zipFile = new ZipFile (jarFile);
        Enumeration e = zipFile.entries ();
        while (e.hasMoreElements ()) {
          ZipEntry entry = (ZipEntry)e.nextElement ();
          String entryName = entry.getName ();
          contentsList.add (entryName);
        }
      }
      finally {
        if (zipFile != null) zipFile.close ();
      }
    }
    return contentsList;
  }


/**
Lists the files in (and under) the specified directory.
In the returned list, files are represented as jar entrynames.
**/
  public static Vector listFiles (File file)
  {
    if (file == null)
    {
      System.err.println ("JMTest.listFiles: Argument is null.");
      return new Vector ();
    }
    File parentDir = new File (file.getParent ());
    return listFiles (file, parentDir.getAbsolutePath ());
  }

  private static Vector listFiles (Vector fileOrDirList, String basePath)
  {
    Vector result = new Vector ();
    Enumeration e = fileOrDirList.elements ();
    while (e.hasMoreElements ())
    {
      File fileOrDir = (File)e.nextElement ();
      Vector subList = listFiles (fileOrDir, basePath);
      copyList (subList, result);  // merge subList into result
    }
    return result;
  }

  private static Vector listFiles (File fileOrDir, String basePath)
  {
    Vector result = new Vector ();
    try
    {
      if (fileOrDir.isDirectory ())
      {
        File thisDir = fileOrDir;  // reduce naming confusion
        String[] contents = thisDir.list ();  // Get contents of this directory
        for (int i=0; i<contents.length; i++)
        {
          String fileName = contents[i];
          File file = new File (thisDir, fileName);
          Vector subList = listFiles (file, basePath);
          copyList (subList, result);  // merge subList into result
        }
      }
      result.add (normalize (fileOrDir, basePath));
   }
    catch (Exception e)
    {
      System.out.println ("listFiles(File,String):" + e.getMessage());
      e.printStackTrace();
    }
    return result;
  }


/**
Lists the entry names in the manifest of the specified jar file.
In the returned list, files are represented as jar entrynames.
**/
  public static Vector listManifestContents (File jarFile)
  {
    Vector contentsList = new Vector ();
    if (jarFile == null)
    {
      System.err.println ("JMTest.listManifestContents: Argument is null.");
    }
    else
    {
      ZipFile zipFile = null;
      BufferedReader reader = null;
      try
      {
        zipFile = new ZipFile (jarFile);
        ZipEntry manifestEntry = zipFile.getEntry (MANIFEST_ENTRY_NAME);
        if (manifestEntry == null)
          System.err.println ("ERROR: listManifestContents(): File " +
                              jarFile.getAbsolutePath () +
                              " contains no manifest.");
        else
        {
          // Get the list of names in the manifest.
          if (DEBUG)
            System.out.println ("Analyzing manifest file.");

          // Set up for reading.  We must use a reader because we
          // are dealing with text.
          reader = new BufferedReader (new InputStreamReader (zipFile.getInputStream (manifestEntry)));
          StringBuffer buffer = new StringBuffer ();

          // Read the manifest file and only record entries which
          // represent referenced files.
          String line;
          while (reader.ready ())
          {
            line = reader.readLine ();
            if (line.startsWith (MANIFEST_NAME_KEYWORD))
            {
              String fileName = line.substring (MANIFEST_NAME_KEYWORD.length ()).trim ();
              if (DEBUG) System.out.println ("Manifest entry: " + fileName);
              contentsList.add (fileName);
              // Skip to beginning of next section.
              while (reader.ready () && (line.length () > 0))
                line = reader.readLine ();
            }
          }  // ... while
        }
      }
      catch (Exception e) {
        System.err.println ("Exception in listManifestContents: " +
                            e.getMessage ());
        e.printStackTrace ();
      }
      finally {
        if (reader != null) {
          try { reader.close (); }
          catch (Exception e) {
            System.err.println (e.toString ());
            e.printStackTrace ();
          }
        }
        if (zipFile != null) {
          try { zipFile.close (); }
          catch (Exception e) {
            System.err.println (e.toString ());
            e.printStackTrace ();
          }
        }
      }
    }
    return contentsList;
  }

  private static String normalize (File fileOrDir, String basePath)
  {
    String filePath = fileOrDir.getAbsolutePath ();
    String outString = null;
    if (!filePath.startsWith (basePath))
      System.err.println ("filePath doesn't start with basePath");
    else
    {
      String relPath = filePath.substring (basePath.length ());
      outString = relPath.replace (FILE_SEPARATOR, '/');
    }
    if (fileOrDir.isDirectory () && !outString.endsWith ("/"))
      outString = outString + "/";
    while (outString.length () > 1 && outString.startsWith ("/"))
      outString = outString.substring (1);
    if (DEBUG) System.out.println ("Normalized path: " + outString);
    return outString;
  }

  /** Removes directory entries from a list of jar entry names.
   Also removes the MANIFEST.MF entry.
   @parm list A list of jar entry names (String's)
   **/
  public static Vector removeDirectoryEntries (Vector inList)
  {
    return removeDirectoryEntries(inList, true);
  }

  /** Removes directory entries from a list of jar entry names.
   @parm list A list of jar entry names (String's)
   @parm removeManifestEntry Whether to remove the MANIFEST.MF entry
   **/
  public static Vector removeDirectoryEntries (Vector inList, boolean removeManifestEntry)
  {
    Vector outList = new Vector (inList.size ());

    Enumeration e = inList.elements ();
    while (e.hasMoreElements ())
    {
      String entryName = (String)e.nextElement ();
      if (!entryName.endsWith ("/"))
        outList.add(entryName);
    }

    if (removeManifestEntry)
      outList.remove(MANIFEST_ENTRY_NAME);

    return outList;
  }

  /** Removes the entry "META-INF/MANIFEST.MF" entry from the list.  Returns a new list.
   @parm list A list of jar entry names (String's)
   **/
  public static Vector removeManifestEntry (Vector inList)
  {
    Vector outList = new Vector (inList.size ());
    copyList (inList, outList);
    outList.remove(MANIFEST_ENTRY_NAME);
    return outList;
  }

  /**
   Generates a default destination jar file name,
   based on the name of the source jar file.

   @param sourceJarFile The source jar file.
   @return              A default destination jar file.
   **/
  public static File setupDefaultDestinationJarFile (File sourceJarFile)
  {
    String sourceJarName = sourceJarFile.getName ();
    int index = sourceJarName.lastIndexOf ('.');
    String destinationJarName;
    String suffix = "Small";
    if (index == -1)
      destinationJarName = sourceJarName + suffix;
    else
      destinationJarName = sourceJarName.substring (0, index)
        + suffix + sourceJarName.substring (index);
    return new File (destinationJarName); // use the current directory
  }


/**
Verifies that the correct files got extracted.
**/
    public static boolean verifyExtraction (File baseDirectory, Vector subDirs, Vector expected)
    {
      Vector actual = new Vector ();
      if (!subDirs.contains (MANIFEST_DIR_NAME))
        subDirs.add (MANIFEST_DIR_NAME);
      Enumeration e = subDirs.elements ();
      while (e.hasMoreElements ())
      {
        String subDirName = (String)e.nextElement ();
        Vector subList =
          listFiles (new File (baseDirectory, subDirName));
      copyList (subList, actual);  // merge subList into cumulative list
      }

      if (!expected.contains (MANIFEST_ENTRY_NAME))
        expected.add (MANIFEST_ENTRY_NAME);
      return compareLists (actual, addDirectoryEntries (expected),
                           "actualFiles", "expectedFiles");
    }

    public static boolean verifyExtraction (File baseDirectory, Vector expected)
    {
      Vector subDirs = new Vector ();
      subDirs.add (JUNGLE_PACKAGE_NAME);
      return verifyExtraction (baseDirectory, subDirs, expected);
    }


/**
Verifies that specific files got extracted.
**/
    public static boolean verifyExtractionContains (File baseDirectory, Vector subDirs, Vector expected)
    {
      boolean result = true;
      Vector actual = new Vector ();
      if (!subDirs.contains (MANIFEST_DIR_NAME))
        subDirs.add (MANIFEST_DIR_NAME);
      Enumeration e = subDirs.elements ();
      while (e.hasMoreElements ())
      {
        String subDirName = (String)e.nextElement ();
        Vector subList =
          listFiles (new File (baseDirectory, subDirName));
      copyList (subList, actual);  // merge subList into cumulative list
      }

      if (!expected.contains (MANIFEST_ENTRY_NAME))
        expected.add (MANIFEST_ENTRY_NAME);

      Enumeration e1 = expected.elements ();
      while (e1.hasMoreElements ()) {
        String elem = (String)e1.nextElement ();
        if (!actual.contains (elem)) {
          System.err.println ("Missing: " + elem);
          result = false;
        }
      }
      return result;
    }


/**
Verifies that specific files did NOT get extracted.
**/
    public static boolean verifyExtractionNotContains (File baseDirectory, Vector subDirs, Vector notExpected)
    {
      boolean result = true;
      Vector actual = new Vector ();
      if (!subDirs.contains (MANIFEST_DIR_NAME))
        subDirs.add (MANIFEST_DIR_NAME);
      Enumeration e = subDirs.elements ();
      while (e.hasMoreElements ())
      {
        String subDirName = (String)e.nextElement ();
        Vector subList =
          listFiles (new File (baseDirectory, subDirName));
      copyList (subList, actual);  // merge subList into cumulative list
      }

      Enumeration e1 = notExpected.elements ();
      while (e1.hasMoreElements ()) {
        String elem = (String)e1.nextElement ();
        if (actual.contains (elem)) {
          System.err.println ("Not expected: " + elem);
          result = false;
        }
      }
      return result;
    }

    // Verifies that the manifest matches the actual contents of the jar.
    public static boolean validateJar (File jarFile)
    {
      boolean result = true;
      try
      {
        // Get list of all entries in the jar.
        Vector jarEntries = listJarContents (jarFile);
        jarEntries = removeDirectoryEntries (jarEntries); // exclude directories

        // Get list of all entries in the manifest.
        Vector manifestEntries = listManifestContents (jarFile);

        result = compareLists (jarEntries, manifestEntries,
                               "jarEntries", "manifestEntries");
      }
      catch (Exception e) {
        e.printStackTrace ();
        result = false;
      }
      return result;
    }

    // Verifies that the manifest matches the actual contents of the jar,
    // and that its size does not exceed the specified value.
    public static boolean validateJar (File jarFile, long maxSizeKbytes)
    {
      return validateJar (jarFile, maxSizeKbytes, false);
    }

    // Verifies that the manifest matches the actual contents of the jar,
    // and that its size does not exceed the specified value.
    public static boolean validateJar (File jarFile, long maxSizeKbytes,
                                boolean expectOversize)
    {
      boolean result = true;
      try
      {
        result = validateJar (jarFile);

        if (jarFile.length () > maxSizeKbytes*1024) {
          System.err.println ("File " + jarFile.getAbsolutePath() +
                              " is too big: " + jarFile.length ());
          if (!expectOversize) result = false;
        }
      }
      catch (Exception e) {
        e.printStackTrace ();
        result = false;
      }
      return result;
    }

    // For each target jar:
    // Verifies that the manifest matches the actual contents of the jar,
    // and that its size does not exceed the specified value.
    // Also verifies that every entry in the source jar file occurs
    // in exactly one of the target jar files.
    public static boolean validateJars (File sourceJarFile, Vector targetJarFiles)
    {
      return validateJars (sourceJarFile, targetJarFiles, SPLIT_SIZE_KBYTES);
    }
    public static boolean validateJars (File sourceJarFile, Vector targetJarFiles,
                                 long maxSizeKbytes)
    {
      return validateJars (sourceJarFile, targetJarFiles, SPLIT_SIZE_KBYTES,
                           false);
    }
    public static boolean validateJars (File sourceJarFile, Vector targetJarFiles,
                                 long maxSizeKbytes, boolean expectOversize)
    {
      boolean result = true;
      try
      {
        Vector targetJarEntries = new Vector ();
        Enumeration e = targetJarFiles.elements ();
        while (e.hasMoreElements ())
        {
          File targetFile = (File)e.nextElement ();
          if (!validateJar (targetFile,maxSizeKbytes,expectOversize))
            result = false;
          Vector entriesInThisJar = listJarContents (targetFile);
          entriesInThisJar.remove (MANIFEST_ENTRY_NAME);
          Enumeration e1 = entriesInThisJar.elements ();
          while (e1.hasMoreElements ())
          {
            String entryName = (String)e1.nextElement ();
            if (targetJarEntries.contains (entryName)) {
              if (!entryName.endsWith ("/")) {
                System.err.println ("Multiple occurrences of entry: " +
                                    entryName);
                result = false;
              }
            }
            else targetJarEntries.add (entryName);
          }
        }
        // Now verify that targetJarEntries matches the list of entries
        // in the source jar file.
        Vector sourceJarEntries = listJarContents (sourceJarFile);
        sourceJarEntries.remove (MANIFEST_ENTRY_NAME);
        if (!compareLists (targetJarEntries, sourceJarEntries,
                           "entriesInTargetJars", "entriesInSourceJar"))
          result = false;
      }
      catch (Exception e) {
        e.printStackTrace ();
        result = false;
      }
      return result;
    }


/**
Verifies that the correct files got copied into the destination jar file,
and that the correct entries got created in the manifest.
**/
    public static boolean verifyJar (File destinationJar, Vector expectedContents)
    {
      return verifyJar (destinationJar, expectedContents, removeManifestEntry(expectedContents), false);
    }

    public static boolean verifyJar (File destinationJar, Vector expectedContents, boolean isToolboxJar)
    {
      return verifyJar (destinationJar, expectedContents, removeManifestEntry(expectedContents), isToolboxJar);
    }

    public static boolean verifyJar (File destinationJar, Vector expectedJarEntries,
                                 Vector expectedManifestEntries)
    {
      return verifyJar (destinationJar, expectedJarEntries, expectedManifestEntries, false);
    }

    // Verifies that the destination jar exists, and contains the
    // expected files and nothing else.
    public static boolean verifyJar (File destinationJar, Vector expectedJarEntries,
                              Vector expectedManifestEntries, boolean isToolboxJar)
    {
      boolean result = true;
      // Verify that the destination jar exists, and contains the
      // expected files and nothing else.
      try
      {
        Vector actualJar = listJarContents (destinationJar);
        Vector actualManifest = listManifestContents (destinationJar);
        Vector expectedManifest = expectedManifestEntries;
        Vector expectedJar = expectedJarEntries;

        // Expect the generated jar to contain a manifest.
        if (!expectedJar.contains (MANIFEST_ENTRY_NAME))
          expectedJar.add (MANIFEST_ENTRY_NAME);

        // Should not see an entry for the manifest directory.
        expectedJar.remove (MANIFEST_DIR_NAME + "/");

        // If it's a Toolbox jar, expect it to contain a copyright file.
        if (isToolboxJar)
        {
          if (!expectedJar.contains (COPYRIGHT_ENTRY_NAME))
            expectedJar.add (COPYRIGHT_ENTRY_NAME);
        }

        if (compareLists (actualManifest, expectedManifest,
                          "actualManifest", "expectedManifest"))
          result = compareLists (actualJar, expectedJar,
                                 "actualJar", "expectedJar");
        else
        {
          System.err.println (">>>Manifest is not as expected.");
          result = false;
        }
      }
      catch (Exception e) {
        e.printStackTrace ();
        result = false;
      }
      return result;
    }

    public static boolean verifyJarContains (File destinationJar,
                                      Vector expectedJarEntries)
    {
      return verifyJarContains (destinationJar, expectedJarEntries, false);
    }

    public static boolean verifyJarContains (File destinationJar,
                                      Vector expectedJarEntries,
                                      boolean isToolboxJar)
    {
      boolean result = true;
      // Verify that the destination jar exists, and contains the
      // expected files and nothing else.
      try
      {
        Vector actualJar = listJarContents (destinationJar);

        // Expect the generated jar to contain directory entries.
        Vector expectedJar = addDirectoryEntries (expectedJarEntries);

        // Expect the generated jar to contain a manifest.
        if (!expectedJar.contains (MANIFEST_ENTRY_NAME))
          expectedJar.add (MANIFEST_ENTRY_NAME);

        // Should not see an entry for the manifest directory.
        expectedJar.remove (MANIFEST_DIR_NAME + "/");

        // If it's a Toolbox jar, expect it to contain a copyright file.
        if (isToolboxJar)
        {
          if (!expectedJar.contains (COPYRIGHT_ENTRY_NAME))
            expectedJar.add (COPYRIGHT_ENTRY_NAME);
        }

        Enumeration e = expectedJar.elements ();
        while (e.hasMoreElements ()) {
          String elem = (String)e.nextElement ();
          if (!actualJar.contains (elem)) {
            System.err.println ("Missing: " + elem);
            result = false;
          }
        }
      }
      catch (Exception e) {
        e.printStackTrace ();
        result = false;
      }
      return result;
    }

    public static boolean verifyJarNotContains (File destinationJar,
                                      Vector notExpectedJarEntries)
    {
      boolean result = true;
      // Verify that the destination jar exists, and contains the
      // expected files and nothing else.
      try
      {
        Vector actualJar = listJarContents (destinationJar);

        Enumeration e = notExpectedJarEntries.elements ();
        while (e.hasMoreElements ()) {
          String elem = (String)e.nextElement ();
          if (actualJar.contains (elem)) {
            System.err.println ("Not expected: " + elem);
            result = false;
          }
        }
      }
      catch (Exception e) {
        e.printStackTrace ();
        result = false;
      }
      return result;
    }



/**
Creates the testcases.
**/
    public void createTestcases ()
    {
      // Verify that we're using the regular (non-native) Toolbox jar file.
      // The native Toolbox jar file doesn't include most of the utilities classes.
      final String JARMAKER_CLASS = "utilities.JarMaker";
      try {
        Class.forName(JARMAKER_CLASS);
        foundJarMakerClassesOnClasspath_ = true;
      }
      catch (ClassNotFoundException e) {
        System.err.println ("ERROR: The class '" + JARMAKER_CLASS + "' was not found on the classpath.  Please be aware that the 'native' Toolbox jar file (jt400Native.jar) does not include the JarMaker classes.");
        System.err.println("No JM testcases will be attempted.");
        foundJarMakerClassesOnClasspath_ = false;
        return;
      }

      // Setup the power user if provided.
      AS400 pwrSys = null;
      
      if (misc_ != null)
      {
        StringTokenizer tokenizer = new StringTokenizer (misc_, ",");
        String uid = tokenizer.nextToken ();
        String pwd = tokenizer.nextToken ();
        pwrSys = new AS400 (systemObject_.getSystemName(), uid, pwd);
        try
        {
          pwrSys.setGuiAvailable(false);
        }
        catch (PropertyVetoException e)
        {
          // Ignore.
        }
      }

      addTestcase (new JMAddlFiles (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMExtract (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMListen (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMMakeJar (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMPackage (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMSetGet (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

// Longer-running testcases:

      addTestcase (new JMSplit (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMBeans (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMCcsid (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMComp (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMLang (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMTBMakeJar (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMParseArgs (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      addTestcase (new JMTBParseArgs (systemObject_,
                                 namesAndVars_, runMode_,
                                 fileOutputStream_));

      // Reports invalid testcase names.
      for (Enumeration e = namesAndVars_.keys (); e.hasMoreElements (); ) {
        String name = (String)e.nextElement ();
        System.out.println ("Testcase " + name + " not found.");
      }
    }

}




