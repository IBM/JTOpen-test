///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDReport.java
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

public class JDReport extends BaseHandler {


 
    
    public static void execute(PrintWriter printWriter, Map<String, String[]> parameterMap) throws Throwable {
	try {
	    StringBuffer sb = new StringBuffer();
	    sb.append("Content-Type: text/html\r\n\r\n");
	    sb.append("<html><head><TITLE>JDReport</TITLE></head><body><h1>JDReport</h1>\n");

	    // System.out.println("path is "+path);

	// Parse the parameter from the path



	    String initials = getArg("INITIALS", parameterMap);
	    String days = getArg("DAYS", parameterMap);
	    if (initials == null) initials = ""; 
	    if (days == null) {
		days = ""; 
		sb.append("<form>\n"); 
		sb.append("<table>\n");
		sb.append("<td>INITIALS <br><td><INPUT NAME=\"INITIALS\" VALUE=\""+initials+"\" size = 10><tr>\n");
		sb.append("<td>REGRESSION DAYS <br><td><INPUT NAME=\"DAYS\" VALUE=\""+days+"\" size = 10><tr>\n");
		sb.append("<tr>\n");
		sb.append("</table>\n");
		sb.append("<INPUT TYPE=\"submit\" VALUE=\"GO\"><p>\n");
		sb.append("</form>\n");
	    } else {
		//
		// Run the report command
		//

	        String[] args = {initials}; 
	        test.JDReport.noExit = true; 
	        test.JDReport.main(args); 

	    }

	    sb.append("<HR>Report available at <a href=\"../latest"+initials+".html\">latest"+initials+".html</a>"); 
	    sb.append("<HR>DONE<HR></body></html>"); 

	    printWriter.println(sb.toString());
	    printWriter.flush();
	} catch (Exception e) {
	    System.out.println("Exception occurred in update note engine");
	    e.printStackTrace();

	} 
        
    }



    
}
