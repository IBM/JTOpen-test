///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceChgAttrTestcase.java
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
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.UserSpace;

import test.Testcase;
import test.UserSpaceTest;

/**
 Test file and directory create/delete operations on UserSpace.
 **/
public class UserSpaceChgAttrTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpaceChgAttrTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserSpaceTest.main(newArgs); 
   }
    private AS400 usSystem_;
    private String userSpacePathName_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC";
    private String pre_existingUserSpace_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC";
    private String authlib_ = UserSpaceTest.COLLECTION+"AU";
    private String testauth = UserSpaceTest.COLLECTION+"TA"; 
    private String usName1 = "/QSYS.LIB/"+authlib_+".LIB/USCHG1.USRSPC";
    private String usName2 = "/QSYS.LIB/"+authlib_+".LIB/USCHG2.USRSPC";
    private String usName3 = "/QSYS.LIB/"+authlib_+".LIB/USCHG3.USRSPC";
    private String usName4 = "/QSYS.LIB/"+authlib_+".LIB/USCHG4.USRSPC";
    private String usName5 = "/QSYS.LIB/"+authlib_+".LIB/USCHG5.USRSPC";
    private byte pre_existingByteValue_ = 0x00;
    private int pre_existingLengthValue_ = 11000;

    private String ustestUserID = null;


    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
      userSpacePathName_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC";
      pre_existingUserSpace_ = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC";
      authlib_ = UserSpaceTest.COLLECTION+"AU"; 
      usName1 = "/QSYS.LIB/"+authlib_+".LIB/USCHG1.USRSPC";
      usName2 = "/QSYS.LIB/"+authlib_+".LIB/USCHG2.USRSPC";
      usName3 = "/QSYS.LIB/"+authlib_+".LIB/USCHG3.USRSPC";
      usName4 = "/QSYS.LIB/"+authlib_+".LIB/USCHG4.USRSPC";
      usName5 = "/QSYS.LIB/"+authlib_+".LIB/USCHG5.USRSPC";
        // Create needed AS400 objects.
        testInit();
        ustestUserID = " USER(" + systemObject_.getUserId() + ") ";
    }

    void deleteUserSpace(UserSpace aUserSpace)
    {
        try
        {
            aUserSpace.delete();
        }
        catch (Exception e)
        {
            failed(e, "Delete failed.");
        }
    }

    void testInit() throws Exception
    {
        // Create a user space to use test on an pre-existing user space.
        UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
        aUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "CRTDLT UserSpace", "*ALL");
        aUserSpace.close();
        systemObject_.disconnectService(AS400.FILE);

	deleteLibrary(""+testauth+""); 
        cmdRun("DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
        cmdRun("QSYS/CRTAUTL AUTL(USAUTHLIST) AUT(*EXCLUDE)");
        cmdRun("CRTLIB LIB("+testauth+") AUT(USAUTHLIST)");

        UserSpace bUserSpace = new UserSpace(pwrSys_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");
        bUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "chg attrs", "*ALL");

        // Create an AS400 object to be used with USTEST user profile.
        usSystem_ = new AS400(systemObject_);

        // do setup for authorization tests (cannot do this
        // 'in-line' because when running on the AS/400 the
        // file server job does not end in time.  This causes
        // the grtobjauth command to fail).
	deleteLibrary(""+authlib_+""); 
        cmdRun("CRTLIB LIB("+authlib_+")");

        String command = "GRTOBJAUT OBJ("+authlib_+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*USE) ";   
        boolean success =  cmdRun(command);
        if (!success) { 
            System.out.println("Command Failed "+command ); 
        } else {
          System.out.println("SetupUSAuthority(): Command worked "+command ); 
        }

        // create user spaces needed for misc variations
        cmdCreateUS("CRTUSRSPC USRSPC("+authlib_+"/USCHG1) SIZE(11000) AUT(*ALL) TEXT(UserSpaceChgAttrTestcase) REPLACE(*YES)", "/QSYS.LIB/"+authlib_+".LIB/USCHG1.USRSPC");
        cmdCreateUS("CRTUSRSPC USRSPC("+authlib_+"/USCHG2) SIZE(11000) AUT(*ALL) TEXT(UserSpaceChgAttrTestcase) REPLACE(*YES)", "/QSYS.LIB/"+authlib_+".LIB/USCHG2.USRSPC");
        cmdCreateUS("CRTUSRSPC USRSPC("+authlib_+"/USCHG3) SIZE(11000) AUT(*ALL) TEXT(UserSpaceChgAttrTestcase) REPLACE(*YES)", "/QSYS.LIB/"+authlib_+".LIB/USCHG3.USRSPC");
        cmdCreateUS("CRTUSRSPC USRSPC("+authlib_+"/USCHG4) SIZE(11000) AUT(*ALL) TEXT(UserSpaceChgAttrTestcase) REPLACE(*YES)", "/QSYS.LIB/"+authlib_+".LIB/USCHG4.USRSPC");
        cmdCreateUS("CRTUSRSPC USRSPC("+authlib_+"/USCHG5) SIZE(11000) AUT(*ALL) TEXT(UserSpaceChgAttrTestcase) REPLACE(*YES)", "/QSYS.LIB/"+authlib_+".LIB/USCHG5.USRSPC");

        pwrSys_.disconnectAllServices();
    }

    /*
     Create a User Space.  Will create a user space using command call if possible, otherwise
     it uses the user space implementation.
     */
    void cmdCreateUS(String cmdString, String spaceName)
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        try
        {
            if (cmd.run(cmdString) != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IOException"))
            {
                String msg = e.getMessage();

                if ((msg.indexOf("CPD0030") > -1) &&
                    (msg.indexOf("CRTUSRSPC") > -1))
                    // if (e.getMessage().equalsIgnoreCase("CPD0030 Command CRTUSRSPC in library *LIBL not found."))
                {
                    UserSpace aUSpace = new UserSpace(pwrSys_, spaceName);
                    try
                    {
                        aUSpace.create(11000, true, " ", (byte)0x00, "USCRTDLT test", "*ALL");
                    }
                    catch (Exception x)
                    {
                        System.out.println("Setup - create user space - failed." + x);
                    }
                    pwrSys_.disconnectAllServices();
                }
                else
                {
                    AS400Message[] messageList = cmd.getMessageList();
                    System.out.println(messageList[0].toString());
                }
            }
            else
            {
                AS400Message[] messageList = cmd.getMessageList();
                System.out.println(messageList[0].toString());
            }
        }
    }

    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
	deleteLibrary(""+testauth+""); 
        cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
	deleteLibrary(""+authlib_+""); 
        pwrSys_.disconnectAllServices();
    }

    /**
     Method tested: setAutoExtendible()
     Ensure that setAutoExtendible(true) allows the User Space to auto extend.
     **/
    public void Var001()
    {
        UserSpace aUserSpace = null;
        try
        {
            // Connect to the AS400.
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
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
    public void Var002()
    {
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 3584, false, " ", (byte)0x00, "CHGATTR User Space", "*EXCLUDE");
            int initialSize = aUserSpace.getLength();

            // Set the User Space as auto extendible.
            aUserSpace.setAutoExtendible(false);

            byte[] inputBuffer = new byte[4096];

            for (int i=0; i<4086; i++)
                inputBuffer[i] = 1;

            if (isNative_)
            {
                try
                {
                    int expected = 4096;
                    aUserSpace.write(inputBuffer, 3584);
		    if (expected == aUserSpace.getLength())
			failed("Unexpected results occurred."+initialSize);
		    else
			succeeded();

                }
                catch (Exception e)
                {
                    assertExceptionStartsWith(e, "AS400Exception", "CPD3C14 ", ErrorCompletingRequestException.AS400_ERROR);
                }
            }
            else
            {
                // Write to User Space to test if it auto extends.

                int expected = 4096;
                aUserSpace.write(inputBuffer, 3584);
                if (expected == aUserSpace.getLength())
                    failed("Unexpected results occurred.");
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }

        deleteUserSpace(aUserSpace);

    }

    /**
     Method tested: setInitialValue()
     Verify setInitialValue(byte) uses the initial byte specified if used before a connection has been made.
     **/
    public void Var003()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);

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
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          systemObject_.disconnectAllServices();
        }
    }

    /**
     Method tested: setInitialValue()
     Verify that setInitialValue(byte) uses the initial byte specified.
     **/
    public void Var004()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");
            byte expectedByte = (byte)0x16;
            aUserSpace.setInitialValue(expectedByte);

            byte testByte = aUserSpace.getInitialValue();
            if (testByte == expectedByte)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setInitialValue()
     Verify that IOException is thrown if the user space does not exist.
     **/
    public void Var005()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.setInitialValue((byte)0x01);

            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: setInitialValue()
     Verify that IOException is thrown if the library does not exist.
     **/
    public void Var006()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC");

        try
        {
            aUserSpace.setInitialValue((byte)0x01);

            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }

    }

    /**
     Method tested: setInitialValue()
     Verify that setInitialValue(byte) can be called after a User Space write.
     **/
    public void Var007()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

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
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setInitialValue()
     Verify that IOException is thrown if the user is not authorized to the User Space object.
     **/
    public void Var008()
    {
        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {
            if (cmd.run("GRTOBJAUT OBJ("+authlib_+"/USCHG1) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }

            // byte[] data = new byte[20];

            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, usName1);
                aUserSpace.setInitialValue((byte)0x01);
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, usName1);
                aUserSpace.setInitialValue((byte)0x01);
            }

            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+authlib_+".LIB/USCHG1.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
    }


    /**
     Method tested: setInitialValue()
     Verify that IOException is thrown if the user is not authorized to the library.
     **/
    public void Var009()
    {
        try
        {
            UserSpace bUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");

            bUserSpace.setInitialValue((byte)0x01);

            failed("No exception occurred (make sure -uid on command line does not have authority to "+testauth+".lib).");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }

        // cleanup
        systemObject_.disconnectAllServices();
    }


    /**
     Method tested: setLength()
     Ensure that IOException is thrown if the length parameter is less than 1.
     **/
    public void Var010()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

            // Attempt to reset the system
            AS400 newAS400 = new AS400();
            newAS400.close(); 
            aUserSpace.setLength(0);
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
     Ensure that an IOException is thrown if the length parameter is greater than MAX.
     **/
    public void Var011()
    {

        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

            // Attempt to reset the system
            AS400 newAS400 = new AS400();
            newAS400.close(); 
            aUserSpace.setLength(16776705);
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
    public void Var012()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 4096, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");
            int initialSize = aUserSpace.getLength();

            // Set the User Space as auto extendible.
            int expectedLength = 4097;
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
            if (8192 == aUserSpace.getLength())
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
    public void Var013()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
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
            if (4096 == aUserSpace.getLength())
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
    public void Var014()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 4097, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");
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

            if (8192 == aUserSpace.getLength())
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
     Ensure that the new length matches the expected length when decreasing within a 4096 byte page size.
     **/
    public void Var015()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 8191, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");
            int initialSize = aUserSpace.getLength();

            // Set the User Space as auto extendible.
            aUserSpace.setLength(7681);

            int expected = 8192;
            if (expected == aUserSpace.getLength())
                succeeded();
            else
                failed("Wrong extend info." + initialSize + ":" + expected + ":" + aUserSpace.getLength());
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
    public void Var016()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);

            int expectedLength = 18000;
            aUserSpace.setLength(expectedLength);

            int testLength = aUserSpace.getLength();

            // calculate expectedLength
            int factor = 1;
            factor += expectedLength / 4096;
            if ( expectedLength > (4096 * factor - 512) )
                factor++;
            expectedLength = 4096 * factor - 512;

            if (testLength == 20480)
            {
                succeeded();
                // set the original length value
                pre_existingLengthValue_ = expectedLength;
            }
            else
                failed("Unexpected results occurred.");

            aUserSpace.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          systemObject_.disconnectAllServices();
        }
    }

    /**
     Method tested: setLength()
     Verify that IOException is thrown if the User Space does not exist.
     **/
    public void Var017()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.setLength(24000);

            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: setLength()
     Verify that IOException is thrown if the library does not exist.
     **/
    public void Var018()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC");

        try
        {
            aUserSpace.setLength(24000);

            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }

    }

    /**
     Method tested: setLength()
     Verify that setLength(int) can be called after a User Space write.
     **/
    public void Var019()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

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

            if (49152 == testLength)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setLength()
     Verify that IOException is thrown if the user is not authorized to the User Space object.
     **/
    public void Var020()
    {
        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {
            if (cmd.run("GRTOBJAUT OBJ("+authlib_+"/USCHG2) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }

            // byte[] data = new byte[20];

            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, usName2);
                aUserSpace.setLength(100000);
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, usName2);
                aUserSpace.setLength(100000);
            }


            failed("Expected exception did not occur (make sure -uid on command line does not have allobj authority).");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+authlib_+".LIB/USCHG2.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: setLength()
     Verify that IOException is thrown if the user is not authorized to the library.
     **/
    public void Var021()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");

            aUserSpace.setLength(15000);

            failed("No exception occurred (make sure -uid on command line is not authorized to "+testauth+".lib).");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }
        // cleanup
        systemObject_.disconnectAllServices();
    }

    /**
     Method tested: setPath()
     Ensure that setPath(String) throws NullPointerException if path is null.
     **/
    public void Var022()
    {
        String nullPath = null;

        try
        {

            UserSpace aUserSpace = new UserSpace();

            // Attempt to set the path to null.
            aUserSpace.setPath(nullPath);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }

    }

    /**
     Method tested: setPath()
     Ensure that setPath(String) throws ExtendedIllegalStateException if connection has already been established.
     **/
    public void Var023()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

            // Attempt to reset the path.
            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWPATH.USRSPC");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException",
                                    "path", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setPath()
     Ensure that setPath(String) sets the Path to the expected String.
     **/
    public void Var024()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");

            // Set the path information.
            String expected = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USPATH.USRSPC";
            aUserSpace.setPath(expected);

            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

            // Get the path information
            if (expected == aUserSpace.getPath())
                succeeded();
            else
                failed("Wrong path info.");
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setSystem()
     Ensure that setSystem(AS400) throws NullPointerException if system is null.
     **/
    public void Var025()
    {
        try
        {

            AS400 as400 = null;
            UserSpace aUserSpace = new UserSpace();

            // Attempt to set the system.
            aUserSpace.setSystem(as400);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }

    }

    /**
     Method tested: setSystem()
     Ensure that setSystem(AS400) throws ExtendedIllegalStateException if connection has already been established.
     **/
    public void Var026()
    {
        UserSpace aUserSpace = null;

        try
        {

            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

            // Attempt to reset the system
            AS400 newAS400 = new AS400();
            aUserSpace.setSystem(newAS400);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException",
                                    "system", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setSystem()
     Ensure that setSystem(AS400) sets the system to the expected AS400.
     **/
    public void Var027()
    {
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");
            aUserSpace.setSystem(systemObject_);

            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "1st User Space", "*EXCLUDE");

            String expected = systemObject_.getSystemName();

            // Get the AS400 information
            AS400 theAS400 = aUserSpace.getSystem();

            if (expected == theAS400.getSystemName())
                succeeded();
            else
                failed("Wrong system info.");
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: getInitialValue()
     Verify getInitialValue() if used before a connection has been made.
     **/
    public void Var028()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/GETUS.USRSPC");

        try
        {
            byte expectedValue = (byte)0x02;
            aUserSpace.create(11000, true, " ", expectedValue, "CHGATTR test", "*ALL");

            byte testByte = aUserSpace.getInitialValue();

            if (testByte == expectedValue)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        try
        {
            aUserSpace.delete();
        }
        catch (Exception e)
        {
            System.out.println("Cleanup failed." + e);
        }

        systemObject_.disconnectAllServices();
    }

    /**
     Method tested: getLength()
     Verify getLength() if used before a connection has been made.
     **/
    public void Var029()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/GETUS.USRSPC");

        try
        {
            int expectedLength = 14000;
            aUserSpace.create(expectedLength, true, " ", (byte)0x00, "CHGATTR test", "*ALL");

            int testLength = aUserSpace.getLength();

            // calculate expectedLength
            int factor = 1;
            factor += expectedLength / 4096;
            if ( expectedLength > (4096 * factor - 512) )
                factor++;
            expectedLength = 4096 * factor - 512;
            if (testLength == 16384)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        try
        {
            aUserSpace.delete();
        }
        catch (Exception e)
        {
            System.out.println("Cleanup failed." + e);
        }
        systemObject_.disconnectAllServices();
    }


    /**
     Method tested: getInitialValue()
     Verify that IOException is thrown if the user space does not exist.
     **/
    public void Var030()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            byte testByte = aUserSpace.getInitialValue();

            failed("No exception occurred."+testByte);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: getInitialValue()
     Verify that IOException is thrown if the library does not exist.
     **/
    public void Var031()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC");

        try
        {
            byte testByte = aUserSpace.getInitialValue();

            failed("No exception occurred."+testByte);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }

    }

    /**
     Method tested: getInitialValue()
     Verify that getInitialValue() can be called after a User Space write.
     **/
    public void Var032()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");

            byte[] writeBuffer = { 9, 8, 7, 6, 5 };
            aUserSpace.write(writeBuffer, 0);

            byte testByte = aUserSpace.getInitialValue();

            assertCondition(true, "testByte is "+testByte);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: getInitialValue()
     Verify that IOException is thrown if the user is not authorized to the User Space object.
     **/
    public void Var033()
    {

        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {
            if (cmd.run("GRTOBJAUT OBJ("+authlib_+"/USCHG3) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }

            // byte[] data = new byte[20];
            byte testByte; 
            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, usName3);
                testByte = aUserSpace.getInitialValue();
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, usName3);
                testByte = aUserSpace.getInitialValue();
            }

            failed("Expected exception did not occur (make sure -uid on command line does not have allobj authority)."+testByte);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+authlib_+".LIB/USCHG3.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: getInitialValue()
     Verify that IOException is thrown if the user is not authorized to the library.
     **/
    public void Var034()
    {
        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {

            // Remove library authority
            cmd.run("RVKOBJAUT OBJ("+testauth+"/*ALL) OBJTYPE(*LIB) " + ustestUserID + " AUT(*EXCLUDE)");
            byte testByte; 
            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");
                testByte = aUserSpace.getInitialValue();
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");
                testByte = aUserSpace.getInitialValue();
            }


            failed("No exception occurred (make sure -uid on command line does not have allobj authority)."+testByte);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }
        usSystem_.disconnectAllServices();
    }
    /**
     Method tested: getLength()
     Verify that IOException is thrown if the User Space does not exist.
     on a non-existing User Space.
     **/
    public void Var035()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            int testLength = aUserSpace.getLength();

            failed("No exception occurred."+testLength);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: getLength()
     Verify that IOException is thrown if the library does not exist.
     **/
    public void Var036()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC");

        try
        {
            int testLength = aUserSpace.getLength();

            failed("No exception occurred."+testLength);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: getLength()
     Verify that getLength() can be called after a User Space write.
     **/
    public void Var037()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

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
            if (testLength == 12288)
                succeeded();
            else
                failed("Unexpected results occurred.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: getLength()
     Verify that IOException is thrown if the user is not authorized to the User Space object.
     **/
    public void Var038()
    {
        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {
            if (cmd.run("GRTOBJAUT OBJ("+authlib_+"/USCHG4) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }

            // byte[] data = new byte[20];

            int testLength; 
            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, usName4);
                testLength = aUserSpace.getLength();
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, usName4);
                testLength = aUserSpace.getLength();
            }

            failed("Expected exception did not occur (make sure -uid on command line does not have allobj authority) ."+testLength);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+authlib_+".LIB/USCHG4.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: getLength()
     Verify that IOException is thrown if the user is not authorized to the library.
     **/
    public void Var039()
    {
        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {

            // Remove library authority
            cmd.run("RVKOBJAUT OBJ("+testauth+"/*ALL) OBJTYPE(*LIB) " + ustestUserID + " AUT(*EXCLUDE)");

            int testLength; 
            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");
                testLength = aUserSpace.getLength();
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");
                testLength = aUserSpace.getLength();
            }

            failed("No exception occurred (make sure -uid on command line does not have allobj authority)."+testLength);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }
        usSystem_.disconnectAllServices();
    }

    /**
     Method tested: getName()
     Ensure getName() returns null if null constructor UserSpace() is used to create the object.
     **/
    public void Var040()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();

            if (aUserSpace.getName().equals(""))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getPath()
     Ensure getPath() returns null if null constructor UserSpace() is used to create the object.
     **/
    public void Var041()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();

            if (aUserSpace.getPath().equals(""))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getSystem()
     Ensure getSystem() returns null if null constructor UserSpace() is used to create the object.
     **/
    public void Var042()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();

            if (aUserSpace.getSystem() == null)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getName()
     Verify getName() returns the expected user space name.
     **/
    public void Var043()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USNAME.USRSPC");

            String expected = "USNAME";

            if (aUserSpace.getName().equals(expected))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getPath()
     Verify getPath() returns the expected AS400.
     **/
    public void Var044()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            if (aUserSpace.getPath().equals(userSpacePathName_))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getSystem()
     Verify getSystem() returns the expected AS400.
     **/
    public void Var045()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            String expected = systemObject_.getSystemName();

            AS400 testSystem = aUserSpace.getSystem();

            if (testSystem.getSystemName().equals(expected))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }
    /**
     Method tested: setName()
     Verify getName() returns the expected user space name.
     **/
    public void Var046()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWNAME.USRSPC");
            String expected = "NEWNAME";

            if (aUserSpace.getName().equals(expected))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPath()
     Verify getPath() returns the expected AS400.
     **/
    public void Var047()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            String expected = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWNAME.USRSPC";
            aUserSpace.setPath(expected);

            if (aUserSpace.getPath().equals(expected))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getSystem()
     Verify getSystem() returns the expected AS400.
     **/
    public void Var048()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

            aUserSpace.setSystem(systemObject_);

            String expected = systemObject_.getSystemName();

            AS400 testSystem = aUserSpace.getSystem();

            if (testSystem.getSystemName().equals(expected))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setAutoExtendible()
     Verify setAutoExtendible(boolean) if used before a connection has been made.
     **/
    public void Var049()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);

            boolean expected = true;

            aUserSpace.setAutoExtendible(expected);

            boolean test = aUserSpace.isAutoExtendible();

            if (test == expected)
                succeeded();
            else
                failed("Unexpected results occurred.");
            aUserSpace.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          systemObject_.disconnectAllServices();
        }
    }

    /**
     Method tested: setAutoExtendible()
     Verify that setAutoExtendible(boolean) uses the value specified.
     **/
    public void Var050()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");
            boolean expected = false;
            aUserSpace.setAutoExtendible(expected);

            boolean test = aUserSpace.isAutoExtendible();
            if (test == expected)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setAutoExtendible()
     Verify that IOException is thrown if the User Space does not exist.
     **/
    public void Var051()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.setAutoExtendible(true);

            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: setAutoExtendible()
     Verify that IOException is thrown if the library does not exist.
     **/
    public void Var052()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC");

        try
        {
            aUserSpace.setAutoExtendible(false);

            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USATTRIB.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: setAutoExtendible()
     Verify that setAutoExtendible(boolean) can be called after a User Space write.
     **/
    public void Var053()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpacePathName_);

        try
        {
            aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");

            byte[] writeBuffer = new byte[20];
            int bytes = aUserSpace.read(writeBuffer, 0);

            boolean expected = true;
            aUserSpace.setAutoExtendible(expected);

            boolean test = aUserSpace.isAutoExtendible();

            if (test == expected)
                succeeded();
            else
                failed("Unexpected results occurred."+bytes);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: setAutoExtendible()
     Verify that IOException is thrown if the user is not authorized to the User Space object.
     **/
    public void Var054()
    {

        CommandCall cmd = new CommandCall(pwrSys_);

        try
        {
            if (cmd.run("GRTOBJAUT OBJ("+authlib_+"/USCHG5) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }

            // byte[] data = new byte[20];

            if (isNative_)
            {
                UserSpace aUserSpace = new UserSpace(systemObject_, usName5);
                aUserSpace.setAutoExtendible(true);
            }
            else
            {
                UserSpace aUserSpace = new UserSpace(usSystem_, usName5);
                aUserSpace.setAutoExtendible(true);
            }

            failed("Expected exception did not occur (make sure -uid on command line does not have allobj authority).");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+authlib_+".LIB/USCHG5.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: setAutoExtendible()
     Verify that IOException is thrown if the user is not authorized to the library.
     **/
    public void Var055()
    {
        try
        {
            UserSpace bUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC");

            bUserSpace.setAutoExtendible(true);

            failed("No exception occurred (make sure -uid on commmand line does not have allobj authority).");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testauth+".LIB/USATTRIB.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }
        // cleanup
        systemObject_.disconnectAllServices();
    }

    /**
     Method tested: setPath()
     Ensure that IllegalPathException is thrown for if an invalid QSYSObjectPathName is specified for the path.
     **/
    public void Var056()
    {
        try
        {

            UserSpace aUserSpace = new UserSpace();

            aUserSpace.setPath("USOBJECT.USRSPC");

            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "IllegalPathNameException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
    }
    /**
     Method tested: setPath()
     Ensure that the Path can be reset after creating a User Space object.
     **/
    public void Var057()
    {
        UserSpace aUserSpace = null;

        try
        {
            AS400 as400 = new AS400();
            aUserSpace = new UserSpace(as400, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/US1TEST.USRSPC");

            // reset the path to a different AS400.
            String expected = "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC";
            aUserSpace.setPath(expected);

            if (aUserSpace.getPath() == expected)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }
}
