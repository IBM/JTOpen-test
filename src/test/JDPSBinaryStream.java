///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBinaryStream.java
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
public class JDPSBinaryStream extends InputStream {
  long size;
  int bytesRead = 0; 
  public JDPSBinaryStream(long size2) { 
    this.size = size2; 
  }

  public int read() throws IOException {
    if (bytesRead < size) {
      int value = 0x20 + bytesRead % 0x5f; 
      bytesRead++; 
      return value; 
    } else {
      return -1; 
    }
  }

  public int available() throws IOException {
    return (int) size - bytesRead; 
  }
}
