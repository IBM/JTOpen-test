///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLUtil.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDMDPerformance.java
//
// Classes:      JDDMDPerformance
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////
package test;

import java.io.StringBufferInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*; 

public class JDXMLUtil {

  static DocumentBuilderFactory documentBuilderFactory = null; 
  static DocumentBuilder documentBuilder = null; 
  public static Document createDocument(String[] arg) throws Exception {
    StringBuffer sb = new StringBuffer(); 
    for (int i = 0; i < arg.length; i++) {
      sb.append(arg[i]); 
      sb.append("\n"); 
    }
    return createDocument(sb.toString()); 
  }
  
  public static Document createDocument(String arg) throws Exception { 
    Document doc = null ;
   
    if (documentBuilderFactory == null) {
      documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
    }
    if (documentBuilder == null) { 
     documentBuilder =  documentBuilderFactory.newDocumentBuilder(); 
    }
   
    StringBufferInputStream is = new StringBufferInputStream(arg); 
    doc = documentBuilder.parse(is); 
    
    return doc; 
  }

  public static String documentToString(Object document) throws Exception {

      /* 
      Object implementation      = JDReflectionUtil.callMethod_O(document, "getImplementation");
      Object domImplementationLS = JDReflectionUtil.callMethod_OSS(implementation, "getFeature", "LS", "3.0");
      Object lsSerializer        = JDReflectionUtil.callMethod_O(domImplementationLS, "createLSSerializer");
      String outString           = (String) JDReflectionUtil.callMethod_O(lsSerializer,"writeToString",Class.forName("org.w3c.dom.Node"),document); 
      return outString; 
      */ 
      
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer trans = tf.newTransformer();
      StringWriter sw = new StringWriter();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.transform(new DOMSource((Node) document), new StreamResult(sw));
      return sw.toString();
      
      
      
  } 
  /**
   * @param args
   */
  public static void main(String[] args) {
    try { 
    System.out.println("UNIT TESTING THE test.JDXMLUtil class ");
    String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
    "<catalog title=\"XML Zone\" publisher='IBM developerWorks'> "+
 "<journal date=\"Jan 2006\">"+ 
 " <article>"+
 "  <title>Managing XML data: Tag URIs</title>"+
 "  <author>Elliotte Harold</author>"+
 " </article>"+
 " <article> "+
 "  <title>Practical data binding: XPath as data binding tool, Part 2</title>"+
 "  <author>Brett McLaughlin</author>"+
 " </article>"+
 "</journal>"+
 "</catalog>"; 

    
    String xml2 = "<?xml version=\"1.0\"?>\n\n"+
"<!DOCTYPE note [\n"+
"  <!ELEMENT note    (to,from,heading,body)>\n"+
 " <!ELEMENT to      (#PCDATA)>\n"+
"  <!ELEMENT from    (#PCDATA)>\n"+
"  <!ELEMENT heading (#PCDATA)>\n"+
"  <!ELEMENT body    (#PCDATA)>\n"+
"]>\n"+
"<note>\n"+
"<to>Tove</to>\n"+
"<from>Jani</from>\n"+
"<heading>Reminder</heading>\n"+
"<body>Don't forget me this weekend!</body>\n"+
"</note> ";
    
    Document testDoc = createDocument(xml2);

    DocumentType docType = testDoc.getDoctype();
    System.out.println("DocumentType = "+docType);

    // testDoc.normalize();
   //     String content = testDoc.getXmlEncoding();
  //  System.out.println("testDoc is "+testDoc+" content="+content); 
    
    } catch (Exception e) { 
      e.printStackTrace(); 
    }
  }

}
