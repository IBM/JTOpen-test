///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INAttributesTestcase.java
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

import java.util.Vector;
import java.net.URL;
import com.ibm.as400.access.AS400;

import test.InstallTest;
import test.Testcase;
import utilities.AS400ToolboxInstaller;

/**
Testcase INAttributesTestcase.
Tests the following methods:
<ul>
<li>AS400ToolboxInstaller.getClasspathAdditions
<li>AS400ToolboxInstaller.getClasspathRemovals
<li>AS400ToolboxInstaller.getUnexpandedFiles
</ul>
<p><b>Note:</b>
<br>
Many of the variations in this testcase are dependent on the
CLASSPATH to work correctly.  checkClasspath() verifies that
the CLASSPATH contains the correct items.  If the CLASSPATH
is not set up correctly, a variation will not be attempted.
The variations do not need conflicting CLASSPATHs, if the
CLASSPATH is set up correctly, all variations dependent on
the CLASSPATH should be attempted.
**/
public class INAttributesTestcase extends Testcase
{

    String targetNoSep = InstallTest.targetPath +
                         "install" + File.separator + "test";
    String target = targetNoSep + File.separator;

/**
Constructor.  This is called from InstallTest::createTestcases().
**/
public INAttributesTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
{
    super(systemObject, "INAttributesTestcase", 22,
          variationsToRun, runMode, fileOutputStream);
}

/**
Runs the variations requested.
**/
public void run()
{
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }
    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }
    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }
    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }
    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }
    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }
    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }
    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }
    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }
    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }
    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }
    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }
    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }
    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }
    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }
    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }
    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }
    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }
    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }
    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }

    // Since we use install/test as the target directory,
    // the install directory will never be cleaned up
    // (the test directory will be cleaned up by uninstalls).
    // Attempt to clean it up in <-misc> and current dir.
    File file = new File(InstallTest.targetPath + "install");
    file.delete();
    file = new File("install" + File.separator + "test");
    file.delete();
}


/**
Checks to see if <i>path</i> is in the CLASSPATH.

@param     path   Path to check the CLASSPATH for.

@return    true if <i>path</i> is in the CLASSPATH; false otherwise

@exception SecurityException If the CLASSPATH cannot be accessed.
**/
private static boolean inCP(String path)
{
    // Search with leading and trailing semicolons to make sure
    // paths match exactly.  Uppercase to make our check case-
    // insensitive.
    String separator = System.getProperty("path.separator");
    String classpath = separator +
                       System.getProperty("java.class.path") +
                       separator;
    classpath = classpath.toUpperCase();
    path = path.toUpperCase();
    if (classpath.lastIndexOf(separator + path + separator) != -1)
        return true;
    return false;
}


/**
Checks the CLASSPATH to ensure it is set up to run the variations in
this testcase correctly.

This method ensures the following are IN the CLASSPATH:
<ol compact>
<li><-misc>\install\test;
<li><-misc>\install\test\TEST3.ZIP;
<li><-misc>\install\test\TEST6.JAR;
<li><-misc>\install\test\TEST9.ZIP;
<li><-misc>\install\test\test2;
<li><-misc>\install\test\test4;
<li>.\install\test;
<li>.\install\test\test2;
<li>.\install\test\TEST3.ZIP;
<li>.\install\test\TEST6.JAR;
</ol>

This method ensures the following are NOT in the CLASSPATH:
<ol compact>
<li><-misc>\install\test\TEST1.ZIP;
<li><-misc>\install\test\TEST2.ZIP;
<li><-misc>\install\test\TEST4.JAR;
<li><-misc>\install\test\TEST5.JAR;
<li><-misc>\install\test\TEST7.ZIP;
<li><-misc>\install\test\TEST8.ZIP;
<li><-misc>\install\test\test1;
<li><-misc>\install\test\test3;
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
    String relativeTarget =
        (new File("install" + File.separator + "test"))
        .getAbsolutePath();

    // Check for items that must be in CLASSPATH
    path = targetNoSep;
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
    path = relativeTarget;
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    relativeTarget = relativeTarget + File.separator;
    path = relativeTarget + "test2";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = relativeTarget + "TEST3.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) == -1)
    {
        output_.println("CLASSPATH does not contain " + path);
        results = false;
    }
    path = relativeTarget + "TEST6.JAR";
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
    path = relativeTarget + "test1";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = relativeTarget + "TEST1.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }
    path = relativeTarget + "TEST2.ZIP";
    if (classpath.lastIndexOf(separator + path.toUpperCase() + separator) != -1)
    {
        output_.println("CLASSPATH contains " + path);
        results = false;
    }

    return results;
}



/**
Attributes before any install operations.
**/
public void Var001()
{
    Vector results;
    results = AS400ToolboxInstaller.getClasspathAdditions();
    if (results.size() != 0)
    {
        failed("Classpath additions not empty.");
        output_.println(results);
    }
    else
    {
        results = AS400ToolboxInstaller.getClasspathRemovals();
        if (results.size() != 0)
        {
            failed("Classpath removals not empty.");
            output_.println(results);
        }
        else
        {
            results = AS400ToolboxInstaller.getUnexpandedFiles();
            if (results.size() != 0)
            {
                failed("Unexpanded files not empty.");
                output_.println(results);
            }
            else
                succeeded();
        }
    }
}

