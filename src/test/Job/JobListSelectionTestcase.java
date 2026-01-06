///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DateTimeConverterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2000-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.Job;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RJob;
import com.ibm.as400.resource.RJobList;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

/**
Testcase RJobListSelectionTestcase.

<p>This tests the following BufferedResourceList methods:
<ul>
<li>getSelectionMetaData()
<li>getSelectionMetaData(Object)
<li>getSelectionValue()
<li>getPresentation()
<li>setSelectionValue(Object,Object)
<li>toString()
</ul>
**/
@SuppressWarnings({"deprecation","unused","resource","rawtypes"})
public class JobListSelectionTestcase
extends Testcase
{
    private String userName_ ;
    private String JobName_;
    private String JobNumber_;
    private String batchNumber_;
    private String interactiveUser = "TSTJOBUSR1";

  private void inputParam( String interactiveUser )
  {
    try
    {
        CommandCall ccallPow_ = new CommandCall( pwrSys_ );
        RJobList jobList = new RJobList( pwrSys_ );
        jobList.setSelectionValue(RJobList.JOB_TYPE, RJob.JOB_TYPE_INTERACTIVE);
        jobList.open();
        jobList.waitForComplete();
        int i;
        for (i=0; i< jobList.getListLength(); ++i)
        {
           RJob job = (RJob) jobList.resourceAt(i);
           if (((String) job.getAttributeValue(RJob.JOB_TYPE)).equals("I") &&
               ((String) job.getAttributeValue(RJob.JOB_SUBTYPE)).trim().equals("") &&
               ((String) job.getAttributeValue(RJob.COMPLETION_STATUS)).length() ==0 &&
               ((String) job.getAttributeValue(RJob.USER_NAME)).trim().toUpperCase().equals(interactiveUser))
           {
               userName_ = (String) job.getAttributeValue(RJob.USER_NAME);
               JobName_ = (String) job.getAttributeValue(RJob.JOB_NAME);
               JobNumber_ = (String) job.getAttributeValue(RJob.JOB_NUMBER);
           }
           // Get a batch RJob for variation 90
           if ( ((String) job.getAttributeValue(RJob.JOB_TYPE)).equals("M") &&
                ((String) job.getAttributeValue(RJob.JOB_NAME)).equals("QBATCH") &&
                ((String) job.getAttributeValue(RJob.USER_NAME)).equals("QSYS"))
           {
             batchNumber_ = (String) job.getAttributeValue(RJob.JOB_NUMBER);
           }
           try
           {
             Boolean bool = (Boolean) job.getAttributeValue(RJob.SIGNED_ON_JOB);
             if (!bool.booleanValue())
             {
               output_.println(job.getAttributeValue(RJob.USER_NAME)+","+job.getAttributeValue(RJob.JOB_NAME)+","+job.getAttributeValue(RJob.JOB_NUMBER)+" is not a signed-on RJob.");
             }
           }
           catch(Exception x)
           {
              output_.println(job.getAttributeValue(RJob.USER_NAME)+","+job.getAttributeValue(RJob.JOB_NAME)+","+job.getAttributeValue(RJob.JOB_NUMBER)+" has ended.");
           }
        }
        if (batchNumber_ == null)
        {
          output_.println("Note: No QBATCH RJob was found, some variations may fail.");
        }
    }
    catch (Exception e)
    {
        e.printStackTrace(output_);
    }
  }



  /**
   Creates several objects.
  **/
  protected boolean setupTestcase()
  {
    if (pwrSys_ == null)
    {
        output_.println("-misc testcase parm with PwrSys not specified.");
        return false;
    }

    // First, create the user profile just to make sure it exists.
    try
    {
        CommandCall ccallPow_ = new CommandCall( pwrSys_ );
        ccallPow_.setCommand("CRTUSRPRF USRPRF("+interactiveUser+") PASSWORD(JTEAM1)");
        ccallPow_.run();
    }
    catch(Exception e) {}

    inputParam(interactiveUser);

    if (JobName_ == null || userName_ == null || JobNumber_ == null)
    {
        String err = "Please bring up an AS/400 session to "+pwrSys_.getSystemName();
        err += ", and \nsign-on as "+interactiveUser+". Run this testcase while that ";
        err += "\nsession is active.";
        output_.println(err);
        return false;
    }
    return true;
  }



/**
Checks a particular selection meta data.
**/
    static boolean verifySelectionMetaData(ResourceMetaData smd, 
                                            Object attributeID, 
                                            Class attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        return((smd.areMultipleAllowed() == multipleAllowed)
               && (smd.getDefaultValue() == (defaultValue))
               && (smd.getPossibleValues().length == possibleValueCount)
               && (smd.getPresentation() != null)
               && (smd.getType() == attributeType)
               && (smd.isReadOnly() == readOnly)
               && (smd.isValueLimited() == valueLimited)
               && (smd.toString().equals(attributeID)));
    }



/**
Checks a particular selection meta data.
**/
    boolean verifySelectionMetaData(ResourceMetaData[] smd, 
                                            Object attributeID, 
                                            Class attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        int found = -1;
        for (int i = 0; (i < smd.length) && (found < 0); ++i) {
            if (smd[i].getID() == attributeID)
                found = i;
        }

        if (found < 0) {
            output_.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifySelectionMetaData(smd[found], 
                                       attributeID, 
                                       attributeType, 
                                       readOnly, 
                                       possibleValueCount, 
                                       defaultValue, 
                                       valueLimited, 
                                       multipleAllowed);
    }


    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        if (!setupTestcase())
        {
          throw new Exception("Setup Failed!");
        }
    }


/**
close() -- Valid close with default constructor followed by an open.
**/
  public void Var001 ()
  {
    try
    {
        RJobList f = new RJobList ();
        f.close();
        succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
close() - Valid close with constructor RJobList(AS400) followed by an open()
**/
  public void Var002 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
close() - Valid close with constructor RJobList(AS400, String)
**/
  public void Var003 ()
    {
        try {
            RJobList f = new RJobList ();
            f.setSystem(systemObject_);
            f.open();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed( e, "Unexpected exception.");
        }
    }

/**
close() - Valid close on list that hasn't been open yet.  Nothing should happen.
**/
  public void Var004 ()
    {
        try {
            RJobList f = new RJobList ();
            f.close();
            succeeded();
        }
        catch (Exception e) {
           failed(e, "Unknown exception.");
        }
    }

/**
close() - Valid close on list that hasn't been open yet.  Nothing should happen
**/
  public void Var005 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

/**
close() - Valid close on list that hasn't been open yet.  Nothing should happen
**/
  public void Var006 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.close();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");

        }
    }

/**
close() - Attempt to close a list twice.  This should not cause an exception        
**/
  public void Var007 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

/**
close() - Attempt to load resources after a close. Nothing should happen since list should be implicity
opened by the refresh.
**/
  public void Var008 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            f.refreshContents();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

/**
close() - Valid use of close. Open/close, Open/close a message queue four times
**/
  public void Var009 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            f.open();
            f.refreshContents();
            f.close();
            f.open();
            f.close();
            f.open();
            f.getListLength();
            f.close();
            succeeded();
   
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
getSelectionMetaData(Object AttributeID) -- Test invalid use; pass null for attribute ID.
**/
  public void Var010 ()
    {
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionData = f.getSelectionMetaData(null);
            f.close();
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

/**
getSelectionMetaData(Object attributeID) -- call with default message queue constructor
**/
  public void Var011 ()
    {
        try {
            ResourceMetaData SelectionData;
            RJobList f = new RJobList ();
            f.setSystem(systemObject_);
            SelectionData = f.getSelectionMetaData(RJobList.JOB_NAME);
            f.close();
            succeeded();
   
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
getSelectionMetaData(Object AttributeID) -- Test invalid use; pass bad value for attribute ID.
**/
  public void Var012()
    {
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionData = f.getSelectionMetaData(RJob.ACCOUNTING_CODE);
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

/**
getSelectionMetaData(Object AttributeID) -- Test valid use; pass valid value for attribute ID.
**/
  public void Var013()
    {
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList ();
            SelectionData = f.getSelectionMetaData(RJobList.JOB_NAME);
            succeeded();
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }

/**
getSelectionMetaData(Object AttributeID) -- Test valid use; pass valid value for attribute ID.
**/
  public void Var014()
    {
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            SelectionData = f.getSelectionMetaData(RJobList.USER_NAME);
            f.close();
            succeeded();
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }


/**
getSelectionMetaData(Object AttributeID) -- Test valid use; pass valid value for attribute ID.
**/
  public void Var015()
    {
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList();
            f.setSystem(systemObject_);
            SelectionData = f.getSelectionMetaData(RJobList.JOB_NUMBER);
            succeeded();
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }

/**
getSelectionMetaData() with 1 parameter - Pass JOB_NAME
**/
    public void Var016()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSelectionMetaData(RJobList.JOB_NAME);
            assertCondition(verifySelectionMetaData(smd, RJobList.JOB_NAME, String.class, false, 3, RJobList.ALL, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSelectionMetaData() with 1 parameter - Pass JOB_NAME
**/
    public void Var017()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSelectionMetaData(RJobList.JOB_NUMBER);
            assertCondition(verifySelectionMetaData(smd, RJobList.JOB_NUMBER, String.class, false, 1, RJobList.ALL, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSelectionMetaData() with 1 parameter - Pass JOB_TYPE
**/
    public void Var018()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSelectionMetaData(RJobList.JOB_TYPE);
            assertCondition(verifySelectionMetaData(smd, RJobList.JOB_TYPE, String.class, false, 9, RJobList.ALL, true , false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionMetaData() with 1 parameter - Pass PRIMARY_JOB_STATUSES
**/
    public void Var019()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSelectionMetaData(RJobList.PRIMARY_JOB_STATUSES);
            assertCondition(verifySelectionMetaData(smd, RJobList.PRIMARY_JOB_STATUSES, String.class, false, 3, null, true, true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionMetaData() with 1 parameter - Pass USER_NAME
**/
    public void Var020()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSelectionMetaData(RJobList.USER_NAME);
            assertCondition(verifySelectionMetaData(smd, RJobList.USER_NAME, String.class, false, 2, RJobList.ALL,false,false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSelectionMetaData() with 1 parameter - Pass JOB_NAME
**/
    public void Var021()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition(verifySelectionMetaData(smd, RJobList.JOB_NAME, String.class, false, 3, RJobList.ALL, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSelectionMetaData() with 1 parameter - Pass JOB_NAME
**/
    public void Var022()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition(verifySelectionMetaData(smd, RJobList.JOB_NUMBER, String.class, false, 1, RJobList.ALL, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionMetaData() with 1 parameter - Pass JOB_TYPE
**/
    public void Var023()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition(verifySelectionMetaData(smd, RJobList.JOB_TYPE, String.class, false, 9, RJobList.ALL, true , false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionMetaData() with 1 parameter - Pass PRIMARY_JOB_STATUSES
**/
    public void Var024()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition(verifySelectionMetaData(smd, RJobList.PRIMARY_JOB_STATUSES, String.class, false, 3, null, true, true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionMetaData() with 1 parameter - Pass USER_NAME
**/
    public void Var025()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition(verifySelectionMetaData(smd, RJobList.USER_NAME, String.class, false, 2, RJobList.ALL,false,false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionMetaData() with 0 parameters - Verify that the array contains all
selections.
**/
  public void Var026()
  {
      try {
          RJobList u = new RJobList();
          ResourceMetaData[] smd = u.getSelectionMetaData();
          assertCondition(smd.length == 5) ;
      }

      catch (Exception e) {
          failed (e, "Unexpected Exception");
      }
  }

/**
getSelectionMetaData() with 1 parameter - Try each of them.
**/
    public void Var027()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            boolean success = true;
            for(int i = 0; i < smd.length; ++i) {
                boolean thisOne = verifySelectionMetaData(u.getSelectionMetaData(smd[i].getID()), 
                                                             smd[i].getID(), 
                                                             smd[i].getType(), 
                                                             smd[i].isReadOnly(), 
                                                             smd[i].getPossibleValues().length, 
                                                             smd[i].getDefaultValue(), 
                                                             smd[i].isValueLimited(),
                                                             smd[i].areMultipleAllowed());
                if (!thisOne) {
                    output_.println("Comparison failed for: " + smd[i] + ".");
                    success = false;
                }
            }
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSelectionValue(Object AttributeID) -- Test invalid use; pass null for attribute ID.
**/
  public void Var028 ()
    {
        Object SelectionValue;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(null);
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

/**
getSelectionValue(Object attributeID) -- call with default RJoblist constructor
**/
  public void Var029 ()
    {
        try {
            Object SelectionValue;
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(RJobList.JOB_NAME);
            succeeded();
   
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; pass bad value for attribute ID.
**/
  public void Var030()
    {
        Object SelectionValue;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue("Hi ya");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }


/**
getSelectionValue() - When the connection is bogus.
**/
    public void Var031()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RJobList u = new RJobList(system);
            Object selectionCriteria = u.getSelectionValue(RJobList.JOB_NAME);
            Object RJobNumber = u.getSelectionValue(RJobList.JOB_NUMBER);
            assertCondition((selectionCriteria.equals(RJobList.ALL) && (RJobNumber.equals(RJobList.ALL))));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionValue(Object AttributeID) -- Test valid use; pass value for attribute ID and check return 
  to ensure the default value is returned.
**/
  public void Var032()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(RJobList.JOB_NAME);
            SelectionData = f.getSelectionMetaData(RJobList.JOB_NAME);
            if (SelectionValue.equals(SelectionData.getDefaultValue()))
              succeeded();
            else
              failed("Default value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; pass value for attribute ID and check return 
  to ensure the default value is returned.
**/
  public void Var033()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(RJobList.JOB_NUMBER);
            SelectionData = f.getSelectionMetaData(RJobList.JOB_NUMBER);
            if (SelectionValue.equals(SelectionData.getDefaultValue()))
              succeeded();
            else
              failed("Default value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }


/**
getSelectionValue(Object AttributeID) -- Test valid use; pass value for attribute ID and check return 
  to ensure the default value is returned.
**/
  public void Var034()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(RJobList.JOB_TYPE);
            SelectionData = f.getSelectionMetaData(RJobList.JOB_TYPE);
            if (SelectionValue.equals(SelectionData.getDefaultValue()))
              succeeded();
            else
              failed("Default value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }


/**
getSelectionValue(Object AttributeID) -- Test valid use; pass value for attribute ID and check return 
  to ensure the default value is returned.
**/
  public void Var035()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(RJobList.PRIMARY_JOB_STATUSES);
            if (SelectionValue == null)
              succeeded();
            else
              failed("Default value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; pass value for attribute ID and check return 
  to ensure the default value is returned.
**/
  public void Var036()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        try {
            RJobList f = new RJobList ();
            SelectionValue = f.getSelectionValue(RJobList.USER_NAME);
            SelectionData = f.getSelectionMetaData(RJobList.USER_NAME);
            if (SelectionValue.equals(SelectionData.getDefaultValue()))
              succeeded();
            else
              failed("Default value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var037()
    {
        Object SelectionValue, SelectionValue1;
        ResourceMetaData SelectionData;
        RJob RJob;
        String values = "QBATCH";
        try {
            RJobList f = new RJobList (systemObject_);
            long listLength1 = f.getListLength();
            SelectionValue1 = f.getSelectionValue(RJobList.JOB_NAME);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_NAME,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              RJob = (RJob) f.resourceAt(0);
            else 
              RJob = null;
            if (RJob != null)
            {
               if (!RJob.getAttributeValue(com.ibm.as400.resource.RJob.JOB_NAME).equals("QBATCH"))
               {
                  failed("Bad RJob name returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.JOB_NAME);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.JOB_NAME, RJobList.ALL);
               f.refreshContents();
               long listLength = f.getListLength();
               if (f.getSelectionValue(RJobList.JOB_NAME).equals(SelectionValue1))
                   succeeded();
               else
                   failed("Default Selection value not returned.");
            }
            else
               failed("Re-set Selection value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var038()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        String[] values = { "0122D" };
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionValue = f.getSelectionValue(RJobList.JOB_NAME);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_NAME,values);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var039()  
  {
        Object SelectionValue,SelectionValue1;
        ResourceMetaData SelectionData;
        RJob  RJob;
        String values = JobNumber_;
        try {
            RJobList f = new RJobList (systemObject_);
            long listLength1 = f.getListLength();
            SelectionValue1 = f.getSelectionValue(RJobList.JOB_NUMBER);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_NUMBER,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              RJob = (RJob) f.resourceAt(0);
            else 
              RJob = null;
            if (RJob != null)
            {
               if (!RJob.getAttributeValue(com.ibm.as400.resource.RJob.JOB_NUMBER).equals(values))
               {
                  failed("Bad attribute value returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.JOB_NUMBER);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.JOB_NUMBER, RJobList.ALL);
               f.refreshContents();
               long listLength = f.getListLength();
               SelectionValue = f.getSelectionValue(RJobList.JOB_NUMBER);
               if (SelectionValue.equals(SelectionValue1) && 
                   listLength == listLength1 )
                   succeeded();
               else
                   failed("Default Selection value not returned.");
            }
            else
               failed("Re-set Selection value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }

    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var040()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        Integer intVal = new Integer(100);
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionValue = f.getSelectionValue(RJobList.JOB_NUMBER);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_NUMBER,intVal);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }


/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var041()  
  {
        Object SelectionValue,SelectionValue1;
        ResourceMetaData SelectionData;
        RJob  job;
        String values = RJob.JOB_TYPE_AUTOSTART;
        try {
            RJobList f = new RJobList (systemObject_);
            long listLength1 = f.getListLength();
            SelectionValue1 = f.getSelectionValue(RJobList.JOB_TYPE);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_TYPE,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              job = (RJob) f.resourceAt(0);
            else 
              job = null;
            if (job != null)
            {
               if (!job.getAttributeValue(RJob.JOB_TYPE).equals(values))
               {
                  failed("Bad attribute value returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.JOB_TYPE);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.JOB_TYPE, RJobList.ALL);
               f.refreshContents();
               long listLength = f.getListLength();
               SelectionValue = f.getSelectionValue(RJobList.JOB_TYPE);
               if (SelectionValue.equals(SelectionValue1) && 
                   listLength == listLength1 )
                   succeeded();
               else
                   failed("Default Selection value not returned.");
            }
            else
               failed("Re-set Selection value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }

    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var042()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        String values =  "jKLu" ;
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionValue = f.getSelectionValue(RJobList.JOB_TYPE);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_TYPE,values);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var043()  
  {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        RJob  job;
        String[] values = { RJob.JOB_STATUS_ACTIVE, RJob.JOB_STATUS_JOBQ };
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionValue = f.getSelectionValue(RJobList.PRIMARY_JOB_STATUSES);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              job  = (RJob) f.resourceAt(0);
            else 
              job  = null;
            if (job  != null)
            {
               if (!job.getAttributeValue(RJob.JOB_STATUS).equals(RJob.JOB_STATUS_ACTIVE) &&
                   !job.getAttributeValue(RJob.JOB_STATUS).equals(RJob.JOB_STATUS_JOBQ))
               {
                  failed("Bad attribute value returned for RJob.");
                  return;
               }
            }
            else
            {
               failed("No active RJobs or RJobs on RJob queue found.");
               return;
            }
            SelectionValue = f.getSelectionValue(RJobList.PRIMARY_JOB_STATUSES);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES, null);
               f.refreshContents();
               SelectionValue = f.getSelectionValue(RJobList.PRIMARY_JOB_STATUSES);
               if (SelectionValue == null)
                   succeeded();
               else
                   failed("Default Selection value not returned.");
            }
            else
               failed("Re-set Selection value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }

    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var044()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        String values =  "jKLu" ;
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionValue = f.getSelectionValue(RJobList.PRIMARY_JOB_STATUSES);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES,values);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var045()  
  {
        Object SelectionValue,SelectionValue1;
        ResourceMetaData SelectionData;
        RJob  job;
        String values = userName_;
        try {
            RJobList f = new RJobList (systemObject_);
            long listLength1 = f.getListLength();
            SelectionValue1 = f.getSelectionValue(RJobList.USER_NAME);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.USER_NAME,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              job  = (RJob) f.resourceAt(0);
            else 
              job  = null;
            if (job  != null)
            {
               if (!job.getAttributeValue(RJob.USER_NAME).equals(values))
               {
                  failed("Bad attribute value returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.USER_NAME);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.USER_NAME, null);
               f.refreshContents();
               long listLength = f.getListLength();
               SelectionValue = f.getSelectionValue(RJobList.USER_NAME);
               if (SelectionValue.equals(SelectionValue1) && 
                   listLength == listLength1 )
                   succeeded();
               else
                   failed("Default Selection value not returned.");
            }
            else
               failed("Re-set Selection value not returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }

    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var046()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        String values =  "jKLuGHujliendz";
        try {
            RJobList f = new RJobList (systemObject_);
            SelectionValue = f.getSelectionValue(RJobList.USER_NAME);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.USER_NAME,values);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }

/**
getListLength(Object AttributeID) -- Test invalid use; Try to get list length prior to open
**/
  public void Var047()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.getListLength();
            if (f.getListLength() > 0)
               succeeded();
            else
               failed("Bad list length returned.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }


/**
getListLength(Object AttributeID) -- getListLength of default constructor
**/
  public void Var048()
    {
        try {
            RJobList f = new RJobList ();
            f.getListLength();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

/**
getListLength(Object AttributeID) -- getListLength of constructor RJobList(system)
**/
  public void Var049()
    {
        try {
            RJobList f = new RJobList ();
            f.setSystem(systemObject_);
            f.open();
            if (f.getListLength() > 0)
              succeeded();
            else
              failed("Incorrect list length returned.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

/**
getListLength(Object AttributeID) -- getListLength of bogus system
**/
  public void Var050()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RJobList f = new RJobList (system);
            f.getListLength();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
           assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }


/**
getSelectionValue() - When the connection is bogus.
**/
    public void Var051()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RJobList u = new RJobList(system);
            Object selectionCriteria = u.getSelectionValue(RJobList.JOB_NAME);
            Object groupProfile = u.getSelectionValue(RJobList.JOB_NUMBER);
            assertCondition((selectionCriteria.equals(RJobList.ALL) && (groupProfile.equals(RJobList.ALL))));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getPresentation() -- Test valid use; Verify presentation object.
**/
  public void Var052()
    {
        Presentation presObj;
        try {
            RJobList f = new RJobList (systemObject_);
            presObj = f.getPresentation();
            if (presObj != null && presObj.getFullName().equals("Job List") && 
                presObj.getName().equals("Job List") && 
                presObj.getValue(Presentation.DESCRIPTION_TEXT) == null &&
                presObj.getValue(Presentation.HELP_TEXT) == null &&
                presObj.getValue(Presentation.ICON_COLOR_16x16) != null &&
                presObj.getValue(Presentation.ICON_COLOR_32x32) != null )
              succeeded();
            else
               failed("Bad presentation info returned.");
        }
        catch (Exception e) {
           if (exceptionIs(e, "IllegalStateException", "open"))
               succeeded();
           else
              failed(e, "Wrong exception info.");
        }
    }



/**
isComplete() -- Test valid use; Check if complete without open
**/
  public void Var053()
    {
       try {
           RJobList f = new RJobList (systemObject_);
            if (!f.isComplete())
               succeeded();
            else
               failed("isComplete() returning incorrect value.");
        }
        catch (Exception e) {
           failed(e, "Unknown exception.");
        }
    }

/**
isComplete() -- Test valid use; Check if complete for default constructor.
**/
  public void Var054()
    {
        try {
            RJobList f = new RJobList ();
            if (!f.isComplete())
               succeeded();
            else
               failed("isComplete() returning incorrect value.");
            f.close();
        }
        catch (Exception e) {
           failed(e, "Unknown exception.");
        }
    }

/**
isComplete() -- Test valid use; Check if complete after open and waitForComplete()
**/
  public void Var055()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            if (f.isComplete())
              succeeded();
            else
               failed("isComplete() returning incorrect value.");
            f.close();
        }
        catch (Exception e) {
           failed(e, "Unknown exception.");
        }
    }

/**
isComplete() -- Test valid use; Check if complete after open and refresh of large message queue.
**/
  public void Var056()
    {
        boolean TimedOut = false;
        int secs = 0;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            while (!f.isComplete() && !TimedOut)
            {
               Thread.sleep(2000);
               secs = secs + 2;
               if (secs > 90)
                  TimedOut = true;
               f.refreshStatus();
            }
            f.refreshContents();
            TimedOut = false;
            while (!f.isComplete() && !TimedOut)
            {
               Thread.sleep(2000);
               secs = secs + 2;
               if (secs > 90)
                  TimedOut = true;
               f.refreshStatus();
            }
            if (f.isComplete())
               succeeded();
            else if (TimedOut)
               failed("Time out prior to list loading.");
            else
               failed("isComplete() returned incorrect value.");
            f.close();
        }
        catch (Exception e) {
           failed(e, "Unknown exception.");
        }
    }


/**
isComplete() -- Test invalid use; Check if non-existent message queue isComplete
**/
  public void Var057()
    {
       try {
           AS400 system = new AS400("This", "is", "bad");
           system.setGuiAvailable(false);
           RJobList f = new RJobList (system);
            if (!f.isComplete())
               succeeded();
            else
               failed("isComplete() returning incorrect value.");
        }
        catch (Exception e) {
           failed(e, "Unknown exception.");
        }
    }


/**
isOpen() -- Check with default constructor followed by an open.
**/
  public void Var058 ()
  {
    try
    {
        RJobList f = new RJobList ();
        f.setSystem(systemObject_);
        f.open();
        if (f.isOpen())
          succeeded();
        else 
          failed("RJob list not open.");
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
isOpen() - Check with constructor RJobList(AS400) followed by an open()
**/
  public void Var059 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            if (f.isOpen())
              succeeded();
            else
              failed("RJob list not open.");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
isOpen() - Check on list that hasn't been open yet.  
**/
  public void Var060 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            if (!f.isOpen())
               succeeded();
            else
               failed("isOpen() returning true instead of false.");
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }

/**
isOpen() - Check after opening and then closing list
**/
  public void Var061 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            if (!f.isOpen())
               succeeded();
            else
               failed("isOpen returning true instead of false.");
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }

/**
isOpen() - Check after opening, closing, the re-opening list
**/
  public void Var062 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            f.open();
            if (f.isOpen())
               succeeded();
            else
               failed("isOpen returning false instead of true.");
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }

/**
isOpen() - Check after opening twice and then closing
**/
  public void Var063 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.open();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }


/**
isOpen() - Check after a series of opens/closes is done
**/
  public void Var064 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            f.open();
            f.close();
            if (!f.isOpen())
            {
               f.open();
               f.close();
               f.open();
               if (f.isOpen())
               {
                  succeeded();
                  f.close();
                }
            }
            else
               failed("isOpen() returning true instead of false.");
   
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
isOpen() - Check on list that hasn't been open yet.  
**/
  public void Var065 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            if (!f.isOpen())
               succeeded();
            else
               failed("isOpen() returning true instead of false.");
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }

/**
isResourceAvailable() -- Check with default constructor followed by an open.
**/
  public void Var066 ()
  {
    try
    {
        RJobList f = new RJobList ();
        f.setSystem(systemObject_);
        if (f.getListLength() > 0 && f.isResourceAvailable(0))
           succeeded();
        else if (f.getListLength() > 0)
           failed("isResourceAvailable returning false.");
        else
        {
           failed("Not applicable. List length is 0.");
        }
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
isResourceAvailable() -- Check with constructor RJobList(system) followed by an open.
**/
  public void Var067 ()
  {
    try
    {
        RJobList f = new RJobList (systemObject_);
        f.open();
        if (f.getListLength() > 0 && f.isResourceAvailable(0))
          succeeded();
        else if (f.getListLength() > 0) 
          failed("isResourceAvailable returning false.");
        else
          failed("Not applicable. List length = 0.");
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
isResourceAvailable(index) -- Test valid use; check availability of non-existent RJob
**/
  public void Var068 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            if (f.isResourceAvailable(5000) == false)
               succeeded();
            else
               failed("isResourceAvailable(0) returned TRUE for non-existent RJob.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }



/**
isResourceAvailable() - Check if resource available without doing an open first. Should return false.  
**/
  public void Var069 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            if (f.isResourceAvailable(0) == false)
               succeeded();
            else
               failed("isResourceAvailable(0) returned true for unopen list.");
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }


/**
isResourceAvailable() - Check if first and last resources are available from a queue.  
**/
  public void Var070 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            if (f.getListLength() > 0 && f.isResourceAvailable(0) )
            {
               // Loop until list is complete
               if (!f.isComplete())
                 f.waitForComplete();
              if (f.isResourceAvailable(f.getListLength()-1) )
                  succeeded();
               else
                  failed("Last resource not available.");
            }
            else
            {
               if (f.getListLength() > 0)
                  failed("First resource of RJob list is not available.");
               else
                  failed("N/A -- RJob list length is 0.");
             }
        }
        catch (Exception e) {
           failed(e, "Unexpected exception.");
        }
    }



/**
isResourceAvailable()  --  Check if resourceisAvailable before and after list is complete.
   
**/
  public void Var071()
    {
        int i;
        boolean timedOut = false;
        int secs = 0;
        boolean Failed = false;

        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            RJobList g = new RJobList(systemObject_);
            g.open();
            if (!g.isResourceAvailable(0) )
              failed("First resource not available.");
            else
            {
               while (!g.isComplete() && !timedOut && !Failed)
               {
                  Thread.sleep(2000);
                  secs = secs + 2;
                  if (secs > 240)
                     timedOut = true;
                  if (!g.isResourceAvailable(g.getListLength()-1))
                     Failed = true;
                  if (!g.isResourceAvailable(g.getListLength()-3))
                     Failed = true;
                  g.refreshStatus();
               }
               if (Failed)
                  failed("isResourceAvailable() returned false instead of true.");
               else if (g.isResourceAvailable(g.getListLength()-1) )  
                  succeeded();
               else
                  failed("Complete request timed out.");
            }
            g.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
  }


/**
isResourceAvailable()  --  Test invalid use. Pass -1 for index value.  A resource exception should
 be thrown.
**/
  public void Var072()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.isResourceAvailable(-1) ;
            f.close();
            failed("No exception thrown.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
  }


/**
open() -- Valid open with default constructor.
**/
  public void Var073()
  {
    try
    {
        RJobList f = new RJobList ();
        f.setSystem(systemObject_);
        f.open();
        f.close();
        succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
open() - Valid open with constructor RJobList(AS400)
**/
  public void Var074 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
open() - Invalid open on list that doesn't exist.  An IllegalStateException should be thrown
**/
  public void Var075 ()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RJobList f = new RJobList (system);
            f.open();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
           assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

/**
open() - Invalid open on undefined queue.  An IllegalStateException should be thrown
**/
  public void Var076 ()
    {
        try {
            RJobList f = new RJobList ();
            f.open();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

/**
open() - Open a list twice. This should cause no error.
**/
  public void Var077 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.open();
            succeeded();
        }
        catch (Exception e) {
           if (exceptionIs(e, "IllegalStateException", "open"))
               succeeded();
           else
              failed(e, "Wrong exception info.");
        }
    }

/**
open() - Open and close list a number of times
**/
  public void Var078 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.close();
            f.open();
            f.close();
            f.open();
            f.close();
            succeeded();

        }
        catch (Exception e) {
           failed(e,"Unexpected exception.");
        }
    }


/**
open() - Open and close list a number of times
**/
  public void Var079 ()
    {
        int i;

        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            for (i=0;i<20; ++i)
            {
              f.close();
              f.open();
            }
            f.close();
            succeeded();

        }
        catch (Exception e) {
           failed(e,"Unexpected exception.");
        }
    }


/**
refreshContents() - Valid refresh with default constructor.
**/
  public void Var080()
  {
    try
    {
        RJobList f = new RJobList ();
        f.setSystem(systemObject_);
        f.open();
        f.refreshContents();
        f.close();
        succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
refreshContents() - Valid refresh with constructor RJobList(AS400)
**/
  public void Var081 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.refreshContents();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
refreshContents() - Valid refresh with constructor RJobList(AS400, String)
**/
  public void Var082 ()
    {
        try {
            RJobList f = new RJobList ();
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }


/**
refreshContents() - Invalid use.  Change system and then refresh.  A ResourceException
  should be thrown.
**/
  public void Var083 ()
    {
        try {
            AS400 newSys_ = new AS400("RCHAS1DD","JAVA","JTEAM1");
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.setSystem(newSys_);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var084()
    {
        Object SelectionValue, SelectionValue1;
        ResourceMetaData SelectionData;
        RJob job;
        String values = "QBATCH";
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_NAME,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              job  = (RJob) f.resourceAt(0);
            else 
              job = null;
            if (job != null)
            {
               if (!job.getAttributeValue(RJob.JOB_NAME).equals("QBATCH"))
               {
                  failed("Bad RJob name returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.JOB_NAME);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.JOB_NAME, RJobList.ALL);
               f.refreshContents();
            }
            else
               failed("Re-set Selection value not returned.");
        succeeded();
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }


/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var085()  
  {
        Object SelectionValue,SelectionValue1;
        ResourceMetaData SelectionData;
        RJob  job;
        String values = JobNumber_;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSelectionValue(RJobList.JOB_NUMBER,values);
            f.refreshContents();
            if (f.getListLength() > 0)
              job  = (RJob) f.resourceAt(0);
            else 
              job  = null;
            if (job  != null)
            {
               if (!job.getAttributeValue(RJob.JOB_NUMBER).equals(values))
               {
                  failed("Bad attribute value returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.JOB_NUMBER);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.JOB_NUMBER, RJobList.ALL);
               f.refreshContents();
            }
            else
               failed("Re-set Selection value not returned.");
        succeeded();
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }

  }

/**
getSelectionValue(Object AttributeID) -- Test valid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var086()  
  {
        Object SelectionValue,SelectionValue1;
        ResourceMetaData SelectionData;
        RJob  job ;
        String values = userName_;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSelectionValue(RJob.USER_NAME,values);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() > 0)
              job  = (RJob) f.resourceAt(0);
            else 
              job  = null;
            if (job  != null)
            {
               if (!job.getAttributeValue(RJob.USER_NAME).equals(values))
               {
                  failed("Bad attribute value returned for RJob.");
                  return;
               }
            }
            SelectionValue = f.getSelectionValue(RJobList.USER_NAME);
            if (SelectionValue.equals(values))
            {
               // Reset value back to default
               f.setSelectionValue(RJobList.USER_NAME, null);
               f.refreshContents();
            }
            else
               failed("Re-set Selection value not returned.");
        succeeded();
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
  }

/**
refreshStatus() - Valid refresh with default constructor.
**/
  public void Var087()
  {
    try
    {
        RJobList f = new RJobList ();
        f.setSystem(systemObject_);
        f.refreshStatus();
        succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
refreshStatus() - Valid refresh with constructor RJobList(AS400)
**/
  public void Var088 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.refreshStatus();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
refreshStatus() - Valid refresh with constructor RJobList(AS400, String)
**/
  public void Var089 ()
    {
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.refreshStatus();
            f.close();
            succeeded();
        }
        catch (Exception e) {
            failed( e, "Unexpected exception.");
        }
    }

/**
refreshStatus() -- Test that refreshing status updates the list length and whether or not the
    list is complete.
**/
  public void Var090()
  {
    long initialLength,postLength;
    int i;
    try
    {
        RJobList f = new RJobList (systemObject_);
        f.open();
        initialLength = f.getListLength();
        f.close();
        RJobList g = new RJobList(systemObject_);
        g.open();
        initialLength = g.getListLength();
        postLength = initialLength;
        while (!g.isComplete())
        {
           Thread.sleep(2000);
           g.refreshStatus();
           postLength = g.getListLength();
        }
        if (initialLength == postLength) 
           failed("List length not updated after refreshStatus");
        else
           succeeded();
        g.close();

    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }

/**
refreshStatus() - Invalid use.  Change system and then refresh.  A ResourceException
  should be thrown.
**/
  public void Var091 ()
    {
        try {
            AS400 newSys_ = new AS400("RCHAS1DD","JAVA","JTEAM1");
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.setSystem(newSys_);
            f.refreshStatus();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

/**
resourceAt() - Valid use. Check first resource in RJob list

**/
  public void Var092 ()
    {
       RJob job;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            if (f.isResourceAvailable(0) )
            {
              job = (RJob) f.resourceAt(0);
              if (job  != null)
                 succeeded();
               else
                 failed("Null RJob returned.");
            }
            else
               failed("Resource 0 not available.");
        }
        catch (Exception e) {
            failed( e, "Unexpected exception.");
        }
    }

/**
resourceAt() - Invalid use. Specify a negative index.  An IllegalArgumentException
  should be thrown

**/
  public void Var093 ()
    {
       RJob job ;
        try {
            RJobList f = new RJobList (systemObject_);
            f.resourceAt(-1);
            failed("Exception not thrown.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf( e, "java.lang.IllegalArgumentException");
        }
    }


/**
resourceAt() - Invalid use. Specify too large of an index.  An IllegalArgumentException
  should be thrown

**/
  public void Var094 ()
    {
       RJob job ;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            long listLength = f.getListLength();
            f.resourceAt(listLength + 100);
            failed("Exception not thrown.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf( e, "java.lang.IllegalArgumentException");
        }
    }

/**
resourceAt() - Valid use.  Try to retrieve a resource that is not loaded yet.  NULL should
   be returned.
**/
  public void Var095()
    {
        int i;

        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            long listLength = f.getListLength();
            f.close();
            RJobList g = new RJobList(systemObject_);
            g.open();
            if (!g.isComplete())
            {
               if  (!g.isResourceAvailable(listLength -1))
               {
                  if (g.resourceAt(listLength-1) != null)
                     failed("Non-null resource returned.");
                  else
                     succeeded();
               } else
                  succeeded();
            } else
               succeeded();
            g.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
  }


/**
resourceAt() - Valid use.  Check each resource to ensure non null
**/
  public void Var096()
    {
        int i;
        RJob job ;
        boolean OK = true;

        try {
            RJobList f = new RJobList (systemObject_);
            f.setSelectionValue(RJobList.JOB_TYPE, RJob.JOB_TYPE_INTERACTIVE);
            f.open();
            f.waitForComplete();
            for (i=0;i< f.getListLength() && OK == true ; ++i)
            {
                if (f.isResourceAvailable(i))
                {
                   job  = (RJob) f.resourceAt(i);
                   if (job  == null)
                   {
                      failed("Resource at " + i + " is null.");
                      OK =false;
                   }
                 }
                 else 
                 {
                   failed("Resource at " + i + " is not available.");
                   OK = false;
                 }
            }
            if (OK)
              succeeded();
            f.close();
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }

  }


/**
setSelectionValue(Object AttributeID) -- Test invalid use; pass null for attribute ID.
**/
  public void Var097 ()
    {
        Object SelectionValue;
        try {
            RJobList f = new RJobList (systemObject_);
            f.setSelectionValue(null, RJobList.JOB_NAME);
            failed("No exception thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test valid use; pass null for value and ensure reset
**/
  public void Var098()
    {
        Object SelectionValue;
        RJob job ;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            f.setSelectionValue(RJobList.JOB_NAME, "QBATCH");
            f.open();
            long listLength = f.getListLength();
            for (i=0; i< f.getListLength(); ++ i)
            {        
               job  = (RJob) f.resourceAt(i);
               if (!( (String) job.getAttributeValue(RJob.JOB_NAME)).equals("QBATCH") )
               {
                  failed("Bad RJob name.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.JOB_NAME, null);
             f.refreshContents();
             if (f.getListLength() <= listLength)
             {
                failed("Bad list length returned.");
                return;
             }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }

    }


/**
setSelectionValue(Object attributeID) -- Test invalid use; call with bad value for value
**/
  public void Var099 ()
    {
        try {
            RJobList f = new RJobList ();
            int badNum = 10;
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES,"THIS IS BAD");
            failed("No exception thrown.");
   
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test invalid use; pass bad value for attribute ID.
**/
  public void Var100()
    {
        try {
            RJobList f = new RJobList ();
            f.setSelectionValue("BADID", "BADVALUE");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e,"com.ibm.as400.access.ExtendedIllegalArgumentException");

        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for JOB_NAME and then
  open list. Ensure correct Selectioning done.
**/
  public void Var101()
    {
       Object SelectionValue;
       int i;
       RJob job;
       try {
            RJobList f = new RJobList(systemObject_);
            f.open();
            f.waitForComplete();
            f.setSelectionValue(RJobList.JOB_NAME, RJobList.CURRENT);
            f.refreshContents();
            f.waitForComplete();
            long listLength = f.getListLength();
            for (i=0; i< f.getListLength(); ++ i)
            {        
               job  = (RJob) f.resourceAt(i);
               if (!job.getAttributeValue(RJob.JOB_NAME).equals("QZRCSRVS") )
               {
                  failed("Bad RJob name.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.JOB_NAME, null);
             f.refreshContents();
             f.waitForComplete();
             if (f.getListLength() <= listLength)
             {
                failed("Bad list length returned.");
                return;
             }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for RJob name and then
  open list. Ensure correct Selectioning done.
**/
  public void Var102()
    {
       Object SelectionValue;
       int i;
       RJob job;
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.JOB_NAME, "*");
            f.open();
            long listLength = f.getListLength();
            if (listLength != 1)
            {
               failed("Bad list length returned.");
               return;
            }
            job  = (RJob) f.resourceAt(0);          
            if (! ( (String) job.getAttributeValue(RJob.JOB_NAME)).equals("QZRCSRVS") )
            {
               failed("Bad RJob name.");
               return;
            }
            f.setSelectionValue(RJobList.JOB_NAME, null);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() <= listLength)
            {
               failed("Bad list length returned.");
               return;
            }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for JOB_NAME and then
  open list. Ensure correct Selectioning done.
**/
  public void Var103()
    {
       Object SelectionValue;
       int i;
       RJob RJob1,RJob2;
       try {
            RJobList f = new RJobList(systemObject_);
            f.open();
            f.waitForComplete();
            f.setSelectionValue(RJobList.JOB_NAME, RJobList.ALL);
            f.refreshContents();
            f.waitForComplete();
            RJobList g = new RJobList(systemObject_);
            g.open();
            g.waitForComplete();
            long listLength1 = f.getListLength();
            long listLength2 = g.getListLength();
            if (listLength1 != listLength2)
            {
               failed("Bad list length returned.");
               return;
            }
            for (i=0; i< f.getListLength(); ++ i)
            {        
               RJob1 = (RJob) f.resourceAt(i);
               RJob2 = (RJob) g.resourceAt(i);
               if (!RJob1.getAttributeValue(RJob.JOB_NAME).equals(RJob2.getAttributeValue(RJob.JOB_NAME)) ||
                   !RJob1.getAttributeValue(RJob.JOB_NUMBER).equals(RJob2.getAttributeValue(RJob.JOB_NUMBER)) ||
                   !RJob1.getAttributeValue(RJob.USER_NAME).equals(RJob2.getAttributeValue(RJob.USER_NAME)) )
               {
                  failed("Bad RJob attribute.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.JOB_NAME, null);
             f.refreshContents();
             f.waitForComplete();
             if ( (f.getListLength()-25) < listLength1 && 
                  (f.getListLength() + 25 > listLength1) )
                succeeded();
             else
                failed("Bad list length returned.");
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for RJob number and then
  open list. Ensure correct Selectioning done.
**/
  public void Var104()
    {
       Object SelectionValue;
       int i;
       RJob job;
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.JOB_NUMBER, batchNumber_);
            f.open();
            long listLength = f.getListLength();
            if (listLength != 1)
            {
               failed("Bad list length returned.");
               return;
            }
            job  = (RJob) f.resourceAt(0);          
            if (!((String)job.getAttributeValue(RJob.JOB_NUMBER)).equals(batchNumber_) )
            {
               failed("Bad RJob number.");
               return;
            }
            f.setSelectionValue(RJobList.JOB_NUMBER, null);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() <= listLength)
            {
               failed("Bad list length returned.");
               return;
            }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for JOB_NUMBER and then
  open list. Ensure correct Selectioning done.
**/
  public void Var105()
    {
       Object SelectionValue;
       int i;
       RJob RJob1,RJob2;
       try {
            RJobList f = new RJobList(systemObject_);
            f.open();
            f.waitForComplete();
            f.setSelectionValue(RJobList.JOB_NUMBER, RJobList.ALL);
            f.refreshContents();
            f.waitForComplete();
            RJobList g = new RJobList(systemObject_);
            g.open();
            g.waitForComplete();
            long listLength1 = f.getListLength();
            long listLength2 = g.getListLength();
            if (listLength1 != listLength2)
            {
               failed("Bad list length returned.");
               return;
            }
            for (i=0; i< f.getListLength(); ++ i)
            {        
               RJob1 = (RJob) f.resourceAt(i);
               RJob2 = (RJob) g.resourceAt(i);
               if (!RJob1.getAttributeValue(RJob.JOB_NAME).equals(RJob2.getAttributeValue(RJob.JOB_NAME)) ||
                   !RJob1.getAttributeValue(RJob.JOB_NUMBER).equals(RJob2.getAttributeValue(RJob.JOB_NUMBER)) ||
                   !RJob1.getAttributeValue(RJob.USER_NAME).equals(RJob2.getAttributeValue(RJob.USER_NAME)) )
               {
                  failed("Bad RJob attribute.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.JOB_NUMBER, null);
             f.refreshContents();
             f.waitForComplete();
             if ( (f.getListLength()-25) < listLength1 && 
                  (f.getListLength() + 25 > listLength1) )
                succeeded();
            else
                failed("Bad list length returned (second).");
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for RJob TYPE and then
  open list. Ensure correct Selectioning done.
**/
  public void Var106()
    {
       Object SelectionValue;
       int i;
       RJob job;
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.JOB_TYPE, RJob.JOB_TYPE_BATCH);
            f.open();
            long listLength = f.getListLength();
            job = (RJob) f.resourceAt(0);          
            if (!job.getAttributeValue(RJob.JOB_TYPE).equals(RJob.JOB_TYPE_BATCH) )
            {
               failed("Bad RJob type.");
               return;
            }
            f.setSelectionValue(RJobList.JOB_TYPE, null);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() <= listLength)
            {
               failed("Bad list length returned.");
               return;
            }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for JOB_TYPE and then
  open list. Ensure correct Selectioning done.
**/
  public void Var107()
    {
       Object SelectionValue;
       int i;
       RJob RJob1,RJob2;
       try {
            RJobList f = new RJobList(systemObject_);
            f.open();
            f.waitForComplete();
            f.setSelectionValue(RJobList.JOB_TYPE, RJobList.ALL);
            f.refreshContents();
            f.waitForComplete();
            RJobList g = new RJobList(systemObject_);
            g.open();
            g.waitForComplete();
            long listLength1 = f.getListLength();
            long listLength2 = g.getListLength();
            if (listLength1 != listLength2)
            {
               failed("Bad list length returned.");
               return;
            }
            for (i=0; i< f.getListLength(); ++ i)
            {        
               RJob1 = (RJob) f.resourceAt(i);
               RJob2 = (RJob) g.resourceAt(i);
               if (!RJob1.getAttributeValue(RJob.JOB_NAME).equals(RJob2.getAttributeValue(RJob.JOB_NAME)) ||
                   !RJob1.getAttributeValue(RJob.JOB_NUMBER).equals(RJob2.getAttributeValue(RJob.JOB_NUMBER)) ||
                   !RJob1.getAttributeValue(RJob.USER_NAME).equals(RJob2.getAttributeValue(RJob.USER_NAME)) )
               {
                  failed("Bad RJob attribute.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.JOB_TYPE, null);
             f.refreshContents();
             f.waitForComplete();
             if ( (f.getListLength()-25) < listLength1 && 
                  (f.getListLength() + 25 > listLength1) )
                succeeded();
            else
                failed("Bad list length returned (second).");
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test valid use; ensure correct selectioning done
**/
  public void Var108()
    {
       Object SelectionValue;
       int i;
       RJob job;
       try {
            RJobList f = new RJobList(systemObject_);
            f.open();
            f.waitForComplete();
            long totalLength = f.getListLength();
            f.setSelectionValue(RJobList.JOB_NUMBER, batchNumber_);
            f.refreshContents();
            job  = (RJob) f.resourceAt(0);
            long listLength = f.getListLength();
            if (listLength != 1)
            {
               failed("Bad list length returned.");
               return;
            }
            f.setSelectionValue(RJobList.USER_NAME, null);
            f.refreshContents();
            f.waitForComplete();
            listLength = f.getListLength();
            job  = (RJob) f.resourceAt(0);
            if (listLength == 1 && ((String) job.getAttributeValue(RJob.JOB_NUMBER)).equals(batchNumber_))
               succeeded();
            else
               failed("Bad reset object returned.");
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for RJob TYPE and then
  open list. Ensure correct Selectioning done.
**/
  public void Var109()
    {
       Object SelectionValue;
       int i;
       RJob job ;
       String[] values = { RJob.JOB_STATUS_ACTIVE };
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES, values);
            f.open();
            f.waitForComplete();
            long listLength = f.getListLength();
            job  = (RJob) f.resourceAt(0);          
            if (!job.getAttributeValue(RJob.JOB_STATUS).equals(RJob.JOB_STATUS_ACTIVE) )
            {
               failed("Bad RJob type.");
               return;
            }
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES, null);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() <= listLength)
            {
               failed("Bad list length returned.");
               return;
            }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for PRIMARY_JOB_STATUSES and then
  open list. Ensure correct Selectioning done.
**/
  public void Var110()
    {
       Object SelectionValue;
       int i;
       RJob job;
       String[] values = { RJob.JOB_STATUS_ACTIVE, RJob.JOB_STATUS_JOBQ };
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES, values);
            f.open();
            f.waitForComplete();
            long listLength = f.getListLength();
            job  = (RJob) f.resourceAt(0);          
            if (!job.getAttributeValue(RJob.JOB_STATUS).equals(RJob.JOB_STATUS_ACTIVE) &&
                !job.getAttributeValue(RJob.JOB_STATUS).equals(RJob.JOB_STATUS_JOBQ) )
            {
               failed("Bad RJob status.");
               return;
            }
            f.setSelectionValue(RJobList.PRIMARY_JOB_STATUSES, null);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() <= listLength)
            {
               failed("Bad list length returned.");
               return;
            }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for RJob number and then
  open list. Ensure correct Selectioning done.
**/
  public void Var111()
    {
       Object SelectionValue;
       int i;
       RJob job ;
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.USER_NAME, userName_);
            f.open();
            long listLength = f.getListLength();
            job  = (RJob) f.resourceAt(0);          
            if (!job.getAttributeValue(RJob.USER_NAME).equals(userName_) )
            {
               failed("Bad user name.");
               return;
            }
            f.setSelectionValue(RJobList.USER_NAME, null);
            f.refreshContents();
            f.waitForComplete();
            if (f.getListLength() <= listLength)
            {
               failed("Bad list length returned.");
               return;
            }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for JOB_NUMBER and then
  open list. Ensure correct Selectioning done.
**/
  public void Var112()
    {
       Object SelectionValue;
       int i;
       RJob RJob1,RJob2;
       try {
            RJobList f = new RJobList(systemObject_);
            f.setSelectionValue(RJobList.USER_NAME, RJobList.ALL);
            f.open();
            f.waitForComplete();
            RJobList g = new RJobList(systemObject_);
            g.open();
            g.waitForComplete();
            long listLength1 = f.getListLength();
            long listLength2 = g.getListLength();
            if (listLength1 != listLength2)
            {
               failed("Bad list length returned.");
               return;
            }
            for (i=0; i< f.getListLength(); ++ i)
            {        
               RJob1 = (RJob) f.resourceAt(i);
               RJob2 = (RJob) g.resourceAt(i);
               if (!RJob1.getAttributeValue(RJob.JOB_NAME).equals(RJob2.getAttributeValue(RJob.JOB_NAME)) ||
                   !RJob1.getAttributeValue(RJob.JOB_NUMBER).equals(RJob2.getAttributeValue(RJob.JOB_NUMBER)) ||
                   !RJob1.getAttributeValue(RJob.USER_NAME).equals(RJob2.getAttributeValue(RJob.USER_NAME)) )
               {
                  failed("Bad RJob attribute.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.USER_NAME, null);
             f.refreshContents();
             f.waitForComplete();
             RJobList h = new RJobList(systemObject_);
             h.open();
             h.waitForComplete();
             if (f.getListLength() != h.getListLength())
             {
                failed("Bad list length returned.");
                return;
             }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }


/**
setSelectionValue(Object AttributeID) -- Test valid use; set value for JOB_NAME and then
  open list. Ensure correct Selectioning done.
**/
  public void Var113()
    {
       Object SelectionValue;
       int i;
       RJob job ;
       try {
            RJobList f = new RJobList(systemObject_);
            f.open();
            f.waitForComplete();
            f.setSelectionValue(RJobList.USER_NAME, RJobList.CURRENT);
            f.refreshContents();
            f.waitForComplete();
            long listLength = f.getListLength();
            for (i=0; i< f.getListLength(); ++ i)
            {        
               job  = (RJob) f.resourceAt(i);
               if (!job.getAttributeValue(RJob.USER_NAME).equals("JAVA") )
               {
                  failed("Bad user name.");
                  return;
                }
             } 
             f.setSelectionValue(RJobList.USER_NAME, null);
             f.refreshContents();
             f.waitForComplete();
             if (f.getListLength() <= listLength)
             {
                failed("Bad list length returned.");
                return;
             }
            succeeded();
            f.close();
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
        }
    }

/**
toString() -- 
 
**/
  public void Var114() 
    {
        int i;
        String toStr;

        try {
            RJobList f = new RJobList (systemObject_);
            toStr = f.toString();
            if (toStr.equals("Job List") )
              succeeded();
            else
              failed("Bad toString returned.");
        }
        catch (Exception e) {
            failed(e,"Unexpected exception.");
       }
  }


/**
resourceAt() - Valid resourceAt() without an open().
**/
  public void Var115()
  {
    try
    {
        RJobList f = new RJobList (systemObject_);
        f.resourceAt(0);
        succeeded();
    }
    catch (Exception e) {
      failed (e, "Unexpected Exception");
    }
  }



}
