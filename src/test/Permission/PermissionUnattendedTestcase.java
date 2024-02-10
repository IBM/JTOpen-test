///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PermissionUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Permission;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.DLOPermission;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Permission;
import com.ibm.as400.access.QSYSPermission;
import com.ibm.as400.access.RootPermission;
import com.ibm.as400.access.UserPermission;

import test.PermissionTestDriver;
import test.Testcase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.String;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.File;
import java.net.InetAddress;                                      //@A1A

/**
The PermissionUnattendedTestcase class provides testcases to test the methods of the Permission class.

<p>This tests the following Permission methods:
<ul>
<li>constructor() - Var001 to Var017
<li>addAuthorizedUser() - Var018 to Var030
<li>addPropertyChangeListener() - Var031 to Var036 
<li>addUserPermission() - Var037 to Var045
<li>commit() - Var046 to Var054
<li>getAuthorizationList() - Var055 to Var057
<li>getAuthorizedUsers() - Var058 to Var060
<li>getName() - Var061 to Var063
<li>getOwner() - Var064 to Var066
<li>getPath() - Var067 to Var069
<li>getPrimaryGroup() - Var070 to Var072
<li>getSensitivityLevel() - Var073 to Var075
<li>getSystem() - Var076 to Var081
<li>getType() - Var082 to Var084
<li>getUserPermission() - Var085 to Var090
<li>getUserPermissions() - Var028 to Var030
<li>isCommitted() - Var091 to Var 093
<li>readObject() - Var094 to Var 096   
<li>removeAuthorizedUser() - Var097 to Var103
<li>removePropertyChangeListener() - Var104 to Var109 
<li>removeUserPermission() - Var110 to Var116
<li>setAuthorizationList() - Var117 to Var 126
<li>setSensitivityLevel() - Var127 to Var 135
<li>setSystem() - Var136 to Var144
<li>writeObject() - Var145 to Var 147
</ul>
**/
public class PermissionUnattendedTestcase
extends Testcase
{
    private static final boolean DEBUG = false;
    private CommandCall ccall_;
    private CommandCall ccallPow_;
    private String[] user={"PERUSER1","PERUSER2","PERUSER3","PERUSER4",
                    "PERUSER5","PERUSER6","PERUSER7"};

/**
Constructor.
**/
    public PermissionUnattendedTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String systemName)
    {
        super(systemObject, "PermissionUnattendedTestcase", namesAndVars, runMode, fileOutputStream);
        systemName_ = systemName;
    }

/**
Creates objects testusr41 and testflr1. 
**/
protected void setup () throws Exception
{
  // Lock the system so the variation can only run from one system. 
  lockSystem("PERMTEST", 300); 
   //  Trace.setTraceOn(true);
   //  Trace.setTraceInformationOn(true);
     try
     {     
        if (PermissionTestDriver.PwrSys == null)
        {
            failed("-pwrSys testcase parm not specified.");
	    throw new Exception("-pwrSys testcase parm not specified."); 
        }
        ccallPow_ = new CommandCall(PermissionTestDriver.PwrSys);
        ccall_ = new CommandCall(systemObject_);
        try
        {
            // Users need to be enrolled in the "system distribution directory"
            // in order to do CRTFLR.
            String userName = systemObject_.getUserId();
            String pwrUserName = PermissionTestDriver.PwrSys.getUserId();
            String sysName = systemObject_.getSystemName();
            if (sysName.equalsIgnoreCase("localhost"))
            {
              // sysName is used in subsequent ADDDIRE commands and       //@A1A
              // needs a real host name.                                  //@A1A
              sysName = InetAddress.getLocalHost().getHostName().toUpperCase().trim();//@A1A
	      int dotIndex = sysName.indexOf(".");
	      if (dotIndex > 0) { 
		  sysName = sysName.substring(0, dotIndex);       //@A1A
	      }
            }
        String commandString;
        if (userName.length() > 8) {
          commandString = "ADDDIRE USRID(" + userName.substring(0, 8) + " "
              + sysName + ") USRD('Test') USER(" + userName + ")";
        } else {
          commandString = "ADDDIRE USRID(" + userName + " " + sysName
              + ") USRD('Test') USER(" + userName + ")";
        }
        System.out.println("Setup: " + commandString);
        runCommand(ccallPow_, commandString);
        commandString = "ADDDIRE USRID(" + pwrUserName + " " + sysName
            + ") USRD('Test') USER(" + pwrUserName + ")";
        System.out.println("Setup: " + commandString);
        runCommand(ccallPow_, commandString);

        commandString = "CRTFLR FLR(TESTFLR1)";
        System.out.println("Setup: " + commandString);
        runCommand(ccall_, commandString);
        runCommand(ccall_, "CRTFLR FLR(TESTFLR)");
        runCommand(ccall_, "CRTFLR FLR(TESTFLR2) INFLR('TESTFLR')");
        runCommand(ccall_, "CRTFLR FLR(TESTFLR3) INFLR('TESTFLR')");
        runCommand(ccall_, "CRTLIB LIB(JTESTLIB1)");
        runCommand(ccall_, "CRTLIB LIB(JTESTLIB2)");
        runCommand(ccall_, "CRTSRCPF FILE(JTESTLIB2/FILE8) MBR(*NONE)");
        runCommand(ccall_, "CRTDIR DIR(TestDir1)");
        runCommand(ccallPow_, "CRTUSRPRF USRPRF(TESTUSR1) PASSWORD(JTEST1)");
        runCommand(ccallPow_, "CRTUSRPRF USRPRF(TESTUSR2) PASSWORD(JTEST2)");
        runCommand(ccallPow_, "CRTUSRPRF USRPRF(TESTUSR3) PASSWORD(JTEST3)");
        runCommand(ccallPow_, "CRTUSRPRF USRPRF(TESTUSR4) PASSWORD(JTEST4)");

        // This is needed in order for setOwner() to work:
        runCommand(ccallPow_, "ADDDIRE USRID(TESTUSR1 " + sysName
            + ") USRD('Test') USER(TESTUSR1)");
        runCommand(ccallPow_, "ADDDIRE USRID(TESTUSR2 " + sysName
            + ") USRD('Test') USER(TESTUSR2)");

        for (int i = 0; i <= 6; i++) {
          runCommand(ccallPow_,
              "CRTUSRPRF USRPRF(" + user[i] + ") PASSWORD(JTEAM1)");
        }
        runCommand(ccallPow_, "CRTSRCPF FILE(jtestlib2/file9) MBR(mbr1)");
        runCommand(ccallPow_, "CRTAUTL AUTL(TESTAUTL2)");
        runCommand(ccallPow_, "CRTAUTL AUTL(TESTAUTL1)");
        runCommand(ccallPow_, "CRTAUTL AUTL(TESTAUTL8)");
      } catch (Exception ex) {
        System.out.println(ex);
        ex.printStackTrace(System.out);
      }
    } catch (Throwable e) {
      // throws new Exception("permission");
       System.out.println(e);
     }
}

  private void runCommand(CommandCall ccall, String commandString)
      throws PropertyVetoException, AS400SecurityException,
      ErrorCompletingRequestException, IOException, InterruptedException {
    ccall.setCommand(commandString);
    boolean result = ccall.run();
    if (!result) {

      System.out.println("Command failed: " + commandString);
      AS400Message[] msgs = ccall.getMessageList();
      for (int j = 0; j < msgs.length; j++) {
        System.out.println(msgs[j].getText());
      }
      System.out.println("----------------------");
    }
  }

/**
Cleans objects that are created by setup method.
**/
protected void cleanup()
{
      try
      {
	  boolean result; 
	  String firstCommands[] = {

	      "DLTDLO DLO(*ALL) FLR(TESTFLR)",
        "DLTDLO DLO(*ALL)",
	      "DLTDLO DLO(TESTFLR1)",
	      "DLTDLO DLO(TESTFLR)",
	      "DLTF FILE(JTESTLIB2/file8)",
	      "DLTF FILE(JTESTLIB2/file9)",	    

	  };

      for (int i = 0; i < firstCommands.length; i++) {
        runCommand(ccallPow_,firstCommands[i]);
      }

	  deleteLibrary(ccallPow_,"JTESTLIB1");
	  deleteLibrary(ccallPow_,"JTESTLIB2");
          runCommand(ccallPow_,"RMVDIR DIR(TestDir1)");
          runCommand(ccallPow_,"DLTUSRPRF USRPRF(TESTUSR1) OWNOBJOPT(*DLT)");
          runCommand(ccallPow_,"DLTUSRPRF USRPRF(TESTUSR2) OWNOBJOPT(*DLT)");
          runCommand(ccallPow_,"DLTUSRPRF USRPRF(TESTUSR3) OWNOBJOPT(*DLT)");
          runCommand(ccallPow_,"DLTUSRPRF USRPRF(TESTUSR4) OWNOBJOPT(*DLT)");
          runCommand(ccallPow_,"DLTAUTL AUTL(TESTAUTL1)");
          runCommand(ccallPow_,"DLTAUTL AUTL(TESTAUTL8)");
          for(int i=0;i<=6;i++)
            {
                runCommand(ccallPow_,"DLTUSRPRF USRPRF("+user[i]+") OWNOBJOPT(*DLT)");
            } 
          runCommand(ccallPow_,"DLTAUTL AUTL(TESTAUTL2)");
          
          unlockSystem(); 
      }    
      catch(Exception ex) 
      {
           System.out.println(ex);
      }

      if (!isApplet_)
      {
        try
        {
          File tempFile = new File("t.tmp");
          tempFile.delete();
        } 
        catch(NullPointerException exe)
        {
          System.out.println("The file does not exist!");
        }
      }
     
}


    // Determines whether two UserPermission objects are equal.
    static boolean areEqual(UserPermission perm1, UserPermission perm2)
    {
      if (perm1 == null) return perm2 == null;
      if (perm2 == null) return false;
      if (perm1.getGroupIndicator() == perm2.getGroupIndicator() &&
          perm1.getUserID().equals(perm2.getUserID()) &&
          perm1.isFromAuthorizationList() == perm2.isFromAuthorizationList() &&
          perm1.isAuthorizationListManagement() == perm2.isAuthorizationListManagement())
        return true;
      else {
        System.out.println("The UserPermission object mismatch.");
        return false;
      }
    }

/**
Method tested: Permission()
 - Ensure that the constructor with one argument for document library objects (DLO) 
stored in QDLS runs well.  
**/

  public void Var001()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
        failed("-pwrSys testcase parm not specified.");
        return;
      }
    try
    {
        IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
        Permission per = new Permission(file);
        per.getType(); 
        succeeded();
    }
    catch (Exception e) {
            failed (e, "Unexpected Exception");
    }
  }

/**
Method tested: Permission()
 - Ensure that the constructor with one argument for traditional AS/400 library structure 
stored in QSYS.LIB runs well.
**/
  public void Var002()
  {
    try
    {
        IFSFile file = new IFSFile(systemObject_,"/QSYS.LIB/JTESTLIB1.LIB");
        Permission per = new Permission(file);
        per.getName(); 
        succeeded();
    }
    catch (Exception e) {
            failed (e, "Unexpected Exception");
    }
  }

/**
Method tested: Permission()
 - Ensure that the constructor with one argument for root directory structure runs well.
for root directory structure runs well.
**/
  public void Var003()
  {
   if (PermissionTestDriver.PwrSys == null)
    {
        failed("-pwrSys testcase parm not specified.");
        return;
      }
    try
    {
        IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
        Permission per = new Permission(file);
        per.getName(); 
        succeeded();
    }
    catch (Exception e) {
            failed (e, "Unexpected Exception");
    }
  }  

