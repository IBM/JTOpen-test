///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SocketProxy.java
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


public class SocketProxy implements Runnable {
  public static void usage() {
    System.out
        .println("Usage:  java test.SocketProxy proxyPort serverName serverPort\n");
  }

  public static void main(String args[]) {
    try {
      if (args.length < 3) {
        usage();
        System.exit(1);
      }
      int proxyPort = Integer.parseInt(args[0]);
      String serverName = args[1];
      int serverPort = Integer.parseInt(args[2]);

      SocketProxy proxy = new SocketProxy(System.out, System.in, proxyPort,
          serverName, serverPort);
      proxy.run();

    } catch (Exception e) {
      System.out.println("Error occurred");
      e.printStackTrace(System.out);
      System.exit(1);
    }
  }

  public static SocketProxy getSocketProxy(int startPort, String serverName, int serverPort) {
  	SocketProxy returnProxy = null ; 
  	while (returnProxy == null) { 
  		try {
	      returnProxy = new SocketProxy (startPort, serverName, serverPort);
	      
      } catch (IOException e) {
      	startPort++; 
      } 
  		
  	}
  	return returnProxy; 
  	
  }
    
  
  
  
  int proxyPort_;
  String serverName_;
  int serverPort_;
  StringWriter stringWriter_ = null; 
  PrintWriter printWriter_;
  InputStream inputStream_;
  SocketProxyMasterThread masterThread; 
  boolean done;
  ServerSocket serverSocket_; 
  
  public SocketProxy(PrintStream out, InputStream in, int proxyPort,
      String serverName, int serverPort) throws IOException {
    proxyPort_ = proxyPort;
    serverName_ = serverName;
    serverPort_ = serverPort;
    printWriter_ = new PrintWriter(out);
    inputStream_ = in;
    serverSocket_ = new ServerSocket(proxyPort_);

    // 
    // Open the server socket
    // 

    masterThread = 
        new SocketProxyMasterThread(serverSocket_, serverName_, serverPort_, printWriter_, this); 

  }

  public SocketProxy(int proxyPort, String serverName, int serverPort) throws IOException {
    proxyPort_ = proxyPort;
    serverName_ = serverName;
    serverPort_ = serverPort;
    stringWriter_ = new StringWriter(); 
    printWriter_ = new PrintWriter(stringWriter_); 
    inputStream_ = null;
    serverSocket_ = new ServerSocket(proxyPort_);
    // 
    // Open the server socket
    // 

    masterThread = 
        new SocketProxyMasterThread(serverSocket_, serverName_, serverPort_, printWriter_, this); 

  }
  
  public void run() {
  	String oldThreadName = Thread.currentThread().getName();
    try {
    	Thread.currentThread().setName("SocketProxy-port="+proxyPort_); 

      
      // Start a thread to handle the server
      // We will keep this thread active so we can control what is going on. 
      
      masterThread.start() ;
      printWriter_.println("Started SocketProxyMasterThread"); 
      done =  false; 
      while (!done) { 
          Thread.sleep(100); 
      }
      masterThread.join(); 
      printWriter_.println("Joined with SocketProxyMasterThread"); 
      
    } catch (Throwable t) {
      printWriter_.println("Throwable caught ");
      try { 
        if (masterThread != null) { 
          masterThread.endService(); 
          masterThread = null; 
        }
      } catch (Throwable t2) { 
        printWriter_.println("Throwable caught ending masterThread ");
        
      }

    }
  	Thread.currentThread().setName(oldThreadName); 

  }

  public void endService() { 
    if (masterThread != null) { 
      masterThread.endService();
      masterThread = null; 
    } 
    done = true; 
  }
  
  public String getOutput() {
  	if (stringWriter_ == null) { 
  		return "NOT AVIALABLE"; 
  	} else {
  		return stringWriter_.toString(); 
  	}
  	
  }
  
  int getPortNumber () {
  	return proxyPort_; 
  }
  
  long getActiveByteCount() { 
  	return masterThread.getActiveByteCount(); 
  }

  int getFailedConnectCount() {
      return masterThread.getFailedConnectCount(); 
  }

  void endActiveConnections() { 
  	masterThread.endActiveConnections(); 
  }

  public void enable(boolean b) {
	  masterThread.enable(b); 
	  
  }
  
  public void setOutputFailedAttempts(PrintStream stream) {
	  masterThread.setOutputFailedAttempts(stream); 
  }

  public void resetCounts() {
    masterThread.resetCounts(); 
  }

  public void endAfterByteCount(long toServerByteCount, long toClientByteCount) {
    masterThread.endAfterByteCount(toServerByteCount, toClientByteCount); 
    
  }

  public long getToServerByteCount() {
    
    return masterThread.getToServerByteCount(); 
    
  }

  public long getToClientByteCount() {
    
    return masterThread.getToClientByteCount(); 
    
  }

  
}
