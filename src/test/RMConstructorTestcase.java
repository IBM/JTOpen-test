///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMConstructorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.data.*;

/**
 The RMConstructorTestcase class tests the following methods of the RecordFormatDocument class:
 &lt;li&gt;constructors,
 &lt;li&gt;toString().
 **/
public class RMConstructorTestcase extends Testcase
{

  private static final boolean DEBUG = false;

    /**
     Constructor.
     **/
    public RMConstructorTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMConstructorTestcase", namesAndVars, runMode, fileOutputStream);
    }


    
    /**
     Default RecordFormatDocument constructor.  Verify that getDescriptor returns null since not set.
     **/
    public void Var001()
    {
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument();
            assertCondition((rfmlDoc.getDescriptor() == null) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     RecordFormatDocument(RecordFormat) constructor. Pass null and expect an exception.
     **/
    public void Var002()
    {
        try
        {
            RecordFormat recFmt = null;
            RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     RecordFormatDocument(Record) constructor. Pass null and expect an exception.
     **/
    public void Var003()
    {
        try
        {
            Record rec = null;
            RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in name that
     doesn't exist. Exception should be generated.
     **/
    public void Var004()
    {
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("bogus");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (XmlException e)
        {
            assertExceptionIsInstanceOf(e.getException(), "java.util.MissingResourceException");
        }
        catch (Exception e)
        {
          failed(e);
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists but is empty. Exception should be generated.
     **/
    public void Var005()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty0");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (XmlException e)
        {
          if (DEBUG) e.printStackTrace();
          Exception e1 = e.getException();
          if (DEBUG) {System.out.println("Embedded exception:"); e1.printStackTrace(); }
          String expectedMsg = "Premature end of file.";
          String receivedMsg = e1.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e1, "com.ibm.as400.data.ParseException"));
        }
        catch (Exception e)
        {
          failed(e);
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only &lt;rfml&gt; element with bad version "1.0" and an empty &lt;recordformat&gt; element. Verify exception thrown for the bad version.
     **/
    public void Var006()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.versionBad");
            failed("Did not throw exception but got"+rfmlDoc);
        }
        catch (XmlException e)
        {
          StringWriter stringWriter = new StringWriter();  
          PrintWriter printWriter = new PrintWriter(stringWriter);  
          e.printStackTrace(printWriter); 
          Exception e1 = e.getException();
          printWriter.println("Embedded exception:");
          e1.printStackTrace(printWriter); 
          ///String expectedMsg = "Attribute \"version\" with value \"1.0\" must have a value of \"4.0\".";
          String expectedMsg = "Attribute \"version\" with value \"1.0\" must have a value from the list \"4.0 5.0 ";
          String receivedMsg = e1.getMessage();
          printWriter.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e1, "com.ibm.as400.data.ParseException"),
			  "receivedMsg "+receivedMsg +" e1="+e1+" "+stringWriter.toString());
        }
        catch (Exception e)
        {
          failed(e);
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
     exists and contains only &lt;rfml&gt; element with good version "4.0". Verify an error is
     generated because no struct or recordformat in document.
     **/
    public void Var007()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
     exists and contains only &lt;rfml&gt; element with good version "1.0" and an empty
     &lt;recordformat&gt; element. Verify the rfml and recordformat nodes are properly
     created.
     **/
    public void Var008()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
            // Check that the node tree has exactly one node: a recordformat element with no children.
            // For convenience, get the children into a Vector.
            Descriptor descriptor = rfmlDoc.getDescriptor();
            Enumeration enumeration = descriptor.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }
            Descriptor firstChild = (Descriptor)(children.elementAt(0));

            assertCondition(children.size() == 1 &&
                   firstChild.getTagName().equals("recordformat") &&
                   firstChild.hasChildren() == false);
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Experiment:
///    public void Var009()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument(systemObject_, "test.pcml.packed1");
///            BigDecimal bigDec = new BigDecimal(123.456); // Note: The value ends up as 123.4560000000000030695446184836328029632568359375 in field1.
///            ///BigDecimal bigDec = new BigDecimal("123.456");
///
///            System.out.println("bigDec == " + bigDec.toString());
///            ///bigDec = bigDec.setScale(3, BigDecimal.ROUND_HALF_EVEN);
///            System.out.println("scale == " + bigDec.scale());
///
///            pcmlDoc.setValue("format1.field1", bigDec);
///            BigDecimal outVal1 = (BigDecimal)pcmlDoc.getValue("format1.field1");
///            System.out.println("Returned value: " + outVal1.toString());
///
///            pcmlDoc.setValue("format1.field2", bigDec);
///            Float outVal2 = (Float)pcmlDoc.getValue("format1.field2");
///            System.out.println("Returned value: " + outVal2.toString());
///
///            pcmlDoc.setValue("format1.field3", bigDec);
///            Double outVal3 = (Double)pcmlDoc.getValue("format1.field3");
///            System.out.println("Returned value: " + outVal3.toString());
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }


///    public void Var109()  // TBD temporary experiment - toRecord problem.
///    {
////* test.pcml.packed1:
///<pcml version="1.0">
///<program name="format1">
///  <data name="field1" type="packed" length="4"/>
///  <data name="field2" type="float" length="4"/>
///  <data name="field3" type="float" length="8"/>
///</program>
///</pcml>
///*/
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.zonedNoInit");
///            Record rec = rfmlDoc.toRecord("format1");
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
     contains &lt;rfml&gt; and &lt;recordformat&gt;, where the recordformat element is missing the required attribute "name". Verify exception thrown for the missing "name" attribute.
     **/
    public void Var009()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1NoName");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document
     has &lt;rfml&gt; and &lt;recordformat&gt; elements only with proper attributes.  Ensure proper nodes created after parsing.
     **/
    public void Var010()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.empty1");
            // Check that the node tree has exactly one node: a recordformat element with no children.
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }
            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) child1NumAttrs++;
            }
