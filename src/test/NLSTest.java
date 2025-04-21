///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Vector;
import java.util.Arrays;
import java.util.Enumeration;
import java.io.IOException;
import java.lang.Integer;
import com.ibm.as400.access.AS400;
///import javasoft.sqe.tests.ibm.jdbc.JDTestDriver;
///import javasoft.sqe.tests.ibm.jdbc.NLSJDBCTestcase;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.Job;

import test.NLS.NLSCmdTestcase;
import test.NLS.NLSDATestcase;
import test.NLS.NLSDDMTestcase;
import test.NLS.NLSDQTestcase;
import test.NLS.NLSIFSTestcase;
import test.NLS.NLSJDBCTestcase;
import test.NLS.NLSMessageFileTestcase;
import test.NLS.NLSMessageQueueTestcase;
import test.NLS.NLSNPTestcase;
import test.NLS.NLSSysvalTestcase;
import test.NLS.NLSUserSpaceTestcase;
import test.NLS.NLSUserTestcase;

/**
Test driver for the NLS component.
**/
public class NLSTest extends TestDriver
{
  
  boolean cleanupFirst_ = false;
  public String pwrUid_ = userId_;  // userid with SECADM authority
  public String pwrPwd_ = password_;  // password associated with pwrUid_
  public String ifsDir_ = null;  // name of local directory, which is mapped to an IFS directory
  public static AS400 PwrSys = null;  // power system - profile thru which to do cleanup
  public static String[] args_ = null; // @E1A

  static final int MAXTOKENS = 5; // maximum tokens interpreted in "-misc" parameter

/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      args_ = args; // @E1A
      NLSTest nls = new NLSTest(args);
      nls.init();
      nls.start();
      nls.stop();
      nls.destroy();
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

       System.exit(0);
  }

/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public NLSTest()
       throws Exception
  {
    super();
  }

