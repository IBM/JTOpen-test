///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceWriteTestcase.java
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
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.UserSpace;

import test.Testcase;
import test.UserSpaceTest;

/**
 * Test write methods for UserSpace.
 **/
public class UserSpaceWriteTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpaceWriteTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserSpaceTest.main(newArgs); 
   }
  private int maxUserSpaceSize_ = 16776704;
  private AS400 usSystem_;
  private static String userSpacePathName_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
  private String pre_existingUserSpace_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC";
  private String testAuth = UserSpaceTest.COLLECTION+"TA"; 
  private String unauthorizedUserSpace_ = "/QSYS.LIB/"+testAuth+".LIB/USWRITE3.USRSPC";
  private boolean setupUnauthorized_ = false;
  private String authLib = UserSpaceTest.COLLECTION+"AL"; 
  private String authorityUserSpace_ = "/QSYS.LIB/"+authLib+".LIB/USWRITE2.USRSPC";
  private boolean setupAuthority_ = false;
  private byte pre_existingByteValue_ = 0x00;
  private int pre_existingLengthValue_ = 11000;
  private String collectorMessageID_ = "";

  /**
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
userSpacePathName_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
pre_existingUserSpace_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC";
testAuth = UserSpaceTest.COLLECTION+"TA"; 
unauthorizedUserSpace_ = "/QSYS.LIB/"+testAuth+".LIB/USWRITE3.USRSPC";
 authLib = UserSpaceTest.COLLECTION+"AL"; 
authorityUserSpace_ = "/QSYS.LIB/"+authLib+".LIB/USWRITE2.USRSPC";
    // Create AS/400 object necessary to run testcases.
    setupUSExisting();
  }

  /*
   * Method: deleteUserSpace(UserSpace) Description: Cleanup user space on
   * AS/400 that was used during testing.
   */
  void deleteUserSpace(UserSpace aUserSpace) {
    try {
      aUserSpace.delete();
    } catch (Exception e) {
      output_.println("Cleanup failed:");
      e.printStackTrace(output_);
    }
  }

  /*
   * Method: setupUSExisting() Description: Create a user space on the AS/400 to
   * be used by testcases needing an existing user space.
   */
  private void setupUSExisting() throws Exception {
    try {
      // Create an AS400 object to be used with USTEST user profile.
      usSystem_ = new AS400(systemObject_);

      // Create a user space to use test on an pre-existing user space.
      UserSpace aUserSpace = new UserSpace(systemObject_,
          pre_existingUserSpace_);
      aUserSpace.create(pre_existingLengthValue_, true, " ",
          pre_existingByteValue_, "CRTDLT UserSpace", "*ALL");
      aUserSpace.close();
    } catch (Exception e) {
      output_.println("Setup failed:");
      e.printStackTrace(output_);
      throw e;
    }
  }

  /*
   * Method: setupUSUnauthorized() Description: Create a library and user space
   * that testcases will have NO authority to.
   */
  private void setupUSUnauthorized() {
    try {
      deleteLibrary(""+testAuth+"");
      cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
      cmdRun("QSYS/CRTAUTL AUTL(USAUTHLIST) AUT(*EXCLUDE)");
      cmdRun("QSYS/CRTLIB LIB("+testAuth+") AUT(USAUTHLIST)");

      UserSpace bUserSpace = new UserSpace(pwrSys_, unauthorizedUserSpace_);
      bUserSpace.create(pre_existingLengthValue_, true, " ",
          pre_existingByteValue_, "create UserSpace", "*ALL");
      bUserSpace.close();

      setupUnauthorized_ = true;
    } catch (Exception e) {
      output_.println("Setup failed:");
      e.printStackTrace(output_);
    } finally {
      pwrSys_.disconnectAllServices();
    }
  }

  /*
   * Method: setupUSAuthority() Description: Create a user space to be used in
   * authority testcases.
   */
  private void setupUSAuthority() {
    try {
      deleteLibrary(""+authLib+"");
      cmdRun("QSYS/CRTLIB LIB("+authLib+")");

      UserSpace aUSpace = new UserSpace(pwrSys_, authorityUserSpace_);
      aUSpace.create(11000, true, " ", (byte) 0x00, "USWRITE test", "*ALL");
      aUSpace.close();

      setupAuthority_ = true;
    } catch (Exception e) {
      output_.println("Setup failed:");
      e.printStackTrace(output_);
    }
  }

  /*
   * Method: writeData() Description: Write data to the user space via the
   * Collector APIs.
   */
  private boolean writeData() {
    boolean collectorWrite = false;

    ProgramCall showData = new ProgramCall(pwrSys_);

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
    } catch (Exception e) {
    }

    try {
      if (showData.run() != true) {
        AS400Message[] messageList = showData.getMessageList();
        for (int msg = 0; msg < messageList.length; msg++)
          collectorMessageID_ = messageList[msg].getID();
      } else {
        // Get output
        AS400Text textConverter = new AS400Text(10, pwrSys_.getCcsid(), pwrSys_);
        @SuppressWarnings("unused")
        String resourceName = (String) textConverter.toObject(resourceBytes);

        collectorWrite = true; // Write successful.
      }

    } catch (Exception ex) {
      output_.println("PgmRun Failed: " + ex);
    }
    return collectorWrite;
  }

  /*
   * Method: workCollector(boolean, String) Description: Start/Stop the work
   * collector via AS/400 APIs.
   */
  public void workCollector(boolean start, String spaceName) {
    ProgramCall collector = new ProgramCall(pwrSys_);

    ProgramParameter[] collectorParmList = new ProgramParameter[6];
    // input parm - type of action to perform (char10)
    String action = (start) ? "*START" : "*END";
    collectorParmList[0] = new ProgramParameter(setName(action, 10));

    // input parm - type of resource (char10)
    collectorParmList[1] = new ProgramParameter(setName("*COMM", 10));

    // input parm - time between collections (bin4)
    AS400Bin4 bin4Converter = new AS400Bin4();
    collectorParmList[2] = new ProgramParameter(bin4Converter.toBytes(240));

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
    } catch (Exception e) {
    }

    try {
      if (collector.run() != true) {
        AS400Message[] messageList = collector.getMessageList();
        for (int msg = 0; msg < messageList.length; msg++)
          output_.println("PgmError: " + messageList[msg].toString());
      }
    } catch (Exception ex) {
      String message = (start) ? "started! " : "ended! ";
      output_.println("PgmRun: Collector failed to " + message + ex);
    }
  }

  /*
   * Method: setName(String, int) Description: Set the name of the user space
   * for input via the AS/400 API.
   */
  public byte[] setName(String name, int size) {
    StringBuffer pathName = null;
    AS400Text converter = new AS400Text(size, pwrSys_.getCcsid(), pwrSys_);

    if (size == 10) {
      pathName = new StringBuffer("          ");
      pathName.insert(0, name);
    } else if (size == 20) {
      // Verify name is valid integrated file system path name.
      QSYSObjectPathName userSpacePath = null;
      try {
        userSpacePath = new QSYSObjectPathName(name);
      } catch (IllegalPathNameException e) {
      }
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
   * Cleanup objects that have been created on the AS400.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    deleteLibrary(""+testAuth+"");
    cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
    deleteLibrary(""+authLib+"");

    // delete the pre-existing User Space.
    UserSpace aUserSpace = new UserSpace(pwrSys_, pre_existingUserSpace_);
    aUserSpace.delete();

    // Disconnect all services used.
    pwrSys_.disconnectAllServices();
    if (systemObject_ != null)
      systemObject_.disconnectAllServices();
  }

  /**
   * Method tested: write(byte[], int) Ensure the NullPointerException is thrown
   * if dataBuffer is null.
   **/
  public void Var001() {
    UserSpace aUserSpace = null;
    byte[] inBuffer = null;

    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.write(inBuffer, 0);

      failed("Expected exception did not occur.");
    } catch (Exception e) {
      if (exceptionIs(e, "NullPointerException", "dataBuffer"))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure the NullPointerException
   * is thrown if dataBuffer is null.
   **/
  public void Var002() {
    UserSpace aUserSpace = null;
    byte[] inBuffer = null;

    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.write(inBuffer, 0, 0, 0);

      failed("Expected exception did not occur.");
    } catch (Exception e) {
      if (exceptionIs(e, "NullPointerException", "dataBuffer"))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");

    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure the
   * NullPointerException is thrown if dataBuffer is null.
   **/
  public void Var003() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    byte[] inBuffer = null;

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.write(inBuffer, 0, 0, 0, 0);

      failed("Expected exception did not occur.");
    } catch (Exception e) {
      if (exceptionIs(e, "NullPointerException", "dataBuffer"))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(String, int) Ensure that NullPointerException is
   * thrown if data is null.
   **/
  public void Var004() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
    String inputString = null;

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.write(inputString, 0);

      failed("Expected exception did not occur.");
    } catch (Exception e) {
      if (exceptionIs(e, "NullPointerException", "data"))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(String, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if String has length 0.
   **/
  public void Var005() {
    UserSpace aUserSpace = null;
    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write("", 0);

      failed("Expected exception did not occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataBuffer has length zero.
   **/
  public void Var006() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      aUserSpace.write(new byte[0], 0);

      failed("Expected exception did not occur."+lengthBefore);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataBuffer", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataBuffer has length zero.
   **/
  public void Var007() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      aUserSpace.write(new byte[0], 0, 0, 0);

      failed("Expected exception did not occur."+lengthBefore);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataBuffer", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataBuffer has length zero.
   **/
  public void Var008() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      aUserSpace.write(new byte[0], 0, 0, 0, 0);

      failed("Expected exception did not occur."+lengthBefore);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataBuffer", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that ExtendedIOException is thrown
   * during write attempt if dataBuffer + userSpaceOffset is greater than
   * maxUserSpaceSize.
   **/
  public void Var009() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(16776704, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      byte[] dataBuffer = { 0, 1, 2, 3 };
      aUserSpace.write(dataBuffer, 16776702);

      failed("Expected exception did not occur."+lengthBefore);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset + length",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that the user space auto extends
   * if it writes up to length+1 if dataBuffer has length greater than length.
   **/
  public void Var010() {
     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(4095, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.setAutoExtendible(true);
      int lengthBefore = aUserSpace.getLength();
      byte[] dataBuffer = { 9, 8, 7, 6, 5 };
      aUserSpace.write(dataBuffer, 4095);
      int lengthAfter = aUserSpace.getLength();

      if (lengthAfter > lengthBefore)
        succeeded();
      else
        failed("Error: Unexpected write results.");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure the
   * ExtendedIllegalArgumentException is thrown if userSpaceOffset is < 0.
   **/
  public void Var011() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      byte[] inputBuffer = new byte[10];
      for (int i = 0; i < 10; i++)
        inputBuffer[i] = 99;

      // Attempt to set offset to null
      aUserSpace.write(inputBuffer, -1);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset (-1): ",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if userSpaceOffset is < 0.
   **/
  public void Var012() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      byte[] inputBuffer = new byte[10];
      for (int i = 0; i < 10; i++)
        inputBuffer[i] = 99;

      aUserSpace.write(inputBuffer, -1, 0, 10);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset (-1): ",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if userSpaceOffset is < 0.
   **/
  public void Var013() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      byte[] inputBuffer = new byte[10];
      for (int i = 0; i < 10; i++)
        inputBuffer[i] = 99;

      aUserSpace.write(inputBuffer, -1, 0, 10, 0);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset (-1): ",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(String, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if userSpaceOffset is less than
   * zero.
   **/
  public void Var014() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      // Attempt to set offset to null
      String inString = "TEST_STRING";
      aUserSpace.write(inString, -1);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset (-1): ",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that IOException is thrown if
   * userSpaceOffset is greater than the length of the User Space. (Impl.Remote
   * - set auto extend is disregarded.)
   **/
  public void Var015() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    // if (isNative_)
    // {
    // try
    // {
    // aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace",
    // "*ALL");
    // aUserSpace.setAutoExtendible(false);
    //
    // byte[] newData = { 1, 2, 3, 4};
    // aUserSpace.write(newData, 10999);
    // int usLength = aUserSpace.getLength();
    //
    // byte[] oneData = { 5 };
    // aUserSpace.write(oneData, usLength);
    // usLength = aUserSpace.getLength();
    //
    // aUserSpace.write(oneData, usLength);
    //
    // failed("Exception didn't occur.");
    // }
    // catch (Exception e)
    // {
    // assertExceptionStartsWith(e, "AS400Exception", "CPD3C14 ",
    // ErrorCompletingRequestException.AS400_ERROR);
    // }
    // finally
    // {
    // deleteUserSpace(aUserSpace);
    // }
    // }
    // else
    {
      try {
        aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
            "*ALL");
        aUserSpace.setAutoExtendible(false);

        byte[] newData = { 1, 2, 3, 4 };
        aUserSpace.write(newData, 10999);
        int usLength = aUserSpace.getLength();

        byte[] oneData = { 5 };
        aUserSpace.write(oneData, usLength);
        usLength = aUserSpace.getLength();

        aUserSpace.write(oneData, usLength);
        usLength = aUserSpace.getLength();

        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected exception occurred.");
      } finally {
        deleteUserSpace(aUserSpace);
      }
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that ExtendedIOException
   * is thrown if userSpaceOffset+length is greater than maxUserSpaceSize.
   **/
  public void Var016() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      // Attempt to set offset to null
      byte[] writeBuffer = new byte[20];
      aUserSpace.write(writeBuffer, maxUserSpaceSize_ - 1, 0,
          writeBuffer.length);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset + length",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is < 0.
   **/
  public void Var017() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write(new byte[10], 0, -1, 10);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is < 0.
   **/
  public void Var018() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write(new byte[10], 0, -1, 10, 0);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that nothing is
   * written is dataOffset is zero.
   **/
  public void Var019() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      aUserSpace.write(new byte[1000], 0, 0, 1000, 0);
      int lengthAfter = aUserSpace.getLength();

      if (lengthBefore == lengthAfter)
        succeeded();
      else
        failed("Error: Unexpected write results.");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is the length of
   * the dataBuffer.
   **/
  public void Var020() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      writeBuffer[19] = 19;
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, writeBuffer.length, 0);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is greater than
   * the length of the data buffer.
   **/
  public void Var021() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, writeBuffer.length + 1,
          writeBuffer.length, 0);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that nothing is written
   * is length is zero.
   **/
  public void Var022() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      aUserSpace.write(new byte[1000], 0, 0, 1000);
      int lengthAfter = aUserSpace.getLength();

      if (lengthBefore == lengthAfter)
        succeeded();
      else
        failed("Error: Unexpected write results.");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if length < 0.
   **/
  public void Var023() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write(new byte[10], 0, 0, -1);

      failed("Exception did not occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if length is < 0.
   **/
  public void Var024() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write(new byte[10], 0, 0, -1, 0);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if the length parameter is
   * greater than the length of the data buffer.
   **/
  public void Var025() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, 0, writeBuffer.length + 1);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(String, int) Ensure that no exception is thrown if
   * userSpaceOffset is greater than the length of the User Space.
   **/
  public void Var026() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.setAutoExtendible(true);

      int usLength = aUserSpace.getLength();

      aUserSpace.write("WRITE_TEST", usLength + 1);

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    }

    finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is greater than
   * the length of the data buffer.
   **/
  public void Var027() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, writeBuffer.length + 1,
          writeBuffer.length, 0);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if the length parameter is
   * greater than the length of the data buffer.
   **/
  public void Var028() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, 0, writeBuffer.length + 1, 0);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length",
          ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if forceAuxiliary is < 0.
   **/
  public void Var029() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write(new byte[10], 0, 0, 10, -1);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "forceAuxiliary",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if forceAuxiliary is > 2.
   **/
  public void Var030() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      aUserSpace.write(new byte[10], 0, 0, 10, 3);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "forceAuxiliary",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, FORCE_NONE) Ensure that changed
   * are not forced.
   **/
  public void Var031() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int FORCE_NONE = 0;
      byte[] data = new byte[10];
      for (int i = 0; i < data.length; i++)
        data[i] = 99;

      aUserSpace.write(data, 0, 0, 10, FORCE_NONE);

      // Verify data was written with changes not forced.
      int i = 0;
      int j = 0;
      byte[] inByte = new byte[1];
      int numBytes = 0;
      do {
        numBytes = aUserSpace.read(inByte, i++);
      } while (inByte[0] == data[j++] && j != data.length);

      if (i == data.length)
        succeeded();
      else
        failed("Byte mismatch during write."+numBytes);
    } catch (Exception e) {
      failed(e, "Exception occured.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, FORCE_ASYNCHRONOUSLY) Ensure
   * that changed are forced asynchronously.
   **/
  public void Var032() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int FORCE_ASYNCHRONOUSLY = 1;
      byte[] data = new byte[10];
      for (int i = 0; i < data.length; i++)
        data[i] = 88;

      aUserSpace.write(data, 0, 0, 10, FORCE_ASYNCHRONOUSLY);

      // Verify data was written with changes not forced.
      int i = 0;
      int j = 0;
      byte[] inByte = new byte[1];
      int numBytes = 0; 
      do {
        numBytes = aUserSpace.read(inByte, i++);
      } while (inByte[0] == data[j++] && j != data.length);

      if (i == data.length)
        succeeded();
      else
        failed("Byte mismatch during write."+numBytes);
    } catch (Exception e) {
      failed(e, "Exception occured.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, FORCE_SYNCHRONOUSLY) Ensure
   * that changed are forced synchronously.
   **/
  public void Var033() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int FORCE_SYNCHRONOUSLY = 2;
      byte[] data = new byte[10];
      for (int i = 0; i < data.length; i++)
        data[i] = 77;

      aUserSpace.write(data, 0, 0, 10, FORCE_SYNCHRONOUSLY);

      // Verify data was written with changes not forced.
      int i = 0;
      int j = 0;
      byte[] inByte = new byte[1];
      int numBytes;
      do {
        numBytes = aUserSpace.read(inByte, i++);
      } while (inByte[0] == data[j++] && j != data.length);

      if (i == data.length)
        succeeded();
      else
        failed("Byte mismatch during write."+numBytes);
    } catch (Exception e) {
      failed(e, "Exception occured.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Write every possible byte to a User
   * Space.
   **/
  public void Var034() {
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "write UserSpace",
          "*ALL");

      byte[] data = new byte[256];
      for (int i = 0; i < data.length; i++)
        data[i] = (byte) i;

      aUserSpace.write(data, 0);

      int i = 0;
      int j = 0;
      byte[] inByte = new byte[1];
      int numBytes = 0; 
      do {
        numBytes = aUserSpace.read(inByte, i++);
      } while (inByte[0] == data[j++] && j != 256);

      if (i == data.length)
        succeeded();
      else
        failed("Byte mismatch during write."+numBytes);
    } catch (Exception e) {
      failed(e);
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Write every possible byte to a
   * User Space.
   **/
  public void Var035() {
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] data = new byte[256];
      for (int i = 0; i < data.length; i++)
        data[i] = (byte) i;

      aUserSpace.write(data, 0, 0, data.length);

      int i = 0;
      int j = 0;
      byte[] inByte = new byte[1];
      int numBytes = 0; 
      do {
        numBytes += aUserSpace.read(inByte, i++);
      } while (inByte[0] == data[j++] && j != 256);

      if (i == data.length)
        succeeded();
      else
        failed("Byte mismatch during write."+numBytes);

    } catch (Exception e) {
      failed(e);
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Write every possible byte to a
   * User Space.
   **/
  public void Var036() {
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] data = new byte[256];
      for (int i = 0; i < data.length; i++)
        data[i] = (byte) i;

      aUserSpace.write(data, 0, 0, data.length, 0);

      int i = 0;
      int j = 0;
      byte[] inByte = new byte[1];
      int numBytes = 0; 
      do {
        numBytes += aUserSpace.read(inByte, i++);
      } while (inByte[0] == data[j++] && j != 256);

      if (i == data.length)
        succeeded();
      else
        failed("Byte mismatch during write."+numBytes);
    } catch (Exception e) {
      failed(e);
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(String, int) Write every possible byte to a User
   * Space.
   **/
  public void Var037() {
    UserSpace aUserSpace = null;
    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "write UserSpace",
          "*ALL");

      String unicode = "";
      for (int i = 0; i < 0x80; i++)
        unicode += (char) i;

      // write string to the user space
      aUserSpace.write(unicode, 0);

      // determine expected byte array
      AS400Text outText = new AS400Text(unicode.length(),
          systemObject_.getCcsid(), systemObject_);
      byte[] outBuffer = outText.toBytes(unicode);

      // read from the user space
      byte[] inBuffer = new byte[unicode.length()];
      int inBytes = aUserSpace.read(inBuffer, 0);

      // verify the byte array contents
      int x = 0;
      while ((x < inBytes) && (inBuffer[x] == outBuffer[x]))
        x++;

      if (x == outBuffer.length)
        succeeded();
      else
        failed("Unexpected results occurred.");

    } catch (Exception e) {
      failed("Unexpected error occurred. " + e);
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that ExtendedIOException will be
   * thrown if write attempts to write past the maximum length of a user space.
   **/
  public void Var038() {
    UserSpace aUserSpace = null;

    try {
      String userSpacePathName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/READAPI.USRSPC";
      aUserSpace = new UserSpace(systemObject_, userSpacePathName);
      aUserSpace.create(16776704, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] moreData = { 6, 7, 8, 9 };
      // attempt to write off end of max user space.
      aUserSpace.write(moreData, 16776701);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset + length",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Verify write is successful if a
   * connection has not been made.
   **/
  public void Var039() {
    byte[] data = { 1, 2, 3, 4, 5 };
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, userSpacePathName_);
      aUserSpace.create(11000, true, " ", (byte) 0x00, "USWRITE test", "*ALL");
      systemObject_.disconnectService(AS400.COMMAND);

      aUserSpace.write(data, 0);

      succeeded();

    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure AS400SecurityException is thrown
   * if the user does not have authority to the library.
   **/
  public void Var040() {
    // Create necessary AS/400 objects for testcase.
    if (!setupUnauthorized_)
      setupUSUnauthorized();

    try {

      UserSpace aUserSpace = new UserSpace(systemObject_,
          unauthorizedUserSpace_);

      byte[] data = new byte[20];
      aUserSpace.write(data, 0);

      failed("Expected exception did not occur (make sure -uid on command line does not have authority to "+testAuth+".lib/uswrite3.usrspc).");
    } catch (Exception e) {
      // if (isNative_)
      // {
      // assertExceptionStartsWith(e, "AS400SecurityException",
      // "/QSYS.LIB/"+testAuth+".LIB/USWRITE3.USRSPC: ",
      // AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
      // }
      // else
      {
        assertExceptionStartsWith(e, "AS400SecurityException",
            "/QSYS.LIB/"+testAuth+".LIB/USWRITE3.USRSPC: ",
            AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
      }
    } finally {
      systemObject_.disconnectAllServices();
    }

  }

  /**
   * Method tested: write(byte[], int) Ensure AS400SecurityException is thrown
   * if the user does not have authority to the User Space.
   **/
  public void Var041() {
    // Create needed AS/400 objects.
    if (!setupAuthority_)
      setupUSAuthority();

    CommandCall cmd = new CommandCall(pwrSys_);

    try {
      if (cmd.run("QSYS/GRTOBJAUT OBJ("+authLib+"/USWRITE2) OBJTYPE(*USRSPC) USER("
          + systemObject_.getUserId() + ") AUT(*EXCLUDE)") != true) {
        AS400Message[] messageList = cmd.getMessageList();
        throw new IOException(messageList[0].toString());
      }

      if (isNative_) {
        byte[] data = new byte[20];
        UserSpace aUserSpace = new UserSpace(systemObject_, authorityUserSpace_);
        aUserSpace.write(data, 0);
      } else {
        byte[] data = new byte[20];
        UserSpace aUserSpace = new UserSpace(usSystem_, authorityUserSpace_);
        aUserSpace.write(data, 0);
      }

      failed("Expected exception did not occur (make sure -uid does not have authority to write to "+authLib+".lib/uswrite2.usrspc).");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "AS400SecurityException",
          "/QSYS.LIB/"+authLib+".LIB/USWRITE2.USRSPC: ",
          AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    }

    try {
      UserSpace aUserSpace = new UserSpace(pwrSys_, authorityUserSpace_);
      aUserSpace.delete();

      deleteLibrary(""+authLib+"");

      pwrSys_.disconnectAllServices();
      usSystem_.disconnectAllServices();
    } catch (Exception e) {
      output_.println("Cleanup failed.");
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that
   * ExtendedIllegalArgumentException is thrown if the library does not exist.
   **/
  public void Var042() {
    try {
      UserSpace aUserSpace = new UserSpace(systemObject_,
          "/QSYS.LIB/USBADLIB.LIB/USWRITE3.USRSPC");

      byte[] dataBuffer = { 9, 8, 7, 6, 5 };
      aUserSpace.write(dataBuffer, 0);

      failed("Exception did not occur.");
    } catch (Exception e) {
      // if (isNative_)
      // {
      // assertExceptionStartsWith(e, "ObjectDoesNotExistException",
      // "/QSYS.LIB/USBADLIB.LIB/USWRITE3.USRSPC: ",
      // ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
      // }
      // else
      {
        assertExceptionStartsWith(e, "ObjectDoesNotExistException",
            "/QSYS.LIB/USBADLIB.LIB/USWRITE3.USRSPC: ",
            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
      }
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that
   * ExtendedIllegalArgumentException is thrown with CPF2105 if the user space
   * does not exist.
   **/
  public void Var043() {
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_,
          "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USBAD.USRSPC");

      byte[] dataBuffer = { 0, 1, 2, 3, 4 };
      aUserSpace.write(dataBuffer, 0);

      failed("Exception did not occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ObjectDoesNotExistException",
          "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USBAD.USRSPC: ",
          ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that ExtededIOException
   * is thrown if dataBuffer + userSpaceOffset is greater than maxUserSpaceSize.
   **/
  public void Var044() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(16776704, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      byte[] dataBuffer = { 0, 1, 2, 3 };
      aUserSpace.write(dataBuffer, 16776702, 0, dataBuffer.length);

      failed("Expected exception did not occur."+lengthBefore);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset + length",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that ExtendedIOException is thrown
   * if dataBuffer + userSpaceOffset is greater than maxUserSpaceSize.
   **/
  public void Var045() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(16776704, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      int lengthBefore = aUserSpace.getLength();
      byte[] dataBuffer = { 0, 1, 2, 3 };
      aUserSpace.write(dataBuffer, 16776702, 0, dataBuffer.length, 0);

      failed("Expected exception did not occur."+lengthBefore);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset + length",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that the User Space auto
   * extends if it writes up to length+1 if dataBuffer has length greater than
   * length.
   **/
  public void Var046() {
     UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(4095, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.setAutoExtendible(true);

      int lengthBefore = aUserSpace.getLength();
      byte[] dataBuffer = { 9, 8, 7, 6, 5 };
      aUserSpace.write(dataBuffer, 4094, 0, dataBuffer.length);
      int lengthAfter = aUserSpace.getLength();

      if (lengthAfter > lengthBefore)
        succeeded();
      else
        failed("Error: Unexpected write results.");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that the UserSpace
   * auto extends if it writes up to length+1 if dataBuffer has length greater
   * than length.
   **/
  public void Var047() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(4095, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");
      aUserSpace.setAutoExtendible(true);

      int lengthBefore = aUserSpace.getLength();
      byte[] dataBuffer = { 9, 8, 7, 6, 5 };
      aUserSpace.write(dataBuffer, 4094, 0, dataBuffer.length, 0);
      int lengthAfter = aUserSpace.getLength();

      if (lengthAfter > lengthBefore)
        succeeded();
      else
        failed("Error: Unexpected write results.");
    } catch (Exception e) {
      failed(e, "Exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int) Ensure that no exception is
   * thrown if userSpaceOffset is greater than the length of the User Space.
   * (Impl.Remote - set auto extend is disregarded.)
   **/
  public void Var048() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    // if (isNative_)
    // {
    // try
    // {
    // aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace",
    // "*ALL");
    // aUserSpace.setAutoExtendible(false);
    //
    // byte[] newData = { 1, 2, 3, 4};
    // aUserSpace.write(newData, 10999, 0, newData.length);
    // int usLength = aUserSpace.getLength();
    //
    // byte[] oneData = { 5 };
    // aUserSpace.write(oneData, usLength, 0, oneData.length);
    // usLength = aUserSpace.getLength();
    //
    // failed("Exception didn't occur.");
    // }
    // catch (Exception e)
    // {
    // assertExceptionStartsWith(e, "AS400Exception", "CPD3C14 ",
    // ErrorCompletingRequestException.AS400_ERROR);
    // }
    // finally
    // {
    // deleteUserSpace(aUserSpace);
    // }
    // }
    // else
    {
      try {
        aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
            "*ALL");
        aUserSpace.setAutoExtendible(false);

        byte[] newData = { 1, 2, 3, 4 };
        aUserSpace.write(newData, 10999, 0, newData.length);
        int usLength = aUserSpace.getLength();

        byte[] oneData = { 5 };
        aUserSpace.write(oneData, usLength, 0, oneData.length);
        usLength = aUserSpace.getLength();

        aUserSpace.write(oneData, usLength, 0, oneData.length);
        usLength = aUserSpace.getLength();

        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected exception occurred.");
      } finally {
        deleteUserSpace(aUserSpace);
      }
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that no exception
   * is thrown if userSpaceOffset is greater than the length of the User Space.
   * (Impl.Remote - set auto extend is disregarded.)
   **/
  public void Var049() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    // if (isNative_)
    // {
    // try
    // {
    // aUserSpace.create(11000, true, " ", (byte)0x00, "create UserSpace",
    // "*ALL");
    // aUserSpace.setAutoExtendible(false);
    //
    // byte[] newData = { 1, 2, 3, 4};
    // aUserSpace.write(newData, 10999, 0, newData.length);
    // int usLength = aUserSpace.getLength();
    //
    // byte[] oneData = { 5 };
    // aUserSpace.write(oneData, usLength, 0, oneData.length, 0);
    // usLength = aUserSpace.getLength();
    //
    // failed("Exception didn't occur.");
    // }
    // catch (Exception e)
    // {
    // assertExceptionStartsWith(e, "AS400Exception", "CPD3C14 ",
    // ErrorCompletingRequestException.AS400_ERROR);
    // }
    // finally
    // {
    // deleteUserSpace(aUserSpace);
    // }
    // }
    // else
    {
      try {
        aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
            "*ALL");
        aUserSpace.setAutoExtendible(false);

        byte[] newData = { 1, 2, 3, 4 };
        aUserSpace.write(newData, 10999, 0, newData.length);
        int usLength = aUserSpace.getLength();

        byte[] oneData = { 5 };
        aUserSpace.write(oneData, usLength, 0, oneData.length, 0);
        usLength = aUserSpace.getLength();

        aUserSpace.write(oneData, usLength, 0, oneData.length, 0);
        usLength = aUserSpace.getLength();

        succeeded();
      } catch (Exception e) {
        failed(e, "Unexpected exception occurred.");
      } finally {
        deleteUserSpace(aUserSpace);
      }
    }

  }

  /**
   * Method tested: write(String, int) Ensure that ExtendedIOException is thrown
   * if userSpaceOffset + writeString.length is greater than maxUserSpaceSize.
   **/
  public void Var050() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      // Attempt to set offset to null
      String writeString = "USWRITE test String";
      aUserSpace.write(writeString, maxUserSpaceSize_ - 1);

      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "userSpaceOffset + length",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is the length of
   * the dataBuffer.
   **/
  public void Var051() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      writeBuffer[19] = 19;
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, writeBuffer.length, 0, 0);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int, int, int, int) Ensure that
   * ExtendedIllegalArgumentException is thrown if dataOffset is greater than
   * the length of the data buffer.
   **/
  public void Var052() {
    UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

    try {
      aUserSpace.create(11000, true, " ", (byte) 0x00, "create UserSpace",
          "*ALL");

      byte[] writeBuffer = new byte[20];
      int usLength = aUserSpace.getLength();
      aUserSpace.write(writeBuffer, 0, writeBuffer.length + 1,
          writeBuffer.length);

      failed("No Exception occurred."+usLength);
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that UserSpace.write will write to
   * a user space being accessed by an AS/400 API.
   **/
  public void Var053(int runMode) {
    if (runMode == UNATTENDED) {
      notApplicable("Attended variation.");
      return;
    }

    String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, usName);
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Test User Space",
          "*USE");

      aUserSpace.close(); // close the user space.

      workCollector(true, usName); // Start the performance collector.
      writeData(); // Write the collection to the user space.
      workCollector(false, usName); // Stop the performance collector.

      byte[] array = { 0, 1, 2, 3 };
      aUserSpace.write(array, 0);

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that a second UserSpace object can
   * write to a open user space.(same AS400 object).
   **/
  public void Var054() // $A1
  {
    String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
    UserSpace aUserSpace = null;
    UserSpace secondUS = null;

    try {
      aUserSpace = new UserSpace(systemObject_, usName);
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Write User Space",
          "*USE");

      secondUS = new UserSpace(systemObject_, usName);

      byte[] dataBuffer = { 0, 1, 2, 3, 4 };
      secondUS.write(dataBuffer, 0);

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      try {
        secondUS.close();
      } catch (Exception e) {
        output_.println("Testcase cleanup failed. " + e);
      }
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that a second UserSpace object can
   * write to a user space after it is closed (different AS400 object).
   **/
  public void Var055() // @A1, @A2
  {
    String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
    UserSpace aUserSpace = null;
    UserSpace secondUS = null;

    try {
      aUserSpace = new UserSpace(systemObject_, usName);
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Write User Space",
          "*ALL");

      aUserSpace.close(); // close the user space.

      secondUS = new UserSpace(usSystem_, usName);

      byte[] dataBuffer = { 0, 1, 2, 3, 4 };
      secondUS.write(dataBuffer, 0);

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      try {
        secondUS.close();
      } catch (Exception e) {
        output_.println("Cleanup failed - UserSpace.close: " + e);
      }
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Verify that an Exception is thrown if a
   * second user space object trys to write to an open user space (different
   * AS400 object).
   **/
  public void Var056() // $A1
  {
    String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(pwrSys_, usName);
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Write User Space",
          "*USE");

      UserSpace secondUS = new UserSpace(usSystem_, usName);

      byte[] dataBuffer = { 0, 1, 2, 3, 4 };
      secondUS.write(dataBuffer, 0);

      failed("No Exception occurred.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "AS400SecurityException",
          "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC: ",
          AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
        succeeded();
      else
        failed(e, "Unexpected exception occurred.");
    } finally {
      try {
        if (aUserSpace != null) {
          aUserSpace.close();
          deleteUserSpace(aUserSpace);
        }
      } catch (Exception e) {
        output_.println("cleanup failed");
        e.printStackTrace(output_);
      }
    }
  }

  /**
   * Method tested: AS400 user space API write) Verify that an Exception is
   * thrown on an AS/400 user space API write if the user space object is NOT
   * closed.
   **/
  public void Var057(int runMode) {
    if (runMode == UNATTENDED) {
      notApplicable("Attended variation.");
      return;
    }

    String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, usName);
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Test User Space",
          "*USE");

      workCollector(true, usName); // Start the performance collector.

      if (writeData()) { // Write the collection to the user space.
        succeeded();
      } else {
        failed("Incorrect message received, " + collectorMessageID_);
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      workCollector(false, usName); // Stop the performance collector.
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: AS400 user space API write) Ensure that an AS/400 user space
   * API write is successful if the user space object has been closed.
   **/
  public void Var058(int runMode) {
    if (runMode == UNATTENDED) {
      notApplicable("Attended variation.");
      return;
    }

    String usName = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC";
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_, usName);
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Test User Space",
          "*USE");

      aUserSpace.close(); // close the user space.

      workCollector(true, usName); // Start the performance collector.
      writeData(); // Write the collection to the user space.
      workCollector(false, usName); // Stop the performance collector.

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }

  /**
   * Method tested: write(byte[], int) Ensure that UserSpace.write is successful
   * after the user space is closed (same AS400 object).
   **/
  public void Var059() // $A1
  {
    UserSpace aUserSpace = null;

    try {
      aUserSpace = new UserSpace(systemObject_,
          "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USWRITE.USRSPC");
      aUserSpace.create(10240, true, " ", (byte) 0x00, "Write User Space",
          "*USE");

      aUserSpace.close(); // Close the user space.

      byte[] dataBuffer = { 0, 1, 2, 3, 4, 5, 6, 7 };
      aUserSpace.write(dataBuffer, 0);

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected exception occurred.");
    } finally {
      deleteUserSpace(aUserSpace);
    }
  }
}
