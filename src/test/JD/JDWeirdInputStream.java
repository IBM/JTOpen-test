///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDWeirdInputStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



package test.JD;
import java.io.*;

public class JDWeirdInputStream  extends InputStream {

    public String readPattern = null;
    int patternOffset = 0;
    int patternLength = 0;
    int thisBlock;
    int thisBlockRead; 
    int bytesRead = 0; 
    boolean unicode = false; 
    
    public JDWeirdInputStream(String readPattern) {
	this.readPattern = readPattern;
	patternOffset = 0;
	patternLength = readPattern.length();
        thisBlock = readPattern.charAt( 0) - '0';
        thisBlockRead = 0;
        bytesRead = 0; 
    }

    public JDWeirdInputStream(String readPattern, String unicode) {
        this.unicode = true; 
        this.readPattern = readPattern;
        patternOffset = 0;
        patternLength = readPattern.length();
        thisBlock = readPattern.charAt( 0) - '0';
        thisBlockRead = 0;
        bytesRead = 0; 
  }

    
    public int read() throws IOException {
       int rc; 
       
       while( (thisBlockRead >= thisBlock) && (patternOffset < patternLength)) { 
         patternOffset++; 
         if (patternOffset < patternLength) { 
           thisBlock = readPattern.charAt( patternOffset) - '0';
           thisBlockRead = 0;
         }
       }
       if (patternOffset < patternLength) { 
         if (unicode) {
           if (bytesRead %2 == 0) {
             rc = 0; 
           } else {
             rc = 0x20 + ( (bytesRead / 2) & 0x5F );
           }
         } else { 
           rc = 0x20 + ( bytesRead & 0x5F );
         }
         bytesRead++; 
         thisBlockRead++; 
       } else { 
         rc = -1; 
       }
       
       
       return rc; 
      
    }

    public int read(byte[] b, int off, int len) {
      int count = 0; 
      if (patternOffset >= patternLength) { 
        return -1; 
      }
      // Read what is possible from the current pattern
      while (thisBlockRead < thisBlock && (count < len)) {
        if (unicode) {
          if (bytesRead % 2 == 0) {
            b[off] = 0; 
          } else {
            b[off] = (byte)(0x20 + ( (bytesRead / 2) & 0x5F ));
          }
          
        } else { 
           b[off] = (byte)( 0x20 + ( bytesRead & 0x5F)) ;
        }
        bytesRead++; 
        thisBlockRead++;
        count++; 
        off++;
      }
      // 
      // Move to next block in pattern if read at least
      // as much as expected. 
      // 
      if (count < len) { 
        patternOffset++; 
        if (patternOffset < patternLength) { 
          thisBlock = readPattern.charAt( patternOffset) - '0';
          thisBlockRead = 0;
        }
      }
      return count; 
    }


    
    public static int doTestRead(String pattern, String expected) { 
      JDWeirdInputStream is = new JDWeirdInputStream(pattern); 
      try {
        String out = ""; 
        byte b[] = new byte[80]; 
        int count = is.read(b, 0, b.length); 
        while (count >= 0) {
          for (int i = 0; i < count; i++){
            String b1 = Integer.toHexString(b[i]); 
            while (b1.length() < 2) {
              b1 = "0"+b1; 
            }
            out += b1;
          }
          if (count == 0) {
            out += "X.";
          } else {
            out += "."; 
          }
          count = is.read(b, 0, b.length); 
        }
        is.close(); 
        if (out.equals(expected)) { 
          System.out.println("read passed "+pattern);
          return 1; 
        } else {
          System.out.println("READ FAILED:  pattern"+pattern+
                             "\n   expected="+expected+
                             "\n   actual  ="+out); 
          return 0; 
        }
        
      }  catch (Exception e) {
        
        e.printStackTrace();
        return 0; 
      }
    }

    public static int doTestRead(String pattern, String expected, int maxSize) { 
      JDWeirdInputStream is = new JDWeirdInputStream(pattern); 
      try {
        String out = ""; 
        byte b[] = new byte[80]; 
        int size = 0; 
        int readSize = maxSize; 
        if (readSize > b.length) readSize = b.length; 
        int count = is.read(b, 0, readSize); 
        while (count >= 0 && (size < maxSize)) {
          size += count; 
          for (int i = 0; i < count; i++){
            String b1 = Integer.toHexString(b[i]); 
            while (b1.length() < 2) {
              b1 = "0"+b1; 
            }
            out += b1;
          }
          if (count == 0) {
            out += "X.";
          } else {
            out += "."; 
          }
          readSize = (maxSize - size); 
          if (readSize > b.length) readSize = b.length; 
          count = is.read(b, 0, readSize); 
        }
        is.close(); 
        if (out.equals(expected)) { 
          System.out.println("read passed "+pattern);
          return 1; 
        } else {
          System.out.println("READ FAILED:  pattern"+pattern+
                             "\n   expected="+expected+
                             "\n   actual  ="+out); 
          return 0; 
        }
        
      }  catch (Exception e) {
        
        e.printStackTrace();
        return 0; 
      }
    }


    
    