/* Debug only:
            System.out.println();
            System.out.println(
                   children.size() + ", " +
                   child1.getTagName() + ", " +
                   child1.hasChildren() + ", " +
                   ///child1AttList.length + ", " +
                   child1NumAttrs + ", " +
                   ///child1AttList[0] + ", " +
                   child1.getAttributeValue("name") + ", " +
                   ///rootAttList.length + ", " +
                   rootNumAttrs + ", " +
                   ///rootAttList[0] + ", " +
                   root.getAttributeValue("version"));
*/
 
            assertCondition(
                   children.size() == 1 &&
                   child1.getTagName().equals("recordformat") &&
                   child1.hasChildren() == false &&
                   child1NumAttrs == 1 &&
                   child1.getAttributeValue("name").equals("format1") &&
                   child1.getAttributeValue("description") == null &&
                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note - I don't think we should check for too-long description in our code.  Wait until we can use XML Schema.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     contains only &lt;rfml&gt; and &lt;recordformat&gt; elements and pass in bad description (&gt;50)
///     attribute for &lt;recordformat&gt;. Ensure exception thrown.
///     **/
///    public void Var011()
///    {
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument("empty1LongDesc");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
///        }
///    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
     contains only a &lt;recordformat&gt; element. Verify exception thrown for the missing &lt;rfml&gt; tag.
     **/
    public void Var011()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.emptyNoRfml");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          ///String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 ///receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document
     that contains only 2 &lt;recordformat&gt; elements both with the same name. Ensure an exception is thrown.
     **/
    public void Var012()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoRecFmtsSameName");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is tested by Var010.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     contains only a &lt;recordformat&gt; entry that has a name only and no description.
///     Ensure node properly built and description attribute is null.
///     **/
///    public void Var014()
///    {
///    }



    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document
     that contains only 2 &lt;recordformat&gt; elements each with a different name. Ensure 2 unique nodes are created.
     **/
    public void Var013()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoRecFmts");
            // Check that the node tree has exactly one node: a recordformat element with no children.
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) child1NumAttrs++;
            }

            Descriptor child2 = (Descriptor)(children.elementAt(1));
            String[] child2AttList = child2.getAttributeList();

            int child2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2AttList.length; i++) {
              String attrVal = child2.getAttributeValue(child2AttList[i]);
              if (attrVal != null) child2NumAttrs++;
            }
/* Debug only:
            System.out.println();
            System.out.println(
                   children.size() + ", " +
                   child1.getTagName() + ", " +
                   child1.hasChildren() + ", " +
                   ///child1AttList.length + ", " +
                   child1NumAttrs + ", " +
                   ///child1AttList[0] + ", " +
                   child1.getAttributeValue("name") + ", " +
                   ///rootAttList.length + ", " +
                   rootNumAttrs + ", " +
                   ///rootAttList[0] + ", " +
                   root.getAttributeValue("version"));
*/
 
            assertCondition(
                   children.size() == 2 &&
                   child1.getTagName().equals("recordformat") &&
                   child1.hasChildren() == false &&
                   child1NumAttrs == 1 &&
                   child1.getAttributeValue("name").equals("format1") &&
                   child1.getAttributeValue("description") == null &&
                   child2.getTagName().equals("recordformat") &&
                   child2.hasChildren() == false &&
                   child2NumAttrs == 1 &&
                   child2.getAttributeValue("name").equals("format2") &&
                   child2.getAttributeValue("description") == null &&
                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: This scenario is tested by the prior variation.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document
///     that contains only 2 &lt;recordformat&gt; elements each with no attributes. Ensure
///     2 unique nodes are created. What is ID for each in this case?
///     **/
///    public void Var016()
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in document
     that contains only 1 &lt;recordformat&gt; element containing a &lt;data&gt; element with redirected "length", "count", and "ccsid" attributes.
     **/
    public void Var014()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.indirectLengthAndCount");
            // Check that the node tree has exactly one child node: a recordformat element with 4 children.
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) child1NumAttrs++;
            }

            enumeration =child1.getChildren();
            Vector child1children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child1children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
                child1Child1NumAttrs++;
              }
            }

            rfmlDoc.setValue("format1.field1", new int[] {0}, "first1");
            rfmlDoc.setValue("format1.field1", new int[] {1}, "second");
            rfmlDoc.setValue("format1.field1", new int[] {2}, "third1");
/*
            System.out.println("format1.field1[0]: " + (String)rfmlDoc.getValue("format1.field1", new int[] {0}));
            System.out.println("format1.field1[1]: " + (String)rfmlDoc.getValue("format1.field1", new int[] {1}));
            System.out.println("format1.field1[2]: " + (String)rfmlDoc.getValue("format1.field1", new int[] {2}));
*/
            byte[] received = rfmlDoc.toByteArray("format1");

            int[] expectedVals = new int[] {
              0x86, 0x89, 0x99, 0xA2, 0xA3, 0xF1,     // "first1"
              0xA2, 0x85, 0x83, 0x96, 0x95, 0x84,     // "second"
              0xA3, 0x88, 0x89, 0x99, 0x84, 0xF1,     // "third1"
              0xC1, 0x40, 0x40, 0x40, 0x40, 0x40,     // "A     "
              0xC1, 0x40, 0x40, 0x40, 0x40, 0x40,     // "A     "
              0x00, 0x06,                             // lengthOfField1 == 6
              0x00, 0x05,                             // countOfField1  == 5
              0x00, 0x25                              // ccsidOfField1  == 37 (0x25)
            };
            byte[] expected = createByteArray(expectedVals);

