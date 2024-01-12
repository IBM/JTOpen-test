///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMTBParseArgs.java
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;



/**
Testcase JMTBParseArgs.  This tests the command-line invocation mode
of class ToolboxJarMaker.
**/
public class JMTBParseArgs
extends Testcase
{
  private static final boolean DEBUG = false;

  static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));
  static final String CLASSPATH = System.getProperty ("java.class.path");
  private File sourceJarFile_ = null;
  private File destinationJarFile_ = null;
  private boolean windows_ = false;
  private boolean DOS_ = false;
  private boolean OS2_ = false;
  private boolean OS400_ = false;
  private String infoOutput_;
  private String errorOutput_;

/**
Constructor.
**/
  public JMTBParseArgs (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMTBParseArgs",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
      if (!JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL))
        output_.println ("Warning: Testcase setup failed to delete " +
                         JMTest.JUNGLE_JAR_SMALL.getAbsolutePath ());

      // Determine operating system we're running under
      String operatingSystem_ = System.getProperty("os.name");
      if (operatingSystem_.indexOf("OS/2") >= 0)
      {
        OS2_ = true;
        DOS_ = true;
      }
      else if (operatingSystem_.indexOf("Windows") >= 0)
      {
        windows_ = true;
        DOS_ = true;
      }
      else if (operatingSystem_.indexOf("DOS") >= 0)
      {
        DOS_ = true;
      }
      else if (operatingSystem_.indexOf("OS/400") >= 0)
      {
          OS400_ = true;
      }

      output_.println("Running under: " + operatingSystem_);
      output_.println("DOS-based file structure: " + DOS_);
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
    }



/**
Calls ToolboxJarMaker, as a separate process.
This simulates a command-line invocation.
@return exit value from the subprocess in which JarMaker was run
 @exception  Exception  If an exception occurs.
**/
    private int callJarMaker (String args)
        throws Exception
    {
      Process prc = null;
      int exitVal = 999999;
      // Note: For some reason, in JDK 1.1.6 we need to specify classpath
      // explicitly on the java invocation.
      String command = "java -classpath " + CLASSPATH +
        " utilities.ToolboxJarMaker " + args;

      Runtime rt = Runtime.getRuntime ();
      prc = rt.exec (command);

      // Workaround for the pipe overflow bug on Windows platforms.
      // Java bug: 4062587;  and followup: 4098442
        InputStream inStream = prc.getInputStream();
        InputStream errStream = prc.getErrorStream();
        StringBuffer infoText = new StringBuffer();
        StringBuffer errorText = new StringBuffer();
        boolean finished = false; // Set to true when p is finished
        while (!finished) {
          try {
            while (inStream.available () > 0) {
              // Print the output of our system call.
              char c = (char)inStream.read ();
              infoText.append (c);
            }
            while (errStream.available () > 0) {
              // Print the output of our system call.
              char c = (char)errStream.read ();
              errorText.append (c);
            }
            // Ask the process for its exitValue. If the process
            // is not finished, an IllegalThreadStateException
            // is thrown. If it is finished, we fall through and
            // the variable finished is set to true.
            exitVal = prc.exitValue();
            finished = true;
          }
          catch (IllegalThreadStateException e) {
            // Sleep a little to save on CPU cycles
            try {Thread.currentThread().sleep(500);} catch (Exception e1){}
          }
        }
        infoOutput_  = infoText.toString ();
        errorOutput_ = errorText.toString ();

      return exitVal;
    }


    private void printErrors ()
    {
      if (errorOutput_.length () != 0) {
        if (isApplet_)
          output_.println (errorOutput_);
        else
          System.err.println (errorOutput_);
      }
    }

    private void printInfo ()
    {
      if (infoOutput_.length () != 0) {
        if (isApplet_)
          output_.println (infoOutput_);
        else
          System.out.println (infoOutput_);
      }
    }

