///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMTest.java
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
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;

import test.DDM.DDMCaching;
import test.DDM.DDMCheckFields;
import test.DDM.DDMCommitmentControl;
import test.DDM.DDMConnect;
import test.DDM.DDMConstructors;
import test.DDM.DDMCreateAndAdd;
import test.DDM.DDMDelete;
import test.DDM.DDMDeletedRecords;
import test.DDM.DDMEvents;
import test.DDM.DDMGetSet;
import test.DDM.DDMLocking;
import test.DDM.DDMMemberList;
import test.DDM.DDMMultipleFormat;
import test.DDM.DDMOpenClose;
import test.DDM.DDMP3666842;
import test.DDM.DDMP3696575;
import test.DDM.DDMP9901531;
import test.DDM.DDMP9907036;
import test.DDM.DDMP9908190;
import test.DDM.DDMP9936798;
import test.DDM.DDMP9946152;
import test.DDM.DDMP9949891;
import test.DDM.DDMP9960329;
import test.DDM.DDMPSA94749;
import test.DDM.DDMPassword;
import test.DDM.DDMPosition;
import test.DDM.DDMPositionExtended;
import test.DDM.DDMReadKey;
import test.DDM.DDMReadRN;
import test.DDM.DDMReadSeq;
import test.DDM.DDMRecordDescription;
import test.DDM.DDMRegressionTestcase;
import test.DDM.DDMSQLCompatibility;
import test.DDM.DDMSerialization;
import test.DDM.DDMTranslation;
import test.DDM.DDMUpdate;
import test.DDM.DDMWrite;

import java.io.IOException;
import java.util.Enumeration;

