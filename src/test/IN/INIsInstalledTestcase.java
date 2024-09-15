///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INIsInstalledTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IN;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import java.util.Vector;
import com.ibm.as400.access.AS400;

import test.InstallTest;
import test.Testcase;
import utilities.AS400ToolboxInstaller;

/**
Testcase INIsInstalledTestcase.
Tests the following methods:
<ul>
<li>AS400ToolboxInstaller.isInstalled
</ul>
**/
public class INIsInstalledTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "INIsInstalledTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.InstallTest.main(newArgs); 
   }

/**
Constructor.  This is called from InstallTest::createTestcases().
**/
public INIsInstalledTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
{
    super(systemObject, "INIsInstalledTestcase", 16,
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

}

/**
Null packageName parm.
**/
public void Var001()
{
    try
    {
        AS400ToolboxInstaller.isInstalled(null, "");
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
        AS400ToolboxInstaller.isInstalled("", null);
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
        // Create LVL file in subdirectory to verify correct target directory
        // being accessed
        InstallTest.writeFile("test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              "test"))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File("test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
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
        // Create LVL file in subdirectory to verify correct target directory
        // being accessed
        InstallTest.writeFile("test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                 "test" + File.separator + ".." + File.separator + "test"))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File("test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File("test");
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
        // Create LVL file in subdirectory to verify correct target directory
        // being accessed
        InstallTest.writeFile("test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              "." + File.separator + "test"))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File("test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
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
        // Create LVL file in root subdirectory to verify correct target
        // directory is being accessed
        InstallTest.writeFile(File.separator + "test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              File.separator + "test"))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(File.separator + "test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
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
        // Create LVL file in root to verify correct target
        // directory is being accessed
        InstallTest.writeFile(File.separator  + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              File.separator))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(File.separator + "ACCESS.LVL");
        file.delete();
    }
}

/**
Use a blank target path (indicating to check the current directory).
**/
public void Var008()
{
    try
    {
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile("ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              ""))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File("ACCESS.LVL");
        file.delete();
    }
}

/**
Use a target path of '.' (indicating to check the current directory).
**/
public void Var009()
{
    try
    {
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile("ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              "."))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File("ACCESS.LVL");
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
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(targetStr + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(targetStr);
        file.delete();
    }
}

/**
Include trailing delimiter on target path.
**/
public void Var011()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
Use a target directory that does not exist.
**/
public void Var012()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    File dir = new File(InstallTest.targetPath + "test");
    if (dir.exists() && !dir.delete())
      failed("Setup failed, directory exists.");
    else
    {
      try
      {
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            failed("Installed when it is not.");
        else
            succeeded();
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }
}

/**
IsInstalled when not installed
**/
public void Var013()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            failed("Installed when it is not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
}

/**
IsInstalled when installed
**/
public void Var014()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file in target directory
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
On a system with forward slashes as the path separator.
**/
public void Var015()
{
  if (!File.separator.equals("/"))
  {
    notApplicable("File separator is not forward-slash.");
    return;
  }

    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
  }

/**
On a system with backward slashes as the path separator.
**/
public void Var016()
{
  if (!File.separator.equals("\\"))
  {
    notApplicable("File separator is not back-slash.");
    return;
  }

    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isInstalled("ACCESS",
                                              targetStr))
            succeeded();
        else
            failed("Not installed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup target LVL file
        File file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
  }

}