/**
Method tested: Permission(AS400 PermissionTestDriver.PwrSys,String name)
 - Ensure that the constructor with two arguments for document library objects (DLO) 
stored in QDLS runs well.  
**/

  public void Var004()
  {
     if (PermissionTestDriver.PwrSys == null)
     {
        failed("-pwrSys testcase parm not specified.");
        return;
      }
      try
      {
            Permission s = new Permission(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
            s.getName(); 
            succeeded();
      }
      catch (Exception e) {
            failed (e, "Unexpected Exception");
      }
  }

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the constructor with two arguments for traditional AS/400 library structure
stored in QSYS.LIB runs well.
**/

  public void Var005()
  {
      try
      {
            Permission s = new Permission(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
            s.getName(); 
            succeeded();
      }
      catch (Exception e) {
            failed (e, "Unexpected Exception");
      }
  }

/**
Method tested: Permission(AS400 PermissionTestDriver.PwrSys,String name)
 - Ensure that the constructor with two arguments for root directory structure runs well.
**/

  public void Var006()
  {
     if (PermissionTestDriver.PwrSys == null)
     {
        failed("-pwrSys testcase parm not specified.");
        return;
      }
      try
      {
            Permission s = new Permission(PermissionTestDriver.PwrSys,"/TestDir1");
            s.getName(); 
            succeeded();
      }
      catch (Exception e) {
            failed (e, "Unexpected Exception");
      }
  }  

/**
Method tested: Permission(IFSFile file)
 - Ensure that the NullPointerException will be thrown if the file is null
in constructor.
**/
   public void Var007()
  {
    try 
    {
       Permission s = new Permission(null);
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
    if (exceptionIs(e, "NullPointerException"))
       succeeded();
    else
       failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: Permission(IFSFile file)
 - Ensure that the AS400Exception will be thrown if specifying an invalid file in constructor with one argument 
for document library objects (DLO) stored in QDLS .  
**/
  public void Var008()
  {
    try {
        IFSFile file = new IFSFile(systemObject_,"/QDLS/Qdis/Qht");
        Permission per = new Permission(file);
        per.getName(); 
        failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: Permission(IFSFile file)
 - Ensure that the AS400Exception will be thrown if specifying an invalid file in constructor with one argument 
for traditional AS/400 library structure stored in QSYS.LIB.
**/
  public void Var009()
  {
    try {
        IFSFile file = new IFSFile(systemObject_,"/QSYS.LIB/WW");
        Permission per = new Permission(file);
        per.getName(); 
        failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: Permission(IFSFile file)
 - Ensure that the AS400Exception will be thrown if specifying an invalid file in constructor with one argument 
for root directory structure runs well.
**/
  public void Var010()
  {
    try {
        IFSFile file = new IFSFile(systemObject_,"/dev/tt");
        Permission per = new Permission(file);
        per.getName(); 
        failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }  

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the NullPointerException will be thrown if name is null in constructor.
**/
  public void Var011()
  {
    try 
    {
       Permission s = new Permission(systemObject_,null);
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }
   
/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the NullPointerException will be thrown if as400 is null in constructor
for document library objects (DLO) stored in QDLS.  
**/
  public void Var012()
  {
    try {
       Permission s = new Permission(null,"/QDLS/TESTFLR1");
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
}

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the NullPointerException will be thrown if as400 is null in constructor
for traditional AS/400 library structure stored in QSYS.LIB.
**/
  public void Var013()
  {
    try {
       Permission s = new Permission(null,"/QSYS.Lib/JTESTLIB1.Lib");
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
}

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the NullPointerException will be thrown if as400 is null in constructor
for root directory structure.
**/
  public void Var014()
  {
    try {
       Permission s = new Permission(null,"/TestDir1");
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
 }

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the AS400Exception will be thrown if specifying an invalid name in constructor with one argument 
for document library objects (DLO) stored in QDLS.  
**/
  public void Var015()
  {
    try
    {
       Permission s = new Permission(systemObject_,"/QDLS/Qdt");
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
}

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the AS400Exception will be thrown if specifying an invalid name in constructor with one argument 
for traditional AS/400 library structure stored in QSYS.LIB.
**/
  public void Var016()
  {
    try
    {
       Permission s = new Permission(systemObject_,"/QSYS.LIB/as");
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
}

/**
Method tested: Permission(AS400 systemObject_,String name)
 - Ensure that the AS400Exception will be thrown if specifying an invalid name in constructor with one argument 
for root directory structure.
**/
  public void Var017()
  {
    try
    {
       Permission s = new Permission(systemObject_,"/dev/qss");
       s.getName(); 
       failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
 }
 
/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that a user for document library objects (DLO) stored in QDLS  
can be added and its permission is created.
**/
 public void Var018()
  {
       int i,j;
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();

           boolean condition_ = false;

           IFSFile file1 = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           
           Permission per1 = new Permission(file1);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           for(j=0; e2.hasMoreElements();e2.nextElement())
           {
               j++;
           }
           DLOPermission dlop = (DLOPermission)per1.getUserPermission("TESTUSR1");
           String str = dlop.getDataAuthority();
           if((str.equals("*EXCLUDE")) && (j == i+1))
               condition_ = true;
           assertCondition(condition_);
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }           
  }
/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that a user for document library objects (DLO) stored in QDLS  
can be added and its permission is created.
**/
 public void Var019()
  {
       int i,j;
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR/TESTFLR2");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission sys = (DLOPermission)en.nextElement();
               if(sys.getUserID().equals("TESTUSR1"))
               per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
            
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           for(j=0; e2.hasMoreElements();e2.nextElement())
           {
               j++;
           }
           DLOPermission dlop = (DLOPermission)per1.getUserPermission("TESTUSR1");
           String str = dlop.getDataAuthority();
           if((str.equals("*EXCLUDE")) && (j == i+1))           
              succeeded();
           else failed();
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }                      
  }
/**
Method tested: addAuthorizedUser(),removeAuthorizedUser() and commit()
 - Ensure that a user for document library objects (DLO) stored in QDLS  
can be added and removed.
**/
 public void Var020()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR/TESTFLR2");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while (en.hasMoreElements()) 
           {
               DLOPermission sys = (DLOPermission)en.nextElement();
               if(sys.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
                          
           Permission per1 = new Permission(file);           
           
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           per1.removeAuthorizedUser("TESTUSR1");
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           succeeded();
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }
       
  }
/**
Method tested: addAuthorizedUser()
 - Ensure that a user whose type is autl for traditional AS/400 library 
structure stored in QSYS.LIB
can be added and removed when he/she has the authority of authorization list 
management.
**/
public void Var021()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/TESTAUTL8.AUTL");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
           while(en.hasMoreElements())
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
               {
                   per.removeAuthorizedUser(user[1]);
                   break;
               }
           }
            per.commit();
           
            Enumeration ee =(Enumeration) per.getUserPermissions();
            int i = 0; 
           for(i=0; ee.hasMoreElements();ee.nextElement())
           {               
               i++;
           }
           
            per.addAuthorizedUser(user[1]);
            per.commit();                    
            Permission per1 = new Permission(file);
            UserPermission permission = per1.getUserPermission(user[1]);
            permission.setAuthorizationListManagement(true);
            Enumeration p1 = per1.getUserPermissions();
            int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
            per1.commit();
            per1.removeAuthorizedUser(user[1]);                       
            per1.commit();
            per1.addAuthorizedUser(user[1]);
            per1.commit();
            Enumeration p2 = per1.getUserPermissions();
            int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }         
            assertCondition((s==k)&&(s==i+1), "s="+s+" k="+k+" i="+i);

    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }




/**
Method tested: addAuthorizedUser() 
 - Ensure that a user whose type is autl for traditional AS/400 library 
structure stored in QSYS.LIB
can be added and removed when he/she has no the authority of authorization 
list management.
**/
 public void Var022()
  {
       int i;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/TESTAUTL8.AUTL");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
           while(en.hasMoreElements())
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
               {
                   per.removeAuthorizedUser(user[1]);
                   break;
               }
           }
            per.commit();
            Enumeration ee =(Enumeration) per.getUserPermissions();
           for(i=0; ee.hasMoreElements();ee.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
            per.addAuthorizedUser(user[1]);
            per.commit(); 
            Permission per1 = new Permission(file);
            UserPermission permission = per1.getUserPermission(user[1]);
            permission.setAuthorizationListManagement(false);
            Enumeration p1 = per1.getUserPermissions();
            int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
            per1.commit();
            per.removeAuthorizedUser(user[1]);                       
            per.commit();
            per.addAuthorizedUser(user[1]);
            per.commit();
            Enumeration p2 = per1.getUserPermissions();
            int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }         
            assertCondition((s==k)&&(s==i+1), "s="+s+" k="+k+" i="+i);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }
 


/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that a user for traditional AS/400 library structure stored in QSYS.LIB
can be added and its permission is created.
**/
 public void Var023()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[3]))
                   per.removeAuthorizedUser(user[3]);
           }
           per.commit();
       
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           { 
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[3]);
           per1.commit();
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           for(j=0; e2.hasMoreElements();e2.nextElement())
           {
               j++;
           }
           QSYSPermission sysp = (QSYSPermission)per1.getUserPermission(user[3]);
           String str = sysp.getObjectAuthority();
           if((str.equals("*EXCLUDE")) && (j == i+1))
               condition_ = true;
           assertCondition(condition_);
  
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that a user for root directory structure can be added 
and its permission is created.
**/
 public void Var024()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
                   
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           per1.commit();
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           for(j=0; e2.hasMoreElements();e2.nextElement())
           {
               j++;
           }
           RootPermission rotp = (RootPermission)per1.getUserPermission("TESTUSR3");
           String str = rotp.getDataAuthority();
           if((str.equals("*EXCLUDE")) && (j == i+1))
               condition_ = true;
           assertCondition(condition_);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that the userPermission for document library objects (DLO) stored in QDLS  
is not changed after commit.
**/
 public void Var025()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while(en.hasMoreElements())
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
           
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           int i; 
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           per1.removeAuthorizedUser("TESTUSR1");                       
           per1.addAuthorizedUser("TESTUSR1");
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }            
           assertCondition(s==k, "s("+s+")!=k("+k+") i=("+i+")");
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that the userPermission for traditional AS/400 library structure stored in QSYS.LIB
is not changed after commit.
**/
 public void Var026()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           int i,j;
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[3]))
                   per.removeAuthorizedUser(user[3]);
           }
           per.commit();
             
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
         
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[3]);
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           per1.removeAuthorizedUser(user[3]);                       
           per1.addAuthorizedUser(user[3]);
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }
         
           assertCondition(s==k, "s="+s+" k="+k+" i="+i+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: addAuthorizedUser(), and commit()
 - Ensure that the userPermission for root directory structure 
is not changed after commit.
**/
 public void Var027()
  {
     int i,j; 
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
           
           
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           per1.removeAuthorizedUser("TESTUSR3");                       
           per1.addAuthorizedUser("TESTUSR3");
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }           
           assertCondition(s==k, "s="+s+" k="+k+" i="+i+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }


/**
Method tested: addAuthorizedUser(), and getUserPermissions()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after adding the user who is already an authorized user
for document library objects (DLO) stored in QDLS.  
**/
 public void Var028()
  {
        int j;
        Permission per ;
         if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       //remove an authorized user for document library objects (DLO) stored in QDLS.  
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();                    
            
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           // add a user who is already authorized.
           per1.addAuthorizedUser("TESTUSR1");
           per.commit();
           failed("Exception didn't occur. j="+j);
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
              succeeded();
           else
              failed(e, "Unexpected exception occurred.");
       }
 }
   
/**
Method tested: addAuthorizedUser(), and getUserPermissions()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after adding the user who is already an authorized user
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var029()
  {
        int j;
        Permission per,per1 ;
        if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[4]))
                   per.removeAuthorizedUser(user[4]);
           }
           per.commit();
          
           per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[4]);
           per1.commit();
           // add a user who is already authorized.
           per1.addAuthorizedUser(user[4]);
           per1.commit();
           failed("Exception didn't occur. j="+j);
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
}
   
/**
Method tested: addAuthorizedUser(), and getUserPermissions()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after adding the user who is already an authorized user
for root directory structure.
**/
 public void Var030()
  {
        Permission per,per1 ;
        int j;
        if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
              RootPermission rot = (RootPermission)en.nextElement();
              if(rot.getUserID().equals("TESTUSR3"))
                  per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
          
           per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           per1.commit();
           // add a user who is already authorized.
           per1.addAuthorizedUser("TESTUSR3");
           per1.commit();
           failed("Exception didn't occur. j="+j);
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
 }
   
/**
Listens for property change events.
**/
    private class PropertyChangeListener_
    implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void propertyChange (PropertyChangeEvent event)
        {

        lastEvent_ = event; }
    }
    
