///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400CertificateUsrPrfBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400CertificateUserProfileUtil;
import com.ibm.as400.access.AS400CertificateEvent;
import com.ibm.as400.access.AS400Certificate;
import com.ibm.as400.access.AS400CertificateListener;
import com.ibm.as400.access.ObjectEvent;
import com.ibm.as400.access.ObjectListener;

import test.AS400CertificateTest;
import test.Testcase;

/**
 * Testcase AS400CertificateUsrPrfBeans.
 **/
public class AS400CertificateUsrPrfBeans extends Testcase implements PropertyChangeListener, VetoableChangeListener, AS400CertificateListener
{
    String goodUsrPrfName = "/QSYS.LIB/CERTTEST.USRPRF";
    String userProfileName_ = "CERTTEST";
    String propertyName;
    Object oldValue;
    Object newValue;
    Object source;
    boolean veto_ = false;
    String propName;
    Object oValue;
    Object nValue;
    Object src;
    Object asource;
    PropertyChangeEvent propChange;
    PropertyChangeEvent vetoChange;
    PropertyChangeEvent vetoRefire;
    AS400CertificateEvent usEvent;
    int usN = -1;

    private String operatingSystem_;
    private boolean DOS_ = false;
    private boolean OS2_ = false;
    private boolean OS400_ = false;
    private boolean usingNativeImpl = false;
   
    byte[][] testcert_ = null; // Array of X.509 test certificates

    /**
     * Constructor.  This is called from AS400CertificateTest::createTestcases().
     **/
    public AS400CertificateUsrPrfBeans(AS400 systemObject, Vector variationsToRun, int runMode, FileOutputStream fileOutputStream)
    {
	super(systemObject, "AS400CertificateUsrPrfBeans", 23, variationsToRun, runMode, fileOutputStream);

    }

    /**
     * Runs the variations requested.
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

	if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED) // Note: This is an unattended variation.
	{
	    setVariation(1);
	    Var001();
	}
	if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
	{
	    setVariation(2);
	    Var002();
	}
	if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
	{
	    setVariation(3);
	    Var003();
	}
	if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
	{
	    setVariation(4);
	    Var004();
	}
	if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
	{
	    setVariation(5);
	    Var005();
	}
	if ((allVariations || variationsToRun_.contains("6")) && runMode_ != ATTENDED)
	{
	    setVariation(6);
	    Var006();
	}
	if ((allVariations || variationsToRun_.contains("7")) && runMode_ != ATTENDED)
	{
	    setVariation(7);
	    Var007();
	}
	if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED)
	{
	    setVariation(8);
	    Var008();
	}
	if ((allVariations || variationsToRun_.contains("9")) && runMode_ != ATTENDED)
	{
	    setVariation(9);
	    Var009();
	}
	if ((allVariations || variationsToRun_.contains("10")) && runMode_ != ATTENDED)
	{
	    setVariation(10);
	    Var010();
	}
	if ((allVariations || variationsToRun_.contains("11")) && runMode_ != ATTENDED)
	{
	    setVariation(11);
	    Var011();
	}
	if ((allVariations || variationsToRun_.contains("12")) && runMode_ != ATTENDED)
	{
	    setVariation(12);
	    Var012();
	}
	if ((allVariations || variationsToRun_.contains("13")) && runMode_ != ATTENDED)
	{
	    setVariation(13);
	    Var013();
	}
	if ((allVariations || variationsToRun_.contains("14")) && runMode_ != ATTENDED)
	{
	    setVariation(14);
	    Var014();
	}
	if ((allVariations || variationsToRun_.contains("15")) && runMode_ != ATTENDED)
	{
	    setVariation(15);
	    Var015();
	}
	if ((allVariations || variationsToRun_.contains("16")) && runMode_ != ATTENDED)
	{
	    setVariation(16);
	    Var016();
	}

	if ((allVariations || variationsToRun_.contains("17")) && runMode_ != ATTENDED)
	{
	    setVariation(17);
	    Var017();
	}

	if ((allVariations || variationsToRun_.contains("18")) && runMode_ != ATTENDED)
	{
	    setVariation(18);
	    Var018();
	}
	if ((allVariations || variationsToRun_.contains("19")) && runMode_ != ATTENDED)
	{
	    setVariation(19);
	    Var019();
	}
	if ((allVariations || variationsToRun_.contains("20")) && runMode_ != ATTENDED)
	{
	    setVariation(20);
	    Var020();
	}

        try
        {
	  cleanup();
	}
	catch (Exception e){}

    }

    void resetValues()
    {
	veto_ = false;
	propChange = null;
	vetoChange = null;
	vetoRefire = null;

	propertyName = null;
	oldValue = null;
	newValue = null;
	source = null;
	propName = null;
	oValue = null;
	nValue = null;
	src = null;
	asource = null;

	usEvent = null;
	usN = -1;
    }

    public void propertyChange(PropertyChangeEvent e)
    {
	if (propChange != null)
	{
	    output_.println("propertyChange refired!");
	}
	propChange = e;
	propertyName = e.getPropertyName();
	oldValue = e.getOldValue();
	newValue = e.getNewValue();
	source = e.getSource();
    }

    /**
    @exception  PropertyVetoException  If an exception occurs.
     **/
    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
    {
	if (vetoChange != null)
	{
	    if (vetoRefire != null)
	    {
		output_.println("vetoableChange refired!");
	    }
	    else
	    {
		vetoRefire = e;
	    }
	}
	else
	{
	    vetoChange = e;
	}
	propName = e.getPropertyName();
	oValue = e.getOldValue();
	nValue = e.getNewValue();
	src = e.getSource();

	if (veto_)
	{
	    throw new PropertyVetoException("Property vetoed", e);
	}
    }

