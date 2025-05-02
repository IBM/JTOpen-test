///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceCrtDltTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.UserSpace;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.UserSpace;

import test.Testcase;
import test.UserSpaceTest;

import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ObjectDoesNotExistException;

/**
 Test file and directory create/delete operations on UserSpace.
 **/
public class UserSpaceCrtDltTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpaceCrtDltTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserSpaceTest.main(newArgs); 
   }
    private boolean setupUnauthorized_ = false;
    private boolean setupAuthority_ = false;
    private String testAuth = UserSpaceTest.COLLECTION+"TA"; 
    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        testAuth = UserSpaceTest.COLLECTION+"TA"; 
        // Create a user space to use to test for a pre-existing user space.
        UserSpace us = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC");
        us.create(11000, true, " ", (byte)0x00, "CRTDLT UserSpace", "*ALL");
        us.close();
    }

    // Cleanup user space that was used during testing.
    void deleteUserSpace(UserSpace us)
    {
        try
        {
            us.close();
            us.delete();
        }
        catch (Exception e)
        {
            output_.println("Cleanup failed:");
            e.printStackTrace(output_);
        }
    }

    // Create a library and user space that testcases will have NO authority to.
    private void setupUSUnauthorized()
    {
        try
        {
	    deleteLibrary(""+testAuth+""); 
            cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
            cmdRun("QSYS/CRTAUTL AUTL(USAUTHLIST) AUT(*EXCLUDE)");
            cmdRun("QSYS/CRTLIB LIB("+testAuth+") AUT(USAUTHLIST)");

            // Create a user space in the unauthorized library.
            UserSpace us = new UserSpace(pwrSys_, "/QSYS.LIB/"+testAuth+".LIB/USCREATE.USRSPC");
            us.create(11000, true, " ", (byte)0x00, "create UserSpace", "*ALL");
            us.close();

            setupUnauthorized_ = true;
        }
        catch (Exception e)
        {
            output_.println("Setup failed:");
            e.printStackTrace(output_);
        }
    }

    //Create a user space to be used in authority testcases.
    private void setupUSAuthority()
    {
        try
        {
	    deleteLibrary("USAUTHLIB"); 
            cmdRun("QSYS/CRTLIB LIB(USAUTHLIB)");
            /* grant access to library to all */ 
            String command = "QSYS/GRTOBJAUT OBJ(USAUTHLIB) OBJTYPE(*LIB) USER(*PUBLIC) AUT(*USE) ";   
            boolean success =  cmdRun(command);
            if (!success) { 
                System.out.println("Command Failed "+command ); 
            } else {
              System.out.println("SetupUSAuthority(): Command worked "+command ); 
            }

            UserSpace aUSpace = new UserSpace(pwrSys_, "/QSYS.LIB/USAUTHLIB.LIB/USCRTDLT1.USRSPC");
            aUSpace.create(10240, true, " ", (byte)0x00, "USCRTDLT test", "*ALL");
            aUSpace.close();

            setupAuthority_ = true;
        }
        catch (Exception e)
        {
            output_.println("Setup failed:");
            e.printStackTrace(output_);
        }
    }

    /**
     Cleanup objects created during testing.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
	deleteLibrary(""+testAuth+""); 
        cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
	deleteLibrary("USAUTHLIB"); 
    }

    /**
     Method tested: Ctor()
     Ensure that NullPointerException is thrown if the user space path is not set.
     **/
    public void Var001()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, null);
            failed("Exception didn't occur."+aUserSpace);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     Method tested: Ctor()
     Ensure that NullPointerException is thrown if the user space system is not set.
     **/
    public void Var002()
    {
        try
        {

            UserSpace aUserSpace = new UserSpace(null, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            failed("Exception didn't occur."+aUserSpace);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException exception is thrown if the length parameter is out of bounds.
     **/
    public void Var003()
    {

        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USCREATE.LIB/"+UserSpaceTest.COLLECTION+".USRSPC");
        try
        {
            aUserSpace.create(0, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException exception is thrown if the length parameter is out of bounds.
     **/
    public void Var004()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(17000000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that the user space is created if the length parameter is the maxUserSpaceSize.
     **/
    public void Var005()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(16776704, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          deleteUserSpace(aUserSpace);
        }
    }

    /**
     Method tested: create()
     Verify that the size of the user space matches the length parameter used during creation.
     **/
    public void Var006()
    {
        int initialSize = 12000;
        int tempSize;
        int predictedSize;
        int a = 1;
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            aUserSpace.create(initialSize, true, " ", (byte)0x00, "USCREATE test", "*ALL");

            // ***************************
            // expected size calculation
            //    On the AS400 the actual size of the user space is different.
            //    A user space is created to the nearest 4096K page size with 512K being used for page info.
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

            assertCondition(12288 == aUserSpace.getLength(), "Length value not expected."+predictedSize);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }

        // Delete the user space.
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: create()
     Ensure that the NullPointerException is thrown if the extendedAttribute parameter is null.
     **/
    public void Var007()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create(11000, true, null, (byte)0x00, "USCREATE test", "*ALL");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "extendedAttribute");
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the extendedAttribute parameter has more than 10 characters.
     **/
    public void Var008()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, "BADUserSpaceTest", (byte)0x00, "USCREATE", "*ALL");
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "extendedAttribute", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the extendedAttribute parameter has a length of 0.
     Nope.  As of v5r1 this is okay.
     **/
    public void Var009()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, "", (byte)0x00, "USCREATE", "*ALL");
            succeeded();
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Ensure that the NullPointerException is thrown if the textDescription parameter is null.
     **/
    public void Var010()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", (byte)0x00, null, "*ALL");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "textDescription");
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the textDescription parameter has length 0.
     Nope.  As of v5r1 this is okay
     **/
    public void Var011()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", (byte)0x00, "", "*ALL");
            succeeded();
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the textDescription parameter has more than 50 characters.
     **/
    public void Var012()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test variation test to see if the description has more than 50 characters", "*ALL");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that the NullPointerException is thrown if the authority parameter is null.
     **/
    public void Var013()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "authority");
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the authority parameter has length 0.
     **/
    public void Var014()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the authority parameter has more than 10 characters.
     **/
    public void Var015()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "AUTHORITYTEST");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "authority", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Verify that the *ALL public authority of the user space matches the parameter used during creation.
     **/
    public void Var016()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "USCREATE test", "*ALL");

            // Connect to the AS400 as a second user.
            AS400 usSystem_ = new AS400(systemObject_);
            UserSpace sameUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC") ;
            sameUserSpace.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Verify that the authorization list used for the user space matches the parameter used during creation.
     **/
    public void Var017()
    {
        // Create objects needed for testcase.
        if (!setupUnauthorized_) setupUSUnauthorized();
        try
        {
            UserSpace aUserSpace = new UserSpace(pwrSys_, "/QSYS.LIB/"+testAuth+".LIB/USAUTHCHK.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "USCREATE test", "USAUTHLIST");

            try
            {
                UserSpace sameUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testAuth+".LIB/USAUTHCHK.USRSPC") ;
                sameUserSpace.delete();
                failed("No exception occurred.  Make sure uid/pwd on command line does not have all object authority. ");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testAuth+".LIB/USAUTHCHK.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }

            // Cleanup.
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Verify that the Exception is thrown used for the user space if the authorization list
     parameter used during creation is invalid.
     **/
    public void Var018()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            // Create an authorization list.
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "USCREATE test", "USBADLIST");
            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIST.AUTL: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: create()
     Verify that the *CHANGE public authority of the user space matches the parameter used during creation.
     **/
    public void Var019()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(pwrSys_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, true, " ", (byte)0x00, "USCREATE test", "*CHANGE");

            // Connect to the AS400 as a second user.
            AS400 usSystem_ = new AS400(systemObject_);
            UserSpace sameUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC") ;
            try
            {
                sameUserSpace.delete();
                failed("No exception occurred.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
            // Cleanup.
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Verify that the *EXCLUDE public authority of the user space matches the parameter used during creation.
     **/
    public void Var020()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(pwrSys_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, true, " ", (byte)0x00, "USCREATE test", "*EXCLUDE");

            // Connect to the AS400 as a second user.
            AS400 usSystem_ = new AS400(systemObject_);
            UserSpace sameUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC") ;
            try
            {
                sameUserSpace.delete();
                failed("No exception occurred.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
            // Cleanup.
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Verify that the *USE public authority of the user space matches the parameter used during creation.
     **/
    public void Var021()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(pwrSys_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "USCREATE test", "*USE");

            // Connect to the AS400 as a second user.
            AS400 usSystem_ = new AS400(systemObject_);
            UserSpace sameUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            try
            {
                sameUserSpace.setLength(15000);
                failed("No exception occurred.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Verify that the *LIBCRTAUT public authority of the user space matches the parameter used during creation.
     **/
    public void Var022()
    {
        try
        {
            CommandCall cmd = new CommandCall(pwrSys_);

            // Create the authorization list.
            if (!cmd.run("QSYS/CRTAUTL AUTL(USAUTH) AUT(*USE)"))
            {
                output_.println("Setup failed:");
                AS400Message[] messageList = cmd.getMessageList();
                for (int i = 0; i  < messageList.length; ++i)
                {
                    output_.println("  " + messageList[i].getID() + " " + messageList[i].getText());
                }
            }
            if (!cmd.run("QSYS/CRTLIB LIB(USAUTH) AUT(USAUTH)"))
            {
                output_.println("Setup failed:");
                AS400Message[] messageList = cmd.getMessageList();
                for (int i = 0; i  < messageList.length; ++i)
                {
                    output_.println("  " + messageList[i].getID() + " " + messageList[i].getText());
                }
            }

            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USAUTH.LIB/USCREATE.USRSPC");
            try
            {
                aUserSpace.create("*DEFAULT", 11776, false, " ", (byte)0x00, "USCREATE test", "*LIBCRTAUT");
                failed("No exception occurred.  Make sure uid/pwd on command line does not have all object authority.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/USAUTH.LIB/USCREATE.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
            }
	    deleteLibrary(cmd, "USAUTH");

            if (!cmd.run("QSYS/DLTAUTL AUTL(USAUTH)"))
            {
                output_.println("Cleanup failed:");
                AS400Message[] messageList = cmd.getMessageList();
                for (int i = 0; i  < messageList.length; ++i)
                {
                    output_.println("  " + messageList[i].getID() + " " + messageList[i].getText());
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Ensure that IOException is thrown if the authority parameter specified is invalid.
     **/
    public void Var023()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "INVALID");
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/INVALID.AUTL: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: create()
     Ensure that the user space uses the InitialValue specified during user space creation.
     **/
    public void Var024()
    {
        byte expected = 0x00;
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create(11000, true, " ", expected, "USCREATE test", "*ALL");
            byte checkValue = aUserSpace.getInitialValue();

            assertCondition(expected == checkValue, "Initial value incorrect.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: create()
     Ensure that the NullPointerException is thrown if the user space domain parameter is null.
     **/
    public void Var025()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create(null, 11000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            failed("No exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "domain");
        }
    }

    /**
     Method tested: create()
     Ensure that the ExtendedIllegalArgumentException is thrown if the domain parameter has more than 10 characters.
     **/
    public void Var026()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
        try
        {
            aUserSpace.create("TOOLONGDOMAIN", 11000, true, " ", (byte)0x00, "USCREATE test", "AUTHORITYTEST");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "domain (TOOLONGDOMAIN): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that ExtendedIllegalArgumentException is thrown if the domain parameter specified is invalid.
     **/
    public void Var027()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create("INVALID", 11000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "domain (INVALID): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }

    /**
     Method tested: create()
     Ensure that an IOException is thrown if replace = false was specified during the user space creation of an existing user space.
     **/
    public void Var028()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/PREEXIST.USRSPC");

        try
        {
            aUserSpace.create(11000, false, " ", (byte)0x00, "USCREATE test", "*ALL");
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF9870 ", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: create()
     Ensure that a new user space is created when replace = true is specified.
     **/
    public void Var029()
    {

        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            int original = aUserSpace.getLength();
            // Replace.
            aUserSpace.create(30000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            int newSize = aUserSpace.getLength();

            assertCondition(newSize > original, "Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: create()
     Ensure that IOException is thrown if existing user space object attempts to create a new user space.
     **/
    public void Var030()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            aUserSpace.create(11000, false, " ", (byte)0x00, "USCREATE test", "*ALL");

            // Replace
            aUserSpace.create(16000, true, " ", (byte)0x00, "USCREATE test", "*ALL");

            succeeded();
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Ensure that the user space is created if replace = false is specified on an non-existing user space.
     **/
    public void Var031()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create(11000, false, " ", (byte)0x00, "USCREATE test", "*ALL");
            succeeded();
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Ensure that IOException is thrown if the user does not have authority to the library.
     **/
    public void Var032()
    {
        // Create objects needed for testcase.
        if (!setupUnauthorized_) setupUSUnauthorized();

        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testAuth+".LIB/USCREATE.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
            failed("No exception occurred.  Make sure uid/pwd on command line does not have all object authority.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testAuth+".LIB/USCREATE.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: create()
     Ensure that IOException is thrown if the library does not exist.
     **/
    public void Var033()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USCREATE.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USCREATE test", "*ALL");
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USCREATE.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: delete()
     Ensure that IOException is thrown with CPF2105 if UserSpace.delete() is called on a non-existing user space.
     **/
    public void Var034()
    {
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
            aUserSpace.delete();
            aUserSpace.delete();
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: delete()
     Ensure that IOException is thrown with CPF2110 if UserSpace.delete() is called on a non-existing library.
     **/
    public void Var035()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USDELETE.USRSPC");
            aUserSpace.delete();
            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USDELETE.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: delete()
     Ensure that true is returned if UserSpace.delete() is called on an existing user space.
     **/
    public void Var036()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
            aUserSpace.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }
    }

    /**
     Method tested: delete()
     Ensure that true is returned if UserSpace.delete() is called on an existing user space after a write request.
     **/
    public void Var037()
    {
        try
        {

            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");

            aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
            byte[] writeBuffer = { 0, 1, 2, 3, 4 };
            aUserSpace.write(writeBuffer, 0);

            aUserSpace.delete();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: delete()
     Ensure that true is returned if UserSpace.delete() is called on an existing user space after a get request.
     **/
    public void Var038()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");

            aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");
            int aLength = aUserSpace.getLength();

            aUserSpace.delete();

            assertCondition(true, "aLength="+aLength);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: delete()
     Validate that the delete request if a connection has not been made. (does not apply in native case)
     **/
    public void Var039()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(pwrSys_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USDELETE test", "*ALL");

            // Create a second user space object that has not connected.
            UserSpace bUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USDELETE.USRSPC");
            bUserSpace.delete();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: delete()
     Ensure that NullPointerException is thrown if the user space path is not set during delete.
     **/
    public void Var040()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();
            aUserSpace.setSystem(systemObject_);

            aUserSpace.delete();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Method tested: delete()
     Ensure that NullPointerException is thrown if the user space system is not set during delete.
     **/
    public void Var041()
    {
        try
        {
            // Connect to the AS400.  This connection will be used for all variations.
            UserSpace aUserSpace = new UserSpace();
            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");

            aUserSpace.delete();
            failed("Exception didn't occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Method tested: delete()
     Ensure that IOException is thrown with CPF2189 if UserSpace.delete() is called and the user does not have authorization to the object.
     **/
    public void Var042()
    {
        // Create objects needed for testcase.
        if (!setupAuthority_) setupUSAuthority();

        CommandCall cmd = new CommandCall(pwrSys_);
        try
        {
            if(!cmd.run("QSYS/GRTOBJAUT OBJ(USAUTHLIB/USCRTDLT1) OBJTYPE(*USRSPC) USER(" + systemObject_.getUserId() + ") AUT(*EXCLUDE)"))
            {
                output_.println("Setup failed:");
                AS400Message[] messageList = cmd.getMessageList();
                for (int i = 0; i  < messageList.length; ++i)
                {
                    output_.println("  " + messageList[i].getID() + " " + messageList[i].getText());
                }
            }

            AS400 usSystem_ = new AS400(systemObject_);
            UserSpace aUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/USAUTHLIB.LIB/USCRTDLT1.USRSPC");
            aUserSpace.delete();
            failed("Expected exception did not occur (make sure -uid on command line is not authorized to usauthlib.lib/uscrtdlt1.usrspc).");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/USAUTHLIB.LIB/USCRTDLT1.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: delete()
     Ensure that IOException is thrown if the user does not have authority to the library.
     **/
    public void Var043()
    {
        // Create objects needed for testcase.
        if (!setupUnauthorized_) setupUSUnauthorized();

        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+testAuth+".LIB/USCREATE.USRSPC");
            aUserSpace.delete();
            failed("No exception occurred.  Make sure uid/pwd on command line does not have authority to "+testAuth+".LIB.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+testAuth+".LIB/USCREATE.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be set after creating a user space with the default constructor.
     **/
    public void Var044()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();

            AS400 as400 = new AS400();
            aUserSpace.setSystem(as400);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be set after creating a user space with the default constructor.
     **/
    public void Var045()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();
            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be reset after creating a user space with the default constructor.
     **/
    public void Var046()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();

            AS400 as400 = new AS400();
            aUserSpace.setSystem(as400);

            // Reset system to a different object.
            AS400 newAS400 = new AS400();
            aUserSpace.setSystem(newAS400);

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be reset after creating a user space with the default constructor.
     **/
    public void Var047()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace();

            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");

            // Reset the path to a different string.
            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be reset after creating a user space object.
     **/
    public void Var048()
    {
        try
        {

            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(as400, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");

            // Reset system to a different object.
            AS400 newAS400 = new AS400();
            aUserSpace.setSystem(newAS400);

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be reset after creating a user space object.
     **/
    public void Var049()
    {
        try
        {
            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(as400, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");

            // Reset the path to a different string.
            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be reset after a AS400 signon.
     **/
    public void Var050()
    {
        try
        {

            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");

            // Reset system to a different object.
            AS400 newAS400 = new AS400();
            aUserSpace.setSystem(newAS400);
            newAS400.close(); 
            as400.close(); 
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be reset after a signon.
     **/
    public void Var051()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USTEST.USRSPC");

            // Reset the path to a different string.
            aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System cannot be reset after a connection.
     **/
    public void Var052()
    {
        try
        {
            AS400 as400 = new AS400();
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USSET.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USSET test", "*ALL");

            // Reset system to a different AS400.
            AS400 newAS400 = new AS400();
            try
            {
                aUserSpace.setSystem(newAS400);
                failed("Exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
            }
            deleteUserSpace(aUserSpace);
            as400.close();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path cannot be reset after a connection.
     **/
    public void Var053()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USSET.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "USSET", "*ALL");

            // Reset the path to a different String.
            try
            {
                aUserSpace.setPath("/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NEWUS.USRSPC");
                failed("Exception did not occur.");
            }
            catch (Exception e)
            {
                assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
            }
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: ctor()
     Ensure that IllegalPathException is thrown for UserSpace(AS400, path) if an invalid QSYSObjectPathName is specified for the path.
     **/
    public void Var054()
    {
        try
        {

            UserSpace aUserSpace = new UserSpace(systemObject_, "USOBJECT.USRSPC");
            failed("Exception did not occur."+aUserSpace);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "IllegalPathNameException");
        }
    }

    /**
     Method tested: create()
     Verify that the extendedAttribute parameter is what is expected.
     Note: This is an attended testcase.
     **/
    public void Var055(int runMode)
    {
        if (runMode == UNATTENDED)
        {
            notApplicable("Attended variation.");
            return;
        }

        try
        {
            String expected = "test";
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            aUserSpace.create(11000, true, expected, (byte)0x00, "NLSUSTEST", "*ALL");

            System.out.println("Does the extendedAttribute: (TEST) equal that of the user space in /QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC (Y/N)? (WRKLIB "+UserSpaceTest.COLLECTION+")");
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader inBuf = new BufferedReader(r);
            String resp = inBuf.readLine();
            assertCondition(resp.equalsIgnoreCase("Y"), "Verification of the user space extendedAttribute failed.");
            // Cleanup user space.
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: create()
     Verify that the textDescription parameter is what is expected.
     This is an attended testcase.
     **/
    public void Var056(int runMode)
    {
        if (runMode == UNATTENDED)
        {
            notApplicable("Attended variation.");
            return;
        }
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            String expected = "test description";

            aUserSpace.create(11000, true, " ", (byte)0x00, expected, "*ALL");

            System.out.println("Does the textDescription: (" + expected + ") equal that of the user space in /QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC (Y/N)? (WRKLIB "+UserSpaceTest.COLLECTION+")");
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader inBuf = new BufferedReader(r);
            String resp = inBuf.readLine();
            assertCondition(resp.equalsIgnoreCase("Y"), "Verification of the user space textDescription failed.");
            // Cleanup user space.
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: close()
     Ensure that the user space can be closed.
     **/
    public void Var057()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

        try
        {
            aUserSpace.create(10240, true, " ", (byte)0x00, "USCLOSE test", "*ALL");
            // Close the user space.
            aUserSpace.close();
            succeeded();
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: close()
     Verify that an Exception is thrown on a UserSpace.close if the user space is not open.
     **/
    public void Var058()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            // Close the user space.
            aUserSpace.close();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: close()
     Verify that an Exception is thrown on a UserSpace.close if the user space has been deleted.
     **/
    public void Var059()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            aUserSpace.create(10240, true, " ", (byte)0x00, "USCLOSE test", "*ALL");
            aUserSpace.close();

            aUserSpace.delete();

            aUserSpace.close();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: delete()
     Ensure that the user space can be deleted after being closed.
     **/
    public void Var060()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

        try
        {
            aUserSpace.create(10240, true, " ", (byte)0x00, "USDELETE test", "*ALL");
            // Close the user space.
            aUserSpace.close();
            // Delete the user space.
            aUserSpace.delete();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: close()
     Verify that an Exception is thrown on a UserSpace.close if the AS400.FILE service has been disconnected.
     **/
    public void Var061()
    {
        try
        {
            UserSpace aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            // Created same userspace with different AS400 object so that cleanup(deleteUserSpace() would work without throwing a disconnected exception.
            AS400 usSystem_ = new AS400(systemObject_);
            UserSpace sameUserSpace = new UserSpace(usSystem_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            aUserSpace.create(10240, true, " ", (byte)0x00, "USCLOSE test", "*ALL");

            // Disconnect FILE server.
            systemObject_.disconnectService(AS400.FILE);

            aUserSpace.close();

            succeeded();
            deleteUserSpace(sameUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    // Test exists.
    public void Var062()
    {
        try
        {
            UserSpace aUserSpace  = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");
            UserSpace aUserSpace2 = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/USCREATE.USRSPC");

            UserSpace us1 = new UserSpace(systemObject_, "/QSYS.LIB/NOLIBXX.LIB/USWRITE.USRSPC");
            us1.setMustUseProgramCall(true);
            if (us1.exists())
            {
                failed("exists() returned true when library (NoLibXXX) should not exist");
                return;
            }

            UserSpace us2 = new UserSpace(systemObject_, "/QSYS.LIB/"+UserSpaceTest.COLLECTION+".LIB/NOUSXXX.USRSPC");
            us2.setMustUseProgramCall(true);
            if (us2.exists())
            {
                failed("exists() returned true when user space (NoUSXXX) should not exist");
                return;
            }

            aUserSpace.setMustUseProgramCall(true);
            aUserSpace.create(12000, true, " ", (byte)0x00, "USATTR test", "*ALL");

            if (aUserSpace.exists() && aUserSpace2.exists())
            {
                succeeded();
            }
            else
            {
                failed("User space should be there " + aUserSpace.exists() + " " + aUserSpace2.exists());
            }
            deleteUserSpace(aUserSpace);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }
}