/**
Test driver for the ddm (record-level database access) component.
The following testcases can be run:
<ul compact>
<li>DDMCaching
<br>
This test class verifies the results of reading and
positioning when caching is in effect.  The following methods are verified:
<ul>
<li>AS400File.positionCursorAfterLast()
<li>AS400File.positionCursorBeforeFirst()
<li>AS400File.positionCursorToFirst()
<li>AS400File.positionCursorToLast()
<li>AS400File.positionCursorToNext()
<li>AS400File.positionCursorToPrevious()
<li>AS400File.read()
<li>AS400File.readFirst()
<li>AS400File.readLast()
<li>AS400File.readNext()
<li>AS400File.readPrevious()
<li>AS400File.readFirst()
<li>SequentialFile.positionCursor(int)
<li>SequentialFile.positionCursorAfter(int)
<li>SequentialFile.positionCursorBefore(int)
<li>SequentialFile.read(int)
<li>SequentialFile.readAfter(int)
<li>SequentialFile.readBefore(int)
<li>KeyedFile.positionCursor(Object[], int)
<li>KeyedFile.positionCursorAfter(Object[], int)
<li>KeyedFile.positionCursorBefore(Object[], int)
<li>KeyedFile.read(Object[], int)
<li>KeyedFile.readAfter(Object[])
<li>KeyedFile.readBefore(Object[])
<li>KeyedFile.readNextEqual()
<li>KeyedFile.readPreviousEqual()
<li>KeyedFile.readNextEqual(Object[])
<li>KeyedFile.readPreviousEqual(Object[])
</ul>
<li>DDMCommitmentControl
<br>
This test class verifies valid and invalid usage of:
<ul compact>
<li>startCommitmentControl()
<li>endCommitmentControl()
<li>isCommitmentControlStarted()
<li>getCommitLockLevel()
<li>commit()
<li>rollback()
</ul>
<li>DDMConstructors
<br>
Verify valid and invalid usages of the constructors
for KeyedFile and SequentialFile.  The ability to specify special values for
the library and member is tested in under the method to which they apply.
E.g. we would verify the ability to open a file which had %LIBL% specified
on the constructor in the DDMOpenClose testcase.
<li>DDMCreateAndAdd
<br>
This test class verifies valid and invalid usage of
the create() methods and the addPhysicalFileMember() method.
<li>DDMDelete
<br>
This test class verifies valid and invalid usage of
the delete(), deleteMember() and deleteCurrentRecord() methods.  This
test class also verifies valid and invalid usage of
SequentialFile.delete(recordNumber)
and KeyedFile.delete(key).
<li>DDMDeletedRecords
<br>
This test class verifies reading records
from a file that contains deleted records.
<li>DDMEvents
<br>
This test class verifies valid and invalid
usage of:
<ul compact>
<li>AS400File.addRecordDescriptionListener()
<li>AS400File.addPropertyChangeListener()
<li>AS400File.addVetoableChangeListener()
<li>AS400File.removeRecordDescriptionListener()
<li>AS400File.removePropertyChangeListener()
<li>AS400File.removeVetoableChangeListener()
</ul>
This test class also verifies that the following events are fired
from the specified methods:
<ul compact>
<li>AS400File.setPath() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>AS400File.setRecordFormat() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>AS400File.setSystem() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>AS400File.close() - FileEvent.fileClosed()
<li>AS400File.create() - FileEvent.fileCreated()
<li>AS400File.delete() - FileEvent.fileDeleted()
<li>AS400File.deleteCurrentRecord() - FileEvent.fileModified()
<li>AS400File.update() - FileEvent.fileModified()
<li>AS400File.write() - FileEvent.fileModified()
<li>KeyedFile.deleteRecord() - FileEvent.fileModified()
<li>SequentialFile.deleteRecord() - FileEvent.fileModified()
<li>KeyedFile.open() - FileEvent.fileOpened()
<li>SequentialFile.open() - FileEvent.fileOpened()
<li>KeyedFile.update() - FileEvent.fileModified()
<li>SequentialFile.update() - FileEvent.fileModified()
</ul>
<li>DDMGetSet
<br>
Testcase DDMGetSet.  This test class verifies valid and invalid usage of
the AS400File getter and setter methods:
<ul compact>
<li>getBlockingFactor()
<li>getCommitLockLevel()
<li>getExplicitLocks()
<li>getFileName()
<li>getMemberName()
<li>getPath()
<li>getRecordFormat()
<li>getSystem()
<li>isCommitmentControlStarted()
<li>isOpen()
<li>isReadOnly()
<li>isReadWrite()
<li>isWriteOnly()
<li>setPath(String)
<li>setRecordFormat(RecordFormat)
<li>setSystem(AS400)
</ul>
<li>DDMLocking
<br>
This test class verifies valid and invalid usage of:
<ul compact>
<li>lock()
<li>getExplicitLocks()
<li>releaseExplicitLocks()
</ul>
<li>DDMMultipleFormat
<br>
This test class verifies valid and invalid usage of multiple format logical files.
<li>DDMOpenClose
<br>
Verify valid and invalid usages of open and close
for KeyedFile and SequentialFile.
<li>DDMPosition
<br>
Verify valid and invalid usages of the positionCursor
methods for AS400File, KeyedFile and SequentialFile with a blocking
factor of 1.
<ul compact>
<li>AS400File.positionCursorAfterLast()
<li>AS400File.positionCursorBeforeFirst()
<li>AS400File.positionCursorToFirst()
<li>AS400File.positionCursorToLast()
<li>AS400File.positionCursorToNext()
<li>AS400File.positionCursorToPrevious()
<li>KeyedFile.positionCursor(key)
<li>KeyedFile.positionCursor(key, searchType)
<li>KeyedFile.positionCursorAfter(key)
<li>KeyedFile.positionCursorBefore(key)
<li>SequentialFile.positionCursor(recordNumber)
<li>SequentialFile.positionCursorAfter(recordNumber)
<li>SequentialFile.positionCursorBefore(recordNumber)
</ul>
<li>DDMPositionCaching0
<br>
Verify valid and invalid usages of the positionCursor
methods for AS400File, KeyedFile and SequentialFile with a blocking
factor of 0 (calculate blocking factor for me).
<ul compact>
<li>AS400File.positionCursorAfterLast()
<li>AS400File.positionCursorBeforeFirst()
<li>AS400File.positionCursorToFirst()
<li>AS400File.positionCursorToLast()
<li>AS400File.positionCursorToNext()
<li>AS400File.positionCursorToPrevious()
<li>KeyedFile.positionCursor(key)
<li>KeyedFile.positionCursor(key, searchType)
<li>KeyedFile.positionCursorAfter(key)
<li>KeyedFile.positionCursorBefore(key)
<li>SequentialFile.positionCursor(recordNumber)
<li>SequentialFile.positionCursorAfter(recordNumber)
<li>SequentialFile.positionCursorBefore(recordNumber)
</ul>
<li>DDMPositionCaching2
<br>
Verify valid and invalid usages of the positionCursor
methods for AS400File, KeyedFile and SequentialFile with a blocking
factor of 2.
<ul compact>
<li>AS400File.positionCursorAfterLast()
<li>AS400File.positionCursorBeforeFirst()
<li>AS400File.positionCursorToFirst()
<li>AS400File.positionCursorToLast()
<li>AS400File.positionCursorToNext()
<li>AS400File.positionCursorToPrevious()
<li>KeyedFile.positionCursor(key)
<li>KeyedFile.positionCursor(key, searchType)
<li>KeyedFile.positionCursorAfter(key)
<li>KeyedFile.positionCursorBefore(key)
<li>SequentialFile.positionCursor(recordNumber)
<li>SequentialFile.positionCursorAfter(recordNumber)
<li>SequentialFile.positionCursorBefore(recordNumber)
</ul>
<li>DDMPositionCaching10
<br>
Verify valid and invalid usages of the positionCursor
methods for AS400File, KeyedFile and SequentialFile with a blocking
factor of 10.
<ul compact>
<li>AS400File.positionCursorAfterLast()
<li>AS400File.positionCursorBeforeFirst()
<li>AS400File.positionCursorToFirst()
<li>AS400File.positionCursorToLast()
<li>AS400File.positionCursorToNext()
<li>AS400File.positionCursorToPrevious()
<li>KeyedFile.positionCursor(key)
<li>KeyedFile.positionCursor(key, searchType)
<li>KeyedFile.positionCursorAfter(key)
<li>KeyedFile.positionCursorBefore(key)
<li>SequentialFile.positionCursor(recordNumber)
<li>SequentialFile.positionCursorAfter(recordNumber)
<li>SequentialFile.positionCursorBefore(recordNumber)
</ul>
<li>DDMReadKey
<br>
This test class verifies valid and invalid usage of
the KeyedFile read methods with a blocking factor of 1:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
<li>readNextEqual()
<li>readPreviousEqual()
<li>readNextEqual(key)
<li>readPreviousEqual(key)
</ul>
<br>
This test class verifies valid and invalid usage of
the KeyedFile read methods with a blocking factor of 1:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
<li>readNextEqual()
<li>readPreviousEqual()
<li>readNextEqual(key)
<li>readPreviousEqual(key)
</ul>
<li>DDMReadKeyCaching0
<br>
This test class verifies valid and invalid usage of
the KeyedFile read methods with a blocking factor of 0:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
<li>readNextEqual()
<li>readPreviousEqual()
<li>readNextEqual(key)
<li>readPreviousEqual(key)
</ul>
<li>DDMReadKeyCaching2
<br>
This test class verifies valid and invalid usage of
the KeyedFile read methods with a blocking factor of 2:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
<li>readNextEqual()
<li>readPreviousEqual()
<li>readNextEqual(key)
<li>readPreviousEqual(key)
</ul>
<li>DDMReadKeyCaching10
<br>
This test class verifies valid and invalid usage of
the KeyedFile read methods with a blocking factor of 10:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
<li>readNextEqual()
<li>readPreviousEqual()
<li>readNextEqual(key)
<li>readPreviousEqual(key)
</ul>
<li>DDMReadRN
<br>
This test class verifies valid and invalid usage of
the SequentialFile read methods with a blocking factor of 1:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
</ul>
<li>DDMReadRNCaching0
<br>
This test class verifies valid and invalid usage of
the SequentialFile read methods with a blocking factor of 0:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
</ul>
<li>DDMReadRNCaching2
<br>
This test class verifies valid and invalid usage of
the SequentialFile read methods with a blocking factor of 2:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
</ul>
<li>DDMReadRNCaching10
<br>
This test class verifies valid and invalid usage of
the SequentialFile read methods with a blocking factor of 10:
<ul compact>
<li>read(key)
<li>read(key, searchType)
<li>readAfter(key)
<li>readBefore(key)
</ul>
<li>DDMReadSeq
<br>
This test class verifies valid and invalid usage of
the AS400File read methods with a blocking factor of 1:
<ul compact>
<li>read()
<li>readFirst()
<li>readNext()
<li>readPrevious()
<li>readLast()
</ul>
<li>DDMReadSeqCaching0
<br>
This test class verifies valid and invalid usage of
the AS400File read methods with a blocking factor of 0:
<ul compact>
<li>read()
<li>readFirst()
<li>readNext()
<li>readPrevious()
<li>readLast()
</ul>
<li>DDMReadSeqCaching2
<br>
This test class verifies valid and invalid usage of
the AS400File read methods with a blocking factor of 2:
<ul compact>
<li>read()
<li>readFirst()
<li>readNext()
<li>readPrevious()
<li>readLast()
</ul>
<li>DDMReadSeqCaching10
<br>
This test class verifies valid and invalid usage of
the AS400File read methods with a blocking factor of 10:
<ul compact>
<li>read()
<li>readFirst()
<li>readNext()
<li>readPrevious()
<li>readLast()
</ul>
<li>DDMRecordDescription
<br>
This test class verifies valid and
invalid usage of the public interface of the AS400FileRecordDescription
class.
<li>DDMSerialization
<br>
This test class verifies the ability to serialize and
deserialize AS400File, KeyedFile and SequentialFile objects.
<li>DDMUpdate
<br>
This test class verifies valid and invalid usage of
the AS400File, KeyedFile and SequentialFile update() methods:
<ul compact>
<li>AS400File.update(record)
<li>KeyedFile.update(key, record)
<li>KeyedFile.update(key, record, searchType)
<li>SequentialFile.update(recordNumber)
</ul>
<li>DDMWrite
<br>
This test class verifies valid and invalid usage of
the AS400File write methods with a blocking factor of 1:
<ul compact>
<li>write(Record)
<li>write(Record[])
</ul>
<li>DDMWriteCaching0
<br>
This test class verifies valid and invalid usage of
the AS400File write methods with a blocking factor of 0:
<ul compact>
<li>write(Record)
<li>write(Record[])
</ul>
<li>DDMWriteCaching2
<br>
This test class verifies valid and invalid usage of
the AS400File write methods with a blocking factor of 2:
<ul compact>
<li>write(Record)
<li>write(Record[])
</ul>
<li>DDMWriteCaching10
<br>
This test class verifies valid and invalid usage of
the AS400File write methods with a blocking factor of 10:
<ul compact>
<li>write(Record)
<li>write(Record[])
</ul>
<li>DDMP3666842
<br>
Verify fix for JACL P3666842: AS400FileRecordDescription ignoring
FLTPCN(*DOUBLE) when number of digits is less than 10.
<li>DDMP3696575
<br>
Verify fix for JACL P3696575: AS400FileRecordDescription not obtaining
correct ccsid for DBCS and other field descriptions.
<li>DDMP9901531
<br>
Verify fix for JACL P9901531: AS400FileImplRemote not wrapping correlation IDs
when 32767 datastreams have been used.
<li>DDMP9907036
<br>
Verify fix for JACL P9907036: AS400Text CCSID getting overwritten when used
with a RecordFormat; AS400File.read(key) not working the same as in 
previous releases.
<li>DDMP9908190
<br>
Verify fix for JACL P9908190: Member JT4FFD in use error while
retrieving record formats across multiple threads.
<li>DDMP9936798
<br>
Verify fix for JACL P9936798: SQLException - Internal driver error (CCSID xxx)
occurs when JDBC is used to retrieve fields tagged with CCSIDs greater than 32767.
<li>DDMP9946152
<br>
Verify fix for JACL P9946152: Creating a file fails when the system-wide default
for the RCDLEN parm on the CRTSRCPF command has been changed to be something 
other than 92.
<li>DDMP9949198
<br>
Verify fix for JACL P9949198: Multithreaded operations on AS400File can fail.
<li>DDMSQLCompatibility
<br>
This test class verifies proper record format and field description
translation when operating on a file that was created as an SQL table.
<li>DDMPassword
<br>
This test class verifies password behavior for a DDM connection which
depends on the release level of the AS/400.
**/
public class DDMTest extends TestDriver
{
  static AS400 PwrSys = null;

