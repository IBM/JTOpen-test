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
import java.util.Vector; 
import com.ibm.as400.access.*; 
/**
 * This class is designed to update an IBM i system with the latest updates currently worked
 * on in eclipse. 
 * 
 * 
 */
public class JTOpenEclipseExport extends Thread  {

  String as400Name_;
  String userid_;
  String password_;
  
  public JTOpenEclipseExport(String as400Name, String userid, String password) {
    as400Name_ = as400Name; 
    userid_ = userid;
    password_ = password; 
  }
  
  public void run()  {
    try { 
    export(as400Name_,userid_,password_); 
    } catch (Exception e) { 
      synchronized(System.out) { 
        System.out.println("Exception caught exporting to "+as400Name_); 
        e.printStackTrace(System.out); 
      }
    }
  }
  public static void usage() { 
    System.out.println("Usage:  java  test.JTOpenEclipseExport IBMi[+IBMi]* userid password   ");
    System.out.println("   Updates IBM i systems with the latest test changes in the Eclipse Environment");
    System.out.println("   Uses {user.dir}/lastUpdate.$system as a time marker"); 
  }

  public static void export(String as400Name, String userid, String password) throws Exception {   
    String prefix = as400Name+":"; 
    String currentDirectory = System.getProperty("user.dir");
    // Home directory should be the base of the git repository
    String homeDirectory = System.getProperty("user.home"); 
    String testDirectory = currentDirectory+File.separatorChar + "src" ;
    File testDirectoryFile = new File(testDirectory+File.separatorChar+"test"); 
    if (!testDirectoryFile.exists()) { 
      System.out.println(prefix+"Error. directory "+testDirectory+File.separatorChar+"test"+" does not exist.  user.home is not the git root");
    }
    System.out.println(prefix+"Current directory is "+currentDirectory);
    long startTime = System.currentTimeMillis(); 
    String lastTimeFilename = homeDirectory+File.separatorChar+"lastUpdate."+as400Name; 
    File lastTimeFile = new File(lastTimeFilename); 
    if (!lastTimeFile.exists()) { 
      lastTimeFile.createNewFile(); 
      lastTimeFile.setLastModified(startTime - 24 * 60 * 60000);
      System.out.println(prefix+lastTimeFilename+" does not exist.  Creating a new one a day old"); 
    }
    long lastModifiedTime = lastTimeFile.lastModified(); 
    Timestamp lastModifiedTimestamp = new Timestamp(lastModifiedTime); 
    System.out.println(prefix+"Comparing against "+lastTimeFilename+" "+lastModifiedTimestamp.toString()); 
    
    Vector<String> fileList = buildFileList(testDirectoryFile, "test", lastModifiedTime); 
    AS400 as400 = new AS400(as400Name, userid, password.toCharArray()); 
    transferFiles(as400, testDirectory, fileList); 
    System.out.println(prefix+"Files transferred"); 
    lastTimeFile.setLastModified(startTime);
 
    compileFiles(as400); 
    synchronized(System.out) {  
       System.out.println(prefix+"==================================================================================================="); 
       System.out.println(prefix+"DONE exporting to "+as400Name+" at "+ (new Timestamp(System.currentTimeMillis()))); 
       System.out.println(prefix+"==================================================================================================="); 
    }
  }
  
