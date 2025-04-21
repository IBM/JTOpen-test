///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INAppTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IN;

import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.util.Vector;
import java.util.ResourceBundle;
import java.security.Security;
import com.ibm.as400.access.AS400;

import test.InstallTest;
import test.Testcase;

/**
The INAppTestcase class provides testcases to test installation
of package.
**/

public class INAppTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "INAppTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.InstallTest.main(newArgs); 
   }

    // Private data.
    private static final int variations_ = 28;
    private String sourcePath = "sourcePath";
    private String targetPath = "targetpath";
    // Where MRI comes from.
    private static ResourceBundle resources_;
    static final String CLASSPATH = System.getProperty ("java.class.path");
    private String infoOutput_;
    private String errorOutput_;

/**
Constructor.
**/
   public INAppTestcase(AS400             systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
{
    super(systemObject, "INAppTestcase", variations_,
          variationsToRun, runMode, fileOutputStream);
}


/**
Checks the CLASSPATH to ensure it is set up to run the variations in
this testcase correctly.

This method ensures the following are IN the CLASSPATH:
<ol compact>
<li><-directory>\install\test;
<li><-directory>\install\test\TEST3.ZIP;
<li><-directory>\install\test\TEST6.JAR;
<li><-directory>\install\test\TEST9.ZIP;
<li><-directory>\install\test\test2;
<li><-directory>\install\test\test4;
<li>.\install\test;
<li>.\install\test\test2;
<li>.\install\test\TEST3.ZIP;
<li>.\install\test\TEST6.JAR;
</ol>

This method ensures the following are NOT in the CLASSPATH:
<ol compact>
<li><-directory>\install\test\TEST1.ZIP;
<li><-directory>\install\test\TEST2.ZIP;
<li><-directory>\install\test\TEST4.JAR;
<li><-directory>\install\test\TEST5.JAR;
<li><-directory>\install\test\TEST7.ZIP;
<li><-directory>\install\test\TEST8.ZIP;
<li><-directory>\install\test\test1;
<li><-directory>\install\test\test3;
<li>.\install\test\test1;
<li>.\install\test\TEST1.ZIP;
<li>.\install\test\TEST2.ZIP;
</ol>

@return    true if the CLASSPATH is setup correctly; false otherwise

@exception SecurityException If the CLASSPATH cannot be accessed.
**/
private boolean checkClasspath()
{
    // Search with leading and trailing semicolons to make sure
    // paths match exactly.
    String separator = System.getProperty("path.separator");
    String classpath = separator +
                       System.getProperty("java.class.path") +
                       separator;
    classpath = classpath.toUpperCase();
    boolean results = true;
    String path;


    // Check for items that must be in CLASSPATH

    String target  = InstallTest.targetPath + "install" + File.separator +"test"+File.separator;

    path = InstallTest.targetPath + "install" + File.separator + "test";

    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = target + "TEST3.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = target + "TEST6.JAR";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = target + "TEST9.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = target + "test2";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = target + "test4";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }

    // Check for items that must NOT be in CLASSPATH
    path = target + "TEST1.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "TEST2.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "TEST4.JAR";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "TEST5.JAR";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "TEST7.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "TEST8.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "test1";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = target + "test3";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }


    return results;
}

/**
Loads the resource bundle if not already done.

@return The resource bundle for this class.
**/
private static ResourceBundle getMRIResource()
{
  // Initialize resource bundle if not already done.
  if (resources_ == null)
      resources_ = ResourceBundle.getBundle("utilities.INMRI");
  return resources_;
}


/**
Calls ExecuteCommand, as a separate process.
This simulates a command-line invocation.
@return exit value from the subprocess in which ExecuteCommand was run
**/
    private int executeCommand (String args)
        throws Exception
    {
      Process prc = null;
      int exitVal = 999999;
      // Note: For some reason, in JDK 1.1.6 we need to specify classpath
      // explicitly on the java invocation.
      String command = "java "+//-classpath " + CLASSPATH +
      " utilities.AS400ToolboxInstaller " + args;

      Runtime rt = Runtime.getRuntime ();
      prc = rt.exec (command);

      // Workaround for the pipe overflow bug on Windows platforms.
      // Java bug: 4062587;  and followup: 4098442
      ///if (windows_)
      ///{
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
      ///}
        infoOutput_  = infoText.toString ();
        errorOutput_ = errorText.toString ();
//System.out.println("  info  "+infoOutput_);
//System.out.println("  error  "+errorOutput_);
      ///if (DEBUG) System.out.println ("Waiting for process to complete.");
      ///exitVal = prc.waitFor ();
      ///if (DEBUG) System.out.println ("Process completed.");
      ///if (exitVal != 1) throw new Exception ("waitFor() exit value: " +
      ///                                       exitVal);

      ///return prc;
      return exitVal;
    }

