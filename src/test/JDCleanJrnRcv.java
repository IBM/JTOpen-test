///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanJrnRcv.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Enumeration;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectList;



public class JDCleanJrnRcv {
  
  public static int SMALL_OBJECT_SIZE = 3000000; 
  /* Note.  The default size seems to be about 2,200,000 bytes */ 
  
  public static void usage() {
    System.out
        .println("Usage:  java JDCleanJrnRcv <system> <userid> <password> ");
    System.out
        .println("        Creates new journal receivers for QSQJRN journal.");
    System.out.println("        Deletes the old journal receivers. ");

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

      clean(system, userid, PasswordVault.getEncryptedPassword(password), System.out);
    } catch (Exception e) {
      e.printStackTrace();
      usage();
    }
  }

  public static void clean(String system, String userid, char[] encryptedPassword,
      PrintStream out) throws Exception {
    AS400 as400;
    if (userid == null) {
      as400 = new AS400(system);
    } else {
      
      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword);
      as400 = new AS400(system, userid, charPassword);
       PasswordVault.clearPassword(charPassword);
    }

    CommandCall commandCall = new CommandCall(as400);

    // Todo:  delete all the audit reciever journals older than
    // 1 week.

    // Delete all the audit receiver journals not attached

    String deleteAudRcvCommand = " DLTJRNRCV JRNRCV(*ALL/AUD*) DLTOPT(*IGNINQMSG)    " ;

    System.out.println("Runnning :"+deleteAudRcvCommand);

    commandCall.run(deleteAudRcvCommand);

    for (char c = 'A'; c <= 'Z'; c++) { 

	if (c != 'Q') { 
	    String deleteRRcvCommand = " DLTJRNRCV JRNRCV(*ALL/"+c+"*) DLTOPT(*IGNINQMSG)    " ;

	    System.out.println("Runnning :"+deleteRRcvCommand);

	    commandCall.run(deleteRRcvCommand);
	}
    }

    ObjectList jrnList = new ObjectList(as400, "*ALL", "Q*", "*JRN");

    Enumeration<?> enumeration = jrnList.getObjects();
    int totalDeletedCount = 0;
    long totalDeletedSize = 0L;
    int totalSkippedCount = 0; 
    long totalSkippedSize = 0L; 
    
    while (enumeration.hasMoreElements()) {
      ObjectDescription journalDescription = (ObjectDescription) enumeration
          .nextElement();
      try {
        if (out != null)
          out.println("Found " + journalDescription);

        String library = journalDescription.getLibrary();
        ObjectList jrnRcvList = new ObjectList(as400, library, "Q*", "*JRNRCV");
        ObjectDescription[] jrnRcvDesc = jrnRcvList.getObjects(0,
            jrnRcvList.getLength());
        String remainingName = "Q000000000";
        String remainingPath = null;
        long remainingSize = 0;
        int deletedCount = 0;
        long deletedBytes = 0;

        /* Delete all the journal receivers associated with journal */
        /* There should only be one that cannot be deleted */
        for (int i = 0; i < jrnRcvDesc.length; i++) {
          String name = jrnRcvDesc[i].getName();
          System.out.println("   Processing receiver "+name); 
          // Try to delete the journal. Sometimes the system deletes the
          // journal
          // So this may fail. Just ignore any errors here.

          long deletedSize = ((Long) jrnRcvDesc[i]
              .getValue(ObjectDescription.OBJECT_SIZE)).longValue();

          String command = "QSYS/DLTJRNRCV JRNRCV(" + library + "/" + name
              + ") DLTOPT(*IGNINQMSG)   ";
          if (commandCall.run(command)) {
            deletedBytes += deletedSize;
            totalDeletedSize += deletedSize;
            deletedCount++;
            totalDeletedCount++;
          } else {
            remainingName = name;
            remainingPath = jrnRcvDesc[i].getPath();
            remainingSize = ((Long) jrnRcvDesc[i]
                .getValue(ObjectDescription.OBJECT_SIZE)).longValue();

          }

        } /* for i -- journal receivers*/ 
          // Check the object size, if it isn't big, don't mess with it
          boolean createNewJrnRcv = true;
          if (remainingPath != null) {
            ObjectDescription biggestObject = new ObjectDescription(as400,
                remainingPath);
            try {
              Long objectSize = (Long) biggestObject
                  .getValue(ObjectDescription.OBJECT_SIZE);
              if (objectSize.longValue() < SMALL_OBJECT_SIZE) {
                if (out != null)
                  out.println("  Skipping small object " + remainingPath
                      + " : " + objectSize.longValue());
                totalSkippedCount++;
                totalSkippedSize += objectSize.longValue(); 
                createNewJrnRcv = false;
              }
            } catch (Exception e) {
              System.out.println("Ignoring exception " + e.toString());
              e.printStackTrace(System.out);
            }
          }
          String command = ""; 
          if (createNewJrnRcv) {
            // Pick a new name
            String numberString = remainingName.substring(6);
            int number = Integer.parseInt(numberString);
            // if (out != null) out.println("The biggest number is "+number);
            number = number + 1;
            if (number > 9999) {
              number = 1;
            }

            String newJrnRcv = remainingName.substring(0, 6) + pad4(number);
            // if (out != null) out.println("  new JRNRCV is "+newJrnRcv);

            // Create the journal
            boolean commandFailed = false;
            command = "QSYS/CRTJRNRCV JRNRCV(" + library + "/" + newJrnRcv
                + ") THRESHOLD(200000)";
            if (commandCall.run(command)) {
              command = "QSYS/CHGJRN JRN(" + library + "/QSQJRN) JRNRCV(" + library
                  + "/" + newJrnRcv + ")";
              if (commandCall.run(command)) {
                String name = remainingName;
                command = "QSYS/DLTJRNRCV JRNRCV(" + library + "/" + name
                    + ") DLTOPT(*IGNINQMSG)   ";
                // Try to delete the receiver.
                // Sometimes the system deletes the received.
                // So this may fail. Just ignore any errors here.
                if (commandCall.run(command)) {
                  deletedCount++;
                  deletedBytes += remainingSize;
                  totalDeletedSize += remainingSize;
                  totalDeletedCount++;
                }

              } else { /* CHGJRN FAILED */
                commandFailed = true;
              }
            } else { /* CRTJRNRCV FAILED */
              commandFailed = true;
            }
            if (commandFailed) {
              if (out != null)
                out.println("  *** ERROR: COMMAND FAILED: " + command);
            }

          } /* if CRTJRNRCV */
          if (out != null)
            out.println("  Done cleaning library");

          if (deletedCount > 0) {
            if (out != null)
              out.println("  Deleted " + deletedCount
                  + " journal receivers of "
                  + NumberFormat.getInstance().format(deletedBytes) + " bytes");
          }
        
    
      } catch (Exception e) {
        System.out.println("Exception processing " + journalDescription);
        e.printStackTrace(System.out);
      }
    } /* while processing libraries */
    System.out.println("Deleted "
        + NumberFormat.getInstance().format(totalDeletedSize) + " bytes from "
        + totalDeletedCount + " journal receivers");
    System.out.println("Skipped "
        + NumberFormat.getInstance().format(totalSkippedSize) + " bytes from "
        + totalSkippedCount + " journal receivers");

  }

  private static String pad4(int number) {
    if (number < 0)
      number = -number;
    if (number < 10) {
      return "000" + number;
    }
    if (number < 100) {
      return "00" + number;
    }
    if (number < 1000) {
      return "0" + number;
    }
    return "" + number;
  }

}
