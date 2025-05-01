///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableDoubleRoundTripTestcase.java
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
 Testcase ConvTableDoubleRoundTripTestcase.
 Test internal character conversion for all ConvTableDoubleMap converter tables.
 **/
public class ConvTableDoubleRoundTripTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableDoubleRoundTripTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    boolean isProxified_ = false;

    /**
     Runs the variations requested.
     Loops through all of the double-byte EBCDIC CCSIDs the Toolbox supports and tests their contents by roundtripping the characters in both tables.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.doubleCcsids_.length;
        boolean allVariations = (variationsToRun_.size() == 0);

        if (systemObject_.getProxyServer().length() > 0)
        {
            isProxified_ = true;
        }

        if (runMode_ != ATTENDED)
        {
            for (int i = 1; i <= ConvTest.doubleCcsids_.length; ++i)
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
                            // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.doubleCcsids_[i - 1]);
                            // ConvTableDoubleMap m = (ConvTableDoubleMap)c.newInstance();
                            ConvTableDoubleMap m = (ConvTableDoubleMap)ConvTable.getTable(
                                ConvTest.doubleCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                            roundTrip(m);
                        }
                        catch (Exception e)
                        {
                            failed(e, "Unexpected exception for EBCDIC CCSID " + ConvTest.doubleCcsids_[i - 1] + ".");
                        }
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
     Replace the surrogates from the toUnicode_ string
     **/
  public String replaceSurrogates(String s) {
    StringBuffer sb = new StringBuffer();
    int stringLength = s.length();
    for (int i = 0; i < stringLength; i++) {
      if (i < 256) {
        sb.append('\u3000');
      } else {
        char c = s.charAt(i);
        if (c < 0xD800 || c >= 0xE000) {
          sb.append(c);
        } else {
          sb.append('\u3000');
        }
      }
    }
    return sb.toString();
  }

    /**
     Helper method used to compare the original string with the roundtripped string.
     **/
    public void roundTrip(ConvTableDoubleMap c) throws Exception
    {
        boolean passed = true; 
        StringBuffer sb = new StringBuffer(); 
        sb.append("\n"); 
        int ccsid = c.getCcsid();

        // S1 is the toUnicode indexed by EBCDIC
        String s1 = new String(c.getToUnicode());
        s1 = replaceSurrogates(s1);

         // b1 is the trip back to EBCDIC
         byte[] b1 = c.stringToByteArray(s1, 0);
        String s2 = c.byteArrayToString(b1, 0, b1.length, 0);
        byte[] b2 = c.stringToByteArray(s2, 0);

    if (!s1.equals(s2)) {
      sb.append("Roundtrip (string) strings not equal for ccsid " + ccsid
          + ":\n");
      int min = s1.length() < s2.length() ? s1.length() : s2.length();
      sb.append("index: { original, roundtripped }\n");
      boolean intolerableMismatch = false;
      for (int i = 0; i < min; ++i) {

        if (s1.charAt(i) != s2.charAt(i)) {
          sb.append(hex(i) + ":{'" + hex((int) s1.charAt(i)) + "','"
              + hex((int) s2.charAt(i)) + "'}\n");

          // Tolerate mismatch in surrogate range
          if (i >= 0xD800 && i < 0xE000) {
            /* tolerate */
          } else {

            // Some chars no longer round-trip for CCSIDs 835 and 4931.
            if ((ccsid == 835 || ccsid == 4931) && 
                (int) s2.charAt(i) == 0xFFFD) // double-byte
                                              // substitution
                                             // character
            {
              int ch = (int) s1.charAt(i);
              if (ch == 0x00A7 || ch == 0x00A8 || ch == 0x00B0 || ch == 0x00B1
                  || ch == 0x00B4 || ch == 0x00B6 || ch == 0x00B7
                  || ch == 0x00D7 || ch == 0x00F7) {
                // Tolerate the mismatch.
                // Due to system converter table updates in V6R1,
                // some characters are known to no longer round-trip.
                // See also: ConvTableDoubleBytesTestcase.
              } else {
                intolerableMismatch = true;
              }
            } else if (ccsid == 300 || ccsid == 4396) {
              // The following characters do not round trip
              switch (s1.charAt(i)) {
              case 0x525D: // Peel, peeloff, shell strip TO peel
                if (s2.charAt(i) != '\u5265') intolerableMismatch = true;
                break;
              case 0x5C5B: // folding screen to folding screen, shield
                if (s2.charAt(i) != '\u5C4F') intolerableMismatch = true;
                break; 
              case 0x7C1E: // small bamboo basket to small bamboo basket
                if (s2.charAt(i) != '\u7baa') intolerableMismatch = true;
                break; 
              case 0x87EC: // cicada continuous to cicada continuous
                if (s2.charAt(i) != '\u8749') intolerableMismatch = true;
                break; 
              case 0x9A52: // dappled to dappled 
                if (s2.charAt(i) != '\u9A28') intolerableMismatch = true;
                break;
              }
            } else {
              intolerableMismatch = true;
            }
          }
        }
      }
      if (intolerableMismatch) {
        passed = false;
      }
    }
        if (b1.length != b2.length)
        {
            sb.append("Roundtrip (string) byte arrays not equal length for ccsid " + ccsid + ".\n");
            passed = false; 
        }
        int smallestArrayLen = b1.length; 
        if (b2.length < smallestArrayLen) smallestArrayLen = b2.length; 
        for (int i = 0; i < smallestArrayLen; ++i)
        {
            if (b1[i] != b2[i])
            {
                sb.append("Roundtrip (string ccsid="+ccsid +") b1[X'"+Integer.toHexString(0xFFFF & (i / 2 + 256))+"']=X'"+Integer.toHexString(0xFF & b1[i])+"'"+
                                       " =! b2["+i+"]=X'"+Integer.toHexString(0xFF & b2[i])+"'\n"); 
                passed = false; 
            }
        }

        // First need to get the DB char[] into a byte[].
        // Get the unicode to EBCID Table
        char[] c1 = c.getFromUnicode();
        b1 = new byte[c1.length * 2];
        // Loop through unicode values and give equivalent
        
        for (int i = 0; i < c1.length; ++i)
        {
          // Set anything in surrogate range to space
          if ((i >= 0xD800 && i < 0xDC00) || (i < 0x100)) {
            b1[i * 2] = (byte)0x40;
            b1[i * 2 + 1] = (byte)0x40;
          } else { 
            b1[i * 2] = (byte)(c1[i] >>> 8);
            b1[i * 2 + 1] = (byte)(c1[i] & 0x00FF);
          }
        }
        s1 = c.byteArrayToString(b1, 0, b1.length, 0);
        b2 = c.stringToByteArray(s1, 0);
        s2 = c.byteArrayToString(b2, 0, b2.length, 0);

        if (b1.length != b2.length)
        {
            sb.append("Roundtrip (bytes) byte arrays not equal length for ccsid " + ccsid + ".\n");
            sb.append("  ebcdicStartLength = "+b1.length+" finishLength="+b2.length+"\n"); 
            passed = false; 
        }
        String failMsg = "";
        smallestArrayLen = b1.length; 
        if (b2.length < smallestArrayLen) smallestArrayLen = b2.length; 
        for (int i = 0; i < smallestArrayLen; ++i)
        {
            if (b1[i] != b2[i])
            {
                if (ccsid == 61952)
                {
                    // There are 38 or so characters that no longer roundtrip between 61952 and 13488
                    // The 61952 table was updated to reflect 'newer' versions of some of the Japanese
                    // Kanji characters. We check for them here.
                    int ch1 = 0;
                    int ch2 = 0;
                    int pos = i / 2;
                    if (i % 2 == 0)
                    {
                        ch1 = 0xFFFF & ((int)(0x00FF & b1[i]) * 256 + (0x00FF & b1[i+1]));
                        ch2 = 0xFFFF & ((int)(0x00FF & b2[i]) * 256 + (0x00FF & b2[i+1]));
                    }
                    else
                    {
                        ch1 = 0xFFFF & ((int)(0x00FF & b1[i-1]) * 256 + (0x00FF & b1[i]));
                        ch2 = 0xFFFF & ((int)(0x00FF & b2[i-1]) * 256 + (0x00FF & b2[i]));
                    }
                    boolean ok = true;
                    switch (pos)
                    {
                        case 0x9E7C: ok = (ch2 == 0x9E78); break;
                        case 0x9830: ok = (ch2 == 0x982C); break;
                        case 0x5861: ok = (ch2 == 0x586B); break;
                        case 0x91AC: ok = (ch2 == 0x91A4); break;
                        case 0x56CA: ok = (ch2 == 0x56A2); break;
                        case 0x91B1: ok = (ch2 == 0x9197); break;
                        case 0x9EB4: ok = (ch2 == 0x9EB9); break;
                        case 0x881F: ok = (ch2 == 0x874B); break;
                        case 0x840A: ok = (ch2 == 0x83B1); break;
                        case 0x7E61: ok = (ch2 == 0x7E4D); break;
                        case 0x4FE0: ok = (ch2 == 0x4FA0); break;
                        case 0x8EC0: ok = (ch2 == 0x8EAF); break;
                        case 0x7E6B: ok = (ch2 == 0x7E4B); break;
                        case 0x9A52: ok = (ch2 == 0x9A28); break;
                        case 0x87EC: ok = (ch2 == 0x8749); break;
                        case 0x7130: ok = (ch2 == 0x7114); break;
                        case 0x8523: ok = (ch2 == 0x848B); break;
                        case 0x5C5B: ok = (ch2 == 0x5C4F); break;
                        case 0x9DD7: ok = (ch2 == 0x9D0E); break;
                        case 0x5699: ok = (ch2 == 0x565B); break;
                        case 0x525D: ok = (ch2 == 0x5265); break;
                        case 0x6414: ok = (ch2 == 0x63BB); break;
                        case 0x7626: ok = (ch2 == 0x75E9); break;
                        case 0x7C1E: ok = (ch2 == 0x7BAA); break;
                        case 0x6451: ok = (ch2 == 0x63B4); break;
                        case 0x555E: ok = (ch2 == 0x5516); break;
                        case 0x6F51: ok = (ch2 == 0x6E8C); break;
                        case 0x7006: ok = (ch2 == 0x6D9C); break;
                        case 0x79B1: ok = (ch2 == 0x7977); break;
                        case 0x9EB5: ok = (ch2 == 0x9EBA); break;
                        case 0x5C62: ok = (ch2 == 0x5C61); break;
                        case 0x985A: ok = (ch2 == 0x985B); break;
                        case 0x6522: ok = (ch2 == 0x6505); break;
                        case 0x688E: ok = (ch2 == 0x688D); break;
                        case 0x7E48: ok = (ch2 == 0x7E66); break;
                        case 0x8141: ok = (ch2 == 0x80FC); break;
                        case 0x9839: ok = (ch2 == 0x983D); break;
                        case 0xF86F: ok = (ch2 == 0x2116); break;
                        case 0xD800: ok = true; break; // ignore surrogate 
                        case 0xF8FF: ok = (ch2 == 0x2116); break; // This one isn't in the document that
                            // explains the 38 Kanji characters, but since the character is in the PUA,
                            // it's OK to map it this way.

                        default: ok = false; break;
                    }

                    if (!ok)
                    {
                        failMsg += "position " + hex(pos) + ": " + hex(ch1) + " != " + hex(ch2) + "\n";
                        passed = false; 
                    }
                }
                else
                {
                  if (b1[i] != b2[i])
                    
                    sb.append("Roundtrip (bytes) ccsid="+ccsid+" b1[X'"+Integer.toHexString((i/2))+"."+(i%2)+"']=X'"+Integer.toHexString(0xFF & b1[i])+
                        "' != b2[X'"+Integer.toHexString(i)+"']=X'"+Integer.toHexString(0xFF & b2[i])+"'\n"); 
                    passed =false; 
                }
            }
        }
        if (failMsg.length() > 0)
        {
            sb.append("Roundtrip (bytes) byte arrays not equal for ccsid "+ccsid+".  " + failMsg);
            passed=false; 
        }
        if (!s1.equals(s2))
        {
            sb.append("Roundtrip (bytes) strings not equal for ccsid " + ccsid + ".");
            passed = false; 
        }

        // Test the surrogate translations if they exist
        
        String originalSurrogateString = getUnicodeSurrogateString();
        sb.append("\nbeginning of surrogateString is "+showStringAsHex(originalSurrogateString.substring(0, 256))+ "\n"); 
        byte[] surrogateBytes1 = c.stringToByteArray(originalSurrogateString, 0);
        String surrogateString = c.byteArrayToString(surrogateBytes1, 0, surrogateBytes1.length, 0);
        sb.append("after 1 pass surrogateString is "+showStringAsHex(surrogateString.substring(0, 256))+ "\n"); 
        byte[] surrogateBytes2 = c.stringToByteArray(surrogateString, 0);
        
        // Check for round trip 
        boolean surrogatesFailed = false; 
        smallestArrayLen = surrogateBytes1.length; 
        if (surrogateBytes2.length < smallestArrayLen) smallestArrayLen = surrogateBytes2.length; 
        for (int i = 0; i < smallestArrayLen; i+=2)
        { 
          int short1 = ( (surrogateBytes1[i] &0xFF) *0x100 + ((surrogateBytes1[i+1]) & 0xFF)); 
          int short2 = ( (surrogateBytes2[i] &0xFF) *0x100 + ((surrogateBytes2[i+1]) & 0xFF)); 
          
            if (short1 != short2)
            {
              if ( !surrogatesFailed) { 
                surrogatesFailed = true;
                sb.append("Roundtrip EBCDIC to EBCDIC for surrgates failed for "+ccsid+"\n");
                sb.append("POS   FROM      -> TO   = UNICODE -> UNICODE \n");
                passed = false; 
              }
              String unicode1 = originalSurrogateString.substring(i,i+2);  
              // byte[] surrogatesBytes3 = c.stringToByteArray(unicode1); 
              String unicode2 = c.byteArrayToString(b1, i, 2);  
              
              sb.append(i+" "+hex(short1)+ " -> " +hex(short2)+" " + showStringAsHex(unicode1)+" " + showStringAsHex(unicode2)+ "\n"); 
            }
        } /* for */ 
        assertCondition(passed, sb); 
    }

    // Create a unicode string will all possible surrogates
    String unicodeSurrogateString = null; 
    String getUnicodeSurrogateString() {
       if (unicodeSurrogateString == null) { 
         StringBuffer sb = new StringBuffer(); 
         for (int i = 0xD800; i < 0xDC00; i++) {
           for (int j = 0xDC00; j < 0xE000; j++) { 
             sb.append((char) i);  
             sb.append((char) j);  
           }
         }
         unicodeSurrogateString = sb.toString(); 
       }
       return unicodeSurrogateString; 
    }
}
