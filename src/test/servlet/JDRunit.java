///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRunit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.servlet;

import java.io.*;
import java.util.*;

public class JDRunit extends BaseHandler {

     
    public static void execute(PrintWriter printWriter, Map<String, String[]> parameterMap) throws Throwable {
	try {
	    StringBuffer sb = new StringBuffer();
	    sb.append("Content-Type: text/html\r\n\r\n");
	    sb.append("<html><head><TITLE>JDRunit</TITLE></head><body><h1>JDRunit</h1>\n");



	    String initials = getArg("INITIALS", parameterMap);
	    String testcase = getArg("TESTCASE", parameterMap);
	    String priority = getArg("PRIORITY", parameterMap);
	    if (priority == null) priority = "1"; 
	    if (initials == null) initials = ""; 
	    if (testcase == null) {
		testcase = ""; 
		sb.append("<form>\n"); 
		sb.append("<table>\n");
		sb.append("<td>INITIALS <br><td><INPUT NAME=\"INITIALS\" VALUE=\""+initials+"\" size = 10><tr>\n");
		sb.append("<td>TESTCASE <br><td><INPUT NAME=\"TESTCASE\" VALUE=\""+testcase+"\" size = 10><tr>\n");
		sb.append("<td>PRIORITY <br><td><INPUT NAME=\"PRIORITY\" VALUE=\""+priority+"\" size = 10><tr>\n");
		sb.append("<tr>\n");
		sb.append("</table>\n");
		sb.append("<INPUT TYPE=\"submit\" VALUE=\"GO\"><p>\n");
		sb.append("</form>\n");
	    } else {
		SchedulerManager.submitProcess(initials, testcase, priority, sb);
		sb.append("<HR>Testcase started <a href=\"SchedulerManager.acgi\">STATUS</a><br>\n");
	    }

	    sb.append("<HR>DONE<HR></body></html>"); 
	    printWriter.println(sb.toString());
	    printWriter.flush();
	} catch (Exception e) {
	    System.out.println("Exception occurred in update note engine");
	    e.printStackTrace();

	} 
        
    }



    
}
