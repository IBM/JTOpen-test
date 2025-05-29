///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDHandleDump.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDHandleDump.java
//
// Classes:      JDHandleDump
//
////////////////////////////////////////////////////////////////////////


package test;

import java.sql.*;
import java.util.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
public class JDHandleDump {

    JDHandleDumpAnalyzer handleDumpAnalyzer = null;



    // Create a handle dump of the current job using toolbox
    public JDHandleDump(String userid, char[] encryptedPassword ) throws Exception {
	String jobname = (String) JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2Driver", "getJobName");
	System.out.println("jobname is "+jobname);
	int slashIndex = jobname.indexOf("/");
	if (slashIndex < 0) {
	    slashIndex = jobname.indexOf("-");
	}
	String jobNumber = jobname.substring(0,slashIndex);
	loadCliDump("localhost", userid, encryptedPassword, jobNumber);
    }

    public JDHandleDump(String system, String userid, char[] encryptedPassword, String jobNumber) throws Exception  {
	loadCliDump(system, userid, encryptedPassword, jobNumber);
    }


    void loadCliDump(String system, String userid, char[] encryptedPassword, String jobNumber) throws Exception {
	Class.forName("com.ibm.as400.access.AS400JDBCDriver");
	Connection conn;
	char[] charPassword = PasswordVault.decryptPassword(encryptedPassword);
	AS400 as400 = new AS400(system,userid,charPassword); 
	PasswordVault.clearPassword(charPassword);
	AS400JDBCDriver driver = new AS400JDBCDriver(); 
	conn  = driver.connect(as400); 
	

	Statement stmt = conn.createStatement();

	String sql = "CALL QSYS.QCMDEXC('crtpf file(qtemp/x) rcdlen(200)  SIZE(10000 1000 30000)           ', 0000000060.00000)";
	stmt.executeUpdate(sql);
	sql = "CALL QSYS.QCMDEXC('ovrdbf file(QPRINT) tofile(qtemp/x) lvlchk(*NO)  OVRSCOPE(*JOB)         ', 0000000070.00000)";
	stmt.executeUpdate(sql);
	sql = "CALL QSYS.QSQDMPHA ('"+jobNumber+"')";
	stmt.executeUpdate(sql);

	sql = "select CAST(X AS CHAR(200) CCSID 37) from qtemp.x";

	ResultSet rs =  stmt.executeQuery(sql);

	handleDumpAnalyzer = new JDHandleDumpAnalyzer(rs);

	rs.close();

    }


    public boolean isConnectionHandleUsed(int connectionHandle) {

	return (null != handleDumpAnalyzer.connectionsHashtable.get(""+connectionHandle));
    }

    public void dumpConnectionHandles(StringBuffer sb) {
	Enumeration<String> enumeration = handleDumpAnalyzer.connectionsHashtable.elements();
	while (enumeration.hasMoreElements()) {
	    sb.append(enumeration.nextElement());
	    sb.append("\n");
	}
    }


    public void printSummary() {


	    System.out.println("CONNECTIONS = "+ handleDumpAnalyzer.connectionCount);
	    System.out.println("       min  = "+ handleDumpAnalyzer.minConnection);
	    System.out.println("       max  = "+ handleDumpAnalyzer.maxConnection);
	    System.out.println("STATEMENTS  = "+ handleDumpAnalyzer.statementCount);
	    System.out.println("       min  = "+ handleDumpAnalyzer.minStatement);
	    System.out.println("       max  = "+ handleDumpAnalyzer.maxStatement);
	    System.out.println("DESCRIPTORS = "+ handleDumpAnalyzer.descriptorCount);
	    System.out.println("       min  = "+ handleDumpAnalyzer.minDescriptor);
	    System.out.println("       max  = "+ handleDumpAnalyzer.maxDescriptor);
	    System.out.println("TOTAL       = "+ (handleDumpAnalyzer.connectionCount + handleDumpAnalyzer.statementCount + handleDumpAnalyzer.descriptorCount ));
	    System.out.println("       min  = "+ handleDumpAnalyzer.minHandle);
	    System.out.println("       max  = "+ handleDumpAnalyzer.maxHandle);

	    System.out.println("Statements with invalid connection = " +	handleDumpAnalyzer.statementNoConnectionCount);
	    System.out.println("Descriptors with invalid connection = "+ handleDumpAnalyzer.descriptorNoConnectionCount);
	    System.out.println("Descriptors with no statements = "     +handleDumpAnalyzer.descriptorNoStatementCount);


    }


    public static String normalizeInteger(String input) {
	return ""+Integer.parseInt(input);
    }

    public static boolean debug = false;

    public static String[] getColumns(String line, int columnCount) {
	String[] output = new String[columnCount];
	int i = 0;
	int spaceIndex = line.indexOf(" ");
	while (i < columnCount && spaceIndex > 0) {
	    String part = line.substring(0,spaceIndex);
	    output[i] = part;
	    line = line.substring(spaceIndex).trim();
	    spaceIndex = line.indexOf(" ") ;
	    i++;
	}
	if (i < columnCount) {
	    output[i] = line;
	}
	return output;
    }

    public static void main(String[] args) {
	if (args.length < 4) {
	    System.out.println("Usage:   java test.JDHandleDump <system> <userid> <password> <JOBNUMBER>");
	    System.exit(1);
	} else {
	    try {
	      
	      char[] encryptedPassword = PasswordVault.getEncryptedPassword(args[2]);
		JDHandleDump handleDump = new JDHandleDump(args[0], args[1], encryptedPassword, args[3]);

		handleDump.printSummary();

		System.out.println("isConnectionHandleUsed(2):"+handleDump.isConnectionHandleUsed(2));
		System.out.println("isConnectionHandleUsed(3):"+handleDump.isConnectionHandleUsed(3));
		System.out.println("isConnectionHandleUsed(4):"+handleDump.isConnectionHandleUsed(4));
		System.out.println("isConnectionHandleUsed(5):"+handleDump.isConnectionHandleUsed(5));
		System.out.println("isConnectionHandleUsed(6):"+handleDump.isConnectionHandleUsed(6));
		System.out.println("isConnectionHandleUsed(7):"+handleDump.isConnectionHandleUsed(7));
		System.out.println("isConnectionHandleUsed(8):"+handleDump.isConnectionHandleUsed(8));



	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }


}
