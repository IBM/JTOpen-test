///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DATypeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.IllegalObjectTypeException;

/**
 Testcase DATypeTestcase.
 Test variations for accessing an object of one type with an
 object of another type.
 **/
public class DATypeTestcase extends Testcase
{
    public static String path = "/QSYS.LIB/DATEST.LIB/TYPETEST.DTAARA";

    /**
     Create a character data area.
     Try to access it using a DecimalDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var001()
    {
        CharacterDataArea da1 = new CharacterDataArea(systemObject_, path);
        try
        {
            da1.create();
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, path);
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_CHARACTER))
                succeeded();
            else
                failed(e, "Wrong exception info.");
            try
            {
                da1.delete();
            }
            catch (Exception f) {}
        }
    }


    /**
     Create a character data area.
     Try to access it using a LogicalDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var002()
    {
        CharacterDataArea da1 = new CharacterDataArea(systemObject_, path);
        try
        {
            da1.create();
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, path);
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_CHARACTER))
                succeeded();
            else
                failed(e, "Wrong exception info.");
            try
            {
                da1.delete();
            }
            catch (Exception f) {}
        }
    }


    /**
     Create a decimal data area.
     Try to access it using a CharacterDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var003()
    {
        DecimalDataArea da1 = new DecimalDataArea(systemObject_, path);
        try
        {
            da1.create();
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, path);
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_DECIMAL))
                succeeded();
            else
                failed(e, "Wrong exception info.");
            try
            {
                da1.delete();
            }
            catch (Exception f) {}
        }
    }


    /**
     Create a decimal data area.
     Try to access it using a LogicalDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var004()
    {
        DecimalDataArea da1 = new DecimalDataArea(systemObject_, path);
        try
        {
            da1.create();
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, path);
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_DECIMAL))
                succeeded();
            else
                failed(e, "Wrong exception info.");
            try
            {
                da1.delete();
            }
            catch (Exception f) {}
        }
    }


    /**
     Create a logical data area.
     Try to access it using a CharacterDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var005()
    {
        LogicalDataArea da1 = new LogicalDataArea(systemObject_, path);
        try
        {
            da1.create();
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, path);
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_LOGICAL))
                succeeded();
            else
                failed(e, "Wrong exception info.");
            try
            {
                da1.delete();
            }
            catch (Exception f) {}
        }
    }


    /**
     Create a logical data area.
     Try to access it using a DecimalDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var006()
    {
        LogicalDataArea da1 = new LogicalDataArea(systemObject_, path);
        try
        {
            da1.create();
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, path);
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_LOGICAL))
                succeeded();
            else
                failed(e, "Wrong exception info.");
            try
            {
                da1.delete();
            }
            catch (Exception f) {}
        }
    }

    /**
     Try to access a local data area using a DecimalDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var007()
    {
        try
        {
            LocalDataArea da1 = new LocalDataArea(systemObject_);
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/          .LIB/*LDA.DTAARA");
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_CHARACTER))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Try to access a local data area using a LogicalDataArea object.
     An IllegalObjectTypeException should be thrown.
     **/
    public void Var008()
    {
        try
        {
            LocalDataArea da1 = new LocalDataArea(systemObject_);
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/          .LIB/*LDA.DTAARA");
            da2.read();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_AREA_CHARACTER))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Try to access a local data area using a CharacterDataArea object.
     The method should succeed.
     **/
    public void Var009()
    {
        try
        {
            LocalDataArea da1 = new LocalDataArea(systemObject_);
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/          .LIB/*LDA.DTAARA");
            da2.read();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
