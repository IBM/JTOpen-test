///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.RF.RFEvents;
import test.RF.RFMisc;
import test.RF.RFNewRecord;
import test.RF.RFRecord;
import test.RF.RFRecordMisc;
import test.RF.RFSerialization;

/**
Test driver for the record format component. The record format component
includes the RecordFormat and Record classes.
The following testcases can be run:
<ul compact>
<li>RFEvents
<br>
This test class verifies valid and invalid
usage of:
<ul compact>
<li>RecordFormat.addRecordDescriptionListener()
<li>RecordFormat.addPropertyChangeListener()
<li>RecordFormat.addVetoableChangeListener()
<li>RecordFormat.removeRecordDescriptionListener()
<li>RecordFormat.removePropertyChangeListener()
<li>RecordFormat.removeVetoableChangeListener()
<li>Record.addRecordDescriptionListener()
<li>Record.addPropertyChangeListener()
<li>Record.addVetoableChangeListener()
<li>Record.removeRecordDescriptionListener()
<li>Record.removePropertyChangeListener()
<li>Record.removeVetoableChangeListener()
</ul>
This test class also verifies that the following events are fired
from the specified methods:
<ul compact>
<li>RecordFormat.setName() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>RecordFormat.addFieldDescription() - RecordDescriptionEvent.fieldDescriptionAdded()
<li>RecordFormat.addKeyFieldDescription() - RecordDescriptionEvent.keyFieldDescriptionAdded()
<li>Record.setRecordNumber() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>Record.setRecordFormat() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>Record.setRecordName() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>Record.setRecordNumber() - PropertyChangeEvent, VetoablePropertyChangeEvent
<li>Record.getField() - RecordDescriptionEvent.fieldModified()
<li>Record.setContents() - RecordDescriptionEvent.fieldModified()
<li>Record.setField() - RecordDescriptionEvent.fieldModified()
<li>Record.setRecordFormat() - RecordDescriptionEvent.fieldModified()
</ul>
<li>RFMisc
<br>
This test class verifes valid and invalid usage of
the RecordFormat constructors and methods with the exception of the
getNewRecord() methods.  See RFNewRecord for testing of the getNewRecord()
methods.
<li>RFNewRecord
<br>
This test class verifies the valid and invalid usage
of the RecordFormat.getNewRecord() methods.  A very basic byte array is
used for verifying the contents of the record returned by the getNewRecord
methods.  See the RFRecord and RFRecordMisc testcases for a thorough testing
of a variety of records.  E.g. dependent fields, variable length fields and
usage of all possible field description types.
<li>RFRecord
<br>
This test class verifes valid and invalid usage of
the Record constructors.
<li>RFRecordMisc
<br>
This test class verifes valid and invalid usage of
all methods in Record (with the exception of the constructors which are
tested in RFRecord.java).
<li>RFSerialization
<br>
This test class verifies the abillity to
serialize and deserialize RecordFormat and Record objects.
</ul>
**/
public class RFTest extends TestDriver
{

/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      RFTest rf = new RFTest(args);
      rf.init();
      rf.start();
      rf.stop();
      rf.destroy();

         System.exit(0);
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
  public RFTest()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public RFTest(String[] args)
       throws Exception
  {
    super(args);
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
  	     	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
  	     	    	System.in.read ();
  	     	 } 
  	     	 catch (Exception exc) {};   	   
	} 
	   
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);

    // $$$ TO DO $$$
    // Repeat the following 'if' block for each testcase.
    // Replace the RFMisc with the name of your testcase.
    if (allTestcases || namesAndVars_.containsKey("RFMisc"))
    {
      RFMisc tc =
        new RFMisc(systemObject_,
                     namesAndVars_.get("RFMisc"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("RFMisc");
    }

    if (allTestcases || namesAndVars_.containsKey("RFNewRecord"))
    {
      RFNewRecord tc =
        new RFNewRecord(systemObject_,
                     namesAndVars_.get("RFNewRecord"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("RFNewRecord");
    }

    if (allTestcases || namesAndVars_.containsKey("RFRecord"))
    {
      RFRecord tc =
        new RFRecord(systemObject_,
                     namesAndVars_.get("RFRecord"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("RFRecord");
    }

    if (allTestcases || namesAndVars_.containsKey("RFRecordMisc"))
    {
      RFRecordMisc tc =
        new RFRecordMisc(systemObject_,
                     namesAndVars_.get("RFRecordMisc"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("RFRecordMisc");
    }

    if (allTestcases || namesAndVars_.containsKey("RFSerialization"))
    {
      RFSerialization tc =
        new RFSerialization(systemObject_, 
                     namesAndVars_.get("RFSerialization"), runMode_,
                     fileOutputStream_, password_);
      testcases_.addElement(tc);
      namesAndVars_.remove("RFSerialization");
    }
    if (allTestcases || namesAndVars_.containsKey("RFEvents"))
    {
      RFEvents tc =
        new RFEvents(systemObject_,
                     namesAndVars_.get("RFEvents"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("RFEvents");
    }

    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }
}
