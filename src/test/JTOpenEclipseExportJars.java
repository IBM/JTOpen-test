///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTOpenEclipseExport
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyVetoException;
import java.io.*;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector; 
import com.ibm.as400.access.*;

import test.misc.TestUtilities; 
/**
 * This class is designed to update an IBM i system with the latest JTOpen jar files build in the Eclipse environment.
 * 
 * Build the jar file using maven build with the target: package -f pom-dist.xml -e
 * 
 * C:\Users\JohnEberhard\git\JTOpenWorking\target\jt400-20.0.7.jar
 * 
 */
public class JTOpenEclipseExportJars  {

  public static void usage() { 
    System.out.println("Usage:  java  test.JDTOpenEclipseExport IBMi userid password eclipse directory  ");
    System.out.println("   Updates an IBM i system with the latest JTOpen jar files from the Eclipse Environment");
  }
 
  public static void main(String args[]) {
    try {
      System.out.println("Usage:  java  test.JDTOpenEclipseExport IBMi userid password  JTOpenDirectory  ");
      System.out.println("   Updates an IBM i system with the latest changes in the Eclipse Environment");

      String as400Name = args[0]; 
      String userid = args[1]; 
      String password = args[2]; 
      String JTOpenDir = args[3]; 
      String currentDirectory = System.getProperty("user.dir");
      // Home directory should be the base of the git repository
      String gitDirectory; 
      int lastSep = currentDirectory.lastIndexOf(File.separatorChar);
      gitDirectory = currentDirectory.substring(0,lastSep) + File.separatorChar + JTOpenDir+File.separatorChar +"target"; 
      File gitDirectoryFile = new File(gitDirectory); 
      if (!gitDirectoryFile.exists()) { 
        System.out.println("Error. directory "+gitDirectory+" does not exist.  user.home is not the git root");
      }
      System.out.println("Current directory is "+currentDirectory);
      /* C:\Users\JohnEberhard\git\JTOpenWorking\target\jt400-20.0.7.jar */ 
      
      AS400 as400 = new AS400(as400Name, userid, password.toCharArray()); 
      transferFiles(as400, gitDirectory); 
      System.out.println("Files transferred"); 
      System.out.println("==================================================================================================="); 
      System.out.println("DONE exporting to "+as400Name+" at "+ (new Timestamp(System.currentTimeMillis()))); 
      System.out.println("==================================================================================================="); 
      TestUtilities.beep(); 
    } catch (Exception e) {
      e.printStackTrace(System.out);
      usage(); 
    }

  }


  private static void transferFiles(AS400 as400, String jarDirectory) throws IOException, AS400SecurityException {
    String [][] files = {
        {"java8.jar",         "/qibm/proddata/os400/jt400/lib/java8/jt400.jar"},
        {"java11.jar",        "/qibm/proddata/os400/jt400/lib/java9/jt400.jar"},
        {"native-java8.jar",  "/qibm/proddata/os400/jt400/lib/java8/jt400Native.jar"},
        {"native-java11.jar", "/qibm/proddata/os400/jt400/lib/java9/jt400Native.jar"},
    };
    
    System.out.println("Transferring "+files.length+" jar files"); 
    Hashtable hashtable = new Hashtable(); 
    for (int i = 0; i < files.length; i++ ) { 
      hashtable.put(files[i][0], files[i][1]); 
    }
    
    File jarDirectoryFile = new File(jarDirectory); 
    String jarFiles[] = jarDirectoryFile.list(); 
    for (int i = 0; i < jarFiles.length; i++)  {
      String jarFile = jarFiles[i]; 
      int firstDash = jarFile.indexOf('-');
      if (firstDash > 0) { 
        int secondDash = jarFile.indexOf('-',firstDash+1); 
        if (secondDash > 0) {
          String endPart = jarFile.substring(secondDash+1); 
          String remoteFilename = (String) hashtable.get(endPart); 
          if (remoteFilename != null) {
            String localFilename = jarFile; 
            File localFile = new File(jarDirectory+File.separatorChar+localFilename);
            System.out.println("Transferring "+localFilename+" to "+remoteFilename+" size="+localFile.length());
            IFSFile ifsFile = new IFSFile(as400, remoteFilename);
            ifsFile.delete(); 
            verifyParent(ifsFile); 
            ifsFile.createNewFile(); 
            ifsFile.setCCSID(819); 
            IFSFileOutputStream ifsFileOutputStream = new IFSFileOutputStream(ifsFile);
            FileInputStream fileInputStream = new FileInputStream(jarDirectory+File.separatorChar+localFilename); 
            byte[] buffer = new byte[100000];
            int bytesRead = fileInputStream.read(buffer );
            while (bytesRead >= 0) { 
              ifsFileOutputStream.write(buffer, 0, bytesRead);
              System.out.print(".");
              System.out.flush(); 
              bytesRead = fileInputStream.read(buffer );
            }
            System.out.println(); 
            fileInputStream.close(); 
            ifsFileOutputStream.close(); 
 
            
          } /* remoteFile exists */ 
        } /* secondDash */ 
      } /* firstDash */ 
    } /* for i */ 
    
    for (int i = 0; i < files.length; i++ ) { 
      String localFilename = files[i][0]; 
      String remoteFilename = files[i][1];
     
    }
    
  }

  private static void verifyParent(IFSFile ifsFile) throws IOException {

    IFSFile parent = ifsFile.getParentFile(); 
    if (!parent.exists()) { 
      parent.mkdirs();
    }
  }

  private static String getRemoteFilename(String testDirectory, String localFilename) {
    return "/home/jdbctest/"+localFilename.replace(File.separatorChar,'/');
  }

  private static Vector<String> buildFileList(File testDirectoryFile, String prefix, long lastModifiedTime) {
    Vector<String> returnList = new Vector<String>(); 
    File[] files = testDirectoryFile.listFiles(); 
    for (int i=0; i < files.length; i++) { 
      File f = files[i]; 
      if (f.isFile()) {
        if (f.lastModified() > lastModifiedTime) { 
          returnList.add(prefix+File.separator+f.getName());
        }
      } else if (f.isDirectory()) { 
        Vector<String> newList = buildFileList(f, prefix+File.separator+f.getName(), lastModifiedTime);
        returnList.addAll(newList); 
      }
    }
    
    return returnList;
  }

}