/**
Method tested: addPropertyChangeListener()
 - Ensure that adding a null listener cause an exception for document library objects (DLO)
stored in QDLS.  
**/
    public void Var031()
    {
        if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
            Permission per = new Permission(file);
            // add listener.
            per.addPropertyChangeListener(null);
            per.commit();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

/**
Method tested: addPropertyChangeListener()
 - Ensure that adding a null listener cause an exception for traditional AS/400 library structure
stored in QSYS.LIB.
**/
    public void Var032()
    {
        try
        {
            IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
            Permission per = new Permission(file);
            // add listener.
            per.addPropertyChangeListener(null);
            per.commit();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }

/**
Method tested: addPropertyChangeListener()
 - Ensure that adding a null listener cause an exception for root directory structure
**/
    public void Var033()
    {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
            Permission per = new Permission(file);
            // add listener.
            per.addPropertyChangeListener(null);
            per.commit();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }
 
/**
Method tested:addPropertyChangeListener()
 - Ensure that events are received after setAuthorizationList() 
for document library objects (DLO) stored in QDLS.  
**/
    public void Var034()
    {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
            Permission per = new Permission(file);
            // create an instance of PropertyChangeListener.
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            // add listener.
            per.addPropertyChangeListener (listener);
            per.commit();
            String str = per.getAuthorizationList();
            per.setAuthorizationList("TESTAUTL1");
            per.commit();
            assertCondition (listener.lastEvent_ != null);
            per.setAuthorizationList(str);
            per.commit();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception occurred");
        }
    }

/**
Method tested:addPropertyChangeListener()
 - Ensure that events are received after setAuthorizationList() 
for traditional AS/400 library structure stored in QSYS.LIB.
**/
    public void Var035()
    {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
       {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB2.Lib");
            Permission per = new Permission(file);
            // create an instance of PropertyChangeListener.
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            // add listener.
            per.addPropertyChangeListener (listener);
            per.commit();
            String str = per.getAuthorizationList();
            per.setAuthorizationList("TESTAUTL2");
            per.commit();
            assertCondition (listener.lastEvent_ != null);
            per.setAuthorizationList(str);                  
            per.commit();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception occurred");
        }
    }

/**
Method tested:addPropertyChangeListener()
 - Ensure that events are received after setAuthorizationList() for root directory structure.
**/
    public void Var036()
    {
        if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try
        {
      
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
            Permission per = new Permission(file);
            // create an instance of PropertyChangeListener.
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            // add listener.
            per.addPropertyChangeListener (listener);
            per.commit();
            String str = per.getAuthorizationList();
            per.setAuthorizationList("TESTAUTL1");
            per.commit();
            assertCondition (listener.lastEvent_ != null);
            per.setAuthorizationList(str);
            per.commit();
        }
        catch (Exception e) {
            failed(e, "Unexpected exception occurred");
        }
    }
 
/**
Method tested: addUserPermission()
 - Ensure that a userPermission will be added if the user is not an authorized user
for document library objects (DLO) stored in QDLS.  
**/
 public void Var037()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
           boolean condition_ = false;
           Permission per1 = new Permission(file);
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements(); e1.nextElement()) 
           { 
               i++;
           }
           // add a userPremission when the user is not authorized user before.
           DLOPermission  dlo = new DLOPermission("TESTUSR1");
           per1.addUserPermission(dlo);
           per1.commit();
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           while( e2.hasMoreElements())
           {
               DLOPermission dlop = (DLOPermission)e2.nextElement();
               if(dlop.getUserID().equals("TESTUSR1"))
               {
                  condition_ = true;                
                  break;
               }
           }
           Enumeration e3 =(Enumeration) per1.getUserPermissions();
           for(j=0; e3.hasMoreElements(); e3.nextElement()) 
           {
               j++;
           }
           if(j != i+1)
               condition_ = false;
           assertCondition(condition_);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }

  }

/**
Method tested: addUserPermission()
 - Ensure that a userPermission will be added if the user is not an authorized user
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var038()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[5]))
                   per.removeAuthorizedUser(user[5]);
           }
           per.commit();
       
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements(); e1.nextElement()) 
              i++;
            
           // add a userPremission when the user is not authorized user before.
           QSYSPermission sys = new QSYSPermission(user[5]);
           per1.addUserPermission(sys);
           per1.commit();
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           while( e2.hasMoreElements())
           {
               QSYSPermission sysp = (QSYSPermission)e2.nextElement();
               if(sysp.getUserID().equals(user[5]))
               {
                   condition_ = true;             
                   break;
               }
           }
           Enumeration e3 =(Enumeration) per1.getUserPermissions();
           for(j=0; e3.hasMoreElements(); e3.nextElement()) 
           {
               j++;
           }
           if(j != i+1)
               condition_ = false;
           assertCondition(condition_);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: addUserPermission()
 - Ensure that a userPermission will be added if the user is not an authorized user
for root directory structure.
**/
 public void Var039 ()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                  per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
       
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements(); e1.nextElement()) 
           { i++;
           }
           // add a userPremission when the user is not authorized user before.
           RootPermission rot = new RootPermission("TESTUSR3");
           per1.addUserPermission(rot);
           per1.commit();
           Enumeration e2 =(Enumeration)per1.getUserPermissions();
           while( e2.hasMoreElements())
           {
               RootPermission rotp = (RootPermission)e2.nextElement();
               if(rotp.getUserID().equals("TESTUSR3"))
               {
                   condition_ = true;
                   break;
               }
           }
           Enumeration e3 =(Enumeration) per1.getUserPermissions();
           for(j=0; e3.hasMoreElements(); e3.nextElement()) 
           {
               j++;
           }
           if(j != i+1)
               condition_ = false;
           assertCondition(condition_);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: addUserPermission()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after adding a user who is already an authorized user
for document library objects (DLO) stored in QDLS.  
**/
 public void Var040()
  {
     int j; 
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
                    
           Permission per1 = new Permission(file);
           // add a userPermission when the user is not authorized before.
           DLOPermission  dlo = new DLOPermission("TESTUSR1");
           per1.addUserPermission(dlo);
           per1.commit();

           DLOPermission   dlop = new DLOPermission("TESTUSR1");
           String str1 = (String)dlop.getDataAuthority();
           // add a userPermission after the user is already authorized .
           per1.addUserPermission(dlop);
           per1.commit();
           failed("Exception didn't occur.j="+j+" str1="+str1);
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
               succeeded();
           else
               failed(e, "Unexpected exception occurred.");
       }
  }
 
/**
Method tested: addUserPermission()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after adding a user who is already an authorized user
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var041()
  {
       int j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
           QSYSPermission sys = (QSYSPermission)en.nextElement();
           if(sys.getUserID().equals(user[1]))
           per.removeAuthorizedUser(user[1]);
           }
           per.commit();
       
           Permission per1 = new Permission(file);
           // add a userPermission when the user is not authorized before.
           QSYSPermission sys = new QSYSPermission(user[1]);
           per1.addUserPermission(sys);
           per1.commit();
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
               
           QSYSPermission  sysp = new QSYSPermission(user[1]);
           String str2 = (String)sysp.getObjectAuthority();
           // add a userPermission after the user is already authorized .
           per1.addUserPermission(sysp);
           per1.commit();
           failed("Exception didn't occur.j="+j+" e1="+e1+" str2="+str2);
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
               succeeded();
           else
               failed(e, "Unexpected exception occurred.");
       }
  }
 
/**
Method tested: addUserPermission()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after adding a user who is already an authorized user
for root directory structure.
**/
 public void Var042()
  {
       int j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                 per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
        
           Permission per1 = new Permission(file);
           // add a userPermission when the user is not authorized before.
           RootPermission rot = new RootPermission("TESTUSR3");
           per1.addUserPermission(rot);
           per1.commit();

           RootPermission  rotp = new RootPermission("TESTUSR3");
           String str3 = (String)rotp.getDataAuthority();
           // add a userPermission after the user is already authorized .
           per1.addUserPermission(rotp);
           per1.commit();
           failed("Exception didn't occur.j="+j+" str3="+str3);
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
              succeeded();
           else
              failed(e, "Unexpected exception occurred.");
       }
  }

/**
Method tested: addUserPermission(), and commit()
 - Ensure that the userPermission for document library objects (DLO) stored in QDLS
is not changed after commit. 
**/
 public void Var043()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           UserPermission permission = per1.getUserPermission("TESTUSR1");
           per1.removeAuthorizedUser("TESTUSR1"); 
            
           per1.addUserPermission(permission);
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }
           
           assertCondition(s==k, "s="+s+" k="+k+" i="+i+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: addUserPermission(), and commit()
 - Ensure that the userPermission for traditional AS/400 library structure stored in QSYS.LIB
is not changed after commit. 
**/
 public void Var044()
  {
       int j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[5]))
                   per.removeAuthorizedUser(user[5]);
           }
           per.commit();
        
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
            while (e1.hasMoreElements()) {
              e1.nextElement(); 
            }
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[5]);
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           UserPermission permission = per1.getUserPermission(user[5]);
           per1.removeAuthorizedUser(user[5]); 
         
           per1.addUserPermission(permission);
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }          
           assertCondition(s==k, "s="+s+" k="+k+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }


/**
Method tested: addUserPermission()
 - Ensure that the userPermission for root directory structure
is not changed after commit.
**/
 public void Var045()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
       
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           UserPermission permission = per1.getUserPermission("TESTUSR3");
           per1.removeAuthorizedUser("TESTUSR3"); 
            
           per1.addUserPermission(permission);
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }          
           assertCondition(s==k, "s="+s+" k="+k+" i="+i+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }
 /**
Method tested: commit(), and addAuthorizedUser()
 - Ensure that a user whose type is autl for traditional AS/400 library structure stored in QSYS.LIB
can be add.
**/
 public void Var046()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/TESTAUTL8.AUTL");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[6]))
               per.removeAuthorizedUser(user[6]);
           }
           per.commit();
       
           Permission per1 = new Permission(file);
           
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }        
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[6]);
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           
           
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }        
           assertCondition(s==k, "s="+s+" k="+k+" i="+i+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }
/**
Method tested: commit(), and addAuthorizedUser()
 - Ensure that the user which is stored in for traditional AS/400 library 
structure stored in QSYS.LIB
is not changed after commit.
**/
  public void Var047()
  {
       int i,j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB2.Lib/file8.file");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
               {
                   per.removeAuthorizedUser(user[1]);
                   break;
               }
           }
            per.commit();
            Permission per1 = new Permission(file);
            Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements();e1.nextElement())
           {               
               i++;
           }
       
            // add a user who is not authorized before.
            per1.addAuthorizedUser(user[1]);
            Enumeration p1 = per1.getUserPermissions();
            int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
            per1.commit();
            per1.removeAuthorizedUser(user[1]); 
            per1.addAuthorizedUser(user[1]);
            per1.commit();
            Enumeration p2 = per1.getUserPermissions();
            int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }        
           assertCondition(s==k, "s="+s+" k="+k+" i="+i+" j="+j);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }

/** 
Method tested: commit()
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/
 public void Var048()   
  {
    AS400 as400 = null;
    try
    {
          as400 = new AS400(systemObject_.getSystemName(),"TESTUSR1","JTEST1");
          IFSFile file = new IFSFile(as400,"/QDLS/TESTFLR/TESTFLR2");
          Permission per = new Permission(file);
          per.setSensitivityLevel(2);     
          per.commit();
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
    finally
    {
      if (as400 != null) try { as400.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
    }
    
  }

 /** 
Method tested: commit() - make condition "if(setAuthority.run()!=true)" true.
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/ 
public void Var049()
  {    
       notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
       return;
/*
       int i,j;               
       try
          {
           AS400 system = new AS400(systemObject_.getSystemName(),"testusr3","jtest3");
           IFSFile file = new IFSFile(systemObject_,"/QDLS/TESTFLR/TESTFLR2");
           boolean flag = true;
           Permission per = new Permission(file);
           DLOPermission userPermission=null;
               
           Enumeration e1 =(Enumeration) per.getUserPermissions();
           while(e1.hasMoreElements())
           {                 
               userPermission = (DLOPermission) e1.nextElement();
               if( userPermission.getUserID().equals("TESTUSR4"))
               {                     
                   flag = false;
                   break;
               }
           }    
           if (flag)
           {
               // add a user who is not authorized before.
               per.addAuthorizedUser("TESTUSR4");               
               per.commit();
           }         
           userPermission.setDataAuthority("*Use");
           userPermission.setDataAuthority("*Change");
           
        //   per.addAuthorizedUser("TESTUSR3");
           systemObject_.disconnectService(AS400.COMMAND);
           per.setSystem(system);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "AS400Exception"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
*/
  }
  
 /** 
Method tested: commit()
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/
 public void Var050()
  {
       notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
       return;
/*
       int i,j;              
       try
          {          
           boolean flag = true;
           UserPermission userPermission=null;
           AS400 system = new AS400(systemObject_.getSystemName(),"testusr1","jtest1");
           IFSFile file = new IFSFile(systemObject_,"/QDLS/TESTFLR/TESTFLR2");
           
           Permission per = new Permission(file);
           // remove the user permission.
           Enumeration e1 =(Enumeration) per.getUserPermissions();
           while(e1.hasMoreElements())
           {               
               userPermission = (UserPermission) e1.nextElement();
               if( userPermission.getUserID().equals("PERUSER1"))
               {
                   flag = false;
                   break;
               }
           }        
           if(flag)
           {
               per.addAuthorizedUser("PERUSER1");
               per.commit();
           }

           Enumeration e2 =(Enumeration) per.getUserPermissions();
           while(e2.hasMoreElements())
           {               
               userPermission = (UserPermission) e2.nextElement();
               if( userPermission.getUserID().equals("PERUSER1"))
                  break;
           }          
           per.removeUserPermission(userPermission);   
           systemObject_.disconnectService(AS400.COMMAND);
           per.setSystem(system);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {    
        if (exceptionIs(e, "AS400Exception"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
*/
  }

