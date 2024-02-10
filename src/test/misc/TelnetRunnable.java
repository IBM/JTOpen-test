/**
 * Establish a telnet session to an AS/400 using the VT/100 protocol
 * The connection is held until the object is garbage collected. 
 *
 * Use the connect method to establish the connection.
 * Use the close method to close the connection. 
 */

package test.misc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TelnetRunnable implements Runnable {

    public static boolean debug = false; 

    static {
	String debugString = System.getProperty("debug");
	if (debugString != null) {
	    debug = true;
	}
    }

    StringBuffer sb; 
    Socket s;
    String system;
    String userId;
    String password;

    public void run() {
        try { 
  	connect();
        } catch (Exception e) {
          e.printStackTrace(); 
          System.out.println(sb.toString()); 
        }
    }

    public void log( String s) {
	sb.append(s);
	sb.append("\n"); 
	if (debug) {
	    System.out.println(s); 
	}
    }


    public byte[] readBytesFromStream(DataInputStream in, int minimumBytes) throws Exception {
    int available = in.available(); 
    while (available < minimumBytes) {
      Thread.sleep(500);
      available = in.available();
    }
    log("bytes available = " + available);
    byte[] buffer = new byte[available];
    in.read(buffer);
    return buffer; 
  }

    public TelnetRunnable(String system, String userId, String password) {
	sb = new StringBuffer();
	s = null;
	this.system=system;
	this.userId=userId;
	this.password=password; 
    };

    /* connect to the system */ 
    public void connect() throws Exception {
	if (s != null) {
	    throw new Exception("Already connected"); 
	}
        sb.setLength(0); 

      /* Open a telnet port (23) to the server */
      log("Opening " + system + " port 23");
      
      s = new Socket(system, 23);
      DataInputStream in = new DataInputStream(s.getInputStream());
      DataOutputStream out = new DataOutputStream(s.getOutputStream());
      byte[] buffer = readBytesFromStream(in,6);

      byte[] outBytes = new byte[5];
      outBytes[0] = (byte) 0xff;
      outBytes[1] = (byte) 0xfc;
      outBytes[2] = 0x18;
      outBytes[3] = 0x0d;
      outBytes[4] = 0x0a;
      out.write(outBytes);
      out.flush();
      log("Wrote \\r\\n");

      buffer = readBytesFromStream(in, 200);
      log("Got "+getStringFromBuffer(buffer)+"\r\n"); 
      
      String signonInformation=pad10(userId)+password+"\r\n\r\n"; 
      outBytes = signonInformation.getBytes("US-ASCII"); 
      out.write(outBytes);
      out.flush();
      log("Wrote '"+signonInformation+"'");
      
      buffer = readBytesFromStream(in, 200);
      log("Got "+getStringFromBuffer(buffer)+"\r\n"); 
	
    }

    public String getStringFromBuffer(byte[] buffer) throws Exception{
      byte[] printBuffer = new byte[buffer.length ]; 
      for (int i = 0; i < buffer.length; i++) {
         if (buffer[i] == 0x0a) {
           printBuffer[i] = 0x0a; 
         } else if (buffer[i] >= 0x20 && buffer[i] <0x7f) {
           printBuffer[i] = buffer[i]; 
         } else {
           printBuffer[i] = ' '; 
         }
      }
      return new String(printBuffer, "US-ASCII"); 
    }
    
    public String pad10(String x) {
      switch(x.length()) {
        case 0: return x + "          "; 
        case 1: return x + "         "; 
        case 2: return x + "        "; 
        case 3: return x + "       "; 
        case 4: return x + "      "; 
        case 5: return x + "     "; 
        case 6: return x + "    "; 
        case 7: return x + "   "; 
        case 8: return x + "  "; 
        case 9: return x + " "; 
        case 10: return x;
        default:
        return x; 
      }
    }
    public void close() throws Exception {
	sb.append("Closing\n"); 
	if (s == null) {
	    throw new Exception("Not connected" ); 
	}
	s.close();
	sb.append("Closed\n"); 
	s = null; 
    }

    public String toString() {
	return sb.toString(); 
    } 

    
    public static void main(String[] args) {
	try {
	    if (args.length < 3) {
		throw new Exception ("usage:  java test.TelnetRunnable <System> <userid> <password>"); 
	    }
	    TelnetRunnable telnet = new TelnetRunnable(args[0], args[1], args[2]);
	    telnet.connect();
	    System.out.println("Connected");
	    System.out.println(telnet.toString());
	    System.out.println("Close");
	    telnet.close();
	    System.out.println("Closed");
	    System.out.println(telnet.toString());

	} catch (Exception e) {
	    e.printStackTrace(); 
	}
    } 
}

