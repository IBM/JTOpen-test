package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class JDJSTPInputThread extends Thread {
  OutputStream oStream;
  BufferedReader reader; 
  String filename;
  Charset charset = null;
  Process process_ = null ;
  private JDJSTPRunFlag runFlag_ = null; 
  private long sleepTime_ = 0; 
  
  static boolean debug = false;
  static {
    String debugProperty = System.getProperty("debug"); 
    if (debugProperty != null) { 
      debug = true; 
    }
  }
   public JDJSTPInputThread(OutputStream os, String filename, Process process, JDJSTPRunFlag runFlag, long sleepTime) throws Exception {
     oStream = os;
     reader = new BufferedReader(new FileReader(filename));
     process_ = process; 
     runFlag_ = runFlag; 
     sleepTime_=sleepTime;
   }


   public JDJSTPInputThread(OutputStream os, String filename, String translateTo, Process process, JDJSTPRunFlag runFlag, long sleepTime) throws Exception {
     oStream = os;
     charset = Charset.forName(translateTo); 
     reader = new BufferedReader(new FileReader(filename));
     process_ = process; 
     runFlag_ = runFlag; 
     sleepTime_=sleepTime;

   }



   public void run() {
       try {
	   String line;
	   runFlag_.setRunning();  
	   line = reader.readLine();
	   while (line != null) {
	       if (charset != null) {
		   if (debug) { 
		       System.out.println("JDJSTPInputThread translating/writing : "+line);
		   }
                   ByteBuffer bb = charset.encode(line); 
		   oStream.write(bb.array());
		   bb = charset.encode("\n");
		   oStream.write(bb.array());
	       } else {
		   if (debug) { 
		       System.out.println("JDJSTPInputThread writing : "+line);
		   }
		   oStream.write(line.getBytes());
		   oStream.write((byte)'\n');
	       }
	       oStream.flush();
	       // When running ssh, everything can get stuck if the input is sent too fast.
	       if (sleepTime_ > 0) { 
	          Thread.sleep(sleepTime_); 
	       }
	       line = reader.readLine();
	   }
	   /* Wait for the process to complete before closing the stream */ 
	   /* Keep the connection alive to keep ssh from closing prematurely */
	   boolean processAlive = process_.isAlive();
	   while (processAlive && runFlag_.isRunning()) { 
	     Thread.sleep(5000); 
             oStream.write("\n".getBytes());
             processAlive = process_.isAlive();
	   }
	   oStream.close();
	   if (debug) { 
	       System.out.println("JDJSTPInputThread done");
	   }
       } catch (Exception e) { 
	   e.printStackTrace(); 
       }

   }

}
