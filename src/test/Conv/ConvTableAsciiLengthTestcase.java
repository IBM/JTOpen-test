///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableAsciiLengthTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import com.ibm.as400.access.AS400ImplRemote;
import com.ibm.as400.access.ConvTable;
import com.ibm.as400.access.ConvTableAsciiMap;

import test.ConvTest;
import test.Testcase;
/**
 Testcase ConvTableAsciiLengthTestcase.
 Test variations for all ConvTableAsciiMap converter tables.
 **/
public class ConvTableAsciiLengthTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableAsciiLengthTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Runs the variations requested.
     Loops through all of the ASCII CCSIDs the Toolbox supports and tests their internal lengths.
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

                        checkSizes(m);
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
     Helper method used to verify the sizes for each variation.
     For a single-byte ccsid, the ebcdic to unicode table should be 256 unicode characters
     and the unicode to ebcdic table should be 64K ebcdic bytes.
     **/
    public void checkSizes(ConvTableAsciiMap c)
    {
        if (c.toUnicode_.length != 256)
            failed("EBCDIC-->Unicode translation table incorrect size: " + c.toUnicode_.length);
        else if (c.fromUnicode_.length != 65536)
            failed("Unicode-->EBCDIC translation table incorrect size: " + c.fromUnicode_.length);
        else
            succeeded();
    }
}
