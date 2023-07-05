package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class SocketProxyStreamThread extends Thread {

  String threadName_; 
  OutputStream outputStream_;  
  InputStream inputStream_; 
  PrintWriter printWriter_; 
  long byteCount = 0; 
  boolean running = true;
  private long maximumByteCount; 
  
  public SocketProxyStreamThread(String threadName,
      OutputStream outputStream, 
      InputStream inputStream,
      PrintWriter printWriter) {
    
    threadName_ = threadName; 
    outputStream_ = outputStream; 
    inputStream_ = inputStream; 
    printWriter_ = printWriter; 
    setName(threadName); 
  }
  public long getByteCount() { 
    return byteCount; 
  }
  public void resetByteCount() {
    byteCount = 0; 
  }
  
  public  void run() { 
    while (running) { 
        try {
          int stuff = inputStream_.read();
          if (stuff >= 0) { 
             outputStream_.write(stuff);
             byteCount++; 
          } else {
            running = false; 
          }
          if (byteCount >= maximumByteCount) {
            running = false; 
          }
          
        } catch (IOException e) {
          synchronized(printWriter_) { 
            printWriter_.println("Exception in "+threadName_);
            e.printStackTrace(printWriter_);
          }
          running = false; 
        } 
        
      
    } /* while running */ 
    
    try {
      inputStream_.close();
    } catch (IOException e) {
      // Ignore errors 
    } 
    try {
      outputStream_.close();
    } catch (IOException e) {
     // Ignore errors
    } 
    synchronized(printWriter_) { 
      printWriter_.println(threadName_+" completed processing "+byteCount+" bytes ");
   
    }
  }
  public void endService() {
    running = false; 
    
  }
  public void endAfterByteCount(long byteCount) {
    maximumByteCount= byteCount; 
    
  }
}
