///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMLang.java
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
Testcase JMLang.  This tests the following
methods of the ToolboxJarMaker class:

<ul compact>
<li>getLanguageDirectory
<li>getLanguages
<li>setLanguageDirectory
<li>setLanguages
</ul>
**/

@SuppressWarnings("deprecation")
public class JMLang
extends Testcase
{
  static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));
  Vector<String> allLangIDs_;  // list of all recognized language ID's

/**
Constructor.
**/
    public JMLang (AS400 systemObject,
                   Hashtable<String, Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMLang",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
      // Remove the generated jar file if it exists.
      JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);

     Vector<String> c = new Vector<String> (16);

     c.add ("de_CH");
     c.add ("de");
     c.add ("es");
     c.add ("fr_BE");
     c.add ("fr_CA");
     c.add ("fr_CH");
     c.add ("fr");
     c.add ("it_CH");
     c.add ("it");
     c.add ("ja");
     c.add ("ko");
     c.add ("zh_TW");
     c.add ("zh");
     c.add ("cs");
     c.add ("hu");
     c.add ("pl");

     allLangIDs_ = new Vector<String> (c.size ());
     JMTest.copyStringList (c, allLangIDs_);
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      // Remove the generated jar file if it exists.
      JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
    }


/**
 setLanguages - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.setLanguages (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "languageList");
      }
    }

/**
 setLanguageDirectory - Arg is null. Should throw an exception.
 **/
    public void Var002 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.setLanguageDirectory (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "baseDirectory");
      }
    }

/**
 setLanguages - List contains a null entry. Should throw an exception.
 **/
    public void Var003 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<?> inList = new Vector<Object> (1);
        inList.add (null);
        jm.setLanguages (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
      }
    }

/**
 setLanguages - List contains an entry with invalid syntax. Should throw an exception.
 **/
    public void Var004 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<String> inList = new Vector<String> (1);
        inList.add ("*");
        jm.setLanguages (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException", "language (*)");
      }
    }

/**
 getLanguages - No prior setLanguages. Should return an empty list.
 **/
    public void Var005 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<?> outList = jm.getLanguages ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 getLanguageDirectory - No prior setLanguageDirectory. Should return current directory.
 **/
    public void Var006 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        File langDir = jm.getLanguageDirectory ();
        assertCondition (langDir.equals (CURRENT_DIR));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setLanguages - Zero-length list. getLanguages should return an empty list.
 **/
    public void Var007 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<?> inList = new Vector<Object> ();
        jm.setLanguages (inList);
        Vector<?> outList = jm.getLanguages ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setLanguages - List contains a non-String entry. Should throw an exception.
 **/
    public void Var008 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<File> inList = new Vector<File> (1);
        inList.add (new File ("MRI_en_US.properties"));
        jm.setLanguages (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "language (object of class java.io.File)");
      }
    }

/**
 setLanguages - List contains an unrecognized language ID.
 The unrecognized language ID should still get added to the list.
 **/
    public void Var009 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<String> inList = new Vector<String> (1);
        inList.add ("zh_tw");
        inList.add ("xx_yy");
        output_.println ("(NOTE TO TESTER: Please ignore warning about " +
                         "unsupported language ID xx_yy)");
        jm.setLanguages (inList);
        Vector<?> outList = jm.getLanguages ();
        assertCondition ((outList.size () == 2) &&
                outList.contains ("zh_TW") &&
                outList.contains ("xx_YY"));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setLanguages - List contains an unrecognized language ID.
 Should print a warning but add the unrecognized language ID to the list.
 **/
    public void Var010 (int runMode)
    {
        notApplicable("Attended testcase");
    }

/**
 setLanguages - Verify using getLanguages().
 **/
    @SuppressWarnings("unchecked")
    public void Var011 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<String> inList = new Vector<String> (allLangIDs_.size ());
        JMTest.copyStringList (allLangIDs_, inList);
        jm.setLanguages (inList);
        Vector<String> outList = jm.getLanguages ();
        assertCondition (JMTest.compareStringLists (outList, inList,"outList","inList"));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setLanguageDirectory - Verify using getLanguageDirectory().
 **/
    public void Var012 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        File langDir = new File (CURRENT_DIR, "Lang");
        jm.setLanguageDirectory (langDir);
        assertCondition (jm.getLanguageDirectory ().equals (langDir));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setLanguages - List contains two supported languages.
 Only the files needed for the specified languages should get included.
 **/
    public void Var013 ()
    {
      printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<String> inList = new Vector<String> (allLangIDs_.size ());
        inList.add ("fr_CA");
        inList.add ("_zh_TW_");
        jm.setLanguageDirectory (JMTest.LANGUAGE_DIR);
        jm.setLanguages (inList);
        // Make a jar for the TRACE component.
        Vector<Integer> compList = new Vector<Integer> (1);
        compList.add (ToolboxJarMaker.TRACE);
        jm.setComponents (compList);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.

        Vector<String> expectedJar = new Vector<String> (20);
        expectedJar.add ("com/ibm/as400/access/Trace.class");
        expectedJar.add ("com/ibm/as400/access/Copyright.class");
        expectedJar.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        expectedJar.add ("com/ibm/as400/access/ReturnCodeException.class");
        expectedJar.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        expectedJar.add ("com/ibm/as400/access/SystemProperties.class");
        expectedJar.add ("com/ibm/as400/access/ToolboxLogger.class");
        expectedJar.add ("com/ibm/as400/access/MRI.class");
        expectedJar.add ("com/ibm/as400/access/MRI_fr_CA.class");
        expectedJar.add ("com/ibm/as400/access/MRI_zh_TW.class");
        expectedJar.add ("com/ibm/as400/access/MRI2.class");
        expectedJar.add ("com/ibm/as400/access/MRI2_fr_CA.class");
        expectedJar.add ("com/ibm/as400/access/MRI2_zh_TW.class");
        expectedJar.add ("com/ibm/as400/access/SVMRI.class");
        expectedJar.add ("com/ibm/as400/access/SVMRI_fr_CA.class");
        expectedJar.add ("com/ibm/as400/access/SVMRI_zh_TW.class");
        expectedJar.add ("com/ibm/as400/access/CoreMRI.class");
        expectedJar.add ("com/ibm/as400/access/CoreMRI_fr_CA.class");
        expectedJar.add ("com/ibm/as400/access/CoreMRI_zh_TW.class");
        expectedJar = JMTest.addDirectoryEntries(expectedJar);

        Vector<String> expectedManifest = new Vector<String> (10);
        expectedManifest.add ("com/ibm/as400/access/");
        expectedManifest.add ("com/ibm/as400/access/MRI_fr_CA.class");
        expectedManifest.add ("com/ibm/as400/access/MRI_zh_TW.class");
        expectedManifest.add ("com/ibm/as400/access/MRI2_fr_CA.class");
        expectedManifest.add ("com/ibm/as400/access/MRI2_zh_TW.class");
        expectedManifest.add ("com/ibm/as400/access/SVMRI_fr_CA.class");
        expectedManifest.add ("com/ibm/as400/access/SVMRI_zh_TW.class");
        expectedManifest.add ("com/ibm/as400/access/CoreMRI_fr_CA.class");
        expectedManifest.add ("com/ibm/as400/access/CoreMRI_zh_TW.class");

        assertCondition (JMTest.verifyJar (JMTest.TOOLBOX_JAR_SMALL, expectedJar, expectedManifest, true));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }


}
