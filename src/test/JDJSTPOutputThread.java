///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDJSTPOutputThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Vector; 

public class JDJSTPOutputThread extends Thread {
  public final static int ENCODING_UNKNOWN=0; 
  public final static int ENCODING_EBCDIC=1;
  public final static int ENCODING_ASCII=2;

  InputStream iStream; 
  StringBuffer outputBuffer;
  int truncationCount = 0; 
  PrintWriter writer;
  boolean writeToStdout = false;
  boolean reading = true;
  JDJSTPOutputThread secondaryThread = null; 
  int encoding;
  boolean doFlush = false;
  long lastFlushCheck = 0; 
  boolean flushOnce = false;
  long nextFlushMillis = 0; 
  String [] hangMessages = null;
  String [] hangMessagesException = null; 
  Vector<String> hangMessagesFound = null;
  String parsedJobName = null;
  String parsedPid     = null; 
  JDJSTPRunFlag runFlag_ = null; 

   public JDJSTPOutputThread(InputStream is, StringBuffer outputBuffer, PrintWriter writer, int encoding, JDJSTPRunFlag runFlag) {
     iStream = is; 
     this.outputBuffer = outputBuffer; 
     this.writer = writer;
      this.encoding = encoding;

     if (JTOpenTestEnvironment.isWindows) this.encoding = ENCODING_ASCII; 
     runFlag_ = runFlag; 
   }

   public JDJSTPOutputThread(InputStream is, StringBuffer outputBuffer, PrintWriter writer, String[] hangMessages, String[] hangMessagesException,  Vector<String> hangMessagesFound, int encoding, JDJSTPRunFlag runFlag ) {
     iStream = is; 
     this.outputBuffer = outputBuffer; 
     this.writer = writer;
  
     this.hangMessagesFound = hangMessagesFound;
     this.hangMessagesException = hangMessagesException;
     this.hangMessages = hangMessages; 
     this.encoding = encoding;

     if (JTOpenTestEnvironment.isWindows) this.encoding = ENCODING_ASCII; 
     runFlag_ = runFlag; 

   }


   public void setWriteToStdout() {
       writeToStdout = true; 
   }

   public void addOutputString(String  currentString ) {
     if (outputBuffer != null) { 
       synchronized (outputBuffer) {
         /* Prevent the buffer from getting larger than 50 meg */ 
         if (outputBuffer.length() > 50000000) { 
           outputBuffer.delete(0, 25000000);
           truncationCount++; 
           outputBuffer.insert(0, "JDJSTPOutputThread.outputBuffer truncated "+truncationCount+" times\n"+
               "--------------------------------------------------------------------------------\n"); 
         }
         
         
	   outputBuffer.append(currentString);
       }
       if (writer != null) {
	   writer.print(currentString);
	   if (currentString.indexOf(TestDriver.RUN_COMPLETED) >= 0) {
	     flushOnce= true; 
	   }
	   if (doFlush || flushOnce ) {
	       if (flushOnce) flushOnce = false; 
	       writer.flush();
	   } else {
	       checkFlush(); 
	   }
       }
	   if (writeToStdout) { 
	       System.out.print(currentString);
	       System.out.flush();
	   }
       }
   }

