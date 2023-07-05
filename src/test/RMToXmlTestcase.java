///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMToXmlTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Hashtable;

import com.ibm.as400.access.*;
import com.ibm.as400.data.RecordFormatDocument;

/**
 The RMToXmlTestcase class tests the following methods of the RecordFormatDocument class:
 <li>ToXml(File)
 <li>ToXml(OutputStream)
 <li>ToXml(fileName)
 **/
public class RMToXmlTestcase extends Testcase
{

  private static final boolean DEBUG = false;

    /**
     Constructor.
     **/
    public RMToXmlTestcase(AS400 systemObject, 
                             Hashtable namesAndVars, 
                             int runMode, 
                             FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMToXmlTestcase", namesAndVars, runMode, fileOutputStream);
    }

    // Note: This method modifies the contents of the passed-in StringBuffers.
    static boolean areEqualXml(StringBuffer got, StringBuffer expected)
    {
      // First collapse any spaces between ">" and "<" symbols.
      collapseBlanksBetweenTags(got);
      collapseBlanksBetweenTags(expected);

      return (got.toString().equals(expected.toString()));
    }

    static void collapseBlanksBetweenTags(StringBuffer buffer)
    {
      // Starting at the end of the string, collapse spaces outside of <...>.
      boolean inTag = false;
      for (int i=buffer.length()-1; i>-1; i--) {
        if (buffer.charAt(i) == ' ') {
          if (!inTag) buffer.deleteCharAt(i);
        }
        else if (buffer.charAt(i) == '>') inTag = true;
        else if (buffer.charAt(i) == '<') inTag = false;
      }
    }

    /**
     Test ToXml(File).  Call on RecordFormatDocument that contains no data. This should cause an exception.
     **/
    public void Var001()
    {
        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument();
            ByteArrayOutputStream oStream = new ByteArrayOutputStream(1000);
            rfmlDoc.toXml(oStream);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          String expectedMsg = "Document has not been set";
          String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
        }
    }


    /**
     Test ToXml(File).  Pass in null for file. Should get null pointer exception.
     **/
    public void Var002()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");
            ByteArrayOutputStream oStream = null;
            rfmlDoc.toXml(oStream);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          if (DEBUG) e.printStackTrace();
          String expectedMsg = "outputStream";
          String receivedMsg = e.getMessage();
          if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(receivedMsg != null &&
                 receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "java.lang.NullPointerException"));
        }
    }

    /**
     Test ToXml(fileName) with a blank fileName. This should cause an error.
     **/
    public void Var003()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        try
        {
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");
            ///ByteArrayOutputStream oStream = null;
            rfmlDoc.toXml("");
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
          ///e.printStackTrace();
          // String expectedMsg = "outputStream";
          // String receivedMsg = e.getMessage();
          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
          assertCondition(///receivedMsg != null &&
                 ///receivedMsg.indexOf(expectedMsg) != -1 &&
                 exceptionIsInstanceOf(e, "java.io.FileNotFoundException"));
        }
    }

/// TBD - I don't know how to make an "uninitialized file".  - jlee
///    /**
///     Test ToXml(File).  Pass a file that has not been initialized yet. This should cause an error.
///     **/
///
///    public void Var004()
///    {
///        File file = null;
///        try
///        {
///            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");
///            file = new File("emptyFile");
///            file.createNewFile();  // Note: This method is new in Java2,
///            rfmlDoc.toXml(file);
///            failed("Did not throw exception.");
///        }
///        catch (Exception e)
///        {
///          ///e.printStackTrace();
///          String expectedMsg = "outputStream";
///          String receivedMsg = e.getMessage();
///          ///System.out.println("DEBUG Received msgs: " + receivedMsg);
///          assertCondition(///receivedMsg != null &&
///                 ///receivedMsg.indexOf(expectedMsg) != -1 &&
///                 exceptionIsInstanceOf(e, "java.io.FileNotFoundException"));
///        }
///        finally
///        {
///          if (file != null) {
///            try { file.delete(); } catch (Exception e) {}
///          }
///        }
///    }


