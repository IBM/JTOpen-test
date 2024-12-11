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

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Vector; 
import com.ibm.as400.access.*; 
/**
 * This class is designed to update an IBM i system with all the test code currently 
 * in eclipse. 
 * 
 * 
 */
public class JTOpenEclipseExportAll extends JTOpenEclipseExport {

  public static void usage() { 
    System.out.println("Usage:  java  test.JTOpenEclipseExportALL IBMi userid password   ");
    System.out.println("   Updates an IBM i system with the all test changes in the Eclipse Environment");
    System.out.println("   MD5 checksums are used to determine what files need to be update"); 
  }
 
  public static void main(String args[]) {
    try {
      System.out.println("Usage:  java  test.JDTOpenEclipseExportALL IBMi userid password   ");
      System.out.println("   Updates an IBM i system with all test changes in the Eclipse Environment");

      String as400Name = args[0]; 
      System.out.println("EXPORTING to "+as400Name); 
      String userid = args[1]; 
      String password = args[2]; 
      String currentDirectory = System.getProperty("user.dir");
      // Home directory should be the base of the git repository

      String testDirectory = currentDirectory+File.separatorChar + "src" ;
      File testDirectoryFile = new File(testDirectory+File.separatorChar+"test"); 
      if (!testDirectoryFile.exists()) { 
        System.out.println("Error. directory "+testDirectory+File.separatorChar+"test"+" does not exist.  user.home is not the git root");
      }
      System.out.println("Current directory is "+currentDirectory);

      AS400 as400 = new AS400(as400Name, userid, password.toCharArray()); 
      AS400JDBCDriver driver = new AS400JDBCDriver(); 
      Connection connection = driver.connect(as400); 
      
      System.out.println("building file list"); 
      Vector<String> fileList = buildAllFileList(testDirectoryFile, "test"); 
      System.out.println("building local checksum table for "+fileList.size()+" files "); 
      buildChecksumFile(connection, testDirectory, fileList, "JDTESTINFO.HASHLOCAL"); 
      System.out.println("running changed query"); 
      Vector<String> updateFileList = buildChangedFileList(connection, "JDTESTINFO.HASHLOCAL");
      System.out.println("transferring "+updateFileList.size()+" files"); 
      transferFiles(as400, testDirectory, updateFileList); 
      System.out.println("Files transferred"); 
      compileFiles(as400); 
      System.out.println("==================================================================================================="); 
      System.out.println("DONE exporting all to "+as400Name+" at "+ (new Timestamp(System.currentTimeMillis()))); 
      System.out.println("==================================================================================================="); 
    } catch (Exception e) {
      e.printStackTrace(System.out);
      usage(); 
    }

  }

  private static Vector<String> buildChangedFileList(Connection c, String filename) throws SQLException {
    
    Vector<String> answer = new Vector<String>(); 
    
    String sql = "WITH X AS ( SELECT FILENAME, CHECKSUM,  "
        + "CASE WHEN   SYSTOOLS.IFS_ACCESS('/home/jdbctest/' || FILENAME) = 0 THEN "
        + "HASH_SHA1(GET_BLOB_FROM_FILE('/home/jdbctest/' || FILENAME) ) "
        + "ELSE BX'0000000000000000' END AS I_CHECKSUM "
        + "FROM "+filename+") "
        + "select FILENAME from X WHERE CHECKSUM <> I_CHECKSUM";
        
    
    Statement s = c.createStatement(); 
    ResultSet rs = s.executeQuery(sql); 
    while (rs.next()) { 
      answer.add(rs.getString(1)); 
    }
    rs.close(); 
    s.close(); 
    return answer;
  }

  
  private static void buildChecksumFile(Connection c, String testDirectory, Vector<String> fileList, String tableName ) throws IOException, AS400SecurityException, SQLException, NoSuchAlgorithmException {
    Statement s = c.createStatement();
    s.execute("CREATE OR REPLACE TABLE "+tableName+" (FILENAME VARCHAR(200), BYTELEN BIGINT, CHECKSUM VARBINARY(64)) ON REPLACE DELETE ROWS"); 
    PreparedStatement ps = c.prepareStatement("INSERT INTO "+tableName+" VALUES(?,?,?)");
    
    Enumeration<String> enumeration = fileList.elements(); 
    while (enumeration.hasMoreElements()) {
      String localFilename = enumeration.nextElement(); 
      boolean binary = JTOpenEclipseExport.isBinaryFile(localFilename); 

      File file = new File(testDirectory+File.separatorChar+localFilename); 
      String unixFilename = localFilename; 
      if (File.separatorChar != '/') {
          unixFilename=localFilename.replace( File.separatorChar, '/');
      }
      MessageDigest digest = MessageDigest.getInstance("SHA1");

      FileInputStream fis = new FileInputStream(file);

      byte[] buffer = new byte[16384];
      int bytesRead = 0;
      long totalBytes = 0; 
      while ((bytesRead = fis.read(buffer)) != -1)  {
        JTOpenEclipseExport.bufferCleanup(buffer, bytesRead, binary); 
        digest.update(buffer, 0, bytesRead);
        totalBytes += bytesRead; 
      }

      fis.close();

      // store the bytes returned by the digest() method
      byte[] bytes = digest.digest();
      ps.setString(1, unixFilename);
      ps.setLong(2, totalBytes);
      ps.setBytes(3, bytes);
      ps.executeUpdate(); 
      
      
    }
    ps.close(); 
    s.close(); 
  }


  private static Vector<String> buildAllFileList(File testDirectoryFile, String prefix) {
    Vector<String> returnList = new Vector<String>(); 
    File[] files = testDirectoryFile.listFiles(); 
    for (int i=0; i < files.length; i++) { 
      File f = files[i]; 
      if (f.isFile()) {
          returnList.add(prefix+File.separator+f.getName());
      } else if (f.isDirectory()) { 
        Vector<String> newList = buildAllFileList(f, prefix+File.separator+f.getName());
        returnList.addAll(newList); 
      }
    }
    
    return returnList;
  }

  
  
}
