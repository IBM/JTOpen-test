///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmRLETestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Pgm;

import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;

import test.Testcase;

/**
 Testcase for testing Program Call RLE Compression variations.
 **/
public class PgmRLETestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PgmRLETestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PgmTest.main(newArgs); 
   }
    String prog3_ = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";
    String prog3p0_;
    short prog3p2_;
    ProgramParameter[] parmlist_;
    byte[] data0;
    byte[] data1;
    byte[] retdata0;
    ProgramParameter parm0, parm1, parm2;
    AS400Bin2 int16 = new AS400Bin2();

    ProgramParameter[] buildParms()
    {
        parmlist_ = new ProgramParameter[3];

        data0 = new byte[1500];
        for (int i=0; i<21; i++)
        {
            data0[i] = (byte) 7;
        }
        for (int i=21; i<37; i++)
        {
            data0[i] = 6;
        }
        parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

        data1 = new byte[2];
        data1[0] = (byte) 4;
        data1[1] = (byte) 2;
        parmlist_[1] = parm1 = new ProgramParameter(data1);
        parmlist_[2] = parm2 = new ProgramParameter(2);

        return parmlist_;
    }

    // Test running successfull program with constructor and run methods
    void verifyProg3( ProgramCall pgm )
    {

        // Verify no messages returned
        AS400Message[] msglist = pgm.getMessageList();
        if (msglist.length!=0 )
        {
            failed("message received " + msglist[0]); return;
        }

        // Verify parameter list
        ProgramParameter[] parmlist = pgm.getParameterList();
        if (parmlist.length != 3)
        {
            failed("parameter list size changed" ); return;
        }
        if (parmlist[0] != parm0)
        {
            failed("parameter 0 changed" ); return;
        }
        if (parmlist[1] != parm1)
        {
            failed("parameter 1 changed" ); return;
        }
        if (parmlist[2] != parm2)
        {
            failed("parameter 2 changed" ); return;
        }

        // Verify each parm
        // Verify parm 0 still in/out
        if (parmlist[0].getInputData() != data0)
        {
            failed("parameter 0 data changed" ); return;
        }
        if (parmlist[0].getOutputDataLength() != parm0.getOutputDataLength())
        {
            failed("parameter 0 output data length changed to "
                   + parmlist[0].getOutputDataLength()); return;
        }
        // Verify parm 0 result string
        AS400Text xlater = new AS400Text( prog3p0_.length(), 37, systemObject_);
        String retp0 = (String) xlater.toObject (parmlist[0].getOutputData(), 0 );
        if (!retp0.equals(prog3p0_))
        {
            failed("parameter 0 wrong value " + retp0 ); return;
        }
        // Verify remaining bytes for parm 0, if any
        retdata0 = new byte[parmlist[0].getOutputData().length];
        retdata0 = parmlist[0].getOutputData();
        if (parmlist[0].getInputData().length > parmlist[0].getOutputData().length) {
            for (int i=retp0.length(); i < retdata0.length; i++) {
                if (retdata0[i] != data0[i])
                {
                    failed("parameter 0 wrong byte " + retdata0[i] + " at index " + i );
                    return;
                }
            }
            for (int i=retdata0.length; i < data0.length; i++) {
                if (data0[i] != (byte) 0)
                {
                    failed("parameter 0 not zero filled from byte " + i + " to " + data0.length);
                    return;
                }
            }
        }
        else {
            for (int i=retp0.length(); i < data0.length; i++) {
                if (retdata0[i] != data0[i])
                {
                    failed("parameter 0 wrong byte " + retdata0[i] + " at index " + i );
                    return;
                }
            }
            for (int i=data0.length; i < retdata0.length; i++) {
                if (retdata0[i] != (byte) 0)
                {
                    failed("parameter 0 not zero filled from byte " + i + " to " + retdata0.length);
                    return;
                }
            }
        }

        // Verify parm 1 still input
        if (parmlist[1].getInputData() != data1)
        {
            failed("parameter 1 data changed" ); return;
        }
        if (parmlist[1].getOutputDataLength() != 0)
        {
            failed("parameter 1 output data length set to "
                   + parmlist[1].getOutputDataLength()); return;
        }
        if (parmlist[1].getOutputData() != null
            && parmlist[1].getOutputData().length != 0)
        {
            failed("parameter 1 output data set" ); return;
        }

        // Verify parm 2 still output
        if (parmlist[2].getInputData() != null
            && parmlist[2].getInputData().length != 0)
        {
            failed("parameter 2 data changed" ); return;
        }
        if (parmlist[2].getOutputDataLength() != parm2.getOutputDataLength())
        {
            failed("parameter 2 output data length changed to "
                   + parmlist[2].getOutputDataLength()); return;
        }

        // Verify parm 2 = parm1+1
        if (prog3p2_ != int16.toShort( parmlist[2].getOutputData(), 0))
        {
            failed("parameter 2 wrong value " + int16.toShort( parmlist[2].getOutputData(), 0) ); return;
        }

        // Verify system not changed
        if (pgm.getSystem()!=systemObject_)
        {
            failed("system changed " + pgm.getSystem().getSystemName() ); return;
        }

        // Verify program not changed
        if (pgm.getProgram()!=prog3_)
        {
            failed("program changed " + pgm.getProgram() ); return;
        }
        succeeded();
    }

    /**
     Verify input parms are compressed correctly and
     output parms are decompressed as expected.
     **/
    public void Var001()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1500
            data0 = new byte[1500];
            int i;
            for (i=0; i<21; i++)
            {
                data0[i] = (byte) 7;
            }
            for (i=21; i<47; i++)
            {
                data0[i] = 6;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify an escape character is handled correctly during
     compression when escape record fits into destination array.
     **/
    public void Var002()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1500
            data0 = new byte[1500];
            int i;
            for (i=0; i<21; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            for (i=21; i<35; i++)
            {
                data0[i] = 6;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 3;
            data1[1] = (byte) 0;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 769;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify an escape character is handled correctly during
     compression when escape record causes overflow in
     the destination array.
     **/
    public void Var003()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1025
            data0 = new byte[1025];
            int i;
            for (i=0; i<1025; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify a single byte before EOD is handled correctly during
     compression when the last byte fits into destination array.
     **/
    public void Var004()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1037
            data0 = new byte[1037];
            int i;
            for (i=0; i<1026; i++)
            {
                data0[i++] = (byte) 1;
                data0[i++] = (byte) 2;
                data0[i] = (byte) 3;
            }
            for (i=1026; i<1036; i++)
            {
                data0[i] = (byte) 1;
            }
            data0[1036] = (byte) 2;
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify a single byte before EOD is handled correctly
     during compression when last byte causes overflow in
     the destination array.
     **/
    public void Var005()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2001
            data0 = new byte[2001];
            int i;
            for (i=0; i<2001; i++)
            {
                data0[i] = (byte) 2;
                i += 2;
            }
            data0[401] = (byte) 0x1b;
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify a single byte followed by an escape byte
     is handled correctly during compression when the 
     three bytes fits into destination array.
     **/
    public void Var006()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1025
            data0 = new byte[1025];
            int i;
            for (i=0; i<1025; i++)
            {
                data0[i] = (byte) 4;
            }
            data0[400] = (byte) 8; // 400th byte is 0x08
            data0[401] = (byte) 0x1b; // 401st byte is 0x1b
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify a single byte followed by an escape byte
     is handled correctly during compression when the
     three bytes cause an overflow in the destination array.
     **/
    public void Var007()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2048
            data0 = new byte[2048];
            int i;
            for (i=0; i<2048; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            data0[2046] = 2;
            data0[2047] = (byte) 0x1b;
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify an RLE repeater record is correctly built and
     written to the destination array when 10 bytes are
     found to be repeating.
     **/
    public void Var008()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1500
            data0 = new byte[1500];
            int i;
            for (i=0; i<50; i++)
            {
                data0[i] = (byte) 7;
            }
            for (i=50; i<60; i++)
            {
                data0[i] = 6;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 1500);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify the overflow is handled correctly when 10 bytes
     are found to be repeating, but the RLE repeater record
     doesn't fit in the destination array.
     **/
    public void Var009()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 3000
            data0 = new byte[3000];
            int i;
            for (i=0; i<2983; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            for (i=2984; i<2990; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            for (i=2990; i<3000; i++)
            {
                data0[i] = 5;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify an RLE repeater record is not built when 9
     bytes are found to be repeating.  Verify the
     correct bytes are just copied into the destination
     array.
     **/
    public void Var010()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2000
            data0 = new byte[2000];
            int i;
            for (i=0; i<50; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            for (i=50; i<59; i++)
            {
                data0[i] = (byte) 9;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify the overflow is handled correctly when 9 bytes
     are found to be repeating, but the bytes do not fit
     in the destination array.
     **/
    public void Var011()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 6000
            data0 = new byte[6000];
            int i;
            for (i=0; i<5985; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            for (i=5985; i<5991; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            for (i=5991; i<6000; i++)
            {
                data0[i] = 5;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify the individual bytes are copied correctly
     into the destination array when no repeating
     bytes are found in the source.
     **/
    public void Var012()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1025
            data0 = new byte[1025];
            int i;
            for (i=0; i<1025; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify the overflow is handled correctly when no
     repeating bytes are found in the rest of the source
     and the number of bytes being copied to the destination
     array causes an overflow situation.
     **/
    public void Var013()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2048
            data0 = new byte[2048];
            int i;
            for (i=0; i<2048; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            data0[0] = (byte) 0x1B;
            data0[1] = (byte) 0x1B;
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify null is returned when the compressed data
     becomes larger than the uncompressed data.
     **/
    public void Var014()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2040
            data0 = new byte[2040];
            int i;
            for (i=0; i<2040; i++)
            {
                data0[i] = (byte) i;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify data is compressed correctly and return
     parms are uncompressed correctly when output
     parm is larger than input parm.
     **/
    public void Var015()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 4000
            data0 = new byte[2000];
            int i;
            for (i=0; i<1500; i++)
            {
                data0[i] = (byte) i;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 4000);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify in/out parm is uncompressed when input is less
     than 1K and output data is zero-suppressed when output
     is larger than 1K (won't be compressed because input
     wasn't compressed).
     **/
    public void Var016()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1000
            data0 = new byte[1000];
            int i;
            for (i=0; i<1000; i++)
            {
                data0[i] = (byte) i;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 4000);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify two escape characters are handled correctly during
     decompression when escape byte fits into destination array.
     **/
    public void Var017()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2000
            data0 = new byte[2000];
            int i;
            for (i=0; i<20; i++)
            {
                data0[i] = (byte) 10;
            }
            for (i=20; i<40; i++)
            {
                data0[i] = (byte) 20;
            }
            for (i=40; i<60; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 3;
            data1[1] = (byte) 0;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 769;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify two escape characters are handled correctly during
     decompression when escape byte causes overflow in
     the destination array.
     **/
    public void Var018()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2050
            data0 = new byte[2050];
            int i;
            for (i=0; i<2001; i++)
            {
                data0[i] = (byte) 2;
            }
            for (i=2001; i<2050; i++)
            {
                data0[i] = (byte) 0x1b;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify a repeater record is handled correctly during
     decompression when the bytes fit into destination array.
     **/
    public void Var019()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 1203
            data0 = new byte[1203];
            int i;
            for (i=0; i<1202; i++)
            {
                data0[i] = (byte) 32;
            }
            data0[1202] = (byte) 4;
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify a repeater record is handled correctly during
     decompression when the bytes cause an overflow in
     the destination array.
     **/
    public void Var020()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2400
            data0 = new byte[2400];
            int i;
            for (i=0; i<2400; i++)
            {
                data0[i] = (byte) 5;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify single bytes are handled correctly during
     decompression when the bytes fit in the destination array.
     **/
    public void Var021()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2000
            data0 = new byte[2000];
            int i;
            for (i=0; i<20; i++)
            {
                data0[i] = (byte) 5;
            }
            for (i=20; i<2000; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify single bytes are handled correctly during
     decompression when the bytes cause an overflow in
     the destination array.
     **/
    public void Var022()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2070
            data0 = new byte[2070];
            int i;
            for (i=0; i<300; i++)
            {
                data0[i] = (byte) 5;
            }
            for (i=300; i<2070; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify single bytes are handled correctly during
     decompression when the bytes cause multiple overflows in
     the destination array.
     **/
    public void Var023()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 2400
            data0 = new byte[2400];
            int i;
            for (i=0; i<2046; i++)
            {
                data0[i] = (byte) 1;
            }
            for (i=2046; i<2076; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            for (i=2076; i<2100; i++)
            {
                data0[i] = (byte) 2;
            }
            for (i=2100; i<2130; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            for (i=2130; i<2400; i++)
            {
                data0[i] = (byte) 6;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Verify single bytes are handled correctly during
     decompression when the bytes cause multiple overflows in
     the destination array.
     **/
    public void Var024()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);

            // Set up 3 parameters
            parmlist_ = new ProgramParameter[3];

            // Parm 0 is in/out with max size of 40000
            data0 = new byte[24000];
            int i;
            for (i=0; i<20460; i++)
            {
                data0[i] = (byte) 1;
            }
            for (i=20460; i<20760; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            for (i=20760; i<21000; i++)
            {
                data0[i] = (byte) 2;
            }
            for (i=21000; i<21300; i++)
            {
                data0[i] = (byte) 3;
                i += 2;
            }
            for (i=21300; i<24000; i++)
            {
                data0[i] = (byte) 6;
            }
            parmlist_[0] = parm0 = new ProgramParameter(data0, 40000);

            // Parm 1 is input with max size of 2
            data1 = new byte[2];
            data1[0] = (byte) 4;
            data1[1] = (byte) 2;
            parmlist_[1] = parm1 = new ProgramParameter(data1);

            // Parm 2 is output with max size of 2
            parmlist_[2] = parm2 = new ProgramParameter(2);

            // Run the program
            pgm.run(prog3_, parmlist_);

            // Verify the results
            prog3p0_ = "Testing 3 Parameters";
            prog3p2_ = 1027;
            verifyProg3(pgm);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
}