/** 
Method tested: commit()
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/ 
  public void Var051()
  {
       notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
       return;
/*
       int i,j;              
       try
          {
           boolean flag = true;
           UserPermission userPermission=null;
           AS400 system = new AS400(systemObject_.getSystemName(),"testusr1","jtest1");
           IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
           
           Permission per = new Permission(file);
           // remove the user permission.
           Enumeration e1 =(Enumeration) per.getUserPermissions();
           while(e1.hasMoreElements())
           {               
               userPermission = (UserPermission) e1.nextElement();
               if( userPermission.getUserID().equals("TESTUSR2"))
               {
                   flag = false;                   
                   break;
               }
           } 
           if (flag)
           {
               per.addAuthorizedUser("TESTUSR2");             
               per.commit();
           }
           per.removeUserPermission(userPermission);   
           systemObject_.disconnectService(AS400.COMMAND);
           per.setSystem(system);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "AS400Exception"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
*/
  }

  /** 
Method tested: commit()
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/ 
public void Var052()
  {
       notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
       return;
/*
       int i,j;  
       try
          {
           AS400 system = new AS400(systemObject_.getSystemName(),"testusr1","jtest1");
           IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
           
           Permission per = new Permission(file);
           UserPermission userPermission = null;
           Enumeration e1 =(Enumeration) per.getUserPermissions();
           while(e1.hasMoreElements())
           {               
               userPermission = (UserPermission) e1.nextElement();
               if( userPermission.getUserID().equals("TESTUSR1"))
               {
                   per.removeUserPermission(userPermission);
                   per.commit();
                   break;
               }
           }  
           
           // add a user who is not authorized before.
           per.addAuthorizedUser("TESTUSR1");
           per.commit();
           per.removeAuthorizedUser("TESTUSR1");
           per.addAuthorizedUser("TESTUSR1");
           systemObject_.disconnectService(AS400.COMMAND);
           per.setSystem(system);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "AS400Exception"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
*/
  } 

/** 
Method tested: commit()
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/
 
  public void Var053()
  {
       notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
       return;
/*
       int i,j;    
       try
          {
           boolean condition_ = false;
           UserPermission userPermission=null;
           AS400 system = new AS400(systemObject_.getSystemName(),"testusr1","jtest1");
           IFSFile file = new IFSFile(systemObject_,"/TestDir1");
           
           Permission per = new Permission(file);
           // remove the user permission.
           Enumeration e1 =(Enumeration) per.getUserPermissions();
           while(e1.hasMoreElements())
           {               
               userPermission = (UserPermission) e1.nextElement();
               if( userPermission.getUserID().equals("TESTUSR1"))
               {
                   per.removeUserPermission(userPermission);
                   per.commit();
                   break;
               }
           }             
           // add a user who is not authorized before.
           per.addAuthorizedUser("TESTUSR1");
           per.commit();
           Enumeration e2 =(Enumeration) per.getUserPermissions();
           while(e2.hasMoreElements())
           {               
               userPermission = (UserPermission) e2.nextElement();
               if( userPermission.getUserID().equals("TESTUSR1"))
                  break;
           }
           per.removeUserPermission(userPermission);   
           systemObject_.disconnectService(AS400.COMMAND);
           per.setSystem(system);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "AS400Exception"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
*/
  }

/** 
Method tested: commit()
 - Ensure that the method commit() does not run successfully when the user is not authorized.
**/
 
  public void Var054()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrsys testcase parm not specified.");
           return;
       }
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           
           Permission per = new Permission(file);
           // add 16 users.     
           for(int i=0;i<=6;i++)            
               per.addAuthorizedUser(user[i]);    
          
           per.commit();
           
           // add a user who is not authorized before.
           Permission per1 = new Permission(file);           
           
           assertCondition(true, "per1="+per1); 
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "AS400Exception"))
        {    
           failed("AS400Exception");
        }
        else
           failed(e, "Unexpected exception occurred.");
    }
  }

  
/**
Method tested: getAuthorizationList()
 - Ensure that the method returns correct result for document library objects (DLO)
stored in QDLS.  
**/
 public void Var055()
  {
     if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           assertCondition(per.getAuthorizationList() != null);
     }
     catch(Exception e)
     {
           failed(e, "Unexpected exception occurred");
     }
  }

/**
Method tested: getAuthorizationList()
 - Ensure that the method returns correct result for traditional AS/400 library structure
stored in QSYS.LIB.
**/
 public void Var056()
  {
     if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           assertCondition(per.getAuthorizationList() != null);
     }
     catch(Exception e)
     {
           failed(e, "Unexpected exception occurred");
     }
  }

/**
Method tested: getAuthorizationList()
 - Ensure that the method returns correct result for root directory structure.
**/
 public void Var057()
  {
     if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           assertCondition(per.getAuthorizationList() != null);
     }
     catch(Exception e)
     {
           failed(e, "Unexpected exception occurred");
     }
  }
      
/**
Method tested: getAuthorizedUsers()
 - Ensure that a user can be added and the authorized user list can be retrieved for document library objects (DLO)
stored in QDLS.  
**/
 public void Var058 ()
  {
       int j;
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for document library objects (DLO) stored in QDLS.  
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
       
           boolean condition_ = false;
           
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           Enumeration e1;
           for( e1 = (Enumeration)per1.getAuthorizedUsers(); e1.hasMoreElements();)
           {
               String name = (String)e1.nextElement();
               if(name.equals("TESTUSR1")) 
               {
                   condition_ = true;                
                   break;
               }
           }
           assertCondition(condition_, "j="+j);
       }
       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred");
       }
  }
      
/**
Method tested: getAuthorizedUsers()
 - Ensure that a user can be added and the authorized user list can be gotten for traditional AS/400 library structure
stored in QSYS.LIB.
**/
 public void Var059()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while (en.hasMoreElements())
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
                   per.removeAuthorizedUser(user[1]);
           }
           per.commit();
        
          boolean condition_ = false;
           
          Permission per1 = new Permission(file);
          // add a user who is not authorized before.
          per1.addAuthorizedUser(user[1]);
          per1.commit();
          for( Enumeration e1 = (Enumeration)per1.getAuthorizedUsers(); e1.hasMoreElements();) 
          {
               String name = (String)e1.nextElement();
               if(name.equals(user[1])) 
               {
                   condition_ = true;               
                   break;
               }
          }
          assertCondition(condition_);
     }
     catch(Exception e)
     {
          failed(e, "Unexpected exception occurred");
     }
  }
       
/**
Method tested: getAuthorizedUsers()
 - Ensure that a user can be added and the authorized user list for root directory structure.
**/
 public void Var060()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while(en.hasMoreElements())
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
        
           boolean condition_ = false;
          
           Permission per1 = new Permission(file);
          // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           per1.commit();
           Enumeration e1;
           for(e1 = (Enumeration)per1.getAuthorizedUsers(); e1.hasMoreElements();)
           {
               String name = (String)e1.nextElement();
               if(name.equals("TESTUSR3")) 
               {
                   condition_ = true;                
                   break;
               }
            }
          assertCondition(condition_);
      }
      catch(Exception e)
      {
          failed(e, "Unexpected exception occurred");
      }
  }

/**
Method tested: getName()
 - Ensure that the method returns the correct name of the object
for document library objects (DLO) stored in QDLS.  
**/
 public void Var061()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);         
          if (per.getName().equals("TESTFLR1"))
            succeeded();
          else
            failed("Incorrect name returned: '"+per.getName()+"' != 'TESTFLR1'");
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: getName()
 - Ensure that the method returns the correct name of the object
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var062()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          if (per.getName().equals("JTESTLIB1.Lib"))
            succeeded();
          else
            failed("Incorrect name returned: '"+per.getName()+"' != 'JTESTLIB1.Lib'");
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: getName()
 - Ensure that the method returns the correct name of the object
for root directory structure.
**/
 public void Var063()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          if (per.getName().equals("TestDir1"))
            succeeded();
          else
            failed("Incorrect name returned: '"+per.getName()+"' != 'TestDir1'");
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getOwner()
 - Ensure that the method returns the correct object owner
for document library objects (DLO) stored in QDLS.  
**/
 public void Var064()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          assertCondition(per.getOwner() != null);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getOwner()
 - Ensure that the method returns the correct object owner
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var065()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          assertCondition(per.getOwner() != null);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getOwner()
 - Ensure that the method returns the correct object owner 
for root directory structure.
**/
 public void Var066()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          assertCondition(per.getOwner() != null);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: getPath()
 - Ensure that the method returns the correct path
for document library objects (DLO) stored in QDLS.  
**/
 public void Var067()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
         IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
         Permission per = new Permission(file);
         if (per.getObjectPath().equals("/QDLS/TESTFLR1"))
           succeeded();
         else
           failed("Incorrect path returned: '"+per.getObjectPath()+"' != '/QDLS/TESTFLR1'");
    }
    catch(Exception e)
    {
         failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getPath()
 - Ensure that the method reutrns the correct path
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var068()
  {
    try
    {
         IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
         Permission per = new Permission(file);
         if (per.getObjectPath().equals("/QSYS.Lib/JTESTLIB1.Lib"))
           succeeded();
         else
           failed("Incorrect path returned: '"+per.getObjectPath()+"' != '/QSYS.Lib/JTESTLIB1.Lib'");
    }
    catch(Exception e)
    {
         failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getPath()
 - Ensure that the method reutrns the correct path
for root directory structure.
**/
 public void Var069()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
         IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
         Permission per = new Permission(file);
         if (per.getObjectPath().equals("/TestDir1"))
           succeeded();
         else
           failed("Incorrect path returned: '"+per.getObjectPath()+"' != '/TestDir1'");
    }
    catch(Exception e)
    {
         failed(e, "Unexpected exception occurred");
    }
  }  
 
/**
Method tested: getPrimaryGroup()
 - Ensure that the method returns the correct primary group
for document library objects (DLO) stored in QDLS.  
**/
 public void Var070()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          assertCondition(per.getPrimaryGroup() != null);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getPrimaryGroup()
 - Ensure that the method returns the correct primary group
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var071()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          assertCondition(per.getPrimaryGroup() != null);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getPrimaryGroup()
 - Ensure that the method returns the correct primary group
for root directory structure.
**/
 public void Var072()
  {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          assertCondition(per.getPrimaryGroup() != null);
          }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: getSensitivityLevel(), and setSensitivityLevel()
 - Ensure that getSensitivityLevel() returns which is set by setSensitivityLevel()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var073()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          int i = per.getSensitivityLevel(); 
          per.setSensitivityLevel(1);
          per.commit();
          assertCondition(per.getSensitivityLevel() == 1);
          per.setSensitivityLevel(i);
          per.commit();
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getSensitivityLevel(), and setSensitivityLevel() 
 - Ensure that getSensitivityLevel() returns which is set by setSensitivityLevel()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var074()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          int i = per.getSensitivityLevel(); 
          per.setSensitivityLevel(3);
          per.commit();
          assertCondition(per.getSensitivityLevel() == 3);
          per.setSensitivityLevel(i);
          per.commit();
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getSensitivityLevel(), and setSensitivityLevel()
 - Ensure that getSensitivityLevel() returns which is set by setSensitivityLevel()
for root directory structure.
**/
 public void Var075()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          int i = per.getSensitivityLevel(); 
          per.setSensitivityLevel(3);
          per.commit();
          assertCondition(per.getSensitivityLevel() == 3);
          per.setSensitivityLevel(i);
          per.commit();
          }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
  
/**
Method tested: getSystem()
 - Ensure that getSystem() returns correct AS400 object
for document library objects (DLO) stored in QDLS.  
**/
 public void Var076()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          assertCondition(per.getSystem().equals(PermissionTestDriver.PwrSys));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
   
/**
Method tested: getSystem()
 - Ensure that getSystem() returns correct AS400 object 
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var077()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          assertCondition(per.getSystem().equals(systemObject_));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
   
/**
Method tested: getSystem()
 - Ensure that getSystem() returns correct AS400 object
for root directory structure. 
**/
 public void Var078()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          assertCondition(per.getSystem().equals(PermissionTestDriver.PwrSys));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: getSystem(), and setSystem()
 - Ensure that getSystem() returns which is set by setSystem()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var079()
  {
    notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
    return;
/*
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          AS400 as = new AS400();
          PermissionTestDriver.PwrSys.disconnectService(AS400.COMMAND);
          per.setSystem(as);
          per.commit();
          assertCondition(per.getSystem().equals(as));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
*/
  }
   
/**
Method tested: getSystem(), and setSystem()
 - Ensure that getSystem() returns which is set by setSystem() 
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var080()
  {
    notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
    return;
/*
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          AS400 as = new AS400();
          systemObject_.disconnectService(AS400.COMMAND);
          per.setSystem(as);
          per.commit();
          assertCondition(per.getSystem().equals(as));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
*/
  }
   
/**
Method tested: getSystem(), and setSystem()
 - Ensure that getSystem() returns which is set by setSystem()
for root directory structure. 
**/
 public void Var081()
  {
    notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
    return;
/*
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          AS400 as = new AS400();
          PermissionTestDriver.PwrSys.disconnectService(AS400.COMMAND);
          per.setSystem(as);
          per.commit();
          assertCondition(per.getSystem().equals(as));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
*/
  }

/**
Method tested: getType()
 - Ensure that getType() returns Permission.TYPE_DLO for document library objects (DLO)
stored in QDLS.  
**/
 public void Var082()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          assertCondition(per.getType() == Permission.TYPE_DLO);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
   
/**
Method tested: getType()
 - Ensure that getType() returns Permission.TYPE_QSYS for traditional AS/400 library structure 
stored in QSYS.LIB.
**/
 public void Var083()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          assertCondition(per.getType() == Permission.TYPE_QSYS);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
   
