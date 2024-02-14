///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalGroupBeanInfoTestcase.java
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
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueList;
import com.ibm.as400.access.SystemValueGroup;
import com.ibm.as400.access.SystemValueEvent;
import com.ibm.as400.access.SystemValueListener;

import test.JTOpenTestEnvironment;
import test.Testcase;

import com.ibm.as400.access.ObjectEvent;
import com.ibm.as400.access.ObjectListener;
import com.ibm.as400.access.ExtendedIllegalStateException;

/**
 * Testcase SysvalGroupBeanInfoTestcase.
 **/
public class SysvalGroupBeanInfoTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
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
      if (JTOpenTestEnvironment.isWindows)
      {
        DOS_ = true;
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

	if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED) 
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
	if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
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
	if ((allVariations || variationsToRun_.contains("7")) && runMode_ != ATTENDED)
	{
	    setVariation(7);
	    Var007();
	}
	if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED) // Note: This is an unattended variation.
	{
	    setVariation(8);
	    Var008();
	}
	if ((allVariations || variationsToRun_.contains("9")) && runMode_ != ATTENDED)
	{
	    setVariation(9);
	    Var009();
	}
	if ((allVariations || variationsToRun_.contains("10")) && runMode_ != ATTENDED)
	{
	    setVariation(10);
	    Var010();
	}
	if ((allVariations || variationsToRun_.contains("11")) && runMode_ != ATTENDED)
	{
	    setVariation(11);
	    Var011();
	}
	if ((allVariations || variationsToRun_.contains("12")) && runMode_ != ATTENDED)
	{
	    setVariation(12);
	    Var012();
	}
	if ((allVariations || variationsToRun_.contains("13")) && runMode_ != ATTENDED)
	{
	    setVariation(13);
	    Var013();
	}
	if ((allVariations || variationsToRun_.contains("14")) && runMode_ != ATTENDED)
	{
	    setVariation(14);
	    Var014();
	}
	if ((allVariations || variationsToRun_.contains("15")) && runMode_ != ATTENDED)
	{
	    setVariation(15);
	    Var015();
	}

	if ((allVariations || variationsToRun_.contains("16")) && runMode_ != ATTENDED)
	{
	    setVariation(16);
	    Var016();
	}
	if ((allVariations || variationsToRun_.contains("17")) && runMode_ != ATTENDED)
	{
	    setVariation(17);
	    Var017();
	}
	if ((allVariations || variationsToRun_.contains("18")) && runMode_ != ATTENDED)
	{
	    setVariation(18);
	    Var018();
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
     * Verify property change events for SystemValueGroup::setGroupName(String).
     **/
    public void Var001()
    {
      try
      {
        String oldName = "MyGroup";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, oldName, "Desc");
        resetValues();
        svg.addPropertyChangeListener(this);
        String newName = "MyNewGroup";
        svg.setGroupName(newName);
        // Verify event
        if (verifyPropChange("groupName", oldName, newName, svg, svg.getGroupName()))
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
     * Verify property change events for SystemValueGroup::setGroupDescription(String).
     **/
    public void Var002()
    {
      try
      {
        String oldDesc = "GroupDesc";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", oldDesc);
        resetValues();
        svg.addPropertyChangeListener(this);
        String newDesc = "NewGroupDesc";
        svg.setGroupDescription(newDesc);
        // Verify event
        if (verifyPropChange("groupDescription", oldDesc, newDesc, svg, svg.getGroupDescription()))
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
     * Verify property change events for SystemValueGroup::setSystem(AS400).
     **/
    public void Var003()
    {
      try
      {
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Desc");
        resetValues();
        svg.addPropertyChangeListener(this);
        AS400 newSys = new AS400();
        svg.setSystem(newSys);
        // Verify event
        if (verifyPropChange("system", systemObject_, newSys, svg, svg.getSystem()))
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
     * Verify property change events do not occur for SystemValueGroup::setGroupName(String).
     **/
    public void Var004()
    {
      try
      {
        String oldName = "MyGroup";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, oldName,"Desc");
        resetValues();
        svg.addPropertyChangeListener(this);
        String newName = "MyNewGroup";
        svg.removePropertyChangeListener(this);
        svg.setGroupName(newName);
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
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }


   /**
     * Verify property change events do not occur for SystemValueGroup::setGroupDescription(String).
     **/
    public void Var005()
    {
      try
      {
        String oldDesc = "MyDesc";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name",oldDesc);
        resetValues();
        svg.addPropertyChangeListener(this);
        String newDesc = "MyNewDesc";
        svg.removePropertyChangeListener(this);
        svg.setGroupDescription(newDesc);
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
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify property change events do not occur for SystemValueGroup::setSystem(AS400).
     **/
    public void Var006()
    {
      try
      {
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name","Desc");
        resetValues();
        svg.addPropertyChangeListener(this);
        AS400 newSys = new AS400();
        svg.removePropertyChangeListener(this);
        svg.setSystem(newSys);
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
     * Verify vetoable change events for SystemValue::setGroupName(String).
     * Don't veto.
     **/
    public void Var007()
    {
      try
      {
        String oldName = "MyName";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, oldName, "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        String newName = "MyNewName";
        svg.setGroupName(newName);
        // Verify event
        if (verifyVetoChange("groupName", oldName, newName, svg, svg.getGroupName()))
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
     * Verify vetoable change events for SystemValueGroup::setGroupDescription(String).
     * Don't veto.
     **/
    public void Var008()
    {
      try
      {
        String oldDesc = "MyDesc";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", oldDesc);
        resetValues();
        svg.addVetoableChangeListener(this);
        String newDesc = "MyNewDesc";
        svg.setGroupDescription(newDesc);
        // Verify event
        if (verifyVetoChange("groupDescription", oldDesc, newDesc, svg, svg.getGroupDescription()))
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
     * Verify vetoable change events for SystemValueGroup::setSystem(AS400).
     * Don't veto.
     **/
    public void Var009()
    {
      try
      {
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name","Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        AS400 newSys = new AS400();
        svg.setSystem(newSys);
        // Verify event
        if (verifyVetoChange("system", systemObject_, newSys, svg, svg.getSystem()))
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
     * Verify vetoable change events do not occur for SystemValueGroup::setGroupName(String).
     * Don't veto.
     **/
    public void Var010()
    {
      try
      {
        String oldName = "MyGroup";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, oldName, "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        String newName = "MyNewGroup";
        svg.removeVetoableChangeListener(this);
        svg.setGroupName(newName);
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
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }


    /**
     * Verify vetoable change events do not occur for SystemValueGroup::setGroupDescription(String).
     * Don't veto.
     **/
    public void Var011()
    {
      try
      {
        String oldDesc = "MyDesc";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", oldDesc);
        resetValues();
        svg.addVetoableChangeListener(this);
        String newDesc = "MyNewDesc";
        svg.removeVetoableChangeListener(this);
        svg.setGroupDescription(newDesc);
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
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events do not occur for SystemValueGroup::setSystem(AS400).
     * Don't veto.
     **/
    public void Var012()
    {
      try
      {
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        AS400 newSys = new AS400();
        svg.removeVetoableChangeListener(this);
        svg.setSystem(newSys);
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
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events for SystemValueGroup::setGroupName(String).
     * Veto.
     **/
    public void Var013()
    {
      try
      {
        String oldName = "MyGroup";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, oldName, "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        veto_ = true;
        String newName = "MyNewGroup";
        try
        {
          svg.setGroupName(newName);
        }
        catch(Exception e)
        {
          // Verify event
          if (verifyVetoChange("groupName", oldName, newName, svg, svg.getGroupName()))
          {
            succeeded();
          }
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events for SystemValueGroup::setGroupDescription(String).
     * Veto.
     **/
    public void Var014()
    {
      try
      {
        String oldDesc = "MyDesc";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", oldDesc);
        resetValues();
        svg.addVetoableChangeListener(this);
        veto_ = true;
        String newDesc = "MyNewDesc";
        try
        {
          svg.setGroupDescription(newDesc);
        }
        catch(Exception e)
        {
          // Verify event
          if (verifyVetoChange("groupDescription", oldDesc, newDesc, svg, svg.getGroupDescription()))
          {
            succeeded();
          }
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }


    /**
     * Verify vetoable change events for SystemValueGroup::setSystem(AS400).
     * Veto.
     **/
    public void Var015()
    {
      try
      {
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        veto_ = true;
        AS400 newSys = new AS400();
        try
        {
          svg.setSystem(newSys);
        }
        catch(Exception e)
        {
          // Verify event
          if (verifyVetoChange("system", systemObject_, newSys, svg, svg.getSystem()))
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
     * Verify vetoable change events do not occur for SystemValueGroup::setGroupName(String).
     * Veto.
     **/
    public void Var016()
    {
      try
      {
        String oldName = "MyGroup";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, oldName, "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        veto_ = true;
        String newName = "MyNewGroup";
        try
        {
          svg.removeVetoableChangeListener(this);
          svg.setGroupName(newName);
          succeeded();
        }
        catch(Exception e)
        {
          failed("Exception fired.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Verify vetoable change events do not occur for SystemValueGroup::setGroupDescription(String).
     * Veto.
     **/
    public void Var017()
    {
      try
      {
        String oldDesc = "MyDesc";
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name" , oldDesc);
        resetValues();
        svg.addVetoableChangeListener(this);
        veto_ = true;
        String newDesc = "MyNewDesc";
        try
        {
          svg.removeVetoableChangeListener(this);
          svg.setGroupDescription(newDesc);
          succeeded();
        }
        catch(Exception e)
        {
          failed("Exception fired.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }


    /**
     * Verify vetoable change events do not occur for SystemValueGroup::setSystem(AS400).
     * Veto.
     **/
    public void Var018()
    {
      try
      {
        SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Desc");
        resetValues();
        svg.addVetoableChangeListener(this);
        veto_ = true;
        AS400 newSys = new AS400();
        try
        {
          svg.removeVetoableChangeListener(this);
          svg.setSystem(newSys);
          succeeded();
        }
        catch(Exception e)
        {
          failed("Exception fired.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

}
