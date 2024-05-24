///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400Test.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;

import test.AS400.AS400NewInstance;

import java.util.Enumeration;

/**
Test driver for the AS400 component.
The following testcases can be run:
<ul compact>
<li>AS400NewInstance</li>
<br>
This test exercises the newInstance methods of the AS400 class.  The following methods are verified:
<ul>
<li>public static AS400 newInstance(boolean useSSL, String systemName)
<li>public static AS400 newInstance(boolean useSSL, String systemName, String userId, char[] password, char[] additionalAuthenticationFactor)
<li>public static AS400 newInstance(boolean useSSL, String systemName, String userId, char[] password, String proxyServer)
<li>public static AS400 newInstance(boolean useSSL, String systemName, ProfileTokenCredential profileToken)
<li>public static AS400 newInstance(boolean useSSL, AS400 system)
</ul> 
<li>TBD
</ul> 
**/
public class AS400Test extends TestDriver
{
  static AS400 PwrSys = null;

  public static  String COLLECTION     = "JDDDMTST";

/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      AS400Test as400Test = new AS400Test(args);
      as400Test.init();
      as400Test.start();
      as400Test.stop();
      as400Test.destroy();
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
  public AS400Test()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public AS400Test(String[] args)
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
  		
  		
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);
    // Determine what library to create files, etc. in
    if (testLib_ == null)
    {
      testLib_ = "JTOAS4000";
    }
    
    
    // do some setup
    
    if (!(pwrSys_.getUserId()).equals(""))
    {
      /* For now, use the old methods to get a connection */ 
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
              System.out.println( "To delete "+testLib_+", signon to the system and use the WRKOBJLCK command to end the jobs holding the locks." );
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
              System.out.println( "To delete "+testLib_+", signon to the system and use the WRKOBJLCK command to end the jobs holding the locks." );
            }
          }
          if (!cmd.run("CRTLIB "+testLib_))
	  {
            System.out.println("Setup could not create library "+testLib_+": "+
                                cmd.getMessageList()[0].toString());
	  }

	  if (!cmdRun("GRTOBJAUT OBJ("+testLib_+") OBJTYPE(*LIB) USER("+userId_+") AUT(*ALL)")) {
	      out_.println("CRTOBJAUT failed");
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
    
    

    if (allTestcases || namesAndVars_.containsKey("AS400NewInstance"))
    {
      AS400NewInstance tc =
          new AS400NewInstance(systemObject_,
                  namesAndVars_, runMode_,
                  fileOutputStream_, testLib_, password_, PwrSys);
      testcases_.addElement(tc);
      namesAndVars_.remove("AS400NewInstance");
    }



    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }  // createTestcases
}

