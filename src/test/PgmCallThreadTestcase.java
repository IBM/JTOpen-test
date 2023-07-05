///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmCallThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;


import java.io.FileOutputStream;
import java.io.PipedInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import com.ibm.as400.access.*;

import java.util.Vector;
import com.ibm.as400.access.AS400;


class PgmCallThreadTestcase
  extends ThreadedTestcase
{
  private String              pgm3Path  = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";
  private String              pgm0Path  = "/QSYS.LIB/W95LIB.LIB/PROG0.PGM";
  private ProgramParameter[]  parmList  = null;
  private byte[]              data0     = null;
  private byte[]              data1     = null;
  private ProgramParameter    parm0     = null;
  private ProgramParameter    parm1     = null;
  private ProgramParameter    parm2     = null;


 /**
  * Creates a new PgmCallThreadTestcase.
  * This is called from ThreadTest::createTestcases().
  */
  public PgmCallThreadTestcase (AS400 systemObject,
                             Vector variationsToRun,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
  {
    super (systemObject, "PgmCallThreadTestcase", 6, variationsToRun,
           runMode, fileOutputStream, password);
    //turnOnTracing();
  }


 /**
  * Turns on Diagnostic, error, information, and warning tracing.
  */
  private void turnOnTracing()
  {
    Trace.setTraceDatastreamOn(false);
    Trace.setTraceDiagnosticOn(true);
    Trace.setTraceErrorOn(true);
    Trace.setTraceInformationOn(true);
    Trace.setTraceWarningOn(true);
    Trace.setTraceOn(true);
  }


 /**
  * Runs the variations requested.
  */
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }
    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }
    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }
    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

  }


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


 /**
  * Multiple Threads running the same program using seperate ProgramCall objects.
  * The program uses In/Out, In, and Out parms.
  */
  public void Var001()
  {
    try
    {
      pipeInput_         = new PipedInputStream();
      PgmCallThread  t1  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.RUN_PROG3, pgm3Path, systemObject_);
      PgmCallThread  t2  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.RUN_PROG3, pgm3Path, systemObject_);
      objectInput_       = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception" + e);
    }
  }


 /**
  * 3 Threads use a shared pre-executed ProgramCall object and Parameter list to
  * verify that the program executed correctly.
  */
  public void Var002()
  {
    try
    {
      pipeInput_         = new PipedInputStream();
      ProgramCall    pc  = new ProgramCall(systemObject_);
      pc.run(pgm3Path, buildParms());

      PgmCallThread  t1  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.VERIFY_PROG3, pc, systemObject_,
                                      pgm3Path, parmList, data0, data1,
                                      parm0, parm1, parm2);
      PgmCallThread  t2  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.VERIFY_PROG3, pc, systemObject_,
                                      pgm3Path, parmList, data0, data1,
                                      parm0, parm1, parm2);
      PgmCallThread  t3  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.VERIFY_PROG3, pc, systemObject_,
                                      pgm3Path, parmList, data0, data1,
                                      parm0, parm1, parm2);
      objectInput_       = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * 3 Threads permforming sets and gets on a Parameter list.
  */
  public void Var003()
  {
    try
    {
      pipeInput_         = new PipedInputStream();
      parmList           = new ProgramParameter[2];
      parmList[0]        = new ProgramParameter();  // to be used for IN
      parmList[1]        = new ProgramParameter();  // to be used for OUT
      PgmCallThread  t1  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.PARM_SET, parmList);
      PgmCallThread  t2  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.PARM_SET, parmList);
      PgmCallThread  t3  = new PgmCallThread(pipeInput_, output_, this,
                                      PgmCallThread.PARM_SET, parmList);
      objectInput_       = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * 2 threads both trying to execute run(command, parmlist) using
  * diff command and diff parameter lists on the same ProgramCall object.
  * The commands and parmlist pairs are both valid
  */
  public void Var004()
  {
    try
    {
      pipeInput_         = new PipedInputStream();
      ProgramCall    pgm = new ProgramCall(systemObject_);
      PgmCallThread  t1  = new PgmCallThread(pipeInput_, output_, this,
                                PgmCallThread.RUN_GENERIC, pgm, pgm0Path, null);
      PgmCallThread  t2  = new PgmCallThread(pipeInput_, output_, this,
                               PgmCallThread.RUN_GENERIC, pgm, pgm3Path, buildParms());
      objectInput_       = new ObjectInputStream(pipeInput_);
      t1.setNumLoops(25);
      t2.setNumLoops(25);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception" + e);
    }
  }


 /**
  * 2 threads both trying to execute setProgram(command, parmlist)
  * and run() using diff command and diff parameter lists. Both
  * pairs are valid.
  */
  public void Var005()
  {
    try
    {
      pipeInput_         = new PipedInputStream();
      ProgramCall    pgm = new ProgramCall(systemObject_);
      PgmCallThread  t1  = new PgmCallThread(pipeInput_, output_, this,
                                PgmCallThread.SET_RUN_GENERIC, pgm, pgm0Path, null);
      PgmCallThread  t2  = new PgmCallThread(pipeInput_, output_, this,
                               PgmCallThread.SET_RUN_GENERIC, pgm, pgm3Path, buildParms());
      objectInput_       = new ObjectInputStream(pipeInput_);
      t1.setNumLoops(25);
      t2.setNumLoops(25);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception" + e);
    }
  }


 /**
  * 2 Threads both running diff commands requiring diff args.
  * One thread is using run(commmand, parmlist) while the other
  * is using setProgram(command, parmlist) in conjunction with
  * run().
  */
  public void Var006()
  {
    try
    {
      pipeInput_         = new PipedInputStream();
      ProgramCall    pgm = new ProgramCall(systemObject_);
      PgmCallThread  t1  = new PgmCallThread(pipeInput_, output_, this,
                                PgmCallThread.RUN_GENERIC, pgm, pgm0Path, null);
      PgmCallThread  t2  = new PgmCallThread(pipeInput_, output_, this,
                               PgmCallThread.SET_RUN_GENERIC, pgm, pgm3Path, buildParms());
      objectInput_       = new ObjectInputStream(pipeInput_);
      t1.setNumLoops(25);
      t2.setNumLoops(25);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception" + e);
    }
  }

}






