///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmRunTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyVetoException;
import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCodeParameter;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.MessageFile;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.resource.RJob;

/**
 Testcase PgmRunTestcase.
 Note to tester: When running on the iSeries, this testcase has better success
 if you have jt400.jar (rather than jt400Native.jar) on the classpath.
 <ul>
 <li> 1-4 are combinations of ctor (w or w/o program) followed by run (w or w/o program).
 <li> 5-6 are combinations of ctor (w or w/o program) followed setProgram and run.
 <li> 7-10 are combinations of ctor (w or w/o program) followed by run with null as 1st or 2nd parm.
 <li> 11-12 tests for program not found
 <li> 13 test a program with wrong number of parameters
 <li> 14-15 test programs taking 25 and 35 parameters
 <li> 16-18 test for running when one of parameter list, program name, system, has not been set.
 <li> 19-20 test default ctor followed by setSystem/setCommand and run
 </ul>
 **/
public class PgmRunTestcase extends Testcase
{
    String goodPgm_ = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";
    String Prog25_ = "/QSYS.LIB/W95LIB.LIB/PROG25.PGM";
    String Prog35_ = "/QSYS.LIB/W95LIB.LIB/PROG35.PGM";
    String Prog36_ = "/QSYS.LIB/W95LIB.LIB/PROG36.PGM";
    String Prog255_ = "/QSYS.LIB/W95LIB.LIB/PROG255.PGM";
    // a normal program for timeout test
    String PgmTimeout1_ = "/QSYS.LIB/W95LIB.LIB/PGMTO1.PGM";
    // a program which will sleep 10s
    String PgmTimeout2_ = "/QSYS.LIB/W95LIB.LIB/PGMTO2.PGM";
    // a program which will is always hanging
    String PgmTimeout3_ = "/QSYS.LIB/W95LIB.LIB/PGMTO3.PGM";
    String result3 = "Testing 3 Parameters";
    ProgramParameter[] parmlist_;
    byte[] data0;
    byte[] data1;
    ProgramParameter parm0, parm1, parm2;
    AS400Bin2 int16 = new AS400Bin2();


    String userHang = "PGMHang";
    String pwdHang  = "PGMHang1";


    static final boolean DEBUG = false;



