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
    public void Var001()
    {
        int ccsid = -1;
        try
        {
            Hashtable table = ConversionMaps.ccsidEncoding_;
            Enumeration enumeration = table.keys();
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
            Hashtable table = ConversionMaps.ccsidEncoding_;
            Enumeration enumeration = table.keys();
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
     Result: Variation should be successful.
     **/
    public void Var003()
    {
        int ccsid = -1;
        try
        {
            Hashtable table = ConversionMaps.encodingCcsid_;
            Enumeration enumeration = table.keys();
            while (enumeration.hasMoreElements())
            {
                String key = (String)enumeration.nextElement();
                String ccsidStr = (String)table.get(key);
                ccsid = Integer.parseInt(ccsidStr);
                try
                {
                    Converter c = new Converter(ccsid);
                    if (!c.getEncoding().equals(key))
                    {
                        // Some encodings map to the same ccsid
                        String enc = c.getEncoding();
                        if ((enc.equals("Cp874") && key.equals("MS874")) ||
                            (enc.equals("MS874") && key.equals("Cp874")) ||
                            (enc.equals("Cp874") && key.equals("TIS620")) ||
                            (enc.equals("TIS620") && key.equals("Cp874")) ||
                            (enc.equals("TIS620") && key.equals("MS874")) ||
                            (enc.equals("MS874") && key.equals("TIS620")) ||
                            (enc.equals("Cp950") && key.equals("MS950")) ||
                            (enc.equals("MS950") && key.equals("Cp950")) ||
                            (enc.equals("Cp950") && key.equals("Big5")) ||
                            (enc.equals("MS950") && key.equals("Big5")) ||
                            (enc.equals("Big5") && key.equals("MS950")) ||
                            (enc.equals("Big5") && key.equals("Cp950")) ||
                            (enc.equals("MS950") && key.equals("Big5")) ||
                            (enc.equals("Cp950") && key.equals("Big5")) ||
                            (enc.equals("EUC_CN") && key.equals("Cp1383")) ||
                            (enc.equals("Cp1383") && key.equals("EUC_CN")) ||
                            (enc.equals("Cp949") && key.equals("MS949")) ||
                            (enc.equals("MS949") && key.equals("Cp949")) ||
                            (enc.equals("Cp949") && key.equals("KSC5601")) ||
                            (enc.equals("KSC5601") && key.equals("Cp949")) ||
                            (enc.equals("KSC5601") && key.equals("MS949")) ||
                            (enc.equals("MS949") && key.equals("KSC5601")) ||
                            (enc.equals("Cp943") && key.equals("MS932")) ||
                            (enc.equals("MS932") && key.equals("Cp943")) ||
                            (enc.equals("Cp943") && key.equals("SJIS")) ||
                            (enc.equals("SJIS") && key.equals("Cp943")) ||
                            (enc.equals("SJIS") && key.equals("MS932")) ||
                            (enc.equals("MS932") && key.equals("SJIS")) ||
                            (enc.equals("MS936") && key.equals("GBK")) ||
                            (enc.equals("GBK") && key.equals("MS936")) ||
                            (enc.equals("Cp970") && key.equals("EUC_KR")) ||
                            (enc.equals("EUC_KR") && key.equals("Cp970")) ||
                            (enc.equals("Cp964") && key.equals("EUC_TW")) ||
                            (enc.equals("EUC_TW") && key.equals("Cp964")) ||
                            (enc.equals("CNS11643") && key.equals("EUC_TW")) ||
                            (enc.equals("EUC_TW") && key.equals("CNS11643")) ||
                            (enc.equals("Cp964") && key.equals("CNS11643")) ||
                            (enc.equals("CNS11643") && key.equals("Cp964")) ||
                            (enc.equals("Cp33722") && key.equals("EUC_JP")) ||
                            (enc.equals("EUC_JP") && key.equals("Cp33722")) ||
                            (enc.equals("Cp1381") && key.equals("GB2312")) ||
                            (enc.equals("GB2312") && key.equals("Cp1381")) ||
                            (enc.equals("Unicode") && key.equals("UnicodeBig")) ||
                            (enc.equals("UTF-8") && key.equals("UTF8")) ||
                            (enc.equals("UnicodeBig") && key.equals("Unicode")))
                        {
                        }
                        else
                        {
                            failed("Incorrect encoding returned for ccsid " + ccsid + ": " + c.getEncoding() + " != " + key);
                            return;
                        }
                    }
                }
                catch (UnsupportedEncodingException uee)
                {
                    output_.println("Warning: JVM does not support CCSID/encoding " + ccsidStr + "/" + key + ".");
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
     Verifies all of the encoding entries in the encodingCcsid table.
     Result: Variation should be successful.
     **/
    public void Var004()
    {
        String encoding = "";
        try
        {
            Hashtable table = ConversionMaps.encodingCcsid_;
            Enumeration enumeration = table.keys();
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
        if (isApplet_)
        {
            notApplicable("Cannot set Locale inside a browser.");
            return;
        }
        Locale saveDefault = Locale.getDefault();
        String localeStr = "";
        try
        {
            Hashtable table = ConversionMaps.localeCcsidMap_;
            Enumeration enumeration = table.keys();
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
                    failed("Incorrect ccsid returned for locale " + localeStr + ": " + c.getCcsid() + " != " + ccsid);
                    return;
                }
            }
            succeeded();
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
    public void Var006()
    {
        if (isApplet_)
        {
            notApplicable("Cannot set Locale inside a browser.");
            return;
        }
        Locale saveDefault = Locale.getDefault();
        String localeStr = "";
        try
        {
            Hashtable table = ConversionMaps.localeNlvMap_;
            Enumeration enumeration = table.keys();
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
                int nlv = Integer.parseInt((String)table.get(localeStr));
                int ee = Integer.parseInt(ExecutionEnvironment.getNlv(Locale.getDefault()));
                if (ee != nlv)
                {
                    failed("Incorrect nlv returned for locale " + localeStr + ": " + ee + " != " + nlv);
                    return;
                }
            }
            succeeded();
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
                        failed("Incorrect ccsid/encoding match: " + tableCcsid + " != " + tableEncoding);
                        return;
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
                            failed("Incorrect ccsid match for encoding " + tableEncoding + ": " + lookupCcsid + " != " + tableCcsid);
                            return;
                        }
                    }
                }
            }
            succeeded();
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
        if (isApplet_)
        {
            notApplicable("Cp942C does not exist in IE.");
            return;
        }
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
