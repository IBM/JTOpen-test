///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMSetValuesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RM;


import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400Date;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Time;
import com.ibm.as400.access.AS400Timestamp;
import com.ibm.as400.access.AS400UnsignedBin8;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryConverter;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.HexFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ZonedDecimalFieldDescription;
import com.ibm.as400.data.RecordFormatDocument;

import test.Testcase;

/**
 The RMSetValuesTestcase class tests the following methods of the RecordFormatDocument class:
 <li>setValues(formatName,record)
 <li>setValues(formatName,byte[])
 **/
public class RMSetValuesTestcase extends Testcase
{
  static final boolean DEBUG = true; ///
    /**
     Constructor.
     **/
    public RMSetValuesTestcase(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMSetValuesTestcase", namesAndVars.get("RMSetValuesTestcase"), runMode, fileOutputStream);
    }

    /**
     Test setValues(formatName,record).  Pass in an empty string for formatName. Ensure error is generated.
     **/
    public void Var001()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        recFmt.setRecordFormatID("id1");
        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("", rec);
        failed("Did not throw exception.");
      }
      catch (Exception e)
      {
        String expectedMsg = "<recordformat> element named '' not found in document";
        String receivedMsg = e.getMessage();
        if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }


    /**
     Test setValues(formatName,record).  Pass in null for formatName. Should get null pointer exception.
     **/
    public void Var002()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        recFmt.setRecordFormatID("id1");
        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues((String)null, rec);
        failed("Did not throw exception.");
      }
      catch (Exception e)
      {
        assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
      }
    }

