///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQDeleteTestcase.java
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
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ConnectionDropper;
import test.DQTest;
import test.Testcase;

/**
 Testcase DQDeleteTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>DataQueue::delete()
 <li>KeyedDataQueue::delete()
 </ul>
 **/
public class DQDeleteTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQDeleteTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
    /**
     <p>Test:  Call DataQueue::delete().
     <p>Result:  Verify the data queue is deleted.
     **/
    public void Var001()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ");
            dq.create(80);
            dq.delete();
            // Verify the queue was deleted on the 400.
            try
            {
                dq.getDescription();
                failed("Queue exists on AS400.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::delete().
     <p>Result:  Verify the data queue is deleted.
     **/
    public void Var002()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ");
            dq.create(10, 80);
            dq.delete();
            // Verify the queue was deleted on the 400.
            try
            {
                dq.getDescription();
                failed("Queue exists on AS400.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::delete() on a queue using *LIBL.  Note this variation assumes that QGPL is in the library list, it will fail if it is not.
     <p>Result:  Verify the data queue is successfully deleted.
     **/
    public void Var003()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/DLTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/%LIBL%.LIB/DLTTEST.DTAQ");
            dq.create(80);
            dq2.delete();
            // Verify the queue was deleted on the 400.
            try
            {
                dq.refreshAttributes();
                failed("Queue exists on AS400.");
                cmdRun("QSYS/DLTDTAQ QGPL/DLTTEST");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QGPL.LIB/DLTTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::delete() on a queue using *LIBL.  Note this variation assumes that QGPL is in the library list, it will fail if it is not.
     <p>Result:  Verify the data queue is successfully deleted.
     **/
    public void Var004()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/DLTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/%LIBL%.LIB/DLTTEST.DTAQ");
            dq.create(10, 80);
            dq2.delete();
            // Verify the queue was deleted on the 400.
            try
            {
                dq.refreshAttributes();
                failed("Queue exists on AS400.");
                cmdRun("QSYS/DLTDTAQ QGPL/DLTTEST");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QGPL.LIB/DLTTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::delete() on a queue which does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var005()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ");
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
                dq.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::delete() on a queue which does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var006()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ");
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
                dq.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/QTEMP.LIB/DLTTEST.DTAQ: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to DataQueue::delete().
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
            ConnectionDropper drop = null; 
            try
            {
                try
                {
		    int raceTime = 10;
		    while (raceTime > 0) { 
			DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQLIB+".LIB/CD1DLTTEST.DTAQ");
			drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, raceTime);
			dq.create(80);
			drop.start();
			dq.delete();
			drop.join();
			drop = null; 
			raceTime--; 
		    }
		    assertCondition(true,"Warning:   Threads did not force condition -- this is a race.. "); 
                }
                catch (Exception e)
                {
                    assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
                }
                finally
                {
                    if (drop != null) drop.join();
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::delete().
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
          ConnectionDropper drop = null; 
            try
            {
                try
                {
		    int raceTime = 20;
		    while (raceTime > 0) { 
			KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQLIB+".LIB/CD2DLTTEST.DTAQ");
			drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, raceTime);
			dq.create(10, 80);
			drop.start();
			dq.delete();
			drop.join(); 
			drop = null; 
			raceTime--; 
		    }
		    assertCondition(true,"Warning.. ConnectionDropper:   Threads did not force condition"); 
                }
                catch (Exception e)
                {
                    assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
                }
                finally
                {
                    if (drop != null) drop.join();
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Call DataQueue::delete() on a queue to which the user does not have enough authority to the queue.  Note: *OBJEXIST authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var009()
    {
        try
        {
            String user = systemObject_.getUserId();
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
            cmdRun("QSYS/CRTDTAQ "+DQTest.DQSECLIB+"/SECTST MAXLEN(80)");
            try
            {
                cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
                cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
                cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*OBJEXIST)");
                dq.getDescription();
                try
                {
                    dq.delete();
                    failed("No exception.");
                }
                catch (Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
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
     <p>Test:  Call KeyedDataQueue::delete() on a queue to which the user does not have enough authority to the queue.  Note: *OBJEXIST authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var010()
    {
        try
        {
            String user = systemObject_.getUserId();
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
            cmdRun("QSYS/CRTDTAQ "+DQTest.DQSECLIB+"/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(5)");
            cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*USE )");

            try
            {
                cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
                cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*READ *OBJOPR)");
                cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*OBJEXIST)");
                dq.getDescription();
                
                try
                {
                    dq.delete();
                    failed("No exception.");
                }
                catch (Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
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
     <p>Test:  Call DataQueue::delete() on a queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var011()
    {
        try
        {
            String user = systemObject_.getUserId();
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
            cmdRun("QSYS/CRTDTAQ   "+DQTest.DQSECLIB+"/SECTST MAXLEN(80)");
            cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*USE )");

            try
            {
                cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
                
                dq.getDescription();
                cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " *EXECUTE");
                try
                {
                    dq.delete();
                    failed("No exception.");
                }
                catch (Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
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
     <p>Test:  Call KeyedDataQueue::delete() on a queue to which the user does not have enough authority to the library.  Note: *EXECUTE authority is required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var012()
    {
        try
        {
            String user = systemObject_.getUserId();
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ");
            cmdRun("QSYS/CRTDTAQ   "+DQTest.DQSECLIB+"/SECTST MAXLEN(80) SEQ(*KEYED) KEYLEN(5)");
            cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+"/SECTST *DTAQ " + user + " AUT(*USE )");
            try
            {
                cmdRun("QSYS/GRTOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " AUT(*EXECUTE *READ)");
                dq.getDescription();
                cmdRun("RVKOBJAUT "+DQTest.DQSECLIB+" *LIB " + user + " *EXECUTE" );
                try
                {
                    dq.delete();
                    failed("No exception.");
                }
                catch (Exception e)
                {
                    assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+DQTest.DQSECLIB+".LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
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
}
