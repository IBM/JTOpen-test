///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  GenCharConverterMapping.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import com.ibm.as400.access.ConversionMaps;

public class GenEncodingCcsidConversionMap {
  public static void main(String args[]) {
    System.out.println("Usage:  java.GenEncodingCcsidConversionMap <system> <userid> <password>");
    System.out.println("This is a program that generates code for the encodingCcsid_ initialization in ConversionMaps");
    System.out.println("If the ccsid wasn't currently found in ConversionMaps, it is check to see if it is supported on the system"); 
    System.out
        .println("It was last used to generate code using /QOpenSys/QIBM/ProdData/JavaVM/jdk80/64bit JVM on the IBM i");

    String system = null;  
    String userid = null;  
    String password = null; 
    if (args.length>0) { 
      system = args[0]; 
    }
    if (args.length>1) { 
      userid = args[1]; 
    }
    if (args.length>2) { 
      password= args[2]; 
    }

    try { 
    Connection connection;
      
    if (system == null) { 
      System.out.println("No system specified attempting to connect with localhost"); 
      connection = DriverManager.getConnection("jdbc:as400:localhost"); 
    } else {
      if (password == null) { 
        System.out.println("Error:  password not set "); 
        return; 
      }
      connection = DriverManager.getConnection("jdbc:as400:"+system,userid,password); 
    }
    Statement stmt = connection.createStatement(); 
    // get the conversion maps for the current JVM
    SortedMap<String, Charset> charsetMap = Charset.availableCharsets();
    Hashtable<String,Vector<String>> charsetAliases = new Hashtable<String,Vector<String>>(); 
    SortedMap<Integer, String> ccsidCharsetMap = new TreeMap<Integer, String>();
    // Build the mapping from the charset name to the CCSID.

    Set<String> keySet = charsetMap.keySet();
    Iterator<String> i = keySet.iterator();
    while (i.hasNext()) {
      String ccsid = null;
      String name = (String) i.next();
      int intCcsid = 0; 
      Vector<String> aliasList = new Vector<String>(); 
      ccsid = (String) ConversionMaps.encodingCcsid_.get(name);
      String aliasString = "";
      Charset cs = (Charset) charsetMap.get(name);
      Set<String> aliases = cs.aliases();
      Iterator<String> i2 = aliases.iterator();
      while (i2.hasNext()) {
        String alias = i2.next();
        aliasList.add(alias);
        aliasString += " " + alias;
        if (ccsid == null) 
            ccsid = (String) ConversionMaps.encodingCcsid_.get(alias);
        if (ccsid == null) {
          if (alias.indexOf("cp") == 0) {
            alias = "Cp" + alias.substring(2);
            ccsid = (String) ConversionMaps.encodingCcsid_.get(alias);
            if (ccsid != null) { 
              aliasList.add(alias);
              aliasString += " " + alias;
            }
          } else {
            if (alias.indexOf("-") >0) {
              alias = alias.replace('-','_'); 
              ccsid = (String) ConversionMaps.encodingCcsid_.get(alias);
              if (ccsid != null) { 
                aliasList.add(alias);
                aliasString += " " + alias;
              }
              
            } else if (alias.indexOf("iso") == 0) {
              alias = alias.toUpperCase(); 
              ccsid = (String) ConversionMaps.encodingCcsid_.get(alias);
              if (ccsid != null) { 
                aliasList.add(alias);
                aliasString += " " + alias;
              }
              
            } else  if (Character.isDigit(alias.charAt(0))) {
              // Convert to a number then check if valid CCSID on the system
              try {
                intCcsid = Integer.parseInt(alias);

              } catch (NumberFormatException nfe) {
                // Just ignore
              }
            }
          }
        }
      }
      charsetAliases.put(name, aliasList); 
      if (ccsid == null) { 
        if (intCcsid > 0)  {
          System.out.println("Found int CCSID "+intCcsid+"  for charset "+name+":"+aliasString); 
          // see if CCSID is valid
          String sql = null; 
          try { 
            sql = "CREATE TABLE QTEMP.TABLE"+intCcsid+" (C1 CHAR(100) CCSID "+intCcsid+")";   
            stmt.execute(sql); 
            ccsid = ""+intCcsid; 
          } catch (Exception e) { 
            try {
              sql = "CREATE TABLE QTEMP.TABLE"+intCcsid+" (C1 GRAPHIC(100) CCSID "+intCcsid+")";   
              stmt.execute(sql); 
              ccsid = ""+intCcsid; 
            } catch (Exception e2) { 
              System.out.println("sql failed:"+sql); 
              e2.printStackTrace(); 
              intCcsid = 0; 
            }
          }
        }
      }
      if (ccsid == null) { 
        System.out.println("Unable to find CCSID for charset "+name+":"+aliasString); 
      } else {
        Integer ccsidInteger = new Integer(Integer.parseInt(ccsid));
        String existing = ccsidCharsetMap.get(ccsidInteger); 
        if (existing == null) { 
          ccsidCharsetMap.put(ccsidInteger, name);
        } else {
          System.out.println("WARNING:  ccsid "+ccsidInteger+" already defined as "+existing+" for "+name); 
          Vector<String> existingAliasList = charsetAliases.get(existing); 
          existingAliasList.add(name); 
          existingAliasList.addAll(aliasList); 
        }
      }
    }
    
    //
    // Now create the entries listed in CCSID order
    // 
    StringBuffer finalSb = new StringBuffer(); 
    Set<Integer> ccsidKeySet = ccsidCharsetMap.keySet();
    Iterator<Integer> iCcsid = ccsidKeySet.iterator();
    int mappingCount = 0; 
    while (iCcsid.hasNext()) {
      Integer ccsidInteger = iCcsid.next();
      String bestName = ccsidCharsetMap.get(ccsidInteger);
      Vector<String> aliasVector = charsetAliases.get(bestName); 
      Enumeration<String> enumeration = aliasVector.elements();
      while (enumeration.hasMoreElements()) {
        String alias = enumeration.nextElement(); 
        finalSb.append("   encodingCcsid_.put(\""+alias+"\", \""+ccsidInteger+"\");\n"); 
        mappingCount++; 
      }
      finalSb.append("   encodingCcsid_.put(\""+bestName+"\", \""+ccsidInteger+"\");\n\n"); 
      mappingCount++; 
    }
    System.out.println("// entry count = "+mappingCount );
    System.out.println(finalSb.toString());

    } catch (Exception e) { 
      e.printStackTrace(); 
    }
    System.exit(1); 
    
  }

}
