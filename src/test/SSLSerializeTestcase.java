///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLSerializeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SecureAS400;

/**
 Testcase SSLSerializeTestcase.
 **/
public class SSLSerializeTestcase extends Testcase
{
    /**
     Serialize, Deserialize, Connect
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            SecureAS400 sys = new SecureAS400(systemName_, userId_, charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            FileOutputStream f = new FileOutputStream("tSecureAS400");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(sys);
            s.flush();

            FileInputStream in = new FileInputStream("tSecureAS400");
            ObjectInputStream s2 = new ObjectInputStream(in);
            SecureAS400 sys2 = (SecureAS400)s2.readObject();

            sys2.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            
            sys2.connectService(AS400.DATAQUEUE);
            sys2.disconnectService(AS400.DATAQUEUE);

            File fd = new File("tSecureAS400");
            fd.delete();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
