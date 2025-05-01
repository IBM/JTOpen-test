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

package test.SSL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SecureAS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SSLSerializeTestcase.
 **/
public class SSLSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SSLSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SSLTest.main(newArgs); 
   }
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
            s.close(); 

            FileInputStream in = new FileInputStream("tSecureAS400");
            ObjectInputStream s2 = new ObjectInputStream(in);
            s2.close(); 
            SecureAS400 sys2 = (SecureAS400)s2.readObject();

            sys2.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);
            
            sys2.connectService(AS400.DATAQUEUE);
            sys2.disconnectService(AS400.DATAQUEUE);
            sys2.close(); 
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
