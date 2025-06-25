///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTFloat8Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400Float8;

import test.Testcase;

/**
 Testcase DTFloat8Testcase.
 **/
public class DTFloat8Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTFloat8Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Test: Construct an AS400Float8 object.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Float8 conv = new AS400Float8();
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
            AS400Float8 conv = new AS400Float8();
            AS400Float8 clone = (AS400Float8)conv.clone();
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
            AS400Float8 conv = new AS400Float8();
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
     Result: A Double object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
        try
        {
            AS400Float8 conv = new AS400Float8();
            Object ret = conv.getDefaultValue();

            if (ret instanceof Double)
            {
                Double dRet = (Double)ret;
                if (dRet.doubleValue() != 0.0)
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
                failed("Incorrect return type");
            }
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call toBytes(Object) with valid javaValue parameter.
     Result: Eight element byte array returned with a valid value.
     **/
    public void Var005()
    {
        try
        {
            AS400Float8 conv = new AS400Float8();
            byte[] data = conv.toBytes(Double.valueOf(0.5546875));
            if (data.length == 8 &&
                data[0] == (byte)0x3F &&
                data[1] == (byte)0xE1 &&
                data[2] == (byte)0xC0 &&
                data[3] == (byte)0x00 &&
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Double.
     Result: ClassCastException thrown.
     **/
    public void Var006()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            byte[] ret = conv.toBytes(Long.valueOf(0));
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var007()
    {
        AS400Float8 conv = new AS400Float8();
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
     Test: Call toBytes(double) with valid doubleValue parameter.
     Result: Eight element byte array returned with a valid value.
     **/
    public void Var008()
    {
        try
        {
            AS400Float8 conv = new AS400Float8();
            byte[] data = conv.toBytes(0.5546875);
            if (data.length == 8 &&
                data[0] == (byte)0x3F &&
                data[1] == (byte)0xE1 &&
                data[2] == (byte)0xC0 &&
                data[3] == (byte)0x00 &&
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
     Test: Call toBytes(Object, byte[]) with valid parameters.
     Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var009()
    {
        try
        {
            double[] testValue =
            {
                -1.5,
                1.00000011920928955078125,
                8589934592.0,
                -68.0,
                0.0625,
                -2.087890625
            };

            byte[][] expectedValue =
            {
                {(byte)0xBF, (byte)0xF8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x42, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x51, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x00, (byte)0xB4, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
            };

            boolean valid = true;
            AS400Float8 conv = new AS400Float8();
            byte[] data = new byte[8];

            for (int i=0; i < 6; ++i)
            {
                int ret = conv.toBytes(Double.valueOf(testValue[i]), data);

                if (ret != 8)
                {
                    valid = false;
                }
                if (data[0] != expectedValue[i][0] ||
                    data[1] != expectedValue[i][1] ||
                    data[2] != expectedValue[i][2] ||
                    data[3] != expectedValue[i][3] ||
                    data[4] != expectedValue[i][4] ||
                    data[5] != expectedValue[i][5] ||
                    data[6] != expectedValue[i][6] ||
                    data[7] != expectedValue[i][7])
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var010()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Double.valueOf(0.0), new byte[7]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Double.
     Result: ClassCastException thrown.
     **/
    public void Var011()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Long.valueOf(0), new byte[10]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var012()
    {
        AS400Float8 conv = new AS400Float8();
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
    public void Var013()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Double.valueOf(0.0), null);
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
     Test: Call toBytes(double, byte[]) with valid parameters.
     Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var014()
    {
        try
        {
            double[] testValue =
            {
                -1.5,
                1.00000011920928955078125,
                8589934592.0,
                -68.0,
                0.0625,
                -2.087890625
            };

            byte[][] expectedValue =
            {
                {(byte)0xBF, (byte)0xF8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x42, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x51, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x00, (byte)0xB4, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
            };

            boolean valid = true;
            AS400Float8 conv = new AS400Float8();
            byte[] data = new byte[8];

            for (int i=0; i < 6; ++i)
            {
                int ret = conv.toBytes(testValue[i], data);

                if (ret != 8)
                {
                    valid = false;
                }
                if (data[0] != expectedValue[i][0] ||
                    data[1] != expectedValue[i][1] ||
                    data[2] != expectedValue[i][2] ||
                    data[3] != expectedValue[i][3] ||
                    data[4] != expectedValue[i][4] ||
                    data[5] != expectedValue[i][5] ||
                    data[6] != expectedValue[i][6] ||
                    data[7] != expectedValue[i][7])
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
     Test: Call toBytes(double, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var015()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(0.0, new byte[7]);
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
     Test: Call toBytes(double, byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var016()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(0.0, null);
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
     Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var017()
    {
        try
        {
            boolean valid = true;
            AS400Float8 conv = new AS400Float8();
            byte[] data = new byte[24];

            for (int i = 0; i < 24; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(Double.valueOf(0.0), data, 0);
            if (ret != 8)
            {
                valid = false;
            }
            if (data[0] != 0 || data[1] != 0 || data[2] != 0 || data[3] != 0 || data[4] != 0 || data[5] != 0 || data[6] != 0 || data[7] != 0)
            {
                valid = false;
            }
            for (int i = 8; i < 24; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }

            for (int i = 0; i < 24; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(Double.valueOf(0.0), data, 5);
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
            if (data[5] != 0 || data[6] != 0 || data[7] != 0 || data[8] != 0 || data[9] != 0 || data[10] != 0 || data[11] != 0 || data[12] != 0)
            {
                valid = false;
            }
            for (int i = 13; i < 24; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }

            for (int i = 0; i < 24; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(Double.valueOf(0.0), data, 16);
            if (ret != 8)
            {
                valid = false;
            }
            for (int i = 0; i <= 15; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[16] != 0 || data[17] != 0 || data[18] != 0 || data[19] != 0 || data[20] != 0 || data[21] != 0 || data[22] != 0 || data[23] != 0)
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
    public void Var018()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Double.valueOf(0.0), new byte[7], 0);
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
    public void Var019()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Double.valueOf(0.0), new byte[10], 4);
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
    public void Var020()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Double.valueOf(0.0), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Double.
     Result: ClassCastException thrown.
     **/
    public void Var021()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Long.valueOf(0), new byte[10], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var022()
    {
        AS400Float8 conv = new AS400Float8();
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
    public void Var023()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(Double.valueOf(0.0), null, 0);
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
     Test: Call toBytes(double, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: Eight is returned, valid bytes are written in the array.
     **/
    public void Var024()
    {
        try
        {
            boolean valid = true;
            AS400Float8 conv = new AS400Float8();
            byte[] data = new byte[24];

            for (int i = 0; i < 24; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(0.0, data, 0);
            if (ret != 8)
            {
                valid = false;
            }
            if (data[0] != 0 || data[1] != 0 || data[2] != 0 || data[3] != 0 || data[4] != 0 || data[5] != 0 || data[6] != 0 || data[7] != 0)
            {
                valid = false;
            }
            for (int i = 8; i < 24; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }

            for (int i = 0; i < 24; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(0.0, data, 5);
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
            if (data[5] != 0 || data[6] != 0 || data[7] != 0 || data[8] != 0 || data[9] != 0 || data[10] != 0 || data[11] != 0 || data[12] != 0)
            {
                valid = false;
            }
            for (int i = 13; i < 24; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }

            for (int i = 0; i < 24; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(0.0, data, 16);
            if (ret != 8)
            {
                valid = false;
            }
            for (int i = 0; i <= 15; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[16] != 0 || data[17] != 0 || data[18] != 0 || data[19] != 0 || data[20] != 0 || data[21] != 0 || data[22] != 0 || data[23] != 0)
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
    public void Var025()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(0.0, new byte[6], 0);
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(0.0, new byte[9], 3);
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(0.0, new byte[10], -2);
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
     Test: Call toBytes(double, byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var028()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            int ret = conv.toBytes(0.0, null, 0);
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
     Result: Double object returned with valid value.
     **/
    public void Var029()
    {
        try
        {
            byte[][] data = 
            {
                {(byte)0xBF, (byte)0xF8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x42, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x51, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x00, (byte)0xB4, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
            };

            double[] expectedValue =
            {
                -1.5,
                1.00000011920928955078125,
                8589934592.0,
                -68.0,
                0.0625,
                -2.087890625
            };

            boolean valid = true;
            AS400Float8 conv = new AS400Float8();

            for (int i = 0; i < 6; ++i)
            {
                Object obj = conv.toObject(data[i], 0);

                if (obj instanceof Double)
                {
                    Double dvalue = (Double)obj;
                    double dataValue = dvalue.doubleValue();
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
     Test: Call toObject(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var030()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            Object obj = conv.toObject(new byte[5]);
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
    public void Var031()
    {
        AS400Float8 conv = new AS400Float8();
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
     Result: Double object return with valid value.
     **/
    public void Var032()
    {
        try
        {
            byte[] data =
            {
                (byte)0x3F, (byte)0xFC, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xC1, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x3F, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
            };

            double[] expectedValue =
            {
                1.75,
                -262144.0,
                0.0078125
            };

            boolean valid = true;
            AS400Float8 conv = new AS400Float8();

            for (int i=0; i<3; ++i)
            {
                Object obj = conv.toObject(data, i*8);

                if (obj instanceof Double)
                {
                    Double dvalue = (Double)obj;
                    double dataValue = dvalue.doubleValue();
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
    public void Var033()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            Object obj = conv.toObject(new byte[7], 0);
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
    public void Var034()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            Object obj = conv.toObject(new byte[12], 5);
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
    public void Var035()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            Object obj = conv.toObject(new byte[12], -10);
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
    public void Var036()
    {
        AS400Float8 conv = new AS400Float8();
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
     Test: Call toDouble(byte[]) with valid as400Value parameters.
     Result: double returned with valid value.
     **/
    public void Var037()
    {
        try
        {
            byte[][] data = 
            {
                {(byte)0xBF, (byte)0xF8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x42, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x51, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0xB0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x00, (byte)0xB4, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
            };

            double[] expectedValue =
            {
                -1.5,
                1.00000011920928955078125,
                8589934592.0,
                -68.0,
                0.0625,
                -2.087890625
            };

            boolean valid = true;
            AS400Float8 conv = new AS400Float8();

            for (int i = 0; i < 6; ++i)
            {
                double dataValue = conv.toDouble(data[i], 0);
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
     Test: Call toDouble(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var038()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            double d = conv.toDouble(new byte[7]);
            failed("no exception thrown for to small array"+d);
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
    public void Var039()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            double d = conv.toDouble(null);
            failed("no exception thrown for null pointer"+d);
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
     Result: double returned with valid value.
     **/
    public void Var040()
    {
        try
        {
            byte[] data =
            {
                (byte)0x3F, (byte)0xFC, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xC1, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x3F, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
            };

            double[] expectedValue =
            {
                1.75,
                -262144.0,
                0.0078125
            };

            boolean valid = true;
            AS400Float8 conv = new AS400Float8();

            for (int i=0; i<3; ++i)
            {
                double dataValue = conv.toDouble(data, i*8);

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
     Test: Call toDouble(byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var041()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            double d = conv.toDouble(new byte[7], 0);
            failed("no exception thrown for to small array"+d);
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
    public void Var042()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            double d = conv.toDouble(new byte[12], 5);
            failed("no exception thrown for not enough space"+d);
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
    public void Var043()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            double d = conv.toDouble(new byte[12], -10);
            failed("no exception thrown for negative value"+d);
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
     Test: Call toDouble(byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var044()
    {
        AS400Float8 conv = new AS400Float8();
        try
        {
            double d = conv.toDouble(null, 0);
            failed("no exception thrown for null pointer"+d);
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



