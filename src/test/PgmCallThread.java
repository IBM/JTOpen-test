///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmCallThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PipedInputStream;
import java.io.PrintWriter;
import com.ibm.as400.access.*;

class PgmCallThread
  extends ComponentThread
{
  // Functions that this thread can perform.  One is specified at object construction.
  public  static final int RUN_PROG3        = 1;
  public  static final int VERIFY_PROG3     = 2;
  public  static final int PARM_SET         = 3;  // sets input and output parms and verifies.
  public  static final int RUN_GENERIC      = 4;  // runs any command w/ any parms.
  public  static final int SET_RUN_GENERIC  = 5;  // uses setProgram(X,X) then run().

  private String              pgmPath   = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";
  private AS400               system    = null;                   // system to run command on.
  private String              result3   = "Testing 3 Parameters"; // expected out parm 3 of prog3.
  private String              command   = null;                   // command to run.
  private AS400Bin2           int16     = new AS400Bin2();        // used for verification.
  private ProgramCall         pgm       = null;                   // object to execute with.
  private ProgramParameter[]  parmList  = null;                   // list of parameters.
  private byte[]              data0     = null;                   // data used to build 1st parm of prog3.
  private byte[]              data1     = null;                   // data used to build 2nd parm of prog3.
  private ProgramParameter    parm0     = null;                   // 1st parm of prog3.
  private ProgramParameter    parm1     = null;                   // 2nd parm of prog3.
  private ProgramParameter    parm2     = null;                   // 3rd parm of prog3.


 /**
  * Constructor
  */
  public PgmCallThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function)
  {
    super(pipeReader, output, testcase, function);
  }


 /**
  * Constructor for RUN_PROG3
  */
  public PgmCallThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, String pgmPath, AS400 sys)
  {
    super(pipeReader, output, testcase, function);
    this.pgmPath = pgmPath;
    system       = sys;
  }


 /**
  * Constructor for VERIFY_PROG3
  */
  public PgmCallThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, ProgramCall pgm, AS400 sys,
                   String pgmPath, ProgramParameter[] parmList, byte[] data0, byte[] data1,
                   ProgramParameter parm0, ProgramParameter parm1, ProgramParameter parm2)
  {
    super(pipeReader, output, testcase, function);
    this.pgm        = pgm;
    system          = sys;
    this.pgmPath    = pgmPath;
    this.parmList   = parmList;
    this.data0      = data0;
    this.data1      = data1;
    this.parm0      = parm0;
    this.parm1      = parm1;
    this.parm2      = parm2;
  }


 /**
  * Constructor for PARM_SET.
  * @parm parmList [0] should be a Input parm, [1] should be an output parm.
  */
  public PgmCallThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function,
                   ProgramParameter[] parmList)
  {
    super(pipeReader, output, testcase, function);
    this.parmList   = parmList;
  }


 /**
  * Constructor for RUN_GENERIC & SET_RUN_GENERIC.
  */
  public PgmCallThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function,
                   ProgramCall pgm, String command, ProgramParameter[] parmList)
  {
    super(pipeReader, output, testcase, function);
    this.pgm        = pgm;
    this.command    = command;
    this.parmList   = parmList;
  }


 /**
  * Constructs a programParameter array with the arguments needed for
  * Prog3.  The 1st item is a in out parameter, The second in an in,
  * and the 3rd is an out parameter.  The values placed into the array
  * have their values saved into data0 and data1 for comparison purposes.
  */
  ProgramParameter[] buildParms()
  {
    parmList = new ProgramParameter[3];

    data0 = new byte[5];
    int i;
    for (i=0; i<5; i++)
    {
      data0[i] = (byte) 0;
    }
    data0[0] = 7;
    data0[1] = 6;
    parmList[0] = parm0 = new ProgramParameter(data0, 500);

    data1 = new byte[2];
    data1[0] = (byte) 4;
    data1[1] = (byte) 2;
    parmList[1] = parm1 = new ProgramParameter(data1);

    parmList[2] = parm2 = new ProgramParameter(2);

    return parmList;
  }


  private void verifyProg3(ProgramCall pgm)
  {
output_.println(getName()+" b4 getMessageList()");
    // Verify no messages returned
    AS400Message[] msglist = pgm.getMessageList();
    if (msglist.length!=0 )
    {
      error("message received " + msglist[0]);
    }
output_.println(getName()+" b4 getParameterList");
    // Verify parameter list
    ProgramParameter[] parmlist = pgm.getParameterList();
    if (parmlist != parmList)
    {
      error("parameter list changed" );
    }
    if (parmlist[0] != parm0)
    {
      error("parameter 0 changed" );
    }
    if (parmlist[1] != parm1)
    {
      error("parameter 1 changed" );
    }
    if (parmlist[2] != parm2)
    {
      error("parameter 2 changed" );
    }

    // Verify each parm
    // parm 0 still inout
output_.println(getName()+" b4 verifying 1st parm");
    if (parmlist[0].getInputData() != data0)
    {
      error("parameter 0 data changed" );
    }
    if (parmlist[0].getOutputDataLength() != 500)
    {
      error("parameter 0 output data length changed"
             + parmlist[0].getOutputDataLength());
    }
    // parm 0 result string
    AS400Text xlater = new AS400Text( result3.length(), system.getCcsid(), system );
    String retp0 =
         (String)xlater.toObject(pgm.getParameterList()[0].getOutputData(), 0 );
    if (!retp0.equals("Testing 3 Parameters"))
    {
      error("parameter 0 wrong value " + retp0 );
    }

    // parm 1 still input
output_.println(getName()+" b4 verifying 2nd parameter");
    if (parmlist[1].getInputData() != data1)
    {
      error("parameter 1 data changed" );
    }
    if (parmlist[1].getOutputDataLength() != 0)
    {
      error("parameter 1 output data length set"
             + parmlist[1].getOutputDataLength());
    }
    if (parmlist[1].getOutputData() != null
        && parmlist[1].getOutputData().length != 0)
    {
      error("parameter 1 output data set" );
    }

    // parm 2 still output
output_.println(getName()+" b4 verifying 3rd parm");
    if (parmlist[2].getInputData() != null
        && parmlist[2].getInputData().length != 0)
    {
      error("parameter 2 data changed" );
    }
    if (parmlist[2].getOutputDataLength() != 2)
    {
      error("parameter 2 output data length changed"
             + parmlist[2].getOutputDataLength());
    }
    // parm 2 = parm1+1
    if (1027 != int16.toShort( pgm.getParameterList()[2].getOutputData(), 0))
    {
      error("parameter 2 wrong value " + int16.toShort( pgm.getParameterList()[2].getOutputData(), 0) ); return;
    }

    // Verify system not changed
output_.println(getName()+" b4 verifying system");
    if (pgm.getSystem()!=system)
    {
      error("system changed " +pgm.getSystem().getSystemName() );
    }

    // Verify program not changed
output_.println(getName()+" b4 verifying program path");
    if (pgm.getProgram()!=pgmPath)
    {
      error("program changed " + pgm.getProgram() );
    }
  }


  public void run()
  {
    // notify testcase that we are ready to start.
    testcase_.ready();

    // perform the task specified by function, numLoops_
    // times unless the stop flag is set.
    for (int i = 0; i < numLoops_ && !stop_ ; i++)
    {
      // don't be a selfish thread...
      try { sleep(1); } catch (InterruptedException e) {}

      switch (function_)
      {
        case RUN_PROG3:
        performRunProg3();
        break;
        case VERIFY_PROG3:
        performVerifyProg3();
        break;
        case PARM_SET:
        performParmSet();
        break;
        case RUN_GENERIC:
        performRunGeneric();
        break;
        case SET_RUN_GENERIC:
        performSetRunGeneric();
        break;
      }
    }

    // done: close our end of pipe and stop.
    this.kill();
  }


 /**
  * Executes the pgm and verifies results.
  */
  private void performRunProg3()
  {
    try
    {
output_.println(getName()+" b4 pgmCall construction");
        ProgramCall pgm    = new ProgramCall(system);
output_.println(getName()+" b4 run");
        if (!pgm.run(pgmPath, buildParms() ))
        {
          error("Command execution failed");
        }
output_.println(getName()+" b4 verification");
        verifyProg3(pgm);
output_.println(getName()+" after verification");
    }
    catch (Exception e)
    {
      error(getName()+" Unexpected Exception"+e, e);
    }
  }


 /**
  * Verify that the already executed program object has returned
  * the expected results.
  */
  private void performVerifyProg3()
  {
    try
    {
      verifyProg3(pgm);
    }
    catch (Exception e)
    {
      error("Unexpected Exception", e);
    }
  }


 /**
  * Sets the values of the input data and output data length to one of 3
  * values and then verifies that is wasn't corrupted.
  * Assumes that the parmList is a ProgramParameter[2] with [0] being
  * an input parm and [1] being an output parm.
  */
  private void performParmSet()
  {
    try
    {
      byte[] data1 = (new String("12345")).getBytes();
      byte[] data2 = (new String("abcde")).getBytes();
      byte[] data3 = (new String("!@#$%")).getBytes();

      parmList[0].setInputData(data1);
      parmList[0].setInputData(data2);
      parmList[0].setInputData(data3);

      parmList[1].setOutputDataLength(4);
      parmList[1].setOutputDataLength(8);
      parmList[1].setOutputDataLength(16);

      // verify that inputData is one of the 3 valid values
      byte[] input = parmList[0].getInputData();
      if (! Testcase.isEqual(data3, input) )
        if (! Testcase.isEqual(data2, input) )
          if (! Testcase.isEqual(data1, input) )
          {
            error("Input Data was corrupted");
            return;
          }

       // verify that output Data length is valid
       int outputLen = parmList[1].getOutputDataLength();
       if ( outputLen != 16 )
         if ( outputLen != 8 )
           if ( outputLen != 4 )
           {
             error("Output Data Length was currupted");
             return;
           }
    }
    catch (Exception e)
    {
      error(getName()+" Unexpected Exception", e);
    }
  }


 /**
  * Runs any program specified by command using the parameters
  * in parmList.  Used to determine if another command using diff
  * args in a seperate thread can get command/parmList mixed up.
  */
  private void performRunGeneric()
  {
    try
    {
      // pgm is shared.
      if (!pgm.run(command, parmList))
        error("Program Executed Incorrectly");
        // can't check message as it might be from diff thread.
    }
    catch (Exception e)
    {
      error(getName()+" Unexpected Exception " + e, e);
    }
  }


 /**
  * Runs any program specified by command using the parameters
  * in parmList.  Used to determine if another command using diff
  * args in a seperate thread can get command/parmList mixed up
  * during call to setProgram(X,X).
  */
  private void performSetRunGeneric()
  {
    try
    {
output_.println(getName()+" b4 setProgram");
      pgm.setProgram(command, parmList);
output_.println(getName()+" b4 run");
      // pgm is shared.
      if (!pgm.run())
        error("Program Executed Incorrectly"); // likely due to command & parmList incorrectly paired.
        // can't check message as it might be from diff thread.
output_.println(getName()+" end");
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      error(getName()+" Unexpected Exception " + e, e);
    }
  }

}




