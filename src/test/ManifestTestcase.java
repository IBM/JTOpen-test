///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ManifestTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.awt.TextArea;
import java.io.FileOutputStream;
import java.util.Vector;

/**
 * Testcase ManifestTestcase.
 *
 * Test variations for the build-generated jar files and their manifests.
 **/
public class ManifestTestcase extends Testcase
{
  /**
   * Constructor.  This is called from the ManifestTest constructor.
   **/
  public ManifestTestcase(AS400 systemObject, Vector variationsToRun, int runMode, FileOutputStream fileOutputStream)
  {
    super(systemObject, "ManifestTestcase", 9, variationsToRun, runMode, fileOutputStream);
  }


  /**
   * Loads a class from each package to setup the testcase.
  **/
  public void setup()
  {
    boolean f = false;
    try
    {
      new com.ibm.as400.access.AS400();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("access not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.vaccess.CommandCallButton();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("vaccess not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.util.html.BidiOrdering();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("html not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.util.servlet.HTMLFormConverter();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("servlet not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.resource.BufferedResourceList();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("resource not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.data.DAMRI();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("data not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.security.SecurityMRI();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("security not found.");
      f = true;
    }
    try
    {
      new com.ibm.as400.security.auth.AS400CredentialBeanInfo();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("security.auth not found.");
      f = true;
    }
    try
    {
      new utilities.AS400ToolboxJarMaker();
    }
    catch (NoClassDefFoundError e)
    {
      output_.println("utilities not found.");
      f = true;
    }
    if (f)
    {
      output_.println("Warning: Some classes were not loaded. Expect some failures.");
    }
  }


  /**
   * Runs the variations requested.
   **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    setup();

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
  }


  // Helper to compare package information
  private void checkPackageInfo(String name)
  {
    try
    {
      Package p = Package.getPackage(name);
      if (p == null)
      {
        failed("No package info for "+name+".\nMake sure the required jar files (not class files) are in your CLASSPATH.");
        return;
      }
      String failStr = "";
      if (!p.getImplementationTitle().equals(name))
      {
        failStr += "\nMismatched implementation titles: '"+p.getImplementationTitle()+"' != '"+name+"'";
      }
      if (!p.getImplementationVendor().equals("IBM Corporation and others"))
      {
        failStr += "\nMismatched implementation vendors: '"+p.getImplementationVendor()+"' != 'IBM Corporation'";
      }
      if (!p.getImplementationVersion().startsWith("JTOpen 6.3"))
      {
    	System.out.println("This value needs to be edited please verify that the testcase is updated with the latest version."); 
        failStr += "\nMismatched implementation versions: '"+p.getImplementationVersion()+"' != 'JTOpen 6.3'";
      }
      if (!p.getSpecificationTitle().equals("IBM Toolbox for Java"))
      {
        failStr += "\nMismatched specification titles: '"+p.getSpecificationTitle()+"' != 'IBM Toolbox for Java'";
      }
      if (!p.getSpecificationVendor().equals("IBM Corporation"))
      {
        failStr += "\nMismatched specification vendors: '"+p.getSpecificationVendor()+"' != 'IBM Corporation'";
      }
      if (!p.getSpecificationVersion().equals("6.1.0.4"))
      {
    	System.out.println("This value needs to be edited please verify that the testcase is updated with the latest version.");  
        failStr += "\nMismatched specification versions: '"+p.getSpecificationVersion()+"' != '6.1.0.4'";
      }
      if (failStr.length() > 0)
      {
        failed("Package information incorrect."+failStr);
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }


  /**
   * Test the package information for the com.ibm.as400.access package.
   **/
  public void Var001()
  {
    checkPackageInfo("com.ibm.as400.access");
  }


  /**
   * Test the package information for the com.ibm.as400.vaccess package.
   **/
  public void Var002()
  {
    checkPackageInfo("com.ibm.as400.vaccess");
  }


  /**
   * Test the package information for the com.ibm.as400.util.html package.
   **/
  public void Var003()
  {
    checkPackageInfo("com.ibm.as400.util.html");
  }


  /**
   * Test the package information for the com.ibm.as400.util.servlet package.
   **/
  public void Var004()
  {
    checkPackageInfo("com.ibm.as400.util.servlet");
  }


  /**
   * Test the package information for the com.ibm.as400.resource package.
   **/
  public void Var005()
  {
    checkPackageInfo("com.ibm.as400.resource");
  }


  /**
   * Test the package information for the com.ibm.as400.security package.
   **/
  public void Var006()
  {
    checkPackageInfo("com.ibm.as400.security");
  }


  /**
   * Test the package information for the com.ibm.as400.security.auth package.
   **/
  public void Var007()
  {
    checkPackageInfo("com.ibm.as400.security.auth");
  }


  /**
   * Test the package information for the com.ibm.as400.data package.
   **/
  public void Var008()
  {
    checkPackageInfo("com.ibm.as400.data");
  }


  /**
   * Test the package information for the utilities package.
   **/
  public void Var009()
  {
//    checkPackageInfo("utilities");
    notApplicable("The utilities package is not part of a jar file.");
  }
}
