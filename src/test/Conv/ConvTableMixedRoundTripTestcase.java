///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableMixedRoundTripTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.util.Random;

import test.ConvTest;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableMixedRoundTripTestcase.
 Test internal character conversion for all ConvTableMixedMap converter tables.
 **/
public class ConvTableMixedRoundTripTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableMixedRoundTripTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Runs the variations requested.
     **/
    public void run()
    {
    	int var = 0; 
        totalVariations_ = ConvTest.mixedCcsids_.length * 3 + 2;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.mixedCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i))
                {
                	var = i; 
                    setVariation(i);
                    try
                    {
                        // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.mixedCcsids_[i - 1]);
                        // ConvTableMixedMap m = (ConvTableMixedMap)c.newInstance();
                        ConvTableMixedMap m = (ConvTableMixedMap)ConvTable.getTable(
                            ConvTest.mixedCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                        roundTripSB(m);
                    }
                    catch (Exception e)
                    {
                        failed(e, "Unexpected exception for mixed-byte EBCDIC CCSID " + ConvTest.mixedCcsids_[i - 1] + ".");
                    }
                }
            }
            for (int i = 1; i <= ConvTest.mixedCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + (i + ConvTest.mixedCcsids_.length)))
                {
                	var = i + ConvTest.mixedCcsids_.length; 
                    setVariation(var);
                    try
                    {
                        Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.mixedCcsids_[i - 1]);
                        ConvTableMixedMap m = (ConvTableMixedMap)c.newInstance();
                        roundTripDB(m);
                    }
                    catch (Exception e)
                    {
                        failed(e, "Unexpected exception for mixed-byte EBCDIC CCSID " + ConvTest.mixedCcsids_[i - 1] + ".");
                    }
                }
            }
            for (int i = 1; i <= ConvTest.mixedCcsids_.length; ++i)
            {
                if (allVariations || variationsToRun_.contains("" + i + ConvTest.mixedCcsids_.length + ConvTest.mixedCcsids_.length))
                {
                	var = i + ConvTest.mixedCcsids_.length + ConvTest.mixedCcsids_.length;
                    setVariation(var);
                    try
                    {
                        Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.mixedCcsids_[i - 1]);
                        ConvTableMixedMap m = (ConvTableMixedMap)c.newInstance();
                        roundTripMB(m);
                    }
                    catch (Exception e)
                    {
                        failed(e, "Unexpected exception for mixed-byte EBCDIC CCSID " + ConvTest.mixedCcsids_[i - 1] + ".");
                    }
                }
            }

            // Do other variations.
            int curVar = ConvTest.mixedCcsids_.length * 3 + 1;

            if (allVariations || variationsToRun_.contains("" + curVar))
            {
                setVariation(curVar);
                MiscVar001();
            }
            ++curVar;
            if (allVariations || variationsToRun_.contains("" + curVar))
            {
                setVariation(curVar);
                MiscVar002();
            }
        }
    }

    public void MiscVar001()
    {
        try
        {
            ConvTable5035 ct = new ConvTable5035();
            String string1 = "\uFF21 \uFF22 A B ";
            byte[] bytes1 = ct.stringToByteArray(string1, 0);
            String string2 = ct.byteArrayToString(bytes1, 0, bytes1.length, 0);
            if (string1.equals(string2)) succeeded("MiscVar001");
            else failed("Strings not equal: " + string1 + " != " + string2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    public void MiscVar002()
    {
        try
        {
            ConvTable5035 ct = new ConvTable5035();
            String string1 = "\u20AC \u20AC A B ";  // Euro.
            byte[] bytes1 = ct.stringToByteArray(string1, 0);
            String string2 = ct.byteArrayToString(bytes1, 0, bytes1.length, 0);
            if (string1.equals(string2)) succeeded("MiscVar002");
            else failed("Strings not equal: " + string1 + " != " + string2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
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
     Helper method used to print a byte as a hex string.
     **/
    public static String hex(byte x)
    {
        StringBuffer h = new StringBuffer(Integer.toHexString(x & 0xFF).toUpperCase());
        while (h.length() < 2) h.insert(0, '0');
        h.insert(0, "0x");
        return h.toString();
    }

    /**
     Helper method used to compare the original string with the roundtripped string.
     **/
    public void roundTripSB(ConvTableMixedMap c) throws Exception
    {
        int ccsid = c.getCcsid();

        String s1 = new String(c.sbTable_.toUnicode_);
        // Replace SO and SI chars with substitution chars.
        s1 = s1.replace((char)0x000E, (char)0x001A); // 0x001A is the Unicode sub char for SB.
        s1 = s1.replace((char)0x000F, (char)0x001A);
       	s1 = s1.replace((char)0xd800, (char)0x001A);
        byte[] b1 = c.stringToByteArray(s1, 0);
        String s2 = c.byteArrayToString(b1, 0, b1.length, 0);
        byte[] b2 = c.stringToByteArray(s2, 0);

        if (!s1.equals(s2))
        {
            StringBuffer buf = new StringBuffer("SB Roundtrip (string) strings not equal for ccsid " + ccsid + ":\n");
            if (s1.length() != s2.length())
            {
                buf.append("Lengths not equal: " + s1.length() + "," + b1.length + " != " + s2.length() + "," + b2.length + "\n");
            }
            int min = s1.length() < s2.length() ? s1.length() : s2.length();
            buf.append("index: { original, roundtripped }\n");
            int cnt = 0;
            for (int i = 0; i < min && cnt < 4; ++i)
            {
                char c1 = s1.charAt(i); 
                char c2 = s2.charAt(i);
                // Ignore replacement 
                if ((c1 == '\ufefe') && (c2 == '\u001a')) {
                  c1 = '\u001a'; 
                } else if ((c1 == '\u001a') && (c2 == '\ufefe')) {
                  c2 = '\u001a'; 
                }
                if (c1 != c2)
                {
                    
                    buf.append(hex(i) + ":{'" + hex((int)s1.charAt(i)) + "','" + hex((int)s2.charAt(i)) + "'}\n");
                    ++cnt;
                }
            }
            failed(buf.toString() + "...");
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
                failed("Roundtrip (string) byte arrays not equal at position " + i + " for ccsid " + ccsid + ": " + hex(b1[i]) + " != " + hex(b2[i]));
                return;
            }
        }

        b1 = c.sbTable_.fromUnicode_;
        // Replace SO and SI with substitution chars.
        for (int i = 0; i < b1.length; ++i)
        {
            if (b1[i] == 0x0E)
            {
                b1[i] = 0x3F;
            }
            else if (b1[i] == 0x0F)
            {
                b1[i] = 0x3F;
            }
        }
        s1 = c.byteArrayToString(b1, 0, b1.length, 0);
        b2 = c.stringToByteArray(s1, 0);
        s2 = c.byteArrayToString(b2, 0, b2.length, 0);

        int minByteSize = b1.length; 
        if (b2.length < minByteSize) minByteSize = b2.length; 
        int i = 0; int j = 0; 
        for (; i < b1.length && j < b2.length; i++, j++)
        {
            if (b1[i] != b2[j])
            {
                String message = "SB Roundtrip (bytes) byte arrays not equal at position 0x" + Integer.toHexString(i) + " for ccsid " + ccsid + ": " + hex(b1[i]) + " != " + hex(b2[j]);
                if (b2[j] == 0x0e) {
                  message += " subsequent b2 values "+hex(b2[j+1])+" "+hex(b2[j+2]); 
                }
                failed(message); 
                return;
            }
        }
        
        if (b1.length != b2.length)
        {
            failed("Roundtrip (bytes) byte arrays not equal length for ccsid " + ccsid + ". b1.length="+b1.length+" b2.length="+b2.length);
            return;
        }

        if (!s1.equals(s2))
        {
            failed("Roundtrip (bytes) strings not equal for ccsid " + ccsid + ".");
            return;
        }

        succeeded("Roundtrip (bytes) ccsid "+ccsid);
    }

    /**
     Helper method used to compare the original string with the roundtripped string.
     **/
    public void roundTripDB(ConvTableMixedMap c) throws Exception
    {
        int ccsid = c.getCcsid();

        String s1 = new String(c.dbTable_.getToUnicode());
        // Replace SO and SI chars with substituion chars.
        s1 = s1.replace((char)0x000E, (char)0xFFFD); // 0xFFFD is the Unicode sub char for DB.
        s1 = s1.replace((char)0x000F, (char)0xFFFD);
        s1 = s1.replace((char)0xD800, (char)0xFFFD); // Replace surrogates with sub
        byte[] b1 = c.stringToByteArray(s1, 0);
        String s2 = c.byteArrayToString(b1, 0, b1.length, 0);
        byte[] b2 = c.stringToByteArray(s2, 0);

        if (!s1.equals(s2))
        {
            StringBuffer buf = new StringBuffer("DB Roundtrip (string) strings not equal for ccsid " + ccsid + ":\n");
            if (s1.length() != s2.length())
            {
                buf.append("Lengths not equal: " + s1.length() + "," + b1.length + " != " + s2.length() + "," + b2.length + "\n");
            }
            int min = s1.length() < s2.length() ? s1.length() : s2.length();
            buf.append("index: { original, roundtripped }\n");
            int cnt = 0;
            for (int i = 0; i < min && cnt < 4; ++i)
            {
              
              char c1 = s1.charAt(i); 
              char c2 = s2.charAt(i);
              // Ignore replacement 
              if ((c1 == '\ufefe') && (c2 == '\u001a')) {
                c1 = '\u001a'; 
              } else if ((c1 == '\u001a') && (c2 == '\ufefe')) {
                c2 = '\u001a'; 
              }
              // Fix known mappings
              if (c1 == '\u525D' && c2 == '\u5265') {
                  c2 =  '\u525D'; 
              } else if (c1 == '\u5c5b' && c2 == '\u5c4f') {
                         c2 =  '\u5c5b'; 
              } else if (c1 == '\u7c1e' && c2 == '\u7baa') {
                c2 =  '\u7c1e'; 
              } else if (c1 == '\u87ec' && c2 == '\u8749') {
                c2 =  '\u87ec'; 
              } else if (c1 == '\u9a52' && c2 == '\u9a28') {
                c2 =  '\u9a52'; 
              }

              
                if (c1 != c2)
                {
                    buf.append(hex(i) + ":{'" + hex((int)s1.charAt(i)) + "','" + hex((int)s2.charAt(i)) + "'}\n");
                    ++cnt;
                }
            }
            if (cnt > 0) { 
              failed(buf.toString() + "...");
              return;
            }
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
                failed("Roundtrip (string) byte arrays not equal at position " + i + " for ccsid " + ccsid + ": " + hex(b1[i]) + " != " + hex(b2[i]));
                return;
            }
        }

        // First need to get the DB char[] into a byte[].
        char[] c1 = c.dbTable_.getFromUnicode();
        b1 = new byte[c1.length * 2 + 2];
        b1[0] = 0x0E; // shift out.
        for (int i = 0; i < c1.length; ++i)
        {
            b1[i * 2 + 1] = (byte)(c1[i] >>> 8);
            b1[i * 2 + 1 +1] = (byte)(c1[i] & 0x00FF);
        }
        b1[c1.length*2 + 1] = 0x0F; // shift in.

        s1 = c.byteArrayToString(b1, 0, b1.length, 0);
        b2 = c.stringToByteArray(s1, 0);
        s2 = c.byteArrayToString(b2, 0, b2.length, 0);

        // The byte arrays won't be the same since the Euro 0x20AC will always
        // be a single-byte lookup. However, the Strings should be the same.

        /*    if (b1.length != b2.length)
         {
         StringBuffer buf = new StringBuffer("DB Roundtrip (bytes) byte arrays not equal for ccsid "+ccsid+":\n");
         buf.append("Lengths not equal: "+s1.length()+","+b1.length+" != "+s2.length()+","+b2.length+"\n");
         int min = b1.length < b2.length ? b1.length : b2.length;
         buf.append("index: { original, roundtripped }\n");
         int cnt=0;
         for (int i=0; i<min && cnt<4; ++i)
         {
         if (b1[i] != b2[i])
         {
         buf.append(hex(i)+":{'"+hex(b1[i])+"','"+hex(b2[i])+"' ["+hex((int)s2.charAt(i/2))+"]}\n");
         ++cnt;
         }
         }
         failed(buf.toString()+"...");
         return;
         }
         for (int i=0; i<b1.length; ++i)
         {
         if (b1[i] != b2[i])
         {
         failed("Roundtrip (bytes) byte arrays not equal at position "+i+" for ccsid "+ccsid+": "+hex(b1[i])+" != "+hex(b2[i]));
         return;
         }
         }
         */
        if (!s1.equals(s2))
        {
            failed("Roundtrip (bytes) strings not equal for ccsid " + ccsid + ".");
            return;
        }

        succeeded("Roundtrip (bytes) ccsid "+ccsid);
    }

    /**
     Helper method used to compare the original string with the roundtripped string.
     **/
    public void roundTripMB(ConvTableMixedMap c) throws Exception
    {
        int ccsid = c.getCcsid();

        Random r = new Random(System.currentTimeMillis());

        char[] cbuf = new char[c.sbTable_.toUnicode_.length + c.dbTable_.getToUnicode().length];
        int sbCount = 0;
        int dbCount = 0;
        for (int i = 0; i < cbuf.length; ++i)
        {
            boolean useSB = r.nextInt() % 2 == 0;
            if (useSB)
            {
                if (sbCount < c.sbTable_.toUnicode_.length)
                {
                    cbuf[i] = c.sbTable_.toUnicode_[sbCount++];
                }
                else
                {
                    for (; i < cbuf.length; ++i)
                    {
                        cbuf[i] = c.dbTable_.getToUnicode()[dbCount++];
                    }
                }
            }
            else
            {
                if (dbCount < c.dbTable_.getToUnicode().length)
                {
                    cbuf[i] = c.dbTable_.getToUnicode()[dbCount++];
                }
                else
                {
                    for (; i < cbuf.length; ++i)
                    {
                        cbuf[i] = c.sbTable_.toUnicode_[sbCount++];
                    }
                }
            }
        }

        String s1 = new String(cbuf);
        // Replace SO and SI chars with substituion chars
        s1 = s1.replace((char)0x000E, (char)0xFFFD); // 0xFFFD is the Unicode sub char for DB
        s1 = s1.replace((char)0x000F, (char)0xFFFD);
        s1 = s1.replace((char)0xD800, (char)0xFFFD);
        byte[] b1 = c.stringToByteArray(s1, 0);
        String s2 = c.byteArrayToString(b1, 0, b1.length, 0);
        byte[] b2 = c.stringToByteArray(s2, 0);

        if (!s1.equals(s2))
        {
            StringBuffer buf = new StringBuffer("Roundtrip (mixed) strings not equal for ccsid " + ccsid + ":\n");
            if (s1.length() != s2.length())
            {
                buf.append("Lengths not equal: " + s1.length() + "," + b1.length + " != " + s2.length() + "," + b2.length + "\n");
            }
            int min = s1.length() < s2.length() ? s1.length() : s2.length();
            buf.append("index: { original, roundtripped }\n");
            int cnt = 0;
            for (int i = 0; i < min && cnt < 4; ++i)
            {
              char c1 = s1.charAt(i); 
              char c2 = s2.charAt(i);
              // Ignore replacement 
              if ((c1 == '\ufefe') && (c2 == '\u001a')) {
                c1 = '\u001a'; 
              } else if ((c1 == '\u001a') && (c2 == '\ufefe')) {
                c2 = '\u001a'; 
              }

              // Fix known mappings difference (see DB below)
              if (c1 == '\u525D' && c2 == '\u5265') {
                  c2 =  '\u525D'; 
              } else if (c1 == '\u5c5b' && c2 == '\u5c4f') {
                         c2 =  '\u5c5b'; 
              } else if (c1 == '\u7c1e' && c2 == '\u7baa') {
                c2 =  '\u7c1e'; 
              } else if (c1 == '\u87ec' && c2 == '\u8749') {
                c2 =  '\u87ec'; 
              } else if (c1 == '\u9a52' && c2 == '\u9a28') {
                c2 =  '\u9a52'; 
              }

                if (c1 != c2)
                {
                    buf.append(hex(i) + ":{'" + hex((int)s1.charAt(i)) + "','" + hex((int)s2.charAt(i)) + "'}\n");
                    ++cnt;
                }
            }
            if (cnt > 0) { 
              failed(buf.toString() + "...");
              return;
            }
           
        }
        if (b1.length != b2.length)
        {
            failed("Roundtrip (mixed) byte arrays not equal length for ccsid " + ccsid + ".");
            return;
        }
        for (int i = 0; i < b1.length; ++i)
        {
            if (b1[i] != b2[i])
            {
                failed("Roundtrip (mixed) byte arrays not equal at position " + i + " for ccsid " + ccsid + ": " + hex(b1[i]) + " != " + hex(b2[i]));
                return;
            }
        }

        succeeded("Roundtrip (mixed) ccsid="+ccsid);
    }
}
