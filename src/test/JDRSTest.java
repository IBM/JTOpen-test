///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Random;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDParallelCounter;
import test.JD.JDSetupCollection;
import test.JD.RS.JDRSAbsolute;
import test.JD.RS.JDRSAfterLast;
import test.JD.RS.JDRSBeforeFirst;
import test.JD.RS.JDRSCachedRowSet;
import test.JD.RS.JDRSCursorScroll;
import test.JD.RS.JDRSCursorScrollInsensitiveFromCall;
import test.JD.RS.JDRSCursorScrollSensitiveFromCall;
import test.JD.RS.JDRSCursorSensitivity;
import test.JD.RS.JDRSDataCompression;
import test.JD.RS.JDRSDeleteRow;
import test.JD.RS.JDRSFetchSize;
import test.JD.RS.JDRSFindColumn;
import test.JD.RS.JDRSFirst;
import test.JD.RS.JDRSGetArray;
import test.JD.RS.JDRSGetAsciiStream;
import test.JD.RS.JDRSGetBigDecimal;
import test.JD.RS.JDRSGetBinaryStream;
import test.JD.RS.JDRSGetBlob;
import test.JD.RS.JDRSGetBoolean;
import test.JD.RS.JDRSGetByte;
import test.JD.RS.JDRSGetBytes;
import test.JD.RS.JDRSGetBytesBinary;
import test.JD.RS.JDRSGetCharacterStream;
import test.JD.RS.JDRSGetClob;
import test.JD.RS.JDRSGetDate;
import test.JD.RS.JDRSGetDouble;
import test.JD.RS.JDRSGetFloat;
import test.JD.RS.JDRSGetInt;
import test.JD.RS.JDRSGetLong;
import test.JD.RS.JDRSGetNCharacterStream;
import test.JD.RS.JDRSGetNClob;
import test.JD.RS.JDRSGetNString;
import test.JD.RS.JDRSGetObject;
import test.JD.RS.JDRSGetObject41;
import test.JD.RS.JDRSGetRef;
import test.JD.RS.JDRSGetRow;
import test.JD.RS.JDRSGetRowId;
import test.JD.RS.JDRSGetSQLXML;
import test.JD.RS.JDRSGetShort;
import test.JD.RS.JDRSGetString;
import test.JD.RS.JDRSGetStringBIDI;
import test.JD.RS.JDRSGetStringMixed;
import test.JD.RS.JDRSGetStringUnsupported;
import test.JD.RS.JDRSGetTime;
import test.JD.RS.JDRSGetTimestamp;
import test.JD.RS.JDRSGetURL;
import test.JD.RS.JDRSGetUnicodeStream;
import test.JD.RS.JDRSInsertRow;
import test.JD.RS.JDRSLast;
import test.JD.RS.JDRSMisc;
import test.JD.RS.JDRSMoveToCurrentRow;
import test.JD.RS.JDRSMoveToInsertRow;
import test.JD.RS.JDRSNext;
import test.JD.RS.JDRSPrevious;
import test.JD.RS.JDRSRefreshRow;
import test.JD.RS.JDRSRelative;
import test.JD.RS.JDRSUpdateAsciiStream;
import test.JD.RS.JDRSUpdateBigDecimal;
import test.JD.RS.JDRSUpdateBinaryStream;
import test.JD.RS.JDRSUpdateBoolean;
import test.JD.RS.JDRSUpdateByte;
import test.JD.RS.JDRSUpdateBytes;
import test.JD.RS.JDRSUpdateCharacterStream;
import test.JD.RS.JDRSUpdateClob;
import test.JD.RS.JDRSUpdateDB2Default;
import test.JD.RS.JDRSUpdateDBDefault;
import test.JD.RS.JDRSUpdateDate;
import test.JD.RS.JDRSUpdateDouble;
import test.JD.RS.JDRSUpdateFloat;
import test.JD.RS.JDRSUpdateInt;
import test.JD.RS.JDRSUpdateLong;
import test.JD.RS.JDRSUpdateNCharacterStream;
import test.JD.RS.JDRSUpdateNClob;
import test.JD.RS.JDRSUpdateNString;
import test.JD.RS.JDRSUpdateNull;
import test.JD.RS.JDRSUpdateObject;
import test.JD.RS.JDRSUpdateObjectSQLType;
import test.JD.RS.JDRSUpdateRow;
import test.JD.RS.JDRSUpdateRowId;
import test.JD.RS.JDRSUpdateSQLXML;
import test.JD.RS.JDRSUpdateShort;
import test.JD.RS.JDRSUpdateString;
import test.JD.RS.JDRSUpdateTime;
import test.JD.RS.JDRSUpdateTimestamp;
import test.JD.RS.JDRSWarnings;
import test.JD.RS.JDRSWasNull;
import test.JD.RS.JDRSWrapper;
import test.JD.RS.JDRowSetRSTestcase;

/**
 * Test driver for the JDBC ResultSet class.
 **/
public class JDRSTest extends JDTestDriver {

  /**
  * 
  */


  public static String BOOLEAN_TRUE_STRING  = "1";
  public static String BOOLEAN_FALSE_STRING = "0";

 private static final boolean IGNORE_FAILURES = true;

  // Constants.
  public static String COLLECTION = "JDTESTRS";

  public static String RSTEST_GET = COLLECTION + ".RSTESTGET";
  // Get with XML values
  public static String RSTEST_GETX = COLLECTION + ".RSTESTGETX";
  public static String RSTEST_GETDL = COLLECTION + ".RSTESTGETDL";
  public static String RSTEST_BINARY = COLLECTION + ".RSTESTBIN"; // @F1
  public static String RSTEST_POS = COLLECTION + ".RSTESTPOS";
  public static String RSTEST_UPDATE = COLLECTION + ".RSTESTUPD";
  public static String RSTEST_SCROLL = COLLECTION + ".RSTSTSCRL";
  public static String RSTEST_GRAPHIC = COLLECTION + ".RSTESTGRAPHIC";
  public static String RSTEST_SENSITIVE = COLLECTION + ".RSTESTSENS"; // @G1A

  public static String RSTEST_DFP16 = COLLECTION + ".RSDFP16";
  public static String RSTEST_DFP16NAN = COLLECTION + ".RSDFP16NAN";
  public static String RSTEST_DFP16INF = COLLECTION + ".RSDFP16INF";
  public static String RSTEST_DFP16NNAN = COLLECTION + ".RSDFP16NNAN";
  public static String RSTEST_DFP16NINF = COLLECTION + ".RSDFP16NINF";
  public static String RSTEST_DFP34 = COLLECTION + ".RSDFP34";
  public static String RSTEST_DFP34NAN = COLLECTION + ".RSDFP34NAN";
  public static String RSTEST_DFP34INF = COLLECTION + ".RSDFP34INF";
  public static String RSTEST_DFP34NNAN = COLLECTION + ".RSDFP34NNAN";
  public static String RSTEST_DFP34NINF = COLLECTION + ".RSDFP34NINF";

  public static String RSTEST_GETXML = COLLECTION + ".RSTESTGETX";
  public static String RSTEST_UPDATEXML = COLLECTION + ".RSTESTUPDX";
  public static String RSTEST_BOOLEAN = COLLECTION + ".RSTESTBOOL"; // @F1

  public static String LOB_FULL_DATALINK = "https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt";
  public static String LOB_FULL_DATALINK_UPPER_DOMAIN = "HTTPS://GITHUB.COM/IBM/JTOpen-test/blob/main/README.testing.txt";

  public static String VALUES_DFP16[] = { "1.1", /*
                                                  * must be convertable to a
                                                  * byte
                                                  */
      "1.0", "1234567890123456", "1.00", "35236450.60", "35236450.600",

  };

  public static String VALUES_DFP16_DOUBLE[] = { "1.1", "1.0",
      "1.234567890123456E15", "1.0", "3.52364506E7", "3.52364506E7", };

  public static String VALUES_DFP16_REAL[] = { "1.100000023841858", "1.0",
      "1.234567948140544E15", "1.0", "3.5236452E7", "3.5236452E7", };

  public static String VALUES_DFP16_SHORT[] = { "1", "1",
      "CONTAINS:Exception|conversion", "1", "CONTAINS:Exception|conversion",
      "CONTAINS:Exception|conversion"

  };

  public static String VALUES_DFP16_INT[] = { "1", "1",
      "CONTAINS:Exception|conversion", "1", "35236450", "35236450",

  };

  public static String VALUES_DFP16_LONG[] = { "1", "1", "1234567890123456",
      "1", "35236450", "35236450", };

  public static String VALUES_DFP34[] = { "1.1", "1.0",
      "1234567890123456789012345678901234" };

  public static String VALUES_DFP34_LUWSET[] = { "1.1", "1.0",
      "DECFLOAT('1234567890123456789012345678901234',34)" };

  public static String VALUES_DFP34_DOUBLE[] = { "1.1", "1.0",
      "1.2345678901234568E33" };

  public static String VALUES_DFP34_REAL[] = { "1.100000023841858", "1.0",
      "1.2345678906183669E33" };

  public static String VALUES_DFP34_SHORT[] = { "1", "1",
      "CONTAINS:Exception|conversion" };

  public static String VALUES_DFP34_INT[] = { "1", "1",
      "CONTAINS:Exception|conversion" };

  public static String VALUES_DFP34_LONG[] = { "1", "1", "CONTAINS:Exception" };

  // Comes from section 2.8 of XML 1.0 (Fourth edition)
  public static String SAMPLE_XML1_UTF16LE = "<?xml version=\"1.0\" encoding=\"UTF-16le\"?>\n"
      + "<!DOCTYPE greeting [ \n" + "  <!ELEMENT greeting (#PCDATA)>\n" + "]>\n"
      + "<greeting>Hello, world!</greeting>";

  public static String SAMPLE_XML1 = "<?xml version=\"1.0\" ?>\n"
      + "<!DOCTYPE greeting [ \n" + "  <!ELEMENT greeting (#PCDATA)>\n" + "]>\n"
      + "<greeting>Hello, world!</greeting>";

