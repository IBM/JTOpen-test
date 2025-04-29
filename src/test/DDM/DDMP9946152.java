///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9946152.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMP9946152. Verify the fix in P9946152. This is to make sure that:
 *<OL>
 *<LI>Test to make sure that if the default value for the RCDLEN parameter
 *    on the CRTSRCPF file command is changed on a system-wide basis that the
 *    Toolbox does not have problems (i.e. still uses the default of 92).
 *</OL>
**/
public class DDMP9946152 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP9946152";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9946152(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         String password,
                         AS400 pwrSys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9946152", 1,
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
   *Verify no exceptions are thrown with a changed RCDLEN on CRTSRCPF.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      if (pwrSys_ == null)
      {
        output_.println("pwrSys not specified.");
        return;
      }
      CommandCall cc = new CommandCall(pwrSys_);
      deleteLibrary(cc, testLib_); 
      cc.run("CRTLIB "+testLib_);

      // First make sure we can create a file normally.
      RecordFormat rf = new RecordFormat("MYRF");
      CharacterFieldDescription cfd = new CharacterFieldDescription(new AS400Text(1, 37, pwrSys_), "FLD1");
      rf.addFieldDescription(cfd);
      cfd = new CharacterFieldDescription(new AS400Text(10, 37, pwrSys_), "FLD2");
      rf.addFieldDescription(cfd);
      cfd = new CharacterFieldDescription(new AS400Text(100, 37, pwrSys_), "FLD3");
      rf.addFieldDescription(cfd);
      cfd = new CharacterFieldDescription(new AS400Text(1000, 37, pwrSys_), "FLD4");
      rf.addFieldDescription(cfd);

      SequentialFile sf = new SequentialFile(pwrSys_, "/QSYS.LIB/"+testLib_+".LIB/P9946152.FILE/TESTRLEN.MBR");
      sf.create(rf, "Testing RCDLEN change");
      sf.close();
      sf.delete();

      // Now change the default (we change it back in the finally block).
      boolean res = cc.run("CHGCMDDFT CMD(CRTSRCPF) NEWDFT('RCDLEN(112)')");
      if (!res)
      {
        AS400Message[] msgs = cc.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Unable to change default parameter. Please examine the messages and try again.");
        return;
      }
      pwrSys_.disconnectAllServices();
      pwrSys_.connectService(AS400.RECORDACCESS); // So the new job knows the new default...?
      pwrSys_.connectService(AS400.COMMAND);

      SequentialFile sf2 = new SequentialFile(pwrSys_, "/QSYS.LIB/"+testLib_+".LIB/P9946152B.FILE/TSTBRLEN.MBR");      
      sf2.create(rf, "Testing RCDLEN change with different parm");
      sf2.close();
      sf2.delete();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    finally
    {
      try
      {
        if (pwrSys_ == null) return;
        CommandCall cc = new CommandCall(pwrSys_);
        boolean res = cc.run("CHGCMDDFT CMD(CRTSRCPF) NEWDFT('RCDLEN(92)')");
        if (!res)
        {
          AS400Message[] msgs = cc.getMessageList();
          for (int i=0; i<msgs.length; ++i)
          {
            output_.println(msgs[i]);
          }
          output_.println("WARNING: Unable to change default length back.");
          output_.println("Please manually issue the command CHGCMDDFT CMD(CRTSRCPF) NEWDFT('RCDLEN(92)') on the system.");
        }
      }
      catch(Exception x)
      {
        x.printStackTrace(output_);
        output_.println("WARNING: Unable to change default length back.");
        output_.println("Please manually issue the command CHGCMDDFT CMD(CRTSRCPF) NEWDFT('RCDLEN(92)') on the system.");
      }
    }   
  }
}
