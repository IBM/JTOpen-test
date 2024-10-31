///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRunitGoOutput.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2024-2024 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRunitGoOutput.java
//
// Classes:      JDRunitGoOuput
//
// Report the results from the JDRunit.go method. 
//
//
////////////////////////////////////////////////////////////////////////
package test;


public class JDRunitGoOutput {
  int failedCount_ = 0; 
  int successfulCount_ = 0; 
  
    public JDRunitGoOutput(int failedCount, int successfulCount ) {
        failedCount_ = failedCount;
        successfulCount_ = successfulCount;
    }
    public int getFailedCount() { return failedCount_; } 
    public int getSuccessfulCount() { return successfulCount_; }

}
