///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMGetDoubleTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RM;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.RecordFormatDocument;

import test.Testcase;

/**
 The RMGetDoubleTestcase class tests the following methods of the RecordFormatDocument class:
 <li>getDoubleValue(name)
 <li>getDoubleValue(name, index)
 **/
public class RMGetDoubleTestcase extends Testcase
{
    /**
     Constructor.
     **/
    public RMGetDoubleTestcase(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMGetDoubleTestcase", namesAndVars.get("RMGetDoubleTestcase"), runMode, fileOutputStream);
    }

    /**
     Test getDoubleValue(name).  Pass in an empty string for name. What should happen?
     **/
    public void Var001()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            double result = rfmlDoc.getDoubleValue("");
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
          String expectedMsg = "<data> element named '' not found in document";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test getDoubleValue(name).  Pass in null for name. Should get null pointer exception.
     **/
    public void Var002()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            double result = rfmlDoc.getDoubleValue(null);
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

/// Note: This scenario is irrelevant, since RFML doesn't allow an empty data tag.
////**
/// Test getDoubleValue(name) with a name that maps to an empty &lt;data&gt;
/// tag in the rfml document. Should this return 0 or cause an error?
///**/
///public void Var003()
///{
///}

    /**
     Test getDoubleValue(name).  Pass a name that does not map to a          
     &lt;data&gt; tag. Ensure error is generated.
     **/
    public void Var003()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1");
            double result = rfmlDoc.getDoubleValue("format1.field99");
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
          String expectedMsg = "<data> element named 'format1.field99' not found in document";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getDoubleValue(name).  Pass a name that maps to a &lt;data&gt; element
     that can not be converted to a double.
     **/
    public void Var004()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1char1");
            double result = rfmlDoc.getDoubleValue("format1.field2");
            System.out.println("DEBUG result = " + result);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          assertCondition(///receivedMsg != null &&
                 ///receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "java.lang.NumberFormatException")); // TBD - We'll probably end up throwing an XmlException instead.
        }
    }

    /**
     Test getDoubleValue(name,index). Pass in negative number for index. Ensure
     error generated.
     **/
    public void Var005()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount1");
            double result = rfmlDoc.getDoubleValue("format1.field3", new int[] {-1});
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "An index specified is out of bounds";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getDoubleValue(name,index). Pass in an invalid number for index. Ensure
     error generated.
     **/
    public void Var006()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount1");
            double result = rfmlDoc.getDoubleValue("format1.field3", new int[] {1});
            failed("Did not throw exception."+result);
        }
        
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "An index specified is out of bounds";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getDoubleValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element whose value has not been set. Ensure exception is thrown.
     **/
    public void Var007()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zonedNoInit");
            ///Integer resultInt = (Integer)(rfmlDoc.getValue("format1.field2"));
            ///if (resultInt == null) System.out.println("getValue() returned null");
            ///else { int result = resultInt.intValue(); }
            ///int result = rfmlDoc.getIntValue("format1.field2");
            double result = rfmlDoc.getDoubleValue("format1.field1");
            failed("Did not throw exception."+result);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "Value is not set. Processing <data> element 'format1.field1'";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getDoubleValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element that contains a valid init value that is a number. Ensure proper double returned.
     **/
    public void Var008()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1char1");
            double result = rfmlDoc.getDoubleValue("format1.field1");
            assertCondition(result == 8.0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }



