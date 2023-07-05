///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Trace;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.ObjectDoesNotExistException;

class DQThreadTestcase extends ThreadedTestcase
{
    private static final String lib = "DQTHDTST";

    /**
     * Creates a new DQThreadTestcase.
     * This is called from ThreadTest::createTestcases().
     */
    public DQThreadTestcase (AS400 systemObject, Vector variationsToRun, int runMode, FileOutputStream fileOutputStream, String password)
    {
	super (systemObject, "DQThreadTestcase", 9, variationsToRun, runMode, fileOutputStream, password);
        //turnTraceOn();
    }

    void turnTraceOn()
    {
	Trace.setTraceDatastreamOn(true);
        //Trace.setTraceDiagnosticOn(true);
        //Trace.setTraceErrorOn(true);
        //Trace.setTraceInformationOn(true);
        //Trace.setTraceWarningOn(true);
	Trace.setTraceOn(true);
    }

    /**
     * Runs the variations requested.
     **/
    public void run()
    {
	boolean allVariations = (variationsToRun_.size() == 0);

        // create a library to work in.
	try
	{
	    CommandCall cmd = new CommandCall(systemObject_);
	    deleteLibrary(cmd, lib);
	    if (cmd.run("CRTLIB " + lib) == false)
	    {
		if (cmd.getMessageList()[0].getText().indexOf("already exists") == -1)
		{
		    output_.println("Unable to create library " + cmd.getMessageList()[0].getID() + cmd.getMessageList()[0].getText());
		}
	    }
	}
	catch (Exception e)
	{
	    if (e.getMessage().indexOf("CPF2111") == -1)
	    {
		output_.println( "Unable to create library " + e);
	    }
	}
	finally
	{
	    systemObject_.disconnectService(AS400.COMMAND);
	}

	if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED)
	{
	    setVariation(1);
	    Var001();
	}

	if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
	{
	    setVariation(2);
	    Var002();
	}

	if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
	{
	    setVariation(3);
	    Var003();
	}

	if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
	{
	    setVariation(4);
	    Var004();
	}

	if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
	{
	    setVariation(5);
	    Var005();
	}

	if ((allVariations || variationsToRun_.contains("6")) && runMode_ != ATTENDED)
	{
	    setVariation(6);
	    Var006();
	}

	if ((allVariations || variationsToRun_.contains("7")) && runMode_ != ATTENDED)
	{
	    setVariation(7);
	    Var007();
	}

