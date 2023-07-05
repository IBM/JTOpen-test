///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PermissionQSYSUnattendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.Permission;
import com.ibm.as400.access.UserPermission;
import com.ibm.as400.access.QSYSPermission;
import com.ibm.as400.access.CommandCall;
import javax.swing.table.TableColumnModel;
import java.awt.Frame;
import java.awt.Point;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.io.File;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
The PermissionQSYSUnattendedTestcase class provides testcases to test the methods of QSYSPermission.

<p>The following QSYSPermission methods are tested:
<ul>
<li>QSYSPermission - from Var001 to Var003
<li>getDataAuthority()
<li>getGroupIndicator()
<li>getObjectAuthority() - from Var004 to Var011
<li>isAdd() - from Var012 to Var013
<li>isAlter() - from Var014 to Var015
<li>isDelete() - from Var016 to Var017
<li>isExecute() - from Var018 to Var019
<li>isExistence() - from Var020 to Var021
<li>isManagement() - from Var022 to Var023
<li>isOperational() - from Var024 to Var025
<li>isRead() - from Var026 to var027
<li>isReference() - from Var028 to Var029
<li>isUpdate() - from Var030 to Var031
<li>setAdd() - Var032
<li>setAlter() - Var033
<li>setDelete() - Var034
<li>setExecute() - Var035
<li>setExistence() - Var036
<li>setManagement() - Var037
<li>setObjectAuthority() - from Var004 to Var007
<li>setOperational() - Var038
<li>setRead() - Var039
<li>setReference() - Var040
<li>setUpdate() - Var041
<li>getGroupIndicator() -Var042
<li>getObjectAuthority() - Var043
<li>readObject() and writeObject() - Var044 to Var045

</ul>
**/

public class PermissionQSYSUnattendedTestcase
extends Testcase
{


        //private data.
        CommandCall ccall_;
        CommandCall ccallPow_;


/**
Constructor.
**/
    public PermissionQSYSUnattendedTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "PermissionQSYSUnattendedTestcase", namesAndVars, runMode, fileOutputStream);
    }

