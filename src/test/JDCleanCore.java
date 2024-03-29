///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanCore.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileReader;



public class JDCleanCore {
  
  public static void usage() {
    System.out
        .println("Usage:  java JDCleanCore <system> <userid> <password> ");
    System.out
        .println("        Cleans duplicate core files from / and "+JTOpenTestEnvironment.testcaseHomeDirectory+" and /home/jdpwrsys.");
  }

  public static void main(String args[]) {
    try {
      String system = "localhost";
      String userid = null;
      String password = null;

      if (args.length >= 1) {
        system = args[0];
      }
      if (args.length >= 2) {
        userid = args[1];
        if (userid.equals("null")) {
          userid = null;
        }
      }
      if (args.length >= 3) {
        password = args[2];
        if (password.equals("null")) {
          password = null;
        }
      }

      clean(system, userid, password, System.out);
    } catch (Exception e) {
      e.printStackTrace();
      usage();
    }
  }

  public static Enumeration<IFSFile> getJavacoreList(AS400 as400, String directory) throws IOException {
    IFSFile file = new IFSFile(as400, directory);
    Enumeration<IFSFile> files = file.enumerateFiles("javacore.*"); 
    return files; 
  }

  public static void clean(String system, String userid, String password,
      PrintStream out) throws Exception {
    AS400 as400;
    out.println("Starting clean"); 
    if (userid == null) {
      as400 = new AS400(system);
    } else {
      as400 = new AS400(system, userid, password.toCharArray());
    }
    
    String directories[] = { "/", 
          JTOpenTestEnvironment.testcaseHomeDirectory, 
            "/home/jdpwrsys", "/tmp", "/home/JAVA", 
            JTOpenTestEnvironment.testcaseHomeDirectory+"/ct", "/home/QWLISVR", "/home/jsonTest", "/home/jsontest/bin", "/home/QSRVAGT" }; 

    for (int i = 0; i < directories.length; i++) {
     Hashtable<String,IFSFile> foundProblems = new Hashtable<String,IFSFile>(); 
	   Enumeration<IFSFile> javacoreList = getJavacoreList(as400,directories[i]); 
	   while (javacoreList.hasMoreElements()) {
	     IFSFile javacorefile = (IFSFile) javacoreList.nextElement();
	     
	     String problemName = isKnownProblem(javacorefile); 
	     if (problemName != null) { 
	       if (foundProblems.get(problemName) == null) { 
	         foundProblems.put(problemName, javacorefile); 
	         out.println("Keeping first ("+problemName+")"+javacorefile.toString()); 
	       } else {
	          cleanRelatedFiles(as400, javacorefile); 
            out.println("Deleted "+javacorefile.toString()); 
	       }
	     } else { 
         out.println("Keeping unknown "+javacorefile.toString()); 
	     }
	     
	   }


    }	
    out.println("Completed clean"); 

  }

  private static void cleanRelatedFiles(AS400 as400, IFSFile javacorefile) throws IOException {
    String fullfilename = javacorefile.getCanonicalPath();
    int lastSlashIndex = fullfilename.lastIndexOf("/");
    if (lastSlashIndex >= 0 ) {
      String directory = fullfilename.substring(0,lastSlashIndex+1); 
      String filename = fullfilename.substring(lastSlashIndex+1); 
      int firstDot = filename.indexOf('.'); 
      int secondDot = filename.indexOf('.',firstDot+1);
      int thirdDot = filename.indexOf('.',secondDot+1);
      int fourthDot = filename.indexOf('.',thirdDot+1);
      
      String pattern = "*"+filename.substring(firstDot, fourthDot+1)+"*"; 
      IFSFile directoryFile = new IFSFile(as400, directory);
      Enumeration<IFSFile> files = directoryFile.enumerateFiles(pattern); 
      while (files.hasMoreElements()) { 
        IFSFile file = (IFSFile) files.nextElement(); 
        file.delete(); 
      }
    }
    
    
    
  }

  static String[] problemNames = {
      "Unsupported CCSID int JDJSTPccsid",
      "OutOfMemory in JDCleanSplf",
      "i5/OS information agent",
      "Failed to create thread",
  };
  
  // Search strings in rder they are found in field
  // Once the a string has been found, the algorithm will
  // try to find the remaining strings in the current set
  // without checking the other sets 
  static String[][] problemSearchStrings = {
      {"test/JDJSTPccsid"}, 
      {"java/lang/OutOfMemoryError", "test/JDCleanSplf"},
      {"i5/OS information agent"},
      {"java/lang/OutOfMemoryError\" \"Failed to create a thread"},
  }; 
  
  
  private static String isKnownProblem(IFSFile javacorefile) throws AS400SecurityException, IOException {
    
    String foundProblemName = null;
    String[] currentProblem = null;  
    int foundProblemIndex = 0; 
    int foundProblemSubIndex =  0 ; 
    BufferedReader reader = new BufferedReader( new IFSFileReader(javacorefile)); 
  
    String line = reader.readLine(); 
    while (foundProblemName == null && line != null) {
      if (currentProblem == null  ) {
        for (int i = 0 ; i < problemSearchStrings.length; i++) {
            if (line.indexOf(problemSearchStrings[i][0]) >= 0) {
              currentProblem = problemSearchStrings[i];
              foundProblemIndex = i; 
              foundProblemSubIndex = 1; 
            }
        }
      } else {
        if (foundProblemSubIndex == currentProblem.length) {
          foundProblemName = problemNames[foundProblemIndex]; 
        } else {
          if (line.indexOf(currentProblem[foundProblemSubIndex])>= 0) {
            foundProblemSubIndex++; 
          }
        } 
      }
      line = reader.readLine();
    }
    reader.close(); 
    
    return foundProblemName; 
  
  
  }
}
