///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalGroupTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;

import java.util.Vector;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueGroup;
import com.ibm.as400.access.SystemValueList;

import test.Testcase;

/**
 Testcase SysvalGroupTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>SystemValueGroup::add(String)
 <li>SystemValueGroup::contains(String)
 <li>SystemValueGroup::getGroupName()
 <li>SystemValueGroup::getGroupDescription()
 <li>SystemValueGroup::getNames()
 <li>SystemValueGroup::getSystem()
 <li>SystemValueGroup::getSystemValues()
 <li>SystemValueGroup::refresh(Vector)
 <li>SystemValueGroup::remove(String)
 <li>SystemValueGroup::setGroupDescription(String)
 <li>SystemValueGroup::setGroupName(String)
 <li>SystemValueGroup::setSystem(AS400)
 <li>SystemValueGroup::toString()
 <li>SystemValue::getGroupName()
 <li>SystemValue::getGroupDescription()
 </ul>
 **/
public class SysvalGroupTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SysvalGroupTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SysvalTestDriver.main(newArgs); 
   }
    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() on a system value group that was created using the default constructor.
     **/
    public void Var001()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.add("QDATE");
            assertCondition(svg.contains("QDATE"), "System value not added.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() on a system value group that was created with a system and name specified.
     **/
    public void Var002()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("QDAY");
            assertCondition(svg.contains("QDAY"), "System value not added.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() on a system value group that was created with a system and name and group value specified.
     **/
    public void Var003()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", SystemValueList.GROUP_ALC);
            svg.add("qday");
            assertCondition(svg.contains("qdAy"), "System value not added.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() on a system value group that was created with a system and name and list of names specified.
     **/
    public void Var004()
    {
        try
        {
            String[] str = new String[] { "qday", "qmonth" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", str);
            svg.add("qYEar");
            assertCondition(svg.contains("QYEAR") && svg.contains("QDAY") && svg.contains("QMONTH"), "System value not added.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::add().
     Try to call add() with a null value
     **/
    public void Var005()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "name");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::add().
     Try to call add() with a value of length(0).
     **/
    public void Var006()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test invalid usage of SystemValueGroup::add().
     Call add() and then try to reset the name using setName().  An ExtendedIllegalStateException should be thrown.
     **/
    public void Var007()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("QDAY");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue)svgv.elementAt(0);
            sv.setName("QCHRID");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "name", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
        }
    }

    /**
     Test invalid usage of SystemValueGroup::add().
     Try to call add() with a value of "TRASH".
     **/
    public void Var008()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.add("TRASH");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() twice on a system value group that was created.
     **/
    public void Var009()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", new String[] { "QMONTH" });
            svg.add("qYEar");
            svg.add("QDATE");
            assertCondition(svg.contains("QYEAR") && svg.contains("QDATE"), "System value not added.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() twice on a system value group that was created.
     **/
    public void Var010()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("qYEar");
            svg.add("QDATE");
            assertCondition(svg.contains("QYEAR") && svg.contains("QDATE"), "System value not added.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() twice on the same system value.
     **/
    public void Var011()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("qDATE");
            svg.add("QDATE");
            assertCondition(svg.getNames().length == 1, "System value added twice.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::add().
     Try to call add() on group that contains all system values.  Ensure name not added twice to group.
     **/
    public void Var012()
    {
        try
        {
            SystemValueList svl = new SystemValueList(pwrSys_);
            Vector sysvals = svl.getGroup(SystemValueList.GROUP_ALL);
            int numSysvals = sysvals.size();
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", SystemValueList.GROUP_ALL);
            svg.add("qDATE");
            // getNames().length won't be the same size necessarily.
            assertCondition(svg.getSystemValues().size() == numSysvals, "System value added twice.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using the default constructor.
     **/
    public void Var013()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            assertCondition(!svg.contains("QDATE"), "Contains failure.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using constructor SystemValueGroup(system, string, string).
     **/
    public void Var014()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            assertCondition(!svg.contains("QDATE"), "Contains failure.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using constructor SystemValueGroup(system, string, string, int).
     **/
    public void Var015()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_DATTIM);
            assertCondition(svg.contains("QDATE"), "Group does not contain QDATE.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using constructor SystemValueGroup(system, string, string, String[]).
     **/
    public void Var016()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QDATE" , "QTIME", "QDAY" });
            assertCondition(svg.contains("QDATE"), "Group does not contain QDATE.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using constructor SystemValueGroup() and had sysval added.
     **/
    public void Var017()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QDAY");
            svg.add("QMONTH");
            svg.add("QDATE");
            assertCondition(svg.contains("Qmonth"), "Group does not contain QMONTH.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using constructor SystemValueGroup(int).
     **/
    public void Var018()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_ALC);
            assertCondition(!svg.contains("Qmonth"), "Group should not contain QMONTH.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was created using constructor SystemValueGroup(int).
     **/
    public void Var019()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_ALC);
            svg.add("qMonth");
            assertCondition(svg.contains("Qmonth"), "Group does not contain QMONTH.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::contains().
     Try to call contains() on a system value group that was added and then removed.
     **/
    public void Var020()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("qMonth");
            if (svg.contains("Qmonth"))
            {
                svg.remove("qmonth");
                assertCondition(!svg.contains("Qmonth"), "QMONTH not removed.");
            }
            else
            {
                failed("Group does not contain QMONTH.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::contains().
     Try to call contains() with a null value.
     **/
    public void Var021()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.contains(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "name");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::contains().
     Try to call contains() with a value of length(0)
     **/
    public void Var022()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.contains("");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test invalid usage of SystemValueGroup::contains().
     Try to call contains() with a value of 0.
     **/
    public void Var023()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.contains("TRASH");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test invalid usage of SystemValueGroup::contains().
     Try to call contains() with a value of "TRASH"
     **/
    public void Var024()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.contains("TRASH");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getGroupDescription().
     Try to call getGroupDescription() on a system value group that was created using the default constructor.  Null should be returned.
     **/
    public void Var025()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            assertCondition(svg.getGroupDescription() == null, "Group description not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call contains() on a system value group that was created using the default constructor SystemValueGroup() plus an add.
     **/
    public void Var026()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setGroupDescription("New Group");
            assertCondition(svg.getGroupDescription().equals("New Group"), "Incorrect Description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call getDescription() on a system value group that was created using constructor SystemValueGroup(system, string, string).
     **/
    public void Var027()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Funny Group");
            assertCondition(svg.getGroupDescription().equals("Funny Group"), "Incorrect Description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call getDescription() on a system value group that was created using the constructor SystemValueGroup(system, string, string, string).
     **/
    public void Var028()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "New Group", new String[] { "QDATE" , "QTIME", "QDAY" });
            assertCondition(svg.getGroupDescription().equals("New Group"), "Incorrect Description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call getDescription() on a system value group that was created using the constructor SystemValueGroup(system, string, string, int).
     **/
    public void Var029()
    {
        try
        {
            String str = "This is my date and time group";
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", str, SystemValueList.GROUP_DATTIM);
            assertCondition(svg.getGroupDescription().equals(str), "Incorrect Description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call getGroupDescription() on a system value group that was created using the description of an existing system value).
     **/
    public void Var030()
    {
        try
        {
            SystemValue sv = new SystemValue(pwrSys_, "QDATE");
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", sv.getDescription(), new String[] { "Qdate" });
            assertCondition(svg.getGroupDescription().equals(sv.getDescription()), "Incorrect Description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call getGroupDescription() on a system value group that was set after it was created using the constructor SystemValueGroup(system, string, string, int).
     **/
    public void Var031()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "This is my date and time group", SystemValueList.GROUP_DATTIM);
            svg.setGroupDescription("A new description");
            assertCondition(svg.getGroupDescription().equals("A new description"), "Incorrect Description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group that was created using the default constructor.  Null should be returned.
     **/
    public void Var032()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            assertCondition(svg.getGroupName() == null, "Group name not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call contains() on a system value group that was created using the default constructor SystemValueGroup() plus an add.
     **/
    public void Var033()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setGroupName("Group1");
            assertCondition(svg.getGroupName().equals("Group1"), "Incorrect Name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group that was created using constructor SystemValueGroup(system, string, string).
     **/
    public void Var034()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Group Name", "Funny Group");
            assertCondition(svg.getGroupName().equals("Group Name"), "Incorrect Name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group that was created using the default constructor SystemValueGroup(system, string, string, string)
     **/
    public void Var035()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Another Group", "New Group", new String[] { "QDATE" , "QTIME", "QDAY" });
            assertCondition(svg.getGroupName().equals("Another Group"), "Incorrect Name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group that was created using the constructor SystemValueGroup(system, string, string, int).
     **/
    public void Var036()
    {
        try
        {
            String str = "This is my new group";
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, str, "Desc", SystemValueList.GROUP_DATTIM);
            assertCondition(svg.getGroupName().equals(str), "Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group name that was changed after it was constructed.
     **/
    public void Var037()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "This is my new group", "Desc", SystemValueList.GROUP_DATTIM);
            svg.setGroupName("I have a new name");
            assertCondition(svg.getGroupName().equals("I have a new name"), "Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group that was created using the description of an existing system value).
     **/
    public void Var038()
    {
        try
        {
            SystemValue sv = new SystemValue(pwrSys_, "QMONTH");
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, sv.getName(), "Desc", new String[] { "Qmonth" });
            assertCondition(svg.getGroupName().equals(sv.getName()), "Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the default constructor.  An ExtendedIllegalStateException should be thrown.
     **/
    public void Var039()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            String[] nameStrArray = svg.getNames();
            assertCondition(nameStrArray.length == 0, "Incorrect length.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using constructor (system, string, string).  An ExtendedIllegalStateException should be thrown.
     **/
    public void Var040()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            String[] nameStrArray = svg.getNames();
            assertCondition(nameStrArray.length == 0, "Incorrect length.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the default constructor SystemValueGroup() plus an add.
     **/
    public void Var041()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QDAY");
            String[] Names = svg.getNames();
            assertCondition(svg.getNames().length == 1 && Names[0].equals("QDAY"), "Incorrect System Value Name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the constructor SystemValueGroup(System, string, string, string[]) plus an add.
     **/
    public void Var042()
    {
        try
        {
            String[] strFinal = new String[] { "QADLTOTJ", "QCHRID", "QASTLVL", "QAUDCTL" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QADLTOTJ", "QCHRID", "QASTLVL" });
            svg.add("QAUDCTL");
            String[] Names = svg.getNames();
            if (Names.length != 4)
            {
                failed("Incorrect Names array length.");
                return;
            }
            for (int i = 0; i < 4; ++i)
            {
                if (!Names[i].equals(strFinal[i]))
                {
                    failed("Incorrect Names array.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the constructor SystemValueGroup(System, string, string, int).
     **/
    public void Var043()
    {
        try
        {
            SystemValueList svl = new SystemValueList(pwrSys_);
            Vector sysvals = svl.getGroup(SystemValueList.GROUP_EDT);
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_EDT);
            String[] Names = svg.getNames();
            if (svg.getNames().length != sysvals.size())
            {
                failed("Incorrect Names array length.");
                return;
            }
            for (int i = 0; i < svg.getNames().length; ++i)
            {
                SystemValue sv = (SystemValue) sysvals.elementAt(i);
                if (!svg.contains(sv.getName()))
                {
                    failed("Incorrect Names array.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the constructor SystemValueGroup(System, string, string, int).
     **/
    public void Var044()
    {
        try
        {
            SystemValueList svl = new SystemValueList(pwrSys_);
            Vector sysvals = svl.getGroup(SystemValueList.GROUP_STG);
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_STG);
            String[] Names = svg.getNames();
            for (int i = 0; i < svg.getNames().length; ++i)
            {
                SystemValue sv = (SystemValue)sysvals.elementAt(i);
                if (!svg.contains(sv.getName()))
                {
                    failed("Incorrect Names array.");
                    return;
                }
            }
            if (svg.getNames().length != sysvals.size())
            {
                failed("Incorrect Names array length.");
                return;
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the constructor SystemValueGroup(System, string, string, int).
     **/
    public void Var045()
    {
        try
        {
            SystemValueList svl = new SystemValueList(pwrSys_);
            Vector sysvals = svl.getGroup(SystemValueList.GROUP_SYSCTL);
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_SYSCTL);
            String[] Names = svg.getNames();
            for (int i = 0; i < svg.getNames().length; ++i)
            {
                SystemValue sv = (SystemValue)sysvals.elementAt(i);
                if (!svg.contains(sv.getName()))
                {
                    failed("Incorrect Names array.");
                    return;
                }
            }
            if (svg.getNames().length != sysvals.size())
            {
                failed("Incorrect Names array length.");
                return;
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the constructor SystemValueGroup(System, string, string, string[]) plus an add and then a remove followed by another remove and then an add.
     **/
    public void Var046()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QCCSID", "QBASPOOL", "QHSTLOGSIZ" });
            svg.add("QAUDCTL");
            svg.remove("QCCSID");
            String[] Names = svg.getNames();
            if (Names.length != 3)
            {
                failed("Incorrect Names array length.");
                return;
            }
            for (int i = 0; i < Names.length; ++i)
            {
                if (!(Names[i].equals("QBASPOOL") || Names[i].equals("QHSTLOGSIZ") || Names[i].equals("QAUDCTL")))
                {
                    failed("Incorrect Names array entry.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using the constructor SystemValueGroup(System, string, string, string[]) plus an add and then a remove followed by another remove and then an add.
     **/
    public void Var047()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QCENTURY", "QRETSVRSEC" });
            svg.add("QMODEL");
            svg.remove("QMODEL");
            svg.remove("QCENTURY");
            svg.add("QRMTIPL");
            String[] Names = svg.getNames();
            if (Names.length != 2)
            {
                failed("Incorrect Names array length.");
                return;
            }
            for (int i = 0; i < Names.length; ++i )
            {
                if (!(Names[i].equals("QRMTIPL") || Names[i].equals("QRETSVRSEC")))
                {
                    failed("Incorrect Names array entry.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getNames().
     Try to call getNames() on a system value group that was created using constructor (system, string, string) that has no values.
     An empty String array should be returned.
     **/
    public void Var048()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("QPRTTXT");
            svg.add("QPRBFTR");
            svg.remove("QPRTTXT");
            svg.add("QPFRADJ");
            svg.remove("QPFRADJ");
            svg.remove("qprbftr");
            assertCondition(svg.getNames().length == 0, "Wrong number of names returned: " + svg.getNames().length + " != 0");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystem().
     Try to call getSystem() on a system value group that was created using the default constructor.  An ExtendedIllegalStateException should be thrown.
     **/
    public void Var049()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            assertCondition(svg.getSystem() == null, "System not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystem().
     Try to call getSystem() on a system value group that was created using the constructor SystemValueGroup(system, name, desc).
     **/
    public void Var050()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            assertCondition(svg.getSystem() == pwrSys_, "Incorrect system.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystem().
     Try to call getSystem() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var051()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_DATTIM);
            assertCondition(svg.getSystem() == pwrSys_, "Incorrect system.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystem().
     Try to call getSystem() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, String[]).
     **/
    public void Var052()
    {
        try
        {
            String[] str = new String[] { "QDATE", "QTIME"};
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            assertCondition(svg.getSystem() == pwrSys_, "Incorrect system.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /** 
     Test invalid usage of SystemValueGroup::getSystem().
     Try to call getSystem() on a system value group that set the system via setSystem().
     **/
    public void Var053()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            assertCondition(svg.getSystem() == pwrSys_, "Incorrect system.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystem().
     Try to call getSystem() on a system value group that set the system via setSystem().
     **/
    public void Var054()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.setSystem(systemObject_);
            assertCondition(svg.getSystem() == systemObject_, "Incorrect system.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the default constructor.  An ExtendedIllegalStateException should be thrown.
     **/
    public void Var055()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.getSystemValues();
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Test invalid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the default constructor.  An empty vector should be returned.
     **/
    public void Var056()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            Vector svgv = svg.getSystemValues();
            assertCondition(svgv.size() == 0, "Bad vector size.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var057()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_MSG);
            Vector svgv = svg.getSystemValues();
            SystemValueList svl = new SystemValueList(pwrSys_);
            Vector svlv = svl.getGroup(SystemValueList.GROUP_MSG);
            SystemValue sv0 = (SystemValue)svgv.elementAt(0);
            SystemValue sv4 = (SystemValue)svgv.elementAt(4);
            SystemValue svF = (SystemValue)svgv.elementAt(svgv.size() - 1);
            SystemValue svl0 = (SystemValue)svlv.elementAt(0);
            SystemValue svl4 = (SystemValue)svlv.elementAt(4);
            SystemValue svlF = (SystemValue)svlv.elementAt(svgv.size() - 1);

            assertCondition(svgv.size() == svlv.size() && sv0.getName().equals(svl0.getName()) && sv4.getName().equals(svl4.getName()) && svF.getName().equals(svlF.getName()), "Incorrect System Value.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, String[]).
     **/
    public void Var058()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QKBDBUF", "QMONTH", "QLANGID"});
            Vector svgv = svg.getSystemValues();
            if (svgv.size() != 3)
            {
                failed("Incorrect vector size.");
                return;
            }
            for (int i = 0; i < svgv.size(); ++i)
            {
                SystemValue sv = (SystemValue)svgv.elementAt(i);
                if (!(sv.getName().equals("QKBDBUF") || sv.getName().equals("QMONTH")  || sv.getName().equals("QLANGID")))
                {
                    failed("Incorrect system value.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the default constructor and add).
     **/
    public void Var059()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QdaTe"});
            svg.add("QmonTH");
            svg.add("qyear");
            Vector svgv = svg.getSystemValues();
            if (svgv.size() != 3)
            {
                failed("Incorrect vector size.");
                return;
            }
            for (int i = 0; i < svgv.size(); ++i)
            {
                SystemValue sv = (SystemValue)svgv.elementAt(i);
                if (!((sv.getName().equals("QDATE") || sv.getName().equals("QYEAR") || sv.getName().equals("QMONTH"))))
                {
                    failed("Incorrect system value.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the default constructor and adds and removes.
     **/
    public void Var060()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QmonTH");
            svg.add("qyear");
            svg.remove("QMONTH");
            svg.remove("QYEAR");
            svg.add("qmonth");
            svg.add("qday");
            svg.add("QSECond");
            Vector svgv = svg.getSystemValues();
            if (svgv.size() != 3)
            {
                failed("Incorrect vector size.");
                return;
            }
            for (int i = 0; i < svgv.size(); ++i)
            {
                SystemValue sv = (SystemValue)svgv.elementAt(i);
                if (!((sv.getName().equals("QDAY") || sv.getName().equals("QMONTH") || sv.getName().equals("QSECOND"))))
                {
                    failed("Incorrect system value.");
                    return;
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getSystemValues().
     Try to call getSystemValues() on a system value group that was created using the default constructor and adds and removes.
     **/
    public void Var061()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QmonTH");
            svg.add("qyear");
            svg.remove("QMONTH");
            svg.remove("QYEAR");
            svg.add("qmonth");
            svg.add("qday");
            svg.add("QSECond");
            svg.remove("QSECOnd");
            svg.remove("qday");
            svg.remove("qmonth");
            Vector svgv = svg.getSystemValues();
            assertCondition(svgv.size() == 0, "Incorrect vector size.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created using the default constructor.  An ExtendedIllegalStateException should be thrown.
     **/
    public void Var062()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.refresh(svg.getSystemValues());
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created using the default constructor.  An empty vector should be returned.
     **/
    public void Var063()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.refresh(svg.getSystemValues());
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, String[]).
     **/
    public void Var064()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", new String[] { "QDAY" });
            svg.refresh(svg.getSystemValues());
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, String[]).
     **/
    public void Var065()
    {
        String failMsg = "";
        try
        {
            String[] str = new String[] { "QYEAR" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            Object obj = sv.getValue();
            Object obj2 = sv.getValue();
            if (!obj.equals(obj2))
            {
                failMsg += "Initial objects are not equal.";
            }
            else
            {
                SystemValueGroup svg2 = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
                Vector svgv2 = svg2.getSystemValues();
                SystemValue sv2 = (SystemValue) svgv2.elementAt(0);
                sv2.setValue("07");
                Object obj3 = sv.getValue();
                // Values should still be the same.
                if (!obj3.equals(obj))
                {
                    failMsg += "Failed to cache value.";
                }
                else
                {
                    // Now refresh the cache to see if we retrieve the reset value.
                    svg.refresh(svgv);
                    Object obj4 = sv.getValue();
                    if (!((String)obj4).trim().equals("07"))
                    {
                        failMsg += "Failed to retrieve reset value.";
                    }
                }
            }
            // Reset back to original.
            sv.setValue(obj);
            SystemValueGroup newsvg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector newsvgv = newsvg.getSystemValues();
            SystemValue newsv = (SystemValue)newsvgv.elementAt(0);
            Object check = newsv.getValue();
            if (!((String)check).trim().equals(((String)obj).trim()))
            {
                failMsg += "Failed to reset value to original.";
            }
        }
        catch (Exception e)
        {
            failMsg += "Unexpected exception.";
            failed(e, failMsg);
            return;
        }
        assertCondition(failMsg.length() == 0, failMsg);
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, String[]).
     **/
    public void Var066()
    {
        String failMsg = "";
        String[] str = new String[] { "QYEAR" , "QDAY", "QMONTH" };
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            SystemValue sv1 = (SystemValue) svgv.elementAt(1);
            SystemValue sv2 = (SystemValue) svgv.elementAt(2);
            Object obj = sv.getValue();
            Object obj1 = sv1.getValue();
            Object obj2 = sv2.getValue();
            Object newobj = sv.getValue();
            Object newobj1 = sv1.getValue();
            Object newobj2 = sv2.getValue();
            if (!obj.equals(newobj))
            {
                failMsg += "Initial objects are not equal (obj).";
            }
            else if (!obj1.equals(newobj1))
            {
                failMsg += "Initial objects are not equal (obj1).";
            }
            else if (!obj2.equals(newobj2))
            {
                failMsg += "Initial objects are not equal (obj2). ";
            }
            else
            {
                SystemValueGroup svg2 = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
                Vector svgv2 = svg2.getSystemValues();
                SystemValue newsv= (SystemValue) svgv2.elementAt(0);
                SystemValue newsv1= (SystemValue) svgv2.elementAt(1);
                SystemValue newsv2 = (SystemValue) svgv2.elementAt(2);
                newsv.setValue("07");
                newsv1.setValue("06");
                newsv2.setValue("03");
                Object testobj = sv.getValue();
                Object testobj1 = sv1.getValue();
                Object testobj2 = sv2.getValue();
         
                // Values should still be the same.
                if (!testobj.equals(obj) || !testobj1.equals(obj1) || !testobj2.equals(obj2))
                {
                    failMsg += "Failed to cache value.";
                }
                else
                {
                    // Now refresh the cache to see if we retrieve the reset value.
                    svg.refresh(svgv);
                    Object newtestobj = sv.getValue();
                    Object newtestobj1 = sv1.getValue();
                    Object newtestobj2 = sv2.getValue();
                    if (!((String)newtestobj).trim().equals("07")  || !((String)newtestobj1).trim().equals("06") || !((String)newtestobj2).trim().equals("03"))
                    {
                        failMsg += "Failed to retrieve reset value.";
                    }
                }
            }
            // Reset back to original.
            sv.setValue(obj);
            sv1.setValue(obj1);
            sv2.setValue(obj2);
            SystemValueGroup newsvg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector newsvgv = newsvg.getSystemValues();
            SystemValue lastsv = (SystemValue)newsvgv.elementAt(0);
            SystemValue lastsv1 = (SystemValue)newsvgv.elementAt(1);
            SystemValue lastsv2 = (SystemValue)newsvgv.elementAt(2);
            Object check = lastsv.getValue();
            Object check1 = lastsv1.getValue();
            Object check2 = lastsv2.getValue();
            if (!((String)check).trim().equals(((String)obj).trim()) || !((String)check1).trim().equals(((String)obj1).trim())  || !((String)check2).trim().equals(((String)obj2).trim()))
            {
                failMsg += "Failed to reset value to original.";
            }
        }
        catch (Exception e)
        {
            failMsg += "Unexpected exception.";
            failed(e, failMsg);
            return;
        }
        assertCondition(failMsg.length() == 0, failMsg);	
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var067()
    {
        String failMsg = "";
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_EDT);
            Vector svgv = svg.getSystemValues();
            SystemValue[] svArray = new SystemValue[svgv.size() + 1];
            Object[] objArray = new Object[svgv.size() + 1];
            Object[] newobjArray = new Object[svgv.size() + 1];
            for (int i = 0; i < svgv.size(); ++i)
            {
                svArray[i] = (SystemValue)svgv.elementAt(i);
                objArray[i]  = svArray[i].getValue();
                newobjArray[i] = svArray[i].getValue();
            }
            for (int i = 0; i < svgv.size(); ++ i)
            {
                if (!objArray[i].equals(newobjArray[i]))
                {
                    failMsg += "Initial objects are not equal (obj)." + i;
                }
            }
            if (failMsg == "")
            {
                SystemValueGroup svg2 = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_EDT);
                Vector svgv2 = svg2.getSystemValues();
                SystemValue[] newsvArray= new SystemValue[svgv2.size() + 1];
                Object[] testobjArray = new Object[svgv2.size() + 1];
                for (int i = 0; i < svgv2.size(); ++i)
                {
                    newsvArray[i] = (SystemValue) svgv2.elementAt(i);
                }
                newsvArray[0].setValue("C");
                newsvArray[1].setValue("YMD");
                newsvArray[2].setValue("-");
                newsvArray[3].setValue("J");
                newsvArray[4].setValue(",");
                testobjArray[0] = svArray[0].getValue();
                testobjArray[1] = svArray[1].getValue();
                testobjArray[2] = svArray[2].getValue();
                testobjArray[3] = svArray[3].getValue();
                testobjArray[4] = svArray[4].getValue();
                // Values should still be the same.
                for (int i = 0; i < svgv.size(); ++i)
                {
                    if (!testobjArray[i].equals(objArray[i]))
                    {
                        failMsg += "Failed to cache value.";
                    }
                }
                if (failMsg == "")
                {
                    // Now refresh the cache to see if we retrieve the reset value.
                    svg.refresh(svgv);
                    Object[] newtestobjArray = new Object[15];
                    for (int i = 0; i < svgv.size(); ++i)
                    {
                        newtestobjArray[i] = svArray[i].getValue();
                    }
                    if (!((String)newtestobjArray[0]).trim().equals("C")  || !((String)newtestobjArray[1]).trim().equals("YMD") || !((String)newtestobjArray[2]).trim().equals("-")  || !((String)newtestobjArray[3]).trim().equals("J")  || !((String)newtestobjArray[4]).trim().equals(","))
                    {
                        failMsg += "Failed to retrieve reset value.";
                    }
                }
            }
            // Reset back to original.
            svArray[0].setValue(objArray[0]);
            svArray[1].setValue(objArray[1]);
            svArray[2].setValue(objArray[2]);
            svArray[3].setValue(objArray[3]);
            svArray[4].setValue(objArray[4]);
            SystemValueGroup newsvg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_EDT);
            Vector newsvgv = newsvg.getSystemValues();
            SystemValue[] lastsvArray = new SystemValue[15];
            Object[] checkobjArray = new Object[15];
            for (int i = 0; i < svgv.size(); ++i)
            {
                lastsvArray[i] = (SystemValue)newsvgv.elementAt(i);
                checkobjArray[i] = lastsvArray[i].getValue();
            }
            for (int i = 0; i < svgv.size(); ++i)
            {
                if (!((String)checkobjArray[i]).trim().equals(((String)objArray[i]).trim()))
                {
                    failMsg += "Failed to reset value to original." + i;
                }
            }
        }
        catch (Exception e)
        {
            failMsg += "Unexpected exception.";
            failed(e, failMsg);
            return;
        }
        assertCondition(failMsg.length() == 0, failMsg);
    }

    /**
     Test invalid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that contains null objects.
     **/
    public void Var068()
    {
        try
        {
            String[] str = new String[] { "QYEAR" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector svgv = svg.getSystemValues();
            Vector newVector = new Vector();
            Object obj = null;
            newVector.addElement(obj);
            svg.refresh(newVector);
            failed("Exception not thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that contains non-SystemValue objects.
     **/
    public void Var069()
    {
        try
        {
            String[] str = new String[] { "QYEAR", "QDAY" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector svgv = svg.getSystemValues();
            Vector newVector = new Vector();
            newVector.addElement(svgv.elementAt(0));
            newVector.addElement(str[0]);
            newVector.addElement(str[1]);
            svg.refresh(newVector);
            failed("Exception not thrown.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "systemValues", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that contains system values that were added and then reset.
     **/
    public void Var070()
    {
        String failMsg = "";
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QDAY");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            Object obj = sv.getValue();
            Object obj2 = sv.getValue();
            if (!obj.equals(obj2))
            {
                failMsg += "Initial objects are not equal.";
            }
            else
            {
                SystemValueGroup svg2 = new SystemValueGroup(pwrSys_, "Name", "Desc");
                svg2.add("QDAY");
                Vector svgv2 = svg2.getSystemValues();
                SystemValue sv2 = (SystemValue) svgv2.elementAt(0);
                sv2.setValue("04");
                sv2.setValue("06");
                Object obj3 = sv.getValue();
                // Values should still be the same.
                if (!obj3.equals(obj))
                {
                    failMsg += "Failed to cache value.";
                }
                else
                {
                    // Now refresh the cache to see if we retrieve the reset value.
                    svg.refresh(svgv);
                    Object obj4 = sv.getValue();
                    if (!((String)obj4).trim().equals("06"))
                    {
                        failMsg += "Failed to retrieve reset value.";
                    }
                    sv2.setValue("02");
                    svg.refresh(svgv);
                    obj4 = sv.getValue();
                    if (! ((String)obj4).trim().equals("02"))
                    {
                        failMsg += "Failed to retrieve reset value.";
                    }
                }
            }
            // Reset back to original.
            sv.setValue(obj);
            SystemValueGroup newsvg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            newsvg.add("QDAY");
            Vector newsvgv = newsvg.getSystemValues();
            SystemValue newsv = (SystemValue)newsvgv.elementAt(0);
            Object check = newsv.getValue();
            if (!((String)check).trim().equals(((String)obj).trim()))
            {
                failMsg += "Failed to reset value to original.";
            }
        }
        catch (Exception e)
        {
            failMsg += "Unexpected exception.";
            failed(e, failMsg);
            return;
        }
        assertCondition(failMsg.length() == 0, failMsg);
    }

    /**
     Test valid usage of SystemValueGroup::refresh().
     Try to call refresh() on a system value group that was created from a constructor using getSystemValues() as the vector.
     **/
    public void Var071()
    {
        try
        {
            String[] str = new String[] { "QDAY", "QMONTH" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            svg.refresh(svg.getSystemValues());
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::remove().
     Try to call remove() on a system value group that was created using the default constructor.
     **/
    public void Var072()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.add("QDATE");
            svg.remove("QDATE");
            assertCondition(!svg.contains("QDATE"), "System value not removed.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::remove().
     Try to call remove() on a system value group that was created with a system and name specified.
     **/
    public void Var073()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.add("qday");
            svg.remove("QDAY");
            assertCondition(!svg.contains("qday"), "System value not removed.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::remove().
     Try to call remove() on a system value group that was created with a system and name and group value specified.
     **/
    public void Var074()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", SystemValueList.GROUP_DATTIM);
            svg.remove("QDATE");
            assertCondition(!svg.contains("QDate"), "System value not removed.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::remove().
     Try to call remove() on a system value group that was created with a system and name and list of names specified.
     **/
    public void Var075()
    {
        try
        {
            String[] str = new String[] { "qday", "qmonth" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", str);
            svg.remove("qMOnth");
            assertCondition(!svg.contains("Qmonth"), "System value not removed.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::remove().
     Try to call remove() on a group with no values.  
     remove should just return a false value.
     **/
    public void Var076()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            if ( !svg.remove("QDATE"))
                succeeded();
            else
                failed("Returned TRUE when trying to remove name from empty list");
        }
        catch (Exception e)
        {
            failed("Unknown exception.");
        }
    }


    /**
     Test invalid usage of SystemValueGroup::remove().
     Try to call remove() whith a null value
     **/
    public void Var077()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", SystemValueList.GROUP_ALL);
            svg.remove(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "name"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Test invalid usage of SystemValueGroup::remove().
     Try to call remove() with a value of length(0)
     **/
    public void Var078()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", SystemValueList.GROUP_ALC);
            svg.remove("");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "",
                                    ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::remove().
     Try to call remove() with a value of "TRASH"
     **/
    public void Var079()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_DATTIM);
            svg.remove("TRASH");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH",
                                    ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::remove().
     Try to call remove() twice on a system value group that was created
     **/
    public void Var080()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", SystemValueList.GROUP_DATTIM);
            if (svg.remove("QYEAR") )
                if (!svg.contains("QYEAR"))
                    if (!svg.remove("QYEAR"))
                        succeeded();
                    else
                        failed("Remove failed.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::remove().
     Try to call remove() twice on the same system value
     **/
    public void Var081()
    {
        try
        {
            String[] str = new String[] { "QYEAR", "QDATE" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc", str);
            if (svg.remove("QYEAR") && svg.remove("QDATE") )
                if (!svg.remove("QYEAR") && !svg.remove("QDATE") )
                    if (svg.getNames().length == 0 )
                        succeeded();
                    else
                        failed("Remove failed.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::remove().
     Try to call remove() on a group with no values
     created using the default constructor 
     remove should just return a false value.
     **/
    public void Var082()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            if ( !svg.remove("QDATE"))
                succeeded();
            else
                failed("Returned TRUE when trying to remove name from empty list");
        }
        catch (Exception e)
        {
            failed("Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::setGroupName().
     Try to call setGroupName() on a system value group that was created
     using the default constructor.
     **/
    public void Var083()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setGroupName("MyGroup");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test valid usage of SystemValueGroup::setGroupName().
     Try to call setGroupName() on a system value group that was created
     using the constructor SystemValueGroup(system, Name, Description).
     **/
    public void Var084()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            if (!svg.getGroupName().equals("MyGroup"))
                failed("Group name not set.");
            svg.setGroupName("MyOtherGroup");
            if (svg.getGroupName().equals("MyOtherGroup"))
                succeeded();
            else
                failed("Group name not set.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test valid usage of SystemValueGroup::setGroupName().
     Try to call setGroupName() on a system value group that was created
     with a system and name specified.
     **/
    public void Var085()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.setGroupName(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "groupName"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Test invalid usage of SystemValueGroup::setGroupName().
     Try to call setGroupName() with a value of length(0)
     **/
    public void Var086()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.setGroupName("");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected info.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::setGroupDescription().
     Try to call setGroupDescription() on a system value group that was created
     using the default constructor.
     **/
    public void Var087()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setGroupDescription("This is a group");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test valid usage of SystemValueGroup::setGroupDescription().
     Try to call setGroupDescription() on a system value group that was created
     using the constructor SystemValueGroup(system, Name, Description).
     **/
    public void Var088()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            if (!svg.getGroupDescription().equals("Desc"))
                failed("Group name not set.");
            svg.setGroupDescription("My New Description");
            if (svg.getGroupDescription().equals("My New Description"))
                succeeded();
            else
                failed("Group description not set.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test valid usage of SystemValueGroup::setGroupDescription().
     Try to call setGroupDescription() on a system value group that was created
     with a system and name specified.
     **/
    public void Var089()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.setGroupDescription(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "groupDescription"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Test invalid usage of SystemValueGroup::setGroupDescription().
     Try to call setGroupName() with a value of length(0)
     **/
    public void Var090()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "MyGroup", "Desc");
            svg.setGroupDescription("");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected info.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::setSystem().
     Try to call setGroupName() on a system value group that was created
     with a system and name specified.
     **/
    public void Var091()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            if (svg.getSystem() != pwrSys_)
                failed("Incorrect system.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::setSystem().
     Try to call setSystem() on a system value group passing
     null as the system name.
     A NullPointerException should be thrown.
     **/
    public void Var092()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::setGroupName().
     Try to call setSystem() on a system value group that was created
     with a system specified passing null in for the system.
     A NullPointerException should be thrown.
     **/
    public void Var093()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.setSystem(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::setSystem().
     Try to set the system of a system value group in which
     the name is already set.
     **/
    public void Var094()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.setSystem(systemObject_);
            if (svg.getSystem() != systemObject_)
                failed("Incorrect system.");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test invalid usage of SystemValueGroup::setSystem().
     Try to set the system of a system value group in which
     the name is already set and has already been connected.
     An ExtendedIllegalStateException should be thrown.
     **/
    public void Var095()
    {
        /*      try
         {
         SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_ALC);
         Vector svgv = svg.getSystemValues();  // causes a connection
         svg.setSystem(systemObject_);
         failed("No exception.");
         }
         catch (Exception e)
         {
         if (exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
         ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
         succeeded();
         else
         failed(e, "Wrong exception info.");
         }
         *///@A1D
        // You can change the system for a SystemValueGroup. This makes it easy to get a list of 
        // system values from one system and then get the same list from another system.
        notApplicable("System can be changed after connecting."); //@A1A
    }


    /**
     Test valid usage of SystemValueGroup::setSystem().
     Try to set the system of a system value group in which
     the name is already set. Ensure resetting the name and description
     is OK.
     **/
    public void Var096()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.setGroupName("MyGroup");
            svg.setGroupDescription("MyDesc");
            svg.setSystem(systemObject_);
            svg.setGroupName("NewGroup");
            svg.setGroupDescription("NewDescription");
            if (svg.getSystem() != systemObject_)
                failed("Incorrect system.");
            else if (svg.getGroupName().equals("NewGroup") && svg.getGroupDescription().equals("NewDescription") )
                succeeded();
            else
                failed("Bad group name or description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test toString()
     **/
    public void Var097()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "My Name", "My Desc");
            String toStr = svg.toString();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName on a system value group that was created
     using the default constructor SystemValueGroup() plus an add.
     **/
    public void Var098()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QYEAR");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svName =  sv.getGroupName();
            if (svName.equals("Name") )
                succeeded();
            else
                failed("Name incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     using the default constructor SystemValueGroup() plus a set and an add.
     **/
    public void Var099()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            svg.setGroupName("Group1");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svName = sv.getGroupName();
            if (svName.equals("Group1"))
                succeeded();
            else
                failed("Incorrect Name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     using the default constructor SystemValueGroup() plus a set and 3 adds.
     **/
    public void Var100()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            svg.add("QMONTH");
            svg.setGroupName("Group1");
            svg.add("QDAY");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            SystemValue sv1 = (SystemValue) svgv.elementAt(1);
            SystemValue sv2 = (SystemValue) svgv.elementAt(2);
            String svName = sv.getGroupName();
            String svName1 = sv1.getGroupName();
            String svName2 = sv2.getGroupName();
            if (svName.equals(svName1)  && svName.equals(svName2) && svName.equals("Group1") )
                succeeded();
            else
                failed("Incorrect Name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     with a null name.
     **/
    public void Var101()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svName = sv.getGroupName();
            if (svName == null) 
                succeeded();
            else
                failed("Name not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     with a null name.
     **/
    public void Var102()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            svg.setGroupName("");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svName = sv.getGroupName();
            if (svName == "") 
                succeeded();
            else
                failed("Name not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     using the constructor SystemValueGroup(system, name, desc)and then reset.
     **/
    public void Var103()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QYEAR");
            svg.setGroupName("MyGroup");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svName =  sv.getGroupName();
            if (svName.equals("MyGroup") )
                succeeded();
            else
                failed("Name incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }


    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var104()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svName =  sv.getGroupName();
            if (svName.equals("Name") )
                succeeded();
            else
                failed("Name incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName on a system value group that was created
     using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var105()
    {
        try
        {
            String[] str = new String[] { "QDATE", "QYEAR", "QMONTH" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svName =  sv.getGroupName();
            if (svName.equals("Name") )
                succeeded();
            else
                failed("Name incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName() on a system value group that was created
     using the constructor SystemValueGroup(system, string, string, int)
     **/
    public void Var106()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, str, "Desc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svName =  sv.getGroupName();
            if (sv.getGroupName().equals(str))
                succeeded();
            else
                failed("Incorrect name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName() on a system value group name that was changed
     after it was constructed.
     **/
    public void Var107()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, str, "Desc", SystemValueList.GROUP_DATTIM);
            svg.setGroupName("I have a new name");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svName =  sv.getGroupName();
            if (svName.equals("I have a new name"))
                succeeded();
            else
                failed("Incorrect name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName() using multiple vectors     **/
    public void Var108()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, str, "Desc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svName = sv.getGroupName();
            svg.setGroupName("I have a new name");
            Vector svgv2 = svg.getSystemValues();
            SystemValue sv2 = (SystemValue) svgv2.elementAt(1);
            String svName2 =  sv2.getGroupName();
            if (svName.equals(str)  && svName2.equals("I have a new name"))
                succeeded();
            else
                failed("Incorrect name.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName() on a system value group name that was 
     set to the empty string ""
     **/
    public void Var109()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "", "Desc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svName =  sv.getGroupName();
            if (svName.equals(""))
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupName().
     Try to call getGroupName() on a system value that was
     not created as part of a group
     **/
    public void Var110()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, str, "Desc", SystemValueList.GROUP_DATTIM);
            SystemValue sv = new SystemValue(pwrSys_, "QYEAR");
            Vector svgv = svg.getSystemValues();
            SystemValue sv1 = (SystemValue) svgv.elementAt(1);
            if (sv.getGroupName() != null)
                failed("Non null group name.");
            else 
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupName().
     Try to call getGroupName() on a system value group that was created
     using the description of an existing system value)
     **/
    public void Var111()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "oldName", "Desc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svName =  sv.getGroupName();
            svg.setGroupName("newName");
            svg.refresh(svgv);
            Vector svgv2 = svg.getSystemValues();
            SystemValue sv2 = (SystemValue) svgv2.elementAt(1);
            String sv2Name =  sv2.getGroupName();
            if (sv2Name.equals("newName") )
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test valid usage of SystemValueGroup::getGroupDescription()
     Try to call getGroupDescription on a system value group that was created
     using the default constructor SystemValueGroup() plus an add.
     **/
    public void Var112()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QYEAR");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svDesc =  sv.getGroupDescription();
            if (svDesc.equals("Desc") )
                succeeded();
            else
                failed("Name incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupName on a system value group that was created
     using the default constructor SystemValueGroup() plus a set and an add.
     **/
    public void Var113()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            svg.setGroupDescription("Group1");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svDesc = sv.getGroupDescription();
            if (svDesc.equals("Group1"))
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription on a system value group that was created
     using the default constructor SystemValueGroup() plus a set and 3 adds.
     **/
    public void Var114()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            svg.add("QMONTH");
            svg.setGroupDescription("Group1");
            svg.add("QDAY");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            SystemValue sv1 = (SystemValue) svgv.elementAt(1);
            SystemValue sv2 = (SystemValue) svgv.elementAt(2);
            String svDesc = sv.getGroupDescription();
            String svDesc1 = sv1.getGroupDescription();
            String svDesc2 = sv2.getGroupDescription();
            if (svDesc.equals(svDesc1)  && svDesc.equals(svDesc2) && svDesc.equals("Group1") )
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription on a system value group that was created
     with a null name.
     **/
    public void Var115()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svDesc = sv.getGroupDescription();
            if (svDesc == null) 
                succeeded();
            else
                failed("Description not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription on a system value group that was created
     with a null name.
     **/
    public void Var116()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            svg.setSystem(pwrSys_);
            svg.add("QYEAR");
            svg.setGroupDescription("");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svDesc = sv.getGroupDescription();
            if (svDesc == "") 
                succeeded();
            else
                failed("Description not null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription on a system value group that was created
     using the constructor SystemValueGroup(system, name, desc)and then reset.
     **/
    public void Var117()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc");
            svg.add("QYEAR");
            svg.setGroupDescription("MyGroup");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svDesc =  sv.getGroupDescription();
            if (svDesc.equals("MyGroup") )
                succeeded();
            else
                failed("Description incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }


    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription on a system value group that was created
     using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var118()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(0);
            String svDesc =  sv.getGroupDescription();
            if (svDesc.equals("Desc") )
                succeeded();
            else
                failed("Description incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription on a system value group that was created
     using the constructor SystemValueGroup(system, name, desc, int).
     **/
    public void Var119()
    {
        try
        {
            String[] str = new String[] { "QDATE", "QYEAR", "QMONTH" };
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "Desc", str);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svDesc =  sv.getGroupDescription();
            if (svDesc.equals("Desc") )
                succeeded();
            else
                failed("Description incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unknown exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription() on a system value group that was created
     using the constructor SystemValueGroup(system, string, string, int)
     **/
    public void Var120()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", str, SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svDesc =  sv.getGroupDescription();
            if (sv.getGroupDescription().equals(str))
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription() on a system value group name that was changed
     after it was constructed.
     **/
    public void Var121()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", str, SystemValueList.GROUP_DATTIM);
            svg.setGroupDescription("I have a new name");
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svDesc =  sv.getGroupDescription();
            if (svDesc.equals("I have a new name"))
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription() using multiple vectors     **/
    public void Var122()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", str, SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svDesc = sv.getGroupDescription();
            svg.setGroupDescription("I have a new name");
            Vector svgv2 = svg.getSystemValues();
            SystemValue sv2 = (SystemValue) svgv2.elementAt(1);
            String svDesc2 =  sv2.getGroupDescription();
            if (svDesc.equals(str)  && svDesc2.equals("I have a new name"))
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription() on a system value group name that was 
     set to the empty string ""
     **/
    public void Var123()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svDesc =  sv.getGroupDescription();
            if (svDesc.equals(""))
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValue::getGroupDescription().
     Try to call getGroupDescription() on a system value that was
     not created as part of a group
     **/
    public void Var124()
    {
        try
        {
            String str = "This is my new group";  
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, str, "Desc", SystemValueList.GROUP_DATTIM);
            SystemValue sv = new SystemValue(pwrSys_, "QYEAR");
            Vector svgv = svg.getSystemValues();
            SystemValue sv1 = (SystemValue) svgv.elementAt(1);
            if (sv.getGroupDescription() != null)
                failed("Non null group description.");
            else 
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test valid usage of SystemValueGroup::getGroupDescription().
     Try to call getGroupDescription() on a system value group that was created
     using the description of an existing system value)
     **/
    public void Var125()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "oldDesc", SystemValueList.GROUP_DATTIM);
            Vector svgv = svg.getSystemValues();
            SystemValue sv = (SystemValue) svgv.elementAt(1);
            String svDesc =  sv.getGroupDescription();
            svg.setGroupDescription("newDesc");
            svg.refresh(svgv);
            Vector svgv2 = svg.getSystemValues();
            SystemValue sv2 = (SystemValue) svgv2.elementAt(1);
            String sv2Name =  sv2.getGroupDescription();
            if (sv2Name.equals("newDesc") )
                succeeded();
            else
                failed("Incorrect description.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test valid usage of SystemValueGroup(AS400, String, String, int).
     Modify the group Vector that is used and see if it changes the underlying master Vector.
     **/
    public void Var126()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(pwrSys_, "Name", "oldDesc", SystemValueList.GROUP_ALC);
            if (!svg.contains("QMONTH"))
            {
                svg.add("QMONTH");
                SystemValueGroup svg2 = new SystemValueGroup(pwrSys_, "Name", "oldDesc", SystemValueList.GROUP_ALC);
                if (!svg2.contains("QMONTH") && svg.contains("QMONTH"))
                {
                    succeeded();
                }
                else
                {
                    failed("New group contains old value.");
                }
            }
            else
            {
                failed("Group ALC contains QMONTH.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


}