/**
After install, verify
all combinations of actions with classpath changes.
Requires the CLASSPATH be set up correctly.
**/
public void Var002()
{
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
                         "ACCESS   PADD NY   test1\n" +
                         "ACCESS   PADD NY   test2\n" +
                         "ACCESS   PRMV NY   test3\n" +
                         "ACCESS   PRMV NY   test4\n" );
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
                         "ACCESS   PADD NY   test1\n" +
                         "ACCESS   PRMV NY   test2\n" +
                         "ACCESS   PADD NY   test3\n" +
                         "ACCESS   PRMV NY   test4\n" );

        // Do the install.
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 4)
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        else if (!results.contains(target + "TEST2.ZIP"))
            failed = true;
        else if (!results.contains(target + "test1"))
            failed = true;
        else if (!results.contains(target + "test3"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "TEST1.ZIP");
            expected.addElement(target + "TEST2.ZIP");
            expected.addElement(target + "test1");
            expected.addElement(target + "test3");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 5)
                failed = true;
            else if (!results.contains(target + "TEST3.ZIP"))
                failed = true;
            else if (!results.contains(target + "TEST6.JAR"))
                failed = true;
            else if (!results.contains(target + "TEST9.ZIP"))
                failed = true;
            else if (!results.contains(target + "test2"))
                failed = true;
            else if (!results.contains(target + "test4"))
                failed = true;
            else
                succeeded();
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(target + "TEST3.ZIP");
                expected.addElement(target + "TEST6.JAR");
                expected.addElement(target + "TEST9.ZIP");
                expected.addElement(target + "test2");
                expected.addElement(target + "test4");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
        }
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
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
}

/**
After install, verify
all combinations of actions with files which should be expanded.
**/
public void Var003()
{
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
                         "ACCESS   ADD  YN   TEST1.ZIP\n" +
                         "ACCESS   ADD  YN   TEST2.ZIP\n" +
                         "ACCESS   ADD  YN   TEST3.ZIP\n" +
                         "ACCESS   UPD  YN   TEST4.JAR\n" +
                         "ACCESS   UPD  YN   TEST5.JAR\n" +
                         "ACCESS   UPD  YN   TEST6.JAR\n" +
                         "ACCESS   RMV  YN   TEST7.ZIP\n" +
                         "ACCESS   RMV  YN   TEST8.ZIP\n" +
                         "ACCESS   RMV  YN   TEST9.ZIP\n" +
                         "ACCESS   PADD YN   test1\n" +  // path actions should be ignored
                         "ACCESS   PADD YN   test2\n" +
                         "ACCESS   PRMV YN   test3\n" +
                         "ACCESS   PRMV YN   test4\n" );
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M2.LST",
                         "ACCESS   ADD  YN   TEST1.ZIP\n" +
                         "ACCESS   UPD  YN   TEST2.ZIP\n" +
                         "ACCESS   RMV  YN   TEST3.ZIP\n" +
                         "ACCESS   ADD  YN   TEST4.JAR\n" +
                         "ACCESS   UPD  YN   TEST5.JAR\n" +
                         "ACCESS   RMV  YN   TEST6.JAR\n" +
                         "ACCESS   ADD  YN   TEST7.ZIP\n" +
                         "ACCESS   UPD  YN   TEST8.ZIP\n" +
                         "ACCESS   RMV  YN   TEST9.ZIP\n" +
                         "ACCESS   PADD YN   test1\n" +  // path actions should be ignored
                         "ACCESS   PRMV YN   test2\n" +
                         "ACCESS   PADD YN   test3\n" +
                         "ACCESS   PRMV YN   test4\n" );

        // Do the install.
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getUnexpandedFiles();
        boolean failed = false;
        if (results.size() != 6)
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        else if (!results.contains(target + "TEST2.ZIP"))
            failed = true;
        else if (!results.contains(target + "TEST4.JAR"))
            failed = true;
        else if (!results.contains(target + "TEST5.JAR"))
            failed = true;
        else if (!results.contains(target + "TEST7.ZIP"))
            failed = true;
        else if (!results.contains(target + "TEST8.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Unexpanded files not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "TEST1.ZIP");
            expected.addElement(target + "TEST2.ZIP");
            expected.addElement(target + "TEST4.JAR");
            expected.addElement(target + "TEST5.JAR");
            expected.addElement(target + "TEST7.ZIP");
            expected.addElement(target + "TEST8.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else
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
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
}

/**
After uninstall, verify
classpath additions and unexpanded files are empty.
**/
public void Var004()
{
    try
    {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG",
                              "ACCESS");
        // Create LVL file.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1");
        // Create package LST file.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
           "TEST1.TXT\nTEST2.TXT");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.TXT",
                              "yippee");
        InstallTest.writeFile(InstallTest.targetPath + "TEST2.TXT",
                              "wow\nwee");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   TEST1.TXT\n" +
                         "ACCESS   ADD  YY   TEST2.TXT\n" );

        // Do install.
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
                                      InstallTest.localURL);

        // Verify classpath additions and unexpanded files are not empty.
        if (AS400ToolboxInstaller.getClasspathAdditions().size() == 0)
        {
            failed("Setup of classpath additions failed.");
        }
        else if (AS400ToolboxInstaller.getUnexpandedFiles().size() == 0)
        {
            failed("Setup of unexpanded files failed.");
        }
        else
        {
            // Do uninstall
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);

            // Verify classpath additions and unexpanded files now empty.
            if (AS400ToolboxInstaller.getClasspathAdditions().size() != 0)
            {
                failed("Classpath additions not empty.");
                output_.println(AS400ToolboxInstaller.getClasspathAdditions());
            }
            else if (AS400ToolboxInstaller.getUnexpandedFiles().size() != 0)
            {
                failed("Unexpanded files not empty.");
                output_.println(AS400ToolboxInstaller.getUnexpandedFiles());
            }
            else
                succeeded();
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

