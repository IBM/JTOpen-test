///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTAUDBIasp.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JTA;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import test.JDJSTPTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JTAUDBIasp.

Test more stored procedure parameter types.
**/

public class JTAUDBIasp
extends JDJSTPTestcase
{

  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAUDBIasp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAUDBTest.main(newArgs); 
   }


    // Private data.
    private              Connection     connection_;
   protected  String      VxRxMx;
   protected static boolean jdk14 = false; 
 
   static final String sourcepath = "test";
   static final String exppath =    "test";

   String myname;
   boolean iaspAvailable; 


/**
Constructor.
**/
    public JTAUDBIasp (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JTAUDBIasp",
            namesAndVars, runMode, fileOutputStream,
            password);
	myname = "JTAUDBIasp"; 
    }

    public JTAUDBIasp (AS400 systemObject,
                        String testcaseName,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, testcaseName,
            namesAndVars, runMode, fileOutputStream,
            password);
	myname = testcaseName; 
    }



    int iaspCount = 0;
    int iaspAvailableCount = 0;
    String iaspVariedOff = null; 

  protected void getIaspInfo() throws SQLException {

    iaspCount = 0;
    iaspAvailableCount = 0;
    iaspVariedOff = null;

    Statement stmt = connection_.createStatement();

    try {
      stmt.executeUpdate("CREATE TABLE QGPL.IASPSTATUS (NAME VARCHAR(80), STATUS VARCHAR(80))");
    } catch (Exception e) {
    }
    stmt.executeUpdate("Delete from QGPL.IASPSTATUS");

    StringBuffer shellCommand = new StringBuffer(
        "QSH CMD('system ''wrkcfgsts *dev *asp'' | grep DEV | sed ''s/^ *\\([^ ][^ ]*\\)  *\\([^ ][^ ]*\\)  *\\([^ ].*\\)/db2  \"insert into qgpl.iaspstatus values(''\"''\"''\\1''\"''\"'', ''\"''\"''\\3''\"''\"'')\"/'' | sh')      ");
    while (shellCommand.length() < 210) {
      shellCommand.append("          ");
    }

    try {
      stmt.executeUpdate("CREATE PROCEDURE QGPL.QCMDEXC(IN :CMDSTR VARCHAR(1024),IN :CMDLENGTH DECIMAL(15,5)) EXTERNAL NAME QSYS.QCMDEXC LANGUAGE C GENERAL ");
    } catch (Exception e) {

    }
    CallableStatement cstmt = connection_
        .prepareCall("CALL QGPL.QCMDEXC(?,000000200.00000)");
    String commandString = shellCommand.toString();
    System.out.println("Running " + commandString);
    cstmt.setString(1, commandString);
    cstmt.executeUpdate();
    cstmt.close();

    ResultSet rs = stmt.executeQuery("Select * from QGPL.IASPSTATUS");
    while (rs.next()) {
      iaspCount++;
      String status = rs.getString(2);
      if ("AVAILABLE".equals(status)) {
        iaspAvailableCount++;
      } else {
        if ("VARIED OFF".equals(status)) {
          iaspVariedOff = rs.getString(1);
        }
      }
    }

    stmt.close();
  }

  /**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {

	super.setup(); 

	connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	getIaspInfo(); 
	  if (iaspCount == 0) {
	      iaspAvailable=false;
	  } else {
	      if (iaspAvailableCount > 0) {
		  iaspAvailable = true;
	      } else {
		  if (iaspVariedOff == null) {
		      iaspAvailable = false; 
		  } else { 
 		     // An IASP is varied off.  Vary it on
		      StringBuffer varyCommand = new StringBuffer();
		      varyCommand.append("VRYCFG CFGOBJ(");
			varyCommand.append(iaspVariedOff);
			varyCommand.append(") CFGTYPE(*DEV) STATUS(*ON) "); 

			  // Note:  It is not possible to vary on an IASP from a QSQSRVR job using
			  //        a stored procedure call because the system does not allow the 
			  //        QSQSRVR job to switch to the IASP during vary on.  
			  // The job log has messages like...
			  //   CPDB8EC: ASP group IASP33 not set for thread X'0000000000000003'.
			  //   Cause . . . . . :   An attempt to set the auxiliary storage pool (ASP) group
			  //  for the current thread failed. The reason code is 1.
			  //    The reason codes are:
			  //    1 - Set is not allowed by an active operating system function. For a list
			  //  of these functions, see the help for the SETASPGRP command.
        // 
		      try { 

		        AS400 as400 = new AS400("localhost"); 
		        CommandCall cmdCall = new CommandCall(as400);
		        String commandString = varyCommand.toString(); 
		        boolean runOk = cmdCall.run(commandString);
		        if (!runOk) {
		          System.out.println("Command failed: "+commandString); 
		          AS400Message[] messageList = cmdCall.getMessageList();
		          if (messageList != null) { 
		             for (int i = 0; i < messageList.length; i++) { 
		               System.out.println(messageList[i]); 
		             }
		          }
              throw new SQLException("Varyon failed"); 
		        }
	          getIaspInfo(); 

	          long endTime = System.currentTimeMillis() + 10 * 60 * 1000; 
	          while (iaspAvailableCount == 0 &&
	           System.currentTimeMillis() < endTime ) {
	            System.out.println("Waiting for IAP to come up");
	            Thread.sleep(15000);
	            getIaspInfo(); 
	          } 


	          if (iaspAvailableCount > 0) {
	        iaspAvailable = true;
	          } else {
	        System.out.println("Error.. IASP did not vary on within 10 minutes"); 
	        iaspAvailable = false;
	          } 
		      } catch (Exception e) { 
		         System.out.println("Error.. Varyon processing failed .. sleeping for 120 seconds ");
		         e.printStackTrace(); 
		         Thread.sleep(120000); 
		          iaspAvailable = false;
		      }

		  } /* iaspVariedOff is set */ 
	      } /*avaiable Count is 0 */ 
	  } /* count of IASP  is not zero */ 


        setSourcepath(sourcepath);
        setExppath(exppath);

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
    }

    public void doit(String test, String message) {
	try {
	    Run(test);
	    assertCondition(true);
	}
	catch(Exception e) {
	    failed(e, "Unexpected Exception: " + message);
	}
    } 

    
   /**
    *
    */
    public void Var001() {
	if (vrm >= 530 ) {
	    if (iaspAvailable) {
		if (System.getProperty("java.home").indexOf("openjdk") >= 0) {
		    notApplicable("Does not run in openjdk "); 
		} else {
		    doit("test.JTA.JTAUseIASP.java", "Make sure the current user has access to JDIASP33.IASPCOMMIT2 on the IASP.");
		}
	    } else {
		notApplicable("NO IASP AVAILABLE ON SYSTEM"); 
	    } 
	} else {
	   notApplicable();
	} 
    } 



}