/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public NLSTest(String[] args)
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
	   
  	
  	if ((pwrSys_.getUserId()).equals(""))
    {               	
      	System.out.println("Warning: -pwrSys option not specified. Some " +
        "variations may fail.");    	
    }
  	else
  	{
      char[] pwrSysCharPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
   PwrSys = new AS400(systemObject_.getSystemName(), pwrSysUserID_, pwrSysCharPassword);  
   Arrays.fill(pwrSysCharPassword, ' ');
  
  	}
  
  	
	
	if (printer_ == null)
    {               	
      	System.out.println("Warning: -printer option not specified  " +
        "variations may fail.");    	
    }
	
		
	ifsDir_ = IFSFile.separator;
	

	cleanupFirst_ = cleanup_ ;
	
	
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);

    
    //No need to call parseMiscParams since -misc is no longer used
    // Interpret the -misc parameter.
    //parseMiscParm();
    

    if (allTestcases || namesAndVars_.containsKey("NLSCmdTestcase"))
    {
      NLSCmdTestcase tc =
        new NLSCmdTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSCmdTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSCmdTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSDDMTestcase"))
    {
      NLSDDMTestcase tc =
        new NLSDDMTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSDDMTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSDDMTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSDQTestcase"))
    {
      NLSDQTestcase tc =
        new NLSDQTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSDQTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSDQTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSIFSTestcase"))
    {
      NLSIFSTestcase tc =
        new NLSIFSTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSIFSTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSIFSTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSJDBCTestcase"))
    {
      NLSJDBCTestcase tc = new NLSJDBCTestcase(systemObject_,                           
                     namesAndVars_, runMode_, fileOutputStream_,
                                                        password_);

      // Kludge this testcase to work like the other JDBC testcases.                       @E1A
      class Kludge extends JDTestDriver {                                               // @E1A
              public Kludge(AS400 systemObject) throws Exception  {                     // @E1A
                  super(args_);                                                         // @E1A
                  super.systemObject_ = systemObject;                                   // @E1A
              }                                                                         // @E1A
              public void createTestcases2 () { }                                    // @E1A
              public void setup() throws Exception { super.setup(); }                   // @E1A
      };                                                                                // @E1A
      Kludge td = null;                                                                 // @E1A
      try {                                                                             // @E1A
        td = new Kludge(systemObject_);                                                 // @E1A
        td.setup();                                                                     // @E1A
      }                                                                                 // @E1A
      catch(Exception ignore) { }                                                       // @E1A
      tc.setTestDriver(td);                                                             // @E1A
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSJDBCTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSMessageQueueTestcase"))
    {
      NLSMessageQueueTestcase tc =
        new NLSMessageQueueTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSMessageQueueTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSMessageQueueTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSMessageFileTestcase"))
    {
      NLSMessageFileTestcase tc =
        new NLSMessageFileTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSMessageFileTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSMessageFileTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSNPTestcase"))
    {
      NLSNPTestcase tc =
        new NLSNPTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSNPTestcase"), runMode_,
                     fileOutputStream_, printer_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSNPTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSUserTestcase"))
    {
      NLSUserTestcase tc =
        new NLSUserTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSUserTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSUserTestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSUserSpaceTestcase"))
    {
      NLSUserSpaceTestcase tc =
        new NLSUserSpaceTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSUserSpaceTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSUserSpaceTestcase");
    }

  
    if (allTestcases || namesAndVars_.containsKey("NLSDATestcase"))
    {
      NLSDATestcase tc =
        new NLSDATestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSDATestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSDATestcase");
    }

    if (allTestcases || namesAndVars_.containsKey("NLSSysvalTestcase"))
    {
      NLSSysvalTestcase tc =
        new NLSSysvalTestcase(systemObject_,
                     (Vector<String>) namesAndVars_.get("NLSSysvalTestcase"), runMode_,
                     fileOutputStream_);
      testcases_.addElement(tc);
      namesAndVars_.remove("NLSSysvalTestcase");
    }

    // Put out error message for each invalid testcase name.
    for (Enumeration<String> e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }

/**
Returns a String of Unicode numbers (in hex) representing the Unicode values
of each of the characters in the source String.
@param source the string to be evaluated
@return a String made up of Unicode hex values of the source, delimited
with spaces; if the source is null or "", that is what is returned.
**/
  public static String stringToUnicode(String source)
  {
    if (source == null || source.length() < 1)
      return source;
    String uniString = new String();
    uniString += (Integer.toHexString((int)source.charAt(0))).toUpperCase();
    for (int i=1; i<source.length(); i++)
    {
      uniString += " ";
      uniString += (Integer.toHexString((int)source.charAt(i))).toUpperCase();
    }
    return uniString;
  }


  
  
/**
Interprets the "-misc" parameter.  Parses it into UID, password, printername, local IFS mapped drive, "no cleanup" option.
For example:    -misc java_power_uid,java_power_pwd,some_printer,/tmp/rchaslzw,noclean
**/
  
  //Changed the parameters to use -pwrsys instead of -misc.
 /* private void parseMiscParm()
  {
	      
    if (misc_ == null)
    {
      System.out.println("-misc option not specified.");
      return;
    }
    StringTokenizer tokenizer = new StringTokenizer(misc_, ",", true);
    String[] tokens = new String[MAXTOKENS];  // note: array entries are init'd to null

    boolean priorWasComma = false;
    int tokenNum = 0;
    while (tokenizer.hasMoreTokens() && tokenNum < MAXTOKENS) {
      String tok = tokenizer.nextToken();
      if (tok.equals(",")) {
        if (priorWasComma || tokenNum==0)
          tokenNum++;  // leave the array entry null
        priorWasComma = true;
      }
      else {
        tokens[tokenNum++] = tok;
        priorWasComma = false;
      }
    }  // while
    int numTokens = tokenNum;  // for clarity

    if ((numTokens >= 1) && (tokens[0] != null)) pwrUid_ = tokens[0];
    if ((numTokens >= 2) && (tokens[1] != null)) pwrPwd_ = tokens[1];
    if ((numTokens >= 3) && (tokens[2] != null)) printer_ = tokens[2];
    if ((numTokens >= 4) && (tokens[3] != null)) ifsDir_ = tokens[3];
    if ((numTokens >= 5) && (tokens[4] != null)) {
      String tok = tokens[4];
      if (tok.toUpperCase().startsWith("NOCLEAN"))
        cleanupFirst_ = false;
      else {
        System.out.println("Ignored extra -misc argument: " + tok);
      }
    }
    if (numTokens > 5) {  // note: we assume MAXTOKENS == 5
      System.out.println("Warning: -misc option specified with more than 5 values. " +
                         "Extra values are ignored.");
    }
    String cleanup = "null";
    if (!cleanupFirst_) { cleanup = "noclean"; }
    System.out.println("-misc parms: " + pwrUid_ + "; xxxx; " + printer_
                       + "; " + ifsDir_ + "; " + cleanup );

    // Instantiate the AS400 power system object.
    if (systemName_ != null)
    {
      if (pwrUid_ != null)
      {
        if (pwrPwd_ != null)
        {
          PwrSys = new AS400(systemName_, pwrUid_, pwrPwd_);
        }
        else
        {
          PwrSys = new AS400(systemName_, pwrUid_);
        }
      }
      else
      {
        PwrSys = new AS400(systemName_);
      }
    }
    else
    {
      PwrSys = new AS400();
    }

  }  // parseMiscParm*/

}



