///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserListSelectionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.RUser;
import com.ibm.as400.resource.RUserList;

/**
 Testcase UserListSelectionTestcase.  This tests the following methods of the RUserList class, some inherited from BufferedResourceList:
 <ul>
 <li>getSelectionMetaData()
 <li>getSelectionMetaData()
 <li>getSelectionValue()
 <li>setSelectionValue()
 </ul>
 **/
public class UserListSelectionTestcase extends Testcase
{
    private UserSandbox sandbox_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "ULST");
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    /**
     Checks a particular selection meta data.
     **/
    static boolean verifySelectionMetaData(ResourceMetaData smd, Object attributeID, Class attributeType, boolean readOnly,  int possibleValueCount, Object defaultValue, boolean valueLimited, boolean multipleAllowed)
    {
        return smd.areMultipleAllowed() == multipleAllowed && smd.getDefaultValue() == (defaultValue) && smd.getPossibleValues().length == possibleValueCount && smd.getPresentation() != null && smd.getType() == attributeType && smd.isReadOnly() == readOnly && smd.isValueLimited() == valueLimited && smd.toString().equals(attributeID);
    }

    /**
     Checks a particular selection meta data.
     **/
    static boolean verifySelectionMetaData(ResourceMetaData[] smd, Object attributeID, Class attributeType, boolean readOnly, int possibleValueCount, Object defaultValue, boolean valueLimited, boolean multipleAllowed)
    {
        int found = -1;
        for (int i = 0; (i < smd.length) && (found < 0); ++i)
        {
            if (smd[i].getID() == attributeID) found = i;
        }

        if (found < 0)
        {
            System.out.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifySelectionMetaData(smd[found], attributeID, attributeType, readOnly, possibleValueCount, defaultValue, valueLimited, multipleAllowed);
    }

    private static RUserList newRUserList(AS400 system, String userInfo, String groupInfo) throws Exception
    {
        RUserList ul = new RUserList(system);
        ul.setSelectionValue(RUserList.SELECTION_CRITERIA, userInfo);
        ul.setSelectionValue(RUserList.GROUP_PROFILE, groupInfo);
        return ul;
    }

    /**
     getSelectionMetaData() with 0 parameters - Verify that the array contains all selections.
     **/
    public void Var001()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition(smd.length == 3 && verifySelectionMetaData(smd, RUserList.SELECTION_CRITERIA, String.class, false, 4, RUserList.ALL, true, false) && verifySelectionMetaData(smd, RUserList.GROUP_PROFILE, String.class, false, 2, RUserList.NONE, false, false) && verifySelectionMetaData(smd, RUserList.USER_PROFILE, String.class, false, 1, RUserList.ALL, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionMetaData() with 1 parameter - Pass null.
     **/
    public void Var002()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData smd = u.getSelectionMetaData(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getSelectionMetaData() with 1 parameter - Pass an invalid attribute ID.
     **/
    public void Var003()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData smd = u.getSelectionMetaData(new Date());
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getSelectionMetaData() with 1 parameter - Pass SELECTION_CRITERIA.
     **/
    public void Var004()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData smd = u.getSelectionMetaData(RUserList.SELECTION_CRITERIA);
            assertCondition(verifySelectionMetaData(smd, RUserList.SELECTION_CRITERIA, String.class, false, 4, RUserList.ALL, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionMetaData() with 1 parameter - Pass GROUP_PROFILE.
     **/
    public void Var005()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData smd = u.getSelectionMetaData(RUserList.GROUP_PROFILE);
            assertCondition(verifySelectionMetaData(smd, RUserList.GROUP_PROFILE, String.class, false, 2, RUserList.NONE, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionMetaData() with 1 parameter - Try each of them.
     **/
    public void Var006()
    {
        try
        {
            RUserList u = new RUserList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            boolean success = true;
            for (int i = 0; i < smd.length; ++i)
            {
                boolean thisOne = verifySelectionMetaData(u.getSelectionMetaData(smd[i].getID()), smd[i].getID(), smd[i].getType(), smd[i].isReadOnly(), smd[i].getPossibleValues().length, smd[i].getDefaultValue(), smd[i].isValueLimited(), smd[i].areMultipleAllowed());
                if (!thisOne)
                {
                    System.out.println("Comparison failed for: " + smd[i] + ".");
                    success = false;
                }
            }
            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - When there is no connection.
     **/
    public void Var007()
    {
        try
        {
            RUserList u = new RUserList();
            Object selectionCriteria = u.getSelectionValue(RUserList.SELECTION_CRITERIA);
            Object groupProfile = u.getSelectionValue(RUserList.GROUP_PROFILE);
            assertCondition((selectionCriteria.equals(RUserList.ALL) && (groupProfile.equals(RUserList.NONE))));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - When the connection is bogus.
     **/
    public void Var008()
    {
        try
        {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RUserList u = new RUserList(system);
            Object selectionCriteria = u.getSelectionValue(RUserList.SELECTION_CRITERIA);
            Object groupProfile = u.getSelectionValue(RUserList.GROUP_PROFILE);
            assertCondition(selectionCriteria.equals(RUserList.ALL) && groupProfile.equals(RUserList.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - Pass null.
     **/
    public void Var009()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            Object value = u.getSelectionValue(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     getSelectionValue() - Pass an invalid selection ID.
     **/
    public void Var010()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            Object value = u.getSelectionValue("Yo");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     getSelectionValue() - Pass an selection ID, whose value has not been referenced.
     **/
    public void Var011()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            Object selectionCriteria = u.getSelectionValue(RUserList.SELECTION_CRITERIA);
            Object groupProfile = u.getSelectionValue(RUserList.GROUP_PROFILE);
            assertCondition(selectionCriteria.equals(RUserList.ALL) && groupProfile.equals(RUserList.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - Pass an selection ID, whose value has been changed using the constructors.
     **/
    public void Var012()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, "Hola");

            Object selectionCriteria = u.getSelectionValue(RUserList.SELECTION_CRITERIA);
            Object groupProfile = u.getSelectionValue(RUserList.GROUP_PROFILE);
            assertCondition(selectionCriteria.equals(RUserList.MEMBER) && groupProfile.equals("Hola"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - Pass an selection ID, whose value has been changed using the old methods.
     **/
    public void Var013()
    {
        try
        {
            RUserList u = newRUserList(pwrSys_, RUserList.MEMBER, RUserList.NOGROUP);

            Object selectionCriteria = u.getSelectionValue(RUserList.SELECTION_CRITERIA);
            Object groupProfile = u.getSelectionValue(RUserList.GROUP_PROFILE);
            assertCondition(selectionCriteria.equals(RUserList.MEMBER) && groupProfile.equals(RUserList.NOGROUP));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - Pass an selection ID, whose value has been changed using the new methods.
     **/
    public void Var014()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.USER);
            u.setSelectionValue(RUserList.GROUP_PROFILE, RUserList.NONE);

            Object selectionCriteria = u.getSelectionValue(RUserList.SELECTION_CRITERIA);
            Object groupProfile = u.getSelectionValue(RUserList.GROUP_PROFILE);
            assertCondition(selectionCriteria.equals(RUserList.USER) && groupProfile.equals(RUserList.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     getSelectionValue() - Pass every selection ID.
     **/
    public void Var015()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            ResourceMetaData[] smd = u.getSelectionMetaData();
            boolean success = true;
            for (int i = 0; i < smd.length; ++i)
            {
                // System.out.println("Getting selection " + smd[i] + ".");
                Object value = u.getSelectionValue(smd[i].getID());
                Class valueClass = value.getClass();
                Class type = smd[i].getType();

                // Validate the type.
                if (smd[i].areMultipleAllowed())
                {
                    if (!valueClass.isArray())
                    {
                        System.out.println("Error getting selection " + smd[i] + ".");
                        System.out.println("Type array mismatch: " + valueClass + " is not an array, but multiple values are allowed.");
                        success = false;
                    }
                    else
                    {
                        Class componentType = valueClass.getComponentType();
                        if (!componentType.equals(type))
                        {
                            System.out.println("Error getting selection " + smd[i] + ".");
                            System.out.println("Type mismatch: " + componentType + " != " + type + ".");
                            success = false;
                        }
                    }
                }
                else if (!valueClass.equals(type))
                {
                    System.out.println("Error getting selection " + smd[i] + ".");
                    System.out.println("Type mismatch: " + valueClass + " != " + type + ".");
                    success = false;
                }

                // Validate the value.
                if (smd[i].isValueLimited())
                {
                    Object[] possibleValues = smd[i].getPossibleValues();
                    boolean found = false;
                    if (smd[i].areMultipleAllowed())
                    {
                        Object[] asArray = (Object[])value;
                        for (int k = 0; k < asArray.length; ++k)
                        {
                            for (int j = 0; j < possibleValues.length && found == false; ++j)
                            {
                                if (possibleValues[j].equals(asArray[k])) found = true;
                            }

                            if (!found)
                            {
                                System.out.println("Error getting selection " + smd[i] + ".");
                                System.out.println("Value: " + asArray[k] + " is not a valid possible value.");
                                success = false;
                            }
                        }
                    }
                    else
                    {
                        for (int j = 0; j < possibleValues.length && found == false; ++j)
                        {
                            if (possibleValues[j].equals(value)) found = true;
                        }

                        if (!found)
                        {
                            System.out.println("Error getting selection " + smd[i] + ".");
                            System.out.println("Value: " + value + " is not a valid possible value.");
                            success = false;
                        }
                    }
                }
            }
            assertCondition(success);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSelectionValue() - When there is no connection.
     **/
    public void Var016()
    {
        try
        {
            RUserList u = new RUserList();
            u.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.GROUP);
            u.setSelectionValue(RUserList.GROUP_PROFILE, RUserList.NONE);
            assertCondition(u.getSelectionValue(RUserList.SELECTION_CRITERIA).equals(RUserList.GROUP) && u.getSelectionValue(RUserList.GROUP_PROFILE).equals(RUserList.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSelectionValue() - Pass null for the selection ID.
     **/
    public void Var017()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(null, new Integer(2));
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSelectionValue() - Pass null for the value.
     **/
    public void Var018()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            // Set to something else first.
            u.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.MEMBER);
            u.setSelectionValue(RUserList.GROUP_PROFILE, "MYGROUP");

            u.setSelectionValue(RUserList.SELECTION_CRITERIA, null);
            u.setSelectionValue(RUserList.GROUP_PROFILE, null);
            assertCondition(u.getSelectionValue(RUserList.SELECTION_CRITERIA).equals(RUserList.ALL) && u.getSelectionValue(RUserList.GROUP_PROFILE).equals(RUserList.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     setSelectionValue() - Pass an invalid selection ID.
     **/
    public void Var019()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(u, "This is a test");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setSelectionValue() - Set a read only selection.
     **/
    public void Var020()
    {
        /* There are no read only selections in RUserList! */
        succeeded();
    }

    /**
     setSelectionValue() - Pass a value which is the wrong type.
     **/
    public void Var021()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(RUserList.GROUP_PROFILE, new Integer(4));
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setSelectionValue() - Pass a value which is not one of the possible values.
     **/
    public void Var022()
    {
        try
        {
            RUserList u = new RUserList(pwrSys_);
            u.setSelectionValue(RUserList.SELECTION_CRITERIA, "Bogus");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     setSelectionValue() - When the list is already open.
     **/
    public void Var023()
    {
        try
        {
            RUserList ul = new RUserList(pwrSys_);

            // Reduce the buffer size - ran out of memory on Linux.
            ul.setPageSize(5);
            ul.setNumberOfPages(3);

            ul.open();
            ul.resourceAt(20);

            // Set the selection to get only group profiles.
            ul.setSelectionValue(RUserList.SELECTION_CRITERIA, RUserList.GROUP);
            ul.setSelectionValue(RUserList.GROUP_PROFILE, RUserList.NONE);

            // There should be some non-group profiles, since the list has not been refreshed.
            boolean found1 = false;
            ul.waitForComplete();
            long length = ul.getListLength();
            for (int i = 0; i < length; ++i)
            {
                RUser u = (RUser)ul.resourceAt(i);
                Long groupIDNumber = (Long)u.getAttributeValue(RUser.GROUP_ID_NUMBER);
                if (groupIDNumber.longValue() == 0) found1 = true;
            }

            // Refresh.  Now no non-group profiles should appear.
            boolean found2 = false;
            ul.refreshContents();
            ul.waitForComplete();
            length = ul.getListLength();
            for (int i = 0; i < length; ++i)
            {
                RUser u = (RUser)ul.resourceAt(i);
                Long groupIDNumber = (Long)u.getAttributeValue(RUser.GROUP_ID_NUMBER);
                if (groupIDNumber.longValue() == 0) found2 = true;
            }

            assertCondition((found1 == true) && (found2 == false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