/**
Creates objects testuser42 and testlib2. 
**/
protected void setup () //throws Exception
{

     try
     {         
        ccall_ = new CommandCall(systemObject_);
        ccallPow_ = new CommandCall(PermissionTestDriver.PwrSys);
        try
        {
            
            ccallPow_.setCommand("CRTUSRPRF USRPRF(TESTUSER42) PASSWORD(JTEAM1)");
            ccallPow_.run();
            ccall_.setCommand("CRTLIB LIB(testlib2)");
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
          ccallPow_.setCommand("DLTUSRPRF USRPRF(TESTUSER42)");
          ccallPow_.run();
          deleteLibrary(ccallPow_,"testLIB2");
      }    
      catch(Exception ex) 
      {
           System.out.println(ex);
      }

      if (!isApplet_)
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
Method tested: QSYSPermission()
 - Ensure that the constructor with one argument for root directory structure 
runs well.
**/
  public void Var001()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
    try {
        IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        Permission pers = new Permission(file);
                QSYSPermission  f = new QSYSPermission("testuser42");
        succeeded();
    }
    catch (Exception e) {
            failed (e, "Unexpected Exception");
    }
  }  


/**
Method tested: QSYSPermission(IFSFile file)
 - Ensure that the NullPointerException is thrown if the file is null
in constructor.
**/
   public void Var002()
  {
    try {
        QSYSPermission f = new QSYSPermission(null);
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
Method tested: QSYSPermission(IFSFile file)
 - Ensure that the As400Exception is thrown if the user is invalid
in constructor.
**/
   public void Var003()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
    try {
        IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        Permission pers = new Permission(file);
            QSYSPermission f = new QSYSPermission("invalid");
        pers.addUserPermission(f);
        f.setAdd(true);
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
Method tested: getObjectAuthority() and setObjectAuthority()
 - Ensure that the method returns *All(committed). 
**/
 public void Var004()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*ALL");
           pers.commit();
           assertCondition(f.getObjectAuthority().equals("*ALL"));
           pers.removeUserPermission(f);
           pers.commit();
           
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getObjectAuthority() and setObjectAuthority()
 - Ensure that the method returns *CHANGE(committed).
**/
 public void Var005()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*CHANGE");
           pers.commit();
           assertCondition(f.getObjectAuthority().equals("*CHANGE"));
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getObjectAuthority() and setObjectAuthority()
 - Ensure that the method returns *EXCLUDE(committed).
**/
 public void Var006()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           assertCondition(f.getObjectAuthority().equals("*EXCLUDE"));
           pers.removeUserPermission(f);
           pers.commit();
           
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getObjectAuthority() and setObjectAuthority()
 - Ensure that the method returns *USE(committed).
**/
 public void Var007()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*USE");
           pers.commit();
           assertCondition(f.getObjectAuthority().equals("*USE"));
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getObjectAuthority()
 - Ensure that the method returns correct value when the object auuthority is set to *USE and it is committed,
  then DataAuthority is set to *ALL and it is committed again.
**/
 public void Var008()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*USE");
           pers.commit();
           f.setObjectAuthority("*ALL");
                   pers.commit();
           assertCondition(f.getObjectAuthority().equals("*ALL"));
           pers.removeUserPermission(f);
           pers.commit();
           
        }
        catch(Exception e)
        {       
                failed(e, "Unexpected exception occurred.");
        }
  }

  
/**
Method tested: getObjectAuthority()
 - Ensure that the method returns correct value when the object auuthority is set to *USE and it is committed,
  then DataAuthority is set to *CHANGE and it is committed again.
**/
 public void Var009()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*USE");
           pers.commit();
           f.setObjectAuthority("*CHANGE");
                   pers.commit();
           assertCondition(f.getObjectAuthority().equals("*CHANGE"));
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getObjectAuthority()
 - Ensure that the method returns correct value when the object auuthority is set to *USE and it is committed,
  then DataAuthority is set to *EXCLUDE and it is committed again.
**/
 public void Var010()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*USE");
           pers.commit();
           f.setObjectAuthority("*EXCLUDE");
                   pers.commit();
           assertCondition(f.getObjectAuthority().equals("*EXCLUDE"));
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: getObjectAuthority()
 - Ensure that the method returns correct value when the object auuthority is set to *ALL and it is committed,
  then DataAuthority is set to *USE and it is committed again.
**/
 public void Var011()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*ALL");
           pers.commit();
           f.setObjectAuthority("*USE");
                   pers.commit();
           assertCondition(f.getObjectAuthority().equals("*USE"));
           pers.removeUserPermission(f);
           pers.commit();
           
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isAdd()
 - Ensure that the method returns true(committed).
**/
 public void Var012()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setAdd(true);
           pers.commit();
           assertCondition(f.isAdd() == true);
           pers.removeUserPermission(f);
           pers.commit();
           
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isAdd()
 - Ensure that the method returns false(committed).
**/
 public void Var013()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setAdd(false);
           pers.commit();
           assertCondition(f.isAdd() == false);
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isAlter()
   - Ensure that the method returns true(committed).

**/
 public void Var014()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setAlter(true);
           pers.commit();
           assertCondition(f.isAlter() == true);
           pers.removeUserPermission(f);
           pers.commit();
        }   
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
        }
  }


/**
Method tested: isAlter()
 - Ensure that the method returns false( committed).
**/
 public void Var015()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setAlter(false);
           pers.commit();
           assertCondition(f.isAlter() == false);
           pers.removeUserPermission(f);
           pers.commit();
          
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isDelete()
 - Ensure that the method returns true(committed).
