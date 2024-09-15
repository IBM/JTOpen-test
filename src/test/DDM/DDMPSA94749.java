///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMPSA94749.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 *Testcase DDMPSA94749. Verify the fix in PSA94749. This is to make sure that:
 *<OL>
 *<LI>Test to make sure that reading the contents of a variable-length field
 *    does not cause a NullPointerException.
 *</OL>
**/
public class DDMPSA94749 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMPSA94749";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMPSA94749(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         String password,
                         AS400 pwrSys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMPSA94749", 1,
          variationsToRun, runMode, fileOutputStream, password);
    testLib_ = testLib;
    pwrSys_ = pwrSys;
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
    systemObject_.disconnectAllServices();
  }


  /**
   *Verify no exceptions are thrown for getField().
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
        // Setup... end commitment control
    try
    {
      AS400File.endCommitmentControl(pwrSys_);
    }
    catch(Exception e)
    {
      output_.println("Warning: Unable to end commitment control: "+e.toString());
    }
    try
    {
      AS400Text t = new AS400Text(32700, systemObject_.getCcsid(), systemObject_);
      CharacterFieldDescription cfd = new CharacterFieldDescription(t, "fld1");
      cfd.setVARLEN(20);
      RecordFormat rf = new RecordFormat("MYRF");
      rf.addFieldDescription(cfd);

      SequentialFile sf = new SequentialFile(systemObject_, "/qsys.lib/qtemp.lib/test.file/myrf.mbr");
      sf.create(rf, "Testing NullPointerException");
      sf.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      sf.write(rf.getNewRecord());
      sf.close();

      Record r = sf.readAll()[0];

      try
      {
        r.getField(0);
        succeeded();
      }
      catch(NullPointerException npe)
      {
        failed("NullPointerException still occurred.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
}
