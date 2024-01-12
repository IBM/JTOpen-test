///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalListSerialization.java
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

/**
 * Testcase SysvalListSerialization.
 **/
public class SysvalListSerialization extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
    AS400 PwrSys_ = pwrSys_;
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
//        if (!DATest.usingSockets)
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
     * Test serialization of a SystemValueList.
     **/
    public void Var001()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);

        // Serialize sv to a file.
        FileOutputStream f = new FileOutputStream("svl.ser");
        ObjectOutput s =  new ObjectOutputStream(f);
        s.writeObject(sv);
        s.flush();
        try
        {
          // Deserialize sv from a file.
          FileInputStream in = new FileInputStream("svl.ser");
          ObjectInputStream s2 = new ObjectInputStream(in);
          SystemValueList sv2 = (SystemValueList)s2.readObject();

          if (false == sv2.getSystem().getSystemName().equals(sv.getSystem().getSystemName()))
          {
            failed("System changed to " + sv2.getSystem().getSystemName());
            return;
          }
          else if (false == sv2.getSystem().getUserId().equals( sv.getSystem().getUserId()))
          {
            failed("User changed to " + sv2.getSystem().getUserId());
            return;
          }
          else
          {
            succeeded();
          }
        }
        finally
        {
          File fd = new File("svl.ser");
          fd.delete();
        }
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     * Ensure that a SystemValueList will correctly serialize and deserialize itself.
     * Verify that system is preserved.  Verify that
     * listeners aren't preserved.
     **/
    public void Var002()
    {
      SystemValueList sv = null;
      try
      {
        sv = new SystemValueList(systemObject_);
        sv.getGroup(SystemValueList.GROUP_NET);

        sv.addVetoableChangeListener(this);
        sv.addPropertyChangeListener(this);

        // serialize
        FileOutputStream fos = new FileOutputStream("svl.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(sv);
        fos.close();

        try
        {
          resetValues();
          // deserialize
          FileInputStream fis = new FileInputStream("svl.ser");
          ObjectInputStream ois = new ObjectInputStream(fis);
          SystemValueList sv2 = (SystemValueList) ois.readObject();
          fis.close();

          String systemName1 = sv.getSystem().getSystemName();
          String systemName2 = sv2.getSystem().getSystemName();
          String systemUserId1 = sv.getSystem().getUserId();
          String systemUserId2 = sv2.getSystem().getUserId();

          boolean et = false;
          try
          {
            // This should not be allowed
            sv2.setSystem(new AS400());
          }
          catch(Exception exc)
          {
            et = exceptionIs(exc, "ExtendedIllegalStateException");
          }

          if (!et)
          {
            failed("System was reset.");
          }
          else if (propChange != null)
          {
            failed("Property change fired.");
          }
          else if (vetoChange != null)
          {
            failed("Vetoable change fired.");
          }
          else if (false == systemName1.equals(systemName2))
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
          File fd = new File("svl.ser");
          fd.delete();
          resetValues();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception occurred.");
      }
    }
}
