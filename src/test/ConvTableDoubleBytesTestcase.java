///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableDoubleBytesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import test.ConvTest;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvTableDoubleBytesTestcase.
 Test internal converter table contents for all ConvTableDoubleMap converter tables.
 **/
public class ConvTableDoubleBytesTestcase extends Testcase
{
    boolean isProxified_ = false;

    /**
     Runs the variations requested.
     Loops through all of the double-byte EBCDIC CCSIDs the Toolbox supports and tests their contents against the same tables downloaded from the server.
     Variations should be successful.
     **/
    public void run()
    {
        totalVariations_ = ConvTest.doubleCcsids_.length;

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
                          // Class c = Class.forName("com.ibm.as400.access.ConvTable" + 
                          // ConvTest.doubleCcsids_[i - 1]);
                          // ConvTableDoubleMap m = (ConvTableDoubleMap)c.newInstance();
                            ConvTableDoubleMap m = (ConvTableDoubleMap)ConvTable.getTable(
                                ConvTest.doubleCcsids_[i - 1], (AS400ImplRemote) systemObject_.getImpl()); 

                            compareTables(m);
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
    public char[] downloadDoubleByteToUnicodeTable(int ccsid) throws Exception
    {
        AS400ImplRemote impl = (AS400ImplRemote)systemObject_.getImpl();

        NLSTableDownload down = new NLSTableDownload(impl);
        down.connect();
        int originalCcsid = ccsid; 
        if (ccsid == 300) {
            // Use 16684 for 300 (it is superset and toolbox implementated uses it 
            ccsid = 16684; 
        }
	// For most CCSIDs use 1200 
	int unicodeCcsid = 1200; 
        char[] table = down.download(ccsid, 
                                    unicodeCcsid,  
                                    true);

        //
        down.disconnect();
	// CCSID 61952 is AS/400 specific UCS level 2
	// Don't need to fix up if the table length is <= 65536
	int baseSize = 65536; 

  if (originalCcsid == 300) {
    // Now fix up the code points that are different  @N4A
    table[0x4260] = '\uFF0D';
    table[0x426A] = '\uFFE4';
    table[0x43A1] = '\uFF5E';
    table[0x444A] = '\u2015';
    table[0x447C] = '\u2225';
  }

  if (originalCcsid ==  61952) {
      if (table.length>0xd801) { 
	  table[0xd800] = '\uFFFD'; 
	  table[0xd801] = '\uFFFD';
      } else {
	  System.out.println("Warning:  table length for CCSID 61952 is "+table.length);
      } 
  }
	if (ccsid == 61952 || table.length <= baseSize) {
	    return table; 
	} else {

	    // Loop through and record two byte translations 
	    int next = 0; 
	    int from = 0;
	    char[] newTable = new char[baseSize * 2]; 
	    while (from < table.length && next < baseSize) {
		int c = 0xFFFF & (int) table[from]; 
		int c2 = 0;
		if (from + 1 < table.length) { 
		  c2 = 0xFFFF & (int) table[from+1]; 
		}
		if (((c >= 0xD800) && (c <=0xDFFF)) ||
		    (c2 == 0x309a) ||
		    (c != 0xfffd && c2 == 0x0300) ||
		    (c != 0x300 && c2 == 0x0301) || 
		    (c == 0x2E9 && c2 == 0x2e5) || 
		    (c == 0x2e5 && c2 == 0x2E9)) {
		  
		    newTable[next]=(char) c;
		    from ++;
		    c = 0xFFFF & (int) table[from];
		    newTable[baseSize+next] = (char) c; 
		    from ++; 
		} else {
		    newTable[next]=(char) c; 
		    from++; 
		}
		next++; 
	    }


	    return newTable;
	}
    }

    /**
     Helper method used to download the ebcdic converter table from the server.
     **/
    public char[] downloadDoubleByteFromUnicodeTable(int ccsid) throws Exception
    {
        AS400ImplRemote impl = (AS400ImplRemote)systemObject_.getImpl();

        NLSTableDownload down = new NLSTableDownload(impl);
        down.connect();
        int originalCcsid = ccsid; 
        if (ccsid == 300) {
          // Use 16684 for 300 (it is superset and toolbox implementated uses it 
          ccsid = 16684; 
        }
        int unicodeCcsid = 1200; 
        char[] table = down.download(unicodeCcsid , ccsid, true);

        down.disconnect();
        // Fixup the table 
        if (originalCcsid == 300) { 
          table[0x2015] = '\u444A';
          table[0x2225] = '\u447C';
          
          table[0x525d] = '\u5481';
          table[0x5c5b] = '\u5443';
          table[0x7c1e] = '\u54ca';
          table[0x87ec] = '\u53e8';
          table[0x9a52] = '\u53da';
          
          table[0xFF0D] = '\u4260';
          table[0xFF5E] = '\u43A1';
          table[0xFFE4] = '\u426A';

          
        }
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
    public void compareTables(ConvTableDoubleMap c) 
    {
      StringBuffer sb = new StringBuffer();
      boolean passed = true; 
      try { 
        int ccsid = c.getCcsid();

        switch(ccsid)
        {
            case 4396: // 4396 is not translatable to/from Unicode on the 400 for some reason.
                ccsid = 300; 
                break; 
                
            case 12588: // The rest of the ccsids are either old or new, but probably not supported
            case 8492:  // on the 400 you're running to, so just use their parent ccsid.
                ccsid = 16684;
                break;

            case 13122:
            case 17218:
            case 9026:
                ccsid = 4930;
                break;

            case 4931:
                ccsid = 835;
                break;
            case 9029:
                ccsid = 4933;
                break;

            default:
                break;
        }

        // First test the toUnicode table.
        // char[] established = c.getToUnicode();
        // We no longer use the table but we translate directly
        
        char[] downloaded = downloadDoubleByteToUnicodeTable(ccsid);
        /* 
        if (established.length != generated.length)
        {
            sb.append("EBCDIC->Unicode table length does not match OS/400's for ccsid " + ccsid + "("+c.getCcsid()+"): " + established.length + " != downloaded:" + generated.length+"\n");
            passed=false;
        }
        */ 

	int tolerated837[][] = {
	    {0xE816, 0xFFFD},
	    {0xE817,0xFFFD},
	    {0xE818,0xFFFD},
	    {0xE831,0xFFFD},
	    {0xE83B,0xFFFD},
	    {0xE855,0xFFFD},
	};

        StringBuffer charFailures = new StringBuffer();
        int charFailuresBaseLength=0; 
        StringBuffer charWarnings = new StringBuffer();
        int loopsize = 65535;
        
        boolean noHeader =true;
        char[]  fromEbcdic = new char[10]; 

        for (int i = 0x100; i < loopsize; ++i) {
            int est;
            int est2 = 0; 
            int len = c.toUnicode(fromEbcdic, 0, i);
            boolean characterOk = true; 
            est = fromEbcdic[0];  
            int gen;
            int gen2 = 0;
            if (i < downloaded.length) {
              gen = (int)(downloaded[i] & 0xFFFF);
            } else {
              gen = -1; 
            }
            if (len > 1) {
              est2 = fromEbcdic[1];
              gen2 = downloaded[i+65536]; 
              if ( est2 != gen2) {
                characterOk = false; 
              }
            }

            if (est != gen) { 
              characterOk = false;
	      // Check for tolerated errors 
	      if (ccsid == 837 || ccsid == 4933 || ccsid == 9029 ) {
		  for (int j = 0; j < tolerated837.length; j++) {
		      if (est == tolerated837[j][0] &&
			  gen == tolerated837[j][1]) {
			  characterOk=true; 
		      }
		  }
	      }
            }
            if (!characterOk) {
                if (noHeader) {
                  noHeader = false;
                  charFailures.append("To UNICODE\n"); 
                  charFailures.append("Index :{TOOLBOX    ,DOWNLOADED}\n");
                  charFailuresBaseLength = charFailures.length(); 
                }
                if (est == 0xFFFD) {
                  charWarnings.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
                } else {
                  if (est2 != 0) { 
                    charFailures.append(hex(i) + ":{'" + hex(est) +" "+hex(est2) + "','" + hex(gen) +" "+hex(gen2)+ "'}\n");
                  } else { 
                    charFailures.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
                  }
                }
                
            }
        } /* for */ 
        if (charFailures.length() > charFailuresBaseLength)  {
          sb.append("ERROR:  EBCDIC->Unicode table characters do not match OS/400's for ccsid " + ccsid + "("+c.getCcsid()+ "):\n" + charFailures.toString()+"\n");

          passed=false;
	    charFailures.setLength(0);
	    charFailuresBaseLength = 0; 
        }

        if (charWarnings.length() > 0)
        {
	    sb.append("WARNING:  EBCDIC->Unicode table characters do not match OS/400's for ccsid " + ccsid + "("+c.getCcsid()+ "):\n" + charWarnings.toString()+"\n");

	    passed=false;
	    charWarnings.setLength(0); 
        }


        // Now test the fromUnicode table.
        char [] established = c.getFromUnicode();
        downloaded = downloadDoubleByteFromUnicodeTable(ccsid);
        if (established.length != downloaded.length)
        {
	    sb.append("Unicode->EBCDIC table length does not match OS/400's for ccsid " + ccsid+ "("+c.getCcsid()+ "): " + established.length + " != downloaded:" + downloaded.length+"\n");
            passed = false; 
        }
        charFailures = new StringBuffer();
	charFailuresBaseLength = 0; 
        charWarnings = new StringBuffer();
	loopsize = established.length;
	if (downloaded.length > loopsize) loopsize = downloaded.length; 
	sb.append("Unicode->EBCDIC\n");

	for (int i = 0; i < loopsize; ++i)         {
	  int est;
	  if (i < established.length) { 
	    est = (int)(established[i] & 0xFFFF);
	  } else {
	    est = -1;
	  }
	  int gen;
	  if (i < downloaded.length) { 
	    gen = (int)(downloaded[i] & 0xFFFF);
	  } else {
	    gen = -1; 
	  }
	    /* Only test for non surrogate range */
	  if ((i < 0xD800 || i > 0xDFFF)  && (est != gen)) {
                
	    if (est == 0xFEFE) { 
	        charWarnings.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
	    } else if (gen == 0xFEFE) {
		      charWarnings.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
	    } else { 
		      charFailures.append(hex(i) + ":{'" + hex(est) + "','" + hex(gen) + "'}\n");
	    }
	  }
	}
        if (charFailures.length() > charFailuresBaseLength)
        {
	        passed = false; 
	        sb.append("ERROR:  Unicode->EBCDIC table characters do not match OS/400's for ccsid " + ccsid+ "("+c.getCcsid()+ "):\n" + charFailures.toString()+"\n");
        }
        if (charWarnings.length() > 0)
        {
          sb.append("WARNING:  Unicode->EBCDIC table characters do not match OS/400's for ccsid " + ccsid+ "("+c.getCcsid()+ "):\n" + charWarnings.toString()+"\n");
        }

	assertCondition(passed, sb.toString()); 
      } catch (Exception e) { 
        failed(e, "passed="+passed+" "+sb.toString()); 
      }
    }
}
