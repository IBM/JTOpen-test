///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMtoRecordFormatTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RM;


import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ZonedDecimalFieldDescription;
import com.ibm.as400.data.RecordFormatDocument;

import test.Testcase;

/**
 The RMtoRecordFormatTestcase class tests the following methods of the RecordFormatDocument class:
 <li>toRecordFormat(formatName)
 **/
public class RMtoRecordFormatTestcase extends Testcase
{
    /**
     Constructor.
     **/
    public RMtoRecordFormatTestcase(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMtoRecordFormatTestcase", namesAndVars.get("RMtoRecordFormatTestcase"), runMode, fileOutputStream);
    }

    /**
     Test toRecordFormat(formatName).  Pass in an empty string for formatName. What should happen?
     **/
    public void Var001()
    {
	
        try 
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           rfmlDoc.toRecordFormat("");
           failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }
    }


    /**
     Test toRecordFormat(formatName).  Pass in null for formatName. Should get null pointer exception.
     **/
    public void Var002()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           rfmlDoc.toRecordFormat(null);
           failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test toRecord(formatName) without having the document set yet
     **/
    public void Var003()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument();
           rfmlDoc.toRecordFormat("format1");
           failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }
    }

    /**
     Test toRecordFormat(formatName) with a formatName that maps to an empty &lt;recordformat&gt;
     tag in the rfml document. Ensure an empty record format created.
     **/
    public void Var004()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt != null)
           {
             if (recFmt.getNumberOfFields() == 0)
                succeeded();
             else
                failed("Invalid number of fields in record format.");
           } 
           else 
                failed("Record format is null.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }

    }

    /**
     Test toRecordFormat(formatName).  Pass an invalid formatName that does not map to a          
     &lt;recordformat&gt; tag. Ensure error is generated.
     **/

    public void Var005()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           rfmlDoc.toRecord("format2");
           failed("Exception not thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }

    }

    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains one &lt;data&gt; element with only length and type set but no
     name set. Ensure one field format generated with all attribute values
     set to default values. What would name be? 
     **/
    public void Var006()
    {
	
        AS400Text dt = new AS400Text(8);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt2");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 1)
             failed("Incorrect number of fields. Should be 1 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1"))
                failed("Bad field name returned.");
             else 
             {

                FieldDescription field1 = recFmt.getFieldDescription(0);
                if ( field1.getDataType().getClass() == dt.getClass() &&
                     field1.getDataType().getByteLength() == dt.getByteLength())
                   succeeded();
                else 
                   failed("Bad data type or length returned for field.");
             }
           }
           CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(0);
           if (! (char1.getCCSID().equals("")))
           {
              failed("Bad ccsid value returned.");
              return;
           }


