///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalP9907639.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;


import java.util.Calendar;

import com.ibm.as400.access.SystemValue;

import test.Testcase;


/**
 * Testcase SysvalP9907639.
 *
 * Tests fixes for JACL PTR 9907639.
 * <OL>
 * <LI>getValue() for QTIME returns hours that are off by 12.
 * </OL>
 **/
public class SysvalP9907639 extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SysvalP9907639";
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
     * Verify QTIME.getValue() returns the correct hours.
     * The variation should be successful.
     **/
    public void Var001()
    {
      try
      {
        SystemValue sv = new SystemValue(systemObject_, "QTIME");
        String t1 = sv.getValue().toString();
        sv.clear();
        String t2 = sv.getValue().toString();

        String h1 = t1.substring(0, t1.indexOf(":"));
        String h2 = t2.substring(0, t2.indexOf(":"));

        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY); // Assuming PC clock is the same as AS/400 clock
        
        if (!h1.equals(h2))
        {
          failed("Local hours not equal. Was this variation run at the top of the hour? ("+h1+" != "+h2+")");
        }
        else
        {
          int x = (new Integer(h1.trim())).intValue();
          if (x != hours)
          {
            failed("Hours not equal. AS/400 says "+x+" and Java says "+hours+". Is the client time the same as the AS/400 time? Was this variation run at the top of the hour?");
          }
          else
          {
            succeeded();
          }
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }
}
