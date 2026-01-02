///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMGetSetValueTestcase.java
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
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.data.RecordFormatDocument;

import test.Testcase;

/**
 The RMGetSetValueTestcase class tests the following methods of the RecordFormatDocument class:
 <li>getValue(name)
 <li>getValue(name,index)
 <li>setValue(name, value)
 <li>setValue(name, index, int)
 **/
public class RMGetSetValueTestcase extends Testcase
{
    /**
     Constructor.
     **/
    public RMGetSetValueTestcase(AS400 systemObject, 
                             Hashtable<String,Vector<String>> namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMGetSetValueTestcase", namesAndVars, runMode, fileOutputStream);
    }

    /**
     Test getValue(name).  Pass in an empty string for name. Ensure error generated.
     **/
    public void Var001()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            Object valOut1 = rfmlDoc.getValue("");
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "<data> element named '' not found in document";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test getValue(name).  Pass in null for name. Should get null pointer exception.
     **/
    public void Var002()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            Object valOut1 = rfmlDoc.getValue((String)null);
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test getValue(name) with a name that maps to a &lt;data&gt; element in the rfml document, that doesn't have an init= value and doesn't have a value set.
    Verify that null is returned.
    **/
    public void Var003()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1NI");
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            assertCondition(valOut1 == null);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name).  Pass a name that does not map to a &lt;data&gt; tag.
     Ensure error is generated.
     **/
    public void Var004()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            Object valOut1 = rfmlDoc.getValue("format1");
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "Element named 'format1' in document is not a <data> element.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name).  Pass a name that maps to a &lt;data&gt; element
     that can not be converted to an Object, i.e. type=Struct.
     **/
    public void Var005()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtDataAndStruct");
            rfmlDoc.setValue("format1.field1.sdata1", "Hello");
            rfmlDoc.setValue("format1.field1.sdata2", new BigDecimal("0.0"));
            Object valOut1 = rfmlDoc.getValue("format1.field1");
            assertCondition(valOut1 == null);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in negative number for index. Ensure
     error generated.
     **/
    public void Var006()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1Count2");
            Object valOut1 = rfmlDoc.getValue("format1.field1", new int[] {-1});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {-1}. Processing <data> element 'format1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name,index). Pass in an invalid number for index. Ensure
     error generated.
     **/
    public void Var007()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1Count2");
            Object valOut1 = rfmlDoc.getValue("format1.field1", new int[] {2});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {2}. Processing <data> element 'format1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int and whose value has not been set. Ensure null returned.
     **/
    public void Var008()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachType");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2");
            assertCondition(valOut1 == null);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 and that contains a valid init
     value that is a number. Ensure proper Short returned.
     **/
    public void Var009()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 12);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// TBD - Find out what Sue means by "a valid init value that is a String".  Is she referring to the bidi stuff?  If so, this scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 and precision<=15 that contains a valid
///     initial number value as a String. Ensure proper Short returned.
///     **/
///    public void Var010()
///    {
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
///            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
///            ///output_.println("valOut1 == " + valOut1.shortValue());
///            assertCondition(valOut1.shortValue() == 12);
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision<=15 that contains a valid number
     that was set using setValue(). Ensure proper Short returned.
     **/
    public void Var010()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field1", Short.valueOf((short)23));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: This scenario is irrelevant.  setStringValue() only applies to type="char".
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 and precision <=15 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Short returned.
///     **/
///
///    public void Var012()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Short returned.
     **/
    public void Var011()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setIntValue("format1.field1", 27);
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 27);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision <=15  that contains a valid 
     number value and pass in 0 for the index. Ensure the value of the data element
     is returned as a Short.
     **/
    public void Var012()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field1", Short.valueOf((short)23));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1", new int[] {0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to an array with count=5 
     of elements. Pass in 0 for the index. Ensure the value of the data element is
     returned as a Short.
     **/
    public void Var013()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2", new int[] {0});
            rfmlDoc.setValue("format1.field2", new int[] {0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field2", new int[] {0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that maps to an array with
     count=5 of elements. Pass in 1 for the index.
     Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var014()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2", new int[] {1});
            rfmlDoc.setValue("format1.field2", new int[] {0}, Short.valueOf((short)20));
            rfmlDoc.setValue("format1.field2", new int[] {1}, Short.valueOf((short)21));
            rfmlDoc.setValue("format1.field2", new int[] {2}, Short.valueOf((short)22));
            rfmlDoc.setValue("format1.field2", new int[] {3}, Short.valueOf((short)23));
            rfmlDoc.setValue("format1.field2", new int[] {4}, Short.valueOf((short)24));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field2", new int[] {1});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 21);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that maps to an array with 
     count=5 of elements. Pass in 4 for the index. Ensure the value of the proper
     data element is returned as a Short.
     **/
    public void Var015()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2", new int[] {4});
            rfmlDoc.setValue("format1.field2", new int[] {0}, Short.valueOf((short)20));
            rfmlDoc.setValue("format1.field2", new int[] {1}, Short.valueOf((short)21));
            rfmlDoc.setValue("format1.field2", new int[] {2}, Short.valueOf((short)22));
            rfmlDoc.setValue("format1.field2", new int[] {3}, Short.valueOf((short)23));
            rfmlDoc.setValue("format1.field2", new int[] {4}, Short.valueOf((short)24));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field2", new int[] {4});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 24);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, and precision=15 that maps to an array with 
     count=5 of elements. Pass in 5 for the index.
     Ensure an ArrayIndexOutOfBounds error is returned.
     **/
    public void Var016()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2", new int[] {5});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 4). The index in error is index number 0 of the indices specified, {5}. Processing <data> element 'format1.field2'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision = 15 that maps to a two dimensional
     array with dimensions [2,3]. Pass in [0,0] for
     the index. Ensure the value of the first data element is returned as a Short.
     **/
    public void Var017()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.print("valOut1: " );
            ///if (valOut1 == null) output_.println("null");
            ///else output_.println(valOut1.shortValue());
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional array
     with dimensions [2,3].  Pass in [0,1] for 
     the index. Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var018()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,1});
            ///output_.print("valOut1: " );
            ///if (valOut1 == null) output_.println("null");
            ///else output_.println(valOut1.shortValue());
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,1}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,1});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,3].  Pass in [1,0] for 
     the index. Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var019()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,0});
            ///output_.print("valOut1: " );
            ///if (valOut1 == null) output_.println("null");
            ///else output_.println(valOut1.shortValue());
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,0});
            Short valOut00 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut00.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional array 
     with dimensions [2,3].  Pass in [1,2] for 
     the index. Ensure the value of the proper data element is returned as a Short
    **/
    public void Var020()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,2});
            ///output_.print("valOut1: " );
            ///if (valOut1 == null) output_.println("null");
            ///else output_.println(valOut1.shortValue());
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,2}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,2});
            Short valOut00 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut00.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional
     array with dimensions [2,3].  Pass in [3,1] for 
     the index. Ensure an ArrayIndexOutOfBounds error is generated.
     **/
    public void Var021()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {3,1});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {3, 1}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,3].  Pass in [1,3] for 
     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
     **/
    public void Var022()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,3});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 2). The index in error is index number 1 of the indices specified, {1, 3}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,3].  Pass in 0 for 
     the index. Ensure error generated.
     **/
    public void Var023()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "The number of indices required is 2. The number of indices specified is 1. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: The following scenarios are invalid - We don't seem to be able to specify an element in a multidimensional array using a single index.

