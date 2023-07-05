///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400CertificateUsrPrfTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import java.io.*;
import java.util.Vector;
// Not available in JDK 7 
// import sun.security.x509.*;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

import com.ibm.as400.access.AS400Certificate;
import com.ibm.as400.access.AS400CertificateAttribute;
import com.ibm.as400.access.AS400CertificateUserProfileUtil;


import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import java.io.IOException;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.UserSpace;

/**
Test methods for AS400CertificateUserProfileUtil.
**/
public class AS400CertificateUsrPrfTestcase extends Testcase
{
  private AS400 certSystem_;
  private static String userProfilePathName_ = "/QSYS.LIB/CERTTEST.USRPRF";
  private static String userProfileName_ = "CERTTEST";
  private static String userSpacePathName_ = "/QSYS.LIB/CERTTEST.LIB/US.USRSPC";
  private String pre_existingUserSpace_ = "/QSYS.LIB/CERTTEST.LIB/PREEXIST.USRSPC";
  private String non_existingUserSpace_ = "/QSYS.LIB/CERTTEST.LIB/XKG52V9QWQ.USRSPC";
  private byte pre_existingByteValue_ = (byte)0x00;
  private int pre_existingLengthValue_ = 11000;
  private String mappedDrive_;
  private String operatingSystem_;
  private boolean DOS_ = false;
  private boolean usingNativeImpl = false;
  boolean failed;  // Keeps track of failure in multi-part tests.
  String msg;      // Keeps track of reason for failure in multi-part tests.
  byte[][] testcert_ = null; // Array of X.509 test certificates

/**
Constructor.
**/
  public AS400CertificateUsrPrfTestcase(AS400  systemObject,
                                Vector           variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream,
                                String           driveLetter)
  {
    super(systemObject, "AS400CertificateUsrPrfTestcase", 47, variationsToRun, runMode,
          fileOutputStream);


    mappedDrive_ = driveLetter;
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  public void setup()
    throws Exception
  {

    if (mappedDrive_ == null)
    {
      output_.println("No mapped drive was specified on command line.");
      throw new Exception("No mapped drive was specified");
    }

    // Determine operating system we're running under
    operatingSystem_ = System.getProperty("os.name");
    if (operatingSystem_.indexOf("Windows") >= 0 ||
        operatingSystem_.indexOf("DOS") >= 0 ||
        operatingSystem_.indexOf("OS/2") >= 0)
    {
      DOS_ = true;
    }


    // Are we running on the AS/400?
    else if (operatingSystem_.indexOf("OS/400") >= 0)
    {
      usingNativeImpl = true;
      output_.println("Will use native implementation");
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("DOS-based file structure: " + DOS_);
    output_.println("Executing " + (isApplet_ ? "applet." : "application."));

    testInit();
  }


  void deleteUsrPrf(String aUserPrf)
  {
      CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

      String clcmd;

      clcmd = "DLTUSRPRF USRPRF(" + aUserPrf + ") OWNOBJOPT(*DLT)";

    try
    {
     if(cmd.run(clcmd) == false)
     {
            AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
        }

    }
    catch(Exception e)
    {
        failed(e);
    }
  }


  //auth to generated usrprf is also granted to currently running usrprf
  void genUsrPrf(String aUserPrf)
  {
     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     String clcmd;

    try
    {
     clcmd = "CRTUSRPRF USRPRF(" + aUserPrf + ")  PASSWORD(JTEAM1) TEXT('dennis  schroepfer 3-3073')";

     if(cmd.run(clcmd) == false)
     {
            AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
     }

     clcmd = "GRTOBJAUT OBJ(" + aUserPrf + ") OBJTYPE(*USRPRF) USER(" +  systemObject_.getUserId()   + ") AUT(*ALL)";

     if(cmd.run(clcmd) == false)
     {
            AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
     }

    }
    catch(Exception e)
    {
        failed(e);
    }
  }


  //no auth to generated usrprf is granted to currently running usrprf
  void genUsrPrfNoAut(String aUserPrf)
  {
     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     String clcmd;

      clcmd = "CRTUSRPRF USRPRF(" + aUserPrf + ") PASSWORD(JTEAM1) TEXT('dennis  schroepfer 3-3073')";

    try
    {
     if(cmd.run(clcmd) == false)
     {
            AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
        }

    }
    catch(Exception e)
    {
        failed(e);
    }
  }



  void deleteUserSpace(UserSpace aUserSpace)
  {
    try
    {
      if (false /*isApplet_*/)
      {
         aUserSpace.delete();
      }
      else
      {
         aUserSpace.delete();
      }
    }
    catch(Exception e)
    {
        failed(e);
    }
  }



  //***********************************************************/
  // Compare 2 certificate byte arrays, return true if the first
  // "numcerts" certificates of the first parameter are found in the
  // second array. Duplicates are not allowed.
  // The following declares define the test certificate's fields.
  //***********************************************************/
  /*****
             //generic cert
    byte[] testcert0 =  gencert.genCert(
                         "subject",
                         "subjORGUNIT",
                         "subjORG",
                         "subjLOCALITY",
                         "subjSTATE",
                         "subjCOUNTRY",

                         new BigInt(100034567),
                         kp.getPublic(),
                         kp.getPrivate(),

                         "issuer",
                         "issORGUNIT",
                         "issORG",
                         "issLOCALITY",
                         "issSTATE",
                         "issCOUNTRY",

                         "DSA",
                         new Date(99, 11, 30),
                         new Date(96, 11, 30)
                         );


         // unique cert
    byte[] testcert1 =  gencert.genCert(
                         "subject1",
                         "subjORGUNIT1",
                         "subjORG1",
                         "subjLOCALITY1",
                         "subjSTATE1",
                         "subjCOUNTRY1",

                         new BigInt(11123456),
                         kp.getPublic(),
                         kp.getPrivate(),

                         "issuer1",
                         "issORGUNIT1",
                         "issORG1",
                         "issLOCALITY1",
                         "issSTATE1",
                         "issCOUNTRY1",

                         "DSA",
                         new Date(99, 11, 30),
                         new Date(96, 11, 30)
                         );

    byte[] testcert2 =  gencert.genCert(
                         "subject2",
                         "subjORGUNIT2",
                         "subjORG2",
                         "subjLOCALITY",
                         "subjSTATE",
                         "subjCOUNTRY",

                         new BigInt(22234567891),
                         null,
                         null,

                         "issuer",
                         "issORGUNIT",
                         "issORG",
                         "issLOCALITY",
                         "issSTATE",
                         "issCOUNTRY",

                         "DSA",
                         new Date(99, 11, 30),
                         new Date(96, 11, 30)
                         );

    byte[] testcert3 =  gencert.genCert(
                         "subject3",
                         "subjORGUNIT3",
                         "subjORG3",
                         "subjLOCALITY",
                         "subjSTATE",
                         "subjCOUNTRY",

                         new BigInt(3334567),
                         null,
                         null,

                         "issuer",
                         "issORGUNIT",
                         "issORG",
                         "issLOCALITY",
                         "issSTATE",
                         "issCOUNTRY",

                         "DSA",
                         new Date(99, 11, 30),
                         new Date(96, 11, 30)
                         );

    byte[] testcert4 =  gencert.genCert(
                         "subject4",
                         "subjORGUNIT4",
                         "subjORG4",
                         "subjLOCALITY4",
                         "subjSTATE",
                         "subjCOUNTRY",

                         new BigInt(44456789123),
                         null,
                         null,

                         "issuer4",
                         "issORGUNIT4",
                         "issORG4",
                         "issLOCALITY4",
                         "issSTATE4",
                         "issCOUNTRY4",

                         "DSA",
                         new Date(98, 11, 30),
                         new Date(96, 11, 30)
                         );

    byte[] testcert5 =  gencert.genCert(
                         "subject5",
                         "subjORGUNIT5",
                         "subjORG5",
                         "subjLOCALITY5",
                         "subjSTATE5",
                         "subjCOUNTRY",

                         new BigInt(555456789123),
                         null,
                         null,

                         "issuer",
                         "issORGUNIT5",
                         "issORG5",
                         "issLOCALITY5",
                         "issSTATE5",
                         "issCOUNTRY5",

                         "DSA",
                         new Date(98, 11, 30),
                         new Date(96, 11, 30)
                         );

         // unique but 1 and 6 have same subj pub key
    byte[] testcert6 =  gencert.genCert(
                         "subject6",
                         "subjORGUNIT6",
                         "subjORG6",
                         "subjLOCALITY6",
                         "subjSTATE6",
                         "subjCOUNTRY6",

                         new BigInt(666456789123),
                         kp.getPublic(),
                         kp.getPrivate(),

                         "issuer",
                         "issORGUNIT",
                         "issORG6",
                         "issLOCALITY6",
                         "issSTATE6",
                         "issCOUNTRY6",

                         "DSA",
                         new Date(98, 11, 30),
                         new Date(96, 11, 30)
                         );

         // no locality or state
    byte[] testcert7 =  gencert.genCert(
                         "subject7",
                         "subjORGUNIT7",
                         "subjORG7",
                         null,
                         null,
                         "subjCOUNTRY7",

                         new BigInt(777345123),
                         kp.getPublic(),
                         kp.getPrivate(),

                         "issuer",
                         "issORGUNIT",
                         "issORG",
                         "issLOCALITY7",
                         "issSTATE",
                         "issCOUNTRY",

                         "DSA",
                         new Date(98, 11, 30),
                         new Date(96, 11, 30)
                         );


         // no locality or state
    byte[] testcert8 =  gencert.genCert(
                         "subject",
                         "subjORGUNIT",
                         "subjORG8",
                         null,
                         null,
                         "subjCOUNTRY8",

                         new BigInt(888345123),
                         kp.getPublic(),
                         kp.getPrivate(),

                         "issuer",
                         "issORGUNIT",
                         "issORG",
                         "issLOCALITY",
                         "issSTATE",
                         "issCOUNTRY",

                         "DSA",
                         new Date(98, 11, 30),
                         new Date(96, 11, 30)
                         );



         // no locality or state
    byte[] testcert9 =  gencert.genCert(
                         "subject",
                         "subjORGUNIT",
                         "subjORG9",
                         null,
                         null,
                         "subjCOUNTRY9",

                         new BigInt(999345123),
                         kp.getPublic(),
                         kp.getPrivate(),

                         "issuer",
                         "issORGUNIT",
                         "issORG",
                         "issLOCALITY",
                         "issSTATE",
                         "issCOUNTRY",

                         "DSA",
                         new Date(98, 11, 30),
                         new Date(96, 11, 30)
                         );

    ************************************************************/
  boolean certsCompare(AS400Certificate[] as400certs,
                 byte[] [] testcert,
                 int numcerts)
  {
      if (as400certs.length > numcerts || testcert.length < numcerts)
       return false;

      byte [] xcert = null;
      boolean[] alreadyfound = new boolean[20];
      int i = 0, j = 0, k = 0;

      loop1:
      for (i = 0; i < as400certs.length; ++i)
      {
       //look for this cert in 2nd array
       xcert = as400certs[i].getEncoded();

       loop2:
       for (j = 0; j < numcerts; ++j)
       {
           if ( alreadyfound[j] ) continue;

           //compare cert lengths
           if (xcert.length != testcert[j].length)
            continue;

           //compare cert data
           loop3:
           for (k = 0; k < xcert.length; ++k)
           {
            if (xcert[k] != testcert[j][k])
                continue loop2;
           }

           //found one
           alreadyfound[j] = true;
           break;
       }
      }

      //did all of them match ?
      for (i = 0; i < as400certs.length; ++i)
      {
       if ( alreadyfound[j] == false )
           return false;
      }

      return true;
  }


  // Compare a as400certificate to a byte array, return true if equal
  boolean certCompare(AS400Certificate as400cert, byte[] testcert)
  {

      byte [] xcert = null;

      xcert = as400cert.getEncoded();
       //compare cert lengths
      if (xcert.length != testcert.length)
       return false;
       //compare cert data
      for (int j = 0; j < xcert.length; ++j)
      {
       if (xcert[j] != testcert[j])
           return false;
      }

      return true;
  }



  void testInit()
    throws Exception
  {
    try
    {
       // Create a user space to use test on an pre-existing user space.
       UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
       aUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "CRTDLT UserSpace", "*ALL");
       systemObject_.disconnectService(AS400.FILE);
    }
    catch(Exception e)
    {
       System.out.println("Setup failed, could not create certtest.lib/preexist.usrspc");
       System.out.println(e);
       throw e;
    }

    // Create a user profile and library/userspace with no authority to.
    CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

    try
    {

     if (cmd.run("CRTUSRPRF USRPRF(CERTAUTH) PASSWORD(JTEAM2) TEXT('dennis  schroepfer 3-3073')") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

       if (cmd.run("QSYS/CRTAUTL AUTL(CERTLISTUS) AUT(*EXCLUDE)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

       if (cmd.run("CRTLIB LIB(CERTAUTHUS) AUT(CERTLISTUS)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

      if (cmd.run("CHGUSRPRF USRPRF(" + systemObject_.getUserId() + ") SPCAUT(*SECADM)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }


    }
    catch (Exception e) {}

    try
    {
       UserSpace bUserSpace = new UserSpace(AS400CertificateTest.PwrSys, "/QSYS.LIB/CERTAUTHUS.LIB/USWRITE3.USRSPC");
       bUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "create UserSpace", "*ALL");
       AS400CertificateTest.PwrSys.disconnectService(AS400.FILE);
    }
    catch(Exception e)
    {
       System.out.println("Setup failed, could not create CERTAUTHUS.lib/uswrite3.usrspc");
       System.out.println(e);
       throw e;
    }

    try
    {
        // Create an AS400 object to be used with CERTTEST user profile.
       certSystem_ = new AS400(systemObject_.getSystemName(), "CERTTEST", "JTEAM1");


       deleteLibrary(cmd, "CERTLIBUS"); 

       if (cmd.run("CRTLIB LIB(CERTLIBUS)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
       }

       UserSpace aUSpace = new UserSpace(AS400CertificateTest.PwrSys, "/QSYS.LIB/CERTLIBUS.LIB/USWRITE2.USRSPC");
       aUSpace.create(11000, true, " ", (byte)0x00, "USWRITE test", "*ALL");
    }
    catch(Exception e)
    {
       System.out.println("Setup failed." + e);
       throw e;
    }

    //read in 13 test X.509 certificates
    //last 3 are just big honkers to cause buffer overflows
    //File file = null;
    InputStream fis = null;
    //String filename = null;

    testcert_ = new byte[16] [3000];
    AS400Certificate as400cert = null;

    try
    {
       //read them into byte []'s
      for (int i = 0; i < 16 ; ++i)
      {
       //filename = "/qibm/proddata/http/public/jt400ct/jt400/test/cert" + i + ".cert";
       //file = new File(filename);
       //fis = new FileInputStream(file);
       fis = AS400CertificateUsrPrfTestcase.class.getResourceAsStream("cert" + i + ".cert");
       fis.read(testcert_[i]);
       fis.close();

       //truncate certificate to exact length
       as400cert = new AS400Certificate(testcert_[i]);
       testcert_[i] = as400cert.getEncoded();

      }
    }

      catch(Exception e)
      {
       System.out.println("Setup failed, could not create test certificates. ");
       System.out.println(e);
       throw e;
      }
  }

/**
  Cleanup objects that have been created on the AS400.
    @exception  Exception  If an exception occurs.
**/
  public void cleanup()
    throws Exception
  {
     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     try {
       deleteLibrary(cmd, "CERTAUTHUS"); 
       if (cmd.run("QSYS/DLTAUTL AUTL(CERTLISTUS)") != true)
       {
             AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
       }

       if (cmd.run("DLTUSRPRF USRPRF(CERTAUTH) OWNOBJOPT(*DLT)") != true)
       {
             AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
       }

       //remove secadm from usr class profile
       if (cmd.run("CHGUSRPRF USRPRF(" + systemObject_.getUserId() + ") USRCLS(*USER) SPCAUT(*JOBCTL *IOSYSCFG)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

       AS400CertificateTest.PwrSys.disconnectAllServices();
     }
     catch(Exception e)
     {
      System.out.println("Cleanup failed.");
      System.out.println(e);
      throw e;
     }
  }

/**
Run variations.
**/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      setup();
    }
    catch (Exception e)
    {
      output_.println("Setup failed.");
      return;
    }




    if (runMode_ != ATTENDED)
    {
      if (allVariations || variationsToRun_.contains("1"))
        variation1();
      if (allVariations || variationsToRun_.contains("2"))
        variation2();
      if (allVariations || variationsToRun_.contains("3"))
        variation3();
      if (allVariations || variationsToRun_.contains("4"))
        variation4();
      if (allVariations || variationsToRun_.contains("5"))
        variation5();
      if (allVariations || variationsToRun_.contains("6"))
        variation6();
      if (allVariations || variationsToRun_.contains("7"))
        variation7();
      if (allVariations || variationsToRun_.contains("8"))
        variation8();
      if (allVariations || variationsToRun_.contains("9"))
        variation9();
      if (allVariations || variationsToRun_.contains("10"))
        variation10();
      if (allVariations || variationsToRun_.contains("11"))
        variation11();
      if (allVariations || variationsToRun_.contains("12"))
        variation12();
      if (allVariations || variationsToRun_.contains("13"))
        variation13();
      if (allVariations || variationsToRun_.contains("14"))
        variation14();
      if (allVariations || variationsToRun_.contains("15"))
        variation15();
      if (allVariations || variationsToRun_.contains("16"))
        variation16();
      if (allVariations || variationsToRun_.contains("17"))
        variation17();
      if (allVariations || variationsToRun_.contains("18"))
        variation18();
      if (allVariations || variationsToRun_.contains("19"))
        variation19();
      if (allVariations || variationsToRun_.contains("20"))
        variation20();
      if (allVariations || variationsToRun_.contains("21"))
        variation21();
      if (allVariations || variationsToRun_.contains("22"))
        variation22();
      if (allVariations || variationsToRun_.contains("23"))
        variation23();
      if (allVariations || variationsToRun_.contains("24"))
        variation24();
      if (allVariations || variationsToRun_.contains("25"))
        variation25();
      if (allVariations || variationsToRun_.contains("26"))
        variation26();
      if (allVariations || variationsToRun_.contains("27"))
        variation27();
      if (allVariations || variationsToRun_.contains("28"))
        variation28();
      if (allVariations || variationsToRun_.contains("29"))
        variation29();
      if (allVariations || variationsToRun_.contains("30"))
        variation30();
      if (allVariations || variationsToRun_.contains("31"))
        variation31();
      if (allVariations || variationsToRun_.contains("32"))
        variation32();
      if (allVariations || variationsToRun_.contains("33"))
        variation33();
      if (allVariations || variationsToRun_.contains("34"))
        variation34();
      if (allVariations || variationsToRun_.contains("35"))
        variation35();
      if (allVariations || variationsToRun_.contains("36"))
        variation36();
      if (allVariations || variationsToRun_.contains("37"))
        variation37();
      if (allVariations || variationsToRun_.contains("38"))
        variation38();
      if (allVariations || variationsToRun_.contains("39"))
        variation39();
      if (allVariations || variationsToRun_.contains("40"))
        variation40();
      if (allVariations || variationsToRun_.contains("41"))
        variation41();
      if (allVariations || variationsToRun_.contains("42"))
        variation42();
      if (allVariations || variationsToRun_.contains("43"))
        variation43();
      if (allVariations || variationsToRun_.contains("44"))
        variation44();
      if (allVariations || variationsToRun_.contains("45"))
        variation45();
      if (allVariations || variationsToRun_.contains("46"))
       variation46();
      if (allVariations || variationsToRun_.contains("47"))
        variation47();

      try
      {
        cleanup();
      }
      catch (Exception e){}
    }
  }



/**
Add a certificate to CERTTEST user profile, then delete it.
Verify it's deleted.
**/
  public void variation1()
  {
      setVariation(1);

      deleteUsrPrf(userProfileName_);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = null;

     try
     {
      try {
          aUsrprf.findCertificateUser(testcert_[0]);
          failed("Certificate already present.");
      }
      catch(Exception e){}

      aUsrprf.addCertificate(testcert_[0]);

      if (!userProfileName_.equals(aUsrprf.findCertificateUser(testcert_[0])) )
          failed("Certificate not added correctly.");

      aUsrprf.deleteCertificate(testcert_[0]);

      //list all certificates for this user, should be none registered.
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
      aUserSpace.close();
      
      int numcerts = aUsrprf.listCertificates(
                          null,
                          userSpacePathName_);
                          

      if (numcerts != 0)
          failed("Delete failed.");

      try {
          aUsrprf.findCertificateUser(testcert_[0]);
          failed("Certificate added to wrong user.");
      }
      catch(Exception e)
      {
          succeeded();
      }

     }
     catch(Exception e)
     {
      failed(e, "Unexpected exception occurred.");

     }
     deleteUserSpace(aUserSpace);
     deleteUsrPrf(userProfileName_);
  }



/**
Add a null certificate to CERTTEST user profile.
Ensure correct exception is thrown.
**/
  public void variation2()
  {
      setVariation(2);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      try
      {
       aUsrprf.addCertificate(null);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
Add a garbage certificate to CERTTEST user profile.
Ensure an invalid certificate ExtendedIOException is thrown.
**/
  public void variation3()
  {
      setVariation(3);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] inBuffer = { (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x35, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x38, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x39, (byte)0x42, (byte)0xce, (byte)0xda,
                          (byte)0x30, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x31, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xce, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x35, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda };


      try
      {
       aUsrprf.addCertificate(inBuffer);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "CPF227B: /QSYS.LIB/CERTTEST.USRPRF:", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
Add a certificate to CERTTEST user profile,
then attempt to add the same certificate again.
Verify correct exception msg.
**/
  public void variation4()
  {
      setVariation(4);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      try
      {
       aUsrprf.addCertificate(testcert_[4]);
       aUsrprf.addCertificate(testcert_[4]);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {
       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227C: /QSYS.LIB/CERTTEST.USRPRF:", ExtendedIOException.CERTIFICATE_ALREADY_ADDED ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);

  }



/**
Add a certificate to CERTTEST user profile, delete it,
then attempt to delete the same certificate again.
Verify correct exception msg.
**/
  public void variation5()
  {
      setVariation(5);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      try
      {
       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.deleteCertificate(testcert_[5]);

       aUsrprf.deleteCertificate(testcert_[5]);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {

       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227D: /QSYS.LIB/CERTTEST.USRPRF:", ExtendedIOException.CERTIFICATE_NOT_FOUND ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");

      }

      deleteUsrPrf(userProfileName_);

  }



/**
Add a certificate to CERTTEST user profile,
then delete it by certificate handle. Verify it's deleted.
**/
  public void variation6()
  {
     setVariation(6);

     genUsrPrf(userProfileName_);

     AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

     byte[] certhandle = null;

     UserSpace  aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {

      aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
      aUserSpace.close();
      
      aUsrprf.addCertificate(testcert_[6]);

      if (!userProfileName_.equals(aUsrprf.findCertificateUser(testcert_[6])) )
          failed("Certificate not added correctly.");

      certhandle = aUsrprf.getCertificateHandle(testcert_[6]);

      aUsrprf.deleteCertificateByHandle(certhandle);

      //list all certificates for this user, should be none registered.
      int numcerts = aUsrprf.listCertificates(
                          null,
                          userSpacePathName_
                          );

      if (numcerts != 0)
          failed("Delete failed.");

      try {
          aUsrprf.findCertificateUser(testcert_[6]);
          failed("Certificate added to wrong user.");
      }
      catch(Exception e)
      {
          succeeded();
      }

     }
     catch(Exception e)
     {
      failed(e, "Unexpected exception occurred.");

     }

     deleteUserSpace(aUserSpace);
     deleteUsrPrf(userProfileName_);

  }



/**
Add a certificate to CERTTEST user profile, delete it by handle,
then attempt to delete the same certificate by handle again.
Verify correct exception msg.
**/
  public void variation7()
  {
      setVariation(7);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] certhandle = null;

      try
      {
       aUsrprf.addCertificate(testcert_[7]);

       certhandle = aUsrprf.getCertificateHandle(testcert_[7]);

       aUsrprf.deleteCertificateByHandle(certhandle);

       aUsrprf.deleteCertificateByHandle(certhandle);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {
       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227D: /QSYS.LIB/CERTTEST.USRPRF:", ExtendedIOException.CERTIFICATE_NOT_FOUND ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");

      }

      deleteUsrPrf(userProfileName_);

  }



/**
Add a certificate to CERTTEST user profile,
find the certificate user by handle, delete it by handle,
then attempt to find the certificate user by handle again.
Verify correct exception msg.
**/
  public void variation8()
  {
      setVariation(8);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] certhandle = null;

      try
      {
       aUsrprf.addCertificate(testcert_[8]);

       certhandle = aUsrprf.getCertificateHandle(testcert_[8]);

       if (!userProfileName_.equals(aUsrprf.findCertificateUserByHandle(certhandle)) )
           failed("Certificate not found by user.");

       aUsrprf.deleteCertificateByHandle(certhandle);

       try {
           aUsrprf.findCertificateUserByHandle(certhandle);
           failed( "Expected exception did not occurr.");
       }
       catch(Exception e)
       {
           succeeded();
       }

      }
      catch(Exception e)
      {
       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);

  }



/**
Add several certificates to CERTTEST user profile,
find the certificate user of the 1st certificate added,
delete it,
then attempt to find the certificate user again.
Verify correct exception msg.
**/
  public void variation9()
  {
      setVariation(9);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);


      try
      {
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);

       if (!userProfileName_.equals(aUsrprf.findCertificateUser(testcert_[9])) )
           failed("Certificate not registered to correct user.");

       aUsrprf.deleteCertificate(testcert_[9]);

       try {
           aUsrprf.findCertificateUser(testcert_[9]);
           failed( "Expected exception did not occurr.");
       }
       catch(Exception e)
       {
           succeeded();
       }

      }
      catch(Exception e)
      {

       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);

  }




/**
Add several certificates to CERTTEST user profile,
list all the certificates, add more certificates,
list the certificates again, verify total number returned,
reuse same user space for next test to verify old data is not used.
**/
  public void variation10()
  {
      setVariation(10);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);

       //list all certs
       int numcerts = aUsrprf.listCertificates(
                           null,
                           userSpacePathName_
                           );

       if (numcerts != 5)
           failed("Partial list failed.");

       aUsrprf.addCertificate(testcert_[4]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[0]);

       //list all certs again
       numcerts = aUsrprf.listCertificates(
                           null,
                           userSpacePathName_
                           );

       if (numcerts != 10)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    200);

       if (as400certs.length != 10)
           failed("Get certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 10))
           failed( "Certificates did not compare.");

       succeeded();

      }
      catch(Exception e)
      {

       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
//    deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST user profile,
retrieve a single matching certificate using all the search attributes,
verify correct certificate was returned
**/
  public void variation11()
  {
      setVariation(11);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

      
//    aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");

      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.addCertificate(testcert_[4]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[0]);

       //setup attributes to search on.
       certAttribute[0] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_COUNTRY,
                           "subjCOUNTRY1");


       certAttribute[1] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_COMMON_NAME,
                                      "subject1");

       certAttribute[2] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_LOCALITY,
                           "subjLOCALITY1");


       certAttribute[3] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_STATE,
                                      "subjSTATE1");

       certAttribute[4] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_ORGANIZATION,
                           "subjORG1");


       certAttribute[5] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_ORGANIZATION_UNIT,
                                      "subjORGUNIT1");

       //also search for subjects public key
       X509Certificate x509cert = null;

       //use a JDK X509Cert to extract public key and remove leading asn tag
       //NOTE - removing asn tag will not be needed after 2/5/98 v4r3 driver.
       // x509cert = new X509Cert(testcert_[1]);
       CertificateFactory cf = CertificateFactory.getInstance("X.509");
       ByteArrayInputStream bais = new ByteArrayInputStream(testcert_[1]); 
       x509cert = (X509Certificate) cf.generateCertificate(bais); 

       //strip off leading tag and len if required
       int len, version, release; 
       byte[] subjPublicKey;

       version = 0;
       release = 0;

       try
       {
	   version = systemObject_.getVersion();
	   release = systemObject_.getRelease();
       }
       catch (Exception e){}
  
       if ( (version == 4) && (release >= 3) )
       {
	   //v4r3 and greater releases
	   subjPublicKey = x509cert.getPublicKey().getEncoded();
       }
       else
       {
	   len = x509cert.getPublicKey().getEncoded().length;
	   subjPublicKey = new byte[len - 3];
	   System.arraycopy(x509cert.getPublicKey().getEncoded(), 3,
			    subjPublicKey, 0,
			    len - 3);
       }

       certAttribute[6] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.PUBLIC_KEY_BYTES,
			   subjPublicKey );

       //get matching certs, should be 1
       int numcerts = aUsrprf.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 1)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    8);

       if (false == certCompare(as400certs[0], testcert_[1]) )
           failed( "Certificate did not compare.");

       succeeded();

      }
      catch(Exception e)
      {

       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST user profile,
retrieve certificates that have null values for state and locality fields,
verify correct certificates were returned
**/
  public void variation12()
  {
      setVariation(12);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.addCertificate(testcert_[4]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[0]);

       //setup attributes to search on.
       certAttribute[0] = null;
       certAttribute[1] = null;
       certAttribute[2] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_LOCALITY,
                           "");

       certAttribute[3] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_STATE,
                                      "");

       certAttribute[4] = null;
       certAttribute[5] = null;
       certAttribute[6] = null;


       //get matching certs, should be 3
       int numcerts = aUsrprf.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 3)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    10);

       //gather expected results for compare
       byte [][]  expectedCerts = new byte[3][];
       expectedCerts[0]  = testcert_[7];
       expectedCerts[1]  = testcert_[8];
       expectedCerts[2]  = testcert_[9];

       if (as400certs.length != 3)
           failed("Get certificates failed.");

       if (false == certsCompare(as400certs, expectedCerts, 3))
           failed( "Certificates did not compare.");

       succeeded();

      }
      catch(Exception e)
      {
       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add no certificates to CERTTEST user profile,
retrieve all certificates using null selection control,
verify no certificates were returned
**/
  public void variation13()
  {
      setVariation(13);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;


      try
      {

       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       //get matching certs, should be 3
       int numcerts = aUsrprf.listCertificates(
                           null,
                           userSpacePathName_
                           );

       if (numcerts != 0)
           failed("List failed.");


       try {
           //verify certificates were stored correctly
           as400certs = aUsrprf.getCertificates(userSpacePathName_,
						0,
						8);


           failed( "Expected exception did not occurr.");
       }
       catch(Exception e) {}


       if (as400certs != null)
           failed( "Certificates did not compare.");

       succeeded();

      }
      catch(Exception e)
      {
       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST user profile,
retrieve all certificates having a null public key,
verify no certificates were returned
**/
  public void variation14()
  {
      setVariation(14);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {

       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.addCertificate(testcert_[4]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[0]);

       //setup attributes to search on.
       certAttribute[0] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.PUBLIC_KEY_BYTES,
                            new byte[0]);


       //get matching certs, should be 3
       int numcerts = aUsrprf.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 0)
           failed("List failed.");

       try {
           //verify certificates were stored correctly
           as400certs = aUsrprf.getCertificates(userSpacePathName_,
						0,
						10);


           failed( "Expected exception did not occurr.");
       }
       catch(Exception e) {}

       if (as400certs != null)
           failed( "Certificates did not compare.");

       succeeded();

      }
      catch(Exception e)
      {
       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST user profile,
retrieve all certificates having same public key,
verify certificates returned.
**/
  public void variation15()
  {
      setVariation(15);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            AS400Certificate[]     as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {
       aUserSpace.create(11500, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       

       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.addCertificate(testcert_[4]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[0]);

       //search for subjects public key
       // X509Cert x509cert = null;
       X509Certificate x509cert = null;

       //use a JDK X509Cert to extract public key and remove leading asn tag
       //NOTE - removing asn tag will not be needed after 3/2/98 v4r3 driver.
       // x509cert = new X509Cert(testcert_[1]);
       CertificateFactory cf = CertificateFactory.getInstance("X.509");
       ByteArrayInputStream bais = new ByteArrayInputStream(testcert_[1]); 
       x509cert = (X509Certificate) cf.generateCertificate(bais); 

       //strip off leading tag and len if required
       int len, version, release; 
       byte[] subjPublicKey;

       version = 0;
       release = 0;
       
       try
       {
	   version = systemObject_.getVersion();
	   release = systemObject_.getRelease();
       }
       catch (Exception e){}

       if ( (version == 4) && (release >= 3) )
       {
	   //v4r3 and greater releases
	   subjPublicKey = x509cert.getPublicKey().getEncoded();
       }
       else
       {
	   len = x509cert.getPublicKey().getEncoded().length;
	   subjPublicKey = new byte[len - 3];
	   System.arraycopy(x509cert.getPublicKey().getEncoded(), 3,
			    subjPublicKey, 0,
			    len - 3);
       }

       certAttribute[5] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.PUBLIC_KEY_BYTES,
			   subjPublicKey );

         //get matching certs, should be 6
       int numcerts = aUsrprf.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 6)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    3550);

       //gather expected results for compare
       byte [][]  expectedCerts = new byte[6] [];
       expectedCerts[0]  = testcert_[0];
       expectedCerts[1]  = testcert_[1];
       expectedCerts[2]  = testcert_[6];
       expectedCerts[3]  = testcert_[7];
       expectedCerts[4]  = testcert_[8];
       expectedCerts[5]  = testcert_[9];

       if (as400certs.length != 6)
           failed("Get certificates failed.");

       if (false == certsCompare(as400certs, expectedCerts, 6))
           failed( "Certificates did not compare.");

       succeeded();

      }
      catch(Exception e)
      {
       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }



/**
Add no certificates to CERTTEST user profile,
test null user space name input parm for list certificates.
verify correct exception is returned.
**/
  public void variation16()
  {
      setVariation(16);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];

      try
      {
       
       int numcerts = aUsrprf.listCertificates(
                                certAttribute,
                                null
                                );

       failed( "Expected exception did not occur. numcerts="+numcerts);

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "userSpaceName"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");


      deleteUsrPrf(userProfileName_);
     }

  }


/**
Add no certificates to CERTTEST user profile,
test null user space lib name input parm for list certificates.
verify correct exception is returned.
**/
  public void variation17()
  {
      setVariation(17);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];

      try
      {
       
       int numcerts = aUsrprf.listCertificates(
                                certAttribute,
                                "/QSYS.LIB/US.USRSPC"
                                );

       failed( "Expected exception did not occurr."+numcerts);

      }

     catch(Exception e)
     {
	 if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF9801: USERSPACENAME (US        )", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
	 succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
Add no certificates to CERTTEST user profile,
test too many search attributes provided for list certificates.
verify correct exception is returned.
**/
  public void variation18()
  {
      setVariation(18);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {

       //setup attributes to search on.
       certAttribute[0] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_COUNTRY,
                           "subjCOUNTRY1");


       certAttribute[1] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_COMMON_NAME,
                                      "subject1");

       certAttribute[2] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_LOCALITY,
                           "subjLOCALITY1");


       certAttribute[3] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_STATE,
                                      "subjSTATE1");

       certAttribute[4] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_ORGANIZATION,
                           "subjORG1");


       certAttribute[5] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.SUBJECT_ORGANIZATION_UNIT,
                                      "subjORGUNIT1");

       //use dummy public key
       byte[] subjPublicKey = {(byte)0x11, (byte)0x22, (byte)0x33 };

       certAttribute[6] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.PUBLIC_KEY_BYTES,
                                      subjPublicKey);

       //the extra search attr
       certAttribute[7] = new AS400CertificateAttribute(
                         AS400CertificateAttribute.SUBJECT_COUNTRY,
                                        "subjCOUNTRYx");

       //should blow
       int numcerts = aUsrprf.listCertificates(
                                certAttribute,
                                userSpacePathName_
                                );

       failed( "Expected exception did not occurr."+numcerts);

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "AS400CertificateAttribute (7)", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
test bad search attribute type.
verify correct exception is returned.
**/
  public void variation19()
  {
      setVariation(19);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[7];

      try
      {

       //the invalid  search attr
       certAttribute[6] = new AS400CertificateAttribute(
                                      24,
                                      "subjCOUNTRYx");

        failed( "Expected exception did not occurr."+aUsrprf);


      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "attributeType (24)", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
test invalid byte search attribute data.
verify correct exception is returned.
**/
  public void variation20()
  {
      setVariation(20);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[7];

      try
      {
       //the invalid  search attr
       certAttribute[6] = new AS400CertificateAttribute(AS400CertificateAttribute.PUBLIC_KEY_BYTES,
                                      "subjCOUNTRYx");

        failed( "Expected exception did not occurr.");


      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "attributeType (1)", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
test invalid String search attribute data.
verify correct exception is returned.
**/
  public void variation21()
  {
      setVariation(21);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[7];

      byte[] byteAttr = {(byte)0x11, (byte)0x22, (byte)0x33 };


      try
      {
       //the invalid  search attr
       certAttribute[6] = new AS400CertificateAttribute(AS400CertificateAttribute.SUBJECT_COMMON_NAME,
                                      byteAttr);

        failed( "Expected exception did not occurr.");
    }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "attributeType (2)", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }


/**
find certificate user with invalid certificate input parm,
Verify correct exception msg.
**/
  public void variation22()
  {
      setVariation(22);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] bytejunk = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44 };

      try
      {
       aUsrprf.findCertificateUser(bytejunk);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "CPF3C1D", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }


/**
find certificate user with null input parm,
Verify correct exception msg.
**/
  public void variation23()
  {
      setVariation(23);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);


      try
      {
       aUsrprf.findCertificateUser(null);

       failed("Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
Delete a null certificate from CERTTEST user profile.
Ensure correct exception is thrown.
**/
  public void variation24()
  {
      setVariation(24);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      try
      {
       aUsrprf.deleteCertificate(null);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
delete a garbage certificate from CERTTEST user profile.
Ensure the correct exception is thrown.
**/
  public void variation25()
  {
      setVariation(25);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] inBuffer = { (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x35, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x38, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x39, (byte)0x42, (byte)0xce, (byte)0xda,
                          (byte)0x30, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x31, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xce, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x35, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda,
                      (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda };

      try
      {
       aUsrprf.deleteCertificate(inBuffer);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "CPF227B", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
Delete a null certificate by handle from CERTTEST user profile.
Ensure correct exception is thrown.
**/
  public void variation26()
  {
      setVariation(26);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      try
      {
       aUsrprf.deleteCertificateByHandle(null);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificateHandle"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
delete a garbage certificate by handle from CERTTEST user profile.
Ensure the correct exception is thrown.
**/
  public void variation27()
  {
      setVariation(27);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] inBuffer = {
                       (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x35, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x38, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x39, (byte)0x42, (byte)0xce, (byte)0xda,
                          (byte)0x30, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x31, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xce, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x35, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda,
                      (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                       (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda };

      try
      {
       aUsrprf.deleteCertificateByHandle(inBuffer);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "CPF3C1D", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
find certificate user by handle with invalid certificate input parm,
Verify correct exception msg.
**/
  public void variation28()
  {
      setVariation(28);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] bytejunk = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44 };

      try
      {
       aUsrprf.findCertificateUserByHandle(bytejunk);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "CPF3C1D", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
find certificate user by handle with null input parm,
Verify correct exception msg.
**/
  public void variation29()
  {
      setVariation(29);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);


      try
      {
       aUsrprf.findCertificateUserByHandle(null);

       failed("Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificateHandle"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
Add several certificates to CERTTEST user profile,
use nonexistent user space to list the certificates,
verify correct exceptionm is thrown.
**/
  public void variation30()
  {
      setVariation(30);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[5]);

       //list all certs
       int numcerts = aUsrprf.listCertificates(
			null,
			non_existingUserSpace_);
                           

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF9801: USERSPACENAME (XKG52V9QWQ)", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
use nonexistent user profile to list the certificates,
verify correct exceptionm is thrown.
**/
  public void variation31()
  {
      setVariation(31);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, "/QSYS.LIB/XKG52V9QWQ.USRPRF");

      AS400Certificate[] as400certs = null;

      try
      {
       aUsrprf.addCertificate(testcert_[9]);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF2204:", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
attempt to list certificates from an unauthorized user profile,
verify correct exception was thrown
**/
  public void variation32()
  {
      setVariation(32);

      genUsrPrfNoAut(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {

       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       int numcerts = aUsrprf.listCertificates(
                                null,
                                userSpacePathName_
                                );

       failed( "Expected exception did not occurred.");

      }
    catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "CPF2217:", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }



  /**
    Ensure AS400SecurityException is thrown if user profile does
    not have authority to the list library.
  **/
  public void variation33()
  {
      setVariation(33);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

     try {

      UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/CERTAUTHUS.LIB/USWRITE3.USRSPC");

      int numcerts =  aUsrprf.listCertificates(
			       null,
                               "/QSYS.LIB/CERTAUTHUS.LIB/USWRITE3.USRSPC");
      
        failed("Expected exception did not occur (make sure -uid on command line does not have authority to CERTAUTHUS.lib/uswrite3.usrspc).");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "CPF9802:",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

     systemObject_.disconnectAllServices();

  }



  /**
    Ensure AS400SecurityException is thrown if user profile does
    not have authority to the list user space.
  **/
  public void variation34()
  {
      setVariation(34);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     try
     {
        if(cmd.run("GRTOBJAUT OBJ(CERTLIBUS/USWRITE2) OBJTYPE(*USRSPC) USER(" + systemObject_.getUserId() + ") AUT(*EXCLUDE)") != true)
        {
           AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
        }

        if (usingNativeImpl)
        {
	    UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/CERTLIBUS.LIB/USWRITE2.USRSPC");
                               
        }
        else
        {
           
	    UserSpace aUserSpace = new UserSpace(certSystem_, "/QSYS.LIB/CERTLIBUS.LIB/USWRITE2.USRSPC");

	}
	
       int numcerts = aUsrprf.listCertificates(
				null,
				"/QSYS.LIB/CERTLIBUS.LIB/USWRITE2.USRSPC");		
	
        failed("Expected exception did not occur (make sure -uid does not have authority to write to CERTLIBUS.lib/uswrite2.usrspc).");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "CPF9802:",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     try
     {
        UserSpace aUserSpace = new UserSpace(AS400CertificateTest.PwrSys, "/QSYS.LIB/CERTLIBUS.LIB/USWRITE2.USRSPC");
        aUserSpace.delete();

	deleteLibrary(cmd,"CERTLIBUS");

        AS400CertificateTest.PwrSys.disconnectAllServices();
        certSystem_.disconnectAllServices();
     }
     catch(Exception e)
     {
        output_.println("Cleanup failed.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
Add a certificate to CERTTEST user profile, get it's handle,
delete it by handle,
verify correct exceptionm is thrown.
**/
  public void variation35()
  {
      setVariation(35);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte [] handle;

      try
      {
       aUsrprf.addCertificate(testcert_[5]);

       handle = aUsrprf.getCertificateHandle(testcert_[5]);

       aUsrprf.deleteCertificateByHandle(handle);

       aUsrprf.deleteCertificate(testcert_[5]);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {
       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227D:", ExtendedIOException.CERTIFICATE_NOT_FOUND ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);

  }



/**
get certificate handle with invalid certificate input parm,
Verify correct exception msg.
**/
  public void variation36()
  {
      setVariation(36);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      byte[] bytejunk = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44 };

      try
      {
       aUsrprf.getCertificateHandle(bytejunk);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIOException",
           "CPF3C1D:" , ExtendedIOException.INVALID_CERTIFICATE))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
get certificate handle with null input parm,
Verify correct exception msg.
**/
  public void variation37()
  {
      setVariation(37);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);


      try
      {
       aUsrprf.getCertificateHandle(null);

       failed("Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
use nonexistent user space to get certificates,
verify correct exceptionm is thrown.
**/
  public void variation38()
  {
      setVariation(38);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {

       as400certs = aUsrprf.getCertificates(
                           non_existingUserSpace_,
			    0,
                           8);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF9801: USERSPACENAME (XKG52V9QWQ)", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }




/**
use nonexistent user space to get first certificates,
verify correct exceptionm is thrown.
**/
  public void variation39()
  {
      setVariation(39);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {

       as400certs  = aUsrprf.getFirstCertificates(
                           non_existingUserSpace_,
                           8);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF9801: USERSPACENAME (XKG52V9QWQ)", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

     deleteUsrPrf(userProfileName_);

  }



/**
get certificates with minus firstCertificateToReturn input parm,
Verify correct exception msg.
**/
  public void variation40()
  {
      setVariation(40);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aUsrprf.getCertificates(
                                non_existingUserSpace_,
			        -1,
			         8);


       failed("Expected exception did not occurr.");

     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "firstCertificateToReturn (-1):", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
get certificates with out of range firstCertificateToReturn input parm,
Verify correct exception msg.
**/
  public void variation41()
  {
      setVariation(41);

      genUsrPrf(userProfileName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
	  aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
	  aUserSpace.close();
	  
       aUsrprf.addCertificate(testcert_[9]);

       aUsrprf.listCertificates(
                       null,
                       userSpacePathName_
                       );

       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    9984321,
					    8);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "FIRSTCERTIFICATETORETURN (9984321)", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

      deleteUserSpace(aUserSpace);

  }



/**
get certificates with out of range buffsize input parm,
Verify correct exception msg.
**/
  public void variation42()
  {
      setVariation(42);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    -1);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "buffSize (-1)", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
get first certificates with out of range buffsize input parm,
Verify correct exception msg.
**/
  public void variation43()
  {
      setVariation(43);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aUsrprf.getFirstCertificates(userSpacePathName_,
						 16385);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "buffSize (16385)", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }




/**
get next certificates with out of range buffsize input parm,
Verify correct exception msg.
**/
  public void variation44()
  {
      setVariation(44);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aUsrprf.getFirstCertificates(userSpacePathName_,
						 3);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "buffSize (3)", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }



/**
Add several certificates to CERTTEST user profile,
get the certificates with get certificates, add more certificates,
get new certificates with get next certificates,
verify total number returned,
**/
  public void variation45()
  {
      setVariation(45);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);



      AS400Certificate[] as400certs = null;

      Vector        certVector = new Vector();

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aUsrprf.addCertificate(testcert_[0]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[4]);

       //list the certs
       int numcerts = aUsrprf.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    8);

       if (as400certs.length != 5)
           failed("Get 5 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates did not compare.");


       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[10]);
       aUsrprf.addCertificate(testcert_[11]);
       aUsrprf.addCertificate(testcert_[12]);
       aUsrprf.addCertificate(testcert_[13]);
       aUsrprf.addCertificate(testcert_[14]);
       aUsrprf.addCertificate(testcert_[15]);

       //list the certs
       numcerts = aUsrprf.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    8);

       if( as400certs.length != 5)
       {
           String msg = "Get next 5 certificates failed. number returned => " + as400certs.length;
           failed(msg);
       }

       if (false == certsCompare(as400certs, testcert_, 16))
           failed( "Certificates next 5 did not compare.");

       //certificates did not change
       if (true == certsCompare(as400certs, testcert_, 5))
           failed( "Old 5 certificates remaining.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 1
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs.length < 4)
           failed("Get next certificates 1 failed.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 2
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs.length < 1)
           failed("Get next certificates 2 failed.");

       // Gather certificates in a vector
       for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 3
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs != null)
           failed("Get next certificates 3 failed.");

       //gather expected results for compare
       AS400Certificate[]     as400resultcerts = new AS400Certificate[16];

       for (int i = 0; i < certVector.size(); ++i)
       {
           as400resultcerts[i] =  (AS400Certificate)certVector.elementAt(i);
       }

       if (false == certsCompare(as400resultcerts, testcert_, 16))
           failed( "Total get next certificates did not compare.");


       //get certificates in a single buffer
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    0,
					    20);

       if (as400certs.length != 16)
           failed("Get 16 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 16))
           failed( "Get 16 certificates failed.");

       succeeded();

      }
      catch(Exception e)
      {

       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }



/**
Add several certificates to CERTTEST user profile,
get the certificates at specified offset with get certificates,
add more certificates,
get new certificates with get first/next certificates,
verify total number returned,
**/
  public void variation46()
  {
      setVariation(46);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;

      Vector        certVector = new Vector();

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aUsrprf.addCertificate(testcert_[0]);
       aUsrprf.addCertificate(testcert_[1]);
       aUsrprf.addCertificate(testcert_[2]);
       aUsrprf.addCertificate(testcert_[3]);
       aUsrprf.addCertificate(testcert_[4]);

       //list the certs
       int numcerts = aUsrprf.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs at offset 1
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    1,
					    8);

       if (as400certs.length != 4)
           failed("Get 4 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates 4 did not compare.");

       //get first certs
       as400certs = aUsrprf.getFirstCertificates(userSpacePathName_,
						 8);

       if (as400certs.length != 5)
           failed("Get first 5 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates first 5 did not compare.");


       //get the cert at offset 4
       as400certs = aUsrprf.getCertificates(userSpacePathName_,
					    4,
					    8);

       if (as400certs.length != 1)
           failed("Get 1 certificate failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates 1 did not compare.");

       //get null next certs
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs != null)
           failed("Get next certificates 0 failed.");

       aUsrprf.addCertificate(testcert_[5]);
       aUsrprf.addCertificate(testcert_[6]);
       aUsrprf.addCertificate(testcert_[7]);
       aUsrprf.addCertificate(testcert_[8]);
       aUsrprf.addCertificate(testcert_[9]);
       aUsrprf.addCertificate(testcert_[10]);
       aUsrprf.addCertificate(testcert_[11]);
       aUsrprf.addCertificate(testcert_[12]);
       aUsrprf.addCertificate(testcert_[13]);
       aUsrprf.addCertificate(testcert_[14]);
       aUsrprf.addCertificate(testcert_[15]);

       //list the certs
       numcerts = aUsrprf.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs
       as400certs = aUsrprf.getFirstCertificates(userSpacePathName_,
						 8);

       if( as400certs.length != 5)
       {
           String msg = "Get 5 certificates failed. number returned => " + as400certs.length;
           failed(msg);
       }

       if (false == certsCompare(as400certs, testcert_, 16))
           failed( "Certificates 5 did not compare.");


       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 1
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs.length < 4)
           failed("Get next certificates 1 failed.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 2
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs.length < 1)
           failed("Get next certificates 2 failed.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 3
       as400certs = aUsrprf.getNextCertificates(8);

       if (as400certs != null)
           failed("Get next certificates 3 failed.");

       //gather expected results for compare
       AS400Certificate[]     as400resultcerts = new AS400Certificate[16];

       for (int i = 0; i < certVector.size(); ++i)
       {
           as400resultcerts[i] =  (AS400Certificate)certVector.elementAt(i);
       }

       if (as400resultcerts.length != 16)
           failed("Get total certificates failed.");

       if (false == certsCompare(as400resultcerts, testcert_, 16))
           failed( "Total get next certificates did not compare.");

       succeeded();

      }
      catch(Exception e)
      {

       failed(e, "Unexpected exception occurred.");
      }

      deleteUsrPrf(userProfileName_);
      deleteUserSpace(aUserSpace);

  }



/**
get next certificates with out setting user space name or firstCertificateToReturn,
Verify correct exception msg.
**/
  public void variation47()
  {
      setVariation(47);

      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, userProfilePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aUsrprf.getNextCertificates(8);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIOException",
          "", ExtendedIOException.INVALID_REQUEST))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

      deleteUsrPrf(userProfileName_);

  }

}






