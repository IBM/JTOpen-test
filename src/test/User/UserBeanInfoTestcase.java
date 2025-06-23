///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.VetoableChangeListener;

import com.ibm.as400.access.User;
import com.ibm.as400.access.UserBeanInfo;
import com.ibm.as400.resource.ResourceListener;

import test.Testcase;

import com.ibm.as400.resource.RUser;
import com.ibm.as400.resource.RUserBeanInfo;

/**
 Testcase UserBeanInfoTestcase.  This tests the methods of the UserBeanInfo and RUserBeanInfo class:
 **/
@SuppressWarnings("deprecation")
public class UserBeanInfoTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserBeanInfoTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    /**
     getAdditionalBeanInfo().
     **/
    public void Var001()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition(abi == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getBeanDescriptor().
     **/
    public void Var002()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition(bd.getBeanClass() == User.class && bd.getCustomizerClass() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getDefaultEventIndex().
     **/
    public void Var003()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            assertCondition(ubi.getDefaultEventIndex() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getDefaultPropertyIndex().
     **/
    public void Var004()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            assertCondition(ubi.getDefaultPropertyIndex() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getEventSetDescriptors().
     **/
    public void Var005()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            EventSetDescriptor[] esd = ubi.getEventSetDescriptors();
            assertCondition(esd.length == 2 && esd[0].getListenerType() == PropertyChangeListener.class && esd[1].getListenerType() == VetoableChangeListener.class);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass an invalid value.
     **/
    public void Var006()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            Image icon = ubi.getIcon(-546);
            failed("Didn't throw exception"+ubi+ " " +icon);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getIcon().  Pass ICON_COLOR_16x16.
     **/
    public void Var007()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            if (checkGui()) { 
                assertCondition(true, "got "+ubi);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_COLOR_32x32.
     **/
    public void Var008()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            if (checkGui()) { 
                assertCondition(true, "got "+ubi);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_MONO_16x16.
     **/
    public void Var009()
    {
        try
        {
          if (checkGui()) { 
                assertCondition(true);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_MONO_32x32.
     **/
    public void Var010()
    {
        try
        {
          if (checkGui()) { 
                assertCondition(true);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getMethodDescriptors().
     **/
    public void Var011()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            assertCondition(ubi.getMethodDescriptors() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPropertyDescriptors().
     **/
    public void Var012()
    {
        try
        {
            UserBeanInfo ubi = new UserBeanInfo();
            PropertyDescriptor[] pd = ubi.getPropertyDescriptors();
            assertCondition(pd.length == 2 && pd[0].getName().equals("system") && pd[1].getName().equals("name"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getAdditionalBeanInfo().
     **/
    public void Var013()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition(abi.length == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getBeanDescriptor().
     **/
    public void Var014()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition(bd.getBeanClass() == RUser.class && bd.getCustomizerClass() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getDefaultEventIndex().
     **/
    public void Var015()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition(ubi.getDefaultEventIndex() == -1 && abi[0].getDefaultEventIndex() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getDefaultPropertyIndex().
     **/
    public void Var016()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            assertCondition(ubi.getDefaultPropertyIndex() == -1 && abi[0].getDefaultPropertyIndex() == -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getEventSetDescriptors().
     **/
    public void Var017()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            EventSetDescriptor[] esd = abi[0].getEventSetDescriptors();
            assertCondition(ubi.getEventSetDescriptors() == null && esd.length == 3 && esd[0].getListenerType() == PropertyChangeListener.class && esd[1].getListenerType() == ResourceListener.class && esd[2].getListenerType() == VetoableChangeListener.class);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass an invalid value.
     **/
    public void Var018()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            Image icon = ubi.getIcon(-546);
            failed("Didn't throw exception"+icon);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getIcon().  Pass ICON_COLOR_16x16.
     **/
    public void Var019()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            if (checkGui()) { 
                assertCondition(true, "got "+ubi);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_COLOR_32x32.
     **/
    public void Var020()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            if (checkGui()) { 
                assertCondition(true, "got "+ubi);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_MONO_16x16.
     **/
    public void Var021()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            if (checkGui()) { 
                assertCondition(true, "got "+ubi);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_MONO_32x32.
     **/
    public void Var022()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            if (checkGui()) { 
                assertCondition(true, "got "+ubi);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getMethodDescriptors().
     **/
    public void Var023()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            assertCondition(ubi.getMethodDescriptors() == null && abi[0].getMethodDescriptors() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getPropertyDescriptors().
     **/
    public void Var024()
    {
        try
        {
            RUserBeanInfo ubi = new RUserBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            PropertyDescriptor[] pd1 = ubi.getPropertyDescriptors();
            PropertyDescriptor[] pd2 = abi[0].getPropertyDescriptors();
            assertCondition(pd1.length == 1 && pd2.length == 1 && pd2[0].getName().equals("system") && pd1[0].getName().equals("name"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
