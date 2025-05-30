///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalGroupCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.SystemValueGroup;
import com.ibm.as400.access.SystemValueList;

import test.Testcase;

/**
 Testcase SysvalGroupCtorTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>SystemValueGroup()
 <li>SystemValueGroup(AS400, String, String)
 <li>SystemValueGroup(AS400, String, String, int)
 <li>SystemValueGroup(AS400, String, String, String[])
 </ul>
 **/
public class SysvalGroupCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SysvalGroupCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SysvalTestDriver.main(newArgs); 
   }
    /**
     Test default constructor: SystemValueGroup().
     Successful construction of a system value group using default ctor.
     **/
    public void Var001()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup();
            assertCondition(true, "created "+svg); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String).
     **/
    public void Var002()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description");
            assertCondition(true, "created "+svg); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String).
     Construct a system value group passing a null for the system.
     An NullPointerException should be thrown.
     **/
    public void Var003()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(null, "NAME", "DESC");
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String).
     Construct a system value group passing a null for String 1.
     A NullPointerException should be thrown.
     **/
    public void Var004()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, null, "DESC");
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "groupName");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String).
     Construct a system value group passing a null for String 2.
     A NullPointerException should be thrown.
     **/
    public void Var005()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "NAME", null);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "groupDescription");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String).
     Construct a system value group passing a name (String 1) with length 0.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var006()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "","DESC");
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String).
     Construct a system value group passing a description (String 2) with length 0.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var007()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "NAME","");
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int).
     **/
    public void Var008()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_ALL);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing a null for the system.
     An NullPointerException should be thrown.
     **/
    public void Var009()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(null, "NAME", "DESC", SystemValueList.GROUP_ALL);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing a null for String 1.
     A NullPointerException should be thrown.
     **/
    public void Var010()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, null, "DESC", SystemValueList.GROUP_ALL);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "groupName");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing a null for String 2.
     A NullPointerException should be thrown.
     **/
    public void Var011()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "NAME", null, SystemValueList.GROUP_ALL);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "groupDescription");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing a name (String 1) with length 0.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var012()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "","DESC", SystemValueList.GROUP_ALL);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing a description (String 2) with length 0.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var013()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "NAME","", SystemValueList.GROUP_ALL);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_ALC.
     **/
    public void Var014()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_ALC);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_DATTIM.
     **/
    public void Var015()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_DATTIM);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_EDT.
     **/
    public void Var016()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_EDT);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_LIBL.
     **/
    public void Var017()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_LIBL);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_MSG.
     **/
    public void Var018()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_MSG);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_NET.
     **/
    public void Var019()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_NET);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_SEC.
     **/
    public void Var020()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_SEC);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_STG.
     **/
    public void Var021()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_STG);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, int) with int value of SystemValueList.GROUP_SYSCTL.
     **/
    public void Var022()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", SystemValueList.GROUP_SYSCTL);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing an int with an invalid value (98766).
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var023()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Desc", 98766);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "group", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing int with an invalid value (-1).
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var024()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Desc", -1);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "group", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, String[]) with String[] value of "QDATE", "QTIME".
     **/
    public void Var025()
    {
        try
        {
            String[] str = new String[] { "QDATE", "QTIME" };
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", str);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, String[]) with String[] value of "QDATE".
     **/
    public void Var026()
    {
        try
        {
            String[] str  = new String[] {"QDATE"};
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", str);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Successful construction of a system value group using ctor SystemValueGroup(AS400, String, String, String[]) with String[] value of "QDATE", "QTIME", "QMONTH".
     **/
    public void Var027()
    {
        try
        {
            String[] str = new String[] { "QDATE", "QTIME", "QMONTH" };
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name", "Description", str);
            assertCondition(true,"svg="+svg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, int).
     Construct a system value group passing a null for system.
     A NullPointerException should be thrown.
     **/
    public void Var028()
    {
        try
        {
            String[] str = new String[] { "QDATE", "QTIME" };
            SystemValueGroup svg = new SystemValueGroup(null, "NAME", "Desc", str);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Construct a system value group passing a null for String[].
     A NullPointerException should be thrown.
     **/
    public void Var029()
    {
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "NAME", "Desc", null);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "names");
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Construct a system value group passing a String[]) with length 0.
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var030()
    {
        try
        {
            String[] str = new String[] {""};
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "NAME","Desc", str);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Construct a system value group passing a String[] with an invalid value ("TRASH").
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var031()
    {
        try
        {
            String[] str = new String[] { "TRASH" };
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name","Desc", str);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Construct a system value group passing a String[] with an invalid value "QDATE","TRASH".
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var032()
    {
        String[] str = new String[] { "QDATE","TRASH" };
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name","Desc", str);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "TRASH", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }

    /**
     Test constructor: SystemValueGroup(AS400, String, String, String[]).
     Construct a system value group passing a String[] with an invalid value "QDATE", "".
     An ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var033()
    {
        String[] str = new String[] { "QDATE","" };
        try
        {
            SystemValueGroup svg = new SystemValueGroup(systemObject_, "Name","Desc", str);
            failed("No exception."+svg);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "", ExtendedIllegalArgumentException.FIELD_NOT_FOUND);
        }
    }
}