   public void checkFlush() {
       long nowMillis = System.currentTimeMillis();
	 if (!doFlush) {
	     // Check every 5 minutes 
	     if (lastFlushCheck < nowMillis - 300000) {
		 lastFlushCheck = nowMillis;
		 File checkFile = new File ("/tmp/JDJSTP.doFlush");
		 if (checkFile.exists()) {
		     doFlush = true; 
		 } 
	     }
	     if (nowMillis > nextFlushMillis) {
		 flushOnce = true;
		 nextFlushMillis = nowMillis + 5000; 
	     } 
	 }
   } 
   public void run() {
      try { 
     StringBuffer currentLine = new StringBuffer(); 
     int bufferCount = 0;
     byte[] buffer = null; 
     int ebcdicCount = 0; 
     int asciiCount = 0; 
     int outByte;

     Thread.currentThread().setName("JDJSTPOutputThread"); 
     if (JDJSTPTestcase.debug) {
	 buffer = new byte[64000]; 
     } 

     int readByte = iStream.read();
     while (reading  && readByte != -1 ) {
        //
        // If the character appears to be EBCDIC convert it to ascii
        //
         outByte=readByte;
	 if (buffer != null) {
	     if (bufferCount < buffer.length) {
		 buffer[bufferCount] = (byte) readByte;
		 bufferCount++; 
	     } 
	 }

         if ( encoding == ENCODING_UNKNOWN) {
              
             if (readByte > 128) {
                 outByte = JDJSTPTestcase.EtoChar[readByte];
                 ebcdicCount++;
             } else {
                //
                // Handle more EBCDIC characters if you think the output is
                // EBCDIC
                //
                 if ( ((readByte==0x25)&&(asciiCount<=ebcdicCount)) || (readByte == 0x5C ) || (readByte == 0x40 ) || (readByte == 0x15) || (ebcdicCount > asciiCount)) {
                     outByte = JDJSTPTestcase.EtoChar[readByte];
                 } else {
                     asciiCount++;
                 }
             }
	 } else if (encoding == ENCODING_EBCDIC) {
	     outByte = JDJSTPTestcase.EtoChar[readByte];
	 } 
         currentLine.append((char) outByte); 
         if (outByte < 0x20) {
           String currentString = currentLine.toString();
           if (JDJSTPTestcase.debug)
             System.out.println("Checking current line: " + currentString);

           if (hangMessages != null) {
             for (int i = 0; i < hangMessages.length; i++) {
               if (currentString.indexOf(hangMessages[i]) >= 0) {
                 /* Don't report the hang message if part of the */
                 /* .rxp output */
                 if (currentString.indexOf(".rxp") < 0) {
                   boolean hangMessageExceptionFound = false;
                   if (hangMessagesException != null) {
                     for (int j = 0; j < hangMessagesException.length; j++) {
                       if (currentString.indexOf(hangMessagesException[j]) >= 0) {
                         hangMessageExceptionFound = true;
                       }
                     }
                   }
                   if (!hangMessageExceptionFound) {
                     synchronized (hangMessagesFound) {
                       hangMessagesFound.addElement(currentString);
                     }
                   }

                 }
               }
             }
           }
           
           if (currentString.indexOf(TestDriver.RUN_COMPLETED) > 0) {
             if (runFlag_ != null) runFlag_.setDone();
           }
           
           int jobnameIndex = currentString.indexOf("Jobname= ");
            if (jobnameIndex >= 0) {
              parsedJobName = currentString.substring(jobnameIndex + 9).trim();
            }

            jobnameIndex = currentString.indexOf("Job is ");
            if (jobnameIndex >= 0) {
              parsedJobName = currentString.substring(jobnameIndex + 7).trim();
            }

            int pidIndex = currentString.indexOf("PID is ");
            if (pidIndex == 0) {
              parsedPid = currentString.substring(pidIndex + 7).trim();
            }

            synchronized (outputBuffer) {
              outputBuffer.append(currentString);
	      if (writer != null) {
		  
                writer.print(currentString);
		if (doFlush || flushOnce ) {
		    if (flushOnce) flushOnce = false; 
		    writer.flush(); 
		} else {
		    checkFlush(); 
		}
	      }
              if (writeToStdout) {
                System.out.print(currentString);
                System.out.flush();
              }
            }

            currentLine.setLength(0);

            if (hangMessages != null) {
              for (int i = 0; i < hangMessages.length; i++) {
                if (currentString.indexOf(hangMessages[i]) >= 0) {
                  /* Don't report the hang message if part of the */
                  /* .rxp output */
                  if (currentString.indexOf(".rxp") < 0) {
                    boolean hangMessageExceptionFound = false;
                    if (hangMessagesException != null) {
                      for (int j = 0; j < hangMessagesException.length; j++) {
                        if (currentString.indexOf(hangMessagesException[j]) >= 0) {
                          hangMessageExceptionFound = true;
                        }
                      }
                    }
                    if (!hangMessageExceptionFound) {
                      synchronized (hangMessagesFound) {
                        hangMessagesFound.addElement(currentString);
                      }
                    }
                  }
                }
              }
            }
            if (currentString.indexOf(TestDriver.RUN_COMPLETED) >= 0) {
              if (runFlag_ != null) runFlag_.setDone();
            }

          }
         
         readByte = iStream.read();
     } /* while reading */
     if (writer != null) { 
	 writer.flush();
     }

     if (JDJSTPTestcase.debug) {
         System.out.println("\nencoding="+encoding+" ebcdicCount="+ebcdicCount+" asciiCount="+asciiCount); 
     } 
     // System.out.println(); 
     if (JDJSTPTestcase.debug)  {
         if ((buffer != null) && bufferCount > 0) {
             System.out.println("JDJSTP.debug: ");
             System.out.println("JDJSTP.debug: Dumping the stdout buffer (in hex)" );
             System.out.println("JDJSTP.debug: ");
             for (int i = 0; i < bufferCount; i++) {
		 int unsignedInt = 0xFF & (int) buffer[i];
		 String hexString = Integer.toHexString(unsignedInt);
		 if (unsignedInt < 0x10) {
		     System.out.print( " 0"+hexString);
		 } else { 
		     System.out.print( " "+hexString);
		 }
             }
             System.out.println(); 
             System.out.println("JDJSTP.debug: ");
         }
     }


      } catch (IOException e) { 
        e.printStackTrace(); 
      }
     
   }


  public void closeWriter() {
    if (writer != null) {
      writer.flush(); 
      writer.close();
      writer=null; 
    }
  }
  
  public void stopReading() { 
    reading  = false; 
    if (secondaryThread != null) { 
      secondaryThread.stopReading(); 
    }
  }
  
  public void setSecondaryOutputThread(JDJSTPOutputThread secondaryThread) {
    this.secondaryThread = secondaryThread; 
  }

  public String getOutput() {
      return outputBuffer.toString(); 
  }

  public String getParsedJobName() {
    return parsedJobName;
  }


  public String getParsedPid() {
    return parsedPid;
  } 

}
