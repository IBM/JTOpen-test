///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableAsciiRoundTripTestcase.java
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
 Testcase ConvTableAsciiRoundTripTestcase.
 Test internal character conversion for all ConvTableAsciiMap converter tables.
 **/
public class ConvTableAsciiRoundTripTestcase extends Testcase
{
    /**
     Runs the variations requested.
     Loops through all of the ASCII CCSIDs the Toolbox supports and tests their contents
     by roundtripping the characters in both tables.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.asciiCcsids_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.asciiCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i))
                {
                    setVariation(i);
                    try
                    {
                        // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.asciiCcsids_[i - 1]);
                        // ConvTableAsciiMap m = (ConvTableAsciiMap)c.newInstance();
                        ConvTableAsciiMap m = (ConvTableAsciiMap)ConvTable.getTable(
                            ConvTest.asciiCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                        roundTrip(m);
                    }
                    catch (Exception e)
                    {
                        failed(e, "Unexpected exception for ASCII CCSID " + ConvTest.asciiCcsids_[i - 1] + ".");
                    }
                }
            }
        }
    }

    /**
     Helper method used to compare the original string with the roundtripped string.
     **/
    public void roundTrip(ConvTableAsciiMap c) throws Exception
    {
        int ccsid = c.getCcsid();

        String s1 = new String(c.toUnicode_);
        byte[] b1 = c.stringToByteArray(s1, 0);
        String s2 = c.byteArrayToString(b1, 0, b1.length, 0);
        byte[] b2 = c.stringToByteArray(s2, 0);

        if (!s1.equals(s2))
        {
            failed("Roundtrip (string) strings not equal for ccsid " + ccsid + ".");
            return;
        }
        if (b1.length != b2.length)
        {
            failed("Roundtrip (string) byte arrays not equal length for ccsid " + ccsid + ".");
            return;
        }
        for (int i = 0; i < b1.length; ++i)
        {
            if (b1[i] != b2[i])
            {
                failed("Roundtrip (string) byte arrays not equal at position " + i + " for ccsid " + ccsid + ".");
                return;
            }
        }

        b1 = c.fromUnicode_;
        s1 = c.byteArrayToString(b1, 0, b1.length, 0);
        b2 = c.stringToByteArray(s1, 0);
        s2 = c.byteArrayToString(b2, 0, b2.length, 0);

        if (b1.length != b2.length)
        {
            failed("Roundtrip (bytes) byte arrays not equal length for ccsid " + ccsid + ".");
            return;
        }
        for (int i = 0; i < b1.length; ++i)
        {
            if (b1[i] != b2[i])
            {
                failed("Roundtrip (bytes) byte arrays not equal at position " + i + " for ccsid " + ccsid + ".");
                return;
            }
        }
        if (!s1.equals(s2))
        {
            failed("Roundtrip (bytes) strings not equal for ccsid " + ccsid + ".");
            return;
        }

        succeeded();
    }
}
