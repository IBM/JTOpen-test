///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalListBeanInfoTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;


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
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemValueList;

import test.Testcase;

import com.ibm.as400.access.ObjectEvent;
import com.ibm.as400.access.ObjectListener;
import com.ibm.as400.access.ExtendedIllegalStateException;

/**
 * Testcase SysvalListBeanInfoTestcase.
 **/
public class SysvalListBeanInfoTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
    AS400 PwrSys_ = pwrSys_;
    String goodYear = "";
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

    private String datestUserID = " USER(DATEST) ";
    private String operatingSystem_;
    private boolean DOS_ = false;
    private static String dirName_;
    private boolean OS2_ = false;
    private boolean OS400_ = false;
    private boolean usingNativeImpl = false;

    PropertyChangeEvent propChange;
    PropertyChangeEvent vetoChange;
    PropertyChangeEvent vetoRefire;
    int daN = -1;

    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup()
      throws Exception
    {

      // Determine operating system we're running under
      operatingSystem_ = System.getProperty("os.name");
      if (operatingSystem_.indexOf("Windows") >= 0 ||
          operatingSystem_.indexOf("DOS") >= 0 ||
          operatingSystem_.indexOf("OS/2") >= 0)
      {
        DOS_ = true;
      }

      // Are we in OS/2? If so, need different commands for deleting stuff...
      if (operatingSystem_.indexOf("OS/2") >= 0)
      {
        OS2_ = true;
      }

      // Are we running on the AS/400?
      else if (operatingSystem_.indexOf("OS/400") >= 0)
      {
        OS400_ = true;
//        if (!SysvalTest.usingSockets)
//        {
//           usingNativeImpl = true;
//           output_.println("Will use native implementation");
//        }
      }

      output_.println("Running under: " + operatingSystem_);
      output_.println("DOS-based file structure: " + DOS_);
      output_.println("Executing " + (isApplet_ ? "applet." : "application."));
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        try
        {
          setup();
        }
        catch (Exception e)
        {
          output_.println("Setup failed.");
          return;
        }

        if (usingNativeImpl)
           datestUserID = " USER(" + systemObject_.getUserId() + ") ";

        boolean allVariations = (variationsToRun_.size() == 0);

	if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED) // Note: This is an unattended variation.
	{
	    setVariation(1);
	    Var001();
	}
	if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
	{
	    setVariation(2);
	    Var002();
	}
	if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
	{
	    setVariation(3);
	    Var003();
	}
	if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED) // Note: This is an unattended variation.
	{
	    setVariation(4);
	    Var004();
	}
	if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
	{
	    setVariation(5);
	    Var005();
	}
	if ((allVariations || variationsToRun_.contains("6")) && runMode_ != ATTENDED)
	{
	    setVariation(6);
	    Var006();
	}
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

    /**
     * PROPERTY CHANGE TESTING
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
     * Verify property change events for SystemValueList::setSystem(AS400).
     **/
    public void Var001()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        resetValues();
        sv.addPropertyChangeListener(this);
        AS400 newSys = new AS400();
        sv.setSystem(newSys);
        // Verify event
        if (verifyPropChange("system", systemObject_, newSys, sv, sv.getSystem()))
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }


    /**
     * Verify property change events do not occur for SystemValueList::setSystem(AS400).
     **/
    public void Var002()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        resetValues();
        sv.addPropertyChangeListener(this);
        AS400 newSys = new AS400();
        sv.removePropertyChangeListener(this);
        sv.setSystem(newSys);
        if (propChange != null)
          failed("Property change event fired.");
        else
          succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }


    /**
     * VETOABLE CHANGE TESTING
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
     * Verify vetoable change events for SystemValueList::setSystem(AS400).
     * Don't veto.
     **/
    public void Var003()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        resetValues();
        sv.addVetoableChangeListener(this);
        AS400 newSys = new AS400();
        sv.setSystem(newSys);
        // Verify event
        if (verifyVetoChange("system", systemObject_, newSys, sv, sv.getSystem()))
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events do not occur for SystemValueList::setSystem(AS400).
     * Don't veto.
     **/
    public void Var004()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        resetValues();
        sv.addVetoableChangeListener(this);
        AS400 newSys = new AS400();
        sv.removeVetoableChangeListener(this);
        sv.setSystem(newSys);
        if (vetoChange != null)
          failed("Vetoable change event fired.");
        else
          succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events for SystemValueList::setSystem(AS400).
     * Veto.
     **/
    public void Var005()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        resetValues();
        sv.addVetoableChangeListener(this);
        veto_ = true;
        AS400 newSys = new AS400();
        try
        {
          sv.setSystem(newSys);
        }
        catch(Exception e)
        {
          // Verify event
          if (verifyVetoChange("system", systemObject_, newSys, sv, sv.getSystem()))
          {
            succeeded();
          }
        }
        // Verify event
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events do not occur for SystemValueList::setSystem(AS400).
     * Veto.
     **/
    public void Var006()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        resetValues();
        sv.addVetoableChangeListener(this);
        veto_ = true;
        AS400 newSys = new AS400();
        try
        {
          sv.removeVetoableChangeListener(this);
          sv.setSystem(newSys);
          succeeded();
        }
        catch(Exception e)
        {
          failed(e, "Exception fired.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }
}
