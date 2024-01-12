///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DACtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;


import java.io.FileOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;

import test.Testcase;

import com.ibm.as400.access.IllegalPathNameException;

/**
 Testcase DACtorTestcase.  Test variations for the methods:
 <ul>
 <li>CharacterDataArea(AS400, String)
 <li>DecimalDataArea(AS400, String)
 <li>LocalDataArea(AS400)
 <li>LogicalDataArea(AS400, String)
 </ul>
 **/
public class DACtorTestcase extends Testcase
{
    /**
     Construct a character data area passing a null for the system.
     An NullPointerException should be thrown.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(null, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTAARA");
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
     * Construct a decimal data area passing a null for the system.
     * An NullPointerException should be thrown.
     **/
    public void Var002()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(null, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTAARA");
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
     * Construct a local data area passing a null for the system.
     * An NullPointerException should be thrown.
     **/
    public void Var003()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(null);
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
     * Construct a logical data area passing a null for the system.
     * An NullPointerException should be thrown.
     **/
    public void Var004()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(null, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTAARA");
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
     * Construct a character data area passing a null for the pathname.
     * An NullPointerException should be thrown.
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a decimal data area passing a null for the pathname.
     * An NullPointerException should be thrown.
     **/
    public void Var006()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a logical data area passing a null for the pathname.
     * An NullPointerException should be thrown.
     **/
    public void Var007()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a character data area passing in an invalid path name.
     * An IllegalPathNameException should be thrown.
     **/
    public void Var008()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QSYS.LIB/TESTAREA.DTAARA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a decimal data area passing in an invalid path name.
     * An IllegalPathNameException should be thrown.
     **/
    public void Var009()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QSYS.LIB/TESTAREA.DTAARA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a logical data area passing in an invalid path name.
     * An IllegalPathNameException should be thrown.
     **/
    public void Var010()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QSYS.LIB/TESTAREA.DTAARA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a character data area passing in an invalid object type.
     * An IllegalPathNameException should be thrown.
     **/
    public void Var011()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException", IllegalPathNameException.OBJECT_TYPE_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a decimal data area passing in an invalid object type.
     * An IllegalPathNameException should be thrown.
     **/
    public void Var012()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException", IllegalPathNameException.OBJECT_TYPE_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Construct a logical data area passing in an invalid object type.
     * An IllegalPathNameException should be thrown.
     **/
    public void Var013()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException", IllegalPathNameException.OBJECT_TYPE_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     * Successful construction of a character data area using ctor with parms.
     **/
    public void Var014()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTAARA");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a decimal data area using ctor with parms.
     **/
    public void Var015()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTAARA");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a local data area using ctor with parms.
     **/
    public void Var016()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a logical data area using ctor with parms.
     **/
    public void Var017()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/TESTAREA.DTAARA");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a character data area using default ctor.
     **/
    public void Var018()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a decimal data area using default ctor.
     **/
    public void Var019()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a local data area using default ctor.
     **/
    public void Var020()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Successful construction of a logical data area using default ctor.
     **/
    public void Var021()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
