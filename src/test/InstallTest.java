///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  InstallTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Vector;

import test.IN.INAppTestcase;
import test.IN.INAttributesTestcase;
import test.IN.INInstallTestcase;
import test.IN.INIsInstalledTestcase;
import test.IN.INIsUpdateNeededTestcase;
import test.IN.INUnInstallTestcase;

import java.util.Enumeration;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
Test driver for the Install component.
See TestDriver for calling syntax.
@see TestDriver
**/
public class InstallTest extends TestDriver
{

// The following variables all have trailing separators.
static public String targetPath; // Path on the local system
static public String systemName;
static public URL localURL;      // File:targetPath
static public URL remoteURL;     // http://-system/QIBM/ProdData/HTTP/Public/jt400/
static public URL remoteURL2;    // ftp://-system/QIBM/ProdData/HTTP/Public/jt400/

private static byte[] data_ = new byte[2560];

public static boolean runningAIX_;
public static boolean runningUNIX_;

/**
Write the data in <i>contents</i> to <i>targetFile</i>.
If the target file exists, it will be replaced, not appended to.

@param     targetFile  File (on the local system) to write the data into.
@param     contents    Contetns to write to the file.

@exception IOException if any errors occur.
**/
public static void writeFile(String targetFile, String contents)
    throws IOException
{
    // Make sure the target directory exists.
    if ((new File(targetFile)).getParent() != null)
    {
      File dir = new File((new File(targetFile)).getParent());
      if (!dir.exists())
      {
        if (!dir.mkdirs())
        {
            throw new IOException("CANNOT_CREATE_DIRECTORY: " + dir);
        }
      }
    }
    // Write out data.
    PrintWriter out = new PrintWriter(new FileOutputStream(targetFile));
    out.print(contents);
    out.close();
}


/**
Returns the contents of the file.

@param     file  The file to read.

@return    contents of the file

@exception IOException if any errors occur.
**/
public static String readFile(String fileName)
    throws IOException
{
    FileInputStream file = new FileInputStream(fileName);

    StringBuffer s = new StringBuffer();
    int n = file.read(data_);
    while (n != -1)
    {
      s.append(new String(data_, 0, n));
      n = file.read(data_);
    }
    file.close();
    return s.toString();
}


/**
Returns the contents of the file located at the URL <i>url</i>.

@param     url  URL of the file to read.

@return    contents of the file

@exception IOException if any errors occur.
**/
public static String readFile(URL url)
    throws IOException
{
    InputStream in = url.openStream();
    String s = "";
    int n = in.read(data_);
    while ( n!= -1)
    {
        s += new String(data_, 0, n);
        n = in.read(data_);
    }
    in.close();
    return s;
}



/**
Main for running standalone application tests.
**/
public static void main(String args[])
{
    try {
      InstallTest example = new InstallTest(args);
      example.init();
      example.start();
      example.stop();
      example.destroy();
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

    // Needed to make the virtual machine quit.
       System.exit(0);
}


/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
public InstallTest()
       throws Exception
{
    super();
}


/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
public InstallTest(String[] args)
       throws Exception
{
    super(args);
}


/**
Creates Testcase objects for all the testcases in this component.
**/
public void createTestcases()
{
    // do some setup
    if (systemName_==null)
    {
      System.out.println("-system flag not specified.");
      System.out.println("Variations 26,27,28 need the parameter.");
      System.out.println("Example: -system Toolbox");
      System.exit(1);
    }
    else if (directory_==null)
    {
      //Changed -misc option to -directory 
      System.out.println("-directory flag not specified.");
      System.out.println("It must be used to specify a fully-qualifieed local directory to work in.");
      System.out.println("Example: -directory c:\\test");
      System.exit(1);
    }
    else
    {
        systemName = systemName_;
        try {
            remoteURL = new URL("http", systemObject_.getSystemName(),
                        "/QIBM/ProdData/HTTP/Public/jt400/");
            remoteURL2 = new URL("ftp", systemObject_.getSystemName(),
                        "/QIBM/ProdData/HTTP/Public/jt400/");
            targetPath = directory_;
            // make sure targetPath has trailing separator
            if (!targetPath.substring(targetPath.length()).equals(File.separator) &&
                !targetPath.substring(targetPath.length()-1).equals(File.separator))
                    targetPath = targetPath + File.separator;

            if (targetPath.startsWith("/"))
               localURL = new URL("file:" + targetPath);
            else
               localURL = new URL("file:/" + targetPath);
        }
        catch (Exception e) {}
    }

      runningAIX_ = JTOpenTestEnvironment.isAIX; 

    if (JTOpenTestEnvironment.isAIX  ||JTOpenTestEnvironment.isLinux) {
      runningUNIX_ = true;
    }
    else runningUNIX_ = false;

    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);
 
    if (allTestcases || namesAndVars_.containsKey("INAppTestcase"))
    {
      INAppTestcase tc =
        new INAppTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("INAppTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("INAppTestcase");
    }
    if (allTestcases || namesAndVars_.containsKey("INAttributesTestcase"))
    {
      INAttributesTestcase tc =
        new INAttributesTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("INAttributesTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("INAttributesTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("INInstallTestcase"))
    {
      INInstallTestcase tc =
        new INInstallTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("INInstallTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("INInstallTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("INIsInstalledTestcase"))
    {
      INIsInstalledTestcase tc =
        new INIsInstalledTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("INIsInstalledTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("INIsInstalledTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("INIsUpdateNeededTestcase"))
    {
      INIsUpdateNeededTestcase tc =
        new INIsUpdateNeededTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("INIsUpdateNeededTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("INIsUpdateNeededTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("INUnInstallTestcase"))
    {
      INUnInstallTestcase tc =
        new INUnInstallTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("INUnInstallTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("INUnInstallTestcase");
    }

    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
}


}