/**
After install, verify
classpath vectors when classpath does not need to be modified because
the classpath is already correct.
Requires the CLASSPATH be set up correctly.
**/
public void Var005()
{
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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NY   TEST3.ZIP\n" +
                         "ACCESS   PADD NY   \n" +
                         "ACCESS   RMV  NY   TEST6.JAR\n" +
                         "ACCESS   PRMV NY   test2\n" +
                         "VACCESS  ADD  NY   TEST1.ZIP\n" +
                         "VACCESS  PADD NY   test1\n" +
                         "VACCESS  RMV  NY   TEST2.ZIP\n" +
                         "VACCESS  PRMV NY   test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test1"))
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test1");
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 2)
                failed = true;
            else if (!results.contains(target + "test2"))
                failed = true;
            else if (!results.contains(target + "TEST6.JAR"))
                failed = true;
            else
                succeeded();
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(target + "test2");
                expected.addElement(target + "TEST6.JAR");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
}

/**
After uninstall, verify
classpath removals do not contain items not in classpath.
Requires the CLASSPATH be set up correctly.
**/
public void Var006()
{
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


    String target2 = InstallTest.targetPath +
                     "install" + File.separator +
                     "test2" + File.separator;
    try
    {
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS\n");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST file.
        InstallTest.writeFile(target + "ACCESS.LST",
           "TEST1.ZIP\nTEST2.TXT\n");
        // Create product files.
        InstallTest.writeFile(target + "TEST1.ZIP",
                              "yippee\n");
        InstallTest.writeFile(target + "TEST2.TXT",
                              "wow\nwee\n");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals does not contain items not in CP.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 1)
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(targetNoSep);
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

  /**
  After install, verify
  classpath vectors when classpath cannot be accessed.
  **/
  public void Var007()
  {
    if (!isApplet_)
    {
      notApplicable("Not running in as an applet.");
      return;
    }

    try
    {
      // try to access CLASSPATH to see if this test can be run
      if (System.getProperty("java.class.path") != null)
      {
        failed("Cannot run - CLASSPATH can be accessed.");
        return;
      }
    }
    catch (Exception e) {}

    try
    {
        // Setup source to install from.
        // Create PKG file.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG",
                              "ACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NY   TEST1.ZIP\n" +
                         "ACCESS   RMV  NY   TEST2.ZIP\n" +
                         "ACCESS   PADD NY   test1\n" +
                         "ACCESS   PRMV NY   test2\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test1"))
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test1");
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 2)
                failed = true;
            else if (!results.contains(target + "test2"))
                failed = true;
            else if (!results.contains(target + "TEST2.ZIP"))
                failed = true;
            else
                succeeded();
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(target + "test2");
                expected.addElement(target + "TEST2.ZIP");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
      // Remove change list file from source
      File file = new File(InstallTest.targetPath + "V1R1M1.LST");
      file.delete();
      try
      {
        // Remove remaining files in source.
        AS400ToolboxInstaller.unInstall("*ALL",
                                        InstallTest.targetPath);
        // Remove files in target and target directory.
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);
      }
      catch (IOException e) {}
    }
  }


