///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSNamed9.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSNamed9.java
//
// Classes:      JDCSNamed9
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.awt.TextArea;
import java.io.FileOutputStream;
import java.util.Hashtable;



/**
Testcase JDCSNamed9.  This tests the use of named parameters.
**/
public class JDCSNamed9
extends JDCSNamed
{

/**
Constructor.
**/
    public JDCSNamed9 (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDCSNamed9",
            namesAndVars, runMode, fileOutputStream,
            password);
    }







    public void Var001() { notApplicable(); }
    public void Var002() { notApplicable(); }
    public void Var003() { notApplicable(); }
    public void Var004() { notApplicable(); }
    public void Var005() { notApplicable(); }
    public void Var006() { notApplicable(); }
    public void Var007() { notApplicable(); }
    public void Var008() { notApplicable(); }

    public void Var010() { notApplicable(); }
    public void Var011() { notApplicable(); }
    public void Var012() { notApplicable(); }
    public void Var013() { notApplicable(); }
    public void Var014() { notApplicable(); }
    public void Var015() { notApplicable(); }
    public void Var016() { notApplicable(); }
    public void Var017() { notApplicable(); }
    public void Var018() { notApplicable(); }


  public void Var009() { 
      runRandomTests(3,  /* parameterCount */
                     240000); /*  runMilliseconds */ 
  }


  
}

