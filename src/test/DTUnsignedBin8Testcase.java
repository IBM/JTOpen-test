///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTUnsignedBin8Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400UnsignedBin8;
import java.math.BigInteger;

/**
  Testcase DTUnsignedBin8Testcase.
 **/
public class DTUnsignedBin8Testcase extends Testcase
{

  // A BigInteger value that's too large to fit into 8 bytes.
  static final BigInteger TOO_LARGE_BIG_INTEGER = new BigInteger("10000000000000000", 16);  // base-16 number: 0xFFFFFFFFFFFF + 1

  static final BigInteger toBigInteger(int value)
  {
    return new BigInteger(Integer.toString(value));
  }

  static final BigInteger toBigInteger(long value)
  {
    return new BigInteger(Long.toString(value));
  }

  static final BigInteger hexToBigInteger(String value)
  {
    return new BigInteger(value,16);
  }

    /**
      Test: Construct an AS400UnsignedBin8 object.
      Result: No exception should be thrown.
     **/
    public void Var001()
    {
	try
	{
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    succeeded();
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      Test: Call clone
      Result: Cloned object returned.
     **/
    public void Var002()
    {
	try
	{
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    AS400UnsignedBin8 clone = (AS400UnsignedBin8)conv.clone();
	    if (clone != null && clone != conv)
	    {
		succeeded();
	    }
	    else
	    {
		failed("Incorrect results");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      Test: Call getByteLength
      Result: The int value 8 returned
     **/
    public void Var003()
    {
	try
	{
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    int ret = conv.getByteLength();
	    assertCondition(ret == 8);
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call getDefaultValue
      Result: A BigInteger object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
	try
	{
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    Object ret = conv.getDefaultValue();

	    if (ret instanceof BigInteger)
	    {
		BigInteger lRet = (BigInteger)ret;
		if (lRet != BigInteger.ZERO)
		{
		    failed("Incorrect return value");
		}
		else
		{
		    byte[] data = conv.toBytes(ret);
		    if (data.length == 8 &&
			data[0] == 0 &&
			data[1] == 0 &&
			data[2] == 0 &&
			data[3] == 0 &&
			data[4] == 0 &&
			data[5] == 0 &&
			data[6] == 0 &&
			data[7] == 0)
		    {
			succeeded();
		    }
		    else
		    {
			failed("Unexpected value");
		    }
		}
	    }
	    else
	    {
		failed("Incorrect return value");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call toBytes(Object) with valid javaValue parameter.
      Result: Eight-element byte array returned with a valid value.
     **/
    public void Var005()
    {
	try
	{
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    byte[] data = conv.toBytes(toBigInteger(2165379345l));
	    if (data.length == 8 &&
		data[0] == (byte)0x00 &&
		data[1] == (byte)0x00 &&
		data[2] == (byte)0x00 &&
		data[3] == (byte)0x00 &&
		data[4] == (byte)0x81 &&
		data[5] == (byte)0x11 &&
		data[6] == (byte)0x11 &&
		data[7] == (byte)0x11)
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
      Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of BigInteger.
      Result: ClassCastException thrown.
     **/
    public void Var006()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    byte[] ret = conv.toBytes(new Object());
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ClassCastException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object) with invalid parameters: javaValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var007()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    byte[] ret = conv.toBytes(toBigInteger(-10));
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object) with invalid parameters: javaValue contains a value too large to be stored in eight bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var008()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    byte[] ret = conv.toBytes(TOO_LARGE_BIG_INTEGER);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object) with invalid parameters: javaValue is null.
      Result: NullPointerException thrown.
     **/
    public void Var009()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    byte[] ret = conv.toBytes(null);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long) with valid longValue parameter.
      Result: Eight-element byte array returned with a valid value.
     **/
    public void Var010()
    {
	try
	{
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    byte[] data = conv.toBytes(Long.MAX_VALUE);
         
	    if (data.length == 8 &&
		data[0] == (byte)0x7F &&
		data[1] == (byte)0xFF &&
		data[2] == (byte)0xFF &&
		data[3] == (byte)0xFF &&
		data[4] == (byte)0xFF &&
		data[5] == (byte)0xFF &&
		data[6] == (byte)0xFF &&
		data[7] == (byte)0xFF)
	    {
		succeeded();
	    }
	    else
	    {
          //printByteArray("Value received:", data);
		failed("Unexpected value");
	    }
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call toBytes(long) with invalid parameters: longValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var011()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    byte[] ret = conv.toBytes(-10);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long) with invalid parameters: longValue contains a value too large to be stored in 8 bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var012()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    byte[] ret = conv.toBytes(TOO_LARGE_BIG_INTEGER);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with valid parameters.
      Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var013()
    {
	try
	{
	    BigInteger[] testValue =
	    {
		hexToBigInteger("00000000"),
		hexToBigInteger("00000080"),
		hexToBigInteger("00008000"),
		hexToBigInteger("00008080"),
		hexToBigInteger("00800000"),
		hexToBigInteger("00800080"),
		hexToBigInteger("00808000"),
		hexToBigInteger("00808080"),
		hexToBigInteger("80000000"),
		hexToBigInteger("80000080"),
		hexToBigInteger("80008000"),
		hexToBigInteger("80008080"),
		hexToBigInteger("80800000"),
		hexToBigInteger("80800080"),
		hexToBigInteger("80808000"),
		hexToBigInteger("80808080")
	    };

	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    byte[] data = new byte[8];
	    int i = 0;

	    for (int w = 0x00; w <= 0xFF; w+=0x80)
	    {
		for (int x = 0x00; x <= 0xFF; x+=0x80)
		{
		    for (int y = 0x00; y <= 0xFF; y+=0x80)
		    {
			for (int z = 0x00; z <= 0xFF; z+=0x80)
			{
			    int ret = conv.toBytes(testValue[i++], data);

			    if (ret != 8)
			    {
				valid = false;
			    }
			    if (data[0] != (byte)0 ||
				data[1] != (byte)0 ||
				data[2] != (byte)0 ||
				data[3] != (byte)0 ||
				data[4] != (byte)w ||
				data[5] != (byte)x ||
				data[6] != (byte)y ||
				data[7] != (byte)z)
			    {
				valid = false;
			    }
			}
		    }
		}
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var014()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(0), new byte[2]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of BigInteger.
      Result: ClassCastException thrown.
     **/
    public void Var015()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(new Object(), new byte[10]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ClassCastException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var016()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(-10), new byte[10]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue contains a value too large to be stored in eight bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var017()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(TOO_LARGE_BIG_INTEGER, new byte[10]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is null.
      Result: NullPointerException thrown.
     **/
    public void Var018()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(null, new byte[10]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var019()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(0), null);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[]) with valid parameters.
      Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var020()
    {
	try
	{
	    BigInteger[] testValue =
	    {
		hexToBigInteger("00000000"),
		hexToBigInteger("00000080"),
		hexToBigInteger("00008000"),
		hexToBigInteger("00008080"),
		hexToBigInteger("00800000"),
		hexToBigInteger("00800080"),
		hexToBigInteger("00808000"),
		hexToBigInteger("00808080"),
		hexToBigInteger("80000000"),
		hexToBigInteger("80000080"),
		hexToBigInteger("80008000"),
		hexToBigInteger("80008080"),
		hexToBigInteger("80800000"),
		hexToBigInteger("80800080"),
		hexToBigInteger("80808000"),
		hexToBigInteger("80808080")
	    };

	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    byte[] data = new byte[8];
	    int i = 0;

	    for (int w = 0x00; w <= 0xFF; w+=0x80)
	    {
		for (int x = 0x00; x <= 0xFF; x+=0x80)
		{
		    for (int y = 0x00; y <= 0xFF; y+=0x80)
		    {
			for (int z = 0x00; z <= 0xFF; z+=0x80)
			{
			    int ret = conv.toBytes(testValue[i++], data);

			    if (ret != 8)
			    {
				valid = false;
			    }
			    if (data[0] != (byte)0 ||
				data[1] != (byte)0 ||
				data[2] != (byte)0 ||
				data[3] != (byte)0 ||
				data[4] != (byte)w ||
				data[5] != (byte)x ||
				data[6] != (byte)y ||
				data[7] != (byte)z)
			    {
				valid = false;
			    }
			}
		    }
		}
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call toBytes(long, byte[]) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var021()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(0, new byte[2]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[]) with invalid parameters: longValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var022()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(-10, new byte[10]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[]) with invalid parameters: longValue contains a value too large to be stored in eight bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var023()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(TOO_LARGE_BIG_INTEGER, new byte[10]);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[]) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var024()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(0, null);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
      Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var025()
    {
	try
	{
	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
         final int ARRAY_LENGTH = 14;
	    byte[] data = new byte[ARRAY_LENGTH];

	    for (int i = 0; i < ARRAY_LENGTH; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    int ret = conv.toBytes(toBigInteger(0), data, 0);
	    if (ret != 8)
	    {
		valid = false;
	    }
	    if (data[0] != 0 ||
		data[1] != 0 ||
		data[2] != 0 ||
		data[3] != 0 ||
		data[4] != 0 ||
		data[5] != 0 ||
		data[6] != 0 ||
		data[7] != 0)
	    {
		valid = false;
	    }
	    for (int i = 8; i < ARRAY_LENGTH; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < ARRAY_LENGTH; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(toBigInteger(0), data, 5);
	    if (ret != 8)
	    {
		valid = false;
	    }
	    for (int i = 0; i <= 4; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }
	    if (data[5] != 0 ||
		data[6] != 0 ||
		data[7] != 0 ||
		data[8] != 0 ||
		data[9] != 0 ||
		data[10] != 0 ||
		data[11] != 0 ||
		data[12] != 0)
	    {
		valid = false;
	    }
	    for (int i = 13; i < ARRAY_LENGTH; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < ARRAY_LENGTH; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(toBigInteger(0), data, 6);
	    if (ret != 8)
	    {
		valid = false;
	    }
	    for (int i = 0; i <= 5; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }
	    if (data[6] != 0 ||
		data[7] != 0 ||
		data[8] != 0 ||
		data[9] != 0 ||
		data[10] != 0 ||
		data[11] != 0 ||
		data[12] != 0 ||
		data[13] != 0)
	    {
		valid = false;
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(0), new byte[3], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: startingPoint too close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(0), new byte[5], 2);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: startingPoint negative number.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var028()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(0), new byte[10], -7);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of BigInteger.
      Result: ClassCastException thrown.
     **/
    public void Var029()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(new Object(), new byte[10], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ClassCastException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var030()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(-10), new byte[10], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue contains a value too large to be stored in eight bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var031()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(TOO_LARGE_BIG_INTEGER, new byte[10], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is null.
      Result: NullPointerException thrown.
     **/
    public void Var032()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(null, new byte[10], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var033()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(toBigInteger(0), null, 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
      Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var034()
    {
	try
	{
	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
         final int ARRAY_LENGTH = 14;
	    byte[] data = new byte[ARRAY_LENGTH];

	    for (int i = 0; i < ARRAY_LENGTH; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    int ret = conv.toBytes(0, data, 0);
	    if (ret != 8)
	    {
		valid = false;
	    }
	    if (data[0] != 0 ||
		data[1] != 0 ||
		data[2] != 0 ||
		data[3] != 0 ||
		data[4] != 0 ||
		data[5] != 0 ||
		data[6] != 0 ||
		data[7] != 0)
	    {
		valid = false;
	    }
	    for (int i = 8; i < ARRAY_LENGTH; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < ARRAY_LENGTH; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(0, data, 5);
	    if (ret != 8)
	    {
		valid = false;
	    }
	    for (int i = 0; i <= 4; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }
	    if (data[5] != 0 ||
		data[6] != 0 ||
		data[7] != 0 ||
		data[8] != 0 ||
		data[9] != 0 ||
		data[10] != 0 ||
		data[11] != 0 ||
		data[12] != 0)
	    {
		valid = false;
	    }
	    for (int i = 13; i < ARRAY_LENGTH; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < ARRAY_LENGTH; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(0, data, 6);
	    if (ret != 8)
	    {
		valid = false;
	    }
	    for (int i = 0; i <= 5; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }
	    if (data[6] != 0 ||
		data[7] != 0 ||
		data[8] != 0 ||
		data[9] != 0 ||
		data[10] != 0 ||
		data[11] != 0 ||
		data[12] != 0 ||
		data[13] != 0)
	    {
		valid = false;
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var035()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(0, new byte[2], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with invalid parameters: startingPoint too close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var036()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(0, new byte[5], 2);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with invalid parameters: startingPoint negative number.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var037()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(0, new byte[10], -7);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with invalid parameters: longValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var038()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(-10, new byte[10], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with invalid parameters: longValue contains a value too large to be stored in eight bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var039()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(TOO_LARGE_BIG_INTEGER, new byte[10], 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ExtendedIllegalArgumentException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBytes(long, byte[], int) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var040()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    int ret = conv.toBytes(0, null, 0);
	    failed("No exception thrown.");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toObject(byte[]) with valid as400Value parameters.
      Result:  object return with valid value.
     **/
    public void Var041()
    {
	try
	{
	    BigInteger[] expectedValue =
	    {
		hexToBigInteger("0000000000000000"),
		hexToBigInteger("0000008000000000"),
		hexToBigInteger("0000800000000000"),
		hexToBigInteger("0000808000000000"),
		hexToBigInteger("0080000000000000"),
		hexToBigInteger("0080008000000000"),
		hexToBigInteger("0080800000000000"),
		hexToBigInteger("0080808000000000"),
		hexToBigInteger("8000000000000000"),
		hexToBigInteger("8000008000000000"),
		hexToBigInteger("8000800000000000"),
		hexToBigInteger("8000808000000000"),
		hexToBigInteger("8080000000000000"),
		hexToBigInteger("8080008000000000"),
		hexToBigInteger("8080800000000000"),
		hexToBigInteger("8080808000000000")
	    };

	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    byte[] data = new byte[8];
	    int i = 0;

	    for (int w = 0x00; w <= 0xFF; w+=0x80)
	    {
		data[0] = (byte)w;
		for (int x = 0x00; x <= 0xFF; x+=0x80)
		{
		    data[1] = (byte)x;
		    for (int y = 0x00; y <= 0xFF; y+=0x80)
		    {
			data[2] = (byte)y;
			for (int z = 0x00; z <= 0xFF; z+=0x80)
			{
			    data[3] = (byte)z;

			    Object obj = conv.toObject(data);

			    if (obj instanceof BigInteger)
			    {
				BigInteger lvalue = (BigInteger)obj;
				///long dataValue = lvalue.longValue();
				if (!lvalue.equals(expectedValue[i++]))
				{
                     System.out.println("Expected: " + expectedValue[i-1].toString());
                     System.out.println("Received: " + lvalue.toString());
				    valid = false;
				}
			    }
			    else
			    {
				valid = false;
			    }
			}
		    }
		}
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      Test: Call toObject(byte[]) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var042()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    Object obj = conv.toObject(new byte[2]);
	    failed("no exception thrown for too small array");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toObject(byte[]) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var043()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    Object obj = conv.toObject(null);
	    failed("no exception thrown for null pointer");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toObject(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
      Result: BigInteger object return with valid value.
     **/
    public void Var044()
    {
	try
	{
	    byte[] data =
	    {
		(byte)0xFF,
		(byte)0xFE,
		(byte)0x01,
		(byte)0x02,
		(byte)0x03,
		(byte)0x82,
		(byte)0xF3,
		(byte)0x54,
		(byte)0x0F,
		(byte)0xA1
	    };

         byte[] subarray0 = new byte[9];
         byte[] subarray1 = new byte[9];
         byte[] subarray2 = new byte[9];
         System.arraycopy(data, 0, subarray0, 1, 8); // need 0 as leading byte, to prevent sign extension
         System.arraycopy(data, 1, subarray1, 1, 8);
         System.arraycopy(data, 2, subarray2, 1, 8);
	    BigInteger[] expectedValue =
	    {
          new BigInteger(subarray0),
          new BigInteger(subarray1),
          new BigInteger(subarray2)
	    };

	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();

	    for (int i=0; i<3; ++i)
	    {
		Object obj = conv.toObject(data, i);

		if (obj instanceof BigInteger)
		{
		    BigInteger lvalue = (BigInteger)obj;
		    ///long dataValue = lvalue.longValue();
		    if (!lvalue.equals(expectedValue[i]))
		    {
			valid = false;
              System.out.println("Expected: " + expectedValue[i].toString());
              System.out.println("Received: " + lvalue.toString());
		    }
		}
		else
		{
		    valid = false;
		}
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      Test: Call toObject(byte[], int) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var045()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    Object obj = conv.toObject(new byte[2], 0);
	    failed("no exception thrown for too small array");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toObject(byte[], int) with invalid parameters: startingPoint too close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var046()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    Object obj = conv.toObject(new byte[7], 4);
	    failed("no exception thrown for not enough space");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toObject(byte[], int) with invalid parameters: startingPoint negative number.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var047()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    Object obj = conv.toObject(new byte[4], -5);
	    failed("no exception thrown for negative value");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toObject(byte[], int) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var048()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    Object obj = conv.toObject(null, 0);
	    failed("no exception thrown for null pointer");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBigInteger(byte[]) with valid as400Value parameters.
      Result:  BigInteger returned with valid value.
     **/
    public void Var049()
    {
	try
	{
	    BigInteger[] expectedValue =
	    {
		hexToBigInteger("0000000000000000"),
		hexToBigInteger("0000008000000000"),
		hexToBigInteger("0000800000000000"),
		hexToBigInteger("0000808000000000"),
		hexToBigInteger("0080000000000000"),
		hexToBigInteger("0080008000000000"),
		hexToBigInteger("0080800000000000"),
		hexToBigInteger("0080808000000000"),
		hexToBigInteger("8000000000000000"),
		hexToBigInteger("8000008000000000"),
		hexToBigInteger("8000800000000000"),
		hexToBigInteger("8000808000000000"),
		hexToBigInteger("8080000000000000"),
		hexToBigInteger("8080008000000000"),
		hexToBigInteger("8080800000000000"),
		hexToBigInteger("8080808000000000")
	    };

	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	    byte[] data = new byte[8];
	    int i = 0;

	    for (int w = 0x00; w <= 0xFF; w+=0x80)
	    {
		data[0] = (byte)w;
		for (int x = 0x00; x <= 0xFF; x+=0x80)
		{
		    data[1] = (byte)x;
		    for (int y = 0x00; y <= 0xFF; y+=0x80)
		    {
			data[2] = (byte)y;
			for (int z = 0x00; z <= 0xFF; z+=0x80)
			{
			    data[3] = (byte)z;

			    BigInteger dataValue = conv.toBigInteger(data);

			    if (!dataValue.equals(expectedValue[i++]))
			    {
				valid = false;
			    }
			}
		    }
		}
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      Test: Call toBigInteger(byte[]) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var050()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    BigInteger l = conv.toBigInteger(new byte[4]);
	    failed("no exception thrown for too small array");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBigInteger(byte[]) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var051()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    BigInteger l = conv.toBigInteger(null);
	    failed("no exception thrown for null pointer");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBigInteger(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
      Result: BigInteger returned with valid value.
     **/
    public void Var052()
    {
	try
	{
	    byte[] data =
	    {
		(byte)0xFF,
		(byte)0xFE,
		(byte)0x01,
		(byte)0x02,
		(byte)0x03,
		(byte)0x82,
		(byte)0xF3,
		(byte)0x54,
		(byte)0x0F,
		(byte)0xA1
	    };

         byte[] subarray0 = new byte[9];
         byte[] subarray1 = new byte[9];
         byte[] subarray2 = new byte[9];
         System.arraycopy(data, 0, subarray0, 1, 8); // need 0 as leading byte, to prevent sign extension
         System.arraycopy(data, 1, subarray1, 1, 8);
         System.arraycopy(data, 2, subarray2, 1, 8);
	    BigInteger[] expectedValue =
	    {
          new BigInteger(subarray0),
          new BigInteger(subarray1),
          new BigInteger(subarray2)
	    };

	    boolean valid = true;
	    AS400UnsignedBin8 conv = new AS400UnsignedBin8();

	    for (int i=0; i<3; ++i)
	    {
		Object obj = conv.toBigInteger(data, i);

		if (obj instanceof BigInteger)
		{
		    BigInteger lvalue = (BigInteger)obj;
		    ///long dataValue = lvalue.longValue();
		    if (!lvalue.equals(expectedValue[i]))
		    {
			valid = false;
              System.out.println("Expected: " + expectedValue[i].toString());
              System.out.println("Received: " + lvalue.toString());
		    }
		}
		else
		{
		    valid = false;
		}
	    }

	    assertCondition(valid);
	}
	catch (Exception e)
	{
	    failed(e, "Unexpected exception.");
	}
    }

    /**
      Test: Call toBigInteger(byte[], int) with invalid parameters: as400Value too small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var053()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    BigInteger l = conv.toBigInteger(new byte[2], 0);
	    failed("no exception thrown for too small array");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBigInteger(byte[], int) with invalid parameters: startingPoint too close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var054()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    BigInteger l = conv.toBigInteger(new byte[7], 4);
	    failed("no exception thrown for not enough space");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBigInteger(byte[], int) with invalid parameters: startingPoint negative number.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var055()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    BigInteger l = conv.toBigInteger(new byte[4], -5);
	    failed("no exception thrown for negative value");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "ArrayIndexOutOfBoundsException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }

    /**
      Test: Call toBigInteger(byte[], int) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var056()
    {
	AS400UnsignedBin8 conv = new AS400UnsignedBin8();
	try
	{
	    BigInteger l = conv.toBigInteger(null, 0);
	    failed("no exception thrown for null pointer");
	}
	catch (Exception e)
	{
	    if (exceptionIs(e, "NullPointerException"))
	    {
		succeeded();
	    }
	    else
	    {
		failed(e, "Incorrect exception");
	    }
	}
    }
}



