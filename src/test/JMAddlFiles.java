///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMAddlFiles.java
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMAddlFiles.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>setAdditionalFiles(Vector)
<li>setAdditionalFiles(Vector,File)
<li>getAdditionalFiles()
</ul>
**/
public class JMAddlFiles
extends Testcase
{
  static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));
  private File sourceJarFile_ = null;
  private File destinationJarFile_ = null;

/**
Constructor.
**/
  public JMAddlFiles (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMAddlFiles",
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



// ---------------------------------------------------------
// Variations for setAdditionalFiles(Vector)

/**
 getAdditionalFiles() - No previous setAdditionalFiles().  Should return empty list.
 **/
    public void Var001 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        Vector outList = jm.getAdditionalFiles ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setAdditionalFiles(Vector) - Verify the setter by using the getter.
 **/
    public void Var002 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        File entry1 = new File ("entry1");
        File entry2 = new File ("entry2");
        File entry3 = new File ("entry3");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        inList.add (entry3);
        jm.setAdditionalFiles (inList);
        Vector outList = jm.getAdditionalFiles ();
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
setAdditionalFiles(Vector,File) - Verify the setter by using the getter.
 **/
    public void Var003 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        File entry1 = new File ("entry1");
        File entry2 = new File ("entry2");
        File entry3 = new File ("entry3");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        inList.add (entry3);
        File baseDir = new File ("baseDir");
        jm.setAdditionalFiles (inList, baseDir);
        Vector outList = jm.getAdditionalFiles ();
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
 setAdditionalFiles(Vector,File) - previously invoked.
 Should augment previous invocation.
 **/
    public void Var004 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        File entry1 = new File ("entry1");
        File entry2 = new File ("entry2");
        File entry3 = new File ("entry3");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        File baseDir = new File ("baseDir");
        jm.setAdditionalFiles (inList, baseDir);
        inList.removeAllElements ();
        inList.add (entry2);
        inList.add (entry3);
        jm.setAdditionalFiles (inList, baseDir);
        Vector outList = jm.getAdditionalFiles ();
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
 setAdditionalFiles(Vector,File) - followed by reset().
 Additional files list should be cleared.
 **/
    public void Var005 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        File entry1 = new File ("entry1");
        File entry2 = new File ("entry2");
        File entry3 = new File ("entry3");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        inList.add (entry3);
        File baseDir = new File ("baseDir");
        jm.setAdditionalFiles (inList, baseDir);
        jm.reset ();
        Vector outList = jm.getAdditionalFiles ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }


/**
 setAdditionalFiles(Vector) - Arg is null.  Should throw an exception.
 **/
    public void Var006 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.setAdditionalFiles (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "fileList");
      }
    }

/**
 setAdditionalFiles(Vector) - List contains a null entry.
 Should throw an exception and terminate.
 **/
    public void Var007 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (null);
        jm.setAdditionalFiles (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "additionalFile");
      }
    }

/**
 setAdditionalFiles(Vector) - List contains an entry that's not a File object.
 Should throw an exception and terminate.
 **/
    public void Var008 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new String ("I am not a file"));
        jm.setAdditionalFiles (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "additionalFile (object of class java.lang.String)");
      }
    }