    public static int doTestSimpleRead(String pattern, String expected) { 
      JDWeirdInputStream is = new JDWeirdInputStream(pattern); 
      try {
        String out = ""; 
        int b = is.read(); 
        while (b >= 0) {
          String b1 = Integer.toHexString(b); 
          while (b1.length() < 2) {
            b1 = "0"+b1; 
          }
          out += b1; 
          b = is.read();
        }
        is.close(); 
        if (out.equals(expected)) { 
          System.out.println("simple passed "+pattern);
          return 1; 
        } else {
          System.out.println("SIMPLE FAILED:  pattern"+pattern+
                             "\n   expected="+expected+
                             "\n   actual  ="+out); 
          return 0; 
        }
        
      }  catch (Exception e) {
        
        e.printStackTrace();
        return 0; 
      }
    }

    
    public static int doTestReadUnicode(String pattern, String expected) { 
      JDWeirdInputStream is = new JDWeirdInputStream(pattern, "UNICODE"); 
      try {
        String out = ""; 
        byte b[] = new byte[80]; 
        int count = is.read(b, 0, b.length); 
        while (count >= 0) {
          for (int i = 0; i < count; i++){
            String b1 = Integer.toHexString(b[i]); 
            while (b1.length() < 2) {
              b1 = "0"+b1; 
            }
            out += b1;
          }
          if (count == 0) {
            out += "X.";
          } else {
            out += "."; 
          }
          count = is.read(b, 0, b.length); 
        }
        is.close();
        if (out.equals(expected)) { 
          System.out.println("read passed "+pattern);
          return 1; 
        } else {
          System.out.println("READ FAILED:  pattern"+pattern+
                             "\n   expected="+expected+
                             "\n   actual  ="+out); 
          return 0; 
        }
        
      }  catch (Exception e) {
        
        e.printStackTrace();
        return 0; 
      }
    }

    public static int doTestReadUnicode(String pattern, String expected, int maxSize) { 
      JDWeirdInputStream is = new JDWeirdInputStream(pattern, "UNICODE"); 
      try {
        String out = ""; 
        byte b[] = new byte[80]; 
        int size = 0; 
        int readSize = maxSize; 
        if (readSize > b.length) readSize = b.length; 
        int count = is.read(b, 0, readSize); 
        while (count >= 0 && (size < maxSize)) {
          size += count; 
          for (int i = 0; i < count; i++){
            String b1 = Integer.toHexString(b[i]); 
            while (b1.length() < 2) {
              b1 = "0"+b1; 
            }
            out += b1;
          }
          if (count == 0) {
            out += "X.";
          } else {
            out += "."; 
          }
          readSize = (maxSize - size); 
          if (readSize > b.length) readSize = b.length; 
          count = is.read(b, 0, readSize); 
        }
        is.close(); 
        if (out.equals(expected)) { 
          System.out.println("read passed "+pattern);
          return 1; 
        } else {
          System.out.println("READ FAILED:  pattern"+pattern+
                             "\n   expected="+expected+
                             "\n   actual  ="+out); 
          return 0; 
        }
        
      }  catch (Exception e) {
        
        e.printStackTrace();
        return 0; 
      }
    }


    
    
