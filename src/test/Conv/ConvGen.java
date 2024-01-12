///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvGen.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.io.CharConversionException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.ibm.as400.access.Converter;

public class ConvGen
{
    public static void main(String args[])
    {
	String inputFile = args[0];
	String outputFile = args[1];
	int ccsidIn = Integer.parseInt(args[2]);
	int ccsidOut = Integer.parseInt(args[3]);

	boolean result = ConvGen.convertAndWrite(inputFile, outputFile, ccsidIn, ccsidOut);
	if (result)
	{
	    System.out.print("Success");
	}
	else
	{
	    System.out.print("Failure");
	}
	System.out.println(": Converting " + inputFile + " to " + outputFile);
    }

    static boolean convertAndWrite(String inputFile, String outputFile, int ccsidIn, int ccsidOut)
    {
	boolean compare = true;
	try
	{
	    DataInputStream disIn = new DataInputStream(ConvGen.class.getResourceAsStream(inputFile));
	    DataOutputStream disOut = new DataOutputStream(new FileOutputStream(outputFile));

	    if (ccsidIn == 13488)
	    {
		int lengthIn = disIn.available()/2;
		char[] charData = new char[lengthIn];
		for (int i = 0; i < lengthIn; ++i)
		{
		    try
		    {
			charData[i] = disIn.readChar();
		    }
		    catch (EOFException e)
		    {
			lengthIn = i;
		    }
		}

		Converter converter = new Converter(ccsidOut);
		String inString = new String(charData);
		byte[] outBytes = converter.stringToByteArray(inString);
		disOut.write(outBytes, 0, outBytes.length);
	    }
	    else
	    {
		int lengthIn = disIn.available();
		byte[] byteData = new byte[lengthIn];
		disIn.readFully(byteData);
		Converter converter = new Converter(ccsidIn);
		String outString = converter.byteArrayToString(byteData, 0, lengthIn);
		disOut.writeChars(outString);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    compare = false;
	}
	return compare;
    }
}
   
