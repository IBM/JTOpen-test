///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSUserSpaceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ReturnCodeException;
import com.ibm.as400.access.UserSpace;

import test.JTOpenTestEnvironment;
import test.Testcase;

import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
 *Testcase NLSUserSpaceTestcase.  This test class verifies the use of DBCS Strings
 *in selected UserSpace testcase variations.
**/
public class NLSUserSpaceTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSUserSpaceTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  private String userSpacePathName_ = "/QSYS.LIB/USTEST.LIB/USNLSTEST.USRSPC";
  private String operatingSystem_;
  private boolean DOS_;

  String dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String dbcs_string50 = getResource("IFS_DBCS_STRING50");

  /**
  Constructor.
  **/
  public NLSUserSpaceTestcase(AS400            systemObject,
                              Vector           variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream
                              )

  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSUserSpaceTestcase", 16,
          variationsToRun, runMode, fileOutputStream);
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
    else
    {
      DOS_ = false;
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("DOS-based file structure: " + DOS_);
    output_.println("Executing applet: " + isApplet_);

    try
    {
        CommandCall cmd = new CommandCall(systemObject_);
        if(cmd.run("CRTLIB LIB(USTEST)") == false)
        {
          AS400Message[] messageList = cmd.getMessageList();
          throw new Exception("Setup - CRTLIB USTEST - failed.  " + messageList[0].toString());
        }
    }
    catch(Exception e)
    {
        System.out.println("Setup failed.  Unexpected exception occurred." + e);
        throw e;
    }
  }

  /**
    Cleanup Test library.
   @exception  Exception  If an exception occurs.
  **/
  protected void cleanup()
    throws Exception
  {
     try
     {
        CommandCall cmd = new CommandCall(systemObject_);
	deleteLibrary(cmd,"USTEST");
     }
     catch(Exception e)
     {
        System.out.println("Cleanup failed. " + e);
        throw e;
     }
  }

  /**
    Delete a User Space
   @exception  Exception  If an exception occurs.
  **/
  public void deleteUserSpace(UserSpace aUserSpace)
    throws Exception
  {
     try
     {
        aUserSpace.delete();
     }
     catch(Exception e)
     {
       if (!exceptionIs(e, "IOException", "CPF2105 OBJECT USNLSTEST IN USTEST TYPE *USRSPC NOT FOUND."))
         throw new Exception("Cleanup failed- UserSpace could not be deleted. " + e);
     }
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.FILE);
    }
    catch(Exception e)
    {
      output_.println("Unable to connect to the AS/400");
      e.printStackTrace();
      return;
    }

    try
    {
      setup();
    }
    catch (Exception e)
    {
      output_.println("Setup failed.");
      return;
    }

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != UNATTENDED)
    {
      setVariation(2);
      Var002();
    }

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != UNATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }

    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }

    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      variation10();
    }

    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      variation11();
    }

    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      variation12();
    }

    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      variation13();
    }

    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      variation14();
    }

    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      variation15();
    }

    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      variation16();
    }

    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.FILE);
      cleanup();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }

