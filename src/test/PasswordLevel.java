///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PasswordLevel.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.*;
import java.net.*;

public class PasswordLevel {

    public static int get(String hostname) throws UnknownHostException, IOException {
      int passwordLevel = -1; 
      byte[] outbuffer = {
          (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x34,
          (byte)0x00,(byte)0x00,(byte)0xE0,(byte)0x09,
          (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
          (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
          (byte)0x00,(byte)0x00,(byte)0x70,(byte)0x03,
          
          (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0A,
          (byte)0x11,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,
          (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,
          (byte)0x11,(byte)0x02,(byte)0x00,(byte)0x05,
          (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E,
          (byte)0x11,(byte)0x03,(byte)0x00,(byte)0x00,
          (byte)0x01,(byte)0x6B,(byte)0xF7,(byte)0x76,
          (byte)0x53,(byte)0x06    
      };
      
  Socket s =  new Socket(hostname, 8476);
  InputStream in = s.getInputStream();
  OutputStream out = s.getOutputStream();

    out.write(outbuffer); 
    int value = in.read();
    while ((value >= 0) && (passwordLevel < 0 ))  {
      
      // Note:  This could fail if the random see from the server has 0x1119
      // You should probably parse this correctly. 
      
      if (value == 0x11) {
        value = in.read(); 
        if (value == 0x19) {
          passwordLevel = in.read(); 
         
        }
      } else {
        value = in.read(); 
      }
    }

      return passwordLevel; 
    }


    public static void main(String args[]) {
      
      try {
        System.out.println("The password level for "+args[0]+" is "+get(args[0]));
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } 
    }
}

