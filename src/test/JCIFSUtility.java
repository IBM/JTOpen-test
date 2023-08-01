///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestDriverSecurityManager.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

/*
 * Class to open a session to the CIFS/SMB server
 *
 * This is used by the NetServer and INetServer testcases
 *
 * Code from jcifs.samba.org.  
 */

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;

import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ISeriesNetServer;
import com.ibm.as400.access.ISeriesNetServerFileShare;

import jcifs.smb.*;

public class JCIFSUtility {

  public static boolean debug = false;
  public static boolean useJdbc = true;
private static Connection jdbcConnection_;
private static Blob blob; 
  static {
    String debugString = System.getProperty("debug");
    if (debugString != null) {
      debug = true;
    }

    debugString = System.getProperty("test.JCIFSUtility.debug");
    if (debugString != null) {
      debug = true;
    }

    // Turn on extended security.
    jcifs.Config.setProperty("jcifs.util.loglevel","3"); 
    jcifs.Config.setProperty("jcifs.smb.client.useExtendedSecurity", "true");
    jcifs.Config.setProperty("jcifs.smb.lmCompatibility",  "3"); 
    
    
  }

  public static String fullyQualifySystem(String system) {
    if (system.indexOf(".") < 0) {
      if (!"LOCALHOST".equals(system.toUpperCase())) {
        system = system + ".rch.stglabs.ibm.com";
      } else {
        system = "localhost";
      }
    }
    return system;
  }

  public static void createFile(String system, String userId, char[] encryptedPassword,
      String filename) throws Exception {
    createFile(system, userId, encryptedPassword, filename, "");
  }

  public static void createFile(String system, String userId,  char[] encryptedPassword,
      String filename, String data) throws Exception {
    createFile(system, userId, encryptedPassword, filename, data.getBytes());
  }

  public static void createFile(String system, String userId, char[] encryptedPassword,
      String filename, byte[] data) throws Exception {
	  
	  if (useJdbc) { 
		  if (jdbcConnection_ == null) {
			  setupJdbcConnection(system, userId, encryptedPassword); 
		  }
		  PreparedStatement ps = jdbcConnection_.prepareStatement("CALL IFS_WRITE_BINARY(?,?,819,'REPLACE')"); 
		  ps.setString(1, filename);
		  ps.setBytes(2, data); 
		  ps.execute();
		  ps.close(); 
	  } else { 
    SmbFileOutputStream outputStream = null;

    String url;
    system = fullyQualifySystem(system);
    url = getUrl(system, userId, encryptedPassword, filename);

    if (debug)
      System.out.println("JCIFSUtility.debug:  createFile url=" + url);

    try {
      int retryCount = 20;
      while (retryCount > 0) {
        try {
          outputStream = new SmbFileOutputStream(url);
          retryCount = 0;
        } catch (jcifs.smb.SmbAuthException a) {
          String message = a.getMessage();
          if (message.indexOf("Access is denied") >= 0) {
            // Loop and try again. JCIFS is kind of quirky
            try {
              System.out.println("Warning:  Access denied to " + url);
              Thread.sleep(333);
            } catch (Exception e) {

            }
            retryCount--;
            if (retryCount == 0) {
              outputStream.close(); 
              throw a;
            }
          } else {
            outputStream.close(); 
              throw a;
          }

        }
      }

      outputStream.write(data);
      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      String message = e.toString();
      if (message.indexOf("") >= 0) {
        System.out.println("JDCFISUtility exception URL=" + url);
      }
      throw e;
    }
	  }	
  }

  private static void setupJdbcConnection(String system, String userId, char[] encryptedPassword) throws SQLException {
	  
	  char[] charPassword = PasswordVault.decryptPassword(encryptedPassword); 
	    // For now use the string.. In the future, this will be removed. 
	    String password = new String(charPassword); 
	     PasswordVault.clearPassword(charPassword);
	  jdbcConnection_ = DriverManager.getConnection("jdbc:as400:"+system, userId, password); 

	
}

public static void deleteFile(String system, String userId, char[] encryptedPassword,
      String filename) throws Exception {
    SmbFile smbFile = null;

    system = fullyQualifySystem(system);
    String url = getUrl(system, userId, encryptedPassword, filename);

    if (debug)
      System.out.println("JCIFSUtility.debug:  deleteFile url=" + url);
    smbFile = new SmbFile(url);
    if (smbFile.exists()) {
      smbFile.delete();
    }

  }

