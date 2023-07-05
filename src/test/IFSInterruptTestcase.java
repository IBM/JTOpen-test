///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSInterruptTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

/**
Test methods not covered by other testcases.
**/
public class IFSInterruptTestcase extends IFSGenericTestcase
{

/**
Constructor.
**/
  public IFSInterruptTestcase(AS400            systemObject,
      String userid, 
      String password,
                         Hashtable        variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String           driveLetter,
                         AS400 pwrSys
                         )
    throws IOException
  {
    super(systemObject, userid, password, "IFSInterruptTestcase", variationsToRun, runMode,
          fileOutputStream, driveLetter, pwrSys);

  }



/**
Run variations.
**/
  public void run()
  {
    // boolean allVariations = (variationsToRun_.size() == 0);

    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
    }
  }
}



