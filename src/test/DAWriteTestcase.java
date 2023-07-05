///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DAWriteTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ErrorCompletingRequestException;      //@A2A
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.DataArea;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import java.math.BigDecimal;

/**
 Testcase DAWriteTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>CharacterDataArea::write(String)
 <li>CharacterDataArea::write(String,int)
 <li>CharacterDataArea::write(byte[],int,int,int)
 <li>DecimalDataArea::write(BigDecimal)
 <li>LocalDataArea::write(String)
 <li>LocalDataArea::write(String,int)
 <li>LocalDataArea::write(byte[],int,int,int)
 <li>LogicalDataArea::write(boolean)
 </ul>
 **/
public class DAWriteTestcase extends Testcase
{
    static String writeStr = "Something to write.";
    static BigDecimal writeDec = new BigDecimal("123.45");

    // New methods were added in JTOpen 6.2 (spec version V6R1M0 PTF 4)
    private static boolean areRawBytesMethodsDefined_ = true;
    static {
      try {
        areRawBytesMethodsDefined_ = TOOLBOX_PACKAGE.isCompatibleWith("6.1.0.4");
        // V6R1M0 PTF 4 (JTOpen 6.2)
      }
      catch (NumberFormatException e) { e.printStackTrace(); } // will never happen
    }

