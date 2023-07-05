///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ORTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Date;
import java.lang.Long;
import java.io.BufferedReader;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ObjectReferences;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileReader;
import com.ibm.as400.access.IFSFileInputStream;

//TEST
import com.ibm.as400.access.*;


/**
 Test cases for the ObjectReferences class.
 **/
public class ORTestcase extends Testcase
{
    private static String PATH = "/ortest/testfile";
    private static String DIR_PATH = "/ortest";
    private IFSFile file;
    private BufferedReader reader;

    private void createFile()
    {
        // Delete the test file.
        cmdRun("DEL OBJLNK('"+PATH+"')", "CPFA0A9");
        // Delete the test directory.
        cmdRun("RMDIR DIR('"+DIR_PATH+"')", "CPFA0A9");
        // Create the test directory.
        cmdRun("CRTDIR DIR('"+DIR_PATH+"')");
        // Create the test file.
        cmdRun("QSH CMD('touch "+PATH+"')");
    }

    private void deleteFile()
    {
        // Delete the test file.
        cmdRun("DEL OBJLNK('"+PATH+"')");
        // Delete the test directory.
        cmdRun("RMDIR DIR('"+DIR_PATH+"')");
    }

    private void checkOutTestFile()
    {
        // Create the file
        createFile(); 
        // Check out the test file
        cmdRun("CHKOUT OBJ('"+PATH+"')");
    }

    private void checkInTestFile()
    {
        // Check in the test file
	cmdRun("CHKIN OBJ('"+PATH+"')");

	//Remove the file 
	deleteFile();
    }

    private void setupTestFile()
    {  

        // Create the file 
	createFile();

	// Open the files 
        file = new IFSFile(systemObject_, PATH);
	try 
	{ 
	    // Acquire the CCSID to pass to the IFSFileReader
	    int CCSID = file.getCCSID();
            // Create a buffed reader 
	    reader = new BufferedReader(new IFSFileReader(file, CCSID, IFSFileInputStream.SHARE_ALL)); 
	    // Read the first line 
            String line1 = reader.readLine();
            // Display the String that was read.
            //System.out.println(line1);
	}
	catch ( Exception e ) { System.out.println("Can not create BufferedReader."); }
    }

    private void cleanupTestFile()
    {
        // Close the files 
	try { reader.close(); }
	catch ( Exception e ) { System.out.println("Can not close the file."); }

	// Remove the file
	deleteFile();
    }


    public void setup() {


        DIR_PATH = "/"+ORTest.COLLECTION+".ortest";
	PATH = DIR_PATH +"/testfile";

	System.out.println("ORTestcase.setup called:  PATH="+PATH); 

    }


