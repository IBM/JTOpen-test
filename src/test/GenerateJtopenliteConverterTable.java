package test; 
import java.io.*;
import java.util.*;
import com.ibm.as400.access.*; 

public class GenerateJtopenliteConverterTable
{
  //5461
//  static final int LIMIT = 70000; // Each char is slash-u-x-x-x-x so that is 6 chars in the String per char in the array.
  // With a potential 64K chars, we could have 12 total Strings.
  static AS400 sys = null;

  static boolean compress_ = false; // Compress the conversion table
                                   // Note: turn this off for debugging purposes

  static boolean ascii_ = false; // Indicates if listed ccsids are ascii tables or not

  static boolean bidi_ = false; // Indicates if listed ccsids are bidi tables or not
  // Note: bidi_ and ascii_ cannot both be true

  static boolean showOffsets_ = false;  // Indicates of the offsets should be printed in the tables
  public static void main(String[] args)
  {
    if (args.length < 4)
    {
      System.out.println("Usage: java com.ibm.as400.access.GenerateJtopenliteConverterTable system uid pwd [-nocompress] [-ascii] [-bidi] [-showOffsets] ccsid [ccsid2] [ccsid3] [ccsid4] ...");
      System.exit(0);
    }

    try
    {
      sys = new AS400(args[0], args[1], args[2]);
      sys.connectService(AS400.CENTRAL);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(0);
    }

    int start = 3;
    if (args[start].equals("-nocompress"))
    {
      compress_ = false;
      ++start;
    }
    if (args[start].equals("-ascii"))
    {
      ascii_ = true;
      ++start;
    }
    if (args[start].equals("-bidi"))
    {
      bidi_ = true;
      ++start;
    }

    if (args[start].equals("-showOffsets"))
    {
      showOffsets_ = true;
      ++start;
    }

    for (int i=start; i<args.length; ++i)
    {
      go((new Integer(args[i])).intValue());
    }
  }


  static String formattedChar(char x) {
      int num = 0xFFFF & (int) x;

      String s = "\\u";
      if (num < 16) s += "0";
      if (num < 256) s += "0";
      if (num < 4096) s += "0";
      s += Integer.toHexString(num).toUpperCase();
      return s;
  }