    String getPaddedUser()
    {
        String user = systemObject_.getUserId();
        int userLength = user.length();
        StringBuffer paddedUser = new StringBuffer(user);
        for (int x = userLength; x < 10; ++x)
        {
            paddedUser.append(' ');
        }
        return paddedUser.toString();
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write null to a data area.
     * The method should throw a NullPointerException.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.write(null);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "NullPointerException", "data"))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write null to a data area.
     * The method should throw a NullPointerException.
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.write(null,1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "NullPointerException", "data"))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area which has no system set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.write(writeStr);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area which has no system set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.write(writeStr,1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area which has no path set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.write(writeStr);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area which has no path set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var006()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.write(writeStr,1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a non-existent data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var007()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.write(writeStr);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a non-existent data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var008()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.write(writeStr,3);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a deleted data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var009()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.write("Text");
            da.delete();
            try
            {
                da2.write(writeStr);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a deleted data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var010()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.write("Text",2);
            da.delete();
            try
            {
                da2.write(writeStr,4);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var011()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.write(writeStr,-1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var012()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            int offset = da.getLength();
            try
            {
                da.write(writeStr,offset);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataLength. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var013()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.write("",1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area, specifying an invalid dataLength. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var014()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.write("");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var015()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            int length = da.getLength();
            try
            {
                da.write("123456789012345678901234567890123",1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
		if (exceptionStartsWith(e, "AS400Exception", "CPF1089", ErrorCompletingRequestException.AS400_ERROR)) //@A2C
                /*if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))*/
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var016()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            int length = da.getLength();
            try
            {
                da.write("123456789012345678901234567890123");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
		if (exceptionStartsWith(e, "AS400Exception", "CPF1089", ErrorCompletingRequestException.AS400_ERROR)) //@A2C
/*                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))*/
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid
     * combination of dataAreaOffset and dataLength.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var017()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create(); // Default size of 32 chars.
            try
            {
                da.write(writeStr, 31);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
		if (exceptionStartsWith(e, "AS400Exception", "CPF1089", ErrorCompletingRequestException.AS400_ERROR)) //@A2C
/*                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))*/
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     **/
    public void Var018()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default size of 32 chars.
                int length = da2.getLength();
                String expected = "Expected text.";
                da.write(expected);
                String data = da.read();
                if (expected.equals(data.trim()) && data.length() == 32)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String).
     * Try to write to a data area created using create(int,String,String,String).
     * The method should return the proper data.
     **/
    public void Var019()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String expected = "Initial";
                da.create(10, expected, "DAWriteTestcase", "*USE");
                da2.write(expected);
                String data = da2.read();
                if (expected.equals(data.trim()) && data.length() == 10)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     **/
    public void Var020()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default size of 32 chars.
                int length = da2.getLength();
                String expected = "Initial Text";
                for (int i=expected.length(); i<length; ++i)
                    expected += " ";

                da.write(expected,0);
                String data = da2.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area created using create(int,String,String,String).
     * The method should return the proper data.
     **/
    public void Var021()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String initialValue = "ZZZZZZZZZZ";
                da.create(10, initialValue, "DAWriteTestcase", "*USE");
                String expected = "Z112233ZZZ";
                da.write("112233", 1);
                String data = da2.read(0,10);
                if (expected.equals(data))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }            
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int) and
     * CharacterDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var022()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String initialValue = "XXXXXXXXXX";
                da.create(10, initialValue, "DAWriteTestcase", "*USE");
                String expected = "X112233XXX";
                da.write("112233", 1);
                String data = da2.read(0,10);
                if (!expected.equals(data))
                {
                    failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                    return;
                }
                da.write(initialValue);
                //          expected = "XX4455   X";
                expected = "4455665544";
                da2.write("4455665544", 0);
                data = da.read();
                if (!expected.equals(data))
                {
                    failed("Incorrect data write(2):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                    return;
                }
                da2.write(initialValue);
                /*          expected = "XXX667XXXX";
                 da.write("667788990011", 3);
                 data = da2.read();
                 if (!expected.equals(data))
                 {
                 failed("Incorrect data write(3):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                 return;
                 }
                 da.write(initialValue);
                 expected = "2244668800";
                 da.write("224466880011335577");
                 data = da.read();
                 if (!expected.equals(data))
                 {
                 failed("Incorrect data write(4):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                 return;
                 }
                 */
                succeeded();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var023()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
	     deleteLibrary(cmd, "DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("GRTOBJAUT DASECTEST/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("RVKOBJAUT DASECTEST/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.write(writeStr);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
		deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var024()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
	    deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("GRTOBJAUT DASECTEST/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("RVKOBJAUT DASECTEST/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.write("test",0);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
		deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area in a library to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var025()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.write(writeStr);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
                deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area in a library to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var026()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.write(writeStr,1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
                deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write null to a data area.
     * The method should throw a NullPointerException.
     **/
    public void Var027()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.write(null);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "NullPointerException", "data"))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception f)
        {
            failed(f, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a data area which has no system set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var028()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.write(writeDec);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a data area which has no path set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var029()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.write(writeDec);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a non-existent data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var030()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.write(writeDec);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a deleted data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var031()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.write(writeDec);
            da.delete();
            try
            {
                da2.write(writeDec);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     **/
    public void Var032()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default value has 5 decimal places
                BigDecimal expected = new BigDecimal("88888888.44444");
                da.write(expected);
                BigDecimal data = da2.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected.toString()+"'\n  Returned: '"+data.toString()+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a data area created using create(int,int,BigDecimal,String,String).
     * The method should return the proper data.
     **/
    public void Var033()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                BigDecimal initialValue = new BigDecimal("12345.678");
                da.create(8, 3, initialValue, "DAWriteTestcase", "*USE");
                BigDecimal expected = new BigDecimal("55555.333");
                da.write(expected);
                BigDecimal data = da2.read();
                if (expected.equals(data))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected.toString()+"'\n  Returned: '"+data.toString()+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a data area to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var034()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*DEC)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("GRTOBJAUT DASECTEST/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("RVKOBJAUT DASECTEST/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.write(writeDec);
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
                deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of DecimalDataArea::write(BigDecimal).
     * Try to write to a data area in a library to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var035()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*DEC)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.write(writeDec);
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
                deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LogicalDataArea::write(boolean).
     * Try to write to a data area which has no system set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var036()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.write(true);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LogicalDataArea::write(boolean).
     * Try to write to a data area which has no path set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var037()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            da.write(true);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LogicalDataArea::write(boolean).
     * Try to write to a non-existent data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var038()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.write(true);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LogicalDataArea::write(boolean).
     * Try to write to a deleted data area.
     * The method should throw an ObjectDoesNotExistException.
     **/
    public void Var039()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.write(true);
            da.delete();
            try
            {
                da2.write(true);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of LogicalDataArea::write(boolean).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     **/
    public void Var040()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default value of false
                da2.write(true);
                if (da.read() == true)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: true\n  Returned: false");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of LogicalDataArea::write(boolean).
     * Try to write to a data area created using create(boolean,String,String).
     * The method should return the proper data.
     **/
    public void Var041()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(true, "DAWriteTestcase", "*USE");
                da2.write(false);
                if (da.read() == false)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: false\n  Returned: true");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LogicalDataArea::write(boolean).
     * Try to write to a data area to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var042()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*LGL)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("GRTOBJAUT DASECTEST/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("RVKOBJAUT DASECTEST/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.write(true);
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
                deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LogicalDataArea::write(boolean).
     * Try to write to a data area in a library to which the user has no authority.
     * The method should throw an AS400SecurityException.
     **/
    public void Var043()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,"DASECTEST");
            if (!cmd.run("CRTLIB DASECTEST"))
            {
                failed("Unable to create library DASECTEST");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("CRTDTAARA DASECTEST/SECTST TYPE(*LGL)");
            cmd.run("GRTOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.write(true);
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("DLTDTAARA DASECTEST/SECTST");
                deleteLibrary(cmd,"DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String).
     * Try to write null to a data area.
     * The method should throw a NullPointerException.
     **/
    public void Var044()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            da.write(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "data"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write null to a data area.
     * The method should throw a NullPointerException.
     **/
    public void Var045()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            da.write(null, 4);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "data"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String).
     * Try to write to a data area which has no system set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var046()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.write(writeStr);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area which has no system set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var047()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.write(writeStr,1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var048()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            try
            {
                da.write(writeStr,-1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var049()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int offset = da.getLength();
            try
            {
                da.write(writeStr,offset);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataLength. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var050()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            try
            {
                da.write("",1);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String).
     * Try to write to a data area, specifying an invalid dataLength. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var051()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            try
            {
                da.write("");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var052()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int length = da.getLength();
            String toWrite = "";
            for (int i=0; i<1025; ++i)
                toWrite += " ";
            try
            {
                da.write(toWrite,0);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var053()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int length = da.getLength();
            String toWrite = "";
            for (int i=0; i<1025; ++i)
                toWrite += " ";
            try
            {
                da.write(toWrite);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid
     * combination of dataAreaOffset and dataLength.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var054()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int offset = da.getLength() - 1;
            try
            {
                da.write(writeStr,offset);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var055()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            try
            {
                int length = da2.getLength();
                String expected = "Initial Value";
                for (int i=expected.length(); i<length; ++i)
                    expected += " ";

                da2.write(expected);
                String data = da.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String,int).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var056()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            try
            {
                int length = da2.getLength();
                String expected = "Initial Value";
                for (int i=expected.length(); i<length; ++i)
                    expected += " ";

                da2.write(expected,0);
                String data = da.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String,int) and
     * LocalDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var057()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            try
            {
                int len = da.getLength();
                String xValue = "";
                for (int i=0; i<len; ++i)
                {
                    xValue += "X";
                }
                da.write(xValue);
                String expected = ("X112233"+xValue).substring(0, len);
                da.write("112233", 1);
                String data = da2.read(0,len);
                if (!expected.equals(data))
                {
                    failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                    return;
                }
                /*          da.write(xValue);
                 expected = ("XX4455   X"+xValue).substring(0, len);
                 da2.write("4455", 2);
                 data = da.read();
                 if (!expected.equals(data))
                 {
                 failed("Incorrect data write(2):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                 return;
                 }
                 da2.write(xValue);
                 expected = ("XXX667XXXX"+xValue).substring(0, len);
                 da.write("667788990011", 3);
                 data = da2.read();
                 if (!expected.equals(data))
                 {
                 failed("Incorrect data write(3):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                 return;
                 }
                 */
                da.write(xValue);
                expected = "";
                for (int i=0; i<len; ++i)
                {
                    expected += String.valueOf(i%10);
                }
                String toWrite = expected;
                da.write(toWrite);
                data = da.read();
                if (!expected.equals(data))
                {
                    failed("Incorrect data write(4):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                    return;
                }
                succeeded();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Verify valid usage of CharacterDataArea::write(String,int, int).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     **/
    public void Var058()                   //@A1A
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default size of 32 chars.
                int length = da2.getLength();
                String expected = "Initial Text";
                for (int i=expected.length(); i<length; ++i)
                    expected += " ";

                da.write(expected,0,BidiStringType.DEFAULT);
                String data = da2.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String,int, int).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var059()          //@A1A
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            try
            {
                int length = da2.getLength();
                String expected = "Initial Value";
                for (int i=expected.length(); i<length; ++i)
                    expected += " ";

                da2.write(expected,0,BidiStringType.DEFAULT);
                String data = da.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area created using create(int,String,String,String).
     * Verify writing single-quote characters to a data area
     * Variation added for single-quote chars.  They have to be written as
     * single-quote pairs... so, while it is two-bytes in the string written, it
     * is only 1-byte written into the data area.  Therefore, some length checks
     * were not handling single-quote pairs correctly.
     **/
    public void Var060()                                          //@A2A
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String initialValue = "ZZZZZ";
          da.create(5, initialValue, "DAWriteTestcaseVar060", "*USE");

          // write() normal 5 bytes - Verify via read()
          // wrtcmd2=[QSYS/CHGDTAARA DTAARA(QTEMP/READTEST (1 5)) VALUE('VWXYZ')]
          String expected = "VWXYZ";
          da.write("VWXYZ", 0);
          String datar = da2.read(0,5);
          if (!datar.equals(expected))
          {
            failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+datar+"'");
            return;
          }

          // write() data with 1 single-quote pair - (5 bytes == 4 chars)
          // wrtcmd2=[QSYS/CHGDTAARA DTAARA(QTEMP/READTEST (1 4)) VALUE('AB''D')]
          expected = "AB'DZ";
          da.write("AB''D", 0); // 4 Chars
          datar = da2.read(0,5);
          if (!datar.equals(expected))
          {
            failed("Incorrect data write(2):\n  Expected: '"+expected+"'\n  Returned: '"+datar+"'");
            return;
          }

          // write() 2 bytes at offset 3
          // wrtcmd2=[QSYS/CHGDTAARA DTAARA(QTEMP/READTEST (4 2)) VALUE('DE')]
          expected = "AB'DE";
          da.write("DE", 3); // Write 2 chars
          datar = da2.read(0,5);
          if (!datar.equals(expected))
          {
            failed("Incorrect data write(3):\n  Expected: '"+expected+"'\n  Returned: '"+datar+"'");
            return;
          }

          // write() data with 1 single-quote pair - (6 bytes == 5 chars)
          // This verifies can write more than 5 "bytes" to a DA of length 5
          // wrtcmd2=[QSYS/CHGDTAARA DTAARA(QTEMP/READTEST (1 5)) VALUE('ML''QR')]
          expected = "ML'QR";
          da.write("ML''QR", 0); // One single-quote pair interpretted by server as one.
          datar = da2.read(0,5);
          if (!datar.equals(expected))
          {
            failed("Incorrect data write(4):\n  Expected: '"+expected+"'\n  Returned: '"+datar+"'");
            return;
          }
          // write() data with 5 single-quote pair - (10 bytes == 5 chars)
          // This verifies can write more than 5 "bytes" to a DA of length 5
          // wrtcmd2=[QSYS/CHGDTAARA DTAARA(QTEMP/READTEST (1 5)) VALUE('''''''''''')]
          expected = "'''''";
          da.write("''''''''''", 0); // Five single-quote pairs (i.e. 5 chars)
          datar = da2.read(0,5);
          if (!datar.equals(expected))
          {
            failed("Incorrect data write(5):\n  Expected: '"+expected+"'\n  Returned: '"+datar+"'");
            return;
          }

          // write() data with 1 single-quote (that is not paired)
          // This is invalid to the CHGDTAARA command (CPD0020)
          // wrtcmd2=[QSYS/CHGDTAARA DTAARA(QTEMP/READTEST (1 5)) VALUE('AB'DE')]
          try
          {
            da2.write("AB'DE",0); // Invalid single quote (not paired is invalid)
          }
          catch (Exception e)
          {
            if (!exceptionStartsWith(e, "AS400Exception", "CPD0020", ErrorCompletingRequestException.AS400_ERROR)) 
            {
              failed(e, "Wrong exception info.");
              return;
            }
          }
          succeeded();
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }

    }


    /**
     Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     Specify null value for first parameter.
     The method should throw a NullPointerException.
     **/
    public void Var061()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.write(null,0,1,1);
      }
      catch (Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "dataBuffer"))
        {
          succeeded();
        }
        else
        {
          failed(e, "Wrong exception info.");
        }
      }
    }


    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataBufferOffset. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var062()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,-1,0,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataBufferOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataBufferOffset. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var063()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,bytes.length,0,1);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataBufferOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }



    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var064()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,0,-1,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var065()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        int offset = da.getLength();
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,0,offset,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataLength. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var066()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,0,0,0);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataLength",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var067()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create(4, "ABCD", "DAWriteTestcase", "*USE");
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x03, 0x1E, 0x1F };
          da.write(bytes,0,0,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataLength",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid
     * combination of dataAreaOffset and dataLength.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var068()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create(); // Default size of 32 chars.
        try
        {
          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray(writeStr);
          da.write(bytes,0,31,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataLength",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     **/
    public void Var069()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          da.create(); // Default size of 32 chars.
          int length = da2.getLength();
          String expected = "Initial Text";
          for (int i=expected.length(); i<length; ++i)
            expected += " ";

          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray(expected);
          da.write(bytes,0,0,bytes.length);
          String data = da2.read();
          if (data.equals(expected))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(byte[],int,int,int).
     * Try to write to a data area created using create(int,String,String,String).
     * The method should return the proper data.
     **/
    public void Var070()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String initialValue = "ZZZZZZZZZZ";
          da.create(10, initialValue, "DAWriteTestcase", "*USE");
          String expected = "Z112233ZZZ";
          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray("112233");
          da.write(bytes,0,1,bytes.length);
          String data = da2.read(0,10);
          if (expected.equals(data))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }            
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(byte[],int,int,int) and
     * CharacterDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var071()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String initialValue = "XXXXXXXXXX";
          da.create(10, initialValue, "DAWriteTestcase", "*USE");
          String expected = "X112233XXX";
          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray("112233");
          da.write(bytes,0,1,bytes.length);
          String data = da2.read(0,10);
          if (!expected.equals(data))
          {
            failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          bytes = converter.stringToByteArray(initialValue);
          da.write(bytes,0,0,bytes.length);
          //          expected = "XX4455   X";
          expected = "4455665544";
          bytes = converter.stringToByteArray(expected);
          da2.write(bytes,0,0,bytes.length);
          data = da.read();
          if (!expected.equals(data))
          {
            failed("Incorrect data write(2):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          bytes = converter.stringToByteArray(initialValue);
          da2.write(bytes,0,0,bytes.length);
          /*          expected = "XXX667XXXX";
           da.write("667788990011", 3);
           data = da2.read();
           if (!expected.equals(data))
           {
           failed("Incorrect data write(3):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
           return;
           }
           da.write(initialValue);
           expected = "2244668800";
           da.write("224466880011335577");
           data = da.read();
           if (!expected.equals(data))
           {
           failed("Incorrect data write(4):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
           return;
           }
           */
          succeeded();
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     Specify null value for first parameter.
     The method should throw a NullPointerException.
     **/
    public void Var072()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        da.write(null,0,1,1);
      }
      catch (Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "dataBuffer"))
        {
          succeeded();
        }
        else
        {
          failed(e, "Wrong exception info.");
        }
      }
    }


    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataBufferOffset. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var073()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,-1,0,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataBufferOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataBufferOffset. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var074()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,bytes.length,0,1);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataBufferOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }



    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var075()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,0,-1,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataAreaOffset. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var076()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        int offset = da.getLength();
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,0,offset,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataAreaOffset",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataLength. (too small)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var077()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        try
        {
          byte[] bytes = { 0x01, 0x02, 0x1E, 0x1F };
          da.write(bytes,0,0,0);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataLength",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var078()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        int daLength = da.getLength();
        byte[] bytes = new byte[daLength+1];
        java.util.Arrays.fill(bytes, (byte)0);
        try
        {
          da.write(bytes,0,0,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataLength",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(byte[],int,int,int).
     * Try to write to a data area, specifying an invalid
     * combination of dataAreaOffset and dataLength.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var079()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        int daLength = da.getLength();
        byte[] bytes = new byte[daLength];
        java.util.Arrays.fill(bytes, (byte)0);
        try
        {
          da.write(bytes,0,1,bytes.length);
          failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dataLength",
                                  ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of LocalDataArea::write(byte[],int,int,int).
     * The method should return the proper data.
     **/
    public void Var080()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        LocalDataArea da2 = new LocalDataArea(systemObject_);
        try
        {
          int length = da2.getLength();
          String expected = "Initial Text";
          for (int i=expected.length(); i<length; ++i)
            expected += " ";

          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray(expected);
          da.write(bytes,0,0,bytes.length);
          String data = da2.read();
          if (data.equals(expected))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          try { da.clear(); } catch (Throwable t) {}
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of LocalDataArea::write(byte[],int,int,int).
     * The method should return the proper data.
     **/
    public void Var081()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        LocalDataArea da2 = new LocalDataArea(systemObject_);
        try
        {
          String initialValue = "ZZZZZZZZZZ";
          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray(initialValue);
          da.write(bytes,0,0,bytes.length);
          String expected = "Z112233ZZZ";
          bytes = converter.stringToByteArray("112233");
          da.write(bytes,0,1,bytes.length);
          String data = da2.read(0,10);
          if (expected.equals(data))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }            
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          try { da.clear(); } catch (Throwable t) {}
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of LocalDataArea::write(byte[],int,int,int) and
     * LocalDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     **/
    public void Var082()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        LocalDataArea da2 = new LocalDataArea(systemObject_);
        try
        {
          String initialValue = "XXXXXXXXXX";
          CharConverter converter = new CharConverter(systemObject_.getCcsid());
          byte[] bytes = converter.stringToByteArray(initialValue);
          da.write(bytes,0,0,bytes.length);
          String expected = "X112233XXX";
          bytes = converter.stringToByteArray("112233");
          da.write(bytes,0,1,bytes.length);
          String data = da2.read(0,10);
          if (!expected.equals(data))
          {
            failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          bytes = converter.stringToByteArray(initialValue);
          da.write(bytes,0,0,bytes.length);
          //          expected = "XX4455   X";
          expected = "4455665544";
          bytes = converter.stringToByteArray(expected);
          da2.write(bytes,0,0,bytes.length);
          data = da.read();
          if (!expected.equals(data.trim()))
          {
            failed("Incorrect data write(2):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          bytes = converter.stringToByteArray(initialValue);
          da2.write(bytes,0,0,bytes.length);
          /*          expected = "XXX667XXXX";
           da.write("667788990011", 3);
           data = da2.read();
           if (!expected.equals(data))
           {
           failed("Incorrect data write(3):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
           return;
           }
           da.write(initialValue);
           expected = "2244668800";
           da.write("224466880011335577");
           data = da.read();
           if (!expected.equals(data))
           {
           failed("Incorrect data write(4):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
           return;
           }
           */
          succeeded();
        }
        catch (Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          try { da.clear(); } catch (Throwable t) {}
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


}
