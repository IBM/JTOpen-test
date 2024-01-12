///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DACreateTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectAlreadyExistsException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.SystemProperties;

import test.Testcase;

/**
 Testcase DACreateTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>CharacterDataArea::create()
 <li>CharacterDataArea::create(int, String, String, String);
 <li>DecimalDataArea::create()
 <li>DecimalDataArea::create(int, int, BigDecimal, String, String);
 <li>LogicalDataArea::create()
 <li>LogicalDataArea::create(boolean, String, String);
 </ul>
 **/
public class DACreateTestcase extends Testcase
{
  // Original settings of the "threadsafe" properties.
  // We don't want static, since we want to pick up any dynamic changes.
  private final String COMMANDCALL_THREADSAFE_OLD = SystemProperties.getProperty(SystemProperties.COMMANDCALL_THREADSAFE);
  private final String PROGRAMCALL_THREADSAFE_OLD = SystemProperties.getProperty(SystemProperties.PROGRAMCALL_THREADSAFE);

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        cmdRun("DLTDTAARA QGPL/CRTTEST", "CPF2105");
 	deleteLibrary("DASEC"); 
	// cmdRun("DLTxLIB DASEC", "CPF2110");
        cmdRun("CRTLIB DASEC");
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        cmdRun("DLTDTAARA QGPL/CRTTEST", "CPF2105");
	deleteLibrary("DASEC"); 
    }	

    private static String getTextDescription(ObjectDescription objDesc)
      throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
    {
      return (String)objDesc.getValue(ObjectDescription.TEXT_DESCRIPTION);
    }

    // Note: The threadsafe properties only make a difference if we're running
    // "natively" on IBM i, using jt400Native.jar.
    // If we are running remotely or using jt400.jar, they are ignored.
    private static void setThreadsafeProperties()
    {
      System.setProperty(SystemProperties.COMMANDCALL_THREADSAFE, "true");
      System.setProperty(SystemProperties.PROGRAMCALL_THREADSAFE, "true");
    }

    private void restoreThreadsafeProperties()
    {
      String oldCmdVal = (COMMANDCALL_THREADSAFE_OLD == null ? "" : COMMANDCALL_THREADSAFE_OLD);
      System.setProperty(SystemProperties.COMMANDCALL_THREADSAFE, oldCmdVal);

      String oldPgmVal = (PROGRAMCALL_THREADSAFE_OLD == null ? "" : PROGRAMCALL_THREADSAFE_OLD);
      System.setProperty(SystemProperties.PROGRAMCALL_THREADSAFE, oldPgmVal);
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing null for the authority argument.
     A NullPointerException should be thrown.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(80, " ", " ", null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "authority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing null for the authority argument.
     A NullPointerException should be thrown.
     **/
    public void Var002()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(2, 2, new BigDecimal("0.0"), " ", null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "authority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) passing null for the authority argument.
     A NullPointerException should be thrown.
     **/
    public void Var003()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(true, " ", null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "authority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing an invalid authority argument. (length 0)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(80, " ", " ", "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid authority argument. (length 0)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var005()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(2, 2, new BigDecimal("0.0"), " ", "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) passing an invalid authority argument. (length 0)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var006()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(true, " ", "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing an invalid authority argument. (invalid authorization list)
     An IOException should be thrown.
     **/
    public void Var007()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(80, " ", " ", "DABADLIST");
                failed("No exception creating data area in QTEMP with "+systemObject_);
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2283", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid authority argument. (invalid authorization list)
     An IOException should be thrown.
     **/
    public void Var008()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(2, 2, new BigDecimal("0.0"), " ", "DABADLIST");
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2283", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) passing an invalid authority argument. (invalid authorization list)
     An IOException should be thrown.
     **/
    public void Var009()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(true, " ", "DABADLIST");
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2283", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing null for the description argument.
     A NullPointerException should be thrown.
     **/
    public void Var010()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(80, " ", null, "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "textDescription");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing null for the description argument.
     A NullPointerException should be thrown.
     **/
    public void Var011()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(2, 2, new BigDecimal("0.0"), null, "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "textDescription");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) passing null for the description argument.
     A NullPointerException should be thrown.
     **/
    public void Var012()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(true, null, "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "textDescription");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing an invalid length argument. (too small)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var013()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(0, " ", " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length: ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid length argument. (too small)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var014()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(0, 3, new BigDecimal("0.0"), " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length: ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing an invalid length argument. (too large)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var015()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(2001, " ", " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length: ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid length argument. (too large)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var016()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(25, 3, new BigDecimal("0.0"), " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length: ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing null for the initial value.
     An NullPointerException should be thrown.
     **/
    public void Var017()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(24, null, " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "initialValue");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing an initial value of length 0.
     An NullPointerException should be thrown.
     **/
    public void Var018()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(24, "", " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "initialValue: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing null for the initial value.
     An NullPointerException should be thrown.
     **/
    public void Var019()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(10, 2, null, " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "initialValue");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid decimalPositions argument. (too small)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var020()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(10, -1, new BigDecimal("0.0"), " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "decimalPositions: ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid decimalPositions argument. (too large)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var021()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(10, 10, new BigDecimal("0.0"), " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "decimalPositions: ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing an invalid decimalPositions argument. (greater than length)
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var022()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(5, 6, new BigDecimal("0.0"), " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "decimalPositions: ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) passing a value which is too long for the description argument.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var023()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(32, " ", "123456789012345678901234567890123456789012345678901", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) passing a value which is too long for the description argument.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var024()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(10, 5, new BigDecimal("0.0"), "123456789012345678901234567890123456789012345678901", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) passing a value which is too long for the description argument.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var025()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(true, "123456789012345678901234567890123456789012345678901", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription: ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) when the library does not exist.
     An ObjectDoesNotExistException should be thrown.
     **/
    public void Var026()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(32, " ", " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) when the library does not exist.
     An ObjectDoesNotExistException should be thrown.
     **/
    public void Var027()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(10, 5, new BigDecimal("0.0"), " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) when the library does not exist.
     An ObjectDoesNotExistException should be thrown.
     **/
    public void Var028()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAARA");
            try
            {
                da.create(true, " ", "*USE");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create() when the library does not exist.
     An ObjectDoesNotExistException should be thrown.
     **/
    public void Var029()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAARA");
            try
            {
                da.create();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create() when the library does not exist.
     An ObjectDoesNotExistException should be thrown.
     **/
    public void Var030()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAARA");
            try
            {
                da.create();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create() when the library does not exist.
     An ObjectDoesNotExistException should be thrown.
     **/
    public void Var031()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAARA");
            try
            {
                da.create();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) when the data area already exists.
     An ObjectAlreadyExistsException should be thrown.
     **/
    public void Var032()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            da2.create();
            try
            {
                da.create(100, " ", " ", "*USE");
                da2.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                da2.delete();
                assertExceptionIs(e, "ObjectAlreadyExistsException", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) when the data area already exists.
     An ObjectAlreadyExistsException should be thrown.
     **/
    public void Var033()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            da2.create();
            try
            {
                da.create(10, 5, new BigDecimal("0.0"), " ", "*USE");
                da2.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                da2.delete();
                assertExceptionIs(e, "ObjectAlreadyExistsException", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) when the data area already exists.
     An ObjectAlreadyExistsException should be thrown.
     **/
    public void Var034()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            da2.create();
            try
            {
                da.create(true, " ", "*USE");
                da2.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                da2.delete();
                assertExceptionIs(e, "ObjectAlreadyExistsException", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create() when the data area already exists.
     An ObjectAlreadyExistsException should be thrown.
     **/
    public void Var035()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            da2.create();
            try
            {
                da.create();
                da2.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                da2.delete();
                assertExceptionIs(e, "ObjectAlreadyExistsException", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create() when the data area already exists.
     An ObjectAlreadyExistsException should be thrown.
     **/
    public void Var036()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            da2.create();
            try
            {
                da.create();
                da2.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                da2.delete();
                assertExceptionIs(e, "ObjectAlreadyExistsException", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create() when the data area already exists.
     An ObjectAlreadyExistsException should be thrown.
     **/
    public void Var037()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            da2.create();
            try
            {
                da.create();
                da2.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                da2.delete();
                assertExceptionIs(e, "ObjectAlreadyExistsException", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful CharacterDataArea::create(int, String, String, String) calls using valid boundary values for the length argument. (lower bound)
     Verify the data area is created correctly.
     **/
    public void Var038()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with length 1.
            da2.create(1, " ", " ", "*USE");

                // Verify create.
	    int length = da.getLength(); 
	    boolean success = (length == 1); 
	    da2.delete();
	    assertCondition(success, "Wrong entry length: 1 != " + length);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful CharacterDataArea::create(int, String, String, String) calls using valid boundary values for the length argument. (upper bound)
     Verify the data area is created correctly.
     **/
    public void Var039()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with length 2000.
            da2.create(2000, " ", " ", "*USE");
                // Verify create.
	    int length = da.getLength(); 
                da2.delete();
                assertCondition(length == 2000, "Wrong entry length: 2000 != " + length);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful DecimalDataArea::create(int, int, BigDecimal, String, String) calls using valid boundary values for the length argument. (lower length bound)
     Verify the data area is created correctly.
     **/
    public void Var040()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with length 1.
            da2.create(1, 0, new BigDecimal("0.0"), " ", "*USE");
	    int length = da.getLength();
                // Verify create.
                da2.delete();
                assertCondition(length == 1, "Wrong entry length (1).");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful DecimalDataArea::create(int, String, String, String) calls using valid boundary values for the length argument. (upper length bound)
     Verify the data area is created correctly.
     **/
    public void Var041()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with length 24.
            da2.create(24, 0, new BigDecimal("0.0"), " ", "*USE");
	    int length = da.getLength(); 
                // Verify create.
                da2.delete();
                assertCondition(length == 24, "Wrong entry length (24).");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful DecimalDataArea::create(int, String, String, String) calls using valid boundary values for the decimalPositions argument. (lower bound)
     Verify the data area is created correctly.
     **/
    public void Var042()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with 0 decimalPositions.
            da2.create(10, 0, new BigDecimal("0.0"), " ", "*USE");
	    int decimalPositions = da.getDecimalPositions(); 
                // Verify create.
                da2.delete();
                assertCondition(decimalPositions == 0, "Wrong number of decimal positions (0).");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful DecimalDataArea::create(int, String, String, String) calls using valid boundary values for the decimalPositions argument. (upper bound)
     Verify the data area is created correctly.
     **/
    public void Var043()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with 9 decimalPositions.
            da2.create(10, 9, new BigDecimal("0.0"), " ", "*USE");
	    int decimalPositions = da.getDecimalPositions(); 
                da2.delete();
                // Verify create.
                assertCondition(decimalPositions == 9, "Wrong number of decimal positions (9).");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do successful DecimalDataArea::create(int, String, String, String) calls using valid boundary values for the decimalPositions argument. (length boundary)
     Verify the data area is created correctly.
     **/
    public void Var044()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
            // Create with 8 decimalPositions and a total length of 8.
            da2.create(8, 8, new BigDecimal("0.0"), " ", "*USE");

                // Verify create.
	    int decimalPositions = da.getDecimalPositions(); 
                da2.delete();
                assertCondition(decimalPositions == 8, "Wrong number of decimal positions (8).");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create() Try to create a data area in a library to which the user has no authority.
     An AS400SecurityException should be thrown.
     **/
    public void Var045()
    {
        try
        {
            String user = systemObject_.getUserId();

            // Delete library.
	    deleteLibrary("DAAUTH"); 
            // Create library.
            cmdRun("CRTLIB LIB(DAAUTH)");
            // Grant library authority for current user
            cmdRun("GRTOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE *READ)");
            // Revoke library authority for current user
            cmdRun("RVKOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE)");

            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");

            try
            {
                da.create();
                failed("Expected exception did not occur user is "+user);
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);

            // Delete library.
		deleteLibrary("DAAUTH");

            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String) Try to create a data area in a library to which the user has no authority.
     An AS400SecurityException should be thrown.
     **/
    public void Var046()
    {
        try
        {
            String user = systemObject_.getUserId();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Create library.
            cmdRun("CRTLIB LIB(DAAUTH)");
            // Grant library authority for current user.
            cmdRun("GRTOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE *READ)");
            // Revoke library authority for current user.
            cmdRun("RVKOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE)");

            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");

            try
            {
                da.create(10, "Initial", " ", "*USE");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }

            // Delete library.
	    deleteLibrary("DAAUTH");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do a successful CharacterDataArea::create().
     Verify the data area is created correctly, using the documented default values for length, initial value, description, and authority.
     **/
    public void Var047()
    {
        CharacterDataArea da = null;
        try
        {
          // Keeps track of reason for failure in multi-part tests.
          String msg = "";
          da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da2.create();
          // Verify create.
          if (da.getLength() != 32)
          {
            msg += "\nWrong length.";
          }
          if (!da.read().equals("                                "))
          {
            msg += "\nWrong initial value.";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals(""))
          {
            msg += "\nWrong text description.";
          }

          assertCondition(msg.equals(""), msg + "\n");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     DecimalDataArea::create() Try to create a data area in a library to which the user has no authority.
     An AS400SecurityException should be thrown.
     **/
    public void Var048()
    {
        try
        {
            String user = systemObject_.getUserId();
            // Delete library.
	    deleteLibrary("DAAUTH");

            // Create library.
            cmdRun("CRTLIB LIB(DAAUTH)");
            // Grant library authority for current user.
            cmdRun("GRTOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE *READ)");
            // Revoke library authority for current user.
            cmdRun("RVKOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE)");

            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");

            try
            {
                da.create();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }

            // Delete library.
	    deleteLibrary("DAAUTH");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String) Try to create a data area in a library to which the user has no authority.
     An AS400SecurityException should be thrown.
     **/
    public void Var049()
    {
        try
        {
            String user = systemObject_.getUserId();
            // Delete library.
	    deleteLibrary("DAAUTH");

            // Create library.
            cmdRun("CRTLIB LIB(DAAUTH)");
            // Grant library authority for current user.
            cmdRun("GRTOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE *READ)");
            // Revoke library authority for current user.
            cmdRun("RVKOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE)");

            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");

            try
            {
                da.create(8, 3, new BigDecimal("1.1"), " ", "*USE");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }

            // Delete library.
	    deleteLibrary("DAAUTH");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do a successful DecimalDataArea::create().
     Verify the data area is created correctly, using the documented default values for length, decimal positions, initial value, description, and authority.
     **/
    public void Var050()
    {
        DecimalDataArea da2 = null;
        try
        {
          // Keeps track of reason for failure in multi-part tests.
          String msg = "";
          DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da2.create();
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 15)
          {
            msg += "\nWrong length.";
          }
          if (da.getDecimalPositions() != 5)
          {
            msg += "\nWrong decimal positions.";
          }
          if (!da.read().equals(new BigDecimal("0.00000")))
          {
            msg += "\nWrong initial value.";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals(""))
          {
            msg += "\nWrong text description.";
          }

          assertCondition(msg.equals(""), msg + "\n");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da2 != null) da2.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     LogicalDataArea::create() Try to create a data area in a library to which the user has no authority.
     An AS400SecurityException should be thrown.
     **/
    public void Var051()
    {
        try
        {
            String user = systemObject_.getUserId();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Create library.
            cmdRun("CRTLIB LIB(DAAUTH)");
            // Grant library authority for current user.
            cmdRun("GRTOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE *READ)");
            // Revoke library authority for current user.
            cmdRun("RVKOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE)");

            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");

            try
            {
                da.create();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }

            // Delete library.
	    deleteLibrary("DAAUTH");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String) Try to create a data area in a library to which the user has no authority.
     An AS400SecurityException should be thrown.
     **/
    public void Var052()
    {
        try
        {
            String user = systemObject_.getUserId();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Create library.
            cmdRun("CRTLIB LIB(DAAUTH)");
            // Grant library authority for current user.
            cmdRun("GRTOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE *READ)");
            // Revoke library authority for current user.
            cmdRun("RVKOBJAUT DAAUTH *LIB " + user + " AUT(*EXECUTE)");

            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");

            try
            {
                da.create(false, " ", "*USE");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Do a successful LogicalDataArea::create().
     Verify the data area is created correctly, using the documented default values for initial value, description, and authority.
     **/
    public void Var053()
    {
        LogicalDataArea da2 = null;
        try
        {
          // Keeps track of reason for failure in multi-part tests.
          String msg = "";
          LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da2.create();
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 1)
          {
            msg += "\nWrong length.";
          }
          if (da.read() != false)
          {
            msg += "\nWrong initial value.";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals(""))
          {
            msg += "\nWrong text description.";
          }

          assertCondition(msg.equals(""), msg + "\n");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da2 != null) da2.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Do a successful CharacterDataArea::create(int, String, String, String).
     Verify the data area is created correctly and contains the specified values for length, initial value, description, and authority.
     **/
    public void Var054()
    {
        CharacterDataArea da2 = null;
        try
        {
          // Keeps track of reason for failure in multi-part tests.
          String msg = "";

          CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");

          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da2.create(88, "Initial Value", "VAR039", "*ALL");
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 88)
          {
            msg += "\nWrong length.";
          }
          if (!da.read().trim().equals("Initial Value"))
          {
            msg += "\nWrong initial value.";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals("VAR039"))
          {
            msg += "\nWrong text description.";
          }

          assertCondition(msg.equals(""), msg + "\n");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da2 != null) da2.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Do a successful DecimalDataArea::create(int, int, BigDecimal, String, String).
     Verify the data area is created correctly and contains the specified values for length, decimal positions, initial value, description, and authority.
     **/
    public void Var055()
    {
        DecimalDataArea da2 = null;
        try
        {
          // Keeps track of reason for failure in multi-part tests.
          String msg = "";

          DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");

          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da2.create(12, 6, new BigDecimal("654321.123456"), "VAR040", "*ALL");
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 12)
          {
            msg += "\nWrong length.";
          }
          if (da.getDecimalPositions() != 6)
          {
            msg += "\nWrong decimal positions.";
          }
          if (!da.read().equals(new BigDecimal("654321.123456")))
          {
            msg += "\nWrong initial value.";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals("VAR040"))
          {
            msg += "\nWrong text description.";
          }

          assertCondition(msg.equals(""), msg + "\n");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da2 != null) da2.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Do a successful LogicalDataArea::create(boolean, String, String).
     Verify the data area is created correctly and contains the specified values for initial value, description, and authority.
     **/
    public void Var056()
    {
         LogicalDataArea da2 = null;
        try
        {
          // Keeps track of reason for failure in multi-part tests.
          String msg = "";
          LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");

          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da2.create(true, "VAR041", "*ALL");
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 1)
          {
            msg += "\nWrong length.";
          }
          if (da.read() != true)
          {
            msg += "\nWrong initial value.";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals("VAR041"))
          {
            msg += "\nWrong text description.";
          }

          assertCondition(msg.equals(""), msg + "\n");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da2 != null) da2.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String)
     Verify the data area is created with the correct authority. (*EXCLUDE)
     **/
    public void Var057()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, "Initial", " ", "*EXCLUDE");
            try
            {
                da2.read();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String)
     Verify the data area is created with the correct authority. (*ALL)
     **/
    public void Var058()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, "Initial", " ", "*ALL");

            da2.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String)
     Verify the data area is created with the correct authority. (*CHANGE)
     **/
    public void Var059()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, "Initial", " ", "*CHANGE");
            try
            {
                da2.delete();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String)
     Verify the data area is created with the correct authority. (*USE)
     **/
    public void Var060()
    {
	boolean errorReported = false; 
        try
        {
            CharacterDataArea da = new CharacterDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, "Initial", " ", "*USE");
            try
            {
                da2.delete();
		da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
		da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
	     failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create()
     Verify the data area is created with the correct default authority of *LIBCRTAUT.
     **/
    public void Var061()
    {
        try
        {
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)", "CPF2105");

            // Create the authorization list.
            cmdRun("CRTAUTL AUTL(DAAUTH) AUT(*USE)");
            // Create the library using the authorization list.
            cmdRun("CRTLIB LIB(DAAUTH) AUT(DAAUTH)");

            // Create the data area with the same authority as the library.
            CharacterDataArea da = new CharacterDataArea(pwrSys_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            da.create();

            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            try
            {
                da2.delete();
                da.delete();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)");
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }


        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     CharacterDataArea::create(int, String, String, String)
     Verify the data area is created with the correct authority. (*LIBCRTAUT)
     **/
    public void Var062()
    {
        try
        {
            // Setup for this variation
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)", "CPF2105");

            // Create the authorization list.
            cmdRun("CRTAUTL AUTL(DAAUTH) AUT(*USE)");
            // Create the library using the authorization list.
            cmdRun("CRTLIB LIB(DAAUTH) AUT(DAAUTH)");

            // Create the data area with the same authority as the library
            CharacterDataArea da = new CharacterDataArea(pwrSys_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            da.create(10, "Initial", " ", "*LIBCRTAUT");

            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            try
            {
                da2.delete();
                da.delete();
	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String)
     Verify the data area is created with the correct authority. (*EXCLUDE)
     **/
    public void Var063()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, 2, new BigDecimal("1.1"), " ", "*EXCLUDE");
            try
            {
                da2.read();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String)
     Verify the data area is created with the correct authority. (*ALL)
     **/
    public void Var064()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, 2, new BigDecimal("1.1"), " ", "*ALL");

            da2.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String)
     Verify the data area is created with the correct authority. (*CHANGE)
     **/
    public void Var065()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, 2, new BigDecimal("1.1"), " ", "*CHANGE");
            try
            {
                da2.delete();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String)
     Verify the data area is created with the correct authority. (*USE)
     **/
    public void Var066()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(10, 2, new BigDecimal("1.1"), " ", "*USE");
            try
            {
                da2.delete();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create()
     Verify the data area is created with the correct default authority of *LIBCRTAUT.
     **/
    public void Var067()
    {
        try
        {
            // Setup for this variation
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)", "CPF2105");

            // Create the authorization list.
            cmdRun("CRTAUTL AUTL(DAAUTH) AUT(*USE)");
            // Create the library using the authorization list.
            cmdRun("CRTLIB LIB(DAAUTH) AUT(DAAUTH)");

            // Create the data area with the same authority as the library.
            DecimalDataArea da = new DecimalDataArea(pwrSys_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            da.create();

            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            try
            {
                da2.delete();
                da.delete();

	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();

	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     DecimalDataArea::create(int, int, BigDecimal, String, String)
     Verify the data area is created with the correct authority. (*LIBCRTAUT)
     **/
    public void Var068()
    {
        try
        {
            // Setup for this variation
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)", "CPF2105");

            // Create the authorization list.
            cmdRun("CRTAUTL AUTL(DAAUTH) AUT(*USE)");
            // Create the library using the authorization list.
            cmdRun("CRTLIB LIB(DAAUTH) AUT(DAAUTH)");

            // Create the data area with the same authority as the library.
            DecimalDataArea da = new DecimalDataArea(pwrSys_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            da.create(10, 2, new BigDecimal("1.1"), " ", "*LIBCRTAUT");

            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            try
            {
                da2.delete();
                da.delete();
	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String)
     Verify the data area is created with the correct authority. (*EXCLUDE)
     **/
    public void Var069()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(true, " ", "*EXCLUDE");
            try
            {
                da2.read();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String)
     Verify the data area is created with the correct authority. (*ALL)
     **/
    public void Var070()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(true, " ", "*ALL");

            da2.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String)
     Verify the data area is created with the correct authority. (*CHANGE)
     **/
    public void Var071()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(true, " ", "*CHANGE");
            try
            {
                da2.delete();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String)
     Verify the data area is created with the correct authority. (*USE)
     **/
    public void Var072()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(pwrSys_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/DASEC.LIB/CRTTEST.DTAARA");

            da.create(true, " ", "*USE");
            try
            {
                da2.delete();
                da.delete();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create()
     Verify the data area is created with the correct default authority of *LIBCRTAUT.
     **/
    public void Var073()
    {
        try
        {
            // Setup for this variation
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)", "CPF2105");

            // Create the authorization list.
            cmdRun("CRTAUTL AUTL(DAAUTH) AUT(*USE)");
            // Create the library using the authorization list.
            cmdRun("CRTLIB LIB(DAAUTH) AUT(DAAUTH)");

            // Create the data area with the same authority as the library.
            LogicalDataArea da = new LogicalDataArea(pwrSys_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            da.create();

            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            try
            {
                da2.delete();
                da.delete();
	    // Delete library.
	    deleteLibrary("DAAUTH");
	    // Delete the authorization list.
		cmdRun("DLTAUTL AUTL(DAAUTH)");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)");
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     LogicalDataArea::create(boolean, String, String)
     Verify the data area is created with the correct authority. (*LIBCRTAUT)
     **/
    public void Var074()
    {
        try
        {
            // Setup for this variation
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)", "CPF2105");

            // Create the authorization list.
            cmdRun("CRTAUTL AUTL(DAAUTH) AUT(*USE)");
            // Create the library using the authorization list.
            cmdRun("CRTLIB LIB(DAAUTH) AUT(DAAUTH)");

            // Create the data area with the same authority as the library.
            LogicalDataArea da = new LogicalDataArea(pwrSys_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            da.create(true, " ", "*LIBCRTAUT");

            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/DAAUTH.LIB/CRTTEST.DTAARA");
            try
            {
                da2.delete();
                da.delete();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)");
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                da.delete();
            // Delete library.
	    deleteLibrary("DAAUTH");
            // Delete the authorization list.
            cmdRun("DLTAUTL AUTL(DAAUTH)");
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify description values with boundary lengths using
     CharacterDataArea::create(int, String, String, String). (length of 0)
     **/
    public void Var075()
    {
        CharacterDataArea da = null;
        try
        {
          String desc = "                                                  ";
          da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da.create(80, "initial value", "", "*USE");
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          assertCondition(getTextDescription(obj).equals(desc.trim()), "Wrong description (length 0).");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Verify description values with boundary lengths using
     DecimalDataArea::create(int, int, BigDecimal, String, String). (length of 0)
     **/
    public void Var076()
    {
        DecimalDataArea da = null;
        try
        {
          String desc = "                                                  ";
          da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da.create(12, 6, new BigDecimal("0.0"), "", "*USE");
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          assertCondition(getTextDescription(obj).equals(desc.trim()), "Wrong description (length 0).");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Verify description values with boundary lengths using
     LogicalDataArea::create(boolean, String, String). (length of 0)
     **/
    public void Var077()
    {
        LogicalDataArea da = null;
        try
        {
          String desc = "                                                  ";
          da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da.create(true, "", "*USE");
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          assertCondition(getTextDescription(obj).equals(desc.trim()), "Wrong description (length 0).");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Verify description values with boundary lengths using
     CharacterDataArea::create(int, String, String, String). (length of 50)
     **/
    public void Var078()
    {
        CharacterDataArea da = null;
        try
        {
          String desc = "12345678901234567890123456789012345678901234567890";
          da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da.create(80, "initial value", desc, "*USE");
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          assertCondition(getTextDescription(obj).equals(desc), "Wrong description (length 50).");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Verify description values with boundary lengths using
     DecimalDataArea::create(int, int, BigDecimal, String, String). (length of 50)
     **/
    public void Var079()
    {
        DecimalDataArea da = null;
        try
        {
          String desc = "12345678901234567890123456789012345678901234567890";
          da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da.create(12, 6, new BigDecimal("0.0"), desc, "*USE");
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          assertCondition(getTextDescription(obj).equals(desc), "Wrong description (length 50).");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Verify description values with boundary lengths using
     LogicalDataArea::create(boolean, String, String). (length of 50)
     **/
    public void Var080()
    {
        LogicalDataArea da = null;
        try
        {
          String desc = "12345678901234567890123456789012345678901234567890";
          da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          // Since we're using QTEMP, we need to keep everything on the same thread.
          setThreadsafeProperties();
          da.create(true, desc, "*USE");
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          assertCondition(getTextDescription(obj).equals(desc), "Wrong description (length 50).");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
          try { if (da != null) da.delete(); } catch (Throwable e) { e.printStackTrace(); }
          restoreThreadsafeProperties();
        }
    }

    /**
     Create a data area that has *CURLIB for the library using CharacterDataArea::create().
     Verify the data area is created in QGPL.  (QGPL is the default for current library.  This test will fail if the user profile has changed its current library.)
     **/
    public void Var081()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAARA");
            da2.create();

                // Verify data area now exists in QGPL.
                da.read();
                da2.delete();
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Create a data area that has *CURLIB for the library using DecimalDataArea::create().
     Verify the data area is created in QGPL.  (QGPL is the default for current library.  This test will fail if the user profile has changed its current library.)
     **/
    public void Var082()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAARA");
            da2.create();
                // Verify data area now exists in QGPL.
                da.read();
                da2.delete();
                succeeded();

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Create a data area that has *CURLIB for the library using LogicalDataArea::create().
     Verify the data area is created in QGPL.  (QGPL is the default for current library.  This test will fail if the user profile has changed its current library.)
     **/
    public void Var083()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAARA");
            da2.create();
                // Verify data area now exists in QGPL.
                da.read();
                da2.delete();
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Create a data area that has *CURLIB for the library using CharacterDataArea::create(int, String, String, String).
     Verify the data area is created in QGPL.  (QGPL is the default for current library.  This test will fail if the user profile has changed its current library.)
     **/
    public void Var084()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAARA");
            da2.create(80, "initial value", "VAR060", "*USE");
                // Verify data area now exists in QGPL.
                da.read();
                da2.delete();
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Create a data area that has *CURLIB for the library using DecimalDataArea::create(int, int, BigDecimal, String, String).
     Verify the data area is created in QGPL.  (QGPL is the default for current library.  This test will fail if the user profile has changed its current library.)
     **/
    public void Var085()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAARA");
            da2.create(12, 6, new BigDecimal("0.0"), "VAR061", "*USE");
                // Verify data area now exists in QGPL.
                da.read();
                da2.delete();
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Create a data area that has *CURLIB for the library using LogicalDataArea::create(boolean, String, String).
     Verify the data area is created in QGPL.  (QGPL is the default for current library.  This test will fail if the user profile has changed its current library.)
     **/
    public void Var086()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAARA");
            da2.create(true, "VAR062", "*USE");
                // Verify data area now exists in QGPL.
                da.read();
                da2.delete();
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
