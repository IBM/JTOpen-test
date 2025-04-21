///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PCConstructorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.PC;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemProperties;
import com.ibm.as400.data.Descriptor;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.PcmlSpecificationException;
import com.ibm.as400.data.ProgramCallDocument;
import com.ibm.as400.data.XmlException;

import test.RMTest;
import test.Testcase;

/**
 The PCConstructorTestcase class tests the following methods of the ProgramCalltDocument class:
 &lt;li&gt;constructors,
 &lt;li&gt;toString().
 **/
public class PCConstructorTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PCConstructorTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PCTest.main(newArgs); 
   }

  private static final boolean DEBUG = true; ///

  private String exceptionProperty_original_;

  private static boolean jdk15orLater = true;
    /**
     Constructor.
     **/
    public PCConstructorTestcase()
    {
        super();
    }

    /**
     Constructor.
     **/
    public PCConstructorTestcase(AS400 systemObject, 
                             Hashtable<String, Vector<String>> namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "PCConstructorTestcase", namesAndVars, runMode, fileOutputStream);
    }

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
      exceptionProperty_original_ = System.getProperty(SystemProperties.THROW_SAX_EXCEPTION_IF_PARSE_ERROR);
      System.out.println("DEBUG exceptionProperty_original_ == " + exceptionProperty_original_); ///
    }

    /**
     Default ProgramCallDocument constructor.  Verify that getDescriptor returns null since not set.
     **/
    public void Var001()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument();
            assertCondition((pcmlDoc.getDescriptor() == null) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     ProgramCallDocument(String) constructor. Pass null and expect an exception.
     **/
    public void Var002()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument((String)null);
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     ProgramCallDocument(String,ClassLoader) constructor. Pass null for first arg and expect an exception.
     **/
    public void Var003()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument((String)null, ClassLoader.getSystemClassLoader());
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in name that
     doesn't exist. Exception should be generated.
     **/
    public void Var004()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("bogus");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists but is empty. Exception should be generated.
     **/
    public void Var005()
    {
	// Does not work in JDK1.4
	if (!jdk15orLater) {
	    notApplicable("Works only on jdk1.6 and later");
	    return; 
	} 
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.empty0");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (XmlException e)
        {
          if (DEBUG) e.printStackTrace();
          Exception e1 = e.getException();
          ///if (DEBUG) {System.out.println("Embedded exception:"); e1.printStackTrace(); }
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only &lt;pcml&gt; element with bad version "1.0" and an empty &lt;program&gt; element. Verify exception thrown for the bad version.
     **/
    public void Var006()
    {
	if (!jdk15orLater) {
	    notApplicable("Works only on jdk1.6 and later");
	    return; 
	} 

        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.versionBad");
            Descriptor descriptor = pcmlDoc.getDescriptor();
            String[] attrs = descriptor.getAttributeList();
            System.out.println("Attributes:");
            for (int i=0; i<attrs.length; i++)
            {
              System.out.println(attrs[i] + ": " + descriptor.getAttributeValue(attrs[i]));
            }
            ///pcmlDoc.serialize();
            failed("Did not throw exception.");
        }
        catch (XmlException e)
        {
          if (DEBUG) e.printStackTrace();
          Exception e1 = e.getException();
          if (DEBUG) {System.out.println("Embedded exception:"); e1.printStackTrace(); }
          ///String expectedMsg = "Attribute \"version\" with value \"1.0\" must have a value of \"4.0\".";
          String expectedMsg = "Attribute \"version\" with value \"99.0\" must have a value from the list \"1.0 2.0 3.0 4.0 ";
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
     exists and contains only &lt;pcml&gt; element with good version "4.0". Verify an error is
     generated because no struct or program in document.
     **/
    public void Var007()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.empty");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
     exists and contains only &lt;pcml&gt; element with good version "4.0" and an empty
     &lt;program&gt; element. Verify the pcml and program nodes are properly
     created.
     **/
    public void Var008()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.empty1");
            // Check that the node tree has exactly one node: a <program> element with no children.
            // For convenience, get the children into a Vector.
            Descriptor descriptor = pcmlDoc.getDescriptor();
            Enumeration<?> enum1 = descriptor.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              children.addElement(child);
            }
            Descriptor firstChild = (Descriptor)(children.elementAt(0));

            assertCondition(children.size() == 1 &&
                   firstChild.getTagName().equals("program") &&
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
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.zonedNoInit");
///            Record rec = pcmlDoc.toRecord("format1");
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }


    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
     contains &lt;pcml&gt; and &lt;program&gt;, where the program element is missing the required attribute "name". Verify exception thrown for the missing "name" attribute.
     **/
    public void Var009()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.empty1NoName");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document
     has &lt;pcml&gt; and &lt;program&gt; elements only with proper attributes.  Ensure proper nodes created after parsing.
     **/
    public void Var010()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.empty1");
            // Check that the node tree has exactly one node: a program element with no children.
            // For convenience, get the children into a Vector.
            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              children.addElement(child);
            }
            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                child1NumAttrs++;
                if (DEBUG) System.out.println(child1AttList[i] + ": " + attrVal);
              }
            }
// Debug only:
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
//
 
            assertCondition(
                   children.size() == 1 &&
                   child1.getTagName().equals("program") &&
                   child1.hasChildren() == false &&
                   //child1NumAttrs == 1 &&
                   child1NumAttrs == 3 &&  // name, path, epccsid
                   child1.getAttributeValue("name").equals("prog1") &&
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
///     contains only &lt;pcml&gt; and &lt;program&gt; elements and pass in bad description (&gt;50)
///     attribute for &lt;program&gt;. Ensure exception thrown.
///     **/
///    public void Var011()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("empty1LongDesc");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
///        }
///    }


    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
     contains only a &lt;struct&gt; element. Verify exception thrown for the missing &lt;pcml&gt; tag.
     **/
    public void Var011()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.emptyNoPcml");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document
     that contains only 2 &lt;program&gt; elements both with the same name. Ensure an exception is thrown.
     **/
    public void Var012()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.twoRecFmtsSameName");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is tested by Var010.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
///     contains only a &lt;program&gt; entry that has a name only and no description.
///     Ensure node properly built and description attribute is null.
///     **/
///    public void Var014()
///    {
///    }



    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document
     that contains only 2 &lt;program&gt; elements each with a different name. Ensure 2 unique nodes are created.
     **/
    public void Var013()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.twoRecFmts");
            // Check that the node tree has exactly one node: a program element with no children.
            // For convenience, get the children into a Vector.
            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
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

            if (DEBUG)
            {
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
            }
 
            assertCondition(
                   children.size() == 2 &&
                   child1.getTagName().equals("program") &&
                   child1.hasChildren() == false &&
                   child1NumAttrs == 3 &&
                   child1.getAttributeValue("name").equals("prog1") &&
                   child1.getAttributeValue("description") == null &&
                   child2.getTagName().equals("program") &&
                   child2.hasChildren() == false &&
                   child2NumAttrs == 3 &&
                   child2.getAttributeValue("name").equals("prog2") &&
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document
///     that contains only 2 &lt;program&gt; elements each with no attributes. Ensure
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document
     that contains only 1 &lt;program&gt; element containing a &lt;data&gt; element with redirected "length", "count", and "ccsid" attributes.
     **/
    public void Var014()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.indirectLengthAndCount");
            // Check that the node tree has exactly one child node: a program element with 4 children.
            // For convenience, get the children into a Vector.
            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) child1NumAttrs++;
            }

            enum1 = child1.getChildren();
            Vector<Descriptor> child1children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              child1children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                if (DEBUG) System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
                child1Child1NumAttrs++;
              }
            }

            pcmlDoc.setValue("prog1.field1", new int[] {0}, "first1");
            pcmlDoc.setValue("prog1.field1", new int[] {1}, "second");
            pcmlDoc.setValue("prog1.field1", new int[] {2}, "third1");
