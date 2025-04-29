///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DateTimeConverterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Job;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.DateTimeConverter;

import test.Testcase;

/**
 The DateTimeConverterTestcase class tests the methods of class DateTimeConverter.
 <p>The methods are listed as following:
 <ul>
 <li>DateTimeConverter(AS400)
 <li>convert(byte[], String, String)
 <li>convert(Date, String)
 <li>convert(byte[], String)
 </ul>
 **/
public class DateTimeConverterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DateTimeConverterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JobTest.main(newArgs); 
   }
    AS400Text text17_;
    AS400Text text16_;
    DateTimeConverter conv_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        text17_ = new AS400Text(17, systemObject_.getCcsid(), systemObject_);
        text16_ = new AS400Text(16, systemObject_.getCcsid(), systemObject_);
        conv_ = new DateTimeConverter(systemObject_);
        systemObject_.connectService(AS400.COMMAND);
    }

    /**
     Method tested: DateTimeConverter(AS400 system).
     Ensure that the constructor with an argument of AS400 system runs well.
     **/
    public void Var001()
    {
        try
        {
            DateTimeConverter conv = new DateTimeConverter(systemObject_);
            assertCondition(true, "Conv="+conv); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested: DateTimeConverter(AS400 system).
     Ensure that a NullPointerException is thrown if the system is null.
     **/
    public void Var002()
    {
        try
        {
            DateTimeConverter conv = new DateTimeConverter(null);
            failed("Expected exception didn't occur."+conv);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure the formatted date will roundtrip.  Test all input formats.
     **/
    public void Var003()
    {
        try
        {
            byte[] test17 = text17_.toBytes("19980920080808111");
            byte[] test16 = text16_.toBytes("0980920080808111");
            String failStr = "";
            byte[] converted = null;

            converted = conv_.convert(test17, "*YYMD", "*DTS");
            byte[] roundtrip = conv_.convert(converted, "*DTS", "*YMD");
            for (int c = 0; c < 16; ++c)
            {
                if (roundtrip[c] != test16[c])
                {
                    failStr += "\nIncorrect conversion for format *DTS at position " + c + ": " + roundtrip[c] + " != " + test16[c];
                }
            }

            converted = conv_.convert(test16, "*CURRENT", "*YYMD");
            roundtrip = conv_.convert(converted, "*YYMD", "*YYMD");
            for (int c = 0; c < 17; ++c)
            {
                if (roundtrip[c] != converted[c])
                {
                    failStr += "\nIncorrect conversion for format *CURRENT/*YYMD at position " + c + ": " + roundtrip[c] + " != " + converted[c];
                }
            }

            converted = conv_.convert(test16, "*YMD", "*YYMD");
            for (int c = 0; c < 17; ++c)
            {
                if (converted[c] != test17[c])
                {
                    failStr += "\nIncorrect conversion for format *YMD at position " + c + ": " + converted[c] + " != " + test17[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*YMD");
            for (int c = 0; c < 16; ++c)
            {
                if (converted[c] != test16[c])
                {
                    failStr += "\nIncorrect conversion for format *YYMD at position " + c + ": " + converted[c] + " != " + test16[c];
                }
            }

            byte[] testb = text16_.toBytes("0092098080808111");
            converted = conv_.convert(testb, "*MDY", "*YMD");
            if (converted[0] != testb[0] || // century
                converted[3] != testb[1] || // month
                converted[4] != testb[2] || // month
                converted[5] != testb[3] || // day
                converted[6] != testb[4] || // day
                converted[1] != testb[5] || // year
                converted[2] != testb[6])   // year
            {
                failStr += "\nIncorrect conversion for format *MDY, positions 0-6.";
            }
            for (int c = 7; c < 16; ++c)
            {
                if (converted[c] != testb[c])
                {
                    failStr += "\nIncorrect conversion for format *MDY at position " + c + ": " + converted[c] + " != " + testb[c];
                }
            }

            testb = text17_.toBytes("09201998080808111");
            converted = conv_.convert(testb, "*MDYY", "*YYMD");
            if (converted[0] != testb[4] || // month
                converted[1] != testb[5] || // month
                converted[2] != testb[6] || // day
                converted[3] != testb[7] || // day
                converted[4] != testb[0] || // year
                converted[5] != testb[1] || // year
                converted[6] != testb[2] || // year
                converted[7] != testb[3])   // year
            {
                failStr += "\nIncorrect conversion for format *MDYY, positions 0-7.";
            }
            for (int c = 8; c < 17; ++c)
            {
                if (converted[c] != testb[c])
                {
                    failStr += "\nIncorrect conversion for format *MDYY at position " + c + ": " + converted[c] + " != " + testb[c];
                }
            }

            testb = text16_.toBytes("0200998080808111");
            converted = conv_.convert(testb, "*DMY", "*YMD");
            if (converted[0] != testb[0] || // century
                converted[5] != testb[1] || // day
                converted[6] != testb[2] || // day
                converted[3] != testb[3] || // month
                converted[4] != testb[4] || // month
                converted[1] != testb[5] || // year
                converted[2] != testb[6])   // year
            {
                failStr += "\nIncorrect conversion for format *DMY, positions 0-6.";
            }
            for (int c = 7; c < 16; ++c)
            {
                if (converted[c] != testb[c])
                {
                    failStr += "\nIncorrect conversion for format *DMY at position " + c + ": " + converted[c] + " != " + testb[c];
                }
            }

            testb = text17_.toBytes("20091998080808111");
            converted = conv_.convert(testb, "*DMYY", "*YYMD");
            if (converted[6] != testb[0] || // day
                converted[7] != testb[1] || // day
                converted[4] != testb[2] || // month
                converted[5] != testb[3] || // month
                converted[0] != testb[4] || // year
                converted[1] != testb[5] || // year
                converted[2] != testb[6] || // year
                converted[3] != testb[7])   // year
            {
                failStr += "\nIncorrect conversion for format *DMYY, positions 0-7.";
            }
            for (int c = 8; c < 17; ++c)
            {
                if (converted[c] != testb[c])
                {
                    failStr += "\nIncorrect conversion for format *DMYY at position " + c + ": " + converted[c] + " != " + testb[c];
                }
            }

            testb = text16_.toBytes("104060 121212456"); // 60th day of year in 2004
            byte[] cmp = text16_.toBytes("1040229121212456"); // Feb 29th, 2004
            converted = conv_.convert(testb, "*JUL", "*YMD");
            for (int c = 0; c < 16; ++c)
            {
                if (converted[c] != cmp[c])
                {
                    failStr += "\nIncorrect conversion for format *JUL at position " + c + ": " + converted[c] + " != " + cmp[c];
                }
            }

            testb = text17_.toBytes("2004060 121212678"); // 60th day of year in 2004
            cmp = text17_.toBytes("20040229121212678"); // Feb 29th, 2004
            converted = conv_.convert(testb, "*LONGJUL", "*YYMD");
            for (int c = 0; c < 17; ++c)
            {
                if (converted[c] != cmp[c])
                {
                    failStr += "\nIncorrect conversion for format *LONGJUL at position " + c + ": " + converted[c] + " != " + cmp[c];
                }
            }

            //      converted = conv_.convert(test17, "*DOS", "*YYMD");
            // *DOS is not a valid input parameter

            assertCondition(failStr.length() == 0, failStr);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure the formatted date will roundtrip.  Test all output formats.
     **/
    public void Var004()
    {
        try
        {
            byte[] test17 = text17_.toBytes("19980920080808111");
            byte[] test16 = text16_.toBytes("0980920080808111");
            String failStr = "";
            byte[] converted = null;

            converted = conv_.convert(test17, "*YYMD", "*DTS");
            byte[] roundtrip = conv_.convert(converted, "*DTS", "*YYMD");
            for (int c = 0; c < 17; ++c)
            {
                if (roundtrip[c] != test17[c])
                {
                    failStr += "\nIncorrect conversion for format *DTS at position " + c + ": " + roundtrip[c] + " != " + test17[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*YMD");
            for (int c = 0; c < 16; ++c)
            {
                if (converted[c] != test16[c])
                {
                    failStr += "\nIncorrect conversion for format *YMD at position " + c + ": " + converted[c] + " != " + test16[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*YYMD");
            for (int c = 0; c < 17; ++c)
            {
                if (converted[c] != test17[c])
                {
                    failStr += "\nIncorrect conversion for format *YYMD at position " + c + ": " + converted[c] + " != " + test17[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*MDY");
            if (converted[0] != test16[0] || // century
                converted[1] != test16[3] || // month
                converted[2] != test16[4] || // month
                converted[3] != test16[5] || // day
                converted[4] != test16[6] || // day
                converted[5] != test16[1] || // year
                converted[6] != test16[2])   // year
            {
                failStr += "\nIncorrect conversion for format *MDY, positions 0-6.";
            }
            for (int c = 7; c < 16; ++c)
            {
                if (converted[c] != test16[c])
                {
                    failStr += "\nIncorrect conversion for format *MDY at position " + c + ": " + converted[c] + " != " + test16[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*MDYY");
            if (converted[0] != test17[4] || // month
                converted[1] != test17[5] || // month
                converted[2] != test17[6] || // day
                converted[3] != test17[7] || // day
                converted[4] != test17[0] || // year
                converted[5] != test17[1] || // year
                converted[6] != test17[2] || // year
                converted[7] != test17[3])   // year
            {
                failStr += "\nIncorrect conversion for format *MDYY, positions 0-7.";
            }
            for (int c = 8; c < 17; ++c)
            {
                if (converted[c] != test17[c])
                {
                    failStr += "\nIncorrect conversion for format *MDYY at position " + c + ": " + converted[c] + " != " + test17[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*DMY");
            if (converted[0] != test16[0] || // century
                converted[1] != test16[5] || // day
                converted[2] != test16[6] || // day
                converted[3] != test16[3] || // month
                converted[4] != test16[4] || // month
                converted[5] != test16[1] || // year
                converted[6] != test16[2])   // year
            {
                failStr += "\nIncorrect conversion for format *DMY, positions 0-6.";
            }
            for (int c = 7; c < 16; ++c)
            {
                if (converted[c] != test16[c])
                {
                    failStr += "\nIncorrect conversion for format *DMY at position " + c + ": " + converted[c] + " != " + test16[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*DMYY");
            if (converted[0] != test17[6] || // day
                converted[1] != test17[7] || // day
                converted[2] != test17[4] || // month
                converted[3] != test17[5] || // month
                converted[4] != test17[0] || // year
                converted[5] != test17[1] || // year
                converted[6] != test17[2] || // year
                converted[7] != test17[3])   // year
            {
                failStr += "\nIncorrect conversion for format *DMYY, positions 0-7.";
            }
            for (int c = 8; c < 17; ++c)
            {
                if (converted[c] != test17[c])
                {
                    failStr += "\nIncorrect conversion for format *DMYY at position " + c + ": " + converted[c] + " != " + test17[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*JUL");
            if (converted[0] != test16[0] || // century
                converted[1] != test16[1] || // year
                converted[2] != test16[2] || // year
                // Hard to convert the month/day into a Julian day-of-the-year,
                // so we skip it and assume that if index 6 is blank that we have
                // a valid Julian date.
                //          converted[3] != test16[x] || // day
                //          converted[4] != test16[x] || // day
                //          converted[5] != test16[x] || // day
                converted[6] != 64)          // blank 0x40
            {
                failStr += "\nIncorrect conversion for format *JUL, positions 0-6.";
            }
            for (int c = 7; c < 16; ++c)
            {
                if (converted[c] != test16[c])
                {
                    failStr += "\nIncorrect conversion for format *JUL at position " + c + ": " + converted[c] + " != " + test16[c];
                }
            }

            converted = conv_.convert(test17, "*YYMD", "*LONGJUL");
            if (converted[0] != test17[0] || // year
                converted[1] != test17[1] || // year
                converted[2] != test17[2] || // year
                converted[3] != test17[3] || // year
                // Hard to convert the month/day into a Julian day-of-the-year,
                // so we skip it and assume that if index 7 is blank that we have
                // a valid Julian date.
                //          converted[4] != test16[x] || // day
                //          converted[5] != test16[x] || // day
                //          converted[6] != test16[x] || // day
                converted[7] != 64)          // blank 0x40
            {
                failStr += "\nIncorrect conversion for format *LONGJUL, positions 0-7.";
            }
            for (int c = 8; c < 17; ++c)
            {
                if (converted[c] != test17[c])
                {
                    failStr += "\nIncorrect conversion for format *LONGJUL at position " + c + ": " + converted[c] + " != " + test17[c];
                }
            }

            //      converted = conv_.convert(test17, "*YYMD", "*DOS");
            // Can only test *DOS if the input is *DTS or *CURRENT

            assertCondition(failStr.length() == 0, failStr);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure a NullPointerException is thrown when the data is null.
     **/
    public void Var005()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert(null, "*YYMD", "*YYMD");
            failed("Expected exception didn't occur."+test);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "data");
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure a NullPointerException is thrown when the inFormat is null.
     **/
    public void Var006()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert(test, null, "*YYMD");
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "inFormat");
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure a NullPointerException is thrown when the outFormat is null.
     **/
    public void Var007()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert(test, "*YYMD", null);
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "outFormat");
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure an AS400Exception is thrown when the inFormat is not valid.
     **/
    public void Var008()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert(test, "INVALID", "*YYMD");
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception") && e.getMessage().equalsIgnoreCase("CPF1850 FORMAT INVALID NOT VALID"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception info.");
            }
        }
    }

    /**
     Method tested: convert(byte[], String, String).
     Ensure an AS400Exception is thrown when the outFormat is not valid.
     **/
    public void Var009()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert(test, "*YYMD", "INVALID");
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception") && e.getMessage().equalsIgnoreCase("CPF1850 FORMAT INVALID NOT VALID"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception info.");
            }
        }
    }

    /**
     Method tested: convert(byte[], String).
     Ensure a NullPointerException is thrown when the data is null.
     **/
    public void Var010()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert((byte[])null, "*YYMD");
            failed("Expected exception didn't occur."+test);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "data");
        }
    }

    /**
     Method tested: convert(byte[], String).
     Ensure a NullPointerException is thrown when the inFormat is null.
     **/
    public void Var011()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert(test, null);
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "inFormat");
        }
    }

    /**
     Method tested: convert(Date, String).
     Ensure a NullPointerException is thrown when the date is null.
     **/
    public void Var012()
    {
        try
        {
            byte[] test = text17_.toBytes("19980920080808111");
            conv_.convert((Date)null, "*YYMD");
            failed("Expected exception didn't occur."+test);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "date");
        }
    }

    /**
     Method tested: convert(Date, String).
     Ensure a NullPointerException is thrown when the outFormat is null.
     **/
    public void Var013()
    {
        try
        {
            Date date = Calendar.getInstance().getTime();
            conv_.convert(date, null);
            failed("Expected exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "outFormat");
        }
    }

    /**
     Methods tested: convert(byte[], String), convert(Date, String), and convert(byte[], String, String).
     Test roundtrip conversion for all 3 convert methods.
     **/
    public void Var014()
    {
        try
        {
            Date d1 = Calendar.getInstance().getTime();
            byte[] b1 = conv_.convert(d1, "*DTS");
            Date d2 = conv_.convert(b1, "*DTS");
            byte[] b2 = conv_.convert(b1, "*DTS", "*YMD");
            Date d3 = conv_.convert(b2, "*YMD");
            byte[] b3 = conv_.convert(d3, "*YYMD");
            Date d4 = conv_.convert(b3, "*YYMD");
            byte[] b4 = conv_.convert(d4, "*YMD");
            if (!d1.equals(d2) || !d2.equals(d3) || !d1.equals(d4))
            {
                failed("Dates not equal:\n " + d1 + " !=\n " + d2 + " !=\n " + d3 + " !=\n " + d4);
                return;
            }
            for (int i = 0; i < b4.length; ++i)
            {
                if (b4[i] != b2[i])
                {
                    failed("Byte arrays not equal.");
                    return;
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
     Methods tested: convert(byte[], String), convert(Date, String), and convert(byte[], String, String).
     Test conversion to/from *DTS format for all other formats.
     **/
    public void Var015()
    {
        try
        {
            Date date = Calendar.getInstance().getTime();
            String failStr = "";
            byte[] master = conv_.convert(date, "*DTS");
            byte[] cmp = conv_.convert(conv_.convert(date, "*YMD"), "*YMD", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *YMD.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*YYMD"), "*YYMD", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *YYMD.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*MDY"), "*MDY", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *MDY.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*MDYY"), "*MDYY", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *MDYY.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*DMY"), "*DMY", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *DMY.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*DMYY"), "*DMYY", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *DMYY.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*JUL"), "*JUL", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *JUL.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*LONGJUL"), "*LONGJUL", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *LONGJUL.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*JOB"), "*JOB", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *JOB.";
                }
            }

            cmp = conv_.convert(conv_.convert(date, "*SYSVAL"), "*SYSVAL", "*DTS");
            for (int i = 0; i < cmp.length; ++i)
            {
                if (cmp[i] != master[i])
                {
                    failStr += "\nByte arrays not equal for *SYSVAL.";
                }
            }

            Date ret = conv_.convert(master, "*DTS");
            if (!ret.equals(date))
                failStr += "\nDates not equal:\n " + ret + " !=\n " + date;

            assertCondition(failStr.length() == 0, failStr);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Methods tested: convert(byte[], String, String).
     Test conversion using *CURRENT format.
     **/
    public void Var016()
    {
        try
        {
            Date date = Calendar.getInstance().getTime();
            byte[] yymd = conv_.convert(new byte[1], "*CURRENT", "*YYMD");
            Date ret = conv_.convert(yymd, "*YYMD");
            long dms = date.getTime();
            long rms = ret.getTime();
            long diff = dms - rms;
            if (diff  <  0) diff = 0 - diff;
            if (diff > 1200000) // Within 20 minutes.
            {
              if (TimeZone.getDefault().getID().equalsIgnoreCase("America/Chicago"))
              {
                // Assume that both client and server are in central timezone
                // Therefore the times should be accurate (i.e. within 10 minutes)
                failed("*CURRENT failed, dates not within 20 minutes:\n " + date + " !=\n " + ret);
              }
              else
              {
                // When client and server are in different timezones, the "byte[] convert()" 
                // method returns the bytes of the server's timezone... and it will
                // not match the "local" time.
                succeeded();
              }
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
     Methods tested: convert(byte[], String).
     Test conversion using *CURRENT format.
     **/
    public void Var017()
    {
        try
        {
            Date date = Calendar.getInstance().getTime();
            Date ret = conv_.convert(new byte[1], "*CURRENT");
            long dms = date.getTime();
            long rms = ret.getTime();
            long diff = dms - rms;
            if (diff < 0) diff = 0 - diff;
            if (diff > 1200000) // Within 20 minutes
            {
                failed("*CURRENT failed, dates not within 20 minutes:\n " + date + " !=\n " + ret);
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
     Methods tested: convert(byte[], String), convert(Date, String), and convert(byte[], String, String).
     Test conversion using *DOS format.  *DOS only works with *CURRENT or *DTS input formats.
     **/
    public void Var018()
    {
        try
        {
            Date date = conv_.convert(new byte[1], "*CURRENT");
            byte[] dts = conv_.convert(date, "*DTS");
            byte[] dos = conv_.convert(dts, "*DTS", "*DOS");
            // Compare the bytes, going by what the *DOS format is.
            Calendar cal = Calendar.getInstance();
            byte upper = dos[6];
            byte lower = dos[7];
            int year = 0;
            if (upper > 0)
            {
                year = upper * 256;
            }
            else
            {
                year = (upper + 256) * 256;
            }
            if (lower > 0)
            {
                year += lower;
            }
            else
            {
                year += (lower + 256);
            }
            cal.set(year, dos[5] - 1, dos[4], dos[0], dos[1], dos[2]);
            cal.set(Calendar.MILLISECOND, dos[3]*10);
            Date ret = cal.getTime();
            long t1 = ret.getTime();
            long t2 = date.getTime();
            // *DOS is only accurate to 100ths, so the dates
            // should be within 10/1000ths.
            long diff = t1 - t2;
            if (diff < 0) diff = 0 - diff;
            if (diff > 10)
            {
                failed("*DOS failed, dates too far off (" + diff + " ms):\n " + date + " !=\n " + ret);
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
