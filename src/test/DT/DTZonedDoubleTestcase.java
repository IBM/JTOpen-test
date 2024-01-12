///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTZonedDoubleTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400ZonedDecimal;

import test.Testcase;

/**
 Testcase DTZonedDoubleTestcase.
 **/
public class DTZonedDoubleTestcase extends Testcase
{
    /**
     Test: Call toBytes(double) with valid doubleValue parameter.
     Result: Byte array returned with a valid value.
     **/
    public void Var001()
    {
        try
        {
            AS400ZonedDecimal conv = new AS400ZonedDecimal(8, 4);
            byte[] data = conv.toBytes(2468.9753d);
            if (data.length == 8 &&
                data[0] == (byte)0xF2 &&
                data[1] == (byte)0xF4 &&
                data[2] == (byte)0xF6 &&
                data[3] == (byte)0xF8 &&
                data[4] == (byte)0xF9 &&
                data[5] == (byte)0xF7 &&
                data[6] == (byte)0xF5 &&
                data[7] == (byte)0xF3)
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
     Test: Call toBytes(double) with invalid parameters: doubleValue has to many decimal places.
     Result: Rouding.
     **/
    public void Var002()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            byte[] data = conv.toBytes(12.3456d);
            if (data.length == 5 &&
                data[0] == (byte)0xF1 &&
                data[1] == (byte)0xF2 &&
                data[2] == (byte)0xF3 &&
                data[3] == (byte)0xF4 &&
                data[4] == (byte)0xF6)
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
     Test: Call toBytes(double) with invalid parameters: doubleValue has to many digits after scale normalization.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var003()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            byte[] ret = conv.toBytes(123.45d);
            failed("No exception thrown but got "+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double) with invalid parameters: doubleValue has to many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var004()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            byte[] ret = conv.toBytes(123.4567d);
            failed("No exception thrown."+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double) with invalid parameters: doubleValue has too many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var005()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(25,3);
        try
        {
            byte[] ret = conv.toBytes(12343432432432432432423.4567d);
            failed("No exception thrown."+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double, byte[]) with valid parameters.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var006()
    {
        try
        {
            int[][] ds =
            {
                {1,0},
                {2,1},
                {3,3},
                {4,2},
                {5,3},
                {6,1},
                {7,6},
                {8,4},
                {9,2},
                {10,10},
                {11,0},
                {12,6},
                {13,7},
                {14,8},
                {15,10},
                {16,9},
                {17,3},
                {18,2},
                {19,1},
                {20,15},
                {21,5},
                {22,4},
                {23,21},
                {24,6},
                {25,23},
                {26,25},
            };

            double[] testValue =
            {
                /*  1 */         0d,
                /*  2 */        1.2d,
                /*  3 */        -0.123d,
                /*  4 */        12.34d,
                /*  5 */       -12.345d,
                /*  6 */        12345.6d,
                /*  7 */        1.234567d,
                /*  8 */       -1234.5678d,
                /*  9 */        1234567.89d,
                /* 10 */      -0.1234567890d,
                /* 11 */         98765432101d,
                /* 12 */        987654.321012d,
                /* 13 */        987654.3210123d,
                /* 14 */        987654.32101234d,
                /* 15 */       -98765.4321012345d,
                /* 16 */        9876543.210123456d,
                /* 17 */       -98765432101234.567d,
                /* 18 */        9876543210123456.78d,
                /* 19 */        987654321012345678.9d,
                /* 20 */       -98765.432101234567890d,
                /* 21 */        1122334455667788.99001d,
                /* 22 */       -234455667788990011.2233d,
                /* 23 */        23.344556677889900112233d,
                /* 24 */       -345566778900112233.445566d,
                /* 25 */        34.45566778890112233445566d,
                /* 26 */       -4.5667788990011223344556677d,
            };

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
                /* 16 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF5}, // Rounding error (last digit).
                /* 17 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xD3}, // Rounding error (last digit).
                /* 18 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF0,(byte)0xF0}, // Rounding error (last 2 digits).
                /* 19 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF7,(byte)0xF2,(byte)0xF8,(byte)0xF0},  // Rounding error (last 5 digits).
                /* 20 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF0,(byte)0xF8,(byte)0xF3,(byte)0xD2}, // Rounding error (last 2 digits).
                /* 21 */ {(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0}, // Rounding error (last 6 digits).
                /* 22 */ {(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF6,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xD0}, // Rounding error (last 5 digits).
                /* 23 */ {(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF1,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0}, // Rounding error (last 7 digits).
                /* 24 */ {(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF5,(byte)0xF6,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xD0}, // Rounding error (last 8 digits).
                /* 25 */ {(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF4,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0}, // Rounding error (last 9 digits).
                /* 26 */ {(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xD0}, // Rounding error (last 10 digits).
            };

            boolean valid = true;
            byte[] data = new byte[31];

            for (int i=0; i<26; ++i)
            {
                AS400ZonedDecimal conv = new AS400ZonedDecimal(ds[i][0], ds[i][1]);
                int ret = conv.toBytes(testValue[i], data);

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
     Test: Call toBytes(double, byte[]) with valid parameters where there are less digits in the input then the output.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var007()
    {
        try
        {
            int[][] ds =
            {
                /*  1 */	{4,0},
                /*  2 */	{3,1},
                /*  3 */	{6,3},
                /*  4 */	{5,2},
                /*  5 */	{6,3},
                /*  6 */	{7,4},
                /*  7 */	{6,3},
                /*  8 */	{7,4},
                /*  9 */	{10,3},
                /* 10 */	{11,4},
                /* 11 */	{10,5},
                /* 12 */	{11,6},
                /* 13 */	{10,0},
                /* 14 */	{11,1},
                /* 15 */	{12,3},
                /* 16 */	{13,2},
                /* 17 */	{10,5},
                /* 18 */	{11,5},
                /* 19 */	{12,6},
                /* 20 */	{13,6},
                /* 21 */	{10,6},
                /* 22 */	{11,6},
                /* 23 */	{12,7},
                /* 24 */	{13,7}
            };

            String[] testValue =
            {
                /*  1 */	"-23",
                /*  2 */        "-2.3",
                /*  3 */	"1.234",
                /*  4 */        "12.34",
                /*  5 */	"-123.4",
                /*  6 */	"-123.456",
                /*  7 */	"123.4",
                /*  8 */	"123.456",
                /*  9 */	"-1234.56",
                /* 10 */	"-1234.56",
                /* 11 */	"1234.56",
                /* 12 */	"1234.56",
                /* 13 */	"-12345",
                /* 14 */	"-1234.5",
                /* 15 */	"12.345",
                /* 16 */	"123.45",
                /* 17 */	"-12345",
                /* 18 */	"-123456.789",
                /* 19 */	"123456.7",
                /* 20 */	"1234567.89",
                /* 21 */	"-123",
                /* 22 */	"-123.45",
                /* 23 */	"123.45",
                /* 24 */	"123.4567"
            };

            byte[][] expectedValue =
            {
                /*  1 */ {(byte)0xF0,(byte)0xF0,(byte)0xF2,(byte)0xD3},
                /*  2 */ {(byte)0xF0,(byte)0xF2,(byte)0xD3},
                /*  3 */ {(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /*  4 */ {(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /*  5 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF0,(byte)0xD0},
                /*  6 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xD0},
                /*  7 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF0,(byte)0xF0},
                /*  8 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF0},
                /*  9 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xD0},
                /* 10 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF0,(byte)0xD0},
                /* 11 */ {(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF0,(byte)0xF0,(byte)0xF0},
                /* 12 */ {(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0},
                /* 13 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xD5},
                /* 14 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xD5},
                /* 15 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5},
                /* 16 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5},
                /* 17 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xD0},
                /* 18 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xD0},
                /* 19 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0},
                /* 20 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0},
                /* 21 */ {(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xD0},
                /* 22 */ {(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xD0},
                /* 23 */ {(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF0},
                /* 24 */ {(byte)0xF0,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF0,(byte)0xF0,(byte)0xF0}
            };

            boolean valid = true;
            byte[] data = new byte[31];

            for (int i=0; i<24; ++i)
            {
                AS400ZonedDecimal conv = new AS400ZonedDecimal(ds[i][0], ds[i][1]);
                int ret = conv.toBytes(Double.valueOf(testValue[i]).doubleValue(), data);

                if (ret != ds[i][0])
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
     Test: Call toBytes(double, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var008()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(12.345d, new byte[3]);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(double, byte[]) with invalid parameters: doubleValue has to many digits after scale normalization.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var009()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(123.45d, new byte[10]);
            failed("No exception thrown."+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double, byte[]) with invalid parameters: doubleValue has to many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var010()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(123.4567d, new byte[10]);
            failed("No exception thrown."+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double, byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var011()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(12.345d, null);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(double, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var012()
    {
        try
        {
            boolean valid = true;
            AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
            double testValue = 11.111d;
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
            if (data[0] != (byte)0xF1 ||
                data[1] != (byte)0xF1 ||
                data[2] != (byte)0xF1 ||
                data[3] != (byte)0xF1 ||
                data[4] != (byte)0xF1)
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
            if (data[3] != (byte)0xF1 ||
                data[4] != (byte)0xF1 ||
                data[5] != (byte)0xF1 ||
                data[6] != (byte)0xF1 ||
                data[7] != (byte)0xF1)
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
            if (data[7]  != (byte)0xF1 ||
                data[8]  != (byte)0xF1 ||
                data[9]  != (byte)0xF1 ||
                data[10] != (byte)0xF1 ||
                data[11] != (byte)0xF1)
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var013()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(12.345d, new byte[3], 0);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var014()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(12.345d, new byte[7], 4);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var015()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(12.345d, new byte[10], -1);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: doubleValue has to many digits after scale normalization.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var016()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(123.45d, new byte[10], 0);
            failed("No exception thrown."+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: doubleValue has to many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var017()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(123.4567d, new byte[10], 0);
            failed("No exception thrown."+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "doubleValue: Length is not valid."))
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var018()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(12.345d, null, 0);
            failed("No exception thrown."+ret);
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
     Test: Call toDouble(byte[]) with valid as400Value parameters.
     Result: double returned with valid value.
     **/
    public void Var019()
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

            int[][] ds =
            {
                {1,0},
                {2,1},
                {3,3},
                {4,2},
                {5,3},
                {6,1},
                {7,6},
                {8,4},
                {9,2},
                {10,10},
                {11,0},
                {12,6},
                {13,7},
                {14,8},
                {15,10},
                {16,9},
                {17,3},
                {18,2},
                {19,1},
                {20,15},
                {21,5},
                {22,4},
                {23,21},
                {24,6},
                {25,23},
                {26,25},
                {27,4},
                {28,7},
                {29,14},
                {30,0},
                {31,31}
            };

            String[] expectedValue =
            {
                /*  1 */         "0",
                /*  2 */        "1.2",
                /*  3 */      "-0.123",
                /*  4 */        "12.34",
                /*  5 */       "-12.345",
                /*  6 */        "12345.6",
                /*  7 */        "1.234567",
                /*  8 */       "-1234.5678",
                /*  9 */        "1234567.89",
                /* 10 */      "-0.1234567890",
                /* 11 */         "98765432101",
                /* 12 */        "987654.321012",
                /* 13 */        "987654.3210123",
                /* 14 */        "987654.32101234",
                /* 15 */       "-98765.4321012345",
                /* 16 */        "9876543.210123456",
                /* 17 */       "-98765432101234.567",
                /* 18 */        "9876543210123456.78",
                /* 19 */        "987654321012345678.9",
                /* 20 */       "-98765.432101234567890",
                /* 21 */        "1122334455667788.99001",
                /* 22 */       "-234455667788990011.2233",
                /* 23 */        "23.344556677889900112233",
                /* 24 */       "-345566778900112233.445566",
                /* 25 */        "34.45566778890112233445566",
                /* 26 */       "-4.5667788990011223344556677",
                /* 27 */        "45566778899001122334455.6677",
                /* 28 */        "567788990011223344556.6778899",
                /* 29 */       "-566778899001122.33445566778899",
                /* 30 */         "789900112233445566778899001122",
                /* 31 */      "-0.7889900112233445566778899001122"
            };

            boolean valid = true;
	    StringBuffer sb = new StringBuffer(); 
            for (int i=0; i<31; ++i)
            {
                AS400ZonedDecimal conv = new AS400ZonedDecimal(ds[i][0], ds[i][1]);
                double actual = conv.toDouble(data[i]);
                double expected = Double.valueOf(expectedValue[i]).doubleValue();

                double difference = Math.abs(actual - expected);
                if (((difference/actual) > 0.00000000001d)
		    || ((difference/expected) > 0.00000000001d)) {
		    sb.append("Got        "+actual+"\n");
		    sb.append("Expected   "+expected+"\n");
		    sb.append("Difference "+difference+"\n"); 
                    valid = false;
		}
            }

            assertCondition(valid, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call toDouble(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var020()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(15, 5);
        try
        {
            double obj = conv.toDouble(new byte[3]);
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
     Test: Call toDouble(byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var021()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(15, 5);
        try
        {
            double obj = conv.toDouble(null);
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
     Test: Call toDouble(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
     Result: double return with valid value.
     **/
    public void Var022()
    {
        try
        {
            byte[] data =
            {
                (byte)0xF8, (byte)0xF9,
                (byte)0xF1, (byte)0xD2,
                (byte)0xF5, (byte)0xF7
            };

            int[][] ds =
            {
                {2,2},
                {2,0},
                {2,2}
            };

            String[] expectedValue =
            {
                "0.89",
                "-12",
                "0.57"
            };

            boolean valid = true;
            for (int i=0; i<3; ++i)
            {
                AS400ZonedDecimal conv = new AS400ZonedDecimal(ds[i][0], ds[i][1]);
                double actual = conv.toDouble(data, i*2);
                double expected = Double.valueOf(expectedValue[i]).doubleValue();

                double difference = Math.abs(actual - expected);
                if (((difference/actual) > 0.00000000001d)
                    || ((difference/expected) > 0.00000000001d))
                    valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call toDouble(byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var023()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(15, 5);
        try
        {
            double obj = conv.toDouble(new byte[3], 0);
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
     Test: Call toDouble(byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var024()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(15, 5);
        try
        {
            double obj = conv.toDouble(new byte[15], 10);
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
     Test: Call toDouble(byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var025()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(15, 5);
        try
        {
            double obj = conv.toDouble(new byte[15], -10);
            failed("no exception thrown for negative value"+obj);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ArrayIndexOutOfBoundsException", "-10"))
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
     Test: Call toDouble(byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var026()
    {
        AS400ZonedDecimal conv = new AS400ZonedDecimal(15, 5);
        try
        {
            double obj = conv.toDouble(null, 0);
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
}