  public static String SAMPLE_XML1_OUTXML = "<greeting>Hello, world!</greeting>";
  public static String SAMPLE_XML1_OUTXML_WITHDECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><greeting>Hello, world!</greeting>"; // toolbox
                                                                                                                                     // gets
                                                                                                                                     // this
                                                                                                                                     // back
  public static byte[] SAMPLE_XML1_OUTXML_WITHDECL_UTF8 = { (byte) 0x3c,
      (byte) 0x3f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x20,
      (byte) 0x76, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69,
      (byte) 0x6f, (byte) 0x6e, (byte) 0x3d, (byte) 0x22, (byte) 0x31,
      (byte) 0x2e, (byte) 0x30, (byte) 0x22, (byte) 0x20, (byte) 0x65,
      (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x64, (byte) 0x69,
      (byte) 0x6e, (byte) 0x67, (byte) 0x3d, (byte) 0x22, (byte) 0x55,
      (byte) 0x54, (byte) 0x46, (byte) 0x2d, (byte) 0x38, (byte) 0x22,
      (byte) 0x3f, (byte) 0x3e, (byte) 0x3c, (byte) 0x67, (byte) 0x72,
      (byte) 0x65, (byte) 0x65, (byte) 0x74, (byte) 0x69, (byte) 0x6e,
      (byte) 0x67, (byte) 0x3e, (byte) 0x48, (byte) 0x65, (byte) 0x6c,
      (byte) 0x6c, (byte) 0x6f, (byte) 0x2c, (byte) 0x20, (byte) 0x77,
      (byte) 0x6f, (byte) 0x72, (byte) 0x6c, (byte) 0x64, (byte) 0x21,
      (byte) 0x3c, (byte) 0x2f, (byte) 0x67, (byte) 0x72, (byte) 0x65,
      (byte) 0x65, (byte) 0x74, (byte) 0x69, (byte) 0x6e, (byte) 0x67,
      (byte) 0x3e };// SAMPLE_XML1_OUTXML_WITHDECL.getBytes("UTF-8");

  public static byte[] SAMPLE_XML1_OUTXML_UTF8 = { (byte) 0x3c, (byte) 0x67,
      (byte) 0x72, (byte) 0x65, (byte) 0x65, (byte) 0x74, (byte) 0x69,
      (byte) 0x6e, (byte) 0x67, (byte) 0x3e, (byte) 0x48, (byte) 0x65,
      (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x2c, (byte) 0x20,
      (byte) 0x77, (byte) 0x6f, (byte) 0x72, (byte) 0x6c, (byte) 0x64,
      (byte) 0x21, (byte) 0x3c, (byte) 0x2f, (byte) 0x67, (byte) 0x72,
      (byte) 0x65, (byte) 0x65, (byte) 0x74, (byte) 0x69, (byte) 0x6e,
      (byte) 0x67, (byte) 0x3e };

  public static byte[] SAMPLE_XML1_BLOB37 = { (byte) 0x4c, (byte) 0x6f,
      (byte) 0xa7, (byte) 0x94, (byte) 0x93, (byte) 0x40, (byte) 0xa5,
      (byte) 0x85, (byte) 0x99, (byte) 0xa2, (byte) 0x89, (byte) 0x96,
      (byte) 0x95, (byte) 0x7e, (byte) 0x7f, (byte) 0xf1, (byte) 0x4b,
      (byte) 0xf0, (byte) 0x7f, (byte) 0x40, (byte) 0x6f, (byte) 0x6e,
      (byte) 0x15, (byte) 0x4c, (byte) 0x5a, (byte) 0xc4, (byte) 0xd6,
      (byte) 0xc3, (byte) 0xe3, (byte) 0xe8, (byte) 0xd7, (byte) 0xc5,
      (byte) 0x40, (byte) 0x87, (byte) 0x99, (byte) 0x85, (byte) 0x85,
      (byte) 0xa3, (byte) 0x89, (byte) 0x95, (byte) 0x87, (byte) 0x40,
      (byte) 0xba, (byte) 0x40, (byte) 0x15, (byte) 0x40, (byte) 0x40,
      (byte) 0x4c, (byte) 0x5a, (byte) 0xc5, (byte) 0xd3, (byte) 0xc5,
      (byte) 0xd4, (byte) 0xc5, (byte) 0xd5, (byte) 0xe3, (byte) 0x40,
      (byte) 0x87, (byte) 0x99, (byte) 0x85, (byte) 0x85, (byte) 0xa3,
      (byte) 0x89, (byte) 0x95, (byte) 0x87, (byte) 0x40, (byte) 0x4d,
      (byte) 0x7b, (byte) 0xd7, (byte) 0xc3, (byte) 0xc4, (byte) 0xc1,
      (byte) 0xe3, (byte) 0xc1, (byte) 0x5d, (byte) 0x6e, (byte) 0x15,
      (byte) 0xbb, (byte) 0x6e, (byte) 0x15, (byte) 0x4c, (byte) 0x87,
      (byte) 0x99, (byte) 0x85, (byte) 0x85, (byte) 0xa3, (byte) 0x89,
      (byte) 0x95, (byte) 0x87, (byte) 0x6e, (byte) 0xc8, (byte) 0x85,
      (byte) 0x93, (byte) 0x93, (byte) 0x96, (byte) 0x6b, (byte) 0x40,
      (byte) 0xa6, (byte) 0x96, (byte) 0x99, (byte) 0x93, (byte) 0x84,
      (byte) 0x5a, (byte) 0x4c, (byte) 0x61, (byte) 0x87, (byte) 0x99,
      (byte) 0x85, (byte) 0x85, (byte) 0xa3, (byte) 0x89, (byte) 0x95,
      (byte) 0x87, (byte) 0x6e, };
  public static byte[] SAMPLE_XML1_BLOB1208 = { (byte) 0x3c, (byte) 0x3f,
      (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x20, (byte) 0x76,
      (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f,
      (byte) 0x6e, (byte) 0x3d, (byte) 0x22, (byte) 0x31, (byte) 0x2e,
      (byte) 0x30, (byte) 0x22, (byte) 0x20, (byte) 0x3f, (byte) 0x3e,
      (byte) 0xa, (byte) 0x3c, (byte) 0x21, (byte) 0x44, (byte) 0x4f,
      (byte) 0x43, (byte) 0x54, (byte) 0x59, (byte) 0x50, (byte) 0x45,
      (byte) 0x20, (byte) 0x67, (byte) 0x72, (byte) 0x65, (byte) 0x65,
      (byte) 0x74, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x20,
      (byte) 0x5b, (byte) 0x20, (byte) 0xa, (byte) 0x20, (byte) 0x20,
      (byte) 0x3c, (byte) 0x21, (byte) 0x45, (byte) 0x4c, (byte) 0x45,
      (byte) 0x4d, (byte) 0x45, (byte) 0x4e, (byte) 0x54, (byte) 0x20,
      (byte) 0x67, (byte) 0x72, (byte) 0x65, (byte) 0x65, (byte) 0x74,
      (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x20, (byte) 0x28,
      (byte) 0x23, (byte) 0x50, (byte) 0x43, (byte) 0x44, (byte) 0x41,
      (byte) 0x54, (byte) 0x41, (byte) 0x29, (byte) 0x3e, (byte) 0xa,
      (byte) 0x5d, (byte) 0x3e, (byte) 0xa, (byte) 0x3c, (byte) 0x67,
      (byte) 0x72, (byte) 0x65, (byte) 0x65, (byte) 0x74, (byte) 0x69,
      (byte) 0x6e, (byte) 0x67, (byte) 0x3e, (byte) 0x48, (byte) 0x65,
      (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x2c, (byte) 0x20,
      (byte) 0x77, (byte) 0x6f, (byte) 0x72, (byte) 0x6c, (byte) 0x64,
      (byte) 0x21, (byte) 0x3c, (byte) 0x2f, (byte) 0x67, (byte) 0x72,
      (byte) 0x65, (byte) 0x65, (byte) 0x74, (byte) 0x69, (byte) 0x6e,
      (byte) 0x67, (byte) 0x3e, };

  public static byte[] SAMPLE_XML1_BLOB1200 = { (byte) 0x0, (byte) 0x3c,
      (byte) 0x0, (byte) 0x3f, (byte) 0x0, (byte) 0x78, (byte) 0x0, (byte) 0x6d,
      (byte) 0x0, (byte) 0x6c, (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x76,
      (byte) 0x0, (byte) 0x65, (byte) 0x0, (byte) 0x72, (byte) 0x0, (byte) 0x73,
      (byte) 0x0, (byte) 0x69, (byte) 0x0, (byte) 0x6f, (byte) 0x0, (byte) 0x6e,
      (byte) 0x0, (byte) 0x3d, (byte) 0x0, (byte) 0x22, (byte) 0x0, (byte) 0x31,
      (byte) 0x0, (byte) 0x2e, (byte) 0x0, (byte) 0x30, (byte) 0x0, (byte) 0x22,
      (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x3f, (byte) 0x0, (byte) 0x3e,
      (byte) 0x0, (byte) 0xa, (byte) 0x0, (byte) 0x3c, (byte) 0x0, (byte) 0x21,
      (byte) 0x0, (byte) 0x44, (byte) 0x0, (byte) 0x4f, (byte) 0x0, (byte) 0x43,
      (byte) 0x0, (byte) 0x54, (byte) 0x0, (byte) 0x59, (byte) 0x0, (byte) 0x50,
      (byte) 0x0, (byte) 0x45, (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x67,
      (byte) 0x0, (byte) 0x72, (byte) 0x0, (byte) 0x65, (byte) 0x0, (byte) 0x65,
      (byte) 0x0, (byte) 0x74, (byte) 0x0, (byte) 0x69, (byte) 0x0, (byte) 0x6e,
      (byte) 0x0, (byte) 0x67, (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x5b,
      (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0xa, (byte) 0x0, (byte) 0x20,
      (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x3c, (byte) 0x0, (byte) 0x21,
      (byte) 0x0, (byte) 0x45, (byte) 0x0, (byte) 0x4c, (byte) 0x0, (byte) 0x45,
      (byte) 0x0, (byte) 0x4d, (byte) 0x0, (byte) 0x45, (byte) 0x0, (byte) 0x4e,
      (byte) 0x0, (byte) 0x54, (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x67,
      (byte) 0x0, (byte) 0x72, (byte) 0x0, (byte) 0x65, (byte) 0x0, (byte) 0x65,
      (byte) 0x0, (byte) 0x74, (byte) 0x0, (byte) 0x69, (byte) 0x0, (byte) 0x6e,
      (byte) 0x0, (byte) 0x67, (byte) 0x0, (byte) 0x20, (byte) 0x0, (byte) 0x28,
      (byte) 0x0, (byte) 0x23, (byte) 0x0, (byte) 0x50, (byte) 0x0, (byte) 0x43,
      (byte) 0x0, (byte) 0x44, (byte) 0x0, (byte) 0x41, (byte) 0x0, (byte) 0x54,
      (byte) 0x0, (byte) 0x41, (byte) 0x0, (byte) 0x29, (byte) 0x0, (byte) 0x3e,
      (byte) 0x0, (byte) 0xa, (byte) 0x0, (byte) 0x5d, (byte) 0x0, (byte) 0x3e,
      (byte) 0x0, (byte) 0xa, (byte) 0x0, (byte) 0x3c, (byte) 0x0, (byte) 0x67,
      (byte) 0x0, (byte) 0x72, (byte) 0x0, (byte) 0x65, (byte) 0x0, (byte) 0x65,
      (byte) 0x0, (byte) 0x74, (byte) 0x0, (byte) 0x69, (byte) 0x0, (byte) 0x6e,
      (byte) 0x0, (byte) 0x67, (byte) 0x0, (byte) 0x3e, (byte) 0x0, (byte) 0x48,
      (byte) 0x0, (byte) 0x65, (byte) 0x0, (byte) 0x6c, (byte) 0x0, (byte) 0x6c,
      (byte) 0x0, (byte) 0x6f, (byte) 0x0, (byte) 0x2c, (byte) 0x0, (byte) 0x20,
      (byte) 0x0, (byte) 0x77, (byte) 0x0, (byte) 0x6f, (byte) 0x0, (byte) 0x72,
      (byte) 0x0, (byte) 0x6c, (byte) 0x0, (byte) 0x64, (byte) 0x0, (byte) 0x21,
      (byte) 0x0, (byte) 0x3c, (byte) 0x0, (byte) 0x2f, (byte) 0x0, (byte) 0x67,
      (byte) 0x0, (byte) 0x72, (byte) 0x0, (byte) 0x65, (byte) 0x0, (byte) 0x65,
      (byte) 0x0, (byte) 0x74, (byte) 0x0, (byte) 0x69, (byte) 0x0, (byte) 0x6e,
      (byte) 0x0, (byte) 0x67, (byte) 0x0, (byte) 0x3e, };

  public static String SAMPLE_XML2 = "<?xml version=\"1.0\" ?>\n"
      + "<name>Harold</name>";

  public static String SAMPLE_XML2_NODECL = "\n" + "<name>Harold</name>";

  public static String VALUES_XML[] = { SAMPLE_XML1, SAMPLE_XML2,

  };

  public static String VALUES_XML_EXPECTED[] = { SAMPLE_XML1_OUTXML,
      "<name>Harold</name>", null,

  };

  public static byte[] BLOB_MEDIUM;
  public static byte[] BLOB_MEDIUMX;
  public static String CLOB_MEDIUM;
  public static String CLOB_MEDIUMX;
  public static String DBCLOB_MEDIUM;

  public static byte[] BLOB_FULL;
  public static String CLOB_FULL;
  public static String DBCLOB_FULL;

  public static byte[] BLOB_FULLX;
  public static String CLOB_FULLX;
  public static String DBCLOB_FULLX;

  // Private data.
  private Connection connection_;
  private Statement statement_;
  private JDParallelCounter parallelCounter_;

  /**
   * Run the test as an application. This should be called from the test
   * driver's main().
   * 
   * @param args
   *          The command line arguments.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public static void main(String args[]) throws Exception {
    runApplication(new JDRSTest(args));
  }

  /**
   * Constructs an object for applets.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public JDRSTest() throws Exception {
    super();
  }

  /**
   * Constructs an object for testing applications.
   * 
   * @param args
   *          The command line arguments.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public JDRSTest(String[] args) throws Exception {
    super(args);
  }

  void setupRstestGet() throws SQLException {

    StringBuffer errorBuffer = new StringBuffer();
    String sql = "";
    try {
      // Setup RSTEST_GET table.
      StringBuffer buffer = new StringBuffer();
      buffer.append(" (C_KEY VARCHAR(20)");
      buffer.append(",C_SMALLINT        SMALLINT      ");
      buffer.append(",C_INTEGER         INTEGER       ");
      buffer.append(",C_REAL            REAL          ");
      buffer.append(",C_FLOAT           FLOAT         ");
      buffer.append(",C_DOUBLE          DOUBLE        ");
      buffer.append(",C_DECIMAL_50      DECIMAL(5,0)  ");
      buffer.append(",C_DECIMAL_105     DECIMAL(10,5) ");
      buffer.append(",C_NUMERIC_50      NUMERIC(5,0)  ");
      buffer.append(",C_NUMERIC_105     NUMERIC(10,5) ");
      buffer.append(",C_CHAR_1          CHAR          ");
      buffer.append(",C_CHAR_50         CHAR(50)      ");
      buffer.append(",C_NCHAR_50        NCHAR(50)      ");
      buffer.append(",C_VARCHAR_50      VARCHAR(50)   ");
      buffer.append(",C_NVARCHAR_50     NVARCHAR(50)   ");
      buffer.append(",C_BINARY_1       CHAR FOR BIT DATA        ");
      buffer.append(",C_BINARY_20      CHAR(20)  FOR BIT DATA   ");
      buffer.append(",C_VARBINARY_20   VARCHAR(20) FOR BIT DATA ");
      buffer.append(",C_DATE            DATE          ");
      buffer.append(",C_TIME            TIME          ");
      buffer.append(",C_TIMESTAMP       TIMESTAMP     ");
      if (areLobsSupported()) {
        //
        // Cannot have lobs for LUW because scrollable cursors with LOBs
        // are not supported.
        //
        if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
          buffer.append(",C_BLOB         BLOB          ");
          buffer.append(",C_CLOB         CLOB          ");
          buffer.append(",C_NCLOB        NCLOB          ");

          // Note: In V5R2, the
          // next line just said
          // clob....
          buffer.append(",C_DBCLOB       DBCLOB   CCSID 13488  "); // @G2 @G3C
          buffer.append(",C_DISTINCT " + COLLECTION + ".SSN ");
        } else {
          buffer.append(",C_BLOB         VARCHAR(5000) FOR BIT DATA        ");
          buffer.append(",C_CLOB         VARCHAR(6000)         ");
          buffer.append(",C_NCLOB        VARCHAR(6000)         ");
          buffer.append(",C_DBCLOB       VARCHAR(6000)  ");
          buffer.append(",C_DISTINCT     CHAR(9) ");

        }
      }
      if (areBigintsSupported()) // @D0A
        buffer.append(",C_BIGINT       BIGINT"); // @D0A
      else // @D0A
        buffer.append(",C_BIGINT       INTEGER"); // @D0A
      if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
        buffer.append(", C_ROWID       ROWID"); // @pda jdbc40
      } else {
        buffer.append(", C_ROWID       VARCHAR(40) FOR BIT DATA "); // @pda
                                                                    // jdbc40
      }
      buffer.append(", C_VARBINARY_40    VARCHAR(40) FOR BIT DATA ");
       if (areBooleansSupported())  
        buffer.append(",C_BOOLEAN      BOOLEAN"); 
       else 
        buffer.append(",C_BOOLEAN       INTEGER"); 
      buffer.append(")");

      sql = buffer.toString();
      boolean tableCreated = true;
      
      
      // Generate sample lobs.
      Random random = new Random(1);
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        BLOB_FULL = new byte[3743];
      } else {
        BLOB_FULL = new byte[15743];
      }
      random.setSeed(1);
      random.nextBytes(BLOB_FULL);

      // Generate sample lobs.
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        BLOB_FULLX = new byte[3743];
      } else {
        BLOB_FULLX = new byte[15743];
      }
      // Create random XML characters in ASCII
      int blobFullLength = BLOB_FULLX.length;
      BLOB_FULLX[0] = (byte) '<';
      BLOB_FULLX[1] = (byte) 'd';
      BLOB_FULLX[2] = (byte) '>';
      for (int i = 3; i < blobFullLength - 4; i++) {
        BLOB_FULLX[i] = (byte) (' ' + random.nextInt(0x5f));
      }

      BLOB_FULLX[blobFullLength - 4] = (byte) '<';
      BLOB_FULLX[blobFullLength - 3] = (byte) '/';
      BLOB_FULLX[blobFullLength - 2] = (byte) 'd';
      BLOB_FULLX[blobFullLength - 1] = (byte) '>';

      /* Alway generate the lobs */ 
      buffer = new StringBuffer();
      for (int i = 0; i < 2348; ++i)
        buffer.append("All work and no play, etc.   ");
      CLOB_FULL = buffer.toString();
      CLOB_FULLX = "<d>" + buffer.toString() + "</d>";

      // The native driver is aware of the lengths of the
      // lobs and therefore this value has to get truncated
      // the the length of the column that it is going to be
      // put into. The LOB default column length is 2556.
      // I do not know why.
      if (getDriver() == JDTestDriver.DRIVER_NATIVE
          || getDriver() == JDTestDriver.DRIVER_JCC)
        CLOB_FULL = CLOB_FULL.substring(0, 2556);

      buffer = new StringBuffer();
      for (int i = 0; i < 3348; ++i)
        buffer.append("Clobs are the best of all lobs!");
      DBCLOB_FULL = buffer.toString();

      // The native driver is aware of the lengths of the
      // lobs and therefore this value has to get truncated
      // the the length of the column that it is going to be
      // put into. The LOB default column length is 2556.
      // I do not know why.
      if (getDriver() == JDTestDriver.DRIVER_NATIVE
          || getDriver() == JDTestDriver.DRIVER_JCC)
        DBCLOB_FULL = DBCLOB_FULL.substring(0, 2556);

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        BLOB_MEDIUM = new byte[2500];
      } else {
        BLOB_MEDIUM = new byte[6500];
      }
      random.setSeed(2);
      random.nextBytes(BLOB_MEDIUM);

      buffer = new StringBuffer();
      for (int i = 0; i < 24; ++i)
        buffer.append("JDBC is really fast.");
      CLOB_MEDIUM = buffer.toString();

      buffer = new StringBuffer();
      for (int i = 0; i < 24; ++i)
        buffer.append("DBCLOBs are best");
      DBCLOB_MEDIUM = buffer.toString();
   
      

      buffer = new StringBuffer();
      buffer.append("<d>");
      for (int i = 0; i < 2348; ++i)
        buffer.append("All work and no play, etc.   ");
      buffer.append("</d>");
      CLOB_FULLX = buffer.toString();

      // The native driver is aware of the lengths of the
      // lobs and therefore this value has to get truncated
      // the the length of the column that it is going to be
      // put into. The LOB default column length is 2556.
      // I do not know why.
      if (getDriver() == JDTestDriver.DRIVER_NATIVE
          || getDriver() == JDTestDriver.DRIVER_JCC)
        CLOB_FULLX = CLOB_FULLX.substring(0, 2556);

      buffer = new StringBuffer();
      buffer.append("<d>");
      for (int i = 0; i < 3348; ++i)
        buffer.append("Clobs are the best of all lobs!");
      buffer.append("</d>");
      DBCLOB_FULLX = buffer.toString();

      // The native driver is aware of the lengths of the
      // lobs and therefore this value has to get truncated
      // the the length of the column that it is going to be
      // put into. The LOB default column length is 2556.
      // I do not know why.
      if (getDriver() == JDTestDriver.DRIVER_NATIVE
          || getDriver() == JDTestDriver.DRIVER_JCC)
        DBCLOB_FULLX = DBCLOB_FULLX.substring(0, 2556);

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        BLOB_MEDIUMX = new byte[2500];
      } else {
        BLOB_MEDIUMX = new byte[6500];
      }

      int blobMediumLength = BLOB_MEDIUMX.length;
      BLOB_MEDIUMX[0] = (byte) '<';
      BLOB_MEDIUMX[1] = (byte) 'd';
      BLOB_MEDIUMX[2] = (byte) '>';
      for (int i = 3; i < blobMediumLength - 4; i++) {
        BLOB_MEDIUMX[i] = (byte) (' ' + random.nextInt(0x5f));
      }

      BLOB_MEDIUMX[blobMediumLength - 4] = (byte) '<';
      BLOB_MEDIUMX[blobMediumLength - 3] = (byte) '/';
      BLOB_MEDIUMX[blobMediumLength - 2] = (byte) 'd';
      BLOB_MEDIUMX[blobMediumLength - 1] = (byte) '>';

      buffer = new StringBuffer();
      buffer.append("<d>");
      for (int i = 0; i < 24; ++i)
        buffer.append("JDBC is really fast.");
      buffer.append("</d>");
      CLOB_MEDIUMX = buffer.toString();


      tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_GET, sql, errorBuffer);
      if (tableCreated) {
        // Key == NUMBER_0.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50)"
            + " VALUES ('NUMBER_0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '0', '0', '0', '0')";
        statement_.executeUpdate(sql); // @D0A

        // Key == NUMBER_POS.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50)"
            + " VALUES ('NUMBER_POS', 198, 98765, 12374321, 4.4, 5.55, 6.666, 7, " // @D0A
            + " 8.8888, 9, 10.10105, '1', '1','1','1')";
        statement_.executeUpdate(sql);

        // Key == NUMBER_NEG.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105, C_BOOLEAN)"
            + " VALUES ('NUMBER_NEG', -198, -98765, -44332123, -4.4, -5.55, -6.666, -7, " // @D0A
            + " -8.8888, -9, -10.10105, 0)";
        statement_.executeUpdate(sql);

        // Key == NUMBER_NULL.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105, C_BOOLEAN)"
            + " VALUES ('NUMBER_NULL', NULL, NULL, NULL, NULL, NULL, NULL, " // @D0A
            + " NULL, NULL, NULL, NULL, NULL)";
        statement_.executeUpdate(sql);

        // Key == CHAR_EMPTY.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50,  "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_EMPTY', '', '', '','','')";
        statement_.executeUpdate(sql);

        // Key == CHAR_FULL.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_FULL', 'X', "
            + " 'Toolbox for Java', 'Toolbox for Java', 'Java Toolbox', 'Java Toolbox')";
        statement_.executeUpdate(sql);

        // Key == CHAR_BOOLEAN_TRUE.
        sql = "INSERT INTO " + RSTEST_GET + " (C_KEY, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_BOOLEAN_TRUE', 'TrUe',  'TrUe', "
            + " 'true', 'true')";
        statement_.executeUpdate(sql);

        // Key == CHAR_BOOLEAN_FALSE.
        sql = "INSERT INTO " + RSTEST_GET + " (C_KEY, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_BOOLEAN_FALSE', 'false', 'false',"
            + " 'fAlSe',  'fAlSe')";
        statement_.executeUpdate(sql);

        // Key == CHAR_INT.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50,"
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_INT', '9', "
            + " '-55', '-55', '567', '567')";
        statement_.executeUpdate(sql);

        // Key == CHAR_FLOAT.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_FLOAT', '9', "
            + " '55.901', '55.901', '-567.56', '-567.56')";
        statement_.executeUpdate(sql);

        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          // No support for escape processing
          // Key == CHAR_DATE.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_DATE', "
              + "'1991-03-15', '1991-03-15',  '2023-11-21',  '2023-11-21')";
          statement_.executeUpdate(sql);

          // Key == CHAR_TIME.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIME', "
              + " '15:55:04', '15:55:04',  '00:01:05', '00:01:05')";
          statement_.executeUpdate(sql);

          // Key == CHAR_TIMESTAMP.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIMESTAMP', "
              + " '2010-02-28 03:23:03.48392', "
              + " '2010-02-28 03:23:03.48392', "
              + " '1984-07-04 23:23:01.102034',"
              + " '1984-07-04 23:23:01.102034')";
          statement_.executeUpdate(sql);

        } else {

          // Key == CHAR_DATE.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_DATE', "
              + "{d '1991-03-15'},{d '1991-03-15'}, {d '2023-11-21'}, {d '2023-11-21'})";
          statement_.executeUpdate(sql);

          // Key == CHAR_TIME.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_CHAR_50,C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIME', "
              + "{t '15:55:04'},{t '15:55:04'}, {t '00:01:05'}, {t '00:01:05'})";
          statement_.executeUpdate(sql);

          // Key == CHAR_TIMESTAMP.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIMESTAMP', "
              + "{ts '2010-02-28 03:23:03.48392'}, "
              + "{ts '2010-02-28 03:23:03.48392'}, "
              + "{ts '1984-07-04 23:23:01.102034'}, "
              + "{ts '1984-07-04 23:23:01.102034'})";
          statement_.executeUpdate(sql);

        }
        // Key == CHAR_NULL.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50 ) VALUES ('CHAR_NULL', NULL, "
            + " NULL, NULL,NULL,NULL)";
        statement_.executeUpdate(sql);

        // Key == BINARY_NOTRANS.
        PreparedStatement ps3;
        sql = "INSERT INTO " + RSTEST_GET + " (C_KEY, C_BINARY_1, C_BINARY_20, "
            + " C_VARBINARY_20) VALUES ('BINARY_NOTRANS', ?, ?, ?)";
        ps3 = connection_.prepareStatement(sql);

        ps3.setBytes(1, new byte[] { (byte) 0xF1 });
        ps3.setBytes(2,
            new byte[] { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v',
                (byte) 'e', (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ',
                (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                (byte) ' ' });
        ps3.setBytes(3, new byte[] { (byte) 'T', (byte) 'w', (byte) 'e',
            (byte) 'l', (byte) 'v', (byte) 'e' });
        try {
          ps3.executeUpdate();
        } catch (Exception e) {
          System.out.println("Warning:  Exception inserting binary data");
          System.err.flush();
          System.out.flush();
          e.printStackTrace();
          System.err.flush();
          System.out.flush();
        }
        ps3.close();

        // Key == BINARY_TRANS.
        sql = "INSERT INTO " + RSTEST_GET + " (C_KEY, C_BINARY_1, C_BINARY_20, "
            + " C_VARBINARY_20) VALUES ('BINARY_TRANS', 'A', "
            + " 'Thirteen', 'Fourteen')";
        statement_.executeUpdate(sql);

        // Key == BINARY_NULL.
        sql = "INSERT INTO " + RSTEST_GET + " (C_KEY, C_BINARY_1, C_BINARY_20, "
            + " C_VARBINARY_20) VALUES ('BINARY_NULL', NULL, " + " NULL, NULL)";
        statement_.executeUpdate(sql);

        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          // Key == DATE_1998.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_1998', '1998-04-08', '08:14:03', "
              + "  '1998-11-18 03:13:42.987654')";
          statement_.executeUpdate(sql);

          // Key == DATE_2000.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_2000', '2000-02-21',  '14:04:55', "
              + "  '2000-06-25 10:30:12.345676')";
          statement_.executeUpdate(sql);

        } else {
          // Key == DATE_1998.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_1998', {d '1998-04-08'}, {t '08:14:03'}, "
              + " {ts '1998-11-18 03:13:42.987654'})";
          statement_.executeUpdate(sql);

          // Key == DATE_2000.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_2000', {d '2000-02-21'}, {t '14:04:55'}, "
              + " {ts '2000-06-25 10:30:12.345676'})";
          statement_.executeUpdate(sql);
        }

        // Key == DATE_NULL.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
            + " ('DATE_NULL', NULL, NULL, NULL)";
        statement_.executeUpdate(sql);

        if (areLobsSupported()) {
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_BLOB, C_CLOB, C_NCLOB, C_DBCLOB, " + " C_DISTINCT) "
              + " VALUES (?, ?, ?, ?, ?, " + "?)";

          PreparedStatement ps4 = connection_.prepareStatement(sql);



          try {
            // Key == LOB_FULL.
            ps4.setString(1, "LOB_FULL");
            ps4.setBytes(2, BLOB_FULL);
            ps4.setString(3, CLOB_FULL);
            ps4.setString(4, CLOB_FULL);
            ps4.setString(5, DBCLOB_FULL);
            ps4.setString(6, "123456789");
            ps4.executeUpdate();

            // Key == LOB_MEDIUM.
            ps4.setString(1, "LOB_MEDIUM");
            ps4.setBytes(2, BLOB_MEDIUM);
            ps4.setString(3, CLOB_MEDIUM);
            ps4.setString(4, CLOB_MEDIUM);
            // Updated DBCLOB_MEDIUM on 1/4/2012
            ps4.setString(5, DBCLOB_MEDIUM);
            // Get short is used on this value,
            // So make sure a short actually fits
            //
            ps4.setString(6, "0");
            ps4.executeUpdate();

            // Key == LOB_EMPTY.
            ps4.setString(1, "LOB_EMPTY");
            ps4.setBytes(2, new byte[] {});
            ps4.setString(3, "");
            ps4.setString(4, "");
            ps4.setString(5, "");
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              ps4.setString(6, "0");
            } else {
              ps4.setString(6, "");
            }
            ps4.executeUpdate();
          } catch (Exception e) {
            System.out.println("Warning:  Exception thrown processing LOBS");
            System.err.flush();
            System.out.flush();
            e.printStackTrace();
            System.err.flush();
            System.out.flush();
          }
          ps4.close();

          // Key == LOB_NULL.
          sql = "INSERT INTO " + RSTEST_GET
              + " (C_KEY, C_BLOB, C_CLOB, C_NCLOB, C_DBCLOB, " + " C_DISTINCT) "
              + " VALUES ('LOB_NULL', NULL, NULL, NULL, NULL, " + " NULL)";
          statement_.executeUpdate(sql);

        }

        // Key == UPDATE_SANDBOX.
        sql = "INSERT INTO " + RSTEST_GET
            + " (C_KEY) VALUES ('UPDATE_SANDBOX')";
        statement_.executeUpdate(sql);

      } // tableCreated RSTEST_GET

    } catch (SQLException e) {
      System.out.println("Error in setupRstest info=" + errorBuffer);
      System.out.println("Last sql = " + sql);
      System.out.println("Rethrowing error");
      throw e;
    }
  }

  void setupRstestGetX() throws SQLException {
    String sql = "";
    StringBuffer errorBuffer = new StringBuffer();
    try {
      StringBuffer buffer = new StringBuffer();
      buffer.append(" (C_KEY VARCHAR(20)");
      buffer.append(",C_SMALLINT        SMALLINT      ");
      buffer.append(",C_INTEGER         INTEGER       ");
      buffer.append(",C_REAL            REAL          ");
      buffer.append(",C_FLOAT           FLOAT         ");
      buffer.append(",C_DOUBLE          DOUBLE        ");
      buffer.append(",C_DECIMAL_50      DECIMAL(5,0)  ");
      buffer.append(",C_DECIMAL_105     DECIMAL(10,5) ");
      buffer.append(",C_NUMERIC_50      NUMERIC(5,0)  ");
      buffer.append(",C_NUMERIC_105     NUMERIC(10,5) ");
      buffer.append(",C_CHAR_1          CHAR          ");
      buffer.append(",C_CHAR_50         CHAR(50)      ");
      buffer.append(",C_NCHAR_50        NCHAR(50)      ");
      buffer.append(",C_VARCHAR_50      VARCHAR(50)   ");
      buffer.append(",C_NVARCHAR_50     NVARCHAR(50)   ");
      buffer.append(",C_BINARY_1       CHAR FOR BIT DATA        ");
      buffer.append(",C_BINARY_20      CHAR(20)  FOR BIT DATA   ");
      buffer.append(",C_VARBINARY_20   VARCHAR(20) FOR BIT DATA ");
      buffer.append(",C_DATE            DATE          ");
      buffer.append(",C_TIME            TIME          ");
      buffer.append(",C_TIMESTAMP       TIMESTAMP     ");
      if (areLobsSupported()) {
        //
        // Cannot have lobs for LUW because scrollable cursors with LOBs
        // are not supported.
        //
        if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
          buffer.append(",C_BLOB         BLOB          ");
          buffer.append(",C_CLOB         CLOB          ");
          buffer.append(",C_NCLOB        NCLOB          ");
          // Note: In V5R2, the
          // next line just said
          // clob....
          buffer.append(",C_DBCLOB       DBCLOB   CCSID 13488  "); // @G2 @G3C
          buffer.append(",C_DISTINCT " + COLLECTION + ".SSN ");
        } else {
          buffer.append(",C_BLOB         VARCHAR(5000) FOR BIT DATA        ");
          buffer.append(",C_CLOB         VARCHAR(6000)         ");
          buffer.append(",C_NCLOB        VARCHAR(6000)         ");
          buffer.append(",C_DBCLOB       VARCHAR(6000)  ");
          buffer.append(",C_DISTINCT     CHAR(9) ");

        }
      }
      if (areBigintsSupported()) // @D0A
        buffer.append(",C_BIGINT       BIGINT"); // @D0A
      else // @D0A
        buffer.append(",C_BIGINT       INTEGER"); // @D0A
      if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
        buffer.append(", C_ROWID       ROWID"); // @pda jdbc40
      } else {
        buffer.append(", C_ROWID       VARCHAR(40) FOR BIT DATA "); // @pda
                                                                    // jdbc40
      }
      buffer.append(", C_VARBINARY_40    VARCHAR(40) FOR BIT DATA ");
      buffer.append(")");
      boolean tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_GETX, buffer.toString(),
          errorBuffer);
      if (tableCreated) {
        // Key == NUMBER_0.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50)"
            + " VALUES ('NUMBER_0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '0', '0','0','0')"); // @D0A

        // Key == NUMBER_POS.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50)"
            + " VALUES ('NUMBER_POS', 198, 98765, 12374321, 4.4, 5.55, 6.666, 7, " // @D0A
            + " 8.8888, 9, 10.10105, '1', '1', '1', '1')");

        // Key == NUMBER_NEG.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105)"
            + " VALUES ('NUMBER_NEG', -198, -98765, -44332123, -4.4, -5.55, -6.666, -7, " // @D0A
            + " -8.8888, -9, -10.10105)");

        // Key == NUMBER_NULL.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_SMALLINT, C_INTEGER, C_BIGINT, C_REAL, C_FLOAT, " // @D0A
            + " C_DOUBLE, C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
            + " C_NUMERIC_105)"
            + " VALUES ('NUMBER_NULL', NULL, NULL, NULL, NULL, NULL, NULL, " // @D0A
            + " NULL, NULL, NULL, NULL)");

        // Key == CHAR_EMPTY.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_EMPTY', '', '', '', '', '')");

        // Key == CHAR_FULL.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_FULL', 'X', "
            + " '<d>Toolbox for Java</d>', '<d>Toolbox for Java</d>', '<d>Java Toolbox</d>', '<d>Java Toolbox</d>')");

        // Key == CHAR_BOOLEAN_TRUE.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_BOOLEAN_TRUE', 'TrUe', 'TrUe', "
            + " 'true', 'true')");

