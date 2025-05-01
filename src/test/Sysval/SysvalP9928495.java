///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalP9928495.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;


import java.math.BigDecimal;

import com.ibm.as400.access.SystemValue;

import test.Testcase;


/**
 * Testcase SysvalP9928495.
 *
 * Tests fix for JACL PTR 9928495.
 * <OL>
 * <LI>getValue() for QSTGLOWLMT fails when QSTGLOWLMT is set to 0.
 * </OL>
 **/
public class SysvalP9928495 extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SysvalP9928495";
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
    }


    /**
     * Verify QSTGLOWLMT.getValue() returns when the value is 0.
     * The variation should be successful.
     **/
    public void Var001()
    {
      String oldValue = null;
      SystemValue sv = null;
      try
      {
        sv = new SystemValue(pwrSys_, "QSTGLOWLMT");
        oldValue = sv.getValue().toString();
        sv.clear();
        
        try
        {
          sv.setValue((new BigDecimal(0.0000)).setScale(4, BigDecimal.ROUND_HALF_UP));
          sv.clear();
          String newValue = sv.getValue().toString();
          if (newValue.equals("0.0000"))
          {
            succeeded();
          }
          else
          {
            failed("Value not set to 0.");
          }
        }
        catch(Exception e1)
        {
          failed(e1, "Unexpected exception.");
        }
      }
      catch(Exception e2)
      {
        failed(e2, "Unable to retrieve initial value for QSTGLOWLMT.");
      }
      finally
      {
        if (oldValue != null)
        {
          try
          {
            sv.setValue((new BigDecimal(oldValue.toString())).setScale(4, BigDecimal.ROUND_HALF_UP));
          }
          catch(Exception e3)
          {
            output_.println("Warning: Unable to reset QSTGLOWLMT to "+oldValue);
            e3.printStackTrace(output_);
          }
        }
      }
    }
}
