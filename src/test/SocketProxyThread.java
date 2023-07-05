package test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketProxyThread extends Thread {

  String threadName_; 
  Socket socket_; 
  String serverName_; 
  int serverPort_;  
  PrintWriter printWriter_;
  SocketProxyMasterThread socketProxyMasterThread_ ;
  long toServerByteCount_ =Long.MAX_VALUE; 
  long toClientByteCount_ =Long.MAX_VALUE; 
  
  SocketProxyStreamThread toServer = null;
  SocketProxyStreamThread toClient = null ; 
  Socket serverSocket_ = null; 
  long processedBytes = 0; 
  
  public SocketProxyThread(SocketProxyMasterThread socketProxyMasterThread, String threadName, Socket socket, String serverName, int serverPort,  PrintWriter printWriter) {
      socketProxyMasterThread_ =  socketProxyMasterThread; 
      threadName_ = threadName; 
      socket_ = socket; 
      serverName_ = serverName; 
      serverPort_ = serverPort; 
      printWriter_ = printWriter; 
      setName(threadName); 

  }
  
  public void run() { 
      try { 
      // Create the socket and streams to the server
      serverSocket_ = new Socket(serverName_, serverPort_); 
      InputStream serverInputStream = serverSocket_.getInputStream(); 
      OutputStream serverOutputStream = serverSocket_.getOutputStream();
      
      // Get the streams for the client
      InputStream clientInputStream = socket_.getInputStream(); 
      OutputStream clientOutputStream = socket_.getOutputStream(); 
    
   
      // Create the stream handler threads for each direction.
      toServer = new SocketProxyStreamThread(threadName_+"-toS", 
          clientOutputStream, serverInputStream, printWriter_);
      toClient = new SocketProxyStreamThread(threadName_+"-toC", 
          serverOutputStream, clientInputStream, printWriter_);
      toServer.endAfterByteCount(toServerByteCount_); 
      toClient.endAfterByteCount(toClientByteCount_); 

      toServer.start(); 
      toClient.start(); 
      
      // Wait until all threads complete
      toServer.join(); 
      toClient.join(); 
      
      processedBytes += toServer.getByteCount(); 
      processedBytes += toClient.getByteCount(); 

      // Let the server know we are done  
      socketProxyMasterThread_.removeSocketProxyThread(this); 
      
    } catch (Exception e) {
      synchronized (printWriter_) {
        printWriter_.println("Exception caught by thread " + threadName_);
        e.printStackTrace(printWriter_); 
      }
    }
      synchronized (printWriter_) {
        printWriter_.println(threadName_+" Completed processing " +processedBytes+" bytes");
     
      }

  }

  public void endService() {
    try {
      if (socket_ != null) {
        socket_.close();
      }
    } catch (Exception e) {
      synchronized (printWriter_) {
        printWriter_.println(threadName_+" Caught exception"); 
        e.printStackTrace(printWriter_); 
      }
    }
    socket_ = null;

    try {
      if (serverSocket_ != null) {
        serverSocket_.close();
      }
    } catch (Exception e) {
      synchronized (printWriter_) {
        printWriter_.println(threadName_+" Caught exception"); 
        e.printStackTrace(printWriter_); 
      }
    }
    serverSocket_ = null;
    if (toServer != null) {
      toServer.endService();
      toServer = null;
    }
    if (toClient != null) {
      toClient.endService();
      toClient = null;
    }
  }

	public long activeByteCount() {
		long activeByteCount = 0; 
		if (toServer != null) { 
			activeByteCount += toServer.getByteCount(); 
		}
		if (toClient != null) { 
      activeByteCount += toClient.getByteCount(); 
		}
		return activeByteCount; 
	}
	
	

  public void resetCounts() {
     if (toServer != null) toServer.resetByteCount(); 
     if (toClient != null) toClient.resetByteCount(); 
    
  }

  public void endAfterByteCount(long toServerByteCount, long toClientByteCount) {
    toServerByteCount_ = toServerByteCount; 
    if (toServer != null) toServer.endAfterByteCount(toServerByteCount); 
    toClientByteCount_ = toClientByteCount; 
    if (toClient != null) toClient.endAfterByteCount(toClientByteCount); 
    
    
  }

  public long getToServerByteCount() {
   if (toServer != null) {
     return toServer.getByteCount(); 
   } else {
     return 0; 
   }
  }

  public long getToClientByteCount() {
    if (toClient != null) {
      return toClient.getByteCount(); 
    } else {
      return 0; 
    }
   }

}