/// TBD - I don't know how to do this. 
////**
/// Test getDoubleValue(name). Pass in a valid name that maps to a &lt;data&gt;
/// element that contains a valid initial number value as a String. Ensure proper double returned.
/// **/
///public void Var010()
///{
///    try
///    {
///        RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zoned1");
///        double result = rfmlDoc.getDoubleValue("format1.field3");
///        assertCondition(result == 7.0);
///    }
///    catch (Exception e)
///    {
///        failed(e);
///    }
///}

    /**
     Test getDoubleValue(name). Pass in a valid name that maps to a &lt;data&gt;
     element that contains a valid number that was set using setValue(). Ensure
     proper double returned.
     **/
    public void Var009()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zonedNoInit");
            rfmlDoc.setValue("format1.field1", new Double(3.0));
            double result = rfmlDoc.getDoubleValue("format1.field1");
            assertCondition(result == 3.0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: setStringValue() is only used for type=char fields.
///    /**
///     Test getDoubleValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element that contains a valid initial number value as a String that was set
///     using setStringValue(). Ensure proper double returned.
///     **/
///    public void Var012()
///    {
///    }


/// Note: This scenario is irrelevant for RFML.
///    /**
///     Test getDoubleValue(name). Pass in a valid name that maps to a &lt;data&gt;
///     element that contains a valid initial number value as a String that was set
///     using setDoubleValue(). Ensure proper double returned.
///     **/
///    public void Var013()
///    {
///    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that contains a valid number value and pass in 0 for the index.
     Ensure the value of the data element is returned as a double.
     **/
    public void Var010()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount1init0");
            double result1 = rfmlDoc.getDoubleValue("format1.field3", new int[] {0});
            rfmlDoc.setValue("format1.field3", new int[] {0}, new Double(5.5));
            double result2 = rfmlDoc.getDoubleValue("format1.field3", new int[] {0});
            assertCondition (result1 == 0.0 && result2 == 5.5);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to an array with count=5 of elements. Pass in 0 for the index.
     Ensure the value of the data element is returned as a double.
     **/
    public void Var011()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            double result1 = rfmlDoc.getDoubleValue("format1.field4", new int[] {0});
            rfmlDoc.setValue("format1.field4", new int[] {0}, new Double(6.7));
            double result2 = rfmlDoc.getDoubleValue("format1.field4", new int[] {0});
            assertCondition (result1 == 0.0 && result2 == 6.7);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to an array with count=5 of elements. Pass in 1 for the index.
     Ensure the value of the proper data element is returned as a double.
     **/
    public void Var012()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            double init0 = rfmlDoc.getDoubleValue("format1.field4", new int[] {0});
            double init1 = rfmlDoc.getDoubleValue("format1.field4", new int[] {1});
            double init2 = rfmlDoc.getDoubleValue("format1.field4", new int[] {2});
            double init3 = rfmlDoc.getDoubleValue("format1.field4", new int[] {3});
            double init4 = rfmlDoc.getDoubleValue("format1.field4", new int[] {4});
            rfmlDoc.setValue("format1.field4", new int[] {0}, new Double(1.2));
            rfmlDoc.setValue("format1.field4", new int[] {1}, new Double(3.4));
            rfmlDoc.setValue("format1.field4", new int[] {2}, new Double(5.6));
            rfmlDoc.setValue("format1.field4", new int[] {3}, new Double(7.8));
            rfmlDoc.setValue("format1.field4", new int[] {4}, new Double(9.0));
            double result0 = rfmlDoc.getDoubleValue("format1.field4", new int[] {0});
            double result1 = rfmlDoc.getDoubleValue("format1.field4", new int[] {1});
            double result2 = rfmlDoc.getDoubleValue("format1.field4", new int[] {2});
            double result3 = rfmlDoc.getDoubleValue("format1.field4", new int[] {3});
            double result4 = rfmlDoc.getDoubleValue("format1.field4", new int[] {4});
            assertCondition (init0 == 0.0 && init1 == 0.0 && init2 == 0.0 && init3 == 0.0 && init4 == 0.0 &&
                    result0 == 1.2 && result1 == 3.4 && result2 == 5.6 && result3 == 7.8 && result4 == 9.0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// The following scenario is covered by the previous variation.
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to an array with count=5 of elements. Pass in 4 for the index.
///     Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var017()
///    {
///    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to an array with count=5 of elements. Pass in 5 for the index.
     Ensure an ArrayIndexOutOfBounds error is returned.
     **/
    public void Var013()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            double result1 = rfmlDoc.getDoubleValue("format1.field4", new int[] {5});
            failed("Did not throw exception."+result1);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "An index specified is out of bounds";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to a two dimensional array with dimensions [5,6]. Pass in [0,0] for
     the index. Ensure the value of the first data element is returned as a double.
     **/
    public void Var014()
    {
	
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
            double init0 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0,0});
            double init1 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0,1});
            double init2 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {1,0});
            double init3 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {1,1});
            double init4 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0,4});
            rfmlDoc.setValue("format1.field7.field3", new int[] {0,0}, new Double(1.2));
            rfmlDoc.setValue("format1.field7.field3", new int[] {0,1}, new Double(3.4));
            rfmlDoc.setValue("format1.field7.field3", new int[] {1,0}, new Double(5.6));
            rfmlDoc.setValue("format1.field7.field3", new int[] {1,1}, new Double(7.8));
            rfmlDoc.setValue("format1.field7.field3", new int[] {0,4}, new Double(9.0));
            double result0 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0,0});
            double result1 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0,1});
            double result2 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {1,0});
            double result3 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {1,1});
            double result4 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0,4});
            assertCondition (init0 == 0.0 && init1 == 0.0 && init2 == 0.0 && init3 == 0.0 && init4 == 0.0 &&
                    result0 == 1.2 && result1 == 3.4 && result2 == 5.6 && result3 == 7.8 && result4 == 9.0);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: The following scenarios are covered by the previous variation.
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [5,6].  Pass in [0,1] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var020()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [5,6].  Pass in [1,0] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var021()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [5,6].  Pass in [1,2] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var022()
///    {
///    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to a two dimensional array with dimensions [5,6].  Pass in [1,6] for 
     the index. Ensure an XmlException is thrown.
     **/
    public void Var015()
    {
	
      try
      {
        RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
        double init0 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {1,6});
        failed("Did not throw exception."+init0);
      }
      catch (Exception e)
      {
        ///e.printStackTrace();
        String expectedMsg = "An index specified is out of bounds";
        String receivedMsg = e.getMessage();
        ///System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to a two dimensional array with dimensions [5,6].  Pass in [5,1] for 
     the index. Ensure an XmlException is thrown.
     **/
    public void Var016()
    {
	
      try
      {
        RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
        double init0 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {5,1});
        failed("Did not throw exception."+init0);
      }
      catch (Exception e)
      {
        ///e.printStackTrace();
        String expectedMsg = "An index specified is out of bounds";
        String receivedMsg = e.getMessage();
        ///System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

    /**
     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
     element that maps to a two dimensional array with dimensions [5,6].  Pass in 0 for 
     the index. Ensure an XmlException is thrown.
     **/
    public void Var017()
    {
	
      try
      {
        RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.oneOfEachTypeCount5init0");
        double init0 = rfmlDoc.getDoubleValue("format1.field7.field3", new int[] {0});
        failed("Did not throw exception."+init0);
      }
      catch (Exception e)
      {
        ///e.printStackTrace();
        String expectedMsg = "The number of indices required is 2";
        String receivedMsg = e.getMessage();
        ///System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

/// Note: These scenarios are probably overkill.  Implementation is option.  -JPL

///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [5,6].  Pass in 3 for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var026()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [5,6].  Pass in 5 for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var027()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [5,6].  Pass in 6 for 
///     the index. Ensure ArrayIndexOutOfBounds error generated.
///     **/
///    public void Var028()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a 3 dimensional array with dimensions [2,2,2]. Pass in [0,0,0] for
///     the index. Ensure the value of the first data element is returned as a double.
///     **/
///    public void Var029()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [0,1,0] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var030()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [0,0,1] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var031()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [1,1,1] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var032()
///    {
///    }
///
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [1,1,0] for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var033()
///    {
///    }
///
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [2,0,1] for 
///     the index. Ensure an ArrayIndexOutOfBounds error is generated.
///     **/
///    public void Var034()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [1,2,1] for 
///     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
///     **/
///    public void Var035()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in [1,1,2] for 
///     the index. Ensure an ArrayIndexOutOfBoundsError is returned.
///     **/
///    public void Var036()
///    {
///    }
///
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in 0 for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var037()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in 4 for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var038()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in 5 for 
///     the index. Ensure the value of the proper data element is returned as a double.
///     **/
///    public void Var039()
///    {
///    }
///
///    /**
///     Test getDoubleValue(name, index). Pass in a valid name that maps to a &lt;data&gt;
///     element that maps to a two dimensional array with dimensions [2,2,2].  Pass in 6 for 
///     the index. Ensure ArrayIndexOutOfBounds error generated.
///     **/
///    public void Var040()
///    {
///    }

}