/**
After uninstall, verify
classpath vectors when classpath cannot be accessed.
**/
public void Var008()
{
    if (!isApplet_)
    {
      notApplicable("Not running in as an applet.");
      return;
    }

    try
    {
      // try to access CLASSPATH to see if this test can be run
      if (System.getProperty("java.class.path") != null)
      {
        failed("Cannot run - CLASSPATH can be accessed.");
        return;
      }
    }
    catch (Exception e) {}

    try
    {
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS\n");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST file.
        InstallTest.writeFile(target + "ACCESS.LST",
           "TEST1.ZIP\\n");
        // Create product files.
        InstallTest.writeFile(target + "TEST1.ZIP",
                              "yippee\n");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals does not have any entries.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath removals not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(targetNoSep);
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

/**
After install, verify
vectors do not contain duplicates.
Requires the CLASSPATH be set up correctly.
**/
public void Var009()
{
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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   TEST3.ZIP\n" +
                         "ACCESS   PADD YY   \n" +
                         "ACCESS   RMV  YY   TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test2\n" +
                         "ACCESS   ADD  YY   TEST3.ZIP\n" +
                         "ACCESS   PADD YY   \n" +
                         "ACCESS   RMV  YY   TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test2\n" +
                         "VACCESS  ADD  YY   TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test1\n" +
                         "VACCESS  RMV  YY   TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test3\n" +
                         "VACCESS  ADD  YY   TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test1\n" +
                         "VACCESS  RMV  YY   TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test1"))
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test1");
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 2)
                failed = true;
            else if (!results.contains(target + "test2"))
                failed = true;
            else if (!results.contains(target + "TEST6.JAR"))
                failed = true;
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(target + "test2");
                expected.addElement(target + "TEST6.JAR");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
            else {
                results = AS400ToolboxInstaller.getUnexpandedFiles();
                if (results.size() != 2)
                    failed = true;
                else if (!results.contains(target + "TEST3.ZIP"))
                    failed = true;
                else if (!results.contains(target + "TEST1.ZIP"))
                    failed = true;
                else
                    succeeded();
                if (failed)
                {
                    failed("Unexpanded files not correct.");
                    output_.println("Expected:");
                    Vector expected = new Vector();
                    expected.addElement(target + "TEST3.ZIP");
                    expected.addElement(target + "TEST1.ZIP");
                    output_.println(expected);
                    output_.println("Received:");
                    output_.println(results);
                }
            }
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
}

