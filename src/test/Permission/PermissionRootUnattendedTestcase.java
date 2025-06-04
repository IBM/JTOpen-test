///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PermissionRootUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Permission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.Permission;
import com.ibm.as400.access.RootPermission;

import test.PermissionTestDriver;
import test.Testcase;


/**
The PermissionRootUnattendedTestcase provides testcases to test the methods of RootPermission.

<p>This tests the following RootPermission methods:
<ul>
<li>RootPermission() - from Var001 to Var003
<li>getDataAuthority() - from Var004 to Var012
<li>isAlter() - from Var013 to Var014
<li>isExistence() - from Var015 to Var016
<li>isManagement() - from Var017 to Var018
<li>isReference() - from Var019 to Var020
<li>setAlter() - Var021
<li>setDataAuthority() - from Var004 to Var011
<li>setExistence() - Var022
<li>setManagement() - Var023
<li>setReference() - Var024
<li>writeObject() and readObject() Var025 to Var026
</ul>
**/

public class PermissionRootUnattendedTestcase
extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PermissionRootUnattendedTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PermissionTestDriver.main(newArgs); 
   }


        //private data.
        private CommandCall ccall_;
        private CommandCall ccallPow_;
        private AS400 PwrSys_;


/**
Constructor.
**/
        public PermissionRootUnattendedTestcase(AS400 systemObject, 
                                            Hashtable<String,Vector<String>> namesAndVars, 
                                            int runMode, 
                                            FileOutputStream fileOutputStream)
        {
          super(systemObject, "PermissionRootUnattendedTestcase", namesAndVars, runMode, fileOutputStream);
          PwrSys_ = PermissionTestDriver.PwrSys;
        }

/**
Creates objects TESTUSER43 and testdir2. 
**/
protected void setup () //throws Exception
{
     try
     {         
        ccall_ = new CommandCall(systemObject_);
        ccallPow_ = new CommandCall(PwrSys_);
        try
        {
            
            ccallPow_.setCommand("QSYS/CRTUSRPRF USRPRF(TESTUSER43) PASSWORD(JTEAM1)");
            ccallPow_.run();
            ccall_.setCommand("QSYS/CRTDIR DIR(TESTDIR2)");
            ccall_.run();
            ccall_.setCommand("QSYS/CRTDIR DIR('\"TEST\"\"DIR2\"')");
            ccall_.run();
            ccall_.setCommand("QSYS/CRTDIR DIR('\"TEST''DIR2\"')");
            ccall_.run();
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
          ccallPow_.setCommand("QSYS/DLTUSRPRF USRPRF(TESTUSER43)");
          ccallPow_.run();
          ccallPow_.setCommand("QSYS/RMVDIR DIR(TESTDIR2)");
          ccallPow_.run();
          ccallPow_.setCommand("QSYS/RMVLNK OBJLNK('/testdir2.symlink')");
          ccallPow_.run();
          ccallPow_.setCommand("QSYS/RMVDIR DIR('\"TEST\"\"DIR2\"')");
          ccallPow_.run();
          ccallPow_.setCommand("QSYS/RMVDIR DIR('\"TEST''DIR2\"')");
          ccallPow_.run();
      }    
      catch(Exception ex) 
      {
           System.out.println(ex);
      }
     
      {
        try
        {
          File tempFile = new File("t1.tmp");
          tempFile.delete();
        } 
        catch(NullPointerException exe)
        {
          System.out.println("The file does not exist!");
        }
      }
}


/**
Method tested: RootPermission()
 - Ensure that the constructor with one argument for root directory structure 
runs well.
**/
  public void Var001()
  {
    try 
    {
        IFSFile file = new IFSFile(PwrSys_,"/testdir2");
        Permission pers = new Permission(file);
        RootPermission  f = new RootPermission("TESTUSER43");
        assertCondition(true, "permission is" +pers+" "+f); 

    }
    catch (Exception e) {
            failed (e, "Unexpected Exception");
    }
  }  


/**
Method tested: RootPermission(IFSFile file)
 - Ensure that the NullPointerException is thrown if the file is null
in constructor.
**/
   public void Var002()
  {
    try {
        RootPermission f = new RootPermission(null);
        failed("Exception didn't occur."+f);
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
Method tested: RootPermission(IFSFile file)
 - Ensure that the As400Exception is thrown if the user is invalid
in constructor.
**/
   public void Var003()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
    try {
        IFSFile file = new IFSFile(PwrSys_,"/testdir2");
        Permission pers = new Permission(file);
            RootPermission f = new RootPermission("invalid");
        pers.addUserPermission(f);
        f.setAlter(true);
        pers.commit();
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
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the return value is *RWX(committed).
**/
        public void Var004()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*RWX");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*RWX"));
                pers.removeUserPermission(f);
                pers.commit();
                
        }
        catch(Exception e)
        {
        
                        
                failed(e, "Unexpected exception occurred.");
        }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *RW(committed).
**/
        public void Var005()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*RW");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*RW"));
                pers.removeUserPermission(f);
                pers.commit();
                
        }
        catch(Exception e)
        {
        
            
        
                failed(e, "Unexpected exception occurred.");
                        }
                }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *RX(committed).