///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional
///     array with dimensions [2,3].  Pass in 2 for 
///     the index. Ensure the value of the proper data element is returned as a Short.
///     **/
///    public void Var026()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional
///     array with dimensions [2,3].  Pass in 5 for 
///     the index. Ensure the value of the proper data element is returned as a Short.
///     **/
///    public void Var027()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional 
///     array with dimensions [2,3].  Pass in 6 for 
///     the index. Ensure ArrayIndexOutOfBounds error generated.
///     **/
///    public void Var028()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision= 15 that maps to a 3 dimensional
     array with dimensions [2,2,2]. Pass in [0,0,0] for
     the index. Ensure the value of the first data element is returned as a Short.
     **/
    public void Var024()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,0,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            Short valOut001 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,1});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut001.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int,length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,2,2].  Pass in [0,1,0] for 
     the index. Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var025()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,1,0});
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,1,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,1,0});
            Short valOut000 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut000.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,2,2].  Pass in [0,0,1] for 
     the index. Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var026()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,1});
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,0,1}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,1});
            Short valOut000 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut000.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,2,2].  Pass in [1,1,1] for 
     the index. Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var027()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,1,1});
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,1,1}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,1,1});
            Short valOut000 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut000.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,2,2].  Pass in [1,0,1] for 
     the index. Ensure the value of the proper data element is returned as a Short.
     **/
    public void Var028()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,0,1});
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,0,1}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,0,1});
            Short valOut000 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 0 &&
                   valOut2.shortValue() == 23 &&
                   valOut000.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional
     array with dimensions [2,2,2].  Pass in [2,0,1] for 
     the index. Ensure an ArrayIndexOutOfBounds error is generated.
     **/
    public void Var029()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {2,0,1});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {2, 0, 1}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision = 15 that maps to a two dimensional
     array with dimensions [2,2,2].  Pass in [1,2,1] for 
     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
     **/
    public void Var030()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,2,1});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 1 of the indices specified, {1, 2, 1}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2, precision=15 that maps to a two dimensional 
     array with dimensions [2,2,2].  Pass in [1,1,2] for 
     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
     **/
    public void Var031()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,1,2});
            failed("Did not throw exception."+valOut1);
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 2 of the indices specified, {1, 1, 2}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: The next few variations are invalid, since a single index can't be used to specify an element in a multidimensional array.

///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional
///     array with dimensions [2,2,2].  Pass in 0 for 
///     the index. Ensure the value of the proper data element is returned as a Short.
///     **/
///    public void Var037()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional
///     array with dimensions [2,2,2].  Pass in 2 for 
///     the index. Ensure the value of the proper data element is returned as a Short.
///     **/
///    public void Var038()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional 
///     array with dimensions [2,2,2].  Pass in 5 for 
///     the index. Ensure the value of the proper data element is returned as a Short.
///     **/
///
///    public void Var039()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test getValue(name,index). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional 
///     array with dimensions [2,2,2].  Pass in 6 for 
///     the index. Ensure ArrayIndexOutOfBounds error generated.
///     **/
///    public void Var040()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 and that contains a valid init
     value that is a number. Ensure proper Integer returned.
     **/
    public void Var032()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 123);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 and precision>=16 that contains a valid
///     initial number value as a String. Ensure proper Integer returned.
///     **/
///    public void Var042()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision>=16 that contains a valid number
     that was set using setValue(). Ensure proper Integer returned.
     **/
    public void Var033()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field4", Integer.valueOf(456));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 456);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: String values are relevant only for type="char".
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 and precision >=16 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Integer returned.
///     **/
///
///    public void Var044()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Integer returned.
     **/
    public void Var034()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field4", 567);
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 and that contains a valid init
     value that is a number. Ensure proper Integer returned.
     **/
    public void Var035()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            ///rfmlDoc.setIntValue("format1.field5", 567);
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 123);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: This scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4 and precision<=31 that contains a valid
///     initial number value as a String. Ensure proper Integer returned.
///     **/
///    public void Var047()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision<=31 that contains a valid number
     that was set using setValue(). Ensure proper Integer returned.
     **/
    public void Var036()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field5", Integer.valueOf(567));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: This scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4 and precision <=31 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Integer returned.
///     **/
///    public void Var049()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Integer returned.
     **/
    public void Var037()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field5", 567);
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 and that contains a valid init
     value that is a number. Ensure proper Long returned.
     **/
    public void Var038()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            ///rfmlDoc.setIntValue("format1.field6", 567);
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.longValue() == 700000);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: This scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4 and precision>=32 that contains a valid
///     initial number value as a String. Ensure proper Long returned.
///     **/
///    public void Var052()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision>=32 that contains a valid number
     that was set using setValue(). Ensure proper Long returned.
     **/
    public void Var039()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field6", Long.valueOf(56789));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.longValue() == 56789);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: This scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4 and precision >=32 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Long returned.
///     **/
///    public void Var054()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Long returned.
     **/
    public void Var040()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field6", 56789);
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.longValue() == 56789);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 and that contains a valid init
     value that is a number. Ensure proper Long returned.
     **/
    public void Var041()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            ///rfmlDoc.setIntValue("format1.field7", 567);
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.longValue() == 777777777);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: This scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=8 and precision<=63 that contains a valid
///     initial number value as a String. Ensure proper Long returned.
///     **/
///    public void Var057()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision<=63 that contains a valid number
     that was set using setValue(). Ensure proper Long returned.
     **/
    public void Var042()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field7", Long.valueOf(5678901));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.longValue() == 5678901);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: This scenario is invalid.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=8 and precision <=63 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Long returned.
