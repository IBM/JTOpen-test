///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:   JTOpenDownloadReleaseJars
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

package test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JTOpenDownloadReleaseJars {
    public static String releaseUrl = "https://api.github.com/repos/IBM/JTOpen/releases"; 
    public static String tmpDir = "/tmp/JTOpenDownloadReleaseJars"; 
    public static String jdk14newPath = "/QIBM/proddata/OS400/jt400/lib";  
    public static String jdk16Path    = "/QIBM/proddata/OS400/jt400/lib/java6";
    public static String jdk18Path    = "/QIBM/proddata/OS400/jt400/lib/java8";
    public static String jdk9Path     = "/QIBM/proddata/OS400/jt400/lib/java9";

    public static String getDownloadUrl() throws Exception {
	String outUrl = null ;

	System.out.println("Fetching "+releaseUrl); 
	URL url = new URL(releaseUrl); 
	URLConnection connection = url.openConnection();

	connection.connect();
	StringBuffer sb = new StringBuffer(); 
	InputStream in = connection.getInputStream();
	byte[] buffer = new byte[4096];
	int bytesRead = in.read(buffer);
	while (bytesRead >= 0) {
	    if (bytesRead > 0) {
		sb.append(new String(buffer,0,bytesRead,"UTF-8")); 
	    }

	    bytesRead = in.read(buffer);
	} 

	String output = sb.toString();

	/* currently the releases are listed with the most recent first.  */
	/* Assume that is the case (so we don't have to parse all the json */ 
	int downloadIndex= output.indexOf("browser_download_url\"");

	while (downloadIndex > 0) {
	    int firstQuoteIndex = output.indexOf('"', downloadIndex + 21);
	    if (firstQuoteIndex > 0)  {
		int secondQuoteIndex = output.indexOf('"', firstQuoteIndex+1);
		if (secondQuoteIndex > 0) { 
		    outUrl =  output.substring(firstQuoteIndex+1,secondQuoteIndex);
		    if (outUrl.indexOf(".zip") >= 0) return outUrl;
		    downloadIndex = output.indexOf("browser_download_url\"",secondQuoteIndex);
		} else {
		    throw new Exception("second quote after browser_download_url not found in download from "+releaseUrl); 
		}

	    } else {
		throw new Exception("first quote after browser_download_url not found in download from "+releaseUrl); 
	    }
	}
	throw new Exception("browser_download_url not found in download from "+releaseUrl); 
    }



    public static void main(String args[]) {
	try {
	    System.out.println("Usage:  java test.JTOpenDownloadReleaseJars  ");
	    System.out.println("   Downloads the latest releast jar files from github");

	    String jdk14Path = jdk14newPath;
	    String jdk14nativePath = jdk14newPath;
	    System.out.println("osVersioname is '" + JTOpenTestEnvironment.osVersion+"'");
	    if (JTOpenTestEnvironment.isWindows||
	        JTOpenTestEnvironment.isLinux ||
	        JTOpenTestEnvironment.isAIX)  {

		 if (JTOpenTestEnvironment.isAIX &&
		     System.getProperty("user.dir").indexOf("/WWW/") > 0 ) {

		     jdk14Path = ".";
		     jdk14nativePath = ".";
		     jdk16Path = "./java6";
		     jdk18Path = "./java8";
		     jdk9Path  = "./java9";

		 } else { 
		jdk14Path = "jars";
		jdk14nativePath = "jars";
		jdk16Path = "jars/java6";
		jdk18Path = "jars/java8";
		jdk9Path  = "jars/java9";
		 }

	    } else {
	      jdk14Path = jdk14newPath;
	    }

	    // Make sure the directories exist
	    String[] dirs = {
	    		jdk14Path,
	    		jdk14nativePath,
	    		jdk16Path,
	    		jdk18Path,
	    		jdk9Path  
	    };

	    for (int i = 0; i < dirs.length; i++) { 
	    	File dir = new File(dirs[i]); 
	    	if (!dir.exists()) { 
	    		dir.mkdir(); 
	    	}
	    }
	    
	    String downloadUrl = getDownloadUrl();


	    System.out.println("Download url is "+downloadUrl);

	    File f = new File (tmpDir);

	    if (f.exists()) {
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
		    files[i].delete(); 
		} 
		f.delete(); 
	    }
	    
	    f.mkdir(); 
	    fetchFile(tmpDir, "allArtifact.zip", downloadUrl);
	    unzip (tmpDir+"/allArtifact.zip",tmpDir);


            copyFile(  tmpDir+"/jt400.jar",         jdk14Path+"/jt400.jar");
            copyFile(  tmpDir+"/jt400.jar",         jdk16Path+"/jt400.jar");
            renameFile(tmpDir+"/jt400.jar",         jdk18Path+"/jt400.jar");
            
            copyFile(  tmpDir+"/jt400-native.jar",  jdk14Path+"/jt400Native.jar");
            copyFile(  tmpDir+"/jt400-native.jar",  jdk16Path+"/jt400Native.jar");
            renameFile(tmpDir+"/jt400-native.jar",  jdk18Path+"/jt400Native.jar");
            
            renameFile(tmpDir+"/jt400-javadoc.jar", jdk14Path+"/jt400-javadoc.jar");
            renameFile(tmpDir+"/jt400-sources.jar", jdk14Path+"/jt400-sources.jar");
            
            renameFile(tmpDir+"/jt400-java11.jar",        jdk9Path+"/jt400.jar");
            renameFile(tmpDir+"/jt400-native-java11.jar", jdk9Path+ "/jt400Native.jar");


	} catch (Exception e) {
        e.printStackTrace(); 
      }

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
                          if (targetName.endsWith(".jar")) { 
                            targetName="JTOpen-test.jar"; 
                          } else if (targetName.endsWith(".war")) {
                            targetName="JTOpen-test.war"; 
                          }
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
	    System.out.println("Moved "+sourceFile+" to "+destFile);
	} else {
	    System.out.println("Failed to move "+sourceFile+" to "+destFile);
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