/**
Method tested: create()
Ensure that the ExtendedIllegalArgumentException is thrown if the extendedAttribute parameter has more than 10 characters.
<i>Taken from:</i> UserSpaceCrtDltTestcase::Var08
**/
  public void Var001()
  {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    try {
      aUserSpace.create(11000, true, dbcs_string50, (byte)0x00, "NLSUSTEST", "*ALL");
      failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "extendedAttribute", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: create()
Verify that the extendedAttribute parameter is what is expected.
Note: This is an attended testcase.
**/
  public void Var002()
  {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    try {
      aUserSpace.create(11000, true, dbcs_string5, (byte)0x00, "NLSUSTEST", "*ALL");
      failed("No exception.");

//    if (systemObject_.isLocal())
//    {
//       System.out.println("Does the extendedAttribute: (" + dbcs_string5 + ") equal that of the user space in " + userSpacePathName_ + "(Y/N)? (WRKLIB)");
//       InputStreamReader r = new InputStreamReader(System.in);
//       BufferedReader inBuf = new BufferedReader(r);
//       String resp = inBuf.readLine();
//       if (!resp.equals("y") && !resp.equals("Y"))
//       {
//          failed("Verification of the User Space extendedAttribute failed.");
//       }
//       else
//       {
//          succeeded();
//       }
//    }
//    else
//    {
//       TestInstructions instructions = new TestInstructions("Does the extendedAttribute: (" + dbcs_string5 + ") equal that of the user space in \n " + userSpacePathName_ + "? \n Use the AS/400 command WRKLIB <library> to verify results.", 0xffffffff, "Test Instructions", TestInstructions.YES_NO);
//       int rc = instructions.display();
//       if (rc != 14)
//       {
//          failed("Verification of the User Space extendedAttribute failed.");
//       }
//       else
//       {
//          succeeded();
//       }
//    }
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "IOException"))
       {
          String message = e.getMessage();

          if (message.indexOf("CPF3C2B") > -1)
             if (message.indexOf(dbcs_string5) > -1)
                succeeded();
             else
                failed("could not find translated string in message");
          else
             failed("could not find CPF3C2B in message");

      //  if (e.getMessage().equalsIgnoreCase("CPF3C2B EXTENDED ATTRIBUTE " + dbcs_string5 + " IS NOT VALID."))
      //     succeeded();
      //  else
      //     failed(e, "Unexpected exception text.");
       }
       else
          failed(e, "Unexpected exception.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }







/**
Method tested: create()
Ensure that the ExtendedIllegalArgumentException is thrown if the textDescription parameter has more than 50 characters.
  <i>Taken from:</i> UserSpaceCrtDltTestcase::Var012
**/
public void Var003()
  {
    String text_string = dbcs_string50 + dbcs_string5 + dbcs_string50;
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    try {
      aUserSpace.create(11000, true, " ", (byte)0x00, text_string, "*ALL");
      failed("No exception.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "textDescription", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: create()
Verify that the textDescription parameter is what is expected.
This is an attended testcase.
**/
  public void Var004()
  {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    try {
      aUserSpace.create(11000, true, " ", (byte)0x00, dbcs_string50, "*ALL");

      if (systemObject_.isLocal())
      {
         System.out.println("Does the textDescription: (" + dbcs_string50 + ") equal that of the user space in " + userSpacePathName_ + "(Y/N)? (WRKLIB)");
         InputStreamReader r = new InputStreamReader(System.in);
         BufferedReader inBuf = new BufferedReader(r);
         String resp = inBuf.readLine();
         if (!resp.equals("y") && !resp.equals("Y"))
         {
            failed("Verification of the User Space textDescription failed.");
         }
         else
         {
            succeeded();
         }
      }
      else
      {
    	  if (true) { 
    		  throw new Exception("Testcase change needed"); 
    	  }
         // TestInstructions instructions = new TestInstructions("Does the textDescription: (" + dbcs_string50 + ") \n equal that of the user space in " + userSpacePathName_ + "? \n Use the AS/400 command WRKLIB <library> to verify results.", 0xffffffff, "Test Instructions", TestInstructions.YES_NO);
         // int rc = instructions.display();
         int rc = 0; 
         
         if (rc != 14)
         {
            failed("Verification of the User Space textDescription failed.");
         }
         else
         {
            succeeded();
         }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
      return;
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: create()
Ensure that the ExtendedIllegalArgumentException is thrown if the authority parameter has more than 10 characters.
  <i>Taken from:</i> UserSpaceCrtDltTestcase::Var015
**/
  public void Var005()
  {
    UserSpace aUserSpace = new UserSpace(systemObject_,
                                         userSpacePathName_);
    try {
      aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", dbcs_string50);
      failed("No exception.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "authority", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
  }
/**
Method tested: create()
Ensure that IOException is thrown if the authority parameter specified is invalid.
  <i>Taken from:</i> UserSpaceCrtDltTestcase::Var023
**/
  public void Var006()
  {
     UserSpace aUserSpace = null;
     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", dbcs_string10);

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       //int rc = ((ReturnCodeException) e).getReturnCode();
       //System.out.println("Return code is: " + rc);
       if (exceptionIs(e, "IOException"))
       {
          String message = e.getMessage();

          if (message.indexOf("CPF3C2D") > -1)
             if (message.indexOf(dbcs_string10) > -1)
                succeeded();
             else
                failed("could not find translated string in message");
          else
             failed("could not find CPF3C2D in message");

       // if (e.getMessage().equalsIgnoreCase("CPF3C2D VALUE " + dbcs_string10 + " FOR AUTHORITY PARAMETER IS NOT VALID."))
       //    succeeded();
       // else
       //    failed(e, "Unexpected exception text.");
       }
       else
         failed(e, "Unexpected exception.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }


/**
Method tested: create()
Ensure that the ExtendedIllegalArgumentException is thrown if the domain parameter has more than 10 characters.
  <i>Taken from:</i> UserSpaceCrtDltTestcase::Var026
**/
  public void Var007()
  {
    UserSpace aUserSpace = new UserSpace(systemObject_,
                                         userSpacePathName_);
    try {
      aUserSpace.create(dbcs_string50, 11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");
      failed("No exception.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "domain", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: create()
Ensure that IOException is thrown if the domain parameter specified is invalid.
  <i>Taken from:</i> UserSpaceCrtDltTestcase::Var027
**/
  public void Var008()
  {
     UserSpace aUserSpace = null;
     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        aUserSpace.create(dbcs_string10, 11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       //int rc = ((ReturnCodeException) e).getReturnCode();
       //System.out.println("Return code is: " + rc);
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
          succeeded();
       else
          failed(e, "Unexpected exception.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }



/**
Method tested: read(byte[], int)
Read and verify every byte of a user space containing all possible byte values.
  <i>Taken from:</i> UserSpaceReadTestcase::Var019
**/
  public void Var009()
  {
    byte[] data = dbcs_string50.getBytes();

    UserSpace aUserSpace = null;

    byte[] inBuffer1 = new byte[1];

    try
    {
       // create a user space and write data to it.
       aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USTEST.LIB/USREADCHK.USRSPC");
       aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");
       aUserSpace.write(data, 0);

       if (isApplet_)
       {
          int i=0;
          int i1;
          do
          {
             i1 = aUserSpace.read(inBuffer1, i);
          }
          while(i < data.length && inBuffer1[0] == data[i++]);

          if(i == data.length)
             succeeded();
          else
             failed("Unexpected results occurred.");

       }
       else
       {
          int i1;
          int i = 0;
          byte[] inByte1 = new byte[1];
          byte[] inByte2 = new byte[1];
          do
          {
             i1 = aUserSpace.read(inByte1, i);
          }
          while(i < data.length && inByte1[0] == data[i++]);

          if (i == data.length)
             succeeded();
          else
             failed("Unexpected results occurred.");
       }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: read(byte[], int, int, int)
Read and verify every byte of a user space containing all possible byte values.
  <i>Taken from:</i> UserSpaceReadTestcase::Var020
**/
  public void variation10()
  {
    byte[] data = dbcs_string10.getBytes();

    UserSpace aUserSpace = null;

    byte[] inBuffer1 = new byte[1];

    try
    {
       // create a user space and read write to it.
       aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USTEST.LIB/USREADCHK.USRSPC");
       aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");
       aUserSpace.write(data, 0, 0, data.length);

       if (isApplet_)
       {
          int i=0;
          int i1;
          do
          {
             i1 = aUserSpace.read(inBuffer1, i);
          }
          while(i < data.length && inBuffer1[0] == data[i++]);

          if(i == data.length)
             succeeded();
          else
             failed("Unexpected results occurred.");

       }
       else
       {
          int i1;
          int i = 0;
          byte[] inByte1 = new byte[1];
          byte[] inByte2 = new byte[1];
          do
          {
             i1 = aUserSpace.read(inByte1, i);
          }
          while(i < data.length && inByte1[0] == data[i++]);

          if (i == data.length)
             succeeded();
          else
             failed("Unexpected results occurred.");
       }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that the data read is stored at the specified offset in the byte array.
  <i>Taken from:</i> UserSpaceReadTestcase::Var021
**/
  public void variation11()
  {
    byte[] data = dbcs_string50.getBytes();

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try
    {
      aUserSpace.create(1000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");
      aUserSpace.write(data, 0);

      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };

      if (isApplet_)
      {
        aUserSpace.read(data1, 3, 4, 1);
      }
      else
      {
        aUserSpace.read(data1, 3, 4, 1);
      }
      if (data1[4] == data[3])
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }
/**
Method tested: read(int, int)
Ensure that the expected String is returned.
  <i>Taken from:</i> UserSpaceReadTestcase::Var031
**/
  public void variation12()
  {

    String readString = null;

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try
    {
      aUserSpace.create(1000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");
      aUserSpace.write(dbcs_string50, 10);

      if (isApplet_)
      {
        readString = aUserSpace.read(10, dbcs_string50.length());
      }
      else
      {
        readString = aUserSpace.read(10, dbcs_string50.length());
      }

      if (readString.equals(dbcs_string50))
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }
/**
Method tested: write(byte[], int)
Write every possible byte to a User Space.
  <i>Taken from:</i> UserSpaceWriteTestcase::Var034
**/
  public void variation13()
  {
     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");

        byte[] data = dbcs_string50.getBytes();

        aUserSpace.write(data, 0);

        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];

        do
        {
           int numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != data.length);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write.");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: write(byte[], int, int, int)
Write every possible byte to a User Space.
  <i>Taken from:</i> UserSpaceReadTestcase::Var035
**/
  public void variation14()
  {

     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");

        byte[] data = dbcs_string10.getBytes();

        aUserSpace.write(data, 0, 0, data.length);

        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];

        do
        {
           int numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != data.length);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write.");

    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: write(byte[], int, int, int)
Write every possible byte to a User Space.
  <i>Taken from:</i> UserSpaceReadTestcase::Var036
**/
  public void variation15()
  {
     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");

        byte[] data = dbcs_string50.getBytes();

        aUserSpace.write(data, 0, 0, data.length, 0);

        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];

        do
        {
           int numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != data.length);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write.");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

/**
Method tested: write(String, int)
Write every possible byte to a User Space.
  <i>Taken from:</i> UserSpaceReadTestcase::Var037
**/
  public void variation16()
  {

    UserSpace aUserSpace = null;
    try
    {
       aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
       aUserSpace.create(11000, true, " ", (byte)0x00, "NLSUSTEST", "*ALL");

       aUserSpace.write(dbcs_string50, 0);

       // verify string data was written to user space.
       String readString = aUserSpace.read(0, dbcs_string50.length());

       if (readString.equals(dbcs_string50))
          succeeded();
       else
          failed("String mismatch during write.");

    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      try {deleteUserSpace(aUserSpace);} catch(Exception e){}
    }
  }

}