//           RecordFormat recFmt2 = new RecordFormat("format2");
//           CharacterFieldDescription field1 = new CharacterFieldDescription();
//           field1.setDataType(new AS400Text(8));
//           recFmt.addFieldDescription(field1);
//           Record newRec = new Record(recFmt);
//           String[] fieldNames = recFmt2.getFieldNames();
//           System.out.println("Field name = " + fieldNames[0] );
//           failed("Exception not thrown.");
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }

    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains three &lt;data&gt; element with no attribute values   
     set except type and length. Ensure three field format generated with all attribute values
     set to default values. What would names be? 
     **/
    public void Var007()
    {
	
        AS400Text dt1= new AS400Text(8);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(12,0);
        AS400ZonedDecimal dt3  = new AS400ZonedDecimal(6,0);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt3");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 3)
             failed("Incorrect number of fields. Should be 3 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1") || !fieldNames[1].equals("field2") ||
                 !fieldNames[2].equals("field3"))
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                   failed("Bad data type or length returned for field 1.");
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                        failed("Bad data type or length returned for field 2.");
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                        failed("Bad data type or length returned for field 3.");
            }
           }
           CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(0);
           PackedDecimalFieldDescription packed1 = (PackedDecimalFieldDescription) recFmt.getFieldDescription(1);
           ZonedDecimalFieldDescription zoned1 = (ZonedDecimalFieldDescription) recFmt.getFieldDescription(2);
           if (! (char1.getCCSID().equals("")))
           {
              failed("Bad ccsid value returned.");
              return;
           }
           if (packed1.getDecimalPositions() != 0 || zoned1.getDecimalPositions() !=0 )
           {
              failed("Bad precision value returned.");
              return;
           }
           succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
    }

    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains two &lt;data&gt; elements with all valid attribute values set.
     Ensure a two field format is returned with proper name, length, precision, 
     ccsid, description, and initial value.
     **/
    public void Var008()
    {
	
        AS400Text dt1= new AS400Text(8);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,3);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt4");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 2)
             failed("Incorrect number of fields. Should be 2 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("CharacterField") || !fieldNames[1].equals("PackedField") )
                failed("Bad empty field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);

                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(0);
                PackedDecimalFieldDescription packed1 = (PackedDecimalFieldDescription) recFmt.getFieldDescription(1);
                if (! (char1.getCCSID().equals("290")))
                {
                   failed("Bad ccsid value returned.");
                   return;
                }
                if (packed1.getDecimalPositions() != 3)
                {
                   failed("Bad precision value returned.");
                   return;
                }
            }
           succeeded();
           }
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }


    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains one &lt;data&gt; element for each supported type with no   
     initial values set and no other attributes set except length and type. Ensure proper format created with 
     proper default values for each type and for each attribute.
     **/
    public void Var009()
    {
	
        AS400Text dt1= new AS400Text(25);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,2);
        AS400Bin2 dt3 = new AS400Bin2();
        AS400Bin4 dt4 = new AS400Bin4();
        AS400Float4 dt5 = new AS400Float4();
        AS400Float8 dt6 = new AS400Float8();
        AS400ZonedDecimal dt7 = new AS400ZonedDecimal(22,0);
        AS400ByteArray dt8 = new AS400ByteArray(12);

        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 8)
             failed("Incorrect number of fields. Should be 8 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1") || !fieldNames[1].equals("field2") ||
                 !fieldNames[2].equals("field3") || !fieldNames[3].equals("field4") ||
                 !fieldNames[4].equals("field5") || !fieldNames[5].equals("field6") ||
                 !fieldNames[6].equals("field7") || !fieldNames[7].equals("field8"))
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                FieldDescription field5 = recFmt.getFieldDescription(4);
                FieldDescription field6 = recFmt.getFieldDescription(5);
                FieldDescription field7 = recFmt.getFieldDescription(6);
                FieldDescription field8 = recFmt.getFieldDescription(7);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                {
                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != dt4.getClass() ||
                     field4.getDataType().getByteLength() != dt4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }
                else if ( field5.getDataType().getClass() != dt5.getClass() ||
                     field5.getDataType().getByteLength() != dt5.getByteLength() )
                {
                        failed("Bad data type or length returned for field 5.");
                        return;
                }
                else if ( field6.getDataType().getClass() != dt6.getClass() ||
                     field6.getDataType().getByteLength() != dt6.getByteLength() )
                {
                        failed("Bad data type or length returned for field 6.");
                        return;
                }
                else if ( field7.getDataType().getClass() != dt7.getClass() ||
                     field7.getDataType().getByteLength() != dt7.getByteLength() )
                {
                        failed("Bad data type or length returned for field 7.");
                        return;
                }
                else if ( field8.getDataType().getClass() != dt8.getClass() ||
                     field8.getDataType().getByteLength() != dt8.getByteLength() )
                {
                        failed("Bad data type or length returned for field 8.");
                        return;
                }
             if ( ((PackedDecimalFieldDescription) field2).getDecimalPositions() != 2 || 
                   ((ZonedDecimalFieldDescription) field7).getDecimalPositions() !=0  )
             {
                failed("Bad precision returned for Packed or Zoned field.");
                return;
             }
             if ( ((CharacterFieldDescription) field1).getCCSID().length() != 0)
             {
                failed("Non blank CCSID returned for char field.");
                return;
             }
            }
           }
        succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
    }


    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains one &lt;data&gt; element for each supported type. Set
     the attribute values in the elements to a variety of valid attribute values
     ensure the correct record format is created with the fields and attributes set 
     correctly.
     **/
    public void Var010()
    {
	

        AS400Text dt1= new AS400Text(25);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,3);
        AS400UnsignedBin2 dt3 = new AS400UnsignedBin2();
        AS400UnsignedBin4 dt4 = new AS400UnsignedBin4();
        AS400Float4 dt5 = new AS400Float4();
        AS400Float8 dt6 = new AS400Float8();
        AS400ZonedDecimal dt7 = new AS400ZonedDecimal(22,5);
        AS400ByteArray dt8 = new AS400ByteArray(12);

        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 8)
             failed("Incorrect number of fields. Should be 8 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1") || !fieldNames[1].equals("field2") ||
                 !fieldNames[2].equals("field3") || !fieldNames[3].equals("field4") ||
                 !fieldNames[4].equals("field5") || !fieldNames[5].equals("field6") ||
                 !fieldNames[6].equals("field7") || !fieldNames[7].equals("field8"))
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                FieldDescription field5 = recFmt.getFieldDescription(4);
                FieldDescription field6 = recFmt.getFieldDescription(5);
                FieldDescription field7 = recFmt.getFieldDescription(6);
                FieldDescription field8 = recFmt.getFieldDescription(7);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                {

                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != dt4.getClass() ||
                     field4.getDataType().getByteLength() != dt4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }
                else if ( field5.getDataType().getClass() != dt5.getClass() ||
                     field5.getDataType().getByteLength() != dt5.getByteLength() )
                {
                        failed("Bad data type or length returned for field 5.");
                        return;
                }
                else if ( field6.getDataType().getClass() != dt6.getClass() ||
                     field6.getDataType().getByteLength() != dt6.getByteLength() )
                {
                        failed("Bad data type or length returned for field 6.");
                        return;
                }
                else if ( field7.getDataType().getClass() != dt7.getClass() ||
                     field7.getDataType().getByteLength() != dt7.getByteLength() )
                {
                        failed("Bad data type or length returned for field 7.");
                        return;
                }
                else if ( field8.getDataType().getClass() != dt8.getClass() ||
                     field8.getDataType().getByteLength() != dt8.getByteLength() )
                {
                        failed("Bad data type or length returned for field 8.");
                        return;
                }
             if ( ((PackedDecimalFieldDescription) field2).getDecimalPositions() != 5 || 
                   ((ZonedDecimalFieldDescription) field7).getDecimalPositions() !=7  )
             {
                failed("Non zero precision specified for Packed or Zoned field.");
                return;
             }
             if ( !((CharacterFieldDescription) field1).getCCSID().equals("37"))
             {
                failed("Bad CCSID returned for char field.");
                return;
             }
            }
           }
        succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
    }

    /**
     Test toRecordFormat(formatName).  Pass in rfml document     
     representing QCUSTCDT format and ensure proper format generated. 
     **/
    public void Var011()
    {
	
        AS400ZonedDecimal dt1= new AS400ZonedDecimal(6,0);
        AS400Text dt2 = new AS400Text(8);
        AS400Text dt3 = new AS400Text(3);
        AS400Text dt4 = new AS400Text(13);
        AS400Text dt5 = new AS400Text(6);
        AS400Text dt6 = new AS400Text(2);
        AS400ZonedDecimal dt7 = new AS400ZonedDecimal(5,0);
        AS400ZonedDecimal dt8 = new AS400ZonedDecimal(4,0);
        AS400ZonedDecimal dt9 = new AS400ZonedDecimal(1,0);
        AS400ZonedDecimal dt10 = new AS400ZonedDecimal(6,2);
        AS400ZonedDecimal dt11 = new AS400ZonedDecimal(6,2);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("cusrec");
           if (recFmt.getNumberOfFields() != 11)
             failed("Incorrect number of fields. Should be 11 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("cusnum") || !fieldNames[1].equals("lstnam") ||
                 !fieldNames[2].equals("init") || !fieldNames[3].equals("street") ||
                 !fieldNames[4].equals("city") || !fieldNames[5].equals("state") ||
                 !fieldNames[6].equals("zipcod") || !fieldNames[7].equals("cdtlmt") ||
                 !fieldNames[8].equals("chgcod") || !fieldNames[9].equals("baldue") ||
                 !fieldNames[10].equals("cdtdue") )
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                FieldDescription field5 = recFmt.getFieldDescription(4);
                FieldDescription field6 = recFmt.getFieldDescription(5);
                FieldDescription field7 = recFmt.getFieldDescription(6);
                FieldDescription field8 = recFmt.getFieldDescription(7);
                FieldDescription field9 = recFmt.getFieldDescription(8);
                FieldDescription field10 = recFmt.getFieldDescription(9);
                FieldDescription field11 = recFmt.getFieldDescription(10);

                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                {
                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != dt4.getClass() ||
                     field4.getDataType().getByteLength() != dt4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }
                else if ( field5.getDataType().getClass() != dt5.getClass() ||
                     field5.getDataType().getByteLength() != dt5.getByteLength() )
                {
                        failed("Bad data type or length returned for field 5.");
                        return;
                }
                else if ( field6.getDataType().getClass() != dt6.getClass() ||
                     field6.getDataType().getByteLength() != dt6.getByteLength() )
                {
                        failed("Bad data type or length returned for field 6.");
                        return;
                }
                else if ( field7.getDataType().getClass() != dt7.getClass() ||
                     field7.getDataType().getByteLength() != dt7.getByteLength() )
                {
                        failed("Bad data type or length returned for field 7.");
                        return;
                }
                else if ( field8.getDataType().getClass() != dt8.getClass() ||
                     field8.getDataType().getByteLength() != dt8.getByteLength() )
                {
                        failed("Bad data type or length returned for field 8.");
                        return;
                }
                else if ( field9.getDataType().getClass() != dt9.getClass() ||
                     field9.getDataType().getByteLength() != dt9.getByteLength() )
                {
                        failed("Bad data type or length returned for field 9.");
                        return;
                }
                else if ( field10.getDataType().getClass() != dt10.getClass() ||
                     field10.getDataType().getByteLength() != dt10.getByteLength() )
                {
                        failed("Bad data type or length returned for field 10.");
                        return;
                }
                else if ( field11.getDataType().getClass() != dt11.getClass() ||
                     field11.getDataType().getByteLength() != dt11.getByteLength() )
                {
                        failed("Bad data type or length returned for field 11.");
                        return;
                }

             if ( ((ZonedDecimalFieldDescription) field1).getDecimalPositions() != 0 || 
                   ((ZonedDecimalFieldDescription) field7).getDecimalPositions() !=0 ||
                   ((ZonedDecimalFieldDescription) field8).getDecimalPositions() !=0 ||
                   ((ZonedDecimalFieldDescription) field9).getDecimalPositions() !=0 ||
                   ((ZonedDecimalFieldDescription) field10).getDecimalPositions() != 2 ||
                   ((ZonedDecimalFieldDescription) field11).getDecimalPositions() != 2)
            {
                failed("Bad precision specified for Zoned field.");
                return;
             }
             if ( !((CharacterFieldDescription) field2).getCCSID().equals("37") ||
                  !((CharacterFieldDescription) field3).getCCSID().equals("37") ||
                  !((CharacterFieldDescription) field4).getCCSID().equals("37") ||
                  !((CharacterFieldDescription) field5).getCCSID().equals("37") ||
                  !((CharacterFieldDescription) field6).getCCSID().equals("37") )
             {
System.out.println("ccsid = " + ((CharacterFieldDescription) field2).getCCSID());
                failed("Bad CCSID returned for char field.");
                return;
             }
            }
           }
        succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }


    }

    /**
     toRecordFormat(formatName).  Pass in formatName that maps to a &lt;recordformat&gt; element
     that contains one &lt;data&gt; element in the rfml document. Ensure the formatName
     maps to the third &lt;recordformat&gt; tag in the rfml document to ensure
     the proper record format can be created when not using the first formatName in the
     rfml document.
     **/
    public void Var012()
    {
	
        AS400Text dt = new AS400Text(8);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt2a");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format3");
           if (recFmt.getNumberOfFields() != 1)
             failed("Incorrect number of fields. Should be 1 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1"))
                failed("Bad field name returned.");
             else 
             {

                FieldDescription field1 = recFmt.getFieldDescription(0);
                if ( field1.getDataType().getClass() == dt.getClass() &&
                     field1.getDataType().getByteLength() == dt.getByteLength())
                   succeeded();
                else 
                   failed("Bad data type or length returned for field.");
             }
           }
