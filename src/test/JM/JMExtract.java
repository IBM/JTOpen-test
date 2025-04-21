///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMExtract.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JM;

import utilities.*;
import com.ibm.as400.access.AS400;

import test.JMTest;
import test.Testcase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMExtract.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>extract(File)
<li>extract(File,File)
</ul>
**/
@SuppressWarnings("deprecation")
public class JMExtract
extends Testcase
{
  static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));

/**
Constructor.
**/
  public JMExtract (AS400 systemObject,
                   Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMExtract",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
    }



//--------------------------------------------------------------------
// Variations for extract(File) .


/**
 extract(File) - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.extract (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "sourceJarFile");
      }
    }

/**
 extract(File) - Jar file doesn't exist. Should throw an exception.
 **/
    public void Var002 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        File bogusFile = new File ("bogus");  // a nonexistent file
        jm.extract (bogusFile);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.io.FileNotFoundException");
      }
    }

/**
 extract(File) - No setters called yet. Should simply extract all
 files in the source jar.
 The base directory for the extraction should be the current directory.
 **/
    public void Var003 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Extract the files.
        File dir = jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        // (We expect all files to have been extracted)
        Vector<String> expected = new Vector<String> ();
        JMTest.copyStringList (JMTest.allJungleFiles_, expected);
        if (!dir.equals (CURRENT_DIR)) failed ("Didn't return CWD");
        else
          assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Single required jar entry which has no dependencies.
 Should simply extract the specified jar entry.
 The base directory for the extraction should be the current directory.
 **/
    public void Var004 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify a required entry.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add (" jungle/animal.jpg "); // leading/trailing spaces
        jm.setRequiredFiles (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        Vector<String> expected = new Vector<String> (1);
        expected.add ("jungle/animal.jpg");
        assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Two required jar entries which have no dependencies.
 Should simply extract the specified jar entries.
 The base directory for the extraction should be the current directory.
 **/
    public void Var005 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify a required entry.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add ("jungle/animal.jpg");
        entryList.add ("jungle/JungleMRI.properties");
        jm.setRequiredFiles (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        assertCondition (JMTest.verifyExtraction (CURRENT_DIR, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Single required jar entry with known list of dependencies.
 Should simply extract the specified jar entry and its dependencies.
 The base directory for the extraction should be the current directory.
 **/
    public void Var006 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify a required entry.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add ("jungle/Animal.class");
        jm.setRequiredFiles (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        Vector<String> expected = new Vector<String> ();
        JMTest.copyStringList (entryList, expected);
        expected.add ("jungle/Animal$AnimalThread.class");
        expected.add ("jungle/JungleMRI.properties");
        expected.add ("jungle/animal.jpg");
        assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Two required jar entries with known list of dependencies.
 Should simply extract the specified jar entries and their dependencies.
 The base directory for the extraction should be the current directory.
 **/
    public void Var007 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify a required entry.
        Vector<String> entryList = new Vector<String>(1);
        entryList.add ("jungle/prey/Prey.class");
        entryList.add ("jungle/predator/Predator.class");
        jm.setRequiredFiles (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        Vector<String> expected = new Vector<String> ();
        JMTest.copyStringList (entryList, expected);
        expected.add ("jungle/Animal$AnimalThread.class");
        expected.add ("jungle/Animal.class");
        expected.add ("jungle/JungleMRI.properties");
        expected.add ("jungle/animal.jpg");
        expected.add ("jungle/predator/Predator.class");
        expected.add ("jungle/prey/Prey.class");
        assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Single required package.
 Should simply extract all the files in the specified package.
 The base directory for the extraction should be the current directory.
 **/
    public void Var008 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify a required package.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add ("jungle.predator");
        jm.setPackages (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        Vector<String> expected = new Vector<String> ();
        expected.add ("jungle/predator/Predator.class");
        expected.add ("jungle/predator/Predator.java");
        expected.add ("jungle/predator/puma.jpg");
        assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Two required packages.
 Should simply extract all the files in the specified packages.
 The base directory for the extraction should be the current directory.
 **/
    public void Var009 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify a required package.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add ("jungle");
        entryList.add ("jungle.prey");
        jm.setPackages (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        Vector<String> expected = new Vector<String> ();
        expected.add ("jungle/Animal.java");
        expected.add ("jungle/Animal.class");
        expected.add ("jungle/Animal$AnimalThread.class");
        expected.add ("jungle/prey/Prey.java");
        expected.add ("jungle/prey/Prey.class");
        expected.add ("jungle/animal.jpg");
        expected.add ("jungle/JungleMRI.properties");
        expected.add ("jungle/tree.jpg");
        assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 extract(File) - Additional files specified, and single required
 jar entry which has no dependencies.
 Should simply extract the specified jar entry.
 The base directory for the extraction should be the current directory.
 Attended: Should see a warning message indicating that the specified
 additional files are ignored.
 **/
    public void Var010 (int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) return;
      JarMaker jm = new JarMaker ();
      try {
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (new File (JMTest.JUNGLE_PACKAGE_NAME));
        // Specify some additional files.
        Vector<File> addlFiles = new Vector<File> (2);
        addlFiles.add (JMTest.ADDITIONAL_FILE_1);
        addlFiles.add (JMTest.ADDITIONAL_FILE_2);
        jm.setAdditionalFiles (addlFiles);
        // Specify a required entry.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add ("jungle/predator/puma.jpg");
        jm.setRequiredFiles (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got extracted.
        Vector<String> expected = new Vector<String> ();
        JMTest.copyStringList (entryList, expected);
        expected.add ("jungle/predator/puma.jpg");

        // String text = "Was a warning message printed to the command entry window," +
        //  " indicating that the additional files would be ignored?";
        //if (true != VTestUtilities.ask(text))
        //  failed("No warning message");
        // else
          assertCondition (JMTest.verifyExtraction (CURRENT_DIR, expected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }





//-----------------------------------------------------------------------
// Variations for extract(File,File) .



/**
 extract(File,File) - First arg is null. Should throw an exception.
 **/
    public void Var011 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        File baseDir = new File ("jungle");
        jm.extract (null, baseDir);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "sourceJarFile");
      }
    }

/**
 extract(File,File) - Second arg is null. Should throw an exception.
 **/
    public void Var012 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.extract (JMTest.JUNGLE_JAR, null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "outputDirectory");
      }
    }

/**
 extract(File,File) - Single required jar entry which has no dependencies.
 Should simply extract the specified jar entry.
 The base directory for the extraction should be the specified directory.
 **/
    public void Var013 ()
    {
      JarMaker jm = new JarMaker ();
      File Dir13 = null;
      try {
        Dir13 = new File ("Dir13");
        // Remove the extraction directory if it exists.
        JMTest.deleteDirectory (Dir13);
        // Specify a required entry.
        Vector<String> entryList = new Vector<String> (1);
        entryList.add ("jungle/animal.jpg");
        jm.setRequiredFiles (entryList);
        // Extract the files.
        jm.extract (JMTest.JUNGLE_JAR, Dir13);
        // Verify that only the correct files got extracted.
        assertCondition (JMTest.verifyExtraction (Dir13, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally
      {
        if (Dir13 != null) JMTest.deleteDirectory (Dir13);
      }
    }






}



