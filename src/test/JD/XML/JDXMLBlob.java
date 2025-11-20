///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLBlob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDXMLBlob.java
//
// Classes:      JDXMLBlob
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.XML;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;                          //@C1A
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Blob;
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
import test.JVMInfo;
import test.JD.JDTestUtilities;



/**
Testcase JDXMLBlob.  This tests the following method
of the JDBC XML class when used against a blob column:

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
public class JDXMLBlob
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDXMLBlob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDXMLTest.main(newArgs); 
   }


    public static final String DOMSOURCE="javax.xml.transform.dom.DOMSource"; 
    public static final String SAXSOURCE="javax.xml.transform.sax.SAXSource"; 
    public static final String STAXSOURCE="javax.xml.transform.stax.StAXSource"; 
    public static final String STREAMSOURCE="javax.xml.transform.stream.StreamSource";

    public static final String DOMRESULT="javax.xml.transform.dom.DOMResult"; 
    public static final String SAXRESULT="javax.xml.transform.sax.SAXResult"; 
    public static final String STAXRESULT="javax.xml.transform.stax.StAXResult"; 
    public static final String STREAMRESULT="javax.xml.transform.stream.StreamResult";


    // Private data.
    private Statement           statement1_;
    private Statement           statement2_;
    private Statement           statement37_;
    private Statement           statement1208_;
    private Statement           statement1200_;
    private Statement           statement1202_;
    private ResultSet           rs37_;
    private ResultSet           rs1208_;
    private ResultSet           rs1200_;
    private ResultSet           rs1202_;
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


    public static String TABLE37_          = JDXMLTest.COLLECTION + ".XMLBLB37";
    public static String TABLE1208_          = JDXMLTest.COLLECTION + ".XMLBLB1208";
    public static String TABLE1200_          = JDXMLTest.COLLECTION + ".XMLBLB1200";
    public static String TABLE1202_          = JDXMLTest.COLLECTION + ".XMLBLB1202";
    public static String TABLE819_          = JDXMLTest.COLLECTION + ".XMLBLB819";

    public static       String  ROW1_S_ = "";
    public static       String  ROW2_S_ = "<?xml version=\"1.0\" ?><T/>";
    public static       String  ROW2_S_NO_DECL="<T/>";
    public static       String  ROW2_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T/>"; 
    public static       String  ROW2_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><T/>"; 

    public static       String  ROW3_S_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T/>";
    public static       String  ROW3_S_NO_DECL="<T/>"; 
    public static       String  ROW3_S37_ = "<?xml version=\"1.0\" encoding=\"IBM-37\"?><T/>";
    public static       String  ROW3_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><T/>";
    public static       String  ROW3_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><T/>";
    public static       String  ROW3_S1202_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?><T/>";
    public static       String  ROW4_S_ = "<?xml version=\"1.0\" ?> <TOP attrib=\"TOP\">TOP</TOP>";

    public static       String  ROW4_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1202_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW4_S1208_STANDALONE_ = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>";

    public static       String  ROW4_S_NO_DECL_ = " <TOP attrib=\"TOP\">TOP</TOP>";
   public static       String  ROW4_S_NO_DECL_NS_ = "<TOP attrib=\"TOP\">TOP</TOP>";

    public static       String  ROW5_S_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S37_ = "<?xml version=\"1.0\" encoding=\"IBM-37\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1202_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1208_T_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1200_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S1202_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW5_S_NO_DECL_ = " <TOP attrib=\"TOP\">TOP</TOP>";
 public static       String  ROW5_S_NO_DECL_NS_ = "<TOP attrib=\"TOP\">TOP</TOP>";




    public static       String  ROW6_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S1208_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW6_S1200_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S_ = " \t\r\n<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S2_ = " \t\n<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW7_S1208_T_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>"; 
    public static       String  ROW7_S1200_T_ = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><TOP attrib=\"TOP\">TOP</TOP>"; 
    public static       String  ROW8_S_ = "NOT XML"; 
    public static       String  ROW9_S_ = "<TOP attrib=\"TOP\">TOP</TOP>";
    public static       String  ROW10_S_ = "<?xml version=\"1.0\" ?>1";
    public static       String  ROW12_S_ = "";
    public static       String  ROW13_S_ = "";
    public static       String  ROW14_S_ = "";
    public static       String  ROW15_S_ = "";
    public static       String  ROW16_S_ = "";
    public static       String  ROW17_S_ = "";
    public static       String  ROW18_S_ = "";
    public static       String  ROW19_S_ = "";
    public static       String  ROW20_S_ = "";

   public static String EXPECTED_ROW2_S1208_PLUS_STANDALONE_[] = {
       ROW2_S1208_,
       ROW2_S1208_STANDALONE_
   }; 


   public static String EXPECTED_ROW4_S1208_PLUS_STANDALONE_[] = {
       ROW4_S1208_,
       ROW4_S1208_STANDALONE_
   }; 

    public static String EXPECTED_ROW5_S1208_T_PLUS_STANDALONE_37_[] = {
	ROW5_S1208_T_,
        "<?xml version=\"1.0\" encoding=\"IBM-37\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>"
    }; 


    public static String EXPECTED_ROW5_S1208_T_PLUS_STANDALONE_[] = {
	ROW5_S1208_T_,
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>"
    }; 

    public static String EXPECTED_ROW5_S1208_T_PLUS_STANDALONE1200_[] = {
	ROW5_S1208_T_,
        "<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>"
    }; 

    public static String EXPECTED_ROW5_S1208_T_PLUS_STANDALONELE_[] = {
	ROW5_S1208_T_,
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>"
    }; 


    public static String[] INVALID_XML_CHARACTER_OR_CONTENT_NOT_ALLOWED = {
	"An invalid XML character",       /* 166P 169P */ 
        "Content is not allowed in prolog"       /* 16 */ 
    }; 


    public static       byte[]  ROW2_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E, 0x3c, 0x54, 0x2f, 0x3e};
    public static       byte[]  ROW3_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x22, 0x3F, 0x3E, 0x3c, 0x54, 0x2f, 0x3e};
    public static       byte[]  ROW4_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW5_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x69, 0x6E, 0x67, 0x3D, 0x22, 0x55, 0x54, 0x46, 0x2D, 0x38, 0x22, 0x3F, 0x3E, 0x20, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW6_1208_      = {0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW7_1208_      = {0x20, 0x09, 0x0d, 0x0a, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW8_1208_      = {0x4E, 0x4F, 0x54, 0x20, 0x58, 0x4D, 0x4C };
    public static       byte[]  ROW9_1208_      = {(byte) 0xef, (byte)0xbb, (byte) 0xbf, 0x3C, 0x54, 0x4F, 0x50, 0x20, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x3D, 0x22, 0x54, 0x4F, 0x50, 0x22, 0x3E, 0x54, 0x4F, 0x50, 0x3C, 0x2F, 0x54, 0x4F, 0x50, 0x3E};
    public static       byte[]  ROW10_1208_      = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2E, 0x30, 0x22, 0x20, 0x3F, 0x3E, 0x31};


    public static       byte[]  ROW2_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x3F, 0x00,0x3E, 0x00,0x3c, 0x00,0x54, 0x00,0x2f, 0x00,0x3e};
    public static       byte[]  ROW3_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F, 0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22, 0x00,0x55, 0x00,0x54, 0x00,0x46, 0x00,0x2D, 0x00,0x31, 0x00,0x36, 0x00,0x22, 0x00,0x3F, 0x00,0x3E, 0x00,0x3c, 0x00,0x54, 0x00,0x2f, 0x00,0x3e};
    public static       byte[]  ROW4_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x3F, 0x00,0x3E, 0x00,0x20, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW5_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x65, 0x00,0x6E, 0x00,0x63, 0x00,0x6F, 0x00,0x64, 0x00,0x69, 0x00,0x6E, 0x00,0x67, 0x00,0x3D, 0x00,0x22, 0x00,0x55, 0x00,0x54, 0x00,0x46, 0x00,0x2D, 0x00,0x31, 0x00,0x36, 0x00,0x22, 0x00,0x3F, 0x00,0x3E, 0x00,0x20, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW6_1200_      = {0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW7_1200_      = {0x00,0x20, 0x00,0x09, 0x00,0x0d, 0x00,0x0a, 0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW8_1200_      = {0x00,0x4E, 0x00,0x4F, 0x00,0x54, 0x00,0x20, 0x00,0x58, 0x00,0x4D, 0x00,0x4C };
    public static       byte[]  ROW9_1200_      = {(byte) 0xfe,(byte) 0xff,  0x00,0x3C, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x20, 0x00,0x61, 0x00,0x74, 0x00,0x74, 0x00,0x72, 0x00,0x69, 0x00,0x62, 0x00,0x3D, 0x00,0x22, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x22, 0x00,0x3E, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3C, 0x00,0x2F, 0x00,0x54, 0x00,0x4F, 0x00,0x50, 0x00,0x3E};
    public static       byte[]  ROW10_1200_      = {0x00,0x3C, 0x00,0x3F, 0x00,0x78, 0x00,0x6D, 0x00,0x6C, 0x00,0x20, 0x00,0x76, 0x00,0x65, 0x00,0x72, 0x00,0x73, 0x00,0x69, 0x00,0x6F, 0x00,0x6E, 0x00,0x3D, 0x00,0x22, 0x00,0x31, 0x00,0x2E, 0x00,0x30, 0x00,0x22, 0x00,0x20, 0x00,0x3F, 0x00,0x3E, 0x00,0x31};


    public static       byte[]  ROW2_1202_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x3F,0x00, 0x3E,0x00, 0x3c,0x00, 0x54,0x00, 0x2f,0x00, 0x3e,0x00};
    public static       byte[]  ROW3_1202_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x65,0x00, 0x6E,0x00, 0x63,0x00, 0x6F,0x00, 0x64,0x00, 0x69,0x00, 0x6E,0x00, 0x67,0x00, 0x3D,0x00, 0x22,0x00, 0x55,0x00, 0x54,0x00, 0x46,0x00, 0x2D,0x00, 0x31,0x00, 0x36,0x00, 0x4c,0x00, 0x45,0x00, 0x22,0x00, 0x3F,0x00, 0x3E,0x00, 0x3c,0x00, 0x54,0x00, 0x2f,0x00, 0x3e,0x00};
    public static       byte[]  ROW4_1202_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x3F,0x00, 0x3E,0x00, 0x20,0x00, 0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW5_1202_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x65,0x00, 0x6E,0x00, 0x63,0x00, 0x6F,0x00, 0x64,0x00, 0x69,0x00, 0x6E,0x00, 0x67,0x00, 0x3D,0x00, 0x22,0x00, 0x55,0x00, 0x54,0x00, 0x46,0x00, 0x2D,0x00, 0x31,0x00, 0x36,0x00, 0x4c,0x00, 0x45,0x00, 0x22,0x00, 0x3F,0x00, 0x3E,0x00, 0x20,0x00, 0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW6_1202_      = {0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW7_1202_      = {0x20,0x00, 0x09,0x00, 0x0d,0x00, 0x0a,0x00, 0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW8_1202_      = {0x4E,0x00, 0x4F,0x00, 0x54,0x00, 0x20,0x00, 0x58,0x00, 0x4D,0x00, 0x4C,0x00 };
    public static       byte[]  ROW9_1202_      = {(byte) 0xff,(byte) 0xfe,  0x3C,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x20,0x00, 0x61,0x00, 0x74,0x00, 0x74,0x00, 0x72,0x00, 0x69,0x00, 0x62,0x00, 0x3D,0x00, 0x22,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x22,0x00, 0x3E,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3C,0x00, 0x2F,0x00, 0x54,0x00, 0x4F,0x00, 0x50,0x00, 0x3E,0x00};
    public static       byte[]  ROW10_1202_      = {0x3C,0x00, 0x3F,0x00, 0x78,0x00, 0x6D,0x00, 0x6C,0x00, 0x20,0x00, 0x76,0x00, 0x65,0x00, 0x72,0x00, 0x73,0x00, 0x69,0x00, 0x6F,0x00, 0x6E,0x00, 0x3D,0x00, 0x22,0x00, 0x31,0x00, 0x2E,0x00, 0x30,0x00, 0x22,0x00, 0x20,0x00, 0x3F,0x00, 0x3E,0x00, 0x31,0x00};








    public static       byte[]  ROW1_         = {}; 
    public static       byte[]  ROW2_37_      = {0x4C, 0x6F,(byte)0xA7,(byte)0x94,(byte)0x93, 0x40,(byte)0xA5,(byte)0x85,(byte)0x99,(byte)0xA2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7E, 0x7F,(byte)0xF1, 0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x6F, (byte)0x6E,(byte)0x4c,(byte)0xe3,(byte)0x61,(byte)0x6e};
    public static       byte[]  ROW3_37_      = { 0x4C, 0x6F, (byte)0xA7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xA5, (byte)0x85, (byte)0x99, (byte)0xA2, (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x85, (byte)0x95, (byte)0x83, (byte)0x96, (byte)0x84, (byte)0x89, (byte)0x95, (byte)0x87, (byte)0x7E, (byte)0x7F, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x60, (byte)0xF3, (byte)0xF7, (byte)0x7F, (byte)0x6F, (byte)0x6E,(byte)0x4c,(byte)0xe3,(byte)0x61,(byte)0x6e } ;
    public static       byte[]  ROW4_37_      = { (byte)0x4C, (byte)0x6F, (byte)0xA7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xA5, (byte)0x85, (byte)0x99, (byte)0xA2, (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x6F, (byte)0x6E, (byte)0x40, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E} ;
    public static       byte[]  ROW5_37_      = { (byte)0x4C, (byte)0x6F, (byte)0xA7, (byte)0x94, (byte)0x93, (byte)0x40, (byte)0xA5, (byte)0x85, (byte)0x99, (byte)0xA2, (byte)0x89, (byte)0x96, (byte)0x95, (byte)0x7E, (byte)0x7F, (byte)0xF1, (byte)0x4B, (byte)0xF0, (byte)0x7F, (byte)0x40, (byte)0x85, (byte)0x95, (byte)0x83, (byte)0x96, (byte)0x84, (byte)0x89, (byte)0x95, (byte)0x87, (byte)0x7E, (byte)0x7F, (byte)0xC9, (byte)0xC2, (byte)0xD4, (byte)0x60, (byte)0xF3, (byte)0xF7, (byte)0x7F, (byte)0x6F, (byte)0x6E, (byte)0x40, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E}; 
    public static       byte[]  ROW6_37_      = { (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E } ;
    public static       byte[]  ROW7_37_      = { (byte)0x40, (byte)0x05, (byte)0x0d, (byte)0x15, (byte)0x4C, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x40, (byte)0x81, (byte)0xA3, (byte)0xA3, (byte)0x99, (byte)0x89, (byte)0x82, (byte)0x7E, (byte)0x7F, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x7F, (byte)0x6E, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x4C, (byte)0x61, (byte)0xE3, (byte)0xD6, (byte)0xD7, (byte)0x6E } ;
    public static       byte[]  ROW8_37_      = { (byte)0xD5, (byte)0xD6, (byte)0xE3, (byte)0x40, (byte)0xE7, (byte)0xD4, (byte)0xD3  };   
    public static       byte[]  ROW9_37_      = {0x4C, 0x6F,(byte)0xA7,(byte)0x94,(byte)0x93, 0x40,(byte)0xA5,(byte)0x85,(byte)0x99,(byte)0xA2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7E, 0x7F,(byte)0xF1, 0x4B, (byte)0xF0, 0x7F, 0x40, 0x6F, 0x6E};
    public static       byte[]  ROW10_37_      = {0x4C, 0x6F,(byte)0xA7,(byte)0x94,(byte)0x93, 0x40,(byte)0xA5,(byte)0x85,(byte)0x99,(byte)0xA2,(byte)0x89,(byte)0x96,(byte)0x95,(byte)0x7E, 0x7F,(byte)0xF1, 0x4B, (byte)0xF0, 0x7F, 0x40, 0x6F, 0x6E, (byte) 0xf1};

    public static       byte[]  ROW11_         = {};
    public static       byte[]  ROW12_         = {};
    public static       byte[]  ROW13_         = {};
    public static       byte[]  ROW14_         = {};
    public static       byte[]  ROW15_         = {};
    public static       byte[]  ROW16_         = {};
    public static       byte[]  ROW17_         = {};
    public static       byte[]  ROW18_         = {};
    public static       byte[]  ROW19_         = {};
    public static       byte[]  ROW20_         = {};

    // UTF8 Rows
    
    public static StringBuffer message = new StringBuffer(); 
    public String lobThreshold = ";lob threshold=100000";

    /**
    Constructor.
    **/
    public JDXMLBlob (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDXMLBlob",
               namesAndVars, runMode, fileOutputStream,
               password);
    }

    public JDXMLBlob (AS400 systemObject,
        String testname, 
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password)
    {
      super (systemObject, testname,
          namesAndVars, runMode, fileOutputStream,
          password);
    }
    

    /**
    Performs setup needed before running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
       TABLE37_          = JDXMLTest.COLLECTION + ".XMLBLB37";
       TABLE1208_          = JDXMLTest.COLLECTION + ".XMLBLB1208";
       TABLE1200_          = JDXMLTest.COLLECTION + ".XMLBLB1200";
       TABLE1202_          = JDXMLTest.COLLECTION + ".XMLBLB1202";
       TABLE819_          = JDXMLTest.COLLECTION + ".XMLBLB819";

        if (isJdbc40 ()) {

	    if (getDriver() == JDTestDriver.DRIVER_JCC) {

		String url_ = "jdbc:db2://"+systemObject_.getSystemName()+":"+JDTestDriver.jccPort+"/"+JDTestDriver.jccDatabase + lobThreshold;
		connection_ = testDriver_.getConnection (url_, systemObject_.getUserId(), encryptedPassword_ );
	    } else { 
		String url = baseURL_
		  + lobThreshold;

		connection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_ );
	    }

            statement1_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);

            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

	    try {
		statement1_.executeUpdate ("DROP TABLE " + TABLE37_);
	    } catch (Exception e) {
	    }
            statement1_.executeUpdate ("CREATE TABLE " + TABLE37_ 
                                      + "(C_BLOB BLOB(50000))");

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                                                                 + TABLE37_ + " (C_BLOB) VALUES (?)");
            ps.setBytes (1, ROW1_);     ps.executeUpdate ();
            ps.setBytes (1, ROW2_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW3_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW4_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW5_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW6_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW7_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW8_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW9_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW10_37_); ps.executeUpdate ();
            ps.setBytes (1, ROW11_);     ps.executeUpdate ();
            ps.setBytes (1, ROW12_);     ps.executeUpdate ();
            ps.setBytes (1, ROW13_);     ps.executeUpdate ();
            ps.setBytes (1, ROW14_);     ps.executeUpdate ();
            ps.setBytes (1, ROW15_);     ps.executeUpdate ();
            ps.setBytes (1, ROW16_);     ps.executeUpdate ();
            ps.setBytes (1, ROW17_);     ps.executeUpdate ();
            ps.setBytes (1, ROW18_);     ps.executeUpdate ();
            ps.setBytes (1, ROW19_);     ps.executeUpdate ();
            ps.setBytes (1, ROW20_);     ps.executeUpdate ();
            // Rows 21 and above are those that are updated. 
            ps.setBytes (1, ROW1_);     ps.executeUpdate ();
            ps.setBytes (1, ROW2_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW3_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW4_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW5_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW6_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW7_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW8_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW9_37_);  ps.executeUpdate ();
            ps.setBytes (1, ROW10_37_); ps.executeUpdate ();


            ps.close ();



            try {
                statement1_.executeUpdate ("DROP TABLE " + TABLE1208_);
            } catch (Exception e) {
            }



          statement1_.executeUpdate ("CREATE TABLE " + TABLE1208_ 
                                    + "(C_BLOB BLOB(50000))");

          ps = connection_.prepareStatement ("INSERT INTO " 
                                                               + TABLE1208_ + " (C_BLOB) VALUES (?)");
          ps.setBytes (1, ROW1_);           ps.executeUpdate ();
          ps.setBytes (1, ROW2_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW3_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW4_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW5_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW6_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW7_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW8_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW9_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW10_1208_);     ps.executeUpdate ();
          ps.setBytes (1, ROW11_);          ps.executeUpdate ();
          ps.setBytes (1, ROW12_);          ps.executeUpdate ();
          ps.setBytes (1, ROW13_);          ps.executeUpdate ();
          ps.setBytes (1, ROW14_);          ps.executeUpdate ();
          ps.setBytes (1, ROW15_);          ps.executeUpdate ();
          ps.setBytes (1, ROW16_);          ps.executeUpdate ();
          ps.setBytes (1, ROW17_);          ps.executeUpdate ();
          ps.setBytes (1, ROW18_);          ps.executeUpdate ();
          ps.setBytes (1, ROW19_);          ps.executeUpdate ();
          ps.setBytes (1, ROW20_);          ps.executeUpdate ();
          ps.setBytes (1, ROW1_);           ps.executeUpdate ();
          ps.setBytes (1, ROW2_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW3_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW4_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW5_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW6_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW7_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW8_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW9_1208_);      ps.executeUpdate ();
          ps.setBytes (1, ROW10_1208_);     ps.executeUpdate ();

          ps.close ();


            try {
                statement1_.executeUpdate ("DROP TABLE " + TABLE1200_);
            } catch (Exception e) {
            }



          statement1_.executeUpdate ("CREATE TABLE " + TABLE1200_ 
                                    + "(C_BLOB BLOB(50000))");

          ps = connection_.prepareStatement ("INSERT INTO " 
                                                               + TABLE1200_ + " (C_BLOB) VALUES (?)");
          ps.setBytes (1, ROW1_);                ps.executeUpdate ();
          ps.setBytes (1, ROW2_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW3_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW4_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW5_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW6_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW7_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW8_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW9_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW10_1200_);          ps.executeUpdate ();
          ps.setBytes (1, ROW11_);          ps.executeUpdate ();
          ps.setBytes (1, ROW12_);          ps.executeUpdate ();
          ps.setBytes (1, ROW13_);          ps.executeUpdate ();
          ps.setBytes (1, ROW14_);          ps.executeUpdate ();
          ps.setBytes (1, ROW15_);          ps.executeUpdate ();
          ps.setBytes (1, ROW16_);          ps.executeUpdate ();
          ps.setBytes (1, ROW17_);          ps.executeUpdate ();
          ps.setBytes (1, ROW18_);          ps.executeUpdate ();
          ps.setBytes (1, ROW19_);          ps.executeUpdate ();
          ps.setBytes (1, ROW20_);          ps.executeUpdate ();
          ps.setBytes (1, ROW1_);                ps.executeUpdate ();
          ps.setBytes (1, ROW2_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW3_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW4_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW5_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW6_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW7_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW8_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW9_1200_);           ps.executeUpdate ();
          ps.setBytes (1, ROW10_1200_);          ps.executeUpdate ();


          ps.close ();

            try {
                statement1_.executeUpdate ("DROP TABLE " + TABLE1202_);
            } catch (Exception e) {
            }



          statement1_.executeUpdate ("CREATE TABLE " + TABLE1202_ 
                                    + "(C_BLOB BLOB(50000))");

          ps = connection_.prepareStatement ("INSERT INTO " 
                                                               + TABLE1202_ + " (C_BLOB) VALUES (?)");
          ps.setBytes (1, ROW1_);                ps.executeUpdate ();
          ps.setBytes (1, ROW2_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW3_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW4_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW5_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW6_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW7_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW8_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW9_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW10_1202_);          ps.executeUpdate ();
          ps.setBytes (1, ROW11_);          ps.executeUpdate ();
          ps.setBytes (1, ROW12_);          ps.executeUpdate ();
          ps.setBytes (1, ROW13_);          ps.executeUpdate ();
          ps.setBytes (1, ROW14_);          ps.executeUpdate ();
          ps.setBytes (1, ROW15_);          ps.executeUpdate ();
          ps.setBytes (1, ROW16_);          ps.executeUpdate ();
          ps.setBytes (1, ROW17_);          ps.executeUpdate ();
          ps.setBytes (1, ROW18_);          ps.executeUpdate ();
          ps.setBytes (1, ROW19_);          ps.executeUpdate ();
          ps.setBytes (1, ROW20_);          ps.executeUpdate ();
          ps.setBytes (1, ROW1_);                ps.executeUpdate ();
          ps.setBytes (1, ROW2_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW3_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW4_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW5_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW6_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW7_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW8_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW9_1202_);           ps.executeUpdate ();
          ps.setBytes (1, ROW10_1202_);          ps.executeUpdate ();



          ps.close ();



	   if (getDriver() == JDTestDriver.DRIVER_JCC) {
	       // SQL0270 from JCC when SCROLL_INSENSITIVIE
	       // DB2 SQL Error: SQLCODE=-270, SQLSTATE=42997, SQLERRMC=63,
               // 63 A column with a LOB type, distinct type on a LOB type, or structured type cannot be specified in the select-list of an insensitive scrollable cursor
               // 53  A column with a LONG VARCHAR, LONG VARGRAPHIC, DATALINK, LOB, XML type, distinct type on any of these types, or structured type cannot be specified in the select-list of a scrollable cursor.
	       statement1208_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							     ResultSet.CONCUR_UPDATABLE);
	       statement1200_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							     ResultSet.CONCUR_UPDATABLE);
	       statement1202_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							     ResultSet.CONCUR_UPDATABLE);

	       statement37_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							   ResultSet.CONCUR_UPDATABLE);

	   } else { 
	       statement1208_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							     ResultSet.CONCUR_UPDATABLE);
	       statement1200_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							     ResultSet.CONCUR_UPDATABLE);
	       statement1202_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							     ResultSet.CONCUR_UPDATABLE);

	       statement37_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							   ResultSet.CONCUR_UPDATABLE);
	   }

          rs1208_ = statement1208_.executeQuery ("SELECT * FROM " + TABLE1208_);
          rs1200_ = statement1200_.executeQuery ("SELECT * FROM " + TABLE1200_);
          rs1202_ = statement1202_.executeQuery ("SELECT * FROM " + TABLE1202_);
	  rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);

        
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
	    statement1202_.close ();

            statement2_.close ();
	    statement1_.executeUpdate ("DROP TABLE " + TABLE37_);
	    statement1_.executeUpdate ("DROP TABLE " + TABLE1208_);
	    statement1_.executeUpdate ("DROP TABLE " + TABLE1200_);
	    statement1_.executeUpdate ("DROP TABLE " + TABLE1202_);
            statement1_.close ();

            connection_.close ();
            connection_ = null; 

        }
    }


    public boolean isXmlDeclarationStripped() {
	return isXmlSupported() ; 
    } 

    public void testMethodAfterFree(String method) {
	if (checkJdbc40 ()) {
	    try {

		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);
		    rs37_.next();
		    rs37_.next(); 
		} else { 
		    rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);
		    rs37_.absolute (2);
		}

		Object xml = JDReflectionUtil.callMethod_OS(rs37_, "getSQLXML", "C_BLOB");
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
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);
		rs37_.next();
		rs37_.next(); 
	    } else { 
	        rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);
		rs37_.absolute (2);
	    }
          Object xml = JDReflectionUtil.callMethod_OS(rs37_, "getSQLXML", "C_BLOB");
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
    



    public void testGetBinaryStream(ResultSet rs, int row, byte[] expected) {

	if (checkJdbc40 ()) {
	    try {
		message.setLength(0);

		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    if (rs == rs37_) { 
			rs37_ = statement37_.executeQuery ("SELECT * FROM " + TABLE37_);
			rs = rs37_; 
		    } else if (rs == rs1208_) { 
			rs1208_ = statement1208_.executeQuery ("SELECT * FROM " + TABLE1208_);
			rs = rs1208_;
		    } else if (rs == rs1200_) { 

			rs1200_ = statement1200_.executeQuery ("SELECT * FROM " + TABLE1200_);
			rs = rs1200_;
		    } else if (rs == rs1202_) { 

			rs1202_ = statement1202_.executeQuery ("SELECT * FROM " + TABLE1202_);
			rs = rs1202_;
		    } else {
			throw new SQLException("Unable to determine RS "); 
		    }

		    for (int i = 0; i < row; i++) { 
			rs37_.next();
		    }

		} else {
		    rs.absolute (row);

		}
		Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_BLOB");
		InputStream v = (InputStream) JDReflectionUtil.callMethod_O(xml, "getBinaryStream");
		boolean passed=compare (v, expected,message);
		if (!passed) {
		    xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_BLOB");
		    v = (InputStream) JDReflectionUtil.callMethod_O(xml, "getBinaryStream");
                    byte[] buffer = new byte[10000]; 
		    int bytesRead = v.read(buffer);
                    byte[] output = new byte[bytesRead]; 
                    for (int i = 0; i < bytesRead; i++) {
                      output[i] = buffer[i]; 
                    }
		    message.append("\nGot      : "+JDTestUtilities.dumpBytes(output));
		    message.append("\nExpected : "+JDTestUtilities.dumpBytes(expected)); 


		} 
		assertCondition (passed, "Comparision failed :"+message.toString());
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    } 
    /**
    getBinaryStream() - CCSID37 and lobs of various sizes
    **/
    public void Var010() { testGetBinaryStream(rs37_, 1, ROW1_); }
    public void Var011() { testGetBinaryStream(rs37_, 2, ROW2_37_); }
    public void Var012() { testGetBinaryStream(rs37_, 3, ROW3_37_); }
    public void Var013() { testGetBinaryStream(rs37_, 4, ROW4_37_); }
    public void Var014() { testGetBinaryStream(rs37_, 5, ROW5_37_); }
    public void Var015() { testGetBinaryStream(rs37_, 6, ROW6_37_); }
    public void Var016() { testGetBinaryStream(rs37_, 7, ROW7_37_); }
    public void Var017() { testGetBinaryStream(rs37_, 8, ROW8_37_); }

    public void Var018() { testGetBinaryStream(rs1208_, 1, ROW1_); }
    public void Var019() { testGetBinaryStream(rs1208_, 2, ROW2_1208_); }
    public void Var020() { testGetBinaryStream(rs1208_, 3, ROW3_1208_); }
    public void Var021() { testGetBinaryStream(rs1208_, 4, ROW4_1208_); }
    public void Var022() { testGetBinaryStream(rs1208_, 5, ROW5_1208_); }
    public void Var023() { testGetBinaryStream(rs1208_, 6, ROW6_1208_); }
    public void Var024() { testGetBinaryStream(rs1208_, 7, ROW7_1208_); }
    public void Var025() { testGetBinaryStream(rs1208_, 8, ROW8_1208_); }
    public void Var026() { testGetBinaryStream(rs1208_, 9, ROW9_1208_); }

    public void Var027() { testGetBinaryStream(rs1200_, 1, ROW1_); }
    public void Var028() { testGetBinaryStream(rs1200_, 2, ROW2_1200_); }
    public void Var029() { testGetBinaryStream(rs1200_, 3, ROW3_1200_); }
    public void Var030() { testGetBinaryStream(rs1200_, 4, ROW4_1200_); }
    public void Var031() { testGetBinaryStream(rs1200_, 5, ROW5_1200_); }
    public void Var032() { testGetBinaryStream(rs1200_, 6, ROW6_1200_); }
    public void Var033() { testGetBinaryStream(rs1200_, 7, ROW7_1200_); }
    public void Var034() { testGetBinaryStream(rs1200_, 8, ROW8_1200_); }
    public void Var035() { testGetBinaryStream(rs1200_, 9, ROW9_1200_); }


    public void Var036() { testGetBinaryStream(rs1202_, 1, ROW1_); }
    public void Var037() { testGetBinaryStream(rs1202_, 2, ROW2_1202_); }
    public void Var038() { testGetBinaryStream(rs1202_, 3, ROW3_1202_); }
    public void Var039() { testGetBinaryStream(rs1202_, 4, ROW4_1202_); }
    public void Var040() { testGetBinaryStream(rs1202_, 5, ROW5_1202_); }
    public void Var041() { testGetBinaryStream(rs1202_, 6, ROW6_1202_); }
    public void Var042() { testGetBinaryStream(rs1202_, 7, ROW7_1202_); }
    public void Var043() { testGetBinaryStream(rs1202_, 8, ROW8_1202_); }
    public void Var044() { testGetBinaryStream(rs1202_, 9, ROW9_1202_); }




    public void testGetCharacterStream(ResultSet rs, String column, int row, String expected, String expectedException) {

	if (checkJdbc40 ()) {
	    try {
		message.setLength(0);  
		rs.absolute (row);
		Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
		Reader v = (Reader) JDReflectionUtil.callMethod_O(xml, "getCharacterStream");

		if (isXmlDeclarationStripped()) {
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
                  failed("Expected exception "+expectedException); 
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
    public void Var045() { testGetCharacterStream(rs37_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); }	
    public void Var046() { testGetCharacterStream(rs37_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var047() { testGetCharacterStream(rs37_, "C_BLOB", 3, ROW3_S37_, null); }
    public void Var048() { testGetCharacterStream(rs37_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var049() { testGetCharacterStream(rs37_, "C_BLOB", 5, ROW5_S37_, null); }
    public void Var050() { testGetCharacterStream(rs37_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var051() { testGetCharacterStream(rs37_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var052() { testGetCharacterStream(rs37_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }


    
    public void Var053() { testGetCharacterStream(rs1208_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); } 
    public void Var054() { testGetCharacterStream(rs1208_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var055() { testGetCharacterStream(rs1208_, "C_BLOB", 3, ROW3_S1208_, null); }
    public void Var056() { testGetCharacterStream(rs1208_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var057() { testGetCharacterStream(rs1208_, "C_BLOB", 5, ROW5_S1208_, null); }
    public void Var058() { testGetCharacterStream(rs1208_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var059() { testGetCharacterStream(rs1208_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var060() { testGetCharacterStream(rs1208_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }
    public void Var061() { testGetCharacterStream(rs1208_, "C_BLOB", 9, ROW9_S_, null); }

    public void Var062() { testGetCharacterStream(rs1200_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); } 
    public void Var063() { testGetCharacterStream(rs1200_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var064() { testGetCharacterStream(rs1200_, "C_BLOB", 3, ROW3_S1200_, null); }
    public void Var065() { testGetCharacterStream(rs1200_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var066() { testGetCharacterStream(rs1200_, "C_BLOB", 5, ROW5_S1200_, null); }
    public void Var067() { testGetCharacterStream(rs1200_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var068() { testGetCharacterStream(rs1200_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var069() { testGetCharacterStream(rs1200_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }
    public void Var070() { testGetCharacterStream(rs1200_, "C_BLOB", 9, ROW9_S_, null); }

    public void Var071() { testGetCharacterStream(rs1202_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); } 
    public void Var072() { testGetCharacterStream(rs1202_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var073() { testGetCharacterStream(rs1202_, "C_BLOB", 3, ROW3_S1202_, null); }
    public void Var074() { testGetCharacterStream(rs1202_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var075() { testGetCharacterStream(rs1202_, "C_BLOB", 5, ROW5_S1202_, null); }
    public void Var076() { testGetCharacterStream(rs1202_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var077() { testGetCharacterStream(rs1202_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var078() { testGetCharacterStream(rs1202_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }
    public void Var079() { testGetCharacterStream(rs1202_, "C_BLOB", 9, ROW9_S_, null); }


    
    public void testGetString(ResultSet rs, String column, int row, String expected, String expectedException) {

	if (checkJdbc40 ()) {
	    try {
		message.setLength(0);  
		rs.absolute (row);
		Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
		String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
		if (isXmlDeclarationStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected); 
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
    public void Var080() { testGetString(rs37_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); }	
    public void Var081() { testGetString(rs37_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var082() { testGetString(rs37_, "C_BLOB", 3, ROW3_S37_, null); }
    public void Var083() { testGetString(rs37_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var084() { testGetString(rs37_, "C_BLOB", 5, ROW5_S37_, null); }
    public void Var085() { testGetString(rs37_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var086() { testGetString(rs37_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var087() { testGetString(rs37_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }

    public void Var088() { testGetString(rs1208_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); }	
    public void Var089() { testGetString(rs1208_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var090() { testGetString(rs1208_, "C_BLOB", 3, ROW3_S1208_, null); }
    public void Var091() { testGetString(rs1208_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var092() { testGetString(rs1208_, "C_BLOB", 5, ROW5_S1208_, null); }
    public void Var093() { testGetString(rs1208_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var094() { testGetString(rs1208_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var095() { testGetString(rs1208_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }
    public void Var096() { testGetString(rs1208_, "C_BLOB", 9, ROW9_S_, null); }

    public void Var097() { testGetString(rs1200_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); }	
    public void Var098() { testGetString(rs1200_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var099() { testGetString(rs1200_, "C_BLOB", 3, ROW3_S1200_, null); }
    public void Var100() { testGetString(rs1200_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var101() { testGetString(rs1200_, "C_BLOB", 5, ROW5_S1200_, null); }
    public void Var102() { testGetString(rs1200_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var103() { testGetString(rs1200_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var104() { testGetString(rs1200_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }
    public void Var105() { testGetString(rs1200_, "C_BLOB", 9, ROW9_S_, null); }

    public void Var106() { testGetString(rs1202_, "C_BLOB", 1, ROW1_S_, "Data type mismatch"); }	
    public void Var107() { testGetString(rs1202_, "C_BLOB", 2, ROW2_S_, null); }
    public void Var108() { testGetString(rs1202_, "C_BLOB", 3, ROW3_S1202_, null); }
    public void Var109() { testGetString(rs1202_, "C_BLOB", 4, ROW4_S_, null); }
    public void Var110() { testGetString(rs1202_, "C_BLOB", 5, ROW5_S1202_, null); }
    public void Var111() { testGetString(rs1202_, "C_BLOB", 6, ROW6_S_, null); }
    public void Var112() { testGetString(rs1202_, "C_BLOB", 7, ROW7_S2_, null); }
    public void Var113() { testGetString(rs1202_, "C_BLOB", 8, ROW8_S_, "Data type mismatch"); }
    public void Var114() { testGetString(rs1202_, "C_BLOB", 9, ROW9_S_, null); }


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
        }
      }
    }
    return passed;
  }

   public void testGetSource(ResultSet rs, String column, int row, String sourceClassName , Object expected, Object expectedException) {
     message.setLength(0);  
	if (checkJdbc40 ()) {
	  Object source = null; 
	  try {
            String classname = sourceClassName; 
            Class<?> sourceClass = null; 
            if (classname != null) { 
              if (classname.indexOf("JAVAX") == 0) {
                classname = "javax"+sourceClassName.substring(5); 
              }
              sourceClass = Class.forName(classname);
            }
	    rs.absolute (row);
	    
	    Object xml = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", column);
                        
	    source =  JDReflectionUtil.callMethod_O(xml, "getSource", Class.forName("java.lang.Class"), sourceClass);

	    if (source == null) {
		throw new SQLException("getSource returned null source"); 
	    }
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
              message.append("SourceClassName is "+sourceClassName); 
	      message.append("Transformer is "+transformer+"\n");
	      message.append("Class loaded from "+getLoadPath(transformer)+"\n"); 
	      
	      transformer.setErrorListener(errorListener); 
	      transformer.transform((Source) source, new StreamResult("/tmp/JDXMLBlob.xml"));

	      //
	      // Peak to see if output data is UTF-16. 
	      //
	      BufferedReader br; 
	      FileInputStream fis = new FileInputStream("/tmp/JDXMLBlob.xml");
	      int b1=fis.read();
	      int b2=fis.read();
	      fis.close(); 
	      message.append("b1=0x"+Integer.toHexString(b1)+" b2=0x"+Integer.toHexString(b2)+"\n");
	      if ((b1 == 0xFE)  && (b2 == 0xFF)) {
          message.append("Using InputStreamReader(/tmp/JDXMLBlob.xml, UTF-16\n)"); 
	        br = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/JDXMLBlob.xml"),"UTF-16"));
	      } else if ((b1 == 0x3C )  && (b2 == 0x0)) {
          message.append("Using InputStreamReader(/tmp/JDXMLBlob.xml, UTF-16LE\n)"); 
          br = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/JDXMLBlob.xml"),"UTF-16LE"));
	      } else { 
	        message.append("Using FileReader(/tmp/JDXMLBlob.xml)\n"); 
	        br = new BufferedReader(new FileReader("/tmp/JDXMLBlob.xml"));
	      }
	      
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
                      passed = checkExpected(output, expected, message); 

                } else if (sourceClassName.equals("JAVAX.xml.transform.stax.StAXSource")) {

                  TransformerFactory factory = TransformerFactory.newInstance();
                  Transformer transformer = factory.newTransformer(); 
                  transformer.transform((Source) source, new StreamResult("/tmp/JDXMLBlob.xml")); 
                    BufferedReader br = new BufferedReader(new FileReader("/tmp/JDXMLBlob.xml"));
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
                String exceptionMessage = e.toString();
                boolean passed =false; 
                if (expectedException instanceof String ) { 
                  passed = exceptionMessage.toUpperCase().indexOf(((String) expectedException).toUpperCase()) >= 0; 
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

                
                
                if (!passed) e.printStackTrace(); 
              } else { 

		failed (e, "Unexpected Exception source="+source);
              }
	    }
	}

    } 
    /**
    getSource() - CCSID37 and lobs of various sizes and different sources 
    **/
    public void Var115() { testGetSource(rs37_, "C_BLOB", 4, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1208_PLUS_STANDALONE_, null); }
    public void Var116() {
	// In JDK 11 and beyond, the ccsid 37 is no longer handled in the binary data.
      int jdk = JVMInfo.getJDK(); 
	if (jdk >= JVMInfo.JDK_V11 ) { notApplicable("CCSID 37 no longer works in transformers for JDK = "+jdk);  return; }
	testGetSource(rs37_, "C_BLOB", 5, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1208_T_PLUS_STANDALONE_37_, null);
    }
    public void Var117() { testGetSource(rs37_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
    public void Var118() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs37_, "C_BLOB", 2, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW2_S1208_PLUS_STANDALONE_, null);
	} else {
	    testGetSource(rs37_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR");

	}
    }

    public void Var119() { testGetSource(rs37_, "C_BLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
    public void Var120() { testGetSource(rs37_, "C_BLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
    public void Var121() { testGetSource(rs37_, "C_BLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var122() { testGetSource(rs37_, "C_BLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var123() { testGetSource(rs37_, "C_BLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var124() { testGetSource(rs37_, "C_BLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
    public void Var125() { testGetSource(rs37_, "C_BLOB", 8, "javax.xml.transform.stax.StAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var126() { testGetSource(rs37_, "C_BLOB", 10, "javax.xml.transform.stax.StAXSource", ROW2_S_, INVALID_XML_CHARACTER_OR_CONTENT_NOT_ALLOWED); }

    public void Var127() { testGetSource(rs37_, "C_BLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
    public void Var128() { testGetSource(rs37_, "C_BLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
    public void Var129() { testGetSource(rs37_, "C_BLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "Data type mismatch"); }
    public void Var130() { testGetSource(rs37_, "C_BLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, "Content is not allowed in prolog"); }

    public static String EXPECTED_ROW4_S1200_PLUS_NL_[] = {
	ROW4_S1200_,  /* 169P 166P */
	"<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>",
	ROW4_S_NO_DECL_NS_,
    };


    public static String EXPECTED_ROW6_S1200_PLUS_NL_[] = {
	ROW6_S1200_,  /* 169P 166P */
	"<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>"
    }; 

    public void Var131() { testGetSource(rs37_, "C_BLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null); }


    public static String EXPECTED_ROW5_S1200_T_PLUS_NL_[] = {
	ROW5_S1200_T_,  /* 169P 166P */
	"<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>",
	ROW5_S_NO_DECL_NS_,
    }; 


    public static String EXPECTED_ROW7_S1200_T_PLUS_NL_[] = {
	ROW7_S1200_T_,  /* 169P 166P */
	"<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<TOP attrib=\"TOP\">TOP</TOP>"

    }; 

    public void Var132() { testGetSource(rs37_, "C_BLOB", 5, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1200_T_PLUS_NL_, null); }

    public void Var133() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs37_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_NO_DECL_, null);
	} else { 
	    testGetSource(rs37_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null);
	}
    }
    public void Var134() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs37_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S_NO_DECL_, null); 
	} else {
	    testGetSource(rs37_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S37_, null); 
	}
    }

    public void Var135() { testGetSource(rs37_, "C_BLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var136() { testGetSource(rs37_, "C_BLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

    public void Var137() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs37_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_NO_DECL_, null);
	} else {
	    testGetSource(rs37_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);
	}
    }
    public void Var138() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs37_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S_NO_DECL_, null);
	} else {
	    testGetSource(rs37_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S37_, null);
	} 
    }

    public void Var139() { testGetSource(rs37_, "C_BLOB", 4, null, ROW4_S1208_, null); }
    public void Var140() { testGetSource(rs37_, "C_BLOB", 5, null, ROW5_S1208_T_, null); }
    public void Var141() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs37_, "C_BLOB", 5, "java.lang.String", ROW5_S37_, "FeatureNotSupportedException");
	} else {
	    testGetSource(rs37_, "C_BLOB", 5, "java.lang.String", ROW5_S37_, "FeatureNotSupportedException"); 
	}
    }



    public void Var142() { testGetSource(rs1208_, "C_BLOB", 4, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1208_PLUS_STANDALONE_, null); }
    public void Var143() { testGetSource(rs1208_, "C_BLOB", 5, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1208_T_PLUS_STANDALONE_, null); }
    public void Var144() { testGetSource(rs1208_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
    public void Var145() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs1208_, "C_BLOB", 2, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW2_S1208_PLUS_STANDALONE_, null);
	} else {
	    testGetSource(rs1208_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR");
	}
    }

    public void Var146() { testGetSource(rs1208_, "C_BLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
    public void Var147() { testGetSource(rs1208_, "C_BLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
    public void Var148() { testGetSource(rs1208_, "C_BLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var149() { testGetSource(rs1208_, "C_BLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var150() { testGetSource(rs1208_, "C_BLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var151() { testGetSource(rs1208_, "C_BLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
    public void Var152() { testGetSource(rs1208_, "C_BLOB", 8, "javax.xml.transform.stax.StAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var153() { testGetSource(rs1208_, "C_BLOB", 10, "javax.xml.transform.stax.StAXSource", ROW2_S_, INVALID_XML_CHARACTER_OR_CONTENT_NOT_ALLOWED); }

    public void Var154() { testGetSource(rs1208_, "C_BLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
    public void Var155() { testGetSource(rs1208_, "C_BLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
    public void Var156() { testGetSource(rs1208_, "C_BLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "Data type mismatch"); }
    public void Var157() { testGetSource(rs1208_, "C_BLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var158() { testGetSource(rs1208_, "C_BLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null); }
    public void Var159() { testGetSource(rs1208_, "C_BLOB", 5, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1200_T_PLUS_NL_, null); }

    public void Var160() {
	if (isXmlDeclarationStripped()) { 
	    testGetSource(rs1208_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_NO_DECL_, null);
	} else {
	    testGetSource(rs1208_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null);
	}
    }
    public void Var161() {
	if (isXmlDeclarationStripped()) { 
	    testGetSource(rs1208_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S_NO_DECL_, null);
	} else {
	    testGetSource(rs1208_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S1208_, null);
	}
    }

    public void Var162() { testGetSource(rs1208_, "C_BLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var163() { testGetSource(rs1208_, "C_BLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

    public void Var164() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1208_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_NO_DECL_, null);
	} else {
	testGetSource(rs1208_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);

	}
    }
    public void Var165() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1208_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S_NO_DECL_, null);
	} else {
	testGetSource(rs1208_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S1208_, null);

	}
    }

    public void Var166() { testGetSource(rs1208_, "C_BLOB", 4, null, ROW4_S1208_, null); }
    public void Var167() { testGetSource(rs1208_, "C_BLOB", 5, null, ROW5_S1208_T_, null); }
    public void Var168() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs1208_, "C_BLOB", 5, "java.lang.String", ROW5_S1208_, "FeatureNotSupportedException"); 
	} else {
	    testGetSource(rs1208_, "C_BLOB", 5, "java.lang.String", ROW5_S1208_, "FeatureNotSupportedException");
	}
    }


    public void Var169() { testGetSource(rs1200_, "C_BLOB", 4, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1208_PLUS_STANDALONE_, null); }
    public void Var170() { testGetSource(rs1200_, "C_BLOB", 5, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1208_T_PLUS_STANDALONE1200_, null); }
    public void Var171() { testGetSource(rs1200_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
    public void Var172() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs1200_, "C_BLOB", 2, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW2_S1208_PLUS_STANDALONE_, null);
	} else {
	    testGetSource(rs1200_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); 
	}
    }


    public void Var173() { testGetSource(rs1200_, "C_BLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
    public void Var174() { testGetSource(rs1200_, "C_BLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
    public void Var175() { testGetSource(rs1200_, "C_BLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var176() { testGetSource(rs1200_, "C_BLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var177() { testGetSource(rs1200_, "C_BLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var178() { testGetSource(rs1200_, "C_BLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
    public void Var179() { testGetSource(rs1200_, "C_BLOB", 8, "javax.xml.transform.stax.StAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var180() { testGetSource(rs1200_, "C_BLOB", 10, "javax.xml.transform.stax.StAXSource", ROW2_S_, INVALID_XML_CHARACTER_OR_CONTENT_NOT_ALLOWED); }

    public void Var181() { testGetSource(rs1200_, "C_BLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
    public void Var182() { testGetSource(rs1200_, "C_BLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
    public void Var183() { testGetSource(rs1200_, "C_BLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "Data type mismatch"); }
    public void Var184() { testGetSource(rs1200_, "C_BLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var185() { testGetSource(rs1200_, "C_BLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null); }
    public void Var186() { testGetSource(rs1200_, "C_BLOB", 5, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1200_T_PLUS_NL_, null); }

    public void Var187() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1200_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_NO_DECL_, null);
	} else {
	testGetSource(rs1200_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null);

	}
    }
    public void Var188() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1200_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S_NO_DECL_, null);
	} else {
	testGetSource(rs1200_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S1200_, null);

	}
    }

    public void Var189() { testGetSource(rs1200_, "C_BLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var190() { testGetSource(rs1200_, "C_BLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

    public void Var191() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1200_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_NO_DECL_, null);
	} else {
	testGetSource(rs1200_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);

	}
    }
    public void Var192() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1200_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S_NO_DECL_, null);
	} else {
	testGetSource(rs1200_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S1200_, null);

	}
    }

    public void Var193() { testGetSource(rs1200_, "C_BLOB", 4, null, ROW4_S1208_, null); }
    public void Var194() { testGetSource(rs1200_, "C_BLOB", 5, null, ROW5_S1208_T_, null); }
    public void Var195() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs1200_, "C_BLOB", 5, "java.lang.String", ROW5_S1200_, "FeatureNotSupportedException");
	} else {
	    testGetSource(rs1200_, "C_BLOB", 5, "java.lang.String", ROW5_S1200_, "FeatureNotSupportedException"); 
	}
    }



    public void Var196() { testGetSource(rs1202_, "C_BLOB", 4, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1208_PLUS_STANDALONE_, null); }
    public void Var197() { testGetSource(rs1202_, "C_BLOB", 5, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1208_T_PLUS_STANDALONELE_, null); }
    public void Var198() { testGetSource(rs1202_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR"); }
    public void Var199() {
	if (isXmlDeclarationStripped()) {
	    testGetSource(rs1202_, "C_BLOB", 2, "javax.xml.transform.dom.DOMSource", EXPECTED_ROW2_S1208_PLUS_STANDALONE_, null);
	} else {
	    testGetSource(rs1202_, "C_BLOB", 8, "javax.xml.transform.dom.DOMSource", ROW8_S_, "XML PARSING ERROR");
	}
    }

    public void Var200() { testGetSource(rs1202_, "C_BLOB", 4, "javax.xml.transform.sax.SAXSource", ROW4_S1208_, null); }
    public void Var201() { testGetSource(rs1202_, "C_BLOB", 5, "javax.xml.transform.sax.SAXSource", ROW5_S1208_T_, null); }
    public void Var202() { testGetSource(rs1202_, "C_BLOB", 8, "javax.xml.transform.sax.SAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var203() { testGetSource(rs1202_, "C_BLOB", 10, "javax.xml.transform.sax.SAXSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var204() { testGetSource(rs1202_, "C_BLOB", 4, "javax.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var205() { testGetSource(rs1202_, "C_BLOB", 5, "javax.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }
    public void Var206() { testGetSource(rs1202_, "C_BLOB", 8, "javax.xml.transform.stax.StAXSource", ROW8_S_, "Data type mismatch"); }
    public void Var207() { testGetSource(rs1202_, "C_BLOB", 10, "javax.xml.transform.stax.StAXSource", ROW2_S_, INVALID_XML_CHARACTER_OR_CONTENT_NOT_ALLOWED); }

    public void Var208() { testGetSource(rs1202_, "C_BLOB", 4, "javax.xml.transform.stream.StreamSource", ROW4_S1208_, null); }
    public void Var209() { testGetSource(rs1202_, "C_BLOB", 5, "javax.xml.transform.stream.StreamSource", ROW5_S1208_T_, null); }
    public void Var210() { testGetSource(rs1202_, "C_BLOB", 8, "javax.xml.transform.stream.StreamSource", ROW8_S_, "Data type mismatch"); }
    public void Var211() { testGetSource(rs1202_, "C_BLOB", 10, "javax.xml.transform.stream.StreamSource", ROW2_S_, "Content is not allowed in prolog"); }

    public void Var212() { testGetSource(rs1202_, "C_BLOB", 4, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW4_S1200_PLUS_NL_, null); }
    public void Var213() { testGetSource(rs1202_, "C_BLOB", 5, "JAVAX.xml.transform.dom.DOMSource", EXPECTED_ROW5_S1200_T_PLUS_NL_, null); }

    public void Var214() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1202_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_NO_DECL_, null);
	} else {
	testGetSource(rs1202_, "C_BLOB", 4, "JAVAX.xml.transform.sax.SAXSource", ROW4_S_, null);

	}
    }
    public void Var215() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1202_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S_NO_DECL_, null);
	} else {
	testGetSource(rs1202_, "C_BLOB", 5, "JAVAX.xml.transform.sax.SAXSource", ROW5_S1202_, null);

	}
    }

    public void Var216() { testGetSource(rs1202_, "C_BLOB", 4, "JAVAX.xml.transform.stax.StAXSource", ROW4_S1208_, null); }
    public void Var217() { testGetSource(rs1202_, "C_BLOB", 5, "JAVAX.xml.transform.stax.StAXSource", ROW5_S1208_T_, null); }

    public void Var218() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1202_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_NO_DECL_, null);
	} else {
	testGetSource(rs1202_, "C_BLOB", 4, "JAVAX.xml.transform.stream.StreamSource", ROW4_S_, null);

	}
    }
    public void Var219() {
	if (isXmlDeclarationStripped()) {
	testGetSource(rs1202_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S_NO_DECL_, null);
	} else {
	testGetSource(rs1202_, "C_BLOB", 5, "JAVAX.xml.transform.stream.StreamSource", ROW5_S1202_, null);

	}
    }

    public void Var220() { testGetSource(rs1202_, "C_BLOB", 4, null, ROW4_S1208_, null); }
    public void Var221() { testGetSource(rs1202_, "C_BLOB", 5, null, ROW5_S1208_T_, null); }
    public void Var222() {
	if (isXmlDeclarationStripped()) { 
	    testGetSource(rs1202_, "C_BLOB", 5, "java.lang.String", ROW5_S1202_, "FeatureNotSupportedException");
	} else {
	    testGetSource(rs1202_, "C_BLOB", 5, "java.lang.String", ROW5_S1202_, "FeatureNotSupportedException");
	}

    }






    public void testSetCharacterStream(String table,
				       String column,
				       int row,
				       String writeString,
				       String expected,
				       String expectedException) {
	if (checkJdbc40()) {
	    message.setLength(0); 
	    try {
		// Clear the row first and validate that is was cleared
		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		rs2_.updateBytes(1,ROW1_);
                rs2_.updateRow();                                               
                rs2_.close();                                                   
		

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		byte[] bytes = rs2_.getBytes(column);
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
		if (isXmlDeclarationStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected); 
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
		assertCondition (bytes.length == 0 && passed, "bytes.length="+bytes.length+" sb 0 "+ message);
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
    setCharacterStream() - CCSID 37 and lobs of various sizes
    **/

    public void Var223() { testSetCharacterStream(TABLE37_, "C_BLOB", 21, ROW1_S_, ROW1_S_, "Data type mismatch" );
    }
    public void Var224() {
	if (isXmlDeclarationStripped()) {
	    testSetCharacterStream(TABLE37_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null);
	} else {
	    testSetCharacterStream(TABLE37_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null );
	}
    }
    public void Var225() { testSetCharacterStream(TABLE37_, "C_BLOB", 23, ROW3_S37_, ROW3_S37_, null ); }
    public void Var226() { testSetCharacterStream(TABLE37_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var227() { testSetCharacterStream(TABLE37_, "C_BLOB", 25, ROW5_S37_, ROW5_S37_, null ); }
    public void Var228() { testSetCharacterStream(TABLE37_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var229() { testSetCharacterStream(TABLE37_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var230() { testSetCharacterStream(TABLE37_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch"); }

    public void Var231() { testSetCharacterStream(TABLE1208_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var232() { testSetCharacterStream(TABLE1208_, "C_BLOB", 23, ROW3_S1208_, ROW3_S1208_, null ); }
    public void Var233() { testSetCharacterStream(TABLE1208_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var234() { testSetCharacterStream(TABLE1208_, "C_BLOB", 25, ROW5_S1208_, ROW5_S1208_, null ); }
    public void Var235() { testSetCharacterStream(TABLE1208_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var236() { testSetCharacterStream(TABLE1208_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var237() { testSetCharacterStream(TABLE1208_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch"); }
    public void Var238() { testSetCharacterStream(TABLE1208_, "C_BLOB", 29, ROW9_S_, ROW9_S_, null ); }

    public void Var239() { testSetCharacterStream(TABLE1200_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var240() { testSetCharacterStream(TABLE1200_, "C_BLOB", 23, ROW3_S1200_, ROW3_S1200_, null ); }
    public void Var241() { testSetCharacterStream(TABLE1200_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var242() { testSetCharacterStream(TABLE1200_, "C_BLOB", 25, ROW5_S1200_, ROW5_S1200_, null ); }
    public void Var243() { testSetCharacterStream(TABLE1200_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var244() { testSetCharacterStream(TABLE1200_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var245() { testSetCharacterStream(TABLE1200_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch"); }
    public void Var246() { testSetCharacterStream(TABLE1200_, "C_BLOB", 29, ROW9_S_, ROW9_S_, null ); }


    public void Var247() { testSetCharacterStream(TABLE1202_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var248() { testSetCharacterStream(TABLE1202_, "C_BLOB", 23, ROW3_S1202_, ROW3_S1202_, null ); }
    public void Var249() { testSetCharacterStream(TABLE1202_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var250() { testSetCharacterStream(TABLE1202_, "C_BLOB", 25, ROW5_S1202_, ROW5_S1202_, null ); }
    public void Var251() { testSetCharacterStream(TABLE1202_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var252() { testSetCharacterStream(TABLE1202_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var253() { testSetCharacterStream(TABLE1202_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch"); }
    public void Var254() { testSetCharacterStream(TABLE1202_, "C_BLOB", 29, ROW9_S_, ROW9_S_, null ); }


 

    public void testSetString(String table, String column, int row, String writeString, 
        String expected, String expectedException ) {
	if (checkJdbc40()) {
	    message.setLength(0); 
	    try {
		// Clear the row first and validate that is was cleared
		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		rs2_.updateBytes(1,ROW1_);
                rs2_.updateRow();                                               
                rs2_.close();                                                   
		

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		byte[] bytes = rs2_.getBytes(column);
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
		JDReflectionUtil.callMethod_V(rs2_, "updateSQLXML", column, sqlxml);                           
                rs2_.updateRow();                                               
                rs2_.close();

                rs2_ = statement2_.executeQuery("SELECT * FROM " + table);      
                rs2_.absolute(row);
		Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
		String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
		if (isXmlDeclarationStripped()) {
		    /* if xml is supported, the declaration is stripped when */
		    /* obtaining as a String */
		    expected = JDTestUtilities.stripXmlDeclaration(expected); 
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
                  assertCondition (bytes.length == 0 && passed, "bytes.length="+bytes.length+" sb 0 "+ message);
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

    public void Var255() { testSetString(TABLE37_, "C_BLOB", 21, ROW1_S_, ROW1_S_, "Data type mismatch" );      }
    public void Var256() { testSetString(TABLE37_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var257() { testSetString(TABLE37_, "C_BLOB", 23, ROW3_S37_, ROW3_S37_, null ); }
    public void Var258() { testSetString(TABLE37_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var259() { testSetString(TABLE37_, "C_BLOB", 25, ROW5_S37_, ROW5_S37_, null ); }
    public void Var260() { testSetString(TABLE37_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var261() { testSetString(TABLE37_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var262() { testSetString(TABLE37_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch" ); }



    public void Var263() { testSetString(TABLE1208_, "C_BLOB", 21, ROW1_S_, ROW1_S_, "Data type mismatch" );      }
    public void Var264() { testSetString(TABLE1208_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var265() { testSetString(TABLE1208_, "C_BLOB", 23, ROW3_S1208_, ROW3_S1208_, null ); }
    public void Var266() { testSetString(TABLE1208_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var267() { testSetString(TABLE1208_, "C_BLOB", 25, ROW5_S1208_, ROW5_S1208_, null ); }
    public void Var268() { testSetString(TABLE1208_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var269() { testSetString(TABLE1208_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var270() { testSetString(TABLE1208_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch" ); }
    public void Var271() { testSetString(TABLE1208_, "C_BLOB", 29, ROW9_S_, ROW9_S_, null ); }

    public void Var272() { testSetString(TABLE1200_, "C_BLOB", 21, ROW1_S_, ROW1_S_, "Data type mismatch" );      }
    public void Var273() { testSetString(TABLE1200_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var274() { testSetString(TABLE1200_, "C_BLOB", 23, ROW3_S1200_, ROW3_S1200_, null ); }
    public void Var275() { testSetString(TABLE1200_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var276() { testSetString(TABLE1200_, "C_BLOB", 25, ROW5_S1200_, ROW5_S1200_, null ); }
    public void Var277() { testSetString(TABLE1200_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var278() { testSetString(TABLE1200_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var279() { testSetString(TABLE1200_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch" ); }
    public void Var280() { testSetString(TABLE1200_, "C_BLOB", 29, ROW9_S_, ROW9_S_, null ); }

    public void Var281() { testSetString(TABLE1202_, "C_BLOB", 21, ROW1_S_, ROW1_S_, "Data type mismatch" );      }
    public void Var282() { testSetString(TABLE1202_, "C_BLOB", 22, ROW2_S_, ROW2_S_, null ); }
    public void Var283() { testSetString(TABLE1202_, "C_BLOB", 23, ROW3_S1202_, ROW3_S1202_, null ); }
    public void Var284() { testSetString(TABLE1202_, "C_BLOB", 24, ROW4_S_, ROW4_S_, null ); }
    public void Var285() { testSetString(TABLE1202_, "C_BLOB", 25, ROW5_S1202_, ROW5_S1202_, null ); }
    public void Var286() { testSetString(TABLE1202_, "C_BLOB", 26, ROW6_S_, ROW6_S_, null ); }
    public void Var287() { testSetString(TABLE1202_, "C_BLOB", 27, ROW7_S_, ROW7_S2_, null ); }
    public void Var288() { testSetString(TABLE1202_, "C_BLOB", 28, ROW8_S_, ROW8_S_, "Data type mismatch" ); }
    public void Var289() { testSetString(TABLE1202_, "C_BLOB", 29, ROW9_S_, ROW9_S_, null ); }



    public void testSetBinaryStream(String table, String column, int row, byte[] expected ) {
	if (checkJdbc40()) {
	    message.setLength(0); 
	    try {
		// Clear the row first and validate that is was cleared
		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		rs2_.updateBytes(1,ROW1_);
                rs2_.updateRow();                                               
                rs2_.close();                                                   
		

		rs2_=statement2_.executeQuery("SELECT * FROM " + table);        
		rs2_.absolute (row);
		byte[] bytes = rs2_.getBytes(column);
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
                Blob blob1 = rs2_.getBlob(column);                       
                InputStream inputStream = blob1.getBinaryStream();
		boolean passed = compare (inputStream, expected,message);
		if (!passed) {
		    Blob blob2 = rs2_.getBlob(column);
		    InputStream is = (InputStream) blob2.getBinaryStream();
		    byte[] buffer = new byte[10000]; 
		    int bytesRead = is.read(buffer);
		    byte[] output = new byte[bytesRead]; 
		    for (int i = 0; i < bytesRead; i++) {
			output[i] = buffer[i]; 
		    }
		    message.append("\nGot      : "+JDTestUtilities.dumpBytes(output));
		    message.append("\nExpected : "+JDTestUtilities.dumpBytes(expected)); 
		} 

		assertCondition (bytes.length == 0 && passed, "bytes.length="+bytes.length+" sb 0 "+ message); 
            }
            catch (Exception e) {                                               
                failed (e, "Unexpected Exception");                             
            }
        }
    }                                                                           

    /**
    setBinaryStream() - CCSID 37 and lobs of various sizes
    **/

    public void Var290() { testSetBinaryStream(TABLE37_, "C_BLOB", 21, ROW1_ );    }
    public void Var291() { testSetBinaryStream(TABLE37_, "C_BLOB", 22, ROW2_37_ ); }
    public void Var292() { testSetBinaryStream(TABLE37_, "C_BLOB", 23, ROW3_37_ ); }
    public void Var293() { testSetBinaryStream(TABLE37_, "C_BLOB", 24, ROW4_37_ ); }
    public void Var294() { testSetBinaryStream(TABLE37_, "C_BLOB", 25, ROW5_37_ ); }
    public void Var295() { testSetBinaryStream(TABLE37_, "C_BLOB", 26, ROW6_37_ ); }
    public void Var296() { testSetBinaryStream(TABLE37_, "C_BLOB", 27, ROW7_37_ ); }
    public void Var297() { testSetBinaryStream(TABLE37_, "C_BLOB", 28, ROW8_37_ ); }



    public void Var298() { testSetBinaryStream(TABLE1208_, "C_BLOB", 21, ROW1_ );    }
    public void Var299() { testSetBinaryStream(TABLE1208_, "C_BLOB", 22, ROW2_1208_ ); }
    public void Var300() { testSetBinaryStream(TABLE1208_, "C_BLOB", 23, ROW3_1208_ ); }
    public void Var301() { testSetBinaryStream(TABLE1208_, "C_BLOB", 24, ROW4_1208_ ); }
    public void Var302() { testSetBinaryStream(TABLE1208_, "C_BLOB", 25, ROW5_1208_ ); }
    public void Var303() { testSetBinaryStream(TABLE1208_, "C_BLOB", 26, ROW6_1208_ ); }
    public void Var304() { testSetBinaryStream(TABLE1208_, "C_BLOB", 27, ROW7_1208_ ); }
    public void Var305() { testSetBinaryStream(TABLE1208_, "C_BLOB", 28, ROW8_1208_ ); }
    public void Var306() { testSetBinaryStream(TABLE1208_, "C_BLOB", 29, ROW9_1208_ ); }


    public void Var307() { testSetBinaryStream(TABLE1200_, "C_BLOB", 21, ROW1_ );    }
    public void Var308() { testSetBinaryStream(TABLE1200_, "C_BLOB", 22, ROW2_1200_ ); }
    public void Var309() { testSetBinaryStream(TABLE1200_, "C_BLOB", 23, ROW3_1200_ ); }
    public void Var310() { testSetBinaryStream(TABLE1200_, "C_BLOB", 24, ROW4_1200_ ); }
    public void Var311() { testSetBinaryStream(TABLE1200_, "C_BLOB", 25, ROW5_1200_ ); }
    public void Var312() { testSetBinaryStream(TABLE1200_, "C_BLOB", 26, ROW6_1200_ ); }
    public void Var313() { testSetBinaryStream(TABLE1200_, "C_BLOB", 27, ROW7_1200_ ); }
    public void Var314() { testSetBinaryStream(TABLE1200_, "C_BLOB", 28, ROW8_1200_ ); }
    public void Var315() { testSetBinaryStream(TABLE1200_, "C_BLOB", 29, ROW9_1200_ ); }


    public void Var316() { testSetBinaryStream(TABLE1202_, "C_BLOB", 21, ROW1_ );    }
    public void Var317() { testSetBinaryStream(TABLE1202_, "C_BLOB", 22, ROW2_1202_ ); }
    public void Var318() { testSetBinaryStream(TABLE1202_, "C_BLOB", 23, ROW3_1202_ ); }
    public void Var319() { testSetBinaryStream(TABLE1202_, "C_BLOB", 24, ROW4_1202_ ); }
    public void Var320() { testSetBinaryStream(TABLE1202_, "C_BLOB", 25, ROW5_1202_ ); }
    public void Var321() { testSetBinaryStream(TABLE1202_, "C_BLOB", 26, ROW6_1202_ ); }
    public void Var322() { testSetBinaryStream(TABLE1202_, "C_BLOB", 27, ROW7_1202_ ); }
    public void Var323() { testSetBinaryStream(TABLE1202_, "C_BLOB", 28, ROW8_1202_ ); }
    public void Var324() { testSetBinaryStream(TABLE1202_, "C_BLOB", 29, ROW9_1202_ ); }







 


  public void testSetResult(String table, String column, int row,
      String resultClassName, String writeString, String sourceClassName,
      Object expected, String expectedException) {
    if (checkJdbc40()) {
      message.setLength(0);
      try {
        // Clear the row first and validate that is was cleared
        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_.absolute(row);
        rs2_.updateBytes(1, ROW1_);
        rs2_.updateRow();
        rs2_.close();

        rs2_ = statement2_.executeQuery("SELECT * FROM " + table);
        rs2_.absolute(row);
        byte[] bytes = rs2_.getBytes(column);
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
        rs2_.absolute(row);
        Object xml = JDReflectionUtil.callMethod_OS(rs2_, "getSQLXML", column);
        String s = (String) JDReflectionUtil.callMethod_O(xml, "getString");
        boolean passed = false;
        String[] expectedArray = null;
        if (expected instanceof String) {
          if (isXmlDeclarationStripped()) {
            /* if xml is supported, the declaration is stripped when */
            /* obtaining as a String */
            expected = JDTestUtilities.stripXmlDeclaration((String) expected);
          }

          passed = ((String) expected).equals(s);
        } else {
          expectedArray = (String[]) expected;
          for (int i = 0; i < expectedArray.length; i++) {
            String expected1 = expectedArray[i];
            if (isXmlDeclarationStripped()) {
              /* if xml is supported, the declaration is stripped when */
              /* obtaining as a String */
              expected1 = JDTestUtilities
                  .stripXmlDeclaration((String) expected1);
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

            message.append("\nExpected: " + (String) expected);
            message.append("\n        : "
                + JDTestUtilities.dumpBytes((String) expected));
          } else {
            if (expectedArray != null) {
              for (int i = 0; i < expectedArray.length; i++) {
                message.append("\nExpected: " + expectedArray[i]);
                message.append("\n        : "
                    + JDTestUtilities.dumpBytes(expectedArray[i]));

              }
            }
          }
        }
        if (expectedException != null) {
          failed("Expected exception " + expectedException);
        } else {
          assertCondition(bytes.length == 0 && passed, "bytes.length="
              + bytes.length + " sb 0 " + message);
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

    public void Var325() { testSetResult(TABLE37_, "C_BLOB", 24, DOMRESULT, ROW4_S_,   DOMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null ); }
    public void Var326() { testSetResult(TABLE37_, "C_BLOB", 25, DOMRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null ); }
    public void Var327() { testSetResult(TABLE37_, "C_BLOB", 26, DOMRESULT, ROW6_S_,   DOMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL_, null ); }
    public void Var328() { testSetResult(TABLE37_, "C_BLOB", 27, DOMRESULT, ROW7_S_,   DOMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null ); }

    public void Var329() { testSetResult(TABLE37_, "C_BLOB", 24, DOMRESULT, ROW4_S_,   SAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null ); }
    public void Var330() { testSetResult(TABLE37_, "C_BLOB", 25, DOMRESULT, ROW5_S_,   SAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null ); }
    public void Var331() { testSetResult(TABLE37_, "C_BLOB", 26, DOMRESULT, ROW6_S_,   SAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL_, null ); }
    public void Var332() { testSetResult(TABLE37_, "C_BLOB", 27, DOMRESULT, ROW7_S_,   SAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null ); }

    public void Var333() { testSetResult(TABLE37_, "C_BLOB", 24, DOMRESULT, ROW4_S_,   STAXSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null ); }
    public void Var334() { testSetResult(TABLE37_, "C_BLOB", 25, DOMRESULT, ROW5_S_,   STAXSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null ); }
    public void Var335() { testSetResult(TABLE37_, "C_BLOB", 26, DOMRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_S1200_PLUS_NL_, null ); }
    public void Var336() { testSetResult(TABLE37_, "C_BLOB", 27, DOMRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null ); }

    public void Var337() { testSetResult(TABLE37_, "C_BLOB", 24, DOMRESULT, ROW4_S_,   STREAMSOURCE, EXPECTED_ROW4_S1200_PLUS_NL_, null ); }
    public void Var338() { testSetResult(TABLE37_, "C_BLOB", 25, DOMRESULT, ROW5_S_,   STREAMSOURCE, EXPECTED_ROW5_S1200_T_PLUS_NL_, null ); }
    public void Var339() { testSetResult(TABLE37_, "C_BLOB", 26, DOMRESULT, ROW6_S_,   STREAMSOURCE, EXPECTED_ROW6_S1200_PLUS_NL_, null ); }
    public void Var340() { testSetResult(TABLE37_, "C_BLOB", 27, DOMRESULT, ROW7_S_,   STREAMSOURCE, EXPECTED_ROW7_S1200_T_PLUS_NL_, null ); }

    
    
    // SAX Results currently not supported
    public void Var341() { testSetResult(TABLE37_, "C_BLOB", 24, SAXRESULT, ROW4_S_, DOMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var342() { testSetResult(TABLE37_, "C_BLOB", 25, SAXRESULT, ROW5_S_, DOMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var343() { testSetResult(TABLE37_, "C_BLOB", 26, SAXRESULT, ROW6_S_, DOMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var344() { testSetResult(TABLE37_, "C_BLOB", 27, SAXRESULT, ROW7_S_, DOMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    
    public void Var345() { testSetResult(TABLE37_, "C_BLOB", 24, SAXRESULT, ROW4_S_,   SAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var346() { testSetResult(TABLE37_, "C_BLOB", 25, SAXRESULT, ROW5_S_, SAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var347() { testSetResult(TABLE37_, "C_BLOB", 26, SAXRESULT, ROW6_S_,   SAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var348() { testSetResult(TABLE37_, "C_BLOB", 27, SAXRESULT, ROW7_S_,   SAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var349() { testSetResult(TABLE37_, "C_BLOB", 24, SAXRESULT, ROW4_S_,   STAXSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var350() { testSetResult(TABLE37_, "C_BLOB", 25, SAXRESULT, ROW5_S_, STAXSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var351() { testSetResult(TABLE37_, "C_BLOB", 26, SAXRESULT, ROW6_S_,   STAXSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var352() { testSetResult(TABLE37_, "C_BLOB", 27, SAXRESULT, ROW7_S_,   STAXSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    public void Var353() { testSetResult(TABLE37_, "C_BLOB", 24, SAXRESULT, ROW4_S_,   STREAMSOURCE, ROW4_S_, "FeatureNotSupportedException"  ); }
    public void Var354() { testSetResult(TABLE37_, "C_BLOB", 25, SAXRESULT, ROW5_S_, STREAMSOURCE, ROW5_S_, "FeatureNotSupportedException"  ); }
    public void Var355() { testSetResult(TABLE37_, "C_BLOB", 26, SAXRESULT, ROW6_S_,   STREAMSOURCE, ROW6_S_, "FeatureNotSupportedException"  ); }
    public void Var356() { testSetResult(TABLE37_, "C_BLOB", 27, SAXRESULT, ROW7_S_,   STREAMSOURCE, ROW7_S2_, "FeatureNotSupportedException"  ); }

    
    String[] EXPECTED_ROW4_ST_PLUS_S1200_ =  {
	ROW4_S1208_,      /* 169P 166P */
	"<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>"     /* 16 */ 
    };
    
    public void Var357() { testSetResult(TABLE37_, "C_BLOB", 24, STAXRESULT, ROW4_S_,   DOMSOURCE,  EXPECTED_ROW4_ST_PLUS_S1200_, null ); }

    String[] EXPECTED_ROW5_ST_PLUS_S1200_T_ =  {
	ROW5_S1208_,      /* 169P 166P */
	"<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>",     /* 16 */
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TOP attrib=\"TOP\">TOP</TOP>"

    };

    public void Var358() { testSetResult(TABLE37_, "C_BLOB", 25, STAXRESULT, ROW5_S_,   DOMSOURCE, EXPECTED_ROW5_ST_PLUS_S1200_T_, null ); }

    String[] EXPECTED_ROW6_ST_PLUS_S1200_ =  {
	ROW6_S1208_,      /* 169P 166P */
	"<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>"     /* 16 */
    };

    public void Var359() { testSetResult(TABLE37_, "C_BLOB", 26, STAXRESULT, ROW6_S_,   DOMSOURCE, EXPECTED_ROW6_ST_PLUS_S1200_, null ); }

    String[] EXPECTED_ROW7_ST_PLUS_S1200_T_ =  {
	ROW7_S1208_T_,      /* 169P 166P */
	"<?xml version=\"1.0\"?><TOP attrib=\"TOP\">TOP</TOP>"     /* 16 */
    };

    public void Var360() { testSetResult(TABLE37_, "C_BLOB", 27, STAXRESULT, ROW7_S_,   DOMSOURCE, EXPECTED_ROW7_ST_PLUS_S1200_T_, null ); }

    public void Var361() { testSetResult(TABLE37_, "C_BLOB", 24, STAXRESULT, ROW4_S1208_,   SAXSOURCE,  EXPECTED_ROW4_ST_PLUS_S1200_, null ); } //saxToStax needs encoding???
    public void Var362() { testSetResult(TABLE37_, "C_BLOB", 25, STAXRESULT, ROW5_S_,   SAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1200_T_, null ); }
    public void Var363() { testSetResult(TABLE37_, "C_BLOB", 26, STAXRESULT, ROW6_S1208_,   SAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1200_, null ); } //saxToStax needs encoding???
    public void Var364() { testSetResult(TABLE37_, "C_BLOB", 27, STAXRESULT, ROW7_S1208_T_,   SAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1200_T_, null ); }//saxToStax needs encoding???

    public void Var365() { testSetResult(TABLE37_, "C_BLOB", 24, STAXRESULT, ROW4_S_,   STAXSOURCE,  EXPECTED_ROW4_ST_PLUS_S1200_, null ); }
    public void Var366() { testSetResult(TABLE37_, "C_BLOB", 25, STAXRESULT, ROW5_S_,   STAXSOURCE, EXPECTED_ROW5_ST_PLUS_S1200_T_, null ); }
    public void Var367() { testSetResult(TABLE37_, "C_BLOB", 26, STAXRESULT, ROW6_S_,   STAXSOURCE, EXPECTED_ROW6_ST_PLUS_S1200_, null ); }
    public void Var368() { testSetResult(TABLE37_, "C_BLOB", 27, STAXRESULT, ROW7_S_,   STAXSOURCE, EXPECTED_ROW7_ST_PLUS_S1200_T_, null ); }

    public void Var369() { testSetResult(TABLE37_, "C_BLOB", 24, STAXRESULT, ROW4_S1208_,   STREAMSOURCE,  EXPECTED_ROW4_ST_PLUS_S1200_, null ); }
    public void Var370() { testSetResult(TABLE37_, "C_BLOB", 25, STAXRESULT, ROW5_S_,   STREAMSOURCE, EXPECTED_ROW5_ST_PLUS_S1200_T_, null ); }
    public void Var371() { testSetResult(TABLE37_, "C_BLOB", 26, STAXRESULT, ROW6_S1208_,   STREAMSOURCE, EXPECTED_ROW6_ST_PLUS_S1200_, null ); }
    public void Var372() { testSetResult(TABLE37_, "C_BLOB", 27, STAXRESULT, ROW7_S1208_T_,   STREAMSOURCE, EXPECTED_ROW7_ST_PLUS_S1200_T_, null ); }



    public void Var373() { testSetResult(TABLE37_, "C_BLOB", 24, STREAMRESULT, ROW4_S_,   DOMSOURCE, EXPECTED_ROW4_S1208_PLUS_STANDALONE_, null ); }
    public void Var374() { testSetResult(TABLE37_, "C_BLOB", 25, STREAMRESULT, ROW5_S_, DOMSOURCE, EXPECTED_ROW5_S1208_T_PLUS_STANDALONE_, null ); }

    public static String EXPECTED_ROW6_S1208_PLUS_STANDALONE_[] = {
	ROW6_S1208_,
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>"
    }; 

    public void Var375() { testSetResult(TABLE37_, "C_BLOB", 26, STREAMRESULT, ROW6_S_,   DOMSOURCE, EXPECTED_ROW6_S1208_PLUS_STANDALONE_, null ); }


    public static String EXPECTED_ROW7_S1208_T_PLUS_STANDALONE_[] = {
	ROW7_S1208_T_,
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TOP attrib=\"TOP\">TOP</TOP>"
    }; 

    public void Var376() { testSetResult(TABLE37_, "C_BLOB", 27, STREAMRESULT, ROW7_S_,   DOMSOURCE, EXPECTED_ROW7_S1208_T_PLUS_STANDALONE_, null ); }

    public void Var377() { testSetResult(TABLE37_, "C_BLOB", 24, STREAMRESULT, ROW4_S1208_,   SAXSOURCE, ROW4_S1208_, null ); }
    public void Var378() { testSetResult(TABLE37_, "C_BLOB", 25, STREAMRESULT, ROW5_S_,   SAXSOURCE, ROW5_S1208_T_, null ); }
    public void Var379() { testSetResult(TABLE37_, "C_BLOB", 26, STREAMRESULT, ROW6_S1208_,   SAXSOURCE, ROW6_S1208_, null ); }
    public void Var380() { testSetResult(TABLE37_, "C_BLOB", 27, STREAMRESULT, ROW7_S1208_T_,   SAXSOURCE, ROW7_S1208_T_, null ); }

    public void Var381() { testSetResult(TABLE37_, "C_BLOB", 24, STREAMRESULT, ROW4_S_,   STAXSOURCE, ROW4_S1208_, null ); }
    public void Var382() { testSetResult(TABLE37_, "C_BLOB", 25, STREAMRESULT, ROW5_S_,   STAXSOURCE, ROW5_S1208_T_, null ); }
    public void Var383() { testSetResult(TABLE37_, "C_BLOB", 26, STREAMRESULT, ROW6_S_,   STAXSOURCE, ROW6_S1208_, null ); }
    public void Var384() { testSetResult(TABLE37_, "C_BLOB", 27, STREAMRESULT, ROW7_S_,   STAXSOURCE, ROW7_S1208_T_, null ); }

    public void Var385() { testSetResult(TABLE37_, "C_BLOB", 24, STREAMRESULT, ROW4_S1208_,   STREAMSOURCE, ROW4_S1208_, null ); }
    public void Var386() { testSetResult(TABLE37_, "C_BLOB", 25, STREAMRESULT, ROW5_S_,   STREAMSOURCE, ROW5_S1208_T_, null ); }
    public void Var387() { testSetResult(TABLE37_, "C_BLOB", 26, STREAMRESULT, ROW6_S1208_,   STREAMSOURCE, ROW6_S1208_, null ); }
    public void Var388() { testSetResult(TABLE37_, "C_BLOB", 27, STREAMRESULT, ROW7_S1208_T_,   STREAMSOURCE, ROW7_S1208_T_, null ); }

    
    
    
}
