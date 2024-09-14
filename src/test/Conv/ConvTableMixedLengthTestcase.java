///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableMixedLengthTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.io.ByteArrayOutputStream;
import com.ibm.as400.access.ConvTableWriter;

import test.Testcase;
import com.ibm.as400.access.*; 

/**
  Testcase ConvTableMixedLengthTestcase.
  Test variations for all ConvTableMixedMap converter tables.
**/
public class ConvTableMixedLengthTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableMixedLengthTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
  /**
    Verifies internal tables for ccsid 1364.
    Result: Variation should be successful.
  **/
  public void Var001()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable1364();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if (sbCcsid != 833)
      {
        failed("Incorrect internal single-byte table for ccsid 1364: " + sbCcsid + " != 833");
      }
      else if (dbCcsid != 834)
      {
        failed("Incorrect internal single-byte table for ccsid 1364: " + dbCcsid + " != 834");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 1388.
    Result: Variation should be successful.
  **/
  public void Var002()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable1388();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if ((sbCcsid != 836) && (sbCcsid != 1001388 ))
      {
        failed("Incorrect internal single-byte table for ccsid 1388: " + sbCcsid + " != 836 or 1001388");
      }
      else if ((dbCcsid != 837) && (dbCcsid != 2001388 ))
      {
        failed("Incorrect internal double-byte table for ccsid 1388: " + dbCcsid + " != 837 or 2001388");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  
  boolean testUnicodeToEbcdic(String input, String expectedOutputBytes, int ccsid, StringBuffer sb)
      throws Exception {
    boolean passed = true;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ConvTableWriter writer = new ConvTableWriter(outputStream, ccsid);
    writer.write(input);
    writer.flush();
    writer.close();
    byte[] outputArray = outputStream.toByteArray();
    byte[] expected = stringToBytes(expectedOutputBytes);
    sb.append("\narray1=output array2=expected\n");
    if (!areEqual(outputArray, expected, sb)) {
      passed = false;
    }
    return passed;
  }

  boolean testEbcdicToUnicode(String inputBytes, String expectedOutput, int ccsid, StringBuffer sb) throws Exception {
    boolean passed = true;
    byte[] inputArray = stringToBytes(inputBytes);
    /* 
    ByteArrayInputStream inputStream = new ByteArrayInputStream(inputArray); 
    ConvTableReader reader = new ConvTableReader(inputStream, ccsid); 
    String outputString = reader.read(expectedOutput.length());
    reader.close(); 
    */
    
    String outputString = CharConverter.byteArrayToString(ccsid , inputArray );

    sb.append("array1=output array2=expected\n");
    if (!areEqual(outputString.getBytes("UTF-16BE"), expectedOutput.getBytes("UTF-16BE"), sb)) {
      passed =false; 
    }
    
    return passed; 
  }
  
  public void verify1399() { 
    try
    {
      ConvTableMixedMap c = new ConvTable1399();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if ((sbCcsid != 5123) && (sbCcsid != 1001399))
      {
        failed("Incorrect internal single-byte table for ccsid 1399: " + sbCcsid + " != 5123 or 1001399 ");
      }
      else if ((dbCcsid != 16684) && (dbCcsid != 2001399))
      {
        failed("Incorrect internal double-byte table for ccsid 1399: " + dbCcsid + " != 16684 or 2001399");
      }
      else
      {
        boolean passed = true; 
        StringBuffer sb = new StringBuffer(); 
        // Verify the following translations from CPS 9ERBJK
        /* 
        Unicode => EBCDIC Conversion.                                            
            +---------+----------------------------+                                 
            | UNICODE |   TARGET EBCDIC RESULT     |                                 
            | SOUCRE  |     IBM i    |    JT400    |                                 
            |         | 5035 | 1399  | 5035 | 1399 |                                 
            +---------+------+-------+------|------+                                 
           1| FF0D    | 4260 | E9F3  | 4260 | 4260*|                                 
           3| 2212    | 4260 | 4260  | 4260 | 4260 |                                 
           5| 2014    | 444A | 444A  | 444A | 444A |                                 
           7| 2015    | 444A | DDB7  | 444A | 444A*|                                 
           9| 301C    | 43A1 | 43A1  | 43A1 | 43A1 |                                 
          11| FF5E    | 43A1 | E9F4  | 43A1 | 43A1*|                                 
          13| 2016    | 447C | 447C  | 447C | 447C |                                 
          15| 2225    | 447C | DFE5  | 447C | 447C*|                                 
          17| 00A6    | 426A | 426A  | 426A | 426A |                                 
          19| FFE4    | 426A | E9F5  | 426A | 426A*|                                 
                                                                                     
                                            */
        
        sb.append("\nUNICODE TO EBCDIC 1399\n"); 
        if (! testUnicodeToEbcdic(
               "\uff0d"+"\u2212"+"\u2014"+"\u2015"+"\u301c"+"\uff5e"+"\u2016"+"\u2225"+"\u00a6"+"\uffe4",
            "0e"+"e9f3"  +"4260"  +"444a"  +"ddb7"  +"43a1"  +"e9f4"  +"447c"  +"dfe5"  +"426a"  +"e9f5"+"0f", 1399, sb)) {
          passed = false; 
        }

        // Test surrogate pairs
        if (! testUnicodeToEbcdic(
            "\ud840\udc0b"+"\ud84a\udf4f",
         "0e"+"b342"  +"b477" +"0f", 1399, sb)) {
       passed = false; 
     }

        
        
        /*
            EBCDIC => Unicode Conversion.                                            
                                                                                     
            +---------+----------------------------+                                 
            | EBCDIC  |   TARGET UNICODE RESULT    |                                 
            | SOUCRE  |     IBM i    |    JT400    |                                 
            |         | 5035 | 1399  | 5035 | 1399 |                                 
            +---------+------+-------+------|------+                                 
            | 4260    | FF0D | 2212  | FF0D | FF0D*|                                 
            | E9F3    | FF0D | FF0D  | FF0D | FF0D |                                 
            | 444A    | 2015 | 2014  | 2015 | 2015*|                                 
            | DDB7    | 2015 | 2015  | 2015 | 2015 |                                 
            | 43A1    | FF5E | 301C  | FF5E | FF5E*|                                 
            | E9F4    | FF5E | FF5E  | FF5E | FF5E |                                 
            | 447C    | 2225 | 2016  | 2225 | 2225*|                                 
            | DFE5    | 2225 | 2225  | 2225 | 2225 |                                 
            | 426A    | FFE4 | 00A6  | FFE4 | FFE4*|                                 
            | E9F5    | FFE4 | FFE4  | FFE4 | FFE4 |  
        
        */
        sb.append("EBCDIC 1399 TO UNICODE\n"); 
        String inputBytes =   "0e"+ "4260"+  "e9f3"+  "444a"+  "ddb7"+  "43a1"+  "e9f4"+  "447c"+  "dfe5"+  "426A"+  "e9f5"+"0F";
        String expectedOutput = "\u2212"+"\uff0d"+"\u2014"+"\u2015"+"\u301c"+"\uff5e"+"\u2016"+"\u2225"+"\u00a6"+"\uffe4"; 
        if (!testEbcdicToUnicode(inputBytes, expectedOutput, 1399, sb)) {
          passed = false; 
        }
        
        /* Test EBCDIC 1399 to unicode conversions with Surrogate */

        /*


         */
        sb.append("EBCDIC 1399 TO UNICODE\n"); 
        String inputSurrogateBytes =   "0e"+ 
"B342"+
"B346"+
"B348"+
"B349"+
"B34E"+
"B353"+
"B35D"+
"B360"+
"B364"+
"B367"+
"B368"+
"B36B"+
"B370"+
"B373"+
"B376"+
"B379"+
"B37A"+
"B380"+
"B382"+
"B384"+
"B388"+
"B389"+
"B38D"+
"B39B"+
"B39F"+
"B3A2"+
"B3A3"+
"B3A4"+
"B3A8"+
"B3B0"+
"B3B1"+
"B3B3"+
"B3B4"+
"B3B5"+
"B3B6"+
"B3B7"+
"B3B8"+
"B3BA"+
"B3BD"+
"B3BE"+
"B3C0"+
"B3C5"+
"B3C6"+
"B3CA"+
"B3D0"+
"B3D1"+
"B3D2"+
"B3D6"+
"B3D9"+
"B3DA"+
"B3E4"+
"B3E9"+
"B3EA"+
"B3EB"+
"B3EC"+
"B3EF"+
"B3F0"+
"B3F2"+
"B3F4"+
"B3F5"+
"B3F9"+
"B3FA"+
"B444"+
"B446"+
"B449"+
"B450"+
"B452"+
"B456"+
"B45A"+
"B462"+
"B468"+
"B469"+
"B46C"+
"B472"+
"B475"+
"B477"+
"B478"+
"B479"+
"B47C"+
"B47D"+
"B480"+
"B491"+
"B493"+
"B494"+
"B496"+
"B49F"+
"B4A1"+
"B4A2"+
"B4A3"+
"B4A4"+
"B4A5"+
"B4A7"+
"B4A8"+
"B4AF"+
"B4B0"+
"B4B1"+
"B4B2"+
"B4BF"+
"B4C4"+
"B4C5"+
"B4CB"+
"B4D2"+
"B4D3"+
"B4D4"+
"B4D5"+
"B4DC"+
"B4DD"+
"B4DE"+
"B4DF"+
"B4E0"+
"B4E3"+
"B4E5"+
"B4E7"+
"B4E8"+
"B4F0"+
"B4F6"+
"B543"+
"B544"+
"B545"+
"B54F"+
"B555"+
"B557"+
"B558"+
"B566"+
"B56C"+
"B56E"+
"B573"+
"B574"+
"B57F"+
"B584"+
"B586"+
"B58E"+
"B591"+
"B59A"+
"B59E"+
"B59F"+
"B5A0"+
"B5A4"+
"B5AE"+
"B5B0"+
"B5B2"+
"B5B4"+
"B5B9"+
"B5BA"+
"B5BB"+
"B5BD"+
"B5BE"+
"B5C0"+
"B5C2"+
"B5C4"+
"B5C8"+
"B5CD"+
"B5CF"+
"B5D0"+
"B5D3"+
"B5E1"+
"B5E3"+
"B5E4"+
"B5E9"+
"B5EA"+
"B5EE"+
"B5EF"+
"B5F0"+
"B5F9"+
"B5FC"+
"B5FD"+
"B644"+
"B647"+
"B648"+
"B649"+
"B64A"+
"B64B"+
"B64C"+
"B64D"+
"B64E"+
"B64F"+
"B651"+
"B652"+
"B654"+
"B656"+
"B657"+
"B659"+
"B65E"+
"B663"+
"B664"+
"B666"+
"B668"+
"B672"+
"B675"+
"B67B"+
"B681"+
"B682"+
"B685"+
"B686"+
"B689"+
"B68A"+
"B68E"+
"B691"+
"B692"+
"B697"+
"B69E"+
"B6A1"+
"B6A4"+
"B6AB"+
"B6AC"+
"B6B0"+
"B6B1"+
"B6B2"+
"B6B3"+
"B6B7"+
"B6B8"+
"B6BA"+
"B6C5"+
"B6C6"+
"B6C7"+
"B6C9"+
"B6CB"+
"B6D1"+
"B6D2"+
"B6D4"+
"B6D6"+
"B6D7"+
"B6DA"+
"B6DC"+
"B6DD"+
"B6E4"+
"B6E7"+
"B6EE"+
"B6EF"+
"B6F0"+
"B6F1"+
"B6F4"+
"B6F7"+
"B6F9"+
"B6FC"+
"B6FD"+
"B743"+
"B744"+
"B747"+
"B74A"+
"B74B"+
"B74E"+
"B750"+
"B751"+
"B752"+
"B754"+
"B756"+
"B759"+
"B75A"+
"B75D"+
"B75E"+
"B75F"+
"B760"+
"B761"+
"B764"+
"B765"+
"B768"+
"B769"+
"B76A"+
"B76B"+
"B76C"+
"B76D"+
"B770"+
"B771"+
"B773"+
"B774"+
"B776"+
"B778"+
"B779"+
"B77A"+
"B77B"+
"B77D"+
"B780"+
"B787"+
"B788"+
"B78C"+
"B78F"+
"B793"+
"B798"+
"B799"+
"B79A"+
"B79C"+
"B7A7"+
"B7AC"+
"B7AE"+
"B7AF"+
"B7B0"+
"B7B2"+
"B7B3"+
"B7B4"+
"B7B5"+
"B7B9"+
"B7BB"+
"B7BD"+
"B7BF"+
"B7C2"+
"B7C6"+
"B7C9"+
"B7CC"+
"B7CE"+
"B7CF"+
"B7D0"+
"B7D1"+"0F"; 

        String expectedSurrogateOutput = 
/* B342 */ "\uD840"+"\uDC0B"+
/* B346 */ "\uD840"+"\uDC89"+
/* B348 */ "\uD840"+"\uDCA2"+
/* B349 */ "\uD840"+"\uDCA4"+
/* B34E */ "\uD840"+"\uDDA2"+
/* B353 */ "\uD840"+"\uDE13"+
/* B35D */ "\uD840"+"\uDF2B"+
/* B360 */ "\uD840"+"\uDF71"+
/* B364 */ "\uD840"+"\uDF81"+
/* B367 */ "\uD840"+"\uDFF9"+
/* B368 */ "\uD841"+"\uDC4A"+
/* B36B */ "\uD841"+"\uDD09"+
/* B370 */ "\uD841"+"\uDDD6"+
/* B373 */ "\uD841"+"\uDE28"+
/* B376 */ "\uD841"+"\uDF4F"+
/* B379 */ "\uD842"+"\uDC07"+
/* B37A */ "\uD842"+"\uDC3A"+
/* B380 */ "\uD842"+"\uDCB9"+
/* B382 */ "\uD842"+"\uDD7C"+
/* B384 */ "\uD842"+"\uDD9D"+
/* B388 */ "\uD842"+"\uDED3"+
/* B389 */ "\uD842"+"\uDF1D"+
/* B38D */ "\uD842"+"\uDF9F"+
/* B39B */ "\uD843"+"\uDD45"+
/* B39F */ "\uD843"+"\uDDE1"+
/* B3A2 */ "\uD843"+"\uDE64"+
/* B3A3 */ "\uD843"+"\uDE6D"+
/* B3A4 */ "\uD843"+"\uDE95"+
/* B3A8 */ "\uD843"+"\uDF5F"+
/* B3B0 */ "\uD844"+"\uDE01"+
/* B3B1 */ "\uD844"+"\uDE3D"+
/* B3B3 */ "\uD844"+"\uDE55"+
/* B3B4 */ "\uD844"+"\uDE74"+
/* B3B5 */ "\uD844"+"\uDE7B"+
/* B3B6 */ "\uD844"+"\uDED7"+
/* B3B7 */ "\uD844"+"\uDEE4"+
/* B3B8 */ "\uD844"+"\uDEFD"+
/* B3BA */ "\uD844"+"\uDF1B"+
/* B3BD */ "\uD844"+"\uDF36"+
/* B3BE */ "\uD844"+"\uDF44"+
/* B3C0 */ "\uD844"+"\uDFC4"+
/* B3C5 */ "\uD845"+"\uDC6D"+
/* B3C6 */ "\uD845"+"\uDC6E"+
/* B3CA */ "\uD845"+"\uDDD7"+
/* B3D0 */ "\uD845"+"\uDE47"+
/* B3D1 */ "\uD845"+"\uDEB4"+
/* B3D2 */ "\uD845"+"\uDF06"+
/* B3D6 */ "\uD845"+"\uDF42"+
/* B3D9 */ "\uD846"+"\uDCBD"+
/* B3DA */ "\uD846"+"\uDDC3"+
/* B3E4 */ "\uD847"+"\uDC56"+
/* B3E9 */ "\uD847"+"\uDD2D"+
/* B3EA */ "\uD847"+"\uDD45"+
/* B3EB */ "\uD847"+"\uDD62"+
/* B3EC */ "\uD847"+"\uDD78"+
/* B3EF */ "\uD847"+"\uDD92"+
/* B3F0 */ "\uD847"+"\uDD9C"+
/* B3F2 */ "\uD847"+"\uDDA1"+
/* B3F4 */ "\uD847"+"\uDDB7"+
/* B3F5 */ "\uD847"+"\uDDE0"+
/* B3F9 */ "\uD847"+"\uDE33"+
/* B3FA */ "\uD847"+"\uDE34"+
/* B444 */ "\uD847"+"\uDF1E"+
/* B446 */ "\uD847"+"\uDF76"+
/* B449 */ "\uD847"+"\uDFFA"+
/* B450 */ "\uD848"+"\uDD7B"+
/* B452 */ "\uD848"+"\uDE18"+
/* B456 */ "\uD848"+"\uDF1E"+
/* B45A */ "\uD848"+"\uDFAD"+
/* B462 */ "\uD849"+"\uDEF3"+
/* B468 */ "\uD84A"+"\uDC5B"+
/* B469 */ "\uD84A"+"\uDCAB"+
/* B46C */ "\uD84A"+"\uDD8F"+
/* B472 */ "\uD84A"+"\uDEB8"+
/* B475 */ "\uD84A"+"\uDF46"+
/* B477 */ "\uD84A"+"\uDF4F"+
/* B478 */ "\uD84A"+"\uDF50"+
/* B479 */ "\uD84A"+"\uDFA6"+
/* B47C */ "\uD84B"+"\uDC1D"+
/* B47D */ "\uD84B"+"\uDC24"+
/* B480 */ "\uD84B"+"\uDDE1"+
/* B491 */ "\uD84C"+"\uDDB6"+
/* B493 */ "\uD84C"+"\uDDC3"+
/* B494 */ "\uD84C"+"\uDDC4"+
/* B496 */ "\uD84C"+"\uDDF5"+
/* B49F */ "\uD84C"+"\uDF72"+
/* B4A1 */ "\uD84C"+"\uDFD0"+
/* B4A2 */ "\uD84C"+"\uDFD2"+
/* B4A3 */ "\uD84C"+"\uDFD3"+
/* B4A4 */ "\uD84C"+"\uDFD5"+
/* B4A5 */ "\uD84C"+"\uDFDA"+
/* B4A7 */ "\uD84C"+"\uDFDF"+
/* B4A8 */ "\uD84C"+"\uDFE4"+
/* B4AF */ "\uD84D"+"\uDC4A"+
/* B4B0 */ "\uD84D"+"\uDC4B"+
/* B4B1 */ "\uD84D"+"\uDC51"+
/* B4B2 */ "\uD84D"+"\uDC65"+
/* B4BF */ "\uD84D"+"\uDCE4"+
/* B4C4 */ "\uD84D"+"\uDD5A"+
/* B4C5 */ "\uD84D"+"\uDD94"+
/* B4CB */ "\uD84D"+"\uDDC4"+
/* B4D2 */ "\uD84D"+"\uDE38"+
/* B4D3 */ "\uD84D"+"\uDE39"+
/* B4D4 */ "\uD84D"+"\uDE3A"+
/* B4D5 */ "\uD84D"+"\uDE47"+
/* B4DC */ "\uD84D"+"\uDF0C"+
/* B4DD */ "\uD84D"+"\uDF1C"+
/* B4DE */ "\uD84D"+"\uDF3F"+
/* B4DF */ "\uD84D"+"\uDF63"+
/* B4E0 */ "\uD84D"+"\uDF64"+
/* B4E3 */ "\uD84D"+"\uDFE7"+
/* B4E5 */ "\uD84D"+"\uDFFF"+
/* B4E7 */ "\uD84E"+"\uDC24"+
/* B4E8 */ "\uD84E"+"\uDC3D"+
/* B4F0 */ "\uD84E"+"\uDE98"+
/* B4F6 */ "\uD84F"+"\uDC7F"+
/* B543 */ "\uD84F"+"\uDCFE"+
/* B544 */ "\uD84F"+"\uDD00"+
/* B545 */ "\uD84F"+"\uDD0E"+
/* B54F */ "\uD84F"+"\uDD40"+
/* B555 */ "\uD84F"+"\uDDD3"+
/* B557 */ "\uD84F"+"\uDDF9"+
/* B558 */ "\uD84F"+"\uDDFA"+
/* B566 */ "\uD84F"+"\uDF7E"+
/* B56C */ "\uD850"+"\uDC96"+
/* B56E */ "\uD850"+"\uDD03"+
/* B573 */ "\uD850"+"\uDDC6"+
/* B574 */ "\uD850"+"\uDDFE"+
/* B57F */ "\uD850"+"\uDFBC"+
/* B584 */ "\uD851"+"\uDE29"+
/* B586 */ "\uD851"+"\uDEA5"+
/* B58E */ "\uD851"+"\uDFF1"+
/* B591 */ "\uD852"+"\uDC96"+
/* B59A */ "\uD852"+"\uDE4D"+
/* B59E */ "\uD852"+"\uDF56"+
/* B59F */ "\uD852"+"\uDF6F"+
/* B5A0 */ "\uD853"+"\uDC16"+
/* B5A4 */ "\uD853"+"\uDD14"+
/* B5AE */ "\uD853"+"\uDE0E"+
/* B5B0 */ "\uD853"+"\uDE37"+
/* B5B2 */ "\uD853"+"\uDE6A"+
/* B5B4 */ "\uD853"+"\uDE8B"+
/* B5B9 */ "\uD854"+"\uDC4A"+
/* B5BA */ "\uD854"+"\uDC55"+
/* B5BB */ "\uD854"+"\uDD22"+
/* B5BD */ "\uD854"+"\uDDA9"+
/* B5BE */ "\uD854"+"\uDDCD"+
/* B5C0 */ "\uD854"+"\uDDE5"+
/* B5C2 */ "\uD854"+"\uDE1E"+
/* B5C4 */ "\uD854"+"\uDE4C"+
/* B5C8 */ "\uD855"+"\uDC2E"+
/* B5CD */ "\uD855"+"\uDC8E"+
/* B5CF */ "\uD855"+"\uDCD9"+
/* B5D0 */ "\uD855"+"\uDD0E"+
/* B5D3 */ "\uD855"+"\uDDA7"+
/* B5E1 */ "\uD855"+"\uDF71"+
/* B5E3 */ "\uD855"+"\uDFA9"+
/* B5E4 */ "\uD855"+"\uDFB4"+
/* B5E9 */ "\uD856"+"\uDDC4"+
/* B5EA */ "\uD856"+"\uDDD4"+
/* B5EE */ "\uD856"+"\uDEE3"+
/* B5EF */ "\uD856"+"\uDEE4"+
/* B5F0 */ "\uD856"+"\uDEF1"+
/* B5F9 */ "\uD856"+"\uDFB2"+
/* B5FC */ "\uD857"+"\uDC4B"+
/* B5FD */ "\uD857"+"\uDC64"+
/* B644 */ "\uD857"+"\uDDA1"+
/* B647 */ "\uD857"+"\uDE2E"+
/* B648 */ "\uD857"+"\uDE56"+
/* B649 */ "\uD857"+"\uDE62"+
/* B64A */ "\uD857"+"\uDE65"+
/* B64B */ "\uD857"+"\uDEC2"+
/* B64C */ "\uD857"+"\uDED8"+
/* B64D */ "\uD857"+"\uDEE8"+
/* B64E */ "\uD857"+"\uDF23"+
/* B64F */ "\uD857"+"\uDF5C"+
/* B651 */ "\uD857"+"\uDFD4"+
/* B652 */ "\uD857"+"\uDFE0"+
/* B654 */ "\uD857"+"\uDFFB"+
/* B656 */ "\uD858"+"\uDC0C"+
/* B657 */ "\uD858"+"\uDC17"+
/* B659 */ "\uD858"+"\uDC60"+
/* B65E */ "\uD858"+"\uDCED"+
/* B663 */ "\uD858"+"\uDE70"+
/* B664 */ "\uD858"+"\uDE86"+
/* B666 */ "\uD858"+"\uDF4C"+
/* B668 */ "\uD859"+"\uDC02"+
/* B672 */ "\uD859"+"\uDE7E"+
/* B675 */ "\uD859"+"\uDEB0"+
/* B67B */ "\uD859"+"\uDF1D"+
/* B681 */ "\uD85A"+"\uDCDD"+
/* B682 */ "\uD85A"+"\uDCEA"+
/* B685 */ "\uD85A"+"\uDD51"+
/* B686 */ "\uD85A"+"\uDD6F"+
/* B689 */ "\uD85A"+"\uDDDD"+
/* B68A */ "\uD85A"+"\uDE1E"+
/* B68E */ "\uD85A"+"\uDE58"+
/* B691 */ "\uD85A"+"\uDE8C"+
/* B692 */ "\uD85A"+"\uDEB7"+
/* B697 */ "\uD85A"+"\uDEFF"+
/* B69E */ "\uD85B"+"\uDC29"+
/* B6A1 */ "\uD85B"+"\uDC73"+
/* B6A4 */ "\uD85B"+"\uDCDD"+
/* B6AB */ "\uD85B"+"\uDE40"+
/* B6AC */ "\uD85B"+"\uDE65"+
/* B6B0 */ "\uD85B"+"\uDF94"+
/* B6B1 */ "\uD85B"+"\uDFF6"+
/* B6B2 */ "\uD85B"+"\uDFF7"+
/* B6B3 */ "\uD85B"+"\uDFF8"+
/* B6B7 */ "\uD85C"+"\uDCF4"+
/* B6B8 */ "\uD85C"+"\uDD0D"+
/* B6BA */ "\uD85C"+"\uDD39"+
/* B6C5 */ "\uD85C"+"\uDFDA"+
/* B6C6 */ "\uD85C"+"\uDFDB"+
/* B6C7 */ "\uD85C"+"\uDFFE"+
/* B6C9 */ "\uD85D"+"\uDC10"+
/* B6CB */ "\uD85D"+"\uDC49"+
/* B6D1 */ "\uD85D"+"\uDE14"+
/* B6D2 */ "\uD85D"+"\uDE15"+
/* B6D4 */ "\uD85D"+"\uDE31"+
/* B6D6 */ "\uD85D"+"\uDE84"+
/* B6D7 */ "\uD85D"+"\uDE93"+
/* B6DA */ "\uD85D"+"\uDF0E"+
/* B6DC */ "\uD85D"+"\uDF23"+
/* B6DD */ "\uD85D"+"\uDF52"+
/* B6E4 */ "\uD85E"+"\uDD85"+
/* B6E7 */ "\uD85E"+"\uDE84"+
/* B6EE */ "\uD85E"+"\uDFB3"+
/* B6EF */ "\uD85E"+"\uDFBE"+
/* B6F0 */ "\uD85E"+"\uDFC7"+
/* B6F1 */ "\uD85F"+"\uDCB8"+
/* B6F4 */ "\uD85F"+"\uDDA0"+
/* B6F7 */ "\uD85F"+"\uDE10"+
/* B6F9 */ "\uD85F"+"\uDFB7"+
/* B6FC */ "\uD860"+"\uDC8A"+
/* B6FD */ "\uD860"+"\uDCBB"+
/* B743 */ "\uD860"+"\uDE77"+
/* B744 */ "\uD860"+"\uDE82"+
/* B747 */ "\uD860"+"\uDEF3"+
/* B74A */ "\uD860"+"\uDFCD"+
/* B74B */ "\uD861"+"\uDC0C"+
/* B74E */ "\uD861"+"\uDC55"+
/* B750 */ "\uD861"+"\uDD6B"+
/* B751 */ "\uD861"+"\uDDC8"+
/* B752 */ "\uD861"+"\uDDC9"+
/* B754 */ "\uD861"+"\uDED7"+
/* B756 */ "\uD861"+"\uDEFA"+
/* B759 */ "\uD862"+"\uDD46"+
/* B75A */ "\uD862"+"\uDD49"+
/* B75D */ "\uD862"+"\uDD6B"+
/* B75E */ "\uD862"+"\uDD87"+
/* B75F */ "\uD862"+"\uDD88"+
/* B760 */ "\uD862"+"\uDDBA"+
/* B761 */ "\uD862"+"\uDDBB"+
/* B764 */ "\uD862"+"\uDE1E"+
/* B765 */ "\uD862"+"\uDE29"+
/* B768 */ "\uD862"+"\uDE43"+
/* B769 */ "\uD862"+"\uDE71"+
/* B76A */ "\uD862"+"\uDE99"+
/* B76B */ "\uD862"+"\uDECD"+
/* B76C */ "\uD862"+"\uDEDD"+
/* B76D */ "\uD862"+"\uDEE4"+
/* B770 */ "\uD862"+"\uDFC1"+
/* B771 */ "\uD862"+"\uDFEF"+
/* B773 */ "\uD863"+"\uDD10"+
/* B774 */ "\uD863"+"\uDD71"+
/* B776 */ "\uD863"+"\uDDFB"+
/* B778 */ "\uD863"+"\uDE1F"+
/* B779 */ "\uD863"+"\uDE36"+
/* B77A */ "\uD863"+"\uDE89"+
/* B77B */ "\uD863"+"\uDEEB"+
/* B77D */ "\uD863"+"\uDF32"+
/* B780 */ "\uD863"+"\uDFF8"+
/* B787 */ "\uD864"+"\uDEA0"+
/* B788 */ "\uD864"+"\uDEB1"+
/* B78C */ "\uD865"+"\uDC90"+
/* B78F */ "\uD865"+"\uDDCF"+
/* B793 */ "\uD865"+"\uDE7F"+
/* B798 */ "\uD865"+"\uDEF0"+
/* B799 */ "\uD865"+"\uDF19"+
/* B79A */ "\uD865"+"\uDF50"+
/* B79C */ "\uD866"+"\uDCC6"+
/* B7A7 */ "\uD866"+"\uDE72"+
/* B7AC */ "\uD867"+"\uDDDB"+
/* B7AE */ "\uD867"+"\uDE15"+
/* B7AF */ "\uD867"+"\uDE3D"+
/* B7B0 */ "\uD867"+"\uDE49"+
/* B7B2 */ "\uD867"+"\uDE8A"+
/* B7B3 */ "\uD867"+"\uDEC4"+
/* B7B4 */ "\uD867"+"\uDEDB"+
/* B7B5 */ "\uD867"+"\uDEE9"+
/* B7B9 */ "\uD867"+"\uDFCE"+
/* B7BB */ "\uD868"+"\uDC1A"+
/* B7BD */ "\uD868"+"\uDC2F"+
/* B7BF */ "\uD868"+"\uDC82"+
/* B7C2 */ "\uD868"+"\uDCF9"+
/* B7C6 */ "\uD868"+"\uDD90"+
/* B7C9 */ "\uD868"+"\uDF8C"+
/* B7CC */ "\uD869"+"\uDC37"+
/* B7CE */ "\uD869"+"\uDDF1"+
/* B7CF */ "\uD869"+"\uDE02"+
/* B7D0 */ "\uD869"+"\uDE1A"+
/* B7D1 */ "\uD869"+"\uDEB2";

        if (!testEbcdicToUnicode(inputSurrogateBytes, expectedSurrogateOutput, 1399, sb)) {
          passed = false; 
        }

        
        
        
        
        /* Test EBCDIC 1399 to unicode conversions with combining characters */ 
  
        sb.append("EBCDIC 1399 TO UNICODE\n"); 
        String inputCombiningBytes =   "0e"+ 
            "ECB5"+
            "ECB6"+
            "ECB7"+
            "ECB8"+
            "ECB9"+
            "ECBA"+
            "ECBB"+
            "ECBC"+
            "ECBD"+
            "ECBE"+
            "ECBF"+
            "ECC0"+
            "ECC1"+
            "ECC2"+
            "ECC3"+
            "ECC4"+
            "ECC5"+
            "ECC8"+
            "ECC9"+
            "ECCA"+
            "ECCB"+
            "ECC6"+
            "ECC7"+
            "ECCD"+
            "ECCC" +
            "0F";
        

        String expectedCombiningOutput = 
        /* ECB5 */         "\u304B"+"\u309A"+
        /* ECB6 */         "\u304D"+"\u309A"+
        /* ECB7 */         "\u304F"+"\u309A"+
        /* ECB8 */         "\u3051"+"\u309A"+
        /* ECB9 */         "\u3053"+"\u309A"+
        /* ECBA */         "\u30AB"+"\u309A"+
        /* ECBB */         "\u30AD"+"\u309A"+
        /* ECBC */         "\u30AF"+"\u309A"+
        /* ECBD */         "\u30B1"+"\u309A"+
        /* ECBE */         "\u30B3"+"\u309A"+
        /* ECBF */         "\u30BB"+"\u309A"+
        /* ECC0 */         "\u30C4"+"\u309A"+
        /* ECC1 */         "\u30C8"+"\u309A"+
        /* ECC2 */         "\u31F7"+"\u309A"+
        /* ECC3 */         "\u00E6"+"\u0300"+
        /* ECC4 */         "\u0254"+"\u0300"+
        /* ECC5 */         "\u0254"+"\u0301"+
        /* ECC8 */         "\u0259"+"\u0300"+
        /* ECC9 */         "\u0259"+"\u0301"+
        /* ECCA */         "\u025A"+"\u0300"+
        /* ECCB */         "\u025A"+"\u0301"+
        /* ECC6 */         "\u028C"+"\u0300"+
        /* ECC7 */         "\u028C"+"\u0301"+
        /* ECCD */         "\u02E5"+"\u02E9"+
        /* ECCC */         "\u02E9"+"\u02E5";  

        if (!testEbcdicToUnicode(inputCombiningBytes, expectedCombiningOutput, 1399, sb)) {
          passed = false; 
        }
        

        sb.append("\nUNICODE TO EBCDIC 1399 Surrogate\n");
        String surrogateInput = expectedSurrogateOutput; 
        String surrogateOutput = inputSurrogateBytes; 
        if (! testUnicodeToEbcdic(surrogateInput,
            surrogateOutput,
            1399, sb)) {
          passed = false; 
        }

        sb.append("\nUNICODE TO EBCDIC 1399 combining\n");
        String combiningInput = expectedCombiningOutput; 
        String combiningOutput = inputCombiningBytes; 
        if (! testUnicodeToEbcdic(combiningInput,
            combiningOutput,
            1399, sb)) {
          passed = false; 
        }
        
        

        
        
        assertCondition(passed, sb); 
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    
  }
  /**
    Verifies internal tables for ccsid 1399.
    Result: Variation should be successful.
  **/
  public void Var003()
  {
    verify1399(); 
  }

  /**
    Verifies internal tables for ccsid 5026.
    Result: Variation should be successful.
  **/
  public void Var004()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable5026();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if ((sbCcsid != 290) && (sbCcsid != 291))
      {
        failed("Incorrect internal single-byte table for ccsid 5026: " + sbCcsid + " != 290 ");
      }
      else if ((dbCcsid != 16684) && (dbCcsid != 2000930))
      {
        failed("Incorrect internal double-byte table for ccsid 5026: " + dbCcsid + " != 16684 or 2000930");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 5035.
    Result: Variation should be successful.
  **/
  public void Var005()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable5035();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if (sbCcsid != 5123)
      {
        failed("Incorrect internal single-byte table for ccsid 5035: " + sbCcsid + " != 5123");
      }
      else if ((dbCcsid != 16684) && (dbCcsid != 2000930))
      {
        failed("Incorrect internal double-byte table for ccsid 5035: " + dbCcsid + " != 16684 or 2000930");
      }
      else
      {
        boolean passed = true; 
        StringBuffer sb = new StringBuffer(); 
        // Verify the following translations from CPS 9ERBJK
        /* 
        Unicode => EBCDIC Conversion.                                            
            +---------+----------------------------+                                 
            | UNICODE |   TARGET EBCDIC RESULT     |                                 
            | SOUCRE  |     IBM i    |    JT400    |                                 
            |         | 5035 | 1399  | 5035 | 1399 |                                 
            +---------+------+-------+------|------+                                 
            | 2212    | 4260 | 4260  | 4260 | 4260 |                                 
            | FF0D    | 4260 | E9F3  | 4260 | 4260*|                                 
            | 2014    | 444A | 444A  | 444A | 444A |                                 
            | 2015    | 444A | DDB7  | 444A | 444A*|                                 
            | 301C    | 43A1 | 43A1  | 43A1 | 43A1 |                                 
            | FF5E    | 43A1 | E9F4  | 43A1 | 43A1*|                                 
            | 2016    | 447C | 447C  | 447C | 447C |                                 
            | 2225    | 447C | DFE5  | 447C | 447C*|                                 
            | 00A6    | 426A | 426A  | 426A | 426A |                                 
            | FFE4    | 426A | E9F5  | 426A | 426A*|                                 
                                                                                     
                                            */
        
        sb.append("\nUNICODE TO EBCDIC 5035 \n"); 
        if (! testUnicodeToEbcdic(
               "\uff0d"+"\u2212"+"\u2014"+"\u2015"+"\u301c"+"\uff5e"+"\u2016"+"\u2225"+"\u00a6"+"\uffe4",
            "0e"+"4260"  +"4260"  +"444a"  +"444a"  +"43a1"  +"43a1"  +"447c"  +"447c"  +"426a"  +"426a"+"0f", 5035, sb)) {
          passed = false; 
        }
        
        /*
            EBCDIC => Unicode Conversion.                                            
                                                                                     
            +---------+----------------------------+                                 
            | EBCDIC  |   TARGET UNICODE RESULT    |                                 
            | SOUCRE  |     IBM i    |    JT400    |                                 
            |         | 5035 | 1399  | 5035 | 1399 |                                 
            +---------+------+-------+------|------+                                 
            | 4260    | FF0D | 2212  | FF0D | FF0D*|                                 
            | E9F3    | FF0D | FF0D  | FF0D | FF0D |                                 
            | 444A    | 2015 | 2014  | 2015 | 2015*|                                 
            | DDB7    | 2015 | 2015  | 2015 | 2015 |                                 
            | 43A1    | FF5E | 301C  | FF5E | FF5E*|                                 
            | E9F4    | FF5E | FF5E  | FF5E | FF5E |                                 
            | 447C    | 2225 | 2016  | 2225 | 2225*|                                 
            | DFE5    | 2225 | 2225  | 2225 | 2225 |                                 
            | 426A    | FFE4 | 00A6  | FFE4 | FFE4*|                                 
            | E9F5    | FFE4 | FFE4  | FFE4 | FFE4 |  
        
        */
        sb.append("EBCDIC 5035 TO UNICODE\n"); 
        String inputBytes =   "0E"+"4260"+  "e9f3"+  "444a"+  "ddb7"+  "43a1"+  "e9f4"+  "447c"+  "dfe5"+  "426A"+  "e9f5"+"0f";
        String expectedOutput = "\uff0d"+"\uff0d"+"\u2015"+"\u2015"+"\uff5e"+"\uff5e"+"\u2225"+"\u2225"+"\uffe4"+"\uffe4"; 
        if (!testEbcdicToUnicode(inputBytes, expectedOutput, 5035, sb)) {
          passed = false; 
        }
        
        assertCondition(passed, sb); 

      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 930.
    Result: Variation should be successful.
  **/
  public void Var006()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable930();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if ((sbCcsid != 290) && (sbCcsid != 1000930))
      {
        failed("Incorrect internal single-byte table for ccsid 930: " + sbCcsid + " != 1000930");
      }
      else if ((dbCcsid != 16684) && (dbCcsid != 2000930))
      {
        failed("Incorrect internal single-byte table for ccsid 930: " + dbCcsid + " != 16684");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 933.
    Result: Variation should be successful.
  **/
  public void Var007()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable933();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if (sbCcsid != 833)
      {
        failed("Incorrect internal single-byte table for ccsid 933: " + sbCcsid + " != 833");
      }
      else if (dbCcsid != 834)
      {
        failed("Incorrect internal single-byte table for ccsid 933: " + dbCcsid + " != 834");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 935.
    Result: Variation should be successful.
  **/
  public void Var008()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable935();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if (sbCcsid != 836)
      {
        failed("Incorrect internal single-byte table for ccsid 935: " + sbCcsid + " != 836");
      }
      else if ((dbCcsid != 837) && (dbCcsid != 2001388))
      {
        failed("Incorrect internal single-byte table for ccsid 935: " + dbCcsid + " != 837 or 2001388 ");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 937.
    Result: Variation should be successful.
  **/
  public void Var009()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable937();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if (sbCcsid != 37)
      {
        failed("Incorrect internal single-byte table for ccsid 937: " + sbCcsid + " != 37");
      }
      else if (dbCcsid != 835)
      {
        failed("Incorrect internal single-byte table for ccsid 937: " + dbCcsid + " != 835");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Verifies internal tables for ccsid 939.
    Result: Variation should be successful.
  **/
  public void Var010()
  {
    try
    {
      ConvTableMixedMap c = new ConvTable939();
      int sbCcsid = c.sbTable_.getCcsid();
      int dbCcsid = c.dbTable_.getCcsid();
      if (sbCcsid != 5123) // 939 is a subset of 1399, so its sb table is 5123, not 1027.
      {
        failed("Incorrect internal single-byte table for ccsid 939: " + sbCcsid + " != 5123");
      }
      else if ((dbCcsid != 16684) && (dbCcsid != 2000930)) // 939 is a subset of 1399, so its db table is 16684, not 300.
      {
        failed("Incorrect internal double-byte table for ccsid 939: " + dbCcsid + " != 16684 or 2000930");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
  Verifies internal tables for ccsid 1399.
  Result: Variation should be successful.
  Note:  Verifying again for CCSID 1399.   CPS 9ERBJK noted a case
  where loading 5035 causes this translation to be incorrect. 
**/
public void Var011()
{
  verify1399(); 
}

}
