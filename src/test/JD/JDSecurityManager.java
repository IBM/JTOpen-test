///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSecurityManager.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD;

public class JDSecurityManager extends SecurityManager {

  public void checkAwtEventQueueAccess() {}
  public void checkCreateClassLoader(){}
  public void checkPrintJobAccess(){}
  public void checkPropertiesAccess(){}
  public void checkSetFactory(){}
  public void checkSystemClipboardAccess(){}
  public void checkExit(int c){}
  public void checkListen(int c){}
  public void checkRead(java.io.FileDescriptor c){}
  public void checkWrite(java.io.FileDescriptor c){}
  public void checkMemberAccess(java.lang.Class c ,int i ){}
  public void checkDelete(java.lang.String c){}
  public void checkExec(java.lang.String c ){}
  public void checkLink(java.lang.String c){}
  public void checkPackageAccess(java.lang.String c){}
  public void checkPackageDefinition(java.lang.String c){}
  public void checkPropertyAccess(java.lang.String c){}
  public void checkRead(java.lang.String c){}
  public void checkSecurityAccess(java.lang.String c){}
  public void checkWrite(java.lang.String c){}
  public void checkAccept(java.lang.String a,int b){}
  public void checkConnect(java.lang.String a,int b){}
  public void checkAccess(java.lang.Thread t){}
  public void checkAccess(java.lang.ThreadGroup t){}
  public void checkMulticast(java.net.InetAddress a){}
  public void checkMulticast(java.net.InetAddress a,byte t){}
  public void checkPermission(java.security.Permission a){}
  public void checkConnect(java.lang.String a,int b ,java.lang.Object c){}
  public void checkRead(java.lang.String a,java.lang.Object b){}
  public void checkPermission(java.security.Permission a,java.lang.Object b){}

}
