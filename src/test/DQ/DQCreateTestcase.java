///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQCreateTestcase.java
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
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.KeyedDataQueue;
import com.ibm.as400.access.DataQueueAttributes;
import com.ibm.as400.access.ObjectAlreadyExistsException;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.ConnectionDropper;
import test.Testcase;

/**
 Testcase DQCreateTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>DataQueue::create(int)
 <li>KeyedDataQueue::create(int, int)
 <li>DataQueue::create(int, String, boolean, boolean, boolean, String)
 <li>KeyedDataQueue::create(int, int, String, boolean, boolean, String)
 <li>DataQueue::create(DataQueueAttributes)
 </ul>
 **/
public class DQCreateTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DQCreateTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DQTest.main(newArgs); 
   }
    private static final String EXPECTEDTEXT = "                                                  ";
    int DROPPER_RETRIES=10;

    String verifyCreate(BaseDataQueue dq, int elength, boolean esave, boolean efifo, boolean eforce, String edesc) throws Exception
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

    String verifyKeyedCreate(KeyedDataQueue dq, int ekeylen, int elength, boolean esave, boolean efifo, boolean eforce, String edesc) throws Exception
    {
        String failMsg = "";
        int keylen = dq.getKeyLength();
        if (keylen != ekeylen) failMsg += "KeyLength: " + keylen + " Expected: " + ekeylen + "\n";
        return failMsg += verifyCreate(dq, elength, esave, efifo, eforce, edesc);
    }

    /**
     <p>Test:  Call DataQueue::create(int).
     <p>Result:  Verify the queue is created correctly and the correct defaults are set for authority, saveSenderInfo, FIFO, forceToAuxiliaryStorage, and description.
     **/
    public void Var001()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(80);
            try
            {
                // Verify create.
                // Note that there is no way to easily verify the authority.
                String failMsg = verifyCreate(dq, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int).
     <p>Result:  Verify the queue is created correctly and the correct defaults are set for authority, saveSenderInfo, FIFO, forceToAuxiliaryStorage, and description.
     **/
    public void Var002()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80);
            try
            {
                // Verify create.
                // Note that there is no way to easily verify the authority.
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String).
     <p>Result:  Verify the queue is created correctly and the correct values are set for authority, saveSenderInfo, FIFO, forceToAuxiliaryStorage, and description.
     **/
    public void Var003()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(80, "*LIBCRTAUT", false, true, false, "");
            try
            {
                // Verify create.
                // Note that there is no way to easily verify the authority.
                String failMsg = verifyCreate(dq, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String).
     <p>Result:  Verify the queue is created correctly and the correct defaults are set for authority, saveSenderInfo, FIFO, forceToAuxiliaryStorage, and description.
     **/
    public void Var004()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80, "*LIBCRTAUT", false, false, "");
            try
            {
                // Verify create.
                // Note that there is no way to easily verify the authority.
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(DataQueueAttributes).
     <p>Result:  Verify the queue is created correctly and the correct defaults are set for authority, saveSenderInfo, FIFO, forceToAuxiliaryStorage, and description.
     **/
    public void Var005()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueueAttributes atts = new DataQueueAttributes();

            dq2.create(atts);
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 1000, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int) on a queue that has *CURLIB for the library.  Note QGPL is the default for current library.  This test will fail if the user profile has changed its current library.
     <p>Result:  Verify the queue is created in QGPL.
     **/
    public void Var006()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAQ");
            dq2.create(80);
            try
            {
                // Verify queue now exists in QGPL.
                String failMsg = verifyCreate(dq, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) on a queue that has *CURLIB for the library.  Note QGPL is the default for current library.  This test will fail if the user profile has changed its current library.
     <p>Result:  Verify the queue is created in QGPL.
     **/
    public void Var007()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80);
            try
            {
                // Verify queue now exists in QGPL.
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) on a queue that has *CURLIB for the library.  Note QGPL is the default for current library.  This test will fail if the user profile has changed its current library.
     <p>Result:  Verify the queue is created in QGPL.
     **/
    public void Var008()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAQ");
            dq2.create(80, "*USE", false, true, false, "");
            try
            {
                // Verify queue now exists in QGPL.
                String failMsg = verifyCreate(dq, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) on a queue that has *CURLIB for the library.  Note QGPL is the default for current library.  This test will fail if the user profile has changed its current library.
     <p>Result:  Verify the queue is created in QGPL.
     **/
    public void Var009()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/%CURLIB%.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QGPL.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, "");
            try
            {
                // Verify queue now exists in QGPL.
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) using valid boundary value of 1 for the key length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var010()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 1.
            dq2.create(1, 80);
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 1, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid boundary value of 1 for the key length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var011()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 1.
            dq2.create(1, 80, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 1, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) using valid boundary value of 256 for the key length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var012()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 256.
            dq2.create(256, 80);
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 256, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid boundary value of 256 for the key length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var013()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 256.
            dq2.create(256, 80, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 256, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) passing an invalid key length of 0.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var014()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // first try too small
            try
            {
                dq.create(0, 80);
                failed("No exception with 0.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "keyLength (0): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing an invalid key length of 0.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var015()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // first try too small
            try
            {
                dq.create(0, 80, "*USE", true, false, "");
                failed("No exception with 0.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "keyLength (0): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int) passing an invalid key length of 257.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var016()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // try too large
            try
            {
                dq.create(257, 80);
                failed("No exception with 257.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "keyLength (257): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing an invalid key length of 257.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var017()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // try too large
            try
            {
                dq.create(257, 80, "*USE", true, false, "");
                failed("No exception with 257.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "keyLength (257): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int) using valid boundary value of 1 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var018()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 1.
            dq2.create(1);
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 1, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) using valid boundary value of 1 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var019()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 1.
            dq2.create(10, 1);
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 1, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid boundary value of 1 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var020()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 1.
            dq2.create(1, "*USE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 1, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid boundary value of 1 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var021()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 1.
            dq2.create(10, 1, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 1, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int) using valid boundary value of 31744 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var022()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31744.
            dq2.create(31744);
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 31744, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) using valid boundary value of 31744 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var023()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31744.
            dq2.create(10, 31744);
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 31744, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid boundary value of 31744 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var024()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31744.
            dq2.create(31744, "*USE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 31744, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid boundary value of 31744 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var025()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31744.
            dq2.create(10, 31744, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 31744, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int) using valid boundary value of 31745 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var026()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31745.
            dq2.create(31745);
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 31745, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) using valid boundary value of 31745 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var027()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31745.
            dq2.create(10, 31745);
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 31745, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid boundary value of 31745 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var028()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31745.
            dq2.create(31745, "*USE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 31745, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid boundary value of 31745 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var029()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 31745.
            dq2.create(10, 31745, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 31745, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int) using valid boundary value of 64512 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var030()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 64512.
            dq2.create(64512);
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 64512, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) using valid boundary value of 64512 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var031()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 64512.
            dq2.create(10, 64512);
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 64512, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid boundary value of 64512 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var032()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 64512.
            dq2.create(64512, "*USE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 64512, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid boundary value of 64512 for the entry length.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var033()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Create with length 64512.
            dq2.create(10, 64512, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 64512, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int) passing an invalid entry length of 0.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var034()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // first try too small
            try
            {
                dq.create(0);
                failed("No exception with 0.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (0): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int) passing an invalid entry length of 0.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var035()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // first try too small
            try
            {
                dq.create(10, 0);
                failed("No exception with 0.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (0): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) passing an invalid entry length of 0.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var036()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // first try too small
            try
            {
                dq.create(0, "*USE", true, true, false, "");
                failed("No exception with 0.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (0): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing an invalid entry length of 0.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var037()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // first try too small
            try
            {
                dq.create(10, 0, "*USE", true, false, "");
                failed("No exception with 0.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (0): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int) passing an invalid entry length of 64513.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var038()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // try too large
            try
            {
                dq.create(64513);
                failed("No exception with 64513.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (64513): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int) passing an invalid entry length of 64513.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var039()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // try too large
            try
            {
                dq.create(10, 64513);
                failed("No exception with 64513.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (64513): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) passing an invalid entry length of 64513.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var040()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // try too large
            try
            {
                dq.create(64513, "*USE", true, true, false, "");
                failed("No exception with 64513.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (64513): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing an invalid entry length of 64513.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var041()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // try too large
            try
            {
                dq.create(10, 64513, "*USE", true, false, "");
                failed("No exception with 64513.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "maxEntryLength (64513): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid authority value of *ALL.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var042()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(40, "*ALL", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid authority value of *ALL.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var043()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 40, "*ALL", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid authority value of *CHANGE.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var044()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(40, "*CHANGE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid authority value of *CHANGE.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var045()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 40, "*CHANGE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid authority value of *EXCLUDE.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var046()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(40, "*EXCLUDE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid authority value of *EXCLUDE.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var047()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 40, "*EXCLUDE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid authority value of *USE.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var048()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(40, "*USE", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid authority value of *USE.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var049()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 40, "*USE", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid authority value of *LIBCRTAUT.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var050()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(40, "*LIBCRTAUT", false, true, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyCreate(dq, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid authority value of *LIBCRTAUT.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var051()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 40, "*LIBCRTAUT", false, false, "");
            try
            {
                // Verify create.
                String failMsg = verifyKeyedCreate(dq, 10, 40, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) passing a null for the authority.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var052()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(80, null, true, true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "authority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing a null for the authority.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var053()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(10, 80, null, true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "authority");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) passing an invalid authority.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var054()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(80, "*NOTYOU", true, true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority (*NOTYOU): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing an invalid authority.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var055()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(10, 80, "*NOTYOU", true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority (*NOTYOU): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid saveSenderInfo value of true.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var056()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // true
            dq2.create(80, "*USE", true, false, false, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, true, false, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid saveSenderInfo value of true.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var057()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // true
            dq2.create(10, 80, "*USE", true, false, "");
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, true, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid saveSenderInfo value of false.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var058()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // false
            dq2.create(80, "*USE", false, true, true, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, false, true, true, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid saveSenderInfo value of false.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var059()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // false
            dq2.create(10, 80, "*USE", false, true, "");
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, true, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid FIFO value of true.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var060()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // true
            dq2.create(80, "*USE", false, true, false, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String).
     <p>Result:  Verify the queue is created correctly and FIFO value is always true.
     **/
    public void Var061()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // Note FIFO is cannot be speciifed for a keyed queue, it's value should always be true.
            dq2.create(10, 80, "*USE", false, false, "");
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid FIFO value of false.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var062()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // false
            dq2.create(80, "*USE", true, false, true, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, true, false, true, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid forceToAuxiliaryStorage value of true.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var063()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // true
            dq2.create(80, "*USE", false, false, true, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, false, false, true, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid forceToAuxiliaryStorage value of true.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var064()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // true
            dq2.create(10, 80, "*USE", false, true, "");
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, true, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid forceToAuxiliaryStorage value of false.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var065()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // false
            dq2.create(80, "*USE", true, true, false, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, true, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid forceToAuxiliaryStorage value of false.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var066()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // false
            dq2.create(10, 80, "*USE", true, false, "");
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, true, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid description of length 0.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var067()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(80, "*USE", false, true, false, "");
            try
            {
                String failMsg = verifyCreate(dq, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid description of length 0.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var068()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, "");
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, EXPECTEDTEXT);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using valid description of length 50.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var069()
    {
        try
        {
            String desc = "12345678901234567890123456789012345678901234567890";
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            // false
            dq2.create(80, "*USE", false, true, false, desc);
            try
            {
                String failMsg = verifyCreate(dq, 80, false, true, false, "12345678901234567890123456789012345678901234567890");
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using valid description of length 50.
     <p>Result:  Verify the queue is created correctly.
     **/
    public void Var070()
    {
        try
        {
            String desc = "12345678901234567890123456789012345678901234567890";
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, desc);
            try
            {
                String failMsg = verifyKeyedCreate(dq, 10, 80, false, true, false, "12345678901234567890123456789012345678901234567890");
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) passing a null for the description.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var071()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(80, "*USE", true, true, false, null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "description");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing a null for the description.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var072()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(10, 80, "*USE", true, false, null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "description");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) passing a description which is too long.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var073()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(80, "*USE", true, true, false, "123456789012345678901234567890123456789012345678901");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "description (123456789012345678901234567890123456789012345678901): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) passing a description which is too long.
     <p>Result:  Verify an ExtendedIllegalArgumentException is thrown.
     **/
    public void Var074()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(10, 80, "*USE", true, false, "123456789012345678901234567890123456789012345678901");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "description (123456789012345678901234567890123456789012345678901): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int) using a library that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var075()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(80);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int) using a library that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var076()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(10, 80);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) using a library that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var077()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(80, "*USE", true, true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) using a library that does not exist.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var078()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ");
            try
            {
                dq.create(10, 80, "*USE", true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/BADLIB.LIB/CRTTEST.DTAQ: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int) when the queue already exists.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var079()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(80);
            try
            {
                dq.create(80);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectAlreadyExistsException", "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ: ", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
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
     <p>Test:  Call KeyedDataQueue::create(int, int) when the queue already exists.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var080()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80);
            try
            {
                dq.create(10, 80);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectAlreadyExistsException", "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ: ", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
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
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) when the queue already exists.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var081()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(80);
            try
            {
                dq.create(80, "*USE", true, true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectAlreadyExistsException", "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ: ", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
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
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) when the queue already exists.
     <p>Result:  Verify an ObjectDoesNotExistException is thrown.
     **/
    public void Var082()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ");
            dq2.create(10, 80);
            try
            {
                dq.create(10, 80, "*USE", true, false, "");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ObjectAlreadyExistsException", "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAQ: ", ObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS);
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
     <p>Test:  Drop the connection to the data queue server during a call to DataQueue::create(int).
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var083()
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
		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST1.DTAQ");
		    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST2.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		// Create this queue just to get connection going.  This will cause the next connect to go fast, so that the timing of the drop should work correctly (during create).
		    dq2.create(80);
		    try
		    {
			drop.start();
			dq.create(80);
			// Ignore this. 
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			dq.delete();
                        dq2.delete();
		    }
		    catch (Exception e)
		    {
			drop.join();
                        try { dq.delete(); } catch (Exception e2) {} 
			dq2.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  connection not dropped after "+DROPPER_RETRIES+" attempts");
		assertCondition(true); 
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::create(int, int).
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var084()
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
		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST5.DTAQ");
		    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST6.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		// Create this queue just to get connection going.  This will cause the next connect to go fast, so that the timing of the drop should work correctly (during create).
		    dq2.create(10, 80);
		    try
		    {
			drop.start();
			dq.create(10, 80);
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			dq.delete();
                        dq2.delete();
		    }
		    catch (Exception e)
		    {
			drop.join();
                        try { dq.delete(); } catch (Exception e2) {} 
			dq2.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  connection not dropped after "+DROPPER_RETRIES+" attempts");
		assertCondition(true); 

            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to DataQueue::create(int, String, boolean, boolean, boolean, String).
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var085()
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

		    DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST3.DTAQ");
		    DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST4.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		// Create this queue just to get connection going.  This will cause the next connect to go fast, so that the timing of the drop should work correctly (during create).
		    dq2.create(80);
		    try
		    {
			drop.start();
			dq.create(80, "*USE", false, false, false, "");
			// ignore and retry 
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			dq.delete();
                        dq2.delete();
		    }
		    catch (Exception e)
		    {
			drop.join();
                        try { 
                        dq.delete();
                        } catch (Exception e2) { } 
			dq2.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  connection not dropped after "+DROPPER_RETRIES+" attempts");
		assertCondition(true); 

            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Drop the connection to the data queue server during a call to KeyedDataQueue::create(int, int, String, boolean, boolean, String).
     <p>Result:  Verify a ConnectionDroppedException is thrown.
     **/
    public void Var086()
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

		    KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST7.DTAQ");
		    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQTEST.LIB/CDCRTTEST8.DTAQ");
		    ConnectionDropper drop = new ConnectionDropper(systemObject_, AS400.DATAQUEUE, 10);
		// Create this queue just to get connection going.  This will cause the next connect to go fast, so that the timing of the drop should work correctly (during create).
		    dq2.create(10, 80);
		    try
		    {
			drop.start();
			dq.create(10, 80, "*USE", false, false, "");
			// notApplicable("ConnectionDropper"); // Threads did not force condition
			drop.join();
			dq.delete();
                        dq2.delete();
		    }
		    catch (Exception e)
		    {
			drop.join();
                        try { dq.delete(); } catch (Exception e2) {} 
			dq2.delete();
			assertExceptionIs(e, "ConnectionDroppedException", ConnectionDroppedException.DISCONNECT_RECEIVED);
			return; 
		    }
		}
		System.out.println("Warning:  connection not dropped after "+DROPPER_RETRIES+" attempts");
		assertCondition(true); 

            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int) on a queue to which the user does not have enough authority to the library.  Note: *READ and *ADD authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var087()
    {
        try
        {
            String user = systemObject_.getUserId();
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
            cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " *READ");
            cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *ADD");
            try
            {
                dq.create(10);
                failed("No exception when creating data queue with user "+user);
                cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int) on a queue to which the user does not have enough authority to the library.  Note: *READ and *ADD authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var088()
    {
        try
        {
            String user = systemObject_.getUserId();
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
            cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " *READ");
            cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *ADD");
            try
            {
                dq.create(5, 10);
                failed("No exception acccessing dataqueue with "+user);
                cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::create(int, String, boolean, boolean, boolean, String) on a queue to which the user does not have enough authority to the library.  Note: *READ and *ADD authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var089()
    {
        try
        {
            String user = systemObject_.getUserId();
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
            cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " *READ");
            cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *ADD");
            try
            {
                dq.create(10, "*USE", false, false, false, "");
                failed("No exception acccessing dataqueue with "+user);
                cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::create(int, int, String, boolean, boolean, String) on a queue to which the user does not have enough authority to the library.  Note: *READ and *ADD authority are required.
     <p>Result:  Verify an AS400SecurityException is thrown.
     **/
    public void Var090()
    {
        try
        {
            String user = systemObject_.getUserId();
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ");
            cmdRun("QSYS/GRTOBJAUT DQSECTEST *LIB " + user + " *READ");
            cmdRun("RVKOBJAUT DQSECTEST *LIB " + user + " *ADD");
            try
            {
                dq.create(5, 10, "*USE", false, false, "");
                failed("No exception acccessing dataqueue with "+user);
                cmdRun("QSYS/DLTDTAQ DQSECTEST/SECTST");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/DQSECTEST.LIB/SECTST.DTAQ: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    // test exists
    public void Var091()
    {
        // boolean Continue = true;

        DataQueue      sdq;
        DataQueue      sdq2;
        DataQueue      sdq3;
        // DataQueue      sdq4;
        KeyedDataQueue kdq;
        KeyedDataQueue kdq2;
        KeyedDataQueue kdq3;

        try
        {
            sdq  = new DataQueue(systemObject_, "/QSYS.LIB/NO.LIB/MYDQ.DTAQ");
            if (sdq.exists())
            {
                failed("Returned true when lib NO.LIB should not exist");
                return;
            }

            sdq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYDQ.DTAQ");
            if (sdq2.exists())
            {
                failed("Returned true when dq qtemp/mydq should not exist");
                return;
            }

            kdq  = new KeyedDataQueue(systemObject_, "/QSYS.LIB/NO.LIB/MYKDQ.DTAQ");
            if (kdq.exists())
            {
                failed("Returned true when lib NO.LIB should not exist (keyed DQ)");
                return;
            }

            kdq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYKDQ.DTAQ");
            if (sdq2.exists())
            {
                failed("Returned true when keyed dq qtemp/mykdq should not exist");
                return;
            }

            sdq2.create(1024);
            kdq2.create(100, 1024);

            if (! sdq2.exists())
            {
                failed("Returned false when dq should exist");
                return;
            }

            if (! kdq2.exists())
            {
                failed("Returned false when keyed dq should exist");
                return;
            }

            sdq3 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYDQ.DTAQ");
            if (! sdq3.exists())
            {
                failed("Returned false when dq should exist");
                return;
            }

            kdq3 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYKDQ.DTAQ");
            if (! kdq3.exists())
            {
                failed("Returned false when dq should exist");
                return;
            }

            sdq2.delete();
            kdq2.delete();

            if (sdq2.exists())
            {
                failed("Returned true when dq qtemp/mydq was just deleted");
                return;
            }

            if (kdq2.exists())
            {
                failed("Returned true when keyed dq just deleted");
                return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
