///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PermissionDLOUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Permission;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.Permission;
import com.ibm.as400.access.DLOPermission;
import com.ibm.as400.access.RootPermission;

import test.PermissionTestDriver;
import test.Testcase;

import com.ibm.as400.access.CommandCall;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.io.File;


/**
The PermissionDLOUnattendedTestcase provides testcases to test the methods of DLOPermission.

<p>This tests the following DLOPermission methods:
<ul>
<li>DLOPermission() - from Var001 to Var003 
<li>getDataAuthority() and setDataAuthority() - from Var004 to Var012
<li>readObject() and writeObject() - Var013 to Var014
</ul>
**/

public class PermissionDLOUnattendedTestcase
extends Testcase
{


  //private data
  private CommandCall ccall_;
  private CommandCall ccallPow_;
  private String systemName_;

  /*private*/ static final boolean DEBUG       = false; //@A2A
  // Sensitivity levels: (for var015 @A2A)
  public static final int SENSLVL_NONE         = 1; // (*NONE) The document has no sensitivity restrictions. 
  public static final int SENSLVL_PERSONAL     = 2; // (*PERSONAL) The document is intended for the user as an individual. 
  public static final int SENSLVL_PRIVATE      = 3; // (*PRIVATE) The document contains information that should be accessed only by the owner. This value cannot be specified if the access code zero (0) is assigned to the object. 
  public static final int SENSLVL_CONFIDENTIAL = 4; // (*CONFIDENTIAL) The document contains information that should be handled according to company procedures. 

  private StringBuffer setupStringBuffer = new StringBuffer();
  boolean setupStringBufferPrinted = false; 

/**
Constructor.
**/
    public PermissionDLOUnattendedTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream, 
                             
                             String systemName)
    {
        super(systemObject, "PermissionDLOUnattendedTestcase", namesAndVars, runMode, fileOutputStream);
        systemName_ = systemName;
    }

/**
Creates objects testuser41 and testflr1. 
**/
protected void setup () //throws Exception
{
     try
     {    
       boolean result; 
        ccallPow_ = new CommandCall(PermissionTestDriver.PwrSys);
        ccall_ = new CommandCall(systemObject_);
        try
        {
          // Users need to be enrolled in the "system distribution directory"
          // in order to do CRTFLR.
          String userName = systemObject_.getUserId();
          String pwrUserName = PermissionTestDriver.PwrSys.getUserId();
          String sysName = systemObject_.getSystemName();
          if (sysName.equalsIgnoreCase("localhost")) sysName = systemName_;
          
          String command="ADDDIRE USRID("+userName +" "+ sysName+") USRD('Test') USER("+userName+")"; 
          ccallPow_.setCommand(command);
          result = ccallPow_.run();
          setupStringBuffer.append("ccalPow_("+command+") returned "+result+"\n"); 
          
          command = "ADDDIRE USRID("+pwrUserName +" "+ sysName+") USRD('Test') USER("+pwrUserName+")";
          ccallPow_.setCommand(command);
          result = ccallPow_.run();
          setupStringBuffer.append("ccalPow_("+command+") returned "+result+"\n"); 

          command="CRTUSRPRF USRPRF(TESTUSER41) PASSWORD(JTEAM1)";
          ccallPow_.setCommand(command);
          result = ccallPow_.run();
          setupStringBuffer.append("ccalPow_("+command+") returned "+result+"\n"); 
          
          command="CRTFLR FLR(TESTFLR1)"; 
          ccall_.setCommand(command);
          result = ccall_.run();
          setupStringBuffer.append("ccall_("+command+") returned "+result+"\n"); 

          command="ADDDIRE USRID(TESTUSER41 "+sysName+") USRD('Test') USER(TESTUSER41)"; 
          ccallPow_.setCommand(command);
          setupStringBuffer.append("ccall_("+command+") returned "+result+"\n"); 
          result = ccallPow_.run();
        }    
        catch(Exception ex) 
        {
            System.out.println(ex);
        }
     }
     catch(Exception e)
     {
        // throws new Exception("permission"); 
     }
}
/**
Cleans objects that are created by setup method.
**/
protected void cleanup()
{               
      try
      {            
          ccallPow_.setCommand("DLTUSRPRF USRPRF(TESTUSER41)");
          ccallPow_.run();
          ccallPow_.setCommand("DLTDLO DLO(TESTFLR1)");
          ccallPow_.run();
      }    
      catch(Exception ex) 
      {
           System.out.println(ex);
      }
     
      if (!isApplet_)
      {
        try
        {
          File tempFile = new File("t2.tmp");
          tempFile.delete();
        } 
        catch(NullPointerException exe)
        {
          System.out.println("The file does not exist!");
        }
      }
}

  void dumpSetupStringBuffer() {
    if (!setupStringBufferPrinted) {
      System.out.println("Setup information\n");
      System.out.println("-------------------------------------");
      System.out.println(setupStringBuffer.toString());
      System.out.println("-------------------------------------");
      setupStringBufferPrinted = true;
    }
  }