/* Debug only:
            System.out.println("Expected:");
            printByteArray(expected);
            System.out.println("Received:");
            printByteArray(received);

            System.out.println();
            System.out.println(
                   children.size() + ", " +
                   child1.getTagName() + ", " +
                   child1.hasChildren() + ", " +
                   child1NumAttrs + ", " +
                   child1.getAttributeValue("name") + ", " +
                   child1.getAttributeValue("description") + ", " +

                   child1Child1.getTagName() + ", " +
                   child1Child1.hasChildren() + ", " +
                   child1Child1NumAttrs + ", " +
                   child1Child1.getAttributeValue("type") + ", " +
                   child1Child1.getAttributeValue("name") + ", " +
                   child1Child1.getAttributeValue("length") + ", " +
                   child1Child1.getAttributeValue("count") + ", " +
                   child1Child1.getAttributeValue("ccsid") + ", " +

                   rfmlDoc.getValue("format1.field1", new int[] {0}) + ", " +
                   rfmlDoc.getValue("format1.field1", new int[] {1}) + ", " +
                   rfmlDoc.getValue("format1.field1", new int[] {2}) + ", " +

                   rootNumAttrs + ", " +
                   root.getAttributeValue("version"));
*/
 
            assertCondition(
                   children.size() == 1 &&
                   child1.getTagName().equals("recordformat") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&
                   child1.getAttributeValue("name").equals("format1") &&
                   child1.getAttributeValue("description") == null &&

                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 6 &&  // name, type, length, count, ccsid, init
                   child1Child1.getAttributeValue("type").equals("char") &&
                   child1Child1.getAttributeValue("name").equals("field1") &&
                   child1Child1.getAttributeValue("length").equals("lengthOfField1") &&
                   child1Child1.getAttributeValue("count").equals("countOfField1") &&
                   child1Child1.getAttributeValue("ccsid").equals("ccsidOfField1") &&

                   rfmlDoc.getValue("format1.field1", new int[] {0}).equals("first1") &&
                   rfmlDoc.getValue("format1.field1", new int[] {1}).equals("second") &&
                   rfmlDoc.getValue("format1.field1", new int[] {2}).equals("third1") &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0") &&

                   areEqual(expected, received));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document
     that contains only 2 &lt;rfml&gt; elements.
     Ensure proper error thrown.
     **/
    public void Var015()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoRfmls");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists but contains a bad SYSTEM reference, i.e.,
     &lt;!DOCTYPE rfml SYSTEM "rfml_bad.dtd"&gt;.
     Ensure proper error thrown
     **/
    public void Var016()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.systemBad");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          if (DEBUG) e.printStackTrace();
          ///String expectedMsg = "Exception 'java.io.FileNotFoundException' received.";
          String expectedMsg = "java.io.FileNotFoundException";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists but contains a pcml SYSTEM reference, i.e.,
     &lt;!DOCTYPE rfml SYSTEM "pcml.dtd"&gt;.
     Ensure proper error thrown
     **/
    public void Var017()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.systemPcml");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          if (DEBUG) e.printStackTrace();
          ///String expectedMsg = "Exception 'java.io.FileNotFoundException' received.";
          String expectedMsg = "java.io.FileNotFoundException";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists but contains a blank SYSTEM reference.
     Ensure proper error thrown.
     **/
    public void Var018()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.systemBlank");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          if (DEBUG) e.printStackTrace();
          ///String expectedMsg = "Exception 'java.net.MalformedURLException' received.";
          String expectedMsg1 = "java.net.MalformedURLException";   // This is expected with JDK 1.4 or earlier
          String expectedMsg2 = "A parse error occurred";           // This is expected with JDK 1.5 or later
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 ((receivedMsg.indexOf(expectedMsg1) != -1) || (receivedMsg.indexOf(expectedMsg2) != -1)) &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists but does not contain &lt;?xml version=1.0 encoding="UTF-8" standalone="no"?&gt; line and see what happens.
     **/
    public void Var019()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.xmlNoVers");
            notApplicable("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;recordformat&gt; element but has no matching end element tag.
     Ensure proper error thrown.
     **/
    public void Var020()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtNoEndTag");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;struct&gt; element but has no matching end element tag.
     Ensure proper error thrown.
     **/
    public void Var021()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structNoEndTag");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;struct&gt; element with only the minimally required elements and attributes.
     Ensure proper node created.

     **/
    public void Var022()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.struct0");
            // Check that the root node has exactly one child: a struct element with exactly one child (a data element).
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector grandChildren = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              grandChildren.addElement(child);
            }

            Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));
            String[] grandChild1AttList = grandChild1.getAttributeList();

            int grandChild1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild1AttList.length; i++) {
              String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
                grandChild1NumAttrs++;
              }
            }

            if (DEBUG)
            {
              System.out.println();
              System.out.println(
                                 children.size() + ", " +
                                 grandChildren.size() + ", " +
                                 child1.getTagName() + ", " +
                                 child1.hasChildren() + ", " +
                                 child1NumAttrs + ", " +
                                 child1.getAttributeValue("name") + ", " +
                                 grandChild1.getTagName() + ", " +
                                 grandChild1.hasChildren() + ", " +
                                 grandChild1NumAttrs + ", " +
                                 grandChild1.getAttributeValue("type") + ", " +
                                 grandChild1.getAttributeValue("name") + ", " +
                                 grandChild1.getAttributeValue("length") + ", " +
                                 rootNumAttrs + ", " +
                                 root.getAttributeValue("version"));
            }

 
            assertCondition(
                   children.size() == 1 &&
                   grandChildren.size() == 1 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&  // name
                   child1.getAttributeValue("name").equals("struct1") &&
                   grandChild1.getTagName().equals("data") &&
                   grandChild1.hasChildren() == false &&
                   grandChild1NumAttrs == 3 && // name, type, length
                   grandChild1.getAttributeValue("type").equals("char") &&
                   grandChild1.getAttributeValue("name").equals("field1") &&
                   grandChild1.getAttributeValue("length").equals("6") &&
                   grandChild1.getAttributeValue("count") == null &&
                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only one empty &lt;struct&gt; element.
     Ensure error is thrown since a struct must contain one or more data or struct nodes.

     **/
    public void Var023()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structEmpty");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
     exists and contains 2 &lt;struct&gt; elements with same name.
     Ensure error is thrown.
     **/
    public void Var024()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoStructsSameName");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;struct&gt; element that contains 3 valid &lt;data&gt; nodes.  Validate the attributes of the struct.
     Ensure data nodes just have a name and type.  Ensure proper node tree is built.

     **/
    public void Var025()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structWith3Datas");
            // Check that the root node has exactly one child: a struct element with exactly 3 children (data elements).
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector grandChildren = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              grandChildren.addElement(child);
            }

            Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));
            String[] grandChild1AttList = grandChild1.getAttributeList();
            int grandChild1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild1AttList.length; i++) {
              String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
                grandChild1NumAttrs++;
              }
            }

            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));
            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));
            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
                grandChild3NumAttrs++;
              }
            }