/**
 setAdditionalFiles(Vector) - List contains no entries.
 No exception is thrown, and no additional files get added.
 **/
    public void Var009 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (0);
        jm.setAdditionalFiles (inList);
        Vector outList = jm.getAdditionalFiles ();
        if (inList.size () != 0)
          failed ("Reported files list is not zero-length.");
        else
        {
          // Remove the destination jar file if it exists.
          JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
          // Make the jar.
          jm.makeJar (JMTest.JUNGLE_JAR);
          // Verify that only the correct files got copied.
          // (We expect all files to have been copied)
          Vector expectedJar = new Vector ();
          JMTest.copyList (JMTest.allJungleFiles_, expectedJar);
          Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
          assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedManifest));
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setAdditionalFiles(Vector) - List contains a file that exists
 and a file that doesn't exist.
 "File not found" is reported by makeJar, and the existing file gets added.
 **/
    public void Var010 ()
    {
      output_.println ("(NOTE TO TESTER: Please ignore any warnings about \"BogusFile\")");
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add (JMTest.ADDITIONAL_FILE_1);
        inList.add (new File ("BogusFile"));
        jm.setAdditionalFiles (inList);
        Vector outList = jm.getAdditionalFiles ();
        if (!JMTest.compareLists (outList, inList))
          failed ("Lists don't match.");
        else
        {
          // Remove the destination jar file if it exists.
          JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
          try {
            // Make the jar.
            jm.makeJar (JMTest.JUNGLE_JAR);
            failed ("Didn't throw exception.");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.io.FileNotFoundException");
          }
/*
          String text = "Was a warning message printed to the command entry window,\n" +
            "indicating that an additional file was not found?";
          TestInstructions instructions =
            new TestInstructions (text, TestInstructions.YES_NO);
          int rc = instructions.display ();
          if (rc != 14)
            failed("No warning message");
          else {
            // Verify that only the correct files got copied.
            // (We expect all files to have been copied)
            Vector expected = new Vector ();
            JMTest.copyList (JMTest.allJungleFiles_, expected);
            expected.add (JMTest.ADDITIONAL_FILE_1_NAME);
            assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expected));
          }
*/
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setAdditionalFiles(Vector) - List contains a file that exists and
 a null entry.
 Should throw a NullPointerException and terminate.
 **/
    public void Var011 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add (JMTest.ADDITIONAL_FILE_1);
        inList.add (null);
        try {
          jm.setAdditionalFiles (inList);
          failed ("Didn't throw exception.");
        }
        catch (Exception e) {
          assertExceptionIs (e, "NullPointerException", "additionalFile");
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setAdditionalFiles(Vector) - List contains two references to the same file.
 Only one copy of the file gets added.
 **/
    public void Var012 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add (JMTest.ADDITIONAL_FILE_1);
        inList.add (JMTest.ADDITIONAL_FILE_1);
        jm.setAdditionalFiles (inList);
        Vector outList = jm.getAdditionalFiles ();
        Vector expected1 = new Vector (1);
        expected1.add (JMTest.ADDITIONAL_FILE_1);
        if (!JMTest.compareLists (outList, expected1))
          failed ("Lists don't match.");
        else
        {
          // Remove the destination jar file if it exists.
          JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
          // Make the jar.
          jm.makeJar (JMTest.JUNGLE_JAR);

          // Verify that only the correct files got copied.
          // (We expect all files to have been copied)
          Vector expectedJar = new Vector ();
          JMTest.copyList (JMTest.allJungleFiles_, expectedJar);
          expectedJar.add (JMTest.ADDITIONAL_FILE_1_NAME);
          Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
          assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedManifest));
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setAdditionalFiles(Vector) - Subsequent invocation with different list of files.
 Second list augments first list.  Verify using getters.
 **/
    public void Var013 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList1 = new Vector (4);
        inList1.add (JMTest.ADDITIONAL_FILE_2);
        inList1.add (JMTest.ADDITIONAL_FILE_1);
        jm.setAdditionalFiles (inList1);
        Vector outList = jm.getAdditionalFiles ();
        if (!JMTest.compareLists (outList, inList1))
          failed ("Lists don't match.");
        else
        {
          Vector inList2 = new Vector (2);
          inList2.add (JMTest.ADDITIONAL_FILE_3);
          jm.setAdditionalFiles (inList2);
          outList = jm.getAdditionalFiles ();
          inList1.add (JMTest.ADDITIONAL_FILE_3);
          if (!JMTest.compareLists (outList, inList1))
            failed ("Lists don't match.");
          else
          {
            // Remove the destination jar file if it exists.
            JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
            // Make the jar.
            jm.makeJar (JMTest.JUNGLE_JAR);
            // Verify that only the correct files got copied.
            // (We expect all files to have been copied)
            Vector expectedJar = new Vector ();
            JMTest.copyList (JMTest.allJungleFiles_, expectedJar);
            expectedJar.add (JMTest.ADDITIONAL_FILE_1_NAME);
            expectedJar.add (JMTest.ADDITIONAL_FILE_2_NAME);
            expectedJar.add ("Trees/");
            expectedJar.add ("Trees/" + JMTest.ADDITIONAL_FILE_3_NAME);
            Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
            assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedManifest));
          }
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }




//----------------------------------------------------------------------
// Variations for setAdditionalFiles(Vector, File)


/**
 setAdditionalFiles(Vector,File) - First arg is null.  Should throw an exception.
 **/
    public void Var014 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.setAdditionalFiles (null, CURRENT_DIR);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "fileList");
      }
    }

/**
 setAdditionalFiles(Vector,File) - Second arg is null.  Should throw an exception.
 **/
    public void Var015 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (JMTest.ADDITIONAL_FILE_1);
        jm.setAdditionalFiles (inList, null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "baseDirectory");
      }
    }

/**
 setAdditionalFiles(Vector,File) - 2nd arg specifies a directory which is
 not the "base directory" for the additional files.
 makeJar() should issue a warning and continue.  The entry names for the
 additional files in the generated jar file will include their entire path.
 **/
    public void Var016 (int runMode)
    {
        notApplicable("Attended testcase");
    }

/**
 setAdditionalFiles(Vector,File) - 2 invocations, with different base dir's.
 All specified additional files should be found and included in the output jar.
 **/
    public void Var017 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add (JMTest.ADDITIONAL_FILE_1);
        inList.add (JMTest.ADDITIONAL_FILE_2);
        jm.setAdditionalFiles (inList, CURRENT_DIR);
        Vector inList2 = new Vector (1);
        inList2.add (JMTest.ADDITIONAL_FILE_3);
        jm.setAdditionalFiles (inList2, new File (CURRENT_DIR, "Trees"));
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);
        // Verify that only the correct files got copied.
        // (We expect all files to have been copied)
        Vector expectedJar = new Vector ();
        JMTest.copyList (JMTest.allJungleFiles_, expectedJar);
        expectedJar.add (JMTest.ADDITIONAL_FILE_1_NAME);
        expectedJar.add (JMTest.ADDITIONAL_FILE_2_NAME);
        expectedJar.add (JMTest.ADDITIONAL_FILE_3_NAME);
        Vector expectedManifest = JMTest.removeDirectoryEntries(expectedJar);
        assertCondition (JMTest.verifyJar (JMTest.JUNGLE_JAR_SMALL, expectedJar, expectedManifest));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }




}



