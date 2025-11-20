///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrintTest.java
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
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;

import test.NP.*;

/**
 * Test driver for the Print component.
 *
 * ASSUMPTIONS:
 *      <ul>
 *      <li> NPJAVA library exists on the AS/400 system.
 *      <LI> QWPTXT workstation customizing object exists on the AS/400 system.
 *      <li> Userid JAVATEAM with password JTEAM1 exists on the AS/400 system.
 *      <li> NPJAVA library contains the following AFP resources
 *              <ul>
 *              <li> C0D0GB10    *FNTRSC
 *              <li> C0D0GB12    *FNTRSC
 *              <li> C0D0GC15    *FNTRSC
 *              <li> C0D0GI12    *FNTRSC
 *              <li> C0D0GL10    *FNTRSC
 *              <li> C0D0GL12    *FNTRSC
 *              <li> QFCIOCA     *PAGSEG
 *              <li> QFCLOGO     *PAGSEG
 *              <li> QFCPAGS     *PAGSEG
 *              <li> F1A000E0    *FORMDF
 *              <li> F1A000M0    *FORMDF
 *              <li> F1A00010    *FORMDF
 *              <li> F1A00011    *FORMDF
 *              <li> F1A00012    *FORMDF
 *              <li> QOOD2L      *OVL
 *              <li> QOOD2P      *OVL
 *              <li> QOOD3L      *OVL
 *              <li> P1A06462    *PAGDFN
 *              <li> P1A08584    *PAGDFN
 *              <li> P1A08682    *PAGDFN
 *              <li> P1B0446B    *PAGDFN
 *              <li> P1B04963    *PAGDFN
 *              </ul>
 *      <li> A valid printer device description needs to be on the system and
 *           varied on, use the -misc printerName flag when involking NPPrintTest
 *      <li> QGPL library needed to be the default CURLIB, it is unless someone
 *           has changed the default job description on the system.
 *      </ul>
 *
 **/

// NOTE:  The AFP Resources for the NPJAVA library can be restored from a save file
//        located in AFS usder ~pclancy/print/test/savf directory.
//        Create a save file on your AS/400
//        FTP the appropriate one to your AS/400 replacing the save file you created above
//        Restore the NPJAVA library from this save file you created above.
//
// See test/sfvinst.txt for more instructiosn 
// 
public class NPPrintTest extends TestDriver
{

    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try {
            NPPrintTest example = new NPPrintTest(args);
            example.init();
            example.start();
            example.stop();
            example.destroy();
            }
        catch (Exception e)
            {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
            }

