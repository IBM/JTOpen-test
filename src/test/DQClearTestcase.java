///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQClearTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.ObjectDoesNotExistException;

/**
 Testcase DQClearTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>DataQueue::clear()
 <li>KeyedDataQueue::clear()
 <li>KeyedDataQueue::clear(byte[])
 <li>KeyedDataQueue::clear(String)
 </ul>
 **/
public class DQClearTestcase extends Testcase
{
	
    public static int DROPPER_RETRIES = 20; 
    /**
     <p>Test:  Call DataQueue::clear().
     <p>Result:  Verify the data queue was successfully cleared.
     **/
    public void Var001()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            dq.create(80);
            try
            {
                dq.write(new String("Some data.").getBytes("UnicodeBigUnmarked"));
                dq.clear();

                // Verify the clear worked.
                assertCondition(dq.read() == null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear().
     <p>Result:  Verify the data queue was successfully cleared.
     **/
    public void Var002()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            dq.create(10, 80);
            try
            {
                byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
                dq.write(key, new String("Some data.").getBytes("UnicodeBigUnmarked"));

                dq.clear();
                // Verify the clear worked.
                assertCondition(dq.read(key) == null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear(byte[]).
     <p>Result:  Verify the data queue was successfully cleared.
     **/
    public void Var003()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            dq.create(10, 80);
            try
            {
                byte[] key1 = new String("key  ").getBytes("UnicodeBigUnmarked");
                byte[] key2 = new String("key2 ").getBytes("UnicodeBigUnmarked");
                dq.write(key1, new String("Some data.").getBytes("UnicodeBigUnmarked"));
                dq.write(key2, new String("Some more data.").getBytes("UnicodeBigUnmarked"));
                dq.clear(key1);
                // Verify the clear worked.
                assertCondition(dq.read(key1) == null && dq.read(key2) != null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear(String).
     <p>Result:  Verify the data queue was successfully cleared.
     **/
    public void Var004()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            dq.create(10, 80);
            try
            {
                String key1 = "key  ";
                String key2 = "key2 ";
                dq.setCcsid(0xF200);

                dq.write(key1, "Some data.");
                dq.write(key2, "Some more data.");

                dq.clear(key1);
                // Verify the clear worked.
                assertCondition(dq.read(key1) == null && dq.read(key2) != null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear(byte[]) passing a null for the key.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var005()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            byte[] key = null;
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "key");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear(String) passing a null for the key.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var006()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            String key = null;
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "key");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear(byte[]) passing a key that is not long enough.
     <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var007()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            byte[] key = new byte[4];
            dq.create(10, 80);
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
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
     <p>Test:  Call KeyedDataQueue::clear(String) passing a key that is not long enough.
     <p>Result:  Verify the key was zero padded and the data queue was successfully cleared.
     **/
    public void Var008()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            String keyShort = "key";
            byte[] key1 = { 0x00, 0x6B, 0x00, 0x65, 0x00, 0x79, 0x00, 0x00, 0x00, 0x00 };
            String key2 = "key  ";
            dq.setCcsid(0xf200);
            dq.create(10, 80);
            try
            {
                dq.write(key1, new String("Some data.").getBytes("UnicodeBigUnmarked"));
                dq.write(key2, "Some more data.");

                dq.clear(keyShort);
                // Verify the clear worked.
                assertCondition(dq.read(key1) == null && dq.read(key2) != null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear(byte[]) passing a that that is too long.
     <p>Result:  Verify an AS400Exception is thrown.
     **/
    public void Var009()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            byte[] key = new byte[11];
            dq.create(10, 80);
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
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
     <p>Test:  Call KeyedDataQueue::clear(String) passing a key that is too long.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var010()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            String key = "Too Long";
            dq.setCcsid(0xf200);
            dq.create(10, 80);
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key (Too Long): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
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
     <p>Test:  Call KeyedDataQueue::clear(byte[]) passing a key argument that is 256 bytes.
     <p>Result:  Verify the data queue was successfully cleared.
     **/
    public void Var011()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            dq.create(256, 80);
            try
            {
                byte[] key1 = new String("abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabc").getBytes("UnicodeBigUnmarked");
                byte[] key2 = new String("abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyacb").getBytes("UnicodeBigUnmarked");
                dq.write(key1, new String("Some data.").getBytes("UnicodeBigUnmarked"));
                dq.write(key2, new String("Some more data.").getBytes("UnicodeBigUnmarked"));
                dq.clear(key1);
                // Verify the clear worked.
                assertCondition(dq.read(key1) == null && dq.read(key2) != null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear(String) passing a key argument that is 256 bytes.
     <p>Result:  Verify the data queue was successfully cleared.
     **/
    public void Var012()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            String key1 = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabc";
            String key2 = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyacb";
            dq.setCcsid(0xf200);
            dq.create(256, 80);
            try
            {
                dq.write(key1, "Some data.");
                dq.write(key2, "Some more data.");

                dq.clear(key1);
                // Verify the clear worked.
                assertCondition(dq.read(key1) == null && dq.read(key2) != null, "Clear did not work.");
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
     <p>Test:  Call KeyedDataQueue::clear(byte[]) passing a key argument that is over 256 bytes.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var013()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            byte[] key = new byte[257];
            dq.create(256, 80);
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key.length (257): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
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
     <p>Test:  Call KeyedDataQueue::clear(String) passing a key argument that is over 256 bytes.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var014()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            String key = "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcd";
            dq.setCcsid(0x34B0);
            dq.create(256, 80);
            try
            {
                dq.clear(key);
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key (abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcd): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
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
     <p>Test:  Call DataQueue::clear() on a data queue that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var015()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
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
                dq.clear();
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear() on a data queue that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var016()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
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
                dq.clear();
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear(byte[]) on a data queue that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var017()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ");
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
                dq.clear(new byte[10]);
                failed("No exception.");
            }
            catch(Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/CLEARTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to DataQueue::clear().
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var018()
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
		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD1ARTEST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(80);
		    try
		    {
			drop.start();
			dq.clear();
			// Try again 
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			dq.delete();

		    }
		    catch(Exception e)
		    {
			drop.join();
			dq.delete();

			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  Threads did not force condition");
		assertCondition(true); 
            }
            catch(Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::clear().
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var019()
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

		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD2ARTEST.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(5, 80);
		    try
		    {
			drop.start();
			dq.clear();
			drop.join();
			dq.delete();
			// notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch(Exception e)
		    {
			drop.join();
			dq.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  Threads did not force condition");
		assertCondition(true); 

            }
            catch(Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::clear(byte[]).
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var020()
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

		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CD3ARTEST.DTAQ");
		    byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		    dq.create(10, 80);
		    try
		    {
			drop.start();
			dq.clear(key);
		    // notApplicable("ConnectionDropper"); // Threads did not force condition
		    drop.join();
		    dq.delete();
		    }
		    catch(Exception e)
		    {
			drop.join();
			dq.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  Threads did not force condition");
		assertCondition(true); 

            }
            catch(Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Call DataQueue::clear() on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var021()
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
                dq.refreshAttributes();
                cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR");
                try
                {
                    dq.clear();
                    failed("No exception.");
                }
                catch(Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
                }
            }
            finally
            {
                cmdRun("DLTDTAQ DQSECTEST/SECTST");
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear() on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var022()
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
                dq.refreshAttributes();
                cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR");
                try
                {
                    dq.clear();
                    failed("No exception.");
                }
                catch(Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
                }
            }
            finally
            {
                cmdRun("DLTDTAQ DQSECTEST/SECTST");
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear(byte[]) on a queue to that the user does not have enough authority to the queue.  Note: *OBJOPR and *READ authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var023()
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
                dq.refreshAttributes();
                cmdRun("RVKOBJAUT DQSECTEST/SECTST *DTAQ " + user + " *OBJOPR" );
                byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
                try
                {
                    dq.clear(key);
                    failed("No exception.");
                }
                catch(Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
                }
            }
            finally
            {
                cmdRun("DLTDTAQ DQSECTEST/SECTST");
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::clear() on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var024()
    {
        try
        {
            String user = systemObject_.getUserId();
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECLIBTST.DTAQ");
            cmdRun("CRTDTAQ DQSECTEST/SECLIBTST MAXLEN(80)");
            try
            {
                cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
                dq.refreshAttributes();
                cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
                try
                {
                    dq.clear();
                    failed("No exception.");
                }
                catch(Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECLIBTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
                }
            }
            finally
            {
                cmdRun("DLTDTAQ DQSECTEST/SECLIBTST");
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear() on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var025()
    {
        try
        {
            String user = systemObject_.getUserId();
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECLIBTST.DTAQ");
            cmdRun("CRTDTAQ DQSECTEST/SECLIBTST MAXLEN(80) SEQ(*KEYED) KEYLEN(5)");
            try
            {
                cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
                dq.refreshAttributes();
                cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
                try
                {
                    dq.clear();
                    failed("No exception.");
                }
                catch(Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECLIBTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
                }
            }
            finally
            {
                cmdRun("DLTDTAQ DQSECTEST/SECLIBTST");
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::clear(byte[]) on a queue to that the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var026()
    {
        try
        {
            String user = systemObject_.getUserId();
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECLIBTST.DTAQ");
            cmdRun("CRTDTAQ DQSECTEST/SECLIBTST MAXLEN(80) SEQ(*KEYED) KEYLEN(10)");
            try
            {
                cmdRun("GRTOBJAUT DQSECTEST *LIB " + user + " AUT(*EXECUTE *READ)");
                dq.refreshAttributes();
                cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *EXECUTE");
                byte[] key = new String("key  ").getBytes("UnicodeBigUnmarked");
                try
                {
                    dq.clear(key);
                    failed("No exception.");
                }
                catch(Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECLIBTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
                }
            }
            finally
            {
                cmdRun("DLTDTAQ DQSECTEST/SECLIBTST");
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