    /**
     <p>Test:  Call ObjectReferences :: ObjectReferences(AS400, String) passing valid parmeters.
     <p>Result:  Verify object is constructed without exception.
     **/
    public void Var001()
    {
        try
        {
            ObjectReferences or  = new ObjectReferences(systemObject_, "/path");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     <p>Test:  Call ObjectReferences :: ObjectReferences(AS400 system, String path)  passing null for system.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var002()
    {
        try
        {
            ObjectReferences or = new ObjectReferences(null, "/path");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     <p>Test:  Call ObjectReferences :: ObjectReferences(AS400, String ) passing null for path.
     <p>Result:  Verify a NullPointerException is thrown.
     **/
    public void Var003()
    {
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }
   
    /**
     <p>Test:  Call ObjectReferences ::ObjectReferences(AS400, String ) passing in valid arguments.
     <p>Result: Verify no exceptions occur for valid instantiation of the ObjectReferences object.
     **/
    public void Var004()
    {
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH );
	    succeeded();
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
    }
    
    public void Var005() { succeeded(); }
    
    public void Var006(){ succeeded(); }

    /**
     <p>Test:  Call ObjectReferences ::inInUseIndicator().
     <p>Result: Verify return value is true.
     **/
    public void Var007()
    {
        checkOutTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
	    boolean expected = true;
	    boolean returned = or.isInUseIndicator();
            assertCondition(returned == expected);
        }
        catch (Exception e)
        {
            failed("unexpected exception");
        }
        checkInTestFile();
    }

    /**
     <p>Test:  Call ObjectReferences :: getCheckedOut().
     <p>Result: Verify return value is 1.
     **/
    public void Var008()
    {
        checkOutTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
	    long expected = 1; 
	    long returned = or.getCheckedOut();
            assertCondition(returned == expected);
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        checkInTestFile();
    }

    /**
     <p>Test:  Call ObjectReferences ::getCheckedOutUserName.
     <p>Result: Verify return value is "JAVA".
     **/
    public void Var009()
    {   
        checkOutTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
	    String expected = pwrSys_.getUserId();
	    String returned = or.getCheckedOutUserName();
            assertCondition(returned.equals(expected));
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
	checkInTestFile();
    }

    /**
     <p>Test:  Call ObjectReferences :: getAttributeLock ().
     <p>Result: Verify return value is .
     **/
    public void Var010()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getAttributeLock();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getCurrentDirectory () .
     <p>Result:  Verify return value is.
     **/
    public void Var011()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getCurrentDirectory();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getExecute().
     <p>Result:  Verify return value is.
     **/
    public void Var012()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecute();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getExecuteShareWithReadersOnly() .
     <p>Result:  Verify return value is.
     **/
    public void Var013()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteShareWithReadersOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences ::getExecuteShareWithReadersAndWriters().
     <p>Result:  Verify return value is.
     **/
    public void Var014()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteShareWithReadersAndWriters(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReference :: getExecuteShareWithWritersOnly().
     <p>Result:  Verify return value is.
     **/
    public void Var015()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteShareWithWritersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences:: getExecuteShareWithNeitherReadersNorWriters ().
     <p>Result:  Verify return value is.
     **/
    public void Var016()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteShareWithNeitherReadersNorWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getExecuteReadShareWithReadersOnly() .
     <p>Result:  Verify return value is.
     **/
    public void Var017()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteReadShareWithReadersOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getExecuteReadShareWithReadersAndWriters() .
     <p>Result:  Verify return value is.
     **/
    public void Var018()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteReadShareWithReadersAndWriters(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReference :: getExecuteReadShareWithWritersOnly() .
     <p>Result:  Verify return value is.
     **/
    public void Var019()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteReadShareWithWritersOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getExecuteReadShareWithNeitherReadersNorWriters().
     <p>Result:  Verify return value is.
     **/
    public void Var020()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getExecuteReadShareWithNeitherReadersNorWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getFileServerReference().
     <p>Result:  Verify return value is.
     **/
    public void Var021()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 1;
            long returned = or.getFileServerReference();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getFileServerReference ().
     <p>Result:  Verify return value is.
     **/
    public void Var022()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 1;
            long returned = or.getFileServerReference();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getFileServerWorkingDirectory () .
     <p>Result:  Verify return value is.
     **/
    public void Var023()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getFileServerWorkingDirectory();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getInternalSaveLock ().
     <p>Result:  Verify return value is.
     **/
    public void Var024()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getInternalSaveLock(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences getLinkChangesLock () .
     <p>Result:  Verify return value is.
     **/
    public void Var025()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getLinkChangesLock(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadOnly () .
     <p>Result:  Verify return value is.
     **/
    public void Var026()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 1;
            long returned = or.getReadOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadOnlyShareWithReadersOnly () .
     <p>Result:  Verify return value is.
     **/
    public void Var027()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadOnlyShareWithReadersOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences ::getReadOnlyShareWithReadersAndWriters().
     <p>Result:  Verify return value is.
     **/
    public void Var028()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadOnlyShareWithReadersAndWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadOnlyShareWithWritersOnly().
     <p>Result:  Verify return value is.
     **/
    public void Var029()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadOnlyShareWithWritersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadOnlyShareWithNeitherReadersNorWriters () .
     <p>Result:  Verify return value is.
     **/
    public void Var030()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadOnlyShareWithNeitherReadersNorWriters(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadWrite ().
     <p>Result:  Verify return value is.
     **/
    public void Var031()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadWrite();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadWriteShareWithReadersOnly ().
     <p>Result:  Verify return value is.
     **/
    public void Var032()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadWriteShareWithReadersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadWriteShareWithReadersAndWriters().
     <p>Result:  Verify return value is.
     **/
    public void Var033()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadWriteShareWithReadersAndWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadWriteShareWithWritersOnly ().
     <p>Result:  Verify return value is.
     **/
    public void Var034()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadWriteShareWithWritersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReadWriteShareWithNeitherReadersNorWriters () .
     <p>Result:  Verify return value is.
     **/
    public void Var035()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getReadWriteShareWithNeitherReadersNorWriters(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getReferenceCount().
     <p>Result:  Verify return value is.
     **/
    public void Var036()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 1;
            long returned = or.getReferenceCount();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getRootDirectory().
     <p>Result:  Verify return value is.
     **/
    public void Var037()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getRootDirectory();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getShareWithReadersOnly ().
     <p>Result:  Verify return value is.
     **/
    public void Var038()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getShareWithReadersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getSaveLock () .
     <p>Result:  Verify return value is.
     **/
    public void Var039()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getSaveLock(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getShareWithReadersAndWriters ().
     <p>Result:  Verify return value is.
     **/
    public void Var040()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 1;
            long returned = or.getShareWithReadersAndWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getShareWithWritersOnly().
     <p>Result:  Verify return value is.
     **/
    public void Var041()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getShareWithWritersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getShareWithNeitherReadersNorWriters().
     <p>Result:  Verify return value is.
     **/
    public void Var042()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getShareWithNeitherReadersNorWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getWriteOnly () .
     <p>Result:  Verify return value is.
     **/
    public void Var043()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getWriteOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    /**
     <p>Test:  Call ObjectReferences :: getWriteOnlyShareWithReadersOnly().
     <p>Result:  Verify return value is.
     **/
    public void Var044()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getWriteOnlyShareWithReadersOnly();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
     /**
     <p>Test:  Call ObjectReferences :: getWriteOnlyShareWithReadersAndWriters().
     <p>Result:  Verify return value is.
     **/
    public void Var045()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getWriteOnlyShareWithReadersAndWriters();
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
     /**
     <p>Test:  Call ObjectReferences :: getWriteOnlyShareWithWritersOnly().
     <p>Result:  Verify return value is.
     **/
    public void Var046()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            long expected = 0;
            long returned = or.getWriteOnlyShareWithWritersOnly(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
     /**
     <p>Test:  Call ObjectReferences :: getWriteOnlyShareWithNeitherReadersNorWriters () .
     <p>Result:  Verify return value is.
     **/
    public void Var047()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences( systemObject_, PATH );
            long expected = 0;
            long returned = or.getWriteOnlyShareWithNeitherReadersNorWriters(); 
            assertCondition( expected == returned );
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }





     /**
     <p>Test:  Call ObjectReferences :: refresh () .
     <p>Result:  Verify there are no exception thrown.
     **/
    public void Var048()
    {
        setupTestFile();
        try
        {
            ObjectReferences or = new ObjectReferences(systemObject_, PATH);
            or.refresh();
	    succeeded();
        }
        catch (Exception e)
        {
            failed("unexpeceted exception.");
        }
        cleanupTestFile();
    }
    

    public void Var049() { succeeded(); }


  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure ::getJobNumber().
   <p>Result: Verify return value is true.
   **/
  public void Var050()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          String returned = jobs[0].getJobNumber();

	  if ( returned == null )
              failed("Job number is null");
	  else 
	      succeeded();
      }
      catch (Exception e)
      {
          failed("unexpected exception");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure ::getJobUser().
   <p>Result: Verify return value is true.
   **/
  public void Var051()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          String expected = "QUSER";
  
          String returned = jobs[0].getJobUser();
          assertCondition(returned.equals(expected));
      }
      catch (Exception e)
      {
          failed("unexpected exception");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure ::getJobName().
   <p>Result: Verify return value is true.
   **/
  public void Var052()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          String expected = "QPWFSERVSO";
  
          String returned = jobs[0].getJobName();
          assertCondition(returned.equals(expected));
      }
      catch (Exception e)
      {
          failed("unexpected exception");
      }
      cleanupTestFile();
  }
  
  
  public void Var053() { succeeded(); }
  
  
  
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getCheckedOut().
   <p>Result: Verify return value is 1.
   **/
  public void Var054()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0; 
          long returned = jobs[0].getCheckedOut();
          assertCondition(returned == expected);
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getCheckedOutUserName.
   <p>Result: Verify return value is "JAVA".
   **/
  public void Var055()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          String expected = "";
          String returned = jobs[0].getCheckedOutUserName();
          assertCondition(returned.equals(expected));
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getAttributeLock ().
   <p>Result: Verify return value is .
   **/
  public void Var056()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getAttributeLock();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getCurrentDirectory () .
   <p>Result:  Verify return value is.
   **/
  public void Var057()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getCurrentDirectory();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getExecute().
   <p>Result:  Verify return value is.
   **/
  public void Var058()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecute();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getExecuteShareWithReadersOnly() .
   <p>Result:  Verify return value is.
   **/
  public void Var059()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteShareWithReadersOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure ::getExecuteShareWithReadersAndWriters().
   <p>Result:  Verify return value is.
   **/
  public void Var060()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteShareWithReadersAndWriters(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReference.JobUsingObjectStructure :: getExecuteShareWithWritersOnly().
   <p>Result:  Verify return value is.
   **/
  public void Var061()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteShareWithWritersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences:: getExecuteShareWithNeitherReadersNorWriters ().
   <p>Result:  Verify return value is.
   **/
  public void Var062()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteShareWithNeitherReadersNorWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getExecuteReadShareWithReadersOnly() .
   <p>Result:  Verify return value is.
   **/
  public void Var063()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteReadShareWithReadersOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getExecuteReadShareWithReadersAndWriters() .
   <p>Result:  Verify return value is.
   **/
  public void Var064()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteReadShareWithReadersAndWriters(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReference.JobUsingObjectStructure :: getExecuteReadShareWithWritersOnly() .
   <p>Result:  Verify return value is.
   **/
  public void Var065()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteReadShareWithWritersOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getExecuteReadShareWithNeitherReadersNorWriters().
   <p>Result:  Verify return value is.
   **/
  public void Var066()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getExecuteReadShareWithNeitherReadersNorWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getFileServerReference().
   <p>Result:  Verify return value is.
   **/
  public void Var067()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 1;
          long returned = jobs[0].getFileServerReference();
          assertCondition( expected == returned , "expected="+expected+" != returned="+returned);
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }

  public void Var068() { succeeded(); }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getFileServerWorkingDirectory () .
   <p>Result:  Verify return value is.
   **/
  public void Var069()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getFileServerWorkingDirectory();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getInternalSaveLock ().
   <p>Result:  Verify return value is.
   **/
  public void Var070()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getInternalSaveLock(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences getLinkChangesLock () .
   <p>Result:  Verify return value is.
   **/
  public void Var071()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getLinkChangesLock(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadOnly () .
   <p>Result:  Verify return value is.
   **/
  public void Var072()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadOnlyShareWithReadersOnly () .
   <p>Result:  Verify return value is.
   **/
  public void Var073()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadOnlyShareWithReadersOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure ::getReadOnlyShareWithReadersAndWriters().
   <p>Result:  Verify return value is.
   **/
  public void Var074()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadOnlyShareWithReadersAndWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadOnlyShareWithWritersOnly().
   <p>Result:  Verify return value is.
   **/
  public void Var075()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadOnlyShareWithWritersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadOnlyShareWithNeitherReadersNorWriters () .
   <p>Result:  Verify return value is.
   **/
  public void Var076()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadOnlyShareWithNeitherReadersNorWriters(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadWrite ().
   <p>Result:  Verify return value is.
   **/
  public void Var077()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadWrite();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadWriteShareWithReadersOnly ().
   <p>Result:  Verify return value is.
   **/
  public void Var078()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadWriteShareWithReadersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadWriteShareWithReadersAndWriters().
   <p>Result:  Verify return value is.
   **/
  public void Var079()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadWriteShareWithReadersAndWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadWriteShareWithWritersOnly ().
   <p>Result:  Verify return value is.
   **/
  public void Var080()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadWriteShareWithWritersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getReadWriteShareWithNeitherReadersNorWriters () .
   <p>Result:  Verify return value is.
   **/
  public void Var081()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getReadWriteShareWithNeitherReadersNorWriters(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  
  
  public void Var082() { succeeded(); }
  
  
  
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getRootDirectory().
   <p>Result:  Verify return value is.
   **/
  public void Var083()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getRootDirectory();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getShareWithReadersOnly ().
   <p>Result:  Verify return value is.
   **/
  public void Var084()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getShareWithReadersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getSaveLock () .
   <p>Result:  Verify return value is.
   **/
  public void Var085()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getSaveLock(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getShareWithReadersAndWriters ().
   <p>Result:  Verify return value is.
   **/
  public void Var086()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getShareWithReadersAndWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getShareWithWritersOnly().
   <p>Result:  Verify return value is.
   **/
  public void Var087()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getShareWithWritersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getShareWithNeitherReadersNorWriters().
   <p>Result:  Verify return value is.
   **/
  public void Var088()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getShareWithNeitherReadersNorWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getWriteOnly () .
   <p>Result:  Verify return value is.
   **/
  public void Var089()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getWriteOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
  /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getWriteOnlyShareWithReadersOnly().
   <p>Result:  Verify return value is.
   **/
  public void Var090()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getWriteOnlyShareWithReadersOnly();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getWriteOnlyShareWithReadersAndWriters().
   <p>Result:  Verify return value is.
   **/
  public void Var091()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getWriteOnlyShareWithReadersAndWriters();
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getWriteOnlyShareWithWritersOnly().
   <p>Result:  Verify return value is.
   **/
  public void Var092()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences(systemObject_, PATH);
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getWriteOnlyShareWithWritersOnly(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.JobUsingObjectStructure :: getWriteOnlyShareWithNeitherReadersNorWriters () .
   <p>Result:  Verify return value is.
   **/
  public void Var093()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences( systemObject_, PATH );
  	    ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          long expected = 0;
          long returned = jobs[0].getWriteOnlyShareWithNeitherReadersNorWriters(); 
          assertCondition( expected == returned );
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.SessionUsingObjectStructure :: new SessionUsingObjectStructure[]
   <p>Result:  Verify that we get a new object.
   **/
  public void Var094()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences( systemObject_, PATH );
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          ObjectReferences.SessionUsingObjectStructure[] sessions = jobs[0].getSessionUsingObjectStructures();
          succeeded();
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.SessionUsingObjectStructure :: getSessionIdentifier()
   <p>Result:  Verify return value is.
   **/
  public void Var095()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences( systemObject_, PATH );
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          ObjectReferences.SessionUsingObjectStructure[] sessions = jobs[0].getSessionUsingObjectStructures();
          if (sessions.length == 0 )
  	      succeeded();
  	  else 
              failed("returned value is not set for getSessionIdentifier()");
          
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.SessionUsingObjectStructure :: getUserName()
   <p>Result:  Verify return value is.
   **/
  public void Var096()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences( systemObject_, PATH );
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          ObjectReferences.SessionUsingObjectStructure[] sessions = jobs[0].getSessionUsingObjectStructures();
          if (sessions.length == 0 )
  	      succeeded();
  	  else 
              failed("Unexpected Exception.");
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.SessionUsingObjectStructure :: getWorkstationAddress()
   <p>Result:  Verify return value is.
   **/
  public void Var097()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences( systemObject_, PATH );
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
	  ObjectReferences.SessionUsingObjectStructure[] sessions = jobs[0].getSessionUsingObjectStructures();
          if (sessions.length == 0 )
  	      succeeded();
  	  else 
              failed("Unexpected exception.");
      }
      catch (Exception e)
      {
          failed("Unexpeceted exception.");
      }
      cleanupTestFile();
  }
   /**
   <p>Test:  Call ObjectReferences.SessionUsingObjectStructure :: getWorkstationName()
   <p>Result:  Verify return value is.
   **/
  public void Var098()
  {
      setupTestFile();
      try
      {
          ObjectReferences or = new ObjectReferences( systemObject_, PATH );
  	  ObjectReferences.JobUsingObjectStructure[] jobs = or.getJobUsingObjectStructures();
          ObjectReferences.SessionUsingObjectStructure[] sessions = jobs[0].getSessionUsingObjectStructures();
          if (sessions.length == 0 )
  	      succeeded();
  	  else 
            failed("Unexpected Exception.");
      }
      catch (Exception e)
      {
          failed("unexpeceted exception.");
      }
      cleanupTestFile();
  }
}
