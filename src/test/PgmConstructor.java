///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmConstructor.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Hashtable;
import java.util.Vector;
import com.ibm.as400.access.*;

/**
 Testcase PgmConstructor.
 <ul>
 <li>1-4 are combinations of ctor (w or w/o command string) followed by getSystem or getProgram
 <li>5-8 are testing for null on ctors
 <li>9-10 are combinations of ctor (w or w/o command string) followed by getMessageList
 <li>11-12 are combinations of ctor (w or w/o command string) followed by getParameterList
 <li>13 tests default ctor for default values and tests setSystem, setProgram and setParameterList
 <li>14-18 are testing for null on setSystem, setProgram and setParameterList
 </ul>
 **/
public class PgmConstructor extends Testcase
{
    String goodPgm_ = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";

    /**
     Create a valid program call object.  Verify getSystem.
     **/
    public void Var001()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            AS400 sys_ = pgm.getSystem();
            if (sys_ == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("System not set");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a valid program call object with a program.
     Verify getSystem.
     **/
    public void Var002()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, new ProgramParameter[3]);
            AS400 sys_ = pgm.getSystem();
            if (sys_ == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("System not set");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a valid program call object.
     Verify getProgram returns empty string.
     **/
    public void Var003()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            String str = pgm.getProgram();
            if (str!=null && str.length()==0)
            {
                succeeded();
            }
            else
            {
                failed("Program set");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a valid program call object with a program.  Verify getProgram.
     **/
    public void Var004()
    {
        try
        {
            String pgmStr = goodPgm_;
            ProgramCall pgm = new ProgramCall(systemObject_, pgmStr, new ProgramParameter[3]);
            String str = pgm.getProgram();
            if (str == pgmStr)
            {
                succeeded();
            }
            else
            {
                failed("Program not set");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test for null system parameter.  An NullPointerException should be thrown.
     **/
    public void Var005()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(null);
            failed("no exception thrown for null pointer");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test for null system parameter and valid program.
     A NullPointerException should be thrown.
     **/
    public void Var006()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(null, goodPgm_, new ProgramParameter[3]);
            failed("no exception thrown for null pointer");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test for null program parameter and valid system and parameter list.
     A NullPointerException should be thrown.
     **/
    public void Var007()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, null, new ProgramParameter[0]);
            failed("no exception thrown for null pointer");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test for null parameter list and valid system and program.
     Parameter list should be 0 length.
     **/
    public void Var008()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, "/QSYS.LIB/LIB.LIB/PROG.PGM", null);

        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }

        }
    }

