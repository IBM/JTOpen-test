///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSUserTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.FileOutputStream;

import java.util.Vector;
import java.util.Enumeration;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.User;
import com.ibm.as400.access.UserList;

import test.JTOpenTestEnvironment;
import test.Testcase;

/**
 *Testcase NLSUserTestcase.  This test class verifies the use of DBCS Strings
 *in selected User testcase variations.
**/
public class NLSUserTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSUserTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  public String UserPathName_ = "/QSYS.LIB/USTEST.LIB/USNLSTEST.USRSPC";
  private String operatingSystem_;
  private boolean DOS_;

  String dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String dbcs_string50 = getResource("IFS_DBCS_STRING50");

  /**
  Constructor.
  **/
  public NLSUserTestcase(AS400            systemObject,
                              Vector<String>           variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream
                              )

  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSUserTestcase", 1,
          variationsToRun, runMode, fileOutputStream);
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {

    // Determine operating system we're running under
    operatingSystem_ = System.getProperty("os.name");
    if (JTOpenTestEnvironment.isWindows)
    {
      DOS_ = true;
    }
    else
    {
      DOS_ = false;
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("DOS-based file structure: " + DOS_);
    output_.println("Executing applet: " + isApplet_);
  }



  /**
    Cleanup Test library.
   @exception  Exception  If an exception occurs.
  **/
  protected void cleanup()
    throws Exception
  {
  }



  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.FILE);
    }
    catch(Exception e)
    {
      output_.println("Unable to connect to the AS/400");
      e.printStackTrace();
      return;
    }

    try
    {
      setup();
    }
    catch (Exception e)
    {
      output_.println("Setup failed.");
      return;
    }

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }

    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.FILE);
      cleanup();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }



/**
Method tested: User.getDescription()
Verify that the User description is what is expected.
**/
  public void Var001()
  {
    CommandCall cmd = null;
    String uid = null;
    String oldDesc = null;
    try
    {

      // Save the old user description so we can restore it.
      UserList userList = new UserList (systemObject_);
      userList.setUserInfo (UserList.USER);
      uid = systemObject_.getUserId();
      Enumeration<User> enumeration = userList.getUsers();
      oldDesc = null;
      while (enumeration.hasMoreElements() && oldDesc == null)
      {
        User user = (User)enumeration.nextElement();
        if (user.getName().equals(uid))
          oldDesc = user.getDescription();
      }
      if (oldDesc == null)
      {
        failed("Unable to determine current description for user " + uid + ".");
        return;
      }

      // Change the user description.
      cmd = new CommandCall(systemObject_);
      String newDesc = dbcs_string50;
      if(cmd.run("CHGOBJD OBJ(QSYS/" + uid + ") OBJTYPE(*USRPRF) TEXT('" + newDesc + "')") == false)
      {
        AS400Message[] messageList = cmd.getMessageList();
        System.out.println("CHGUSRPRF failed.  " + messageList[0].toString());
        failed("Unable to change user description.");
        return;
      }

      // Verify that the user description got changed.
      userList.load();
      enumeration = userList.getUsers();
      String desc = null;
      while (enumeration.hasMoreElements() && desc == null)
      {
        User user = (User)enumeration.nextElement();
        if (user.getName().equals(uid))
          desc = user.getDescription();
      }
      if (desc != null && desc.equals(newDesc))
        succeeded();
      else
      {
        System.out.println("Expected: " + newDesc);
        System.out.println("Got:      " + desc);
        failed();
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      try {
        if (oldDesc != null)
          cmd.run("CHGOBJD OBJ(QSYS/" + uid + ") OBJTYPE(*USRPRF) TEXT('" + oldDesc + "')");
      }
      catch(Exception e) {}
    }
  }

}



