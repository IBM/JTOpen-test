///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvConverterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.io.CharConversionException;
import java.util.Locale;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
  Testcase ConvConverterTestcase.
 **/
public class ConvConverterTestcase extends Testcase
{
  /**
    Call new Converter()
   **/
  public void Var001()
  {
    if (isApplet_)
    {
      notApplicable("Cannot set Locale inside a browser.");
      return;
    }
    Locale saveDefault = Locale.getDefault();
    try
    {
      Locale.setDefault(new Locale("ar", ""));
      Converter ret = new Converter();
      succeeded(ret.toString());
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    finally
    {
      Locale.setDefault(saveDefault);
    }
  }

  /**
    Call new Converter() twice, change default locale inbetween, verify different object
   **/
  public void Var002()
  {
    if (isApplet_)
    {
      notApplicable("Cannot set Locale inside a browser.");
      return;
    }
    Locale saveDefault = Locale.getDefault();
    try
    {
      Locale.setDefault(new Locale("ca", ""));
      Converter ret1 = new Converter();
      Locale.setDefault(new Locale("cs", ""));
      Converter ret2 = new Converter();
      if (ret1 != ret2)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    finally
    {
      Locale.setDefault(saveDefault);
    }
  }

  /**
    Call new Converter(String)
   **/
  public void Var003()
  {
    try
    {
      Converter ret = new Converter("Cp273");
      succeeded(ret.toString());
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call new Converter(String), with encoding that does not exist.
   **/
  public void Var004()
  {
    try
    {
      Converter ret = new Converter("blah");
      failed("Expected exception did not occur."+ret.toString());
    }
    catch (Exception e)
    {
      if (!exceptionIs(e, "UnsupportedEncodingException"))
      {
        failed(e, "Incorrect exception info.");
      }
      else
      {
        succeeded();
      }
    }
  }

  /**
    Call new Converter(String) with existing encoding that isn't supported.
   **/
  public void Var005()
  {
    if (isApplet_)
    {
      notApplicable("Cp942C does not exist in IE.");
      return;
    }
    try
    {
        // Java should be able to handle Cp942C, and we punt to Java if we don't have a table.
      Converter ret = new Converter("Cp942C");
      succeeded(ret.toString());
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call new Converter(int) with existing ccsid.
   **/
  public void Var006()
  {
    try
    {
      Converter ret = new Converter(875);
      succeeded(ret.toString());
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call new Converter(int, AS400) with existing ccsid and null system.
   **/
  public void Var007()
  {
    try
    {
      Converter ret = new Converter(875, null);
      succeeded(ret.toString());
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
     Call new Converter(int, AS400) with existing ccsid and system.
   **/
  public void Var008()
  {
    try
    {
      Converter ret1 = new Converter(939, systemObject_);
      succeeded(ret1.toString());
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call byteArrayToString(byte[])
   **/
  public void Var009()
  {
    try
    {
      byte[] data = {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4};
      Converter conv = new Converter(37);
      String ret = conv.byteArrayToString(data);
      if (ret.compareTo("1234") == 0)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call byteArrayToString(byte[], int)
   **/
  public void Var010()
  {
    try
    {
      byte[] data = {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4};
      Converter conv = new Converter(37);
      String ret = conv.byteArrayToString(data, 0);
      if (ret.compareTo("1234") == 0)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call byteArrayToString(byte[], int, int)
   **/
  public void Var011()
  {
    try
    {
      byte[] data = {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4};
      Converter conv = new Converter(37);
      String ret = conv.byteArrayToString(data, 0, 4);
      if (ret.compareTo("1234") == 0)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call getCcsid()
   **/
  public void Var012()
  {
    try
    {
      Converter conv = new Converter("Cp037");
      int ret = conv.getCcsid();
      if (ret == 37)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call getEncoding()
   **/
  public void Var013()
  {
    try
    {
      Converter conv = new Converter(37);
      String ret = conv.getEncoding();
      if (ret.compareTo("Cp037") == 0)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
    Call stringToByteArray(String)
   **/
  public void Var014()
  {
    try
    {
      Converter conv = new Converter(37);
      byte[] data = conv.stringToByteArray("1234");
      if (data.length == 4 &&
          data[0] == (byte)0xF1 &&
          data[1] == (byte)0xF2 &&
          data[2] == (byte)0xF3 &&
          data[3] == (byte)0xF4)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }

  /**
    Call stringToByteArray(String, byte[])
   **/
  public void Var015()
  {
    try
    {
      Converter conv = new Converter(37);
      byte[] data = new byte[4];
      conv.stringToByteArray("1234", data);
      if (data.length == 4 &&
          data[0] == (byte)0xF1 &&
          data[1] == (byte)0xF2 &&
          data[2] == (byte)0xF3 &&
          data[3] == (byte)0xF4)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }

  /**
    Call stringToByteArray(String, byte[]), string to long
   **/
  public void Var016()
  {
    byte[] data = new byte[3];
    try
    {
      Converter conv = new Converter(37);
      conv.stringToByteArray("1234", data);
      failed("no exception thrown");
    }
    catch (CharConversionException e)
    {
      if (data.length == 3 &&
          data[0] == (byte)0xF1 &&
          data[1] == (byte)0xF2 &&
          data[2] == (byte)0xF3)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }

  /**
    Call stringToByteArray(String, byte[], int)
   **/
  public void Var017()
  {
    try
    {
      Converter conv = new Converter(37);
      byte[] data = new byte[4];
      conv.stringToByteArray("1234", data, 0);
      if (data.length == 4 &&
          data[0] == (byte)0xF1 &&
          data[1] == (byte)0xF2 &&
          data[2] == (byte)0xF3 &&
          data[3] == (byte)0xF4)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }

  /**
    Call stringToByteArray(String, byte[], int), string to long
   **/
  public void Var018()
  {
    byte[] data = new byte[4];
    try
    {
      Converter conv = new Converter(37);
      conv.stringToByteArray("1234", data, 1);
      failed("no exception thrown");
    }
    catch (CharConversionException e)
    {
      if (data.length == 4 &&
          data[1] == (byte)0xF1 &&
          data[2] == (byte)0xF2 &&
          data[3] == (byte)0xF3)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }

  /**
    Call stringToByteArray(String, byte[], int, int)
   **/
  public void Var019()
  {
    try
    {
      Converter conv = new Converter(37);
      byte[] data = new byte[4];
      conv.stringToByteArray("1234", data, 0, 4);
      if (data.length == 4 &&
          data[0] == (byte)0xF1 &&
          data[1] == (byte)0xF2 &&
          data[2] == (byte)0xF3 &&
          data[3] == (byte)0xF4)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }

  /**
    Call stringToByteArray(String, byte[], int, int), string to long
   **/
  public void Var020()
  {
    byte[] data = new byte[4];
    try
    {
      Converter conv = new Converter(37);
      conv.stringToByteArray("1234", data, 0, 2);
      failed("no exception thrown");
    }
    catch (CharConversionException e)
    {
      if (data.length == 4 &&
          data[0] == (byte)0xF1 &&
          data[1] == (byte)0xF2)
      {
        succeeded();
      }
      else
      {
        failed("Unexpected value");
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception");
    }
  }
}

