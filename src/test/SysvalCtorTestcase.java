///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueList;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
 * Testcase SysvalCtorTestcase.
 *
 * Test variations for the methods:
 * <ul>
 * <li>SystemValue()
 * <li>SystemValue(AS400, String)
 * </ul>
 **/
public class SysvalCtorTestcase extends Testcase
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
    }


    /**
     * Construct a system value passing a null for the system.
     * An NullPointerException should be thrown.
     **/
    public void Var001()
    {
      try
      {
        SystemValue sv = new SystemValue(null, "QDAY");
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
     * Construct a system value passing a null for the name.
     * An NullPointerException should be thrown.
     **/
    public void Var002()
    {
      try
      {
        SystemValue sv = new SystemValue(systemObject_, null);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "name"))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Construct a system value passing a name with length 0.
     * An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var003()
    {
      try
      {
        SystemValue sv = new SystemValue(systemObject_, "");
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name",
                ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Construct a system value passing a name that is not a valid system value.
     * An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var004()
    {
      try
      {
        SystemValue sv = new SystemValue(systemObject_, "TRASH");
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH",
                        ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Successful construction of a system value using ctor with parms.
     **/
    public void Var005()
    {
      try
      {
        SystemValue sv = new SystemValue(systemObject_, "QDAY");
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Successful construction of a system value using default ctor.
     **/
    public void Var006()
    {
      try
      {
        SystemValue sv = new SystemValue();
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }
}
