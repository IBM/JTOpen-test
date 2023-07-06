///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSecurityManagerDenySQLPermission.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.security.Permission;
import java.sql.SQLPermission;

public class JDSecurityManagerDenySQLPermission extends SecurityManager {

  // Disallow all SQL calls 
  public void checkPermission(Permission perm) {
    if (perm instanceof SQLPermission) {
      throw new SecurityException("JDSecurityManagerDenySQLPermission does not allow "+perm.toString()); 
      
    }
  }
  
}
