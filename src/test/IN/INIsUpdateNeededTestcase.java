///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INIsUpdateNeededTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.IN;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.InstallTest;
import test.Testcase;
import utilities.AS400ToolboxInstaller;

/**
Testcase INIsUpdateNeededTestcase.
Tests the following methods:
<ul>
<li>AS400ToolboxInstaller.isUpdateNeeded
</ul>
**/
@SuppressWarnings("deprecation")
public class INIsUpdateNeededTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "INIsUpdateNeededTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.InstallTest.main(newArgs); 
   }

/**
Constructor.  This is called from InstallTest::createTestcases().
**/
public INIsUpdateNeededTestcase(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
{
    super(systemObject, "INIsUpdateNeededTestcase", 27,
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
    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }
    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }
    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }

}


/**
Null packageName parm.
**/
public void Var001()
{
    try
    {
        AS400ToolboxInstaller.isUpdateNeeded(null, "", InstallTest.localURL);
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
        AS400ToolboxInstaller.isUpdateNeeded("", null, InstallTest.localURL);
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
Null source parm.
**/
public void Var003()
{
    try
    {
        AS400ToolboxInstaller.isUpdateNeeded("", "", null);
        failed("No exception.");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "NullPointerException", "source"))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
Use source URL protocols other than http.
**/
public void Var004()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 InstallTest.targetPath,
                                                 InstallTest.localURL))
            failed("Update needed when it is not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Attempt cleanup
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
    }
}

/**
Use an AS/400 as the source.
**/
public void Var005()
{
    try
    {
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 InstallTest.targetPath,
                                                 InstallTest.remoteURL))
            succeeded();
        else
            failed("No update needed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
}

/**
Do not include trailing delimiter on source URL (error).
**/
public void Var006()
{
    String urlStr = InstallTest.localURL.toExternalForm();
    // Make sure the source URL does not have a trailing separator.
    if (File.separator.length() == 1)
        urlStr = urlStr.substring(0, urlStr.length()-1);
    else
        urlStr = urlStr.substring(0, urlStr.length()-2);
    try
    {
        AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                             InstallTest.targetPath,
                                             new URL(urlStr));
        failed("No exception.");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "FileNotFoundException"))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
Include trailing delimiter on source URL.
**/
public void Var007()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 InstallTest.targetPath,
                                                 InstallTest.localURL))
            failed("Update needed when it is not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Attempt cleanup
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
    }
}

/**
Use a source URL that does not exist.  (Exception)
**/
public void Var008()
{
    try
    {
        URL badURL = new URL("http", "BadSystem",
                        "/QIBM/ProdData/HTTP/Public/jt400/");
        AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                             InstallTest.targetPath,
                                             badURL);
        failed("No exception.");
    }
    catch (Exception e)
    {
        if (exceptionIs(e, "UnknownHostException"))
            succeeded();
        else
            failed(e, "Wrong exception info");
    }
}

/**
Use a target path that is a subdirectory of the current directory,
not fully qualifying the path.
**/
public void Var009()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in subdirectory to verify correct target directory
        // being accessed
        InstallTest.writeFile("test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 "test",
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File("test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File("test");
        file.delete();
    }
}

/**
Use a relative target path using "..".
**/
public void Var010()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in subdirectory to verify correct target directory
        // being accessed
        InstallTest.writeFile("test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                 "test" + File.separator + ".." + File.separator + "test",
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File("test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File("test");
        file.delete();
    }
}

/**
Use a relative target path using ".".
**/
public void Var011()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in subdirectory to verify correct target directory
        // being accessed
        InstallTest.writeFile("test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 "." + File.separator + "test",
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File("test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File("test");
        file.delete();
    }
}

/**
Use a target path starting at the root.
**/
public void Var012()
{
    if (InstallTest.runningAIX_) {
      notApplicable("Running on AIX");
      return;
    }
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in root subdirectory to verify correct target
        // directory is being accessed
        InstallTest.writeFile(File.separator + "test" + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 File.separator + "test",
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(File.separator + "test" + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(File.separator + "test");
        file.delete();
    }
}

/**
Use a target path of the root.
**/
public void Var013()
{
    if (InstallTest.runningAIX_) {
      notApplicable("Running on AIX");
      return;
    }
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in root to verify correct target
        // directory is being accessed
        InstallTest.writeFile(File.separator  + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 File.separator,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(File.separator + "ACCESS.LVL");
        file.delete();
    }
}

/**
Use a blank target path (indicating to check the current directory).
**/
public void Var014()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile("ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 "",
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File("ACCESS.LVL");
        file.delete();
    }
}

