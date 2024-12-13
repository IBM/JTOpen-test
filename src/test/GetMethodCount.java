///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  GetMethodCount.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test; 


import java.io.*;
/**
 *
**/
public class GetMethodCount
{

  public static void main(String[] args)
  {
    BufferedReader in = null;
    BufferedWriter out = null;
    try
    {
      ///////////////////////////////////////////////////////////////////
      //  Help and error checking
      ///////////////////////////////////////////////////////////////////
      if (args.length == 1 && args[0].equals("-h"))
      {
        System.out.println("Usage: java GetMethodCount <inputFileName>");
        System.out.println("  where <inputFileName> is the file containing the results of the coverage tool.");
        System.out.println("");
      }
      // Error checking
      File input = new File(args[0]);
      if (!input.exists())
      {
        System.out.println("Error: File " + args[0] + " does not exist.");
        return;
      }

      ///////////////////////////////////////////////////////////////////
      //  Extract the not tested classes and methods
      ///////////////////////////////////////////////////////////////////
      in = new BufferedReader(new FileReader(args[0]));
      out = new BufferedWriter(new FileWriter("NotCovered.html"));
      // Get to the beginning of the method information
      String line = null;
      for (line = in.readLine(); line.indexOf(" Method") == -1; line = in.readLine())
      {
      }
      int methodCount = 0;
      while (line != null)
      {
        for (line = in.readLine(); line != null && !line.equals(""); methodCount++, line = in.readLine())
        {
          System.out.println(line);
        }
        // Get to beginning of next method info
        for (line = in.readLine(); line != null && line.indexOf(" Method") == -1; line = in.readLine())
        {
        }
      }
      System.out.println("Number of methods: " + String.valueOf(methodCount));
    }
    catch(Exception e)
    {
      System.out.println("Exception occurred:");
      e.printStackTrace();
    }
    try
    {
      in.close();
      out.close();
    }
    catch(Exception e)
    {
    }
  }
}