/**
Runs the variations.
**/
public void run ()
{

    System.out.println("");
    System.out.println("Testcase INAppTestcase -- hints for successful execution");
    System.out.println("   Examples for -directory option");
    System.out.println("      PC:  -directory c:\\jt400");
    System.out.println("      AIX: -directory /abc/def/g");
    System.out.println("");
    
    boolean allVariations = (variationsToRun_.size () == 0);
    File dir = new File(InstallTest.targetPath + targetPath );

    if (!dir.exists())
    {
       dir.mkdirs();
    }

    if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED)
    {
        setVariation(1);
        Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
    {
        setVariation(2);
        Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
    {
        setVariation(3);
        Var003();
    }
    if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
    {
        setVariation(4);
        Var004();
    }
    if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
    {
        setVariation(5);
        Var005();
    }
    if ((allVariations || variationsToRun_.contains("6")) && runMode_ != ATTENDED)
    {
        setVariation(6);
        Var006();
    }
    if ((allVariations || variationsToRun_.contains("7")) && runMode_ != ATTENDED)
    {
        setVariation(7);
        Var007();
    }
    if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED)
    {
        setVariation(8);
        Var008();
    }
    if ((allVariations || variationsToRun_.contains("9")) && runMode_ != ATTENDED)
    {
        setVariation(9);
        Var009();
    }
    if ((allVariations || variationsToRun_.contains("10")) && runMode_ != ATTENDED)
    {
        setVariation(10);
        Var010();
    }
    if ((allVariations || variationsToRun_.contains("11")) && runMode_ != ATTENDED)
    {
        setVariation(11);
        Var011();
    }
    if ((allVariations || variationsToRun_.contains("12")) && runMode_ != ATTENDED)
    {
        setVariation(12);
        Var012();
    }
    if ((allVariations || variationsToRun_.contains("13")) && runMode_ != ATTENDED)
    {
        setVariation(13);
        Var013();
    }
    if ((allVariations || variationsToRun_.contains("14")) && runMode_ != ATTENDED)
    {
        setVariation(14);
        Var014();
    }
    if ((allVariations || variationsToRun_.contains("15")) && runMode_ != ATTENDED)
    {
        setVariation(15);
        Var015();
    }
    if ((allVariations || variationsToRun_.contains("16")) && runMode_ != ATTENDED)
    {
        setVariation(16);
        Var016();
    }
    if ((allVariations || variationsToRun_.contains("17")) && runMode_ != ATTENDED)
    {
        setVariation(17);
        Var017();
    }
    if ((allVariations || variationsToRun_.contains("18")) && runMode_ != ATTENDED)
    {
        setVariation(18);
        Var018();
    }
    if ((allVariations || variationsToRun_.contains("19")) && runMode_ != ATTENDED)
    {
        setVariation(19);
        Var019();
    }
    if ((allVariations || variationsToRun_.contains("20")) && runMode_ != ATTENDED)
    {
        setVariation(20);
        Var020();
    }
    if ((allVariations || variationsToRun_.contains("21")) && runMode_ != ATTENDED)
    {
        setVariation(21);
        Var021();
    }
    if ((allVariations || variationsToRun_.contains("22")) && runMode_ != ATTENDED)
    {
        setVariation(22);
        Var022();
    }
    if ((allVariations || variationsToRun_.contains("23")) && runMode_ != ATTENDED)
    {
        setVariation(23);
        Var023();
    }
    if ((allVariations || variationsToRun_.contains("24")) && runMode_ != ATTENDED)
    {
        setVariation(24);
        Var024();
    }
    if ((allVariations || variationsToRun_.contains("25")) && runMode_ != ATTENDED)
    {
        setVariation(25);
        Var025();
    }
    if ((allVariations || variationsToRun_.contains("26")) && runMode_ != ATTENDED)
    {
        setVariation(26);
        Var026();
    }
    if ((allVariations || variationsToRun_.contains("27")) && runMode_ != ATTENDED)
    {
        setVariation(27);
        Var027();
    }
    if ((allVariations || variationsToRun_.contains("28")) && runMode_ != ATTENDED)
    {
        setVariation(28);
        Var028();
    }

    dir.delete();
    String source  = InstallTest.targetPath + sourcePath + File.separator;
    File file;
    file = new File(source + "test1.zip");
    file.delete();
    file = new File(source + "test2.zip");
    file.delete();
    file = new File(source + "ACCESS.LST");
    file.delete();
    file = new File(source + "ACCESS.LVL");
    file.delete();
    file = new File(source + "ACCESS1.LST");
    file.delete();
    file = new File(source + "ACCESS1.LVL");
    file.delete();
    file = new File(source + "JT400.PKG");
    file.delete();
    file = new File(source + "test3.zip");
    file.delete();
    file = new File(source + "test4.zip");
    file.delete();
    file = new File(source + "listfile1.lst");
    file.delete();
    file = new File(source + "listfile2.lst");
    file.delete();
    file = new File(source);
    file.delete();
    file = new File(InstallTest.targetPath);
    file.delete();
}
// Replaces a substitution variable in a string.
// @param  text  The text string, with a single substitution variable (e.g. "Error &0 has occurred.")
// @param  value  The replacement value.
// @return  The text string with the substitution variable replaced.
private String substitute(String text, String value)
{
    String result = text;
    String variable = "&0";
    int j = result.indexOf (variable);
    if (j >= 0)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(result.substring(0, j));
        buffer.append(value);
        buffer.append(result.substring(j + variable.length ()));
        result = buffer.toString ();
    }
    return result;
}


/**
Verify that the package is installed successfully when the parameters are
 " -p <packageName> -s <source path> -t <target path> -i".
**/
public void Var001 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message = substitute(message,"access");
   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        File targetDir = new File(target1);
        int len1 = targetDir.list().length;

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  YY   TEST1.ZIP\n"+
                         "ACCESS   PRMV NY   test2\n" );
                         
        String com;
        if (source.startsWith("/"))
           com = " -p  ACCESS  -s  file:" + source1 +"  -t "+ target1+" -i ";
        else
           com = " -p  ACCESS  -s  file:/"+ source1 +"  -t "+ target1+" -i ";

        int exitVal = executeCommand (com);
        targetDir = new File(target1);
        int len2 = targetDir.list().length;

        if(len1 != 0)
            failed(" The target directory was not empty before the test!");

        else if(len2 ==0)
            failed(" The package is not installed into the specified source directory!");

        else if(! (infoOutput_.toUpperCase().startsWith(message.toUpperCase())))
            failed(" Prompting message is not correct!");

        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
            // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();

    }
}

/**
Verify that the package is installed successfully when the parameters are
 " -p <packageName> -t <target path> -s <source path> -i".
**/
public void Var002 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message = substitute(message,"access");
   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  YY   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source.startsWith("/"))
           com = " -p  ACCESS  -t "+target1+" -s file:" + source1 +" -i ";
        else
           com = " -p  ACCESS  -t "+target1+" -s file:/"+ source1 +" -i ";
        
        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");

        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
        }
        finally   // Cleanup.
        {
            // Remove change list files from source
            File file = new File(target+"TEST1.ZIP");
            file.delete();
            file = new File(target+"TEST2.ZIP");
            file.delete();
            file = new File(target+"ACCESS.LST");
            file.delete();
            file = new File(target+"ACCESS.LVL");
            file.delete();
            file = new File(target+"JT400.PKG");
            file.delete();
        }
   }


/**
Verify that the package is installed successfully when the parameters  are
 " -t <target path> -s <source path> -i -p <packageName> ".
**/
public void Var003 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message = substitute(message,"access");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");


        String com;
        if (source1.startsWith("/"))
           com = " -t "+target1+" -s file:" + source1 +" -i -p  ACCESS ";
        else
           com = " -t "+target1+" -s file:/"+ source1 +" -i -p  ACCESS ";

        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
            // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
    }
}

/**
Verify that the package is installed successfully when the parameters are
 " -t <target path> -i  -p <packageName> -s <source path> ".
**/
public void Var004 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message = substitute(message,"access");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com = " -t "+target1+" -i -p  ACCESS -s file:" + source1 ;
        else
           com = " -t "+target1+" -i -p  ACCESS -s file:/"+ source1 ;

        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");

        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
    }
  }

/**
Verify that the package is installed successfully when the parameters are
 " -s <source path>  -t <target path>  -p <packageName> -i ".
**/
public void Var005 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message = substitute(message,"access");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com = " -s file:" + source1+" -t "+target1+" -p ACCESS -i";
        else
           com = " -s file:/"+ source1+" -t "+target1+" -p ACCESS -i";
        
        int exitVal = executeCommand (com);
        
        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();

    }
}


