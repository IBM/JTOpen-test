///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DAClearTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.Testcase;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.DataArea;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;

/**
 Testcase DAClearTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>CharacterDataArea::clear()
 <li>DecimalDataArea::clear()
 <li>LocalDataArea::clear()
 <li>LogicalDataArea::clear()
 </ul>
 **/
public class DAClearTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DAClearTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DATest.main(newArgs); 
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
     Verify invalid usage of CharacterDataArea::clear().
     Try to clear a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.clear();
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
     Verify invalid usage of CharacterDataArea::clear().
     Try to clear a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.clear();
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
     Verify invalid usage of CharacterDataArea::clear().
     Try to clear a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.clear();
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
     Verify invalid usage of CharacterDataArea::clear().
     Try to clear a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.clear();
            da.delete();
            try
            {
                da2.clear();
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
     Verify valid usage of CharacterDataArea::clear().
     Try to clear a data area created using create().
     The method should return the proper data.
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default size of 32 chars.
                int length = da2.getLength();
                String expected = "";
                for (int i=0; i<length; ++i)
                    expected += " ";

                da2.write("Some string");
                da.clear();
                String data = da2.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
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
     Verify valid usage of CharacterDataArea::clear().
     Try to clear a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var006()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String expected = "          ";
                da.create(10, "Initial", "DAClearTestcase", "*USE");
                da.clear();
                String data = da2.read();
                if (expected.equals(data))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
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
     Verify invalid usage of CharacterDataArea::clear().
     Try to clear a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var007()
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
                da.clear();
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
                deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::clear().
     Try to clear a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var008()
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
            cmd.run("RVKOBJAUT DASECTEST *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.clear();
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
                deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::clear().
     Try to clear a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var009()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.clear();
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
     Verify invalid usage of DecimalDataArea::clear().
     Try to clear a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var010()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.clear();
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
     Verify invalid usage of DecimalDataArea::clear().
     Try to clear a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var011()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.clear();
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
     Verify invalid usage of DecimalDataArea::clear().
     Try to clear a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var012()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.clear();
            da.delete();
            try
            {
                da2.clear();
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
     Verify valid usage of DecimalDataArea::clear().
     Try to clear a data area created using create().
     The method should return the proper data.
     **/
    public void Var013()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default value of 0.00000
                BigDecimal expected = new BigDecimal("0.00000");
                da2.write(new BigDecimal("1.2"));
                da.clear();
                BigDecimal data = da2.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: '"+expected.toString()+"'\n  Returned: '"+data.toString()+"'");
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
     Verify valid usage of DecimalDataArea::clear().
     Try to clear a data area created using create(int,int,BigDecimal,String,String).
     The method should return the proper data.
     **/
    public void Var014()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                BigDecimal expected = new BigDecimal("0.000");
                da.create(8, 3, new BigDecimal("12345.678"), "DAClearTestcase", "*USE");
                da.clear();
                BigDecimal data = da2.read();
                if (expected.equals(data))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: '"+expected.toString()+"'\n  Returned: '"+data.toString()+"'");
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
     Verify invalid usage of DecimalDataArea::clear().
     Try to clear a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var015()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd, "DASECTEST");
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
                da.clear();
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
                deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::clear().
     Try to clear a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var016()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd, "DASECTEST");
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
                da.clear();
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
                deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::clear().
     Try to clear a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var017()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.clear();
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
     Verify invalid usage of LogicalDataArea::clear().
     Try to clear a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var018()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            da.clear();
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
     Verify invalid usage of LogicalDataArea::clear().
     Try to clear a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var019()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.clear();
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
     Verify invalid usage of LogicalDataArea::clear().
     Try to clear a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var020()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.clear();
            da.delete();
            try
            {
                da2.clear();
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
     Verify valid usage of LogicalDataArea::clear().
     Try to clear a data area created using create().
     The method should return the proper data.
     **/
    public void Var021()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default value of false
                da.write(true);
                da2.clear();
                if (da.read() == false)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: false\n  Returned: true");
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
     Verify valid usage of LogicalDataArea::clear().
     Try to clear a data area created using create(boolean,String,String).
     The method should return the proper data.
     **/
    public void Var022()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(true, "DAClearTestcase", "*USE");
                da2.clear();
                if (da.read() == false)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: false\n  Returned: true");
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
     Verify invalid usage of LogicalDataArea::clear().
     Try to clear a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var023()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd, "DASECTEST");
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
                da.clear();
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
                deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::clear().
     Try to clear a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var024()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASECTEST.LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd, "DASECTEST");
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
                da.clear();
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
                deleteLibrary(cmd, "DASECTEST");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LocalDataArea::clear().
     Try to clear a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var025()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.clear();
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
     Verify valid usage of LocalDataArea::clear().
     Try to clear a data area.
     The method should return the proper data.
     **/
    public void Var026()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            try
            {
                int length = da2.getLength();
                String expected = "";
                for (int i=0; i<length; ++i)
                    expected += " ";
                da2.write("Some string");
                da2.clear();
                String data = da.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
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
}
