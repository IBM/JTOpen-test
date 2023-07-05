///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvMain.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.CharConversionException;
import java.io.DataInputStream;
import java.io.EOFException;

import com.ibm.as400.access.Converter;
import com.ibm.as400.access.Trace;

public class ConvMain
{
    public static void main(String args[])
    {
	String inputFile = args[0];
	String resultsFile = args[1];
	int ccsidIn = Integer.parseInt(args[2]);
	int ccsidResult = Integer.parseInt(args[3]);
	int lengthIn = -1;
	int lengthResult = -1;
	boolean ExceptionExpected = false;
	if (args.length > 4) lengthIn = Integer.parseInt(args[4]);
	if (args.length > 5) lengthResult = Integer.parseInt(args[5]);
	if (args.length > 6) ExceptionExpected = (Boolean.valueOf(args[6])).booleanValue();

	boolean result = ConvMain.convertAndCompare(inputFile, resultsFile, ccsidIn, ccsidResult, lengthIn, lengthResult, ExceptionExpected);
	if (result)
	{
	    System.out.print("Success");
	}
	else
	{
	    System.out.print("Failure");
	}
	System.out.println(": Comparing " + inputFile + " to " + resultsFile);
    }

    static boolean convertAndCompare(String inputFile, String resultsFile, int ccsidIn, int ccsidResult, int lengthIn, int lengthResult, boolean ExceptionExpected)
    {
	boolean compare = true;
	try
	{
	    DataInputStream disIn = new DataInputStream(ConvMain.class.getResourceAsStream(inputFile));
	    DataInputStream disResult = new DataInputStream(ConvMain.class.getResourceAsStream(resultsFile));

	    if (ccsidIn == 13488)
	    {
		if (lengthIn == -1) lengthIn = disIn.available()/2;
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
		String inString = new String(charData);

		if (lengthResult == -1) lengthResult = disResult.available();
		byte[] resultBytes = new byte[lengthResult];
		disResult.readFully(resultBytes);

		Converter converter = new Converter(ccsidResult);
		int ccsidGot = converter.getCcsid();
		if (ccsidGot != ccsidResult)
		{
		    Trace.log(Trace.ERROR, "Asked for CCSID: " + ccsidResult + " Got CCSID: " + ccsidGot);
		    compare = false;
		}
		String encodingGot = converter.getEncoding();
		Trace.log(Trace.INFORMATION, "CCSID: " + ccsidResult + " reports encoding: " + encodingGot);

		byte[] outBytes = new byte[lengthResult];
		try
		{
		    converter.stringToByteArray(inString, outBytes, 0, lengthResult);
		}
		catch (CharConversionException e)
		{
		    if (!ExceptionExpected)
		    {
			Trace.log(Trace.ERROR, "Unexpected CharacterConversionException");
			compare = false;
		    }
		}

		for (int i = 0; i < lengthResult; ++i)
		{
		    if (outBytes[i] != resultBytes[i] &&  // don't match
			(outBytes[i] != 0x6f || resultBytes[i] != 0x00) && // not sub chars ebcdic
			(outBytes[i] != 0x6f || resultBytes[i] != 0x3F) && // not sub chars both
			(outBytes[i] != 0x3f || resultBytes[i] != 0x00))   // not sub chars ascii
		    {
			Trace.log(Trace.INFORMATION, "Compare failure at byte: " + Integer.toHexString(i) +
					   " Expected: " + Integer.toHexString(resultBytes[i]&0xFF) +
					   " Received: " + Integer.toHexString(outBytes[i]&0xFF));
			compare = false;
		    }
		}
	    }
	    else
	    {
		if (lengthIn == -1) lengthIn = disIn.available();
		byte[] inBytes = new byte[lengthIn];
		disIn.readFully(inBytes);

		if (lengthResult == -1) lengthResult = disResult.available()/2;
		char[] resultChars = new char[lengthResult];
		for (int i = 0; i < lengthResult; ++i)
		{
		    try
		    {
			resultChars[i] = disResult.readChar();
		    }
		    catch (EOFException e)
		    {
			lengthResult = i;
		    }
		}

		if (ccsidIn == 99999) ccsidIn = 13488;
		Converter converter = new Converter(ccsidIn);
		int ccsidGot = converter.getCcsid();
		if (ccsidGot != ccsidIn)
		{
		    Trace.log(Trace.INFORMATION, "Asked for CCSID: " + ccsidIn + " Got CCSID: " + ccsidGot);
		}
		String encodingGot = converter.getEncoding();
		Trace.log(Trace.INFORMATION, "CCSID: " + ccsidIn + " reports encoding: " + encodingGot);

		String outString = converter.byteArrayToString(inBytes, 0, lengthIn);
		char[] outChars = outString.toCharArray();

		for (int i = 0; i < lengthResult; ++i)
		{
		    if (outChars[i] != resultChars[i] && // don't match
			(outChars[i] != (char)0xFFFD || resultChars[i] != (char)0x001A))  // not sub chars
		    {
			Trace.log(Trace.INFORMATION, "Compare failure at char: " + Integer.toHexString(i) +
					   " Expected: " + Integer.toHexString((int)resultChars[i]) +
					   " Received: " + Integer.toHexString((int)outChars[i]));
			compare = false;
		    }
		}
	    }
	    disIn.close();
	    disResult.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    compare = false;
	}
	return compare;
    }
}
   