**/
        public void Var006()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*RX");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*RX"));
                pers.removeUserPermission(f);
                pers.commit();
                 
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *WX(committed).
**/
        public void Var007()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*WX");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*WX"));
                pers.removeUserPermission(f);
                pers.commit();
                 
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *R(committed).
**/
        public void Var008()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*R");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*R"));
                pers.removeUserPermission(f);
                pers.commit();
                 
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *W(committed).
**/
        public void Var009()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*W");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*W"));
                pers.removeUserPermission(f);
                pers.commit();
                 
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *X(committed).
**/
        public void Var010()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*X");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*X"));
                pers.removeUserPermission(f);
                pers.commit();
                 
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority() and setDataAuthority()
- Ensure that the returned value is *NONE(committed).
**/
        public void Var011()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                        f.setDataAuthority("*NONE");
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority() != null);
                pers.removeUserPermission(f);
                pers.commit();
                 
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: getDataAuthority()
- Ensure that the returned value is *EXCLUDE(committed).
**/
        public void Var012()
        {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
                try{
            Permission pers = new Permission(PwrSys_,"/testdir2");
                        RootPermission f = new RootPermission("TESTUSER43");
                pers.addUserPermission(f);
                pers.commit();
                RootPermission f1;
                Permission pers1 = new Permission(PwrSys_,"/testdir2");
                f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
                assertCondition(f1.getDataAuthority().equals("*EXCLUDE"));
                pers.removeUserPermission(f);
                pers.commit();
                
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
                }
        }


/**
Method tested: isALter()
 - Ensure that the method returns true(committed).
**/
 public void Var013()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setAlter(true);
           pers.commit();
           assertCondition(root.isAlter() == true);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {       
                failed(e, "Unexpected exception occurred.");
        }
  }


/**
Method tested: isALter()
 - Ensure that the method returns false(committed).
**/
 public void Var014()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setAlter(false);
           pers.commit();
           assertCondition(root.isAlter() == false);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isExistence()
 - Ensure that the method returns true(committed).
**/
 public void Var015()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setExistence(true);
           pers.commit();
           assertCondition(root.isExistence() == true);
           pers.removeUserPermission(root);
           pers.commit();
                
       }
       catch(Exception e)
       {
           failed(e, "Unexpected exception occurred.");
       }
  }


/**
Method tested: isExistence()
  - Ensure that the method returns false(committed).
**/
 public void Var016()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setExistence(false);
           pers.commit();
           assertCondition(root.isExistence() == false);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {     
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isManagement()
 - Ensure that the method returns true(committed).
**/
 public void Var017()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setManagement(true);
           pers.commit();
           assertCondition(root.isManagement() == true);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {
  
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isManagement()
  - Ensure that the method returns false(committed).
**/
 public void Var018()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setManagement(false);
           pers.commit();
           assertCondition(root.isManagement() == false);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isReference()
 - Ensure that the method returns true(committed).
**/
 public void Var019()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setReference(true);
           pers.commit();
           assertCondition(root.isReference() == true);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {       
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isReference()
 - Ensure that the method returns false(committed).
**/
 public void Var020()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PwrSys_,"/testdir2");
           Permission pers = new Permission(file);
           RootPermission root = new RootPermission("TESTUSER43");
           pers.addUserPermission(root);
           root.setReference(false);
           pers.commit();
           assertCondition(root.isReference() == false);
           pers.removeUserPermission(root);
           pers.commit();
                
        }
        catch(Exception e)
        {
           failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: setAlter()
- Ensure that the method set the alter object authority is successful.
**/
 public void Var021()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
          Permission pers = new Permission(PwrSys_,"/testdir2");
          RootPermission f = new RootPermission("TESTUSER43");
          pers.addUserPermission(f);
          f.setAlter(true);
          pers.commit();
          RootPermission f1;
          Permission pers1 = new Permission(PwrSys_,"/testdir2");
          f1 = (RootPermission) pers1.getUserPermission("TESTUSER43");
          assertCondition(f1.isAlter() == true);
          pers.removeUserPermission(f);
          pers.commit();                
        }
        catch(Exception e)
        {
                    failed(e, "Unexpected exception occurred.");
        } 
  }


