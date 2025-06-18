///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UpdateNote.java
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

import test.JTOpenTestEnvironment;

public class UpdateNote extends BaseHandler {


  public static void execute(PrintWriter printWriter, Map<String, String[]> parameterMap) throws Throwable {
	try {
	    StringBuffer sb = new StringBuffer();
	    sb.append("Content-Type: text/html\r\n\r\n");
	    sb.append("<html><head><TITLE>Update Note</TITLE></head><body><h1>Update Note</h1>\n");


	    String noteFilename = getArg("FILE", parameterMap);
	    String test = getArg("TEST", parameterMap);
	    String note= getArg("NOTE", parameterMap);
	    String update= getArg("UPDATE", parameterMap);



	    if (update == null) { 
		sb.append("<form>\n"); 
		sb.append("<table>\n");
		sb.append("<td>FILE(s*) <br><td><INPUT NAME=\"FILE\" VALUE=\""+noteFilename+"\" size = 25><tr>\n");
		sb.append("<td>TEST <br><td><INPUT NAME=\"TEST\" VALUE=\""+test+"\" size = 25><tr>\n");
		sb.append("<td>NOTE <br><td><INPUT NAME=\"NOTE\" VALUE=\""+note+"\" size = 80><br><tr>\n");
		sb.append("<td>UPDATE <br><td><INPUT NAME=\"UPDATE\" VALUE=\"Y\" size = 5><tr>\n");
		sb.append("<tr>\n");
		sb.append("</table>\n");
		sb.append("<INPUT TYPE=\"submit\" VALUE=\"GO\"><p>\n");
		sb.append("</form>\n");
	    } else {
		int starIndex = noteFilename.indexOf("*"); 
		if (starIndex >= 0) {
		    File curdir = new File(JTOpenTestEnvironment.testcaseHomeDirectory+File.separator+"ct");
		    File[] files = curdir.listFiles() ;
		    noteFilename = noteFilename.substring(0,starIndex); 
		    for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			if (name.indexOf(noteFilename) == 0) { 
			    sb.append("UPDATING "+name+"<br>\n");
			    String contents = updateNoteFile(name,
							     test,
							     note); 
			    sb.append("UPDATE MADE NOTE FILE CONTAINS <br><pre>\n"); 
			    sb.append(contents);
			    sb.append("</pre>\n");

			} else {
			    
			    // sb.append("NOT UPDATING "+name+" for "+noteFilename+"<br>\n"); 
			} 
		    } 

		    
		} else { 
		    String contents = updateNoteFile(noteFilename,
						     test,
						     note); 
		    sb.append("UPDATE MADE NOTE FILE CONTAINS <br><pre>\n"); 
		    sb.append(escapeHtml(contents));
		    sb.append("</pre>\n");
		}

	    }



	    sb.append("<HR>DONE<HR></body></html>"); 




	    printWriter.println(sb.toString());
	    printWriter.flush();
	} catch (Exception e) {
	    System.out.println("Exception occurred in update note engine");
	    e.printStackTrace();

	} 
        
    }


    public static String  updateNoteFile(String noteFilename, String test, String note) throws Exception  { 
	File noteFile = new File(JTOpenTestEnvironment.testcaseHomeDirectory+File.separator+"ct"+File.separator+ noteFilename);
	StringBuffer contents = new StringBuffer();
	String line = null; 
	if (noteFile.exists()) {
	    BufferedReader reader = new BufferedReader(new FileReader(noteFile));
	    line = reader.readLine();
	    while (line != null) {
		if (line.startsWith(test+" ")) {
			// skip it 
		} else { 
		    contents.append(line);
		    contents.append("\n");
		}

		line = reader.readLine();
	    }
	    reader.close();

	}
	line = test+" "+note;
	contents.append(line);
	contents.append("\n");

	PrintWriter writer = new PrintWriter(new FileWriter(noteFile));
	writer.print(contents.toString());
	writer.close();
	return contents.toString();
    }


    
}
