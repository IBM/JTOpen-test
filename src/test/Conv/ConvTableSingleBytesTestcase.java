///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableSingleBytesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import test.ConvTest;
import test.NLSTableDownload;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableSingleBytesTestcase.
 Test internal converter table contents for all ConvTableSingleMap converter tables.
 **/
public class ConvTableSingleBytesTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableSingleBytesTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    boolean isProxified_ = false;

    /**
     Runs the variations requested.
     Loops through all of the single-byte EBCDIC CCSIDs the Toolbox supports and tests their contents against the same tables downloaded from the server.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.singleCcsids_.length;

        try
        {
            setup();
        }
        catch (Exception e)
        {
            System.out.println("Testcase setup error: " + e.getMessage());
            e.printStackTrace(System.out);
            if (endIfSetupFails_ == true)
            {
                return;
            }
        }

        boolean allVariations = (variationsToRun_.size() == 0);

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.singleCcsids_.length; ++i)
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
                            // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.singleCcsids_[i - 1]);
                            // ConvTableSingleMap m = (ConvTableSingleMap)c.newInstance();
                            ConvTableSingleMap m = (ConvTableSingleMap)ConvTable.getTable(
                                ConvTest.singleCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                            compareTables(m);
                        }
                        catch (Exception e)
                        {
                            failed(e, "Unexpected exception for EBCDIC CCSID " + ConvTest.singleCcsids_[i - 1] + ".");
                        }
                    }
                }
            }
        }
    }

    /**
     Connects to the server.
     **/
    protected void setup() throws Exception
    {
        systemObject_.connectService(AS400.CENTRAL);
        if (systemObject_.getProxyServer().length() > 0) isProxified_ = true;
    }

    /**
     Helper method used to download the unicode converter table from the server.
     **/
    public char[] downloadSingleByteToUnicodeTable(int ccsid) throws Exception
    {
        AS400ImplRemote impl = (AS400ImplRemote)systemObject_.getImpl();

        NLSTableDownload down = new NLSTableDownload(impl);
        down.connect();

        char[] table = down.download(ccsid, 13488, false);

        down.disconnect();

        return table;
    }

    /**
     Helper method used to download the ebcdic converter table from the server.
     **/
    public char[] downloadSingleByteFromUnicodeTable(int ccsid) throws Exception
    {
        AS400ImplRemote impl = (AS400ImplRemote)systemObject_.getImpl();

        NLSTableDownload down = new NLSTableDownload(impl);
        down.connect();

        char[] table = down.download(13488, ccsid, true);

        down.disconnect();

        return table;
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
     Helper method used to compare the established and generated converter tables.
     **/
    public void compareTables(ConvTableSingleMap c) throws Exception
    {
        int ccsid = c.getCcsid();
        // First test the toUnicode table.
        char[] established = c.toUnicode_;
        char[] generated = downloadSingleByteToUnicodeTable(ccsid);
        if (established.length != generated.length)
        {
            failed("EBCDIC->Unicode table length does not match OS/400's for ccsid " + ccsid + ": " + established.length + " != " + generated.length);
            return;
        }
        StringBuffer charFailures = new StringBuffer();
        for (int i = 0; i < established.length; ++i)
        {
            int est = (int)(established[i] & 0xFFFF);
            int gen = (int)(generated[i] & 0xFFFF);
            if (est != gen)
            {
                if(!(i == 0xBC) && !(systemObject_.getVRM() < 0x00060100) && !(est == 0x00AF) && !(gen == 0x203E))  //@A1A
                    charFailures.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
            }
        }
        if (charFailures.length() > 0)
        {
            failed("EBCDIC->Unicode table characters do not match OS/400's for ccsid " + ccsid + ":\n" + charFailures.toString());
            return;
        }

        // Now test the fromUnicode table.
        byte[] established_b = c.fromUnicode_;
        generated = downloadSingleByteFromUnicodeTable(ccsid);
        byte[] generated_b = new byte[generated.length * 2]; // The chars are doubled-up bytes.
        for (int i = 0; i < generated.length; ++i)
        {
            int ch = (int)(generated[i] & 0xFFFF);
            generated_b[i * 2] = (byte)(ch >>> 8);
            generated_b[i * 2 + 1] = (byte)(ch & 0x00FF);
        }
        if (established_b.length != generated_b.length)
        {
            failed("Unicode->EBCDIC table length does not match OS/400's for ccsid " + ccsid + ": " + established_b.length + " != " + generated_b.length);
            return;
        }
        charFailures = new StringBuffer();
        for (int i=0; i<established_b.length; ++i)
        {
            int est = (int)(established_b[i] & 0xFFFF);
            int gen = (int)(generated_b[i] & 0xFFFF);
            if (est != gen)
            {
                if(!(systemObject_.getVRM() < 0x00060100) && !(i == 0x00AF) && !(est==0xFFBC) && !(gen==0x003F))    //@A1A
                    charFailures.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
            }
        }
        if (charFailures.length() > 0)
        {
            failed("Unicode->EBCDIC table characters do not match OS/400's for ccsid " + ccsid + ":\n" + charFailures.toString());
            return;
        }
        succeeded();
    }
}