  static void go(int ccsid)
  {
    char[] table1 = new char[0];
    char[] table2 = new char[0];
    char[][] surrogateTable = null; 

    // int numTables1 = 1;
    // int numTables2 = 1;


    try  {
      AS400ImplRemote impl = (AS400ImplRemote)sys.getImpl();

      NLSTableDownload down = new NLSTableDownload(impl);
      down.connect();

      if (ccsid == 1089) // There are currently no tables for 1089->13488->1089; use 61952 instead, since it would be the same anyway.
      {
        System.out.println("Special case for ccsid 1089.");
        System.out.println("Retrieving "+ccsid+"->61952 table...");
        table1 = down.download(ccsid, 61952, false);
      }
      else
      {
        System.out.println("Retrieving "+ccsid+"->13488 table...");
        table1 = down.download(ccsid, 13488, false);
      }
      if (table1 == null || table1.length == 0)
      {
        System.out.println(ccsid+" must be double-byte. Performing secondary retrieve of "+ccsid+"->1200 table...");
        down.disconnect();
        down.connect();
        table1 = down.download(ccsid, 1200, true);
      }

      System.out.println("  Size: "+table1.length);
      if (table1.length > 65536) {
	  System.out.println("Size is > 65536.  Fixing table");
	  int next = 0; 
	  int from = 0; 
	  char[] newTable = new char[65536];
	  while (from < table1.length && next < 65536) {
	      int c = 0xFFFF & (int) table1[from]; 
	      if ((c >= 0xD800) && (c <=0xDFFF)) {
		  // Mark as surrogate
		  newTable[next]=(char) 0xD800;
		  // add to surrogate table
		  if (surrogateTable == null) {
		      surrogateTable = new char[65536][]; 
		  }
		  char[] pair = new char[2];
		  surrogateTable[next] = pair; 
		  pair[0] = (char) (0xFFFF & (int) table1[from]);
		  pair[1] = (char) (0xFFFF & (int) table1[from+1]);
		  /* System.out.println("Warning: Sub at offset "+Integer.toHexString(next)+" for "+Integer.toHexString(0xFFFF & (int) table1[from])+" "+Integer.toHexString(0xFFFF & (int) table1[from+1]));  */ 
		  from +=2;
	      } else {
		  newTable[next]=(char) c; 
		  from++; 
	      }
	      next++; 
	  }
	  table1 = newTable; 

      } 
      down.disconnect();
      down.connect();
      if (ccsid == 1089)
      {
        System.out.println("Special case for ccsid 1089.");
        System.out.println("Retrieving 61952->"+ccsid+" table...");
        table2 = down.download(61952, ccsid, true);
      }
      else
      {
	  /* Use 1200 instead of 13488 */ 
        System.out.println("Retrieving 1200->"+ccsid+" table...");
        table2 = down.download(1200, ccsid, true);
      }
      System.out.println("  Size: "+table2.length);

      sys.disconnectAllServices();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    
    // Write out the ccsid table
    try  {
      String fName = "CCSID"+ccsid+".java";
      FileWriter f = new FileWriter(fName);
      writeHeader(f, ccsid);
      f.write("package com.ibm.jtopenlite.ccsidConversion;\n\n"); 
      f.write("public class CCSID"+ccsid+" implements SingleByteConversion{\n");
      f.write("  static CCSID"+ccsid+" singleton = new CCSID"+ccsid+"();\n"); 
      f.write("  \n"); 
      f.write("  public static SingleByteConversion getInstance() {\n");
      f.write("    return singleton;\n");
      f.write("  }\n");
      f.write("  \n");
      f.write("  public int getCcsid() {\n");
      f.write("    return "+ccsid+";\n");
      f.write("  }\n");
      f.write("  \n");
      f.write("  public byte[] returnFromUnicode() {\n");
      f.write("    return fromUnicode_;\n");
      f.write("  }\n");
      f.write("  \n");
      f.write("  public char[] returnToUnicode() {\n");
      f.write("    return toUnicode_;\n");
      f.write("  }\n");


      f.write("  private static final char[] toUnicode_ = { \n");
      System.out.print("Writing table for conversion from "+ccsid+" to 13488... to "+fName+"\n");
      for (int i=0; i<table1.length; i=i+16)  {
	  if (showOffsets_) {
	      f.write("/* "+Integer.toHexString(i)+" */ "); 
	  } else { 
	      f.write("    ");
	  }
	  for (int j=0; j<16 && (i+j)<table1.length; ++j)
	  {
	      int num = (int)table1[i+j];

	      String s = ""; 
	      switch (num) {
		  /* some numbers are not valid unicode constants */
		  case 0x0a:
		  case 0x0d:
		  case 0x5c:
		  case 0x27:
		      s="(char)0x";
		      s += Integer.toHexString(num).toUpperCase();
		      f.write(s);
		      f.write(",");
		      break; 
		  default:
		      s = "'\\u";
		      if (num < 16) s += "0";
		      if (num < 256) s += "0";
		      if (num < 4096) s += "0";
		      s += Integer.toHexString(num).toUpperCase();
		      f.write(s);
		      f.write("',");
	      }

	  }

	  if (i+16 < table1.length) f.write("\n");
	  else f.write("};\n");
      } /* for i */ 
      f.write("\n");
      f.write("\n");

      if (true) {
	  f.write("  private static byte[]  fromUnicode_ = null;  \n");
	  f.write("  /* dynamically generate the inverse table */\n");
	  f.write("  static { \n");
	  f.write("      fromUnicode_ = SingleByteConversionTable.generateFromUnicode(toUnicode_);\n");
	  f.write("  }\n"); 
      } else { 
	  f.write("  private static final byte[]  fromUnicode_ = new byte[] { \n");
	  System.out.print("Writing table for conversion from 13488 to "+ccsid+"... to "+fName+"\n");
	  int table2Length = table2.length;
	  System.out.println("table2[table2Length-1]="+Integer.toHexString(table2[table2Length-1])); 
	  while (table2[table2Length-1] == (char) 0x3f3f) {
	      table2Length--; 
	  };
	  for (int i=0; i<table2Length; i=i+8)     {
	      if (showOffsets_) {
		  f.write("/* "+Integer.toHexString(2*i)+" */ "); 
	      } else { 
		  f.write("    ");
	      }
	      for (int j=0; j<8 && (i+j)<table2Length; ++j)         {
		  int num = (int)table2[i+j]; // these each contain 2 single byte chars
		  int first  = (0xFF00 & num) >> 8;
		  int second = 0xFf & num;
		  if (first >= 0x80) {
		      f.write("(byte)");
		  }
		  f.write("0x"+Integer.toHexString(first)+",");
		  if (second >= 0x80) {
		      f.write("(byte)");
		  }
		  f.write("0x"+Integer.toHexString(second)+",");

	      }
	      if (i+8 < table2Length) f.write("\n");
	      else f.write("};\n");
	  } /* for i */
      }
      f.write("\n");


      f.write("}\n");
      f.close();
  
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.print("Done.\n");
  } /* go */ 






  private static final char repSig = '\uFFFF'; // compression indication character
  private static final char cic_ = repSig;

  private static final char rampSig = '\uFFFE'; // ramp indication character
  private static final char ric_ = rampSig;

  private static final char hbSig = '\u0000'; // high-byte compression indication character
  private static final char pad = '\u0000'; // pad character



  static int repeatCheck(char[] arr, int startingIndex)
  {
    int index = startingIndex+1;
    while (index < arr.length && arr[index] == arr[index-1])
    {
      ++index;
    }
    return(index-startingIndex);
  }


  static final int rampCheck(char[] arr, int startingIndex)
  {
    int index = startingIndex+1;
    while (index < arr.length && arr[index] == arr[index-1]+1)
    {
      ++index;
    }
    return(index-startingIndex);
  }


  static int hbCheck(char[] arr, int startingIndex)
  {
    int index = startingIndex+1;
    while (index < arr.length)
    {
      // check for repeat
      // for 6 repeated chars, we'd need either 3 hb-compressed chars or 3 repeatsig chars, so it's a toss up
      if (repeatCheck(arr, index) > 6) return(index-startingIndex); // at this point though, it's better to stop and do the repeat

      // check for ramp, same reason
      if (rampCheck(arr, index) > 6) return(index-startingIndex);

      // OK, finally check for hb
      if ((arr[index] & 0xFF00) != (arr[index-1] & 0xFF00)) return(index-startingIndex);

      ++index;
    }
    return(index-startingIndex);
  }


  static int numRepeats;
  static int numRamps;
  static int hbRepeats;
  static int charRepeats;

  // This is the new way - 05/04/2000.
  static char[] compressBetter(char[] arr)
  {
    numRepeats = 0;
    numRamps = 0;
    hbRepeats = 0;
    charRepeats = 0;

    // This uses the "correct" compression scheme from my invention disclosure
    // It also employs high-byte compression, something that I did not include in my disclosure.
    StringBuffer buf = new StringBuffer();

    for (int i=0; i<arr.length; ++i)
    {
      int repNum = repeatCheck(arr, i);
      if (repNum > 3) // had enough repeats
      {
        numRepeats++;
        buf.append(repSig);
        buf.append((char)repNum);
        buf.append(arr[i]);
        i += repNum-1;
      }
      else
      {
        int rampNum = rampCheck(arr, i);
        if (rampNum > 3) // had enough in the ramp
        {
          numRamps++;
          buf.append(rampSig);
          buf.append((char)rampNum);
          buf.append(arr[i]);
          i += rampNum-1;
        }
        else
        {
          int hbNum = hbCheck(arr, i);
          --hbNum; // don't include the first char, since we always append it.
          if (hbNum >= 6)
          {
//            System.out.print("HBNUM is "+Integer.toHexString((int)hbNum)+"; ");
            hbRepeats++;
            // pattern is this: ss ss nn nn hh tt xx xx xx xx ...
            // where ss ss is hbSig
            //       nn nn is hbNum
            //       hh tt is the first char (hh is the repeated high byte)
            //       xx is the lower byte of the next char in the sequence
            //       xx repeats hbNum/2 times so that
            //       hbNum is the total number of repeated db chars in the ciphertext, not including the first char.
            //       Note that there may be, in actuality, hbNum*2 +1 chars in the cleartext that fit into the
            //       conversion, but since we'd have to fill out the last char with an empty byte, there's no point
            //       in doing it anyway. Besides, it might be able to be compressed via another scheme with itself as
            //       the starting character.
            // int start = i;
            buf.append(hbSig);
            if (hbNum % 2 == 1) // odd number
            {
              --hbNum; // no point in doing the last char
            }
//            System.out.println("Appending "+Integer.toHexString((int)((char)(hbNum/2))));
            buf.append((char)(hbNum/2)); // hbNum is always even, so this comes out.
//            System.out.print("hb comp: "+Integer.toHexString(hbNum)+": ");
//            for (int b=0; b<hbNum; ++b)
//            {
//              System.out.print(Integer.toHexString((int)arr[i+b])+" ");
//            }
//            System.out.println();
            buf.append(arr[i++]);
            for (int j=0; j<(hbNum/2); ++j)
            {
              char x = (char)(((0x00FF & arr[i+(j*2)])*256) + (0x00FF & arr[i+(j*2)+1]));
              buf.append(x);
            }
            i = i + hbNum - 1;
//            System.out.print("row ("+start+","+i+"): ");
//            for (int b=start-1; b<=i; ++b)
//            {
//             System.out.print(Integer.toHexString((int)arr[b])+" ");
//            }
//            System.out.println();
          }
          else
          {
            buf.append(arr[i]);
            charRepeats++;
            if (arr[i] == repSig || arr[i] == rampSig || arr[i] == hbSig)
            {
              buf.append(pad); // pad
            }
          }
        }
      }
    }
    System.out.println("Compression stats: "+numRepeats+" repeats, "+numRamps+" ramps, "+hbRepeats+" highbytes, "+charRepeats+" regular.");
    numRepeats = 0;
    numRamps = 0;
    hbRepeats = 0;
    charRepeats = 0;
    return buf.toString().toCharArray();
  }



  static char[] decompressBetter(char[] arr)
  {
    // let's try decompressing for testing purposes
    StringBuffer buf = new StringBuffer();
    for (int i=0; i<arr.length; ++i)
    {
      if (arr[i] == repSig)
      {
        if (arr[i+1] == pad)
        {
          buf.append(repSig);
          i++;
        }
        else
        {
          numRepeats++;
          int repNum = arr[i+1];
          char repChar = arr[i+2];
          for (int j=0; j<repNum; ++j)
          {
            buf.append(repChar);
          }
          i += 2;
        }
      }
      else if (arr[i] == rampSig)
      {
        if (arr[i+1] == pad)
        {
          buf.append(rampSig);
          i++;
        }
        else
        {
          numRamps++;
          int rampNum = arr[i+1];
          char rampStart = arr[i+2];
          for (int j=0; j<rampNum; ++j)
          {
            buf.append((char)(j+rampStart));
          }
          i += 2;
        }
      }
      else if (arr[i] == hbSig)
      {
        if (arr[i+1] == pad)
        {
          buf.append(hbSig);
          i++;
        }
        else
        {
          hbRepeats++;
          int hbNum = (0x0000FFFF & arr[++i]);
//          System.out.print("hb decomp: "+Integer.toHexString(hbNum)+": ");
          char firstChar = arr[++i];
          char highByteMask = (char)(0xFF00 & firstChar);
//          System.out.print("found: "+Integer.toHexString((int)arr[i-2])+" ("+Integer.toHexString((int)arr[i-1])+") "+Integer.toHexString((int)firstChar)+" ");
          buf.append(firstChar);
          ++i;
          for (int j=0; j<hbNum; ++j)
          {
            char both = arr[i+j];
            char c1 = (char)(highByteMask + ((0xFF00 & both) >>> 8));
            char c2 = (char)(highByteMask + (0x00FF & both));
            buf.append(c1);
            buf.append(c2);
//            System.out.print(Integer.toHexString((int)c1)+" "+Integer.toHexString((int)c2)+" ");
          }
//          System.out.println(Integer.toHexString((int)arr[i+hbNum]));
          i = i + hbNum - 1;
        }
      }
      else
      {
        buf.append(arr[i]);
        charRepeats++;
      }
    }
    System.out.println("Decompression stats: "+numRepeats+" repeats, "+numRamps+" ramps, "+hbRepeats+" highbytes, "+charRepeats+" regular.");
    numRepeats = 0;
    numRamps = 0;
    hbRepeats = 0;
    charRepeats = 0;
    return buf.toString().toCharArray();
  }


  // This is the old way
  static char[] compress(char[] arr)
  {

    if (arr.length < 3) return arr;
    StringBuffer buf = new StringBuffer();
    char oldold = arr[0];
    char old = arr[1];
    int count = 0;
    boolean inCompression = false; // this flags if we are repeating the same character
    boolean inRamp = false; // this flags if each subsequent characters is the previous character + 1

    for (int i=2; i<arr.length; ++i)
    {
      if (!inCompression && !inRamp)
      {
        if (arr[i] == old && arr[i] == oldold) // Check for repeating
        {
          inCompression = true;
          buf.append(cic_);
          buf.append(oldold);
          count = 3;
        }
        else if (arr[i] == old+1 && arr[i] == oldold+2) // Check for ramp
        {
          inRamp = true;
          buf.append(ric_);
          buf.append(oldold);
        }
        else if (oldold == cic_) // Check for duplicate cic
        {
          buf.append(cic_);
        }
        else if (oldold == ric_) // Check for duplicate ric
        {
          buf.append(ric_);
        }
        else // Just copy it normal
        {
          buf.append(oldold);
        }
        oldold = old;
        old = arr[i];
      }
      else if (inCompression)
      {
        if (arr[i] == old && arr[i] == oldold) // Still repeating?
        {
          ++count;
          oldold = old;
          old = arr[i];
        }
        else // Not repeating anymore
        {
          inCompression = false;
          char c = (char)count;
          if (count == 0x0008) c = '\b';
          else if (count == 0x0009) c = '\t';
          //else if(count == 0x000A) c = '\r'; // Had trouble with 0x0A and 0x0D getting messed up.
          else if (count == 0x000A) c = '\n'; // Had trouble with 0x0A and 0x0D getting messed up.
          else if (count == 0x000C) c = '\f';
          //else if(count == 0x000D) c = '\n';
          else if (count == 0x000D) c = '\r';
          else if (count == 0x0022) c = '\"';
          else if (count == 0x0027) c = '\'';
          else if (count == 0x005C) c = '\\';
          buf.append(c);
          oldold = arr[i++]; // yes this is right... think about it.
          old = arr[i];
        }
      }
      else
      { // must be in ramp
        if (arr[i] == old+1 && arr[i] == oldold+2)
        {
          oldold = old;
          old = arr[i];
        }
        else
        {
          inRamp = false;
          buf.append(old);
          oldold = arr[i++];
          old = arr[i];
        }
      }
    }
    if (inCompression)
    {
      char c = (char)count;
      if (count == 0x0008) c = '\b';
      else if (count == 0x0009) c = '\t';
      //else if(count == 0x000A) c = '\r'; // Had trouble with 0x0A and 0x0D getting messed up.
      else if (count == 0x000A) c = '\n'; // Had trouble with 0x0A and 0x0D getting messed up.
      else if (count == 0x000C) c = '\f';
      //else if(count == 0x000D) c = '\n';
      else if (count == 0x000D) c = '\r';
      else if (count == 0x0022) c = '\"';
      else if (count == 0x0027) c = '\'';
      else if (count == 0x005C) c = '\\';
      buf.append(c);
    }
    if (inRamp)
    {
      buf.append(old);
    }

    return buf.toString().toCharArray();
  }

  static void writeHeader(FileWriter f, int ccsid) throws Exception
  {
    f.write("///////////////////////////////////////////////////////////////////////////////\n");
    f.write("//\n");
    f.write("// JTOpen (IBM Toolbox for Java - OSS version)\n");
    f.write("//\n");
    f.write("// Filename:  CCSID"+ccsid+".java\n");
    f.write("//\n");
    f.write("// The source code contained herein is licensed under the IBM Public License\n");
    f.write("// Version 1.0, which has been approved by the Open Source Initiative.\n");
    f.write("// Copyright (C) 2011-2012 International Business Machines Corporation and\n");
    f.write("// others.  All rights reserved.\n");
    f.write("//\n");
    f.write("///////////////////////////////////////////////////////////////////////////////\n\n");
  }

}

