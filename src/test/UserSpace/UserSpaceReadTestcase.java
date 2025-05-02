///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceReadTestcase.java
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
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.UserSpace;

import test.Testcase;
import test.UserSpaceTest;

/**
 Test write methods for UserSpace.
 **/
public class UserSpaceReadTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpaceReadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserSpaceTest.main(newArgs); 
   }
    private AS400 usSystem_;

    // Library names to be overwritten later
    private static String userSpaceLibraryPath_          = "/QSYS.LIB/USTEST.LIB/";
    private static String userSpaceLibrary_ = "USTEST"; 
    private static String authorityUserSpaceLibraryPath_ = "/QSYS.LIB/USAUTHLIB.LIB/";
    private static String authorityLibrary_               = "USAUTHLIB"; 
    private static String unauthorizedUserSpaceLibraryPath_ = "/QSYS.LIB/USTESTAUTH.LIB/"; 
    private static String unauthorizedLibrary_               = "USTESTAUTH";

    private static String userSpaceObjectPath_           = "USREAD.USRSPC";
    private static String authorityUserSpaceObjectPath_  = "USREAD1.USRSPC";
    private static String unauthorizedUserSpaceObjectPath_ = "USREAD2.USRSPC"; 

    private String ustestUserID = null;
    // Enviroment variables.

    // Pre-existing user space variables.
    private String pre_existingUserSpaceObjectPath_ = "PREEXIST.USRSPC";

    private byte pre_existingByteValue_ = (byte)0x00;
    private int pre_existingLengthValue_ = 11000;
    private static int maxUserSpaceSize_ = 16776704;
    // Authority user space variables.
    
    private boolean setupAuthority_ = false;
    // Unauthorized user space variables.
    private boolean setupUnauthorized_ = false;

    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {

	//
	// Fix the library names
	//
      userSpaceLibrary_ = UserSpaceTest.COLLECTION;  
      userSpaceLibraryPath_          = "/QSYS.LIB/"+userSpaceLibrary_+".LIB/";
      authorityLibrary_               = userSpaceLibrary_+"AU"; 
      authorityUserSpaceLibraryPath_ = "/QSYS.LIB/"+authorityLibrary_+".LIB/";
      unauthorizedLibrary_               = userSpaceLibrary_+"UA"; 
      unauthorizedUserSpaceLibraryPath_ = "/QSYS.LIB/"+unauthorizedLibrary_+".LIB/"; 



        // Create AS400 objects necessary for running the testcases.
        setupUSExisting();
        ustestUserID = " USER(" + systemObject_.getUserId() + ") ";
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
        catch (Exception e)
        {
            output_.println("Cleanup failed:");
            e.printStackTrace(output_);
        }
    }

    private void setupUSExisting()
    {
        try
        {
            // Create an AS400 object to be used with USTEST user profile.
            usSystem_ = new AS400(systemObject_);

            // Create a user space to use test on a pre-existing user space.
            UserSpace aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            aUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "CRTDLT UserSpace", "*ALL");
            aUserSpace.close();
        }
        catch (Exception e)
        {
            output_.println("Setup failed:");
            e.printStackTrace(output_);
        }
        finally
        {
            systemObject_.disconnectService(AS400.COMMAND);
        }
    }

    /*
     Method: setupUSUnauthorized()
     Description: Create a library and user space that testcases will have NO authority to.
     */
    private void setupUSUnauthorized()
    {
        try
        {
	    deleteLibrary(""+unauthorizedLibrary_+""); 
            cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
            cmdRun("QSYS/CRTAUTL AUTL(USAUTHLIST) AUT(*EXCLUDE)");
            cmdRun("QSYS/CRTLIB LIB("+unauthorizedLibrary_+") AUT(USAUTHLIST)");

            UserSpace bUserSpace = new UserSpace(pwrSys_, unauthorizedUserSpaceLibraryPath_+unauthorizedUserSpaceObjectPath_);
            bUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "create UserSpace", "*ALL");
            bUserSpace.close();

            setupUnauthorized_ = true;
        }
        catch (Exception e)
        {
            output_.println("Setup failed:");
            e.printStackTrace(output_);
        }
        finally
        {
            pwrSys_.disconnectAllServices();
        }
    }

    /*
     Method: setupUSAuthority()
     Description: Create a user space to be used in authority testcases.
     */
    private void setupUSAuthority()
    {
        try
        {
	    deleteLibrary(""+authorityLibrary_+""); 
            cmdRun("QSYS/CRTLIB LIB("+authorityLibrary_+")");

            UserSpace aUSpace = new UserSpace(pwrSys_, authorityUserSpaceLibraryPath_+authorityUserSpaceObjectPath_);
            aUSpace.create(11000, true, " ", (byte)0x00, "USRead test", "*ALL");
            aUSpace.close();

            setupAuthority_ = true;
        }
        catch (Exception e)
        {
            output_.println("Setup failed:");
            e.printStackTrace(output_);
        }
        finally
        {
            pwrSys_.disconnectAllServices();
        }
    }

    /**
     Cleanup objects that have been created on the AS400.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
	deleteLibrary(""+unauthorizedLibrary_+""); 
        cmdRun("QSYS/DLTAUTL AUTL(USAUTHLIST)", "CPF2105");
	deleteLibrary(""+authorityLibrary_+""); 

        // delete the pre-existing User Space.
        UserSpace aUserSpace = new UserSpace(pwrSys_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
        aUserSpace.delete();

        // Disconnect all services used.
        pwrSys_.disconnectAllServices();
        if (systemObject_ != null) systemObject_.disconnectAllServices();
    }

    /**
     Method tested: read(byte[], int)
     Ensure that NullPointerException is thrown if dataBuffer is null.
     **/
    public void Var001()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            byte[] inBuffer = null;
            int bytesReturned = aUserSpace.read(inBuffer, 0);

            failed("Expected exception did not occur "+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "dataBuffer"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure the NullPointerException is thrown if dataBuffer is null.
     **/
    public void Var002()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            byte[] inBuffer = null;
            int bytesReturned = aUserSpace.read(inBuffer, 0, 0, 0);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "dataBuffer"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that ExtendedIllegalArgumentException is thrown if the length of dataBuffer is zero.
     **/
    public void Var003()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            int bytesReturned = aUserSpace.read(new byte[0], 0);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "dataBuffer.length (0): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();

            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is out of bounds
     **/
    public void Var004()
    {
        UserSpace aUserSpace = null;
        int byteCount = -999; 
        try
        {
            byte[] inBuffer = new byte[20];
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            byteCount = aUserSpace.read(inBuffer, -1);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "userSpaceOffset (-1): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();

            else
                failed(e, "Unexpected exception occurred. byteCount="+byteCount);
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if argument two is < 0.
     **/
    public void Var005()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            int bytesReturned = aUserSpace.read(new byte[1], -1, 1, 1);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if(exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                   "userSpaceOffset (-1): ",
                                   ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");

        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is less than zero.
     **/
    public void Var006()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);

            // Attempt to set offset to null
            String inString = aUserSpace.read(-1, 20);

            failed("Expected exception did not occur."+inString);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "userSpaceOffset (-1): ", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");

        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure -1 is returned at the end of user space.
     **/
    public void Var007()
    {
        UserSpace aUserSpace = null;
        byte[] inBuffer = new byte[10];
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");

            if (aUserSpace.read(inBuffer, aUserSpace.getLength() + 1) == -1)
                succeeded();
            else
                failed("Unexpected results occurred.");
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
     Method tested: read(byte[], int)
     Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is greater than
     the length of the User Space.
     **/
    public void Var008()
    {
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");

            byte[] dataBuffer = new byte[100];
            int usLength = aUserSpace.getLength();

            if (aUserSpace.read(dataBuffer, 2*usLength) == -1)
                succeeded();
            else
                failed("Expected exception did not occur.");
        }
        catch (Exception e)
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
    public void Var009()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);

            // Attempt to set offset to null
            byte[] readBuffer = new byte[20];
	    int bytesReturned = 0;
             
	    // User space block size differance from V5r3 to V5r4 
	    if ( usSystem_.getVRM() <= 0x00050300 ) 
		bytesReturned = aUserSpace.read(readBuffer, maxUserSpaceSize_ - 1, 0, readBuffer.length);
	    else  
		bytesReturned = aUserSpace.read(readBuffer, maxUserSpaceSize_ - 1 - 3584 , 0, readBuffer.length);

            if (bytesReturned == -1)
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if argument three is < 0.
     **/
    public void Var010()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            int bytesReturned = aUserSpace.read(new byte[1], 0, -1, 1);
            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that the read returns zero if the length is zero.
     **/
    public void Var011()
    {
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(11000, true, " ", (byte)0x00, "USREAD test", "*ALL");

            if (aUserSpace.read(new byte[20], 0, 1, 0) == 0)
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
     Method tested: read(int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if length is 0.
     **/
    public void Var012()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            String readString = aUserSpace.read(0, 0);

            failed("Expected exception did not occur."+readString);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is
     the length of the dataBuffer.
     **/
    public void Var013()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            byte[] readBuffer = new byte[20];
            readBuffer[19] = 19;
            int bytesReturned = aUserSpace.read(readBuffer, 0, readBuffer.length, 0);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if dataOffset is greater than
     the length of the data buffer.
     **/
    public void Var014()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            byte[] readBuffer = new byte[20];
            int bytesReturned = aUserSpace.read(readBuffer, 0, readBuffer.length + 1, readBuffer.length);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "dataOffset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if length is < 0.
     **/
    public void Var015()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            String readString = aUserSpace.read(0, -1);

            failed("Expected exception did not occur."+readString);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if length is < 0.
     **/
    public void Var016()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            int bytesReturned = aUserSpace.read(new byte[10], 0, 0, -1);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if the length parameter is greater than
     the length of the data buffer.
     **/
    public void Var017()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            byte[] readBuffer = new byte[20];
            int bytesReturned = aUserSpace.read(readBuffer, 0, 0, readBuffer.length + 1);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "length", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that the read reads up to the end of the user space
     successfully, returning the number of bytes read when the number of bytes
     available to be read is less than the byte array length.
     **/
    public void Var018()
    {
        UserSpace aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);

        try
        {
            aUserSpace.create(3584, true, " ", (byte)0x00, "READ test", "*ALL");

            if (isApplet_)
            {
                byte[] data = new byte[5000];
                int bytesRead = aUserSpace.read(data, 0);

                if(bytesRead < data.length)
                    succeeded();
                else
                    failed("Unexpected results occurred.");
            }
            else
            {
                byte[] data = new byte[5000];
                int bytesRead = aUserSpace.read(data, 0);

                if(bytesRead < data.length)
                    succeeded();
                else
                    failed("Unexpected results occurred.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: read(byte[], int)
     Read and verify every byte of a user space containing all possible byte values.
     **/
    public void Var019()
    {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++)
            data[i] = (byte) i;

        UserSpace aUserSpace = null;

        byte[] inBuffer1 = new byte[1];

        try
        {
            // create a user space and read data to it.
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+userSpaceLibrary_+".LIB/USREADCHK.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");
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
                    failed("Unexpected results occurred."+i1);

            }
            else
            {
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
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: read(byte[], int, int, int)
     Read and verify every byte of a user space containing all possible byte values.
     **/
    public void Var020()
    {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++)
            data[i] = (byte) i;

        UserSpace aUserSpace = null;

        byte[] inBuffer1 = new byte[1];

        try
        {
            // create a user space and read data to it.
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+userSpaceLibrary_+".LIB/USREADCHK.USRSPC");
            aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");
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
                    failed("Unexpected results occurred."+i1);

            }
            else
            {
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
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that the data read is stored at the specified offset in the byte array.
     **/
    public void Var021()
    {
        byte[] data = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };

        UserSpace aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);

        try
        {
            aUserSpace.create(1000, true, " ", (byte)0x00, "USREAD test", "*ALL");
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
        catch (Exception e)
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
    public void Var022()
    {
        if (isApplet_)
        {
            notApplicable();
            return;
        }

        UserSpace aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);

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
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        deleteUserSpace(aUserSpace);
    }

    /**
     Method tested: read(byte[], int)
     Verify read is successful if a connection has not been made.
     **/
    public void Var023()
    {
        byte[] data = new byte[20];
        UserSpace aUserSpace = null;

        try {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);

            int bytesReturned = aUserSpace.read(data, 0);
            if (bytesReturned > 0)
                succeeded();
            else
                failed("Unexpected results occurred.");

            aUserSpace.close();

        } catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }

          //systemObject_.disconnectService(AS400.FILE);
          systemObject_.disconnectAllServices();
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure AS400SecurityException is thrown if the user does not have authority to the library.
     **/
    public void Var024()
    {

        // Create AS400 objects used in testcase.
        if (!setupUnauthorized_)
            setupUSUnauthorized();

        UserSpace aUserSpace = null;
        try {

            aUserSpace = new UserSpace(systemObject_, unauthorizedUserSpaceLibraryPath_+unauthorizedUserSpaceObjectPath_);

            byte[] data = new byte[20];
            int bytesReturned = aUserSpace.read(data, 0);

            failed("Expected exception did not occur (make sure -uid on command line does not have authority to "+unauthorizedLibrary_+".lib/usread2.usrspc)."+bytesReturned);
        }
        catch (Exception e)
        {
//            if (isNative_)
//            {
//                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+unauthorizedLibrary+".LIB/USREAD2.USRSPC: ", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT);
//            }
//            else
            {
                assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+unauthorizedLibrary_+".LIB/USREAD2.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
            }
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          systemObject_.disconnectAllServices();
        }

    }

    /**
     Method tested: read(byte[], int)
     Ensure AS400SecurityException is thrown if the user does not have authority to the user space.
     **/
    public void Var025()
    {

        // Create AS400 objects used in testcase.
        if (!setupAuthority_)
            setupUSAuthority();

        UserSpace aUserSpace = null;
        try
        {
            CommandCall cmd = new CommandCall(pwrSys_);

            if(cmd.run("QSYS/GRTOBJAUT OBJ("+authorityLibrary_+"/USREAD1) OBJTYPE(*USRSPC) " + ustestUserID + " AUT(*EXCLUDE)") != true)
            {
                AS400Message[] messageList = cmd.getMessageList();
                throw new IOException(messageList[0].toString());
            }
            int bytes = 0; 
            if (isNative_)
            {
                byte[] data = new byte[20];
                aUserSpace = new UserSpace(systemObject_, authorityUserSpaceLibraryPath_+authorityUserSpaceObjectPath_);
                bytes = aUserSpace.read(data, 0);
            }
            else
            {
                byte[] data = new byte[20];
                aUserSpace = new UserSpace(usSystem_, authorityUserSpaceLibraryPath_+authorityUserSpaceObjectPath_);
                bytes = aUserSpace.read(data, 0);
            }

            failed("Expected exception did not occur (make sure -uid on command line does not have authority to "+authorityLibrary_+".lib/usread1.usrspc)."+bytes);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+authorityLibrary_+".LIB/USREAD1.USRSPC: ",  AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that ExtendedIllegalArgumentException is thrown if the library does not exist.
     **/
    public void Var026()
    {

        UserSpace aUserSpace = null;
        try {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/USBADLIB.LIB/USREAD2.USRSPC");

            byte[] dataBuffer = new byte[20];
            aUserSpace.read(dataBuffer, 0);

            failed("Exception did not occur.");
        }
        catch (Exception e)
        {
//            if (isNative_)
//            {
//                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USREAD2.USRSPC: ", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
//            }
//            else
            {
                assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/USBADLIB.LIB/USREAD2.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
            }
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that ExtendedIllegalArgumentException is thrown with CPF2105 if the User Space does not exist.
     **/
    public void Var027()
    {
        UserSpace aUserSpace = null;

        try {
            aUserSpace = new UserSpace(systemObject_, "/QSYS.LIB/"+userSpaceLibrary_+".LIB/USBAD.USRSPC");

            byte[] dataBuffer = new byte[20];
            int bytesReturned = aUserSpace.read(dataBuffer, 0);

            failed("Exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ObjectDoesNotExistException", "/QSYS.LIB/"+userSpaceLibrary_+".LIB/USBAD.USRSPC: ", ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST);
        }
    }

    /**
     Method tested: read(int, int)
     Ensure that the read is successful up to the end of the user space returning a String with
     length less than the String specified by the length parameter.
     **/
    public void Var028()
    {

        UserSpace aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);

        try
        {
            aUserSpace.create(3584, true, " ", (byte)0x00, "READ test", "*ALL");

            if (isApplet_)
            {
                int expectedLength = 10;
                String readString = aUserSpace.read(3580, expectedLength);

                if(readString.length() == expectedLength)
                    succeeded();
                else
                    failed("Unexpected results occurred.");
            }
            else
            {
                int expectedLength = 10;
                String readString = aUserSpace.read(3580, expectedLength);

                if(readString.length() == expectedLength)
                    succeeded();
                else
                    failed("Unexpected results occurred.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          deleteUserSpace(aUserSpace);
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if the length of dataBuffer is zero.
     **/
    public void Var029()
    {
        UserSpace aUserSpace = null;
        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+pre_existingUserSpaceObjectPath_);
            int bytesReturned = aUserSpace.read(new byte[0], 0, 0, 0);

            failed("Expected exception did not occur."+bytesReturned);
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "dataBuffer", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();

            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     Method tested: read(byte[], int, int, int)
     Ensure that ExtendedIllegalArgumentException is thrown if userSpaceOffset is greater than
     the length of the User Space.
     **/
    public void Var030()
    {
        UserSpace aUserSpace = null;

        try
        {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(11000, true, " ", (byte)0x00, "read UserSpace", "*ALL");

            byte[] dataBuffer = new byte[100];
            int usLength = aUserSpace.getLength();

            if (aUserSpace.read(dataBuffer, 2*usLength, 0, 80) == -1)
                succeeded();
            else
                failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "userSpaceOffset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          deleteUserSpace(aUserSpace);
        }

    }

    /**
     Method tested: read(int, int)
     Ensure that the expected String is returned.
     **/
    public void Var031()
    {
        String expectedString = "USTESTSTRING";
        String readString = null;

        UserSpace aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);

        try
        {
            aUserSpace.create(1000, true, " ", (byte)0x00, "USREAD test", "*ALL");
            aUserSpace.write(expectedString, 10);

            if (isApplet_)
            {
                readString = aUserSpace.read(10, 12);
            }
            else
            {
                readString = aUserSpace.read(10,12);
            }
            if (readString.equals(expectedString))
                succeeded();
            else
                failed("Unexpected results occurred.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          deleteUserSpace(aUserSpace);
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that a second UserSpace object can read from a open user space.(same AS400 object).
     **/
    public void Var032()
    {
        UserSpace aUserSpace = null;
        UserSpace secondUS = null;

        try {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*USE");

            secondUS = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);

            byte[] dataBuffer = new byte[16];
            int bytes = secondUS.read(dataBuffer, 0);

            succeeded("bytes="+bytes);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          if (secondUS != null)
            try {
                secondUS.close();
            }catch (Exception e) { output_.println("Testcase cleanup failed. " + e); }
          deleteUserSpace(aUserSpace);
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that a second UserSpace object can read from a user space after it is closed (different AS400 object).
     **/
    public void Var033()
    {

        UserSpace aUserSpace = null;
        UserSpace secondUS = null;

        try {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*ALL");

            aUserSpace.close();         // close the user space.

            secondUS = new UserSpace(usSystem_, userSpaceLibraryPath_+userSpaceObjectPath_);

            byte[] dataBuffer = new byte[16];
            int bytes = secondUS.read(dataBuffer, 0);

            succeeded("bytes="+bytes);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          if (secondUS != null)
            try {
                secondUS.close();
            }catch (Exception e) { output_.println("Testcase cleanup failed. " + e); }
          deleteUserSpace(aUserSpace);
        }
    }

    /**
     Method tested: read(byte[], int)
     Verify that an Exception is thrown if a second user space object tries to read from an open user space
     (different AS400 object).
     **/
    public void Var034()
    {

        UserSpace aUserSpace = null;

        try {
            aUserSpace = new UserSpace(pwrSys_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*USE");

            UserSpace secondUS = new UserSpace(usSystem_, userSpaceLibraryPath_+userSpaceObjectPath_);

            byte[] dataBuffer = new byte[16];
            int bytes = secondUS.read(dataBuffer, 0);

            if (isNative_)
                succeeded("bytes="+bytes);
            else
                failed("No Exception occurred.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400SecurityException", "/QSYS.LIB/"+userSpaceLibrary_+".LIB/USREAD.USRSPC: ", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT);
        }
        finally
        {
            try
            {
              if (aUserSpace != null) { 
                aUserSpace.close();
                deleteUserSpace(aUserSpace);
              }
            }
            catch (Exception e)
            {
                output_.println("cleanup failed ");
                e.printStackTrace(output_);
            }
        }
    }

    /**
     Method tested: read(byte[], int)
     Ensure that UserSpace.read is successful after the user space is closed (same AS400 object).
     **/
    public void Var035()
    {
        UserSpace aUserSpace = null;

        try {
            aUserSpace = new UserSpace(systemObject_, userSpaceLibraryPath_+userSpaceObjectPath_);
            aUserSpace.create(10240, true, " ", (byte)0x00, "Read User Space", "*USE");

            aUserSpace.close();             // Close the user space.

            byte[] dataBuffer = new byte[16];
            int bytes = aUserSpace.read(dataBuffer, 0);

            succeeded("bytes="+bytes);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
        finally
        {
          if (aUserSpace != null) try { aUserSpace.close(); } catch (Exception e) { e.printStackTrace(); }
          deleteUserSpace(aUserSpace);
        }
    }
}
