///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTPackedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

// OCO Source Materials
// 5722-JC1 5769-JC1
// (C) Copyright IBM Corp. 1997, 2005
//
// The source code for this program is not published or otherwise
// divested of its trade secrets, irrespective of what has been
// deposited with the U.S. Copyright Office.
//
////////////////////////////////////////////////////////////////////////
//
// File Name:  DTPackedTestcase.java
//
// Class Name:  DTPackedTestcase
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
//
// Release  Date  User ID  Comments
//                         Created.
//
// END CHANGE ACTIVITY
//
////////////////////////////////////////////////////////////////////////

package test.DT;

import java.math.BigDecimal;
import com.ibm.as400.access.AS400PackedDecimal;

import test.Testcase;

/**
 Testcase DTPackedTestcase.
 **/
public class DTPackedTestcase extends Testcase
{
    /**
     Test: Construct an AS400PackedDecimal object with valid parameters.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            for (int x = 1; x <= 31; ++x)
            {
                for (int y = 0; y <= x; ++y)
                {
                    AS400PackedDecimal conv = new AS400PackedDecimal(x, y);
                }
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Construct an AS400PackedDecimal object with invalid parameters: numDigits is less than one.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var002()
    {
        try
        {
            AS400PackedDecimal conv = new AS400PackedDecimal(0, 0);
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
     Test: Construct an AS400PackedDecimal object with invalid parameters: numDigits is greater than 31.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var003()
    {
        try
        {
            //	    AS400PackedDecimal conv = new AS400PackedDecimal(32, 0);
            AS400PackedDecimal conv = new AS400PackedDecimal(64, 0);
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
     Test: Construct an AS400PackedDecimal object with invalid parameters: numDecimalPositions is less than zero.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var004()
    {
        try
        {
            AS400PackedDecimal conv = new AS400PackedDecimal(16, -1);
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
     Test: Construct an AS400PackedDecimal object with invalid parameters: numDecimalPositions is greater than numDigits
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var005()
    {
        try
        {
            AS400PackedDecimal conv = new AS400PackedDecimal(16, 17);
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
     Test: Call clone
     Result: Cloned object returned.
     **/
    public void Var006()
    {
        try
        {
            AS400PackedDecimal conv = new AS400PackedDecimal(10, 5);
            AS400PackedDecimal clone = (AS400PackedDecimal)conv.clone();
            if (clone != null && clone != conv && clone.getNumberOfDecimalPositions() == 5)
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
    public void Var007()
    {
        try
        {
            boolean valid = true;

            int[] retValue =
            {
                1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12,12,13,13,14,14,15,15,16,16
            };

            for (int x=1; x <= 31; ++x)
            {
                for (int y=0; y <=x; ++y)
                {
                    AS400PackedDecimal conv = new AS400PackedDecimal(x, y);
                    int ret = conv.getByteLength();
                    if (ret != retValue[x-1])
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
     Test: Call getDefaultValue
     Result: An BigDecimal object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var008()
    {
        try
        {
            AS400PackedDecimal conv = new AS400PackedDecimal(1,0);
            Object ret = conv.getDefaultValue();

            if (ret instanceof BigDecimal)
            {
                BigDecimal dobj = (BigDecimal)ret;
                String dataValue = dobj.toString();
                if (dataValue.compareTo("0") != 0)
                {
                    failed("Incorrect return value");
                }
                else
                {
                    byte[] data = conv.toBytes(ret);
                    if (data.length == 1 && data[0] == (byte)0x0F)
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
                failed("Incorrect return type");
            }
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call getNumberOfDigits.
     Result: No exception should be thrown.  Correct number of digits returned.
     **/
    public void Var009()
    {
        try
        {
            boolean valid = true;

            for (int x=1; x <= 31; ++x)
            {
                for (int y=0; y <=x; ++y)
                {
                    AS400PackedDecimal conv = new AS400PackedDecimal(x, y);
                    int dig = conv.getNumberOfDigits();
                    if (dig != x)
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
     Test: Call getNumberOfDecimalPositions.
     Result: No exception should be thrown.  Correct number of decimal digits returned.
     **/
    public void Var010()
    {
        try
        {
            boolean valid = true;

            for (int x=1; x <= 31; ++x)
            {
                for (int y=0; y <=x; ++y)
                {
                    AS400PackedDecimal conv = new AS400PackedDecimal(x, y);
                    int dec = conv.getNumberOfDecimalPositions();
                    if (dec != y)
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
     Test: Call toBytes(Object) with valid javaValue parameter.
     Result: Byte array returned with a valid value.
     **/
    public void Var011()
    {
        try
        {
            AS400PackedDecimal conv = new AS400PackedDecimal(8, 4);
            byte[] data = conv.toBytes(new BigDecimal("2468.9753"));
            if (data.length == 5 &&
                data[0] == (byte)0x02 &&
                data[1] == (byte)0x46 &&
                data[2] == (byte)0x89 &&
                data[3] == (byte)0x75 &&
                data[4] == (byte)0x3F)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of BigDecimal.
     Result: ClassCastException thrown.
     **/
    public void Var012()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            byte[] ret = conv.toBytes(new Float(0.0f));
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
     Test: Call toBytes(Object) with invalid parameters: javaValue has to many decimal places.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var013()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            byte[] ret = conv.toBytes(new BigDecimal("12.3456"));
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (12.3456): Length is not valid."))
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
     Test: Call toBytes(Object) with invalid parameters: javaValue has to many digits after scale normalization.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var014()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            byte[] ret = conv.toBytes(new BigDecimal("123.45"));
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123.45): Length is not valid."))
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
     Test: Call toBytes(Object) with invalid parameters: javaValue has to many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var015()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            byte[] ret = conv.toBytes(new BigDecimal("123.4567"));
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123.4567): Length is not valid."))
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
    public void Var016()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
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
     Test: Call toBytes(Object, byte[]) with valid parameters.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var017()
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
                {27,4},
                {28,7},
                {29,14},
                {30,0},
                {31,31}
            };

            String[] testValue =
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

            byte[][] expectedValue =
            {
                /*  1 */ {(byte)0x0F},
                /*  2 */ {(byte)0x01,(byte)0x2F},
                /*  3 */ {(byte)0x12,(byte)0x3D},
                /*  4 */ {(byte)0x01,(byte)0x23,(byte)0x4F},
                /*  5 */ {(byte)0x12,(byte)0x34,(byte)0x5D},
                /*  6 */ {(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x6F},
                /*  7 */ {(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x7F},
                /*  8 */ {(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x8D},
                /*  9 */ {(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x9F},
                /* 10 */ {(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0x0D},
                /* 11 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x1F},
                /* 12 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x2F},
                /* 13 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x3F},
                /* 14 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x4F},
                /* 15 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x34,(byte)0x5D},
                /* 16 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x6F},
                /* 17 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x7D},
                /* 18 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x8F},
                /* 19 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x9F},
                /* 20 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0x0D},
                /* 21 */ {(byte)0x11,(byte)0x22,(byte)0x33,(byte)0x44,(byte)0x55,(byte)0x66,(byte)0x77,(byte)0x88,(byte)0x99,(byte)0x00,(byte)0x1F},
                /* 22 */ {(byte)0x02,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x3D},
                /* 23 */ {(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x3F},
                /* 24 */ {(byte)0x03,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x6D},
                /* 25 */ {(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x6F},
                /* 26 */ {(byte)0x04,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x7D},
                /* 27 */ {(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x7F},
                /* 28 */ {(byte)0x05,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x9F},
                /* 29 */ {(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x9D},
                /* 30 */ {(byte)0x07,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x2F},
                /* 31 */ {(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x2D}
            };

            int[] retValue =
            {
                1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12,12,13,13,14,14,15,15,16,16
            };

            boolean valid = true;
            byte[] data = new byte[16];

            for (int i=0; i<31; ++i)
            {
                AS400PackedDecimal conv = new AS400PackedDecimal(ds[i][0], ds[i][1]);
                int ret = conv.toBytes(new BigDecimal(testValue[i]), data);

                if (ret != retValue[i])
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
     Test: Call toBytes(Object, byte[]) with valid parameters where there are less digits in the input then the output.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var018()
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
                /*  1 */	{(byte)0x00,(byte)0x02,(byte)0x3D},
                /*  2 */	{(byte)0x02,(byte)0x3D},
                /*  3 */	{(byte)0x00,(byte)0x01,(byte)0x23,(byte)0x4F},
                /*  4 */	{(byte)0x01,(byte)0x23,(byte)0x4F},
                /*  5 */	{(byte)0x01,(byte)0x23,(byte)0x40,(byte)0x0D},
                /*  6 */	{(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x0D},
                /*  7 */	{(byte)0x01,(byte)0x23,(byte)0x40,(byte)0x0F},
                /*  8 */	{(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x0F},
                /*  9 */	{(byte)0x00,(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x0D},
                /* 10 */	{(byte)0x00,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x60,(byte)0x0D},
                /* 11 */	{(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x00,(byte)0x0F},
                /* 12 */	{(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x60,(byte)0x00,(byte)0x0F},
                /* 13 */	{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x5D},
                /* 14 */	{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x5D},
                /* 15 */	{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x5F},
                /* 16 */	{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x5F},
                /* 17 */	{(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x00,(byte)0x00,(byte)0x0D},
                /* 18 */	{(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x90,(byte)0x0D},
                /* 19 */	{(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x00,(byte)0x00,(byte)0x0F},
                /* 20 */	{(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x90,(byte)0x00,(byte)0x0F},
                /* 21 */	{(byte)0x00,(byte)0x12,(byte)0x30,(byte)0x00,(byte)0x00,(byte)0x0D},
                /* 22 */	{(byte)0x00,(byte)0x12,(byte)0x34,(byte)0x50,(byte)0x00,(byte)0x0D},
                /* 23 */	{(byte)0x00,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x00,(byte)0x00,(byte)0x0F},
                /* 24 */	{(byte)0x00,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x00,(byte)0x0F}
            };

            int[] retValue =
            {
                3,2,4,3,4,4,4,4,6,6,6,6,6,6,7,7,6,6,7,7,6,6,7,7
            };

            boolean valid = true;
            byte[] data = new byte[16];

            for (int i=0; i<24; ++i)
            {
                AS400PackedDecimal conv = new AS400PackedDecimal(ds[i][0], ds[i][1]);
                int ret = conv.toBytes(new BigDecimal(testValue[i]), data);

                if (ret != retValue[i])
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
    public void Var019()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.345"), new byte[2]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of BigDecimal.
     Result: ClassCastException thrown.
     **/
    public void Var020()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new Float(0.0f), new byte[10]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue has to many decimal places.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var021()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.3456"), new byte[10]);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (12.3456): Length is not valid."))
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue has to many digits after scale normalization.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var022()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("123.45"), new byte[10]);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123.45): Length is not valid."))
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue has to many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var023()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("123.4567"), new byte[10]);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123.4567): Length is not valid."))
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
    public void Var024()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
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
    public void Var025()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.345"), null);
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
    public void Var026()
    {
        try
        {
            boolean valid = true;
            AS400PackedDecimal conv = new AS400PackedDecimal(8,3);
            BigDecimal testValue = new BigDecimal("11111.111");
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
            if (data[0] != (byte)0x01 ||
                data[1] != (byte)0x11 ||
                data[2] != (byte)0x11 ||
                data[3] != (byte)0x11 ||
                data[4] != (byte)0x1F)
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
            if (data[3] != (byte)0x01 ||
                data[4] != (byte)0x11 ||
                data[5] != (byte)0x11 ||
                data[6] != (byte)0x11 ||
                data[7] != (byte)0x1F)
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
            if (data[7]  != (byte)0x01 ||
                data[8]  != (byte)0x11 ||
                data[9]  != (byte)0x11 ||
                data[10] != (byte)0x11 ||
                data[11] != (byte)0x1F)
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
    public void Var027()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.345"), new byte[2], 0);
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
    public void Var028()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.345"), new byte[7], 5);
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
    public void Var029()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.345"), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of BigDecimal.
     Result: ClassCastException thrown.
     **/
    public void Var030()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new Float(0.0f), new byte[10], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue has to many decimal places.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var031()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.3456"), new byte[10], 0);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (12.3456): Length is not valid."))
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue has to many digits after scale normalization.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var032()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("123.45"), new byte[10], 0);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123.45): Length is not valid."))
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue has to many digits.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var033()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("123.4567"), new byte[10], 0);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123.4567): Length is not valid."))
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
    public void Var034()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
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
    public void Var035()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(5,3);
        try
        {
            int ret = conv.toBytes(new BigDecimal("12.345"), null, 0);
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
     Result: BigDecimal object returned with valid value.
     **/
    public void Var036()
    {
        try
        {
            byte[][] data =
            {
                /*  1 */ {(byte)0x0F},
                /*  2 */ {(byte)0x01,(byte)0x2E},
                /*  3 */ {(byte)0x12,(byte)0x3D},
                /*  4 */ {(byte)0x01,(byte)0x23,(byte)0x4C},
                /*  5 */ {(byte)0x12,(byte)0x34,(byte)0x5B},
                /*  6 */ {(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x6A},
                /*  7 */ {(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x7A},
                /*  8 */ {(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x8B},
                /*  9 */ {(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x9C},
                /* 10 */ {(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0x0D},
                /* 11 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x1E},
                /* 12 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x2F},
                /* 13 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x3A},
                /* 14 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x4A},
                /* 15 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x34,(byte)0x5B},
                /* 16 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x6A},
                /* 17 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x7B},
                /* 18 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x8C},
                /* 19 */ {(byte)0x98,(byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10,(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x9A},
                /* 20 */ {(byte)0x09,(byte)0x87,(byte)0x65,(byte)0x43,(byte)0x21,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0x0B},
                /* 21 */ {(byte)0x11,(byte)0x22,(byte)0x33,(byte)0x44,(byte)0x55,(byte)0x66,(byte)0x77,(byte)0x88,(byte)0x99,(byte)0x00,(byte)0x1C},
                /* 22 */ {(byte)0x02,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x3D},
                /* 23 */ {(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x3A},
                /* 24 */ {(byte)0x03,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x6B},
                /* 25 */ {(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x6C},
                /* 26 */ {(byte)0x04,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x7D},
                /* 27 */ {(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x7E},
                /* 28 */ {(byte)0x05,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x9A},
                /* 29 */ {(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x9B},
                /* 30 */ {(byte)0x07,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x2C},
                /* 31 */ {(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x23,(byte)0x34,(byte)0x45,(byte)0x56,(byte)0x67,(byte)0x78,(byte)0x89,(byte)0x90,(byte)0x01,(byte)0x12,(byte)0x2D}
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
                /*  1 */	"0",
                /*  2 */	"1.2",
                /*  3 */	"-0.123",
                /*  4 */	"12.34",
                /*  5 */	"-12.345",
                /*  6 */	"12345.6",
                /*  7 */	"1.234567",
                /*  8 */	"-1234.5678",
                /*  9 */	"1234567.89",
                /* 10 */	"-0.1234567890",
                /* 11 */	"98765432101",
                /* 12 */	"987654.321012",
                /* 13 */	"987654.3210123",
                /* 14 */	"987654.32101234",
                /* 15 */	"-98765.4321012345",
                /* 16 */	"9876543.210123456",
                /* 17 */	"-98765432101234.567",
                /* 18 */	"9876543210123456.78",
                /* 19 */	"987654321012345678.9",
                /* 20 */	"-98765.432101234567890",
                /* 21 */	"1122334455667788.99001",
                /* 22 */	"-234455667788990011.2233",
                /* 23 */	"23.344556677889900112233",
                /* 24 */	"-345566778900112233.445566",
                /* 25 */	"34.45566778890112233445566",
                /* 26 */	"-4.5667788990011223344556677",
                /* 27 */	"45566778899001122334455.6677",
                /* 28 */	"567788990011223344556.6778899",
                /* 29 */	"-566778899001122.33445566778899",
                /* 30 */	 "789900112233445566778899001122",
                /* 31 */	"-0.7889900112233445566778899001122"
            };

            boolean valid = true;
            for (int i=0; i<31; ++i)
            {
                AS400PackedDecimal conv = new AS400PackedDecimal(ds[i][0], ds[i][1]);
                Object obj = conv.toObject(data[i], 0);

                if (obj instanceof BigDecimal)
                {
                    BigDecimal dobj = (BigDecimal)obj;
                    String dataValue = dobj.toString();
                    if (dataValue.compareTo(expectedValue[i]) != 0)
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
     Test: Call toObject(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var037()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(15, 5);
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
    public void Var038()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(15, 5);
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
     Result: BigDecimal object return with valid value.
     **/
    public void Var039()
    {
        try
        {
            byte[] data =
            {
                (byte)0x08,
                (byte)0x9F,
                (byte)0x12,
                (byte)0x3D,
                (byte)0x57,
                (byte)0x1F
            };

            int[][] ds =
            {
                {2,2},
                {3,0},
                {3,3}
            };

            String[] expectedValue =
            {
                "0.89",
                "-123",
                "0.571"
            };

            boolean valid = true;
            for (int i=0; i<3; ++i)
            {
                AS400PackedDecimal conv = new AS400PackedDecimal(ds[i][0], ds[i][1]);
                Object obj = conv.toObject(data, i*2);

                if (obj instanceof BigDecimal)
                {
                    BigDecimal dobj = (BigDecimal)obj;
                    String dataValue = dobj.toString();
                    if (dataValue.compareTo(expectedValue[i]) != 0)
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
    public void Var040()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(15, 5);
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
     Test: Call toObject(byte[], int) with invalid parameters: startingPoint to close to end of buffer
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var041()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(15, 5);
        try
        {
            Object obj = conv.toObject(new byte[15], 10);
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
    public void Var042()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(15, 5);
        try
        {
            Object obj = conv.toObject(new byte[15], -3);
            failed("no exception thrown for negative value");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ArrayIndexOutOfBoundsException", "-3"))
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
    public void Var043()
    {
        AS400PackedDecimal conv = new AS400PackedDecimal(15, 5);
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



