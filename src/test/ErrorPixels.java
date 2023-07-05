///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ErrorPixels.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import java.awt.image.ColorModel;


class ErrorPixels
{
/**
The color model used for this image.
**/
  static ColorModel colorModel = new BasicColorModel();

  private final static long[] data_ =
  {
    0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,
    0x000000000000000fL,0xfffffffff0000000L,0x0000000000000000L,0x00000ff111111111L,0x1ff0000000000000L,
    0x00000000000ff111L,0x11111111111ff000L,0x0000000000000000L,0x0ff1111111111111L,0x11111ff000000000L,
    0x00000000f1111111L,0x111111111111111fL,0x000000000000000fL,0x1111111111111111L,0x11111111f0000000L,
    0x000000f111111111L,0x1111111111111111L,0x1f00000000000f11L,0x1111111111111111L,0x111f111111f00000L,
    0x00000f11111ff111L,0x1111111111fff111L,0x11f000000000f111L,0x11ffff1111111111L,0x1fffff11111f0000L,
    0x0000f1111ffffff1L,0x11111111ffffff11L,0x111f0000000f1111L,0x11ffffff1111111fL,0xfffff1111111f000L,
    0x000f1111111fffffL,0xf11111ffffff1111L,0x1111f00000f11111L,0x1111ffffff111fffL,0xfff1111111111f00L,
    0x00f1111111111fffL,0xfff1ffffff111111L,0x11111f0000f11111L,0x111111ffffffffffL,0xf111111111111f00L,
    0x00f111111111111fL,0xffffffff11111111L,0x11111f0000f11111L,0x11111111fffffff1L,0x1111111111111f00L,
    0x00f1111111111111L,0xfffffff111111111L,0x11111f0000f11111L,0x1111111fffffffffL,0x1111111111111f00L,
    0x00f11111111111ffL,0xfffffffff1111111L,0x11111f0000f11111L,0x11111ffffff1ffffL,0xff11111111111f00L,
    0x00f111111111ffffL,0xff111ffffff11111L,0x11111f00000f1111L,0x111ffffff11111ffL,0xffff11111111f000L,
    0x000f111111ffffffL,0x1111111ffffff111L,0x1111f0000000f111L,0x1ffffff111111111L,0xffffff11111f0000L,
    0x0000f11111ffff11L,0x111111111fffff11L,0x111f000000000f11L,0x111ff11111111111L,0x11fff11111f00000L,
    0x00000f1111111111L,0x11111111111f1111L,0x11f00000000000f1L,0x1111111111111111L,0x111111111f000000L,
    0x0000000f11111111L,0x1111111111111111L,0xf000000000000000L,0xf111111111111111L,0x1111111f00000000L,
    0x000000000ff11111L,0x1111111111111ff0L,0x0000000000000000L,0x000ff11111111111L,0x111ff00000000000L,
    0x0000000000000ff1L,0x111111111ff00000L,0x0000000000000000L,0x0000000fffffffffL,0xf000000000000000L,
    0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L
  };

/**
The height of the image in pixels.
**/
  final static int height = 40;

/**
The width of the image in pixels.
**/
  final static int width = 40;


/**
Returns the pixel data.
**/
  static byte[] getPixels()
  {
    byte[] data = new byte[ErrorPixels.data_.length * 16];
    for (int i = 0, j = 0; i < ErrorPixels.data_.length; i++, j += 16)
    {
      long longWord = ErrorPixels.data_[i];
      for (int l = 15; l >= 0; l--, longWord >>>= 4)
      {
        data[j+l] = (byte) (longWord & 0xf);
      }
    }

    return data;
  }

}