/*
            System.out.println("prog1.field1[0]: " + (String)pcmlDoc.getValue("prog1.field1", new int[] {0}));
            System.out.println("prog1.field1[1]: " + (String)pcmlDoc.getValue("prog1.field1", new int[] {1}));
            System.out.println("prog1.field1[2]: " + (String)pcmlDoc.getValue("prog1.field1", new int[] {2}));
*/
///            byte[] received = pcmlDoc.toByteArray("prog1");
///
///            int[] expectedVals = new int[] {
///              0x86, 0x89, 0x99, 0xA2, 0xA3, 0xF1,     // "first1"
///              0xA2, 0x85, 0x83, 0x96, 0x95, 0x84,     // "second"
///              0xA3, 0x88, 0x89, 0x99, 0x84, 0xF1,     // "third1"
///              0xC1, 0x40, 0x40, 0x40, 0x40, 0x40,     // "A     "
///              0xC1, 0x40, 0x40, 0x40, 0x40, 0x40,     // "A     "
///              0x00, 0x06,                             // lengthOfField1 == 6
///              0x00, 0x05,                             // countOfField1  == 5
///              0x00, 0x25                              // ccsidOfField1  == 37 (0x25)
///            };
///            byte[] expected = createByteArray(expectedVals);

            if (DEBUG)
            {
              ///System.out.println("Expected:");
              ///printByteArray(expected);
              ///System.out.println("Received:");
              ///printByteArray(received);

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

                                 pcmlDoc.getValue("prog1.field1", new int[] {0}) + ", " +
                                 pcmlDoc.getValue("prog1.field1", new int[] {1}) + ", " +
                                 pcmlDoc.getValue("prog1.field1", new int[] {2}) + ", " +

                                 rootNumAttrs + ", " +
                                 root.getAttributeValue("version"));
            }
 
            assertCondition(
                   children.size() == 1 &&
                   child1.getTagName().equals("program") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 3 &&
                   child1.getAttributeValue("name").equals("prog1") &&
                   child1.getAttributeValue("description") == null &&

                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 7 &&  // name, type, length, count, ccsid, init, passby
                   child1Child1.getAttributeValue("type").equals("char") &&
                   child1Child1.getAttributeValue("name").equals("field1") &&
                   child1Child1.getAttributeValue("length").equals("lengthOfField1") &&
                   child1Child1.getAttributeValue("count").equals("countOfField1") &&
                   child1Child1.getAttributeValue("ccsid").equals("ccsidOfField1") &&

                   pcmlDoc.getValue("prog1.field1", new int[] {0}).equals("first1") &&
                   pcmlDoc.getValue("prog1.field1", new int[] {1}).equals("second") &&
                   pcmlDoc.getValue("prog1.field1", new int[] {2}).equals("third1") &&

                   rootNumAttrs == 1 &&
                   root.getAttributeValue("version").equals("4.0") /* &&

                   areEqual(expected, received)*/ );
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document
     that contains two empty &lt;pcml&gt; elements, and nothing else.
     Ensure proper error thrown.
     **/
    public void Var015()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.twoPcmls");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document
     that starts with a tag other than <pcml>.
     Ensure proper error thrown.
     **/
    public void Var016()
    {
	if (!jdk15orLater) {
	    notApplicable("Works only on jdk1.6 and later");
	    return; 
	} 

        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.badFirstTag");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists but contains a bad SYSTEM reference, i.e.,
///     &lt;!DOCTYPE pcml SYSTEM "pcml_bad.dtd"&gt;.
///     Ensure proper error thrown
///     **/
///    public void Var016()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.systemBad");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          if (DEBUG) e.printStackTrace();
///          ///String expectedMsg = "Exception 'java.io.FileNotFoundException' received.";
///          String expectedMsg = "java.io.FileNotFoundException";
///          String receivedMsg = e.getMessage();
///          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(receivedMsg != null &&
///                 receivedMsg.indexOf(expectedMsg) != -1 &&
///                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
///        }
///    }
///
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists but contains a pcml SYSTEM reference, i.e.,
///     &lt;!DOCTYPE pcml SYSTEM "pcml.dtd"&gt;.
///     Ensure proper error thrown
///     **/
///    public void Var017()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.systemPcml");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          if (DEBUG) e.printStackTrace();
///          ///String expectedMsg = "Exception 'java.io.FileNotFoundException' received.";
///          String expectedMsg = "java.io.FileNotFoundException";
///          String receivedMsg = e.getMessage();
///          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(receivedMsg != null &&
///                 receivedMsg.indexOf(expectedMsg) != -1 &&
///                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
///        }
///    }
///
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists but contains a blank SYSTEM reference.
///     Ensure proper error thrown.
///     **/
///    public void Var018()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.systemBlank");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          if (DEBUG) e.printStackTrace();
///          ///String expectedMsg = "Exception 'java.net.MalformedURLException' received.";
///          String expectedMsg1 = "java.net.MalformedURLException";   // This is expected with JDK 1.4 or earlier
///          String expectedMsg2 = "A parse error occurred";           // This is expected with JDK 1.5 or later
///          String receivedMsg = e.getMessage();
///          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(receivedMsg != null &&
///                 ((receivedMsg.indexOf(expectedMsg1) != -1) || (receivedMsg.indexOf(expectedMsg2) != -1)) &&
///                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
///        }
///    }
///
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists but does not contain &lt;?xml version=1.0 encoding="UTF-8" standalone="no"?&gt; line and see what happens.
///     **/
///    public void Var019()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.xmlNoVers");
///            notApplicable("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          String expectedMsg = "A parse error occurred";
///          String receivedMsg = e.getMessage();
///          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(receivedMsg != null &&
///                 receivedMsg.indexOf(expectedMsg) != -1 &&
///                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
///        }
///    }


    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;program&gt; element but has no matching end element tag.
     Ensure proper error thrown.
     **/
    public void Var017()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.recFmtNoEndTag");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;struct&gt; element but has no matching end element tag.
     Ensure proper error thrown.
     **/
    public void Var018()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structNoEndTag");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;struct&gt; element with only the minimally required elements and attributes.
     Ensure proper node created.

     **/
    public void Var019()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.struct0");
            // Check that the root node has exactly one child: a struct element with exactly one child (a data element).
            // For convenience, get the children into a Vector.
            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                if (DEBUG) System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enum1 = child1.getChildren();
            Vector<Descriptor> grandChildren = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              grandChildren.addElement(child);
            }

            Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));
            String[] grandChild1AttList = grandChild1.getAttributeList();

            int grandChild1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild1AttList.length; i++) {
              String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
              if (attrVal != null) {
                if (DEBUG) System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
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
                                 grandChild1.getAttributeValue("passby") + ", " +
                                 rootNumAttrs + ", " +
                                 root.getAttributeValue("version"));
            }

 
            assertCondition(
                   children.size() == 1 &&
                   grandChildren.size() == 1 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 2 &&  // name, usage
                   child1.getAttributeValue("name").equals("struct1") &&
                   grandChild1.getTagName().equals("data") &&
                   grandChild1.hasChildren() == false &&
                   grandChild1NumAttrs == 4 && // name, type, length, passby
                   grandChild1.getAttributeValue("type").equals("char") &&
                   grandChild1.getAttributeValue("name").equals("field1") &&
                   grandChild1.getAttributeValue("length").equals("6") &&
                   grandChild1.getAttributeValue("passby").equals("reference") &&
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only one empty &lt;struct&gt; element.
     Ensure error is thrown since a struct must contain one or more data or struct nodes.

     **/
    public void Var020()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structEmpty");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
     exists and contains 2 &lt;struct&gt; elements with same name.
     Ensure error is thrown.
     **/
    public void Var021()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.twoStructsSameName");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;struct&gt; element that contains 3 valid &lt;data&gt; nodes.  Validate the attributes of the struct.
     Ensure data nodes just have a name and type.  Ensure proper node tree is built.

     **/
    public void Var022()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structWith3Datas");
            // Check that the root node has exactly one child: a struct element with exactly 3 children (data elements).
            // For convenience, get the children into a Vector.
            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
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

            enum1 = child1.getChildren();
            Vector<Descriptor> grandChildren = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
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
                   child1NumAttrs == 2 &&  // name, usage
                   child1.getAttributeValue("name").equals("struct1") &&

                   grandChild1.getTagName().equals("data") &&
                   grandChild1.hasChildren() == false &&
                   grandChild1NumAttrs == 4 && // name, type, length, passby
                   grandChild1.getAttributeValue("type").equals("char") &&
                   grandChild1.getAttributeValue("name").equals("field1") &&

                   grandChild2.getTagName().equals("data") &&
                   grandChild2.hasChildren() == false &&
                   grandChild2NumAttrs == 4 && // name, type, length, passby
                   grandChild2.getAttributeValue("type").equals("byte") &&
                   grandChild2.getAttributeValue("name").equals("field2") &&

                   grandChild3.getTagName().equals("data") &&
                   grandChild3.hasChildren() == false &&
                   grandChild3NumAttrs == 4 && // name, type, length, passby
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


// This variation is not valid for PCML:
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains a valid &lt;struct&gt; element that contains another &lt;struct&gt; element.
///     Ensure proper error thrown.
///
///     **/
///    public void Var026()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structInStruct");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          String expectedMsg = "A parse error occurred";
///          String receivedMsg = e.getMessage();
///          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(receivedMsg != null &&
///                 receivedMsg.indexOf(expectedMsg) != -1 &&
///                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
///        }
///    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains an array of &lt;struct&gt; elements, i.e., count > 1.
     Ensure proper node tree is generated.

     **/
    public void Var023()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structArray");

            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
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

            enum1 = child1.getChildren();
            Vector<Descriptor> child1children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
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
                if (DEBUG) System.out.println("child2Attr: " + child2AttList[i] + " = " + attrVal);
                child2NumAttrs++;
              }
            }

            enum1 = child2.getChildren();
            Vector<Descriptor> child2children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              child2children.addElement(child);
            }

            Descriptor child2Child1 = (Descriptor)(child2children.elementAt(0));
            String[] child2Child1AttList = child2Child1.getAttributeList();

            int child2Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child2Child1AttList.length; i++) {
              String attrVal = child2Child1.getAttributeValue(child2Child1AttList[i]);
              if (attrVal != null) {
                if (DEBUG) System.out.println("child2Child1Attr: " + child2Child1AttList[i] + " = " + attrVal);
                child2Child1NumAttrs++;
              }
            }

            if (DEBUG)
            {
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
            }
 
            assertCondition(
                   children.size() == 2 &&

                   child1children.size() == 1 &&
                   child1.getTagName().equals("struct") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 2 &&  // name, usage
                   child1.getAttributeValue("name").equals("struct1") &&
                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 4 &&  // name, type, length, passby
                   child1Child1.getAttributeValue("type").equals("char") &&
                   child1Child1.getAttributeValue("name").equals("field1") &&
                   child1Child1.getAttributeValue("length").equals("1") &&

                   child2children.size() == 1 &&
                   child2.getTagName().equals("program") &&
                   child2.hasChildren() == true &&
                   child2NumAttrs == 3 &&  // name, path, epccsid
                   child2.getAttributeValue("name").equals("prog1") &&
                   child2Child1.getTagName().equals("data") &&
                   child2Child1.hasChildren() == true && // it points to a struct with children
                   child2Child1NumAttrs == 5 &&  // name, type, struct, count, passby
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

// This scenario is adequately tested in the RFML tests.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains a two dimensional array of &lt;struct&gt; elements.
///     Ensure proper node tree is generated.
///
///     **/
///    public void Var028()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structArray2Dim");
///
///            Descriptor root = pcmlDoc.getDescriptor();
///            String[] rootAttList = root.getAttributeList();
///            int rootNumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<rootAttList.length; i++) {
///              String attrVal = root.getAttributeValue(rootAttList[i]);
///              if (attrVal != null) rootNumAttrs++;
///            }
///
///            Enumeration enum1 = root.getChildren();
///            Vector children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              children.addElement(child);
///            }
///
///            Descriptor child1 = (Descriptor)(children.elementAt(0));
///            String[] child1AttList = child1.getAttributeList();
///
///            int child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child1AttList.length; i++) {
///              String attrVal = child1.getAttributeValue(child1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
///                child1NumAttrs++;
///              }
///            }
///
///            enum1 = child1.getChildren();
///            Vector child1children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child1children.addElement(child);
///            }
///
///            Descriptor child1Child1 = (Descriptor)(child1children.elementAt(0));
///            String[] child1Child1AttList = child1Child1.getAttributeList();
///
///            int child1Child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child1Child1AttList.length; i++) {
///              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
///                child1Child1NumAttrs++;
///              }
///            }
///
///            Descriptor child2 = (Descriptor)(children.elementAt(1));
///            String[] child2AttList = child2.getAttributeList();
///
///            int child2NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child2AttList.length; i++) {
///              String attrVal = child2.getAttributeValue(child2AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child2Attr: " + child2AttList[i] + " = " + attrVal);
///                child2NumAttrs++;
///              }
///            }
///
///            enum1 = child2.getChildren();
///            Vector child2children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child2children.addElement(child);
///            }
///
///            Descriptor child2Child1 = (Descriptor)(child2children.elementAt(0));
///            String[] child2Child1AttList = child2Child1.getAttributeList();
///
///            int child2Child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child2Child1AttList.length; i++) {
///              String attrVal = child2Child1.getAttributeValue(child2Child1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child2Child1Attr: " + child2Child1AttList[i] + " = " + attrVal);
///                child2Child1NumAttrs++;
///              }
///            }
///
///            Descriptor child3 = (Descriptor)(children.elementAt(2));
///            String[] child3AttList = child3.getAttributeList();
///
///            int child3NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child3AttList.length; i++) {
///              String attrVal = child3.getAttributeValue(child3AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child3Attr: " + child3AttList[i] + " = " + attrVal);
///                child3NumAttrs++;
///              }
///            }
///
///            enum1 = child3.getChildren();
///            Vector child3children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child3children.addElement(child);
///            }
///
///            Descriptor child3Child1 = (Descriptor)(child3children.elementAt(0));
///            String[] child3Child1AttList = child3Child1.getAttributeList();
///
///            int child3Child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child3Child1AttList.length; i++) {
///              String attrVal = child3Child1.getAttributeValue(child3Child1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child3Child1Attr: " + child3Child1AttList[i] + " = " + attrVal);
///                child3Child1NumAttrs++;
///              }
///            }
///
///            enum1 = child3Child1.getChildren();
///            Vector child3Child1children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child3Child1children.addElement(child);
///            }
///
///            Descriptor child3Child1Child1 = (Descriptor)(child3Child1children.elementAt(0));
///            String[] child3Child1Child1AttList = child3Child1Child1.getAttributeList();
///
///            int child3Child1Child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child3Child1Child1AttList.length; i++) {
///              String attrVal = child3Child1Child1.getAttributeValue(child3Child1Child1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child3Child1Child1Attr: " + child3Child1Child1AttList[i] + " = " + attrVal);
///                child3Child1Child1NumAttrs++;
///              }
///            }
///
////* Debug only:
///            System.out.println();
///            System.out.println(
///                   children.size() + ", " +
///
///                   );
///*/
/// 
///            assertCondition(
///                   children.size() == 3 &&
///
///                   child1children.size() == 1 &&
///                   child1.getTagName().equals("struct") &&
///                   child1.hasChildren() == true &&
///                   child1NumAttrs == 1 &&  // name
///                   child1.getAttributeValue("name").equals("struct1") &&
///                   child1Child1.getTagName().equals("data") &&
///                   child1Child1.hasChildren() == true &&
///                   child1Child1NumAttrs == 4 &&  // name, type, length, count
///                   child1Child1.getAttributeValue("type").equals("struct") &&
///                   child1Child1.getAttributeValue("struct").equals("struct2") &&
///                   child1Child1.getAttributeValue("count").equals("3") &&
///
///                   child2children.size() == 1 &&
///                   child2.getTagName().equals("struct") &&
///                   child2.hasChildren() == true &&
///                   child2NumAttrs == 1 &&  // name
///                   child2.getAttributeValue("name").equals("struct2") &&
///                   child2Child1.getTagName().equals("data") &&
///                   child2Child1.hasChildren() == false &&
///                   child2Child1NumAttrs == 3 &&  // name, type, length
///                   child2Child1.getAttributeValue("type").equals("char") &&
///                   child2Child1.getAttributeValue("length").equals("1") &&
///                   child2Child1.getAttributeValue("count") == null &&
///
///                   child3children.size() == 1 &&
///                   child3.getTagName().equals("program") &&
///                   child3.hasChildren() == true &&
///                   child3NumAttrs == 1 &&  // name
///                   child3.getAttributeValue("name").equals("prog1") &&
///                   child3Child1.getTagName().equals("data") &&
///                   child3Child1.hasChildren() == true && // it points to a struct with children
///                   child3Child1NumAttrs == 4 &&  // name, type, struct, count
///                   child3Child1.getAttributeValue("type").equals("struct") &&
///                   child3Child1.getAttributeValue("struct").equals("struct1") &&
///                   child3Child1.getAttributeValue("count").equals("2") &&
///
///                   rootNumAttrs == 1 &&
///                   root.getAttributeValue("version").equals("4.0"));
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }

