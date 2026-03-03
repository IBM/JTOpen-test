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
import java.util.Hashtable;
import java.util.Vector;

import test.Testcase;
import com.ibm.as400.access.*;

/**
 * Testcase ConvTableJavaTestcase. Test internal character conversion for
 * ConvTableJavaMap converter tables.
 **/
public class ConvTableJavaTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "ConvTableJavaTestcase";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.ConvTest.main(newArgs);
  }

  /**
   * Runs the variations requested. Loops through all of the double-byte EBCDIC
   * CCSIDs the Toolbox supports and tests their contents by roundtripping the
   * characters in both tables. Variations should be successful.
   **/
  public void run() {
    String[] ebcdicDoubleByte= {
        "cp300",
        "ibm-300",
        "csIBM300" ,
        "x-IBM300",
        "cpibm300",
       "300",
       "cp835",
       "cp834",
       "x0208",
       "835",
       "834",
       "x-JIS0208",
       "cp16684",
       "ibm-835",
       "ibm-834",
       "ibm-16684",
       "iso-ir-87",
       "16684",
       "csISO87JISX0208",
       "x-IBM834",
       "x-IBM835",
       "JIS0208", 
       "JIS_X0208-1983",
       "ibm300",
       "ibm835",
       "ibm834",
       "ibm300",
       "ibm835",
       "ibm834",
       "x-IBM16684",
       "JIS_C6226-1983",
       "ibm16684",     
    };
    
    String[] jis0212 = {
        "x0212",
        "jis_x0212-1990",
        "csISO159JISX02121990",
        "JIS0212",
        "iso-ir-159",
        "JIS_X0212-1990",
     
    };
    
    totalVariations_ = ConversionMaps.encodingCcsid_.size();
    boolean allVariations = (variationsToRun_.size() == 0);
    Vector<String> failedEncodings = new Vector<String>(); 
    Hashtable<String,String> ebcdicDoubleBytesHash = new Hashtable<String,String>();
    for (int i = 0; i < ebcdicDoubleByte.length; i++) {
      ebcdicDoubleBytesHash.put(ebcdicDoubleByte[i], ebcdicDoubleByte[i]);
    }

    Hashtable<String,String> jis0212Hash = new Hashtable<String,String>();
    for (int i = 0; i < jis0212.length; i++) {
      jis0212Hash.put(jis0212[i], jis0212[i]);
    }

    
    
    if (runMode_ != ATTENDED) {
      int i = 1;
      for (@SuppressWarnings("unchecked")
      Enumeration<String> keys = ConversionMaps.encodingCcsid_.keys(); keys.hasMoreElements(); ++i) {
        String encoding = (String) keys.nextElement();
        if (allVariations || variationsToRun_.contains("" + i)) {
          setVariation(i);
          if ( false) {
            notApplicable("DBCS-only encoding: " + encoding); // the Java encoding is DBCS-only.
          } else {
            try {
              ConvTable t = ConvTable.getTable(encoding);
              String inString = "ABCDEFG"; 
              String expectedString = "ABCDEFG";
              if (ebcdicDoubleBytesHash.get(encoding) != null ) {
                inString = "\uFF21\uFF22\uFF23\uFF24\uFF25\uFF26\uFF27"; 
                expectedString=inString; 
              } else if (jis0212Hash.get(encoding) != null) { 
                inString = "\u5092\u5093\u5094"; 
                expectedString=inString; 
                
              } else if ( "JIS0208".equals(encoding)) {
                inString = "\u5092\u5093\u5094"; 
                expectedString=inString; 
              }
              
              
              byte[] b = t.stringToByteArray(inString, 0);
              String s = t.byteArrayToString(b, 0, b.length, 0);
              if (s.equals(expectedString)) {
                succeeded();
              } else {
                failedEncodings.add(encoding);
                failed("Roundtrip failed for encoding " + encoding + ": " + inString + " != "+expectedString);
              }
            } catch (UnsupportedEncodingException uee) {
              failed(uee, "Unexpected exception for encoding " + encoding + ".");
            } catch (Exception e) {
              failed(e, "Unexpected exception for encoding " + encoding + ".");
            }
          }
        }
      }
    }
    int failedEncodingsSize = failedEncodings.size();
    if (failedEncodingsSize > 0) {
      System.out.println("Failed encodings"); 
      for (int i = 0; i < failedEncodingsSize; i++) {
        System.out.println("\""+failedEncodings.elementAt(i)+"\",");
      }
    }
  }
}