/**
Method tested: getType()
 - Ensure that getType() returns Permission.TYPE_ROOT for root directory structure.
**/
 public void Var084()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          assertCondition(per.getType() == Permission.TYPE_ROOT);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
/**
Method tested: getUserPermission()
 - Ensure that the method returns the correct userPermission
for document library objects (DLO) stored in QDLS.  
**/
 public void Var085()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
      //remove an authorized user for document library objects (DLO) stored in QDLS.  
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while(en.hasMoreElements())
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
              
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           assertCondition(per1.getUserPermission("TESTUSR1").getUserID().equals("TESTUSR1"));
     }
     catch(Exception e)
     {
           failed(e, "Unexpected exception occurred");
     }
  }

/**
Method tested: getUserPermission()
 - Ensure that the method returns the correct userPermission for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var086()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while (en.hasMoreElements())           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
                   per.removeAuthorizedUser(user[1]);
           }
           per.commit();
        
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[1]);
           per1.commit();
           assertCondition(per1.getUserPermission(user[1]).getUserID().equals(user[1]));
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }
  }

/**
Method tested: getUserPermission()
 - Ensure that the method returns the correct userPermission
for root directory structure.
**/
 public void Var087()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while (en.hasMoreElements())
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
        
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           per1.commit();
           assertCondition(per1.getUserPermission("TESTUSR3").getUserID().equals("TESTUSR3"));
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }
  }  
 
/**
Method tested: getUserPermission()
 - Ensure that the result is null after using a invalid user name
for document library objects (DLO) stored in QDLS.  
**/
 public void Var088()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {       
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           assertCondition( per.getUserPermission("A!*c") == null, "got "+per.getUserPermission("A!*c")+"sb null");
    }
    catch(Exception e)
    {
           failed(e, "Unexpected exception occurred");
    }
 }

/**
Method tested: getUserPermission()
 - Ensure that the result is null after using a invalid user name
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var089()
  {
    try
    { 
           IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
	         UserPermission userPermission = per.getUserPermission("A!*c");
           // Changed 7/2/2020 -- This returns the public permission if the specified user 
	         // permissions does not exist. 
	         String userId = userPermission.getUserID(); 
           assertCondition( "*PUBLIC".equals(userId), "got "+userId+" sb public");
    }
    catch(Exception e)
    {
           failed(e, "Unexpected exception occurred");
    }
 }

/**
Method tested: getUserPermission()
 - Ensure that the result is null after using a invalid user name
for root directory structure.
**/
 public void Var090()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           assertCondition( per.getUserPermission("A!*c") == null, "got "+per.getUserPermission("A!*c")+" sb null");
    }
    catch(Exception e)
    {
           failed(e, "Unexpected exception occurred");
    }
 }

/**
Method tested: isCommitted()
 - Ensure that isCommitted() returns .
stored in QDLS.  
**/
 public void Var091()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {          
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.commit();
          assertCondition(per.isCommitted() == true);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
   
/**
Method tested: isCommitted()
 - Ensure that isCommitted() returns Permission.TYPE_QSYS for traditional AS/400 library structure 
stored in QSYS.LIB.
**/
 public void Var092()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          per.commit();
          assertCondition(per.isCommitted() == true);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
   
/**
Method tested: isCommitted()
 - Ensure that isCommitted() returns Permission.TYPE_ROOT for root directory structure.
**/
 public void Var093()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {        
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          per.commit();
          assertCondition(per.isCommitted() == true);
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: readObject()
 - Ensure that correct object will be returned by this method 
for document library objects (DLO) stored in QDLS.  
**/
  public void Var094()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);        
          FileOutputStream ostream = new FileOutputStream("t.tmp");
          ObjectOutputStream p = new ObjectOutputStream(ostream);          
          p.writeObject(per);           
          p.flush();
          ostream.close();

          FileInputStream istream = new FileInputStream("t.tmp");
          ObjectInputStream p1 = new ObjectInputStream(istream);          
          Permission permission = (Permission)p1.readObject();
          
          istream.close();
          if (permission.getObjectPath().equals("/QDLS/TESTFLR1"))
            succeeded();
          else
            failed("Incorrect path: '"+per.getObjectPath()+"' != '/QDLS/TESTFLR1'");
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: readObject()
 - Ensure that correct object will be returned by this method 
for traditional AS/400 library structure stored in QSYS.LIB.  
**/
  public void Var095()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
      try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);        
          FileOutputStream ostream = new FileOutputStream("t.tmp");
          ObjectOutputStream p = new ObjectOutputStream(ostream);          
          p.writeObject(per);           
          p.flush();
          ostream.close();

          FileInputStream istream = new FileInputStream("t.tmp");
          ObjectInputStream p1 = new ObjectInputStream(istream);          
          Permission permission = (Permission)p1.readObject();
          
          istream.close();
          if (permission.getObjectPath().equals("/QSYS.Lib/JTESTLIB1.Lib"))
            succeeded();
          else
            failed("Incorrect path: '"+per.getObjectPath()+"' != '/QSYS.Lib/JTESTLIB1.Lib'");
          
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: readObject()
 - Ensure that correct object will be returned by this method 
for root directory structure.  
**/
  public void Var096()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);        
          FileOutputStream ostream = new FileOutputStream("t.tmp");
          ObjectOutputStream p = new ObjectOutputStream(ostream);          
          p.writeObject(per);           
          p.flush();
          ostream.close();

          FileInputStream istream = new FileInputStream("t.tmp");
          ObjectInputStream p1 = new ObjectInputStream(istream);          
          Permission permission = (Permission)p1.readObject();
          
          istream.close();
          if (permission.getObjectPath().equals("/TestDir1"))
            succeeded();
          else
            failed("Incorrect path: '"+per.getObjectPath()+"' != '/TestDir1'");
          
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
 
 
/**
Method tested: removeAuthorizedUser()
 - Ensure that a user will be removed if he/she is an authorized user
for document library objects (DLO) stored in QDLS.  
**/
 public void Var097()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
      int i,j;
      //remove an authorized user for document library objects (DLO) stored in QDLS.  
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
      
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR1");
           per1.commit();
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements(); e1.nextElement())
           {  i++;
           }
           // remove a user who is authorized.
           per1.removeAuthorizedUser("TESTUSR1");
           per1.commit();
           Enumeration e2 =(Enumeration) per1.getUserPermissions();
           for(j=0; e2.hasMoreElements(); e2.nextElement())
           {
               j++;
           } 
           if(i == j+1)
               condition_ = true;
           Enumeration e3 =(Enumeration) per1.getUserPermissions();
           for(j=0; e3.hasMoreElements(); j++)
           {
               DLOPermission dlop1 = (DLOPermission)e3.nextElement();               
               if(dlop1.getUserID() == "TESTUSR1")
               {
                   condition_ = false;            
                   break;
               }
           }
           assertCondition(condition_);
          }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }
  }
 
/**
Method tested: removeAuthorizedUser()
 - Ensure that a user will be removed if he/she is an authorized user
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var098()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        int i,j;
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
                   per.removeAuthorizedUser(user[1]);
           }
           per.commit();
       
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[1]);
           per1.commit();
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements(); e1.nextElement())
           {  i++;
           }
           // remove a user who is authorized.
           per1.removeAuthorizedUser(user[1]);
           per1.commit();
           Enumeration e2 =(Enumeration) per1.getUserPermissions();
           for(j=0; e2.hasMoreElements(); e2.nextElement())
           {
              j++;
           }
           if(i == j+1)
              condition_ = true;
           Enumeration e3 =(Enumeration) per1.getUserPermissions();
           for(j=0; e3.hasMoreElements();j++)
           {
               QSYSPermission sysp1 = (QSYSPermission)e3.nextElement();
               if(sysp1.getUserID().equals(user[1]))
               {
                  condition_ = false;             
                  break;
              }
           }
           assertCondition(condition_);
          }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }
  }
 
/**
Method tested: removeAuthorizedUser()
 - Ensure that a user will be removed if he/she is an authorized user
for root directory structure.
**/
 public void Var099()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        int i,j;
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
        
           boolean condition_ = false;
            
           Permission per1 = new Permission(file);
           // add a user who is not authorized before.
           per1.addAuthorizedUser("TESTUSR3");
           per1.commit();
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           for(i=0; e1.hasMoreElements(); e1.nextElement())
           {  i++;
           }
           // remove a user who is authorized.
           per1.removeAuthorizedUser("TESTUSR3");
           per1.commit();
           Enumeration e2 =(Enumeration) per1.getUserPermissions();
           for(j=0; e2.hasMoreElements(); e2.nextElement())
           {
               j++;
           }
           if(i == j+1)
              condition_ = true;
          
           Enumeration e3 =(Enumeration) per1.getUserPermissions();
           for(j=0; e3.hasMoreElements(); j++)
           {
               RootPermission rotp1 = (RootPermission)e3.nextElement();
               if(rotp1.getUserID().equals("TESTUSR3"))
               {
                  condition_ = false;
                  break;
               }
           }
           assertCondition(condition_);
          }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }
  }
 
/**
Method tested: removeAuthorizedUser()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after removing the user who is not an authorized user
for document library objects (DLO) stored in QDLS.  
**/
 public void Var100()
   {
   if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
      //remove an authorized user for document library objects (DLO) stored in QDLS.  
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while( en.hasMoreElements())
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
      
           Permission per1 = new Permission(file);
           // remove a user who is not authorized before.
           per1.removeAuthorizedUser("TESTUSR1");
           per1.commit();
           failed("Exception didn't occur.");
     }
     catch(Exception e)
     {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
               succeeded();
           else
               failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: removeAuthorizedUser()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after removing the user who is not an authorized user
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var101()
   {
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while( en.hasMoreElements())
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals("TESTUSR2"))
                   per.removeAuthorizedUser("TESTUSR2");
           }
           per.commit();
        
           Permission per1 = new Permission(file);
           // remove a user who is not authorized before.
           per1.removeAuthorizedUser("TESTUSR2");
           per1.commit();
           failed("Exception didn't occur.");
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
  }

/**
Method tested: removeAuthorizedUser()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after removing the user who is not an authorized user
for root directory structure.
**/
 public void Var102()
   {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while( en.hasMoreElements())
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
       
           Permission per1 = new Permission(file);
           // remove a user who is not authorized before.
           per1.removeAuthorizedUser("TESTUSR3");
           per1.commit();
           failed("Exception didn't occur.");
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "ExtendedIllegalArgumentException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
  }
/**
Method tested: removeAuthorizedUser()
 - Ensure that a user whose type is autl for traditional AS/400 library 
structure stored in QSYS.LIB
can be removed when he/she is authorized.
**/
 public void Var103()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/TESTAUTL8.AUTL");
           Permission per = new Permission(file);
           
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           while(en.hasMoreElements())
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
                  per.removeAuthorizedUser(user[1]);
           }
           per.commit();

          
           Permission per1 = new Permission(file);
           Enumeration e1 =(Enumeration) per1.getUserPermissions();
           while( e1.hasMoreElements()) { 
             e1.nextElement();
             
           }
      
           // add a user who is not authorized before.
           per1.addAuthorizedUser(user[1]);
           Enumeration p1 = per1.getUserPermissions();
           int s=0;
           while(p1.hasMoreElements())
           {
              p1.nextElement();
              s += 1;
           }
           per1.commit();
           per1.removeAuthorizedUser(user[1]);                       
          
           Enumeration p2 = per1.getUserPermissions();
           int k=0;
           while(p2.hasMoreElements())
           {
              p2.nextElement();
              k += 1;
           }
       
           assertCondition(s==(k+1));
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred");
       }

       
  }  
