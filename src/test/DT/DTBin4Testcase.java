///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTBin4Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400Bin4;

import test.Testcase;

/**
 Testcase DTBin4Testcase.
 **/
public class DTBin4Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTBin4Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Test: Construct an AS400Bin4 object.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Bin4 conv = new AS400Bin4();
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
            AS400Bin4 conv = new AS400Bin4();
            AS400Bin4 clone = (AS400Bin4)conv.clone();
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
            AS400Bin4 conv = new AS400Bin4();
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
     Result: An Integer object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
        try
        {
            AS400Bin4 conv = new AS400Bin4();
            Object ret = conv.getDefaultValue();

            if (ret instanceof Integer)
            {
                Integer intRet = (Integer)ret;
                if (intRet.intValue() != 0)
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
            AS400Bin4 conv = new AS400Bin4();
            byte[] data = conv.toBytes(new Integer(-22216467));
            if (data.length == 4 &&
                data[0] == (byte)0xFE &&
                data[1] == (byte)0xAD &&
                data[2] == (byte)0x00 &&
                data[3] == (byte)0xED)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Integer.
     Result: ClassCastException thrown.
     **/
    public void Var006()
    {
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();

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
     Test: Call toBytes(int) with valid intValue parameter.
     Result: Four element byte array returned with a valid value.
     **/
    public void Var008()
    {
        try
        {
            AS400Bin4 conv = new AS400Bin4();
            byte[] data = conv.toBytes(-22216467);
            if (data.length == 4 &&
                data[0] == (byte)0xFE &&
                data[1] == (byte)0xAD &&
                data[2] == (byte)0x00 &&
                data[3] == (byte)0xED)
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
            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();
            byte[] data = new byte[4];

            int[] testValue =
            {
                0x00000000,
                0x00000080,
                0x00008000,
                0x00008080,
                0x00800000,
                0x00800080,
                0x00808000,
                0x00808080,
                0x80000000,
                0x80000080,
                0x80008000,
                0x80008080,
                0x80800000,
                0x80800080,
                0x80808000,
                0x80808080
            };
            int i = 0;

            for (int w = 0x00; w <= 0xFF; w+=0x80)
            {
                for (int x = 0x00; x <= 0xFF; x+=0x80)
                {
                    for (int y = 0x00; y <= 0xFF; y+=0x80)
                    {
                        for (int z = 0x00; z <= 0xFF; z+=0x80)
                        {
                            int ret = conv.toBytes(new Integer(testValue[i++]), data);

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
    public void Var010()
    {
        AS400Bin4 conv = new AS400Bin4();

        try
        {
            int ret = conv.toBytes(new Integer(0), new byte[0]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Integer.
     Result: ClassCastException thrown.
     **/
    public void Var011()
    {
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(new Integer(0), null);
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
     Test: Call toBytes(int, byte[]) with valid parameters.
     Result: Four is returned, valid bytes are written in the array.
     **/
    public void Var014()
    {
        try
        {
            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();
            byte[] data = new byte[4];

            int[] testValue =
            {
                0x00000000,
                0x00000080,
                0x00008000,
                0x00008080,
                0x00800000,
                0x00800080,
                0x00808000,
                0x00808080,
                0x80000000,
                0x80000080,
                0x80008000,
                0x80008080,
                0x80800000,
                0x80800080,
                0x80808000,
                0x80808080
            };
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
     Test: Call toBytes(int, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var015()
    {
        AS400Bin4 conv = new AS400Bin4();
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
     Test: Call toBytes(int, byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var016()
    {
        AS400Bin4 conv = new AS400Bin4();
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
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var017()
    {
        try
        {
            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();
            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(new Integer(0), data, 0);
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
            ret = conv.toBytes(new Integer(0), data, 4);
            if (ret != 4)
            {
                valid = false;
            }
            for (int i = 0; i <= 3; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[4] != 0 || data[5] != 0 || data[6] != 0 || data[7] != 0)
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
            ret = conv.toBytes(new Integer(0), data, 8);
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
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(new Integer(0), new byte[3], 0);
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
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(new Integer(0), new byte[7], 5);
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
        AS400Bin4 conv = new AS400Bin4();

        try
        {
            int ret = conv.toBytes(new Integer(0), new byte[10], -5);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Integer.
     Result: ClassCastException thrown.
     **/
    public void Var021()
    {
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(new Integer(0), null, 0);
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
     Test: Call toBytes(int, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: Two is returned, valid bytes are written in the array.
     **/
    public void Var024()
    {
        try
        {
            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();
            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(0, data, 0);
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
            ret = conv.toBytes(0, data, 4);
            if (ret != 4)
            {
                valid = false;
            }
            for (int i = 0; i <= 3; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                }
            }
            if (data[4] != 0 || data[5] != 0 || data[6] != 0 || data[7] != 0)
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
            ret = conv.toBytes(0, data, 8);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var025()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(0, new byte[3], 0);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(0, new byte[8], 6);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int ret = conv.toBytes(0, new byte[10], -1);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var028()
    {
        AS400Bin4 conv = new AS400Bin4();
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
     Result: Integer object return with valid value.
     **/
    public void Var029()
    {
        try
        {
            int[] expectedValue =
            {
                0x00000000,
                0x00000080,
                0x00008000,
                0x00008080,
                0x00800000,
                0x00800080,
                0x00808000,
                0x00808080,
                0x80000000,
                0x80000080,
                0x80008000,
                0x80008080,
                0x80800000,
                0x80800080,
                0x80808000,
                0x80808080
            };
            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();
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

                            if (obj instanceof Integer)
                            {
                                Integer ivalue = (Integer)obj;
                                int dataValue = ivalue.intValue();
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
    public void Var030()
    {
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
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
     Result: Integer object return with valid value.
     **/
    public void Var032()
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

            int[] expectedValue =
            {
                25359188,
                -2097982449,
                -212594783
            };

            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();

            for (int i=0; i<3; ++i)
            {
                Object obj = conv.toObject(data, i);

                if (obj instanceof Integer)
                {
                    Integer ivalue = (Integer)obj;
                    int dataValue = ivalue.intValue();
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
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            Object obj = conv.toObject(new byte[2], 0);
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
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
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
        AS400Bin4 conv = new AS400Bin4();
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
     Test: Call toInt(byte[]) with valid as400Value parameters.
     Result: int returned with valid value.
     **/
    public void Var037()
    {
        try
        {
            int[] expectedValue =
            {
                0x00000000,
                0x00000080,
                0x00008000,
                0x00008080,
                0x00800000,
                0x00800080,
                0x00808000,
                0x00808080,
                0x80000000,
                0x80000080,
                0x80008000,
                0x80008080,
                0x80800000,
                0x80800080,
                0x80808000,
                0x80808080
            };

            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();
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

                            int dataValue = conv.toInt(data);
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
     Test: Call toInt(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var038()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int i = conv.toInt(new byte[3]);
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
     Test: Call toInt(byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var039()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int i = conv.toInt(null);
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
     Test: Call toInt(byte[], int) with valid startingPoint parameters: start of buffer, middle of buffer, and end of buffer.
     Result: Integer object return with valid value.
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
                (byte)0x54,
                (byte)0x0F,
                (byte)0xA1
            };

            int[] expectedValue =
            {
                25359188,
                -2097982449,
                -212594783
            };

            boolean valid = true;
            AS400Bin4 conv = new AS400Bin4();

            for (int i=0; i<3; ++i)
            {
                int dataValue = conv.toInt(data, i);
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
     Test: Call toInt(byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var041()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int i = conv.toInt(new byte[3], 0);
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
     Test: Call toInt(byte[], int) with invalid parameters: startingPoint to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var042()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int i = conv.toInt(new byte[6], 4);
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
     Test: Call toInt(byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var043()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int i = conv.toInt(new byte[5], -10);
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
     Test: Call toInt(byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var044()
    {
        AS400Bin4 conv = new AS400Bin4();
        try
        {
            int i = conv.toInt(null, 0);
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
