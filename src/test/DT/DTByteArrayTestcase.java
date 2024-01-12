///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTByteArrayTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400ByteArray;

import test.Testcase;

/**
 Testcase DTByteArrayTestcase.
 **/
public class DTByteArrayTestcase extends Testcase
{
    /**
     Test: Construct an AS400ByteArray object with valid parameter.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            for (int i=0; i<100; ++i)
            {
                AS400ByteArray conv = new AS400ByteArray(i);
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Construct an AS400ByteArray object with negative number parameter.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var002()
    {
        try
        {
            AS400ByteArray conv = new AS400ByteArray(-1);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "length (-1): Length is not valid."))
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
     Test: Call clone
     Result: Cloned object returned.
     **/
    public void Var003()
    {
        try
        {
            AS400ByteArray conv = new AS400ByteArray(10);
            AS400ByteArray clone = (AS400ByteArray)conv.clone();
            if (clone != null && clone != conv && clone.getByteLength() == 10)
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
     Result: The correct int value returned
     **/
    public void Var004()
    {
        try
        {
            boolean valid = true;

            for (int i=0; i<100; ++i)
            {
                AS400ByteArray conv = new AS400ByteArray(i);
                int ret = conv.getByteLength();
                if (ret != i)
                {
                    valid = false;
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
     Test: Call getDefaultValue
     Result: A byte array with a length of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var005()
    {
        try
        {
            AS400ByteArray conv = new AS400ByteArray(10);
            Object ret = conv.getDefaultValue();

            if (ret instanceof byte[])
            {
                byte[] barray = (byte[])ret;

                if (barray.length != 0)
                {
                    failed("Incorrect return value");
                }
                else
                {
                    byte[] data = conv.toBytes(ret);
                    if (data.length == 10 &&
                        data[0] == (byte)0x00 &&
                        data[1] == (byte)0x00 &&
                        data[2] == (byte)0x00 &&
                        data[3] == (byte)0x00 &&
                        data[4] == (byte)0x00 &&
                        data[5] == (byte)0x00 &&
                        data[6] == (byte)0x00 &&
                        data[7] == (byte)0x00 &&
                        data[8] == (byte)0x00 &&
                        data[9] == (byte)0x00)
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
     Result: Byte array returned with a valid value.
     **/
    public void Var006()
    {
        try
        {
            AS400ByteArray conv = new AS400ByteArray(8);
            byte[] input = {(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03};
            byte[] data = conv.toBytes(input);
            if (data.length == 8 &&
                data[0] == (byte)0x00 &&
                data[1] == (byte)0x01 &&
                data[2] == (byte)0x02 &&
                data[3] == (byte)0x03 &&
                data[4] == (byte)0x00 &&
                data[5] == (byte)0x00 &&
                data[6] == (byte)0x00 &&
                data[7] == (byte)0x00)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of byte[].
     Result: ClassCastException thrown.
     **/
    public void Var007()
    {
        AS400ByteArray conv = new AS400ByteArray(5);

        try
        {
            byte[] ret = conv.toBytes(new int[0]);
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var008()
    {
        AS400ByteArray conv = new AS400ByteArray(5);

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
     Test: Call toBytes(Object, byte[]) with valid javaValue parameter, test extra bytes are silently ignored.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var009()
    {
        try
        {
            AS400ByteArray conv = new AS400ByteArray(3);
            byte[] input = {(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03};
            byte[] data =
            {
                (byte)0x00,
                (byte)0x00,
                (byte)0x00,
                (byte)0x00
            };

            int ret = conv.toBytes(input, data);
            if (ret == 3 &&
                data[0] == (byte)0x00 &&
                data[1] == (byte)0x01 &&
                data[2] == (byte)0x02 &&
                data[3] == (byte)0x00)
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
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var010()
    {
        try
        {
            byte[][] expectedValue =
            {
                /*  1 */ {(byte)0xF0},
                /*  2 */ {(byte)0xF1,(byte)0xF2},
                /*  3 */ {(byte)0xF1,(byte)0xF2,(byte)0xD3},
                /*  4 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /*  5 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xD5},
                /*  6 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6},
                /*  7 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7},
                /*  8 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xD8},
                /*  9 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9},
                /* 10 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xD0},
                /* 11 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1},
                /* 12 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2},
                /* 13 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3},
                /* 14 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /* 15 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xD5},
                /* 16 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6},
                /* 17 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xD7},
                /* 18 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8},
                /* 19 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9},
                /* 20 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xD0},
                /* 21 */ {(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1},
                /* 22 */ {(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xD3},
                /* 23 */ {(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3},
                /* 24 */ {(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xD6},
                /* 25 */ {(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6},
                /* 26 */ {(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xD7},
                /* 27 */ {(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7},
                /* 28 */ {(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9},
                /* 29 */ {(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xD9},
                /* 30 */ {(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2},
                /* 31 */ {(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xD2}
            };

            boolean valid = true;
            AS400ByteArray conv;
            byte[] data = new byte[31];

            for (int i=0; i<31; ++i)
            {
                conv = new AS400ByteArray(i+1);
                int ret = conv.toBytes(expectedValue[i], data);

                if (ret != i+1)
                {
                    valid = false;
                }
                for (int x = 0; x < ret; ++x)
                {
                    if (data[x] != expectedValue[i][x])
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var011()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            int ret = conv.toBytes(new byte[5], new byte[3]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of byte[].
     Result: ClassCastException thrown.
     **/
    public void Var012()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            int ret = conv.toBytes(new int[0], new byte[10]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var013()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
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
    public void Var014()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            int ret = conv.toBytes(new byte[5], null);
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
     Result: Length is returned, valid bytes are written in the array.
     **/
    public void Var015()
    {
        try
        {
            boolean valid = true;
            AS400ByteArray conv = new AS400ByteArray(5);
            byte[] testValue = new byte[0];
            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(testValue, data, 0);
            if (ret != 5)
            {
                valid = false;
            }
            if (data[0] != (byte)0x00 ||
                data[1] != (byte)0x00 ||
                data[2] != (byte)0x00 ||
                data[3] != (byte)0x00 ||
                data[4] != (byte)0x00)
            {
                valid = false;
            }
            for (int i = 5; i < 12; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(testValue, data, 3);
            if (ret != 5)
            {
                valid = false;
            }
            for (int i = 0; i <= 2; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[3] != (byte)0x00 ||
                data[4] != (byte)0x00 ||
                data[5] != (byte)0x00 ||
                data[6] != (byte)0x00 ||
                data[7] != (byte)0x00)
            {
                valid = false;
            }
            for (int i = 8; i < 12; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(testValue, data, 7);
            if (ret != 5)
            {
                valid = false;
            }
            for (int i = 0; i <= 6; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[7]  != (byte)0x00 ||
                data[8]  != (byte)0x00 ||
                data[9]  != (byte)0x00 ||
                data[10] != (byte)0x00 ||
                data[11] != (byte)0x00)
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
    public void Var016()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            int ret = conv.toBytes(new byte[5], new byte[3], 0);
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
    public void Var017()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            int ret = conv.toBytes(new byte[5], new byte[7], 4);
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
    public void Var018()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            int ret = conv.toBytes(new byte[5], new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of byte[].
     Result: ClassCastException thrown.
     **/
    public void Var019()
    {
        AS400ByteArray conv = new AS400ByteArray(5);

        try
        {
            int ret = conv.toBytes(new int[0], new byte[10], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var020()
    {
        AS400ByteArray conv = new AS400ByteArray(5);

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
    public void Var021()
    {
        AS400ByteArray conv = new AS400ByteArray(5);

        try
        {
            int ret = conv.toBytes(new byte[5], null, 0);
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
     Result: Byte array returned with valid value.
     **/
    public void Var022()
    {
        try
        {
            byte[][] data =
            {
                /*  1 */ {(byte)0xF0},
                /*  2 */ {(byte)0xF1,(byte)0xE2},
                /*  3 */ {(byte)0xF1,(byte)0xF2,(byte)0xD3},
                /*  4 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xC4},
                /*  5 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xB5},
                /*  6 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xA6},
                /*  7 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xA7},
                /*  8 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xB8},
                /*  9 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xC9},
                /* 10 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xD0},
                /* 11 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xE1},
                /* 12 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2},
                /* 13 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xA3},
                /* 14 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xA4},
                /* 15 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xB5},
                /* 16 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xA6},
                /* 17 */ {(byte)0x09,(byte)0x18,(byte)0x27,(byte)0x36,(byte)0x45,(byte)0x54,(byte)0x63,(byte)0x72,(byte)0x81,(byte)0x90,(byte)0xA1,(byte)0xB2,(byte)0xC3,(byte)0xD4,(byte)0xE5,(byte)0xF6,(byte)0xB7},
                /* 18 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xC8},
                /* 19 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xA9},
                /* 20 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xB0},
                /* 21 */ {(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xC1},
                /* 22 */ {(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xD3},
                /* 23 */ {(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xA3},
                /* 24 */ {(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xB6},
                /* 25 */ {(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xC6},
                /* 26 */ {(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xD7},
                /* 27 */ {(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xE7},
                /* 28 */ {(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xA9},
                /* 29 */ {(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xB9},
                /* 30 */ {(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xC2},
                /* 31 */ {(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xD2}
            };

            boolean valid = true;

            for (int i=0; i<31; ++i)
            {
                AS400ByteArray conv = new AS400ByteArray(i+1);
                Object obj = conv.toObject(data[i]);

                if (obj instanceof byte[])
                {
                    byte[] barray = (byte[])obj;
                    if (barray.length != i+1)
                    {
                        valid = false;
                    }
                    for (int x=0; x<barray.length; ++x)
                    {
                        if (barray[x] != data[i][x])
                        {
                            valid = false;
                        }
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
     Test: Call toObject(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var023()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            Object obj = conv.toObject(new byte[3]);
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
     Test: Call toObject(byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var024()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
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
     Result: Byte array returned with valid value.
     **/
    public void Var025()
    {
        try
        {
            byte[] data =
            {
                (byte)0xF8,
                (byte)0xF9,
                (byte)0xF1,
                (byte)0xD2,
                (byte)0xF5,
                (byte)0xF7
            };

            boolean valid = true;
            AS400ByteArray conv = new AS400ByteArray(4);

            for (int i=0; i<3; ++i)
            {
                Object obj = conv.toObject(data, i);

                if (obj instanceof byte[])
                {
                    byte[] barray = (byte[])obj;
                    if (barray.length != 4 ||
                        barray[0] != data[i] ||
                        barray[1] != data[i+1] ||
                        barray[2] != data[i+2] ||
                        barray[3] != data[i+3])
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
    public void Var026()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            Object obj = conv.toObject(new byte[3], 0);
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
     Test: Call toObject(byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            Object obj = conv.toObject(new byte[15], 13);
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
    public void Var028()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
        try
        {
            Object obj = conv.toObject(new byte[15], -10);
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
    public void Var029()
    {
        AS400ByteArray conv = new AS400ByteArray(5);
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
}
