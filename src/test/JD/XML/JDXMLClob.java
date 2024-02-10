///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDXMLClob.java
//
// Classes:      JDXMLClob
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.XML;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JDXMLTest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;                          //@C1A
import java.sql.Clob; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;



/**
Testcase JDXMLClob.  This tests the following method
of the JDBC XML class when used against a clob column:

<ul>
  <li>free()
  <li>getBinaryStream()
  <li>setBinaryStream() 
  <li>getCharacterStream()
  <li>getString()
  <li>setCharacterStream()
  <li>setString()
  <li>getSource()
  <li>setResult()

</ul>
**/
public class JDXMLClob
extends JDTestcase
{
  public static final String DOMSOURCE="javax.xml.transform.dom.DOMSource"; 
  public static final String SAXSOURCE="javax.xml.transform.sax.SAXSource"; 
  public static final String STAXSOURCE="javax.xml.transform.stax.StAXSource"; 
  public static final String STREAMSOURCE="javax.xml.transform.stream.StreamSource";

  public static final String DOMRESULT="javax.xml.transform.dom.DOMResult"; 
  public static final String SAXRESULT="javax.xml.transform.sax.SAXResult"; 
  public static final String STAXRESULT="javax.xml.transform.stax.StAXResult"; 
  public static final String STREAMRESULT="javax.xml.transform.stream.StreamResult";




    // Private data.
    private Connection          connection_;
    private Statement           statement1_;
    private Statement           statement2_;
    private Statement           statement37_;
    private Statement           statement1208_;
    private Statement           statement1200_;
    private Statement           statement930_;
    private ResultSet           rs37_;
    private ResultSet           rs1208_;
    private ResultSet           rs1200_;
    private ResultSet           rs930_;
    private ResultSet           rs2_;

    StringBuffer sb = new StringBuffer(); 
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
    // Row 11 -- xml declaration with invalid encoding -- larger than 80 characters (Based on Row 5) 


    public static String TABLE37_          = JDXMLTest.COLLECTION + ".XMLCLB37";
    public static String TABLE1208_          = JDXMLTest.COLLECTION + ".XMLCLB1208";
    public static String TABLE1200_          = JDXMLTest.COLLECTION + ".XMLCLB1200";
    public static String TABLE930_          = JDXMLTest.COLLECTION + ".XMLCLB930";


    public static       String  ROW1_S_ = "";
    public static       String  ROW1_CLEAR_S_ = "<T/>";
    public static       String  ROW2_S_ = "<?xml version=\"1.0\" ?><T/>";
    public static       String  ROW2_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T/>"; 
    public static       String  ROW2_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><T/>"; 
    public static       String  ROW3_S_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T/>";
    public static       String  ROW3_S37_ = "<?xml version=\"1.0\" encoding=\"IBM-37\"?><T/>";
    public static       String  ROW3_S930_ = "<?xml version=\"1.0\" encoding=\"IBM-930\"?><T/>";
    public static       String  ROW3_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T/>";
    public static       String  ROW3_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><T/>";
    public static       String  ROW3_S1202_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?><T/>";
    public static       String  ROW4_S_ = "<?xml version=\"1.0\" ?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S_T_ = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_NODECL_T_ =  "<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1200_NL_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";

    public static       String  ROW5_S_       = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S_T_     = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_NODECL_T_     = "<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S37_     = "<?xml version=\"1.0\" encoding=\"IBM-37\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1200_   = "<?xml version=\"1.0\" encoding=\"UTF-16\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1202_   = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S930_    = "<?xml version=\"1.0\" encoding=\"IBM-930\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1208_   = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1208_T_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S37_T_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"IBM-37\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1200_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1200_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S_T_ = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S1200_NL_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S_ = " \t\r\n<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S_T_ = "<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S2_ = " \t\n<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S1208_T_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S1200_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>"; 
    public static       String  ROW7_S1200_TNL_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>"; 
    public static       String  ROW8_S_ = "NOT XML"; 
    public static       String  ROW9_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW10_S_ = "<?xml version=\"1.0\" ?>1";
    public static       String  ROW11_S_ = "<?xml version=\"1.0\" encoding=\"BAD-ENCODING\"?> <TOP attrib=\"TOP\">TOP 01234567891234567893234567894234567895234567896234567897234567898</TOP>";
    public static       String  ROW11_NODECL_T_ = "<TOP attrib=\"TOP\">TOP 01234567891234567893234567894234567895234567896234567897234567898</TOP>";
    public static       String  ROW11_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP 01234567891234567893234567894234567895234567896234567897234567898</TOP>";


    public static String EXPECTED_ROW4_S1200_PLUS_NL_[] =
    { ROW4_S1200_NL_, /* 16 */
    ROW4_S1200_, /* 169P 166P */
    ROW4_NODECL_T_,
    };
    public static String EXPECTED_ROW5_S1200_T_PLUS_NL_[] =
    {
	ROW4_S1200_NL_, /* 16 */
    ROW5_S1200_T_, /* 169P 166P */
    ROW5_NODECL_T_,
    };
    public static String EXPECTED_ROW6_S1200_PLUS_NL[] =
	{ ROW6_S1200_NL_, /* 16 */
	  ROW6_S1200_, /* 169P 166P */
	};

	public static String EXPECTED_ROW7_S1200_T_PLUS_NL_[] =
	{ ROW7_S1200_TNL_, /* 16 */
	  ROW7_S1200_T_, /* 169P 166P */
	};

        public static String[] EXPECTED_ROW4_ST_PLUS_S1208_ = {
          ROW4_S_T_,            /* 16 */ 
          ROW4_S1208_        /* 169P, 166P */ 
        }; 

        public static String[] EXPECTED_ROW5_ST_PLUS_S1208_ = {
          ROW5_S_T_,            /* 16 */ 
          ROW5_S1208_T_        /* 169P, 166P */ 
        }; 

        public static String[] EXPECTED_ROW6_ST_PLUS_S1208_ = {
          ROW6_S_T_,            /* 16 */ 
          ROW6_S1208_        /* 169P, 166P */ 
        }; 

        public static String[] EXPECTED_ROW7_ST_PLUS_S1208_ = {
          ROW7_S_T_,            /* 16 */ 
          ROW7_S1208_T_        /* 169P, 166P */ 
        }; 


    public static byte[] ROW11_ = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x42, 0x41, 0x44, 0x2D, 0x45, 0x4E, 0x43, 0x4F, 0x44, 0x49, 0x4E, 0x47, 0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x20, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x33, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x34, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x35, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x36, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x37, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x38, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E}; 

    public static byte[] ROW11_37_ = {
	(byte)0x4c,(byte)0x6f,(byte)0xa7,(byte)0x94,(byte)0x93,(byte)0x40,(byte)0xa5,(byte)0x85,
	(byte)0x99,(byte)0xa2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7e,(byte)0x7f,(byte)0xf1,
	(byte)0x4b,(byte)0xf0,(byte)0x7f,(byte)0x40,(byte)0x85,(byte)0x95,(byte)0x83,(byte)0x96,
	(byte)0x84,(byte)0x89,(byte)0x95,(byte)0x87,(byte)0x7e,(byte)0x7f,(byte)0xc2,(byte)0xc1,
	(byte)0xc4,(byte)0x60,(byte)0xc5,(byte)0xd5,(byte)0xc3,(byte)0xd6,(byte)0xc4,(byte)0xc9,
	(byte)0xd5,(byte)0xc7,(byte)0x7f,(byte)0x6f,(byte)0x6e,(byte)0x40,(byte)0x4c,(byte)0xe3,
	(byte)0xd6,(byte)0xd7,(byte)0x40,(byte)0x81,(byte)0xa3,(byte)0xa3,(byte)0x99,(byte)0x89,
	(byte)0x82,(byte)0x7e,(byte)0x7f,(byte)0xe3,(byte)0xd6,(byte)0xd7,(byte)0x7f,(byte)0x6e,
	(byte)0xe3,(byte)0xd6,(byte)0xd7,(byte)0x40,(byte)0xf0,(byte)0xf1,(byte)0xf2,(byte)0xf3,
	(byte)0xf4,(byte)0xf5,(byte)0xf6,(byte)0xf7,(byte)0xf8,(byte)0xf9,(byte)0xf1,(byte)0xf2,
	(byte)0xf3,(byte)0xf4,(byte)0xf5,(byte)0xf6,(byte)0xf7,(byte)0xf8,(byte)0xf9,(byte)0xf3,
	(byte)0xf2,(byte)0xf3,(byte)0xf4,(byte)0xf5,(byte)0xf6,(byte)0xf7,(byte)0xf8,(byte)0xf9,
	(byte)0xf4,(byte)0xf2,(byte)0xf3,(byte)0xf4,(byte)0xf5,(byte)0xf6,(byte)0xf7,(byte)0xf8,
	(byte)0xf9,(byte)0xf5,(byte)0xf2,(byte)0xf3,(byte)0xf4,(byte)0xf5,(byte)0xf6,(byte)0xf7,
	(byte)0xf8,(byte)0xf9,(byte)0xf6,(byte)0xf2,(byte)0xf3,(byte)0xf4,(byte)0xf5,(byte)0xf6,
	(byte)0xf7,(byte)0xf8,(byte)0xf9,(byte)0xf7,(byte)0xf2,(byte)0xf3,(byte)0xf4,(byte)0xf5,
	(byte)0xf6,(byte)0xf7,(byte)0xf8,(byte)0xf9,(byte)0xf8,(byte)0x4c,(byte)0x61,(byte)0xe3,
	(byte)0xd6,(byte)0xd7,(byte)0x6e
    } ;

    public static byte[] ROW11_1200_ = {
	0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20,
	0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F,
	0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30,
	0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F,
	0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22,
	0x00,0x42, 0x00,0x41, 0x00,0x44, 0x00,0x2D, 0x00,0x45, 0x00,0x4E,
	0x00,0x43, 0x00,0x4F, 0x00,0x44, 0x00,0x49, 0x00,0x4E, 0x00,0x47,
	0x00,0x22, 0x00,0x3F, 0x00,0x3E, 0x00,0x20, 0x00,0x3C, 0x00,0x54,
	0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74,
	0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54,
	0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F,
	0x00,0x50, 0x00,0x20, 0x00,0x30, 0x00,0x31, 0x00,0x32, 0x00,0x33,
	0x00,0x34, 0x00,0x35, 0x00,0x36, 0x00,0x37, 0x00,0x38, 0x00,0x39,
	0x00,0x31, 0x00,0x32, 0x00,0x33, 0x00,0x34, 0x00,0x35, 0x00,0x36,
	0x00,0x37, 0x00,0x38, 0x00,0x39, 0x00,0x33, 0x00,0x32, 0x00,0x33,
	0x00,0x34, 0x00,0x35, 0x00,0x36, 0x00,0x37, 0x00,0x38, 0x00,0x39,
	0x00,0x34, 0x00,0x32, 0x00,0x33, 0x00,0x34, 0x00,0x35, 0x00,0x36,
	0x00,0x37, 0x00,0x38, 0x00,0x39, 0x00,0x35, 0x00,0x32, 0x00,0x33,
	0x00,0x34, 0x00,0x35, 0x00,0x36, 0x00,0x37, 0x00,0x38, 0x00,0x39,
	0x00,0x36, 0x00,0x32, 0x00,0x33, 0x00,0x34, 0x00,0x35, 0x00,0x36,
	0x00,0x37, 0x00,0x38, 0x00,0x39, 0x00,0x37, 0x00,0x32, 0x00,0x33,
	0x00,0x34, 0x00,0x35, 0x00,0x36, 0x00,0x37, 0x00,0x38, 0x00,0x39,
	0x00,0x38, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50,
	0x00,0x3E
    };


    public static       byte[]  ROW2_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E,0x3c,0x54,0x2f,0x3e};
    public static       byte[]  ROW3_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x22, 0x3F, 0x3E,0x3c,0x54,0x2f,0x3e};
    public static       byte[]  ROW3_1208_37_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x49, 0x42, 0x4d, 0x2d, 0x33, 0x37, 0x22, 0x3F, 0x3E,0x3c,0x54,0x2f,0x3e};
    public static       byte[]  ROW3_1208_930_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x49, 0x42, 0x4d, 0x2d, 0x39, 0x33, 0x30, 0x22, 0x3F, 0x3E,0x3c,0x54,0x2f,0x3e};

    public static       byte[]  ROW3_1208_1200_     = {
       0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65,
       0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31,
       0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F,
       0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54,
       0x46, 0x2D, 0x31, 0x36, 0x22, 0x3F, 0x3e, 0x3c, 0x54,
     /*                           "     ?     >    <      T */ 
       0x2f,0x3e};
     /*   /   >  */ 

    public static       byte[]  ROW4_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW5_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW5_1208_1200_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x31, 0x36, 0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW5_1208_37_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22,
       0x49, 0x42, 0x4d, 0x2d, 0x33, 0x37,
       0x22,0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};

    public static       byte[]  ROW5_1208_930_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22,
       0x49, 0x42, 0x4d, 0x2d, 0x39, 0x33, 0x30,
       0x22,0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};

    public static       byte[]  ROW6_1208_      = {0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};

    // Updated 01/20/2012 to remove leading whitespace
    public static       byte[]  ROW7_1208_      = {0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW8_1208_      = {0x4E, 0x4F, 0x54, 0x20, 0x58, 0x4D, 0x4C };
    public static       byte[]  ROW9_1208_      = {0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW10_1208_     = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E, 0x31};


    public static       byte[]  ROW2_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x3F, 0x00,0x3E,      0x00,0x3c,0x00,0x54,0x00,0x2f,0x00,0x3e};
    public static       byte[]  ROW3_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F, 0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22, 0x00,0x55, 0x00,0x54, 0x00,0x46, 0x00,0x2D, 0x00,0x31, 0x00,0x36, 0x00,0x22, 0x00,0x3F, 0x00,0x3E,      0x00,0x3c,0x00,0x54,0x00,0x2f,0x00,0x3e};
    public static       byte[]  ROW3_1200_M_    = {(byte) 0xfe,(byte) 0xff,0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F, 0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22, 0x00,0x55, 0x00,0x54, 0x00,0x46, 0x00,0x2D, 0x00,0x31, 0x00,0x36, 0x00,0x22, 0x00,0x3F, 0x00,0x3E,      0x00,0x3c,0x00,0x54,0x00,0x2f,0x00,0x3e};
    public static       byte[]  ROW4_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x3F, 0x00,0x3E, 0x00,0x20, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW5_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F, 0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22, 0x00,0x55, 0x00,0x54, 0x00,0x46, 0x00,0x2D, 0x00,0x31, 0x00,0x36, 0x00,0x22, 0x00,0x3F, 0x00,0x3E, 0x00,0x20, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW5_1200_M_    = {(byte) 0xfe,(byte) 0xff,0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F, 0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22, 0x00,0x55, 0x00,0x54, 0x00,0x46, 0x00,0x2D, 0x00,0x31, 0x00,0x36, 0x00,0x22, 0x00,0x3F, 0x00,0x3E, 0x00,0x20, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW6_1200_      = {0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW7_1200_      = {0x00,0x20, 0x00,0x09, 0x00,0x0d, 0x00,0x0a, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW8_1200_      = {0x00,0x4E, 0x00,0x4F, 0x00,0x54, 0x00,0x20, 0x00,0x58, 0x00,0x4D, 0x00,0x4C };
    public static       byte[]  ROW9_1200_      = {(byte) 0xfe,(byte) 0xff,  0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW9_1200_NO_BOM_      = {0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW10_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x3F, 0x00,0x3E, 0x00,0x31};


    public static       byte[]  ROW2_930_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x3F,0x00, 0x3E,0x00,      0x3c,0x00,0x54,0x00,0x2f,0x00,0x3e,0x00};
                     /*                              4c    6f          b7          75          74          40          b5          66          9b          ab          71          77          76          7e          7f          f1          4b          f0          7f          40          66          76          64          77          65          71          76          68          7e          7f          c9          c2          d4          60          f9          f3          f0          7f          6f          6e}; */ 
    public static       byte[]  ROW3_930_      = { 0x4C, 0x6F, (byte)0xb7, (byte)0x75, (byte)0x74, (byte)0x40, (byte)0xb5, (byte)0x66, (byte)0x9b, (byte)0xAb, (byte)0x71, (byte)0x77, (byte)0x76, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x66, (byte)0x76, (byte)0x64, (byte)0x77, (byte)0x65, (byte)0x71, (byte)0x76, (byte)0x68, (byte)0x7E, (byte)0x7F, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x60, (byte)0xF9, (byte)0xf3, (byte)0xF0, (byte)0x7F, (byte)0x6F, (byte)0x6E, (byte)0x4C,(byte)0xe3,(byte)0x61,(byte)0x6e } ;
    public static       byte[]  ROW4_930_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x3F,0x00, 0x3E,0x00, 0x20,0x00, 0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00, };
                     /*                              4c    6f          b7          75          74          40          b5          66          9b          ab          71          77          76          7e          7f          f1          4b          f0          7f          40          66          76          64          77          65          71          76          68          7e          7f          c9          c2          d4          60          f9          f3          f0          7f          6f          6e          404ce3d6d74062b3b39b71637e7fe3d6d77f6ee3d6d74c61e3d6d76e */  
    public static       byte[]  ROW5_930_      = { 0x4C, 0x6F, (byte)0xb7, (byte)0x75, (byte)0x74, (byte)0x40, (byte)0xb5, (byte)0x66, (byte)0x9b, (byte)0xAb, (byte)0x71, (byte)0x77, (byte)0x76, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x66, (byte)0x76, (byte)0x64, (byte)0x77, (byte)0x65, (byte)0x71, (byte)0x76, (byte)0x68, (byte)0x7E, (byte)0x7F, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x60, (byte)0xF9, (byte)0xf3, (byte)0xF0, (byte)0x7F, (byte)0x6F, (byte)0x6E, (byte)0x40, (byte)0x4c, (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x40, (byte)0x62, (byte)0xb3, (byte)0xb3, (byte)0x9b, (byte)0x71, (byte)0x63, (byte)0x7e, (byte)0x7f, (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x7f, (byte)0x6e, (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x4c, (byte)0x61, (byte)0xe3, (byte)0xd6, (byte)0xd7, (byte)0x6e  } ;
           
    public static       byte[]  ROW6_930_      = {0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW7_930_      = {0x20,0x00, 0x09,0x00, 0x0d,0x00, 0x0a,0x00, 0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW8_930_      = {0x4E,0x00, 0x4F,0x00, 0x54,0x00, 0x20,0x00, 0x58,0x00, 0x4D,0x00, 0x4C,0x00 };
    public static       byte[]  ROW9_930_      = {(byte) 0xff,(byte) 0xfe,  0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW10_930_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x3F,0x00, 0x3E,0x00, 0x31,0x00};








    public static       byte[]  ROW1_         = {}; 
    public static       byte[]  ROW2_37_      = {0x4C, 0x6F,(byte)0xA7,(byte)0x94,(byte)0x93, 0x40,(byte)0xA5,(byte)0x85,(byte)0x99,(byte)0xA2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7E, 0x7F,(byte)0xF1, 0x4B, (byte)0xF0, 0x7F, 0x40, 0x6F, 0x6E,  (byte)0x4C,(byte)0xe3,(byte)0x61,(byte)0x6e };
    public static       byte[]  ROW3_37_      = { 0x4C, 0x6F, (byte)0xA7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xA5, (byte)0x85, (byte)0x99, (byte)0xA2, (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x85, (byte)0x95, (byte)0x83, (byte)0x96, (byte)0x84, (byte)0x89, (byte)0x95, (byte)0x87, (byte)0x7E, (byte)0x7F, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x60, (byte)0xF3, (byte)0xF7, (byte)0x7F, (byte)0x6F, (byte)0x6E,  (byte)0x4C,(byte)0xe3,(byte)0x61,(byte)0x6e  } ;
    public static       byte[]  ROW4_37_      = { (byte)0x4C, (byte)0x6F, (byte)0xA7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xA5, (byte)0x85, (byte)0x99, (byte)0xA2, (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x6F, (byte)0x6E, (byte)0x40, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E} ;
    public static       byte[]  ROW5_37_      = { (byte)0x4C, (byte)0x6F, (byte)0xA7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xA5, (byte)0x85, (byte)0x99, (byte)0xA2, (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x85, (byte)0x95, (byte)0x83, (byte)0x96, (byte)0x84, (byte)0x89, (byte)0x95, (byte)0x87, (byte)0x7E, (byte)0x7F, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x60, (byte)0xF3, (byte)0xF7, (byte)0x7F, (byte)0x6F, (byte)0x6E, (byte)0x40, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E}; 
    public static       byte[]  ROW6_37_      = { (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E } ;
    public static       byte[]  ROW7_37_      = { (byte)0x40, (byte)0x05, (byte)0x0d, (byte)0x15, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E } ;
    public static       byte[]  ROW7_37_2_      = { (byte)0x40, (byte)0x05, (byte)0x0d, (byte)0x25, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E } ;
    public static       byte[]  ROW8_37_      = { (byte)0xD5, (byte)0xD6, (byte)0xE3, (byte)0x40, (byte)0xE7, (byte)0xD4, (byte)0xD3  };   
    public static       byte[]  ROW9_37_      = {0x4C, 0x6F,(byte)0xA7,(byte)0x94,(byte)0x93, 0x40,(byte)0xA5,(byte)0x85,(byte)0x99,(byte)0xA2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7E, 0x7F,(byte)0xF1, 0x4B, (byte)0xF0, 0x7F, 0x40, 0x6F, 0x6E};
    public static       byte[]  ROW10_37_      = {0x4C, 0x6F,(byte)0xA7,(byte)0x94,(byte)0x93, 0x40,(byte)0xA5,(byte)0x85,(byte)0x99,(byte)0xA2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7E, 0x7F,(byte)0xF1, 0x4B, (byte)0xF0, 0x7F, 0x40, 0x6F, 0x6E, (byte) 0xf1};
      
    // UTF8 Rows
    
    public static StringBuffer message = new StringBuffer(); 
    public String lobThreshold = ";lob threshold=100000";
    public boolean isLocator   = false; 
    /**
    Constructor.
    **/
    public JDXMLClob (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDXMLClob",
               namesAndVars, runMode, fileOutputStream,
               password);
    }

    public JDXMLClob (AS400 systemObject,
        String testname, 
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password)
    {
      super (systemObject, testname,
          namesAndVars, runMode, fileOutputStream,
          password);
    }
    

    boolean isHeaderStripped() {
	
	return isXmlSupported() || (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V6R1M0); 
    } 

    /**
    Performs setup needed before running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
	TABLE37_          = JDXMLTest.COLLECTION + ".XMLCLB37";
	TABLE1208_        = JDXMLTest.COLLECTION + ".XMLCLB1208";
	TABLE1200_        = JDXMLTest.COLLECTION + ".XMLCLB1200";
	TABLE930_        = JDXMLTest.COLLECTION + ".XMLCLB930";

        if (isJdbc40 ()) {
        	if (getDriver() == JDTestDriver.DRIVER_JCC) {

				String url_ = "jdbc:db2://" + systemObject_.getSystemName()
						+ ":" + JDTestDriver.jccPort + "/"
						+ JDTestDriver.jccDatabase + lobThreshold;
				connection_ = testDriver_.getConnection(url_, systemObject_
						.getUserId(), encryptedPassword_);

				statement1_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);

				statement2_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				
			} else {
				String url = baseURL_ + lobThreshold + ";user="
						+ systemObject_.getUserId() + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) ;
            connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
				statement1_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);

				statement2_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
			}

	    try {
		statement1_.executeUpdate ("DROP TABLE " + TABLE37_);
	    } catch (Exception e) {
	    }
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
            statement1_.executeUpdate ("CREATE TABLE " + TABLE37_ 
                    + "(C_CLOB CLOB(50000) )");
	    	
	    } else { 
	    
            statement1_.executeUpdate ("CREATE TABLE " + TABLE37_ 
                                      + "(C_CLOB CLOB(50000) CCSID 37)");
	    }

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                                                                 + TABLE37_ + " (C_CLOB) VALUES (?)");
            ps.setString (1, ROW1_S_);
            ps.executeUpdate ();
            ps.setString (1, ROW2_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW3_S37_); 
            ps.executeUpdate ();
            ps.setString (1, ROW4_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW5_S37_); 
            ps.executeUpdate ();
            ps.setString (1, ROW6_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW7_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW8_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW9_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW10_S_); 
            ps.executeUpdate ();
            ps.setString (1, ROW11_S_); 
            ps.executeUpdate ();


            ps.close ();



            try {
                statement1_.executeUpdate ("DROP TABLE " + TABLE1208_);
            } catch (Exception e) {
            }


    	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
          statement1_.executeUpdate ("CREATE TABLE " + TABLE1208_ 
                          + "(C_CLOB CLOB(50000) )");
    	    } else { 
          statement1_.executeUpdate ("CREATE TABLE " + TABLE1208_ 
                                    + "(C_CLOB CLOB(50000) CCSID 1208)");
    	    }
          ps = connection_.prepareStatement ("INSERT INTO " 
                                                               + TABLE1208_ + " (C_CLOB) VALUES (?)");
          ps.setString (1, ROW1_S_);
          ps.executeUpdate ();
          ps.setString (1, ROW2_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW3_S1208_); 
          ps.executeUpdate ();
          ps.setString (1, ROW4_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW5_S1208_); 
          ps.executeUpdate ();
          ps.setString (1, ROW6_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW7_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW8_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW9_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW10_S_); 
          ps.executeUpdate ();
	  ps.setString (1, ROW11_S_); 
	  ps.executeUpdate ();


          ps.close ();


            try {
                statement1_.executeUpdate ("DROP TABLE " + TABLE1200_);
            } catch (Exception e) {
            }


    	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
          statement1_.executeUpdate ("CREATE TABLE " + TABLE1200_ 
                          + "(C_CLOB DBCLOB(50000) )");
    	    } else { 
          statement1_.executeUpdate ("CREATE TABLE " + TABLE1200_ 
                                    + "(C_CLOB DBCLOB(50000) CCSID 1200)");
    	    }
          ps = connection_.prepareStatement ("INSERT INTO " 
                                                               + TABLE1200_ + " (C_CLOB) VALUES (?)");
          ps.setString (1, ROW1_S_);
          ps.executeUpdate ();
          ps.setString (1, ROW2_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW3_S1200_); 
          ps.executeUpdate ();
          ps.setString (1, ROW4_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW5_S1200_); 
          ps.executeUpdate ();
          ps.setString (1, ROW6_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW7_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW8_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW9_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW10_S_); 
          ps.executeUpdate ();
	  ps.setString (1, ROW11_S_); 
	  ps.executeUpdate ();


          ps.close ();




            try {
                statement1_.executeUpdate ("DROP TABLE " + TABLE930_);
            } catch (Exception e) {
            }

    	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
				statement1_.executeUpdate("CREATE TABLE " + TABLE930_
						+ "(C_CLOB CLOB(50000) )");

			} else {
          statement1_.executeUpdate ("CREATE TABLE " + TABLE930_ 
                                    + "(C_CLOB CLOB(50000) CCSID 930)");
			}

          ps = connection_.prepareStatement ("INSERT INTO " 
                                                               + TABLE930_ + " (C_CLOB) VALUES (?)");
          ps.setString (1, ROW1_S_);
          ps.executeUpdate ();
          ps.setString (1, ROW2_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW3_S930_); 
          ps.executeUpdate ();
          ps.setString (1, ROW4_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW5_S930_); 
          ps.executeUpdate ();
          ps.setString (1, ROW6_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW7_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW8_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW9_S_); 
          ps.executeUpdate ();
          ps.setString (1, ROW10_S_); 
          ps.executeUpdate ();
	  ps.setString (1, ROW11_S_); 
	  ps.executeUpdate ();


          ps.close ();


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
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
				statement1200_ = connection_.createStatement(
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);

				statement37_ = connection_.createStatement(
						ResultSet.TYPE_FORWARD_ONLY,
						     ResultSet.CONCUR_UPDATABLE);
				statement930_ = connection_.createStatement(
						ResultSet.TYPE_FORWARD_ONLY,
                     ResultSet.CONCUR_UPDATABLE);

			} else {
				statement1208_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						     ResultSet.CONCUR_UPDATABLE);
				statement1200_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);

				statement37_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				statement930_ = connection_.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);

			}
          
          rs1208_ = statement1208_.executeQuery ("SELECT * FROM " + TABLE1208_);
          rs1200_ = statement1200_.executeQuery ("SELECT * FROM " + TABLE1200_);
	  rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);
          rs930_ = statement930_.executeQuery ("SELECT * FROM " + TABLE930_);

        
        }
    }


    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ()) {
	    statement37_.close ();
	    statement1208_.close ();
	    statement1200_.close ();
	    statement930_.close ();

            statement2_.close ();
	    statement1_.executeUpdate ("DROP TABLE " + TABLE37_);
	    statement1_.executeUpdate ("DROP TABLE " + TABLE1208_);
	    statement1_.executeUpdate ("DROP TABLE " + TABLE1200_);
	    statement1_.executeUpdate ("DROP TABLE " + TABLE930_);
            statement1_.close ();

            connection_.close ();
        }
    }



    public void testMethodAfterFree(String method) {
	if (checkJdbc40 ()) {
	    try {
				if (getDriver() == JDTestDriver.DRIVER_JCC) {
					rs37_ = statement37_.executeQuery("SELECT * FROM "
							+ TABLE37_);
					rs37_.next();
					rs37_.next();
				} else {

		rs37_.absolute (2);
				}
		Object xml = JDReflectionUtil.callMethod_OS(rs37_, "getSQLXML", "C_CLOB");
		JDReflectionUtil.callMethod_V(xml, "free");
		try {
		    if (method.equals("setString")) { 
			JDReflectionUtil.callMethod_V(xml, method, "X");
		    } else if (method.equals("getSource")) {
			JDReflectionUtil.callMethod_O(xml, method, Class.forName("javax.xml.transform.dom.DOMSource" ));
		    } else if (method.equals("setResult")) {
			JDReflectionUtil.callMethod_V(xml, method, Class.forName("javax.xml.transform.dom.DOMResult"));
		    } else {
			JDReflectionUtil.callMethod_O(xml, method);
		    } 
		    failed ("Didn't throw SQLException for "+method);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	    catch (Exception e)
	    {                                                                   
		failed(e, "Unexpected Exception");                              
	    }
	}

    } 


    
    
    /**
     free() getString() -- Make sure method throws an exception after free
     **/
    public void Var001() { testMethodAfterFree("getBinaryStream"); }
    public void Var002() { testMethodAfterFree("setBinaryStream"); }
    public void Var003() { testMethodAfterFree("getCharacterStream"); }
    public void Var004() { testMethodAfterFree("setCharacterStream"); }
    public void Var005() { testMethodAfterFree("getString"); }
    public void Var006() { testMethodAfterFree("setString"); }
    public void Var007() { testMethodAfterFree("getSource"); }
    public void Var008() { testMethodAfterFree("setResult"); }

        /**
     free() free() -- Shouldn't throw exception
     **/
    public void Var009()                                                        
    {                                                                           
      if (checkJdbc40()) { 
        
        try
        {
          rs37_.absolute (2);
          Object xml = JDReflectionUtil.callMethod_OS(rs37_, "getSQLXML", "C_CLOB");
          JDReflectionUtil.callMethod_V(xml, "free");
          JDReflectionUtil.callMethod_V(xml, "free");
          assertCondition (true); 
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    



    public void testGetBinaryStream(ResultSet rs, int row, byte[] expected, String expectedException) {

	if (checkJdbc40 ()) {
	    try {
		message.setLength(0);  
		rs.absolute (row);
		Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_CLOB");
		InputStream v = (InputStream) JDReflectionUtil.callMethod_O(xml, "getBinaryStream");
		boolean passed=compare (v, expected,message);
		if ((!passed) || (expectedException != null) ) {
		    xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_CLOB");
		    v = (InputStream) JDReflectionUtil.callMethod_O(xml, "getBinaryStream");
                    byte[] buffer = new byte[10000]; 
		    int bytesRead = v.read(buffer);
		    if (bytesRead < 0) { bytesRead = 0; }
                    byte[] output = new byte[bytesRead]; 
                    for (int i = 0; i < bytesRead; i++) {
                      output[i] = buffer[i]; 
                    }
		    message.append("\nGot      : "+JDTestUtilities.dumpBytes(output));
		    message.append("\nExpected : "+JDTestUtilities.dumpBytes(expected)); 


		} 
                if (expectedException != null) { 
                  failed("Expected exception "+expectedException+" : "+message.toString()); 
                } else {
  		  assertCondition (passed, "Comparision failed :"+message.toString());
                }
	    }
	    catch (Exception e) {
	      if (expectedException != null) { 
	        String message1 = e.toString();
	        boolean passed = message1.toUpperCase().indexOf(expectedException.toUpperCase()) >= 0; 
	        assertCondition(passed,
	            "Expected exception '"+message1 +"' should contain '"+expectedException+"'");
	        if (!passed) {
	          e.printStackTrace(); 
	        }
	      } else { 
	        failed (e, "Unexpected Exception expectedException = "+expectedException);
	      }
	    }
	}

    } 
    /**
    getBinaryStream() - CCSID37 and lobs of various sizes
    **/
    // Updated 1/20/2011 - Row1 is empty and is not valid XML 
    public void Var010() {
	    testGetBinaryStream(rs37_, 1, ROW1_, null);
    }

    public void Var011() {
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 2, ROW2_37_, null);
	} else if (getRelease() < JDTestDriver.RELEASE_V7R1M0 || !isLocator) {
	    testGetBinaryStream(rs37_, 2, ROW2_1208_, null);
	} else {
	    testGetBinaryStream(rs37_, 2, ROW2_37_, null);
	}
    }
    public void Var012() {
	// Due to the locator implementation, we get back the results of
        // getAsciiStream, prior to V7R1
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 3, ROW3_37_, null);
	} else if (isLocator && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs37_, 3, ROW3_1208_37_, null);
	} else { 
	    testGetBinaryStream(rs37_, 3, ROW3_37_, null);
	}
    }
    public void Var013() {
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 4, ROW4_37_, null);
	} else 	if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs37_, 4, ROW4_37_, null);
	} else { 
	    testGetBinaryStream(rs37_, 4, ROW4_1208_, null);
	}
    }
    public void Var014() {
	if (isLocator) {
	    if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
		testGetBinaryStream(rs37_, 5, ROW5_37_, null);
	    } else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) { 
		testGetBinaryStream(rs37_, 5, ROW5_1208_37_, null);
	    } else {
		testGetBinaryStream(rs37_, 5, ROW5_37_, null);
	    } 
	} else { 
	    testGetBinaryStream(rs37_, 5, ROW5_37_, null);
	}
    }
    public void Var015() {
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 6, ROW6_37_, null);
	} else if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs37_, 6, ROW6_37_, null);
	} else { 
	    testGetBinaryStream(rs37_, 6, ROW6_1208_, null);
	}
    }
    public void Var016() {
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 7, ROW7_37_2_, null);
	} else if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs37_, 7, ROW7_37_2_, null);
	} else { 
	    testGetBinaryStream(rs37_, 7, ROW7_1208_, null);
	}
    }
    public void Var017() {
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 8, ROW8_37_, null);
	} else if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 ) {
	    testGetBinaryStream(rs37_, 8, ROW8_1208_, null);
	} else 	if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    if (isLocator) {
		testGetBinaryStream(rs37_, 8, ROW8_37_, null);
	    } else {
		testGetBinaryStream(rs37_, 8, ROW8_1208_, null); 
	    }
	} else { 
	    testGetBinaryStream(rs37_, 8, ROW8_1208_, "Data type mismatch");
	}
    }
    public void Var018() {
	if (getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0 && isLocator) {
	    testGetBinaryStream(rs37_, 11, ROW11_37_, null);
	} else 	if (isLocator) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0){
		testGetBinaryStream(rs37_, 11, ROW11_37_, null);
	    } else { 
		testGetBinaryStream(rs37_, 11, ROW11_, null);
	    }
	} else { 
	    testGetBinaryStream(rs37_, 11, ROW11_, "XML PARSING ERROR");
	}
    }

    public void Var019() { testGetBinaryStream(rs1208_, 1, ROW1_, null); }
    public void Var020() { testGetBinaryStream(rs1208_, 2, ROW2_1208_, null); }
    public void Var021() { testGetBinaryStream(rs1208_, 3, ROW3_1208_, null); }
    public void Var022() { testGetBinaryStream(rs1208_, 4, ROW4_1208_, null); }
    public void Var023() { testGetBinaryStream(rs1208_, 5, ROW5_1208_, null); }
    public void Var024() { testGetBinaryStream(rs1208_, 6, ROW6_1208_, null); }
    public void Var025() { testGetBinaryStream(rs1208_, 7, ROW7_1208_, null); }
    public void Var026() { testGetBinaryStream(rs1208_, 8, ROW8_1208_, null ); }
    public void Var027() { testGetBinaryStream(rs1208_, 9, ROW9_1208_, null); }
    public void Var028() { 
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }
        testGetBinaryStream(rs1208_, 11, ROW11_, "XML PARSING ERROR"); 
   }

    public void Var029() {
	if (isLocator) {
	    testGetBinaryStream(rs1200_, 1, ROW1_, null); 
	} else {
	    testGetBinaryStream(rs1200_, 1, ROW1_, null);
	}
    }
    public void Var030() {

	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 2, ROW2_1200_, null);
	} else if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs1200_, 2, ROW2_1200_, null);
	} else {
	    testGetBinaryStream(rs1200_, 2, ROW2_1208_, null);
	}
    }
    public void Var031() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 3, ROW3_1200_, null);
	} else if (isLocator) {
	    if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0 ) {
		testGetBinaryStream(rs1200_, 3, ROW3_1200_, null);
	    } else { 
		testGetBinaryStream(rs1200_, 3, ROW3_1208_1200_, null);
	    }
	} else { 
	    testGetBinaryStream(rs1200_, 3, ROW3_1200_M_, null);
	}
    }
    public void Var032() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 4, ROW4_1200_, null);
	} else 	if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs1200_, 4, ROW4_1200_, null);
	} else { 
	    testGetBinaryStream(rs1200_, 4, ROW4_1208_, null);
	}
    }
    public void Var033() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 5, ROW5_1200_, null);
	} else 	if (isLocator) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		testGetBinaryStream(rs1200_, 5, ROW5_1200_, null);
	    } else { 
		testGetBinaryStream(rs1200_, 5, ROW5_1208_1200_, null);
	    }
	} else { 
	    testGetBinaryStream(rs1200_, 5, ROW5_1200_M_, null);
	}
    }
    public void Var034() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 6, ROW6_1200_, null);
	} else 	if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs1200_, 6, ROW6_1200_, null);
	} else { 
	    testGetBinaryStream(rs1200_, 6, ROW6_1208_, null);
	}
    }
    public void Var035() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 7, ROW7_1200_, null);
	} else if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs1200_, 7, ROW7_1200_, null);
	} else { 
	    testGetBinaryStream(rs1200_, 7, ROW7_1208_, null);
	}
    }
    public void Var036() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 8, ROW8_1200_, null);
	} else 	if (getRelease() >= JDTestDriver.RELEASE_V6R1M0) {
	    if (isLocator) {
		testGetBinaryStream(rs1200_, 8, ROW8_1200_, null);
	    } else { 
		testGetBinaryStream(rs1200_, 8, ROW8_1208_, null);
	    }
	} else { 
	    testGetBinaryStream(rs1200_, 8, ROW8_1208_, null);
	}
    }
    public void Var037() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 9, ROW9_1200_NO_BOM_, null);
	} else if (isLocator && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
	    testGetBinaryStream(rs1200_, 9, ROW9_1200_NO_BOM_, null);
	} else { 
	    testGetBinaryStream(rs1200_, 9, ROW9_1208_, null);
	}
    }
    public void Var038() {
	if (isLocator && getDriver()==JDTestDriver.DRIVER_NATIVE  && getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	    testGetBinaryStream(rs1200_, 11, ROW11_1200_, null);
	} else if (isLocator) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		testGetBinaryStream(rs1200_, 11, ROW11_1200_, null);
	    } else { 
		testGetBinaryStream(rs1200_, 11, ROW11_, null);
	    }
	} else { 
	    testGetBinaryStream(rs1200_, 11, ROW11_, "XML PARSING ERROR"); 
	}
    }


    public void Var039() { testGetBinaryStream(rs930_, 1, ROW1_, null); }
    public void Var040() {
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }
        testGetBinaryStream(rs930_, 2, ROW2_1208_, null);
    }
    public void Var041() { testGetBinaryStream(rs930_, 3, ROW3_930_, null); }
    public void Var042() { 
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }
        testGetBinaryStream(rs930_, 4, ROW4_1208_, null);
    }
    public void Var043() { testGetBinaryStream(rs930_, 5, ROW5_930_, null); }
    public void Var044() { 
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }
        testGetBinaryStream(rs930_, 6, ROW6_1208_, null);
    }
    public void Var045() { 
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }
        testGetBinaryStream(rs930_, 7, ROW7_1208_, null); 
    }
    public void Var046() {         if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
        notApplicable("TC not consistant with other locator vars (var30)");
        return;
    }
    testGetBinaryStream(rs930_, 8, ROW8_1208_, null); 
    }
    public void Var047() { 
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }testGetBinaryStream(rs930_, 9, ROW9_1208_, null); 
    }
    public void Var048() {
        if (isLocator && getDriver() == JDTestDriver.DRIVER_TOOLBOX ) { 
            notApplicable("TC not consistant with other locator vars (var30)");
            return;
        }
        testGetBinaryStream(rs930_, 11, ROW11_, "XML PARSING ERROR"); 
    }




    public void testGetCharacterStream(ResultSet rs, String column, int row, String expected, String expectedException) {

	if (checkJdbc40 ()) {
	    try {
		message.setLength(0);  
		rs.absolute (row);
		Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
		Reader v = (Reader) JDReflectionUtil.callMethod_O(xml, "getCharacterStream");

		if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected); 
		} 
    sb.setLength(0); 

		boolean passed = compare (v, expected,sb);
		if (!passed) {
                  String s = (String) JDReflectionUtil.callMethod_O(xml,"getString");
                  message.append("\nGot     : "+s); 
                  message.append("\n        : "+JDTestUtilities.dumpBytes(s));
                  message.append("\nExpected: "+expected);  
                  message.append("\n        : "+JDTestUtilities.dumpBytes(expected));
		}
                if (expectedException == null) { 
		  assertCondition (passed, "Comparision failed :"+message.toString());
                } else {
                  String s = (String) JDReflectionUtil.callMethod_O(xml,"getString");
                  failed("Expected exception "+expectedException+" : got "+s+" : "+message.toString()); 
                }
	    }
	    catch (Exception e) {
                if (expectedException == null) { 
                   failed (e, "Unexpected Exception");
                } else { 
                  String message1 = e.toString();
                  assertCondition(message1.indexOf(expectedException) >= 0,
                                "Expected exception '"+message1 +"' should contain '"+expectedException+"'"); 
                
                }
	    }
	}

    } 
    /**
    getCharacterStream() - CCSID37 and lobs of various sizes
    **/
    /* 08/08/2013 invalid XML not allows since the XMLTEXT can produce an SQLXML that is not valid */ 
    public void Var049() { testGetCharacterStream(rs37_, "C_CLOB", 1, ROW1_S_, null); }	
    public void Var050() { testGetCharacterStream(rs37_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var051() { testGetCharacterStream(rs37_, "C_CLOB", 3, ROW3_S37_, null); }
    public void Var052() {
	if (isLocator) {
	    testGetCharacterStream(rs37_, "C_CLOB", 4, ROW4_NODECL_T_, null);
	} else { 
	    testGetCharacterStream(rs37_, "C_CLOB", 4, ROW4_S_, null);
	};
    }
    public void Var053() {
	if (isLocator) { 
	    testGetCharacterStream(rs37_, "C_CLOB", 5, ROW5_NODECL_T_, null);
	} else {
	    testGetCharacterStream(rs37_, "C_CLOB", 5, ROW5_S37_, null);
	} 
    }
    public void Var054() { testGetCharacterStream(rs37_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var055() { testGetCharacterStream(rs37_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    /* Changed 8/8/2013 */ 
    public void Var056() { testGetCharacterStream(rs37_, "C_CLOB", 8, ROW8_S_, null); }
    public void Var057() {
	if (isLocator) {
	    testGetCharacterStream(rs37_, "C_CLOB", 11, ROW11_NODECL_T_, null);
	} else { 
	    testGetCharacterStream(rs37_, "C_CLOB", 11, ROW11_S_, null);
	}
    }


    
    public void Var058() { testGetCharacterStream(rs1208_, "C_CLOB", 1, ROW1_S_, null); } 
    public void Var059() { testGetCharacterStream(rs1208_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var060() { testGetCharacterStream(rs1208_, "C_CLOB", 3, ROW3_S1208_, null); }
    public void Var061() { testGetCharacterStream(rs1208_, "C_CLOB", 4, ROW4_S_, null); }
    public void Var062() { testGetCharacterStream(rs1208_, "C_CLOB", 5, ROW5_S1208_, null); }
    public void Var063() { testGetCharacterStream(rs1208_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var064() { testGetCharacterStream(rs1208_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    public void Var065() { testGetCharacterStream(rs1208_, "C_CLOB", 8, ROW8_S_,null); }
    public void Var066() { testGetCharacterStream(rs1208_, "C_CLOB", 9, ROW9_S_, null); }
    public void Var067() { testGetCharacterStream(rs1208_, "C_CLOB", 11, ROW11_S_, null); }

    /* Changed 8/8/2013 */ 
    public void Var068() { testGetCharacterStream(rs1200_, "C_CLOB", 1, ROW1_S_, null); } 
    public void Var069() { testGetCharacterStream(rs1200_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var070() { testGetCharacterStream(rs1200_, "C_CLOB", 3, ROW3_S1200_, null); }
    public void Var071() {
	if (isLocator) {
	    testGetCharacterStream(rs1200_, "C_CLOB", 4, ROW4_NODECL_T_, null);
	} else { 
	    testGetCharacterStream(rs1200_, "C_CLOB", 4, ROW4_S_, null);
	}
    }
    public void Var072() {
	if (isLocator) {
	    testGetCharacterStream(rs1200_, "C_CLOB", 5, ROW5_NODECL_T_.trim(), null);
	} else {
	    testGetCharacterStream(rs1200_, "C_CLOB", 5, ROW5_S1200_, null);
	}
    }	
    public void Var073() { testGetCharacterStream(rs1200_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var074() { testGetCharacterStream(rs1200_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    /* Changed 8/8/2013 */ 
    public void Var075() { testGetCharacterStream(rs1200_, "C_CLOB", 8, ROW8_S_, null); }
    public void Var076() { testGetCharacterStream(rs1200_, "C_CLOB", 9, ROW9_S_, null); }
    public void Var077() {
	if (isLocator) {
	    testGetCharacterStream(rs1200_, "C_CLOB", 11, ROW11_NODECL_T_, null);
	} else { 
	    testGetCharacterStream(rs1200_, "C_CLOB", 11, ROW11_S_, null);
	}
    }

    public void Var078() { testGetCharacterStream(rs930_, "C_CLOB", 1, ROW1_S_, null); } 
    public void Var079() { testGetCharacterStream(rs930_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var080() { testGetCharacterStream(rs930_, "C_CLOB", 3, ROW3_S930_, null); }
    public void Var081() { testGetCharacterStream(rs930_, "C_CLOB", 4, ROW4_S_, null); }
    public void Var082() { testGetCharacterStream(rs930_, "C_CLOB", 5, ROW5_S930_, null); }
    public void Var083() { testGetCharacterStream(rs930_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var084() { testGetCharacterStream(rs930_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    public void Var085() { testGetCharacterStream(rs930_, "C_CLOB", 8, ROW8_S_, null); }
    public void Var086() { testGetCharacterStream(rs930_, "C_CLOB", 9, ROW9_S_, null); }
    public void Var087() { testGetCharacterStream(rs930_, "C_CLOB", 11, ROW11_S_, null); }


    
    public void testGetString(ResultSet rs, String column, int row, String expected, String expectedException) {

	if (checkJdbc40 ()) {
	    try {
		message.setLength(0);  
		rs.absolute (row);
		Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
		String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
		if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected);
		    expected = expected.trim(); 
		} 
		boolean passed = expected.equals(s);
		if (!passed) {
                  message.append("\nGot     : "+s); 
                  message.append("\n        : "+JDTestUtilities.dumpBytes(s));
                  message.append("\nExpected: "+expected);  
                  message.append("\n        : "+JDTestUtilities.dumpBytes(expected));
		}
		if (expectedException != null) { 
		  failed("Expected exception "+expectedException); 
		} else {
		  assertCondition (passed, "Comparision failed :"+message.toString());
		}
	    }
	    catch (Exception e) {
	      if (expectedException != null) { 
	        String message1 = e.toString();
	        assertCondition(message1.indexOf(expectedException) >= 0,
	            "Expected exception '"+message1 +"' should contain '"+expectedException+"'"); 
	      } else { 
	        failed (e, "Unexpected Exception");
	        
	      }
	    }
	}

    } 
    /**
    getString() - CCSID37 and lobs of various sizes
    **/
    /* Changed 8/8/2013 */ 
    public void Var088() { testGetString(rs37_, "C_CLOB", 1, ROW1_S_, null); }	
    public void Var089() { testGetString(rs37_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var090() { testGetString(rs37_, "C_CLOB", 3, ROW3_S37_, null); }
    public void Var091() { testGetString(rs37_, "C_CLOB", 4, ROW4_S_.trim(), null); }
    public void Var092() { testGetString(rs37_, "C_CLOB", 5, ROW5_S37_.trim(), null); }
    public void Var093() { testGetString(rs37_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var094() { testGetString(rs37_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    /* Changed 8/8/2013 */ 
    public void Var095() { testGetString(rs37_, "C_CLOB", 8, ROW8_S_, null); }
    public void Var096() { testGetString(rs37_, "C_CLOB", 11, ROW11_S_.trim(), null); }

    public void Var097() { testGetString(rs1208_, "C_CLOB", 1, ROW1_S_, null); }	
    public void Var098() { testGetString(rs1208_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var099() { testGetString(rs1208_, "C_CLOB", 3, ROW3_S1208_, null); }
    public void Var100() { testGetString(rs1208_, "C_CLOB", 4, ROW4_S_.trim(), null); }
    public void Var101() { testGetString(rs1208_, "C_CLOB", 5, ROW5_S1208_.trim(), null); }
    public void Var102() { testGetString(rs1208_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var103() { testGetString(rs1208_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    public void Var104() { testGetString(rs1208_, "C_CLOB", 8, ROW8_S_, null); }
    public void Var105() { testGetString(rs1208_, "C_CLOB", 9, ROW9_S_, null); }
    public void Var106() { testGetString(rs1208_, "C_CLOB", 11, ROW11_S_.trim(), null); }

    /* Changed 8/8/2013 */ 
    public void Var107() { testGetString(rs1200_, "C_CLOB", 1, ROW1_S_, null); }	
    public void Var108() { testGetString(rs1200_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var109() { testGetString(rs1200_, "C_CLOB", 3, ROW3_S1200_, null); }
    public void Var110() { testGetString(rs1200_, "C_CLOB", 4, ROW4_S_.trim(), null); }
    public void Var111() { testGetString(rs1200_, "C_CLOB", 5, ROW5_S1200_.trim(), null); }
    public void Var112() { testGetString(rs1200_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var113() { testGetString(rs1200_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    /* Changed 8/8/2013 */ 
    public void Var114() { testGetString(rs1200_, "C_CLOB", 8, ROW8_S_, null); }
    public void Var115() { testGetString(rs1200_, "C_CLOB", 9, ROW9_S_, null); }
    public void Var116() { testGetString(rs1200_, "C_CLOB", 11, ROW11_S_.trim(), null); }

    public void Var117() { testGetString(rs930_, "C_CLOB", 1, ROW1_S_,null); }
    public void Var118() { testGetString(rs930_, "C_CLOB", 2, ROW2_S_, null); }
    public void Var119() { testGetString(rs930_, "C_CLOB", 3, ROW3_S930_, null); }
    public void Var120() { testGetString(rs930_, "C_CLOB", 4, ROW4_S_.trim(), null); }
    public void Var121() { testGetString(rs930_, "C_CLOB", 5, ROW5_S930_.trim(), null); }
    public void Var122() { testGetString(rs930_, "C_CLOB", 6, ROW6_S_, null); }
    public void Var123() { testGetString(rs930_, "C_CLOB", 7, ROW7_S_.trim(), null); }
    public void Var124() { testGetString(rs930_, "C_CLOB", 8, ROW8_S_, null); } 
    public void Var125() { testGetString(rs930_, "C_CLOB", 9, ROW9_S_, null); }
    public void Var126() { testGetString(rs930_, "C_CLOB", 11, ROW11_S_.trim(), null); }


    public boolean checkExpected(String output, Object expected, StringBuffer message1) {  
      boolean passed = false;
      String expectedString =null; 
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
        message1.append("\nGot     : "+output);
        if (expectedString != null) { 
          message1.append("\nExpected: "+expectedString);
          
        } else { 
          if (expectedArray != null) { 
          for (int i = 0; i < expectedArray.length; i++) {
            message1.append("\nExpected: "+expectedArray[i]);
          }
          }
        }       
      }
      return passed; 
    }
    
    public void testGetSource(ResultSet rs, String column, int row, String sourceClassName , Object expected, Object expectedException) {
         if(expectedException instanceof String)
             expectedException = ((String)expectedException).toUpperCase();
         else if(expectedException instanceof String[]){
             for(int x=0;x< ((String[])expectedException).length ; x++)
             {
                 ((String[])expectedException)[x] = ((String[])expectedException)[x].toUpperCase();
             }
         }  
        
        String columnData = "Not retrieved"; 
        if (checkJdbc40 ()) {
          try {
          String classname = sourceClassName; 
          Class sourceClass = null; 
          if (classname != null) { 
            if (classname.indexOf("JAVAX") == 0) {
              classname = "javax"+sourceClassName.substring(5); 
            }
            sourceClass = Class.forName(classname);
          }
            message.setLength(0);  
            rs.absolute (row);
            columnData = rs.getString(column); 
            Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
                      
            Object source =  JDReflectionUtil.callMethod_O(xml, "getSource", Class.forName("java.lang.Class"), sourceClass);
            
            boolean passed = false; 
          //
          // This generic code with an identity transformer should handle 
          // any case.  To get other cases, use JAVAX as the class
          //
          String output = "";
            if (sourceClassName == null || 
              sourceClassName.equals("javax.xml.transform.stax.StAXSource") ||
              sourceClassName.equals("javax.xml.transform.sax.SAXSource") || 
                sourceClassName.equals("javax.xml.transform.stream.StreamSource") ||
              sourceClassName.equals("javax.xml.transform.dom.DOMSource")) {
              
              TransformerFactory factory = TransformerFactory.newInstance();
              Transformer transformer = factory.newTransformer();
              transformer.setErrorListener(errorListener);
	      if (source == null) {
		  throw new Exception("source is null"); 
	      } 
              transformer.transform((Source) source, new StreamResult("/tmp/JDXMLClob.xml")); 
              BufferedReader br = new BufferedReader(new FileReader("/tmp/JDXMLClob.xml"));
              output = "";
              String next = br.readLine();
              while (next != null) {
                output += next;
                next = br.readLine();
              }
              br.close(); 
              passed = checkExpected(output, expected, message); 
              
            } else if (sourceClassName.equals("JAVAX.xml.transform.dom.DOMSource")) {
              Object document=JDReflectionUtil.callMethod_O(source, "getNode");
                    output = JDXMLUtil.documentToString(document); 

                    
                    passed = checkExpected(output, expected, message); 
                } else if (sourceClassName.equals("JAVAX.xml.transform.sax.SAXSource")) {

                    Object inputSource = JDReflectionUtil.callMethod_O(source, "getInputSource");
                  if (inputSource == null) throw new Exception("inputSource is null "); 
                    Reader reader = (Reader) JDReflectionUtil.callMethod_O(inputSource, "getCharacterStream");
                    BufferedReader br = new BufferedReader(reader);
                    output = "";
                    String next = br.readLine();
                    while (next != null) {
                        output += next;
                        next = br.readLine();
                    }

		    if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
			if (expected instanceof String) { 
			    expected = JDTestUtilities.stripXmlDeclaration((String) expected);
			}
		    } 

                    passed = checkExpected(output, expected, message); 
              } else if (sourceClassName.equals("JAVAX.xml.transform.stax.StAXSource")) {

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(); 
                transformer.transform((Source) source, new StreamResult("/tmp/JDXMLClob.xml")); 
                  BufferedReader br = new BufferedReader(new FileReader("/tmp/JDXMLClob.xml"));
                  output = "";
                  String next = br.readLine();
                  while (next != null) {
                      output += next;
                      next = br.readLine();
                  }
                  br.close(); 
                  passed = checkExpected(output, expected, message); 
                  
              } else if (sourceClassName.equals("JAVAX.xml.transform.stream.StreamSource")) {

                  Reader reader = (Reader) JDReflectionUtil.callMethod_O(source, "getReader");
                  if (reader == null) throw new Exception("reader is null "); 
                  BufferedReader br = new BufferedReader(reader);
                  
                  String next = br.readLine();
                  while (next != null) {
                      output += next;
                      next = br.readLine();
                  }

		  if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		      if (expected instanceof String) { 
			  expected = JDTestUtilities.stripXmlDeclaration((String) expected);
		      }
		  } 

                  passed = checkExpected(output, expected, message); 


                } else {
                  passed =false;
                  message.append("Could not handle class "+sourceClassName); 
                }
            if (expectedException != null) { 
              failed("Did not get expected exception "+expectedException+" : got "+output+" : message : "+message.toString()); 
            } else {
              
              assertCondition (passed, "Comparision failed :"+message.toString());
            }
          }
          catch (Throwable e) {
            if (expectedException != null) { 
              String exceptionMessage = e.toString().toUpperCase();
              boolean passed;
              if (expectedException instanceof String ) { 
                passed = exceptionMessage.indexOf((String) expectedException) >= 0; 
                assertCondition(passed,
                  "Got exception '"+exceptionMessage +"' expected to find contain '"+expectedException+"'");
              } else {
                  String notFound = ""; 
                 passed = false; 
                 String expectedArray[] = (String[]) expectedException; 
                 for (int i = 0; i < expectedArray.length; i++) {
                   if (exceptionMessage.indexOf( expectedArray[i]) >= 0) {
                     passed = true; 
                   } else {
                     notFound +=  "\nGot exception '"+exceptionMessage +"' expected to find contain '"+expectedArray[i]+"'";
 
                   }
                 }
                 assertCondition(passed, notFound); 
                 
              }
              if (!passed) {
                System.out.println("Column data is '"+columnData+"'");
                e.printStackTrace(); 
              }
            } else { 
                System.out.println("Column data is '"+columnData+"'");
             
                failed (e, "Unexpected Exception");
            }
            }
        }

  } 
  /**
  getSource() - CCSID37 and lobs of various sizes and different sources 
  **/

    public void Var127() {
      String expected[] =  { 
          ROW4_S1208_ ,             /* 169P */
          ROW4_S1208_STANDALONE_    /* 16 */
      };
      
      testGetSource(rs37_, "C_CLOB", 4, "javax.xml.transform.dom.DOMSource",
          expected, 
          null); 
    }
    public void Var128() {
      String expected[] = { ROW5_S1208_T_,            /* 169P */
          ROW5_S37_T_STANDALONE_,    /* 16 */
          ROW5_S1208_STANDALONE_ /*from windows*/
      }; 
      testGetSource(rs37_, "C_CLOB", 5, "javax.xml.transform.dom.DOMSource",
          expected,
          null); 
    }
    /* Changed 08/08/2013 */ 
    public void Var129() { testGetSource(rs37_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }

  public void Var130() {
      String expected[] = { ROW2_S1208_,           
              ROW2_S1208_STANDALONE_  /*from windows*/
          }; 
      if (isHeaderStripped()) { 
      testGetSource(rs37_, "C_CLOB", 2, "javax.xml.transform.dom.DOMSource", expected, null);
      } else {
	  testGetSource(rs37_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR");
      }
 }

  public void Var131() { testGetSource(rs37_, "C_CLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
  public void Var132() { testGetSource(rs37_, "C_CLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
  /* changed 08/08/2013 -- primed from JDXMLClobLocator*/ 
  public void Var133() { testGetSource(rs37_, "C_CLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var134() {
    String expected[] =
      { "An invalid XML character", "Content is not allowed in prolog","Data type mismatch" };

      testGetSource(rs37_, "C_CLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, expected);
  }

  public void Var135() { testGetSource(rs37_, "C_CLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var136() { testGetSource(rs37_, "C_CLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
  public void Var137() {
    String expected[] =
      { "An invalid XML character", "Content is not allowed in prolog","Data type mismatch" };
    testGetSource(rs37_, "C_CLOB", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S_, expected);
  }
  public void Var138() {
    String expected[] =
      { "An invalid XML character", "Content is not allowed in prolog", "Data type mismatch" };

    testGetSource(rs37_, "C_CLOB", 10, "javax.xml.transform.stax.StAXSource",
        ROW2_S_, expected);
  }

  public void Var139() { testGetSource(rs37_, "C_CLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
  public void Var140() { testGetSource(rs37_, "C_CLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
  /* Changed 08/08/2013 */ 
  public void Var141() { testGetSource(rs37_, "C_CLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var142() {
    String expected[] =
      { "An invalid XML character", "Content is not allowed in prolog", "Data type mismatch" };

      testGetSource(rs37_, "C_CLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, expected); 
  }

  public void Var143() {testGetSource(rs37_, "C_CLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null);  }

  public void Var144() {
    testGetSource(rs37_, "C_CLOB", 5, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var145() {
      if (isLocator) {
	  testGetSource(rs37_, "C_CLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_NODECL_T_, null);
      } else { 
	  testGetSource(rs37_, "C_CLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null);
      }
  }
  public void Var146() {
      if (isLocator) { 
	  testGetSource(rs37_, "C_CLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_NODECL_T_, null);
      } else {
	  testGetSource(rs37_, "C_CLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S37_, null);
      }
}

  public void Var147() { testGetSource(rs37_, "C_CLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var148() { testGetSource(rs37_, "C_CLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

  public void Var149() {
      if (isLocator) {
	  testGetSource(rs37_, "C_CLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_NODECL_T_, null);
      } else { 
	  testGetSource(rs37_, "C_CLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);
      }
  }
  public void Var150() {
      if (isLocator) {
	  testGetSource(rs37_, "C_CLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_NODECL_T_,  null);
      } else { 
      testGetSource(rs37_, "C_CLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S37_, null);
      }
}

  public void Var151() { testGetSource(rs37_, "C_CLOB", 4, null, ROW4_S1208_, null); }
  public void Var152() { testGetSource(rs37_, "C_CLOB", 5, null, ROW5_S1208_T_, null); }
  public void Var153() { testGetSource(rs37_, "C_CLOB", 5, "java.lang.String", ROW5_S37_, "FeatureNotSupportedException"); }



  public void Var154() {
    String expected[] =
      { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };
    testGetSource(rs1208_, "C_CLOB", 4, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var155() {
    String expected[] =
      { ROW5_S1208_T_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };
    testGetSource(rs1208_, "C_CLOB", 5, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }

  public void Var156() { testGetSource(rs1208_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
  public void Var157() {
      String expected[] =
      { ROW2_S1208_,  
      ROW2_S1208_STANDALONE_ /* windows */
      };
      if (isHeaderStripped()) {
	  testGetSource(rs1208_, "C_CLOB", 2, "javax.xml.transform.dom.DOMSource", expected, null);
      } else {
	  testGetSource(rs1208_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR");
      }
  }	

  public void Var158() { testGetSource(rs1208_, "C_CLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
  public void Var159() { testGetSource(rs1208_, "C_CLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
  public void Var160() { testGetSource(rs1208_, "C_CLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var161() { testGetSource(rs1208_, "C_CLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, "Content is not allowed in prolog"); }

  public void Var162() { testGetSource(rs1208_, "C_CLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var163() { testGetSource(rs1208_, "C_CLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
  public void Var164() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Data type mismatch",
      "Content is not allowed in prolog" /* 16 */
      };
    testGetSource(rs1208_, "C_CLOB", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S_, expected);
  }

  public void Var165() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Content is not allowed in prolog" /* 16 */
      };
    testGetSource(rs1208_, "C_CLOB", 10, "javax.xml.transform.stax.StAXSource",
        ROW2_S_, expected);
  }

  public void Var166() { testGetSource(rs1208_, "C_CLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
  public void Var167() { testGetSource(rs1208_, "C_CLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
  public void Var168() { testGetSource(rs1208_, "C_CLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var169() { testGetSource(rs1208_, "C_CLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, "Content is not allowed in prolog"); }

  public void Var170() {

    
    testGetSource(rs1208_, "C_CLOB", 4, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW4_S1200_PLUS_NL_, null);
  }

  public void Var171() {
    testGetSource(rs1208_, "C_CLOB", 5, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var172() { testGetSource(rs1208_, "C_CLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null); }
  public void Var173() { testGetSource(rs1208_, "C_CLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S1208_, null); }

  public void Var174() { testGetSource(rs1208_, "C_CLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var175() { testGetSource(rs1208_, "C_CLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

  public void Var176() { testGetSource(rs1208_, "C_CLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null); }
  public void Var177() { testGetSource(rs1208_, "C_CLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S1208_, null); }

  public void Var178() { testGetSource(rs1208_, "C_CLOB", 4, null, ROW4_S1208_, null); }
  public void Var179() { testGetSource(rs1208_, "C_CLOB", 5, null, ROW5_S1208_T_, null); }
  public void Var180() { testGetSource(rs1208_, "C_CLOB", 5, "java.lang.String", ROW5_S1208_, "FeatureNotSupportedException"); }


  public void Var181() {
    String expected[] =
      { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };
    testGetSource(rs1200_, "C_CLOB", 4, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }
  public void Var182() {
    String expected[] =
      { ROW5_S1208_T_, /* 169P */
        ROW5_S1200_STANDALONE_ /* 16 */,
        ROW5_S1208_STANDALONE_
      };
    testGetSource(rs1200_, "C_CLOB", 5, "javax.xml.transform.dom.DOMSource",
        expected, null);
  }
  /* changed 08/08/2013 */ 
  public void Var183() { testGetSource(rs1200_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
  public void Var184() {
      String expected[] =
      { ROW2_S1208_, /* 169P */
              ROW2_S1208_STANDALONE_ /* windows*/
      };
      if (isHeaderStripped()) {
	  testGetSource(rs1200_, "C_CLOB", 2, "javax.xml.transform.dom.DOMSource", expected, null);
      } else {
	  testGetSource(rs1200_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); 
      } 
  }
  public void Var185() { testGetSource(rs1200_, "C_CLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
  public void Var186() { testGetSource(rs1200_, "C_CLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
 /* changed 08/08/2013 */ 
  public void Var187() { testGetSource(rs1200_, "C_CLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var188() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Data type mismatch",
      "Content is not allowed in prolog" /* 16 */
      };
testGetSource(rs1200_, "C_CLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, expected); }

  public void Var189() { testGetSource(rs1200_, "C_CLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var190() { testGetSource(rs1200_, "C_CLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
  public void Var191() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Data type mismatch",
      "Content is not allowed in prolog" /* 16 */
      };
    testGetSource(rs1200_, "C_CLOB", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S_, expected);
  }

  public void Var192() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Content is not allowed in prolog", /* 16 */
      "Data type mismatch",
      };
    testGetSource(rs1200_, "C_CLOB", 10, "javax.xml.transform.stax.StAXSource",
        ROW2_S_, expected);
  }

  public void Var193() { testGetSource(rs1200_, "C_CLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
  public void Var194() { testGetSource(rs1200_, "C_CLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
  /* 08/08/2013 changed */ 
  public void Var195() { testGetSource(rs1200_, "C_CLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var196() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Data type mismatch",
      "Content is not allowed in prolog" /* 16 */
      };

 testGetSource(rs1200_, "C_CLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, expected); }

  public void Var197() {testGetSource(rs1200_, "C_CLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null);  }

  public void Var198() {

    testGetSource(rs1200_, "C_CLOB", 5, "JAVAX.xml.transform.dom.DOMSource",
        EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
  }

  public void Var199() {
      if (isLocator) {
	  testGetSource(rs1200_, "C_CLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_NODECL_T_, null);
      } else { 
	  testGetSource(rs1200_, "C_CLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null);
      }
  }
  public void Var200() {
      if (isLocator) {
	  testGetSource(rs1200_, "C_CLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_NODECL_T_, null);
      } else {
	  testGetSource(rs1200_, "C_CLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S1200_, null);
      }
}

  public void Var201() { testGetSource(rs1200_, "C_CLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var202() { testGetSource(rs1200_, "C_CLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

  public void Var203() {
      if (isLocator) {
	  testGetSource(rs1200_, "C_CLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_NODECL_T_, null);
      } else { 
	  testGetSource(rs1200_, "C_CLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);
      }
  }
  public void Var204() {
      if (isLocator) {
	  testGetSource(rs1200_, "C_CLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_NODECL_T_, null);
      } else { 
	  testGetSource(rs1200_, "C_CLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S1200_, null);
      }
  }

  public void Var205() { testGetSource(rs1200_, "C_CLOB", 4, null, ROW4_S1208_, null); }
  public void Var206() { testGetSource(rs1200_, "C_CLOB", 5, null, ROW5_S1208_T_, null); }
  public void Var207() { testGetSource(rs1200_, "C_CLOB", 5, "java.lang.String", ROW5_S1200_, "FeatureNotSupportedException"); }



  public void Var208() {
    String[] expected = { ROW4_S1208_, /* 169P */
      ROW4_S1208_STANDALONE_ /* 16 */
      };

    testGetSource(rs930_, "C_CLOB", 4, "javax.xml.transform.dom.DOMSource", expected, null); }
  // The rs930, row 5, variation files because getBinaryStream returns the data in CCSID 930, which is not
  // handled by the DOM Parser.  
  // The following test (run above verifies that CCSID 930 data is returned
  // testGetBinaryStream(rs930_, 5, ROW5_930_, null
  public void Var209() {
      String expected[] =
      { ROW5_S1208_T_,  
              ROW5_S1208_STANDALONE_ /* windows*/
      };
      if (isHeaderStripped()) {
	  testGetSource(rs930_, "C_CLOB", 5, "javax.xml.transform.dom.DOMSource", expected, null);
      } else {
	  testGetSource(rs930_, "C_CLOB", 5, "javax.xml.transform.dom.DOMSource", ROW5_S1208_T_, "XML PARSING ERROR");
      }
  }
  public void Var210() { testGetSource(rs930_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
  public void Var211() {
      String expected[] =
      { ROW2_S1208_,  
              ROW2_S1208_STANDALONE_ /* windows*/
      };
      if (isHeaderStripped()) {
	  testGetSource(rs930_, "C_CLOB", 2, "javax.xml.transform.dom.DOMSource", expected, null);
      } else {
	  testGetSource(rs930_, "C_CLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR");
      }
  }

  public void Var212() { testGetSource(rs930_, "C_CLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
  public void Var213() { testGetSource(rs930_, "C_CLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
  public void Var214() { testGetSource(rs930_, "C_CLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var215() { testGetSource(rs930_, "C_CLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, "Content is not allowed in prolog"); }

  public void Var216() { testGetSource(rs930_, "C_CLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var217() { testGetSource(rs930_, "C_CLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
  public void Var218() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Data type mismatch",
      "Content is not allowed in prolog" /* 16 */
      };
    testGetSource(rs930_, "C_CLOB", 8, "javax.xml.transform.stax.StAXSource",
        ROW8_S_, expected);
  }

  public void Var219() {
    String expected[] =
      { "An invalid XML character", /* 169P */
      "Content is not allowed in prolog" /* 16 */
      };
    testGetSource(rs930_, "C_CLOB", 10, "javax.xml.transform.stax.StAXSource",
        ROW2_S_, expected);
  }

  public void Var220() {
    testGetSource(rs930_, "C_CLOB", 4,
        "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null);
  }

  public void Var221() {
    testGetSource(rs930_, "C_CLOB", 5,
        "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
  public void Var222() { testGetSource(rs930_, "C_CLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "CONTENT IS NOT ALLOWED IN PROLOG"); }
  public void Var223() { testGetSource(rs930_, "C_CLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, "Content is not allowed in prolog"); }

  public void Var224() {testGetSource(rs930_, "C_CLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null); 
    }
  // The rs930, row 5, variation files because getBinaryStream returns the data in CCSID 930, which is not
  // handled by the DOM Parser.  
  // The following test (run above verifies that CCSID 930 data is returned
  // testGetBinaryStream(rs930_, 5, ROW5_930_, null
  public void Var225() {
      if (isHeaderStripped()) {
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	      getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	      // 03/14/2012 on V6R1 on fowgai2 
	      testGetSource(rs930_, "C_CLOB", 5, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1200_T_PLUS_NL_, null);
	  } else { 
	      testGetSource(rs930_, "C_CLOB", 5, "JAVAX.xml.transform.dom.DOMSource", ROW5_NODECL_T_, null);
	  }

      } else { 
	  testGetSource(rs930_, "C_CLOB", 5, "JAVAX.xml.transform.dom.DOMSource", ROW5_S1200_T_, "XML PARSING ERROR");
      }
  }

  public void Var226() { testGetSource(rs930_, "C_CLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null); }
  public void Var227() { testGetSource(rs930_, "C_CLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S930_, null); }

  public void Var228() { testGetSource(rs930_, "C_CLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
  public void Var229() { testGetSource(rs930_, "C_CLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

  public void Var230() { testGetSource(rs930_, "C_CLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null); }
  public void Var231() { testGetSource(rs930_, "C_CLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S930_, null); }

  public void Var232() { testGetSource(rs930_, "C_CLOB", 4, null, ROW4_S1208_, null); }
  public void Var233() { testGetSource(rs930_, "C_CLOB", 5, null, ROW5_S1208_T_, null); }
  public void Var234() { testGetSource(rs930_, "C_CLOB", 5, "java.lang.String", ROW5_S930_, "FeatureNotSupportedException"); }






    public void testSetCharacterStream(String table, String column, int row, String writeString, String expected, String expectedException ) {
	String clearString = "";
	if (checkJdbc40()) {
	    message.setLength(0); 
	    try {
		// Clear the row first and validate that is was cleared
		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		rs2_.updateString(1,ROW1_CLEAR_S_);
                rs2_.updateRow();                                               
                rs2_.close();                                                   
		

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		clearString = rs2_.getString(column);
		rs2_.close(); 

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		Object sqlxml; 
		if (row % 2 == 0) {
		    sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
		} else { 
		    sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
		}

                Writer v = (Writer) JDReflectionUtil.callMethod_O(sqlxml, "setCharacterStream");
		PrintWriter writer = new PrintWriter(v); 
		writer.print(writeString);                                           
                writer.close();
		JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);                           
                rs2_.updateRow();                                               
                rs2_.close();

                rs2_ = statement2_.executeQuery("SELECT * FROM " + table);      
                rs2_.absolute(row);
		Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
		String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
		if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected);
		    expected = expected.trim(); 
		} 

		boolean passed = expected.equals(s); 
		if (!passed) {
                    message.append("\nGot     : "+s); 
                    message.append("\n        : "+JDTestUtilities.dumpBytes(s));
                    message.append("\nExpected: "+expected);  
                    message.append("\n        : "+JDTestUtilities.dumpBytes(expected));
		} 
                if (expectedException != null) { 
                  failed("Expected exception "+expectedException); 
                } else {
		assertCondition (clearString.length() ==  ROW1_CLEAR_S_.length() && passed, "clearString.length()="+clearString.length()+" sb " +ROW1_CLEAR_S_.length()+" "+ message);
                }
            }
            catch (Exception e) {                                               
              if (expectedException != null) { 
                String message1 = e.toString();
                assertCondition(message1.indexOf(expectedException) >= 0,
                    "Expected exception '"+message1 +"' should contain '"+expectedException+"' clearString="+clearString); 
              } else { 
                failed (e, "Unexpected Exception clearString="+clearString);
              }
            }
        }
    }                                                                           

    /**
    setCharacterStream() - CCSID 37 and lobs of various sizes
    **/

    public void Var235() { testSetCharacterStream(TABLE37_, "C_CLOB", 1, ROW1_S_, ROW1_S_, null );
    }
    public void Var236() { testSetCharacterStream(TABLE37_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var237() { testSetCharacterStream(TABLE37_, "C_CLOB", 3, ROW3_S37_, ROW3_S37_, null ); }
    public void Var238() { testSetCharacterStream(TABLE37_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var239() { testSetCharacterStream(TABLE37_, "C_CLOB", 5, ROW5_S37_.trim(), ROW5_S37_.trim(), null ); }
    public void Var240() { testSetCharacterStream(TABLE37_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var241() { testSetCharacterStream(TABLE37_, "C_CLOB", 7, ROW7_S_, ROW7_S_.trim(), null ); }
    public void Var242() { testSetCharacterStream(TABLE37_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null); }
    public void Var243() { testSetCharacterStream(TABLE37_, "C_CLOB", 11, ROW11_S_.trim(), ROW11_S_.trim(), null ); }

    public void Var244() { testSetCharacterStream(TABLE1208_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var245() { testSetCharacterStream(TABLE1208_, "C_CLOB", 3, ROW3_S1208_, ROW3_S1208_, null ); }
    public void Var246() { testSetCharacterStream(TABLE1208_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var247() { testSetCharacterStream(TABLE1208_, "C_CLOB", 5, ROW5_S1208_, ROW5_S1208_, null ); }
    public void Var248() { testSetCharacterStream(TABLE1208_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var249() { testSetCharacterStream(TABLE1208_, "C_CLOB", 7, ROW7_S_.trim(), ROW7_S_.trim(), null ); }
    public void Var250() { testSetCharacterStream(TABLE1208_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null); }
    public void Var251() { testSetCharacterStream(TABLE1208_, "C_CLOB", 9, ROW9_S_, ROW9_S_, null ); }
    public void Var252() { testSetCharacterStream(TABLE1208_, "C_CLOB", 11, ROW11_S_.trim(), ROW11_S_.trim(), null ); }

    public void Var253() { testSetCharacterStream(TABLE1200_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var254() { testSetCharacterStream(TABLE1200_, "C_CLOB", 3, ROW3_S1200_, ROW3_S1200_, null ); }
    public void Var255() { testSetCharacterStream(TABLE1200_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_NODECL_T_.trim(), null ); }
    public void Var256() { testSetCharacterStream(TABLE1200_, "C_CLOB", 5, ROW5_S1200_, ROW5_S1200_, null ); }
    public void Var257() { testSetCharacterStream(TABLE1200_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var258() { testSetCharacterStream(TABLE1200_, "C_CLOB", 7, ROW7_S_.trim(), ROW7_S_.trim(), null ); }
    public void Var259() { testSetCharacterStream(TABLE1200_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null); }
    public void Var260() { testSetCharacterStream(TABLE1200_, "C_CLOB", 9, ROW9_S_, ROW9_S_, null ); }
    public void Var261() { testSetCharacterStream(TABLE1200_, "C_CLOB", 11, ROW11_S_.trim(), ROW11_S_.trim(), null ); }


    public void Var262() { testSetCharacterStream(TABLE930_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var263() { testSetCharacterStream(TABLE930_, "C_CLOB", 3, ROW3_S930_, ROW3_S930_, null ); }
    public void Var264() { testSetCharacterStream(TABLE930_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var265() { testSetCharacterStream(TABLE930_, "C_CLOB", 5, ROW5_S930_.trim(), ROW5_S930_.trim(), null ); }
    public void Var266() { testSetCharacterStream(TABLE930_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var267() { testSetCharacterStream(TABLE930_, "C_CLOB", 7, ROW7_S_.trim(), ROW7_S_.trim(), null ); }
    public void Var268() { testSetCharacterStream(TABLE930_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null); }
    public void Var269() { testSetCharacterStream(TABLE930_, "C_CLOB", 9, ROW9_S_, ROW9_S_, null ); }
    public void Var270() { testSetCharacterStream(TABLE930_, "C_CLOB", 11, ROW11_S_.trim(), ROW11_S_.trim(), null ); }


 

    public void testSetString(String table, String column, int row, String writeString, 
        String expected, String expectedException ) {
	if (checkJdbc40()) {
	    message.setLength(0); 
	    try {
		// Clear the row first and validate that is was cleared
		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		rs2_.updateString(1,ROW1_CLEAR_S_);
                rs2_.updateRow();                                               
                rs2_.close();                                                   
		

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		String blankString = rs2_.getString(column);
		rs2_.close(); 

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);

		Object sqlxml; 
		if (row % 2 == 0) {
		    sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
		} else { 
		    sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
		}


           
                JDReflectionUtil.callMethod_V(sqlxml, "setString", writeString);
		// For testing purposes, call setString again for some rows 
		if (row % 2 == 0) {
		    JDReflectionUtil.callMethod_V(sqlxml, "setString", writeString);
		}

		JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);                           
                rs2_.updateRow();                                               
                rs2_.close();

                rs2_ = statement2_.executeQuery("SELECT * FROM " + table);      
                rs2_.absolute(row);
		Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
		String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");

		if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected);
		    expected = expected.trim(); 
		} 

		boolean passed = expected.equals(s); 
		if (!passed) {
		    message.append("\nGot     : "+s); 
                    message.append("\n        : "+JDTestUtilities.dumpBytes(s));
		    message.append("\nExpected: "+expected);  
                    message.append("\n        : "+JDTestUtilities.dumpBytes(expected));
		} 
                if (expectedException != null) { 
                  failed("Expected exception "+expectedException); 
                } else {
                  assertCondition (blankString.length() == ROW1_CLEAR_S_.length() && passed, "blankString.length="+blankString.length()+" sb "+ROW1_CLEAR_S_.length()+" "+ message);
                }
	    }
	    catch (Exception e) {                                               
	      if (expectedException != null) { 
	        String message1 = e.toString();
	        assertCondition(message1.indexOf(expectedException) >= 0,
	            "Expected exception '"+message1 +"' should contain '"+expectedException+"'"); 
	      } else { 
	        failed (e, "Unexpected Exception");
	      }
	    }
        }
    }                                                                           

    /**
    setString() - CCSID 37 and lobs of various sizes
    **/

    public void Var271() { testSetString(TABLE37_, "C_CLOB", 1, ROW1_S_, ROW1_S_, null );      }
    public void Var272() { testSetString(TABLE37_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var273() { testSetString(TABLE37_, "C_CLOB", 3, ROW3_S37_, ROW3_S37_, null ); }
    public void Var274() { testSetString(TABLE37_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var275() { testSetString(TABLE37_, "C_CLOB", 5, ROW5_S37_, ROW5_S37_, null ); }
    public void Var276() { testSetString(TABLE37_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var277() { testSetString(TABLE37_, "C_CLOB", 7, ROW7_S_, ROW7_S_, null ); }
    public void Var278() { testSetString(TABLE37_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null ); }
    public void Var279() { testSetString(TABLE37_, "C_CLOB", 11, ROW11_S_, ROW11_S_, null ); }



    public void Var280() { testSetString(TABLE1208_, "C_CLOB", 1, ROW1_S_, ROW1_S_, null );      }
    public void Var281() { testSetString(TABLE1208_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var282() { testSetString(TABLE1208_, "C_CLOB", 3, ROW3_S1208_, ROW3_S1208_, null ); }
    public void Var283() { testSetString(TABLE1208_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var284() { testSetString(TABLE1208_, "C_CLOB", 5, ROW5_S1208_, ROW5_S1208_, null ); }
    public void Var285() { testSetString(TABLE1208_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var286() { testSetString(TABLE1208_, "C_CLOB", 7, ROW7_S_, ROW7_S_, null ); }
    public void Var287() { testSetString(TABLE1208_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null ); }
    public void Var288() { testSetString(TABLE1208_, "C_CLOB", 9, ROW9_S_, ROW9_S_, null ); }
    public void Var289() { testSetString(TABLE1208_, "C_CLOB", 11, ROW11_S_, ROW11_S_, null ); }

    public void Var290() { testSetString(TABLE1200_, "C_CLOB", 1, ROW1_S_, ROW1_S_, null );      }
    public void Var291() { testSetString(TABLE1200_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var292() { testSetString(TABLE1200_, "C_CLOB", 3, ROW3_S1200_, ROW3_S1200_, null ); }
    public void Var293() { testSetString(TABLE1200_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var294() { testSetString(TABLE1200_, "C_CLOB", 5, ROW5_S1200_, ROW5_S1200_, null ); }
    public void Var295() { testSetString(TABLE1200_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var296() { testSetString(TABLE1200_, "C_CLOB", 7, ROW7_S_, ROW7_S_, null ); }
    public void Var297() { testSetString(TABLE1200_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null ); }
    public void Var298() { testSetString(TABLE1200_, "C_CLOB", 9, ROW9_S_, ROW9_S_, null ); }
    public void Var299() { testSetString(TABLE1200_, "C_CLOB", 11, ROW11_S_, ROW11_S_, null ); }

    public void Var300() { testSetString(TABLE930_, "C_CLOB", 1, ROW1_S_, ROW1_S_, null );      }
    public void Var301() { testSetString(TABLE930_, "C_CLOB", 2, ROW2_S_, ROW2_S_, null ); }
    public void Var302() { testSetString(TABLE930_, "C_CLOB", 3, ROW3_S930_, ROW3_S930_, null ); }
    public void Var303() { testSetString(TABLE930_, "C_CLOB", 4, ROW4_S_.trim(), ROW4_S_.trim(), null ); }
    public void Var304() { testSetString(TABLE930_, "C_CLOB", 5, ROW5_S930_, ROW5_S930_, null ); }
    public void Var305() { testSetString(TABLE930_, "C_CLOB", 6, ROW6_S_, ROW6_S_, null ); }
    public void Var306() { testSetString(TABLE930_, "C_CLOB", 7, ROW7_S_, ROW7_S_, null ); }
    public void Var307() { testSetString(TABLE930_, "C_CLOB", 8, ROW8_S_, ROW8_S_, null ); }
    public void Var308() { testSetString(TABLE930_, "C_CLOB", 9, ROW9_S_, ROW9_S_, null ); }
    public void Var309() { testSetString(TABLE930_, "C_CLOB", 11, ROW11_S_, ROW11_S_, null ); }


    public void testSetBinaryStream(String table, String column, int row, byte[] expected, String expectedString, String expectedException ) {
	if (checkJdbc40()) {
	    message.setLength(0); 
	    try {
		// Clear the row first and validate that is was cleared
		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		rs2_.updateString(1,ROW1_CLEAR_S_);
                rs2_.updateRow();                                               
                rs2_.close();                                                   
		

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		String blankString = rs2_.getString(column);
		rs2_.close(); 

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
                Object sqlxml;
                // Change how the sqlxml object is obtained 
		if (row % 2 == 0) {
		    sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
		} else { 
		    sqlxml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
		}
                OutputStream v = (OutputStream) JDReflectionUtil.callMethod_O(sqlxml, "setBinaryStream");                      
		v.write(expected);                                           
                v.close();
		JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);                           
                rs2_.updateRow();                                               
                rs2_.close();

                rs2_ = statement2_.executeQuery("SELECT * FROM " + table);      
                rs2_.absolute(row);                                               
                String outputString = rs2_.getString(column);


		if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expectedString = JDTestUtilities.stripXmlDeclaration(expectedString);
		} 


		boolean passed = expectedString.equals(outputString); 
		if (!passed) {
                    message.append("\nInput    : "+JDTestUtilities.dumpBytes(expected)); 
                    message.append("\nGot      : "+outputString);
		    message.append("\nGot      : "+JDTestUtilities.dumpBytes(outputString));
                    message.append("\nExpected : "+expectedString); 
		    message.append("\nExpected : "+JDTestUtilities.dumpBytes(expectedString)); 
		} 

		if (expectedException != null) { 
		  failed("Expected exception "+expectedException); 
		} else {
		  assertCondition (blankString.length() == ROW1_CLEAR_S_.length()  && passed, "blankString.length="+blankString.length()+" sb "+ROW1_CLEAR_S_.length()+" "+ message);
		}
            }
            catch (Exception e) {                                               
              if (expectedException != null) { 
                String message1 = e.toString();
                assertCondition(message1.indexOf(expectedException) >= 0,
                    "Expected exception '"+message1 +"' should contain '"+expectedException+"'"); 
              } else { 
                failed (e, "Unexpected Exception");
              }
            }
        }
    }                                                                           

    /**
    setBinaryStream() - CCSID 37 and lobs of various sizes
    **/

    public void Var310() { testSetBinaryStream(TABLE37_, "C_CLOB", 1, ROW1_ , ROW1_S_, "Data type mismatch");    }
    public void Var311() { testSetBinaryStream(TABLE37_, "C_CLOB", 2, ROW2_37_, ROW2_S_,  null ); }
    public void Var312() { testSetBinaryStream(TABLE37_, "C_CLOB", 3, ROW3_37_, ROW3_S37_, null ); }
    public void Var313() { testSetBinaryStream(TABLE37_, "C_CLOB", 4, ROW4_37_, ROW4_S_, null ); }
    public void Var314() { testSetBinaryStream(TABLE37_, "C_CLOB", 5, ROW5_37_, ROW5_S37_, null ); }
    public void Var315() { testSetBinaryStream(TABLE37_, "C_CLOB", 6, ROW6_37_, ROW6_S_, null ); }
    public void Var316() { testSetBinaryStream(TABLE37_, "C_CLOB", 7, ROW7_37_, ROW7_S2_, null ); }
    public void Var317() { testSetBinaryStream(TABLE37_, "C_CLOB", 8, ROW8_37_, ROW8_S_, "Data type mismatch" ); }
    public void Var318() { testSetBinaryStream(TABLE37_, "C_CLOB", 11, ROW11_, ROW11_S_, null ); }


    public void Var319() { testSetBinaryStream(TABLE1208_, "C_CLOB", 1, ROW1_, ROW1_S_, "Data type mismatch" );    }
    public void Var320() { testSetBinaryStream(TABLE1208_, "C_CLOB", 2, ROW2_1208_ ,ROW2_S_, null); }
    public void Var321() { testSetBinaryStream(TABLE1208_, "C_CLOB", 3, ROW3_1208_ ,ROW3_S1208_, null); }
    public void Var322() { testSetBinaryStream(TABLE1208_, "C_CLOB", 4, ROW4_1208_ ,ROW4_S_, null); }
    public void Var323() { testSetBinaryStream(TABLE1208_, "C_CLOB", 5, ROW5_1208_ ,ROW5_S1208_,  null); }
    public void Var324() { testSetBinaryStream(TABLE1208_, "C_CLOB", 6, ROW6_1208_ ,ROW6_S_,  null); }
    public void Var325() { testSetBinaryStream(TABLE1208_, "C_CLOB", 7, ROW7_1208_ ,ROW7_S2_.trim(),  null); }
    public void Var326() { testSetBinaryStream(TABLE1208_, "C_CLOB", 8, ROW8_1208_ ,ROW8_S_,  "Data type mismatch"); }
    public void Var327() { testSetBinaryStream(TABLE1208_, "C_CLOB", 9, ROW9_1208_ ,ROW9_S_,  null); }
    public void Var328() { testSetBinaryStream(TABLE1208_, "C_CLOB", 11, ROW11_, ROW11_S_, null ); }

    public void Var329() { testSetBinaryStream(TABLE1200_, "C_CLOB", 1, ROW1_ ,ROW1_S_,  "Data type mismatch");    }
    public void Var330() { testSetBinaryStream(TABLE1200_, "C_CLOB", 2, ROW2_1200_ ,ROW2_S_, null); }
    public void Var331() { testSetBinaryStream(TABLE1200_, "C_CLOB", 3, ROW3_1200_ ,ROW3_S1200_, null); }
    public void Var332() { testSetBinaryStream(TABLE1200_, "C_CLOB", 4, ROW4_1200_ ,ROW4_S_,  null); }
    public void Var333() { testSetBinaryStream(TABLE1200_, "C_CLOB", 5, ROW5_1200_ ,ROW5_S1200_,  null); }
    public void Var334() { testSetBinaryStream(TABLE1200_, "C_CLOB", 6, ROW6_1200_ ,ROW6_S_,  null); }
    public void Var335() { testSetBinaryStream(TABLE1200_, "C_CLOB", 7, ROW7_1200_ ,ROW7_S2_,  null); }
    public void Var336() { testSetBinaryStream(TABLE1200_, "C_CLOB", 8, ROW8_1200_ ,ROW8_S_, "Data type mismatch"); }
    public void Var337() { testSetBinaryStream(TABLE1200_, "C_CLOB", 9, ROW9_1200_ ,ROW9_S_,  null); }
    public void Var338() { testSetBinaryStream(TABLE1200_, "C_CLOB", 11, ROW11_, ROW11_S_, null ); }

    public void Var339() { testSetBinaryStream(TABLE930_, "C_CLOB", 1, ROW1_ , ROW1_S_, "Data type mismatch");    }
    public void Var340() { testSetBinaryStream(TABLE930_, "C_CLOB", 2, ROW2_930_ , ROW2_S_, null); }
    // Set Binary stream returning garbage
    public void Var341() { testSetBinaryStream(TABLE930_, "C_CLOB", 3, ROW3_930_ , ROW3_S930_, null); }
    public void Var342() { testSetBinaryStream(TABLE930_, "C_CLOB", 4, ROW4_930_ ,ROW4_S_,  null); }
    public void Var343() { testSetBinaryStream(TABLE930_, "C_CLOB", 5, ROW5_930_ , ROW5_S930_, null); }
    public void Var344() { testSetBinaryStream(TABLE930_, "C_CLOB", 6, ROW6_930_ ,ROW6_S_,  null); }
    public void Var345() { testSetBinaryStream(TABLE930_, "C_CLOB", 7, ROW7_930_ ,ROW7_S2_,  null); }
    public void Var346() { testSetBinaryStream(TABLE930_, "C_CLOB", 8, ROW8_930_ ,ROW8_S_, "Data type mismatch"); }
    public void Var347() { testSetBinaryStream(TABLE930_, "C_CLOB", 9, ROW9_930_ ,ROW9_S_,  null); }
    public void Var348() { testSetBinaryStream(TABLE930_, "C_CLOB", 11, ROW11_, ROW11_S_, null ); }




    public void testSetResult(String table, String column, int row,
      String resultClassName, String writeString, String sourceClassName,
      Object expected, String expectedException) {
    if (checkJdbc40()) {
      message.setLength(0);
      try {
        // Clear the row first and validate that is was cleared
        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_.absolute(row);
        rs2_.updateString(1, ROW1_CLEAR_S_);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_.absolute(row);
        String clearString = rs2_.getString(column);
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_.absolute(row);
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
          Object inputFactory = JDReflectionUtil.callStaticMethod_O("javax.xml.stream.XMLInputFactory", "newInstance"); 
          Object  xmlStreamReader = JDReflectionUtil.callMethod_O(inputFactory, "createXMLStreamReader", 
                                                                  Class.forName("java.io.Reader"), new StringReader(writeString));
          source = (Source) JDReflectionUtil.createObject("javax.xml.transform.stax.StAXSource", "javax.xml.stream.XMLStreamReader", xmlStreamReader); 
          
        } else if (sourceClassName.equals("javax.xml.transform.sax.SAXSource")) {
          InputSource inputSource = new InputSource(new StringReader(writeString));
          source =  new javax.xml.transform.sax.SAXSource(inputSource);

        } else if (sourceClassName
            .equals("javax.xml.transform.stream.StreamSource")) {
          source = new javax.xml.transform.stream.StreamSource(new StringReader(writeString)); 
          
        } else if (sourceClassName.equals("javax.xml.transform.dom.DOMSource")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            // TODO:  Figure out how to get a stream from the STRING..

            org.w3c.dom.Document doc = parser.parse( new ByteArrayInputStream(writeString.getBytes()));
            source =  new DOMSource(doc);

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
        rs2_.absolute(row);
        Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
	if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
	    if (expected instanceof String) {
		expected = JDTestUtilities.stripXmlDeclaration((String) expected);
		expected = ((String)expected).trim(); 
	    }
	} 
        boolean passed; 
        if (expected instanceof String) {
          passed = expected.equals(s);
        } else { 
          passed = false; 
          String[] expectedArray = (String[])expected; 
          for (int i =0 ; i < expectedArray.length; i++) {
	    String expected1 = expectedArray[i];
	    if (isHeaderStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected1 = JDTestUtilities.stripXmlDeclaration(expected1);
		    expected1 = expected1.trim(); 
	    } 
            if (expected1.equals(s)) {
              passed = true; 
            }
          }
        }
        if (!passed) {
          message.append("\nGot     : " + s);
          message.append("\n        : " + JDTestUtilities.dumpBytes(s));
          if (expected instanceof String) { 
            message.append("\nExpected: " + expected);
            message.append("\n        : " + JDTestUtilities.dumpBytes((String) expected));
          } else { 
            String[] expectedArray = (String[])expected; 
            for (int i =0 ; i < expectedArray.length; i++) {
              message.append("\nExpected: " + expectedArray[i]);
              message.append("\n        : " + JDTestUtilities.dumpBytes( expectedArray[i]));
              
            }            
          }
        }
        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition (clearString.length() == ROW1_CLEAR_S_.length() && passed, "clearString.length()="+clearString.length()+" sb "+ROW1_CLEAR_S_+" "+ message);
        }
      } catch (Exception e) {
        if (expectedException != null) {
          String message1 = e.toString();
          assertCondition(message1.indexOf(expectedException) >= 0,
              "Expected exception '"+message1 +"' should contain '"+expectedException+"'"); 

        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }                                                                           



    // DOM Results currently not supported 
    
    public void Var349() {testSetResult(TABLE37_, "C_CLOB", 4, DOMRESULT, ROW4_S_, DOMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  );    }
    public void Var350() { testSetResult(TABLE37_, "C_CLOB", 5, DOMRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var351() {testSetResult(TABLE37_, "C_CLOB", 6, DOMRESULT, ROW6_S_, DOMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var352() {testSetResult(TABLE37_, "C_CLOB", 7, DOMRESULT, ROW7_S_, DOMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }
    public void Var353() {testSetResult(TABLE37_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   SAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }

    public void Var354() { testSetResult(TABLE37_, "C_CLOB", 5, DOMRESULT, ROW5_S_, SAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var355() { testSetResult(TABLE37_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   SAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var356() { testSetResult(TABLE37_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   SAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    public void Var357() {testSetResult(TABLE37_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var358() { testSetResult(TABLE37_, "C_CLOB", 5, DOMRESULT, ROW5_S_, STAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var359() { testSetResult(TABLE37_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var360() { testSetResult(TABLE37_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    public void Var361() {testSetResult(TABLE37_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   STREAMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }

    public void Var362() { testSetResult(TABLE37_, "C_CLOB", 5, DOMRESULT, ROW5_S_, STREAMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var363() { testSetResult(TABLE37_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   STREAMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var364() { testSetResult(TABLE37_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   STREAMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    
    // SAX Results currently not supported
    public void Var365() { testSetResult(TABLE37_, "C_CLOB", 4, SAXRESULT, ROW4_S_, DOMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var366() { testSetResult(TABLE37_, "C_CLOB", 5, SAXRESULT, ROW5_S_, DOMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var367() { testSetResult(TABLE37_, "C_CLOB", 6, SAXRESULT, ROW6_S_, DOMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var368() { testSetResult(TABLE37_, "C_CLOB", 7, SAXRESULT, ROW7_S_, DOMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    
    public void Var369() { testSetResult(TABLE37_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   SAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var370() { testSetResult(TABLE37_, "C_CLOB", 5, SAXRESULT, ROW5_S_, SAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var371() { testSetResult(TABLE37_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   SAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var372() { testSetResult(TABLE37_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   SAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var373() { testSetResult(TABLE37_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   STAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var374() { testSetResult(TABLE37_, "C_CLOB", 5, SAXRESULT, ROW5_S_, STAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var375() { testSetResult(TABLE37_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   STAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var376() { testSetResult(TABLE37_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   STAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var377() { testSetResult(TABLE37_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   STREAMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var378() { testSetResult(TABLE37_, "C_CLOB", 5, SAXRESULT, ROW5_S_, STREAMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var379() { testSetResult(TABLE37_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   STREAMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var380() { testSetResult(TABLE37_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   STREAMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var381() { testSetResult(TABLE37_, "C_CLOB", 4, STAXRESULT, ROW4_S_,   DOMSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var382() { testSetResult(TABLE37_, "C_CLOB", 5, STAXRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var383() { testSetResult(TABLE37_, "C_CLOB", 6, STAXRESULT, ROW6_S_,   DOMSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var384() { testSetResult(TABLE37_, "C_CLOB", 7, STAXRESULT, ROW7_S_,   DOMSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var385() { testSetResult(TABLE37_, "C_CLOB", 4, STAXRESULT, ROW4_S1208_,   SAXSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); } //needs encodeing???
    public void Var386() { testSetResult(TABLE37_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   SAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var387() { testSetResult(TABLE37_, "C_CLOB", 6, STAXRESULT, ROW6_S1208_,   SAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var388() { testSetResult(TABLE37_, "C_CLOB", 7, STAXRESULT, ROW7_S1208_T_,   SAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var389() { testSetResult(TABLE37_, "C_CLOB", 4, STAXRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var390() { testSetResult(TABLE37_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   STAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var391() { testSetResult(TABLE37_, "C_CLOB", 6, STAXRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var392() { testSetResult(TABLE37_, "C_CLOB", 7, STAXRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var393() { testSetResult(TABLE37_, "C_CLOB", 4, STAXRESULT, ROW4_S1208_,   STREAMSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var394() { testSetResult(TABLE37_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   STREAMSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var395() { testSetResult(TABLE37_, "C_CLOB", 6, STAXRESULT, ROW6_S1208_,   STREAMSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var396() { testSetResult(TABLE37_, "C_CLOB", 7, STAXRESULT, ROW7_S1208_T_,   STREAMSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    

    public void Var397() { testSetResult(TABLE1208_, "C_CLOB", 4, DOMRESULT, ROW4_S_, DOMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var398() { testSetResult(TABLE1208_, "C_CLOB", 5, DOMRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_ , null  ); }
    public void Var399() { testSetResult(TABLE1208_, "C_CLOB", 6, DOMRESULT, ROW6_S_, DOMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var400() { testSetResult(TABLE1208_, "C_CLOB", 7, DOMRESULT, ROW7_S_, DOMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }
    public void Var401() { testSetResult(TABLE1208_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   SAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var402() { testSetResult(TABLE1208_, "C_CLOB", 5, DOMRESULT, ROW5_S_, SAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var403() { testSetResult(TABLE1208_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   SAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var404() { testSetResult(TABLE1208_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   SAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    public void Var405() { testSetResult(TABLE1208_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var406() { testSetResult(TABLE1208_, "C_CLOB", 5, DOMRESULT, ROW5_S_, STAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var407() { testSetResult(TABLE1208_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var408() { testSetResult(TABLE1208_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    public void Var409() {testSetResult(TABLE1208_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   STREAMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }

    public void Var410() { testSetResult(TABLE1208_, "C_CLOB", 5, DOMRESULT, ROW5_S_, STREAMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var411() { testSetResult(TABLE1208_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   STREAMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var412() { testSetResult(TABLE1208_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   STREAMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    
    // SAX Results currently not supported
    public void Var413() { testSetResult(TABLE1208_, "C_CLOB", 4, SAXRESULT, ROW4_S_, DOMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var414() { testSetResult(TABLE1208_, "C_CLOB", 5, SAXRESULT, ROW5_S_, DOMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var415() { testSetResult(TABLE1208_, "C_CLOB", 6, SAXRESULT, ROW6_S_, DOMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var416() { testSetResult(TABLE1208_, "C_CLOB", 7, SAXRESULT, ROW7_S_, DOMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    
    public void Var417() { testSetResult(TABLE1208_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   SAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var418() { testSetResult(TABLE1208_, "C_CLOB", 5, SAXRESULT, ROW5_S_, SAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var419() { testSetResult(TABLE1208_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   SAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var420() { testSetResult(TABLE1208_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   SAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var421() { testSetResult(TABLE1208_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   STAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var422() { testSetResult(TABLE1208_, "C_CLOB", 5, SAXRESULT, ROW5_S_, STAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var423() { testSetResult(TABLE1208_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   STAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var424() { testSetResult(TABLE1208_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   STAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var425() { testSetResult(TABLE1208_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   STREAMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var426() { testSetResult(TABLE1208_, "C_CLOB", 5, SAXRESULT, ROW5_S_, STREAMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var427() { testSetResult(TABLE1208_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   STREAMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var428() { testSetResult(TABLE1208_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   STREAMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var429() { testSetResult(TABLE1208_, "C_CLOB", 4, STAXRESULT, ROW4_S_,   DOMSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var430() { testSetResult(TABLE1208_, "C_CLOB", 5, STAXRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var431() { testSetResult(TABLE1208_, "C_CLOB", 6, STAXRESULT, ROW6_S_,   DOMSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var432() { testSetResult(TABLE1208_, "C_CLOB", 7, STAXRESULT, ROW7_S_,   DOMSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var433() { testSetResult(TABLE1208_, "C_CLOB", 4, STAXRESULT, ROW4_S1208_,   SAXSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var434() { testSetResult(TABLE1208_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   SAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var435() { testSetResult(TABLE1208_, "C_CLOB", 6, STAXRESULT, ROW6_S1208_,   SAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var436() { testSetResult(TABLE1208_, "C_CLOB", 7, STAXRESULT, ROW7_S1208_T_,   SAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var437() { testSetResult(TABLE1208_, "C_CLOB", 4, STAXRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var438() { testSetResult(TABLE1208_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   STAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var439() { testSetResult(TABLE1208_, "C_CLOB", 6, STAXRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var440() { testSetResult(TABLE1208_, "C_CLOB", 7, STAXRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var441() { testSetResult(TABLE1208_, "C_CLOB", 4, STAXRESULT, ROW4_S1208_,   STREAMSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var442() { testSetResult(TABLE1208_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   STREAMSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var443() { testSetResult(TABLE1208_, "C_CLOB", 6, STAXRESULT, ROW6_S1208_,   STREAMSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var444() { testSetResult(TABLE1208_, "C_CLOB", 7, STAXRESULT, ROW7_S1208_T_,   STREAMSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    


  public void Var445() { testSetResult(TABLE1200_, "C_CLOB", 4, DOMRESULT, ROW4_S_, DOMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var446() { testSetResult(TABLE1200_, "C_CLOB", 5, DOMRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var447() { testSetResult(TABLE1200_, "C_CLOB", 6, DOMRESULT, ROW6_S_, DOMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var448() { testSetResult(TABLE1200_, "C_CLOB", 7, DOMRESULT, ROW7_S_, DOMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }
    
    public void Var449() {testSetResult(TABLE1200_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   SAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var450() { testSetResult(TABLE1200_, "C_CLOB", 5, DOMRESULT, ROW5_S_, SAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var451() { testSetResult(TABLE1200_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   SAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var452() { testSetResult(TABLE1200_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   SAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    public void Var453() { testSetResult(TABLE1200_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var454() { testSetResult(TABLE1200_, "C_CLOB", 5, DOMRESULT, ROW5_S_, STAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var455() { testSetResult(TABLE1200_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var456() { testSetResult(TABLE1200_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    public void Var457() { testSetResult(TABLE1200_, "C_CLOB", 4, DOMRESULT, ROW4_S_,   STREAMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null  ); }
    public void Var458() { testSetResult(TABLE1200_, "C_CLOB", 5, DOMRESULT, ROW5_S_, STREAMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null  ); }
    public void Var459() { testSetResult(TABLE1200_, "C_CLOB", 6, DOMRESULT, ROW6_S_,   STREAMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL, null  ); }
    public void Var460() { testSetResult(TABLE1200_, "C_CLOB", 7, DOMRESULT, ROW7_S_,   STREAMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null  ); }

    
    // SAX Results currently not supported
    public void Var461() { testSetResult(TABLE1200_, "C_CLOB", 4, SAXRESULT, ROW4_S_, DOMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var462() { testSetResult(TABLE1200_, "C_CLOB", 5, SAXRESULT, ROW5_S_, DOMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var463() { testSetResult(TABLE1200_, "C_CLOB", 6, SAXRESULT, ROW6_S_, DOMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var464() { testSetResult(TABLE1200_, "C_CLOB", 7, SAXRESULT, ROW7_S_, DOMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    
    public void Var465() { testSetResult(TABLE1200_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   SAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var466() { testSetResult(TABLE1200_, "C_CLOB", 5, SAXRESULT, ROW5_S_, SAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var467() { testSetResult(TABLE1200_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   SAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var468() { testSetResult(TABLE1200_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   SAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var469() { testSetResult(TABLE1200_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   STAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var470() { testSetResult(TABLE1200_, "C_CLOB", 5, SAXRESULT, ROW5_S_, STAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var471() { testSetResult(TABLE1200_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   STAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var472() { testSetResult(TABLE1200_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   STAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var473() { testSetResult(TABLE1200_, "C_CLOB", 4, SAXRESULT, ROW4_S_,   STREAMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var474() { testSetResult(TABLE1200_, "C_CLOB", 5, SAXRESULT, ROW5_S_, STREAMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var475() { testSetResult(TABLE1200_, "C_CLOB", 6, SAXRESULT, ROW6_S_,   STREAMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var476() { testSetResult(TABLE1200_, "C_CLOB", 7, SAXRESULT, ROW7_S_,   STREAMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var477() { testSetResult(TABLE1200_, "C_CLOB", 4, STAXRESULT, ROW4_S_,   DOMSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var478() { testSetResult(TABLE1200_, "C_CLOB", 5, STAXRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var479() { testSetResult(TABLE1200_, "C_CLOB", 6, STAXRESULT, ROW6_S_,   DOMSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var480() { testSetResult(TABLE1200_, "C_CLOB", 7, STAXRESULT, ROW7_S_,   DOMSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var481() { testSetResult(TABLE1200_, "C_CLOB", 4, STAXRESULT, ROW4_S1208_,   SAXSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var482() { testSetResult(TABLE1200_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   SAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var483() { testSetResult(TABLE1200_, "C_CLOB", 6, STAXRESULT, ROW6_S1208_,   SAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var484() { testSetResult(TABLE1200_, "C_CLOB", 7, STAXRESULT, ROW7_S1208_T_,   SAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var485() { testSetResult(TABLE1200_, "C_CLOB", 4, STAXRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var486() { testSetResult(TABLE1200_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   STAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var487() { testSetResult(TABLE1200_, "C_CLOB", 6, STAXRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var488() { testSetResult(TABLE1200_, "C_CLOB", 7, STAXRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    public void Var489() { testSetResult(TABLE1200_, "C_CLOB", 4, STAXRESULT, ROW4_S1208_,   STREAMSOURCE, EXPECTED_ROW4_ST_PLUS_S1208_, null ); }
    public void Var490() { testSetResult(TABLE1200_, "C_CLOB", 5, STAXRESULT, ROW5_S_,   STREAMSOURCE, EXPECTED_ROW5_ST_PLUS_S1208_, null ); }
    public void Var491() { testSetResult(TABLE1200_, "C_CLOB", 6, STAXRESULT, ROW6_S1208_,   STREAMSOURCE, EXPECTED_ROW6_ST_PLUS_S1208_, null ); }
    public void Var492() { testSetResult(TABLE1200_, "C_CLOB", 7, STAXRESULT, ROW7_S1208_T_,   STREAMSOURCE, EXPECTED_ROW7_ST_PLUS_S1208_, null ); }

    


    // Test XMLTEXT 
    public void Var493() {
	if (getRelease() <= JDTestDriver.RELEASE_V6R1M0) {
	    notApplicable("V7R1 test with XMLTEXT");
	    return; 
	} 
	if (checkJdbc40()) {

	    String[][] tests = {
	    /* SQLStatement  */ /* Result if result set */
		{"select XMLTEXT('hello') from sysibm.sysdummy1", "hello"},
		{"select XMLTEXT(CAST('hello' AS VARGRAPHIC(100) CCSID 13488)) from sysibm.sysdummy1", "hello"},


		{"CALL QSYS.QCMDEXC('CRTDUPOBJ OBJ(QAQQINI) FROMLIB(QSYS) OBJTYPE(*FILE) TOLIB("+collection_+") DATA(*YES)                        ',000000100.00000)","IGNORE_EXCEPTION"},
		{"UPDATE "+collection_+".QAQQINI SET QQVAL = '13488'  WHERE QQPARM =  'SQL_XML_DATA_CCSID'", null}, 
		{"CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection_+")                                 ',000000040.00000)",null},
		{"select XMLTEXT('hello') from sysibm.sysdummy1", "hello"},
		{"select XMLTEXT(CAST('hello' AS VARGRAPHIC(100) CCSID 13488)) from sysibm.sysdummy1", "hello"},



		{"UPDATE "+collection_+".QAQQINI SET QQVAL = '297'  WHERE QQPARM =  'SQL_XML_DATA_CCSID'", null}, 
		{"CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection_+")                                 ',000000040.00000)",null},
		{"select XMLTEXT('hello') from sysibm.sysdummy1", "hello"},
		{"select XMLTEXT(CAST('hello' AS VARGRAPHIC(100) CCSID 13488)) from sysibm.sysdummy1", "hello"},

		{"UPDATE "+collection_+".QAQQINI SET QQVAL = '1208'  WHERE QQPARM =  'SQL_XML_DATA_CCSID'", null}, 
		{"CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection_+")                                 ',000000040.00000)",null},
		{"select XMLTEXT('hello') from sysibm.sysdummy1", "hello"},
		{"select XMLTEXT(CAST('hello' AS VARGRAPHIC(100) CCSID 13488)) from sysibm.sysdummy1", "hello"},

		{"UPDATE "+collection_+".QAQQINI SET QQVAL = '1388'  WHERE QQPARM =  'SQL_XML_DATA_CCSID'", null}, 
		{"CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection_+")                                 ',000000040.00000)",null},
		{"select XMLTEXT('hello') from sysibm.sysdummy1", "hello"},
		{"select XMLTEXT(CAST('hello' AS VARGRAPHIC(100) CCSID 13488)) from sysibm.sysdummy1", "hello"},

		{"UPDATE "+collection_+".QAQQINI SET QQVAL = '37'  WHERE QQPARM =  'SQL_XML_DATA_CCSID'", null}, 
		{"CALL QSYS.QCMDEXC('CHGQRYA QRYOPTLIB("+collection_+")                                 ',000000040.00000)",null},
		{"select XMLTEXT('hello') from sysibm.sysdummy1", "hello"},
		{"select XMLTEXT(CAST('hello' AS VARGRAPHIC(100) CCSID 13488)) from sysibm.sysdummy1", "hello"},


/*
UPDATE K2KUADYN06.QAQQINI SET QQVAL = '37'  WHERE QQPARM =  'SQL_XML_DATA_CCSID'
CALL QSYS.QCMDEXC('Chgjob ccsid(5026)                ',000000020.00000)
*/
	    };

	    sb = new StringBuffer();
	    sb.append("Added 7/18/2013 -- to test XMLTEST\n ");
	    try {
		boolean passed = true;
		Statement s = connection_.createStatement();
		for (int i = 0; i < tests.length; i++) {
		    String sql = tests[i][0];
		    String expected = tests[i][1];
		    sb.append("Executing " + sql + "\n");
		    if (expected != null) {
			if (expected.indexOf("IGNORE_EXCEPTION") >= 0) {
			    try { 
				s.executeUpdate(sql);
			    }  catch (Exception e) { 
				sb.append("Ignored "+e+"\n"); 
				printStackTraceToStringBuffer(e, sb);
			    }
			} else {
			    ResultSet rs = s.executeQuery(sql);
			    rs.next();
			    String value = rs.getString(1);
			    if (!expected.equals(value)) {
				passed = false;
				sb.append("got string " + value + " expected " + expected + "\n");
			    } else {
				sb.append("got string " + value + "\n");
			    }
			    try {
				Object obj = rs.getObject(1);
				value = JDReflectionUtil.callMethod_S(obj, "getString");
				if (!expected.equals(value)) {
				    passed = false;
				    sb.append("got  object " + value + " expected " + expected
					      + "\n");
				} else {
				    sb.append("got  object " + value + "\n");
				}
			    } catch (Exception e) {
				passed = false;
				sb.append("got exception on getObject " + e + "\n");
				printStackTraceToStringBuffer(e, sb);

			    }

			    try {
				Clob clob = rs.getClob(1);
				int length = (int) clob.length();
				value = clob.getSubString(1, length);
				if (!expected.equals(value)) {
				    passed = false;
				    sb.append("got  clob " + value + " expected " + expected + "\n");
				} else {
				    sb.append("got  clob " + value + "\n");
				}
			    } catch (Exception e) {
				passed = false;
				sb.append("got exception on getClob " + e + "\n");
				printStackTraceToStringBuffer(e, sb);

			    }
			}

		    } else {
			s.executeUpdate(sql);
		    }

		}
		assertCondition(passed,sb);

	    } catch (Exception e) {
		failed(e, "Unexpected Exception -- " + sb.toString());
	    }

	}
    }

           
    
}
