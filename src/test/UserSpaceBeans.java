///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

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

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.UserSpace;
import com.ibm.as400.access.UserSpaceEvent;
import com.ibm.as400.access.UserSpaceListener;

/**
 Testcase UserSpaceBeans.
 **/
public class UserSpaceBeans extends Testcase implements PropertyChangeListener, VetoableChangeListener, UserSpaceListener
{
    String goodUSName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USBEAN.USRSPC";
    String abcPath = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/ABC.USRSPC";
    String newusPath = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC"; 
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

    private String ustestUserID = " USER(USTEST) ";
    private boolean usingNativeImpl = false;

    PropertyChangeEvent propChange;
    PropertyChangeEvent vetoChange;
    PropertyChangeEvent vetoRefire;
    UserSpaceEvent usEvent;
    int usN = -1;

    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        if (usingNativeImpl) ustestUserID = " USER(" + systemObject_.getUserId() + ") ";
        goodUSName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USBEAN.USRSPC";
        abcPath = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/ABC.USRSPC";
        newusPath = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC"; 

    }

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

        usEvent = null;
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

    /**
     @exception  PropertyVetoException  If an exception occurs.
     **/
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

    public void created(UserSpaceEvent e)
    {
        if (usEvent != null)
        {
            output_.println("created refired!");
        }
        usEvent = e;
        usN = 0;
        asource = e.getSource();
    }

    public void deleted(UserSpaceEvent e)
    {
        if (usEvent != null)
        {
            output_.println("objectDeleted refired!");
        }
        usEvent = e;
        usN = 1;
        asource = e.getSource();
    }

    public void read(UserSpaceEvent event)
    {
        if (usEvent != null)
        {
            output_.println("read refired!");
        }
        usEvent = event;
        usN = 2;
        asource = event.getSource();
    }

    public void written(UserSpaceEvent event)
    {
        if (usEvent != null)
        {
            output_.println("written refired!");
        }
        usEvent = event;
        usN = 3;
        asource = event.getSource();
    }

    /**
     Do successful UserSpace::create calls
     Verify the user space is created correctly.
     **/
    public void Var001()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            // Create with length 3485.
            us.addUserSpaceListener(this);
            us.create(3485, true, " ", (byte)0x08, "text user space", "*ALL");
            try
            {
                // Verify event
                if (usEvent == null)
                {
                    failed("event not fired on create");
                }
                else if (usN != 0)
                {
                    failed("create not fired, instead " + usN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                resetValues();
                us.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Do successful UserSpace::delete calls
     Verify the user space is deleted correctly.
     **/
    public void Var002()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            us.create(10, true, " ", (byte)0x01, "text user space", "*ALL");
            try
            {
                resetValues();
                // delete.
                us.addUserSpaceListener(this);
            }
            finally
            {
                us.delete();
            }
            // Verify event
            if (usEvent == null)
            {
                failed("event not fired on delete");
            }
            else if (usN != 1)
            {
                failed("delete not fired, instead " + usN);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Delete failed.");
        }
    }

    /**
     Do successful UserSpace::read calls
     Verify the user space is read from correctly.
     **/
    public void Var003()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            us.create(10, true, " ", (byte)0x01, "text user space", "*ALL");
            try
            {
                resetValues();
                // read no wait.
                byte[] bytes = {1,2,3};
                us.write(bytes, 0);
                us.addUserSpaceListener(this);
                byte[] bytesIn = new byte[3];
                int bytesRead = us.read(bytesIn, 0);
                // Verify event
                if (usEvent == null)
                {
                    failed("event not fired on read");
                }
                else if (usN != 2)
                {
                    failed("read not fired, instead " + usN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                resetValues();
                us.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Clear failed.");
        }
    }

    /**
     Do successful UserSpace::write calls
     Verify the user space is written from correctly.
     **/
    public void Var004()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            us.create(10, true, " ", (byte)0x01, "text user space", "*ALL");
            try
            {
                resetValues();
                // write 3 bytes
                us.addUserSpaceListener(this);
                byte[] bytes = {1,2,3};
                us.write(bytes, 0);
                // Verify event
                if (usEvent == null)
                {
                    failed("event not fired on write");
                }
                else if (usN != 3)
                {
                    failed("write not fired, instead " + usN);
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                resetValues();
                us.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Clear failed.");
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
     setPath
     **/
    public void Var005()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            us.addPropertyChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.USRSPC";
            us.setPath(newPath);
            // Verify event
            if (verifyPropChange("path", goodUSName, newPath, us, us.getPath()))
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
     setSystem
     **/
    public void Var006()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            us.addPropertyChangeListener(this);
            AS400 newsys = new AS400();
            us.setSystem(newsys);
            // Verify event
            if (verifyPropChange("system", systemObject_, newsys, us, us.getSystem()) == true)
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

    // don't veto

    /**
     setPath
     **/
    public void Var007()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            us.addVetoableChangeListener(this);
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.USRSPC";
            us.setPath(newPath);
            // Verify event
            if (verifyVetoChange("path", goodUSName, newPath, us, us.getPath()))
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
     setSystem
     **/
    public void Var008()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            us.addVetoableChangeListener(this);
            AS400 newsys = new AS400();
            us.setSystem(newsys);
            // Verify event
            if (verifyVetoChange("system", systemObject_, newsys, us, us.getSystem()) == true)
            {
                succeeded();
            }
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
    public void Var009()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            String newPath = "/QSYS.LIB/QTEMP.LIB/CHANGE.USRSPC";
            us.addVetoableChangeListener(this);
            veto_ = true;
            try
            {
                us.setPath(newPath);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("path", goodUSName, newPath, us, us.getPath()))
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
     setSystem
     **/
    public void Var010()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            AS400 newsys = new AS400();
            us.addVetoableChangeListener(this);
            veto_ = true;
            try
            {
                us.setSystem(newsys);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", systemObject_, newsys, us, us.getSystem()) == true)
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
    public void Var011()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            us.setMustUseProgramCall(true);
            us.addUserSpaceListener(this);

            // Serialize us to a file.
            FileOutputStream f = new FileOutputStream("us.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(us);
            s.flush();
            try
            {
                // Deserialize us from a file.
                FileInputStream in = new FileInputStream("us.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                UserSpace us2 = (UserSpace)s2.readObject();

                if (false == us2.getPath().equals(us.getPath()))
                {
                    failed("Path changed to " + us2.getPath());
                    return;
                }
                else if (false == us2.getSystem().getSystemName().equals(us.getSystem().getSystemName()))
                {
                    failed("system changed to " + us2.getSystem().getSystemName());
                    return;
                }
                else if (false == us2.getSystem().getUserId().equals( us.getSystem().getUserId()))
                {
                    failed("user changed to " + us2.getSystem().getUserId());
                    return;
                }
                else if (!us2.isMustUseProgramCall())
                {
                    failed("must use program call not true");
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("us.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     setPath
     **/
    public void Var012()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, abcPath);
            resetValues();
            aUserSpace.setPath(goodUSName);
            aUserSpace.addPropertyChangeListener(this);
            String newUSName = newusPath;
            aUserSpace.setPath(newUSName);
            // Verify event
            if (verifyPropChange("path", goodUSName, newUSName, aUserSpace, aUserSpace.getPath()))
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
     setSystem
     **/
    public void Var013()
    {
        try
        {
            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(as400, goodUSName);
            resetValues();
            AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1");
            aUserSpace.setSystem(oldAS400);
            aUserSpace.addPropertyChangeListener(this);
            AS400 newAS400 = systemObject_;
            aUserSpace.setSystem(newAS400);
            // Verify event
            if (verifyPropChange("system", oldAS400, newAS400, aUserSpace, aUserSpace.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    // User Space Attributes don't veto

    /**
     setPath
     **/
    public void Var014()
    {
        try
        {
            String path = abcPath;
            UserSpace aUserSpace = new UserSpace(systemObject_, path);
            resetValues();
            aUserSpace.setPath(goodUSName);
            aUserSpace.addVetoableChangeListener(this);
            String newUSName = newusPath;
            aUserSpace.setPath(newUSName);
            // Verify event
            if (verifyVetoChange("path", goodUSName, newUSName, aUserSpace, aUserSpace.getPath()))
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
     setSystem
     **/
    public void Var015()
    {
        try
        {
            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(as400, goodUSName);
            resetValues();
            AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1");
            aUserSpace.setSystem(oldAS400);
            aUserSpace.addVetoableChangeListener(this);
            AS400 newAS400 = systemObject_;
            aUserSpace.setSystem(newAS400);
            // Verify event
            if (verifyVetoChange("system", oldAS400, newAS400, aUserSpace, aUserSpace.getSystem()))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    // User Spaces Attributes do veto

    /**
     setPath
     **/
    public void Var016()
    {
        try
        {
            String path = abcPath;
            UserSpace aUserSpace = new UserSpace(systemObject_, path);
            resetValues();
            aUserSpace.setPath(goodUSName);
            veto_ = true;
            aUserSpace.addVetoableChangeListener(this);
            String newUSName = newusPath;

            try
            {
                aUserSpace.setPath(newUSName);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("path", goodUSName, newUSName, aUserSpace, aUserSpace.getPath()))
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
     setSystem
     **/
    public void Var017()
    {
        try
        {
            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(as400, goodUSName);
            resetValues();
            AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1");
            aUserSpace.setSystem(oldAS400);
            veto_ = true;
            aUserSpace.addVetoableChangeListener(this);
            AS400 newAS400 = systemObject_;

            try
            {
                aUserSpace.setSystem(newAS400);
            }
            catch (Exception e)
            {
                // Verify event
                if (verifyVetoChange("system", oldAS400, newAS400, aUserSpace, aUserSpace.getSystem()))
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
    public void Var018()
    {
        try
        {
            AS400 badSystem = new AS400("rchasxxx", "JAVA", "JTEAM1");
            UserSpace aUserSpace = new UserSpace(badSystem, goodUSName);
            String newUSName = newusPath;
            aUserSpace.setPath(newUSName);
            aUserSpace.setSystem(systemObject_);
            aUserSpace.addPropertyChangeListener(this);

            // Serialize a UserSpace to a file.
            FileOutputStream f = new FileOutputStream("uspace.ser");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(aUserSpace);
            s.flush();
            try
            {
                // Deserialize a UserSpace from a file.
                FileInputStream in = new FileInputStream("uspace.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                UserSpace aUserSpace2 = (UserSpace)s2.readObject();

                if (false == aUserSpace2.getPath().equals(aUserSpace.getPath()))
                {
                    failed("Path changed to " + aUserSpace2.getPath());
                    return;
                }
                else if (false == aUserSpace2.getSystem().getSystemName().equals(aUserSpace.getSystem().getSystemName()))
                {
                    failed("System changed to " + aUserSpace2.getSystem());
                    return;
                }
                else if (false == aUserSpace2.getSystem().getUserId().equals(aUserSpace.getSystem().getUserId()))
                {
                    failed("System changed to " + aUserSpace2.getSystem());
                    return;
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("uspace.ser");
                fd.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Attempt to create a User Space and make sure an exception is returned
     and event not fired.
     **/
    public void Var019()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, goodUSName);
        aUserSpace.addUserSpaceListener(this);

        resetValues();
        try
        {
            aUserSpace.create("bad__domain", 4000, true, " ", (byte)0x09, "test", "*ALL");
            failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            {
                if (usEvent == null)
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
     Attempt to delete a User Space and make sure an exception is returned
     and event not fired.
     **/
    public void Var020()
    {
        UserSpace aUserSpace = new UserSpace();
        aUserSpace.addUserSpaceListener(this);

        resetValues();
        try
        {
            aUserSpace.delete();
            failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                if (usEvent == null)
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
     Attempt to read from a User Space and make sure an exception
     is returned and event not fired.
     **/
    public void Var021()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, goodUSName);
        aUserSpace.addUserSpaceListener(this);

        try
        {
            resetValues();
            aUserSpace.create(4000, true, " ", (byte)0x09, "test", "*ALL");
            resetValues();

            byte[] bytes = null;
            aUserSpace.read(bytes, 0);
            failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                if (usEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
        try
        {
            aUserSpace.delete();
        }
        catch (Exception e)
        {
            System.out.println("Cleanup failed - User Space NOT DELETED.");
        }
    }
    /**
     Attempt to write to a User Space and make sure an exception
     is returned and event not fired.
     **/
    public void Var022()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, goodUSName);
        aUserSpace.addUserSpaceListener(this);

        try
        {
            resetValues();
            aUserSpace.create(4000, true, " ", (byte)0x09, "test", "*ALL");

            resetValues();

            byte[] bytes = null;
            aUserSpace.write(bytes, 0);
            failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                if (usEvent == null)
                    succeeded();
                else
                    failed("Unexpected event occurred.");
            }
            else
            {
                failed(e, "Unexpected exception occurred.");
            }
        }
        try
        {
            aUserSpace.delete();
        }
        catch (Exception e)
        {
            System.out.println("Cleanup failed - User Space NOT DELETED.");
        }
    }

    /**
     Ensure that UserSpace will correctly serialize and deserialize itself.
     Verify that system name and path name are preserved.  Verify that
     listeners aren't preserved.
     **/
    public void Var023()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, goodUSName);
            aUserSpace.create(3500, true, " ", (byte)0x05, "test us", "*ALL");

            aUserSpace.addUserSpaceListener(this);
            aUserSpace.addVetoableChangeListener(this);
            aUserSpace.addPropertyChangeListener(this);

            // serialize
            FileOutputStream fos = new FileOutputStream("us.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(aUserSpace);
            fos.close();

            try
            {
                // deserialize
                FileInputStream fis = new FileInputStream("us.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                UserSpace aUS = (UserSpace) ois.readObject();
                fis.close();

                String systemName1 = aUserSpace.getSystem().getSystemName();
                String systemName2 = aUS.getSystem().getSystemName();
                String systemUserId1 = aUserSpace.getSystem().getUserId();
                String systemUserId2 = aUS.getSystem().getUserId();
                String pathName1 = aUserSpace.getPath();
                String pathName2 = aUS.getPath();

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
                else if (aUS.isMustUseProgramCall())
                {
                    failed("must use program call wrong");
                }
                else
                {
                    succeeded();
                }
            }
            finally
            {
                File fd = new File("us.ser");
                fd.delete();
                resetValues();
                aUserSpace.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }


    public void Var024()
    {
        try
        {
            UserSpace us = new UserSpace(systemObject_, goodUSName);
            resetValues();
            us.addPropertyChangeListener(this);
            us.setMustUseProgramCall(true);
            // Verify event
            if (verifyPropChange("mustUseProgramCall", new Boolean(false), new Boolean(true), us, new Boolean(true)))
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