// Note: I don't know how to do this.  
//    /**
//     Test setValues(formatName,record) with a formatName that maps to an empty &lt;recordformat&gt;
//     tag in the rfml document. Ensure an empty record created.
//     **/
//    public void Var003()
//    {
//        try
//        {
//        }
//        catch (Exception e)
//        {
//            failed(e, "Unexpected exception");
//        }
//    }

    /**
     Test setValues(formatName,record).  Pass an invalid formatName that does not map to a &lt;recordformat&gt; tag. Ensure error is generated.
     **/
    public void Var003()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        recFmt.setRecordFormatID("id1");
        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);
        rfmlDoc.setValues("id1", rec);  // specify the ID instead of the name.
        failed("Did not throw exception.");
      }
      catch (Exception e)
      {
        String expectedMsg = "<recordformat> element named 'id1' not found in document";
        String receivedMsg = e.getMessage();
        if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

    /**
     Test setValues(formatName,record).  Pass null record in. Ensure error is generated.
     **/
    public void Var004()
    {
	

      try
      {
        RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtEmpty");
        rfmlDoc.setValues("format1", (Record)null);
        failed("Did not throw exception.");
      }
      catch (Exception e)
      {
        assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
      }
    }

    /**
     Test setValues(formatName,record).  Pass empty default record in. Ensure error is generated.
     **/
    public void Var005()
    {
      try
      {
        // Make a Record.
        Record rec = new Record();

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        failed("Did not throw exception.");
      }
      catch (Exception e)
      {
        String expectedMsg = "The Record object is not initialized";
        String receivedMsg = e.getMessage();
        if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that does not match format of the record passed in. Ensure an error is generated.
     **/
    public void Var006()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        recFmt.setRecordFormatID("id1");
        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

        rfmlDoc.setValues("FORMAT2", rec);
        failed("Did not throw exception.");
      }
      catch (Exception e)
      {
        String expectedMsg = "<recordformat> element named 'FORMAT2' not found in document";
        String receivedMsg = e.getMessage();
        if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=char but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var007()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        recFmt.setRecordFormatID("id1");
        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        String field1Val = (String)rfmlDoc.getValue("format1.field1");
        assertCondition(field1Val.equals(""));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=char and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var008()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        recFmt.setRecordFormatID("id1");
        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        field1.setDFT("Val1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        String field1Val = (String)rfmlDoc.getValue("format1.field1");
        assertCondition(field1Val.equals("Val1"));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=int but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var009()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        BinaryFieldDescription field1 = new BinaryFieldDescription(new AS400Bin2(), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        int field1Val = rfmlDoc.getIntValue("format1.field1");
        assertCondition(field1Val == 0);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=int and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var010()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        BinaryFieldDescription field1 = new BinaryFieldDescription(new AS400Bin2(), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", new Short("567"));

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        int field1Val = rfmlDoc.getIntValue("format1.field1");
        assertCondition(field1Val == 567);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=zoned but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var011()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        ZonedDecimalFieldDescription field1 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        double field1Val = rfmlDoc.getDoubleValue("format1.field1");
        assertCondition(field1Val == 0.0);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=zoned and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var012()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        ZonedDecimalFieldDescription field1 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", new BigDecimal("56.78"));

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        double field1Val = rfmlDoc.getDoubleValue("format1.field1");

        assertCondition(field1Val == 56.78);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=packed but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var013()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        PackedDecimalFieldDescription field1 = new PackedDecimalFieldDescription(new AS400PackedDecimal(6,2), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        double field1Val = rfmlDoc.getDoubleValue("format1.field1");
        assertCondition(field1Val == 0.0);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=packed and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var014()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        PackedDecimalFieldDescription field1 = new PackedDecimalFieldDescription(new AS400PackedDecimal(6,2), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", new BigDecimal("1234.56"));

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        BigDecimal field1Val = (BigDecimal)rfmlDoc.getValue("format1.field1");
        assertCondition(field1Val.toString().equals("1234.56"));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=float but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var015()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        FloatFieldDescription field1 = new FloatFieldDescription(new AS400Float4(), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        double field1Val = rfmlDoc.getDoubleValue("format1.field1");
        assertCondition(field1Val == 0.0);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=float and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var016()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        FloatFieldDescription field1 = new FloatFieldDescription(new AS400Float4(), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", new Float(1.2e3));

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        double field1Val = rfmlDoc.getDoubleValue("format1.field1");
        if (DEBUG) System.out.println("field1Val == " + field1Val);
        assertCondition(field1Val == 1.2e3);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=binary but does not have the contents of the record set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var017()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        BinaryFieldDescription field1 = new BinaryFieldDescription(new AS400Bin4(), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        int field1Val = rfmlDoc.getIntValue("format1.field1");
        assertCondition(field1Val == 0);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=binary and that contains
     a valid value. Ensure the value for the field is set properly.
     **/

    public void Var018()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        BinaryFieldDescription field1 = new BinaryFieldDescription(new AS400Bin4(), "field1");
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", new Integer(-3456));

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        int field1Val = rfmlDoc.getIntValue("format1.field1");
        assertCondition(field1Val == -3456);
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=hex but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/

    public void Var019()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        HexFieldDescription field1 = new HexFieldDescription(new AS400ByteArray(8), "field1");
        // field1.setDFT(new byte[] {0,0,0,0,0,0,0,0});
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        // rec.setField("field1", "Val1");

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        byte[] field1Val = (byte[])rfmlDoc.getValue("format1.field1");
        if (DEBUG) printByteArray("field1Val: ", field1Val);
        assertCondition(areEqual(field1Val, new byte[] {})); // TBD - What I'm getting back is a zero-length array.  Is that working-as-designed?
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field whose type=hex and that contains
     a valid value. Ensure the value for the field is set properly.
     **/

    public void Var020()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        HexFieldDescription field1 = new HexFieldDescription(new AS400ByteArray(8), "field1");
        // field1.setDFT(new byte[] {0,0,0,0,0,0,0,0});
        recFmt.addFieldDescription(field1);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", new byte[] {1,2,3,4,5,6,7,8});

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);
        byte[] field1Val = (byte[])rfmlDoc.getValue("format1.field1");
        if (DEBUG) printByteArray("field1Val: ", field1Val);
        assertCondition(areEqual(field1Val, new byte[] {1,2,3,4,5,6,7,8}));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

    /**
     Test setValues(formatName,record).  Pass formatName that is correct for the
     record passed in and contains one field for each type and that contains
     valid value. Ensure the value for each field is set properly.
     **/

    public void Var021()
    {
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");

        CharacterFieldDescription field1 = new CharacterFieldDescription(new AS400Text(5,systemObject_), "field1");
        recFmt.addFieldDescription(field1);
        BinaryFieldDescription field2 = new BinaryFieldDescription(new AS400Bin2(), "field2");
        recFmt.addFieldDescription(field2);
        ZonedDecimalFieldDescription field3 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "field3");
        recFmt.addFieldDescription(field3);
        PackedDecimalFieldDescription field4 = new PackedDecimalFieldDescription(new AS400PackedDecimal(6,2), "field4");
        recFmt.addFieldDescription(field4);
        FloatFieldDescription field5 = new FloatFieldDescription(new AS400Float4(), "field5");
        recFmt.addFieldDescription(field5);
        BinaryFieldDescription field6 = new BinaryFieldDescription(new AS400Bin4(), "field6");
        recFmt.addFieldDescription(field6);
        HexFieldDescription field7 = new HexFieldDescription(new AS400ByteArray(8), "field7");
        recFmt.addFieldDescription(field7);

        // Make a Record.
        Record rec = recFmt.getNewRecord();
        rec.setField("field1", "Val1");
        rec.setField("field2", new Short("-34"));
        rec.setField("field3", new BigDecimal("-56.78"));
        rec.setField("field4", new BigDecimal("-1234.56"));
        rec.setField("field5", new Float(-1.2e3));
        rec.setField("field6", new Integer(-34567890));
        rec.setField("field7", new byte[] {1});

        RecordFormatDocument rfmlDoc = new RecordFormatDocument();
        rfmlDoc.setValues("format1", rec);

        String field1Val = (String)rfmlDoc.getValue("format1.field1");
        int field2Val = rfmlDoc.getIntValue("format1.field2");
        double field3Val = rfmlDoc.getDoubleValue("format1.field3");
        double field4Val = rfmlDoc.getDoubleValue("format1.field4");
        double field5Val = rfmlDoc.getDoubleValue("format1.field5");
        int field6Val = rfmlDoc.getIntValue("format1.field6");
        byte[] field7Val = (byte[])rfmlDoc.getValue("format1.field7");

        if (DEBUG) printByteArray("field7Val: " , field7Val);

        assertCondition(field1Val.equals("Val1") &&
               field2Val == -34 &&
               field3Val == -56.78 &&
               field4Val == -1234.56 &&
               field5Val == -1.2e3 &&
               field6Val == -34567890 &&
               areEqual(field7Val, new byte[]{1}));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
    }

//    /**
//     Test setValues(formatName,record).  Pass formatName that is correct for the
//     record passed in and contains one field that is a keyed field. Ensure the
//     value of the keyed field is returned correctly (should have to use getKeyFields()
//     in implementation, I think.
//     **/
//
//    public void Var022()
//    {
//        try
//        {
//        }
//        catch (Exception e)
//        {
//            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
//        }
//    }

//
// Testcases for setValues using byte[] as second parameter
//

    /**
     Test setValues(formatName,byte[]).  Pass in an empty string for formatName.
     Verify exception.
     **/
    public void Var022()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] valsIn = new byte[] { 0,1,2,3 };
            rfmlDoc.setValues("", valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "<recordformat> element named '' not found in document";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test setValues(formatName,byte[]).  Pass in null for formatName. Should get null pointer exception.
     **/
    public void Var023()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] valsIn = new byte[] { 0,1,2,3 };
            rfmlDoc.setValues(null, valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test setValues(formatName,byte[]) with a formatName that maps to an empty &lt;recordformat&gt;
     tag in the rfml document. Ensure an empty record created.
     **/
    public void Var024()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtEmpty");
            byte[] valsIn = new byte[0];
            rfmlDoc.setValues("format1", valsIn);
            byte[] valsOut = rfmlDoc.toByteArray("format1");
            assertCondition(valsOut.length == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass an invalid formatName that does not map to a          
     &lt;recordformat&gt; tag. Ensure error is generated.
     **/
    public void Var025()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] valsIn = new byte[0];
            rfmlDoc.setValues("noSuchFormat", valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "<recordformat> element named 'noSuchFormat' not found in document";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass null in for byte[]. Ensure error is generated.
     **/
    public void Var026()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] valsIn = null;
            rfmlDoc.setValues("format1", valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass empty byte[] array in. Ensure error is generated.
     **/
    public void Var027()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            byte[] valsIn = new byte[0];
            rfmlDoc.setValues("format1", valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Insufficient input data available for this document element";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that does not match format of the byte[] array passed in (not enough bytes). Ensure an error is generated.
     **/
    public void Var028()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoRecFmtsDiff");
            byte[] valsIn = new byte[] { 1 };
            rfmlDoc.setValues("format2", valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Insufficient input data available for this document element";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that does not match format of the byte[] array passed in (too many bytes). Ensure an error is generated.
     **/
    public void Var029()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoRecFmtsDiff");
            byte[] valsIn = new byte[] { 1,2,3,4,5,6,7 };
            rfmlDoc.setValues("format1", valsIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Excess input data was provided for this document element";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

// Note: This scenario is only relevant for setValues(Record).
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=char but does not have the contents of the record 
//     set, i.e., the record is empty. This should set the value to the 
//     default value for the field and if that doesn't exist to the default value
//     for the field type.
//     **/
//    public void Var030()
//    {
//    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=char and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var030()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoRecFmtsDiff");
            byte[] valsIn = new byte[] { (byte)'Z' };
            rfmlDoc.setValues("format1", valsIn);
            String valOut = (String)rfmlDoc.getValue("format1.field1");
            if (DEBUG) System.out.println("Expected: |" + "Z" + "|");
            if (DEBUG) System.out.println("Got:      |" + valOut + "|");
            assertCondition(valOut.equals("Z"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=char, ccsid=Unicode, and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var031()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtUnicode");
            String valsInStr = "Z";
            byte[] bytes0 = valsInStr.getBytes("UnicodeBig");  // Note: UnicodeLittle doesn't come back so as to match input value.
            byte[] valsIn = new byte[bytes0.length-2];
            // Skip the initial "FE FF" that gets generated by getBytes().
            System.arraycopy(bytes0, 2, valsIn, 0, valsIn.length);
            if (DEBUG) System.out.println("length of byte array: " + valsIn.length);
            rfmlDoc.setValues("format1", valsIn);
            String valOut = (String)rfmlDoc.getValue("format1.field1");
            //System.out.println("Expected: |" + "Z" + "|");
            //System.out.println("Got:      |" + valOut + "|");
            assertCondition(valOut.equals("Z"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=char and that contains
     an invalid value. Ensure the ascii substitution character (0x3F) is generated.
     **/
    public void Var032()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt819");  // ascii code page
            byte[] valsIn = new byte[] { (byte)0x80 };  // 0x80 is not defined in code page 819
            rfmlDoc.setValues("format1", valsIn);
            String valOut = (String)rfmlDoc.getValue("format1.field1");
            byte[] bytesOut = valOut.getBytes("ASCII");
            byte[] expected = new byte[] { (byte)0x3F };  // the ascii substition character
            if (DEBUG) System.out.println("Expected:");
            if (DEBUG) printByteArray(expected);
            if (DEBUG) System.out.println("Got:");
            if (DEBUG) printByteArray(bytesOut);
            assertCondition(areEqual(bytesOut, expected));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=char and count=2 and that contains a valid value. Ensure the value for the field is set properly.
     **/
    public void Var033()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1Count2"); // ccsid==ascii
            CharConverter conv = new CharConverter(819); // ascii converter
            byte[] valsIn = conv.stringToByteArray("AB");
            rfmlDoc.setValues("format1", valsIn);
            String valOut = (String)rfmlDoc.getValue("format1.field1", new int[] {0}) +
              (String)rfmlDoc.getValue("format1.field1", new int[] {1});
            assertCondition(valOut.equals("AB"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

// Note: This scenario is irrelevant for setValues(byte[]).
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=int but does not have the contents of the record 
//     set, i.e., the record is empty. This should set the value to the 
//     default value for the field and if that doesn't exist to the default value
//     for the field type.
//     **/
//    public void Var033()
//    {
//    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=int and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var034()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.int1");
            byte[] valsIn = new byte[] { 0,7 };
            rfmlDoc.setValues("format1", valsIn);
            Short valOutObj = (Short)rfmlDoc.getValue("format1.field1");
            short valOut = valOutObj.shortValue();
            if (DEBUG) System.out.println("Expected: |" + "7" + "|");
            if (DEBUG) System.out.println("Got:      |" + valOut + "|");
            assertCondition(valOut == 7);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

// Note: This scenario is irrelevant for byte arrays.
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=int and that contains
//     an invalid value. Ensure an error is generated.
//     **/
//    public void Var035()
//    {
//    }

// Note: This scenario is irrelevant for byte arrays.
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=zoned but does not have the contents of the record 
//     set, i.e., the record is empty. This should set the value to the 
//     default value for the field and if that doesn't exist to the default value
//     for the field type.
//     **/
//    public void Var036()
//    {
//    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=zoned and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var035()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1");
            byte[] valsIn = new byte[] { (byte)0xF3 };  // ebcdic '3'
            rfmlDoc.setValues("format1", valsIn);
            BigDecimal valOutObj = (BigDecimal)rfmlDoc.getValue("format1.field1");
            assertCondition(valOutObj.intValue() == 3);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=zoned and that contains
     an invalid value. Ensure an error is generated.
     **/
    public void Var036()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1");
            byte[] valsIn = new byte[] { (byte)0xFF };  // invalid as a digit
            //rfmlDoc.setValue("format1.field1", "Z");  // This causes an exception.
            rfmlDoc.setValues("format1", valsIn);  // This doesn't cause an exception.  TBD
            BigDecimal valOutObj = (BigDecimal)rfmlDoc.getValue("format1.field1");
            failed("Did not throw exception."+valOutObj);
        }
        catch (Exception e)
        {
          assertCondition(exceptionIsInstanceOf(e, "java.lang.NumberFormatException"));
        }
    }

// Note: This scenario is irrelevant for byte arrays.
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=packed but does not have the contents of the record 
//     set, i.e., the record is empty. This should set the value to the 
//     default value for the field and if that doesn't exist to the default value
//     for the field type.
//     **/
//    public void Var039()
//    {
//    }


    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=packed and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var037()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.packed1");
            //byte[] bytesOut0 = rfmlDoc.toByteArray("format1");
            //printByteArray("Initial bytes: ", bytesOut0);
            byte[] valsIn = new byte[] { (byte)0x01, (byte)0x7d };  // 17, minus
            rfmlDoc.setValues("format1", valsIn);
            BigDecimal valOutObj = (BigDecimal)rfmlDoc.getValue("format1.field1");
            assertCondition(valOutObj.intValue() == -17);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=packed and that contains
     an invalid value. Ensure an error is generated.
     **/
    public void Var038()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.packed1");
            byte[] valsIn = new byte[] { (byte)0x01, (byte)0xad };  // invalid value, minus
            rfmlDoc.setValues("format1", valsIn);  // This doesn't cause an exception.  TBD
            BigDecimal valOutObj = (BigDecimal)rfmlDoc.getValue("format1.field1");
            failed("Did not throw exception."+valOutObj);
        }
        catch (Exception e)
        {
          assertCondition(exceptionIsInstanceOf(e, "java.lang.NumberFormatException"));
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=float but does not have the contents of the record 
     set, i.e., the record is empty. This should set the value to the 
     default value for the field and if that doesn't exist to the default value
     for the field type.
     **/
    public void Var039()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.float1");
            Float valOutObj = (Float)rfmlDoc.getValue("format1.field1");
            if (DEBUG) System.out.println("Value out: " + valOutObj.intValue() + ", " + valOutObj.floatValue() + ", " + valOutObj.toString());
            assertCondition(valOutObj.floatValue() == 0.0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=float and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var040()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.float1");
            //byte[] bytesOut0 = rfmlDoc.toByteArray("format1");
            //printByteArray("Initial bytes: ", bytesOut0);
            byte[] valsIn = new byte[] { (byte)0x43, (byte)0xA0, (byte)0x00, (byte)0x00 };  // float representation of the value 320
            rfmlDoc.setValues("format1", valsIn);
            Float valOutObj = (Float)rfmlDoc.getValue("format1.field1");
            if (DEBUG) System.out.println("Value out: " + valOutObj.intValue() + ", " + valOutObj.floatValue() + ", " + valOutObj.toString());
            assertCondition(valOutObj.floatValue() == 320.0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=float and that contains
     an invalid value. Ensure an error is generated.
     **/
    public void Var041()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.float1BadValue");
            BigDecimal valOutObj = (BigDecimal)rfmlDoc.getValue("format1.field1");
            failed("Did not throw exception."+valOutObj);
        }
        catch (Exception e)
        {
          String expectedMsg = "A PCML specification error occurred";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


// Note: This scenario is irrelevant for byte arrays.
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=byte but does not have the contents of the record 
//     set, i.e., the record is empty. This should set the value to the 
//     default value for the field and if that doesn't exist to the default value
//     for the field type.
//     **/
//    public void Var045()
//    {
//    }

    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field whose type=byte and that contains
     a valid value. Ensure the value for the field is set properly.
     **/
    public void Var042()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.byte1");
            byte[] valsIn = new byte[] { (byte)0xF3 };
            rfmlDoc.setValues("format1", valsIn);
            byte[] valOutObj = (byte[])rfmlDoc.getValue("format1.field1");
            assertCondition(areEqual(valsIn, valOutObj));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

// Note: This scenario is irrelevant for byte arrays.
//    /**
//     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
//     byte[] passed in and contains one field whose type=byte and that contains
//     an invalid value. Ensure an error is generated.
//     **/
//    public void Var047()
//    {
//    }


    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field for each type and that contains
     valid value. Ensure the value for each field is set properly.
     **/
    public void Var043()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachType");
            int offset = 0;
            byte[] valsIn = new byte[100];
            byte[] bytes0 = null;

            // format1.field1: The char field (2 bytes).
            String valChar = "Z";  // value == "Z"
            bytes0 = valChar.getBytes("UnicodeBig");
            if (bytes0.length == 2) {
              valsIn[offset++] = bytes0[0];
              valsIn[offset++] = bytes0[1];
            }
            else { // Skip the initial "FE FF" that gets generated by getBytes().
              valsIn[offset++] = bytes0[2];
              valsIn[offset++] = bytes0[3];
            }

            // format1.field2: The int field (2 bytes).
            bytes0 = new byte[] { 0,3 };  // value == 3
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field3: The zoned field (2 bytes).
            bytes0 = new byte[] { (byte)0xF0,(byte)0xF5 };  // value == 05 (ebcdic digits)
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field4: The packed field (2 bytes).
            bytes0 = new byte[] { (byte)0x53,(byte)0x7F };  // value == 537 (followed by "sign nibble")   //TBD - Should probably test that all possible valid sign-nibble values are accepted.
            /* Note: From AS400PackedDecimal.java:
             switch (nibble)
             {
             case 0x0B: // valid negative sign bits
             case 0x0D:
             outputData = new char[numDigits+1];
             outputData[outputPosition++] = '-';
             break;
             case 0x0A: // valid positive sign bits
             case 0x0C:
             case 0x0E:
             case 0x0F:
             outputData = new char[numDigits];
             break;
             default: // others invalid
             throw new NumberFormatException(String.valueOf(offset+inputSize-1));
             }
             */
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field5: The float field (4 bytes).
            bytes0 = new byte[4];
            BinaryConverter.floatToByteArray((float)1.23e4, bytes0, 0);  // value == 1.23e4
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];
            valsIn[offset++] = bytes0[2];
            valsIn[offset++] = bytes0[3];

            // format1.field6: The byte field (2 bytes).
            byte[] field6In = new byte[] { (byte)0xBE, (byte)0xEF }; // value = x"BEEF"
            valsIn[offset++] = field6In[0];
            valsIn[offset++] = field6In[1];

            // format1.field7.field1: The struct1's char field (2 bytes).
            /*String*/ valChar = "W";  // value == "W"
            bytes0 = valChar.getBytes("UnicodeBig");
            if (bytes0.length == 2) {
              valsIn[offset++] = bytes0[0];
              valsIn[offset++] = bytes0[1];
            }
            else { // Skip the initial "FE FF" that gets generated by getBytes().
              valsIn[offset++] = bytes0[2];
              valsIn[offset++] = bytes0[3];
            }

            // format1.field7.field2: The struct's int field (2 bytes).
            bytes0 = new byte[] { 0,9 };  // value == 9
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field7.field3: The struct's zoned field (2 bytes).
            bytes0 = new byte[] { (byte)0xF3,(byte)0xF2 };  // value == 32 (ebcdic digits)
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field7.field4: The struct's packed field (2 bytes).
            bytes0 = new byte[] { (byte)0x73,(byte)0x5A };  // value == 735 (followed by "sign nibble")   //TBD - Should probably test that all possible valid sign-nibble values are accepted.
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field7.field5: The struct's float field (4 bytes).
            bytes0 = new byte[4];
            BinaryConverter.floatToByteArray((float)4.56e7, bytes0, 0);  // value == 4.56e7
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];
            valsIn[offset++] = bytes0[2];
            valsIn[offset++] = bytes0[3];

            // format1.field7.field6: The struct's byte field (2 bytes).
            byte[] field7_6In = new byte[] { (byte)0xFE, (byte)0xED }; // value = x"FEED"
            valsIn[offset++] = field7_6In[0];
            valsIn[offset++] = field7_6In[1];



            // Trim the array down to required size for a record.
            byte[] trimmed = new byte[offset];
            System.arraycopy(valsIn, 0, trimmed, 0, offset);
            rfmlDoc.setValues("format1", trimmed);


            if (DEBUG) System.out.println("Got:");

            String field1Out = (String)rfmlDoc.getValue("format1.field1");
            if (DEBUG) System.out.println("field1: |" + field1Out + "|");

            Short field2Out = (Short)rfmlDoc.getValue("format1.field2");
            if (DEBUG) System.out.println("field2: |" + field2Out + "|");

            BigDecimal field3Out = (BigDecimal)rfmlDoc.getValue("format1.field3");
            if (DEBUG) System.out.println("field3: |" + field3Out + "|");

            BigDecimal field4Out = (BigDecimal)rfmlDoc.getValue("format1.field4");
            if (DEBUG) System.out.println("field4: |" + field4Out.toString() + "|");

            Float field5Out = (Float)rfmlDoc.getValue("format1.field5");
            if (DEBUG) System.out.println("field5: |" + field5Out.toString() + "|");

            byte[] field6Out = (byte[])rfmlDoc.getValue("format1.field6");
            if (DEBUG) printByteArray("field6: ", field6Out);

            String field7_1Out = (String)rfmlDoc.getValue("format1.field7.field1");
            if (DEBUG) System.out.println("field7.field1: |" + field7_1Out + "|");

            Short field7_2Out = (Short)rfmlDoc.getValue("format1.field7.field2");
            if (DEBUG) System.out.println("field7.field2: |" + field7_2Out.shortValue() + "|");

            BigDecimal field7_3Out = (BigDecimal)rfmlDoc.getValue("format1.field7.field3");
            if (DEBUG) System.out.println("field7.field3: |" + field7_3Out.toString() + "|");

            BigDecimal field7_4Out = (BigDecimal)rfmlDoc.getValue("format1.field7.field4");
            if (DEBUG) System.out.println("field7.field4: |" + field7_4Out.toString() + "|");

            Float field7_5Out = (Float)rfmlDoc.getValue("format1.field7.field5");
            if (DEBUG) System.out.println("field7.field5: |" + field7_5Out.toString() + "|");

            byte[] field7_6Out = (byte[])rfmlDoc.getValue("format1.field7.field6");
            if (DEBUG) System.out.println("field7.field6: ");
            if (DEBUG) printByteArray(field7_6Out);

            assertCondition(
                   field1Out.equals("Z") &&
                   field2Out.intValue() == 3 &&
                   field3Out.doubleValue() == 5 &&
                   field4Out.doubleValue() == 537 &&
                   field5Out.floatValue() == 1.23e4 &&
                   areEqual(field6Out, field6In) &&
                   field7_1Out.equals("W") &&
                   field7_2Out.intValue() == 9 &&
                   field7_3Out.doubleValue() == 32 &&
                   field7_4Out.doubleValue() == 735 &&
                   field7_5Out.floatValue() == 4.56e7 &&
                   areEqual(field7_6Out, field7_6In) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field for each type and that contains
     valid value. In the RFML, each field specifies count=1.
     Ensure the value for each field is set properly.
     **/
    public void Var044()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount1");

            int offset = 0;
            byte[] valsIn = new byte[100];
            byte[] bytes0 = null;

            // format1.field1: The char field (2 bytes).
            String valChar = "Z";  // value == "Z"
            bytes0 = valChar.getBytes("UnicodeBig");
            if (bytes0.length == 2) {
              valsIn[offset++] = bytes0[0];
              valsIn[offset++] = bytes0[1];
            }
            else { // Skip the initial "FE FF" that gets generated by getBytes().
              valsIn[offset++] = bytes0[2];
              valsIn[offset++] = bytes0[3];
            }

            // format1.field2: The int field (2 bytes).
            bytes0 = new byte[] { 0,3 };  // value == 3
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field3: The zoned field (2 bytes).
            bytes0 = new byte[] { (byte)0xF0,(byte)0xF5 };  // value == 05 (ebcdic digits)
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field4: The packed field (2 bytes).
            bytes0 = new byte[] { (byte)0x53,(byte)0x7F };  // value == 537 (followed by "sign nibble")   //TBD - Should probably test that all possible valid sign-nibble values are accepted.
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field5: The float field (4 bytes).
            bytes0 = new byte[4];
            BinaryConverter.floatToByteArray((float)1.23e4, bytes0, 0);  // value == 1.23e4
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];
            valsIn[offset++] = bytes0[2];
            valsIn[offset++] = bytes0[3];

            // format1.field6: The byte field (2 bytes).
            byte[] field6In = new byte[] { (byte)0xBE, (byte)0xEF }; // value = x"BEEF"
            valsIn[offset++] = field6In[0];
            valsIn[offset++] = field6In[1];

            // format1.field7.field1: The struct1's char field (2 bytes).
            /*String*/ valChar = "W";  // value == "W"
            bytes0 = valChar.getBytes("UnicodeBig");
            if (bytes0.length == 2) {
              valsIn[offset++] = bytes0[0];
              valsIn[offset++] = bytes0[1];
            }
            else { // Skip the initial "FE FF" that gets generated by getBytes().
              valsIn[offset++] = bytes0[2];
              valsIn[offset++] = bytes0[3];
            }

            // format1.field7.field2: The struct's int field (2 bytes).
            bytes0 = new byte[] { 0,9 };  // value == 9
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field7.field3: The struct's zoned field (2 bytes).
            bytes0 = new byte[] { (byte)0xF3,(byte)0xF2 };  // value == 32 (ebcdic digits)
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field7.field4: The struct's packed field (2 bytes).
            bytes0 = new byte[] { (byte)0x73,(byte)0x5A };  // value == 735 (followed by "sign nibble")   //TBD - Should probably test that all possible valid sign-nibble values are accepted.
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field7.field5: The struct's float field (4 bytes).
            bytes0 = new byte[4];
            BinaryConverter.floatToByteArray((float)4.56e7, bytes0, 0);  // value == 4.56e7
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];
            valsIn[offset++] = bytes0[2];
            valsIn[offset++] = bytes0[3];

            // format1.field7.field6: The struct's byte field (2 bytes).
            byte[] field7_6In = new byte[] { (byte)0xFE, (byte)0xED }; // value = x"FEED"
            valsIn[offset++] = field7_6In[0];
            valsIn[offset++] = field7_6In[1];



            // Trim the array down to required size for a record.
            byte[] trimmed = new byte[offset];
            System.arraycopy(valsIn, 0, trimmed, 0, offset);
            rfmlDoc.setValues("format1", trimmed);


            ///System.out.println("Got:");
            int[] indices1 = new int[] { 0 };
            int[] indices2 = new int[] { 0,0 };

            String field1Out = (String)rfmlDoc.getValue("format1.field1", indices1);
            ///System.out.println("field1: |" + field1Out + "|");

            Short field2Out = (Short)rfmlDoc.getValue("format1.field2", indices1);
            ///System.out.println("field2: |" + field2Out.shortValue() + "|");

            BigDecimal field3Out = (BigDecimal)rfmlDoc.getValue("format1.field3", indices1);
            ///System.out.println("field3: |" + field3Out.toString() + "|");

            BigDecimal field4Out = (BigDecimal)rfmlDoc.getValue("format1.field4", indices1);
            ///System.out.println("field4: |" + field4Out.toString() + "|");

            Float field5Out = (Float)rfmlDoc.getValue("format1.field5", indices1);
            ///System.out.println("field5: |" + field5Out.toString() + "|");

            byte[] field6Out = (byte[])rfmlDoc.getValue("format1.field6", indices1);
            ///System.out.println("field6: ");
            ///printByteArray(field6Out);

            String field7_1Out = (String)rfmlDoc.getValue("format1.field7.field1", indices2);
            ///System.out.println("field7.field1: |" + field7_1Out + "|");

            Short field7_2Out = (Short)rfmlDoc.getValue("format1.field7.field2", indices2);
            ///System.out.println("field7.field2: |" + field7_2Out.shortValue() + "|");

            BigDecimal field7_3Out = (BigDecimal)rfmlDoc.getValue("format1.field7.field3", indices2);
            ///System.out.println("field7.field3: |" + field7_3Out.toString() + "|");

            BigDecimal field7_4Out = (BigDecimal)rfmlDoc.getValue("format1.field7.field4", indices2);
            ///System.out.println("field7.field4: |" + field7_4Out.toString() + "|");

            Float field7_5Out = (Float)rfmlDoc.getValue("format1.field7.field5", indices2);
            ///System.out.println("field7.field5: |" + field7_5Out.toString() + "|");

            byte[] field7_6Out = (byte[])rfmlDoc.getValue("format1.field7.field6", indices2);
            ///System.out.println("field7.field6: ");
            ///printByteArray(field7_6Out);


            assertCondition(
                   field1Out.equals("Z") &&
                   field2Out.intValue() == 3 &&
                   field3Out.doubleValue() == 5 &&
                   field4Out.doubleValue() == 537 &&
                   field5Out.floatValue() == 1.23e4 &&
                   areEqual(field6Out, field6In) &&
                   field7_1Out.equals("W") &&
                   field7_2Out.intValue() == 9 &&
                   field7_3Out.doubleValue() == 32 &&
                   field7_4Out.doubleValue() == 735 &&
                   field7_5Out.floatValue() == 4.56e7 &&
                   areEqual(field7_6Out, field7_6In) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    static final BigInteger toBigInteger(int value)
    {
      return new BigInteger(Integer.toString(value));
    }

    static final BigInteger toBigInteger(long value)
    {
      return new BigInteger(Long.toString(value));
    }


    /**
     Test setValues(formatName,byte[]).  Pass formatName that is correct for the
     byte[] passed in and contains one field for each type and that contains
     valid value. In the RFML, each field specifies count=1.
     Ensure the value for each field is set properly.
     **/
    public void Var045()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount1new");

            int offset = 0;
            //byte[] valsIn = new byte[100];
            byte[] valsIn = new byte[200];
            byte[] bytes0 = null;
            AS400Date dateConv;
            AS400Time timeConv;
            AS400Timestamp timestampConv;
            java.sql.Date date;
            java.sql.Time time;
            java.sql.Timestamp timestamp;

            // format1.field1: The char field (2 bytes).
            String valChar = "Z";  // value == "Z"
            bytes0 = valChar.getBytes("UnicodeBig");
            if (bytes0.length == 2) {
              valsIn[offset++] = bytes0[0];
              valsIn[offset++] = bytes0[1];
            }
            else { // Skip the initial "FE FF" that gets generated by getBytes().
              valsIn[offset++] = bytes0[2];
              valsIn[offset++] = bytes0[3];
            }

            // format1.field2: The int field (2 bytes).
            bytes0 = new byte[] { 0,3 };  // value == 3
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field3: The zoned field (2 bytes).
            bytes0 = new byte[] { (byte)0xF0,(byte)0xF5 };  // value == 05 (ebcdic digits)
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field4: The packed field (2 bytes).
            bytes0 = new byte[] { (byte)0x53,(byte)0x7F };  // value == 537 (followed by "sign nibble")   //TBD - Should probably test that all possible valid sign-nibble values are accepted.
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.field5: The float field (4 bytes).
            bytes0 = new byte[4];
            BinaryConverter.floatToByteArray((float)1.23e4, bytes0, 0);  // value == 1.23e4
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];
            valsIn[offset++] = bytes0[2];
            valsIn[offset++] = bytes0[3];

            // format1.field6: The byte field (2 bytes).
            byte[] field6In = new byte[] { (byte)0xBE, (byte)0xEF }; // value = x"BEEF"
            valsIn[offset++] = field6In[0];
            valsIn[offset++] = field6In[1];

            // format1.field7: The date field (no length specified).
            dateConv = new AS400Date(TimeZone.getDefault(),AS400Date.FORMAT_ISO);
            date = (java.sql.Date)dateConv.getDefaultValue();
            bytes0 = dateConv.toBytes(date);
            if (DEBUG) System.out.println("date bytes length: " + bytes0.length);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.field8: The time field (no length specified).
            timeConv = new AS400Time(TimeZone.getDefault(),AS400Time.FORMAT_ISO);
            time = (java.sql.Time)timeConv.getDefaultValue();
            bytes0 = timeConv.toBytes(time);
            if (DEBUG) System.out.println("time bytes length: " + bytes0.length);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.field9: The timestamp field (no length specified).
            timestampConv = new AS400Timestamp(TimeZone.getDefault());
            timestamp = (java.sql.Timestamp)timestampConv.getDefaultValue();
            bytes0 = timestampConv.toBytes(timestamp);
            if (DEBUG) System.out.println("timestamp bytes length: " + bytes0.length);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.fieldA: The int field (8 bytes, precision 63).
            bytes0 = new AS400Bin8().toBytes(new Long(-2433234545462216467L));
            if (DEBUG) System.out.println("signedBin8 bytes length: " + bytes0.length);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.fieldB: The int field (8 bytes, precision 64).
            bytes0 = new AS400UnsignedBin8().toBytes(toBigInteger(2165379345l));
            if (DEBUG) System.out.println("unsignedBin8 bytes length: " + bytes0.length);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

//            // format1.fieldC: The int field (2 bytes, precision 7).
//            valsIn[offset++] = 0;           // leading 0
//            valsIn[offset++] = (byte)0x80;  // -128 (two's complement)
//
//            // format1.fieldD: The int field (2 bytes, precision 8).
//            valsIn[offset++] = 0;           // leading 0
//            valsIn[offset++] = (byte)0xFF;  // 255 (no 'sign' bit)

            // format1.fieldS.field1: The struct1's char field (2 bytes).
            /*String*/ valChar = "W";  // value == "W"
            bytes0 = valChar.getBytes("UnicodeBig");
            if (bytes0.length == 2) {
              valsIn[offset++] = bytes0[0];
              valsIn[offset++] = bytes0[1];
            }
            else { // Skip the initial "FE FF" that gets generated by getBytes().
              valsIn[offset++] = bytes0[2];
              valsIn[offset++] = bytes0[3];
            }

            // format1.fieldS.field2: The struct's int field (2 bytes).
            bytes0 = new byte[] { 0,9 };  // value == 9
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.fieldS.field3: The struct's zoned field (2 bytes).
            bytes0 = new byte[] { (byte)0xF3,(byte)0xF2 };  // value == 32 (ebcdic digits)
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.fieldS.field4: The struct's packed field (2 bytes).
            bytes0 = new byte[] { (byte)0x73,(byte)0x5A };  // value == 735 (followed by "sign nibble")   //TBD - Should probably test that all possible valid sign-nibble values are accepted.
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];

            // format1.fieldS.field5: The struct's float field (4 bytes).
            bytes0 = new byte[4];
            BinaryConverter.floatToByteArray((float)4.56e7, bytes0, 0);  // value == 4.56e7
            valsIn[offset++] = bytes0[0];
            valsIn[offset++] = bytes0[1];
            valsIn[offset++] = bytes0[2];
            valsIn[offset++] = bytes0[3];

            // format1.fieldS.field6: The struct's byte field (2 bytes).
            byte[] fieldS_6In = new byte[] { (byte)0xFE, (byte)0xED }; // value = x"FEED"
            valsIn[offset++] = fieldS_6In[0];
            valsIn[offset++] = fieldS_6In[1];

            // format1.fieldS.field7: The struct's date field (no length specified).
            dateConv = new AS400Date(TimeZone.getDefault(),AS400Date.FORMAT_ISO);
            date = (java.sql.Date)dateConv.getDefaultValue();
            bytes0 = dateConv.toBytes(date);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.fieldS.field8: The struct's time field (no length specified).
            timeConv = new AS400Time(TimeZone.getDefault(),AS400Time.FORMAT_ISO);
            time = (java.sql.Time)timeConv.getDefaultValue();
            bytes0 = timeConv.toBytes(time);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.fieldS.field9: The struct's timestamp field (no length specified).
            timestampConv = new AS400Timestamp(TimeZone.getDefault());
            timestamp = (java.sql.Timestamp)timestampConv.getDefaultValue();
            bytes0 = timestampConv.toBytes(timestamp);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.fieldS.fieldA: The int field (8 bytes, precision 63).
            bytes0 = new AS400Bin8().toBytes(new Long(Long.MAX_VALUE));
            if (DEBUG) System.out.println("signedBin8 bytes length: " + bytes0.length);
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

            // format1.fieldS.fieldB: The int field (8 bytes, precision 64).
            final byte[] MAX_UNSIGNED_VALUE_8_BYTES = { (byte)0, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF }; // Specify a leading '0', to prevent interpretation as a (two's-complement) negative number.
            BigInteger maxValFor8Bytes = new BigInteger(MAX_UNSIGNED_VALUE_8_BYTES);
            if (DEBUG) System.out.println("maxValFor8Bytes.toString(): |" + maxValFor8Bytes.toString() + "|");
            bytes0 = new AS400UnsignedBin8().toBytes(maxValFor8Bytes);
            if (DEBUG) System.out.println("unsignedBin8 bytes length (should be 8): " + bytes0.length);
            if (DEBUG) System.out.println("maxValFor8Bytes: " + maxValFor8Bytes.toString());
            for (int i=0; i<bytes0.length; i++) {
              valsIn[offset++] = bytes0[i];
            }

//            // format1.fieldS.fieldC: The int field (1 byte, precision 7).
//            valsIn[offset++] = 0;           // leading 0
//            valsIn[offset++] = (byte)0xFF;  // -1  (two's complement)
//
//            // format1.fieldS.fieldD: The int field (1 byte, precision 8).
//            valsIn[offset++] = 0;           // leading 0
//            valsIn[offset++] = (byte)0x00;  // 0



            // Trim the array down to required size for a record.
            byte[] trimmed = new byte[offset];
            System.arraycopy(valsIn, 0, trimmed, 0, offset);
            if (DEBUG) System.out.println("DEBUG: Trimmed byte array (length="+trimmed.length+")");
            if (DEBUG) printByteArray(trimmed);
            rfmlDoc.setValues("format1", trimmed);


            if (DEBUG) if (DEBUG) System.out.println("Got:");
            int[] indices1 = new int[] { 0 };
            int[] indices2 = new int[] { 0,0 };

            String field1Out = (String)rfmlDoc.getValue("format1.field1", indices1);
            if (DEBUG) if (DEBUG) System.out.println("field1: |" + field1Out + "|");

            Short field2Out = (Short)rfmlDoc.getValue("format1.field2", indices1);
            if (DEBUG) if (DEBUG) System.out.println("field2: |" + field2Out.shortValue() + "|");

            BigDecimal field3Out = (BigDecimal)rfmlDoc.getValue("format1.field3", indices1);
            if (DEBUG) if (DEBUG) System.out.println("field3: |" + field3Out.toString() + "|");

            BigDecimal field4Out = (BigDecimal)rfmlDoc.getValue("format1.field4", indices1);
            if (DEBUG) if (DEBUG) System.out.println("field4: |" + field4Out.toString() + "|");

            Float field5Out = (Float)rfmlDoc.getValue("format1.field5", indices1);
            if (DEBUG) if (DEBUG) System.out.println("field5: |" + field5Out.toString() + "|");

            byte[] field6Out = (byte[])rfmlDoc.getValue("format1.field6", indices1);
            if (DEBUG) if (DEBUG) System.out.println("field6: ");
            if (DEBUG) printByteArray(field6Out);

            java.sql.Date field7Out = (java.sql.Date)rfmlDoc.getValue("format1.field7", indices1);
            if (DEBUG) System.out.println("field7: |" + field7Out.toString() + "|");///


            java.sql.Time field8Out = (java.sql.Time)rfmlDoc.getValue("format1.field8", indices1);
            if (DEBUG) System.out.println("field8: |" + field8Out.toString() + "|");///

            java.sql.Timestamp field9Out = (java.sql.Timestamp)rfmlDoc.getValue("format1.field9", indices1);
            if (DEBUG) System.out.println("field9: |" + field9Out.toString() + "|");///

            Long fieldAOut = (Long)rfmlDoc.getValue("format1.fieldA", indices1);
            if (DEBUG) System.out.println("fieldA: |" + fieldAOut.toString() + "|");

            BigInteger fieldBOut = (BigInteger)rfmlDoc.getValue("format1.fieldB", indices1);
            if (DEBUG) System.out.println("fieldB: |" + fieldBOut.toString() + "|");

//            Byte fieldCOut = (Byte)rfmlDoc.getValue("format1.fieldC", indices1);
//            if (DEBUG) if (DEBUG) System.out.println("fieldC: |" + fieldCOut.toString() + "|");
//
//            Short fieldDOut = (Short)rfmlDoc.getValue("format1.fieldD", indices1);
//            if (DEBUG) if (DEBUG) System.out.println("fieldD: |" + fieldDOut.toString() + "|");

            String fieldS_1Out = (String)rfmlDoc.getValue("format1.fieldS.field1", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field1: |" + fieldS_1Out + "|");

            Short fieldS_2Out = (Short)rfmlDoc.getValue("format1.fieldS.field2", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field2: |" + fieldS_2Out.shortValue() + "|");

            BigDecimal fieldS_3Out = (BigDecimal)rfmlDoc.getValue("format1.fieldS.field3", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field3: |" + fieldS_3Out.toString() + "|");

            BigDecimal fieldS_4Out = (BigDecimal)rfmlDoc.getValue("format1.fieldS.field4", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field4: |" + fieldS_4Out.toString() + "|");

            Float fieldS_5Out = (Float)rfmlDoc.getValue("format1.fieldS.field5", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field5: |" + fieldS_5Out.toString() + "|");

            byte[] fieldS_6Out = (byte[])rfmlDoc.getValue("format1.fieldS.field6", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field6: ");
            if (DEBUG) printByteArray(fieldS_6Out);

            java.sql.Date fieldS_7Out = (java.sql.Date)rfmlDoc.getValue("format1.fieldS.field7", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field7: |" + fieldS_7Out.toString() + "|");

            java.sql.Time fieldS_8Out = (java.sql.Time)rfmlDoc.getValue("format1.fieldS.field8", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field8: |" + fieldS_8Out.toString() + "|");

            java.sql.Timestamp fieldS_9Out = (java.sql.Timestamp)rfmlDoc.getValue("format1.fieldS.field9", indices2);
            if (DEBUG) if (DEBUG) System.out.println("fieldS.field9: |" + fieldS_9Out.toString() + "|");

            Long fieldS_AOut = (Long)rfmlDoc.getValue("format1.fieldS.fieldA", indices2);
            if (DEBUG) System.out.println("fieldS.fieldA: |" + fieldS_AOut.toString() + "|");

            BigInteger fieldS_BOut = (BigInteger)rfmlDoc.getValue("format1.fieldS.fieldB", indices2);
            if (DEBUG) System.out.println("fieldS.fieldB: |" + fieldS_BOut.toString() + "|");

//            Byte fieldS_COut = (Byte)rfmlDoc.getValue("format1.fieldS.fieldC", indices2);
//            if (DEBUG) if (DEBUG) System.out.println("fieldS.fieldC: |" + fieldS_COut.toString() + "|");
//
//            Short fieldS_DOut = (Short)rfmlDoc.getValue("format1.fieldS.fieldD", indices2);
//            if (DEBUG) if (DEBUG) System.out.println("fieldS.fieldD: |" + fieldS_DOut.toString() + "|");

            assertCondition(
                   field1Out.equals("Z") &&
                   field2Out.intValue() == 3 &&
                   field3Out.doubleValue() == 5 &&
                   field4Out.doubleValue() == 537 &&
                   field5Out.floatValue() == 1.23e4 &&
                   areEqual(field6Out, field6In) &&
                   (field7Out.toString()).equals(dateConv.getDefaultValue().toString()) &&
                   (field8Out.toString()).equals(timeConv.getDefaultValue().toString()) &&
                   ((java.sql.Timestamp)field9Out).equals(timestampConv.getDefaultValue()) &&
                   fieldAOut.longValue() == -2433234545462216467L &&
                   fieldBOut.equals(toBigInteger(2165379345l)) &&
                   //fieldCOut.intValue() == -128 &&
                   //fieldDOut.intValue() == 255 &&
                   fieldS_1Out.equals("W") &&
                   fieldS_2Out.intValue() == 9 &&
                   fieldS_3Out.doubleValue() == 32 &&
                   fieldS_4Out.doubleValue() == 735 &&
                   fieldS_5Out.floatValue() == 4.56e7 &&
                   areEqual(fieldS_6Out, fieldS_6In) &&
                   (fieldS_7Out.toString()).equals(dateConv.getDefaultValue().toString()) &&
                   (fieldS_8Out.toString()).equals(timeConv.getDefaultValue().toString()) &&
                   ((java.sql.Timestamp)fieldS_9Out).equals(timestampConv.getDefaultValue()) &&
                   fieldS_AOut.longValue() == Long.MAX_VALUE &&
                   fieldS_BOut.equals(maxValFor8Bytes) //&&
                   //fieldS_COut.intValue() == -1 &&
                   //fieldS_DOut.intValue() == 0 //&&
                 );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

}
