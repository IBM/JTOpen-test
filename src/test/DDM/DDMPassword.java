///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMPassword.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.*;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 *Testcase DDMPassword.  This test class verifies DDM connections with various release levels of the AS/400.
 *<ul compact>
 *<li>record formats
 *<li>field descriptions
 *</ul>
**/
public class DDMPassword extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMPassword";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  long start;
  long time;
  String testLib_ = "DDMTEST";

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMPassword(AS400            systemObject,
                      Vector<String> variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMPassword", 7, variationsToRun, runMode,
          fileOutputStream);
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMPassword(AS400            systemObject,
                      Vector<String> variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMPassword", 7, variationsToRun, runMode,
          fileOutputStream);
    if (testLib != null)
    {
      testLib_ = testLib;
    }
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Do any necessary setup work for the variations
    try
    {
      setup();
    }
    catch (Exception e)
    {
      // Testcase setup did not complete successfully
      System.out.println("Unable to complete setup; variations not run");
      return;
    }

    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("2"))
        Var002();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("3"))
        Var003();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("4"))
        Var004();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("5"))
        Var005();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("6"))
        Var006();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("7"))
        Var007();
    }


    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to complete cleanup.");
    }

    // Disconnect from the AS/400 for record the record level access service
    systemObject_.disconnectAllServices();
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void cleanup()
    throws Exception
  {
  }

  /**
   *Test connection to pre-V4R2 system.
   *<ul compact>
   *<li>No exchange of security information is done in this case.
   *<li>No password substitution is done.
   *</ul>
   *
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var001()
  {
    setVariation(1);
    try
    {
      if (systemObject_.getVRM() >= AS400.generateVRM(4, 2, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not pre-V4R2.");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Test connection to V4R2 or V4R3 system.
   *<ul compact>
   *<li>Security information should be exchanged.
   *<li>Password substitution should not be performed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var002()
  {
    setVariation(2);
    try
    {
      if (systemObject_.getVRM() >= AS400.generateVRM(4, 4, 0) ||
          systemObject_.getVRM() < AS400.generateVRM(4, 2, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not V4R2 or V4R3.");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Test connection to V4R4 or V4R5 system.
   *<ul compact>
   *<li>Security information should be exchanged.
   *<li>Password substitution should occur.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var003()
  {
    setVariation(3);
    try
    {
      if (systemObject_.getVRM() < AS400.generateVRM(4, 4, 0) ||
          systemObject_.getVRM() >= AS400.generateVRM(4, 6, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not V4R4 or V4R5.");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Test connection to V5R1 system at password level 0 or 1.
   *<ul compact>
   *<li>Security information should be exchanged.
   *<li>Password substitution should occur.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var004()
  {
    setVariation(4);
    try
    {
      if (systemObject_.getVRM() != AS400.generateVRM(5, 1, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not V5R1.");
        return;
      }
      try
      {
         Class fred = Class.forName("com.ibm.as400.access.SystemValue");
      }
      catch(Throwable e)
      {
         notApplicable("Could not load SystemValue class, probably running as proxy.");
         return;
      }
      SystemValue sv = new SystemValue(systemObject_, "QPWDLVL");
      String val = sv.getValue().toString().trim();
      int i = Integer.parseInt(val);
      if (i != 0 && i != 1)
      {
        notApplicable(systemObject_.getSystemName()+" is not at password level 0 or 1. It is at level "+i+".");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Test connection to V5R1 system at password level 2 or 3.
   *<ul compact>
   *<li>Security information should be exchanged.
   *<li>Strong encryption of the password should occur.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var005()
  {
    setVariation(5);
    try
    {
      if (systemObject_.getVRM() != AS400.generateVRM(5, 1, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not V5R1.");
        return;
      }
      try
      {
         Class fred = Class.forName("com.ibm.as400.access.SystemValue");
      }
      catch(Throwable e)
      {
         notApplicable("Could not load SystemValue class, probably running as proxy.");
         return;
      }
      SystemValue sv = new SystemValue(systemObject_, "QPWDLVL");
      String val = sv.getValue().toString().trim();
      int i = Integer.parseInt(val);
      if (i != 2 && i != 3)
      {
        notApplicable(systemObject_.getSystemName()+" is not at password level 2 or 3. It is at level "+i+".");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Test connection to V5R2 (or higher) system at password level 0 or 1.
   *<ul compact>
   *<li>Security information should be exchanged.
   *<li>Password substitution should occur.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var006()
  {
    setVariation(6);
    try
    {
      if (systemObject_.getVRM() < AS400.generateVRM(5, 2, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not V5R2.");
        return;
      }
      try
      {
         Class fred = Class.forName("com.ibm.as400.access.SystemValue");
      }
      catch(Throwable e)
      {
         notApplicable("Could not load SystemValue class, probably running as proxy.");
         return;
      }
      SystemValue sv = new SystemValue(systemObject_, "QPWDLVL");
      String val = sv.getValue().toString().trim();
      int i = Integer.parseInt(val);
      if (i != 0 && i != 1)
      {
        notApplicable(systemObject_.getSystemName()+" is not at password level 0 or 1. It is at level "+i+".");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Test connection to V5R2 (or higher) system at password level 2 or 3.
   *<ul compact>
   *<li>Security information should be exchanged.
   *<li>Strong encryption of the password should occur.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var007()
  {
    setVariation(7);
    try
    {
      if (systemObject_.getVRM() < AS400.generateVRM(5, 2, 0))
      {
        notApplicable(systemObject_.getSystemName()+" is not V5R2.");
        return;
      }
      try
      {
         Class fred = Class.forName("com.ibm.as400.access.SystemValue");
      }
      catch(Throwable e)
      {
         notApplicable("Could not load SystemValue class, probably running as proxy.");
         return;
      }
      SystemValue sv = new SystemValue(systemObject_, "QPWDLVL");
      String val = sv.getValue().toString().trim();
      int i = Integer.parseInt(val);
      if (i != 2 && i != 3)
      {
        notApplicable(systemObject_.getSystemName()+" is not at password level 2 or 3. It is at level "+i+".");
        return;
      }
      systemObject_.connectService(AS400.RECORDACCESS);

      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

}


