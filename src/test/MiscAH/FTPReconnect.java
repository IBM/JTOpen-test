///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FTPReconnect.java
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
import test.TestDriverStatic;
import test.Testcase;

import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.lang.Integer;
import java.lang.String;


public class FTPReconnect extends    Testcase
                               implements FTPListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "FTPReconnect";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FTPTest.main(newArgs); 
   }
    private String user_     = null;
    private String clearPasswordString_ = null;
    private String system_   = null;
    private String testDirectory = "FTPTestDir";
    private String initialToken_ = null;
    boolean notWorthTrying = false;

    //private static final int DETAILED = 1;
    //private static final int NAME_ONLY = 0;

    //private static final int ONE     =  1;
    //private static final int TWO     =  2;
    //private static final int FOUR    =  4;
    //private static final int EIGHT   =  8;
    //private static final int SIXTEEN = 16;
    //private static final int TOTAL   = ONE + TWO + FOUR + EIGHT + SIXTEEN;

    private long timeout = 1000 * 60 * 8;
    private long oldTimeout;
    AS400 pwrSys_ = null;

    FTPEvent ftpEvent = null;
    private char[] clearPassword_;


    public FTPReconnect (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String userid,
                              String initialToken,
			      AS400  powerSystem)
    {
        super (systemObject,
               "FTPReconnect",
               namesAndVars,
               runMode,
               fileOutputStream,
               password);


	pwrSys_       = powerSystem;

        initialToken_ = initialToken;

        if (initialToken_ == null)
        {
           System.out.println("-directory is invalid, no test will be run");
           notWorthTrying = true;
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

        if ((system_ == null) || (system_.length() < 1))
           System.out.println("===> warning, variations will fail because no -system specified");

        if (FTPTest.DEBUG) System.out.println();

    }


    /**
     @exception  Exception  If an exception occurs.
     **/
    public void setup()
      throws Exception
    {
     lockSystem("FTP",600); 
      cleanUpDirs();
      echoTrace();

      // Create a privileged connection 
      // Get the old timeout
      SequentialFile file = new SequentialFile(pwrSys_, "/QSYS.LIB/QUSRSYS.LIB/QATMFTP.FILE");
      RecordFormat format = new RecordFormat();
      format.addFieldDescription(new CharacterFieldDescription(new AS400Text(100, 37, pwrSys_), "charfield"));
      
      file.setRecordFormat("QTMFTPR"); 
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // The third record has the timeout value 
      Record record = file.read(3);
      String value = ((String) record.getField(0)).trim(); 
      System.out.println("Current FTP timeout is "+value+" seconds"); 
      file.close(); 
      oldTimeout = Integer.parseInt(value);  

      if (oldTimeout > 0) {
        // Drop the timeout down to 10 seconds .. will only apply to newest
        // connections
	  CommandCall cc = new CommandCall(pwrSys_);
	  String command = "CHGFTPA INACTTIMO(10)";
	  if (!cc.run(command)) {
	      System.out.println("Warning:  '" + command + "' failed");
	  } else {
	      System.out.println("Changed FTP timeout using "+command); 
	  }
	  timeout = 1000 * 30;
      }

    }


    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
      ///if (cleanup)
        cleanUpDirs();

       if (oldTimeout > 0) {
	   // Make sure the timeout is reasonable 
	   if (oldTimeout < 200) oldTimeout = 200; 
	// Restore the old timeout
	// CHGFTPA INACTTIMO(3600)
	   CommandCall cc = new CommandCall(pwrSys_);
	String command = "CHGFTPA INACTTIMO("+oldTimeout+")"; 
	if (!cc.run(command)) {
	    System.out.println("Warning:  '"+command+"' failed"); 
	} else {
	    System.out.println("Successfully change timeout using "+command); 
	} 
       }

       unlockSystem(); 
    }


    void cleanUpDirs()
    {
       if (FTPTest.DEBUG) System.out.println();
       System.gc();

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


    void echoTrace()
    {
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
    //  Various setTransferType tests
    // ---------------------------------------------------------------------
    public void Var001()
    {
       if (TestDriverStatic.brief_)
       {
         succeeded("Skipping long-running variation.");
         return;
       }
       boolean Continue = true;

       if (Continue)
       {
          try
          {
	      // try 4 times to fail
	      for (int i = 0; i < 4; i++) { 
		  FTP c = new FTP(system_, user_, clearPasswordString_);
		  c.setDataTransferType(FTP.ASCII);

		  System.out.println("==> Will now wait " + timeout + " milliseconds for a timeout");
		  Thread.sleep(timeout);

		  c.setDataTransferType(FTP.ASCII);
	     // Comments from CHGFTPA command help text:
	     // The Change FTP attributes (CHGFTPA) command changes the             
	     // configuration for the File Transfer Protocol (FTP) servers.  The FTP
	     // attributes can be changed while the FTP servers are active.  The    
	     // attributes that were changed take affect the next time an FTP server
	     // connection is made.  All existing connections are not changed but   
	     // keep the same attributes they had when the connection was           
	     // established.                                                        

	      }
	      Continue = false;
	     failed("no exception, is FTP timeout longer than "+(timeout / 1000)+" seconds (CHGFTPA INACTTIMO()???");

          }
          catch (Exception e)
          {
             if ((exceptionIs(e, "IOException")) ||               //@A1C
                 (exceptionIs(e, "SocketException")))             //@A1C
             {}
             else
             {
                failed(e, "Unexpected exception var001 ");
                Continue = false;
             }
          }
       }

       if (Continue)
          succeeded();
    }




    // ---------------------------------------------------------------------
    //  Various disconnect tests
    // ---------------------------------------------------------------------
    public void Var002()
    {
       if (TestDriverStatic.brief_)
       {
         succeeded("Skipping long-running variation.");
         return;
       }
       boolean Continue = true;

       if (Continue)
       {
          try
          {
             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.setDataTransferType(FTP.ASCII);

             System.out.println("==> Will now wait " + timeout + " milliseconds for a timeout");
             Thread.sleep(timeout);

             c.disconnect();
          }
          catch (Exception e)
          {
             failed(e, "Unexpected exception ");
             Continue = false;
          }
       }

       if (Continue)
          succeeded();
    }


    // ---------------------------------------------------------------------
    //  Various ls() tests
    // ---------------------------------------------------------------------
    public void Var003()
    {
       if (TestDriverStatic.brief_)
       {
         succeeded("Skipping long-running variation.");
         return;
       }
       boolean Continue = true;

       if (Continue)
       {
          try
          {
            String[] result = null; 
	      for (int i = 0 ; i < 4; i++) { 
		  FTP c = new FTP(system_, user_, clearPasswordString_);
		  c.cd(initialToken_);
		  c.cd(testDirectory);

		  System.out.println("==> Will now wait " + timeout + " milliseconds for a timeout");
		  Thread.sleep(timeout);

		  result = c.dir();
	      }

             failed("no exception"+result);
             Continue = false;
          }
          catch (Exception e)
          {
             if ((exceptionIs(e, "IOException")) ||               //@A1C
                 (exceptionIs(e, "SocketException")))             //@A1C
             {}
             else
             {
                failed(e, "Unexpected exception (1)");
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
    public void Var004()
    {
       if (TestDriverStatic.brief_)
       {
         succeeded("Skipping long-running variation.");
         return;
       }
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

       String source   = targetDirFull + "PureJava.html";
       String original = "FTPTest" + File.separator + "PureJava.html";
       FTP c = new FTP(system_, user_, clearPasswordString_);

       if (Continue)
       {
          try
          {
             c.cd(initialToken_);
             c.cd(testDirectory);
             c.setDataTransferType(FTP.BINARY);
          }
          catch (Exception e)
          {
             failed("setup failed original="+original);
             Continue = false;
          }

          if (Continue)
          {
             try
             {
		 for (int i = 0; i < 4; i++) { 
		     System.out.println("==> Will now wait " + timeout + " milliseconds for a timeout");
		     Thread.sleep(timeout);

		     c.get("PureJava.html", source);
		 }
                failed("no exception");
                Continue = false;
             }
             catch (Exception e)
             {
               if ((exceptionIs(e, "IOException")) ||               //@A1C
                   (exceptionIs(e, "SocketException")))             //@A1C
                {}
                else
                {
                   failed(e, "Unexpected exception (6)");
                   Continue = false;
                }
             }
          }
       }

       if (Continue)
          succeeded();

    }




    // ---------------------------------------------------------------------
    //  Various outputStream put(String) tests
    // ---------------------------------------------------------------------
    public void Var005()
    {
       if (TestDriverStatic.brief_)
       {
         succeeded("Skipping long-running variation.");
         return;
       }
       boolean Continue = true;

       // String  sourceDir      = "FTPTestDir";
       String  compareDir     = "targetDirTest22";
       // String  compareDirFull = compareDir + File.separator;
       // String  targetDirFull  = compareDir + File.separator;

       if (Continue)
       {
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
          try
          {
             String target   = "jt400.jar";
             // String original = testDirectory  + File.separator + target;
             // String compare  = compareDirFull + target;

             FTP c = new FTP(system_, user_, clearPasswordString_);
             c.cd(initialToken_);
             c.cd(compareDir);
             c.setDataTransferType(FTP.BINARY);
             OutputStream o = null; 
	     for (int i = 0;  i < 4; i++) { 
		 System.out.println("==> Will now wait " + timeout + " milliseconds for a timeout");
		 Thread.sleep(timeout);

		 o = c.put(target);
	     }

             failed ("no exception"+o);
          }
          catch (Exception e)
          {
            if ((exceptionIs(e, "IOException")) ||               //@A1C
                (exceptionIs(e, "SocketException")))             //@A1C
             {}
             else
             {
                failed(e, "Unexpected exception (6)");
                Continue = false;
             }
          }
       }

       if (Continue)
          succeeded();


    }

















///    //
///    // Varxious CD() test cases
///    //
///    public void Varx007()
///    {
///       boolean Continue = true;
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.cd(null);
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "NullPointerException")) {}
///             else
///             {
///                failed(e, "Unexpected exception (1)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.cd("");
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalArgumentException")) {}
///             else
///             {
///                failed(e, "Unexpected exception (2)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             if (c.cd("/notThere"))
///             {
///                failed("cd to notThere worked and shouldn't have");
///                Continue = false;
///             }
///             else
///             {
///                if (! c.getLastMessage().startsWith("550"))
///                {
///                   failed("wrong message returned " + c.getLastMessage());
///                   Continue = false;
///                }
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (3)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.cd(initialToken_);
///
///             if (c.cd(testDirectory))
///             {
///             }
///             else
///             {
///                {
///                   failed("could not cd (1) " + c.getLastMessage());
///                   Continue = false;
///                }
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (4)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///          succeeded();
///    }
///
///
///    // ---------------------------------------------------------------------
///    //  Various setCurrentDirectory() tests
///    // ---------------------------------------------------------------------
///    public void Varx012()
///    {
///       boolean Continue = true;
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.setCurrentDirectory(null);
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "NullPointerException")) {}
///             else
///             {
///                failed(e, "Unexpected exception (1)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.setCurrentDirectory("");
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalArgumentException")) {}
///             else
///             {
///                failed(e, "Unexpected exception (2)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             if (c.setCurrentDirectory("/notThere"))
///             {
///                failed("cd to notThere worked and shouldn't have");
///                Continue = false;
///             }
///             else
///             {
///                if (! c.getLastMessage().startsWith("550"))
///                {
///                   failed("wrong message returned " + c.getLastMessage());
///                   Continue = false;
///                }
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (3)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///
///             if (c.setCurrentDirectory(testDirectory))
///             {
///             }
///             else
///             {
///                {
///                   failed("could not cd (1) " + c.getLastMessage());
///                   Continue = false;
///                }
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (4)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///          succeeded();
///    }
///
///
///
///
///
///
///    // ---------------------------------------------------------------------
///    //  Various getCurrentDirectory() tests
///    // ---------------------------------------------------------------------
///    public void Varx014()
///    {
///       boolean Continue = true;
///
///       if (Continue)
///       {
///          String it2;
///
///          if (initialToken_.startsWith("~"))
///             it2 = initialToken_.substring(1);
///          else
///             it2 = initialToken_;
///
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///             String cd = c.getCurrentDirectory();
///
///             if (cd.indexOf(it2) >= 0)
///             {}
///             else
///             {
///                failed("getCD not same as setCD (1): " + cd);
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (1)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///             c.setCurrentDirectory(testDirectory);
///             String cd = c.getCurrentDirectory();
///
///             if (cd.indexOf(testDirectory) >= 0)
///             {}
///             else
///             {
///                failed("getCD not same as setCD (2): " + cd);
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (2)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///             c.setCurrentDirectory(testDirectory);
///             c.setCurrentDirectory("notThere");
///             String cd = c.getCurrentDirectory();
///
///             if (cd.indexOf(testDirectory) >= 0)
///             {}
///             else
///             {
///                failed("getCD not same as setCD (3): " + cd);
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (3)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///          succeeded();
///    }
///
///
///
///
///
///
///    // ---------------------------------------------------------------------
///    //  Various pwd() tests
///    // ---------------------------------------------------------------------
///    public void Varx015()
///    {
///       boolean Continue = true;
///
///       if (Continue)
///       {
///          String it2;
///
///          if (initialToken_.startsWith("~"))
///             it2 = initialToken_.substring(1);
///          else
///             it2 = initialToken_;
///
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///             String cd = c.pwd();
///
///             if (cd.indexOf(it2) >= 0)
///             {}
///             else
///             {
///                failed("getCD not same as setCD (1): " + cd);
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (1)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///             c.setCurrentDirectory(testDirectory);
///             String cd = c.pwd();
///
///             if (cd.indexOf(testDirectory) >= 0)
///             {}
///             else
///             {
///                failed("getCD not same as setCD (2): " + cd);
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (2)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///
///             c.setCurrentDirectory(initialToken_);
///             c.setCurrentDirectory(testDirectory);
///             c.setCurrentDirectory("notThere");
///             String cd = c.pwd();
///
///             if (cd.indexOf(testDirectory) >= 0)
///             {}
///             else
///             {
///                failed("getCD not same as setCD (3): " + cd);
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (3)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///          succeeded();
///    }
///
///
///
///
///
///    // ---------------------------------------------------------------------
///    //  Various noop() tests
///    // ---------------------------------------------------------------------
///    public void Varx016()
///    {
///       boolean Continue = true;
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP();
///             c.noop();
///             failed("No exception");
///             Continue = false;
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalStateException"))
///             {}
///             else
///             {
///                failed(e, "Unexpected exception (1)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.noop();
///             if (c.getLastMessage().startsWith("200"))
///             {}
///             else
///             {
///                failed("Unexpected message (2): " + c.getLastMessage());
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (2)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///          succeeded();
///    }
///
///
///
///
///    // ---------------------------------------------------------------------
///    //  Various connect/disconnect() tests
///    // ---------------------------------------------------------------------
///    public void Varx017()
///    {
///       boolean Continue = true;
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP();
///             c.connect();
///             failed("No exception");
///             Continue = false;
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalStateException"))
///             {}
///             else
///             {
///                failed(e, "Unexpected exception (1)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP();
///             c.setUser("BillGates");
///             c.setPassword("$$$");
///             c.connect();
///             failed("No exception");
///             Continue = false;
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalStateException"))
///             {}
///             else
///             {
///                failed(e, "Unexpected exception (2)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP();
///             c.setServer("mySys");
///             c.setUser("BillGates");
///             c.connect();
///             failed("No exception");
///             Continue = false;
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalStateException"))
///             {}
///             else
///             {
///                failed(e, "Unexpected exception (3)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP();
///             c.setServer("mySys");
///             c.setPassword("********");
///             c.connect();
///             failed("No exception");
///             Continue = false;
///          }
///          catch (Exception e)
///          {
///             if (exceptionIs(e, "IllegalStateException"))
///             {}
///             else
///             {
///                failed(e, "Unexpected exception (4)");
///                Continue = false;
///             }
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.disconnect();
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (5)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.connect();
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (5a)");
///             Continue = false;
///          }
///       }
///
///       if (Continue)
///       {
///          try
///          {
///             FTP c = new FTP(system_, user_, password_);
///             c.connect();
///             c.disconnect();
///             if (c.connect())
///             {}
///             else
///             {
///                failed("connect after disconnect failed");
///                Continue = false;
///             }
///          }
///          catch (Exception e)
///          {
///             failed(e, "Unexpected exception (6)");
///             Continue = false;
///          }
///       }
///
///
///       if (Continue)
///          succeeded();
///    }



}



