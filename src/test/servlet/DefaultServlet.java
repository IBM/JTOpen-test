///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DefaultServlet.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import test.JTOpenTestEnvironment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

/**
 * Servlet implementation class DefaultServlet
 */
/* @WebServlet("/") */ 
public class DefaultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DefaultServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
      void dumpServletInfo(PrintWriter writer, HttpServletRequest request) {
        int parameterCount = 0; 

        writer.append("Default Servlet info updated 2025-06-13 13:17  ");
        writer.append(request.getContextPath());
        writer.append("<br>\n");

        // Map<String, String[]> map = request.getParameterMap(); 
        Enumeration<String> parameterNames = request.getParameterNames(); 
        while (parameterNames.hasMoreElements()) {
          String parameterName=parameterNames.nextElement();
          String parameterValue = request.getParameter(parameterName);
          writer.append("parameter: "+parameterName+" value:"+parameterValue+"<br>\n");
          parameterCount++; 
        }
        writer.append("parameter count="+parameterCount+"<br>\n"); 
        int attributeCount = 0; 
        Enumeration<String> attributeNames = request.getAttributeNames(); 
        while (attributeNames.hasMoreElements()) {
          String attributeName=attributeNames.nextElement();
          Object attributeValue = request.getAttribute(attributeName);
          writer.append("attribute: "+attributeName+" value:"+attributeValue+"<br>\n");
          attributeCount++; 
        }
        writer.append("attribute count="+attributeCount+"<br>\n"); 
        
        String pathInfo = request.getPathInfo();
        writer.append("pathInfo = "+pathInfo+"<br>\n"); 
        
        String servletPath = request.getServletPath(); 
        writer.append("servletPath = "+servletPath+"<br>\n"); 
        
        StringBuffer requestURL = request.getRequestURL();
        writer.append("requestURL = "+requestURL+"<br>\n"); 
        
      }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  PrintWriter writer = response.getWriter();
          
	  // The servlet path is the path we want to use to find the resource. 
	  String servletPath = request.getServletPath(); 
	  
	  if (servletPath.indexOf("/cgi-pase/SchedulerManager.acgi") >= 0) {
	    response.setContentType("text/html"); 
	    schedulerManager(writer, request.getParameterMap()); 
          } else if (servletPath.indexOf("/cgi-pase/JDRunit.acgi") >= 0) {
            response.setContentType("text/html"); 
            jdRunit(writer, request.getParameterMap()); 
          } else if (servletPath.indexOf("/cgi-pase/JDReport.acgi") >= 0) {
            response.setContentType("text/html"); 
            jdReport(writer, request.getParameterMap()); 
          } else if (servletPath.indexOf("/cgi-pase/updateNote.acgi") >= 0) {
            response.setContentType("text/html"); 
            updateNote(writer, request.getParameterMap()); 
          } else if (servletPath.indexOf("/cgi-pase/QueryByTestcase.acgi") >= 0) {
            response.setContentType("text/html"); 
            queryByTestcase(writer, request.getParameterMap()); 
 	  } else if (servletPath.endsWith("/")) { 
	    showDirectory(writer, servletPath);
	  } else  if (servletPath.endsWith(".html")) { 
	      showHtml(writer, servletPath); 
	  } else if (servletPath.indexOf("/out/") == 0) {
	    response.setContentType("text/plain"); 
	    showText(writer, servletPath);     
	  }
	  
	  dumpServletInfo(writer, request); 
	}

	private void schedulerManager(PrintWriter writer, Map<String,String[]> args) {
	  try { 
               SchedulerManager.execute(writer, args);
	  } catch (Throwable e) { 
	    writer.println("<pre>Exception caught"); 
	    e.printStackTrace(writer); 
	    writer.println("</pre>");
	  }
	}

	
	       private void jdRunit(PrintWriter writer, Map<String,String[]> args) {
	          try { 
	               JDRunit.execute(writer, args);
	          } catch (Throwable e) { 
	            writer.println("<pre>Exception caught"); 
	            e.printStackTrace(writer); 
	            writer.println("</pre>");
	          }
	        }

               private void jdReport(PrintWriter writer, Map<String,String[]> args) {
                 try { 
                      JDReport.execute(writer, args);
                 } catch (Throwable e) { 
                   writer.println("<pre>Exception caught"); 
                   e.printStackTrace(writer); 
                   writer.println("</pre>");
                 }
               }

               private void updateNote(PrintWriter writer, Map<String,String[]> args) {
                 try { 
                      UpdateNote.execute(writer, args);
                 } catch (Throwable e) { 
                   writer.println("<pre>Exception caught"); 
                   e.printStackTrace(writer); 
                   writer.println("</pre>");
                 }
               }

               private void queryByTestcase(PrintWriter writer, Map<String,String[]> args) {
                 try { 
                      QueryByTestcase.execute(writer, args);
                 } catch (Throwable e) { 
                   writer.println("<pre>Exception caught"); 
                   e.printStackTrace(writer); 
                   writer.println("</pre>");
                 }
               }


  private void showHtml(PrintWriter writer, String servletPath) {
	  
	  if (servletPath.charAt(0)=='/') servletPath = servletPath.substring(1); 
	  if (File.separatorChar != '/') {
	    servletPath.replace('/', File.separatorChar); 
	  }
	  String filename = JTOpenTestEnvironment.testcaseHomeDirectory+ File.separator+"ct"+ File.separator+ servletPath; 
	  File file = new File(filename); 
	  if (!file.exists()) {
	    writer.println("<hr>File does not exist ==> "+filename); 
	    writer.println("<hr>"); 
	  } else {
	    try { 
	      BufferedReader in = new BufferedReader(new FileReader(filename));
	      String line = in.readLine(); 
	      while (line != null) { 
	        writer.println(line); 
	        line = in.readLine(); 
	      }
	      in.close(); 
	    } catch (Exception e) { 
	      writer.println("<pre>");
	      e.printStackTrace(writer);
	      writer.println("</pre>");
	    }
	  }
	  

	  
  }

  private void showText(PrintWriter writer, String servletPath) {
    
    if (servletPath.charAt(0)=='/') servletPath = servletPath.substring(1); 
    if (File.separatorChar != '/') {
      servletPath.replace('/', File.separatorChar); 
    }
    String filename = JTOpenTestEnvironment.testcaseHomeDirectory+ File.separator+"ct"+ File.separator+ servletPath; 
    File file = new File(filename); 
    if (!file.exists()) {
      writer.println("File does not exist ==> "+filename); 
      writer.println(""); 
    } else {
      try { 
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = in.readLine(); 
        while (line != null) { 
          writer.println(line); 
          line = in.readLine(); 
        }
        in.close(); 
      } catch (Exception e) { 
        writer.println("----------------------------------------------");
        e.printStackTrace(writer);
        writer.println("----------------------------------------------");
      }
    }
    

}


  private void showDirectory(PrintWriter writer, String servletPath) {
    // TODO Auto-generated method stub
    
  }


  /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
