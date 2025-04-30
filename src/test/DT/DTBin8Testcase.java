///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTBin8Testcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import com.ibm.as400.access.AS400Bin8;

import test.Testcase;

/**
 Testcase DTBin8Testcase.
 **/
public class DTBin8Testcase extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DTBin8Testcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DTTest.main(newArgs); 
   }
    /**
     Constructor - No exception should be thrown.
     **/
    public void Var001() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            assertCondition(true,"Able to create converted "+converter); 
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }



    /**
     clone() - Works.
     **/
    public void Var002() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            AS400Bin8 clone = (AS400Bin8)converter.clone();
            assertCondition(clone != null && clone != converter);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }



    /**
     getByteLength() - Should return 8.
     **/
    public void Var003() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            int length = converter.getByteLength();
            assertCondition(length == 8);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**
     getDefaultValue() - Returns 0 as a Long.
     **/
    public void Var004() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            Long value = (Long)converter.getDefaultValue();
            assertCondition(value.longValue() == 0);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**
     getDefaultValue() - The return value can be appropriately convertable.
     **/
    public void Var005() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            Object value = converter.getDefaultValue();
            byte[] data = converter.toBytes(value);
            assertCondition(data.length == 8 && data[0] == 0 
                            && data[1] == 0
                            && data[2] == 0
                            && data[3] == 0
                            && data[4] == 0
                            && data[5] == 0
                            && data[6] == 0
                            && data[7] == 0);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**
     toBytes(Object) - Pass an invalid object.
     **/
    public void Var006() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Integer(0));
            failed("No exception thrown."+data);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ClassCastException");
        }
    }



    /**
     toBytes(Object) - Pass null.
     **/
    public void Var007() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(null);
            failed("No exception thrown."+data);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, the largest possible negative number.
     **/
    public void Var008() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(Long.MIN_VALUE));                
            assertCondition(data.length == 8 && data[0] == (byte)0x80 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x00
                            && data[7] == (byte)0x00);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, a large negative number.
     **/
    public void Var009() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(-2433234545462216467L));                
            assertCondition(data.length == 8 && data[0] == (byte)0xDE 
                            && data[1] == (byte)0x3B
                            && data[2] == (byte)0x6A
                            && data[3] == (byte)0x12
                            && data[4] == (byte)0x9E
                            && data[5] == (byte)0x83
                            && data[6] == (byte)0xF8
                            && data[7] == (byte)0xED);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, a small negative number.
     **/
    public void Var010() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(-8L));                
            assertCondition(data.length == 8 && data[0] == (byte)0xFF 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFF
                            && data[4] == (byte)0xFF
                            && data[5] == (byte)0xFF
                            && data[6] == (byte)0xFF
                            && data[7] == (byte)0xF8);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, zero.
     **/
    public void Var011() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(0));                
            assertCondition(data.length == 8 && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x00
                            && data[7] == (byte)0x00);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, a small positive number.
     **/
    public void Var012() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(46L));                
            assertCondition(data.length == 8 && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x00
                            && data[7] == (byte)0x2E);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, a large positive number.
     **/
    public void Var013() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(4645645787888794345L));                
            assertCondition(data.length == 8 && data[0] == (byte)0x40 
                            && data[1] == (byte)0x78
                            && data[2] == (byte)0xA6
                            && data[3] == (byte)0x3A
                            && data[4] == (byte)0xFB
                            && data[5] == (byte)0x66
                            && data[6] == (byte)0xBA
                            && data[7] == (byte)0xE9);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object) - Pass a valid value, the largest positive number.
     **/
    public void Var014() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(new Long(Long.MAX_VALUE));                
            assertCondition(data.length == 8 && data[0] == (byte)0x7F 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFF
                            && data[4] == (byte)0xFF
                            && data[5] == (byte)0xFF
                            && data[6] == (byte)0xFF
                            && data[7] == (byte)0xFF);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, the largest possible negative number.
     **/
    public void Var015() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(Long.MIN_VALUE);
            assertCondition(data.length == 8 && data[0] == (byte)0x80 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x00
                            && data[7] == (byte)0x00);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, a large negative number.
     **/
    public void Var016() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(-5663324334563454435L);
            assertCondition(data.length == 8 && data[0] == (byte)0xB1 
                            && data[1] == (byte)0x67
                            && data[2] == (byte)0xD4
                            && data[3] == (byte)0x85
                            && data[4] == (byte)0xF6
                            && data[5] == (byte)0x85
                            && data[6] == (byte)0x12
                            && data[7] == (byte)0x1D);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, a small negative number.
     **/
    public void Var017() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(-67);
            assertCondition(data.length == 8 && data[0] == (byte)0xFF 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFF
                            && data[4] == (byte)0xFF
                            && data[5] == (byte)0xFF
                            && data[6] == (byte)0xFF
                            && data[7] == (byte)0xBD);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, zero.
     **/
    public void Var018() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(0);                
            assertCondition(data.length == 8 && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x00
                            && data[7] == (byte)0x00);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, a small positive number.
     **/
    public void Var019() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(9);                
            assertCondition(data.length == 8 && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x00
                            && data[7] == (byte)0x09);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, a large positive number.
     **/
    public void Var020() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(9886874373265456L);                
            assertCondition(data.length == 8 && data[0] == (byte)0x00 
                            && data[1] == (byte)0x23
                            && data[2] == (byte)0x20
                            && data[3] == (byte)0x0F
                            && data[4] == (byte)0x52
                            && data[5] == (byte)0xF5
                            && data[6] == (byte)0x18
                            && data[7] == (byte)0x30);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long) - Pass a valid value, the largest positive number.
     **/
    public void Var021() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = converter.toBytes(Long.MAX_VALUE);    
            assertCondition(data.length == 8 && data[0] == (byte)0x7F 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFF
                            && data[4] == (byte)0xFF
                            && data[5] == (byte)0xFF
                            && data[6] == (byte)0xFF
                            && data[7] == (byte)0xFF);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object, byte[]) - Pass null for the Object.
     **/
    public void Var022() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(null, data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(Object, byte[]) - Pass an invalid value for the Object.
     **/
    public void Var023() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes("Hi Mom!", data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ClassCastException");
        }
    }



    /**                
     toBytes(Object, byte[]) - Pass null for the byte[].
     **/
    public void Var024() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            int length = converter.toBytes(new Long(543), data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(Object, byte[]) - Pass empty array for the byte[].
     **/
    public void Var025() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            int length = converter.toBytes(new Long(-45), data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(Object, byte[]) - Pass a 7-byte (too short) array for the byte[].
     **/
    public void Var026() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            int length = converter.toBytes(new Long(30), data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }




    /**                
     toBytes(Object, byte[]) - Pass an 8-byte (right size) array for the byte[].
     **/
    public void Var027() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(new Long(-4543), data);       
            assertCondition(length == 8      && data[0] == (byte)0xFF 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFF
                            && data[4] == (byte)0xFF
                            && data[5] == (byte)0xFF
                            && data[6] == (byte)0xEE
                            && data[7] == (byte)0x41);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }




    /**                
     toBytes(Object, byte[]) - Pass 9-byte (too long) array for the byte[].
     **/
    public void Var028() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[9];
            data[8] = (byte)0x75;
            int length = converter.toBytes(new Long(4543), data);       
            assertCondition(length == 8      && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x00
                            && data[5] == (byte)0x00
                            && data[6] == (byte)0x11
                            && data[7] == (byte)0xBF
                            && data[8] == (byte)0x75);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long, byte[]) - Pass null for the byte[].
     **/
    public void Var029() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            int length = converter.toBytes(5432, data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(long, byte[]) - Pass empty array for the byte[].
     **/
    public void Var030() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            int length = converter.toBytes(-876, data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(long, byte[]) - Pass a 7-byte (too short) array for the byte[].
     **/
    public void Var031() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            int length = converter.toBytes(2389, data);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }




    /**                
     toBytes(long, byte[]) - Pass an 8-byte (right size) array for the byte[].
     **/
    public void Var032() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(677843356L, data);       
            assertCondition(length == 8      && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x28
                            && data[5] == (byte)0x67
                            && data[6] == (byte)0x11
                            && data[7] == (byte)0x9C);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }




    /**                
     toBytes(long, byte[]) - Pass 9-byte (too long) array for the byte[].
     **/
    public void Var033() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[9];
            data[8] = (byte)0x32;
            int length = converter.toBytes(-345678900445L, data);       
            assertCondition(length == 8      && data[0] == (byte)0xFF 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xAF
                            && data[4] == (byte)0x83
                            && data[5] == (byte)0xEE
                            && data[6] == (byte)0x93
                            && data[7] == (byte)0x23
                            && data[8] == (byte)0x32);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass null for the Object.
     **/
    public void Var034() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(null, data, 0);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass an invalid value for the Object.
     **/
    public void Var035() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(new Short((short)43), data, 0);     
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ClassCastException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass null for the byte[].
     **/
    public void Var036() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            int length = converter.toBytes(new Long(43243), data, 0);   
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass empty array for the byte[].
     **/
    public void Var037() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            int length = converter.toBytes(new Long(-45543999), data, 0);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass a 7-byte (too short) array for the byte[].
     **/
    public void Var038() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            int length = converter.toBytes(new Long(30), data, 0);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }




    /**                
     toBytes(Object, byte[], int) - Pass an 8-byte (right size) array for the byte[],
     with offset -1 (too small).
     **/
    public void Var039() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(new Long(2), data, -1);   
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass an 8-byte (right size) array for the byte[],
     with offset 1 (too large).
     **/
    public void Var040() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(new Long(-656444334112L), data, 1);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass an 8-byte (right size) array for the byte[],
     with offset 0.
     **/
    public void Var041() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(new Long(3454656444334L), data, 0);       
            assertCondition(length == 8      && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x03
                            && data[3] == (byte)0x24
                            && data[4] == (byte)0x59
                            && data[5] == (byte)0x91
                            && data[6] == (byte)0xF7
                            && data[7] == (byte)0xAE);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass 9-byte (too long) array for the byte[],
     with offset -1 (too small).
     **/
    public void Var042() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[9];
            int length = converter.toBytes(new Long(-4543), data, -1);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass 9-byte (too long) array for the byte[],
     with offset 2 (too large).
     **/
    public void Var043() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[9];
            int length = converter.toBytes(new Long(-4543), data, 2);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass 9-byte (too long) array for the byte[],
     with offset 0 (valid).
     **/
    public void Var044() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[9];
            data[8] = (byte)0xFE;
            int length = converter.toBytes(new Long(-9454332234L), data, 0);       
            assertCondition(length == 8      && data[0] == (byte)0xFF 
                            && data[1] == (byte)0xFF
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFD
                            && data[4] == (byte)0xCC
                            && data[5] == (byte)0x7A
                            && data[6] == (byte)0x56
                            && data[7] == (byte)0xB6
                            && data[8] == (byte)0xFE);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(Object, byte[], int) - Pass 9-byte (too long) array for the byte[],
     with offset 1 (valid).
     **/
    public void Var045() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[9];
            data[0] = (byte)0x96;
            int length = converter.toBytes(new Long(43434332234L), data, 1);       
            assertCondition(length == 8      && data[0] == (byte)0x96 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x00
                            && data[4] == (byte)0x0A
                            && data[5] == (byte)0x1C
                            && data[6] == (byte)0xE3
                            && data[7] == (byte)0x50
                            && data[8] == (byte)0x4A);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass null for the byte[].
     **/
    public void Var046() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            int length = converter.toBytes(1234567, data, 0);   
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass empty array for the byte[].
     **/
    public void Var047() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            int length = converter.toBytes(-9876543, data, 0);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass a 7-byte (too short) array for the byte[].
     **/
    public void Var048() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            int length = converter.toBytes(433456, data, 0);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }




    /**                
     toBytes(long, byte[], int) - Pass an 8-byte (right size) array for the byte[],
     with offset -1 (too small).
     **/
    public void Var049() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(3445354455645L, data, -1);   
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass an 8-byte (right size) array for the byte[],
     with offset 1 (too large).
     **/
    public void Var050() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(-3457258294375984L, data, 1);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass an 8-byte (right size) array for the byte[],
     with offset 0.
     **/
    public void Var051() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[8];
            int length = converter.toBytes(7577676534L, data, 0);       
            assertCondition(length == 8      && data[0] == (byte)0x00 
                            && data[1] == (byte)0x00
                            && data[2] == (byte)0x00
                            && data[3] == (byte)0x01
                            && data[4] == (byte)0xC3
                            && data[5] == (byte)0xAA
                            && data[6] == (byte)0x2A
                            && data[7] == (byte)0xF6);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass 10-byte (too long) array for the byte[],
     with offset -1 (too small).
     **/
    public void Var052() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[10];
            int length = converter.toBytes(-456893, data, -1);       
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass 10-byte (too long) array for the byte[],
     with offset 3 (too large).
     **/
    public void Var053() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[10];
            int length = converter.toBytes(8965, data, 3);
            failed("No exception thrown."+length);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass 10-byte (too long) array for the byte[],
     with offset 0 (valid).
     **/
    public void Var054() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[10];
            data[8] = (byte)0x77;
            data[9] = (byte)0x34;
            int length = converter.toBytes(435565676768768L, data, 0);       
            assertCondition(length == 8      && data[0] == (byte)0x00 
                            && data[1] == (byte)0x01
                            && data[2] == (byte)0x8C
                            && data[3] == (byte)0x25
                            && data[4] == (byte)0x09
                            && data[5] == (byte)0x70
                            && data[6] == (byte)0xAE
                            && data[7] == (byte)0x00
                            && data[8] == (byte)0x77
                            && data[9] == (byte)0x34);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toBytes(long, byte[], int) - Pass 10-byte (too long) array for the byte[],
     with offset 2 (valid).
     **/
    public void Var055() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[10];
            data[0] = (byte)0x44;
            data[1] = (byte)0x10;
            int length = converter.toBytes(-34343433220000L, data, 2);       
            assertCondition(length == 8      && data[0] == (byte)0x44 
                            && data[1] == (byte)0x10
                            && data[2] == (byte)0xFF
                            && data[3] == (byte)0xFF
                            && data[4] == (byte)0xE0
                            && data[5] == (byte)0xC3
                            && data[6] == (byte)0xCB
                            && data[7] == (byte)0xDC
                            && data[8] == (byte)0xD0
                            && data[9] == (byte)0x60);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[]) - Pass null.
     **/
    public void Var056() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            long value = converter.toLong(data);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toLong(byte[]) - Pass an empty array.
     **/
    public void Var057() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            long value = converter.toLong(data);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[]) - Pass an 7-byte array (too small).
     **/
    public void Var058() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            long value = converter.toLong(data);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[]) - Pass an 8-byte array with all 0x00's.
     **/
    public void Var059() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00 };
            long value = converter.toLong(data);       
            assertCondition(value == 0);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[]) - Pass an 8-byte array with many leading 0x00's.
     **/
    public void Var060() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x46, 
            (byte)0x69, 
            (byte)0xDA, 
            (byte)0xAA };
            long value = converter.toLong(data);       
            assertCondition(value == 1181342378L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[]) - Pass an 8-byte array with a variety of bytes.
     **/
    public void Var061() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x12, 
            (byte)0xFE, 
            (byte)0x00, 
            (byte)0x56, 
            (byte)0x99, 
            (byte)0xED, 
            (byte)0x20, 
            (byte)0xDF };
            long value = converter.toLong(data);  
            assertCondition(value == 1368531708716851423L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[]) - Pass an 8-byte array with many leading 0xFF's.
     **/
    public void Var062() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0x32, 
            (byte)0x65, 
            (byte)0x31, 
            (byte)0xEE };
            long value = converter.toLong(data);       
            assertCondition(value == -3449474578L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[]) - Pass an 8-byte array with all 0xFF's.
     **/
    public void Var063() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF };
            long value = converter.toLong(data);       
            assertCondition(value == -1);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[]) - Pass an 9-byte array (too large) with a variety of bytes.
     **/
    public void Var064() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF,
            (byte)0x12, 
            (byte)0xFE, 
            (byte)0x00, 
            (byte)0x56, 
            (byte)0x99, 
            (byte)0xED, 
            (byte)0x20, 
            (byte)0xDF };
            long value = converter.toLong(data);       
            assertCondition(value == -66711767050752736L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass null for byte[]
     **/
    public void Var065() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            long value = converter.toLong(data, 0);     
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an empty array.
     **/
    public void Var066() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            long value = converter.toLong(data, 0);   
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 7-byte array (too small).
     **/
    public void Var067() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            long value = converter.toLong(data, 0);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array (right size) with 
     offset -1 (too small).
     **/
    public void Var068() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x30, 
            (byte)0x43, 
            (byte)0xFF, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0xE5, 
            (byte)0x67, 
            (byte)0x88 };
            long value = converter.toLong(data, -1);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array (right size) with 
     offset 1 (too large).
     **/
    public void Var069() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x30, 
            (byte)0x43, 
            (byte)0xFF, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0xE5, 
            (byte)0x67, 
            (byte)0x88 };
            long value = converter.toLong(data, 1);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array with all 0x00's.
     **/
    public void Var070() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00 };
            long value = converter.toLong(data, 0);       
            assertCondition(value == 0);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array with many leading 0x00's.
     **/
    public void Var071() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0xFF, 
            (byte)0x56, 
            (byte)0xFF, 
            (byte)0xED, 
            (byte)0x49 };
            long value = converter.toLong(data, 0);       
            assertCondition(value == 1096676273481L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array with a variety of bytes.
     **/
    public void Var072() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFA, 
            (byte)0xAA, 
            (byte)0x83, 
            (byte)0xD4, 
            (byte)0x73, 
            (byte)0x12, 
            (byte)0x00, 
            (byte)0xDF };
            long value = converter.toLong(data, 0);       
            assertCondition(value == -384349869699890977L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array with many leading 0xFF's.
     **/
    public void Var073() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0x23, 
            (byte)0x45, 
            (byte)0x66, 
            (byte)0xEE, 
            (byte)0xDD };
            long value = converter.toLong(data, 0);       
            assertCondition(value == -948023398691L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 8-byte array with all 0xFF's.
     **/
    public void Var074() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF };
            long value = converter.toLong(data, 0);       
            assertCondition(value == -1);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 10-byte array (too large)
     with offset -1 (too small).
     **/
    public void Var075() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x6F };
            long value = converter.toLong(data, -1);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 10-byte array (too large)
     with offset 3 (too large).
     **/
    public void Var076() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x6F };
            long value = converter.toLong(data, 3);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 10-byte array (too large)
     with offset 0 (valid).
     **/
    public void Var077() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x53,
            (byte)0x22, 
            (byte)0x44, 
            (byte)0x34, 
            (byte)0x33, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0xD3, 
            (byte)0xAB };
            long value = converter.toLong(data, 0);       
            assertCondition(value == 5990425445340807168L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toLong(byte[], int) - Pass an 10-byte array (too large)
     with offset 2 (valid).
     **/
    public void Var078() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x6F };
            long value = converter.toLong(data, 2);       
            assertCondition(value == -3876379236574091921L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[]) - Pass null.
     **/
    public void Var079() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            Long value = (Long)converter.toObject(data);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toObject(byte[]) - Pass an empty array.
     **/
    public void Var080() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            Long value = (Long)converter.toObject(data);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[]) - Pass an 7-byte array (too small).
     **/
    public void Var081() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            Long value = (Long)converter.toObject(data);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[]) - Pass an 8-byte array with all 0x00's.
     **/
    public void Var082() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00 };
            Long value = (Long)converter.toObject(data);       
            assertCondition(value.longValue() == 0);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[]) - Pass an 8-byte array with many leading 0x00's.
     **/
    public void Var083() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x21, 
            (byte)0x11, 
            (byte)0x11 };
            Long value = (Long)converter.toObject(data);       
            assertCondition(value.longValue() == 2167057);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[]) - Pass an 8-byte array with a variety of bytes.
     **/
    public void Var084() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xE2, 
            (byte)0x00, 
            (byte)0x56, 
            (byte)0x20, 
            (byte)0x99, 
            (byte)0xFE, 
            (byte)0xED, 
            (byte)0xDF };
            Long value = (Long)converter.toObject(data);       
            assertCondition(value.longValue() == -2161633123115274785L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[]) - Pass an 8-byte array with many leading 0xFF's.
     **/
    public void Var085() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0x99, 
            (byte)0x99, 
            (byte)0x99 };
            Long value = (Long)converter.toObject(data);       
            assertCondition(value.longValue() == -6710887);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[]) - Pass an 8-byte array with all 0xFF's.
     **/
    public void Var086() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF };
            Long value = (Long)converter.toObject(data);       
            assertCondition(value.longValue() == -1);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[]) - Pass an 9-byte array (too large) with a variety of bytes.
     **/
    public void Var087() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x27,
            (byte)0x12, 
            (byte)0x11, 
            (byte)0x00, 
            (byte)0x56, 
            (byte)0x44, 
            (byte)0xED, 
            (byte)0xAA, 
            (byte)0xDF };
            Long value = (Long)converter.toObject(data);       
            assertCondition(value.longValue() == 2815331410205011370L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass null for byte[]
     **/
    public void Var088() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = null;
            Long value = (Long)converter.toObject(data, 0);     
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "NullPointerException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an empty array.
     **/
    public void Var089() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[0];
            Long value = (Long)converter.toObject(data, 0);   
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 7-byte array (too small).
     **/
    public void Var090() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[7];
            Long value = (Long)converter.toObject(data, 0);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array (right size) with 
     offset -1 (too small).
     **/
    public void Var091() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xE3, 
            (byte)0x43, 
            (byte)0xFF, 
            (byte)0xDD, 
            (byte)0xCA, 
            (byte)0xE5, 
            (byte)0x67, 
            (byte)0x00 };
            Long value = (Long)converter.toObject(data, -1);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array (right size) with 
     offset 1 (too large).
     **/
    public void Var092() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xE3, 
            (byte)0x43, 
            (byte)0xFF, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0xE5, 
            (byte)0x67, 
            (byte)0x00 };
            Long value = (Long)converter.toObject(data, 1);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array with all 0x00's.
     **/
    public void Var093() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00, 
            (byte)0x00 };
            Long value = (Long)converter.toObject(data, 0);       
            assertCondition(value.longValue() == 0);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array with many leading 0x00's.
     **/
    public void Var094() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0x00, 
            (byte)0x00, 
            (byte)0x45, 
            (byte)0xFF, 
            (byte)0x56, 
            (byte)0x58, 
            (byte)0x8F, 
            (byte)0x49 };
            Long value = (Long)converter.toObject(data, 0);       
            assertCondition(value.longValue() == 76962967621449L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array with a variety of bytes.
     **/
    public void Var095() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xAB, 
            (byte)0xCD, 
            (byte)0x83, 
            (byte)0xD4, 
            (byte)0x73, 
            (byte)0x21, 
            (byte)0x04, 
            (byte)0x00 };
            Long value = (Long)converter.toObject(data, 0);       
            assertCondition(value.longValue() == -6067048174510341120L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array with many leading 0xFF's.
     **/
    public void Var096() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xEE, 
            (byte)0x44, 
            (byte)0x23, 
            (byte)0x45, 
            (byte)0x66, 
            (byte)0x10 };
            Long value = (Long)converter.toObject(data, 0);       
            assertCondition(value.longValue() == -19498559773168L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 8-byte array with all 0xFF's.
     **/
    public void Var097() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF, 
            (byte)0xFF };
            Long value = (Long)converter.toObject(data, 0);       
            assertCondition(value.longValue() == -1);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass a 20-byte array (too large)
     with offset -1 (too small).
     **/
    public void Var098() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x6F };
            Long value = (Long)converter.toObject(data, -1);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 10-byte array (too large)
     with offset 13 (too large).
     **/
    public void Var099() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x6F };
            Long value = (Long)converter.toObject(data, 13);       
            failed("No exception thrown."+value);
        }
        catch (Exception e) {
            assertExceptionIs(e, "ArrayIndexOutOfBoundsException");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 20-byte array (too large)
     with offset 0 (valid).
     **/
    public void Var100() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x6F };
            Long value = (Long)converter.toObject(data, 0);       
            assertCondition(value.longValue() == -77183293030293683L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 20-byte array (too large)
     with offset 6 (valid).
     **/
    public void Var101() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x6F };
            Long value = (Long)converter.toObject(data, 6);       
            assertCondition(value.longValue() == -6967871345242393515L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



    /**                
     toObject(byte[], int) - Pass an 20-byte array (too large)
     with offset 12 (valid).
     **/
    public void Var102() 
    {
        try {
            AS400Bin8 converter = new AS400Bin8();
            byte[] data = new byte[] {  (byte)0xFE,
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0xED, 
            (byte)0xCA, 
            (byte)0x34, 
            (byte)0x34, 
            (byte)0x55, 
            (byte)0x9F, 
            (byte)0x4D, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x25, 
            (byte)0x6F };
            Long value = (Long)converter.toObject(data, 12);       
            assertCondition(value.longValue() == 3771095416677148015L);
        }
        catch (Exception e) {
            failed(e, "Incorrect exception");
        }
    }



}
