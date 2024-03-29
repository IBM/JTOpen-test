///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400CertificateTest.java
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
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import test.MiscAH.AS400CertificateUsrPrfBeans;
import test.MiscAH.AS400CertificateUsrPrfTestcase;
import test.MiscAH.AS400CertificateVldlBeans;
import test.MiscAH.AS400CertificateVldlTestcase;

import java.util.StringTokenizer;

/**
Test driver for the AS400Certificate component.
For security tests to be run in unattended mode, a AS400 userid/password
with *SECADM authority must be passed on the -misc parameter (ie -misc uid,pwd).
If not specified, these tests will not be attempted in unattended mode.
If not specified and running attended, a sign-on will be displayed.
A AS400 userid/password with *SECADM authority with *ALLOBJ must be used.

The user (-uid) profile provided with the -system parm should be a
USRCLS usrprf with no special authority to permit the
AS400CertificateUsrPrfTestcase to run. No other special authorities
should be given to it or security failures will occurr.

For example -
RUNJVA  CLASS(test.AS400CertificateTest) PARM('-system' 'rchasjew' '-uid'  'DENNISSUSR' '-pwd' 'xxxx' '-run' 'u' '-misc' 'JAVACTL,xxxx') OPTIMIZE(10)



See TestDriver for remaining calling syntax.
@see TestDriver
**/
public class AS400CertificateTest extends TestDriverApplet
{
    public static AS400 PwrSys = null;



/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public AS400CertificateTest(String[] args)
       throws Exception
  {
    super(args);
  }

/**
Cleanup some of the AS400 objects created during the test.
 @exception  Exception  If an exception occurs.
**/
  public void cleanup()
    throws Exception
  {
     try
     {
        CommandCall cmd = new CommandCall(PwrSys);
	TestDriver.deleteLibrary(cmd, "CERTTEST");

        if(cmd.run("DLTUSRPRF USRPRF(CERTTEST) OWNOBJOPT(*DLT)") == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
        }
     }
     catch(Exception e)
     {
        System.out.println("Cleanup failed. " + e);
        throw e;
     }
  }

/**
Creates Testcase objects for all the testcases in this component.
**/
  public void createTestcases()
  {
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);

    // do some setup
    if (misc_!=null)
    {
      StringTokenizer miscTok = new StringTokenizer(misc_, ",");
      String uid = miscTok.nextToken();
      String pwd = miscTok.nextToken();
      char[] encryptedPwd = PasswordVault.getEncryptedPassword(pwd); 
      if (runMode_!=Testcase.ATTENDED)
      {
        char[] decryptedPassword = PasswordVault.decryptPassword(encryptedPwd);
        PwrSys = new AS400( systemObject_.getSystemName(), uid, decryptedPassword );
        PasswordVault.clearPassword(decryptedPassword);
        try {
          PwrSys.setGuiAvailable(false);
        } catch (Exception e) {}
      }
    }
    else if (runMode_!=Testcase.UNATTENDED)
    {
        PwrSys = new AS400( systemObject_.getSystemName(), "QSECOFR" );
    }

    try {
        CommandCall cmd = new CommandCall(PwrSys);
        if(cmd.run("CRTLIB LIB(CERTTEST)") == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
            if (!messageList[0].getID().equals("CPF2111")) // Not "Already exists"
               throw new IOException(messageList[0].toString());
     }

        if(cmd.run("CRTUSRPRF USRPRF(CERTTEST) PASSWORD(JTEAM1) TEXT('dennis  schroepfer 3-3073')") == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
            throw new IOException(messageList[0].toString());
        }
        PwrSys.disconnectService(AS400.COMMAND);
     }
     catch (Exception e)
     {
        System.out.println("Setup failed. " + e);
        e.printStackTrace();
     }

    if (allTestcases) {    }

    // Repeat the following 'if' block for each testcase.

    if (allTestcases || namesAndVars_.containsKey("AS400CertificateVldlBeans"))
    {
      AS400CertificateVldlBeans tc =
        new AS400CertificateVldlBeans(systemObject_,
                      namesAndVars_.get("AS400CertificateVldlBeans"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("AS400CertificateVldlBeans");
    }


   if (allTestcases || namesAndVars_.containsKey("AS400CertificateVldlTestcase"))
    {
      AS400CertificateVldlTestcase tc =
        new AS400CertificateVldlTestcase(systemObject_,
                     namesAndVars_.get("AS400CertificateVldlTestcase"), runMode_,
                     fileOutputStream_, "e");
      testcases_.addElement(tc);
      namesAndVars_.remove("AS400CertificateVldlTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("AS400CertificateUsrPrfTestcase"))
    {
      AS400CertificateUsrPrfTestcase tc =
        new AS400CertificateUsrPrfTestcase(systemObject_,
                     namesAndVars_.get("AS400CertificateUsrPrfTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("AS400CertificateUsrPrfTestcase");
    }



    if (allTestcases || namesAndVars_.containsKey("AS400CertificateUsrPrfBeans"))
    {
      AS400CertificateUsrPrfBeans tc =
        new AS400CertificateUsrPrfBeans(systemObject_,
                      namesAndVars_.get("AS400CertificateUsrPrfBeans"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("AS400CertificateUsrPrfBeans");
    }


    // cleanup AS400 objects and shut down servers
    if (PwrSys != null)
    {
        PwrSys.disconnectService(AS400.COMMAND);
    }
    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }
}

