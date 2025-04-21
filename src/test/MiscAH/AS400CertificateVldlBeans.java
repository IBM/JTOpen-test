///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400CertificateVldlBeans.java
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
import com.ibm.as400.access.AS400CertificateVldlUtil;
import com.ibm.as400.access.AS400CertificateEvent;
import com.ibm.as400.access.AS400Certificate;
import com.ibm.as400.access.AS400CertificateListener;
import test.AS400CertificateTest;
import test.JTOpenTestEnvironment;
import test.Testcase;

/**
 * Testcase AS400CertificateVldlBeans.
 **/
public class AS400CertificateVldlBeans extends Testcase implements PropertyChangeListener, VetoableChangeListener, AS400CertificateListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400CertificateVldlBeans";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.AS400CertificateTest.main(newArgs); 
   }
    String goodVldlName = "/QSYS.LIB/CERTTEST.LIB/VLDLCERT.VLDL";
    String vldlName_ = "CERTTEST/VLDLCERT";
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
    
    byte[][] testcert_ = null; // Array of X.509 test certificates

    /**
     * Constructor.  This is called from AS400CertificateTest::createTestcases().
     **/
    public AS400CertificateVldlBeans(AS400 systemObject, Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream)
    {
	super(systemObject, "AS400CertificateVldlBeans", 23, variationsToRun, runMode, fileOutputStream);

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
    if (JTOpenTestEnvironment.isWindows)
    {
      DOS_ = true;
    }

    // Are we running on the AS/400?
    else if (operatingSystem_.indexOf("OS/400") >= 0)
    {
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
  {

    // Create a user profile and library/userspace with no authority to.
    // CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

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
       fis = AS400CertificateVldlBeans.class.getResourceAsStream("cert" + i + ".cert");
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
      }


  }

/**
  Cleanup objects that have been created on the AS400.
 @exception  Exception  If an exception occurs.
**/
  protected void cleanup()
    throws Exception
  {
     // CommandCall cmd = new CommandCall(AS400CertificateTest.PwrSys);

     try {

	 AS400CertificateTest.PwrSys.disconnectAllServices();
     }
     catch(Exception e)
     {
      System.out.println("Cleanup failed.");
      System.out.println(e);
      throw e;
     }
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

 
/**
Add a certificate to CERTTEST user profile, verify add event.
**/
  public void Var001()
  {
      setVariation(1);

      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, goodVldlName);

      resetValues();

      try {

      aVldl.addAS400CertificateListener(this);

      //add certificate
      aVldl.addCertificate(testcert_[0]);

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

      if (!(aVldl.checkCertificate(testcert_[0])) )
	  failed("Certificate not added correctly.");


      }
      
      catch(Exception e)
      {
	  failed(e, "Unexpected exception occurred.");

      }

      deleteVldl(vldlName_);
  
}

    
    /**
     * Do successful AS400Certificate::delete certificate
     * Verify the user space is deleted correctly.
     **/
    public void Var002()
    {

      setVariation(2);

      
      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, goodVldlName);

      try {

      //add certificate
      aVldl.addCertificate(testcert_[0]);

      if (!(aVldl.checkCertificate(testcert_[0])) )
	  failed("Certificate not added correctly.");

      resetValues();

      aVldl.addAS400CertificateListener(this);
      
      //delete certificate
      aVldl.deleteCertificate(testcert_[0]);

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

   }
  catch(Exception e)
  {
      failed(e, "Unexpected exception occurred.");

  }

  deleteVldl(vldlName_);

}

    /**
     * Do successful AS400CertificateVldlUtil:: add and delete calls
     * Verify the certificate is added and deleted correctly.
     **/
    public void Var003()
    {

      setVariation(3);

     
      genVldl(vldlName_);

      AS400CertificateVldlUtil aVldl = new AS400CertificateVldlUtil(systemObject_, goodVldlName);

      resetValues();

      aVldl.addAS400CertificateListener(this);

      try {

      //add certificate
      aVldl.addCertificate(testcert_[0]);

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

      if (!(aVldl.checkCertificate(testcert_[0])) )
	  failed("Certificate not added correctly.");

      //delete certificate
      aVldl.deleteCertificate(testcert_[0]);

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

      //add certificate
      aVldl.addCertificate(testcert_[0]);

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
      aVldl.deleteCertificate(testcert_[0]);

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

  deleteVldl(vldlName_);
	

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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
	    resetValues();
	    us.addPropertyChangeListener(this);
	    String newPath = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    us.setPath(newPath);
		// Verify event
	    if (verifyPropChange("path", goodVldlName, newPath, us, us.getPath()))
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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
	    resetValues();
	    us.addVetoableChangeListener(this);
	    String newPath = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    us.setPath(newPath);
	    // Verify event
	    if (verifyVetoChange("path", goodVldlName, newPath, us, us.getPath()))
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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
	    resetValues();
	    String newPath = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    us.addVetoableChangeListener(this);
	    veto_ = true;
	    try
	    {
		us.setPath(newPath);
	    }
	    catch(Exception e)
	    {
	        // Verify event
		if (verifyVetoChange("path", goodVldlName, newPath, us, us.getPath()))
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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
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
	    AS400CertificateVldlUtil us = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
	    us.addAS400CertificateListener(this);

	    // Serialize us to a file.
	    FileOutputStream f = new FileOutputStream("us.ser");
	    ObjectOutput s =  new ObjectOutputStream(f);
	    s.writeObject(us);
	    s.flush();
	    s.close(); 
	    ObjectInputStream s2 = null; 
	    try
	    {
	        // Deserialize us from a file.
		FileInputStream in = new FileInputStream("us.ser");
		s2 = new ObjectInputStream(in);
		AS400CertificateVldlUtil us2 = (AS400CertificateVldlUtil)s2.readObject();
		
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
		s.close(); 
		if (s2 != null) s2.close(); 
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
	    String path = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(systemObject_, path);
	    resetValues();
	    aAS400CertificateVldlUtil.setPath(goodVldlName);
	    aAS400CertificateVldlUtil.addPropertyChangeListener(this);
	    String newUSName = "/QSYS.LIB/CERTTEST.LIB/CERTTEST3.VLDL";
	    aAS400CertificateVldlUtil.setPath(newUSName);
	    // Verify event
	    if (verifyPropChange("path", goodVldlName, newUSName, aAS400CertificateVldlUtil, aAS400CertificateVldlUtil.getPath()))
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
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(as400, goodVldlName);
	    resetValues();
	    AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1".toCharArray());
	    aAS400CertificateVldlUtil.setSystem(oldAS400);
	    aAS400CertificateVldlUtil.addPropertyChangeListener(this);
	    AS400 newAS400 = systemObject_;
	    aAS400CertificateVldlUtil.setSystem(newAS400);
	    // Verify event
	    if (verifyPropChange("system", oldAS400, newAS400, aAS400CertificateVldlUtil, aAS400CertificateVldlUtil.getSystem()))
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
	    String path = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(systemObject_, path);
	    resetValues();
	    aAS400CertificateVldlUtil.setPath(goodVldlName);
	    aAS400CertificateVldlUtil.addVetoableChangeListener(this);
	    String newUSName = "/QSYS.LIB/CERTTEST.LIB/CERTTEST3.VLDL";
	    aAS400CertificateVldlUtil.setPath(newUSName);
	    // Verify event
	    if (verifyVetoChange("path", goodVldlName, newUSName, aAS400CertificateVldlUtil, aAS400CertificateVldlUtil.getPath()))
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
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(as400, goodVldlName);
	    resetValues();
	    AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1".toCharArray());
	    aAS400CertificateVldlUtil.setSystem(oldAS400);
	    aAS400CertificateVldlUtil.addVetoableChangeListener(this);
	    AS400 newAS400 = systemObject_;
	    aAS400CertificateVldlUtil.setSystem(newAS400);
	    // Verify event
	    if (verifyVetoChange("system", oldAS400, newAS400, aAS400CertificateVldlUtil, aAS400CertificateVldlUtil.getSystem()))
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
	    String path = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(systemObject_, path);
	    resetValues();
	    aAS400CertificateVldlUtil.setPath(goodVldlName);
	    veto_ = true;
	    aAS400CertificateVldlUtil.addVetoableChangeListener(this);
	    String newUSName = "/QSYS.LIB/CERTTEST.LIB/CERTTEST2.VLDL";
	    try
	    {
	       aAS400CertificateVldlUtil.setPath(newUSName);
	    }
        catch(Exception e)
        {
	       // Verify event
	       if (verifyVetoChange("path", goodVldlName, newUSName, aAS400CertificateVldlUtil, aAS400CertificateVldlUtil.getPath()))
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
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(as400, goodVldlName);
	    resetValues();
	    AS400 oldAS400 = new AS400("rchasxxx", "JAVA", "JTEAM1".toCharArray());
	    aAS400CertificateVldlUtil.setSystem(oldAS400);
	    veto_ = true;
	    aAS400CertificateVldlUtil.addVetoableChangeListener(this);
	    AS400 newAS400 = systemObject_;

	    try
	    {
	       aAS400CertificateVldlUtil.setSystem(newAS400);
	    }
	    catch(Exception e)
	    {
	       // Verify event
	       if (verifyVetoChange("system", oldAS400, newAS400, aAS400CertificateVldlUtil, aAS400CertificateVldlUtil.getSystem()))
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
	    AS400 badSystem = new AS400("rchasxxx", "JAVA", "JTEAM1".toCharArray());
	    AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(badSystem, goodVldlName);
	    String newUSName = "/QSYS.LIB/CERTTEST.LIB/CERTTEST1.VLDL";
	    aAS400CertificateVldlUtil.setPath(newUSName);
	    aAS400CertificateVldlUtil.setSystem(systemObject_);
	    aAS400CertificateVldlUtil.addPropertyChangeListener(this);

	    // Serialize a AS400CertificateVldlUtil to a file.
	    FileOutputStream f = new FileOutputStream("uspace.ser");
	    ObjectOutput s =  new ObjectOutputStream(f);
	    s.writeObject(aAS400CertificateVldlUtil);
	    s.flush();
	    ObjectInputStream s2 = null; 
	    try
	    {
	        // Deserialize a AS400CertificateVldlUtil from a file.
		FileInputStream in = new FileInputStream("uspace.ser");
		s2 = new ObjectInputStream(in);
		AS400CertificateVldlUtil aAS400CertificateVldlUtil2 = (AS400CertificateVldlUtil)s2.readObject();

		if (false == aAS400CertificateVldlUtil2.getPath().equals(aAS400CertificateVldlUtil.getPath()))
		{
		    failed("Path changed to " + aAS400CertificateVldlUtil2.getPath());
		    return;
		}
		else if (false == aAS400CertificateVldlUtil2.getSystem().getSystemName().equals(aAS400CertificateVldlUtil.getSystem().getSystemName()))
		{
		    failed("System changed to " + aAS400CertificateVldlUtil2.getSystem());
		    return;
		}
		else if (false == aAS400CertificateVldlUtil2.getSystem().getUserId().equals(aAS400CertificateVldlUtil.getSystem().getUserId()))
		{
		    failed("System changed to " + aAS400CertificateVldlUtil2.getSystem());
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
		s.close(); 
		if (s2 != null) s2.close(); 
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
	
       AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(systemObject_, goodVldlName);
       aAS400CertificateVldlUtil.addAS400CertificateListener(this);

       resetValues();
       
       byte[] inBuffer = { (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda };

       genVldl(vldlName_);
      
       try
       {
	   aAS400CertificateVldlUtil.addCertificate(inBuffer);
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

       deleteVldl(vldlName_);
    }

    /**
     * Attempt to delete an invalid certificate and make sure an exception is returned
     * and event not fired.
     **/
    public void Var019()
    {

	
	setVariation(19);
	
       AS400CertificateVldlUtil aAS400CertificateVldlUtil = new AS400CertificateVldlUtil();
       aAS400CertificateVldlUtil.addAS400CertificateListener(this);

       byte[] inBuffer = { (byte)0x31, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x32, (byte)0x42, (byte)0xae, (byte)0xda,
                          (byte)0x33, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x34, (byte)0x42, (byte)0xfe, (byte)0xda,
                          (byte)0x36, (byte)0x42, (byte)0xbe, (byte)0xda,
                          (byte)0x37, (byte)0x42, (byte)0xce, (byte)0xda };

	  resetValues();

      try
       {
	   aAS400CertificateVldlUtil.deleteCertificate(inBuffer);
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
     * Ensure that AS400CertificateVldlUtil will correctly serialize and deserialize itself.
     * Verify that system name and path name are preserved.  Verify that
     * listeners aren't preserved.
     **/
    public void Var020()
    {

	setVariation(20);

	AS400CertificateVldlUtil aAS400CertificateVldlUtil = null;
	try
	{
	    aAS400CertificateVldlUtil = new AS400CertificateVldlUtil(systemObject_, goodVldlName);

	    genVldl(vldlName_);

	    aAS400CertificateVldlUtil.addAS400CertificateListener(this);
	    aAS400CertificateVldlUtil.addVetoableChangeListener(this);
	    aAS400CertificateVldlUtil.addPropertyChangeListener(this);

	  //add certificate
	    aAS400CertificateVldlUtil.addCertificate(testcert_[0]);

	  // serialize
	    FileOutputStream fos = new FileOutputStream("us.ser");
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(aAS400CertificateVldlUtil);
	    fos.close();

	    try
	    {
	  // deserialize
		FileInputStream fis = new FileInputStream("us.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		AS400CertificateVldlUtil aUS = (AS400CertificateVldlUtil) ois.readObject();
		fis.close();

		String systemName1 = aAS400CertificateVldlUtil.getSystem().getSystemName();
		String systemName2 = aUS.getSystem().getSystemName();
		String systemUserId1 = aAS400CertificateVldlUtil.getSystem().getUserId();
		String systemUserId2 = aUS.getSystem().getUserId();
		String pathName1 = aAS400CertificateVldlUtil.getPath();
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
		deleteVldl(vldlName_);
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception occurred.");
	}
    }
}
