///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  INInstallTestcase.java
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
import java.net.URL;

import java.util.Vector;
import com.ibm.as400.access.AS400;

import test.InstallTest;
import test.Testcase;
import utilities.AS400ToolboxInstaller;

/**
Testcase INInstallTestcase.
Tests the following methods:
<ul>
<li>AS400ToolboxInstaller.install
</ul>
**/
public class INInstallTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "INInstallTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.InstallTest.main(newArgs); 
   }

    String target = InstallTest.targetPath + "test" + File.separator;
    String targetNoSep = InstallTest.targetPath + "test";

/**
Constructor.  This is called from InstallTest::createTestcases().
**/
public INInstallTestcase(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
{
    super(systemObject, "INInstallTestcase", 34,
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
    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }
    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }
    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }
    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }
    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }
    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }
    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }

}


/**
Creates test control files on the local system in <i>targetDir</i>.

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
void createLocalSourceFiles(String targetDir)
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
    // Create change LST file.
    InstallTest.writeFile(targetDir + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT"   );
}

/**
Verifies that files in <i>targetDir</i> match those written by
createLocalSourceFiles().
Returns true if they match, if they do not match, failed() will have been
called and false is returned.

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
boolean verifyLocalInstall(String targetDir)
    throws IOException
{
    // Verify PKG file
    if (!InstallTest.readFile(targetDir + "JT400.PKG").equals("ACCESS"))
        failed("PKG file not copied correctly.");
    // Verify LVL file
    else if (!InstallTest.readFile(targetDir + "ACCESS.LVL").equals("V1R1M1"))
        failed("LVL file not copied correctly.");
    // Verify package LST file
    else if (!InstallTest.readFile(targetDir + "ACCESS.LST").equals("TEST1.TXT\nTEST2.TXT"))
        failed("LST file not copied correctly.");
    // Verify product files
    else if (!InstallTest.readFile(targetDir + "TEST1.TXT").equals("yippee"))
        failed("Product file not copied correctly.");
    else if (!InstallTest.readFile(targetDir + "TEST2.TXT").equals("wow\nwee"))
        failed("Product file not copied correctly.");
    else
        return true;
    return false;
}

/**
Creates test control files on the local system in <i>targetDir</i>.
Different than createLocalControlFiles in that more than one package
is created.

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
void createLocalSourceFiles2(String targetDir)
    throws IOException
{
    // Create PKG file.
    InstallTest.writeFile(targetDir + "JT400.PKG", "ACCESS\nVACCESS");
    // Create LVL files.
    InstallTest.writeFile(targetDir + "ACCESS.LVL", "V1R1M1");
    InstallTest.writeFile(targetDir + "VACCESS.LVL", "V1R1M1");
    // Create package LST files.
    InstallTest.writeFile(targetDir + "ACCESS.LST", "TEST1.TXT\nTEST2.TXT");
    InstallTest.writeFile(targetDir + "VACCESS.LST", "VTEST1.TXT\nVTEST2.TXT");
    // Create product files.
    InstallTest.writeFile(targetDir + "TEST1.TXT", "yippee");
    InstallTest.writeFile(targetDir + "TEST2.TXT", "wow\nwee");
    InstallTest.writeFile(targetDir + "VTEST1.TXT", "vyippee");
    InstallTest.writeFile(targetDir + "VTEST2.TXT", "vwow\nvwee");
    // Create change LST file.
    InstallTest.writeFile(targetDir + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "VACCESS  ADD  NN   VTEST1.TXT\n" +
                         "VACCESS  ADD  NN   VTEST2.TXT"   );
}

/**
Verifies that files in <i>targetDir</i> match those written by
createLocalSourceFiles2().
Returns true if they match, if they do not match, failed() will have been
called and false is returned.

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
boolean verifyLocalInstall2(String targetDir)
    throws IOException
{
    // Verify PKG file
    if (!InstallTest.readFile(targetDir + "JT400.PKG").equals("ACCESS\nVACCESS"))
        failed("PKG file not copied correctly.");
    // Verify LVL files
    else if (!InstallTest.readFile(targetDir + "ACCESS.LVL").equals("V1R1M1"))
        failed("LVL file not copied correctly.");
    else if (!InstallTest.readFile(targetDir + "VACCESS.LVL").equals("V1R1M1"))
        failed("LVL file not copied correctly.");
    // Verify package LST files
    else if (!InstallTest.readFile(targetDir + "ACCESS.LST").equals("TEST1.TXT\nTEST2.TXT"))
        failed("LST file not copied correctly.");
    else if (!InstallTest.readFile(targetDir + "VACCESS.LST").equals("VTEST1.TXT\nVTEST2.TXT"))
        failed("LST file not copied correctly.");
    // Verify product files
    else if (!InstallTest.readFile(targetDir + "TEST1.TXT").equals("yippee"))
        failed("Product file not copied correctly.");
    else if (!InstallTest.readFile(targetDir + "TEST2.TXT").equals("wow\nwee"))
        failed("Product file not copied correctly.");
    else if (!InstallTest.readFile(targetDir + "VTEST1.TXT").equals("vyippee"))
        failed("Product file not copied correctly.");
    else if (!InstallTest.readFile(targetDir + "VTEST2.TXT").equals("vwow\nvwee"))
        failed("Product file not copied correctly.");
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
    // Create change LST file.
    InstallTest.writeFile(targetDir + "V1R1M1.LST",
                         "ACCESS   ADD  NN   install/test/TEST1.TXT\n" +
                         "ACCESS   ADD  NN   install/test2/TEST2.TXT"   );
}

/**
Verifies that files in <i>targetDir</i> match those written by
createNestedFiles().
Returns true if they match, if they do not match, failed() will have been
called and false is returned.

@parm targetDir Directory in which to create the files.

@exception IOException if any errors occur
**/
private boolean verifyNestedInstall(String targetDir)
    throws IOException
{
    String file1 = targetDir + "install" + File.separator +
                   "test" + File.separator + "TEST1.TXT";
    String file2 = targetDir + "install" + File.separator +
                   "test2" + File.separator + "TEST2.TXT";
    // Verify PKG file
    if (!InstallTest.readFile(targetDir + "JT400.PKG").equals("ACCESS"))
        failed("PKG file not copied correctly.");
    // Verify LVL file
    else if (!InstallTest.readFile(targetDir + "ACCESS.LVL").equals("V1R1M1"))
        failed("LVL file not copied correctly.");
    // Verify package LST file
    else if (!InstallTest.readFile(targetDir + "ACCESS.LST").equals(
          "install/test/TEST1.TXT\ninstall/test2/TEST2.TXT"))
        failed("LVL file not copied correctly.");
    // Verify product files
    else if (!InstallTest.readFile(file1).equals("yippee"))
        failed("TEST1.TXT file not copied correctly.");
    else if (!InstallTest.readFile(file2).equals("wow\nwee"))
        failed("TEST2.TXT file not copied correctly.");
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
        AS400ToolboxInstaller.install(null, "", InstallTest.localURL);
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
        AS400ToolboxInstaller.install("", null, InstallTest.localURL);
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
        AS400ToolboxInstaller.install("", "", null);
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
Verify files are downloaded correctly.
**/
public void Var004()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
//                                           InstallTest.remoteURL2))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
Use an AS/400 as the source.
Verify files are downloaded correctly.
**/
public void Var005()
{
    try
    {
        String source = InstallTest.remoteURL.toExternalForm();
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.remoteURL))
            failed("Update not needed when it is.");
        // Verify PKG file
        else if (!InstallTest.readFile(target + "JT400.PKG").equals
                 (InstallTest.readFile(new URL(source + "JT400.PKG"))))
            failed("PKG file not copied correctly.");
        // Verify LVL file
        else if (!InstallTest.readFile(target + "ACCESS.LVL").equals
                 (InstallTest.readFile(new URL(source + "ACCESS.LVL"))))
            failed("LVL file not copied correctly.");
        // Verify package LST file
        else if (!InstallTest.readFile(target + "ACCESS.LST").equals
                  (InstallTest.readFile(new URL(source + "ACCESS.LST"))))
            failed("LST file not copied correctly.");
        // Verify a couple product files
/*        else if (!InstallTest.readFile(target + "lib" + File.separator + "jt400.zip").equals
                  (InstallTest.readFile(new URL(source + "lib/jt400.zip"))))
            failed("Product file not copied correctly.");
        else if (!InstallTest.readFile(target + "com" + File.separator +
                 "ibm" + File.separator + "as400" + File.separator + "access"
                 + File.separator + "MRI.properties").equals
                 (InstallTest.readFile(new URL(source + "com/ibm/as400/access/MRI.properties"))))
            failed("Product file not copied correctly.");
*/        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception. You may need more local storage.");
    }
    finally
    {
        try
        {
            // Remove files in target and target directory.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            target);
        }
        catch (IOException e) {}
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
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
                                      new URL(urlStr));
        failed("No exception.");
    }
    catch (Exception e)
    {
        //                                       get rid of File:/
        // String fred = File.separator + urlStr.substring(6) + "ACCESS.LVL";
        // System.out.println(fred);
        // daw -- cannot get java 1.1.x and java 2 to produce same strings
        // if (exceptionIs(e, "FileNotFoundException", fred))
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
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
Use a source URL that does not exist.  (Exception)
**/
public void Var008()
{
    try
    {
        URL badURL = new URL("http", "BadSystem",
                        "/QIBM/ProdData/HTTP/Public/jt400/");
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
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
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           "test",
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall("test" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            "test" + File.separator);
        }
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Use a relative target path using "..".
**/
public void Var010()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                 "test" + File.separator + ".." + File.separator + "test",
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall("test" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            "test");
        }
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Use a relative target path using ".".
**/
public void Var011()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           "." + File.separator + "test",
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall("test" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            "test");
        }
        catch (IOException e) {e.printStackTrace(output_);}
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
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                          File.separator + "testing",
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall(File.separator + "testing" + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            File.separator + "testing");
        }
        catch (IOException e) {e.printStackTrace(output_);}
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
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           File.separator,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall(File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            File.separator);
        }
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Use a blank target path (indicating to install in current directory).
**/
public void Var014()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           "",
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall("." + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            ".");
        }
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Use a target path of '.' (indicating to install in current directory).
**/
public void Var015()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           ".",
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        else if (verifyLocalInstall("." + File.separator))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
                                            ".");
        }
        catch (IOException e) {e.printStackTrace(output_);}
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
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           targetStr,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Include trailing delimiter on target path.
**/
public void Var017()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Use a target directory that does not exist, it should be created.
**/
public void Var018()
{
    File dir = new File(targetNoSep);
    if (dir.exists() && !dir.delete())
          failed("Setup failed, directory exists.");
    else
    {
      try
      {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
            succeeded();
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
      finally
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
        catch (IOException e) {e.printStackTrace(output_);}
      }
    }
}