  public static void main(String args[]) {
    try {
      System.out.println("Usage:  java  test.JDTOpenEclipseExport IBMi userid password   ");
      System.out.println("   Updates an IBM i system with the latest changes in the Eclipse Environment");

      
      String as400Name = args[0]; 
      System.out.println("EXPORTING to "+as400Name); 
      String userid = args[1]; 
      String password = args[2]; 
      if (as400Name.indexOf('+') < 0) { 
         export(as400Name, userid, password); 
      } else {
        String[] systems = as400Name.split("\\+");
        JTOpenEclipseExport[] threads = new JTOpenEclipseExport[systems.length];
        for (int i = 0; i < systems.length; i++) { 
          threads[i] = new JTOpenEclipseExport(systems[i],userid,password); 
        }
        for (int i = 0; i < systems.length; i++) { 
          System.out.println("Starting export for system "+systems[i]); 
          threads[i].start(); 
        }
        for (int i = 0; i < systems.length; i++) { 
          System.out.println("Waiting for export for system "+systems[i]); 
          threads[i].join(); 
          System.out.println("Export completed for system "+systems[i]); 
        }
        
        System.out.println("All exports done"); 
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
      usage(); 
    }

  }

  protected static void compileFiles(AS400 as400) throws AS400SecurityException, ErrorCompletingRequestException, IOException, InterruptedException, PropertyVetoException {
   String prefix=as400+":";  
   System.out.println(prefix+"Running compile "); 
   CommandCall cc = new CommandCall(as400);
   String compileCommand = "QSH CMD('cd /home/jdbctest; test/AllQshCompileScript > /tmp/JTOpenCompile.out 2>&1') "; 
   cc.run(compileCommand);
   IFSFile ifsFile = new IFSFile(as400,"/tmp/JTOpenCompile.out"); 
   IFSFileReader reader = new IFSFileReader(ifsFile); 
   BufferedReader bufferedReader = new BufferedReader(reader); 
   String line = bufferedReader.readLine(); 
   while (line != null) { 
     System.out.println(prefix+line);
     line = bufferedReader.readLine(); 
   }
   bufferedReader.close(); 
  }

  static int bufferCleanup(byte[] buffer, int length, boolean binary) { 
    if (!binary) { 
      /* Remove any CR in the file */ 
      int toPosition = 0; 
      for (int i = 0; i < length; i++) { 
        boolean skipCR = false; 
        if (buffer[i] < 0x20) { 
          switch (buffer[i]) { 
          case 0x0a:
          case 0x09:  
            break; 
          case 0x0D:
            skipCR = true; 
            break; 
          default:
            buffer[i]=0x20;
            break;
          }
        } /* if control */ 
        if (skipCR) { 
          // Do not write anything 
        } else {
          if (toPosition == i) { 
            /* nothing to move */ 
          } else {
            buffer[toPosition] = buffer[i]; 
          }
          toPosition++; 
        }
      }
      return toPosition; 
    } else {
      /* Binary */ 
      return length; 
    }
  }
  static void transferFiles(AS400 as400, String testDirectory, Vector<String> fileList) throws IOException, AS400SecurityException {
    String prefix = as400+":";
    System.out.println(prefix+"Transferring "+fileList.size()+" files"); 
    Enumeration<String> enumeration = fileList.elements(); 
    while (enumeration.hasMoreElements()) {
      String localFilename = enumeration.nextElement(); 
      boolean binary = isBinaryFile(localFilename); 
      String remoteFilename = getRemoteFilename(testDirectory, localFilename);
      System.out.println(prefix+"Transferring to "+remoteFilename);
      IFSFile ifsFile = new IFSFile(as400, remoteFilename);
      ifsFile.delete(); 
      verifyParent(ifsFile); 
      ifsFile.createNewFile(); 
      ifsFile.setCCSID(1208); 
      IFSFileOutputStream ifsFileOutputStream = new IFSFileOutputStream(ifsFile);
      FileInputStream fileInputStream = new FileInputStream(testDirectory+File.separatorChar+localFilename); 
      byte[] buffer = new byte[65536];
      int bytesRead = fileInputStream.read(buffer );
      while (bytesRead >= 0) { 
        int bytesToWrite = bufferCleanup(buffer, bytesRead, binary); 
        ifsFileOutputStream.write(buffer, 0, bytesToWrite);
        bytesRead = fileInputStream.read(buffer );
      }
      fileInputStream.close(); 
      ifsFileOutputStream.close(); 
     
    }
    
  }

  static boolean isBinaryFile(String localFilename) {
    if (localFilename.endsWith(".zip")) return true; 
    if (localFilename.endsWith(".savf")) return true; 
    return false; 
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
