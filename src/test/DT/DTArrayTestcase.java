///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTArrayTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400Array;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Structure;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.ExtendedIllegalStateException;

import test.Testcase;

/**
 Testcase DTArrayTestcase.
 **/
public class DTArrayTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTArrayTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Test: Construct an AS400Array object with the null constructor.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Array conv = new AS400Array();
            assertCondition(true,  "created conv="+conv); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Construct an AS400Array object with valid parameters.
     Result: No exception should be thrown.
     **/
    public void Var002()
    {
        try
        {
            AS400Array conv = null;
            for (int i=0; i<100; ++i)
            {
                conv = new AS400Array(new AS400Bin2(), i);
                conv = new AS400Array(new AS400Bin4(), i);
                conv = new AS400Array(new AS400ByteArray(i), i);
                conv = new AS400Array(new AS400Float4(), i);
                conv = new AS400Array(new AS400Float8(), i);
                conv = new AS400Array(new AS400PackedDecimal(5, 3), i);
                conv = new AS400Array(new AS400Text(i, 1123, systemObject_), i);
                conv = new AS400Array(new AS400UnsignedBin2(), i);
                conv = new AS400Array(new AS400UnsignedBin4(), i);
                conv = new AS400Array(new AS400ZonedDecimal(7, 7), i);
                conv = new AS400Array(new AS400Structure(), 5);
            }
            assertCondition(true, "Created conv="+conv); 
            
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Construct an AS400Array object with negative number length parameter.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var003()
    {
        try
        {
            AS400Array conv = new AS400Array(new AS400Bin4(), -1);
            failed("Exception not thrown. ret="+conv);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "size (-1): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Test: Construct an AS400Array object with null type parameter.
     Result: NullPointerException should be thrown.
     **/
    public void Var004()
    {
        try
        {
            AS400Array conv = new AS400Array(null, 10);
            failed("Exception not thrown. conv="+conv);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Test: Call clone on fully set object
     Result: Cloned object returned.
     **/
    public void Var005()
    {
        try
        {
            AS400Bin2 type = new AS400Bin2();
            AS400Array conv = new AS400Array(type, 5);
            AS400Array clone = (AS400Array)conv.clone();
            AS400Bin2 cloneType = (AS400Bin2)clone.getType();
            int cloneSize = clone.getNumberOfElements();
            if (clone != null &&
                clone != conv &&
                cloneType instanceof AS400Bin2 &&
                cloneType != type &&
                cloneSize == 5)
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
     Test: Call clone on unset object
     Result: Cloned object returned.
     **/
    public void Var006()
    {
        try
        {
            AS400Array conv = new AS400Array();
            AS400Array clone = (AS400Array)conv.clone();
            Object cloneType = clone.getType();
            int cloneSize = clone.getNumberOfElements();
            if (clone != null &&
                clone != conv &&
                cloneType == null &&
                cloneSize == -1)
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

            for (int i=0; i<100; ++i)
            {
                AS400Array conv = new AS400Array(new AS400UnsignedBin4(), i);
                int ret = conv.getByteLength();
                if (ret != i*4)
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
     Test: Call getByteLength with type unset
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var008()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(5);
        try
        {
            int ret = conv.getByteLength();
            assertCondition(false,"Expected exception but got "+ret); 
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call getByteLength with number of elements unset
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var009()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        try
        {
            int ret = conv.getByteLength();
            assertCondition(false,"Expected exception but got "+ret); 
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call getByteLength with type unset, catch exception, fix, and call again
     Result: The correct int value returned
     **/
    public void Var010()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(5);
        try
        {
            int ret = conv.getByteLength();
            assertCondition(false,"Expected exception but got "+ret); 
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin4());
            int ret = conv.getByteLength();
            if (ret == 20)
            {
                succeeded();
            }
            else
            {
                failed("Incorrect results");
            }
        }
    }

    /**
     Test: Call getByteLength with number of elements unset, catch exception, fix, and call again
     Result: The correct int value returned
     **/
    public void Var011()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        try
        {
            int ret = conv.getByteLength();
            assertCondition(false,"Expected exception but got "+ret); 
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setNumberOfElements(5);
            int ret = conv.getByteLength();
            if (ret == 20)
            {
                succeeded();
            }
            else
            {
                failed("Incorrect results");
            }
        }
    }

    /**
     Test: Call getDefaultValue
     Result: An empty array is returned and the default value passes into toBytes without error.
     **/
    public void Var012()
    {
        try
        {
            AS400Array conv = new AS400Array(new AS400Bin4(), 0);
            Object ret = conv.getDefaultValue();

            if (ret instanceof Object[])
            {
                Object[] dataValue = (Object[])ret;

                if (dataValue.length != 0)
                {
                    failed("Incorrect return value");
                }
                else
                {
                    byte[] data = conv.toBytes(ret);
                    if (data.length == 0)
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
     Test: Call getNumberOfElements
     Result: The correct int value returned
     **/
    public void Var013()
    {
        try
        {
            boolean valid = true;

            for (int i=0; i<100; ++i)
            {
                AS400Array conv = new AS400Array(new AS400UnsignedBin4(), i);
                int ret = conv.getNumberOfElements();
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
     Test: Call getNumberOfElements with number of elements unset
     Result: The value -1 returned
     **/
    public void Var014()
    {
        try
        {
            AS400Array conv = new AS400Array();
            int ret = conv.getNumberOfElements();
            if (ret == -1)
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
     Test: Call getType
     Result: The correct type value returned
     **/
    public void Var015()
    {
        try
        {
            boolean valid = true;
            AS400Array conv;
            AS400DataType type;

            conv = new AS400Array(new AS400Bin2(), 5);
            type = conv.getType();
            if (!(type instanceof AS400Bin2))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400Bin4(), 5);
            type = conv.getType();
            if (!(type instanceof AS400Bin4))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400ByteArray(5), 5);
            type = conv.getType();
            if (!(type instanceof AS400ByteArray))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400Float4(), 5);
            type = conv.getType();
            if (!(type instanceof AS400Float4))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400Float8(), 5);
            type = conv.getType();
            if (!(type instanceof AS400Float8))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400PackedDecimal(5, 3), 5);
            type = conv.getType();
            if (!(type instanceof AS400PackedDecimal))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400Text(5, 1123, systemObject_), 5);
            type = conv.getType();
            if (!(type instanceof AS400Text))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400UnsignedBin2(), 5);
            type = conv.getType();
            if (!(type instanceof AS400UnsignedBin2))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400UnsignedBin4(), 5);
            type = conv.getType();
            if (!(type instanceof AS400UnsignedBin4))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400ZonedDecimal(7, 7), 5);
            type = conv.getType();
            if (!(type instanceof AS400ZonedDecimal))
            {
                valid = false;
            }
            conv = new AS400Array(new AS400Structure(), 3);
            type = conv.getType();
            if (!(type instanceof AS400Structure))
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
     Test: Call getType with type unset
     Result: null returned
     **/
    public void Var016()
    {
        try
        {
            AS400Array conv = new AS400Array();
            AS400DataType ret = conv.getType();
            if (ret == null)
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
     Test: Call setNumberOfElements
     Result: The correct int value set
     **/
    public void Var017()
    {
        try
        {
            boolean valid = true;
            AS400Array conv = new AS400Array();

            for (int i=0; i<100; ++i)
            {
                conv.setNumberOfElements(i);
                int ret = conv.getNumberOfElements();
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
     Test: Call setNumberOfElements with negative number length parameter.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var018()
    {
        AS400Array conv = new AS400Array();
        try
        {
            conv.setNumberOfElements(-10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "size (-10): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Test: Call setNumberOfElements after using non-null constructor
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var019()
    {
        AS400Array conv = new AS400Array(new AS400Float8(), 5);
        try
        {
            conv.setNumberOfElements(10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setNumberOfElements after using toBytes(Object)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var020()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toBytes(new Object[0]);
        try
        {
            conv.setNumberOfElements(10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setNumberOfElements after using toBytes(Object, byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var021()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toBytes(new Object[0], new byte[10]);
        try
        {
            conv.setNumberOfElements(10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setNumberOfElements after using toBytes(Object, byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var022()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toBytes(new Object[0], new byte[10], 0);
        try
        {
            conv.setNumberOfElements(10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setNumberOfElements after using toObject(byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var023()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toObject(new byte[10]);
        try
        {
            conv.setNumberOfElements(10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setNumberOfElements after using toObject(byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var024()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toObject(new byte[10], 0);
        try
        {
            conv.setNumberOfElements(10);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType
     Result: The correct type value returned
     **/
    public void Var025()
    {
        try
        {
            boolean valid = true;
            AS400Array conv = new AS400Array();
            AS400DataType type;

            conv.setType(new AS400Bin2());
            type = conv.getType();
            if (!(type instanceof AS400Bin2))
            {
                valid = false;
            }
            conv.setType(new AS400Bin4());
            type = conv.getType();
            if (!(type instanceof AS400Bin4))
            {
                valid = false;
            }
            conv.setType(new AS400ByteArray(5));
            type = conv.getType();
            if (!(type instanceof AS400ByteArray))
            {
                valid = false;
            }
            conv.setType(new AS400Float4());
            type = conv.getType();
            if (!(type instanceof AS400Float4))
            {
                valid = false;
            }
            conv.setType(new AS400Float8());
            type = conv.getType();
            if (!(type instanceof AS400Float8))
            {
                valid = false;
            }
            conv.setType(new AS400PackedDecimal(5, 3));
            type = conv.getType();
            if (!(type instanceof AS400PackedDecimal))
            {
                valid = false;
            }
            conv.setType(new AS400Text(5, 1123, systemObject_));
            type = conv.getType();
            if (!(type instanceof AS400Text))
            {
                valid = false;
            }
            conv.setType(new AS400UnsignedBin2());
            type = conv.getType();
            if (!(type instanceof AS400UnsignedBin2))
            {
                valid = false;
            }
            conv.setType(new AS400UnsignedBin4());
            type = conv.getType();
            if (!(type instanceof AS400UnsignedBin4))
            {
                valid = false;
            }
            conv.setType(new AS400ZonedDecimal(7, 7));
            type = conv.getType();
            if (!(type instanceof AS400ZonedDecimal))
            {
                valid = false;
            }
            conv.setType(new AS400ZonedDecimal(7, 7));
            type = conv.getType();
            if (!(type instanceof AS400ZonedDecimal))
            {
                valid = false;
            }
            conv.setType(new AS400Structure());
            type = conv.getType();
            if (!(type instanceof AS400Structure))
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
     Test: Call setType after using non-null constructor
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var026()
    {
        AS400Array conv = new AS400Array(new AS400Float8(), 5);
        try
        {
            conv.setType(new AS400Bin4());
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType after using toBytes(Object)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var027()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toBytes(new Object[0]);
        try
        {
            conv.setType(new AS400Bin4());
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType after using toBytes(Object, byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var028()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toBytes(new Object[0], new byte[10]);
        try
        {
            conv.setType(new AS400Bin4());
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType after using toBytes(Object, byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var029()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toBytes(new Object[0], new byte[10], 0);
        try
        {
            conv.setType(new AS400Bin4());
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType after using toObject(byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var030()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toObject(new byte[10]);
        try
        {
            conv.setType(new AS400Bin4());
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType after using toObject(byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var031()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        conv.setNumberOfElements(0);
        conv.toObject(new byte[10], 0);
        try
        {
            conv.setType(new AS400Bin4());
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call setType with null type parameter.
     Result: NullPointerException should be thrown.
     **/
    public void Var032()
    {
        AS400Array conv = new AS400Array();
        try
        {
            conv.setType(null);
            failed("Exception not thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Unexpected exception.");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with valid javaValue parameter.
     Result: Byte array returned with a valid value.
     **/
    public void Var033()
    {
        try
        {
            AS400Array conv = new AS400Array(new AS400Bin2(), 4);
            Object[] inputValue =
            {
                new Short((short)0x0000),
                new Short((short)0x0080),
                new Short((short)0x8000),
                new Short((short)0x8080)
            };
            byte[] data = conv.toBytes(inputValue);
            if (data.length == 8 &&
                data[0] == (byte)0x00 &&
                data[1] == (byte)0x00 &&
                data[2] == (byte)0x00 &&
                data[3] == (byte)0x80 &&
                data[4] == (byte)0x80 &&
                data[5] == (byte)0x00 &&
                data[6] == (byte)0x80 &&
                data[7] == (byte)0x80)
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
     Test: Call toBytes(Object) with invalid parameter: javaValue is not an instance of Object[]
     Result: ClassCastException thrown.
     **/
    public void Var034()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        try
        {
            byte[] ret = conv.toBytes(new Object());
            assertCondition(false,"Expected exception but got "+ret); 
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
     Test: Call toBytes(Object) with invalid parameter: an element of javaValue is not an instance of the type of the Array
     Result: ClassCastException thrown.
     **/
    public void Var035()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new Short((short)0),
            new String("l;")
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            assertCondition(false,"Expected exception but got "+ret); 
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
     Test: Call toBytes(Object) with invalid parameter: javaValue has an incorrect number of elements
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var036()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("l;")
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            assertCondition(false,"Expected exception but got "+ret); 
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
     Test: Call toBytes(Object) with invalid parameter: javaValue has an incorrect element
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var037()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("ghjk"),
            new String("l;")
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            assertCondition(false,"Expected exception but got "+ret); 
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (ghjk): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with invalid state: type not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var038()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("gh"),
            new String("l;")
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            assertCondition(false,"Expected exception but got "+ret); 
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with invalid state: type not set, catch, fix, call again
     Result: Byte array returned with a valid value.
     **/
    public void Var039()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        Object[] testValue =
        {
            new Short((short)0x0000),
            new Short((short)0x0080),
            new Short((short)0x8000),
            new Short((short)0x8080)
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin2());
            byte[] data = conv.toBytes(testValue);
            if (data.length == 8 &&
                data[0] == (byte)0x00 &&
                data[1] == (byte)0x00 &&
                data[2] == (byte)0x00 &&
                data[3] == (byte)0x80 &&
                data[4] == (byte)0x80 &&
                data[5] == (byte)0x00 &&
                data[6] == (byte)0x80 &&
                data[7] == (byte)0x80)
            {
                succeeded();
            }
            else
            {
                failed("Unexpected value");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with invalid state: number of elements not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var040()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        Object[] testValue = {
            new Integer(0),
            new Integer(1),
            new Integer(2),
            new Integer(3)
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with invalid state: number of elements not set, catch, fix, call again
     Result: Byte array returned with a valid value.
     **/
    public void Var041()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        Object[] testValue =
        {
            new Short((short)0x0000),
            new Short((short)0x0080),
            new Short((short)0x8000),
            new Short((short)0x8080)
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
            failed("No exception thrown."+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setNumberOfElements(4);
            byte[] data = conv.toBytes(testValue);
            if (data.length == 8 &&
                data[0] == (byte)0x00 &&
                data[1] == (byte)0x00 &&
                data[2] == (byte)0x00 &&
                data[3] == (byte)0x80 &&
                data[4] == (byte)0x80 &&
                data[5] == (byte)0x00 &&
                data[6] == (byte)0x80 &&
                data[7] == (byte)0x80)
            {
                succeeded();
            }
            else
            {
                failed("Unexpected value");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with invalid parameter: an element of javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var042()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            null,
            new String("jk"),
            new String("l;")
        };
        try
        {
            byte[] ret = conv.toBytes(testValue);
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
     Test: Call toBytes(Object) with invalid parameter: javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var043()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 37, systemObject_), 4);
        try
        {
            byte[] ret = conv.toBytes(null);
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
     Test: Call toBytes(Object, byte[]) with valid parameters.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var044()
    {
        try
        {
            Object[] testValue =
            {
                new Short((short)-1),
                new Short((short)-100),
                new Short((short)1),
                new Short((short)100)
            };

            byte[] expectedValue =
            {
                (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0x9C,
                (byte)0x00, (byte)0x01,
                (byte)0x00, (byte)0x64
            };

            boolean valid = true;
            AS400Array conv = new AS400Array(new AS400Bin2(), 4);
            byte[] data = new byte[31];

            int ret = conv.toBytes(testValue, data);

            if (ret != 8)
            {
                valid = false;
            }
            for (int i=0; i<8; ++i)
            {
                if (data[i] != expectedValue[i])
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
    public void Var045()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 855, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[4]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameter: javaValue is not an instance of Object[]
     Result: ClassCastException thrown.
     **/
    public void Var046()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        try
        {
            int ret = conv.toBytes(new Object(), new byte[10]);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(Object, byte[]) with invalid parameter: an element of javaValue is not an instance of the type of the Array
     Result: ClassCastException thrown.
     **/
    public void Var047()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new Short((short)0),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("No exception thrown."+ret);
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
     Test: Call toBytes(Object, byte[]) with invalid parameter: javaValue has an incorrect number of elements
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var048()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[]) with invalid parameter: javaValue has an incorrect element
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var049()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("ghjk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (ghjk): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[]) with invalid state: type not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var050()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("gh"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[]) with invalid state: type not set, catch, fix, call again
     Result: Valid result returned
     **/
    public void Var051()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        Object[] testValue =
        {
            new Short((short)-1),
            new Short((short)-100),
            new Short((short)1),
            new Short((short)100)
        };
        byte[] expectedValue =
        {
            (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x9C,
            (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x64
        };

        boolean valid = true;
        byte[] data = new byte[31];

        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin2());
            int ret = conv.toBytes(testValue, data);

            if (ret != 8)
            {
                valid = false;
            }
            for (int i=0; i<8; ++i)
            {
                if (data[i] != expectedValue[i])
                {
                    valid = false;
                }
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toBytes(Object, byte[]) with invalid state: number of elements not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var052()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        Object[] testValue = {
            new Integer(0),
            new Integer(1),
            new Integer(2),
            new Integer(3)
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[]) with invalid state: number of elements not set, catch, fix, call again
     Result: Valid result returned
     **/
    public void Var053()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        Object[] testValue =
        {
            new Short((short)-1),
            new Short((short)-100),
            new Short((short)1),
            new Short((short)100)
        };

        byte[] expectedValue =
        {
            (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x9C,
            (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x64
        };

        boolean valid = true;
        byte[] data = new byte[31];

        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setNumberOfElements(4);
            int ret = conv.toBytes(testValue, data);

            if (ret != 8)
            {
                valid = false;
            }
            for (int i=0; i<8; ++i)
            {
                if (data[i] != expectedValue[i])
                {
                    valid = false;
                }
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toBytes(Object, byte[]) with invalid parameter: an element of javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var054()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            null,
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10]);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[]) with invalid parameter: javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var055()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 37, systemObject_), 4);

        try
        {
            int ret = conv.toBytes(null, new byte[8]);
            failed("Exception not thrown. ret="+ret);
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
    public void Var056()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, null);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                assertCondition(e.getMessage() == null);
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
    public void Var057()
    {
        try
        {
            boolean valid = true;
            AS400Array conv = new AS400Array(new AS400Text(2, 37, systemObject_), 2);
            Object[] testValue =
            {
                new String(""),
                new String("  ")
            };
            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(testValue, data, 0);
            if (ret != 4)
            {
                valid = false;
                System.out.println("Test 1: ret = " + ret);
            }
            if (data[0] != (byte)0x40 ||
                data[1] != (byte)0x40 ||
                data[2] != (byte)0x40 ||
                data[3] != (byte)0x40)
            {
                valid = false;
                System.out.println("Test 1: data 0-3 bad");
                System.out.println("Test 1: data[0] = " + data[0]);
                System.out.println("Test 1: data[1] = " + data[1]);
                System.out.println("Test 1: data[2] = " + data[2]);
                System.out.println("Test 1: data[3] = " + data[3]);
            }
            for (int i = 4; i < 12; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                    System.out.println("Test 1: data bad i = " + i);
                }
            }

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(testValue, data, 3);
            if (ret != 4)
            {
                valid = false;
                System.out.println("Test 2: ret = " + ret);
            }
            for (int i = 0; i <= 2; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                    System.out.println("Test 2: data bad i = " + i);
                }
            }
            if (data[3] != (byte)0x40 ||
                data[4] != (byte)0x40 ||
                data[5] != (byte)0x40 ||
                data[6] != (byte)0x40)
            {
                valid = false;
                System.out.println("Test 2: data 3-6 bad");
            }
            for (int i = 7; i < 12; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                    System.out.println("Test 2: data bad i = " + i);
                }
            }

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            ret = conv.toBytes(testValue, data, 8);
            if (ret != 4)
            {
                valid = false;
                System.out.println("Test 3: ret = " + ret);
            }
            for (int i = 0; i <= 7; ++i)
            {
                if (data[i] != (byte)0xEE)
                {
                    valid = false;
                    System.out.println("Test 3: data bad i = " + i);
                }
            }
            if (data[8]  != (byte)0x40 ||
                data[9]  != (byte)0x40 ||
                data[10] != (byte)0x40 ||
                data[11] != (byte)0x40)
            {
                valid = false;
                System.out.println("Test 2: data 8-11 bad");
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with valid parameters: array of array.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var058()
    {
        try
        {
            Object[][] testValue =
            {
                {
                    new Short((short)-1),
                    new Short((short)-100)
                },
                {
                    new Short((short)1),
                    new Short((short)100)
                }
            };

            byte[] expectedValue =
            {
                (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0x9C,
                (byte)0x00, (byte)0x01,
                (byte)0x00, (byte)0x64
            };

            boolean valid = true;
            AS400Array inner = new AS400Array(new AS400Bin2(), 2);
            AS400Array conv = new AS400Array(inner, 2);
            byte[] data = new byte[31];

            int ret = conv.toBytes(testValue, data, 0);

            if (ret != 8)
            {
                valid = false;
            }
            for (int i=0; i<8; ++i)
            {
                if (data[i] != expectedValue[i])
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
     Test: Call toBytes(Object, byte[], int) with valid parameters: array of structure.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var059()
    {
        try
        {
            Object[][] testValue =
            {
                {
                    new Short((short)-1),
                    new Integer(-100)
                },
                {
                    new Short((short)1),
                    new Integer(100)
                }
            };

            byte[] expectedValue =
            {
                (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x9C,
                (byte)0x00, (byte)0x01,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x64
            };

            boolean valid = true;
            AS400DataType[] members = {new AS400Bin2(), new AS400Bin4()};
            AS400Structure inner = new AS400Structure(members);
            AS400Array conv = new AS400Array(inner, 2);
            byte[] data = new byte[31];

            int ret = conv.toBytes(testValue, data, 0);

            if (ret != 12)
            {
                valid = false;
            }
            for (int i=0; i<12; ++i)
            {
                if (data[i] != expectedValue[i])
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var060()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 855, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[4], 0);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: offset to close to end of buffer.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var061()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 1123, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 4);
            failed("Exception not thrown. ret="+ret);
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
    public void Var062()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 855, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], -1);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameter: javaValue is not an instance of Object[]
     Result: ClassCastException thrown.
     **/
    public void Var063()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        try
        {
            int ret = conv.toBytes(new Object(), new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameter: an element of javaValue is not an instance of the type of the Array
     Result: ClassCastException thrown.
     **/
    public void Var064()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new Short((short)0),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameter: javaValue has an incorrect number of elements
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var065()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameter: javaValue has an incorrect element
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var066()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("ghjk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (ghjk): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with invalid state: type not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var067()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("gh"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with invalid state: type not set, catch, fix, call again
     Result: Valid result returned
     **/
    public void Var068()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        Object[] testValue =
        {
            new Short((short)-1),
            new Short((short)-100),
            new Short((short)1),
            new Short((short)100)
        };
        byte[] expectedValue =
        {
            (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x9C,
            (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x64
        };

        boolean valid = true;
        byte[] data = new byte[31];

        try
        {
            int ret = conv.toBytes(testValue, data, 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin2());
            int ret = conv.toBytes(testValue, data, 0);

            if (ret != 8)
            {
                valid = false;
            }
            for (int i=0; i<8; ++i)
            {
                if (data[i] != expectedValue[i])
                {
                    valid = false;
                }
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with invalid state: number of elements not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var069()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        Object[] testValue = {
            new Integer(0),
            new Integer(1),
            new Integer(2),
            new Integer(3)
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with invalid state: number of elements not set, catch, fix, call again
     Result: Valid result returned
     **/
    public void Var070()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin2());
        Object[] testValue =
        {
            new Short((short)-1),
            new Short((short)-100),
            new Short((short)1),
            new Short((short)100)
        };

        byte[] expectedValue =
        {
            (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x9C,
            (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x64
        };

        boolean valid = true;
        byte[] data = new byte[31];

        try
        {
            int ret = conv.toBytes(testValue, data, 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setNumberOfElements(4);
            int ret = conv.toBytes(testValue, data, 0);

            if (ret != 8)
            {
                valid = false;
            }
            for (int i=0; i<8; ++i)
            {
                if (data[i] != expectedValue[i])
                {
                    valid = false;
                }
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with invalid parameter: an element of javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var071()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            null,
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameter: javaValue is null
     Result: NullPointerException thrown.
     **/
    public void Var072()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 37, systemObject_), 4);

        try
        {
            int ret = conv.toBytes(null, new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
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
    public void Var073()
    {
        AS400Array conv = new AS400Array(new AS400Text(2, 500, systemObject_), 4);
        Object[] testValue = {
            new String("as"),
            new String("df"),
            new String("jk"),
            new String("l;")
        };
        try
        {
            int ret = conv.toBytes(testValue, null, 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException"))
            {
                assertCondition(e.getMessage() == null);
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toObject(byte[]) with valid as400Value parameters.
     Result: Object array returned with valid value.
     **/
    public void Var074()
    {
        try
        {
            byte[] data =
            {
                (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
                (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
                (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
                (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
                (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13
            };

            int[] expectedValue = {66051, 67438087, 134810123, 202182159, 269554195};

            boolean valid = true;
            AS400Array conv = new AS400Array(new AS400Bin4(), 5);
            Object obj = conv.toObject(data);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 5)
                {
                    for (int i=0; i<5; ++i)
                    {
                        if (objArray[i] instanceof Integer)
                        {
                            Integer iValue = (Integer)objArray[i];
                            if (iValue.intValue() != expectedValue[i])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
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
     Test: Call toObject(byte[]) with invalid parameter: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var075()
    {
        AS400Array conv = new AS400Array(new AS400UnsignedBin2(), 5);
        try
        {
            Object obj = conv.toObject(new byte[8]);
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
     Test: Call toObject(byte[]) with invalid state: type not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var076()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        try
        {
            Object ret = conv.toObject(new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid state: type not set, catch, fix, call again
     Result: Object array returned with valid value.
     **/
    public void Var077()
    {
        byte[] data =
        {
            (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
            (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
            (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
            (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
            (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13
        };

        int[] expectedValue = {66051, 67438087, 134810123, 202182159, 269554195};

        boolean valid = true;

        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(5);
        try
        {
            Object ret = conv.toObject(data);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin4());
            Object obj = conv.toObject(data);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 5)
                {
                    for (int i=0; i<5; ++i)
                    {
                        if (objArray[i] instanceof Integer)
                        {
                            Integer iValue = (Integer)objArray[i];
                            if (iValue.intValue() != expectedValue[i])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
                valid = false;
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid state: number of elements not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var078()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        try
        {
            Object ret = conv.toObject(new byte[10]);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid state: number of elements not set, catch, fix, call again
     Result: Object array returned with valid value.
     **/
    public void Var079()
    {
        byte[] data =
        {
            (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
            (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
            (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
            (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
            (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13
        };

        int[] expectedValue = {66051, 67438087, 134810123, 202182159, 269554195};

        boolean valid = true;

        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        try
        {
            Object ret = conv.toObject(data);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setNumberOfElements(5);
            Object obj = conv.toObject(data);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 5)
                {
                    for (int i=0; i<5; ++i)
                    {
                        if (objArray[i] instanceof Integer)
                        {
                            Integer iValue = (Integer)objArray[i];
                            if (iValue.intValue() != expectedValue[i])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
                valid = false;
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid parameter: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var080()
    {
        AS400Array conv = new AS400Array(new AS400UnsignedBin2(), 5);
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
     Result: Byte array returned with valid value.
     **/
    public void Var081()
    {
        try
        {
            byte[] data =
            {
                (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17,
                (byte)0x18, (byte)0x19, (byte)0x1A, (byte)0x1B,
                (byte)0x1C, (byte)0x1D, (byte)0x1E, (byte)0x1F,
                (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23,
                (byte)0x24, (byte)0x25, (byte)0x26, (byte)0x27,
                (byte)0x28, (byte)0x29, (byte)0x2A, (byte)0x2B
            };

            int[] expectedValue = {
                336926231,
                404298267,
                471670303,
                539042339,
                606414375,
                673786411
            };

            boolean valid = true;
            AS400Array conv = new AS400Array(new AS400Bin4(), 4);

            for (int i=0; i<3; ++i)
            {
                Object obj = conv.toObject(data, i*4);

                if (obj instanceof Object[])
                {
                    Object[] objArray = (Object[])obj;

                    if (objArray.length == 4)
                    {
                        for (int x=0; x<4; ++x)
                        {
                            if (objArray[x] instanceof Integer)
                            {
                                Integer iValue = (Integer)objArray[x];
                                if (iValue.intValue() != expectedValue[i+x])
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
                    else
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
     Test: Call toObject(byte[], int) with valid as400Value parameters: array of arrays.
     Result: Object array returned with valid value.
     **/
    public void Var082()
    {
        try
        {
            byte[] data =
            {
                (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
                (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
                (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
                (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
            };

            int[] expectedValue = {66051, 67438087, 134810123, 202182159};

            boolean valid = true;
            AS400Array inner = new AS400Array(new AS400Bin4(), 2);
            AS400Array conv = new AS400Array(inner, 2);
            Object obj = conv.toObject(data, 0);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 2)
                {
                    for (int i=0; i<2; ++i)
                    {
                        Object objobj = objArray[i];
                        if (objobj instanceof Object[])
                        {
                            Object[] objArrayArray = (Object[])objobj;

                            for (int ii=0; ii<2; ++ii)
                            {
                                if (objArrayArray[ii] instanceof Integer)
                                {
                                    Integer iValue = (Integer)objArrayArray[ii];
                                    if (iValue.intValue() != expectedValue[i*2+ii])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
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
     Test: Call toObject(byte[], int) with valid as400Value parameters: array of structures.
     Result: Object array returned with valid value.
     **/
    public void Var083()
    {
        try
        {
            byte[] data =
            {
                (byte)0x00, (byte)0x01,
                (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
                (byte)0x08, (byte)0x09,
                (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
            };

            int[] expectedValue = {1, 67438087, 2057, 202182159};

            boolean valid = true;
            AS400DataType[] members = {new AS400Bin2(), new AS400Bin4()};
            AS400Structure inner = new AS400Structure(members);
            AS400Array conv = new AS400Array(inner, 2);
            Object obj = conv.toObject(data, 0);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 2)
                {
                    for (int i=0; i<2; ++i)
                    {
                        Object objobj = objArray[i];

                        if (objobj instanceof Object[])
                        {
                            Object[] objArrayArray = (Object[])objobj;

                            if (objArray.length == 2)
                            {
                                if (objArrayArray[0] instanceof Short)
                                {
                                    Short iValue = (Short)objArrayArray[0];
                                    if (iValue.intValue() != expectedValue[i*2])
                                    {
                                        valid = false;
                                    }
                                }
                                else
                                {
                                    valid = false;
                                }
                                if (objArrayArray[1] instanceof Integer)
                                {
                                    Integer iValue = (Integer)objArrayArray[1];
                                    if (iValue.intValue() != expectedValue[i*2+1])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
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
     Test: Call toObject(byte[], int) with invalid parameter: as400Value to small
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var084()
    {
        AS400Array conv = new AS400Array(new AS400UnsignedBin2(), 5);
        try
        {
            Object obj = conv.toObject(new byte[8], 0);
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
     Test: Call toObject(byte[], int) with invalid parameter: startingPoint to close to end of buffer
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var085()
    {
        AS400Array conv = new AS400Array(new AS400UnsignedBin2(), 5);
        try
        {
            Object obj = conv.toObject(new byte[20], 15);
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
     Test: Call toObject(byte[], int) with invalid parameter: startingPoint negative number
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var086()
    {
        AS400Array conv = new AS400Array(new AS400UnsignedBin2(), 5);
        try
        {
            Object obj = conv.toObject(new byte[15], -10);
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
     Test: Call toObject(byte[]) with invalid state: type not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var087()
    {
        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(4);
        try
        {
            Object ret = conv.toObject(new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toObject(byte[], int) with invalid state: type not set, catch, fix, call again
     Result: Object array returned with valid value.
     **/
    public void Var088()
    {
        byte[] data =
        {
            (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
            (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
            (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
            (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
            (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13
        };

        int[] expectedValue = {66051, 67438087, 134810123, 202182159, 269554195};

        boolean valid = true;

        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(5);
        try
        {
            Object ret = conv.toObject(data, 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin4());
            Object obj = conv.toObject(data, 0);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 5)
                {
                    for (int i=0; i<5; ++i)
                    {
                        if (objArray[i] instanceof Integer)
                        {
                            Integer iValue = (Integer)objArray[i];
                            if (iValue.intValue() != expectedValue[i])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
                valid = false;
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid state: number of elements not set
     Result: ExtendedIllegalStateException thrown.
     **/
    public void Var089()
    {
        AS400Array conv = new AS400Array();
        conv.setType(new AS400Bin4());
        try
        {
            Object ret = conv.toObject(new byte[10], 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toObject(byte[], int) with invalid state: type not set, catch, fix, call again
     Result: Object array returned with valid value.
     **/
    public void Var090()
    {
        byte[] data =
        {
            (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
            (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
            (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
            (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
            (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13
        };

        int[] expectedValue = {66051, 67438087, 134810123, 202182159, 269554195};

        boolean valid = true;

        AS400Array conv = new AS400Array();
        conv.setNumberOfElements(5);
        try
        {
            Object ret = conv.toObject(data, 0);
            failed("Exception not thrown. ret="+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setType(new AS400Bin4());
            Object obj = conv.toObject(data, 0);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 5)
                {
                    for (int i=0; i<5; ++i)
                    {
                        if (objArray[i] instanceof Integer)
                        {
                            Integer iValue = (Integer)objArray[i];
                            if (iValue.intValue() != expectedValue[i])
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
                else
                {
                    valid = false;
                }
            }
            else
            {
                valid = false;
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toObject(byte[], int) with invalid parameter: as400Value null
     Result: NullPointerException thrown.
     **/
    public void Var091()
    {
        AS400Array conv = new AS400Array(new AS400UnsignedBin2(), 5);
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
}



