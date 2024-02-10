///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTATestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JTA;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCXADataSource;

import test.JDJobName;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.util.Hashtable;
import javax.sql.DataSource;
import javax.sql.XADataSource;



public class JTATestcase
extends JDTestcase
{

   protected boolean isNTS = false; //@PDA move from JTAX children classes
   protected boolean isIasp = false; 
   protected boolean useUDBDataSource=false; 
   static boolean cliTraceEnabled = false; 

   public JTATestcase (AS400 systemObject,
                       String testcaseName,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password,
                       String pwrSysUid,
                       String pwrSysPwd)
   {
       super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream, password, pwrSysUid, pwrSysPwd);

       //
       // Check to see if CLI trace should be turned on
       //
       if (!cliTraceEnabled) { 
        String enableCLITrace = System.getProperty("enableCLITrace");
        if (enableCLITrace != null) {
	   enableCLITrace = enableCLITrace.toUpperCase().trim();
	   if (enableCLITrace.equals("TRUE")) {
	       String jobname = JDJobName.getJobName();
               System.out.println("Enabling CLI trace for "+jobname);
               int slashIndex = jobname.indexOf("/"); 
               
               String jobnum = jobname.substring(0,slashIndex);
               String command = "system CALL QSYS/QP0WUSRT parm('-l 2'  '"+jobnum+"') ";
               System.out.println("Running "+command);
               Runtime rt = Runtime.getRuntime(); 
               try { 
               Process process = rt.exec(command); 
               process.waitFor();
               JDJobName.showProcessOutput(process, null); 
               cliTraceEnabled = true; 
               JDJobName.setJobLogOption(); 
               } catch (Exception e ) {
                 e.printStackTrace(); 
               }
               
	   } 
        } 
       }
   }

   public JTATestcase (AS400 systemObject,
                       String testcaseName,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
       super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream, password);

       //
       // Check to see if CLI trace should be turned on
       //
       if (!cliTraceEnabled) { 
        String enableCLITrace = System.getProperty("enableCLITrace");
        if (enableCLITrace != null) {
	   enableCLITrace = enableCLITrace.toUpperCase().trim();
	   if (enableCLITrace.equals("TRUE")) {
	       String jobname = JDJobName.getJobName();
               System.out.println("Enabling CLI trace for "+jobname);
               int slashIndex = jobname.indexOf("/"); 
               
               String jobnum = jobname.substring(0,slashIndex);
               String command = "system CALL QSYS/QP0WUSRT parm('-l 2'  '"+jobnum+"') ";
               System.out.println("Running "+command);
               Runtime rt = Runtime.getRuntime(); 
               try { 
               Process process = rt.exec(command); 
               process.waitFor();
               JDJobName.showProcessOutput(process, null); 
               cliTraceEnabled = true; 
               JDJobName.setJobLogOption(); 
               } catch (Exception e ) {
                 e.printStackTrace(); 
               }
               
	   } 
        } 
       }
   }



    protected DataSource newDataSource() throws Exception
    {
        DataSource ds;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
        }
        else if (getDriver() == JDTestDriver.DRIVER_JCC) {
            /* TODO.. create the right data source */ 
            ds = null; 
        }

        else {
	    if (useUDBDataSource) {
	    	   ds   = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBDataSource");

		 JDReflectionUtil.callMethod_V(ds, "setDatabaseName",system_);
	    } else { 
 	   ds   = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdDataSource");
		 JDReflectionUtil.callMethod_V(ds, "setDatabaseName",system_);
	    }
        }
        return ds;
    }


    protected XADataSource newXADataSource() throws Exception
    {
        XADataSource xads = null ;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            xads = new AS400JDBCXADataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
            isNTS = true;
        }
	else if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    try { 
		xads = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jcc.DB2XADataSource");
		JDReflectionUtil.callMethod_V(xads, "setDatabaseName", systemObject_.getSystemName());
		JDReflectionUtil.callMethod_V(xads, "setUser", systemObject_.getUserId());
		int intPortNumber = 0;
		String jccPort = System.getProperty("jccPort");
		if (jccPort == null) {
		    intPortNumber = 446;  /* drda port number */ 
		} else {
		    intPortNumber = Integer.parseInt(jccPort); 
		} 
		JDReflectionUtil.callMethod_V(xads, "setPortNumber", intPortNumber); 
		JDReflectionUtil.callMethod_V(xads, "setPassword", PasswordVault.decryptPasswordLeak(encryptedPassword_));
		isNTS = true;
	    } catch (Exception e) {
		e.printStackTrace(); 
	    } 
        }

        else
        {
	    if (useUDBDataSource) {
        xads     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");

        JDReflectionUtil.callMethod_V(xads,"setDatabaseName",system_);
         isNTS = true;
	    } else { 
        xads     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdXADataSource");

        JDReflectionUtil.callMethod_V(xads,"setDatabaseName",system_);
         isNTS = false;
	    }
        }
        return xads;
    }


    protected XADataSource newXADataSource(String systemName, String userId, String password) throws Exception
    {
        XADataSource xads;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            xads = new AS400JDBCXADataSource(systemName, userId, password);
            isNTS = true;
        }
        else {
	    if (useUDBDataSource) {
        xads     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");

		 JDReflectionUtil.callMethod_V(xads, "setDatabaseName",systemName);

		try {
		    JDReflectionUtil.callMethod_V(xads,"setUser",userId);
		    JDReflectionUtil.callMethod_V(xads,"setPassword",password);

		}
		catch(Exception e) {
		    e.printStackTrace();
		}
        isNTS = true;
	    } else { 
        xads     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdXADataSource");
		 JDReflectionUtil.callMethod_V(xads, "setDatabaseName",systemName);
		try {
		    JDReflectionUtil.callMethod_V(xads,"setUser",userId);
		    JDReflectionUtil.callMethod_V(xads,"setPassword",password);
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
        isNTS = false;
	    }
        }
        return xads;
    }


}



