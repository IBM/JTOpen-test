///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTOpenDownloadDevJars.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTOpenDownloadDevJars
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
//
//  File Name:  JTOpenDownloadDevJars.java
//
//  Class Name:  JTOpenDownloadDev
//
////////////////////////////////////////////////////////////////////////
//
// This class is designed to run on an IBM i and download the latest development
// toolbox jar files from git and install them on the system. 
//
//
// This can be automated using the following.
// ADDJOBSCDE JOB(JDUPDJAR) CMD(JAVA CLASS(test.JTOpenDownloadDevJars) CLASSPATH('/home/jdbctest')) FRQ(*WEEKLY) SCDDATE(*NONE) SCDDAY(*ALL) SCDTIME('19:00') 
// 
////////////////////////////////////////////////////////////////////////

package test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*; 
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Properties;

public class JTOpenDownloadDevJars {

    public static String artifactsUrl = "https://api.github.com/repos/IBM/JTOpen/actions/artifacts"; 
    public static String artifactsTestUrl = "https://api.github.com/repos/IBM/JTOpen-test/actions/artifacts"; 
    public static String githubAuth="";

    public static String tmpDir = "/tmp/JTOpenDownloadDev"; 
    public static String jdk14newPath = "/QIBM/proddata/OS400/jt400/lib";  
    public static String jdk16Path    = "/QIBM/proddata/OS400/jt400/lib/java6";
    public static String jdk18Path    = "/QIBM/proddata/OS400/jt400/lib/java8";
    public static String jdk9Path     = "/QIBM/proddata/OS400/jt400/lib/java9";


    public static void setupEnv() throws Exception {
	if (JTOpenTestEnvironment.isAIX) {
	    /* Do not set cacerts */ 
	} else { 
	    System.setProperty("javax.net.ssl.trustStore","cacerts");
	}
	System.setProperty("https.protocols","TLSv1.2"); 
	String javaHome = System.getProperty("java.home");
	System.out.println("java.home is "+javaHome);
	if (javaHome.indexOf("jdk60") > 0) {
	    throw new Exception("Invalid java.home("+javaHome+") : export -s JAVA_HOME=/QOpenSys/QIBM/ProdData/JavaVM/jdk70/32bit"); 
	}

	javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance("TLSv1.2");
	ctx.init(null, null, null);
    }


