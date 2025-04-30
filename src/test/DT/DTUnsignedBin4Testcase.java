///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTUnsignedBin4Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400UnsignedBin4;

import test.Testcase;

/**
  Testcase DTUnsignedBin4Testcase.
 **/
public class DTUnsignedBin4Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTUnsignedBin4Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
      Test: Construct an AS400UnsignedBin4 object.
      Result: No exception should be thrown.
     **/
    public void Var001()
    {
	try
	{
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    assertCondition(true, "conv="+conv); 
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
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    AS400UnsignedBin4 clone = (AS400UnsignedBin4)conv.clone();
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
      Result: The int value 4 returned
     **/
    public void Var003()
    {
	try
	{
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    int ret = conv.getByteLength();
	    assertCondition(ret == 4);
	}
	catch (Exception e)
	{
	    failed(e, "Incorrect exception");
	}
    }

    /**
      Test: Call getDefaultValue
      Result: A Long object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
	try
	{
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    Object ret = conv.getDefaultValue();

	    if (ret instanceof Long)
	    {
		Long lRet = (Long)ret;
		if (lRet.longValue() != 0)
		{
		    failed("Incorrect return value");
		}
		else
		{
		    byte[] data = conv.toBytes(ret);
		    if (data.length == 4 &&
			data[0] == 0 &&
			data[1] == 0 &&
			data[2] == 0 &&
			data[3] == 0)
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
      Result: Four element byte array returned with a valid value.
     **/
    public void Var005()
    {
	try
	{
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = conv.toBytes(new Long(2165379345l));
	    if (data.length == 4 &&
		data[0] == (byte)0x81 &&
		data[1] == (byte)0x11 &&
		data[2] == (byte)0x11 &&
		data[3] == (byte)0x11)
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
      Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Long.
      Result: ClassCastException thrown.
     **/
    public void Var006()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    byte[] ret = conv.toBytes(new Object());
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    byte[] ret = conv.toBytes(new Long(-10));
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(Object) with invalid parameters: javaValue contains a value to large to be stored in four bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var008()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    byte[] ret = conv.toBytes(new Long(0x100000000l));
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    byte[] ret = conv.toBytes(null);
	    failed("Did not throw exception. ret="+ret);
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
      Result: Four element byte array returned with a valid value.
     **/
    public void Var010()
    {
	try
	{
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = conv.toBytes(2165379345l);
	    if (data.length == 4 &&
		data[0] == (byte)0x81 &&
		data[1] == (byte)0x11 &&
		data[2] == (byte)0x11 &&
		data[3] == (byte)0x11)
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
      Test: Call toBytes(long) with invalid parameters: longValue contains a negative value.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var011()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    byte[] ret = conv.toBytes(-10);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(long) with invalid parameters: longValue contains a value to large to be stored in four bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var012()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    byte[] ret = conv.toBytes(0x100000000l);
	    failed("Did not throw exception. ret="+ret);
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
      Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var013()
    {
	try
	{
	    long[] testValue =
	    {
		0x00000000l,
		0x00000080l,
		0x00008000l,
		0x00008080l,
		0x00800000l,
		0x00800080l,
		0x00808000l,
		0x00808080l,
		0x80000000l,
		0x80000080l,
		0x80008000l,
		0x80008080l,
		0x80800000l,
		0x80800080l,
		0x80808000l,
		0x80808080l
	    };

	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = new byte[4];
	    int i = 0;

	    for (int w = 0x00; w <= 0xFF; w+=0x80)
	    {
		for (int x = 0x00; x <= 0xFF; x+=0x80)
		{
		    for (int y = 0x00; y <= 0xFF; y+=0x80)
		    {
			for (int z = 0x00; z <= 0xFF; z+=0x80)
			{
			    int ret = conv.toBytes(new Long(testValue[i++]), data);

			    if (ret != 4)
			    {
				valid = false;
			    }
			    if (data[0] != (byte)w ||
				data[1] != (byte)x ||
				data[2] != (byte)y ||
				data[3] != (byte)z)
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
      Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var014()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0), new byte[2]);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Long.
      Result: ClassCastException thrown.
     **/
    public void Var015()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Object(), new byte[10]);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(-10), new byte[10]);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue contains a value to large to be stored in four bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var017()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0x100000000l), new byte[10]);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(null, new byte[10]);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0), null);
	    failed("Did not throw exception. ret="+ret);
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
      Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var020()
    {
	try
	{
	    long[] testValue =
	    {
		0x00000000l,
		0x00000080l,
		0x00008000l,
		0x00008080l,
		0x00800000l,
		0x00800080l,
		0x00808000l,
		0x00808080l,
		0x80000000l,
		0x80000080l,
		0x80008000l,
		0x80008080l,
		0x80800000l,
		0x80800080l,
		0x80808000l,
		0x80808080l
	    };

	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = new byte[4];
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

			    if (ret != 4)
			    {
				valid = false;
			    }
			    if (data[0] != (byte)w ||
				data[1] != (byte)x ||
				data[2] != (byte)y ||
				data[3] != (byte)z)
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
      Test: Call toBytes(long, byte[]) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var021()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0, new byte[2]);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(-10, new byte[10]);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(long, byte[]) with invalid parameters: longValue contains a value to large to be stored in four bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var023()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0x100000000l, new byte[10]);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0, null);
	    failed("Did not throw exception. ret="+ret);
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
      Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var025()
    {
	try
	{
	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = new byte[10];

	    for (int i = 0; i < 10; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    int ret = conv.toBytes(new Long(0), data, 0);
	    if (ret != 4)
	    {
		valid = false;
	    }
	    if (data[0] != 0 ||
		data[1] != 0 ||
		data[2] != 0 ||
		data[3] != 0)
	    {
		valid = false;
	    }
	    for (int i = 4; i < 10; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < 10; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(new Long(0), data, 5);
	    if (ret != 4)
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
		data[8] != 0)
	    {
		valid = false;
	    }
	    for (int i = 9; i < 10; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < 10; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(new Long(0), data, 6);
	    if (ret != 4)
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
		data[9] != 0)
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
      Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0), new byte[3], 0);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(Object, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0), new byte[5], 2);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0), new byte[10], -7);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Long.
      Result: ClassCastException thrown.
     **/
    public void Var029()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Object(), new byte[10], 0);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(-10), new byte[10], 0);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue contains a value to large to be stored in four bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var031()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0x100000000l), new byte[10], 0);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(null, new byte[10], 0);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(new Long(0), null, 0);
	    failed("Did not throw exception. ret="+ret);
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
      Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var034()
    {
	try
	{
	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = new byte[10];

	    for (int i = 0; i < 10; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    int ret = conv.toBytes(0, data, 0);
	    if (ret != 4)
	    {
		valid = false;
	    }
	    if (data[0] != 0 ||
		data[1] != 0 ||
		data[2] != 0 ||
		data[3] != 0)
	    {
		valid = false;
	    }
	    for (int i = 4; i < 10; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < 10; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(0, data, 5);
	    if (ret != 4)
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
		data[8] != 0)
	    {
		valid = false;
	    }
	    for (int i = 9; i < 10; ++i)
	    {
		if (data[i] != (byte)0xEE)
		{
		    valid = false;
		}
	    }

	    for (int i = 0; i < 10; ++i)
	    {
		data[i] = (byte)0xEE;
	    }
	    ret = conv.toBytes(0, data, 6);
	    if (ret != 4)
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
		data[9] != 0)
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
      Test: Call toBytes(long, byte[], int) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var035()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0, new byte[2], 0);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(long, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var036()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0, new byte[5], 2);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0, new byte[10], -7);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(-10, new byte[10], 0);
	    failed("Did not throw exception. ret="+ret);
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
      Test: Call toBytes(long, byte[], int) with invalid parameters: longValue contains a value to large to be stored in four bytes.
      Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var039()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0x100000000l, new byte[10], 0);
	    failed("Did not throw exception. ret="+ret);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    int ret = conv.toBytes(0, null, 0);
	    failed("Did not throw exception. ret="+ret);
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
	    long[] expectedValue =
	    {
		0x00000000l,
		0x00000080l,
		0x00008000l,
		0x00008080l,
		0x00800000l,
		0x00800080l,
		0x00808000l,
		0x00808080l,
		0x80000000l,
		0x80000080l,
		0x80008000l,
		0x80008080l,
		0x80800000l,
		0x80800080l,
		0x80808000l,
		0x80808080l
	    };

	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = new byte[4];
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

			    if (obj instanceof Long)
			    {
				Long lvalue = (Long)obj;
				long dataValue = lvalue.longValue();
				if (dataValue != expectedValue[i++])
				{
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
      Test: Call toObject(byte[]) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var042()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    Object obj = conv.toObject(new byte[2]);
	    failed("no exception thrown for to small array"+obj);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    Object obj = conv.toObject(null);
	    failed("no exception thrown for null pointer"+obj);
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
      Result: Long object return with valid value.
     **/
    public void Var044()
    {
	try
	{
	    byte[] data =
	    {
		(byte)0x01,
		(byte)0x82,
		(byte)0xF3,
		(byte)0x54,
		(byte)0x0F,
		(byte)0xA1
	    };

	    long[] expectedValue =
	    {
		25359188l,
		2196984847l,
		4082372513l
	    };

	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();

	    for (int i=0; i<3; ++i)
	    {
		Object obj = conv.toObject(data, i);

		if (obj instanceof Long)
		{
		    Long lvalue = (Long)obj;
		    long dataValue = lvalue.longValue();
		    if (dataValue != expectedValue[i])
		    {
			valid = false;
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
      Test: Call toObject(byte[], int) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var045()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    Object obj = conv.toObject(new byte[2], 0);
	    failed("no exception thrown for to small array"+obj);
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
      Test: Call toObject(byte[], int) with invalid parameters: startingPoint to close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var046()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    Object obj = conv.toObject(new byte[7], 4);
	    failed("no exception thrown for not enough space"+obj);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    Object obj = conv.toObject(new byte[4], -5);
	    failed("no exception thrown for negative value"+obj);
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
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    Object obj = conv.toObject(null, 0);
	    failed("no exception thrown for null pointer"+obj);
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
      Test: Call toLong(byte[]) with valid as400Value parameters.
      Result:  long returned with valid value.
     **/
    public void Var049()
    {
	try
	{
	    long[] expectedValue =
	    {
		0x00000000l,
		0x00000080l,
		0x00008000l,
		0x00008080l,
		0x00800000l,
		0x00800080l,
		0x00808000l,
		0x00808080l,
		0x80000000l,
		0x80000080l,
		0x80008000l,
		0x80008080l,
		0x80800000l,
		0x80800080l,
		0x80808000l,
		0x80808080l
	    };

	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	    byte[] data = new byte[4];
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

			    long dataValue = conv.toLong(data);

			    if (dataValue != expectedValue[i++])
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
      Test: Call toLong(byte[]) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var050()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    long l = conv.toLong(new byte[2]);
	    failed("no exception thrown for to small array"+l);
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
      Test: Call toLong(byte[]) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var051()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    long l = conv.toLong(null);
	    failed("no exception thrown for null pointer"+l);
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
      Test: Call toLong(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
      Result: long returned with valid value.
     **/
    public void Var052()
    {
	try
	{
	    byte[] data =
	    {
		(byte)0x01,
		(byte)0x82,
		(byte)0xF3,
		(byte)0x54,
		(byte)0x0F,
		(byte)0xA1
	    };

	    long[] expectedValue =
	    {
		25359188l,
		2196984847l,
		4082372513l
	    };

	    boolean valid = true;
	    AS400UnsignedBin4 conv = new AS400UnsignedBin4();

	    for (int i=0; i<3; ++i)
	    {
		long dataValue = conv.toLong(data, i);

		if (dataValue != expectedValue[i])
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
      Test: Call toLong(byte[], int) with invalid parameters: as400Value to small.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var053()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    long l = conv.toLong(new byte[2], 0);
	    failed("no exception thrown for to small array"+l);
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
      Test: Call toLong(byte[], int) with invalid parameters: startingPoint to close to end of buffer.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var054()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    long l = conv.toLong(new byte[7], 4);
	    failed("no exception thrown for not enough space"+l);
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
      Test: Call toLong(byte[], int) with invalid parameters: startingPoint negative number.
      Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var055()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    long l = conv.toLong(new byte[4], -5);
	    failed("no exception thrown for negative value"+l);
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
      Test: Call toLong(byte[], int) with invalid parameters: as400Value null.
      Result: NullPointerException thrown.
     **/
    public void Var056()
    {
	AS400UnsignedBin4 conv = new AS400UnsignedBin4();
	try
	{
	    long l = conv.toLong(null, 0);
	    failed("no exception thrown for null pointer"+l);
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



