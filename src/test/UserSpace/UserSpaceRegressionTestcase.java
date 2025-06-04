///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceRegressionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////




package test.UserSpace;

import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.UserSpace;

import test.JTOpenTestEnvironment;
import test.Testcase;
import test.UserSpaceTest;

/**
Test write methods for UserSpace.
**/
public class UserSpaceRegressionTestcase extends Testcase
{
  private int maxUserSpaceSize_ = 16776704;
  private AS400 usSystem_;
  // private static String ifsDirName_ = "/";
  // private static String userSpaceName_ = "UserSpace";
  private static String userSpacePathName_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
  private String pre_existingUserSpace_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC";
  private String testAuth = UserSpaceTest.COLLECTION+"TA"; 
  private String unauthorizedUserSpace_ = "/QSYS.LIB/"+testAuth+".LIB/USWRITE3.USRSPC";
  private boolean setupUnauthorized_ = false;
  private String authLib = UserSpaceTest.COLLECTION+"AL"; 
  private String authorityUserSpace_ = "/QSYS.LIB/"+authLib+".LIB/USWRITE2.USRSPC";
  private boolean setupAuthority_ = false;
  private byte pre_existingByteValue_ = (byte)0x00;
  private int pre_existingLengthValue_ = 11000;
  private boolean DOS_ = false;
  private boolean usingNativeImpl = false;
  boolean failed;  // Keeps track of failure in multi-part tests.
  String msg;      // Keeps track of reason for failure in multi-part tests.

  boolean regression_   = false;
  boolean usingSockets_ = false;
  ///AS400   PwrSys_       = null;

  private String ustestUserID = null;

  private String collectorMessage_ = "";

