///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DASerializeTestcase.java
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DataAreaEvent;
import com.ibm.as400.access.DataAreaListener;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;

import test.Testcase;

/**
 Testcase DASerializeTestcase.
 **/
public class DASerializeTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener, DataAreaListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DASerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DATest.main(newArgs); 
   }
    String goodDAName = "/QSYS.LIB/DATEST.LIB/DASER.DTAARA";
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

    //private String datestUserID = " USER(DATEST) ";
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

        if (verifyPropChange(prop, (Object)old_V, (Object)new_V, sourceV, (Object)cur_V))
            return true;
        else
            return false;
    }

    // verify for boolean property change
    public boolean verifyPropChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
        if (verifyPropChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
            return true;
        else
            return false;
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
     Test serialization of a CharacterDataArea.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, goodDAName);
            da.addDataAreaListener(this);

            // Serialize da to a file.
            FileOutputStream f = new FileOutputStream("da.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(da);
            s.flush();
            s.close(); 
            try
            {
                // Deserialize da from a file.
                FileInputStream in = new FileInputStream("da.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                CharacterDataArea da2 = (CharacterDataArea)s2.readObject();
                s2.close(); 

                if (false == da2.getPath().equals(da.getPath()))
                {
                    failed("Path changed to " + da2.getPath());
                    return;
                }
                else if (false == da2.getSystem().getSystemName().equals(da.getSystem().getSystemName()))
                {
                    failed("System changed to " + da2.getSystem().getSystemName());
                    return;
                }
                else if (false == da2.getSystem().getUserId().equals( da.getSystem().getUserId()))
                {
                    failed("User changed to " + da2.getSystem().getUserId());
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test serialization of a DecimalDataArea.
     **/
    public void Var002()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, goodDAName);
            da.addDataAreaListener(this);

            // Serialize da to a file.
            FileOutputStream f = new FileOutputStream("da.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(da);
            s.flush();
            s.close(); 
            try
            {
                // Deserialize da from a file.
                FileInputStream in = new FileInputStream("da.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                DecimalDataArea da2 = (DecimalDataArea)s2.readObject();
                s2.close();

                if (false == da2.getPath().equals(da.getPath()))
                {
                    failed("Path changed to " + da2.getPath());
                    return;
                }
                else if (false == da2.getSystem().getSystemName().equals(da.getSystem().getSystemName()))
                {
                    failed("System changed to " + da2.getSystem().getSystemName());
                    return;
                }
                else if (false == da2.getSystem().getUserId().equals( da.getSystem().getUserId()))
                {
                    failed("User changed to " + da2.getSystem().getUserId());
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test serialization of a LocalDataArea.
     **/
    public void Var003()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            da.addDataAreaListener(this);

            // Serialize da to a file.
            FileOutputStream f = new FileOutputStream("da.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(da);
            s.flush();
            s.close();
            try
            {
                // Deserialize da from a file.
                FileInputStream in = new FileInputStream("da.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                LocalDataArea da2 = (LocalDataArea)s2.readObject();
                s2.close();
                if (false == da2.getSystem().getSystemName().equals(da.getSystem().getSystemName()))
                {
                    failed("System changed to " + da2.getSystem().getSystemName());
                    return;
                }
                else if (false == da2.getSystem().getUserId().equals( da.getSystem().getUserId()))
                {
                    failed("User changed to " + da2.getSystem().getUserId());
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test serialization of a LogicalDataArea.
     **/
    public void Var004()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, goodDAName);
            da.addDataAreaListener(this);

            // Serialize da to a file.
            FileOutputStream f = new FileOutputStream("da.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(da);
            s.flush();
            s.close(); 
            try
            {
                // Deserialize da from a file.
                FileInputStream in = new FileInputStream("da.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                LogicalDataArea da2 = (LogicalDataArea)s2.readObject();
                s2.close(); 
                if (false == da2.getPath().equals(da.getPath()))
                {
                    failed("Path changed to " + da2.getPath());
                    return;
                }
                else if (false == da2.getSystem().getSystemName().equals(da.getSystem().getSystemName()))
                {
                    failed("System changed to " + da2.getSystem().getSystemName());
                    return;
                }
                else if (false == da2.getSystem().getUserId().equals( da.getSystem().getUserId()))
                {
                    failed("User changed to " + da2.getSystem().getUserId());
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Ensure that a CharacterDataArea will correctly serialize and deserialize itself.
     Verify that system name and path name are preserved.  Verify that
     listeners aren't preserved.
     **/
    public void Var005()
    {
        CharacterDataArea da = null;
        try
        {
            da = new CharacterDataArea(systemObject_, goodDAName);
            da.create();

            da.addDataAreaListener(this);
            da.addVetoableChangeListener(this);
            da.addPropertyChangeListener(this);

            // serialize
            FileOutputStream fos = new FileOutputStream("da.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(da);
            fos.close();

            try
            {
                // deserialize
                FileInputStream fis = new FileInputStream("da.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                CharacterDataArea da2 = (CharacterDataArea) ois.readObject();
                fis.close();

                String systemName1 = da.getSystem().getSystemName();
                String systemName2 = da2.getSystem().getSystemName();
                String systemUserId1 = da.getSystem().getUserId();
                String systemUserId2 = da2.getSystem().getUserId();
                String pathName1 = da.getPath();
                String pathName2 = da2.getPath();

                if (false == systemName1.equals(systemName2))
                {
                    failed("Unexpected results occurred - systemName.");
                }
                else if (false == systemUserId1.equals(systemUserId2))
                {
                    failed("Unexpected results occurred - systemUserId.");
                }
                else if (false == pathName1.equals(pathName2))
                {
                    failed("Unexpected results occurred - pathName.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
                resetValues();
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Ensure that a DecimalDataArea will correctly serialize and deserialize itself.
     Verify that system name and path name are preserved.  Verify that
     listeners aren't preserved.
     **/
    public void Var006()
    {
        DecimalDataArea da = null;
        try
        {
            da = new DecimalDataArea(systemObject_, goodDAName);
            da.create();

            da.addDataAreaListener(this);
            da.addVetoableChangeListener(this);
            da.addPropertyChangeListener(this);

            // serialize
            FileOutputStream fos = new FileOutputStream("da.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(da);
            fos.close();

            try
            {
                // deserialize
                FileInputStream fis = new FileInputStream("da.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                DecimalDataArea da2 = (DecimalDataArea) ois.readObject();
                fis.close();

                String systemName1 = da.getSystem().getSystemName();
                String systemName2 = da2.getSystem().getSystemName();
                String systemUserId1 = da.getSystem().getUserId();
                String systemUserId2 = da2.getSystem().getUserId();
                String pathName1 = da.getPath();
                String pathName2 = da2.getPath();

                if (false == systemName1.equals(systemName2))
                {
                    failed("Unexpected results occurred - systemName.");
                }
                else if (false == systemUserId1.equals(systemUserId2))
                {
                    failed("Unexpected results occurred - systemUserId.");
                }
                else if (false == pathName1.equals(pathName2))
                {
                    failed("Unexpected results occurred - pathName.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
                resetValues();
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Ensure that a LocalDataArea will correctly serialize and deserialize itself.
     Verify that system name and path name are preserved.  Verify that
     listeners aren't preserved.
     **/
    public void Var007()
    {
        LocalDataArea da = null;
        try
        {
            da = new LocalDataArea(systemObject_);

            da.addDataAreaListener(this);
            da.addVetoableChangeListener(this);
            da.addPropertyChangeListener(this);

            // serialize
            FileOutputStream fos = new FileOutputStream("da.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(da);
            fos.close();

            try
            {
                // deserialize
                FileInputStream fis = new FileInputStream("da.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                LocalDataArea da2 = (LocalDataArea) ois.readObject();
                fis.close();

                String systemName1 = da.getSystem().getSystemName();
                String systemName2 = da2.getSystem().getSystemName();
                String systemUserId1 = da.getSystem().getUserId();
                String systemUserId2 = da2.getSystem().getUserId();

                if (false == systemName1.equals(systemName2))
                {
                    failed("Unexpected results occurred - systemName.");
                }
                else if (false == systemUserId1.equals(systemUserId2))
                {
                    failed("Unexpected results occurred - systemUserId.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Ensure that a LogicalDataArea will correctly serialize and deserialize itself.
     Verify that system name and path name are preserved.  Verify that
     listeners aren't preserved.
     **/
    public void Var008()
    {
        LogicalDataArea da = null;
        try
        {
            da = new LogicalDataArea(systemObject_, goodDAName);
            da.create();

            da.addDataAreaListener(this);
            da.addVetoableChangeListener(this);
            da.addPropertyChangeListener(this);

            // serialize
            FileOutputStream fos = new FileOutputStream("da.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(da);
            fos.close();

            try
            {
                // deserialize
                FileInputStream fis = new FileInputStream("da.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                LogicalDataArea da2 = (LogicalDataArea) ois.readObject();
                fis.close();

                String systemName1 = da.getSystem().getSystemName();
                String systemName2 = da2.getSystem().getSystemName();
                String systemUserId1 = da.getSystem().getUserId();
                String systemUserId2 = da2.getSystem().getUserId();
                String pathName1 = da.getPath();
                String pathName2 = da2.getPath();

                if (false == systemName1.equals(systemName2))
                {
                    failed("Unexpected results occurred - systemName.");
                }
                else if (false == systemUserId1.equals(systemUserId2))
                {
                    failed("Unexpected results occurred - systemUserId.");
                }
                else if (false == pathName1.equals(pathName2))
                {
                    failed("Unexpected results occurred - pathName.");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("da.ser");
                fd.delete();
                resetValues();
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

}
