///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQReadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DQ;

import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueEntry;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ConnectionDropper;
import test.DQTest;
import test.JDTestDriver;
import test.Testcase;

/**
  Testcase DQReadTestcase.
  <p>Test variations for the methods:
  <ul>
  <li>DataQueue::read()
  <li>DataQueue::read(int)
  </ul>
 **/
public class DQReadTestcase extends Testcase
{
  public String expectedQuser = null; 
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQReadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
  static int DROPPER_RETRIES = 10;

  private boolean usingNativeOptimizations_ = false;
  protected void setup() throws Exception
  {
    if (isNative_ && systemObject_.canUseNativeOptimizations())
    {
      usingNativeOptimizations_ = true;
    }
    super.setup();
    if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
      expectedQuser =   "QUSER_NC  ";
     
    } else { 
      expectedQuser =   "QUSER     ";
    }

  }

    String getPaddedUser()
    {
	String user = systemObject_.getUserId();
	int userLength = user.length();
	StringBuffer paddedUser = new StringBuffer(user);
	for (int x = userLength; x < 10; ++x)
	{
	    paddedUser.append(' ');
	}
	return paddedUser.toString();
    }

    /**
      <p>Test:  Call DataQueue::read() on a data queue that does not have any entries.
      <p>Result:  Verify null is returned.
     **/
    public void Var001()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    dq.create(80);
	    try
	    {
		DataQueueEntry data = dq.read();
		if (data == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(data.getData(), "UnicodeBig") + "'");
		}
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) on a data queue that does not have any entries.
      <p>Result:  Verify null is returned.
     **/
    public void Var002()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    dq.create(80);
	    try
	    {
		DataQueueEntry data = dq.read(0);
		if (data == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(data.getData(), "UnicodeBig") + "'");
		}
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) with a wait of 10 on a queue that does not have any entries.
      <p>Result:  Verify null is returned after 10 seconds.
     **/
    public void Var003()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    dq.create(80);
	    try
	    {
		Date start = new Date();
		DataQueueEntry data = dq.read(10);  // wait up to 10 secs
		Date end = new Date();
		if (data != null)
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig" + "'");
		}
		long timeElapsed = (end.getTime() - start.getTime()) / 1000; // getTime() returns milliseconds
		if (timeElapsed < 0) timeElapsed = timeElapsed + 60;
		if (timeElapsed < 9 || timeElapsed > 11)
		{
		    failed = true;
		    msg += "\nIncorrect time elapsed: '" + timeElapsed + "'";
		}
		assertCondition(!failed, msg);
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read() on a data queue that does not have sender information.
      <p>Result:  Verify the senderInformation in the return entry is the empty string.
     **/
    public void Var004()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    String expected = new String("Here I am.");
	    dq.create(80);
	    try
	    {
		dq.write(expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		DataQueueEntry data = dq.read();
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!data.getSenderInformation().equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect sender info: '" + data.getSenderInformation() + "'";
		}
	        // verify entry removed from queue
		data = dq.read();
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		assertCondition(!failed, msg);
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) on a data queue that does not have sender information.
      <p>Result:  Verify the senderInformation in the return entry is the empty string.
     **/
    public void Var005()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    String expected = new String("Here I am.");
	    dq.create(80);
	    try
	    {
		dq.write(expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		DataQueueEntry data = dq.read(0);
		if (!expected.equals(new String(data.getData(),"UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!data.getSenderInformation().equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect sender info: '" + data.getSenderInformation() + "'";
		}
	        // verify entry removed from queue
		data = dq.read();
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		assertCondition(!failed, msg);
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read() on a data queue that does have sender information.
      <p>Result:  Verify the senderInformation in the return entry is correct.
     **/
    public void Var006()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    String expected = new String("Here I go.");
	    String user = getPaddedUser();
	    String job = (usingNativeOptimizations_) ? DQTest.SERVERNAME_NATIVE.substring(0,7)  : "QZHQSSRV".substring(0,7);
	    String sender = (usingNativeOptimizations_) ? user : expectedQuser;
	    dq.create(80, "*USE", true, true, false, "");
	    try
	    {
		dq.write(expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		DataQueueEntry data = dq.read();
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig" + "'");
		}
		if (data.getSenderInformation() == null ||
		    data.getSenderInformation().length() != 36 ||
		    !(data.getSenderInformation().substring(0,7).equals(job) ||
		      data.getSenderInformation().substring(0,8).equals("QJVAEXEC")) || 
		    !(data.getSenderInformation().substring(10,20).equals(sender)  ||
		      data.getSenderInformation().substring(10,16).equals("QSHSVR")))
		{
		    failed = true;
		    msg += "\nIncorrect sender info: '" + data.getSenderInformation() + "'";
		}
	        // verify entry removed from queue
		data = dq.read();
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		assertCondition(!failed, msg);
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) on a data queue that does have sender information.
      <p>Result:  Verify the senderInformation in the return entry is correct.
     **/
    public void Var007()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    String expected = new String("Here I go.");
	    String user = getPaddedUser();
	    String job = (usingNativeOptimizations_) ? DQTest.SERVERNAME_NATIVE.substring(0,7)  : "QZHQSSRV".substring(0,7);
	    String sender = (usingNativeOptimizations_) ? user : expectedQuser;

	    dq.create(80, "*USE", true, true, false, "");
	    try
	    {
		dq.write(expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		DataQueueEntry data = dq.read(0);
		if (!expected.equals(new String(data.getData(),"UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (data.getSenderInformation() == null ||
		    data.getSenderInformation().length() != 36 ||
		    !(data.getSenderInformation().substring(0,7).equals(job) ||
		      data.getSenderInformation().substring(0,8).equals("QJVAEXEC")) || 
		    !(data.getSenderInformation().substring(10,20).equals(sender)  ||
		      data.getSenderInformation().substring(10,16).equals("QSHSVR")))
		{
		    failed = true;
		    msg += "\nIncorrect sender info: '" + data.getSenderInformation() + "'";
		}
	        // verify entry removed from queue
		data = dq.read();
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		assertCondition(!failed, msg);
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read() on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var008()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    dq.create(80);
	    try
	    {
		dq2.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    try
	    {
		dq.read();
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var009()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    dq.create(80);
	    try
	    {
		dq2.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    try
	    {
		dq.read(0);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read().
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var010()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    String expected = "Here I go.";
	    byte[] expectedData = expected.getBytes("UnicodeBigUnmarked");
	    dq.create(80);
	    try
	    {
		dq.write(expectedData);
		DataQueueEntry data = dq.read();
		byte[] byteData = data.getData();
		if (byteData.length != expectedData.length)
		{
		    failed("Incorrect data length.");
		}
		else
		{
		    for (int i = 0; i < byteData.length; i++)
		    {
			if (byteData[i] != expectedData[i])
			{
			    failed = true;
			    failed("Bad byte data.");
			    i = byteData.length;
			}
		    }
		    assertCondition(!failed);
		}
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int).
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var011()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    String expected = "Here I go.";
	    byte[] expectedData = expected.getBytes("UnicodeBigUnmarked");
	    dq.create(80);
	    try
	    {
		dq.write(expectedData);
		DataQueueEntry data = dq.read(0);
		byte[] byteData = data.getData();
		if (byteData.length != expectedData.length)
		{
		    failed("Incorrect data length.");
		}
		else
		{
		    for (int i = 0; i < byteData.length; i++)
		    {
			if (byteData[i] != expectedData[i])
			{
			    failed = true;
			    failed("Bad byte data.");
			    i = byteData.length;
			}
		    }
		    assertCondition(!failed);
		}
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) with a wait time of -1.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var012()
    {
	try
	{
	    // boolean failed = false;  // Keeps track of failure in multi-part tests.
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAQ");
	    DQWriter writer = new DQWriter(dq);
	    dq.create(80);
	    try
	    {
		writer.start();
		DataQueueEntry data = dq.read(-1);
		assertCondition(new String(data.getData(), "UnicodeBig").equals("Infinite wait data"), "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'");
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to DataQueue::read().
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var013()
    {
	if (usingNativeOptimizations_)
	{
	    notApplicable("Running natively");
	}
	else
	{
	    try
	    {
		for (int i = 0; i < DROPPER_RETRIES; i++) { 
		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD1DTEST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(80);
		    try
		    {
			drop.start();
			dq.read();
			// try again 
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			drop = null; 
			dq.delete();
			dq = null; 

		    }
		    catch (Exception e)
		    {
			if (drop != null) drop.join();
			if (dq != null) dq.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning Dropper failed");
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to DataQueue::read(int).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var014()
    {
	if (usingNativeOptimizations_)
	{
	    notApplicable("Running natively");
	}
	else
	{
         // Avoid hang at read(-1) when running in proxy mode.
         String propVal = System.getProperty("com.ibm.as400.access.AS400.proxyServer");
         if (propVal != null && propVal.length() != 0)
         {
           notApplicable("Running in proxy mode");
           return;
         }

	    try
	    {
		for (int i = 0; i < DROPPER_RETRIES; i++) { 

		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD2DTEST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(80);
		    try
		    {
			drop.start();
			dq.read(-1);
			notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			drop = null; 
			dq.delete();
			dq = null; 
		    }
		    catch (Exception e)
		    {
			if (drop != null) drop.join();
			if (dq != null) dq.delete();

			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning Dropper failed");
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Call DataQueue::read() on a data queue to which the user does not have enough authority to the data queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var015()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("QSYS/CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("QSYS/GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR");
		try
		{
		    dq.read();
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) on a data queue to which the user does not have enough authority to the data queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var016()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("QSYS/CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("QSYS/GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR");
		try
		{
		    dq.read(0);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read() on a data queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var017()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("QSYS/CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
//		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
		try
		{
		    dq.read();
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::read(int) on a data queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var018()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("QSYS/CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
//		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE" );
		try
		{
		    dq.read(0);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }
}
