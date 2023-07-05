///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTBin2Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400Bin2;

/**
 Testcase DTBin2Testcase.
 **/
public class DTBin2Testcase extends Testcase
{
    /**
     Test: Construct an AS400Bin2 object.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Bin2 conv = new AS400Bin2();
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
            AS400Bin2 conv = new AS400Bin2();
            AS400Bin2 clone = (AS400Bin2)conv.clone();
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
     Result: The int value 2 returned
     **/
    public void Var003()
    {
        try
        {
            AS400Bin2 conv = new AS400Bin2();
            int ret = conv.getByteLength();
            assertCondition(ret == 2);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call getDefaultValue
     Result: An integer object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
        try
        {
            AS400Bin2 conv = new AS400Bin2();
            Object ret = conv.getDefaultValue();

            if (ret instanceof Short)
            {
                Short sRet = (Short)ret;
                if (sRet.shortValue() != 0)
                {
                    failed("Incorrect return value");
                }
                else
                {
                    byte[] data = conv.toBytes(ret);
                    if (data.length == 2 && data[0] == 0 && data[1] == 0)
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
     Result: Two element byte array returned with a valid value.
     **/
    public void Var005()
    {
        try
        {
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = conv.toBytes(new Short((short)4343));
            if (data.length == 2 && data[0] == (byte)0x10 && data[1] == (byte)0xF7)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Short
     Result: ClassCastException thrown.
     **/
    public void Var006()
    {
        AS400Bin2 conv = new AS400Bin2();
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
        AS400Bin2 conv = new AS400Bin2();
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
     Test: Call toBytes(short) with valid shortValue parameter.
     Result: Two element byte array returned with a valid value.
     **/
    public void Var008()
    {
        try
        {
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = conv.toBytes((short)4343);
            if (data.length == 2 && data[0] == (byte)0x10 && data[1] == (byte)0xF7)
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
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var009()
    {
        try
        {
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = new byte[2];
            int ret = conv.toBytes(new Short((short)4343), data);
            if (ret == 2 && data[0] == (byte)0x10 && data[1] == (byte)0xF7)
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var010()
    {
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes(new Short((short)0), new byte[0]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Short
     Result: ClassCastException thrown.
     **/
    public void Var011()
    {
        AS400Bin2 conv = new AS400Bin2();
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
        AS400Bin2 conv = new AS400Bin2();
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
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes(new Short((short)0), null);
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
     Test: Call toBytes(short, byte[]) with valid parameters.
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var014()
    {
        try
        {
            boolean valid = true;
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = new byte[2];

            short testValue = (short)0x8000;

            for (int x = 0x80; x <= 0xFF; ++x)
            {
                for (int y = 0x00; y <= 0xFF; ++y)
                {
                    int ret = conv.toBytes(testValue++, data);

                    if (ret != 2)
                    {
                        valid = false;
                    }
                    if (data[0] != (byte)x || data[1] != (byte)y)
                    {
                        valid = false;
                    }
                }
            }

            for (int x = 0x00; x <= 0x7F; ++x)
            {
                for (int y = 0x00; y <= 0xFF; ++y)
                {
                    int ret = conv.toBytes(testValue++, data);

                    if (ret != 2)
                    {
                        valid = false;
                    }
                    if (data[0] != (byte)x || data[1] != (byte)y)
                    {
                        valid = false;
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
     Test: Call toBytes(short, byte[]) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var015()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            int ret = conv.toBytes((short)0, new byte[1]);
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
     Test: Call toBytes(short, byte[]) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var016()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            int ret = conv.toBytes((short)5, null);
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
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var017()
    {
        try
        {
            boolean valid = true;
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = new byte[10];

            for (int i = 0; i < 10; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(new Short((short)0), data, 0);
            if (ret != 2)
            {
                valid = false;
            }
            if (data[0] != 0 || data[1] != 0)
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
            ret = conv.toBytes(new Short((short)0), data, 1);
            if (ret != 2)
            {
                valid = false;
            }
            if (data[0] != (byte)0xEE || data[1] != 0 || data[2] != 0)
            {
                valid = false;
            }
            for (int i = 3; i < 10; ++i)
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
            ret = conv.toBytes(new Short((short)0), data, 8);
            if (ret != 2)
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
            if (data[8] != 0 || data[9] != 0)
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var018()
    {
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes(new Short((short)0), new byte[1], 0);
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
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes(new Short((short)0), new byte[5], 4);
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
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes(new Short((short)0), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Short
     Result: ClassCastException thrown.
     **/
    public void Var021()
    {
        AS400Bin2 conv = new AS400Bin2();
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
        AS400Bin2 conv = new AS400Bin2();
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
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes(new Short((short)0), null, 0);
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
     Test: Call toBytes(short, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var024()
    {
        try
        {
            boolean valid = true;
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = new byte[10];

            for (int i = 0; i < 10; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes((short)0, data, 0);
            if (ret != 2)
            {
                valid = false;
            }
            if (data[0] != 0 || data[1] != 0)
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
            ret = conv.toBytes((short)0, data, 1);
            if (ret != 2)
            {
                valid = false;
            }
            if (data[0] != (byte)0xEE || data[1] != 0 || data[2] != 0)
            {
                valid = false;
            }
            for (int i = 3; i < 10; ++i)
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
            ret = conv.toBytes((short)0, data, 8);
            if (ret != 2)
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
            if (data[8] != 0 || data[9] != 0)
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
     Test: Call toBytes(short, byte[], int) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var025()
    {
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes((short)0, new byte[1], 0);
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
     Test: Call toBytes(short, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes((short)0, new byte[5], 4);
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
     Test: Call toBytes(short, byte[], int) with invalid parameters: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes((short)0, new byte[10], -1);
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
     Test: Call toBytes(short, byte[], int) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var028()
    {
        AS400Bin2 conv = new AS400Bin2();

        try
        {
            int ret = conv.toBytes((short)0, null, 0);
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
     Result: Short object return with valid value.
     **/
    public void Var029()
    {
        try
        {
            boolean valid = true;
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = new byte[2];
            Object obj;
            Short svalue;
            short dataValue;
            short expectedValue = (short)0x8000;

            for (int x = 0x80; x <= 0xFF; ++x)
            {
                data[0] = (byte)x;
                for (int y = 0x00; y <= 0xFF; ++y)
                {
                    data[1] = (byte)y;

                    obj = conv.toObject(data);

                    if (obj instanceof Short)
                    {
                        svalue = (Short)obj;
                        dataValue = svalue.shortValue();
                        if (dataValue != expectedValue++)
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

            for (int x = 0x00; x <= 0x7F; ++x)
            {
                data[0] = (byte)x;
                for (int y = 0x00; y <= 0xFF; ++y)
                {
                    data[1] = (byte)y;

                    obj = conv.toObject(data);

                    if (obj instanceof Short)
                    {
                        svalue = (Short)obj;
                        dataValue = svalue.shortValue();
                        if (dataValue != expectedValue++)
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

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var030()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            Object obj = conv.toObject(new byte[0]);
            failed("no exception thrown for to small array");
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
        AS400Bin2 conv = new AS400Bin2();
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
     Result: Short object return with valid value.
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
            AS400Bin2 conv = new AS400Bin2();
            Object obj;
            Short svalue;
            short dataValue;
            short[] expectedValue =
            {
                (short)386,
                (short)-32013,
                (short)-3244
            };

            for (int i=0; i<3; ++i)
            {
                obj = conv.toObject(data, i);

                if (obj instanceof Short)
                {
                    svalue = (Short)obj;
                    dataValue = svalue.shortValue();
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
     Test: Call toObject(byte[], int) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var033()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            Object obj = conv.toObject(new byte[1], 0);
            failed("no exception thrown for to small array");
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
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            Object obj = conv.toObject(new byte[10], 9);
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
        AS400Bin2 conv = new AS400Bin2();
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
        AS400Bin2 conv = new AS400Bin2();
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
     Test: Call toShort(byte[]) with valid as400Value parameters.
     Result: short returned with valid value.
     **/
    public void Var037()
    {
        try
        {
            boolean valid = true;
            AS400Bin2 conv = new AS400Bin2();
            byte[] data = new byte[2];
            short dataValue;
            short expectedValue = (short)0x8000;

            for (int x = 0x80; x <= 0xFF; ++x)
            {
                data[0] = (byte)x;
                for (int y = 0x00; y <= 0xFF; ++y)
                {
                    data[1] = (byte)y;

                    dataValue = conv.toShort(data);

                    if (dataValue != expectedValue++)
                    {
                        valid = false;
                    }
                }
            }

            for (int x = 0x00; x <= 0x7F; ++x)
            {
                data[0] = (byte)x;
                for (int y = 0x00; y <= 0xFF; ++y)
                {
                    data[1] = (byte)y;

                    dataValue = conv.toShort(data);

                    if (dataValue != expectedValue++)
                    {
                        valid = false;
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
     Test: Call toShort(byte[]) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var038()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            short sh = conv.toShort(new byte[0]);
            failed("no exception thrown for to small array");
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
     Test: Call toShort(byte[]) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var039()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            short sh = conv.toShort(null);
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
     Test: Call toShort(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
     Result: short returned with valid value.
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

            short[] expectedValue =
            {
                (short)386,
                (short)-32013,
                (short)-3244
            };

            boolean valid = true;
            AS400Bin2 conv = new AS400Bin2();
            for (int i=0; i<3; ++i)
            {
                short dataValue = conv.toShort(data, i);

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
     Test: Call toShort(byte[], int) with invalid parameters: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var041()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            short sh = conv.toShort(new byte[0]);
            failed("no exception thrown for to small array");
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
     Test: Call toShort(byte[], int) with invalid parameters: startingPoint to close to end of buffer
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var042()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            short sh = conv.toShort(new byte[10], 9);
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
     Test: Call toShort(byte[], int) with invalid parameters: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var043()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            short sh = conv.toShort(new byte[5], -100);
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
     Test: Call toShort(byte[], int) with invalid parameters: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var044()
    {
        AS400Bin2 conv = new AS400Bin2();
        try
        {
            short sh = conv.toShort(null);
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



