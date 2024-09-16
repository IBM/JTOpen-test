///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DQ;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.BaseDataQueue;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.KeyedDataQueue;

import test.Testcase;

/**
 Testcase DQCtorTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>DataQueue::DataQueue()
 <li>KeyedDataQueue::KeyedDataQueue()
 <li>DataQueue::DataQueue(AS400, String)
 <li>KeyedDataQueue::KeyedDataQueue(AS400, String)
 </ul>
 **/
public class DQCtorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQCtorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
    String verifyCtor(BaseDataQueue dq, int eccsid, AS400 esystem, String ename, String epath)
    {
        String failMsg = "";
        int ccsid = dq.getCcsid();
        AS400 system = dq.getSystem();
        String name = dq.getName();
        String path = dq.getPath();
        if (ccsid != eccsid) failMsg += "CCSID: " + ccsid + " Expected: " + eccsid + "\n";
        if (system != esystem) failMsg += "System: " + system + " Expected: " + esystem + "\n";
        if (name == null || !name.equals(ename)) failMsg += "Name: " + name + " Expected: " + ename + "\n";
        if (path == null || !path.equals(epath)) failMsg += "Path: " + path + " Expected: " + epath + "\n";
        return failMsg;
    }

    /**
     <p>Test:  Call DataQueue::DataQueue().
     <p>Result:  Verify object is constructed without exception.
     **/
    public void Var001()
    {
        try
        {
            DataQueue dq = new DataQueue();
            String failMsg = verifyCtor(dq, 0, null, "", "");
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::KeyedDataQueue().
     <p>Result:  Verify object is constructed without exception.
     **/
    public void Var002()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            String failMsg = verifyCtor(dq, 0, null, "", "");
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::DataQueue(AS400, String) passing valid parmeters.
     <p>Result:  Verify object is constructed without exception.
     **/
    public void Var003()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTAQ");
            String failMsg = verifyCtor(dq, 0, systemObject_, "TESTQUEUE", "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTAQ");
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::KeyedDataQueue(AS400, String) passing valid parmeters.
     <p>Result:  Verify object is constructed without exception.
     **/
    public void Var004()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTAQ");
            String failMsg = verifyCtor(dq, 0, systemObject_, "TESTQUEUE", "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTAQ");
            assertCondition(failMsg.equals(""), "\n" + failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::DataQueue(AS400, String) passing a null for the system.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var005()
    {
        try
        {
            DataQueue dq = new DataQueue(null, "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTAQ");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::KeyedDataQueue(AS400, String) passing a null for the system.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var006()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(null, "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTAQ");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     <p>Test:  Call DataQueue::DataQueue(AS400, String) passing a null for the path.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var007()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::KeyedDataQueue(AS400, String) passing a null for the path.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var008()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     <p>Test:  Call DataQueue::DataQueue(AS400, String) passing an invalid path name.
     <p>Result:  Verify an IllegalPathNameException is thrown.
     **/
    public void Var009()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QSYS.LIB/TESTQUEUE.DTAQ");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QSYS.LIB/TESTQUEUE.DTAQ: ", IllegalPathNameException.QSYS_SYNTAX_NOT_VALID);
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::KeyedDataQueue(AS400, String) passing an invalid path name.
     <p>Result:  Verify an IllegalPathNameException is thrown.
     **/
    public void Var010()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QSYS.LIB/TESTQUEUE.DTAQ");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/QSYS.LIB/TESTQUEUE.DTAQ: ", IllegalPathNameException.QSYS_SYNTAX_NOT_VALID);
        }
    }

    /**
     <p>Test:  Call DataQueue::DataQueue(AS400, String) passing an invalid object type.
     <p>Result:  Verify an IllegalPathNameException is thrown.
     **/
    public void Var011()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTA: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::KeyedDataQueue(AS400, String) passing an invalid object type.
     <p>Result:  Verify an IllegalPathNameException is thrown.
     **/
    public void Var012()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTA");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", "/QSYS.LIB/DQTEST.LIB/TESTQUEUE.DTA: ", IllegalPathNameException.OBJECT_TYPE_NOT_VALID);
        }
    }
}
