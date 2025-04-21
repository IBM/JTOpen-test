///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  MessageFileBeansTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Message;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.MessageFile;

import test.Testcase;

/**
 Testcase MessageFileBeansTestcase.
 **/
public class MessageFileBeansTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "MessageFileBeansTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.MessageTest.main(newArgs); 
   }
    String goodMFName  = "/QSYS.LIB/QCPFMSG.MSGF";
    String goodMFName2 = "/QSYS.LIB/QJVAMSG.MSGF";
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

    PropertyChangeEvent propChange;
    PropertyChangeEvent vetoChange;
    PropertyChangeEvent vetoRefire;
    int usN = -1;

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

        usN = -1;
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

    /**
     PROPERTY CHANGE TESTING
     **/
    public boolean baseVerifyPropChange(String prop, Object oldV, Object newV, Object sourceV)
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

    // Verify for byte property change.
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

    // Verify for boolean property change.
    public boolean verifyPropChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
        if (verifyPropChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
            return true;
        else
            return false;
    }

    /**
     setPath
     **/
    public void Var001()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            resetValues();
            mf.addPropertyChangeListener(this);
            mf.setPath(goodMFName2);

            assertCondition(verifyPropChange("path", goodMFName, goodMFName2, mf, mf.getPath()));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setSystem
     **/
    public void Var002()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            resetValues();
            mf.addPropertyChangeListener(this);
            AS400 newsys = new AS400();
            mf.setSystem(newsys);
            assertCondition(verifyPropChange("system", systemObject_, newsys, mf, mf.getSystem()) == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Vetoable change testing.
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

    // Verify veto changes for a byte.
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

    // Verify veto changes for a boolean.
    public boolean verifyVetoChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
        if (true == verifyVetoChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
            return true;
        else
            return false;
    }

    // don't veto

    /**
     setPath
     **/
    public void Var003()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            resetValues();
            mf.addVetoableChangeListener(this);
            mf.setPath(goodMFName2);
            assertCondition(verifyVetoChange("path", goodMFName, goodMFName2, mf, mf.getPath()));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setSystem
     **/
    public void Var004()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            resetValues();
            mf.addVetoableChangeListener(this);
            AS400 newsys = new AS400();
            mf.setSystem(newsys);
            assertCondition(verifyVetoChange("system", systemObject_, newsys, mf, mf.getSystem()) == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    // veto

    /**
     setPath
     **/
    public void Var005()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            resetValues();
            mf.addVetoableChangeListener(this);
            veto_ = true;
            try
            {
                mf.setPath(goodMFName2);
            }
            catch (Exception e)
            {
                // Verify event
                assertCondition(verifyVetoChange("path", goodMFName, goodMFName2, mf, mf.getPath()));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setSystem
     **/
    public void Var006()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            resetValues();
            AS400 newsys = new AS400();
            mf.addVetoableChangeListener(this);
            veto_ = true;
            try
            {
                mf.setSystem(newsys);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", systemObject_, newsys, mf, mf.getSystem()) == true)
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
     Test serialization
     **/
    public void Var007()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            mf.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);
            resetValues();

            // Serialize mf to a file.
            FileOutputStream f = new FileOutputStream("mf.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(mf);
            s.flush();
            s.close(); 
            ObjectInputStream s2 = null; 
            try
            {
                // Deserialize mf from a file.
                FileInputStream in = new FileInputStream("mf.ser");
                s2 = new ObjectInputStream(in);
                MessageFile mf2 = (MessageFile)s2.readObject();

                if (false == mf2.getPath().equals(mf.getPath()))
                {
                    failed("Path changed to " + mf2.getPath());
                    return;
                }
                else if (false == mf2.getSystem().getSystemName().equals(mf.getSystem().getSystemName()))
                {
                    failed("system changed to " + mf2.getSystem().getSystemName());
                    return;
                }
                else if (false == mf2.getSystem().getUserId().equals( mf.getSystem().getUserId()))
                {
                    failed("mfer changed to " + mf2.getSystem().getUserId());
                    return;
                }
                else if (mf2.getHelpTextFormatting() != MessageFile.RETURN_FORMATTING_CHARACTERS)
                {
                    failed("format value is now " + mf2.getHelpTextFormatting());
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
              if (s2 != null) s2.close(); 
                File fd = new File("mf.ser");
                fd.delete();
                
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test RemovePropertyChangeListener:
     1) Remove when list is empty,
     2) Remove when listener is on list
     **/
    public void Var008()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            AS400 newsys = new AS400();
            resetValues();

            try
            {
                mf.removePropertyChangeListener(this);
            }
            catch (Exception e2)
            {
                failed("remove when nothing on list");
                newsys.close(); 
                return;
            } 

            mf.addPropertyChangeListener(this);
            mf.removePropertyChangeListener(this);

            mf.setSystem(newsys);

            if (propChange != null)
                failed("received property change after the remove");
            else
            {
                succeeded();
                return;
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     test RemoveVetoableChangeListener:
     1) Remove when list is empty,
     2) Remove when listener is on list
     **/
    public void Var009()
    {
        try
        {
            MessageFile mf = new MessageFile(systemObject_, goodMFName);
            AS400 newsys = new AS400();
            resetValues();

            try
            {
                mf.removeVetoableChangeListener(this);
            }
            catch (Exception e2)
            {
                failed("remove when nothing on list");
                newsys.close(); 
                return;
            }

            mf.addVetoableChangeListener(this);
            mf.removeVetoableChangeListener(this);

            mf.setSystem(newsys);

            if (propChange != null)
                failed("received veto change after the remove");
            else
            {
                succeeded();
                return;
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     BeanInfo
     **/
    public void Var010()
    {
        try
        {
            Class<?> beanclass = Class.forName("com.ibm.as400.access.MessageFile");
            BeanInfo beaninfo = Introspector.getBeanInfo(beanclass);
            //System.out.println("Class of beaninfo is: " + beaninfo.getClass().getName());
            // Note: Experimentation reveals that the returned BeanInfo object
            // is of type java.beans.GenericBeanInfo.
            // Why isn't it of type com.ibm.as400.access.MessageFileBeanInfo ?

            // Note: If you create the BeanInfo object the following way, getEventSetDescriptors() returns an array of length 2.
            //beaninfo = new com.ibm.as400.access.MessageFileBeanInfo();

            // Icons / GUI components no longer available in JTOpen 20.0.X

            EventSetDescriptor[] event = beaninfo.getEventSetDescriptors();
            //System.out.println("eventSetDescriptors.length == " + event.length);
            //for (int i=0; i<event.length; i++)
            //{
            //  EventSetDescriptor esd = event[i];
            //  System.out.println("esd["+i+"]: " + esd.getDisplayName() + " (" + esd.getName() + ")");
            //}
            PropertyDescriptor[] props = beaninfo.getPropertyDescriptors();

            Vector<String> v = new Vector<String>();
            v.addElement("path");
            v.addElement("system");
            v.addElement("helpTextFormatting");
            //v.addElement("propertyChange");
            v.addElement("vetoableChange");

            for (int i = 0; i < props.length; i++)
            {
                PropertyDescriptor p = props[i];

                if (! p.isHidden())
                {
                    v.removeElement(p.getDisplayName());
                }
            }

            for (int i = 0; i < event.length; i++)
            {
                EventSetDescriptor e = event[i];

                v.removeElement(e.getDisplayName());
            }

            if (v.isEmpty())
                succeeded();
            else
            {
                failed("Vector is not empty.");
                while (!v.isEmpty())
                {
                    System.out.println("   " + v.elementAt(0));
                    v.removeElementAt(0);
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