**/
 public void Var016()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setDelete(true);
           pers.commit();
           assertCondition(f.isDelete() == true);
           pers.removeUserPermission(f);
           pers.commit();
           
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isDelete()
  - Ensure that the method returns false(committed).
 **/
 public void Var017()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setDelete(false);
           pers.commit();
           assertCondition(f.isDelete() == false);
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isExecute()
 - Ensure that the method returns true ( committed ).
**/
 public void Var018()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setExecute(true);
           pers.commit();
           assertCondition(f.isExecute() == true);
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isExecute()
 - Ensure that the method returns false(committed).
**/
 public void Var019()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setExecute(false);
           pers.commit();
           assertCondition(f.isExecute() == false);
           pers.removeUserPermission(f);
           pers.commit();
          
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isExistence()
- Ensure that the method returns true ( committed ).
**/
 public void Var020()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setExistence(true);
           pers.commit();
           assertCondition(f.isExistence() == true);
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isExistence()
- Ensure that the method returns false ( committed ).
**/
 public void Var021()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setExistence(false);
           pers.commit();
           assertCondition(f.isExistence() == false);
           pers.removeUserPermission(f);
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
 public void Var022()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setManagement(true);
           pers.commit();
           assertCondition(f.isManagement() == true);
           pers.removeUserPermission(f);
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
 public void Var023()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setManagement(false);
           pers.commit();
           assertCondition(f.isManagement() == false);
           pers.removeUserPermission(f);
           pers.commit();
          
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isOperational()
- Ensure that the method returns true(committed).
**/
 public void Var024()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setOperational(true);
           pers.commit();
           assertCondition(f.isOperational() == true);
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isOperational()
- Ensure that the method returns false(committed).
**/
 public void Var025()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setOperational(false);
           pers.commit();
           assertCondition(f.isOperational() == false);
           pers.removeUserPermission(f);
           pers.commit();
         
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isRead()
- Ensure that the method returns true(committed).
**/
 public void Var026()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
          IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
          Permission pers = new Permission(file);
          QSYSPermission f = new QSYSPermission("testuser42");
          pers.addUserPermission(f);
          f.setRead(true);
          pers.commit();
          assertCondition(f.isRead() == true);
          pers.removeUserPermission(f);
          pers.commit();
    }
    catch(Exception e)
    {            
        failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isRead()
- Ensure that the method returns false(committed).
**/
 public void Var027()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setRead(false);
           pers.commit();
           assertCondition((f.isRead() == false)&&(f.isUpdate() == false)&&(f.isDelete() == false));
           pers.removeUserPermission(f);
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
 public void Var028()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setReference(true);
           pers.commit();
           assertCondition(f.isReference() == true);
           pers.removeUserPermission(f);
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
 public void Var029()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setReference(false);
           pers.commit();
           assertCondition(f.isReference() == false);
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


/**
Method tested: isUpdate()
- Ensure that the method returns true(committed).
**/
 public void Var030()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
       IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
       Permission pers = new Permission(file);
       QSYSPermission f = new QSYSPermission("testuser42");
       pers.addUserPermission(f);
       f.setUpdate(true);
       pers.commit();
       assertCondition(f.isUpdate() == true);
       pers.removeUserPermission(f);
       pers.commit();            
    }
    catch(Exception e)
    {           
       failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: isUpdate()
- Ensure that the method returns false(committed).
**/
 public void Var031()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setUpdate(false);
           pers.commit();
           assertCondition(f.isUpdate() == false);
           pers.removeUserPermission(f);
           pers.commit();
        }
        catch(Exception e)
        {        
                failed(e, "Unexpected exception occurred.");
        }
  }