/* Debug only: */
//            System.out.println();
//            System.out.println(
//                   children.size() + ", " +
//                   child1.getTagName() + ", " +
//                   child1.hasChildren() + ", " +
//                   child1NumAttrs + ", " +
//                   child1.getAttributeValue("name") + ", " +
//                   grandChild1.getTagName() + ", " +
//                   grandChild1.hasChildren() + ", " +
//                   grandChild1NumAttrs + ", " +
//                   grandChild1.getAttributeValue("type") + ", " +
//                   grandChild1.getAttributeValue("name") + ", " +
//                   rootNumAttrs + ", " +
//                   root.getAttributeValue("version"));
/* */
 
            assertCondition(
                   children.size() == 1 &&
                   grandChildren.size() == 3 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&  // name
                   child1.getAttributeValue("name").equals("struct1") &&

                   grandChild1.getTagName().equals("data") &&
                   grandChild1.hasChildren() == false &&
                   grandChild1NumAttrs == 3 && // name, type, length
                   grandChild1.getAttributeValue("type").equals("char") &&
                   grandChild1.getAttributeValue("name").equals("field1") &&

                   grandChild2.getTagName().equals("data") &&
                   grandChild2.hasChildren() == false &&
                   grandChild2NumAttrs == 3 && // name, type, length
                   grandChild2.getAttributeValue("type").equals("byte") &&
                   grandChild2.getAttributeValue("name").equals("field2") &&

                   grandChild3.getTagName().equals("data") &&
                   grandChild3.hasChildren() == false &&
                   grandChild3NumAttrs == 3 && // name, type, length
                   grandChild3.getAttributeValue("type").equals("char") &&
                   grandChild3.getAttributeValue("name").equals("field3") &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains a valid &lt;struct&gt; element that contains another &lt;struct&gt; element.
     Ensure proper error thrown.

     **/
    public void Var026()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structInStruct");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains an array of &lt;struct&gt; elements, i.e., count > 1.
     Ensure proper node tree is generated.

     **/
    public void Var027()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray");

            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector child1children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child1children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
                child1Child1NumAttrs++;
              }
            }

            Descriptor child2 = (Descriptor)(children.elementAt(1));
            String[] child2AttList = child2.getAttributeList();

            int child2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2AttList.length; i++) {
              String attrVal = child2.getAttributeValue(child2AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child2Attr: " + child2AttList[i] + " = " + attrVal);
                child2NumAttrs++;
              }
            }

            enumeration =child2.getChildren();
            Vector child2children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child2children.addElement(child);
            }

            Descriptor child2Child1 = (Descriptor)(child2children.elementAt(0));
            String[] child2Child1AttList = child2Child1.getAttributeList();

            int child2Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2Child1AttList.length; i++) {
              String attrVal = child2Child1.getAttributeValue(child2Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child2Child1Attr: " + child2Child1AttList[i] + " = " + attrVal);
                child2Child1NumAttrs++;
              }
            }

/* Debug only:
            System.out.println();
            System.out.println(
                   children.size() + ", " +

                   child1children.size() + ", " +
                   child1.getTagName() + ", " +
                   child1.hasChildren() + ", " +
                   child1NumAttrs + ", " +
                   child1.getAttributeValue("name") + ", " +
                   child1Child1.getTagName() + ", " +
                   child1Child1.hasChildren() + ", " +
                   child1Child1NumAttrs + ", " +
                   child1Child1.getAttributeValue("type") + ", " +
                   child1Child1.getAttributeValue("name") + ", " +
                   child1Child1.getAttributeValue("length") + ", " +

                   child2children.size() + ", " +
                   child2.getTagName() + ", " +
                   child2.hasChildren() + ", " +
                   child2NumAttrs + ", " +
                   child2.getAttributeValue("name") + ", " +
                   child2Child1.getTagName() + ", " +
                   child2Child1.hasChildren() + ", " +
                   child2Child1NumAttrs + ", " +
                   child2Child1.getAttributeValue("type") + ", " +
                   child2Child1.getAttributeValue("struct") + ", " +
                   child2Child1.getAttributeValue("count") + ", " +

                   rootNumAttrs + ", " +
                   root.getAttributeValue("version"));
*/
 
            assertCondition(
                   children.size() == 2 &&

                   child1children.size() == 1 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&  // name
                   child1.getAttributeValue("name").equals("struct1") &&
                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 3 &&  // name, type, length
                   child1Child1.getAttributeValue("type").equals("char") &&
                   child1Child1.getAttributeValue("name").equals("field1") &&
                   child1Child1.getAttributeValue("length").equals("1") &&

                   child2children.size() == 1 &&
                   child2.getTagName().equals("recordformat") &&
                   child2.hasChildren() == true &&
                   child2NumAttrs == 1 &&  // name
                   child2.getAttributeValue("name").equals("format1") &&
                   child2Child1.getTagName().equals("data") &&
                   child2Child1.hasChildren() == true && // it points to a struct with children
                   child2Child1NumAttrs == 4 &&  // name, type, struct, count
                   child2Child1.getAttributeValue("type").equals("struct") &&
                   child2Child1.getAttributeValue("struct").equals("struct1") &&
                   child2Child1.getAttributeValue("count").equals("2") &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains a two dimensional array of &lt;struct&gt; elements.
     Ensure proper node tree is generated.

     **/
    public void Var028()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structArray2Dim");

            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector child1children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child1children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
                child1Child1NumAttrs++;
              }
            }

            Descriptor child2 = (Descriptor)(children.elementAt(1));
            String[] child2AttList = child2.getAttributeList();

            int child2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2AttList.length; i++) {
              String attrVal = child2.getAttributeValue(child2AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child2Attr: " + child2AttList[i] + " = " + attrVal);
                child2NumAttrs++;
              }
            }

            enumeration =child2.getChildren();
            Vector child2children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child2children.addElement(child);
            }

            Descriptor child2Child1 = (Descriptor)(child2children.elementAt(0));
            String[] child2Child1AttList = child2Child1.getAttributeList();

            int child2Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2Child1AttList.length; i++) {
              String attrVal = child2Child1.getAttributeValue(child2Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child2Child1Attr: " + child2Child1AttList[i] + " = " + attrVal);
                child2Child1NumAttrs++;
              }
            }

            Descriptor child3 = (Descriptor)(children.elementAt(2));
            String[] child3AttList = child3.getAttributeList();

            int child3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child3AttList.length; i++) {
              String attrVal = child3.getAttributeValue(child3AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child3Attr: " + child3AttList[i] + " = " + attrVal);
                child3NumAttrs++;
              }
            }

            enumeration =child3.getChildren();
            Vector child3children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child3children.addElement(child);
            }

            Descriptor child3Child1 = (Descriptor)(child3children.elementAt(0));
            String[] child3Child1AttList = child3Child1.getAttributeList();

            int child3Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child3Child1AttList.length; i++) {
              String attrVal = child3Child1.getAttributeValue(child3Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child3Child1Attr: " + child3Child1AttList[i] + " = " + attrVal);
                child3Child1NumAttrs++;
              }
            }

            enumeration =child3Child1.getChildren();
            Vector child3Child1children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child3Child1children.addElement(child);
            }

            Descriptor child3Child1Child1 = (Descriptor)(child3Child1children.elementAt(0));
            String[] child3Child1Child1AttList = child3Child1Child1.getAttributeList();

            int child3Child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child3Child1Child1AttList.length; i++) {
              String attrVal = child3Child1Child1.getAttributeValue(child3Child1Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child3Child1Child1Attr: " + child3Child1Child1AttList[i] + " = " + attrVal);
                child3Child1Child1NumAttrs++;
              }
            }

