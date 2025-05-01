///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FATestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.FileAttributes;

import test.Testcase;

/**
 Test cases for the FileAttributes class.
 **/
public class FATestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "FATestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FATest.main(newArgs); 
   }
    private static final String FILENAME = "testfile";

    // Directory and file that get deleted/recreated between variations.
    private static final String DIR_PATH = "/tbxfatest."+System.getProperty("user.name");;
    private static final String FILE_PATH = DIR_PATH+"/"+FILENAME;

    // Directory and file that persist across variations.
    private static final String DIR_PATH_PERSISTENT = "/fatest."+System.getProperty("user.name");
    private static final String FILE_PATH_PERSISTENT = DIR_PATH_PERSISTENT+"/"+FILENAME;

    boolean skipCleanup = false; 
    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
      // Delete the test file.
      cmdRun("DEL OBJLNK('"+FILE_PATH_PERSISTENT+"')", "CPFA0A9");
      // Delete the test directory.
      cmdRun("RMDIR DIR('"+DIR_PATH_PERSISTENT+"')", "CPFA0A9");
      // Create the test directory.
      cmdRun("CRTDIR DIR('"+DIR_PATH_PERSISTENT+"')");
      // Create the test file.
      cmdRun("QSH CMD('touch "+FILE_PATH_PERSISTENT+"')");

      super.setup();
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
	if (skipCleanup) {
	    System.out.println("Skipping cleanup\n"); 
	} else { 
      // Delete the test file.
	    cmdRun("DEL OBJLNK('"+FILE_PATH_PERSISTENT+"')");
      // Delete the test directory.
	    cmdRun("RMDIR DIR('"+DIR_PATH_PERSISTENT+"')");
	}
      super.cleanup();
    }

    private void setupTestFile()
    {
        // Delete the test file.
	String command= "DEL OBJLNK('"+DIR_PATH+"')"; 
        cmdRun(command, "CPFA0A9");
        // Delete the test directory.
	command="RMDIR DIR('"+DIR_PATH+"')";
        cmdRun(command, "CPFA0A9");
        // Create the test directory.
	command="CRTDIR DIR('"+DIR_PATH+"')"; 
        cmdRun(command);

	command = "QSH CMD('chmod a+rwx "+DIR_PATH+"')";
        cmdRun(command);
        // Create the test file.
	command = "QSH CMD('touch "+FILE_PATH+"')";
        cmdRun(command);

	command = "QSH CMD('chmod a+rwx "+FILE_PATH+"')";
        cmdRun(command);

    }
    private void cleanupTestFile()
    {
	if (skipCleanup) {
	    System.out.println("Skipping cleanupTestFile\n"); 
	} else { 

	// Delete the test file.
	    cmdRun("DEL OBJLNK('"+FILE_PATH+"')");
	// Delete the test directory.
	    cmdRun("RMDIR DIR('"+DIR_PATH+"')");
	}
    }

    /**
     <p>Test:  Call FileAttributes::FileAttributes(AS400, String, boolean) passing valid parmeters.
     <p>Result:  Verify object is constructed without exception.
     **/
    public void Var001()
    {
        try
        {
            FileAttributes fa = new FileAttributes(systemObject_, "/path", true);
            fa = new FileAttributes(new AS400(), "/some/path", false);
            assertCondition(true, "fa="+fa); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <p>Test:  Call FileAttributes::FileAttributes(AS400, String, boolean) passing null for system.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var002()
    {
        try
        {
            FileAttributes fa = new FileAttributes(null, "/path", true);
            failed("No exception."+fa);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     <p>Test:  Call FileAttributes::FileAttributes(AS400, String, boolean) passing null for path.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var003()
    {
        try
        {
            FileAttributes fa = new FileAttributes(systemObject_, null, true);
            failed("No exception."+fa);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getObjectType().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var004()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            String expectedValue = "*SRVPGM";

            String returnValue = fa.getObjectType();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getExtendedAttributeSize().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var005()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            long expectedValue = 300;

            long returnValue = fa.getExtendedAttributeSize();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateTime() with a valid Date parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateTime() returns that date.</dd>
     </dl>
     **/
    public void Var006()
    {
        try
        {
            setupTestFile();
            boolean passed = false; 
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                Date testValue = new Date();
                Date expectedValue = new Date(testValue.getTime() / 1000 * 1000);

                fa.setCreateTime(testValue);
                Date returnValue = fa.getCreateTime();
                passed = returnValue.equals(expectedValue); 
                assertCondition(passed);
            }
            finally
            {
              if (passed) {
                cleanupTestFile();
              }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception getting file attribues for "+FILE_PATH);
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateTime() with a null Date parameter.</dd>
     <dt>Result:</dt><dd>Verify a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var007()
    {
        try
        {
            FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
            Date testValue = null;

            fa.setCreateTime(testValue);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "createTime");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setAccessTime() with a valid Date parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getAccessTime() returns that date.</dd>
     </dl>
     **/
    public void Var008()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                Date testValue = new Date();
                Date expectedValue = new Date(testValue.getTime() / 1000 * 1000 );
                fa.setAccessTime(testValue);
                Date returnValue = fa.getAccessTime();
                assertCondition(returnValue.equals(expectedValue));
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setAccessTime() with a null Date parameter.</dd>
     <dt>Result:</dt><dd>Verify a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var009()
    {
        try
        {
            FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
            Date testValue = null;

            fa.setAccessTime(testValue);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "accessTime");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getChangeTime().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var010()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            Date returnValue = fa.getChangeTime();
            assertCondition(returnValue != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setModifyTime() with a valid Date parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getModifyTime() returns that date.</dd>
     </dl>
     **/
    public void Var011()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                Date testValue = new Date();
                testValue.setTime(111111);
                Date expectedValue = new Date(testValue.getTime() / 1000 * 1000 );

                fa.setModifyTime(testValue);
                Date returnValue = fa.getModifyTime();
                assertCondition(returnValue.equals(expectedValue));
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setModifyTime() with a null Date parameter.</dd>
     <dt>Result:</dt><dd>Verify a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var012()
    {
        try
        {
            FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
            Date testValue = null;

            fa.setModifyTime(testValue);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "modifyTime");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::isStorageFree().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var013()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            boolean expectedValue = false;

            boolean returnValue = fa.isStorageFree();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::isCheckedOut().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var014()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            boolean expectedValue = false;

            boolean returnValue = fa.isCheckedOut();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getCheckedOutUser().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var015()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            String expectedValue = "";

            String returnValue = fa.getCheckedOutUser();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getCheckOutTime().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var016()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            Date returnValue = fa.getCheckOutTime();
            assertCondition(returnValue != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getLocalRemote().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var017()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            int expectedValue = FileAttributes.LOCAL_OBJECT;

            int returnValue = fa.getLocalRemote();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getObjectOwner().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var018()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            String expectedValue = "QSYS";

            String returnValue = fa.getObjectOwner();
            assertCondition(returnValue.equals(expectedValue), "object owner of /QSYS.LIB/QYJSPART.SRVPGM is not QSYS" );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getPrimaryGroup().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var019()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            String expectedValue = "*NONE";

            String returnValue = fa.getPrimaryGroup();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getAuthorizationListName().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var020()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            String expectedValue = "*NONE";

            String returnValue = fa.getAuthorizationListName();
            assertCondition(returnValue.equals(expectedValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getFileId().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var021()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            byte[] returnValue = fa.getFileId();
            assertCondition(returnValue != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getAsp().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var022()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);
            int expectedValue = 1;

            int returnValue = fa.getAsp();
            assertCondition(returnValue == expectedValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getDataSize().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var023()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            long returnValue = fa.getDataSize();
            assertCondition(returnValue > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getAllocatedSize().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var024()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            long returnValue = fa.getAllocatedSize();
            assertCondition(returnValue > 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getResetDate().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var025()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            Date returnValue = fa.getResetDate();
            assertCondition(returnValue != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getLastUsedDate().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var026()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            Date returnValue = fa.getLastUsedDate();
            assertCondition(returnValue != null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getDaysUsedCount().</dd>
     <dt>Result:</dt><dd>Verify the returned data.</dd>
     </dl>
     **/
    public void Var027()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, "/QSYS.LIB/QYJSPART.SRVPGM", true);

            int returnValue = fa.getDaysUsedCount();
            assertCondition(returnValue >= 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcReadOnly() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcReadOnly() returns true.</dd>
     </dl>
     **/
    public void Var028()
    {

	StringBuffer sb = new StringBuffer(); 
        try
        {
            setupTestFile();
            try
            {
		sb.append("Creating FileAttribues object for "+FILE_PATH+"\n"); 
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = true;
                boolean expectedValue = true;
		sb.append("Calling setPcReadOnly\n"); 
                fa.setPcReadOnly(testValue);
                try
                {
		sb.append("Calling isReadOnly\n"); 
                    boolean returnValue = fa.isPcReadOnly();
                    assertCondition(returnValue == expectedValue, "Got returnValue="+returnValue+"\n"+sb.toString());
                }
                finally
                {
                    fa.setPcReadOnly(false);
                }
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {

            failed(e, "Unexpected Exception.\n"+sb.toString());
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcReadOnly() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcReadOnly() returns false.</dd>
     </dl>
     **/
    public void Var029()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = false;
                boolean expectedValue = false;

                fa.setPcReadOnly(testValue);
                boolean returnValue = fa.isPcReadOnly();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcHidden() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcHidden() returns true.</dd>
     </dl>
     **/
    public void Var030()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = true;
                boolean expectedValue = true;

                fa.setPcHidden(testValue);
                boolean returnValue = fa.isPcHidden();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcHidden() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcHidden() returns false.</dd>
     </dl>
     **/
    public void Var031()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = false;
                boolean expectedValue = false;

                fa.setPcHidden(testValue);
                boolean returnValue = fa.isPcHidden();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcSystem() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcSystem() returns true.</dd>
     </dl>
     **/
    public void Var032()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = true;
                boolean expectedValue = true;

                fa.setPcSystem(testValue);
                boolean returnValue = fa.isPcSystem();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcSystem() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcSystem() returns false.</dd>
     </dl>
     **/
    public void Var033()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = false;
                boolean expectedValue = false;

                fa.setPcSystem(testValue);
                boolean returnValue = fa.isPcSystem();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcArchive() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcArchive() returns true.</dd>
     </dl>
     **/
    public void Var034()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = true;
                boolean expectedValue = true;

                fa.setPcArchive(testValue);
                boolean returnValue = fa.isPcArchive();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setPcArchive() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isPcArchive() returns false.</dd>
     </dl>
     **/
    public void Var035()
    {
        try
        {
            setupTestFile();
            try
            {
                FileAttributes fa = new FileAttributes(systemObject_, FILE_PATH, true);
                boolean testValue = false;
                boolean expectedValue = false;

                fa.setPcArchive(testValue);
                boolean returnValue = fa.isPcArchive();
                assertCondition(returnValue == expectedValue);
            }
            finally
            {
                cleanupTestFile();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setSystemArchive() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isSystemArchive() returns true.</dd>
     </dl>
     **/
    public void Var036()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = true;

            fa.setSystemArchive(testValue);

            boolean returnValue = fa.isSystemArchive();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setSystemArchive() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isSystemArchive() returns false.</dd>
     </dl>
     **/
    public void Var037()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            fa.setSystemArchive(testValue);
            boolean returnValue = fa.isSystemArchive();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCodePage() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCodePage() returns that value.</dd>
     </dl>
     **/
    public void Var038()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 37;

            fa.setCodePage(testValue);
            int returnValue = fa.getCodePage();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCodePage() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCodePage() returns that value.</dd>
     </dl>
     **/
    public void Var039()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 819;

            fa.setCodePage(testValue);
            int returnValue = fa.getCodePage();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setAllowCheckpointWrite() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isAllowCheckpointWrite() returns true.</dd>
     </dl>
     **/
    public void Var040()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = true;

            fa.setAllowCheckpointWrite(testValue);
            boolean returnValue = fa.isAllowCheckpointWrite();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setAllowCheckpointWrite() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isAllowCheckpointWrite() returns false.</dd>
     </dl>
     **/
    public void Var041()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            fa.setAllowCheckpointWrite(testValue);
            boolean returnValue = fa.isAllowCheckpointWrite();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCcsid() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCcsid() returns that value.</dd>
     </dl>
     **/
    public void Var042()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 37;

            fa.setCcsid(testValue);
            int returnValue = fa.getCcsid();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCcsid() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCcsid() returns that value.</dd>
     </dl>
     **/
    public void Var043()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 819;

            fa.setCcsid(testValue);
            int returnValue = fa.getCcsid();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setDiskStorageOption() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getDiskStorageOption() returns that value.</dd>
     </dl>
     **/
    public void Var044()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.STORAGE_NORMAL;

            fa.setDiskStorageOption(testValue);
            int returnValue = fa.getDiskStorageOption();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setDiskStorageOption() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getDiskStorageOption() returns that value.</dd>
     </dl>
     **/
    public void Var045()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.STORAGE_MINIMIZE;

            fa.setDiskStorageOption(testValue);
            int returnValue = fa.getDiskStorageOption();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setDiskStorageOption() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getDiskStorageOption() returns that value.</dd>
     </dl>
     **/
    public void Var046()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.STORAGE_DYNAMIC;

            fa.setDiskStorageOption(testValue);
            int returnValue = fa.getDiskStorageOption();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setDiskStorageOption() with a not valid parameter.</dd>
     <dt>Result:</dt><dd>Verify an ErrnoException is thrown.</dd>
     </dl>
     **/
    public void Var047()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 3;

            try
            {
                fa.setDiskStorageOption(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ErrnoException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setMainStorageOption() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getMainStorageOption() returns that value.</dd>
     </dl>
     **/
    public void Var048()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.STORAGE_NORMAL;

            fa.setMainStorageOption(testValue);
            int returnValue = fa.getMainStorageOption();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setMainStorageOption() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getMainStorageOption() returns that value.</dd>
     </dl>
     **/
    public void Var049()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.STORAGE_MINIMIZE;

            fa.setMainStorageOption(testValue);
            int returnValue = fa.getMainStorageOption();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setMainStorageOption() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getMainStorageOption() returns that value.</dd>
     </dl>
     **/
    public void Var050()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.STORAGE_DYNAMIC;

            fa.setMainStorageOption(testValue);
            int returnValue = fa.getMainStorageOption();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setMainStorageOption() with a not valid parameter.</dd>
     <dt>Result:</dt><dd>Verify an ErrnoException is thrown.</dd>
     </dl>
     **/
    public void Var051()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 3;

            try
            {
                fa.setMainStorageOption(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ErrnoException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectScan() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectScan() returns that value.</dd>
     </dl>
     **/
    public void Var052()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            int testValue = FileAttributes.SCANNING_NO;
            fa.setCreateObjectScan(testValue);

            int returnValue = fa.getCreateObjectScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectScan() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectScan() returns that value.</dd>
     </dl>
     **/
    public void Var053()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            int testValue = FileAttributes.SCANNING_YES;

            fa.setCreateObjectScan(testValue);
            int returnValue = fa.getCreateObjectScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectScan() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectScan() returns that value.</dd>
     </dl>
     **/
    public void Var054()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            int testValue = FileAttributes.SCANNING_CHANGE_ONLY;

            fa.setCreateObjectScan(testValue);
            int returnValue = fa.getCreateObjectScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectScan() with a not valid parameter.</dd>
     <dt>Result:</dt><dd>Verify an ErrnoException is thrown.</dd>
     </dl>
     **/
    public void Var055()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            int testValue = 3;

            try
            {
                fa.setCreateObjectScan(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ErrnoException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setScan() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getScan() returns that value.</dd>
     </dl>
     **/
    public void Var056()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.SCANNING_NO;

            fa.setScan(testValue);
            int returnValue = fa.getScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setScan() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getScan() returns that value.</dd>
     </dl>
     **/
    public void Var057()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.SCANNING_YES;

            fa.setScan(testValue);
            int returnValue = fa.getScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setScan() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getScan() returns that value.</dd>
     </dl>
     **/
    public void Var058()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.SCANNING_CHANGE_ONLY;

            fa.setScan(testValue);
            int returnValue = fa.getScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setScan() with a not valid parameter.</dd>
     <dt>Result:</dt><dd>Verify an ErrnoException is thrown.</dd>
     </dl>
     **/
    public void Var059()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 3;

            try
            {
                fa.setScan(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ErrnoException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setAllowSave() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isAllowSave() returns true.</dd>
     </dl>
     **/
    public void Var060()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = true;

            fa.setAllowSave(testValue);
            boolean returnValue = fa.isAllowSave();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setAllowSave() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isAllowSave() returns false.</dd>
     </dl>
     **/
    public void Var061()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            fa.setAllowSave(testValue);
            boolean returnValue = fa.isAllowSave();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setRestrictedRenameAndUnlink() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isRestrictedRenameAndUnlink() returns true.</dd>
     </dl>
     **/
    public void Var062()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = true;

            fa.setRestrictedRenameAndUnlink(testValue);
            boolean returnValue = fa.isRestrictedRenameAndUnlink();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setRestrictedRenameAndUnlink() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isRestrictedRenameAndUnlink() returns false.</dd>
     </dl>
     **/
    public void Var063()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            fa.setRestrictedRenameAndUnlink(testValue);
            boolean returnValue = fa.isRestrictedRenameAndUnlink();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectAuditing() returns that value.</dd>
     </dl>
     **/
    public void Var064()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }

            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = "*SYSVAL";
            fa.setCreateObjectAuditing(testValue);
            String returnValue = fa.getCreateObjectAuditing();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectAuditing() returns that value.</dd>
     </dl>
     **/
    public void Var065()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = "*NONE";

            fa.setCreateObjectAuditing(testValue);
            String returnValue = fa.getCreateObjectAuditing();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectAuditing() returns that value.</dd>
     </dl>
     **/
    public void Var066()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( systemObject_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = "*USRPRF";
    
            fa.setCreateObjectAuditing(testValue);
            String returnValue = fa.getCreateObjectAuditing();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectAuditing() returns that value.</dd>
     </dl>
     **/
    public void Var067()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( systemObject_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = "*CHANGE";
    
            fa.setCreateObjectAuditing(testValue);
            String returnValue = fa.getCreateObjectAuditing();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a valid parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getCreateObjectAuditing() returns that value.</dd>
     </dl>
     **/
    public void Var068()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = "*ALL";

            fa.setCreateObjectAuditing(testValue);
            String returnValue = fa.getCreateObjectAuditing();
            assertCondition(returnValue.equals(testValue));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a not valid parameter.</dd>
     <dt>Result:</dt><dd>Verify an ErrnoException is thrown.</dd>
     </dl>
     **/
    public void Var069()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = "*NOTVALID";

            try
            {
                fa.setCreateObjectAuditing(testValue);
                failed("No exception.");
            }
            catch (Exception e)
            {
                assertExceptionIs(e, "ErrnoException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setCreateObjectAuditing() with a null parameter.</dd>
     <dt>Result:</dt><dd>Verify a NullPointerException is thrown.</dd>
     </dl>
     **/
    public void Var070()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, DIR_PATH_PERSISTENT, true);
            String testValue = null;

            fa.setCreateObjectAuditing(testValue);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "createObjectAuditing");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setSetEffectiveUserId() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isSetEffectiveUserId() returns true.</dd>
     </dl>
     **/
    public void Var071()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = true;

            fa.setSetEffectiveUserId(testValue);
            boolean returnValue = fa.isSetEffectiveUserId();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setSetEffectiveUserId() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isSetEffectiveUserId() returns false.</dd>
     </dl>
     **/
    public void Var072()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            fa.setSetEffectiveUserId(testValue);
            boolean returnValue = fa.isSetEffectiveUserId();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setSetEffectiveGroupId() with a true parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isSetEffectiveGroupId() returns true.</dd>
     </dl>
     **/
    public void Var073()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = true;

            fa.setSetEffectiveGroupId(testValue);
            boolean returnValue = fa.isSetEffectiveGroupId();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::setSetEffectiveGroupId() with a false parameter.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::isSetEffectiveGroupId() returns false.</dd>
     </dl>
     **/
    public void Var074()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            fa.setSetEffectiveGroupId(testValue);
            boolean returnValue = fa.isSetEffectiveGroupId();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    
    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getFileFormat .</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getFileFormat() returns that value.</dd>
     </dl>
     **/
    public void Var075()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 1;
            int returnValue = fa.getFileFormat();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::isJournalingStatus .</dd>
     <dt>Result:</dt><dd>Verify the return data</dd>
     </dl>
     **/
    public void Var076()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;

            boolean returnValue = fa.isJournalingStatus();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getJournalingOptions() .</dd>
     <dt>Result:</dt><dd>Verify expected return</dd>
     </dl>
     **/
    public void Var077()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 0;
    
            int returnValue = fa.getJournalingOptions();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getJournalIdentifier() .</dd>
     <dt>Result:</dt><dd>Verify the return value is null</dd>
     </dl>
     **/
    public void Var078()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            byte [] testValue;   
            
	    testValue = fa.getJournalIdentifier();
	    if ( testValue == null )
	        succeeded();
	    else 
                failed("Unexpected Exception.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getJournal() .</dd>
     <dt>Result:</dt><dd>Verify return value is null</dd>
     </dl>
     **/
    public void Var079()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            String testValue;
    
            testValue = fa.getJournal();
	    if ( testValue == null )
	        succeeded();
	    else 
                failed("Unexpected Exception.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    
    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getJournalingStartTime() .</dd>
     <dt>Result:</dt><dd>Verify return value is null</dd>
     </dl>
     **/
    public void Var080()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
    
            Date startTime= fa.getJournalingStartTime();
	    if ( startTime == null )
	        succeeded();
	    else 
                failed("Unexpected Exception.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getDirectoryFormat() .</dd>
     <dt>Result:</dt><dd>Verify return value</dd>
     </dl>
     **/
    public void Var081()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 0;
    
            int returnValue = fa.getDirectoryFormat();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getAudit().</dd>
     <dt>Result:</dt><dd>Verify *NONE is returned</dd>
     </dl>
     **/
    public void Var082()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            String testValue = "*NONE";
    
            String returnValue = fa.getAudit();
            // Auditing can be enabled on a system, so we should support the values
            // that can be seen. 
            boolean passed = returnValue.equals(testValue);
            if (!passed) {
                testValue="*ALL";
              passed = returnValue.equals(testValue);
            }
            assertCondition(returnValue.equals(testValue), "Got '"+returnValue+"' sb '"+testValue+"'");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: isSystemSigned</dd>
     <dt>Result:</dt><dd>Verify the return value is false</dd>
     </dl>
     **/
    public void Var083()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;
    
            boolean returnValue = fa.isSystemSigned();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: isMultipleSignatures()</dd>
     <dt>Result:</dt><dd>Verify the return value is false</dd>
     </dl>
     **/
    public void Var084()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;
    
            boolean returnValue = fa.isMultipleSignatures();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:getSystemUse .</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getSystemUse() returns that value.</dd>
     </dl>
     **/
    public void Var085()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 0;
            int returnValue = fa.getSystemUse();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    
    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getScanStatus.</dd>
     <dt>Result:</dt><dd>Verify return for getScanStatus</dd>
     </dl>
     **/
    public void Var086()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 0;
    
            int returnValue = fa.getScanStatus();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: isScanSignaturesDifferen.</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::</dd>
     </dl>
     **/
    public void Var087()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;
    
            boolean returnValue = fa.isScanSignaturesDifferent();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: isBinaryScan() .</dd>
     <dt>Result:</dt><dd>Verify the return  </dd>
     </dl>
     **/
    public void Var088()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;
    
            boolean returnValue = fa.isBinaryScan();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getScanCcsid1 .</dd>
     <dt>Result:</dt><dd>Verify return.</dd>
     </dl>
     **/
    public void Var089()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 0;
    
            int returnValue = fa.getScanCcsid1();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    
    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getScanCcsid2 .</dd>
     <dt>Result:</dt><dd>Verify return.</dd>
     </dl>
     **/
    public void Var090()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = 0;
    
            int returnValue = fa.getScanCcsid2();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getStartingJournalReceiverForApply() .</dd>
     <dt>Result:</dt><dd>Verify return </dd>
     </dl>
     **/
    public void Var091()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            String returnValue = fa.getStartingJournalReceiverForApply();
	    if ( returnValue == null )
	        succeeded();
	    else 
                failed("Unexpected Exception.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    
    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: getStartingJournalReceiverAspDevice() .</dd>
     <dt>Result:</dt><dd>Verify return </dd>
     </dl>
     **/
    public void Var092()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            String returnValue = fa.getStartingJournalReceiverAspDevice();
	    if ( returnValue == null )
	        succeeded();
	    else 
                failed("Unexpected Exception.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: isApplyJournaledChangesRequired().</dd>
     <dt>Result:</dt><dd>Verify return</dd>
     </dl>
     **/
    public void Var093()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;
    
            boolean returnValue = fa.isApplyJournaledChangesRequired();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
    
    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes:: isRollbackWasEnded().</dd>
     <dt>Result:</dt><dd>Verify return</dd>
     </dl>
     **/
    public void Var094()
    {
        try
        {
	    // This api is not supported by v5r3
            if ( pwrSys_.getVRM() == 0x00050300 ) 
	    {
                notApplicable("FOR <= V5R3");
                return;
	    }
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            boolean testValue = false;
    
            boolean returnValue = fa.isRollbackWasEnded();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    ///** Yank 23

    ///** Yank 23
    // <dl>
    // <dt>Test:</dt><dd>Call FileAttributes:: .</dd>
    // <dt>Result:</dt><dd>Verify FileAttributes::</dd>
    // </dl>
    // **/
    //public void Var()
    //{
    //    try
    //    {
    //        FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
    //        //testValue;
     
    //        //fa.(testValue);
    //        //boolean returnValue = fa.();
    //        //assertCondition(returnValue == testValue);
    //    }
    //    catch (Exception e)
    //    {
    //        failed(e, "Unexpected Exception.");
    //    }
    //}


    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getUdfsDefaultFormat .</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getUdfsDefaultFormat() returns that value.</dd>
     </dl>
     **/
    public void Var095()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.UDFS_DEFAULT_TYPE1;
            int returnValue = fa.getUdfsDefaultFormat();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /** 
     <dl>
     <dt>Test:</dt><dd>Call FileAttributes::getUdfsPreferredStorageUnit .</dd>
     <dt>Result:</dt><dd>Verify FileAttributes::getUdfsPreferredStorageUnit() returns that value.</dd>
     </dl>
     **/
    public void Var096()
    {
        try
        {
            FileAttributes fa = new FileAttributes(pwrSys_, FILE_PATH_PERSISTENT, true);
            int testValue = FileAttributes.UDFS_PREFERRED_STORAGE_UNIT_ANY;
            int returnValue = fa.getUdfsPreferredStorageUnit();
            assertCondition(returnValue == testValue);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

}
