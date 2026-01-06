///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConversionMapTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException; 
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;

import test.ConvTest;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConversionMapTestcase.
 Verifies all possible encodings used in the ConversionMaps class.
 **/
public class ConversionMapTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConversionMapTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Verifies all of the ccsid entries in the ccsidEncoding table.
     Result: Variation should be successful.
     **/
    @SuppressWarnings("unchecked")
    public void Var001()
    {
        int ccsid = -1;
        try
        {
            Hashtable<String,String> table = ConversionMaps.ccsidEncoding_;
            Enumeration<String> enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                String ccsidStr = (String)enumeration.nextElement();
                ccsid = Integer.parseInt(ccsidStr);
                try
                {
                    Converter c = new Converter(ccsid);
                    if (!c.getEncoding().equals(table.get(ccsidStr)))
                    {
                        failed("Incorrect encoding returned for ccsid " + ccsid + ": " + c.getEncoding() + " != " + table.get(ccsidStr));
                        return;
                    }
                }
                catch (UnsupportedEncodingException uee)
                {
                    output_.println("Warning: JVM does not support CCSID/encoding " + ccsidStr + "/" + table.get(ccsidStr) + ".");
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception while testing converter for ccsid " + ccsid + ".");
        }
    }

    /**
     Verifies all of the encoding entries in the ccsidEncoding table.
     Result: Variation should be successful.
     **/
    public void Var002()
    {
        String encoding = "";
        try
        {
            @SuppressWarnings("unchecked")
            Hashtable<String,String> table = ConversionMaps.ccsidEncoding_;
            Enumeration<String> enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                String key = (String)enumeration.nextElement();
                encoding = (String)table.get(key);
                try
                {
                    Converter c = new Converter(encoding);
                    int ccsid = Integer.parseInt(key);
                    if (c.getCcsid() != ccsid)
                    {
                        if (encoding.equals("UTF-16BE") && c.getCcsid() == 1200 && ccsid == 17584)
                        {
                            // 1200 prefered to 17584.
                        }
                        else
                        {
                            failed("Incorrect ccsid returned for encoding " + encoding + ": " + c.getCcsid() + " != " + ccsid);
                            return;
                        }
                    }
                }
                catch (UnsupportedEncodingException uee)
                {
                    output_.println("Warning: JVM does not support CCSID/encoding " + key + "/" + encoding + ".");
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception while testing converter for encoding " + encoding + ".");
        }
    }

    /**
     Verifies all of the ccsid entries in the encodingCcsid table.
     Note: the encodingCcsid table may have several names for each CCSID, so the there is
     no longer a one to one mapping between the two. 
     Result: Variation should be successful.
     **/
    public void Var003()
    {
        int ccsid = -1;
        try
        {
           boolean passed = true; 
           StringBuffer sb = new StringBuffer(); 
            @SuppressWarnings("unchecked")
            Hashtable<String,String> table = ConversionMaps.encodingCcsid_;
            Enumeration<String> enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                String key = (String)enumeration.nextElement();
                String ccsidStr = (String)table.get(key);
                ccsid = Integer.parseInt(ccsidStr);
                try
                {
                    Converter c = new Converter(ccsid);
                    String converterEncoding = c.getEncoding(); 
                    String enc = converterEncoding; 
                    // Use the JVM to get the canonical name for the encoding. 
                    Charset charset = Charset.forName(key); 
                    String canonicalName = charset.name();
                    if (!converterEncoding.equals(canonicalName))
                    {
                        String[][] equivalentCombos = {
                          {"GB2312","x-IBM1381"},
                          {"windows-31j","x-IBM943"},
                          {"Big5","x-IBM950"},
                       };
                       boolean equivalent=false; 
                       for (int i = 0; i < equivalentCombos.length; i++) { 
                         if (canonicalName.equals(equivalentCombos[i][0]) &&
                             enc.equals(equivalentCombos[i][1])) {
                           equivalent = true; 
                         }
                       }
                      // Some combinations are equivalent
                      if (! equivalent) {
                        passed = false;
                        sb.append("For key=" + key + " ccsid=" + ccsid + " canonicalName(" + canonicalName
                            + ") != converterEncoding(" + converterEncoding + ")\n");
                      }
                    }
                }
                catch (UnsupportedEncodingException uee)
                {
                    output_.println("Warning: JVM does not support CCSID/encoding " + ccsidStr + "/" + key + ".");
                }
                catch (UnsupportedCharsetException uee)
                {
                    output_.println("Warning: JVM does not support CCSID/encoding " + ccsidStr + "/" + key + ".");
                }
            }
            assertCondition(passed, sb);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception while testing converter for ccsid " + ccsid + ".");
        }
    }

    /**
     Verifies all of the encoding entries in the encodingCcsid table.
     Result: Variation should be successful.
     **/
    public void Var004()
    {
        String encoding = "";
        try
        {
            @SuppressWarnings("unchecked")
            Hashtable<String,String> table = ConversionMaps.encodingCcsid_;
            Enumeration<String> enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                encoding = (String)enumeration.nextElement();
                try
                {
                    Converter c = new Converter(encoding);
                    int ccsid = Integer.parseInt((String)table.get(encoding));
                    if (c.getCcsid() != ccsid)
                    {
                        failed("Incorrect ccsid returned for encoding " + encoding + ": " + c.getCcsid() + " != " + ccsid);
                        return;
                    }
                }
                catch (UnsupportedEncodingException uee)
                {
                    output_.println("Warning: JVM does not support CCSID/encoding " + table.get(encoding) + "/" + encoding + ".");
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception while testing converter for encoding " + encoding + ".");
        }
    }

    /**
     Verifies all of the entries in the localeCcsid table.
     Result: Variation should be successful.
     **/
    public void Var005()
    {
        Locale saveDefault = Locale.getDefault();
        String localeStr = "";
        try
        {
            boolean passed = true; 
            StringBuffer sb = new StringBuffer(); 
            @SuppressWarnings("unchecked")
            Hashtable<String,String> table = ConversionMaps.localeCcsidMap_;
            Enumeration<String> enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                localeStr = (String)enumeration.nextElement();
                StringTokenizer st = new StringTokenizer(localeStr, "_");
                Locale loc = null;
                switch (st.countTokens())
                {
                    case 2:
                        loc = new Locale(st.nextToken(), st.nextToken());
                        break;
                    case 3:
                        loc = new Locale(st.nextToken(), st.nextToken(), st.nextToken());
                        break;
                    default:
                        loc = new Locale(localeStr, "");
                        break;
                }
                Locale.setDefault(loc);
                Converter c = new Converter();
                int ccsid = Integer.parseInt((String)table.get(localeStr));
                if (c.getCcsid() != ccsid)
                {
                    sb.append("Incorrect ccsid returned for locale " + localeStr + ": " + c.getCcsid() + " != " + ccsid+"\n");
                    passed = false; 
                }
            }
            assertCondition(passed, sb );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception while testing converter for locale " + localeStr + ".");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }

    /**
     Verifies all of the entries in the localeNlv table.
     Result: Variation should be successful.
     **/
    @SuppressWarnings("unchecked")
    public void Var006()
    {
        Locale saveDefault = Locale.getDefault();
        String localeStr = "";
        try
        {
          boolean passed = true; 
          StringBuffer sb = new StringBuffer(); 
            Hashtable<String,String> table = ConversionMaps.localeNlvMap_;
            Enumeration<String> enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                localeStr = (String)enumeration.nextElement();
                StringTokenizer st = new StringTokenizer(localeStr, "_");
                Locale loc = null;
                switch (st.countTokens())
                {
                    case 2:
                        loc = new Locale(st.nextToken(), st.nextToken());
                        break;
                    case 3:
                        loc = new Locale(st.nextToken(), st.nextToken(), st.nextToken());
                        break;
                    default:
                        loc = new Locale(localeStr, "");
                        break;
                }
                Locale.setDefault(loc);
                // Converter c = new Converter();
                int nlv = Integer.parseInt((String)table.get(localeStr));
                int ee = Integer.parseInt(ExecutionEnvironment.getNlv(Locale.getDefault()));
                if (ee != nlv)
                {
                    sb.append("Incorrect nlv returned for locale " + localeStr + ": " + ee + " != " + nlv+"\n");
                    passed=false; 
                }
            }
            assertCondition(passed, sb); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception while testing converter for locale " + localeStr + ".");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }


    /**
     Verifies all of the ConvTable ccsids in the Toolbox are in the encodingCcsid table;
     if one is not, it should match the encoding for that particular ConvTable.
     Result: Variation should be successful.
     **/
    public void Var007()
    {
        try
        {
          boolean passed = true; 
          StringBuffer sb = new StringBuffer(); 
            for (int i = 0; i < ConvTest.allCcsids_.length; ++i)
            {
                // Class c = Class.forName("com.ibm.as400.access.ConvTable" + ConvTest.allCcsids_[i]);
                // ConvTable t = (ConvTable)c.newInstance();
                ConvTable  t = (ConvTable)ConvTable.getTable(
                    ConvTest.allCcsids_[i], (AS400ImplRemote) systemObject_.getImpl()); 

                String tableEncoding = t.getEncoding();
                String tableCcsid = t.getCcsid() + "";

                String lookupCcsid = (String)ConversionMaps.encodingCcsid_.get(tableEncoding);

                if (lookupCcsid == null)
                {
                    if (!tableCcsid.equals(tableEncoding))
                    {
                        sb.append("Incorrect ccsid/encoding match: " + tableCcsid + " != " + tableEncoding+"\n");
                        passed = false; 
                    }
                }
                else
                {
                    if (!lookupCcsid.equals(tableCcsid))
                    {
                        if (tableEncoding.equals("UTF-16BE") && lookupCcsid.equals("1200") && tableCcsid.equals("17584"))
                        {
                            // 1200 prefered to 17584.
                        }
                        else
                        {
                            sb.append("Incorrect ccsid match for encoding " + tableEncoding + ": " + lookupCcsid + " != " + tableCcsid+"\n");
                            passed = false; 
                          
                        }
                    }
                }
            }
            assertCondition(passed, sb); 
       }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verifies a Java encoding (for which the Toolbox does not have a mapping) returns a ccsid of 0.
     Result: Variation should be successful.
     **/
    public void Var008()
    {
        try
        {
            ConvTable t = ConvTable.getTable("Cp942C");
            if (t.getCcsid() != 0)
            {
                failed("Incorrect ccsid returned for Cp942C encoding: " + t.getCcsid());
            }
            else if (!t.getEncoding().equals("Cp942C"))
            {
                failed("Incorrect encoding returned for Cp942C: " + t.getEncoding());
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
}
