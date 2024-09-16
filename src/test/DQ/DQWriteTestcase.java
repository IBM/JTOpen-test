///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQWriteTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DQ;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueEntry;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.KeyedDataQueueEntry;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ConnectionDropper;
import test.DQTest;
import test.Testcase;

/**
  Testcase DQWriteTestcase.
  <p>Test variations for the methods:
  <ul>
  <li>DataQueue::write(byte[])
  <li>KeyedDataQueue::write(byte[], byte[])
  <li>DataQueue::write(String)
  <li>KeyedDataQueue::write(String, String)
  </ul>
 **/
public class DQWriteTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQWriteTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }

    int DROPPER_RETRIES = 10; 
    private String verifyWrite(DataQueue dq, String expected) throws Exception
    {
	String failMsg = "";

	// Verify write worked by reading.
	DataQueueEntry entry = dq.read();
	if (entry == null)
	{
	    failMsg += "\nEntry equals null";
	}
	else
	{
	    String byteData = new String(entry.getData(), "UnicodeBig");
	    String stringData = entry.getString();
	    String sender = entry.getSenderInformation();
	    if (!byteData.equals(expected)) failMsg += "\nByte Data Received: '" + byteData + "'\nExpected: '" + expected + "'";
	    if (!stringData.equals(expected)) failMsg += "\nString Data Received: '" + stringData + "'\nExpected: '" + expected + "'";
	    if (!sender.equals("")) failMsg += "\nSender Information Received: '" + sender + "'\nExpected empty string (\"\")";
	}
	return failMsg;
    }

    private String verifyKeyedWrite(KeyedDataQueue dq, String expectedKey, String expectedData) throws Exception
    {
	String failMsg = "";

	// Verify write worked by reading.
	KeyedDataQueueEntry entry = dq.read(expectedKey);
	if (entry == null)
	{
	    failMsg += "\nEntry equals null";
	}
	else
	{
	    String byteKey = new String(entry.getKey(), "UnicodeBig");
	    String stringKey = entry.getKeyString();
	    String byteData = new String(entry.getData(), "UnicodeBig");
	    String stringData = entry.getString();
	    String sender = entry.getSenderInformation();
	    if (!byteKey.equals(expectedKey)) failMsg += "\nByte Key Received: '" + byteKey + "'\nExpected: '" + expectedKey + "'";
	    if (!stringKey.equals(expectedKey)) failMsg += "\nString Key Received: '" + stringKey + "'\nExpected: '" + expectedKey + "'";
	    if (!byteData.equals(expectedData)) failMsg += "\nByte Data Received: '" + byteData + "'\nExpected: '" + expectedData + "'";
	    if (!stringData.equals(expectedData)) failMsg += "\nString Data Received: '" + stringData + "'\nExpected: '" + expectedData + "'";
	    if (!sender.equals("")) failMsg += "\nSender Information Received: '" + sender + "'\nExpected empty string (\"\")";
	}
	return failMsg;
    }

    private String verifyBigWrite(DataQueue dq, int length) throws Exception
    {
	String failMsg = "";

	// Verify write worked by reading.
	DataQueueEntry entry = dq.read();
	if (entry == null)
	{
	    failMsg += "\nEntry equals null";
	}
	else
	{
	    byte[] byteData = entry.getData();
	    char[] stringData = entry.getString().toCharArray();
	    String sender = entry.getSenderInformation();
	    for (int i = 0; i < length; ++i)
	    {
		if (byteData[i] != 0x77) failMsg += "\nByte[" + i + "]: " + byteData[i];
 	    }
	    for (int i = 0; i < length/2; ++i)
	    {
		if (stringData[i] != 0x7777) failMsg += "\nChar[" + i + "]: " + stringData[i];
	    }
	    if (!sender.equals("")) failMsg += "\nSender Information Received: '" + sender + "'\nExpected empty string (\"\")";
	}
	return failMsg;
    }

    private String verifyBigKeyedWrite(KeyedDataQueue dq, String expectedKey, int length) throws Exception
    {
	String failMsg = "";

	// Verify write worked by reading.
	KeyedDataQueueEntry entry = dq.read(expectedKey);
	if (entry == null)
	{
	    failMsg += "\nEntry equals null";
	}
	else
	{
	    String byteKey = new String(entry.getKey(), "UnicodeBig");
	    String stringKey = entry.getKeyString();
	    byte[] byteData = entry.getData();
	    char[] stringData = entry.getString().toCharArray();
	    String sender = entry.getSenderInformation();
	    if (!byteKey.equals(expectedKey)) failMsg += "\nByte Key Received: " + byteKey + " Expected: " + expectedKey;
	    if (!stringKey.equals(expectedKey)) failMsg += "\nString Key Received: " + stringKey + " Expected: " + expectedKey;
	    for (int i = 0; i < length; ++i)
	    {
		if (byteData[i] != 0x77) failMsg += "\nByte[" + i + "]: " + byteData[i];
 	    }
	    for (int i = 0; i < length/2; ++i)
	    {
		if (stringData[i] != 0x7777) failMsg += "\nChar[" + i + "]: " + stringData[i];
	    }
	    if (!sender.equals("")) failMsg += "\nSender Information Received: '" + sender + "'\nExpected empty string (\"\")";
	}
	return failMsg;
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]).
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var001()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(80);
	    try
	    {
		byte[] data = (new String("data")).getBytes("UnicodeBigUnmarked");
		dq.write(data);
		dq.setCcsid(0xf200);
		String failMsg = verifyWrite(dq, "data");
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]).
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var002()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		byte[] data = (new String("data")).getBytes("UnicodeBigUnmarked");
		dq.write(key, data);
		dq.setCcsid(0xf200);
		String failMsg = verifyKeyedWrite(dq, "key  ", "data");
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String).
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var003()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(80);
	    try
	    {
		dq.setCcsid(0xf200);
		String data = "data";
		dq.write(data);
		String failMsg = verifyWrite(dq, "data");
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String).
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var004()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.setCcsid(0xf200);
		String key = "key  ";
		String data = "data";
		dq.write(key, data);
		String failMsg = verifyKeyedWrite(dq, "key  ", "data");
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing a null for the key.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var005()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] key = null;
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    dq2.create(10, 80);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "key");
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing a null for the key.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var006()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    String key = null;
	    String data = "data";
	    dq2.create(10, 80);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "key");
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing a key that is not long enough.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var007()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] key = new byte[4];
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    dq2.create(10, 80);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing a key that is not long enough.
      <p>Result:  Verify the key was zero padded and the data queue entry was successfully written.
     **/
    public void Var008()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.setCcsid(0xf200);
		String key = "key";
		String data = "data";
		dq.write(key, data);
		String failMsg = "";

	        // Verify write worked by reading.
		KeyedDataQueueEntry entry = dq.read("key");
		if (entry == null)
		{
		    failMsg += "\nEntry equals null";
		}
		else
		{
		    String byteKey = new String(entry.getKey(), "UnicodeBig");
		    String stringKey = entry.getKeyString();
		    String byteData = new String(entry.getData(), "UnicodeBig");
		    String stringData = entry.getString();
		    String sender = entry.getSenderInformation();
		    if (!byteKey.equals("key\u0000\u0000")) failMsg += "\nByte Key Received: '" + byteKey + "'\nExpected: key\u0000\u0000";
		    if (!stringKey.equals("key")) failMsg += "\nString Key Received: '" + stringKey + "'\nExpected: 'key'";
		    if (!byteData.equals("data")) failMsg += "\nByte Data Received: '" + byteData + "'\nExpected: 'data'";
		    if (!stringData.equals("data")) failMsg += "\nString Data Received: '" + stringData + "'\nExpected: 'data'";
		    if (!sender.equals("")) failMsg += "\nSender Information Received: '" + sender + "'\nExpected empty string (\"\")";
		}
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing a key that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var009()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] key = new byte[11];
	    byte[] data = (new String("data")).getBytes("UnicodeBigUnmarked");
	    dq2.create(10, 80);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF9506", ErrorCompletingRequestException.AS400_ERROR);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing a key that is too long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var010()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.setCcsid(0xf200);
		String key = "key    ";  // a too long key string
		String data ="data";
		try
		{
		    dq.write(key, data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key (key    ): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing a key that is 256 bytes long.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var011()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(256, 80);
	    try
	    {
		byte[] key = (new String("abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabc")).getBytes("UnicodeBigUnmarked");
		byte[] data = (new String("data")).getBytes("UnicodeBigUnmarked");
		dq.write(key, data);
		dq.setCcsid(0xf200);
		String failMsg = verifyKeyedWrite(dq, "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabc", "data");
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing a key that is 256 bytes long.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var012()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(256, 80);
	    try
	    {
		dq.setCcsid(0xf200);
		String key = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabc";
		String data = "data";
		dq.write(key, data);
		String failMsg = verifyKeyedWrite(dq, "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabc", "data");
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing a key that is over 256 bytes long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var013()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(256, 80);
	    try
	    {
		byte[] key = new byte[257];
		byte[] data = (new String("data")).getBytes("UnicodeBigUnmarked");
		try
		{
		    dq.write(key, data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key.length (257): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing a key that is over 256 bytes long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var014()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(256, 80);
	    try
	    {
		dq.setCcsid(0xf200);
		String key = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcd";
		String data ="data";
		try
		{
		    dq.write(key, data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key (abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcd): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) passing a null for the data.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var015()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] data = null;
	    dq2.create(80);
	    try
	    {
		dq.write(data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "data");
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing a null for the data.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var016()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    byte[] data = null;
	    dq2.create(10, 80);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "data");
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) passing a null for the data.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var017()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    String data = null;
	    dq.create(80);
	    try
	    {
		dq.write(data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "data");
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing a null for the data.
      <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var018()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    String key = "key  ";
	    String data = null;
	    dq.create(10, 80);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionIs(e, "NullPointerException", "data");
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) passing data that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var019()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    dq2.create(5);
	    try
	    {
		dq.write(data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2498", ErrorCompletingRequestException.AS400_ERROR);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing data that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var020()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    dq2.create(10, 5);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2498", ErrorCompletingRequestException.AS400_ERROR);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) passing data that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var021()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq.setCcsid(0xf200);
	    String data = "data";
	    dq2.create(5);
	    try
	    {
		dq.write(data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2498", ErrorCompletingRequestException.AS400_ERROR);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing data that is too long.
      <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var022()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq.setCcsid(0xf200);
	    String key = "key  ";
	    String data = "data";
	    dq2.create(10, 5);
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "AS400Exception", "CPF2498", ErrorCompletingRequestException.AS400_ERROR);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) with data of length 31744.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var023()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(31744);
	    try
	    {
		byte[] data = new byte[31744];
		for (int i = 0; i < 31744; ++i)
		{
		    data[i] = 0x77;
		}
		dq.write(data);
		dq.setCcsid(13488);
		String failMsg = verifyBigWrite(dq, 31744);
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) with data of length 31744.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var024()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 31744);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		byte[] data = new byte[31744];
		for (int i = 0; i < 31744; ++i)
		{
		    data[i] = 0x77;
		}
		dq.write(key, data);
		dq.setCcsid(13488);
		String failMsg = verifyBigKeyedWrite(dq, "key  ", 31744);
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) with data of length 31744.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var025()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(31744);
	    try
	    {
		dq.setCcsid(13488);
		char[] charData = new char[15872];
		for (int i = 0; i < 15872; ++i)
		{
		    charData[i] = 0x7777;
		}
		String data = new String(charData);
		dq.write(data);
		String failMsg = verifyBigWrite(dq, 31744);
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) with data of length 31744.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var026()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 31744);
	    try
	    {
		dq.setCcsid(13488);
		String key = "key  ";
		char[] charData = new char[15872];
		for (int i = 0; i < 15872; ++i)
		{
		    charData[i] = 0x7777;
		}
		String data = new String(charData);
		dq.write(key, data);
		String failMsg = verifyBigKeyedWrite(dq, "key  ", 31744);
		assertCondition(failMsg.equals(""), failMsg);
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) with data of length 31746.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var027()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(31746);
	    try
	    {
		byte[] data = new byte[31746];
		for (int i = 0; i < 31746; ++i)
		{
		    data[i] = 0x77;
		}
		dq.write(data);
		dq.setCcsid(13488);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigWrite(dq, 31746);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			DataQueueEntry entry = dq.read();
			failed("No exception for "+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) with data of length 31746.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var028()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 31746);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		byte[] data = new byte[31746];
		for (int i = 0; i < 31746; ++i)
		{
		    data[i] = 0x77;
		}
		dq.write(key, data);
		dq.setCcsid(13488);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigKeyedWrite(dq, "key  ", 31746);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			KeyedDataQueueEntry entry = dq.read("key  ");
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) with data of length 31746.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var029()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(31746);
	    try
	    {
		dq.setCcsid(13488);
		char[] charData = new char[15873];
		for (int i = 0; i < 15873; ++i)
		{
		    charData[i] = 0x7777;
		}
		String data = new String(charData);
		dq.write(data);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigWrite(dq, 31746);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			DataQueueEntry entry = dq.read();
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) with data of length 31746.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var030()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 31746);
	    try
	    {
		dq.setCcsid(13488);
		String key = "key  ";
		char[] charData = new char[15873];
		for (int i = 0; i < 15873; ++i)
		{
		    charData[i] = 0x7777;
		}
		String data = new String(charData);
		dq.write(key, data);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigKeyedWrite(dq, "key  ", 31744);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			KeyedDataQueueEntry entry = dq.read("key  ");
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) with data of length 64512.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var031()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(64512);
	    try
	    {
		byte[] data = new byte[64512];
		for (int i = 0; i < 64512; ++i)
		{
		    data[i] = 0x77;
		}
		dq.write(data);
		dq.setCcsid(13488);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigWrite(dq, 64512);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			DataQueueEntry entry = dq.read();
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) with data of length 64512.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var032()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 64512);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		byte[] data = new byte[64512];
		for (int i = 0; i < 64512; ++i)
		{
		    data[i] = 0x77;
		}
		dq.write(key, data);
		dq.setCcsid(13488);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigKeyedWrite(dq, "key  ", 64512);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			KeyedDataQueueEntry entry = dq.read("key  ");
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) with data of length 64512.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var033()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(64512);
	    try
	    {
		dq.setCcsid(13488);
		char[] charData = new char[32256];
		for (int i = 0; i < 32256; ++i)
		{
		    charData[i] = 0x7777;
		}
		String data = new String(charData);
		dq.write(data);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigWrite(dq, 64512);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			DataQueueEntry entry = dq.read();
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) with data of length 64512.
      <p>Result:  Verify the data queue entry was successfully written.
     **/
    public void Var034()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 64512);
	    try
	    {
		dq.setCcsid(13488);
		String key = "key  ";
		char[] charData = new char[32256];
		for (int i = 0; i < 32256; ++i)
		{
		    charData[i] = 0x7777;
		}
		String data = new String(charData);
		dq.write(key, data);
		if (DQTest.allowBigDQ)
		{
		    String failMsg = verifyBigKeyedWrite(dq, "key  ", 64512);
		    assertCondition(failMsg.equals(""), failMsg);
		}
		else
		{
		    try
		    {
			KeyedDataQueueEntry entry = dq.read("key  ");
			failed("No exception."+entry);
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ErrorCompletingRequestException", ErrorCompletingRequestException.LENGTH_NOT_VALID);
		    }
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) passing data that is over 64512 bytes long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var035()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(64512);
	    try
	    {
		byte[] data = new byte[64513];
		try
		{
		    dq.write(data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "data.length (64513): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) passing data that is over 64512 bytes long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var036()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 64512);
	    try
	    {
		byte[] key = (new String("key  ")).getBytes("UnicodeBigUnmarked");
		byte[] data = new byte[64513];
		try
		{
		    dq.write(key, data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "data.length (64513): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) passing data that is over 64512 bytes long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var037()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(64512);
	    try
	    {
		dq.setCcsid(13488);
		String data = new String(new char[32257]);
		try
		{
		    dq.write(data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "data.length (64514): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) passing data that is over 64512 bytes long.
      <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var038()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 64512);
	    try
	    {
		dq.setCcsid(13488);
		String key = "key  ";
		String data = new String(new char[32257]);
		try
		{
		    dq.write(key, data);
		    failed("Exception not thrown.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "data.length (64514): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
		}
	    }
	    finally
	    {
		dq2.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var039()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(80);
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    try
	    {
		dq.write(data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var040()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call DataQueue::write(String) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var041()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(80);
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    String data = "data";
	    try
	    {
		dq.write(data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::write(String, String) on a data queue that does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var042()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    String key = "key  ";
	    String data = "data";
	    try
	    {
		dq.write(key, data);
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/WRITETEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to DataQueue::write(byte[]).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var043()
    {
	if (isNative_)
	{
	    succeeded();
	}
	else
	{
	    try { 
		for (int i = 0; i < DROPPER_RETRIES; i++) { 
		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD1TETEST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
		    dq.create(80);
		    try
		    {
			drop.start();
			dq.write(data);
			// Loop again and retry
			// notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch (Exception e)
		    {
			drop.join();
			dq.delete();

			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		    drop.join();
		    dq.delete();
		}

		System.out.println("Warning.. Threads did not force condition");
		assertCondition(true); 
	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::write(byte[], byte[]).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var044()
    {
	if (isNative_)
	{
	    succeeded();
	}
	else
	{
	    try
	    {
		for (int i = 0; i < DROPPER_RETRIES; i++) { 
		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD2TETEST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
		    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
		    dq.create(10, 80);
		    try
		    {
			drop.start();
			dq.write(key, data);
		    // loop again and retry 
		    // notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch (Exception e)
		    {
			drop.join();
			dq.delete();

			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return ; 
		    }
		    drop.join();
                    try {
                      dq.delete();
                  } catch (Exception e2) { 
                    // Ignore
                  }
		}

		System.out.println("Warning.. Threads did not force condition");
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to DataQueue::write(String).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var045()
    {
	if (isNative_)
	{
	    succeeded();
	}
	else
	{
	    try
	    {
	      DataQueue dq = null;
	      ConnectionDropper drop = null; 
		for (int i = 0; i < DROPPER_RETRIES; i++) { 
		    try
		    {
			dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD3TETEST.DTAQ");
			drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
			String data = "data";
			dq.create(80);
			drop.start();
			dq.write(data);
                        dq.delete();
		    // loop again and retry 
		    // notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join(); 
		    }
		    catch (Exception e)
		    {
		        try {
		          if (dq != null ) { 
	                    dq.delete();
		          }
		        } catch (Exception e2) { 
		          // Ignore
		        }
		        if (drop != null) drop.join();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning.. Threads did not force condition");
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception 2.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::write(String, String).
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var046()
    {
	if (isNative_)
	{
	    succeeded();
	}
	else
	{
	    try
	      {
	        KeyedDataQueue dq = null; 
	        ConnectionDropper drop = null; 
		for (int i = 0; i < DROPPER_RETRIES; i++) { 
		    try
		    {
		    dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD2TETEST.DTAQ");
		    drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    String key = "key  ";
		    String data = "data";
		    dq.create(10, 80);
			drop.start();
			dq.write(key, data);
		    // loop again and retry 
		    // notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			drop = null; 
		    }
		    catch (Exception e)
		    {
			if (drop != null) drop.join();
                        try {
                          if (dq != null) dq.delete();
                      } catch (Exception e2) { 
                        // Ignore
                      }
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		    dq.delete();
		}
		System.out.println("Warning.. Threads did not force condition");
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Call DataQueue::write(byte[]) on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var047()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*ADD)");
		dq.getDescription();
		try
		{
		    dq.write(data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var048()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*ADD)");
		dq.getDescription();
		try
		{
		    dq.write(key, data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call DataQueue::write(String) on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var049()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    String data = "data";
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*ADD)");
		dq.getDescription();
		try
		{
		    dq.write(data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call KeyedDataQueue::write(String, String) on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var050()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    String key = "key  ";
	    String data = "data";
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*ADD)");
		dq.getDescription();
		try
		{
		    dq.write(key, data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call DataQueue::write(byte[]) on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var051()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
		try
		{
		    dq.write(data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call KeyedDataQueue::write(byte[], byte[]) on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var052()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
	    byte[] data = new String("data").getBytes("UnicodeBigUnmarked");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE" );
		try
		{
		    dq.write(key, data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call DataQueue::write(String) on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var053()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    String data = "data";
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
		try
		{
		    dq.write(data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
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
      <p>Test:  Call KeyedDataQueue::write(String, String) on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var054()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    String key = "key  ";
	    String data = "data";
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE" );
		try
		{
		    dq.write(key, data);
		    failed("No exception.");
		}
		catch (Exception e)
		{
		    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
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
}