/**
Verify that the package is installed successfully when the parameters are
 " -s <source path> -p <packageName> -t <target path> -i".
**/
public void Var006 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   
   message = substitute(message,"access");
   
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com = " -s file:" + source1+" -p ACCESS  -t "+target1+ " -i";
        else
           com = " -s file:/"+ source1+" -p ACCESS  -t "+target1+ " -i";

        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
    }
}

/**
Verify that the package is not installed successfully when the parameters are
 " <packageName>  <source path>  <target path> ".
**/
public void Var007 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("ERR_INVALID_ARGUMENT");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com = " ACCESS file:" + source1+" "+target1+ " -i";
       else
           com = " ACCESS file:/"+ source1+" "+target1+ " -i";

        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}

/**
Verify that the package are installed successfully when the parameters are
 " -p <packageName1,packageName2> -s <source path> -t <target path> -i".
 and uninstalled successfully when the parameters are
 " -p <packageName1,packageName2> -t <target path> -u".
**/
public void Var008 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message1 = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message1 = substitute(message1,"access");
   String message2 = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message2 = substitute(message2,"access1");

   String message3 = getMRIResource().getString("RESULT_PACKAGE_UNINSTALLED");
   message3 = substitute(message3,"access");
   String message4 = getMRIResource().getString("RESULT_PACKAGE_UNINSTALLED");
   message4 = substitute(message4,"access1");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\nACCESS1\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL", "ListFile1\n");
        InstallTest.writeFile(source + "ACCESS1.LVL","ListFile2\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST", "TEST1.ZIP\nTEST2.ZIP\n");
        InstallTest.writeFile(source + "ACCESS1.LST","TEST3.ZIP\nTEST4.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test zip 1\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip 2\n");
        InstallTest.writeFile(source + "TEST3.ZIP","test zip 3\n");
        InstallTest.writeFile(source + "TEST4.ZIP","test zip 4\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");
        InstallTest.writeFile(source + "ListFile2.LST",
                         "ACCESS1  ADD  NN   TEST3.ZIP\n"+
                         "ACCESS1  ADD  NN   TEST4.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com = " -p ACCESS,ACCESS1 -s file:" + source1 +" -t "+target1+" -i";
        else
           com = " -p ACCESS,ACCESS1 -s file:/"+ source1 +" -t "+target1+" -i";
        int exitVal1 = executeCommand(com);

        File targetDir = new File(target1);
        int len1 = targetDir.list().length;

        com =" -p ACCESS -t "+target1+" -u";
        int exitVal2 = executeCommand(com);
        String m1=infoOutput_;

        com =" -t "+target1+" -u";
        exitVal2 = executeCommand(com);
        String m2 =infoOutput_;

        if(len1 == 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!m1.toUpperCase().startsWith(message3.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();

   }
   catch (Exception e)
   {
        failed (e, "Unexpected Exception");
   }
   finally   // Cleanup.
   {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"TEST3.ZIP");
        file.delete();
        file = new File(target+"TEST4.ZIP");
        file.delete();
        file = new File(target+"ACCESS1.LST");
        file.delete();
        file = new File(target+"ACCESS1.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
        file = new File(source+"ACCESS1.LST");
        file.delete();
        file = new File(source+"ACCESS1.LVL");
        file.delete();
        file = new File(source+"TEST4.ZIP");
        file.delete();
   }
  }
/**
Verify that all packages are installed successfully when the parameters are
 "-s <source path> -t <target path> -i".

**/
public void Var009 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message1 = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message1 = substitute(message1,"ACCESS");
   String message2 = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message2 = substitute(message2,"ACCESS1");

   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\nACCESS1\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL", "ListFile1\n");
        InstallTest.writeFile(source + "ACCESS1.LVL","ListFile2\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST", "TEST1.ZIP\nTEST2.ZIP\n");
        InstallTest.writeFile(source + "ACCESS1.LST","TEST3.ZIP\nTEST4.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test zip 1\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip 2\n");
        InstallTest.writeFile(source + "TEST3.ZIP","test zip 3\n");
        InstallTest.writeFile(source + "TEST4.ZIP","test zip 4\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");
        InstallTest.writeFile(source + "listFile2.LST",
                         "ACCESS1  ADD  NN   TEST3.ZIP\n"+
                         "ACCESS1  ADD  NN   TEST4.ZIP\n");


        String com;
        if (source1.startsWith("/"))
           com = " -s file:" + source1 +" -t "+target1+" -i";
        else
           com = " -s file:/"+ source1 +" -t "+target1+" -i";
       
        int exitVal1 = executeCommand(com);
       
        if(exitVal1 != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
       
        else if(!infoOutput_.toUpperCase().startsWith(message1.toUpperCase())&&
                !(infoOutput_.toUpperCase().indexOf(message2.toUpperCase())>0))
            failed(" Prompting message is not correct!");
       
        else
            succeeded();
   }
   catch (Exception e)
   {
        failed (e, "Unexpected Exception");
   }
   finally   // Cleanup.
   {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"TEST3.ZIP");
        file.delete();
        file = new File(target+"TEST4.ZIP");
        file.delete();
        file = new File(target+"ACCESS1.LST");
        file.delete();
        file = new File(target+"ACCESS1.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
        file = new File(source+"ACCESS1.LST");
        file.delete();
        file = new File(source+"ACCESS1.LVL");
        file.delete();
        file = new File(source+"TEST4.ZIP");
        file.delete();
   }
  }

/**
Verify that the help information is displayed when parameter is " -? "  or "-help"
**/
public void Var010 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;

   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\nACCESS1\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL", "ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST", "TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test zip 1\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip 2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "listFile1.LST",
                         "ACCESS   ADD  YN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NY   TEST2.ZIP\n");

        String com = " -?";

        int exitVal1 = executeCommand(com);

        if(exitVal1 != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!(infoOutput_.toUpperCase().indexOf("USAGE")<3))
            failed(" Prompting message is not correct!");
        else
            succeeded();

   }
   catch (Exception e)
   {
        failed (e, "Unexpected Exception");
   }

  }

/**
Verify that the help information display when there is no parameter.
**/
public void Var011 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\nACCESS1\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL", "ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST", "TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test zip 1\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip 2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "listFile1.LST",
                         "ACCESS   ADD  YN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NY   TEST2.ZIP\n");

        String com = "";
        int exitVal1 = executeCommand(com);
        if(exitVal1 != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!(infoOutput_.toUpperCase().indexOf("USAGE")<3))
            failed(" Prompting message is not correct!");
        else
            succeeded();
   }
   catch (Exception e)
   {
        failed (e, "Unexpected Exception");
   }

  }

