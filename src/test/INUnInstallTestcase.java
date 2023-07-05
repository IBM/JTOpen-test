///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INUnInstallTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import utilities.AS400ToolboxInstaller;
import java.io.RandomAccessFile;

/**
Testcase INUnInstallTestcase.
Tests the following methods:
<ul>
<li>AS400ToolboxInstaller.uninstall
</ul>
**/
public class INUnInstallTestcase extends Testcase
{

    String target = InstallTest.targetPath + "test" + File.separator;
    String targetNoSep = InstallTest.targetPath + "test";

/**
Constructor.  This is called from InstallTest::createTestcases().
**/
public INUnInstallTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
{
    super(systemObject, "INUnInstallTestcase", 24,
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
    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }
    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }

}

/**
Creates test product and control files on the local system
in <i>targetDir</i>.

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
void createFiles(String targetDir)
    throws IOException
{
    // Create PKG file.
    InstallTest.writeFile(targetDir + "JT400.PKG", "ACCESS");
    // Create LVL file.
    InstallTest.writeFile(targetDir + "ACCESS.LVL", "V1R1M1");
    // Create package LST file.
    InstallTest.writeFile(targetDir + "ACCESS.LST", "TEST1.TXT\nTEST2.TXT");
    // Create product files.
    InstallTest.writeFile(targetDir + "TEST1.TXT", "yippee");
    InstallTest.writeFile(targetDir + "TEST2.TXT", "wow\nwee");
}

/**
Verifies test product and control files created by createFiles
have been removed.
JT400.PKG is not checked for.
failed() will be called if any errors are found.

@parm targetDir Directory in which to look for the files.

@return true if no files were found, false if files existed.

@exception IOException if any errors occur
**/
boolean verifyUnInstall(String targetDir)
//    throws IOException
{
    // Check for LVL file.
    if ((new File(targetDir + "ACCESS.LVL")).exists())
        failed("ACCESS.LVL exists in " + targetDir);
    // Check for package LST file.
    if ((new File(targetDir + "ACCESS.LST")).exists())
        failed("ACCESS.LST exists in " + targetDir);
    // Check for product files.
    if ((new File(targetDir + "TEST1.TXT")).exists())
        failed("TEST1.TXT exists in " + targetDir);
    if ((new File(targetDir + "TEST2.TXT")).exists())
        failed("TEST2.TXT exists in " + targetDir);
    else
        return true;
    return false;
}

/**
Create product with nested directories

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
private void createNestedFiles(String targetDir)
    throws IOException
{
    // Create PKG file.
    InstallTest.writeFile(targetDir + "JT400.PKG", "ACCESS");
    // Create LVL file.
    InstallTest.writeFile(targetDir + "ACCESS.LVL", "V1R1M1");
    // Create package LST file.
    InstallTest.writeFile(targetDir + "ACCESS.LST",
              "install/test/TEST1.TXT\ninstall/test2/TEST2.TXT");
    // Create product files.
    String file1 = targetDir + "install" + File.separator +
                   "test" + File.separator + "TEST1.TXT";
    String file2 = targetDir + "install" + File.separator +
                   "test2" + File.separator + "TEST2.TXT";
    InstallTest.writeFile(file1, "yippee");
    InstallTest.writeFile(file2, "wow\nwee");
}

/**
Verify files and directories created by createNestedFiles were deleted.
**/
private boolean verifyNestedUnInstall(String targetDir)
{
    String file1 = targetDir + "install" + File.separator +
                   "test" + File.separator + "TEST1.TXT";
    String file2 = targetDir + "install" + File.separator +
                   "test2" + File.separator + "TEST2.TXT";
    // Verify LVL file deleted
    if ((new File(targetDir + "ACCESS.LVL")).exists())
    {
        failed("ACCESS.LVL file not deleted");
        (new File(targetDir + "ACCESS.LVL")).delete();
    }
    // Verify package LST file deleted
    else if ((new File(targetDir + "ACCESS.LST")).exists())
    {
        failed("ACCESS.LST file not deleted");
        (new File(targetDir + "ACCESS.LST")).delete();
    }
    // Verify product files deleted
    else if ((new File(file1)).exists())
    {
        failed("TEST1.TXT file not deleted");
        (new File(file1)).delete();
    }
    else if ((new File(file2)).exists())
    {
        failed("TEST2.TXT file not deleted");
        (new File(file2)).delete();
    }
    // Verify all directories deleted
    else if ((new File(targetDir + "install" + File.separator + "test")).exists())
    {
        failed(target + "install" + File.separator + "test directory not deleted");
        (new File(target + "install" + File.separator + "test")).delete();
    }
    else if ((new File(targetDir + "install" + File.separator + "test2")).exists())
    {
        failed(targetDir + "install" + File.separator + "test2 directory not deleted");
        (new File(targetDir + "install" + File.separator + "test2")).delete();
    }
    else if ((new File(target + "install")).exists())
    {
        failed(targetDir + "install directory not deleted");
        (new File(targetDir + "install")).delete();
    }
    else
        return true;
    return false;
}