/**
Method tested: removePropertyChangeListener()
 - Ensure that removing a null listener causes NullPointerException
for document library objects (DLO) stored in QDLS.  
**/
    public void Var104()
    {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
       {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
            Permission per = new Permission(file);
            // remove listener.
            per.removePropertyChangeListener (null);
            per.commit();
            failed("Exception didn't occur.");
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "NullPointerException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
    }

/**
Method tested: removePropertyChangeListener()
 - Ensure that removing a null listener causes NullPointerException
for traditional AS/400 library structure stored in QSYS.LIB.
**/
    public void Var105()
    {
        try
        {
            IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
            Permission per = new Permission(file);
            // remove listener.
            per.removePropertyChangeListener (null);
            per.commit();
            failed("Exception didn't occur.");
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "NullPointerException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
    }

/**
Method tested: removePropertyChangeListener()
 - Ensure that removing a null listener causes NullPointerException
for root directory structure.
**/
    public void Var106()
    {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
            Permission per = new Permission(file);
            // remove listener.
            per.removePropertyChangeListener (null);
            per.commit();
            failed("Exception didn't occur.");
       }
       catch(Exception e)
       {
           if (exceptionIs(e, "NullPointerException"))
            succeeded();
           else
            failed(e, "Unexpected exception occurred.");
       }
    }

/**
Method tested: removePropertyChangeListener()
 - Ensure that events are no longer received for document library objects (DLO) stored in QDLS.  
**/
    public void Var107()
    {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try 
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
            Permission per = new Permission(file);
            // create an instance of PropertyChangeListener.
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            // add listener.
            per.addPropertyChangeListener (listener);
            per.commit();
            // remove listener.
            per.removePropertyChangeListener (listener);
            per.commit();
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
Method tested: removePropertyChangeListener()
 - Ensure that events are no longer received for traditional AS/400 library structure stored in QSYS.LIB.
**/
    public void Var108()
    {
            try 
        {
            IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
            Permission per = new Permission(file);
            // create an instance of PropertyChangeListener.
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            // add listener.
            per.addPropertyChangeListener (listener);
            per.commit();
            // remove listener.
            per.removePropertyChangeListener (listener);
            per.commit();
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
Method tested: removePropertyChangeListener()
 - Ensure that events are no longer received for root directory structure.
**/
    public void Var109()
    {
    if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
            Permission per = new Permission(file);
            // create an instance of PropertyChangeListener.
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            // add listener.
            per.addPropertyChangeListener (listener);
            per.commit();
            // remove listener.
            per.removePropertyChangeListener (listener);
            per.commit();
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    
/**
Method tested: removeUserPermission()
 - Ensure that a userPermission will be removed if the user is an authorized user
for document library objects (DLO) stored in QDLS.  
**/
 public void Var110()
  {
        int i,j;
       //remove an authorized user for document library objects (DLO) stored in QDLS.  
if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
          {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               DLOPermission dlo = (DLOPermission)en.nextElement();
               if(dlo.getUserID().equals("TESTUSR1"))
                   per.removeAuthorizedUser("TESTUSR1");
           }
           per.commit();
       
          boolean condition_ = false;
          
          Permission per1 = new Permission(file);
          // add a userPermission .
          DLOPermission  dlop = new DLOPermission("TESTUSR1");
          per1.addUserPermission(dlop);
          per1.commit();

          Enumeration e1 =(Enumeration) per1.getUserPermissions();
          for(i=0; e1.hasMoreElements(); e1.nextElement())
           {
              i++;
           }
          // remove a userPermission after the user is already authorized.
          per1.removeUserPermission(dlop);
          per1.commit(); 
          Enumeration e2 =(Enumeration) per1.getUserPermissions();
          for(j=0; e2.hasMoreElements(); e2.nextElement())
           {
              j++;
           } 
          if(i == j+1)
            condition_ = true;
          Enumeration e3 =(Enumeration) per.getUserPermissions();
          for(j=0; e3.hasMoreElements(); j++)
          {
              DLOPermission dlop1 = (DLOPermission)e3.nextElement();
              if(dlop1.getUserID() == "TESTUSR1")
              {
                condition_ = false;             
                break;
              }
           }
          assertCondition(condition_);
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }
/**
 Method tested: removeUserPermission(), and commit()
 - Ensure that a user permission can be removed and commit successfully.
 **/
  public void Var111()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       try
          {
           UserPermission userPermission=null;

           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR/TESTFLR2");
           
           Permission per = new Permission(file);
           // remove the user permission.
           Enumeration e1 =(Enumeration) per.getUserPermissions();
           while(e1.hasMoreElements())
           {               
               userPermission = (UserPermission) e1.nextElement();
               if( userPermission.getUserID().equals("TESTUSR1"))
               {
                   per.removeUserPermission(userPermission);
                   break;
               }
           }          
           // add a user who is not authorized before.
           per.addAuthorizedUser("TESTUSR1");
           per.commit();
           Enumeration e2 =(Enumeration) per.getUserPermissions();
           while(e2.hasMoreElements())
           {               
               userPermission = (UserPermission) e2.nextElement();
               if( userPermission.getUserID().equals("TESTUSR1"))
               {
                   per.removeUserPermission(userPermission); 
                   break;
               }
           }                      
           per.commit();
           succeeded();
    }
    catch(Exception e)
    {
        failed(e, "Unexpected exception occurred");
    }
  }
/**
Method tested: removeUserPermission()
 - Ensure that a userPermission will be removed if the user is an authorized user
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var112()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        int i,j;
       //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements(); j++)
           {
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[1]))
                  per.removeAuthorizedUser(user[1]);
           }
           per.commit();
       
          boolean condition_ = false;
           
          Permission per1 = new Permission(file);
          // add a userPermission .
          QSYSPermission sysp = new QSYSPermission(user[1]);
          per1.addUserPermission(sysp);
          per1.commit();

          Enumeration e1 =(Enumeration) per1.getUserPermissions();
          for(i=0; e1.hasMoreElements(); e1.nextElement())
          {
             i++;
          }
          // remove a userPermission after the user is already authorized.
          per1.removeUserPermission(sysp);
          per1.commit(); 
          Enumeration e2 =(Enumeration) per1.getUserPermissions();
          for(j=0; e2.hasMoreElements(); e2.nextElement())
          {
              j++;
          }
          if(i == j+1)
             condition_ = true;
          Enumeration e3 =(Enumeration) per1.getUserPermissions();
          for(j=0; e3.hasMoreElements();j++)
           {
               QSYSPermission sysp1 = (QSYSPermission)e3.nextElement();
               if(sysp1.getUserID().equals(user[1]))
               {
                   condition_ = false;
                   break;
               }
           }
          assertCondition(condition_);
       }
       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred");
       }
  }

/**
Method tested: removeUserPermission()
 - Ensure that a userPermission will be removed if the user is an authorized user
for root directory structure.
**/
 public void Var113()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
       int i,j;
       //remove an authorized user for root directory structure.
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           // remove a user who is authorized.
           Enumeration en =(Enumeration)per.getUserPermissions();
           for(j=0; en.hasMoreElements();j++)
           {
               RootPermission rot = (RootPermission)en.nextElement();
               if(rot.getUserID().equals("TESTUSR3"))
                   per.removeAuthorizedUser("TESTUSR3");
           }
           per.commit();
       
          boolean condition_ = false;
          
          Permission per1 = new Permission(file);
          // add a userPermission .
          RootPermission rotp = new RootPermission("TESTUSR3");
          per1.addUserPermission(rotp);
          per1.commit();

          Enumeration e1 =(Enumeration) per1.getUserPermissions();
          for(i=0; e1.hasMoreElements(); e1.nextElement())
          { i++;
          }
          // remove a userPermission after the user is already authorized.
          per1.removeUserPermission(rotp);
          per1.commit(); 
          Enumeration e2 =(Enumeration) per1.getUserPermissions();
          for(j=0; e2.hasMoreElements(); e2.nextElement())
           {
               j++;
           }
          if(i == j+1)
              condition_ = true;
          
          Enumeration e3 =(Enumeration) per1.getUserPermissions();
          for(j=0; e3.hasMoreElements(); j++)
           {
               RootPermission rotp1 = (RootPermission)e3.nextElement();
               if(rotp1.getUserID() == "TESTUSR3")
               {
                   condition_ = false;           
                   break;
               }
           }
          assertCondition(condition_);
      }
      catch(Exception e)
      {
          failed(e, "Unexpected exception occurred");
      }
  }

/**
Method tested: removeUserPermission()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after removing a userPermission 
when the user is not authorized for document library objects (DLO) stored in QDLS.  
**/
 public void Var114()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
          //remove an authorized user for document library objects (DLO) stored in QDLS.  
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
            while ( en.hasMoreElements())
            {
                DLOPermission dlo = (DLOPermission)en.nextElement();
                if(dlo.getUserID().equals("TESTUSR1"))
                    per.removeAuthorizedUser("TESTUSR1");
            }
            per.commit();
        
            Permission per1 = new Permission(file);
            // remove a userPermission when the user is not authorized.
            DLOPermission  dlop = new DLOPermission("TESTUSR1");
            per1.removeUserPermission(dlop);
            per1.commit();
            failed("Exception didn't occur.");
       }
       catch(Exception e)
       {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
       }
  }

/**
Method tested: removeUserPermission()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after removing a userPermission 
when the user is not authorized for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var115()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
         int j;
         //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
       {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB1.Lib");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
            for(j=0; en.hasMoreElements(); j++)
            {
                QSYSPermission sys = (QSYSPermission)en.nextElement();
                if(sys.getUserID().equals("TESTUSR2"))
                    per.removeAuthorizedUser("TESTUSR2");
            }
            per.commit();
       
            Permission per1 = new Permission(file);
            // remove a userPermission when the user is not authorized.
            QSYSPermission sysp = new QSYSPermission("TESTUSR2");
            per1.removeUserPermission(sysp);
            per1.commit();
            failed("Exception didn't occur. j="+j);
       }
       catch(Exception e)
       {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
                succeeded();
            else
                failed(e, "Unexpected exception occurred.");
       }
  }

/**
Method tested: removeUserPermission()
 - Ensure that the ExtendedIllegalArgumentException will be thrown after removing a userPermission 
when the user is not authorized for root directory structure.
**/
 public void Var116()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        int j;
         //remove an authorized user for root directory structure.
        try
        {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
            for(j=0; en.hasMoreElements();j++)
            {
                RootPermission rot = (RootPermission)en.nextElement();
                if(rot.getUserID().equals("TESTUSR3"))
                    per.removeAuthorizedUser("TESTUSR3");
            }
            per.commit();
       
            Permission per1 = new Permission(file);
            // remove a userPermission when the user is not authorized.
            RootPermission rotp = new RootPermission("TESTUSR3");
            per1.removeUserPermission(rotp);
            per1.commit();
            failed("Exception didn't occur. j="+j);
       }
       catch(Exception e)
       {
            if (exceptionIs(e, "ExtendedIllegalArgumentException"))
             succeeded();
            else
             failed(e, "Unexpected exception occurred.");
       }
  }

/**
Method tested: setAuthorizationList(), and getAuthorizationList()
 - Ensure that getAuthorizationList() returns which is set by setAuthorizationList()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var117()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           String str = "TESTAUTL1";
           String s = per.getAuthorizationList();
           per.setAuthorizationList(str);
           per.commit();
           assertCondition(per.getAuthorizationList().equals("TESTAUTL1"));
           per.setAuthorizationList(s);
           per.commit();
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred");
     }
  }
 /**
  Method tested: setAuthorizationList(), and getAuthorizationList()
 - Ensure that getAuthorizationList() returns which is set by setAuthorizationList()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var118()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR/TESTFLR3");
           Permission per = new Permission(file);
           String str = "TESTAUTL1";
           per.setAuthorizationList(str);
           per.commit();
           assertCondition(per.getAuthorizationList().equals("TESTAUTL1"));
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred");
     }
  }

/**
Method tested:  setAuthorizationList(), and getAuthorizationList()
 - Ensure that getAuthorizationList() returns which is set by setAuthorizationList()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var119()
  {
     try
     {
           IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           String str = "TESTAUTL1";
           String s = per.getAuthorizationList();
           per.setAuthorizationList(str);
           per.commit();
           assertCondition(per.getAuthorizationList().equals("TESTAUTL1"));
           per.setAuthorizationList(s);
           per.commit();
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred");
     }
  }

/**
Method tested: setAuthorizationList(), and getAuthorizationList()
 - Ensure that getAuthorizationList() returns which is set by setAuthorizationList()
for root directory structure.
**/
 public void Var120()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           String str = "TESTAUTL1";
           String s = per.getAuthorizationList();
           per.setAuthorizationList(str);
           per.commit();
           assertCondition(per.getAuthorizationList().equals("TESTAUTL1"));
           per.setAuthorizationList(s);
           per.commit();
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred");
     }
  }

/**
Method tested: setAuthorizationList()
 - Ensure that the NullPointerException will be thrown if null is set by setAuthorizationList()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var121()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           per.setAuthorizationList(null);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: setAuthorizationList()
 - Ensure that the NullPointerException will be thrown if null is set by setAuthorizationList()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
/**
Method tested: setAuthorizationList()
 - Ensure that the NullPointerException will be thrown if null is set by 
setAuthorizationList()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
public void Var122()
  {
        if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
        //remove an authorized user for traditional AS/400 library structure stored in QSYS.LIB.
       try
          {
            IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.Lib/JTESTLIB2.Lib/file9.file/mbr1.mbr");
            Permission per = new Permission(file);
            // remove a user who is authorized.
            Enumeration en =(Enumeration)per.getUserPermissions();
            while(en.hasMoreElements())
            {   
               QSYSPermission sys = (QSYSPermission)en.nextElement();
               if(sys.getUserID().equals(user[6]))
               {
                   per.removeAuthorizedUser(user[6]);
               }
               if(sys.getUserID().equals(user[2]))
               {
                  per.removeAuthorizedUser(user[2]);
               }
            }
            per.commit();                 
            // add a user who is not authorized before.
            per.addAuthorizedUser(user[6]);
            QSYSPermission user1 = new QSYSPermission(user[2]);
            per.addUserPermission(user1);
            per.commit();
             
            per.removeAuthorizedUser(user[6]); 
            per.removeUserPermission(user1);
            per.commit();
//            succeeded();
            per.setAuthorizationList(null);
            failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: setAuthorizationList()
 - Ensure that the NullPointerException will be thrown if null is set by setAuthorizationList()
for root directory structure.
**/
 public void Var123()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
     try
     {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
           Permission per = new Permission(file);
           per.setAuthorizationList(null);
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }
  
