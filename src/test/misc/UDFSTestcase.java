///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UDFSTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.misc;

import com.ibm.as400.access.UDFS;

import test.Testcase;

import com.ibm.as400.access.FileAttributes;
import com.ibm.as400.access.ErrorCompletingRequestException;

/**
 Testcase UDFSTestcase.
 **/
public class UDFSTestcase extends Testcase
{
    String verifyUDFS(UDFS obj, String eowner, int eccsid, String ecaseSensitivity, String edescription, String epathWhereMounted, boolean eisUdfs, boolean eisMounted) throws Exception
    {
        String failMsg = "";

        UDFS.UdfsInformationStructure uis = obj.getUdfsInformationStructure();
        String owner = uis.getOwner();
        int ccsid = uis.getCcsid();
        String caseSensitivity = uis.getCaseSensitivity();
        String description = uis.getDescription();
        String pathWhereMounted = uis.getPathWhereMounted();

        UDFS.MountedFsInformationStructure mfis = obj.getMountedFsInformationStructure();
        boolean isUdfs = mfis.isUdfs();
        boolean isMounted = mfis.isMounted();

        if (!owner.equals(eowner)) failMsg += "\nOwner: " + owner + " Expected: " + eowner;
        if (ccsid != eccsid) failMsg += "\nCCSID: " + ccsid + " Expected: " + eccsid;
        if (!caseSensitivity.equals(ecaseSensitivity)) failMsg += "\nCase sensitivity: " + caseSensitivity + " Expected: " + ecaseSensitivity;
        if (!description.equals(edescription)) failMsg += "\nDescription: " + description + " Expected: " + edescription;
        if (!pathWhereMounted.equals(epathWhereMounted)) failMsg += "\nPath where mounted: " + pathWhereMounted + " Expected: " + epathWhereMounted;

        if (isUdfs != eisUdfs) failMsg += "\nIs UDFS: " + isUdfs + " Expected: " + eisUdfs;
        if (isMounted != eisMounted) failMsg += "\nIs mounted: " + isMounted + " Expected: " + eisMounted;

        return failMsg;
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::UDFS(AS400, String).</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var001()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            assertCondition(true,"able to create object "+obj); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::UDFS(null, String).</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var002()
    {
        try
        {
            UDFS obj = new UDFS(null, "/dev/QASP01/tbxtest.udfs");
            failed("No exception."+obj);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::UDFS(AS400, null).</dd>
     <dt>Result:</dt><dd>Verify that a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var003()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, null);
            failed("No exception."+obj);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create().</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create();
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create() with a bad path.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/def/QASP01/tbxtest.udfs");
            try
            {
                obj.create();
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPFA0A9", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with default parameter values.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *RWX.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *RW.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RW", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *RX.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RX", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *WX.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*WX", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *R.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*R", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *W.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*W", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *X.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*X", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *EXCLUDE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*EXCLUDE", new String[] { "*NONE" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of *NONE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*NONE", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicDataAuthority of an authorization list.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            // Delete the authorization list.
            cmdRun("QSYS/DLTAUTL AUTL(TBXUDFSTST)", "CPF2105");
            // Create the authorization list.
            cmdRun("QSYS/CRTAUTL AUTL(TBXUDFSTST)");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create("TBXUDFSTST", new String[] { "*NONE" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
                try
                {
                    String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                    assertCondition(failMsg.equals(""), failMsg + "\n");
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/DLTAUTL AUTL(TBXUDFSTST)");
            }

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *NONE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*NONE" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *NONE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*ALL" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJEXIST.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJEXIST" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJMGT.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJMGT" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJALTER.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJALTER" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJREF.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJREF" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJEXIST and *OBJMGT.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJEXIST", "*OBJMGT" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJEXIST, *OBJMGT, and *OBJALTER.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJEXIST", "*OBJMGT", "*OBJALTER" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with publicObjectAuthority of *OBJEXIST, *OBJMGT, *OBJALTER, and *OBJREF.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*RWX", new String[] { "*OBJEXIST", "*OBJMGT", "*OBJALTER", "*OBJREF" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with auditingValue of *NONE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*NONE", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with auditingValue of *USRPRF.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*USRPRF", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with auditingValue of *CHANGE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var028()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*CHANGE", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with auditingValue of *ALL.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*ALL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with scanningOption of *YES.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*YES", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with scanningOption of *NO.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*NO", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with scanningOption of *CHGONLY.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*CHGONLY", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with specialRestrictions of true.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", true, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with defaultDiskStorageOption of *MINIMIZE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*MINIMIZE", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with defaultDiskStorageOption of *DYNAMIC.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*DYNAMIC", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with defaultMainStorageOption of *MINIMIZE.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*MINIMIZE", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with defaultMainStorageOption of *DYNAMIC.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*DYNAMIC", "*MONO", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with caseSensitivity of *MIXED.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MIXED", "*TYPE2", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MIXED", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with defaultFileFormat of *TYPE1.</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE1", "*BLANK");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with description of "Some text.".</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "Some text.");
            try
            {
                String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "Some text.", "Not mounted", true, false);
                assertCondition(failMsg.equals(""), failMsg + "\n");
            }
            finally
            {
                obj.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::create(String, String[], String, String, boolean, String, String, String, String, String) with a bad path.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/def/QASP01/tbxtest.udfs");
            try
            {
                obj.create("*INDIR", new String[] { "*INDIR" }, "*SYSVAL", "*PARENT", false, "*NORMAL", "*NORMAL", "*MONO", "*TYPE2", "*BLANK");
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPFA0A9", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::delete().</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
            obj.create();
            obj.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::delete() with a bad path.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/def/QASP01/tbxtest.udfs");
            try
            {
                obj.delete();
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPFA0A9", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String).</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            // Delete the mount directory.
            cmdRun("QSYS/RMVDIR DIR('/tbxmount')", "CPFA0A9");
            // Create the mount directory.
            cmdRun("QSYS/CRTDIR DIR('/tbxmount')");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create();
                try
                {
                    obj.mount("/tbxmount");
                    try
                    {
                        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "/tbxmount", true, true);
                        assertCondition(failMsg.equals(""), failMsg + "\n");
                    }
                    finally
                    {
                        obj.unmount();
                    }
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/RMVDIR DIR('/tbxmount')");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String) with a bad path.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/def/QASP01/tbxtest.udfs");
            try
            {
                obj.mount("/tbxmount");
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPFA0A9", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String, boolean, boolean) with true and true.</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            // Delete the mount directory.
            cmdRun("QSYS/RMVDIR DIR('/tbxmount')", "CPFA0A9");
            // Create the mount directory.
            cmdRun("QSYS/CRTDIR DIR('/tbxmount')");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create();
                try
                {
                    obj.mount("/tbxmount", true, true);
                    try
                    {
                        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "/tbxmount", true, true);
                        assertCondition(failMsg.equals(""), failMsg + "\n");
                    }
                    finally
                    {
                        obj.unmount();
                    }
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/RMVDIR DIR('/tbxmount')");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String, boolean, boolean) with true and false.</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            // Delete the mount directory.
            cmdRun("QSYS/RMVDIR DIR('/tbxmount')", "CPFA0A9");
            // Create the mount directory.
            cmdRun("QSYS/CRTDIR DIR('/tbxmount')");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create();
                try
                {
                    obj.mount("/tbxmount", true, false);
                    try
                    {
                        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "/tbxmount", true, true);
                        assertCondition(failMsg.equals(""), failMsg + "\n");
                    }
                    finally
                    {
                        obj.unmount();
                    }
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/RMVDIR DIR('/tbxmount')");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String, boolean, boolean) with false and true.</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var048()
    {
        try
        {
            // Delete the mount directory.
            cmdRun("QSYS/RMVDIR DIR('/tbxmount')", "CPFA0A9");
            // Create the mount directory.
            cmdRun("QSYS/CRTDIR DIR('/tbxmount')");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create();
                try
                {
                    obj.mount("/tbxmount", false, true);
                    try
                    {
                        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "/tbxmount", true, true);
                        assertCondition(failMsg.equals(""), failMsg + "\n");
                    }
                    finally
                    {
                        obj.unmount();
                    }
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/RMVDIR DIR('/tbxmount')");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String, boolean, boolean) with false and false.</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var049()
    {
        try
        {
            // Delete the mount directory.
            cmdRun("QSYS/RMVDIR DIR('/tbxmount')", "CPFA0A9");
            // Create the mount directory.
            cmdRun("QSYS/CRTDIR DIR('/tbxmount')");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create();
                try
                {
                    obj.mount("/tbxmount", false, false);
                    try
                    {
                        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "/tbxmount", true, true);
                        assertCondition(failMsg.equals(""), failMsg + "\n");
                    }
                    finally
                    {
                        obj.unmount();
                    }
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/RMVDIR DIR('/tbxmount')");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::mount(String, boolean, boolean) with a bad path.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/def/QASP01/tbxtest.udfs");
            try
            {
                obj.mount("/tbxmount", true, true);
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPFA0A9", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::unmount().</dd>
     <dt>Result:</dt><dd>Verify the correct state of the object.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            // Delete the mount directory.
            cmdRun("QSYS/RMVDIR DIR('/tbxmount')", "CPFA0A9");
            // Create the mount directory.
            cmdRun("QSYS/CRTDIR DIR('/tbxmount')");
            try
            {
                UDFS obj = new UDFS(pwrSys_, "/dev/QASP01/tbxtest.udfs");
                obj.create();
                try
                {
                    obj.mount("/tbxmount");
                    obj.unmount();
                    String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
                    assertCondition(failMsg.equals(""), failMsg + "\n");
                }
                finally
                {
                    obj.delete();
                }
            }
            finally
            {
                // Delete the authorization list.
                cmdRun("QSYS/RMVDIR DIR('/tbxmount')");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::unmount() with a bad path.</dd>
     <dt>Result:</dt><dd>Verify that an AS400Exception is thrown.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            UDFS obj = new UDFS(pwrSys_, "/def/QASP01/tbxtest.udfs");
            try
            {
                obj.unmount();
                failed("No exception.");
            }
            catch (Exception e)
            {
		assertExceptionStartsWith(e, "AS400Exception", "CPFA0A9", ErrorCompletingRequestException.AS400_ERROR);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::setPreferredStorageUnit(*ANY) and UDFS::create().</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var053()
    {
      UDFS obj = null;
      final String path = "/dev/QASP01/tbxtest.udfs";
      try
      {
        obj = new UDFS(pwrSys_, path);
        obj.setPreferredStorageUnit("*ANY");
        obj.create();
        FileAttributes attrs = new FileAttributes(pwrSys_, path);
        int storageUnit = attrs.getUdfsPreferredStorageUnit();
        if (storageUnit != FileAttributes.UDFS_PREFERRED_STORAGE_UNIT_ANY) {
          failed("Unexpected value returned by getUdfsPreferredStorageUnit(): " + storageUnit);
          return;
        }
        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
        assertCondition(failMsg.equals(""), failMsg + "\n");
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
      finally
      {
        if (obj != null) try { obj.delete(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call UDFS::setPreferredStorageUnit(*SSD) and UDFS::create().</dd>
     <dt>Result:</dt><dd>Verify the correct initial state of the object.</dd>
     </dl>
     **/
    public void Var054()
    {
      UDFS obj = null;
      final String path = "/dev/QASP01/tbxtest.udfs";
      try
      {
        obj = new UDFS(pwrSys_, path);
        obj.setPreferredStorageUnit("*SSD");
        obj.create();
        FileAttributes attrs = new FileAttributes(pwrSys_, path);
        int storageUnit = attrs.getUdfsPreferredStorageUnit();
        int expectedValue = ( pwrSys_.getVRM() >= 0x00070100 ? FileAttributes.UDFS_PREFERRED_STORAGE_UNIT_SSD :  FileAttributes.UDFS_PREFERRED_STORAGE_UNIT_ANY );
        if (storageUnit != expectedValue) {
          failed("Unexpected value returned by getUdfsPreferredStorageUnit(): " + storageUnit);
          return;
        }
        String failMsg = verifyUDFS(obj, pwrSys_.getUserId(), 37, "*MONO", "", "Not mounted", true, false);
        assertCondition(failMsg.equals(""), failMsg + "\n");
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
      finally
      {
        if (obj != null) try { obj.delete(); } catch (Throwable t) {}
      }
    }

}
