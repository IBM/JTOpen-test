///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecSerializeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import com.ibm.as400.access.AS400;

import test.PasswordVault;
import test.Testcase;

/**
 Testcase SecSerializeTestcase.
 **/
public class SecSerializeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecSerializeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecTest.main(newArgs); 
   }
    /**
     Serialize, Deserialize, Connect
     **/
    public void Var001()
    {
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            AS400 sys = new AS400(systemName_, userId_, charPassword);
            sys.setMustUseSockets(mustUseSockets_);
            FileOutputStream f = new FileOutputStream("tAS400");
            ObjectOutput s =  new ObjectOutputStream(f);
            s.writeObject(sys);
            s.flush();

            FileInputStream in = new FileInputStream("tAS400");
            ObjectInputStream s2 = new ObjectInputStream(in);
            AS400 sys2 = (AS400)s2.readObject();
            sys2.setPassword(charPassword);
             PasswordVault.clearPassword(charPassword);
            sys2.connectService(AS400.FILE);
            sys2.disconnectService(AS400.FILE);

            File fd = new File("tAS400");
            fd.delete();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
