///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMParseArgs.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JMTest;
import test.JTOpenTestEnvironment;
import test.Testcase;



/**
Testcase JMParseArgs.  This tests the command-line invocation mode
of class JarMaker.
**/
public class JMParseArgs
extends Testcase
{
  private static final boolean DEBUG = false;
  static final File CURRENT_DIR = new File (System.getProperty ("user.dir"));
  static final String CLASSPATH = System.getProperty ("java.class.path");
  private boolean DOS_ = false;
  private String infoOutput_;
  private String errorOutput_;

/**
Constructor.
**/
  public JMParseArgs (AS400 systemObject,
                   Hashtable<String,Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMParseArgs",
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
        System.err.println ("Setup error: JMParseArgs: Could not delete file " +
                             JMTest.JUNGLE_JAR_SMALL.getAbsolutePath ());

      // Determine operating system we're running under
      String operatingSystem_ = System.getProperty("os.name");
      if (JTOpenTestEnvironment.isWindows)
      {
        DOS_ = true;
      }
      else if (operatingSystem_.indexOf("DOS") >= 0)
      {
        DOS_ = true;
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
Calls JarMaker, as a separate process.
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
        " utilities.JarMaker " + args;

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
            try {
            Thread.sleep(500);} catch (Exception e1){}
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
        if (DEBUG)
          System.out.println("DEBUG exitVal="+exitVal+", errorOutput_="+errorOutput_);
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
        assertCondition ((exitVal == 1) &&
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
        assertCondition (exitVal == 1 &&
                -1 != errorOutput_.indexOf  ("Warning: No file specified after -source."));
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
        if (DEBUG) {
          System.out.println("infoOutput_: |" + infoOutput_ + "|");
          System.out.println("errorOutput_: |" + errorOutput_ + "|; exitVal==" + exitVal);
        }
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
        assertCondition (exitVal == 1 &&
               (-1 != errorOutput_.indexOf ("Warning: No file specified after -requiredFile.") ||
                -1 != errorOutput_.indexOf ("Warning: No file specified after -fileRequired.")) &&
                !JMTest.JUNGLE_JAR_SMALL.exists ());
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 -package - No argument specified.  Should complain and quit.
 **/
    public void Var009 ()
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
    public void Var010 ()
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
    public void Var011 ()
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
    public void Var012 ()
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
    public void Var013 ()
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
 Test the following combination of options with valid arguments.
 verify by examining the generated jar:
<ul compact>
<li> -source
<li> -destination
<li> -requiredFile
<li> -package
<li> -additionalFile
<li> -additionalFilesDirectory
</ul>
 **/
    public void Var014 ()
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
          " -package com.ibm.as400.vaccess" +
          " -additionalFile " +
             JMTest.ADDITIONAL_FILE_4.getAbsolutePath () +
          " -additionalFilesDirectory " + JMTest.ADDLFILE_DIR.getAbsolutePath ();

        int exitVal = callJarMaker (args); // invoke jarmaker
        if (DEBUG) {
          printErrors ();
          printInfo ();
        }

        // Verify that only the correct files got copied.
        Vector<String> expected = new Vector<String> (20);
        // Required entries specified.
        expected.add ("com/ibm/as400/vaccess/AS400ListModel32.gif");
        // Packages specified.
        expected.add ("com/ibm/as400/vaccess/VMRI.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementButton16.gif");
        expected.add ("com/ibm/as400/vaccess/CommandCallButton.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementDocument32.gif");
        // Additional files specified.
        expected.add (JMTest.ADDITIONAL_FILE_4_NAME);

        // Files that should NOT get included:
        Vector<String> notExpected = new Vector<String> (20);
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
        else if (!JMTest.verifyJarContains (smallJar, expected, false) ||
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
        try {Thread.currentThread();
        Thread.sleep(500);} catch (Exception e){}
        JMTest.deleteFile (smallJar);
      }
    }

/**
 Similar to prior variation, except use the abbreviated forms of the option tags:
<ul compact>
<li> -s
<li> -d
<li> -r
<li> -p
<li> -af
<li> -afd
</ul>
 **/
    public void Var015 ()
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
          " -p com.ibm.as400.vaccess" +
          " -af " +
             JMTest.ADDITIONAL_FILE_1.getAbsolutePath () + "," +
             JMTest.ADDITIONAL_FILE_2.getAbsolutePath () +
          " -afd " + CURRENT_DIR.getAbsolutePath ();

        int exitVal = callJarMaker (args); // invoke jarmaker
        printErrors ();
        printInfo ();

        // Verify that only the correct files got copied.
        Vector<String> expected = new Vector<String> (20);
        // Required entries specified.
        expected.add ("com/ibm/as400/access/IFSFile16.gif");
        expected.add ("com/ibm/as400/access/UserSpace16.gif");
        // Packages specified.
        expected.add ("com/ibm/as400/vaccess/VMRI.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementButton16.gif");
        expected.add ("com/ibm/as400/vaccess/CommandCallButton.class");
        expected.add ("com/ibm/as400/vaccess/SQLStatementDocument32.gif");
        // Additional files specified.
        expected.add (JMTest.ADDITIONAL_FILE_1_NAME);
        expected.add (JMTest.ADDITIONAL_FILE_2_NAME);

        // Files that should NOT get included:
        Vector<String> notExpected = new Vector<String> (20);
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
        else if (!JMTest.verifyJarContains (smallJar, expected, false) ||
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
        try {Thread.currentThread();
        Thread.sleep(500);} catch (Exception e){}
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
<li> -package
<li> -additionalFile
<li> -additionalFilesDirectory
<li> -extract (no directory specified)
<li> -verbose
</ul>
 **/
    public void Var016 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

/**
 Same as prior variation, except use abbreviated forms of options:
<ul compact>
<li> -s
<li> -d
<li> -rf
<li> -p
<li> -af
<li> -afd
<li> -x (no directory specified)
<li> -v
</ul>
 **/
    public void Var017 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

/**
 Verify that a warning gets printed if other options are specified along
 with the -split option, and that -extract is overridden by -split.
<ul compact>
<li> -source
<li> -destination
<li> -requiredFile
<li> -package
<li> -additionalFile
<li> -additionalFilesDirectory
<li> -extract
<li> -split
</ul>
 **/
    public void Var018 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

/**
 Verify that no progress messages are printed when -verbose is not specified.
 **/
    public void Var019 (int runMode)
    {
    	notApplicable("Attended testcase");
    	    }

}