/* Debug only:
            System.out.println();
            System.out.println(
                   children.size() + ", " +

                   );
*/
 
            assertCondition(
                   children.size() == 3 &&

                   child1children.size() == 1 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&  // name
                   child1.getAttributeValue("name").equals("struct1") &&
                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == true &&
                   child1Child1NumAttrs == 4 &&  // name, type, length, count
                   child1Child1.getAttributeValue("type").equals("struct") &&
                   child1Child1.getAttributeValue("struct").equals("struct2") &&
                   child1Child1.getAttributeValue("count").equals("3") &&

                   child2children.size() == 1 &&
                   child2.getTagName().equals("struct") &&
                   child2.hasChildren() == true &&
                   child2NumAttrs == 1 &&  // name
                   child2.getAttributeValue("name").equals("struct2") &&
                   child2Child1.getTagName().equals("data") &&
                   child2Child1.hasChildren() == false &&
                   child2Child1NumAttrs == 3 &&  // name, type, length
                   child2Child1.getAttributeValue("type").equals("char") &&
                   child2Child1.getAttributeValue("length").equals("1") &&
                   child2Child1.getAttributeValue("count") == null &&

                   child3children.size() == 1 &&
                   child3.getTagName().equals("recordformat") &&
                   child3.hasChildren() == true &&
                   child3NumAttrs == 1 &&  // name
                   child3.getAttributeValue("name").equals("format1") &&
                   child3Child1.getTagName().equals("data") &&
                   child3Child1.hasChildren() == true && // it points to a struct with children
                   child3Child1NumAttrs == 4 &&  // name, type, struct, count
                   child3Child1.getAttributeValue("type").equals("struct") &&
                   child3Child1.getAttributeValue("struct").equals("struct1") &&
                   child3Child1.getAttributeValue("count").equals("2") &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note - This scenario is overkill.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains a three dimensional array of &lt;struct&gt; elements.
///     Ensure proper node tree is generated.
///
///     **/
///    public void Var031()
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;struct&gt; element that contains a negative value for count.
     Ensure error generated.

     **/
    public void Var029()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structBadCount");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A PCML specification error occurred.";  //TBD - fix the MRI.
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains a &lt;data&gt; element of type "struct" that refers to a nonexistent struct.
     Ensure error generated.

     **/
    public void Var030()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structBadRef");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains a &lt;data&gt; element of type "struct" that doesn't also have a 'struct' attribute.
     Ensure error generated.

     **/
    public void Var031()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structNoRef");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";  //TBD - fix the MRI.
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains a &lt;data&gt; element of type "struct" that also specifies a value for the 'length' attribute.
     Ensure error generated.

     **/
    public void Var032()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataStructWithLength");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";  //TBD - fix the MRI.
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is irrelevant, since RFML doesn't support an "offset" attribute for <struct>.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;struct&gt; element that contains a negative value
///     for offset. Ensure error generated.
///
///     **/
///    public void Var033()
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
/// Note: This scenario is irrelevant, since RFML doesn't support an "offsetfrom" attribute for <struct>.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;struct&gt; element that contains a negative value
///     for offsetfrom. Ensure error generated.
///
///     **/
///    public void Var034()
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
/// Note: This scenario is irrelevant, since RFML doesn't support an "outputsize" attribute for <struct>.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;struct&gt; element that contains a negative value
///     for outputsize. Ensure error generated.
///
///     **/
///    public void Var035()
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
/// Note: This scenario is irrelevant, since RFML requires a "name" attribute for each struct.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains 2 &lt;struct&gt; element with no attributes.
///     Ensure proper nodes created with proper ids. How does it set the id when not given?
///
///     **/
///    public void Var036()
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;data&gt; element with minimal attributes.
     Ensure error generated because doc must contain a struct or a recordformat element.

     **/
    public void Var033()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataOnly");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          ///String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;data&gt; element with no attributes and one &lt;recordformat&gt; element.
     Ensure proper node created.

     **/
    public void Var034()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmt1");
            // Check that the root node has exactly one child: a recordformat element with exactly one child (a data element).
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector child1Children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child1Children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1Children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
                child1Child1NumAttrs++;
              }
            }

            if (DEBUG)
            {
              System.out.println();
              System.out.println(
                                 children.size() + ", " +
                                 child1Children.size() + ", " +
                                 child1.getTagName() + ", " +
                                 child1.hasChildren() + ", " +
                                 child1NumAttrs + ", " +
                                 child1.getAttributeValue("name") + ", " +
                                 child1Child1.getTagName() + ", " +
                                 child1Child1.hasChildren() + ", " +
                                 child1Child1NumAttrs + ", " +
                                 child1Child1.getAttributeValue("name") + ", " +
                                 child1Child1.getAttributeValue("type") + ", " +
                                 child1Child1.getAttributeValue("length") + ", " +
                                 child1Child1.getAttributeValue("init") + ", " +
                                 child1Child1.getAttributeValue("count") + ", " +
                                 rootNumAttrs + ", " +
                                 root.getAttributeValue("version"));
            }

            assertCondition(
                   children.size() == 1 &&
                   child1Children.size() == 1 &&
                   child1.getTagName().equals("recordformat") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&  // name
                   child1.getAttributeValue("name").equals("format1") &&
                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 4 &&  // name, type, length, init
                   child1Child1.getAttributeValue("name").equals("field1") &&
                   child1Child1.getAttributeValue("type").equals("char") &&
                   child1Child1.getAttributeValue("length").equals("2") &&
                   child1Child1.getAttributeValue("init").equals(" ") &&
                   child1Child1.getAttributeValue("count") == null &&
                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// This is not a valid scenario.  All data elements must be inside a recordformat.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains only one empty &lt;data&gt; element with a &lt;struct&gt; element.
///     Ensure a proper empty node is created.
///
///     **/
///    public void Var039()
///    {
///    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains 2 &lt;data&gt; elements with same name.
     Ensure error is thrown.
     **/
    public void Var035()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.twoDataSameName");
            ///AS400 sys = new AS400();
            ///ProgramCallDocument pcmlDoc = new ProgramCallDocument(sys, "test.pcml.twoDataSameName");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;struct&gt; element that contains 3 valid &lt;data&gt; nodes.  Ensure &lt;data&gt; has all attributes.
     Ensure proper node tree is built with proper attribute values for each node.

     **/
    public void Var036()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.structDataAttrs");
            // Check that the root node has exactly one child: a struct element with exactly 3 children (data elements).
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector grandChildren = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              grandChildren.addElement(child);
            }

            Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));
            String[] grandChild1AttList = grandChild1.getAttributeList();
            int grandChild1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild1AttList.length; i++) {
              String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
                grandChild1NumAttrs++;
              }
            }

            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));
            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));
            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                ///System.out.println("grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
                grandChild3NumAttrs++;
              }
            }

