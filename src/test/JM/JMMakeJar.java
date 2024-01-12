///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMMakeJar.java
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
Testcase JMMakeJar.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>makeJar(File)
<li>makeJar(File,File)
</ul>
**/
public class JMMakeJar
extends Testcase
{

/**
Constructor.
**/
  public JMMakeJar (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMMakeJar",
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
// Variations for makeJar(File) :


/**
 makeJar(File) - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      JarMaker jm = new JarMaker ();
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
      JarMaker jm = new JarMaker ();
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        // (We expect all files to have been copied)
        Vector expectedJar = new Vector ();
        JMTest.copyList (JMTest.allJungleFiles_, expectedJar);
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector ();
        entryList.add ("jungle/animal.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector ();
        entryList.add ("jungle/animal.jpg");
        entryList.add ("jungle/JungleMRI.properties");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector ();
        entryList.add ("jungle/Animal.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        expectedJar.add ("jungle/Animal$AnimalThread.class");
        expectedJar.add ("jungle/JungleMRI.properties");
        expectedJar.add ("jungle/animal.jpg");
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector ();
        entryList.add ("jungle/prey/Prey.class");
        entryList.add ("jungle/predator/Predator.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        expectedJar.add ("jungle/Animal$AnimalThread.class");
        expectedJar.add ("jungle/Animal.class");
        expectedJar.add ("jungle/JungleMRI.properties");
        expectedJar.add ("jungle/animal.jpg");
        expectedJar.add ("jungle/predator/Predator.class");
        expectedJar.add ("jungle/prey/Prey.class");
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required package.
        Vector entryList = new Vector ();
        entryList.add ("jungle.predator");
        jm.setPackages (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedManifest = new Vector ();
        expectedManifest.add ("jungle/predator/Predator.class");
        expectedManifest.add ("jungle/predator/Predator.java");
        expectedManifest.add ("jungle/predator/puma.jpg");
        Vector expectedJar = JMTest.addDirectoryEntries(expectedManifest);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required package.
        Vector entryList = new Vector ();
        entryList.add ("jungle");
        entryList.add ("jungle.prey");
        jm.setPackages (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedManifest = new Vector ();
        expectedManifest.add ("jungle/Animal.java");
        expectedManifest.add ("jungle/Animal.class");
        expectedManifest.add ("jungle/Animal$AnimalThread.class");
        expectedManifest.add ("jungle/prey/Prey.java");
        expectedManifest.add ("jungle/prey/Prey.class");
        expectedManifest.add ("jungle/animal.jpg");
        expectedManifest.add ("jungle/JungleMRI.properties");
        expectedManifest.add ("jungle/tree.jpg");
        Vector expectedJar = JMTest.addDirectoryEntries(expectedManifest);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
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
      JarMaker jm = new JarMaker ();
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify some additional files.
        Vector addlFiles = new Vector ();
        addlFiles.add (JMTest.ADDITIONAL_FILE_1);
        addlFiles.add (JMTest.ADDITIONAL_FILE_2);
        jm.setAdditionalFiles (addlFiles);
        // We want manifest entries for the additional files.
        //jm.requireAdditionalManifestEntries (true);
        // Specify a required entry.
        Vector entryList = new Vector ();
        entryList.add ("jungle/predator/puma.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        File destJar = jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        expectedJar.add (JMTest.ADDITIONAL_FILE_1_NAME);
        expectedJar.add (JMTest.ADDITIONAL_FILE_2_NAME);
        expectedJar.add ("jungle/predator/puma.jpg");
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.areSameFile (destJar, JMTest.JUNGLE_JAR_SMALL) &&
                JMTest.verifyJar (destJar, expectedJar, expectedManifest));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }





//////////////////////////////////////////////////////////////////////
// Variations for makeJar(File,File) .



/**
 makeJar(File,File) - First arg is null. Should throw an exception.
 **/
    public void Var011 ()
    {
      JarMaker jm = new JarMaker ();
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
      JarMaker jm = new JarMaker ();
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
      JarMaker jm = new JarMaker ();
        File Dir13 = null;
      try {
        Dir13 = new File ("Dir13");
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector ();
        entryList.add ("jungle/animal.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR, Dir13);

        // Verify that only the correct files got copied.
        Vector expectedManifest = entryList;
        Vector expectedJar = JMTest.addDirectoryEntries(entryList);
        assertCondition (JMTest.verifyJar (Dir13, expectedJar, expectedManifest));
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
