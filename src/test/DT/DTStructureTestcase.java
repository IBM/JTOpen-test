///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTStructureTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import java.util.Vector;
import com.ibm.as400.access.AS400Array;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400Structure;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.ExtendedIllegalStateException;

import test.Testcase;

/**
 Testcase DTStructureTestcase.
 **/
public class DTStructureTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTStructureTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Test: Construct an AS400Structure object with null constructor.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        try
        {
            AS400Structure conv = new AS400Structure();
            assertCondition(true, "conv created "+conv); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Construct an AS400Structure object with valid parameter.
     Result: No exception should be thrown.
     **/
    public void Var002()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float8(),
                new AS400Text(40, 1383, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);
            assertCondition(true, "conv created "+conv); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Construct an AS400Structure object with a null parameter.
     Result: NullPointerException should be thrown.
     **/
    public void Var003()
    {
        try
        {
            AS400Structure conv = new AS400Structure(null);
            failed("Did not throw exception. conv="+conv);
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
     Test: Construct an AS400Structure object with a null member.
     Result: NullPointerException should be thrown.
     **/
    public void Var004()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                null,
                new AS400Float8(),
                // new AS400Text(40, "Cp1381")
                new AS400Text(40, 1381, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);
            failed("Did not throw exception. conv="+conv);
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
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);
            AS400Structure clone = (AS400Structure)conv.clone();
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
     Test: Call clone on unset object
     Result: Cloned object returned.
     **/
    public void Var006()
    {
        try
        {
            AS400Structure conv = new AS400Structure();
            AS400Structure clone = (AS400Structure)conv.clone();
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
     Result: The correct int value returned
     **/
    public void Var007()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            int ret = conv.getByteLength();
            if (ret == 16)
            {
                succeeded();
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
     Test: Call getByteLength with members unset
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var008()
    {
        AS400Structure conv = new AS400Structure();
        try
        {
            int ret = conv.getByteLength();
            failed("no exception thrown"+ret);
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
     Test: Call getByteLength with members unset, catch, fix, call again
     Result: The correct int value returned
     **/
    public void Var009()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            //new AS400Text(4, "Cp037")
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();

        try
        {
            int ret = conv.getByteLength();
            failed("no exception thrown"+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setMembers(struct);
            int ret = conv.getByteLength();
            if (ret == 16)
            {
                succeeded();
            }
            else
            {
                failed("Incorrect return value");
            }
        }
    }

    /**
     Test: Call getDefaultValue
     Result: An empty array is returned and the default value passes into toBytes without error.
     **/
    public void Var010()
    {
        try
        {
            AS400Structure conv = new AS400Structure(new AS400DataType[0]);
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
                failed("Incorrect return type");
            }
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
    }

    /**
     Test: Call getNumberOfMembers
     Result: The correct int value returned
     **/
    public void Var011()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            int ret = conv.getNumberOfMembers();
            if (ret == 4)
            {
                succeeded();
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
     Test: Call getNumberOfMembers, members is unset
     Result: The value -1 returned
     **/
    public void Var012()
    {
        try
        {
            AS400Structure conv = new AS400Structure();

            int ret = conv.getNumberOfMembers();
            if (ret == -1)
            {
                succeeded();
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
     Test: Call getMembers()
     Result: The correct array returned
     **/
    public void Var013()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)

            };
            AS400Structure conv = new AS400Structure(struct);

            AS400DataType[] ret = conv.getMembers();
            if (ret != struct && ret.length == 4 &&
                ret[0] != struct[0] && ret[0] instanceof AS400Bin4 &&
                ret[1] != struct[1] && ret[1].getByteLength() == 4 &&
                ret[2] != struct[2] && ret[2] instanceof AS400Float4 &&
                ret[3] != struct[3] && ret[3].getByteLength() == 4)
            {
                succeeded();
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
     Test: Call getMembers(), members unset
     Result: null returned
     **/
    public void Var014()
    {
        try
        {
            AS400Structure conv = new AS400Structure();

            AS400DataType[] ret = conv.getMembers();
            if (ret == null)
            {
                succeeded();
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
     Test: Call getMembers(int)
     Result: The correct array member returned
     **/
    public void Var015()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            AS400DataType ret0 = conv.getMembers(0);
            AS400DataType ret1 = conv.getMembers(1);
            AS400DataType ret2 = conv.getMembers(2);
            AS400DataType ret3 = conv.getMembers(3);
            if (ret0 != struct[0] && ret0 instanceof AS400Bin4 &&
                ret1 != struct[1] && ret1.getByteLength() == 4 &&
                ret2 != struct[2] && ret2 instanceof AS400Float4 &&
                ret3 != struct[3] && ret3.getByteLength() == 4)
            {
                succeeded();
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
     Test: Call getMembers(int) with invalid parameter: index less than zero.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var016()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        try
        {
            AS400DataType ret = conv.getMembers(-1);
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
     Test: Call getMembers(int) with invalid parameter: index greater than the number of members.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var017()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        try
        {
            AS400DataType ret = conv.getMembers(4);
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
     Test: Call getMembers(int) with invalid state: members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var018()
    {
        AS400Structure conv = new AS400Structure();
        try
        {
            conv.getMembers(0);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) with valid parameter.
     Result: No exception should be thrown.
     **/
    public void Var019()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float8(),
                new AS400Text(40, 1383, systemObject_)
            };
            AS400Structure conv = new AS400Structure();
            conv.setMembers(struct);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call setMembers(AS400DataType[]) after using non-null constructor
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var020()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            new AS400Text(40, 1383, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            conv.setMembers(struct);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) after using toBytes(Object)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var021()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toBytes(value);
        try
        {
            conv.setMembers(struct);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) after using toBytes(Object, byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var022()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toBytes(value, new byte[16]);
        try
        {
            conv.setMembers(struct);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) after using toBytes(Object, byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var023()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toBytes(value, new byte[16], 0);
        try
        {
            conv.setMembers(struct);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) after using toObject(byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var024()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toObject(new byte[16]);
        try
        {
            conv.setMembers(struct);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) after using toObject(byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var025()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toObject(new byte[16], 0);
        try
        {
            conv.setMembers(struct);
            failed("no exception thrown");
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
     Test: Call setMembers(AS400DataType[]) with a null parameter.
     Result: NullPointerException should be thrown.
     **/
    public void Var026()
    {
        try
        {
            AS400Structure conv = new AS400Structure();
            conv.setMembers(null);
            failed("Did not throw exception. conv="+conv);
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
     Test: Call setMembers(AS400DataType[]) with a null member.
     Result: NullPointerException should be thrown.
     **/
    public void Var027()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                null,
                new AS400Float8(),
                //new AS400Text(40, "Cp1381")
                new AS400Text(40, 1381, systemObject_)
            };
            AS400Structure conv = new AS400Structure();
            conv.setMembers(struct);
            failed("Did not throw exception. conv="+conv);
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
     Test: Call setMembers(int, AS400DataType) with valid parameters.
     Result: No exception should be thrown.
     **/
    public void Var028()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float8(),
                new AS400Text(40, 1383, systemObject_)
            };
            AS400Structure conv = new AS400Structure();
            conv.setMembers(struct);
            conv.setMembers(0, struct[0]);
            conv.setMembers(1, struct[1]);
            conv.setMembers(2, struct[2]);
            conv.setMembers(3, struct[3]);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test: Call setMembers(int, AS400DataType[]) with invalid parameter: index less than zero.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var029()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);

        try
        {
            conv.setMembers(-1, struct[0]);
            failed("Did not throw exception. conv="+conv);
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
     Test: Call setMembers(int, AS400DataType[]) with invalid parameter: index greater than the number of members.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var030()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);

        try
        {
            conv.setMembers(4, struct[0]);
            failed("Did not throw exception. conv="+conv);
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
     Test: Call setMembers(int, AS400DataType) with members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var031()
    {
        AS400Structure conv = new AS400Structure();
        try
        {
            conv.setMembers(0, new AS400Bin4());
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) after using non-null constructor
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var032()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            new AS400Text(40, 1383, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            conv.setMembers(0, struct[0]);
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) after using toBytes(Object)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var033()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(40, 1383, systemObject_)
        };
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toBytes(value);
        try
        {
            conv.setMembers(0, struct[0]);
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) after using toBytes(Object, byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var034()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toBytes(value, new byte[16]);
        try
        {
            conv.setMembers(0, struct[0]);
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) after using toBytes(Object, byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var035()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toBytes(value, new byte[16], 0);
        try
        {
            conv.setMembers(0, struct[0]);
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) after using toObject(byte[])
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var036()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toObject(new byte[16]);
        try
        {
            conv.setMembers(0, struct[0]);
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) after using toObject(byte[], int)
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var037()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        conv.toObject(new byte[16], 0);
        try
        {
            conv.setMembers(0, struct[0]);
            failed("no exception thrown");
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
     Test: Call setMembers(int, AS400DataType) with a null parameter.
     Result: NullPointerException should be thrown.
     **/
    public void Var038()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            new AS400Text(40, 1383, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        conv.setMembers(struct);
        try
        {
            conv.setMembers(2, null);
            failed("Did not throw exception. conv="+conv);
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
    public void Var039()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            Object[] value =
            {
                Integer.valueOf(88),
                new byte[0],
                Float.valueOf(-1.5f),
                "1234"
            };

            byte[] data = conv.toBytes(value);
            if (data.length ==  16 &&
                data[0]  == (byte)0x00 &&
                data[1]  == (byte)0x00 &&
                data[2]  == (byte)0x00 &&
                data[3]  == (byte)0x58 &&
                data[4]  == (byte)0x00 &&
                data[5]  == (byte)0x00 &&
                data[6]  == (byte)0x00 &&
                data[7]  == (byte)0x00 &&
                data[8]  == (byte)0xBF &&
                data[9]  == (byte)0xC0 &&
                data[10] == (byte)0x00 &&
                data[11] == (byte)0x00 &&
                data[12] == (byte)0xF1 &&
                data[13] == (byte)0xF2 &&
                data[14] == (byte)0xF3 &&
                data[15] == (byte)0xF4)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of Object[].
     Result: ClassCastException thrown.
     **/
    public void Var040()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            byte[] ret = conv.toBytes(new Vector<Object>());
            failed("Did not throw exception. ret="+ret+ret);
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
     Test: Call toBytes(Object) with invalid parameters: an element of javaValue is not an instance of the correct type.
     Result: ClassCastException thrown.
     **/
    public void Var041()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Double.valueOf(-1.5),
            "1234"
        };
        try
        {
            byte[] ret = conv.toBytes(value);
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
     Test: Call toBytes(Object) with invalid parameters: javaValue has an incorrect number of elements.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var042()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
        };
        try
        {
            byte[] ret = conv.toBytes(value);
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
     Test: Call toBytes(Object) with invalid parameters: javaValue has an incorrect element.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var043()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "123456"
        };
        try
        {
            byte[] ret = conv.toBytes(value);
            failed("Did not throw exception. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123456): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object) with invalid state: members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var044()
    {
        AS400Structure conv = new AS400Structure();
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            byte[] ret = conv.toBytes(value);
            failed("no exception thrown"+ret);
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
     Test: Call toBytes(Object) with invalid state: members not set, catch, fix, and call again
     Result: Byte array returned with a valid value.
     **/
    public void Var045()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            byte[] data = conv.toBytes(value);
            failed("no exception thrown"+data);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setMembers(struct);
            byte[] data = conv.toBytes(value);
            if (data.length ==  16 &&
                data[0]  == (byte)0x00 &&
                data[1]  == (byte)0x00 &&
                data[2]  == (byte)0x00 &&
                data[3]  == (byte)0x58 &&
                data[4]  == (byte)0x00 &&
                data[5]  == (byte)0x00 &&
                data[6]  == (byte)0x00 &&
                data[7]  == (byte)0x00 &&
                data[8]  == (byte)0xBF &&
                data[9]  == (byte)0xC0 &&
                data[10] == (byte)0x00 &&
                data[11] == (byte)0x00 &&
                data[12] == (byte)0xF1 &&
                data[13] == (byte)0xF2 &&
                data[14] == (byte)0xF3 &&
                data[15] == (byte)0xF4)
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
     Test: Call toBytes(Object) with invalid parameters: an element of javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var046()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            null,
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            byte[] ret = conv.toBytes(value);
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var047()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
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
     Test: Call toBytes(Object, byte[]) with valid parameters.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var048()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            Object[] value =
            {
                Integer.valueOf(88),
                new byte[0],
                Float.valueOf(-1.5f),
                "1234"
            };

            byte[] expectedValue =
            {
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x58,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00,
                (byte)0xF1, (byte)0xF2, (byte)0xF3, (byte)0xF4
            };

            boolean valid = true;
            byte[] data = new byte[16];

            int ret = conv.toBytes(value, data);

            if (ret != 16)
            {
                valid = false;
            }
            for (int i=0; i<16; ++i)
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
    public void Var049()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };

        try
        {
            int ret = conv.toBytes(value, new byte[15]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of Object[].
     Result: ClassCastException thrown.
     **/
    public void Var050()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        try
        {
            int ret = conv.toBytes(new Vector<Object>(), new byte[16]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: an element of javaValue is not an instance of the correct type.
     Result: ClassCastException thrown.
     **/
    public void Var051()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Double.valueOf(-1.5),
            "1234"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[20]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue has an incorrect number of elements.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var052()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue has an incorrect element.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var053()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "123456"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16]);
            failed("Did not throw exception. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123456): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[]) with invalid state: members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var054()
    {
        AS400Structure conv = new AS400Structure();
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16]);
            failed("no exception thrown"+ret);
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
     Test: Call toBytes(Object, byte[]) with invalid state: members not set, catch, fix and call again
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var055()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        byte[] expectedValue =
        {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x58,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00,
            (byte)0xF1, (byte)0xF2, (byte)0xF3, (byte)0xF4
        };

        boolean valid = true;
        byte[] data = new byte[16];
        try
        {
            int ret = conv.toBytes(value, data);
            failed("no exception thrown"+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setMembers(struct);
            int ret = conv.toBytes(value, data);

            if (ret != 16)
            {
                valid = false;
            }
            for (int i=0; i<16; ++i)
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: an element of javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var056()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            null,
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var057()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        try
        {
            int ret = conv.toBytes(null, new byte[16]);
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
    public void Var058()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };

        try
        {
            int ret = conv.toBytes(value, null);
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
     Result: Length is returned, valid bytes are written in the array.
     **/
    public void Var059()
    {
        try
        {
            boolean valid = true;

            AS400DataType[] struct =
            {
                new AS400Bin2(),
                new AS400ByteArray(2)
            };
            AS400Structure conv = new AS400Structure(struct);

            Object[] value =
            {
                Short.valueOf((short)0),
                new byte[0]
            };

            byte[] data = new byte[12];

            for (int i = 0; i < 12; ++i)
            {
                data[i] = (byte)0xEE;
            }
            int ret = conv.toBytes(value, data, 0);
            if (ret != 4)
            {
                valid = false;
            }
            if (data[0] != (byte)0x00 ||
                data[1] != (byte)0x00 ||
                data[2] != (byte)0x00 ||
                data[3] != (byte)0x00)
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
            ret = conv.toBytes(value, data, 3);
            if (ret != 4)
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
                data[6] != (byte)0x00)
            {
                valid = false;
            }
            for (int i = 7; i < 12; ++i)
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
            ret = conv.toBytes(value, data, 8);
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
            if (data[8]  != (byte)0x00 ||
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
     Test: Call toBytes(Object, byte[], int) with valid parameters: Structure in structure.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var060()
    {
        try
        {
            AS400DataType[] innerStruct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };

            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400Structure(innerStruct),
                new AS400Float4(),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            Object[] innerValue =
            {
                Integer.valueOf(88),
                new byte[0],
                Float.valueOf(-1.5f),
                "1234"
            };

            Object[] value =
            {
                Integer.valueOf(99),
                innerValue,
                Float.valueOf(1.5f),
                "5678"
            };

            byte[] expectedValue =
            {
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x63,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x58,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00,
                (byte)0xF1, (byte)0xF2, (byte)0xF3, (byte)0xF4,
                (byte)0x3F, (byte)0xC0, (byte)0x00, (byte)0x00,
                (byte)0xF5, (byte)0xF6, (byte)0xF7, (byte)0xF8
            };

            boolean valid = true;
            byte[] data = new byte[28];

            int ret = conv.toBytes(value, data);

            if (ret != 28)
            {
                valid = false;
            }
            for (int i=0; i<28; ++i)
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
     Test: Call toBytes(Object, byte[], int) with valid parameters: Structure with arrays.
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var061()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Array(new AS400Float4(), 2),
                //new AS400Text(4, "Cp037")
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            Object[] innerValue =
            {
                Float.valueOf(-1.5f),
                Float.valueOf(1.5f),
            };

            Object[] value =
            {
                Integer.valueOf(99),
                new byte[0],
                innerValue,
                "5678"
            };

            byte[] expectedValue =
            {
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x63,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00,
                (byte)0x3F, (byte)0xC0, (byte)0x00, (byte)0x00,
                (byte)0xF5, (byte)0xF6, (byte)0xF7, (byte)0xF8
            };

            boolean valid = true;
            byte[] data = new byte[20];

            int ret = conv.toBytes(value, data);

            if (ret != 20)
            {
                valid = false;
            }
            for (int i=0; i<20; ++i)
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
    public void Var062()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };

        try
        {
            int ret = conv.toBytes(value, new byte[15]);
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
    public void Var063()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };

        try
        {
            int ret = conv.toBytes(value, new byte[25], 15);
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
    public void Var064()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };

        try
        {
            int ret = conv.toBytes(value, new byte[25], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of Object[].
     Result: ClassCastException thrown.
     **/
    public void Var065()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        try
        {
            int ret = conv.toBytes(new Vector<Object>(), new byte[16], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: an element of javaValue is not an instance of the correct type.
     Result: ClassCastException thrown.
     **/
    public void Var066()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Double.valueOf(-1.5),
            "1234"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[20], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue has an incorrect number of elements.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var067()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue has an incorrect element.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var068()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "123456"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16], 0);
            failed("Did not throw exception. ret="+ret);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (123456): Length is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /**
     Test: Call toBytes(Object, byte[], int) with invalid state: members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var069()
    {
        AS400Structure conv = new AS400Structure();
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16], 0);
            failed("no exception thrown"+ret);
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
     Test: Call toBytes(Object, byte[], int) with invalid state: members not set, catch, fix and call again
     Result: The length is returned, valid bytes are written in the array.
     **/
    public void Var070()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure();
        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        byte[] expectedValue =
        {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x58,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xBF, (byte)0xC0, (byte)0x00, (byte)0x00,
            (byte)0xF1, (byte)0xF2, (byte)0xF3, (byte)0xF4
        };

        boolean valid = true;
        byte[] data = new byte[16];
        try
        {
            int ret = conv.toBytes(value, data, 0);
            failed("no exception thrown"+ret);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setMembers(struct);
            int ret = conv.toBytes(value, data, 0);

            if (ret != 16)
            {
                valid = false;
            }
            for (int i=0; i<16; ++i)
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: an element of javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var071()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object[] value =
        {
            null,
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };
        try
        {
            int ret = conv.toBytes(value, new byte[16], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is null.
     Result: NullPointerException thrown.
     **/
    public void Var072()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        try
        {
            int ret = conv.toBytes(null, new byte[16], 0);
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
    public void Var073()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float4(),
            new AS400Text(4, 37, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);

        Object[] value =
        {
            Integer.valueOf(88),
            new byte[0],
            Float.valueOf(-1.5f),
            "1234"
        };

        try
        {
            int ret = conv.toBytes(value, null, 0);
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
     Result: Object array returned with valid value.
     **/
    public void Var074()
    {
        try
        {
            AS400DataType[] struct =
            {
                new AS400Bin4(),
                new AS400ByteArray(4),
                new AS400Float8(),
                new AS400Text(4, 37, systemObject_)
            };
            AS400Structure conv = new AS400Structure(struct);

            byte[] data =
            {
                (byte)0x12, (byte)0x12, (byte)0x12, (byte)0x12,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xF0, (byte)0xF1, (byte)0xF2, (byte)0xF3
            };

            boolean valid = true;
            Object obj = conv.toObject(data);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 4)
                {
                    if (objArray[0] instanceof Integer)
                    {
                        Integer iValue = (Integer)objArray[0];
                        if (iValue.intValue() != 303174162)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[1] instanceof byte[])
                    {
                        byte[] bValue = (byte[])objArray[1];
                        if (bValue[0] != (byte)0xFF ||
                            bValue[1] != (byte)0xFF ||
                            bValue[2] != (byte)0xFF ||
                            bValue[3] != (byte)0xFF)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[2] instanceof Double)
                    {
                        Double dValue = (Double)objArray[2];
                        if (dValue.doubleValue() != 0.0)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[3] instanceof String)
                    {
                        String sValue = (String)objArray[3];
                        if (sValue.compareTo("0123") != 0)
                        {
                            valid = false;
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
     Test: Call toObject(byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var075()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            //new AS400Text(4, "Cp277")
            new AS400Text(4, 277, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            Object obj = conv.toObject(new byte[8]);
            failed("no exception thrown for to small Structure"+obj);
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
     Test: Call toObject(byte[]) with invalid state: members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var076()
    {
        AS400Structure conv = new AS400Structure();
        try
        {
            Object obj = conv.toObject(new byte[18]);
            failed("no exception thrown"+obj);
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
     Test: Call toObject(byte[]) with invalid state: members not set, catch, fix, and call again
     Result: Object array returned with valid value.
     **/
    public void Var077()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            new AS400Text(4, 37, systemObject_)
        };
        byte[] data =
        {
            (byte)0x12, (byte)0x12, (byte)0x12, (byte)0x12,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xF0, (byte)0xF1, (byte)0xF2, (byte)0xF3
        };

        boolean valid = true;
        AS400Structure conv = new AS400Structure();
        try
        {
            Object obj = conv.toObject(data);
            failed("no exception thrown"+obj);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setMembers(struct);
            Object obj = conv.toObject(data);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 4)
                {
                    if (objArray[0] instanceof Integer)
                    {
                        Integer iValue = (Integer)objArray[0];
                        if (iValue.intValue() != 303174162)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[1] instanceof byte[])
                    {
                        byte[] bValue = (byte[])objArray[1];
                        if (bValue[0] != (byte)0xFF ||
                            bValue[1] != (byte)0xFF ||
                            bValue[2] != (byte)0xFF ||
                            bValue[3] != (byte)0xFF)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[2] instanceof Double)
                    {
                        Double dValue = (Double)objArray[2];
                        if (dValue.doubleValue() != 0.0)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[3] instanceof String)
                    {
                        String sValue = (String)objArray[3];
                        if (sValue.compareTo("0123") != 0)
                        {
                            valid = false;
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
            else
            {
                valid = false;
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toObject(byte[]) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var078()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            //new AS400Text(4, "Cp277")
            new AS400Text(4, 277, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
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
     Result: Byte Structure returned with valid value.
     **/
    public void Var079()
    {
        try
        {
            AS400DataType[][] struct =
            {
                {new AS400Bin4(), new AS400ByteArray(4)},
                {new AS400ByteArray(4), new AS400Float4()},
                {new AS400Float4(), /*new AS400Text(4, "Cp037")*/ new AS400Text(4, 37, systemObject_)}
            };


            AS400Structure[] conv =
            {
                new AS400Structure(struct[0]),
                new AS400Structure(struct[1]),
                new AS400Structure(struct[2])
            };

            byte[] data =
            {
                (byte)0x12, (byte)0x12, (byte)0x12, (byte)0x12,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xF0, (byte)0xF1, (byte)0xF2, (byte)0xF3
            };

            boolean valid = true;

            Object obj = conv[0].toObject(data, 0);
            if (obj instanceof Object[])
            {
                Object[] vect = (Object[])obj;

                if (vect.length == 2)
                {
                    if (vect[0] instanceof Integer)
                    {
                        Integer iValue = (Integer)vect[0];
                        if (iValue.intValue() != 303174162)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }
                    if (vect[1] instanceof byte[])
                    {
                        byte[] bValue = (byte[])vect[1];
                        if (bValue[0] != (byte)0xFF ||
                            bValue[1] != (byte)0xFF ||
                            bValue[2] != (byte)0xFF ||
                            bValue[3] != (byte)0xFF)
                        {
                            valid = false;
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
            else
            {
                valid = false;
            }

            obj = conv[1].toObject(data, 4);
            if (obj instanceof Object[])
            {
                Object[] vect = (Object[])obj;

                if (vect.length == 2)
                {
                    if (vect[0] instanceof byte[])
                    {
                        byte[] bValue = (byte[])vect[0];
                        if (bValue[0] != (byte)0xFF ||
                            bValue[1] != (byte)0xFF ||
                            bValue[2] != (byte)0xFF ||
                            bValue[3] != (byte)0xFF)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }
                    if (vect[1] instanceof Float)
                    {
                        Float fValue = (Float)vect[1];
                        if (fValue.floatValue() != 0.0f)
                        {
                            valid = false;
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
            else
            {
                valid = false;
            }

            obj = conv[2].toObject(data, 8);
            if (obj instanceof Object[])
            {
                Object[] vect = (Object[])obj;

                if (vect.length == 2)
                {
                    if (vect[0] instanceof Float)
                    {
                        Float fValue = (Float)vect[0];
                        if (fValue.floatValue() != 0.0f)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }
                    if (vect[1] instanceof String)
                    {
                        String sValue = (String)vect[1];
                        if (sValue.compareTo("0123") != 0)
                        {
                            valid = false;
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
     Test: Call toObject(byte[], int) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var080()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            //new AS400Text(4, "Cp277")
            new AS400Text(4, 277, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            Object obj = conv.toObject(new byte[8], 0);
            failed("no exception thrown for to small Structure"+obj);
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
    public void Var081()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            //new AS400Text(4, "Cp277")
            new AS400Text(4, 277, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            Object obj = conv.toObject(new byte[20], 1);
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
    public void Var082()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            //new AS400Text(4, "Cp277")
            new AS400Text(4, 277, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        try
        {
            Object obj = conv.toObject(new byte[25], -10);
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
     Test: Call toObject(byte[], int) with invalid state: members not set
     Result: ExtendedIllegalStateException thrown
     **/
    public void Var083()
    {
        AS400Structure conv = new AS400Structure();
        try
        {
            Object obj = conv.toObject(new byte[25], 0);
            failed("no exception thrown"+obj);
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
     Test: Call toObject(byte[], int) with invalid state: members not set, catch, fix, and call again
     Result: Object array returned with valid value.
     **/
    public void Var084()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            new AS400Text(4, 37, systemObject_)
        };
        byte[] data =
        {
            (byte)0x12, (byte)0x12, (byte)0x12, (byte)0x12,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xF0, (byte)0xF1, (byte)0xF2, (byte)0xF3
        };

        boolean valid = true;
        AS400Structure conv = new AS400Structure();
        try
        {
            Object obj = conv.toObject(data, 0);
            failed("no exception thrown"+obj);
        }
        catch (ExtendedIllegalStateException e)
        {
            conv.setMembers(struct);
            Object obj = conv.toObject(data, 0);

            if (obj instanceof Object[])
            {
                Object[] objArray = (Object[])obj;

                if (objArray.length == 4)
                {
                    if (objArray[0] instanceof Integer)
                    {
                        Integer iValue = (Integer)objArray[0];
                        if (iValue.intValue() != 303174162)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[1] instanceof byte[])
                    {
                        byte[] bValue = (byte[])objArray[1];
                        if (bValue[0] != (byte)0xFF ||
                            bValue[1] != (byte)0xFF ||
                            bValue[2] != (byte)0xFF ||
                            bValue[3] != (byte)0xFF)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[2] instanceof Double)
                    {
                        Double dValue = (Double)objArray[2];
                        if (dValue.doubleValue() != 0.0)
                        {
                            valid = false;
                        }
                    }
                    else
                    {
                        valid = false;
                    }

                    if (objArray[3] instanceof String)
                    {
                        String sValue = (String)objArray[3];
                        if (sValue.compareTo("0123") != 0)
                        {
                            valid = false;
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
            else
            {
                valid = false;
            }

            assertCondition(valid);
        }
    }

    /**
     Test: Call toObject(byte[], int) with invalid parameters: as400Value null.
     Result: NullPointerException thrown.
     **/
    public void Var085()
    {
        AS400DataType[] struct =
        {
            new AS400Bin4(),
            new AS400ByteArray(4),
            new AS400Float8(),
            //new AS400Text(4, "Cp277")
            new AS400Text(4, 277, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
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
     Test: Call getDefaultValue
     Result: An array is returned and the default value passes into toBytes without error.
     **/
    public void Var086()
    {
      try
      {

      { // First pass.
        AS400DataType[] struct =
        {
          new AS400ByteArray(4),
          new AS400Bin4(),
          new AS400Float8(),
          new AS400Text(40, 1383, systemObject_)
        };
        AS400Structure conv = new AS400Structure(struct);
        Object ret = conv.getDefaultValue();

        if (ret instanceof Object[])
        {
          Object[] dataValue = (Object[])ret;

          if (dataValue.length != 4)
          {
            failed("[1] Incorrect return value. Returned array length == " + dataValue.length);
            return;
          }
          else
          {
            // Check the first element (a byte array).
            if (!java.util.Arrays.equals((byte[])dataValue[0], (byte[])struct[0].getDefaultValue()))
            {
              failed("[1] Incorrect return type. Type of dataValue["+0+"] is a " + dataValue[0].getClass().getName());
              return;
            }
            // Check the remaining elements.
            for (int i=1; i<dataValue.length; i++)
            {
              if (!dataValue[i].equals(struct[i].getDefaultValue()))
              {
                failed("[1] Incorrect return type. Type of dataValue["+i+"] is a " + dataValue[i].getClass().getName());
                return;
              }
            }

            byte[] data = conv.toBytes(ret);
            if (data.length != 56)
            {
              failed("[1] Unexpected byte length of converted value: " + data.length);
              return;
            }
          }
        }
        else
        {
          failed("[1] Incorrect return type");
          return;
        }
      }

        { // Second pass, with different structure.
          AS400DataType[] struct =
          {
            new AS400Bin2(),
            new AS400Bin8(),
            new AS400UnsignedBin4(),
            new AS400UnsignedBin2(),
            new AS400Bin8()
          };
          AS400Structure conv = new AS400Structure(struct);
          Object ret = conv.getDefaultValue();

          if (ret instanceof Object[])
          {
            Object[] dataValue = (Object[])ret;

            if (dataValue.length != 5)
            {
              failed("[2] Incorrect return value. Returned array length == " + dataValue.length);
              return;
            }
            else
            {
              // Check the elements.
              for (int i=0; i<dataValue.length; i++)
              {
                if (!dataValue[i].equals(struct[i].getDefaultValue()))
                {
                  failed("[2] Incorrect return type. Type of dataValue["+i+"] is a " + dataValue[i].getClass().getName());
                  return;
                }
              }

              byte[] data = conv.toBytes(ret);
              if (data.length != 24)
              {
                failed("[2] Unexpected byte length of converted value: " + data.length);
                return;
              }
            }
          }
          else
          {
            failed("[2] Incorrect return type");
            return;
          }
        }
        succeeded();
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception");
      }
    }



}



