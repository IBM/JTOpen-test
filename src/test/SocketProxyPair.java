///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SocketProxyPair.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test; 

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;


public class SocketProxyPair  {
  public static void usage() {
    System.out
        .println("Usage:  java test.SocketProxy proxyPort1 proxyPort2 serverName serverPort\n");
  }

  public static void main(String args[]) {
    try {
      if (args.length < 4) {
        usage();
        System.exit(1);
      }
      int proxyPort1 = Integer.parseInt(args[0]);
      int proxyPort2 = Integer.parseInt(args[1]);
      String serverName = args[2];
      int serverPort = Integer.parseInt(args[3]);

      SocketProxyPair proxyPair = new SocketProxyPair(proxyPort1, proxyPort2,
          serverName, serverPort);
      

    } catch (Exception e) {
      System.out.println("Error occurred");
      e.printStackTrace(System.out);
      System.exit(1);
    }
  }

  public static SocketProxyPair getSocketProxyPair(int startPort1, int startPort2, String serverName, int serverPort) throws IOException {

      SocketProxyPair socketProxyPair = new SocketProxyPair(startPort1, startPort2, serverName, serverPort);

      return socketProxyPair ;
  	
  }
    

  SocketProxy proxy1;
  SocketProxy proxy2;
  Thread proxyThread1;
  Thread proxyThread2;

  public SocketProxyPair(int proxyPort1, int proxyPort2, String serverName, int serverPort) throws IOException {
      proxy1 = SocketProxy.getSocketProxy(proxyPort1, serverName, serverPort); 
      proxyThread1 = new Thread(proxy1); 
      proxyThread1.start(); 

      proxy2 = SocketProxy.getSocketProxy(proxyPort2, serverName, serverPort); 
      Thread proxyThread2 = new Thread(proxy2); 
      proxyThread2.start(); 


  }
  

  public void endService() { 
      proxy1.endService();
      proxy2.endService(); 
  }
  
  public String[] getOutput() {
      String[] result = new String[2];
      result[0] = proxy1.getOutput();
      result[1] = proxy2.getOutput();
      return result; 
  }
  
  int getPortNumber1 () {
    return proxy1.getPortNumber(); 
  }

  int getPortNumber2 () {
    return proxy2.getPortNumber(); 
  }

  long getActiveByteCount() {
      return proxy1.getActiveByteCount() + proxy2.getActiveByteCount(); 
  }

  long getToServerByteCount() {
    return proxy1.getToServerByteCount() + proxy2.getToServerByteCount(); 
  }
  
  long getToClientByteCount() {
    return proxy1.getToClientByteCount() + proxy2.getToClientByteCount(); 
  }
  
  int getFailedConnectCount() {
      return proxy1.getFailedConnectCount() + proxy2.getFailedConnectCount();
  }

  void resetCounts() {
      proxy1.resetCounts();
      proxy2.resetCounts(); 
  }
  void endActiveConnections() {
      proxy1.endActiveConnections();
      proxy2.endActiveConnections();

  }

  void endAfterByteCount(long toServerByteCount, long toClientByteCount) {
    if (toServerByteCount < 0) {
      toServerByteCount = Long.MAX_VALUE; 
    }
    if (toClientByteCount < 0) {
      toClientByteCount = Long.MAX_VALUE; 
    }
    proxy1.endAfterByteCount(toServerByteCount, toClientByteCount); 
    proxy2.endAfterByteCount(toServerByteCount, toClientByteCount); 
  }

  
  
}