/**
Use a target path of '.' (indicating to check the current directory).
**/
public void Var015()
{
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile("ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 ".",
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File("ACCESS.LVL");
        file.delete();
    }
}

/**
Do not include trailing delimiter on target path.
**/
public void Var016()
{
    String targetStr = InstallTest.targetPath + "test";
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + File.separator + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + File.separator + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(targetStr);
        file.delete();
    }
}

/**
Include trailing delimiter on target path.
**/
public void Var017()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
Use a target directory that does not exist.
**/
public void Var018()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    File dir = new File(InstallTest.targetPath + "test");
    if (dir.exists() && !dir.delete())
      failed("Setup failed, directory exists.");
    else
    {
      try
      {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            succeeded();
        else
            failed("No update when update needed.");
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
      finally
      {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
      }
    }
}

/**
Use *ALL for the package when no updates needed.
**/
public void Var019()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create PKG file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG", "ACCESS\nVACCESS");
        // Create LVL files at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL", "123");
        // Create LVL files in target directory.
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        InstallTest.writeFile(targetStr + "VACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("*ALL",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source PKG file
        File file = new File(InstallTest.targetPath + "JT400.PKG");
        file.delete();
        // Cleanup source LVL files
        file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        file = new File(InstallTest.targetPath + "VACCESS.LVL");
        file.delete();
        // Cleanup target LVL files
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        file = new File(targetStr + "VACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
Use *ALL for the package when updates needed.
**/
public void Var020()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create PKG file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG", "ACCESS\nVACCESS");
        // Create LVL files at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        InstallTest.writeFile(InstallTest.targetPath + "VACCESS.LVL", "123");
        // Create LVL files in target directory.
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("*ALL",
                                                 targetStr,
                                                 InstallTest.localURL))
            succeeded();
        else
            failed("Update not needed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source PKG file
        File file = new File(InstallTest.targetPath + "JT400.PKG");
        file.delete();
        // Cleanup source LVL files
        file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        file = new File(InstallTest.targetPath + "VACCESS.LVL");
        file.delete();
        // Cleanup target LVL files
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
When not installed.
**/
public void Var021()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            succeeded();
        else
            failed("No update needed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
When update not needed.
**/
public void Var022()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in target directory
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
When update needed.
**/
public void Var023()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123\n456");
        // Create LVL file in target directory
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            succeeded();
        else
            failed("No update needed when it is.");
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
When source is back-level.
**/
public void Var024()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file in target directory
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123\n456");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
}

/**
Control files missing on source.  (Exception)
**/
public void Var025()
{
    String targetStr = InstallTest.targetPath + "test" + File.separator;
    if ((new File(InstallTest.targetPath + "ACCESS.LVL")).exists())
        failed("Setup failed.");
    else
    {
      try
      {
        AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                             targetStr,
                                             InstallTest.localURL);
        failed("No exception.");
      }
      catch (Exception e)
      {
        if (exceptionIs(e, "FileNotFoundException"))
            succeeded();
        else
            failed(e, "Wrong exception info");
      }
    }
}

/**
On a system with forward slashes as the path separator.
**/
public void Var026()
{
  if (!File.separator.equals("/"))
  {
    notApplicable("File separator is not forward-slash.");
    return;
  }

    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
  }

/**
On a system with backward slashes as the path separator.
**/
public void Var027()
{
  if (!File.separator.equals("\\"))
  {
    notApplicable("File separator is not back-slash.");
    return;
  }

    String targetStr = InstallTest.targetPath + "test" + File.separator;
    try
    {
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "123");
        // Create LVL file to verify correct target directory
        // being accessed
        InstallTest.writeFile(targetStr + "ACCESS.LVL", "123");
        if (AS400ToolboxInstaller.isUpdateNeeded("ACCESS",
                                                 targetStr,
                                                 InstallTest.localURL))
            failed("Update needed when not.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Cleanup source LVL file
        File file = new File(InstallTest.targetPath + "ACCESS.LVL");
        file.delete();
        // Cleanup target LVL file
        file = new File(targetStr + "ACCESS.LVL");
        file.delete();
        // Cleanup test subdirectory
        file = new File(InstallTest.targetPath + "test");
        file.delete();
    }
  }

}



