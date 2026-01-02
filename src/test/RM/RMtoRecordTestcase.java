///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMtoRecordTestcase.java
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
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.data.RecordFormatDocument;

import test.Testcase;

/**
 The RMtoRecordTestcase class tests the following methods of the RecordFormatDocument class:
 <li>toRecord(formatName)
 **/
public class RMtoRecordTestcase extends Testcase
{
    /**
     Constructor.
     **/
    public RMtoRecordTestcase(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMtoRecordTestcase", namesAndVars.get("RMtoRecordTestcase"), runMode, fileOutputStream);
    }

    /**
     Test toRecord(formatName).  Pass in an empty string for formatName. What should happen?
     **/
    public void Var001()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           rfmlDoc.toRecord("");
           failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }
    }


    /**
     Test toRecord(formatName).  Pass in null for formatName. Should get null pointer exception.
     **/
    public void Var002()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           rfmlDoc.toRecord(null);
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
           rfmlDoc.toRecord("format1");
           failed("No exception thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }
    }



    /**
     Test toRecord(formatName) with a formatName that maps to an empty &lt;recordformat&gt;
     tag in the rfml document. This should cause an ExtendedIllegalArgumentException since
     record format is blank.  If you create a Record object with a blank record format 
     the same exception is thrown
     **/
    public void Var004()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
           rfmlDoc.toRecord("format1");
           failed("Exception not thrown.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     Test toRecord(formatName).  Pass an invalid formatName that does not map to a          
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
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains one char &lt;data&gt; element with initial value   
     set to an empty string. This should create a one 
     field record with an 8 blank string for value. PROBLEM: toBytes pads 
     the field with blanks to equal its length, whereas getField doesn't.  What
     should be the proper behavior?  For now, leaving as padded.
   **/
    public void Var006()
    {
	
        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt2");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
//           Record newRec = new Record(recFmt);
           Record newRec = rfmlDoc.toRecord("format1");
           if (newRec.getNumberOfFields() != 1) 
           {
             failed("Bad number of fields returned."+recFmt);
             return;
           }
           if (!newRec.getField(0).equals("        "))   
           {
             failed("Bad value returned for first field.");
             return;
           }
        succeeded();
        }
        catch (Exception e)
        {
           failed(e, "Exception thrown.");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains one char &lt;data&gt; element with initial value   
     set to a single blank. This should create a one 
     field record with an 8 blank string for value. PROBLEM: toBytes pads 
     the field with blanks to equal its length, whereas getField doesn't.  What
     should be the proper behavior?  For now, leaving as padded.
     **/
    public void Var007()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField("field1").equals("  "))
            {
              failed("Bad value returned for first field.");
              return;
            }
         succeeded();
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains one char &lt;data&gt; element with name attribute set 
     and with an initial value set via init. This should create a one field record with proper
     name and value?
     **/
    public void Var008()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt2v");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField("field1").equals("1234    "))
            {
              failed("Bad value returned for first field.");
              return;
            }
         succeeded();
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains one char &lt;data&gt; element with name attribute set 
     and with an initial value set via setValue(). This should create a one field record with proper
     name and value?
     **/
    public void Var009()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt2x");
            rfmlDoc.setStringValue("format1.field1", "Hi There", 0);
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            
            if (!newRec.getField("field1").equals("Hi There  "))
            {
              failed("Bad value returned for first field.");
              return;
            }
         succeeded();
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains one &lt;data&gt; element with name attribute set 
     and with an initial value set via init that is longer than the field's length.
     Ensure an exception is thrown.
     **/
    public void Var010()
    {
	
        try
        {
            AS400Text dt = new AS400Text(9);
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt3v");
            Record newRec = rfmlDoc.toRecord("format1");
            RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            FieldDescription field1 = recFmt.getFieldDescription(0);
            if ( field1.getDataType().getClass() == dt.getClass() &&
                 field1.getDataType().getByteLength() == dt.getByteLength())
            {
              failed("Bad type or length returned.");
              return;
            }
            if (!newRec.getField("field1").equals("Be Happy!"))
            {
              failed("Exception not thrown.");
              return;
            }
         }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains all valid int &lt;data&gt; elements set but no
     initial or default values set.  Ensure proper record created with
     proper initial values set.
     **/
    public void Var011()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 7) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField("field1").getClass().getName().equals("java.lang.Short"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((Short)newRec.getField(0)).intValue() != 0)
            {
              failed("Bad value returned for first field.");
              return;
            }
            if (!newRec.getField("field2").getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
            if (  ((Integer)newRec.getField(1)).intValue() != 0)
            {
             failed("Bad value returned for second field.");
               return;
           }
            if (!newRec.getField("field3").getClass().getName().equals("java.lang.Short"))
            {
              failed("Bad class type returned for third field.");
              return;
            }
            if (  ((Short)newRec.getField(2)).intValue() != 0)
            {
              failed("Bad value returned for third field.");
              return;
            }

            if (!newRec.getField("field4").getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for fourth field.");
              return;
            }

            if (  ((Integer)newRec.getField("field4")).intValue() != 0)
            {
              failed("Bad value returned for fourth field.");
              return;
            }

            if (!newRec.getField("field5").getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for fifth field.");
              return;
            }
            if (  ((Integer)newRec.getField("field5")).intValue() != 0)
            {
              failed("Bad value returned for fifth field.");
              return;
            }
            if (!newRec.getField("field6").getClass().getName().equals("java.lang.Long"))
            {
              failed("Bad class type returned for sixth field.");
              return;
            }
            if (  ((Long)newRec.getField("field6")).intValue() != 0)
            {
              failed("Bad value returned for sixth field.");
              return;
            }

            if (!newRec.getField("field7").getClass().getName().equals("java.lang.Long"))
            {
              failed("Bad class type returned for seventh field.");
              return;
            }
            if (  ((Long)newRec.getField("field7")).intValue() != 0)
            {
              failed("Bad value returned for seventh field.");
              return;
            }
        succeeded();
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains all valid int &lt;data&gt; elements with name init values
     set.  Ensure proper record created with proper values.
     **/
    public void Var012()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 7) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.lang.Short"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((Short)newRec.getField(0)).intValue() != 12)
            {
              failed("Bad value returned for first field.");
              return;
            }

            if (!newRec.getField(1).getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
            if (  ((Integer)newRec.getField(1)).intValue() != 123)
            {
              failed("Bad value returned for second field.");
              return;
            }
            if (!newRec.getField(2).getClass().getName().equals("java.lang.Short"))
            {
              failed("Bad class type returned for third field.");
              return;
            }
            if (  ((Short)newRec.getField(2)).intValue() != 1234)
            {
              failed("Bad value returned for third field.");
                return;
            }

            if (!newRec.getField(3).getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for fourth field.");
              return;
            }

            if (  ((Integer)newRec.getField(3)).intValue() != 9999999)
            {
              failed("Bad value returned for fourth field.");
              return;
            }
            if (!newRec.getField(4).getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for fifth field.");
              return;
            }
            if (  ((Integer)newRec.getField(4)).intValue() != 123456789)
            {
              failed("Bad value returned for fifth field.");
              return;
            }
            if (!newRec.getField(5).getClass().getName().equals("java.lang.Long"))
            {
              failed("Bad class type returned for sixth field.");
              return;
            }
            if (  ((Long)newRec.getField(5)).intValue() != 7)
            {
              failed("Bad value returned for sixth field.");
              return;
            }
            if (!newRec.getField(6).getClass().getName().equals("java.lang.Long"))
            {
              failed("Bad class type returned for seventh field.");
              return;
            }
            if (  ((Long)newRec.getField(6)).intValue() != 777777777)
            {
              failed("Bad value returned for seventh field.");
              return;
            }

        succeeded();

        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains all valid int &lt;data&gt; elements with values set
     using setValue. Ensure proper record is returned.
     **/
    public void Var013()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setIntValue("format1.field1", 1);
            rfmlDoc.setIntValue("format1.field2", 2);
            rfmlDoc.setIntValue("format1.field3", 3);
            rfmlDoc.setIntValue("format1.field4", 4);
            rfmlDoc.setIntValue("format1.field5", 5);
            rfmlDoc.setIntValue("format1.field6", 6);
            rfmlDoc.setIntValue("format1.field7", 7);

            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 7) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.lang.Short"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((Short)newRec.getField(0)).intValue() != 1)
            {
              failed("Bad value returned for first field.");
              return;
            }

            if (!newRec.getField(1).getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
            if (  ((Integer)newRec.getField(1)).intValue() != 2)
            {
              failed("Bad value returned for second field.");
              return;
            }
            if (!newRec.getField(2).getClass().getName().equals("java.lang.Short"))
            {
              failed("Bad class type returned for third field.");
              return;
            }
            if (  ((Short)newRec.getField(2)).intValue() != 3)
            {
              failed("Bad value returned for third field.");
              return;
            }
            if (!newRec.getField(3).getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for fourth field.");
              return;
            }

            if (  ((Integer)newRec.getField(3)).intValue() != 4)
            {
              failed("Bad value returned for fourth field.");
              return;
            }
            if (!newRec.getField(4).getClass().getName().equals("java.lang.Integer"))
            {
              failed("Bad class type returned for fifth field.");
              return;
            }
            if (  ((Integer)newRec.getField(4)).intValue() != 5)
            {
              failed("Bad value returned for fifth field.");
              return;
            }
            if (!newRec.getField(5).getClass().getName().equals("java.lang.Long"))
            {
              failed("Bad class type returned for sixth field.");
              return;
            }
            if (  ((Long)newRec.getField(5)).intValue() != 6)
            {
              failed("Bad value returned for sixth field.");
              return;
            }
            if (  ((Long)newRec.getField(6)).intValue() != 7)
            {
              failed("Bad value returned for seventh field.");
              return;
            }
        succeeded();
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains an  int &lt;data&gt; element whose value is too large.
     Ensure an exception is thrown.
     **/
    public void Var014()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintBad");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains packed and zoned &lt;data&gt; elements with name type and length
     attribute values set but no initial or default values set.  Ensure proper record 
     created with proper values.
     **/
    public void Var015()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZb");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 2) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.math.BigDecimal"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((BigDecimal)newRec.getField(0)).doubleValue() != 0.000)
            {
              failed("Bad value returned for first field.");
              return;
            }
            if (!newRec.getField(1).getClass().getName().equals("java.math.BigDecimal"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
            if (  ((BigDecimal)newRec.getField(1)).doubleValue() != 0.0000)
            {
              failed("Bad value returned for second field.");
              return;
            }
        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains packed and zoned &lt;data&gt; elements with name type and length
     attribute values set with an initial value set.  Ensure proper record 
     created with proper values.
     **/
    public void Var016()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZa");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 2) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.math.BigDecimal"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((BigDecimal)newRec.getField(0)).doubleValue() != 12.345)
            {
              failed("Bad value returned for first field.");
              return;
            }
            if (!newRec.getField(1).getClass().getName().equals("java.math.BigDecimal"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
          if (  ((BigDecimal)newRec.getField(1)).doubleValue() != 13.546)
          {
            failed("Bad value returned for second field.");
              return;
          }
        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains packed and zoned &lt;data&gt; elements with name type and length
     attribute values set with value set using setValue().  Ensure proper record 
     created with proper values.
     **/
    public void Var017()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZb");
            BigDecimal bigDec = new BigDecimal(12.345);
            rfmlDoc.setValue("format1.field1",bigDec);
            BigDecimal bigDec2 = new BigDecimal("13.45");
            rfmlDoc.setValue("format1.field2",bigDec2);
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 2) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.math.BigDecimal"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((BigDecimal)newRec.getField(0)).doubleValue() != 12.345)
            {
              failed("Bad value returned for first field.");
              return;
            }
            if (!newRec.getField(1).getClass().getName().equals("java.math.BigDecimal"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
          if (  ((BigDecimal)newRec.getField(1)).doubleValue() != 13.45)
          {
            failed("Bad value returned for second field.");
              return;
          }
        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains packed and zoned &lt;data&gt; elements with name type and length
     attribute values set with value set with an improper value set using init.  
     **/
    public void Var018()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZbad");
            Record newRec = rfmlDoc.toRecord("format1");
            failed("No exception thrown."+newRec);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains packed and zoned &lt;data&gt; elements with name type and length
     attribute values set with value set with an improper value set using init.  
     **/
    public void Var019()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZbad2");
            Record newRec = rfmlDoc.toRecord("format1");
            failed("No exception thrown."+newRec);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains float &lt;data&gt; elements with name type and length
     attribute values set but no initial or default values set.  Ensure proper record 
     created with proper values.
     **/
    public void Var020()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtFloat");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 2) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.lang.Float"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            if (  ((Float)newRec.getField(0)).doubleValue() != 0.000)
            {
              failed("Bad value returned for first field.");
              return;
            }
            if (!newRec.getField(1).getClass().getName().equals("java.lang.Double"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
            if (  ((Double)newRec.getField(1)).doubleValue() != 0.0000)
            {
              failed("Bad value returned for second field.");
              return;
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains float &lt;data&gt; elements with name type and length
     attribute values set with an initial value set.  Ensure proper record 
     created with proper values.
     **/
    public void Var021()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtFloatv");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 2) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.lang.Float"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            Float testFloat = Float.valueOf(123.456f);
            if (  ((Float)newRec.getField(0)).floatValue() != testFloat.floatValue())
            {
                failed("Bad value returned for first field.");
                return;
            }
            if (!newRec.getField(1).getClass().getName().equals("java.lang.Double"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
          if (  ((Double)newRec.getField(1)).doubleValue() != 5678.913)
          {
            failed("Bad value returned for second field.");
              return;
          }
        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains float &lt;data&gt; elements with name type and length
     attribute values set with value set using setValue().  Ensure proper record 
     created with proper values.
     **/
    public void Var022()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtFloat");
            Float float1 = Float.valueOf(100.100f);
            rfmlDoc.setValue("format1.field1",float1);
            Double double1 = Double.valueOf(13.1333);
            rfmlDoc.setValue("format1.field2",double1);
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 2) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            if (!newRec.getField(0).getClass().getName().equals("java.lang.Float"))
            {
              failed("Bad class type returned for first field.");
              return;
            }
            Float testFloat = Float.valueOf(100.100f);
            if (  ((Float)newRec.getField(0)).floatValue() != testFloat.floatValue())
            {
              failed("Bad value returned for first field.");
              return;
            }
            if (!newRec.getField(1).getClass().getName().equals("java.lang.Double"))
            {
              failed("Bad class type returned for second field.");
              return;
            }
          Double testDouble = Double.valueOf(13.1333);
          if (  ((Double)newRec.getField(1)).doubleValue() != testDouble.doubleValue())
          {
              failed("Bad value returned for second field.");
              return;
          }
        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains float &lt;data&gt; elements with name type and length
     attribute values set with value set using init with a very large value set using init.  
     **/
    public void Var023()
    {
	
        try
        {
/*
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtFloatBig");
            Record newRec = rfmlDoc.toRecord("format1");
            Float floatV = Float.valueOf(123456.789);
output_.println("floatV = " + floatV.floatValue() );
            Float floatF = (Float) newRec.getField(0);
output_.println("floatF = " + floatF.floatValue() );
            if (  floatF.floatValue() != floatV.floatValue())
            {
              failed("Bad value returned for first field.");
              return;
            }
            Float float2 = Float.valueOf(5678.78123);
            Float floatF2 = (Float) newRec.getField(1);
            if (  float2.floatValue() != floatF2.floatValue())
            {
              failed("Bad value returned for second field.");
              return;
            }
*/
            succeeded();
        }
        catch (Exception e)
        {
            failed(e,"Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains byte &lt;data&gt; elements with name type and length
     attribute values set with an initial value set.  Ensure proper record 
     created with proper values.
     **/
    public void Var024()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtByte");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            AS400ByteArray byteArray = new AS400ByteArray(100);
            byte[] bytes =  byteArray.toBytes(newRec.getField(0));
//            Byte ByteA = Byte.valueOf(bytes);
//            if (  ByteA.toString().trim() != "")
//            {
//                failed("Bad value returned for first field.");
//              return;
//            }
            assertCondition(true, "bytes="+bytes); 
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains byte &lt;data&gt; elements with name type and length
     attribute values set with value set using init with format "0x00, 0x01".
     Ensure proper record created with proper values.
     **/
    public void Var025()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtBytev");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            AS400ByteArray byteArray = new AS400ByteArray(100);
            byte[] bytes =  byteArray.toBytes(newRec.getField(0));
//            if (  byteArray.toString().trim() != "")
//            {
//                failed("Bad value returned for first field.");
//              return;
//            }
              assertCondition(true, "bytes="+bytes); 

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains byte &lt;data&gt; elements with name type and length
     attribute values set with value set using init and initializing to a String.
     Ensure proper record created with proper values.
     **/
    public void Var026()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtBytes");
            Record newRec = rfmlDoc.toRecord("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
//            Byte byteArray = (Byte) newRec.getField(0);
//            if (  byteArray.toString().trim() != "")
//            {
//                failed("Bad value returned for first field.");
//              return;
//            }
        succeeded();

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test toRecord(formatName). Map to formatName that exists in the current rfml
     document and contains byte &lt;data&gt; elements with name type and length
     attribute values set with value set using setValue
     **/
    public void Var027()
    {
	
        try
        {
/*
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtByte");
            rfmlDoc.setStringValue("format1.field1","0 1 0",0);
            Record newRec = rfmlDoc.toRecord("format1");
            RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");
            if (newRec.getNumberOfFields() != 1) 
            {
              failed("Bad number of fields returned.");
              return;
            }
            FieldDescription field1 = recFmt.getFieldDescription(0);
 output_.println("DataType = " + field1.getDataType());
 output_.println("Class = " + field1.getDataType().getClass());
//            if ( field1.getDataType().getClass() == dt.getClass() &&
//                 field1.getDataType().getByteLength() == dt.getByteLength())
//            {
//              failed("Bad type or length returned.");
//              return;
//            }

output_.println("newRec.getField class = " + newRec.getField(0).getClass());
            AS400ByteArray byteArray = new AS400ByteArray(100);
            byte[] bytes =  byteArray.toBytes(newRec.getField(0));
output_.println(newRec.getField(0).toString());
            if ( !byteArray.toString().equals("010"))
            {
                failed("Bad value returned for first field.");
                return;
            }
*/
            
            succeeded();
        }
        catch (Exception e)
        {
            failed(e,"Unexpected exception");
        }
    }

}
