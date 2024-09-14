///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableThreadTestcase.
 Test usage of Toolbox converters in a multithreaded environment.
 **/
public class ConvTableThreadTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableThreadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    class CTThread implements Runnable
    {
        int ccsid_ = -1;

        boolean keepRunning_ = true;

        String expectedString_ = null;
        byte[] bytes_ = null;

        String error_ = null;

        long num_ = 0;

        public CTThread(int ccsid, String str, byte[] b)
        {
            ccsid_ = ccsid;
            expectedString_ = str;
            bytes_ = b;
        }

        public void run()
        {
            try
            {
                Thread thr = Thread.currentThread();
                while (keepRunning_)
                {
                    ConvTable t = ConvTable.getTable(ccsid_, null);
                    String s = t.byteArrayToString(bytes_, 0, bytes_.length, 0);
                    if (!s.equals(expectedString_))
                    {
                        keepRunning_ = false;
                        error_ = "Conversion failed on byteArrayToString for ccsid " + ccsid_;
                    }
                    byte[] b = t.stringToByteArray(s, 0);
                    for (int i = 0; i < b.length; ++i)
                    {
                        if (b[i] != bytes_[i])
                        {
                            keepRunning_ = false;
                            error_ = "Conversion failed on stringToByteArray for ccsid " + ccsid_;
                        }
                    }
                    ++num_;
                    Thread.yield(); // don't hog the processor.
                }
            }
            catch (Exception e)
            {
                keepRunning_ = false;
                error_ = "Exception occurred in thread for ccsid " + ccsid_ + ": " + e.getMessage();
            }
        }
    }

    /**
     Tests ConvTable.byteArrayToString() and ConvTable.stringToByteArray() with multiple threads.
     Variation should be successful.
     **/
    public void Var001()
    {
        CTThread[] ct = null;
        Thread[] t = null;

        try
        {
            output_.println("This variation will take approximately 120 seconds.");
            ct = new CTThread[11];
            int c=0;
            // ConvTableSingleMap
            ct[c++] = new CTThread(37, "ABCDEFG", new byte[] { (byte)0xC1, (byte)0xC2, (byte)0xC3, (byte)0xC4, (byte)0xC5, (byte)0xC6, (byte)0xC7 });
            ct[c++] = new CTThread(1140, "JKLMN", new byte[] { (byte)0xD1, (byte)0xD2, (byte)0xD3, (byte)0xD4, (byte)0xD5 });
            // ConvTableDoubleMap
            //      ct[c++] = new CTThread(16684, "ABCDEFG", new byte[] { (byte)0xC1, (byte)0xC2, (byte)0xC3, (byte)0xC4, (byte)0xC5, (byte)0xC6, (byte)0xC7 });
            ct[c++] = new CTThread(61952, "JKLMN", new byte[] { (byte)0x00, (byte)0x4A, (byte)0x00, (byte)0x4B, (byte)0x00, (byte)0x4C, (byte)0x00, (byte)0x4D, (byte)0x00, (byte)0x4E });
            // ConvTableMixedMap
            ct[c++] = new CTThread(5026, "ABCDEFG", new byte[] { (byte)0xC1, (byte)0xC2, (byte)0xC3, (byte)0xC4, (byte)0xC5, (byte)0xC6, (byte)0xC7 });
            ct[c++] = new CTThread(935, "JKLMN", new byte[] { (byte)0xD1, (byte)0xD2, (byte)0xD3, (byte)0xD4, (byte)0xD5 });
            // ConvTableAsciiMap
            ct[c++] = new CTThread(1252, "ABCDEFG", new byte[] { (byte)0x41, (byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47 });
            ct[c++] = new CTThread(437, "JKLMN", new byte[] { (byte)0x4A, (byte)0x4B, (byte)0x4C, (byte)0x4D, (byte)0x4E });
            // Unicode
            ct[c++] = new CTThread(1202, "ABCDEFG", new byte[] { (byte)0x41, (byte)0x00, (byte)0x42, (byte)0x00, (byte)0x43, (byte)0x00, (byte)0x44, (byte)0x00, (byte)0x45, (byte)0x00, (byte)0x46, (byte)0x00, (byte)0x47, (byte)0x00 });
            ct[c++] = new CTThread(13488, "JKLMN", new byte[] { (byte)0x00, (byte)0x4A, (byte)0x00, (byte)0x4B, (byte)0x00, (byte)0x4C, (byte)0x00, (byte)0x4D, (byte)0x00, (byte)0x4E });
            // ConvTableBidiMap
            //      ct[c++] = new CTThread(12708, "ABCDEFG", new byte[] { (byte)0xC1, (byte)0xC2, (byte)0xC3, (byte)0xC4, (byte)0xC5, (byte)0xC6, (byte)0xC7 });
            ct[c++] = new CTThread(420, "JKLMN", new byte[] { (byte)0xD1, (byte)0xD2, (byte)0xD3, (byte)0xD4, (byte)0xD5 });
            // ConvTableJavaMap
            ct[c++] = new CTThread(1208, "ABCDEFG", new byte[] { (byte)0x41, (byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47 });
            //      ct[c++] = new CTThread(950, "JKLMN", new byte[] { (byte)0xD1, (byte)0xD2, (byte)0xD3, (byte)0xD4, (byte)0xD5 });

            t = new Thread[ct.length];
            for (int i = 0; i < t.length; ++i)
            {
                t[i] = new Thread(ct[i]);
            }

            for (int i = 0; i < t.length; ++i)
            {
                t[i].start();
                Thread.sleep(20);
            }

            for (int i = 0; i < 12; ++i) // sleep for 2 minutes.
            {
                Thread.sleep(10000);
                for (int j = 0; j < ct.length; ++j)
                {
                    if (!ct[j].keepRunning_)
                    {
                        i=20;
                        j=14;
                    }
                }
            }

            for (int i = 0; i < ct.length; ++i)
            {
                ct[i].keepRunning_ = false;
            }

            for (int i = 0; i < t.length; ++i)
            {
                t[i].join();
            }

            output_.println("Conversion results:");

            long total = 0;
            for (int i = 0; i < ct.length; ++i)
            {
                output_.println(ct[i].num_ + " conversions occurred for ccsid " + ct[i].ccsid_);
                total += ct[i].num_;
            }
            output_.println(total + " conversions occurred for all threads.");

            String failMsg = "";
            for (int i = 0; i < ct.length; ++i)
            {
                if (ct[i].error_ != null)
                {
                    failMsg += "\n" + ct[i].error_;
                }
            }
            if (failMsg.length() == 0)
            {
                succeeded();
            }
            else
            {
                failed("Multithreaded conversion failure: " + failMsg);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
            try
            {
                for (int i = 0; i < ct.length; ++i)
                {
                    ct[i].keepRunning_ = false;
                }
                for (int i = 0; i < t.length; ++i)
                {
                    t[i].join();
                }
            }
            catch (Exception f)
            {
            }
        }
    }

    /**
     Tests ConvTable.byteArrayToString() and ConvTable.stringToByteArray() with multiple threads for several ConvTableJavaMap classes.
     Variation should be successful.
     **/
    public void Var002()
    {
        if (isApplet_)
        {
            notApplicable("Various CCSIDs not available in browser JVMs.");
            return;
        }

        CTThread[] ct = null;
        Thread[] t = null;

        try
        {
            output_.println("This variation will take approximately 120 seconds.");
            ct = new CTThread[4];
            int c = 0;
            // ConvTableSingleMap
            // ConvTableJavaMap
            ct[c++] = new CTThread(1208, "ABCDEFG", new byte[] { (byte)0x41, (byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47 });
            ct[c++] = new CTThread(1282, "JKLMN", new byte[] { (byte)0x4A, (byte)0x4B, (byte)0x4C, (byte)0x4D, (byte)0x4E });
            ct[c++] = new CTThread(913, "OPQR", new byte[] { (byte)0x4F, (byte)0x50, (byte)0x51, (byte)0x52 });
            ct[c++] = new CTThread(5054, "ZYX", new byte[] { (byte)0x5A, (byte)0x59, (byte)0x58 });

            t = new Thread[ct.length];
            for (int i = 0; i < t.length; ++i)
            {
                t[i] = new Thread(ct[i]);
            }

            for (int i = 0; i < t.length; ++i)
            {
                t[i].start();
                Thread.sleep(20);
            }

            for (int i = 0; i < 12; ++i) // sleep for 2 minutes.
            {
                Thread.sleep(10000);
                for (int j = 0; j < ct.length; ++j)
                {
                    if (!ct[j].keepRunning_)
                    {
                        i=20;
                        j=14;
                    }
                }
            }

            for (int i = 0; i < ct.length; ++i)
            {
                ct[i].keepRunning_ = false;
            }

            for (int i = 0; i < t.length; ++i)
            {
                t[i].join();
            }

            output_.println("Conversion results:");

            long total = 0;
            for (int i = 0; i < ct.length; ++i)
            {
                output_.println(ct[i].num_ + " conversions occurred for ccsid " + ct[i].ccsid_);
                total += ct[i].num_;
            }
            output_.println(total + " conversions occurred for all threads.");

            String failMsg = "";
            for (int i = 0; i < ct.length; ++i)
            {
                if (ct[i].error_ != null)
                {
                    failMsg += "\n" + ct[i].error_;
                }
            }
            if (failMsg.length() == 0)
            {
                succeeded();
            }
            else
            {
                failed("Multithreaded conversion failure: " + failMsg);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
            try
            {
                for (int i = 0; i < ct.length; ++i)
                {
                    ct[i].keepRunning_ = false;
                }
                for (int i = 0; i < t.length; ++i)
                {
                    t[i].join();
                }
            }
            catch (Exception f)
            {
            }
        }
    }
}