    public static int doTestSimpleReadUnicode(String pattern, String expected) { 
      JDWeirdInputStream is = new JDWeirdInputStream(pattern, "UNICODE"); 
      try {
        String out = ""; 
        int b = is.read(); 
        while (b >= 0) {
          String b1 = Integer.toHexString(b); 
          while (b1.length() < 2) {
            b1 = "0"+b1; 
          }
          out += b1; 
          b = is.read();
        }
        is.close(); 
        if (out.equals(expected)) { 
          System.out.println("simple passed "+pattern);
          return 1; 
        } else {
          System.out.println("SIMPLE FAILED:  pattern"+pattern+
                             "\n   expected="+expected+
                             "\n   actual  ="+out); 
          return 0; 
        }
        
      }  catch (Exception e) {
        
        e.printStackTrace();
        return 0; 
      }
    }

    
    
    
    public static void main(String[] args) {
      System.out.println("Unit test for JDWeirdInputStream"); 
      
      String pattern;
      String expected; 
      int passed = 0; 
      int total = 0; 
      
      pattern="1"; expected = "20"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 

      pattern="0001"; expected = "20"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 

      pattern="1000"; expected = "20"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 

      pattern="123"; expected = "202122232425"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 
      
      pattern="0000123"; expected = "202122232425"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 
      
      pattern="1000023"; expected = "202122232425"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 
      
      pattern="12000003"; expected = "202122232425"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 
      
      pattern="12300000"; expected = "202122232425"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 
      
      pattern="00100200300"; expected = "202122232425"; 
      passed+= doTestSimpleRead(pattern, expected); 
      total++; 
      
      pattern="1"; expected = "20."; 
      passed+= doTestRead(pattern, expected); 
      total++; 

      pattern="0001"; expected = "X.X.X.20."; 
      passed+= doTestRead(pattern, expected); 
      total++; 

      pattern="1000"; expected = "20.X.X.X."; 
      passed+= doTestRead(pattern, expected); 
      total++; 

      pattern="123"; expected = "20.2122.232425."; 
      passed+= doTestRead(pattern, expected); 
      total++; 
      
      pattern="0000123"; expected = "X.X.X.X.20.2122.232425."; 
      passed+= doTestRead(pattern, expected); 
      total++; 
      
      pattern="1000023"; expected = "20.X.X.X.X.2122.232425."; 
      passed+= doTestRead(pattern, expected); 
      total++; 
      
      pattern="12000003"; expected = "20.2122.X.X.X.X.X.232425."; 
      passed+= doTestRead(pattern, expected); 
      total++; 
      
      pattern="12300000"; expected = "20.2122.232425.X.X.X.X.X."; 
      passed+= doTestRead(pattern, expected); 
      total++; 
      
      pattern="00100200300"; expected = "X.X.20.X.X.2122.X.X.232425.X.X."; 
      passed+= doTestRead(pattern, expected); 
      total++; 

      pattern="00100200300"; expected = "X.X.20.X.X.2122.X.X.23."; 
      passed+= doTestRead(pattern, expected, 4); 
      total++; 

      pattern="00100200300"; expected = "X.X.20.X.X.2122.X.X.2324."; 
      passed+= doTestRead(pattern, expected, 5); 
      total++; 

      pattern="00100200300"; expected = "X.X.20.X.X.2122.X.X.232425."; 
      passed+= doTestRead(pattern, expected, 6); 
      total++; 

      pattern="00100200300"; expected = "X.X.20.X.X.2122.X.X.232425.X.X."; 
      passed+= doTestRead(pattern, expected, 7); 
      total++; 

      
      pattern="1"; expected = "00"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 

      pattern="0001"; expected = "00"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 

      pattern="1000"; expected = "00"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 

      pattern="123"; expected = "002000210022"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 
      
      pattern="0000123"; expected = "002000210022"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 
      
      pattern="1000023"; expected = "002000210022"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 
      
      pattern="12000003"; expected = "002000210022"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 
      
      pattern="12300000"; expected = "002000210022"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 
      
      pattern="00100200300"; expected = "002000210022"; 
      passed+= doTestSimpleReadUnicode(pattern, expected); 
      total++; 
      
      pattern="1"; expected = "00."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 

      pattern="0001"; expected = "X.X.X.00."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 

      pattern="1000"; expected = "00.X.X.X."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 

      pattern="123"; expected = "00.2000.210022."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 
      
      pattern="0000123"; expected = "X.X.X.X.00.2000.210022."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 
      
      pattern="1000023"; expected = "00.X.X.X.X.2000.210022."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 
      
      pattern="12000003"; expected = "00.2000.X.X.X.X.X.210022."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 
      
      pattern="12300000"; expected = "00.2000.210022.X.X.X.X.X."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 
      
      pattern="00100200300"; expected = "X.X.00.X.X.2000.X.X.210022.X.X."; 
      passed+= doTestReadUnicode(pattern, expected); 
      total++; 

      pattern="00100200300"; expected = "X.X.00.X.X.2000.X.X.21."; 
      passed+= doTestReadUnicode(pattern, expected, 4); 
      total++; 

      pattern="00100200300"; expected = "X.X.00.X.X.2000.X.X.2100."; 
      passed+= doTestReadUnicode(pattern, expected, 5); 
      total++; 

      pattern="00100200300"; expected = "X.X.00.X.X.2000.X.X.210022."; 
      passed+= doTestReadUnicode(pattern, expected, 6); 
      total++; 

      pattern="00100200300"; expected = "X.X.00.X.X.2000.X.X.210022.X.X."; 
      passed+= doTestReadUnicode(pattern, expected, 7); 
      total++; 


      
       if (passed == total) {
         System.out.println(" ALL TESTS PASSED "); 
         
       } else { 
         System.out.println(" ******  ONLY "+passed+" OF "+total+" TESTS PASSED"); 
       }
      
      
    }

}
