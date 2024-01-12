///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTBin1Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400Bin1;

import test.Testcase;

/**
 Testcase DTBin1Testcase.
 **/
public class DTBin1Testcase extends Testcase
{
    /**
     Test: Construct an AS400Bin1 object.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Bin1 conv = new AS400Bin1();
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
            AS400Bin1 conv = new AS400Bin1();
            AS400Bin1 clone = (AS400Bin1)conv.clone();
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
     Result: The int value 1 returned
     **/
    public void Var003()
    {
        try
        {
            AS400Bin1 conv = new AS400Bin1();
            int ret = conv.getByteLength();
            assertCondition(ret == 1);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call getDefaultValue
     Result: A Byte object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
        try
        {
            AS400Bin1 conv = new AS400Bin1();
            Object ret = conv.getDefaultValue();

            if (ret instanceof Byte)
            {
                Byte sRet = (Byte)ret;
                if (sRet.byteValue() != 0)
                {
                    failed("Incorrect return value");
                }
                else
                {
                    byte[] data = conv.toBytes(ret);
                    if (data.length == 1 && data[0] == 0)
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
     Result: One-element byte array returned with a valid value.
     **/
    public void Var005()
    {
        try
        {
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = conv.toBytes(new Byte((byte)0x7F));
            if (data.length == 1 && data[0] == (byte)0x7F)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Byte
     Result: ClassCastException thrown.
     **/
    public void Var006()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte[] ret = conv.toBytes(new Double(0));
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var007()
    {
        AS400Bin1 conv = new AS400Bin1();
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
     Test: Call toBytes(byte) with valid byteValue parameter.
     Result: One-element byte array returned with a valid value.
     **/
    public void Var008()
    {
        try
        {
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = conv.toBytes((byte)0xFF);
            if (data.length == 1 && data[0] == (byte)0xFF)
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
     Test: Call toBytes(Object, byte[]) with valid parameters.
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var009()
    {
        try
        {
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = new byte[1];
            int ret = conv.toBytes(new Byte((byte)43), data);
            if (ret == 1 && data[0] == (byte)0x2B)
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var010()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes(new Byte((byte)0), new byte[0]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Byte
     Result: ClassCastException thrown.
     **/
    public void Var011()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            int ret = conv.toBytes(new Integer(0xFFFF), new byte[10]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var012()
    {
        AS400Bin1 conv = new AS400Bin1();
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var013()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes(new Byte((byte)0), null);
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
     Test: Call toBytes(byte, byte[]) with valid parameters.
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var014()
    {
        try
        {
            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = new byte[1];

            byte testValue = (byte)0x80;

            for (int x = 0x80; x <= 0xFF; ++x)
            {
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                    int ret = conv.toBytes(testValue++, data);

                    if (ret != 1)
                    {
                        valid = false;
                    }
                    if (data[0] != (byte)x)
                    {
                        valid = false;
                    }
                ///}
            }

            for (int x = 0x00; x <= 0x7F; ++x)
            {
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                    int ret = conv.toBytes(testValue++, data);

                    if (ret != 1)
                    {
                        valid = false;
                    }
                    if (data[0] != (byte)x)
                    {
                        valid = false;
                    }
                ///}
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call toBytes(byte, byte[]) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var015()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            int ret = conv.toBytes((byte)0, new byte[0]);
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
     Test: Call toBytes(byte, byte[]) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var016()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            int ret = conv.toBytes((byte)5, null);
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
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var017()
    {
        try
        {
            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = new byte[10];

            for (int i = 0; i < 10; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(new Byte((byte)0), data, 0);
            if (ret != 1)
            {
                valid = false;
            }
            if (data[0] != 0)
            {
                valid = false;
            }
            for (int i = 1; i < 10; ++i)
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
            ret = conv.toBytes(new Byte((byte)0), data, 1);
            if (ret != 1)
            {
                valid = false;
            }
            if (data[0] != (byte)0xEE || data[1] != 0)
            {
                valid = false;
            }
            for (int i = 2; i < 10; ++i)
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
            ret = conv.toBytes(new Byte((byte)0), data, 8);
            if (ret != 1)
            {
                valid = false;
            }
            for (int i = 0; i <= 7; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[8] != 0 || data[9] != (byte)0xEE)
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var018()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes(new Byte((byte)0), new byte[0], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var019()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes(new Byte((byte)0), new byte[5], 5);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var020()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes(new Byte((byte)0), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Byte
     Result: ClassCastException thrown.
     **/
    public void Var021()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            int ret = conv.toBytes(new Integer(0xFFFF), new byte[10], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var022()
    {
        AS400Bin1 conv = new AS400Bin1();
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var023()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes(new Byte((byte)0), null, 0);
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
     Test: Call toBytes(byte, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var024()
    {
        try
        {
            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = new byte[10];

            for (int i = 0; i < 10; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes((byte)0, data, 0);
            if (ret != 1)
            {
                valid = false;
            }
            if (data[0] != 0)
            {
                valid = false;
            }
            for (int i = 1; i < 10; ++i)
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
            ret = conv.toBytes((byte)0, data, 1);
            if (ret != 1)
            {
                valid = false;
            }
            if (data[0] != (byte)0xEE || data[1] != 0)
            {
                valid = false;
            }
            for (int i = 2; i < 10; ++i)
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
            ret = conv.toBytes((byte)0, data, 8);
            if (ret != 1)
            {
                valid = false;
            }
            for (int i = 0; i <= 7; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[8] != 0 || data[9] != (byte)0xEE)
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
     Test: Call toBytes(byte, byte[], int) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var025()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes((byte)0, new byte[0], 0);
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
     Test: Call toBytes(byte, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes((byte)0, new byte[5], 5);
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
     Test: Call toBytes(byte, byte[], int) with invalid parameters: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes((byte)0, new byte[10], -1);
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
     Test: Call toBytes(byte, byte[], int) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var028()
    {
        AS400Bin1 conv = new AS400Bin1();

        try
        {
            int ret = conv.toBytes((byte)0, null, 0);
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
     Result: Byte object return with valid value.
     **/
    public void Var029()
    {
        try
        {
            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = new byte[1];
            Object obj;
            Byte svalue;
            byte dataValue;
            byte expectedValue = (byte)0x80;

            for (int x = 0x80; x <= 0xFF; ++x)
            {
                data[0] = (byte)x;
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                ///    data[1] = (byte)y;

                    obj = conv.toObject(data);

                    if (obj instanceof Byte)
                    {
                        svalue = (Byte)obj;
                        dataValue = svalue.byteValue();
                        if (dataValue != expectedValue++)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }
                ///}
            }

            for (int x = 0x00; x <= 0x7F; ++x)
            {
                data[0] = (byte)x;
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                    ///data[1] = (byte)y;

                    obj = conv.toObject(data);

                    if (obj instanceof Byte)
                    {
                        svalue = (Byte)obj;
                        dataValue = svalue.byteValue();
                        if (dataValue != expectedValue++)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }
                ///}
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var030()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            Object obj = conv.toObject(new byte[0]);
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
     Test: Call toObject(byte[]) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var031()
    {
        AS400Bin1 conv = new AS400Bin1();
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
     Result: Byte object return with valid value.
     **/
    public void Var032()
    {
        try
        {
            byte[] data = new byte[4];
            data[0] = (byte)0x01;
            data[1] = (byte)0x82;
            data[2] = (byte)0xF3;
            data[3] = (byte)0x54;

            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            Object obj;
            Byte svalue;
            byte dataValue;
            byte[] expectedValue =
            {
                (byte)0x01,
                (byte)0x82,
                (byte)0xF3,
                (byte)0x54
            };

            for (int i=0; i<4; ++i)
            {
                obj = conv.toObject(data, i);

                if (obj instanceof Byte)
                {
                    svalue = (Byte)obj;
                    dataValue = svalue.byteValue();
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
     Test: Call toObject(byte[], int) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var033()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            Object obj = conv.toObject(new byte[0], 0);
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
     Test: Call toObject(byte[], int) with invalid parameters: startingPoint to close to end of buffer
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var034()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            Object obj = conv.toObject(new byte[10], 10);
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
     Test: Call toObject(byte[], int) with invalid parameters: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var035()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            Object obj = conv.toObject(new byte[5], -100);
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
     Test: Call toObject(byte[], int) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var036()
    {
        AS400Bin1 conv = new AS400Bin1();
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
     Test: Call toByte(byte[]) with valid as400Value parameters.
     Result: byte returned with valid value.
     **/
    public void Var037()
    {
        try
        {
            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            byte[] data = new byte[2];
            byte dataValue;
            byte expectedValue = (byte)0x80;

            for (int x = 0x80; x <= 0xFF; ++x)
            {
                data[0] = (byte)x;
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                ///    data[1] = (byte)y;

                    dataValue = conv.toByte(data);

                    if (dataValue != expectedValue++)
                    {
                        valid = false;
                    }
                ///}
            }

            for (int x = 0x00; x <= 0x7F; ++x)
            {
                data[0] = (byte)x;
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                ///    data[1] = (byte)y;

                    dataValue = conv.toByte(data);

                    if (dataValue != expectedValue++)
                    {
                        valid = false;
                    }
                ///}
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call toByte(byte[]) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var038()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte sh = conv.toByte(new byte[0]);
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
     Test: Call toByte(byte[]) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var039()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte sh = conv.toByte(null);
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
     Test: Call toByte(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
     Result: byte returned with valid value.
     **/
    public void Var040()
    {
        try
        {
            byte[] data =
            {
                (byte)0x01,
                (byte)0x82,
                (byte)0xF3,
                (byte)0x54
            };

            byte[] expectedValue =
            {
                (byte)0x01,
                (byte)0x82,
                (byte)0xF3,
                (byte)0x54
            };

            boolean valid = true;
            AS400Bin1 conv = new AS400Bin1();
            for (int i=0; i<4; ++i)
            {
                byte dataValue = conv.toByte(data, i);

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
     Test: Call toByte(byte[], int) with invalid parameters: as400Value too small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var041()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte sh = conv.toByte(new byte[0]);
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
     Test: Call toByte(byte[], int) with invalid parameters: startingPoint to close to end of buffer
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var042()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte sh = conv.toByte(new byte[10], 10);
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
     Test: Call toByte(byte[], int) with invalid parameters: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var043()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte sh = conv.toByte(new byte[5], -100);
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
     Test: Call toByte(byte[], int) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var044()
    {
        AS400Bin1 conv = new AS400Bin1();
        try
        {
            byte sh = conv.toByte(null);
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