  public static void createDirectory(String system, String userId,
      char[] encryptedPassword, String directoryname) throws Exception {
    SmbFile smbFile = null;

    system = fullyQualifySystem(system);
    String url;
    url = getUrl(system, userId, encryptedPassword, directoryname);

    if (debug)
      System.out.println("JCIFSUtility.debug:  createDirectory url=" + url);

    try {
      int retryCount = 20;
      while (retryCount > 0) {
        try {
          smbFile = new SmbFile(url);
          smbFile.mkdirs();

          retryCount = 0;
        } catch (jcifs.smb.SmbAuthException a) {
          String message = a.getMessage();
          if (message.indexOf("Access is denied") >= 0) {
            // Loop and try again. JCIFS is kind of quirky
            try {
              System.out.println("Warning:  Access denied to " + url);
              Thread.sleep(333);
            } catch (Exception e) {

            }
            retryCount--;
            if (retryCount == 0) {
              throw a;
            }
          } else {
            throw a;
          }

        }
      }

    } catch (Exception e) {
      String message = e.toString();
      if (message.indexOf("") >= 0) {
        System.out.println("JDCFISUtility exception URL=" + url);
      }
      throw e;
    }

  }

  public static String[] listDirectory(String system, String userId,
      char[] encryptedPassword, String directoryname) throws Exception {
    SmbFile smbFile = null;

    system = fullyQualifySystem(system);
    String url;
    url = getUrl(system, userId, encryptedPassword, directoryname);

    if (debug)
      System.out.println("JCIFSUtility.debug:  listDirectory url=" + url);
    //
    // Verify that directory ends with /
    // otherwise JCIFS returns null.
    if (url.charAt(url.length() - 1) != '/') {
      url = url + "/";
    }

    try {
      int retryCount = 20;
      while (retryCount > 0) {
        try {
          smbFile = new SmbFile(url);
          String[] files = smbFile.list();

          // SmbFile has a bug in that it returns the files several time.. Make
          // sure they are unique
          Hashtable uniqueNames = new Hashtable();
          for (int i = 0; i < files.length; i++) {
            uniqueNames.put(files[i], "X");
          }

          files = new String[uniqueNames.size()];
          Enumeration keys = uniqueNames.keys();
          int i = 0;
          while (keys.hasMoreElements()) {
            files[i] = (String) keys.nextElement();
            i++;
          }
          return files;

        } catch (jcifs.smb.SmbAuthException a) {
          String message = a.getMessage();
          if (message.indexOf("Access is denied") >= 0) {
            // Loop and try again. JCIFS is kind of quirky
            try {
              System.out.println("Warning:  Access denied to " + url);
              Thread.sleep(333);
            } catch (Exception e) {

            }
            retryCount--;
            if (retryCount == 0) {
              throw a;
            }
          } else {
            throw a;
          }

        }
      }

    } catch (Exception e) {
      String message = e.toString();
      if (message.indexOf("") >= 0) {
        System.out.println("JDCFISUtility exception URL=" + url);
      }
      throw e;
    }
    return null;
  }