/**
Verify exception when target directory cannot be created.
**/
public void Var019()
{
    // Make sure the directory does not exist.
    File dir = new File(targetNoSep);
    if (dir.exists() && !dir.delete())
          failed("Setup failed, directory exists.");
    else
    {
      try
      {
        createLocalSourceFiles(InstallTest.targetPath);
        // Create a file of the same name so the directory cannot
        // be created.
        InstallTest.writeFile(InstallTest.targetPath + "test", "junk");

        AS400ToolboxInstaller.install("ACCESS",
                                      target,
                                      InstallTest.localURL);
        failed("No exception.");
        // Remove files in target and target directory.
        try { AS400ToolboxInstaller.unInstall("*ALL",
                                              target);}
        catch(Exception e){}
      }
      catch (Exception e)
      {
        String excText = "(" + target.substring(0, target.length()-1) + ") Cannot create directory.";
        if (exceptionIs(e, "IOException", excText))
            succeeded();
        else
            failed(e, "Wrong exception info");
      }
      finally
      {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        // remove dummy file
        file = new File(InstallTest.targetPath + "test");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
        }
        catch (IOException e) {e.printStackTrace(output_);}
      }
    }
}

/**
Use *ALL for the package when no updates needed.
**/
public void Var020()
{
    try
    {
        createLocalSourceFiles2(InstallTest.targetPath);
        createLocalSourceFiles2(target);
        if (AS400ToolboxInstaller.install("*ALL",
                                          target,
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
        // Remove change list files from source and target
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        file = new File(target + "V1R1M1.LST");
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Use *ALL for the package when updates needed.
**/
public void Var021()
{
    try
    {
        createLocalSourceFiles2(InstallTest.targetPath);
        createLocalSourceFiles(target);
        if (!AS400ToolboxInstaller.install("*ALL",
                                          target,
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall2(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove change list files from source and target
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        file = new File(target + "V1R1M1.LST");
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Install when not installed.
**/
public void Var022()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                          target,
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Install when update not needed.
**/
public void Var023()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        createLocalSourceFiles(target);
        if (AS400ToolboxInstaller.install("ACCESS",
                                          target,
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
        // Remove change list files from source and target
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        file = new File(target + "V1R1M1.LST");
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Install when source is back-level.
**/
public void Var024()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        // put a longer LVL file in the target
        InstallTest.writeFile(target + "ACCESS.LVL", "V1R1M1\nV1R1M1\nV1R1M1\nV1R1M1\n");
        if (AS400ToolboxInstaller.install("ACCESS",
                                          target,
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
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        try
        {
            // Remove remaining files in source.
            AS400ToolboxInstaller.unInstall("*ALL",
                                            InstallTest.targetPath);
            // Remove files in target and target directory.
            file = new File(target + "ACCESS.LVL");
            file.delete();
            file = new File(targetNoSep);
            file.delete();
        }
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Verify all combinations of updates to be sure combining works.
**/
public void Var025()
{
    try
    {
        // First create a set of product and control files at the source.
        // Create PKG file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG", "ACCESS");
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "V1R1M1\nV1R1M2");
        // Create package LST file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
          "TEST1.TXT\nTEST2.TXT\nTEST3.TXT\nTEST4.TXT\nTEST5.TXT\nTEST6.TXT\nTEST7.TXT\nTEST8.TXT\nTEST9.TXT");
        // Create product files at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.TXT", "yeah1");
        InstallTest.writeFile(InstallTest.targetPath + "TEST2.TXT", "yeah2");
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.TXT", "yeah3");
        InstallTest.writeFile(InstallTest.targetPath + "TEST4.TXT", "yeah4");
        InstallTest.writeFile(InstallTest.targetPath + "TEST5.TXT", "yeah5");
        InstallTest.writeFile(InstallTest.targetPath + "TEST6.TXT", "yeah6");
        InstallTest.writeFile(InstallTest.targetPath + "TEST7.TXT", "yeah7");
        InstallTest.writeFile(InstallTest.targetPath + "TEST8.TXT", "yeah8");
        InstallTest.writeFile(InstallTest.targetPath + "TEST9.TXT", "yeah9");
        // Create change LST files at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "ACCESS   ADD  NN   TEST3.TXT\n" +
                         "ACCESS   UPD  NN   TEST4.TXT\n" +
                         "ACCESS   UPD  NN   TEST5.TXT\n" +
                         "ACCESS   UPD  NN   TEST6.TXT\n" +
                         "ACCESS   RMV  NN   TEST7.TXT\n" +
                         "ACCESS   RMV  NN   TEST8.TXT\n" +
                         "ACCESS   RMV  NN   TEST9.TXT\n" );
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M2.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   UPD  NN   TEST2.TXT\n" +
                         "ACCESS   RMV  NN   TEST3.TXT\n" +
                         "ACCESS   ADD  NN   TEST4.TXT\n" +
                         "ACCESS   UPD  NN   TEST5.TXT\n" +
                         "ACCESS   RMV  NN   TEST6.TXT\n" +
                         "ACCESS   ADD  NN   TEST7.TXT\n" +
                         "ACCESS   UPD  NN   TEST8.TXT\n" +
                         "ACCESS   RMV  NN   TEST9.TXT\n" );

        // Create a set of product files in the target that have the same
        // names as in the source, but have different contents so we can verify
        // if they were changed.
        InstallTest.writeFile(target + "TEST1.TXT", "yippee");
        InstallTest.writeFile(target + "TEST2.TXT", "yippee");
        InstallTest.writeFile(target + "TEST3.TXT", "yippee");
        InstallTest.writeFile(target + "TEST4.TXT", "yippee");
        InstallTest.writeFile(target + "TEST5.TXT", "yippee");
        InstallTest.writeFile(target + "TEST6.TXT", "yippee");
        InstallTest.writeFile(target + "TEST7.TXT", "yippee");
        InstallTest.writeFile(target + "TEST8.TXT", "yippee");
        InstallTest.writeFile(target + "TEST9.TXT", "yippee");

        // Do the update.
        if (!AS400ToolboxInstaller.install("ACCESS",
                                          target,
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        // Verify files that should have been removed are no longer in target.
        if ((new File(target + "TEST3.TXT")).exists())
            failed("TEST3.TXT exists in " + target);
        else if ((new File(target + "TEST6.TXT")).exists())
            failed("TEST6.TXT exists in " + target);
        else if ((new File(target + "TEST9.TXT")).exists())
            failed("TEST9.TXT exists in " + target);
        // Verify files that should have been added or updated contain new content.
        else if (!InstallTest.readFile(target + "TEST1.TXT").equals("yeah1"))
            failed("TEST1 not updated.");
        else if (!InstallTest.readFile(target + "TEST2.TXT").equals("yeah2"))
            failed("TEST2 not updated.");
        else if (!InstallTest.readFile(target + "TEST4.TXT").equals("yeah4"))
            failed("TEST4 not updated.");
        else if (!InstallTest.readFile(target + "TEST5.TXT").equals("yeah5"))
            failed("TEST5 not updated.");
        else if (!InstallTest.readFile(target + "TEST7.TXT").equals("yeah7"))
            failed("TEST7 not updated.");
        else if (!InstallTest.readFile(target + "TEST8.TXT").equals("yeah8"))
            failed("TEST8 not updated.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Verify only new updates are applied and control files are updated.
**/
public void Var026()
{
    try
    {
        // First create a set of product and control files at the source.
        // Create PKG file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "JT400.PKG", "ACCESS");
        // Create LVL file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LVL", "V1R1M1\nV1R1M2");
        // Create package LST file at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "ACCESS.LST",
          "TEST1.TXT\nTEST2.TXT\nTEST3.TXT\nTEST4.TXT\nTEST5.TXT\nTEST6.TXT");
        // Create product files at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "TEST1.TXT", "yeah1");
        InstallTest.writeFile(InstallTest.targetPath + "TEST2.TXT", "yeah2");
        InstallTest.writeFile(InstallTest.targetPath + "TEST3.TXT", "yeah3");
        InstallTest.writeFile(InstallTest.targetPath + "TEST4.TXT", "yeah4");
        InstallTest.writeFile(InstallTest.targetPath + "TEST5.TXT", "yeah5");
        InstallTest.writeFile(InstallTest.targetPath + "TEST6.TXT", "yeah6");
        // Create change LST files at local URL location.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "ACCESS   ADD  NN   TEST3.TXT\n" );
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M2.LST",
                         "ACCESS   ADD  NN   TEST4.TXT\n" +
                         "ACCESS   ADD  NN   TEST5.TXT\n" +
                         "ACCESS   ADD  NN   TEST6.TXT\n" );

        // Create down-level control files at the target location.
        // Create PKG file.
        InstallTest.writeFile(target + "JT400.PKG", "old");
        // Create LVL file.
        InstallTest.writeFile(target + "ACCESS.LVL", "V1R1M1");
        // Create package LST file.
        InstallTest.writeFile(target + "ACCESS.LST",
          "TEST1.TXT\nTEST2.TXT\nTEST3.TXT");

        // Do the update.
        if (!AS400ToolboxInstaller.install("ACCESS",
                                          target,
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        // Verify files in update that was already applied have not been copied.
        if ((new File(target + "TEST1.TXT")).exists())
            failed("TEST1.TXT exists in " + target);
        else if ((new File(target + "TEST2.TXT")).exists())
            failed("TEST2.TXT exists in " + target);
        else if ((new File(target + "TEST3.TXT")).exists())
            failed("TEST3.TXT exists in " + target);
        // Verify files that should have been added were.                              .
        else if (!(new File(target + "TEST4.TXT")).exists())
            failed("TEST4.TXT does not exist in " + target);
        else if (!(new File(target + "TEST5.TXT")).exists())
            failed("TEST5.TXT does not exist in " + target);
        else if (!(new File(target + "TEST6.TXT")).exists())
            failed("TEST6.TXT does not exist in " + target);
        // Verify control files have been updated in target.
        else if (!InstallTest.readFile(target + "ACCESS.LVL").equals("V1R1M1\nV1R1M2"))
            failed("ACCESS.LVL not updated.");
        else if (!InstallTest.readFile(target + "JT400.PKG").equals("ACCESS"))
            failed("JT400.PKG not updated.");
        else if (!InstallTest.readFile(target + "ACCESS.LST").equals(
          "TEST1.TXT\nTEST2.TXT\nTEST3.TXT\nTEST4.TXT\nTEST5.TXT\nTEST6.TXT"))
            failed("ACCESS.LST not updated.");
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Verify only those files belonging to installed package are downloaded.
**/
public void Var027()
{
    try
    {
        createLocalSourceFiles2(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                          target,
                                          InstallTest.localURL))
            failed("Update not needed when it is.");
        // Verify files in VACCESS were not installed."
        if ((new File(target + "VTEST1.TXT")).exists())
            failed("VTEST1.TXT exists in " + target);
        else if ((new File(target + "VTEST2.TXT")).exists())
            failed("VTEST2.TXT exists in " + target);
        else if ((new File(target + "VACCESS.LVL")).exists())
            failed("VACCESS.LVL exists in " + target);
        else if ((new File(target + "VACCESS.LST")).exists())
            failed("VACCESS.LST exists in " + target);
        else
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Control files missing on source.  (Exception)
**/
public void Var028()
{
    if ((new File(InstallTest.targetPath + "ACCESS.LVL")).exists())
        failed("Setup failed.");
    else
    {
      try
      {
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
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
Missing properties file will not cause install failure.
**/
public void Var029()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        // Create some extra properties files
        InstallTest.writeFile(InstallTest.targetPath + "MRI.properties", "here is text");
        InstallTest.writeFile(InstallTest.targetPath + "MRI2.properties", "more\ntext");
        // Overwrite change LST file to add properties files.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "ACCESS   ADD  NN   MRI.properties\n" +
                         "ACCESS   ADD  NN   MRI1.properties\n" +
                         "ACCESS   ADD  NN   MRI2.properties"   );
        // Don't overwrite package list file or verify will fail below.
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
        {
            // Verify properties files
            if (!InstallTest.readFile(target + "MRI.properties").equals("here is text"))
                failed("Property file not copied correctly.");
            if (!InstallTest.readFile(target + "MRI2.properties").equals("more\ntext"))
                failed("Property file not copied correctly.");
            else
                succeeded();
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        // Remove properties files from target and source.
        // (Not in package list file so uninstall won't delete them.)
        file = new File(InstallTest.targetPath + "MRI.properties");
        file.delete();
        file = new File(InstallTest.targetPath + "MRI2.properties");
        file.delete();
        file = new File(target + "MRI.properties");
        file.delete();
        file = new File(target + "MRI2.properties");
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Missing files (other than properties) will cause failure.
**/
public void Var030()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        // Overwrite change LST file to add a file.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "ACCESS   ADD  NN   MISSING.FILE" );
        AS400ToolboxInstaller.install("ACCESS",
                                      target,
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
    finally
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        // Remove properties files from target and source.
        // (Not in package list file so uninstall won't delete them.)
        file = new File(InstallTest.targetPath + "MRI.properties");
        file.delete();
        file = new File(InstallTest.targetPath + "MRI2.properties");
        file.delete();
        file = new File(target + "MRI.properties");
        file.delete();
        file = new File(target + "MRI2.properties");
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
All versions of properties files available will be downloaded.
**/
public void Var031()
{
    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        // Create some extra properties files
        InstallTest.writeFile(InstallTest.targetPath + "MRI.properties", "here is text");
        InstallTest.writeFile(InstallTest.targetPath + "MRI2.properties", "more\ntext");
        // Overwrite change LST file to add properties files.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NN   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "ACCESS   ADD  NN   MRI.properties\n" +
                         "ACCESS   ADD  NN   MRI1.properties\n" +
                         "ACCESS   ADD  NN   MRI2.properties"   );
        // Don't overwrite package list file or verify will fail below.
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
        {
            // Verify properties files
            if (!InstallTest.readFile(target + "MRI.properties").equals("here is text"))
                failed("Property file not copied correctly.");
            if (!InstallTest.readFile(target + "MRI2.properties").equals("more\ntext"))
                failed("Property file not copied correctly.");
            else
                succeeded();
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        // Remove change list file from source
        File file = new File(InstallTest.targetPath + "V1R1M1.LST");
        file.delete();
        // Remove properties files from target and source.
        // (Not in package list file so uninstall won't delete them.)
        file = new File(InstallTest.targetPath + "MRI.properties");
        file.delete();
        file = new File(InstallTest.targetPath + "MRI2.properties");
        file.delete();
        file = new File(target + "MRI.properties");
        file.delete();
        file = new File(target + "MRI2.properties");
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
}

/**
Verify classpath code does not fail when running as an applet
(can't get classpath).
**/
public void Var032()
{
  if (!isApplet_)
  {
    notApplicable("Not running as an applet.");
    return;
  }

    try
    {
        createLocalSourceFiles(InstallTest.targetPath);
        // Overwrite change LST file to make classpath affected.
        InstallTest.writeFile(InstallTest.targetPath + "V1R1M1.LST",
                         "ACCESS   ADD  NY   TEST1.TXT\n" +
                         "ACCESS   ADD  NN   TEST2.TXT\n" +
                         "ACCESS   PADD NY   \n" );
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyLocalInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
  }

/**
Install a product with subdirectories on a system with forward slashes
as the path separator.
**/
public void Var033()
{
  if (!File.separator.equals("/"))
  {
    notApplicable("File separator is not forward-slash.");
    return;
  }

    try
    {
        createNestedFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyNestedInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
  }


/**
Install a product with subdirectories on a system with backward slashes
as the path separator.
**/
public void Var034()
{
  if (!File.separator.equals("\\"))
  {
    notApplicable("File separator is not back-slash.");
    return;
  }

    try
    {
        createNestedFiles(InstallTest.targetPath);
        if (!AS400ToolboxInstaller.install("ACCESS",
                                           target,
                                           InstallTest.localURL))
            failed("Update not needed when it is.");
        if (verifyNestedInstall(target))
            succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
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
        catch (IOException e) {e.printStackTrace(output_);}
    }
  }

}