/**
After uninstall of single package, verify
classpath removals does not contain duplicates.
Requires the CLASSPATH be set up correctly.
**/
public void Var010()
{
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
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1");
        // Create package LST file.
        // Put in duplicate file to make sure it is not added twice.
        InstallTest.writeFile(target + "ACCESS.LST",
           "TEST3.ZIP\nTEST3.ZIP\nTEST2.TXT");
        // Create product files.
        InstallTest.writeFile(target + "TEST3.ZIP",
                              "yippee");
        InstallTest.writeFile(target + "TEST2.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("ACCESS",
                                        target);

        // Verify classpath removals does not have duplicates.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else if (!results.contains(target + "TEST3.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(targetNoSep);
            expected.addElement(target + "TEST3.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove package list file and target directory
        File file = new File(target + "JT400.PKG");
        file.delete();
        file = new File(targetNoSep);
        file.delete();
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

/**
After uninstall of *ALL, verify
classpath removals does not contain duplicates.
Requires the CLASSPATH be set up correctly.
**/
public void Var011()
{
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
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(target + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(target + "ACCESS.LST",
           "TEST3.ZIP\nTEST2.TXT\n");
        InstallTest.writeFile(target + "VACCESS.LST",
           "TEST3.ZIP\nTEST2.TXT\n");
        // Create product files.
        InstallTest.writeFile(target + "TEST3.ZIP",
                              "yippee");
        InstallTest.writeFile(target + "TEST2.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals does not have duplicates.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else if (!results.contains(target + "TEST3.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(targetNoSep);
            expected.addElement(target + "TEST3.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

/**
After uninstall, verify
classpath removals contains subdirectories if they are not in the CLASSPATH.
Requires the CLASSPATH be set up correctly.
**/
public void Var012()
{
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
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1");
        // Create package LST file.
        // Put in files which are in subdirectories.
        InstallTest.writeFile(target + "ACCESS.LST",
           "test1/TEST1.TXT\ntest2/TEST2.TXT");
        // Create product files.
        InstallTest.writeFile(target + "test1" + File.separator + "TEST1.TXT",
                              "yippee");
        InstallTest.writeFile(target + "test2" + File.separator + "TEST2.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals has subdirectories.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test2"))
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else
            succeeded();
        if (failed)
        {
            failed("Classpath removals not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test2");
            expected.addElement(targetNoSep);
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

/**
After install using *ALL, verify
Vectors are cumulative.
Requires the CLASSPATH be set up correctly.
**/
public void Var013()
{
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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   TEST3.ZIP\n" +
                         "ACCESS   PADD YY   \n" +
                         "ACCESS   RMV  YY   TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test2\n" +
                         "VACCESS  ADD  YY   TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test1\n" +
                         "VACCESS  RMV  YY   TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test1"))
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test1");
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 2)
                failed = true;
            else if (!results.contains(target + "test2"))
                failed = true;
            else if (!results.contains(target + "TEST6.JAR"))
                failed = true;
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(target + "test2");
                expected.addElement(target + "TEST6.JAR");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
            else {
                results = AS400ToolboxInstaller.getUnexpandedFiles();
                if (results.size() != 2)
                    failed = true;
                else if (!results.contains(target + "TEST3.ZIP"))
                    failed = true;
                else if (!results.contains(target + "TEST1.ZIP"))
                    failed = true;
                else
                    succeeded();
                if (failed)
                {
                    failed("Unexpanded files not correct.");
                    output_.println("Expected:");
                    Vector expected = new Vector();
                    expected.addElement(target + "TEST3.ZIP");
                    expected.addElement(target + "TEST1.ZIP");
                    output_.println(expected);
                    output_.println("Received:");
                    output_.println(results);
                }
            }
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
}

/**
After uninstall using *ALL, verify
Vectors are cumulative.
Requires the CLASSPATH be set up correctly.
**/
public void Var014()
{
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
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(target + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(target + "ACCESS.LST",
           "TEST3.ZIP\ntest2/TEST.TXT\n");
        InstallTest.writeFile(target + "VACCESS.LST",
           "TEST6.JAR\ntest4/TEST.TXT\n");
        // Create product files.
        InstallTest.writeFile(target + "TEST3.ZIP",
                              "yippee");
        InstallTest.writeFile(target + "TEST6.JAR",
                              "yippee");
        InstallTest.writeFile(target + "test2" + File.separator + "TEST.TXT",
                              "wow\nwee");
        InstallTest.writeFile(target + "test4" + File.separator + "TEST.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals is aggregate of packages.
        // Vector should have a five paths.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 5)
            failed = true;
        else if (!results.contains(target + "TEST3.ZIP"))
            failed = true;
        else if (!results.contains(target + "test2"))
            failed = true;
        else if (!results.contains(target + "TEST6.JAR"))
            failed = true;
        else if (!results.contains(target + "test4"))
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else
            succeeded();
        if (failed)
        {
            failed("Classpath removals not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "TEST1.ZIP");
            expected.addElement(target + "test1");
            expected.addElement(target + "TEST2.ZIP");
            expected.addElement(target + "test2");
            expected.addElement(targetNoSep);
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

/**
After install using a relative target path, verify paths
are absolute.
Requires the CLASSPATH be set up correctly.
**/
public void Var015()
{
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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   TEST3.ZIP\n" +
                         "ACCESS   PADD YY   \n" +
                         "ACCESS   RMV  YY   TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test2\n" +
                         "VACCESS  ADD  YY   TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test1\n" +
                         "VACCESS  RMV  YY   TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      "install" + File.separator + "test",
                                      InstallTest.localURL);

        // Verify attribute vectors.
        String targetDir = (new File("install" + File.separator + "test" + File.separator)).getAbsolutePath();

        if (targetDir.endsWith(File.separator))
        {}
        else
        targetDir = targetDir + File.separator;

        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(targetDir + "test1"))
            failed = true;
        else if (!results.contains(targetDir + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(targetDir + "test1");
            expected.addElement(targetDir + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 2)
                failed = true;
            else if (!results.contains(targetDir + "test2"))
                failed = true;
            else if (!results.contains(targetDir + "TEST6.JAR"))
                failed = true;
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(targetDir + "test2");
                expected.addElement(targetDir + "TEST6.JAR");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
            else {
                results = AS400ToolboxInstaller.getUnexpandedFiles();
                if (results.size() != 2)
                    failed = true;
                else if (!results.contains(targetDir + "TEST3.ZIP"))
                    failed = true;
                else if (!results.contains(targetDir + "TEST1.ZIP"))
                    failed = true;
                else
                    succeeded();
                if (failed)
                {
                    failed("Unexpanded files not correct.");
                    output_.println("Expected:");
                    Vector expected = new Vector();
                    expected.addElement(targetDir + "TEST3.ZIP");
                    expected.addElement(targetDir + "TEST1.ZIP");
                    output_.println(expected);
                    output_.println("Received:");
                    output_.println(results);
                }
            }
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            "install" + File.separator + "test");
        }
        catch (IOException e) {}
    }
}

/**
After uninstall using a relative target path, verify paths
are absolute.
Requires the CLASSPATH be set up correctly.
**/
public void Var016()
{
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
        // Setup target to uninstall from.
        String targetDir = "install" + File.separator + "test" + File.separator;

        // Create PKG file.
        InstallTest.writeFile(targetDir + "JT400.PKG",
                              "ACCESS");
        // Create LVL file.
        InstallTest.writeFile(targetDir + "ACCESS.LVL",
                              "V1R1M1");
        // Create package LST file.
        InstallTest.writeFile(targetDir + "ACCESS.LST",
           "TEST3.ZIP\nTEST2.TXT");
        // Create product files.
        InstallTest.writeFile(targetDir + "TEST3.ZIP",
                              "yippee");
        InstallTest.writeFile(targetDir + "TEST2.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        targetDir);

        // Verify classpath removals are absolute
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;


        String qualifiedDir = (new File(targetDir)).getAbsolutePath();
        if (qualifiedDir.endsWith(File.separator))
        {}
        else
        qualifiedDir = qualifiedDir + File.separator;

        String qualifiedDirNoSep = (new File("install" + File.separator + "test")).getAbsolutePath();



        if (results.size() != 2)
            failed = true;
        else if (!results.contains(qualifiedDirNoSep))
            failed = true;
        else if (!results.contains(qualifiedDir + "TEST3.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(qualifiedDirNoSep);
            expected.addElement(qualifiedDir + "TEST3.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
}

  /**
  After install verify paths
  on a system with forward slashes as the path separator.
  Requires the CLASSPATH be set up correctly.
  **/
  public void Var017()
  {
    if (!File.separator.equals("/"))
    {
      notApplicable("File separator is not forward-slash.");
      return;
    }

    // Make sure classpath set as required by this test.
    try
    {
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
                            "ACCESS\nVACCESS\n");
      // Create LVL files.
      InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
      InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
      // Create package LST files.
      InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "test/TEST3.ZIP\n");
      InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "test/TEST1.ZIP\n");
      // Create product files.
      String sourceDir = InstallTest.targetPath + "test" + File.separator;
      InstallTest.writeFile(sourceDir + "TEST3.ZIP",
                              "yippee\n");
      InstallTest.writeFile(sourceDir + "TEST1.ZIP",
                              "yippee\n");
      // Create change LST file.
      InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   test/TEST3.ZIP\n" +
                         "ACCESS   PADD YY   test/test4\n" +
                         "ACCESS   RMV  YY   test/TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test/test2\n" +
                         "VACCESS  ADD  YY   test/TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test/test1\n" +
                         "VACCESS  RMV  YY   test/TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test/test3\n" );

      // Do the install.
      AS400ToolboxInstaller.install("*ALL",
                                      InstallTest.targetPath + "install",
                                      InstallTest.localURL);

      // Verify attribute vectors.
      Vector results = AS400ToolboxInstaller.getClasspathAdditions();
      boolean failed = false;
      if (results.size() != 2)
        failed = true;
      else if (!results.contains(target + "test1"))
        failed = true;
      else if (!results.contains(target + "TEST1.ZIP"))
        failed = true;
      if (failed)
      {
        failed("Classpath additions not correct.");
        output_.println("Expected:");
        Vector expected = new Vector();
        expected.addElement(target + "test1");
        expected.addElement(target + "TEST1.ZIP");
        output_.println(expected);
        output_.println("Received:");
        output_.println(results);
      }
      else
      {
        results = AS400ToolboxInstaller.getClasspathRemovals();
        if (results.size() != 2)
          failed = true;
        else if (!results.contains(target + "test2"))
          failed = true;
        else if (!results.contains(target + "TEST6.JAR"))
          failed = true;
        if (failed)
        {
          failed("Classpath removals not correct.");
          output_.println("Expected:");
          Vector expected = new Vector();
          expected.addElement(target + "test2");
          expected.addElement(target + "TEST6.JAR");
          output_.println(expected);
          output_.println("Received:");
          output_.println(results);
        }
        else
        {
          results = AS400ToolboxInstaller.getUnexpandedFiles();
          if (results.size() != 2)
            failed = true;
          else if (!results.contains(target + "TEST3.ZIP"))
            failed = true;
          else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
          else
            succeeded();
          if (failed)
          {
            failed("Unexpanded files not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "TEST3.ZIP");
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
          }
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
      // Remove change list file from source
      File file = new File(InstallTest.targetPath + "V1R1M1.LST");
      file.delete();
      try
      {
        // Remove remaining files in source.
        AS400ToolboxInstaller.unInstall("*ALL",
                                        InstallTest.targetPath);
        // Remove files in target and target directory.
        AS400ToolboxInstaller.unInstall("*ALL",
                                        InstallTest.targetPath + "install");
      }
      catch (IOException e) {}
    }
  }


  /**
  After install verify paths
  on a system with backward slashes as the path separator.
  Requires the CLASSPATH be set up correctly.
  **/
  public void Var018()
  {
    if (!File.separator.equals("\\"))
    {
      notApplicable("File separator is not back-slash.");
      return;
    }

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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "test/TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "test/TEST1.ZIP\n");
        // Create product files.
        String sourceDir = InstallTest.targetPath + "test" + File.separator;
        InstallTest.writeFile(sourceDir + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(sourceDir + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   test/TEST3.ZIP\n" +
                         "ACCESS   PADD YY   test/test4\n" +
                         "ACCESS   RMV  YY   test/TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test/test2\n" +
                         "VACCESS  ADD  YY   test/TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test/test1\n" +
                         "VACCESS  RMV  YY   test/TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test/test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      InstallTest.targetPath + "install",
                                      InstallTest.localURL);

        // Verify attribute vectors.
        Vector results = AS400ToolboxInstaller.getClasspathAdditions();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test1"))
            failed = true;
        else if (!results.contains(target + "TEST1.ZIP"))
            failed = true;
        if (failed)
        {
            failed("Classpath additions not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test1");
            expected.addElement(target + "TEST1.ZIP");
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
        else {
            results = AS400ToolboxInstaller.getClasspathRemovals();
            if (results.size() != 2)
                failed = true;
            else if (!results.contains(target + "test2"))
                failed = true;
            else if (!results.contains(target + "TEST6.JAR"))
                failed = true;
            if (failed)
            {
                failed("Classpath removals not correct.");
                output_.println("Expected:");
                Vector expected = new Vector();
                expected.addElement(target + "test2");
                expected.addElement(target + "TEST6.JAR");
                output_.println(expected);
                output_.println("Received:");
                output_.println(results);
            }
            else {
                results = AS400ToolboxInstaller.getUnexpandedFiles();
                if (results.size() != 2)
                    failed = true;
                else if (!results.contains(target + "TEST3.ZIP"))
                    failed = true;
                else if (!results.contains(target + "TEST1.ZIP"))
                    failed = true;
                else
                    succeeded();
                if (failed)
                {
                    failed("Unexpanded files not correct.");
                    output_.println("Expected:");
                    Vector expected = new Vector();
                    expected.addElement(target + "TEST3.ZIP");
                    expected.addElement(target + "TEST1.ZIP");
                    output_.println(expected);
                    output_.println("Received:");
                    output_.println(results);
                }
            }
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath + "install");
        }
        catch (IOException e) {}
    }
  }

  /**
  After uninstall verify paths
  on a system with forward slashes as the path separator.
  Requires the CLASSPATH be set up correctly.
  **/
  public void Var019()
  {
    if (!File.separator.equals("/"))
    {
      notApplicable("File separator is not forward-slash.");
      return;
    }

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
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1");
        // Create package LST file.
        // Put in files which are in subdirectories.
        InstallTest.writeFile(target + "ACCESS.LST",
           "test1/TEST1.TXT\ntest2/TEST2.TXT");
        // Create product files.
        InstallTest.writeFile(target + "test1" + File.separator + "TEST1.TXT",
                              "yippee");
        InstallTest.writeFile(target + "test2" + File.separator + "TEST2.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals has subdirectories.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test2"))
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else
            succeeded();
        if (failed)
        {
            failed("Classpath removals not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test2");
            expected.addElement(targetNoSep);
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
  }


  /**
  After uninstall verify paths
  on a system with backward slashes as the path separator.
  Requires the CLASSPATH be set up correctly.
  **/
  public void Var020()
  {
    if (!File.separator.equals("\\"))
    {
      notApplicable("File separator is not back-slash.");
      return;
    }

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
        // Setup target to uninstall from.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG",
                              "ACCESS");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL",
                              "V1R1M1");
        // Create package LST file.
        // Put in files which are in subdirectories.
        InstallTest.writeFile(target + "ACCESS.LST",
           "test1/TEST1.TXT\ntest2/TEST2.TXT");
        // Create product files.
        InstallTest.writeFile(target + "test1" + File.separator + "TEST1.TXT",
                              "yippee");
        InstallTest.writeFile(target + "test2" + File.separator + "TEST2.TXT",
                              "wow\nwee");

        // Do uninstall
        AS400ToolboxInstaller.unInstall("*ALL",
                                        target);

        // Verify classpath removals has subdirectories.
        Vector results = AS400ToolboxInstaller.getClasspathRemovals();
        boolean failed = false;
        if (results.size() != 2)
            failed = true;
        else if (!results.contains(target + "test2"))
            failed = true;
        else if (!results.contains(targetNoSep))
            failed = true;
        else
            succeeded();
        if (failed)
        {
            failed("Classpath removals not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "test2");
            expected.addElement(targetNoSep);
            output_.println(expected);
            output_.println("Received:");
            output_.println(results);
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        try
        {
            // Remove files in target and target directory if
            // setup failed so uninstall not already done.
            if (AS400ToolboxInstaller.isInstalled("ACCESS", target))
            {
                AS400ToolboxInstaller.unInstall("*ALL",
                                                target);
            }
        }
        catch (IOException e) {}
    }
  }


  /**
  After isUpdateNeeded (should not affect).
  Requires the CLASSPATH be set up correctly.
  **/
  public void Var021()
  {
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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   TEST3.ZIP\n" +
                         "ACCESS   PADD YY   \n" +
                         "ACCESS   RMV  YY   TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test2\n" +
                         "VACCESS  ADD  YY   TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test1\n" +
                         "VACCESS  RMV  YY   TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors are now not empty.
        Vector cpa = AS400ToolboxInstaller.getClasspathAdditions();
        Vector cpr = AS400ToolboxInstaller.getClasspathRemovals();
        Vector uef = AS400ToolboxInstaller.getUnexpandedFiles();
        if (cpa.size() == 0 || cpr.size() == 0 || uef.size() == 0)
            failed("Setup failed, vectors not exciting.");
        else
        {
            AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 target,
                                                 InstallTest.localURL);
            if (cpa != AS400ToolboxInstaller.getClasspathAdditions())
                failed("Classpath additions changed.");
            else if (cpr != AS400ToolboxInstaller.getClasspathRemovals())
                failed("Classpath removals changed.");
            else if (uef != AS400ToolboxInstaller.getUnexpandedFiles())
                failed("Unexpanded files changed.");
            else
                succeeded();
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
}

/**
After isInstalled (should not affect).
Requires the CLASSPATH be set up correctly.
**/
public void Var022()
{
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
                              "ACCESS\nVACCESS\n");
        // Create LVL files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL",
                              "V1R1M1\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL",
                              "V1R1M1\n");
        // Create package LST files.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
                             "TEST3.ZIP\n");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LST",
                             "TEST1.ZIP\n");
        // Create product files.
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.ZIP",
                              "yippee\n");
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.ZIP",
                              "yippee\n");
        // Create change LST file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  YY   TEST3.ZIP\n" +
                         "ACCESS   PADD YY   \n" +
                         "ACCESS   RMV  YY   TEST6.JAR\n" +
                         "ACCESS   PRMV YY   test2\n" +
                         "VACCESS  ADD  YY   TEST1.ZIP\n" +
                         "VACCESS  PADD YY   test1\n" +
                         "VACCESS  RMV  YY   TEST2.ZIP\n" +
                         "VACCESS  PRMV YY   test3\n" );

        // Do the install.
        AS400ToolboxInstaller.install("*ALL",
                                      target,
                                      InstallTest.localURL);

        // Verify attribute vectors are now not empty.
        Vector cpa = AS400ToolboxInstaller.getClasspathAdditions();
        Vector cpr = AS400ToolboxInstaller.getClasspathRemovals();
        Vector uef = AS400ToolboxInstaller.getUnexpandedFiles();
        if (cpa.size() == 0 || cpr.size() == 0 || uef.size() == 0)
            failed("Setup failed, vectors not exciting.");
        else
        {
            AS400ToolboxInstaller.isInstalled("ACCESS",
                                              target);
            if (cpa != AS400ToolboxInstaller.getClasspathAdditions())
                failed("Classpath additions changed.");
            else if (cpr != AS400ToolboxInstaller.getClasspathRemovals())
                failed("Classpath removals changed.");
            else if (uef != AS400ToolboxInstaller.getUnexpandedFiles())
                failed("Unexpanded files changed.");
            else
                succeeded();
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally   // Cleanup.
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
    }
  }

}