/* Debug only: 
            System.out.println();
            System.out.println(
                   children.size() + ", " +
                   grandChildren.size() + ", " +
                   child1.getTagName() + ", " +
                   child1.hasChildren() + ", " +
                   child1NumAttrs + ", " +
                   child1.getAttributeValue("name") + ", " +

                   grandChild1.getTagName() + ", " +
                   grandChild1.hasChildren() + ", " +
                   grandChild1NumAttrs + ", " +
                   grandChild1.getAttributeValue("type") + ", " +
                   grandChild1.getAttributeValue("name") + ", " +
                   grandChild1.getAttributeValue("count") + ", " +
                   grandChild1.getAttributeValue("length") + ", " +
                   grandChild1.getAttributeValue("ccsid") + ", " +
                   grandChild1.getAttributeValue("init") + ", " +
                   grandChild1.getAttributeValue("bidistringtype") + ", " +

                   grandChild2.getTagName() + ", " +
                   grandChild2.hasChildren() + ", " +
                   grandChild2NumAttrs + ", " +
                   grandChild2.getAttributeValue("type") + ", " +
                   grandChild2.getAttributeValue("name") + ", " +
                   grandChild2.getAttributeValue("count") + ", " +
                   grandChild2.getAttributeValue("length") + ", " +
                   grandChild2.getAttributeValue("ccsid") + ", " +
                   grandChild2.getAttributeValue("init") + ", " +
                   grandChild2.getAttributeValue("bidistringtype") + ", " +
                   grandChild2.getAttributeValue("precision") + ", " +

                   grandChild3.getTagName() + ", " +
                   grandChild3.hasChildren() + ", " +
                   grandChild3NumAttrs + ", " +
                   grandChild3.getAttributeValue("type") + ", " +
                   grandChild3.getAttributeValue("name") + ", " +
                   grandChild3.getAttributeValue("count") + ", " +
                   grandChild3.getAttributeValue("length") + ", " +
                   grandChild3.getAttributeValue("ccsid") + ", " +
                   grandChild3.getAttributeValue("init") + ", " +
                   grandChild3.getAttributeValue("precision") + ", " +
                   grandChild3.getAttributeValue("bidistringtype") + ", " +

                   rootNumAttrs + ", " +
                   root.getAttributeValue("version")
                   );
*/
 
            assertCondition(
                   children.size() == 1 &&
                   grandChildren.size() == 3 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 && // name
                   child1.getAttributeValue("name").equals("struct1") &&

                   grandChild1.getTagName().equals("data") &&
                   grandChild1.hasChildren() == false &&
                   grandChild1NumAttrs == 7 &&
                   grandChild1.getAttributeValue("type").equals("char") &&
                   grandChild1.getAttributeValue("name").equals("field1") &&
                   grandChild1.getAttributeValue("count").equals("1") &&
                   grandChild1.getAttributeValue("length").equals("5") &&
                   grandChild1.getAttributeValue("ccsid").equals("819") &&
                   grandChild1.getAttributeValue("init").equals("ABC") &&
                   grandChild1.getAttributeValue("bidistringtype").equals("ST4") &&

                   grandChild2.getTagName().equals("data") &&
                   grandChild2.hasChildren() == false &&
                   grandChild2NumAttrs == 5 &&
                   grandChild2.getAttributeValue("type").equals("byte") &&
                   grandChild2.getAttributeValue("name").equals("field2") &&
                   grandChild2.getAttributeValue("count").equals("2") &&
                   grandChild2.getAttributeValue("length").equals("6") &&
                   grandChild2.getAttributeValue("ccsid") == null &&
                   grandChild2.getAttributeValue("init").equals("1") &&
                   grandChild2.getAttributeValue("bidistringtype") == null &&
                   grandChild2.getAttributeValue("precision") == null &&

                   grandChild3.getTagName().equals("data") &&
                   grandChild3.hasChildren() == false &&
                   grandChild3NumAttrs == 5 &&
                   grandChild3.getAttributeValue("type").equals("int") &&
                   grandChild3.getAttributeValue("name").equals("field3") &&
                   grandChild3.getAttributeValue("count") == null &&
                   grandChild3.getAttributeValue("length").equals("2") &&
                   grandChild3.getAttributeValue("ccsid") == null &&
                   grandChild3.getAttributeValue("init").equals("3") &&
                   grandChild3.getAttributeValue("precision").equals("16") &&
                   grandChild3.getAttributeValue("bidistringtype") == null &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;recordformat&gt; element that contains 1 valid &lt;data&gt; nodes.
     Ensure the &lt;data&gt; has type struct and struct attribute.
     Ensure proper node tree is built.

     **/
    public void Var037()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtWithStruct");
            // Check that the root node has exactly 2 children: a struct with a <data> child, and a recordformat with a <data> child.
            // For convenience, get the children into a Vector.
            Descriptor root = rfmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration enumeration = root.getChildren();
            Vector children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            Descriptor child2 = (Descriptor)(children.elementAt(1));
            String[] child2AttList = child2.getAttributeList();

            int child2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2AttList.length; i++) {
              String attrVal = child2.getAttributeValue(child2AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child2Attr: " + child2AttList[i] + " = " + attrVal);
                child2NumAttrs++;
              }
            }

            enumeration =child1.getChildren();
            Vector child1Children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child1Children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1Children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
                child1Child1NumAttrs++;
              }
            }

            enumeration =child2.getChildren();
            Vector child2Children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child2Children.addElement(child);
            }

            Descriptor child2Child1 = (Descriptor)(child2Children.elementAt(0));
            String[] child2Child1AttList = child2Child1.getAttributeList();

            int child2Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2Child1AttList.length; i++) {
              String attrVal = child2Child1.getAttributeValue(child2Child1AttList[i]);
              if (attrVal != null) {
                ///System.out.println("child2Child1Attr: " + child2Child1AttList[i] + " = " + attrVal);
                child2Child1NumAttrs++;
              }
            }

            enumeration =child2Child1.getChildren();
            Vector child2Child1Children = new Vector();
            while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              child2Child1Children.addElement(child);
            }
            Descriptor child2Child1Child1 = (Descriptor)(child2Child1Children.elementAt(0));
