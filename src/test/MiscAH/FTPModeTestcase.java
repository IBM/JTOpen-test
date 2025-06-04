///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FTPModeTestcase.java
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
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable; import java.util.Vector;
import java.util.Properties;
import java.util.Arrays;
import java.lang.String;


public class FTPModeTestcase extends    Testcase
                               implements FTPListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "FTPModeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FTPTest.main(newArgs); 
   }
    private String user_     = null;
    private char[] clearPassword = null;
    String clearPasswordString = null; 
    private String system_   = null;
    private String testDirectory = "FTPTestDir";
    private String testDirectoryDeep = "FTPTestDir/rootDir/subdir2";
    private String initialToken_ = null;
    private int ftpMode_;    // either PASSIVE_MODE or ACTIVE_MODE
    private int otherMode_;  // opposite value of ftpMode_

    // Added following WindowsFTPSleepTime due to intermittent FTP failures where 
    // the FTP::get() fails getting a socket with "425 Not able to open data connection"
    // The failure was debugged by Kent Bruinsma Rich Jirsa, and Brian Jongekryg.  Conclusion,
    // was that the failure appears to be related to Windows OS behavior. Adding wait time to
    // allow Windows client to function without error.
    //private static final int WindowsFTPSleepTime = 1500; //ddpdebug
    private static final int WindowsFTPSleepTime = 0;
    private static final int DETAILED = 1;
    private static final int NAME_ONLY = 0;

    private static final int ONE     =  1;
    private static final int TWO     =  2;
    private static final int FOUR    =  4;
    private static final int EIGHT   =  8;
    private static final int SIXTEEN = 16;
    private static final int TOTAL   = ONE + TWO + FOUR + EIGHT + SIXTEEN;

    private static final String SYSTEM_PROP_NAME = "com.ibm.as400.access.FTP.reuseSocket";

    private FTPEvent ftpEvent = null;

    private boolean cleanup = true;
    private boolean windows_;
    private boolean linux_;
    private boolean aix_;

    public FTPModeTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String userid,
                              String initialToken,
                              int ftpMode)
    {
        super (systemObject,
               "FTPModeTestcase",
               namesAndVars,
               runMode,
               fileOutputStream,
               password);


        initialToken_ = initialToken;
        ftpMode_ = ftpMode;  // either FTP.ACTIVE_MODE or FTP.PASSIVE_MODE
        otherMode_ = (ftpMode_ == FTP.ACTIVE_MODE ? FTP.PASSIVE_MODE : FTP.ACTIVE_MODE);

        if (initialToken_ == null)
        {
           System.out.println("-directory is invalid, no test will be run");
           System.out.println();
        }
        else
        {
          if (FTPTest.DEBUG) {
            System.out.println("using initial token " + initialToken_);
            System.out.println();
          }
        }

        if ((password == null) || (password.length() < 1)) {
          System.out.println("===> warning, variations will fail because no -password specified");
        } else { 
          char[] encryptedPassword = PasswordVault.getEncryptedPassword(password);
          clearPassword = PasswordVault.decryptPassword(encryptedPassword); 
          clearPasswordString = new String(clearPassword); 
        }

        if (systemObject_ != null)
        {
           user_     = userid;
           system_   = systemObject_.getSystemName();
        }

        if (FTPTest.DEBUG) System.out.println();

        if ((user_ == null) || (user_.length() < 1))
           System.out.println("===> warning, variations will fail because no -uid specified");


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
    // Determine operating system we're running under.

    String operatingSystem_ = System.getProperty("os.name");

    windows_ = JTOpenTestEnvironment.isWindows;
      linux_ = JTOpenTestEnvironment.isLinux; 
      aix_ = JTOpenTestEnvironment.isAIX;

    output_.println("Running under: " + operatingSystem_);

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

       targetDir = "targetDirTest25";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest26";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest27";
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) System.out.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest28";
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
       catch (Throwable e)
       {
          System.out.println("Warning!  Cleanup failed, could not delete " + targetDir + " on workstation");
          e.printStackTrace();
       }
    }




    void cleanUpDirsFTP(String targetDir)
    {
       try
       {
          boolean ftpRC;                                          //@A1A
          FTP c = new FTP(system_, user_, clearPasswordString);
          c.setMode(ftpMode_);
          c.cd(initialToken_);
          ftpRC = c.cd(targetDir);                                //@A1C
          if (ftpRC)                                              //@A1A
	  {                                                       //@A1A
	    c.issueCommand("DELE " + "a.a");
	    c.issueCommand("DELE " + "javasp.savf");
	    c.issueCommand("DELE " + "jt400.jar");
	    c.issueCommand("DELE " + "PureJava.html");
	    c.issueCommand("DELE " + "jt400Append.jar");
	    c.issueCommand("DELE " + "PureJavaAppend.html");
	  }                                                       //@A1A
          c.cd(initialToken_);
          c.issueCommand("RMD " + targetDir);
          c.disconnect();
       }
       catch (Exception e)
       {
          System.out.println("Warning!  Cleanup failed, could not delete " + targetDir + " on server");
          e.printStackTrace();
       }
    }




    void deleteDirectoryFTP(String targetDir)
    {
       try
       {
          FTP c = new FTP(system_, user_, clearPasswordString);
          c.setMode(ftpMode_);
          c.cd(initialToken_);
          c.cd(targetDir);

          // Get a list of all files in the target directory.
          // Delete each of those files.
          String[] remoteFileNames = c.ls();
          ///System.out.println("\nRemote files:");
          for (int i=0; i<remoteFileNames.length; i++) {
            ///System.out.println(remoteFileNames[i]);
            c.issueCommand("DELE " + remoteFileNames[i]);
          }

          // Delete the target directory.
          c.cd(initialToken_);
          c.issueCommand("RMD " + targetDir);
          c.disconnect();
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

/**
Set the "FTP.reuseSocket" system property.
**/
    private void setReuseSocketProperty(boolean state)
    {
      Properties sysProps = System.getProperties();
      if (state == true)
        sysProps.put(SYSTEM_PROP_NAME, "true");
      else
        sysProps.put(SYSTEM_PROP_NAME, "false");
      System.setProperties(sysProps);
    }


/**
Clear the "FTP.reuseSocket" system property.
**/
    private void clearReuseSocketProperty()
    {
      Properties sysProps = System.getProperties();
      sysProps.remove(SYSTEM_PROP_NAME);
      System.setProperties(sysProps);
    }



    // ---------------------------------------------------------------------
    //  Various default constructor tests
    // ---------------------------------------------------------------------
    public void Var001()
    {
       boolean Continue = true;
       FTP c = null;

       try
       {
         try
         {
           c = new FTP();
           c.setMode(ftpMode_);
           c.noop();
         }
         catch (Exception e)
         {
           if (exceptionIs(e, "IllegalStateException")) {}
           else
           {
             failed(e, "Unexpected exception (2)");
             Continue = false;
           }
         }

         if (Continue)
         {
           try
           {
             c = new FTP();
             c.setMode(ftpMode_);
             c.setServer(system_);
             c.noop();
           }
           catch (Exception e)
           {
             if (exceptionIs(e, "IllegalStateException")) {}
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
             c = new FTP();
             c.setServer(system_);
             c.setUser(user_);
             c.setMode(ftpMode_);
             c.noop();
           }
           catch (Exception e)
           {
             if (exceptionIs(e, "IllegalStateException")) {}
             else
             {
               failed(e, "Unexpected exception (4)");
               Continue = false;
             }
           }
         }

         if (Continue)
         {
           try
           {
             c = new FTP();
             c.setMode(ftpMode_);
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword(clearPasswordString);
             c.noop();
             c.disconnect();
           }
           catch (Exception e)
           {
             failed(e, "Unexpected exception (5) ");
             Continue = false;
           }
         }

         if (Continue)
           succeeded();
       }
       catch (Exception e) {
         failed(e, "Unexpected exception");
       }
       finally {
         if (c != null) {
           try { c.disconnect(); } catch (Exception e) { e.printStackTrace(); } 
         }
       }
    }









    // ---------------------------------------------------------------------
    //  Various constructor(system) tests
    // ---------------------------------------------------------------------
    public void Var002()
    {
       boolean Continue = true;

          try
          {
             FTP c = new FTP(system_);
             c.setMode(ftpMode_);

             if (! (c.getServer().equals(system_)))
             {
                failed("System not returned correctly");
                Continue = false;
             }
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
             FTP c = new FTP(system_);
             c.setMode(ftpMode_);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException")) {}
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
             FTP c = new FTP(null);
             failed("The expected NullPointerException was not thrown "+c);
             Continue = false;
          }
          catch (NullPointerException e) {}
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP("");
             c.setUser(user_);
             c.setPassword(clearPasswordString);
             c.setMode(ftpMode_);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (4)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_);
             c.setUser(user_);
             c.setMode(ftpMode_);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException")) {}
             else
             {
                failed(e, "Unexpected exception (5)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_);
             c.setUser(user_);
             c.setPassword(clearPasswordString);
             c.setMode(ftpMode_);
             c.noop();
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (6) ");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }









    // ---------------------------------------------------------------------
    //  Various constructor(system, uid, pwd) tests
    // ---------------------------------------------------------------------
    public void Var003()
    {
       boolean Continue = true;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);

             if (! (c.getServer().equals(system_)))
             {
                failed("System not returned correctly");
                Continue = false;
             }

             if (! (c.getUser().equals(user_)))
             {
                failed("User not returned correctly");
                Continue = false;
             }
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
             FTP c = new FTP(null, user_, clearPasswordString);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
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
             FTP c = new FTP(system_, null, clearPasswordString);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
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
             FTP c = new FTP(system_, user_, null);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException")) {}
             else
             {
                failed(e, "Unexpected exception (4)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP("", user_, clearPasswordString);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (5)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, "", clearPasswordString);
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
             else
             {
                failed(e, "Unexpected exception (6)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, "");
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException")) {}
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.noop();
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8) ");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }











    // ---------------------------------------------------------------------
    //  Various ascii tests
    // ---------------------------------------------------------------------
    public void Var004()
    {
       boolean Continue = true;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);

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
             FTP c = new FTP(system_, user_, clearPasswordString);

             c.setMode(ftpMode_);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setDataTransferType(FTP.BINARY);
             c.setDataTransferType(FTP.ASCII);
             c.setMode(ftpMode_);
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
    public void Var005()
    {
       boolean Continue = true;

          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setDataTransferType(FTP.BINARY);
             c.setMode(ftpMode_);
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
             FTP c = new FTP(system_, user_, clearPasswordString);

             c.setDataTransferType(FTP.BINARY);
             c.setMode(ftpMode_);

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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setDataTransferType(FTP.ASCII);
             c.setDataTransferType(FTP.BINARY);
             c.setMode(ftpMode_);
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
    public void Var006()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);

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
    public void Var007()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
    public void Var008()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.cd("rootDir");
             c.cd("subdir1");
             String[] result = c.ls();
             if (result.length == 0) {}
             else
             {
                if ((result.length == 1) && (result[0].indexOf("total") >= 0))
                {
                  c.setMode(otherMode_);
                  /*String[]*/ result = c.ls();
                  if (result.length == 0) {}
                  else
                  {
                    if ((result.length == 1) && (result[0].indexOf("total") >= 0))
                    {}
                    else
                    {
                      for (int i = 0; i < result.length; i++)
                        System.out.println("    " + result[i]);

                      failed("contents of directory incorrect (4a) ");
                      Continue = false;
                    }
                  }
                }
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
    public void Var009()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setMode(ftpMode_);
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
             c.setMode(ftpMode_);
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword(clearPasswordString);
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
             c.setPassword(clearPasswordString);
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
    //  Various get/setTraceOn() tests
    // ---------------------------------------------------------------------
    public void Var010()
    {
       boolean Continue = true;

       if (Continue)
          succeeded();
    }





    // ---------------------------------------------------------------------
    //  Various get/setSystem() tests
    // ---------------------------------------------------------------------
    public void Var011()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setServer(null);
             failed("no exception when system is null");
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
             c.setServer("");
             failed("no exception when system is empty string");
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
             c.setServer("fred");
             if (c.getServer().equals("fred")) {}
             else
             {
                failed("setSystem / getSystem return different values (3)");
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
             c.setServer("fred");
             c.setServer("barney");
             if (c.getServer().equals("barney")) {}
             else
             {
                failed("setSystem / getSystem return different values (4)");
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
             FTP c = new FTP("fred", "uid", "pw");
             if (c.getServer().equals("fred")) {}
             else
             {
                failed("setSystem / getSystem return different values (3)");
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
             FTP c = new FTP("fred", "uid", "pw");
             c.setServer("barney");
             if (c.getServer().equals("barney")) {}
             else
             {
                failed("setSystem / getSystem return different values (6)");
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
             c.setServer("badds8");
             c.setUser(user_);
             c.setPassword(clearPasswordString);
             c.cd(initialToken_);
             failed("no failure when uid bad (system name = badds8) ");
             Continue = false;

          }
          catch (Exception e)
          {
             if (exceptionIs(e, "UnknownHostException"))
             {}
             else
             {
                failed(e, "Unexpected exception (8)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.noop();
             c.setServer("fred");
             failed("no exception when setting server after connection established");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
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
    //  Various setCurrentDirectory() tests
    // ---------------------------------------------------------------------
    public void Var012()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);

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
    public void Var013()
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
             c.setMode(ftpMode_);
             c.setServer(system_);
             c.setUser(user_);
             c.setPassword("fred");
             c.setPassword(clearPasswordString);
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
    public void Var014()
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);

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
             FTP c = new FTP(system_, user_, clearPasswordString);

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
             FTP c = new FTP(system_, user_, clearPasswordString);

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
    public void Var015()
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);

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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);

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
             FTP c = new FTP(system_, user_, clearPasswordString);

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
    public void Var016()
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
    //  Various connect/disconnect() tests
    // ---------------------------------------------------------------------
    public void Var017()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setMode(ftpMode_);
             c.connect();
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
             FTP c = new FTP();
             c.setMode(ftpMode_);
             c.setUser("BillGates");
             c.setPassword("$$$");
             c.connect();
             failed("No exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
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
          try
          {
             FTP c = new FTP();
             c.setMode(ftpMode_);
             c.setServer("mySys");
             c.setUser("BillGates");
             c.connect();
             failed("No exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
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
             c.setServer("mySys");
             c.setPassword("********");
             c.connect();
             failed("No exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
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
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.disconnect();
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.connect();
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (5a)");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.connect();
             c.disconnect();
             if (c.connect())
             {}
             else
             {
                failed("connect after disconnect failed");
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
          succeeded();
    }






    // ---------------------------------------------------------------------
    //  Various get/set Port() tests
    // ---------------------------------------------------------------------
    public void Var018()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             c.setMode(ftpMode_);
             if (c.getPort() == 21)
             {}
             else
             {
                failed("Port not 21");
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
             FTP c = new FTP();
             c.setMode(ftpMode_);
             c.setPort(100);
             if (c.getPort() == 100)
             {}
             else
             {
                failed("Port not 100");
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
             FTP c = new FTP();
             c.setPort(0);
             failed("no exception (3) ");
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
          try
          {
             FTP c = new FTP();
             c.setPort(-5);
             failed("no exception (4) ");
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
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.noop();
             c.setPort(100);
             failed("no exception (5)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalStateException"))
             {}
             else
             {
                failed(e, "Unexpected exception (5)");
                Continue = false;
             }
          }
       }


       if (Continue)
          succeeded();
    }





    // ---------------------------------------------------------------------
    //  Various get(String, String) tests
    // ---------------------------------------------------------------------
    public void Var019()
    {
       boolean Continue = true;
       String targetDir = "targetDirTest19";
       String targetDirFull = targetDir + File.separator;
       if ((aix_) || (windows_)) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       try
       {
          File f = new File(targetDir);
          if (! f.mkdir())
          {
             failed("Variation setup failed, could not create targetDirTest19 on workstation");
             Continue = false;
          }
       }
       catch (Exception e)
       {
          failed(e, "Variation setup failed, could not create targetDirTest19 on workstation");
          Continue = false;
       }


       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("PureJava.html", source))
             {
                if (FTPUtilities.compareFile(source, original))
                {
                  c.setMode(otherMode_); // switch to the other FTP mode

                  if (c.get("PureJava.html", source))
                  {
                    if (FTPUtilities.compareFile(source, original))
                    {}
                    else
                    {
                      failed("compare failed (6a)");
                      Continue = false;
                    }
                  }
                  else
                  {
                    failed("get returned false (6a)");
                    Continue = false;
                  }
                }
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
          System.out.print("7 ");
          try
          {
             String source   = targetDirFull + "jt400.jar";
             String original = testDirectory + File.separator + "jt400.jar";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);
             beansCleanup();
             c.addFTPListener(this);
             if (c.get("jt400.jar", source))
             {
                c.removeFTPListener(this);
                if (FTPUtilities.compareFile(source, original))
                {
                   if ((ftpEvent != null) && (ftpEvent.getID() == FTPEvent.FTP_RETRIEVED))
                   {}
                   else
                   {
                      failed("event not right (7)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("compare failed (7)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (7)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7)");
             Continue = false;
          }
       }


       if (Continue)
       {
          try
          {
          System.out.print("8 ");
             String source   = targetDirFull + "javasp.savf";
             String original = testDirectory + File.separator + "javasp.savf";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("javasp.savf", source))
             {
                if (FTPUtilities.compareFile(source, original))
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
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8)");
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
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
          System.out.print("11 ");
          try
          {
             String source   = targetDir;
             String original = testDirectory + File.separator +
                               "rootDir"     + File.separator +
                               "subdir2"     + File.separator + "FSTOOL.EXE";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             c.get("rootDir/subdir2/FSTOOL.EXE", source);
             {
                failed("no exception (11) original="+original);
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
          Continue = Var019a();

       clearReuseSocketProperty();                                //@A2A
       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various get(String, file) tests
    // ---------------------------------------------------------------------
    boolean Var019a()
    {
       boolean Continue = true;
       String targetDir = "targetDirTest19a";
       String targetDirFull = targetDir + File.separator;

       try
       {
          File f = new File(targetDir);
          if (! f.mkdir())
          {
             failed("Variation setup failed, could not create targetDirTest19a on workstation");
             Continue = false;
          }
       }
       catch (Exception e)
       {
          failed(e, "Variation setup failed, could not create targetDirTest19a on workstation");
          Continue = false;
       }


       if (Continue)
       {
          System.out.print("12 ");
          try
          {
             java.io.File fred = new java.io.File("fred");
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.get(null, fred);
             failed("no exception (1a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (1a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("13 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.get("fred", (java.io.File) null);
             failed("no exception (2a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (2a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("14 ");
          try
          {
             java.io.File fred = new java.io.File("fred");
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.get("", fred);
             failed("no exception (4a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (4a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("15 ");
          try
          {
             java.io.File fred = new java.io.File(targetDirFull + "fred");
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.get("notThere", targetDirFull + fred);
             failed("get of file that doesn't exit returned true");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (5a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("16 ");
          try
          {
             String source   = targetDirFull + "PureJava.html";
             String original = testDirectory + File.separator + "PureJava.html";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("PureJava.html", new java.io.File(source)))
             {
                if (FTPUtilities.compareFile(source, original))
                {}
                else
                {
                   failed("compare failed (6a)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (6a)");
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
          System.out.print("17 ");
          try
          {
             String source   = targetDirFull + "jt400.jar";
             String original = testDirectory + File.separator + "jt400.jar";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);
             beansCleanup();
             c.addFTPListener(this);
             if (c.get("jt400.jar", new java.io.File(source)))
             {
                c.removeFTPListener(this);
                if (FTPUtilities.compareFile(source, original))
                {
                   if ((ftpEvent != null) && (ftpEvent.getID() == FTPEvent.FTP_RETRIEVED))
                   {}
                   else
                   {
                      failed("event not right (7a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("compare failed (7a)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (7a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("18 ");
          try
          {
             String source   = targetDirFull + "javasp.savf";
             String original = testDirectory + File.separator + "javasp.savf";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("javasp.savf", new java.io.File(source)))
             {
                if (FTPUtilities.compareFile(source, original))
                {}
                else
                {
                   failed("compare failed (8a)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (8a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.println("19 ");
          try
          {
             String source   = targetDirFull + "a.a";
             String original = testDirectory + File.separator + "a.a";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.ASCII);

             if (c.get("a.a", new java.io.File(source)))
             {
                if (FTPUtilities.compareFile(source, original))
                {}
                else
                {
                   System.out.println();
                   System.out.println("   warning, ascii compare failed (9a) ");
                   cleanup = false;
                   // failed("compare failed (9a)");
                   // Continue = false;
                }
             }
             else
             {
                failed("get returned false (9a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (9a)");
             Continue = false;
          }
       }



       return Continue;

    }



    // ---------------------------------------------------------------------
    //  Various stream get(String) tests
    // ---------------------------------------------------------------------
    public void Var020()
    {
       boolean Continue = true;
       String targetDir = "targetDirTest20";
       String targetDirFull = targetDir + File.separator;
       if ((aix_) || (windows_)) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       try
       {
          File f = new File(targetDir);
          if (! f.mkdir())
          {
             failed("Variation setup failed, could not create targetDirTest20 on workstation");
             Continue = false;
          }
       }
       catch (Exception e)
       {
          failed(e, "Variation setup failed, could not create targetDirTest20 on workstation");
          Continue = false;
       }


       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             InputStream i = c.get(null);
             failed("no exception (1) for "+i);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             InputStream i = c.get("");
             failed("no exception (3) for " +i );
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
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             InputStream i = c.get("notThere");
             if (i != null)
             {
                failed("no exception on get of file that doesn't exist");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
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
          System.out.print("4");
          try
          {
             String target   = targetDirFull + "PureJava.html";
             String original = testDirectory + File.separator + "PureJava.html";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("PureJava.html");

             if (i != null)
             {
                System.out.print("v ");
                FTPUtilities.copy(i, target);

                if (FTPUtilities.compareFile(target, original))
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
          System.out.print("5");
          try
          {
             String target   = targetDirFull + "jt400.jar";
             String original = testDirectory + File.separator + "jt400.jar";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("jt400.jar");
             if (i != null)
             {
                System.out.print("v ");
                FTPUtilities.copy(i, target);
                if (FTPUtilities.compareFile(target, original))
                {}
                else
                {
                   failed("compare failed (7)");
                   Continue = false;
                }
             }
             else
             {
                failed("get returned false (7)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("6");
          try
          {
             String target   = targetDirFull + "javasp.savf";
             String original = testDirectory + File.separator + "javasp.savf";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("javasp.savf");
             if (i != null)
             {
                System.out.print("v ");
                FTPUtilities.copy(i, target);
                if (FTPUtilities.compareFile(target, original))
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
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("7");
          try
          {
             String target   = targetDirFull + "a.a";
             String original = testDirectory + File.separator + "a.a";

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.ASCII);
             beansCleanup();
             c.addFTPListener(this);
             InputStream i = c.get("a.a");
             if (i != null)
             {
                System.out.println("v ");
                FTPUtilities.copy(i, target);
                c.removeFTPListener(this);
                if (FTPUtilities.compareFile(target, original))
                {
                   if ((ftpEvent != null) && (ftpEvent.getID() == FTPEvent.FTP_RETRIEVED))
                   {}
                   else
                   {
                      failed("event not right (9)");
                      Continue = false;
                   }
                }
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
          catch (Exception e)
          {
             failed(e, "Unexpected exception (9)");
             Continue = false;
          }
       }


       clearReuseSocketProperty();                                //@A2A
       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various put(String, String) tests
    // ---------------------------------------------------------------------
    public void Var021()
    {
       boolean Continue = true;

       String  compareDir     = "targetDirTest21";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;
       if ((aix_) || (windows_) || linux_) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("Variation setup failed, could not create targetDirTest21 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest21 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("2 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("Variation setup failed, could not create targetDirTest21 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest21 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
          System.out.print("8");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             beansCleanup();
             c.addFTPListener(this);
             if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
             if (c.put(original, target))
             {
                System.out.print("v ");
                FTPEvent localFTPEvent = ftpEvent;
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   c.removeFTPListener(this);
                   if (FTPUtilities.compareFile(compare, original))
                   {
                      if ((localFTPEvent != null) && (localFTPEvent.getID() == FTPEvent.FTP_PUT))
                      {}
                      else
                      {
                         failed("event not right (6)");
                         Continue = false;
                      }
                   }
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
             else
             {
                failed("put returned false (6)");
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
          System.out.print("9");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
             if (c.put(original, target))
             {
                System.out.print("v ");

                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (7)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (7)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (7)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7)");
             Continue = false;
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
             if (c.put(original, target))
             {
               if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
             if (c.put(original, target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
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
          System.out.print("12 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             c.put(original, target);
             failed("no exception (10) for "+compare);
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
          Continue = Var021a();

       clearReuseSocketProperty();

       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various put(File, String) tests
    // ---------------------------------------------------------------------
    boolean Var021a()
    {
       boolean Continue = true;

       String  compareDir     = "targetDirTest21a";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          System.out.print("13 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("Variation setup failed, could not create targetDirTest21a on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest21a on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("14 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("Variation setup failed, could not create targetDirTest21a on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest21a on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("15 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.put((java.io.File) null, "fred");
             failed("no exception (1a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (1a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("16 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.put(new java.io.File("fred"), null);
             failed("no exception (2a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (2a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("17 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.put(new java.io.File("fred"), "");
             failed("no exception (3a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (3a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("18 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.put(new java.io.File(targetDirFull + "fred"), "notThere");
             failed("put of file that doesn't exist -- no exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (5a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("19");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             beansCleanup();
             c.addFTPListener(this);
             if (c.put(new java.io.File(original), target))
             {
                FTPEvent localFTPEvent = ftpEvent;
                System.out.print("v ");
                if (c.get(target, compare))
                {
                   c.removeFTPListener(this);
                   if (FTPUtilities.compareFile(compare, original))
                   {
                      if ((localFTPEvent != null) && (localFTPEvent.getID() == FTPEvent.FTP_PUT))
                      {}
                      else
                      {
                         failed("event not right (6a)");
                         Continue = false;
                      }
                   }
                   else
                   {
                      failed("compare failed (6a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (6a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (6a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (6a)");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("20");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (7a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (7a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (7a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("21");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (8a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (8a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (8a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("22");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.put(new java.io.File(original), target))
             {
                System.out.println("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      System.out.println();
                      System.out.println("   warning, ascii compare failed (9a) ");
                      cleanup = false;
                      // failed("compare failed (9a)");
                      // Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (9a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (9a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (9a)");
             Continue = false;
          }
       }

       return Continue;

    }





    // ---------------------------------------------------------------------
    //  Various outputStream put(String) tests
    // ---------------------------------------------------------------------
    public void Var022()
    {
       boolean Continue = true;

       String  compareDir     = "targetDirTest22";
       String  compareDirFull = compareDir + File.separator;
       if ((aix_) || (windows_) || linux_) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("Variation setup failed, could not create targetDirTest22 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest22 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("2 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("Variation setup failed, could not create targetDirTest21 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest21 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             OutputStream o = c.put(null);
             failed("no exception (1) for "+o);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             OutputStream o = c.put("");
             failed("no exception (4) for "+o);
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
          System.out.print("5");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 5v.
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.put(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
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
             else
             {
                failed("put returned false (6)");
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
          System.out.print("6");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 6v.
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.put(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (7)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (7)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (7)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("7");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 7v.
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.put(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
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
          System.out.print("8");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 8v.
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             beansCleanup();
             c.addFTPListener(this);
             OutputStream o = c.put(target);
             FTPEvent localFTPEvent = ftpEvent;
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.println("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   c.removeFTPListener(this);
                   if (FTPUtilities.compareFile(compare, original))
                   {
                      if ((localFTPEvent != null) && (localFTPEvent.getID() == FTPEvent.FTP_PUT))
                      {}
                      else
                      {
                         failed("event not right (9)");
                         Continue = false;
                      }
                   }
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



       clearReuseSocketProperty();                                //@A2A
       if (Continue)
          succeeded();


    }





    // ---------------------------------------------------------------------
    //  Various get/set bufferSize() tests
    // ---------------------------------------------------------------------
    public void Var023()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP();
             if (c.getBufferSize() == 4096)
             {}
             else
             {
                failed("Default buffer size incorrect " + c.getBufferSize());
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
             FTP c = new FTP();
             c.setBufferSize(100);
             if (c.getBufferSize() == 100)
             {}
             else
             {
                failed("Buffer size not 100");
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
             FTP c = new FTP();
             c.setBufferSize(0);
             failed("no exception (3) ");
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
          try
          {
             FTP c = new FTP();
             c.setBufferSize(-5);
             failed("no exception (4) ");
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
          succeeded();
    }


    // ---------------------------------------------------------------------
    //  Various get/put error tests
    // ---------------------------------------------------------------------
    public void Var024()
    {
       boolean Continue = true;


       if (Continue)
       {
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
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
    //  Various append(String, String) tests
    // ---------------------------------------------------------------------
    public void Var025()
    {
       boolean Continue = true;

       String  compareDir     = "targetDirTest25";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;
       if ((aix_) || (windows_) || linux_ ) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("Variation setup failed, could not create targetDirTest25 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest25 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("2 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("Variation setup failed, could not create targetDirTest25 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest25 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.append((java.io.File) null, "fred");
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.append("fred", null);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.append("fred", "");
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.append("", "fred");
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.append(targetDirFull + "fred", "notThere");
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
          System.out.print("8");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 8v.
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {
                   }
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
             else
             {
                failed("put returned false (6)");
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
          System.out.print("9");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 9v.
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (7)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (7)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (7)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7)");
             Continue = false;
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 10v.
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
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

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.append(original, target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
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
          System.out.print("12 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             c.append(original, target);
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
       {
          System.out.print("13");
          try
          {
             String target   = "PureJavaAppend.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 13v.
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (13)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (13)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (13)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (13)");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("14");
          try
          {
             String target   = "jt400Append.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             if (aix_) c.setReuseSocket(false);  // Avoid hang in block 14v.
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {
                   }
                   else
                   {
                      failed("compare failed (14)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (14)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (14)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (14)");
             Continue = false;
          }
       }

       System.out.println();
       
       clearReuseSocketProperty();                                //@A2A
       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various put(File, String) tests
    // ---------------------------------------------------------------------
    public void Var026()
    {
       boolean Continue = true;

       String  compareDir     = "targetDirTest26";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;
       if ((aix_) || (windows_) || linux_) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       if (Continue)
       {
          System.out.print("13 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("Variation setup failed, could not create targetDirTest26 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest26 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("14 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("Variation setup failed, could not create targetDirTest26 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest26 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("15 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.append((java.io.File) null, "fred");
             failed("no exception (1a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (1a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("16 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.append(new java.io.File("fred"), null);
             failed("no exception (2a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "NullPointerException"))
             {}
             else
             {
                failed(e, "Unexpected exception (2a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("17 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.append(new java.io.File("fred"), "");
             failed("no exception (3a)");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "IllegalArgumentException"))
             {}
             else
             {
                failed(e, "Unexpected exception (3a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("18 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.append(new java.io.File(targetDirFull + "fred"), "notThere");
             failed("put of file that doesn't exist -- no exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "FileNotFoundException"))
             {}
             else
             {
                failed(e, "Unexpected exception (5a)");
                Continue = false;
             }
          }
       }

       if (Continue)
       {
          System.out.print("19");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {
                   }
                   else
                   {
                      failed("compare failed (6a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (6a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (6a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (6a)");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("20");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (7a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (7a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (7a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("21");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (8a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (8a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (8a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("22");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.setMode(ftpMode_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.append(new java.io.File(original), target))
             {
                System.out.println("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      System.out.println();
                      System.out.println("   warning, ascii compare failed (9a) ");
                      cleanup = false;
                      // failed("compare failed (9a)");
                      // Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (9a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (9a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (9a)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("23");
          try
          {
             String target   = "PureJavaAppend.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (8a)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (8a)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (8a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8a)");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("24");
          try
          {
             String target   = "jt400Append.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(new java.io.File(original), target))
             {
                System.out.print("v ");
                if (windows_) Thread.sleep(WindowsFTPSleepTime); // Avoid windows failures (FTP - 425 Not able to open data connection)
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {
                   }
                   else
                   {
                      failed("compare failed (24v)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (24v)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (24)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (24)");
             Continue = false;
          }
       }

       clearReuseSocketProperty();

       System.out.println();

       clearReuseSocketProperty();                                //@A2A
       if (Continue)
          succeeded();

    }





    // ---------------------------------------------------------------------
    //  Various outputStream put(String) tests
    // ---------------------------------------------------------------------
    public void Var027()
    {
       boolean Continue = true;

       String  compareDir     = "targetDirTest27";
       String  compareDirFull = compareDir + File.separator;
       if ((aix_) || (windows_)) setReuseSocketProperty(false);   //@A2A
       else setReuseSocketProperty(true);                         //@A2A

       if (Continue)
       {
          System.out.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("Variation setup failed, could not create targetDirTest27 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest27 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("2 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("Variation setup failed, could not create targetDirTest21 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Variation setup failed, could not create targetDirTest21 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          System.out.print("3 ");
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString);
             OutputStream o = c.append(null);
             failed("no exception (1) for "+o);
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
             FTP c = new FTP(system_, user_, clearPasswordString);
             OutputStream o = c.append("");
             failed("no exception (4) for "+ o);
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
          System.out.print("5");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
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
             else
             {
                failed("put returned false (6)");
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
          System.out.print("6");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {}
                   else
                   {
                      failed("compare failed (7)");
                      Continue = false;
                   }
                }
                else
                {
                   failed("get returned false (7)");
                   Continue = false;
                }
             }
             else
             {
                failed("put returned false (7)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (7)");
             Continue = false;
          }
       }


       if (Continue)
       {
          System.out.print("7");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

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
          System.out.print("8");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.println("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
                   {
                   }
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
          System.out.print("9");
          try
          {
             String target   = "PureJavaAppend.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

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
          System.out.print("10");
          try
          {
             String target   = "jt400Append.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                System.out.print("v ");
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original))
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
             else
             {
                failed("put returned false (6)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (6)");
             Continue = false;
          }
       }


       System.out.println();

       clearReuseSocketProperty();                                //@A2A
       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various ren(String, String) tests
    // ---------------------------------------------------------------------
    public void Var028()
    {
	boolean passed=false; 
      String  compareDir     = "targetDirTest28";
      String  compareDirFull = compareDir + File.separator;
      FTP conn = null;
      if (aix_ || linux_ || windows_) setReuseSocketProperty(false);  // AIX: Avoid hang when reusing socket.  Linux: Avoid list mismatch in step 6.  Windows: Avoid cleanup errors.

      try
      {
	  StringBuffer sb = new StringBuffer();
	  sb.append("\n"); 
        sb.append("1.  creating directory\n"); 
        File f = new File(compareDir);
        if (! f.mkdir())
        {
          failed("Variation setup failed, could not create targetDirTest28 on workstation"+sb.toString());
          return;
        }


        sb.append("2 \n");
	sb.append("..new FTP "+system_+","+user_+"\n"); 
        conn = new FTP(system_, user_, clearPasswordString);
	sb.append("setMode("+ftpMode_+")\n"); 
        conn.setMode(ftpMode_);
	sb.append("..cd "+initialToken_+"\n"); 
        conn.cd(initialToken_);
        String message = conn.issueCommand("MKD " + compareDir);

        // Success could be 250 (Requested file action okay)
        // or 257 (PathName created)
        if (! message.startsWith("25"))
        {
	    // Try to delete it first
	    deleteDirectoryFTP(compareDir);
	    message = conn.issueCommand("MKD " + compareDir);     
	}

        if (! message.startsWith("25"))
        {
          failed("Variation setup failed, could not create targetDirTest28 on target via FTP.  Message: " + message + sb.toString());
          return;
        }
        else {
	    sb.append("cd->"+compareDir+" \n"); 
	    conn.cd(compareDir);
	}

        // Create a bunch of files on the current system, with names conforming to various patterns.
        // 'put' the files to the remote system.
        // Rename some of the files (on the remote system) according to specific patterns.
        // Verify that the files got renamed as expected, and nothing else got renamed.

        sb.append("3 \n");
        for (int i=0; i<3; i++) {
          FTPUtilities.createFile(compareDirFull+"file"+i+".txt", "data"+i);
          FTPUtilities.createFile(compareDirFull+"file"+i+".lst", "data"+i);
          FTPUtilities.createFile(compareDirFull+"file"+i+"lst", "data"+i);
          FTPUtilities.createFile(compareDirFull+"file."+i+".lst", "data"+i);

          FTPUtilities.createFile(compareDirFull+"test"+i+".txt", "data"+i);
          FTPUtilities.createFile(compareDirFull+"test"+i+".lst", "data"+i);
          FTPUtilities.createFile(compareDirFull+"test"+i+"lst", "data"+i);
          FTPUtilities.createFile(compareDirFull+"test."+i+".lst", "data"+i);
        }

        sb.append("4 \n");
        File compareDirectory = new File(compareDir);
        File[] localFiles = compareDirectory.listFiles();
        ///System.out.println("\nLocal files:");
        for (int i=0; i<localFiles.length; i++) {
          ///sb.append(localFiles[i].getPath());
          RandomAccessFile raf = new RandomAccessFile(localFiles[i], "r");
          ///System.out.println(" ; contents==|"+raf.readUTF()+"|");
          conn.put(localFiles[i], localFiles[i].getName());
          raf.close(); // Need to close or won't be able to delete later @A1A
        }

        String[] remoteFileNames;


        sb.append("5 \n");
        remoteFileNames = conn.ls();
        String[] expected5 = {
          "file0.txt",  "file1.txt",  "file2.txt",
          "file0.lst",  "file1.lst",  "file2.lst",
          "file0lst",   "file1lst",   "file2lst", 
          "file.0.lst", "file.1.lst", "file.2.lst",

          "test0.txt",  "test1.txt",  "test2.txt",
          "test0.lst",  "test1.lst",  "test2.lst",
          "test0lst",   "test1lst",   "test2lst", 
          "test.0.lst", "test.1.lst", "test.2.lst"
        };
        if (!listsMatch(remoteFileNames, expected5, sb)) {
          failed("File list is not as expected"+ sb.toString());
          return;
        }


        sb.append("6 rename *.txt to *.DONE\n ");
        conn.ren("*.txt","*.DONE");
        remoteFileNames = conn.ls();
        String[] expected6 = {
          "file0.DONE",  "file1.DONE",  "file2.DONE",
          "file0.lst",  "file1.lst",  "file2.lst",
          "file0lst",   "file1lst",   "file2lst", 
          "file.0.lst", "file.1.lst", "file.2.lst",

          "test0.DONE",  "test1.DONE",  "test2.DONE",
          "test0.lst",  "test1.lst",  "test2.lst",
          "test0lst",   "test1lst",   "test2lst", 
          "test.0.lst", "test.1.lst", "test.2.lst"
        };
        if (!listsMatch(remoteFileNames, expected6, sb)) {
          failed("File list is not as expected"+ sb.toString());
          return;
        }


        sb.append("7 \n");
        conn.ren("*.lst","*_DONE.*");
        remoteFileNames = conn.ls();
        String[] expected7 = {
          "file0.DONE",  "file1.DONE",  "file2.DONE",
          "file0_DONE.lst",  "file1_DONE.lst",  "file2_DONE.lst",
          "file0lst",   "file1lst",   "file2lst", 
          "file.0_DONE.lst", "file.1_DONE.lst", "file.2_DONE.lst",

          "test0.DONE",  "test1.DONE",  "test2.DONE",
          "test0_DONE.lst",  "test1_DONE.lst",  "test2_DONE.lst",
          "test0lst",   "test1lst",   "test2lst", 
          "test.0_DONE.lst", "test.1_DONE.lst", "test.2_DONE.lst"
        };
        if (!listsMatch(remoteFileNames, expected7, sb)) {
          failed("File list is not as expected"+ sb.toString());
          return;
        }


///        sb.append("8 ");
///        conn.ren("test*","boogy*");
///        remoteFileNames = conn.ls();
///        String[] expected8 = {
///          "file0.DONE",  "file1.DONE",  "file2.DONE",
///          "file0_DONE.lst",  "file1_DONE.lst",  "file2_DONE.lst",
///          "file0lst",   "file1lst",   "file2lst", 
///          "file.0_DONE.lst", "file.1_DONE.lst", "file.2_DONE.lst",
///
///          "boogy0.DONE",  "boogy1.DONE",  "boogy2.DONE",
///          "boogy0_DONE.lst",  "boogy1_DONE.lst",  "boogy2_DONE.lst",
///          "boogy0lst",   "boogy1lst",   "boogy2lst", 
///          "boogy.0_DONE.lst", "boogy.1_DONE.lst", "boogy.2_DONE.lst"
///        };
///        if (!listsMatch(remoteFileNames, expected8)) {
///          failed("File list is not as expected"+ sb.toString());
///          return;
///        }


        sb.append("8 \n");
        conn.ren("*.lst","*_1055am");
        remoteFileNames = conn.ls();
        String[] expected8 = {
          "file0.DONE",  "file1.DONE",  "file2.DONE",
          "file0_DONE.lst_1055am",  "file1_DONE.lst_1055am",  "file2_DONE.lst_1055am",
          "file0lst",   "file1lst",   "file2lst", 
          "file.0_DONE.lst_1055am", "file.1_DONE.lst_1055am", "file.2_DONE.lst_1055am",

          "test0.DONE",  "test1.DONE",  "test2.DONE",
          "test0_DONE.lst_1055am",  "test1_DONE.lst_1055am",  "test2_DONE.lst_1055am",
          "test0lst",   "test1lst",   "test2lst", 
          "test.0_DONE.lst_1055am", "test.1_DONE.lst_1055am", "test.2_DONE.lst_1055am"
        };
        if (!listsMatch(remoteFileNames, expected8, sb)) {
          failed("File list is not as expected"+ sb.toString());
          return;
        }

        ///     sb.append("3 ");
        ///     try
        ///     {
        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.put((java.io.File) null, "fred");
        ///       failed("no exception (1)"+ sb.toString());
        ///       return;
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       if (exceptionIs(e, "NullPointerException"))
        ///       {}
        ///       else
        ///       {
        ///         failed(e, "Unexpected exception (1)"+ sb.toString());
        ///         return;
        ///       }
        ///     }


        ///     sb.append("4 ");
        ///     try
        ///     {
        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.put("fred", null);
        ///       failed("no exception (2)"+ sb.toString());
        ///       return;
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       if (exceptionIs(e, "NullPointerException"))
        ///       {}
        ///       else
        ///       {
        ///         failed(e, "Unexpected exception (2)"+ sb.toString());
        ///         return;
        ///       }
        ///     }


        ///     sb.append("5 ");
        ///     try
        ///     {
        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.put("fred", "");
        ///       failed("no exception (3)"+ sb.toString());
        ///       return;
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       if (exceptionIs(e, "IllegalArgumentException"))
        ///       {}
        ///       else
        ///       {
        ///         failed(e, "Unexpected exception (3)"+ sb.toString());
        ///         return;
        ///       }
        ///     }


        ///     sb.append("6 ");
        ///     try
        ///     {
        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.put("", "fred");
        ///       failed("no exception (4)"+ sb.toString());
        ///       return;
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       if (exceptionIs(e, "IllegalArgumentException"))
        ///       {}
        ///       else
        ///       {
        ///         failed(e, "Unexpected exception (4)"+ sb.toString());
        ///         return;
        ///       }
        ///     }


        ///     sb.append("7 ");
        ///     try
        ///     {
        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.cd(initialToken_);
        ///       c.cd(testDirectory);
        ///       c.put(targetDirFull + "fred", "notThere");
        ///       failed("put of file that doesn't exist -- no exception"+ sb.toString());
        ///       return;
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       if (exceptionIs(e, "FileNotFoundException"))
        ///       {}
        ///       else
        ///       {
        ///         failed(e, "Unexpected exception (5)"+ sb.toString());
        ///         return;
        ///       }
        ///     }


        ///     sb.append("8");
        ///     try
        ///     {
        ///       String target   = "jt400.jar";
        ///       String original = testDirectory  + File.separator + target;
        ///       String compare  = compareDirFull + target;

        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.cd(initialToken_);
        ///       c.cd(compareDir);
        ///       c.setDataTransferType(FTP.BINARY);

        ///       beansCleanup();
        ///       c.addFTPListener(this);
        ///       if (c.put(original, target))
        ///       {
        ///         sb.append("v ");
        ///         FTPEvent localFTPEvent = ftpEvent;
        ///         if (c.get(target, compare))
        ///         {
        ///           c.removeFTPListener(this);
        ///           if (FTPUtilities.compareFile(compare, original))
        ///           {
        ///             if ((localFTPEvent != null) && (localFTPEvent.getID() == FTPEvent.FTP_PUT))
        ///             {}
        ///             else
        ///             {
        ///               failed("event not right (6)"+ sb.toString());
        ///               return;
        ///             }
        ///           }
        ///           else
        ///           {
        ///             failed("compare failed (6)"+ sb.toString());
        ///             return;
        ///           }
        ///         }
        ///         else
        ///         {
        ///           failed("get returned false (6)"+ sb.toString());
        ///           return;
        ///         }
        ///       }
        ///       else
        ///       {
        ///         failed("put returned false (6)"+ sb.toString());
        ///         return;
        ///       }
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       failed(e, "Unexpected exception (6)"+ sb.toString());
        ///       return;
        ///     }


        ///     sb.append("9");
        ///     try
        ///     {
        ///       String target   = "javasp.savf";
        ///       String original = testDirectory  + File.separator + target;
        ///       String compare  = compareDirFull + target;

        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.cd(initialToken_);
        ///       c.cd(compareDir);
        ///       c.setDataTransferType(FTP.BINARY);

        ///       if (c.put(original, target))
        ///       {
        ///         sb.append("v ");
        ///         if (c.get(target, compare))
        ///         {
        ///           if (FTPUtilities.compareFile(compare, original))
        ///           {}
        ///           else
        ///           {
        ///             failed("compare failed (7)"+ sb.toString());
        ///             return;
        ///           }
        ///         }
        ///         else
        ///         {
        ///           failed("get returned false (7)"+ sb.toString());
        ///           return;
        ///         }
        ///       }
        ///       else
        ///       {
        ///         failed("put returned false (7)"+ sb.toString());
        ///         return;
        ///       }
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       failed(e, "Unexpected exception (7)"+ sb.toString());
        ///       return;
        ///     }



        ///     sb.append("10");
        ///     try
        ///     {
        ///       String target   = "PureJava.html";
        ///       String original = testDirectory  + File.separator + target;
        ///       String compare  = compareDirFull + target;

        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.cd(initialToken_);
        ///       c.cd(compareDir);
        ///       c.setDataTransferType(FTP.BINARY);

        ///       if (c.put(original, target))
        ///       {
        ///         sb.append("v ");
        ///         if (c.get(target, compare))
        ///         {
        ///           if (FTPUtilities.compareFile(compare, original))
        ///           {}
        ///           else
        ///           {
        ///             failed("compare failed (8)"+ sb.toString());
        ///             return;
        ///           }
        ///         }
        ///         else
        ///         {
        ///           failed("get returned false (8)"+ sb.toString());
        ///           return;
        ///         }
        ///       }
        ///       else
        ///       {
        ///         failed("put returned false (8)"+ sb.toString());
        ///         return;
        ///       }
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       failed(e, "Unexpected exception (8)"+ sb.toString());
        ///       return;
        ///     }



        ///     sb.append("11");
        ///     try
        ///     {
        ///       String target   = "a.a";
        ///       String original = testDirectory  + File.separator + target;
        ///       String compare  = compareDirFull + target;

        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.cd(initialToken_);
        ///       c.cd(compareDir);
        ///       c.setDataTransferType(FTP.ASCII);
        ///       if (c.put(original, target))
        ///       {
        ///         sb.append("v ");
        ///         if (c.get(target, compare))
        ///         {
        ///           if (FTPUtilities.compareFile(compare, original))
        ///           {}
        ///           else
        ///           {
        ///             System.out.println();
        ///             System.out.println("   warning, ascii compare failed (9) ");
        ///             cleanup = false;
        ///             // failed("compare failed (9)");
        ///             // return;
        ///           }
        ///         }
        ///         else
        ///         {
        ///           failed("get returned false (9)"+ sb.toString());
        ///           return;
        ///         }
        ///       }
        ///       else
        ///       {
        ///         failed("put returned false (9)"+ sb.toString());
        ///         return;
        ///       }
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       failed(e, "Unexpected exception (9)"+ sb.toString());
        ///       return;
        ///     }


        ///     sb.append("12 ");
        ///     try
        ///     {
        ///       String target   = "PureJava.html";
        ///       String original = testDirectory;
        ///       String compare  = compareDirFull + target;

        ///       FTP c = new FTP(system_, user_, password_);
        ///       c.cd(initialToken_);
        ///       c.cd(compareDir);
        ///       c.setDataTransferType(FTP.BINARY);

        ///       c.put(original, target);
        ///       failed("no exception (10)"+ sb.toString());
        ///       return;
        ///     }
        ///     catch (Exception e)
        ///     {
        ///       if (exceptionIs(e,"FileNotFoundException"))
        ///       {}
        ///       else
        ///       {
        ///         failed(e, "Unexpected exception (10)"+ sb.toString());
        ///         return;
        ///       }
        ///     }
        // System.out.println();
	passed = true; 
        succeeded();
      }
      catch (Exception e) {
        failed(e, "Unexpected exception");
      }
      finally {
        if (conn != null) {
          try { conn.disconnect(); } catch (Exception e) { e.printStackTrace(); }
          conn = null;
        }
        // deleteDirectoryFTP(targetDirFull); // Trailing delim is a problem @A1D
	if (passed) { 
	    deleteDirectoryFTP(compareDir);       //@A1A
	} else {
	    System.out.println("Not deleting "+compareDir); 
	}
        ///clearReuseSocketProperty();
      }
    }


    boolean listsMatch(String[] got, String[] expected, StringBuffer sb)
    {
      if (got == null) { 
        sb.append("listMatch: Error -- got is null\n");
        return false; 
      }
      if (expected == null) { 
        sb.append("listMatch: Error -- expected is null\n");
        return false; 
      }
      Arrays.sort(got);
      Arrays.sort(expected);
      for (int i=0; i<Math.min(got.length, expected.length); i++) {
        if (! got[i].equals(expected[i])) {
          sb.append("listMatch: Error --  Got |"+got[i]+"|, expected |"+expected[i]+"|\n");
          return false;
        }
      }
      if (got.length != expected.length) {
        sb.append("listMatch: Error -- List lengths mismatch: " + got.length + " versus " + expected.length+"\n");
        return false;
      }
      return true;
    }


}



