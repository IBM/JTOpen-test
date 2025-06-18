///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  BaseHandler.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.servlet;

import java.util.*;

public class BaseHandler {

  public static String getArg(String keyword,  Map<String, String[]> parameterMap) {
    String[] values = parameterMap.get(keyword); 
    if (values == null) { return null; }
    if (values.length==0) { return ""; }
    return values[0];
  }
    
  
  /*
   * Replace any HTML characters with appropriate escape 
   */
  public static String escapeHtml(String contents) {
    
  if (contents.indexOf('<') >= 0)  {
    contents = contents.replace("<","&lt;");
  }
  
  if (contents.indexOf('>') >= 0)  {
    contents = contents.replace(">","&gt;");
  }

  if (contents.indexOf('&') >= 0)  {
    contents = contents.replace("&","&amp;");
  }
    
  return contents;
}

}
