///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ServiceProgramCallUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.ServiceProgram;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;

import test.Testcase;

/**
 The ServiceProgramCallUnattendedTestcase class tests the methods of ServiceProgramCall.
 <p>This tests the following ServiceProgramCall methods:
 <ul>
 <li>constructor
 <li>getProcedureName()
 <li>getReturnValue()
 <li>getReturnValueFormat()
 <li>getReturnValueLength()
 <li>run()
 <li>setProcedureName()
 <li>setReturnValueFormat()
 <li>setReturnValueLength()
 </ul>
 **/
public class ServiceProgramCallUnattendedTestcase extends Testcase
{
    private String serviceProgramName_ = "/QSYS.LIB/JAVASP.LIB/ENTRYPTS.SRVPGM";

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the parameter procedureName is null.
     **/
    public void Var001()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, null, ServiceProgramCall.RETURN_INTEGER, paramList);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that NullPointerException will be thrown when the parameter parameterList is null.
     **/
    public void Var002()
    {
        try
        {
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that IllegalArgumentException will be thrown when the parameter returnValueFormat is less than 0.
     **/
    public void Var003()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", -1, paramList);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that IllegalArgumentException will be thrown when the parameter returnValueFormat is larger than 2.
     **/
    public void Var004()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", 3, paramList);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  ServiceProgramCall(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that this method runs well when the parameter is set properly.
     **/
    public void Var005()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  getProcedureName()
     - Ensure that this method runs well.
     **/
    public void Var006()
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
    public void Var007()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
            assertCondition(s.getReturnValueFormat()==ServiceProgramCall.RETURN_INTEGER);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested:  run()
     - Ensure that NullPointerException will be thrown when the as400 system is null.
     **/
    public void Var008()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(null);
            s.setProgram(serviceProgramName_);
            s.setProcedureName("i_i");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that NullPointerException will be thrown when service program is null.
     **/
    public void Var009()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(null);
            s.setProcedureName("i_i");
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that NullPointerException will be thrown when the procedure name is null.
     **/
    public void Var010()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(serviceProgramName_);
            s.setProcedureName(null);
            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that IllegalArgumentException will be thrown when return value format is less than 0.
     **/
    public void Var011()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(serviceProgramName_);
            s.setProcedureName("i_i");
            s.setReturnValueFormat(-1);
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  run()
     - Ensure that IllegalArgumentException will be thrown when return value format is larger than 2.
     **/
    public void Var012()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setSystem(systemObject_);
            s.setProgram(serviceProgramName_);
            s.setProcedureName("i_i");
            s.setReturnValueFormat(3);
            s.run();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Method tested:  getReturnValue() and run(AS400 system, String serviceProgram, String procedureName, int returnValueFormat, ProgramParameter[] parameterList)
     - Ensure that these methods run well.
     **/
    public void Var013()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            ProgramParameter p = new ProgramParameter(parm);
            p.setParameterType(ProgramParameter.PASS_BY_VALUE);
            paramList[0] = new ProgramParameter(parm);

            ServiceProgramCall s = new ServiceProgramCall();

            // System.out.println("about to call run, 5 parms");

            if (s.run(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer)bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();
                // System.out.println("Result is: " + i + " Expected 10");

                assertCondition(i == 10);
            }
            else
            {
                failed();
                System.out.println("Call to run() failed.");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    System.out.println(messageList[msg].toString());
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
     - Ensure that NullPointerException will be thrown when the procedure name is null.
     **/
    public void Var014()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
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
    public void Var015()
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
     - Ensure that IllegalArgumentException will be thrown when return value format is less than 0.
     **/
    public void Var016()
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
     - Ensure that IllegalArgumentException will be thrown when return value format is larger than 2.
     **/
    public void Var017()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setReturnValueFormat(3);
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
    public void Var018()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);
            ServiceProgramCall s = new ServiceProgramCall();
            s.setReturnValueFormat(0);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Supply a bad AS/400 name.
     **/
    public void Var019()
    {
        try
        {
            AS400 fred = new AS400("BADSYS", "HI", "MOM");
            fred.setGuiAvailable(false);

            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            ProgramParameter p = new ProgramParameter(parm);
            p.setParameterType(ProgramParameter.PASS_BY_VALUE);
            paramList[0] = new ProgramParameter(parm);

            ServiceProgramCall s = new ServiceProgramCall();

            // System.out.println("about to call run, 5 parms");

            s.run(fred, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);

            failed("no exception");
        }
        catch (Exception e)
        {
            if ((exceptionIs(e, "UnknownHostException")) || (exceptionIs(e, "ConnectException")) || (exceptionIs(e, "NoRouteToHostException")))
            {
                succeeded();
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Supply a bad procedure name.
     **/
    public void Var020()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            ProgramParameter p = new ProgramParameter(parm);
            p.setParameterType(ProgramParameter.PASS_BY_VALUE);
            paramList[0] = new ProgramParameter(parm);

            ServiceProgramCall s = new ServiceProgramCall();

            // System.out.println("about to call run, 5 parms");

            if (s.run(systemObject_, serviceProgramName_, "abcdefg", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                failed("calling entry point that does not exist but no error message");
            }
            else
            {
                AS400Message[] messageList = s.getMessageList();
                AS400Message m = messageList[0];
                if (m.getID().startsWith("CPF226E"))
                {
                    succeeded();
                }
                else
                {
                    failed("wrong error message:");
                    System.out.println(m.getID() + " " + m.getText());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Supply a bad procdure.
     **/
    public void Var021()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];

            AS400Bin4 bin4 = new AS400Bin4();
            byte[] parm = bin4.toBytes(9);
            ProgramParameter p = new ProgramParameter(parm);
            p.setParameterType(ProgramParameter.PASS_BY_VALUE);
            paramList[0] = new ProgramParameter(parm);

            ServiceProgramCall s = new ServiceProgramCall();

            // System.out.println("about to call run, 5 parms");

            if (s.run(systemObject_, "/QSYS.LIB/DAW.LIB/XYZ.SRVPGM", "aaa", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                failed("calling entry point that does not exist but no error message");
            }
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ObjectDoesNotExistException");
        }
    }
}
