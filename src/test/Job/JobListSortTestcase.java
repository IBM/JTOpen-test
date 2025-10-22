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
import test.*;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.resource.RJobList;
import com.ibm.as400.resource.RJob;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.access.AS400Exception;
import java.io.CharConversionException;
import java.io.UnsupportedEncodingException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.awt.TextArea;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;



/**
Testcase RJobListSortTestcase.

<p>This tests the following BufferedResourceList methods:
<ul>
<li>getSortMetaData()
<li>getSortMetaData(Object)
<li>getSortOrder()
<li>getSortValue()
<li>setSortOrder()
<li>setSortValue()
</ul>
**/
@SuppressWarnings({"deprecation","unused","resource","rawtypes"})
public class JobListSortTestcase
extends Testcase
{

    // Private data.

    private String userName_ ;
    private String jobName_;
    private String jobNumber_;
    private String batchNumber_;
    private String interactiveUser = "TSTJOBUSR1";

  private void inputParam( String interactiveUser )
  {
    try
    {
        CommandCall ccallPow_ = new CommandCall( pwrSys_ );
        RJobList jobList = new RJobList( pwrSys_ );
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
               jobName_ = (String) job.getAttributeValue(RJob.JOB_NAME);
               jobNumber_ = (String) job.getAttributeValue(RJob.JOB_NUMBER);
           }
           // Get a batch job for variation 90
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
               output_.println(job.getAttributeValue(RJob.USER_NAME)+","+job.getAttributeValue(RJob.JOB_NAME)+","+job.getAttributeValue(RJob.JOB_NUMBER)+" is not a signed-on job.");
             }
           }
           catch(Exception x)
           {
              output_.println(job.getAttributeValue(RJob.USER_NAME)+","+job.getAttributeValue(RJob.JOB_NAME)+","+job.getAttributeValue(RJob.JOB_NUMBER)+" has ended.");
           }
        }
        if (batchNumber_ == null)
        {
          output_.println("Note: No QBATCH job was found, some variations may fail.");
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

    if (jobName_ == null || userName_ == null || jobNumber_ == null)
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
    static boolean verifySortMetaData(ResourceMetaData smd, 
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
    static boolean verifySortMetaData(ResourceMetaData[] smd, 
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
            System.out.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifySortMetaData(smd[found], 
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
getSortMetaData() with 0 parameters - Verify that the array contains 5 sorts
**/
    public void Var001()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSortMetaData();
            assertCondition(smd.length == 5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSortMetaData() with 1 parameter - Pass null.
**/
    public void Var002()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getSortMetaData() with 1 parameter - Pass an invalid attribute ID.
**/
    public void Var003()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(new Date());
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSortOrder() - Pass null.
**/
    public void Var004()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.getSortOrder(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getSortOrder() - Pass an invalid sort ID.
**/
    public void Var005()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.getSortOrder("Yo");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSortValue() - Try it.
**/
    public void Var006()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            Object[] sortValue = u.getSortValue();
            assertCondition(sortValue.length == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSortOrder() - Pass null for the sort ID.
**/
    public void Var007()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.setSortOrder(null, true);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSortOrder() - Pass an invalid sort ID.
**/
    public void Var008()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.setSortOrder(u, false);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSortValue() - Pass null.
**/
    public void Var009()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.setSortValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSortOrder() - Pass an invalid sort ID.
**/
    public void Var010()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.setSortValue(new Object[] { "Yo" });
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSortOrder() - Pass an empty array.
**/
    public void Var011()
    {
        try {
            RJobList u = new RJobList(pwrSys_);
            u.setSortValue(new Object[0]);
            u.open();
            u.waitForComplete();
            long length = u.getListLength();
            for(int i = 0; i < length; ++i)
                u.resourceAt(i);
            assertCondition(length > 20);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData(Object attributeID) -- call with default job list constructor
**/
  public void Var012 ()
    {
        try {
            ResourceMetaData sortData;
            RJobList f = new RJobList ();
            f.setSystem(systemObject_);
            sortData = f.getSortMetaData(RJob.JOB_NAME);
            succeeded();
   
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
getSortMetaData(Object attributeID) -- call with default job list constructor
**/
  public void Var013 ()
    {
        try {
            ResourceMetaData sortData;
            RJobList f = new RJobList (systemObject_);
            sortData = f.getSortMetaData(RJob.USER_NAME);
            succeeded();
   
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
getSortMetaData() with 1 parameter - Pass JOB_NAME
**/
    public void Var014()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(RJob.JOB_NAME);
            assertCondition(verifySortMetaData(smd, RJob.JOB_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
 
/**
getSortMetaData() with 1 parameter - Pass USER_NAME
**/
    public void Var015()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(RJob.USER_NAME);
            assertCondition(verifySortMetaData(smd, RJob.USER_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData() with 1 parameter - Pass JOB_NUMBER
**/
    public void Var016()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(RJob.JOB_NUMBER);
            assertCondition(verifySortMetaData(smd, RJob.JOB_NUMBER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData() with 1 parameter - Pass JOB_TYPE
**/
    public void Var017()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(RJob.JOB_TYPE);
            assertCondition(verifySortMetaData(smd, RJob.JOB_TYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData() with 1 parameter - Pass JOB_SUBTYPE
**/
    public void Var018()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData smd = u.getSortMetaData(RJob.JOB_SUBTYPE);
            assertCondition(verifySortMetaData(smd, RJob.JOB_SUBTYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSelectionMetaData() with 1 parameter - Try each of them.
**/
    public void Var019()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSortMetaData();
            boolean success = true;
            for(int i = 0; i < smd.length; ++i) {
                boolean thisOne = verifySortMetaData(u.getSortMetaData(smd[i].getID()), 
                                                             smd[i].getID(), 
                                                             smd[i].getType(), 
                                                             smd[i].isReadOnly(), 
                                                             smd[i].getPossibleValues().length, 
                                                             smd[i].getDefaultValue(), 
                                                             smd[i].isValueLimited(),
                                                             smd[i].areMultipleAllowed());
                if (!thisOne) {
                    System.out.println("Comparison failed for: " + smd[i] + ".");
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
getSortMetaData() with 0 parameter 
**/
    public void Var020()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSortMetaData();
          assertCondition(verifySortMetaData(smd, RJob.JOB_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
 
/**
getSortMetaData() with 0 parms
**/
    public void Var021()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSortMetaData();
            assertCondition(verifySortMetaData(smd, RJob.USER_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData() with 1 parameter - Pass JOB_NUMBER
**/
    public void Var022()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSortMetaData();
            assertCondition(verifySortMetaData(smd, RJob.JOB_NUMBER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData() with 0 parameter
**/
    public void Var023()
    {
        try {
            RJobList u = new RJobList();
            ResourceMetaData[] smd = u.getSortMetaData();
          assertCondition(verifySortMetaData(smd, RJob.JOB_TYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortMetaData() with 0 parameter 
**/
    public void Var024()
    {
        try {
          RJobList u = new RJobList();
          ResourceMetaData smd[] = u.getSortMetaData();
          assertCondition(verifySortMetaData(smd, RJob.JOB_SUBTYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/****
  compareStrings -- Function to compare two strings
******/

    public int compareStrings(String firstUniStr, String secondUniStr) 
    {
          byte[] firstByteA = new byte[100];
          byte[] secondByteA = new byte[100];
          int result = 0;
          StringCharacterIterator firstIter = new StringCharacterIterator(firstUniStr);
          StringCharacterIterator secondIter = new StringCharacterIterator(secondUniStr);
          int fLength = firstUniStr.length();
          int sLength = secondUniStr.length();
          int index = 0;
          boolean done = false;
          if (fLength == 0 && sLength > 0)
          {
            result = -1;
          }
          else if (sLength == 0 && fLength > 0)
          {
            result = 1;
          }
          else if (fLength >= sLength)
          {
            char d;
            d = secondIter.first();
            for (char c = firstIter.first(); (c != CharacterIterator.DONE && !done); c = firstIter.next()) 
            {
               ++index;
              if ( Character.isDigit(c)  && !Character.isDigit(d)  )
              {
                 result = 1;
                 done = true;
              } else if ( Character.isDigit(d)  && !Character.isDigit(c) )
              {
                 result = -1;
                 done = true;
              } else if (d < c)
              {
                 result = 1;
                 done = true;
              } else if (d > c)
              {
                 result = -1;
                 done = true;
              } else if ( (d==c) && index == sLength && sLength != fLength)
              {
                 result = 1;
                 done = true;
              }
              d = secondIter.next();
            }
          } else if (sLength > fLength)
          {
            char f;
            f = firstIter.first();
            for (char s = secondIter.first(); s != CharacterIterator.DONE && !done; s = secondIter.next()) 
            {
              ++index;
              Character sChar = new Character(s);
              Character fChar = new Character(f);
              if ( Character.isDigit(f)  && !Character.isDigit(s)  )
              {
                 result = 1;
                 done = true;
              } else if ( Character.isDigit(s)  && !Character.isDigit(f)  )
              {
                 result = -1;
                 done = true;
              } else if (s < f)
              {
                 result = 1;
                 done = true;
              } else if (s > f)
              {
                 result = -1;
                 done = true;
              } else if ( (s==f) && index == fLength)
              {
                 result = -1;
                 done = true;
              }
              f = firstIter.next();
            }
          }
        return result;
      }



/**
getSortValue(Object attributeID) -- call with default RJobList constructor
**/
  public void Var025 ()
    {
        try {
            Object sortValue;
            RJobList f = new RJobList ();
            sortValue = f.getSortValue();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected exception.");
        }
    }

/**
getSortValue() - When the connection is bogus.
**/
    public void Var026()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RJobList u = new RJobList(system);
            Object sortValue = u.getSortValue();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getSortValue(Object AttributeID) -- Test valid use; getSortValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var027()
    {
        Object[] sortValues = new Object[] { RJob.JOB_NAME };
        ResourceMetaData SortData;
        RJob job;
        RJob job1,job2;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            // Set value and then re-get to check
            long listLength = f.getListLength();
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            long sortLength = f.getListLength();
            if ( (listLength-10) > sortLength || (listLength +10) < sortLength)
            {
               failed("Sort length not close to pre-sorted length.");
               return;
            }
            if (f.getSortValue().equals(sortValues))
               succeeded();
            else
               failed("Bad sort value returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSortValue(Object AttributeID) -- Test valid use; getSortValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var028()
    {
        Object[] sortValues = new Object[] { RJob.USER_NAME, RJob.JOB_NAME };
        ResourceMetaData SortData;
        RJob job;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            // Set value and then re-get to check
            long listLength = f.getListLength();
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            long sortLength = f.getListLength();
            if ((listLength-10) > sortLength || (listLength +10) < sortLength)
            {
               failed("Sort length not equal to pre-sorted length.");
               return;
            }
            if (f.getSortValue().equals(sortValues))
               succeeded();
            else
               failed("Bad sort value returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSortValue(Object AttributeID) -- Test valid use; getSortValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var029()
    {
        Object[] sortValues = new Object[] { RJob.USER_NAME, RJob.JOB_NAME, RJob.JOB_TYPE };
        ResourceMetaData SortData;
        RJob job;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            // Set value and then re-get to check
            long listLength = f.getListLength();
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            long sortLength = f.getListLength();
            if ((listLength-10) > sortLength || (listLength +10) < sortLength)
            {
               failed("Sort length not equal to pre-sorted length.");
               return;
            }
            if (f.getSortValue().equals(sortValues))
               succeeded();
            else
               failed("Bad sort value returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSortValue(Object AttributeID) -- Test valid use; getSortValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var030()
    {
        Object[] sortValues = new Object[] { RJob.USER_NAME, RJob.JOB_NAME, RJob.JOB_TYPE, RJob.JOB_NUMBER };
        ResourceMetaData SortData;
        RJob job;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            // Set value and then re-get to check
            long listLength = f.getListLength();
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            long sortLength = f.getListLength();
            if ((listLength-10) > sortLength || (listLength +10) < sortLength)
            {
               failed("Sort length not equal to pre-sorted length.");
               return;
            }
            if (f.getSortValue().equals(sortValues))
               succeeded();
            else
               failed("Bad sort value returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSortValue(Object AttributeID) -- Test valid use; getSortValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var031()
    {
        Object[] sortValues = new Object[] { RJob.USER_NAME, RJob.JOB_NAME, RJob.JOB_TYPE, RJob.JOB_NUMBER, RJob.JOB_SUBTYPE };
        ResourceMetaData SortData;
        RJob job;
        try {
            RJobList f = new RJobList (systemObject_);
            f.open();
            f.waitForComplete();
            // Set value and then re-get to check
            long listLength = f.getListLength();
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            long sortLength = f.getListLength();
            if ((listLength-10) > sortLength || (listLength +10) < sortLength)
            {
               failed("Sort length not equal to pre-sorted length.");
               return;
            }
            if (f.getSortValue().equals(sortValues))
               succeeded();
            else
               failed("Bad sort value returned.");
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
getSelectionValue(Object AttributeID) -- Test invalid use; getSelectionValue, reset the value and then get again
    and ensure the changed value is returned.
**/
  public void Var032()
    {
        Object SelectionValue;
        ResourceMetaData SelectionData;
        Object[] sortValues = new Object[] { "0122D" };
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            failed("Exception not thrown.");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

/**
setSortValue(Object AttributeID) -- Test valid use
**/
  public void Var033()
    {
        Object[] sortValues = new Object[] { RJob.JOB_NAME };
        RJob job1, job2;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            for (i=1;i<f.getListLength()-1; ++i)
            {
               job1 = (RJob) f.resourceAt(i);
               job2 = (RJob) f.resourceAt(i-1);
               String attr2 = (String) job1.getAttributeValue(RJob.JOB_NAME);
               String attr1 = (String) job2.getAttributeValue(RJob.JOB_NAME);
               if (compareStrings(attr1,attr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }
            }
        succeeded();             
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
setSortValue(Object AttributeID) -- Test valid use
**/
  public void Var034()
    {
        Object[] sortValues = new Object[] { RJob.JOB_NUMBER };
        RJob job1, job2;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            for (i=1;i<f.getListLength()-1; ++i)
            {
               job1 = (RJob) f.resourceAt(i);
               job2 = (RJob) f.resourceAt(i-1);
               String attr2 = (String) job1.getAttributeValue(RJob.JOB_NUMBER);
               String attr1 = (String) job2.getAttributeValue(RJob.JOB_NUMBER);
               if (compareStrings(attr1,attr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }
            }
        succeeded();             
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
setSortValue(Object AttributeID) -- Test valid use
**/
  public void Var035()
    {
        Object[] sortValues = new Object[] { RJob.JOB_TYPE };
        RJob job1, job2;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            for (i=1;i<f.getListLength()-1; ++i)
            {
               job1 = (RJob) f.resourceAt(i);
               job2 = (RJob) f.resourceAt(i-1);
               String attr2 = (String) job1.getAttributeValue(RJob.JOB_TYPE);
               String attr1 = (String) job2.getAttributeValue(RJob.JOB_TYPE);
               if (compareStrings(attr1,attr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }
            }
        succeeded();             
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
setSortValue(Object AttributeID) -- Test valid use
**/
  public void Var036()
    {
        Object[] sortValues = new Object[] { RJob.JOB_SUBTYPE };
        RJob job1, job2;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            for (i=1;i<f.getListLength()-1; ++i)
            {
               job1 = (RJob) f.resourceAt(i);
               job2 = (RJob) f.resourceAt(i-1);
               String attr2 = (String) job1.getAttributeValue(RJob.JOB_SUBTYPE);
               String attr1 = (String) job2.getAttributeValue(RJob.JOB_SUBTYPE);
System.out.println("attr1 =" + attr1 + "..");
System.out.println("attr2 =" + attr2 + "..");
               if (compareStrings(attr1,attr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }
            }
        succeeded();             
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
setSortValue(Object AttributeID) -- Test valid use
**/
  public void Var037()
    {
        Object[] sortValues = new Object[] { RJob.USER_NAME };
        RJob job1, job2;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            for (i=1;i<f.getListLength()-1; ++i)
            {
               job1 = (RJob) f.resourceAt(i);
               job2 = (RJob) f.resourceAt(i-1);
               String attr2 = (String) job1.getAttributeValue(RJob.USER_NAME);
               String attr1 = (String) job2.getAttributeValue(RJob.USER_NAME);
               if (compareStrings(attr1,attr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }
            }
        succeeded();             
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }

/**
setSortValue(Object AttributeID) -- Test valid use
**/
  public void Var038()
    {
        Object[] sortValues = new Object [] { RJob.JOB_TYPE, RJob.JOB_NAME };
        RJob job1, job2;
        int i;
        try {
            RJobList f = new RJobList (systemObject_);
            // Set value and then re-get to check
            f.setSortValue(sortValues);
            f.refreshContents();
            f.waitForComplete();
            for (i=1;i<f.getListLength()-1; ++i)
            {
               job1 = (RJob) f.resourceAt(i);
               job2 = (RJob) f.resourceAt(i-1);
               String typeAttr2 = (String) job1.getAttributeValue(RJob.JOB_TYPE);
               String typeAttr1 = (String) job2.getAttributeValue(RJob.JOB_TYPE);
               String nameAttr2 = (String) job1.getAttributeValue(RJob.JOB_NAME);
               String nameAttr1 = (String) job2.getAttributeValue(RJob.JOB_NAME);
               if (compareStrings(typeAttr1,typeAttr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }
               if (compareStrings(typeAttr1,typeAttr2)==0 && compareStrings(nameAttr1,nameAttr2) > 0)
               {
                 failed("Sort order bad.");
                 return;
               }  
            }
        succeeded();             
        }
        catch (Exception e) {
            failed(e, "Unknown exception.");
        }
    }



}

