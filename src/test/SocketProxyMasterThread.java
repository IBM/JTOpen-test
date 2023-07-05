package test;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class SocketProxyMasterThread extends Thread {

  ServerSocket serverSocket_;
  String serverName_;
  int serverPort_;
  PrintWriter printWriter_;
  SocketProxy master_;
  boolean enabled_ = true;
  boolean running = true;
  Vector threadList = null;
  PrintStream outputFailedAttempts = null;
  long startTime = 0;
  int localPort_ = 0;
  int failedConnectCount_ = 0;
  private long toServerByteCount_ = Long.MAX_VALUE;
  private long toClientByteCount_ = Long.MAX_VALUE;
  
  public SocketProxyMasterThread(ServerSocket serverSocket, String serverName,
      int serverPort, PrintWriter printWriter, SocketProxy master) {

    serverSocket_ = serverSocket;
    serverName_ = serverName;
    serverPort_ = serverPort;
    printWriter_ = printWriter;
    master_ = master;
    localPort_ = serverSocket.getLocalPort();
    this.setName("SocketProxyMasterThread-port#" + localPort_ + "-to-"
        + serverName + "-" + serverPort);

  }

  public void setOutputFailedAttempts(PrintStream stream) {
    outputFailedAttempts = stream;
    startTime = System.currentTimeMillis();
  }

  public void run() {
    int count = 0;
    int exceptionCount = 0;
    threadList = new Vector();
    while (running) {
      // Pass the socket to a new thread so that it can be dealt with
      // while we can go and get ready to accept another connection.
      Socket socket;
      try {
        socket = serverSocket_.accept();
        if (enabled_) {
          SocketProxyThread socketProxyThread = new SocketProxyThread(this,
              "PROXY_THREAD_" + count, socket, serverName_, serverPort_,
              printWriter_);
          synchronized (threadList) {
            threadList.add(socketProxyThread);
          }
          socketProxyThread.endAfterByteCount(toServerByteCount_, toClientByteCount_); 
          socketProxyThread.start();
          count++;
        } else {
          failedConnectCount_++;
          if (outputFailedAttempts != null) {
            double seconds = (System.currentTimeMillis() - startTime) / 1000.0;
            outputFailedAttempts.println(" Failed attempt on port "
                + localPort_ + " after " + seconds + " seconds ");
          }
          // Make sure this takes some time
          try {
            Thread.sleep(2);
          } catch (InterruptedException e) {
            IOException e2  = new IOException(e.toString()); 
            e2.initCause(e); 
            throw e2; 
          } 
          socket.close();
        }
      } catch (IOException e) {
        exceptionCount++;
        synchronized (printWriter_) {
          printWriter_.println("Failure #" + exceptionCount
              + "in SocketProxyMasterThread");
          e.printStackTrace(printWriter_);
        }

        if (exceptionCount >= 100) {
          running = false;
        }
      }

    }

  }

  public void endService() {

    running = false;
    try {
      serverSocket_.close();
    } catch (Exception e) {

    }

    endActiveConnections();
  }

  public void endActiveConnections() {

    Object[] threads = null;

    synchronized (threadList) {
      threads = threadList.toArray();
    }
    for (int i = 0; i < threads.length; i++) {
      ((SocketProxyThread) threads[i]).endService();
    }
    for (int i = 0; i < threads.length; i++) {
      try {
        ((SocketProxyThread) threads[i]).join();
      } catch (InterruptedException e) {
      }
    }

  }

  public void removeSocketProxyThread(SocketProxyThread socketProxyThread) {
    synchronized (threadList) {
      threadList.remove(socketProxyThread);
    }
  }

  public int getFailedConnectCount() {
    return failedConnectCount_;
  }

  public long getActiveByteCount() {
    long activeByteCount = 0;
    Object[] threads = null;

    synchronized (threadList) {
      threads = threadList.toArray();
    }
    for (int i = 0; i < threads.length; i++) {
      activeByteCount += ((SocketProxyThread) threads[i]).activeByteCount();
    }
    return activeByteCount;
  }

  public long getToServerByteCount() {
    long byteCount = 0;
    Object[] threads = null;

    synchronized (threadList) {
      threads = threadList.toArray();
    }
    for (int i = 0; i < threads.length; i++) {
      byteCount += ((SocketProxyThread) threads[i]).getToServerByteCount();
    }
    return byteCount;
    
  }

  public long getToClientByteCount() {
    long byteCount = 0;
    Object[] threads = null;

    synchronized (threadList) {
      threads = threadList.toArray();
    }
    for (int i = 0; i < threads.length; i++) {
      byteCount += ((SocketProxyThread) threads[i]).getToClientByteCount();
    }
    return byteCount;
    
  }
  
  
  public void enable(boolean b) {
    enabled_ = b;

  }

  public void resetCounts() {
    failedConnectCount_ = 0; 
    Object[] threads = null;
    synchronized (threadList) {
      threads = threadList.toArray();
    }
    for (int i = 0; i < threads.length; i++) {
      ((SocketProxyThread) threads[i]).resetCounts();
    }
  }

  public void endAfterByteCount(long toServerByteCount, long toClientByteCount) {
    toServerByteCount_ = toServerByteCount; 
    toClientByteCount_ = toClientByteCount;  
    Object[] threads = null;
    synchronized (threadList) {
      threads = threadList.toArray();
    }
    for (int i = 0; i < threads.length; i++) {
      ((SocketProxyThread) threads[i]).endAfterByteCount(toServerByteCount, toClientByteCount);
    }
    
  }


}
