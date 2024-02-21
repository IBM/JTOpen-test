///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SetupJobLog.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Setup;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import com.ibm.as400.access.AS400;

/**
  * SetupJobLog "testcase".
**/
public class SetupJobLog extends SetupLibraryTestcase
{


  /**
  Constructor.
  **/
  public SetupJobLog(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream,
                   
                   String           password,
                   AS400            PwrSys,
                   String           PwrPwd)
  {
    super(systemObject, "SetupJobLog", 1, variationsToRun, runMode, fileOutputStream, password, PwrSys.getUserId(), PwrPwd); 
    
  }

 /**
  Performs setup operations to prepare a system for Program Call testing.
  **/
  public void Var001()
  {
    try
    {
      
      restoreLibrary("joblogtest.savf", "JOBLOGTEST"); 
      
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }
}


