///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserListBeanInfoTestcase.java
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

import com.ibm.as400.access.UserList;
import com.ibm.as400.access.UserListBeanInfo;
import com.ibm.as400.resource.ResourceListListener;

import test.Testcase;

import com.ibm.as400.resource.RUserList;
import com.ibm.as400.resource.RUserListBeanInfo;

/**
 Testcase UserListBeanInfoTestcase.  This tests the methods of the UserListBeanInfo and RUserListBeanInfo classes:
 **/
@SuppressWarnings("deprecation")
public class UserListBeanInfoTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserListBeanInfoTestcase";
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
            UserListBeanInfo ubi = new UserListBeanInfo();
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
            UserListBeanInfo ubi = new UserListBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition(bd.getBeanClass() == UserList.class && bd.getCustomizerClass() == null);
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
            UserListBeanInfo ubi = new UserListBeanInfo();
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
            UserListBeanInfo ubi = new UserListBeanInfo();
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
            UserListBeanInfo ubi = new UserListBeanInfo();
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
            UserListBeanInfo ubi = new UserListBeanInfo();
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
    public void Var007()
    {
        try
        {
            UserListBeanInfo ubi = new UserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
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
            UserListBeanInfo ubi = new UserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
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
            UserListBeanInfo ubi = new UserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
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
            UserListBeanInfo ubi = new UserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
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
            UserListBeanInfo ubi = new UserListBeanInfo();
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
            UserListBeanInfo ubi = new UserListBeanInfo();
            PropertyDescriptor[] pd = ubi.getPropertyDescriptors();
            assertCondition(pd.length == 4 && pd[0].getName().equals("system"));
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition(abi.length == 1);
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanDescriptor bd = ubi.getBeanDescriptor();
            assertCondition(bd.getBeanClass() == RUserList.class && bd.getCustomizerClass() == null);
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanInfo abi[] = ubi.getAdditionalBeanInfo();
            assertCondition(ubi.getDefaultEventIndex() == -1 && abi[0].getDefaultEventIndex() == 0);
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            assertCondition(ubi.getDefaultPropertyIndex() == -1 && abi[0].getDefaultPropertyIndex() == -1);
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            EventSetDescriptor[] esd = abi[0].getEventSetDescriptors();
            assertCondition(ubi.getEventSetDescriptors() == null && esd.length == 3 && esd[0].getListenerType() == PropertyChangeListener.class && esd[1].getListenerType() == ResourceListListener.class && esd[2].getListenerType() == VetoableChangeListener.class);
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            Image icon = ubi.getIcon(-546);
            failed("Didn't throw exception"+icon);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getIcon().  Pass ICON_COLOR_16x16.
     **/
    public void Var019()
    {
        try
        {
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
            }
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
            }
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
            }
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            if (checkGui()) {
                assertCondition(true,"got "+ubi);
            }
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            assertCondition(ubi.getMethodDescriptors() == null && abi[0].getMethodDescriptors() == null);
        }
        catch (Throwable e)
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
            RUserListBeanInfo ubi = new RUserListBeanInfo();
            BeanInfo[] abi = ubi.getAdditionalBeanInfo();
            PropertyDescriptor[] pd1 = ubi.getPropertyDescriptors();
            PropertyDescriptor[] pd2 = abi[0].getPropertyDescriptors();
            assertCondition(pd1 == null && pd2.length == 1 && pd2[0].getName().equals("system"));
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
