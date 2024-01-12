///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTTextTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import java.util.Locale;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

import test.Testcase;

/**
 Testcase DTTextTestcase.
 **/
public class DTTextTestcase extends Testcase
{
    // AS400Text needs an AS400 object to be run proxified; some variations don't use one.
    boolean proxified()
    {
        return systemObject_.getProxyServer().length() != 0;
    }


    /**
     Test: Construct an AS400Text(int) object with a valid parameter.
     Result: No exception should be thrown.
     **/
    public void Var001()
    {
        if (isApplet_)
        {
            notApplicable("Locale not settable in an applet.");
            return;
        }
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            Locale saveDefault = Locale.getDefault();
            try
            {
                boolean valid = true;
                Locale.setDefault(Locale.US);

                for (int i=0; i<100; ++i)
                {
                    AS400Text converter = new AS400Text(i);
                    if (converter.getCcsid() != 37)
                    {
                        valid = false;
                    }
                }
                assertCondition(valid);
            }
            finally
            {
                Locale.setDefault(saveDefault);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test: Construct an AS400Text(int) object with negative number length.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var002()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            AS400Text converter = new AS400Text(-1);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length (-1): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }


    /**
     Test: Construct an AS400Text(int, int) object with valid parameters.
     Result: No exception should be thrown.
     **/
    public void Var003()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            boolean valid = true;
            for (int i=0; i<100; ++i)
            {
                AS400Text converter = new AS400Text(i, 37);
                if (converter.getCcsid() != 37)
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
     Test: Construct an AS400Text(int, int) object with valid parameters.  Use a ccsid of 65535
     Result: No exception should be thrown.  Ccsid used should match default locale.
     **/
    public void Var004()
    {
        if (isApplet_)
        {
            notApplicable("Locale not settable in an applet.");
            return;
        }
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            Locale saveDefault = Locale.getDefault();
            try
            {
                Locale.setDefault(Locale.US);

                AS400Text converter = new AS400Text(53, 65535);
                assertCondition(converter.getCcsid() == 37);
            }
            finally
            {
                Locale.setDefault(saveDefault);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test: Construct an AS400Text(int, int) object with negative number length.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var005()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            AS400Text converter = new AS400Text(-1000, 37);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length (-1000): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }


    /**
     Test: Construct an AS400Text(int, int) object with an invalid ccsid.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var006()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            AS400Text converter = new AS400Text(100, -37);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "ccsid (-37): Parameter value is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }


    /**
     Test: Construct an AS400Text(int, int, AS400) object with valid parameters.
     Result: No exception should be thrown.
     **/
    public void Var007()
    {
        try
        {
            boolean valid = true;

            for (int i=0; i<100; ++i)
            {
                AS400Text converter = new AS400Text(i, 37, systemObject_);
                if (converter.getCcsid() != 37)
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
     Test: Construct an AS400Text(int, int, AS400) object with valid parameters.  Use a ccsid of 65535.
     Result: No exception should be thrown.  Ccsid used should match system's ccsid.
     **/
    public void Var008()
    {
        try
        {
            boolean valid = true;

            for (int i=0; i<100; ++i)
            {
                AS400Text converter = new AS400Text(i, 65535, systemObject_);
                if (converter.getCcsid() != systemObject_.getCcsid())
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
     Test: Construct an AS400Text(int, int, AS400) object with negative number length.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var009()
    {
        try
        {
            AS400Text converter = new AS400Text(-1000, 37, systemObject_);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length (-1000): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }


    /**
     Test: Construct an AS400Text(int, int, AS400) object with an invalid ccsid.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var010()
    {
        try
        {
            AS400Text converter = new AS400Text(100, -37, systemObject_);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "ccsid (-37): Parameter value is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }


    /**
     Test: Construct an AS400Text(int, int, AS400) object with a null system.
     Result: NullPointerException should be thrown.
     **/
    public void Var011()
    {
        try
        {
            AS400Text converter = new AS400Text(100, 37, (AS400)null);
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
     Test: Construct an AS400Text(int, string) object with valid parameters.
     Result: No exception should be thrown.
     **/
    public void Var012()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            boolean valid = true;
            for (int i=0; i<100; ++i)
            {
                AS400Text converter = new AS400Text(i, "Cp037");
                if (converter.getCcsid() != 37)
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
     Test: Construct an AS400Text(int, string) object with negative number length.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var013()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            AS400Text converter = new AS400Text(Integer.MIN_VALUE, "Cp037");
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "length (-2147483648): ", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
    }


    /**
     Test: Construct an AS400Text(int, string) object with an invalid encoding.
     Result: ExtendedIllegalArgumentException should be thrown.
     **/
    public void Var014()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            AS400Text converter = new AS400Text(100, "NoSuchThing");
            converter.toBytes("hello"); // This will validate the encoding for this AS400Text object; it doesn't happen on the ctor anymore.
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "encoding (NoSuchThing): Parameter value is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }


    /**
     Test: Construct an AS400Text(int, string) object with a null encoding.
     Result: NullPointerException should be thrown.
     **/
    public void Var015()
    {
        if (proxified())
        {
            notApplicable("Running proxified.");
            return;
        }
        try
        {
            AS400Text converter = new AS400Text(100, (String)null);
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
     Test: Call clone
     Result: Cloned object returned.
     **/
    public void Var016()
    {
        try
        {
            AS400Text conv = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
            AS400Text clone = (AS400Text)conv.clone();
            if (clone != null && clone != conv && clone.getByteLength() == 10 && clone.getCcsid() == conv.getCcsid() && clone.getEncoding().equals(conv.getEncoding()))
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
    public void Var017()
    {
        try
        {
            boolean valid = true;

            for (int i=0; i<100; ++i)
            {
                AS400Text conv = new AS400Text(i, 37, systemObject_);
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
     Test: Call getCcsid
     Result: The correct int value returned
     **/
    public void Var018()
    {
        if (isApplet_)
        {
            notApplicable("Locale not settable in an applet.");
            return;
        }
        Locale saveDefault = Locale.getDefault();
        String failMsg = "";
        try
        {
            Locale.setDefault(Locale.US);
            AS400Text conv = null;
            int ret = 0;

            if (!proxified())
            {
                conv = new AS400Text(10);
                ret = conv.getCcsid();
                if (ret != 37)
                {
                    failMsg += "\nAS400Text getCcsid() failed: Expected 37, returned "+ret+".";
                }

                conv = new AS400Text(10, 37);
                ret = conv.getCcsid();
                if (ret != 37)
                {
                    failMsg += "\nAS400Text (37) getCcsid() failed: Expected 37, returned "+ret+".";
                }

                conv = new AS400Text(10, "Cp037");
                ret = conv.getCcsid();
                if (ret != 37)
                {
                    failMsg += "\nAS400Text (Cp037) getCcsid() failed: Expected 37, returned "+ret+".";
                }
            }
            conv = new AS400Text(10, 37, systemObject_);
            ret = conv.getCcsid();
            if (ret != 37)
            {
                failMsg += "\nAS400Text (37, AS400) getCcsid() failed: Expected 37, returned "+ret+".";
            }

            if (failMsg.length() == 0)
                succeeded();
            else
                failed(failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }


    /**
     Test: Call getDefaultValue
     Result: An empty String is returned and the default value passes into toBytes without error.
     **/
    public void Var019()
    {
        try
        {
            AS400Text conv = new AS400Text(10, 37, systemObject_);
            Object ret = conv.getDefaultValue();

            if (ret instanceof String)
            {
                String dataValue = (String)ret;

                if (dataValue.compareTo("") != 0)
                {
                    failed("Incorrect return value");
                }
                else
                {
                    byte[] data = conv.toBytes(ret);
                    if (data.length == 10 &&
                        data[0] == (byte)0x40 &&
                        data[1] == (byte)0x40 &&
                        data[2] == (byte)0x40 &&
                        data[3] == (byte)0x40 &&
                        data[4] == (byte)0x40 &&
                        data[5] == (byte)0x40 &&
                        data[6] == (byte)0x40 &&
                        data[7] == (byte)0x40 &&
                        data[8] == (byte)0x40 &&
                        data[9] == (byte)0x40)
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
     Test: Call getEncoding
     Result: The correct string value returned
     **/
    public void Var020()
    {
        if (isApplet_)
        {
            notApplicable("Locale not settable in an applet.");
            return;
        }
        Locale saveDefault = Locale.getDefault();
        String failMsg = "";
        try
        {
            Locale.setDefault(Locale.US);
            AS400Text conv = null;
            String ret = "";
            if (!proxified())
            {
                conv = new AS400Text(10);
                ret = conv.getEncoding();
                if (ret.compareTo("Cp037") != 0)
                {
                    failMsg += "\nAS400Text getEncoding() failed: Expected Cp037, returned "+ret+".";
                }

                conv = new AS400Text(10, 37);
                ret = conv.getEncoding();
                if (ret.compareTo("Cp037") != 0)
                {
                    failMsg += "\nAS400Text (37) getEncoding() failed: Expected Cp037, returned "+ret+".";
                }

                conv = new AS400Text(10, "Cp037");
                ret = conv.getEncoding();
                if (ret.compareTo("Cp037") != 0)
                {
                    failMsg += "\nAS400Text (Cp037) getEncoding() failed: Expected Cp037, returned "+ret+".";
                }
            }
            conv = new AS400Text(10, 37, systemObject_);
            ret = conv.getEncoding();
            if (ret.compareTo("Cp037") != 0)
            {
                failMsg += "\nAS400Text (37, AS400) getEncoding() failed: Expected Cp037, returned "+ret+".";
            }

            if (failMsg.length() == 0)
                succeeded();
            else
                failed(failMsg);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }


    /**
     Test: Call toBytes(Object) with valid javaValue parameter.
     Result: Byte array returned with a valid value.
     **/
    public void Var021()
    {
        try
        {
            AS400Text conv = new AS400Text(8, 37, systemObject_);
            byte[] data = conv.toBytes("1234");
            if (data.length == 8 &&
                data[0] == (byte)0xF1 &&
                data[1] == (byte)0xF2 &&
                data[2] == (byte)0xF3 &&
                data[3] == (byte)0xF4 &&
                data[4] == (byte)0x40 &&
                data[5] == (byte)0x40 &&
                data[6] == (byte)0x40 &&
                data[7] == (byte)0x40)
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is not an instance of String.
     Result: ClassCastException thrown.
     **/
    public void Var022()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            byte[] ret = conv.toBytes(new Character('a'));
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
     Test: Call toBytes(Object) with invalid parameters: javaValue is too long.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var023()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            byte[] ret = conv.toBytes("1234567890");
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (1234567890): Length is not valid."))
            {
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
    public void Var024()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var025()
    {
        try
        {
            String[] testValue =
            {        
                /*  1 */"0",
                /*  2 */        "12",
                /*  3 */        "123",
                /*  4 */        "1234",
                /*  5 */        "12345",
                /*  6 */        "123456",
                /*  7 */        "1234567",
                /*  8 */        "12345678",
                /*  9 */        "123456789",
                /* 10 */        "1234567890",
                /* 11 */        "98765432101",
                /* 12 */        "987654321012",
                /* 13 */        "9876543210123",
                /* 14 */        "98765432101234",
                /* 15 */        "987654321012345",
                /* 16 */        "9876543210123456",
                /* 17 */        "98765432101234567",
                /* 18 */        "987654321012345678",
                /* 19 */        "9876543210123456789",
                /* 20 */        "98765432101234567890",
                /* 21 */        "112233445566778899001",
                /* 22 */        "2344556677889900112233",
                /* 23 */        "23344556677889900112233",
                /* 24 */        "345566778900112233445566",
                /* 25 */        "3445566778890112233445566",
                /* 26 */        "45667788990011223344556677",
                /* 27 */        "455667788990011223344556677",
                /* 28 */        "5677889900112233445566778899",
                /* 29 */        "56677889900112233445566778899",
                /* 30 */        "789900112233445566778899001122",
                /* 31 */        "7889900112233445566778899001122"
            };

            byte[][] expectedValue =
            {
                /*  1 */ {(byte)0xF0},
                /*  2 */ {(byte)0xF1,(byte)0xF2},
                /*  3 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3},
                /*  4 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /*  5 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5},
                /*  6 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6},
                /*  7 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7},
                /*  8 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8},
                /*  9 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9},
                /* 10 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0},
                /* 11 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1},
                /* 12 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2},
                /* 13 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3},
                /* 14 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /* 15 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5},
                /* 16 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6},
                /* 17 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7},
                /* 18 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8},
                /* 19 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9},
                /* 20 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0},
                /* 21 */ {(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1},
                /* 22 */ {(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3},
                /* 23 */ {(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3},
                /* 24 */ {(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6},
                /* 25 */ {(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6},
                /* 26 */ {(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7},
                /* 27 */ {(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7},
                /* 28 */ {(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9},
                /* 29 */ {(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9},
                /* 30 */ {(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2},
                /* 31 */ {(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2}
            };

            boolean valid = true;
            byte[] data = new byte[31];

            for (int i=0; i<31; ++i)
            {
                AS400Text conv = new AS400Text(i+1, 37, systemObject_);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: as400Value to small.
     Result: ArrayIndexOutOfBoundsException thrown.
     **/
    public void Var026()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("abcde", new byte[3]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is not an instance of String.
     Result: ClassCastException thrown.
     **/
    public void Var027()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes(new Character('a'), new byte[10]);
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
     Test: Call toBytes(Object, byte[]) with invalid parameters: javaValue is too long.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var028()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("1234567890", new byte[10]);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (1234567890): Length is not valid."))
            {
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
    public void Var029()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var030()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("abcde", null);
            failed("No exception thrown.");
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
    public void Var031()
    {
        try
        {
            boolean valid = true;
            AS400Text conv = new AS400Text(5, 37, systemObject_);
            String testValue = "";
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
            if (data[0] != (byte)0x40 ||
                data[1] != (byte)0x40 ||
                data[2] != (byte)0x40 ||
                data[3] != (byte)0x40 ||
                data[4] != (byte)0x40)
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
            if (data[3] != (byte)0x40 ||
                data[4] != (byte)0x40 ||
                data[5] != (byte)0x40 ||
                data[6] != (byte)0x40 ||
                data[7] != (byte)0x40)
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
            if (data[7]  != (byte)0x40 ||
                data[8]  != (byte)0x40 ||
                data[9]  != (byte)0x40 ||
                data[10] != (byte)0x40 ||
                data[11] != (byte)0x40)
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
    public void Var032()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("abcde", new byte[3], 0);
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
    public void Var033()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("abcde", new byte[7], 4);
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
    public void Var034()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes(new String("abcde"), new byte[10], -1);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is not an instance of String.
     Result: ClassCastException thrown.
     **/
    public void Var035()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes(new Character('a'), new byte[10], 0);
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
     Test: Call toBytes(Object, byte[], int) with invalid parameters: javaValue is too long.
     Result: ExtendedIllegalArgumentException thrown.
     **/
    public void Var036()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("1234567890", new byte[10], 0);
            failed("No exception thrown.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "javaValue (1234567890): Length is not valid."))
            {
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
    public void Var037()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var038()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
        try
        {
            int ret = conv.toBytes("abcde", null, 0);
            failed("No exception thrown.");
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
     Result: Byte array returned with valid value.
     **/
    public void Var039()
    {
        try
        {
            byte[][] data =
            {
                /*  1 */ {(byte)0xF0},
                /*  2 */ {(byte)0xF1,(byte)0xF2},
                /*  3 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3},
                /*  4 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /*  5 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5},
                /*  6 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6},
                /*  7 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7},
                /*  8 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8},
                /*  9 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9},
                /* 10 */ {(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0},
                /* 11 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1},
                /* 12 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2},
                /* 13 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3},
                /* 14 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4},
                /* 15 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5},
                /* 16 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6},
                /* 17 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7},
                /* 18 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8},
                /* 19 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9},
                /* 20 */ {(byte)0xF9,(byte)0xF8,(byte)0xF7,(byte)0xF6,(byte)0xF5,(byte)0xF4,(byte)0xF3,(byte)0xF2,(byte)0xF1,(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0},
                /* 21 */ {(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1},
                /* 22 */ {(byte)0xF2,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3},
                /* 23 */ {(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3},
                /* 24 */ {(byte)0xF3,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6},
                /* 25 */ {(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6},
                /* 26 */ {(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7},
                /* 27 */ {(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7},
                /* 28 */ {(byte)0xF5,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9},
                /* 29 */ {(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9},
                /* 30 */ {(byte)0xF7,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2},
                /* 31 */ {(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2,(byte)0xF3,(byte)0xF3,(byte)0xF4,(byte)0xF4,(byte)0xF5,(byte)0xF5,(byte)0xF6,(byte)0xF6,(byte)0xF7,(byte)0xF7,(byte)0xF8,(byte)0xF8,(byte)0xF9,(byte)0xF9,(byte)0xF0,(byte)0xF0,(byte)0xF1,(byte)0xF1,(byte)0xF2,(byte)0xF2}
            };

            String[] expectedValue =
            {        
                /*  1 */"0",
                /*  2 */        "12",
                /*  3 */        "123",
                /*  4 */        "1234",
                /*  5 */        "12345",
                /*  6 */        "123456",
                /*  7 */        "1234567",
                /*  8 */        "12345678",
                /*  9 */        "123456789",
                /* 10 */        "1234567890",
                /* 11 */        "98765432101",
                /* 12 */        "987654321012",
                /* 13 */        "9876543210123",
                /* 14 */        "98765432101234",
                /* 15 */        "987654321012345",
                /* 16 */        "9876543210123456",
                /* 17 */        "98765432101234567",
                /* 18 */        "987654321012345678",
                /* 19 */        "9876543210123456789",
                /* 20 */        "98765432101234567890",
                /* 21 */        "112233445566778899001",
                /* 22 */        "2344556677889900112233",
                /* 23 */        "23344556677889900112233",
                /* 24 */        "345566778900112233445566",
                /* 25 */        "3445566778890112233445566",
                /* 26 */        "45667788990011223344556677",
                /* 27 */        "455667788990011223344556677",
                /* 28 */        "5677889900112233445566778899",
                /* 29 */        "56677889900112233445566778899",
                /* 30 */        "789900112233445566778899001122",
                /* 31 */        "7889900112233445566778899001122"
            };

            boolean valid = true;

            for (int i=0; i<31; ++i)
            {
                AS400Text conv = new AS400Text(i+1, 37, systemObject_);
                Object obj = conv.toObject(data[i]);

                if (obj instanceof String)
                {
                    String sobj = (String)obj;
                    if (sobj.compareTo(expectedValue[i]) != 0)
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
    public void Var040()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var041()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var042()
    {
        try
        {
            byte[] data = new byte[6];
            data[0] = (byte)0xF8;
            data[1] = (byte)0xF9;
            data[2] = (byte)0xF1;
            data[3] = (byte)0xF2;
            data[4] = (byte)0xF5;
            data[5] = (byte)0xF7;

            String[] expectedValue =
            {
                "8912",
                "9125",
                "1257"
            };

            boolean valid = true;
            AS400Text converter = new AS400Text(4, 37, systemObject_);

            for (int i=0; i<3; ++i)
            {
                Object obj = converter.toObject(data, i);

                if (obj instanceof String)
                {
                    String dataValue = (String)obj;
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
    public void Var043()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var044()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var045()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
    public void Var046()
    {
        AS400Text conv = new AS400Text(5, 37, systemObject_);
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
     Test: Call toBytes() using a Bidi CCSID with different string types.
     Result: Variation should succeed.
     **/
    public void Var047()
    {
        try
        {
            AS400Text conv = new AS400Text(10, 420);
            String test = "1234";
            byte[] bufST5 = new byte[10];
            byte[] bufST6 = new byte[10];
            int numST5 = conv.toBytes(test, bufST5, 0, BidiStringType.ST5); // LTR
            int numST6 = conv.toBytes(test, bufST6, 0, BidiStringType.ST6); // RTL
            if (numST5 != 10 || numST6 != 10)
            {
                failed("Incorrect return value for Bidi toBytes(): "+numST5+","+numST6);
                return;
            }

            // Convert back.
            AS400Text conv37 = new AS400Text(10, 37);
            String strST5 = (String)conv37.toObject(bufST5);
            String strST6 = (String)conv37.toObject(bufST6);
            if (!strST5.equals("1234      "))
            {
                failed("LTR toBytes() failed: '"+strST5+"'");
                return;
            }
            if (!strST6.equals("      1234"))
            {
                failed("RTL toBytes() failed: '"+strST6+"'");
                return;
            }
            succeeded();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}



