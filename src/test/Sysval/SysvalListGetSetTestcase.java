///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalListGetSetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;


import java.io.FileOutputStream;
import java.util.Vector;
import java.util.ResourceBundle;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueList;

import test.Testcase;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.RequestNotSupportedException;

/**
 * Testcase SysvalListGetSetTestcase.
 *
 * Test variations for the methods:
 * <ul>
 * <li>SystemValueList::getGroup(int)
 * <li>SystemValueList::getGroupCount()
 * <li>SystemValueList::getGroupDescription(int)
 * <li>SystemValueList::getGroupName(int)
 * <li>SystemValueList::getSystem()
 * <li>SystemValueList::setSystem(AS400)
 * </ul>
 **/
public class SysvalListGetSetTestcase extends Testcase
{
    /**
     * Runs the variations requested.
     **/
    public void run()
    {
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

	if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED)
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

	if ((allVariations || variationsToRun_.contains("19")) && runMode_ != ATTENDED)
	{
	    setVariation(19);
	    Var019();
	}

	if ((allVariations || variationsToRun_.contains("20")) && runMode_ != ATTENDED)
	{
	    setVariation(20);
	    Var020();
	}
    }


    /**
     * Verify invalid usage of SystemValueList::getGroup().
     * Try to get the group from a system value list created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var001()
    {
      try
      {
        SystemValueList sv = new SystemValueList();
        sv.getGroup(SystemValueList.GROUP_ALL);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::getGroup().
     * Try to get a non-existent group.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var002()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        sv.getGroup(SystemValueList.GROUP_ALL+1);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "group",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::getGroup().
     * Try to get a non-existent group.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var003()
    {
      try
      {
        SystemValueList sv = new SystemValueList(systemObject_);
        sv.getGroup(-1);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "group",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getGroup().
     * Try to get the group from a system value list which has the
     * system set on the constructor.
     **/
    public void Var004()
    {
      try
      {
        SystemValueList sv = new SystemValueList(pwrSys_);
        Vector vec = sv.getGroup(SystemValueList.GROUP_ALL);
        if (vec == null)
	{
          failed("getGroup() returned null.");
	}
        else if (vec.size() < 160)
        {
          failed("Incorrect number of group entries: "+vec.size()); 
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getGroup().
     * Try to get the group from a system value list which has the
     * system set using setSystem().
     **/
    public void Var005()
    {
      try
      {
        SystemValueList sv = new SystemValueList();
        sv.setSystem(pwrSys_);
        Vector vec = sv.getGroup(SystemValueList.GROUP_ALL);
        if (vec == null)
	{
          failed("getGroup() returned null.");
	}
        else if (vec.size() < 160)
        {
          failed("Incorrect number of group entries: "+vec.size()); 
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getGroupCount().
     * Try to get the group count.
     **/
    public void Var006()
    {
      try
      {
        int i = SystemValueList.getGroupCount();
        if (i != 10)
          failed("Wrong group count: "+i);
        else
          succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::getGroupDescription().
     * Try to get the group description of a non-existent group.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var007()
    {
      try
      {
        SystemValueList.getGroupDescription(-1);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "group",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::getGroupDescription().
     * Try to get the group description of a non-existent group.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var008()
    {
      try
      {
        SystemValueList.getGroupDescription(SystemValueList.GROUP_ALL+1);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "group",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getGroupDescription().
     * Try to get the group description of an existing group.
     **/
    public void Var009()
    {
      try
      {
        String desc = SystemValueList.getGroupDescription(SystemValueList.GROUP_NET);
        ResourceBundle res = ResourceBundle.getBundle("com.ibm.as400.access.SVMRI");
        if (!desc.trim().equals(((String)res.getString("SYSTEM_VALUE_GROUP_NET_DESC")).trim()))
        {
          failed("Incorrect description.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::getGroupName().
     * Try to get the group description of a non-existent group.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var010()
    {
      try
      {
        SystemValueList.getGroupName(-1);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "group",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::getGroupName().
     * Try to get the group description of a non-existent group.
     * The method should throw an ExtendedIllegalArgumentException.
     **/
    public void Var011()
    {
      try
      {
        SystemValueList.getGroupName(SystemValueList.GROUP_ALL+1);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "group",
                        ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getGroupName().
     * Try to get the group name of an existing group.
     **/
    public void Var012()
    {
      try
      {
        String desc = SystemValueList.getGroupName(SystemValueList.GROUP_NET);
        ResourceBundle res = ResourceBundle.getBundle("com.ibm.as400.access.SVMRI");
        if (!desc.trim().equals(((String)res.getString("SYSTEM_VALUE_GROUP_NET_NAME")).trim()))
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getSystem().
     * Try to get the system of a system value list created using
     * the default constructor.
     **/
    public void Var013()
    {
      try
      {
        SystemValueList sv = new SystemValueList();
        if (sv.getSystem() != null)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getSystem().
     * Try to get the system of a system value list which has the
     * system set on the constructor.
     **/
    public void Var014()
    {
      try
      {
        SystemValueList sv = new SystemValueList(pwrSys_);
        if (sv.getSystem() != pwrSys_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::getSystem().
     * Try to get the system of a system value list which has the
     * system set using setSystem().
     **/
    public void Var015()
    {
      try
      {
        SystemValueList sv = new SystemValueList();
        sv.setSystem(pwrSys_);
        if (sv.getSystem() != pwrSys_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSystem().
     * Try to get the system of a system value list which has the
     * system set using setSystem() after the system was already set.
     **/
    public void Var016()
    {
      try
      {
        SystemValueList sv = new SystemValueList(pwrSys_);
        sv.setSystem(systemObject_);
        if (sv.getSystem() != systemObject_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::setSystem().
     * Try to set the system of a system value list created using
     * the default constructor.
     **/
    public void Var017()
    {
      try
      {
        SystemValueList sv = new SystemValueList();
        sv.setSystem(pwrSys_);
        if (sv.getSystem() != pwrSys_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValueList::setSystem().
     * Try to set the system of a system value by passing in null.
     * A NullPointerException should be thrown.
     **/
    public void Var018()
    {
      try
      {
        SystemValueList sv = new SystemValueList();
        sv.setSystem(null);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "system"))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValueList::setSystem().
     * Try to set the system of a system value which has the
     * system already set.
     **/
    public void Var019()
    {
      try
      {
        SystemValueList sv = new SystemValueList(pwrSys_);
        sv.setSystem(systemObject_);
        if (sv.getSystem() != systemObject_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setSystem().
     * Try to set the system of a system value which has the
     * system already set and has already connected.
     * An ExtendedIllegalStateException should be thrown.
     **/
    public void Var020()
    {
      try
      {
        SystemValueList sv = new SystemValueList(pwrSys_);
        sv.getGroup(SystemValueList.GROUP_ALL); // causes a connection
        sv.setSystem(systemObject_);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }
}