  public static void recursiveDelete(SmbFile smbFile) throws Exception {
    Exception savedException = null;
    SmbFile[] files = null;
    try {
      files = smbFile.listFiles();
    } catch (SmbException smbEx) {
      String exMessage = smbEx.toString();
      if (exMessage.indexOf("must end with") >= 0) {
        smbFile = new SmbFile(smbFile.getURL() + "/");
        files = smbFile.listFiles();

      } else {
        throw smbEx;
      }
    }
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        recursiveDelete(files[i]);
      } else {
        try {
          files[i].delete();
        } catch (Exception e) {
          savedException = e;
        }
      }
    }
    if (savedException != null) {
      throw savedException;
    }
    smbFile.delete();
  }

  public static boolean deleteDirectory(String system, String userId,
      char[] encryptedPassword, String filename) throws Exception {

    if (useJdbc) { 
    	if (jdbcConnection_ == null) { 
    		setupJdbcConnection(system, userId, encryptedPassword);
    	}
    	
    	Statement s = jdbcConnection_.createStatement(); 
    	s.executeUpdate("CALL QSYS2.QCMDEXC('QSH CMD(''rm -fr "+filename+"'')')"); 
    	s.close(); 
    	return true; 
    } else { 
        SmbFile smbFile = null;

	    system = fullyQualifySystem(system);
	    String url = getUrl(system, userId, encryptedPassword, filename);
	
	    if (debug)
	      System.out.println("JCIFSUtility.debug:  deleteFile url=" + url);
	    smbFile = new SmbFile(url);
	    if (smbFile.exists()) {
	      if (smbFile.isDirectory()) {
	        recursiveDelete(smbFile);
	      }
	    }
	    smbFile = new SmbFile(url);
	    if (smbFile.exists()) {
	      System.out.println("Delete of " + url + " failed");
	      return false;
	    }
	    return true;
    }	
  }

  public static DataInput openDataInput(String system, String userId,
      char[] encryptedPassword, String filename, String mode)
      throws MalformedURLException, SmbException, UnknownHostException,
      FileNotFoundException, SQLException {

    system = fullyQualifySystem(system);

    if (system.equals("localhost")) {
      return new RandomAccessFile(filename, mode);
    } else {
    	if (useJdbc) { 
    		
    		InputStream inputStream = getFileInputStream(system, userId, encryptedPassword, filename);
    		return new DataInputStream(inputStream); 
    	} else {
	      String url = getUrl(system, userId, encryptedPassword, filename);
	      SmbFile smbFile = new SmbFile(url);
	
	      if (debug)
	        System.out.println("JCIFSUtility.debug:  openDataInput url=" + url);
	      return new SmbRandomAccessFile(smbFile, mode);
    	}
    }
  }

  public static DataOutput openDataOutput(String system, String userId,
      char[] encryptedPassword, String filename, String mode)
      throws MalformedURLException, SmbException, UnknownHostException {
	  
    system = fullyQualifySystem(system);
    String url = getUrl(system, userId, encryptedPassword, filename);
    SmbFile smbFile = new SmbFile(url);

    if (debug)
      System.out.println("JCIFSUtility.debug:  openDataOutput url=" + url);
    return new SmbRandomAccessFile(smbFile, mode);
  }

  SmbFileOutputStream outputStream = null;
  String url = null;

  public JCIFSUtility(String system, String userId, char[] encryptedPassword) {
    try {

      system = fullyQualifySystem(system);

      long today = System.currentTimeMillis();
      url = getUrl(system, userId, encryptedPassword, "/tmp/JCIFSUtility." + today);
      boolean retry = true;
      int retryCount = 10;
      while (retry && retryCount > 0) {
        retry = false;
        retryCount--;
        try {
          outputStream = new SmbFileOutputStream(url);
        } catch (Exception e) {
          // Handle the unmapped problem (shows up as network name cannot be
          // found )
          String message = e.toString();
          if (message.indexOf("cannot be found") > 0) {
            // Make sure the root mapping exists
            System.out.println("JCIFSUtility attempting to map root directory");
            char[] charPassword = PasswordVault.decryptPassword(encryptedPassword);
            AS400 as400 = new AS400(system, userId, charPassword);
            PasswordVault.clearPassword(charPassword);  
            
            ISeriesNetServer netServer = new ISeriesNetServer(as400);
            netServer.createFileShare("root", "/", "ROOT",
                ISeriesNetServerFileShare.READ_WRITE);

            // Try again to attach
            outputStream = new SmbFileOutputStream(url);
          } else if (message
              .indexOf("NTLMv2 requires extended security") >= 0) {
            if (retryCount > 0) {
              retry = true;
            } else {
              throw e;
            }
          } else {
            throw e;
          }
        }
      }
      String info = "Connected from " + InetAddress.getLocalHost().getHostName()
          + "at " + new Date();
      outputStream.write(info.getBytes());
      outputStream.flush();
      outputStream.write(0x41);

      // Sleep for at least a second so that the session has an "age"
      try {
        Thread.sleep(1000);
      } catch (Exception e) {

      }
    } catch (Exception e) {
      System.out.flush();
      e.printStackTrace();
      System.out.println("url=" + url);
      System.out.flush();

    }

    // Sleep for at least a second so that the session has an "age"
    try {
      Thread.sleep(1000);
    } catch (Exception e) {

    }

  }

  public void close() {
    try {
      if (outputStream != null) {
        outputStream.close();
      }
      if (url != null) {
        SmbFile file = new SmbFile(url);
        file.delete();
      }
    } catch (Exception e) {
      System.out.flush();
      e.printStackTrace();
      System.out.flush();
      System.out.println("url=" + url);
    }
  }

  public static void main(String args[]) {
    try {
      if (args.length < 3) {
        System.out.println(
            "Usage:  java test.JCIFSUtility <system> <userid> <password>");
        return;
      }
      String system = args[0];
      String userId = args[1];
      String password = args[2];
      System.out.println("Creating new utility");
      char[] encryptedPassword =  PasswordVault.getEncryptedPassword(password);
      JCIFSUtility util = new JCIFSUtility(args[0], args[1], encryptedPassword);

      System.out.println("Utility created");

      System.out.println("Waiting for input");
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(System.in));
      String line = reader.readLine();
      while (line != null && (!(line.equalsIgnoreCase("quit")))
          && (!(line.equalsIgnoreCase("exit")))) {
        try {
          System.out.println("Executing " + line);
          line = line.trim();
          String command = line;
          String commandArgs = "";
          int spaceIndex = line.indexOf(' ');
          if (spaceIndex > 0) {
            command = line.substring(0, spaceIndex);
            commandArgs = line.substring(spaceIndex + 1);
          }
          if (command.equalsIgnoreCase("createFile")) {
            createFile(system, userId, encryptedPassword, commandArgs);
          } else if (command.equalsIgnoreCase("createDirectory")) {
            createDirectory(system, userId, encryptedPassword, commandArgs);
          } else if (command.equalsIgnoreCase("deleteDirectory")) {
            deleteDirectory(system, userId, encryptedPassword, commandArgs);

          } else if (command.equalsIgnoreCase("quit")) {
          } else if (command.equalsIgnoreCase("exit")) {
          } else {
            System.out.println(
                "Did not understand command: Valid commands are the following ");
            System.out.println("createFile FILENAME");
            System.out.println("createDirectory DIRECTORYNAME");
            System.out.println("deleteDirectory DIRECTORYNAME");
            System.out.println("quit");
            System.out.println("exit");
          }

          System.out.println("Enter command:  quit to exit");
        } catch (Exception e) {
          e.printStackTrace();
        }
        line = reader.readLine();
      }
      System.out.println("Closing");
      util.close();
      System.out.println("Closed");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static InputStream getFileInputStream(String system, String userId,
      char[] encryptedPassword, String ifsPathName) throws SmbException,
      MalformedURLException, UnknownHostException, FileNotFoundException, SQLException {
    InputStream fis = null;

	if (system == "localhost") {
		// System.out.println("Using local input stream");
		fis = new FileInputStream(ifsPathName);
	} else {
		if (useJdbc) {
			if (jdbcConnection_ == null) {
				setupJdbcConnection(system, userId, encryptedPassword);
			}
			String sql = "SELECT LINE FROM TABLE(QSYS2.IFS_READ_BINARY(?))";
			PreparedStatement ps = jdbcConnection_.prepareStatement(sql);
			ps.setString(1, ifsPathName);
			;
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				blob = rs.getBlob(1);
				
			} else {
				SQLWarning rsWarning = rs.getWarnings(); 
				if (rsWarning != null) { throw rsWarning; }; 
				SQLWarning psWarning = ps.getWarnings(); 
				if (psWarning != null) { throw psWarning; }; 
                
				// File is empty
				blob = jdbcConnection_.createBlob(); 
			}
			fis = blob.getBinaryStream();
		} else { 
      String url = getUrl(system, userId, encryptedPassword, ifsPathName);

      if (debug)
        System.out
            .println("JCIFSUtility.debug:  getFileInputStream url=" + url);
      try {
        fis = new SmbFileInputStream(url);
      } catch (SmbException e) {
        System.err.println("Exception on new SmbFileInputStream(" + url + ")");
        throw e;
      }
    	}	
    }
    return fis;
  }

  public static OutputStream getFileOutputStream(String system, String userId,
      char[] encryptedPassword, String ifsPathName)
      throws SmbException, MalformedURLException, UnknownHostException {

    OutputStream fos = null;
    String url = getUrl(system, userId, encryptedPassword, ifsPathName);

    if (debug)
      System.out.println("JCIFSUtility.debug:  getFileInputStream url=" + url);

    fos = new SmbFileOutputStream(url);

    return fos;
  }

  public static String getUrl(String system, String userId, char[] encryptedPassword,
      String ifsPathName) {
    String url;
    char[] charPassword = PasswordVault.decryptPassword(encryptedPassword); 
    // For now use the string.. In the future, this will be removed. 
    String password = new String(charPassword); 
     PasswordVault.clearPassword(charPassword);
     
    if (ifsPathName.charAt(0) == '/') {
      url = "smb://" + userId + ":" + password + "@" + system + "/root"
          + ifsPathName;
    } else {
      if (ifsPathName.charAt(0) == '\\') {

        url = "smb://" + userId + ":" + password + "@" + system + "/root/"
            + ifsPathName.substring(1);
      } else {

        url = "smb://" + userId + ":" + password + "@" + system + "/root/"
            + ifsPathName;
      }
    }
    return url;
  }

  public static DataOutput RandomAccessFileDataOutput(String system,
      String userId, char[] encryptedPassword, String ifsPathName, String mode,
      int shareAccess)
      throws SmbException, MalformedURLException, UnknownHostException {

    String url = getUrl(system, userId, encryptedPassword, ifsPathName);

    return new SmbRandomAccessFile(url, mode, shareAccess);
  }

  public static boolean fileExists(String system, String userId,
      char[] encryptedPassword, String filename)
      throws MalformedURLException, SmbException, SQLException {
	  
	  
	  if (useJdbc) {
		  	boolean result; 
			if (jdbcConnection_ == null) {
				setupJdbcConnection(system, userId, encryptedPassword);
			}
			String sql = "SELECT LINE FROM TABLE(QSYS2.IFS_READ_BINARY(?))";
			PreparedStatement ps = jdbcConnection_.prepareStatement(sql);
			ps.setString(1, filename);
			;
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true; 
				
			} else {
				SQLWarning rsWarning = rs.getWarnings(); 
				if (rsWarning != null) { return false;  }; 
				SQLWarning psWarning = ps.getWarnings(); 
				if (psWarning != null) { return false; }; 
              
				// File is empty
			return true;  
			}
			
		} else { 
    String url = getUrl(system, userId, encryptedPassword, filename);

    SmbFile smbFile = new SmbFile(url);
    return smbFile.exists();
    
    
		}
  }

  public static long fileLength(String system, String userId, char[] encryptedPassword,
      String filename) throws MalformedURLException, SmbException {
    String url = getUrl(system, userId, encryptedPassword, filename);

    SmbFile smbFile = new SmbFile(url);
    return smbFile.length();
  }

  public static boolean checkExpectedRead2(String system, String userId,
      char[] encryptedPassword, String ifsPathName, int x1, int x2) throws IOException, SQLException {
	  if (useJdbc) { 
		  
		  
			if (jdbcConnection_ == null) {
				setupJdbcConnection(system, userId, encryptedPassword);
			}
			String sql = "SELECT LINE FROM TABLE(QSYS2.IFS_READ_BINARY(?))";
			PreparedStatement ps = jdbcConnection_.prepareStatement(sql);
			ps.setString(1, ifsPathName);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(1); 
				if (blob.length() < 2) {
					return false; 
				} else {
					byte[] bytes = blob.getBytes(1,2); 
					if (bytes[0] == x1 && bytes[1] == x2) {
						return true; 
					} else { 
						return false; 
					}
				}
			} else {
				ps.close(); 
				return false; 
			}
	  } else { 
    String url = getUrl(system, userId, encryptedPassword, ifsPathName);

    if (debug)
      System.out.println("JCIFSUtility.debug:  checkExpectedRead2 url=" + url);

    InputStream fis = new SmbFileInputStream(url);
    boolean passed =    fis.read() == x1 && fis.read() == x2;
    fis.close(); 
    return passed; 
	  }
  }

}
