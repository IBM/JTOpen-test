///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTableWriterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.ConvTableReader;
import com.ibm.as400.access.ConvTableWriter;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

import test.JTOpenTestEnvironment;
import test.Testcase;

/**
 Testcase ConvTableWriterTestcase. Tests all the methods on the ConvTableWriter class.
 **/
public class ConvTableWriterTestcase extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvTableWriterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    private static final String testfile_ = "ctw.tst";
    private static final String testdata_ = "This is test data for ConvTableWriterTestcase.\nMom\nDad\n\n";

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

    private void delete(String file) throws Exception
    {
        File f = new File(file);
        if (f.exists()) f.delete();
    }

    private String read(String file) throws Exception
    {
        FileInputStream fis = new FileInputStream(file);
        ConvTableReader ctr = new ConvTableReader(fis);
        int c = ctr.read();
        StringBuffer buf = new StringBuffer();
        while (c != -1)
        {
            buf.append((char)c);
            c = ctr.read();
        }
        String s = buf.toString();
        ctr.close();
        return s;
    }

    private String readAndDelete(String file)
    {
        try
        {
            String s = read(file);
            delete(file);
            return s;
        }
        catch (Exception e)
        {
            output_.println("Exception occurred on readAndDelete: "+e);
            return null;
        }
    }

    /**
     Test ConvTableWriter(OutputStream) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var001()
    {
        try
        {
            ConvTableWriter ctw = new ConvTableWriter(null);
            ctw.close();
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
     Test ConvTableWriter(OutputStream) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var002()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableWriter(OutputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var003()
    {
        try
        {
            ConvTableWriter ctw = new ConvTableWriter(null, "Unicode");
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var004()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, null);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var005()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, "Unicode");
            ctw.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableWriter(OutputStream, String) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var006()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, "blahblah");
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var007()
    {
        try
        {
            ConvTableWriter ctw = new ConvTableWriter(null, 13488);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var008()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, -1);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var009()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 65536);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var010()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488);
            ctw.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableWriter(OutputStream, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var011()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 0);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var012()
    {
        try
        {
            ConvTableWriter ctw = new ConvTableWriter(null, 13488, BidiStringType.DEFAULT);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var013()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, -1, BidiStringType.DEFAULT);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var014()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 65536, BidiStringType.DEFAULT);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var015()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488, BidiStringType.DEFAULT);
            ctw.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var016()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 0, BidiStringType.DEFAULT);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var017()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488, -2);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var018()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488, 12);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test ConvTableWriter(OutputStream, int, int) constructor.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var019()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488, BidiStringType.ST4);
            ctw.close();

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
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.close();
            ctw.close();
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
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, "Unicode");
            ctw.close();
            ctw.close();
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
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488);
            ctw.close();
            ctw.close();
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
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488, BidiStringType.ST4);
            ctw.close();
            ctw.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush(). Test to see if not calling flush() makes a difference.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var024()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            StringBuffer buf = new StringBuffer();
            for (int i=0; i<20; ++i)
            {
                ctw.write(testdata_);
                buf.append(testdata_);
            }
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.length() < buf.toString().length())
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: "+(s == null ? s : ""+s.length())+" ");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var025()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            StringBuffer buf = new StringBuffer();
            for (int i=0; i<20; ++i)
            {
                ctw.write(testdata_);
                ctw.flush();
                buf.append(testdata_);
            }
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.equals(buf.toString()))
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: "+(s == null ? s : ""+s.length())+" != "+buf.toString().length());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var026()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_);
            ctw.flush();
            ctw.flush();
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var027()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write('Z');
            ctw.flush();
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.equals("Z"))
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: '"+s+"' != 'Z'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var028()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_.toCharArray());
            ctw.flush();
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var029()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_.toCharArray(), 3, 4);
            ctw.flush();
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.equals(testdata_.substring(3,7)))
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: '"+s+"' != '"+testdata_.substring(3,7)+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var030()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.flush();
            ctw.close();
            delete(testfile_);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var031()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            StringBuffer buf = new StringBuffer();
            for (int i=0; i<10; ++i)
            {
                ctw.write(testdata_);
                buf.append(testdata_);
            }
            ctw.flush();
            ctw.write(testdata_);
            buf.append(testdata_);
            ctw.flush();
            String s = read(testfile_);
            ctw.close();
            delete(testfile_);
            if (s != null && s.equals(buf.toString()))
            {
                succeeded();
            }
            else
            {
                failed("flush() failed: "+(s == null ? s : ""+s.length())+" != "+buf.toString().length());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test flush().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var032()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.close();
            ctw.flush();
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
     Test getCcsid().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var033()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            int ccsid = ctw.getCcsid();
            ctw.close();
            if (JTOpenTestEnvironment.isWindows  && ( ccsid == 1252 || ccsid == 1208)) // for Windows
            {
                succeeded();
            }
            else if (JTOpenTestEnvironment.isOS400 && (ccsid == 819  || ccsid == 1208)) // for AS/400 
            {
                succeeded();
            }
            else if (JTOpenTestEnvironment.isAIX && (ccsid == 850 || ccsid == 819)) // for AIX
            {
                succeeded();
            }
            else if (JTOpenTestEnvironment.isLinux  && (ccsid == 819 || ccsid == 1208)) // for Linux
            {
                succeeded();
            }
            else
            {
                failed("Incorrect ccsid for '"+JTOpenTestEnvironment.osVersion+"': "+ccsid);
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
    public void Var034()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, "Cp037");
            int ccsid = ctw.getCcsid();
            ctw.close();
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
    public void Var035()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488);
            int ccsid = ctw.getCcsid();
            ctw.close();
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
    public void Var036()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 437, BidiStringType.ST10);
            int ccsid = ctw.getCcsid();
            ctw.close();
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
    public void Var037()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            String enc = ctw.getEncoding();
            ctw.close();
            if (enc == null)
            {
                failed("Encoding is null.");
                return;
            }
            if (JTOpenTestEnvironment.isWindows && (enc.equals("Cp1252") || enc.equals("UTF-8"))) // for Windows
            {
                succeeded();
            }
            else if (JTOpenTestEnvironment.isOS400 && (enc.equals("ISO8859_1")|| enc.equals("UTF-8"))) // for AS/400 
            {
                succeeded();
            }
            else if (JTOpenTestEnvironment.isAIX && (enc.equals("Cp850") || enc.equals("ISO8859_1"))) // for AIX
            {
                succeeded();
            }
            else if (JTOpenTestEnvironment.isLinux && (enc.equals("ISO8859_1") || enc.equals("UTF-8"))) // for Linux
            {
                succeeded();
            }
            else
            {
                failed("Incorrect encoding for '"+JTOpenTestEnvironment.osVersion+"': "+enc);
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
    public void Var038()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, "Cp037");
            String enc = ctw.getEncoding();
            ctw.close();
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
    public void Var039()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488);
            String enc = ctw.getEncoding();
            ctw.close();
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
    public void Var040()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos, 1142, BidiStringType.ST11);
            String enc = ctw.getEncoding();
            ctw.close();
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
     Test write(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var041()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char c = 'A';
            ctw.write(c);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(new String(new char[] {c})))
            {
                succeeded();
            }
            else
            {
                failed("write(int) failed: '"+s+"' != '"+c+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var042()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char c = 'A';
            ctw.write(c);
            ctw.write(c);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(new String(new char[] { c, c })))
            {
                succeeded();
            }
            else
            {
                failed("write(int) failed: '"+s+"' != '"+c+c+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var043()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write((char[])null);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test write(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var044()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = new char[0];
            ctw.write(buf);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s != null && s.length() == 0)
            {
                succeeded();
            }
            else
            {
                failed("Should not have read any characters: "+s);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var045()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = testdata_.toCharArray();
            ctw.write(buf);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("write(char[]) failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(char[]).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var046()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = testdata_.toCharArray();
            ctw.write(buf);
            ctw.write(buf);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_+testdata_))
            {
                try
                {
                    ctw.write(buf);
                    failed("write() after close() did not throw exception.");
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
                failed("write(char[]) failed: '"+buf.toString()+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var047()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write((char[])null, 0, 2000);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test write(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var048()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = new char[2000];
            ctw.write(buf, -1, 2000);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test write(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var049()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = new char[2000];
            ctw.write(buf, 0, -1);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test write(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var050()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = testdata_.toCharArray();
            ctw.write(buf, 0, buf.length);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("write(char[],int,int) failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var051()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = testdata_.toCharArray();
            ctw.write(buf, 0, 5);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_.substring(0,5)))
            {
                succeeded();
            }
            else
            {
                failed("write(char[],int,int) failed: '"+s+"' != '"+testdata_.substring(0,5)+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(char[], int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var052()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            char[] buf = testdata_.toCharArray();
            ctw.write(buf, 0, buf.length);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_))
            {
                try
                {
                    ctw.write(buf, 0, buf.length);
                    failed("write() after close() did not throw exception.");
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
                failed("write(char[],int,int) failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(String).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var053()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write((String)null);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "data"))
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
     Test write(String).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var054()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write("");
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s != null && s.length() == 0)
            {
                succeeded();
            }
            else
            {
                failed("write(String) failed: '"+s+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(String).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var055()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("write(String) failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(String).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var056()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_);
            ctw.write(testdata_);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_+testdata_))
            {
                succeeded();
            }
            else
            {
                failed("write(String) failed: '"+s+"' != '"+testdata_+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(String).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var057()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_);
            ctw.close();
            ctw.write(testdata_);
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
     Test write(String, int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var058()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write((String)null, 0, 2000);            ctw.close();

            failed("Expected exception did not occur."+ctw);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "data"))
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
     Test write(String, int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var059()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write("fred", -1, 2000);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test write(String, int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var060()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write("fred", 0, -1);
            ctw.close();

            failed("Expected exception did not occur."+ctw);
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
     Test write(String, int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var061()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_, 0, testdata_.length());
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("write(String,int,int) failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(String, int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var062()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_, 0, 5);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_.substring(0,5)))
            {
                succeeded();
            }
            else
            {
                failed("write(String,int,int) failed: '"+s+"' != '"+testdata_.substring(0,5)+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test write(String, int, int).
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var063()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_, 0, testdata_.length());
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_))
            {
                try
                {
                    ctw.write(testdata_, 0, testdata_.length());
                    failed("write() after close() did not throw exception.");
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
                failed("write(String,int,int) failed: '"+s+"' != '"+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple writes.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var064()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_.charAt(0));
            ctw.write(testdata_.toCharArray());
            ctw.write(testdata_.toCharArray(), 1, 2);
            ctw.write(testdata_);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_.charAt(0)+testdata_+testdata_.substring(1,3)+testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_.charAt(0)+testdata_+testdata_.substring(1,3)+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple writes.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var065()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_.toCharArray(), 5, 4);
            ctw.write(testdata_.toCharArray());
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_.substring(5,9)+testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_.substring(5,9)+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple writes.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var066()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_.toCharArray(), 10, 5);
            ctw.write('B');
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_.substring(10,15)+'B'))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_.substring(10,15)+'B'+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test multiple writes.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var067()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(testfile_);
            ConvTableWriter ctw = new ConvTableWriter(fos);
            ctw.write(testdata_.charAt(2));
            ctw.write(testdata_);
            ctw.close();
            String s = readAndDelete(testfile_);
            if (s.equals(testdata_.charAt(2)+testdata_))
            {
                succeeded();
            }
            else
            {
                failed("String read not correct: '"+s+"' != '"+testdata_.charAt(2)+testdata_+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
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
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 37);
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(37);
            String s = cc.byteArrayToString(buf, 0, r);
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
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 939);
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(939);
            String s = cc.byteArrayToString(buf, 0, r);
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
    public void Var070()
    {
        try
        {
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, "Cp1252");
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(1252);
            String s = cc.byteArrayToString(buf, 0, r);
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
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 1399);
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(1399);
            String s = cc.byteArrayToString(buf, 0, r);
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
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 61952);
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(61952);
            String s = cc.byteArrayToString(buf, 0, r);
            if (s.equals(testdata_))
            {
                succeeded();
            }
            else
            {
                failed("61952 conversion failed: '"+s+"' != '"+testdata_+"'");
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
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 420);
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(420);
            String s = cc.byteArrayToString(buf, 0, r);
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
    public void Var074()
    {
        try
        {
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 13488);
            ctw.write(testdata_);
            ctw.close();

            FileInputStream fis = new FileInputStream("ccsid.tst");
            byte[] buf = new byte[2000];
            int r = fis.read(buf);
            fis.close();
            CharConverter cc = new CharConverter(13488);
            String s = cc.byteArrayToString(buf, 0, r);
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
     Test writing and flushing a buffer the same size as the internal buffer
     used by ConvTableWriter.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var075()
    {
        try
        {
            delete("ccsid.tst");

            FileOutputStream fos = new FileOutputStream("ccsid.tst");
            ConvTableWriter ctw = new ConvTableWriter(fos, 1252);
            char[] buf = new char[1024];
            for (int i=0; i<buf.length; ++i) buf[i] = 'X';
            ctw.write(buf);
            ctw.flush();
            ctw.close();

            File f = new File("ccsid.tst");
            if (f.length() != 1024)
            {
                failed("Unexpected file length: 1024 != "+f.length());
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
