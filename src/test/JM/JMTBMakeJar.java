///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMTBMakeJar.java
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMTBMakeJar.  This tests the following
methods of the ToolboxJarMaker class:

<ul compact>
<li>Using a non-Toolbox source jar file:
  <ul compact>
  <li>makeJar(File)
  <li>makeJar(File,File)
  </ul>
<li>Using a Toolbox source jar file:
  <ul compact>
  <li>makeJar(File)
  <li>makeJar(File,File)
  </ul>
</ul>
**/

public class JMTBMakeJar
extends Testcase
{

  Vector allComponents_;  // list of all Toolbox components

/**
Constructor.
**/
    public JMTBMakeJar (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMTBMakeJar",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
      JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);

     // List of all defined components.
     Vector c = new Vector (27);
     allComponents_ = new Vector (27);
     c.add (ToolboxJarMaker.AS400);
     c.add (ToolboxJarMaker.AS400_VISUAL);
     c.add (ToolboxJarMaker.COMMAND_CALL);
     c.add (ToolboxJarMaker.COMMAND_CALL_VISUAL);
     c.add (ToolboxJarMaker.DATA_DESCRIPTION);
     c.add (ToolboxJarMaker.DATA_QUEUE);
     c.add (ToolboxJarMaker.DATA_QUEUE_VISUAL);
     c.add (ToolboxJarMaker.DIGITAL_CERTIFICATE);
     c.add (ToolboxJarMaker.INTEGRATED_FILE_SYSTEM);
     c.add (ToolboxJarMaker.INTEGRATED_FILE_SYSTEM_VISUAL);
     c.add (ToolboxJarMaker.JDBC);
     c.add (ToolboxJarMaker.JDBC_VISUAL);
     c.add (ToolboxJarMaker.JOB);
     c.add (ToolboxJarMaker.JOB_VISUAL);
     c.add (ToolboxJarMaker.MESSAGE);
     c.add (ToolboxJarMaker.MESSAGE_VISUAL);
     c.add (ToolboxJarMaker.PRINT);
     c.add (ToolboxJarMaker.PRINT_VISUAL);
     c.add (ToolboxJarMaker.PROGRAM_CALL);
     c.add (ToolboxJarMaker.PROGRAM_CALL_VISUAL);
     c.add (ToolboxJarMaker.RECORD_LEVEL_ACCESS);
     c.add (ToolboxJarMaker.RECORD_LEVEL_ACCESS_VISUAL);
     c.add (ToolboxJarMaker.TRACE);
     c.add (ToolboxJarMaker.USER);
     c.add (ToolboxJarMaker.USER_SPACE);
     c.add (ToolboxJarMaker.USER_VISUAL);
     JMTest.copyList (c, allComponents_);
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      if (!JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL))
        output_.println ("Failed to cleanup " +
                         JMTest.JUNGLE_JAR_SMALL.getAbsolutePath ());
    }


//////////////////////////////////////////////////////////////////////
// Variations for makeJar (File), using a non-Toolbox source jar file :


/**
 makeJar(File) - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.makeJar (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
    }

/**
 makeJar(File) - Jar file doesn't exist. Should throw an exception.
 **/
    public void Var002 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        File bogusFile = new File ("bogus");  // a nonexistent file
        jm.makeJar (bogusFile);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.io.FileNotFoundException");
      }
    }

