///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFRecordMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RF;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.DBCSEitherFieldDescription;
import com.ibm.as400.access.DBCSGraphicFieldDescription;
import com.ibm.as400.access.DBCSOnlyFieldDescription;
import com.ibm.as400.access.DBCSOpenFieldDescription;
import com.ibm.as400.access.DateFieldDescription;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.HexFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.TimestampFieldDescription;
import com.ibm.as400.access.VariableLengthFieldDescription;
import com.ibm.as400.access.ZonedDecimalFieldDescription;

import test.Testcase;

/**
  Testcase RFRecordMisc. This test class verifed valid and invalid usage of
  all methods in Record (with the exception of the constructors which are
  tested in RFRecord.java).
**/
public class RFRecordMisc extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RFRecordMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RFTest.main(newArgs); 
   }
  Record rec;       // Record for use in the variations
  // Contains the Java objects to use to create
  // the AS/400 data for contents byte array.
  Object[] jtArray = {Integer.valueOf(5),
                      "Field2",
                      "01/01/97",
                      "Field3",
                      "Field four",
                      "Field five",
                      "Field six",
                      Double.valueOf(999.111),
                      new byte[3],
                      new BigDecimal("1234567890.01234"),
                      "01:01:01",
                      "01/01/97.01:01:01",
                      new BigDecimal("12345.12345")};

  // String to check toString () with
  String contentsAsString = "5 Field2     01/01/97 Field3     Field four Field five Field six  999.111 " + "\0\0\0 " +
                             "1234567890.01234 01:01:01 01/01/97.01:01:01 12345.12345";
  String contentsAsStringWithNull = "null Field2     01/01/97 Field3     Field four Field five Field six  999.111 " + "\0\0\0 " +
                             "1234567890.01234 01:01:01 01/01/97.01:01:01 12345.12345";

  String defaultContents = "0       0.0  0   0";

  // All of the following values are set by the setup() method
  RecordFormat r;   // Record format for the variations
  byte[] contents;  // Byte array with which to populate the record
  int recLength;    // Length that the record should be
  int numFields;    // Number of fields that the record should have

  /**
  Constructor.  This is called from the RFTest constructor.
  **/
  public RFRecordMisc(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    super(systemObject, "RFRecordMisc", 90,
          variationsToRun, runMode, fileOutputStream);
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Run setup.  This will create the RecordFormat object for the
    // variations and initialize the byte array which will be used to
    // populate the record.
    try
    {
      setup();
    }
    catch (Exception e)
    {
      output_.println("Unable to complete setup");
      return;
    }

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }

    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }

    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }

    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }

    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }

    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }

    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }

    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }

    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }

    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }

    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }

    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }

    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }

    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }

    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }

    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }

    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }

    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }

    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }

    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }

    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }

    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }

    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }

    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }

    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }

    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }

    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }

    if ((allVariations || variationsToRun_.contains("35")) &&
        runMode_ != ATTENDED)
    {
      setVariation(35);
      Var035();
    }

    if ((allVariations || variationsToRun_.contains("36")) &&
        runMode_ != ATTENDED)
    {
      setVariation(36);
      Var036();
    }

    if ((allVariations || variationsToRun_.contains("37")) &&
        runMode_ != ATTENDED)
    {
      setVariation(37);
      Var037();
    }

    if ((allVariations || variationsToRun_.contains("38")) &&
        runMode_ != ATTENDED)
    {
      setVariation(38);
      Var038();
    }

    if ((allVariations || variationsToRun_.contains("39")) &&
        runMode_ != ATTENDED)
    {
      setVariation(39);
      Var039();
    }

    if ((allVariations || variationsToRun_.contains("40")) &&
        runMode_ != ATTENDED)
    {
      setVariation(40);
      Var040();
    }

    if ((allVariations || variationsToRun_.contains("41")) &&
        runMode_ != ATTENDED)
    {
      setVariation(41);
      Var041();
    }

    if ((allVariations || variationsToRun_.contains("42")) &&
        runMode_ != ATTENDED)
    {
      setVariation(42);
      Var042();
    }

    if ((allVariations || variationsToRun_.contains("43")) &&
        runMode_ != ATTENDED)
    {
      setVariation(43);
      Var043();
    }

    if ((allVariations || variationsToRun_.contains("44")) &&
        runMode_ != ATTENDED)
    {
      setVariation(44);
      Var044();
    }

    if ((allVariations || variationsToRun_.contains("45")) &&
        runMode_ != ATTENDED)
    {
      setVariation(45);
      Var045();
    }

    if ((allVariations || variationsToRun_.contains("46")) &&
        runMode_ != ATTENDED)
    {
      setVariation(46);
      Var046();
    }

    if ((allVariations || variationsToRun_.contains("47")) &&
        runMode_ != ATTENDED)
    {
      setVariation(47);
      Var047();
    }

    if ((allVariations || variationsToRun_.contains("48")) &&
        runMode_ != ATTENDED)
    {
      setVariation(48);
      Var048();
    }

    if ((allVariations || variationsToRun_.contains("49")) &&
        runMode_ != ATTENDED)
    {
      setVariation(49);
      Var049();
    }

    if ((allVariations || variationsToRun_.contains("50")) &&
        runMode_ != ATTENDED)
    {
      setVariation(50);
      Var050();
    }

    if ((allVariations || variationsToRun_.contains("51")) &&
        runMode_ != ATTENDED)
    {
      setVariation(51);
      Var051();
    }

    if ((allVariations || variationsToRun_.contains("52")) &&
        runMode_ != ATTENDED)
    {
      setVariation(52);
      Var052();
    }

    if ((allVariations || variationsToRun_.contains("53")) &&
        runMode_ != ATTENDED)
    {
      setVariation(53);
      Var053();
    }

    if ((allVariations || variationsToRun_.contains("54")) &&
        runMode_ != ATTENDED)
    {
      setVariation(54);
      Var054();
    }

    if ((allVariations || variationsToRun_.contains("55")) &&
        runMode_ != ATTENDED)
    {
      setVariation(55);
      Var055();
    }

    if ((allVariations || variationsToRun_.contains("56")) &&
        runMode_ != ATTENDED)
    {
      setVariation(56);
      Var056();
    }

    if ((allVariations || variationsToRun_.contains("57")) &&
        runMode_ != ATTENDED)
    {
      setVariation(57);
      Var057();
    }

    if ((allVariations || variationsToRun_.contains("58")) &&
        runMode_ != ATTENDED)
    {
      setVariation(58);
      Var058();
    }

    if ((allVariations || variationsToRun_.contains("59")) &&
        runMode_ != ATTENDED)
    {
      setVariation(59);
      Var059();
    }

    if ((allVariations || variationsToRun_.contains("60")) &&
        runMode_ != ATTENDED)
    {
      setVariation(60);
      Var060();
    }

    if ((allVariations || variationsToRun_.contains("61")) &&
        runMode_ != ATTENDED)
    {
      setVariation(61);
      Var061();
    }

    if ((allVariations || variationsToRun_.contains("62")) &&
        runMode_ != ATTENDED)
    {
      setVariation(62);
      Var062();
    }

    if ((allVariations || variationsToRun_.contains("63")) &&
        runMode_ != ATTENDED)
    {
      setVariation(63);
      Var063();
    }

    if ((allVariations || variationsToRun_.contains("64")) &&
        runMode_ != ATTENDED)
    {
      setVariation(64);
      Var064();
    }

    if ((allVariations || variationsToRun_.contains("65")) &&
        runMode_ != ATTENDED)
    {
      setVariation(65);
      Var065();
    }

    if ((allVariations || variationsToRun_.contains("66")) &&
        runMode_ != ATTENDED)
    {
      setVariation(66);
      Var066();
    }

    if ((allVariations || variationsToRun_.contains("67")) &&
        runMode_ != ATTENDED)
    {
      setVariation(67);
      Var067();
    }

    if ((allVariations || variationsToRun_.contains("68")) &&
        runMode_ != ATTENDED)
    {
      setVariation(68);
      Var068();
    }

    if ((allVariations || variationsToRun_.contains("69")) &&
        runMode_ != ATTENDED)
    {
      setVariation(69);
      Var069();
    }

    if ((allVariations || variationsToRun_.contains("70")) &&
        runMode_ != ATTENDED)
    {
      setVariation(70);
      Var070();
    }

    if ((allVariations || variationsToRun_.contains("71")) &&
        runMode_ != ATTENDED)
    {
      setVariation(71);
      Var071();
    }

    if ((allVariations || variationsToRun_.contains("72")) &&
        runMode_ != ATTENDED)
    {
      setVariation(72);
      Var072();
    }

    if ((allVariations || variationsToRun_.contains("73")) &&
        runMode_ != ATTENDED)
    {
      setVariation(73);
      Var073();
    }

    if ((allVariations || variationsToRun_.contains("74")) &&
        runMode_ != ATTENDED)
    {
      setVariation(74);
      Var074();
    }

    if ((allVariations || variationsToRun_.contains("75")) &&
        runMode_ != ATTENDED)
    {
      setVariation(75);
      Var075();
    }

    if ((allVariations || variationsToRun_.contains("76")) &&
        runMode_ != ATTENDED)
    {
      setVariation(76);
      Var076();
    }

    if ((allVariations || variationsToRun_.contains("77")) &&
        runMode_ != ATTENDED)
    {
      setVariation(77);
      Var077();
    }

    if ((allVariations || variationsToRun_.contains("78")) &&
        runMode_ != ATTENDED)
    {
      setVariation(78);
      Var078();
    }

    if ((allVariations || variationsToRun_.contains("79")) &&
        runMode_ != ATTENDED)
    {
      setVariation(79);
      Var079();
    }

    if ((allVariations || variationsToRun_.contains("80")) &&
        runMode_ != ATTENDED)
    {
      setVariation(80);
      Var080();
    }

    if ((allVariations || variationsToRun_.contains("81")) &&
        runMode_ != ATTENDED)
    {
      setVariation(81);
      Var081();
    }

    if ((allVariations || variationsToRun_.contains("82")) &&
        runMode_ != ATTENDED)
    {
      setVariation(82);
      Var082();
    }

    if ((allVariations || variationsToRun_.contains("83")) &&
        runMode_ != ATTENDED)
    {
      setVariation(83);
      Var083();
    }

    if ((allVariations || variationsToRun_.contains("84")) &&
        runMode_ != ATTENDED)
    {
      setVariation(84);
      Var084();
    }

    if ((allVariations || variationsToRun_.contains("85")) &&
        runMode_ != ATTENDED)
    {
      setVariation(85);
      Var085();
    }

    if ((allVariations || variationsToRun_.contains("86")) &&
        runMode_ != ATTENDED)
    {
      setVariation(86);
      Var086();
    }

    if ((allVariations || variationsToRun_.contains("87")) &&
        runMode_ != ATTENDED)
    {
      setVariation(87);
      Var087();
    }

    if ((allVariations || variationsToRun_.contains("88")) &&
        runMode_ != ATTENDED)
    {
      setVariation(88);
      Var088();
    }

    if ((allVariations || variationsToRun_.contains("89")) &&
        runMode_ != ATTENDED)
    {
      setVariation(89);
      Var089();
    }

    if ((allVariations || variationsToRun_.contains("90")) &&
        runMode_ != ATTENDED)
    {
      setVariation(90);
      Var090();
    }
  }


  /**
   *Verify valid usage of getContents().
   *<ul compact>
   *<li>Record format has dependent fields, no set contents has been done.
   *<li>Test the following types of record formats:
   *<ul compact>
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The contents returned should be a byte array of AS/400 data initialized
   *to the appropriate default values for each field in the record format.
   *<li>The length of the byte array returned should be exactly the length
   *of an expected record of data.  E.g. if record format contains variable
   *length fields, there will be two extra bytes of data for each variable
   *length field.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      // Get a new record
      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      if (r1.getRecordLength() != expectedLength1)
      {
        failed("r1.getRecordLength: " + String.valueOf(r1.getRecordLength()) + ", expected: " + String.valueOf(expectedLength1));
      }
      else if (r2.getRecordLength() != expectedLength2)
      {
        failed("r2.getRecordLength: " + String.valueOf(r2.getRecordLength()) + ", expected: " + String.valueOf(expectedLength2));
      }
      else
      {
        byte[] b1 = r1.getContents();
        byte[] b2 = r2.getContents();
        if (bin4.toInt(b1,0) != 0 || bin2.toShort(b1,14) != 0 ||
            !((String)txt10.toObject(b1, 4)).equals("          ") ||
            !((String)txt50.toObject(b1,16)).equals("                                                  "))
        {
          output_.println(bin4.toInt(b1,0));
          output_.println(((String)txt10.toObject(b1, 4)) + ".");
          output_.println(bin2.toShort(b1,14));
          output_.println(((String)txt50.toObject(b1, 16)) + ".");
          failed("Record 1 does not contain correct values.");
        }
        else if (bin4.toInt(b2,0) != 0 || bin2.toShort(b2,14) != 0 ||
            !((String)txt10.toObject(b2, 4)).equals("          ") ||
            !((String)txt50.toObject(b2,18)).equals("                                                  "))
        {
          output_.println(bin4.toInt(b2,0));
          output_.println(((String)txt10.toObject(b2, 4)) + ".");
          output_.println(bin2.toShort(b2,14));
          output_.println(((String)txt50.toObject(b2, 16)) + ".");
          failed("Record 2 does not contain correct values.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getContents().
   *<ul compact>
   *<li>Record format has dependent fields, set contents has been done via
   *constructor.
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The contents returned should be a byte array of AS/400 data initialized
   *to the contents of the byte array specified on the constructor.
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 5;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
      rf2.setLengthDependency("VARLEN", "NEXTLEN");
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1 + 45];
      byte[] c2 = new byte[expectedLength2 + 45];
      bin4.toBytes(0, c1);
      bin4.toBytes(0,c2);
      txt10.toBytes("          ",c1,4);
      txt10.toBytes("          ",c2,4);
      bin2.toBytes((short)5, c1,14);
      bin2.toBytes((short)5,c2,14);
      txt50.toBytes("12345",c1,16);
      bin2.toBytes((short)10, c2, 16);
      txt50.toBytes("5432112345",c2,18);

      // Get a new record
      Record r1 = new Record(rf1, c1);
      Record r2 = new Record(rf2, c2);
      if (r1.getRecordLength() != expectedLength1)
      {
        failed("r1.getRecordLength: " + String.valueOf(r1.getRecordLength()) + ", expected: " + String.valueOf(expectedLength1));
      }
      else if (r2.getRecordLength() != expectedLength2)
      {
        failed("r2.getRecordLength: " + String.valueOf(r2.getRecordLength()) + ", expected: " + String.valueOf(expectedLength2));
      }
      else
      {
        byte[] b1 = r1.getContents();
        byte[] b2 = r2.getContents();
        if (bin4.toInt(b1,0) != 0 || bin2.toShort(b1,14) != 5 ||
            !((String)txt10.toObject(b1, 4)).equals("          ") ||
            !((String)new AS400Text(5,37, systemObject_).toObject(b1,16)).equals("12345"))
        {
          output_.println(bin4.toInt(b1,0));
          output_.println(((String)txt10.toObject(b1, 4)) + ".");
          output_.println(bin2.toShort(b1,14));
          output_.println(((String)new AS400Text(5,37, systemObject_).toObject(b1, 16)) + ".");
          failed("Record 1 does not contain correct values.");
        }
        else if (bin4.toInt(b2,0) != 0 || bin2.toShort(b2,14) != 5 ||
            !((String)txt10.toObject(b2, 4)).equals("          ") ||
            !((String)new AS400Text(5, 37, systemObject_).toObject(b2,18)).equals("54321"))
        {
          output_.println(bin4.toInt(b2,0));
          output_.println(((String)txt10.toObject(b2, 4)) + ".");
          output_.println(bin2.toShort(b2,14));
          output_.println(((String)new AS400Text(5, 37, systemObject_).toObject(b2, 18)) + ".");
          failed("Record 2 does not contain correct values.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getContents().
   *<ul compact>
   *<li>Record format has dependent fields, set contents has been done via
   *setContents.
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The contents returned should be a byte array of AS/400 data initialized
   *to the contents of the byte array specified on the setContents().
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 5;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
      rf2.setLengthDependency("VARLEN", "NEXTLEN");
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1 + 45];
      byte[] c2 = new byte[expectedLength2 + 45];
      bin4.toBytes(0, c1);
      bin4.toBytes(0,c2);
      txt10.toBytes("          ",c1,4);
      txt10.toBytes("          ",c2,4);
      bin2.toBytes((short)5, c1,14);
      bin2.toBytes((short)5,c2,14);
      txt50.toBytes("12345",c1,16);
      bin2.toBytes((short)10, c2, 16);
      txt50.toBytes("5432112345",c2,18);

      // Get a new record
      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      r1.setContents(c1);
      r2.setContents(c2);
      if (r1.getRecordLength() != expectedLength1)
      {
        failed("r1.getRecordLength: " + String.valueOf(r1.getRecordLength()) + ", expected: " + String.valueOf(expectedLength1));
      }
      else if (r2.getRecordLength() != expectedLength2)
      {
        failed("r2.getRecordLength: " + String.valueOf(r2.getRecordLength()) + ", expected: " + String.valueOf(expectedLength2));
      }
      else
      {
        byte[] b1 = r1.getContents();
        byte[] b2 = r2.getContents();
        if (bin4.toInt(b1,0) != 0 || bin2.toShort(b1,14) != 5 ||
            !((String)txt10.toObject(b1, 4)).equals("          ") ||
            !((String)new AS400Text(5,37, systemObject_).toObject(b1,16)).equals("12345"))
        {
          output_.println(bin4.toInt(b1,0));
          output_.println(((String)txt10.toObject(b1, 4)) + ".");
          output_.println(bin2.toShort(b1,14));
          output_.println(((String)txt50.toObject(b1, 16)) + ".");
          failed("Record 1 does not contain correct values.");
        }
        else if (bin4.toInt(b2,0) != 0 || bin2.toShort(b2,14) != 5 ||
            !((String)txt10.toObject(b2, 4)).equals("          ") ||
            !((String)txt50.toObject(b2,18)).trim().equals("54321"))
        {
          output_.println(bin4.toInt(b2,0));
          output_.println(((String)txt10.toObject(b2, 4)) + ".");
          output_.println(bin2.toShort(b2,14));
          output_.println(((String)txt50.toObject(b2, 18)).trim() + ".");
          failed("Record 2 does not contain correct values.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getContents().
   *<ul compact>
   *<li>Record format has no dependent fields, no set contents has been done.
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No field descriptions with default values
   *<li>Field descriptions with default values
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The contents returned should be a byte array of AS/400 data initialized
   *to the appropriate default values for each field in the record format.
   *<li>The length of the byte array returned should be exactly the length
   *of an expected record of data.  E.g. if record format contains variable
   *length fields, there will be two extra bytes of data for each variable
   *length field.
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((BinaryFieldDescription)rf1.getFieldDescription("TOTLEN")).setDFT(Integer.valueOf(68));
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
  
      // Get a new record
      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      if (r1.getRecordLength() != expectedLength1)
      {
        failed("r1.getRecordLength: " + String.valueOf(r1.getRecordLength()) + ", expected: " + String.valueOf(expectedLength1));
      }
      else if (r2.getRecordLength() != expectedLength2)
      {
        failed("r2.getRecordLength: " + String.valueOf(r2.getRecordLength()) + ", expected: " + String.valueOf(expectedLength2));
      }
      else
      {
        byte[] b1 = r1.getContents();
        byte[] b2 = r2.getContents();
        if (bin4.toInt(b1,0) != 68 || bin2.toShort(b1,14) != 0 ||
            !((String)txt10.toObject(b1, 4)).equals("          ") ||
            !((String)txt50.toObject(b1,16)).equals("                                                  "))
        {
          output_.println(bin4.toInt(b1,0));
          output_.println(((String)txt10.toObject(b1, 4)) + ".");
          output_.println(bin2.toShort(b1,14));
          output_.println(((String)txt50.toObject(b1, 16)) + ".");
          failed("Record 1 does not contain correct values.");
        }
        else if (bin4.toInt(b2,0) != 0 || bin2.toShort(b2,14) != 0 ||
            !((String)txt10.toObject(b2, 4)).equals("          ") ||
            !((String)txt50.toObject(b2,18)).equals("                                                  "))
        {
          output_.println(bin4.toInt(b2,0));
          output_.println(((String)txt10.toObject(b2, 4)) + ".");
          output_.println(bin2.toShort(b2,14));
          output_.println(((String)txt50.toObject(b2, 16)) + ".");
          failed("Record 2 does not contain correct values.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getContents().
   *<ul compact>
   *<li>Record format has no dependent fields, set contents has been done via
   *constructor.
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No field descriptions with default values
   *<li>Field descriptions with default values
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The contents returned should be a byte array of AS/400 data initialized
   *to the contents of the byte array specified on the constructor.
   *</ul>
  **/
  public void Var005()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((BinaryFieldDescription)rf1.getFieldDescription("TOTLEN")).setDFT(Integer.valueOf(68));
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1];
      byte[] c2 = new byte[expectedLength2];
      bin4.toBytes(0, c1);
      bin4.toBytes(0,c2);
      txt10.toBytes("          ",c1,4);
      txt10.toBytes("          ",c2,4);
      bin2.toBytes((short)5, c1,14);
      bin2.toBytes((short)5,c2,14);
      txt50.toBytes("12345",c1,16);
      bin2.toBytes((short)10, c2, 16);
      txt50.toBytes("5432112345",c2,18);

      // Get a new record
      Record r1 = new Record(rf1, c1);
      Record r2 = new Record(rf2, c2);
      if (r1.getRecordLength() != expectedLength1)
      {
        failed("r1.getRecordLength: " + String.valueOf(r1.getRecordLength()) + ", expected: " + String.valueOf(expectedLength1));
      }
      else if (r2.getRecordLength() != expectedLength2)
      {
        failed("r2.getRecordLength: " + String.valueOf(r2.getRecordLength()) + ", expected: " + String.valueOf(expectedLength2));
      }
      else
      {
        byte[] b1 = r1.getContents();
        byte[] b2 = r2.getContents();
        if (bin4.toInt(b1,0) != 0 || bin2.toShort(b1,14) != 5 ||
            !((String)txt10.toObject(b1, 4)).equals("          ") ||
            !((String)txt50.toObject(b1,16)).equals("12345                                             "))
        {
          output_.println(bin4.toInt(b1,0));
          output_.println(((String)txt10.toObject(b1, 4)) + ".");
          output_.println(bin2.toShort(b1,14));
          output_.println(((String)txt50.toObject(b1, 16)) + ".");
          failed("Record 1 does not contain correct values.");
        }
        else if (bin4.toInt(b2,0) != 0 || bin2.toShort(b2,14) != 5 ||
            !((String)txt10.toObject(b2, 4)).equals("          ") ||
            !((String)txt10.toObject(b2,18)).equals("5432112345"))
        {
          output_.println(bin4.toInt(b2,0));
          output_.println(((String)txt10.toObject(b2, 4)) + ".");
          output_.println(bin2.toShort(b2,14));
          output_.println(((String)txt10.toObject(b2, 18)) + ".");
          failed("Record 2 does not contain correct values.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getContents().
   *<ul compact>
   *<li>Record format has no dependent fields, set contents has been done via
   *setContents.
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No field descriptions with default values
   *<li>Field descriptions with default values
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The contents returned should be a byte array of AS/400 data initialized
   *to the contents of the byte array specified on the setContents().
   *</ul>
  **/
  public void Var006()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((BinaryFieldDescription)rf1.getFieldDescription("TOTLEN")).setDFT(Integer.valueOf(68));
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1];
      byte[] c2 = new byte[expectedLength2];
      bin4.toBytes(0, c1);
      bin4.toBytes(0,c2);
      txt10.toBytes("          ",c1,4);
      txt10.toBytes("          ",c2,4);
      bin2.toBytes((short)5, c1,14);
      bin2.toBytes((short)5,c2,14);
      txt50.toBytes("12345",c1,16);
      bin2.toBytes((short)10, c2, 16);
      txt50.toBytes("5432112345",c2,18);

      // Get a new record
      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      r1.setContents(c1);
      r2.setContents(c2);
      if (r1.getRecordLength() != expectedLength1)
      {
        failed("r1.getRecordLength: " + String.valueOf(r1.getRecordLength()) + ", expected: " + String.valueOf(expectedLength1));
      }
      else if (r2.getRecordLength() != expectedLength2)
      {
        failed("r2.getRecordLength: " + String.valueOf(r2.getRecordLength()) + ", expected: " + String.valueOf(expectedLength2));
      }
      else
      {
        byte[] b1 = r1.getContents();
        byte[] b2 = r2.getContents();
        if (bin4.toInt(b1,0) != 0 || bin2.toShort(b1,14) != 5 ||
            !((String)txt10.toObject(b1, 4)).equals("          ") ||
            !((String)txt50.toObject(b1,16)).equals("12345                                             "))
        {
          output_.println(bin4.toInt(b1,0));
          output_.println(((String)txt10.toObject(b1, 4)) + ".");
          output_.println(bin2.toShort(b1,14));
          output_.println(((String)txt50.toObject(b1, 16)) + ".");
          failed("Record 1 does not contain correct values.");
        }
        else if (bin4.toInt(b2,0) != 0 || bin2.toShort(b2,14) != 5 ||
            !((String)txt10.toObject(b2, 4)).equals("          ") ||
            !((String)txt10.toObject(b2,18)).equals("5432112345"))
        {
          output_.println(bin4.toInt(b2,0));
          output_.println(((String)txt10.toObject(b2, 4)) + ".");
          output_.println(bin2.toShort(b2,14));
          output_.println(((String)txt10.toObject(b2, 18)) + ".");
          failed("Record 2 does not contain correct values.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify invalid usage of getContents().
   *<ul compact>
   *<li>Before recordFormat has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating recordFormat and
   *PROPERTY_NOT_SET
   *</ul>
  **/
  public void Var007()
  {
    try
    {
      Record rc = new Record();
      byte[] b = rc.getContents();
      failed("No exception calling with no record format"+b);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                               ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling with no record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getContents(OutputStream).  Becuase this
   *version of getContents calls getContents(), only a simple test
   *is done here.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The contents as set on the constructor will be returned.
   *</ul>
  **/
  public void Var008()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try
    {
      // Create record based on our RecordFormat and contents
      rec = new Record(r, contents);
      // Verify getContents
      rec.getContents(out);
      byte[] match = out.toByteArray();
      if (match.length != contents.length)
      {
        failed("getContents did not set OutputStream correctly");
        return;
      }
      else
      {
        for (short i = 0; i < contents.length; ++i)
        {
          if (match[i] != contents[i])
          {
            failed("OutputStream contents do not match contents of record");
            return;
          }
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Exception on getContents\n");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getContents(OutputStream).
   *<ul>
   *<li>Passing null for outputstream
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "out"
   *</ul>
  **/
  public void Var009()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      rec.getContents(null);
      failed("No exception passing null for output stream");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "out"))
      {
        failed(e, "Wrong exception/parameter passing null for output stream");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getContents(OutputStream).
   *<ul>
   *<li>Call to getContents(OutputStream) before recordFormat has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating "recordFormat" and
   *PROPERTY_NOT_SET
   *</ul>
  **/
  public void Var010()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try
    {
      Record rc = new Record();
      rc.getContents(out);
      failed("No exception calling without record format");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling without record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getField(int).
   *The variation testing getFields() will verify that each field is returned
   *correctly in a record for a variety of RecordFormat scenarios
   *(same scenarios as tested under getContents()).  Therefore we will do a
   *trivial test here (simple record format) to verify the valid usage of
   *getField(int).
   *<ul compact>
   *<li>Retrieve each field in the record, verifying it's object type and value.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct field type and value will be returned in each case.
   *</ul>
  **/
  public void Var011()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      Object field;
      // Verify the object types returned from getField()
      for (int i = 0; i < 13; ++i)
      {
        // Verify the type
        field = rec.getField(i);
        if (field.getClass() != jtArray[i].getClass())
        {
          failed((Integer.valueOf(i)).toString() + ": Wrong object type returned");
          return;
        }
      }
      // Verify the object values returned
      if (((Integer)rec.getField(0)).intValue() != ((Integer)jtArray[0]).intValue())
      {
        failed("0: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(1)).equals(pad((String)jtArray[1], 10)))
      {
        failed("1: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(2)).equals((String)jtArray[2]))
      {
        failed("2: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(3)).equals(pad((String)jtArray[3], 10)))
      {
        failed("3: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(4)).equals((String)jtArray[4]))
      {
        failed("4: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(5)).equals((String)jtArray[5]))
      {
        failed("5: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(6)).equals(pad((String)jtArray[6], 10)))
      {
        failed("6: Wrong value returned.");
        return;
      }

      if (((Double)rec.getField(7)).doubleValue() != ((Double)jtArray[7]).doubleValue())
      {
        failed("7: Wrong value returned.");
        return;
      }

      byte[] val = (byte[])rec.getField(8);
      byte[] chkVal = (byte[])jtArray[8];
      if (val.length != chkVal.length)
      {
        failed("8: Wrong value length returned.");
        return;
      }
      for (int i = 0; i < val.length; ++i)
      {
        if (val[i] != chkVal[i])
        {
          failed("08 Wrong value returned.");
          return;
        }
      }

      if (!((BigDecimal)rec.getField(9)).toString().equals(((BigDecimal)jtArray[9]).toString()))
      {
        failed("9: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(10)).equals((String)jtArray[10]))
      {
        failed("10: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(11)).equals((String)jtArray[11]))
      {
        failed("11: Wrong value returned.");
        return;
      }

      if (!((BigDecimal)rec.getField(12)).toString().equals(((BigDecimal)jtArray[12]).toString()))
      {
        failed("12: Wrong value returned.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getField(int).
   *<ul>
   *<li>index < 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID
  **/
  public void Var012()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      Object obj = rec.getField(-1);
      failed("No exception passing invalid index"+obj);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter when passing invalid index");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getField(int).
   *<ul>
   *<li>index > number of fields - 1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID
  **/
  public void Var013()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      Object obj = rec.getField(r.getNumberOfFields());
      failed("No exception passing invalid index"+obj);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter when passing invalid index");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getField(int).
   *<ul>
   *<li>Call getField before recordFormat has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating "recordFormat" and
   *PROPERTY_NOT_SET
  **/
  public void Var014()
  {
    try
    {
      Record rc = new Record();
      Object obj = rc.getField(2);
      failed("No exception calling without a record format"+obj);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getField(String).
   *The variation testing getFields() will verify that each field is returned
   *correctly in a record for a variety of RecordFormat scenarios
   *(same scenarios as tested under getContents()).  Therefore we will do a
   *trivial test here (simple record format) to verify the valid usage of
   *getField(String).
   *<ul compact>
   *<li>Retrieve each field in the record, verifying it's object type and value.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct field type and value will be returned in each case.
   *</ul>
  **/
  public void Var015()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      // Verify the object types returned from getField()
      String name = null;
      Object field;
      for (int i = 0; i < 13; ++i)
      {
        name = "field" + (Integer.valueOf(i+1)).toString();
        // Verify the type
        field = rec.getField(name);
        if (field.getClass() != jtArray[i].getClass())
        {
          failed((Integer.valueOf(i)).toString() + ": Wrong object type returned");
          return;
        }
      }
      // Verify the object values returned
      if (((Integer)rec.getField("field1")).intValue() != ((Integer)jtArray[0]).intValue())
      {
        failed("0: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field2")).equals(pad((String)jtArray[1], 10)))
      {
        failed("1: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field3")).equals((String)jtArray[2]))
      {
        failed("2: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field4")).equals(pad((String)jtArray[3], 10)))
      {
        failed("3: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field5")).equals((String)jtArray[4]))
      {
        failed("4: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field6")).equals((String)jtArray[5]))
      {
        failed("5: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field7")).equals(pad((String)jtArray[6], 10)))
      {
        failed("6: Wrong value returned.");
        return;
      }

      if (((Double)rec.getField("field8")).doubleValue() != ((Double)jtArray[7]).doubleValue())
      {
        failed("7: Wrong value returned.");
        return;
      }

      byte[] val = (byte[])rec.getField("field9");
      byte[] chkVal = (byte[])jtArray[8];
      if (val.length != chkVal.length)
      {
        failed("8: Wrong value length returned.");
        return;
      }
      for (int i = 0; i < val.length; ++i)
      {
        if (val[i] != chkVal[i])
        {
          failed("8: Wrong value returned.");
          return;
        }
      }

      if (!((BigDecimal)rec.getField("field10")).toString().equals(((BigDecimal)jtArray[9]).toString()))
      {
        failed("9: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field11")).equals((String)jtArray[10]))
      {
        failed("10: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField("field12")).equals((String)jtArray[11]))
      {
        failed("11 Wrong value returned.");
        return;
      }

      if (!((BigDecimal)rec.getField("field13")).toString().equals(((BigDecimal)jtArray[12]).toString()))
      {
        failed("12: Wrong value returned.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getField(String).
   *<ul>
   *<li>Passing null for field name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "name"
   *</ul>
  **/
  public void Var016()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      // Verify exception when passing null for field name
      Object obj = rec.getField(null);
      failed("No exception passing invalid index"+obj);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong exception/parameter when passing null field name");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getField(String).
   *<ul>
   *<li>Passing non-existent field name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
  **/
  public void Var017()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      Object obj = rec.getField("non-existent");
      failed("No exception passing invalid index"+obj);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong exception/parameter when passing non-existent field name");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getField(String).
   *<ul>
   *<li>Call getField before recordFormat has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating "recordFormat" and
   *PROPERTY_NOT_SET
   *</ul>
  **/
  public void Var018()
  {
    try
    {
      Record rc = new Record();
      Object obj = rc.getField("field2");
      failed("No exception calling without a record format"+obj);
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling with no record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getFields().
   *<ul compact>
   *<li>Record format has dependent fields, no set contents has been done.
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The field values returned should be the Java objects corresponding
   *to the default values of the fields.
   *</ul>
  **/
  public void Var019()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
      rf2.setLengthDependency("VARLEN", "NEXTLEN");
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1];
      byte[] c2 = new byte[expectedLength2];

      // Get a new record
      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      Object[] fields1 = r1.getFields();
      Object[] fields2 = r2.getFields();
      if (((Integer)fields1[0]).intValue() != 0 ||
          !((String)fields1[1]).equals("") ||
          ((Short)fields1[2]).shortValue() != 0 ||
          !((String)fields1[3]).equals(""))
      {
        for (int i = 0; i < fields1.length; ++i)
        {
          output_.println(fields1[i].toString() + ".");
        }
        failed("Record 1 does not contain correct values. c1="+c1+"c2="+c2);
      }
      else if (((Integer)fields2[0]).intValue() != 0 ||
          !((String)fields2[1]).equals("") ||
          ((Short)fields2[2]).shortValue() != 0 ||
          !((String)fields2[3]).equals(""))
      {
        for (int i = 0; i < fields2.length; ++i)
        {
          output_.println(fields2[i].toString() + ".");
        }
        failed("Record 2 does not contain correct values.");
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getFields().
   *<ul compact>
   *<li>Record format has dependent fields, set contents has been done
   *via the constructor
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The field values returned should be the Java objects corresponding
   *to the AS/400 values of the fields.
   *</ul>
  **/
  public void Var020()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 5;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 5 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
      rf2.setLengthDependency("VARLEN", "NEXTLEN");
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1 + 45];
      byte[] c2 = new byte[expectedLength2 + 45];
      bin4.toBytes(0, c1);
      bin4.toBytes(0,c2);
      txt10.toBytes("          ",c1,4);
      txt10.toBytes("          ",c2,4);
      bin2.toBytes((short)5, c1,14);
      bin2.toBytes((short)5,c2,14);
      txt50.toBytes("12345",c1,16);
      bin2.toBytes((short)10, c2, 16);
      txt50.toBytes("5432112345",c2,18);

      // Get a new record
      Record r1 = new Record(rf1, c1);
      Record r2 = new Record(rf2, c2);
      Object[] fields1 = r1.getFields();
      Object[] fields2 = r2.getFields();
      if (((Integer)fields1[0]).intValue() != 0 ||
          !((String)fields1[1]).equals("          ") ||
          ((Short)fields1[2]).shortValue() != 5 ||
          !((String)fields1[3]).equals("12345"))
      {
        for (int i = 0; i < fields1.length; ++i)
        {
          output_.println(fields1[i].toString() + ".");
        }
        failed("Record 1 does not contain correct values.");
      }
      else if (((Integer)fields2[0]).intValue() != 0 ||
          !((String)fields2[1]).equals("          ") ||
          ((Short)fields2[2]).shortValue() != 5 ||
          !((String)fields2[3]).equals("54321"))
      {
        for (int i = 0; i < fields2.length; ++i)
        {
          output_.println(fields2[i].toString() + ".");
        }
        failed("Record 2 does not contain correct values.");
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getFields().
   *<ul compact>
   *<li>Record format has dependent fields, set contents has been done
   *via setContents
   *<li>Test a variety of record formats:
   *<ul compact>
   *<li>No variable length field descriptions
   *<li>Variable length field descriptions
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The field values returned should be the Java objects corresponding
   *to the AS/400 values of the fields.
   *</ul>
  **/
  public void Var021()
  {
    try
    {
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 5;
      int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 5 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
      rf2.setLengthDependency("VARLEN", "NEXTLEN");
  
      // Set up contents array
      byte[] c1 = new byte[expectedLength1 + 45];
      byte[] c2 = new byte[expectedLength2 + 45];
      bin4.toBytes(0, c1);
      bin4.toBytes(0,c2);
      txt10.toBytes("          ",c1,4);
      txt10.toBytes("          ",c2,4);
      bin2.toBytes((short)5, c1,14);
      bin2.toBytes((short)5,c2,14);
      txt50.toBytes("12345",c1,16);
      bin2.toBytes((short)10, c2, 16);
      txt50.toBytes("5432112345",c2,18);

      // Get a new record
      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      r1.setContents(c1);
      r2.setContents(c2);
      Object[] fields1 = r1.getFields();
      Object[] fields2 = r2.getFields();
      if (((Integer)fields1[0]).intValue() != 0 ||
          !((String)fields1[1]).equals("          ") ||
          ((Short)fields1[2]).shortValue() != 5 ||
          !((String)fields1[3]).equals("12345"))
      {
        for (int i = 0; i < fields1.length; ++i)
        {
          output_.println(fields1[i].toString() + ".");
        }
        failed("Record 1 does not contain correct values.");
      }
      else if (((Integer)fields2[0]).intValue() != 0 ||
          !((String)fields2[1]).equals("          ") ||
          ((Short)fields2[2]).shortValue() != 5 ||
          !((String)fields2[3]).equals("54321"))
      {
        for (int i = 0; i < fields2.length; ++i)
        {
          output_.println(fields2[i].toString() + ".");
        }
        failed("Record 2 does not contain correct values.");
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of getFields();
   *<ul compact>
   *<li>Record format has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 is returned.
   *</ul>
  **/
  public void Var022()
  {
    rec = new Record();
    try
    {
      Object[] fields = rec.getFields();
      if (fields.length != 0)
      {
        failed("Array of size 0 not returned when no record format set.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getKeyFields();
   *<ul compact>
   *<li>When there are no key fields
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 is returned.
   *</ul>
  **/
  public void Var023()
  {
    rec = new Record();
    try
    {
      Object[] fields = rec.getKeyFields();
      if (fields.length != 0)
      {
        failed("Array of size 0 not returned when no key fields exist.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getKeyFields();
   *<ul compact>
   *<li>When there are key fields
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size getNumberOfKeyFields is returned.  The object type
   *and value of each key field is correct.
   *</ul>
  **/
  public void Var024()
  {
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "b2"));
    rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(5, systemObject_.getCcsid(), systemObject_), "c"));
    rf.addKeyFieldDescription(2);
    rf.addKeyFieldDescription(0);
    try
    {
      rec = new Record(rf);
      rec.setField(0, Integer.valueOf(1));
      rec.setField(2, "ABCDE");
      Object[] fields = rec.getKeyFields();
      if (fields.length != 2)
      {
        failed("Wrong size array returned.");
        return;
      }
      if (((Integer)fields[1]).intValue() != 1)
      {
        failed("Wrong key value for key field 0");
        return;
      }
      if (!((String)fields[0]).equals("ABCDE"))
      {
        failed("Wrong key value for key field 1");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getKeyFields();
   *<ul compact>
   *<li>Record format has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 is returned.
   *</ul>
  **/
  public void Var025()
  {
    rec = new Record();
    try
    {
      Object[] fields = rec.getKeyFields();
      if (fields.length != 0)
      {
        failed("Array of size 0 not returned when no record format set.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getNumberOfFields().
   *<ul compact>
   *<li>Record format has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct number of fields is returned.
   *</ul>
  **/
  public void Var026()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      // Verify number of fields
      if (rec.getNumberOfFields() != numFields)
      {
        failed("Wrong number of fields returned");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getNumberOfFields().
   *<ul compact>
   *<li>Record format has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>0 is returned.
   *</ul>
  **/
  public void Var027()
  {
    try
    {
      Record rc = new Record();
      if (rc.getNumberOfFields() != 0)
      {
        failed("0 not returned for number of fields");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getNumberOfKeyFields().
   *<ul compact>
   *<li>Record format has been set and there are key fields.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct number of fields is returned.
   *</ul>
  **/
  public void Var028()
  {
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "b2"));
    rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(5, systemObject_.getCcsid(), systemObject_), "c"));
    rf.addKeyFieldDescription(2);
    rf.addKeyFieldDescription(0);
    try
    {
      // Create record based on our RecordFormat and contents
      rec = new Record(rf);
      // Verify number of fields
      if (rec.getNumberOfKeyFields() != 2)
      {
        failed("Wrong number of key fields returned");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getNumberOfKeyFields().
   *<ul compact>
   *<li>Record format has been set and there are no key fields.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct number of fields is returned.
   *</ul>
  **/
  public void Var029()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      // Verify number of fields
      if (rec.getNumberOfKeyFields() != 0)
      {
        failed("0 not returned");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getNumberOfKeyFields().
   *<ul compact>
   *<li>Record format has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>0 is returned.
   *</ul>
  **/
  public void Var030()
  {
    try
    {
      Record rc = new Record();
      if (rc.getNumberOfKeyFields() != 0)
      {
        failed("0 not returned for number of fields");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordFormat().
   *<ul compact>
   *<li>Record format not set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>null is returned.
   *</ul>
  **/
  public void Var031()
  {
    try
    {
      rec = new Record();
      if (rec.getRecordFormat() != null)
      {
        failed("null not returned");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordFormat().
   *<ul compact>
   *<li>Record format set via constructor.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Specified record format returned.
   *</ul>
  **/
  public void Var032()
  {
    try
    {
      rec = new Record(r);
      if (rec.getRecordFormat() != r)
      {
        failed("record format not returned");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordFormat().
   *<ul compact>
   *<li>Record format set via setRecordFormat.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Specified record format returned.
   *</ul>
  **/
  public void Var033()
  {
    try
    {
      rec = new Record();
      rec.setRecordFormat(r);
      if (rec.getRecordFormat() != r)
      {
        failed("record format not returned");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordLength().
   *<ul compact>
   *<li>Record format set.
   *<li>Use several different record formats
   *<ul compact>
   *<li>Contains variable length fields
   *<li>Contains length dependent fields
   *<li>Contains offset dependent fields
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record length based on the record format field descriptions is
   *returned.
   *</ul>
  **/
  public void Var034()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);

      if (rec.getRecordLength() != recLength)
      {
        failed("Wrong record length returned");
        return;
      }
      AS400Bin4 bin4 = new AS400Bin4();
      AS400Text txt10 = new AS400Text(10,37, systemObject_);
      AS400Bin2 bin2 = new AS400Bin2();
      AS400Text txt50 = new AS400Text(50,37, systemObject_);
      // int expectedLength1 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50;
      // int expectedLength2 = bin4.getByteLength() + 10 + bin2.getByteLength() + 50 + 2;
      RecordFormat rf1 = new RecordFormat();
      rf1.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf1.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf1.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf1.setLengthDependency("VARLEN", "NEXTLEN");
  
      RecordFormat rf2 = new RecordFormat();
      rf2.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf2.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf2.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      ((VariableLengthFieldDescription)rf2.getFieldDescription("VARLEN")).setVariableLength(true);
    
      RecordFormat rf3 = new RecordFormat();
      rf3.addFieldDescription(new BinaryFieldDescription(bin4, "TOTLEN"));
      rf3.addFieldDescription(new CharacterFieldDescription(txt10, "NAME"));
      rf3.addFieldDescription(new BinaryFieldDescription(bin2, "NEXTLEN"));
      rf3.addFieldDescription(new BinaryFieldDescription(bin2, "SKIP"));
      rf3.addFieldDescription(new CharacterFieldDescription(txt50, "VARLEN"));
      rf3.setOffsetDependency("VARLEN", "NEXTLEN");

      Record r1 = new Record(rf1);
      Record r2 = new Record(rf2);
      Record r3 = new Record(rf3);
  
      if (r1.getRecordLength() != 66 || r2.getRecordLength() != 68 || r3.getRecordLength() != 68)
      {
        output_.println("r1.getRecordLength: " +
                         String.valueOf(r1.getRecordLength()) +
                         ", expected: " + String.valueOf(66));
        output_.println("r2.getRecordLength: " +
                         String.valueOf(r2.getRecordLength()) +
                         ", expected: " + String.valueOf(68));
        output_.println("r3.getRecordLength: " +
                         String.valueOf(r3.getRecordLength()) +
                         ", expected: " + String.valueOf(68));
        failed("Incorrect record length after default contents constructor.");
      }
      else
      {
        // Verify record length changed as appropriate by setContents
        // Set up contents array
        byte[] c1 = new byte[66];
        byte[] c2 = new byte[68];
        byte[] c3 = new byte[68];
        bin4.toBytes(0, c1);
        bin4.toBytes(0,c2);
        bin4.toBytes(0,c3);
        txt10.toBytes("          ",c1,4);
        txt10.toBytes("          ",c2,4);
        bin2.toBytes((short)5, c1,14);
        bin2.toBytes((short)5,c2,14);
        bin2.toBytes((short)18,c3,14);
        bin2.toBytes((short)0,c3,16);
        txt50.toBytes("12345",c1,16);
        bin2.toBytes((short)10, c2, 16);
        txt50.toBytes("5432112345",c2,18);
        txt50.toBytes("23456",c1,16);

        r1.setContents(c1);
        r2.setContents(c2);
        r3.setContents(c3);
        if (r1.getRecordLength() != 21 || r2.getRecordLength() != 68 || r3.getRecordLength() != 68)
        {
          output_.println("r1.getRecordLength: " +
                           String.valueOf(r1.getRecordLength()) +
                           ", expected: " + String.valueOf(21));
          output_.println("r2.getRecordLength: " +
                           String.valueOf(r2.getRecordLength()) +
                           ", expected: " + String.valueOf(68));
          output_.println("r3.getRecordLength: " +
                           String.valueOf(r3.getRecordLength()) +
                           ", expected: " + String.valueOf(68));
          failed("Incorrect record length after contents set.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
    }
    return;
  }

  /**
   *Verify usage of getRecordLength().
   *<ul>
   *<li>Record format has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Zero is returned
   </ul>
  **/
  public void Var035()
  {
    try
    {
      Record rc = new Record();
      if (rc.getRecordLength() != 0)
      {
        failed("0 not returned calling getRecordLength without record format");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordName().
   *<ul>
   *<li>When no record name was specified
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Empty string is returned.
   *</ul>
  **/
  public void Var036()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      // Verify null returned when no name specified
      if (!rec.getRecordName().equals(""))
      {
        failed("Empty string not returned for record name");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordName().
   *<ul>
   *<li>When a record name was specified
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Name specified is returned
   *</ul>
  **/
  public void Var037()
  {
    try
    {
      // Verify correct record name retrned when specified
      Record rec2 = r.getNewRecord(contents, "recName");
      if (!rec2.getRecordName().equals("recName"))
      {
        failed("Wrong record name returned");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception.");
       return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordNumber().
   *<ul>
   *<li>When no record number was set
   *<li>When a record number was set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>0 returned.
   *</ul>
  **/
  public void Var038()
  {
    try
    {
      // Create record based on our RecordFormat and contents
      rec = r.getNewRecord(contents);
      // Verify 0 returned when no number set
      if (rec.getRecordNumber() != 0)
      {
        failed("0 not returned for record number");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
       return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getRecordNumber().
   *<ul>
   *<li>When a record number was set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>record number specified is returned.
   *</ul>
  **/
  public void Var039()
  {
    try
    {
      // Verify correct record umber returned when set
      Record rec2 = r.getNewRecord(contents, "recName");
      rec2.setRecordNumber(1);
      if (rec2.getRecordNumber() != 1)
      {
        failed("Wrong record number returned");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
       return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of isNullField(int).
   *<ul compact>
   *<li>Field is not null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>false is returned.
   *</ul>
  **/
  public void Var040()
  {
    try
    {
      rec = new Record(r);
      if (rec.isNullField(0))
      {
        failed("Field 0 returning true for isNull.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of isNullField(int).
   *<ul compact>
   *<li>Field is null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>true is returned.
   *</ul>
  **/
  public void Var041()
  {
    try
    {
      rec = new Record(r);
      rec.setField(0, null);
      if (!rec.isNullField(0))
      {
        failed("Field 0 returning false for isNull.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of isNullField(int).
   *<ul compact>
   *<li>index < 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var042()
  {
    try
    {
      rec = new Record(r);
      rec.isNullField(-1);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of isNullField(int).
   *<ul compact>
   *<li>index > getNumberOfFields - 1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var043()
  {
    try
    {
      rec = new Record(r);
      rec.isNullField(rec.getNumberOfFields());
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of isNullField(int).
   *<ul compact>
   *<li>Record format not set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating "recordFormat" and
   *PROPERTY_NOT_SET.
   *</ul>
  **/
  public void Var044()
  {
    try
    {
      rec = new Record();
      rec.isNullField(0);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat", ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of isNullField(String).
   *<ul compact>
   *<li>Field is not null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>false is returned.
   *</ul>
  **/
  public void Var045()
  {
    try
    {
      rec = new Record(r);
      if (rec.isNullField("field1"))
      {
        failed("Field 0 returning true for isNull.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of isNullField(String).
   *<ul compact>
   *<li>Field is null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>true is returned.
   *</ul>
  **/
  public void Var046()
  {
    try
    {
      rec = new Record(r);
      rec.setField(0, null);
      if (!rec.isNullField("field1"))
      {
        failed("Field 0 returning false for isNull.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of isNullField(String).
   *<ul compact>
   *<li>name is null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "name"
   *</ul>
  **/
  public void Var047()
  {
    try
    {
      rec = new Record(r);
      rec.isNullField(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of isNullField(String).
   *<ul compact>
   *<li>Non existent field name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var048()
  {
    try
    {
      rec = new Record(r);
      rec.isNullField("blah");
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of isNullField(String).
   *<ul compact>
   *<li>Record format not set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating "recordFormat" and
   *PROPERTY_NOT_SET.
   *</ul>
  **/
  public void Var049()
  {
    try
    {
      rec = new Record();
      rec.isNullField("field1");
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat", ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setContents(byte[]).
   *We use a simple record format for this test.  setContents() using a variety of
   *record formats is tested under getContents().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>THe object types and values will be correct.
   *</ul>
  **/
  public void Var050()
  {
    // Create Record object containing default values
    rec = r.getNewRecord();
    try
    {
      // Set the contents of the record
      rec.setContents(contents);
      // Verify the contents set
      // Verify the object values returned
      if (((Integer)rec.getField(0)).intValue() != ((Integer)jtArray[0]).intValue())
      {
        failed("0: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(1)).equals(pad((String)jtArray[1], 10)))
      {
        failed("1: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(2)).equals((String)jtArray[2]))
      {
        failed("2: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(3)).equals(pad((String)jtArray[3], 10)))
      {
        failed("3: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(4)).equals((String)jtArray[4]))
      {
        failed("4: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(5)).equals((String)jtArray[5]))
      {
        failed("5: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(6)).equals(pad((String)jtArray[6], 10)))
      {
        failed("6: Wrong value returned.");
        return;
      }

      if (((Double)rec.getField(7)).doubleValue() != ((Double)jtArray[7]).doubleValue())
      {
        failed("7: Wrong value returned.");
        return;
      }

      byte[] val = (byte[])rec.getField(8);
      byte[] chkVal = (byte[])jtArray[8];
      if (val.length != chkVal.length)
      {
        failed("8: Wrong value length returned.");
        return;
      }
      for (int i = 0; i < val.length; ++i)
      {
        if (val[i] != chkVal[i])
        {
          failed("8: Wrong value returned.");
          return;
        }
      }

      if (!((BigDecimal)rec.getField(9)).toString().equals(((BigDecimal)jtArray[9]).toString()))
      {
        failed("9: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(10)).equals((String)jtArray[10]))
      {
        failed("10: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(11)).equals((String)jtArray[11]))
      {
        failed("11: Wrong value returned.");
        return;
      }

      if (!((BigDecimal)rec.getField(12)).toString().equals(((BigDecimal)jtArray[12]).toString()))
      {
        failed("12: Wrong value returned.");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[]).
   *<ul>
   *<li>Passing null for byte[]
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "contents"
   *</ul>
  **/
  public void Var051()
  {
 
    // Create Record object containing default values
    rec = r.getNewRecord();
    // Verify exception passing null byte[]
    try
    {
      rec.setContents((byte[])null);
      failed("No exception passing null byte[]");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "contents"))
      {
        failed(e, "Wrong exception/parameter passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[]).
   *<ul>
   *<li>Passing byte[] of length 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "contents" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var052()
  {
    // Create Record object containing default values
    try
    {
      rec = r.getNewRecord();
      // Verify exception passing byte[] of length 0
      rec.setContents(new byte[0]);
      failed("No exception passing 0 length byte[]");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter passing byte[] of length 0");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[]).
   *<ul>
   *<li>Passing byte[] of length < record length
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException
   *</ul>
  **/
  public void Var053()
  {
    // Create Record object containing default values
    try
    {
      rec = r.getNewRecord();
      // Verify exception passing byte[] of length 0
      rec.setContents(new byte[1]);
      failed("No exception passing byte[] of length < record length");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException"))
      {
        failed(e, "Wrong exception/parameter passing byte[] of length  < record length");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[]).
   *<ul>
   *<li>byte[] whose length is not appropriate based on the length dependent field length.
   *I.e. the depended on field specified a length which causes the record length to be greater
   *than the length of teh byte array.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException.
   *</ul>
  **/
  public void Var054()
  {
    // Create Record object containing default values
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c"));
    rf.setLengthDependency(1, 0);
    rec = r.getNewRecord();
    rec.setField(0, Integer.valueOf(5));
    // Verify exception passing byte[] of length 0
    try
    {
      rec.setContents(new byte[7]);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[]).
   *<ul>
   *<li>Calling setContents(byte[]) before record format has been set.
   *</ul>
  **/
  public void Var055()
  {
    try
    {
      Record rc = new Record();
      rc.setContents(contents);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",
                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling without record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setContents(byte[], int).
   *We use a simple record format for this test.  setContents() using a variety of
   *record formats is tested under getContents().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The object types and values will be correct.
   *</ul>
  **/
  public void Var056()
  {
    // Create Record object containing default values
    rec = r.getNewRecord();
    byte[] bytes = new byte[rec.getRecordLength() + 20];
    System.arraycopy(contents, 0, bytes, 10, contents.length);
    try
    {
      // Set the contents of the record
      rec.setContents(bytes, 10);
      // Verify the contents set
      // Verify the object values returned
      if (((Integer)rec.getField(0)).intValue() != ((Integer)jtArray[0]).intValue())
      {
        failed("0: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(1)).equals(pad((String)jtArray[1], 10)))
      {
        failed("1: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(2)).equals((String)jtArray[2]))
      {
        failed("2: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(3)).equals(pad((String)jtArray[3], 10)))
      {
        failed("3: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(4)).equals((String)jtArray[4]))
      {
        failed("4: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(5)).equals((String)jtArray[5]))
      {
        failed("5: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(6)).equals(pad((String)jtArray[6], 10)))
      {
        failed("6: Wrong value returned.");
        return;
      }

      if (((Double)rec.getField(7)).doubleValue() != ((Double)jtArray[7]).doubleValue())
      {
        failed("7: Wrong value returned.");
        return;
      }

      byte[] val = (byte[])rec.getField(8);
      byte[] chkVal = (byte[])jtArray[8];
      if (val.length != chkVal.length)
      {
        failed("8: Wrong value length returned.");
        return;
      }
      for (int i = 0; i < val.length; ++i)
      {
        if (val[i] != chkVal[i])
        {
          failed("8: Wrong value returned.");
          return;
        }
      }

      if (!((BigDecimal)rec.getField(9)).toString().equals(((BigDecimal)jtArray[9]).toString()))
      {
        failed("9: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(10)).equals((String)jtArray[10]))
      {
        failed("10: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(11)).equals((String)jtArray[11]))
      {
        failed("11: Wrong value returned.");
        return;
      }

      if (!((BigDecimal)rec.getField(12)).toString().equals(((BigDecimal)jtArray[12]).toString()))
      {
        failed("12: Wrong value returned.");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
       return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[], int).
   *<ul>
   *<li>Passing null for byte[]
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "contents"
   *</ul>
  **/
  public void Var057()
  {
    // Create Record object containing default values
    rec = r.getNewRecord();
    // Verify exception passing null byte[]
    try
    {
      rec.setContents((byte[])null, 0);
      failed("No exception passing null byte[]");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "contents"))
      {
        failed(e, "Wrong exception/parameter passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[], int).
   *<ul>
   *<li>Passing byte[] of length 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "contents" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var058()
  {
    // Create Record object containing default values
    try
    {
      rec = r.getNewRecord();
      // Verify exception passing byte[] of length 0
      rec.setContents(new byte[0], 0);
      failed("No exception passing 0 length byte[]");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter passing byte[] of length 0");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[]).
   *<ul>
   *<li>Passing byte[] of length < record length
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException
   *</ul>
  **/
  public void Var059()
  {
    // Create Record object containing default values
    try
    {
      rec = r.getNewRecord();
      rec.setContents(new byte[2], 1);
      failed("No exception passing byte[] of length < record length");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException"))
      {
        failed(e, "Wrong exception/parameter passing byte[] of length  < record length");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[], int).
   *<ul>
   *<li>Passing < 0 for offset
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "offset" and
   *RANGE_NOT_VALID
  **/
  public void Var060()
  {
    // Create Record object containing default values
    rec = r.getNewRecord();
    // Verify exception passing < 0 for offset
    try
    {
      rec.setContents(contents, -1);
      failed("No exception passing offset < 0");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "offset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter passing  0 for offset");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(byte[], int).
   *<ul>
   *<li>Calling setContents(byte[], int) before record format has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException idicating "recordFormat" and
   *PROPERTY_NOT_SET
   *</ul>
  **/
  public void Var061()
  {
    try
    {
      Record rc = new Record();
      rc.setContents(contents, 0);
      failed("No exception calling without record format");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling without record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setContents(InputStream)
   *<br>
   *<ul compact>
   *<li>The object types and values will be correct
  **/
  public void Var062()
  {
    ByteArrayInputStream in = new ByteArrayInputStream(contents);
    // Create record based on our RecordFormat
    rec = r.getNewRecord();
    // Verify setContents
    try
    {
      rec.setContents(in);
      // Verify that the contents of the record are correct (should match the contents byte array)
      byte[] recContents = rec.getContents();
      if (contents.length != recContents.length)
      {
        failed("getContents did not set InputStream correctly");
        return;
      }
      else
      {
        for (short i = 0; i < recContents.length; ++i)
        {
          if (contents[i] != recContents[i])
          {
            failed("InputStream contents do not match contents of record");
            return;
          }
        }
      }
    }
    catch (Exception e)
    {
      failed(e, "Exception on getContents");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(InputStream)
   *<ul>
   *<li>Passing null for input stream
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "in"
   *</ul>
  **/
  public void Var063()
  {
    // Get record initialized to the default values
    rec = r.getNewRecord();
    // Verify exception when passing null for input stream
    try
    {
      rec.setContents((InputStream)null);
      failed("No exception passing null for input stream");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "in"))
      {
        failed(e, "Wrong exception/parameter passing null for input stream");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setContents(InputStream)
   *<ul>
   *<li>Calling setContents(InputStream) before record format has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException idicating "recordFormat" and
   *PROPERTY_NOT_SET
   *</ul>
  **/
  public void Var064()
  {
    ByteArrayInputStream in = new ByteArrayInputStream(contents);
    try
    {
      Record rc = new Record();
      rc.setContents(in);
      failed("No exception calling without record format");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling without record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setField(int, Object)
   *<ul compact>
   *<li>Set field to null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getField() returns null
   *<li>isNullField returns null
   *</ul>
  **/
  public void Var065()
  {
    try
    {
      // Create record with contents set with the contents array
      rec = r.getNewRecord(contents);
      // Set three of the fields
      rec.setField(0, null);
      if (rec.getField(0) != null)
      {
        failed("getField() not returning null");
        return;
      }
      if (!rec.isNullField(0))
      {
        failed("isNullField() not returning null");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setField(int, Object)
   *<ul compact>
   *<li>Set field to a value other than null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getField() returns the value set
   *</ul>
  **/
  public void Var066()
  {
    try
    {
      // Create record with contents set with the contents array
      rec = r.getNewRecord(contents);
      // Set three of the fields
      rec.setField(0, Integer.valueOf(25));
      if (((Integer)rec.getField(0)).intValue() != 25)
      {
        failed("getField() not returning correct value");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setField(int, Object)
   *<ul compact>
   *<li>Field being set is a variable length field..
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getField() returns the value set
   *</ul>
  **/
  public void Var067()
  {
    try
    {
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "a"));
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
      CharacterFieldDescription v = new CharacterFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), "v");
      v.setVariableLength(true);
      rf.addFieldDescription(v);
      rf.addFieldDescription(new ZonedDecimalFieldDescription(new AS400ZonedDecimal(15, 5), "z"));

      rec = new Record(rf);

      rec.setField(0, "Hello");
      rec.setField(1, Integer.valueOf(10));
      rec.setField(2, "Variable");
      rec.setField(3, new BigDecimal("1.1"));
      // Verify the values set
      if (!((String)rec.getField(0)).equals("Hello"))
      {
        failed("getField() not returning correct value");
        return;
      }
      if (((Integer)rec.getField(1)).intValue() != 10)
      {
        failed("getField() not returning correct value");
        return;
      }
      if (!((String)rec.getField(2)).equals("Variable"))
      {
        failed("getField() not returning correct value");
        return;
      }
      if (!((BigDecimal)rec.getField(3)).toString().equals("1.1"))
      {
        failed("getField() not returning correct value: " + ((BigDecimal)rec.getField(3)).toString());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setField(int, Object)
   *<ul compact>
   *<li>Record format has dependent fields.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getField() returns the values set
   *</ul>
  **/
  public void Var068()
  {
    try
    {
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "a"));
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
      CharacterFieldDescription v = new CharacterFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), "v");
      rf.addFieldDescription(v);
      rf.addFieldDescription(new ZonedDecimalFieldDescription(new AS400ZonedDecimal(15, 5), "z"));
      rf.setLengthDependency(2,1);

      rec = new Record(rf);

      rec.setField(0, "Hello");
      rec.setField(1, Integer.valueOf(10));
      rec.setField(2, "Variable");
      rec.setField(3, new BigDecimal("1.1"));
      // Verify the values set
      if (!((String)rec.getField(0)).equals("Hello"))
      {
        failed("getField() not returning correct value");
        return;
      }
      if (((Integer)rec.getField(1)).intValue() != 10)
      {
        failed("getField() not returning correct value");
        return;
      }
      if (!((String)rec.getField(2)).equals("Variable"))
      {
        failed("getField() not returning correct value");
        return;
      }
      if (!((BigDecimal)rec.getField(3)).toString().equals("1.1"))
      {
        failed("getField() not returning correct value: " + String.valueOf(((BigDecimal)rec.getField(3)).floatValue()));
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setField(int, Object).
   *<ul>
   *<li>Passing index < 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID
   *</ul>
  **/
  public void Var069()
  {
    // Create a record
    rec = r.getNewRecord();
    // Verify exception when setting field with invalid index
    try
    {
      rec.setField(-1, Integer.valueOf(5));
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter passing invalid index");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setField(int, Object).
   *<ul>
   *<li>Passing index > number of fields - 1 
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID
   *</ul>
  **/
  public void Var070()
  {
    // Create a record
    rec = r.getNewRecord();
    // Verify exception when setting field with invalid index
    try
    {
      rec.setField(rec.getNumberOfFields(), Integer.valueOf(5));
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter passing invalid index");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setField(int, Object).
   *<ul>
   *<li>Object type does not match field type 
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ClassCastException
   *</ul>
  **/
  public void Var071()
  {
    // Create a record
    rec = r.getNewRecord();
    // Verify exception when setting field with invalid index
    try
    {
      rec.setField(0, new BigDecimal("5"));
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ClassCastException"))
      {
        failed(e, "Wrong exception/parameter passing invalid object type");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setField(int, Object).
   *<ul>
   *<li>Calling setField(int, Object) before record format has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating "recordFormat" and
   *PROPERTY_NOT_SET
   *</ul>
  **/
  public void Var072()
  {
    try
    {
      Record rc = new Record();
      rc.setField(0, Integer.valueOf(10));
      failed("No exception calling without record format");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling without record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setField(String, Object)
   *<br>
   *Expected results;
   *<ul compact>
   *<li>Fields will be set and getField will return the appropriate values.
   *</ul>
  **/
  public void Var073()
  {
    try
    {
      // Create record with contents set with the contents array
      rec = r.getNewRecord(contents);
      // Set three of the fields
      rec.setField("field1", Integer.valueOf(10));
      rec.setField("field2", "new field");
      rec.setField("field10", new BigDecimal("99999.992"));
      // Verify the correctness of the record
      // Verify the object values returned
      if (((Integer)rec.getField(0)).intValue() != 10)
      {
        failed("0: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(1)).equals("new field"))
      {
        failed("1: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(2)).equals((String)jtArray[2]))
      {
        failed("2: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(3)).equals(pad((String)jtArray[3], 10)))
      {
        failed("3: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(4)).equals((String)jtArray[4]))
      {
        failed("4: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(5)).equals((String)jtArray[5]))
      {
        failed("5: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(6)).equals(pad((String)jtArray[6], 10)))
      {
        failed("6: Wrong value returned.");
        return;
      }

      if (((Double)rec.getField(7)).doubleValue() != ((Double)jtArray[7]).doubleValue())
      {
        failed("7: Wrong value returned.");
        return;
      }

      byte[] val = (byte[])rec.getField(8);
      byte[] chkVal = (byte[])jtArray[8];
      if (val.length != chkVal.length)
      {
        failed("8: Wrong value length returned.");
        return;
      }
      for (int i = 0; i < val.length; ++i)
      {
        if (val[i] != chkVal[i])
        {
          failed("8: Wrong value returned.");
          return;
        }
      }

      if (!((BigDecimal)rec.getField(9)).toString().equals("99999.992"))
      {
        failed("9: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(10)).equals((String)jtArray[10]))
      {
        failed("10: Wrong value returned.");
        return;
      }

      if (!((String)rec.getField(11)).equals((String)jtArray[11]))
      {
        failed("11: Wrong value returned.");
        return;
      }

      if (!((BigDecimal)rec.getField(12)).toString().equals(((BigDecimal)jtArray[12]).toString()))
      {
        failed("12: Wrong value returned.");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
    }
    succeeded();
  }

  /**
   *Verify valid usage of setField(String, Object)
   *<ul>
   *<li>Pass null for Object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getField returns null
   *<li>isNullField returns true
   *</ul>
  **/
  public void Var074()
  {
    try
    {
      // Create record with contents set with the contents array
      rec = r.getNewRecord(contents);
      // Set three of the fields
      Object obj = null;
      rec.setField("field1", obj);
      // Verify the correctness of the record
      // Verify the object values returned
      if (((Integer)rec.getField("field1")) != null)
      {
        failed("field1: null not returned");
        return;
      }
      if (!rec.isNullField("field1"))
      {
        failed("isNullField not returning true.");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setField(String, Object).
   *<ul>
   *<li>Passing invalid field name
   *<li>Passing null for field name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND
   *<li>NullPointerException indicating "name"
   *</ul>
  **/
  public void Var075()
  {
    // Create a record
    rec = r.getNewRecord();
    // Verify exception when setting field with invalid field name
    try
    {
      rec.setField("non existent", Integer.valueOf(5));
      failed("No exception setting field with invalid field name");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed("Wrong exception/parameter passing invalid field name");
        return;
      }
    }

    // Verify exception passing null for field name
    try
    {
      rec.setField(null, Integer.valueOf(5));
      failed("No exception passing null for field name");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong exception/parameter passing null for field name");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setField(String, Object).
   *<ul>
   *<li>Calling setField(int, Object) before record format has been set.
   *</ul>
  **/
  public void Var076()
  {
    try
    {
      Record rc = new Record();
      rc.setField("field1", Integer.valueOf(10));
      failed("No exception calling without record format");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "recordFormat",                       ExtendedIllegalStateException.PROPERTY_NOT_SET))
      {
        failed(e, "Wrong exception/parameter when calling without record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setRecordFormat().
   *<ul compact>
   *<li>Record format not set yet.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format set will be returned from getRecordFormat.
   *</ul>
  **/
  public void Var077()
  {
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "new"));
    try
    {
      rec = new Record();
      rec.setRecordFormat(rf);
      if (rec.getRecordFormat() != rf)
      {
        failed("Wrong record format returned.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setRecordFormat().
   *<ul compact>
   *<li>Record format has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format set will be returned from getRecordFormat.
   *</ul>
  **/
  public void Var078()
  {
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "new"));
    try
    {
      rec = new Record(r);
      rec.setRecordFormat(rf);
      if (rec.getRecordFormat() != rf)
      {
        failed("Wrong record format returned.");
        return;
      }
      if (rec.getRecordLength() != 4)
      {
        failed("Wrong record length returned: " + String.valueOf(rec.getRecordLength()));
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setRecordFormat().
   *<ul compact>
   *<li>Record format = null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var079()
  {
    try
    {
      rec = new Record();
      rec.setRecordFormat(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setRecordFormat().
   *<ul compact>
   *<li>Record format has no fields.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var080()
  {
    try
    {
      rec = new Record();
      rec.setRecordFormat(new RecordFormat());
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setRecordNumber.
   *<ul compact>
   *<li>First time set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordNumber will return the correct record number.
   *</ul>
  **/
  public void Var081()
  {
    // Create a record
    rec = r.getNewRecord();
    // Set the record number
    try
    {
      rec.setRecordNumber(22);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    // Verify record number was set
    if (rec.getRecordNumber() != 22)
    {
      failed("Wrong record number returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setRecordNumber.
   *<ul compact>
   *<li>After it has already been set once.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordNumber will return the correct record number.
   *</ul>
  **/
  public void Var082()
  {
    // Create a record
    rec = r.getNewRecord();
    // Set the record number
    try
    {
      rec.setRecordNumber(22);
      rec.setRecordNumber(1);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    // Verify record number was set
    if (rec.getRecordNumber() != 1)
    {
      failed("Wrong record number returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setRecordNumber.
   *<ul>
   *<li>Passing number < 0 for record number
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordNumber" and
   *RANGE_NOT_VALID
   *</ul>
  **/
  public void Var083()
  {
    // Create a record
    rec = r.getNewRecord();
    // Verify exception when passing < 0 for record number
    try
    {
      rec.setRecordNumber(-1);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordNumber", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter passing < 0 for record number");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setRecordName().
   *<ul compact>
   *<li>Record name not set yet.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record name set will be returned from getRecordName().
   *</ul>
  **/
  public void Var084()
  {
    try
    {
      rec = new Record();
      rec.setRecordName("record");
      if (!rec.getRecordName().equals("record"))
      {
        failed("Wrong record name returned.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setRecordName().
   *<ul compact>
   *<li>Record name has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record name set will be returned from getRecordName().
   *</ul>
  **/
  public void Var085()
  {
    try
    {
      rec = new Record();
      rec.setRecordName("record");
      rec.setRecordName("newRecord");
      if (!rec.getRecordName().equals("newRecord"))
      {
        failed("Wrong record name returned.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setRecordName().
   *<ul compact>
   *<li>Record name = null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "name"
   *</ul>
  **/
  public void Var086()
  {
    try
    {
      rec = new Record();
      rec.setRecordName(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of toString().
   *<ul compact>
   *<li>Record format not set yet.
   *</ul>
   *Expected results:
   *<ul>
   *<li>Empty string returned.
   *</ul>
  **/
  public void Var087()
  {
    try
    {
      rec = new Record();
      String s = rec.toString();
      if (!s.equals(""))
      {
        failed("Empty string not returned.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of toString.
   *<ul compact>
   *<li>Contents have been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>A string containing the properly formatted Java contents of the
   *record is returned.
   *</ul>
  **/
  public void Var088()
  {
    try
    {
      // Create a record initialised with contents arrat
      rec = r.getNewRecord(contents);
      if (!rec.toString().equals(contentsAsString))
      {
        failed("Wrong string returned from toString");
        output_.println("toString()      : " + rec.toString() + ".");
        System.out.println("contentsAsString: " + contentsAsString + ".");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
       return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of toString
   *<ul>
   *<li>Use null for a field.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>"null" will be substituted for the contents of the null field
   *</ul>
  **/
  public void Var089()
  {
    try
    {
      // Create a record initialised with contents arrat
      rec = r.getNewRecord(contents);
      Object obj = null;
      rec.setField(0, obj);
      if (!rec.toString().equals(contentsAsStringWithNull))
      {
        failed("Wrong string returned from toString");
        output_.println("toString()      : " + rec.toString() + ".");
        System.out.println("contentsAsString: " + contentsAsStringWithNull + ".");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
       return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of toString
   *<ul>
   *<li>Contents not set (default record)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The string will be properly formatted with the default values for the
   *fields.
   *</ul>
  **/
  public void Var090()
  {
    try
    {
      // Create a default record 
      rec = r.getNewRecord();
      if (!rec.toString().equals(defaultContents))
      {
        failed("Wrong string returned from toString");
        output_.println("toString()      : " + rec.toString() + ".");
        System.out.println("defaultContents : " + defaultContents + ".");
        return;
      }
    }
    catch(Exception e)
    {
       failed(e, "Unexpected exception");
       return;
    }
    succeeded();
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    try
    {
      // Create the record format for this test class
      r = new RecordFormat();
      r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "field1"));
      r.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field2"));
      r.addFieldDescription(new DateFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "field3"));
      r.addFieldDescription(new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field4"));
      r.addFieldDescription(new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field5"));
      r.addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field6"));
      r.addFieldDescription(new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field7"));
      r.addFieldDescription(new FloatFieldDescription(new AS400Float8(), "field8"));
      r.addFieldDescription(new HexFieldDescription(new AS400ByteArray(3), "field9"));
      r.addFieldDescription(new PackedDecimalFieldDescription(new AS400PackedDecimal(15, 5), "field10"));
      r.addFieldDescription(new TimeFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "field11"));
      r.addFieldDescription(new TimestampFieldDescription(new AS400Text(17, systemObject_.getCcsid(), systemObject_), "field12"));
      r.addFieldDescription(new ZonedDecimalFieldDescription(new AS400ZonedDecimal(10, 5), "field13"));


      // Initialize the number of fields and the record length based on
      // the above record format
      numFields = 13;
      for (short i = 0; i < 13; ++i)
      {
        recLength += ((FieldDescription)r.getFieldDescription(i)).getDataType().getByteLength();
      }

      // Create the byte[] with which to populate the records used in this
      // test class
      contents = new byte[recLength];
      AS400DataType d;
      for (int offset = 0, i = 0; i < 13; ++i)
      {
        d = ((FieldDescription)r.getFieldDescription(i)).getDataType();
        d.toBytes(jtArray[i], contents, offset);
        offset += d.getByteLength();
      }
    }
    catch(Exception e)
    {
      output_.println(e.toString());
      throw e;
    }
  }

  String pad(String str, int length)
  {
    String retStr = str;
    for (int i = str.length(); i < length; ++i)
    {
      retStr += " ";
    }
    return retStr;
  }
}



