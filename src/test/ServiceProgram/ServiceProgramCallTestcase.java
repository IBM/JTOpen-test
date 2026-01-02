///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ServiceProgramCallTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.ServiceProgram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectList;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;

import test.Testcase;

/**
 The ServiceProgramCallTestcase class tests the methods of ServiceProgramCall.
 <p>This tests the following ServiceProgramCall methods:
 <ul>
 <li>constructor
 <li>getIntegerReturnValue()
 <li>getProcedureName()
 <li>getReturnValue()
 <li>getReturnValueFormat()
 <li>run()
 <li>setProcedureName()
 <li>setReturnValueFormat()
 </ul>
 **/
public class ServiceProgramCallTestcase extends Testcase implements VetoableChangeListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ServiceProgramCallTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ServiceProgramCallTest.main(newArgs); 
   }
    private static final boolean DEBUG = false;
    private String serviceProgramName_ = "/QSYS.LIB/JAVASP.LIB/ENTRYPTS.SRVPGM";

    void fillBuff(byte[] buffer, byte value)
    {
        for (int i = 0; i < buffer.length; ++i)
        {
            buffer[i] = value;
        }
    }

    boolean verifyBuff(byte[] buffer, byte value)
    {
        for (int i = 0; i < buffer.length; ++i)
        {
            if (buffer[i] != value)
            {
                output_.println("Buffer Compare Failed: " + buffer[i]);
                return false;
            }
        }
        return true;
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
    {
        throw new PropertyVetoException("Property vetoed", e);
    }

    /**
     Method tested:  ServiceProgramCall()
     - Ensure that this method runs well.
     **/
    public void Var001()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall();
            assertCondition(true, "got s="+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system)
     - Ensure that this method runs well.
     **/
    public void Var002()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_);
            assertCondition(true, "got s="+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the parameter procedureName is null.
     **/
    public void Var003()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, null, ServiceProgramCall.RETURN_INTEGER, paramList);
            failed("Expected exception did not occur."+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that ExtendedIllegalArgumentException will be thrown when the parameter returnValueFormat is less than 0.
     **/
    public void Var004()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", -1, paramList);
            failed("Expected exception did not occur. for "+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that ExtendedIllegalArgumentException will be thrown when the parameter returnValueFormat is larger than 2.
     **/
    public void Var005()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", 3, paramList);
            failed("Expected exception did not occur for "+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the parameter list is null.
     **/
    public void Var006()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", 1, null);
            failed("Expected exception did not occur for "+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that this method runs well when the parameter is set properly.
     **/
    public void Var007()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            assertCondition(true, "got s="+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, ProgramParameter[] parmlist)
     - Ensure that NullPointerException will be thrown if procedure name is null.
     **/
    public void Var008()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, null);
            failed("Expected exception did not occur for s="+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     - Ensure that nullPointerException will be thrown if procedure name is null.
     **/
    public void Var009()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, null, paramList);
            failed("Expected exception did not occur."+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown if program parameter list is null.
     **/
    public void Var010()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", null);
            failed("Expected exception did not occur."+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     - Ensure that this method runs well when the parameter is set properly.
     **/
    public void Var011()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", paramList);
            assertCondition(true, "got s="+s);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getIntegerReturnValue()
     - Ensure that ExtendedIllegalStateException will be thrown if no value returns.
     **/
    public void Var012()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "v_i", ServiceProgramCall.NO_RETURN_VALUE, paramList))
            {
                s.getIntegerReturnValue();
                failed("no exception");
            }
            else
            {
                failed();
                output_.println("Call to run() failed.");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException");
        }
    }

    /**
     Method tested:  getProcedureName()
     - Ensure that this method runs well.
     **/
    public void Var013()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, "/QSYS.LIB/JAVASP.LIB/ENTRYPTS.SRVPGM", "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            assertCondition(s.getProcedureName().equals("i_i"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getReturnValueFormat()
     - Ensure that this method runs well.
     **/
    public void Var014()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            assertCondition(s.getReturnValueFormat() == ServiceProgramCall.RETURN_INTEGER);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run()
     - Ensure that ExtendedIllegalStateException will be thrown when the as400 system is null.
     **/
    public void Var015()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that ExtendedIllegalStateException will be thrown when service program is null.
     **/
    public void Var016()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s1 = new ServiceProgramCall();
            s1.setSystem(systemObject_);
            s1.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that ExtendedIllegalStateException will be thrown when the procedure name is null.
     **/
    public void Var017()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(serviceProgramName_);
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalStateException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that this method run well.
     **/
    public void Var018()
    {
        try
        {
            AS400Bin4 bin4 = new AS400Bin4();
            ProgramParameter[] paramList = new ProgramParameter[1];

            byte[] parm1 = bin4.toBytes(123);
            paramList[0] = new ProgramParameter(parm1, 0);

            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            if (!s.run())
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
                return;
            }
            int returnValue = s.getIntegerReturnValue();
            if (returnValue == 124)
            {
                succeeded();
            }
            else
            {
                failed("Wrong return value.  Expected 124, received " + returnValue);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that these methods run well with procedure i_i.
     **/
    public void Var019()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                assertCondition(s.getIntegerReturnValue() == 10);
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that this methods run well with procedure v_v.
     **/
    public void Var020()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[0];
            ServiceProgramCall s = new ServiceProgramCall();
            if (s.run(systemObject_, serviceProgramName_, "v_v", 0, paramList))
            {
                succeeded();
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that this methods run well with procedure v_i.
     **/
    public void Var021()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "v_i", ServiceProgramCall.NO_RETURN_VALUE, paramList))
            {
                succeeded();
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that this methods run well with procedure v_p.
     **/
    public void Var022()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)1);
            paramList[0] = new ProgramParameter(parm, 50);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "v_p", ServiceProgramCall.NO_RETURN_VALUE, paramList))
            {
                if ((paramList[0].getOutputData().length == 50) && (verifyBuff(paramList[0].getOutputData(), (byte)0x71)))
                {
                    succeeded();
                }
                else
                {
                    failed("data incorrect, length = " + paramList[0].getOutputData().length);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getReturnValue() and run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that these methods run well with procedure i_p.
     **/
    public void Var023()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = new byte[50];
            fillBuff(parm, (byte)1);
            paramList[0] = new ProgramParameter(parm, 50);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_p", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();
                if (i == 0)
                {
                    if ((paramList[0].getOutputData().length == 50) && (verifyBuff(paramList[0].getOutputData(), (byte)0x71)))
                    {
                        succeeded();
                    }
                    else
                    {
                        failed("data incorrect, length = " + paramList[0].getOutputData().length);
                    }
                }
                else
                {
                    failed("wrong return value: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getReturnValue() and run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that these methods run well with i_iiiiiii.
     **/
    public void Var024()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[7];

            AS400Bin4 bin4 = new AS400Bin4();
            for(int i = 0; i < 7; ++i)
            {
                byte[] parm = bin4.toBytes(i+1);
                ProgramParameter p = new ProgramParameter(parm);
                p.setParameterType(ProgramParameter.PASS_BY_VALUE);
                paramList[i] = new ProgramParameter(parm);
            }
            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_iiiiiii", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();
                assertCondition(i == 28);
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getReturnValue() and run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that these methods run well with i_ppppppp.
     **/
    public void Var025()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[7];

            AS400Bin4 bin4 = new AS400Bin4();
            for (int i = 0; i < 7; ++i)
            {
                byte[] parm = new byte[50];
                fillBuff(parm, (byte)(i+1));
                paramList[i] = new ProgramParameter(parm, 50);
                paramList[i].setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            }

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_ppppppp", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if ((i == 0) && (verifyBuff(paramList[0].getOutputData(), (byte)0x71)) && (verifyBuff(paramList[1].getOutputData(), (byte)0x72)) && (verifyBuff(paramList[2].getOutputData(), (byte)0x73)) && (verifyBuff(paramList[3].getOutputData(), (byte)0x74)) && (verifyBuff(paramList[4].getOutputData(), (byte)0x75)) && (verifyBuff(paramList[5].getOutputData(), (byte)0x76)) && (verifyBuff(paramList[6].getOutputData(), (byte)0x77)))
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect");
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run(String serviceProgram,ProgramParameter[] paramlist)
     - Ensure that these methods run well with procedure i_i.
     **/
    public void Var026()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_);

            s.setProcedureName("i_i");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);

            AS400Bin4 bin4 = new AS400Bin4();
            ProgramParameter[] paramList = new ProgramParameter[1];

            byte[] parm1 = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm1, 0);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            if (s.run(serviceProgramName_,paramList))
            {
                Integer I = (Integer) bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();
                assertCondition(i == 10);
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run()
     - Ensure that this method will return false if the parameters are not set correctly.
     **/
    public void Var027()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "noSuchProcedure", ServiceProgramCall.RETURN_INTEGER, paramList);
            assertCondition(false == s.run());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run(String serviceProgram,ProgramParameter[] paramlist)
     - Ensure that InternalErrorException will be thrown if procedure name is not set correctely.
     **/
    public void Var028()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_);

            s.setProcedureName("i_i");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);

            AS400Bin4 bin4 = new AS400Bin4();
            ProgramParameter[] paramList = new ProgramParameter[1];

            byte[] parm1 = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm1, 0);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            s.run(null, paramList);

            failed("Excepted exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that InternalErrorException will be thrown if return value format is not set correctely.
     **/
    public void Var029()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            ServiceProgramCall s = new ServiceProgramCall();

            s.run(systemObject_, serviceProgramName_, "i_i", -1, paramList);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that InternalErrorException will be thrown if procedure name is null.
     **/
    public void Var030()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            paramList[0] = new ProgramParameter(parm);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_VALUE);

            ServiceProgramCall s = new ServiceProgramCall();

            s.run(systemObject_, serviceProgramName_, null, 1, paramList);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that this method run well.
     **/
    public void Var031()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            AS400Bin4 bin4 = new AS400Bin4();

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)1);
            paramList[0] = new ProgramParameter(parm, 50);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            ServiceProgramCall s = new ServiceProgramCall(systemObject_);
            s.setProgram(serviceProgramName_);
            s.setProcedureName("i_p");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            s.setParameterList(paramList);

            s.addVetoableChangeListener(this);

            if (s.run())
            {
                Integer I = (Integer) bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();
                assertCondition(i == 0);
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  setProcedureName(String procedureName)
     - Ensure that ExtendedIllegalArgumentException will be thrown when the procedure name is null.
     **/
    public void Var032()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall();
            s.setProcedureName(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  setProcedureName(String procedureName)
     - Ensure that this method runs well.
     **/
    public void Var033()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setProcedureName("aaa");
            assertCondition(s.getProcedureName().equals("aaa"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  setReturnValueFormat(int returnValueFormat)
     - Ensure that ExtendedIllegalArgumentException will be thrown when return value format is less than 0.
     **/
    public void Var034()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setReturnValueFormat(-1);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  setReturnValueFormat(int returnValueFormat)
     - Ensure that ExtendedIllegalArgumentException will be thrown when return value format is larger than 1.
     **/
    public void Var035()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setReturnValueFormat(2);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  setReturnValueFormat(int returnValueFormat)
     - Ensure that this method run well when return value format is correct.
     **/
    public void Var036()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setReturnValueFormat(0);
            s.setReturnValueFormat(1);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  2 parameters
     **/
    public void Var037()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[2];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm1 = bin4.toBytes(15);
            paramList[0] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(22);
            paramList[1] = new ProgramParameter(parm2, 0);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_ii", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if (i == 37)
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  3 parameters
     **/
    public void Var038()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[3];

            AS400Bin4 bin4 = new AS400Bin4();

            byte[] parm1 = bin4.toBytes(30);
            paramList[0] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(60);
            paramList[2] = new ProgramParameter(parm2, 0);

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)0x01);
            paramList[1] = new ProgramParameter(parm, 50);
            paramList[1].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_ipi", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if ((i == 90) && (verifyBuff(paramList[1].getOutputData(), (byte)0x71)))
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  4 parameters
     **/
    public void Var039()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[4];

            AS400Bin4 bin4 = new AS400Bin4();

            byte[] parm1 = bin4.toBytes(30);
            paramList[1] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(60);
            paramList[3] = new ProgramParameter(parm2, 0);

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)0x01);
            paramList[0] = new ProgramParameter(parm, 50);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            byte[] parm2p = new byte[50];
            fillBuff(parm2p, (byte)0x02);
            paramList[2] = new ProgramParameter(parm2p, 50);
            paramList[2].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_pipi", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if ((i == 90) && (verifyBuff(paramList[0].getOutputData(), (byte)0x71)) && (verifyBuff(paramList[2].getOutputData(), (byte)0x72)))
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  5 parameters
     **/
    public void Var040()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[5];

            AS400Bin4 bin4 = new AS400Bin4();

            byte[] parm1 = bin4.toBytes(30);
            paramList[1] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(60);
            paramList[2] = new ProgramParameter(parm2, 0);

            byte[] parm3 = bin4.toBytes(90);
            paramList[3] = new ProgramParameter(parm3, 0);

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)0x01);
            paramList[0] = new ProgramParameter(parm, 50);
            paramList[0].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            byte[] parm2p = new byte[50];
            fillBuff(parm2p, (byte)0x02);
            paramList[4] = new ProgramParameter(parm2p, 50);
            paramList[4].setParameterType(ProgramParameter.PASS_BY_REFERENCE);

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_piiip", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer) bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if ((i == 180) && (verifyBuff(paramList[0].getOutputData(), (byte)0x71)) && (verifyBuff(paramList[4].getOutputData(), (byte)0x72)))
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  6 parameters
     **/
    public void Var041()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[6];

            AS400Bin4 bin4 = new AS400Bin4();

            byte[] parm1 = bin4.toBytes((short)30);
            paramList[3] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(60);
            paramList[4] = new ProgramParameter(parm2, 0);

            byte[] parm3 = bin4.toBytes(60);
            paramList[5] = new ProgramParameter(parm3, 0);

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)0x01);
            ProgramParameter p0 = new ProgramParameter(parm, 50);
            p0.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[0] = p0;

            byte[] parm2p = new byte[50];
            fillBuff(parm2p, (byte)0x02);
            ProgramParameter p1 = new ProgramParameter(parm2p, 50);
            p1.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[1] = p1;

            byte[] parm3p = new byte[50];
            fillBuff(parm3p, (byte)0x03);
            ProgramParameter p2 = new ProgramParameter(parm3p, 50);
            p2.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[2] = p2;

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "i_pppiii", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer) bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if ((i == 150) && (verifyBuff(p0.getOutputData(), (byte)0x71)) && (verifyBuff(p1.getOutputData(), (byte)0x72)) && (verifyBuff(p2.getOutputData(), (byte)0x73)))
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the system name is null.
     **/
    public void Var042()
    {
        ProgramParameter[] paramList = new ProgramParameter[6];

        try
        {
            ServiceProgramCall s = new ServiceProgramCall(null, serviceProgramName_, paramList);
            failed("Expected exception did not occur."+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the program name is null.
     **/
    public void Var043()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[6];
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, null, paramList);
            failed("Expected exception did not occur."+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the parm list is null.
     **/
    public void Var044()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, null);
            failed("Expected exception did not occur."+s);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, ProgramParameter[] parameterList)
     Method tested:  6 parameters
     **/
    public void Var045()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[6];

            AS400Bin4 bin4 = new AS400Bin4();

            byte[] parm1 = bin4.toBytes((short)30);
            paramList[3] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(60);
            paramList[4] = new ProgramParameter(parm2, 0);

            byte[] parm3 = bin4.toBytes(60);
            paramList[5] = new ProgramParameter(parm3, 0);

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)0x01);
            ProgramParameter p0 = new ProgramParameter(parm, 50);
            p0.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[0] = p0;

            byte[] parm2p = new byte[50];
            fillBuff(parm2p, (byte)0x02);
            ProgramParameter p1 = new ProgramParameter(parm2p, 50);
            p1.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[1] = p1;

            byte[] parm3p = new byte[50];
            fillBuff(parm3p, (byte)0x03);
            ProgramParameter p2 = new ProgramParameter(parm3p, 50);
            p2.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[2] = p2;

            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, paramList);
            s.setProcedureName("i_pppiii");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);

            if (s.run())
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if ((i == 150) && (verifyBuff(p0.getOutputData(), (byte)0x71)) && (verifyBuff(p1.getOutputData(), (byte)0x72)) && (verifyBuff(p2.getOutputData(), (byte)0x73)))
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getErrono
     **/
    public void Var046()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Text text = new AS400Text(4, 65535, systemObject_);
            byte[] data = text.toBytes("fred");
            ProgramParameter p0 = new ProgramParameter(data);
            p0.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[0] = p0;

            ServiceProgramCall s = new ServiceProgramCall(systemObject_, "/QSYS.LIB/QP0ZCPA.SRVPGM", paramList);
            s.setProcedureName("Qp0zDltEnv");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);

            if (s.run())
            {
                if (s.getIntegerReturnValue() == -1)
                {
                    if (s.getErrno() == 3025)
                    {
                        succeeded();
                    }
                    else
                    {
                        failed("errno is incorrect: " + s.getErrno());
                    }
                }
                else
                {
                    failed("return code is wrong: " + s.getIntegerReturnValue());
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    public void Var047()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            byte[] parm = new byte[50];
            fillBuff(parm, (byte)0x01);
            ProgramParameter p0 = new ProgramParameter(parm, 50);
            p0.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            paramList[0] = p0;

            ServiceProgramCall s = new ServiceProgramCall();

            if (s.run(systemObject_, serviceProgramName_, "v_p", ServiceProgramCall.NO_RETURN_VALUE, paramList))
            {
                if ((p0.getOutputData().length == 50) && (verifyBuff(p0.getOutputData(), (byte)0x71)))
                {
                    succeeded();
                }
                else
                {
                    failed("data incorrect, length = " + p0.getOutputData().length);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run()
     - Ensure that the SPC runs successfully, and the message list is empty.
     **/
    public void Var048()
    {
        try
        {
            AS400Bin4 bin4 = new AS400Bin4();
            ProgramParameter[] paramList = new ProgramParameter[1];

            byte[] parm1 = bin4.toBytes(123);
            paramList[0] = new ProgramParameter(parm1, 0);

            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            if (!s.run())
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
                return;
            }

            int returnValue = s.getIntegerReturnValue();
            if (returnValue == 124)
            {
                AS400Message[] ml = s.getMessageList();
                if (ml.length == 0)
                {
                    succeeded();
                }
                else
                {
                    failed("Unexpected message in message list.");
                }
            }
            else
            {
                failed("Wrong return value.  Expected 124, received " + returnValue);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    /**
     Entry Point Name CCSID
     **/
    public void Var049()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[2];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm1 = bin4.toBytes(15);
            paramList[0] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(22);
            paramList[1] = new ProgramParameter(parm2, 0);

            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(serviceProgramName_);
            s.setProcedureName("i_ii", 37);
            s.setParameterList(paramList);
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);


            if (s.run())
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();

                if (i == 37)
                {
                    succeeded();
                }
                else
                {
                    failed("returned data incorrect: " + i);
                }
            }
            else
            {
                failed();
                output_.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    output_.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Entry Point Name CCSID
     **/
    public void Var050()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[2];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm1 = bin4.toBytes(15);
            paramList[0] = new ProgramParameter(parm1, 0);

            byte[] parm2 = bin4.toBytes(22);
            paramList[1] = new ProgramParameter(parm2, 0);

            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(serviceProgramName_);
            s.setProcedureName("i_ii", 5026);
            s.setParameterList(paramList);
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);


            if (s.run())
            {
                failed("no exception");
            }
            else
            {
                AS400Message[] messageList = s.getMessageList();

                if (messageList[0].getID().startsWith("CPF226E"))
                    succeeded();
                else
                    failed("wrong message.  Expected CPF226E.  Received " + messageList[0].getText());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  ServiceProgramCall(system, svcProgram, procedure, parmList)
     Note: The called service program requires that the receiver variable (parm 1)
     be aligned on a 16-byte boundary.
     **/
    public void Var051()
    {
      int attribute1 = ObjectDescription.LIBRARY;
      int attribute2 = ObjectDescription.NAME;
      boolean succeeded = true;
      StringBuffer sb = new StringBuffer();

      try
      {
        // Get a list of all the *JRN objects (journals) on the system.

        ObjectList list = new ObjectList(systemObject_, ObjectList.LIBRARY_LIST, ObjectList.ALL, "*JRN"); // system, objLib, objName, objType

        list.addObjectAttributeToRetrieve(attribute1);
        list.addObjectAttributeToRetrieve(attribute2);

        @SuppressWarnings("rawtypes")
        Enumeration journals = list.getObjects();
        ObjectDescription jrn = null;
        int count = 0;
        while (journals.hasMoreElements())
        {
          jrn = (ObjectDescription) journals.nextElement();
          count++;
          String jrnLib  = (String)jrn.getValue(attribute1);
          String jrnName = (String)jrn.getValue(attribute2);
          if (DEBUG) output_.println(jrnLib+"/"+jrnName);

          // Call the service program that lists the entries in the journal.
          // This service program requires that the first parm be 16-byte aligned.

	  // If the normal object does not work, try the pwrSys_ object

	      if (!listJournalEntries(systemObject_, jrnLib, jrnName, sb)) {
		  if (!listJournalEntries(pwrSys_, jrnLib, jrnName, sb)) {
		      succeeded = false;
		      sb.append("Call to listJournalEntries failed\n");
		  }
	      }

        }
        list.close(); 
	if (count == 0)  {
	    failed("No journals were found on system.");
	} else  {
	    assertCondition(succeeded, sb.toString());
	}
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception occurred.");
      }
    }


  public   boolean listJournalEntries(AS400 system, String jrn_Lib_Name, String jrn_Name, StringBuffer sb)
  {
    boolean succeeded = true;
    AS400Bin4 bin4 = new AS400Bin4();
    AS400Text text8 = new AS400Text(8,system);
    AS400Text text10 = new AS400Text(10,system);
    @SuppressWarnings("unused")
    AS400ZonedDecimal zoned20 = new AS400ZonedDecimal(20,0);
    AS400Text text20 = new AS400Text(20,system); 
    // test : read sequence >= 1
    byte toto[] = new byte[36];
    // 1 variable length entry
    bin4.toBytes(1, toto, 0);
    // The following table defines the format for the variable length records.
    // Offset  Type  Field
    // Dec   Hex
    // 0   0   BINARY(4)   Length of variable length record
    // 4   4   BINARY(4)   Key
    // 8   8   BINARY(4)   Length of data
    // 12  C   CHAR(*)   Data
    bin4.toBytes(32, toto, 4+0);
    bin4.toBytes(2, toto, 4+4);
    bin4.toBytes(20, toto, 4+8);
    /* Use the *FIRST special value instead of 1 */
    /* for some reason 1 returns an error when the starting sequence number is 1133304 */ 
    text20.toBytes("*FIRST",toto,4+12); 
    /* zoned20.toBytes(1, toto, 4+12); */ 

    // Blank fill service program name and library name.
    byte[] serviceProgramBytes = new byte[20];
    for (int i = 0; i < 20; ++i)
    {
      serviceProgramBytes[i] = (byte)0x40;
    }
    text10.toBytes(jrn_Name, serviceProgramBytes, 0);
    text10.toBytes(jrn_Lib_Name, serviceProgramBytes, 10);

    ProgramParameter[] parameterList =
    {
      // QjoRetrieveJournalEntries

      // Receiver variable - Must be aligned on a 16-byte boundary.
      // If not aligned, it will fail with the following error:
      // "CPF6949: Receiver variable not aligned."
      new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, 32000),
      // Length of receiver variable
      new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, bin4.toBytes(32000)),
      // Qualified journal name
      new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, serviceProgramBytes),
      // Format name
      new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, text8.toBytes("RJNE0100")),
      // Optional parameters:
      // Journal entries to retrieve
      new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, toto),
      // Error code
      new ProgramParameter(ProgramParameter.PASS_BY_REFERENCE, bin4.toBytes(0))
    };

    final String PROCEDURE_NAME = "QjoRetrieveJournalEntries";
    final String SERVICE_PROGRAM_NAME = "/QSYS.LIB/QJOURNAL.SRVPGM";
    ServiceProgramCall pgm = new ServiceProgramCall(system, SERVICE_PROGRAM_NAME, PROCEDURE_NAME, parameterList);
    pgm.setAlignOn16Bytes(true);  // QjoRetrieveJournalEntries requires alignment

    boolean ret = false;
    try {
      ret = pgm.run();
    } catch (Exception e) {
      ret = false;
    }

    if (ret)
    {
      byte[] out = parameterList[0].getOutputData();

      int offset = 0;
      int bytesReturned = BinaryConverter.byteArrayToInt(out, offset+0);
      int offsetToFirst = BinaryConverter.byteArrayToInt(out, offset+4);
      int numEntries = BinaryConverter.byteArrayToInt(out, offset+8);
      @SuppressWarnings("deprecation")
      String contHandle = BinaryConverter.bytesToString(out, offset+12, 1);
      if (DEBUG) { output_.println(bytesReturned+","+offsetToFirst+","+numEntries+","+contHandle);
      }
      succeeded = true;  // if we got this far, consider it a success
    }
    else
    {
      AS400Message[] messageList = pgm.getMessageList();
      for (int i = 0; i < messageList.length; ++i)
      {
        // Tolerate "Not authorized to object" errors.
        String messageId =messageList[i].getID(); 
        if (!messageId.equals("CPF9802")) {
          // Force the job log to be printed
          try { 
            CommandCall cc = new CommandCall(system);
            cc.run("DSPJOBLOG OUTPUT(*PRINT)"); 
          } catch (Exception e) { 
            output_.println("Error printing job log"); 
            e.printStackTrace(output_); 
          }
          succeeded = false;
          sb.append("Hit error "+messageList[i].getID() + ": "
                             + messageList[i].getText()+" retrieving journal entries from "+
                             jrn_Lib_Name+"/"+jrn_Name+"\n");
        }
      }
    }

    return succeeded; // if we got this far without throwing an exception, consider it a successful test
  }
}
