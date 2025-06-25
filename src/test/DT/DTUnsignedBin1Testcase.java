///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTUnsignedBin1Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400UnsignedBin1;

import test.Testcase;

/**
 Testcase DTUnsignedBin1Testcase.
 **/
public class DTUnsignedBin1Testcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTUnsignedBin1Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Test: Construct an AS400UnsignedBin1 object.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            AS400UnsignedBin1 clone = (AS400UnsignedBin1)conv.clone();
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
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
     Result: A short object with the value of zero is returned and the default value passes into toBytes without error.
     **/
    public void Var004()
    {
        try
        {
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            Object ret = conv.getDefaultValue();

            if (ret instanceof Short)
            {
                Short intRet = (Short)ret;
                if (intRet.shortValue() != 0)
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
     Result: One-element byte array returned with a valid value.
     **/
    public void Var005()
    {
        try
        {
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = conv.toBytes(Short.valueOf((short)255));
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Short.
     Result: ClassCastException thrown.
     **/
    public void Var006()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            byte[] ret = conv.toBytes(new String());
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            byte[] ret = conv.toBytes(Short.valueOf((short)-1));
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
     Test: Call toBytes(Object) with invalid parameters: javaValue contains a value too large to be stored in one byte.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var008()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            byte[] ret = conv.toBytes(Short.valueOf((short)0x1000));
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
     Test: Call toBytes(int) with valid shortValue parameter.
     Result: One-element byte array returned with a valid value.
     **/
    public void Var010()
    {
        try
        {
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = conv.toBytes((short)255);
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
     Test: Call toBytes(int) with invalid parameters: shortValue contains a negative value.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var011()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            byte[] ret = conv.toBytes((short)-1);
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
     Test: Call toBytes(int) with invalid parameters: shortValue contains a value too large to be stored in one byte.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var012()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            byte[] ret = conv.toBytes((short)0x0100);
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
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var013()
    {
        try
        {
            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = new byte[1];

            short testValue = 0x00;

            for (int x = 0x00; x <= 0xFF; ++x)
            {
                ///for (int y = 0x00; y <= 0xFF; ++y)
                ///{
                    int ret = conv.toBytes(Short.valueOf(testValue++), data);

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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var014()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0), new byte[0]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Short.
     Result: ClassCastException thrown.
     **/
    public void Var015()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(new String(), new byte[10]);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)-1), new byte[10]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue contains a value too large to be stored in one byte.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var017()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0x0100), new byte[10]);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0), null);
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
     Test: Call toBytes(int, byte[]) with valid parameters.
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var020()
    {
        try
        {
            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = new byte[1];

            short testValue = 0x00;

            for (int x = 0x00; x <= 0xFF; ++x)
            {
                //for (int y = 0x00; y <= 0xFF; ++y)
                //{
                    int ret = conv.toBytes(testValue++, data);

                    if (ret != 1)
                    {
                        valid = false;
                    }
                    if (data[0] != (byte)x)
                    {
                        valid = false;
                    }
                //}
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call toBytes(int, byte[]) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var021()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0, new byte[0]);
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
     Test: Call toBytes(int, byte[]) with invalid parameters: shortValue contains a negative value.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var022()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)-1, new byte[10]);
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
     Test: Call toBytes(int, byte[]) with invalid parameters: shortValue contains a value too large to be stored in one byte.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var023()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0x0100, new byte[10]);
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
     Test: Call toBytes(int, byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var024()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0, null);
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
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var025()
    {
        try
        {
            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = new byte[10];

            for (int i = 0; i < 10; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(Short.valueOf((short)0), data, 0);
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
            ret = conv.toBytes(Short.valueOf((short)0), data, 5);
            if (ret != 1)
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
            if (data[5] != 0)
            {
                valid = false;
            }
            for (int i = 6; i < 10; ++i)
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
            ret = conv.toBytes(Short.valueOf((short)0), data, 8);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0), new byte[0], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: startingPoint too close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var027()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0), new byte[11], 11);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Short.
     Result: ClassCastException thrown.
     **/
    public void Var029()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(new String(), new byte[10], 0);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)-1), new byte[10], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue contains a value too large to be stored in one byte.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var031()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0x0100), new byte[10], 0);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes(Short.valueOf((short)0), null, 0);
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
     Test: Call toBytes(int, byte[], int) with valid starting points: start of array, middle of array, and end of the array.
     Result: One is returned, valid bytes are written in the array.
     **/
    public void Var034()
    {
        try
        {
            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = new byte[10];

            for (int i = 0; i < 10; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes((short)0, data, 0);
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
            ret = conv.toBytes((short)0, data, 5);
            if (ret != 1)
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
            if (data[5] != 0)
            {
                valid = false;
            }
            for (int i = 6; i < 10; ++i)
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var035()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0, new byte[0], 0);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: startingPoint too close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var036()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0, new byte[11], 11);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var037()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0, new byte[10], -1);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: shortValue contains a negative value.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var038()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)-1, new byte[10], 0);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: shortValue contains a value too large to be stored in one byte.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var039()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0x0100, new byte[10], 0);
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
     Test: Call toBytes(int, byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var040()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int ret = conv.toBytes((short)0, null, 0);
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
     Result: Short object return with valid value.
     **/
    public void Var041()
    {
        try
        {
            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = new byte[1];
            short expectedValue = 0x0000;

            for (int x = 0x00; x <= 0xFF; ++x)
            {
                data[0] = (byte)x;
                //for (int y = 0x00; y <= 0xFF; ++y)
                //{
                //    data[1] = (byte)y;

                    Object obj = conv.toObject(data);

                    if (obj instanceof Short)
                    {
                        Short ivalue = (Short)obj;
                        short dataValue = ivalue.shortValue();
                        if (dataValue != expectedValue++)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }
                //}
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            Object obj = conv.toObject(new byte[0]);
            failed("no exception thrown for too small array"+obj);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
     Result: Short object return with valid value.
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
                (byte)0x54
            };

            short[] expectedValue =
            {
                (short)0x01,
                (short)0x82,
                (short)0xF3,
                (short)0x54
            };

            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            for (int i=0; i<4; ++i)
            {
                Object obj = conv.toObject(data, i);

                if (obj instanceof Short)
                {
                    Short ivalue = (Short)obj;
                    short dataValue = ivalue.shortValue();
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
     Test: Call toObject(byte[], int) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var045()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            Object obj = conv.toObject(new byte[0], 0);
            failed("no exception thrown for too small array"+obj);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            Object obj = conv.toObject(new byte[2], 2);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            Object obj = conv.toObject(new byte[3], -67);
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
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
     Test: Call toShort(byte[]) with valid as400Value parameters.
     Result: int returned with valid value.
     **/
    public void Var049()
    {
        try
        {
            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
            byte[] data = new byte[1];
            short expectedValue = 0x0000;

            for (int x = 0x00; x <= 0xFF; ++x)
            {
                data[0] = (byte)x;
                //for (int y = 0x00; y <= 0xFF; ++y)
                //{
                //    data[1] = (byte)y;

                    short dataValue = conv.toShort(data);

                    if (dataValue != expectedValue++)
                    {
                        valid = false;
                    }
                //}
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call toShort(byte[]) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var050()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int i = conv.toShort(new byte[0]);
            failed("no exception thrown for too small array"+i);
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
     Test: Call toShort(byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var051()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int i = conv.toShort(null);
            failed("no exception thrown for null pointer"+i);
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
     Result: int returned with valid value.
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
                (byte)0x54
            };

            short[] expectedValue =
            {
                (short)0x01,
                (short)0x82,
                (short)0xF3,
                (short)0x54
            };

            boolean valid = true;
            AS400UnsignedBin1 conv = new AS400UnsignedBin1();
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
     Test: Call toShort(byte[], int) with invalid parameters: as400Value too small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var053()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int i = conv.toShort(new byte[0], 0);
            failed("no exception thrown for too small array"+i);
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
     Test: Call toShort(byte[], int) with invalid parameters: startingPoint too close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var054()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int i = conv.toShort(new byte[2], 2);
            failed("no exception thrown for not enough space"+i);
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
     Test: Call toShort(byte[], int) with invalid parameters: startingPoint negative number.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var055()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int i = conv.toShort(new byte[3], -67);
            failed("no exception thrown for negative value"+i);
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
     Test: Call toShort(byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var056()
    {
        AS400UnsignedBin1 conv = new AS400UnsignedBin1();
        try
        {
            int i = conv.toShort(null, 0);
            failed("no exception thrown for null pointer"+i);
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