  public static  String COLLECTION     = "JDDDMTST";

/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      DDMTest ddm = new DDMTest(args);
      ddm.init();
      ddm.start();
      ddm.stop();
      ddm.destroy();
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

  }

/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public DDMTest()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public DDMTest(String[] args)
       throws Exception
  {
    super(args);
  }


/**
 * Performs setup for the testcase
 */ 
    public void setup() {
      if (testLib_ != null) { 
          COLLECTION = testLib_;
      }
    }


/**
Creates Testcase objects for all the testcases in this component.
**/
  public void createTestcases()
  {
	  
  		if(TestDriverStatic.pause_)
  		{ 
  			try 
  			{						
  				systemObject_.connectService(AS400.RECORDACCESS);
  			}
  			catch (AS400SecurityException e) 
  			{
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			} 
  			catch (IOException e) 
  			{
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
		 	 	   
  			try
  			{
  				Job[] jobs = systemObject_.getJobs(AS400.RECORDACCESS);
  				System.out.println("Host Server job(s): ");

  				for(int i = 0 ; i< jobs.length; i++)
  				{   	    	
  					System.out.println(jobs[i]);
  				}
   	    
  			}
  			catch(Exception exc){}
   	    
  			try 
  			{
  				System.out.println ("Press ENTER to continue.");
  				System.in.read ();
  			} 
  			catch (Exception exc) {};   	   
  		} 
  		
  		
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);
    // Determine what library to create files, etc. in
    if (testLib_ == null)
    {
      testLib_ = "DDMTEST";
    }
    
    
    // do some setup
    
    if (!(pwrSys_.getUserId()).equals(""))
    {
      char[] decryptedPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_); 
      PwrSys = new AS400( systemObject_.getSystemName(), pwrSysUserID_, decryptedPassword);
      PasswordVault.clearPassword(decryptedPassword);
   	
      try {
    	  
    	PwrSys.setGuiAvailable(false);
        if (allTestcases)
        {
          CommandCall cmd = new CommandCall(PwrSys);
	  String deleteResult = deleteLibrary(cmd, testLib_); 
          if (deleteResult != null  &&
              !deleteResult.equals("CPF2110"))
          {
            System.out.println( "Setup could not delete library " + testLib_ + ": "
                                + cmd.getMessageList()[0].getID() + " "
                                + cmd.getMessageList()[0].getText() );
            if (deleteResult.equals("CPF3202"))
            {
              System.out.println( "To delete DDMTEST/DDMLOCK, signon to the system and use the WRKOBJLCK command to end the jobs holding the locks." );
            }
          }
	  deleteResult  = deleteLibrary(cmd, "\"" + testLib_ +"\""); 
          if (deleteResult != null  &&
              !deleteResult.equals("CPF2110"))
          {
            System.out.println( "Setup could not delete library " + testLib_ + ": "
                                + cmd.getMessageList()[0].getID() + " "
                                + cmd.getMessageList()[0].getText() );
            if (deleteResult.equals("CPF3202"))
            {
              System.out.println( "To delete DDMTEST/DDMLOCK, signon to the system and use the WRKOBJLCK command to end the jobs holding the locks." );
            }
          }
          if (!cmd.run("QSYS/CRTLIB "+testLib_))
	  {
            System.out.println("Setup could not create library "+testLib_+": "+
                                cmd.getMessageList()[0].toString());
	  }

	  if (!cmdRun("QSYS/GRTOBJAUT OBJ("+testLib_+") OBJTYPE(*LIB) USER("+userId_+") AUT(*ALL)")) {
	      out_.println("GRTOBJAUT failed");
	  } 

        }
      }
      catch (Exception e)
      {
        System.out.println( "Setup failed " + e );
        e.printStackTrace();
      }
    }
    else
    {
        System.out.println("Warning: -pwrSys option not specified. Some " +
                           "variations may fail due to leftover objects.");
    }
    
    
    //Changed misc_ to pwrSys_ so all test drivers are consistent.
        
    /*if (misc_!=null)
    {
      StringTokenizer miscTok = new StringTokenizer(misc_, ",");
      String uid = miscTok.nextToken();
      String pwd = miscTok.nextToken();
      // Use SECOFR user profile, for setup, cleanup, etc.,
      // specified via -misc uid,pwd
      PwrSys = new AS400( systemObject_.getSystemName(), uid, pwd );
      try {
        PwrSys.setGuiAvailable(false);
        if (allTestcases)
        {
          CommandCall cmd = new CommandCall( PwrSys );
          if ((cmd.run( "QSYS/DLTxLIB " + testLib_ ) == false) &&
              !cmd.getMessageList()[0].getID().equals("CPF2110"))
          {
            System.out.println( "Setup could not delete library " + testLib_ + ": "
                                + cmd.getMessageList()[0].getID() + " "
                                + cmd.getMessageList()[0].getText() );
          }
          if ((cmd.run( "QSYS/DLTxLIB \"" + testLib_ + "\"" ) == false) &&
              !cmd.getMessageList()[0].getID().equals("CPF2110"))
          {
            System.out.println( "Setup could not delete library " + testLib_ + ": "
                                + cmd.getMessageList()[0].getID() + " "
                                + cmd.getMessageList()[0].getText() );
          }
          if (!cmd.run("QSYS/CRTLIB "+testLib_))
	  {
            System.out.println("Setup could not create library "+testLib_+": "+
                                cmd.getMessageList()[0].toString());
	  }
        }
      }
      catch (Exception e)
      {
        System.out.println( "Setup failed " + e );
        e.printStackTrace();
      }
    }
    else
    {
        System.out.println("Warning: -misc option not specified. Some " +
                           "variations may fail due to leftover objects.");
    }*/

    if (allTestcases || namesAndVars_.containsKey("DDMConstructors"))
    {
      DDMConstructors tc =
        new DDMConstructors(systemObject_,
                      namesAndVars_.get("DDMConstructors"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMConstructors");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMConnect"))
    {
      DDMConnect tc =
        new DDMConnect(systemObject_,
                      namesAndVars_.get("DDMConnect"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMConnect");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMOpenClose"))
    {
      DDMOpenClose tc =
        new DDMOpenClose(systemObject_,
                      namesAndVars_.get("DDMOpenClose"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMOpenClose");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCreateAndAdd"))
    {
      DDMCreateAndAdd tc =
        new DDMCreateAndAdd(systemObject_,
                      namesAndVars_.get("DDMCreateAndAdd"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCreateAndAdd");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMDelete"))
    {
      DDMDelete tc =
        new DDMDelete(systemObject_,
                      namesAndVars_.get("DDMDelete"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMDelete");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMLocking"))
    {
      DDMLocking tc =
        new DDMLocking(systemObject_,
                      namesAndVars_.get("DDMLocking"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMLocking");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCommitmentControl"))
    {
      DDMCommitmentControl tc =
        new DDMCommitmentControl(systemObject_,
                      namesAndVars_.get("DDMCommitmentControl"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCommitmentControl");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPosition"))
    {
      DDMPosition tc =
        new DDMPosition(systemObject_,
                      namesAndVars_.get("DDMPosition"), runMode_,
                     fileOutputStream_, testLib_,  1, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMPosition");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPositionExtended"))
    {
      DDMPositionExtended tc =
        new DDMPositionExtended(systemObject_,
                      namesAndVars_.get("DDMPositionExtended"), runMode_,
                     fileOutputStream_, testLib_, 0, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMPositionExtended");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPositionCaching0"))
    {
      DDMPosition tc =
        new DDMPosition(systemObject_,
                      namesAndVars_.get("DDMPositionCaching0"), runMode_,
                     fileOutputStream_, testLib_,  0, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMPositionCaching0");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPositionCaching2"))
    {
      DDMPosition tc =
        new DDMPosition(systemObject_,
                      namesAndVars_.get("DDMPositionCaching2"), runMode_,
                     fileOutputStream_, testLib_,  2, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMPositionCaching2");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPositionCaching10"))
    {
      DDMPosition tc =
        new DDMPosition(systemObject_,
                      namesAndVars_.get("DDMPositionCaching10"), runMode_,
                     fileOutputStream_, testLib_,  10, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMPositionCaching10");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadSeq"))
    {
      DDMReadSeq tc =
        new DDMReadSeq(systemObject_,
                      namesAndVars_.get("DDMReadSeq"), runMode_,
                     fileOutputStream_, testLib_,  1);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadSeq");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadSeqCaching0"))
    {
      DDMReadSeq tc =
        new DDMReadSeq(systemObject_,
                      namesAndVars_.get("DDMReadSeqCaching0"), runMode_,
                     fileOutputStream_, testLib_,  0);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadSeqCaching0");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadSeqCaching2"))
    {
      DDMReadSeq tc =
        new DDMReadSeq(systemObject_,
                      namesAndVars_.get("DDMReadSeqCaching2"), runMode_,
                     fileOutputStream_, testLib_,  2);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadSeqCaching2");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadSeqCaching10"))
    {
      DDMReadSeq tc =
        new DDMReadSeq(systemObject_,
                      namesAndVars_.get("DDMReadSeqCaching10"), runMode_,
                     fileOutputStream_, testLib_,  10);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadSeqCaching10");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadKey"))
    {
      DDMReadKey tc =
        new DDMReadKey(systemObject_,
                      namesAndVars_.get("DDMReadKey"), runMode_,
                     fileOutputStream_, testLib_,  1);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadKey");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadKeyCaching0"))
    {
      DDMReadKey tc =
        new DDMReadKey(systemObject_,
                      namesAndVars_.get("DDMReadKeyCaching0"), runMode_,
                     fileOutputStream_, testLib_,  0);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadKeyCaching0");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadKeyCaching2"))
    {
      DDMReadKey tc =
        new DDMReadKey(systemObject_,
                      namesAndVars_.get("DDMReadKeyCaching2"), runMode_,
                     fileOutputStream_, testLib_,  2);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadKeyCaching2");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadKeyCaching10"))
    {
      DDMReadKey tc =
        new DDMReadKey(systemObject_,
                      namesAndVars_.get("DDMReadKeyCaching10"), runMode_,
                     fileOutputStream_, testLib_,  10);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadKeyCaching10");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadRN"))
    {
      DDMReadRN tc =
        new DDMReadRN(systemObject_,
                      namesAndVars_.get("DDMReadRN"), runMode_,
                     fileOutputStream_, testLib_,  1);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadRN");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadRNCaching0"))
    {
      DDMReadRN tc =
        new DDMReadRN(systemObject_,
                      namesAndVars_.get("DDMReadRNCaching0"), runMode_,
                     fileOutputStream_, testLib_,  0);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadRNCaching0");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadRNCaching2"))
    {
      DDMReadRN tc =
        new DDMReadRN(systemObject_,
                      namesAndVars_.get("DDMReadRNCaching2"), runMode_,
                     fileOutputStream_, testLib_,  2);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadRNCaching2");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMReadRNCaching10"))
    {
      DDMReadRN tc =
        new DDMReadRN(systemObject_,
                      namesAndVars_.get("DDMReadRNCaching10"), runMode_,
                     fileOutputStream_, testLib_,  10);
      addTestcase(tc);
      namesAndVars_.remove("DDMReadRNCaching10");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMDeletedRecords"))
    {
      DDMDeletedRecords tc =
        new DDMDeletedRecords(systemObject_,
                      namesAndVars_.get("DDMDeletedRecords"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMDeletedRecords");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMWrite"))
    {
      DDMWrite tc =
        new DDMWrite(systemObject_,
                      namesAndVars_.get("DDMWrite"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMWrite");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMWriteCaching0"))
    {
      DDMWrite tc =
        new DDMWrite(systemObject_,
                      namesAndVars_.get("DDMWriteCaching0"), runMode_,
                     fileOutputStream_, testLib_,  0);
      addTestcase(tc);
      namesAndVars_.remove("DDMWriteCaching0");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMWriteCaching2"))
    {
      DDMWrite tc =
        new DDMWrite(systemObject_,
                      namesAndVars_.get("DDMWriteCaching2"), runMode_,
                     fileOutputStream_, testLib_,  2);
      addTestcase(tc);
      namesAndVars_.remove("DDMWriteCaching2");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMWriteCaching10"))
    {
      DDMWrite tc =
        new DDMWrite(systemObject_,
                      namesAndVars_.get("DDMWriteCaching10"), runMode_,
                     fileOutputStream_, testLib_,  10);
      addTestcase(tc);
      namesAndVars_.remove("DDMWriteCaching10");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMUpdate"))
    {
      DDMUpdate tc =
        new DDMUpdate(systemObject_,
                      namesAndVars_.get("DDMUpdate"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMUpdate");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMGetSet"))
    {
      DDMGetSet tc =
        new DDMGetSet(systemObject_,
                      namesAndVars_.get("DDMGetSet"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMGetSet");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMRecordDescription"))
    {
      DDMRecordDescription tc =
        new DDMRecordDescription(systemObject_,
                      namesAndVars_.get("DDMRecordDescription"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMRecordDescription");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMSerialization"))
    {
      DDMSerialization tc =
        new DDMSerialization(systemObject_,
                      namesAndVars_.get("DDMSerialization"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMSerialization");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMEvents"))
    {
      DDMEvents tc =
        new DDMEvents(systemObject_,
                      namesAndVars_.get("DDMEvents"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMEvents");
    }
    if (allTestcases || namesAndVars_.containsKey("DDMCaching"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching"), runMode_,
                     fileOutputStream_, testLib_,  1, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching0"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching0"), runMode_,
                     fileOutputStream_, testLib_,  0, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching0");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching2"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching2"), runMode_,
                     fileOutputStream_, testLib_,  2, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching2");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching100"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching100"), runMode_,
                     fileOutputStream_, testLib_,  100, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching100");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching499"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching499"), runMode_,
                     fileOutputStream_, testLib_,  499, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching499");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching500"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching500"), runMode_,
                     fileOutputStream_, testLib_,  500, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching500");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching501"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching501"), runMode_,
                     fileOutputStream_, testLib_,  501, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching501");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMCaching600"))
    {
      DDMCaching tc =
        new DDMCaching(systemObject_,
                      namesAndVars_.get("DDMCaching600"), runMode_,
                     fileOutputStream_, testLib_,  600, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCaching600");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMMultipleFormat"))
    {
      DDMMultipleFormat tc =
        new DDMMultipleFormat(systemObject_,
                      namesAndVars_.get("DDMMultipleFormat"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMMultipleFormat");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP3666842"))
    {
      DDMP3666842 tc =
        new DDMP3666842(systemObject_,
                      namesAndVars_.get("DDMP3666842"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMP3666842");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP3696575"))
    {
      DDMP3696575 tc =
        new DDMP3696575(systemObject_,
                      namesAndVars_.get("DDMP3696575"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMP3696575");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP9901531"))
    {
      DDMP9901531 tc =
        new DDMP9901531(systemObject_,
                      namesAndVars_.get("DDMP9901531"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9901531");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP9907036"))
    {
      DDMP9907036 tc =
        new DDMP9907036(systemObject_,
                      namesAndVars_.get("DDMP9907036"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9907036");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP9908190"))
    {
      DDMP9908190 tc =
        new DDMP9908190(systemObject_,
                      namesAndVars_.get("DDMP9908190"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9908190");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP9936798"))
    {
      DDMP9936798 tc =
        new DDMP9936798(systemObject_,
                      namesAndVars_.get("DDMP9936798"), runMode_,
                     fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9936798");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP9946152"))
    {
      DDMP9946152 tc =
        new DDMP9946152(systemObject_,
                      namesAndVars_.get("DDMP9946152"), runMode_,
                     fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9946152");
    }

    if ((allTestcases || namesAndVars_.containsKey("DDMP9949891")))
    {
      DDMP9949891 tc =
        new DDMP9949891(systemObject_,
                      namesAndVars_.get("DDMP9949891"), runMode_,
                     fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9949891");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPSA94749"))
    {
      DDMPSA94749 tc =
        new DDMPSA94749(systemObject_,
                      namesAndVars_.get("DDMPSA94749"), runMode_,
                     fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMPSA94749");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMSQLCompatibility"))
    {
      DDMSQLCompatibility tc =
        new DDMSQLCompatibility(systemObject_,
                      namesAndVars_.get("DDMSQLCompatibility"), runMode_,
                     fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMSQLCompatibility");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMPassword"))
    {
      DDMPassword tc =
        new DDMPassword(systemObject_,
                      namesAndVars_.get("DDMPassword"), runMode_,
                     fileOutputStream_, testLib_);
      addTestcase(tc);
      namesAndVars_.remove("DDMPassword");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMRegressionTestcase"))
    {
      DDMRegressionTestcase tc =
        new DDMRegressionTestcase(systemObject_,
                      namesAndVars_.get("DDMRegressionTestcase"), runMode_,
                     fileOutputStream_, testLib_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMRegressionTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("DDMP9960329"))
    {
      DDMP9960329 tc =
        new DDMP9960329(systemObject_,
                      namesAndVars_.get("DDMP9960329"), runMode_,
                     fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMP9960329");
    }
    
    if (allTestcases || namesAndVars_.containsKey("DDMCheckFields"))
    {
      DDMCheckFields tc =
          new DDMCheckFields(systemObject_,
                   namesAndVars_.get("DDMCheckFields"), runMode_,
                  fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMCheckFields");
    }
        
    if (allTestcases || namesAndVars_.containsKey("DDMMemberList"))
    {
      DDMMemberList tc =
          new DDMMemberList(systemObject_,
                  namesAndVars_, runMode_,
                  fileOutputStream_, testLib_, password_, PwrSys);
      addTestcase(tc);
      namesAndVars_.remove("DDMMemberList");
    }


    if (allTestcases || namesAndVars_.containsKey("DDMTranslation"))
    {
      DDMTranslation tc =
          new DDMTranslation(systemObject_,
                  namesAndVars_, runMode_,
                  fileOutputStream_, testLib_);
      tc.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, 
          onAS400_, namesAndVars_, runMode_, fileOutputStream_); 
      addTestcase(tc);
      namesAndVars_.remove("DDMTranslation");
    }



    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }  // createTestcases
}

