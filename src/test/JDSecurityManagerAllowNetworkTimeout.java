///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSecurityManagerAllowNetworkTimeout.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.security.Permission;

public class JDSecurityManagerAllowNetworkTimeout extends SecurityManager {

  // Allow all permissions
  public void checkPermission(Permission perm) {
    return; 
  }
  
}
