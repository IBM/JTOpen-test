///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableBidiLengthTestcase.java
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
 Testcase ConvTableBidiLengthTestcase.
 Test variations for all ConvTableBidiMap converter tables.
 **/
public class ConvTableBidiLengthTestcase extends Testcase
{
    /**
     Runs the variations requested.
     Loops through all of the Bidi CCSIDs the Toolbox supports and tests their internal lengths.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.bidiCcsids_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.bidiCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i))
                {
                    setVariation(i);
                    try
                    {
                        // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.bidiCcsids_[i - 1]);
                        // ConvTableBidiMap m = (ConvTableBidiMap)c.newInstance();
                        ConvTableBidiMap m = (ConvTableBidiMap)ConvTable.getTable(
                            ConvTest.bidiCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                        checkSizes(m);
                    }
                    catch (Exception e)
                    {
                        failed(e, "Unexpected exception for Bidi CCSID " + ConvTest.bidiCcsids_[i - 1] + ".");
                    }
                }
            }
        }
    }

    /**
     Helper method used to verify the sizes for each variation.
     For a single-byte ccsid, the ebcdic to unicode table should be 256 unicode characters
     and the unicode to ebcdic table should be 64K ebcdic bytes.
     **/
    public void checkSizes(ConvTableBidiMap c)
    {
        if (c.toUnicode_.length != 256)
            failed("EBCDIC-->Unicode translation table incorrect size: " + c.toUnicode_.length);
        else if (c.fromUnicode_.length != 65536)
            failed("Unicode-->EBCDIC translation table incorrect size: " + c.fromUnicode_.length);
        else
            succeeded();
    }
}