    /**
     Get message list after constructing program call object.
     Should throw NullPointerException.
     **/
    public void Var009()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            AS400Message[] msglist = pgm.getMessageList();
            if (msglist==null  ||  msglist.length!=0)
                failed("Message list not empty");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get message list after constructing program call object with a program.
     Should return null.
     **/
    public void Var010()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, new ProgramParameter[3]);
            AS400Message[] msglist = pgm.getMessageList();
            if (msglist==null  ||  msglist.length!=0)
                failed("Message list not empty");
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get parameter list after constructing program call object.
     Should be empty.
     **/
    public void Var011()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            ProgramParameter[] parmlist = pgm.getParameterList();
            if (parmlist!=null && parmlist.length==0)
                succeeded();
            else
                failed("Parameter list not empty");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get parameter list after constructing program call object with a program.
     Should return null.
     **/
    public void Var012()
    {
        try
        {
            ProgramParameter[] parmList = new ProgramParameter[3];
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, parmList);
            ProgramParameter[] parmlist = pgm.getParameterList();
            if (parmlist==parmList)
                succeeded();
            else
                failed("Parameter list not empty");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Construct with default ctor,
     verify default values,
     and verify setSystem, setProgram, setParameterList.
     **/
    public void Var013()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            if (pgm.getSystem()!=null)
            {
                failed("system not null");return;
            }
            if (pgm.getProgram().length() != 0)
            {
                failed("program name not empty");return;
            }
            if (pgm.getParameterList().length != 0)
            {
                failed("parameter list not empty");return;
            }
            AS400Message[] msglist = pgm.getMessageList();
            if (msglist.length!=0)
            {
                failed("message list length " + msglist.length + "not 0");return;
            }

            // test setSystem
            pgm.setSystem( systemObject_ );
            if (pgm.getSystem()!=systemObject_)
            {
                failed("system not set");return;
            }
            // test setProgram
            pgm.setProgram( goodPgm_ );
            if (pgm.getProgram() != goodPgm_)
            {
                failed("Program not set");return;
            }
            // test setParameterList
            ProgramParameter[] parmList = new ProgramParameter[3];
            pgm.setParameterList( parmList );
            if (pgm.getParameterList() != parmList)
            {
                failed("Parameter list not set");return;
            }
            // test setProgram+parmlist
            String pgm2 = "/QSYS.LIB/W95LIB.LIB/PROG5.PGM";
            parmList = new ProgramParameter[5];
            pgm.setProgram(pgm2, parmList);
            if (pgm.getProgram() != pgm2)
            {
                failed("Program not set");return;
            }
            else if (pgm.getParameterList() != parmList)
            {
                failed("Parameter list not set");return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test for NullPointerException on setSystem.
     **/
    public void Var014()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setSystem(null);
            failed("no exception thrown for null pointer");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test for NullPointerException on setProgram
     **/
    public void Var015()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setProgram(null);
            failed("no exception thrown for null pointer");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test setParameterList( null ) after setting it not empty.
     Should create an empty list.
     **/
    public void Var016()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setParameterList(new ProgramParameter[3]);
            if (pgm.getParameterList().length != 3)
            {
                failed("parameter list not set"); return;
            }
            pgm.setParameterList(null);

        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }

        }
    }

    /**
     Test for NullPointerException on setProgram first parameter.
     **/
    public void Var017()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setProgram(null, new ProgramParameter[3]);
            failed("no exception thrown for null pointer");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test null on setProgram second parameter
     after setting it not empty.
     Should create an null list.
     **/
    public void Var018()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setParameterList(new ProgramParameter[3]);
            if (pgm.getParameterList().length != 3)
            {
                failed("parameter list not set"); return;
            }
            pgm.setProgram("/QSYS.LIB/W95LIB.LIB/PROG3.PGM", null);

        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }

        }
    }

    /**
     Test illegal pathname on setProgram
     Should throw IllegalPathNameException.
     **/
    public void Var019()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setProgram( "/QSYS.LIB/W95LIB.LIB/PROG3.xxx");
            failed("no exception thrown for IllegalPathNameException");
        }
        catch (Exception e)
        {
            assertExceptionIs( e, "IllegalPathNameException",
                               IllegalPathNameException.OBJECT_TYPE_NOT_VALID );
        }
    }

    /**
     Test illegal pathname on setProgram with 2 parameters.
     Should throw IllegalPathNameException.
     **/
    public void Var020()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setProgram( "/QSYS.LIB/W95LIB.LIB/PROG3.xxx", null);
            failed("no exception thrown for IllegalPathNameException");
        }
        catch (Exception e)
        {
            assertExceptionIs( e, "IllegalPathNameException",
                               IllegalPathNameException.OBJECT_TYPE_NOT_VALID );
        }
    }

    /**
     Test for more than 35 parameters on setParameterList.
     Should throw ExtendedIllegalArgumentException.
     **/
    public void Var021()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setParameterList( new ProgramParameter[36] );
            failed("no exception thrown for 36 parameters");
        }
        catch (Exception e)
        {
            assertExceptionIs( e, "ExtendedIllegalArgumentException",
                               ExtendedIllegalArgumentException.LENGTH_NOT_VALID );
        }
    }

    /**
     Test for more than 35 parameters on setProgram.
     Should throw ExtendedIllegalArgumentException.
     **/
    public void Var022()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setProgram( "/QSYS.LIB/W95LIB.LIB/PROG3.PGM",
                            new ProgramParameter[36]);
            failed("no exception thrown for 36 parameters");
        }
        catch (Exception e)
        {
            assertExceptionIs( e, "ExtendedIllegalArgumentException",
                               ExtendedIllegalArgumentException.LENGTH_NOT_VALID );
        }
    }
}