/**
Verify that the help information is displayed on Dos windwo when the parameters are
 " -p <packageName1,packageName2> -s <source path> -t <target path> -i -? ".
**/
public void Var012 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL", "ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST", "TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test zip 1\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip 2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "listFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com = "-p  ACCESS,ACCESS1  -s  file:/"+ source1 +"  -t  "+ target1+" -i -?";
      
        int exitVal1 = executeCommand(com);
      
        if(exitVal1 != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!(infoOutput_.toUpperCase().indexOf("USAGE")<3))
            failed(" Prompting message is not correct!");
        else
            succeeded();

   }
   catch (Exception e)
   {
        failed (e, "Unexpected Exception");
   }

  }


/**
Verify that the package is uninstalled.
**/
public void Var013 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;

   String message = getMRIResource().getString("RESULT_PACKAGE_UNINSTALLED");
   message = substitute(message,"access");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com="-s  file:" +source1+ " -t "+target1+ " -p ACCESS -i";
        else
           com="-s  file:/"+source1+ " -t "+target1+ " -p ACCESS -i";
     
        int exitVal = executeCommand (com);
        
        com=" -t "+target1+ " -p ACCESS -u";
        exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!infoOutput_.toUpperCase().startsWith(message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
            
        com=" -t "+target1+ " -p ACCESS -u";
        exitVal = executeCommand (com);
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}

/**
Verify that all packages are uninstalled.
**/
public void Var014 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;

   String message1 = getMRIResource().getString("RESULT_PACKAGE_UNINSTALLED");
   message1 = substitute(message1,"ACCESS");

   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com;
        if (source1.startsWith("/"))
           com=" -p ACCESS -s  file:"+ source1+ " -t "+target1+ " -i ";
        else
           com=" -p ACCESS -s  file:/"+source1+ " -t "+target1+ " -i ";
        int exitVal = executeCommand (com);

        com=" -t "+target1+ "  -u";
        exitVal = executeCommand (com);
       
        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
       
        else if(!infoOutput_.toUpperCase().startsWith (message1.toUpperCase()))
            failed(" Prompting message is not correct! " + message1 + " ... " + infoOutput_);
       
        else
            succeeded();

    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}

/**
Verify that the help information is displayed when the parameters are not enough.
**/
public void Var015 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message  = getMRIResource().getString("ERR_MISSING_OPTION");
   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");


        String com=" -p access -t "+target1+ " -i ";
        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!infoOutput_.toUpperCase().startsWith (message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();

    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}

/**
Verify that the help information is displayed when the parameters are not enough.
**/
public void Var016 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message  = getMRIResource().getString("ERR_MISSING_OPTION_VALUE");

   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com=" -p -s file:/"+source1+" -t "+target1+ " -i ";
        int exitVal = executeCommand (com);
        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!infoOutput_.toUpperCase().startsWith (message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}

/**
Verify that the help information is displayed when the parameters are not enough.
**/
public void Var017 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message  = getMRIResource().getString("ERR_MISSING_OPTION");
   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com=" -p ACCESS -s file:/"+source1+" -i ";
        int exitVal = executeCommand (com);
        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");
        else if(!infoOutput_.toUpperCase().startsWith (message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
       failed (e, "Unexpected Exception");
    }
 }