//           RecordFormat recFmt2 = new RecordFormat("format2");
//           CharacterFieldDescription field1 = new CharacterFieldDescription();
//           field1.setDataType(new AS400Text(8));
//           recFmt.addFieldDescription(field1);
//           Record newRec = new Record(recFmt);
//           String[] fieldNames = recFmt2.getFieldNames();
//           System.out.println("Field name = " + fieldNames[0] );
//           failed("Exception not thrown.");
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }

    /**
     Test toRecordFormat(formatName). Pass in formatName that maps to a &lt;recordformat&gt;
     that contains only one &lt;struct&gt; element with no attributes including name
     that contains one &lt;data&gt; element that contains only type and length attributes.
     This should cause a one field format to be created with default values set.
     **/
    public void Var013()
    {
	
        AS400Text dt = new AS400Text(10);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtwithStruct0");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           Record newRec = rfmlDoc.toRecord("format1");
           newRec.setField("struct1","newField1");

           if (recFmt.getNumberOfFields() != 2)
             failed("Incorrect number of fields. Should be 2 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
            // Create an AS400 Toolbox Record from recFmt
            Record rec = new Record(recFmt);

            if (!fieldNames[0].equals("struct1"))
                failed("Bad field name returned."+rec);
            else 
            {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                if ( field1.getDataType().getClass() == dt.getClass() &&
                     field1.getDataType().getByteLength() == dt.getByteLength())
                   succeeded();
                else 
                   failed("Bad data type or length returned for field.");
             }
           }
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
    }

    /**
     Test toRecordFormat(formatName). Pass in formatName that maps to a &lt;recordformat&gt;
     that contains only one &lt;struct&gt; element with name set
     that contains one &lt;data&gt; element that contains name, type and length attributes.
     This should cause a one field format to be created..
     **/
    public void Var014()
    {
	
        AS400Text dt = new AS400Text(1);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtwithStruct1");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 1)
             failed("Incorrect number of fields. Should be 1 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("struct1"))   // Check field name!
                failed("Bad field name returned.");
             else 
             {

                FieldDescription field1 = recFmt.getFieldDescription(0);
                if ( field1.getDataType().getClass() != dt.getClass() ||
                     field1.getDataType().getByteLength() != dt.getByteLength())
                   failed("Bad data type or length returned for field.");
                else if ( !((CharacterFieldDescription) field1).getCCSID().equals("56"))
                  failed("Bad ccsid value in field.");
             }
           }
        succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }


    /**
     Test toRecordFormat(formatName). Pass in formatName that maps to a &lt;recordformat&gt;
     that contains two &lt;struct&gt; elements that have a name only set that each
     contain &lt;data&gt; elements with type and length only specified. Assume the first struct
     contains one element and the second struct contains 2 elements.
     This should cause a three field record format to be created with default values set.
     **/

    public void Var015()
    {
	
        AS400Text dt1= new AS400Text(500);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,0);
        AS400ZonedDecimal dt3  = new AS400ZonedDecimal(8,0);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtwithStruct2");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 3)
             failed("Incorrect number of fields. Should be 3 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("struct1") || !fieldNames[1].equals("s2field1") ||
                 !fieldNames[2].equals("s2field2") ) {
                failed("Bad field name returned.");
                return;
             }
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                   failed("Bad data type or length returned for field 1.");
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                        failed("Bad data type or length returned for field 2.");
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                        failed("Bad data type or length returned for field 3.");
                if ( ((PackedDecimalFieldDescription) field2).getDecimalPositions() != 0 || 
                     ((ZonedDecimalFieldDescription) field3).getDecimalPositions() !=0  )
                {
                    failed("Non zero precision specified for Packed or Zoned field.");
                    return;
                }
                if ( !((CharacterFieldDescription) field1).getCCSID().equals(""))
                {
                   failed("Bad CCSID returned for char field.");
                   return;
                }
             }
          }
          succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
    }

    /**
     Test toRecordFormat(formatName). Pass in formatName that maps to a &lt;recordformat&gt;
     that contains two &lt;struct&gt; elements that have a name only set that each
     contain &lt;data&gt; elements with all attributes set. Assume the first struct
     contains one element and the second struct contains 2 elements.
     This should cause a three field record format to be created with proper values set.
     **/

    public void Var016()
    {
	
        AS400Text dt1= new AS400Text(500);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,0);
        AS400ZonedDecimal dt3  = new AS400ZonedDecimal(8,0);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtwithStruct2a");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 3)
             failed("Incorrect number of fields. Should be 3 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("struct1") || !fieldNames[1].equals("s2field1") ||
                 !fieldNames[2].equals("s2field2") )
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                   failed("Bad data type or length returned for field 1.");
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                        failed("Bad data type or length returned for field 2.");
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                        failed("Bad data type or length returned for field 3.");
                if ( ((PackedDecimalFieldDescription) field2).getDecimalPositions() != 5 || 
                      ((ZonedDecimalFieldDescription) field3).getDecimalPositions() !=4  )
                {
                    failed("Bad precision specified for Packed or Zoned field.");
                    return;
                }
                if ( !((CharacterFieldDescription) field1).getCCSID().equals("59"))
                {
                   failed("Bad CCSID returned for char field.");
                   return;
                }
              }
           }
           succeeded();
         }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
   }

    /**
     Test toRecordFormat(formatName). Pass in formatName that maps to a &lt;recordformat&gt;
     that contains one &lt;struct&gt; element that contains
     one &lt;struct&gt; element that contains two &lt;data&gt; elements.
     Ensure proper record format is created.
     **/
    public void Var017()
    {
	
        AS400Text dt1= new AS400Text(18);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,8);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtStructInStruct");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 4)
             failed("Incorrect number of fields. Should be 4 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("CharacterFieldValue") || 
                 !fieldNames[1].equals("PackedFieldValue")    || 
                 !fieldNames[2].equals("CharacterFieldValue") || 
                 !fieldNames[3].equals("PackedFieldValue"))
                failed("Bad  name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                if ( field3.getDataType().getClass() != dt1.getClass() ||
                     field3.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 3.");
                   return;
                }
                else if ( field4.getDataType().getClass() != dt2.getClass() ||
                     field4.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }

                CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(0);
                PackedDecimalFieldDescription packed1 = (PackedDecimalFieldDescription) recFmt.getFieldDescription(1);
                if (! (char1.getCCSID().equals("2039")))
                {
                   failed("Bad ccsid value returned.");
                   return;
                }
                if (packed1.getDecimalPositions() != 8)
                {
                   failed("Bad precision value returned.");
                   return;
                }
            }
           succeeded();
           }
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }

    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains one &lt;struct&gt; struct element that contains a data element
     &lt;data&gt; element for each type with all attribute values set. Ensure the proper record format is 
     returned.
     **/

    public void Var018()
    {
	
        AS400Text dt1= new AS400Text(25);
        AS400PackedDecimal dt2 = new AS400PackedDecimal(10,3);
        AS400UnsignedBin2 dt3 = new AS400UnsignedBin2();
        AS400UnsignedBin4 dt4 = new AS400UnsignedBin4();
        AS400Float4 dt5 = new AS400Float4();
        AS400Float8 dt6 = new AS400Float8();
        AS400ZonedDecimal dt7 = new AS400ZonedDecimal(22,5);
        AS400ByteArray dt8 = new AS400ByteArray(12);

        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtwithStructAll");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 8)
             failed("Incorrect number of fields. Should be 8 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1") || !fieldNames[1].equals("field2") ||
                 !fieldNames[2].equals("field3") || !fieldNames[3].equals("field4") ||
                 !fieldNames[4].equals("field5") || !fieldNames[5].equals("field6") ||
                 !fieldNames[6].equals("field7") || !fieldNames[7].equals("field8"))
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                FieldDescription field5 = recFmt.getFieldDescription(4);
                FieldDescription field6 = recFmt.getFieldDescription(5);
                FieldDescription field7 = recFmt.getFieldDescription(6);
                FieldDescription field8 = recFmt.getFieldDescription(7);
                if ( field1.getDataType().getClass() != dt1.getClass() ||
                     field1.getDataType().getByteLength() != dt1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != dt2.getClass() ||
                     field2.getDataType().getByteLength() != dt2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != dt3.getClass() ||
                     field3.getDataType().getByteLength() != dt3.getByteLength() )
                {
                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != dt4.getClass() ||
                     field4.getDataType().getByteLength() != dt4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }
                else if ( field5.getDataType().getClass() != dt5.getClass() ||
                     field5.getDataType().getByteLength() != dt5.getByteLength() )
                {
                        failed("Bad data type or length returned for field 5.");
                        return;
                }
                else if ( field6.getDataType().getClass() != dt6.getClass() ||
                     field6.getDataType().getByteLength() != dt6.getByteLength() )
                {
                        failed("Bad data type or length returned for field 6.");
                        return;
                }
                else if ( field7.getDataType().getClass() != dt7.getClass() ||
                     field7.getDataType().getByteLength() != dt7.getByteLength() )
                {
                        failed("Bad data type or length returned for field 7.");
                        return;
                }
                else if ( field8.getDataType().getClass() != dt8.getClass() ||
                     field8.getDataType().getByteLength() != dt8.getByteLength() )
                {
                        failed("Bad data type or length returned for field 8.");
                        return;
                }
             if ( ((PackedDecimalFieldDescription) field2).getDecimalPositions() != 5 || 
                   ((ZonedDecimalFieldDescription) field7).getDecimalPositions() !=7  )
             {
                failed("Bad precision specified for Packed or Zoned field.");
                return;
             }
             if ( !((CharacterFieldDescription) field1).getCCSID().equals("48"))
             {
                failed("Bad CCSID returned for char field.");
                return;
             }
            }
           }
        succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }
    }


    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains a complex combination of nested structs.  Ensure proper
     record format is created
     **/
    public void Var019()
    {
	
        AS400Text s3field1= new AS400Text(18,37);
        AS400PackedDecimal s3field2 = new AS400PackedDecimal(8,3);
        AS400ZonedDecimal s2field1 = new AS400ZonedDecimal(5,2);
        AS400Text format1_field1 = new AS400Text(10,65535);
        AS400Bin2 struct4 = new AS400Bin2();
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtStruct3");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 15)
             failed("Incorrect number of fields. Should be 15 but is " + recFmt.getNumberOfFields());
           else {

             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("s3field1") ||
                 !fieldNames[1].equals("s3field2") ||
                 !fieldNames[2].equals("struct4") ||
                 !fieldNames[3].equals("s2field1") ||
                 !fieldNames[4].equals("s3field1") ||
                 !fieldNames[5].equals("s3field2") ||
                 !fieldNames[6].equals("struct4") ||
                 !fieldNames[7].equals("s3field1") ||
                 !fieldNames[8].equals("s3field2") ||
                 !fieldNames[9].equals("struct4") ||
                 !fieldNames[10].equals("s2field1") ||
                 !fieldNames[11].equals("s3field1") ||
                 !fieldNames[12].equals("s3field2") ||
                 !fieldNames[13].equals("struct4") ||
                 !fieldNames[14].equals("field1") )
              {
                 failed("Bad field name returned.");
                 return;
              }
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                FieldDescription field5 = recFmt.getFieldDescription(4);
                FieldDescription field6 = recFmt.getFieldDescription(5);
                FieldDescription field7 = recFmt.getFieldDescription(6);
                FieldDescription field8 = recFmt.getFieldDescription(7);
                FieldDescription field9 = recFmt.getFieldDescription(8);
                FieldDescription field10 = recFmt.getFieldDescription(9);
                FieldDescription field11 = recFmt.getFieldDescription(10);
                FieldDescription field12 = recFmt.getFieldDescription(11);
                FieldDescription field13 = recFmt.getFieldDescription(12);
                FieldDescription field14 = recFmt.getFieldDescription(13);
                FieldDescription field15 = recFmt.getFieldDescription(14);


                if ( field1.getDataType().getClass() != s3field1.getClass() ||
                     field1.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != s3field2.getClass() ||
                     field2.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != struct4.getClass() ||
                     field3.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != s2field1.getClass() ||
                     field4.getDataType().getByteLength() != s2field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }
                else if ( field5.getDataType().getClass() != s3field1.getClass() ||
                     field5.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 5.");
                        return;
                }
                else if ( field6.getDataType().getClass() != s3field2.getClass() ||
                     field6.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 6.");
                        return;
                }
                else if ( field7.getDataType().getClass() != struct4.getClass() ||
                     field7.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 7.");
                        return;
                }
                else if ( field8.getDataType().getClass() != s3field1.getClass() ||
                     field8.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 8.");
                        return;
                }
                else if ( field9.getDataType().getClass() != s3field2.getClass() ||
                     field9.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 9.");
                        return;
                }
                else if ( field10.getDataType().getClass() != struct4.getClass() ||
                     field10.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 10.");
                        return;
                }

                else if ( field11.getDataType().getClass() != s2field1.getClass() ||
                     field11.getDataType().getByteLength() != s2field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 11.");
                        return;
                }
                else if ( field12.getDataType().getClass() != s3field1.getClass() ||
                     field12.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 12.");
                        return;
                }
                else if ( field13.getDataType().getClass() != s3field2.getClass() ||
                     field13.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 13.");
                        return;
                }
                else if ( field14.getDataType().getClass() != struct4.getClass() ||
                     field14.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 14.");
                        return;
                }
                else if ( field15.getDataType().getClass() != format1_field1.getClass() ||
                     field15.getDataType().getByteLength() != format1_field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 15.");
                        return;
                }
                CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(0);
                ZonedDecimalFieldDescription zoned1 = (ZonedDecimalFieldDescription) recFmt.getFieldDescription(3);
                CharacterFieldDescription char2 = (CharacterFieldDescription) recFmt.getFieldDescription(14);
                PackedDecimalFieldDescription packed1 = (PackedDecimalFieldDescription) recFmt.getFieldDescription(5);
                if (! (char1.getCCSID().equals("37")))
                {
                   failed("Bad ccsid value returned for first char field.");
                   return;
                }
                if (! (char2.getCCSID().equals("65535")))
                {
                   failed("Bad ccsid value returned for second char field.");
                   return;
                }
                if (zoned1.getDecimalPositions() != 2)
                {
                   failed("Bad precision value returned for zoned field.");
                   return;
                }
                if (packed1.getDecimalPositions() != 3)
                {
                   failed("Bad precision value returned for packed field.");
                   return;
                }

            }
           succeeded();
           }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }

    }

    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains a complex combination of nested structs.  Ensure proper
     record format is created
     **/

    public void Var020()
    {
	
        AS400ZonedDecimal s2field1= new AS400ZonedDecimal(5,2);
        AS400Text s3field1 = new AS400Text(18,37);
        AS400Text format1_field1 = new AS400Text(10,65535);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtStruct2");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 4)
             failed("Incorrect number of fields. Should be 4 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("struct1") ||
                !fieldNames[1].equals("struct2") ||
                !fieldNames[2].equals("struct3")  ||
                !fieldNames[3].equals("field1") )
                failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                if ( field1.getDataType().getClass() != s2field1.getClass() ||
                     field1.getDataType().getByteLength() != s2field1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != s2field1.getClass() ||
                     field2.getDataType().getByteLength() != s2field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != s3field1.getClass() ||
                     field3.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != format1_field1.getClass() ||
                     field4.getDataType().getByteLength() != format1_field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }


                ZonedDecimalFieldDescription zoned1 = (ZonedDecimalFieldDescription) recFmt.getFieldDescription(0);
                CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(2);
                CharacterFieldDescription char2 = (CharacterFieldDescription) recFmt.getFieldDescription(3);
                if (! (char1.getCCSID().equals("37")))
                {
                   failed("Bad ccsid value returned for first char field.");
                   return;
                }
                if (! (char2.getCCSID().equals("65535")))
                {
                   failed("Bad ccsid value returned for second char field.");
                   return;
                }

                if (zoned1.getDecimalPositions() != 2)
                {
                   failed("Bad precision value returned for zoned field.");
                   return;
                }
            }
           succeeded();
           }
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }


    }


    /**
     Test toRecordFormat(formatName) that maps to a &lt;recordformat&gt; element that 
     exists and contains 3 &lt;data&gt; elements - 2 structs and a data field. Make
     one of the structs a simple struct and one a nested struct. Ensure the proper
     record format is created.
     **/
     public void Var021()
     {
	
        AS400Text ffield1 = new AS400Text(10,65535);
        AS400ZonedDecimal s2field1 = new AS400ZonedDecimal(5,2);
        AS400PackedDecimal s3field2 = new AS400PackedDecimal(8,3);
        AS400Bin2 struct4 = new AS400Bin2();
        AS400Text s3field1 = new AS400Text(18,37);
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtStruct3");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 15)
             failed("Incorrect number of fields. Should be 15 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("s3field1") ||
                !fieldNames[1].equals("s3field2") ||
                !fieldNames[2].equals("struct4")  ||
                !fieldNames[3].equals("s2field1") ||
                !fieldNames[4].equals("s3field1") ||
                !fieldNames[5].equals("s3field2") ||
                !fieldNames[6].equals("struct4") ||
                !fieldNames[7].equals("s3field1") ||
                !fieldNames[8].equals("s3field2") ||
                !fieldNames[9].equals("struct4") ||
                !fieldNames[10].equals("s2field1") ||
                !fieldNames[11].equals("s3field1") ||
                !fieldNames[12].equals("s3field2") ||
                !fieldNames[13].equals("struct4") ||
                !fieldNames[14].equals("field1"))  	
                  failed("Bad field name returned.");
             else 
             {
                FieldDescription field1 = recFmt.getFieldDescription(0);
                FieldDescription field2 = recFmt.getFieldDescription(1);
                FieldDescription field3 = recFmt.getFieldDescription(2);
                FieldDescription field4 = recFmt.getFieldDescription(3);
                FieldDescription field5 = recFmt.getFieldDescription(4);
                FieldDescription field6 = recFmt.getFieldDescription(5);
                FieldDescription field7 = recFmt.getFieldDescription(6);
                FieldDescription field8 = recFmt.getFieldDescription(7);
                FieldDescription field9 = recFmt.getFieldDescription(8);
                FieldDescription field10 = recFmt.getFieldDescription(9);
                FieldDescription field11 = recFmt.getFieldDescription(10);
                FieldDescription field12 = recFmt.getFieldDescription(11);
                FieldDescription field13 = recFmt.getFieldDescription(12);
                FieldDescription field14 = recFmt.getFieldDescription(13);
                FieldDescription field15 = recFmt.getFieldDescription(14);

                if ( field1.getDataType().getClass() != s3field1.getClass() ||
                     field1.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != s3field2.getClass() ||
                     field2.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                else if ( field3.getDataType().getClass() != struct4.getClass() ||
                     field3.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 3.");
                        return;
                }
                else if ( field4.getDataType().getClass() != s2field1.getClass() ||
                     field4.getDataType().getByteLength() != s2field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 4.");
                        return;
                }
                else if ( field5.getDataType().getClass() != s3field1.getClass() ||
                     field5.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 5.");
                        return;
                }
                else if ( field6.getDataType().getClass() != s3field2.getClass() ||
                     field6.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 6.");
                        return;
                }
                else if ( field7.getDataType().getClass() != struct4.getClass() ||
                     field7.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 7.");
                        return;
                }
                else if ( field8.getDataType().getClass() != s3field1.getClass() ||
                     field8.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 8.");
                        return;
                }
                else if ( field9.getDataType().getClass() != s3field2.getClass() ||
                     field9.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 9.");
                        return;
                }
                else if ( field10.getDataType().getClass() != struct4.getClass() ||
                     field10.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 10.");
                        return;
                }
                else if ( field11.getDataType().getClass() != s2field1.getClass() ||
                     field11.getDataType().getByteLength() != s2field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 11.");
                        return;
                }
                else if ( field12.getDataType().getClass() != s3field1.getClass() ||
                     field12.getDataType().getByteLength() != s3field1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 12.");
                        return;
                }
                else if ( field13.getDataType().getClass() != s3field2.getClass() ||
                     field13.getDataType().getByteLength() != s3field2.getByteLength() )
                {
                        failed("Bad data type or length returned for field 13.");
                        return;
                }
                else if ( field14.getDataType().getClass() != struct4.getClass() ||
                     field14.getDataType().getByteLength() != struct4.getByteLength() )
                {
                        failed("Bad data type or length returned for field 14.");
                        return;
                }
                else if ( field15.getDataType().getClass() != ffield1.getClass() ||
                     field15.getDataType().getByteLength() != ffield1.getByteLength() )
                {
                        failed("Bad data type or length returned for field 10.");
                        return;
                }



                CharacterFieldDescription char1 = (CharacterFieldDescription) recFmt.getFieldDescription(0);
                ZonedDecimalFieldDescription zoned1 = (ZonedDecimalFieldDescription) recFmt.getFieldDescription(3);
                PackedDecimalFieldDescription packed1 = (PackedDecimalFieldDescription) recFmt.getFieldDescription(1);
                BinaryFieldDescription bin2 = (BinaryFieldDescription) recFmt.getFieldDescription(2);
                CharacterFieldDescription char2 = (CharacterFieldDescription) recFmt.getFieldDescription(14);
                if (! (char1.getCCSID().equals("37")))
                {
                   failed("Bad ccsid value returned for s3field1 char field."+bin2);
                   return;
                }
                if (! (char2.getCCSID().equals("65535")))
                {
                   failed("Bad ccsid value returned for format1.field1 char field.");
                   return;
                }
                if (zoned1.getDecimalPositions() != 2)
                {
                   failed("Bad precision value returned for first zoned field.");
                   return;
                }
                if (packed1.getDecimalPositions() != 3)
                {
                   failed("Bad precision value returned for packed field.");
                   return;
                }

            }
           succeeded();
           }
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }

   
    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains two float &lt;data&gt; elements
     **/
    public void Var022()
    {
	
        AS400Float4 field1_dt= new AS400Float4();
        AS400Float8 field2_dt = new AS400Float8();
        FloatFieldDescription field1_desc = new FloatFieldDescription(field1_dt, "field1");
        FloatFieldDescription field2_desc = new FloatFieldDescription(field2_dt, "field2");
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtFloatv");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
           if (recFmt.getNumberOfFields() != 2)
             failed("Incorrect number of fields. Should be 2 but is " + recFmt.getNumberOfFields());
           else {
             String[] fieldNames = recFmt.getFieldNames();
             if (!fieldNames[0].equals("field1") || !fieldNames[1].equals("field2") )
                failed("Bad empty field name returned."+field2_desc);
             else 
             {
                FloatFieldDescription field1 = (FloatFieldDescription) recFmt.getFieldDescription(0);
                FloatFieldDescription field2 = (FloatFieldDescription) recFmt.getFieldDescription(1);

                if ( field1.getDataType().getClass() != field1_dt.getClass() ||
                     field1.getDataType().getByteLength() != field1_dt.getByteLength() )
                {
                   failed("Bad data type or length returned for field 1.");
                   return;
                }
                else if ( field2.getDataType().getClass() != field2_dt.getClass() ||
                     field2.getDataType().getByteLength() != field2_dt.getByteLength() )
                {
                        failed("Bad data type or length returned for field 2.");
                        return;
                }
                if (field1_desc.getDecimalPositions() != 0)
                {
                   failed("Bad precision value returned.");
                   return;
                }
            }
           succeeded();
           }
        }
        catch (Exception e)
        {
           failed(e, "Unexpected exception thrown.");
        }

    }


    /**
     Test toRecordFormat(formatName). Map to formatName that exists in the current rfml
     document and contains one &lt;data&gt; element for each supported type with no   
     initial values set and no other attributes set except length and type.
     Specify some of the data elements as "key fields".
     Ensure proper format created with 
     proper default values for each type and for each attribute.
     **/
    public void Var023()
    {
	
      AS400Text dt1= new AS400Text(25);
      AS400PackedDecimal dt2 = new AS400PackedDecimal(10,2);
      AS400Bin2 dt3 = new AS400Bin2();
      AS400Bin4 dt4 = new AS400Bin4();
      AS400Float4 dt5 = new AS400Float4();
      AS400Float8 dt6 = new AS400Float8();
      AS400ZonedDecimal dt7 = new AS400ZonedDecimal(22,0);
      AS400ByteArray dt8 = new AS400ByteArray(12);
      AS400ByteArray dt9 = new AS400ByteArray(12);

      File file1 = null;
      RandomAccessFile raFile1 = null;

      try
      {
        RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5k");
        RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
        if (recFmt.getNumberOfFields() != 9) {
          failed("Incorrect number of fields. Should be 9 but is " + recFmt.getNumberOfFields());
          return;
        }

        String[] fieldNames = recFmt.getFieldNames();
        if (!fieldNames[0].equals("field1") || !fieldNames[1].equals("field2") ||
            !fieldNames[2].equals("field3") || !fieldNames[3].equals("field4") ||
            !fieldNames[4].equals("field5") || !fieldNames[5].equals("field6") ||
            !fieldNames[6].equals("field7") || !fieldNames[7].equals("field8") ||
            !fieldNames[8].equals("field9hasALongName"))
          failed("Bad field name returned.");
        else 
        {
          FieldDescription field1 = recFmt.getFieldDescription(0);
          FieldDescription field2 = recFmt.getFieldDescription(1);
          FieldDescription field3 = recFmt.getFieldDescription(2);
          FieldDescription field4 = recFmt.getFieldDescription(3);
          FieldDescription field5 = recFmt.getFieldDescription(4);
          FieldDescription field6 = recFmt.getFieldDescription(5);
          FieldDescription field7 = recFmt.getFieldDescription(6);
          FieldDescription field8 = recFmt.getFieldDescription(7);
          FieldDescription field9 = recFmt.getFieldDescription(8);
          if ( field1.getDataType().getClass() != dt1.getClass() ||
               field1.getDataType().getByteLength() != dt1.getByteLength() )
          {
            failed("Bad data type or length returned for field 1.");
            return;
          }
          else if ( field2.getDataType().getClass() != dt2.getClass() ||
                    field2.getDataType().getByteLength() != dt2.getByteLength() )
          {
            failed("Bad data type or length returned for field 2.");
            return;
          }
          else if ( field3.getDataType().getClass() != dt3.getClass() ||
                    field3.getDataType().getByteLength() != dt3.getByteLength() )
          {
            failed("Bad data type or length returned for field 3.");
            return;
          }
          else if ( field4.getDataType().getClass() != dt4.getClass() ||
                    field4.getDataType().getByteLength() != dt4.getByteLength() )
          {
            failed("Bad data type or length returned for field 4.");
            return;
          }
          else if ( field5.getDataType().getClass() != dt5.getClass() ||
                    field5.getDataType().getByteLength() != dt5.getByteLength() )
          {
            failed("Bad data type or length returned for field 5.");
            return;
          }
          else if ( field6.getDataType().getClass() != dt6.getClass() ||
                    field6.getDataType().getByteLength() != dt6.getByteLength() )
          {
            failed("Bad data type or length returned for field 6.");
            return;
          }
          else if ( field7.getDataType().getClass() != dt7.getClass() ||
                    field7.getDataType().getByteLength() != dt7.getByteLength() )
          {
            failed("Bad data type or length returned for field 7.");
            return;
          }
          else if ( field8.getDataType().getClass() != dt8.getClass() ||
                    field8.getDataType().getByteLength() != dt8.getByteLength() )
          {
            failed("Bad data type or length returned for field 8.");
            return;
          }
          else if ( field9.getDataType().getClass() != dt9.getClass() ||
                    field9.getDataType().getByteLength() != dt9.getByteLength() )
          {
            failed("Bad data type or length returned for field 9.");
            return;
          }
          if ( ((PackedDecimalFieldDescription) field2).getDecimalPositions() != 2 || 
               ((ZonedDecimalFieldDescription) field7).getDecimalPositions() !=0  )
          {
            failed("Bad precision returned for Packed or Zoned field.");
            return;
          }
          if ( ((CharacterFieldDescription) field1).getCCSID().length() != 0)
          {
            failed("Non blank CCSID returned for char field.");
            return;
          }

          FieldDescription[] keyFields = recFmt.getKeyFieldDescriptions();
          if (keyFields.length != 3) {
            failed("Incorrect number of key fields fields. Should be 3 but is " + keyFields.length);
            return;
          }
          else {
            String keyName0 = keyFields[0].getFieldName();
            String keyName1 = keyFields[1].getFieldName();
            String keyName2 = keyFields[2].getFieldName();
            if (!keyName0.equals("field3")) {
              failed("Incorrect field name returned for first key field: " + keyName0 + " (DDS name: " + keyFields[0].getDDSName() + ")");
              return;
            }
            if (!keyName1.equals("field8")) {
              failed("Incorrect field name returned for second key field: " + keyName1 + " (DDS name: " + keyFields[1].getDDSName() + ")");
              return;
            }
            if (!keyName2.equals("field9hasALongName")) {
              failed("Incorrect field name returned for third key field: " + keyName2 + " (DDS name: " + keyFields[2].getDDSName() + ")");
              return;
            }
          }
        }

        // Now try a round-trip: Construct a new RecordFormatDocument out of the RecordFormat that we generated earlier, and see what kind of RFML it generates.
        rfmlDoc = new RecordFormatDocument(recFmt);
        file1 = new File("emptyFile");
        file1.createNewFile();  // Note: This method is new in Java2.
        raFile1 = new RandomAccessFile(file1, "r");
        rfmlDoc.toXml(file1);

        // Verify that the file is as expected.
        StringBuffer buffer = new StringBuffer();
        String nextLine = raFile1.readLine();
        while (nextLine != null) {
          buffer.append(nextLine);
          nextLine = raFile1.readLine();
        }

        ///System.out.println("File contents:\n" + buffer.toString());
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"25\"/>    <data name=\"field2\" type=\"packed\" length=\"10\" precision=\"2\" init=\"77888899908.7777\"/>    <data name=\"field3\" type=\"int\" length=\"2\" keyfield=\"true\"/>    <data name=\"field4\" type=\"int\" length=\"4\"/>    <data name=\"field5\" type=\"float\" length=\"4\"/>    <data name=\"field6\" type=\"float\" length=\"8\"/>    <data name=\"field7\" type=\"zoned\" length=\"22\" precision=\"0\"/>    <data name=\"field8\" type=\"byte\" length=\"12\" keyfield=\"true\"/>    <data name=\"field9hasALongName\" type=\"byte\" length=\"12\" keyfield=\"true\"/>  </recordformat></rfml>");

        assertCondition(RMToXmlTestcase.areEqualXml(buffer, expected));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception thrown.");
      }
      finally
      {
        if (raFile1 != null) {
          try { raFile1.close(); } catch (Exception e) {}
        }
        if (file1 != null) {
          try { file1.delete(); } catch (Exception e) {}
        }
      }
    }


    
}
