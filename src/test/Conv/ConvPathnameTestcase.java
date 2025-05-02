///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvPathnameTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase ConvPathnameTestcase. This tests the CharConverter class. Test additional methods in CharConverter for converting QSYS pathnames with variant characters.  Specifically, it tests to make sure that:
 <ol>
 <li>The method convertIFSQSYSPathnameToJobPathname functions correctly.
 <li>The method convertJobPathnameToIFSQSYSPathname functions correctly.
 </ol>
**/
public class ConvPathnameTestcase extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvPathnameTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
    systemObject_.disconnectAllServices();
  }

  /**
   Verify CharConverter.convertIFSQSYSPathnameToJobPathname() with EBCDIC codepoint 0x5B for CCSID 285.
   Expected results:
   <ul compact>
   <li>The variation should succeed.
   </ul>
  **/
  public void Var001()
  {
    try
    {
      int ccsid = 285; // United Kingdom
      String original = "CONVTEST$#@";
      String expected = "CONVTEST\u00A3#@"; // 0xA3 is the pound sterling
      String returned = CharConverter.convertIFSQSYSPathnameToJobPathname(original, ccsid);
      if (!returned.equals(expected))
      {
        failed("Mismatch for ccsid " + ccsid + ": '" + returned + "' != '" + expected + "'");
      }
      else
      {
        String ret2 = CharConverter.convertJobPathnameToIFSQSYSPathname(returned, ccsid);
        if (ret2.equals(original))
        {
          succeeded();
        }
        else
        {
          failed("Roundtrip failed for ccsid " + ccsid + ": '" + ret2 + "' != '" + expected + "'");
        }
      }
    }
    catch (Throwable t)
    {
      failed(t, "Unexpected exception.");
    }
  }

  /**
Verify CharConverter.convertIFSQSYSPathnameToJobPathname() with EBCDIC codepoint 0x7C for CCSID 273.
Expected results:
<ul compact>
<li>The variation should succeed.
</ul>
  **/
  public void Var002()
  {
    try
    {
      int ccsid = 273; // Germany
      String original = "CONVTEST$#@";
      String expected = "CONVTEST$#\u00A7"; // 0xA7 is the section sign
      String returned = CharConverter.convertIFSQSYSPathnameToJobPathname(original, ccsid);
      if (!returned.equals(expected))
      {
        failed("Mismatch for ccsid " + ccsid + ": '" + returned + "' != '" + expected + "'");
      }
      else
      {
        String ret2 = CharConverter.convertJobPathnameToIFSQSYSPathname(returned, ccsid);
        if (ret2.equals(original))
        {
          succeeded();
        }
        else
        {
          failed("Roundtrip failed for ccsid " + ccsid + ": '" + ret2 + "' != '" + expected + "'");
        }
      }
    }
    catch (Throwable t)
    {
      failed(t, "Unexpected exception.");
    }
  }

  /**
Verify CharConverter.convertIFSQSYSPathnameToJobPathname() with EBCDIC codepoints 0x5B, 0x7B, and 0x7C for CCSID 277.
Expected results:
<ul compact>
<li>The variation should succeed.
</ul>
  **/
  public void Var003()
  {
    try
    {
      int ccsid = 277; // Norway/Denmark
      String original = "CONVTEST$#@";
      String expected = "CONVTEST\u00C5\u00C6\u00D8";
      // 0xC5 is capital 'A' with ring above (angstrom)
      // 0xC6 is capital 'AE' ligature
      // 0xD8 is capital 'O' with stroke (empty set)
      String returned = CharConverter.convertIFSQSYSPathnameToJobPathname(original, ccsid);
      if (!returned.equals(expected))
      {
        failed("Mismatch for ccsid " + ccsid + ": '" + returned + "' != '" + expected + "'");
      }
      else
      {
        String ret2 = CharConverter.convertJobPathnameToIFSQSYSPathname(returned, ccsid);
        if (ret2.equals(original))
        {
          succeeded();
        }
        else
        {
          failed("Roundtrip failed for ccsid " + ccsid + ": '" + ret2 + "' != '" + expected + "'");
        }
      }
    }
    catch (Throwable t)
    {
      failed(t, "Unexpected exception.");
    }
  }

  /**
Verify real-world scenario with library name that contains variant codepoints.
Create the library using CCSID 37.
Retrieve the library name using IFS.
Delete the library name using CCSID 277 after converting it.
Expected results:
<ul compact>
<li>The variation should succeed.
</ul>
  **/
  public void Var004()
  {
      try
      {
          if (pwrSys_.getVRM() >= 0x00060100)
          {
              notApplicable("FOR >= V6R1");
              return;
          }
      }
      catch (Throwable t)
      {
          failed(t, "Unexpected exception.");
      }

    AS400 user277 = null;
    try
    {
      if (pwrSys_ == null)
      {
        failed("pwrSys not specified.");
        return;
      }
      CommandCall cc = new CommandCall(pwrSys_);
      cc.run("QSYS/DLTUSRPRF USRPRF(CONVTST277) OWNOBJOPT(*DLT)");
      boolean res = cc.run("QSYS/CRTUSRPRF USRPRF(CONVTST277) PASSWORD(CNVTST1) CCSID(277)");
      if (!res)
      {
        AS400Message[] msgs = cc.getMessageList();
        for (int i=0; i<msgs.length;  ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Variation setup: Unable to create user profile.");
        return;
      }
      user277 = new AS400(pwrSys_.getSystemName(), "CONVTST277", "CNVTST1".toCharArray());
      user277.setMustUseSockets(true);
      int originalCCSID = pwrSys_.getCcsid();
      int newCCSID = user277.getCcsid();
      if (originalCCSID == newCCSID)
      {
        failed("Variation setup: User profile CCSIDs are the same: " + originalCCSID);
        user277.close(); 
        return;
      }
      if (newCCSID != 277)
      {
        failed("Variation setup: User profile CCSID not 277: " + newCCSID);
        user277.close(); 
        return;
      }

      String lib37 = "CNVTEST$#@";
      deleteLibrary(cc, lib37); 
      res = cc.run("QSYS/CRTLIB " + lib37 + " AUT(*ALL)");
      if (!res)
      {
        AS400Message[] msgs = cc.getMessageList();
        for (int i=0; i<msgs.length;  ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Variation setup: Unable to create library with variant characters: " + lib37);
        user277.close(); 
        return;
      }

      // We shouldn't be able to delete the library using CommandCall with a different job CCSID.
      CommandCall cc277 = new CommandCall(user277);
      String delRes = deleteLibrary(cc277,lib37);
      if (delRes == null)
      {
        failed("Shouldn't be able to delete library " + lib37 + " using CCSID " + newCCSID + ".");
        return;
      }

      // OK, now we read the library name using IFS,
      // run it through our converter,
      // and delete it with CommandCall.
      IFSFile ifs = new IFSFile(pwrSys_, "/QSYS.LIB/CNVTEST$#@.LIB");
      if (!ifs.exists())
      {
        failed("Unable to access library name using IFS: " + ifs.getCanonicalPath());
        return;
      }
      String name = ifs.getName().substring(0, ifs.getName().lastIndexOf("."));
      String convertedName = CharConverter.convertIFSQSYSPathnameToJobPathname(name, 277);
      delRes = deleteLibrary(cc277,convertedName);
      if (delRes != null )
      {
        AS400Message[] msgs = cc277.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Unable to delete converted library name: " + convertedName);
        return;
      }
      succeeded();
    }
    catch (Throwable t)
    {
      failed(t, "Unexpected exception.");
    }
    finally
    {
      try
      {
        user277.disconnectAllServices();
      }
      catch (Exception e)
      {
      }
      try
      {
        CommandCall cc = new CommandCall(pwrSys_);
        cc.run("QSYS/DLTUSRPRF USRPRF(CONVTST277) OWNOBJOPT(*DLT)");
      }
      catch (Exception e)
      {
      }
    }
  }

  /**
Verify real-world scenario with library name that contains variant codepoints.
Create the library using CCSID 37.
Access the library using CCSID 277.
Delete the library using IFS.
Expected results:
<ul compact>
<li>The variation should succeed.
</ul>
  **/
  public void Var005()
  {
      try
      {
          if (pwrSys_.getVRM() >= 0x00060100)
          {
              notApplicable("FOR >= V6R1");
              return;
          }
      }
      catch (Throwable t)
      {
          failed(t, "Unexpected exception.");
      }

    AS400 user277 = null;
    try
    {
      if (pwrSys_ == null)
      {
        failed("pwrSys not specified.");
        return;
      }
      CommandCall cc = new CommandCall(pwrSys_);
      cc.run("QSYS/DLTUSRPRF USRPRF(CONVTST277) OWNOBJOPT(*DLT)");
      boolean res = cc.run("QSYS/CRTUSRPRF USRPRF(CONVTST277) PASSWORD(CNVTST1) CCSID(277)");
      if (!res)
      {
        AS400Message[] msgs = cc.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Variation setup: Unable to create user profile.");
        return;
      }
      user277 = new AS400(pwrSys_.getSystemName(), "CONVTST277", "CNVTST1".toCharArray());
      user277.setMustUseSockets(true);
      int originalCCSID = pwrSys_.getCcsid();
      int newCCSID = user277.getCcsid();
      if (originalCCSID == newCCSID)
      {
        failed("Variation setup: User profile CCSIDs are the same: " + originalCCSID);
        user277.close(); 

        return;
      }
      if (newCCSID != 277)
      {
        failed("Variation setup: User profile CCSID not 277: " + newCCSID);
        user277.close(); 
        return;
      }

      String lib277 = "CNVTEST\u00C5\u00C6\u00D8";
      CommandCall cc277 = new CommandCall(user277);
      deleteLibrary(cc277, lib277);
      res = cc277.run("QSYS/CRTLIB " + lib277 + " AUT(*ALL)");
      if (!res)
      {
        AS400Message[] msgs = cc277.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Variation setup: Unable to create library with variant characters: " + lib277);
        return;
      }

      // We shouldn't be able to delete the library using CommandCall with a different job CCSID.
      String deleteReturn = deleteLibrary(cc, lib277); 
      if (deleteReturn == null)
      {
        failed("Shouldn't be able to delete library " + lib277 + " using CCSID " + originalCCSID + ".");
        return;
      }

      // OK, now we use the library name from CommandCall,
      // run it through our converter,
      // and delete it with IFS.

      cc277.run("QSYS/CRTLIB CNVTEMP");
      cc277.run("QDEVELOP/CRTUSRSPC CNVTEMP/CNVUSRSPC");

      ProgramParameter[] parms = new ProgramParameter[5];
      AS400Text text20 = new AS400Text(20, user277.getCcsid(), user277);
      AS400Text text8 = new AS400Text(8, user277.getCcsid(), user277);
      AS400Text text10 = new AS400Text(10, user277.getCcsid(), user277);
      parms[0] = new ProgramParameter(text20.toBytes("CNVUSRSPC CNVTEMP   "));
      parms[1] = new ProgramParameter(text8.toBytes("OBJL0100"));
      parms[2] = new ProgramParameter(text20.toBytes("CNVTEST*  QSYS      "));
      parms[3] = new ProgramParameter(text10.toBytes("*LIB      "));
      parms[4] = new ProgramParameter(4); // error code
      ProgramCall pc = new ProgramCall(user277, "/QSYS.LIB/QUSLOBJ.PGM", parms);
      boolean b = pc.run();
      if (!b)
      {
        AS400Message[] msgs = pc.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        failed("Unable to call list API.");
        return;
      }

      UserSpace us = new UserSpace(user277, "/QSYS.LIB/CNVTEMP.LIB/CNVUSRSPC.USRSPC");
      byte[] data = new byte[us.getLength()];
      int numRead = us.read(data, 0);
      if (numRead != data.length)
      {
        failed("Didn't read all of user space: " + numRead + " != " + data.length);
        return;
      }
      int offsetToListDataSection = BinaryConverter.byteArrayToInt(data, 124);
      CharConverter charc = new CharConverter(user277.getCcsid(), user277);

      // The data is 37 but we converted it using 277, like a user would expect to do
      // since their profile is 277. This is the scenario we are testing.

      String objectName = charc.byteArrayToString(data, offsetToListDataSection, 10);
      String convertedName = CharConverter.convertJobPathnameToIFSQSYSPathname(objectName, 277); // So we run it through the converter for our CCSID.

      IFSFile ifs = new IFSFile(pwrSys_, "/QSYS.LIB/" + convertedName + ".LIB");
      if (!ifs.exists())
      {
        failed("Unable to access library name using IFS: " + ifs.getCanonicalPath());
        return;
      }
      if (!ifs.delete())
      {
        failed("Unable to delete library name using IFS: " + ifs.getCanonicalPath());
        return;
      }
      succeeded();

    }
    catch (Throwable t)
    {
      failed(t, "Unexpected exception.");
    }
    finally
    {
      try
      {
        user277.disconnectAllServices();
      }
      catch (Exception e) {}
      try
      {
        CommandCall cc = new CommandCall(pwrSys_);
        cc.run("QSYS/DLTUSRPRF USRPRF(CONVTST277) OWNOBJOPT(*DLT)");
	deleteLibrary(cc,"CNVTEMP");
      }
      catch (Exception e) {}
    }
  }
}
