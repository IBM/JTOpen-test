///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLXML.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.XML;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream; //@C1A
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JDXMLTest;
import test.JD.JDTestUtilities;

/**
 * Testcase JDXMLXML. This tests the following method of the JDBC XML class when
 * used against a clob column:
 * 
 * <ul>
 * <li>free()
 * <li>getBinaryStream()
 * <li>setBinaryStream()
 * <li>getCharacterStream()
 * <li>getString()
 * <li>setCharacterStream()
 * <li>setString()
 * <li>getSource()
 * <li>setResult()
 * 
 * </ul>
 */
public class JDXMLXML extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDXMLXML";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDXMLTest.main(newArgs); 
   }
  public static final String DOMSOURCE = "javax.xml.transform.dom.DOMSource";

  public static final String SAXSOURCE = "javax.xml.transform.sax.SAXSource";

  public static final String STAXSOURCE = "javax.xml.transform.stax.StAXSource";

  public static final String STREAMSOURCE = "javax.xml.transform.stream.StreamSource";

  public static final String DOMRESULT = "javax.xml.transform.dom.DOMResult";

  public static final String SAXRESULT = "javax.xml.transform.sax.SAXResult";

  public static final String STAXRESULT = "javax.xml.transform.stax.StAXResult";

  public static final String STREAMRESULT = "javax.xml.transform.stream.StreamResult";

  // Private data.

  private Statement statement1_;

  private Statement statement2_;

  private Statement statement37_;

  private Statement statement1208_;

  private Statement statement1200_;

  private Statement statement930_;

  private ResultSet rs37_;

  private ResultSet rs1208_;

  private ResultSet rs1200_;

  private ResultSet rs930_;

  private ResultSet rs2_;
  
  private String rs2Query_ = null; 

  private JDXMLErrorListener errorListener = new JDXMLErrorListener();

  //
  // We create tables with different encodings
  // For each table the following rows are set
  // Row 1 -- blank
  // Row 2 -- only xml declaration
  // Row 3 -- only xml declaration with encoding
  // Row 4 -- single item with xml declaration
  // Row 5 -- single item with xml declaration with encoding
  // Row 6 -- single item without xml declaration
  // Row 7 -- single item without xml declaration but with beginning whitespace
  // Row 8 -- not valid XML
  // Row 9 -- single item with byte order mark (for unicode testcases
  // Row 10 -- xml declaration, but invalid XML
  // Row 11 -- xml declaration with invalid encoding -- larger than 80
  // characters (Based on Row 5)

  public static String TABLE37_ = JDXMLTest.COLLECTION + ".XMLXML37";

  public static String TABLE1208_ = JDXMLTest.COLLECTION + ".XMLXML1208";

  public static String TABLE1200_ = JDXMLTest.COLLECTION + ".XMLXML1200";

  public static String TABLE930_ = JDXMLTest.COLLECTION + ".XMLXMLB930";

  public static String ROW1_S_ = "<TEST/>";//optimized
  public static String ROW1_S37_ = "<?xml version=\"1.0\" encoding=\"IBM-37\"?><TEST/>";

  public static String ROW1_S_LUW_ = "<TEST/>";

  public static String ROW2_S_ = "<?xml version=\"1.0\" ?><TEST/>";
  public static String ROW2_S_NODCL_ = "<TEST/>";

  public static String ROW2_S_LUW_ = "<TEST/>";

  public static String ROW2_S1208_STANDALONE_= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TEST/>";

  public static String ROW2_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TEST/>"; 
  public static String ROW3_S_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TEST></TEST>";

  // TODO:  Change this back to specific encoding
  // public static String ROW3_S_UTF16_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TEST></TEST>";
  public static String ROW3_S_UTF16_ENCODING = "<?xml version=\"1.0\" ?><TEST></TEST>";

  public static String ROW3_S_LUW_ = "<TEST/>";
  public static String ROW3_S_NODCL_ = "<TEST/>";

  public static String ROW3_S37_ = "<?xml version=\"1.0\" encoding=\"IBM-37\"?><TEST></TEST>";

  public static String ROW3_S930_ = "<?xml version=\"1.0\" encoding=\"IBM-930\"?><TEST></TEST>";

  public static String ROW3_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TEST></TEST>";

  public static String ROW3_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TEST></TEST>";

  public static String ROW3_S1202_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?><TEST></TEST>";

  public static String ROW4_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW4_S_NL_ = "<TOP attrib=\"TOP\">TOP</TOP>\n";

  public static String ROW4_S_T_ = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";


  public static String ROW4_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW4_S1200_NL_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW4_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW4_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";
  public static String ROW5_S_NL_ = "<TOP attrib=\"TOP\">TOP</TOP>\n";

  public static String ROW5_S_T_ = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S37_ = "<?xml version=\"1.0\" encoding=\"IBM-37\"?> <TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?> <TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1202_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?> <TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S930_ = "<?xml version=\"1.0\" encoding=\"IBM-930\"?> <TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1208_T_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1208_T_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S37_T_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"IBM-37\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1200_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
  public static String ROW5_S1200_T_NL = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1200_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW5_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";


  public static String ROW6_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW6_S_T_ = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW6_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW6_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW6_S1200_NL_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW7_S_ = " \t\r\n<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW7_S_T_ = "<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW7_S2_ = " \t\n<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW7_S1208_T_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW7_S1200_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW7_S1200_TNL_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW8_S_ = "<TEST/>";
  public static String ROW8_S_LUW_ = "<TEST/>";
  public static String ROW8_S_SOURCEOUTPUT_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TEST/>"; 

  public static String ROW8_S_SAXSOURCEOUTPUT_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TEST/>"; 

  public static String ROW8_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TEST/>" ;

  public static String ROW8_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TEST/>";

  public static String ROW9_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";

  public static String ROW10_S_ = "<?xml version=\"1.0\" ?><T1></T1>";

  public static String ROW10_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T1/>"; 


  public static String ROW11_S_ = "<?xml version=\"1.0\" encoding=\"BAD-ENCODING\"?> <TOP attrib=\"TOP\">TOP 01234567891234567893234567894234567895234567896234567897234567898</TOP>";

  public static String ROW11_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP 01234567891234567893234567894234567895234567896234567897234567898</TOP>";

  public static String EXPECTED_ROW4_S1200_PLUS_NL_[] =
    { ROW4_S_	,  /* LUW */
      ROW4_S1200_NL_, /* Also seen on LUW */ 
      ROW4_S1200_, /* ??? */
      ROW4_S1208_
    };

  public static String EXPECTED_ROW5_S1200_T_PLUS_NL_[] =
    {
    ROW5_S_,  /* LUW */ 
    ROW4_S1200_NL_, /* 16 */
    ROW5_S1200_T_, /* 169P 166P */
    ROW5_S1208_T_,
    };

  public static String EXPECTED_ROW6_S1200_PLUS_NL[] =
    { ROW6_S_, /* LUW */  
    ROW6_S1200_NL_, /* 16 */
    ROW6_S1200_, /* 169P 166P */
    };

  public static String EXPECTED_ROW7_S1200_T_PLUS_NL_[] =
    {
    ROW7_S_T_, /* LUW */ 
    ROW7_S1200_TNL_, /* 16 */
    ROW7_S1200_T_, /* 169P 166P */
    };

  public static String[] EXPECTED_ROW4_ST_PLUS_S1208_ =
    {
    ROW4_S_, /* LUW */ 
    ROW4_S_T_, /* 16 */
    ROW4_S1208_ /* 169P, 166P */
    };

  public static String[] EXPECTED_ROW5_ST_PLUS_S1208_ =
    {
    ROW4_S_, /* LUW */ 
    ROW5_S_T_, /* 16 */
    ROW5_S1208_T_ /* 169P, 166P */
    };

  public static String[] EXPECTED_ROW6_ST_PLUS_S1208_ =
    {
    ROW6_S_, /* LUW */ 
    ROW6_S_T_, /* 16 */
    ROW6_S1208_ /* 169P, 166P */
    };

  public static String[] EXPECTED_ROW7_ST_PLUS_S1208_ =
    {
    ROW7_S_, /* LUW */ 
    ROW7_S_T_, /* 16 */
    ROW7_S1208_T_ /* 169P, 166P */
    };

  public static byte[] ROW11_ = 
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x42, 0x41, 0x44, 0x2D, 0x45, 0x4E,
        0x43, 0x4F, 0x44, 0x49, 0x4E, 0x47, 0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54,
        0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54,
        0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x20, 0x30, 0x31, 0x32, 0x33,
        0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
        0x37, 0x38, 0x39, 0x33, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x34, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x35, 0x32, 0x33,
        0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x36, 0x32, 0x33, 0x34, 0x35, 0x36,
        0x37, 0x38, 0x39, 0x37, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x38, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E };

  public static byte[] ROW1_1208_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6e, 0x63 , 0x6f,
        0x64, 0x69, 0x6e, 0x67, 0x3d , 0x22, 0x55, 0x54, 0x46, 0x2d, 0x38, 0x22, 
        0x3f, 0x3e, 0x3c, 0x54, 0x45, 0x53, 0x54, 0x2f, 0x3e}; // <?xml version="1.0" encoding="UTF-8"?><TEST/>

  public static byte[] ROW2_1208_ = ROW1_1208_;//  <?xml version="1.0" encoding="UTF-8"?><TEST/>

	
  public static byte[] ROW2_LUW_ = {0x3c,0x54,0x45,0x53,0x54,0x2f,0x3e
	} ;

  public static byte[] ROW3_LUW_ = {0x3c,0x54,0x45,0x53,0x54,0x2f,0x3e
  };
  public static byte[] ROW3_1208_ = ROW1_1208_;//  <?xml version="1.0" encoding="UTF-8"?><TEST/>

  public static byte[] ROW3_1208_37_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x49, 0x42, 0x4d, 0x2d, 0x33, 0x37,
        0x22, 0x3F, 0x3E };

  public static byte[] ROW3_1208_930_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x49, 0x42, 0x4d, 0x2d, 0x39, 0x33,
        0x30, 0x22, 0x3F, 0x3E };

  public static byte[] ROW3_1208_1200_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x31, 0x36,
        0x22, 0x3F, 0x3E };

  public static byte[] ROW4_LUW_ = {0x3c,0x54,0x4f,0x50,0x20,0x61,0x74,0x74,0x72,0x69,0x62,0x3d,0x22, 0x54,0x4f,0x50,0x22,0x3e,0x54,0x4f,0x50,0x3c,0x2f,0x54,0x4f,0x50,0x3e } ;

  public static byte[] ROW4_1208_ =
    {   0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6e, 0x63, 0x6f,
        0x64, 0x69, 0x6e, 0x67, 0x3d, 0x22, 0x55, 0x54, 0x46, 0x2d, 0x38, 0x22,
        0x3F, 0x3E,  0x3C,
        0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22,
        0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F,
        0x50, 0x3E }; //<?xml version="1.0" encoding="UTF-8"?><TOP attrib="TOP">TOP</TOP>
 

  public static byte[] ROW5_LUW_ = {0x3c, 0x54, 0x4f, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3d, 0x22, 0x54, 0x4f, 0x50, 0x22, 0x3e, 0x54, 0x4f, 0x50, 0x3c, 0x2f, 0x54, 0x4f, 0x50, 0x3e }; 
  public static byte[] ROW5_1208_ = ROW4_1208_;
    

  public static byte[] ROW5_1208_1200_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x31, 0x36,
        0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74,
        0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F,
        0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E };

  public static byte[] ROW5_1208_37_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x49, 0x42, 0x4d, 0x2d, 0x33, 0x37,
        0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74,
        0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F,
        0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E };

  public static byte[] ROW5_1208_930_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
        0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x49, 0x42, 0x4d, 0x2d, 0x39, 0x33,
        0x30, 0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74,
        0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54,
        0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E };

  public static byte[] ROW6_LUW_  = ROW4_LUW_; 
  public static byte[] ROW6_1208_ = ROW4_1208_;
    

  public static byte[] ROW7_LUW_  = ROW4_LUW_; 
  public static byte[] ROW7_1208_ = ROW4_1208_;


  public static byte[] ROW8_LUW_ = {0x3c, 0x54, 0x45, 0x53, 0x54, 0x2f, 0x3e};

  public static byte[] ROW8_1208_ = {0x3c, 0x3f, 0x78, 0x6d, 0x6c, 0x20, 0x76, 
      0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x3d, 0x22, 0x31, 0x2e, 0x30, 0x22, 
      0x20, 0x65, 0x6e, 0x63, 0x6f, 0x64, 0x69, 0x6e, 0x67, 0x3d, 0x22, 0x55, 
      0x54, 0x46, 0x2d, 0x38, 0x22, 0x3f, 0x3e, 0x3c, 0x54, 0x45, 0x53, 0x54, 0x2f, 0x3e};


  public static byte[] ROW9_LUW_ = 
    { 0x3c, 0x54, 0x4f, 0x50,
      0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3d, 0x22, 0x54, 0x4f, 0x50, 0x22, 0x3e,
      0x54, 0x4f, 0x50, 0x3c, 0x2f, 0x54, 0x4f, 0x50, 0x3e};

  public static byte[] ROW9_1208_ =
    { 0x3c, 0x3f, 0x78, 0x6d, 0x6c, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x3d, 
      0x22, 0x31, 0x2e, 0x30, 0x22, 0x20, 0x65, 0x6e, 0x63, 0x6f, 0x64, 0x69, 0x6e, 0x67,
      0x3d, 0x22, 0x55, 0x54, 0x46, 0x2d, 0x38, 0x22, 0x3f, 0x3e, 0x3c, 0x54, 0x4f, 0x50,
      0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3d, 0x22, 0x54, 0x4f, 0x50, 0x22, 0x3e,
      0x54, 0x4f, 0x50, 0x3c, 0x2f, 0x54, 0x4f, 0x50, 0x3e};


  public static byte[] ROW10_1208_ =
    { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F,
        0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E, 0x31 };

  public static byte[] ROW1_1200_ =
    { (byte)0Xfe, (byte)0Xff, (byte)0X00, (byte)0X3c, (byte)0X00, (byte)0X3f, (byte)0X00, (byte)0X78, 
      (byte)0X00, (byte)0X6d, (byte)0X00, (byte)0X6c, (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X76, 
      (byte)0X00, (byte)0X65, (byte)0X00, (byte)0X72, (byte)0X00, (byte)0X73, (byte)0X00, (byte)0X69, 
      (byte)0X00, (byte)0X6f, (byte)0X00, (byte)0X6e, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, 
      (byte)0X00, (byte)0X31, (byte)0X00, (byte)0X2e, (byte)0X00, (byte)0X30, (byte)0X00, (byte)0X22, 
      (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X65, (byte)0X00, (byte)0X6e, (byte)0X00, (byte)0X63, 
      (byte)0X00, (byte)0X6f, (byte)0X00, (byte)0X64, (byte)0X00, (byte)0X69, (byte)0X00, (byte)0X6e, 
      (byte)0X00, (byte)0X67, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X55, 
      (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X46, (byte)0X00, (byte)0X2d, (byte)0X00, (byte)0X31, 
      (byte)0X00, (byte)0X36, (byte)0X00, (byte)0X42, (byte)0X00, (byte)0X45, (byte)0X00, (byte)0X22, 
      (byte)0X00, (byte)0X3f, (byte)0X00, (byte)0X3e, (byte)0X00, (byte)0X3c, (byte)0X00, (byte)0X54, 
      (byte)0X00, (byte)0X45, (byte)0X00, (byte)0X53, (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X2f, 
      (byte)0X00, (byte)0X3e }; //"?<?xml version="1.0" encoding="UTF-16BE"?><TEST/>"  with BOM at beginning
 

  public static byte[] ROW2_1200_ = ROW1_1200_;  

  public static byte[] ROW3_1200_ =
    { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00, 0x20,
        0x00, 0x76, 0x00, 0x65, 0x00, 0x72, 0x00, 0x73, 0x00, 0x69, 0x00, 0x6F,
        0x00, 0x6E, 0x00, 0x3D, 0x00, 0x22, 0x00, 0x31, 0x00, 0x2E, 0x00, 0x30,
        0x00, 0x22, 0x00, 0x20, 0x00, 0x65, 0x00, 0x6E, 0x00, 0x63, 0x00, 0x6F,
        0x00, 0x64, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x67, 0x00, 0x3D, 0x00, 0x22,
        0x00, 0x55, 0x00, 0x54, 0x00, 0x46, 0x00, 0x2D, 0x00, 0x31, 0x00, 0x36,
        0x00, 0x22, 0x00, 0x3F, 0x00, 0x3E, 
        
        0x00, 0x3c, 0x00, 0x54, 0x00, 0x45, 0x00, 0x53, 0x00, 0x54, 0x00, 0x2f,
        0x00, 0x3e };

  public static byte[] ROW3_1200_M_ =  ROW1_1200_;  

  public static byte[] ROW4_1200_ = 
          {(byte)0Xfe, (byte)0Xff, (byte)0X00, (byte)0X3c, (byte)0X00, (byte)0X3f, (byte)0X00, (byte)0X78, 
          (byte)0X00, (byte)0X6d, (byte)0X00, (byte)0X6c, (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X76, 
          (byte)0X00, (byte)0X65, (byte)0X00, (byte)0X72, (byte)0X00, (byte)0X73, (byte)0X00, (byte)0X69, 
          (byte)0X00, (byte)0X6f, (byte)0X00, (byte)0X6e, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, 
          (byte)0X00, (byte)0X31, (byte)0X00, (byte)0X2e, (byte)0X00, (byte)0X30, (byte)0X00, (byte)0X22, 
          (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X65, (byte)0X00, (byte)0X6e, (byte)0X00, (byte)0X63, 
          (byte)0X00, (byte)0X6f, (byte)0X00, (byte)0X64, (byte)0X00, (byte)0X69, (byte)0X00, (byte)0X6e, 
          (byte)0X00, (byte)0X67, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X55, 
          (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X46, (byte)0X00, (byte)0X2d, (byte)0X00, (byte)0X31, 
          (byte)0X00, (byte)0X36, (byte)0X00, (byte)0X42, (byte)0X00, (byte)0X45, (byte)0X00, (byte)0X22, 
          (byte)0X00, (byte)0X3f, (byte)0X00, (byte)0X3e, (byte)0X00, (byte)0X3c, (byte)0X00, (byte)0X54, 
          (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X61, 
          (byte)0X00, (byte)0X74, (byte)0X00, (byte)0X74, (byte)0X00, (byte)0X72, (byte)0X00, (byte)0X69, 
          (byte)0X00, (byte)0X62, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X54, 
          (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X3e, 
          (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, (byte)0X00, (byte)0X3c, 
          (byte)0X00, (byte)0X2f, (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, 
          (byte)0X00, (byte)0X3e}; //"?<?xml version="1.0" encoding="UTF-16BE"?><TOP attrib="TOP">TOP</TOP>" with BOM


  public static byte[] ROW5_1200_ =
    { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00, 0x20,
        0x00, 0x76, 0x00, 0x65, 0x00, 0x72, 0x00, 0x73, 0x00, 0x69, 0x00, 0x6F,
        0x00, 0x6E, 0x00, 0x3D, 0x00, 0x22, 0x00, 0x31, 0x00, 0x2E, 0x00, 0x30,
        0x00, 0x22, 0x00, 0x20, 0x00, 0x65, 0x00, 0x6E, 0x00, 0x63, 0x00, 0x6F,
        0x00, 0x64, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x67, 0x00, 0x3D, 0x00, 0x22,
        0x00, 0x55, 0x00, 0x54, 0x00, 0x46, 0x00, 0x2D, 0x00, 0x31, 0x00, 0x36,
        0x00, 0x22, 0x00, 0x3F, 0x00, 0x3E, 0x00, 0x20, 0x00, 0x3C, 0x00, 0x54,
        0x00, 0x4F, 0x00, 0x50, 0x00, 0x20, 0x00, 0x61, 0x00, 0x74, 0x00, 0x74,
        0x00, 0x72, 0x00, 0x69, 0x00, 0x62, 0x00, 0x3D, 0x00, 0x22, 0x00, 0x54,
        0x00, 0x4F, 0x00, 0x50, 0x00, 0x22, 0x00, 0x3E, 0x00, 0x54, 0x00, 0x4F,
        0x00, 0x50, 0x00, 0x3C, 0x00, 0x2F, 0x00, 0x54, 0x00, 0x4F, 0x00, 0x50,
        0x00, 0x3E };

  public static byte[] ROW5_1200_M_ =
    { (byte)0Xfe, (byte)0Xff, (byte)0X00, (byte)0X3c, (byte)0X00, (byte)0X3f, (byte)0X00, (byte)0X78, 
      (byte)0X00, (byte)0X6d, (byte)0X00, (byte)0X6c, (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X76, 
      (byte)0X00, (byte)0X65, (byte)0X00, (byte)0X72, (byte)0X00, (byte)0X73, (byte)0X00, (byte)0X69, 
      (byte)0X00, (byte)0X6f, (byte)0X00, (byte)0X6e, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, 
      (byte)0X00, (byte)0X31, (byte)0X00, (byte)0X2e, (byte)0X00, (byte)0X30, (byte)0X00, (byte)0X22, 
      (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X65, (byte)0X00, (byte)0X6e, (byte)0X00, (byte)0X63, 
      (byte)0X00, (byte)0X6f, (byte)0X00, (byte)0X64, (byte)0X00, (byte)0X69, (byte)0X00, (byte)0X6e, 
      (byte)0X00, (byte)0X67, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X55, 
      (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X46, (byte)0X00, (byte)0X2d, (byte)0X00, (byte)0X31, 
      (byte)0X00, (byte)0X36, (byte)0X00, (byte)0X42, (byte)0X00, (byte)0X45, (byte)0X00, (byte)0X22, 
      (byte)0X00, (byte)0X3f, (byte)0X00, (byte)0X3e, (byte)0X00, (byte)0X3c, (byte)0X00, (byte)0X54, 
      (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, (byte)0X00, (byte)0X20, (byte)0X00, (byte)0X61, 
      (byte)0X00, (byte)0X74, (byte)0X00, (byte)0X74, (byte)0X00, (byte)0X72, (byte)0X00, (byte)0X69, 
      (byte)0X00, (byte)0X62, (byte)0X00, (byte)0X3d, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X54, 
      (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, (byte)0X00, (byte)0X22, (byte)0X00, (byte)0X3e, 
      (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, (byte)0X00, (byte)0X3c, 
      (byte)0X00, (byte)0X2f, (byte)0X00, (byte)0X54, (byte)0X00, (byte)0X4f, (byte)0X00, (byte)0X50, 
      (byte)0X00, (byte)0X3e}; //"?<?xml version="1.0" encoding="UTF-16BE"?><TOP attrib="TOP">TOP</TOP>"


  public static byte[] ROW6_1200_ = ROW5_1200_M_;

  public static byte[] ROW7_1200_ = ROW5_1200_M_;

  public static byte[] ROW8_1200_ = { 
   0x00, 0x4E,  /* N */ 
   0x00, 0x4F,  /* O */ 
   0x00, 0x54,  /* T */ 
   0x00, 0x20,  /*   */ 
   0x00, 0x58,  /* X */ 
   0x00, 0x4D,  /* M */ 
   0x00, 0x4C   /* L */ 
  };


  public static byte[] ROW9_1200_ = ROW5_1200_M_; 

  public static byte[] ROW10_1200_ =
    { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00, 0x20,
        0x00, 0x76, 0x00, 0x65, 0x00, 0x72, 0x00, 0x73, 0x00, 0x69, 0x00, 0x6F,
        0x00, 0x6E, 0x00, 0x3D, 0x00, 0x22, 0x00, 0x31, 0x00, 0x2E, 0x00, 0x30,
        0x00, 0x22, 0x00, 0x20, 0x00, 0x3F, 0x00, 0x3E, 0x00, 0x31 };

  public static byte[] ROW1_930_ =
     {(byte)0X4c, (byte)0X6f, (byte)0Xb7, (byte)0X75, (byte)0X74, (byte)0X40, (byte)0Xb5, (byte)0X66, 
      (byte)0X9b, (byte)0Xab, (byte)0X71, (byte)0X77, (byte)0X76, (byte)0X7e, (byte)0X7f, (byte)0Xf1, 
      (byte)0X4b, (byte)0Xf0, (byte)0X7f, (byte)0X40, (byte)0X66, (byte)0X76, (byte)0X64, (byte)0X77, 
      (byte)0X65, (byte)0X71, (byte)0X76, (byte)0X68, (byte)0X7e, (byte)0X7f, (byte)0Xc9, (byte)0Xc2, 
      (byte)0Xd4, (byte)0X60, (byte)0Xf9, (byte)0Xf3, (byte)0Xf0, (byte)0X7f, (byte)0X6f, (byte)0X6e, 
      (byte)0X4c, (byte)0Xe3, (byte)0Xc5, (byte)0Xe2, (byte)0Xe3, (byte)0X61, (byte)0X6e }; //without decl, data sent is utf-8 -> cp930 <?xml version="1.0" encoding="IBM-930"?><TEST/>
     

  
  public static byte[] ROW2_930_ = ROW1_930_;
  /*
   * 4c 6f b7 75 74 40 b5 66 9b ab 71 77 76 7e 7f f1 4b f0 7f 40 66 76 64 77 65
   * 71 76 68 7e 7f c9 c2 d4 60 f9 f3 f0 7f 6f 6e};
   */
  public static byte[] ROW3_930_ = ROW1_930_;

  public static byte[] ROW4_930_ = 
      {(byte)0X4c, (byte)0X6f, (byte)0Xb7, (byte)0X75, (byte)0X74, (byte)0X40, (byte)0Xb5, (byte)0X66, 
       (byte)0X9b, (byte)0Xab, (byte)0X71, (byte)0X77, (byte)0X76, (byte)0X7e, (byte)0X7f, (byte)0Xf1, 
       (byte)0X4b, (byte)0Xf0, (byte)0X7f, (byte)0X40, (byte)0X66, (byte)0X76, (byte)0X64, (byte)0X77, 
       (byte)0X65, (byte)0X71, (byte)0X76, (byte)0X68, (byte)0X7e, (byte)0X7f, (byte)0Xc9, (byte)0Xc2, 
       (byte)0Xd4, (byte)0X60, (byte)0Xf9, (byte)0Xf3, (byte)0Xf0, (byte)0X7f, (byte)0X6f, (byte)0X6e, 
       (byte)0X4c, (byte)0Xe3, (byte)0Xd6, (byte)0Xd7, (byte)0X40, (byte)0X62, (byte)0Xb3, (byte)0Xb3, 
       (byte)0X9b, (byte)0X71, (byte)0X63, (byte)0X7e, (byte)0X7f, (byte)0Xe3, (byte)0Xd6, (byte)0Xd7, 
       (byte)0X7f, (byte)0X6e, (byte)0Xe3, (byte)0Xd6, (byte)0Xd7, (byte)0X4c, (byte)0X61, (byte)0Xe3, 
       (byte)0Xd6, (byte)0Xd7, (byte)0X6e}; //<?xml version="1.0" encoding="IBM-930"?><TOP attrib="TOP">TOP</TOP>
    

  /*
   * 4c 6f b7 75 74 40 b5 66 9b ab 71 77 76 7e 7f f1 4b f0 7f 40 66 76 64 77 65
   * 71 76 68 7e 7f c9 c2 d4 60 f9 f3 f0 7f 6f 6e
   * 404ce3d6d74062b3b39b71637e7fe3d6d77f6ee3d6d74c61e3d6d76e
   */
  public static byte[] ROW5_930_ = ROW4_930_;

  public static byte[] ROW6_930_ = ROW4_930_;

  public static byte[] ROW7_930_ = ROW4_930_;

  public static byte[] ROW8_930_ =
    { 0x4E, 0x00, 0x4F, 0x00, 0x54, 0x00, 0x20, 0x00, 0x58, 0x00, 0x4D, 0x00,
        0x4C, 0x00 };

  public static byte[] ROW9_930_ = ROW4_930_;


  public static byte[] ROW10_930_ = ROW4_930_;
  
  public static byte[] ROW11_930 = ROW1_930_; 
      
  public static byte[] ROW1_ = {}; 
  public static byte[] ROW1_LUW_ = {0x3c,0x54,0x45,0x53,0x54,0x2f,0x3e};
  public static byte[] ROW1_37_ = { 
            0x4c,       0x6f, (byte)0xa7, (byte)0x94, (byte)0x93,       0x40, (byte)0xa5, 
      (byte)0x85, (byte)0x99, (byte)0xa2, (byte)0x89, (byte)0x96, (byte)0x95,       0x7e, 
            0x7f, (byte)0xf1,       0x4b, (byte)0xf0,       0x7f,       0x40, (byte)0x85, 
      (byte)0x95, (byte)0x83, (byte)0x96, (byte)0x84, (byte)0x89, (byte)0x95, (byte)0x87, 
            0x7e,       0x7f, (byte)0xc9, (byte)0xc2, (byte)0xd4, (byte)0xf0, (byte)0xf3, 
      (byte)0xf7,       0x7f,       0x6f,       0x6e,       0x4c, (byte)0xe3, (byte)0xc5, 
      (byte)0xe2, (byte)0xe3,       0x61,       0x6e};//<?xml version="1.0" encoding="IBM037"?><TEST/>  i5 returns decl and optimized xml //

  public static byte[] ROW2_37_ = ROW1_37_; // same as row1 with decl and optimized xml
 

  public static byte[] ROW3_37_ = ROW1_37_;  // same as row1 with decl and optimized xml
   

  public static byte[] ROW4_37_ =
     {(byte)0x4c, (byte)0x6f, (byte)0xa7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xa5, (byte)0x85, (byte)0x99, (byte)0xa2,
      (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7e, (byte)0x7f, (byte)0xf1, (byte)0x4b, (byte)0xf0, (byte)0x7f, (byte)0x40, 
      (byte)0x85, (byte)0x95, (byte)0x83, (byte)0x96, (byte)0x84, (byte)0x89, (byte)0x95, (byte)0x87, (byte)0x7e, (byte)0x7f, 
      (byte)0xc9, (byte)0xc2, (byte)0xd4, (byte)0xf0, (byte)0xf3, (byte)0xf7, (byte)0x7f, (byte)0x6f, (byte)0x6e, (byte)0x4c, 
      (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x40, (byte)0x81, (byte)0xa3, (byte)0xa3, (byte)0x99, (byte)0x89, (byte)0x82, 
      (byte)0x7e, (byte)0x7f, (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x7f, (byte)0x6e, (byte)0xe3, (byte)0xd6, (byte)0xd7, 
      (byte)0x4c, (byte)0x61, (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x6e};
      //  <?xml version="1.0" encoding="IBM037"?><TOP attrib="TOP">TOP</TOP>
 

  public static byte[] ROW5_37_ = ROW4_37_;
  // <?xml version="1.0" encoding="IBM037"?><TOP attrib="TOP">TOP</TOP>
 
  
  public static byte[] ROW6_37_ = ROW5_37_; // <?xml version="1.0" encoding="IBM037"?><TOP attrib="TOP">TOP</TOP>

  public static byte[] ROW7_37_ =ROW5_37_; // <?xml version="1.0" encoding="IBM037"?><TOP attrib="TOP">TOP</TOP>
 



  /* Row 8 37 is invalid XML without declaration */ 
  public static byte[] ROW8_37_ =     {
      (byte) 0xD5,  /* N */ 
      (byte) 0xD6,  /* O */
      (byte) 0xE3,  /* T */ 
      (byte) 0x40,  /*   */ 
      (byte) 0xE7,  /* X */
      (byte) 0xD4,  /* M */ 
      (byte) 0xD3   /* L */ 
  };



  public static byte[] ROW9_37_ =
    { 0x4C, 0x6F, (byte) 0xA7, (byte) 0x94, (byte) 0x93, 0x40, (byte) 0xA5,
        (byte) 0x85, (byte) 0x99, (byte) 0xA2, (byte) 0x89, (byte) 0x96,
        (byte) 0x95, (byte) 0x7E, 0x7F, (byte) 0xF1, 0x4B, (byte) 0xF0, 0x7F,
        0x40, 0x6F, 0x6E };

  public static byte[] ROW10_37_ =
    { 0x4C, 0x6F, (byte) 0xA7, (byte) 0x94, (byte) 0x93, 0x40, (byte) 0xA5,
        (byte) 0x85, (byte) 0x99, (byte) 0xA2, (byte) 0x89, (byte) 0x96,
        (byte) 0x95, (byte) 0x7E, 0x7F, (byte) 0xF1, 0x4B, (byte) 0xF0, 0x7F,
        0x40, 0x6F, 0x6E, (byte) 0xf1 };

  // UTF8 Rows

  public static StringBuffer message = new StringBuffer();

  public String lobThreshold = ";lob threshold=100000";

  public boolean isLocator = false;

  /**
   * Constructor.
   */
  public JDXMLXML(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDXMLXML", namesAndVars, runMode, fileOutputStream,
 password);
  }

  public JDXMLXML(AS400 systemObject, String testname, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
  protected void setup() throws Exception {
      String updateValue = "";

    TABLE37_ = JDXMLTest.COLLECTION + ".XMLXMLB37";
    TABLE1208_ = JDXMLTest.COLLECTION + ".XMLXML1208";
    TABLE1200_ = JDXMLTest.COLLECTION + ".XMLXML1200";
    TABLE930_ = JDXMLTest.COLLECTION + ".XMLXML930";
    
    if (isJdbc40() && isXmlSupported()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {

        String url_ = "jdbc:db2://" + systemObject_.getSystemName() + ":"
            + JDTestDriver.jccPort + "/" + JDTestDriver.jccDatabase
            + lobThreshold;
        connection_ = testDriver_.getConnection(url_,
            systemObject_.getUserId(), encryptedPassword_);

        statement1_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        statement2_ = connection_.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

      } else {
        String url = baseURL_ + lobThreshold ;
        connection_ = testDriver_.getConnection(url, systemObject_.getUserId(),encryptedPassword_);
        statement1_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        statement2_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      }

      try {
        statement1_.executeUpdate("DROP TABLE " + TABLE37_);
      } catch (Exception e) {
      }
      if (getDriver() == JDTestDriver.DRIVER_JCC) {

	  statement1_.executeUpdate("CREATE TABLE " + TABLE37_ + "(C_XML XML  )");
      } else {

	  statement1_.executeUpdate("CREATE TABLE " + TABLE37_ + "(C_XML XML CCSID 37 )");
      }

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + TABLE37_ + " (C_XML) VALUES (?)");
      try { 
	  updateValue = ROW1_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW2_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
          // Note:  LUW supports the insert of ROW3
          // The IBM i drivers need to support this
          // also by stripping the encoding
	  updateValue = ROW3_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();

	  updateValue = ROW4_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW5_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW6_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW7_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW8_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW9_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
	  updateValue = ROW10_S_;
	  ps.setString(1, updateValue);
	  ps.executeUpdate();
      //
      // We can't prime a row with bad data if it is really XML
      // put row 1 there 
	  updateValue = ROW1_S_;
	  ps.setString (1, updateValue); ps.executeUpdate ();


	  ps.close();
      } catch (Exception e) {
	  output_.println("Exception loading table on value "+updateValue);
	  e.printStackTrace(); 
      } 

      try {
        statement1_.executeUpdate("DROP TABLE " + TABLE1208_);
      } catch (Exception e) {
      }

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        statement1_
            .executeUpdate("CREATE TABLE " + TABLE1208_ + "(C_XML XML )");
      } else {
        statement1_.executeUpdate("CREATE TABLE " + TABLE1208_
            + "(C_XML XML CCSID 1208)");
      }
      ps = connection_.prepareStatement("INSERT INTO " + TABLE1208_
          + " (C_XML) VALUES (?)");
      try { 
      updateValue = ROW1_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW2_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW3_S1208_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW4_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      
      updateValue = ROW5_S1208_;
      ps.setString(1, updateValue);
      
      ps.executeUpdate();
      updateValue = ROW6_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW7_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW8_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW9_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW10_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW1_S_;
      ps.setString (1, updateValue); ps.executeUpdate ();
      } catch (Exception e) {
          output_.println("Exception loading table on value "+updateValue);
          e.printStackTrace(); 
    } 

      ps.close();

      try {
        statement1_.executeUpdate("DROP TABLE " + TABLE1200_);
      } catch (Exception e) {
      }

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        statement1_
            .executeUpdate("CREATE TABLE " + TABLE1200_ + "(C_XML XML )");
      } else {
        statement1_.executeUpdate("CREATE TABLE " + TABLE1200_
            + "(C_XML XML CCSID 1200)");
      }
      ps = connection_.prepareStatement("INSERT INTO " + TABLE1200_
          + " (C_XML) VALUES (?)");
      try { 
      updateValue = ROW1_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
 
      updateValue = ROW2_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();

      updateValue = ROW3_S1200_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW4_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();

      updateValue = ROW5_S1200_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW6_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW7_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW8_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW9_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW10_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW1_S_;
      ps.setString (1, updateValue); ps.executeUpdate ();
      } catch (Exception e) {
          output_.println("Exception loading table on value "+updateValue);
          e.printStackTrace(); 
    } 

      ps.close();

      try {
        statement1_.executeUpdate("DROP TABLE " + TABLE930_);
      } catch (Exception e) {
      }

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        statement1_.executeUpdate("CREATE TABLE " + TABLE930_ + "(C_XML XML )");

      } else {
        statement1_.executeUpdate("CREATE TABLE " + TABLE930_
            + "(C_XML XML CCSID 930)");
      }

      ps = connection_.prepareStatement("INSERT INTO " + TABLE930_
          + " (C_XML) VALUES (?)");
try { 
      updateValue = ROW1_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW2_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW3_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();

      updateValue = ROW4_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW5_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW6_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW7_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW8_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW9_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW10_S_;
      ps.setString(1, updateValue);
      ps.executeUpdate();
      updateValue = ROW1_S_;
      ps.setString (1, updateValue); ps.executeUpdate ();
} catch (Exception e) {
  output_.println("Exception loading table on value "+updateValue);
  e.printStackTrace(); 
} 

      ps.close();

      connection_.commit();

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        // SQL0270 from JCC when SCROLL_INSENSITIVIE
        // DB2 SQL Error: SQLCODE=-270, SQLSTATE=42997, SQLERRMC=63,
        // 63 A column with a LOB type, distinct type on a LOB type, or
        // structured type cannot be specified in the select-list of an
        // insensitive scrollable cursor
        // 53 A column with a LONG VARCHAR, LONG VARGRAPHIC, DATALINK,
        // LOB, XML type, distinct type on any of these types, or
        // structured type cannot be specified in the select-list of a
        // scrollable cursor.

        statement1208_ = connection_.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        statement1200_ = connection_.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

        statement37_ = connection_.createStatement(ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_UPDATABLE);
        statement930_ = connection_.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

      } else {
        statement1208_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        statement1200_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        statement37_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        statement930_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      }

      rs1208_ = statement1208_.executeQuery("SELECT * FROM " + TABLE1208_);
      rs1200_ = statement1200_.executeQuery("SELECT * FROM " + TABLE1200_);
      rs37_ = statement37_.executeQuery("SELECT * FROM " + TABLE37_);
      rs930_ = statement930_.executeQuery("SELECT * FROM " + TABLE930_);

    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
  protected void cleanup() throws Exception {
    if (isJdbc20()) {
      if (statement37_ != null) statement37_.close();
      if (statement1208_ != null)       statement1208_.close();
      if (statement1200_ != null)       statement1200_.close();
      if (statement930_ != null)       statement930_.close();

      if (statement2_ != null) statement2_.close();
      statement1_.executeUpdate("DROP TABLE " + TABLE37_);
      statement1_.executeUpdate("DROP TABLE " + TABLE1208_);
      statement1_.executeUpdate("DROP TABLE " + TABLE1200_);
      statement1_.executeUpdate("DROP TABLE " + TABLE930_);
      statement1_.close();

      connection_.close();
      connection_ = null; 

    }
  }

  public ResultSet rsAbsolute(ResultSet rs, int row) throws SQLException {

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      if (rs == rs37_) {
        rs = statement37_.executeQuery("SELECT * FROM " + TABLE37_);
        rs37_ = rs;
      } else if (rs == rs1200_) {
        rs = statement1200_.executeQuery("SELECT * FROM " + TABLE1200_);
        rs1200_ = rs;
      } else if (rs == rs1208_) {
        rs = statement1208_.executeQuery("SELECT * FROM " + TABLE1208_);
        rs1208_ = rs;
      } else if (rs == rs2_) {
        rs = statement1208_.executeQuery(rs2Query_);
        rs2_ = rs;
      } else {
        throw new SQLException("Unknown result set");
      }
      for (int i = 0; i < row; i++) {
        rs.next();
      }
    } else {
      rs.absolute(row);
    }
    return rs; 
  }

  public void testMethodAfterFree(String method) {
    if (checkJdbc40() && checkXmlSupport()) {
      try {
        rsAbsolute(rs37_, 2);
        Object xml = JDReflectionUtil
            .callMethod_OS(rs37_, "getSQLXML", "C_XML");
        JDReflectionUtil.callMethod_V(xml, "free");
        try {
          if (method.equals("setString")) {
            JDReflectionUtil.callMethod_V(xml, method, "X");
          } else if (method.equals("getSource")) {
            JDReflectionUtil.callMethod_O(xml, method, Class
                .forName("javax.xml.transform.dom.DOMSource"));
          } else if (method.equals("setResult")) {
            JDReflectionUtil.callMethod_V(xml, method, Class
                .forName("javax.xml.transform.dom.DOMResult"));
          } else {
            JDReflectionUtil.callMethod_O(xml, method);
          }
          failed("Didn't throw SQLException for " + method);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * free() getString() -- Make sure method throws an exception after free
   */
  public void Var001() {
    testMethodAfterFree("getBinaryStream");
  }

  public void Var002() {
    testMethodAfterFree("setBinaryStream");
  }

  public void Var003() {
    testMethodAfterFree("getCharacterStream");
  }

  public void Var004() {
    testMethodAfterFree("setCharacterStream");
  }

  public void Var005() {
    testMethodAfterFree("getString");
  }

  public void Var006() {
    testMethodAfterFree("setString");
  }

  public void Var007() {
    testMethodAfterFree("getSource");
  }

  public void Var008() {
    testMethodAfterFree("setResult");
  }

  /**
   * free() free() -- Shouldn't throw exception
   */
  public void Var009() {
    if (checkJdbc40() && checkXmlSupport()) {

      try {

        rsAbsolute(rs37_, 2);

        Object xml = JDReflectionUtil
            .callMethod_OS(rs37_, "getSQLXML", "C_XML");
        JDReflectionUtil.callMethod_V(xml, "free");
        JDReflectionUtil.callMethod_V(xml, "free");
        assertCondition(true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testGetBinaryStream(ResultSet rs, int row, byte[] expected,
      String expectedException, String testDescription) {

    if (checkXmlSupport() && checkJdbc40()) {
      try {
        message.setLength(0);

        rs = rsAbsolute(rs, row);
        Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_XML");
        InputStream v = (InputStream) JDReflectionUtil.callMethod_O(xml,
            "getBinaryStream");
        // On LUW, you only get 1 call to getBinaryStream() after that, the
	// XML object is unreadable.  Because of this, we need to have
        // compare pass back what bytes were returned
	StringBuffer sb = new StringBuffer(); 
        boolean passed = compare(v, expected, sb);
        if ((!passed) || (expectedException != null)) {
	    output_.println("Failed:  sb = "+sb.toString()); 
	    message.append(sb.toString());
        }
        if (expectedException != null) {
          failed("For test "+testDescription+" Expected exception " + expectedException + " : "
              + message.toString());
        } else {
          assertCondition(passed, "Comparision failed for "+testDescription+":" + message.toString());
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          boolean passed = message1.indexOf(expectedException) >= 0;
          assertCondition(passed, "For test "+testDescription+" Expected exception '" + message1
              + "' should contain '" + expectedException + "'");
          if (!passed) {
            e.printStackTrace();
          }
        } else {
          failed(e, "Unexpected Exception for test "+testDescription);
        }
      }
    }

  }

  /**
   * getBinaryStream() - CCSID37 and lobs of various sizes
   */
  public void Var010() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs37_, 1, ROW1_LUW_, null, "ROW1 CCSID 37");
      } else {  
	  testGetBinaryStream(rs37_, 1, ROW1_37_, null, "ROW1 CCSID 37");
      }
  }

  public void Var011() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs37_, 2, ROW2_LUW_, null, "ROW2 CCSID 37");
      } else {  
	  testGetBinaryStream(rs37_, 2, ROW2_37_, null, "ROW2 CCSID 37");
      }
  }

  public void Var012() {
    // Due to the locator implementation, we get back the results of
    // getAsciiStream
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs37_, 3, ROW3_LUW_, null, "ROW3 CCSID 37");
      } else {  

	  if (isLocator) {
	      testGetBinaryStream(rs37_, 3, ROW3_1208_37_, null, "ROW3 CCSID 37 LOCATOR");
	  } else {
	      testGetBinaryStream(rs37_, 3, ROW3_37_, null,  "ROW3 CCSID 37");
	  }
      }
  }

  public void Var013() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs37_, 4, ROW4_LUW_, null, "ROW4 CCSID 37");
      } else {  

	  testGetBinaryStream(rs37_, 4, ROW4_37_, null, "ROW4 CCSID 37");
      }
  }

  public void Var014() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs37_, 5, ROW5_LUW_, null, "ROW5 CCSID 37");
      } else {  
	  if (isLocator) {
	      testGetBinaryStream(rs37_, 5, ROW5_1208_37_, null, "ROW5 CCSID 37 LOCATOR");
	  } else {
	      testGetBinaryStream(rs37_, 5, ROW5_37_, null, "ROW5 CCSID 37");
	  }
      }
  }

  public void Var015() {
      if (JDTestDriver.isLUW()) {

	  testGetBinaryStream(rs37_, 6, ROW6_LUW_, null, "ROW6 CCSID 37");
      } else {
	  testGetBinaryStream(rs37_, 6, ROW6_37_, null, "ROW6 CCSID 37");

      }
  }

  public void Var016() {
      if (JDTestDriver.isLUW()) {

    testGetBinaryStream(rs37_, 7, ROW7_LUW_, null, "ROW7 CCSID 37");
      } else {
    testGetBinaryStream(rs37_, 7, ROW7_37_, null, "ROW7 CCSID 37");
      }
  }

  public void Var017() {
      notApplicable("parsing error case");
  }

  public void Var018() {
      notApplicable("Parsing error case");
  }

  public void Var019() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 1, ROW1_LUW_, null, "ROW1 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 1, ROW1_1208_, null, "ROW1 CCSID 1208");
      }

  }

  public void Var020() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 2, ROW2_LUW_, null, "ROW2 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 2, ROW2_1208_, null, "ROW2 CCSID 1208");
      }
  }

  public void Var021() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 3, ROW3_LUW_, null, "ROW3 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 3, ROW3_1208_, null, "ROW3 CCSID 1208");
      }
  }

  public void Var022() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 4, ROW4_LUW_, null, "ROW4 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 4, ROW4_1208_, null, "ROW4 CCSID 1208");
      }
  }

  public void Var023() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 5, ROW5_LUW_, null, "ROW5 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 5, ROW5_1208_, null, "ROW5 CCSID 1208");
      }
  }

  public void Var024() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 6, ROW6_LUW_, null, "ROW6 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 6, ROW6_1208_, null, "ROW6 CCSID 1208");
      }
  }

  public void Var025() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 7, ROW7_LUW_, null, "ROW7 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 7, ROW7_1208_, null, "ROW7 CCSID 1208");
      }
  }

  public void Var026() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 8, ROW8_LUW_, null, "ROW8 CCSID 1208");
      } else {
	  testGetBinaryStream(rs1208_, 8, ROW8_1208_, null, "ROW8 CCSID 1208");
      } 
  }

  public void Var027() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1208_, 9, ROW9_LUW_, null, "ROW9 CCSID 1208");
      } else { 
	  testGetBinaryStream(rs1208_, 9, ROW9_1208_, null, "ROW9 CCSID 1208");
      }
  }

  public void Var028() {
      notApplicable("Parsing error"); 
      // testGetBinaryStream(rs1208_, 11, ROW11_, "XML PARSING ERROR");
  }

  public void Var029() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 1, ROW1_LUW_, null, "ROW1 CCSID 1200");
      } else { 
	  testGetBinaryStream(rs1200_, 1, ROW1_1200_, null, "ROW1 CCSID 1200");
      }
  }

  public void Var030() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 2, ROW2_LUW_, null, "ROW2 CCSID 1200");
      } else { 
	  testGetBinaryStream(rs1200_, 2, ROW2_1200_, null, "ROW2 CCSID 1200");
      }
  }

  public void Var031() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 3, ROW3_LUW_, null, "ROW3 CCSID 1200");
      } else { 

	  if (isLocator) {
	      testGetBinaryStream(rs1200_, 3, ROW3_1208_1200_, null, "ROW3 CCSID 1200 LOCATOR");
	  } else {
	      testGetBinaryStream(rs1200_, 3, ROW3_1200_M_, null, "ROW3 CCSID 1200");
	  }
      }
  }

  public void Var032() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 4, ROW4_LUW_, null, "ROW4 CCSID 1200");
      } else { 

	  testGetBinaryStream(rs1200_, 4, ROW4_1200_, null, "ROW4 CCSID 1200");
      }
  }

  public void Var033() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 5, ROW5_LUW_, null, "ROW5 CCSID 1200");
      } else { 
	  if (isLocator) {
	      testGetBinaryStream(rs1200_, 5, ROW5_1208_1200_, null, "ROW5 CCSID 1200");
	  } else {
	      testGetBinaryStream(rs1200_, 5, ROW5_1200_M_, null, "ROW5 CCSID 1200");
	  }
      }
  }

  public void Var034() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 6, ROW6_LUW_, null, "ROW6 CCSID 1200");
      } else {
	  testGetBinaryStream(rs1200_, 6, ROW6_1200_, null, "ROW6 CCSID 1200");
      }
  }

  public void Var035() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 7, ROW7_LUW_, null, "ROW7 CCSID 1200");
      } else {
	  testGetBinaryStream(rs1200_, 7, ROW7_1200_, null, "ROW7 CCSID 1200");
      }
  }

  public void Var036() {
      notApplicable("Parsing error");
  }

  public void Var037() {
      if (JDTestDriver.isLUW()) {
	  testGetBinaryStream(rs1200_, 9, ROW9_LUW_, null, "ROW9 CCSID 1200");
      } else {
	  testGetBinaryStream(rs1200_, 9, ROW9_1200_, null, "ROW9 CCSID 1200");
      }
  }

  public void Var038() {
    notApplicable("PARSING ERROR");
    /* 
    if (isLocator) {
      testGetBinaryStream(rs1200_, 11, ROW11_, null);
    } else {
      testGetBinaryStream(rs1200_, 11, ROW11_, "XML PARSING ERROR");
    }
     */ 
  }

  public void Var039() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else {  
	     testGetBinaryStream(rs930_, 1, ROW1_930_, null, "ROW1 CCSID 930");
	 }
	 
  }

  public void Var040() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else   {
	     testGetBinaryStream(rs930_, 2, ROW2_930_, null, "ROW2 CCSID 930");
	 }
  }

  public void Var041() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else   {
	     testGetBinaryStream(rs930_, 3, ROW3_930_, null, "ROW3 CCSID 930");
	 }
  }

  public void Var042() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else  
    testGetBinaryStream(rs930_, 4, ROW4_930_, null, "ROW4 CCSID 930");
  }

  public void Var043() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else  
    testGetBinaryStream(rs930_, 5, ROW5_930_, null, "ROW5 CCSID 930");
  }

  public void Var044() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else  
    testGetBinaryStream(rs930_, 6, ROW6_930_, null, "ROW6 CCSID 930");
  }

  public void Var045() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else  
    testGetBinaryStream(rs930_, 7, ROW7_930_, null, "ROW7 CCSID 930");
  }

  public void Var046() {
      notApplicable("parsing error test"); 
  }

  public void Var047() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else  
    testGetBinaryStream(rs930_, 9, ROW9_930_, null, "ROW9 CCSID 930");
  }

  public void Var048() {
         if (JDTestDriver.isLUW()) {
	     notApplicable("CCSID 930 test");
	 } else  
    testGetBinaryStream(rs930_, 11, ROW11_930, null, "ROW11 CCSID 930"); //dup of row 1
  }

  public void testGetCharacterStream(ResultSet rs, String column, int row,
      String expected, String expectedException) {

    if (checkJdbc40() && checkXmlSupport()) {
      try {
        message.setLength(0);
        rs = rsAbsolute(rs, row);
        Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
        Reader v = (Reader) JDReflectionUtil.callMethod_O(xml,
            "getCharacterStream");
	StringBuffer sb = new StringBuffer(); 
        boolean passed = compare(v, expected, sb);
        if (!passed) {
	    message.append(sb.toString());
        }
        if (expectedException == null) {
          assertCondition(passed, "Comparision failed :" + message.toString());
        } else {
          failed("Expected exception " + expectedException);
        }
      } catch (Exception e) {
        if (expectedException == null) {
          failed(e, "Unexpected Exception");
        } else {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '" + message1 + "' should contain '"
                  + expectedException + "'");

        }
      }
    }

  }

  /**
   * getCharacterStream() - CCSID37 and lobs of various sizes
   */
  public void Var049() {
         if (JDTestDriver.isLUW()) {
	     testGetCharacterStream(rs37_, "C_XML", 1, ROW1_S_LUW_, null);
	 } else { 
	     testGetCharacterStream(rs37_, "C_XML", 1, ROW1_S_, null);
	 }
  }

  public void Var050() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs37_, "C_XML", 2, ROW2_S_LUW_, null);
      } else { 
	  testGetCharacterStream(rs37_, "C_XML", 2, ROW2_S_NODCL_, null);
      }
  }

  public void Var051() {
         if (JDTestDriver.isLUW()) {
	     testGetCharacterStream(rs37_, "C_XML", 3, ROW3_S_LUW_, null);
	 } else { 
	     testGetCharacterStream(rs37_, "C_XML", 3, ROW2_S_NODCL_, null); //same as row2
	 }
  }

  public void Var052() {
	  testGetCharacterStream(rs37_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var053() {
	  testGetCharacterStream(rs37_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var054() {
    testGetCharacterStream(rs37_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var055() {
	  testGetCharacterStream(rs37_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var056() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs37_, "C_XML", 8, ROW8_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs37_, "C_XML", 8, ROW8_S_, null);

      }
  }

  public void Var057() {
      notApplicable("No row 11"); 
      /* testGetCharacterStream(rs37_, "C_XML", 11, ROW11_S_, null); */ 
  }

  public void Var058() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1208_, "C_XML", 1, ROW1_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1208_, "C_XML", 1, ROW1_S_, null);

      }
  }

  public void Var059() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1208_, "C_XML", 2, ROW2_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1208_, "C_XML", 2, ROW2_S_NODCL_, null);

      }
  }

  public void Var060() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1208_, "C_XML", 3, ROW3_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1208_, "C_XML", 3, ROW2_S_NODCL_, null);

      }
  }

  public void Var061() {
	  testGetCharacterStream(rs1208_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var062() {
      testGetCharacterStream(rs1208_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var063() {
    testGetCharacterStream(rs1208_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var064() {
	  testGetCharacterStream(rs1208_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var065() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1208_, "C_XML", 8, ROW8_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1208_, "C_XML", 8, ROW8_S_, null);

      }
  }

  public void Var066() {
      testGetCharacterStream(rs1208_, "C_XML", 9, ROW9_S_, null);
  }

  public void Var067() {
      notApplicable("No Row 11"); 
      /* testGetCharacterStream(rs1208_, "C_XML", 11, ROW11_S_, null); */ 
  }

  public void Var068() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1200_, "C_XML", 1, ROW1_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1200_, "C_XML", 1, ROW1_S_, null);

      }
  }

  public void Var069() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1200_, "C_XML", 2, ROW2_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1200_, "C_XML", 2, ROW2_S_NODCL_, null);

      }
  }

  public void Var070() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1200_, "C_XML", 3, ROW3_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1200_, "C_XML", 3, ROW2_S_NODCL_, null);

      }
  }

  public void Var071() {
	  testGetCharacterStream(rs1200_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var072() {
      testGetCharacterStream(rs1200_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var073() {
    testGetCharacterStream(rs1200_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var074() {
	  testGetCharacterStream(rs1200_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var075() {
      if (JDTestDriver.isLUW()) {
	  testGetCharacterStream(rs1200_, "C_XML", 8, ROW8_S_LUW_, null);
      } else {
	  testGetCharacterStream(rs1200_, "C_XML", 8, ROW8_S_, null);

      }
  }

  public void Var076() {
      testGetCharacterStream(rs1200_, "C_XML", 9, ROW9_S_, null);
  }

  public void Var077() {
      notApplicable("No Row 11"); 
      /* testGetCharacterStream(rs1200_, "C_XML", 11, ROW11_S_, null); */ 
  }

  public void Var078() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 1, ROW1_S_, null);
  }

  public void Var079() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 2, ROW2_S_NODCL_, null);
  }

  public void Var080() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 3, ROW3_S_LUW_, null);
  }

  public void Var081() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var082() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var083() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var084() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var085() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 8, ROW8_S_, null);
  }

  public void Var086() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 9, ROW9_S_, null);
  }

  public void Var087() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
      testGetCharacterStream(rs930_, "C_XML", 11, ROW1_S_, null);
  }

  public void testGetString(ResultSet rs, String column, int row,
      String expected, String expectedException) {

    if (checkJdbc40() && checkXmlSupport()) {
      try {
        message.setLength(0);
        rs = rsAbsolute(rs, row);

        Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
        String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
        boolean passed = expected.equals(s);
        if (!passed) {
          message.append("\nGot     : " + s);
          message.append("\n        : " + JDTestUtilities.dumpBytes(s));
          message.append("\nExpected: " + expected);
          message.append("\n        : " + JDTestUtilities.dumpBytes(expected));
        }
        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition(passed, "Comparision failed :" + message.toString());
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '" + message1 + "' should contain '"
                  + expectedException + "'");
        } else {
          failed(e, "Unexpected Exception");

        }
      }
    }

  }

  /**
   * getString() - CCSID37 and lobs of various sizes
   */
  public void Var088() {
      if (JDTestDriver.isLUW()) {
	  testGetString(rs37_, "C_XML", 1, ROW1_S_LUW_, null);
      } else {
	  testGetString(rs37_, "C_XML", 1, ROW1_S_, null);
      } 
  }

  public void Var089() {
      if (JDTestDriver.isLUW()) {
	  testGetString(rs37_, "C_XML", 2, ROW2_S_LUW_, null);
      } else { 
	  testGetString(rs37_, "C_XML", 2, ROW2_S_NODCL_, null);
      }
  }

  public void Var090() {
      if (JDTestDriver.isLUW()) {
	  testGetString(rs37_, "C_XML", 3, ROW3_S_LUW_, null);
      } else {
	  testGetString(rs37_, "C_XML", 3, ROW3_S_LUW_, null);
      } 
  }

  public void Var091() {
    testGetString(rs37_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var092() {
    testGetString(rs37_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var093() {
    testGetString(rs37_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var094() {
    testGetString(rs37_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var095() {
      if (JDTestDriver.isLUW()) {
	  testGetString(rs37_, "C_XML", 8, ROW8_S_LUW_, null);
      } else {
	  testGetString(rs37_, "C_XML", 8, ROW8_S_, null);
      }
  }

  public void Var096() {
      notApplicable("Row 11");
      /*    testGetString(rs37_, "C_XML", 11, ROW11_S_, null); */ 
  }

  public void Var097() {
      if (JDTestDriver.isLUW()) { 
	  testGetString(rs1208_, "C_XML", 1, ROW1_S_LUW_, null);
      } else {
	  testGetString(rs1208_, "C_XML", 1, ROW1_S_, null);

      }
  }

  public void Var098() {
      if (JDTestDriver.isLUW()) { 
	  testGetString(rs1208_, "C_XML", 2, ROW2_S_LUW_, null);
      } else {
	  testGetString(rs1208_, "C_XML", 2, ROW2_S_NODCL_, null);

      }
  }

  public void Var099() {
      if (JDTestDriver.isLUW()) { 
	  testGetString(rs1208_, "C_XML", 3, ROW3_S_LUW_, null);
      } else {
	  testGetString(rs1208_, "C_XML", 3, ROW3_S_LUW_, null);

      }
  }

  public void Var100() {
    testGetString(rs1208_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var101() {
    testGetString(rs1208_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var102() {
    testGetString(rs1208_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var103() {
    testGetString(rs1208_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var104() {
      if (JDTestDriver.isLUW()) { 
    testGetString(rs1208_, "C_XML", 8, ROW8_S_LUW_, null);
      } else {
    testGetString(rs1208_, "C_XML", 8, ROW8_S_, null);

      }
  }

  public void Var105() {
    testGetString(rs1208_, "C_XML", 9, ROW9_S_, null);
  }

  public void Var106() {
      notApplicable("no row 11"); 
      /* testGetString(rs1208_, "C_XML", 11, ROW11_S_, null); */ 
  }

  public void Var107() {
      if (JDTestDriver.isLUW()) { 
    testGetString(rs1200_, "C_XML", 1, ROW1_S_LUW_, null);
      } else {
    testGetString(rs1200_, "C_XML", 1, ROW1_S_, null);

      }
  }

  public void Var108() {
      if (JDTestDriver.isLUW()) { 
    testGetString(rs1200_, "C_XML", 2, ROW2_S_LUW_, null);
      } else {
    testGetString(rs1200_, "C_XML", 2, ROW2_S_NODCL_, null);

      }
  }

  public void Var109() {
      if (JDTestDriver.isLUW()) { 
    testGetString(rs1200_, "C_XML", 3, ROW3_S_LUW_, null);
      } else {
    testGetString(rs1200_, "C_XML", 3, ROW3_S_LUW_, null);

      }
  }

  public void Var110() {
    testGetString(rs1200_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var111() {
    testGetString(rs1200_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var112() {
    testGetString(rs1200_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var113() {
    testGetString(rs1200_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var114() {
      if (JDTestDriver.isLUW()) { 
    testGetString(rs1200_, "C_XML", 8, ROW8_S_LUW_, null);
      } else {
    testGetString(rs1200_, "C_XML", 8, ROW8_S_, null);

      }
  }

  public void Var115() {
    testGetString(rs1200_, "C_XML", 9, ROW9_S_, null);
  }

  public void Var116() {
      notApplicable("No Row 11"); 
      /* testGetString(rs1200_, "C_XML", 11, ROW11_S_, null); */ 
  }

  public void Var117() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }

    testGetString(rs930_, "C_XML", 1, ROW1_S_, null);
  }

  public void Var118() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 2, ROW2_S_NODCL_, null);
  }

  public void Var119() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 3, ROW3_S_LUW_, null);
  }

  public void Var120() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 4, ROW4_S_, null);
  }

  public void Var121() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 5, ROW5_S_, null);
  }

  public void Var122() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 6, ROW6_S_, null);
  }

  public void Var123() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 7, ROW7_S_T_, null);
  }

  public void Var124() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 8, ROW8_S_, null);
  }

  public void Var125() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 9, ROW9_S_, null);
  }

  public void Var126() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 case ");
	  return;
      }
    testGetString(rs930_, "C_XML", 11, ROW1_S_, null);
  }

  public boolean checkExpected(String output, Object expected,
      StringBuffer message1) {
    boolean passed = false;
    String expectedString = null;
    String[] expectedArray = null;
    if (expected instanceof String) {
      expectedString = (String) expected;
      passed = expectedString.equals(output);
    } else {
      expectedArray = (String[]) expected;
      for (int i = 0; i < expectedArray.length; i++) {
        if (expectedArray[i].equals(output)) {
          passed = true;
        }
      }
    }

    if (!passed) {
      message1.append("\nGot     : " + output);
      if (expectedString != null) {
        message1.append("\nExpected: " + expectedString);

      } else {
        if (expectedArray != null) {
          for (int i = 0; i < expectedArray.length; i++) {
            message1.append("\nExpected: " + expectedArray[i]);
          }
	  message1.append("\n"); 
        }
      }
    }
    return passed;
  }

  public void testGetSource(ResultSet rs, String column, int row,
      String sourceClassName, Object expected, Object expectedException) {
    String columnData = "Not retrieved";
    if (checkJdbc40() && checkXmlSupport()) {
      try {
        String classname = sourceClassName;
        Class<?> sourceClass = null;
        if (classname != null) {
          if (classname.indexOf("JAVAX") == 0) {
            classname = "javax" + sourceClassName.substring(5);
          }
          sourceClass = Class.forName(classname);
        }
        message.setLength(0);
        rs = rsAbsolute(rs, row);

        columnData = rs.getString(column);
        Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);

	output_.println("sourceClass is "+sourceClass); 
        Object source = JDReflectionUtil.callMethod_O(xml, "getSource", Class
            .forName("java.lang.Class"), sourceClass);

        boolean passed = false;
        //
        // This generic code with an identity transformer should handle
        // any case. To get other cases, use JAVAX as the class
        //
        String output = "";
        if (sourceClassName == null
            || sourceClassName.equals("javax.xml.transform.stax.StAXSource")
            || sourceClassName.equals("javax.xml.transform.sax.SAXSource")
            || sourceClassName
                .equals("javax.xml.transform.stream.StreamSource")
            || sourceClassName.equals("javax.xml.transform.dom.DOMSource")) {

          TransformerFactory factory = TransformerFactory.newInstance();
          Transformer transformer = factory.newTransformer();
          transformer.setErrorListener(errorListener);
	  if (source == null) {
	      throw new Exception("ERROR:  source is null"); 
	  }
	  
	  transformer.transform((Source) source, new StreamResult(
								      "/tmp/JDXMLXML.xml"));
	  
          BufferedReader br = new BufferedReader(new FileReader(
              "/tmp/JDXMLXML.xml"));
          output = "";
          String next = br.readLine();
          while (next != null) {
            output += next;
            next = br.readLine();
          }
          br.close(); 
          passed = checkExpected(output, expected, message);

        } else if (sourceClassName.equals("JAVAX.xml.transform.dom.DOMSource")) {
          Object document = JDReflectionUtil.callMethod_O(source, "getNode");
          output = JDXMLUtil.documentToString(document);

          passed = checkExpected(output, expected, message);
        } else if (sourceClassName.equals("JAVAX.xml.transform.sax.SAXSource")) {

          Object inputSource = JDReflectionUtil.callMethod_O(source,
              "getInputSource");
          if (inputSource == null)
            throw new Exception("inputSource is null ");
          Reader reader = (Reader) JDReflectionUtil.callMethod_O(inputSource,
              "getCharacterStream");
	  if (reader == null)
	      throw new Exception("reader from inputSource '"+inputSource+"' is null ");
          BufferedReader br = new BufferedReader(reader);
          output = "";
          String next = br.readLine();
          while (next != null) {
            output += next;
            next = br.readLine();
          }
          passed = checkExpected(output, expected, message);
        } else if (sourceClassName
            .equals("JAVAX.xml.transform.stax.StAXSource")) {

          TransformerFactory factory = TransformerFactory.newInstance();
          Transformer transformer = factory.newTransformer();
          transformer.transform((Source) source, new StreamResult(
              "/tmp/JDXMLXML.xml"));
          BufferedReader br = new BufferedReader(new FileReader(
              "/tmp/JDXMLXML.xml"));
          output = "";
          String next = br.readLine();
          while (next != null) {
            output += next;
            next = br.readLine();
          }
          br.close(); 
          passed = checkExpected(output, expected, message);

        } else if (sourceClassName
            .equals("JAVAX.xml.transform.stream.StreamSource")) {

          Reader reader = (Reader) JDReflectionUtil.callMethod_O(source,
              "getReader");
          if (reader == null)
            throw new Exception("reader from source '"+source+"' is null ");
          BufferedReader br = new BufferedReader(reader);

          String next = br.readLine();
          while (next != null) {
            output += next;
            next = br.readLine();
          }
          passed = checkExpected(output, expected, message);


        } else if (sourceClassName.equals("java.lang.String")) {
	    message.append("java.lang.String handled? source = "+source+"\n");
	    if (source != null) { 
		message.append("source.classname="+source.getClass().getName()); 
	    }

	} else {
          passed = false;
          message.append("Could not handle class " + sourceClassName+" show have thrown earlier exception");
        }
        if (expectedException != null) {
          failed("Did not get expected exception '" + expectedException
              + "' : got  output = '" + output + "' : message : " + message.toString());
        } else {

          assertCondition(passed, "Comparision failed :" + message.toString());
        }
      } catch (Throwable e) {
        if (expectedException != null) {
          String exceptionMessage = e.getClass().getName() +":"+e.toString();
          boolean passed;
          if (expectedException instanceof String) {
            passed = exceptionMessage.indexOf((String) expectedException) >= 0;
            assertCondition(passed, "Got exception '" + exceptionMessage
                + "' expected to find contain '" + expectedException + "'");
          } else {
            String notFound = "";
            passed = false;
            String expectedArray[] = (String[]) expectedException;
            for (int i = 0; i < expectedArray.length; i++) {
              if (exceptionMessage.indexOf(expectedArray[i]) >= 0) {
                passed = true;
              } else {
                notFound += "\nGot exception '" + exceptionMessage
                    + "' expected to find contain '" + expectedArray[i] + "'";

              }
            }
            assertCondition(passed, notFound);

          }
          if (!passed) {
            output_.println("Column data is '" + columnData + "'");
            e.printStackTrace();
          }
        } else {
          output_.println("Column data is '" + columnData + "'");

          failed(e, "Unexpected Exception");
        }
      }
    }

  }

  /**
   * getSource() - CCSID37 and lobs of various sizes and different sources
   */

  public void Var127() {
    String expected[] =
      { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };

    testGetSource(rs37_, "C_XML", 4, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var128() {
    String expected[] =
      { ROW5_S1208_T_, /* 169P */
      ROW5_S1208_T_STANDALONE_, ROW5_S37_T_STANDALONE_ /* 16 */
      };
    testGetSource(rs37_, "C_XML", 5, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var129() {
      String expected[] = {
	  ROW8_S_SOURCEOUTPUT_,
	  ROW8_S_SAXSOURCEOUTPUT_,
      }; 
    testGetSource(rs37_, "C_XML", 8, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var130() {
      String expected[] = {
	  ROW8_S_SOURCEOUTPUT_,
	  ROW8_S_SAXSOURCEOUTPUT_,
      }; 

    testGetSource(rs37_, "C_XML", 2, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var131() {
    testGetSource(rs37_, "C_XML", 4, "javax.xml.transform.sax.SAXSource",
        ROW4_S1208_, null);
  }

  public void Var132() {
    testGetSource(rs37_, "C_XML", 5, "javax.xml.transform.sax.SAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var133() {
    testGetSource(rs37_, "C_XML", 8, "javax.xml.transform.sax.SAXSource",
        ROW8_S_SAXSOURCEOUTPUT_, null);
  }

  public void Var134() {
    notApplicable("Row 10 test");
  }

  public void Var135() {
    testGetSource(rs37_, "C_XML", 4, "javax.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var136() {
    testGetSource(rs37_, "C_XML", 5, "javax.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var137() {
    testGetSource(rs37_, "C_XML", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S_SAXSOURCEOUTPUT_, null);
  }

  public void Var138() {
      notApplicable("Row 10");
  }

  public void Var139() {
    testGetSource(rs37_, "C_XML", 4, "javax.xml.transform.stream.StreamSource",
        ROW4_S1208_, null);
  }

  public void Var140() {
    testGetSource(rs37_, "C_XML", 5, "javax.xml.transform.stream.StreamSource",
        ROW5_S1208_T_, null);
  }

  public void Var141() {
    testGetSource(rs37_, "C_XML", 8, "javax.xml.transform.stream.StreamSource",
        ROW8_S1208_, null);
  }

  public void Var142() {
      notApplicable("No Row 10"); 
      /* testGetSource(rs37_, "C_XML", 10,
        "javax.xml.transform.stream.StreamSource", ROW2_S_,
        "Content is not allowed in prolog"); */ 
  }

  public void Var143() {
    testGetSource(rs37_, "C_XML", 4, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var144() {
    testGetSource(rs37_, "C_XML", 5, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var145() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to org.xml.sax.InputSource");
	  return;
      }
    testGetSource(rs37_, "C_XML", 4, "JAVAX.xml.transform.sax.SAXSource",
        ROW4_S_, null);
  }

  public void Var146() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to org.xml.sax.InputSource");
	  return;
      }

    testGetSource(rs37_, "C_XML", 5, "JAVAX.xml.transform.sax.SAXSource",
        ROW5_S_, null);
  }

  public void Var147() {
    testGetSource(rs37_, "C_XML", 4, "JAVAX.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var148() {
    testGetSource(rs37_, "C_XML", 5, "JAVAX.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var149() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to javax.xml.transform.stream.StreamSource");
	  return;
      }

    testGetSource(rs37_, "C_XML", 4, "JAVAX.xml.transform.stream.StreamSource",
        ROW4_S_, null);
  }

  public void Var150() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to javax.xml.transform.stream.StreamSource");
	  return;
      }
    testGetSource(rs37_, "C_XML", 5, "JAVAX.xml.transform.stream.StreamSource",
        ROW5_S_, null);
  }

  public void Var151() {
    testGetSource(rs37_, "C_XML", 4, null, ROW4_S1208_, null);
  }

  public void Var152() {
    testGetSource(rs37_, "C_XML", 5, null, ROW5_S1208_T_, null);
  }

  public void Var153() {
      if (JDTestDriver.isLUW()) {

	  notApplicable("LUW returns StreamSourcefor java.lang.String");
	  return; 
      }
	  testGetSource(rs37_, "C_XML", 5, "java.lang.String", ROW5_S37_,
			"FeatureNotSupportedException");

  }

  public void Var154() {
    String expected[] =
      { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };
    testGetSource(rs1208_, "C_XML", 4, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var155() {
    String expected[] =
      { ROW5_S1208_T_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };
    testGetSource(rs1208_, "C_XML", 5, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var156() {
      String expected[] = {
	  ROW8_S1208_STANDALONE_,
	  ROW8_S1208_,
      }; 

    testGetSource(rs1208_, "C_XML", 8, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var157() {
      String expected[] = {
	  ROW2_S1208_STANDALONE_,
	  ROW2_S1208_,
      } ;
    testGetSource(rs1208_, "C_XML", 2, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var158() {
    testGetSource(rs1208_, "C_XML", 4, "javax.xml.transform.sax.SAXSource",
        ROW4_S1208_, null);
  }

  public void Var159() {
    testGetSource(rs1208_, "C_XML", 5, "javax.xml.transform.sax.SAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var160() {
    testGetSource(rs1208_, "C_XML", 8, "javax.xml.transform.sax.SAXSource",
        ROW8_S1208_, null); 
  }

  public void Var161() {
    testGetSource(rs1208_, "C_XML", 10, "javax.xml.transform.sax.SAXSource",
        ROW10_S1208_, null);
  }

  public void Var162() {
    testGetSource(rs1208_, "C_XML", 4, "javax.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var163() {
    testGetSource(rs1208_, "C_XML", 5, "javax.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var164() {
    testGetSource(rs1208_, "C_XML", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S1208_, null);
  }

  public void Var165() {
    testGetSource(rs1208_, "C_XML", 10, "javax.xml.transform.stax.StAXSource",
            ROW10_S1208_, null);
  }

  public void Var166() {
    testGetSource(rs1208_, "C_XML", 4,
        "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null);
  }

  public void Var167() {
    testGetSource(rs1208_, "C_XML", 5,
        "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null);
  }

  public void Var168() {
    testGetSource(rs1208_, "C_XML", 8,
        "javax.xml.transform.stream.StreamSource", ROW8_S1208_,
        null);
  }

  public void Var169() {
    testGetSource(rs1208_, "C_XML", 10,
        "javax.xml.transform.stream.StreamSource", ROW10_S1208_,
        null);
  }

  public void Var170() {

    testGetSource(rs1208_, "C_XML", 4, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var171() {
    testGetSource(rs1208_, "C_XML", 5, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var172() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to org.xml.sax.InputSource");
	  return;
      }

    testGetSource(rs1208_, "C_XML", 4, "JAVAX.xml.transform.sax.SAXSource",
        ROW4_S_, null);
  }

  public void Var173() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to org.xml.sax.InputSource");
	  return;
      }

    testGetSource(rs1208_, "C_XML", 5, "JAVAX.xml.transform.sax.SAXSource",
        ROW5_S_, null);
  }

  public void Var174() {
    testGetSource(rs1208_, "C_XML", 4, "JAVAX.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var175() {
    testGetSource(rs1208_, "C_XML", 5, "JAVAX.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var176() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to javax.xml.transform.stream.StreamSource");
	  return;
      }
    testGetSource(rs1208_, "C_XML", 4,
        "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);
  }

  public void Var177() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to javax.xml.transform.stream.StreamSource");
	  return;
      }
    testGetSource(rs1208_, "C_XML", 5,
        "JAVAX.xml.transform.stream.StreamSource", ROW5_S_, null);
  }

  public void Var178() {
    testGetSource(rs1208_, "C_XML", 4, null, ROW4_S1208_, null);
  }

  public void Var179() {
    testGetSource(rs1208_, "C_XML", 5, null, ROW5_S1208_T_, null);
  }

  public void Var180() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW returns StreamSourcefor java.lang.String");
	  return; 
      }
      testGetSource(rs1208_, "C_XML", 5, "java.lang.String", ROW5_S1208_,
			"FeatureNotSupportedException");
  }

  public void Var181() {
    String expected[] =
      { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };
    testGetSource(rs1200_, "C_XML", 4, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var182() {
    String expected[] =
      { ROW5_S1208_T_, /* 169P */
      ROW5_S1200_STANDALONE_, /* 16 */
      ROW5_S1208_STANDALONE_ /* LUW */ 
      };
    testGetSource(rs1200_, "C_XML", 5, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var183() {
      String expected[] = {
	  ROW8_S1208_STANDALONE_,
	  ROW8_S1208_
      };
    testGetSource(rs1200_, "C_XML", 8, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var184() {
      String expected[] = {
	  ROW2_S1208_STANDALONE_,
	  ROW2_S1208_
      }; 
    testGetSource(rs1200_, "C_XML", 2, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var185() {
    testGetSource(rs1200_, "C_XML", 4, "javax.xml.transform.sax.SAXSource",
        ROW4_S1208_, null);
  }

  public void Var186() {
    testGetSource(rs1200_, "C_XML", 5, "javax.xml.transform.sax.SAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var187() {
    testGetSource(rs1200_, "C_XML", 8, "javax.xml.transform.sax.SAXSource",
        ROW8_S1208_, null);
  }

  public void Var188() {
    testGetSource(rs1200_, "C_XML", 10, "javax.xml.transform.sax.SAXSource",
        ROW10_S1208_, null);
  }

  public void Var189() {
    testGetSource(rs1200_, "C_XML", 4, "javax.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var190() {
    testGetSource(rs1200_, "C_XML", 5, "javax.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var191() {
    testGetSource(rs1200_, "C_XML", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S1208_, null);
  }

  public void Var192() {
  
    testGetSource(rs1200_, "C_XML", 10, "javax.xml.transform.stax.StAXSource",
            ROW10_S1208_, null);
  }

  public void Var193() {
    testGetSource(rs1200_, "C_XML", 4,
        "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null);
  }

  public void Var194() {
    testGetSource(rs1200_, "C_XML", 5,
        "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null);
  }

  public void Var195() {
    testGetSource(rs1200_, "C_XML", 8,
        "javax.xml.transform.stream.StreamSource", ROW8_S1208_,
        null);
  }

  public void Var196() {
    testGetSource(rs1200_, "C_XML", 10,
        "javax.xml.transform.stream.StreamSource", ROW10_S1208_,
		  null);
  }

  public void Var197() {
    testGetSource(rs1200_, "C_XML", 4, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var198() {

    testGetSource(rs1200_, "C_XML", 5, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var199() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to org.xml.sax.InputSource");
	  return;
      }
    testGetSource(rs1200_, "C_XML", 4, "JAVAX.xml.transform.sax.SAXSource",
        ROW4_S_, null);
  }

  public void Var200() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to org.xml.sax.InputSource");
	  return;
      }

    testGetSource(rs1200_, "C_XML", 5, "JAVAX.xml.transform.sax.SAXSource",
            ROW5_S_, null);
  }

  public void Var201() {
    testGetSource(rs1200_, "C_XML", 4, "JAVAX.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var202() {
    testGetSource(rs1200_, "C_XML", 5, "JAVAX.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var203() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to javax.xml.transform.stream.StreamSource");
	  return;
      }

    testGetSource(rs1200_, "C_XML", 4,
        "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);
  }

  public void Var204() {

      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW does not provide characterStream to javax.xml.transform.stream.StreamSource");
	  return;
      }

    testGetSource(rs1200_, "C_XML", 5,
        "JAVAX.xml.transform.stream.StreamSource", ROW5_S_, null);
  }

  public void Var205() {
    testGetSource(rs1200_, "C_XML", 4, null, ROW4_S1208_, null);
  }

  public void Var206() {
    testGetSource(rs1200_, "C_XML", 5, null, ROW5_S1208_T_, null);
  }

  public void Var207() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("LUW returns StreamSourcefor java.lang.String");
	  return; 
      }

    testGetSource(rs1200_, "C_XML", 5, "java.lang.String", ROW5_S1200_,
        "FeatureNotSupportedException");
  }

  public void Var208() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 

    String[] expected =
      { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };

    testGetSource(rs930_, "C_XML", 4, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  // The rs930, row 5, variation files because getBinaryStream returns the data
  // in CCSID 930, which is not
  // handled by the DOM Parser.
  // The following test (run above verifies that CCSID 930 data is returned
  // testGetBinaryStream(rs930_, 5, ROW5_930_, null
  public void Var209() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
      String[] expected = {
	  ROW5_S1208_T_STANDALONE_,
	  ROW5_S1208_T_
      }; 
    testGetSource(rs930_, "C_XML", 5, "javax.xml.transform.dom.DOMSource",
            expected, null);  //ok now "XML PARSING ERROR");
  }

  public void Var210() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
      String[] expected = {
	  ROW8_S1208_STANDALONE_,
	  ROW8_S1208_
      };
    testGetSource(rs930_, "C_XML", 8, "javax.xml.transform.dom.DOMSource",
            expected, null);//"XML PARSING ERROR");
  }

  public void Var211() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
      String[] expected = {
	  ROW2_S1208_STANDALONE_,
	  ROW2_S1208_,
      };
    testGetSource(rs930_, "C_XML", 2, "javax.xml.transform.dom.DOMSource",
            expected, null);
  }

  public void Var212() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4, "javax.xml.transform.sax.SAXSource",
        ROW4_S1208_, null);
  }

  public void Var213() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5, "javax.xml.transform.sax.SAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var214() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 8, "javax.xml.transform.sax.SAXSource",
            ROW8_S1208_, null);//"Content is not allowed in prolog");
  }

  public void Var215() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 10, "javax.xml.transform.sax.SAXSource",
            ROW10_S1208_, null);// "Content is not allowed in prolog");
  }

  public void Var216() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4, "javax.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var217() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5, "javax.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var218() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 8, "javax.xml.transform.stax.StAXSource",
            ROW8_S1208_, null);//expected);
  }

  public void Var219() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 2, "javax.xml.transform.stax.StAXSource",
            ROW2_S1208_, null);// expected);
  }

  public void Var220() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4,
        "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null);
  }

  public void Var221() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5,
        "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null);
  }

  public void Var222() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 8,
        "javax.xml.transform.stream.StreamSource", ROW8_S1208_, null);
       // "Content is not allowed in prolog");
  }

  public void Var223() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 10,
        "javax.xml.transform.stream.StreamSource", ROW10_S1208_, null);
       // "Content is not allowed in prolog");
  }

  public void Var224() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  // The rs930, row 5, variation files because getBinaryStream returns the data
  // in CCSID 930, which is not
  // handled by the DOM Parser.
  // The following test (run above verifies that CCSID 930 data is returned
  // testGetBinaryStream(rs930_, 5, ROW5_930_, null
  public void Var225() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
      String[] expected = {
	  ROW5_S1200_T_NL,
	  ROW5_S1200_T_,
	  ROW5_S_,
      } ; 
    testGetSource(rs930_, "C_XML", 5, "JAVAX.xml.transform.dom.DOMSource",
        expected, null);//"XML PARSING ERROR");  //seems to handle 930 now
  }

  public void Var226() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4, "JAVAX.xml.transform.sax.SAXSource",
        ROW4_S_, null);
  }

  public void Var227() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5, "JAVAX.xml.transform.sax.SAXSource",
            ROW5_S_, null);
  }

  public void Var228() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4, "JAVAX.xml.transform.stax.StAXSource",
        ROW4_S1208_, null);
  }

  public void Var229() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5, "JAVAX.xml.transform.stax.StAXSource",
        ROW5_S1208_T_, null);
  }

  public void Var230() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4,
        "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);
  }

  public void Var231() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5,
        "JAVAX.xml.transform.stream.StreamSource", ROW5_S_, null);
  }

  public void Var232() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 4, null, ROW4_S1208_, null);
  }

  public void Var233() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5, null, ROW5_S1208_T_, null);
  }

  public void Var234() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testGetSource(rs930_, "C_XML", 5, "java.lang.String", ROW5_S930_,
        "FeatureNotSupportedException");
  }

  public void testSetCharacterStream(String table, String column, int row,
      String writeString, String expected, String expectedException) {
    if (checkJdbc40() && checkXmlSupport()) {
      message.setLength(0);
      try {
        // Clear the row first and validate that is was cleared
        
        rs2Query_="SELECT * FROM " + table;
        rs2_ = statement2_.executeQuery(rs2Query_);
        rs2_ = rsAbsolute(rs2_, row);
        rs2_.updateString(1, "<T/>");
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_ = rsAbsolute(rs2_, row);
        String clearString = rs2_.getString(column);
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_ = rsAbsolute(rs2_, row);
        Object sqlxml;
        if (row % 2 == 0) {
          sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        } else {
          sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        }

        Writer v = (Writer) JDReflectionUtil.callMethod_O(sqlxml,
            "setCharacterStream");
        PrintWriter writer = new PrintWriter(v);
        writer.print(writeString);
        writer.close();
        JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);
        rs2_.updateRow();
        rs2_.close();
        rs2Query_ = "SELECT * FROM " + table;
        rs2_ = statement2_.executeQuery(rs2Query_);
        rs2_ = rsAbsolute(rs2_, row);
        Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
        boolean passed = expected.equals(s);
        if (!passed) {
          message.append("\nGot     : " + s);
          message.append("\n        : " + JDTestUtilities.dumpBytes(s));
          message.append("\nExpected: " + expected);
          message.append("\n        : " + JDTestUtilities.dumpBytes(expected));
        }
        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition(clearString.length() == 4 && passed,
              "clearString.length()=" + clearString.length() + " sb 4  clearString='"+
                  clearString+"' "+
                  message);
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '" + message1 + "' should contain '"
                  + expectedException + "'");
        } else {
          failed(e, "Unexpected Exception writeString="+writeString);
        }
      }
    }
  }

  /**
   * setCharacterStream() - CCSID 37 and lobs of various sizes
   */

  public void Var235() {
    if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE37_, "C_XML", 1, ROW1_S_, ROW1_S_LUW_, null);
    
    } else { 
    testSetCharacterStream(TABLE37_, "C_XML", 1, ROW1_S_, ROW1_S_, null);
    }
  }

  public void Var236() {
    if (JDTestDriver.isLUW()) {
    testSetCharacterStream(TABLE37_, "C_XML", 2, ROW2_S_, ROW2_S_LUW_, null);
    } else {
      testSetCharacterStream(TABLE37_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
    }
  }

  public void Var237() {
    if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE37_, "C_XML", 3, ROW3_S_, ROW3_S_LUW_, null);
    } else {
      testSetCharacterStream(TABLE37_, "C_XML", 3, ROW3_S_, ROW3_S_LUW_, null);
    }
  }

  public void Var238() {
    testSetCharacterStream(TABLE37_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var239() {
    testSetCharacterStream(TABLE37_, "C_XML", 5, ROW5_S_, ROW5_S_, null);
  }

  public void Var240() {
    testSetCharacterStream(TABLE37_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var241() {
    testSetCharacterStream(TABLE37_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var242() {
    if (JDTestDriver.isLUW() ) {
      testSetCharacterStream(TABLE37_, "C_XML", 8, ROW8_S_, ROW8_S_LUW_, null);
    } else { 
      testSetCharacterStream(TABLE37_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
      
    }
  }

  public void Var243() {
    notApplicable("No row 11"); 
      /*   testSetCharacterStream(TABLE37_, "C_XML", 11, ROW11_S_, ROW11_S_, null); */ 
  }

  public void Var244() {
      if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE1208_, "C_XML", 2, ROW2_S_, ROW2_S_LUW_, null);
      } else { 
      testSetCharacterStream(TABLE1208_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
      }
  }

  public void Var245() {
      if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE1208_, "C_XML", 3, ROW3_S1208_, ROW3_S_LUW_,
          null);
      } else { 
      testSetCharacterStream(TABLE1208_, "C_XML", 3, ROW3_S1208_, ROW3_S_LUW_,
          null);
      }
  }

  public void Var246() {
    testSetCharacterStream(TABLE1208_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var247() {
    testSetCharacterStream(TABLE1208_, "C_XML", 5, ROW5_S1208_, ROW5_S_,
        null);
  }

  public void Var248() {
    testSetCharacterStream(TABLE1208_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var249() {
    testSetCharacterStream(TABLE1208_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var250() {
    if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE1208_, "C_XML", 8, ROW8_S_, ROW8_S_LUW_, null);
      
    } else {
      testSetCharacterStream(TABLE1208_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
    }
  }

  public void Var251() {
    testSetCharacterStream(TABLE1208_, "C_XML", 9, ROW9_S_, ROW9_S_, null);
  }

  public void Var252() {
      notApplicable("No Row 11");
/* 
    if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE1208_, "C_XML", 11, ROW11_S_, ROW11_S_, "SQLCODE=-16168");
    
    } else {
	testSetCharacterStream(TABLE1208_, "C_XML", 11, ROW11_S_, ROW11_S_, "Invalid XML Exception ");
    }
*/ 
  }

  public void Var253() {
    if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE1200_, "C_XML", 2, ROW2_S_, ROW2_S_LUW_, null);
      
    } else { 
    testSetCharacterStream(TABLE1200_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
    }
  }

  public void Var254() {
    if (JDTestDriver.isLUW()) {
      testSetCharacterStream(TABLE1200_, "C_XML", 3, ROW3_S1200_, ROW3_S_LUW_,
          null);
     
    } else { 
      testSetCharacterStream(TABLE1200_, "C_XML", 3, ROW3_S1200_, ROW3_S_NODCL_,
        null);
    }
  }

  public void Var255() {
    testSetCharacterStream(TABLE1200_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var256() {
    testSetCharacterStream(TABLE1200_, "C_XML", 5, ROW5_S1200_, ROW5_S_,
        null);
  }

  public void Var257() {
    testSetCharacterStream(TABLE1200_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var258() {
    testSetCharacterStream(TABLE1200_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var259() {
    if (JDTestDriver.isLUW()){
      testSetCharacterStream(TABLE1200_, "C_XML", 8, ROW8_S_, ROW8_S_LUW_, null);
     
    } else {
    testSetCharacterStream(TABLE1200_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
    }
  }

  public void Var260() {
    testSetCharacterStream(TABLE1200_, "C_XML", 9, ROW9_S_, ROW9_S_, null);
  }

  public void Var261() {
    notApplicable("No row 11"); 

/*
    if (JDTestDriver.isLUW()){
      testSetCharacterStream(TABLE1200_, "C_XML", 11, ROW11_S_, ROW11_S_, "SQLCODE=-16168");
    
    } else { 
      testSetCharacterStream(TABLE1200_, "C_XML", 11, ROW11_S_, ROW11_S_, "Exception");
    }
*/ 
  }

  public void Var262() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 

    testSetCharacterStream(TABLE930_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
  }

  public void Var263() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 

    testSetCharacterStream(TABLE930_, "C_XML", 3, ROW3_S930_, ROW3_S_NODCL_, null);
  }

  public void Var264() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 

    testSetCharacterStream(TABLE930_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var265() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetCharacterStream(TABLE930_, "C_XML", 5, ROW5_S930_, ROW5_S_, null);
  }

  public void Var266() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetCharacterStream(TABLE930_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var267() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetCharacterStream(TABLE930_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var268() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetCharacterStream(TABLE930_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
  }

  public void Var269() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetCharacterStream(TABLE930_, "C_XML", 9, ROW9_S_, ROW9_S_, null);
  }

  public void Var270() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
    notApplicable("No row 11"); 
    /* 
    testSetCharacterStream(TABLE930_, "C_XML", 11, ROW11_S_, ROW11_S_, null);
    */ 
  }

  public void testSetString(String table, String column, int row,
      String writeString, String expected, String expectedException) {
    if (checkJdbc40() && checkXmlSupport()) {
      message.setLength(0);
      try {
        // Clear the row first and validate that is was cleared
        rs2Query_ = "SELECT * FROM " + table;
        rs2_ = statement2_.executeQuery(rs2Query_);
        rs2_ = rsAbsolute(rs2_, row);
        rs2_.updateString(1, "<T/>");
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_ = rsAbsolute(rs2_, row);
        String blankString = rs2_.getString(column);
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_ = rsAbsolute(rs2_, row);

        Object sqlxml;
        if (row % 2 == 0) {
          sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        } else {
          sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        }

        JDReflectionUtil.callMethod_V(sqlxml, "setString", writeString);
        // 
        // On LUW, you can only write once to the SQLXML
        // For testing purposes, call setString again for some rows
        // if (row % 2 == 0) {
        //  JDReflectionUtil.callMethod_V(sqlxml, "setString", writeString);
        // }

        JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_ = rsAbsolute(rs2_, row);
        Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
        boolean passed = expected.equals(s);
        if (!passed) {
          message.append("\nGot     : " + s);
          message.append("\n        : " + JDTestUtilities.dumpBytes(s));
          message.append("\nExpected: " + expected);
          message.append("\n        : " + JDTestUtilities.dumpBytes(expected));
        }
        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition(blankString.length() == 4 && passed,
              "blankString.length=" + blankString.length() + " sb 4 " + message);
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '" + message1 + "' should contain '"
                  + expectedException + "'");
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - CCSID 37 and lobs of various sizes
   */

  public void Var271() {
    if (JDTestDriver.isLUW()) {
    testSetString(TABLE37_, "C_XML", 1, ROW1_S_, ROW1_S_LUW_, null);
    } else {
      testSetString(TABLE37_, "C_XML", 1, ROW1_S_, ROW1_S_, null);
      
    }
  }

  public void Var272() {
    if (JDTestDriver.isLUW()) {
    testSetString(TABLE37_, "C_XML", 2, ROW2_S_, ROW2_S_LUW_, null);
    } else {
      testSetString(TABLE37_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
      
    }
  }
    

  public void Var273() {
    if (JDTestDriver.isLUW()) {
    testSetString(TABLE37_, "C_XML", 3, ROW3_S_, ROW3_S_LUW_, null);
    } else {
      testSetString(TABLE37_, "C_XML", 3, ROW3_S37_, ROW3_S_NODCL_, null);
      
    }
  }

  public void Var274() {
    testSetString(TABLE37_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var275() {
    testSetString(TABLE37_, "C_XML", 5, ROW5_S_, ROW5_S_, null);
  }

  public void Var276() {
    testSetString(TABLE37_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var277() {
    testSetString(TABLE37_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var278() {
    if (JDTestDriver.isLUW()) {
      testSetString(TABLE37_, "C_XML", 8, ROW8_S_, ROW8_S_LUW_, null);
    } else { 
      testSetString(TABLE37_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
    }
  }

  public void Var279() {
    notApplicable("No row 11"); 

/* 
    if (JDTestDriver.isLUW()) {
      testSetString(TABLE37_, "C_XML", 11, ROW11_S_, ROW11_S_, "SQLCODE=-16168");
    } else {
      testSetString(TABLE37_, "C_XML", 11, ROW11_S_, ROW11_S_, "16168");
    }
   */  
  }

  public void Var280() {
    if (JDTestDriver.isLUW()) {
    testSetString(TABLE1208_, "C_XML", 1, ROW1_S_, ROW1_S_LUW_, null);
    } else { 
    testSetString(TABLE1208_, "C_XML", 1, ROW1_S_, ROW1_S_, null);
    }
  }

  public void Var281() {
    if (JDTestDriver.isLUW()) {
    testSetString(TABLE1208_, "C_XML", 2, ROW2_S_, ROW2_S_LUW_, null);
    } else { 
    testSetString(TABLE1208_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
    }
  }

  public void Var282() {
    if (JDTestDriver.isLUW()) {
    testSetString(TABLE1208_, "C_XML", 3, ROW3_S1208_, ROW3_S_LUW_, null);
    } else { 
    testSetString(TABLE1208_, "C_XML", 3, ROW3_S1208_, ROW3_S_NODCL_, null);
    }
  }

  public void Var283() {
    testSetString(TABLE1208_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var284() {
    testSetString(TABLE1208_, "C_XML", 5, ROW5_S1208_, ROW5_S_, null);
  }

  public void Var285() {
    testSetString(TABLE1208_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var286() {
    testSetString(TABLE1208_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var287() {
    if (JDTestDriver.isLUW()) { 
      testSetString(TABLE1208_, "C_XML", 8, ROW8_S_, ROW8_S_LUW_, null);
    } else { 
      testSetString(TABLE1208_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
     
    }
  }

  public void Var288() {
    testSetString(TABLE1208_, "C_XML", 9, ROW9_S_, ROW9_S_, null);
  }

  public void Var289() {
    notApplicable("No row 11"); 

/* 
    testSetString(TABLE1208_, "C_XML", 11, ROW11_S_, ROW11_S_, "SQLCODE=-16168");
*/
  }

  public void Var290() {
    
    testSetString(TABLE1200_, "C_XML", 1, ROW1_S_, ROW1_S_LUW_, null);
  }

  public void Var291() {
      if (JDTestDriver.isLUW()) { 
	  testSetString(TABLE1200_, "C_XML", 2, ROW2_S_, ROW2_S_LUW_, null);
      } else {
	  testSetString(TABLE1200_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
      }
  }

  public void Var292() {
    testSetString(TABLE1200_, "C_XML", 3, ROW3_S1200_, ROW3_S_LUW_, null);
  }

  public void Var293() {
    testSetString(TABLE1200_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var294() {
    testSetString(TABLE1200_, "C_XML", 5, ROW5_S1200_, ROW5_S_, null);
  }

  public void Var295() {
    testSetString(TABLE1200_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var296() {
    testSetString(TABLE1200_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var297() {
    testSetString(TABLE1200_, "C_XML", 8, ROW8_S_, ROW8_S_LUW_, null);
  }

  public void Var298() {
    testSetString(TABLE1200_, "C_XML", 9, ROW9_S_, ROW9_S_, null);
  }

  public void Var299() {
    notApplicable("No row 11"); 

    /* 
    testSetString(TABLE1200_, "C_XML", 11, ROW11_S_, ROW11_S_, "SQLCODE=-16168");
    */ 
  }

  public void Var300() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 1, ROW1_S_, ROW1_S_, null);
  }

  public void Var301() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 2, ROW2_S_, ROW2_S_NODCL_, null);
  }

  public void Var302() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 3, ROW3_S930_, ROW3_S_NODCL_, null);
  }

  public void Var303() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 4, ROW4_S_, ROW4_S_, null);
  }

  public void Var304() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 5, ROW5_S930_, ROW5_S_, null);
  }

  public void Var305() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 6, ROW6_S_, ROW6_S_, null);
  }

  public void Var306() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 7, ROW7_S_, ROW7_S_T_, null);
  }

  public void Var307() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 8, ROW8_S_, ROW8_S_, null);
  }

  public void Var308() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetString(TABLE930_, "C_XML", 9, ROW9_S_, ROW9_S_, null);
  }

  public void Var309() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
    notApplicable("No row 11"); 
/* 
    testSetString(TABLE930_, "C_XML", 11, ROW11_S_, ROW11_S_, null);
*/ 
  }

  public void testSetBinaryStream(String table, String column, int row,
      byte[] expected, String expectedString, String expectedException) {
    if (checkJdbc40() && checkXmlSupport()) {
      message.setLength(0);
      try {
        // Clear the row first and validate that is was cleared
        rs2Query_ = "SELECT * FROM " + table;
        rs2_ = statement2_.executeQuery(rs2Query_);
        rsAbsolute(rs2_, row);
        rs2_.updateString(1, "<T/>");
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rsAbsolute(rs2_, row);
        String blankString = rs2_.getString(column);
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rsAbsolute(rs2_, row);
        Object sqlxml;
        // Change how the sqlxml object is obtained
        if (row % 2 == 0) {
          sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        } else {
          sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        }
        OutputStream v = (OutputStream) JDReflectionUtil.callMethod_O(sqlxml,
            "setBinaryStream");
        v.write(expected);
        v.close();
        JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rsAbsolute(rs2_, row);
        String outputString = rs2_.getString(column);
        boolean passed = expectedString.equals(outputString);
        if (!passed) {
          message.append("\nInput    : " + JDTestUtilities.dumpBytes(expected));
          message.append("\nGot      : " + outputString);
          message.append("\nGot      : "
              + JDTestUtilities.dumpBytes(outputString));
          message.append("\nExpected : " + expectedString);
          message.append("\nExpected : "
              + JDTestUtilities.dumpBytes(expectedString));
        }

        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition(blankString.length() == 4 && passed,
              "blankString.length=" + blankString.length() + " sb 4 " + message);
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '" + message1 + "' should contain '"
                  + expectedException + "'");
        } else {
          message.append("\nInput    : " + JDTestUtilities.dumpBytes(expected));
          message.append("\nInput    : " + JDTestUtilities.dumpBytesAsAscii(expected));
          
          failed(e, "Unexpected Exception "+message.toString());
        }
      }
    }
  }

  /**
   * setBinaryStream() - CCSID 37 and lobs of various sizes
   */

  public void Var310() {
    // ROW1_ is a blank array
    if (JDTestDriver.isLUW()) { 
      testSetBinaryStream(TABLE37_, "C_XML", 1, ROW1_, ROW1_S_,
      "SQLCODE=-16132");
      
    } else { 
       testSetBinaryStream(TABLE37_, "C_XML", 1, ROW1_, ROW1_S_,
           "Data type mismatch");
    }
  }

  public void Var311() {
    if (JDTestDriver.isLUW()) { 
	testSetBinaryStream(TABLE37_, "C_XML", 2, ROW2_37_, ROW2_S_NODCL_, null);
    } else {
	testSetBinaryStream(TABLE37_, "C_XML", 2, ROW2_37_, ROW2_S_NODCL_, null);
      
    }
  }

  public void Var312() {
    if (JDTestDriver.isLUW()) { 
      // SQL16168NXML document contained an invalid XML declaration. Reason code = reason-code.
      // Explanation:
      //  While processing an XML document or XML schema the XML parser encountered a missing 
      // or invalid XML declaration. The reason-code indicates which of the following conditions
      // was found. 
      // 7  The specified document encoding was invalid or contradicts the automatically 
      //    sensed encoding.
        
    testSetBinaryStream(TABLE37_, "C_XML", 3, ROW3_37_, ROW3_S_NODCL_, null /*
        "SQLCODE=-16168, SQLSTATE=2200M, SQLERRMC=7" */ );
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 3, ROW3_37_, ROW3_S_NODCL_, null);
      
    }
  }

  public void Var313() {
    if (JDTestDriver.isLUW()) { 
    testSetBinaryStream(TABLE37_, "C_XML", 4, ROW4_37_, ROW4_S_,null/* "SQLCODE=-16103" */);
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 4, ROW4_37_, ROW4_S_, null);
      
    }
  }

  public void Var314() {
    if (JDTestDriver.isLUW()) { 
    testSetBinaryStream(TABLE37_, "C_XML", 5, ROW5_37_, ROW5_S_, 
        null /* "SQLCODE=-16168, SQLSTATE=2200M, SQLERRMC=7" */ );
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 5, ROW5_37_, ROW5_S_, null);
      
    }
  }

  public void Var315() {
    if (JDTestDriver.isLUW()) { 
    testSetBinaryStream(TABLE37_, "C_XML", 6, ROW6_37_, ROW6_S_, null /* "SQLCODE=-16103" */ );
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 6, ROW6_37_, ROW6_S_, null);
      
    }
  }

  public void Var316() {
    if (JDTestDriver.isLUW()) { 
	testSetBinaryStream(TABLE37_, "C_XML", 7, ROW7_37_, ROW7_S_T_, null /* "SQLCODE=-16103" */ );
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 7, ROW7_37_, ROW7_S_T_, null);
    }
  }

  public void Var317() {
    if (JDTestDriver.isLUW()) { 
    testSetBinaryStream(TABLE37_, "C_XML", 8, ROW8_37_, ROW8_S_,
        "SQLCODE=-16103");
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 8, ROW8_37_, ROW8_S_,
      "Data type mismatch");
      
    }
  }

  public void Var318() {
    notApplicable("No row 11"); 

/*
    if (JDTestDriver.isLUW()) {

    testSetBinaryStream(TABLE37_, "C_XML", 11, ROW11_, ROW11_S_, 
        "SQLCODE=-16168, SQLSTATE=2200M, SQLERRMC=7");
    } else {
      testSetBinaryStream(TABLE37_, "C_XML", 11, ROW11_, ROW11_S_, null);
      
    }
*/ 
    }

  public void Var319() {
    if (JDTestDriver.isLUW()) { 
      testSetBinaryStream(TABLE1208_, "C_XML", 1, ROW1_, ROW1_S_,
      "SQLCODE=-16132");
    } else {
      testSetBinaryStream(TABLE1208_, "C_XML", 1, ROW1_, ROW1_S_,
      "Data type mismatch");
      
    }
  }

  public void Var320() {
    if (JDTestDriver.isLUW()) {
      testSetBinaryStream(TABLE1208_, "C_XML", 2, ROW2_1208_, ROW2_S_LUW_, null);
    } else { 
      testSetBinaryStream(TABLE1208_, "C_XML", 2, ROW2_1208_, ROW2_S_NODCL_, null);
    }
  }

  public void Var321() {
    if (JDTestDriver.isLUW()) {
      testSetBinaryStream(TABLE1208_, "C_XML", 3, ROW3_1208_, ROW3_S_LUW_, null);
      
    } else {
      testSetBinaryStream(TABLE1208_, "C_XML", 3, ROW3_1208_, ROW3_S_NODCL_, null);
      
    }
  }

  public void Var322() {
    testSetBinaryStream(TABLE1208_, "C_XML", 4, ROW4_1208_, ROW4_S_, null);
  }

  public void Var323() {
    if (JDTestDriver.isLUW()) {
      testSetBinaryStream(TABLE1208_, "C_XML", 5, ROW5_1208_, ROW5_S_, null);
    } else {
      testSetBinaryStream(TABLE1208_, "C_XML", 5, ROW5_1208_, ROW5_S_, null);
    
    }
  }

  public void Var324() {
    testSetBinaryStream(TABLE1208_, "C_XML", 6, ROW6_1208_, ROW6_S_, null);
  }

  public void Var325() {
    testSetBinaryStream(TABLE1208_, "C_XML", 7, ROW7_1208_, ROW7_S_T_, null);
  }

  public void Var326() {
    testSetBinaryStream(TABLE1208_, "C_XML", 8, ROW8_1208_, ROW8_S_LUW_,
        null);
  }

  public void Var327() {
    testSetBinaryStream(TABLE1208_, "C_XML", 9, ROW9_1208_, ROW9_S_, null);
  }

  public void Var328() {
    notApplicable("No row 11"); 
/* 
    if (JDTestDriver.isLUW()) {
      testSetBinaryStream(TABLE1208_, "C_XML", 11, ROW11_, ROW11_S_, "SQLCODE=-16168, SQLSTATE=2200M, SQLERRMC=7");
     
    } else { 
      testSetBinaryStream(TABLE1208_, "C_XML", 11, ROW11_, ROW11_S_, "FIX ME");
    }
*/ 
  }

  public void Var329() {

    if (JDTestDriver.isLUW()) {

      testSetBinaryStream(TABLE1200_, "C_XML", 1, ROW1_, ROW1_S_,
      "SQLCODE=-16132, SQLSTATE=2200M");
    } else {
      testSetBinaryStream(TABLE1200_, "C_XML", 1, ROW1_, ROW1_S_,
      "Data type mismatch");
   
    }

  }

  public void Var330() {
    if (JDTestDriver.isLUW()) {
    testSetBinaryStream(TABLE1200_, "C_XML", 2, ROW2_1200_, ROW2_S_LUW_, null);
    } else { 
      testSetBinaryStream(TABLE1200_, "C_XML", 2, ROW2_1200_, ROW2_S_NODCL_, null);
    }
  }

  public void Var331() {
    testSetBinaryStream(TABLE1200_, "C_XML", 3, ROW3_1200_, ROW3_S_LUW_, null);
  }

  public void Var332() {
    testSetBinaryStream(TABLE1200_, "C_XML", 4, ROW4_1200_, ROW4_S_, null);
  }

  public void Var333() {
    testSetBinaryStream(TABLE1200_, "C_XML", 5, ROW5_1200_, ROW5_S_, null);
  }

  public void Var334() {
    if (JDTestDriver.isLUW()) {
      notApplicable("LUW not handling 1200 data without prolog"); 
    } else { 
      testSetBinaryStream(TABLE1200_, "C_XML", 6, ROW6_1200_, ROW6_S_, null);
    }
  }

  public void Var335() {
    if (JDTestDriver.isLUW()) {
      notApplicable("LUW not handling 1200 data without prolog"); 
    } else { 
       testSetBinaryStream(TABLE1200_, "C_XML", 7, ROW7_1200_, ROW7_S_T_, null);
    }
  }

  public void Var336() {
    if (JDTestDriver.isLUW()) {
      testSetBinaryStream(TABLE1200_, "C_XML", 8, ROW8_1200_, ROW8_S_,
      "SQLCODE=-16132, SQLSTATE=2200M");
    } else {     
    testSetBinaryStream(TABLE1200_, "C_XML", 8, ROW8_1200_, ROW8_S_,
        "Data type mismatch");
    }
  }

  public void Var337() {
    testSetBinaryStream(TABLE1200_, "C_XML", 9, ROW9_1200_, ROW9_S_, null);
  }

  public void Var338() {
    notApplicable("No row 11"); 
/* 
    if (JDTestDriver.isLUW()) {
      testSetBinaryStream(TABLE1200_, "C_XML", 11, ROW11_, ROW11_S_, "SQLCODE=-16168, SQLSTATE=2200M, SQLERRMC=7");
    
    } else {     
    testSetBinaryStream(TABLE1200_, "C_XML", 11, ROW11_, ROW11_S_, "Should be bad encoding");
    }
*/ 
  }

  public void Var339() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 1, ROW1_, ROW1_S_,
        "Data type mismatch");
  }

  public void Var340() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 2, ROW2_930_, ROW2_S_NODCL_, null);
  }

  // Set Binary stream returning garbage
  public void Var341() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 3, ROW3_930_, ROW3_S_NODCL_, null);
  }

  public void Var342() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 4, ROW4_930_, ROW4_S_, null);
  }

  public void Var343() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 5, ROW5_930_, ROW5_S_, null);
  }

  public void Var344() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 6, ROW6_930_, ROW6_S_, null);
  }

  public void Var345() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 7, ROW7_930_, ROW7_S_T_, null);
  }

  public void Var346() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 8, ROW8_930_, ROW8_S_,
        "Data type mismatch");
  }

  public void Var347() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      } 
    testSetBinaryStream(TABLE930_, "C_XML", 9, ROW9_930_, ROW9_S_, null);
  }

  public void Var348() {
      if (JDTestDriver.isLUW()) {
	  notApplicable("CCSID 930 test");
	  return; 
      }
    notApplicable("No row 11"); 

/* 
    testSetBinaryStream(TABLE930_, "C_XML", 11, ROW11_, ROW11_S_, null);
*/ 
  }

  public void testSetResult(String table, String column, int row,
      String resultClassName, String writeString, String sourceClassName,
      Object expected, String expectedException) {
    if (checkJdbc40() && checkXmlSupport()) {
      message.setLength(0);
      try {
        // Clear the row first and validate that is was cleared
        rs2Query_ = "SELECT * FROM " + table;
        rs2_ = statement2_.executeQuery(rs2Query_);
        rsAbsolute(rs2_, row);
        rs2_.updateString(1, "<T/>");
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rsAbsolute(rs2_, row);
        String clearString = rs2_.getString(column);
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rsAbsolute(rs2_, row);
        Object sqlxml;
        if (row % 2 == 0) {
          sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        } else {
          sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        }

        Result result = (Result) JDReflectionUtil.callMethod_O(sqlxml,
            "setResult", Class.forName(resultClassName));

        //
        // Do the transform using the selected parser.
        //
        Source source = null;
        if (sourceClassName.equals("javax.xml.transform.stax.StAXSource")) {
          XMLInputFactory inputFactory = XMLInputFactory.newInstance(); 
          XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(
              new StringReader(writeString));
          source = (Source) JDReflectionUtil.createObject(
              "javax.xml.transform.stax.StAXSource",
              "javax.xml.stream.XMLStreamReader", xmlStreamReader);

        } else if (sourceClassName.equals("javax.xml.transform.sax.SAXSource")) {
          InputSource inputSource = new InputSource(new StringReader(
              writeString));
          source = new javax.xml.transform.sax.SAXSource(inputSource);

        } else if (sourceClassName
            .equals("javax.xml.transform.stream.StreamSource")) {
          source = new javax.xml.transform.stream.StreamSource(
              new StringReader(writeString));

        } else if (sourceClassName.equals("javax.xml.transform.dom.DOMSource")) {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder parser = factory.newDocumentBuilder();
          // TODO: Figure out how to get a stream from the STRING..

          org.w3c.dom.Document doc = parser.parse(new ByteArrayInputStream(
              writeString.getBytes()));
          source = new DOMSource(doc);

        } else {
          throw new Exception("Unrecognized sourceClassName:" + sourceClassName);
        }

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setErrorListener(errorListener);
        transformer.transform(source, result);

        JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rsAbsolute(rs2_, row);
        Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
        boolean passed;
        if (expected instanceof String) {
          passed = expected.equals(s);
        } else {
          passed = false;
          String[] expectedArray = (String[]) expected;
          for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i].equals(s)) {
              passed = true;
            }
          }
        }
        if (!passed) {
          message.append("\nGot     : " + s);
          message.append("\n        : " + JDTestUtilities.dumpBytes(s));
          if (expected instanceof String) {
            message.append("\nExpected: " + expected);
            message.append("\n        : "
                + JDTestUtilities.dumpBytes((String) expected));
          } else {
            String[] expectedArray = (String[]) expected;
            for (int i = 0; i < expectedArray.length; i++) {
              message.append("\nExpected: " + expectedArray[i]);
              message.append("\n        : "
                  + JDTestUtilities.dumpBytes(expectedArray[i]));

            }
          }
        }
        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition(clearString.length() == 4 && passed,
              "clearString.length()=" + clearString.length() + " sb 4 "
                  + message);
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '" + message1 + "' should contain '"
                  + expectedException + "'");

        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  // DOM Results currently not supported

  public void Var349() {
    testSetResult(TABLE37_, "C_XML", 4, DOMRESULT, ROW4_S_, DOMSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var350() {
    testSetResult(TABLE37_, "C_XML", 5, DOMRESULT, ROW5_S_, DOMSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var351() {
    testSetResult(TABLE37_, "C_XML", 6, DOMRESULT, ROW6_S_, DOMSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var352() {
    testSetResult(TABLE37_, "C_XML", 7, DOMRESULT, ROW7_S_, DOMSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var353() {
    testSetResult(TABLE37_, "C_XML", 4, DOMRESULT, ROW4_S_, SAXSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var354() {
    testSetResult(TABLE37_, "C_XML", 5, DOMRESULT, ROW5_S_, SAXSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var355() {
    testSetResult(TABLE37_, "C_XML", 6, DOMRESULT, ROW6_S_, SAXSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var356() {
    testSetResult(TABLE37_, "C_XML", 7, DOMRESULT, ROW7_S_, SAXSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var357() {
    testSetResult(TABLE37_, "C_XML", 4, DOMRESULT, ROW4_S_, STAXSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var358() {
    testSetResult(TABLE37_, "C_XML", 5, DOMRESULT, ROW5_S_, STAXSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var359() {
    testSetResult(TABLE37_, "C_XML", 6, DOMRESULT, ROW6_S_, STAXSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var360() {
    testSetResult(TABLE37_, "C_XML", 7, DOMRESULT, ROW7_S_, STAXSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var361() {
    testSetResult(TABLE37_, "C_XML", 4, DOMRESULT, ROW4_S_, STREAMSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var362() {
    testSetResult(TABLE37_, "C_XML", 5, DOMRESULT, ROW5_S_, STREAMSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var363() {
    testSetResult(TABLE37_, "C_XML", 6, DOMRESULT, ROW6_S_, STREAMSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var364() {
    testSetResult(TABLE37_, "C_XML", 7, DOMRESULT, ROW7_S_, STREAMSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  // SAX Results currently not supported
  public void Var365() {
    if (JDTestDriver.isLUW()) {
    
    testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, DOMSOURCE, ROW4_S_,
        null);
    } else {
      testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, DOMSOURCE, ROW4_S_,
      "FeatureNotSupportedException");
      
    }
  }

  public void Var366() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, DOMSOURCE, ROW5_S_,
        null);
    } else {
      testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, DOMSOURCE, ROW5_S_,
      "FeatureNotSupportedException");
      
    }
  }

  public void Var367() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, DOMSOURCE, ROW6_S_,
        null);
    } else {
      testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, DOMSOURCE, ROW6_S_,
      "FeatureNotSupportedException");
      
    }
  }

  public void Var368() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, DOMSOURCE,
        ROW7_S_T_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, DOMSOURCE,
          ROW7_S2_, "FeatureNotSupportedException");
      
    }
  }

  public void Var369() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, SAXSOURCE, ROW4_S_,
        null);
    } else {
      testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, SAXSOURCE, ROW4_S_,
      "FeatureNotSupportedException");
      
    }
  }

  public void Var370() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, SAXSOURCE, ROW5_S_,
        null);
    } else {
      testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, SAXSOURCE, ROW5_S_,
      "FeatureNotSupportedException");
      
    }
  }

  public void Var371() {
    if (JDTestDriver.isLUW()) {
testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, SAXSOURCE, ROW6_S_,
        null);
    } else {
      testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, SAXSOURCE, ROW6_S_,
      "FeatureNotSupportedException");
      
    }
  }

  public void Var372() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, SAXSOURCE,
        ROW7_S_T_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, SAXSOURCE,
          ROW7_S2_, "FeatureNotSupportedException");
      
    }
  }

  public void Var373() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, STAXSOURCE,
        ROW4_S_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, STAXSOURCE,
          ROW4_S_, "FeatureNotSupportedException");
      
    }
  }

  public void Var374() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, STAXSOURCE,
        ROW5_S_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, STAXSOURCE,
          ROW5_S_, "FeatureNotSupportedException");
      
    }
  }

  public void Var375() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, STAXSOURCE,
        ROW6_S_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, STAXSOURCE,
          ROW6_S_, "FeatureNotSupportedException");
      
    }
  }

  public void Var376() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, STAXSOURCE,
        ROW7_S_T_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, STAXSOURCE,
          ROW7_S2_, "FeatureNotSupportedException");
      
    }
  }

  public void Var377() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, STREAMSOURCE,
        ROW4_S_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 4, SAXRESULT, ROW4_S_, STREAMSOURCE,
          ROW4_S_, "FeatureNotSupportedException");
      
    }
  }

  public void Var378() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, STREAMSOURCE,
        ROW5_S_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 5, SAXRESULT, ROW5_S_, STREAMSOURCE,
          ROW5_S_, "FeatureNotSupportedException");
      
    }
  }

  public void Var379() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, STREAMSOURCE,
        ROW6_S_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 6, SAXRESULT, ROW6_S_, STREAMSOURCE,
          ROW6_S_, "FeatureNotSupportedException");
      
    }
  }

  public void Var380() {
    if (JDTestDriver.isLUW()) {
    testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, STREAMSOURCE,
        ROW7_S_T_, null);
    } else {
      testSetResult(TABLE37_, "C_XML", 7, SAXRESULT, ROW7_S_, STREAMSOURCE,
          ROW7_S2_, "FeatureNotSupportedException");
      
    }
  }

  public void Var381() {
    testSetResult(TABLE37_, "C_XML", 4, STAXRESULT, ROW4_S_, DOMSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var382() {
    testSetResult(TABLE37_, "C_XML", 5, STAXRESULT, ROW5_S_, DOMSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var383() {
    testSetResult(TABLE37_, "C_XML", 6, STAXRESULT, ROW6_S_, DOMSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var384() {
    testSetResult(TABLE37_, "C_XML", 7, STAXRESULT, ROW7_S_, DOMSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var385() {
    testSetResult(TABLE37_, "C_XML", 4, STAXRESULT, ROW4_S_, SAXSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var386() {
    testSetResult(TABLE37_, "C_XML", 5, STAXRESULT, ROW5_S_, SAXSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var387() {
    testSetResult(TABLE37_, "C_XML", 6, STAXRESULT, ROW6_S_, SAXSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var388() {
    testSetResult(TABLE37_, "C_XML", 7, STAXRESULT, ROW7_S_, SAXSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var389() {
    testSetResult(TABLE37_, "C_XML", 4, STAXRESULT, ROW4_S_, STAXSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var390() {
    testSetResult(TABLE37_, "C_XML", 5, STAXRESULT, ROW5_S_, STAXSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var391() {
    testSetResult(TABLE37_, "C_XML", 6, STAXRESULT, ROW6_S_, STAXSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var392() {
    testSetResult(TABLE37_, "C_XML", 7, STAXRESULT, ROW7_S_, STAXSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var393() {
    testSetResult(TABLE37_, "C_XML", 4, STAXRESULT, ROW4_S_, STREAMSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var394() {
    testSetResult(TABLE37_, "C_XML", 5, STAXRESULT, ROW5_S_, STREAMSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var395() {
    testSetResult(TABLE37_, "C_XML", 6, STAXRESULT, ROW6_S_, STREAMSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var396() {
    testSetResult(TABLE37_, "C_XML", 7, STAXRESULT, ROW7_S_, STREAMSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var397() {
    testSetResult(TABLE1208_, "C_XML", 4, DOMRESULT, ROW4_S_, DOMSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var398() {
    testSetResult(TABLE1208_, "C_XML", 5, DOMRESULT, ROW5_S_, DOMSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var399() {
    testSetResult(TABLE1208_, "C_XML", 6, DOMRESULT, ROW6_S_, DOMSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var400() {
    testSetResult(TABLE1208_, "C_XML", 7, DOMRESULT, ROW7_S_, DOMSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var401() {
    testSetResult(TABLE1208_, "C_XML", 4, DOMRESULT, ROW4_S_, SAXSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var402() {
    testSetResult(TABLE1208_, "C_XML", 5, DOMRESULT, ROW5_S_, SAXSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var403() {
    testSetResult(TABLE1208_, "C_XML", 6, DOMRESULT, ROW6_S_, SAXSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var404() {
    testSetResult(TABLE1208_, "C_XML", 7, DOMRESULT, ROW7_S_, SAXSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var405() {
    testSetResult(TABLE1208_, "C_XML", 4, DOMRESULT, ROW4_S_, STAXSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var406() {
    testSetResult(TABLE1208_, "C_XML", 5, DOMRESULT, ROW5_S_, STAXSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var407() {
    testSetResult(TABLE1208_, "C_XML", 6, DOMRESULT, ROW6_S_, STAXSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var408() {
    testSetResult(TABLE1208_, "C_XML", 7, DOMRESULT, ROW7_S_, STAXSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var409() {
    testSetResult(TABLE1208_, "C_XML", 4, DOMRESULT, ROW4_S_, STREAMSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var410() {
    testSetResult(TABLE1208_, "C_XML", 5, DOMRESULT, ROW5_S_, STREAMSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var411() {
    testSetResult(TABLE1208_, "C_XML", 6, DOMRESULT, ROW6_S_, STREAMSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var412() {
    testSetResult(TABLE1208_, "C_XML", 7, DOMRESULT, ROW7_S_, STREAMSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  // SAX Results currently not supported
  public void Var413() {

	if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, DOMSOURCE,
        ROW4_S_, null);
	} else {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, DOMSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

	}
  }

  public void Var414() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, DOMSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, DOMSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var415() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, DOMSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, DOMSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var416() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, DOMSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, DOMSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var417() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, SAXSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, SAXSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var418() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, SAXSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, SAXSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var419() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, SAXSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, SAXSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var420() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, SAXSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, SAXSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var421() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, STAXSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, STAXSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var422() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, STAXSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, STAXSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var423() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, STAXSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, STAXSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var424() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, STAXSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, STAXSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var425() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, STREAMSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 4, SAXRESULT, ROW4_S_, STREAMSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var426() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, STREAMSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 5, SAXRESULT, ROW5_S_, STREAMSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var427() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, STREAMSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 6, SAXRESULT, ROW6_S_, STREAMSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var428() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, STREAMSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1208_, "C_XML", 7, SAXRESULT, ROW7_S_, STREAMSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var429() {
    testSetResult(TABLE1208_, "C_XML", 4, STAXRESULT, ROW4_S_, DOMSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var430() {
    testSetResult(TABLE1208_, "C_XML", 5, STAXRESULT, ROW5_S_, DOMSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var431() {
    testSetResult(TABLE1208_, "C_XML", 6, STAXRESULT, ROW6_S_, DOMSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var432() {
    testSetResult(TABLE1208_, "C_XML", 7, STAXRESULT, ROW7_S_, DOMSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var433() {
    testSetResult(TABLE1208_, "C_XML", 4, STAXRESULT, ROW4_S_, SAXSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var434() {
    testSetResult(TABLE1208_, "C_XML", 5, STAXRESULT, ROW5_S_, SAXSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var435() {
    testSetResult(TABLE1208_, "C_XML", 6, STAXRESULT, ROW6_S_, SAXSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var436() {
    testSetResult(TABLE1208_, "C_XML", 7, STAXRESULT, ROW7_S_, SAXSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var437() {
    testSetResult(TABLE1208_, "C_XML", 4, STAXRESULT, ROW4_S_, STAXSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var438() {
    testSetResult(TABLE1208_, "C_XML", 5, STAXRESULT, ROW5_S_, STAXSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var439() {
    testSetResult(TABLE1208_, "C_XML", 6, STAXRESULT, ROW6_S_, STAXSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var440() {
    testSetResult(TABLE1208_, "C_XML", 7, STAXRESULT, ROW7_S_, STAXSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var441() {
    testSetResult(TABLE1208_, "C_XML", 4, STAXRESULT, ROW4_S_, STREAMSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var442() {
    testSetResult(TABLE1208_, "C_XML", 5, STAXRESULT, ROW5_S_, STREAMSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var443() {
    testSetResult(TABLE1208_, "C_XML", 6, STAXRESULT, ROW6_S_, STREAMSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var444() {
    testSetResult(TABLE1208_, "C_XML", 7, STAXRESULT, ROW7_S_, STREAMSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var445() {
    testSetResult(TABLE1200_, "C_XML", 4, DOMRESULT, ROW4_S_, DOMSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var446() {
    testSetResult(TABLE1200_, "C_XML", 5, DOMRESULT, ROW5_S_, DOMSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var447() {
    testSetResult(TABLE1200_, "C_XML", 6, DOMRESULT, ROW6_S_, DOMSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var448() {
    testSetResult(TABLE1200_, "C_XML", 7, DOMRESULT, ROW7_S_, DOMSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var449() {
    testSetResult(TABLE1200_, "C_XML", 4, DOMRESULT, ROW4_S_, SAXSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var450() {
    testSetResult(TABLE1200_, "C_XML", 5, DOMRESULT, ROW5_S_, SAXSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var451() {
    testSetResult(TABLE1200_, "C_XML", 6, DOMRESULT, ROW6_S_, SAXSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var452() {
    testSetResult(TABLE1200_, "C_XML", 7, DOMRESULT, ROW7_S_, SAXSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var453() {
    testSetResult(TABLE1200_, "C_XML", 4, DOMRESULT, ROW4_S_, STAXSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var454() {
    testSetResult(TABLE1200_, "C_XML", 5, DOMRESULT, ROW5_S_, STAXSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var455() {
    testSetResult(TABLE1200_, "C_XML", 6, DOMRESULT, ROW6_S_, STAXSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var456() {
    testSetResult(TABLE1200_, "C_XML", 7, DOMRESULT, ROW7_S_, STAXSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  public void Var457() {
    testSetResult(TABLE1200_, "C_XML", 4, DOMRESULT, ROW4_S_, STREAMSOURCE,
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var458() {
    testSetResult(TABLE1200_, "C_XML", 5, DOMRESULT, ROW5_S_, STREAMSOURCE,
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var459() {
    testSetResult(TABLE1200_, "C_XML", 6, DOMRESULT, ROW6_S_, STREAMSOURCE,
        EXPECTED_ROW6_S1200_PLUS_NL, null);
  }

  public void Var460() {
    testSetResult(TABLE1200_, "C_XML", 7, DOMRESULT, ROW7_S_, STREAMSOURCE,
        EXPECTED_ROW7_S1200_T_PLUS_NL_, null);
  }

  // SAX Results currently not supported
  public void Var461() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, DOMSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, DOMSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var462() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, DOMSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, DOMSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var463() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, DOMSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, DOMSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var464() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, DOMSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, DOMSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var465() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, SAXSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, SAXSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var466() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, SAXSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, SAXSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var467() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, SAXSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, SAXSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var468() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, SAXSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, SAXSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var469() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, STAXSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, STAXSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var470() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, STAXSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, STAXSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var471() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, STAXSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, STAXSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var472() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, STAXSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, STAXSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var473() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, STREAMSOURCE,
        ROW4_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 4, SAXRESULT, ROW4_S_, STREAMSOURCE,
        ROW4_S_, "FeatureNotSupportedException");

      }
  }

  public void Var474() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, STREAMSOURCE,
        ROW5_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 5, SAXRESULT, ROW5_S_, STREAMSOURCE,
        ROW5_S_, "FeatureNotSupportedException");

      }
  }

  public void Var475() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, STREAMSOURCE,
        ROW6_S_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 6, SAXRESULT, ROW6_S_, STREAMSOURCE,
        ROW6_S_, "FeatureNotSupportedException");

      }
  }

  public void Var476() {
      if (JDTestDriver.isLUW()) {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, STREAMSOURCE,
        ROW7_S_T_, null);
      } else {
    testSetResult(TABLE1200_, "C_XML", 7, SAXRESULT, ROW7_S_, STREAMSOURCE,
        ROW7_S2_, "FeatureNotSupportedException");

      }
  }

  public void Var477() {
    testSetResult(TABLE1200_, "C_XML", 4, STAXRESULT, ROW4_S_, DOMSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var478() {
    testSetResult(TABLE1200_, "C_XML", 5, STAXRESULT, ROW5_S_, DOMSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var479() {
    testSetResult(TABLE1200_, "C_XML", 6, STAXRESULT, ROW6_S_, DOMSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var480() {
    testSetResult(TABLE1200_, "C_XML", 7, STAXRESULT, ROW7_S_, DOMSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var481() {
    testSetResult(TABLE1200_, "C_XML", 4, STAXRESULT, ROW4_S_, SAXSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var482() {
    testSetResult(TABLE1200_, "C_XML", 5, STAXRESULT, ROW5_S_, SAXSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var483() {
    testSetResult(TABLE1200_, "C_XML", 6, STAXRESULT, ROW6_S_, SAXSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var484() {
    testSetResult(TABLE1200_, "C_XML", 7, STAXRESULT, ROW7_S_, SAXSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var485() {
    testSetResult(TABLE1200_, "C_XML", 4, STAXRESULT, ROW4_S_, STAXSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var486() {
    testSetResult(TABLE1200_, "C_XML", 5, STAXRESULT, ROW5_S_, STAXSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var487() {
    testSetResult(TABLE1200_, "C_XML", 6, STAXRESULT, ROW6_S_, STAXSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var488() {
    testSetResult(TABLE1200_, "C_XML", 7, STAXRESULT, ROW7_S_, STAXSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  public void Var489() {
    testSetResult(TABLE1200_, "C_XML", 4, STAXRESULT, ROW4_S_, STREAMSOURCE,
        EXPECTED_ROW4_ST_PLUS_S1208_, null);
  }

  public void Var490() {
    testSetResult(TABLE1200_, "C_XML", 5, STAXRESULT, ROW5_S_, STREAMSOURCE,
        EXPECTED_ROW5_ST_PLUS_S1208_, null);
  }

  public void Var491() {
    testSetResult(TABLE1200_, "C_XML", 6, STAXRESULT, ROW6_S_, STREAMSOURCE,
        EXPECTED_ROW6_ST_PLUS_S1208_, null);
  }

  public void Var492() {
    testSetResult(TABLE1200_, "C_XML", 7, STAXRESULT, ROW7_S_, STREAMSOURCE,
        EXPECTED_ROW7_ST_PLUS_S1208_, null);
  }

  /**
   * Check the use of free with SQLXML locators.
   * Make sure that 250000 rows can be processed.
   */ 
  public void Var493() {
      if (checkRelease710()) {
	  if (checkJdbc40()) {
	    Object o = null; 
	    try { 
	      StringBuffer sb = new StringBuffer();
	      sb.append("Test the use of 250,000 rows of locators\n"); 
	      boolean passed = true;

	      int rowCount = 0; 
	      Statement s = connection_.createStatement();
	      String queryTable = "SYSIBM.SQLCOLUMNS";
	      ResultSet rs = s.executeQuery("select count(*) from "+queryTable);
	      rs.next();
	      int count = rs.getInt(1);
	      sb.append(queryTable+" rowcount = "+count+"\n"); 
	      if (count < 250000) {
		  queryTable="SYSIBM.SQLCOLUMNS a, SYSIBM.SQLCOLUMNS b"; 
	      }
	      String sql = "select XMLTEXT('<a>a</a>') from "+queryTable+" FETCH FIRST 250000 ROWS ONLY";
	      sb.append("Running query "+sql+"\n"); 
	      rs = s.executeQuery(sql);
	      while (rs.next()) {
		      o = JDReflectionUtil.callMethod_O(rs,"getSQLXML",1);
		      JDReflectionUtil.callMethod_V(o,"free");
		      rowCount++; 
	      } 
	      rs.close();
	      s.close();
	      passed = rowCount >= 250000; 
	      if (!passed) sb.append("Error rowCount="+rowCount+" sb < 250000"); 
	      assertCondition(passed,sb);


	    } catch (Exception e) {
		String objectClassName="";
		if ( o != null) {
		    objectClassName = o.getClass().toString(); 
		}
		  failed(e, "Unexpected Exception -- Use of 'free' should have freed locators objectClass="+objectClassName);
	      } 
	  }
      }
  } 


}