/**
Method tested: setAdd()
- Ensure that the method set the add data authority is successful.
**/
 public void Var032()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
        Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        QSYSPermission f = new QSYSPermission("testuser42");
        pers.addUserPermission(f);
        f.setAdd(true);
        pers.commit();
        QSYSPermission f1;
        Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        f1 = (QSYSPermission) pers1.getUserPermission("testuser42");
        assertCondition(f1.isAdd() == true);
        pers.removeUserPermission(f);
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
 public void Var033()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");        
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setAlter(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
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
Method tested: setDelete()
- Ensure that the method set the delete data authority is successful.
**/
 public void Var034()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setDelete(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
         assertCondition(f1.isDelete() == true);
         pers.removeUserPermission(f);
         pers.commit();        
    }
    catch(Exception e)
    {
         
         failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: setExecute()
- Ensure that the method set the execute data authority is successful.
**/
 public void Var035()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setExecute(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
         assertCondition(f1.isExecute() == true);
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
 public void Var036()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setExistence(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
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
 public void Var037()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setManagement(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
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
Method tested: setOperational()
- Ensure that the method set the operational object authority is successful.
**/
 public void Var038()
 {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
        Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        QSYSPermission f = new QSYSPermission("testuser42");
        pers.addUserPermission(f);
        f.setOperational(true);
        pers.commit();
        QSYSPermission f1;
        Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
        assertCondition(f1.isOperational() == true);
        pers.removeUserPermission(f);
        pers.commit();       
    }
    catch(Exception e)
    {           
        failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: setRead()
- Ensure that the method set the read data authority is successful.
**/
 public void Var039()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
        Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        QSYSPermission f = new QSYSPermission("testuser42");
        pers.addUserPermission(f);
        f.setRead(true);
        pers.commit();
        QSYSPermission f1;
        Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
        f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
        assertCondition(f1.isRead() == true);
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
 public void Var040()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");         
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setReference(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
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
Method tested: setUpdate()
- Ensure that the method set the update data authority is successful.
**/
 public void Var041()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setUpdate(true);
         pers.commit();
         QSYSPermission f1;
         Permission pers1 = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         f1 =  (QSYSPermission) pers1.getUserPermission("testuser42");
         assertCondition(f1.isUpdate() == true);
         pers.removeUserPermission(f);
         pers.commit();             
    }
    catch(Exception e)
    {            
        failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: getObjectAuthority()
- Ensure that the method returns the default data.
**/
 public void Var042()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         f.setRead(true);
         f.setAlter(true);
         pers.commit(); 
         assertCondition(f.getObjectAuthority().equals("USER DEFINED"));
         pers.removeUserPermission(f);
         pers.commit();             
    }
    catch(Exception e)
    {            
        failed(e, "Unexpected exception occurred.");
    }
  }


/**
Method tested: getGroupIndicator()
- Ensure that the method returns the correct data.
**/
 public void Var043()
  {
     try
     {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         pers.addUserPermission(f);
         assertCondition(f.getGroupIndicator() == 0 );
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
  public void Var044()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }  
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
    try
    {
         Permission per = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         FileOutputStream ostream = new FileOutputStream("t1.tmp");
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
 public void Var045()
  {
    if (isApplet_)
    {
      notApplicable("Running as applet.");
      return;
    }  
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
    try
    {
         Permission pers = new Permission(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
         QSYSPermission f = new QSYSPermission("testuser42");
         FileOutputStream ostream = new FileOutputStream("t1.tmp");
         ObjectOutputStream p = new ObjectOutputStream(ostream);          
         p.writeObject(f);           
         p.flush();
         ostream.close();

         FileInputStream istream = new FileInputStream("t1.tmp");
         ObjectInputStream p1 = new ObjectInputStream(istream);          
         QSYSPermission permission = (QSYSPermission)p1.readObject();
          
         istream.close();
         assertCondition(permission.getUserID().equals("TESTUSER42"));
          
    }
    catch(Exception e)
    {
          failed(e, "Unexpected exception occurred");
    }
  }


 /**
  Method tested: hasObjectAuthorities().
  - Set authorities, then verify that hasObjectAuthorities returns the correct result.
  **/
 public void Var046()
 {
   if (PermissionTestDriver.PwrSys == null)
   {
     failed("-pwrSys testcase parm with PwrSys not specified.");
     return;
   }
   try
   {
     String path = "/QSYS.LIB/TESTLIB2.LIB";
     Permission pers = new Permission(PermissionTestDriver.PwrSys,path);
     QSYSPermission f = new QSYSPermission("TESTUSER42");
     pers.addUserPermission(f);
     f.setAlter(true);
     f.setExistence(true);
     pers.commit();

     boolean result;
     result = QSYSPermission.hasObjectAuthorities(PermissionTestDriver.PwrSys, "TESTUSER42", path, new String[] {"*OBJALTER "});
     if (result != true) {
       failed("Result 1 is not as expected.");
       return;
     }

     result = QSYSPermission.hasObjectAuthorities(PermissionTestDriver.PwrSys, "TESTUSER42", path, new String[] {"*OBJALTER ", "*OBJEXIST "});
     if (result != true) {
       failed("Result 2 is not as expected.");
       return;
     }

     result = QSYSPermission.hasObjectAuthorities(PermissionTestDriver.PwrSys, "TESTUSER42", path, new String[] {"*OBJALTER ", "*OBJMGT   "});
     if (result != false) {
       failed("Result 3 is not as expected.");
       return;
     }

     result = QSYSPermission.hasObjectAuthorities(PermissionTestDriver.PwrSys, "TESTUSER42", path, new String[] {"*OBJALTER ", "*OBJMGT   ", "*OBJEXIST "});
     if (result != false) {
       failed("Result 4 is not as expected.");
       return;
     }

     pers.removeUserPermission(f);
     pers.commit();
     succeeded();
   }
   catch(Exception e)
   {
     failed(e, "Unexpected exception occurred.");
   } 
 }


 /**
  Method tested: hasObjectAuthorities().
  - Set authorities, then verify that hasObjectAuthorities (where no authorities are specified) throws an exception.
  **/
 public void Var047()
 {
   if (PermissionTestDriver.PwrSys == null)
   {
     failed("-pwrSys testcase parm with PwrSys not specified.");
     return;
   }
   Permission pers = null;
   QSYSPermission f = null;
   try
   {
     String path = "/QSYS.LIB/TESTLIB2.LIB";
     pers = new Permission(PermissionTestDriver.PwrSys,path);
     f = new QSYSPermission("TESTUSER42");
     pers.addUserPermission(f);
     f.setAlter(true);
     f.setExistence(true);
     pers.commit();

     boolean result;
     result = QSYSPermission.hasObjectAuthorities(PermissionTestDriver.PwrSys, "TESTUSER42", path, new String[] {});
     failed ("Did not throw exception.");
   }
   catch (Exception e) {
     assertExceptionIsInstanceOf (e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
   }
   finally {
     if (pers != null && f != null)
     {
       try
       {
         pers.removeUserPermission(f);
         pers.commit();
       }
       catch (Exception e) { e.printStackTrace(); }
     }
   }
 }


/**
Method tested: getObjectAuthority() and setObjectAuthority(), with 'followLinks==false'
 - Ensure that the method returns *CHANGE(committed).
**/
 public void Var048()
  {
       if (PermissionTestDriver.PwrSys == null)
       {
            failed("-pwrSys testcase parm with PwrSys not specified.");
            return;
       }
       try
       {
           IFSFile file = new IFSFile(PermissionTestDriver.PwrSys,"/QSYS.LIB/testlib2.lib");
           Permission pers = new Permission(file,true,false);
           QSYSPermission f = new QSYSPermission("testuser42");
           pers.addUserPermission(f);
           f.setObjectAuthority("*CHANGE");
           pers.commit();
           assertCondition(f.getObjectAuthority().equals("*CHANGE"));
           pers.removeUserPermission(f);
           pers.commit();
            
        }
        catch(Exception e)
        {
         
                failed(e, "Unexpected exception occurred.");
     }
  }


}


