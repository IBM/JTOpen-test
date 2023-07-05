///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQAttributesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.KeyedDataQueue;

/**
 Testcase DQAttributesTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>DataQueue::getCcsid()
 <li>KeyedDataQueue::getCcsid()
 <li>DataQueue::setCcsid(int)
 <li>KeyedDataQueue::setCcsid(int)
 <li>DataQueue::getDescription()
 <li>KeyedDataQueue::getDescription()
 <li>DataQueue::getForceToAuxiliaryStorage()
 <li>KeyedDataQueue::getForceToAuxiliaryStorage()
 <li>DataQueue::getMaxEntryLength()
 <li>KeyedDataQueue::getMaxEntryLength()
 <li>DataQueue::getName()
 <li>KeyedDataQueue::getName()
 <li>DataQueue::getPath()
 <li>KeyedDataQueue::getPath()
 <li>DataQueue::setPath(String)
 <li>KeyedDataQueue::setPath(String)
 <li>DataQueue::getSaveSenderInformation()
 <li>KeyedDataQueue::getSaveSenderInformation()
 <li>DataQueue::getSystem()
 <li>KeyedDataQueue::getSystem()
 <li>DataQueue::setSystem(AS400)
 <li>KeyedDataQueue::setSystem(AS400)
 <li>DataQueue::isFIFO()
 <li>KeyedDataQueue::isFIFO()
 <li>KeyedDataQueue::getKeyLength()
 </ul>
 <p>Note that not all values for all attributes are tested.  All attribute values are tested in DQCreateTestcase.
 **/
public class DQAttributesTestcase extends Testcase
{
    private static final String EXPECTEDTEXT = "text                                              ";

