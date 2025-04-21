///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMComp.java
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

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMComp.  This tests the following
methods of the ToolboxJarMaker class:

<ul compact>
<li>getComponents
<li>setComponents
</ul>
**/
@SuppressWarnings("deprecation")
public class JMComp
extends Testcase
{

/**
Constructor.
**/
    public JMComp (AS400 systemObject,
                   Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMComp",
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


/**
 setComponents - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.setComponents (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "componentList");
      }
    }

/**
 setComponents - List contains a null entry. Should throw an exception.
 **/
    public void Var002 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (1);
        inList.add (null);
        jm.setComponents (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "component");
      }
    }

/**
 getComponents - No prior setComponents. Should return an empty list.
 **/
    public void Var003 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<?> outList = jm.getComponents ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - Zero-length list. getComponents should return an empty list.
 **/
    public void Var004 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> ();
        jm.setComponents (inList);
        Vector<?> outList = jm.getComponents ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - List contains a non-Integer entry. Should throw an exception.
 **/
    public void Var005 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (1);
        inList.add ("12345");  // String instead of Integer
        jm.setComponents (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "component (object of class java.lang.String)");
      }
    }

/**
 setComponents - List contains an invalid component number. Should throw an exception.
 **/
    public void Var006 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (1);
        inList.add (new Integer (999));
        jm.setComponents (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException", "component (999)");
      }
    }

/**
 setComponents - Verify using getComponents().
 **/
    public void Var007 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (2);
        inList.add (ToolboxJarMaker.PROGRAM_CALL);
        inList.add (ToolboxJarMaker.JDBC);
        inList.add (ToolboxJarMaker.COMMAND_CALL);
        jm.setComponents (inList);
        Vector<?> outList = jm.getComponents ();
        assertCondition ((outList.size () == 3) &&
                outList.contains (ToolboxJarMaker.PROGRAM_CALL) &&
                outList.contains (ToolboxJarMaker.JDBC) &&
                outList.contains (ToolboxJarMaker.COMMAND_CALL));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - with BeanInfo option false. Verify using getComponents().
 **/
    public void Var008 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Integer> inList = new Vector<Integer> (2);
        inList.add (ToolboxJarMaker.PROGRAM_CALL);
        inList.add (ToolboxJarMaker.JDBC);
        inList.add (ToolboxJarMaker.COMMAND_CALL);
        jm.setComponents (inList, false);
        Vector<?> outList = jm.getComponents ();
        assertCondition ((outList.size () == 3) &&
                outList.contains (ToolboxJarMaker.PROGRAM_CALL) &&
                outList.contains (ToolboxJarMaker.JDBC) &&
                outList.contains (ToolboxJarMaker.COMMAND_CALL));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - with BeanInfo option true. Verify using getComponents().
 **/
    public void Var009 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Integer> inList = new Vector<Integer> (2);
        inList.add (ToolboxJarMaker.PROGRAM_CALL);
        inList.add (ToolboxJarMaker.JDBC);
        inList.add (ToolboxJarMaker.COMMAND_CALL);
        jm.setComponents (inList, true);
        Vector<?> outList = jm.getComponents ();
        assertCondition ((outList.size () == 3) &&
                outList.contains (ToolboxJarMaker.PROGRAM_CALL) &&
                outList.contains (ToolboxJarMaker.JDBC) &&
                outList.contains (ToolboxJarMaker.COMMAND_CALL));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - List contains an existing component and a nonexistent component.
 Should throw an exception.
 **/
    public void Var010 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (2);
        inList.add (ToolboxJarMaker.PROGRAM_CALL);
        inList.add (new Integer (999));
        jm.setComponents (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException", "component (999)");
      }
    }

/**
 setComponents - List contains one component.
 Only the files needed for the specified component should get included.
 **/
    public void Var011 ()
    {
      printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (1);
        inList.add (ToolboxJarMaker.TRACE);
        jm.setComponents (inList);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.
        Vector<String> expectedJar = new Vector<String> ();
        expectedJar.add ("com/ibm/as400/access/Trace.class");
        expectedJar.add ("com/ibm/as400/access/Copyright.class");
        expectedJar.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        expectedJar.add ("com/ibm/as400/access/ReturnCodeException.class");
        expectedJar.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        expectedJar.add ("com/ibm/as400/access/CoreMRI.class");
        expectedJar.add ("com/ibm/as400/access/MRI.class");
        expectedJar.add ("com/ibm/as400/access/MRI2.class");
        expectedJar.add ("com/ibm/as400/access/SVMRI.class");
        expectedJar.add ("com/ibm/as400/access/SVMRI_en.class");
        expectedJar.add ("com/ibm/as400/access/ToolboxLogger.class");
        expectedJar.add ("com/ibm/as400/access/SystemProperties.class");
        expectedJar = JMTest.addDirectoryEntries(expectedJar);
        Vector<String> expectedManifest = new Vector<String> ();
        expectedManifest.add ("com/ibm/as400/access/");
        assertCondition (JMTest.verifyJar (JMTest.TOOLBOX_JAR_SMALL, expectedJar, expectedManifest, true));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - List contains two existing Components.
 Only the files needed for the specified components should get included.
 **/
    public void Var012 ()
    {
      printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector<Comparable<?>> inList = new Vector<Comparable<?>> (2);
        inList.add (ToolboxJarMaker.TRACE);
        inList.add (ToolboxJarMaker.USER_VISUAL);
        jm.setComponents (inList);
        jm.setExcludeSomeDependencies(true);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);
        // Verify that only the correct files got copied.
        Vector<String> expected = new Vector<String> (20);
        // Files to expect for TRACE component:
        expected.add ("com/ibm/as400/access/Trace.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        expected.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        expected.add ("com/ibm/as400/access/InternalErrorException.class");
        expected.add ("com/ibm/as400/access/ReturnCodeException.class");
        expected.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        expected.add ("com/ibm/as400/access/MRI.class");
        // Some files to expect for USER_VISUAL component:
        expected.add ("com/ibm/as400/vaccess/VUserList.class");
        expected.add ("com/ibm/as400/access/User.class");
        expected.add ("com/ibm/as400/access/UserList.class");
        expected.add ("com/ibm/as400/vaccess/AS400ExplorerPane.class");
        expected.add ("com/ibm/as400/vaccess/AS400ListPane.class");
        expected.add ("com/ibm/as400/access/AS400Server.class");
        expected.add ("com/ibm/as400/access/DataStream.class");
        expected.add ("com/ibm/as400/access/ProgramCall.class");
        expected.add ("com/ibm/as400/vaccess/VMRI.class");

        // Files that should NOT get included:
        Vector<String> notExpected = new Vector<String> (10);
        notExpected.add ("com/ibm/as400/vaccess/VUserListBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/AS400JDBCCallableStatement.class");
        notExpected.add ("com/ibm/as400/access/AS400Certificate.class");
        notExpected.add ("com/ibm/as400/access/BaseDataQueue.class");
        notExpected.add ("com/ibm/as400/access/JDMRI.class");
        notExpected.add ("com/ibm/as400/vaccess/SQLStatementButton16.gif");
        notExpected.add ("com/ibm/as400/vaccess/CommandCallButton.class");

        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          expected, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setComponents - List contains one component in the 'access' package.
 All of the files needed for the specified component should get included.
 **/
    public void Var013 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

/**
 setComponents - List contains one component that has PCML files.
 All of the files needed for the specified component should get included.
 **/
    public void Var014 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

}
