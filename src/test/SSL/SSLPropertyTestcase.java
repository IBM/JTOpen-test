///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLPropertyTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.SSL;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SecureAS400;

import test.Testcase;

/**
 Testcase SSLPropertyTestcase.
 **/
public class SSLPropertyTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
    String propertyName;
    Object oldPropValue;
    Object newPropValue;
    Object propSource;
    int propCount;

    boolean veto = false;
    String vetoPropName;
    Object oldVetoValue;
    Object newVetoValue;
    Object vetoSource;
    int vetoCount;

    public void propertyChange(PropertyChangeEvent e)
    {
        propertyName = e.getPropertyName();
        oldPropValue = e.getOldValue();
        newPropValue = e.getNewValue();
        propSource = e.getSource();
        ++propCount;
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
    {
        vetoPropName = e.getPropertyName();
        oldVetoValue = e.getOldValue();
        newVetoValue = e.getNewValue();
        vetoSource = e.getSource();
        ++vetoCount;

        if (veto)
        {
            throw new PropertyVetoException("Property vetoed", e);
        }
    }

    void resetState()
    {
        propertyName = null;
        oldPropValue = null;
        newPropValue = null;
        propSource = null;
        propCount = 0;
        veto = false;
        vetoPropName = null;
        oldVetoValue = null;
        newVetoValue = null;
        vetoSource = null;
        vetoCount = 0;
    }

    String verifyPropertyChange(SecureAS400 sys, String name)
    {
        String failMsg = "";
        if (propertyName == null)
        {
            failMsg += "property name not set\n";
        }
        else if (propertyName.equals(name) == false)
        {
            failMsg += "property name incorrect\n";
        }
        if (oldPropValue == null)
        {
            failMsg += "old value not set\n";
        }
        if (newPropValue == null)
        {
            failMsg += "new value not set\n";
        }
        if (propSource == null)
        {
            failMsg += "property change event was not received\n";
        }
        if (((SecureAS400)propSource) != sys)
        {
            failMsg += "event source is not correct\n";
        }
        if (propCount != 1)
        {
            failMsg += "property count incorrect\n";
        }
        if (vetoPropName != null)
        {
            failMsg += "veto change received in property change\n";
        }
        if (oldVetoValue != null)
        {
            failMsg += "old veto value changed in property change\n";
        }
        if (newVetoValue != null)
        {
            failMsg += "new veto value changed in property change\n";
        }
        if (vetoSource != null)
        {
            failMsg += "veto source value changed in property change\n";
        }
        if (vetoCount != 0)
        {
            failMsg += "veto count changed in property change\n";
        }
        return failMsg;
    }

    String verifyVetoableChange(SecureAS400 sys, String name)
    {
        String failMsg = "";
        if (vetoPropName == null)
        {
            failMsg += "veto property name not set\n";
        }
        else if (vetoPropName.equals(name) == false)
        {
            failMsg += "veto property name incorrect\n";
        }
        if (oldVetoValue == null)
        {
            failMsg += "old veto value not set\n";
        }
        if (newVetoValue == null)
        {
            failMsg += "new veto value not set\n";
        }
        if (vetoSource == null)
        {
            failMsg += "veto change event was not received\n";
        }
        if (((SecureAS400)vetoSource) != sys)
        {
            failMsg += "veto event source is not correct\n";
        }
        if (vetoCount != 1)
        {
            failMsg += "veto count incorrect\n";
        }

        if (propertyName != null)
        {
            failMsg += "property change received in veto change\n";
        }
        if (oldPropValue != null)
        {
            failMsg += "old property value changed in veto change\n";
        }
        if (newPropValue != null)
        {
            failMsg += "new property value changed in veto change\n";
        }
        if (propSource != null)
        {
            failMsg += "property source value changed in veto change\n";
        }
        if (propCount != 0)
        {
            failMsg += "property count changed in veto change\n";
        }
        return failMsg;
    }

    String verifyVetoedChange(SecureAS400 sys, String name)
    {
        String failMsg = "";
        if (vetoPropName == null)
        {
            failMsg += "veto property name not set\n";
        }
        else if (vetoPropName.equals(name) == false)
        {
            failMsg += "veto property name incorrect\n";
        }
        if (oldVetoValue == null)
        {
            failMsg += "old veto value not set\n";
        }
        if (newVetoValue == null)
        {
            failMsg += "new veto value not set\n";
        }
        if (vetoSource == null)
        {
            failMsg += "veto change event was not received\n";
        }
        if (((SecureAS400)vetoSource) != sys)
        {
            failMsg += "veto event source is not correct\n";
        }
        if (vetoCount != 2)
        {
            failMsg += "veto count incorrect\n";
        }

        if (propertyName != null)
        {
            failMsg += "property change received in veto change\n";
        }
        if (oldPropValue != null)
        {
            failMsg += "old property value changed in veto change\n";
        }
        if (newPropValue != null)
        {
            failMsg += "new property value changed in veto change\n";
        }
        if (propSource != null)
        {
            failMsg += "property source value changed in veto change\n";
        }
        if (propCount != 0)
        {
            failMsg += "property count changed in veto change\n";
        }
        return failMsg;
    }

    String verifyNoEvent()
    {
        String failMsg = "";
        if (propertyName != null)
        {
            failMsg += "property change received in no event\n";
        }
        if (oldPropValue != null)
        {
            failMsg += "old property value changed in no event\n";
        }
        if (newPropValue != null)
        {
            failMsg += "new property value changed in no event\n";
        }
        if (propSource != null)
        {
            failMsg += "property source value changed in no event\n";
        }
        if (propCount != 0)
        {
            failMsg += "property count changed in no event\n";
        }
        if (vetoPropName != null)
        {
            failMsg += "veto change received in no event\n";
        }
        if (oldVetoValue != null)
        {
            failMsg += "old veto value changed in no event\n";
        }
        if (newVetoValue != null)
        {
            failMsg += "new veto value changed in no event\n";
        }
        if (vetoSource != null)
        {
            failMsg += "veto source value changed in no event\n";
        }
        if (vetoCount != 0)
        {
            failMsg += "veto count changed in no event\n";
        }
        return failMsg;
    }

    /**
     Set ccsid for the system object, and make sure the property change event is fired.
     Verify that the property changed.
     **/
    public void Var001()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setCcsid(500);
            failMsg += verifyNoEvent();

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setCcsid(437);
                failMsg += verifyPropertyChange(sys, "ccsid");

                if (((Integer)oldPropValue).intValue() != 500)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((Integer)newPropValue).intValue() != 437)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.getCcsid() != 437)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set guiAvailable for the system object, and make sure the property change event is fired.
     Verify that the property changed.
     **/
    public void Var002()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setGuiAvailable(true);
            failMsg += verifyNoEvent();

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setGuiAvailable(false);
                failMsg += verifyPropertyChange(sys, "guiAvailable");

                if (((Boolean)oldPropValue).booleanValue() != true)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((Boolean)newPropValue).booleanValue() != false)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.isGuiAvailable() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the system name for the system object, and make sure the property change event is fired.
     Verify that the property changed.
     **/
    public void Var003()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("system1");
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setSystemName("system2");
                failMsg += verifyPropertyChange(sys, "systemName");

                if (((String)oldPropValue).equals("system1") == false)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((String)newPropValue).equals("system2") == false)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.getSystemName().equals("system2") == false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the user ID for the system object, and make sure the property change event is fired.
     Verify that the property changed.
     **/
    public void Var004()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(systemName_, "user1");
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setUserId("newuser");
                failMsg += verifyPropertyChange(sys, "userId");

                if (((String)oldPropValue).equals("USER1") == false)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((String)newPropValue).equals("NEWUSER") == false)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.getUserId().equals("NEWUSER") == false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the useDefaultUser for the system object, and make sure the property change event is fired.
     Verify that the property changed.
     **/
    public void Var005()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUseDefaultUser(true);
            failMsg += verifyNoEvent();

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setUseDefaultUser(false);
                failMsg += verifyPropertyChange(sys, "useDefaultUser");

                if (((Boolean)oldPropValue).booleanValue() != true)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((Boolean)newPropValue).booleanValue() != false)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.isUseDefaultUser() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the usePasswordCache for the system object, and make sure the property change event is fired.
     Verify that the property changed.
     **/
    public void Var006()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUsePasswordCache(true);
            failMsg += verifyNoEvent();

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setUsePasswordCache(false);
                failMsg += verifyPropertyChange(sys, "usePasswordCache");

                if (((Boolean)oldPropValue).booleanValue() != true)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((Boolean)newPropValue).booleanValue() != false)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.isUsePasswordCache() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the ccsid for the system object, and make sure the property veto event is fired.
     Veto the change and verify the property did not change.
     **/
    public void Var007()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setCcsid(500);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = true;
                sys.setCcsid(437);
                failed("PropertyVetoException was not thrown");
            }
            catch (PropertyVetoException e)
            {
                failMsg += verifyVetoedChange(sys, "ccsid");

                if (((Integer)oldVetoValue).intValue() != 437)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Integer)newVetoValue).intValue() != 500)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.getCcsid() != 500)
                {
                    failMsg += "property changed\n";
                }
                if (e.getMessage().equals("Property vetoed") == false)
                {
                    failMsg += "incorrect message in exception\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the ccsid for the system object, and make sure the property veto event is fired.
     Do not veto the change and verify the property did change.
     **/
    public void Var008()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setCcsid(500);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setCcsid(437);

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "ccsid");

                if (((Integer)oldVetoValue).intValue() != 500)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Integer)newVetoValue).intValue() != 437)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.getCcsid() != 437)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set guiAvailable for the system object, and make sure the property veto event is fired.
     Veto the change and verify the property did not change.
     **/
    public void Var009()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setGuiAvailable(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = true;
                sys.setGuiAvailable(false);
                failed("property veto exception not thrown");
            }
            catch (PropertyVetoException e)
            {
                failMsg += verifyVetoedChange(sys, "guiAvailable");

                if (((Boolean)oldVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isGuiAvailable() != true)
                {
                    failMsg += "property changed\n";
                }
                if (e.getMessage().equals("Property vetoed") == false)
                {
                    failMsg += "incorrect message in exception\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the guiAvailable for the system object, and make sure the property veto event is fired.
     Do not veto the change and verify the property did change.
     **/
    public void Var010()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setGuiAvailable(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setGuiAvailable(false);

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "guiAvailable");

                if (((Boolean)oldVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isGuiAvailable() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the system name for the system object, and make sure the property veto event is fired.
     Veto the change and verify the property did not change.
     **/
    public void Var011()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("system1");
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = true;
                sys.setSystemName("system2");
                failed("property veto exception not thrown");
            }
            catch (PropertyVetoException e)
            {
                failMsg += verifyVetoedChange(sys, "systemName");

                if (((String)oldVetoValue).equals("system2") == false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((String)newVetoValue).equals("system1") == false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.getSystemName().equals("system1") == false)
                {
                    failMsg += "property changed\n";
                }
                if (e.getMessage().equals("Property vetoed") == false)
                {
                    failMsg += "incorrect message in exception\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the system name for the system object, and make sure the property veto event is fired.
     Do not veto the change and verify the property did change.
     **/
    public void Var012()
    {
        try
        {
            SecureAS400 sys = new SecureAS400("system1");
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setSystemName("system2");

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "systemName");

                if (((String)oldVetoValue).equals("system1") == false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((String)newVetoValue).equals("system2") == false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.getSystemName().equals("system2") == false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the user ID for the system object, and make sure the property veto event is fired.
     Veto the change and verify the property did not change.
     **/
    public void Var013()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(systemName_, "user1");
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = true;
                sys.setUserId("newuser");
                failed("property veto exception not thrown");
            }
            catch (PropertyVetoException e)
            {
                failMsg += verifyVetoedChange(sys, "userId");

                if (((String)oldVetoValue).equals("NEWUSER") == false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((String)newVetoValue).equals("USER1") == false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.getUserId().equals("USER1") == false)
                {
                    failMsg += "property changed\n";
                }
                if (e.getMessage().equals("Property vetoed") == false)
                {
                    failMsg += "incorrect message in exception\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the user ID for the system object, and make sure the property veto event is fired.
     Do not veto the change and verify the property did change.
     **/
    public void Var014()
    {
        try
        {
            SecureAS400 sys = new SecureAS400(systemName_, "user1");
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setUserId("newuser");

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "userId");

                if (((String)oldVetoValue).equals("USER1") == false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((String)newVetoValue).equals("NEWUSER") == false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.getUserId().equals("NEWUSER") == false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the useDefaultUser for the system object, and make sure the property veto event is fired.
     Veto the change and verify the property did not change.
     **/
    public void Var015()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUseDefaultUser(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = true;
                sys.setUseDefaultUser(false);
                failed("property veto exception not thrown");
            }
            catch (PropertyVetoException e)
            {
                failMsg += verifyVetoedChange(sys, "useDefaultUser");

                if (((Boolean)oldVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isUseDefaultUser() != true)
                {
                    failMsg += "property changed\n";
                }
                if (e.getMessage().equals("Property vetoed") == false)
                {
                    failMsg += "incorrect message in exception\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the useDefaultUser for the system object, and make sure the property veto event is fired.
     Do not veto the change and verify the property did change.
     **/
    public void Var016()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUseDefaultUser(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setUseDefaultUser(false);

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "useDefaultUser");

                if (((Boolean)oldVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isUseDefaultUser() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the usePasswordCache for the system object, and make sure the property veto event is fired.
     Veto the change and verify the property did not change.
     **/
    public void Var017()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUsePasswordCache(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = true;
                sys.setUsePasswordCache(false);
                failed("property veto exception not thrown");
            }
            catch (PropertyVetoException e)
            {
                failMsg += verifyVetoedChange(sys, "usePasswordCache");

                if (((Boolean)oldVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isUsePasswordCache() != true)
                {
                    failMsg += "property changed\n";
                }
                if (e.getMessage().equals("Property vetoed") == false)
                {
                    failMsg += "incorrect message in exception\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Set the usePasswordCache for the system object, and make sure the property veto event is fired.
     Do not veto the change and verify the property did change.
     **/
    public void Var018()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUsePasswordCache(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setUsePasswordCache(false);

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "usePasswordCache");

                if (((Boolean)oldVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isUsePasswordCache() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Add a property listener and verify that we receive a property change event.
     Remove the property change listener and verify that we no longer receive the event.
     **/
    public void Var019()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUseDefaultUser(true);
            failMsg += verifyNoEvent();

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setUseDefaultUser(false);
                // first make sure we receive the event
                failMsg += verifyPropertyChange(sys, "useDefaultUser");

                if (((Boolean)oldPropValue).booleanValue() != true)
                {
                    failMsg += "old value incorrect\n";
                }
                if (((Boolean)newPropValue).booleanValue() != false)
                {
                    failMsg += "new value incorrect\n";
                }
                if (sys.isUseDefaultUser() != false)
                {
                    failMsg += "property not changed\n";
                }
            }
            finally
            {
                sys.removePropertyChangeListener(this);
                resetState();
                sys.setUseDefaultUser(true);
                failMsg += verifyNoEvent();

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Add a property veto listener and verify that we receive a property veto event.
     Remove the property veto listener and verify that we no longer receive the event.
     **/
    public void Var020()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setUseDefaultUser(true);
            failMsg += verifyNoEvent();

            sys.addVetoableChangeListener(this);
            try
            {
                resetState();
                veto = false;
                sys.setUseDefaultUser(false);

                // check the event info to make sure the right values are being passed
                failMsg += verifyVetoableChange(sys, "useDefaultUser");

                if (((Boolean)oldVetoValue).booleanValue() != true)
                {
                    failMsg += "incorrect old value\n";
                }
                if (((Boolean)newVetoValue).booleanValue() != false)
                {
                    failMsg += "incorrect new value\n";
                }
                if (sys.isUseDefaultUser() != false)
                {
                    failMsg += "property not changed\n";
                }
            }
            finally
            {
                sys.removeVetoableChangeListener(this);
                resetState();
                sys.setUseDefaultUser(true);
                failMsg += verifyNoEvent();

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Start from a disconnected state, connect to a service, and verify that a property change event is fired for connect.
     **/
    public void Var021()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.connectService(AS400.FILE);
         failMsg += verifyPropertyChange(sys, "connected");

         if (((Boolean)oldPropValue).booleanValue() != false)
         {
         failMsg += "old value incorrect\n";
         }
         if (((Boolean)newPropValue).booleanValue() != true)
         {
         failMsg += "new value incorrect\n";
         }
         if (sys.isConnected() == false)
         {
         failMsg += "property not changed\n";
         }

         assertCondition(failMsg.equals(""), "\n" + failMsg);
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         sys.disconnectService(AS400.FILE);
         }
         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Connect to a service, then disconnect.
     Verify that a property change event is fired for connect.
     **/
    public void Var022()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         resetState();
         sys.connectService(AS400.COMMAND);
         failMsg += verifyNoEvent();

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.disconnectService(AS400.COMMAND);
         failMsg += verifyPropertyChange(sys, "connected");

         if (((Boolean)oldPropValue).booleanValue() != true)
         {
         failMsg += "old value incorrect\n";
         }
         if (((Boolean)newPropValue).booleanValue() != false)
         {
         failMsg += "new value incorrect\n";
         }
         if (sys.isConnected() != false)
         {
         failMsg += "property not changed\n";
         }

         assertCondition(failMsg.equals(""), "\n" + failMsg);
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         }
         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Connect multiple services, and disconnect one.
     Verify that a property change event is not fired.
     **/
    public void Var023()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         resetState();
         sys.connectService(AS400.COMMAND);
         failMsg += verifyNoEvent();

         resetState();
         sys.connectService(AS400.FILE);
         failMsg += verifyNoEvent();

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.disconnectService(AS400.COMMAND);
         failMsg += verifyNoEvent();
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         sys.disconnectAllServices();
         }
         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Verify that disconnectAllServices result in a property change event.
     **/
    public void Var024()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         resetState();
         sys.connectService(AS400.FILE);
         failMsg += verifyNoEvent();

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.disconnectAllServices();
         failMsg += verifyPropertyChange(sys, "connected");

         if (((Boolean)oldPropValue).booleanValue() != true)
         {
         failMsg += "old value incorrect\n";
         }
         if (((Boolean)newPropValue).booleanValue() != false)
         {
         failMsg += "new value incorrect\n";
         }
         if (sys.isConnected() != false)
         {
         failMsg += "property not changed\n";
         }

         assertCondition(failMsg.equals(""), "\n" + failMsg);
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         }

         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Verify that disconnectAllServices, when there are no connections, does not result in a property change event.
     **/
    public void Var025()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.disconnectAllServices();
         failMsg += verifyNoEvent();

         assertCondition(failMsg.equals(""), "\n" + failMsg);
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         }
         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Verify that a disconnect without a connection does not result in a property change event.
     **/
    public void Var026()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.disconnectService(AS400.FILE);
         failMsg += verifyNoEvent();

         assertCondition(failMsg.equals(""), "\n" + failMsg);
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         }
         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Connect to a service, verify that a second connect does not result in a property change event
     **/
    public void Var027()
    {
        succeeded();
        /*	try
         {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
         sys.setMustUseSockets(mustUseSockets_);
         String failMsg = "";

         resetState();
         sys.connectService(AS400.COMMAND);
         failMsg += verifyNoEvent();

         sys.addPropertyChangeListener(this);
         try
         {
         resetState();
         sys.connectService(AS400.FILE);
         failMsg += verifyNoEvent();

         assertCondition(failMsg.equals(""), "\n" + failMsg);
         }
         finally
         {
         sys.removePropertyChangeListener(this);
         sys.disconnectAllServices();
         }
         }
         catch (Exception e)
         {
         failed(e, "Unexpected exception");
         }*/
    }

    /**
     Call setShowCheckboxes on the system object.
     Verify that the property changed.
     **/
    public void Var028()
    {
        try
        {
            SecureAS400 sys = new SecureAS400();
            sys.setMustUseSockets(mustUseSockets_);
            String failMsg = "";

            resetState();
            sys.setShowCheckboxes(true);
            failMsg += verifyNoEvent();
            if (sys.isShowCheckboxes() != true)
            {
                failMsg += "property not changed\n";
            }

            sys.addPropertyChangeListener(this);
            try
            {
                resetState();
                sys.setShowCheckboxes(false);
                failMsg += verifyNoEvent();
                if (sys.isShowCheckboxes() != false)
                {
                    failMsg += "property not changed\n";
                }

                assertCondition(failMsg.equals(""), "\n" + failMsg);
            }
            finally
            {
                sys.removePropertyChangeListener(this);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