        // Key == CHAR_BOOLEAN_FALSE.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_BOOLEAN_FALSE', 'false', 'false', "
            + " 'fAlSe', 'fAlSe')");

        // Key == CHAR_INT.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_INT', '9', "
            + " '-55', '-55', '567', '567')");

        // Key == CHAR_FLOAT.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_FLOAT', '9', "
            + " '55.901','55.901', '-567.56', '-567.56')");

        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          // No support for escape processing
          // Key == CHAR_DATE.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_DATE', "
              + "'1991-03-15', '1991-03-15',  '2023-11-21',  '2023-11-21')");

          // Key == CHAR_TIME.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIME', "
              + " '15:55:04','15:55:04',  '00:01:05',  '00:01:05')");

          // Key == CHAR_TIMESTAMP.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIMESTAMP', "
              + " '2010-02-28 03:23:03.48392', "
              + " '2010-02-28 03:23:03.48392', "
              + " '1984-07-04 23:23:01.102034', "
              + " '1984-07-04 23:23:01.102034')");

        } else {

          // Key == CHAR_DATE.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50 ) VALUES ('CHAR_DATE', "
              + "{d '1991-03-15'}, {d '1991-03-15'}, {d '2023-11-21'}, {d '2023-11-21'})");

          // Key == CHAR_TIME.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIME', "
              + "{t '15:55:04'},{t '15:55:04'}, {t '00:01:05'}, {t '00:01:05'})");

          // Key == CHAR_TIMESTAMP.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_CHAR_50, C_NCHAR_50, C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_TIMESTAMP', "
              + "{ts '2010-02-28 03:23:03.48392'}, "
              + "{ts '2010-02-28 03:23:03.48392'}, "
              + "{ts '1984-07-04 23:23:01.102034'}, "
              + "{ts '1984-07-04 23:23:01.102034'})");

        }
        // Key == CHAR_NULL.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_CHAR_1, C_CHAR_50, C_NCHAR_50, "
            + " C_VARCHAR_50, C_NVARCHAR_50) VALUES ('CHAR_NULL', NULL, "
            + " NULL, NULL, NULL, NULL)");

        // Key == BINARY_NOTRANS.
        PreparedStatement ps3;
        ps3 = connection_.prepareStatement(
            "INSERT INTO " + RSTEST_GETX + " (C_KEY, C_BINARY_1, C_BINARY_20, "
                + " C_VARBINARY_20) VALUES ('BINARY_NOTRANS', ?, ?, ?)");

        ps3.setBytes(1, new byte[] { (byte) 0xF1 });
        ps3.setBytes(2,
            new byte[] { (byte) '<', (byte) 'd', (byte) '>', (byte) 'E',
                (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
                (byte) '<', (byte) '/', (byte) 'd', (byte) '>', (byte) ' ',
                (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                (byte) ' ' });
        ps3.setBytes(3,
            new byte[] { (byte) '<', (byte) 'd', (byte) '>', (byte) 'T',
                (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v', (byte) 'e',
                (byte) '<', (byte) '/', (byte) 'd', (byte) '>' });
        try {
          ps3.executeUpdate();
        } catch (Exception e) {
          System.out.println("Warning:  Exception inserting binary data");
          System.err.flush();
          System.out.flush();
          e.printStackTrace();
          System.err.flush();
          System.out.flush();
        }
        ps3.close();

        // Key == BINARY_TRANS.
        statement_.executeUpdate(
            "INSERT INTO " + RSTEST_GETX + " (C_KEY, C_BINARY_1, C_BINARY_20, "
                + " C_VARBINARY_20) VALUES ('BINARY_TRANS', 'A', "
                + " '<d>Thirteen</d>', '<d>Fourteen</d>')");

        // Key == BINARY_NULL.
        statement_.executeUpdate(
            "INSERT INTO " + RSTEST_GETX + " (C_KEY, C_BINARY_1, C_BINARY_20, "
                + " C_VARBINARY_20) VALUES ('BINARY_NULL', NULL, "
                + " NULL, NULL)");

        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          // Key == DATE_1998.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_1998', '1998-04-08', '08:14:03', "
              + "  '1998-11-18 03:13:42.987654')");

          // Key == DATE_2000.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_2000', '2000-02-21',  '14:04:55', "
              + "  '2000-06-25 10:30:12.345676')");

        } else {
          // Key == DATE_1998.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_1998', {d '1998-04-08'}, {t '08:14:03'}, "
              + " {ts '1998-11-18 03:13:42.987654'})");

          // Key == DATE_2000.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
              + " ('DATE_2000', {d '2000-02-21'}, {t '14:04:55'}, "
              + " {ts '2000-06-25 10:30:12.345676'})");
        }

        // Key == DATE_NULL.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY, C_DATE, C_TIME, C_TIMESTAMP) VALUES "
            + " ('DATE_NULL', NULL, NULL, NULL)");

        if (areLobsSupported()) {

          PreparedStatement ps4 = connection_.prepareStatement("INSERT INTO "
              + RSTEST_GETX + " (C_KEY, C_BLOB, C_CLOB, C_NCLOB, C_DBCLOB, "
              + " C_DISTINCT) " + " VALUES (?, ?, ?, ?,?, " + "?)");

          try {
            // Key == LOB_FULL.
            ps4.setString(1, "LOB_FULL");
            ps4.setBytes(2, BLOB_FULLX);
            ps4.setString(3, CLOB_FULLX);
            ps4.setString(4, CLOB_FULLX);
            ps4.setString(5, DBCLOB_FULLX);
            ps4.setString(6, "123456789");
            ps4.executeUpdate();

            // Key == LOB_MEDIUM.
            ps4.setString(1, "LOB_MEDIUM");
            ps4.setBytes(2, BLOB_MEDIUMX);
            ps4.setString(3, CLOB_MEDIUMX);
            ps4.setString(4, CLOB_MEDIUMX);
            ps4.setString(5, "");
            // Get short is used on this value,
            // So make sure a short actually fits
            //
            ps4.setString(6, "0");
            ps4.executeUpdate();

            // Key == LOB_EMPTY.
            ps4.setString(1, "LOB_EMPTY");
            ps4.setBytes(2, new byte[] {});
            ps4.setString(3, "");
            ps4.setString(4, "");
            ps4.setString(5, "");
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              ps4.setString(6, "0");
            } else {
              ps4.setString(6, "");
            }
            ps4.executeUpdate();
          } catch (Exception e) {
            System.out.println("Warning:  Exception thrown processing LOBS");
            System.err.flush();
            System.out.flush();
            e.printStackTrace();
            System.err.flush();
            System.out.flush();
          }
          ps4.close();

          // Key == LOB_NULL.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
              + " (C_KEY, C_BLOB, C_CLOB, C_NCLOB,  C_DBCLOB, "
              + " C_DISTINCT) "
              + " VALUES ('LOB_NULL', NULL, NULL, NULL, NULL, " + " NULL)");

        }

        // Key == UPDATE_SANDBOX.
        statement_.executeUpdate("INSERT INTO " + RSTEST_GETX
            + " (C_KEY) VALUES ('UPDATE_SANDBOX')");

      } // tableCreated RSTEST_GETX

    } catch (SQLException e) {
      System.out.println("Error in setupRstest info=" + errorBuffer);
      System.out.println("Last sql = " + sql);
      System.out.println("Rethrowing error");
      throw e;
    }
  }

  /**
   * Performs setup needed before running testcases.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public void setup() throws Exception {

    super.setup(); // @D2A

    connection_ = getConnection(getBaseURL(), systemObject_.getUserId(),
        encryptedPassword_, "INFO_JDRSTEST_SETUP2");

    parallelCounter_ = new JDParallelCounter(connection_, "JDRSCONCUR"); 
    
    try {
      setup2();
    } catch (Exception e) {
      System.out.println("WARNING:  setup2() failed");
      System.out.flush();
      e.printStackTrace();
      System.err.flush();

      //
      // Sometimes the collection can get into a weird state.
      // When this happens, delete the entire collection and retry again
      //

      String exceptionInfo = e.toString();
      if ((exceptionInfo.indexOf("not valid for operation") > 0)
          || (exceptionInfo.indexOf("type *SQLUDT already exists") > 0)) {
        System.out.println(
            "WARNING:  found '" + exceptionInfo + "':  deleting collection");
        if (connection_ != null)
          connection_.close();
        connection_ = getConnection(getBaseURL(), systemObject_.getUserId(),
            encryptedPassword_, "INFO_JDRSTEST_SETUP");

        JDTestDriver.dropCollection(connection_, COLLECTION);

        System.out.println("WARNING:  attemping to run setup again");
        setup2();

      }

    }

  }

  public void setup2() throws Exception {
    StringBuffer errorBuffer = new StringBuffer();
    String sql = "";
    System.out.println("Running JDRSTest.setup2()"); 
    try {
      boolean tableCreated;
      // Initialization.

      if (testLib_ != null) { // @E2A
        COLLECTION = testLib_;

        RSTEST_GET = COLLECTION + ".RSTESTGET";
        RSTEST_GETX = COLLECTION + ".RSTESTGTX";
        RSTEST_GETDL = COLLECTION + ".RSTESTGETDL";
        RSTEST_BINARY = COLLECTION + ".RSTESTBIN";
        RSTEST_BOOLEAN = COLLECTION + ".RSTESTBOOL";
        RSTEST_POS = COLLECTION + ".RSTESTPOS";
        RSTEST_UPDATE = COLLECTION + ".RSTESTUPD";
        RSTEST_SCROLL = COLLECTION + ".RSTSTSCRL";
        RSTEST_GRAPHIC = COLLECTION + ".RSTESTGRAPHIC";
        RSTEST_SENSITIVE = COLLECTION + ".RSTESTSENS"; // @G1A

        RSTEST_DFP16 = COLLECTION + ".RSDFP16";
        RSTEST_DFP16NAN = COLLECTION + ".RSDFP16NAN";
        RSTEST_DFP16INF = COLLECTION + ".RSDFP16INF";
        RSTEST_DFP16NNAN = COLLECTION + ".RSDFP16NNAN";
        RSTEST_DFP16NINF = COLLECTION + ".RSDFP16NINF";
        RSTEST_DFP34 = COLLECTION + ".RSDFP34";
        RSTEST_DFP34NAN = COLLECTION + ".RSDFP34NAN";
        RSTEST_DFP34INF = COLLECTION + ".RSDFP34INF";
        RSTEST_DFP34NNAN = COLLECTION + ".RSDFP34NNAN";
        RSTEST_DFP34NINF = COLLECTION + ".RSDFP34NINF";
        RSTEST_GETXML = COLLECTION + ".RSTESTGETX";
        RSTEST_UPDATEXML = COLLECTION + ".RSTESTUPDX";

      }

      JDSetupCollection.create(systemObject_,  connection_,
          COLLECTION, out_);

      statement_ = connection_.createStatement();

      // The native driver does not do escape processing by default
      // so we need to explicitly turn it on.
      if (getDriver() != JDTestDriver.DRIVER_JTOPENLITE) {
        statement_.setEscapeProcessing(true);
      }

      // Create a distinct type.
      if (areLobsSupported()) {

        // Do not always drop distinct type
        // dropDistinctType(statement_, COLLECTION + ".SSN");
        sql = "CREATE DISTINCT TYPE " + COLLECTION + ".SSN AS CHAR(9) "
            + "WITH COMPARISONS";
        safeExecuteUpdate(statement_, sql,
            IGNORE_FAILURES); /* added with comparisons for luw */
      }

      setupRstestGet();
      setupRstestGetX();

      // Setup RSTEST_GETDL table. We need this because we cannot open
      // an updatable cursor on the other table if it includes a datalink.
      StringBuffer buffer = new StringBuffer();
      if (areLobsSupported()) {

        buffer.setLength(0);
        buffer.append(" (C_KEY VARCHAR(20)");
        if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
          buffer.append(",C_DATALINK     DATALINK      )");
        } else {
          buffer.append(",C_DATALINK     VARCHAR(150)   )");
        }
        sql = buffer.toString();
        tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_GETDL, sql, errorBuffer);

        if (tableCreated) {
          PreparedStatement ps4;
          if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
            sql = "INSERT INTO " + RSTEST_GETDL + " (C_KEY, C_DATALINK) "
                + " VALUES (?, DLVALUE( CAST(? AS VARCHAR(200)),'URL'))";
            ps4 = connection_.prepareStatement(sql);
          } else {
            sql = "INSERT INTO " + RSTEST_GETDL + " (C_KEY, C_DATALINK) "
                + " VALUES (?, ? )";
            ps4 = connection_.prepareStatement(sql);
          }
          // Key == LOB_FULL.
          ps4.setString(1, "LOB_FULL");
          ps4.setString(2, LOB_FULL_DATALINK );
          ps4.executeUpdate();

          // Key == LOB_EMPTY.
          ps4.setString(1, "LOB_EMPTY");
          ps4.setString(2, "");
          ps4.executeUpdate();

          ps4.close();

          // Key == LOB_NULL.
          sql = "INSERT INTO " + RSTEST_GETDL + " (C_KEY, C_DATALINK) "
              + " VALUES ('LOB_NULL', NULL)";
          statement_.executeUpdate(sql);

          // Key == UPDATE_SANDBOX.
          sql = "INSERT INTO " + RSTEST_GETDL
              + " (C_KEY) VALUES ('UPDATE_SANDBOX')";
          statement_.executeUpdate(sql);
        } // tableCreated RSTEST_GETDL
      }

      // @F2
      // Setup RSTEST_GRAPH table.
      if (true) {
        buffer = new StringBuffer();

        buffer = new StringBuffer();

        buffer.append(" (C_KEY VARCHAR(20)");
        if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
          buffer.append(",C_GRAPHIC         GRAPHIC(50)    CCSID 1200 ");
          buffer.append(",C_VARGRAPHIC      VARGRAPHIC(50) CCSID 1200 ");
          buffer.append(",C_GRAPHIC_835     GRAPHIC(10)    CCSID 835 "); // @G2
          buffer.append(",C_VARGRAPHIC_835  VARGRAPHIC(10) CCSID 835 "); // @G2
        } else {
          buffer.append(",C_GRAPHIC         CHAR(50)     ");
          buffer.append(",C_VARGRAPHIC      VARCHAR(50)  ");
          buffer.append(",C_GRAPHIC_835     CHAR(20)    "); // @G2
          buffer.append(",C_VARGRAPHIC_835  VARCHAR(20)  "); // @G2
        }
        buffer.append(")");
        sql = buffer.toString();
        tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_GRAPHIC, sql, errorBuffer);
        char[] mychar = { '\u5e03', '\u5f17', '\u672b', '\u5378' }; // @G2
        String unicode = new String(mychar); // @G2

        if (tableCreated) {

          // Key == GRAPHIC_FULL.
          statement_.executeUpdate("INSERT INTO " + RSTEST_GRAPHIC
              + " (C_KEY, C_GRAPHIC, C_VARGRAPHIC, C_GRAPHIC_835, C_VARGRAPHIC_835)" // @G2
              + " VALUES ('GRAPHIC_FULL', 'TOOLBOX FOR JAVA', 'JAVA TOOLBOX', '"
              + unicode + "' , '" + unicode + "')"); // @G2
        } // tableCreated RSTEST_GRAPH
      }
      // @F1
      // Setup RSTEST_BINARY table.
      // in release V5R3 binary and varbinary will be their own data type in DB2
      if (true) {

        buffer.setLength(0);
        buffer.append(" (C_KEY           VARCHAR(20)   ");
        if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
          buffer.append(",C_BINARY_1       BINARY        ");
          buffer.append(",C_BINARY_20      BINARY(20)    ");
          buffer.append(",C_VARBINARY_20   VARBINARY(20) ");
          buffer.append(",C_PADDED         VARBINARY(20) ");
        } else {
          buffer.append(",C_BINARY_1       CHAR        FOR BIT DATA");
          buffer.append(",C_BINARY_20      CHAR(20)    FOR BIT DATA");
          buffer.append(",C_VARBINARY_20   VARCHAR(20) FOR BIT DATA");
          buffer.append(",C_PADDED         VARCHAR(20) FOR BIT DATA");
        }
        buffer.append(")");
        tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_BINARY, buffer.toString(),
            errorBuffer);
        if (tableCreated) {
          // Key == BINARY_NOTRANS.
          PreparedStatement ps5 = connection_.prepareStatement("INSERT INTO "
              + RSTEST_BINARY + " (C_KEY, C_BINARY_1, C_BINARY_20, "
              + " C_VARBINARY_20, C_PADDED) VALUES ('BINARY_NOTRANS', ?, ?, ?, ?)");

          ps5.setBytes(1, new byte[] { (byte) 0xF1 });
          ps5.setBytes(2,
              new byte[] { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v',
                  (byte) 'e', (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ',
                  (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                  (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                  (byte) ' ' });
          ps5.setBytes(3, new byte[] { (byte) 'T', (byte) 'w', (byte) 'e',
              (byte) 'l', (byte) 'v', (byte) 'e' });
          ps5.setBytes(4, new byte[] { (byte) 'T', (byte) 'w', (byte) 'e',
              (byte) 'l', (byte) 'v', (byte) 'e', (byte) '\0' });
          ps5.executeUpdate();
          ps5.close();

          ps5 = connection_.prepareStatement("INSERT INTO " + RSTEST_BINARY
              + " (C_KEY, C_BINARY_1, C_BINARY_20, "
              + " C_VARBINARY_20) VALUES ('BINARY_TRANS', ?, ?, ?)");
          try {
            if (getDriver() == DRIVER_TOOLBOX) {
              ps5.setString(1, "41");
              ps5.setString(2, "546869727465656e");
              ps5.setString(3, "666f75727465656e");
            } else {
              ps5.setString(1, "A");
              ps5.setString(2, "Thirteen");
              ps5.setString(3, "fourteen");
            }
            ps5.executeUpdate();
          } catch (SQLException e) {
            System.out.println(
                "Warning:  Unable to setup row BINARY_TRANS because of "
                    + e.toString());
          }
          ps5.close();
          // This fails currently, should work at V5R3 GA
          // Key == BINARY_TRANS.
          // safeExecuteUpdate(statement_,"INSERT INTO " + RSTEST_BINARY
          // + " (C_KEY, C_BINARY_1, C_BINARY_20, "
          // + " C_VARBINARY_20) VALUES ('BINARY_TRANS', 'A', "
          // + " 'Thirteen', 'Fourteen')");

          // Key == BINARY_NULL.
          statement_.executeUpdate("INSERT INTO " + RSTEST_BINARY
              + " (C_KEY, C_BINARY_1, C_BINARY_20, "
              + " C_VARBINARY_20) VALUES ('BINARY_NULL', NULL, "
              + " NULL, NULL)");

          // Key == UPDATE_SANDBOX
          statement_.executeUpdate("INSERT INTO " + RSTEST_BINARY
              + " (C_KEY) VALUES('UPDATE_SANDBOX')");
        }
      } // tableCreated RSTEST_BINARY

      // Setup RSTEST_POS table.
      tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_POS,
          " (ID INT, VALUE VARCHAR(20))", errorBuffer);
      if (tableCreated) {
        for (int i = 1; i <= 99; ++i) {
          statement_.executeUpdate("INSERT INTO " + RSTEST_POS
              + " (ID, VALUE) VALUES (" + i + ", 'Speed on, JDBC.')");
        }
      } // tableCreated RSTEST_POS

      // Setup RSTEST_UPDATE table.

      buffer.setLength(0);
      buffer.append(" (C_KEY VARCHAR(50)");
      buffer.append(",C_SMALLINT        SMALLINT      ");
      buffer.append(",C_INTEGER         INTEGER       ");
      buffer.append(",C_REAL            REAL          ");
      buffer.append(",C_FLOAT           FLOAT         ");
      buffer.append(",C_DOUBLE          DOUBLE        ");
      buffer.append(",C_DECIMAL_40      DECIMAL(4,0)  ");
      buffer.append(",C_DECIMAL_105     DECIMAL(10,5) ");
      buffer.append(",C_NUMERIC_40      NUMERIC(4,0)  ");
      buffer.append(",C_NUMERIC_105     NUMERIC(10,5) ");
      buffer.append(",C_CHAR_1          CHAR          ");
      buffer.append(",C_CHAR_50         CHAR(50)      ");
      buffer.append(",C_NCHAR_50        NCHAR(50)      ");
      buffer.append(",C_VARCHAR_50      VARCHAR(50)   ");
      buffer.append(",C_NVARCHAR_50     NVARCHAR(50)   ");
      buffer.append(",C_BINARY_1       CHAR FOR BIT DATA        ");
      buffer.append(",C_BINARY_20      CHAR(20)  FOR BIT DATA   ");
      buffer.append(",C_VARBINARY_20   VARCHAR(20) FOR BIT DATA ");
      buffer.append(",C_DATE            DATE          ");
      buffer.append(",C_TIME            TIME          ");
      buffer.append(",C_TIMESTAMP       TIMESTAMP     ");
      if (areLobsSupported()) {
        buffer.append(",C_BLOB         BLOB          ");
        buffer.append(",C_CLOB         CLOB          ");
        buffer.append(",C_NCLOB        NCLOB          ");
        buffer.append(",C_DBCLOB       CLOB        "); // ??? should be DBCLOB

        // We do not test updating datalinks, since it is not
        // possible to open a updatable cursor/result set with
        // a datalink column.
        // buffer.append (",C_DATALINK DATALINK ");
        buffer.append(",C_DISTINCT " + COLLECTION + ".SSN ");
      }
      if (areBigintsSupported()) // @D0A
        buffer.append(",C_BIGINT       BIGINT"); // @D0A
      else // @D0A
        buffer.append(",C_BIGINT       INTEGER"); // @D0A
      if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
        buffer.append(", C_ROWID       ROWID generated by default"); // @pda
                                                                     // jdbc40
        buffer.append(", C_VARBINARY_40    VARBINARY(40)");
      } else {
        buffer.append(", C_ROWID       VARCHAR(40) FOR BIT DATA");
        buffer.append(", C_VARBINARY_40    VARCHAR(40) FOR BIT DATA");
      }
      if (areBooleansSupported()) 
        buffer.append(",C_BOOLEAN       BOOLEAN"); 
      else 
        buffer.append(",C_BOOLEAN       INTEGER"); 
        
      buffer.append(")");

      tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_UPDATE, buffer.toString(),
          errorBuffer);
     

      // Make use the table is usable with a transactional connection
      Connection c = getConnection(getBaseURL(), systemObject_.getUserId(),
          encryptedPassword_, "INFO_JDRSTEST_SETUP2_TRANS");
      try {
        c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        c.setAutoCommit(false);
      } catch (Exception e) {
        System.out
            .println("Warning:  Exception thrown changing isolation level");
        System.err.flush();
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
        System.out.flush();
      }
      Statement stmt = c.createStatement();
      stmt.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE
          + " (C_KEY) VALUES ('DUMMY_ROW')");
      stmt.executeUpdate("DELETE FROM  " + JDRSTest.RSTEST_UPDATE);
      stmt.close();
      c.commit();
      c.close();

      try {
        connection_.commit(); // needed for XA testing
      } catch (Exception e) {
        System.out.println("Exception calling commit");
        if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_SYSTEMI) {
          System.out.flush();
          e.printStackTrace();
          System.err.flush();

        } else {
          System.out.println("Ignoring for LUW");
        }
      }

      //
      // Setup the table for scroll test. A table with 97 rows will be created
      //

      buffer.setLength(0);
      buffer.append(" ( ROWNUMBER INTEGER ");
      buffer.append(",C_INTEGER         INTEGER       ");
      buffer.append(",C_VARCHAR_50      VARCHAR(50)   ");
      buffer.append(",C_NVARCHAR_50     NVARCHAR(50)  ) ");

      tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_SCROLL, buffer.toString(),
          errorBuffer);
      

      if (tableCreated) {
        for (int i = 0; i < 97; i++) {
          statement_.executeUpdate("INSERT INTO " + RSTEST_SCROLL
              + " ( ROWNUMBER, C_INTEGER, C_VARCHAR_50, C_NVARCHAR_50 )" // @D0A
              + " VALUES ( " + (i + 1) + " ," + (i + 1)
              + " , 'Hi Mom', 'Hi Mom')");
        }
      }

      // G1A
      // Setup the table for sensitivity test. A table with 97 rows will be
      // created
      //

      buffer.setLength(0);
      buffer.append(" ( C_INTEGER         INTEGER  ) ");

      tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_SENSITIVE, buffer.toString(),
          errorBuffer);

      if (tableCreated) {
        for (int i = 0; i < 97; i++) {
          statement_.executeUpdate("INSERT INTO " + RSTEST_SENSITIVE
              + " ( C_INTEGER )" + " VALUES ( " + (i + 1) + ")");
        }
      }

      //
      // Setup tables for DECFLOAT
      //

      if (true) {
        if (getDatabaseType() == JDTestDriver.DB_ZOS
            || getDatabaseType() == JDTestDriver.DB_LUW) {
          createAndPopulateTable(RSTEST_DFP16, "C1 DECFLOAT(16)", VALUES_DFP16);
          createAndPopulateTable(RSTEST_DFP16NAN, "C1 DECFLOAT(16)", "NAN");
          createAndPopulateTable(RSTEST_DFP16INF, "C1 DECFLOAT(16)", "INF");
          createAndPopulateTable(RSTEST_DFP16NNAN, "C1 DECFLOAT(16)", "-NAN");
          createAndPopulateTable(RSTEST_DFP16NINF, "C1 DECFLOAT(16)", "-INF");
          createAndPopulateTable(RSTEST_DFP34, "C1 DECFLOAT(34)",
              VALUES_DFP34_LUWSET);
          createAndPopulateTable(RSTEST_DFP34NAN, "C1 DECFLOAT(34)", "NAN");
          createAndPopulateTable(RSTEST_DFP34INF, "C1 DECFLOAT(34)", "INF");
          createAndPopulateTable(RSTEST_DFP34NNAN, "C1 DECFLOAT(34)", "-NAN");
          createAndPopulateTable(RSTEST_DFP34NINF, "C1 DECFLOAT(34)", "-INF");
        } else {
          createAndPopulateTable(RSTEST_DFP16, "C1 DECFLOAT(16)", VALUES_DFP16);
          createAndPopulateTable(RSTEST_DFP16NAN, "C1 DECFLOAT(16)", "'NAN'");
          createAndPopulateTable(RSTEST_DFP16INF, "C1 DECFLOAT(16)", "'INF'");
          createAndPopulateTable(RSTEST_DFP16NNAN, "C1 DECFLOAT(16)", "'-NAN'");
          createAndPopulateTable(RSTEST_DFP16NINF, "C1 DECFLOAT(16)", "'-INF'");
          createAndPopulateTable(RSTEST_DFP34, "C1 DECFLOAT(34)", VALUES_DFP34);
          createAndPopulateTable(RSTEST_DFP34NAN, "C1 DECFLOAT(34)", "'NAN'");
          createAndPopulateTable(RSTEST_DFP34INF, "C1 DECFLOAT(34)", "'INF'");
          createAndPopulateTable(RSTEST_DFP34NNAN, "C1 DECFLOAT(34)", "'-NAN'");
          createAndPopulateTable(RSTEST_DFP34NINF, "C1 DECFLOAT(34)", "'-INF'");

        }
      }

      // Setup RSTEST_GETXML table.
      // in release V5R3 binary and varbinary will be their own data type in DB2
      if (true) {

        buffer.setLength(0);
        buffer.append("( C_KEY               INT ");
        if (getDatabaseType() == JDTestDriver.DB_ZOS
            || getDatabaseType() == JDTestDriver.DB_LUW) {
          buffer.append(", C_CLOB0037           CLOB(1M)    ");
          buffer.append(", C_CLOB0937           CLOB(1M)  ");
          buffer.append(", C_CLOB1208           CLOB(1M)  ");
          buffer.append(", C_DBCLOB1200         DBCLOB(1M)  ");
        } else {
          buffer.append(", C_CLOB0037           CLOB(1M) CCSID 37   ");
          buffer.append(", C_CLOB0937           CLOB(1M) CCSID 0937 ");
          buffer.append(", C_CLOB1208           CLOB(1M) CCSID 1208 ");
          buffer.append(", C_DBCLOB1200         DBCLOB(1M) CCSID 1200 ");
        }
        buffer.append(", C_BLOB0037           BLOB(1M)              ");
        buffer.append(", C_BLOB1208           BLOB(1M)              ");
        buffer.append(", C_BLOB1200           BLOB(1M)              ");
        if (true) {
          buffer.append(", C_XML              XML              ");
        }
        buffer.append(")");
        tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_GETXML, buffer.toString(),
            errorBuffer);
        if (tableCreated) {
          // Key == BINARY_NOTRANS.
          try {
            PreparedStatement ps5;
            ps5 = connection_.prepareStatement("INSERT INTO " + RSTEST_GETXML
                + " VALUES(?,    ?, ?, ?,   ?,   ?, ?, ?, ?) ");
            ps5.setInt(1, 1);
            ps5.setString(2, SAMPLE_XML1);
            ps5.setString(3, SAMPLE_XML1);
            ps5.setString(4, SAMPLE_XML1);
            ps5.setString(5, SAMPLE_XML1);
            ps5.setBytes(6, SAMPLE_XML1_BLOB37);
            ps5.setBytes(7, SAMPLE_XML1_BLOB1208);
            ps5.setBytes(8, SAMPLE_XML1_BLOB1200);
            if (true) {
              ps5.setString(9, SAMPLE_XML1);
            }
            ps5.executeUpdate();

            for (int i = 1; i < VALUES_XML.length; i++) {
              ps5.setInt(1, (i + 1));
              if (true) {
                ps5.setString(9, VALUES_XML[i]);
              }

              ps5.executeUpdate();
            }
            ps5.setInt(1, 4);
            ps5.setString(2, null);
            ps5.setString(3, null);
            ps5.setString(4, null);
            ps5.setString(5, null);
            ps5.setBytes(6, null);
            ps5.setBytes(7, null);
            ps5.setBytes(8, null);
            if (true) {
              ps5.setBytes(9, null);
            }
            ps5.executeUpdate();

            ps5.close();
          } catch (SQLException e) {
            System.out.flush();
            System.err.flush();
            System.out.println("Unable to setup row " + RSTEST_GETXML);
            System.out.flush();
            e.printStackTrace();
            System.err.flush();
          }

        } /* tableCreated */

        buffer.setLength(0);
        buffer.append("( C_KEY               INT ");
        if (getDatabaseType() == JDTestDriver.DB_ZOS
            || getDatabaseType() == JDTestDriver.DB_LUW) {
          buffer.append(", C_CLOB0037           CLOB(1M)    ");
          buffer.append(", C_CLOB0937           CLOB(1M)  ");
          buffer.append(", C_CLOB1208           CLOB(1M)  ");
          buffer.append(", C_DBCLOB1200         DBCLOB(1M)  ");
        } else {
          buffer.append(", C_CLOB0037           CLOB(1M) CCSID 37   ");
          buffer.append(", C_CLOB0937           CLOB(1M) CCSID 0937 ");
          buffer.append(", C_CLOB1208           CLOB(1M) CCSID 1208 ");
          buffer.append(", C_DBCLOB1200         DBCLOB(1M) CCSID 1200 ");
        }
        buffer.append(", C_BLOB0037           BLOB(1M)              ");
        buffer.append(", C_BLOB1208           BLOB(1M)              ");
        buffer.append(", C_BLOB1200           BLOB(1M)              ");
        buffer.append(")");
        tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_UPDATEXML, buffer.toString(),
            errorBuffer);
        if (tableCreated) {
          // Key == BINARY_NOTRANS.
          try {
            PreparedStatement ps5 = connection_.prepareStatement("INSERT INTO "
                + RSTEST_UPDATEXML + " VALUES(?,    ?, ?, ?,   ?,   ?, ?, ?) ");

            ps5.setInt(1, 1);
            ps5.setString(2, SAMPLE_XML1);
            ps5.setString(3, SAMPLE_XML1);
            ps5.setString(4, SAMPLE_XML1);
            ps5.setString(5, SAMPLE_XML1);
            ps5.setBytes(6, SAMPLE_XML1_BLOB37);
            ps5.setBytes(7, SAMPLE_XML1_BLOB1208);
            ps5.setBytes(8, SAMPLE_XML1_BLOB1200);
            ps5.executeUpdate();

            ps5.setInt(1, 2);
            ps5.executeUpdate();

            ps5.setInt(1, 3);
            ps5.executeUpdate();

            ps5.setInt(1, 4);
            ps5.setString(2, null);
            ps5.setString(3, null);
            ps5.setString(4, null);
            ps5.setString(5, null);
            ps5.setBytes(6, null);
            ps5.setBytes(7, null);
            ps5.setBytes(8, null);
            ps5.executeUpdate();

            ps5.close();
          } catch (SQLException e) {

            System.out.flush();
            e.printStackTrace();
            System.err.flush();
            System.out.println("Unable to setup row for " + RSTEST_UPDATEXML);
            System.out.println("Error buffer = " + errorBuffer.toString());
          }

        } /* tableCreated */

      } /* V5R4 */

      // Setup RSTEST_BOOLEAn
      if (getRelease() >= JDTestDriver.RELEASE_V7R5M0) {
        buffer.setLength(0);
        buffer.append(" (C_KEY           VARCHAR(20)   ");
        buffer.append(",C_BOOLEAN        BOOLEAN        ");
        buffer.append(")");
        tableCreated = JDTestDriver.createTableIfNeeded(statement_, RSTEST_BOOLEAN, buffer.toString(),
            errorBuffer);
        if (tableCreated) {
          // Key == BINARY_NOTRANS.
          PreparedStatement ps5 = connection_.prepareStatement("INSERT INTO "
              + RSTEST_BOOLEAN + " (C_KEY, C_BOOLEAN) VALUES (?, ?)");
          ps5.setString(1, "BOOLEAN_TRUE");
          try { 
          ps5.setBoolean(2, true);
          } catch (SQLException e) { 
            byte [] stuff = {(byte) 0xF1};
            ps5.setBytes(2, stuff );
          }
          ps5.executeUpdate();

          ps5.setString(1, "BOOLEAN_FALSE");
          try {
          ps5.setBoolean(2, false);
          } catch (SQLException e) { 
            byte [] stuff = {(byte) 0xF0};
            ps5.setBytes(2, stuff );
          }
        
          ps5.executeUpdate();

          ps5.setString(1, "BOOLEAN_NULL");
          ps5.setNull(2, Types.BOOLEAN);
          ps5.executeUpdate();

          ps5.close();

        } // tableCreated RSTEST_BOOLEAN
      } /* V7R5 or later */

      connection_.commit();

    } catch (Exception e) {
      System.out.flush();
      System.err.flush();
      System.out.println("***************************************************");
      System.out.println("*  Unexpected exception in setup.. Please correct  ");
      System.out.println("***************************************************");
      System.out.flush();
      e.printStackTrace();
      JDTestcase.checkInUse(e, statement_);
      System.err.flush();
      System.out.flush();

    }
  } /* setup */

  public void createAndPopulateTable(String tableName, String columnDefinition,
      String value) throws SQLException {

    createAndPopulateTable(tableName, columnDefinition, value, statement_);
  }

  public static void createAndPopulateTable(String tableName,
      String columnDefinition, String value, Statement statementParm)
      throws SQLException {
    StringBuffer errorBuffer = new StringBuffer();

    try {
      boolean tableCreated = JDTestDriver.createTableIfNeeded(statementParm, tableName,
          " (" + columnDefinition + ")", errorBuffer);
      if (tableCreated) { 
        statementParm
          .executeUpdate("INSERT INTO " + tableName + " VALUES(" + value + ")");
      } 
    } catch (Exception e) {
      System.out.println("Unable to insert '" + value + "' into " + tableName);
      System.out.flush();
      e.printStackTrace();
      System.err.flush();
      System.out.println("Error buffer: " + errorBuffer.toString());

    }

  }

  public void createAndPopulateTable(String tableName, String columnDefinition,
      String values[]) throws SQLException {

    createAndPopulateTable(tableName, columnDefinition, values, statement_);
  }

  public static void createAndPopulateTable(String tableName,
      String columnDefinition, String values[], Statement statementParm)
      throws SQLException {

    boolean tableCreated;
    StringBuffer errorBuffer = new StringBuffer();
    try {
      tableCreated = JDTestDriver.createTableIfNeeded(statementParm, tableName,
          " (" + columnDefinition + ")", errorBuffer);

      if (tableCreated) {
        for (int i = 0; i < values.length; i++) {
          String sql;
          if (getDatabaseTypeStatic() == JDTestDriver.DB_ZOS
              || getDatabaseTypeStatic() == JDTestDriver.DB_LUW) {
            sql = "INSERT INTO " + tableName + " VALUES(" + values[i] + ")";
          } else {
            sql = "INSERT INTO " + tableName + " VALUES('" + values[i] + "')";
          }
          try {
            statementParm.executeUpdate(sql);
          } catch (Exception e) {
            System.out.println("Error executing " + sql);
            System.out.flush();
            e.printStackTrace();
            System.err.flush();

          }
        }
      }
    } catch (SQLException e) {
      System.out.println("Exception caught " + e.toString() + " ");
      System.out.println(errorBuffer.toString());
      System.out.println("Rethrowing error");
      throw e;
    }

  }

  /**
   * Cleanup - - this does not run automatically - - it is called by JDCleanup.
   **/
  public static void dropCollections(Connection c) {

    try {
      // Make sure the system reply list is set so that reply
      // message will be sent when dropping the collection.
      Statement stmt = c.createStatement();
      stmt.executeUpdate(
          "CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)       ', 0000000030.00000)");
    } catch (Exception e) {
      System.out.flush();
      e.printStackTrace();
      System.err.flush();

    }

    dropCollection(c, COLLECTION);
  }

  /**
   * Positions the cursor on a particular row and return the result set.
   * 
   * @param rs
   *          The result set.
   * @param key
   *          The key. If null, positions to before the first row.
   * 
   * @exception SQLException
   *              If an exception occurs.
   **/
  public static void position(ResultSet rs, String key) throws SQLException {
    rs.beforeFirst();
    position0(rs, key);
  }

  /**
   * Positions the cursor, reopening the result set if running to LUW
   */
  public static ResultSet position(ResultSet rs, Statement s, String query,
      String key) throws SQLException {
    rs = s.executeQuery(query);

    position0(rs, key);
    return rs;
  }

  /**
   * Positions the cursor, reopening the result set if needed for the driver
   */
  public static ResultSet position(int driver, Statement s, String query,
      ResultSet rs, String key) throws SQLException {
    if (driver == JDTestDriver.DRIVER_JTOPENLITE) {
      rs = s.executeQuery(query);
    } else {
      rs.beforeFirst();
    }

    position0(rs, key);
    return rs;
  }

  /**
   * Positions the cursor on a particular row and return the result set.
   * 
   * @param rs
   *          The result set.
   * @param key
   *          The key. If null, positions to before the first row.
   * 
   * @exception SQLException
   *              If an exception occurs.
   **/
  public static void position0(ResultSet rs, String key) throws SQLException {
    if (key != null) {
      while (rs.next()) {
        String s = rs.getString("C_KEY");
        if (s != null)
          if (s.equals(key))
            return;
      }

      System.out.println("Warning: Key " + key + " not found.");
      try {
        rs.beforeFirst();
        System.out.println("  valid keys are ... ");
        while (rs.next()) {
          String s = rs.getString("C_KEY");
          System.out.println("    '" + s + "'");
        }
      } catch (Exception e) {
        System.out
            .println("Warning:  Unable to rs.beforeFirst() to get valid keys");
      }
    }
    return;
  }

  /**
   * Performs setup needed after running testcases.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
    /* Only cleanup if there are no failures */
    /* And no other concurrent tests are running */
    boolean doCleanup = true; 
    if (!parallelCounter_.doCleanup()) {
      out_.println("JDRTest.cleanup not cleaning other parallel tests running ="+parallelCounter_.toString()); 
      doCleanup = false; 
    }
    if (doCleanup) {
      if (totalFail_ > 0) {
        out_.println("JDRTest.cleanup not cleaning because failures > 0"); 
        doCleanup = false; 
      }
      if (doCleanup) { 
        /* Do not cleanup if files created within last day */ 
        if (newerFile(statement_, RSTEST_GET)) { 
          out_.println("JDRTest.cleanup not cleaning because "+RSTEST_GET +" is newer file"); 
          doCleanup = false;
        }
      }
      if (doCleanup) {
        boolean dropUDTfailed = false;
        if (areLobsSupported()) {
          cleanupTable(statement_, RSTEST_GETDL);
        }
        cleanupTable(statement_, RSTEST_GET);

        cleanupTable(statement_, RSTEST_GETX);

        cleanupTable(statement_, RSTEST_POS);
        if (true) {
          cleanupTable(statement_, RSTEST_BINARY);
          cleanupTable(statement_, RSTEST_GRAPHIC);
        }
        cleanupTable(statement_, RSTEST_UPDATE);
        cleanupTable(statement_, RSTEST_SCROLL);
        cleanupTable(statement_, RSTEST_SENSITIVE); // @G1A

        if (areLobsSupported()) {

          statement_.executeUpdate("DROP DISTINCT TYPE " + COLLECTION + ".SSN");
        }

        if (true) {
          cleanupTable(statement_, RSTEST_GETXML);
          cleanupTable(statement_, RSTEST_UPDATEXML);

        }

        statement_.close();

        if (dropUDTfailed) {
          System.out.println("Deleting collection since drop UDT failed");
          dropCollections(connection_);
        }
      }
    }
    parallelCounter_.close(); 
    connection_.close();
  }

  /**
   * Creates the testcases.
   **/
  public void createTestcases2() {

    if (TestDriverStatic.pause_) {
      try {
        systemObject_.connectService(AS400.DATABASE);
      } catch (AS400SecurityException e) {
        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      } catch (IOException e) {

        System.out.flush();
        e.printStackTrace();
        System.err.flush();
      }

      try {
        Job[] jobs = systemObject_.getJobs(AS400.DATABASE);
        System.out.println("Host Server job(s): ");

        for (int i = 0; i < jobs.length; i++) {
          System.out.println(jobs[i]);
        }
      } catch (Exception exc) {
      }

      try {
        System.out.println("Toolbox is paused. Press ENTER to continue.");
        System.in.read();
      } catch (Exception exc) {
      }
      ;
    }

    /*
     * 2017/09/15 Ordered alphabetically to prevent update test from causing GET
     * tests to fail
     */

    if (getDriver() != JDTestDriver.DRIVER_JCC) {
      addTestcase(new JDRowSetRSTestcase(systemObject_, namesAndVars_, runMode_,
          fileOutputStream_,  password_, pwrSysUserID_,
          pwrSysPassword_));
    }

    addTestcase(new JDRSAbsolute(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSAfterLast(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSBeforeFirst(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    if (getDriver() != JDTestDriver.DRIVER_JCC) {
      addTestcase(new JDRSCachedRowSet(systemObject_, namesAndVars_, runMode_,
          fileOutputStream_,  password_));
    }
    addTestcase(new JDRSCursorScroll(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSCursorScrollInsensitiveFromCall(systemObject_,
        namesAndVars_, runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSCursorScrollSensitiveFromCall(systemObject_,
        namesAndVars_, runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSCursorSensitivity(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSDataCompression(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSDeleteRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSFetchSize(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSFindColumn(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSFirst(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetArray(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetAsciiStream(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetBigDecimal(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetBinaryStream(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetBlob(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetBoolean(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetByte(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetBytes(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetBytesBinary(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetCharacterStream(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSGetClob(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetDate(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetDouble(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetFloat(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetInt(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetLong(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetNCharacterStream(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSGetNClob(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetNString(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetObject(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetObject41(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetRef(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetRowId(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetSQLXML(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetShort(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetString(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetStringBIDI(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetStringMixed(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetStringUnsupported(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSGetTime(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetTimestamp(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetURL(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSGetUnicodeStream(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSInsertRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSLast(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSMisc(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSMoveToCurrentRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSMoveToInsertRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSNext(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSPrevious(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSRefreshRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSRelative(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateAsciiStream(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateBigDecimal(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateBinaryStream(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateBoolean(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateByte(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateBytes(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateCharacterStream(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateClob(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateDB2Default(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateDBDefault(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateDate(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateDouble(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateFloat(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateInt(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateLong(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateNCharacterStream(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateNClob(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateNString(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateNull(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateObject(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateObjectSQLType(systemObject_, namesAndVars_,
        runMode_, fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateRow(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateRowId(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateSQLXML(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateShort(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateString(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateTime(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSUpdateTimestamp(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSWarnings(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSWasNull(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));
    addTestcase(new JDRSWrapper(systemObject_, namesAndVars_, runMode_,
        fileOutputStream_,  password_));

  }
}
