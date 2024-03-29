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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDHostname.java
//
// Classes:      JDHostname
//
// Return the host name and normalize the name as needed.
//
////////////////////////////////////////////////////////////////////////
package test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector; 
import java.io.*;

public class JDHostName {
  static String INI_FILE="ini/hostname.ini"; 
  static Properties renameProperties = null;

  static void initializeProperties() { 
    if (renameProperties == null) {
      renameProperties = new Properties();
      File f = new File(INI_FILE);
      if (f.exists()) {
        FileInputStream fileInputStream;
        try {
          fileInputStream = new FileInputStream(f);
          renameProperties.load(fileInputStream);
          fileInputStream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    
  }
  
  /** Return the current host name in lower case with the domain removed.
   *  The name is normalize using the ini/hostname.ini properties file
   */ 
  public static String getHostName() {
    String hostname = null;
    initializeProperties(); 
    try {
      hostname = InetAddress.getLocalHost().getHostName().toLowerCase();
    } catch (UnknownHostException e) {
      hostname = "UNKNOWN:" + e.toString();
    }
    int dotIndex = hostname.indexOf('.');
    if (dotIndex > 0) {
      hostname = hostname.substring(0, dotIndex);
    }

    // normalize the hostname
    hostname = renameProperties.getProperty(hostname, hostname);

    return hostname;
  }
  /* Return the known aliases for the current host name*/ 
  public static String[]  getAliases(String hostname) { 
    initializeProperties();
    Vector<String> aliases = new Vector<String>(); 
    Enumeration<Object> keys = renameProperties.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement(); 
      String value = renameProperties.getProperty(key); 
      if (value.equals(hostname)) {
        aliases.add(key); 
      }
    }
    int aliasCount = aliases.size(); 
    String[] answer = new String[aliasCount]; 
    for (int i = 0; i < aliasCount; i++) { 
      answer[i] = (String) aliases.elementAt(i); 
    }
    return answer; 
  }
  
  public static void main(String[] args) { 
    String hostname = getHostName(); 
    System.out.println("hostname is "+hostname); 
    System.out.println("ini file is "+INI_FILE); 
    String[] aliases = getAliases(hostname); 
    System.out.println("There are "+aliases.length+" aliases for "+hostname); 
    for (int i = 0; i < aliases.length; i++) { 
      System.out.println("  " +aliases[i]); 
    }
  }
}