	if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED)
	{
	    setVariation(8);
	    Var008();
	}

	if ((allVariations || variationsToRun_.contains("9")) && runMode_ != ATTENDED)
	{
	    setVariation(9);
	    Var009();
	}

	try
	{
	    systemObject_.disconnectService(AS400.DATAQUEUE);
	}
	catch(Exception e)
	{
	    output_.println("Unable to disconnect service " + e);
	}
    }

    void deleteDQ(DataQueue dq) throws Exception
    {
	try
	{
	    dq.delete();
	}
	catch (ObjectDoesNotExistException e)
	{
	    // ignore
	}
    }

    void deleteDQ(KeyedDataQueue kdq) throws Exception
    {
	try
	{
	    kdq.delete();
	}
	catch(ObjectDoesNotExistException e)
	{
	    // ignore.
	}
    }

    /**
     * Mult threads performing reads & writes on a Keyed Data Queue.
     **/
    public void Var001()
    {
	try
	{
	    pipeInput_    = new PipedInputStream();
            // create the keyed data queue.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADDQ1.DTAQ");
	    deleteDQ(dq);
	    output_.println("Creating");
	    dq.create(50, 200);
	    try
	    {
		DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_RD_WR, dq);
		DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_RD_WR, dq);
		objectInput_  = new ObjectInputStream(pipeInput_);
		output_.println("starting");
		t1.start();
		t2.start();
		go();
		handleError();
		stopThreads();
	    }
	    finally
	    {
		output_.println("b4 delete");
		deleteDQ(dq);
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace(output_);
	    failed(e, "Unexpected Exception");
	}
    }

    /**
     * Mult threads performing reads & writes on a FIFO Data Queue.
     **/
    public void Var002()
    {
	try
	{
	    pipeInput_    = new PipedInputStream();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADDQ2.DTAQ");
	    deleteDQ(dq);
	    dq.create(200);
	    try
	    {
		output_.println("created dq");
		DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, dq);
		DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, dq);
		objectInput_  = new ObjectInputStream(pipeInput_);
		t1.start();
		t2.start();
		go();
		output_.println("b4 handleError");
		handleError();
		output_.println("b4 stopThreads");
		stopThreads();
	    }
	    finally
	    {
		output_.println("deleting dq");
		deleteDQ(dq);
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace(output_);
	    failed(e, "Unexpected Exception in Var");
	}
    }

    /**
     * Mult threads performing reads & writes on a LIFO Data Queue.
     */
    public void Var003()
    {
	try
	{
	    pipeInput_    = new PipedInputStream();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADDQ3.DTAQ");
	    deleteDQ(dq);
	    dq.create(200, "*LIBCRTAUT", false, false, false, "DQTHREADDQ TEST");
	    try
	    {
		DQThread t1 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, dq);
		DQThread t2 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, dq);
		DQThread t3 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, dq);
		objectInput_  = new ObjectInputStream(pipeInput_);
		t1.start();
		t2.start();
		t3.start();
		go();
		handleError();
		stopThreads();
	    }
	    finally
	    {
		deleteDQ(dq);
		output_.println("after delete");
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace(output_);
	    failed(e, "Unexpected Exception");
	}
    }

    /**
     * 3 Pairs of threads executing on keyed, fifo, lifo dataqueues simultaneously.
     **/
    public void Var004()
    {
	try
	{
	    pipeInput_    = new PipedInputStream();
	    KeyedDataQueue kdq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADKDQ.DTAQ");
	    DataQueue fdq = new DataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADFDQ.DTAQ");
	    DataQueue ldq = new DataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADLDQ.DTAQ");
	    deleteDQ(kdq);
	    deleteDQ(fdq);
	    deleteDQ(ldq);
	    kdq.create(50, 200);
	    fdq.create(200);
	    ldq.create(200, "*LIBCRTAUT", false, false, false, "DQTHREADDQ TEST");
	    try
	    {
		DQThread  tk1 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_RD_WR, kdq);
		DQThread  tk2 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_RD_WR, kdq);
		DQThread  tf1 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, fdq);
		DQThread  tf2 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, fdq);
		DQThread  tl1 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, ldq);
		DQThread  tl2 = new DQThread(pipeInput_, output_, this, DQThread.UNKEYED_RD_WR, ldq);
		objectInput_  = new ObjectInputStream(pipeInput_);
		tk1.start();
		tk2.start();
		tf1.start();
		tf2.start();
		tl1.start();
		tl2.start();
		go();
		handleError();
		stopThreads();
	    }
	    finally
	    {
		deleteDQ(kdq);
		deleteDQ(fdq);
		deleteDQ(ldq);
		output_.println("after deletes");
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace(output_);
	    failed(e, "Unexpected Exception");
	}
    }

    /**
     * Mult threads performing writes & clears on a Keyed Data Queue.
     **/
    public void Var005()
    {
	try
	{
	    pipeInput_    = new PipedInputStream();
            // create the keyed data queue.
	    KeyedDataQueue kdq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADDQ5.DTAQ");
	    deleteDQ(kdq);
	    kdq.create(50, 200);
	    try
	    {
		DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_WR_CLEAR, kdq);
		DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_WR_CLEAR, kdq);
		objectInput_  = new ObjectInputStream(pipeInput_);
		t1.start();
		t2.start();
		go();
		handleError();
		stopThreads();
	    }
	    finally
	    {
		deleteDQ(kdq);
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace(output_);
	    failed(e, "Unexpected Exception");
	}
    }

    /**
     * Mult threads performing reads/writes & writes/clears on a Keyed Data Queue.
     **/
    public void Var006()
    {
	try
	{
	    pipeInput_    = new PipedInputStream();
            // create the keyed data queue.
	    KeyedDataQueue kdq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/" + lib + ".LIB/THREADDQ6.DTAQ");
	    deleteDQ(kdq);
	    kdq.create(50, 200);
	    try
	    {
		DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_RD_WR, kdq);
		DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.KEYED_WR_CLEAR, kdq);
		objectInput_  = new ObjectInputStream(pipeInput_);
		t1.start();
		t2.start();
		go();
		handleError();
		stopThreads();
	    }
	    finally
	    {
		deleteDQ(kdq);
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace(output_);
	    failed(e, "Unexpected Exception");
	}
    }

    /**
     * Mult threads performing reads/writes using Keyed Data
     * Queue local to each thread.
     **/
    public void Var007()
    {
	try
	{
	    pipeInput_   = new PipedInputStream();
	    String    p1 = "/QSYS.LIB/" + lib + ".LIB/THDDQ1.DTAQ";
	    String    p2 = "/QSYS.LIB/" + lib + ".LIB/THDDQ2.DTAQ";
	    DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.LOCAL_KEYED_RD_WR, systemObject_, p1, 50, 200);
	    DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.LOCAL_KEYED_RD_WR, systemObject_, p2, 52, 202);
	    objectInput_  = new ObjectInputStream(pipeInput_);
	    t1.start();
	    t2.start();
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
     * Mult threads performing reads/writes using unkeyed Data
     * Queue local to each thread.
     **/
    public void Var008()
    {
	try
	{
	    pipeInput_   = new PipedInputStream();
	    String    p1 = "/QSYS.LIB/" + lib + ".LIB/THDDQ1.DTAQ";
	    String    p2 = "/QSYS.LIB/" + lib + ".LIB/THDDQ2.DTAQ";
	    DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.LOCAL_UNKEYED_RD_WR, systemObject_, p1, 200);
	    DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.LOCAL_UNKEYED_RD_WR, systemObject_, p2, 202);
	    objectInput_  = new ObjectInputStream(pipeInput_);
	    t1.start();
	    t2.start();
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
     * 2 Threads doing reads and writes on dq.  DQs are local to
     * each thread with one is using a keyed and the other is
     * using a FIFO.
     **/
    public void Var009()
    {
	try
	{
	    pipeInput_   = new PipedInputStream();
	    String    p1 = "/QSYS.LIB/" + lib + ".LIB/THDDQ1.DTAQ";
	    String    p2 = "/QSYS.LIB/" + lib + ".LIB/THDDQ2.DTAQ";
	    DQThread  t1 = new DQThread(pipeInput_, output_, this, DQThread.LOCAL_KEYED_RD_WR, systemObject_, p1, 50, 200);
	    DQThread  t2 = new DQThread(pipeInput_, output_, this, DQThread.LOCAL_UNKEYED_RD_WR, systemObject_, p2, 202);
	    objectInput_  = new ObjectInputStream(pipeInput_);
	    t1.start();
	    t2.start();
	    go();
	    handleError();
	    stopThreads();
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected Exception");
	}
    }
}
