///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400CertificateVldlTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.MiscAH;

import java.io.*;
import java.util.Vector;
// 
// Support removed in JDK 1.7 
// import sun.security.x509.*;
//
// Replace with java.security.cert version
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

import com.ibm.as400.access.AS400Certificate;
import com.ibm.as400.access.AS400CertificateAttribute;
import com.ibm.as400.access.AS400CertificateVldlUtil;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ConnectionDroppedException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ObjectAlreadyExistsException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import java.io.IOException;


import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.UserSpace;

import test.AS400CertificateTest;
import test.JTOpenTestEnvironment;
import test.Testcase;

/**
Test methods for AS400CertificateVldlUtil.
**/
public class AS400CertificateVldlTestcase extends Testcase
{
  private int maxUserSpaceSize_ = 16776704;
  private AS400 certSystem_;
  private static String ifsDirName_ = "/";
  private static String vldlPathName_ = "/QSYS.LIB/CERTTEST.LIB/VLDLCERT.VLDL";
  private static String vldlName_ = "CERTTEST/VLDLCERT";
  private static String userSpacePathName_ = "/QSYS.LIB/CERTTEST.LIB/US.USRSPC";
  private String pre_existingUserSpace_ = "/QSYS.LIB/CERTTEST.LIB/PREEXIST.USRSPC";
  private String non_existingUserSpace_ = "/QSYS.LIB/CERTTEST.LIB/XKG52V9QWQ.USRSPC";
  private byte pre_existingByteValue_ = (byte)0x00;
  private int pre_existingLengthValue_ = 11000;
  private String operatingSystem_;
  private boolean usingNativeImpl = false;
  boolean failed;  // Keeps track of failure in multi-part tests.
  String msg;      // Keeps track of reason for failure in multi-part tests.
  byte[][] testcert_ = null; // Array of X.509 test certificates

/**
Constructor.
**/
  public AS400CertificateVldlTestcase(AS400  systemObject,
                                Vector           variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream,
                                String           driveLetter)
  {
    super(systemObject, "AS400CertificateVldlTestcase", 47, variationsToRun, runMode,
          fileOutputStream);


  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {


    // Determine operating system we're running under
    operatingSystem_ = System.getProperty("os.name");


    // Are we running on the AS/400?
    if (operatingSystem_.indexOf("OS/400") >= 0)
    {
      usingNativeImpl = true;
      output_.println("Will use native implementation");
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("Executing " + (isApplet_ ? "applet." : "application."));

    testInit();
  }


  void deleteVldl(String aVldl)
  {
      CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

      String clcmd;

      clcmd = "DLTVLDL VLDL(" + aVldl + ") ";

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
  void genVldl(String aVldl)
  {
     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     String clcmd;

    try
    {

     clcmd = "CRTVLDL VLDL(" + aVldl + ")";

     if(cmd.run(clcmd) == false)
     {
            AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
     }


     clcmd = "GRTOBJAUT OBJ(" + aVldl + ") OBJTYPE(*VLDL) USER(" +  systemObject_.getUserId()   + ") AUT(*ALL)";

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


  //no auth to generated vldl is granted to currently running usrprf
  void genVldlNoAut(String aVldl)
  {
     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     String clcmd;

     clcmd = "CRTVLDL VLDL(" + aVldl + ") AUT(*EXCLUDE) ";

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
      if (isApplet_)
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

    // Create a validation list and library/userspace with no authority to.
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
        // Create an AS400 object to be used with CERTTEST validation list.
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

    //read in 17 test X.509 certificates
    //last 7 are just big honkers to cause buffer overflows
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
       fis = AS400CertificateVldlTestcase.class.getResourceAsStream("cert" + i + ".cert");
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
  protected void cleanup()
    throws Exception
  {
      CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

      try {

	  deleteLibrary(cmd, "CERTLIBUS");

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
Add a certificate to CERTTEST validation list, then delete it.
Verify it's deleted.
**/
  public void variation1()
  {
      setVariation(1);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = null;
      String certuser = null;

     try
     {

      if (aVldl.checkCertificate(testcert_[0]))
	  failed("Certificate already present.");

      aVldl.addCertificate(testcert_[0]);

      if (!(aVldl.checkCertificate(testcert_[0])) )
          failed("Certificate not added correctly.");

      aVldl.deleteCertificate(testcert_[0]);

      //list all certificates for this vldl, should be none registered.
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
      aUserSpace.close();
      
      int numcerts = aVldl.listCertificates(
                          null,
                          userSpacePathName_);

      if (numcerts != 0)
          failed("Delete failed.");

      if (aVldl.checkCertificate(testcert_[0]))
	  failed("Certificate added to wrong vldl .");

      succeeded();

     }
     catch(Exception e)
     {
      failed(e, "Unexpected exception occurred.");

     }
     deleteUserSpace(aUserSpace);
     deleteVldl(vldlName_);
  }



/**
Add a null certificate to CERTTEST validation list.
Ensure correct exception is thrown.
**/
  public void variation2()
  {
      setVariation(2);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      try
      {
       aVldl.addCertificate(null);

       failed("Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }



/**
Add a garbage certificate to CERTTEST validation list.
Ensure an invalid certificate ExtendedIOException is thrown.
**/
  public void variation3()
  {
      setVariation(3);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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
       aVldl.addCertificate(inBuffer);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "CPF227B: /QSYS.LIB/CERTTEST.LIB/VLDLCERT.VLDL:", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteVldl(vldlName_);

  }



/**
Add a certificate to CERTTEST validation list,
then attempt to add the same certificate again.
Verify correct exception msg.
**/
  public void variation4()
  {
      setVariation(4);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      try
      {
       aVldl.addCertificate(testcert_[4]);
       aVldl.addCertificate(testcert_[4]);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {
       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227C: /QSYS.LIB/CERTTEST.LIB/VLDLCERT.VLDL:", ExtendedIOException.CERTIFICATE_ALREADY_ADDED ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
      }

      deleteVldl(vldlName_);

  }



/**
Add a certificate to CERTTEST validation list, delete it,
then attempt to delete the same certificate again.
Verify correct exception msg.
**/
  public void variation5()
  {
      setVariation(5);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      try
      {
       aVldl.addCertificate(testcert_[5]);
       aVldl.deleteCertificate(testcert_[5]);

       aVldl.deleteCertificate(testcert_[5]);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {

       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227D: /QSYS.LIB/CERTTEST.LIB/VLDLCERT.VLDL:", ExtendedIOException.CERTIFICATE_NOT_FOUND ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");

      }

      deleteVldl(vldlName_);

  }



/**
Add a certificate to CERTTEST validation list,
then delete it by certificate handle. Verify it's deleted.
**/
  public void variation6()
  {
     setVariation(6);

     genVldl(vldlName_);

     AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

     String certuser = null;
     byte[] certhandle = null;

     UserSpace  aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {

	 aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
	 aUserSpace.close();
	 
      aVldl.addCertificate(testcert_[6]);

      if (!(aVldl.checkCertificate(testcert_[6])) )
          failed("Certificate not added correctly.");

      certhandle = aVldl.getCertificateHandle(testcert_[6]);

      aVldl.deleteCertificateByHandle(certhandle);

      //list all certificates for this vldl, should be none registered.
      int numcerts = aVldl.listCertificates(
                          null,
                          userSpacePathName_
                          );

      if (numcerts != 0)
          failed("Delete failed.");


      if  (aVldl.checkCertificate(testcert_[6]))
          failed("Certificate added to wrong vldl .");

      succeeded();


     }
     catch(Exception e)
     {
      failed(e, "Unexpected exception occurred.");

     }

     deleteUserSpace(aUserSpace);
     deleteVldl(vldlName_);

  }



/**
Add a certificate to CERTTEST validation list, delete it by handle,
then attempt to delete the same certificate by handle again.
Verify correct exception msg.
**/
  public void variation7()
  {
      setVariation(7);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      byte[] certhandle = null;

      try
      {
       aVldl.addCertificate(testcert_[7]);

       certhandle = aVldl.getCertificateHandle(testcert_[7]);

       aVldl.deleteCertificateByHandle(certhandle);

       aVldl.deleteCertificateByHandle(certhandle);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {
       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227D: /QSYS.LIB/CERTTEST.LIB/VLDLCERT.VLDL:", ExtendedIOException.CERTIFICATE_NOT_FOUND ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");

      }

      deleteVldl(vldlName_);

  }



/**
Add a certificate to CERTTEST validation list,
find the certificate by handle, delete it by handle,
then attempt to find the certificate by handle again.
Verify correct exception msg.
**/
  public void variation8()
  {
      setVariation(8);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      byte[] certhandle = null;

      try
      {
       aVldl.addCertificate(testcert_[8]);

       certhandle = aVldl.getCertificateHandle(testcert_[8]);

       if (!(aVldl.checkCertificateByHandle(certhandle)) )
           failed("Certificate not found by user.");

       aVldl.deleteCertificateByHandle(certhandle);


       if (aVldl.checkCertificateByHandle(certhandle))
           failed( "Expected exception did not occurr.");

       succeeded();


      }
      catch(Exception e)
      {
       failed(e, "Unexpected exception occurred.");
      }

      deleteVldl(vldlName_);

  }



/**
Add several certificates to CERTTEST validation list,
find the certificate of the 1st certificate added,
delete it,
then attempt to find the certificate again.
Verify correct exception msg.
**/
  public void variation9()
  {
      setVariation(9);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);


      try
      {
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);

       if (!(aVldl.checkCertificate(testcert_[9])) )
           failed("Certificate not registered to correct user.");

       aVldl.deleteCertificate(testcert_[9]);


       if (aVldl.checkCertificate(testcert_[9]))
           failed( "Expected exception did not occurr.");

       succeeded();


      }
      catch(Exception e)
      {

       failed(e, "Unexpected exception occurred.");
      }

      deleteVldl(vldlName_);

  }




/**
Add several certificates to CERTTEST validation list,
list all the certificates, add more certificates,
list the certificates again, verify total number returned,
reuse same user space for next test to verify old data is not used.
**/
  public void variation10()
  {
      setVariation(10);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);

       //list all certs
       int numcerts = aVldl.listCertificates(
                           null,
                           userSpacePathName_
                           );

       if (numcerts != 5)
           failed("Partial list failed.");

       aVldl.addCertificate(testcert_[4]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[0]);

       //list all certs again
       numcerts = aVldl.listCertificates(
                           null,
                           userSpacePathName_
                           );

       if (numcerts != 10)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
//    deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST validation list,
retrieve a single matching certificate using all the search attributes,
verify correct certificate was returned
**/
  public void variation11()
  {
      setVariation(11);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


//    aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");

      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);
       aVldl.addCertificate(testcert_[4]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[0]);

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
       CertificateFactory cf = CertificateFactory.getInstance("X.509");
       ByteArrayInputStream bais = new ByteArrayInputStream(testcert_[1]); 
       x509cert = (X509Certificate) cf.generateCertificate(bais); 
       // x509cert = new X509Cert(testcert_[1]);

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
       int numcerts = aVldl.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 1)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST validation list,
retrieve certificates that have null values for state and locality fields,
verify correct certificates were returned
**/
  public void variation12()
  {
      setVariation(12);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);
       aVldl.addCertificate(testcert_[4]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[0]);

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
       int numcerts = aVldl.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 3)
           failed("List failed.");

       //verify certificates were stored correctly
       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add no certificates to CERTTEST validation list,
retrieve all certificates using null selection control,
verify no certificates were returned
**/
  public void variation13()
  {
      setVariation(13);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;


      try
      {

       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       //get matching certs, should be 3
       int numcerts = aVldl.listCertificates(
                           null,
                           userSpacePathName_
                           );

       if (numcerts != 0)
           failed("List failed.");


       try {
           //verify certificates were stored correctly
           as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST validation list,
retrieve all certificates having a null public key,
verify no certificates were returned
**/
  public void variation14()
  {
      setVariation(14);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {

       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);
       aVldl.addCertificate(testcert_[4]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[0]);

       //setup attributes to search on.
       certAttribute[0] = new AS400CertificateAttribute(
                           AS400CertificateAttribute.PUBLIC_KEY_BYTES,
                            new byte[0]);


       //get matching certs, should be 3
       int numcerts = aVldl.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 0)
           failed("List failed.");

       try {
           //verify certificates were stored correctly
           as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }




/**
Add certificates to CERTTEST validation list,
retrieve all certificates having same public key,
verify certificates returned.
**/
  public void variation15()
  {
      setVariation(15);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            AS400Certificate[]     as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {
       aUserSpace.create(11500, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       

       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);
       aVldl.addCertificate(testcert_[4]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[0]);

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
       int numcerts = aVldl.listCertificates(
                           certAttribute,
                           userSpacePathName_
                           );

       if (numcerts != 6)
       {
	   System.out.println("public key list => " + numcerts);
	   failed("List failed.");
       }

       //verify certificates were stored correctly
       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }



/**
Add no certificates to CERTTEST validation list,
test null user space name input parm for list certificates.
verify correct exception is returned.
**/
  public void variation16()
  {
      setVariation(16);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];

      try
      {
       //get matching certs, should be 1
       int numcerts = aVldl.listCertificates(
                                certAttribute,
                                null
                                );

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "userSpaceName"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");


      deleteVldl(vldlName_);
     }

  }


/**
Add no certificates to CERTTEST validation list,
test null user space lib name input parm for list certificates.
verify correct exception is returned.
**/
  public void variation17()
  {
      setVariation(17);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];

      try
      {
       //get matching certs, should be 1
       int numcerts = aVldl.listCertificates(
                                certAttribute,
                                "/QSYS.LIB/US.USRSPC"
                                );

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
	if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF9801: USERSPACENAME (US        )", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }




/**
Add no certificates to CERTTEST validation list,
test too many search attributes provided for list certificates.
verify correct exception is returned.
**/
  public void variation18()
  {
      setVariation(18);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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
       int numcerts = aVldl.listCertificates(
                                certAttribute,
                                userSpacePathName_
                                );

       failed( "Expected exception did not occurr.");

      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "AS400CertificateAttribute (7)", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteVldl(vldlName_);

  }



/**
test bad search attribute type.
verify correct exception is returned.
**/
  public void variation19()
  {
      setVariation(19);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[7];

      try
      {

       //the invalid  search attr
       certAttribute[6] = new AS400CertificateAttribute(
                                      24,
                                      "subjCOUNTRYx");

        failed( "Expected exception did not occurr.");


      }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "attributeType (24)", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     deleteVldl(vldlName_);

  }



/**
test invalid byte search attribute data.
verify correct exception is returned.
**/
  public void variation20()
  {
      setVariation(20);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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

     deleteVldl(vldlName_);

  }



/**
test invalid String search attribute data.
verify correct exception is returned.
**/
  public void variation21()
  {
      setVariation(21);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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

     deleteVldl(vldlName_);

  }


/**
check certificate with invalid certificate input parm,
Verify correct exception msg.
**/
  public void variation22()
  {
      setVariation(22);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      byte[] bytejunk = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44 };

      try
      {
       aVldl.checkCertificate(bytejunk);

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

      deleteVldl(vldlName_);

  }


/**
check certificate with null input parm,
Verify correct exception msg.
**/
  public void variation23()
  {
      setVariation(23);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);


      try
      {
       aVldl.checkCertificate(null);

       failed("Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }



/**
Delete a null certificate from CERTTEST validation list.
Ensure correct exception is thrown.
**/
  public void variation24()
  {
      setVariation(24);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      try
      {
       aVldl.deleteCertificate(null);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }



/**
delete a garbage certificate from CERTTEST validation list.
Ensure the correct exception is thrown.
**/
  public void variation25()
  {
      setVariation(25);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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
       aVldl.deleteCertificate(inBuffer);

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

     deleteVldl(vldlName_);

  }



/**
Delete a null certificate by handle from CERTTEST validation list.
Ensure correct exception is thrown.
**/
  public void variation26()
  {
      setVariation(26);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      try
      {
       aVldl.deleteCertificateByHandle(null);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificateHandle"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }



/**
delete a garbage certificate by handle from CERTTEST validation list.
Ensure the correct exception is thrown.
**/
  public void variation27()
  {
      setVariation(27);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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
       aVldl.deleteCertificateByHandle(inBuffer);

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

     deleteVldl(vldlName_);

  }



/**
check certificate by handle with invalid certificate input parm,
Verify correct exception msg.
**/
  public void variation28()
  {
      setVariation(28);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      byte[] bytejunk = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44 };

      try
      {
       aVldl.checkCertificateByHandle(bytejunk);

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

      deleteVldl(vldlName_);

  }



/**
check certificate by handle with null input parm,
Verify correct exception msg.
**/
  public void variation29()
  {
      setVariation(29);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);


      try
      {
       aVldl.checkCertificateByHandle(null);

       failed("Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificateHandle"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }



/**
Add several certificates to CERTTEST validation list,
use nonexistent user space to list the certificates,
verify correct exceptionm is thrown.
**/
  public void variation30()
  {
      setVariation(30);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[5]);

       //list all certs
       int numcerts = aVldl.listCertificates(
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

     deleteVldl(vldlName_);

  }



/**
use nonexistent validation list to list the certificates,
verify correct exceptionm is thrown.
**/
  public void variation31()
  {
      setVariation(31);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, "/QSYS.LIB/CERTTEST.LIB/XKG52V9QWQ.VLDL");

      AS400Certificate[] as400certs = null;

      try
      {
       aVldl.addCertificate(testcert_[9]);

       failed( "Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ObjectDoesNotExistException", "CPF9801:", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

     deleteVldl(vldlName_);

  }



/**
attempt to list certificates from an unauthorized validation list,
verify correct exception was thrown
**/
  public void variation32()
  {
      setVariation(32);

      genVldlNoAut(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;
      AS400CertificateAttribute[] certAttribute = new AS400CertificateAttribute[20];


      try
      {

       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       int numcerts = aVldl.listCertificates(
                                null,
                                userSpacePathName_
                                );

       failed( "Expected exception did not occurred.");

      }
    catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "CPF9802:", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }



  /**
    Ensure AS400SecurityException is thrown if user does
    not have authority to the list library.
  **/
  public void variation33()
  {
      setVariation(33);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

     try {

      UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/CERTAUTHUS.LIB/USWRITE3.USRSPC");

      int numcerts =  aVldl.listCertificates(
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

     deleteVldl(vldlName_);

     systemObject_.disconnectAllServices();

  }



  /**
    Ensure AS400SecurityException is thrown if user does
    not have authority to the list user space.
  **/
  public void variation34()
  {
      setVariation(34);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

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
	
	int numcerts = aVldl.listCertificates(
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

     deleteVldl(vldlName_);

  }



/**
Add a certificate to CERTTEST validation list, get it's handle,
delete it by handle,
verify correct exceptionm is thrown.
**/
  public void variation35()
  {
      setVariation(35);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      byte [] handle;

      try
      {
       aVldl.addCertificate(testcert_[5]);

       handle = aVldl.getCertificateHandle(testcert_[5]);

       aVldl.deleteCertificateByHandle(handle);

       aVldl.deleteCertificate(testcert_[5]);

       failed( "Expected exception did not occurr.");

      }
      catch(Exception e)
      {
       if (exceptionStartsWith(e, "ExtendedIOException", "CPF227D:", ExtendedIOException.CERTIFICATE_NOT_FOUND ))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
      }

      deleteVldl(vldlName_);

  }



/**
get certificate handle with invalid certificate input parm,
Verify correct exception msg.
**/
  public void variation36()
  {
      setVariation(36);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      byte[] bytejunk = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44 };

      try
      {
       aVldl.getCertificateHandle(bytejunk);

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

      deleteVldl(vldlName_);

  }



/**
get certificate handle with null input parm,
Verify correct exception msg.
**/
  public void variation37()
  {
      setVariation(37);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);


      try
      {
       aVldl.getCertificateHandle(null);

       failed("Expected exception did not occurr.");

      }

     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "certificate"))
           succeeded();
        else
         failed(e, "Unexpected exception occurred.");
     }

      deleteVldl(vldlName_);

  }



/**
use nonexistent user space to get certificates,
verify correct exceptionm is thrown.
**/
  public void variation38()
  {
      setVariation(38);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {

       as400certs = aVldl.getCertificates(
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

     deleteVldl(vldlName_);

  }




/**
use nonexistent user space to get first certificates,
verify correct exceptionm is thrown.
**/
  public void variation39()
  {
      setVariation(39);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {

       as400certs  = aVldl.getFirstCertificates(
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

     deleteVldl(vldlName_);

  }



/**
get certificates with minus firstCertificateToReturn input parm,
Verify correct exception msg.
**/
  public void variation40()
  {
      setVariation(40);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aVldl.getCertificates(
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

      deleteVldl(vldlName_);

  }



/**
get certificates with out of range firstCertificateToReturn input parm,
Verify correct exception msg.
**/
  public void variation41()
  {
      setVariation(41);

      genVldl(vldlName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aVldl.addCertificate(testcert_[9]);

       aVldl.listCertificates(
                       null,
                       userSpacePathName_
                       );

       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);

      deleteUserSpace(aUserSpace);

  }



/**
get certificates with out of range buffsize input parm,
Verify correct exception msg.
**/
  public void variation42()
  {
      setVariation(42);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);

  }



/**
get first certificates with out of range buffsize input parm,
Verify correct exception msg.
**/
  public void variation43()
  {
      setVariation(43);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aVldl.getFirstCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);

  }




/**
get next certificates with out of range buffsize input parm,
Verify correct exception msg.
**/
  public void variation44()
  {
      setVariation(44);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aVldl.getFirstCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);

  }



/**
Add several certificates to CERTTEST validation list,
get the certificates with get certificates, add more certificates,
get new certificates with get next certificates,
verify total number returned,
**/
  public void variation45()
  {
      setVariation(45);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);



      AS400Certificate[] as400certs = null;

      Vector        certVector = new Vector();

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aVldl.addCertificate(testcert_[0]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[4]);

       //list the certs
       int numcerts = aVldl.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs
       as400certs = aVldl.getCertificates(userSpacePathName_,
					    0,
					    8);

       if (as400certs.length != 5)
           failed("Get 5 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates did not compare.");


       aVldl.addCertificate(testcert_[5]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[10]);
       aVldl.addCertificate(testcert_[11]);
       aVldl.addCertificate(testcert_[12]);
       aVldl.addCertificate(testcert_[13]);
       aVldl.addCertificate(testcert_[14]);
       aVldl.addCertificate(testcert_[15]);

       //list the certs
       numcerts = aVldl.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs
       as400certs = aVldl.getCertificates(userSpacePathName_,
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
       as400certs = aVldl.getNextCertificates(8);

       if (as400certs.length < 4)
           failed("Get next certificates 1 failed.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 2
       as400certs = aVldl.getNextCertificates(8);

       if (as400certs.length < 1)
           failed("Get next certificates 2 failed.");

       // Gather certificates in a vector
       for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 3
       as400certs = aVldl.getNextCertificates(8);

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
       as400certs = aVldl.getCertificates(userSpacePathName_,
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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }



/**
Add several certificates to CERTTEST validation list,
get the certificates at specified offset with get certificates,
add more certificates,
get new certificates with get first/next certificates,
verify total number returned,
**/
  public void variation46()
  {
      setVariation(46);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);


      AS400Certificate[] as400certs = null;

      Vector        certVector = new Vector();

      try
      {
       aUserSpace.create(5, true, " ", (byte)0x00, "create UserSpace", "*ALL");
       aUserSpace.close();
       
       aVldl.addCertificate(testcert_[0]);
       aVldl.addCertificate(testcert_[1]);
       aVldl.addCertificate(testcert_[2]);
       aVldl.addCertificate(testcert_[3]);
       aVldl.addCertificate(testcert_[4]);

       //list the certs
       int numcerts = aVldl.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs at offset 1
       as400certs = aVldl.getCertificates(userSpacePathName_,
					    1,
					    8);

       if (as400certs.length != 4)
           failed("Get 4 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates 4 did not compare.");

       //get first certs
       as400certs = aVldl.getFirstCertificates(userSpacePathName_,
						 8);

       if (as400certs.length != 5)
           failed("Get first 5 certificates failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates first 5 did not compare.");


       //get the cert at offset 4
       as400certs = aVldl.getCertificates(userSpacePathName_,
					    4,
					    8);

       if (as400certs.length != 1)
           failed("Get 1 certificate failed.");

       if (false == certsCompare(as400certs, testcert_, 5))
           failed( "Certificates 1 did not compare.");

       //get null next certs
       as400certs = aVldl.getNextCertificates(8);

       if (as400certs != null)
           failed("Get next certificates 0 failed.");

       aVldl.addCertificate(testcert_[5]);
       aVldl.addCertificate(testcert_[6]);
       aVldl.addCertificate(testcert_[7]);
       aVldl.addCertificate(testcert_[8]);
       aVldl.addCertificate(testcert_[9]);
       aVldl.addCertificate(testcert_[10]);
       aVldl.addCertificate(testcert_[11]);
       aVldl.addCertificate(testcert_[12]);
       aVldl.addCertificate(testcert_[13]);
       aVldl.addCertificate(testcert_[14]);
       aVldl.addCertificate(testcert_[15]);

       //list the certs
       numcerts = aVldl.listCertificates(
                                null,
                                userSpacePathName_
                                );

       //get the certs
       as400certs = aVldl.getFirstCertificates(userSpacePathName_,
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
       as400certs = aVldl.getNextCertificates(8);

       if (as400certs.length < 4)
           failed("Get next certificates 1 failed.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 2
       as400certs = aVldl.getNextCertificates(8);

       if (as400certs.length < 1)
           failed("Get next certificates 2 failed.");

       // Gather certificates in a vector
         for (int i = 0; i < as400certs.length; ++i)
         {
          certVector.addElement(as400certs[i]);
         }

       //get next certs, iteration 3
       as400certs = aVldl.getNextCertificates(8);

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

      deleteVldl(vldlName_);
      deleteUserSpace(aUserSpace);

  }



/**
get next certificates with out setting user space name or firstCertificateToReturn,
Verify correct exception msg.
**/
  public void variation47()
  {
      setVariation(47);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, vldlPathName_);

      AS400Certificate[] as400certs = null;

      try
      {
       as400certs = aVldl.getNextCertificates(8);

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

      deleteVldl(vldlName_);

  }

}






