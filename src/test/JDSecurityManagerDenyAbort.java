///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSecurityManagerDenyAbort.java
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

public class JDSecurityManagerDenyAbort extends SecurityManager {

  // Disallow all calls to abort 
  public void checkPermission(Permission perm) {
    if (perm instanceof SQLPermission) {
        SQLPermission sqlPermission = (SQLPermission ) perm; 
        if ( sqlPermission.getName().equals("callAbort")) {
          throw new SecurityException("JDSecurityManagerDenyAbort does not allow callAbort"); 
        }
      
    }
  }
  
}