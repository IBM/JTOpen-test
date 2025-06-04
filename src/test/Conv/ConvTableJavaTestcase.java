///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableJavaTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableJavaTestcase.
 Test internal character conversion for ConvTableJavaMap converter tables.
 **/
public class ConvTableJavaTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableJavaTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Runs the variations requested.
     Loops through all of the double-byte EBCDIC CCSIDs the Toolbox supports and tests their contents
     by roundtripping the characters in both tables.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConversionMaps.encodingCcsid_.size();
        boolean allVariations = (variationsToRun_.size() == 0);

        if (runMode_ != ATTENDED)
        {
            int i = 1;
            for (@SuppressWarnings("unchecked")
            Enumeration<String> keys = ConversionMaps.encodingCcsid_.keys(); keys.hasMoreElements(); ++i)
            {
                String encoding = (String)keys.nextElement();
                if (allVariations || variationsToRun_.contains("" + i))
                {
                    setVariation(i);
                    if (encoding.equals("JIS0212") || encoding.equals("JIS0208"))
                    {
                        notApplicable("DBCS-only encoding: " + encoding); // the Java encoding is DBCS-only.
                    }
                    else
                    {
                        try
                        {
                            ConvTable t = ConvTable.getTable(encoding);
                            byte[] b = t.stringToByteArray("ABCDEFG", 0);
                            String s = t.byteArrayToString(b, 0, b.length, 0);
                            if (s.equals("ABCDEFG"))
                            {
                                succeeded();
                            }
                            else
                            {
                                failed("Roundtrip failed for encoding " + encoding + ": " + s + " != ABCDEFG");
                            }
                        }
                        catch (UnsupportedEncodingException uee)
                        {
                                failed(uee, "Unexpected exception for encoding " + encoding + ".");
                        }
                        catch(Exception e)
                        {
                            failed(e, "Unexpected exception for encoding " + encoding + ".");
                        }
                    }
                }
            }
        }
    }
}