    /**
    Performs setup needed before running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void setup ()  throws Exception {

	String command = ""; 
	String cmdCrtUsr = "CRTUSRPRF USRPRF("+userHang+") PASSWORD("+pwdHang+") USRCLS(*SECADM) SPCAUT(*ALLOBJ)  TEXT('Toolbox testing profile')";
	System.out.println("command = " + cmdCrtUsr);
	CommandCall cc = new CommandCall(pwrSys_);
	try {
	    command = cmdCrtUsr; 
	    boolean result = cc.run(command);
	    if (result)
		System.out.println("user profile PGMHang is successfully created. ");
	    else{
		System.out.println("Create failed, adjusting existing profile"); 
               // Profile not created.. Assume it is already there and adjust it 
		 command = "CHGUSRPRF USRPRF("+userHang+") PASSWORD(GARBAGE)";
		 result = cc.run(command);
		 if (result) { 
		     command = "CHGUSRPRF USRPRF("+userHang+") PASSWORD("+pwdHang+")  STATUS(*ENABLED)   ";
		     result = cc.run(command);
		     if (!result) {
			 System.out.println("Command failed: "+command); 
		     }
		 } else {
			 System.out.println("Command failed: "+command); 
		 } 
	    }
	} catch (Exception e) {
	    System.out.println("Warning: Command Failed :"+command); 
	    e.printStackTrace();
	}


    }


    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
      throws Exception
    {
	String cmdDltUsr = "DLTUSRPRF USRPRF("+userHang+")";
	try {

	    CommandCall cc = new CommandCall(pwrSys_);

	    boolean result = cc.run(cmdDltUsr);
	    if (result)
		System.out.println("user profile PGMHang is successfully deleted. ");
	    else
		System.out.println("can't delete user profile using "+cmdDltUsr);
	} catch (Exception e) {
	    e.printStackTrace(); 
	    failed("can't delete user profile using "+cmdDltUsr);
	}

    }

    private final boolean isRunningNatively()
    {
      if (onAS400_ /*&& isNative_ && isLocal_*/ && systemObject_.canUseNativeOptimizations())
      {
        return true;
      }
      else return false;
    }

    private final boolean isRunningNativelyAndThreadsafe()
    {
      if (!isRunningNatively()) return false;
      String prop = getProgramCallThreadSafetyProperty(); // we only care about ProgramCall, not CommandCall
      if (prop != null && prop.equals("true"))
      {
        return true;
      }
      else return false;
    }

    ProgramParameter[] buildParms()
    {
        parmlist_ = new ProgramParameter[3];

        data0 = new byte[5];
        data0[0] = 7;
        data0[1] = 6;
        parmlist_[0] = parm0 = new ProgramParameter(data0, 500);

        data1 = new byte[2];
        data1[0] = (byte)4;
        data1[1] = (byte)2;
        parmlist_[1] = parm1 = new ProgramParameter(data1);
        parmlist_[2] = parm2 = new ProgramParameter(2);

        return parmlist_;
    }


    // Test running successfull program with constructor and run methods.
    void verifyProg3(ProgramCall pgm)
    {
        // Verify no messages returned.
        AS400Message[] msglist = pgm.getMessageList();
        if (msglist.length != 0)
        {
            failed("message received " + msglist[0]); return;
        }

        // Verify parameter list.
        ProgramParameter[] parmlist = pgm.getParameterList();
        if (parmlist.length != 3)
        {
            failed("parameter list size changed" ); return;
        }
        if (parmlist[0] != parm0)
        {
            failed("parameter 0 changed" ); return;
        }
        if (parmlist[1] != parm1)
        {
            failed("parameter 1 changed" ); return;
        }
        if (parmlist[2] != parm2)
        {
            failed("parameter 2 changed" ); return;
        }

        // Verify each parm.
        // parm 0 still inout.
        if (parmlist[0].getInputData() != data0)
        {
            failed("parameter 0 data changed" ); return;
        }
        if (parmlist[0].getOutputDataLength() != 500)
        {
            failed("parameter 0 output data length changed"
                   + parmlist[0].getOutputDataLength()); return;
        }
        // parm 0 result string.
        AS400Text xlater = new AS400Text( result3.length(), 37, systemObject_ );
        String retp0 = (String)xlater.toObject(pgm.getParameterList()[0].getOutputData(), 0);
        if (!retp0.equals("Testing 3 Parameters"))
        {
            failed("parameter 0 wrong value " + retp0 ); return;
        }

        // parm 1 still input.
        if (parmlist[1].getInputData() != data1)
        {
            failed("parameter 1 data changed" ); return;
        }
        if (parmlist[1].getOutputDataLength() != 0)
        {
            failed("parameter 1 output data length set" + parmlist[1].getOutputDataLength()); return;
        }
        if (parmlist[1].getOutputData() != null && parmlist[1].getOutputData().length != 0)
        {
            failed("parameter 1 output data set" ); return;
        }

        // parm 2 still output.
        if (parmlist[2].getInputData() != null && parmlist[2].getInputData().length != 0)
        {
            failed("parameter 2 data changed" ); return;
        }
        if (parmlist[2].getOutputDataLength() != 2)
        {
            failed("parameter 2 output data length changed" + parmlist[2].getOutputDataLength()); return;
        }
        // parm 2 = parm1+1
        if (1027 != int16.toShort( pgm.getParameterList()[2].getOutputData(), 0))
        {
            failed("parameter 2 wrong value " + int16.toShort(pgm.getParameterList()[2].getOutputData(), 0)); return;
        }

        // Verify system not changed.
        if (pgm.getSystem()!=systemObject_)
        {
            failed("system changed " +pgm.getSystem().getSystemName() ); return;
        }

        // Verify program not changed.
        if (pgm.getProgram() != goodPgm_)
        {
            failed("program changed " +pgm.getProgram() ); return;
        }
        succeeded();
    }

    /**
     Create a program object,
     run with program and parameters,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var001()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            if (!pgm.run(goodPgm_, buildParms() ))
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object with a program and parameters,
     run it,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var002()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
            if (!pgm.run())
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object with a program and parameters,
     run it with a program and parameters,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var003()
    {
        try
        {
            ProgramParameter[] parmlist0 = new ProgramParameter[2];
            ProgramCall pgm = new ProgramCall(systemObject_, Prog25_, parmlist0 );
            if (!pgm.run( goodPgm_, buildParms() ))
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object, run it,
     verify ExtendedIllegalStateException thrown and system not changed.
     **/
    public void Var004()
    {
        ProgramCall pgm = new ProgramCall(systemObject_ );
        try
        {
            pgm.run();
            failed("Exception not thrown"); return;
        }
        catch (Exception e)
        {
            if (!exceptionIs(e, "ExtendedIllegalStateException",ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                failed(e, "Incorrect exception"); return;
            }
            // Verify system not changed
            if (pgm.getSystem()!=systemObject_)
            {
                failed("system changed " +pgm.getSystem().getSystemName() ); return;
            }

            succeeded();
        }
    }

    // Test running successfull program with a setProgram before the run.

    /**
     Create a program object with the system,
     set the program and parameter list,
     then invoke the run method
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var005()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.setProgram(goodPgm_, buildParms());
            if (!pgm.run())
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object with a program and parameters,
     set the program and parameters,
     then run it,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var006()
    {
        try
        {
            ProgramParameter[] parmlist0 = new ProgramParameter[2];
            ProgramCall pgm = new ProgramCall(systemObject_, Prog25_, parmlist0 );
            pgm.setProgram( goodPgm_, buildParms() );
            if (!pgm.run())
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    // Test for NullPointerExceptions for nulls on run

    /**
     Create a program object with the system, then invoke the run method
     with a null program but a valid parameter list.
     A NullPointerException should be generated.
     **/
    public void Var007()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.run( null, buildParms() );
            failed("no exception generated");
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
     Create a program object with the system, then invoke the run method
     with a program but a null parameter list.
     A NullPointerException should be generated.
     **/
    public void Var008()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.run(goodPgm_, null);

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
     Create a program object with the system and program and parameter list,
     then invoke the run method
     with a null program but a valid parameter list.
     A NullPointerException should be generated.
     **/
    public void Var009()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms());
            pgm.run(null, buildParms() );
            failed("no exception generated");
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
     Create a program object with the system and program and parameter list,
     then invoke the run method
     A NullPointerException should be generated.
     **/
    public void Var010()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms());
            pgm.run(goodPgm_, null);

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

    // $$$ null on set *4

    // invalid program name

    /**
     Run an invalid program and make sure an exception is returned.
     **/
    public void Var011()
    {
        ProgramCall pgm = new ProgramCall(systemObject_);;
        try
        {
            pgm.run("/QSYS.LIB/W95LIB.LIB/xxxx.PGM", buildParms());
            failed("program returned");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (pgm==null
                    ||  pgm.getMessageList()==null
                    ||  pgm.getMessageList().length != 1
                    ||  pgm.getMessageList()[0].getText().trim().length() == 0)
                {
                    failed( "incorrect number of messages returned" ); return;
                }
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Run an invalid program and make sure an exception is returned.
     **/
    public void Var012()
    {
        ProgramCall pgm=null; // = new ProgramCall(systemObject_, "/QSYS.LIB/W95LIB.LIB/xxxx.PGM", buildParms());
        try
        {
            pgm = new ProgramCall(systemObject_, "/QSYS.LIB/W95LIB.LIB/xxxx.PGM", buildParms());
            pgm.run();
            failed("program returned");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (pgm==null
                    ||  pgm.getMessageList()==null
                    ||  pgm.getMessageList().length != 1
                    ||  pgm.getMessageList()[0].getText().trim().length() == 0)
                {
                    failed( "incorrect number of messages returned" ); return;
                }
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    // error calling program

    /**
     Run a program that returns a message (wrong number of parameters).
     **/
    public void Var013()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.run(Prog25_, buildParms() );
            if (pgm!=null
                && pgm.getMessageList()!=null
                &&  pgm.getMessageList().length == 1
                &&  pgm.getMessageList()[0].getText().trim().length() > 0)
            {
                succeeded();
            }
            else
            {
                failed("incorrect number of messages returned");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    // programs with 25 and 35 parameters

    boolean compare( byte[] x, byte[] y)
    {
        if (x.length != y.length)
            return false;
        for (int i=0; i<x.length; i++)
            if (x[i]!=y[i])
                return false;
        return true;
    }

    /**
     Run a program with 25 parameters and check output parameters.
     **/
    public void Var014()
    {
        try
        {
            parmlist_ = new ProgramParameter[25];

            // P0 = sum of P1 to P9
            parmlist_[0] = new ProgramParameter( int16.toBytes((short)1), 2 );

            for (short i=1; i<10; i++)
            {
                parmlist_[i] = new ProgramParameter( int16.toBytes(i) );
            }

            // P24 = sum of P10 to P23
            parmlist_[24] = new ProgramParameter( 700 );
            byte[] strret = new byte[700];
            int strpos = 0;
            for (int i=10; i<24; i++)
            {
                byte[] str = new byte[50];
                for (int j=1; j<49; j++)
                    str[j] = (byte)(i+65);
                str[0] = (byte)'[';
                str[49] = (byte)']';
                parmlist_[i] = new ProgramParameter( str );
                System.arraycopy(str, 0, strret, strpos, 50);
                strpos += 50;
            }

            ProgramCall pgm = new ProgramCall(systemObject_);
            if (pgm.run(Prog25_, parmlist_))
            {
                if (compare(
                            strret,
                            pgm.getParameterList()[24].getOutputData()
                            )  &&
                    (int16.toShort(
                                   pgm.getParameterList()[0].getOutputData(), 0)
                     == 46))
                    succeeded();
                else
                    failed( "incorrect parameter returned" );
            }
            else
            {
                failed("program returned false");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Run a program with 35 parameters and check output parameters.
     **/
    public void Var015()
    {
        try
        {
            parmlist_ = new ProgramParameter[35];

            // P0 = sum of P1 to P9
            parmlist_[0] = new ProgramParameter( int16.toBytes((short)1), 2 );

            for (short i=1; i<10; i++)
            {
                parmlist_[i] = new ProgramParameter( int16.toBytes(i) );
            }

            // P34 = sum of P10 to P33
            parmlist_[34] = new ProgramParameter( 240 );
            byte[] strret = new byte[240];
            int strpos = 0;
            for (int i=10; i<34; i++)
            {
                byte[] str = new byte[10];
                for (int j=1; j<9; j++)
                    str[j] = (byte)(i+65);
                str[0] = (byte)'[';
                str[9] = (byte)']';
                parmlist_[i] = new ProgramParameter( str );
                System.arraycopy(str, 0, strret, strpos, 10);
                strpos += 10;
            }

            ProgramCall pgm = new ProgramCall(systemObject_);
            if (pgm.run(Prog35_, parmlist_))
            {
            	System.out.println(pgm.getParameterList()[34].getOutputDataLength());
            	System.out.println(pgm.getParameterList()[34].getParameterType());
            for(int i = 0; i < pgm.getParameterList()[34].getOutputData().length; i++ ) {
            	System.out.print( (pgm.getParameterList()[34].getOutputData()[i]-65) + ", ");
            }
                if (compare(
                            strret,
                            pgm.getParameterList()[34].getOutputData()
                            ) &&
                    (int16.toShort(
                                   pgm.getParameterList()[0].getOutputData(), 0)
                     == 46))
                    succeeded();
                else
                    failed( "incorrect parameter returned" );
            }
            else
            {
                failed("program returned false");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    // property not set
    /**
     Create a program call object with a system,
     set program name but not parameter list,
     run it,
     verify no exception.
     **/
    public void Var016()
    {
        ProgramCall pgm = new ProgramCall(systemObject_ );
        try
        {
            pgm.setProgram( "/QSYS.LIB/W95LIB.LIB/PROG0.PGM" );
            pgm.run();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program call object with a system,
     set parameter list but not program name,
     run it,
     verify ExtendedIllegalStateException thrown and system not changed.
     **/
    public void Var017()
    {
        ProgramCall pgm = new ProgramCall(systemObject_ );
        try
        {
            pgm.setParameterList( buildParms() );
            pgm.run();
            failed("Exception not thrown"); return;
        }
        catch (Exception e)
        {
            if (!exceptionIs(e, "ExtendedIllegalStateException",ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                failed(e, "Incorrect exception"); return;
            }
            // Verify system not changed
            if (pgm.getSystem()!=systemObject_)
            {
                failed("system changed " +pgm.getSystem().getSystemName() ); return;
            }

            succeeded();
        }
    }

    /**
     Create a program call object without a system,
     set parameter list and program name,
     run it,
     verify ExtendedIllegalStateException thrown and system not changed.
     **/
    public void Var018()
    {
        ProgramCall pgm = new ProgramCall();
        try
        {
            pgm.setProgram( goodPgm_, buildParms() );
            pgm.run();
            failed("Exception not thrown"); return;
        }
        catch (Exception e)
        {
            if (!exceptionIs(e, "ExtendedIllegalStateException",ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                failed(e, "Incorrect exception"); return;
            }
            // Verify system not changed
            if (pgm.getSystem()!=null)
            {
                failed("system changed " +pgm.getSystem().getSystemName() ); return;
            }

            succeeded();
        }
    }

    /**
     Create a program object,
     set the system,
     run with program and parameters,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed,
     and system cannot be set.
     **/
    public void Var019()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setSystem(systemObject_);
            if (!pgm.run(goodPgm_, buildParms() ))
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            // slip in a test of setSystem ...
            try {
                pgm.setSystem( new AS400() );
                failed( "system reset");
            } catch (Exception e)
            {
                if (!exceptionIs(e, "ExtendedIllegalStateException",ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                {
                    failed(e, "Incorrect exception"); return;
                }
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object,
     set the system,
     set the program and parameters,
     run program,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var020()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            pgm.setSystem(systemObject_);
            pgm.setProgram( goodPgm_, buildParms() );
            if (!pgm.run())
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Create a program object,
     set program name,
     add parameters one at a time,
     run,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var021()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.setProgram(goodPgm_);
            buildParms();
            pgm.addParameter(parm0);
            pgm.addParameter(parm1);
            pgm.addParameter(parm2);
            if (!pgm.run())
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object,
     run with program and parameters,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var022()
    {
        try
        {
            //AS400 ltd = new AS400( systemObject_.getSystemName(),
            //                       "JAVALTD", "JTEAM1" );
            //ProgramCall pgm = new ProgramCall(ltd);
            ProgramCall pgm = new ProgramCall(systemObject_);
            if (!pgm.run(goodPgm_, buildParms() ))
            {
                failed("command failed " + pgm.getMessageList()[0].getText() ); return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Create a program object,
     run with program and parameter list.
     Verify that a null parameter will throw ExtendedIllegalArgumentException.
     **/
    public void Var023()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            ProgramParameter[] parmlist = new ProgramParameter[3];
            byte[]  data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist[0] = new ProgramParameter(data1);
            parmlist[1] = new ProgramParameter(2);

            // Set the program name and parameter list
            pgm.setProgram(goodPgm_, parmlist );

            pgm.run();
            failed("Expected exception not thrown"); return;
        }
        catch(Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException",ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }


    //
    //
    // Variations to test getJob()                             @B2A
    //
    //


    /**
     Create a program object, without setting system,
     verify that getJob() returns null.
     **/
    public void Var024()
    {
      notApplicable("obsolete method");  // method eliminated: ProgramCall.getJob()
//        try
//        {
//            ProgramCall pgm = new ProgramCall();
//            RJob job = pgm.getJob();
//            failed("Did not throw exception.");
//        }
//        catch (Exception e)
//        {
//            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
//        }
//        catch (NoClassDefFoundError e)
//        { // Tolerate missing RJob class.
//            if (e.getMessage().indexOf("RJob") != -1) {
//                failed("Class not found: RJob");
//            }
//            else failed(e, "Unexpected exception");
//        }
    }

    /**
     Create a program object,
     verify that getJob() returns non-null.
     **/
    public void Var025()
    {
      notApplicable("obsolete method");  // method eliminated: ProgramCall.getJob()
//        if (systemObject_.getProxyServer().length() > 0)                  // @B4A
//        {
//            notApplicable("getJob() with proxy not supported");             // @B4A
//            return;                                                         // @B4A
//        }
//
//        try
//        {
//            ProgramCall pgm = new ProgramCall(systemObject_);
//            RJob job = pgm.getJob();
//            if (job == null)
//                failed("Null Job returned from getJob()");
//            else
//            {
//                if (DEBUG) {
//                    String jobID = job.getName() + "/" + job.getUser() + "/" + job.getNumber();
//                    output_.println("DEBUG job name/user/number: |" + jobID + "|");
//                }
//                succeeded();
//            }
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception");
//        }
//        catch (NoClassDefFoundError e)
//        { // Tolerate missing RJob class.
//            if (e.getMessage().indexOf("RJob") != -1) {
//                failed("Class not found: RJob");
//            }
//            else failed(e, "Unexpected exception");
//        }
    }

    /**
     If running on an AS/400:
     Verify that getJob() returns different values if program is declared
     threadsafe vs non-threadsafe.  This is a crude way to check that
     we get the job of the JVM versus the job of the Host Server.
     If *not* running on an AS/400:
     Verify that getJob() returns same values if program is declared
     threadsafe vs non-threadsafe.  This is a crude way to check that
     we always get the job of the Host Server rather than the JVM.
     **/
    public void Var026()
    {
      notApplicable("obsolete method");  // method eliminated: ProgramCall.getJob()
//        if (systemObject_.getProxyServer().length() > 0)                  // @B4A
//        {
//            notApplicable("getJob() with proxy not supported");             // @B4A
//            return;                                                         // @B4A
//        }
//
//        try
//        {
//            ProgramCall pgm = new ProgramCall(systemObject_);
//
//            pgm.setThreadSafe(false);
//            RJob job1 = pgm.getJob();
//            if (job1 == null) {
//                failed("Null Job returned from first getJob()");
//                return;
//            }
//            String job1ID = job1.getName() + "/" + job1.getUser() + "/" + job1.getNumber();
//            if (DEBUG)
//                output_.println("DEBUG job1 name/user/number: |" + job1ID + "|");
//
//            pgm.setThreadSafe(true);
//            RJob job2 = pgm.getJob();
//            if (job2 == null) {
//                failed("Null Job returned from second getJob()");
//                return;
//            }
//            String job2ID = job2.getName() + "/" + job2.getUser() + "/" + job2.getNumber();
//            if (DEBUG)
//                output_.println("DEBUG job2 name/user/number: |" + job2ID + "|");
//
//            pgm.setThreadSafe(false);
//            RJob job3 = pgm.getJob();
//            if (job3 == null) {
//                failed("Null Job returned from third getJob()");
//                return;
//            }
//            String job3ID = job3.getName() + "/" + job3.getUser() + "/" + job3.getNumber();
//            if (DEBUG)
//                output_.println("DEBUG job3 name/user/number: |" + job3ID + "|");
//
//            // Note: Threadsafety features became available in V5R1.
//            if (onAS400_ && systemObject_.getVersion() >= 5)
//            {
//                if (DEBUG) output_.println("DEBUG Running on an AS/400 (V5 or later)");
//                assertCondition (!job1ID.equals(job2ID) &&
//                                 job1ID.equals(job3ID));
//            }
//            else
//            {
//                //if (DEBUG) output_.println("DEBUG Not running on an AS/400");
//                assertCondition (job1ID.equals(job2ID) &&
//                                 job1ID.equals(job3ID));
//            }
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception");
//        }
//        catch (NoClassDefFoundError e)
//        { // Tolerate missing RJob class.
//            if (e.getMessage().indexOf("RJob") != -1) {
//                failed("Class not found: RJob");
//            }
//            else failed(e, "Unexpected exception");
//        }
    }

    /**
     Call a system API but alter parm 3 so that it is an InOut parm.
     The API doc says parm 3 is really an input parm but it doesn't
     hurt to make it an i/o parm.  What we really want to test is
     where outputSize < inputSize on an InOut parm.  This failed
     with an index out of bounds exception in v4r5.  Make sure we
     don't get an exception.
     **/
    public void Var027()
    {
        try
        {
            String msgId, msgText;

            AS400Bin4 bin4   = new AS400Bin4();
            AS400Text char6  = new AS400Text(6,  systemObject_);
            AS400Text char7  = new AS400Text(7,  systemObject_);
            AS400Text char8  = new AS400Text(8,  systemObject_);
            AS400Text char10 = new AS400Text(10, systemObject_);

            ProgramCall pc = new ProgramCall(systemObject_);
            pc.setProgram("/QSYS.LIB/QSYRUSRI.PGM");

            ProgramParameter[] parms = new ProgramParameter[5];

            // First parm is the output area that contains the result
            parms[0] = new ProgramParameter(100);

            // Second parm is the size of the output area
            // (100 bytes in our case)
            parms[1] = new ProgramParameter(bin4.toBytes(100));

            // Third parm is the output format to use
            parms[2] = new ProgramParameter(char8.toBytes("USRI0100"), 5);

            // Fourth parm is the user profile
            parms[3] = new ProgramParameter(char10.toBytes("*CURRENT"));

            // Fifth parm is the error area
            byte[] errorArea = new byte[32];
            parms[4] = new ProgramParameter(errorArea, 32);

            pc.setParameterList(parms);

            // If return code is false, we received messages from the AS/400
            if (pc.run() == false)
            {
                failed("pc.run() returned false.  Messages:");

                // Retrieve list of AS/400 messages
                AS400Message[] msgs = pc.getMessageList();

                // Iterate through messages and write them to standard output
                for (int m = 0; m < msgs.length; m++)
                {
                    msgId = msgs[m].getID();
                    msgText = msgs[m].getText();
                    System.out.println("    " + msgId + " - " + msgText);
                }
            }
            else
            {
                succeeded();
                //  byte[] data = parms[0].getOutputData();
                //
                //  int value   = ((Integer) bin4.toObject(data)).intValue();
                //  System.out.println("        Bytes returned:      " + value);
                //
                //  value   = ((Integer) bin4.toObject(data, 4)).intValue();
                //  System.out.println("        Bytes available:     " + value);
                //
                //  String strValue = (String) char10.toObject(data, 8);
                //  System.out.println("        Profile name:        " + strValue);
                //
                //  strValue = (String) char7.toObject(data, 18);
                //  System.out.println("        Previous signon date:" + strValue);
                //
                //  strValue = (String) char6.toObject(data, 25);
                //  System.out.println("        Previous signon time:" + strValue);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // Tolerate missing Job class.
            assertCondition(e.getMessage().indexOf("Job") != -1);
        }
    }

    //
    //
    // Variations to test getServerJob()
    //
    //

    /**
     Create a program object, without setting system,
     verify that getServerJob() throws an exception.
     **/
    public void Var028()
    {
        try
        {
            ProgramCall pgm = new ProgramCall();
            Job job = pgm.getServerJob();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     Create a program object,
     verify that getServerJob() returns non-null.
     **/
    public void Var029()
    {
        if (systemObject_.getProxyServer().length() > 0)           
        {
            notApplicable("getServerJob() with proxy not supported");
            return;                                                  
        }

        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            Job job = pgm.getServerJob();
            if (job == null)
                failed("Null Job returned from getServerJob()");
            else
            {
                if (DEBUG) {
                    String jobID = job.getName() + "/" + job.getUser() + "/" + job.getNumber();
                    output_.println("DEBUG job name/user/number: |" + jobID + "|");
                }
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // JarMaker may have removed Job class.
            if (e.getMessage().indexOf("Job") != -1 ||
                e.getMessage().indexOf("IntegerHashtable") != -1) {
                failed("Job-related class not found: " + e.getMessage());
            }
            else failed(e, "Unexpected exception");
        }
    }

    /**
     If running on an AS/400:
     Use jt400.jar instead of jt400Native.jar.
     Verify that getServerJob() returns different values if program is declared
     threadsafe vs non-threadsafe.  This is a crude way to check that
     we get the job of the JVM versus the job of the Host Server.
     If *not* running on an AS/400:
     Verify that getServerJob() returns same values if program is declared
     threadsafe vs non-threadsafe.  This is a crude way to check that
     we always get the job of the Host Server rather than the JVM.
     **/
    public void Var030()
    {
        if (systemObject_.getProxyServer().length() > 0)
        {
            notApplicable("getServerJob() with proxy not supported");
            return;                                                  
        }

        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            pgm.setThreadSafe(false);
            Job job1 = pgm.getServerJob();
            if (job1 == null) {
                failed("Null Job returned from first getServerJob()");
                return;
            }
            String job1ID = job1.getName() + "/" + job1.getUser() + "/" + job1.getNumber();
            if (DEBUG)
                output_.println("DEBUG job1 name/user/number: |" + job1ID + "|");

            pgm.setThreadSafe(true);
            Job job2 = pgm.getServerJob();
            if (job2 == null) {
                failed("Null Job returned from second getServerJob()");
                return;
            }
            String job2ID = job2.getName() + "/" + job2.getUser() + "/" + job2.getNumber();
            if (DEBUG)
                output_.println("DEBUG job2 name/user/number: |" + job2ID + "|");

            pgm.setThreadSafe(false);
            Job job3 = pgm.getServerJob();
            if (job3 == null) {
                failed("Null Job returned from third getServerJob()");
                return;
            }
            String job3ID = job3.getName() + "/" + job3.getUser() + "/" + job3.getNumber();
            if (DEBUG)
                output_.println("DEBUG job3 name/user/number: |" + job3ID + "|");

            // Note: Threadsafety features became available in V5R1.
            if (/*onAS400_*/ isRunningNatively() && systemObject_.getVersion() >= 5)
            {
                if (DEBUG) output_.println("DEBUG Running on an AS/400 (V5 or later)");
                assertCondition (!job1ID.equals(job2ID) &&
                                 job1ID.equals(job3ID));
            }
            else
            {
                //if (DEBUG) output_.println("DEBUG Not running on an AS/400");
                assertCondition (job1ID.equals(job2ID) &&
                                 job1ID.equals(job3ID));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // JarMaker may have removed Job class.
            if (e.getMessage().indexOf("Job") != -1 ||
                e.getMessage().indexOf("IntegerHashtable") != -1) {
                failed("Job-related class not found: " + e.getMessage());
            }
            else failed(e, "Unexpected exception");
        }
    }

    /**
     * Try calling a program that throws a CPF9801 to make sure that
     * the run() method still returns.
     * Use the ErrorCodeParameter class in various ways.
     **/
    public void Var031()
    {
      boolean succeeded = true;
      try
      {
        // Use a non-existent library to generate a CPF9801.
        ProgramParameter[] parms = new ProgramParameter[6];
        parms[0] = new ProgramParameter(90); // output
        parms[1] = new ProgramParameter(BinaryConverter.intToByteArray(90)); // length
        parms[2] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "OBJD0100")); // format name
        parms[3] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "QCUSTCDT  LIBNAME890")); // object/library name
        parms[4] = new ProgramParameter(CharConverter.stringToByteArray(37, systemObject_, "*USRPRF   ")); // object type
        ErrorCodeParameter errorParm = new ErrorCodeParameter(); // error code - use default ctor
        parms[5] = errorParm;

        ProgramCall pc = new ProgramCall(systemObject_, "/QSYS.LIB/QUSROBJD.PGM", parms);

        final MessageFile msgFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

        if (DEBUG) System.out.println("\nUsing default ErrorCodeParameter constructor:\n");
        boolean result = pc.run();
        boolean foundExpectedMessage = false;
        if (result == false)
        {
          AS400Message[] msgs = pc.getMessageList();
          for (int i=0; i<msgs.length; ++i) {
            if (DEBUG) output_.println(msgs[i]);
            if (msgs[i].getID().equals("CPF9801")) {
              foundExpectedMessage = true;
              if (msgs[i].getText().trim().length() == 0) {
                succeeded = false;
                output_.println("Blank message text returned on CPF9801.");
              }
            }
          }
        }
        if (!foundExpectedMessage) {
          succeeded = false;
          output_.println("Didn't receive expected message CPF9801.");
        }
        {
          String messageID = errorParm.getMessageID();
          String substData = errorParm.getSubstitutionData();
          //int ccsid = errorParm.getDataCCSID();
          if (DEBUG) {
            System.out.println("result == " + result);
            System.out.println("messageID == |" + messageID + "|");
            System.out.println("substData == |" + substData + "|");
            //System.out.println("ccsid == |" + ccsid + "|");
          }
          if (messageID != null) {
            AS400Message msg = msgFile.getMessage(messageID, substData);
            if (DEBUG) System.out.println("Substituted message: |" + msg.getText() + "|");
            msg.load();
            if (DEBUG) System.out.println("Help text: |" + msg.getHelp() + "|");
          }
        }

        // Now try the form of ErrorCodeParameter that returns the error info in the output parameter rather than as an exception.  Specify 'false,false'.

        if (DEBUG) System.out.println("\nUsing ErrorCodeParameter(false,false):\n");
        errorParm = new ErrorCodeParameter(false, false); // error code, no replacement data, no CCHAR
        parms[5] = errorParm;
        result = pc.run();
        {
          String messageID = errorParm.getMessageID();
          String substData = errorParm.getSubstitutionData();
          //int ccsid = errorParm.getDataCCSID();
          if (DEBUG) {
            System.out.println("result == " + result);
            System.out.println("messageID == |" + messageID + "|");
            System.out.println("substData == |" + substData + "|");
            //System.out.println("ccsid == |" + ccsid + "|");
          }
          if (messageID != null) {
            AS400Message msg = msgFile.getMessage(messageID, substData);
            if (DEBUG) System.out.println("Substituted message: |" + msg.getText() + "|");
            msg.load();
            if (DEBUG) System.out.println("Help text: |" + msg.getHelp() + "|");
          }
        }


        // Now try the form of ErrorCodeParameter that returns the error info in the output parameter rather than as an exception.  Specify 'false,true'.

        if (DEBUG) System.out.println("\nUsing ErrorCodeParameter(false,true):\n");
        errorParm = new ErrorCodeParameter(false, true); // error code, no replacement data, use CCHAR
        parms[5] = errorParm;
        result = pc.run();
        {
          String messageID = errorParm.getMessageID();
          String substData = errorParm.getSubstitutionData();
          //int ccsid = errorParm.getDataCCSID();
          if (DEBUG) {
            System.out.println("result == " + result);
            System.out.println("messageID == |" + messageID + "|");
            System.out.println("substData == |" + substData + "|");
            //System.out.println("ccsid == |" + ccsid + "|");
          }
          if (messageID != null) {
            AS400Message msg = msgFile.getMessage(messageID, substData);
            if (DEBUG) System.out.println("Substituted message: |" + msg.getText() + "|");
            msg.load();
            if (DEBUG) System.out.println("Help text: |" + msg.getHelp() + "|");
          }
        }


        // Now try the form of ErrorCodeParameter that returns the error info in the output parameter rather than as an exception.  Specify 'true,false'.

        if (DEBUG) System.out.println("\nUsing ErrorCodeParameter(true,false):\n");
        errorParm = new ErrorCodeParameter(true, false); // error code, return replacement data, no CCHAR
        parms[5] = errorParm;
        result = pc.run();
        {
          String messageID = errorParm.getMessageID();
          String substData = errorParm.getSubstitutionData();
          //int ccsid = errorParm.getDataCCSID();
          if (DEBUG) {
            System.out.println("result == " + result);
            System.out.println("messageID == |" + messageID + "|");
            System.out.println("substData == |" + substData + "|");
            //System.out.println("ccsid == |" + ccsid + "|");
          }
          if (messageID != null) {
            AS400Message msg = msgFile.getMessage(messageID, substData);
            if (DEBUG) System.out.println("Substituted message: |" + msg.getText() + "|");
            msg.load();
            if (DEBUG) System.out.println("Help text: |" + msg.getHelp() + "|");
          }
        }


        // Now try the form of ErrorCodeParameter that returns the error info in the output parameter rather than as an exception.  Specify 'true,true'.

        if (DEBUG) System.out.println("\nUsing ErrorCodeParameter(true,true):\n");
        errorParm = new ErrorCodeParameter(true, true); // error code, return replacement data, use CCHAR
        parms[5] = errorParm;
        result = pc.run();
        {
          String messageID = errorParm.getMessageID();
          String substData = errorParm.getSubstitutionData();
          //int ccsid = errorParm.getDataCCSID();
          if (DEBUG) {
            System.out.println("result == " + result);
            System.out.println("messageID == |" + messageID + "|");
            System.out.println("substData == |" + substData + "|");
            //System.out.println("ccsid == |" + ccsid + "|");
          }
          if (messageID != null) {
            AS400Message msg = msgFile.getMessage(messageID, substData);
            if (DEBUG) System.out.println("Substituted message: |" + msg.getText() + "|");
            msg.load();
            if (DEBUG) System.out.println("Help text: |" + msg.getHelp() + "|");
          }
        }


        assertCondition(succeeded);
      }

      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    // No hanging, No timeout. 
    // The program can ends normally, just to make sure it works by adding timeout support
    // One question, I mannualy update those three pgms to lp03ut5, PgmTimeout1_, PgmTimeout2_, PgmTimeout3_
    // but idea way should be automatcially created just like Prog25_  declaration in the top the class. 
    // Do you know how, John?
    // currently I mannually create program on the server, will make it automtically created when I have time. 
    public void Var032() {
    	boolean succeeded = true;
    	String pgmNormal = PgmTimeout1_;
		try {
			ProgramCall pgmCall = new ProgramCall(systemObject_, pgmNormal, new ProgramParameter[]{new ProgramParameter()});
			succeeded = pgmCall.run();
			assertCondition(succeeded);
		} catch (Exception e) {
			failed(e, "Unexpected exception");
			e.printStackTrace();
		} 
    }
    
    
    //Hanging, timeout < running time
    // In this case, the timeout set is short than running time, the expected result is connection ended.  
    // in order not to have impact on other cases, I tried to create a user profile using JAVA profile, but failed, need to figure out why,
    // Anyway, I temporily changed another way to create a userprofile mannually via green screen.
    // currently I mannually create program on the server, will make it automtically created when I have time. 
	public void Var033() {
		boolean succeeded = false;
		String pgmHang = PgmTimeout3_;
		
		AS400 system = null;
		try {
			system = new AS400(systemObject_.getSystemName(), userHang, pwdHang);
			ProgramCall pgmCall = new ProgramCall(system, pgmHang, new ProgramParameter[]{new ProgramParameter()});
			JDReflectionUtil.callMethod_V(pgmCall, "setTimeOut", 20);
			pgmCall.run();
			failed("did not throw timeout exception");
		} catch (Exception e) {
			// the system should disconnect after the timeout occurs.
			if (!system.isConnected()) {
				succeeded = true;
				assertCondition(succeeded);
			} else {
				failed(e, "Unexpected exception");
			}
		} finally {
		}
	}

    //Hanging, timeout > running time
	// This is opposite to the previous one, the expected result is that after the program is successfully completed, 
	// the connection still is available. 
    // currently I mannually create program on the server, will make it automtically created when I have time. 
	public void Var034() {
		boolean succeeded = false;
		String pgmHang = PgmTimeout2_;
		

		AS400 system = null;
		try {
			system = new AS400(systemObject_.getSystemName(), userHang, pwdHang);
			ProgramCall pgmCall = new ProgramCall(system, pgmHang, new ProgramParameter[]{new ProgramParameter()});
                        JDReflectionUtil.callMethod_V(pgmCall, "setTimeOut", 50);
			succeeded = pgmCall.run();
			if (!succeeded) {
				failed("program is not sucessfully executed.");
				return;
			}
			Thread.sleep(60000);
			if (system.isConnected()) {
					succeeded = true;
					assertCondition(succeeded);
			} else {
				failed("connection is ended.");
			}
		} catch (Exception e) {
			failed(e, "Unexpected exception");
		} finally {
		}
	}
	
	 /**
    Run a program with 36 parameters and check output parameters.
    **/
   public void Var035()
   {
	   try {
			if(systemObject_.getVRM() < 0x00070200) {
				  notApplicable("Currently this function is suported on 7.2 and later");
				  return;
			}
		  } catch (AS400SecurityException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
       try
       {
           parmlist_ = new ProgramParameter[36];

           // P0 = sum of P1 to P9
           parmlist_[0] = new ProgramParameter( int16.toBytes((short)1), 2 );

           for (short i=1; i<10; i++)
           {
               parmlist_[i] = new ProgramParameter( int16.toBytes(i) );
           }

           // P34 = sum of P10 to P33
           parmlist_[34] = new ProgramParameter( 240 );
           parmlist_[35] = new ProgramParameter( 240 );
           byte[] strret = new byte[240];
           int strpos = 0;
           for (int i=10; i<34; i++)
           {
               byte[] str = new byte[10];
               for (int j=1; j<9; j++)
                   str[j] = (byte)(i+65);
               str[0] = (byte)'[';
               str[9] = (byte)']';
               parmlist_[i] = new ProgramParameter( str );
               System.arraycopy(str, 0, strret, strpos, 10);
               strpos += 10;
           }

           // P35 = P34
           ProgramCall pgm = new ProgramCall(systemObject_);
           if (pgm.run(Prog36_, parmlist_))
           {
               if (compare(
                           strret,
                           pgm.getParameterList()[34].getOutputData()
                           ) &&compare(
                                   strret,
                                   pgm.getParameterList()[35].getOutputData()
                                   )&&
                   (int16.toShort(
                                  pgm.getParameterList()[0].getOutputData(), 0)
                    == 46))
                   succeeded();
               else
                   failed( "incorrect parameter returned" );
           }
           else
           {
               failed("program returned false");
           }
       }
       catch (Exception e)
       {
           failed(e, "Unexpected exception");
       }
   }

   /**
   Run a program with 255 parameters and check output parameters.
   **/
  public void Var036()
  {
	  try {
		if(systemObject_.getVRM() < 0x00070200) {
			  notApplicable("Currently this function is suported on 7.2");
			  return;
		}
	  } catch (AS400SecurityException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
      try
      {
          parmlist_ = new ProgramParameter[255];

          // P0 = sum of P1 to P9
          parmlist_[0] = new ProgramParameter( int16.toBytes((short)1), 2 );

          for (short i=1; i<10; i++)
          {
              parmlist_[i] = new ProgramParameter( int16.toBytes(i) );
          }

          // P34 = sum of P10 to P33
          parmlist_[34] = new ProgramParameter( 240 );
          parmlist_[35] = new ProgramParameter( 240 );
          parmlist_[254] = new ProgramParameter( 240 );
          byte[] strret = new byte[240];
          int strpos = 0;
          for (int i=10; i<34; i++)
          {
              byte[] str = new byte[10];
              for (int j=1; j<9; j++)
                  str[j] = (byte)(i+65);
              str[0] = (byte)'[';
              str[9] = (byte)']';
              parmlist_[i] = new ProgramParameter( str );
              System.arraycopy(str, 0, strret, strpos, 10);
              strpos += 10;
          }
          
          for (int i=35; i<254; i++)
          {
              byte[] str = new byte[10];
              for (int j=1; j<9; j++)
                  str[j] = (byte)(i+65);
              str[0] = (byte)'[';
              str[9] = (byte)']';
              parmlist_[i] = new ProgramParameter( str );
          }

          // P35 = P34， P255 = P34
          ProgramCall pgm = new ProgramCall(systemObject_);
          if (pgm.run(Prog255_, parmlist_))
          {
              if (compare(
                          strret,
                          pgm.getParameterList()[34].getOutputData()
                          ) &&compare(
                                          strret,
                                          pgm.getParameterList()[254].getOutputData()
                                          )&&
                  (int16.toShort(
                                 pgm.getParameterList()[0].getOutputData(), 0)
                   == 46))
                  succeeded();
              else
                  failed( "incorrect parameter returned" );
          }
          else
          {
              failed("program returned false");
          }
      }
      catch (Exception e)
      {
          failed(e, "Unexpected exception");
      }
  }
  
	/**
	 * Run a nonexist program and expect message returned w/ date and time.
	 **/
	public void Var037() {
		try {
			if (systemObject_.getVRM() < 0x00070200) {
				notApplicable("Currently this function is suported on 7.2");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ProgramCall pgm = new ProgramCall(systemObject_);
		try {
			boolean isSuccess = pgm.run("/QSYS.LIB/QSYRUSRI.PGM", buildParms());
			if (!isSuccess) {
				// we except error, and we only check the first error message and verify its date is empty or not. 
				AS400Message[] msgs = pgm.getMessageList();
				for (int i = 0; i < msgs.length; i++) {
					AS400Message msg = msgs[i];
					assertCondition(msg.getDate()!=null);
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(); // The exception is unexpected here
		}
	}
	
}
