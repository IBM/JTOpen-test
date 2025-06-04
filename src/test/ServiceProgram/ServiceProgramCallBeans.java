///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ServiceProgramCallBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.ServiceProgram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ActionCompletedEvent;
import com.ibm.as400.access.ActionCompletedListener;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.ServiceProgramCall;

import test.Testcase;

/**
 The ServiceProgramCallBean methods (listeners and serialization).
 **/
public class ServiceProgramCallBeans extends Testcase implements PropertyChangeListener, VetoableChangeListener, ActionCompletedListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ServiceProgramCallBeans";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ServiceProgramCallTest.main(newArgs); 
   }
    private String serviceProgramName_ = "/QSYS.LIB/JAVASP.LIB/ENTRYPTS.SRVPGM";

    String propertyName_;
    Object oldValue_;
    Object newValue_;
    Object source_;
    boolean veto_ = false;
    String propName_;
    Object oValue_;
    Object nValue_;
    Object src_;
    Object asource_;
    PropertyChangeEvent propChange_;
    PropertyChangeEvent vetoChange_;
    PropertyChangeEvent vetoRefire_;

    void resetValues()
    {
        veto_ = false;
        propChange_ = null;
        vetoChange_ = null;
        vetoRefire_ = null;

        propertyName_ = null;
        oldValue_ = null;
        newValue_ = null;
        source_ = null;
        propName_ = null;
        oValue_ = null;
        nValue_ = null;
        src_ = null;
        asource_ = null;
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        propChange_ = e;
        propertyName_ = e.getPropertyName();
        oldValue_ = e.getOldValue();
        newValue_ = e.getNewValue();
        source_ = e.getSource();
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
    {
        propName_ = e.getPropertyName();
        oValue_ = e.getOldValue();
        nValue_ = e.getNewValue();
        src_ = e.getSource();

        if (veto_)
        {
            throw new PropertyVetoException("Property vetoed", e);
        }
    }

    public void actionCompleted(ActionCompletedEvent e)      //$C0A
    {
        if (asource_ != null)
        {
            System.out.println("actionCompleted refired!");
        }
        asource_ = e.getSource();
    }

    /**
     Serialize and de-serialize ServiceProgramCall.
     **/
    public void Var001()
    {
        try
        {
            {
                ProgramParameter[] paramList = new ProgramParameter[1];
                paramList[0] = new ProgramParameter(4);
                ServiceProgramCall sOut = new ServiceProgramCall(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList);
                FileOutputStream fOut = new FileOutputStream("SrvPgmCall.ser");
                ObjectOutputStream output = new ObjectOutputStream(fOut);
                output.writeObject(sOut);
                output.flush();

                FileInputStream fIn = new FileInputStream("SrvPgmCall.ser");
                ObjectInputStream input = new ObjectInputStream(fIn);
                ServiceProgramCall sIn = (ServiceProgramCall)input.readObject();

                if (!sIn.getSystem().getSystemName().toString().equals(systemObject_.getSystemName().toString()))
                {
                    failed("System objects are not same. In:" + sIn.getSystem().getSystemName());
                }

                if (!sIn.getProgram().toString().equals(serviceProgramName_))
                {
                    failed("Program names are not same.");
                }

                if (!sIn.getProcedureName().toString().equals("i_i"))
                {
                    failed("Procedure names are not same.");
                }

                if (sIn.getReturnValueFormat() != ServiceProgramCall.RETURN_INTEGER)
                {
                    failed("Return value formats are not same.");
                }

                ProgramParameter[] para = sIn.getParameterList();
                if (para.length != 1) failed("Parameter number is not 1.");
                if (para[0].getOutputDataLength()!=4) failed("Parameter length is not 4.");

                fOut.close();
                fIn.close();
                File tempFile = new File("SrvPgmCall.ser");
                if (tempFile.exists())
                {
                    if (!tempFile.delete())
                    {
                        System.out.println("Could not delete serialization file SrvPgmCall.ser.");
                    }
                }
                else
                {
                    System.out.println("Could not find serialization file SrvPgmCall.ser.");
                }
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occured.");
        }
    }

    /**
     Test vetoable property support.  Set veto true, verify that the changes did not happen.
     **/
    public void Var002()
    {
        try
        {
            veto_ = true;

            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);

            ServiceProgramCall s = new ServiceProgramCall();
            s.addVetoableChangeListener(this);

            try
            {
                s.setSystem(systemObject_);
            }
            catch (PropertyVetoException e)
            {
            }

            if (s.getSystem() != null) failed("System object changed.");

            try
            {
                s.setProgram(serviceProgramName_);
            }
            catch (PropertyVetoException e)
            {
            }

            if (!s.getProgram().equals("")) failed("Program name changed. Program:" + s.getProgram());

            try
            {
                s.setProcedureName("i_i");
            }
            catch (PropertyVetoException e)
            {
            }

            if (!s.getProcedureName().equals("")) failed("Procedure name changed.");

            try
            {
                s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            }
            catch (PropertyVetoException e)
            {
            }
            if (s.getReturnValueFormat() != ServiceProgramCall.NO_RETURN_VALUE)
            {
                failed("Return value format changed.");
            }

            try
            {
                s.setParameterList(paramList);
            }
            catch (PropertyVetoException e)
            {
            }

            if (s.getParameterList().length != 0)
            {
                failed("Parameter list changed.");
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexcepted exception occured.");
        }
        finally
        {
            veto_ = false;
        }
    }

    /**
     Test vetoable property support.  Set veto false, verify that the changes happened.
     **/
    public void Var003()
    {
        try
        {
            veto_ = false;

            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);

            ServiceProgramCall s = new ServiceProgramCall();
            s.addVetoableChangeListener(this);

            s.setSystem(systemObject_);
            if (!s.getSystem().getSystemName().toString().equals(systemObject_.getSystemName().toString()))
            {
                failed("System object not changed.");
            }

            s.setProgram(serviceProgramName_);
            if (!s.getProgram().equals(serviceProgramName_))
            {
                failed("Program name not changed.");
            }

            s.setProcedureName("i_i");
            if (!s.getProcedureName().equals("i_i"))
            {
                failed("Procedure name not changed.");
            }

            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            if (s.getReturnValueFormat()!=ServiceProgramCall.RETURN_INTEGER)
            {
                failed("Return value format not changed.");
            }

            s.setParameterList(paramList);
            if (s.getParameterList().length != 1)
            {
                failed("Parameter list  not changed.");
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexcepted exception occured.");
        }
    }

    /**
     Test property change listener support.  Set the testcase up as a listener, change a property, make sure the testcase class is notified.
     **/
    public void Var004()
    {
        try
        {
            ProgramParameter[] paramList = new ProgramParameter[1];
            paramList[0] = new ProgramParameter(4);

            ServiceProgramCall s = new ServiceProgramCall();
            s.addPropertyChangeListener(this);

            s.setSystem(systemObject_);
            if (!propertyName_.equals("system"))
            {
                failed("Property change was not heard when set system.");
            }

            s.setProgram(serviceProgramName_);
            if (!propertyName_.equals("program"))
            {
                failed("Property change was not heard when set program.");
            }

            s.setProcedureName("i_i");
            if (!propertyName_.equals("procedureName"))
            {
                failed("Property change was not heard when set procedure.");
            }

            s.setReturnValueFormat(ServiceProgramCall.RETURN_INTEGER);
            if (!propertyName_.equals("returnValueFormat"))
            {
                failed("Property change was not heard when set return value format.");
            }

            s.setParameterList(paramList);
            if (!propertyName_.equals("parameterList"))
            {
                failed("Property change was not heard when set parameter list.");
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexcepted exception occured.");
        }
    }

    /**
     Run a valid service program and make sure no exceptions are returned and an event is fired.
     **/
    public void Var005()
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

            s.addActionCompletedListener(this);
            resetValues();

            // System.out.println("about to call run, 5 parms");
            if (s.run(systemObject_, serviceProgramName_, "i_i", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                Integer I = (Integer) bin4.toObject(s.getReturnValue(), 0);
                int i = I.intValue();
                // System.out.println("Result is: " + i + " Expected 10");

                // verify event fired
                if (asource_ != s)
                {
                    failed("actionCompleted not called with source " + asource_);
                    return;
                }

                assertCondition(i == 10);
            }
            else
            {
                failed();
                System.out.println("call to run() failed");
                AS400Message[] messageList = s.getMessageList();
                for (int msg = 0; msg < messageList.length; ++msg)
                {
                    System.out.println(messageList[msg].toString());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occured.");
        }
    }

    /**
     Run an invalid service program and make sure an exception is returned and event not fired.
     **/
    public void Var006()
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

            s.addActionCompletedListener(this);
            resetValues();

            // System.out.println("about to call run, 5 parms");
            if (s.run(systemObject_, "/QSYS.LIB/JAVASP.LIB/xxxxxxxx.SRVPGM", "i_i", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                failed("calling entry point that does not exist but no error message");
            }

            failed("No exception.");
        }
        catch (Exception e)
        {  
            if (asource_ != null)
            {
                failed("actionCompleted called with source: " + asource_);
                return;
            }
            succeeded();
        }
    }

    /**
     Run an invalid service program and make sure an exception is returned and event not fired.
     **/
    public void Var007()
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

            s.addActionCompletedListener(this);
            resetValues();

            // System.out.println("about to call run, 5 parms");
            if (s.run(systemObject_, "/QSYS.LIB/xxxxxx.LIB/ENTRYPTS.SRVPGM", "i_i", ServiceProgramCall.RETURN_INTEGER, paramList))
            {
                failed("calling entry point that does not exist but no error message");
            }

            failed("No exception.");
        }
        catch (Exception e)
        {
            if (asource_ != null)
            {
                failed("actionCompleted called with source: " + asource_);
                return;
            }
            succeeded();
        }
    }
}
