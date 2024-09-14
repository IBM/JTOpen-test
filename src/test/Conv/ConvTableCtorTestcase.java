///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import test.ConvTest;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableCtorTestcase.
 Test variations for all ConvTableXXX classes where XXX is a ccsid.
 **/
public class ConvTableCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    boolean isProxified_ = false;

    /**
     Runs the variations requested.
     Loops through all of the CCSIDs the Toolbox supports and tests their constructors.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.allCcsids_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (systemObject_.getProxyServer().length() > 0)
        {
            isProxified_ = true;
        }

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.allCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i))
                {
                    setVariation(i);
                    if (isProxified_)
                    {
                        notApplicable("Running proxified.");
                    }
                    else
                    {
                        try
                        {
                            // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.allCcsids_[i - 1]);
                            // ConvTable m = (ConvTable)c.newInstance();
                            ConvTable m = ConvTable.getTable(
                                ConvTest.allCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 
                            succeeded();
                        }
                        catch (Exception e)
                        {
                            failed(e, "Unexpected exception for CCSID " + ConvTest.allCcsids_[i - 1] + ".");
                        }
                    }
                }
            }
        }
    }
}