    public void deleted(AS400CertificateEvent e)
    {
	if (usEvent != null)
	{
	    output_.println("deleted refired!");
	}
	usEvent = e;
	usN = 1;
	asource = e.getSource();
    }



    public void added(AS400CertificateEvent event)
    {
	if (usEvent != null)
	{
	    output_.println("added refired!");
	}
	usEvent = event;
	usN = 0;
	asource = event.getSource();
    }

    /**
     @exception  Exception  If an exception occurs.
     **/
  protected void setup()
    throws Exception
  {
    // Determine operating system we're running under
    operatingSystem_ = System.getProperty("os.name");
    if (operatingSystem_.indexOf("Windows") >= 0 ||
        operatingSystem_.indexOf("DOS") >= 0 ||
        operatingSystem_.indexOf("OS/2") >= 0)
    {
      DOS_ = true;
    }

    // Are we in OS/2? If so, need different commands for deleting stuff...
    if (operatingSystem_.indexOf("OS/2") >= 0)
    {
      OS2_ = true;
    }

    // Are we running on the AS/400?
    else if (operatingSystem_.indexOf("OS/400") >= 0)
    {
      OS400_ = true;
      usingNativeImpl = true;
      output_.println("Will use native implementation");
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("DOS-based file structure: " + DOS_);
    output_.println("Executing " + (isApplet_ ? "applet." : "application."));

    testInit();
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

    // Create a user profile and library/userspace with no authority to.
    CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

    try
    {
       if (cmd.run("CHGUSRPRF USRPRF(" + systemObject_.getUserId() + ") SPCAUT(*SECADM)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }
    }
    catch (Exception e) {}



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
       fis = AS400CertificateUsrPrfBeans.class.getResourceAsStream("cert" + i + ".cert");
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
    

  //auth to generated usrprf is also granted to currently running usrprf
  void genUsrPrf(String aUserPrf)
  {
     CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     String clcmd;

    try
    {
     clcmd = "CRTUSRPRF USRPRF(" + aUserPrf + ")  PASSWORD(JTEAM1)  TEXT('dennis  schroepfer 3-3073')";

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


  void deleteUsrPrf(String aUserPrf)
  {
      CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

      String clcmd;

      clcmd = "DLTUSRPRF USRPRF(" + aUserPrf + ") OWNOBJOPT(*DLT)";

      AS400Message[] messageList = null;

    try
    {
     if(cmd.run(clcmd) == false)
     {
           messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
        }

    }
    catch(Exception e)
    {
	if  (!(messageList[0].getID().equals("CPF2204")))  
        failed(e);
    }
  }  
  

  


/**
Add a certificate to CERTTEST user profile, verify add event.
**/
  public void Var001()
  {
      setVariation(1);

      deleteUsrPrf(userProfileName_);
      
      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);

      resetValues();

      try {

      aUsrprf.addAS400CertificateListener(this);

      //add certificate
      aUsrprf.addCertificate(testcert_[0]);

      try
      {
		// Verify event
	  if (usEvent == null)
	  {
	      failed("event not fired on create");
	  }
	  else if (usN != 0)
	  {
	      failed("added not fired, instead " + usN);
	  }
	  else
	  {
	      succeeded();
	  }
      }
      finally
      {
	  resetValues();

      }

      if (!userProfileName_.equals(aUsrprf.findCertificateUser(testcert_[0])) )
	  failed("Certificate not added correctly.");


      }
      
      catch(Exception e)
      {
	  failed(e, "Unexpected exception occurred.");

      }

      deleteUsrPrf(userProfileName_);
  
}

    
    /**
     * Do successful AS400Certificate::delete certificate
     * Verify the user space is deleted correctly.
     **/
    public void Var002()
    {

      setVariation(2);

      
      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);

      try {

      //add certificate
      aUsrprf.addCertificate(testcert_[0]);

      if (!userProfileName_.equals(aUsrprf.findCertificateUser(testcert_[0])) )
	  failed("Certificate not added correctly.");

      resetValues();

      aUsrprf.addAS400CertificateListener(this);
      
      //delete certificate
      aUsrprf.deleteCertificate(testcert_[0]);

      try
      {
		// Verify event
	  if (usEvent == null)
	  {
	      failed("event not fired on create");
	  }
	  else if (usN != 1)
	  {
	      failed("delete not fired, instead " + usN);
	  }
	  else
	  {
	      succeeded();
	  }
      }
      finally
      {
	  resetValues();

      }

      try {
          aUsrprf.findCertificateUser(testcert_[0]);
          failed("Certificate added to wrong user.");
      }
      catch(Exception e) {}

    }
  catch(Exception e)
  {
      failed(e, "Unexpected exception occurred.");

  }

  deleteUsrPrf(userProfileName_);

}

    /**
     * Do successful AS400CertificateUserProfileUtil:: add and delete calls
     * Verify the certificate is added and deleted correctly.
     **/
    public void Var003()
    {

      setVariation(3);

     
      genUsrPrf(userProfileName_);

      AS400CertificateUserProfileUtil aUsrprf = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);

      resetValues();

      aUsrprf.addAS400CertificateListener(this);

      try {

      //add certificate
      aUsrprf.addCertificate(testcert_[0]);

      try
      {
		// Verify event
	  if (usEvent == null)
	  {
	      failed("event not fired on create");
	  }
	  else if (usN != 0)
	  {
	      failed("added not fired, instead " + usN);
	  }
	  else
	  {
	      succeeded();
	      
	  }
      }
      finally
      {
	  resetValues();

      }

      if (!userProfileName_.equals(aUsrprf.findCertificateUser(testcert_[0])) )
	  failed("Certificate not added correctly.");

      //delete certificate
      aUsrprf.deleteCertificate(testcert_[0]);

      try
      {
		// Verify event
	  if (usEvent == null)
	  {
	      failed("event not fired on create");
	  }
	  else if (usN != 1)
	  {
	      failed("added not fired, instead " + usN);
	  }
	  else
	  {
	      succeeded();
	  }
      }
      finally
      {
	  resetValues();

      }

      try {
          aUsrprf.findCertificateUser(testcert_[0]);
          failed("Certificate added to wrong user.");
      }
      catch(Exception e) {}

      

      //add certificate
      aUsrprf.addCertificate(testcert_[0]);

      try
      {
		// Verify event
	  if (usEvent == null)
	  {
	      failed("event not fired on create");
	  }
	  else if (usN != 0)
	  {
	      failed("added not fired, instead " + usN);
	  }
	  else
	  {
	      succeeded();
	  }
      }
      finally
      {
	  resetValues();

      }


      //delete certificate
      aUsrprf.deleteCertificate(testcert_[0]);

      try
      {
		// Verify event
	  if (usEvent == null)
	  {
	      failed("event not fired on create");
	  }
	  else if (usN != 1)
	  {
	      failed("added not fired, instead " + usN);
	  }
	  else
	  {
	      succeeded();
	  }
      }
      finally
      {
	  resetValues();

      }



  }
  catch(Exception e)
  {
      failed(e, "Unexpected exception occurred.");

  }

  deleteUsrPrf(userProfileName_);
	

}



    /**

     * PROPERTY CHANGE TESTING
     **/
    public boolean baseVerifyPropChange(String prop, Object oldV, Object newV, Object sourceV )
    {
	if (null == propChange)
	{
	    failed("propertyChange not fired for " + prop);
	}
	else if (null == propChange.getPropertyName())
	{
	    failed("propertyName is null");
	}
	else if (null == oldV)
	{
	    failed("old Value is null for " + prop);
	}
	else if (null == newV)
	{
	    failed("new Value is null for " + prop);
	}
	else if (null == sourceV)
	{
	    failed("source Value is null for " + prop);
	}
	else if (!propChange.getPropertyName().equals(prop))
	{
	    failed("propertyName " + prop + ", expected " + propChange.getPropertyName());
	}
	else if (! oldV.equals(propChange.getOldValue()))
	{
	    failed("old value " + oldV + ", expected " + propChange.getOldValue());
	}
	else if (!newV.equals(propChange.getNewValue()))
	{
	    failed("new value " + newV + ", expected " + propChange.getNewValue());
	}
	else if (!sourceV.equals(propChange.getSource()))
	{
	    failed("source " + sourceV + ", expected " + propChange.getSource());
	}
	else
	{
	    return true;
	}
	return false;
    }

    public boolean verifyPropChange(String prop, Object oldV, Object newV, Object sourceV, Object curV)
    {
	if (true == baseVerifyPropChange(prop, oldV, newV, sourceV))
	{
	    if (!newV.equals(curV))
	    {
		failed("changed value " + curV + ", expected " + newV);
	    }
	    else
	    {
		return true;
	    }
	}
	return false;
    }

    // verify for byte property change
    public boolean verifyPropChange(String prop, byte oldV, byte newV, Object sourceV, byte curV)
    {
        byte[] old_V = { oldV };
        byte[] new_V = { newV };
        byte[] cur_V = { curV };

        if(verifyPropChange(prop, (Object)old_V, (Object)new_V, sourceV, (Object)cur_V))
           return true;
        else
           return false;
    }

    // verify for boolean property change
    public boolean verifyPropChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
        if(verifyPropChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
           return true;
        else
           return false;
	}

    /**
     * setPath
     **/
    public void Var004()
    {

	setVariation(4);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    resetValues();
	    us.addPropertyChangeListener(this);
	    String newPath = "/QSYS.LIB/CERTTEST1.USRPRF";
	    us.setPath(newPath);
		// Verify event
	    if (verifyPropChange("path", goodUsrPrfName, newPath, us, us.getPath()))
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setSystem
     **/
    public void Var005()
    {
	setVariation(5);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    resetValues();
	    us.addPropertyChangeListener(this);
	    AS400 newsys = new AS400();
	    us.setSystem(newsys);
	    // Verify event
	    if (verifyPropChange("system", systemObject_, newsys, us, us.getSystem()) == true)
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * VETOABLE CHANGE TESTING
     **/
    public boolean verifyVetoChange(String prop, Object oldV, Object newV, Object sourceV, Object curV)
    {
	if (vetoChange == null)
	{
	    failed("no veto change event");
	    return false;
	}
	else if (propChange != null)
	{
	    failed("property change fired as well as veto");
	    return false;
	}
	propChange = vetoChange;
	if (true == baseVerifyPropChange(prop, oldV, newV, sourceV))
	{
	    Object checkV = (veto_ ? oldV : newV);
	    if (!checkV.equals(curV))
	    {
		failed("changed value " + curV + ", expected " + checkV);
	    }
	    else
	    {
		return true;
	    }
	}
	return false;
    }

    // verify veto changes for a byte
    public boolean verifyVetoChange(String prop, byte oldV, byte newV, Object sourceV, byte curV)
    {
       byte[] old_V = { oldV };
       byte[] new_V = { newV };
       byte[] cur_V = { curV };

	   if (true == verifyVetoChange(prop, (Object)old_V, (Object)new_V, sourceV, (Object)cur_V))
          return true;
       else
	      return false;
    }

    // verify veto changes for a boolean
    public boolean verifyVetoChange(String prop, Boolean oldV, Boolean newV, Object sourceV, Boolean curV)
    {
	   if (true == verifyVetoChange(prop, (Object)oldV, (Object)newV, sourceV, (Object)curV))
          return true;
	   else
	      return false;
    }

    // don't veto

    /**
     * setPath
     **/
    public void Var006()
    {
	 setVariation(6);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    resetValues();
	    us.addVetoableChangeListener(this);
	    String newPath = "/QSYS.LIB/CERTTEST1.USRPRF";
	    us.setPath(newPath);
	    // Verify event
	    if (verifyVetoChange("path", goodUsrPrfName, newPath, us, us.getPath()))
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setSystem
     **/
    public void Var007()
    {
	 setVariation(7);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    resetValues();
	    us.addVetoableChangeListener(this);
	    AS400 newsys = new AS400();
	    us.setSystem(newsys);
	    // Verify event
	    if (verifyVetoChange("system", systemObject_, newsys, us, us.getSystem()) == true)
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    // veto

    /**
     * setPath
     **/
    public void Var008()
    {
	 setVariation(8);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    resetValues();
	    String newPath = "/QSYS.LIB/CERTTEST1.USRPRF";
	    us.addVetoableChangeListener(this);
	    veto_ = true;
	    try
	    {
		us.setPath(newPath);
	    }
	    catch(Exception e)
	    {
	        // Verify event
		if (verifyVetoChange("path", goodUsrPrfName, newPath, us, us.getPath()))
		{
		    succeeded();
		}
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setSystem
     **/
    public void Var009()
    {

	 setVariation(9);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    resetValues();
	    AS400 newsys = new AS400();
	    us.addVetoableChangeListener(this);
	    veto_ = true;
	    try
	    {
		us.setSystem(newsys);
	    }
	    catch(Exception e)
	    {
	        // Verify event
		if (verifyVetoChange("system", systemObject_, newsys, us, us.getSystem()) == true)
		{
		    succeeded();
		}
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * Test serialization
     **/
    public void Var010()
    {

	 setVariation(10);
	
	try
	{
	    AS400CertificateUserProfileUtil us = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
	    us.addAS400CertificateListener(this);

	    // Serialize us to a file.
	    FileOutputStream f = new FileOutputStream("us.ser");
	    ObjectOutput s =  new ObjectOutputStream(f);
	    s.writeObject(us);
	    s.flush();
	    try
	    {
	        // Deserialize us from a file.
		FileInputStream in = new FileInputStream("us.ser");
		ObjectInputStream s2 = new ObjectInputStream(in);
		AS400CertificateUserProfileUtil us2 = (AS400CertificateUserProfileUtil)s2.readObject();

		if (false == us2.getPath().equals(us.getPath()))
		{
		    failed("Path changed to " + us2.getPath());
		    return;
		}
		else if (false == us2.getSystem().getSystemName().equals(us.getSystem().getSystemName()))
		{
		    failed("system changed to " + us2.getSystem().getSystemName());
		    return;
		}
		else if (false == us2.getSystem().getUserId().equals( us.getSystem().getUserId()))
		{
		    failed("user changed to " + us2.getSystem().getUserId());
		    return;
		}
		else
		{
		    succeeded();
		}
	    }
	    finally
	    {
		File fd = new File("us.ser");
		fd.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setPath
     **/
    public void Var011()
    {

	 setVariation(11);
	
	try
	{
	    String path = "/QSYS.LIB/CERTTEST1.USRPRF";
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(systemObject_, path);
	    resetValues();
	    aAS400CertificateUserProfileUtil.setPath(goodUsrPrfName);
	    aAS400CertificateUserProfileUtil.addPropertyChangeListener(this);
	    String newUSName = "/QSYS.LIB/CERTTEST3.USRPRF";
	    aAS400CertificateUserProfileUtil.setPath(newUSName);
	    // Verify event
	    if (verifyPropChange("path", goodUsrPrfName, newUSName, aAS400CertificateUserProfileUtil, aAS400CertificateUserProfileUtil.getPath()))
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setSystem
     **/
    public void Var012()
    {

	 setVariation(12);
	
	try
	{
	    AS400 as400 = new AS400();
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(as400, goodUsrPrfName);
	    resetValues();
	    AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1");
	    aAS400CertificateUserProfileUtil.setSystem(oldAS400);
	    aAS400CertificateUserProfileUtil.addPropertyChangeListener(this);
	    AS400 newAS400 = systemObject_;
	    aAS400CertificateUserProfileUtil.setSystem(newAS400);
	    // Verify event
	    if (verifyPropChange("system", oldAS400, newAS400, aAS400CertificateUserProfileUtil, aAS400CertificateUserProfileUtil.getSystem()))
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    // User Space Attributes don't veto

    /**
     * setPath
     **/
    public void Var013()
    {

	
	setVariation(13);
	
	try
	{
	    String path = "/QSYS.LIB/CERTTEST1.USRPRF";
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(systemObject_, path);
	    resetValues();
	    aAS400CertificateUserProfileUtil.setPath(goodUsrPrfName);
	    aAS400CertificateUserProfileUtil.addVetoableChangeListener(this);
	    String newUSName = "/QSYS.LIB/CERTTEST3.USRPRF";
	    aAS400CertificateUserProfileUtil.setPath(newUSName);
	    // Verify event
	    if (verifyVetoChange("path", goodUsrPrfName, newUSName, aAS400CertificateUserProfileUtil, aAS400CertificateUserProfileUtil.getPath()))
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setSystem
     **/
    public void Var014()
    {

	setVariation(14);
	
	try
	{
	    AS400 as400 = new AS400();
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(as400, goodUsrPrfName);
	    resetValues();
	    AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1");
	    aAS400CertificateUserProfileUtil.setSystem(oldAS400);
	    aAS400CertificateUserProfileUtil.addVetoableChangeListener(this);
	    AS400 newAS400 = systemObject_;
	    aAS400CertificateUserProfileUtil.setSystem(newAS400);
	    // Verify event
	    if (verifyVetoChange("system", oldAS400, newAS400, aAS400CertificateUserProfileUtil, aAS400CertificateUserProfileUtil.getSystem()))
	    {
		succeeded();
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    // User Spaces Attributes do veto

    /**
     * setPath
     **/
    public void Var015()
    {


	setVariation(15);
	
	try
	{
	    String path = "/QSYS.LIB/CERTTEST1.USRPRF";
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(systemObject_, path);
	    resetValues();
	    aAS400CertificateUserProfileUtil.setPath(goodUsrPrfName);
	    veto_ = true;
	    aAS400CertificateUserProfileUtil.addVetoableChangeListener(this);
	    String newUSName = "/QSYS.LIB/CERTTEST2.USRPRF";

	    try
	    {
	       aAS400CertificateUserProfileUtil.setPath(newUSName);
	    }
        catch(Exception e)
        {
	       // Verify event
	       if (verifyVetoChange("path", goodUsrPrfName, newUSName, aAS400CertificateUserProfileUtil, aAS400CertificateUserProfileUtil.getPath()))
	       {
		      succeeded();
	       }
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * setSystem
     **/
    public void Var016()
    {


	
	setVariation(16);
	
	try
	{
	    AS400 as400 = new AS400();
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(as400, goodUsrPrfName);
	    resetValues();
	    AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1");
	    aAS400CertificateUserProfileUtil.setSystem(oldAS400);
	    veto_ = true;
	    aAS400CertificateUserProfileUtil.addVetoableChangeListener(this);
	    AS400 newAS400 = systemObject_;

	    try
	    {
	       aAS400CertificateUserProfileUtil.setSystem(newAS400);
	    }
	    catch(Exception e)
	    {
	       // Verify event
	       if (verifyVetoChange("system", oldAS400, newAS400, aAS400CertificateUserProfileUtil, aAS400CertificateUserProfileUtil.getSystem()))
	       {
	          succeeded();
	       }
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * Test serialization
     **/
    public void Var017()
    {

	
	setVariation(17);
	
	try
	{
	    AS400 badSystem = new AS400("rchasxxx", "JAVA", "JTEAM1");
	    AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(badSystem, goodUsrPrfName);
	    String newUSName = "/QSYS.LIB/CERTTEST1.USRPRF";
	    aAS400CertificateUserProfileUtil.setPath(newUSName);
	    aAS400CertificateUserProfileUtil.setSystem(systemObject_);
	    aAS400CertificateUserProfileUtil.addPropertyChangeListener(this);

	    // Serialize a AS400CertificateUserProfileUtil to a file.
	    FileOutputStream f = new FileOutputStream("uspace.ser");
	    ObjectOutput s =  new ObjectOutputStream(f);
	    s.writeObject(aAS400CertificateUserProfileUtil);
	    s.flush();
	    try
	    {
	        // Deserialize a AS400CertificateUserProfileUtil from a file.
		FileInputStream in = new FileInputStream("uspace.ser");
		ObjectInputStream s2 = new ObjectInputStream(in);
		AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil2 = (AS400CertificateUserProfileUtil)s2.readObject();

		if (false == aAS400CertificateUserProfileUtil2.getPath().equals(aAS400CertificateUserProfileUtil.getPath()))
		{
		    failed("Path changed to " + aAS400CertificateUserProfileUtil2.getPath());
		    return;
		}
		else if (false == aAS400CertificateUserProfileUtil2.getSystem().getSystemName().equals(aAS400CertificateUserProfileUtil.getSystem().getSystemName()))
		{
		    failed("System changed to " + aAS400CertificateUserProfileUtil2.getSystem());
		    return;
		}
		else if (false == aAS400CertificateUserProfileUtil2.getSystem().getUserId().equals(aAS400CertificateUserProfileUtil.getSystem().getUserId()))
		{
		    failed("System changed to " + aAS400CertificateUserProfileUtil2.getSystem());
		    return;
		}
		else
		{
		    succeeded();
		}
	    }
	    finally
	    {
		File fd = new File("uspace.ser");
		fd.delete();
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}
    }

    /**
     * Attempt to add invalid certificate and make sure an exception is returned
     * and event not fired.
     **/
    public void Var018()
    {


	
	setVariation(18);
	
       AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);
       aAS400CertificateUserProfileUtil.addAS400CertificateListener(this);

       resetValues();
       
       byte[] inBuffer = { (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda };

       genUsrPrf(userProfileName_);
      
       try
       {
	   aAS400CertificateUserProfileUtil.addCertificate(inBuffer);
	   failed("Unexpected results occurred.");
       }
       catch (Exception e)
       {
          if (exceptionIs(e, "ExtendedIllegalArgumentException"))
          {
             if (usEvent == null)
     		    succeeded();
     		 else
     		    failed("Unexpected event occurred.");
          }
          else
          {
             failed(e, "Unexpected exception occurred.");
          }
       }

       deleteUsrPrf(userProfileName_);
    }

    /**
     * Attempt to delete an invalid certificate and make sure an exception is returned
     * and event not fired.
     **/
    public void Var019()
    {

	
	setVariation(19);
	
       AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil();
       aAS400CertificateUserProfileUtil.addAS400CertificateListener(this);

       byte[] inBuffer = { (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda };

	  resetValues();

      try
       {
	   aAS400CertificateUserProfileUtil.deleteCertificate(inBuffer);
	   failed("Unexpected results occurred.");
       }
       catch (Exception e)
       {
          if (exceptionIs(e, "ExtendedIllegalStateException"))
          {
             if (usEvent == null)
     		    succeeded();
     		 else
     		    failed("Unexpected event occurred.");
          }
          else
          {
             failed(e, "Unexpected exception occurred.");
          }
       }


    }


    /**
     * Ensure that AS400CertificateUserProfileUtil will correctly serialize and deserialize itself.
     * Verify that system name and path name are preserved.  Verify that
     * listeners aren't preserved.
     **/
    public void Var020()
    {
	
	setVariation(20);
	
       AS400CertificateUserProfileUtil aAS400CertificateUserProfileUtil = null;
       try
       {
	   aAS400CertificateUserProfileUtil = new AS400CertificateUserProfileUtil(systemObject_, goodUsrPrfName);

	   genUsrPrf(userProfileName_);
         
          aAS400CertificateUserProfileUtil.addAS400CertificateListener(this);
          aAS400CertificateUserProfileUtil.addVetoableChangeListener(this);
	  aAS400CertificateUserProfileUtil.addPropertyChangeListener(this);

	  //add certificate
	  aAS400CertificateUserProfileUtil.addCertificate(testcert_[0]);

          // serialize
          FileOutputStream fos = new FileOutputStream("us.ser");
          ObjectOutputStream oos = new ObjectOutputStream(fos);
          oos.writeObject(aAS400CertificateUserProfileUtil);
          fos.close();

          try
          {
          // deserialize
          FileInputStream fis = new FileInputStream("us.ser");
          ObjectInputStream ois = new ObjectInputStream(fis);
          AS400CertificateUserProfileUtil aUS = (AS400CertificateUserProfileUtil) ois.readObject();
          fis.close();

          String systemName1 = aAS400CertificateUserProfileUtil.getSystem().getSystemName();
          String systemName2 = aUS.getSystem().getSystemName();
          String systemUserId1 = aAS400CertificateUserProfileUtil.getSystem().getUserId();
          String systemUserId2 = aUS.getSystem().getUserId();
          String pathName1 = aAS400CertificateUserProfileUtil.getPath();
          String pathName2 = aUS.getPath();

          if (false == systemName1.equals(systemName2))
          {
             failed("Unexpected results occurred - systemName.");
          }
          else if (false == systemUserId1.equals(systemUserId2))
          {
             failed("Unexpected results occurred - systemUserId.");
          }
          else if (false == pathName1.equals(pathName2))
          {
             failed("Unexpected results occurred - pathName.");
          }
          else
          {
             succeeded();
          }
          }
   	      finally
	      {
		     File fd = new File("us.ser");
		     fd.delete();
	      }
       }
       catch(Exception e)
       {
         failed(e, "Unexpected exception occurred.");
       }
    }

}
