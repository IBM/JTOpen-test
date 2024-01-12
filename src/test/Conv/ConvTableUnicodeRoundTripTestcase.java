///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableUnicodeRoundTripTestcase.java
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
 Testcase ConvTableUnicodeRoundTripTestcase.
 Test internal character conversion for the ConvTable13488 converter table.
 **/
public class ConvTableUnicodeRoundTripTestcase extends Testcase
{
    /**
     Runs the variations requested.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.unicodeCcsids_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.unicodeCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i))
                {
                    setVariation(i);
                    try
                    {
                        // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.unicodeCcsids_[i - 1]);
                        // ConvTable m = (ConvTable)c.newInstance();
                        ConvTable m = (ConvTable)ConvTable.getTable(
                            ConvTest.unicodeCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                        roundTrip(m);
                    }
                    catch (Exception e)
                    {
                        failed(e, "Unexpected exception for ASCII CCSID " + ConvTest.unicodeCcsids_[i - 1] + ".");
                    }
                }
            }
        }
    }

    /**
     Helper method used to print an integer as a hex string.
     **/
    public static String hex(int x)
    {
        StringBuffer h = new StringBuffer(Integer.toHexString(x).toUpperCase());
        while (h.length() < 4) h.insert(0, '0');
        h.insert(0, "0x");
        return h.toString();
    }

    /**
     Helper method used to compare the original string with the roundtripped string.
     **/
    public void roundTrip(ConvTable c) throws Exception
    {
        int ccsid = c.getCcsid();

        StringBuffer b = new StringBuffer();
        for (int i = 0x0000; i < 0xFFFF; ++i)
        {
            b.append((char)i);
        }
        String s1 = b.toString();
        byte[] b1 = c.stringToByteArray(s1, 0);
        String s2 = c.byteArrayToString(b1, 0, b1.length, 0);
        byte[] b2 = c.stringToByteArray(s2, 0);

        if (!s1.equals(s2))
        {
            StringBuffer buf = new StringBuffer("Roundtrip (string) strings not equal for ccsid " + ccsid + ":\n");
            int min = s1.length() < s2.length() ? s1.length() : s2.length();
            buf.append("index: { original, roundtripped }\n");
            for (int i = 0; i < min; ++i)
            {
                if (s1.charAt(i) != s2.charAt(i))
                {
                    buf.append(hex(i) + ":{'" + hex((int)s1.charAt(i)) + "','" + hex((int)s2.charAt(i)) + "'}\n");
                }
            }
            failed(buf.toString());
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

        // First need to get the DB char[] into a byte[].
        char[] c1 = b.toString().toCharArray();
        b1 = new byte[c1.length * 2];
        for (int i = 0; i < c1.length; ++i)
        {
            b1[i * 2] = (byte)(c1[i] >>> 8);
            b1[i * 2 + 1] = (byte)(c1[i] & 0x00FF);
        }
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
