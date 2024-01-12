///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableBidiRoundTripTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.util.ResourceBundle;
import test.ConvTest;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
  Testcase ConvTableBidiRoundTripTestcase.
  Test internal character conversion for all ConvTableBidiMap converter tables.
**/
public class ConvTableBidiRoundTripTestcase extends Testcase
{
   private static ResourceBundle resources_ = ResourceBundle.getBundle("test.TestMRI_he_HE");
   private static ResourceBundle resources2_ = ResourceBundle.getBundle("test.TestMRI_ar_AR");

  /**
    Runs the variations requested.
    Loops through all of the Bidi CCSIDs the Toolbox supports and tests their contents
    by roundtripping the provided resource MRI strings.
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
          int ccsid = ConvTest.bidiCcsids_[i - 1];
          try
          {
            // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ccsid);
            // ConvTableBidiMap m = (ConvTableBidiMap)c.newInstance();
            ConvTableBidiMap m = (ConvTableBidiMap)ConvTable.getTable(
                ConvTest.bidiCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 


            if (ccsid == 916 || ccsid == 5351 || ccsid == 1255 || ccsid == 862 || ccsid == 62245 ||
                ccsid == 62211 || ccsid == 62235 || ccsid == 424)
            {
               roundTrip(m, resources_.getString("UNI_STRING_" + ccsid));
            }
            else
            {
               roundTrip(m, resources2_.getString("UNI_STRING_" + ccsid));
            }
          }
          catch (Exception e)
          {
            failed(e, "Unexpected exception for Bidi CCSID " + ccsid + ".");
          }
        }
      }
    }
  }

  /**
    Helper method used to compare the original string with the roundtripped string.
  **/
  public void roundTrip(ConvTableBidiMap c, String unicodeString) throws Exception
  {
    int ccsid = c.getCcsid();

    String s1 = new String(unicodeString);
    byte[] b1 = c.stringToByteArray(s1, AS400BidiTransform.getStringType((char)ccsid));
    String s2 = c.byteArrayToString(b1, 0, b1.length, AS400BidiTransform.getStringType((char)ccsid));
    byte[] b2 = c.stringToByteArray(s2, AS400BidiTransform.getStringType((char)ccsid));

    if (Trace.isTraceConversionOn())
    {
       for (int i = 0; i < s1.length(); ++i)
       {
          if ((s1.charAt(i)) == ((s2.charAt(i))))
          {
          }
          else
          {
             System.out.println("FAILED: " + i + " S1: " + (int)s1.charAt(i) + " S2: " + (int)s2.charAt(i));
          }
       }
    }
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

    s1 = c.byteArrayToString(b1, 0, b1.length, AS400BidiTransform.getStringType((char)ccsid));
    b2 = c.stringToByteArray(s1, AS400BidiTransform.getStringType((char)ccsid));
    s2 = c.byteArrayToString(b2, 0, b2.length, AS400BidiTransform.getStringType((char)ccsid));

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
