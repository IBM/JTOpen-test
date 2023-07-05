///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import test.ConvTableCtorTestcase;
import test.ConvTableSingleLengthTestcase;
import test.ConvTableSingleBytesTestcase;
import test.ConvTableSingleRoundTripTestcase;
import test.ConvTableDoubleLengthTestcase;
import test.ConvTableDoubleBytesTestcase;
import test.ConvTableDoubleRoundTripTestcase;
import test.ConvTableMixedLengthTestcase;
import test.ConvTableMixedRoundTripTestcase;
import test.ConvTableUnicodeRoundTripTestcase;
import test.ConvTableBidiLengthTestcase;
import test.ConvTableBidiBytesTestcase;
import test.ConvTableBidiRoundTripTestcase;
import test.ConvTableAsciiLengthTestcase;
import test.ConvTableAsciiBytesTestcase;
import test.ConvTableAsciiRoundTripTestcase;
import test.ConvTableJavaTestcase;
import test.ConvTableEncodingTestcase;
import test.ConvTableThreadTestcase;
import test.ConvExecutionTestcase;
import test.ConvConverterTestcase;
import test.ConversionMapTestcase;

/**
 Test driver for the Conv (text converter) component.
 For security tests to be run in unattended mode, a AS400 userid/password with *SECADM authority must be passed on the -misc parameter (ie -misc uid,pwd).
 If not specified, these tests will not be attempted in unattended mode.
 If not specified and running attended, a sign-on will be displayed.
 See TestDriver for remaining calling syntax.
 @see TestDriver
 **/
public class ConvTest extends TestDriver
{
    /**
     The list of supported ASCII CCSIDs in the Toolbox.
     **/
    public static final int[] asciiCcsids_ =
    { 367, 437, 720, 737, 775, 813, 819, 850,
    851, 852, 855, 857, 860, 861, 863, 865,
    866, 869, 874, 878, 912, 914, 915,
    920, 921, 922, 923, 1125, 1129, 1131, 1250,
    1251, 1252, 1253, 1254, 1257, 1258,
    4948, 4951, 9066 };

    /**
     The list of supported Bidi CCSIDs in the Toolbox.
     **/
    public static final int[] bidiCcsids_ =
    { 420, 424, 425, 12708, 62211, 62235, 62245,
    862, 916, 1255, 5351,
    864, 1046, 1089, 1256, 8612, 62224 };

    /**
     The list of supported single-byte EBCDIC CCSIDs in the Toolbox.
     **/
    public static final int[] singleCcsids_ =
    { 37, 256, 273, 277, 278, 280, 284, 285, 290,
    297, 423, 500, 833, 836, 838, 870, 871,
    875, 880, 1025, 1026, 1027, 1112, 1122, 1123,
    1130, 1132, 1137, 1140, 1141, 1142, 1143, 1144, 1145,
    1146, 1147, 1148, 1149, 1153, 1154, 1155, 1156, 1157,
    1158, 1160, 1164, 4971, 5123, 9030, 28709 };

    /**
     The list of supported double-byte EBCDIC CCSIDs in the Toolbox.
     **/
    public static final int[] doubleCcsids_ =
    { 300, 834, 835, 837, 4396, 4930, 4931, 4933,
    8492, 9026, 9029, 12588, 13122, 16684, 17218, 61952 }; //61952 is actually AS/400 old Unicode.

    /**
     The list of supported mixed-byte EBCDIC CCSIDs in the Toolbox.
     **/
    public static final int[] mixedCcsids_ =
    { 930, 933, 935, 937, 939, 1364, 1388, 1399,
    5026, 5035 };

    /**
     The list of supported Unicode CCSIDs in the Toolbox.
     **/
    public static final int[] unicodeCcsids_ =
    { 1200, 1201, 1202, 13488, 17584, 21680 }; //61952 is included as a doubleCcsid even though it is actually AS/400 old Unicode.

    /**
     The list of all supported CCSIDs in the Toolbox.
     **/
    public static final int[] allCcsids_ =
    { 367, 437, 720, 737, 775, 813, 819, 850,
    851, 852, 855, 857, 860, 861, 863, 865,
    866, 869, 874, 878, 912, 914, 915, 916,
    920, 921, 922, 923, 1125, 1129, 1131, 1250,
    1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258,
    4948, 4951, 9066,
    420, 424, 425, 12708, 62211, 62235, 62245,
    862, 916, 1255, 5351,
    864, 1046, 1089, 1256, 8612, 62224,
    37, 256, 273, 277, 278, 280, 284, 285, 290,
    297, 423, 500, 833, 836, 838, 870, 871,
    875, 880, 1025, 1026, 1027, 1112, 1122, 1123,
    1130, 1132, 1137, 1140, 1141, 1142, 1143, 1144, 1145,
    1146, 1147, 1148, 1149, 1153, 1154, 1155, 1156,
    1157, 1158, 1160, 1164, 4971, 5123, 9030, 28709,
    300, 834, 835, 837, 4396, 4930, 4931, 4933,
    8492, 9026, 9029, 12588, 13122, 16684, 17218,
    930, 933, 935, 937, 939, 1364, 1388, 1399,
    5026, 5035, 1200, 1201, 1202, 9030, 13488, 17584, 21680, 61952 };

    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new ConvTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     This ctor used for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public ConvTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param args the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
     **/
    public ConvTest(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Creates Testcase objects for all the testcases in this component.
     **/
    public void createTestcases()
    {
        Testcase[] testcases =
        {
            new ConvTableCtorTestcase(),
            new ConvTableSingleLengthTestcase(),
            new ConvTableSingleBytesTestcase(),
            new ConvTableSingleRoundTripTestcase(),
            new ConvTableDoubleLengthTestcase(),
            new ConvTableDoubleBytesTestcase(),
            new ConvTableDoubleRoundTripTestcase(),
            new ConvTableMixedLengthTestcase(),
            new ConvTableMixedRoundTripTestcase(),
            new ConvTableUnicodeRoundTripTestcase(),
            new ConvTableBidiLengthTestcase(),
            new ConvTableBidiBytesTestcase(),
            new ConvTableBidiRoundTripTestcase(),
            new ConvTableAsciiLengthTestcase(),
            new ConvTableAsciiBytesTestcase(),
            new ConvTableAsciiRoundTripTestcase(),
            new ConvTableJavaTestcase(),
            new ConvTableEncodingTestcase(),
            new ConvTableThreadTestcase(),
            new ConvExecutionTestcase(),
            new ConvConverterTestcase(),
            new ConversionMapTestcase(),
            new ConvPSA89229(),
            new ConvTableReaderTestcase(),
            new ConvTableWriterTestcase(),
            new ConvPathnameTestcase(),
            new ConvP9953884(),
            new ConvPSE04743()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_ );
            addTestcase(testcases[i]);
        }
    }


    /**
     Performs cleanup needed after running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
        systemObject_.disconnectAllServices();
    }
}
