package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class JDJSTPInputThread extends Thread {
  OutputStream oStream;
  BufferedReader reader; 
  String filename;
  Charset charset = null; 
  static boolean debug = false;

   public JDJSTPInputThread(OutputStream os, String filename) throws Exception {
     oStream = os;
     reader = new BufferedReader(new FileReader(filename));
   }


   public JDJSTPInputThread(OutputStream os, String filename, String translateTo) throws Exception {
     oStream = os;
     charset = Charset.forName(translateTo); 
     reader = new BufferedReader(new FileReader(filename));

   }



   public void run() {
       try {
	   String line;

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
	       line = reader.readLine();
	   }
	   oStream.close();
	   if (debug) { 
	       System.out.println("JDJSTPInputThread done");
	   }
       } catch (IOException e) { 
	   e.printStackTrace(); 
       }

   }

}
