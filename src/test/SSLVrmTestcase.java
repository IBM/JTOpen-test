///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLVrmTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.SecureAS400;

/**
 Testcase SSLVrmTestcase.
 **/
public class SSLVrmTestcase extends Testcase
{
    /**
     Signon to a V4R4 system and verify the version.
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasptm", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.FILE);
            try
            {
                assertCondition(system.getVersion() == 4, "Incorrect version - test runs to rchasptm.");
            }
            finally
            {
                system.disconnectService(SecureAS400.FILE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasptm.");
        }
    }

    /**
     Signon to a V4R4 system and verify the release.
     **/
    public void Var002()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasptm", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.PRINT);
            try
            {
                assertCondition(system.getRelease() == 4, "Incorrect release - test runs to rchasptm.");
            }
            finally
            {
                system.disconnectService(SecureAS400.PRINT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasptm.");
        }
    }

    /**
     Signon to a V4R5 system and verify the version.
     **/
    public void Var003()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchmary", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.COMMAND);
            try
            {
                assertCondition(system.getVersion() == 4, "Incorrect version - test runs to rchmary.");
            }
            finally
            {
                system.disconnectService(SecureAS400.COMMAND);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchmary.");
        }
    }

    /**
     Signon to a V4R5 system and verify the release.
     **/
    public void Var004()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchmary", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.DATAQUEUE);
            try
            {
                assertCondition(system.getRelease() == 5, "Incorrect release - test runs to rchmary.");
            }
            finally
            {
                system.disconnectService(SecureAS400.DATAQUEUE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchmary.");
        }
    }

    /**
     Signon to a V5R1 system and verify the version.
     **/
    public void Var005()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasf6d", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.DATABASE);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to rchasf6d.");
            }
            finally
            {
                system.disconnectService(SecureAS400.DATABASE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasf6d.");
        }
    }

    /**
     Signon to a V5R1 system and verify the release.
     **/
    public void Var006()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasf6d", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.RECORDACCESS);
            try
            {
                assertCondition(system.getRelease() == 1, "Incorrect release - test runs to rchasf6d.");
            }
            finally
            {
                system.disconnectService(SecureAS400.RECORDACCESS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasf6d.");
        }
    }

    /**
     Signon to a V5R1 system and verify the version.
     **/
    public void Var007()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchas1dd", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.CENTRAL);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to rchas1dd.");
            }
            finally
            {
                system.disconnectService(SecureAS400.CENTRAL);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchas1dd.");
        }
    }

    /**
     Signon to a V5R1 system and verify the release.
     **/
    public void Var008()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchas1dd", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.SIGNON);
            try
            {
                assertCondition(system.getRelease() == 1, "Incorrect release - test runs to rchas1dd.");
            }
            finally
            {
                system.disconnectService(SecureAS400.SIGNON);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchas1dd.");
        }
    }

    /**
     Signon to a V5R2 system and verify the version.
     **/
    public void Var009()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasd8b", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.FILE);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to rchasd8b.");
            }
            finally
            {
                system.disconnectService(SecureAS400.FILE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasd8b.");
        }
    }

    /**
     Signon to a V5R2 system and verify the release.
     **/
    public void Var010()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasd8b", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.PRINT);
            try
            {
                assertCondition(system.getRelease() == 2, "Incorrect release - test runs to rchasd8b.");
            }
            finally
            {
                system.disconnectService(SecureAS400.PRINT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasd8b.");
        }
    }

    /**
     Signon to a V5R2 system and verify the version.
     **/
    public void Var011()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp116ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.COMMAND);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to lp116ab.");
            }
            finally
            {
                system.disconnectService(SecureAS400.COMMAND);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp116ab.");
        }
    }

    /**
     Signon to a V5R2 system and verify the release.
     **/
    public void Var012()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp116ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.DATAQUEUE);
            try
            {
                assertCondition(system.getRelease() == 2, "Incorrect release - test runs to lp116ab.");
            }
            finally
            {
                system.disconnectService(SecureAS400.DATAQUEUE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp116ab.");
        }
    }

    /**
     Signon to a V5R3 system and verify the version.
     **/
    public void Var013()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasrjs", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.DATABASE);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to rchasrjs.");
            }
            finally
            {
                system.disconnectService(SecureAS400.DATABASE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasrjs.");
        }
    }

    /**
     Signon to a V5R3 system and verify the release.
     **/
    public void Var014()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("rchasrjs", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.RECORDACCESS);
            try
            {
                assertCondition(system.getRelease() == 3, "Incorrect release - test runs to rchasrjs.");
            }
            finally
            {
                system.disconnectService(SecureAS400.RECORDACCESS);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to rchasrjs.");
        }
    }

    /**
     Signon to a V5R3 system and verify the version.
     **/
    public void Var015()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp016ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.CENTRAL);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to lp016ab.");
            }
            finally
            {
                system.disconnectService(SecureAS400.CENTRAL);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp016ab.");
        }
    }

    /**
     Signon to a V5R3 system and verify the release.
     **/
    public void Var016()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp016ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.SIGNON);
            try
            {
                assertCondition(system.getRelease() == 3, "Incorrect release - test runs to lp016ab.");
            }
            finally
            {
                system.disconnectService(SecureAS400.SIGNON);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp016ab.");
        }
    }

    /**
     Signon to a V5R3 system and verify the version.
     **/
    public void Var017()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp126ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.FILE);
            try
            {
                assertCondition(system.getVersion() == 5, "Incorrect version - test runs to lp126ab.");
            }
            finally
            {
                system.disconnectService(SecureAS400.FILE);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp126ab.");
        }
    }

    /**
     Signon to a V5R3 system and verify the release.
     **/
    public void Var018()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp126ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            system.connectService(SecureAS400.PRINT);
            try
            {
                assertCondition(system.getRelease() == 3, "Incorrect release - test runs to lp126ab.");
            }
            finally
            {
                system.disconnectService(SecureAS400.PRINT);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp126ab.");
        }
    }

    /**
     Retrieve the version without signing on.  Should return version 5.
     **/
    public void Var019()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp126ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            assertCondition(system.getVersion() == 5, "Incorrect version - test runs to lp126ab.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp126ab.");
        }
    }

    /**
     Retrieve the release without signing on.  Should return 3.
     **/
    public void Var020()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp126ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            assertCondition(system.getRelease() == 3, "Incorrect release - test runs to lp126ab.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp126ab.");
        }
    }

    /**
     Retrieve the modification level without signing on.  Should return 0.
     **/
    public void Var021()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp126ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            assertCondition(system.getModification() == 0, "Incorrect modification - test runs to lp126ab.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp126ab.");
        }
    }

    /**
     Retrieve the vrm without signing on.  Should return 0x00050300.
     **/
    public void Var022()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 system = new SecureAS400("lp126ab", userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
            system.setMustUseSockets(mustUseSockets_);
            assertCondition(system.getVRM() == 0x00050300, "VRM not correct - test runs to lp126ab.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception - test runs to lp126ab.");
        }
    }

    /**
     Generate a VRM and verify it was generated correctly.
     **/
    public void Var023()
    {
        try
        {
            int vrm = SecureAS400.generateVRM(4, 5, 6);
            assertCondition(vrm == 0x00040506, "Incorrect VRM generated.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