/**
Verify that the file that exists in source package but does not exist in the target package
is installed.
**/
public void Var018 ()
{
   String sourcePath1 = "sourcePath1";
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String newSource  = InstallTest.targetPath + sourcePath1 + File.separator;
   String newSource1 = InstallTest.targetPath + sourcePath1 ;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message  = getMRIResource().getString("RESULT_PACKAGE_NOT_NEED_UPDATED");
   message = substitute(message,"ACCESS");
   String message1  = getMRIResource().getString("RESULT_PACKAGE_NEEDS_UPDATED");
   message1 = substitute(message1,"access");
   String message2  = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message2 = substitute(message2,"access");

   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");


        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST",    "TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.

        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");
        // Setup source1 to install from.
        // Create PKG file.
        InstallTest.writeFile(newSource + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(newSource + "ACCESS.LVL","ListFile1\nListFile2\n");

        // Create package LST files.
        InstallTest.writeFile(newSource + "ACCESS.LST",    "TEST1.ZIP\nTEST2.ZIP\nTEST3.ZIP\n");

        // Create product files.
        InstallTest.writeFile(newSource + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(newSource + "TEST2.ZIP","test2 zip\n");
        InstallTest.writeFile(newSource + "TEST3.ZIP","test3 zip\n");

        // Create change LST files.

        InstallTest.writeFile(newSource + "listFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");
        InstallTest.writeFile(newSource + "listFile2.LST",
                         "ACCESS   ADD  NN   TEST3.ZIP\n");

        String prefix;

        if (source1.startsWith("/"))
           prefix = "file:";
        else
           prefix = "file:/";
           
        String com = " -s  " + prefix + source1 + " -t "+target1+ " -p ACCESS -i ";
        
        com = " -p ACCESS -s  " + prefix + source1+ " -t "+target1+ " -c ";
        int exitVal = executeCommand (com);

        com = " -s  " + prefix + source1+ " -t "+target1+ " -c ";
        exitVal = executeCommand (com);

        com = " -p ACCESS -s " + prefix + newSource1+ " -t "+target1+ " -c ";
        exitVal = executeCommand (com);
        
        com = " -p ACCESS -s " + prefix + newSource1+ " -t "+target1+ " -i ";
        exitVal = executeCommand (com);

        succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
        file = new File(target+"TEST3.ZIP");
        file.delete();
        file = new File(newSource +"TEST1.ZIP");
        file.delete();
        file = new File(newSource+"TEST2.ZIP");
        file.delete();
        file = new File(newSource+"ACCESS.LST");
        file.delete();
        file = new File(newSource+"ACCESS.LVL");
        file.delete();
        file = new File(newSource+"JT400.PKG");
        file.delete();
        file = new File(newSource+"TEST3.ZIP");
        file.delete();
        file = new File(newSource+"ListFile1.LST");
        file.delete();
        file = new File(newSource+"ListFile2.LST");
        file.delete();
        file = new File(newSource1);
        file.delete();

    }
}

/**
Verify that the package is not installed successfully when the parameters are
 " -p <packageName> -s  -t <target path> -i".
**/
public void Var019 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message = getMRIResource().getString("ERR_MISSING_OPTION_VALUE");

   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com=" -p  ACCESS -s -t  "+ target1+" -i";

        int exitVal=executeCommand(com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!infoOutput_.toUpperCase().startsWith (message.toUpperCase()))
            failed(" Prompting message is not correct!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
 }

/**
Verify that the package is not installed successfully when the parameters are
 " -p <packageName> -s <source path> -t -i ".
**/
public void Var020 ()
{
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message  = getMRIResource().getString("ERR_MISSING_OPTION_VALUE");
   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.
        InstallTest.writeFile(source + "ListFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String com=" -p  ACCESS  -s  file:/"+source1+" -t -i";

        int exitVal = executeCommand (com);

        if(exitVal != 0)
            failed(" Error in running AS400ToolboxInstaller application!");

        else if(!infoOutput_.toUpperCase().startsWith (message.toUpperCase()))
            failed(" Prompting message is not correct!");

        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}


/**
Verify that the source package is installed when the version is changed.
**/
public void Var021 ()
{
   String sourcePath1 = "sourcePath1";
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String newSource  = InstallTest.targetPath + sourcePath1 + File.separator;
   String newSource1 = InstallTest.targetPath + sourcePath1 ;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message1 = getMRIResource().getString("RESULT_PACKAGE_NOT_NEED_UPDATED");
   message1 = substitute(message1,"access");
   String message2 = getMRIResource().getString("RESULT_PACKAGE_NEEDS_UPDATED");
   message2 = substitute(message2,"access");
   String message3 = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message3 = substitute(message3,"access");

   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","V1R1M1\n");


        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST", "TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.

        InstallTest.writeFile(source + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");
        // Setup source1 to install from.
        // Create PKG file.
        InstallTest.writeFile(newSource + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(newSource + "ACCESS.LVL","V2R1M1\n");

        // Create package LST files.
        InstallTest.writeFile(newSource + "ACCESS.LST",    "TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(newSource + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(newSource + "TEST2.ZIP","test211 zip\n");


        // Create change LST files.

        InstallTest.writeFile(newSource + "V2R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String prefix;
        if (source1.startsWith("/"))
           prefix = "file:";
        else
           prefix = "file:/";
           
        String com=" -s  " + prefix + source1+ " -t "+target1+ " -p ACCESS -i ";
        int exitVal = executeCommand (com);
        if( exitVal !=0)
        {
            failed(" Error in running AS400ToolboxInstaller application!");
        }
        else
        {
            com=" -s  " + prefix + source1+ " -t "+target1+ " -p ACCESS -c ";
            int exitVal1 = executeCommand (com);
            boolean b1 =  infoOutput_.toUpperCase().startsWith(message1.toUpperCase());

            com=" -s  " + prefix + newSource1+ " -t "+target1+ " -p ACCESS -c ";
            int exitVal2 = executeCommand (com);
            boolean b2 =  infoOutput_.toUpperCase().startsWith(message2.toUpperCase());

            com=" -s  " + prefix + newSource1+ " -t "+target1+ " -p ACCESS -i ";
            int exitVal3 = executeCommand (com);
            boolean b3 =  infoOutput_.toUpperCase().startsWith(message3.toUpperCase());

            if(!b1)
                failed(" Error in comparring the source packages with target packages!");
            else if(!b2)
               failed(" Error in comparring the source packages with target packages!");
            else if (!b3)
               failed(" Error in installing packages !");
            else
                succeeded();
        }
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(source + "V1R1M1.LST");
        file.delete();
        file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
        file = new File(target+"TEST3.ZIP");
        file.delete();
        file = new File(newSource +"TEST1.ZIP");
        file.delete();
        file = new File(newSource+"TEST2.ZIP");
        file.delete();
        file = new File(newSource+"V2R1M1.LST");
        file.delete();
        file = new File(newSource+"ACCESS.LST");
        file.delete();
        file = new File(newSource+"ACCESS.LVL");
        file.delete();
        file = new File(newSource+"JT400.PKG");
        file.delete();
        file = new File(newSource+"TEST3.ZIP");
        file.delete();
        file = new File(newSource1);
        file.delete();

    }
}

/**
Verify that the source package is installed when the version is changed.
**/
public void Var022 ()
{
   String sourcePath1 = "sourcePath1";
   String source  = InstallTest.targetPath + sourcePath + File.separator;
   String newSource  = InstallTest.targetPath + sourcePath1 + File.separator;
   String newSource1 = InstallTest.targetPath + sourcePath1 ;
   String target  = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message1 = getMRIResource().getString("RESULT_PACKAGE_NOT_NEED_UPDATED");
   message1 = substitute(message1,"access");
   String message2 = getMRIResource().getString("RESULT_PACKAGE_NEEDS_UPDATED");
   message2 = substitute(message2,"access");
   String message3 = getMRIResource().getString("RESULT_PACKAGE_INSTALLED");
   message3 = substitute(message3,"access");
   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","V1R1M1\n");


        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test2 zip\n");

        // Create change LST files.

        InstallTest.writeFile(source + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");
        // Setup source1 to install from.
        // Create PKG file.
        InstallTest.writeFile(newSource + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(newSource + "ACCESS.LVL","V2R1M1\n");

        // Create package LST files.
        InstallTest.writeFile(newSource + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\nTest3.zip\n");

        // Create product files.
        InstallTest.writeFile(newSource + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(newSource + "TEST2.ZIP","test211 zip\n");
        InstallTest.writeFile(newSource + "TEST3.ZIP","test311 zip\n");

        // Create change LST files.

        InstallTest.writeFile(newSource + "V2R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n"+
                         "ACCESS   ADD  NN   TEST3.ZIP\n");

        String prefix;
        if (source1.startsWith("/"))
           prefix = "file:";
        else
           prefix = "file:/";
           
        String com=" -s  " + prefix + source1+ " -t "+target1+ " -p ACCESS -i ";
        int exitVal = executeCommand (com);
        if(exitVal !=0)
        {
            failed(" Error in running AS400ToolboxInstaller application!");
        }
        else
        {
            com=" -s  " + prefix + source1+ " -t "+target1+ " -p ACCESS -c ";
            int exitVal1 = executeCommand (com);
            boolean b1 =  infoOutput_.toUpperCase().startsWith(message1.toUpperCase());

            com=" -s  " + prefix + newSource1+ " -t "+target1+ " -p ACCESS -c ";
            int exitVal2 = executeCommand (com);
            boolean b2 =  infoOutput_.toUpperCase().startsWith(message2.toUpperCase());

            com=" -s  " + prefix + newSource1+ " -t "+target1+ " -p ACCESS -i ";
            int exitVal3 = executeCommand (com);
            boolean b3 =  infoOutput_.toUpperCase().startsWith(message3.toUpperCase());
     
            if(!b1)
                failed(" Error in comparring the source packages with target packages!");
            else if(!b2)
               failed(" Error in comparring the source packages with target packages!");
            else if (!b3)
               failed(" Error in installing packages !");
            else
                succeeded();
       }
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(target+"TEST1.ZIP");
        file.delete();
        file = new File(source+"V1R1M1.LST");
        file.delete();
        file = new File(target+"TEST2.ZIP");
        file.delete();
        file = new File(target+"ACCESS.LST");
        file.delete();
        file = new File(target+"ACCESS.LVL");
        file.delete();
        file = new File(target+"JT400.PKG");
        file.delete();
        file = new File(target+"TEST3.ZIP");
        file.delete();
        file = new File(newSource +"TES1.ZIP");
        file.delete();
        file = new File(newSource +"V2R1M1.LST");
        file.delete();
        file = new File(newSource+"TES2.ZIP");
        file.delete();
        file = new File(newSource+"ACCESS.LST");
        file.delete();
        file = new File(newSource+"ACCESS.LVL");
        file.delete();
        file = new File(newSource+"JT400.PKG");
        file.delete();
        file = new File(newSource+"TEST3.ZIP");
        file.delete();
        file = new File(newSource1);
        file.delete();

    }
}

/**
Verify that the package is not installed successfully when the parameters are
invalid.
**/
public void Var023 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   String message1 = getMRIResource().getString("ERR_NOT_VALID_URL");
   String message2 = getMRIResource().getString("ERR_TOO_MANY_OPTIONS");
   String message3 = getMRIResource().getString("ERR_OPTION_NOT_COMPATIBLE");
   String message4 = getMRIResource().getString("ERR_NO_I_U_C");
   String message5 = getMRIResource().getString("WARNING_SOURCE_URL_NOT_USED");
   String message6 = getMRIResource().getString("EXC_NO_PACKAGES_INSTALLED");
   String message7 = getMRIResource().getString("ERR_MISSING_OPTION_VALUE");
   String message8 = getMRIResource().getString("ERR_MISSING_OPTION");
   String message9 = getMRIResource().getString("RESULT_PACKAGE_NEEDS_UPDATED");
   message9 = substitute(message9,"ACCESS");
   String message10 = getMRIResource().getString("ERR_UNEXPECTED_OPTION");

   try
   {

        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(source + "JT400.PKG","ACCESS\n");

        // Create LVL files.
        InstallTest.writeFile(source + "ACCESS.LVL","ListFile1\n");

        // Create package LST files.
        InstallTest.writeFile(source + "ACCESS.LST","TEST1.ZIP\nTEST2.ZIP\n");

        // Create product files.
        InstallTest.writeFile(source + "TEST1.ZIP","test1 zip\n");
        InstallTest.writeFile(source + "TEST2.ZIP","test zip2\n");

        // Create change LST files.
        InstallTest.writeFile(source + "listFile1.LST",
                         "ACCESS   ADD  NN   TEST1.ZIP\n"+
                         "ACCESS   ADD  NN   TEST2.ZIP\n");

        String prefix1;
        String prefix2;
        
        if (source1.startsWith("/"))
        {
           prefix1 = "filek:";
           prefix2 = "file:";
        }
        else
        {
           prefix1 = "filek:/";
           prefix2 = "file:/";
        }
           
        String com = " -p ACCESS -s " + prefix1 + source1+" -t "+target1+ " -i";
        int exitVal = executeCommand (com);
        boolean b1 = infoOutput_.startsWith(message1);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -i -s";
        exitVal = executeCommand (com);
        boolean b2 = infoOutput_.startsWith(message2);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -i -t";
        exitVal = executeCommand (com);
        boolean b3 = infoOutput_.startsWith(message2);

        com = " -p ACCESS  -s " + prefix2 + source1+" -t "+target1+ " -i -p";
        exitVal = executeCommand (com);
        boolean b4 = infoOutput_.startsWith(message2);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -i -i";
        exitVal = executeCommand (com);
        boolean b5 = infoOutput_.startsWith(message2);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -c -c";
        exitVal = executeCommand (com);
        boolean b6 = infoOutput_.startsWith(message2);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -pr -pr";
        exitVal = executeCommand (com);
        boolean b7 = infoOutput_.startsWith(message2);

        com = " -p ACCESS -t "+target1+ " -u -u";
        exitVal = executeCommand (com);
        boolean b8 = infoOutput_.startsWith(message2);
 
        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -i -pr -pr";
        exitVal = executeCommand (com);
        boolean b9 = infoOutput_.startsWith(message2);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -i -c";
        exitVal = executeCommand (com);
        boolean b10 = infoOutput_.startsWith(message3);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -c -i";
        exitVal = executeCommand (com);
        boolean b11 = infoOutput_.startsWith(message3);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1;
        exitVal = executeCommand (com);
        boolean b12 = infoOutput_.startsWith(message4);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -c -u";
        exitVal = executeCommand (com);
        boolean b13 = infoOutput_.startsWith(message3);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -i -u";
        exitVal = executeCommand (com);
        boolean b14 = infoOutput_.startsWith(message3);

        com = " -s " + source1 + " -u";
        exitVal = executeCommand (com);
        boolean b15 = infoOutput_.startsWith(message5);

        com = " -t z:\\aa  -u";
        exitVal = executeCommand (com);
        boolean b16 = infoOutput_.startsWith(message6);

        com = " -t  -u";
        exitVal = executeCommand (com);
        boolean b17 = infoOutput_.startsWith(message7);

        com = " -p ACCESS  -t "+target1+ " -c ";
        exitVal = executeCommand (com);
        boolean b18 = infoOutput_.startsWith(message8);

        com = " -p ACCESS  -s "+source1 + " -c";
        exitVal = executeCommand (com);
        boolean b19 = infoOutput_.startsWith(message8);

        com = " -s " + prefix2 + source1+" -t "+target1+ " -c ";
        exitVal = executeCommand (com);
        boolean b20 = infoOutput_.toUpperCase().startsWith(message9.toUpperCase());

        com = " -s file:\\z:\\no_exist -t c:\\aa -c ";
        exitVal = executeCommand (com);
        boolean b21 = infoOutput_.startsWith("java.io.FileNotFoundException");


        com = " -s file:\\z:\\dd -t z:\\aa -i ";
        exitVal = executeCommand (com);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -j";
        exitVal = executeCommand (com);
        boolean b22 = infoOutput_.startsWith(message10);

        com = " -p ACCESS -s " + prefix2 + source1+" -t "+target1+ " -u";
        exitVal = executeCommand (com);
        boolean b23 = infoOutput_.startsWith(message5);

        com = " -?";
        exitVal = executeCommand (com);
        boolean b24 = infoOutput_.indexOf("Usage")<3;

        if(!b1)
            failed(" The prompting message is not matched the value of message1!");
        else if(!b2)
            failed(" The prompting message is not matched the value of message2!");
        else if(!b3)
            failed(" The prompting message is not matched the value of message2!");
        else if(!b4)
            failed(" The prompting message is not matched the value of message2!");
        else if(!b5)
            failed(" The prompting message is not matched the value of message2!");

        else if(!b6)
            failed(" The prompting message is not matched the value of message2!");

        else if(!b7)
            failed(" The prompting message is not matched the value of message2!");

        else if(!b8)
            failed(" The prompting message is not matched the value of message2!");

        else if(!b9)
            failed(" The prompting message is not matched the value of message2!");

        else if(!b10)
            failed(" The prompting message is not matched the value of message3!");

        else if(!b11)
            failed(" The prompting message is not matched the value of message3!");

        else if(!b12)
            failed(" The prompting message is not matched the value of message4!");

        else if(!b13)
            failed(" The prompting message is not matched the value of message3!");

        else if(!b14)
            failed(" The prompting message is not matched the value of message3!");

        else if(!b15)
            failed(" The prompting message is not matched the value of message5!");

        else if(!b16)
            failed(" The prompting message is not matched the value of message6!");

        else if(!b17)
            failed(" The prompting message is not matched the value of message7!");

        else if(!b18)
            failed(" The prompting message is not matched the value of message8!");

        else if(!b19)
            failed(" The prompting message is not matched the value of message8!");
        else if(!b20)
            failed(" The prompting message is not matched the value of message9!");
        else if(!b21)
            failed(" java.io.FileNotFoundException!");
        else if(!b22)
            failed(" The prompting message is not matched the value of message10!");
        else if(!b23)
            failed(" The prompting message is not matched the value of message5!");
        else if(!b24)
            failed(" The prompting message is not matched the message of help!");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
}

/**
Verify that the message display when the version is changed.
**/

public void Var024 ()
{
/*   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
 */
   // Make sure classpath set as required by this test.
   try {
       if (!checkClasspath())
       {
           failed("Cannot run, CLASSPATH not correct.");
           return;
       }
   }
   catch (Exception e)
   {
       failed("Cannot run, CLASSPATH can't be accessed.");
       return;
   }
   try
    {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG",
                              "ACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\nV1R1M2\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
              "TEST1.ZIP\nTEST2.ZIP\nTEST3.ZIP\nTEST4.JAR\nTEST5.JAR\n" +
              "TEST6.JAR\nTEST7.ZIP\nTEST8.ZIP\nTEST9.ZIP\n");
    
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST2.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST4.JAR",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST5.JAR",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST6.JAR",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST7.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST8.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST9.ZIP",
                              "yippee\n");
    
        // Create change LST files.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NY   TEST1.ZIP\n" +
                         "ACCESS   ADD  NY   TEST2.ZIP\n" +
                         "ACCESS   ADD  NY   TEST3.ZIP\n" +
                         "ACCESS   UPD  NY   TEST4.JAR\n" +
                         "ACCESS   UPD  NY   TEST5.JAR\n" +
                         "ACCESS   UPD  NY   TEST6.JAR\n" +
                         "ACCESS   RMV  NY   TEST7.ZIP\n" +
                         "ACCESS   RMV  NY   TEST8.ZIP\n" +
                         "ACCESS   RMV  NY   TEST9.ZIP\n" +
                         "ACCESS   PADD NY   TEST1\n" +
                         "ACCESS   PADD NY   TEST2\n" +
                         "ACCESS   PRMV NY   TEST3\n" +
                         "ACCESS   PRMV NY   TEST4\n" );
    
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M2.LST",
                         "ACCESS   ADD  NY   TEST1.ZIP\n" +
                         "ACCESS   UPD  NY   TEST2.ZIP\n" +
                         "ACCESS   RMV  NY   TEST3.ZIP\n" +
                         "ACCESS   ADD  NY   TEST4.JAR\n" +
                         "ACCESS   UPD  NY   TEST5.JAR\n" +
                         "ACCESS   RMV  NY   TEST6.JAR\n" +
                         "ACCESS   ADD  NY   TEST7.ZIP\n" +
                         "ACCESS   UPD  NY   TEST8.ZIP\n" +
                         "ACCESS   RMV  NY   TEST9.ZIP\n" +
                         "ACCESS   PADD NY   TEST1\n" +
                         "ACCESS   PRMV NY   TEST2\n" +
                         "ACCESS   PADD NY   TEST3\n" +
                         "ACCESS   PRMV NY   TEST4\n" );

       String com;
       if (InstallTest.targetPath.startsWith("/"))
          com = "-s  file:"+InstallTest.targetPath+
                " -t "+InstallTest.targetPath+ "install"+File.separator+"test  -i ";
       else
          com = "-s  file:/"+InstallTest.targetPath+
                " -t "+InstallTest.targetPath+ "install"+File.separator+"test  -i ";
       int exitVal1 = executeCommand (com);
       assertCondition(exitVal1==0);
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        file = new File(InstallTest.targetPath + "V1R1M2.LST");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST4.JAR");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST5.JAR");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST6.JAR");
        file.delete();
        file = new File(InstallTest.targetPath + "ACCESS.LST");
        file.delete();
        file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        file = new File(InstallTest.targetPath + "JT400.PKG");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST1.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST2.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST3.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST7.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST8.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST9.ZIP");
        file.delete();
        String testdir = InstallTest.targetPath+"install"+File.separator +"test"+File.separator;
        file = new File(testdir + "ACCESS.LST");
        file.delete();
        file = new File(testdir + "ACCESS.LVL");
        file.delete();
        file = new File(testdir + "JT400.pkg");
        file.delete();
        file = new File(testdir + "TEST1.ZIP");
        file.delete();
        file = new File(testdir + "TEST2.ZIP");
        file.delete();
        file = new File(testdir);
        file.delete();
        testdir = InstallTest.targetPath+"install";
        file = new File(testdir);
        file.delete();
    }

 }

 /**
Verify that the message display when the version is changed.
**/

public void Var025 ()
{
   String source = InstallTest.targetPath + sourcePath + File.separator;
   String target = InstallTest.targetPath + targetPath + File.separator;
   String source1 = InstallTest.targetPath + sourcePath ;
   String target1 = InstallTest.targetPath + targetPath ;
   // Make sure classpath set as required by this test.
   try {
       if (!checkClasspath())
       {
           failed("Cannot run, CLASSPATH not correct.");
           return;
       }
    }
   catch (Exception e)
   {
       failed("Cannot run, CLASSPATH can't be accessed.");
       return;
   }

   try
   {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG",
                              "ACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\nV1R1M2\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
              "TEST1.ZIP\nTEST2.ZIP\nTEST3.ZIP\nTEST4.JAR\nTEST5.JAR\n" +
              "TEST6.JAR\nTEST7.ZIP\nTEST8.ZIP\nTEST9.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST2.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST4.JAR",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST5.JAR",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST6.JAR",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST7.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST8.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST9.ZIP",
                              "yippee\n");
        // Create change LST files.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NY   TEST1.ZIP\n" +
                         "ACCESS   ADD  NY   TEST2.ZIP\n" +
                         "ACCESS   ADD  NY   TEST3.ZIP\n" +
                         "ACCESS   UPD  NY   TEST4.JAR\n" +
                         "ACCESS   UPD  NY   TEST5.JAR\n" +
                         "ACCESS   UPD  NY   TEST6.JAR\n" +
                         "ACCESS   RMV  NY   TEST7.ZIP\n" +
                         "ACCESS   RMV  NY   TEST8.ZIP\n" +
                         "ACCESS   RMV  NY   TEST9.ZIP\n" +
                         "ACCESS   PADD NY   TEST1\n" +
                         "ACCESS   PADD NY   TEST2\n" +
                         "ACCESS   PRMV NY   TEST3\n" +
                         "ACCESS   PRMV NY   TEST4\n" );
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M2.LST",
                         "ACCESS   ADD  NY   TEST1.ZIP\n" +
                         "ACCESS   UPD  NY   TEST2.ZIP\n" +
                         "ACCESS   RMV  NY   TEST3.ZIP\n" +
                         "ACCESS   ADD  NY   TEST4.JAR\n" +
                         "ACCESS   UPD  NY   TEST5.JAR\n" +
                         "ACCESS   RMV  NY   TEST6.JAR\n" +
                         "ACCESS   ADD  NY   TEST7.ZIP\n" +
                         "ACCESS   UPD  NY   TEST8.ZIP\n" +
                         "ACCESS   RMV  NY   TEST9.ZIP\n" +
                         "ACCESS   PADD NY   TEST1\n" +
                         "ACCESS   PRMV NY   TEST2\n" +
                         "ACCESS   PADD NY   TEST3\n" +
                         "ACCESS   PRMV NY   TEST4\n" );
       
       String com;
       if (InstallTest.targetPath.startsWith("/"))
          com="-s  file:"+InstallTest.targetPath+
                " -t "+InstallTest.targetPath+ "install"+File.separator+"test  -i ";
       else
          com="-s  file:/"+InstallTest.targetPath+
                " -t "+InstallTest.targetPath+ "install"+File.separator+"test  -i ";
       
       int exitVal1 = executeCommand (com);
       
       com=" -t "+InstallTest.targetPath+ "install"+File.separator+"test -u ";
       int exitVal2 = executeCommand (com);
       
       if(exitVal1!=0)
           failed(" Error in running installing package!");
       else if(exitVal2!=0)
           failed(" Error in running uninstalling package!");
       succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list files from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        file = new File(InstallTest.targetPath + "V1R1M2.LST");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST4.JAR");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST5.JAR");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST6.JAR");
        file.delete();
        file = new File(InstallTest.targetPath + "ACCESS.LST");
        file.delete();
        file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        file = new File(InstallTest.targetPath + "JT400.PKG");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST1.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST2.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST3.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST7.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST8.ZIP");
        file.delete();
        file = new File(InstallTest.targetPath + "TEST9.ZIP");
        file.delete();
        String testdir = InstallTest.targetPath+"install"+File.separator +"test"+File.separator;
        file = new File(testdir + "ACCESS.LST");
        file.delete();
        file = new File(testdir + "ACCESS.LVL");
        file.delete();
        file = new File(testdir + "JT400.PKG");
        file.delete();
        file = new File(testdir + "TEST1.ZIP");
        file.delete();
        file = new File(testdir + "TEST2.ZIP");
        file.delete();
        file = new File(testdir);
        file.delete();
        testdir = InstallTest.targetPath+"install";
        file = new File(testdir);
        file.delete();
    }
 }

