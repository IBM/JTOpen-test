///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecAuthTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.FeatureDescriptor;
import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;

import test.Sec.SecPHActionTestcase;
import test.Sec.SecPHMiscTestcase;
import test.Sec.SecPHPropertyTestcase;
import test.Sec.SecPTActionTestcase;
import test.Sec.SecPTMiscTestcase;
import test.Sec.SecPTPropertyTestcase;
import test.Sec.SecUPMiscTestcase;
import test.Sec.SecUPPropertyTestcase;

/**
 Test driver for the security auth package.  This includes the ProfileHandleCredential, ProfileTokenCredential, and UserProfilePrincipal components.
 NOTE TO TESTER: When running natively, specify the same userid on the "-uid" parameter, as the profile under which you are signed-on to the system.
   Otherwise several of the variations will fail with the following error:
   "RetrieveFailedException: Request is not supported."
 **/
public class SecAuthTest extends TestDriver
{
   static final long serialVersionUID = 1L;
    public static  String uid1 = "SECPTTEST1";
    public static  String pwd1 = "SECPT01PWD";
    public static  String uid2 = "SECPTTEST2";
    public static  String pwd2 = "SECPT02PWD";

    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new SecAuthTest(args));
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
     Used for applets.
     @exception  Exception  If initialization errors occur.
     **/
    public SecAuthTest() throws Exception
    {
        super();
    }

    /**
     Used for applications.
     @param  args  Array of command line arguments.
     @exception  Exception  If initialization errors occur.
     **/
    public SecAuthTest(String[] args) throws Exception
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
      		  		systemObject_.connectService(AS400.SIGNON);
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
      	     	    Job[] jobs = systemObject_.getJobs(AS400.SIGNON);
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
    	
    	
        Testcase[] testcases =
        {
            new SecPHPropertyTestcase(),
            new SecPHActionTestcase(),
            new SecPHMiscTestcase(),
            new SecPTPropertyTestcase(),
            new SecPTActionTestcase(),
            new SecPTMiscTestcase(),
            new SecUPPropertyTestcase(),
            new SecUPMiscTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
    }

    public static void createProfiles(AS400 pwrSys) throws Exception {

        CommandCall cmd = new CommandCall(pwrSys);
        String command = "CRTUSRPRF USRPRF(" + uid1 + ") PASSWORD(" + pwd1 + ") USRCLS(*USER) TEXT('JTOpen Test profile')";
        cmd.run(command);
        command = "CRTUSRPRF USRPRF(" + uid2 + ") PASSWORD(" + pwd2 + ") USRCLS(*USER) TEXT('JTOPEN Test profile')";
        cmd.run(command);

    }

    public static void deleteProfiles(AS400 pwrSys) throws Exception {
        CommandCall cmd = new CommandCall(pwrSys);
        cmd.run("DLTUSRPRF USRPRF(" + uid1 + ") OWNOBJOPT(*DLT)");
        cmd.run("DLTUSRPRF USRPRF(" + uid2 + ") OWNOBJOPT(*DLT)");
    }

    /**
     Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void setup() throws Exception
    {
	/* Use the client as part of the name so that */ 
        /* testcases running from differen clients do not conflict */
	/* 60466175 is 36 ^ 5 */ 
	// int intKey1 = InetAddress.getLocalHost().hashCode() % 60466175; 
	// String stringKey1 = Integer.toString(intKey1,36);
	// String stringKey2 = Integer.toString(intKey1 + 1 ,36); 

	uid1 =  Testcase.generateClientUser("SECP1").toUpperCase(); 
	uid2 =  Testcase.generateClientUser("SECP2").toUpperCase(); 


	createProfiles(pwrSys_); 
    }

    /**
     Performs cleanup needed after running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
        // Shut down servers.
        systemObject_.disconnectAllServices();
	deleteProfiles(pwrSys_); 
        if (pwrSys_ != null)
        {
            pwrSys_.disconnectAllServices();
        }
        super.cleanup();
    }

    /**
     Removes a profile token from the server.
     @exception  Exception  If errors occur while removing the credential.
     **/
    public static void removeToken(AS400 sys, byte[] tkn) throws Exception
    {
        ProgramParameter[] parmlist = new ProgramParameter[3];
        parmlist[0] = new ProgramParameter(new byte[] { (byte)0x5C, (byte)0xD7, (byte)0xD9, (byte)0xC6, (byte)0xE3, (byte)0xD2, (byte)0xD5, (byte)0x40, (byte)0x40, (byte)0x40 } ); // *PRFTKN
        parmlist[1] = new ProgramParameter(new byte[] { 0x00, 0x00, 0x00, 0x00 } );
        parmlist[2] = new ProgramParameter(tkn);
        ProgramCall programCall = new ProgramCall(sys, "/QSYS.LIB/QSYRMVPT.PGM", parmlist);
        programCall.setThreadSafe(true); // run on-thread, in case current uid is disabled
        if (!programCall.run())
        {
            String msg = "";
            AS400Message[] list = programCall.getMessageList();
            for (int i = 0; i < list.length; ++i)
            {
                msg = msg + list[i] + "; ";
            }
            throw new Exception("Call to QSYRMVPT failed. >> " + msg);
        }
    }

    /**
     Return true if the byte arrays are equivalent; false otherwise.
     **/
    static public boolean compareBytes(byte[] b1, byte[] b2)
    {
        if (b1.length != b2.length) return false;
        for (int i = 0; i < b1.length; ++i)
        {
            if (b1[i] != b2[i]) return false;
        }
        return true;
    }

    /**
     Verify the given feature names against the descriptors provided.
     @param  fNames  The feature names.
     @param  fDescs  The feature descriptors.
     @return  true if descriptors exist for all the names; otherwise false.
     **/
    public static boolean verifyDescriptors(String[] fNames, FeatureDescriptor[] fDescs)
    {
        boolean success = true;
        for (int i = 0; i < fNames.length && success; ++i)
        {
            String f = fNames[i];
            success = false;
            for (int j = 0; j < fDescs.length && !success; ++j)
            {
                if (f.equals(fDescs[j].getName())) success = true;
            }
            if (!success)
            {
              System.out.print("Feature name |"+f+"| not found in FeatureDescriptor list:");
              for (int ii=0; ii<fDescs.length; ii++)
              {
                System.out.print(" " + fDescs[ii].getName());
              }
              System.out.println();
            }
        }
        return success;
    }
}
