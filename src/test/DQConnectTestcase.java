///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQConnectTestcase.java
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
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IllegalObjectTypeException;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.SecureAS400;
import com.ibm.as400.access.ServerStartupException;

/**
 Testcase DQConnectTestcase.
 <p>Test various errors that occur while connecting the data queue server.  Connecting is an internal function, it does not have a public method, but is done under the covers by most methods.  A variety of public methods will be used to test the underlying connect function.
 **/
public class DQConnectTestcase extends Testcase
{

    public static int RETRIES = 60; 
    /**
     <p>Test:  Remove data queue from the service table, then connect to the data queue server by calling DataQueue::delete().
     <p>Result:  Verify a ServerStartupException is thrown.
     **/
    public void Var001()
    {
        if (isNative_)
        {
            succeeded();
        }
        else
        {
            try
            {
                // Create new, unconnected AS400 object
                AS400 system;
                String serviceName;
                String portNumber;
                if (systemObject_ instanceof SecureAS400)
                {
                    system = new SecureAS400(systemObject_);
                    serviceName = "as-dtaq-s";
                    portNumber = "9472";
                }
                else
                {
                    system = new AS400(systemObject_);
                    serviceName = "as-dtaq";
                    portNumber = "8472";
                }

                system.setServicePort(AS400.DATAQUEUE, AS400.USE_PORT_MAPPER);
                DataQueue dq = new DataQueue(system, "/QSYS.LIB/DQTEST.LIB/CONNECT.DTAQ");

                // Remove the data queue server entry from the TCPIP service table
                if (!cmdRun("RMVSRVTBLE SERVICE('" + serviceName + "') PORT(" + portNumber + ") PROTOCOL('tcp')"))
                {
                    ///failed("setup failed");
                    ///return;
                    System.err.println("RMVSRVTBLE failed");
                }
                // Now, try to delete the data queue - this will cause a
                // new as400 connection to be created.
                try
                {
                    dq.delete();
                    failed("No exception.");
                }
                catch (Exception e)
                {
                    assertExceptionIs(e, "ServerStartupException", ServerStartupException.CONNECTION_PORT_CANNOT_CONNECT_TO);
                }
                finally
                {
                    // Cleanup the test environment:
                    // Restore the data queue server entry in the TCP/IP service table
                    String cmdStr = "ADDSRVTBLE SERVICE('" + serviceName + "') PORT(" + portNumber + ") PROTOCOL('tcp') TEXT('Data Queue Server') ALIAS('asdtaq' 'AS-DTAQ' 'ASDTAQ')";
                    String failMsg = "Unable to restore service table entry.\nPlease execute the following command on " + systemObject_.getSystemName() + ":\n" + cmdStr;
                    try
                    {
                        if (!cmdRun(cmdStr))
                        {
                            failed(failMsg);
                        }
                    }
                    catch (Exception e)
                    {
                        failed(e, failMsg);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Remove data queue from the service table, then connect to the data queue server by calling KeyedDataQueue::delete().
     <p>Result:  Verify a ServerStartupException is thrown.
     **/
    public void Var002()
    {
        if (isNative_)
        {
            succeeded();
        }
        else
        {
            try
            {
                // Create new, unconnected AS400 object
                AS400 system;
                String serviceName;
                String portNumber;
                if (systemObject_ instanceof SecureAS400)
                {
                    system = new SecureAS400(systemObject_);
                    serviceName = "as-dtaq-s";
                    portNumber = "9472";
                }
                else
                {
                    system = new AS400(systemObject_);
                    serviceName = "as-dtaq";
                    portNumber = "8472";
                }

                system.setServicePort(AS400.DATAQUEUE, AS400.USE_PORT_MAPPER);
                KeyedDataQueue dq = new KeyedDataQueue(system, "/QSYS.LIB/DQTEST.LIB/CONNECT.DTAQ");

                // Remove the data queue server entry from the TCPIP service table
                if (!cmdRun("RMVSRVTBLE SERVICE('" + serviceName + "') PORT(" + portNumber + ") PROTOCOL('tcp')"))
                {
                    ///failed("setup failed");
                    ///return;
                    System.err.println("RMVSRVTBLE failed");
                }
                try
                {
                    dq.delete();
                    failed("No exception.");
                }
                catch (Exception e)
                {
                    assertExceptionIs(e, "ServerStartupException", ServerStartupException.CONNECTION_PORT_CANNOT_CONNECT_TO);
                }
                finally
                {
                    // Cleanup the test environment:
                    // Restore the data queue server entry in the TCP/IP service table
                    String cmdStr = "ADDSRVTBLE SERVICE('" + serviceName + "') PORT(" + portNumber + ") PROTOCOL('tcp') TEXT('Data Queue Server') ALIAS('asdtaq' 'AS-DTAQ' 'ASDTAQ')";
                    String failMsg = "Unable to restore service table entry.\nPlease execute the following command on " + systemObject_.getSystemName() + ":\n" + cmdStr;
                    try
                    {
                        if (!cmdRun(cmdStr))
                        {
                            failed(failMsg);
                        }
                    }
                    catch (Exception e)
                    {
                        failed(e, failMsg);
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Connect to the data queue server by calling DataQueue::clear() using a data queue that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var003()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/HS345Q9D.DTAQ");
            try
            {
                dq.clear();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/HS345Q9D.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Connect to the data queue server by calling KeyedDataQueue::clear() using a data queue that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var004()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/HS345Q9D.DTAQ");
            try
            {
                dq.clear();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/HS345Q9D.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Connect to the data queue server by calling DataQueue::refreshAttributes() using a library that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var005()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/J9S7D9DS.LIB/HS345Q9D.DTAQ");
            try
            {
                dq.refreshAttributes();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/J9S7D9DS.LIB/HS345Q9D.DTAQ: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Connect to the data queue server by calling KeyedDataQueue::refreshAttributes() using a library that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var006()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/J9S7D9DS.LIB/HS345Q9D.DTAQ");
            try
            {
                dq.refreshAttributes();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/J9S7D9DS.LIB/HS345Q9D.DTAQ: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::read() on a keyed data queue.
     <p>Result:  Verify an IllegalObjectTypeException is thrown.
     **/
    public void Var007()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            dq2.create(10, 80);
            try
            {
                dq.read();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_QUEUE_KEYED);
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
     <p>Test:  Call KeyedDataQueue::read() on a non-keyed data queue.
     <p>Result:  Verify an IllegalObjectTypeException is thrown.
     **/
    public void Var008()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            dq2.create(80);
            try
            {
                dq.read(new String("sos").getBytes("UnicodeBigUnmarked"));
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "IllegalObjectTypeException", IllegalObjectTypeException.DATA_QUEUE_NOT_KEYED);
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
     <p>Test:  Attempt to connect to the data queue server by calling DataQueue::peek() using a system name that does not exist.
     <p>Result:  Verify an UnknownHostException is thrown.
     **/
    public void Var009()
    {
        try
        {
            AS400 system = new AS400("BADSYS", "uid", "pw");
            system.setGuiAvailable(false);
            DataQueue dq = new DataQueue(system, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            try
            {
                dq.peek();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (isApplet_)
                {
                    succeeded();
                }
                else
                {
                    assertExceptionIs(e, "UnknownHostException");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Attempt to connect to the data queue server by calling KeyedDataQueue::peek() using a system name that does not exist.
     <p>Result:  Verify an UnknownHostException is thrown.
     **/
    public void Var010()
    {
        try
        {
            AS400 system = new AS400("BADSYS", "uid", "pw");
            system.setGuiAvailable(false);
            KeyedDataQueue dq = new KeyedDataQueue(system, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            try
            {
                dq.peek(new byte[0]);
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (isApplet_)
                {
                    succeeded();
                }
                else
                {
                    assertExceptionIs(e, "UnknownHostException");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Connect to the data queue server by calling DataQueue::write() using a user ID that does not exist.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var011()
    {
        try
        {
            AS400 sys = new AS400(systemObject_.getSystemName(), "Press", "cancel");
            sys.setGuiAvailable(false);
            DataQueue dq = new DataQueue(sys, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            try
            {
                dq.write(new String("test").getBytes("UnicodeBigUnmarked"));
                failed("No exception.");
            }
            catch (Exception e)
            {
		if (getRelease() <= JDTestDriver.RELEASE_V7R4M0) { 
                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
		} else {
		    // New security is not to leak infomation but return invalid password
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);

		} 
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Connect to the data queue server by calling KeyedDataQueue::write() using a user ID that does not exist.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var012()
    {
        try
        {
            AS400 sys = new AS400(systemObject_.getSystemName(), "Press", "cancel");
            sys.setGuiAvailable(false);
            KeyedDataQueue dq = new KeyedDataQueue(sys, "/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            try
            {
                dq.write(new String("great").getBytes("UnicodeBigUnmarked"), new String("test").getBytes("UnicodeBigUnmarked"));
                failed("No exception.");
            }
            catch (Exception e)
            {
		if (getRelease() <= JDTestDriver.RELEASE_V7R4M0) { 

                assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.USERID_UNKNOWN);
		} else {
		    // New security is not to leak infomation but return invalid password
		    assertExceptionIs(e, "AS400SecurityException", AS400SecurityException.PASSWORD_INCORRECT);

		} 

            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Drop an attempted connection to the data queue server during a call to DataQueue::clear().
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var013()
    {
        if (isNative_)
        {
            succeeded();
        }
        else
        {
            try
            {
		for (int i = 0; i < RETRIES; i++) { 
		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CONNECT.DTAQ");
		    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CONNECT.DTAQ");
		    dq2.create(80);
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 100);
		    try
		    {
			drop.start();
			dq.clear();
		        // Try again  
		        //	notApplicable("ConnectionDropper"); // Threads did not force condition
		    }
		    catch (Exception e)
		    {
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		    finally
		    {
			drop.join();
			dq2.delete();
		    }
		}
		System.out.println("Warning:  connection dropper testcase did not work retries="+RETRIES);
		assertCondition(true); 

            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop an attempted connection to the data queue server during a call to KeyedDataQueue::clear().
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var014()
    {
        if (isNative_)
        {
            succeeded();
        }
        else
        {
            try
            {
		for (int i = 0; i < RETRIES; i++) { 
		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CONNECT.DTAQ");
		    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CONNECT.DTAQ");
		    dq2.create(10,80);
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 100);
		    try
		    {
			drop.start();
			dq.clear();
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
			dq2.delete();
		    }

		}
		System.out.println("Warning:  connection dropper testcase did not work RETRIES="+RETRIES);
		assertCondition(true); 

            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Call DataQueue::create() using a path name the uses *CURLIB.  Note that this variation assumes that QGPL is the current library, if it is not, this variation will fail.
     <p>Result:  Verify the data queue is successfully created.
     **/
    public void Var015()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CONNECT.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CONNECT.DTAQ");
            dq2.create(80);
            try
            {
                dq.refreshAttributes();
                int len = dq.getMaxEntryLength();
                assertCondition(len == 80, "Wrong value: " + len);
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
     <p>Test:  Call KeyedDataQueue::create() using a path name the uses *CURLIB.  Note that this variation assumes that QGPL is the current library, if it is not, this variation will fail.
     <p>Result:  Verify the data queue is successfully created.
     **/
    public void Var016()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CONNECT.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CONNECT.DTAQ");
            dq2.create(10, 80);
            try
            {
                dq.refreshAttributes();
                int len = dq.getKeyLength();
                assertCondition(len == 10, "Wrong value: " + len);
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
     <p>Test:  Call DataQueue::create() using a path name the uses *LIBL.  Note that this variation assumes that QGPL is in the library list, if it is not, this variation will fail.
     <p>Result:  Verify the data queue is successfully created.
     **/
    public void Var017()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/%LIBL%.LIB/CONNECT.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CONNECT.DTAQ");
            dq2.create(80);
            try
            {
                dq.refreshAttributes();
                int len = dq.getMaxEntryLength();
                assertCondition(len == 80, "Wrong value: " + len);
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
     <p>Test:  Call KeyedDataQueue::create() using a path name the uses *LIBL.  Note that this variation assumes that QGPL is in the library list, if it is not, this variation will fail.
     <p>Result:  Verify the data queue is successfully created.
     **/
    public void Var018()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/%LIBL%.LIB/CONNECT.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CONNECT.DTAQ");
            dq2.create(10, 80);
            try
            {
                dq.refreshAttributes();
                int len = dq.getKeyLength();
                assertCondition(len == 10, "Wrong value: " + len);
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
     <p>Test:  Attempt to connect to the data queue server with call to DataQueue::isFIFO() when the path is not set.
     <p>Result:  Verify a ExtendedIllegalStateException is thrown.
     **/
    public void Var019()
    {
        try
        {
            DataQueue dq = new DataQueue();
            dq.setSystem(systemObject_);
            try
            {
                dq.isFIFO();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Attempt to connect to the data queue server with call to KeyedDataQueue::isFIFO() when the path is not set.
     <p>Result:  Verify a ExtendedIllegalStateException is thrown.
     **/
    public void Var020()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            dq.setSystem(systemObject_);
            try
            {
                dq.isFIFO();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Attempt to connect to the data queue server with call to DataQueue::getMaxEntryLength() when the system is not set.
     <p>Result:  Verify a ExtendedIllegalStateException is thrown.
     **/
    public void Var021()
    {
        try
        {
            DataQueue dq = new DataQueue();
            dq.setPath("/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            try
            {
                dq.getMaxEntryLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Attempt to connect to the data queue server with call to KeyedDataQueue::getMaxEntryLength() when the system is not set.
     <p>Result:  Verify a ExtendedIllegalStateException is thrown.
     **/
    public void Var022()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            dq.setPath("/QSYS.LIB/QTEMP.LIB/CONNECT.DTAQ");
            try
            {
                dq.getMaxEntryLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
