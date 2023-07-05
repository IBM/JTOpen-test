///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP3696575.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.*;

/**
 *Testcase DDMP3696575.  Verify fix for P3696575.
**/
public class DDMP3696575 extends Testcase
{
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP3696575(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         AS400 pwrSys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP3696575", 1,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
    pwrSys_ = pwrSys;
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);
    if (!setUp())
    {
      output_.println("Unable to complete setup; variations not run.");
      return;
    }
    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    systemObject_.disconnectAllServices();
    cleanUp();
  }

  boolean setUp()
  {
    try
    {
      CommandCall cc = new CommandCall(pwrSys_);
      deleteLibrary(cc, testLib_);
      cc = new CommandCall(systemObject_);
      boolean res = cc.run("CRTLIB "+testLib_);
      if (!res)
      {
        output_.println("Could not create test library:");
        AS400Message[] msgs = cc.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i].toString());
        }
      }
      return res;
    }
    catch(Exception e)
    {
      output_.println("Unexepected exception during setup.");
      e.printStackTrace(output_);
      return false;
    }
  }

  void cleanUp()
  {
    try
    {
      CommandCall cc = new CommandCall(pwrSys_);
      deleteLibrary(cc,testLib_); 
    }
    catch(Exception e)
    {
      output_.println("Unexpected exception during cleanup.");
      e.printStackTrace(output_);
    }
  }


  /**
   *Verify AS400FileRecordDescription.retrieveRecordFormat().
   *Expected results:
   *<ul compact>
   *<li>retrieveRecordFormat() will complete successfully for a file with DBCS fields.
   *This variation specifically targets applets or other JVM environments that do not include
   *the proper converter tables.
   *</ul>
  **/
  public void Var001()
  {
    SequentialFile f = null;
    try
    {
        systemObject_.connectService(AS400.RECORDACCESS);
        f = new SequentialFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/DBCSTEST.FILE/MBR1.MBR");
        RecordFormat rf = new RecordFormat("RF");
        AS400Text text = new AS400Text(20, systemObject_.getCcsid(), systemObject_);
        
        // These are the field descriptions that use an AS400Text object
        // underneath when they are retrieved.
        rf.addFieldDescription(new CharacterFieldDescription(text, "F1"));
        rf.addFieldDescription(new DBCSEitherFieldDescription(text, "F2"));
        rf.addFieldDescription(new DBCSOpenFieldDescription(text, "F3"));
        rf.addFieldDescription(new DBCSGraphicFieldDescription(text, "F4"));
        rf.addFieldDescription(new DBCSOnlyFieldDescription(text, "F5"));
        rf.addFieldDescription(new DateFieldDescription(text, "F6"));
        rf.addFieldDescription(new TimeFieldDescription(text, "F7"));
        rf.addFieldDescription(new TimestampFieldDescription(text, "F8"));
        
        // Cleanup file if it already exists
        try
        {
          f.delete();
        }
        catch (Exception exc) {}
        
        // Create file with DBCS field descriptions and other ones that
        // use AS400Text objects for conversion.
        f.create(rf, "Testing JACL PTR 3696575");
        f.close();
        
        AS400FileRecordDescription frd = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/DBCSTEST.FILE");
        RecordFormat rfs[] = frd.retrieveRecordFormat();
    
        // If we were able to retrieve the description, then it worked.
        succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    try
    {
      f.delete();
    }
    catch(Exception x){}
  }

}