//            System.out.println("child2Child1Child1: " + child2Child1Child1.getAttributeValue("name"));

/* Debug only:
            System.out.println();
            System.out.println(
                   children.size() + ", " +

                   child1Children.size() + ", " +
                   child1.getTagName() + ", " +
                   child1.hasChildren() + ", " +
                   child1NumAttrs + ", " +
                   child1.getAttributeValue("name") + ", " +
                   child1Child1.getTagName() + ", " +
                   child1Child1.hasChildren() + ", " +
                   child1Child1NumAttrs + ", " +
                   child1Child1.getAttributeValue("name") + ", " +
                   child1Child1.getAttributeValue("type") + ", " +

                   child2Children.size() + ", " +
                   child2.getTagName() + ", " +
                   child2.hasChildren() + ", " +
                   child2NumAttrs + ", " +
                   child2.getAttributeValue("name") + ", " +
                   child2Child1.getTagName() + ", " +
                   child2Child1.hasChildren() + ", " +
                   child2Child1NumAttrs + ", " +
                   child2Child1.getAttributeValue("name") + ", " +
                   child2Child1.getAttributeValue("type") + ", " +
                   child2Child1.getAttributeValue("struct") + ", " +

                   rootNumAttrs + ", " +
                   root.getAttributeValue("version"));
*/

            assertCondition(
                   children.size() == 2 &&

                   child1Children.size() == 1 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 1 &&  // name
                   child1.getAttributeValue("name").equals("struct1") &&
                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 3 &&  // name, type, length
                   child1Child1.getAttributeValue("name").equals("field2") &&
                   child1Child1.getAttributeValue("type").equals("char") &&
                   child1Child1.getAttributeValue("length").equals("1") &&

                   child2Children.size() == 1 &&
                   child2.getTagName().equals("recordformat") &&
                   child2.hasChildren() == true &&
                   child2NumAttrs == 1 &&  // name
                   child2.getAttributeValue("name").equals("format1") &&
                   child2Child1.getTagName().equals("data") &&
                   child2Child1.hasChildren() == true && // its children are the struct contents
                   child2Child1NumAttrs == 3 &&  // name, type, struct
                   child2Child1.getAttributeValue("name").equals("field1") &&
                   child2Child1.getAttributeValue("type").equals("struct") &&
                   child2Child1.getAttributeValue("struct").equals("struct1") &&
                   child2Child1Child1.getAttributeValue("name").equals("field2") &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0"));
        }
        catch (Exception e)
        {
            failed(e);
        }
    }


/// Note: This scenario is tested by other variations.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains an array of &lt;data&gt; elements, i.e., count > 1.
///     Ensure proper node tree is generated.
///
///     **/
///    public void Var043()
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
/// Note: This scenario is overkill.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains a &lt;data&gt; element that references a struct that contains a one dimensional
///     array of &lt;struct&gt; elements.
///     Ensure proper node tree is generated.
///
///     **/
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
///
/// Note: This scenario is overkill.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains a &lt;data&gt; element that references a struct that contains a two dimensional
///     array of &lt;struct&gt; elements.
///     Ensure proper node tree is generated.
///
///     **/
///    public void Var045()
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
/// Note: This scenario is overkill.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains an array of &lt;data&gt; elements, i.e. count>1 that reference a struct that
///     contains a one dimensional array of &lt;struct&gt; elements.
///     Ensure proper node tree is generated.
///
///     **/
///    public void Var046()
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
/// Note: This scenario is overkill.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains an array of &lt;data&gt; elements, i.e. count> 1 that reference a struct that
///     contains a two dimensional array of &lt;struct&gt; elements.
///     Ensure proper node tree is generated.
///
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for count.
     Ensure error generated.

     **/
    public void Var038()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataCountBad");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is irrelevant.  RFML does not use the 'offset' attribute.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;data&gt; element that contains a negative value
///     for offset. Ensure error generated.
///
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
///
/// Note: This scenario is irrelevant.  RFML does not use the 'offsetfrom' attribute.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;data&gt; element that contains a negative value
///     for offsetfrom. Ensure error generated.
///
///     **/
///    public void Var050()
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for length.
     Ensure error generated.

     **/
    public void Var039()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataLengthNegative");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains no value for length.
     Ensure error generated.

     **/
    public void Var040()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataLengthMissing");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a blank value for length.
     Ensure error generated.

     **/
    public void Var041()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataLengthBlank");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for precision.
     Ensure error generated.

     **/
    public void Var042()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataPrecisionNegative");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for ccsid.
     Ensure error generated.

     **/
    public void Var043()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataCcsidNegative");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is tested by other variations.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;data&gt; element that contains an initial value
