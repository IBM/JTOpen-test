///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmParmTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Pgm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import com.ibm.as400.access.ActionCompletedEvent;
import com.ibm.as400.access.ActionCompletedListener;
import com.ibm.as400.access.ProgramParameter;

import test.Testcase;

/**
 Testcase PgmParmTestcase.
 <ul>
 <li>1-7 test default, input, inout, and output parameters for default values (from ctor), and verifies setting of inputData and OutputDataLength
 <li>8-11 test for negative number for output data length on ctor and set.
 </ul>
 **/
public class PgmParmTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener, ActionCompletedListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PgmParmTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PgmTest.main(newArgs); 
   }
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

    public void propertyChange(PropertyChangeEvent e)
    {
        if (propChange_ != null)
        {
            output_.println("propertyChange refired!");
        }
        propChange_ = e;
        propertyName_ = e.getPropertyName();
        oldValue_ = e.getOldValue();
        newValue_ = e.getNewValue();
        source_ = e.getSource();
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
    {
        if (vetoChange_ != null)
        {
            if (vetoRefire_ != null)
            {
                output_.println("vetoableChange refired!");
            }
            else
            {
                vetoRefire_ = e;
            }
        }
        else
        {
            vetoChange_ = e;
        }
        propName_ = e.getPropertyName();
        oValue_ = e.getOldValue();
        nValue_ = e.getNewValue();
        src_ = e.getSource();

        if (veto_)
        {
            throw new PropertyVetoException("Property vetoed", e);
        }
    }

    public void actionCompleted(ActionCompletedEvent e)
    {
        if (asource_ != null)
        {
            output_.println("actionCompleted refired!");
        }
        asource_ = e.getSource();
    }

    /**
     Create a default parameter object, verify that the data empty, set the data, and verify data was set correctly.
     **/
    public void Var001()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter();
            // *** Default values
            // input is empty
            byte[] data = parm.getInputData();
            if (data != null && data.length != 0)
            {
                failed("input data is not null"); return;
            }
            // output is empty
            data = parm.getOutputData();
            if (data != null && data.length != 0)
            {
                failed("output data is not null"); return;
            }
            // output len 0
            if (parm.getOutputDataLength() != 0)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }

            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 0)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a parameter object with input data, verify initial data, set the data, and verify data was set correctly.
     **/
    public void Var002()
    {
        try
        {
            byte[] data0 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
            ProgramParameter parm = new ProgramParameter(data0);
            // *** Default values
            // input is set
            byte[] data = parm.getInputData();
            if (data != data0)
            {
                failed("input data not set"); return;
            }
            // output is empty
            data = parm.getOutputData();
            if (data != null && data.length != 0)
            {
                failed("output data is not null"); return;
            }
            // output len 0
            if (parm.getOutputDataLength() != 0)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }

            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 0)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a parameter object with input data and output size, verify initial data, set the data, and verify data was set correctly.
     **/
    public void Var003()
    {
        try
        {
            byte[] data0 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
            ProgramParameter parm = new ProgramParameter(data0, 50);
            // *** Default values
            // input is set
            byte[] data = parm.getInputData();
            if (data != data0)
            {
                failed("input data not set"); return;
            }
            // output is empty
            data = parm.getOutputData();
            if (data != null && data.length != 0)
            {
                failed("output data is not null"); return;
            }
            // output len set
            if (parm.getOutputDataLength() != 50)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }

            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 50)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a parameter object with output size, verify initial data, set the data, and verify data was set correctly.
     **/
    public void Var004()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter(50);
            // *** Default values
            // input is empty
            byte[] data = parm.getInputData();
            if (data != null && data.length != 0)
            {
                failed("input data is not null"); return;
            }
            // output len set
            if (parm.getOutputDataLength() != 50)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }

            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 50)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a parameter object with parameter type and input data, verify initial data, set the data, and verify data was set correctly.
     **/
    public void Var005()
    {
        try
        {
            byte[] data0 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
            ProgramParameter parm = new ProgramParameter(ProgramParameter.PASS_BY_VALUE, data0);

            // *** Default values
            // input is set
            byte[] data = parm.getInputData();
            if (data != data0)
            {
                failed("input data not set"); return;
            }
            // output is empty
            data = parm.getOutputData();
            if (data != null && data.length != 0)
            {
                failed("output data is not null"); return;
            }
            // output len set
            if (parm.getOutputDataLength() != 0)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }
            // parameter type set
            if (parm.getParameterType() != ProgramParameter.PASS_BY_VALUE)
            {
                failed("parameter type " + parm.getParameterType()); return;
            }
            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 0)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }
            // set parameter type
            parm.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            if (parm.getParameterType() != ProgramParameter.PASS_BY_REFERENCE)
            {
                failed("setParameter failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Create a parameter object with parameter type and output size, verify initial data, set the data, and verify data was set correctly.
     **/
    public void Var006()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter(ProgramParameter.PASS_BY_VALUE, 50);

            // *** Default values
            // input is set
            byte[] data = parm.getInputData();
            if (data != null && data.length != 0)
            {
                failed("input data is not null"); return;
            }
            // output is empty
            data = parm.getOutputData();
            if (data != null && data.length != 0)
            {
                failed("output data is not null"); return;
            }
            // output len set
            if (parm.getOutputDataLength() != 50)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }
            // parameter type set
            if (parm.getParameterType() != ProgramParameter.PASS_BY_VALUE)
            {
                failed("parameter type " + parm.getParameterType()); return;
            }
            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 50)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }
            // set parameter type
            parm.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            if (parm.getParameterType() != ProgramParameter.PASS_BY_REFERENCE)
            {
                failed("setParameter failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Create a parameter object with parameter type ,input data, and output size, verify initial data, set the data, and verify data was set correctly.
     **/
    public void Var007()
    {
        try
        {
            byte[] data0 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
            ProgramParameter parm = new ProgramParameter(ProgramParameter.PASS_BY_VALUE, data0, 50);

            // *** Default values
            // input is set
            byte[] data = parm.getInputData();
            if (data != data0)
            {
                failed("input data not set"); return;
            }
            // output is empty
            data = parm.getOutputData();
            if (data != null && data.length != 0)
            {
                failed("output data is not null"); return;
            }
            // output len set
            if (parm.getOutputDataLength() != 50)
            {
                failed("output data len " + parm.getOutputDataLength()); return;
            }
            // parameter type set
            if (parm.getParameterType() != ProgramParameter.PASS_BY_VALUE)
            {
                failed("parameter type " + parm.getParameterType()); return;
            }
            // *** Setting values;
            // set input
            byte[] data1 = {0, 1, 2, 3};
            parm.setInputData(data1);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 50)
            {
                failed("setInputData failed"); return;
            }
            // set output len
            parm.setOutputDataLength(10);
            if (parm.getInputData() != data1 || parm.getOutputData() != null || parm.getOutputDataLength() != 10)
            {
                failed("setOutputData failed"); return;
            }
            // set parameter type
            parm.setParameterType(ProgramParameter.PASS_BY_REFERENCE);
            if (parm.getParameterType() != ProgramParameter.PASS_BY_REFERENCE)
            {
                failed("setParameter failed"); return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Test ExtendedIllegalArgumentException on for negative number in OutputParameter constructor.  output len -1
     **/
    public void Var008()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter(-1);
            failed("ExtendedIllegalArgumentException not thrown"+parm);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Test for ExtendedIllegalArgumentException on negative number in OutputParameter on setData.
     **/
    public void Var009()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter(9);
            parm.setOutputDataLength(-1);
            failed("ExtendedIllegalArgumentException not thrown");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Test for ExtendedIllegalArgumentException on invalid parameter type on setParameterType.
     **/
    public void Var010()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter(9);
            parm.setParameterType(-1);
            failed("ExtendedIllegalArgumentException not thrown");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Test for ExtendedIllegalArgumentException on invalid parameter type on setParameterType.
     **/
    public void Var011()
    {
        try
        {
            ProgramParameter parm = new ProgramParameter(9);
            parm.setParameterType(3);
            failed("ExtendedIllegalArgumentException not thrown");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Adds and Removes a Property Change Listener from the Jelly Beans list.
     **/
    public void Var012()
    {
        try
        {
            ProgramParameter pgm = new ProgramParameter();
            pgm.addPropertyChangeListener(this);
            pgm.removePropertyChangeListener(this);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Adds and Removes a Vetoable Change Listener from the Jelly Beans list.
     **/
    public void Var013()
    {
        try
        {
            ProgramParameter pgm = new ProgramParameter();
            pgm.addVetoableChangeListener(this);
            pgm.removeVetoableChangeListener(this);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
