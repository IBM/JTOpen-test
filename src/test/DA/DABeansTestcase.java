///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DABeansTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DataAreaEvent;
import com.ibm.as400.access.DataAreaListener;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;

import test.Testcase;

/**
 Testcase DABeansTestcase.
 **/
public class DABeansTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener, DataAreaListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DABeansTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DATest.main(newArgs); 
   }
    String goodDAName = "/QSYS.LIB/DATEST.LIB/DABEAN.DTAARA";
    String propertyName;
    Object oldValue;
    Object newValue;
    Object source;
    boolean veto_ = false;
    String propName;
    Object oValue;
    Object nValue;
    Object src;
    Object asource;

    // private String datestUserID = " USER(DATEST) ";
    // private static String dirName_;

    PropertyChangeEvent propChange;
    PropertyChangeEvent vetoChange;
    PropertyChangeEvent vetoRefire;
    DataAreaEvent daEvent;
    int daN = -1;

    void resetValues()
    {
        veto_ = false;
        propChange = null;
        vetoChange = null;
        vetoRefire = null;

        propertyName = null;
        oldValue = null;
        newValue = null;
        source = null;
        propName = null;
        oValue = null;
        nValue = null;
        src = null;
        asource = null;

        daEvent = null;
        daN = -1;
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        if (propChange != null)
        {
            output_.println("propertyChange refired!");
        }
        propChange = e;
        propertyName = e.getPropertyName();
        oldValue = e.getOldValue();
        newValue = e.getNewValue();
        source = e.getSource();
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
    {
        if (vetoChange != null)
        {
            if (vetoRefire != null)
            {
                output_.println("vetoableChange refired!");
            }
            else
            {
                vetoRefire = e;
            }
        }
        else
        {
            vetoChange = e;
        }
        propName = e.getPropertyName();
        oValue = e.getOldValue();
        nValue = e.getNewValue();
        src = e.getSource();

        if (veto_)
        {
            throw new PropertyVetoException("Property vetoed", e);
        }
    }

    public void cleared(DataAreaEvent e)
    {
        if (daEvent != null)
        {
            output_.println("cleared refired!");
        }
        daEvent = e;
        daN = 0;
        asource = e.getSource();
    }

    public void created(DataAreaEvent e)
    {
        if (daEvent != null)
        {
            output_.println("created refired!");
        }
        daEvent = e;
        daN = 1;
        asource = e.getSource();
    }

    public void deleted(DataAreaEvent e)
    {
        if (daEvent != null)
        {
            output_.println("objectDeleted refired!");
        }
        daEvent = e;
        daN = 2;
        asource = e.getSource();
    }

    public void read(DataAreaEvent event)
    {
        if (daEvent != null)
        {
            output_.println("read refired!");
        }
        daEvent = event;
        daN = 3;
        asource = event.getSource();
    }

    public void written(DataAreaEvent event)
    {
        if (daEvent != null)
        {
            output_.println("written refired!");
        }
        daEvent = event;
        daN = 4;
        asource = event.getSource();
    }


    /**
     Verify CLEARED event is fired on CharacterDataArea::clear().
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.clear();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 0)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is fired on CharacterDataArea::create().
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.create();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 1)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is fired on CharacterDataArea::create(int,String,String,String).
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.create(100, "Initial", " ", "*USE");
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 1)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify DELETED event is fired on CharacterDataArea::delete().
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            try
            {
                resetValues();
                da.addDataAreaListener(this);
            }
            finally
            {
                da.delete();
            }
            // Verify event
            if (daEvent == null)
            {
                failed("Event not fired.");
            }
            else if (daN != 2)
            {
                failed("Incorrect event fired: " + daN);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is fired on CharacterDataArea::read().
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.read();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 3)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is fired on CharacterDataArea::read(int,int).
     **/
    public void Var006()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.read(2,2);
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 3)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is fired on CharacterDataArea::write(String).
     **/
    public void Var007()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.write("Test1");
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 4)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is fired on CharacterDataArea::write(String,int).
     **/
    public void Var008()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.write("Test2",1);
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 4)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is fired on DecimalDataArea::clear().
     **/
    public void Var009()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.clear();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 0)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is fired on DecimalDataArea::create().
     **/
    public void Var010()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.create();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 1)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is fired on DecimalDataArea::create(int,int,BigDecimal,String,String).
     **/
    public void Var011()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.create(10, 2, new BigDecimal("3.3"), " ", "*USE");
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 1)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify DELETED event is fired on DecimalDataArea::delete().
     **/
    public void Var012()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            da.create();
            try
            {
                resetValues();
                da.addDataAreaListener(this);
            }
            finally
            {
                da.delete();
            }
            // Verify event
            if (daEvent == null)
            {
                failed("Event not fired.");
            }
            else if (daN != 2)
            {
                failed("Incorrect event fired: " + daN);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is fired on DecimalDataArea::read().
     **/
    public void Var013()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.read();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 3)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is fired on DecimalDataArea::write(BigDecimal).
     **/
    public void Var014()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.write(new BigDecimal("4.5"));
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 4)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is fired on LocalDataArea::clear().
     **/
    public void Var015()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.clear();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 0)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is fired on LocalDataArea::read().
     **/
    public void Var016()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.read();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 3)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is fired on LocalDataArea::read(int,int).
     **/
    public void Var017()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.read(2,2);
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 3)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is fired on LocalDataArea::write(String).
     **/
    public void Var018()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.write("Test1");
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 4)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is fired on LocalDataArea::write(String,int).
     **/
    public void Var019()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.write("Test2",1);
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 4)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is fired on LogicalDataArea::clear().
     **/
    public void Var020()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.clear();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 0)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is fired on LogicalDataArea::create().
     **/
    public void Var021()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.create();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 1)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is fired on LogicalDataArea::create(boolean,String,String).
     **/
    public void Var022()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.create(true, " ", "*USE");
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 1)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify DELETED event is fired on LogicalDataArea::delete().
     **/
    public void Var023()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            da.create();
            try
            {
                resetValues();
                da.addDataAreaListener(this);
            }
            finally
            {
                da.delete();
            }
            // Verify event
            if (daEvent == null)
            {
                failed("Event not fired.");
            }
            else if (daN != 2)
            {
                failed("Incorrect event fired: " + daN);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is fired on LogicalDataArea::read().
     **/
    public void Var024()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.read();
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 3)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is fired on LogicalDataArea::write(boolean).
     **/
    public void Var025()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            da.create();
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.write(false);
                // Verify event
                if (daEvent == null)
                {
                    failed("Event not fired.");
                }
                else if (daN != 4)
                {
                    failed("Incorrect event fired: " + daN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is not fired on CharacterDataArea::clear().
     **/
    public void Var026()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.clear();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is not fired on CharacterDataArea::create().
     **/
    public void Var027()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.create();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is not fired on CharacterDataArea::create(int,String,String,String).
     **/
    public void Var028()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.create(100, "Initial", " ", "*USE");
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify DELETED event is not fired on CharacterDataArea::delete().
     **/
    public void Var029()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();
            try
            {
                resetValues();
                da.addDataAreaListener(this);
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
            // Verify event
            if (daEvent != null)
            {
                failed("Event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is not fired on CharacterDataArea::read().
     **/
    public void Var030()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.read();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is not fired on CharacterDataArea::read(int,int).
     **/
    public void Var031()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.read(2,2);
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is not fired on CharacterDataArea::write(String).
     **/
    public void Var032()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.write("Test1");
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is not fired on CharacterDataArea::write(String,int).
     **/
    public void Var033()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.write("Test2",1);
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is not fired on DecimalDataArea::clear().
     **/
    public void Var034()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.clear();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is not fired on DecimalDataArea::create().
     **/
    public void Var035()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.create();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event not fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is not fired on DecimalDataArea::create(int,int,BigDecimal,String,String).
     **/
    public void Var036()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.create(10, 2, new BigDecimal("3.3"), " ", "*USE");
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify DELETED event is not fired on DecimalDataArea::delete().
     **/
    public void Var037()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            da.create();
            try
            {
                resetValues();
                da.addDataAreaListener(this);
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
            // Verify event
            if (daEvent != null)
            {
                failed("Event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is not fired on DecimalDataArea::read().
     **/
    public void Var038()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.read();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is not fired on DecimalDataArea::write(BigDecimal).
     **/
    public void Var039()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.write(new BigDecimal("4.5"));
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is not fired on LocalDataArea::clear().
     **/
    public void Var040()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.clear();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is not fired on LocalDataArea::read().
     **/
    public void Var041()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.read();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is not fired on LocalDataArea::read(int,int).
     **/
    public void Var042()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.read(2,2);
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is not fired on LocalDataArea::write(String).
     **/
    public void Var043()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.write("Test1");
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is not fired on LocalDataArea::write(String,int).
     **/
    public void Var044()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.write("Test2",1);
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CLEARED event is not fired on LogicalDataArea::clear().
     **/
    public void Var045()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.clear();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is not fired on LogicalDataArea::create().
     **/
    public void Var046()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.create();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify CREATED event is not fired on LogicalDataArea::create(boolean,String,String).
     **/
    public void Var047()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            try
            {
                da.removeDataAreaListener(this);
                da.create(true, " ", "*USE");
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify DELETED event is not fired on LogicalDataArea::delete().
     **/
    public void Var048()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            da.create();
            try
            {
                resetValues();
                da.addDataAreaListener(this);
            }
            finally
            {
                da.removeDataAreaListener(this);
                da.delete();
            }
            // Verify event
            if (daEvent != null)
            {
                failed("Event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify READ event is not fired on LogicalDataArea::read().
     **/
    public void Var049()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.read();
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Verify WRITTEN event is not fired on LogicalDataArea::write(boolean).
     **/
    public void Var050()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addDataAreaListener(this);
            da.create();
            try
            {
                resetValues();
                da.removeDataAreaListener(this);
                da.write(false);
                // Verify event
                if (daEvent != null)
                {
                    failed("Event fired.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }
    /**
     PROPERTY CHANGE TESTING
     **/
    public boolean baseVerifyPropChange(String prop, Object oldV, Object newV, Object sourceV )
    {
        if (null == propChange)
        {
            failed("propertyChange not fired for " + prop);
        }
        else if (null == propChange.getPropertyName())
        {
            failed("propertyName is null");
        }
        else if (null == oldV)
        {
            failed("old Value is null for " + prop);
        }
        else if (null == newV)
        {
            failed("new Value is null for " + prop);
        }
        else if (null == sourceV)
        {
            failed("source Value is null for " + prop);
        }
        else if (!propChange.getPropertyName().equals(prop))
        {
            failed("propertyName " + prop + ", expected " + propChange.getPropertyName());
        }
        else if (! oldV.equals(propChange.getOldValue()))
        {
            failed("old value " + oldV + ", expected " + propChange.getOldValue());
        }
        else if (!newV.equals(propChange.getNewValue()))
        {
            failed("new value " + newV + ", expected " + propChange.getNewValue());
        }
        else if (!sourceV.equals(propChange.getSource()))
        {
            failed("source " + sourceV + ", expected " + propChange.getSource());
        }
        else
        {
            return true;
        }
        return false;
    }

    public boolean verifyPropChange(String prop, Object oldV, Object newV, Object sourceV, Object curV)
    {
        if (true == baseVerifyPropChange(prop, oldV, newV, sourceV))
        {
            if (!newV.equals(curV))
            {
                failed("changed value " + curV + ", expected " + newV);
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    // verify for byte property change
    public boolean verifyPropChange(String prop, byte oldV, byte newV, Object sourceV, byte curV)
    {
        byte[] old_V = { oldV };
        byte[] new_V = { newV };
        byte[] cur_V = { curV };

        if(verifyPropChange(prop, (Object)old_V, (Object)new_V, sourceV, (Object)cur_V))
            return true;
        else
            return false;
    }

    // verify for boolean property change
    public boolean verifyPropChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
        if(verifyPropChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
            return true;
        else
            return false;
    }


    /**
     Verify property change events for CharacterDataArea::setPath(String).
     **/
    public void Var051()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.setPath(newPath);
            // Verify event
            if (verifyPropChange("path", goodDAName, newPath, da, da.getPath()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events for CharacterDataArea::setSystem(AS400).
     **/
    public void Var052()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyPropChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events for DecimalDataArea::setPath(String).
     **/
    public void Var053()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.setPath(newPath);
            // Verify event
            if (verifyPropChange("path", goodDAName, newPath, da, da.getPath()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events for DecimalDataArea::setSystem(AS400).
     **/
    public void Var054()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyPropChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events for LocalDataArea::setSystem(AS400).
     **/
    public void Var055()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyPropChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events for LogicalDataArea::setPath(String).
     **/
    public void Var056()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.setPath(newPath);
            // Verify event
            if (verifyPropChange("path", goodDAName, newPath, da, da.getPath()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events for LogicalDataArea::setSystem(AS400).
     **/
    public void Var057()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyPropChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for CharacterDataArea::setPath(String).
     **/
    public void Var058()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.removePropertyChangeListener(this);
            da.setPath(newPath);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for CharacterDataArea::setSystem(AS400).
     **/
    public void Var059()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.removePropertyChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for DecimalDataArea::setPath(String).
     **/
    public void Var060()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.removePropertyChangeListener(this);
            da.setPath(newPath);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for DecimalDataArea::setSystem(AS400).
     **/
    public void Var061()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.removePropertyChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for LocalDataArea::setSystem(AS400).
     **/
    public void Var062()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.removePropertyChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for LogicalDataArea::setPath(String).
     **/
    public void Var063()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.removePropertyChangeListener(this);
            da.setPath(newPath);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify property change events are not fired for LogicalDataArea::setSystem(AS400).
     **/
    public void Var064()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addPropertyChangeListener(this);
            AS400 newSys = new AS400();
            da.removePropertyChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (propChange != null)
            {
                failed("Property change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     VETOABLE CHANGE TESTING
     **/
    public boolean verifyVetoChange(String prop, Object oldV, Object newV, Object sourceV, Object curV)
    {
        if (vetoChange == null)
        {
            failed("no veto change event");
            return false;
        }
        else if (propChange != null)
        {
            failed("property change fired as well as veto");
            return false;
        }
        propChange = vetoChange;
        if (true == baseVerifyPropChange(prop, oldV, newV, sourceV))
        {
            Object checkV = (veto_ ? oldV : newV);
            if (!checkV.equals(curV))
            {
                failed("changed value " + curV + ", expected " + checkV);
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    // verify veto changes for a byte
    public boolean verifyVetoChange(String prop, byte oldV, byte newV, Object sourceV, byte curV)
    {
        byte[] old_V = { oldV };
        byte[] new_V = { newV };
        byte[] cur_V = { curV };

        if (true == verifyVetoChange(prop, (Object)old_V, (Object)new_V, sourceV, (Object)cur_V))
            return true;
        else
            return false;
    }

    // verify veto changes for a boolean
    public boolean verifyVetoChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
        if (true == verifyVetoChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
            return true;
        else
            return false;
    }

    /**
     Verify vetoable change events for CharacterDataArea::setPath(String).
     Don't veto.
     **/
    public void Var065()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.setPath(newPath);
            // Verify event
            if (verifyVetoChange("path", goodDAName, newPath, da, da.getPath()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for CharacterDataArea::setSystem(AS400).
     Don't veto.
     **/
    public void Var066()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for DecimalDataArea::setPath(String).
     Don't veto.
     **/
    public void Var067()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.setPath(newPath);
            // Verify event
            if (verifyVetoChange("path", goodDAName, newPath, da, da.getPath()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for DecimalDataArea::setSystem(AS400).
     Don't veto.
     **/
    public void Var068()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for LocalDataArea::setSystem(AS400).
     Don't veto.
     **/
    public void Var069()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for LogicalDataArea::setPath(String).
     Don't veto.
     **/
    public void Var070()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.setPath(newPath);
            // Verify event
            if (verifyVetoChange("path", goodDAName, newPath, da, da.getPath()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for LogicalDataArea::setSystem(AS400).
     Don't veto.
     **/
    public void Var071()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.setSystem(newSys);
            // Verify event
            if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for CharacterDataArea::setPath(String).
     **/
    public void Var072()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.removeVetoableChangeListener(this);
            da.setPath(newPath);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for CharacterDataArea::setSystem(AS400).
     **/
    public void Var073()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.removeVetoableChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for DecimalDataArea::setPath(String).
     **/
    public void Var074()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.removeVetoableChangeListener(this);
            da.setPath(newPath);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for DecimalDataArea::setSystem(AS400).
     **/
    public void Var075()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.removeVetoableChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for LocalDataArea::setSystem(AS400).
     **/
    public void Var076()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.removeVetoableChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for LogicalDataArea::setPath(String).
     **/
    public void Var077()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            da.removeVetoableChangeListener(this);
            da.setPath(newPath);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events are not fired for LogicalDataArea::setSystem(AS400).
     **/
    public void Var078()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            AS400 newSys = new AS400();
            da.removeVetoableChangeListener(this);
            da.setSystem(newSys);
            // Verify event
            if (vetoChange != null)
            {
                failed("Vetoable change event fired.");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for CharacterDataArea::setPath(String).
     Veto.
     **/
    public void Var079()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            try
            {
                da.setPath(newPath);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("path", goodDAName, newPath, da, da.getPath()))
                {
                    succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for CharacterDataArea::setSystem(AS400).
     Veto.
     **/
    public void Var080()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            AS400 newSys = new AS400();
            try
            {
                da.setSystem(newSys);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
                {
                    succeeded();
                }
            }
            // Verify event
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for DecimalDataArea::setPath(String).
     Veto.
     **/
    public void Var081()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            try
            {
                da.setPath(newPath);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("path", goodDAName, newPath, da, da.getPath()))
                {
                    succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for DecimalDataArea::setSystem(AS400).
     Veto.
     **/
    public void Var082()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            AS400 newSys = new AS400();
            try
            {
                da.setSystem(newSys);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
                {
                    succeeded();
                }
            }
            // Verify event
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for LocalDataArea::setSystem(AS400).
     Veto.
     **/
    public void Var083()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            AS400 newSys = new AS400();
            try
            {
                da.setSystem(newSys);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
                {
                    succeeded();
                }
            }
            // Verify event
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for LogicalDataArea::setPath(String).
     Veto.
     **/
    public void Var084()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.DTAARA";
            try
            {
                da.setPath(newPath);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("path", goodDAName, newPath, da, da.getPath()))
                {
                    succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify vetoable change events for LogicalDataArea::setSystem(AS400).
     Veto.
     **/
    public void Var085()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            resetValues();
            da.addVetoableChangeListener(this);
            veto_ = true;
            AS400 newSys = new AS400();
            try
            {
                da.setSystem(newSys);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", systemObject_, newSys, da, da.getSystem()))
                {
                    succeeded();
                }
            }
            // Verify event
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify that an event is not fired on a failed
     CharacterDataArea::create().
     **/
    public void Var086()
    {
        CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.create(-5, "", " ", "*USE");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     DecimalDataArea::create().
     **/
    public void Var087()
    {
        DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.create(-5, 3, new BigDecimal("0.0"), " ", "*USE");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LogicalDataArea::create().
     **/
    public void Var088()
    {
        LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.create(true, null, null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     CharacterDataArea::delete().
     **/
    public void Var089()
    {
        CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.delete();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     DecimalDataArea::delete().
     **/
    public void Var090()
    {
        DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.delete();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LogicalDataArea::delete().
     **/
    public void Var091()
    {
        LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.delete();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     CharacterDataArea::read().
     **/
    public void Var092()
    {
        CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.read();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     DecimalDataArea::read().
     **/
    public void Var093()
    {
        DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.read();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LocalDataArea::read().
     **/
    public void Var094()
    {
        LocalDataArea da = new LocalDataArea(systemObject_);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.read(-1, 3);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LogicalDataArea::read().
     **/
    public void Var095()
    {
        LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.read();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     CharacterDataArea::write().
     **/
    public void Var096()
    {
        CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.write("Test");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     DecimalDataArea::write().
     **/
    public void Var097()
    {
        DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.write(new BigDecimal("1.2"));
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LocalDataArea::write().
     **/
    public void Var098()
    {
        LocalDataArea da = new LocalDataArea(systemObject_);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.write(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LogicalDataArea::write().
     **/
    public void Var099()
    {
        LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.write(true);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     CharacterDataArea::clear().
     **/
    public void Var100()
    {
        CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.clear();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     DecimalDataArea::clear().
     **/
    public void Var101()
    {
        DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.clear();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LocalDataArea::clear().
     **/
    public void Var102()
    {
        LocalDataArea da = new LocalDataArea();
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.clear();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }

    /**
     Verify that an event is not fired on a failed
     LogicalDataArea::clear().
     **/
    public void Var103()
    {
        LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
        da.addDataAreaListener(this);
        resetValues();

        try
        {
            da.clear();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (daEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
    }
}