///     Ensure proper node generated.
///
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains an invalid type value.
     Ensure error generated.

     **/
    public void Var044()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataTypeBad");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a non-valid value for ccsid, e.g. 70000.
     Ensure error generated.

     **/
    public void Var045()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataCcsidBad");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a reference to a &lt;struct&gt; that does not exist.
     Ensure error generated.

     **/
    public void Var046()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataStructNotExist");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          // String expectedMsg = "A parse error occurred";
          // String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(///receivedMsg != null &&  // TBD - For some reason the msg is null.
                 ///receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a bad value for bidistringtype.
     Ensure error generated.

     **/
    public void Var047()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.dataBidiBad");
            failed("Did not throw exception."+rfmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is irrelevant, since the 'description' attribute has been dropped.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;data&gt; element that contains a description whose length
///     is > 50. Ensure error generated.
///
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
///
/// Note: This scenario is irrelevant, since the 'key' attribute has been dropped.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;data&gt; element that contains a bad value for key.
///     Ensure error generated.
///
///     **/
///    public void Var060()
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
/// Note: This scenario is irrelevant, since the 'varlen' attribute has been dropped.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains one &lt;data&gt; element that contains a bad value
///     for varlen. Ensure error generated.
///
///     **/
///    public void Var061()
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
///
/// Note: This scenario is tested by other variations.
///    /**
///     Test RecordFormatDocument constructor with DocName parameter. Pass in document that
///     exists and contains 2 &lt;data&gt; element with no attributes.
///     Ensure proper nodes created.
///
///     **/
///    public void Var062()
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
     Test RecordFormatDocument constructor with DocName parameter. Pass in complex RFML document.
     Ensure proper node tree created.
     **/
    public void Var048(int runMode) {
      notApplicable("Attended variation");
      }

    /**
     Test RecordFormatDocument constructor with DocName parameter. Serialize complex RFML document from Var 62. Parse serialized document and ensure results same as non-serialized document.
    **/
    public void Var049()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        java.io.File outFile1 = null;
        java.io.File outFile2 = null;
        boolean ok = true;
        try
        {
          //
          // Serialize/deserialize a RecordFormatDocument.
          //

            RecordFormatDocument rfmlDoc1 = new RecordFormatDocument("test.rfml.qcustcdt");

            // Serialize the document.
            String docName = "Var049";
            outFile1 = new java.io.File(docName+".rfml.ser");
            rfmlDoc1.serialize(outFile1);  // TBD - Add variations to test the other serialize() methods as well.
            ///rfmlDoc1.serialize();

            Record rec1 = rfmlDoc1.toRecord("cusrec");
            Record rec1a = rfmlDoc1.toRecord("cusrec1");

            ///RecordFormatDocument rfmlDoc2 = new RecordFormatDocument("test." + outFile1.getName());
            RecordFormatDocument rfmlDoc2 = new RecordFormatDocument(docName);
            Record rec2 = rfmlDoc2.toRecord("cusrec");
            Record rec2a = rfmlDoc2.toRecord("cusrec1");

            // Verify that the new document matches the original document.
            if (RMTest.areEqual(rfmlDoc1.getDescriptor(),rfmlDoc2.getDescriptor())) {
              //succeeded();
            }
            else {
              ok = false;
              System.out.println("Deserialized document does not match original. rec1="+rec1+" rc1a="+rec1a+" rec2="+rec2+" rec2a="+rec2a);
            }


            //
            // Serialize/deserialize a ProgramCallDocument, to/from the default location.
            //

            ProgramCallDocument pcmlDoc1 = new ProgramCallDocument(systemObject_, "com.ibm.as400.resource.RUser");

            // Serialize the document.
            /*String*/ docName = "RUser";
            outFile2 = new java.io.File(docName+".pcml.ser");
            pcmlDoc1.serialize();

            ProgramCallDocument pcmlDoc2 = new ProgramCallDocument(systemObject_, docName);

            // Verify that the new document matches the original document.
            if (RMTest.areEqual(pcmlDoc1.getDescriptor(),pcmlDoc2.getDescriptor()) &&
                pcmlDoc1.getSystem().getSystemName().equals(pcmlDoc1.getSystem().getSystemName()) &&
                pcmlDoc1.getSystem().getUserId().equals(pcmlDoc1.getSystem().getUserId()))
            {}
            else {
              ok = false;
              System.out.println("Deserialized document does not match original (1,2)");
            }


            //
            // Serialize/deserialize a ProgramCallDocument, to/from a stream.
            //

            ProgramCallDocument pcmlDoc3 = new ProgramCallDocument(systemObject_, "com.ibm.as400.resource.RUser");

            // Serialize the document to an output stream.
            ByteArrayOutputStream outStream = new ByteArrayOutputStream(10000);
            /*String*/ docName = "RUser";
            ///outFile2 = new java.io.File(docName+".pcml.ser");
            pcmlDoc3.serialize(outStream);
            byte[] bytes = outStream.toByteArray();

            // Deserialize the document from an input stream.
            ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
            ProgramCallDocument pcmlDoc4 = new ProgramCallDocument(systemObject_, docName, inStream);

            // Verify that the new document matches the original document.
            if (RMTest.areEqual(pcmlDoc3.getDescriptor(),pcmlDoc4.getDescriptor()) &&
                pcmlDoc3.getSystem().getSystemName().equals(pcmlDoc4.getSystem().getSystemName()) &&
                pcmlDoc3.getSystem().getUserId().equals(pcmlDoc4.getSystem().getUserId()))
            {}
            else {
              ok = false;
              System.out.println("Deserialized document does not match original (3,4)");
            }


            if (ok) succeeded();
            else failed();
        }
        catch (Exception e)
        {
            failed(e);
        }
        finally {
          ///System.out.println ("Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};

          if (outFile1 != null) {
            try { outFile1.delete(); } catch (Exception e) {}
          }
          if (outFile2 != null) {
            try { outFile2.delete(); } catch (Exception e) {}
          }
        }
    }

    /**
     Test RecordFormatDocument constructor with DocName and Loader parms.  Pass in valid rfml document and valid class loader and ensure document parsed correctly.
    **/
    public void Var050()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }
        try
        {
            ClassLoader loader = java.lang.ClassLoader.getSystemClassLoader(); // Note: This method is new in Java2.
            RecordFormatDocument rfmlDoc1 = new RecordFormatDocument("test.rfml.qcustcdt", loader);

            ///RecordFormatDocument rfmlDoc2 = new RecordFormatDocument("test." + outFile.getName());
            RecordFormatDocument rfmlDoc2 = new RecordFormatDocument("test.rfml.qcustcdt");

            // Verify that the new document matches the original document.
            if (RMTest.areEqual(rfmlDoc1.getDescriptor(),rfmlDoc2.getDescriptor())) succeeded();
            else failed("Document does not match original.");
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: This scenario may be impossible to test, since there doesn't seem to be a way to create an "invalid" ClassLoader object.
///    /**
///     Test RecordFormatDocument constructor with DocName and Loader parms.  Pass in valid rfml document and invalid class loader and ensure correct error generated.
///    **/
///    public void Var066()
///    {
///        try
///        {
///            RecordFormatDocument rfmlDoc1 = new RecordFormatDocument("test.rfml.qcustcdt", null);
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test RecordFormatDocument constructor with DocName and Loader parms.  Pass in valid rfml document
     and null for class loader and ensure error generated
    **/
    public void Var051()
    {
        try
        {
            RecordFormatDocument rfmlDoc1 = new RecordFormatDocument("test.rfml.qcustcdt", null);
            failed("Did not throw exception."+rfmlDoc1);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

}