/**
Method tested: DLOPermission()
 - Ensure that the constructor with one argument for root directory structure 
runs well.
**/
public void Var001()
  {
    if (PermissionTestDriver.PwrSys == null)
    {
        failed("-misc testcase parm with PwrSys not specified.");
        return;
    }

    try {
        IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
        Permission pers = new Permission(file);
                DLOPermission  f = new DLOPermission("TESTUSER41");
        succeeded();
        
    }
    catch (Exception e) {
            failed (e, "Unexpected Exception");
            dumpSetupStringBuffer(); 
    }
  }  


/**
Method tested: DLOPermission(IFSFile file)
 - Ensure that the NullPointerException is thrown if the file is null
in constructor.
**/
   public void Var002()
  {
    try {
        DLOPermission f = new DLOPermission(null);
        failed("Exception didn't occur.");
        dumpSetupStringBuffer(); 
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
Method tested: DLOPermission(IFSFile file)
 - Ensure that the As400Exception is thrown if the user is invalid
in constructor.
**/
   public void Var003()
  {
    try {     
        IFSFile file = new IFSFile(systemObject_,"/QDLS/TESTFLR1");
        Permission permission_ = new Permission(file);
            DLOPermission f = new DLOPermission("invalid");
        permission_.addUserPermission(f);
        f.setDataAuthority("*USE");
        permission_.commit();
        failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
    if (exceptionIs(e, "AS400Exception"))
       succeeded();
    else
       failed(e, "Unexpected exception occurred.");
       dumpSetupStringBuffer(); 
    }
  }


/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *ALL and it is committed.
**/
 public void Var004()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-misc testcase parm with PwrSys not specified.");
           return;
       }

       try
       {            
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permissionDLO_.setDataAuthority("*ALL");   
           
           permission_.addUserPermission(permissionDLO_);                              
           permission_.commit();        
           assertCondition(permissionDLO_.getDataAuthority().equals("*ALL"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();            
        }
        catch(Exception e)
        {        
                failed(e, "Unexpected exception occurred.");
                dumpSetupStringBuffer(); 
        }
} 


/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *CHANGE and it is committed.
**/
 public void Var005()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-misc testcase parm with PwrSys not specified.");
           return;
       }

       try
       {           
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*CHANGE");
           permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*CHANGE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();            
        }
        catch(Exception e)
        { 
                failed(e, "Unexpected exception occurred.");
     }
 }


/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *EXCLUDE and it is committed.
**/
 public void Var006()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
           failed("-misc testcase parm with PwrSys not specified.");
           return;
       }

       try
       {            
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*EXCLUDE");
           permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*EXCLUDE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
}


/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *USE and it is committed.
**/
 public void Var007()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }

       try
       {     
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
                   permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*USE");
           permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*USE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *USE and it is committed,
  then DataAuthority is set to *ALL and it is committed again.
**/
 public void Var008()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }

       try
       {          
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*USE");
           permission_.commit();
           permissionDLO_.setDataAuthority("*ALL");
                   permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*ALL"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
}       
  

/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *USE and it is committed,
  then DataAuthority is set to *CHANGE and it is committed again.
**/
 public void Var009()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }

       try
       {    
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*USE");
           permission_.commit();
           permissionDLO_.setDataAuthority("*CHANGE");
                   permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*CHANGE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();
            
        }
        catch(Exception e)
        {
System.out.println(" exception :"+e);            
                failed(e, "Unexpected exception occurred.");
        }
}       


/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *USE and it is committed,
  then DataAuthority is set to *EXCLUDE and it is committed again.
**/
 public void Var010()
  {
      if (PermissionTestDriver.PwrSys == null)
      {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
      }

       try
       {      
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*USE");
           permission_.commit();
           permissionDLO_.setDataAuthority("*EXCLUDE");
                   permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*EXCLUDE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();           
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
            }

  }

