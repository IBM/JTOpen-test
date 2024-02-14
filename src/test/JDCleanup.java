///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanup.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDCleanup.java
 //
 // Classes:      JDCleanup
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;



/**
Testcase JDCleanup.  
**/
public class JDCleanup
extends JDTestcase
{

   static String sep = System.getProperty("file.separator");


    // Private data.
    boolean cleanupSuccess = false; 


/**
Constructor.
**/
    public JDCleanup (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDCleanup",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
      
        cleanIfs(); 


        JDCleanJrnRcv.clean(systemObject_.getSystemName(), systemObject_.getUserId(), encryptedPassword_, System.out);
    JDCleanUsrSpc.clean(systemObject_.getSystemName(),
        systemObject_.getUserId(), encryptedPassword_, System.out);

    String password = "passwordLeak.JDCleanup";
    char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
    password = new String(charPassword);
    PasswordVault.clearPassword(charPassword);

    String url = baseURL_ + ";user=" + systemObject_.getUserId() + ";password="
        + password;
    Connection c = DriverManager.getConnection(url);

    if (collection_ != null)
      JDSetupProcedure.dropProcedures(c, collection_);
    else
            JDSetupProcedure.dropProcedures(c);

        // DROP COLLECTION always sends an inquiry message.
        // This will automatically reply so we don't hang 
        // the job.
	try { 
	    CommandCall cmd = new CommandCall(systemObject_, "ADDRPYLE SEQNBR(7025) MSGID(CPA7025) RPY(I)");
	    cmd.run();
	    AS400Message[] messageList = cmd.getMessageList();
	    for(int i = 0; i < messageList.length; ++i)
		System.out.println(messageList[i]);
	} catch (Exception e) {
	    System.out.println("Warning:  ADDRPYLE failed with exception: "+e);
	    System.out.flush(); 
	    e.printStackTrace();
	    System.err.flush(); 
	} 
        // We need to change the database server job to use
        // the system reply list.
        Statement s = c.createStatement();
        s.executeUpdate("CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)', 0000000026.00000)");

	
        s.close();

        if(collection_ != null) {
            JDTestDriver.dropCollection(c, collection_);
            int endIndex = 8;
            if(collection_.length() < 8)
                endIndex = collection_.length();
	    String[] suffixes = { "2", "XX", "A", "B", "C", "D", "E", "F", "GT", "O","JN","Z" };

	    for (int i = 0; i < suffixes.length; i++) {
		String collection_XX = collection_.substring(0, endIndex) + suffixes[i];
		JDTestDriver.dropCollection(c, collection_XX);

	    } 
        }
        else {
            JDDriverTest.dropCollections(c);
            JDConnectionTest.dropCollections(c);
            JDStatementTest.dropCollections(c);
            JDPSTest.dropCollections(c);
            JDCSTest.dropCollections(c);
            JDRSTest.dropCollections(c);
            JDRSMDTest.dropCollections(c);
            JDDMDTest.dropCollections(c);
            JDLobTest.dropCollections(c);
            JDCPDSTest.dropCollections(c);
            JTATest.dropCollections(c);
            JDSetupProcedure.dropCollections(c);
        }


	if(JDCleanupTest.COLLECTION != null) {
	    String sql = "CALL QSYS.QCMDEXC('DLTJRNRCV JRNRCV("+JDCleanupTest.COLLECTION+"/QSQ*) DLTOPT(*IGNINQMSG)                                                                                      ', 0000000080.00000 )  ";
	    try {
		Statement stmt = c.createStatement(); 
		stmt.executeUpdate(sql); 
	    } catch (SQLException e) {
	      if (isImportantException(e)) { 
		System.out.println("Exception running "+ sql);
		System.out.flush(); 
		e.printStackTrace(System.out);
		System.out.flush(); 
	      }
	    } 

            JDTestDriver.dropCollection(c, JDCleanupTest.COLLECTION);
            int endIndex = 7;
            if(JDCleanupTest.COLLECTION.length() < 8)
                endIndex = JDCleanupTest.COLLECTION.length() -1;
            String collection_2  = JDCleanupTest.COLLECTION.substring(0, endIndex) + "2";
            String collection_XX = JDCleanupTest.COLLECTION.substring(0, endIndex) + "XX";
            JDTestDriver.dropCollection(c, collection_2);
            JDTestDriver.dropCollection(c, collection_XX);
            // Delete the JDX tests as well
            if (JDCleanupTest.COLLECTION.indexOf("JDT")== 0) {
              String xaCollection = "JDX"+JDCleanupTest.COLLECTION.substring(3); 
              JDTestDriver.dropCollection(c, xaCollection);
            }
        }
        else {
            JDDriverTest.dropCollections(c);
            JDConnectionTest.dropCollections(c);
            JDStatementTest.dropCollections(c);
            JDPSTest.dropCollections(c);
            JDCSTest.dropCollections(c);
            JDRSTest.dropCollections(c);
            JDRSMDTest.dropCollections(c);
            JDDMDTest.dropCollections(c);
            JDLobTest.dropCollections(c);
            JDCPDSTest.dropCollections(c);
            JTATest.dropCollections(c);
            JDSetupProcedure.dropCollections(c);
        }

        c.close();
        
	cleanupSuccess=true;
	System.out.flush();
	System.err.flush(); 

            
    }


    
    private static String [] ignoredMessages = {
        "CPF2110", /* Library  not found. */ 
        "CPF9810", /* Library not found. */ 
        "CPF700B", /* objects have ended journaling */ 
    };
    public static boolean isImportantException(SQLException e) {
      int errorCode = e.getErrorCode();
      switch (errorCode) {
      case -204: /* not found */ 
        return false;
      case -443: /* Trigger program returned an error */ 
        String message = e.getMessage(); 
        for (int i = 0; i < ignoredMessages.length; i++) { 
          if (message.indexOf(ignoredMessages[i]) >= 0) {
            return false; 
          }
        }
        return true; 
      default:
        return true;
      }
    
    }

    
      static void cleanDirectory(String directory, String[] patterns, int days ) {

	  long daysAgoInMillis     = System.currentTimeMillis() - ( days *  24 * 60 * 60 * 1000L);
	  long oneDayAgo     = System.currentTimeMillis() - ( 1 *  24 * 60 * 60 * 1000L);

	  String subDir = directory;
	  File subDirFile = new File(subDir);
	  if (subDirFile.isDirectory()) {
	      subDir = subDirFile.getAbsolutePath(); 
	      String checkFileName = subDir+sep+"lastClean";
	      if (subDir.equals(sep)) {
	        checkFileName = subDir+"lastClean";
	      }
	      File checkFile = new File(checkFileName);
	      boolean doClean = true; 
	      if (checkFile.exists()) {
		  if (checkFile.lastModified() > oneDayAgo) {
		      System.out.println("Not cleaning IFS because "+checkFileName+" was less than one day ago");
		      doClean = false; 
		  }
	      }
	      if (doClean) {

		  String[] subDirContents = subDirFile.list();

		  System.out.println("Cleaning "+subDirFile+ " checking "+subDirContents.length+" files");
		  for (int j = 0; j < subDirContents.length; j++) {
		      String filename = subDirContents[j];

		      for (int k = 0; k < patterns.length; k++) {
			  if (filename.indexOf(patterns[k]) >= 0) {
			      File file = new File(subDir+sep+filename);
			      if (file.lastModified() < daysAgoInMillis) {
			        // Only delete if the owner is from a testcase
			       
			        System.out.println("Deleting "+subDir+sep+filename);
			        file.delete(); 
			      }  /* if older than 1 day */ 
			      
			  } /*if match pattern */ 
		      } /* loop through patterns */ 
		  } /* loop through files */ 
	      } /* doClean */
	      try {
		  checkFile.createNewFile();

		  checkFile.setLastModified(System.currentTimeMillis());
	      } catch (Exception e) {
		  System.out.println("WARNING:  Ignoring exception on "+checkFile+" for "+checkFileName); 
		  e.printStackTrace();
	      }

	  } /* if is Directory */ 
      } /* cleanDirectory */ 

    /*
   * delete the output files in ct/out that are older than 14 days. 
   * For most files, this is
   * based on the name to avoid the expensive stat calls.
   */
  public static void cleanIfs() {
    try {
      long fourteenDaysAgo = System.currentTimeMillis()
          - (14 * 24 * 60 * 60 * 1000L);
      long oneDayAgo = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000L);
      Calendar cal = Calendar.getInstance();
      String outDir = "ct" + sep + "out";
      File outDirFile = new File(outDir);
      String[][] outContents = new String[2][];
      String[] debugDataDirs = new String[1];
      debugDataDirs[0] = "ct" + sep + "debugdata";
      outContents[0] = outDirFile.list();
      outContents[1] = debugDataDirs;
      for (int h = 0; h < outContents.length; h++) {
        for (int i = 0; i < outContents[h].length; i++) {
          String subDir = outDir + sep + outContents[h][i];
          if (h > 0) {
            subDir =  outContents[h][i]; 
          }
          /* System.out.println("h="+h+" i="+i+" subdir="+subDir) */ ; 
          
          File subDirFile = new File(subDir);
          subDir = subDirFile.getAbsolutePath();
          if (subDirFile.isDirectory()) {
            boolean doClean = true;
            /* Check the time of last cleaning. If less than 1 day then skip */
            /* the current cleaning */
            String checkFileName = subDir + sep + "lastClean";
            File checkFile = new File(checkFileName);
            if (checkFile.exists()) {
              if (checkFile.lastModified() > oneDayAgo) {
                System.out.println("Not cleaning IFS because " + checkFileName
                    + " was less than 1 day ago");
                doClean = false;
              }
            }
            if (doClean) {

              String[] subDirContents = subDirFile.list();
              System.out.println("Cleaning " + subDirFile);
              for (int j = 0; j < subDirContents.length; j++) {
                String filename = subDirContents[j];
                if (filename.indexOf("runit.") == 0) {
                  String yearString = filename.substring(6, 10);
                  String monthString = filename.substring(11, 13);
                  String dayString = filename.substring(14, 16);
                  int year = Integer.parseInt(yearString);
                  int monthZeroBased = Integer.parseInt(monthString) - 1;
                  int day = Integer.parseInt(dayString);
                  // System.out.println("For file "+filename+" setting "+year+"
                  // "+monthZeroBased+" "+day);
                  cal.clear();
                  cal.set(year, monthZeroBased, day);
                  long fileTime;
                  try {
                    fileTime = cal.getTimeInMillis();
                  } catch (java.lang.IllegalAccessError iae) {
                    // JDK 1.3
                    fileTime = cal.getTime().getTime();
                  }
                  if (fileTime < fourteenDaysAgo) {
                    System.out.println("Deleting file " + filename);
                    File deleteFile = new File(subDir + sep + filename);
                    deleteFile.delete();
                  }
                } else if ( (filename.indexOf(".html") > 0 ) || (filename.indexOf(".txt") > 0)) {
                  File deleteFile = new File(subDir + sep + filename);
                  long lastModified = deleteFile.lastModified();

                  if (lastModified < fourteenDaysAgo) {
                    System.out.println("Deleting file " + filename);
                    deleteFile.delete();
                  }
                }
              }

              checkFile.createNewFile();
              checkFile.setLastModified(System.currentTimeMillis());
            }
          } else {
            System.out.println(
                "Found " + outContents[h][i] + " not directory in " + outDir);
          }
        } /* for i */
      } /* for h */
      // Cleanup other items 
	    //find /tmp -name "runit*" -mtime +1 -print  -exec rm {} \;
	    //find /tmp -name "*.rcp" -mtime +1 -print  -exec rm {} \; 
	    //find /tmp -name "*.java.out" -mtime +1 -print  -exec rm {} \;
      String[] tmpPatterns={"runit",".rcp", ".java.out", "trace.", "JCIFSUtility.", ".rxp" , "scheduler.restart", "JDConnectionStress.", "JDJSTP.", "jdbcTrace.", "dlfm_", "db2jtmp_",  "failed.joblog", "jdbccur.", "core","Snap","heapdump",  }; 

      cleanDirectory(sep+"tmp", tmpPatterns, 4 /* day */ );



      String[] corePatterns = {"core","Snap","heapdump",}; 
      cleanDirectory(sep+"home"+sep+"jdbctest", corePatterns, 4 /* days */ );
      cleanDirectory(sep, corePatterns, 4 /* days */ );
     

      String [] schedPatterns = {"scheduler.out" };
      cleanDirectory(sep+"tmp"+sep+"schedulerLogs",  schedPatterns, 7 /* days */);

		      
		      
      } catch (Exception e) { 
       System.out.println("IFS Cleanup failed"); 
       System.out.flush();
       e.printStackTrace(); 
       System.err.flush(); 
       
      }
    }
    public void Var001()
    {

	assertCondition(cleanupSuccess, "Cleanup was not successful.  See exceptions from setup");
    }


    public static void cleanPfrData() {

/* Big files to clean up
	
 QPFRDATA    QAPMJOBMI  
 QPFRDATA    QAPMJOBOS  
 QPFRDATA    QAPMJOBWT  
 QPFRDATA    QAPMJOBL     
 QPFRDATA    QAPMJOBWTG

 QPFRDATA    Q305010031 
 QPFRDATA    Q302000146 
 QPFRDATA    Q303035913 
 QPFRDATA    Q301000144 
 QPFRDATA    Q304000010 

 QPFRDATA    Q303000142 

 QPFRDATA    Q305000024 
 QPFRDATA    Q306000009 
 QPFRDATA    Q305230153 

*/


    }


    public static void main(String args[]) {
	System.out.println("test.JDCleanup:  Running cleanIfs");
	cleanIfs();
	System.out.println("test.JDCleanup:  cleanIfs Completed");
	System.out.println("test.JDCleanup:  Running cleanPfrData"); 
	cleanPfrData(); 
	System.out.println("test.JDCleanup:  cleanPrfData Completed");

	/* Need to clean  dltf qusrsys/qjt*   */
	/* Need to clean  QUSRSYS     QAOSDI1418  *JRNRCV    */
	/* Need to clean JRNRCV  QSYS        QE1D480501 */

    } 
}