///     **/
///    public void Var059()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Long returned.
     **/

    public void Var043()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setIntValue("format1.field7", 5678901);
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.longValue() == 5678901);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: The following scenario is invalid.  Setting the precision to 68 causes a PcmlSpecificationException during parsing of the document.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=8 and precision=68 that contains a valid initial
///     number value as an int that was set using setIntValue(). What happens when 
///     precision set to > 63?  
///     **/
///    public void Var061()
///    {
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char and that contains a valid init
     value that is a number. Ensure proper String returned.
     **/
    public void Var044()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt2v");
            ///rfmlDoc.setValue("format1.field1", "5678901");
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.equals("1234"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that contains a valid
     initial value as a String. Ensure proper String returned.
     **/
    public void Var045()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            ///rfmlDoc.setValue("format1.field1", "5678901");
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.equals("Brian and Boo"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that contains a valid value
     that was set using setValue(). Ensure proper String returned.
     **/
    public void Var046()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field1", "Boogy Woogy");
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.equals("Boogy Woogy"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that contains a valid 
     initial value as a String that was set using setStringValue(). Ensure
     proper String returned.
     **/
    public void Var047()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setStringValue("format1.field1", "Boogy Woogy", BidiStringType.ST5);
            // Note: We must specify the same BidiStringType as is specified in the RFML, otherwise the getValue() returns the init value.
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            assertCondition(valOut1.equals("Boogy Woogy"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     String returned.
     **/
    public void Var048()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field1", 34567);
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.equals("34567"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte and that contains a valid init
     value. Ensure proper byte[] returned.
     **/
    public void Var049()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.byte2");
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field1");
            ///printByteArray("valOut1:", valOut1);
            byte[] expected = new byte[] { (byte)0, (byte)0, (byte)0, (byte)0 };
            assertCondition(areEqual(valOut1, expected));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte that contains a valid value
     that was set using setValue(). Ensure proper byte[] returned.
     **/
    public void Var050()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.byte2");
            ///byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field1");
            ///printByteArray("valOut1:", valOut1);
            byte[] valsIn = new byte[] { (byte)0xF3, (byte)0xF4, (byte)0xF5, (byte)0xF6 /*, (byte)0xF7, (byte)0xF8, (byte)0xF9, (byte)0xFA */ };
            ///rfmlDoc.setValues("format1", valsIn);
            rfmlDoc.setValue("format1.field1", valsIn);
            byte[] valOut2 = (byte[])rfmlDoc.getValue("format1.field1");
///            output_.println("Expected:");
///            printByteArray(valsIn);
///            output_.println("Got:");
///            printByteArray(valOut2);
            ///byte[] valsInsubstr = new byte[4]; // Only examine what we put in field1.
            ///System.arraycopy(valsIn, 0, valsInsubstr, 0, 4);
            ///assertCondition(areEqual(valsInsubstr, valOut2));
            assertCondition(areEqual(valsIn, valOut2));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte that contains a valid 
///     initial value as a String that was set using setStringValue(). Ensure
///     proper byte[] returned.
///     **/
///
///    public void Var069()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte that contains a valid initial
///     number value as an int that was set using setIntValue(). Ensure proper
///     byte[] returned.
///     **/
///
///    public void Var070()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=packed and that contains a valid init
///     value that is a number. Ensure proper BigDecimal returned.
///     **/
///    public void Var071()
///    {
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid
     initial value as a String. Ensure proper BigDecimal returned.
     **/
    public void Var051()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.toString().equals("100.45000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid value
     that was set using setValue(). Ensure proper BigDecimal returned.
     **/

    public void Var052()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field2", new BigDecimal("999.88"));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("999.88000"));  // precision is 5
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=packed that contains a valid 
///     initial value as a String that was set using setStringValue(). Ensure
///     proper BigDecimal returned.
///     **/
///    public void Var074()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     BigDecimal returned.
     **/
    public void Var053()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field2", 98765);
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("98765.00000"));  // precision is 5
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=zoned and that contains a valid init
///     value that is a number. Ensure proper BigDecimal returned.
///     **/
///    public void Var076()
///    {
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned that contains a valid
     initial value as a String. Ensure proper BigDecimal returned.
     **/
    public void Var054()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.toString().equals("1000.7600000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned that contains a valid value
     that was set using setValue(). Ensure proper BigDecimal returned.
     **/
    public void Var055()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field9", new BigDecimal("5432.10"));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.toString().equals("5432.1000000")); // precision==7
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=zoned that contains a valid 
///     initial value as a String that was set using setStringValue(). Ensure
///     proper BigDecimal returned.
///     **/
///
///    public void Var079()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     BigDecimal returned.
     **/
    public void Var056()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field9", 543210);
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.toString().equals("543210.0000000")); // precision==7
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=4  and that contains a valid init
///     value that is a number. Ensure proper Float returned.
///     **/
///
///    public void Var081()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid
     initial number value as a String. Ensure proper Float returned.
     **/
    public void Var057()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("87.98"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid number
     that was set using setValue(). Ensure proper Float returned.
     **/
    public void Var058()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field7", Float.valueOf("-5432.02"));
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("-5432.02"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=4 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Float returned.
///     **/
///
///    public void Var084()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Float returned.
     **/

    public void Var059()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field7", -5432);
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("-5432.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=8 and that contains a valid init
///     value that is a number. Ensure proper Double returned.
///     **/
///
///    public void Var086()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8  that contains a valid
     initial number value as a String. Ensure proper Double returned.
     **/
    public void Var060()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("9.1"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid number
     that was set using setValue(). Ensure proper Double returned.
     **/
    public void Var061()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field8", Double.valueOf("10.4"));
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("10.4"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=8 that contains a valid 
///     initial number value as a String that was set using setStringValue(). Ensure
///     proper Double returned.
///     **/
///    public void Var089()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test getValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid initial
     number value as an int that was set using setIntValue(). Ensure proper
     Double returned.
     **/
    public void Var062()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field8", 2579);
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("2579.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



// Test setValue methods
 
    /**
     Test setValue(name, value).  Pass in an empty string for name. Ensure error generated.
     **/
    public void Var063()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("", Integer.valueOf(2579));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "<data> element named '' not found in document.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test setValue(name, value).  Pass in null for name. Should get null pointer exception.
     **/
    public void Var064()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            rfmlDoc.setValue((String)null, Integer.valueOf(5));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

/// Note: Invalid scenario.  'type' is a required attribute for <data> elements.
///    /**
///     Test setValue(name, value) with a name that maps to an empty &lt;data&gt;
///     tag in the rfml document with no type attribute set. Should this set the value to 0
///     or cause an error?
///    **/
///    public void Var093()
///    {
///    }

    /**
     Test setValue(name, value).  Pass a name that does not map to a          
     &lt;data&gt; tag. Ensure error is generated.
     **/
    public void Var065()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.bogusField", Integer.valueOf(2579));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "<data> element named 'format1.bogusField' not found in document.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, value).  Pass a name that maps to a &lt;data&gt; element,
     that can not be set to an int, e.g. a Struct type. Ensure an error is generated.
     **/
    public void Var066()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structChar1");
            rfmlDoc.setValue("format1.field1", Integer.valueOf(2579));
            ///rfmlDoc.setValue("format1", Integer.valueOf(2579));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "Cannot set or get the value of a <data> with type='struct'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: PCML does not appear to check for overflow.
///    /**
///     Test setValue(name, value).  Pass in a Long value that causes an overflow 
///     error to occur for a &lt;data&gt; element whose type=int, length=2 and 
///     precision=15. Does the code check for overflow or not?
///     **/
///    public void Var096()
///    {
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
///            rfmlDoc.setValue("format1.field3", Long.valueOf(987654321987654321L));
///            Short valOut1 = (Short)rfmlDoc.getValue("format1.field3");
///            output_.println("valOut1 == " + valOut1.toString());
///        }
///        catch (Exception e)
///        {
///          failed(e);
///        }
///    }


    /**
     Test setValue(name, index, value). Pass in negative number for index. Ensure
     error generated.
     **/
    public void Var067()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1Count2");
            rfmlDoc.setValue("format1.field1", new int[] {-1}, "Z");
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {-1}.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, index, value). Pass in an invalid number for index. Ensure
     error generated.
     **/
    public void Var068()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.char1Count2");
            rfmlDoc.setValue("format1.field1", new int[] {2}, "Z");
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {2}. Processing <data> element 'format1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 an whose value has not been
     set. Make the type of value be a Short. Ensure the value is set properly.
     **/
    public void Var069()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field1", Short.valueOf((short)25));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == 25);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 and precision=15 that contains a valid init value
///     that is a number. Pass in a valid Integer value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var100()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Long value that does not cause an overflow to occur.
     Ensure the set worked correctly.
     **/
    public void Var070()
    {
	
        try
        {
            short shortVal = Short.MAX_VALUE;  // 32767
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field1", Long.valueOf(shortVal));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == shortVal);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that contains a valid number
     that was set using setValue(). Reset the value to the new int value using a BigDecimal that does not cause an overflow
     to occur. Ensure the set worked correctly.
     **/
    public void Var071()
    {
	
        try
        {
            short shortVal = Short.MAX_VALUE;  // 32767
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field1", Short.valueOf((short)-53));
            rfmlDoc.setValue("format1.field1", new BigDecimal(shortVal));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == shortVal);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Short using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var103()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that contains a valid initial 
     number value as a String that was set using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var072()
    {
	
        try
        {
            short shortVal = Short.MAX_VALUE;  // 32767
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setIntValue("format1.field1", -53);
            rfmlDoc.setValue("format1.field1", Float.valueOf(shortVal));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == shortVal);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow and ensure the new value has been set correctly.
     **/
    public void Var073()
    {
	
        try
        {
            short shortVal = Short.MIN_VALUE;  // -32768
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZ");
            rfmlDoc.setIntValue("format1.field1", -53);
            rfmlDoc.setValue("format1.field1", Double.valueOf(shortVal));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == shortVal);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=15 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to a Short (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var074()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtPZ");
            rfmlDoc.setValue("format1.field1", new Date());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Date'. String or Number expected. Processing <data> element 'format1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 and pass in 0 for the index.
     Ensure the value of the data element can be set to the Short value.
     **/
    public void Var075()
    {
	
        try
        {
            short shortVal = Short.MAX_VALUE;  // 32767
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field1", new int[] {0}, Short.valueOf(shortVal));
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.shortValue() == shortVal);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precsion=15 that maps to an array with count=5 of elements. Pass in 0 for the index.
     Ensure the value of the first data element is set correctly.
     **/
    public void Var076()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            rfmlDoc.setValue("format1.field2", new int[] {0}, Short.valueOf((short)23));
            Short valOut0 = (Short)rfmlDoc.getValue("format1.field2", new int[] {0});
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2", new int[] {1});
            assertCondition(valOut0.shortValue() == 23 &&
                   valOut1.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to an array with count=5 of elements. Pass in 1 for the index.
     Ensure the value of the proper data element is set correctly.
     **/
    public void Var077()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            rfmlDoc.setValue("format1.field2", new int[] {1}, Short.valueOf((short)23));
            Short valOut0 = (Short)rfmlDoc.getValue("format1.field2", new int[] {0});
            Short valOut1 = (Short)rfmlDoc.getValue("format1.field2", new int[] {1});
            assertCondition(valOut0.shortValue() == 0 &&
                   valOut1.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to an array with count=5 of elements. Pass in 2 for the index.
     Ensure the value of the proper data element is set correctly.    
     **/
    public void Var078()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            rfmlDoc.setValue("format1.field2", new int[] {2}, Short.valueOf((short)23));
            Short valOut4 = (Short)rfmlDoc.getValue("format1.field2", new int[] {4});
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field2", new int[] {2});
            assertCondition(valOut4.shortValue() == 0 &&
                   valOut2.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to an array with count=5 of elements. Pass in 5 for the index.
     Ensure an ArrayIndexOutOfBounds error is returned.
     **/
    public void Var079()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            rfmlDoc.setValue("format1.field2", new int[] {5}, Short.valueOf((short)23));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 4). The index in error is index number 0 of the indices specified, {5}. Processing <data> element 'format1.field2'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to a two dimensional array with dimensions [2,3]. Pass in [0,0] for
     the index. Ensure the value of the first data element is set correctly.
     **/
    public void Var080()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,2});
            Short valOut00 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut2.shortValue() == 0 &&
                   valOut00.shortValue() == 23);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int,length=2, precision=15 that maps to a two dimensional array with dimensions [2,3].  Pass in [0,1] for 
     the index. Ensure the value of the proper data element is set correctly.
     **/
    public void Var081()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,1}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,1});
            Short valOut00 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut2.shortValue() == 23 &&
                   valOut00.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to a two dimensional array with dimensions [2,3].  Pass in [1,0] for 
     the index. Ensure the value of the proper data element is set correctly.   
     **/
    public void Var082()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,0});
            Short valOut00 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut2.shortValue() == 23 &&
                   valOut00.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to a two dimensional array with dimensions [2,3].  Pass in [1,2] for 
     the index. Ensure the value of the proper data element is set correctly.
     **/

    public void Var083()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,2}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {1,2});
            Short valOut00 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut2.shortValue() == 23 &&
                   valOut00.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to a two dimensional array with dimensions [2,3].  Pass in [3,1] for 
     the index. Ensure an ArrayIndexOutOfBounds error is generated.
     **/
    public void Var084()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {3,1}, Short.valueOf((short)23));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 1). The index in error is index number 0 of the indices specified, {3, 1}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int,length=2, precision=15 that maps to a two dimensional array with dimensions [2,3].  Pass in [1,3] for 
     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
     **/
    public void Var085()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2DimInt");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {1,3}, Short.valueOf((short)23));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "An index specified is out of bounds (0 - 2). The index in error is index number 1 of the indices specified, {1, 3}. Processing <data> element 'format1.field1.field1.field1'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: Invalid scenario.  Must specify both indices for a 2-dimensional array.
///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional array 
///     with dimensions [2,3].  Pass in 0 for 
///     the index. Ensure the value of the proper data element is set correctly.    
///     **/
///
///    public void Var118()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

/// Note: Invalid scenario.  Must specify both indices for a 2-dimensional array.
///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional array
///     with dimensions [2,3].  Pass in 2 for 
///     the index. Ensure the value of the proper data element is set correctly.
///     **/
///
///    public void Var119()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

/// Note: Invalid scenario.  Must specify both indices for a 2-dimensional array.
///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15  that maps to a two dimensional array
///     with dimensions [2,3].  Pass in 5 for 
///     the index. Ensure the value of the proper data element is set correctly.    
///     **/
///
///    public void Var120()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


/// Note: Invalid scenario.  Must specify both indices for a 2-dimensional array.
///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a two dimensional array 
///     with dimensions [2,3].  Pass in 6 for 
///     the index. Ensure ArrayIndexOutOfBounds error generated.
///     **/
///
///    public void Var121()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to a 3 dimensional array with dimensions [2,2,2]. Pass in [0,0,0] for
     the index. Ensure the value of the first data element is set correctly.
     **/
    public void Var086()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,0,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            Short valOut001 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,1});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut2.shortValue() == 23 &&
                   valOut001.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt; element whose type=int, length=2, precision=15 that maps to a three dimensional array with dimensions [2,2,2].  Pass in [0,1,0] for 
     the index. Ensure the value of the proper data element is set correctly. 
     **/
    public void Var087()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray3Dim");
            rfmlDoc.setValue("format1.field1.field1.field1", new int[] {0,1,0}, Short.valueOf((short)23));
            Short valOut2 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,1,0});
            Short valOut000 = (Short)rfmlDoc.getValue("format1.field1.field1.field1", new int[] {0,0,0});
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut2.shortValue() == 23 &&
                   valOut000.shortValue() == 0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: The following "three dimensional" variations are probably overkill.

///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a three dimensional 
///     array with dimensions [2,2,2].  Pass in [0,0,1] for 
///     the index. Ensure the value of the proper data element is set correctly.    
///     **/
///
///    public void Var124()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=15 that maps to a three dimensional 
///     array with dimensions [2,2,2].  Pass in [1,1,1] for 
///     the index. Ensure the value of the proper data element is set correctly.
///     **/
///
///    public void Var125()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///
///    /**
///     Test setValue(name, index, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 precision=15 that maps to a three dimensional array
///     with dimensions [2,2,2].  Pass in [1,0,1] for 
///     the index. Ensure the value of the proper data element is set correctly.
///     **/
///
///    public void Var126()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, and precision=15 that maps to a three dimensional
///     array with dimensions [2,2,2].  Pass in [2,0,1] for 
///     the index. Ensure an ArrayIndexOutOfBounds error is generated.
///     **/
///
///    public void Var127()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a three dimensional array with dimensions [2,2,2].  Pass in [1,2,1] for 
///     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
///     **/
///
///    public void Var128()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a three dimensional array with dimensions [2,2,2].  Pass in [1,1,2] for 
///     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
///     **/
///
///    public void Var129()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a three dimensional array with dimensions [2,2,2].  Pass in 0 for 
///     the index. Ensure the value of the proper data element is set correctly.
///     **/
///
///    public void Var130()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a three dimensional array with dimensions [2,2,2].  Pass in 2 for 
///     the index. Ensure the value of the proper data element is set correctly.
///     **/
///
///    public void Var131()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a three dimensional array with dimensions [2,2,2].  Pass in 5 for 
///     the index. Ensure the value of the proper data element is set correctly.
///     **/
///
///    public void Var132()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setIntValue(name, index, int). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a three dimensional array with dimensions [2,2,2].  Pass in 6 for 
///     the index. Ensure ArrayIndexOutOfBounds error generated.
///     **/
///
///    public void Var133()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 an whose value has not been
     set. Make the type of value be a Integer. Ensure the value is set properly.
     **/
    public void Var088()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field2", Integer.valueOf(56789));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 56789);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2 and precision=16 that contains a valid init value
///     that is a number. Pass in a valid Long value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var135()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly..
     **/
    public void Var089()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field4", Integer.valueOf(56789));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 56789);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 that contains a valid number
     that was set using setValue(). Reset the value to the new int value using a BigDecimal that does not cause an overflow to occur. Ensure the set worked correctly.
     **/
    public void Var090()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field4", Integer.valueOf(56789));
            rfmlDoc.setValue("format1.field4", new BigDecimal(98765));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 98765);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=2, precision=16 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Integer using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var138()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 that contains a valid initial 
     number value as a String that was set using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var091()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field4", 56789);
            rfmlDoc.setValue("format1.field4", Float.valueOf(98765));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 98765);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow and ensure the new value has been set correctly.
     **/
    public void Var092()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field4", Double.valueOf(98765));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field4");
            ///output_.println("valOut1 == " + valOut1.shortValue());
            assertCondition(valOut1.intValue() == 98765);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=2 and precision=16 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to an Integer (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var093()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field4", new Date());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Date'. String or Number expected. Processing <data> element 'format1.field4'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 an whose value has not been
     set. Make the type of value be a Integer. Ensure the value is set properly.
     **/
    public void Var094()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field5", Integer.valueOf(567));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4 and precision=31 that contains a valid init value
///     that is a number. Pass in a valid Long value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var143()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly..
     **/
    public void Var095()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field5", Short.valueOf((short)567));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a BigDecimal that does not cause an overflow
     to occur. Ensure the set worked correctly.
     **/
    public void Var096()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field5", Short.valueOf((short)125));
            rfmlDoc.setValue("format1.field5", new BigDecimal(567));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4, precision=31 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Integer using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var146()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var097()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field5", 125);
            rfmlDoc.setValue("format1.field5", Float.valueOf(567));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var098()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field5", 125);
            rfmlDoc.setValue("format1.field5", Double.valueOf(567));
            Integer valOut1 = (Integer)rfmlDoc.getValue("format1.field5");
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=31 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to an Integer (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var099()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field5", new Date());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Date'. String or Number expected. Processing <data> element 'format1.field5'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 an whose value has not been
     set. Make the type of value be a Long. Ensure the value is set properly.
     **/

    public void Var100()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field6", Long.valueOf(567890));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            assertCondition(valOut1.intValue() == 567890);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4 and precision=32 that contains a valid init value
///     that is a number. Pass in a valid Long value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var151()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly..
     **/
    public void Var101()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field6", Short.valueOf((short)567));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            assertCondition(valOut1.intValue() == 567);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a BigDecimal that does not cause an overflow
     to occur. Ensure the set worked correctly.
     **/
    public void Var102()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field6", Integer.valueOf(567));
            rfmlDoc.setValue("format1.field6", new BigDecimal(123456789));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            assertCondition(valOut1.intValue() == 123456789);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=4, precision=32 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Long using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var154()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var103()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field6", 567);
            rfmlDoc.setValue("format1.field6", Float.valueOf(123456.789f));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            assertCondition(valOut1.intValue() == 123456);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var104()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field6", Double.valueOf(123456.789));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field6");
            assertCondition(valOut1.intValue() == 123456);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=4 and precision=32 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to an Long (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var105()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field6", Character.valueOf('Z'));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.lang.Character'. String or Number expected. Processing <data> element 'format1.field6'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 an whose value has not been
     set. Make the type of value be a Long. Ensure the value is set properly.
     **/
    public void Var106()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtintB");
            rfmlDoc.setValue("format1.field7", Long.valueOf(55667788));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            assertCondition(valOut1.longValue() == 55667788);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=8 and precision=63 that contains a valid init value
///     that is a number. Pass in a valid BigDecimal value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var159()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly.
     **/
    public void Var107()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field7", Short.valueOf((short)556));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            assertCondition(valOut1.longValue() == 556);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a BigDecimal that does not cause an overflow
     to occur. Ensure the set worked correctly.
     **/
    public void Var108()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field7", Short.valueOf((short)556));
            rfmlDoc.setValue("format1.field7", new BigDecimal(556.789));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            assertCondition(valOut1.longValue() == 556);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=int, length=8, precision=63 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Long using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var162()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var109()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setIntValue("format1.field7", 556);
            rfmlDoc.setValue("format1.field7", Float.valueOf(556.789f));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            assertCondition(valOut1.longValue() == 556);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var110()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field7", Double.valueOf(556.789));
            Long valOut1 = (Long)rfmlDoc.getValue("format1.field7");
            assertCondition(valOut1.longValue() == 556);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=int, length=8 and precision=63 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to a Long (say a Date object or something) and ensure an error
     is thrown.
     **/
    @SuppressWarnings("rawtypes")
    public void Var111()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtint");
            rfmlDoc.setValue("format1.field7", new java.util.Vector());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Vector'. String or Number expected. Processing <data> element 'format1.field7'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed and whose value has not been
     set. Make the type of value be a BigDecimal. Ensure the value is set properly.
     **/
    public void Var112()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
            rfmlDoc.setValue("format1.field2", new BigDecimal("5566.77"));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("5566.77000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=packed that contains a valid init value
///     that is a number. Pass in a valid Long value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var167()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly.
     **/
    public void Var113()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field2", Short.valueOf((short)(5566)));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("5566.00000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a Long that does not cause an overflow
     to occur. Ensure the set worked correctly.
     **/
    public void Var114()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field2", Short.valueOf((short)(5566)));
            rfmlDoc.setValue("format1.field2", Long.valueOf(12345));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("12345.00000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=packed that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new BigDecimal using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var170()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    @SuppressWarnings("deprecation")
    public void Var115()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field2", 5566);
            Float valIn = Float.valueOf(12345.68f);
            rfmlDoc.setValue("format1.field2", valIn);
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            BigDecimal valOut1Rounded = valOut1.setScale(2, BigDecimal.ROUND_HALF_UP);  // match the scale of the inputted Float.
            ///output_.println("valOut1Rounded.floatValue()=="+valOut1Rounded.floatValue());
            ///output_.println("valIn.floatValue()=="+valIn.floatValue());
            assertCondition(valOut1Rounded.floatValue() == valIn.floatValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    @SuppressWarnings("deprecation")
    public void Var116()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Double valIn = Double.valueOf(12345.68);
            rfmlDoc.setValue("format1.field2", valIn);
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field2");
            BigDecimal valOut1Rounded = valOut1.setScale(2, BigDecimal.ROUND_HALF_UP);  // match the scale of the inputted Double.
            ///output_.println("valOut1Rounded.doubleValue()=="+valOut1Rounded.doubleValue());
            ///output_.println("valIn.doubleValue()=="+valIn.doubleValue());
            assertCondition(valOut1Rounded.doubleValue() == valIn.doubleValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=packed that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to a BigDecimal (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var117()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field2", new Date());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Date'. String or Number expected. Processing <data> element 'format1.field2'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned an whose value has not been
     set. Make the type of value be a BigDecimal. Ensure the value is set properly.
     **/
    public void Var118()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
            rfmlDoc.setValue("format1.field7", new BigDecimal("5566.77"));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("5566.7700000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=zoned that contains a valid init value
///     that is a number. Pass in a valid Long value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var175()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned  that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly..
     **/
    public void Var119()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field9", Short.valueOf((short)(5566)));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("5566.0000000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a Long that does not cause an overflow
     to occur. Ensure the set worked correctly.
     **/
    public void Var120()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field9", Short.valueOf((short)(5566)));
            rfmlDoc.setValue("format1.field9", Long.valueOf(123456789012345L));
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.toString().equals("123456789012345.0000000"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=zoned that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new BigDecimal using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var178()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned  that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var121()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field9", 5566);
            Float valIn = Float.valueOf(-123456789012345.68f);
            rfmlDoc.setValue("format1.field9", valIn);
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            @SuppressWarnings("deprecation")
            BigDecimal valOut1Rounded = valOut1.setScale(2, BigDecimal.ROUND_HALF_UP);  // match the scale of the inputted Float.
            ///output_.println("valOut1Rounded.floatValue()=="+valOut1Rounded.floatValue());
            ///output_.println("valIn.floatValue()=="+valIn.floatValue());
            assertCondition(valOut1Rounded.floatValue() == valIn.floatValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    @SuppressWarnings("deprecation")
    public void Var122()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Double valIn = Double.valueOf(-123456789012345.68);
            rfmlDoc.setValue("format1.field9", valIn);
            BigDecimal valOut1 = (BigDecimal)rfmlDoc.getValue("format1.field9");
            BigDecimal valOut1Rounded = valOut1.setScale(2, BigDecimal.ROUND_HALF_UP);  // match the scale of the inputted Double.
            ///output_.println("valOut1Rounded.doubleValue()=="+valOut1Rounded.doubleValue());
            ///output_.println("valIn.doubleValue()=="+valIn.doubleValue());
            assertCondition(valOut1Rounded.doubleValue() == valIn.doubleValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=zoned that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to a BigDecimal (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var123()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field9", Character.valueOf('a'));
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.lang.Character'. String or Number expected. Processing <data> element 'format1.field9'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 an whose value has not been
     set. Make the type of value be a Float. Ensure the value is set properly.
     **/
    public void Var124()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
            Float valIn = Float.valueOf("5566.77");
            rfmlDoc.setValue("format1.field5", valIn);
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field5");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.floatValue() == valIn.floatValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=4 that contains a valid init value
///     that is a number. Pass in a valid BigDecimal value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///
///    public void Var183()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly..
     **/
    public void Var125()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field7", Short.valueOf((short)1234));
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.intValue() == 1234);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a String value. Ensure the set worked correctly.
     **/
    public void Var126()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field7", Short.valueOf((short)1234));
            rfmlDoc.setValue("format1.field7", "123.4509");
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            ///assertCondition(valOut1.floatValue() == 123.4509);
            assertCondition(valOut1.toString().equals("123.4509"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=4 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Integer using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///
///    public void Var186()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Long that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var127()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field7", 1234);
            rfmlDoc.setValue("format1.field7", Long.valueOf(1234509));
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.longValue() == 1234509L);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid initial 
     number value (init). Reset the value using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var128()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Double valIn = Double.valueOf("12345.6789");
            rfmlDoc.setValue("format1.field7", valIn);
            Float valOut1 = (Float)rfmlDoc.getValue("format1.field7");
            ///output_.println("valOut1 == " + valOut1.doubleValue());
            assertCondition(Math.round(valOut1.doubleValue()*1000) == Math.round(valIn.doubleValue()*1000));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=4 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to a Float (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var129()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field7", new Date());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Date'. String or Number expected. Processing <data> element 'format1.field7'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 an whose value has not been
     set. Make the type of value be a Double. Ensure the value is set properly.
     **/
    public void Var130()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
            Double valIn = Double.valueOf("5566.778899");
            rfmlDoc.setValue("format1.field6", valIn);
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field6");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.doubleValue() == valIn.doubleValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=8 that contains a valid init value
///     that is a number. Pass in a valid BigDecimal value that will not cause an 
///     overflow to occur. Ensure the new value is set correctly.
///     **/
///    public void Var191()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly..
     **/
    public void Var131()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Short valIn = Short.valueOf((short)556);
            rfmlDoc.setValue("format1.field8", valIn);
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.shortValue() == valIn.shortValue());
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid number
     that was set using setValue(). Reset the
     value to the new int value using a String value. Ensure the set worked correctly.
     **/
    public void Var132()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Short valIn = Short.valueOf((short)556);
            rfmlDoc.setValue("format1.field8", valIn);
            rfmlDoc.setValue("format1.field8", "22334455.66778899D");
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.doubleValue() == 22334455.66778899D);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// Note: Invalid scenario.  setStringValue() is only relevant to type=char.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=float, length=8 that contains a valid initial 
///     number value as a String that was set
///     using setStringValue(). Reset the value to a new Double using a String
///     as the value and passing in a number that does not cause an overflow error.
///     Ensure correct value set.
///     **/
///    public void Var194()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid initial 
     number value as a String that was set
     using setIntValue(). Reset the value using a Long that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var133()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setIntValue("format1.field8", (short)345);
            rfmlDoc.setValue("format1.field8", Long.valueOf(22334455));
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.longValue() == 22334455);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid initial 
     number value (init). Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var134()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Float valIn = Float.valueOf(-123456789012345.68f);
            rfmlDoc.setValue("format1.field8", valIn);
            Double valOut1 = (Double)rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1Rounded.floatValue()=="+valOut1Rounded.floatValue());
            ///output_.println("valIn.floatValue()=="+valIn.floatValue());
            assertCondition(Math.round(valOut1.doubleValue()*1000) == Math.round(valIn.doubleValue()*1000));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=float, length=8 that contains a valid initial 
     number value (init). Reset the value using a bad Object value that cannot be
     converted to a Double (say a Date object or something) and ensure an error
     is thrown.
     **/
    public void Var135()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field8", new Date());
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type 'java.util.Date'. String or Number expected. Processing <data> element 'format1.field8'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char, length=4 an whose value has not been
     set. Make the type of value be a String. Ensure the value is set properly.
     **/
    public void Var136()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6a");
            rfmlDoc.setValue("format1.field9", "well");
            String valOut1 = (String)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.equals("well"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=char, length=4 that contains a valid init value
///     that is a number. Pass in a String with length = 8. Will this cause an 
///     error or will value be truncated?
///     **/
///    public void Var199()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char length=4 that contains a valid initial 
     number value as a String. 
     Set the value to a new valid Short value and ensure the set worked correctly.
     **/
    public void Var137()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field11", Short.valueOf((short)2345));
            String valOut1 = (String)rfmlDoc.getValue("format1.field11");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.equals("2345"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char, length=4 that contains a valid number
     that was set using setValue(). Set the value to the empty string.
     **/
    public void Var138()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field11", Short.valueOf((short)2345));
            rfmlDoc.setValue("format1.field11", "");
            String valOut1 = (String)rfmlDoc.getValue("format1.field11");
            ///output_.println("valOut1 == " + valOut1.toString());
            assertCondition(valOut1.equals(""));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



    /**
     Test setValue(name, value). Pass in null for the value.
     **/
    public void Var139()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field11", (String)null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that contains a valid initial 
     value as a String.  Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var140()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field1", Float.valueOf(123.456f));
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.trim().equals("123.456"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that contains a valid initial value. Reset the value
     using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var141()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field1", Double.valueOf(123.456));
            String valOut1 = (String)rfmlDoc.getValue("format1.field1");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.trim().equals("123.456"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: This scenario may be impossible to test.  All classes that extend Object inherit or implement a toString() method, and therefore can be converted to String.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=char contains a valid initial value. Reset the value using
///     a bad Object value that cannot be
///     converted to a String (say a Date object or something) and ensure an error
///     is thrown.
///     **/
///    public void Var205()
///    {
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=char that is not initialized. Set the value to
     a byte[] array.
     **/
    public void Var142()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6a");
            byte[] valIn = new byte[] {0,0,0,0};
            rfmlDoc.setValue("format1.field9", valIn);
            String valOut1 = (String)rfmlDoc.getValue("format1.field9");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(valOut1.trim().equals(valIn.toString()));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=Integer that is not initialized. Set the value to
     a byte[] array and ensure an error is thrown.
     **/
    public void Var143()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
            byte[] valIn = new byte[] {0,0,0,0};
            rfmlDoc.setValue("format1.field3", valIn);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          String expectedMsg = "Invalid data type '[B'. String or Number expected. Processing <data> element 'format1.field3'.";
          String receivedMsg = e.getMessage();
          ///output_.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


/// Note: Invalid scenario.  No such thing as an uninitialized Integer object.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=Integer that is not initialized. Set the value to
///     to an unitialized Integer object. Will this set the value to zero?
///     **/
///
///    public void Var208()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte[],length=12 an whose value has not been
     set. Make the type of value be a byte[]. Ensure the value is set properly.
     **/
    public void Var144()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt6");
            byte[] valIn = new byte[] {1,2,3,4,5,6,7,8,9,10,11,12};
            rfmlDoc.setValue("format1.field8", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field8");
            ///output_.println("valOut1 == " + valOut1);
            assertCondition(areEqual(valOut1,valIn));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: Invalid scenario.  Can't specify unquoted number as init value.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte, length=4 that contains a valid init value
///     that is a number. Pass in a byte[] with length = 8. Will this cause an 
///     error or will value be truncated?
///     **/
///
///    public void Var210()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte length=8 that contains a valid initial 
     value. 
     Set the value to a new valid String value and ensure the set worked correctly.
     **/
    public void Var145()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            byte[] valIn = new byte[] {2,4,6,8,10,12,14,16};
            rfmlDoc.setValue("format1.field12", "0x02, 0x04, 0x06, 0x08, 0x0a, 0x0c, 0x0e, 0x10");
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field12");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte, length=8 that contains a valid byte[]
     that was set using setValue(). Set the value to the empty array.
     **/
    public void Var146()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            byte[] valIn = new byte[] {};
            rfmlDoc.setValue("format1.field12", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field12");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



    /**
     Test setValue(name, value). Pass in null for the value.
     **/
    public void Var147()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            rfmlDoc.setValue("format1.field12", (byte[])null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte that contains a valid initial value.
     Reset the value using a Float that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var148()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Float valIn = Float.valueOf(3.0f);
            byte[] valIn0 = new byte[] {3,3,3,3,3,3,3,3,3,3,3,3};
            rfmlDoc.setValue("format1.field10", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field10");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn0));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte that contains a valid initial value. Reset the value
     using a Double that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var149()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Double valIn = Double.valueOf(3.0);
            byte[] valIn0 = new byte[] {3,3,3,3,3,3,3,3,3,3,3,3};
            rfmlDoc.setValue("format1.field10", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field10");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn0));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte that contains a valid initial value. Reset the value
     using a Integer that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var150()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Integer valIn = Integer.valueOf(3);
            byte[] valIn0 = new byte[] {3,3,3,3,3,3,3,3,3,3,3,3};
            rfmlDoc.setValue("format1.field10", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field10");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn0));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte that contains a valid initial value. Reset the value
     using a Long that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var151()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Long valIn = Long.valueOf(3);
            byte[] valIn0 = new byte[] {3,3,3,3,3,3,3,3,3,3,3,3};
            rfmlDoc.setValue("format1.field10", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field10");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn0));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
     element whose type=byte that contains a valid initial value. Reset the value
     using a Short that does not cause an overflow
     and ensure the new value has been set correctly.
     **/
    public void Var152()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt5v");
            Short valIn = Short.valueOf((short)3);
            byte[] valIn0 = new byte[] {3,3,3,3,3,3,3,3,3,3,3,3};
            rfmlDoc.setValue("format1.field10", valIn);
            byte[] valOut1 = (byte[])rfmlDoc.getValue("format1.field10");
            ///printByteArray("valOut1 == ", valOut1);
            assertCondition(areEqual(valOut1,valIn0));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: The following scenarios are covered by earlier variations.
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte that contains a valid initial value. Reset the value
///     using a Integer that does not cause an overflow
///     and ensure the new value has been set correctly.
///     **/
///
///    public void Var219()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte that contains a valid initial value. Reset the value
///     using a Long that does not cause an overflow
///     and ensure the new value has been set correctly.
///     **/
///
///    public void Var220()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte that contains a valid initial value. Reset the value
///     using a Short that does not cause an overflow
///     and ensure the new value has been set correctly.
///     **/
///
///    public void Var221()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }
///
///    /**
///     Test setValue(name, value). Pass in a valid name that maps to a &lt;data&gt;
///     element whose type=byte contains a valid initial value. Reset the value using
///     a bad Object value that cannot be
///     converted to a byte[] (say a Date object or something) and ensure an error
///     is thrown.
///     **/
///
///    public void Var222()
///    {
///        try
///        {
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

}