/**
Method tested: getDataAuthority() and setDataAuthority()
 - Ensure that the method returns correct value when DataAuthority is set to *ALL and it is committed,
  then DataAuthority is set to *USE and it is committed again.
**/
 public void Var011()
 {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }

       try
       {    
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           permissionDLO_.setDataAuthority("*ALL");
           permission_.commit();
           permissionDLO_.setDataAuthority("*USE");
                   permission_.commit();
           assertCondition(permissionDLO_.getDataAuthority().equals("*USE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();            
        }
        catch(Exception e)
        {
         
          failed(e, "Unexpected exception occurred.");
        }
  }

/**
Method tested: getDataAuthority() 
- Ensure that the method returns the defalt data. 
**/
 public void Var012()
 {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }

       try
       {    
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
           Permission permission_ = new Permission(file);
           DLOPermission permissionDLO_ = new DLOPermission("TESTUSER41");
           permission_.addUserPermission(permissionDLO_);
           assertCondition(permissionDLO_.getDataAuthority().equals("*EXCLUDE"));
           permission_.removeUserPermission(permissionDLO_);
                   permission_.commit();            
        }
        catch(Exception e)
        {
         
          failed(e, "Unexpected exception occurred.");
        }
  }

/**
Method tested: writeObject()
- Ensure that the method returns the correct data.
**/  
public void Var013()
{
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
    if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }
    try
    {
         Permission per = new Permission(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
         RootPermission f = new RootPermission("testuser41");
         FileOutputStream ostream = new FileOutputStream("t2.tmp");
         ObjectOutputStream p = new ObjectOutputStream(ostream);          
         p.writeObject(f);           
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
Method tested: writeObject() and readObject()
- Ensure that the method returns the correct data.
**/
 public void Var014()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }
    if (PermissionTestDriver.PwrSys == null)
       {
            failed("-misc testcase parm with PwrSys not specified.");
            return;
       }
    try
    {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QDLS/TESTFLR1");
         RootPermission f = new RootPermission("testuser41");
         FileOutputStream ostream = new FileOutputStream("t2.tmp");
         ObjectOutputStream p = new ObjectOutputStream(ostream);          
         p.writeObject(f);           
         p.flush();
         ostream.close();

         FileInputStream istream = new FileInputStream("t2.tmp");
         ObjectInputStream p1 = new ObjectInputStream(istream);          
         RootPermission permission = (RootPermission)p1.readObject();
          
         istream.close();
         assertCondition(permission.getUserID().equals("TESTUSER41"));
          
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: commit()                                           //@A2A
- Ensure that multiple authority attribute changes may be committed
  via a single call to commit().  There was a problem encountered
  as follows:
 The specific problem encountered was for a DLO object when going from Sensitivity/PublicAuth == None/*ALL to Sensitivity/PublicAuth == Private/*EXCLUDE. In this example, the PublicAuth must be changed before the Sensitivity.  There are other examples where the Sensitivity would need to be changed first. A loop was added to the commit() method to accomodate either order.
**/
 public void Var015()                                             //@A2A
 {
   if (isApplet_)
   {
     notApplicable("Running as applet.");
     return;
   }
   if (PermissionTestDriver.PwrSys == null)
   {
     failed("-misc testcase parm with PwrSys not specified.");
     return;
   }
   try
   {
     Permission perm = new Permission(PermissionTestDriver.PwrSys, "/QDLS/TESTFLR1");
     DLOPermission publicPerm = (DLOPermission)(perm.getUserPermission("*PUBLIC"));
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     boolean tst1 = ToExcludePrivate_OneStep(perm, publicPerm);
     boolean tst2 = ToExcludePrivate_TwoSteps(perm, publicPerm);

     boolean tst3 = FromExcludePrivate_OneStep(perm, publicPerm);
     boolean tst4 = FromExcludePrivate_TwoSteps(perm, publicPerm);
     assertCondition(tst1 == tst2 == tst3 == tst4 == true);
   }
   catch(Exception e)
   {
     failed(e, "Unexpected exception occurred");
   }
 }

 // Go from *ALL, NONE to *EXCLUDE, PRIVATE in one commit() step          //@A2A
 public static boolean ToExcludePrivate_OneStep(Permission perm, DLOPermission publicPerm)
 {
   boolean success = true;
   try
   {
     // Set default auth = "*ALL", sens lvl = NONE
     setAllNoneDefault(perm, publicPerm);
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     // Now, set the public authority to "*EXCLUDE", which SHOULD allow the sensitivity level to be set to PRIVATE
     publicPerm.setDataAuthority("*EXCLUDE");
     perm.setSensitivityLevel(SENSLVL_PRIVATE);
     perm.commit();            
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     if (DEBUG) System.out.println("SUCCESS!  (Going from *ALL,NONE  to  *EXCLUDE,PRIVATE in 1 step");
   }
   catch(Exception e )
   {
     success = false;
     if (DEBUG) System.out.println("Error in ToExcludePrivate_OneStep - Could not set auth = *EXCLUDE, sens lvl = PRIVATE in one step");
     e.printStackTrace();
   }

   if (DEBUG) System.out.println("----------------------------------------------------");
   return(success);
 }

 // Go from *ALL, NONE to *EXCLUDE, PRIVATE in two commit() steps         //@A2A
 public static boolean ToExcludePrivate_TwoSteps(Permission perm, DLOPermission publicPerm)
 {
   boolean success = true;
   try
   {
     // Set default auth = "*ALL", sens lvl = NONE
     setAllNoneDefault(perm, publicPerm);
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     // Now, set the public authority to "*EXCLUDE", which SHOULD allow the sensitivity level to be set to PRIVATE
     publicPerm.setDataAuthority("*EXCLUDE");
     perm.commit();
     perm.setSensitivityLevel(SENSLVL_PRIVATE);
     perm.commit();            
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     if (DEBUG) System.out.println("SUCCESS!  (Going from *ALL,NONE  to  *EXCLUDE,PRIVATE in 2 steps");
   }
   catch(Exception e )
   {
     success = false;
     if (DEBUG) System.out.println("Error in ToExcludePrivate_TwoSteps - Could not set auth = *EXCLUDE, sens lvl = PRIVATE in two steps");
     e.printStackTrace();
   }

   if (DEBUG) System.out.println("----------------------------------------------------");
   return(success);
 }

 // Go from *EXCLUDE, PRIVATE  to  ALL, NONE in 1 commit() step           //@A2A
 public static boolean FromExcludePrivate_OneStep(Permission perm, DLOPermission publicPerm)
 {
   boolean success = true;
   try
   {            
     // Make sure the public auth is set to *EXCLUDE with PRIVATE 
     setExcludePrivateDefault(perm, publicPerm);
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     // Now, set the public authority to "*ALL", sens lvl = NONE in 1 step
     publicPerm.setDataAuthority("*ALL");
     perm.setSensitivityLevel(SENSLVL_NONE);
     perm.commit();            
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     if (DEBUG) System.out.println("SUCCESS!  (Going from *EXCLUDE,PRIVATE  to  *ALL,NONE in 1 step");
   }
   catch(Exception e )
   {
     success = false;
     if (DEBUG) System.out.println("Error going from *EXCLUDE, PRIVATE  to  *ALL, NONE in 1 step");
     e.printStackTrace();
   }

   if (DEBUG) System.out.println("----------------------------------------------------");
   return(success);
 }


 // Go from *EXCLUDE, PRIVATE  to  ALL, NONE in 2 commit() steps          //@A2A
 public static boolean FromExcludePrivate_TwoSteps(Permission perm, DLOPermission publicPerm)
 {
   boolean success = true;
   try
   {
     // Make sure the public auth is set to *EXCLUDE with PRIVATE 
     setExcludePrivateDefault(perm, publicPerm);
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     // Now, set public authority to "*ALL", sens lvl = NONE in 2 steps
     perm.setSensitivityLevel(SENSLVL_NONE);
     perm.commit();
     publicPerm.setDataAuthority("*ALL");
     perm.commit();
     if (DEBUG) System.out.println("DataAuth/Sensitivity=" +publicPerm.getDataAuthority()+"/"+perm.getSensitivityLevel());

     if (DEBUG) System.out.println("SUCCESS!  (Going from *EXCLUDE,PRIVATE  to  *ALL,NONE in 2 steps");
   }
   catch(Exception e )
   {
     success = false;
     if (DEBUG) System.out.println("Error going from *EXCLUDE, PRIVATE  to  *ALL, NONE in 2 steps");
     e.printStackTrace();
   }

   if (DEBUG) System.out.println("----------------------------------------------------");
   return(success);
 }

 //========================================================================================

 // Set the default Public authority to *EXCLUDE with sensitivity level of PRIVATE //@A2A
 public static void setExcludePrivateDefault(Permission perm, DLOPermission publicPerm)
 {
   try
   {
     // Set to Exclude and PRIVATE in two steps to make sure it gets changed
     publicPerm.setDataAuthority("*EXCLUDE");
     perm.commit();

     perm.setSensitivityLevel(SENSLVL_PRIVATE);
     perm.commit();            
   }
   catch(Exception e )
   {
     if (DEBUG) System.out.println("setExcludePrivateDefault() - Error setting authority to *EXCLUDE and sensitivity level to PRIVATE");
     e.printStackTrace();
   }
 }

 // Set the default Public authority to *ALL with sensitivity level of NONE  //@A2A
 public static void setAllNoneDefault(Permission perm, DLOPermission publicPerm)
 {
   try
   {
     perm.setSensitivityLevel(SENSLVL_NONE);
     perm.commit();            

     // Set to *ALL auth with NONE sensitivity level
     publicPerm.setDataAuthority("*ALL");
     perm.commit();
   }
   catch(Exception e )
   {
     if (DEBUG) System.out.println("setAllNoneDefault() - Error setting authority to *ALL and sensitivity level to NONE");
     e.printStackTrace();
   }
 }


}       



