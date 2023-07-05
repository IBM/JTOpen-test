///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableDoubleLengthTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import test.ConvTest;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableDoubleLengthTestcase.
 Test variations for all ConvTableSingleMap converter tables.
 **/
public class ConvTableDoubleLengthTestcase extends Testcase
{
    boolean isProxified_ = false;

    /**
     Runs the variations requested.
     Loops through all of the double-byte EBCDIC CCSIDs the Toolbox supports and tests their internal lengths.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.doubleCcsids_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (systemObject_.getProxyServer().length() > 0)
        {
            isProxified_ = true;
        }

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.doubleCcsids_.length; ++i)
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
                            // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.doubleCcsids_[i - 1]);
                            // ConvTableDoubleMap m = (ConvTableDoubleMap)c.newInstance();
                            ConvTableDoubleMap m = (ConvTableDoubleMap)ConvTable.getTable(
                                ConvTest.doubleCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                            checkSizes(m);
                        }
                        catch (Exception e)
                        {
                            failed(e, "Unexpected exception for EBCDIC CCSID " + ConvTest.doubleCcsids_[i - 1] + ".");
                        }
                    }
                }
            }
        }
    }

    /**
     Helper method used to verify the sizes for each variation.
     For a double-byte ccsid, the ebcdic to unicode table should be 64K unicode characters
     and the unicode to ebcdic table should be 64K ebcdic characters.
     **/
    public void checkSizes(ConvTableDoubleMap c)
    {
        if (c.getToUnicode().length != 65536)
            failed("EBCDIC-->Unicode translation table incorrect size: " + c.getToUnicode().length);
        else if (c.getFromUnicode().length != 65536)
            failed("Unicode-->EBCDIC translation table incorrect size: " + c.getFromUnicode().length);
        else
            succeeded();
    }
}
