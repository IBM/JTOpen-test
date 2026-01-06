///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400FTPTestcase.java
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
import test.Testcase;

import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.String;


public class AS400FTPTestcase extends    Testcase
                         implements FTPListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400FTPTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FTPTest.main(newArgs); 
   }
    private String user_     = null;
    private String password_ = null;
    private String system_   = null;
    private String testDirectory = "FTPTestDir";
    private String testDirectoryDeep = "FTPTestDir/rootDir/subdir2";
    private String initialToken_ = null;
    // private boolean notWorthTrying = false;

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

    public AS400FTPTestcase (AS400 systemObject,
                        Hashtable<String, Vector<String>> namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        String password,
                        String userid,
                        String initialToken,
                        AS400  powerSystem)
    {
        super (systemObject,
               "AS400FTPTestcase",
               namesAndVars,
               runMode,
               fileOutputStream,
               password);


        initialToken_ = initialToken;
        pwrSys_       = powerSystem;

        if (initialToken_ == null)
        {
           output_.println("-directory is invalid, no test will be run");
           // notWorthTrying = true;
           output_.println();
        }
        else
        {
          if (FTPTest.DEBUG) {
            output_.println("using initial token " + initialToken_);
            output_.println();
          }
        }


        if (systemObject_ != null)
        {
           user_     = userid;
           password_ = password;
           system_   = systemObject_.getSystemName();
        }

        if (FTPTest.DEBUG) output_.println();

        if ((user_ == null) || (user_.length() < 1))
           output_.println("===> warning, variations will fail because no -uid specified");

        if ((password_ == null) || (password_.length() < 1))
           output_.println("===> warning, variations will fail because no -password specified");

        if ((system_ == null) || (system_.length() < 1))
           output_.println("===> warning, variations will fail because no -system specified");

        if (pwrSys_ == null)
           output_.println("===> warning, variations will fail because no power system (-misc uid,pwd,token)");

        if (FTPTest.DEBUG) output_.println();

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
       if (FTPTest.DEBUG) output_.println();

       String targetDir = "targetDirTest19";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);

       targetDir = "targetDirTest19a";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);

       targetDir = "targetDirTest20";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);

       targetDir = "targetDirTest21";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest21a";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest22";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest24";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest25";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest26";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       targetDir = "targetDirTest27";
       if (FTPTest.DEBUG) output_.println("Cleaning up (on client) " + targetDir);
       cleanUpDirs2(targetDir);
       if (FTPTest.DEBUG) output_.println("Cleaning up (on server) " + targetDir);
       cleanUpDirsFTP(targetDir);

       if (FTPTest.DEBUG) output_.println();
    }




    void cleanUpDirs2(String targetDir)
    {
       try
       {
          if (! FTPUtilities.deleteDirectory(targetDir,output_))
          {
             output_.println("Warning!  Cleanup failed, could not delete " + targetDir + " on workstation");
          }
       }
       catch (Exception e)
       {
          output_.println("Warning!  Cleanup failed, could not delete " + targetDir + " on workstation");
          e.printStackTrace();
       }
    }




    void cleanUpDirsFTP(String targetDir)
    {
       try
       {
          AS400FTP c = new AS400FTP(systemObject_);
          c.cd(initialToken_);
          c.cd(targetDir);
          c.issueCommand("DELE " + "a.a");
          c.issueCommand("DELE " + "javasp.savf");
          c.issueCommand("DELE " + "jt400.jar");
          c.issueCommand("DELE " + "PureJava.html");
          c.issueCommand("DELE " + "jt400Append.jar");
          c.issueCommand("DELE " + "PureJavaAppend.html");
          c.cd(initialToken_);
          c.issueCommand("RMD " + targetDir);
       }
       catch (Exception e)
       {
          output_.println("Warning!  Cleanup failed, could not delete " + targetDir + " on server");
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
    //  Various default constructor tests
    // ---------------------------------------------------------------------
    public void Var001()
    {
       boolean Continue = true;

//          try
//          {
//             AS400FTP c = new AS400FTP();
//          }
//          catch (Exception e)
//          {
//             failed(e, "Unexpected exception (1)");
//             Continue = false;
//          }

       if (Continue)
       {
          try
          {
             AS400FTP c = new AS400FTP();
             c.noop();
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "ExtendedIllegalStateException")) {}
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
             AS400FTP c = new AS400FTP();
             c.setSystem(systemObject_);
             c.noop();
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
    //  Various constructor(system) tests
    // ---------------------------------------------------------------------
    public void Var002()
    {
       boolean Continue = true;

          try
          {
             AS400FTP c = new AS400FTP(systemObject_);

             if (c.getSystem() != systemObject_)
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
             AS400FTP c = new AS400FTP(systemObject_);
             c.noop();
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (2)");
          }
       }

       if (Continue)
       {
          try
          {
             AS400FTP c = new AS400FTP(null);
             failed("Exception was not thrown."+c);
          }
          catch (NullPointerException e)
          {
//             if (exceptionIs(e, "NullPointerException")) {}
//             else
//             {
//                failed(e, "Unexpected exception (3)");
//                Continue = false;
//             }
          }
       }


       if (Continue)
          succeeded();
    }








    // place holder for now

    public void Var003()
    {
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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);
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
    public void Var005()
    {
       boolean Continue = true;

          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);
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
    public void Var006()
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
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             String[] result = c.dir();
             if (FTPUtilities.checkTestDir(result, DETAILED, TOTAL,output_)) {}
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
             AS400FTP c = new AS400FTP(systemObject_);
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
                      output_.println("    " + result[i]);

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
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             String[] result = c.ls();
             if (FTPUtilities.checkTestDir(result, NAME_ONLY, TOTAL,output_)) {}
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
             AS400FTP c = new AS400FTP(systemObject_);
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
                      output_.println("    " + result[i]);

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
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectoryDeep);
             String[] result = c.ls();
             if (FTPUtilities.checkForFile(result, "FSTOOL.EXE",output_)) {}
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
             AS400FTP c = new AS400FTP();
             c.setSaveFilePublicAuthority(null);
             failed("no exception when authority is null");
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
             AS400FTP c = new AS400FTP();
             c.setSaveFilePublicAuthority("");
             failed("no exception when authority is empty string");
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
             AS400FTP c = new AS400FTP();
             if (! c.getSaveFilePublicAuthority().equals("*EXCLUDE"))
             {
                failed("default authority value wrong, value is: " + c.getSaveFilePublicAuthority() );
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
             AS400FTP c = new AS400FTP();
             c.setSaveFilePublicAuthority("fred");
             if (! c.getSaveFilePublicAuthority().equals("fred"))
             {
                failed("set authority value wrong");
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
          succeeded();
    }



    // ---------------------------------------------------------------------
    //  A couple save file tests
    // ---------------------------------------------------------------------
    public void Var010()
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

	     String message = deleteLibrary(cc, "FTPTEST");
                if ((message == null) || message.startsWith("CPF2110"))
                {}
                else
		{
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
	     }

		message = deleteLibrary(cc, "FTPTEST2"); 
                if ((message == null) || message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest2 " + message);
                   Continue = false;
                }

             if (! cc.run("QSYS/CRTLIB FTPTEST2"))
             {
                message = getFirstMessage(cc);
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
                ftp.disconnect();  // Cleanup to allow later DLTLIB FTPTEST2 to succeed
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

	     String message = deleteLibrary(cc, "FTPTEST3");
                if ((message == null) || message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib3 ftptest " + message);
                   Continue = false;
                }
	     message = deleteLibrary(cc, "FTPTEST");
                if ((message == null) ||  message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }

             if (! cc.run("QSYS/CRTLIB FTPTEST3"))
             {
                message = getFirstMessage(cc);
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
	     String message = deleteLibrary(cc, "FTPTEST"); 
                if ((message == null) ||  message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }
		message = deleteLibrary(cc, "FTPTEST4"); 
                if ((message == null) || message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest4 " + message);
                   Continue = false;
                }

             if (! cc.run("QSYS/CRTLIB FTPTEST4"))
             {
                message = getFirstMessage(cc);
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
                ftp.disconnect();  // Cleanup
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

	     String message = deleteLibrary(cc, "FTPTEST"); 
                if ((message == null) ||  message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }

		message = deleteLibrary(cc, "FTPTEST5"); 

                if (message == null || message.startsWith("CPF2110"))
                {}
                
                else
                {
                   failed("setup failed, dltlib ftptest5 " + message);
                   Continue = false;
                }


             if (! cc.run("QSYS/CRTLIB FTPTEST5"))
             {
                message = getFirstMessage(cc);
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
             AS400FTP c = new AS400FTP();
             c.setSystem(null);
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
             AS400 system2 = new AS400();
             AS400FTP c = new AS400FTP();
             c.setSystem(systemObject_);
             c.setSystem(system2);
             if (c.getSystem() == system2) {}
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
             AS400FTP c = new AS400FTP();
             AS400 system2 = new AS400("badd", "baduid", "BADPWD".toCharArray());
             c.setSystem(system2);
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
             AS400 system2 = new AS400();
             AS400FTP c = new AS400FTP(systemObject_);
             c.noop();
             c.setSystem(system2);
             failed("no exception when setting server after connection established");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "ExtendedIllegalStateException"))
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP(systemObject_);

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
    //  Make sure setUser(), setPassword() and setServer() throw exceptions
    // ---------------------------------------------------------------------
    public void Var013()
    {
       boolean Continue = true;

       AS400FTP ftp = new AS400FTP();

       try
       {
          ftp.setUser("fred");
       }
       catch (Exception e)
       {
          if (exceptionIs(e, "IllegalStateException"))
          {}
          else
          {
             failed("wrong exception -- setUser()");
             Continue = false;
          }
       }

       try
       {
          ftp.setPassword("fred");
       }
       catch (Exception e)
       {
          if (exceptionIs(e, "IllegalStateException"))
          {}
          else
          {
             failed("wrong exception -- setPassword()");
             Continue = false;
          }
       }

       try
       {
          ftp.setServer("fred");
       }
       catch (Exception e)
       {
          if (exceptionIs(e, "IllegalStateException"))
          {}
          else
          {
             failed("wrong exception -- setServer()");
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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP(systemObject_);

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
             AS400FTP c = new AS400FTP();
             c.noop();
             failed("No exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "ExtendedIllegalStateException"))
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP();
             c.connect();
             failed("No exception");
             Continue = false;
          }
          catch (Exception e)
          {
             if (exceptionIs(e, "ExtendedIllegalStateException"))
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP(systemObject_);
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("1 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("2 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("3 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("4 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("5 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("6 ");
          try
          {
             String source   = targetDirFull + "PureJava.html";
             String original = testDirectory + File.separator + "PureJava.html";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(AS400FTP.BINARY);

             if (c.get("PureJava.html", source))
             {
                if (FTPUtilities.compareFile(source, original,output_))
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
          output_.print("7 ");
          try
          {
             String source   = targetDirFull + "jt400.jar";
             String original = testDirectory + File.separator + "jt400.jar";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);
             beansCleanup();
             c.addFTPListener(this);
             if (c.get("jt400.jar", source))
             {
                c.removeFTPListener(this);
                if (FTPUtilities.compareFile(source, original,output_))
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
          output_.print("8 ");
             String source   = targetDirFull + "javasp.savf";
             String original = testDirectory + File.separator + "javasp.savf";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("javasp.savf", source))
             {
                if (FTPUtilities.compareFile(source, original,output_))
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
          output_.print("9 ");
             String source   = targetDirFull + "a.a";
             String original = testDirectory + File.separator + "a.a";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.ASCII);

             if (c.get("a.a", source))
             {
                if (FTPUtilities.compareFile(source, original,output_))
                {}
                else
                {
                   output_.println();
                   output_.println(" warning, ascii compare failed (9) ");
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
          output_.print("10 ");
          try
          {
             String source   = targetDirFull + "FSTOOL.EXE";
             String original = testDirectory + File.separator +
                               "rootDir"     + File.separator +
                               "subdir2"     + File.separator + "FSTOOL.EXE";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("rootDir/subdir2/FSTOOL.EXE", source))
             {
                if (FTPUtilities.compareFile(source, original,output_))
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
          output_.print("11 ");
          try
          {
             String source   = targetDir;
             // String original = testDirectory + File.separator +
             //                  "rootDir"     + File.separator +
             //                   "subdir2"     + File.separator + "FSTOOL.EXE";

             AS400FTP c = new AS400FTP(systemObject_);
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
          Continue = Var019a();

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
             failed("setup failed, could not create targetDirTest19a on workstation");
             Continue = false;
          }
       }
       catch (Exception e)
       {
          failed(e, "setup failed, could not create targetDirTest19a on workstation");
          Continue = false;
       }


       if (Continue)
       {
          output_.print("12 ");
          try
          {
             java.io.File fred = new java.io.File("fred");
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("13 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("14 ");
          try
          {
             java.io.File fred = new java.io.File("fred");
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("15 ");
          try
          {
             java.io.File fred = new java.io.File(targetDirFull + "fred");
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("16 ");
          try
          {
             String source   = targetDirFull + "PureJava.html";
             String original = testDirectory + File.separator + "PureJava.html";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("PureJava.html", new java.io.File(source)))
             {
                if (FTPUtilities.compareFile(source, original,output_))
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
          output_.print("17 ");
          try
          {
             String source   = targetDirFull + "jt400.jar";
             String original = testDirectory + File.separator + "jt400.jar";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);
             beansCleanup();
             c.addFTPListener(this);
             if (c.get("jt400.jar", new java.io.File(source)))
             {
                c.removeFTPListener(this);
                if (FTPUtilities.compareFile(source, original,output_))
                {
                   if ((ftpEvent != null) && (ftpEvent.getID() == FTPEvent.FTP_RETRIEVED))
                   {}
                   else
                   {
                      failed("event not right (7a)");
                      output_.println(ftpEvent.getID());
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
          output_.print("18 ");
          try
          {
             String source   = targetDirFull + "javasp.savf";
             String original = testDirectory + File.separator + "javasp.savf";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             if (c.get("javasp.savf", new java.io.File(source)))
             {
                if (FTPUtilities.compareFile(source, original,output_))
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
          output_.println("19 ");
          try
          {
             String source   = targetDirFull + "a.a";
             String original = testDirectory + File.separator + "a.a";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.ASCII);

             if (c.get("a.a", new java.io.File(source)))
             {
                if (FTPUtilities.compareFile(source, original,output_))
                {}
                else
                {
                   output_.println();
                   output_.println("   warning, ascii compare failed (9a) ");
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

       try
       {
          File f = new File(targetDir);
          if (! f.mkdir())
          {
             failed("setup failed, could not create targetDirTest20 on workstation");
             Continue = false;
          }
       }
       catch (Exception e)
       {
          failed(e, "setup failed, could not create targetDirTest20 on workstation");
          Continue = false;
       }


       if (Continue)
       {
          output_.print("1 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             InputStream i = c.get(null);
             failed("no exception (1)"+i);
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
          output_.print("2 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             InputStream i = c.get("");
             failed("no exception (3)"+i);
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
          output_.print("3 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("4 ");
          try
          {
             String target   = targetDirFull + "PureJava.html";
             String original = testDirectory + File.separator + "PureJava.html";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("PureJava.html");

             if (i != null)
             {
                FTPUtilities.copy(i, target);

                if (FTPUtilities.compareFile(target, original,output_))
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
          output_.print("5 ");
          try
          {
             String target   = targetDirFull + "jt400.jar";
             String original = testDirectory + File.separator + "jt400.jar";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("jt400.jar");
             if (i != null)
             {
                FTPUtilities.copy(i, target);
                if (FTPUtilities.compareFile(target, original,output_))
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
          output_.print("6 ");
          try
          {
             String target   = targetDirFull + "javasp.savf";
             String original = testDirectory + File.separator + "javasp.savf";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("javasp.savf");
             if (i != null)
             {
                FTPUtilities.copy(i, target);
                if (FTPUtilities.compareFile(target, original,output_))
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
          output_.println("7 ");
          try
          {
             String target   = targetDirFull + "a.a";
             String original = testDirectory + File.separator + "a.a";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.ASCII);
             beansCleanup();
             c.addFTPListener(this);
             InputStream i = c.get("a.a");
             if (i != null)
             {
                c.removeFTPListener(this);
                FTPUtilities.copy(i, target);
                if (FTPUtilities.compareFile(target, original,output_))
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
                   output_.println();
                   output_.println("   warning, ascii compare failed (9) ");
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
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various put(String, String) tests
    // ---------------------------------------------------------------------
    public void Var021()
    {
       boolean Continue = true;

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest21";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          output_.print("1 ");
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
          output_.print("2 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("3 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("4 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("5 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("6 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("7 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("8 ");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             beansCleanup();
             c.addFTPListener(this);
             if (c.put(original, target))
             {
                FTPEvent localFTPEvent = ftpEvent;
                if (c.get(target, compare))
                {
                   c.removeFTPListener(this);
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("9 ");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("10 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("11 ");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.put(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
                   {}
                   else
                   {
                      output_.println();
                      output_.println("   warning, ascii compare failed (9) ");
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
          output_.print("12 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory;
             // String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
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
          Continue = Var021a();


       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various put(File, String) tests
    // ---------------------------------------------------------------------
    boolean Var021a()
    {
       boolean Continue = true;

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest21a";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          output_.print("13 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest21a on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest21a on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("14 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("setup failed, could not create targetDirTest21a on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest21a on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("15 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("16 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("17 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("18 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("19 ");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             beansCleanup();
             c.addFTPListener(this);
             if (c.put(new java.io.File(original), target))
             {
                FTPEvent localFTPEvent = ftpEvent;
                if (c.get(target, compare))
                {
                   c.removeFTPListener(this);
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("20 ");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(new java.io.File(original), target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("21 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.put(new java.io.File(original), target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.println("22 ");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.put(new java.io.File(original), target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
                   {}
                   else
                   {
                      output_.println();
                      output_.println("   warning, ascii compare failed (9a) ");
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

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest22";
       String  compareDirFull = compareDir + File.separator;
       // String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          output_.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest22 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest22 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("2 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("3 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             OutputStream o = c.put(null);
             failed("no exception (1)"+o);
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
          output_.print("4 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             OutputStream o = c.put("");
             failed("no exception (4)"+o);
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
          output_.print("5 ");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.put(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("6 ");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.put(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("7 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.put(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.println("8 ");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
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

                if (c.get(target, compare))
                {
                   c.removeFTPListener(this);
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                      output_.println();
                      output_.println("   warning, ascii compare failed (9) ");
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP();
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
             AS400FTP c = new AS400FTP();
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
    //  100% Pure Java testcase
    // ---------------------------------------------------------------------
    public void Var024()
    {
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             if (FTPTest.DEBUG) output_.println("Testing cd()");

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
       {
          try
          {
             if (FTPTest.DEBUG) output_.println("Testing ls()");
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             String[] result = c.ls();
             if (FTPUtilities.checkTestDir(result, NAME_ONLY, TOTAL, output_)) {}
             else
             {
                failed("contents of directory incorrect");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception");
             Continue = false;
          }
       }

       String targetDir = "targetDirTest24";
       String targetDirFull = targetDir + File.separator;

       if (Continue)
       {
          if (FTPTest.DEBUG) output_.println("Setting up for get() and put()");
          try
          {
             File f = new File(targetDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest24 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest24 on workstation");
             Continue = false;
          }
       }


       if (Continue)
       {
          if (FTPTest.DEBUG) output_.println("Testing get()");
          try
          {
             String target   = targetDirFull + "ftptest.savf";
             // String original = testDirectory + File.separator + "ftptest.savf";

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);

             InputStream i = c.get("ftptest.savf");
             if (i != null)
             {
                FTPUtilities.copy(i, target);
             }
             else
             {
                failed("failed -- get returned false ");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception in get()");
             Continue = false;
          }
       }


       if (Continue)
       {
          if (FTPTest.DEBUG) output_.println("Testing put() and save file processing");

          if (pwrSys_ == null)
          {
             failed("no power system AS400");
             Continue = false;
          }
       }

       if (Continue)
       {
          try
          {
             CommandCall cc = new CommandCall(pwrSys_);

	     String message = deleteLibrary(cc, "FTPTEST");
	     if (message == null || message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest " + message);
                   Continue = false;
                }


             message = deleteLibrary(cc, "FTPTEST2");
                if (message == null || message.startsWith("CPF2110"))
                {}
                else
                {
                   failed("setup failed, dltlib ftptest2 " + message);
                   Continue = false;
                }


             if (! cc.run("QSYS/CRTLIB FTPTEST2"))
             {
                message = getFirstMessage(cc);
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
                ftp.put(targetDirFull + "ftptest.savf", "FTPTEST.SAVF");
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


       if (Continue)
          succeeded();
    }



    // ---------------------------------------------------------------------
    //  Various append(String, String) tests
    // ---------------------------------------------------------------------
    public void Var025()
    {
      Class<?>[] parmTypes = { String.class, String.class };
      if (!FTPTestcase.checkMethodExists("append", parmTypes)) {
        notApplicable("No method named append(String,String)");
        return;
      }

       boolean Continue = true;

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest25";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          output_.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest25 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest25 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("2 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("setup failed, could not create targetDirTest25 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest25 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("3 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("4 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("5 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("6 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("7 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("8 ");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("9 ");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("10 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("11 ");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.append(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
                   {}
                   else
                   {
                      output_.println();
                      output_.println("   warning, ascii compare failed (9) ");
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
          output_.print("12 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory;
             // String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
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
          output_.print("13 ");
          try
          {
             String target   = "jt400Append.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
          output_.print("14 ");
          try
          {
             String target   = "PureJavaAppend.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             if (c.append(original, target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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

       output_.println();

       if (Continue)
          succeeded();


    }




    // ---------------------------------------------------------------------
    //  Various append(File, String) tests
    // ---------------------------------------------------------------------
    public void Var026()
    {
      Class<?>[] parmTypes = { java.io.File.class, String.class };
      if (!FTPTestcase.checkMethodExists("append", parmTypes)) {
        notApplicable("No method named append(File,String)");
        return;
      }

       boolean Continue = true;

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest26";
       String  compareDirFull = compareDir + File.separator;
       String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          output_.print("13 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest26 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest26 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("14 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("setup failed, could not create targetDirTest26 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest26 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("15 ");
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
          output_.print("16 ");
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
          output_.print("17 ");
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
          output_.print("18 ");
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
          output_.print("19 ");
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
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (6a)");
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
          output_.print("20 ");
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
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (7a)");
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
          output_.print("21 ");
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
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (8a)");
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
          output_.print("22 ");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             if (c.append(new java.io.File(original), target))
             {
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
                   {}
                   else
                   {
                      output_.println();
                      output_.println("   warning, ascii compare failed (9a) ");
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
                failed("append returned false (9a)");
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
          output_.print("23 ");
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
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (6a)");
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
          output_.print("24 ");
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
                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (8a)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8a)");
             Continue = false;
          }
       }

       output_.println();

       if (Continue)
          succeeded();


    }





    // ---------------------------------------------------------------------
    //  Various outputStream put(String) tests
    // ---------------------------------------------------------------------
    public void Var027()
    {
      Class<?>[] parmTypes = { String.class };
      if (!FTPTestcase.checkMethodExists("append", parmTypes)) {
        notApplicable("No method named append(File,String)");
        return;
      }

       boolean Continue = true;

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest27";
       String  compareDirFull = compareDir + File.separator;
       // String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
          output_.print("1 ");
          try
          {
             File f = new File(compareDir);
             if (! f.mkdir())
             {
                failed("setup failed, could not create targetDirTest27 on workstation");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest27 on workstation");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("2 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             String message = c.issueCommand("MKD " + compareDir);

             // Success could be 250 (Requested file action okay)
             // or 257 (PathName created)
             if (! message.startsWith("25"))
             {
                failed("setup failed, could not create targetDirTest27 on target via FTP");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "setup failed, could not create targetDirTest27 on server via FTP");
             Continue = false;
          }
       }

       if (Continue)
       {
          output_.print("3 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             OutputStream o = c.append(null);
             failed("no exception (1)"+o);
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
          output_.print("4 ");
          try
          {
             AS400FTP c = new AS400FTP(systemObject_);
             OutputStream o = c.append("");
             failed("no exception (4)"+o);
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
          output_.print("5 ");
          try
          {
             String target   = "jt400.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (6)");
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
          output_.print("6 ");
          try
          {
             String target   = "javasp.savf";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (7)");
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
          output_.print("7 ");
          try
          {
             String target   = "PureJava.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (8)");
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
          output_.print("8 ");
          try
          {
             String target   = "a.a";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.ASCII);
             c.addFTPListener(this);
             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
                   {
                   }
                   else
                   {
                      output_.println();
                      output_.println("   warning, ascii compare failed (9) ");
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
                failed("append returned false (9)");
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
          output_.print("9 ");
          try
          {
             String target   = "jt400Append.jar";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (6)");
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
          output_.print("10 ");
          try
          {
             String target   = "PureJavaAppend.html";
             String original = testDirectory  + File.separator + target;
             String compare  = compareDirFull + target;

             AS400FTP c = new AS400FTP(systemObject_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);

             OutputStream o = c.append(target);
             if (o != null)
             {
                FTPUtilities.copy(original, o);

                if (c.get(target, compare))
                {
                   if (FTPUtilities.compareFile(compare, original,output_))
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
                failed("append returned false (8)");
                Continue = false;
             }
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception (8)");
             Continue = false;
          }
       }



       output_.println();

       if (Continue)
          succeeded();


    }









}