/**
 No options specified.  Should complain and quit.
 **/
    public void Var001 ()
    {
      try {
        // Make the jar.
        String args = "";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Error: No options were specified."));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 Invalid tag specified.  Should complain and quit.
 **/
    public void Var002 ()
    {
      try {
        // Make the jar.
        String args = "-bogusTag bogusValue";
        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          System.out.println("infoOutput_: |" + infoOutput_ + "|");
          System.out.println("errorOutput_: |" + errorOutput_ + "|; exitVal==" + exitVal);
        }
        String expectedError  = "Error: Unrecognized option: -bogusTag";
        String expectedError2 = "Error: Unrecognized option: -bogustag";
        assertCondition (exitVal == 1 &&
                (-1 != errorOutput_.indexOf (expectedError) ||
                 -1 != errorOutput_.indexOf (expectedError2)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -source - No argument specified.  Should complain and quit.
 **/
    public void Var003 ()
    {
      try {
        // Make the jar.
        String args = "-source";
        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          System.out.println("infoOutput_: |" + infoOutput_ + "|");
          System.out.println("errorOutput_: |" + errorOutput_ + "|; exitVal==" + exitVal);
        }
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No file specified after -source."));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -source - Nonexistent jar file specified.  Should complain and quit.
 **/
    public void Var004 ()
    {
      try {
        // Make the jar.
        String args = "-source BogusJar.jar -package bogus";
        int exitVal = callJarMaker (args); // invoke jarmaker
        String jarPath = CURRENT_DIR.getAbsolutePath() +
          JMTest.FILE_SEPARATOR_STRING + "BogusJar.jar";
        String expectedError = "java.io.FileNotFoundException: " + jarPath;
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf (expectedError));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -source - Empty jar file specified.  Should complain and quit.
 **/
    public void Var005 ()
    {
      File emptyJar = null;
      try {
        // Make the jar.
        emptyJar = new File (CURRENT_DIR, "emptyJar.jar");
        JMTest.createFile (emptyJar);
        String args = "-source emptyJar.jar -package bogus";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("ZipException"));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally {
        if (emptyJar != null) JMTest.deleteFile (emptyJar);
      }
    }

/**
 -source - Invalid jar file specified.  Should complain and quit.
 **/
    public void Var006 ()
    {
      try {
        // Make the jar.
        String args = "-source " + JMTest.ADDITIONAL_FILE_1_NAME +
          " -package bogus";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("ZipException"));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -destination - No argument specified.
 Should issue warning and quit.
 **/
    public void Var007 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -destination";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No file specified after -destination.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -requiredFile - No argument specified.  Should complain and quit.
 **/
    public void Var008 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -requiredFile";
        int exitVal = callJarMaker (args); // invoke jarmaker
        String expectedError  = "Warning: No file specified after -requiredFile.";
        String expectedError2 = "Warning: No file specified after -fileRequired.";
        assertCondition (exitVal == 1 &&
                         !JMTest.JUNGLE_JAR_SMALL.exists () &&
                         (-1 != errorOutput_.indexOf (expectedError) ||
                          -1 != errorOutput_.indexOf (expectedError2)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -component - No argument specified.  Should complain and quit.
 **/
    public void Var009 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -component";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No component specified") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -component - Invalid argument specified.  Should complain and quit.
 **/
    public void Var010 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -component BogusComponent";
        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          System.out.println("infoOutput_: |" + infoOutput_ + "|");
          System.out.println("errorOutput_: |" + errorOutput_ + "|; exitVal==" + exitVal);
        }
        String expectedError  = "Error: Invalid component name: BogusComponent";
        String expectedError2 = "Error: Unrecognized component name: BogusComponent";
        assertCondition (exitVal == 1 &&
                         !JMTest.JUNGLE_JAR_SMALL.exists () &&
                         (-1 != errorOutput_.indexOf (expectedError) ||
                          -1 != errorOutput_.indexOf (expectedError2)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -package - No argument specified.  Should complain and quit.
 **/
    public void Var011 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -package";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No package specified after -package.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -additionalFile - No argument specified.  Should complain and quit.
 **/
    public void Var012 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -additionalFile";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No file specified after -additionalFile.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -additionalFilesDirectory - No argument specified.  Should complain and quit.
 **/
    public void Var013 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -additionalFilesDirectory";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No directory specified after -additionalFilesDirectory.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -split - Non-integer argument specified.  Should complain and quit.
 **/
    public void Var014 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -split Bogus1";
        int exitVal = callJarMaker (args); // invoke jarmaker
        String expectedError = "Error: Non-integer split size: Bogus1";
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf (expectedError) &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -split - Negative integer argument specified.  Should complain and quit.
 **/
    public void Var015 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -split -456";
        int exitVal = callJarMaker (args); // invoke jarmaker
        String expectedError  = "Error: Unrecognized option: -456";
        String expectedError2 = "Error: Negative split size: -456";
        if (DEBUG) {
          System.out.println("infoOutput_: |" + infoOutput_ + "|");
          System.out.println("errorOutput_: |" + errorOutput_ + "|; exitVal==" + exitVal);
        }
        assertCondition (exitVal == 1 &&
                         !JMTest.JUNGLE_JAR_SMALL.exists () &&
                         (-1 != errorOutput_.indexOf (expectedError) ||
                          -1 != errorOutput_.indexOf (expectedError2)));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -language - No argument specified.  Should complain and quit.
 **/
    public void Var016 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -language";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No language specified after -language.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -languageDirectory - No argument specified.  Should complain and quit.
 **/
    public void Var017 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -languageDirectory";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No directory specified after -languageDirectory.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -ccsid - No argument specified.  Should complain and quit.
 **/
    public void Var018 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -ccsid";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No CCSID specified after -ccsid.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -ccsid - Non-integer argument specified.  Should complain and quit.
 **/
    public void Var019 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -ccsid Bogus1, Bogus2";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Error: Non-integer CCSID value: Bogus1") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -ccsid - Negative integer argument specified.  Should complain and quit.
 **/
    public void Var020 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -ccsid 835,-456";
        int exitVal = callJarMaker (args); // invoke jarmaker
        String expectedError = "java.lang.IllegalArgumentException: CCSID (-456)";
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf (expectedError) &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -ccsidExcluded - No argument specified.  Should complain and quit.
 **/
    public void Var021 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -ccsidExcluded";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Warning: No CCSID specified after -ccsidExcluded.") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -ccsidExcluded - Non-integer argument specified.  Should complain and quit.
 **/
    public void Var022 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -ccsidExcluded Boogie";
        int exitVal = callJarMaker (args); // invoke jarmaker
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf ("Error: Non-integer CCSID value: Boogie") &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -ccsidExcluded - Negative integer argument specified.  Should complain and quit.
 **/
    public void Var023 ()
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Make the jar.
        String args = " -source jungle.jar -ccsidExcluded 835,-456";
        int exitVal = callJarMaker (args); // invoke jarmaker
        String expectedError = "java.lang.IllegalArgumentException: CCSID (-456)";
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf (expectedError) &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }



/**
 Test the following combination of options with valid arguments.
 verify by examining the generated jar:
<ul compact>
<li> -source
<li> -destination
<li> -requiredFile
<li> -component
<li> -package
<li> -additionalFile
<li> -additionalFilesDirectory
<li> -language
<li> -languageDirectory
<li> -ccsid
<li> -beans
<li> -excludeSomeDependencies
</ul>
 **/
    public void Var024 ()
    {
      if (DEBUG) printVariationStartTime ();
      File smallJar = null;
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        String destJarName = "Var" + getVariation () + ".jar";
        smallJar = new File (CURRENT_DIR, destJarName);
        JMTest.deleteFile (smallJar);
        // Make the jar.
        String args =
          " -source toolbox.jar" +
          " -destination " + destJarName +
          " -requiredFile com/ibm/as400/vaccess/AS400ListModel32.gif" +
          " -component Trace,CommandCall" +
          " -package com.ibm.as400.vaccess" +
          " -additionalFile " +
             JMTest.ADDITIONAL_FILE_4.getAbsolutePath () +
          " -additionalFilesDirectory " + JMTest.ADDLFILE_DIR.getAbsolutePath () +
          " -language zh_TW,fr_CA" +
          " -languageDirectory Lang" +
          " -ccsid 833,937" +
          " -beans" +
          " -excludeSomeDependencies";

        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          printErrors ();
          printInfo ();
        }

        // Verify that only the correct files got copied.
        Vector expected = new Vector (20);
        // Files to expect for TRACE component:
        expected.add ("com/ibm/as400/access/Trace.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        expected.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        expected.add ("com/ibm/as400/access/InternalErrorException.class");
        expected.add ("com/ibm/as400/access/ReturnCodeException.class");
        expected.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        expected.add ("com/ibm/as400/access/MRI.class");
        // Some files to expect for COMMAND_CALL component:
        expected.add ("com/ibm/as400/access/CommandCall.class");
        expected.add ("com/ibm/as400/access/AS400.class");
        expected.add ("com/ibm/as400/access/AS400Server.class");
        expected.add ("com/ibm/as400/access/DataStream.class");
        expected.add ("com/ibm/as400/access/ProgramCall.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        // Required entries specified.
        expected.add ("com/ibm/as400/vaccess/AS400ListModel32.gif");
        // Packages specified.
        expected.add ("com/ibm/as400/vaccess/VMRI.class");
        expected.add ("com/ibm/as400/vaccess/VMRI_fr_CA.class");
        expected.add ("com/ibm/as400/vaccess/VMRI_zh_TW.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementButton16.gif");
        expected.add ("com/ibm/as400/vaccess/CommandCallButton.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementDocument32.gif");
        // Additional files specified.
        expected.add (JMTest.ADDITIONAL_FILE_4_NAME);
        // MRI files for specified languages.
        expected.add ("com/ibm/as400/access/MRI_fr_CA.class");
        expected.add ("com/ibm/as400/access/MRI_zh_TW.class");
        // ConvTables for specified CCSIDs.
        expected.add ("com/ibm/as400/access/ConvTable833.class");
        expected.add ("com/ibm/as400/access/ConvTable937.class");
        // Beans files for specified components.
        expected.add ("com/ibm/as400/access/CommandCallBeanInfo.class");

        // Files that should NOT get included:
        Vector notExpected = new Vector (20);
        notExpected.add ("com/ibm/as400/access/AS400JDBCCallableStatement.class");
        notExpected.add ("com/ibm/as400/access/AS400Certificate.class");
        notExpected.add ("com/ibm/as400/access/BaseDataQueue.class");
        notExpected.add ("com/ibm/as400/access/JDMRI.class");
        notExpected.add ("com/ibm/as400/access/MRI_ja.class");
        notExpected.add (JMTest.ADDITIONAL_FILE_3_NAME);
        notExpected.add ("com/ibm/as400/access/ConvTable5026.class");
        notExpected.add ("com/ibm/as400/access/ConvTable5035.class");
        notExpected.add ("com/ibm/as400/access/ProgramCallBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/ProgramParameterBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/JDTrace.class");

        if (exitVal != 0) {
          failed ("Wrong exit value");
        }
        else if (!smallJar.exists ()) {
          failed ("Jar not created");
        }
        else if (!JMTest.verifyJarContains (smallJar, expected, true) ||
                 !JMTest.verifyJarNotContains (smallJar, notExpected)) {
          failed ("Generated jar is not as expected");
        }
        //else assertCondition (JMTest.validateJar (smallJar));
        else succeeded ();
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally {
        // Wait for files to get closed
        try {Thread.currentThread().sleep(500);} catch (Exception e){}
        JMTest.deleteFile (smallJar);
      }
    }

/**
 Similar to prior variation, except use the abbreviated forms of the option tags:
<ul compact>
<li> -s
<li> -d
<li> -r
<li> -c
<li> -p
<li> -af
<li> -afd
<li> -l
<li> -ld
<li> -cc
<li> -b
<li> -xd
</ul>
 **/
    public void Var025 ()
    {
      if (DEBUG) printVariationStartTime ();
      File smallJar = null;
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        String destJarName = "Var" + getVariation () + ".jar";
        smallJar = new File (CURRENT_DIR, destJarName);
        // Make the jar.
        String args =
          " -s toolbox.jar" +
          " -d " + destJarName +
          " -rf com/ibm/as400/access/IFSFile16.gif," +
          "com/ibm/as400/access/UserSpace16.gif" +
          " -c Trace,CommandCall" +
          " -p com.ibm.as400.vaccess" +
          " -af " +
             JMTest.ADDITIONAL_FILE_1.getAbsolutePath () + "," +
             JMTest.ADDITIONAL_FILE_2.getAbsolutePath () +
          " -afd " + CURRENT_DIR.getAbsolutePath () +
          " -l zh_TW,fr_CA" +
          " -ld Lang" +
          " -cc 833,937 " +
          " -b" +
          " -xd";

        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          System.out.println("exitVal = " + exitVal);
          printErrors ();
          printInfo ();
        }

        // Verify that only the correct files got copied.
        Vector expected = new Vector (20);
        // Files to expect for TRACE component:
        expected.add ("com/ibm/as400/access/Trace.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        expected.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        expected.add ("com/ibm/as400/access/InternalErrorException.class");
        expected.add ("com/ibm/as400/access/ReturnCodeException.class");
        expected.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        expected.add ("com/ibm/as400/access/MRI.class");
        // Some files to expect for COMMAND_CALL component:
        expected.add ("com/ibm/as400/access/CommandCall.class");
        expected.add ("com/ibm/as400/access/AS400.class");
        expected.add ("com/ibm/as400/access/AS400Server.class");
        expected.add ("com/ibm/as400/access/DataStream.class");
        expected.add ("com/ibm/as400/access/ProgramCall.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        // Required entries specified.
        expected.add ("com/ibm/as400/access/IFSFile16.gif");
        expected.add ("com/ibm/as400/access/UserSpace16.gif");
        // Packages specified.
        expected.add ("com/ibm/as400/vaccess/VMRI.class");
        expected.add ("com/ibm/as400/vaccess/VMRI_fr_CA.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementButton16.gif");
        expected.add ("com/ibm/as400/vaccess/CommandCallButton.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementDocument32.gif");
        // Additional files specified.
        expected.add (JMTest.ADDITIONAL_FILE_1_NAME);
        expected.add (JMTest.ADDITIONAL_FILE_2_NAME);
        // MRI files for specified languages.
        expected.add ("com/ibm/as400/access/MRI_fr_CA.class");
        expected.add ("com/ibm/as400/access/MRI_zh_TW.class");
        // ConvTables for specified CCSIDs.
        expected.add ("com/ibm/as400/access/ConvTable833.class");
        expected.add ("com/ibm/as400/access/ConvTable937.class");
        // Beans files for specified components.
        expected.add ("com/ibm/as400/access/CommandCallBeanInfo.class");

        // Files that should NOT get included:
        Vector notExpected = new Vector (20);
        notExpected.add ("com/ibm/as400/access/AS400JDBCCallableStatement.class");
        notExpected.add ("com/ibm/as400/access/AS400Certificate.class");
        notExpected.add ("com/ibm/as400/access/BaseDataQueue.class");
        notExpected.add ("com/ibm/as400/access/JDMRI.class");
        notExpected.add ("com/ibm/as400/access/MRI_ja.class");
        notExpected.add (JMTest.ADDITIONAL_FILE_3_NAME);
        notExpected.add ("com/ibm/as400/access/ConvTable5026.class");
        notExpected.add ("com/ibm/as400/access/ConvTable5035.class");
        notExpected.add ("com/ibm/as400/access/ProgramCallBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/ProgramParameterBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/JDTrace.class");
        notExpected.add ("com/ibm/as400/access/IFSFile32.gif");
        notExpected.add ("com/ibm/as400/access/UserSpace32.gif");

        if (exitVal != 0) {
          output_.println (errorOutput_);
          failed ("Wrong exit value");
        }
        else if (!smallJar.exists ()) {
          output_.println (errorOutput_);
          failed ("Jar not created");
        }
        else if (!JMTest.verifyJarContains (smallJar, expected, true) ||
                 !JMTest.verifyJarNotContains (smallJar, notExpected))
          failed ("Generated jar is not as expected");
        //else assertCondition (JMTest.validateJar (smallJar));
        else succeeded ();
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally {
        // Wait for files to get closed
        try {Thread.currentThread().sleep(500);} catch (Exception e){}
        JMTest.deleteFile (smallJar);
      }
    }

/**
 Test the following combination of options with valid arguments, and
 verify by inventorying the extracted files:
<ul compact>
<li> -source
<li> -destination
<li> -requiredFile
<li> -component
<li> -package
<li> -additionalFile
<li> -additionalFilesDirectory
<li> -language
<li> -languageDirectory
<li> -ccsidExcluded
<li> -extract <directory>
<li> -excludeSomeDependencies
<li> (no beans)
</ul>
 **/
    public void Var026 ()
    {
      if (DEBUG) printVariationStartTime ();
      File smallJar = null;
      File tempDir = null;
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        String destJarName = "Var" + getVariation () + ".jar";
        smallJar = new File (CURRENT_DIR, destJarName);
        JMTest.deleteFile (smallJar);
        tempDir = new File (CURRENT_DIR, "TempDir");
        JMTest.deleteDirectory (tempDir);
        // Make the jar.
        String args =
          " -source toolbox.jar" +
          " -destination " + destJarName +
          " -requiredFile com/ibm/as400/vaccess/AS400ListModel32.gif" +
          " -component Trace,CommandCall" +
          " -package com.ibm.as400.vaccess" +
          " -additionalFile " +
             JMTest.ADDITIONAL_FILE_4.getAbsolutePath () +
          " -additionalFilesDirectory " + JMTest.ADDLFILE_DIR.getAbsolutePath () +
          " -language zh_TW,fr_CA" +
          " -languageDirectory Lang" +
          " -ccsidExcluded 833,937" +
          " -extract TempDir" +
          " -excludeSomeDependencies";

        output_.println ("NOTE TO TESTER: Please ignore warning about "+
                         "additional files specified.");
        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          System.out.println("exitVal = " + exitVal);
          printErrors ();
          printInfo ();
        }

        // Verify that only the correct files got copied.
        Vector expected = new Vector (20);
        // Files to expect for TRACE component:
        expected.add ("com/ibm/as400/access/Trace.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        expected.add ("com/ibm/as400/access/ExtendedIllegalArgumentException.class");
        expected.add ("com/ibm/as400/access/InternalErrorException.class");
        expected.add ("com/ibm/as400/access/ReturnCodeException.class");
        expected.add ("com/ibm/as400/access/ResourceBundleLoader.class");
        expected.add ("com/ibm/as400/access/MRI.class");
        // Some files to expect for COMMAND_CALL component:
        expected.add ("com/ibm/as400/access/CommandCall.class");
        expected.add ("com/ibm/as400/access/AS400.class");
        expected.add ("com/ibm/as400/access/AS400Server.class");
        expected.add ("com/ibm/as400/access/DataStream.class");
        expected.add ("com/ibm/as400/access/ProgramCall.class");
        expected.add ("com/ibm/as400/access/Copyright.class");
        // Required entries specified.
        expected.add ("com/ibm/as400/vaccess/AS400ListModel32.gif");
        // Packages specified.
        expected.add ("com/ibm/as400/vaccess/VMRI.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementButton16.gif");
        expected.add ("com/ibm/as400/vaccess/CommandCallButton.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementDocument32.gif");
        // ConvTables for CCSIDs other than the excluded ones.
        expected.add ("com/ibm/as400/access/ConvTable5026.class");
        expected.add ("com/ibm/as400/access/ConvTable5035.class");

        // Files that should NOT get included:
        Vector notExpected = new Vector (20);
        notExpected.add ("com/ibm/as400/access/AS400JDBCCallableStatement.class");
        notExpected.add ("com/ibm/as400/access/AS400Certificate.class");
        notExpected.add ("com/ibm/as400/access/BaseDataQueue.class");
        notExpected.add ("com/ibm/as400/access/JDMRI.class");
        notExpected.add ("com/ibm/as400/access/MRI_ja.class");
        notExpected.add (JMTest.ADDITIONAL_FILE_3_NAME);
        notExpected.add ("com/ibm/as400/access/ConvTable833.class");
        notExpected.add ("com/ibm/as400/access/ConvTable937.class");
        notExpected.add ("com/ibm/as400/access/ProgramCallBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/ProgramParameterBeanInfo.class");
        notExpected.add ("com/ibm/as400/access/JDTrace.class");
        // Additional files should not get extracted.
        notExpected.add (JMTest.ADDITIONAL_FILE_4_NAME);
        // MRI files for specified languages should not get extracted.
        notExpected.add ("com/ibm/as400/access/MRI_fr_CA.class");
        notExpected.add ("com/ibm/as400/access/MRI_zh_TW.class");
        notExpected.add ("com/ibm/as400/vaccess/VMRI_fr_CA.class");
        // Beans files for specified components should not get extracted.
        notExpected.add ("com/ibm/as400/access/CommandCallBeanInfo.class");

        Vector subDirs = new Vector (1);
        subDirs.add ("com");  // name of extraction directory

        if (exitVal != 0) {
          output_.println (errorOutput_);
          failed ("Wrong exit value");
        }
//        else if (smallJar.exists ()) {
//          output_.println (errorOutput_);
//          failed ("Jar got created but should not have");
//        }
        else if (!JMTest.verifyExtractionContains (tempDir, subDirs, expected) ||
                 !JMTest.verifyExtractionNotContains (tempDir, subDirs, notExpected))
          failed ("The result of the extraction is not as expected");
        else succeeded ();
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally {
        // Wait for files to get closed
        try {Thread.currentThread().sleep(500);} catch (Exception e){}
        JMTest.deleteFile (smallJar);
        JMTest.deleteDirectory (tempDir);
      }
    }

/**
 Test the following combination of options with valid arguments, and
 verify by inventorying the extracted files:
<ul compact>
<li> -source
<li> -destination
<li> -requiredFile
<li> (no component)
<li> -package
<li> -additionalFile
<li> -additionalFilesDirectory
<li> (no language)
<li> (no languageDirectory)
<li> (no ccsid)
<li> (no ccsidExcluded)
<li> -extract (no directory specified)
<li> (no beans)
<li> -verbose
</ul>
 **/
    public void Var027 (int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) return;
      if (DEBUG) printVariationStartTime ();
      File smallJar = null;
      File tempDir = null;
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        String destJarName = "Var" + getVariation () + ".jar";
        smallJar = new File (CURRENT_DIR, destJarName);
        JMTest.deleteFile (smallJar);
        // Remove the extraction directory if it exists.
        tempDir = new File (JMTest.JUNGLE_PACKAGE_NAME);
        JMTest.deleteDirectory (tempDir);
        // Make the jar.
        String args =
          " -source jungle.jar" +
          " -destination " + destJarName +
          " -requiredFile jungle/predator/puma.jpg," +
          "jungle/tree.jpg" +
          " -package jungle.prey" +
          " -additionalFile " +
             JMTest.ADDITIONAL_FILE_4.getAbsolutePath () +
          " -additionalFilesDirectory " + JMTest.ADDLFILE_DIR.getAbsolutePath () +
          " -extract" +
          " -verbose";

        int exitVal = callJarMaker (args); // invoke jarmaker
        printErrors ();
        printInfo ();
        // String text = "Were some warnings and progress messages" +
        //  " printed to the command entry window" +
        //  " (and/or to the redirection file)?";
        //if (true != VTestUtilities.ask(text))
        //  failed("No progress messages were printed");
        // else 
        {

          // Verify that only the correct files got extracted.
          Vector expected = new Vector ();
          // The specified jar entries:
          expected.add ("jungle/predator/puma.jpg");
          expected.add ("jungle/tree.jpg");
          // The specified package:
          expected.add ("jungle/prey/Prey.java");
          expected.add ("jungle/prey/Prey.class");
          // Note: No additional files should get copied.

          if (exitVal != 0) {
            output_.println (errorOutput_);
            failed ("Wrong exit value");
          }
          else if (smallJar.exists ()) {
            output_.println (errorOutput_);
            failed ("Jar got created but should not have");
          }
          else if (!JMTest.verifyExtraction (CURRENT_DIR, expected))
            failed ("The result of the extraction is not as expected");
          else succeeded ();
        }
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
      finally {
        // Wait for files to get closed
        try {Thread.currentThread().sleep(500);} catch (Exception e){}
        JMTest.deleteFile (smallJar);
        JMTest.deleteDirectory (tempDir);
      }
    }

/**
 Same as prior variation, except use abbreviated forms of options:
<ul compact>
<li> -s
<li> -d
<li> -rf
<li> (no component)
<li> -p
<li> -af
<li> -afd
<li> (no language)
<li> (no languageDirectory)
<li> (no ccsid)
<li> (no ccsidExcluded)
<li> -x (no directory specified)
<li> (no beans)
<li> -v
</ul>
 **/
    public void Var028 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

/**
 Verify that a warning gets printed if other options are specified along
 with the -split option, and that -extract is overridden by -split.
<ul compact>
<li> -source
<li> -component
<li> -language
<li> -languageDirectory
<li> -ccsidExcluded
<li> -beans
<li> -split
</ul>
 **/
    public void Var029 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

/**
 Verify that no progress messages are printed when -verbose is not specified.
 **/
    public void Var030 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

//----------------------------------------------------------------------
// Variations for makeJar (File), using a non-Toolbox source jar file :

/**
 makeJar(File) - Specify all components, and require beans.
 Do not specify a source jar or a destination jar (take the defaults).
 Should copy all files in the source jar (jt400.jar), except any "orphans".
 The default destination jar file (jt400Small.jar) should be created
 in the current directory.
 **/
    public void Var031 ()
    {
      notApplicable("This variation is no longer applicable.");
      return;
//      if (DEBUG) printVariationStartTime (); // this var may be long-running
//      File sourceJar = null;
//      File smallJar = null;
//      String allComponents = "AS400,CommandCall,DataDescription," +
//        "DataQueue,DigitalCertificate,IntegratedFileSystem,JDBC,Job,Message," +
//        "Print,ProgramCall,RecordLevelAccess,Trace,User,UserSpace," +
//        "AS400Visual,CommandCallVisual,DataQueueVisual," +
//        "IntegratedFileSystemVisual,JDBCVisual,JobVisual,"+
//        "MessageVisual,PrintVisual,ProgramCallVisual," +
//        "RecordLevelAccessVisual,UserVisual";
//      try {
//        sourceJar = new File (CURRENT_DIR, "jt400.jar");
//        smallJar = new File (CURRENT_DIR, "jt400Small.jar");
//        // Copy the "toolbox.jar" file to "jt400.jar",
//        // so we can check the default.
//        JMTest.copyFile (JMTest.TOOLBOX_JAR, sourceJar);
//        // Remove the destination jar file if it exists.
//        JMTest.deleteFile (smallJar);
//
//        // Make the jar.
//        String args =
//          // QSYSObjectPathNameBeanInfo is an orphan, so specify
//          // it so we can match the contents of the source jar.
//          " -requiredFile com/ibm/as400/access/QSYSObjectPathNameBeanInfo.class" +
//          " -component " + allComponents +
//          " -additionalFilesDirectory " + JMTest.ADDLFILE_DIR.getAbsolutePath () +
//          " -beans";
//
//        int exitVal = callJarMaker (args); // invoke jarmaker
//        if (DEBUG) {
//          printErrors ();
//          printInfo ();
//        }
//
//        // Verify that only the correct files got copied.
//        // (We expect all files to have been copied)
//        assertCondition (JMTest.compareJars (smallJar, sourceJar));
//      }
//      catch (Exception e) {
//        failed (e, "Unexpected Exception");
//      }
//      finally {
//        // Wait for files to get closed
//        try {Thread.currentThread().sleep(500);} catch (Exception e){}
//        if (sourceJar != null) {
//          try {sourceJar.delete ();}
//          catch (Exception e) {}
//        }
//        if (smallJar != null) {
//          try {smallJar.delete ();}
//          catch (Exception e) {}
//        }
//      }
    }

}
