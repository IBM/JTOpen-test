///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DAP9934437Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;


import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;

import test.Testcase;

/**
 Testcase DAP9934437Testcase.
 Tests fix for JACL PTR 9934437.
 **/
public class DAP9934437Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DAP9934437Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DATest.main(newArgs); 
   }

    String daName_ = "/QSYS.LIB/DATEST.LIB/D9934437.DTAARA";

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        try
        {
            CommandCall cc = new CommandCall(systemObject_);
            cc.run("CRTDTAARA DTAARA(DATEST/D9934437) TYPE(*CHAR) LEN(100)");
        }
        catch (Exception e)
        {
            output_.println(e);
        }
    }

    /**
     Create a data area on the system with length other than default 32. 
     Test CharacterDataArea.read() to make sure it uses the correct length.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, daName_);
            String s = da.read();
            if (s.length() != 100)
            {
                failed("Length not 100 characters: "+s.length());
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Create a data area on the system with length other than default 32. 
     Test CharacterDataArea.read(offset, length) to make sure it uses the correct length.
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, daName_);
            da.write("33characters345678901234567890123");
            String s = da.read(32, 10);
            if (!s.equals("3         "))
            {
                failed("read() failed: '"+s+"'");
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Create a data area on the system with length other than default 32. 
     Test CharacterDataArea.write() to make sure it uses the correct length.
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, daName_);
            String toWrite = "This string is longer than the default 32 characters.";
            da.write(toWrite);
            String s = da.read();
            if (s.length() != 100)
            {
                failed("Length not 100 characters: "+s.length());
            }
            else if (s.trim().equals(toWrite))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect data read: '"+s+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Create a data area on the system with length other than default 32. 
     Test CharacterDataArea.write(offset) to make sure it uses the correct length.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, daName_);
            da.clear();
            String toWrite = "This string is longer than the default 32 characters.";
            da.write(toWrite, 32);
            String s = da.read();
            if (s.length() != 100)
            {
                failed("Length not 100 characters: "+s.length());
            }
            else if (s.trim().equals(toWrite))
            {
                succeeded();
            }
            else
            {
                failed("Incorrect data read: '"+s+"'");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
