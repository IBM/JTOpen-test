///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableReaderTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import com.ibm.as400.access.*;

/** 
 Testcase ConvTableReaderTestcase.  Tests all the methods on the ConvTableReader class.
 **/
public class ConvTableReaderTestcase extends Testcase implements Runnable
{
    private static final String testfile_ = "ctr.tst";
    private static final String testdata_ = "This is test data for ConvTableReaderTestcase.\nMom\nDad\n\n";

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        FileOutputStream fos = new FileOutputStream(testfile_);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        osw.write(testdata_);
        osw.close();
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        systemObject_.disconnectAllServices();
        File f = new File(testfile_);
        if (f.exists()) f.delete();
    }

    /**
     Test ConvTableReader(InputStream) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var001()
    {
        try
        {
            ConvTableReader ctr = new ConvTableReader(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var002()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var003()
    {
        try
        {
            ConvTableReader ctr = new ConvTableReader(null, "Unicode");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var004()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var005()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, "Unicode");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var006()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, "blahblah");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "UnsupportedEncodingException", "blahblah"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var007()
    {
        try
        {
            ConvTableReader ctr = new ConvTableReader(null, 13488);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var008()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, -1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "ccsid", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ConvTableReader(InputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var009()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 65536);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "ccsid", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ConvTableReader(InputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var010()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var011()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 0);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "UnsupportedEncodingException", "CCSID 0"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var012()
    {
        try
        {
            ConvTableReader ctr = new ConvTableReader(null, 13488, BidiStringType.DEFAULT);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var013()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, -1, BidiStringType.DEFAULT);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "ccsid", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var014()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 65536, BidiStringType.DEFAULT);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "ccsid", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var015()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488, BidiStringType.DEFAULT);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var016()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 0, BidiStringType.DEFAULT);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "UnsupportedEncodingException", "CCSID 0"))
                succeeded();
            else
                failed(e, "Incorrect exception.");
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var017()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488, -2);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "bidiStringType", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var018()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488, 12);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "bidiStringType", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ConvTableReader(InputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var019()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488, BidiStringType.ST4);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test close().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var020()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.close();
            ctr.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test close().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var021()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, "Unicode");
            ctr.close();
            ctr.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test close().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var022()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488);
            ctr.close();
            ctr.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test close().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var023()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488, BidiStringType.ST4);
            ctr.close();
            ctr.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test getCcsid().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var024()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            int ccsid = ctr.getCcsid();
            ctr.close();
            String s = System.getProperty("os.name").trim().toUpperCase();
            if (s.indexOf("WINDOWS") > -1 && ccsid == 1252) // for Windows
            {
                succeeded();
            }
            else if (s.indexOf("OS/400") > -1 && (JVMInfo.getJDK() == JVMInfo.JDK_14) && ccsid == 916) // for AS/400 if jdk 1.4, then change ccsid from 819 to 916
            {
                succeeded();
            }
            else if (s.indexOf("OS/400") > -1 && (ccsid == 819 || ccsid == 1208)) // for AS/400 
            {
                succeeded();
            }
            else if (s.indexOf("AIX") > -1 && (ccsid == 850 || ccsid == 819)) // for AIX
            {
                succeeded();
            }
            else if ((s.indexOf("Linux") > -1 || s.indexOf("LINUX") > -1) && (ccsid == 819 || ccsid == 1208)) // for Linux
            {
                succeeded();
            }
            else
            {
                failed("Incorrect ccsid for '"+s+"': "+ccsid);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test getCcsid().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var025()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, "Cp037");
            int ccsid = ctr.getCcsid();
            ctr.close();
            if (ccsid != 37)
            {
                failed("Incorrect ccsid: "+ccsid);
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
     Test getCcsid().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var026()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488);
            int ccsid = ctr.getCcsid();
            ctr.close();
            if (ccsid != 13488)
            {
                failed("Incorrect ccsid: "+ccsid);
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
     Test getCcsid().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var027()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 437, BidiStringType.ST10);
            int ccsid = ctr.getCcsid();
            ctr.close();
            if (ccsid != 437)
            {
                failed("Incorrect ccsid: "+ccsid);
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
     Test getEncoding().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var028()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String enc = ctr.getEncoding();
            ctr.close();
            if (enc == null)
            {
                failed("Encoding is null.");
                return;
            }
            String s = System.getProperty("os.name").trim().toUpperCase();
            if (s.indexOf("WINDOWS") > -1 && enc.equals("Cp1252")) // for Windows
            {
                succeeded();
            }
            else if (s.indexOf("OS/400") > -1 && (JVMInfo.getJDK() == JVMInfo.JDK_14) && enc.equals("ISO8859_8")) // for AS/400 if jdk 1.4, then change ccsid from ISO8859_1 to ISO8859_8
            {
                succeeded();
            }
            else if (s.indexOf("OS/400") > -1 && ( enc.equals("ISO8859_1") ||  enc.equals("UTF-8"))) // for AS/400 
            {
                succeeded();
            }
            else if (s.indexOf("AIX") > -1 && (enc.equals("Cp850") || enc.equals("ISO8859_1"))) // for AIX
            {
                succeeded();
            }
            else if ((s.indexOf("Linux") > -1 || s.indexOf("LINUX") > -1) && (enc.equals("ISO8859_1") || enc.equals("UTF-8"))) // for Linux
            {
                succeeded();
            }
            else
            {
                failed("Incorrect encoding for '"+s+"': "+enc);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test getEncoding().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var029()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, "Cp037");
            String enc = ctr.getEncoding();
            ctr.close();
            if (enc == null || !enc.equals("Cp037"))
            {
                failed("Incorrect encoding: "+enc);
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
     Test getEncoding().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var030()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 13488);
            String enc = ctr.getEncoding();
            ctr.close();
            if (enc == null || (!enc.equals("Unicode") && !enc.equals("UnicodeBig")) )
            {
                failed("Incorrect encoding: "+enc);
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
     Test getEncoding().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var031()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 1142, BidiStringType.ST11);
            String enc = ctr.getEncoding();
            ctr.close();
            if (enc == null || !enc.equals("Cp1142"))
            {
                failed("Incorrect encoding: "+enc);
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
     Test markSupported().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var032()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis, 1399, BidiStringType.ST5);
            boolean b = ctr.markSupported();
            ctr.close();
            if (b)
            {
                failed("Mark is supported.");
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
     Test read().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var033()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            int c = ctr.read();
            StringBuffer buf = new StringBuffer();
            while (c != -1)
            {
                buf.append((char)c);
                c = ctr.read();
            }
            ctr.close();
            if (buf.toString().equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("read() failed: '"+buf.toString()+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var034()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            int c = ctr.read();
            while (c != -1)
            {
                c = ctr.read();
            }
            c = ctr.read();
            ctr.close();
            if (c != -1)
            {
                failed("Last read() not -1: "+c);
                return;
            }

            try
            {
                ctr.read();
                failed("read() after close() did not throw exception.");
                return;
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "IOException", "Stream closed"))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Incorrect exception.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var035()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            int c = ctr.read((char[])null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "buffer"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test read(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var036()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[0];
            int c = ctr.read(buf);
            ctr.close();
            if (c != 0)
            {
                failed("Should not have read any characters: "+c);
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
     Test read(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var037()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[2000];
            int c = ctr.read(buf);
            ctr.close();
            if (c != testdata_.length())
            {
                failed("Did not read entire file: "+c);
                return;
            }
            StringBuffer sbuf = new StringBuffer();
            for (int i=0; i<c; ++i)
            {
                sbuf.append(buf[i]);
            }
            if (sbuf.toString().equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("read(char[]) failed: '"+sbuf.toString()+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var038()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[5];
            int c = ctr.read(buf);
            ctr.close();
            if (c != 5)
            {
                failed("Did not read entire buffer(5): "+c);
                return;
            }
            StringBuffer sbuf = new StringBuffer();
            for (int i=0; i<5; ++i)
            {
                sbuf.append(buf[i]);
            }
            if (sbuf.toString().equals(testdata_.substring(0,5)))
            {
                succeeded();
            }
            else
            {
                failed("read(char[]) failed: '"+sbuf.toString()+"' != '"+testdata_.substring(0,5)+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var039()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[testdata_.length()];
            int c = ctr.read(buf);
            if (c != testdata_.length())
            {
                ctr.close();
                failed("Did not read entire file: "+c);
                return;
            }
            StringBuffer sbuf = new StringBuffer();
            for (int i=0; i<c; ++i)
            {
                sbuf.append(buf[i]);
            }
            if (sbuf.toString().equals(testdata_))
            {
                c = ctr.read(buf);
                if (c != -1)
                {
                    ctr.close();
                    failed("Last read() did not return -1: "+c);
                    return;
                }
                ctr.close();
                try
                {
                    c = ctr.read(buf);
                    failed("read() after close() did not throw exception.");
                    return;
                }
                catch (Exception e)
                {
                    if (exceptionIs(e, "IOException", "Stream closed"))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed(e, "Incorrect exception.");
                    }
                }
            }
            else
            {
                failed("read(char[]) failed: '"+buf.toString()+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var040()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            int c = ctr.read(null, 0, 2000);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "buffer"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test read(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var041()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[2000];
            int c = ctr.read(buf, -1, 2000);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "offset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test read(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var042()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[2000];
            int c = ctr.read(buf, 0, -1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test read(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var043()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[2000];
            int c = ctr.read(buf, 0, 2000);
            ctr.close();
            if (c != testdata_.length())
            {
                failed("Did not read entire file: "+c);
                return;
            }
            StringBuffer sbuf = new StringBuffer();
            for (int i=0; i<c; ++i)
            {
                sbuf.append(buf[i]);
            }
            if (sbuf.toString().equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("read(char[],int,int) failed: '"+sbuf.toString()+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var044()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[5];
            int c = ctr.read(buf, 0, 5);
            ctr.close();
            if (c != 5)
            {
                failed("Did not read entire buffer(5): "+c);
                return;
            }
            StringBuffer sbuf = new StringBuffer();
            for (int i=0; i<5; ++i)
            {
                sbuf.append(buf[i]);
            }
            if (sbuf.toString().equals(testdata_.substring(0,5)))
            {
                succeeded();
            }
            else
            {
                failed("read(char[],int,int) failed: '"+sbuf.toString()+"' != '"+testdata_.substring(0,5)+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var045()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[testdata_.length()];
            int c = ctr.read(buf, 0, buf.length);
            if (c != testdata_.length())
            {
                ctr.close();
                failed("Did not read entire file: "+c);
                return;
            }
            StringBuffer sbuf = new StringBuffer();
            for (int i=0; i<c; ++i)
            {
                sbuf.append(buf[i]);
            }
            if (sbuf.toString().equals(testdata_))
            {
                c = ctr.read(buf, 0, buf.length);
                if (c != -1)
                {
                    ctr.close();
                    failed("Last read() did not return -1: "+c);
                    return;
                }
                ctr.close();
                try
                {
                    c = ctr.read(buf, 0, buf.length);
                    failed("read() after close() did not throw exception.");
                    return;
                }
                catch (Exception e)
                {
                    if (exceptionIs(e, "IOException", "Stream closed"))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed(e, "Incorrect exception.");
                    }
                }
            }
            else
            {
                failed("read(char[],int,int) failed: '"+buf.toString()+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var046()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(-1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var047()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(0);
            if (s != null && s.length() == 0)
            {
                succeeded();
            }
            else
            {
                failed("Incorrect string on read(int): '"+s+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var048()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(10);
            if (s != null && s.length() == 10 && s.equals(testdata_.substring(0,10)))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect string on read(int): '"+s+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var049()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(100);
            if (s != null && s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect string on read(int): '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var050()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(2);
            s = s + ctr.read(100);
            if (s != null && s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect string on read(int): '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var051()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(100);
            s = ctr.read(10);
            if (s == null)
            {
                succeeded();
            }
            else
            {
                failed("Incorrect string on read(int): '"+s+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test read(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var052()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(100);
            ctr.close();
            s = ctr.read(10);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IOException", "Stream closed"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test multiple reads.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var053()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(10);
            char[] buf = new char[100];
            int l = ctr.read(buf);
            s = s + new String(buf, 0, l);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple reads.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var054()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[100];
            ctr.read(buf, 5, 4);
            char[] buf2 = new char[100];
            int l = ctr.read(buf2);
            String s = new String(buf, 5, 4) + new String(buf2, 0, l);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple reads.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var055()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            char[] buf = new char[100];
            ctr.read(buf, 10, 5);
            int c = ctr.read();
            String s = ctr.read(100);
            s = new String(buf, 10, 5) + new String(new char[] {(char)c}) + s;
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple reads.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var056()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(testdata_.length()-1);
            int c = ctr.read();
            s = s + new String(new char[] {(char)c});
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ready().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var057()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            if (!ctr.ready())
            {
                failed("Reader not ready().");
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
     Test ready().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var058()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.read(testdata_.length());
            if (ctr.ready())
            {
                failed("Reader ready() with no characters.");
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
     Test ready().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var059()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.close();
            ctr.ready();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IOException", "Stream closed"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test ready().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var060()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.read();
            ctr.close();
            ctr.ready();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IOException", "Stream closed"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var061()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.skip(-1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var062()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            long l = ctr.skip(0);
            if (l != 0)
            {
                failed("Skipped wrong number of characters: "+l);
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
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var063()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            long l = ctr.skip(1);
            if (l != 1)
            {
                failed("Skipped wrong number of characters: "+l);
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
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var064()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            long l = ctr.skip(100);
            if (l != testdata_.length())
            {
                failed("Skipped wrong number of characters: "+l+" != "+testdata_.length());
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
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var065()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.skip(5);
            String s = ctr.read(100);
            if (s.equals(testdata_.substring(5)))
            {
                succeeded();
            }
            else
            {
                failed("Skip failed: '"+s+"' != '"+testdata_.substring(5)+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var066()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            String s = ctr.read(2);
            long l = ctr.skip(100);
            s = ctr.read(100);
            if (s != null || l != testdata_.length()-2)
            {
                failed("Skip failed: '"+s+"'; "+l);
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
     Test skip(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var067()
    {
        try
        {
            FileInputStream fis = new FileInputStream(testfile_);
            ConvTableReader ctr = new ConvTableReader(fis);
            ctr.skip(5);
            ctr.close();
            ctr.skip(1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IOException", "Stream closed"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception.");
            }
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var068()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(37);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();
            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, 37);
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("37 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var069()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(61952);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, 61952);
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("13488 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var070()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(1252);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, "Cp1252");
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("Cp1252 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var071()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(1399);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, 1399);
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("1399 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var072()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(420);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, 420);
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("420 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var073()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(939);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();
            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, 939);
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("939 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test miscellaneous codepage conversion.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var074()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter(13488);
            byte[] b = cc.stringToByteArray(testdata_);
            fos.write(b, 0, b.length);
            fos.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, 13488);
            String s = ctr.read(100);
            ctr.close();
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("13488 conversion failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test fix in 9957567 (v5r2) and 9A02213 (v5r1m0f).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var075()
    {
        try
        {
            File f = new File("ccsid.tst");
            if (f.exists()) f.delete();

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            CharConverter cc = new CharConverter("UTF-8");
            StringBuffer buf = new StringBuffer(testdata_);
            while (buf.length() < 5000) buf.append(testdata_);
            byte[] b = cc.stringToByteArray(buf.toString());
            fos.write(b, 0, b.length);
            fos.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            ConvTableReader ctr = new ConvTableReader(fis, "UTF-8");
            String s = ctr.read(buf.toString().length());
            ctr.close();
            if (s.equals(buf.toString()))
            {
                succeeded();
            }
            else
            {
                failed("UTF-8 conversion failed. Len1: "+s.length()+", Len2: "+buf.toString().length());
            }
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            try
            {
                File f = new File("ccsid.tst");
                f.delete();
            }
            catch (Exception e) {}
        }
    }


    /**
     Test some enhancements to BidiConversionProperties.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var076()
    {
      boolean ok = true;
      StringBuffer errorMsg = new StringBuffer();
      String result = null;
      BidiConversionProperties properties = null;

      try
      {
        AS400BidiTransform bidiTransform = new AS400BidiTransform(420);
        String nominalVisual = " \ufe95 1234567890 1234567890 \ufefbA";

        // Do not set and see default behaviour
        properties = bidiTransform.getBidiConversionProperties();
        if (BidiConversionProperties.NUMERALS_NOMINAL != properties.getBidiNumeralShaping()) {
          ok = false;
          errorMsg.append("Failed check 1. ");
        }

        // This NOMINAL value came from the AS400BidiTransform(420) which set the string type to ST4 which set the
        //  the numeral shaping to nominal

        // So to really test the uninitialized case we need to create a BidiConversionProperties() on its
        // own and set it to the transform.
        properties = new BidiConversionProperties();
        if (BidiConversionProperties.NUMERALS_DEFAULT != properties.getBidiNumeralShaping()) {
          ok = false;
          errorMsg.append("Failed check 2. ");
        }
        properties.setBidiStringType(5);
        bidiTransform.setBidiConversionProperties(properties);

        result = bidiTransform.toJavaLayout(" \ufe95 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA");
        if (!result.equals("\u0644\u0627 1234567890 1234567890 \u062AA")){
          ok = false;
          errorMsg.append("Failed check 3. ");
        }
        // behaviour is like NOMINAL
        String visualResult = bidiTransform.toAS400Layout("\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA");
        if (!nominalVisual.equals(visualResult)){
          ok = false;
          errorMsg.append("Failed check 4. ");
        }

        // Set to ANY and have both types of digits unchanged
        properties = bidiTransform.getBidiConversionProperties();
        properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_ANY);
        bidiTransform.setBidiConversionProperties(properties);
        if (BidiConversionProperties.NUMERALS_ANY != properties.getBidiNumeralShaping()) {
          ok = false;
          errorMsg.append("Failed check 5. ");
        }
        result = bidiTransform.toJavaLayout(" \ufe951234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\ufefbA");
        if (!result.equals("\u0644\u0627\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 1234567890\u062AA")) {
          ok = false;
          errorMsg.append("Failed check 6. ");
        }
        visualResult = bidiTransform.toAS400Layout("\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA");
        if (!visualResult.equals(" \ufe95 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 1234567890 \ufefbA")) {
          ok = false;
          errorMsg.append("Failed check 7. ");
        }

        // Set to national and all digits become Indic
        properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_NATIONAL);
        bidiTransform.setBidiConversionProperties(properties);
        if (BidiConversionProperties.NUMERALS_NATIONAL != properties.getBidiNumeralShaping()) {
          ok = false;
          errorMsg.append("Failed check 8. ");
        }
        result = bidiTransform.toJavaLayout(" \ufe951234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\ufefbA");
        if (!result.equals("\u0644\u0627\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\u062AA")) {           ok = false;
          errorMsg.append("Failed check 9. ");
        }
        visualResult = bidiTransform.toAS400Layout("\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA");
        if (!visualResult.equals(" \ufe95 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA")) {
          ok = false;
          errorMsg.append("Failed check 10. ");
        }

        // Set to Nominal and all digits become Arabic 1,2,3
        properties = bidiTransform.getBidiConversionProperties();
        properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_NOMINAL);
        bidiTransform.setBidiConversionProperties(properties);
        if (BidiConversionProperties.NUMERALS_NOMINAL != properties.getBidiNumeralShaping()) {
          ok = false;
          errorMsg.append("Failed check 11. ");
        }
        result = bidiTransform.toJavaLayout(" \ufe95 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA");
        if (!result.equals("\u0644\u0627 1234567890 1234567890 \u062AA")) {
          ok = false;
          errorMsg.append("Failed check 12. ");
        }
        visualResult = bidiTransform.toAS400Layout("\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA");
        if (!nominalVisual.equals(visualResult)) {
          ok = false;
          errorMsg.append("Failed check 13. ");
        }

        // Set to contextual, given the Arabic characters around them, the digits will become National (Indic)
        properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_CONTEXTUAL);
        bidiTransform.setBidiConversionProperties(properties);
        if (BidiConversionProperties.NUMERALS_CONTEXTUAL != properties.getBidiNumeralShaping()) {
          ok = false;
          errorMsg.append("Failed check 14. ");
        }
        result = bidiTransform.toJavaLayout(" \ufe951234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\ufefbA");
        if (!result.equals("\u0644\u0627\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\u062AA")) {
          ok = false;
          errorMsg.append("Failed check 15. ");
        }
        visualResult = bidiTransform.toAS400Layout("\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA");
        if (!visualResult.equals(" \ufe95 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA")) {
          ok = false;
          errorMsg.append("Failed check 16. ");
        }

        // Set to contextual, note that contextual acts as NATIONAL if Arabic characters are present and as ANY
        // if non-Arabic characters are present, so here it acts as ANY and passes both shapes through, unchanged 
        properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_CONTEXTUAL);
        bidiTransform.setBidiConversionProperties(properties);
        result = bidiTransform.toJavaLayout("Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin");
        if (!result.equals("Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin")) {
          ok = false;
          errorMsg.append("Failed check 17. ");
        }
        visualResult = bidiTransform.toAS400Layout("Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin");
        if (!visualResult.equals("Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin")) {
          ok = false;
          errorMsg.append("Failed check 18. ");
        }

        // Test Lam-Alef Expansion
        {
          bidiTransform = new AS400BidiTransform(420);

          // Conversion from logical to visual expands lam alef ligatures even though
          // there are not enough spaces
          properties = bidiTransform.getBidiConversionProperties();
          properties.setBidiExpandLamAlef(true);
          bidiTransform.setBidiConversionProperties(properties);
          String visual =" \ufe95\ufef51\ufef8\ufefbA";
          result = bidiTransform.toJavaLayout(visual);
          String logical = " \u0644\u0627\u0644\u06231\u0644\u0622\u062AA";
          if (!result.equals(logical)) {
            ok = false;
            errorMsg.append("Failed check 19. ");
          }
        } 


        if (ok) succeeded();
        else failed(errorMsg.toString());
      }
      catch (Throwable e)
      {
        failed(e, "Unexpected exception.");
      }
      finally
      {
        try
        {
          File f = new File("ccsid.tst");
          f.delete();
        }
        catch (Exception e) {}
      }
    }

}