    /**
     <p>Test:  Call DataQueue::getCcsid() before the CCSID has been set.
     <p>Result:  Verify zero is returned.
     **/
    public void Var001()
    {
        try
        {
            DataQueue dq = new DataQueue();
            int ccsid = dq.getCcsid();
            assertCondition(ccsid == 0, "Incorrect value returned: " + ccsid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::getCcsid() before the CCSID has been set.
     <p>Result:  Verify zero is returned.
     **/
    public void Var002()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            int ccsid = dq.getCcsid();
            assertCondition(ccsid == 0, "Incorrect value returned: " + ccsid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call DataQueue::setCcsid(int).
     <p>Result:  Verify with DataQueue::getCcsid() that the correct value is set.
     **/
    public void Var003()
    {
        try
        {
            DataQueue dq = new DataQueue();
            dq.setCcsid(37);
            int ccsid = dq.getCcsid();   // verify with get
            assertCondition(ccsid == 37, "Incorrect value returned: " + ccsid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::setCcsid(int).
     <p>Result:  Verify with KeyedDataQueue::getCcsid() that the correct value is set.
     **/
    public void Var004()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            dq.setCcsid(37);
            int ccsid = dq.getCcsid();   // verify with get
            assertCondition(ccsid == 37, "Incorrect value returned: " + ccsid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::getDescription().
     <p>Result: Verify correct description is returned.
     **/
    public void Var005()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(80, "*USE", false, true, false, "text");
            try
            {
                String desc = dq.getDescription();
                assertCondition(desc.equals(EXPECTEDTEXT), "Wrong value: '" + desc + "'\n   Expected: '" + EXPECTEDTEXT + "'");
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
     <p>Test: Call KeyedDataQueue::getDescription().
     <p>Result: Verify correct description is returned.
     **/
    public void Var006()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, "text");
            try
            {
                String desc = dq.getDescription();
                assertCondition(desc.equals(EXPECTEDTEXT), "Wrong value: '" + desc + "'\n   Expected: '" + EXPECTEDTEXT + "'");
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
     <p>Test: Call DataQueue::getForceToAuxiliaryStorage.
     <p>Result: Verify correct value is returned.
     **/
    public void Var007()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(80, "*USE", false, false,true,  "text");
            try
            {
                boolean val = dq.getForceToAuxiliaryStorage();
                assertCondition(val == true, "Wrong value: " + val);
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
     <p>Test: Call KeyedDataQueue::getForceToAuxiliaryStorage.
     <p>Result: Verify correct value is returned.
     **/
    public void Var008()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, true, "text");
            try
            {
                boolean val = dq.getForceToAuxiliaryStorage();
                assertCondition(val == true, "Wrong value: " + val);
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
     <p>Test: Call DataQueue::getMaxEntryLength().
     <p>Result: Verify correct value is returned.
     **/
    public void Var009()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(80, "*USE", false, true, false, "text");
            try
            {
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
     <p>Test: Call KeyedDataQueue::getMaxEntryLength().
     <p>Result: Verify correct value is returned.
     **/
    public void Var010()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, "text");
            try
            {
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
     <p>Test: Call DataQueue::getName() before the path has been set.
     <p>Result: Verify that an empty String is returned.
     **/
    public void Var011()
    {
        try
        {
            DataQueue dq = new DataQueue();
            String name = dq.getName();
            assertCondition(name.equals(""), "Incorrect name returned: '" + name + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call KeyedDataQueue::getName() before the path has been set.
     <p>Result: Verify that an empty String is returned.
     **/
    public void Var012()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            String name = dq.getName();
            assertCondition(name.equals(""), "Incorrect name returned: '" + name + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::getName() with the path passed on the constructor.
     <p>Result: Verify that the correct name is returned.
     **/
    public void Var013()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            String name = dq.getName();
            assertCondition(name.equals("ATTRTEST"), "Incorrect name returned: '" + name + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call KeyedDataQueue::getName() with the path passed on the constructor.
     <p>Result: Verify that the correct name is returned.
     **/
    public void Var014()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            String name = dq.getName();
            assertCondition(name.equals("ATTRTEST"), "Incorrect name returned: '" + name + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::getName() when path is set by DataQueue::setPath(String).
     <p>Result: Verify that the correct name is returned.
     **/
    public void Var015()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/FAILED.DTAQ");
            dq.setPath("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            String name = dq.getName();
            assertCondition(name.equals("ATTRTEST"), "Incorrect name returned: '" + name + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call KeyedDataQueue::getName() when path is set by KeyedDataQueue::setPath(String).
     <p>Result: Verify that the correct name is returned.
     **/
    public void Var016()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/FAILED.DTAQ");
            dq.setPath("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            String name = dq.getName();
            assertCondition(name.equals("ATTRTEST"), "Incorrect name returned: '" + name + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::getPath() before the path has been set.
     <p>Result: Verify that an empty String is returned.
     **/
    public void Var017()
    {
        try
        {
            DataQueue dq = new DataQueue();
            String path = dq.getPath();
            assertCondition(path.equals(""), "Incorrect path returned: '" + path + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call KeyedDataQueue::getPath() before the path has been set.
     <p>Result: Verify that an empty String is returned.
     **/
    public void Var018()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            String path = dq.getPath();
            assertCondition(path.equals(""), "Incorrect path returned: '" + path + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::getPath() with the path passed on the constructor.
     <p>Result: Verify that the correct path is returned.
     **/
    public void Var019()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            String path = dq.getPath();
            assertCondition(path.equals("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ"), "Incorrect path returned: '" + path + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call KeyedDataQueue::getPath() when path passed on the constructor.
     <p>Result: Verify that the correct path is returned.
     **/
    public void Var020()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            String path = dq.getPath();
            assertCondition(path.equals("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ"), "Incorrect path returned: '" + path + "'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::setPath(String).
     <p>Result:  Verify with DataQueue::getPath() that the correct value is set.
     **/
    public void Var021()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/BAD.LIB/ONE.DTAQ");
            dq.setPath("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.create(80); // verify with create
            try
            {
                String path = dq.getPath();
                assertCondition(path.equals("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ"), "Incorrect path returned: '" + path + "'");
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
     <p>Test:  Call KeyedDataQueue::setPath(String).
     <p>Result:  Verify with KeyedDataQueue::getPath() that the correct value is set.
     **/
    public void Var022()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/BAD.LIB/ONE.DTAQ");
            dq.setPath("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.create(10, 80, "*USE", false, false, "text"); // verify with create
            try
            {
                String path = dq.getPath();
                assertCondition(path.equals("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ"), "Incorrect path returned: " + path);
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
     <p>Test:  Call DataQueue::setPath(String) with a null path.
     <p>Result:  Verify that a NullPointerException is thrown.
     **/
    public void Var023()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            try
            {
                dq.setPath(null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "path");
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::setPath(String) with a null path.
     <p>Result:  Verify that a NullPointerException is thrown.
     **/
    public void Var024()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            try
            {
                dq.setPath(null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "path");
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call DataQueue::setPath(String) after connection made.
     <p>Result:  Verify that an ExtendedIllegalStateException is thrown.
     **/
    public void Var025()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.create(80);
            try
            {
                dq.setPath("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path: ", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
            }
            finally
            {
                dq.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::setPath(String) after connection made.
     <p>Result:  Verify that an ExtendedIllegalStateException is thrown.
     **/
    public void Var026()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.create(10, 80);
            try
            {
                dq.setPath("/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path: ", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
            }
            finally
            {
                dq.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call DataQueue::getSaveSenderInformation().
     <p>Result: Verify correct value is returned.
     **/
    public void Var027()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(80, "*USE", true, false, false, "text");
            try
            {
                boolean val = dq.getSaveSenderInformation();
                assertCondition(val == true, "Wrong value: " + val);
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
     <p>Test:  Call KeyedDataQueue::getSaveSenderInformation().
     <p>Result: Verify correct value is returned.
     **/
    public void Var028()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(10, 80, "*USE", true, false, "text");
            try
            {
                boolean val = dq.getSaveSenderInformation();
                assertCondition(val == true, "Wrong value: " + val);
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
     <p>Test:  Call DataQueue::getSystem() before the system has been set.
     <p>Result: Verify null is returned.
     **/
    public void Var029()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            AS400 system = dq.getSystem();
            assertCondition(system == null, "Incorrect AS400 object returned: " + system);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::getSystem() before the system has been set.
     <p>Result: Verify null is returned.
     **/
    public void Var030()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue();
            AS400 system = dq.getSystem();
            assertCondition(system == null, "Incorrect AS400 object returned: " + system);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::getSystem() with the path passed on the constructor.
     <p>Result: Verify that the system is returned.
     **/
    public void Var031()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            AS400 system = dq.getSystem();
            assertCondition(systemObject_ == system, "Incorrect AS400 object returned: " + system);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call KeyedDataQueue::getSystem() with the path passed on the constructor.
     <p>Result: Verify that the system is returned.
     **/
    public void Var032()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            AS400 system = dq.getSystem();
            assertCondition(systemObject_ == system, "Incorrect AS400 object returned: " + system);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <p>Test: Call DataQueue::setSystem(AS400).
     <p>Result:  Verify with DataQueue::getSystem() that the correct value is set.
     **/
    public void Var033()
    {
        try
        {
            AS400 dummy = new AS400("Badsys", "xxx", "yyy");
            DataQueue dq = new DataQueue(dummy, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.setSystem(systemObject_);
            dq.create(80); // verify with create
            try
            {
                AS400 system = dq.getSystem();   // verify with get
                assertCondition(systemObject_ == system, "Incorrect AS400 object returned: " + system);
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
     <p>Test: Call KeyedDataQueue::setSystem(AS400).
     <p>Result:  Verify with DataQueue::getSystem() that the correct value is set.
     **/
    public void Var034()
    {
        try
        {
            AS400 dummy = new AS400("Badsys", "xxx", "yyy");
            KeyedDataQueue dq = new KeyedDataQueue(dummy, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.setSystem(systemObject_);
            dq.create(10, 80); // verify with create
            try
            {
                AS400 system = dq.getSystem();   // verify with get
                assertCondition(systemObject_ == system, "Incorrect AS400 object returned: " + system);
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
     <p>Test:  Call DataQueue::setSystem(AS400) with a null system.
     <p>Result:  Verify that a NullPointerException is thrown.
     **/
    public void Var035()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            try
            {
                dq.setSystem(null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "system");
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::setSystem(AS400) with a null system.
     <p>Result:  Verify that a NullPointerException is thrown.
     **/
    public void Var036()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            try
            {
                dq.setSystem(null);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "NullPointerException", "system");
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call DataQueue::setSystem() after connection made.
     <p>Result:  Verify that an ExtendedIllegalStateException is thrown.
     **/
    public void Var037()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.create(80);
            try
            {
                dq.setSystem(systemObject_);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
            }
            finally
            {
                dq.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call KeyedDataQueue::setSystem() after connection made.
     <p>Result:  Verify that an ExtendedIllegalStateException is thrown.
     **/
    public void Var038()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq.create(10, 80);
            try
            {
                dq.setSystem(systemObject_);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
            }
            finally
            {
                dq.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Setup failed.");
        }
    }

    /**
     <p>Test:  Call DataQueue::isFIFO().
     <p>Result: Verify correct value is returned.
     **/
    public void Var039()
    {
        try
        {
            DataQueue dq = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            DataQueue dq2 = new DataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(80, "*USE", true, false, true, "text");
            try
            {
                boolean val = dq.isFIFO();
                assertCondition(val == false, "Wrong value: " + val);
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
     <p>Test:  Call KeyedDataQueue::isFIFO().
     <p>Result: Verify correct value is returned.
     **/
    public void Var040()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, "text");
            try
            {
                boolean val = dq.isFIFO();
                assertCondition(val == true, "Wrong value: " + val);
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
     <p>Test:  Call KeyedDataQueue::getKeyLength().
     <p>Result: Verify correct value is returned.
     **/
    public void Var041()
    {
        try
        {
            KeyedDataQueue dq = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAQ");
            dq2.create(10, 80, "*USE", false, false, "text");
            try
            {
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
}