/**
Method tested: setAuthorizationList()
 - Ensure that the AS400Exception will be thrown if an invalid value is set by setAuthorizationList()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var124()
  {
     try
     {
           IFSFile file = new IFSFile(systemObject_,"/QDLS/TESTFLR1");
           Permission per = new Permission(file);
           per.setAuthorizationList("A*CC");
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: setAuthorizationList()
 - Ensure that the AS400Exception will be thrown if an invalid value is set by setAuthorizationList()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var125()
  {
     try
     {
           IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
           Permission per = new Permission(file);
           per.setAuthorizationList("A*CC");
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: setAuthorizationList()
 - Ensure that the AS400Exception will be thrown if an in valid value is set by setAuthorizationList()
for root directory structure.
**/
 public void Var126()
  {
     try
     {
           IFSFile file = new IFSFile(systemObject_,"/TestDir1");
           Permission per = new Permission(file);
           per.setAuthorizationList("A*CC");
           per.commit();
           failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }
  
/**
Method tested: setSensitivityLevel()
 - Ensure that the ExtendedIllegalArgumentException will be thrown if an in valid value is set by setAuthorizationList()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var127()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(-1);
          per.commit();
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }
 
/**
Method tested: setSensitivityLevel() 
 - Ensure that the ExtendedIllegalArgumentException will be thrown if an in valid value is set by setAuthorizationList()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var128()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          per.setSensitivityLevel(-1);
          per.commit();
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }
 
/**
Method tested: setSensitivityLevel()
 - Ensure that the ExtendedIllegalArgumentException will be thrown if an in valid value is set by setAuthorizationList()
for root directory structure.
**/
 public void Var129()
  {if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(-1);
          per.commit();
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: setSensitivityLevel()
 - Ensure that the method setSensitivityLevel() runs successfully when the parameter is 1.
**/
 public void Var130()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR/TESTFLR2");
          Permission per = new Permission(file);
          per.setSensitivityLevel(1);
          per.commit();
          succeeded();
          
    }
    catch(Exception e)
    {
           failed(e, "Unexpected exception occurred.");
    }
  }
/**
Method tested: setSensitivityLevel()
 - Ensure that the method setSensitivityLevel() runs successfully when the parameter is 2.
**/
  public void Var131()
  {
    notApplicable();  //setSystem no longer allows a disconnect & then a setSystem
    return;
/*
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(2);
          per.commit();
//          succeeded();                    
    }
    catch(Exception e)
    {
          failed(e, "(1) Unexpected exception occurred.");
          return;
    }
    try
    {
          AS400 system = new AS400(systemObject_.getSystemName(),"testusr1","jtest1");
          IFSFile file = new IFSFile(systemObject_,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(2);
          systemObject_.disconnectService(AS400.COMMAND);
          per.setSystem(system);
          per.commit();
          failed("Exception didn't occur.");                    
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "AS400Exception"))
           succeeded();
       else
           failed(e, "(2) Unexpected exception occurred.");
    }
*/
  }

/**
Method tested: setSensitivityLevel()
 - Ensure that the method setSensitivityLevel() runs successfully when the parameter is 3.
**/
  public void Var132()
  {
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(3);
          per.commit();
          succeeded();
    }
    catch(Exception e)
    {   
           failed(e, "Unexpected exception occurred.");
    }
  }
  /**
Method tested: setSensitivityLevel()
 - Ensure that the method setSensitivityLevel() runs successfully when the parameter is 4.
**/
  public void Var133()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(4);
          per.commit();
          succeeded();
    }
    catch(Exception e)
    {
           failed(e, "Unexpected exception occurred.");
    }
  }
/**
Method tested: setSensitivityLevel()
 - Ensure that the method setSensitivityLevel() does not run successfully when the parameter is 5.
**/
  public void Var134()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          per.setSensitivityLevel(5);
          per.commit();
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
    
  }

/** 
Method tested: setSensitivityLevel()
 - Ensure that the method setSensitivityLevel() runs successfully when the parameter is valid.
**/
  public void Var135()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR/TESTFLR2");
          Permission per = new Permission(file);
          per.setSensitivityLevel(2);
          per.commit();
          succeeded();
    }
    catch(Exception e)
    {
       if (exceptionIs(e, "ExtendedIllegalArgumentException"))
           succeeded();
       else
           failed(e, "Unexpected exception occurred.");
    }
    
  }


/**
Method tested: setSystem()
 - Ensure that nothing will be happened after setting the same system by this method
for document library objects (DLO) stored in QDLS.
**/
 public void Var136()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          PermissionTestDriver.PwrSys.disconnectService(AS400.COMMAND);          
          per.setSystem(PermissionTestDriver.PwrSys);
          assertCondition(per.getSystem().equals(PermissionTestDriver.PwrSys));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }


/**
Method tested: setSystem()
 - Ensure that nothing will be happened after setting the same system by this method
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var137()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          systemObject_.disconnectService(AS400.COMMAND);    
          systemObject_.disconnectService(AS400.FILE) ;
          per.setSystem(systemObject_);
          assertCondition(per.getSystem().equals(systemObject_));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }


/**
Method tested: setSystem()
 - Ensure that nothing will be happened after setting the same system by this method
for root directory structure.
**/
 public void Var138()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          PermissionTestDriver.PwrSys.disconnectService(AS400.COMMAND);          
          per.setSystem(PermissionTestDriver.PwrSys);
          assertCondition(per.getSystem().equals(PermissionTestDriver.PwrSys));
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

  
/**
Method tested: setSystem()
 - Ensure that the NullPointerException will be thrown if null is set by setSystem()
for document library objects (DLO) stored in QDLS.  
**/
 public void Var139()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
         IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
         Permission per = new Permission(file);
         per.setSystem(null);
         failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "NullPointerException"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }
   
/**
Method tested: setSystem()
 - Ensure that the NullPointerException will be thrown if null is set by setSystem()
for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var140()
  {
    try
    {
         IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
         Permission per = new Permission(file);
         per.setSystem(null);
         failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "NullPointerException"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }
   
/**
Method tested: setSystem()
 - Ensure that the NullPointerException will be thrown if null is set by setSystem()
for root directory structure. 
**/
 public void Var141()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
         IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
         Permission per = new Permission(file);
         per.setSystem(null);
         failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "NullPointerException"))
            succeeded();
        else
            failed(e, "Unexpected exception occurred.");
    }
  }

/**
Method tested: setSystem()
 - Ensure that the system cannot be reset and ExtendedIllegalStateException will be thrown
after a connection for document library objects (DLO) stored in QDLS.  
**/
 public void Var142()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          //create a connection.
          systemObject_.connectService(AS400.COMMAND);
          AS400 as = new AS400();
          per.setSystem(as);
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "ExtendedIllegalStateException"))
           succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }
   
/**
Method tested: setSystem()
 - Ensure that the system cannot be reset and ExtendedIllegalStateException will be thrown
after a connection for traditional AS/400 library structure stored in QSYS.LIB.
**/
 public void Var143()
  {
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          //create a connection.
          systemObject_.connectService(AS400.COMMAND);
          AS400 as = new AS400();
          per.setSystem(as);
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "ExtendedIllegalStateException"))
            succeeded();
        else
            failed(e, "Unexpected exception occurred.");
    }
  }
   
