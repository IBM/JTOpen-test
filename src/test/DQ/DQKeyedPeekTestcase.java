///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQKeyedPeekTestcase.java
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
  Testcase DQKeyedPeekTestcase.
  <p>Test variations for the methods:
  <ul>
  <li>KeyedDataQueue::peek(byte[])
  <li>KeyedDataQueue::peek(byte[], int, String)
  <li>KeyedDataQueue::peek(String)
  <li>KeyedDataQueue::peek(String, int, String)
  </ul>
 **/
public class DQKeyedPeekTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQKeyedPeekTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
  private boolean usingNativeOptimizations_ = false;
  protected void setup() throws Exception
  {
    if (isNative_ && systemObject_.canUseNativeOptimizations())
    {
      usingNativeOptimizations_ = true;
    }
    super.setup();
  }

    // Check for valid sender info, first 10 chars are correct server name, next 10 chars are correct user id, next 6 chars are digits, last 10 are current user id
    boolean checkSenderInfo(String senderInfo)
    {
        StringBuffer tempUser = new StringBuffer(systemObject_.getUserId());
        int userLength = tempUser.length();
        for (int x = userLength; x < 10; ++x)
        {
            tempUser.append(' ');
        }
        String currentUser = tempUser.toString();

        String user;
        String user1;
        String server;
	String server1; 
        if (usingNativeOptimizations_)
        {
            //server = "QJVACMDSRV";
            server = DQTest.SERVERNAME_NATIVE.substring(0,7);
	    server1 = DQTest.SERVERNAME_NATIVE1; 
            user = currentUser;
	    user1 = "QSHSVR"; 
        }
        else
        {
          if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
            user =   "QUSER_NC  ";
            user1 =  "QUSER_NC  ";
            
          } else { 
            user =   "QUSER     ";
	    user1 =  "QUSER     ";
          }
            server = "QZHQSSR";
	    server1= "QZHQSSR";
        }

        if (senderInfo.length() == 36 &&
	    ( senderInfo.substring(0, 7).equals(server) || senderInfo.substring(0, server1.length()).equals(server1) )
	    && ( senderInfo.substring(10, 20).equals(user) ||
		 senderInfo.substring(10, 10+user1.length()).equals(user1))
	    && senderInfo.substring(26, 36).equals(currentUser))
        {
            for (int x = 20; x < 26; ++x)
            {
                if (!Character.isDigit(senderInfo.charAt(x))) return false;
            }
            return true;
        }
        else
        {
	    output_.println("checkServerInfo returning false:  senderInfo='"+senderInfo+"' user="+user+ "user1="+user1+" server="+server+" server1="+server1); 
            return false;
        }
    }

    /**
      <p>Test:  Call KeyedDataQueue::peek(byte[]) passing a null for the key.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var001()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.peek((byte[])null);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "key");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) passing a null for the key.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var002()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.peek((byte[])null, 0, "EQ");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "key");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) passing a null for the searchType.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var003()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.peek(new byte[10], 0, (String)null);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "searchType");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]) on a data queue that does not have any entries.
      <p>Result:  Verify null is returned.
     **/
    public void Var004()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry peekEntry = dq.peek(key, 0, "EQ");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) on a data queue that does not have any entries.
      <p>Result:  Verify null is returned.
     **/
    public void Var005()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		KeyedDataQueueEntry peekEntry = dq.peek(key, 0, "EQ");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a wait of 10 on a queue that does not have any entries.
      <p>Result:  Verify null is returned after 10 seconds.
     **/
    public void Var006()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");

		Date start = new Date();
		KeyedDataQueueEntry peekEntry = dq.peek(key, 10, "EQ");  // wait up to 10 secs
		Date end = new Date();

		if (peekEntry != null)
		{
		    failed = true;
		    msg = "\nIncorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'";
		}
		long timeElapsed = (end.getTime() - start.getTime()) / 1000; // getTime() returns milliseconds
		if (timeElapsed < 0) timeElapsed = timeElapsed + 60;
		if (timeElapsed < 9 || timeElapsed > 11)
		{
		    failed = true;
		    msg += "\nIncorrect time elapsed: " + timeElapsed;
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]) on a data queue that does not have sender information.
      <p>Result:  Verify the senderInformation in the return entry is the empty string.
     **/
    public void Var007()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "key  ";
		byte[] testKey = key.getBytes("UnicodeBigUnmarked");
		String expected = "Here I am.";
		dq.write(testKey, expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

	        // do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey);
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey);
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg = "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) on a data queue that does not have sender information.
      <p>Result:  Verify the senderInformation in the return entry is the empty string.
     **/
    public void Var008()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "key  ";
		byte[] testKey = key.getBytes("UnicodeBigUnmarked");
		String expected = "Here I am.";
		dq.write(testKey, expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

	        // do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "EQ");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "EQ");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg = "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]) on a data queue that does have sender information.
      <p>Result:  Verify the senderInformation in the return entry is correct.
     **/
    public void Var009()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		String key = "key  ";
		byte[] testKey = key.getBytes("UnicodeBigUnmarked");
		String expected = "Here I go.";
		dq.write(testKey, expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

	        // do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey);
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "' expected '"+expected+"'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!checkSenderInfo(peekSender))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey);
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!checkSenderInfo(readSender))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) on a data queue that does have sender information.
      <p>Result:  Verify the senderInformation in the return entry is correct.
     **/
    public void Var010()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		String key = "key  ";
		byte[] testKey = key.getBytes("UnicodeBigUnmarked");
		String expected = "Here I go.";
		dq.write(testKey, expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

	        // do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "EQ");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!checkSenderInfo(peekSender))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "EQ");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!checkSenderInfo(readSender))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var011()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
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
		dq.peek((new String("key  ")).getBytes("UnicodeBigUnmarked"));
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var012()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
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
		dq.peek((new String("key  ")).getBytes("UnicodeBigUnmarked"), 0, "EQ");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "EQ".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var013()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = key.getBytes("UnicodeBigUnmarked");
		String expected = "Successful EQ.";
		dq.write(testKey, expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "EQ");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "EQ");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "EQ".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var014()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("AAA  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "EQ");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "EQ".  The data queue entry's key is less than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var015()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("ZZZ  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "EQ");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "NE".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var016()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("JJJ  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful NE.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "NE");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "NE");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
		}
		assertCondition(!failed, msg);
	    }
	    finally
	    {
		dq.delete();
	    }
	}
	catch  (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "NE".  The data queue entry's key is less than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var017()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("MMM  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful NE.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "NE");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "NE");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "NE".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var018()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "NE");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "LT".  The data queue entry's key is less than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var019()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("LLM  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful LT.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

	        // do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "LT");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "LT");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "LT".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var020()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("AAA  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "LT");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: " + new String(peekEntry.getData(), "UnicodeBig"));
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "LT".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var021()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "LT");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "LE".  The data queue entry's key is less than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var022()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("POO  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful LE.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "LE");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "LE");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "LE".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var023()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful LE.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "LE");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "LE");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "LE".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var024()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("GEE  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "LE");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "GT".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var025()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("LLK  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful GT.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "GT");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "GT");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: " + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "GT".  The data queue entry's key is less than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var026()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("SOS  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key, (new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "GT");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "GT".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var027()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "GT");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "GE".  The data queue entry's key is greater than the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var028()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("DOG  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful GE.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

	        // do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "GE");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "GE");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "GE".  The data queue entry's key is equal to the search key.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var029()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		String key = "LLL  ";
		byte[] testKey = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		String expected = "Successful GE.";
		dq.write(key.getBytes("UnicodeBigUnmarked"), expected.getBytes("UnicodeBigUnmarked"));
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.
		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "GE");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey, 0, "GE");
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a search string of "GE".  The data queue entry's key is less than the search key.
      <p>Result:  Verify null is returned.
     **/
    public void Var030()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] key = (new String("LLL  ")).getBytes("UnicodeBigUnmarked");
		byte[] testKey = (new String("MOM  ")).getBytes("UnicodeBigUnmarked");
		dq.write(key,(new String("whatever")).getBytes("UnicodeBigUnmarked"));
		KeyedDataQueueEntry peekEntry = dq.peek(testKey, 0, "GE");
		if (peekEntry == null)
		{
		    succeeded();
		}
		else
		{
		    failed("Incorrect data: '" + new String(peekEntry.getData(), "UnicodeBig") + "'");
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]) with a key that is too short.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var031()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.peek(new byte[5]);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR);
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a key that is too short.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var032()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.peek(new byte[5], 0, "EQ");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR);
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]) with a key that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var033()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
	        // do peek
		dq.peek((new String("LLL   ")).getBytes("UnicodeBigUnmarked"));
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR);
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a key that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var034()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
	        // do peek
		dq.peek((new String("LLL   ")).getBytes("UnicodeBigUnmarked"), 0, "EQ");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR);
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with an invalid value for the searchType.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var035()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		dq.peek(new byte[10], 0, "QW");
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
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
      <p>Test:  Call KeyedDataQueue::peek(byte[]).
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var036()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] expectedKey = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		byte[] expectedData = (new String("Here I go.")).getBytes("UnicodeBigUnmarked");
		dq.write(expectedKey, expectedData);
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// Peek
		KeyedDataQueueEntry peekEntry = dq.peek(expectedKey);
		byte[] byteData = peekEntry.getData();
		if (byteData.length != expectedData.length)
		{
		    failed = true;
		    msg = "\nIncorrect peek data length.";
		}
		else
		{
		    for (int i = 0; i < byteData.length; i++)
		    {
			if (byteData[i] != expectedData[i])
			{
			    failed = true;
			    msg += "\nBad peek byte data.";
			    i = byteData.length;
			}
		    }
		}
		byte[] byteKey = peekEntry.getKey();
		if (byteKey.length != expectedKey.length)
		{
		    failed = true;
		    msg = "\nIncorrect peek key length.";
		}
		else
		{
		    for (int i = 0; i < byteKey.length; i++)
		    {
			if (byteKey[i] != expectedKey[i])
			{
			    failed = true;
			    msg += "\nBad peek byte key.";
			    i = byteKey.length;
			}
		    }
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String).
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var037()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		byte[] expectedData = (new String("Here I go.")).getBytes("UnicodeBigUnmarked");
		byte[] expectedKey = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		dq.write(expectedKey, expectedData);
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// peek
		KeyedDataQueueEntry peekEntry = dq.peek(expectedKey, 0, "EQ");
		byte[] byteData = peekEntry.getData();
		if (byteData.length != expectedData.length)
		{
		    failed = true;
		    msg = "\nIncorrect peek data length.";
		}
		else
		{
		    for (int i = 0; i < byteData.length; i++)
		    {
			if (byteData[i] != expectedData[i])
			{
			    failed = true;
			    msg += "\nBad peek byte data.";
			    i = byteData.length;
			}
		    }
		}
		byte[] byteKey = peekEntry.getKey();
		if (byteKey.length != expectedKey.length)
		{
		    failed = true;
		    msg = "\nIncorrect peek key length.";
		}
		else
		{
		    for (int i = 0; i < byteKey.length; i++)
		    {
			if (byteKey[i] != expectedKey[i])
			{
			    failed = true;
			    msg += "\nBad peek byte key.";
			    i = byteKey.length;
			}
		    }
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
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) with a wait time of -1.
      <p>Result:  Verify the return entry is correct.
     **/
    public void Var038()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80);
	    try
	    {
		(new DQWriter(dq,output_)).start();

		String key = "wait ";
		byte[] testKey = key.getBytes("UnicodeBigUnmarked");
		String expected = "Infinite wait data";
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		KeyedDataQueueEntry peekEntry = dq.peek(testKey, -1, "EQ");
		String peekData = new String(peekEntry.getData(), "UnicodeBig");
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = new String(peekEntry.getKey(), "UnicodeBig");
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!peekSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(testKey);
		String readData = new String(readEntry.getData(), "UnicodeBig");
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = new String(readEntry.getKey(), "UnicodeBig");
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!readSender.equals(""))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::peek(byte[]).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var039()
    {
	int RETRIES = 20; 
	if (usingNativeOptimizations_)
	{
	    notApplicable("Running natively");
	}
	else
	{
	    try
	    {
		KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQLIB+".LIB/CD1EKTST.DTAQ");

		    for (int i = 0; i < RETRIES; i++) {
			output_.println("Running drop iteration "+i); 
			dq.create(10, 80);
			byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");

			ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
			drop.start();
			try
			{

			    dq.peek(key);
			    drop.join();
			    dq.delete();
			    // Keep trying in loop 
			    // notApplicable("ConnectionDropper"); // Threads did not force condition
			}
			catch (Exception e)
			{
			    drop.join();
			    dq.delete();
			    assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			    return; 
			}
		    }
		assertCondition(true,"Warning:  Unable to force race condition"); 
	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::peek(byte[], int, String).
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
         // Avoid hang at peek(-1) when running in proxy mode.
         String propVal = System.getProperty("com.ibm.as400.access.AS400.proxyServer");
         if (propVal != null && propVal.length() != 0)
         {
           notApplicable("Running in proxy mode");
           return;
         }

	    try
	    {
		KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQLIB+".LIB/CD2EKTST.DTAQ");
		dq.create(10, 80);
		try
		{
		    byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");

                    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    drop.start();
		    try
		    {
			dq.peek(key, -1, "EQ");
			notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
		    }
		    finally
		    {
			drop.join();
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

    /**
      <p>Test:  Call KeyedDataQueue::peek(byte[]) on a data queue to which the user does not have enough authority to the data queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var041()
    {
	try
	{
	    cmdRun("QSYS/CRTDTAQ "+DQTest.DQSECLIB+"/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		String user = systemObject_.getUserId();
		cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");

		KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
		dq.getDescription();

		cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " *OBJOPR");
		try
		{
		    dq.peek((new String("key  ")).getBytes("UnicodeBigUnmarked"));
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ "+DQTest.DQSECLIB+"/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) on a data queue to which the user does not have enough authority to the data queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var042()
    {
	try
	{
	    cmdRun("QSYS/CRTDTAQ "+DQTest.DQSECLIB+"/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		String user = systemObject_.getUserId();
		cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");

		KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
		dq.getDescription();

		cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " *OBJOPR" );
		try
		{
		    dq.peek((new String("key  ")).getBytes("UnicodeBigUnmarked"), 0, "EQ");
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ "+DQTest.DQSECLIB+"/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::peek(byte[]) on a data queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var043()
    {
	try
	{
	    cmdRun("QSYS/CRTDTAQ "+DQTest.DQSECLIB+"/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		String user = systemObject_.getUserId();
		cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " *EXECUTE");

		KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
		try
		{
		    dq.peek((new String("key  ")).getBytes("UnicodeBigUnmarked"));
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ "+DQTest.DQSECLIB+"/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::peek(byte[], int, String) on a data queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var044()
    {
	try
	{
	    cmdRun("QSYS/CRTDTAQ "+DQTest.DQSECLIB+"/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		String user = systemObject_.getUserId();
		cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " *EXECUTE");

		KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
		try
		{
		    dq.peek((new String("key  ")).getBytes("UnicodeBigUnmarked"), 0, "EQ");
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
		}
	    }
	    finally
	    {
		cmdRun("QSYS/DLTDTAQ "+DQTest.DQSECLIB+"/SECTST");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::peek(String) on a queue that does have sender information.
      <p>Result:  Verify the sender information in the return entry.
     **/
    public void Var045()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		String key = "key  ";
		String expected = "Here I go.";
		dq.write(key, expected);
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(key);
		String peekData = peekEntry.getString();
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = peekEntry.getKeyString();
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!checkSenderInfo(peekSender))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(key);
		String readData = readEntry.getString();
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = readEntry.getKeyString();
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!checkSenderInfo(readSender))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
      <p>Test:  Call KeyedDataQueue::peek(String, int, String) on a queue that does have sender information.
      <p>Result:  Verify the sender information in the return entry.
     **/
    public void Var046()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/KPEEKTST.DTAQ");
	    dq.create(10, 80, "*USE", true, false, "");
	    try
	    {
		String key = "key  ";
		String expected = "Here I go.";
		dq.write(key, expected);
		boolean failed = false;  // Keeps track of failure in multi-part tests.
		String msg = "";      // Keeps track of reason for failure in multi-part tests.

		// do peek
		KeyedDataQueueEntry peekEntry = dq.peek(key, 0, "EQ");
		String peekData = peekEntry.getString();
		if (!expected.equals(peekData))
		{
		    failed = true;
		    msg = "\nIncorrect peek data: '" + peekData + "'";
		}
		String peekKey = peekEntry.getKeyString();
		if (!key.equals(peekKey))
		{
		    failed = true;
		    msg += "\nIncorrect peek key: '" + peekKey + "'";
		}
		String peekSender = peekEntry.getSenderInformation();
		if (!checkSenderInfo(peekSender))
		{
		    failed = true;
		    msg += "\nIncorrect peek sender info: '" + peekSender + "'";
		}

	        // verify peek did not remove entry from queue
		KeyedDataQueueEntry readEntry = dq.read(key, 0, "EQ");
		String readData = readEntry.getString();
		if (!expected.equals(readData))
		{
		    failed = true;
		    msg += "\nIncorrect read data: '" + readData + "'";
		}
		String readKey = readEntry.getKeyString();
		if (!key.equals(readKey))
		{
		    failed = true;
		    msg += "\nIncorrect read key: '" + readKey + "'";
		}
		String readSender = readEntry.getSenderInformation();
		if (!checkSenderInfo(readSender))
		{
		    failed = true;
		    msg += "\nIncorrect read sender info: '" + readSender + "'";
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
}
