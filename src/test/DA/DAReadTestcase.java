///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DAReadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;


import java.math.BigDecimal;

import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.Testcase;

/**
 Testcase DAReadTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>CharacterDataArea::read()
 <li>CharacterDataArea::read(int, int)
 <li>CharacterDataArea::read(byte[], int, int, int)
 <li>DecimalDataArea::read()
 <li>LocalDataArea::read()
 <li>LocalDataArea::read(int, int)
 <li>LocalDataArea::read(byte[], int, int, int)
 <li>LogicalDataArea::read()
 </ul>
 **/
public class DAReadTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DAReadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DATest.main(newArgs); 
   }
    // New methods were added in JTOpen 6.2 (spec version V6R1M0 PTF 4)
    private static boolean areRawBytesMethodsDefined_ = true;

    String daSecTest_ ="DASECTST"; 
    
    protected void setup() { 
      if (testLib_ != null ) { 
        int len = testLib_.length(); 
        if (len >= 5) { 
          daSecTest_ = "DASEC"+testLib_.substring(len-5); 
        }
      }

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
     Verify invalid usage of CharacterDataArea::read().
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.read();
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.read(1,1);
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
     Verify invalid usage of CharacterDataArea::read().
     Try to read from a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.read();
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.read(1,1);
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
     Verify invalid usage of CharacterDataArea::read().
     Try to read from a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.read();
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var006()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.read(3,4);
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
     Verify invalid usage of CharacterDataArea::read().
     Try to read from a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var007()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.read();
            da.delete();
            try
            {
                da2.read();
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var008()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.read(2,3);
            da.delete();
            try
            {
                da2.read(4,5);
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var009()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.read(-1,1);
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var010()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            int offset = da.getLength();
            try
            {
                da.read(offset,1);
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataLength. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var011()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            try
            {
                da.read(1,0);
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataLength. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var012()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            int length = da.getLength();
            try
            {
                da.read(1,length+1);
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
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area, specifying an invalid
     combination of dataAreaOffset and dataLength.
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var013()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create(); // Default size of 32 chars.
            try
            {
                da.read(31, 2);
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
     Verify valid usage of CharacterDataArea::read().
     Try to read from a data area created using create().
     The method should return the proper data.
     **/
    public void Var014()
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
     Verify valid usage of CharacterDataArea::read().
     Try to read from a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var015()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String expected = "Initial";
                da.create(10, expected, "DAReadTestcase", "*USE");
                String data = da2.read();
                if (expected.equals(data.trim()) && data.length() == 10)
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
     Verify valid usage of CharacterDataArea::read(int,int).
     Try to read from a data area created using create().
     The method should return the proper data.
     **/
    public void Var016()
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

                String data = da.read(0,length);
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
     Verify valid usage of CharacterDataArea::read(int,int).
     Try to read from a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var017()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String expected = "Initial";
                da.create(10, expected, "DAReadTestcase", "*USE");
                String data = da2.read(0,10);
                if (expected.equals(data.trim()) && data.length() == 10)
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
     Verify invalid usage of CharacterDataArea::read().
     Try to read from a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var018()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.read();
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var019()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.read(2,2);
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::read().
     Try to read from a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var020()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.read();
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::read(int,int).
     Try to read from a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var021()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.read(1,1);
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::read().
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var022()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.read();
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
     Verify invalid usage of DecimalDataArea::read().
     Try to read from a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var023()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.read();
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
     Verify invalid usage of DecimalDataArea::read().
     Try to read from a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var024()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.read();
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
     Verify invalid usage of DecimalDataArea::read().
     Try to read from a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var025()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.read();
            da.delete();
            try
            {
                da2.read();
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
     Verify valid usage of DecimalDataArea::read().
     Try to read from a data area created using create().
     The method should return the proper data.
     **/
    public void Var026()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default value of 0.00000
                BigDecimal expected = new BigDecimal("0.00000");
                BigDecimal data = da.read();
                if (data.equals(expected))
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: '"+expected.toString()+"'\n  Returned: '"+data.toString()+"' da2="+da2);
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
     Verify valid usage of DecimalDataArea::read().
     Try to read from a data area created using create(int,int,BigDecimal,String,String).
     The method should return the proper data.
     **/
    public void Var027()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                BigDecimal expected = new BigDecimal("12345.678");
                da.create(8, 3, expected, "DAReadTestcase", "*USE");
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
     Verify invalid usage of DecimalDataArea::read().
     Try to read from a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var028()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.read();
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::read().
     Try to read from a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var029()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.read();
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::read().
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var030()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.read();
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
     Verify invalid usage of LogicalDataArea::read().
     Try to read from a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var031()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            da.read();
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
     Verify invalid usage of LogicalDataArea::read().
     Try to read from a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var032()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.read();
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
     Verify invalid usage of LogicalDataArea::read().
     Try to read from a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var033()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            da2.read();
            da.delete();
            try
            {
                da2.read();
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
     Verify valid usage of LogicalDataArea::read().
     Try to read from a data area created using create().
     The method should return the proper data.
     **/
    public void Var034()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(); // Default value of false
                if (da2.read() == false)
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
     Verify valid usage of LogicalDataArea::read().
     Try to read from a data area created using create(boolean,String,String).
     The method should return the proper data.
     **/
    public void Var035()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                da.create(true, "DAReadTestcase", "*USE");
                if (da2.read() == true)
                {
                    succeeded();
                }
                else
                {
                    failed("Incorrect data read:\n  Expected: true\n  Returned: false");
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
     Verify invalid usage of LogicalDataArea::read().
     Try to read from a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var036()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*LGL)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.read();
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::read().
     Try to read from a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var037()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*LGL)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.read();
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
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LocalDataArea::read().
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var038()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.read();
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
     Verify invalid usage of LocalDataArea::read(int,int).
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var039()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.read(1,1);
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
     Verify invalid usage of LocalDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var040()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            try
            {
                da.read(-1,1);
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
     Verify invalid usage of LocalDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var041()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int offset = da.getLength();
            try
            {
                da.read(offset,1);
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
     Verify invalid usage of LocalDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataLength. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var042()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            try
            {
                da.read(1,0);
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
     Verify invalid usage of LocalDataArea::read(int,int).
     Try to read from a data area, specifying an invalid dataLength. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var043()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int length = da.getLength();
            try
            {
                da.read(1,length+1);
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
     Verify invalid usage of LocalDataArea::read(int,int).
     Try to read from a data area, specifying an invalid
     combination of dataAreaOffset and dataLength.
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var044()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int offset = da.getLength() - 1;
            try
            {
                da.read(offset, 2);
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
     Verify valid usage of LocalDataArea::read().
     Try to read from a data area.
     The method should return the proper data.
     **/
    public void Var045()
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

    /**
     Verify valid usage of LocalDataArea::read(int,int).
     Try to read from a data area.
     The method should return the proper data.
     **/
    public void Var046()
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

                String data = da.read(0,length);
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


    /**
     Verify valid usage of CharacterDataArea::read(int).
     Try to read from a data area created using create().
     The method should return the proper data.
     **/
    public void Var047()            //@A1A
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

                String data = da.read(BidiStringType.DEFAULT);
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
     Verify valid usage of CharacterDataArea::read(int,int,int).
     Try to read from a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var048()        //@A1A
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            try
            {
                String expected = "Initial";
                da.create(10, expected, "DAReadTestcase", "*USE");
                String data = da2.read(0,10,BidiStringType.DEFAULT);
                if (expected.equals(data.trim()) && data.length() == 10)
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
     Verify valid usage of LocalDataArea::read(int).
     Try to read from a data area.
     The method should return the proper data.
     **/
    public void Var049()         //@A1A
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

                String data = da.read(BidiStringType.DEFAULT);
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

    /**
     Verify valid usage of LocalDataArea::read(int,int,int).
     Try to read from a data area.
     The method should return the proper data.
     **/
    public void Var050()          //@A1A
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

                String data = da.read(0,length,BidiStringType.DEFAULT);
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

    /**
     Verify valid usage of CharacterDataArea::read(byte[],int,int,int).
     The method should return the proper data.
     **/
    public void Var051()
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
                String expected = "ABCDEF";
                da.create();
                da.write(expected);
                byte[] dataBuf = new byte[30];
                java.util.Arrays.fill(dataBuf, (byte)0);
                int numBytesReturned = da2.read(dataBuf, 0, 0, 10);
                CharConverter converter = new CharConverter(systemObject_.getCcsid());
                String data = converter.byteArrayToString(dataBuf, 0, numBytesReturned);
                if (expected.equals(data.trim()) && numBytesReturned == 10)
                {
                  numBytesReturned = da2.read(dataBuf,2,4,2);  // read 2 chars into buffer offset 2, from data area offset 4
                  expected = "ABEFEF";
                  data = converter.byteArrayToString(dataBuf, 0, dataBuf.length);
                  if (expected.equals(data.trim()) && numBytesReturned == 2)
                  {
                    succeeded();
                  }
                  else
                  {
                    failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                  }
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
     Verify valid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var052()
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
                String expected = "ABCDEF";
                da.create(10, expected, "DAReadTestcase", "*USE");
                byte[] dataBuf = new byte[30];
                java.util.Arrays.fill(dataBuf, (byte)0);
                int numBytesReturned = da2.read(dataBuf, 0, 0, 10);
                CharConverter converter = new CharConverter(systemObject_.getCcsid());
                String data = converter.byteArrayToString(dataBuf, 0, numBytesReturned);
                if (expected.equals(data.trim()) && numBytesReturned == 10)
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Specify null value for first parameter.
     The method should throw a NullPointerException.
     **/
    public void Var053()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.read(null,0,1,1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var054()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            byte[] bytes = new byte[10];
            da.read(bytes,0,1,1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataBufferOffset. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var055()
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
                byte[] bytes = new byte[10];
                da.read(bytes,-1,0,1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataBufferOffset. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var056()
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
                byte[] bytes = new byte[10];
                da.read(bytes,10,0,1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var057()
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
                byte[] bytes = new byte[20];
                da.read(bytes,0,-1,1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var058()
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
                byte[] bytes = new byte[20];
                da.read(bytes,0,offset,1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataLength. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var059()
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
                byte[] bytes = new byte[20];
                da.read(bytes,0,1,0);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataLength. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var060()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
            da.create();
            int length = da.getLength();
            try
            {
                byte[] bytes = new byte[20];
                da.read(bytes,0,1,length+1);
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
     Verify invalid usage of CharacterDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid
     combination of dataAreaOffset and dataLength.
     The method should throw an ExtendedIllegalArgumentException.
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
            da.create(); // Default size of 32 chars.
            try
            {
                byte[] bytes = new byte[10];
                da.read(bytes,0,31,2);
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
     Verify valid usage of LocalDataArea::read(byte[],int,int,int).
     The method should return the proper data.
     **/
    public void Var062()
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
                String expected = "ABCDEF";
                da.write(expected);
                byte[] dataBuf = new byte[30];
                java.util.Arrays.fill(dataBuf, (byte)0);
                int numBytesReturned = da2.read(dataBuf, 0, 0, 10);
                CharConverter converter = new CharConverter(systemObject_.getCcsid());
                String data = converter.byteArrayToString(dataBuf, 0, numBytesReturned);
                if (expected.equals(data.trim()) && numBytesReturned == 10)
                {
                  numBytesReturned = da2.read(dataBuf,2,4,2);  // read 2 chars into buffer offset 2, from data area offset 4
                  expected = "ABEFEF";
                  data = converter.byteArrayToString(dataBuf, 0, dataBuf.length);
                  if (expected.equals(data.trim()) && numBytesReturned == 2)
                  {
                    succeeded();
                  }
                  else
                  {
                    failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
                  }
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
              try { da.clear(); } catch (Throwable t) {}
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Specify null value for first parameter.
     The method should throw a NullPointerException.
     **/
    public void Var063()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            da.read(null,0,1,1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var064()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            LocalDataArea da = new LocalDataArea();
            byte[] bytes = new byte[10];
            da.read(bytes,0,1,1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataBufferOffset. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var065()
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
                byte[] bytes = new byte[10];
                da.read(bytes,-1,0,1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataBufferOffset. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var066()
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
                byte[] bytes = new byte[10];
                da.read(bytes,10,0,1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var067()
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
                byte[] bytes = new byte[20];
                da.read(bytes,0,-1,1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataAreaOffset. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var068()
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
                byte[] bytes = new byte[20];
                da.read(bytes,0,offset,1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataLength. (too small)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var069()
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
                byte[] bytes = new byte[20];
                da.read(bytes,0,1,0);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid dataLength. (too large)
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var070()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int length = da.getLength();
            try
            {
                byte[] bytes = new byte[20];
                da.read(bytes,0,1,length+1);
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
     Verify invalid usage of LocalDataArea::read(byte[],int,int,int).
     Try to read from a data area, specifying an invalid
     combination of dataAreaOffset and dataLength.
     The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var071()
    {
      if (!areRawBytesMethodsDefined_) {
        notApplicable("Running old version of Toolbox. New methods are missing.");
        return;
      }
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            int offset = da.getLength() - 1;
            try
            {
                byte[] bytes = new byte[10];
                da.read(bytes,0,offset,2);
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

}