/**
Method tested: setExistence()
- Ensure that the method set the existence object authority is successful.
**/
 public void Var022()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
       Permission pers = new Permission(PwrSys_,"/testdir2");
       RootPermission f = new RootPermission("TESTUSER43");
       pers.addUserPermission(f);
       f.setExistence(true);
       pers.commit();
       RootPermission f1;
       Permission pers1 = new Permission(PwrSys_,"/testdir2");
       f1 =  (RootPermission) pers1.getUserPermission("TESTUSER43");
       assertCondition(f1.isExistence() == true);
       pers.removeUserPermission(f);
       pers.commit();
                
        }
        catch(Exception e)
        {
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: setManagement()
- Ensure that the method set the management object authority is successful.
**/
 public void Var023()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
       Permission pers = new Permission(PwrSys_,"/testdir2");
       RootPermission f = new RootPermission("TESTUSER43");
       pers.addUserPermission(f);
       f.setManagement(true);
       pers.commit();
       RootPermission f1;
       Permission pers1 = new Permission(PwrSys_,"/testdir2");
       f1 =  (RootPermission) pers1.getUserPermission("TESTUSER43");
       assertCondition(f1.isManagement() == true);
       pers.removeUserPermission(f);
       pers.commit();
                
        }
        catch(Exception e)
        {                               
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: setReference()
- Ensure that the method set the reference object authority is successful.
**/
 public void Var024()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
       Permission pers = new Permission(PwrSys_,"/testdir2");
       RootPermission f = new RootPermission("TESTUSER43");
       pers.addUserPermission(f);
       f.setReference(true);
       pers.commit();
       RootPermission f1;
       Permission pers1 = new Permission(PwrSys_,"/testdir2");
       f1 =  (RootPermission) pers1.getUserPermission("TESTUSER43");
       assertCondition(f1.isReference() == true);
       pers.removeUserPermission(f);
       pers.commit();
                
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
  public void Var025()
  {
    try
    {
         Permission per = new Permission(systemObject_,"/testdir2");
         RootPermission f = new RootPermission("testuser43");
         FileOutputStream ostream = new FileOutputStream("t1.tmp");
         ObjectOutputStream p = new ObjectOutputStream(ostream);          
         p.writeObject(f);           
         p.flush();
         ostream.close();
         assertCondition(true, "permission is" +per+" "+f); 
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
 public void Var026()
  {
    try
    {
         Permission pers = new Permission(systemObject_,"/testdir2");
         RootPermission f = new RootPermission("testuser43");
         FileOutputStream ostream = new FileOutputStream("t1.tmp");
         ObjectOutputStream p = new ObjectOutputStream(ostream);          
         p.writeObject(f);           
         p.flush();
         ostream.close();

         FileInputStream istream = new FileInputStream("t1.tmp");
         ObjectInputStream p1 = new ObjectInputStream(istream);          
         RootPermission permission = (RootPermission)p1.readObject();
          
         istream.close();
          assertCondition(permission.getUserID().equals("TESTUSER43"), "User not correct pers="+pers);
          
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }

/**
Method tested: setReference(), where object name contains a single quote.
- Ensure that the method set the reference object authority is successful.
**/
 public void Var027()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
       Permission pers = new Permission(PwrSys_,"/test'dir2");
       RootPermission f = new RootPermission("TESTUSER43");
       pers.addUserPermission(f);
       f.setReference(true);
       pers.commit();
       RootPermission f1;
       Permission pers1 = new Permission(PwrSys_,"/test'dir2");
       f1 =  (RootPermission) pers1.getUserPermission("TESTUSER43");
       assertCondition(f1.isReference() == true);
       pers.removeUserPermission(f);
       pers.commit();
                
        }
        catch(Exception e)
        {                   
        
                failed(e, "Unexpected exception occurred.");
     }
  }

/**
Method tested: setReference(), where object name contains a double quote.
- Ensure that the method set the reference object authority is successful.
**/
 public void Var028()
  {
       if (PwrSys_ == null)
       {
            failed("-pwrSys testcase parm not specified.");
            return;
       }
       try
       {
       Permission pers = new Permission(PwrSys_,"/test\"dir2");
       RootPermission f = new RootPermission("TESTUSER43");
       pers.addUserPermission(f);
       f.setReference(true);
       pers.commit();
       RootPermission f1;
       Permission pers1 = new Permission(PwrSys_,"/test\"dir2");
       f1 =  (RootPermission) pers1.getUserPermission("TESTUSER43");
       assertCondition(f1.isReference() == true);
       pers.removeUserPermission(f);
       pers.commit();
                
        }
        catch(Exception e)
        {                   
        
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getDataAuthority() and setDataAuthority() on a symbolic link.
- Ensure that the returned value is *RW(committed).
**/
 public void Var029()
 {
   if (PwrSys_ == null)
   {
     failed("-pwrSys testcase parm with PwrSys not specified.");
     return;
   }
   boolean succeeded = true;
   String fileName = "/testdir2";
   String symlinkName = fileName + ".symlink";
   Permission pers1 = null;
   Permission pers2 = null;
   RootPermission f1 = null;
   try
   {
     // Create a symlink to the file.
     String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
     CommandCall cmd = new CommandCall(systemObject_);
     if (!cmd.run(cmdStr)) {
       AS400Message[] msgs = cmd.getMessageList();
       for (int i=0; i<msgs.length; i++) {
         if (!msgs[i].getID().equals("CPFA0A0")) { // "Object already exists."
           System.out.println("Failed to create symbolic link.");
           System.out.println(msgs[i]);
         }
       }
     }

     {
       pers1 = new Permission(PwrSys_,symlinkName,true,true); // follow links
       f1 = new RootPermission("TESTUSER43");
       pers1.addUserPermission(f1);
       f1.setDataAuthority("*RW");
       pers1.commit();
       Permission pers1a = new Permission(PwrSys_,symlinkName,true,true);  // follow links
       RootPermission f1a = (RootPermission) pers1a.getUserPermission("TESTUSER43");
       if (!f1a.getDataAuthority().equals("*RW")) {
         succeeded = false;
         System.out.println("Failed to set user permission of resolved object.");
       }
     }

     {
       pers2 = new Permission(PwrSys_,symlinkName,true,false); // don't follow links (only works for V5R4 and higher) ///
       RootPermission f2 = new RootPermission("TESTUSER43");
       //pers2.addUserPermission(f2);  // permission has already been added (above)
       f2.setDataAuthority("*NONE");
       pers2.commit();
       Permission pers2a = new Permission(PwrSys_,symlinkName,true,false);  // don't follow links
       RootPermission f2a = (RootPermission) pers2a.getUserPermission("TESTUSER43");
       if (f2a != null) {
         if (PwrSys_.getVRM() < 0x00050400) {
           System.out.println("Tolerating failure to set symlink-only permission, since system is pre-V5R4.");
         }
         else {
           succeeded = false;
           System.out.println("Failed to set user permission of symlink.");
         }
       }
       Permission pers2b = new Permission(PwrSys_,symlinkName,true,true);  // follow links
       RootPermission f2b = (RootPermission) pers2b.getUserPermission("TESTUSER43");
       if (!f2b.getDataAuthority().equals("*RW")) {
         succeeded = false;
         System.out.println("User permission of resolved object wasn't preserved.");
       }
     }

     pers1.removeUserPermission(f1);
     pers1.commit();

     assertCondition(succeeded);
   }
   catch(Exception e)
   {
     failed(e, "Unexpected exception occurred.");
   }
 }

}


