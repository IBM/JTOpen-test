///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMSplit.java
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
Testcase JMSplit.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>split(File)
<li>split(File,int)
</ul>
**/
@SuppressWarnings("deprecation")
public class JMSplit
extends Testcase
{
  static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));
  static final boolean DEBUG = false;

/**
Constructor.
**/
  public JMSplit (AS400 systemObject,
                   Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMSplit",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
      JMTest.deleteSplitFiles (CURRENT_DIR, "jungle", ".jar");
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      if (!DEBUG)
        JMTest.deleteSplitFiles (CURRENT_DIR, "jungle", ".jar");
    }


  /**
   Generates a destination jar file name,
   based on the name of the source jar file.
   Inserts the specified suffix, before the final ".".

   @param sourceJarFile The source jar file.
   @param suffix The suffix to append to the name.
   @return A destination jar file.
   **/
  static File setupSplitJarFile (File sourceJarFile, int suffix)
  {
    String sourceJarName = sourceJarFile.getName ();
    String destinationJarName;
    int index = sourceJarName.lastIndexOf ('.');
    if (index == -1)
      destinationJarName = sourceJarName + suffix;
    else
      destinationJarName = sourceJarName.substring (0, index)
        + Integer.toString (suffix) + sourceJarName.substring (index);
    // Put it in the current directory.
    return new File (CURRENT_DIR, destinationJarName);
  }



//----------------------------------------------------------------------
// Variations for split(File) .


/**
 split(File) - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.split (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "sourceJarFile");
      }
    }

/**
 split(File) - Jar file doesn't exist. Should throw an exception.
 **/
    public void Var002 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        File bogusFile = new File ("bogus");  // a nonexistent file
        jm.split (bogusFile);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "FileNotFoundException");
      }
    }

/**
 split(File) - Source file is smaller than 2 megabytes.
 Should simply copy the source jar file to a single
 destination jar file in the current directory.
 **/
    @SuppressWarnings("unchecked")
    public void Var003 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove any existing split target files.
        JMTest.deleteSplitFiles (CURRENT_DIR, "jungle", ".jar");

        // Split the files.
        Vector<File> jarList = jm.split (JMTest.JUNGLE_JAR);

        if (DEBUG) {
          output_.println ("Resulting jar files:");
          Enumeration<File> e = jarList.elements ();
          while (e.hasMoreElements ()) {
            File file = (File)e.nextElement ();
            output_.println (file.getAbsolutePath ());
          }
        }

        assertCondition (JMTest.validateJars (JMTest.JUNGLE_JAR, jarList));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }


//-----------------------------------------------------------------------
// Variations for split(File,int) .


/**
 split(File) - Arg is zero. Should throw an exception.
 **/
    public void Var004 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.split (JMTest.JUNGLE_JAR, 0);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "splitSizeKbytes (0)");
      }
    }


/**
 split(File) - Arg is negative. Should throw an exception.
 **/
    public void Var005 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.split (JMTest.JUNGLE_JAR, -1);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "splitSizeKbytes (-1)");
      }
    }


/**
 split(File,30) - Should split the jar file
 into jar files that are smaller than 30 kilobytes.
 The split files should get created the current directory.
 **/
    @SuppressWarnings("unchecked")
    public void Var006 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        // Remove any existing split target files.
        JMTest.deleteSplitFiles (CURRENT_DIR, "jungle", ".jar");

        // Split the files.
        Vector<File> jarList = jm.split (JMTest.JUNGLE_JAR, 30);

        if (DEBUG) {
          output_.println ("Resulting jar files:");
          Enumeration<File> e = jarList.elements ();
          while (e.hasMoreElements ()) {
            File file = (File)e.nextElement ();
            output_.println (file.getAbsolutePath ());
          }
        }

        assertCondition (JMTest.validateJars (JMTest.JUNGLE_JAR, jarList, 30));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 split(File,27) - Should split the jar file
 into jar files that are smaller than 27 kilobytes.
 One of the entries in the source jar is larger than 27K,
 so we should see a warning.
 The split files should get created the current directory.
 **/
    public void Var007 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

}