    public static void main(String args[]) {

	try {
	    setupEnv(); 
	    boolean sendShipped = true ; 
	    System.out.println("Usage:  java test.JTOpenDownloadDevJars  ");
	    System.out.println("   Downloads the latest artifact from github");
	    System.out.println("   setenv DISABLESSL to disable SSL");
	    System.out.println("   setenv BRANCH=ANY or BRANCH=someBranch"); 

	    String system = null;
	    String userid = null;
	    String password = null; 

	    String jdk14Path = jdk14newPath;
	    System.out.println("osVersion is '" + JTOpenTestEnvironment.osVersion+"'");
	    String osVersion = JTOpenTestEnvironment.osVersion; 
	    if (JTOpenTestEnvironment.isWindows ||
	        JTOpenTestEnvironment.isLinux ||
	        JTOpenTestEnvironment.isAIX)  {

		 if (JTOpenTestEnvironment.isAIX &&
		     System.getProperty("user.dir").indexOf("yjac.jacl") < 0 ) {

		     jdk14Path = ".";
		     jdk16Path = "./java6";
		     jdk18Path = "./java8";
		     jdk9Path  = "./java9";

		 } else { 
		jdk14Path = "jars";
		jdk16Path = "jars/java6";
		jdk18Path = "jars/java8";
		jdk9Path  = "jars/java9";
		 }

		system = System.getenv("AS400");
		userid = System.getenv("USERID");
		password = System.getenv("PASSWORD"); 
		if (system == null || userid == null || password == null ) {
		    System.out.println("ERROR: environment variables AS400 USERID PASSWORD must be set when not on IBM i");
		    System.exit(1); 
		}

	    } else {
		if (osVersion.equals("V7R1M0")) {
		    jdk14Path = jdk14newPath;
		} else if (osVersion.equals("V7R2M0")) {
		    jdk14Path = jdk14newPath;
		} else if (osVersion.equals("V7R3M0")) {
		    jdk14Path = jdk14newPath;
		} else if (osVersion.equals("V7R4M0")) {
		    jdk14Path = jdk14newPath;
		} else if (osVersion.equals("V7R5M0")) {
		    jdk14Path = jdk14newPath;
		} else if (osVersion.equals("V7R6M0")) {
		    jdk14Path = jdk14newPath;
		} else {
		    System.out.println("Unrecognized os.version = " + osVersion);
		    System.out.println("Exiting");
		    System.exit(1);
		}
	    }

	    String disableSSL = System.getenv("DISABLESSL");
	    if (disableSSL != null) {
		System.out.println("DISABLESSL set");
		DisableSSL.go(); 
	    } 
	    // Connect to the system
	    Connection connection;
	    if (userid==null) {
		connection = DriverManager.getConnection("jdbc:db2:localhost"); 
	    } else {
		Class.forName("com.ibm.as400.access.AS400JDBCDriver"); 
		connection = DriverManager.getConnection("jdbc:as400:"+system, userid, password); 
	    }
	    // 
	    // run the query to show the possible URLS. 
            String sql;
            Statement stmt = connection.createStatement();
            sql = "SELECT URL,"
                + "TIMESTAMP( SUBSTRING(UPDATED_AT,1,10) || ' ' || SUBSTRING(UPDATED_AT,12,8)) + CURRENT TIMEZONE AS UPDATED , "
                + "CURRENT TIMEZONE as CURRENT_TIMEZONE, "
                + " BRANCH"
                + " FROM JSON_TABLE( HTTP_GET('"+artifactsUrl+"','{\"headers\":{\"Accept\":\"application/vnd.github+json\"}, \"sslTolerate\":true}'), '$.artifacts[*]' "
                + "COLUMNS ( "
                + "URL VARCHAR(200) CCSID 1208 PATH '$.url', "
                + "UPDATED_AT VARCHAR(40) PATH '$.updated_at', "
                + "BRANCH VARCHAR(120) PATH '$.workflow_run.head_branch'  ))  "
                + "ORDER BY UPDATED_AT desc FETCH FIRST 10 ROWS ONLY";
            System.out.println("FYI:  Latests artifacts are the following: "+sql); 
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("URL,UPDATED_CURRENT_TIMEZONE,TIMEZONE,BRANCH"); 
            while (rs.next()) { 
              System.out.println(rs.getString(1)+","+rs.getTimestamp(2)+","+rs.getString(3)+","+rs.getString(4));
            }
            rs.close(); 
            // run the query to get the latest URL
	    String branch = System.getenv("BRANCH");
	    if (branch == null) { 
		sql = "SELECT * FROM JSON_TABLE( HTTP_GET('"+artifactsUrl+"',"
		    + "'{\"headers\":{\"Accept\":\"application/vnd.github+json\"}, \"sslTolerate\":true}'), "
		    + "'$.artifacts[*]' "
		    + "COLUMNS ( "
		    + "URL VARCHAR(200) CCSID 1208 PATH '$.url', "
		    + "UPDATED_AT VARCHAR(40) PATH '$.updated_at', "
		    + "BRANCH VARCHAR(120) PATH '$.workflow_run.head_branch'  )) WHERE BRANCH='main' ORDER BY UPDATED_AT desc FETCH FIRST 1 ROWS ONLY";
	    } else if ("ANY".equalsIgnoreCase(branch)) {
		sql = "SELECT * FROM JSON_TABLE( HTTP_GET('"+artifactsUrl+"','{\"headers\":{\"Accept\":\"application/vnd.github+json\"}, \"sslTolerate\":true}'), '$.artifacts[*]' COLUMNS ( URL VARCHAR(200) CCSID 1208 PATH '$.url', UPDATED_AT VARCHAR(40) PATH '$.updated_at', BRANCH VARCHAR(120) PATH '$.workflow_run.head_branch'  ))  ORDER BY UPDATED_AT desc FETCH FIRST 1 ROWS ONLY";
	    } else {
		sql = "SELECT * FROM JSON_TABLE( HTTP_GET('"+artifactsUrl+"','{\"headers\":{\"Accept\":\"application/vnd.github+json\"}, \"sslTolerate\":true}'), '$.artifacts[*]' COLUMNS ( URL VARCHAR(200) CCSID 1208 PATH '$.url', UPDATED_AT VARCHAR(40) PATH '$.updated_at', BRANCH VARCHAR(120) PATH '$.workflow_run.head_branch'  )) WHERE BRANCH='"+branch+"' ORDER BY UPDATED_AT desc FETCH FIRST 1 ROWS ONLY";
	    }
	    System.out.println("Running to get latest artifact from BRANCH: "+sql); 
	    rs = stmt.executeQuery(sql);
	    String downloadUrl;
	    String updatedAt;
	    String downloadBranch; 
	    if (rs.next()) {
		downloadUrl = rs.getString(1);
		updatedAt = rs.getString(2);
		downloadBranch =    rs.getString(3); 
	    } else {
		throw new Exception("query "+sql+" did not return any results"); 
	    }


	    System.out.println("Download url is "+downloadUrl+" updated at "+updatedAt+" branch="+downloadBranch);

	    File f = new File (tmpDir);

	    if (f.exists()) {
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
		    files[i].delete(); 
		} 
		f.delete(); 
	    }
	    
	    f.mkdir(); 
	    fetchFile(tmpDir, "allArtifact.zip", downloadUrl+"/zip");
	    unzip (tmpDir+"/allArtifact.zip",tmpDir);


            copyFile(  tmpDir+"/jt400.jar",               jdk14Path+"/jt400.jar");
            copyFile(  tmpDir+"/jt400-native.jar",        jdk14Path+"/jt400Native.jar");
            renameFile(tmpDir+"/jt400-javadoc.jar",       jdk14Path+"/jt400-javadoc.jar");
            renameFile(tmpDir+"/jt400-sources.jar",       jdk14Path+"/jt400-sources.jar");
	    renameFile(tmpDir+"/jt400.jar",               jdk16Path+"/jt400.jar");
            renameFile(tmpDir+"/jt400-native.jar",        jdk16Path+"/jt400Native.jar");
	    renameFile(tmpDir+"/jt400-java8.jar",         jdk18Path+"/jt400.jar");
            renameFile(tmpDir+"/jt400-native-java8.jar",  jdk18Path+"/jt400Native.jar");
	    renameFile(tmpDir+"/jt400-java11.jar",        jdk9Path+ "/jt400.jar");
	    renameFile(tmpDir+"/jt400-native-java11.jar", jdk9Path+ "/jt400Native.jar");


	    //
	    // Repeat for the test jar files
	    //
	    sql = "SELECT * FROM JSON_TABLE( HTTP_GET('"+artifactsTestUrl+"','{\"headers\":{\"Accept\":\"application/vnd.github+json\"}, \"sslTolerate\":true}'), '$.artifacts[*]' COLUMNS ( URL VARCHAR(200) CCSID 1208 PATH '$.url', UPDATED_AT VARCHAR(40) PATH '$.updated_at', BRANCH VARCHAR(40) PATH '$.workflow_run.head_branch'  )) WHERE BRANCH='main' ORDER BY UPDATED_AT desc FETCH FIRST 1 ROWS ONLY";
	    stmt = connection.createStatement();
	    System.out.println("Running to get latest artifact: "+sql); 
	    rs = stmt.executeQuery(sql);
	    if (rs.next()) {
		downloadUrl = rs.getString(1);
		updatedAt = rs.getString(2); 
	    } else {
		throw new Exception("query "+sql+" did not return any results"); 
	    }


	    System.out.println("Download url is "+downloadUrl+" updated at "+updatedAt);


	    f = new File (tmpDir);

	    if (f.exists()) {
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
		    files[i].delete(); 
		} 
		f.delete(); 
	    }
	    
