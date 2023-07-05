///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFRecord.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.DateFieldDescription;
import com.ibm.as400.access.DBCSEitherFieldDescription;
import com.ibm.as400.access.DBCSGraphicFieldDescription;
import com.ibm.as400.access.DBCSOnlyFieldDescription;
import com.ibm.as400.access.DBCSOpenFieldDescription;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.HexFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.TimestampFieldDescription;
import com.ibm.as400.access.ZonedDecimalFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
  Testcase RFRecord. This test class verifed valid and invalid usage of
  the Record constructors.
**/
public class RFRecord extends Testcase
{
  /**
  Constructor.  This is called from the RFTest constructor.
  **/
  public RFRecord(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    super(systemObject, "RFRecord", 36,
          variationsToRun, runMode, fileOutputStream);
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

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

}


  /**
   *Verify valid usage of Record().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getRecordName() returns the empty string
   *</ul>
  **/
  public void Var001()
  {
    Record r = new Record();
    if (!r.getRecordName().equals(""))
    {
      failed("Empty string not returned from getRecordName.");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of Record(RecordFormat).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() returns the specified record format.
   *<li>getRecordName() returns the empty string
   *</ul>
  **/
  public void Var002()
  {
    RecordFormat r = new RecordFormat();
    // Populate the record format
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    Record rec = new Record(r);
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (!rec.getRecordName().equals(""))
    {
      failed("Empty string not returned for record name");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat).
   *<ul>
   *<li>Passing null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      Record rec = new Record(null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed(e, "Wrong exception/parameter when passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat).
   *<ul>
   *<li>Passing record format with no field descriptions
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      Record rec = new Record(r);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter when passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of Record(RecordFormat, String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() returns the record format specified
   *<li>getRecordName() returns the name specified.
   *</ul>
  **/
  public void Var005()
  {
    RecordFormat r = new RecordFormat();
    // Populate the record format
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    Record rec = new Record(r, "myRecord");
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (!rec.getRecordName().equals("myRecord"))
    {
      failed("Incorrect for record name returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, String).
   *<ul>
   *<li>Passing null for RecordFormat
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var006()
  {
    try
    {
      Record rec = new Record(null, "myRecord");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed(e, "Wrong exception/parameter when passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, String).
   *<ul>
   *<li>Passing record format with no field descriptions
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var007()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      Record rec = new Record(r, "myRecord");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter when passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat).
   *<ul>
   *<li>Passing null for String
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordName"
   *</ul>
  **/
  public void Var008()
  {
    try
    {
      RecordFormat r = new RecordFormat();
      r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
      Record rec = new Record(r, (String)null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordName"))
      {
        failed(e, "Wrong exception/parameter when passing null");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of Record(RecordFormat, byte[]).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() returns the specified record format
   *<li>getRecordName() returns empty string
   *<li>getContents() returns the specified byte array
   *</ul>
  **/
  public void Var009()
  {
    RecordFormat r = new RecordFormat();

    // Populate the record format
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    // Create byte array with which to populate the record
    byte[] b1 = (new AS400Bin4()).toBytes(new Integer(10));
    byte[] b2 = (new AS400Text(10, systemObject_.getCcsid(), systemObject_)).toBytes("abcdefghij");
    byte[] b3 = (new AS400Float8()).toBytes(new Double(123.89));
    byte[] bytes = new byte[22];  // Our byte arrays take up 22 bytes
    System.arraycopy(b1, 0, bytes, 0, b1.length);
    System.arraycopy(b2, 0, bytes, 4, b2.length);
    System.arraycopy(b3, 0, bytes, 14, b3.length);

    try
    {
      Record rec = new Record(r, bytes);
      // Verify the state of rec
      if (rec.getRecordFormat() != r)
      {
        failed("Wrong record format returned after construction");
        return;
      }
      if (!rec.getRecordName().equals(""))
      {
        failed("Empty string not returned for record name");
        return;
      }
      // Verify that the bytes written to rec are correct
      byte[] match = rec.getContents();
      if (match.length != bytes.length)
      {
        failed("Wrong size bye array returned from getContents");
        return;
      }
      else
      {
        for (short i = 0; i < match.length; ++i)
        {
          if (match[i] != bytes[i])
          {
            failed("Wrong bytes returned from getContents");
            return;
          }
        }
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
   *Verify invalid usage of Record(RecordFormat, byte[]).
   *<ul>
   *<li>Passing null for record format
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var010()
  {
    // Verify passing null for record format
    try
    {
      Record rec = new Record(null, new byte[22]);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed(e, "Wrong exception/parameter when passing null for record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[]).
   *<ul>
   *<li>Passing RecordFormat with no fields
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var011()
  {
    try
    {
      Record rec = new Record(new RecordFormat(), new byte[22]);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[]).
   *<ul>
   *<li>Passing null for byte[]
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "contents"
   *</ul>
  **/
  public void Var012()
  {
    // Verify passing null for byte[]
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(rf, (byte[])null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "contents"))
      {
        failed("Wrong exception/parameter when passing null for byte []");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[]).
   *<ul>
   *<li>Passing byte[] of length 0
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "contents" and
   *LENGTH_NOT_VALID
   *</ul>
   *</ul>
  **/
  public void Var013()
  {
    // Verify passing byte[] of length 0
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    byte[] bytes = new byte[0];
    try
    {
      Record rec = new Record(rf, bytes);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter when passing byte [] of length 0");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[]).
   *<ul>
   *<li>byte[] not long enough for record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ArrayIndexOutOfBoundsException
   *</ul>
  **/
  public void Var014()
  {
    // Verify passing too short byte[]
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    byte[] bytes = new byte[1];
    try
    {
      Record rec = new Record(rf, bytes);
      failed("No exception");
      return;
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
    }
    catch(Exception e)
    {
      failed(e, "Wrong exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of Record(RecordFormat, byte[], String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() returns the specified record format
   *<li>getRecordName() returns name specified
   *<li>getContents() returns the specified byte array
   *</ul>
  **/
  public void Var015()
  {
    RecordFormat r = new RecordFormat();

    // Populate the record format
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    // Create byte array with which to populate the record
    byte[] b1 = (new AS400Bin4()).toBytes(new Integer(10));
    byte[] b2 = (new AS400Text(10, systemObject_.getCcsid(), systemObject_)).toBytes("abcdefghij");
    byte[] b3 = (new AS400Float8()).toBytes(new Double(123.89));
    byte[] bytes = new byte[22];  // Our byte arrays take up 22 bytes
    System.arraycopy(b1, 0, bytes, 0, b1.length);
    System.arraycopy(b2, 0, bytes, 4, b2.length);
    System.arraycopy(b3, 0, bytes, 14, b3.length);

    try
    {
      Record rec = new Record(r, bytes, "myRecord");
      // Verify the state of rec
      if (rec.getRecordFormat() != r)
      {
        failed("Wrong record format returned after construction");
        return;
      }
      if (!rec.getRecordName().equals("myRecord"))
      {
        failed("Wrong name returned for record name");
        return;
      }
      // Verify that the bytes written to rec are correct
      byte[] match = rec.getContents();
      if (match.length != bytes.length)
      {
        failed("Wrong size bye array returned from getContents");
        return;
      }
      else
      {
        for (short i = 0; i < match.length; ++i)
        {
          if (match[i] != bytes[i])
          {
            failed("Wrong bytes returned from getContents");
            return;
          }
        }
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
   *Verify invalid usage of Record(RecordFormat, byte[], String).
   *<ul>
   *<li>Passing null for record format
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var016()
  {
    // Verify passing null for record format
    try
    {
      Record rec = new Record(null, new byte[22], "myrec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed(e, "Wrong exception/parameter when passing null for record format");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], String).
   *<ul>
   *<li>Passing RecordFormat with no fields
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var017()
  {
    try
    {
      Record rec = new Record(new RecordFormat(), new byte[22], "myrec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], String).
   *<ul>
   *<li>Passing null for byte[]
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "contents"
   *</ul>
  **/
  public void Var018()
  {
    // Verify passing null for byte[]
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(rf, (byte[])null, "myrec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "contents"))
      {
        failed("Wrong exception/parameter when passing null for byte []");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], String).
   *<ul>
   *<li>Passing byte[] of length 0
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "contents" and
   *LENGTH_NOT_VALID
   *</ul>
   *</ul>
  **/
  public void Var019()
  {
    // Verify passing byte[] of length 0
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    byte[] bytes = new byte[0];
    try
    {
      Record rec = new Record(rf, bytes, "myrec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Wrong exception/parameter when passing byte [] of length 0");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], String).
   *<ul>
   *<li>byte[] not long enough for record
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ArrayIndexOutOfBoundsException
   *</ul>
  **/
  public void Var020()
  {
    // Verify passing too short byte[]
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    byte[] bytes = new byte[1];
    try
    {
      Record rec = new Record(rf, bytes, "myrec");
      failed("No exception");
      return;
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
    }
    catch(Exception e)
    {
      failed(e, "Wrong exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], String).
   *<ul>
   *<li>null for record name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ArrayIndexOutOfBoundsException
   *</ul>
  **/
  public void Var021()
  {
    // Verify passing too short byte[]
    RecordFormat rf = new RecordFormat();
    rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    byte[] bytes = new byte[2];
    try
    {
      Record rec = new Record(rf, bytes, null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordName"))
      {
        failed(e, "Wrong exception/info");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of Record(RecordFormat, byte[], int offset).
  **/
  public void Var022()
  {
    RecordFormat r = new RecordFormat();

    // Populate the record format
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    // Create byte array with which to populate the record
    byte[] b1 = (new AS400Bin4()).toBytes(new Integer(10));
    byte[] b2 = (new AS400Text(10, systemObject_.getCcsid(), systemObject_)).toBytes("abcdefghij");
    byte[] b3 = (new AS400Float8()).toBytes(new Double(123.89));
    byte[] bytes = new byte[32];  // Our byte arrays take up 22 bytes
    System.arraycopy(b1, 0, bytes, 5, b1.length);
    System.arraycopy(b2, 0, bytes, 9, b2.length);
    System.arraycopy(b3, 0, bytes, 19, b3.length);

    try
    {
      Record rec = new Record(r, bytes, 5);
      // Verify the state of rec
      if (rec.getRecordFormat() != r)
      {
        failed("Wrong record format returned after construction");
        return;
      }
      // Verify that the bytes written to rec are correct
      byte[] match = rec.getContents();
      if (match.length != bytes.length - 10)
      {
        failed("Wrong size byte array returned from getContents");
        return;
      }
      else
      {
        for (short i = 0; i < match.length; ++i)
        {
          if (match[i] != bytes[i + 5])
          {
            failed("Wrong bytes returned from getContents");
            return;
          }
        }
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
   *Verify invalid usage of Record(RecordFormat, byte[], int offset).
   *<ul>
   *<li>Passing null for record format
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var023()
  {
    // Verify passing null for record format
    try
    {
      Record rec = new Record(null, new byte[22], 0);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset).
   *<ul>
   *<li>Passing RecordFormat with no fields
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var024()
  {
    // Verify passing no RecordFormat fields
    try
    {
      Record rec = new Record(new RecordFormat(), new byte[22], 0);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset).
   *<ul>
   *<li>Passing null for byte[]
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "contents"
   *</ul>
  **/
  public void Var025()
  {
    // Verify passing null for record format
    // Verify passing null for byte[]
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, null, 0);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "contents"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset).
   *<ul>
   *<li>Passing byte[] of length 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "contents" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var026()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    // Verify passing byte[] of length 0
    byte[] bytes = new byte[0];
    try
    {
      Record rec = new Record(r, bytes, 0);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset).
   *<ul>
   *<li>Passing int < 0 for offset
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "offset" and
   *RANGE_NOT_VALID
   *</ul>
  **/
  public void Var027()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    // Verify passing offset < 0
    r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, new byte[22], -1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "offset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset).
   *<ul>
   *<li>Passing offset > size of byte[] - record length
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ArrayIndexOutOfBoundsException
   *</ul>
  **/
  public void Var028()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    // Verify passing offset > size of byte[] - record length
    r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, new byte[22], 21);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ArrayIndexOutOfBoundsException"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of Record(RecordFormat, byte[], int offset, String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() returns the record format specified
   *<li>getRecordName9) returns the name specified
   *<li>getContents() returns the contents specified
   *</ul>
  **/
  public void Var029()
  {
    RecordFormat r = new RecordFormat();

    // Populate the record format
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    // Create byte array with which to populate the record
    byte[] b1 = (new AS400Bin4()).toBytes(new Integer(10));
    byte[] b2 = (new AS400Text(10, systemObject_.getCcsid(), systemObject_)).toBytes("abcdefghij");
    byte[] b3 = (new AS400Float8()).toBytes(new Double(123.89));
    byte[] bytes = new byte[32];  // Our byte arrays take up 22 bytes
    System.arraycopy(b1, 0, bytes, 5, b1.length);
    System.arraycopy(b2, 0, bytes, 9, b2.length);
    System.arraycopy(b3, 0, bytes, 19, b3.length);

    try
    {
      Record rec = new Record(r, bytes, 5, "myRecord");
      // Verify the state of rec
      if (rec.getRecordFormat() != r)
      {
        failed("Wrong record format returned after construction");
        return;
      }
      if (!rec.getRecordName().equals("myRecord"))
      {
        failed("Incorrect record name returned");
        return;
      }
      // Verify that the bytes written to rec are correct
      byte[] match = rec.getContents();
      if (match.length != bytes.length - 10)
      {
        failed("Wrong size bye array returned from getContents");
        return;
      }
      else
      {
        for (short i = 0; i < match.length; ++i)
        {
          if (match[i] != bytes[i + 5])
          {
            failed("Wrong bytes returned from getContents");
            return;
          }
        }
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
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing null for record format
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordFormat"
   *</ul>
  **/
  public void Var030()
  {
    // Verify passing null for record format
    try
    {
      Record rec = new Record(null, new byte[22], 0, "myRec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordFormat"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing RecordFormat with no fields
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "recordFormat" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var031()
  {
    // Verify passing no RecordFormat fields
    try
    {
      Record rec = new Record(new RecordFormat(), new byte[22], 0, "myRec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "recordFormat", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing null for byte[]
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "contents"
   *</ul>
  **/
  public void Var032()
  {
    // Verify passing null for record format
    // Verify passing null for byte[]
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, null, 0, "myRec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "contents"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing byte[] of length 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "contents" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var033()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    // Verify passing byte[] of length 0
    byte[] bytes = new byte[0];
    try
    {
      Record rec = new Record(r, bytes, 0, "myRec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing int < 0 for offset
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "offset" and
   *RANGE_NOT_VALID
   *</ul>
  **/
  public void Var034()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    // Verify passing offset < 0
    r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, new byte[22], -1, "myRec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "offset", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing offset > size of byte[] - record length
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ArrayIndexOutOfBoundsException
   *</ul>
  **/
  public void Var035()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    // Verify passing offset > size of byte[] - record length
    r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, new byte[22], 21, "myRec");
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ArrayIndexOutOfBoundsException"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of Record(RecordFormat, byte[], int offset, String).
   *<ul>
   *<li>Passing null for recordName
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "recordName"
   *</ul>
  **/
  public void Var036()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), "fld"));
    try
    {
      Record rec = new Record(r, new byte[22], 20, null);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "recordName"))
      {
        failed("Wrong exception/parameter");
        return;
      }
    }
    succeeded();
  }
}