/// Note - This scenario is overkill.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;struct&gt; element that contains a negative value for count.
     Ensure error generated.

     **/
    public void Var024()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structBadCount");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains a &lt;data&gt; element of type "struct" that refers to a nonexistent struct.
     Ensure error generated.

     **/
    public void Var025()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structBadRef");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

// This variation is not relevant to PCML, which tolerates nested structures.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains a &lt;data&gt; element of type "struct" that doesn't also have a 'struct' attribute.
///     Ensure error generated.
///
///     **/
///    public void Var031()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structNoRef");
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          ///String expectedMsg = "A parse error occurred";
///          String expectedMsg = "A PCML specification error occurred.";  //TBD - fix the MRI.
///          String receivedMsg = e.getMessage();
///          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(receivedMsg != null &&
///                 receivedMsg.indexOf(expectedMsg) != -1 &&
///                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
///        }
///    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains a &lt;data&gt; element of type "struct" that also specifies a value for the 'length' attribute.
     Ensure error generated.

     **/
    public void Var026()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataStructWithLength");
            failed("Did not throw exception."+pcmlDoc);
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

/// Note: This scenario is irrelevant, since PCML doesn't support an "offset" attribute for <struct>.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
/// Note: This scenario is irrelevant, since PCML doesn't support an "offsetfrom" attribute for <struct>.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
/// Note: This scenario is irrelevant, since PCML doesn't support an "outputsize" attribute for <struct>.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
/// Note: This scenario is irrelevant, since PCML requires a "name" attribute for each struct.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;data&gt; element with minimal attributes.
     Ensure error generated because doc must contain a struct or a program element.

     **/
    public void Var027()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataOnly");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains only one &lt;data&gt; element with no attributes and one &lt;program&gt; element.
     Ensure proper node created.

     **/
    public void Var028()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.recFmt1");
            // Check that the root node has exactly one child: a program element with exactly one child (a data element).
            // For convenience, get the children into a Vector.
            Descriptor root = pcmlDoc.getDescriptor();
            String[] rootAttList = root.getAttributeList();
            int rootNumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<rootAttList.length; i++) {
              String attrVal = root.getAttributeValue(rootAttList[i]);
              if (attrVal != null) rootNumAttrs++;
            }

            Enumeration<?> enum1 = root.getChildren();
            Vector<Descriptor> children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              children.addElement(child);
            }

            Descriptor child1 = (Descriptor)(children.elementAt(0));
            String[] child1AttList = child1.getAttributeList();

            int child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1AttList.length; i++) {
              String attrVal = child1.getAttributeValue(child1AttList[i]);
              if (attrVal != null) {
                if (DEBUG) System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
                child1NumAttrs++;
              }
            }

            enum1 = child1.getChildren();
            Vector<Descriptor> child1Children = new Vector<Descriptor>();
            while (enum1.hasMoreElements()) {
              Descriptor child = (Descriptor)(enum1.nextElement());
              child1Children.addElement(child);
            }

            Descriptor child1Child1 = (Descriptor)(child1Children.elementAt(0));
            String[] child1Child1AttList = child1Child1.getAttributeList();

            int child1Child1NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<child1Child1AttList.length; i++) {
              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
              if (attrVal != null) {
                if (DEBUG) System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
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
                   child1.getTagName().equals("program") &&
                   child1.hasChildren() == true &&
                   child1NumAttrs == 3 &&  // name, path, epccsid
                   child1.getAttributeValue("name").equals("prog1") &&
                   child1Child1.getTagName().equals("data") &&
                   child1Child1.hasChildren() == false &&
                   child1Child1NumAttrs == 5 &&  // name, type, length, init, passby
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


/// This is not a valid scenario.  All data elements must be inside a program.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
///     exists and contains only one empty &lt;data&gt; element with a &lt;struct&gt; element.
///     Ensure a proper empty node is created.
///
///     **/
///    public void Var039()
///    {
///    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains 2 &lt;data&gt; elements with same name.
     Ensure error is thrown.
     **/
    public void Var029()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.twoDataSameName");
            ///AS400 sys = new AS400();
            ///ProgramCallDocument pcmlDoc = new ProgramCallDocument(sys, "test.pcml.twoDataSameName");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          System.out.println("Received an exception of type " + e.getClass().getName()); ///
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          Exception cause = null;
          if (e instanceof PcmlException)
          {
            cause = ((PcmlException)e).getException();
            if (cause != null) {
              System.out.println("Original exception: " + cause.getClass().getName() + ": " + cause.getMessage());
              if (cause instanceof PcmlSpecificationException)
              {
                PcmlSpecificationException pse = (PcmlSpecificationException)cause;
                String[] msgs = pse.getMessages();
                System.out.println("Messages in PcmlSpecificationException: ");
                for (int i=0; i<msgs.length; i++) {
                  System.out.println(msgs[i]);
                }
              }
            }
          }

          assertCondition(e instanceof PcmlException &&
                 cause instanceof PcmlSpecificationException &&
                 receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    // This scenario is adequately tested by the RFML tests.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;struct&gt; element that contains 3 valid &lt;data&gt; nodes.  Ensure &lt;data&gt; has all attributes.
///     Ensure proper node tree is built with proper attribute values for each node.
///
///     **/
///    public void Var036()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.structDataAttrs");
///            // Check that the root node has exactly one child: a struct element with exactly 3 children (data elements).
///            // For convenience, get the children into a Vector.
///            Descriptor root = pcmlDoc.getDescriptor();
///            String[] rootAttList = root.getAttributeList();
///            int rootNumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<rootAttList.length; i++) {
///              String attrVal = root.getAttributeValue(rootAttList[i]);
///              if (attrVal != null) rootNumAttrs++;
///            }
///
///            Enumeration enum1 = root.getChildren();
///            Vector children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              children.addElement(child);
///            }
///
///            Descriptor child1 = (Descriptor)(children.elementAt(0));
///            String[] child1AttList = child1.getAttributeList();
///
///            int child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child1AttList.length; i++) {
///              String attrVal = child1.getAttributeValue(child1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
///                child1NumAttrs++;
///              }
///            }
///
///            enum1 = child1.getChildren();
///            Vector grandChildren = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              grandChildren.addElement(child);
///            }
///
///            Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));
///            String[] grandChild1AttList = grandChild1.getAttributeList();
///            int grandChild1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<grandChild1AttList.length; i++) {
///              String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
///                grandChild1NumAttrs++;
///              }
///            }
///
///            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));
///            String[] grandChild2AttList = grandChild2.getAttributeList();
///            int grandChild2NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<grandChild2AttList.length; i++) {
///              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
///                grandChild2NumAttrs++;
///              }
///            }
///
///            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));
///            String[] grandChild3AttList = grandChild3.getAttributeList();
///            int grandChild3NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<grandChild3AttList.length; i++) {
///              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
///              if (attrVal != null) {
///                ///System.out.println("grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
///                grandChild3NumAttrs++;
///              }
///            }
///
////* Debug only: 
///            System.out.println();
///            System.out.println(
///                   children.size() + ", " +
///                   grandChildren.size() + ", " +
///                   child1.getTagName() + ", " +
///                   child1.hasChildren() + ", " +
///                   child1NumAttrs + ", " +
///                   child1.getAttributeValue("name") + ", " +
///
///                   grandChild1.getTagName() + ", " +
///                   grandChild1.hasChildren() + ", " +
///                   grandChild1NumAttrs + ", " +
///                   grandChild1.getAttributeValue("type") + ", " +
///                   grandChild1.getAttributeValue("name") + ", " +
///                   grandChild1.getAttributeValue("count") + ", " +
///                   grandChild1.getAttributeValue("length") + ", " +
///                   grandChild1.getAttributeValue("ccsid") + ", " +
///                   grandChild1.getAttributeValue("init") + ", " +
///                   grandChild1.getAttributeValue("bidistringtype") + ", " +
///
///                   grandChild2.getTagName() + ", " +
///                   grandChild2.hasChildren() + ", " +
///                   grandChild2NumAttrs + ", " +
///                   grandChild2.getAttributeValue("type") + ", " +
///                   grandChild2.getAttributeValue("name") + ", " +
///                   grandChild2.getAttributeValue("count") + ", " +
///                   grandChild2.getAttributeValue("length") + ", " +
///                   grandChild2.getAttributeValue("ccsid") + ", " +
///                   grandChild2.getAttributeValue("init") + ", " +
///                   grandChild2.getAttributeValue("bidistringtype") + ", " +
///                   grandChild2.getAttributeValue("precision") + ", " +
///
///                   grandChild3.getTagName() + ", " +
///                   grandChild3.hasChildren() + ", " +
///                   grandChild3NumAttrs + ", " +
///                   grandChild3.getAttributeValue("type") + ", " +
///                   grandChild3.getAttributeValue("name") + ", " +
///                   grandChild3.getAttributeValue("count") + ", " +
///                   grandChild3.getAttributeValue("length") + ", " +
///                   grandChild3.getAttributeValue("ccsid") + ", " +
///                   grandChild3.getAttributeValue("init") + ", " +
///                   grandChild3.getAttributeValue("precision") + ", " +
///                   grandChild3.getAttributeValue("bidistringtype") + ", " +
///
///                   rootNumAttrs + ", " +
///                   root.getAttributeValue("version")
///                   );
///*/
/// 
///            assertCondition(
///                   children.size() == 1 &&
///                   grandChildren.size() == 3 &&
///                   child1.getTagName().equals("struct") &&
///                   child1.hasChildren() == true &&
///                   child1NumAttrs == 1 && // name
///                   child1.getAttributeValue("name").equals("struct1") &&
///
///                   grandChild1.getTagName().equals("data") &&
///                   grandChild1.hasChildren() == false &&
///                   grandChild1NumAttrs == 7 &&
///                   grandChild1.getAttributeValue("type").equals("char") &&
///                   grandChild1.getAttributeValue("name").equals("field1") &&
///                   grandChild1.getAttributeValue("count").equals("1") &&
///                   grandChild1.getAttributeValue("length").equals("5") &&
///                   grandChild1.getAttributeValue("ccsid").equals("819") &&
///                   grandChild1.getAttributeValue("init").equals("ABC") &&
///                   grandChild1.getAttributeValue("bidistringtype").equals("ST4") &&
///
///                   grandChild2.getTagName().equals("data") &&
///                   grandChild2.hasChildren() == false &&
///                   grandChild2NumAttrs == 5 &&
///                   grandChild2.getAttributeValue("type").equals("byte") &&
///                   grandChild2.getAttributeValue("name").equals("field2") &&
///                   grandChild2.getAttributeValue("count").equals("2") &&
///                   grandChild2.getAttributeValue("length").equals("6") &&
///                   grandChild2.getAttributeValue("ccsid") == null &&
///                   grandChild2.getAttributeValue("init").equals("1") &&
///                   grandChild2.getAttributeValue("bidistringtype") == null &&
///                   grandChild2.getAttributeValue("precision") == null &&
///
///                   grandChild3.getTagName().equals("data") &&
///                   grandChild3.hasChildren() == false &&
///                   grandChild3NumAttrs == 5 &&
///                   grandChild3.getAttributeValue("type").equals("int") &&
///                   grandChild3.getAttributeValue("name").equals("field3") &&
///                   grandChild3.getAttributeValue("count") == null &&
///                   grandChild3.getAttributeValue("length").equals("2") &&
///                   grandChild3.getAttributeValue("ccsid") == null &&
///                   grandChild3.getAttributeValue("init").equals("3") &&
///                   grandChild3.getAttributeValue("precision").equals("16") &&
///                   grandChild3.getAttributeValue("bidistringtype") == null &&
///
///                   rootNumAttrs == 1 &&
///                   root.getAttributeValue("version").equals("4.0"));
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }

    // This scenario is adequately tested by the RFML tests.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;program&gt; element that contains 1 valid &lt;data&gt; nodes.
///     Ensure the &lt;data&gt; has type struct and struct attribute.
///     Ensure proper node tree is built.
///
///     **/
///    public void Var037()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.recFmtWithStruct");
///            // Check that the root node has exactly 2 children: a struct with a <data> child, and a program with a <data> child.
///            // For convenience, get the children into a Vector.
///            Descriptor root = pcmlDoc.getDescriptor();
///            String[] rootAttList = root.getAttributeList();
///            int rootNumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<rootAttList.length; i++) {
///              String attrVal = root.getAttributeValue(rootAttList[i]);
///              if (attrVal != null) rootNumAttrs++;
///            }
///
///            Enumeration enum1 = root.getChildren();
///            Vector children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              children.addElement(child);
///            }
///
///            Descriptor child1 = (Descriptor)(children.elementAt(0));
///            String[] child1AttList = child1.getAttributeList();
///
///            int child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child1AttList.length; i++) {
///              String attrVal = child1.getAttributeValue(child1AttList[i]);
///              if (attrVal != null) {
///                if (DEBUG) System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
///                child1NumAttrs++;
///              }
///            }
///
///            Descriptor child2 = (Descriptor)(children.elementAt(1));
///            String[] child2AttList = child2.getAttributeList();
///
///            int child2NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child2AttList.length; i++) {
///              String attrVal = child2.getAttributeValue(child2AttList[i]);
///              if (attrVal != null) {
///                if (DEBUG) System.out.println("child2Attr: " + child2AttList[i] + " = " + attrVal);
///                child2NumAttrs++;
///              }
///            }
///
///            enum1 = child1.getChildren();
///            Vector child1Children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child1Children.addElement(child);
///            }
///
///            Descriptor child1Child1 = (Descriptor)(child1Children.elementAt(0));
///            String[] child1Child1AttList = child1Child1.getAttributeList();
///
///            int child1Child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child1Child1AttList.length; i++) {
///              String attrVal = child1Child1.getAttributeValue(child1Child1AttList[i]);
///              if (attrVal != null) {
///                if (DEBUG) System.out.println("child1Child1Attr: " + child1Child1AttList[i] + " = " + attrVal);
///                child1Child1NumAttrs++;
///              }
///            }
///
///            enum1 = child2.getChildren();
///            Vector child2Children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child2Children.addElement(child);
///            }
///
///            Descriptor child2Child1 = (Descriptor)(child2Children.elementAt(0));
///            String[] child2Child1AttList = child2Child1.getAttributeList();
///
///            int child2Child1NumAttrs = 0;  // number of non-null attributes
///            for (int i=0; i<child2Child1AttList.length; i++) {
///              String attrVal = child2Child1.getAttributeValue(child2Child1AttList[i]);
///              if (attrVal != null) {
///                if (DEBUG) System.out.println("child2Child1Attr: " + child2Child1AttList[i] + " = " + attrVal);
///                child2Child1NumAttrs++;
///              }
///            }
///
///            enum1 = child2Child1.getChildren();
///            Vector child2Child1Children = new Vector();
///            while (enum1.hasMoreElements()) {
///              Descriptor child = (Descriptor)(enum1.nextElement());
///              child2Child1Children.addElement(child);
///            }
///            Descriptor child2Child1Child1 = (Descriptor)(child2Child1Children.elementAt(0));
/////            System.out.println("child2Child1Child1: " + child2Child1Child1.getAttributeValue("name"));
///
///            if (DEBUG)
///            {
///              System.out.println();
///              System.out.println(
///                                 children.size() + ", " +
///
///                                 child1Children.size() + ", " +
///                                 child1.getTagName() + ", " +
///                                 child1.hasChildren() + ", " +
///                                 child1NumAttrs + ", " +
///                                 child1.getAttributeValue("name") + ", " +
///                                 child1Child1.getTagName() + ", " +
///                                 child1Child1.hasChildren() + ", " +
///                                 child1Child1NumAttrs + ", " +
///                                 child1Child1.getAttributeValue("name") + ", " +
///                                 child1Child1.getAttributeValue("type") + ", " +
///
///                                 child2Children.size() + ", " +
///                                 child2.getTagName() + ", " +
///                                 child2.hasChildren() + ", " +
///                                 child2NumAttrs + ", " +
///                                 child2.getAttributeValue("name") + ", " +
///                                 child2Child1.getTagName() + ", " +
///                                 child2Child1.hasChildren() + ", " +
///                                 child2Child1NumAttrs + ", " +
///                                 child2Child1.getAttributeValue("name") + ", " +
///                                 child2Child1.getAttributeValue("type") + ", " +
///                                 child2Child1.getAttributeValue("struct") + ", " +
///
///                                 rootNumAttrs + ", " +
///                                 root.getAttributeValue("version"));
///            }
///
///            assertCondition(
///                   children.size() == 2 &&
///
///                   child1Children.size() == 1 &&
///                   child1.getTagName().equals("struct") &&
///                   child1.hasChildren() == true &&
///                   child1NumAttrs == 2 &&  // name, usage
///                   child1.getAttributeValue("name").equals("struct1") &&
///                   child1Child1.getTagName().equals("data") &&
///                   child1Child1.hasChildren() == false &&
///                   child1Child1NumAttrs == 4 &&  // name, type, length, passby
///                   child1Child1.getAttributeValue("name").equals("field2") &&
///                   child1Child1.getAttributeValue("type").equals("char") &&
///                   child1Child1.getAttributeValue("length").equals("1") &&
///
///                   child2Children.size() == 1 &&
///                   child2.getTagName().equals("program") &&
///                   child2.hasChildren() == true &&
///                   child2NumAttrs == 3 &&  // name, path, epccsid
///                   child2.getAttributeValue("name").equals("prog1") &&
///                   child2Child1.getTagName().equals("data") &&
///                   child2Child1.hasChildren() == true && // its children are the struct contents
///                   child2Child1NumAttrs == 4 &&  // name, type, struct, passby
///                   child2Child1.getAttributeValue("name").equals("field1") &&
///                   child2Child1.getAttributeValue("type").equals("struct") &&
///                   child2Child1.getAttributeValue("struct").equals("struct1") &&
///                   child2Child1Child1.getAttributeValue("name").equals("field2") &&
///
///                   rootNumAttrs == 1 &&
///                   root.getAttributeValue("version").equals("4.0"));
///        }
///        catch (Exception e)
///        {
///            failed(e);
///        }
///    }


/// Note: This scenario is tested by other variations.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for count.
     Ensure error generated.

     **/
    public void Var030()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataCountBad");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();  ///TBD temporary
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          Exception cause = null;
          if (e instanceof PcmlException)
          {
            cause = ((PcmlException)e).getException();
            if (cause != null) {
              System.out.println("Original exception: " + cause.getClass().getName() + ": " + cause.getMessage());
              if (cause instanceof PcmlSpecificationException)
              {
                PcmlSpecificationException pse = (PcmlSpecificationException)cause;
                String[] msgs = pse.getMessages();
                System.out.println("Messages in PcmlSpecificationException: ");
                for (int i=0; i<msgs.length; i++) {
                  System.out.println(msgs[i]);
                }
              }
            }
          }

          assertCondition(e instanceof PcmlException &&
                 cause instanceof PcmlSpecificationException &&
                 receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

/// Note: This scenario is irrelevant.  PCML does not use the 'offset' attribute.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
/// Note: This scenario is irrelevant.  PCML does not use the 'offsetfrom' attribute.
///    /**
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for length.
     Ensure error generated.

     **/
    public void Var031()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataLengthNegative");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a too-large value for length.
     Ensure error generated.

     **/
    public void Var032()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataLengthTooLarge");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          Exception cause = null;
          if (e instanceof PcmlException)
          {
            cause = ((PcmlException)e).getException();
            if (cause != null) {
              System.out.println("Original exception: " + cause.getClass().getName() + ": " + cause.getMessage());
              if (cause instanceof PcmlSpecificationException)
              {
                PcmlSpecificationException pse = (PcmlSpecificationException)cause;
                String[] msgs = pse.getMessages();
                System.out.println("Messages in PcmlSpecificationException: ");
                for (int i=0; i<msgs.length; i++) {
                  System.out.println(msgs[i]);
                }
              }
            }
          }
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 cause instanceof PcmlSpecificationException &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains no value for length.
     Ensure error generated.

     **/
    public void Var033(int runMode)
    {
        notApplicable("Attended variation");
    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a blank value for length.
     Ensure error generated.

     **/
    public void Var034(int runMode)
    {
        notApplicable("Attended variation");
         }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for precision.
     Ensure error generated.

     **/
    public void Var035()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataPrecisionNegative");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a negative value for ccsid.
     Ensure error generated.

     **/
    public void Var036()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataCcsidNegative");
            failed("Did not throw exception."+pcmlDoc);
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains an invalid type value.
     Ensure error generated.

     **/
    public void Var037()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataTypeBad");
            failed("Did not throw exception."+pcmlDoc);
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a non-valid value for ccsid, e.g. 70000.
     Ensure error generated.

     **/
    public void Var038()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataCcsidBad");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          ///String expectedMsg = "A parse error occurred";
          String expectedMsg = "A PCML specification error occurred.";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);

          Exception cause = null;
          if (e instanceof PcmlException)
          {
            cause = ((PcmlException)e).getException();
            if (cause != null) {
              System.out.println("Original exception: " + cause.getClass().getName() + ": " + cause.getMessage());
              if (cause instanceof PcmlSpecificationException)
              {
                PcmlSpecificationException pse = (PcmlSpecificationException)cause;
                String[] msgs = pse.getMessages();
                System.out.println("Messages in PcmlSpecificationException: ");
                for (int i=0; i<msgs.length; i++) {
                  System.out.println(msgs[i]);
                }
              }
            }
          }

          assertCondition(receivedMsg != null &&
                 cause instanceof PcmlSpecificationException &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a reference to a &lt;struct&gt; that does not exist.
     Ensure error generated.

     **/
    public void Var039()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataStructNotExist");
            failed("Did not throw exception."+pcmlDoc);
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "A parse error occurred";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(///receivedMsg != null &&  // TBD - For some reason the msg is null.
                 ///receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"), "expectedMsg="+expectedMsg +" receivedMsg= "+receivedMsg);
        }
    }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Pass in document that exists and contains one &lt;data&gt; element that contains a bad value for bidistringtype.
     Ensure error generated.

     **/
    public void Var040()
    {
        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument("test.pcml.dataBidiBad");
            failed("Did not throw exception."+pcmlDoc);
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
///     Test ProgramCallDocument constructor with DocName parameter. Pass in document that
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
     Test ProgramCallDocument constructor with DocName parameter. Pass in complex PCML document.
     Ensure proper node tree created.
     **/
    public void Var041(int runMode)
    {
        notApplicable("Attended variation");
         }

    /**
     Test ProgramCallDocument constructor with DocName parameter. Serialize complex PCML document from Var 62. Parse serialized document and ensure results same as non-serialized document.
    **/
    @SuppressWarnings("deprecation")
    public void Var042()
    {
      java.io.File outFile1 = null;
      java.io.File outFile2 = null;
      boolean ok = true;
      try
      {
        //
        // Serialize/deserialize a ProgramCallDocument.
        //
      {
        ProgramCallDocument pcmlDoc1 = new ProgramCallDocument("test.pcml.qcustcdt");

        // Serialize the document.
        String docName = "Var049";
        outFile1 = new java.io.File(docName+".pcml.ser");
        pcmlDoc1.serialize(outFile1);  // TBD - Add variations to test the other serialize() methods as well.
        ///pcmlDoc1.serialize();

        ///Record rec1 = pcmlDoc1.toRecord("cusrec");
        ///Record rec1a = pcmlDoc1.toRecord("cusrec1");

        ///ProgramCallDocument pcmlDoc2 = new ProgramCallDocument("test." + outFile1.getName());
        ProgramCallDocument pcmlDoc2 = new ProgramCallDocument(docName);
        ///Record rec2 = pcmlDoc2.toRecord("cusrec");
        ///Record rec2a = pcmlDoc2.toRecord("cusrec1");

        // Verify that the new document matches the original document.
        if (RMTest.areEqual(pcmlDoc1.getDescriptor(),pcmlDoc2.getDescriptor())) {
          //succeeded();
        }
        else {
          ok = false;
          System.out.println("Deserialized document does not match original.");
        }
      }


        //
        // Serialize/deserialize a ProgramCallDocument, to/from the default location.
        //
        {
          ProgramCallDocument pcmlDoc1 = new ProgramCallDocument(systemObject_, "com.ibm.as400.resource.RUser");

          // Serialize the document.
          String docName = "RUser";
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
        }


        //
        // Serialize/deserialize a ProgramCallDocument, to/from a stream.
        //
        {

          ProgramCallDocument pcmlDoc3 = new ProgramCallDocument(systemObject_, "com.ibm.as400.resource.RUser");

          // Serialize the document to an output stream.
          ByteArrayOutputStream outStream = new ByteArrayOutputStream(10000);
          String docName = "RUser";
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
     Test ProgramCallDocument constructor with DocName and Loader parms.  Pass in valid PCML document and valid class loader and ensure document parsed correctly.
    **/
    public void Var043()
    {
        try
        {
            ClassLoader loader = java.lang.ClassLoader.getSystemClassLoader(); // Note: This method is new in Java2.
            ProgramCallDocument pcmlDoc1 = new ProgramCallDocument("test.pcml.qcustcdt", loader);

            ///ProgramCallDocument pcmlDoc2 = new ProgramCallDocument("test." + outFile.getName());
            ProgramCallDocument pcmlDoc2 = new ProgramCallDocument("test.pcml.qcustcdt");

            // Verify that the new document matches the original document.
            if (RMTest.areEqual(pcmlDoc1.getDescriptor(),pcmlDoc2.getDescriptor())) succeeded();
            else failed("Document does not match original.");
        }
        catch (Exception e)
        {
            failed(e);
        }
    }

/// Note: This scenario may be impossible to test, since there doesn't seem to be a way to create an "invalid" ClassLoader object.
///    /**
///     Test ProgramCallDocument constructor with DocName and Loader parms.  Pass in valid PCML document and invalid class loader and ensure correct error generated.
///    **/
///    public void Var066()
///    {
///        try
///        {
///            ProgramCallDocument pcmlDoc1 = new ProgramCallDocument("test.pcml.qcustcdt", null);
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
///        }
///    }

    /**
     Test ProgramCallDocument constructor with DocName and Loader parms.  Pass in valid PCML document
     and null for class loader and ensure error generated
    **/
    public void Var044()
    {
        try
        {
            ProgramCallDocument pcmlDoc1 = new ProgramCallDocument("test.pcml.qcustcdt", null);
            failed("Did not throw exception."+pcmlDoc1);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }
  
    static boolean extractFile(String resourceName, File destination) throws FileNotFoundException, IOException
    {
      boolean succeeded = false;
      InputStream is = ClassLoader.getSystemResourceAsStream(resourceName);
      if (is == null) {
        System.out.println("Resource not found: |" + resourceName + "|");
      }
      else
      {
        BufferedOutputStream bos = null;
        try
        {
          File parent = destination.getParentFile();
          if (parent != null) parent.mkdirs();  // create parent directory(s) if needed
          bos = new BufferedOutputStream(new FileOutputStream(destination));
          byte[] buffer = new byte[1024];
          while (true)
          {
            int bytesRead = is.read(buffer);
            if (bytesRead == -1) break;
            bos.write(buffer, 0, bytesRead);
          }
          succeeded = true;
        }
        finally {
          if (is != null) try { is.close(); } catch (Throwable t) {}
          if (bos != null) try { bos.close(); } catch (Throwable t) {}
        }
      }
      return succeeded;
    }
    
    /**
    Test ProgramCallDocument constructor with DocName parameter. Pass in document is xpcml with struct_i tag. Ensure the source can be successfully load.

    **/
    public void Var045(){
    	
    	File xpcmlSchemaFile = null;
        try
        {
        	 // Extract the XPCML XSD specification file (from the Toolbox jar) to the local directory, so that the XML parser can find it.
            xpcmlSchemaFile = new File("xpcml.xsd");
            extractFile ("com/ibm/as400/data/xpcml.xsd", xpcmlSchemaFile);

            ProgramCallDocument pcmlDoc = new ProgramCallDocument(systemObject_,"test.xpcml.struct_i.xpcml");
            assertCondition(true, "pcmlDoc="+pcmlDoc); 
        }
        catch (Exception e)
        {
            failed(e);
        }
    
    }
    
}