	    f.mkdir();

	    fetchFile(tmpDir, "allArtifact.zip", downloadUrl+"/zip");
	    unzip (tmpDir+"/allArtifact.zip",tmpDir);
	    System.out.println("Unzipped "+ tmpDir+"/allArtifact.zip to "+tmpDir); 

            copyFile(tmpDir+"/JTOpen-test.jar", jdk14Path+"/JTOpen-test.jar");

	    connection.close();

	} catch (Exception e) {
        e.printStackTrace(); 
      }

    }


  private static void removeDirectory(File dir) {
      System.out.println("Removing directory "+dir.getAbsolutePath()); 
      File[] files = dir.listFiles(); 
      for (int i = 0; i < files.length; i++) {
        File file = files[i]; 
        if (file.isDirectory()) {
          removeDirectory(file); 
        } else {
          file.delete(); 
        }
      }
      dir.delete(); 
    }



  private static void unzip(String zipFile, String targetDir) throws IOException {
    // Unzip the file
    System.out.println("unzipping "+zipFile); 
    ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
    ZipEntry entry = zis.getNextEntry();
    while (entry != null) {
      String entryName = entry.getName();

        String targetName = entryName;
	int dashIndex = entryName.indexOf('-');
	if (dashIndex > 0) {
	    int snapshotIndex = entryName.indexOf("SNAPSHOT");
	    if (snapshotIndex > 0) {
		targetName = entryName.substring(0,dashIndex)+entryName.substring(snapshotIndex+8);
		
	    } else {
		snapshotIndex = entryName.indexOf("alpha");
		if (snapshotIndex > 0) {
		    targetName = entryName.substring(0,dashIndex)+entryName.substring(snapshotIndex+7);
		} else {
		    if (Character.isDigit(entryName.charAt(dashIndex+1))) {
			/* Check for another dash */
			int dash2Index = entryName.indexOf('-', dashIndex+1);
			if (dash2Index > 0) {
			    targetName = entryName.substring(0,dashIndex)+entryName.substring(dash2Index);
			} else {
			    /* must be the base .jar file */
			    targetName = entryName.substring(0,dashIndex)+".jar"; 
			}

		    }  else {
			if (targetName.indexOf("JTOpen-test") == 0) {
			    targetName="JTOpen-test.jar"; 
			} 
		    }
		}
	    }
	}
	System.out.println("processing:"+entryName+" to "+targetName);	
        String path = targetDir + "/" + targetName;
        if (entry.isDirectory()) {
          File subdir = new File(path);
          if (!subdir.exists()) { 
            subdir.mkdirs();
          }
        } else {
          BufferedOutputStream bos = new BufferedOutputStream(
              new FileOutputStream(path));
          byte[] inBytes = new byte[65535];
          int read = 0;
          while ((read = zis.read(inBytes)) != -1) {
              bos.write(inBytes, 0, read);
          }
          bos.close();
        }
      zis.closeEntry();
      entry = zis.getNextEntry();
    }
    zis.close();

  }


    public static void fetchFile(String destinationDirectory,
				 String fileName,
				 String urlPath) throws Exception {
      String destPath = destinationDirectory+"/"+fileName;
      try { 
	System.out.println("Fetching "+urlPath+ " to "+destPath); 
	URL url = new URL(urlPath); 
	URLConnection connection = url.openConnection();

	/* Github requires authorization to download a non-release artifacte */
	Properties iniProperties = new Properties();

	{
	    String filename = "ini/netrc.ini";
	    File file = new File(filename); 
	    if (!file.exists()) {
		throw new Exception("Unable to load ini/netrc.ini to get GITHUB token");
	    } 
	    InputStream fileInputStream= 	new FileInputStream(filename);
	    iniProperties.load(fileInputStream);
	    fileInputStream.close();
	}
	githubAuth=iniProperties.getProperty("GITHUBTOKEN"); 
	if (githubAuth == null) {
	    throw new Exception("Unable to get GITHUBTOKEN from ini/netrc.ini"); 
	}
	System.out.println("Authorization set to "+githubAuth); 
	connection.setRequestProperty("Authorization",githubAuth);
	connection.connect();
	InputStream in = connection.getInputStream();
	FileOutputStream out = new FileOutputStream(destPath);

	byte[] buffer = new byte[65536]; 
	int bytesRead = in.read(buffer);
	while (bytesRead > 0) {
	    out.write(buffer,0,bytesRead); 
	    out.flush(); 
	    bytesRead = in.read(buffer);
	} 
	out.close();
	in.close(); 
      } catch (Exception e) { 
        System.out.println("Exception processing "+urlPath+" to "+destPath);
        e.printStackTrace(); 
      }
    }


    public static void renameFile(String sourceFile, String destFile) throws Exception {
	File source = new File(sourceFile);
	File dest = new File(destFile);
	if (dest.exists()) {
	    dest.delete();
	} 
	boolean worked = source.renameTo(dest);
	if (worked) { 
	    System.out.println("Renamed "+sourceFile+" to "+destFile);
	} else {
            System.out.println("  rename failed, using copy instead"); 
	    copyFile(sourceFile, destFile);
	} 
    } 



    public static void copyFile(String sourceFile, String destFile) throws Exception {
      BufferedInputStream source = null;
      BufferedOutputStream destination = null;
      int bufSize = 2048;
        source =
          new BufferedInputStream (new FileInputStream (sourceFile), bufSize);
        destination =
          new BufferedOutputStream (new FileOutputStream (destFile), bufSize);
        byte[] buffer = new byte[bufSize];
        boolean done = false;
        while (!done)
        {
          int bytesRead = source.read (buffer);
          if (bytesRead == -1)
          {
            done = true;
            destination.flush ();
          }
          else destination.write (buffer, 0, bytesRead);
        }
	source.close();
	destination.close();
	System.out.println("Copied "+sourceFile+" to "+destFile); 
    } 


}