/// Note: I don't know how to make an uninitialized output stream.  - jlee
///    /**
///     Test ToXml(OutputStream). Pass an output stream that hasn't been initialized yet. This
///     should cause an error.
///     **/
///
///    public void Var005()
///    {
///    }

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains a field whose type is not one of the 9 supported RFML types, e.g. a TimeFieldDescription field.
     **/
    public void Var004()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Text converter = new AS400Text(8, systemObject_);
          TimeFieldDescription fd = new TimeFieldDescription(converter, "timeField1");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
          file1 = new File("emptyFile");
          file1.createNewFile();  // Note: This method is new in Java2.
          raFile1 = new RandomAccessFile(file1, "r");
          rfmlDoc.toXml(file1);
          ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};

          // Verify that the file is as expected.
          StringBuffer buffer = new StringBuffer();
          String nextLine = raFile1.readLine();
          while (nextLine != null) {
            buffer.append(nextLine);
            nextLine = raFile1.readLine();
          }
          ///System.out.println("File contents:\n" + buffer.toString());
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"timeField1\" type=\"char\" length=\"8\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains an ArrayFieldDescription for a supported RFML type, e.g. a CharacterFieldDescription field.
     Ensure proper RFML generated for the array.
     **/

    public void Var005()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Text converter = new AS400Text(8, systemObject_);
          ArrayFieldDescription fd = new ArrayFieldDescription(new AS400Array(converter, 5), "field1");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
          file1 = new File("emptyFile");
          file1.createNewFile();  // Note: This method is new in Java2.
          raFile1 = new RandomAccessFile(file1, "r");
          rfmlDoc.toXml(file1);
          ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};

          // Verify that the file is as expected.
          StringBuffer buffer = new StringBuffer();
          String nextLine = raFile1.readLine();
          while (nextLine != null) {
            buffer.append(nextLine);
            nextLine = raFile1.readLine();
          }
          if (DEBUG) System.out.println("File contents:\n" + buffer.toString());
          Integer intC = new Integer(systemObject_.getCcsid());
          String ccsidVal = new String(intC.toString());
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" count=\"5\" type=\"char\" length=\"8\" ccsid=\"" + ccsidVal.trim() + "\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains an ArrayFieldDescription whose elements are AS400Structure objects.
     Ensure proper error is generated.
     **/

    public void Var006()
    {
      try
      {
        AS400Text converter = new AS400Text(8, systemObject_);
        AS400Structure structure = new AS400Structure(new AS400DataType[] {converter});
        ArrayFieldDescription fd = new ArrayFieldDescription(new AS400Array(structure, 5), "field1");
        RecordFormat rf = new RecordFormat("recformat1");
        rf.addFieldDescription(fd);
        RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
        failed("Did not throw exception." +rfmlDoc);
      }
      catch (Exception e)
      {
        String expectedMsg = "Data type AS400Structure is not supported by RFML";
        String receivedMsg = e.getMessage();
        if (DEBUG) System.out.println("DEBUG Received msgs: " + receivedMsg);
        assertCondition(receivedMsg != null &&
               receivedMsg.indexOf(expectedMsg) != -1 &&
               exceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException"));
      }
    }

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one CharacterFieldDescription that has ccsid, length and description set and is not a key field or a variable length field and has no DFT value set and no contents. Ensure the proper RFML generated.  
     **/
    public void Var007()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Text converter = new AS400Text(20, systemObject_);
          CharacterFieldDescription fd = new CharacterFieldDescription(converter, "field1");
          fd.setCCSID("37");
          fd.setTEXT("Description for field1");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
          file1 = new File("emptyFile.rfml");
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"char\" length=\"20\" ccsid=\"37\"/>  </recordformat></rfml>");

          // Try constructing a new RecordFormatDocument from the generated file.
          ///System.out.println ("Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
          ///RecordFormatDocument doc2 = new RecordFormatDocument("test.emptyFile");
          RecordFormatDocument doc2 = new RecordFormatDocument("emptyFile");

          assertCondition(areEqualXml(buffer, expected), "doc2="+doc2);
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one CharacterFieldDescription that has length set but no ccsid or description and that is a key field and is a variable length field and has a default value set using setDFT(). Ensure proper RFML generated.
     
     **/
    public void Var008()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Text converter = new AS400Text(20, systemObject_);
          CharacterFieldDescription fd = new CharacterFieldDescription(converter, "field1");
          ///fd.setCCSID("37");
          ///fd.setTEXT("Description for charFieldDesc");
          String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
          fd.setKeyFieldFunctions(keyFunctions);
          fd.setVariableLength(true);
          fd.setDFT("This is the default");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"char\" length=\"20\" init=\"This is the default\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a Record that contains one CharacterFieldDescription that has length, ccsid, description that is not a key field or variable length and which has its contents set, i.e., it was constructed using a valid Record object that contains data. Ensure the proper RFML is generated.
     **/
    public void Var009()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Text converter = new AS400Text(20, 37);
          CharacterFieldDescription fd = new CharacterFieldDescription(converter, "field1");
          fd.setCCSID("37");
          fd.setTEXT("Description for charFieldDesc");
          // String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
          ///fd.setKeyFieldFunctions(keyFunctions);
          ///fd.setVariableLength(true);
          fd.setDFT("This is the default");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);
          Record rec = new Record(rf, "record1");
          String fieldContents1 = "This is a test";
          rec.setField("field1", fieldContents1);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"char\" length=\"20\" ccsid=\"37\" init=\"This is the default\"/>  </recordformat></rfml>");
          ///RMTest.displayTree(rfmlDoc);
          String fieldContents2 = (String)rfmlDoc.getValue("RECFORMAT1.field1");
          ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
          assertCondition(areEqualXml(buffer, expected) &&
                 fieldContents2.equals(fieldContents1));
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one BinaryFieldDescription that has AS400Bin2 data type, a length and no default value set. Ensure the proper RFML (type=int) is created.
     **/
    public void Var010()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Bin2 converter = new AS400Bin2();
          BinaryFieldDescription fd = new BinaryFieldDescription(converter, "field1", "BINFLD1", 2);
          ///fd.setCCSID("37");
          ///fd.setTEXT("Description for field1");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"int\" length=\"2\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one BinaryFieldDescription that has AS400Bin4 type a length and a default value set.  Ensure proper RFML generated.
     
     **/
    public void Var011()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Bin4 converter = new AS400Bin4();
          BinaryFieldDescription fd = new BinaryFieldDescription(converter, "field1", "BINFLD1", 4);
          ///fd.setCCSID("37");
          ///fd.setTEXT("Description for field1");
          fd.setDFT(new Integer(123));
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"int\" length=\"4\" init=\"123\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a Record that contains one BinaryFieldDescription that has AS400Bin2 type, a length and which has its contents set, i.e., it was constructed using a valid Record object that contains data.
     Ensure the proper RFML is generated, setting field value to contents value.
     **/
    public void Var012()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Bin4 converter = new AS400Bin4();
          BinaryFieldDescription fd = new BinaryFieldDescription(converter, "field1", "BINFLD1", 4);
          ///fd.setCCSID("37");
          ///fd.setTEXT("Description for field1");
          fd.setDFT(new Integer(123));
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);
          Record rec = new Record(rf, "record1");
          Integer fieldContents1 = new Integer(678);
          rec.setField("field1", fieldContents1);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"int\" length=\"4\" init=\"123\"/>  </recordformat></rfml>");
          ///RMTest.displayTree(rfmlDoc);
          Integer fieldContents2 = (Integer)rfmlDoc.getValue("RECFORMAT1.field1");
          ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
          assertCondition(areEqualXml(buffer, expected) &&
                 fieldContents2.equals(fieldContents1));
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one FloatFieldDescription that has AS400Float4 type, has setDecimalPositions=4, has setFLTPCN=*SINGLE and has a valid length but no default value set.
     Ensure proper RFML generated.
     **/
    public void Var013()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Float4 converter = new AS400Float4();
          FloatFieldDescription fd = new FloatFieldDescription(converter, "field1", "FLTFLD1", 4);
          fd.setDecimalPositions(4);
          fd.setFLTPCN("*SINGLE");

          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);
          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"float\" length=\"4\"/>  </recordformat></rfml>");
          // Note: The <data type="float"> element has no 'precision' attribute.
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one FloatFieldDescription that has AS400Float4 type, has setDecimalPositions=2, has setFLTPCN=*DOUBLE and has a valid length and default value set.
     Ensure proper RFML generated.
     **/
    public void Var014()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Float4 converter = new AS400Float4();
          FloatFieldDescription fd = new FloatFieldDescription(converter, "field1"/*, "FLTFLD1", 4*/);
          fd.setDecimalPositions(2);
          fd.setFLTPCN("*DOUBLE");
          fd.setLength(4);
          fd.setDFT(new Float(.123e25));

          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);
          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"float\" length=\"4\" init=\"1.23E24\"/>  </recordformat></rfml>");
          // Note: The <data type="float"> element has no 'precision' attribute.
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a Record that contains one FloatFieldDescription that has AS400Float8 type, has setDecimalPositions=8, has setFLTPCN=*DOUBLE and has a valid length and contents set.
     Ensure proper RFML generated.
     **/
    public void Var015()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400Float8 converter = new AS400Float8();
          FloatFieldDescription fd = new FloatFieldDescription(converter, "field1"/*, "FLTFLD1", 4*/);
          fd.setDecimalPositions(8);
          fd.setFLTPCN("*DOUBLE");
          fd.setLength(8);
          ///fd.setDFT(new Float(.123e25));
          fd.setDFT(new Double(.123e25));

          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          Record rec = new Record(rf, "record1");
          Double fieldContents1 = new Double(.456e78);
          rec.setField("field1", fieldContents1);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"float\" length=\"8\" init=\"1.23E24\"/>  </recordformat></rfml>");
          ///RMTest.displayTree(rfmlDoc);
          Double fieldContents2 = (Double)rfmlDoc.getValue("RECFORMAT1.field1");
          ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
          assertCondition(areEqualXml(buffer, expected) &&
                 fieldContents2.equals(fieldContents1));
        }
        catch (Exception e)
        {
            failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one PackedFieldDescription that has length and precision set and has no DFT value set and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var016()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400PackedDecimal converter = new AS400PackedDecimal(8,2);
          PackedDecimalFieldDescription fd = new PackedDecimalFieldDescription(converter, "field1"/*, "FLTFLD1", 4*/);
          ///fd.setDecimalPositions(4);
          ///fd.setFLTPCN("*SINGLE");

          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);
          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          //System.out.println("File contents:\n" + buffer.toString());
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"packed\" length=\"8\" precision=\"2\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one PackedFieldDescription that has length and precision set and has a DFT value set and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var017()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400PackedDecimal converter = new AS400PackedDecimal(8,2);
          PackedDecimalFieldDescription fd = new PackedDecimalFieldDescription(converter, "field1"/*, "FLTFLD1", 4*/);
          ///fd.setLength(8);
          ///fd.setDecimalPositions(2);
          fd.setDFT(new BigDecimal("1122334455"));
          ///fd.setFLTPCN("*SINGLE");

          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);
          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"packed\" length=\"8\" precision=\"2\" init=\"1122334455\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a Record that contains one PackedFieldDescription that has length and precision set and has its contents set.
     Ensure the proper RFML generated.  
     **/
    public void Var018()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400PackedDecimal converter = new AS400PackedDecimal(8,2);
          PackedDecimalFieldDescription fd = new PackedDecimalFieldDescription(converter, "field1"/*, "FLTFLD1", 4*/);
          fd.setDFT(new BigDecimal(112233D));

          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          Record rec = new Record(rf, "record1");
          ///Float fieldContents1 = new Float(.456e78);
          BigDecimal fieldContents1 = new BigDecimal(998877D);
          rec.setField("field1", fieldContents1);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"packed\" length=\"8\" precision=\"2\" init=\"112233\"/>  </recordformat></rfml>");
          ///RMTest.displayTree(rfmlDoc);
          BigDecimal fieldContents2 = (BigDecimal)rfmlDoc.getValue("RECFORMAT1.field1");
          ///System.out.println("fieldContents1 == |" + fieldContents1.toString() + "|");
          ///System.out.println("fieldContents2 == |" + fieldContents2.toString() + "|");
          assertCondition(areEqualXml(buffer, expected) &&
                 fieldContents2.doubleValue() == fieldContents1.doubleValue());
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one ZonedFieldDescription that has length and precision set and has no DFT value set and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var019()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        ZonedDecimalFieldDescription field1 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "field1");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"zoned\" length=\"6\" precision=\"2\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        BigDecimal fieldContents2 = (BigDecimal)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2==null);  // TBD - Working as designed?
      }
      catch (Exception e)
      {
        failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one ZonedFieldDescription that has length and precision set and has a DFT value set and no contents. Ensure the proper RFML generated.  
     **/
    public void Var020()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        ZonedDecimalFieldDescription field1 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "field1");
        field1.setDFT(new BigDecimal("12.34"));
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"12.34\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        BigDecimal fieldContents2 = (BigDecimal)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.toString().equals("12.34"));
      }
      catch (Exception e)
      {
        failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one ZonedFieldDescription that has length and precision set and has its contents set.
     Ensure the proper RFML generated.  
     **/
    public void Var021()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        ZonedDecimalFieldDescription field1 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "field1");
        field1.setDFT(new BigDecimal("12.34"));
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);
        rfmlDoc.setValue("FORMAT1.field1", new BigDecimal("56.78"));

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"12.34\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        BigDecimal fieldContents2 = (BigDecimal)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.toString().equals("56.78"));
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one HexFieldDescription that has length and is not variable length and has no DFT value set.
     Ensure the proper RFML generated.
     **/
    public void Var022()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400ByteArray converter = new AS400ByteArray(32);
          HexFieldDescription fd = new HexFieldDescription(converter, "field1");
          ///fd.setCCSID("37");
          ///fd.setTEXT("Description for field1");
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"byte\" length=\"32\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one HexFieldDescription that is variable length and has a DFT value set (repeating byte value) and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var023()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400ByteArray converter = new AS400ByteArray(4);
          HexFieldDescription fd = new HexFieldDescription(converter, "field1");
          fd.setVariableLength(true);  // TBD - Should we attempt to capture the "variable-length-ness" of the field???
          byte[] defaultVal = new byte[] {3,3,3,3};  // Same byte repeated.
          fd.setDFT(defaultVal);
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"byte\" length=\"4\" init=\"3\"/>  </recordformat></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one HexFieldDescription that is variable length and has a DFT value set (nonrepeating byte value) and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var024()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400ByteArray converter = new AS400ByteArray(4);
          HexFieldDescription fd = new HexFieldDescription(converter, "field1");
          fd.setVariableLength(true);  // TBD - Should we attempt to capture the "variable-length-ness" of the field???
          byte[] defaultVal = new byte[] {1,2,3,4};  // Not same byte repeated.
          fd.setDFT(defaultVal);
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rf);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"byte\" length=\"4\" init=\"1 2 3 4\"/>  </recordformat></rfml>");
          byte[] fieldContents2 = (byte [])rfmlDoc.getValue("RECFORMAT1.field1");
          ///printByteArray("fieldContents2: ", fieldContents2);
          assertCondition(areEqualXml(buffer, expected) &&
                 areEqual(fieldContents2,defaultVal));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a Record that contains one HexFieldDescription that is variable length and has a DFT value set (repeating byte value) and its contents set.
     Ensure the proper RFML generated.  
     **/
    public void Var025()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;
        try
        {
          AS400ByteArray converter = new AS400ByteArray(4);
          HexFieldDescription fd = new HexFieldDescription(converter, "field1");
          fd.setVariableLength(true);  // TBD - Should we attempt to capture the "variable-length-ness" of the field???
          byte[] defaultVal = new byte[] {3,3,3,3};  // Same byte repeated.
          fd.setDFT(defaultVal);
          RecordFormat rf = new RecordFormat("recformat1");
          rf.addFieldDescription(fd);

          Record rec = new Record(rf, "record1");
          ///Float fieldContents1 = new Float(.456e78);
          byte[] fieldContents1 = new byte[]{5,6,-1,-2};
          rec.setField("field1", fieldContents1);

          RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);
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
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"RECFORMAT1\">    <data name=\"field1\" type=\"byte\" length=\"4\" init=\"3\"/>  </recordformat></rfml>");
          byte[] fieldContents2 = (byte[])rfmlDoc.getValue("RECFORMAT1.field1");
          ///printByteArray("fieldContents2:", fieldContents2);
          assertCondition(areEqualXml(buffer, expected) &&
                 areEqual(fieldContents2,fieldContents1));
        }
        catch (Exception e)
        {
            failed(e);
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


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one PackedFieldDescription that is variable length set and has its contents set.
     Ensure the proper RFML generated.  
     **/
    public void Var026()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        PackedDecimalFieldDescription field1 = new PackedDecimalFieldDescription(new AS400PackedDecimal(6,2), "field1");
        field1.setDFT(new BigDecimal("12.34"));
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);
        rfmlDoc.setValue("FORMAT1.field1", new BigDecimal("56.78"));

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"packed\" length=\"6\" precision=\"2\" init=\"12.34\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        BigDecimal fieldContents2 = (BigDecimal)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.toString().equals("56.78"));
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains a field whose type is not one of the 9 supported RFML types, e.g. a DateFieldDescription field.
     **/
    public void Var027()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DateFieldDescription field1 = new DateFieldDescription(new AS400Text(10,systemObject_), "field1");
        field1.setDATFMT("*ISO");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2==null);  // TBD - Working as designed?
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains a field whose type is not one of the 9 supported RFML types, e.g. a DateFieldDescription field, that has a DFT value set.
     **/
    public void Var028()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DateFieldDescription field1 = new DateFieldDescription(new AS400Text(10,systemObject_), "field1");
        field1.setDATFMT("*ISO");
        field1.setDFT("1999-11-05");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" init=\"1999-11-05\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.equals("1999-11-05"));
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains a field whose type is not one of the 9 supported RFML types, e.g. a TimeStampFieldDescription field.
     **/
    public void Var029()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        TimestampFieldDescription field1 = new TimestampFieldDescription(new AS400Text(26,systemObject_), "field1");
        field1.setDFTCurrent();
        ///System.out.println("Default after setDFTCurrent(): " + (String)field1.getDFT() + ", " + (String)field1.getDFTCurrentValue());
        field1.setDFT("1999-01-30-12.47.55.00000");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"26\" init=\"1999-01-30-12.47.55.00000\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.equals("1999-01-30-12.47.55.00000"));
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSOnlyFieldDescription that has ccsid, length and description set and is not a key field or a variable length field and has no DFT value set and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var030()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSOnlyFieldDescription field1 = new DBCSOnlyFieldDescription(new AS400Text(10,systemObject_), "field1");
        field1.setCCSID("37");
        field1.setTEXT("Description for field1");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" ccsid=\"37\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2==null);  // TBD - Working as designed?
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSOnlyFieldDescription that has length set but no ccsid or description and that is a key field and is a variable length field and has a default value set using setDFT().
     Ensure proper RFML generated.
     
     **/
    public void Var031()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSOnlyFieldDescription field1 = new DBCSOnlyFieldDescription(new AS400Text(10,systemObject_), "field1");
        ///field1.setCCSID("37");
        ///field1.setTEXT("Description for field1");
        String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
        field1.setKeyFieldFunctions(keyFunctions);
        field1.setVariableLength(true);
        field1.setDFT("Field1Dflt");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" init=\"Field1Dflt\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.equals("Field1Dflt"));
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSOnlyFieldDescription that has length, ccsid, description that is not a key field or variable length and which has its contents set, i.e., it was constructed using a valid Record object that contains data. Ensure the proper RFML is generated setting
     init value to contents value.
    
     **/
    public void Var032()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSOnlyFieldDescription field1 = new DBCSOnlyFieldDescription(new AS400Text(10,systemObject_), "field1");
        field1.setCCSID("37");
        field1.setTEXT("Description for field1");
        ///String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
        ///field1.setKeyFieldFunctions(keyFunctions);
        ///field1.setVariableLength(true);
        field1.setDFT("Field1Dflt");
        recFmt.addFieldDescription(field1);

        Record rec = recFmt.getNewRecord();
        rec.setField("field1", "Field1Val");
        RecordFormatDocument rfmlDoc = new RecordFormatDocument(rec);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" ccsid=\"37\" init=\"Field1Dflt\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.equals("Field1Val"));
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSOpenFieldDescription that has ccsid, length and description set and is not a key field or a variable length field and has no DFT value set and no contents. Ensure the proper RFML generated.  
     **/
    public void Var033()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSOpenFieldDescription field1 = new DBCSOpenFieldDescription(new AS400Text(10,systemObject_), "field1");
        field1.setCCSID("37");
        field1.setTEXT("Description for field1");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" ccsid=\"37\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2==null);  // TBD - Working as designed?
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSOpenFieldDescription that has length set but no ccsid or description and that is a key field and is a variable length field and has a default value set using setDFT().
     Ensure proper RFML generated.
     
     **/
    public void Var034()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSOpenFieldDescription field1 = new DBCSOpenFieldDescription(new AS400Text(10,systemObject_), "field1");
        ///field1.setCCSID("37");
        ///field1.setTEXT("Description for field1");
        String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
        field1.setKeyFieldFunctions(keyFunctions);
        field1.setVariableLength(true);
        field1.setDFT("Field1Dflt");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" init=\"Field1Dflt\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.equals("Field1Dflt"));
      }
      catch (Exception e)
      {
        failed(e);
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


/// Note: These variations are probably overkill.  Implementation is optional.  -JPL

///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSOpenFieldDescription that has length, ccsid, description that is not a key field or variable length and which has its contents set, i.e., it was constructed using a valid Record object that contains data.
///     Ensure the proper RFML is generated setting init value to contents value.
///    
///     **/
///    public void Var035()
///    {
///    }
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat
///     that contains one DBCSEitherFieldDescription that has ccsid, length and description set
///     and is not a key field or a variable length field and has no DFT value set and no 
///     contents. Ensure the proper RFML generated.  
///     **/
///    public void Var036()
///    {
///    }
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSEitherFieldDescription that has length set but no ccsid or description and that is a key field and is a variable length field and has a default value set using setDFT().
///     Ensure proper RFML generated.
///     **/
///    public void Var037()
///    {
///    }
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSEitherFieldDescription that has length, ccsid, description that is not a key field or variable length and which has its contents set, i.e., it was constructed using a valid Record object that contains data.
///     Ensure the proper RFML is generated setting init value to contents value.
///    
///     **/
///    public void Var038()
///    {
///    }


    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSGraphicFieldDescription that has ccsid, length and description set and is not a key field or a variable length field and has no DFT value set and no contents.
     Ensure the proper RFML generated.  
     **/
    public void Var035()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSGraphicFieldDescription field1 = new DBCSGraphicFieldDescription(new AS400Text(10,systemObject_), "field1");
        field1.setCCSID("37");
        field1.setTEXT("Description for field1");
        ///String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
        ///field1.setKeyFieldFunctions(keyFunctions);
        ///field1.setVariableLength(true);
        ///field1.setDFT("Field1Dflt");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" ccsid=\"37\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2==null);
      }
      catch (Exception e)
      {
        failed(e);
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

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSGraphicFieldDescription that has length set but no ccsid or description and that is a key field and is a variable length field and has a default value set using setDFT().
     Ensure proper RFML generated.
     **/
    public void Var036()
    {
      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
        // Make a RecordFormat.
        RecordFormat recFmt = new RecordFormat("format1");
        DBCSGraphicFieldDescription field1 = new DBCSGraphicFieldDescription(new AS400Text(10,systemObject_), "field1");
        ///field1.setCCSID("37");
        ///field1.setTEXT("Description for field1");
        String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
        field1.setKeyFieldFunctions(keyFunctions);
        field1.setVariableLength(true);
        field1.setDFT("Field1Dflt");
        recFmt.addFieldDescription(field1);

        RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

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
        StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\">  <recordformat name=\"FORMAT1\">    <data name=\"field1\" type=\"char\" length=\"10\" init=\"Field1Dflt\"/>  </recordformat></rfml>");
        ///RMTest.displayTree(rfmlDoc);
        String fieldContents2 = (String)rfmlDoc.getValue("FORMAT1.field1");
        ///System.out.println("fieldContents2 == |" + fieldContents2 + "|");
        assertCondition(areEqualXml(buffer, expected) &&
               fieldContents2.equals("Field1Dflt"));
      }
      catch (Exception e)
      {
        failed(e);
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

/// Note: These variations are probably overkill.  Implementation is optional.  -JPL

///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one DBCSGraphicFieldDescription that has length, ccsid, description that is not a key field or variable length and which has its contents set, i.e., it was constructed using a valid Record object that contains data.
///     Ensure the proper RFML is generated setting init value to contents value.
///    
///     **/
///    public void Var041()
///    {
///    }
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one field for each supported RFML type with some default values set and with some fields without default values. Ensure proper RFML generated.
///     **/
///    public void Var042()
///    {
///    }
///
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one field for each supported RFML type and has its contents set.         
///     Ensure the proper RFML generated with init value set to contents value.
///     **/
///    public void Var043()
///    {
///    }
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one field for each supported RFML type where each field is an ArrayFieldDescription with various sizes and has its contents set.         
///     Ensure the proper RFML generated.
///     **/
///    public void Var044()
///    {
///    }
///
///    /**
///     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from a RecordFormat that contains one field for each supported RFML type where each field is an ArrayFieldDescription with various sizes and has its default value set (setDFT) but no contents set.
///     Ensure the proper RFML generated.
///     **/
///    public void Var045()
///    {
///    }

    /**
     Test ToXml(File).  Run ToXml on a RecordFormatDocument object created from RFML file "qcustcdt.rfml".
     Ensure the proper RFML generated.
     **/
    public void Var037()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

      File file1 = null;
      RandomAccessFile raFile1 = null;
      try
      {
          RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdt");

          file1 = new File("emptyFile");
          file1.createNewFile();  // Note: This method is new in Java2.
          raFile1 = new RandomAccessFile(file1, "r");
          rfmlDoc.toXml(file1);
          ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};

          // Verify that the file is as expected.
          StringBuffer buffer = new StringBuffer();
          String nextLine = raFile1.readLine();
          while (nextLine != null) {
            buffer.append(nextLine);
            nextLine = raFile1.readLine();
          }
          ///System.out.println("File contents:\n" + buffer.toString());
          StringBuffer expected = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE rfml SYSTEM \"rfml.dtd\"><rfml version=\"4.0\" ccsid=\"819\">  <recordformat name=\"cusrec\">    <data name=\"cusnum\" type=\"zoned\" length=\"6\" precision=\"0\" init=\"0\"/>    <data name=\"lstnam\" type=\"char\" length=\"8\" ccsid=\"37\" init=\"A\"/>    <data name=\"init\" type=\"char\" length=\"3\" ccsid=\"37\" init=\"B\"/>    <data name=\"street\" type=\"char\" length=\"13\" ccsid=\"37\" init=\"C\"/>    <data name=\"city\" type=\"char\" length=\"6\" ccsid=\"37\" init=\"D\"/>    <data name=\"state\" type=\"char\" length=\"2\" ccsid=\"37\" init=\"E\"/>    <data name=\"zipcod\" type=\"zoned\" length=\"5\" init=\"1\"/>    <data name=\"cdtlmt\" type=\"zoned\" length=\"4\" init=\"2\"/>    <data name=\"chgcod\" type=\"zoned\" length=\"1\" init=\"3\"/>    <data name=\"baldue\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"4\"/>    <data name=\"cdtdue\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"5\"/>  </recordformat>  <recordformat name=\"cusrec1\">    <data name=\"cusnum\" type=\"zoned\" length=\"6\" precision=\"0\" init=\"0\"/>    <data name=\"lstnam\" type=\"char\" length=\"8\" ccsid=\"37\" init=\"A\"/>    <data name=\"init\" type=\"char\" length=\"3\" ccsid=\"37\" init=\"B\"/>    <data name=\"street\" type=\"char\" length=\"13\" ccsid=\"37\" init=\"C\"/>    <data name=\"city\" type=\"char\" length=\"6\" ccsid=\"37\" init=\"D\"/>    <data name=\"state\" type=\"char\" length=\"2\" ccsid=\"37\" init=\"E\"/>    <data name=\"zipcod\" type=\"zoned\" length=\"5\" init=\"1\"/>    <data name=\"cdtlmt\" type=\"zoned\" length=\"4\" init=\"2\"/>    <data name=\"chgcod\" type=\"zoned\" length=\"1\" init=\"3\"/>    <data name=\"baldue\" type=\"struct\" struct=\"balance\"/>    <data name=\"cdtdue\" type=\"struct\" struct=\"balance\"/>  </recordformat>  <recordformat name=\"cusrecAscii\">    <data name=\"cusnum\" type=\"zoned\" length=\"6\" precision=\"0\" init=\"0\"/>    <data name=\"lstnam\" type=\"char\" length=\"8\" init=\"A\"/>    <data name=\"init\" type=\"char\" length=\"3\" init=\"B\"/>    <data name=\"street\" type=\"char\" length=\"13\" init=\"C\"/>    <data name=\"city\" type=\"char\" length=\"6\" init=\"D\"/>    <data name=\"state\" type=\"char\" length=\"2\" init=\"E\"/>    <data name=\"zipcod\" type=\"zoned\" length=\"5\" init=\"1\"/>    <data name=\"cdtlmt\" type=\"zoned\" length=\"4\" init=\"2\"/>    <data name=\"chgcod\" type=\"zoned\" length=\"1\" init=\"3\"/>    <data name=\"baldue\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"4\"/>    <data name=\"cdtdue\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"5\"/>  </recordformat>  <struct name=\"balance\">    <data name=\"amount\" type=\"zoned\" length=\"6\" precision=\"2\" init=\"7\"/>  </struct></rfml>");
          assertCondition(areEqualXml(buffer, expected));
        }
        catch (Exception e)
        {
            failed(e);
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

