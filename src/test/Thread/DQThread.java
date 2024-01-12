///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Thread;

import java.io.PipedInputStream;
import java.io.PrintWriter;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.KeyedDataQueueEntry;
import com.ibm.as400.access.BaseDataQueue;
import com.ibm.as400.access.DataQueueEntry;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ComponentThread;
import test.ThreadedTestcase;

class DQThread extends ComponentThread
{
    // Functions that this thread can perform.  Determined at construction
    public static final int KEYED_RD_WR          = 0;
    public static final int KEYED_WR_CLEAR       = 1;
    public static final int UNKEYED_RD_WR        = 2;
    public static final int LOCAL_KEYED_RD_WR    = 3;
    public static final int LOCAL_UNKEYED_RD_WR  = 4;

    private KeyedDataQueue kdq        = null;
    private DataQueue      dq         = null;
    private AS400          system     = null;
    private String         dqPath     = null;
    private int            keyLength;
    private int            dataLength;

    /**
     * Constructor
     **/
    public DQThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, KeyedDataQueue kdq)
    {
	super(pipeReader, output, testcase, function);
	this.kdq = kdq;
    }

    /**
     * Constructor
     **/
    public DQThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, DataQueue dq)
    {
	super(pipeReader, output, testcase, function);
	this.dq = dq;
    }

    /**
     * Constructor for LOCAL_KEYED_RD_WR
     **/
    public DQThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, AS400 sys, String path, int keyLength, int dataLength)
    {
	super(pipeReader, output, testcase, function);
	system          = sys;
	dqPath          = path;
	this.keyLength  = keyLength;
	this.dataLength = dataLength;
    }

    /**
     * Constructor for LOCAL_UNKEYED_RD_WR
     **/
    public DQThread(PipedInputStream pipeReader, PrintWriter output, ThreadedTestcase testcase, int function, AS400 sys, String path, int dataLength)
    {
	super(pipeReader, output, testcase, function);
	system          = sys;
	dqPath          = path;
	this.dataLength = dataLength;
    }

    public void run()
    {
        // notify testcase that we are ready to start.
	testcase_.ready();

        // perform the task specified by function, numLoops
        // times unless the stop flag is set.
	for (int i = 0; i < numLoops_ && !stop_; i++)
	{
            // don't be a selfish thread...
	    try
	    {
		sleep(1);
	    }
	    catch (InterruptedException e)
	    {
	    }

	    switch (function_)
	    {
		case KEYED_RD_WR:LOCAL_KEYED_RD_WR:
		    performKeyedRdWr();
		    break;
		case KEYED_WR_CLEAR:
		    performKeyedWrClear();
		    break;
		case UNKEYED_RD_WR:LOCAL_UNKEYED_RD_WR:
		    performUnKeyedRdWr();
		    break;
	    }
	}

        // done: close our end of pipe and stop.
	this.kill();
    }

    /**
     * Writes ten unique entries to a keyed DQ and then reads them back
     * and verifies them.  The DQ may or may not be shared between threads
     * depending on which constructor was invoked.
     **/
    private void performKeyedRdWr()
    {
	try
	{
	    if (function_ == LOCAL_KEYED_RD_WR)
	    {
		output_.println(getName() + " b4 new kdq");
		kdq = new KeyedDataQueue(system, dqPath);
		kdq.create(keyLength, dataLength);

	        // verify dq was created w/ expected parameters
		output_.println(getName() + " b4 keyLen");
		if (kdq.getKeyLength() != keyLength)
		{
		    error("Key length corrupted");
		    return;
		}
		output_.println(getName() + " b4 maxEntLen");
		if (kdq.getMaxEntryLength() != dataLength)
		{
		    error("Entry length corrupted");
		    return;
		}
		output_.println(getName() + " b4 getPath");
		if (kdq.getPath() != dqPath)
		{
		    error("Entry length corrupted");
		    return;
		}
	    }

            // write 10 entries to the dq
	    for (int i = 0; i < 10; i++)
	    {
		output_.println(getName() + " writing keyed");
	        // create byte array containing key and blank fill it to the keyLength.
		byte[] key       = new byte[kdq.getKeyLength()];
		byte[] nameBytes = (getName() + i).getBytes();
		int j = 0;
		for (; j < nameBytes.length; j++)
		{
		    key[j] = nameBytes[j];
		}
		for (; j < key.length; j++)
		{
		    key[j] = 0x20;
		}
		String data = getThreadGroup() + getName() + i;
		kdq.write(key , data.getBytes());
	    }

            // read back the entries and verify them
	    for (int i = 0; i < 10; i++)
	    {
		output_.println(getName() + " reading keyed");
	        // create byte array containing key and blank fill it to the keyLength.
		byte[] key       = new byte[kdq.getKeyLength()];
		byte[] nameBytes = (getName() + i).getBytes();
		int j = 0;
		for (; j < nameBytes.length; j++)
		{
		    key[j] = nameBytes[j];
		}
		for (; j < key.length; j++)
		{
		    key[j] = 0x20;
		}

		KeyedDataQueueEntry entry = kdq.read(key, 0, "EQ");
		String              data  = new String(entry.getData());
		if (!data.trim().equals(getThreadGroup()+ getName() + i))
		{
		    error("Keyed DQ Entry doesn't contain expected data");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace(output_);
	    error("Unexpected Exception" + e, e);
	}
	finally
	{
             // if kdq is local then delete it.
	    if (function_ == LOCAL_KEYED_RD_WR)
	    {
		try
		{
		    deleteDQ(kdq);
		}
		catch (Exception e)
		{
		    error("Unable to delete queue", e);
		}
	    }
	}
    }

    /**
     * Writes ten unique entries to the non-keyed data queue then
     * retrieves these by peeking at the entry and then reading it
     * if it was written by this thread. The DQ can be shared or not
     * shared between threads depending on which constructor was
     * invoked.
     */
    private void performUnKeyedRdWr()
    {
	try
	{
            // create a data queue
	    if (function_ == LOCAL_UNKEYED_RD_WR)
	    {
		dq = new DataQueue(system, dqPath);
		output_.println(getName() + "b4 local create");
		dq.create(200);
		output_.println(getName() + "b4 getMaxEntryLength");
	        // verify dq was created w/ expected parameters
		if (dq.getMaxEntryLength() != dataLength)
		{
		    error("Entry length corrupted");
		    return;
		}
		output_.println(getName() + "b4 getPath");
		if (dq.getPath() != dqPath)
		{
		    error("Entry length corrupted");
		    return;
		}
	    }
	    output_.println(getName() + " b4 10 writes unkeyed '" + getName() + "'");
            // write data to queue
	    for (int i = 0; i < 10; i++)
	    {
		dq.write(getName().getBytes());
	    }

            // remove all of our entries on dq.
	    int retrieved = 0;
	    while (retrieved != 10)
	    {
		synchronized (dq)
		{
		    DataQueueEntry entry = dq.peek();
	            // examine entry on dq and remove if it's ours.
		    String data  = new String(entry.getData());
		    output_.println(getName() + " after unkeyed peeks=" + data);
		    if (data.trim().equals(getName()))
		    {
			output_.println(getName() + " b4 unkeyed reads");
			dq.read(0);
			retrieved++;
		    }
		}

                // let another thread examine entry.
		Thread.yield();
	    }
	}
	catch (Exception e)
	{
	    output_.println(getName() + " calling error");
	    e.printStackTrace(output_);
	    error("Unexpected Exception" + e, e);
	}
	finally
	{
	    if (function_ == LOCAL_UNKEYED_RD_WR)
	    {
		try
		{
		    deleteDQ(dq);
		}
		catch (Exception e)
		{
		    error("Unable to delete queue", e);
		}
	    }
	}
    }

    /**
     * Writes Unique keyed enties (based on thread name) to dtaq and then
     * clears all entries that match these keys.
     **/
    private void performKeyedWrClear()
    {
	try
	{
            // create byte array containing key and blank fill it to the keyLength.
	    byte[] key       = new byte[kdq.getKeyLength()];
	    byte[] nameBytes = (getName()).getBytes();
	    int j = 0;
	    for (; j < nameBytes.length; j++)
	    {
		key[j] = nameBytes[j];
	    }
	    for (; j < key.length; j++)
	    {
		key[j] = 0x20;
	    }

            // write 10 entries to the dq
	    for (int i = 0; i < 10; i++)
	    {
		String data = getThreadGroup() + getName() + i;
		kdq.write(key , data.getBytes());
	    }

            // remove all of our enties on dq.
	    kdq.clear(key);

            //verify that entries were removed.
	    if (kdq.read(key, 0, "EQ") != null)
	    {
		error("Didn't remove all relevent keyed entries");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace(output_);
	    error("Unexpected Exception" + e, e);
	}
    }

    void deleteDQ(BaseDataQueue dq) throws Exception
    {
	output_.println(getName() + "deleting dq");
	try
	{
	    dq.delete();
	}
	catch(ObjectDoesNotExistException e)
	{
	  // ignore
	}
    }
}
