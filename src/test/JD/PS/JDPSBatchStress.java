///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBatchStress.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSBatchStress.java
//
// Classes:      JDPSBatchStress
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDJobName;
import test.JDPSTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSBatchStress.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>addBatch()</li>
</ul>
**/
public class JDPSBatchStress
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSBatchStress";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }


    boolean runningJ9 = false;

    // Private data.
    private static String table_    = null ; /* Set below */
    private static String insert0_  = null; /* Set below */

    

    protected Connection      connection_;

    protected boolean  useBlockInsert = false;			// @L2
    
    int longRunTest = 0; 

/**
Constructor.
**/
    public JDPSBatchStress (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDPSBatchStress",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    public JDPSBatchStress (AS400 systemObject,
		      String testname, 
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        String url = baseURL_;
        connection_ = testDriver_.getConnection (url, userId_, encryptedPassword_);
        connection_.setAutoCommit(true); // for xa

        Statement s = connection_.createStatement ();

        table_      = JDPSTest.COLLECTION + ".JDPSBSTRES";
	insert0_    = "INSERT INTO " + table_ + " VALUES (?)";

	try{
	    s.executeUpdate(" DROP TABLE "+table_);
	}catch(Exception e0){
	}

        s.executeUpdate ("CREATE TABLE " + table_
                         + " (C1 BLOB(1M))");


        connection_.commit(); // for xa
        s.close ();


	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    String vmName = System.getProperty("java.vm.name");
	    if (vmName ==  null) {
		runningJ9 = false; 
	    } else { 
		if (vmName.indexOf("Classic VM") >= 0) {
		    runningJ9 = false;
		} else {
		    runningJ9 = true;
		}
	    }
	}
        

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        Statement s = connection_.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        connection_.commit(); // for xa
        connection_.close ();
    }





/**
addBatch() - Add batches to two different statements.  Should not cause an error.
For the native driver, there should be no warnings about malloc problems in the
job log. 
**/
    public void Var001 ()
    {
	String added  = " -- Added 2009/11/16 by native driver for CPS 7XMLUW";
	String message = "";
	boolean passed = true;

	if (getDriverFixLevel()<= 36699 && runningJ9) {
	    notApplicable("new testcase dumps core for J9 need ptf newer then 36699"+added);
	    return;
	}

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s1 = connection_.prepareStatement (insert0_);
                PreparedStatement s2 = connection_.prepareStatement (insert0_);

		// add batches of 33 / 65 to each connection and check the job log
		byte[] stuff = new byte[30];

		for (int i = 1; i <=256; i = i * 2) {
		    s1.close();
		    s1 = connection_.prepareStatement (insert0_);
		    for (int j = 0; j < i*32+1; j++) {
			s1.setBytes(1,stuff);
			s1.addBatch(); 
		    } 
		    for (int j = 0; j < i*64+1; j++) {
			s2.setBytes(1,stuff);
			s2.addBatch(); 
		    } 
		} 

		s1.close();
		s2.close();
		if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		    (System.getProperty("java.home").indexOf("openjdk")<0)){
		    JDJobName.system(" DSPJOBLOG OUTPUT(*OUTFILE) OUTFILE("+JDPSTest.COLLECTION+"/QPJOBLOG)  ");

		    Statement s = connection_.createStatement();
		    ResultSet rs = s.executeQuery("select QMHMID from "+JDPSTest.COLLECTION+".QPJOBLOG");
		    while (rs.next()) {
			String mid = rs.getString(1);
			/* System.out.println(mid); */ 
			if (mid.indexOf("MCH") >= 0) {
			    passed = false;
			    message+=" FOUND "+mid+" in job log\n";
			    JDJobName.system("CHGJOB LOG(4 00 *SECLVL) "); 
			}

		    } 
		    rs.close();
		    s.close(); 
		    
		} 

		assertCondition(passed, message+added);

            }
            catch (Exception e) {
		failed (e, "Unexpected Exception"+added );
            }
        }
    }

}