/**
Method tested: setSystem()
 - Ensure that the system cannot be reset and ExtendedIllegalStateException will be thrown
after a connection for root directory structure. 
**/
 public void Var144()
  {
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          //create a connection.
          systemObject_.connectService(AS400.COMMAND);
          AS400 as = new AS400();
          per.setSystem(as);
          failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
        if (exceptionIs(e, "ExtendedIllegalStateException"))
            succeeded();
        else
           failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: writeObject()
 - Ensure that the method runs well result for document library objects (DLO)
stored in QDLS.  
**/

  public void Var145()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
          Permission per = new Permission(file);
          FileOutputStream ostream = new FileOutputStream("t.tmp");
          ObjectOutputStream p = new ObjectOutputStream(ostream);          
          p.writeObject(per);           
          p.flush();
          ostream.close();
          succeeded();
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: writeObject()
 - Ensure that the method runs well for traditional AS/400 library structure 
stored in QSYS.LIB.
**/

  public void Var146()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
    try
    {
          IFSFile file = new IFSFile(systemObject_,"/QSYS.Lib/JTESTLIB1.Lib");
          Permission per = new Permission(file);
          FileOutputStream ostream = new FileOutputStream("t.tmp");
          ObjectOutputStream p = new ObjectOutputStream(ostream);          
          p.writeObject(per);           
          p.flush();
          ostream.close();
          succeeded();
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }
  
/**
Method tested: writeObject()
 - Ensure that the method runs well for root directory structure.
**/
  
  public void Var147()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
  if (PermissionTestDriver.PwrSys == null)
       {
           failed("-pwrSys testcase parm not specified.");
           return;
       }
    try
    {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
          Permission per = new Permission(file);
          FileOutputStream ostream = new FileOutputStream("t.tmp");
          ObjectOutputStream p = new ObjectOutputStream(ostream);          
          p.writeObject(per);           
          p.flush();
          ostream.close();
          succeeded();
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }


  /**
   Method tested: getOwner, setOwner().
   Specify "revoke old authority" on the setOwner().
   - Ensure that getOwner() returns value which is set by setOwner()
   for document library objects (DLO) stored in QDLS.
   **/
  public void Var148()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
      failed("-pwrSys testcase parm not specified.");
      return;
    }
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
      Permission perm0 = new Permission(file);
      String owner0 = perm0.getOwner();

      UserPermission uPerm0 = perm0.getUserPermission(owner0);
      /*
       System.out.println("Original UserPermission for " + owner0 + ":"); 
       if (uPerm0 == null) System.out.println("null");
       else {
       System.out.println(uPerm0.getGroupIndicator() + ", " +
       uPerm0.getUserID() + ", " +
       uPerm0.isFromAuthorizationList() + ", " +
       uPerm0.isAuthorizationListManagement());
       }
       */
      String newOwner;
      if (owner0.equals("TESTUSR1")) newOwner = "TESTUSR2";
      else                            newOwner = "TESTUSR1";
      perm0.setOwner(newOwner, true);
      perm0.commit();

      Permission perm1 = new Permission(file);
      UserPermission uPerm1 = perm1.getUserPermission(owner0);
      /*
       System.out.println("Changed UserPermission for " + owner0 + ":");
       if (uPerm1 == null) System.out.println("null");
       else {
       System.out.println(uPerm1.getGroupIndicator() + ", " +
       uPerm1.getUserID() + ", " +
       uPerm1.isFromAuthorizationList() + ", " +
       uPerm1.isAuthorizationListManagement());
       }
       System.out.println("After commit(): Owner = " + perm1.getOwner());
       */
      String owner1 = perm1.getOwner();

      perm1.setOwner(owner0, true);
      perm1.commit();

      Permission perm2 = new Permission(file);
      UserPermission uPerm2 = perm2.getUserPermission(owner0);
      String owner2 = perm2.getOwner();

      assertCondition(owner1.equals(newOwner) &&
             uPerm1 == null &&     // original owner's permission got revoked
             owner2.equals(owner0) &&  // owner restored to original
             areEqual(uPerm2, uPerm0));  // permissions restored to original
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }
  }


  /**
   Method tested: getOwner, setOwner().
   Specify "do not revoke old authority" on the setOwner().
   - Ensure that getOwner() returns value which is set by setOwner()
   for document library objects (DLO) stored in QDLS.
   **/
  public void Var149()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
      failed("-pwrSys testcase parm not specified.");
      return;
    }
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
      Permission perm0 = new Permission(file);
      String owner0 = perm0.getOwner();

      UserPermission uPerm0 = perm0.getUserPermission(owner0);
      /*
       System.out.println("Original UserPermission for " + owner0 + ":"); 
       if (uPerm0 == null) System.out.println("null");
       else {
       System.out.println(uPerm0.getGroupIndicator() + ", " +
       uPerm0.getUserID() + ", " +
       uPerm0.isFromAuthorizationList() + ", " +
       uPerm0.isAuthorizationListManagement());
       }
       */
      String newOwner;
      if (owner0.equals("TESTUSR1")) newOwner = "TESTUSR2";
      else                            newOwner = "TESTUSR1";
      perm0.setOwner(newOwner, false);
      perm0.commit();

      Permission perm1 = new Permission(file);
      UserPermission uPerm1 = perm1.getUserPermission(owner0);
      /*
       System.out.println("Changed UserPermission for " + owner0 + ":");
       if (uPerm1 == null) System.out.println("null");
       else {
       System.out.println(uPerm1.getGroupIndicator() + ", " +
       uPerm1.getUserID() + ", " +
       uPerm1.isFromAuthorizationList() + ", " +
       uPerm1.isAuthorizationListManagement());
       }
       System.out.println("After commit(): Owner = " + perm1.getOwner());
       */
      String owner1 = perm1.getOwner();

      perm1.setOwner(owner0, true);
      perm1.commit();

      Permission perm2 = new Permission(file);
      UserPermission uPerm2 = perm2.getUserPermission(owner0);
      String owner2 = perm2.getOwner();

      assertCondition(owner1.equals(newOwner) &&
             areEqual(uPerm1, uPerm0)&& // permissions preserverd
             owner2.equals(owner0) &&   // owner restored to original
             areEqual(uPerm2, uPerm0)); // permissions restored to original
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }
  }


  /**
   Method tested: getOwner(), and setOwner().
   Specify "revoke old authority" on the setOwner().
   - Ensure that getOwner() returns value which is set by setOwner()
   for traditional AS/400 library structure stored in QSYS.LIB.
   **/
  public void Var150()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
      failed("-pwrSys testcase parm not specified.");
      return;
    }
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/JTESTLIB1.LIB");
      Permission perm0 = new Permission(file);
      String owner0 = perm0.getOwner();

      UserPermission uPerm0 = perm0.getUserPermission(owner0);
      /*
       System.out.println("Original UserPermission for " + owner0 + ":"); 
       if (uPerm0 == null) System.out.println("null");
       else {
       System.out.println(uPerm0.getGroupIndicator() + ", " +
       uPerm0.getUserID() + ", " +
       uPerm0.isFromAuthorizationList() + ", " +
       uPerm0.isAuthorizationListManagement());
       }
       */
      String newOwner;
      if (owner0.equals("TESTUSR1")) newOwner = "TESTUSR2";
      else                            newOwner = "TESTUSR1";
      perm0.setOwner(newOwner, true);
      perm0.commit();

      Permission perm1 = new Permission(file);
      UserPermission uPerm1 = perm1.getUserPermission(owner0);
      /*
       System.out.println("Changed UserPermission for " + owner0 + ":");
       if (uPerm1 == null) System.out.println("null");
       else {
       System.out.println(uPerm1.getGroupIndicator() + ", " +
       uPerm1.getUserID() + ", " +
       uPerm1.isFromAuthorizationList() + ", " +
       uPerm1.isAuthorizationListManagement());
       }
       System.out.println("After commit(): Owner = " + perm1.getOwner());
       */
      String owner1 = perm1.getOwner();

      perm1.setOwner(owner0, true);
      perm1.commit();

      Permission perm2 = new Permission(file);
      UserPermission uPerm2 = perm2.getUserPermission(owner0);
      String owner2 = perm2.getOwner();

      assertCondition(owner1.equals(newOwner) &&
             uPerm1 != null &&  "*PUBLIC".equals(uPerm1.getUserID()) &&   // original owner's permission no longer present
             owner2.equals(owner0) &&  // owner restored to original
             areEqual(uPerm2, uPerm0),
             "owner1="+owner1+" newOwner="+newOwner+" uPerm1="+uPerm1+" owner2="+owner2+" owner0="+owner0+" uPerm2="+uPerm2+" uPerm0="+uPerm0);  // permissions restored to original
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }
  }


  /**
   Method tested: getOwner(), and setOwner().
   Specify "do not revoke old authority" on the setOwner().
   - Ensure that getOwner() returns value which is set by setOwner()
   for traditional AS/400 library structure stored in QSYS.LIB.
   **/
  public void Var151()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
      failed("-pwrSys testcase parm not specified.");
      return;
    }
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/JTESTLIB1.LIB");
      Permission perm0 = new Permission(file);
      String owner0 = perm0.getOwner();

      UserPermission uPerm0 = perm0.getUserPermission(owner0);
      /*
       System.out.println("Original UserPermission for " + owner0 + ":"); 
       if (uPerm0 == null) System.out.println("null");
       else {
       System.out.println(uPerm0.getGroupIndicator() + ", " +
       uPerm0.getUserID() + ", " +
       uPerm0.isFromAuthorizationList() + ", " +
       uPerm0.isAuthorizationListManagement());
       }
       */
      String newOwner;
      if (owner0.equals("TESTUSR1")) newOwner = "TESTUSR2";
      else                            newOwner = "TESTUSR1";
      perm0.setOwner(newOwner, false);
      perm0.commit();

      Permission perm1 = new Permission(file);
      UserPermission uPerm1 = perm1.getUserPermission(owner0);
      /*
       System.out.println("Changed UserPermission for " + owner0 + ":");
       if (uPerm1 == null) System.out.println("null");
       else {
       System.out.println(uPerm1.getGroupIndicator() + ", " +
       uPerm1.getUserID() + ", " +
       uPerm1.isFromAuthorizationList() + ", " +
       uPerm1.isAuthorizationListManagement());
       }
       System.out.println("After commit(): Owner = " + perm1.getOwner());
       */
      String owner1 = perm1.getOwner();

      perm1.setOwner(owner0, true);
      perm1.commit();

      Permission perm2 = new Permission(file);
      UserPermission uPerm2 = perm2.getUserPermission(owner0);
      String owner2 = perm2.getOwner();

      assertCondition(owner1.equals(newOwner) &&
             areEqual(uPerm1, uPerm0)&& // permissions preserverd
             owner2.equals(owner0) &&   // owner restored to original
             areEqual(uPerm2, uPerm0)); // permissions restored to original
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }
  }

  /**
   Method tested: getOwner(), and setOwner().
   Specify "revoke old authority" on the setOwner().
   - Ensure that getOwner() returns value which is set by setOwner()
   for root directory structure.
   **/
  public void Var152()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
      failed("-pwrSys testcase parm not specified.");
      return;
    }
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
      Permission perm0 = new Permission(file);
      String owner0 = perm0.getOwner();

      UserPermission uPerm0 = perm0.getUserPermission(owner0);
      /*
       System.out.println("Original UserPermission for " + owner0 + ":"); 
       if (uPerm0 == null) System.out.println("null");
       else {
       System.out.println(uPerm0.getGroupIndicator() + ", " +
       uPerm0.getUserID() + ", " +
       uPerm0.isFromAuthorizationList() + ", " +
       uPerm0.isAuthorizationListManagement());
       }
       */
      String newOwner;
      if (owner0.equals("TESTUSR1")) newOwner = "TESTUSR2";
      else                            newOwner = "TESTUSR1";
      perm0.setOwner(newOwner, true);
      perm0.commit();

      Permission perm1 = new Permission(file);
      UserPermission uPerm1 = perm1.getUserPermission(owner0);
      /*
       System.out.println("Changed UserPermission for " + owner0 + ":");
       if (uPerm1 == null) System.out.println("null");
       else {
       System.out.println(uPerm1.getGroupIndicator() + ", " +
       uPerm1.getUserID() + ", " +
       uPerm1.isFromAuthorizationList() + ", " +
       uPerm1.isAuthorizationListManagement());
       }
       System.out.println("After commit(): Owner = " + perm1.getOwner());
       */
      String owner1 = perm1.getOwner();

      perm1.setOwner(owner0, true);
      perm1.commit();

      Permission perm2 = new Permission(file);
      UserPermission uPerm2 = perm2.getUserPermission(owner0);
      String owner2 = perm2.getOwner();

      assertCondition(owner1.equals(newOwner) &&
             uPerm1 == null &&     // original owner's permission got revoked
             owner2.equals(owner0) &&  // owner restored to original
             areEqual(uPerm2, uPerm0));  // permissions restored to original
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }
  }

  /**
   Method tested: getOwner(), and setOwner().
   Specify "do not revoke old authority" on the setOwner().
   - Ensure that getOwner() returns value which is set by setOwner()
   for root directory structure.
   **/
  public void Var153()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
      failed("-pwrSys testcase parm not specified.");
      return;
    }
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/TestDir1");
      Permission perm0 = new Permission(file);
      String owner0 = perm0.getOwner();

      UserPermission uPerm0 = perm0.getUserPermission(owner0);
      /*
       System.out.println("Original UserPermission for " + owner0 + ":"); 
       if (uPerm0 == null) System.out.println("null");
       else {
       System.out.println(uPerm0.getGroupIndicator() + ", " +
       uPerm0.getUserID() + ", " +
       uPerm0.isFromAuthorizationList() + ", " +
       uPerm0.isAuthorizationListManagement());
       }
       */
      String newOwner;
      if (owner0.equals("TESTUSR1")) newOwner = "TESTUSR2";
      else                            newOwner = "TESTUSR1";
      perm0.setOwner(newOwner, false);
      perm0.commit();

      Permission perm1 = new Permission(file);
      UserPermission uPerm1 = perm1.getUserPermission(owner0);
      /*
       System.out.println("Changed UserPermission for " + owner0 + ":");
       if (uPerm1 == null) System.out.println("null");
       else {
       System.out.println(uPerm1.getGroupIndicator() + ", " +
       uPerm1.getUserID() + ", " +
       uPerm1.isFromAuthorizationList() + ", " +
       uPerm1.isAuthorizationListManagement());
       }
       System.out.println("After commit(): Owner = " + perm1.getOwner());
       */
      String owner1 = perm1.getOwner();

      perm1.setOwner(owner0, true);
      perm1.commit();

      Permission perm2 = new Permission(file);
      UserPermission uPerm2 = perm2.getUserPermission(owner0);
      String owner2 = perm2.getOwner();

      assertCondition(owner1.equals(newOwner) &&
             areEqual(uPerm1, uPerm0)&& // permissions preserverd
             owner2.equals(owner0) &&   // owner restored to original
             areEqual(uPerm2, uPerm0)); // permissions restored to original
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }
  }

  private void createFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      file.createNewFile();
    }
    catch(Exception e)
    {
      if (DEBUG)
        e.printStackTrace(output_);
    }
  }


  private void createDirectory(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      file.mkdirs();
    }
    catch(Exception e)
    {
      if (DEBUG)
        e.printStackTrace(output_);
    }
  }

  void deleteFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(PermissionTestDriver.PwrSys, pathName);
      if (file.exists())
      {
        file.delete();
      }
    }
    catch(Exception e)
    {
      if (DEBUG)
        e.printStackTrace(output_);
    }
  }

/**
Method tested: setPrimaryGroup()
 - Ensure that the method correctly sets the primary group
for a file in QOpenSys.
This variation was added to verify the fix for CPS 86RPQQ on 2010-07-09.
**/
 public void Var154()
  {
   if (PermissionTestDriver.PwrSys == null)
   {
     failed("-pwrSys testcase parm not specified.");
     return;
   }
   String directory  = "/QOpenSys/home/java";  // mixed case
   final String path = "/QOpenSys/home/java/file154";  // mixed case
   try
   {
     createDirectory(directory); 
     createFile(path);
     IFSFile file = new IFSFile(PermissionTestDriver.PwrSys, path);
     Permission per = new Permission(file);
     String oldGroup = per.getPrimaryGroup();
     per.setPrimaryGroup(oldGroup, false);  // This way we avoid having to come up with another valid group name.
     per.commit();
     assertCondition(per.getPrimaryGroup().equals(oldGroup));
   }
   catch(Exception e)
   {
     failed(e, "Unexpected exception occurred");
   }
   finally {
     deleteFile(path);
   }
  }

/**
Method tested: setPrimaryGroup()
 - Ensure that the method correctly sets the primary group
for a file in the root file system.
This variation was added to verify the fix for CPS 86RPQQ on 2010-07-09.
**/
 public void Var155()
  {
   if (PermissionTestDriver.PwrSys == null)
   {
     failed("-pwrSys testcase parm not specified.");
     return;
   }
   final String path = "/tmp/file155";  // mixed case
   try
   {
     createFile(path);
     IFSFile file = new IFSFile(PermissionTestDriver.PwrSys, path);
     Permission per = new Permission(file);
     String oldGroup = per.getPrimaryGroup();
     per.setPrimaryGroup(oldGroup, false);  // This way we avoid having to come up with another valid group name.
     per.commit();
     assertCondition(per.getPrimaryGroup().equals(oldGroup));
   }
   catch(Exception e)
   {
     failed(e, "Unexpected exception occurred");
   }
   finally {
     deleteFile(path);
   }
  }

/**
Method tested: setPrimaryGroup()
 - Ensure that the method correctly sets the primary group
for a file in QSYS.
This variation was added to verify the fix for CPS 86RPQQ on 2010-07-09.
**/
 public void Var156()
  {
   if (PermissionTestDriver.PwrSys == null)
   {
     failed("-pwrSys testcase parm not specified.");
     return;
   }
   final String path = "/Qsys.lib/JTESTLIB1.lib";  // mixed case
   try
   {
     IFSFile file = new IFSFile(PermissionTestDriver.PwrSys, path);
     Permission per = new Permission(file);
     String oldGroup = per.getPrimaryGroup();
     per.setPrimaryGroup(oldGroup, false);  // This way we avoid having to come up with another valid group name.
     per.commit();
     assertCondition(per.getPrimaryGroup().equals(oldGroup));
   }
   catch(Exception e)
   {
     failed(e, "Unexpected exception occurred");
   }
   finally {
     deleteFile(path);
   }
  }

}
