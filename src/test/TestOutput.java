///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JestOutput.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.ReturnCodeException;
import com.ibm.as400.access.AS400;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Date;
import java.util.Hashtable;

// For NLS testing
import java.util.Properties;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


final class TestOutput extends FilterOutputStream
{
  // Represents the output stream for file output.
  public PrintWriter fileOut = null;
  

/**
  This will create an output stream which will write output
  to System.out only.
**/
  public TestOutput()
  {
	  
    super(System.out); // left this for compatibility
  
  }


/**
  This will create an output stream which will write output
  to both System.out and the file.
**/
  public TestOutput(FileOutputStream file)
  {
    super(System.out); // left this for compatibility
    fileOut = new PrintWriter(file, true);
  }




/**
  Override to handle file and text area.
**/
  public void close() throws IOException
  {
    if (fileOut != null)
      fileOut.close();
//    super.close();  -- I can't believe we were closing System.out!
  }


/**
  Override to handle file and text area.
**/
  public void flush() throws IOException
  {
    if (fileOut != null)
      fileOut.flush();
//    super.flush();
    System.out.flush();
  }


/**
  Override to handle file and text area.
**/
  // Note this method seems to be used to output all characters
  // in strings, not just numbers, so input should be treated as
  // a character, not a number.
  public void write(int b) throws IOException
  {
    if (fileOut != null)
    {
      // file requires CRLF to get newline
      if ((char)b == '\n')
        fileOut.write((int)'\r');;
      fileOut.write(b);
    }
//    super.write(b);
    System.out.write(b);
  }


/**
  Override to handle file and text area.
**/
  public void write(byte b[]) throws IOException
  {
    if (fileOut != null)
    {
      fileOut.write(new String(b));
    }
//    super.write(b);
    System.out.write(b);
  }


/**
  Override to handle file and text area.
**/
public void write(byte b[], int off, int len) throws IOException {
	if (fileOut != null) {
		fileOut.write(new String(b, off, len));
	}
	System.out.write(b, off, len);


}
}
