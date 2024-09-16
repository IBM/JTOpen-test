///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalReleaseTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemValue;

import test.Testcase;

import com.ibm.as400.access.RequestNotSupportedException;

/**
 * Testcase SysvalReleaseTestcase.
 *
 * Test variations for system values that are not supported at various
 * release levels.
 **/
public class SysvalReleaseTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SysvalReleaseTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SysvalTestDriver.main(newArgs); 
   }

  /**
   * Runs the variations requested.
   **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

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

  }

  /**
   * Actually does the test.
  **/
  private void test(String str, int vrm)
  {
    boolean expectException = false;
    try
    {
      expectException = systemObject_.getVRM() < vrm;
      SystemValue sv = new SystemValue(systemObject_, str);
      sv.getValue();
      if (expectException)
        failed("No exception.");
      else
        succeeded();
    }
    catch (Exception e)
    {
      if (expectException && exceptionStartsWith(e, "RequestNotSupportedException", str,
                                                 RequestNotSupportedException.SYSTEM_LEVEL_NOT_CORRECT))
        succeeded();
      else
        failed(e, "Wrong exception info.");
    }
  }


  /**
   * Test V4R3 system values to a pre-V4R3 system.
   * QCHRIDCTL
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var001()
  {
    test("QCHRIDCTL", AS400.generateVRM(4,3,0));
  }


  /**
   * Test V4R3 system values to a pre-V4R3 system.
   * QDYNPTYADJ
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var002()
  {
    test("QDYNPTYADJ", AS400.generateVRM(4,3,0));
  }


  /**
   * Test V4R3 system values to a pre-V4R3 system.
   * QIGCFNTSIZ
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var003()
  {
    test("QIGCFNTSIZ", AS400.generateVRM(4,3,0));
  }


  /**
   * Test V4R3 system values to a pre-V4R3 system.
   * QPRCMLTTSK
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var004()
  {
    test("QPRCMLTTSK", AS400.generateVRM(4,3,0));
  }


  /**
   * Test V4R3 system values to a pre-V4R3 system.
   * QPRCFEAT
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var005()
  {
    test("QPRCFEAT", AS400.generateVRM(4,3,0));
  }


  /**
   * Test V4R4 system values to a pre-V4R4 system.
   * QCFGMSGQ
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var006()
  {
    test("QCFGMSGQ", AS400.generateVRM(4,3,0));
  }


  /**
   * Test V4R4 system values to a pre-V4R4 system.
   * QMLTTHDACN
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var007()
  {
    test("QMLTTHDACN", AS400.generateVRM(4,4,0));
  }


  /**
   * Test V4R4 system values to a pre-V4R4 system.
   * ALWADDCLU
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var008()
  {
    test("ALWADDCLU", AS400.generateVRM(4,4,0));
  }


  /**
   * Test V4R4 system values to a pre-V4R4 system.
   * MDMCNTRYID
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var009()
  {
    test("MDMCNTRYID", AS400.generateVRM(4,4,0));
  }



// There are no new V4R5 system values.




  /**
   * Test V5R1 system values to a pre-V5R1 system.
   * QMAXJOB
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var010()
  {
    test("QMAXJOB", AS400.generateVRM(5,1,0));
  }


  /**
   * Test V5R1 system values to a pre-V5R1 system.
   * QMAXSPLF
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var011()
  {
    test("QMAXSPLF", AS400.generateVRM(5,1,0));
  }


  /**
   * Test V5R1 system values to a pre-V5R1 system.
   * QVFYOBJRST
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var012()
  {
    test("QVFYOBJRST", AS400.generateVRM(5,1,0));
  }


  /**
   * Test V5R1 system values to a pre-V5R1 system.
   * QSHRMEMCTL
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var013()
  {
    test("QSHRMEMCTL", AS400.generateVRM(5,1,0));
  }


  /**
   * Test V5R1 system values to a pre-V5R1 system.
   * QLIBLCKLVL
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var014()
  {
    test("QLIBLCKLVL", AS400.generateVRM(5,1,0));
  }


  /**
   * Test V5R1 system values to a pre-V5R1 system.
   * QPWDLVL
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var015()
  {
    test("QPWDLVL", AS400.generateVRM(5,1,0));
  }

  /**
   * Test V5R2 system values to a pre-V5R2 system.
   * QDBFSTCCOL
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var016()
  {
    test("QDBFSTCCOL", AS400.generateVRM(5,2,0));
  }

  /**
   * Test V5R2 system values to a pre-V5R2 system.
   * QSPLFACN
   * The method should throw a RequestNotSupportedException.
   **/
  public void Var017()
  {
    test("QSPLFACN", AS400.generateVRM(5,2,0));
  }
}
