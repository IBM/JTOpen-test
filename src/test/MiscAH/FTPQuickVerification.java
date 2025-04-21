///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FTPQuickVerification.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import com.ibm.as400.access.*;

import test.FTPTest;
import test.PasswordVault;
import test.Testcase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.Integer;
import java.lang.String;
import java.text.SimpleDateFormat;


public class FTPQuickVerification extends    Testcase
                               implements FTPListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "FTPQuickVerification";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FTPTest.main(newArgs); 
   }
    private String user_     = null;
    private String clearPasswordString_ = null;
    private String system_   = null;
    private String testDirectory = "FTPTestDir";
    private String testDirectoryDeep = "FTPTestDir/rootDir/subdir2";
    private String initialToken_ = null;
    private boolean notWorthTrying = false;

    private static final int DETAILED = 1;
    private static final int NAME_ONLY = 0;

    private static final int ONE     =  1;
    private static final int TWO     =  2;
    private static final int FOUR    =  4;
    private static final int EIGHT   =  8;
    private static final int SIXTEEN = 16;
    private static final int TOTAL   = ONE + TWO + FOUR + EIGHT + SIXTEEN;

    private FTPEvent ftpEvent = null;

    private boolean cleanup = true;

    private AS400 pwrSys_ = null;
    private char[] clearPassword_;

    public FTPQuickVerification (AS400 systemObject,
                              ///Vector<String> variationsToRun,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String userid,
                              String initialToken,
                              AS400  PwrSys)
    {
        super (systemObject,
               "FTPQuickVerification",
               namesAndVars,
               runMode,
               fileOutputStream,
               password);


        initialToken_ = initialToken;
        pwrSys_       = PwrSys;

        if (initialToken_ == null)
        {
           System.out.println("-directory is invalid, no test will be run");
           notWorthTrying = true;
           System.out.println();
        }

        if ((password == null) || (password.length() < 1)) {
          System.out.println("===> warning, variations will fail because no -password specified");
        } else { 
            char[] encryptedPassword = PasswordVault.getEncryptedPassword(password);
            clearPassword_ = PasswordVault.decryptPassword(encryptedPassword); 
            clearPasswordString_ = new String(clearPassword_); 
        }

        if (systemObject_ != null)
        {
           user_     = userid;
           system_   = systemObject_.getSystemName();
        }

        if (FTPTest.DEBUG) System.out.println();

        if ((user_ == null) || (user_.length() < 1))
           System.out.println("===> warning, variations will fail because no -uid specified");

        if ((clearPasswordString_ == null) || (clearPasswordString_.length() < 1))
           System.out.println("===> warning, variations will fail because no -password specified");

        if ((system_ == null) || (system_.length() < 1))
           System.out.println("===> warning, variations will fail because no -system specified");

        if (FTPTest.DEBUG) System.out.println();

    }


    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup()
      throws Exception
    {
	lockSystem("FTP",600); 
      cleanUpDirs();
    }


    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
      if (cleanup)
        cleanUpDirs();
      unlockSystem(); 
    }


    void cleanUpDirs()
    {
       System.gc();
       if (FTPTest.DEBUG) System.out.println();

       String targetDir = "targetDirTest19";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);

       targetDir = "targetDirTest19a";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);

       targetDir = "targetDirTest20";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);

       targetDir = "targetDirTest21";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest21a";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest22";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       if (FTPTest.DEBUG) System.out.println();
    }




    void cleanUpDirs2(String targetDir)
    {
       try
       {
          if (! FTPUtilities.deleteDirectory(targetDir))
          {
             System.out.println("Warning!  Cleanup failed, could not delete " + targetDir + " on workstation");
          }
       }
       catch (Exception e)
       {
          System.out.println("Warning!  Cleanup failed, could not delete " + targetDir + " on workstation");
          e.printStackTrace();
       }
    }




    void cleanUpDirsFTP(String targetDir)
    {
       try
       {
          FTP c = new FTP(system_, user_, clearPasswordString_);
          c.cd(initialToken_);
          c.cd(targetDir);
          c.issueCommand("DELE " + "a.a");
          c.issueCommand("DELE " + "javasp.savf");
          c.issueCommand("DELE " + "jt400.jar");
          c.issueCommand("DELE " + "PureJava.html");
          c.cd(initialToken_);
          c.issueCommand("RMD " + targetDir);
       }
       catch (Exception e)
       {
          System.out.println("Warning!  Cleanup failed, could not delete " + targetDir + " on server");
          e.printStackTrace();
       }
    }


    public void beansCleanup()
    {
       ftpEvent = null;
    }

    public void retrieved(FTPEvent e)
    {
       ftpEvent = e;
    }

    public void put(FTPEvent e)
    {
       ftpEvent = e;
    }

    public void listed(FTPEvent e)
    {
       ftpEvent = e;
    }

    public void connected(FTPEvent e)
    {
       ftpEvent = e;
    }

    public void disconnected(FTPEvent e)
    {
       ftpEvent = e;
    }




    // ---------------------------------------------------------------------
    //  Various ascii tests
    // ---------------------------------------------------------------------
    public void Var001()
    {
       boolean Continue = true;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setDataTransferType(FTP.ASCII);
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (1)");
             Continue = false;
          }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setDataTransferType(FTP.ASCII);

             String msg = c.getLastMessage();

             if (msg.startsWith("200")) {}
             else
             {
                failed("Unexpected message " + msg);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.setDataTransferType(FTP.BINARY);
             c.setDataTransferType(FTP.ASCII);
             String msg = c.getLastMessage();

             if (msg.startsWith("200")) {}
             else
             {
                failed("Unexpected message " + msg);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }











    // ---------------------------------------------------------------------
    //  Various binary tests
    // ---------------------------------------------------------------------
    public void Var002()
    {
       boolean Continue = true;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.setDataTransferType(FTP.BINARY);
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (1)");
             Continue = false;
          }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setDataTransferType(FTP.BINARY);

             String msg = c.getLastMessage();

             if (msg.startsWith("200")) {}
             else
             {
                failed("Unexpected message " + msg);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.setDataTransferType(FTP.ASCII);
             c.setDataTransferType(FTP.BINARY);
             String msg = c.getLastMessage();

             if (msg.startsWith("200")) {}
             else
             {
                failed("Unexpected message " + msg);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }









    // ---------------------------------------------------------------------
    //  Various cd() tests
    // ---------------------------------------------------------------------
    public void Var003()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(null);
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd("");
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             if (c.cd("/notThere"))
             {
                failed("cd to notThere worked and shouldn't have");
                Continue = false;
             }
             else
             {
                if (! c.getLastMessage().startsWith("550"))
                {
                   failed("wrong message returned " + c.getLastMessage());
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.cd(initialToken_);

             if (c.cd(testDirectory))
             {
             }
             else
             {
                {
                   failed("could not cd (1) " + c.getLastMessage());
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (4)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }









    // ---------------------------------------------------------------------
    //  Various dir() tests
    // ---------------------------------------------------------------------
    public void Var004()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             String[] result = c.dir();
             if (FTPUtilities.checkTestDir(result, DETAILED, TOTAL)) {}
             else
             {
                failed("contents of directory incorrect (1) ");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (1)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.cd("rootDir");
             c.cd("subdir1");
             String[] result = c.dir();
             if (result.length == 0) {}
             else
             {
                if ((result.length == 1) && (result[0].indexOf("total") >= 0))
                {}
                else
                {
                   for (int i = 0; i < result.length; i++)
                      System.out.println("    " + result[i]);

                   failed("contents of directory incorrect (2) ");
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }










    // ---------------------------------------------------------------------
    //  Various ls() tests
    // ---------------------------------------------------------------------
    public void Var005()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             String[] result = c.ls();
             if (FTPUtilities.checkTestDir(result, NAME_ONLY, TOTAL)) {}
             else
             {
                failed("contents of directory incorrect (3) ");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.cd("rootDir");
             c.cd("subdir1");
             String[] result = c.ls();
             if (result.length == 0) {}
             else
             {
                if ((result.length == 1) && (result[0].indexOf("total") >= 0))
                {}
                else
                {
                   for (int i = 0; i < result.length; i++)
                      System.out.println("    " + result[i]);

                   failed("contents of directory incorrect (4) ");
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (4)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectoryDeep);
             String[] result = c.ls();
             if (FTPUtilities.checkForFile(result, "FSTOOL.EXE")) {}
             else
             {
                failed("contents of directory incorrect (5) ");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (5)");
             Continue = false;
          }
       }


       if (Continue)
          succeeded();
    }



    // ---------------------------------------------------------------------
    //  Various get/setUser() tests
    // ---------------------------------------------------------------------
    public void Var006()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setUser(null);
             failed("no exception when user is null");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setUser("");
             failed("no exception when user is empty string");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setUser("fred");
             if (c.getUser().equals("fred")) {}
             else
             {
                failed("setUser / getUser return different values (3)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setUser("fred");
             c.setUser("barney");
             if (c.getUser().equals("barney")) {}
             else
             {
                failed("setUser / getUser return different values (4)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (4)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP("sys", "fred", "pw");
             if (c.getUser().equals("fred")) {}
             else
             {
                failed("setUser / getUser return different values (3)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (5)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP("sys", "fred", "pw");
             c.setUser("barney");
             if (c.getUser().equals("barney")) {}
             else
             {
                failed("setUser / getUser return different values (6)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (6)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword(clearPasswordString_);
             c.cd(initialToken_);
             c.setUser(user_);
             failed("No exception when setting user after connect");
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
             {}
             else
             {
                failed(e, "Unexpected exception (7)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setServer(system_);
             c.setUser("baddS8");
             c.setPassword(clearPasswordString_);
             c.cd(initialToken_);
             failed("no exception 8");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "SecurityException"))
             {}
             else
             {
                failed(e, "Unexpected exception (8)");
                Continue = false;
             }
          }
       }

       if (Continue)
          succeeded();
    }




    // ---------------------------------------------------------------------
    //  Various setCurrentDirectory() tests
    // ---------------------------------------------------------------------
    public void Var007()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.setCurrentDirectory(null);
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.setCurrentDirectory("");
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             if (c.setCurrentDirectory("/notThere"))
             {
                failed("cd to notThere worked and shouldn't have");
                Continue = false;
             }
             else
             {
                if (! c.getLastMessage().startsWith("550"))
                {
                   failed("wrong message returned " + c.getLastMessage());
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);

             if (c.setCurrentDirectory(testDirectory))
             {
             }
             else
             {
                {
                   failed("could not cd (1) " + c.getLastMessage());
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (4)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }






    // ---------------------------------------------------------------------
    //  Various setPassword() tests
    // ---------------------------------------------------------------------
    public void Var008()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setPassword(null);
             failed("no exception when pw is null");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setPassword("");
             failed("no exception when pw is empty string");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword("fred");
             c.cd(initialToken_);
             failed("no failure when uid pw (pw is = fred): " + c.getLastMessage());
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e,"SecurityException"))
             {}
             else
             {
                failed(e, "Unexpected exception (3)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword("fred");
             c.cd(initialToken_);
             failed("no failure when uid pw (pw is = fred): " + c.getLastMessage());
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e,"SecurityException"))
             {}
             else
             {
                failed(e, "Unexpected exception (3)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword("fred");
             c.setPassword(clearPasswordString_);
             c.cd(initialToken_);
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (4)");
             Continue = false;
          }
       }


       if (Continue)
          succeeded();
    }






    // ---------------------------------------------------------------------
    //  Various getCurrentDirectory() tests
    // ---------------------------------------------------------------------
    public void Var009()
    {
       boolean Continue = true;

       if (Continue)
       {
          String it2;

          if (initialToken_.startsWith("~"))
             it2 = initialToken_.substring(1);
          else
             it2 = initialToken_;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);
             String cd = c.getCurrentDirectory();

             if (cd.indexOf(it2) >= 0)
             {}
             else
             {
                failed("getCD not same as setCD (1): " + cd);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (1)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);
             c.setCurrentDirectory(testDirectory);
             String cd = c.getCurrentDirectory();

             if (cd.indexOf(testDirectory) >= 0)
             {}
             else
             {
                failed("getCD not same as setCD (2): " + cd);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);
             c.setCurrentDirectory(testDirectory);
             c.setCurrentDirectory("notThere");
             String cd = c.getCurrentDirectory();

             if (cd.indexOf(testDirectory) >= 0)
             {}
             else
             {
                failed("getCD not same as setCD (3): " + cd);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }






    // ---------------------------------------------------------------------
    //  Various pwd() tests
    // ---------------------------------------------------------------------
    public void Var010()
    {
       boolean Continue = true;

       if (Continue)
       {
          String it2;

          if (initialToken_.startsWith("~"))
             it2 = initialToken_.substring(1);
          else
             it2 = initialToken_;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);
             String cd = c.pwd();

             if (cd.indexOf(it2) >= 0)
             {}
             else
             {
                failed("getCD not same as setCD (1): " + cd);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (1)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);
             c.setCurrentDirectory(testDirectory);
             String cd = c.pwd();

             if (cd.indexOf(testDirectory) >= 0)
             {}
             else
             {
                failed("getCD not same as setCD (2): " + cd);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);

             c.setCurrentDirectory(initialToken_);
             c.setCurrentDirectory(testDirectory);
             c.setCurrentDirectory("notThere");
             String cd = c.pwd();

             if (cd.indexOf(testDirectory) >= 0)
             {}
             else
             {
                failed("getCD not same as setCD (3): " + cd);
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }





    // ---------------------------------------------------------------------
    //  Various noop() tests
    // ---------------------------------------------------------------------
    public void Var011()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.noop();
             failed("No exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
             {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.noop();
             if (c.getLastMessage().startsWith("200"))
             {}
             else
             {
                failed("Unexpected message (2): " + c.getLastMessage());
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();

    }





    // ---------------------------------------------------------------------
    //  Various get(String, String) tests
    // ---------------------------------------------------------------------
    public void Var012()
    {
       boolean Continue = true;
       String targetDir = "targetDirTest19";
       String targetDirFull = targetDir + File.separator;

       try
       {
          File f = new File(targetDir);
          if (! f.mkdir())
          {
             failed("setup failed, could not create targetDirTest19 on workstation");
             Continue = false;
          }
       }
       catch (Exception e)
       {
          failed(e, "setup failed, could not create targetDirTest19 on workstation");
          Continue = false;
       }


       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.get(null, "fred");
             failed("no exception (1)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("2 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.get("fred", (String) null);
             failed("no exception (2)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.get("fred", "");
             failed("no exception (3)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (3)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("4 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.get("", "fred");
             failed("no exception (4)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (4)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("5 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.get("notThere", targetDirFull + "fred");
             failed("get of file that doesn't exit returned true");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (5)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("6 ");
          try
          {
             String source   = targetDirFull + "PureJava.html";
             String original = testDirectory + File.separator + "PureJava.html";

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("PureJava.html", source))
             {
                if (FTPUtilities.compareFile(source, original))
                {}
                else
                {
                   failed("compare failed (6)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (6)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (6)");
             Continue = false;
          }
       }


       if (Continue)
       {
          try
          {
          System.out.print("9 ");
             String source   = targetDirFull + "a.a";
             String original = testDirectory + File.separator + "a.a";

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.ASCII);

             if (c.get("a.a", source))
             {
                if (FTPUtilities.compareFile(source, original))
                {}
                else
                {
                   System.out.println();
                   System.out.println(" warning, ascii compare failed (9) ");
                   cleanup = false;
                   // failed("compare failed (9)");
                   // Continue = false;
                }
             }
             else
             {
                failed("get returned false (9)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (9)");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("10 ");
          try
          {
             String source   = targetDirFull + "FSTOOL.EXE";
             String original = testDirectory + File.separator +
                               "rootDir"     + File.separator +
                               "subdir2"     + File.separator + "FSTOOL.EXE";

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("rootDir/subdir2/FSTOOL.EXE", source))
             {
                if (FTPUtilities.compareFile(source, original))
                {}
                else
                {
                   failed("compare failed (10)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (10)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (10)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.println("11 ");
          try
          {
             String source   = targetDir;
             String original = testDirectory + File.separator +
                               "rootDir"     + File.separator +
                               "subdir2"     + File.separator + "FSTOOL.EXE";

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             c.get("rootDir/subdir2/FSTOOL.EXE", source);
             {
                failed("no exception (11)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             if (exceptionIs(e,"FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (11)");
                Continue = false;
             }
          }
       }

       if (Continue)
          succeeded();


    }



    // ---------------------------------------------------------------------
    //  Various put(String, String) tests
    // ---------------------------------------------------------------------
    public void Var013()
    {
       boolean Continue = true;

       String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest21";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest21 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest21 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("2 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("setup failed, could not create targetDirTest21 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest21 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.put((java.io.File) null, "fred");
             failed("no exception (1)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("4 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.put("fred", null);
             failed("no exception (2)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("5 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.put("fred", "");
             failed("no exception (3)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (3)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("6 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.put("", "fred");
             failed("no exception (4)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (4)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("7 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.put(targetDirFull + "fred", "notThere");
             failed("put of file that doesn't exist -- no exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (5)");
                Continue = false;
             }
          }
       }


       if (Continue)
       {
          System.out.print("10");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(original, target))
             {
                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (8)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (8)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (8)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("11");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.put(original, target))
             {
                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      System.out.println();
                      System.out.println("   warning, ascii compare failed (9) ");
                      cleanup = false;
                      // failed("compare failed (9)");
                      // Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (9)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (9)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (9)");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.println("12 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             c.put(original, target);
             failed("no exception (10)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e,"FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (10)");
                Continue = false;
             }
          }
       }


       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various get/put error tests
    // ---------------------------------------------------------------------
    public void Var014()
    {
       boolean Continue = true;

       String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest21";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);

             c.put(original, "notThere/a.a/b.b");
             failed("put to non-existent directory failed)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IOException"))
             {}
             else
             {
                failed(e, "Unexpected exception (9)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);

             c.get("notThere/a.a/b.b", "b.b");
             failed("put to non-existent directory failed)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (9)");
                Continue = false;
             }
          }
       }

       if (Continue)
          succeeded();

    }


    // ---------------------------------------------------------------------
    //  A couple save file tests
    // ---------------------------------------------------------------------
    public void Var015()
    {
       boolean Continue = true;

       if (pwrSys_ == null)
       {
          failed("no power system AS400");
          Continue = false;
       }
       else
       {
          // Test1: cd to library then put
          try
          {
             CommandCall cc = new CommandCall(pwrSys_);
	     cc.run("CHGJOB INQMSGRPY(*SYSRPYL)"); 
             if (! cc.run("DLTLIB FTPTEST") )
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }
             }

             if (! cc.run("DLTLIB FTPTEST2"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest2 " + message);
                   Continue = false;
                }
             }

             if (! cc.run("CRTLIB FTPTEST2"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, crtlib ftptest2 " + message);
                   Continue = false;
                }
             }

             if (Continue)
             {
                AS400FTP ftp = new AS400FTP(pwrSys_);
                ftp.cd("/QSYS.LIB/FTPTEST2.LIB");
                ftp.put(testDirectory + File.separator + "ftptest.savf", "FTPTEST.SAVF");
                if (! cc.run("RSTLIB SAVLIB(FTPTEST) DEV(*SAVF) SAVF(FTPTEST2/FTPTEST)"))
                {
		   String firstMessage = getFirstMessage(cc);
		   if (firstMessage.indexOf("CPF3848") >= 0) {
		       // OK to see security or format changes 
		   } else { 
		       failed("rstlib failed " + firstMessage);
		       Continue = false;
		   }
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "unexpected exception");
             Continue = false;
          }
       }



       // Test2: save file name fully qualified.
       if (Continue && (pwrSys_ != null))
       {
          try
          {
             CommandCall cc = new CommandCall(pwrSys_);
	     cc.run("CHGJOB INQMSGRPY(*SYSRPYL)"); 
             if (! cc.run("DLTLIB FTPTEST3") )
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib3 ftptest " + message);
                   Continue = false;
                }
             }

             if (! cc.run("DLTLIB FTPTEST"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }
             }

             if (! cc.run("CRTLIB FTPTEST3"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, crtlib ftptest3 " + message);
                   Continue = false;
                }
             }

             if (Continue)
             {
                AS400FTP ftp = new AS400FTP(pwrSys_);
                ftp.setSaveFilePublicAuthority("*CHANGE");
                ftp.put(testDirectory + File.separator + "ftptest.savf", "/QSYS.LIB/FTPTEST3.LIB/FTPTEST.SAVF");
                if (! cc.run("RSTLIB SAVLIB(FTPTEST) DEV(*SAVF) SAVF(FTPTEST3/FTPTEST)"))
                {
		   String firstMessage = getFirstMessage(cc);
		   if (firstMessage.indexOf("CPF3848") >= 0) {
		       // OK to see security or format changes 
		   } else { 
		       failed("rstlib failed " + firstMessage);
		       Continue = false;
		   }
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "unexpected exception");
             Continue = false;
          }
       }




       // Test3: cd to ifs, save file name fully qualified.
       if (Continue && (pwrSys_ != null))
       {
          try
          {
             CommandCall cc = new CommandCall(pwrSys_);
	     cc.run("CHGJOB INQMSGRPY(*SYSRPYL)"); 
             if (! cc.run("DLTLIB FTPTEST") )
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }
             }

             if (! cc.run("DLTLIB FTPTEST4"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest4 " + message);
                   Continue = false;
                }
             }

             if (! cc.run("CRTLIB FTPTEST4"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, crtlib ftptest4 " + message);
                   Continue = false;
                }
             }

             if (Continue)
             {
                AS400FTP ftp = new AS400FTP(pwrSys_);
                ftp.cd(initialToken_);
                ftp.cd(testDirectory);
                ftp.put(testDirectory + File.separator + "ftptest.savf", "/QSYS.LIB/FTPTEST4.LIB/FTPTEST.SAVF");
                if (! cc.run("RSTLIB SAVLIB(FTPTEST) DEV(*SAVF) SAVF(FTPTEST4/FTPTEST)"))
                {
		   String firstMessage = getFirstMessage(cc);
		   if (firstMessage.indexOf("CPF3848") >= 0) {
		       // OK to see security or format changes 
		   } else { 
		       failed("rstlib failed " + firstMessage);
		       Continue = false;
		   }
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "unexpected exception");
             Continue = false;
          }
       }




       // Test4: bad save file public authority
       if (Continue && (pwrSys_ != null))
       {
          try
          {
             CommandCall cc = new CommandCall(pwrSys_);
	     cc.run("CHGJOB INQMSGRPY(*SYSRPYL)"); 
             if (! cc.run("DLTLIB FTPTEST") )
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }
             }

             if (! cc.run("DLTLIB FTPTEST5"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest5 " + message);
                   Continue = false;
                }
             }

             if (! cc.run("CRTLIB FTPTEST5"))
             {
                String message = getFirstMessage(cc);
                if (message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, crtlib ftptest5 " + message);
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "unexpected exception");
             Continue = false;
          }

          if (Continue)
          {
             try
             {
                AS400FTP ftp = new AS400FTP(pwrSys_);
                ftp.cd(initialToken_);
                ftp.setSaveFilePublicAuthority("Bad Value");
                ftp.cd(testDirectory);
                ftp.put(testDirectory + File.separator + "ftptest.savf", "/QSYS.LIB/FTPTEST4.LIB/FTPTEST.SAVF");
                failed("no exception when aut bad");
                Continue = false;
             }
             catch (Exception e)
             {
                if (exceptionIs(e, "IOException")) {}
                else
                {
                   failed(e, "Unexpected exception (5)");
                   Continue = false;
                }
             }
          }
       }



       if (Continue)
          succeeded();
    }


    private String getFirstMessage(CommandCall cc)
    {
       AS400Message[] l = cc.getMessageList();
       return l[0].getID() + " " + l[0].getText() ;
    }


    // --------------------------------------------------------------------------
    //  Various cd() tests (using cd testcases to test working with AS400 object
    // --------------------------------------------------------------------------
    public void Var016()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(null);
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
             else
             {
                failed(e, "Unexpected exception (1)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd("");
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (2)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             if (c.cd("/notThere"))
             {
                failed("cd to notThere worked and shouldn't have");
                Continue = false;
             }
             else
             {
                if (! c.getLastMessage().startsWith("550"))
                {
                   failed("wrong message returned " + c.getLastMessage());
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (3)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);

             c.cd(initialToken_);

             if (c.cd(testDirectory))
             {
             }
             else
             {
                {
                   failed("could not cd (1) " + c.getLastMessage());
                   Continue = false;
                }
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (4)");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }








}



