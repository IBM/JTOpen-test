///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMToByteArrayTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.*;

/**
 The RMToByteArrayTestcase class tests the following methods of the RecordFormatDocument class:
 &lt;li&gt;constructors,
 &lt;li&gt;toString().
 **/
public class RMToByteArrayTestcase extends Testcase
{

    /**
     Constructor.
     **/
    public RMToByteArrayTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMToByteArrayTestcase", namesAndVars, runMode, fileOutputStream);
    }

    /**
     toByteArray(null).  Verify that a NullPointerException is thrown.
     **/
    public void Var001()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] result = rfmlDoc.toByteArray(null);
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     toByteArray(non-existent format).  Verify that exception is thrown.
     **/
    public void Var002()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] result = rfmlDoc.toByteArray("doesNotExist");
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
          String expectedMsg = "<recordformat> element named 'doesNotExist' not found in document";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     toByteArray().  Specify a format that has a single <data type="char">.  The field specifies a ccsid and an init value.  Verify that correct bytes are generated.
     **/
    public void Var003()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1");

            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {0xC1});  // ebcdic 'A'

///            System.out.println("Expected:");
///            printByteArray(expected);
///            System.out.println("Received:");
///            printByteArray(received);

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Specify a format that has a single <data type="int">.  Verify that correct bytes are generated.
     **/
    public void Var004()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.int1");

            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {0,9});

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Specify a format that has a single <data type="zoned">.  Verify that correct bytes are generated.
     **/
    public void Var005()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1");

            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {0xF8});  // ebcdic '8'

///            System.out.println("Expected:");
///            printByteArray(expected);
///            System.out.println("Received:");
///            printByteArray(received);

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// The following two scenarios are somewhat redundant with the "zoned" scenario, and the expected array is more challenging to compose.
///    /**
///     toByteArray().  Specify a format that has a single <data type="packed">.  Verify that correct bytes are generated.
///     **/
///    public void Var00x()
///    {
///    }
///
///    /**
///     toByteArray().  Specify a format that has a single <data type="float">.  Verify that correct bytes are generated.
///     **/
///    public void Var00x()
///    {
///    }

    /**
     toByteArray().  Specify a format that has a single <data type="byte">.  Verify that correct bytes are generated.
     **/
    public void Var006()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.byte1");

            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {7});

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Specify a format that has a single <data type="struct">, and the struct has a single <data type="char">.  Verify that correct bytes are generated.
     **/
    public void Var007()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structChar1");

            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {0xC1});  // ebcdic 'A'

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Specify a format that has a single <data type="struct">, and the struct has a single <data type="char"> with count=2.  Verify that correct bytes are generated.
     **/
    public void Var008()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structChar1_2");

            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {0xC1, 0xC1}); // ebcdic 'A's

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Verify that correct bytes are generated for a more complex RFML file.
     **/
    public void Var009()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");

            byte[] received = rfmlDoc.toByteArray("cusrec");

            int[] expectedVals = new int[] {
              0xF0, 0xF0, 0xF0, 0xF0, 0xF0, 0xF0,              // cusnum
              0xC1, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40,  // lstnam
              0xC2, 0x40, 0x40,                                // init
              0xC3, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40,  // lstnam
              0x40, 0x40, 0x40, 0x40, 0x40,
              0xC4, 0x40, 0x40, 0x40, 0x40, 0x40,              // city
              0xC5, 0x40,                                      // state
              0xF0, 0xF0, 0xF0, 0xF0, 0xF1,                    // zipcod
              0xF0, 0xF0, 0xF0, 0xF2,                          // cdtlmt
              0xF3,                                            // chgcod
              0xF0, 0xF0, 0xF0, 0xF4, 0xF0, 0xF0,              // baldue
              0xF0, 0xF0, 0xF0, 0xF5, 0xF0, 0xF0,              // cdtdue
            };
            byte[] expected = createByteArray(expectedVals);
/*
            System.out.println("Expected:");
            printByteArray(expected);
            System.out.println("Received:");
            printByteArray(received);
*/
            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Verify that correct bytes are generated for a more complex RFML file.
     **/
    public void Var010()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");

            byte[] received = rfmlDoc.toByteArray("cusrec1");

            int[] expectedVals = new int[] {
              0xF0, 0xF0, 0xF0, 0xF0, 0xF0, 0xF0,              // cusnum
              0xC1, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40,  // lstnam
              0xC2, 0x40, 0x40,                                // init
              0xC3, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40,  // lstnam
              0x40, 0x40, 0x40, 0x40, 0x40,
              0xC4, 0x40, 0x40, 0x40, 0x40, 0x40,              // city
              0xC5, 0x40,                                      // state
              0xF0, 0xF0, 0xF0, 0xF0, 0xF1,                    // zipcod
              0xF0, 0xF0, 0xF0, 0xF2,                          // cdtlmt
              0xF3,                                            // chgcod
              0xF0, 0xF0, 0xF0, 0xF7, 0xF0, 0xF0,              // baldue
              0xF0, 0xF0, 0xF0, 0xF7, 0xF0, 0xF0,              // cdtdue
            };
            byte[] expected = createByteArray(expectedVals);
/*
            System.out.println("Expected:");
            printByteArray(expected);
            System.out.println("Received:");
            printByteArray(received);
*/
            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     toByteArray().  Specify a format that has a single <data type="char">.  The field specifies a ccsid and an init value.  Do a setValue() to change the field.  Verify that correct bytes are generated.
     **/
    public void Var011()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1");

            rfmlDoc.setValue("format1.field1", "B");
            String got = (String)(rfmlDoc.getValue("format1.field1"));
            if (!(got.equals("B"))) {
              failed("getValue() returned wrong value: " + got);
              return;
            }
            byte[] received = rfmlDoc.toByteArray("format1");
            byte[] expected = createByteArray(new int[] {0xC2});  // ebcdic 'B'

///            System.out.println("Expected:");
///            printByteArray(expected);
///            System.out.println("Received:");
///            printByteArray(received);

            assertCondition (areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

}
