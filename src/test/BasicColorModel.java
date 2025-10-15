///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  BasicColorModel.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


/**
This class is a simple 16 color index color scheme.  The colors are:
0  Transparent
1  Red
2  Orange
3  Yellow
4  Green
5  Blue
6  Indigo
7  Violet
8  Black
9  Gray0
10 Gray1
11 Gray2
12 Gray3
13 Gray4
14 Gray5
15 White
**/
public class BasicColorModel extends java.awt.image.IndexColorModel
{
  private static byte[] colorMap = new byte[64];

  static
  {
    // Initialize the color map with 16 basic colors.  There are four
    // components per color: red, green, blue, and transparency.
    // Transparent 
    colorMap[0] = 0;
    colorMap[1] = 0;
    colorMap[2] = 0;
    colorMap[3] = 0;
    // Red
    colorMap[4] = (byte) 255;
    colorMap[5] = 0;
    colorMap[6] = 0;
    colorMap[7] = (byte) 255;
    // Orange
    colorMap[8] = (byte) 255;
    colorMap[9] = (byte) 200;
    colorMap[10] = 0;
    colorMap[11] = (byte) 255;
    // Yellow
    colorMap[12] = (byte) 255;
    colorMap[13] = (byte) 255;
    colorMap[14] = 0;
    colorMap[15] = (byte) 255;
    // Green
    colorMap[16] = 0;
    colorMap[17] = (byte) 255;
    colorMap[18] = 0;
    colorMap[19] = (byte) 255;
    // Blue
    colorMap[20] = 0;
    colorMap[21] = 0;
    colorMap[22] = (byte) 255;
    colorMap[23] = (byte) 255;
    // Indigo
    colorMap[24] = (byte) 128;
    colorMap[25] = 0;
    colorMap[26] = (byte) 255;
    colorMap[27] = (byte) 255;
    // Violet
    colorMap[28] = (byte) 255;
    colorMap[29] = 0;
    colorMap[30] = (byte) 255;
    colorMap[31] = (byte) 255;
    // Black
    colorMap[32] = 0;
    colorMap[33] = 0;
    colorMap[34] = 0;
    colorMap[35] = (byte) 255;
    // Gray0 (very dark)
    colorMap[36] = 0;
    colorMap[37] = 0;
    colorMap[38] = 0;
    colorMap[39] = (byte) 255;
    // Gray1
    colorMap[40] = 64;
    colorMap[41] = 64;
    colorMap[42] = 64;
    colorMap[43] = (byte) 255;
    // Gray2
    colorMap[44] = (byte) 128;
    colorMap[45] = (byte) 128;
    colorMap[46] = (byte) 128;
    colorMap[47] = (byte) 255;
    // Gray3
    colorMap[48] = (byte) 160;
    colorMap[49] = (byte) 160;
    colorMap[50] = (byte) 160;
    colorMap[51] = (byte) 255;
    // Gray4
    colorMap[52] = (byte) 192;
    colorMap[53] = (byte) 192;
    colorMap[54] = (byte) 192;
    colorMap[55] = (byte) 255;
    // Gray5 (very light gray)
    colorMap[56] = (byte) 224;
    colorMap[57] = (byte) 224;
    colorMap[58] = (byte) 224;
    colorMap[59] = (byte) 255;
    // White
    colorMap[60] = (byte) 255;
    colorMap[61] = (byte) 255;
    colorMap[62] = (byte) 255;
    colorMap[63] = (byte) 255;
  }

  public BasicColorModel()
  {
    super(4, 16, colorMap, 0, true, 0);
  }

}