/**
Null packageName parm.
**/
public void Var001()
{
    try
    {
        AS400ToolboxInstaller.unInstall(null, "");
        failed("No exception.");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "NullPointerException", "packageName"))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
Null targetPath parm.
**/
public void Var002()
{
    try
    {
        AS400ToolboxInstaller.unInstall("", null);
        failed("No exception.");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "NullPointerException", "targetPath"))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
Use a target path that is a subdirectory of the current directory,
not fully qualifying the path.
**/
public void Var003()
{
    try
    {
        createFiles("test" + File.separator);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      "test");
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall("test" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File("test" + File.separator + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File("test");
        file.delete();
    }
}

/**
Use a relative target path using "..".
**/
public void Var004()
{
    try
    {
        createFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
               target + File.separator + ".." + File.separator + "test");
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
}

/**
Use a relative target path using ".".
**/
public void Var005()
{
    try
    {
        createFiles("test" + File.separator);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                   "." + File.separator + "test");
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall("test" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File("test" + File.separator + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File("test");
        file.delete();
    }
}

/**
Use a target path starting at the root.
**/
public void Var006()
{
    if (InstallTest.runningAIX_) {
      notApplicable("Running on AIX");
      return;
    }
    try
    {
        createFiles(File.separator + "test" + File.separator);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                    File.separator + "test");
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(File.separator + "test" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(File.separator + "test" + File.separator + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(File.separator + "test");
        file.delete();
    }
}

/**
Use a target path of the root.
**/
public void Var007()
{
    if (InstallTest.runningAIX_) {
      notApplicable("Running on AIX");
      return;
    }
    try
    {
        createFiles(File.separator);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                File.separator);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(File.separator + "JT400.PKG");
        file.delete();
    }
}

/**
Use a blank target path (indicating to uninstall from current directory).
**/
public void Var008()
{
    try
    {
        createFiles("." + File.separator);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      "");
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall("." + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File("JT400.PKG");
        file.delete();
    }
}

/**
Use a target path of '.' (indicating to uninstall from current directory).
**/
public void Var009()
{
    try
    {
        createFiles("." + File.separator);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      ".");
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall("." + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File("JT400.PKG");
        file.delete();
    }
}

/**
Do not include trailing delimiter on target path.
**/
public void Var010()
{
    String targetStr = InstallTest.targetPath + "test";
    try
    {
        createFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      targetStr);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
}

/**
Include trailing delimiter on target path.
**/
public void Var011()
{
    try
    {
        createFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
}

/**
Use a target directory that does not exist, should get error
that not installed.
**/
public void Var012()
{
    File dir = new File(targetNoSep);
    if (dir.exists() && !dir.delete())
          failed("Setup failed, directory exists.");
    else
    {
      try
      {
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        failed("No exception");
      }
      catch (Exception e)
      {
        if (exceptionIs(e, "IOException", "(ACCESS) Package is not installed."))
            succeeded();
        else
            failed(e, "Wrong exception info");
      }
    }
}

/**
Use *ALL for the package, verify all packages removed, as well as the
JT400.PKG file.
**/
public void Var013()
{
    try
    {
        createFiles(target);
        // add another package to PKG file
        InstallTest.writeFile(target + "JT400.PKG", "ACCESS\nVACCESS");
        // Create LVL file.
        InstallTest.writeFile(target + "VACCESS.LVL", "V1R1M1");
        // Create package LST file.
        InstallTest.writeFile(target + "VACCESS.LST", "VTEST1.TXT\nVTEST2.TXT");
        // Create product files.
        InstallTest.writeFile(target + "VTEST1.TXT", "yippee");
        InstallTest.writeFile(target + "VTEST2.TXT", "wow\nwee");

        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        // Verify JT400.PKG file deleted
        else if ((new File(target + "JT400.PKG")).exists())
        {
            failed("JT400.PKG file not deleted");
            (new File(target + "JT400.PKG")).delete();
        }
        // Verify second package files deleted
        else if ((new File(target + "VACCESS.LVL")).exists())
        {
            failed("VACCESS.LVL file not deleted");
            (new File(target + "VACCESS.LVL")).delete();
        }
        else if ((new File(target + "VACCESS.LST")).exists())
        {
            failed("VACCESS.LST file not deleted");
            (new File(target + "VACCESS.LST")).delete();
        }
        else if ((new File(target + "VTEST1.TXT")).exists())
        {
            failed("VTEST1.TXT file not deleted");
            (new File(target + "VTEST1.TXT")).delete();
        }
        else if ((new File(target + "VTEST2.TXT")).exists())
        {
            failed("VTEST2.TXT file not deleted");
            (new File(target + "VTEST2.TXT")).delete();
        }
        // Verify first package files deleted
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove directory
        File file = new File(targetNoSep);
        file.delete();
    }
}

/**
Use *ALL for the package, verify no failure if not all packages installed.
**/
public void Var014()
{
    try
    {
        createFiles(target);
        // add another package to PKG file
        InstallTest.writeFile(target + "JT400.PKG", "ACCESS\nVACCESS");
        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        // Verify JT400.PKG file deleted
        else if ((new File(target + "JT400.PKG")).exists())
        {
            failed("JT400.PKG file not deleted");
            (new File(target + "JT400.PKG")).delete();
        }
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove directory
        File file = new File(targetNoSep);
        file.delete();
    }
}

/**
UnInstall when not installed.
**/
public void Var015()
{
    try
    {
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        failed("No exception");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "IOException", "(ACCESS) Package is not installed."))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
UnInstall using *ALL when no packages installed, and JT400.PKG exists.
**/
public void Var016()
{
    try
    {
        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        failed("No exception");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "IOException", "No packages were installed."))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
UnInstall using *ALL when no packages installed, and JT400.PKG does not exist.
**/
public void Var017()
{
    try
    {
        // write PKG file
        InstallTest.writeFile(target + "JT400.PKG", "ACCESS\nVACCESS");
        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        failed("No exception");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "IOException", "No packages were installed."))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
    finally
    {
        // Remove package file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
}

/**
UnInstall when installed.
**/
public void Var018()
{
    try
    {
        createFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
}

/**
Uninstall doesn't fail if files missing.
**/
public void Var019()
{
    try
    {
        createFiles(target);
        // remove some files
        (new File(target + "TEST1.TXT")).delete();
        (new File(target + "ACCESS.LVL")).delete();
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
}

/**
Verify errors when uninstall cannot delete files.  The package list file
should not be deleted when errors are encountered.
**/
public void Var020()
{
    File open1 = null;
    File open2 = null;
    RandomAccessFile raf1 = null;
    RandomAccessFile raf2 = null;

    if (InstallTest.runningUNIX_)
    {
      notApplicable("Haven't figured out how to make files undeletable on Sun, AIX or Linux.");
      return;
    }

    try
    {
        createFiles(target);

        // Make some files un-deletable.
        // StringBuffer text = new StringBuffer(
        // "Make the following files un-deletable:\n");
        // text.append(target + "TEST1.TXT\n");
        // text.append(target + "ACCESS.LVL\n");
        // Can use ourgang/w32Users/daw/dostools/fstool to open files.
        // At prompt, type "open 1 test1.txt" then "open 2 access.lvl".
        // TestInstructions msg = new TestInstructions(text.toString());
        // msg.display();

        if (InstallTest.runningUNIX_)
        {  // Note: This doesn't seem to make the files undeletable.  -JPL 3/98
          try
          {
            String cmd;
            Runtime runtime = Runtime.getRuntime();
            cmd = "chmod 444 " + target + "TEST1.TXT";
            runtime.exec(cmd);
            cmd = "chmod 444 " + target + "ACCESS.LVL";
            runtime.exec(cmd);
          }
          catch(IOException e)
          {
            failed(e, "Failed to make files un-deletable");
            return;
          }
        }
        else
        {
          // This block added to make files un-deletable by keeping an
          // open stream to them. - csmith
          open1 = new File(target + "TEST1.TXT");
          open2 = new File(target + "ACCESS.LVL");
          raf1 = new RandomAccessFile(open1, "rw");
          raf2 = new RandomAccessFile(open2, "rw");
        }

        // Make the directory un-deletable by adding another file.
        InstallTest.writeFile(target + "junk.txt", "filler");

        // Files that cannot be deleted should result
        // in error.  Directories should not cause errors.
        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        boolean failed = false;
        if (errs.size() != 2)
            failed = true;
        else if (!errs.contains(target + "TEST1.TXT"))
            failed = true;
        else if (!errs.contains(target + "ACCESS.LVL"))
            failed = true;
        if (failed)
        {
            failed("Errors not correct.");
            output_.println("Expected:");
            Vector expected = new Vector();
            expected.addElement(target + "TEST1.TXT");
            expected.addElement(target + "ACCESS.LVL");
            output_.println(expected);
            output_.println("Received:");
            output_.println(errs);
        }
        else
        {
            // Make sure the package list file was not deleted.
            File file = new File(target + "ACCESS.LST");
            if (file.exists())
                succeeded();
            else
                failed("Package file list deleted.");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Allow write again to allow cleanup.
        // StringBuffer text = new StringBuffer(
        // "Make the following files deletable:\n");
        // text.append(target + "TEST1.TXT\n");
        // text.append(target + "ACCESS.LVL\n");
        // (exit out of fstool)
        // TestInstructions msg = new TestInstructions(text.toString());
        // msg.display();

      // Make files deletable again.
      if (InstallTest.runningUNIX_)
      {
        try
        {
          String cmd;
          Runtime runtime = Runtime.getRuntime();
          cmd = "chmod 777 " + target + "TEST1.TXT";
          runtime.exec(cmd);
          cmd = "chmod 777 " + target + "ACCESS.LVL";
          runtime.exec(cmd);
        }
        catch(IOException e)
        {
          output_.println("Failed to make files deletable");
        }
      }
      else
      {
        try
        {
          raf1.close();
          raf2.close();
        }
        catch(Exception e) {}
      }

      // Remove files and directories that could not be deleted
      File file = new File(target + "TEST1.TXT");
      file.delete();
      file = new File(target + "ACCESS.LVL");
      file.delete();
      file = new File(target + "ACCESS.LST");
      file.delete();
      file = new File(target + "junk.txt");
      file.delete();
      file = new File(targetNoSep);
      file.delete();
    }
}

/**
Delete (nested) directories if empty during uninstall.
**/
public void Var021()
{
    try
    {
        createNestedFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        // Verify JT400.PKG file deleted
        else if ((new File(target + "JT400.PKG")).exists())
        {
            failed("JT400.PKG file not deleted");
            (new File(target + "JT400.PKG")).delete();
        }
        // Verify target directory deleted
        else if ((new File(target)).exists())
        {
            failed(target + " directory not deleted");
            (new File(targetNoSep)).delete();
        }
        else if (verifyNestedUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
}

/**
Do not delete directories if not empty during uninstall
**/
public void Var022()
{
    String extraFile1 = target + "install" + File.separator + "test" + "EXTRA.TXT";
    String extraFile2 = target + "install" + File.separator + "test2" + "EXTRA.TXT";
    try
    {
        createNestedFiles(target);
        // Create extra files in subdirectories.
        InstallTest.writeFile(extraFile1, "spiders");
        InstallTest.writeFile(extraFile2, "spiders");
        Vector errs = AS400ToolboxInstaller.unInstall("*ALL",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        String file1 = target + "install" + File.separator +
                   "test" + File.separator + "TEST1.TXT";
        String file2 = target + "install" + File.separator +
                   "test2" + File.separator + "TEST2.TXT";
        // Verify LVL file deleted
        if ((new File(target + "ACCESS.LVL")).exists())
        {
            failed("ACCESS.LVL file not deleted");
            (new File(target + "ACCESS.LVL")).delete();
        }
        // Verify package LST file deleted
        else if ((new File(target + "ACCESS.LST")).exists())
        {
            failed("ACCESS.LST file not deleted");
            (new File(target + "ACCESS.LST")).delete();
        }
        // Verify product files deleted
        else if ((new File(file1)).exists())
        {
            failed("TEST1.TXT file not deleted");
            (new File(file1)).delete();
        }
        else if ((new File(file2)).exists())
        {
            failed("TEST2.TXT file not deleted");
            (new File(file2)).delete();
        }
        // Verify extra files not deleted with directories
        else if (!(new File(extraFile1)).exists())
        {
            failed(extraFile1 + " file deleted");
        }
        else if (!(new File(extraFile2)).exists())
        {
            failed(extraFile2 + " file deleted");
        }
        // Verify JT400.PKG file deleted
        else if ((new File(target + "JT400.PKG")).exists())
        {
            failed("JT400.PKG file not deleted");
            (new File(target + "JT400.PKG")).delete();
        }
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove extra files and directories
        File file = new File(extraFile1);
        file.delete();
        file = new File(file.getParent());  // remove test/install/test
        file.delete();
        file = new File(extraFile2);
        file.delete();
        file = new File(file.getParent());  // remove test/install/test2
        file.delete();
        file = new File(file.getParent());  // remove test/install
        file.delete();
        file = new File(targetNoSep);       // remove test
        file.delete();
    }
}

/**
UnInstall a product with subdirectories on a system with forward slashes
as the path separator.
**/
public void Var023()
{
  if (!File.separator.equals("/"))
  {
    notApplicable("File separator is not forward-slash.");
    return;
  }

    try
    {
        createNestedFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyNestedUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
  }

/**
UnInstall a product with subdirectories on a system with backward slashes
as the path separator.
**/
public void Var024()
{
  if (!File.separator.equals("\\"))
  {
    notApplicable("File separator is not back-slash.");
    return;
  }

    try
    {
        createNestedFiles(target);
        Vector errs = AS400ToolboxInstaller.unInstall("ACCESS",
                                                      target);
        if (errs.size() != 0)
        {
            failed("Errors removing files.");
            output_.println(errs);
        }
        else if (verifyNestedUnInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove package list file
        File file = new File(target + "JT400.PKG");
        file.delete();
        // Remove directory
        file = new File(targetNoSep);
        file.delete();
    }
  }

}