  public void setRegressionTest(boolean regression)
  {
      regression_ = regression;
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {

    
 userSpacePathName_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
pre_existingUserSpace_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC";
 testAuth = UserSpaceTest.COLLECTION+"TA"; 
unauthorizedUserSpace_ = "/QSYS.LIB/"+testAuth+".LIB/USWRITE3.USRSPC";
 authLib = UserSpaceTest.COLLECTION+"AL"; 
 authorityUserSpace_ = "/QSYS.LIB/"+authLib+".LIB/USWRITE2.USRSPC";

    
    // Determine operating system we're running under
    if (JTOpenTestEnvironment.isWindows)
    {
      DOS_ = true;
    }


    // Are we running on the AS/400?
    if (JTOpenTestEnvironment.isOS400) { ; 
      if (usingSockets_)
      {
         usingNativeImpl = true;
         output_.println("Will use native implementation");
      }
    }
    output_.println("Running under: " + JTOpenTestEnvironment.osVersion);
    output_.println("DOS-based file structure: " + DOS_);
    output_.println("Executing " + ("application."));

    // Create AS/400 object necessary to run testcases.
    setupUSExisting();
  }

  /*
  Method: deleteUserSpace(UserSpace)
  Description: Cleanup user space on AS/400 that was used during testing.
  */
  void deleteUserSpace(UserSpace aUserSpace)
  {
    try
    {
      aUserSpace.delete();
    }
    catch(Exception e)
    {
        System.out.println("Cleanup failed - Delete UserSpace: " + e);
    }
  }

  /*
  Method: setupUSExisting()
  Description: Create a user space on the AS/400 to be used by testcases needing an existing user space.
  */
  private void setupUSExisting()
    throws Exception
  {
    try
    {
        // Create an AS400 object to be used with USTEST user profile.
       usSystem_ = new AS400(systemObject_);

       CommandCall cmd = new CommandCall(usSystem_);
       if (cmd.run("QSYS/CRTLIB LIB(USTEST)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

       // Create a user space to use test on an pre-existing user space.
       UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
       aUserSpace.setMustUseProgramCall(false);
       aUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "CRTDLT UserSpace", "*ALL");
       aUserSpace.close();
    }
    catch(Exception e)
    {
       System.out.println("Setup failed, could not create "+UserSpaceTest.COLLECTION+".LIB/preexist.usrspc");
       System.out.println(e);
       throw e;
    }
  }

  /*
  Method: setupUSUnauthorized()
  Description: Create a library and user space that testcases will have NO authority to.
  */
  private void setupUSUnauthorized()
  {
    if (pwrSys_ == null) {
      System.out.println("Setup failed, pwrSys_ not specified.");
      return;
    }
    CommandCall cmd = new CommandCall(pwrSys_);

    try
    {
       // Delete any existing setup objects.
	deleteLibrary(cmd,""+testAuth+"");
       cmd.run("QSYS/DLTAUTL AUTL(USAUTHLIST)");

       if (cmd.run("QSYS/CRTAUTL AUTL(USAUTHLIST) AUT(*EXCLUDE)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

       if (cmd.run("QSYS/CRTLIB LIB("+testAuth+") AUT(USAUTHLIST)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }
    }
    catch (Exception e) {}

    try
    {
       UserSpace bUserSpace = new UserSpace(pwrSys_, unauthorizedUserSpace_);
       bUserSpace.setMustUseProgramCall(false);
       bUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "create UserSpace", "*ALL");
       bUserSpace.close();

       setupUnauthorized_ = true;
    }
    catch(Exception e)
    {
       System.out.println("Setup failed, could not create "+testAuth+".LIB/uswrite3.usrspc " + e);
    }
  }

  /*
  Method: setupUSAuthority()
  Description: Create a user space to be used in authority testcases.
  */
  private void setupUSAuthority()
  {
    CommandCall cmd = new CommandCall(pwrSys_);

    try
    {
	deleteLibrary(cmd,""+authLib+"");

       if (cmd.run("QSYS/CRTLIB LIB("+authLib+")") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println("Setup warning - " + messageList[0].toString());
       }

       UserSpace aUSpace = new UserSpace(pwrSys_, authorityUserSpace_);
       aUSpace.setMustUseProgramCall(false);
       aUSpace.create(11000, true, " ", (byte)0x00, "USWRITE test", "*ALL");
       aUSpace.close();

       setupAuthority_ = true;
    }
    catch(Exception e)
    {
       System.out.println("Setup failed." + e);
    }
  }

   /*
   Method: writeData()
   Description: Write data to the user space via the Collector APIs.
   */
   private boolean writeData()
   {
      boolean collectorWrite = false;

      ProgramCall showData = new ProgramCall(systemObject_);

      ProgramParameter[] dataParmList = new ProgramParameter[3];
      // output - type of resource (char10)
      byte[] resourceBytes = new byte[10];
      dataParmList[0] = new ProgramParameter(resourceBytes.length);

      // output - sequence number of collection (bin4)
      byte[] outBytes = new byte[4];
      dataParmList[1] = new ProgramParameter(outBytes.length);

      // input/output - error code (char*)
      byte[] errorInfo = new byte[32];
      dataParmList[2] = new ProgramParameter(errorInfo, 0);

      try {
         showData.setProgram("/QSYS.LIB/QPMLPFRD.PGM", dataParmList);
      }
      catch(Exception e) {}

      try {
      if (showData.run() != true)
      {
         AS400Message[] messageList = showData.getMessageList();
         for (int msg = 0; msg < messageList.length; msg++)
            collectorMessage_ = messageList[msg].toString();
      }
      else {
         // Get output
         AS400Text textConverter = new AS400Text(10, usSystem_.getCcsid(), usSystem_);
         @SuppressWarnings("unused")
        String resourceName = (String)textConverter.toObject(resourceBytes);

         collectorWrite = true;         // Write successful.
       }

      }catch(Exception ex)
      {
         System.out.println("PgmRun Failed: " + ex);
      }
      return collectorWrite;
   }

   /*
   Method: workCollector(boolean, String)
   Description: Start/Stop the work collector via AS/400 APIs.
   */
   public  void workCollector(boolean start, String spaceName)
   {
      ProgramCall collector = new ProgramCall(systemObject_);

      ProgramParameter[] collectorParmList = new ProgramParameter[6];
      // input parm - type of action to perform (char10)
      String action = (start) ? "*START":"*END";
      collectorParmList[0] = new ProgramParameter(setName(action, 10) );

      // input parm - type of resource (char10)
      collectorParmList[1] = new ProgramParameter(setName("*COMM", 10) );

      // input parm - time between collections (bin4)
      AS400Bin4  bin4Converter = new AS400Bin4();
      collectorParmList[2] = new ProgramParameter(bin4Converter.toBytes(240) );

      // input parm - User Space Name (char20)
      byte[] nameArray = setName(spaceName, 20);
      collectorParmList[3] = new ProgramParameter(nameArray);

      // output parm - 1st sequence number (bin4)
      byte[] outBytes = new byte[4];
      collectorParmList[4] = new ProgramParameter(outBytes.length);

      // input/output parm - error code (char*)
      byte[] errorInfo = new byte[32];
      collectorParmList[5] = new ProgramParameter(errorInfo, 0);

      try {
         collector.setProgram("/QSYS.LIB/QPMWKCOL.PGM", collectorParmList);
      }
      catch(Exception e) {}

      try {
         if (collector.run() != true)
         {
            AS400Message[] messageList = collector.getMessageList();
            for (int msg = 0; msg < messageList.length; msg++)
               System.out.println("PgmError: " + messageList[msg].toString());
         }
      }
      catch(Exception ex)
      {
         String message = (start) ? "started! ":"ended! ";
         System.out.println("PgmRun: Collector failed to " + message + ex);
      }
   }

   /*
   Method: setName(String, int)
   Description: Set the name of the user space for input via the AS/400 API.
   */
   public byte[] setName(String name, int size)
   {
       StringBuffer pathName = null;
       AS400Text converter = new AS400Text(size, usSystem_.getCcsid(), usSystem_);

       if (size == 10) {
         pathName = new StringBuffer("          ");
         pathName.insert(0, name);
       }
       else if (size == 20) {
         // Verify name is valid integrated file system path name.
         QSYSObjectPathName userSpacePath = null;
         try {
             userSpacePath = new QSYSObjectPathName(name);
          }
          catch(IllegalPathNameException e)   { }
          String library = userSpacePath.getLibraryName();
          String space = userSpacePath.getObjectName();

           pathName = new StringBuffer("                    ");
           pathName.insert(0, space);
           pathName.insert(10, library);
       }
           pathName.setLength(size);
           String newString = pathName.toString();
           return converter.toBytes(newString);
   }

/**
  Cleanup objects that have been created on the AS400.
 @exception  Exception  If an exception occurs.
**/
  protected void cleanup()
    throws Exception
  {
     CommandCall cmd = new CommandCall(pwrSys_);

     try {
	 deleteLibrary(cmd, ""+testAuth+"");
       if (cmd.run("QSYS/DLTAUTL AUTL(USAUTHLIST)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
       }
       pwrSys_.disconnectAllServices();
     }
     catch(Exception e)
     {
        System.out.println("Cleanup failed." + e);
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


    ustestUserID = " USER(" + systemObject_.getUserId() + ") ";


    if (runMode_ != ATTENDED)
    {
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
      if (allVariations || variationsToRun_.contains("2"))
        Var002();
      if (allVariations || variationsToRun_.contains("3"))
        Var003();
      if (allVariations || variationsToRun_.contains("4"))
        Var004();
      if (allVariations || variationsToRun_.contains("5"))
        Var005();
      if (allVariations || variationsToRun_.contains("6"))
        Var006();
      if (allVariations || variationsToRun_.contains("7"))
        Var007();
      if (allVariations || variationsToRun_.contains("8"))
        Var008();
      if (allVariations || variationsToRun_.contains("9"))
        Var009();
      if (allVariations || variationsToRun_.contains("10"))
        Var010();
      if (allVariations || variationsToRun_.contains("11"))
        Var011();
      if (allVariations || variationsToRun_.contains("12"))
        Var012();
      if (allVariations || variationsToRun_.contains("13"))
        Var013();
      if (allVariations || variationsToRun_.contains("14"))
        Var014();
      if (allVariations || variationsToRun_.contains("15"))
        Var015();
      if (allVariations || variationsToRun_.contains("16"))
        Var016();
      if (allVariations || variationsToRun_.contains("17"))
        Var017();
      if (allVariations || variationsToRun_.contains("18"))
        Var018();
      if (allVariations || variationsToRun_.contains("19"))
        Var019();
      if (allVariations || variationsToRun_.contains("20"))
        Var020();
      if (allVariations || variationsToRun_.contains("21"))
        Var021();
      if (allVariations || variationsToRun_.contains("22"))
        Var022();
      if (allVariations || variationsToRun_.contains("23"))
        Var023();
      if (allVariations || variationsToRun_.contains("24"))
        Var024();
      if (allVariations || variationsToRun_.contains("25"))
        Var025();
      if (allVariations || variationsToRun_.contains("26"))
        Var026();
      if (allVariations || variationsToRun_.contains("27"))
        Var027();
      if (allVariations || variationsToRun_.contains("28"))
        Var028();
      if (allVariations || variationsToRun_.contains("29"))
        Var029();
      if (allVariations || variationsToRun_.contains("30"))
        Var030();
      if (allVariations || variationsToRun_.contains("31"))
        Var031();
      if (allVariations || variationsToRun_.contains("32"))
        Var032();
      if (allVariations || variationsToRun_.contains("33"))
        Var033();
      if (allVariations || variationsToRun_.contains("34"))
        Var034();
      if (allVariations || variationsToRun_.contains("35"))
        Var035();
      if (allVariations || variationsToRun_.contains("36"))
        Var036();
      if (allVariations || variationsToRun_.contains("37"))
        Var037();
      if (allVariations || variationsToRun_.contains("38"))
        Var038();
      if (allVariations || variationsToRun_.contains("39"))
        Var039();
      if (allVariations || variationsToRun_.contains("40"))
        Var040();
      if (allVariations || variationsToRun_.contains("41"))
        Var041();
      if (allVariations || variationsToRun_.contains("42"))
        Var042();
      if (allVariations || variationsToRun_.contains("43"))
        Var043();
      if (allVariations || variationsToRun_.contains("44"))
        Var044();
      if (allVariations || variationsToRun_.contains("45"))
        Var045();
      if (allVariations || variationsToRun_.contains("46"))
        Var046();
      if (allVariations || variationsToRun_.contains("47"))
        Var047();
      if (allVariations || variationsToRun_.contains("48"))
        Var048();
      if (allVariations || variationsToRun_.contains("49"))
        Var049();
      if (allVariations || variationsToRun_.contains("50"))
        Var050();
      if (allVariations || variationsToRun_.contains("51"))
        Var051();
      if (allVariations || variationsToRun_.contains("52"))
        Var052();
      if (allVariations || variationsToRun_.contains("53"))
        Var053();
      if (allVariations || variationsToRun_.contains("54"))
        Var054();
      if (allVariations || variationsToRun_.contains("55"))
        Var055();
      if (allVariations || variationsToRun_.contains("56"))
        Var056();
      if (allVariations || variationsToRun_.contains("57"))
        Var057();
      if (allVariations || variationsToRun_.contains("58"))
        Var058();
      if (allVariations || variationsToRun_.contains("59"))
        Var059();
      if (allVariations || variationsToRun_.contains("60"))
        Var060();
      if (allVariations || variationsToRun_.contains("61"))
        Var061();
      if (allVariations || variationsToRun_.contains("62"))
        Var062();
      if (allVariations || variationsToRun_.contains("63"))
        Var063();
      if (allVariations || variationsToRun_.contains("64"))
        Var064();
      if (allVariations || variationsToRun_.contains("65"))
        Var065();
      if (allVariations || variationsToRun_.contains("66"))
        Var066();
      if (allVariations || variationsToRun_.contains("67"))
        Var067();
      if (allVariations || variationsToRun_.contains("68"))
        Var068();
      if (allVariations || variationsToRun_.contains("69"))
        Var069();
      if (allVariations || variationsToRun_.contains("70"))
        Var070();
      if (allVariations || variationsToRun_.contains("71"))
        Var071();
      if (allVariations || variationsToRun_.contains("72"))
        Var072();
      if (allVariations || variationsToRun_.contains("73"))
        Var073();
      if (allVariations || variationsToRun_.contains("74"))
        Var074();
      if (allVariations || variationsToRun_.contains("75"))
        Var075();
      if (allVariations || variationsToRun_.contains("76"))
        Var076();
      if (allVariations || variationsToRun_.contains("77"))
        Var077();
      if (allVariations || variationsToRun_.contains("78"))
        Var078();
      if (allVariations || variationsToRun_.contains("79"))
        Var079();
      if (allVariations || variationsToRun_.contains("80"))
        Var080();
      if (allVariations || variationsToRun_.contains("81"))
        Var081();
      if (allVariations || variationsToRun_.contains("82"))
        Var082();
      if (allVariations || variationsToRun_.contains("83"))
        Var083();
      if (allVariations || variationsToRun_.contains("84"))
        Var084();
      if (allVariations || variationsToRun_.contains("85"))
        Var085();
      if (allVariations || variationsToRun_.contains("86"))
        Var086();
      if (allVariations || variationsToRun_.contains("87"))
        Var087();
      if (allVariations || variationsToRun_.contains("88"))
        Var088();
      if (allVariations || variationsToRun_.contains("89"))
        Var089();
      if (allVariations || variationsToRun_.contains("90"))
        Var090();
      if (allVariations || variationsToRun_.contains("91"))
        Var091();
      if (allVariations || variationsToRun_.contains("92"))
        Var092();
      if (allVariations || variationsToRun_.contains("93"))
        Var093();
      if (allVariations || variationsToRun_.contains("94"))
        Var094();
      if (allVariations || variationsToRun_.contains("95"))
        Var095();
      if (allVariations || variationsToRun_.contains("96"))
        Var096();
      if (allVariations || variationsToRun_.contains("97"))
        Var097();
      if (allVariations || variationsToRun_.contains("98"))
        Var098();
      if (allVariations || variationsToRun_.contains("99"))
        Var099();
      if (allVariations || variationsToRun_.contains("100"))
        Var100();
      if (allVariations || variationsToRun_.contains("101"))
        Var101();
      if (allVariations || variationsToRun_.contains("102"))
        Var102();
      if (allVariations || variationsToRun_.contains("103"))
        Var103();
      if (allVariations || variationsToRun_.contains("104"))
        Var104();
      if (allVariations || variationsToRun_.contains("105"))
        Var105();
      if (allVariations || variationsToRun_.contains("106"))
        Var106();
      if (allVariations || variationsToRun_.contains("107"))
        Var107();
      if (allVariations || variationsToRun_.contains("108"))
        Var108();
      if (allVariations || variationsToRun_.contains("109"))
        Var109();
      if (allVariations || variationsToRun_.contains("110"))
        Var110();
      if (allVariations || variationsToRun_.contains("111"))
        Var111();
      if (allVariations || variationsToRun_.contains("112"))
        Var112();
      if (allVariations || variationsToRun_.contains("113"))
        Var113();
      if (allVariations || variationsToRun_.contains("114"))
        Var114();
      if (allVariations || variationsToRun_.contains("115"))
        Var115();
      if (allVariations || variationsToRun_.contains("116"))
        Var116();
      if (allVariations || variationsToRun_.contains("117"))
        Var117();
      if (allVariations || variationsToRun_.contains("118"))
        Var118();
      if (allVariations || variationsToRun_.contains("119"))
        Var119();
      if (allVariations || variationsToRun_.contains("120"))
        Var120();
      if (allVariations || variationsToRun_.contains("121"))
        Var121();
      if (allVariations || variationsToRun_.contains("122"))
        Var122();
      if (allVariations || variationsToRun_.contains("123"))
        Var123();
      if (allVariations || variationsToRun_.contains("124"))
        Var124();
      if (allVariations || variationsToRun_.contains("125"))
        Var125();

      try
      {
        cleanup();
      }
      catch (Exception e){}
    }
  }

/**
Method tested: write(byte[], int)
Ensure the NullPointerException is thrown if dataBuffer is null.
**/
  public void Var001()
  {
     setVariation(1);

     UserSpace aUserSpace = null;
     byte[] inBuffer = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.write(inBuffer, 0);

        failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "dataBuffer"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure the NullPointerException is thrown if dataBuffer is null.
**/
  public void Var002()
  {
     setVariation(2);

     UserSpace aUserSpace = null;
     byte[] inBuffer = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.write(inBuffer, 0, 0, 0);

        failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "dataBuffer"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");

     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure the NullPointerException is thrown if dataBuffer is null.
**/
  public void Var003()
  {
     setVariation(3);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     byte[] inBuffer = null;

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.write(inBuffer, 0, 0, 0, 0);

        failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "dataBuffer"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(String, int)
Ensure that NullPointerException is thrown if data is null.
**/
  public void Var004()
  {
     setVariation(4);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     String inputString = null;

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.write(inputString, 0);

        failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "data"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(String, int)
Ensure that ExtendedIllegalArgumentException is thrown if String has length 0.
**/
  public void Var005()
  {
     setVariation(5);

     UserSpace aUserSpace = null;
     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write("", 0);

        failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "data"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown if dataBuffer has length zero.
**/
  public void Var006()
  {
     setVariation(6);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        aUserSpace.write(new byte[0], 0);

        failed("Expected exception did not occur."+lengthBefore);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataBuffer"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataBuffer has length zero.
**/
  public void Var007()
  {
     setVariation(7);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        aUserSpace.write(new byte[0], 0, 0, 0);

        failed("Expected exception did not occur."+lengthBefore);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataBuffer"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataBuffer has length zero.
**/
  public void Var008()
  {
     setVariation(8);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        aUserSpace.write(new byte[0], 0, 0, 0, 0);

        failed("Expected exception did not occur."+lengthBefore);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataBuffer"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that ExtendedIOException is thrown during write attempt if
dataBuffer + userSpaceOffset is greater than maxUserSpaceSize.
**/
  public void Var009()
  {
     setVariation(9);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(16776704, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        byte[] dataBuffer = { 0, 1, 2, 3 };
        aUserSpace.write(dataBuffer, 16776702);

        failed("Expected exception did not occur."+lengthBefore);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIOException",
          "Request causes space overflow."/*, ExtendedIOException.REQUEST_NOT_SUPPORTED*/))
          succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that the user space auto extends if it writes up to length+1 if
dataBuffer has length greater than length.
**/
  public void Var010()
  {
     setVariation(10);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(3584, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.setAutoExtendible(true);
        int lengthBefore = aUserSpace.getLength();
        byte[] dataBuffer = { 9, 8, 7, 6, 5};
        aUserSpace.write(dataBuffer, 3583);
        int lengthAfter = aUserSpace.getLength();

        if (lengthAfter > lengthBefore)
           succeeded();
        else
           failed("Error: Unexpected write results.");
     }
     catch(Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure the ExtendedIllegalArgumentException is thrown if userSpaceOffset is < 0.
**/
  public void Var011()
  {
     setVariation(11);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        byte[] inputBuffer = new byte[10];
        for (int i=0; i<10; i++)
           inputBuffer[i] = 99;

        // Attempt to set offset to null
        aUserSpace.write(inputBuffer, -1);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "userSpaceOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is < 0.
**/
  public void Var012()
  {
     setVariation(12);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        byte[] inputBuffer = new byte[10];
        for (int i=0; i<10; i++)
           inputBuffer[i] = 99;

        aUserSpace.write(inputBuffer, -1 ,0, 10);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "userSpaceOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is < 0.
**/
  public void Var013()
  {
     setVariation(13);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        byte[] inputBuffer = new byte[10];
        for (int i=0; i<10; i++)
           inputBuffer[i] = 99;

        aUserSpace.write(inputBuffer, -1 ,0, 10, 0);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "userSpaceOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(String, int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is less than zero.
**/
  public void Var014()
  {
     setVariation(14);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        // Attempt to set offset to null
        String inString = "TEST_STRING";
        aUserSpace.write(inString, -1);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that IOException is thrown if userSpaceOffset is greater than
the length of the User Space.  (Impl.Remote - set auto extend is disregarded.)
**/
  public void Var015()
  {
     setVariation(15);
     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIOException is thrown if userSpaceOffset+length is greater than maxUserSpaceSize.
**/
  public void Var016()
  {
     setVariation(16);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        // Attempt to set offset to null
        byte[] writeBuffer = new byte[20];
        aUserSpace.write(writeBuffer, maxUserSpaceSize_ - 1, 0, writeBuffer.length);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIOException",
          "Request causes space overflow."/*, ExtendedIOException.REQUEST_NOT_SUPPORTED*/))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is < 0.
**/
  public void Var017()
  {
     setVariation(17);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write(new byte[10], 0, -1, 10);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is < 0.
**/
  public void Var018()
  {
     setVariation(18);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write(new byte[10], 0, -1, 10, 0);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that nothing is written is dataOffset is zero.
**/
  public void Var019()
  {
    setVariation(19);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        aUserSpace.write(new byte[1000], 0, 0, 1000, 0);
        int lengthAfter = aUserSpace.getLength();

        if (lengthBefore == lengthAfter)
           succeeded();
        else
           failed("Error: Unexpected write results.");
     }
     catch(Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is
the length of the dataBuffer.
**/
  public void Var020()
  {
     setVariation(20);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        writeBuffer[19] = 19;
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, writeBuffer.length, 0);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is greater than
the length of the data buffer.
**/
  public void Var021()
  {
     setVariation(21);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, writeBuffer.length + 1, writeBuffer.length, 0);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset"/*, ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID*/))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that nothing is written is length is zero.
**/
  public void Var022()
  {
     setVariation(22);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        aUserSpace.write(new byte[1000], 0, 0, 1000);
        int lengthAfter = aUserSpace.getLength();

        if (lengthBefore == lengthAfter)
           succeeded();
        else
           failed("Error: Unexpected write results.");
     }
     catch(Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if length < 0.
**/
  public void Var023()
  {
     setVariation(23);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write(new byte[10], 0, 0, -1);

        failed("Exception did not occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if length is < 0.
**/
  public void Var024()
  {
     setVariation(24);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write(new byte[10], 0, 0, -1, 0);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if the length parameter is greater than
the length of the data buffer.
**/
  public void Var025()
  {
     setVariation(25);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, 0, writeBuffer.length + 1);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(String, int)
Ensure that no exception is thrown if userSpaceOffset is greater than
the length of the User Space.
**/
  public void Var026()
  {
     setVariation(26);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.setAutoExtendible(true);

        int usLength = aUserSpace.getLength();

        aUserSpace.write("WRITE_TEST", usLength + 1);

        succeeded();
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }

     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is greater than
the length of the data buffer.
**/
  public void Var027()
  {
     setVariation(27);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, writeBuffer.length + 1, writeBuffer.length, 0);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if the length parameter is greater than
the length of the data buffer.
**/
  public void Var028()
  {
     setVariation(28);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, 0, writeBuffer.length + 1, 0);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if forceAuxiliary is < 0.
**/
  public void Var029()
  {
     setVariation(29);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write(new byte[10], 0, 0, 10, -1);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "forceAuxiliary", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if forceAuxiliary is > 2.
**/
  public void Var030()
  {
     setVariation(30);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        aUserSpace.write(new byte[10], 0, 0, 10, 3);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "forceAuxiliary", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, FORCE_NONE)
Ensure that changed are not forced.
**/
  public void Var031()
  {
     setVariation(31);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int FORCE_NONE = 0;
        byte[] data = new byte[10];
        for (int i=0; i< data.length; i++)
           data[i] = 99;

        aUserSpace.write(data, 0, 0, 10, FORCE_NONE);

        // Verify data was written with changes not forced.
        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];
        int numBytes = 0; 
        do
        {
           numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != data.length);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write."+numBytes);
     }
     catch(Exception e)
     {
        failed(e, "Exception occured.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, FORCE_ASYNCHRONOUSLY)
Ensure that changed are forced asynchronously.
**/
  public void Var032()
  {
     setVariation(32);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int FORCE_ASYNCHRONOUSLY = 1;
        byte[] data = new byte[10];
        for (int i=0; i< data.length; i++)
           data[i] = 88;

        aUserSpace.write(data, 0, 0, 10, FORCE_ASYNCHRONOUSLY);

        // Verify data was written with changes not forced.
        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];
        int numBytes; 
        do
        {
           numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != data.length);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write."+numBytes);
     }
     catch(Exception e)
     {
        failed(e, "Exception occured.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, FORCE_SYNCHRONOUSLY)
Ensure that changed are forced synchronously.
**/
  public void Var033()
  {
     setVariation(33);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int FORCE_SYNCHRONOUSLY = 2;
        byte[] data = new byte[10];
        for (int i=0; i< data.length; i++)
           data[i] = 77;

        aUserSpace.write(data, 0, 0, 10, FORCE_SYNCHRONOUSLY);

        // Verify data was written with changes not forced.
        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];
        int numBytes = 0; 
        do
        {
           numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != data.length);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write."+numBytes);
     }
     catch(Exception e)
     {
        failed(e, "Exception occured.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Write every possible byte to a User Space.
**/
  public void Var034()
  {
     setVariation(34);
     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "write UserSpace", "*ALL");

        byte[] data = new byte[256];
        for (int i=0; i < data.length; i++)
           data[i] = (byte) i;

        aUserSpace.write(data, 0);

        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];
        int numBytes = 0; 
        do
        {
           numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != 256);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write."+numBytes);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Write every possible byte to a User Space.
**/
  public void Var035()
  {
     setVariation(35);

     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] data = new byte[256];
        for (int i=0; i < data.length; i++)
           data[i] = (byte) i;

        aUserSpace.write(data, 0, 0, data.length);

        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];
        int numBytes = 0; 
        do
        {
           numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != 256);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write."+numBytes);

    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Write every possible byte to a User Space.
**/
  public void Var036()
  {
     setVariation(36);
     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] data = new byte[256];
        for (int i =0; i < data.length; i++)
           data[i] = (byte) i;

        aUserSpace.write(data, 0, 0, data.length, 0);

        int i = 0;
        int j = 0;
        byte[] inByte = new byte[1];
        int numBytes = 0; 

        do
        {
           numBytes = aUserSpace.read(inByte, i++);
        }
        while(inByte[0] == data[j++] && j != 256);

        if (i == data.length)
           succeeded();
        else
           failed("Byte mismatch during write."+numBytes);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(String, int)
Write every possible byte to a User Space.
**/
  public void Var037()
  {
    setVariation(37);

    UserSpace aUserSpace = null;
    try
    {
       aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
       aUserSpace.setMustUseProgramCall(false);
       aUserSpace.create(11000, true, " ", (byte)0x00, "write UserSpace", "*ALL");

       String unicode = "";
       for (int i=0; i < 0x80; i++)
          unicode += (char) i;

       // write string to the user space
       aUserSpace.write(unicode, 0);

       // determine expected byte array
       AS400Text outText = new AS400Text(unicode.length(), systemObject_.getCcsid(), systemObject_);
       byte[] outBuffer = outText.toBytes(unicode);

       // read from the user space
       byte[] inBuffer = new byte[unicode.length()];
       int inBytes = aUserSpace.read(inBuffer, 0);

       // verify the byte array contents
       int x=0;
       while ((x < inBytes) && (inBuffer[x] == outBuffer[x]))
          x++;

       if ( x == outBuffer.length )
          succeeded();
       else
          failed("Unexpected results occurred.");

    }
    catch(Exception e)
    {
      failed("Unexpected error occurred. " + e);
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that ExtendedIOException will be thrown if write attempts
to write past the maximum length of a user space.
**/
  public void Var038()
  {
    setVariation(38);
    UserSpace aUserSpace = null;

    try
    {
      String userSpacePathName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/READAPI.USRSPC";
      aUserSpace = new UserSpace(systemObject_, userSpacePathName);
      aUserSpace.setMustUseProgramCall(false);
      aUserSpace.create(16776704, true, " ", (byte)0x00, "create UserSpace", "*ALL");

      byte[] moreData = { 6, 7, 8, 9 };
      // attempt to write off end of max user space.
      aUserSpace.write(moreData, 16776701);
    } catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIOException", "Request causes space overflow.",
          ExtendedIOException.REQUEST_NOT_SUPPORTED))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Verify write is successful if a connection has not been made.
**/
  public void Var039()
  {
     setVariation(39);
     byte[] data = { 1, 2, 3, 4, 5 };
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "USWRITE test", "*ALL");
        systemObject_.disconnectService(AS400.COMMAND);

        aUserSpace.write(data, 0);

        succeeded();

     } catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure AS400SecurityException is thrown if the user does not have authority to the library.
**/
  public void Var040()
  {
     setVariation(40);

     // Create necessary AS/400 objects for testcase.
     if (!setupUnauthorized_)
        setupUSUnauthorized();

     try {

        UserSpace aUserSpace = new UserSpace(systemObject_, unauthorizedUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        byte[] data = new byte[20];
        aUserSpace.write(data, 0);

        failed("Expected exception did not occur (make sure -uid on command line does not have authority to "+testAuth+".LIB/uswrite3.usrspc).");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     systemObject_.disconnectAllServices();

  }

/**
Method tested: write(byte[], int)
Ensure AS400SecurityException is thrown if the user does not have authority to the User Space.
**/
  public void Var041()
  {
     setVariation(41);

     // Create needed AS/400 objects.
     if (!setupAuthority_)
        setupUSAuthority();

     CommandCall cmd = new CommandCall(pwrSys_);

     try
     {
        if(cmd.run("QSYS/GRTOBJAUT OBJ("+authLib+"/USWRITE2) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
        {
           AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
        }

        if (usingNativeImpl)
        {
           byte[] data = new byte[20];
           UserSpace aUserSpace = new UserSpace(systemObject_, authorityUserSpace_);
           aUserSpace.setMustUseProgramCall(false);
           aUserSpace.write(data, 0);
        }
        else
        {
           byte[] data = new byte[20];
           UserSpace aUserSpace = new UserSpace(usSystem_, authorityUserSpace_);
           aUserSpace.setMustUseProgramCall(false);
           aUserSpace.write(data, 0);
        }

        failed("Expected exception did not occur (make sure -uid does not have authority to write to "+authLib+".lib/uswrite2.usrspc).");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }

     try
     {
        UserSpace aUserSpace = new UserSpace(pwrSys_, authorityUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.delete();

	deleteLibrary(cmd, ""+authLib+"");

        {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println("Testcase cleanup failed. - " + messageList[0].toString());
        }
        pwrSys_.disconnectAllServices();
        usSystem_.disconnectAllServices();
        setupAuthority_ = false;
     }
     catch(Exception e)
     {
        output_.println("Cleanup failed.");
     }
  }

/**
Method tested: write(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown if the library does not exist.
**/
  public void Var042()
  {
     setVariation(42);

     try {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USWRITE3.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        byte[] dataBuffer = { 9, 8, 7, 6, 5 };
        aUserSpace.write(dataBuffer, 0);

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ObjectDoesNotExistException",
          "path", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: write(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown with CPF2105 if the user space does not exist.
**/
  public void Var043()
  {
     setVariation(43);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USBAD.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        byte[] dataBuffer = { 0 , 1, 2, 3, 4 };
        aUserSpace.write(dataBuffer, 0);

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ObjectDoesNotExistException",
          "path", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
  }
/**
Method tested: write(byte[], int, int, int)
Ensure that ExtededIOException is thrown if dataBuffer + userSpaceOffset is greater than maxUserSpaceSize.
**/
  public void Var044()
  {
     setVariation(44);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(16776704, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        byte[] dataBuffer = { 0, 1, 2, 3 };
        aUserSpace.write(dataBuffer, 16776702, 0, dataBuffer.length);

        failed("Expected exception did not occur."+lengthBefore);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIOException",
          "Request causes space overflow.", ExtendedIOException.REQUEST_NOT_SUPPORTED))
          succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that ExtendedIOException is thrown if dataBuffer + userSpaceOffset is greater than maxUserSpaceSize.
**/
  public void Var045()
  {
     setVariation(45);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(16776704, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        int lengthBefore = aUserSpace.getLength();
        byte[] dataBuffer = { 0, 1, 2, 3 };
        aUserSpace.write(dataBuffer, 16776702, 0, dataBuffer.length, 0);

        failed("Expected exception did not occur."+lengthBefore);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIOException",
          "Request causes space overflow.", ExtendedIOException.REQUEST_NOT_SUPPORTED))
          succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int)
Ensure that the User Space auto extends if it writes up to length+1 if
dataBuffer has length greater than length.
**/
  public void Var046()
  {
     setVariation(46);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(3584, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.setAutoExtendible(true);

        int lengthBefore = aUserSpace.getLength();
        byte[] dataBuffer = { 9, 8, 7, 6, 5};
        aUserSpace.write(dataBuffer, 3583, 0, dataBuffer.length);
        int lengthAfter = aUserSpace.getLength();

        if (lengthAfter > lengthBefore)
           succeeded();
        else
           failed("Error: Unexpected write results.");
     }
     catch(Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that the UserSpace  auto extends if it writes up to length+1 if
dataBuffer has length greater than length.
**/
  public void Var047()
  {
     setVariation(47);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

     try
     {
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(3584, true, " ", (byte)0x00, "create UserSpace", "*ALL");
        aUserSpace.setAutoExtendible(true);

        int lengthBefore = aUserSpace.getLength();
        byte[] dataBuffer = { 9, 8, 7, 6, 5};
        aUserSpace.write(dataBuffer, 3583, 0, dataBuffer.length, 0);
        int lengthAfter = aUserSpace.getLength();

        if (lengthAfter > lengthBefore)
           succeeded();
        else
           failed("Error: Unexpected write results.");
     }
     catch(Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }
/**
Method tested: write(byte[], int, int, int)
Ensure that no exception is thrown if userSpaceOffset is greater than
the length of the User Space.  (Impl.Remote - set auto extend is disregarded.)
**/
  public void Var048()
  {
     setVariation(48);

     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that no exception is thrown if userSpaceOffset is greater than
the length of the User Space.  (Impl.Remote - set auto extend is disregarded.)
**/
  public void Var049()
  {
     setVariation(49);
     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }
/**
Method tested: write(String, int)
Ensure that ExtendedIOException is thrown if userSpaceOffset + writeString.length is greater than maxUserSpaceSize.
**/
  public void Var050()
  {
     setVariation(50);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        // Attempt to set offset to null
        String writeString = "USWRITE test String";
        aUserSpace.write(writeString, maxUserSpaceSize_ - 1);

        failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIOException",
          "Request causes space overflow.", ExtendedIOException.REQUEST_NOT_SUPPORTED))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }
/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is
the length of the dataBuffer.
**/
  public void Var051()
  {
     setVariation(51);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        writeBuffer[19] = 19;
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, writeBuffer.length, 0, 0);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int, int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is greater than
the length of the data buffer.
**/
  public void Var052()
  {
     setVariation(52);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");

        byte[] writeBuffer = new byte[20];
        int usLength = aUserSpace.getLength();
        aUserSpace.write(writeBuffer, 0, writeBuffer.length + 1, writeBuffer.length);

        failed("No Exception occurred."+usLength);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: write(byte[], int)
Ensure that UserSpace.write will write to a user space being accessed by an AS/400 API.
**/
  public void Var053()              // $A1
  {
     setVariation(53);

     if (regression_)
     {
        succeeded();
        return;
     }

     String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, usName);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Test User Space", "*USE");

        aUserSpace.close();                     // close the user space.

        workCollector(true, usName);            // Start the performance collector.
        writeData();                            // Write the collection to the user space.
        workCollector(false, usName);           // Stop the performance collector.

        byte[] array = { 0, 1, 2, 3 };
        aUserSpace.write(array, 0);

        succeeded();
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {
        deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: write(byte[], int)
Ensure that a second UserSpace object can write to a open user space.(same AS400 object).
**/
  public void Var054()              // $A1
  {
     setVariation(54);
     String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
     UserSpace aUserSpace = null;
     UserSpace secondUS = null;

     try {
        aUserSpace = new UserSpace(systemObject_, usName);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Write User Space", "*USE");

        secondUS = new UserSpace(systemObject_, usName);
        secondUS.setMustUseProgramCall(false);

        byte[] dataBuffer = { 0 , 1, 2, 3, 4 };
        secondUS.write(dataBuffer, 0);

        succeeded();
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {
        try {
           secondUS.close();
        }catch(Exception e) { System.out.println("Testcase cleanup failed. " + e); }
        deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: write(byte[], int)
Ensure that a second UserSpace object can write to a user space after it is closed (different AS400 object).
**/
  public void Var055()              // @A1, @A2
  {
     setVariation(55);

     String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
     UserSpace aUserSpace = null;
     UserSpace secondUS = null;

     try {
        aUserSpace = new UserSpace(systemObject_, usName);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Write User Space", "*ALL");

        aUserSpace.close();         // close the user space.

        secondUS = new UserSpace(usSystem_, usName);
        secondUS.setMustUseProgramCall(false);

        byte[] dataBuffer = { 0 , 1, 2, 3, 4 };
        secondUS.write(dataBuffer, 0);

        succeeded();
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {
       try {
          secondUS.close();
       }catch(Exception e) { System.out.println("Cleanup failed - UserSpace.close: " + e);
       }
       deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: write(byte[], int)
Verify that an Exception is thrown if a second user space object trys to write to an open user space
(different AS400 object).
**/
  public void Var056()              // $A1
  {
     setVariation(56);

     String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(pwrSys_, usName);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Write User Space", "*USE");
        aUserSpace.close();

        UserSpace secondUS = new UserSpace(usSystem_, usName);
        secondUS.setMustUseProgramCall(false);

        byte[] dataBuffer = { 0 , 1, 2, 3, 4 };
        secondUS.write(dataBuffer, 0);

        failed("No Exception occurred.");
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     finally
     {
        deleteUserSpace(aUserSpace);
     }
  }

/**
Method tested: AS400 user space API write)
Verify that an Exception is thrown on an AS/400 user space API write if the user space object is NOT closed.
**/
  public void Var057()              // @A1, @A2
  {
     setVariation(57);

     if (regression_)
     {
        succeeded();
        return;
     }

     String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, usName);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Test User Space", "*USE");

        workCollector(true, usName);            // Start the performance collector.

        if (writeData()) {                      // Write the collection to the user space.
              succeeded();
        }
        else {
           if (collectorMessage_.equalsIgnoreCase("CPF0A45 Cannot copy data to user space USWRITE."))
              succeeded();
           else
              failed("Incorrect message received: " + collectorMessage_);
        }
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {
        workCollector(false, usName);           // Stop the performance collector.
        deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: AS400 user space API write)
Ensure that an AS/400 user space API write is successful if the user space object has been closed.
**/
  public void Var058()              // $A1
  {
     setVariation(58);

     if (regression_)
     {
        succeeded();
        return;
     }

     String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, usName);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Test User Space", "*USE");

        aUserSpace.close();                     // close the user space.

        workCollector(true, usName);            // Start the performance collector.
        writeData();                            // Write the collection to the user space.
        workCollector(false, usName);           // Stop the performance collector.

        succeeded();
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {
        deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: write(byte[], int)
Ensure that UserSpace.write is successful after the user space is closed (same AS400 object).
**/
  public void Var059()              // $A1
  {
     setVariation(59);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC");
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Write User Space", "*USE");

        aUserSpace.close();             // Close the user space.

        byte[] dataBuffer = { 0 , 1, 2, 3, 4, 5, 6, 7 };
        aUserSpace.write(dataBuffer, 0);

        succeeded();
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

















/**
Method tested: read(byte[], int)
Ensure that NullPointerException is thrown if dataBuffer is null.
**/
  public void Var060()
  {
    setVariation(60);

    try
    {
      UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
      aUserSpace.setMustUseProgramCall(false);
      byte[] inBuffer = null;
      int bytesReturned = aUserSpace.read(inBuffer, 0);

      failed("Expected exception did not occur"+bytesReturned);
    }
    catch(Exception e)
    {
      if (exceptionIs(e, "NullPointerException", "dataBuffer"))
         succeeded();
      else
         failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure the NullPointerException is thrown if dataBuffer is null.
**/
  public void Var061()
  {
     setVariation(61);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        byte[] inBuffer = null;
        int bytesReturned = aUserSpace.read(inBuffer, 0, 0, 0);

        failed("Expected exception did not occur."+bytesReturned);
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "NullPointerException", "dataBuffer"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown if the length of dataBuffer is zero.
**/
  public void Var062()
  {
    setVariation(62);
    try
    {
      UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
      aUserSpace.setMustUseProgramCall(false);
      int bytesReturned = aUserSpace.read(new byte[0], 0);

      failed("Expected exception did not occur."+bytesReturned);
    }
    catch(Exception e)
    {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataBuffer", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();

        else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is out of bounds
**/
  public void Var063()
  {
     setVariation(63);

     try
     {
        byte[] inBuffer = new byte[20];
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        int byteCount = aUserSpace.read(inBuffer, -1);
        assertCondition(false,"Exception not thrown byteCount="+byteCount);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "userSpaceOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();

        else
           failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if argument two is < 0.
**/
  public void Var064()
  {
    setVariation(64);

    try
    {
      UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
      aUserSpace.setMustUseProgramCall(false);
      int bytesReturned = aUserSpace.read(new byte[1], -1, 1, 1);

      failed("Expected exception did not occur."+bytesReturned);
    }
    catch(Exception e)
    {
      if(exceptionIs(e, "ExtendedIllegalArgumentException",
                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
         succeeded();
      else
         failed(e, "Unexpected exception occurred.");

    }
  }

/**
Method tested: read(int, int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is less than zero.
**/
  public void Var065()
  {
     setVariation(65);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        // Attempt to set offset to null
        String inString = aUserSpace.read(-1, 20);

        failed("Expected exception did not occur."+inString);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");

     }
  }

/**
Method tested: read(byte[], int)
Ensure -1 is returned at the end of user space.
**/
  public void Var066()
  {

    setVariation(66);
    UserSpace aUserSpace = null;
    byte[] inBuffer = new byte[10];
    try
    {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.setMustUseProgramCall(false);
      aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");

      if (aUserSpace.read(inBuffer, aUserSpace.getLength() + 1) == -1)
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is greater than
the length of the User Space.
**/
  public void Var067()
  {
     setVariation(67);

     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");

        byte[] dataBuffer = new byte[100];
        int usLength = aUserSpace.getLength();

        if (aUserSpace.read(dataBuffer, 2*usLength) == -1)
           succeeded();
        else
           failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);

  }

/**
Method tested: read(byte[], int, int, int)
Ensure that -1 is returned if userSpaceOffset+length is greater than maxUserSpaceSize.
**/
  public void Var068()
  {
     setVariation(68);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        // Attempt to set offset to null
        byte[] readBuffer = new byte[20];
        int bytesReturned = aUserSpace.read(readBuffer, maxUserSpaceSize_ - 1, 0, readBuffer.length);

        if (bytesReturned == -1)
           succeeded();
        else
           failed("Unexpected results occurred.");
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if argument three is < 0.
**/
  public void Var069()
  {
    setVariation(69);

    try
    {
      UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
      aUserSpace.setMustUseProgramCall(false);
      int bytesReturned = aUserSpace.read(new byte[1], 0, -1, 1);
      failed("Expected exception did not occur."+bytesReturned);
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that the read returns zero if the length is zero.
**/
  public void Var070()
  {
    setVariation(70);
    UserSpace aUserSpace = null;

    try
    {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.setMustUseProgramCall(false);
      aUserSpace.create(11000, true, " ", (byte)0x00, "USREAD test", "*ALL");

      if (aUserSpace.read(new byte[20], 0, 1, 0) == 0)
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(int, int)
Ensure that ExtendedIllegalArgumentException is thrown if length is 0.
**/
  public void Var071()
  {
     setVariation(71);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        String readString = aUserSpace.read(0, 0);

        failed("Expected exception did not occur."+readString);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is
the length of the dataBuffer.
**/
  public void Var072()
  {
     setVariation(72);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        byte[] readBuffer = new byte[20];
        readBuffer[19] = 19;
        int bytesReturned = aUserSpace.read(readBuffer, 0, readBuffer.length, 0);

        failed("Expected exception did not occur."+bytesReturned);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is greater than
the length of the data buffer.
**/
  public void Var073()
  {
     setVariation(73);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        byte[] readBuffer = new byte[20];
        int bytesReturned = aUserSpace.read(readBuffer, 0, readBuffer.length + 1, readBuffer.length);

        failed("Expected exception did not occur."+bytesReturned);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
           failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(int, int)
Ensure that ExtendedIllegalArgumentException is thrown if length is < 0.
**/
  public void Var074()
  {
     setVariation(74);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        String readString = aUserSpace.read(0, -1);

        failed("Expected exception did not occur."+readString);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if length is < 0.
**/
  public void Var075()
  {
     setVariation(75);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        int bytesReturned = aUserSpace.read(new byte[10], 0, 0, -1);

        failed("Expected exception did not occur."+bytesReturned);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if the length parameter is greater than
the length of the data buffer.
**/
  public void Var076()
  {
     setVariation(76);

     try
     {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);
        byte[] readBuffer = new byte[20];
        int bytesReturned = aUserSpace.read(readBuffer, 0, 0, readBuffer.length + 1);

        failed("Expected exception did not occur."+bytesReturned);
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(byte[], int)
Ensure that the read reads up to the end of the user space
successfully, returning the number of bytes read when the number of bytes
available to be read is less than the byte array length.
**/
  public void Var077()
  {
    setVariation(77);

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try
    {
      aUserSpace.create(3584, true, " ", (byte)0x00, "READ test", "*ALL");

      byte[] data = new byte[5000];
      int bytesRead = aUserSpace.read(data, 0);

      if(bytesRead < data.length)
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int)
Read and verify every byte of a user space containing all possible byte values.
**/
  public void Var078()
  {
    setVariation(78);
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;

    UserSpace aUserSpace = null;


    try
    {
       // create a user space and read data to it.
       aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USREADCHK.USRSPC");
       aUserSpace.setMustUseProgramCall(false);
       aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");
       aUserSpace.write(data, 0);

       int i1;
      int i = 0;
      byte[] inByte1 = new byte[1];
      
      do
      {
         i1 = aUserSpace.read(inByte1, i);
      }
      while(i < 256 && inByte1[0] == data[i++]);

      if (i == data.length)
         succeeded();
      else
         failed("Unexpected results occurred."+i1);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int, int, int)
Read and verify every byte of a user space containing all possible byte values.
**/
  public void Var079()
  {
    setVariation(79);
    byte[] data = new byte[256];
    for (int i = 0; i < data.length; i++)
      data[i] = (byte) i;

    UserSpace aUserSpace = null;


    try
    {
       // create a user space and read data to it.
       aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USREADCHK.USRSPC");
       aUserSpace.setMustUseProgramCall(false);
       aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");
       aUserSpace.write(data, 0, 0, data.length);

       int i1;
      int i = 0;
      byte[] inByte1 = new byte[1];
      do
      {
         i1 = aUserSpace.read(inByte1, i);
      }
      while(i < 256 && inByte1[0] == data[i++]);

      if (i == data.length)
         succeeded();
      else
         failed("Unexpected results occurred."+i1);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that the data read is stored at the specified offset in the byte array.
**/
  public void Var080()
  {
   setVariation(80);
    byte[] data = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try
    {
      aUserSpace.create(1000, true, " ", (byte)0x00, "USREAD test", "*ALL");
      aUserSpace.write(data, 0);

      byte[] data1 = { 0,1,2,3,4,5,6,7,8,9 };

      aUserSpace.read(data1, 3, 4, 1);
      if (data1[4] == data[3])
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that the read is successful up to the end of the user space, returning the
number of bytes read when the length is greater than the number of bytes available to be read.
**/
  public void Var081()
  {
    setVariation(81);
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try
    {
      aUserSpace.create(3584, true, " ", (byte)0x00, "USREAD test", "*ALL");

      byte[] data1 = new byte[512];
      int usLength = aUserSpace.getLength();
      int bytesRead1 = aUserSpace.read(data1, usLength - 128, 0, data1.length);

      if (bytesRead1 < data1.length)
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int)
Verify read is successful if a connection has not been made.
**/
  public void Var082()
  {
     setVariation(82);
     byte[] data = new byte[20];
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        int bytesReturned = aUserSpace.read(data, 0);
        if (bytesReturned > 0)
           succeeded();
        else
           failed("Unexpected results occurred.");

        aUserSpace.close();

     } catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }


     systemObject_.disconnectService(AS400.FILE);
  }

/**
Method tested: read(byte[], int)
Ensure AS400SecurityException is thrown if the user does not have authority to the library.
**/
  public void Var083()
  {
     setVariation(83);

     // Create AS400 objects used in testcase.
     if (!setupUnauthorized_)
        setupUSUnauthorized();

     try {

        UserSpace aUserSpace = new UserSpace(systemObject_, unauthorizedUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        byte[] data = new byte[20];
        int bytesReturned = aUserSpace.read(data, 0);

        failed("Expected exception did not occur (make sure -uid on command line does not have authority to "+testAuth+".LIB/usread2.usrspc)."+bytesReturned);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
     systemObject_.disconnectAllServices();

  }

/**
Method tested: read(byte[], int)
Ensure AS400SecurityException is thrown if the user does not have authority to the user space.
**/
  public void Var084()
  {
     setVariation(84);

     // Create AS400 objects used in testcase.
     if (!setupAuthority_)
        setupUSAuthority();

     try
     {
        CommandCall cmd = new CommandCall(pwrSys_);

        if(cmd.run("QSYS/GRTOBJAUT OBJ("+authLib+"/USWRITE2) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
        {
           AS400Message[] messageList = cmd.getMessageList();
           throw new IOException(messageList[0].toString());
        }
        int bytes; 
        if (usingNativeImpl)
        {
           byte[] data = new byte[20];
           UserSpace aUserSpace = new UserSpace(systemObject_, authorityUserSpace_);
           aUserSpace.setMustUseProgramCall(false);
           bytes = aUserSpace.read(data, 0);
        }
        else
        {
           byte[] data = new byte[20];
           UserSpace aUserSpace = new UserSpace(usSystem_, authorityUserSpace_);
           aUserSpace.setMustUseProgramCall(false);
           bytes = aUserSpace.read(data, 0);
        }

        failed("Expected exception did not occur (make sure -uid on command line does not have authority to "+authLib+".lib/usread1.usrspc)."+bytes);
     }
     catch(Exception e)
     {
        if (exceptionStartsWith(e, "AS400SecurityException", "",
           AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: read(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown if the library does not exist.
**/
  public void Var085()
  {
     setVariation(85);

     try {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USREAD2.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        byte[] dataBuffer = new byte[20];
        aUserSpace.read(dataBuffer, 0);

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ObjectDoesNotExistException",
          "path", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
          succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int)
Ensure that ExtendedIllegalArgumentException is thrown with CPF2105 if the User Space does not exist.
**/
  public void Var086()
  {
     setVariation(86);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USBAD.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        byte[] dataBuffer = new byte[20];
        int bytesReturned = aUserSpace.read(dataBuffer, 0);

        failed("Exception did not occur."+bytesReturned);
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ObjectDoesNotExistException",
          "path", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
          succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(int, int)
Ensure that the read is successful up to the end of the user
space returning a String with
length less than the String specified by the length parameter.
**/
  public void Var087()
  {
    setVariation(87);

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try
    {
      aUserSpace.create(3584, true, " ", (byte)0x00, "READ test", "*ALL");

      int expectedLength = 10;
      String readString = aUserSpace.read(3580, expectedLength);

      if(readString.length() == expectedLength)
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if the length of dataBuffer is zero.
**/
  public void Var088()
  {
    setVariation(88);
    try
    {
      UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
      aUserSpace.setMustUseProgramCall(false);
      int bytesReturned = aUserSpace.read(new byte[0], 0, 0, 0);

      failed("Expected exception did not occur."+bytesReturned);
    }
    catch(Exception e)
    {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "dataBuffer", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
           succeeded();

        else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: read(byte[], int, int, int)
Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is greater than
the length of the User Space.
**/
  public void Var089()
  {
     setVariation(89);

     UserSpace aUserSpace = null;

     try
     {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");

        byte[] dataBuffer = new byte[100];
        int usLength = aUserSpace.getLength();

        if (aUserSpace.read(dataBuffer, 2*usLength, 0, 80) == -1)
           succeeded();
        else
           failed("Expected exception did not occur.");
     }
     catch(Exception e)
     {
       if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);

  }


/**
Method tested: read(int, int)
Ensure that the expected String is returned.
**/
  public void Var090()
  {
   setVariation(90);

    String expectedString = "USTESTSTRING";
    String readString = null;

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try
    {
      aUserSpace.create(1000, true, " ", (byte)0x00, "USREAD test", "*ALL");
      aUserSpace.write(expectedString, 10);

      readString = aUserSpace.read(10,12);
      if (readString.equals(expectedString))
         succeeded();
      else
         failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

/**
Method tested: read(byte[], int)
Ensure that a second UserSpace object can read from a open user space.(same AS400 object).
**/
  public void Var091()              // $A1
  {
     setVariation(91);
     UserSpace aUserSpace = null;
     UserSpace secondUS = null;

     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*USE");

        secondUS = new UserSpace(systemObject_, userSpacePathName_);
        secondUS.setMustUseProgramCall(false);

        byte[] dataBuffer = new byte[16];
        int bytes = secondUS.read(dataBuffer, 0);

        assertCondition(true,"bytes = "+bytes); 
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {
        try {
           secondUS.close();
        }catch(Exception e) { System.out.println("Testcase cleanup failed. " + e); }
        deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: read(byte[], int)
Ensure that a second UserSpace object can read from a user space after it is closed (different AS400 object).
**/
  public void Var092()              // @A1  , @A2
  {
     setVariation(92);

     UserSpace aUserSpace = null;
     UserSpace secondUS = null;

     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*ALL");

        aUserSpace.close();         // close the user space.

        secondUS = new UserSpace(usSystem_, userSpacePathName_);
        secondUS.setMustUseProgramCall(false);

        byte[] dataBuffer = new byte[16];
        int bytes = secondUS.read(dataBuffer, 0);

        assertCondition(true, "bytes="+bytes); 
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    finally
    {

       try {
          secondUS.close();
       }
       catch(Exception e) { System.out.println("Testcase cleanup failed. " + e);
       }
       deleteUserSpace(aUserSpace);
    }
  }

/**
Method tested: read(byte[], int)
Verify that an Exception is thrown if a second user space object trys to read from an open user space
(different AS400 object).
**/
  public void Var093()              // $A1
  {
     setVariation(93);
     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }

/**
Method tested: read(byte[], int)
Ensure that UserSpace.read is successful after the user space is closed (same AS400 object).
**/
  public void Var094()              // $A1
  {
     setVariation(94);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*USE");

        aUserSpace.close();             // Close the user space.

        byte[] dataBuffer = new byte[16];
        int bytes = aUserSpace.read(dataBuffer, 0);
        assertCondition(true, "bytes="+bytes); 
        
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }







/**
Method tested: create()
Verify that the size of the User Space matches the length parameter used during creation.
**/
  public void Var095()
  {
    setVariation(95);

    int initialSize = 12000;
    int tempSize;
    int predictedSize;
    int a = 1;
    UserSpace aUserSpace = null;

    try {
       aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
       aUserSpace.setMustUseProgramCall(false);

       aUserSpace.create(initialSize, true, " ", (byte)0x00, "USCREATE test", "*ALL");

       // ***************************
       // expected size calculation
       //    On the AS400 the actual size of the user space is different.
       //    A User Space is created to the nearest 4096K page size with 512K being used for page info.
       //    For example:
       //        create length =    1- 3584  creates 4096K space w/ 512K for page info.
       //                        3585- 7680  creates 8192K space w/ 512K for page info.
       //    So if you create a user space of length 3585.  Add 512 for page info puts you over the 4096
       //    page size so the as400 bumps it up to 8192.  So the as400 will report the user space as
       //    8192 but w/ the 512 for page info, you really have 7680 in usable space.
       //
       // Summary: calculation needs to determine the 4096K block the user space will be created into
       //    and subtract 512K for page info resulting in the correct length of usable space.
       // ***************************
       tempSize = initialSize - 3584;
       if (tempSize > 0)
       {
          a += tempSize / 4096;
          if (tempSize % 4096 != 0)
             a++;
          predictedSize = 4096 * a - 512;
       }
       else
          predictedSize = 4096 - 512;

       if (predictedSize == aUserSpace.getLength())
          succeeded();
       else
          failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }

    // delete the user space
    deleteUserSpace(aUserSpace);
  }


/**
Method tested: create()
Ensure that the ExtendedIllegalArgumentException is thrown if the extendedAttribute parameter has more than 10 characters.
**/
  public void Var096()
  {
    setVariation(96);
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);
    try {
      aUserSpace.create(11000, true, "BADUserSpaceTest", (byte)0x00, "USCREATE", "*ALL");
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
  }


/**
Method tested: create()
Verify that the *USE public authority of the User Space
matches the parameter used during creation.
**/
  public void Var097()
  {
     setVariation(97);

     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(pwrSys_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "USWRITE test", "*USE");
        aUserSpace.close();

        // Connect to the AS400 as a second user.
        UserSpace sameUserSpace = new UserSpace(usSystem_, userSpacePathName_) ;
        sameUserSpace.setMustUseProgramCall(false);

        sameUserSpace.setLength(15000);

        failed("No exception occurred.");

     }
     catch(Exception e)
     {
        if (exceptionIs(e, "IOException"))
        {
           if (e.getMessage().equalsIgnoreCase("CPF9802 Not authorized to object USWRITE in USTEST."))
              succeeded();
           else
              failed(e, "Unexpected exception occurred.");
        }
        else
           failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: create()
Ensure that the User Space uses the InitialValue specified during User Space creation.
**/
  public void Var098()
  {
     setVariation(98);

     byte expected = 0x00;

     UserSpace aUserSpace = null;
     aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try {
       aUserSpace.create(11000, true, " ", expected, "USCREATE test", "*ALL");
       byte checkValue = aUserSpace.getInitialValue();

       if (expected == checkValue)
          succeeded();
       else
       {
          failed("Unexpected results occurred.");
       }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }


/**
Method tested: create()
Ensure that an IOException is thrown if replace = false was specified during the User Space creation
of an existing User Space.
  **/
  public void Var099()
  {
    setVariation(99);

    UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
    aUserSpace.setMustUseProgramCall(false);

    try {
        aUserSpace.create(11000, false, " ", (byte)0x00, "USCREATE test", "*ALL");
        failed("No exception.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "IOException"))
       {
          if (e.getMessage().equalsIgnoreCase("CPF9870 Object PREEXIST type *USRSPC already exists in library USTEST."))
             succeeded();
          else
             failed(e, "Unexpected exception occurred.");
       }
       else
          failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: create()
Ensure that a new user space is created when replace=true is specified.
**/
  public void Var100()
  {
     setVariation(100);

     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
        aUserSpace.setMustUseProgramCall(false);

        aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
        int original = aUserSpace.getLength();

        // replace
        aUserSpace.create(30000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
        int newSize = aUserSpace.getLength();

        if (newSize > original)
           succeeded();
        else
           failed("Unexpected results occurred.");
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }

  public void Var101()
  {
     setVariation(101);

     try {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USCREATE.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "*ALL");

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "IOException"))
       {
          if (e.getMessage().equalsIgnoreCase("CPF9810 Library USBADLIB not found."))
             succeeded();
          else
             failed(e, "Unexpected exception occurred.");
       }
       else
          failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: delete()
Ensure that IOException is thrown with CPF2105 if UserSpace.delete() is
called on a non-existing User Space.
**/
  public void Var102()
  {
     setVariation(102);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
        aUserSpace.delete();

        aUserSpace.delete();
        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "IOException"))
       {
          if (e.getMessage().equalsIgnoreCase("CPF2105 Object USDELETE in USTEST type *USRSPC not found."))
             succeeded();
          else
             failed(e, "Unexpected exception occurred.");
       }
       else
          failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: delete()
Ensure that IOException is thrown with CPF2110 if UserSpace.delete() is
called on a non-existing library.
**/
  public void Var103()
  {
     setVariation(103);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USDELETE.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        //aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
        aUserSpace.delete();

        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "IOException"))
       {
          if (e.getMessage().equalsIgnoreCase("CPF2110 Library USBADLIB not found."))
             succeeded();
          else
             failed(e, "Unexpected exception occurred.");
       }
       else
          failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: delete()
Ensure that true is returned if UserSpace.delete() is called on an existing User Space.
**/
  public void Var104()
  {
     setVariation(104);
     UserSpace aUserSpace = null;

     try {
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");
        aUserSpace.setMustUseProgramCall(false);

        aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
        aUserSpace.delete();

        succeeded();
     }
     catch(Exception e)
     {
        failed(e, "Exception occurred.");
     }
  }


/**
Method tested: setSystem()
Ensure that the System cannot be reset after a connection.
**/
  public void Var105()
  {
     setVariation(105);
     UserSpace aUserSpace = null;

     try {

        AS400 as400 = new AS400();
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USSET.USRSPC");
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create(11000, true, " ", (byte)0x00, "USSET test", "*ALL");

        aUserSpace.setMustUseProgramCall(false);
        as400.close(); 
        failed("Exception did not occur.");
    }
    catch(Exception e)
    {
       if (exceptionStartsWith(e, "ExtendedIllegalStateException",
          "mustUseProgramCall", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
          succeeded();
       else
          failed(e, "Unexpected exception occurred.");
    }
    deleteUserSpace(aUserSpace);
  }





/**
Method tested: close()
Verify that an Exception is thrown on a UserSpace.close if the user space has been deleted.
**/
  public void Var106()              // $A1
  {
    setVariation(106);

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try {
       aUserSpace.create(10240, true, " ", (byte)0x00, "USCLOSE test", "*ALL");
       aUserSpace.delete();
       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: delete()
Ensure that the user space can be deleted after being closed.
**/
  public void Var107()                  // $A1
  {
    setVariation(107);

    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    aUserSpace.setMustUseProgramCall(false);

    try {
       aUserSpace.create(10240, true, " ", (byte)0x00, "USDELETE test", "*ALL");

       aUserSpace.close();            // Close the user space.

       aUserSpace.delete();           // Delete the user space.
       succeeded();
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception occurred.");
       deleteUserSpace(aUserSpace);
    }
  }







/**
Method tested: setAutoExtendible()
Ensure that setAutoExtendible(true) allows the User Space to auto extend.
**/
  public void Var108()
  {
     setVariation(108);

     UserSpace aUserSpace = null;
     try {
        // Connect to the AS400.
        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create("*DEFAULT", 3584, false, " ", (byte)0x00, "CHGATTR user space", "*EXCLUDE");
        boolean initialValue = aUserSpace.isAutoExtendible();

        // Set the User Space as auto extendible.
        if (!initialValue)
           aUserSpace.setAutoExtendible(true);

        if (aUserSpace.isAutoExtendible())
           succeeded();
        else
           failed("Wrong extend info.");
     }
     catch (Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: setAutoExtendible()
Ensure that setAutoExtendible(false) does not auto extend the User Space when running natively on
the AS/400 JVM.  Ensure that the User Space will auto extend if not running natively on the AS/400
JVM.
**/
  public void Var109()
  {
     setVariation(109);
     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }

/**
Method tested: setInitialValue()
Verify setInitialValue(byte) uses the initial byte specified if used before a connection has been made.
**/
  public void Var110()
  {
     setVariation(110);

     try {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        byte expectedByte = (byte)0x03;

        aUserSpace.setInitialValue(expectedByte);

        byte testByte = aUserSpace.getInitialValue();

        if (testByte == expectedByte)
        {
           succeeded();
           pre_existingByteValue_ = expectedByte;
        }
        else
           failed("Unexpected results occurred.");

        aUserSpace.close();
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     systemObject_.disconnectAllServices();
  }

/**
Method tested: setInitialValue()
Verify that setInitialValue(byte) uses the initial byte specified.
**/
  public void Var111()
  {
     setVariation(111);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try {
        aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");
        byte expectedByte = (byte)0x16;
        aUserSpace.setInitialValue(expectedByte);

        byte testByte = aUserSpace.getInitialValue();
        if (testByte == expectedByte)
           succeeded();
        else
           failed("Unexpected results occurred.");
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: setInitialValue()
Verify that setInitialValue(byte) can be called after a User Space write.
**/
  public void Var112()
  {
     setVariation(112);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");

        byte[] writeBuffer = { 9, 8, 7, 6, 5 };
        aUserSpace.write(writeBuffer, 0);

        byte expectedByte = (byte)0x19;
        aUserSpace.setInitialValue(expectedByte);

        byte testByte = aUserSpace.getInitialValue();

        if (testByte == expectedByte)
           succeeded();
        else
           failed("Unexpected results occurred.");
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: setLength()
Ensure that an IOException is thrown if the length parameter is greater than MAX.
**/
  public void Var113()
  {
     setVariation(113);


     UserSpace aUserSpace = null;

     try {

       aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");
       aUserSpace.setMustUseProgramCall(false);
       aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

       // Attempt to reset the system
       AS400 newAS400 = new AS400();
       aUserSpace.setLength(16776705);
       newAS400.close();
     }
     catch (Exception e)
     {
        if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
           "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
           succeeded();
        else
           failed(e, "Wrong exception info.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: setLength()
Ensure that the new length matches the expected length when increased to next page limit.
**/
  public void Var114()
  {
     setVariation(114);

     UserSpace aUserSpace = null;

     try {

        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create("*DEFAULT", 3584, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");
        int initialSize = aUserSpace.getLength();

        // Set the User Space as auto extendible.
        int expectedLength = 3585;
        aUserSpace.setLength(expectedLength);

        // ***************************
        // expected size calculation
        //    On the AS400 the actual size of the user space is different.
        //    A User Space is created to the nearest 4096K page size with 512K being used for page info.
        //    For example:
        //        create length =    1- 3584  creates 4096K space w/ 512K for page info.
        //                        3585- 7680  creates 8192K space w/ 512K for page info.
        //    So if you create a user space of length 3585.  Add 512 for page info puts you over the 4096
        //    page size so the as400 bumps it up to 8192.  So the as400 will report the user space as
        //    8192 but w/ the 512 for page info, you really have 7680 in usable space.
        //
        // Summary: calculation needs to determine the 4096K block the user space will be created into
        //    and subtract 512K for page info resulting in the correct length of usable space.
        // ***************************
        int factor = 1;
        factor += expectedLength / 4096;
        if ( expectedLength > (4096 * factor - 512) )
           factor++;
        expectedLength = 4096 * factor - 512;
        if (expectedLength == aUserSpace.getLength())
           succeeded();
        else
           failed("Wrong extend info." + initialSize + ":" + expectedLength + ":" + aUserSpace.getLength());
     }
     catch (Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: setLength()
Ensure that the new length matches the expected length when decreased to previous page limit.
**/
  public void Var115()
  {
      setVariation(115);

     UserSpace aUserSpace = null;

     try {

        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create("*DEFAULT", 7680, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");
        int initialSize = aUserSpace.getLength();

        // Set the User Space as auto extendible.
        int expectedLength = 3584;
        aUserSpace.setLength(expectedLength);

        // calculate expectedLength
        int factor = 1;
        factor += expectedLength / 4096;
        if ( expectedLength > (4096 * factor - 512) )
           factor++;
        expectedLength = 4096 * factor - 512;
        if (expectedLength == aUserSpace.getLength())
           succeeded();
        else
           failed("Wrong extend info." + initialSize + ":" + expectedLength + ":" + aUserSpace.getLength());
     }
     catch (Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
Method tested: setLength()
Ensure that the new length matches the expected length when increasing within a 4096 byte page size.
**/
  public void Var116()
  {
     setVariation(116);

     UserSpace aUserSpace = null;

     try {

        aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");
        aUserSpace.setMustUseProgramCall(false);
        aUserSpace.create("*DEFAULT", 3585, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");
        int initialSize = aUserSpace.getLength();

        // Set the User Space as auto extendible.
        int expectedLength = 7680;
        aUserSpace.setLength(expectedLength);

        // calculate expectedLength
        int factor = 1;
        factor += expectedLength / 4096;
        if ( expectedLength > (4096 * factor - 512) )
           factor++;
        expectedLength = 4096 * factor - 512;

        if (expectedLength == aUserSpace.getLength())
           succeeded();
        else
           failed("Wrong extend info." + initialSize + ":" + expectedLength + ":" + aUserSpace.getLength());
     }
     catch (Exception e)
     {
        failed(e, "Exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: setLength()
Verify that setLength(int) can be called when a connection has not been made.
**/
  public void Var117()
  {
      setVariation(117);

      try {
         UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
         aUserSpace.setMustUseProgramCall(false);

         int expectedLength = 18000;
         aUserSpace.setLength(expectedLength);

         int testLength = aUserSpace.getLength();

         // calculate expectedLength
         int factor = 1;
         factor += expectedLength / 4096;
         if ( expectedLength > (4096 * factor - 512) )
            factor++;
         expectedLength = 4096 * factor - 512;

         if (testLength == expectedLength)
         {
            succeeded();
            // set the original length value
            pre_existingLengthValue_ = expectedLength;
         }
         else
            failed("Unexpected results occurred.");

         aUserSpace.close();
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception occurred.");
      }
      systemObject_.disconnectAllServices();
  }

/**
Method tested: setLength()
Verify that setLength(int) can be called after a User Space write.
**/
  public void Var118()
  {
     setVariation(118);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");

        byte[] writeBuffer = { 9, 8, 7, 6, 5 };
        aUserSpace.write(writeBuffer, 0);

        int expectedLength = 48000;
        aUserSpace.setLength(expectedLength);

        int testLength = aUserSpace.getLength();

        // calculate expectedLength
        int factor = 1;
        factor += expectedLength / 4096;
        if ( expectedLength > (4096 * factor - 512) )
           factor++;
        expectedLength = 4096 * factor - 512;

        if (testLength == expectedLength)
           succeeded();
        else
           failed("Unexpected results occurred.");
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: getInitialValue()
Verify that getInitialValue() can be called after a User Space write.
**/
  public void Var119()
  {
     setVariation(119);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");

        byte[] writeBuffer = { 9, 8, 7, 6, 5 };
        aUserSpace.write(writeBuffer, 0);

        byte testByte = aUserSpace.getInitialValue();

        assertCondition(true,"testByte:"+testByte);
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: getLength()
Verify that IOException is thrown if the library does not exist.
**/
  public void Var120()
  {
     setVariation(120);

     UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/BADLIB.LIB/USATTRIB.USRSPC");
     aUserSpace.setMustUseProgramCall(false);

     try {
        int testLength = aUserSpace.getLength();

        failed("No exception occurred."+testLength);
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "IOException"))
        {
           if (e.getMessage().equalsIgnoreCase("CPF9810 Library BADLIB not found."))
              succeeded();
           else
              failed(e, "Unexpected exception occurred.");
        }
        else
           failed(e, "Unexpected exception occurred.");
     }

  }

/**
Method tested: getLength()
Verify that getLength() can be called after a User Space write.
**/
  public void Var121()
  {
     setVariation(121);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try
     {
        int expectedLength = 11000;

        aUserSpace.create(expectedLength, true, " ", (byte)0x00, "USATTR test", "*ALL");

        byte[] writeBuffer = { 9, 8, 7, 6, 5 };
        aUserSpace.write(writeBuffer, 0);

        int testLength = aUserSpace.getLength();

        // calculate expectedLength
        int factor = 1;
        factor += expectedLength / 4096;
        if ( expectedLength > (4096 * factor - 512) )
           factor++;
        expectedLength = 4096 * factor - 512;
        if (testLength == expectedLength)
           succeeded();
        else
           failed("Unexpected results occurred.");

     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }


/**
Method tested: setAutoExtendible()
Verify setAutoExtendible(boolean) if used before a connection has been made.
**/
  public void Var122()
  {
     setVariation(122);

     try {
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.setMustUseProgramCall(false);

        boolean expected = true;

        aUserSpace.setAutoExtendible(expected);

        boolean test = aUserSpace.isAutoExtendible();

        if (test == expected)
           succeeded();
        else
           failed("Unexpected results occurred.");
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     systemObject_.disconnectAllServices();
  }

/**
Method tested: setAutoExtendible()
Verify that setAutoExtendible(boolean) uses the value specified.
**/
  public void Var123()
  {
     setVariation(123);

     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
     aUserSpace.setMustUseProgramCall(false);

     try {
        aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");
        boolean expected = false;
        aUserSpace.setAutoExtendible(expected);

        boolean test = aUserSpace.isAutoExtendible();
        if (test == expected)
           succeeded();
        else
           failed("Unexpected results occurred.");
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
     deleteUserSpace(aUserSpace);
  }

/**
**/
  public void Var124()
  {
     setVariation(124);
     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }


  // test exists
  public void Var125()
  {
     setVariation(125);
     succeeded();       // variation applies only to setMustUseProgramCall(true);
                        // so we tested this in UserSpacePgmCallTestcase.
  }


}

