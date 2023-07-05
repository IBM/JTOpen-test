///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMPackage.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import utilities.*;
import com.ibm.as400.access.AS400;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMPackage.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>getPackages()
<li>setPackages(Vector)
</ul>
**/
public class JMPackage
extends Testcase
{


/**
Constructor.
**/
  public JMPackage (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMPackage",
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


/**
 getPackages() - No previous setPackages().  Should return empty list.
 **/
public void Var001 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        Vector outList = jm.getPackages ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setPackages() - Arg is null. Should throw exception.
 **/
public void Var002 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.setPackages (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException");
      }
    }

/**
 setPackages() - List contains a null entry. Should throw exception.
 **/
public void Var003 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add ("booga");
        inList.add (null);
        jm.setPackages (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException");
      }
    }

/**
setPackages() - Verify the setter by using the getter.
 **/
public void Var004 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("com.ibm.as400");
        String entry2 = new String ("mypackage");
        String entry3 = new String ("java.somespecialpackage");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        inList.add (entry3);
        jm.setPackages (inList);
        Vector outList = jm.getPackages ();
        assertCondition ((outList.size () == 3) &&
                (outList.contains (entry1)) &&
                (outList.contains (entry2)) &&
                (outList.contains (entry3)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setPackages() - Previous invocation. Should augment previous invocation.
 **/
public void Var005 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("com.ibm.as400");
        String entry2 = new String ("mypackage");
        String entry3 = new String ("java.somespecialpackage");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        jm.setPackages (inList);
        inList.removeAllElements ();
        inList.add (entry2);
        inList.add (entry3);
        jm.setPackages (inList);
        Vector outList = jm.getPackages ();
        assertCondition ((outList.size () == 3) &&
                (outList.contains (entry1)) &&
                (outList.contains (entry2)) &&
                (outList.contains (entry3)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setPackages() - Single package.  Verify by analyzing output of makeJar().
 **/
public void Var006 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("jungle");
        Vector inList = new Vector (1);
        inList.add (entry1);
        jm.setPackages (inList);
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedManifest = new Vector ();
        expectedManifest.add ("jungle/Animal.java");
        expectedManifest.add ("jungle/Animal.class");
        expectedManifest.add ("jungle/Animal$AnimalThread.class");
        expectedManifest.add ("jungle/animal.jpg");
        expectedManifest.add ("jungle/JungleMRI.properties");
        expectedManifest.add ("jungle/tree.jpg");
        Vector expectedJar = JMTest.addDirectoryEntries(expectedManifest);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedManifest));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setPackages() - Bogus package.  Should throw a ZipException.
 **/
public void Var007 ()
    {
      output_.println ("(NOTE TO TESTER: Please ignore warning about package 'bogus'.)");
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("bogus");
        Vector inList = new Vector (1);
        inList.add (entry1);
        jm.setPackages (inList);
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        jm.makeJar (JMTest.JUNGLE_JAR);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.util.zip.ZipException");
      }
    }

/**
setPackages() - Two packages.  Verify by analyzing output of makeJar().
 **/
public void Var008 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("jungle");
        String entry2 = new String (" jungle.predator ");
        Vector inList = new Vector (1);
        inList.add (entry1);
        inList.add (entry2);
        jm.setPackages (inList);
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        jm.makeJar (JMTest.JUNGLE_JAR);

        // Verify that only the correct files got copied.
        Vector expectedManifest = new Vector ();
        expectedManifest.add ("jungle/Animal.java");
        expectedManifest.add ("jungle/Animal.class");
        expectedManifest.add ("jungle/Animal$AnimalThread.class");
        expectedManifest.add ("jungle/animal.jpg");
        expectedManifest.add ("jungle/JungleMRI.properties");
        expectedManifest.add ("jungle/tree.jpg");
        expectedManifest.add ("jungle/predator/Predator.class");
        expectedManifest.add ("jungle/predator/Predator.java");
        expectedManifest.add ("jungle/predator/puma.jpg");
        Vector expectedJar = JMTest.addDirectoryEntries(expectedManifest);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedManifest));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setPackages() - Two packages, one of which is bogus.
Should throw a ZipException.
 **/
public void Var009 ()
    {
      output_.println ("(NOTE TO TESTER: Please ignore warning about package 'jungle.bogus'.)");
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("jungle");
        String entry2 = new String ("jungle.bogus");
        Vector inList = new Vector (1);
        inList.add (entry1);
        inList.add (entry2);
        jm.setPackages (inList);
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        jm.makeJar (JMTest.JUNGLE_JAR);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.util.zip.ZipException");
      }
    }

/**
setPackages() - reset(). Should clear the required packages list.
 **/
public void Var010 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("com.ibm.as400");
        String entry2 = new String ("mypackage");
        String entry3 = new String ("java.somespecialpackage");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        jm.setPackages (inList);
        jm.reset ();
        Vector outList = jm.getPackages ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setPackages() - Single package.  Have the tester verify that only files from the specified package got included.
 **/
public void Var011 (int runMode)
    {
	notApplicable("Attended testcase");
	    }

}



