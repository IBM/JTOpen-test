///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMSetGet.java
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

import test.Testcase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMSetGet.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>getRequiredFiles()
<li>reset()
<li>setRequiredFiles(Vector)
</ul>
**/

public class JMSetGet
extends Testcase
{


/**
Constructor.
**/
  public JMSetGet (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMSetGet",
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


// getRequiredFiles, setRequiredFiles

/**
 getRequiredFiles() - no previous setRequiredFiles(). Should return empty list.
 **/
    public void Var001 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        Vector outList = jm.getRequiredFiles ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setRequiredFiles() - arg is null. Should throw exception.
 **/
    public void Var002 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        jm.setRequiredFiles (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
    }

/**
 setRequiredFiles() - List contains a null entry. Should throw exception.
 **/
    public void Var003 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add ("booga");
        inList.add (null);
        jm.setRequiredFiles (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
    }

/**
 setRequiredFiles() - List contains a zero-length entry.
 Should throw an IllegalArgumentException.
 **/
    public void Var004 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add ("booga");
        inList.add ("");
        jm.setRequiredFiles (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
      }
    }

/**
 setRequiredFiles() - List contains a non-String entry.
 Should throw an IllegalArgumentException.
 **/
    public void Var005 ()
    {
      JarMaker jm = new JarMaker ();
      try {
        Vector inList = new Vector (2);
        inList.add (new File("booga"));
        jm.setRequiredFiles (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
      }
    }

/**
setRequiredFiles() - Verify the setter by using the getter.
 **/
    public void Var006 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("com/ibm/as400/entry1");
        String entry2 = new String ("entry2.gif");
        String entry3 = new String (" java/entry3.properties ");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        inList.add (entry3);
        jm.setRequiredFiles (inList);
        Vector outList = jm.getRequiredFiles ();
        assertCondition ((outList.size () == 3) &&
                (outList.contains (entry1)) &&
                (outList.contains (entry2)) &&
                (outList.contains (new String ("java/entry3.properties"))));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setRequiredFiles() - previous invocation. Should augment previous invocation.
 **/
    public void Var007 ()
    {
      try {
        JarMaker jm = new JarMaker ();
        String entry1 = new String ("com/ibm/as400/entry1");
        String entry2 = new String ("entry2.gif");
        String entry3 = new String ("java/entry3.properties");
        Vector inList = new Vector ();
        inList.add (entry1);
        inList.add (entry2);
        jm.setRequiredFiles (inList);
        inList.removeAllElements ();
        inList.add (entry2);
        inList.add (entry3);
        jm.setRequiredFiles (inList);
        Vector outList = jm.getRequiredFiles ();
        assertCondition ((outList.size () == 3) &&
                (outList.contains (entry1)) &&
                (outList.contains (entry2)) &&
                (outList.contains (entry3)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }


// reset

/**
 reset() - Should reset the JarMaker object to the default, just-created state.
 Verify by using getters.
 **/
    public void Var008 ()
    {
      try {
        JarMaker jm = new JarMaker ();

        File file1 = new File ("file1");
        Vector fileList = new Vector ();
        fileList.add (file1);
        File baseDir = new File ("baseDir");
        jm.setAdditionalFiles (fileList, baseDir);

        String entry1 = new String ("com/ibm/as400/entry1");
        Vector entryList = new Vector ();
        entryList.add (entry1);
        jm.setRequiredFiles (entryList);

        String pkg1 = new String ("com.ibm.as400");
        Vector pkgList = new Vector ();
        pkgList.add (pkg1);
        jm.setPackages (pkgList);

        jm.reset ();

        assertCondition ( (jm.getAdditionalFiles ().size () == 0) &&
                 (jm.getRequiredFiles ().size () == 0) &&
                 (jm.getPackages ().size () == 0));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }


// setVerbose()

/**
 setVerbose() - Should cause progress messages to be printed.
 **/
    public void Var009 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

}
