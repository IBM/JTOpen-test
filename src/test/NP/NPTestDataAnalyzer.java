///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPTestDataAnalyzer.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.NP;
import java.io.*;
import com.ibm.as400.access.*; 
// Tests the NPDataAnalyzer class
// Typical usage is something like:
// <PRE>
//    java com.ibm.as400.access.NPTestDataAnalyzer test.dat
//  OR to test a bunch of files at once:
//    for (%i) in *.scs do java com.ibm.as400.access.NPTestDataAnalyzer %i
// </PRE>
//
class NPTestDataAnalyzer
{



    public static void main(String args[])
    {
        String fileName = null;
        byte[]  buffer = new byte[2048];
        FileInputStream dataFileStream = null;
        try
        {


           if (args.length != 0)
           {
               fileName = args[0];
           } else {
               System.out.println("Enter the name of a file to analyze:");
               fileName = (new BufferedReader(new InputStreamReader(System.in))).readLine();
           }
           try
           {
               dataFileStream = new FileInputStream(fileName);
               int bytesRead = dataFileStream.read(buffer);

               dataFileStream.close();
               if (bytesRead == -1)
               {
                   System.out.println("Unable to read any bytes from " + fileName);
               } else {

                  String dataType = NPDataAnalyzer.sniff(buffer, 0, bytesRead);
                  System.out.println("Analyzed " + bytesRead + " bytes in file " +
                                      fileName + " and the data appears to be " +
                                      dataType);
               }
           }
           catch (FileNotFoundException e)
           {
               System.out.println("Couldn't find this file: " + fileName);
           }
        }

        catch (Exception e)
        {
            System.out.println(" Exception caught! - " + e);
        }


    }
 }



