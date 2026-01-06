///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PCUsageTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.PC;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileReader;
import com.ibm.as400.data.ProgramCallDocument;

import test.JDTestcase;
import test.PCTest;
import test.PasswordVault;
import test.Testcase;

/**
 Testcase PCUsageTestcase.
 **/
public class PCUsageTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PCUsageTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PCTest.main(newArgs); 
   }
      

    Connection connection_ = null; 
    String COLLECTION = "PCUTC"; 
    CommandCall cmd_ = null; 
    
    protected void setup() throws Exception {
       super.setup();
       COLLECTION = PCTest.COLLECTION; 


       try { 
         cmd_ = new CommandCall(systemObject_);
	 cmd_.run("DSPLIBL");

      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);

	    AS400 as400 = new AS400(systemName_, userId_, charPassword); 

	 AS400JDBCDriver driver = new AS400JDBCDriver(); 
  PasswordVault.clearPassword(charPassword);
	 connection_ =  driver.connect(as400); 
	 

       } catch (Exception e) { 
         e.printStackTrace(); 
       }
       
    }
    
    
    protected void cleanup() throws Exception {
      super.cleanup();
    }
   
    public void retrieveServerFile(String serverFileName, String localName) throws Exception  {
      IFSFile file = new IFSFile(systemObject_, serverFileName); 
      BufferedReader reader = new BufferedReader( new IFSFileReader(file)) ; 
      PrintWriter writer = new PrintWriter(new FileWriter(localName)); 
     
      String line = reader.readLine(); 
      while (line != null) { 
        writer.println(line); 
        line = reader.readLine();
      }
      reader.close(); 
      writer.close(); 
      
    }

    public void copyLocalFile(String source, String destination) throws Exception {
       BufferedReader reader = new BufferedReader(new FileReader(source)); 
       PrintWriter writer = new PrintWriter(new FileWriter(destination)); 
      
       String line = reader.readLine(); 
       while (line != null) { 
         writer.println(line); 
         line = reader.readLine();
       }
       reader.close(); 
       writer.close(); 
    }
    
    public void compileAndGeneratePcml(String testname) throws Exception {
      String command; 
      String[] stringArray = null; 
      stringArray = JDTestcase.fileToStringArray("test/rpg/"+testname+".rpg"); 
      JDTestcase.stringArrayToSourceFile(connection_, stringArray, COLLECTION, testname);
      
      String currentDirectory=System.getProperty("user.dir"); 
      
      command = "QSYS/crtrpgmod module("+COLLECTION+"/"+testname+") srcfile("+COLLECTION+"/"+testname+")"+
                       " dbgview(*list)  PGMINFO(*PCML) INFOSTMF('/tmp/"+testname+".pcml') ";
      boolean rc = cmd_.run(command);
      if (rc) {
          retrieveServerFile("/tmp/"+testname+".pcml",currentDirectory+"/"+testname+".pcml" ); 
      } else { 
          output_.println("Warning: Failure of "+command);
          // Copy the PCML from the server to the current directory. 
          copyLocalFile("test/rpg/"+testname+".pcml",  currentDirectory+"/"+testname+".pcml");
          
          // Try to create without PCML
          command = "QSYS/crtrpgmod module("+COLLECTION+"/"+testname+") srcfile("+COLLECTION+"/"+testname+")"+
          " dbgview(*list) ";
          rc = cmd_.run(command);
          if (!rc) { 
            output_.println("Warning: Failure of "+command);
          }          
      }

      
      command = "QSYS/crtsrvpgm "+COLLECTION+"/"+testname+" module("+COLLECTION+"/"+testname+") export(*all)"; 
      rc = cmd_.run(command);
      if (!rc) output_.println("Warning: Failure of "+command); 

      
    }

    public static boolean checkString (ProgramCallDocument pcd, String name, String expect, StringBuffer sb)
    throws Exception
    {
       String stringV;
       boolean ok;

       stringV =  pcd.getValue(name).toString();
       
       ok = stringV.trim().equals (expect.trim());
       if (!ok)
       {
          sb.append("For value "+name+"\n"); 
          sb.append(" ---- expect value: " + expect.trim()+"\n");
          sb.append(" ---- actual value: " + stringV.trim()+"\n");
       }
       return ok;
    }

    /**
     Test using RPG to generate PCML. Then call using the PCML. 
     **/
    public void Var001()
    {
	String testname="PDZ410F1"; 
	StringBuffer sb = new StringBuffer(); 
	try {
	  compileAndGeneratePcml(testname); 
	  ProgramCallDocument pcd = new ProgramCallDocument(systemObject_, testname);
	  
	  String path = "/QSYS.LIB/"+COLLECTION+".LIB/"+testname+".SRVPGM";

          pcd.setValue ("STARTTEST.TEMPLIB", COLLECTION);
          pcd.setPath ("STARTTEST", path);
	  boolean rc1 = pcd.callProgram("STARTTEST");
	  
          pcd.setPath  ("VAR1DTZ", path);
	  pcd.setValue ("VAR1DTZ.TIME", "01:02:03");
	  pcd.setValue ("VAR1DTZ.DATE", "1995-12-25");
	  pcd.setValue ("VAR1DTZ.TIMESTAMP", "2025-01-22T03:04:05.654321");
	  pcd.setValue ("VAR1DTZ.OK", "0");
	  boolean rc2 = pcd.callProgram("VAR1DTZ");
	  
	  boolean rc3 = checkString (pcd, "VAR1DTZ.TIME", "12:13:14", sb);
	  boolean rc4 = checkString (pcd, "VAR1DTZ.DATE", "2011-03-16", sb);
	  boolean rc5 = checkString (pcd, "VAR1DTZ.TIMESTAMP",
	                            "2011-03-16 12:13:14.123456", sb);
	  boolean rc6 = checkString (pcd, "VAR1DTZ.OK", "1", sb );

	  assertCondition(rc1 && rc2 && rc3 && rc4 && rc5 && rc6, "STARTTEST="+rc1+" VAR1DTZ="+rc2+"\n"+sb.toString()); 
	  
	} catch (Exception e) {
	    failed(e, "Unexpected exception"); 
	} 
    }


    /**
     Test using RPG to generate PCML. Then call using the PCML. 
     **/
    public void Var002()
    {
	String testname="PDZ410F2"; 
	StringBuffer sb = new StringBuffer(); 
	try {
	  compileAndGeneratePcml(testname); 
	  ProgramCallDocument pcd = new ProgramCallDocument(systemObject_, testname);
	  
	  String path = "/QSYS.LIB/"+COLLECTION+".LIB/"+testname+".SRVPGM";

          pcd.setValue ("STARTTEST.TEMPLIB", COLLECTION);
          pcd.setPath ("STARTTEST", path);
	  boolean rc1 = pcd.callProgram("STARTTEST");
	  
          pcd.setPath  ("VAR2DTZ", path);
	  pcd.setValue ("VAR2DTZ.TIME", "01:02:03");
	  pcd.setValue ("VAR2DTZ.DATE", "1995-12-25");
	  pcd.setValue ("VAR2DTZ.TIMESTAMP", "2025-01-22T03:04:05.654321");
	  pcd.setValue ("VAR2DTZ.OK", "0");
	  boolean rc2 = pcd.callProgram("VAR2DTZ");
	  
	  boolean rc3 = checkString (pcd, "VAR2DTZ.TIME", "01:02:03", sb);
	  boolean rc4 = checkString (pcd, "VAR2DTZ.DATE", "1995-12-25", sb);
	  boolean rc5 = checkString (pcd, "VAR2DTZ.TIMESTAMP",
	                            "2025-01-22 03:04:05.654321", sb);
	  boolean rc6 = checkString (pcd, "VAR2DTZ.OK", "1", sb );

	  assertCondition(rc1 && rc2 && rc3 && rc4 && rc5 && rc6, "STARTTEST="+rc1+" VAR2DTZ="+rc2+"\n"+sb.toString()); 
	  
	} catch (Exception e) {
	    failed(e, "Unexpected exception"); 
	} 
    }


   
}
