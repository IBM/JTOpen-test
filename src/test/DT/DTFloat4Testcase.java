///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTFloat4Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400Float4;

import test.Testcase;

/**
 Testcase DTFloat4Testcase.
 **/
public class DTFloat4Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTFloat4Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Test: Construct an AS400Float4 object.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Float4 conv = new AS400Float4();
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
            AS400Float4 conv = new AS400Float4();
            AS400Float4 clone = (AS400Float4)conv.clone();
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
            AS400Float4 conv = new AS400Float4();
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
     Result: A Float object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
        try
        {
            AS400Float4 conv = new AS400Float4();
            Object ret = conv.getDefaultValue();

            if (ret instanceof Float)
            {
                Float fRet = (Float)ret;
                if (fRet.floatValue() != 0.0f)
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
     Result: Four element byte array returned with a valid value.
     **/
    public void Var005()
    {
        try
        {
            AS400Float4 conv = new AS400Float4();
            byte[] data = conv.toBytes(new Float(0.5546875f));
            if (data.length == 4 &&
                data[0] == (byte)0x3F &&
                data[1] == (byte)0x0E &&
                data[2] == (byte)0x00 &&
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Float.
     Result: ClassCastException thrown.
     **/
    public void Var006()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            byte[] ret = conv.toBytes(new Long(0));
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
    public void Var007()
    {
        AS400Float4 conv = new AS400Float4();
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
     Test: Call toBytes(float) with valid floatValue parameter.
     Result: Four element byte array returned with a valid value.
     **/
    public void Var008()
    {
        try
        {
            AS400Float4 conv = new AS400Float4();
            byte[] data = conv.toBytes(0.5546875f);
            if (data.length == 4 &&
                data[0] == (byte)0x3F &&
                data[1] == (byte)0x0E &&
                data[2] == (byte)0x00 &&
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
     Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var009()
    {
        try
        {
            float[] testValue =
            {
                -1.5f,
                1.00000011920928955078125f,
                8589934592.0f,
                -68.0f,
                0.0625f,
                -2.087890625f
            };

            byte[][] expectedValue =
            {
                {(byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0x80, (byte)0x00, (byte)0x01},
                {(byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC2, (byte)0x88, (byte)0x00, (byte)0x00},
                {(byte)0x3D, (byte)0x80, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x05, (byte)0xA0, (byte)0x00}
            };

            boolean valid = true;
            AS400Float4 conv = new AS400Float4();
            byte[] data = new byte[4];

            for (int i=0; i < 6; ++i)
            {
                int ret = conv.toBytes(new Float(testValue[i]), data);

                if (ret != 4)
                {
                    valid = false;
                }
                if (data[0] != expectedValue[i][0] ||
                    data[1] != expectedValue[i][1] ||
                    data[2] != expectedValue[i][2] ||
                    data[3] != expectedValue[i][3])
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
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Float(0.0f), new byte[0]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Float.
     Result: ClassCastException thrown.
     **/
    public void Var011()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Long(0), new byte[10]);
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
    public void Var012()
    {
        AS400Float4 conv = new AS400Float4();
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
    public void Var013()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Float(0.0f), null);
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
     Test: Call toBytes(float, byte[]) with valid parameters.
     Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var014()
    {
        try
        {
            float[] testValue =
            {
                -1.5f,
                1.00000011920928955078125f,
                8589934592.0f,
                -68.0f,
                0.0625f,
                -2.087890625f
            };

            byte[][] expectedValue =
            {
                {(byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0x80, (byte)0x00, (byte)0x01},
                {(byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC2, (byte)0x88, (byte)0x00, (byte)0x00},
                {(byte)0x3D, (byte)0x80, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x05, (byte)0xA0, (byte)0x00}
            };

            boolean valid = true;
            AS400Float4 conv = new AS400Float4();
            byte[] data = new byte[4];

            for (int i=0; i < 6; ++i)
            {
                int ret = conv.toBytes(testValue[i], data);

                if (ret != 4)
                {
                    valid = false;
                }
                if (data[0] != expectedValue[i][0] ||
                    data[1] != expectedValue[i][1] ||
                    data[2] != expectedValue[i][2] ||
                    data[3] != expectedValue[i][3])
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
     Test: Call toBytes(float, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var015()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(0.0f, new byte[1]);
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
     Test: Call toBytes(float, byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var016()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(0.0f, null);
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
     Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var017()
    {
        try
        {
            boolean valid = true;
            AS400Float4 conv = new AS400Float4();
            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(new Float(0.0f), data, 0);
            if (ret != 4)
            {
                valid = false;
            }
            if (data[0] != 0 || data[1] != 0 || data[2] != 0 || data[3] != 0)
            {
                valid = false;
            }
            for (int i = 4; i < 12; ++i)
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
            ret = conv.toBytes(new Float(0.0f), data, 5);
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
            if (data[5] != 0 || data[6] != 0 || data[7] != 0 || data[8] != 0)
            {
                valid = false;
            }
            for (int i = 9; i < 12; ++i)
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
            ret = conv.toBytes(new Float(0.0f), data, 8);
            if (ret != 4)
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
            if (data[8] != 0 || data[9] != 0 || data[10] != 0 || data[11] != 0)
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
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Float(0.0f), new byte[3], 0);
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
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Float(0.0f), new byte[7], 6);
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
    public void Var020()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Float(0.0f), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Float.
     Result: ClassCastException thrown.
     **/
    public void Var021()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Long(0), new byte[4], 0);
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
    public void Var022()
    {
        AS400Float4 conv = new AS400Float4();
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
    public void Var023()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(new Float(0.0f), null, 0);
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
     Test: Call toBytes(float, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var024()
    {
        try
        {
            boolean valid = true;
            AS400Float4 conv = new AS400Float4();
            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(0.0f, data, 0);
            if (ret != 4)
            {
                valid = false;
            }
            if (data[0] != 0 || data[1] != 0 || data[2] != 0 || data[3] != 0)
            {
                valid = false;
            }
            for (int i = 4; i < 12; ++i)
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
            ret = conv.toBytes(0.0f, data, 5);
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
            if (data[5] != 0 || data[6] != 0 || data[7] != 0 || data[8] != 0)
            {
                valid = false;
            }
            for (int i = 9; i < 12; ++i)
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
            ret = conv.toBytes(0.0f, data, 8);
            if (ret != 4)
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
            if (data[8] != 0 || data[9] != 0 || data[10] != 0 || data[11] != 0)
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
     Test: Call toBytes(float, byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var025()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(0.0f, new byte[2], 0);
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
     Test: Call toBytes(float, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(0.0f, new byte[7], 6);
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
     Test: Call toBytes(float, byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(0.0f, new byte[10], -1);
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
     Test: Call toBytes(float, byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var028()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            int ret = conv.toBytes(0.0f, null, 0);
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
     Result: Float object returned with valid value.
     **/
    public void Var029()
    {
        try
        {
            byte[][] data = 
            {
                {(byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0x80, (byte)0x00, (byte)0x01},
                {(byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC2, (byte)0x88, (byte)0x00, (byte)0x00},
                {(byte)0x3D, (byte)0x80, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x05, (byte)0xA0, (byte)0x00}
            };

            float[] expectedValue =
            {
                -1.5f,
                1.00000011920928955078125f,
                8589934592.0f,
                -68.0f,
                0.0625f,
                -2.087890625f
            };

            boolean valid = true;
            AS400Float4 conv = new AS400Float4();

            for (int i = 0; i < 6; ++i)
            {
                Object obj = conv.toObject(data[i]);

                if (obj instanceof Float)
                {
                    Float fvalue = (Float)obj;
                    float dataValue = fvalue.floatValue();
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
        AS400Float4 conv = new AS400Float4();
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
    public void Var031()
    {
        AS400Float4 conv = new AS400Float4();
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
     Result: Float object return with valid value.
     **/
    public void Var032()
    {
        try
        {
            byte[] data =
            {
                (byte)0x3F, (byte)0xE0,	(byte)0x00, (byte)0x00,
                (byte)0xC8, (byte)0x80, (byte)0x00, (byte)0x00,
                (byte)0x3C, (byte)0x00, (byte)0x00, (byte)0x00
            };

            float[] expectedValue =
            {
                1.75f,
                -262144.0f,
                0.0078125f
            };

            boolean valid = true;
            AS400Float4 conv = new AS400Float4();

            for (int i=0; i<3; ++i)
            {
                Object obj = conv.toObject(data, i*4);

                if (obj instanceof Float)
                {
                    Float fvalue = (Float)obj;
                    float dataValue = fvalue.floatValue();
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
        AS400Float4 conv = new AS400Float4();
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
    public void Var034()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            Object obj = conv.toObject(new byte[6], 4);
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
    public void Var035()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            Object obj = conv.toObject(new byte[5], -10);
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
    public void Var036()
    {
        AS400Float4 conv = new AS400Float4();
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
     Test: Call toFloat(byte[]) with valid as400Value parameters.
     Result: float returned with valid value.
     **/
    public void Var037()
    {
        try
        {
            byte[][] data = 
            {
                {(byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00},
                {(byte)0x3F, (byte)0x80, (byte)0x00, (byte)0x01},
                {(byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00},
                {(byte)0xC2, (byte)0x88, (byte)0x00, (byte)0x00},
                {(byte)0x3D, (byte)0x80, (byte)0x00, (byte)0x00},
                {(byte)0xC0, (byte)0x05, (byte)0xA0, (byte)0x00}
            };

            float[] expectedValue =
            {
                -1.5f,
                1.00000011920928955078125f,
                8589934592.0f,
                -68.0f,
                0.0625f,
                -2.087890625f
            };

            boolean valid = true;
            AS400Float4 conv = new AS400Float4();

            for (int i = 0; i < 6; ++i)
            {
                float dataValue = conv.toFloat(data[i]);

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
     Test: Call toFloat(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var038()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            float f = conv.toFloat(new byte[3]);
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
     Test: Call toFloat(byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var039()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            float f = conv.toFloat(null);
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
     Test: Call toFloat(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
     Result: float returned with valid value.
     **/
    public void Var040()
    {
        try
        {
            byte[] data =
            {
                (byte)0x3F, (byte)0xE0,	(byte)0x00, (byte)0x00,
                (byte)0xC8, (byte)0x80, (byte)0x00, (byte)0x00,
                (byte)0x3C, (byte)0x00, (byte)0x00, (byte)0x00
            };

            float[] expectedValue =
            {
                1.75f,
                -262144.0f,
                0.0078125f
            };

            boolean valid = true;
            AS400Float4 conv = new AS400Float4();

            for (int i=0; i<3; ++i)
            {
                float dataValue = conv.toFloat(data, i*4);

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
     Test: Call toFloat(byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var041()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            float f = conv.toFloat(new byte[3], 0);
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
     Test: Call toFloat(byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var042()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            float f = conv.toFloat(new byte[6], 4);
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
     Test: Call toFloat(byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var043()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            float f = conv.toFloat(new byte[5], -10);
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
     Test: Call toFloat(byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var044()
    {
        AS400Float4 conv = new AS400Float4();
        try
        {
            float f = conv.toFloat(null, 0);
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



