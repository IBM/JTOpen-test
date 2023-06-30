///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestDriverSecurityManager.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.security.Permission;

public class TestDriverSecurityManager extends SecurityManager {

	public static boolean allowExit = false;

	public static void setAllowExit(boolean setting) {
		allowExit = setting;
	}

	public void checkExit(int code) {
		if (allowExit)
			return;
		SecurityException exception = new SecurityException("Calling exit not allowed");
		// System.err.println("TestDriverSecurityManager: exit called ");
		// exception.printStackTrace(System.err);
		throw exception;
	}

	public void checkPermission(Permission perm) {
		if (perm.getName().indexOf("exit") >= 0) {
			checkExit(0);
		}
	}

	public void checkPermission(Permission perm, Object context) {
		checkPermission(perm);
	}

}
