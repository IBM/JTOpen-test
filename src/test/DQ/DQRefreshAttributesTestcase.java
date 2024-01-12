///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQRefreshAttributesTestcase.java
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
import com.ibm.as400.access.BaseDataQueue;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ConnectionDropper;
import test.Testcase;

/**
  Testcase DQRefreshAttributesTestcase.
  <p>Test variations for the methods:
  <ul>
  <li>DataQueue::refreshAttributes()
  <li>KeyedDataQueue::refreshAttributes()
  </ul>
 **/
public class DQRefreshAttributesTestcase extends Testcase
{
    private static final String EXPECTEDTEXT = "                                                  ";

    static int RETRIES = 20;

    String verifyRefresh(BaseDataQueue dq, int elength, boolean esave, boolean efifo, boolean eforce, String edesc) throws Exception
    {
	String failMsg = "";
	int length = dq.getMaxEntryLength();
	boolean save = dq.getSaveSenderInformation();
	boolean fifo = dq.isFIFO();
	boolean force = dq.getForceToAuxiliaryStorage();
	String desc = dq.getDescription();
	if (length != elength) failMsg += "MaxEntryLength: " + length + " Expected: " + elength + "\n";
	if (save != esave) failMsg += "SaveSenderInformation: " + save + " Expected: " + esave + "\n";
	if (fifo != efifo) failMsg += "FIFO: " + fifo + " Expected: " + efifo + "\n";
	if (force != eforce) failMsg += "ForceToAuxiliaryStorage: " + force + " Expected: " + eforce + "\n";
	if (desc == null || !desc.equals(edesc)) failMsg += "Description: " + desc + " Expected: " + edesc + "\n";
	return failMsg;
    }

    String verifyKeyedRefresh(KeyedDataQueue dq, int ekeylen, int elength, boolean esave, boolean efifo, boolean eforce, String edesc) throws Exception
    {
	String failMsg = "";
	int keylen = dq.getKeyLength();
	if (keylen != ekeylen) failMsg += "KeyLength: " + keylen + " Expected: " + ekeylen + "\n";
	return failMsg += verifyRefresh(dq, elength, esave, efifo, eforce, edesc);
    }

    /**
      <p>Test:  Call DataQueue::refreshAttributes().
      <p>Result:  Verify the attributes have been successfully refreshed.
     **/
    public void Var001()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    dq2.create(80);
	    try
	    {
		dq.refreshAttributes();
		String failMsg = verifyRefresh(dq, 80, false, true, false, EXPECTEDTEXT);
		assertCondition(failMsg.equals(""), "\n" + failMsg);
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
      <p>Test:  Call KeyedDataQueue::refreshAttributes() refreshing the attributes of a queue whose attributes have changed.
      <p>Result:  Verify the attributes have been successfully refreshed.
     **/
    public void Var002()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.refreshAttributes();
		String failMsg = verifyKeyedRefresh(dq, 10, 80, false, true, false, EXPECTEDTEXT);
		assertCondition(failMsg.equals(""), "\n" + failMsg);
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
      <p>Test:  Call DataQueue::refreshAttributes() refreshing the attributes of a queue whose attributes have changed.
      <p>Result:  Verify the attributes have been successfully refreshed.
     **/
    public void Var003()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    dq2.create(80, "*USE", true, false, false, "Text for #1");
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    dq2.create(65, "*EXCLUDE", false, true, true, "Different text");
	    try
	    {
		dq.refreshAttributes();
		String failMsg = verifyRefresh(dq, 65, false, true, true, "Different text                                    ");
		assertCondition(failMsg.equals(""), "\n" + failMsg);
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
      <p>Test:  Call KeyedDataQueue::refreshAttributes() refreshing the attributes of a queue whose attributes have changed.
      <p>Result:  Verify the attributes have been successfully refreshed.
     **/
    public void Var004()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/OPENTEST.DTAQ");
	    dq2.create(10, 80, "*USE", true, false, "Text for #1");
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    dq2.create(35, 65, "*EXCLUDE", false, true, "Different text");
	    try
	    {
		dq.refreshAttributes();
		String failMsg = verifyKeyedRefresh(dq, 35, 65, false, true, true, "Different text                                    ");
		assertCondition(failMsg.equals(""), "\n" + failMsg);
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
      <p>Test:  Call DataQueue::refreshAttributes() on a queue which does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var005()
    {
	try
	{
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/REFRESH.DTAQ");
	    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/REFRESH.DTAQ");
	    dq2.create(80);
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    try
	    {
		dq.refreshAttributes();
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/REFRESH.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Call KeyedDataQueue::refreshAttributes() on a queue which does not exist.
      <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var006()
    {
	try
	{
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/REFRESH.DTAQ");
	    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/REFRESH.DTAQ");
	    dq2.create(10, 80);
	    try
	    {
		dq.getDescription();
	    }
	    finally
	    {
		dq2.delete();
	    }
	    try
	    {
		dq.refreshAttributes();
		failed("No exception.");
	    }
	    catch (Exception e)
	    {
		assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/REFRESH.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to DataQueue::refreshAttributes().
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var007()
    {
	if (isNative_)
	{
	    succeeded();
	}
	else
	{
	    try
	    {
		for (int i = 0; i < RETRIES; i++ ) { 
		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD1REFRESH.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(80);
		    try
		    {
			drop.start();
			dq.refreshAttributes();
			// notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		    finally
		    {
			drop.join();
			dq.delete();
		    }
		}
		System.out.println("Connection dropper testcase did not work with RETRIES="+RETRIES);
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::refreshAttributes().
      <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var008()
    {
	if (isNative_)
	{
	    succeeded();
	}
	else
	{
	    try
	    {
		for (int i = 0 ; i < RETRIES; i++) { 
		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD2REFRESH.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(5, 80);
		    try
		    {
			drop.start();
			dq.refreshAttributes();
			// try again.. 
			// notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		    finally
		    {
			drop.join();
			dq.delete();
		    }
		}
		System.out.println("Connection dropper testcase did not work with RETRIES="+RETRIES);
		assertCondition(true); 

	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }

    /**
      <p>Test:  Call DataQueue::refreshAttributes() on a queue to which the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var009()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *READ");
		try
		{
		    dq.refreshAttributes();
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
      <p>Test:  Call KeyedDataQueue::refreshAttributes() on a queue to which the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var010()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(5)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
		cmdRun("GRTOBJAUT DQSECTEST/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
		cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *READ");
		try
		{
		    dq.refreshAttributes();
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
      <p>Test:  Call DataQueue::refreshAttributes() on a queue to which the user does not have enough authority to the library.  Note: *EXECUTE is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var011()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " *READ");
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
		try
		{
		    dq.refreshAttributes();
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
      <p>Test:  Call KeyedDataQueue::refreshAttributes() on a queue to which the user does not have enough authority to the library.  Note: *EXECUTE is required.
      <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var012()
    {
	try
	{
	    String user = systemObject_.getUserId();
	    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
	    cmdRun("CRTDTAQ DQSECTEST/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(5)");
	    try
	    {
		cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " *READ");
		cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
		try
		{
		    dq.refreshAttributes();
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
