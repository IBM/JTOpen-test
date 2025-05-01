///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvP9953884.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase ConvP9953884.  Verify fix for P9953884.  This is to make sure that:
 <ol>
 <li>Converting bad data using one of the Java maps does not ruin the
 underlying converter for all subsequent conversions.
 </ol>
 **/
public class ConvP9953884 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvP9953884";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        systemObject_.disconnectAllServices();
    }

    /**
     Verify CharConverter.byteArrayToString does not mess up the converter
     when "bad" data is passed in.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var001()
    {
        try
        {
            // Test data provided by Scott Bailey.
            // This data is EBCDIC and will cause a Java conversion error when converting using UTF8
            byte[] badData = { (byte)0xC1, (byte)0xD4, (byte)0xD8, (byte)0x40, (byte)0xD8, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x4B, (byte)0xC9, (byte)0xC3, (byte)0xD6, (byte)0xD5, (byte)0xD5, (byte)0xC5, (byte)0xC3, (byte)0x3B, (byte)0x82, (byte)0x17, (byte)0xAB, (byte)0x00, (byte)0x4A, (byte)0x00, (byte)0x12 };

            // This data is ASCII and should convert correctly when using UTF8
            byte[] goodData = { 0x31, 0x32, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 };
            String expected = "1223456789";

            CharConverter cc = new CharConverter(1208, systemObject_); // 1208 is the CCSID for UTF8
            String s = cc.byteArrayToString(badData);
            String t1 = cc.byteArrayToString(goodData);

            CharConverter c2 = new CharConverter(1208, systemObject_);
            s = c2.byteArrayToString(badData);
            String t2 = c2.byteArrayToString(goodData);

            if (t1.equals(expected) && t2.equals(expected))
            {
                succeeded();
            }
            else
            {
                failed("Strings not converted:\n '" + t1 + "' and '" + t2 + "' != '" + expected + "' bad data = "+s);
            }
        }
        catch (Throwable t)
        {
            failed(t, "Unexpected exception.");
        }
    }
}
