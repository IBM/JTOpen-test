///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  GetNotTested.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test; 
import java.io.*;
import java.util.*;
/**
 *
**/
public class GetNotTested
{
  public static String replaceLessThan(String line)
  {
    int index = line.indexOf("<");
    boolean firstTime = true;
    StringBuffer buf = null;
    String newLine = line;
    while (index != -1)
    {
      // Replace the less than sign with "&lt;"
      if (firstTime)
      {
        buf = new StringBuffer(newLine.substring(0, index));
        firstTime = false;
      }
      buf.append("&lt;");
      buf.append(newLine.substring(index+1, newLine.length()));
      newLine = newLine.substring(index+1, newLine.length());
      index = newLine.indexOf("<");
    }
    return (buf == null)? line : buf.toString();
  }

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
        System.out.println("Usage: java GetNotTested <inputFileName>");
        System.out.println("  where <inputFileName> is the file containing the results of the coverage tool.");
        System.out.println("");
        System.out.println("  NOTE: Output will be written to file NotCovered.html and if the file exists, it will be overwritten.");
        return;
      }
      if (args.length != 1)
      {
        System.out.println("Usage: java GetNotTested <inputFileName> <outputFileName>");
        System.out.println("  where <inputFileName> is the file containing the results of the coverage tool.");
        System.out.println("");
        System.out.println("  NOTE: Output will be written to file NotCovered.html and if the file exists, it will be overwritten.");
        return;
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
      // Get to the beginning of the class information
      String line = null;
      for (line = in.readLine(); line.indexOf("Class") == -1; line = in.readLine())
      {
      }
      Vector classes = new Vector();
      Vector aClass;
      while (line != null)
      {
        aClass = new Vector();
        aClass.addElement(line);
        for (line = in.readLine(); line != null && line.indexOf("Class") != 0; line = in.readLine())
        {
          if (line.endsWith(" 0") || line.endsWith("\t0"))
          {
            if (line.indexOf("getCopyright") != -1 || line.indexOf("Copyright") != -1)
            {
            }
            else
            {
              // Cleanup the line
              line = line.substring(0, line.length() - 1);
              line = line.trim();
              line = "    " + line;
              aClass.addElement(replaceLessThan(line));
            }
          }
        }
        if (aClass.size() > 1)
        {
          classes.addElement(aClass);
        }
      }

      ///////////////////////////////////////////////////////////////////
      // Sort the classes
      ///////////////////////////////////////////////////////////////////
      Vector[] sorted = new Vector[classes.size()];
      classes.copyInto(sorted);
      int length = sorted.length;
      Vector temp;
      for (int j = 0; j < length; ++j)
      {
        for (int k = j + 1; k < length; ++k)
        {
          if (((String)sorted[k].elementAt(0)).compareTo(((String)sorted[j].elementAt(0))) < 0)
          {
            temp = sorted[j];
            sorted[j] = sorted[k];
            sorted[k] = temp;
          }
        }
      }

      ///////////////////////////////////////////////////////////////////
      // Write the results to the output file
      ///////////////////////////////////////////////////////////////////
      if (sorted.length != 0)
      { // Write out html lines for heading and order list
        out.write("<h1>Classes and methods not yet covered</h1>");
        out.newLine();
        out.write("<ol>");
        out.newLine();
      }

      int z;
      for (int l = 0; l < sorted.length; ++l)
      {
        z = 0;
        for (Enumeration e = sorted[l].elements(); e.hasMoreElements();)
        {
          if (z == 1)
          {
            out.write("<ul>");
            out.newLine();
          }
          out.write("<li>");
          line = (z == 0)? ((String)e.nextElement()).substring(7) : (String)e.nextElement();
          out.write(line, 0, line.length());
          out.newLine();
          ++z;
        }
        out.newLine();
        out.write("</ul>");
        out.newLine();
      }
      out.write("</ol>");
      out.newLine();
      out.flush();
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