        // Needed to make the virtual machine quit.
           System.exit(0);
    }

    /**
     This ctor used for applets.
     @exception Exception Initialization errors may cause an exception.
     **/
    public NPPrintTest()
      throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param args the array of command line arguments
     @exception Exception Incorrect arguments will cause an exception
     **/
    public NPPrintTest(String[] args)
      throws Exception
    {
        super(args);
    }

    /**
     Creates Testcase objects for all the testcases in this component.
     **/
    public void createTestcases()
    {
	/**
    * Make sure the setup is done
    * TODO:  This should be automated.  Otherwise check
    * sfvinst.txt
	 */






    	
    	if(TestDriverStatic.pause_)
  	    { 
    		  	try 
    		  	{						
    		  		systemObject_.connectService(AS400.PRINT);
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
    	     	    Job[] jobs = systemObject_.getJobs(AS400.PRINT);
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
  	   
   	 
	    if (printer_ != null)
        {
	    	System.out.println("Printer device name: "+ printer_);
        }
        else
        {
     	    System.out.println("The -printer flag must be set to a valid printer device name.");
        }
	       
        try
            {
            // print out which AS/400 system we are running to
            System.out.println("System: " + systemObject_.getSystemName() +
                               " Version: " + systemObject_.getVersion() +
                               " Release: " + systemObject_.getRelease() );

            // Instantiate all testcases to be run.
            boolean allTestcases = (namesAndVars_.size() == 0);
           
            // make sure NPJAVA library is in the library list
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("addlible npjava") != false)
                {

                ///////////////////////////////////
                // Print Parameter List testcase //
                ///////////////////////////////////

                // getFloatParameter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtParmGetFloatTestcase"))
                    {
                    NPPrtParmGetFloatTestcase tc =
                      new NPPrtParmGetFloatTestcase(systemObject_,
                                                    namesAndVars_.get("NPPrtParmGetFloatTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtParmGetFloatTestcase");
                    }

                // setFloatParameter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtParmSetFloatTestcase"))
                    {
                    NPPrtParmSetFloatTestcase tc =
                      new NPPrtParmSetFloatTestcase(systemObject_,
                                                    namesAndVars_.get("NPPrtParmSetFloatTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtParmSetFloatTestcase");
                    }

                // getIntegerParameter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtParmGetIntTestcase"))
                    {
                    NPPrtParmGetIntTestcase tc =
                      new NPPrtParmGetIntTestcase(systemObject_,
                                                  namesAndVars_.get("NPPrtParmGetIntTestcase"),
                                                  runMode_,
                                                  fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtParmGetIntTestcase");
                    }

                // setIntegerParameter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtParmSetIntTestcase"))
                    {
                    NPPrtParmSetIntTestcase tc =
                      new NPPrtParmSetIntTestcase(systemObject_,
                                                  namesAndVars_.get("NPPrtParmSetIntTestcase"),
                                                  runMode_,
                                                  fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtParmSetIntTestcase");
                    }

                // getStringParameter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtParmGetStrTestcase"))
                    {
                    NPPrtParmGetStrTestcase tc =
                      new NPPrtParmGetStrTestcase(systemObject_,
                                                  namesAndVars_.get("NPPrtParmGetStrTestcase"),
                                                  runMode_,
                                                  fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtParmGetStrTestcase");
                    }

                // setStringParameter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtParmSetStrTestcase"))
                    {
                    NPPrtParmSetStrTestcase tc =
                      new NPPrtParmSetStrTestcase(systemObject_,
                                                  namesAndVars_.get("NPPrtParmSetStrTestcase"),
                                                  runMode_,
                                                  fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtParmSetStrTestcase");
                    }

                ////////////////////////////////////////
                // Print Object Input Stream testcase //
                ////////////////////////////////////////
                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrAvailableTestcase"))
                    {
                    NPPrtObjectInStrAvailableTestcase tc =
                      new NPPrtObjectInStrAvailableTestcase(systemObject_,
                                                            namesAndVars_.get("NPPrtObjectInStrAvailableTestcase"),
                                                            runMode_,
                                                            fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrAvailableTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrCloseTestcase"))
                    {
                    NPPrtObjectInStrCloseTestcase tc =
                      new NPPrtObjectInStrCloseTestcase(systemObject_,
                                                        namesAndVars_.get("NPPrtObjectInStrCloseTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrCloseTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrMarkTestcase"))
                    {
                    NPPrtObjectInStrMarkTestcase tc =
                      new NPPrtObjectInStrMarkTestcase(systemObject_,
                                                       namesAndVars_.get("NPPrtObjectInStrMarkTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrMarkTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrMarkSupTestcase"))
                    {
                    NPPrtObjectInStrMarkSupTestcase tc =
                      new NPPrtObjectInStrMarkSupTestcase(systemObject_,
                                                          namesAndVars_.get("NPPrtObjectInStrMarkSupTestcase"),
                                                          runMode_,
                                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrMarkSupTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrReadTestcase"))
                    {
                    NPPrtObjectInStrReadTestcase tc =
                      new NPPrtObjectInStrReadTestcase(systemObject_,
                                                       namesAndVars_.get("NPPrtObjectInStrReadTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrReadTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrResetTestcase"))
                    {
                    NPPrtObjectInStrResetTestcase tc =
                      new NPPrtObjectInStrResetTestcase(systemObject_,
                                                        namesAndVars_.get("NPPrtObjectInStrResetTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrResetTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPPrtObjectInStrSkipTestcase"))
                    {
                    NPPrtObjectInStrSkipTestcase tc =
                      new NPPrtObjectInStrSkipTestcase(systemObject_,
                                                       namesAndVars_.get("NPPrtObjectInStrSkipTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjectInStrSkipTestcase");
                    }


                /////////////////////////////////////////////     // @A1A Added testcase
                // Print Object Page Input Stream testcase //
                /////////////////////////////////////////////
                if (allTestcases || namesAndVars_.containsKey("NPPrtObjPageInStrTestcase"))
                    {
                    NPPrtObjPageInStrTestcase tc =
                      new NPPrtObjPageInStrTestcase(systemObject_,
                                                    namesAndVars_.get("NPPrtObjPageInStrTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjPageInStrTestcase");
                    }

                ////////////////////////////////////////////////////     // @A1A Added testcase
                // Print Object Transformed Input Stream testcase //
                ////////////////////////////////////////////////////
                if (allTestcases || namesAndVars_.containsKey("NPPrtObjTransInStrTestcase"))
                    {
                    NPPrtObjTransInStrTestcase tc =
                      new NPPrtObjTransInStrTestcase(systemObject_,
                                                    namesAndVars_.get("NPPrtObjTransInStrTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtObjTransInStrTestcase");
                    }


                /////////////////////////////////////////
                // Spooled File Output Stream testcase //
                /////////////////////////////////////////

                // Close method
                if (allTestcases || namesAndVars_.containsKey("NPSplFOutStrCloseTestcase"))
                    {
                    NPSplFOutStrCloseTestcase tc =
                      new NPSplFOutStrCloseTestcase(systemObject_,
                                                    namesAndVars_.get("NPSplFOutStrCloseTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFOutStrCloseTestcase");
                    }

                // Constructor method
                if (allTestcases || namesAndVars_.containsKey("NPSplFOutStrCtorTestcase"))
                    {
                    NPSplFOutStrCtorTestcase tc =
                      new NPSplFOutStrCtorTestcase(systemObject_,
                                                   namesAndVars_.get("NPSplFOutStrCtorTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFOutStrCtorTestcase");
                    }

                // Flush method
                if (allTestcases || namesAndVars_.containsKey("NPSplFOutStrFlushTestcase"))
                    {
                    NPSplFOutStrFlushTestcase tc =
                      new NPSplFOutStrFlushTestcase(systemObject_,
                                                    namesAndVars_.get("NPSplFOutStrFlushTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFOutStrFlushTestcase");
                    }

                // GetSpooledFile method
                if (allTestcases || namesAndVars_.containsKey("NPSplFOutStrGetSplFTestcase"))
                    {
                    NPSplFOutStrGetSplFTestcase tc =
                      new NPSplFOutStrGetSplFTestcase(systemObject_,
                                                      namesAndVars_.get("NPSplFOutStrGetSplFTestcase"),
                                                      runMode_,
                                                      fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFOutStrGetSplFTestcase");
                    }

                // Write method
                if (allTestcases || namesAndVars_.containsKey("NPSplFOutStrWriteTestcase"))
                    {
                    NPSplFOutStrWriteTestcase tc =
                      new NPSplFOutStrWriteTestcase(systemObject_,
                                                    namesAndVars_.get("NPSplFOutStrWriteTestcase"),
                                                    runMode_,
                                                    fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFOutStrWriteTestcase");
                    }

                ///////////////////////////
                // Spooled File testcase //
                ///////////////////////////

                // Constructor method
                if (allTestcases || namesAndVars_.containsKey("NPSplFCtorTestcase"))
                    {
                    NPSplFCtorTestcase tc =
                      new NPSplFCtorTestcase(systemObject_,
                                             namesAndVars_.get("NPSplFCtorTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFCtorTestcase");
                    }


                // AnswerMessage method
                if (allTestcases || namesAndVars_.containsKey("NPSplFAnsMsgTestcase"))
                    {
                    NPSplFAnsMsgTestcase tc =
                      new NPSplFAnsMsgTestcase(systemObject_,
                                               namesAndVars_.get("NPSplFAnsMsgTestcase"),
                                               runMode_,
                                               fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFAnsMsgTestcase");
                    }

                // Delete method
                if (allTestcases || namesAndVars_.containsKey("NPSplFDeleteTestcase"))
                    {
                    NPSplFDeleteTestcase tc =
                      new NPSplFDeleteTestcase(systemObject_,
                                               namesAndVars_.get("NPSplFDeleteTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFDeleteTestcase");
                    }

                // GetInputStream method
                if (allTestcases || namesAndVars_.containsKey("NPSplFGetInputStreamTestcase"))
                    {
                    NPSplFGetInputStreamTestcase tc =
                      new NPSplFGetInputStreamTestcase(systemObject_,
                                                       namesAndVars_.get("NPSplFGetInputStreamTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFGetInputStreamTestcase");
                    }

                // GetMessage method
                if (allTestcases || namesAndVars_.containsKey("NPSplFGetMessageTestcase"))
                    {
                    NPSplFGetMessageTestcase tc =
                      new NPSplFGetMessageTestcase(systemObject_,
                                                   namesAndVars_.get("NPSplFGetMessageTestcase"),
                                                   runMode_,
                                                   fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFGetMessageTestcase");
                    }

                // Name
                //   - getName method
                if (allTestcases || namesAndVars_.containsKey("NPSplFNameTestcase"))
                    {
                    NPSplFNameTestcase tc =
                      new NPSplFNameTestcase(systemObject_,
                                             namesAndVars_.get("NPSplFNameTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFNameTestcase");
                    }

                // Hold method
                if (allTestcases || namesAndVars_.containsKey("NPSplFHoldTestcase"))
                    {
                    NPSplFHoldTestcase tc =
                      new NPSplFHoldTestcase(systemObject_,
                                             namesAndVars_.get("NPSplFHoldTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFHoldTestcase");
                    }

                // Move to Output Queue method
                if (allTestcases || namesAndVars_.containsKey("NPSplFMoveOutQTestcase"))
                    {
                    NPSplFMoveOutQTestcase tc =
                      new NPSplFMoveOutQTestcase(systemObject_,
                                                 namesAndVars_.get("NPSplFMoveOutQTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFMoveOutQTestcase");
                    }

                // Move Spooled File method
                if (allTestcases || namesAndVars_.containsKey("NPSplFMoveSplFTestcase"))
                    {
                    NPSplFMoveSplFTestcase tc =
                      new NPSplFMoveSplFTestcase(systemObject_,
                                                 namesAndVars_.get("NPSplFMoveSplFTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFMoveSplFTestcase");
                    }

                // MoveToTop method
                if (allTestcases || namesAndVars_.containsKey("NPSplFMoveToTopTestcase"))
                    {
                    NPSplFMoveToTopTestcase tc =
                      new NPSplFMoveToTopTestcase(systemObject_,
                                                  namesAndVars_.get("NPSplFMoveToTopTestcase"),
                                                  runMode_,
                                                  fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFMoveToTopTestcase");
                    }

                // Release method
                if (allTestcases || namesAndVars_.containsKey("NPSplFReleaseTestcase"))
                    {
                    NPSplFReleaseTestcase tc =
                      new NPSplFReleaseTestcase(systemObject_,
                                                namesAndVars_.get("NPSplFReleaseTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFReleaseTestcase");
                    }

                // SendNet method
                if (allTestcases || namesAndVars_.containsKey("NPSplFSendNetTestcase"))
                    {
                    NPSplFSendNetTestcase tc =
                      new NPSplFSendNetTestcase(systemObject_,
                                                namesAndVars_.get("NPSplFSendNetTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFSendNetTestcase");
                    }

                // SendTCP method
                if (allTestcases || namesAndVars_.containsKey("NPSplFSendTCPTestcase"))
                    {
                    NPSplFSendTCPTestcase tc =
                      new NPSplFSendTCPTestcase(systemObject_,
                                                namesAndVars_.get("NPSplFSendTCPTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFSendTCPTestcase");
                    }

                // SetAttributes method
                if (allTestcases || namesAndVars_.containsKey("NPSplFSetAttrsTestcase"))
                    {
                    NPSplFSetAttrsTestcase tc =
                      new NPSplFSetAttrsTestcase(systemObject_,
                                                 namesAndVars_.get("NPSplFSetAttrsTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFSetAttrsTestcase");
                    }

                // System
                //   - getSystem
                if (allTestcases || namesAndVars_.containsKey("NPSplFSystemTestcase"))
                    {
                    NPSplFSystemTestcase tc =
                      new NPSplFSystemTestcase(systemObject_,
                                             namesAndVars_.get("NPSplFSystemTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFSystemTestcase");
                    }

                // Update method
                if (allTestcases || namesAndVars_.containsKey("NPSplFUpdateTestcase"))
                    {
                    NPSplFUpdateTestcase tc =
                      new NPSplFUpdateTestcase(systemObject_,
                                             namesAndVars_.get("NPSplFUpdateTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFUpdateTestcase");
                    }

                ////////////////////////////////
                // Spooled File List testcase //
                ////////////////////////////////

                // FormType Filter
                if (allTestcases || namesAndVars_.containsKey("NPSplFListFormTypeFilterTestcase"))
                    {
                    NPSplFListFormTypeFilterTestcase tc =
                      new NPSplFListFormTypeFilterTestcase(systemObject_,
                                                           namesAndVars_.get("NPSplFListFormTypeFilterTestcase"),
                                                           runMode_,
                                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListFormTypeFilterTestcase");
                    }

                // Queue Filter
                if (allTestcases || namesAndVars_.containsKey("NPSplFListQueueFilterTestcase"))
                    {
                    NPSplFListQueueFilterTestcase tc =
                      new NPSplFListQueueFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPSplFListQueueFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListQueueFilterTestcase");
                    }

                // System
                if (allTestcases || namesAndVars_.containsKey("NPSplFListSystemTestcase"))
                    {
                    NPSplFListSystemTestcase tc =
                      new NPSplFListSystemTestcase(systemObject_,
                                                   namesAndVars_.get("NPSplFListSystemTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListSystemTestcase");
                    }

                // UserData Filter
                if (allTestcases || namesAndVars_.containsKey("NPSplFListUserDataFilterTestcase"))
                    {
                    NPSplFListUserDataFilterTestcase tc =
                      new NPSplFListUserDataFilterTestcase(systemObject_,
                                                           namesAndVars_.get("NPSplFListUserDataFilterTestcase"),
                                                           runMode_,
                                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListUserDataFilterTestcase");
                    }
                // Date Filter
                if (allTestcases || namesAndVars_.containsKey("NPSplFListDateFilterTestcase"))
                    {
                    NPSplFListDateFilterTestcase tc =
                      new NPSplFListDateFilterTestcase(systemObject_,
                                                           namesAndVars_.get("NPSplFListDateFilterTestcase"),
                                                           runMode_,
                                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListDateFilterTestcase");
                    }
                // Job System Filter
                if (allTestcases || namesAndVars_.containsKey("NPSplFListJobSystemFilterTestcase"))
                    {
                    NPSplFListJobSystemFilterTestcase tc =
                      new NPSplFListJobSystemFilterTestcase(systemObject_,
                                                           namesAndVars_.get("NPSplFListJobSystemFilterTestcase"),
                                                           runMode_,
                                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListJobSystemFilterTestcase");
                    }
                // User Filter
                if (allTestcases || namesAndVars_.containsKey("NPSplFListUserFilterTestcase"))
                    {
                    NPSplFListUserFilterTestcase tc =
                      new NPSplFListUserFilterTestcase(systemObject_,
                                                       namesAndVars_.get("NPSplFListUserFilterTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListUserFilterTestcase");
                    }

                // Ctor
                if (allTestcases || namesAndVars_.containsKey("NPSplFListCtorTestcase"))
                    {
                    NPSplFListCtorTestcase tc =
                      new NPSplFListCtorTestcase(systemObject_,
                                                 namesAndVars_.get("NPSplFListCtorTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListCtorTestcase");
                    }

                // OpenAsync method
                if (allTestcases || namesAndVars_.containsKey("NPSplFListOpenAsyncTestcase"))
                    {
                    NPSplFListOpenAsyncTestcase tc =
                      new NPSplFListOpenAsyncTestcase(systemObject_,
                                                      namesAndVars_.get("NPSplFListOpenAsyncTestcase"),
                                                      runMode_,
                                                      fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListOpenAsyncTestcase");
                    }

                // OpenSync method
                if (allTestcases || namesAndVars_.containsKey("NPSplFListOpenSyncTestcase"))
                    {
                    NPSplFListOpenSyncTestcase tc =
                      new NPSplFListOpenSyncTestcase(systemObject_,
                                                     namesAndVars_.get("NPSplFListOpenSyncTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListOpenSyncTestcase");
                    }

                // Listener methods
                if (allTestcases || namesAndVars_.containsKey("NPSplFListListenerTestcase"))
                    {
                    NPSplFListListenerTestcase tc =
                      new NPSplFListListenerTestcase(systemObject_,
                                                     namesAndVars_.get("NPSplFListListenerTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListListenerTestcase");
                    }

                // ResetFilter method
                if (allTestcases || namesAndVars_.containsKey("NPSplFListResetFilterTestcase"))
                    {
                    NPSplFListResetFilterTestcase tc =
                      new NPSplFListResetFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPSplFListResetFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListResetFilterTestcase");
                    }

                // AttributesToRetrieve methods
                //   - setAttributesToRetrieve method
                //   - resetAttributesToRetrieve method
                if (allTestcases || namesAndVars_.containsKey("NPSplFListAttrsToRetTestcase"))
                    {
                    NPSplFListAttrsToRetTestcase tc =
                      new NPSplFListAttrsToRetTestcase(systemObject_,
                                                       namesAndVars_.get("NPSplFListAttrsToRetTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListAttrsToRetTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPSplFListSerializeTestcase"))
                    {
                    NPSplFListSerializeTestcase tc =
                      new NPSplFListSerializeTestcase(systemObject_,
                                                      namesAndVars_.get("NPSplFListSerializeTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSplFListSerializeTestcase");
                    }

                ////////////////////////////
                // Output Queue testcases //
                ////////////////////////////

                // Constructor method
                if (allTestcases || namesAndVars_.containsKey("NPOutQCtorTestcase"))
                    {
                    NPOutQCtorTestcase tc =
                      new NPOutQCtorTestcase(systemObject_,
                                             namesAndVars_.get("NPOutQCtorTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQCtorTestcase");
                    }

                // Clear method
                if (allTestcases || namesAndVars_.containsKey("NPOutQClearTestcase"))
                    {
                    NPOutQClearTestcase tc =
                      new NPOutQClearTestcase(systemObject_,
                                              namesAndVars_.get("NPOutQClearTestcase"),
                                              runMode_,
                                              fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQClearTestcase");
                    }

                // Name
                // - getName method
                if (allTestcases || namesAndVars_.containsKey("NPOutQNameTestcase"))
                    {
                    NPOutQNameTestcase tc =
                      new NPOutQNameTestcase(systemObject_,
                                             namesAndVars_.get("NPOutQNameTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQNameTestcase");
                    }

                // Path
                // - getPath method
                if (allTestcases || namesAndVars_.containsKey("NPOutQPathTestcase"))
                    {
                    NPOutQPathTestcase tc =
                      new NPOutQPathTestcase(systemObject_,
                                             namesAndVars_.get("NPOutQPathTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQPathTestcase");
                    }

                // Hold method
                if (allTestcases || namesAndVars_.containsKey("NPOutQHoldTestcase"))
                    {
                    NPOutQHoldTestcase tc =
                      new NPOutQHoldTestcase(systemObject_,
                                             namesAndVars_.get("NPOutQHoldTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQHoldTestcase");
                    }

                // System method
                //   - setSystem
                //   - getSystem
                if (allTestcases || namesAndVars_.containsKey("NPOutQSystemTestcase"))
                    {
                    NPOutQSystemTestcase tc =
                      new NPOutQSystemTestcase(systemObject_,
                                               namesAndVars_.get("NPOutQSystemTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQSystemTestcase");
                    }

                // Release method
                if (allTestcases || namesAndVars_.containsKey("NPOutQReleaseTestcase"))
                    {
                    NPOutQReleaseTestcase tc =
                      new NPOutQReleaseTestcase(systemObject_,
                                                namesAndVars_.get("NPOutQReleaseTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQReleaseTestcase");
                    }

                // Update method
                if (allTestcases || namesAndVars_.containsKey("NPOutQUpdateTestcase"))
                    {
                    NPOutQUpdateTestcase tc =
                      new NPOutQUpdateTestcase(systemObject_,
                                               namesAndVars_.get("NPOutQUpdateTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQUpdateTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPOutQSerializeTestcase"))
                    {
                    NPOutQSerializeTestcase tc =
                      new NPOutQSerializeTestcase(systemObject_,
                                                  namesAndVars_.get("NPOutQSerializeTestcase"),
                                                  runMode_,
                                                  fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQSerializeTestcase");
                    }

                /////////////////////////////////
                // Output Queue List testcases //
                /////////////////////////////////

                // Queue Filter
                //   -setQueueFilter method
                //   -getQueueFilter method
                if (allTestcases || namesAndVars_.containsKey("NPOutQListQueueFilterTestcase"))
                    {
                    NPOutQListQueueFilterTestcase tc =
                      new NPOutQListQueueFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPOutQListQueueFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListQueueFilterTestcase");
                    }

                // System
                //   -setSystem method
                //   -getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPOutQListSystemTestcase"))
                    {
                    NPOutQListSystemTestcase tc =
                      new NPOutQListSystemTestcase(systemObject_,
                                                   namesAndVars_.get("NPOutQListSystemTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListSystemTestcase");
                    }

                // Constructor
                if (allTestcases || namesAndVars_.containsKey("NPOutQListCtorTestcase"))
                    {
                    NPOutQListCtorTestcase tc =
                      new NPOutQListCtorTestcase(systemObject_,
                                                 namesAndVars_.get("NPOutQListCtorTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListCtorTestcase");
                    }

                // OpenAsynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPOutQListOpenAsyncTestcase"))
                    {
                    NPOutQListOpenAsyncTestcase tc =
                      new NPOutQListOpenAsyncTestcase(systemObject_,
                                                      namesAndVars_.get("NPOutQListOpenAsyncTestcase"),
                                                      runMode_,
                                                      fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListOpenAsyncTestcase");
                    }

                // OpenSynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPOutQListOpenSyncTestcase"))
                    {
                    NPOutQListOpenSyncTestcase tc =
                      new NPOutQListOpenSyncTestcase(systemObject_,
                                                     namesAndVars_.get("NPOutQListOpenSyncTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListOpenSyncTestcase");
                    }

                // Listener methods
                if (allTestcases || namesAndVars_.containsKey("NPOutQListListenerTestcase"))
                    {
                    NPOutQListListenerTestcase tc =
                      new NPOutQListListenerTestcase(systemObject_,
                                                     namesAndVars_.get("NPOutQListListenerTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListListenerTestcase");
                    }

                // resetFilter method
                if (allTestcases || namesAndVars_.containsKey("NPOutQListResetFilterTestcase"))
                    {
                    NPOutQListResetFilterTestcase tc =
                      new NPOutQListResetFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPOutQListResetFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListResetFilterTestcase");
                    }

                // AttributesToRetrieve methods
                //   - setAttributesToRetrieve method
                //   - resetAttributesToRetrieve method
                if (allTestcases || namesAndVars_.containsKey("NPOutQListAttrsToRetTestcase"))
                    {
                    NPOutQListAttrsToRetTestcase tc =
                      new NPOutQListAttrsToRetTestcase(systemObject_,
                                                       namesAndVars_.get("NPOutQListAttrsToRetTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListAttrsToRetTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPOutQListSerializeTestcase"))
                    {
                    NPOutQListSerializeTestcase tc =
                      new NPOutQListSerializeTestcase(systemObject_,
                                                      namesAndVars_.get("NPOutQListSerializeTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPOutQListSerializeTestcase");
                    }

                //////////////////////
                // Printer testcase //
                //////////////////////

                // Constructor method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDCtorTestcase"))
                    {
                    NPPrtDCtorTestcase tc =
                      new NPPrtDCtorTestcase(systemObject_,
                                             namesAndVars_.get("NPPrtDCtorTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDCtorTestcase");
                    }

                // Name method
                //   -setName method
                //   -getName method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDNameTestcase"))
                    {
                    NPPrtDNameTestcase tc =
                      new NPPrtDNameTestcase(systemObject_,
                                                namesAndVars_.get("NPPrtDNameTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDNameTestcase");
                    }

                // System method
                //   -setSystem method
                //   -getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDSystemTestcase"))
                    {
                    NPPrtDSystemTestcase tc =
                      new NPPrtDSystemTestcase(systemObject_,
                                                namesAndVars_.get("NPPrtDSystemTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDSystemTestcase");
                    }

                // Update method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDUpdateTestcase"))
                    {
                    NPPrtDUpdateTestcase tc =
                      new NPPrtDUpdateTestcase(systemObject_,
                                                namesAndVars_.get("NPPrtDUpdateTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDUpdateTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPPrtDSerializeTestcase"))
                    {
                    NPPrtDSerializeTestcase tc =
                      new NPPrtDSerializeTestcase(systemObject_,
                                                  namesAndVars_.get("NPPrtDSerializeTestcase"),
                                                  runMode_,
                                                  fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDSerializeTestcase");
                    }

                ///////////////////////////
                // Printer List testcase //
                ///////////////////////////

                // Printer Filter
                //   -setPrinterFilter method
                //   -getPrinterFilter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListPrinterFilterTestcase"))
                    {
                    NPPrtDListPrinterFilterTestcase tc =
                      new NPPrtDListPrinterFilterTestcase(systemObject_,
                                                          namesAndVars_.get("NPPrtDListPrinterFilterTestcase"),
                                                          runMode_,
                                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListPrinterFilterTestcase");
                    }

                // System
                //   -setSystem method
                //   -getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListSystemTestcase"))
                    {
                    NPPrtDListSystemTestcase tc =
                      new NPPrtDListSystemTestcase(systemObject_,
                                                   namesAndVars_.get("NPPrtDListSystemTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListSystemTestcase");
                    }

                // Constructor
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListCtorTestcase"))
                    {
                    NPPrtDListCtorTestcase tc =
                      new NPPrtDListCtorTestcase(systemObject_,
                                                 namesAndVars_.get("NPPrtDListCtorTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListCtorTestcase");
                    }

                // OpenAsynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListOpenAsyncTestcase"))
                    {
                    NPPrtDListOpenAsyncTestcase tc =
                      new NPPrtDListOpenAsyncTestcase(systemObject_,
                                                      namesAndVars_.get("NPPrtDListOpenAsyncTestcase"),
                                                      runMode_,
                                                      fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListOpenAsyncTestcase");
                    }

                // OpenSynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListOpenSyncTestcase"))
                    {
                    NPPrtDListOpenSyncTestcase tc =
                      new NPPrtDListOpenSyncTestcase(systemObject_,
                                                     namesAndVars_.get("NPPrtDListOpenSyncTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListOpenSyncTestcase");
                    }

                // Listener methods
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListListenerTestcase"))
                    {
                    NPPrtDListListenerTestcase tc =
                      new NPPrtDListListenerTestcase(systemObject_,
                                                     namesAndVars_.get("NPPrtDListListenerTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListListenerTestcase");
                    }

                // resetFilter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListResetFilterTestcase"))
                    {
                    NPAFPRListResetFilterTestcase tc =
                      new NPAFPRListResetFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPPrtDListResetFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListResetFilterTestcase");
                    }

                // AttributesToRetrieve methods
                //   - setAttributesToRetrieve method
                //   - resetAttributesToRetrieve method
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListAttrsToRetTestcase"))
                    {
                    NPPrtDListAttrsToRetTestcase tc =
                      new NPPrtDListAttrsToRetTestcase(systemObject_,
                                                       namesAndVars_.get("NPPrtDListAttrsToRetTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListAttrsToRetTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPPrtDListSerializeTestcase"))
                    {
                    NPPrtDListSerializeTestcase tc =
                      new NPPrtDListSerializeTestcase(systemObject_,
                                                      namesAndVars_.get("NPPrtDListSerializeTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtDListSerializeTestcase");
                    }

                ////////////////////////
                // WriterJob testcase //
                ////////////////////////

                // Start method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJStartTestcase"))
                    {
                    NPWrtJStartTestcase tc =
                      new NPWrtJStartTestcase(systemObject_,
                                              namesAndVars_.get("NPWrtJStartTestcase"),
                                              runMode_,
                                              fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJStartTestcase");
                    }

                // Name
                //   - getName method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJNameTestcase"))
                    {
                    NPWrtJNameTestcase tc =
                      new NPWrtJNameTestcase(systemObject_,
                                             namesAndVars_.get("NPWrtJNameTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJNameTestcase");
                    }

                // End method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJEndTestcase"))
                    {
                    NPWrtJEndTestcase tc =
                      new NPWrtJEndTestcase(systemObject_,
                                            namesAndVars_.get("NPWrtJEndTestcase"),
                                            runMode_,
                                            fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJEndTestcase");
                    }

                // System
                //   - getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJSystemTestcase"))
                    {
                    NPWrtJSystemTestcase tc =
                      new NPWrtJSystemTestcase(systemObject_,
                                               namesAndVars_.get("NPWrtJSystemTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJSystemTestcase");
                    }

                // Update method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJUpdateTestcase"))
                    {
                    NPWrtJUpdateTestcase tc =
                      new NPWrtJUpdateTestcase(systemObject_,
                                               namesAndVars_.get("NPWrtJUpdateTestcase"),
                                               runMode_,
                                               fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJUpdateTestcase");
                    }

                /////////////////////////////
                // WriterJob List testcase //
                /////////////////////////////

                // Queue Filter
                //   -setQueueFilter method
                //   -getQueueFilter method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListQueueFilterTestcase"))
                    {
                    NPWrtJListQueueFilterTestcase tc =
                      new NPWrtJListQueueFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPWrtJListQueueFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListQueueFilterTestcase");
                    }

                // System
                //   -setSystem method
                //   -getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListSystemTestcase"))
                    {
                    NPWrtJListSystemTestcase tc =
                      new NPWrtJListSystemTestcase(systemObject_,
                                                   namesAndVars_.get("NPWrtJListSystemTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListSystemTestcase");
                    }

                // Writer Filter
                //   -setWriterFilter method
                //   -getWriterFilter method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListWriterFilterTestcase"))
                    {
                    NPWrtJListWriterFilterTestcase tc =
                      new NPWrtJListWriterFilterTestcase(systemObject_,
                                                         namesAndVars_.get("NPWrtJListWriterFilterTestcase"),
                                                         runMode_,
                                                         fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListWriterFilterTestcase");
                    }

                // Constructor
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListCtorTestcase"))
                    {
                    NPWrtJListCtorTestcase tc =
                      new NPWrtJListCtorTestcase(systemObject_,
                                                 namesAndVars_.get("NPWrtJListCtorTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListCtorTestcase");
                    }

                // OpenAsynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListOpenAsyncTestcase"))
                    {
                    NPWrtJListOpenAsyncTestcase tc =
                      new NPWrtJListOpenAsyncTestcase(systemObject_,
                                                      namesAndVars_.get("NPWrtJListOpenAsyncTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListOpenAsyncTestcase");
                    }

                // OpenSynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListOpenSyncTestcase"))
                    {
                    NPWrtJListOpenSyncTestcase tc =
                      new NPWrtJListOpenSyncTestcase(systemObject_,
                                                     namesAndVars_.get("NPWrtJListOpenSyncTestcase"),
                                                     runMode_,
                                                     fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListOpenSyncTestcase");
                    }

                // Listener methods
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListListenerTestcase"))
                    {
                    NPWrtJListListenerTestcase tc =
                      new NPWrtJListListenerTestcase(systemObject_,
                                                     namesAndVars_.get("NPWrtJListListenerTestcase"),
                                                     runMode_,
                                                     fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListListenerTestcase");
                    }

                // resetFilter method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListResetFilterTestcase"))
                    {
                    NPWrtJListResetFilterTestcase tc =
                      new NPWrtJListResetFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPWrtJListResetFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListResetFilterTestcase");
                    }

                // AttributesToRetrieve methods
                //   - setAttributesToRetrieve method
                //   - resetAttributesToRetrieve method
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListAttrsToRetTestcase"))
                    {
                    NPWrtJListAttrsToRetTestcase tc =
                      new NPWrtJListAttrsToRetTestcase(systemObject_,
                                                       namesAndVars_.get("NPWrtJListAttrsToRetTestcase"),
                                                       runMode_,
                                                       fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListAttrsToRetTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPWrtJListSerializeTestcase"))
                    {
                    NPWrtJListSerializeTestcase tc =
                      new NPWrtJListSerializeTestcase(systemObject_,
                                                      namesAndVars_.get("NPWrtJListSerializeTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, printer_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPWrtJListSerializeTestcase");
                    }

                ///////////////////////////
                // Printer File testcase //
                ///////////////////////////

                // Constructor method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFCtorTestcase"))
                    {
                    NPPrtFCtorTestcase tc =
                      new NPPrtFCtorTestcase(systemObject_,
                                             namesAndVars_.get("NPPrtFCtorTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFCtorTestcase");
                    }

                // Name
                //   - getName method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFNameTestcase"))
                    {
                    NPPrtFNameTestcase tc =
                      new NPPrtFNameTestcase(systemObject_,
                                             namesAndVars_.get("NPPrtFNameTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFNameTestcase");
                    }

                // SetAttributes method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFSetAttrsTestcase"))
                    {
                    NPPrtFSetAttrsTestcase tc =
                      new NPPrtFSetAttrsTestcase(systemObject_,
                                                 namesAndVars_.get("NPPrtFSetAttrsTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFSetAttrsTestcase");
                    }

                // Path
                //   - setPath method
                //   - getPath method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFPathTestcase"))
                    {
                    NPPrtFPathTestcase tc =
                      new NPPrtFPathTestcase(systemObject_,
                                             namesAndVars_.get("NPPrtFPathTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFPathTestcase");
                    }

                // System
                //   - setSystem method
                //   - getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFSystemTestcase"))
                    {
                    NPPrtFSystemTestcase tc =
                      new NPPrtFSystemTestcase(systemObject_,
                                               namesAndVars_.get("NPPrtFSystemTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFSystemTestcase");
                    }

                // Update method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFUpdateTestcase"))
                    {
                    NPPrtFUpdateTestcase tc =
                      new NPPrtFUpdateTestcase(systemObject_,
                                               namesAndVars_.get("NPPrtFUpdateTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFUpdateTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPPrtFSerializeTestcase"))
                    {
                    NPPrtFSerializeTestcase tc =
                      new NPPrtFSerializeTestcase(systemObject_,
                                                  namesAndVars_.get("NPPrtFSerializeTestcase"),
                                                  runMode_,
                                                  fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFSerializeTestcase");
                    }


                ////////////////////////////////
                // Printer File List testcase //
                ////////////////////////////////

                // Constructor
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListCtorTestcase"))
                    {
                    NPPrtFListCtorTestcase tc =
                      new NPPrtFListCtorTestcase(systemObject_,
                                                 namesAndVars_.get("NPPrtFListCtorTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListCtorTestcase");
                    }

                // PrinterFile Filter
                //   -setPrinterFileFilter method
                //   -getPrinterFileFilter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListPrinterFileFilterTestcase"))
                    {
                    NPPrtFListPrinterFileFilterTestcase tc =
                      new NPPrtFListPrinterFileFilterTestcase(systemObject_,
                                                              namesAndVars_.get("NPPrtFListPrinterFileFilterTestcase"),
                                                              runMode_,
                                                              fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListPrinterFileFilterTestcase");
                    }

                // System
                //   -setSystem method
                //   -getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListSystemTestcase"))
                    {
                    NPPrtFListSystemTestcase tc =
                      new NPPrtFListSystemTestcase(systemObject_,
                                                   namesAndVars_.get("NPPrtFListSystemTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListSystemTestcase");
                    }

                // OpenAsynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListOpenAsyncTestcase"))
                    {
                    NPPrtFListOpenAsyncTestcase tc =
                      new NPPrtFListOpenAsyncTestcase(systemObject_,
                                                      namesAndVars_.get("NPPrtFListOpenAsyncTestcase"),
                                                      runMode_,
                                                      fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListOpenAsyncTestcase");
                    }

                // OpenSynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListOpenSyncTestcase"))
                    {
                    NPPrtFListOpenSyncTestcase tc =
                      new NPPrtFListOpenSyncTestcase(systemObject_,
                                                     namesAndVars_.get("NPPrtFListOpenSyncTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListOpenSyncTestcase");
                    }

                // Listener methods
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListListenerTestcase"))
                    {
                    NPPrtFListListenerTestcase tc =
                      new NPPrtFListListenerTestcase(systemObject_,
                                                     namesAndVars_.get("NPPrtFListListenerTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListListenerTestcase");
                    }

                // resetFilter method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListResetFilterTestcase"))
                    {
                    NPPrtFListResetFilterTestcase tc =
                      new NPPrtFListResetFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPPrtFListResetFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListResetFilterTestcase");
                    }

                // AttributesToRetrieve methods
                //   - setAttributesToRetrieve method
                //   - resetAttributesToRetrieve method
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListAttrsToRetTestcase"))
                    {
                    NPPrtFListAttrsToRetTestcase tc =
                      new NPPrtFListAttrsToRetTestcase(systemObject_,
                                                       namesAndVars_.get("NPPrtFListAttrsToRetTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListAttrsToRetTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPPrtFListSerializeTestcase"))
                    {
                    NPPrtFListSerializeTestcase tc =
                      new NPPrtFListSerializeTestcase(systemObject_,
                                                      namesAndVars_.get("NPPrtFListSerializeTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPPrtFListSerializeTestcase");
                    }

                ///////////////////////////
                // AFP Resource testcase //
                ///////////////////////////

                // Constructor method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRCtorTestcase"))
                    {
                    NPAFPRCtorTestcase tc =
                      new NPAFPRCtorTestcase(systemObject_,
                                             namesAndVars_.get("NPAFPRCtorTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRCtorTestcase");
                    }

                // Name methods
                //   - getName
                //   - setName
                if (allTestcases || namesAndVars_.containsKey("NPAFPRNameTestcase"))
                    {
                    NPAFPRNameTestcase tc =
                      new NPAFPRNameTestcase(systemObject_,
                                             namesAndVars_.get("NPAFPRNameTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRNameTestcase");
                    }

                // Path methods
                //   - getPath
                if (allTestcases || namesAndVars_.containsKey("NPAFPRPathTestcase"))
                    {
                    NPAFPRPathTestcase tc =
                      new NPAFPRPathTestcase(systemObject_,
                                             namesAndVars_.get("NPAFPRPathTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRPathTestcase");
                    }

                // System methods
                //   - getSystem
                //   - setSystem
                if (allTestcases || namesAndVars_.containsKey("NPAFPRSystemTestcase"))
                    {
                    NPAFPRSystemTestcase tc =
                      new NPAFPRSystemTestcase(systemObject_,
                                               namesAndVars_.get("NPAFPRSystemTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRSystemTestcase");
                    }

                // Update method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRUpdateTestcase"))
                    {
                    NPAFPRUpdateTestcase tc =
                      new NPAFPRUpdateTestcase(systemObject_,
                                               namesAndVars_.get("NPAFPRUpdateTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRUpdateTestcase");
                    }

                // getInputStream method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRGetInputStreamTestcase"))
                    {
                    NPAFPRGetInputStreamTestcase tc =
                      new NPAFPRGetInputStreamTestcase(systemObject_,
                                                       namesAndVars_.get("NPAFPRGetInputStreamTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRGetInputStreamTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPAFPRSerializeTestcase"))
                    {
                    NPAFPRSerializeTestcase tc =
                      new NPAFPRSerializeTestcase(systemObject_,
                                                  namesAndVars_.get("NPAFPRSerializeTestcase"),
                                                  runMode_,
                                                  fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRSerializeTestcase");
                    }

                ////////////////////////////////
                // AFP Resource List testcase //
                ////////////////////////////////

                // Constructor
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListCtorTestcase"))
                    {
                    NPAFPRListCtorTestcase tc =
                      new NPAFPRListCtorTestcase(systemObject_,
                                                 namesAndVars_.get("NPAFPRListCtorTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListCtorTestcase");
                    }

                // FontPelDensity Filter
                //   -setFontPelDensityFilter method
                //   -getFontPelDensityFilter method
/* Network Print Server does not support FontPelDensity filtering so this is commented
   out until they do.
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListFontPelDFilterTestcase"))
                    {
                    NPAFPRListFontPelDFilterTestcase tc =
                      new NPAFPRListFontPelDFilterTestcase(
                                                           systemObject_,
                                                           namesAndVars_.get("NPAFPRListFontPelDFilterTestcase"),
                                                           runMode_,
                                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListFontPelDFilterTestcase");
                    }
*/
                // Resource Filter
                //   -setResourceFilter method
                //   -getResourceFilter method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListResourceFilterTestcase"))
                    {
                    NPAFPRListResourceFilterTestcase tc =
                      new NPAFPRListResourceFilterTestcase(
                                                           systemObject_,
                                                           namesAndVars_.get("NPAFPRListResourceFilterTestcase"),
                                                           runMode_,
                                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListResourceFilterTestcase");
                    }

                // SpooledFile Filter
                //   -setSpooledFileFilter method
                //   -getSpooledFileFilter method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListSpooledFileFilterTestcase"))
                    {
                    NPAFPRListSpooledFileFilterTestcase tc =
                      new NPAFPRListSpooledFileFilterTestcase(
                                                              systemObject_,
                                                              namesAndVars_.get("NPAFPRListSpooledFileFilterTestcase"),
                                                              runMode_,
                                                              fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListSpooledFileFilterTestcase");
                    }

                // System
                //   -setSystem method
                //   -getSystem method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListSystemTestcase"))
                    {
                    NPAFPRListSystemTestcase tc =
                      new NPAFPRListSystemTestcase(
                                                   systemObject_,
                                                   namesAndVars_.get("NPAFPRListSystemTestcase"),
                                                   runMode_,
                                                   fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListSystemTestcase");
                    }

                // OpenAsynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListOpenAsyncTestcase"))
                    {
                    NPAFPRListOpenAsyncTestcase tc =
                      new NPAFPRListOpenAsyncTestcase(systemObject_,
                                                      namesAndVars_.get("NPAFPRListOpenAsyncTestcase"),
                                                      runMode_,
                                                      fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListOpenAsyncTestcase");
                    }

                // OpenSynchronous method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListOpenSyncTestcase"))
                    {
                    NPAFPRListOpenSyncTestcase tc =
                      new NPAFPRListOpenSyncTestcase(systemObject_,
                                                     namesAndVars_.get("NPAFPRListOpenSyncTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListOpenSyncTestcase");
                    }

                // Listener methods
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListListenerTestcase"))
                    {
                    NPAFPRListListenerTestcase tc =
                      new NPAFPRListListenerTestcase(systemObject_,
                                                     namesAndVars_.get("NPAFPRListListenerTestcase"),
                                                     runMode_,
                                                     fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListListenerTestcase");
                    }

                // resetFilter method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListResetFilterTestcase"))
                    {
                    NPAFPRListResetFilterTestcase tc =
                      new NPAFPRListResetFilterTestcase(systemObject_,
                                                        namesAndVars_.get("NPAFPRListResetFilterTestcase"),
                                                        runMode_,
                                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListResetFilterTestcase");
                    }

                // AttributesToRetrieve methods
                //   - setAttributesToRetrieve method
                //   - resetAttributesToRetrieve method
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListAttrsToRetTestcase"))
                    {
                    NPAFPRListAttrsToRetTestcase tc =
                      new NPAFPRListAttrsToRetTestcase(systemObject_,
                                                       namesAndVars_.get("NPAFPRListAttrsToRetTestcase"),
                                                       runMode_,
                                                       fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListAttrsToRetTestcase");
                    }

                // Serialization
                if (allTestcases || namesAndVars_.containsKey("NPAFPRListSerializeTestcase"))
                    {
                    NPAFPRListSerializeTestcase tc =
                      new NPAFPRListSerializeTestcase(systemObject_,
                                                      namesAndVars_.get("NPAFPRListSerializeTestcase"),
                                                      runMode_,
                                                      fileOutputStream_, password_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPAFPRListSerializeTestcase");
                    }

                /////////////////////////////
                // SCS5256Writer testcases //
                /////////////////////////////

                if (allTestcases || namesAndVars_.containsKey("NPSCS5256CtorTestcase"))
                    {
                    NPSCS5256CtorTestcase tc =
                      new NPSCS5256CtorTestcase(systemObject_,
                                          namesAndVars_.get("NPSCS5256CtorTestcase"),
                                          runMode_,
                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5256CtorTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSCRTestcase"))
                    {
                    NPSCSCRTestcase tc =
                      new NPSCSCRTestcase(systemObject_,
                                        namesAndVars_.get("NPSCSCRTestcase"),
                                        runMode_,
                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSCRTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSCloseTestcase"))
                    {
                    NPSCSCloseTestcase tc =
                      new NPSCSCloseTestcase(systemObject_,
                                           namesAndVars_.get("NPSCSCloseTestcase"),
                                           runMode_,
                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSCloseTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5256EndPageTestcase"))
                    {
                    NPSCS5256EndPageTestcase tc =
                      new NPSCS5256EndPageTestcase(systemObject_,
                                                 namesAndVars_.get("NPSCS5256EndPageTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5256EndPageTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSFlushTestcase"))
                    {
                    NPSCSFlushTestcase tc =
                      new NPSCSFlushTestcase(systemObject_,
                                           namesAndVars_.get("NPSCSFlushTestcase"),
                                           runMode_,
                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSFlushTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSGetEncTestcase"))
                    {
                    NPSCSGetEncTestcase tc =
                      new NPSCSGetEncTestcase(systemObject_,
                                            namesAndVars_.get("NPSCSGetEncTestcase"),
                                            runMode_,
                                            fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSGetEncTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSLFTestcase"))
                    {
                    NPSCSLFTestcase tc =
                      new NPSCSLFTestcase(systemObject_,
                                        namesAndVars_.get("NPSCSLFTestcase"),
                                        runMode_,
                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSLFTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSNLTestcase"))
                    {
                    NPSCSNLTestcase tc =
                      new NPSCSNLTestcase(systemObject_,
                                        namesAndVars_.get("NPSCSNLTestcase"),
                                        runMode_,
                                        fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSNLTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSWriteTestcase"))
                    {
                    NPSCSWriteTestcase tc =
                      new NPSCSWriteTestcase(systemObject_,
                                           namesAndVars_.get("NPSCSWriteTestcase"),
                                           runMode_,
                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSWriteTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5256Testcase"))
                    {
                    NPSCS5256Testcase tc =
                      new NPSCS5256Testcase(systemObject_,
                                           namesAndVars_.get("NPSCS5256Testcase"),
                                           runMode_,
                                           fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5256Testcase");
                    }

                /////////////////////////////
                // SCS5224Writer testcases //
                /////////////////////////////

                if (allTestcases || namesAndVars_.containsKey("NPSCS5224CPITestcase"))
                    {
                    NPSCS5224CPITestcase tc =
                      new NPSCS5224CPITestcase(systemObject_,
                                             namesAndVars_.get("NPSCS5224CPITestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5224CPITestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5224LPITestcase"))
                    {
                    NPSCS5224LPITestcase tc =
                      new NPSCS5224LPITestcase(systemObject_,
                                             namesAndVars_.get("NPSCS5224LPITestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5224LPITestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5224Testcase"))
                    {
                    NPSCS5224Testcase tc =
                      new NPSCS5224Testcase(systemObject_,
                                           namesAndVars_.get("NPSCS5224Testcase"),
                                           runMode_,
                                           fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5224Testcase");
                    }


                /////////////////////////////
                // SCS5219Writer testcases //
                /////////////////////////////

                if (allTestcases || namesAndVars_.containsKey("NPSCSAbsHorPosTestcase"))
                    {
                    NPSCSAbsHorPosTestcase tc =
                      new NPSCSAbsHorPosTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSAbsHorPosTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSAbsHorPosTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSAbsVerPosTestcase"))
                    {
                    NPSCSAbsVerPosTestcase tc =
                      new NPSCSAbsVerPosTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSAbsVerPosTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSAbsVerPosTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSBoldTestcase"))
                    {
                    NPSCSBoldTestcase tc =
                      new NPSCSBoldTestcase(systemObject_,
                                          namesAndVars_.get("NPSCSBoldTestcase"),
                                          runMode_,
                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSBoldTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5219CPITestcase"))
                    {
                    NPSCS5219CPITestcase tc =
                      new NPSCS5219CPITestcase(systemObject_,
                                             namesAndVars_.get("NPSCS5219CPITestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5219CPITestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSDestDrawerTestcase"))
                    {
                    NPSCSDestDrawerTestcase tc =
                      new NPSCSDestDrawerTestcase(systemObject_,
                                                namesAndVars_.get("NPSCSDestDrawerTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSDestDrawerTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5219EndPageTestcase"))
                    {
                    NPSCS5219EndPageTestcase tc =
                      new NPSCS5219EndPageTestcase(systemObject_,
                                                 namesAndVars_.get("NPSCS5219EndPageTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5219EndPageTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSEnvelopeTestcase"))
                    {
                    NPSCSEnvelopeTestcase tc =
                      new NPSCSEnvelopeTestcase(systemObject_,
                                              namesAndVars_.get("NPSCSEnvelopeTestcase"),
                                              runMode_,
                                              fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSEnvelopeTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSLeftMarginTestcase"))
                    {
                    NPSCSLeftMarginTestcase tc =
                      new NPSCSLeftMarginTestcase(systemObject_,
                                                namesAndVars_.get("NPSCSLeftMarginTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSLeftMarginTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSPaperTestcase"))
                    {
                    NPSCSPaperTestcase tc =
                      new NPSCSPaperTestcase(systemObject_,
                                           namesAndVars_.get("NPSCSPaperTestcase"),
                                           runMode_,
                                           fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSPaperTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSQualityTestcase"))
                    {
                    NPSCSQualityTestcase tc =
                      new NPSCSQualityTestcase(systemObject_,
                                             namesAndVars_.get("NPSCSQualityTestcase"),
                                             runMode_,
                                             fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSQualityTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSSrcDrawerTestcase"))
                    {
                    NPSCSSrcDrawerTestcase tc =
                      new NPSCSSrcDrawerTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSSrcDrawerTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSSrcDrawerTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSUnderlineTestcase"))
                    {
                    NPSCSUnderlineTestcase tc =
                      new NPSCSUnderlineTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSUnderlineTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSUnderlineTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5219Testcase"))
                    {
                    NPSCS5219Testcase tc =
                      new NPSCS5219Testcase(systemObject_,
                                           namesAndVars_.get("NPSCS5219Testcase"),
                                           runMode_,
                                           fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5219Testcase");
                    }

                /////////////////////////////
                // SCS3812Writer testcases //
                /////////////////////////////

                if (allTestcases || namesAndVars_.containsKey("NPSCSBoldTestcase"))
                    {
                    NPSCSBoldTestcase tc =
                      new NPSCSBoldTestcase(systemObject_,
                                          namesAndVars_.get("NPSCSBoldTestcase"),
                                          runMode_,
                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSBoldTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSDuplexTestcase"))
                    {
                    NPSCSDuplexTestcase tc =
                      new NPSCSDuplexTestcase(systemObject_,
                                            namesAndVars_.get("NPSCSDuplexTestcase"),
                                            runMode_,
                                            fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSDuplexTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSFontTestcase"))
                    {
                    NPSCSFontTestcase tc =
                      new NPSCSFontTestcase(systemObject_,
                                          namesAndVars_.get("NPSCSFontTestcase"),
                                          runMode_,
                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSFontTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSOrientationTestcase"))
                    {
                    NPSCSOrientationTestcase tc =
                      new NPSCSOrientationTestcase(systemObject_,
                                                 namesAndVars_.get("NPSCSOrientationTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSOrientationTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSCreateSplfTestcase"))
                    {
                    NPSCSCreateSplfTestcase tc =
                      new NPSCSCreateSplfTestcase(systemObject_,
                                                 namesAndVars_.get("NPSCSCreateSplfTestcase"),
                                                 runMode_,
                                                 fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSCreateSplfTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS3812Testcase"))
                    {
                    NPSCS3812Testcase tc =
                      new NPSCS3812Testcase(systemObject_,
                                           namesAndVars_.get("NPSCS3812Testcase"),
                                           runMode_,
                                           fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS3812Testcase");
                    }

                /////////////////////////////
                // SCS5553Writer testcases //
                /////////////////////////////

                if (allTestcases || namesAndVars_.containsKey("NPSCSHGridLineTestcase"))
                    {
                    NPSCSHGridLineTestcase tc =
                      new NPSCSHGridLineTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSHGridLineTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSHGridLineTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSCharRotateTestcase"))
                    {
                    NPSCSCharRotateTestcase tc =
                      new NPSCSCharRotateTestcase(systemObject_,
                                                namesAndVars_.get("NPSCSCharRotateTestcase"),
                                                runMode_,
                                                fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSCharRotateTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSFontScaleTestcase"))
                    {
                    NPSCSFontScaleTestcase tc =
                      new NPSCSFontScaleTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSFontScaleTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSFontScaleTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSVGridLineTestcase"))
                    {
                    NPSCSVGridLineTestcase tc =
                      new NPSCSVGridLineTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSVGridLineTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSVGridLineTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCSCreateDBCSSplfTestcase"))
                    {
                    NPSCSCreateDBCSSplfTestcase tc =
                      new NPSCSCreateDBCSSplfTestcase(systemObject_,
                                               namesAndVars_.get("NPSCSCreateDBCSSplfTestcase"),
                                               runMode_,
                                               fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCSCreateDBCSSplfTestcase");
                    }

                if (allTestcases || namesAndVars_.containsKey("NPSCS5553Testcase"))
                    {
                    NPSCS5553Testcase tc =
                      new NPSCS5553Testcase(systemObject_,
                                           namesAndVars_.get("NPSCS5553Testcase"),
                                           runMode_,
                                           fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPSCS5553Testcase");
                    }

                //////////////////////////////////////     // @A2A Added testcase
                // Line Data Record Writer testcase //
                //////////////////////////////////////
                if (allTestcases || namesAndVars_.containsKey("NPLDRWTestcase"))
                    {
                    NPLDRWTestcase tc =
                      new NPLDRWTestcase(systemObject_,
                                       namesAndVars_.get("NPLDRWTestcase"),
                                        runMode_,
                                        fileOutputStream_, printer_);
                    addTestcase(tc);
                    namesAndVars_.remove("NPLDRWTestcase");
                    }


                // $$$ TO DO $$$
                // Create an 'if' block for each testcase using the following as a template.
                // Replace all "example" occurances with the testcase name.
                // Replace the ExampleTestcase with the name of your testcase.

                //////////////////////
                // Example testcase //
                //////////////////////
/*      $$$ TO DO $$$ - delete this line
                if (allTestcases || namesAndVars_.containsKey("example"))
                    {
                    ExampleTestcase tc =
                      new ExampleTestcase(systemObject_,
                                          namesAndVars_.get("example"), runMode_,
                                          fileOutputStream_);
                    addTestcase(tc);
                    namesAndVars_.remove("example");
                    }
$$$ TO DO $$$ - delete this line */


                // Put out error message for each invalid testcase name.
                for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
                    {
                    System.out.println("Testcase " + e.nextElement() + " not found.");
                    }
                } // end if
            else
                {
                System.out.println( "Unable to add NPJAVA to library list. "
                                    + cmd.getMessageList()[0].getID()
                                    + ": " + cmd.getMessageList()[0].getText());

                }
            } // end try block
        catch (Exception e)
            {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
            }
    } // end createTestcases
}


