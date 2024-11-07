///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQKeyedReadTestcase.java
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
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.KeyedDataQueueEntry;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ConnectionDropper;
import test.DQTest;
import test.JDTestDriver;
import test.Testcase;

/**
  Testcase DQKeyedReadTestcase.
  <p>Test variations for the methods:
  <ul>
  <li>KeyedDataQueue::read(byte[])
  <li>KeyedDataQueue::read(byte[], int, String)
  <li>KeyedDataQueue::read(String)
  <li>KeyedDataQueue::read(String, int, String)
  </ul>
 **/
public class DQKeyedReadTestcase extends Testcase
{
  String expectedQuser = null; 
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQKeyedReadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
  private boolean usingNativeOptimizations_ = false;

  static int DROPPER_RETRIES = 20;
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) passing a null for the key.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var001()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.read((byte[])null);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionIs(e, "NullPointerException", "key"))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) passing a null for the key.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var002()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.read((byte[])null, 0, "EQ");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionIs(e, "NullPointerException", "key"))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) passing a null for the searchType.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var003()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = new byte[10];
		dq.read(key, 0, (String)null);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionIs(e, "NullPointerException", "searchType"))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) on a data queue that does not have any entries.
      <p>Result:  Verify null is returned.
     **/
    public void Var004()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) on a data queue that does not have any entries.
      <p>Result:  Verify null is returned.
     **/
    public void Var005()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a wait time of 10 on a queue that does not have any entries.
      <p>Result:  Verify null is returned after 10 seconds.
     **/
    public void Var006()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		Date start = new Date();
		KeyedDataQueueEntry data = dq.read(key, 10, "EQ");  // wait up to 10 secs
		Date end = new Date();
		if (data != null)
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) on a data queue that does not have sender information.
      <p>Result:  Verify the senderInformation in the return entry is the empty string.
     **/
    public void Var007()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I am.");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		KeyedDataQueueEntry data = dq.read(key);
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
		data = dq.read(key);
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) on a data queue that does not have sender information.
      <p>Result:  Verify the senderInformation in the return entry is the empty string.
     **/
    public void Var008()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I am.");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
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
		data = dq.read(key, 0, "EQ");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) on a data queue that does have sender information.
      <p>Result:  Verify the senderInformation in the return entry is correct.
     **/
    public void Var009()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I go.");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    String user = getPaddedUser();
	    String job = "QZHQSSRV".substring(0,7);
	    if (usingNativeOptimizations_) job = DQTest.SERVERNAME_NATIVE.substring(0,7); 
	    String sender = (usingNativeOptimizations_) ? user : expectedQuser;

	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		KeyedDataQueueEntry data = dq.read(key);
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
		data = dq.read(key);
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) on a data queue that does have sender information.
      <p>Result:  Verify the senderInformation in the return entry is correct.
     **/
    public void Var010()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I go.");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    String user = getPaddedUser();
	    String job = "QZHQSSRV".substring(0,7);
	    if (usingNativeOptimizations_) job = DQTest.SERVERNAME_NATIVE.substring(0,7); 
	    String sender = (usingNativeOptimizations_) ? user : expectedQuser;

	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
	        // do read
		KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
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
		data = dq.read(key, 0, "EQ");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var011()
    {
	try
	{
	    // boolean failed = false;  // Keeps track of failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
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
		dq.read(key);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
		}
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var012()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
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
		dq.read(key, 0, "EQ");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionIs(e, "ObjectDoesNotExistException", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
		}
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "EQ".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var013()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful EQ.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "EQ");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals((new String(data.getKey(), "UnicodeBig"))))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "EQ");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "EQ".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var014()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("AAA  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "EQ");
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
		try {dq.delete();} catch (Exception e) {}
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "EQ".  The data queue entry's key is less than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var015()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("ZZZ  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "EQ");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "NE".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var016()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful NE.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("JJJ  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "NE");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals((new String(data.getKey(), "UnicodeBig"))))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "NE");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "NE".  The data queue entry's key is less than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var017()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful NE.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("MMM  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "NE");
		if (!expected.equals(new String(data.getData(),"UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key,"UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(), "UnicodeBig") + "'";
		    msg += "\nExpected key:  '" + new String(key, "UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "NE");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "NE".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var018()
    {
	try
	{
	    // boolean failed = false;  // Keeps track of failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "NE");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "LT".  The data queue entry's key is less than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var019()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful LT.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLM  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "LT");
		if (!expected.equals(new String(data.getData(),"UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key,"UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "LT");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "LT".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var020()
    {
	try
	{
	    // boolean failed = false;  // Keeps track of failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("AAA  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "LT");
		if (data == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: " + new String(data.getData(), "UnicodeBig"));
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "LT".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var021()
    {
	try
	{
	    // boolean failed = false;  // Keeps track of failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "LT");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "LE".  The data queue entry's key is less than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var022()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful LE.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("PPP  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "LE");
		if (!expected.equals(new String(data.getData(),"UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "LE");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "LE".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var023()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful LE.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "LE");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "LE");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "LE".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var024()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("GEE  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "LE");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "GT".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var025()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful GT.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLK  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "GT");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "GT");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "GT".  The data queue entry's key is less than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var026()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("SOS  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "GT");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "GT".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var027()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "GT");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "GE".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var028()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful GE.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("DOG  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "GE");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "GE");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "GE".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var029()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Successful GE.");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected.getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	        // do read
		KeyedDataQueueEntry data = dq.read(readKey, 0, "GE");
		if (!expected.equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!(new String(key, "UnicodeBig")).equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(),"UnicodeBig") + "'";
		}
	        // verify entry removed from queue
		data = dq.read(readKey, 0, "GE");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a search string of "GE".  The data queue entry's key is less than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var030()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		byte[] readKey = (new String("MOM  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(readKey, 0, "GE");
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) with a key that is too short.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var031()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = new byte[5];
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.read(key);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a key that is too short.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var032()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte[] key = new byte[5];
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.read(key, 0, "EQ");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[]) with a key that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var033()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = "Key too long.";
	    byte[] readKey = "LLL   ".getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
	        // do read
		dq.read(readKey);
		failed("No exception for "+expected);
	    }
	    catch (Exception e)
	    {
		if (exceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a key that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var034()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Key too long.");
	    byte[] readKey = (new String("LLL   ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
	        // do read
		dq.read(readKey, 0, "EQ");
		failed("No exception."+expected);
	    }
	    catch (Exception e)
	    {
		if (exceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
		}
	    }
	    finally
	    {
		try {dq.delete();} catch (Exception e) {}
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with an invalid value for the searchType.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var035()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    byte key[] = new byte[10];
	    dq.create(10, 80);
	    try
	    {
		dq.read(key, 0, "QW");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
		{
		    succeeded();
		}
		else
		{
		    failed(e, "Wrong exception info.");
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
      <p>Test:  Call KeyedDataQueue::read(byte[]).
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var036()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I go.");
	    byte[] expectedData = expected.getBytes("UnicodeBigUnmarked");
	    byte[] expectedKey = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		dq.write((new String("key  ")).getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(key);
		byte[] byteData = data.getData();
		if (byteData.length != expectedData.length)
		{
		    failed = true;
		    msg = "\nIncorrect data length.";
		}
		else
		{
		    for (int i = 0; i < byteData.length; i++)
		    {
			if (byteData[i] != expectedData[i])
			{
			    failed = true;
			    msg += "\nBad byte data.";
			    i = byteData.length;
			}
		    }
		}
		byte[] byteKey = data.getKey();
		if (byteKey.length != expectedKey.length)
		{
		    failed = true;
		    msg += "\nIncorrect data length.";
		}
		else
		{
		    for (int i = 0; i < byteKey.length; i++)
		    {
			if (byteKey[i] != expectedKey[i])
			{
			    failed = true;
			    msg += "\nBad byte key.";
			    i = byteKey.length;
			}
		    }
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String).
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var037()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I go.");
	    byte[] expectedData = expected.getBytes("UnicodeBigUnmarked");
	    byte[] expectedKey = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    dq.create(10, 80);
	    try
	    {
		dq.write(expectedKey, expected.getBytes("UnicodeBigUnmarked"));
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
		byte[] byteData = data.getData();
		if (byteData.length != expectedData.length)
		{
		    failed = true;
		    msg = "\nIncorrect data length.";
		}
		else
		{
		    for (int i = 0; i < byteData.length; i++)
		    {
			if (byteData[i] != expectedData[i])
			{
			    failed = true;
			    msg += "\nBad byte data.";
			    i = byteData.length;
			}
		    }
		}
		byte[] byteKey = data.getKey();
		if (byteKey.length != expectedKey.length)
		{
		    failed = true;
		    msg = "\nIncorrect data length.";
		}
		else
		{
		    for (int i = 0; i < byteKey.length; i++)
		    {
			if (byteKey[i] != expectedKey[i])
			{
			    failed = true;
			    msg += "\nBad byte key.";
			    i = byteKey.length;
			}
		    }
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) with a wait time of -1.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var038()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    dq.create(10,80);
	    try
	    {
		DQWriter writer = new DQWriter(dq);
		writer.start();
		byte key[] = (new String("wait ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry data = dq.read(key, -1, "EQ");
		if (!new String("Infinite wait data").equals(new String(data.getData(), "UnicodeBig")))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(data.getData(), "UnicodeBig") + "'";
		}
		if (!new String("wait ").equals(new String(data.getKey(), "UnicodeBig")))
		{
		    failed = true;
		    msg += "\nIncorrect key: '" + new String(data.getKey(), "UnicodeBig") + "'";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::read(byte[]).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var039()
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
		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD1ADTST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		    dq.create(10,80);
		    try
		    {
			drop.start();
			dq.read(key);
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			// Try again... 
		    }
		    catch (Exception e)
		    {
			if (exceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED))
			{
			    succeeded();
			}
			else
			{
			    failed(e, "Wrong exception info.");
			}
			return; 
		    }
		    finally
		    {
			drop.join();
			dq.delete();
		    }
		}
		System.out.println("Drop test did not work in "+DROPPER_RETRIES);
		assertCondition(true); 
		
	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::read(byte[], int, String).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var040()
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
		 KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD2ADTST.DTAQ");
		 byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		 ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		 dq.create(10,80);
		 try
		 {
		     drop.start();
		     dq.read(key, -1, "EQ");
		     // notApplicable("ConnectionDropper"); // Threads did not force condition
		     // Loop and try again
		 }
		 catch (Exception e)
		 {
		     if (exceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED))
		     {
			 succeeded();
		     }
		     else
		     {
			 failed(e, "Wrong exception info.");
		     }
		     return; 
		 }
		 finally
		 {
		     dq.delete();
		 }
	     }
	     System.out.println("Drop test did not work in "+DROPPER_RETRIES);
	     assertCondition(true); 

		

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[]) on a data queue to which the user does not have enough authority to the data queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
    **/
    public void Var041()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR" );
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		try
		{
		    dq.read(key);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
		    {
			succeeded();
		    }
		    else
		    {
			failed(e, "Wrong exception info.");
		    }
		}
	    }
	    finally
	    {
		cmdRun("DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) on a data queue to which the user does not have enough authority to the data queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var042()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR" );
		try
		{
		    dq.read(key, 0, "EQ");
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
		    {
			succeeded();
		    }
		    else
		    {
			failed(e, "Wrong exception info.");
		    }
		}
	    }
	    finally
	    {
		cmdRun("DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[]) on a data queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var043()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
//		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		try
		{
		    dq.read(key);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
		    {
			succeeded();
		    }
		    else
		    {
			failed(e, "Wrong exception info.");
		    }
		}
	    }
	    finally
	    {
		cmdRun("DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(byte[], int, String) on a data queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var044()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
//		dq.getDescription();
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE" );
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		try
		{
		    dq.read(key, 0, "EQ");
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
		    {
			succeeded();
		    }
		    else
		    {
			failed(e, "Wrong exception info.");
		    }
		}
	    }
	    finally
	    {
		cmdRun("DLTDTAQ DQSECTEST/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::read(String) on a queue that does have sender information.
      <p>Result:  Verify the sender information in the return entry.
     **/
    public void Var045()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I go.");
	    String key = "key  ";
	    String user = getPaddedUser();
	    String job = "QZHQSSRV".substring(0,7);
	    if (usingNativeOptimizations_) job = DQTest.SERVERNAME_NATIVE.substring(0,7); 
	    String sender = (usingNativeOptimizations_) ? user : expectedQuser;
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected);
	        // do read
		KeyedDataQueueEntry data = dq.read(key);
		if (!expected.equals(data.getString()))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + data.getString() + "'";
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
		data = dq.read(key);
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
      <p>Test:  Call KeyedDataQueue::read(String, int, String) on a queue that does have sender information.
      <p>Result:  Verify the sender information in the return entry.
     **/
    public void Var046()
    {
	try
	{
	    boolean failed = false;  // Keeps track of failure in multi-part tests.
	    String msg = "";      // Keeps track of reason for failure in multi-part tests.
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KREADTST.DTAQ");
	    String expected = new String("Here I go.");
	    String key = "key  ";
	    String user = getPaddedUser();
	    String job = "QZHQSSRV".substring(0,7);
	    if (usingNativeOptimizations_) job = DQTest.SERVERNAME_NATIVE.substring(0,7); 
	    String sender = (usingNativeOptimizations_) ? user : expectedQuser;
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		dq.write(key, expected);
		// do read
		KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
		if (!expected.equals(data.getString()))
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + data.getString() + "'";
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
		data = dq.read(key, 0, "EQ");
		if (data != null)
		{
		    failed = true;
		    msg += "\nEntry not removed from queue.";
		}
		if (failed)
		{
		    failed(msg);
		}
		else
		{
		    succeeded();
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
}