/**
 makeJar(File) - No setters called yet. Should simply copy all
 files in the source jar.
 The default destination jar file should be created in the current directory.
 **/
    public void Var003 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        // (We expect all files to have been copied)

        Vector expectedJarFiles = new Vector ();
        JMTest.copyList (JMTest.allJungleFiles_, expectedJarFiles);

        Vector expectedManifestFiles = new Vector ();
        JMTest.copyList (expectedJarFiles, expectedManifestFiles);
        expectedManifestFiles.remove("META-INF/");
        expectedManifestFiles.remove("META-INF/MANIFEST.MF");
        expectedManifestFiles.remove("jungle/");
        expectedManifestFiles.remove("jungle/predator/");
        expectedManifestFiles.remove("jungle/prey/");

        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJarFiles, expectedManifestFiles));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Single required jar entry which has no dependencies.
 Should simply copy the specified jar entry.
 The default destination jar file should be created in the current directory.
 **/
    public void Var004 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/animal.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Two required jar entries which have no dependencies.
 Should simply copy the specified jar entries.
 The default destination jar file should be created in the current directory.
 **/
    public void Var005 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/animal.jpg");
        entryList.add ("jungle/JungleMRI.properties");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Single required jar entry with known list of dependencies.
 Should simply copy the specified jar entry and its dependencies.
 The default destination jar file should be created in the current directory.
 **/
    public void Var006 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/Animal.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.

        entryList.add ("jungle/Animal$AnimalThread.class");
        entryList.add ("jungle/JungleMRI.properties");
        entryList.add ("jungle/animal.jpg");

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Two required jar entries with known list of dependencies.
 Should simply copy the specified jar entries and their dependencies.
 The default destination jar file should be created in the current directory.
 **/
    public void Var007 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/prey/Prey.class");
        entryList.add ("jungle/predator/Predator.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.

        entryList.add ("jungle/Animal$AnimalThread.class");
        entryList.add ("jungle/Animal.class");
        entryList.add ("jungle/JungleMRI.properties");
        entryList.add ("jungle/animal.jpg");
        entryList.add ("jungle/predator/Predator.class");
        entryList.add ("jungle/prey/Prey.class");

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Single required package.
 Should simply copy all the files in the specified package.
 The default destination jar file should be created in the current directory.
 **/
    public void Var008 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required package.
        Vector entryList = new Vector (1);
        entryList.add ("jungle.predator");
        jm.setPackages (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.

        Vector expectedEntries = new Vector();
        expectedEntries.add ("jungle/predator/Predator.class");
        expectedEntries.add ("jungle/predator/Predator.java");
        expectedEntries.add ("jungle/predator/puma.jpg");

        Vector expectedJar = JMTest.addDirectoryEntries(expectedEntries);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedEntries));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Two required packages.
 Should simply copy all the files in the specified packages.
 The default destination jar file should be created in the current directory.
 **/
    public void Var009 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required package.
        Vector entryList = new Vector (1);
        entryList.add ("jungle");
        entryList.add ("jungle.prey");
        jm.setPackages (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.

        Vector expectedEntries = new Vector();
        expectedEntries.add ("jungle/Animal.java");
        expectedEntries.add ("jungle/Animal.class");
        expectedEntries.add ("jungle/Animal$AnimalThread.class");
        expectedEntries.add ("jungle/prey/Prey.java");
        expectedEntries.add ("jungle/prey/Prey.class");
        expectedEntries.add ("jungle/animal.jpg");
        expectedEntries.add ("jungle/JungleMRI.properties");
        expectedEntries.add ("jungle/tree.jpg");

        Vector expectedJar = JMTest.addDirectoryEntries(expectedEntries);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedEntries));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Additional files specified, and single required
 jar entry which has no dependencies.
 Require manifest entries for additional files.
 Should simply copy the specified jar entry and the additional files.
 The default destination jar file should be created in the current directory.
 **/
    public void Var010 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify some additional files.
        Vector addlFiles = new Vector (2);
        addlFiles.add (JMTest.ADDITIONAL_FILE_1);
        addlFiles.add (JMTest.ADDITIONAL_FILE_2);
        jm.setAdditionalFiles (addlFiles);
        // We want manifest entries for the additional files.
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/predator/puma.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.

        entryList.add (JMTest.ADDITIONAL_FILE_1_NAME);
        entryList.add (JMTest.ADDITIONAL_FILE_2_NAME);
        entryList.add ("jungle/predator/puma.jpg");

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }





//////////////////////////////////////////////////////////////////////
// Variations for makeJar (File,File), using a non-Toolbox source jar file :



/**
 makeJar(File,File) - First arg is null. Should throw an exception.
 **/
    public void Var011 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        File baseDir = new File ("jungle");
        jm.makeJar (null, baseDir);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
    }

/**
 makeJar(File,File) - Second arg is null. Should throw an exception.
 **/
    public void Var012 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.makeJar (JMTest.JUNGLE_JAR, null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
    }

/**
 makeJar(File,File) - Single required jar entry which has no dependencies.
 Should simply copy the specified jar entry.
 The default destination jar file should be created in the current directory.
 **/
    public void Var013 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
        File Dir13 = null;
      try {
        Dir13 = new File ("Dir13");
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/animal.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR, Dir13);

        // Verify that only the correct files got copied.

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (Dir13, expectedJar, entryList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally
      {
        if (Dir13 != null) JMTest.deleteDirectory (Dir13);
      }
    }



//////////////////////////////////////////////////////////////////////
// Variations for makeJar (File), using a Toolbox source jar file :

