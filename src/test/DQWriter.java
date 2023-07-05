///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQWriter.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.KeyedDataQueue;

/**
  Helper class DQWriter.
 **/
public class DQWriter extends Thread
{
    // Only one of these variables will be non-null, depending on
    // which ctor is used.
    DataQueue dq = null;
    KeyedDataQueue kdq = null;
    DataQueue dq2 = null;
    KeyedDataQueue kdq2 = null;
    String key2;

    String data = "Infinite wait data";
    String key = "wait ";

    String documentData;

    public DQWriter(DataQueue dq)
    {
	super();
	this.dq = new DataQueue(dq.getSystem(), dq.getPath());
    }

    public DQWriter(KeyedDataQueue dq)
    {
	super();
	this.kdq = new KeyedDataQueue(dq.getSystem(), dq.getPath());
    }

    public DQWriter(DataQueue x, String s)
    {
	super();
	this.dq2 = x;
	documentData = s;
    }

    public DQWriter(KeyedDataQueue x, String key, String s)
    {
	super();
	this.kdq2 = x;
	this.key2 = key;
	documentData = s;
    }

    public void run()
    {
	try
	{
	    // wait for 10 seconds
	    Thread.sleep(1000);

	    // write entry to the queue
	    if (this.dq != null)
	    {
		this.dq.write(data.getBytes("UnicodeBigUnmarked"));
	    }
	    else if (kdq != null)
	    {
		this.kdq.write(key.getBytes("UnicodeBigUnmarked"), data.getBytes("UnicodeBigUnmarked"));
	    }
	    else if (dq2 != null)
	    {
		this.dq2.write(documentData);
	    }
	    else if (kdq2 != null)
	    {
		this.kdq2.write(key2, documentData);
	    }
	}
	catch (Exception e)  // can't throw exception cuz parent didn't
	{
	    System.out.println("Exception during write");
	    e.printStackTrace();
	}
    }
}