/**
Verify that the package is installed successfully when the parameters are
 " -s <system name> -t <target path> -i".
**/
public void Var026 ()
{
   String target1 = InstallTest.targetPath + targetPath ;
   String message1 = getMRIResource().getString("ERR_NOT_VALID_URL");
   try
   {
        String com = " -s "+InstallTest.systemName +"  -t "+ target1+" -i ";
        File targetDir = new File(target1);
        int len1 = targetDir.list().length;
        int exitVal1 = executeCommand (com);
        if(exitVal1 !=0)
        {
            failed(" Error in running AS400ToolboxInstaller application!");
        }
        else if(infoOutput_.startsWith(message1))
            failed(" system name is not correct : "+InstallTest.systemName);
        else
        {
            int len2 = targetDir.list().length;
            com = "  -t "+ target1+" -u ";
            exitVal1 = executeCommand (com);
     
      //      if(len1 != 0)
      //          failed(" Setup warning, the target directory is not empty !");
        
            if(len2 ==0)
            {
                failed(" The package is not installed successfully!");
                System.out.println("NOTE TO TESTER: This is the first variation in this Testcase that actually goes out to the server to get files.");
                System.out.println("Suggestion: Verify that the HTTP Server is started (STRTCPSVR *HTTP), and that WRKHTTPCFG shows the line \"Pass /* /*\".");
            }
         
            else
                assertCondition(exitVal1 ==0);
        }
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }

/**
Verify that the package is installed successfully when the parameters are
 " -s <http://systemName/...> -t <target path> -i".
**/
public void Var027 ()
{
   String target1 = InstallTest.targetPath + targetPath ;

   try
   {
        String com = " -s http://"+InstallTest.systemName +"/QIBM/ProdData/HTTP/Public/jt400/  -t "+ target1+" -i ";
        File targetDir = new File(target1);
        if (!targetDir.exists())
        {
           targetDir.mkdirs();
        }

        int len1 = targetDir.list().length;
        int exitVal1 = executeCommand (com);
        if(exitVal1 !=0)
        {
            failed(" Error in running AS400ToolboxInstaller application!");
        }
        else if(infoOutput_.startsWith("java.net.UnknownHostException"))
            failed("java.net.UnknownHostException: "+ InstallTest.systemName);
        else
        {
            int len2 = targetDir.list().length;
            com = "  -t "+ target1+" -u ";
            exitVal1 = executeCommand (com);
            if(len1 != 0)
                failed(" The target directory is not empty !");
            else if(len2 ==0)
                failed(" The package is not installed successfully!");
            else
                assertCondition(exitVal1 ==0);
        }
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }

  }

/**
Verify that the package is installed successfully when the parameters are
 " -s <http://systemName/...> -t <target path> -c".
**/
public void Var028 ()
{
   String target1 = InstallTest.targetPath + targetPath ;
   try
   {
        String com = " -s http://"+InstallTest.systemName +"/QIBM/ProdData/HTTP/Public/jt400/  -t "+ target1+" -i ";
        int exitVal1 = executeCommand (com);
        if(exitVal1 !=0)
        {
            failed(" Error in running AS400ToolboxInstaller application!");
        }
        else if(infoOutput_.startsWith("java.net.UnknownHostException"))
            failed("java.net.UnknownHostException: "+ InstallTest.systemName);
        else
        {

            com = " -s http://"+InstallTest.systemName +"/QIBM/ProdData/HTTP/Public/jt400/  -t "+ target1+" -c ";
            exitVal1 = executeCommand (com);
            assertCondition(exitVal1 ==0);
            com = " -t "+ target1+" -u ";
            exitVal1 = executeCommand (com);
        }
    }
    catch (Exception e)
    {
        failed (e, "Unexpected Exception");
    }

  }
} // End of class