/**
 makeJar(File) - No setters called yet. Should simply copy all
 files in the source jar.
 The default destination jar file should be created in the current directory.
 **/
    public void Var014 ()
    {
      printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);
        // Verify that only the correct files got copied.
        // (We expect all files to have been copied)
        assertCondition (JMTest.compareJars (JMTest.TOOLBOX_JAR_SMALL, JMTest.TOOLBOX_JAR));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Specify all components, and require beans.
 Should copy all files in the source jar (except any "orphans").
 The default destination jar file should be created in the current directory.
 **/
    public void Var015 ()
    {
      notApplicable("This variation is no longer applicable.");
      return;

//      printVariationStartTime (); // this var may be long-running
//      ToolboxJarMaker jm = new ToolboxJarMaker ();
//      try {
//        // Remove the destination jar file if it exists.
//        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
//        // Require all components.  Require beans.
//        jm.setComponents (allComponents_, true);
//        // Also specify bean info for QSYSObjectPathName, since
//        // it's an "orphan".
//        Vector req = new Vector (1);
//        req.add ("com/ibm/as400/access/QSYSObjectPathNameBeanInfo.class");
//        jm.setRequiredFiles (req);
//        // Make the jar.
//        jm.makeJar (JMTest.TOOLBOX_JAR);
//        // Verify that only the correct files got copied.
//        // (We expect all files to have been copied)
//        assertCondition (JMTest.compareJars (JMTest.TOOLBOX_JAR_SMALL, JMTest.TOOLBOX_JAR));
//      }
//      catch (Exception e) {
//        failed (e, "Unexpected Exception");
//      }
    }

/**
 makeJar(File) - Single required jar entry which has no dependencies.
 Should simply copy the specified jar entry.
 The default destination jar file should be created in the current directory.
 **/
    public void Var016 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/Copyright.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        Vector expectedManifest = new Vector();
        expectedManifest.add("com/ibm/as400/access/");
        assertCondition (JMTest.verifyJar (JMTest.TOOLBOX_JAR_SMALL, expectedJar, expectedManifest));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Single required jar entry with known list of dependencies.
 Should simply copy the specified jar entry and its dependencies.
 The default destination jar file should be created in the current directory.
 **/
    public void Var017 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/Trace.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.

        entryList.add ("com/ibm/as400/access/Copyright.class");
        entryList.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        entryList.add ("com/ibm/as400/access/ReturnCodeException.class");
        entryList.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        entryList.add ("com/ibm/as400/access/CoreMRI.class");
        entryList.add ("com/ibm/as400/access/MRI.class");
        entryList.add ("com/ibm/as400/access/MRI2.class");
        entryList.add ("com/ibm/as400/access/SVMRI.class");
        entryList.add ("com/ibm/as400/access/SVMRI_en.class");
        entryList.add ("com/ibm/as400/access/SystemProperties.class");
        entryList.add ("com/ibm/as400/access/ToolboxLogger.class");

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        Vector expectedManifest = new Vector(1);
        expectedManifest.add("com/ibm/as400/access/");

        assertCondition (JMTest.verifyJar (JMTest.TOOLBOX_JAR_SMALL, expectedJar, expectedManifest, true));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 makeJar(File) - Two required jar entries with known list of dependencies.
 Should simply copy the specified jar entries and their dependencies.
 The default destination jar file should be created in the current directory.
 **/
    public void Var018 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify required entries.
        Vector entryList = new Vector (2);
        entryList.add ("com/ibm/as400/access/Trace.class");
        entryList.add ("com/ibm/as400/access/ExecutionEnvironment.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.
        entryList.add ("com/ibm/as400/access/Copyright.class");
        entryList.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        entryList.add ("com/ibm/as400/access/ReturnCodeException.class");
        entryList.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        entryList.add ("com/ibm/as400/access/CoreMRI.class");
        entryList.add ("com/ibm/as400/access/MRI.class");
        entryList.add ("com/ibm/as400/access/MRI2.class");
        entryList.add ("com/ibm/as400/access/SVMRI.class");
        entryList.add ("com/ibm/as400/access/SVMRI_en.class");
        entryList.add ("com/ibm/as400/access/SystemProperties.class");
        entryList.add ("com/ibm/as400/access/ToolboxLogger.class");
        entryList.add ("com/ibm/as400/access/ConversionMaps.class");

        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        Vector expectedManifest = new Vector(1);
        expectedManifest.add("com/ibm/as400/access/");

        assertCondition (JMTest.verifyJar (JMTest.TOOLBOX_JAR_SMALL, expectedJar, expectedManifest, true));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }


}
