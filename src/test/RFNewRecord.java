///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFNewRecord.java
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
  Testcase RFNewRecord.  This test class verifies the valid and invalid usage
  of the RecordFormat.getNewRecord() methods.  A very basic byte array is
  used for verifying the contents of the record returned by the getNewRecord
  methods.  See the RFRecord and RFRecordMisc testcases for a thorough testing
  of a variety of records.  E.g. dependent fields, variable length fields and
  usage of all possible field description types.
**/
public class RFNewRecord extends Testcase
{
  /**
   Constructor.  This is called from the RFTest constructor.
   **/
public RFNewRecord(AS400            systemObject,
                   Vector           variationsToRun,
                   int              runMode,
                   FileOutputStream fileOutputStream)
{
  // Replace the third parameter (3) with the total number of variations
  // in this testcase.
  super(systemObject, "RFNewRecord", 35,
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

}

/**
 *Verify valid usage of getNewRecord().
 *<ul compact>
 *<li>getNewRecord() from empty record format.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>Null is returned when record format is empty
 *</ul>
**/
public void Var001()
{
  RecordFormat r = new RecordFormat();

  // Verify null returned when record format is empty
  Record rec = r.getNewRecord();
  if (rec != null)
  {
    failed("Null record not returned from empty record format");
    return;
  }
  succeeded();
}

/**
 *Verify valid usage of getNewRecord().
 *<ul compact>
 *<li>getNewRecord() from record format with at least one field.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns the empty string.
 *</ul>
**/
public void Var002()
{
  RecordFormat r = new RecordFormat();
  // Populate the record format and then verify the record returned.
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
  FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

  r.addFieldDescription(b);
  r.addFieldDescription(c);
  r.addFieldDescription(f);

  Record rec = r.getNewRecord();
  // Verify the state of rec
  if (rec.getRecordFormat() != r)
  {
    failed("Wrong record format returned after construction");
    return;
  }
  if (rec.getNumberOfFields() != r.getNumberOfFields())
  {
    failed("Wrong number of fields after construction");
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
 *Verify valid usage of getNewRecord(String).
 *<ul compact>
 *<li>getNewRecord(String) from empty record format.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>Null is returned when record format is empty
 *<li>getRecordName() returns the name specified on the getNewRecord() call.
 *</ul>
 **/
public void Var003()
{
  RecordFormat r = new RecordFormat();

  // Verify null returned when record format is empty
  Record rec = r.getNewRecord("myRecord");
  if (rec != null)
  {
    failed("Null record not returned from empty record format");
    return;
  }
  succeeded();
}

/**
 *Verify valid usage of getNewRecord(String).
 *<ul compact>
 *<li>getNewRecord(String) from record format with at least one field.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns the name specified on the getNewRecord() call..
 *</ul>
 **/
public void Var004()
{
  RecordFormat r = new RecordFormat();

  // Populate the record format and then verify the record returned.
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
  FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

  r.addFieldDescription(b);
  r.addFieldDescription(c);
  r.addFieldDescription(f);

  Record rec = r.getNewRecord("myRecord");
  // Verify the state of rec
  if (rec.getRecordFormat() != r)
  {
    failed("Wrong record format returned after construction");
    return;
  }
  if (rec.getNumberOfFields() != r.getNumberOfFields())
  {
    failed("Wrong number of fields after construction");
    return;
  }
  if (!rec.getRecordName().equals("myRecord"))
  {
    failed("Wrong record name returned");
    return;
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(String)
 *<ul>
 *<li>Passing null for String
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "recordName".
 *</ul>
 **/
public void Var005()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);
  // Verify NullPointerException when null passed for record name
  try
  {
    Record rec = r.getNewRecord((String)null);
    failed("No exception when passing null for record name");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "recordName"))
    {
      failed(e, "Wrong exception/paramter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify valid usage of getNewRecord(byte[]).
 *<ul>
 *<li>getNewRecord(byte[]) from empty record format
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>Null is returned.
 *</ul>
 **/
public void Var006()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));

  // Verify null returned when record format is empty
  try
  {
    Record rec = r.getNewRecord(bytes);
    if (rec != null)
    {
      failed("Null not returned when record format is empty");
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
 *Verify valid usage of getNewRecord(byte[]).
 *<ul>
 *<li>getNewRecord(byte[]) from record format with one field only
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns empty string.
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var007()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));
  // Verify record returned from record format with one field
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);
  try
  {
    Record rec = r.getNewRecord(bytes);
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 1)
    {
      failed("Wrong number of fields after construction");
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
      failed("Wrong size byte array returned from getContents");
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
    // Verify the java contents of the field
    if (((Integer)rec.getField(0)).intValue() != 5)
    {
      failed("Wrong value returned from getField()" + (Integer)rec.getField(0));
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
 *Verify valid usage of getNewRecord(byte[]).
 *<ul>
 *<li>getNewRecord(byte[]) from record format with more than one field
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns empty string.
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var008()
{
  RecordFormat r = new RecordFormat();
  // Verify record returned from record format with three fields
  // Add two more fields to the record format
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
    Record rec = r.getNewRecord(bytes);
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 3)
    {
      failed("Wrong number of fields after construction");
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
      failed("Wrong size byte array returned from getContents");
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
    // Verify the java contents of the fields
    if (((Integer)rec.getField(0)).intValue() != 10)
    {
      failed("Wrong value returned from getField(): " + (Integer)rec.getField(0));
      return;
    }
    else if (!((String)rec.getField(1)).equals("abcdefghij"))
    {
      failed("Wrong value returned from getField(): " + (String)rec.getField(0));
      return;
    }
    else if (((Double)rec.getField(2)).doubleValue() != 123.89)
    {
      failed("Wrong value returned from getField()" + (Double)rec.getField(0));
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
 *Verify invalid usage of getNewRecord(byte[]).
 *<ul>
 *<li>Passing null for byte[]
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "contents".
 *</ul>
 **/
public void Var009()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify NullPointerException when passing null
  try
  {
    rec = r.getNewRecord((byte[])null);
    failed("No exception passing null");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "contents"))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[]).
 *<ul>
 *<li>Passing byte array of length 0
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating "contents" and
 *LENGTH_NOT_VALID
 *</ul>
 **/
public void Var010()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  // Verify ExtendedIllegalArgument exception when passing byte[] of length 0
  try
  {
    byte[] bytes = new byte[0];
    Record rec = r.getNewRecord(bytes);
    failed("No exception passing 0 length byte array");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[]).
 *<ul>
 *<li>Passing byte[] of length less than what is needed for the record
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ArrayIndexOutOfBoundsException.
 *</ul>
 **/
public void Var011()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[2];
    rec = r.getNewRecord(bytes);
    failed("No exception passing too short byte array");
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
 *Verify valid usage of getNewRecord(byte[], String).
 *<ul>
 *<li>getNewRecord(byte[], String) from empty record format
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>Null is returned.
 *</ul>
 **/
public void Var012()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));

  // Verify null returned when record format is empty
  try
  {
    Record rec = r.getNewRecord(bytes, "name");
    if (rec != null)
    {
      failed("Null not returned when record format is empty");
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
 *Verify valid usage of getNewRecord(byte[], String).
 *<ul>
 *<li>getNewRecord(byte[], String) from record format with one field only
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns name specified on method call.
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var013()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));
  // Verify record returned from record format with one field
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);
  try
  {
    Record rec = r.getNewRecord(bytes, "myRecord");
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 1)
    {
      failed("Wrong number of fields after construction");
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
      failed("Wrong size byte array returned from getContents");
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
    // Verify the java contents of the field
    if (((Integer)rec.getField(0)).intValue() != 5)
    {
      failed("Wrong value returned from getField()" + (Integer)rec.getField(0));
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
 *Verify valid usage of getNewRecord(byte[], String).
 *<ul>
 *<li>getNewRecord(byte[], String) from record format with more than one field
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns name specified on method call.
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var014()
{
  RecordFormat r = new RecordFormat();
  // Verify record returned from record format with three fields
  // Add two more fields to the record format
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
    Record rec = r.getNewRecord(bytes, "myRecord");
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 3)
    {
      failed("Wrong number of fields after construction");
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
      failed("Wrong size byte array returned from getContents");
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
    // Verify the java contents of the fields
    if (((Integer)rec.getField(0)).intValue() != 10)
    {
      failed("Wrong value returned from getField(): " + (Integer)rec.getField(0));
      return;
    }
    else if (!((String)rec.getField(1)).equals("abcdefghij"))
    {
      failed("Wrong value returned from getField(): " + (String)rec.getField(0));
      return;
    }
    else if (((Double)rec.getField(2)).doubleValue() != 123.89)
    {
      failed("Wrong value returned from getField()" + (Double)rec.getField(0));
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
 *Verify invalid usage of getNewRecord(byte[], String).
 *<ul>
 *<li>Passing null for byte[]
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "contents".
 *</ul>
 **/
public void Var015()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify NullPointerException when passing null
  try
  {
    rec = r.getNewRecord((byte[])null, "myRecord");
    failed("No exception passing null");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "contents"))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], String).
 *<ul>
 *<li>Passing byte array of length 0
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating "contents" and
 *LENGTH_NOT_VALID
 *</ul>
 **/
public void Var016()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  // Verify ExtendedIllegalArgument exception when passing byte[] of length 0
  try
  {
    byte[] bytes = new byte[0];
    Record rec = r.getNewRecord(bytes, "myRecord");
    failed("No exception passing 0 length byte array");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], String).
 *<ul>
 *<li>Passing byte[] of length less than what is needed for the record
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ArrayIndexOutOfBoundsException.
 *</ul>
 **/
public void Var017()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[2];
    rec = r.getNewRecord(bytes, "MyRec");
    failed("No exception passing too short byte array");
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
 *Verify invalid usage of getNewRecord(byte[], String)
 *<ul>
 *<li>Passing null for String
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "recordName".
 *</ul>
 **/
public void Var018()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify NullPointerException when null passed for record name
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));
  try
  {
    rec = r.getNewRecord(bytes, null);
    failed("No exception when passing null for record name");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "recordName"))
    {
      failed("Wrong exception/paramter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify valid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>getNewRecord(byte[], int) from empty record format
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>Null is returned when record format is empty
 *</ul>
 **/
public void Var019()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] contents = (new AS400Bin4()).toBytes(new Integer(5));
  byte[] bytes = new byte[contents.length + 10];
  System.arraycopy(contents, 0, bytes, 5, contents.length);

  // Verify null returned when record format is empty
  try
  {
    Record rec = r.getNewRecord(bytes, 5);
    if (rec != null)
    {
      failed("Null not returned when record format is empty");
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
 *Verify valid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>getNewRecord(byte[], int) from record format with one field only
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns empty string.
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var020()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] contents = (new AS400Bin4()).toBytes(new Integer(5));
  byte[] bytes = new byte[contents.length + 10];
  System.arraycopy(contents, 0, bytes, 5, contents.length);
  // Verify record returned from record format with one field
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);
  try
  {
    Record rec = r.getNewRecord(bytes, 5);
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 1)
    {
      failed("Wrong number of fields after construction");
      return;
    }
    if (!rec.getRecordName().equals(""))
    {
      failed("Empty string not returned for record name");
      return;
    }
    // Verify that the bytes written to rec are correct
    byte[] match = rec.getContents();
    if (match.length != (bytes.length - 10))
    {
      failed("Wrong size byte array returned from getContents, match: " + String.valueOf(match.length) + "  bytes.length - 10: " + String.valueOf(bytes.length - 10));
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
    // Verify the java contents of the field
    if (((Integer)rec.getField(0)).intValue() != 5)
    {
      failed("Wrong value returned from getField()" + (Integer)rec.getField(0));
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
 *Verify valid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>getNewRecord(byte[], int) from record format with more than one field
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns an empty string.
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var021()
{
  RecordFormat r = new RecordFormat();
  // Verify record returned from record format with three fields
  // Add two more fields to the record format
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
  byte[] bytes = new byte[22 + 10];  // Our byte arrays take up 22 bytes
  System.arraycopy(b1, 0, bytes, 5, b1.length);
  System.arraycopy(b2, 0, bytes, 9, b2.length);
  System.arraycopy(b3, 0, bytes, 19, b3.length);
  try
  {
    Record rec = r.getNewRecord(bytes, 5);
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 3)
    {
      failed("Wrong number of fields after construction");
      return;
    }
    if (!rec.getRecordName().equals(""))
    {
      failed("Empty string not returned for record name");
      return;
    }
    // Verify that the bytes written to rec are correct
    byte[] match = rec.getContents();
    if (match.length != (bytes.length - 10))
    {
      failed("Wrong size byte array returned from getContents, match: " + String.valueOf(match.length) + "  bytes.length - 10: " + String.valueOf(bytes.length - 10));
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
    // Verify the java contents of the fields
    if (((Integer)rec.getField(0)).intValue() != 10)
    {
      failed("Wrong value returned from getField()" + (Integer)rec.getField(0));
      return;
    }
    else if (!((String)rec.getField(1)).equals("abcdefghij"))
    {
      failed("Wrong value returned from getField()" + (String)rec.getField(0));
      return;
    }
    else if (((Double)rec.getField(2)).doubleValue() != 123.89)
    {
      failed("Wrong value returned from getField()" + (Double)rec.getField(0));
      return;
    }
  }
  catch(Exception e)
  {
    failed(e, "Unexepected exception.");
    return;
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>Passing null for byte[]
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "contents".
 *</ul>
 **/
public void Var022()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify NullPointerException when passing null
  try
  {
    rec = r.getNewRecord((byte[])null, 0);
    failed("No exception passing null");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "contents"))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>Passing byte array of length 0
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating "contents" and
 *LENGTH_NOT_VALID
 *</ul>
 **/
public void Var023()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  // Verify ExtendedIllegalArgument exception when passing byte[] of length 0
  try
  {
    byte[] bytes = new byte[0];
    Record rec = r.getNewRecord(bytes, 0);
    failed("No exception passing 0 length byte array");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>Passing byte[] of length less than what is needed for the record
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ArrayIndexOutOfBoundsException.
 *</ul>
 **/
public void Var024()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[2];
    rec = r.getNewRecord(bytes, 0);
    failed("No exception passing too short byte array");
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
 *Verify invalid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>Passing offset < 0.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating "offset" and
 *PARAMETER_VALUE_NOT_VALID
 *</ul>
 **/
public void Var025()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[2];
    rec = r.getNewRecord(bytes, -1);
    failed("No exception offset = -1");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "offset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int).
 *<ul>
 *<li>Passing offset that is greater than byte[].length - record length
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ArrayIndexOutOfBoundsException.
 *</ul>
 **/
public void Var026()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[8];
    rec = r.getNewRecord(bytes, 6);
    failed("No exception passing too large offset");
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
 *Verify valid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>getNewRecord(byte[], int, String) from empty record format
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>Null is returned when record format is empty
 *</ul>
 **/
public void Var027()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));

  // Verify null returned when record format is empty
  try
  {
    Record rec = r.getNewRecord(bytes, 0, "myRecord");
    if (rec != null)
    {
      failed("Null not returned when record format is empty");
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
 *Verify valid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>getNewRecord(byte[], int, String) from record format with one field only
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns the name specified on the getNewRecord() call..
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var028()
{
  RecordFormat r = new RecordFormat();
  // Create byte array with which to populate the record
  byte[] contents = (new AS400Bin4()).toBytes(new Integer(5));
  byte[] bytes = new byte[contents.length + 10];
  System.arraycopy(contents, 0, bytes, 5, contents.length);
  // Verify record returned from record format with one field
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);
  try
  {
    Record rec = r.getNewRecord(bytes, 5, "myRecord");
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 1)
    {
      failed("Wrong number of fields after construction");
      return;
    }
    if (!rec.getRecordName().equals("myRecord"))
    {
      failed("Wrong record name returned");
      return;
    }

    // Verify that the bytes written to rec are correct
    byte[] match = rec.getContents();
    if (match.length != (bytes.length - 10))
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
    // Verify the java contents of the field
    if (((Integer)rec.getField(0)).intValue() != 5)
    {
      failed("Wrong value returned from getField()" + (Integer)rec.getField(0));
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
 *Verify valid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>getNewRecord(byte[], int, String) from record format with more than one
 *field.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>getRecordFormat() returns the record format object upon which
 *getNewRecord() was invoked
 *<li>Record.getNumberOfFields() = RecordFormat.getNumberOfFields
 *<li>getRecordName() returns the name specified on the getNewRecord() call..
 *<li>The contents of the Record match the contents passed in to getNewRecord()
 *</ul>
 **/
public void Var029()
{
  RecordFormat r = new RecordFormat();
  // Verify record returned from record format with three fields
  // Add two more fields to the record format
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
  byte[] bytes = new byte[22 + 10];  // Our byte arrays take up 22 bytes
  System.arraycopy(b1, 0, bytes, 5, b1.length);
  System.arraycopy(b2, 0, bytes, 9, b2.length);
  System.arraycopy(b3, 0, bytes, 19, b3.length);
  try
  {
    Record rec = r.getNewRecord(bytes, 5, "myRecord");
    // Verify the state of rec
    if (rec.getRecordFormat() != r)
    {
      failed("Wrong record format returned after construction");
      return;
    }
    if (rec.getNumberOfFields() != r.getNumberOfFields() &&
        rec.getNumberOfFields() != 3)
    {
      failed("Wrong number of fields after construction");
      return;
    }
    if (!rec.getRecordName().equals("myRecord"))
    {
      failed("Wrong record name returned");
      return;
    }

    // Verify that the bytes written to rec are correct
    byte[] match = rec.getContents();
    if (match.length != (bytes.length - 10))
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
    // Verify the java contents of the fields
    if (((Integer)rec.getField(0)).intValue() != 10)
    {
      failed("Wrong value returned from getField()" + (Integer)rec.getField(0));
      return;
    }
    else if (!((String)rec.getField(1)).equals("abcdefghij"))
    {
      failed("Wrong value returned from getField()" + (String)rec.getField(0));
      return;
    }
    else if (((Double)rec.getField(2)).doubleValue() != 123.89)
    {
      failed("Wrong value returned from getField()" + (Double)rec.getField(0));
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
 *Verify invalid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>Passing null for byte[]
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "contents".
 *</ul>
 **/
public void Var030()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify NullPointerException when passing null
  try
  {
    rec = r.getNewRecord((byte[])null, 0, "myRec");
    failed("No exception passing null");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "contents"))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>Passing byte array of length 0
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating "contents" and
 *LENGTH_NOT_VALID
 *</ul>
 **/
public void Var031()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  // Verify ExtendedIllegalArgument exception when passing byte[] of length 0
  try
  {
    byte[] bytes = new byte[0];
    Record rec = r.getNewRecord(bytes, 0, "theRec");
    failed("No exception passing 0 length byte array");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "contents", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>Passing byte[] of length less than what is needed for the record
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ArrayIndexOutOfBoundsException.
 *</ul>
 **/
public void Var032()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[2];
    rec = r.getNewRecord(bytes, 0, "Arecord");
    failed("No exception passing too short byte array");
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
 *Verify invalid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>Passing offset < 0.
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ExtendedIllegalArgumentException indicating "offset" and
 *PARAMETER_VALUE_NOT_VALID
 *</ul>
 **/
public void Var033()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[2];
    rec = r.getNewRecord(bytes, -1, "MyRec");
    failed("No exception offset = -1");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "offset", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
    {
      failed(e, "Wrong exception/parameter thrown");
      return;
    }
  }
  succeeded();
}

/**
 *Verify invalid usage of getNewRecord(byte[], int, String).
 *<ul>
 *<li>Passing offset that is greater than byte[].length - record length
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>ArrayIndexOutOfBoundsException.
 *</ul>
 **/
public void Var034()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify ArrayIndexOutOfBounds exception when passing byte[] of length 2
  try
  {
    byte[] bytes = new byte[8];
    rec = r.getNewRecord(bytes, 6, "Blah");
    failed("No exception passing too large offset");
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
 *Verify invalid usage of getNewRecord(byte[], int, String)
 *<ul>
 *<li>Passing null for String
 *</ul>
 *Expected results:
 *<ul compact>
 *<li>NullPointerException with getMessage() returning "recordName".
 *</ul>
 **/
public void Var035()
{
  RecordFormat r = new RecordFormat();
  BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
  r.addFieldDescription(b);

  Record rec;
  // Verify NullPointerException when null passed for record name
  byte[] bytes = (new AS400Bin4()).toBytes(new Integer(5));
  try
  {
    rec = r.getNewRecord(bytes, 0, null);
    failed("No exception when passing null for record name");
    return;
  }
  catch(Exception e)
  {
    if (!exceptionIs(e, "NullPointerException", "recordName"))
    {
      failed("Wrong exception/paramter thrown");
      return;
    }
  }
  succeeded();
}

}



