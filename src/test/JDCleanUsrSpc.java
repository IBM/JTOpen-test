///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanUsrSpc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PrintStream;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;



public class JDCleanUsrSpc {
  
  public static int SMALL_OBJECT_SIZE = 3000000; 
  /* Note.  The default size seems to be about 2,200,000 bytes */ 
  
  public static void usage() {
    System.out
        .println("Usage:  java JDCleanUsrSpc <system> <userid> <password> ");
    System.out
        .println("        Cleans up USRSPC object -- i.e. QUSRSYS/QRW*.");

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

      clean(system, userid, PasswordVault.encryptPassword(password.toCharArray()), System.out);
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

 
    String deleteRwCommand = "DLTUSRSPC QUSRSYS/QRW*";

    System.out.println("Runnning :"+deleteRwCommand);

    commandCall.run(deleteRwCommand); 

    String deleteP0ZCommand = "DLTUSRSPC QUSRSYS/QP0Z*";

    System.out.println("Runnning :"+deleteP0ZCommand);

    commandCall.run(deleteP0ZCommand); 

    String deleteJTCommand = "DLTUSRSPC QUSRSYS/QJT*";

    System.out.println("Runnning :"+deleteJTCommand);

    commandCall.run(deleteJTCommand); 



  }


}
