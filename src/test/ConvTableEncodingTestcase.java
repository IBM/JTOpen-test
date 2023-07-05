///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableEncodingTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.text.Collator;
import java.util.Locale;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableEncodingTestcase.
 Test loading of all of Java's supported encodings against Toolbox internal tables.
 **/
public class ConvTableEncodingTestcase extends Testcase
{
    static Locale[] locales_;
    static
    {
        System.out.print("Loading locales...");
        locales_ = Collator.getAvailableLocales();
        System.out.println("Done.");
    }

    boolean isProxified_ = false;

    /**
     Runs the variations requested.
     Loops through all of the JVM locales and tests their default encodings. All "best guess" CCSIDs should be in the Toolbox.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = locales_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        isProxified_ = systemObject_.getProxyServer().length() > 0;

        Locale original = Locale.getDefault();

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= locales_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i))
                {
                    setVariation(i);
                    if (isApplet_)
                    {
                        notApplicable("Cannot set Locale inside a browser.");
                    }
                    else if (isProxified_)
                    {
                        notApplicable("Running proxified.");
                    }
                    else
                    {
                        Converter t = null;
                        String encoding = null;
                        try
                        {
                            Locale.setDefault(locales_[i - 1]);
                            t = new Converter();
                            encoding = t.getEncoding();
                            byte[] b = t.stringToByteArray("ABCDEFG");
                            String s = t.byteArrayToString(b, 0, b.length);
                            if (s.equals("ABCDEFG"))
                            {
                                ConverterImplRemote impl = (ConverterImplRemote)t.impl;
                                ConvTable ct = impl.table_;
                                if (ct instanceof ConvTableJavaMap)
                                {
                                    failed("Wrong table instance loaded for locale/encoding " + locales_[i - 1] + "/" + encoding + ": " + ct);
                                }
                                else
                                {
                                    succeeded();
                                }
                            }
                            else
                            {
                                failed("Roundtrip failed for locale/encoding " + locales_[i - 1] + "/" + encoding + ": " + s + " != ABCDEFG");
                            }
                        }
                        catch (Exception e)
                        {
                            failed(e, "Unexpected exception for locale/encoding " + locales_[i - 1] + "/" + encoding + ".");
                        }
                        Locale.setDefault(original);
                    }
                }
            }
        }
    }
}
