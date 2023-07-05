///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PCTransformTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

/**
 Testcase PCTransformTestcase.
 **/
public class PCTransformTestcase extends Testcase
{
    static boolean debug = false; 
    
    static { 
       String debugSet = System.getProperty("debug");
       if (debugSet != null) debug = true;
    }
  
    
    int jdk_; 
    
    
    public static boolean compareStrings(String a, String b, StringBuffer sb) {
    if (a == null) {
      if (b == null) {
        return true;
      } else {
        return false;
      }
    } else {
      boolean answer = a.equals(b);
      if (answer) { 
        sb.append("Strings are the same\n"); 
      } else  {
        if (b != null) {
          int length = a.length();
          if (b.length() < length)
            length = b.length();
          int mismatchCount = 0;
          for (int i = 0; mismatchCount < 2 && i < length; i++) {
            if (a.charAt(i) != b.charAt(i)) {
              sb.append("Mismatch at offset " + i + " \n");
              int beginIndex = i - 10;
              if (beginIndex < 0)
                beginIndex = 0;
              sb.append(a.substring(beginIndex, i));
              sb.append(">diff> 0x" + Integer.toHexString(0xFF & a.charAt(i))
                  + "'" + a.charAt(i) + "'\n");
              sb.append(a.substring(beginIndex, i));
              sb.append(">diff> 0x" + Integer.toHexString(0xFF & b.charAt(i))
                  + "'" + b.charAt(i) + "'\n");
              mismatchCount++;
            }

          }

        }
      }
      return answer;
    }
  }

    public static String showOutputString(String s) {
      int len = s.length(); 
      StringBuffer sb = new StringBuffer(); 
      sb.append("String expected = \""); 
      for (int i = 0; i < len; i++) { 
        char c = s.charAt(i); 
        switch (c) {
          case '"':   sb.append("\\\""); break; 
          case '\r':  break; 
          case '\n':  sb.append("\\n\"+\n\""); break;
          default:    sb.append(c); 
              
        }
      }
      sb.append("\";\n"); 
      
      return sb.toString(); 
      
    }

    public static String strip0d(String s) {
      if (s.indexOf('\r') < 0) return s; 
      
      int len = s.length(); 
      StringBuffer sb = new StringBuffer(); 
      for (int i = 0; i < len; i++) { 
        char c = s.charAt(i); 
        switch (c) {
          case '\r':  break; 
          default:    sb.append(c); 
        }
      }
      return sb.toString(); 
    }
    
    
    File xpcmlSchemaFile = null; 

    
    protected void setup() throws Exception {
       super.setup();

       jdk_ = JVMInfo.getJDK();

       System.out.println("jdk_ is "+jdk_+ " from "+JVMInfo.getJavaVersionString()); 
       // Extract the XPCML XSD specification file (from the Toolbox jar) to the local directory, so that the XML parser can find it.
       xpcmlSchemaFile = new File("xpcml.xsd");
       PCMiscTestcase.extractFile ("com/ibm/as400/data/xpcml.xsd", xpcmlSchemaFile);

    }
    
    
    protected void cleanup() throws Exception {
      super.cleanup();
      if (xpcmlSchemaFile != null) { 
        try { 
            xpcmlSchemaFile.delete(); 
            } catch (Throwable t) { 
              t.printStackTrace(); 
              }
      }
    }
   
    
    void checkDocuments(String pcmlString, String xpcmlString, StringBuffer sb, boolean checkPcml, boolean checkXpcml) throws Exception {
      if (checkPcml) {
      sb.append("\nMaking ProgramCallDocument from pcml\n");
      InputStream pcmlStream = new StringBufferInputStream(pcmlString);
      ProgramCallDocument pcd = new ProgramCallDocument(systemObject_,
                                                        "prog1",
                                                        pcmlStream, 
                                                        null,
                                                        null,
                                                        ProgramCallDocument.SOURCE_PCML); 
      sb.append("\nCreated pcd is "+pcd+"\n"); 
      }  else {
          System.out.println("Warning: did not check pcml"); 
      }
      if (checkXpcml) { 
      sb.append("\nMaking ProgramCallDocument from xpcml\n");
      InputStream xpcmlStream2 = new StringBufferInputStream(xpcmlString);
      ProgramCallDocument pcd2 = new ProgramCallDocument(systemObject_,
                                                                "prog1",
                                                                xpcmlStream2, 
                                                                null,
                                                                null,
                                                                ProgramCallDocument.SOURCE_XPCML); 
      sb.append("\nCreated pcd2 is "+pcd2+"\n");
      }  else {
        System.out.println("Warning: did not check xpcml"); 
    }

    }
    

    public void checkTransform(String pcmlString, String expectedXpcmlString) {
      checkTransform(pcmlString, expectedXpcmlString, true, true); 
    }
    
    public void checkTransform(String pcmlString, String expectedXpcmlString, boolean checkPcml, boolean checkXpcml) {


	if (jdk_ < JVMInfo.JDK_15) {
	    notApplicable("JDK 1.5 or later testcase");
	    return; 
	} 
      StringBuffer sb = new StringBuffer();
      String xpcmlString = "NOT SET"; 
      try
      {

          InputStream pcmlStream = new StringBufferInputStream(pcmlString);
          if (debug) System.out.println("-----\ninput\n-----\n"+pcmlString);
          
          OutputStream xpcmlStream = new ByteArrayOutputStream();

          ProgramCallDocument.transformPCMLToXPCML(pcmlStream, xpcmlStream);

          xpcmlString = strip0d(xpcmlStream.toString());
          if (debug) System.out.println("-----\noutput\n-----\n"+xpcmlString);
          
          boolean stringCompare = compareStrings(expectedXpcmlString, xpcmlString, sb); 
          // Make sure they are usable, i.e. no exception is thrown
          checkDocuments(pcmlString,xpcmlString, sb, checkPcml, checkXpcml ); 
          
          assertCondition(stringCompare, "Error.  Did not equal\n"+
                          "expected\n"+expectedXpcmlString+"\nGot\n"+xpcmlString+"\nOutput:"+sb+"\n"+showOutputString(xpcmlString));

      }
      catch (Exception e)
      {
          failed(e, 
                "Unexpected exception "+sb.toString()+
                "\npcmlString=\n"+pcmlString+
                "\nxpcmlstring=\n"+xpcmlString);
      }
      
    }
    
    /**
     Test ProgramCallDocument.transformPCMLtoXPCML()
     **/
    public void Var001()
    {
      String pcmlString =
        "<pcml version=\"4.0\">\n"+
        "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
        "<data name=\"packed1\" type=\"packed\" length=\"4\" usage=\"input\" init=\"0\"/>\n"+
        "<data  name=\"float1\" type=\"float\" length=\"4\" init=\"0\" />\n"+
        "<data  name=\"zoned1\" type=\"zoned\" length=\"8\" precision=\"2\" init=\"0\" />\n"+
        "</program>\n"+
        "</pcml>\n"; 

      String expectedXpcmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
        "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
        "\n"+
        "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
        "<parameterList> \n"+
        "<packedDecimalParm name=\"packed1\" passDirection=\"in\" totalDigits=\"4\">0</packedDecimalParm>\n"+
        "<floatParm name=\"float1\">0</floatParm>\n"+
        "<zonedDecimalParm name=\"zoned1\" totalDigits=\"8\" fractionDigits=\"2\">0</zonedDecimalParm>\n"+
        "</parameterList> \n"+
        "</program>\n"+
        "</xpcml>";
      
      checkTransform(pcmlString, expectedXpcmlString); 

    }

    
    
    /**
    Test ProgramCallDocument.transformPCMLtoXPCML() no default values
    Note:  This currently does not work, so I don't think anyone is expecting this to work. 
    **/
   public void Var002()
   {
     String pcmlString =
       "<pcml version=\"4.0\">\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<data name=\"packed1\" type=\"packed\" length=\"4\" usage=\"input\" />\n"+
       "<data  name=\"float1\" type=\"float\" length=\"4\"  />\n"+
       "<data  name=\"zoned1\" type=\"zoned\" length=\"8\" precision=\"2\" />\n"+
       "</program>\n"+
       "</pcml>\n"; 

     String expectedXpcmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
       "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
       "\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<parameterList> \n"+
       "<packedDecimalParm name=\"packed1\" passDirection=\"in\" totalDigits=\"4\"/>\n"+
       "<floatParm name=\"float1\"/>\n"+
       "<zonedDecimalParm name=\"zoned1\" totalDigits=\"8\" fractionDigits=\"2\"/>\n"+
       "</parameterList> \n"+
       "</program>\n"+
       "</xpcml>";
     
     
     checkTransform(pcmlString, expectedXpcmlString, true, false); 

   }
   

    
    
    /**
    Test ProgramCallDocument.transformPCMLtoXPCML() date parameter
    **/
   public void Var003()
   {
           String pcmlString =
             "<pcml version=\"6.0\">\n"+
             "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
             "<data name=\"date1\" type=\"date\" usage=\"input\"  init=\"2010-12-15\" />\n"+
             "</program>\n"+
             "</pcml>\n"; 

           String expectedXpcmlString =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
           "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
           "\n"+
           "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
           "<parameterList> \n"+
           "<dateParm name=\"date1\" passDirection=\"in\">2010-12-15</dateParm>\n"+
           "</parameterList> \n"+
           "</program>\n"+
           "</xpcml>";

           checkTransform(pcmlString, expectedXpcmlString); 

   }

   /**
   Test ProgramCallDocument.transformPCMLtoXPCML() date parameter with dateFormat
   **/
  public void Var004()
  {


          String pcmlString =
            "<pcml version=\"6.0\">\n"+
            "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
            "<data name=\"date1\" type=\"date\" dateformat=\"ISO\" usage=\"input\"  init=\"2010-12-15\" />\n"+
            "</program>\n"+
            "</pcml>\n"; 

          String expectedXpcmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
          "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
          "\n"+
          "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
          "<parameterList> \n"+
          "<dateParm name=\"date1\" passDirection=\"in\" dateFormat=\"ISO\">2010-12-15</dateParm>\n"+
          "</parameterList> \n"+
          "</program>\n"+
          "</xpcml>";

          
          checkTransform(pcmlString, expectedXpcmlString); 

  }

  /**
  Test ProgramCallDocument.transformPCMLtoXPCML() date parameter with dateseparator
  **/
 public void Var005()
 {


         String pcmlString =
           "<pcml version=\"6.0\">\n"+
           "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
           "<data name=\"date1\" type=\"date\" dateseparator=\"slash\" usage=\"input\"  init=\"2010-12-15\" />\n"+
           "</program>\n"+
           "</pcml>\n"; 

         String expectedXpcmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
         "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
         "\n"+
         "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
         "<parameterList> \n"+
         "<dateParm name=\"date1\" passDirection=\"in\" dateSeparator=\"slash\">2010-12-15</dateParm>\n"+
         "</parameterList> \n"+
         "</program>\n"+
         "</xpcml>";

         checkTransform(pcmlString, expectedXpcmlString); 
         
 }
 
 /**
 Test ProgramCallDocument.transformPCMLtoXPCML() date parameter with dateformat, dateseparator, defaultvalue
 **/
public void Var006()
{

        String pcmlString =
          "<pcml version=\"6.0\">\n"+
          "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
          "<data name=\"date1\" type=\"date\" usage=\"input\" dateformat=\"USA\" dateseparator=\"slash\" init=\"2010-12-15\"/>\n" +
          "</program>\n"+
          "</pcml>\n"; 

        String expectedXpcmlString =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
        "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
        "\n"+
        "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
        "<parameterList> \n"+
        "<dateParm name=\"date1\" passDirection=\"in\" dateFormat=\"USA\" dateSeparator=\"slash\">2010-12-15</dateParm>\n"+
        "</parameterList> \n"+
        "</program>\n"+
        "</xpcml>";
        
        checkTransform(pcmlString, expectedXpcmlString); 
}



/**
Test ProgramCallDocument.transformPCMLtoXPCML() time parameter
**/
public void Var007()
{
       String pcmlString =
         "<pcml version=\"6.0\">\n"+
         "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
         "<data name=\"time1\" type=\"time\" usage=\"input\"  init=\"13:14:15\" />\n"+
         "</program>\n"+
         "</pcml>\n"; 

       String expectedXpcmlString =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
       "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
       "\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<parameterList> \n"+
       "<timeParm name=\"time1\" passDirection=\"in\">13:14:15</timeParm>\n"+
       "</parameterList> \n"+
       "</program>\n"+
       "</xpcml>";

       checkTransform(pcmlString, expectedXpcmlString); 

}

/**
Test ProgramCallDocument.transformPCMLtoXPCML() time parameter with timeFormat
**/
public void Var008()
{


      String pcmlString =
        "<pcml version=\"6.0\">\n"+
        "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
        "<data name=\"time1\" type=\"time\" timeformat=\"ISO\" usage=\"input\"  init=\"13:14:15\" />\n"+
        "</program>\n"+
        "</pcml>\n"; 

      String expectedXpcmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
      "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
      "\n"+
      "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
      "<parameterList> \n"+
      "<timeParm name=\"time1\" passDirection=\"in\" timeFormat=\"ISO\">13:14:15</timeParm>\n"+
      "</parameterList> \n"+
      "</program>\n"+
      "</xpcml>";

      
      checkTransform(pcmlString, expectedXpcmlString); 

}

/**
Test ProgramCallDocument.transformPCMLtoXPCML() time parameter with timeseparator
**/
public void Var009()
{


     String pcmlString =
       "<pcml version=\"6.0\">\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<data name=\"time1\" type=\"time\" timeseparator=\"colon\" usage=\"input\"  init=\"13:14:15\" />\n"+
       "</program>\n"+
       "</pcml>\n"; 

     String expectedXpcmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
     "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
     "\n"+
     "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
     "<parameterList> \n"+
     "<timeParm name=\"time1\" passDirection=\"in\" timeSeparator=\"colon\">13:14:15</timeParm>\n"+
     "</parameterList> \n"+
     "</program>\n"+
     "</xpcml>";

     checkTransform(pcmlString, expectedXpcmlString); 
     
}

/**
Test ProgramCallDocument.transformPCMLtoXPCML() time parameter with timeformat, timeseparator, defaultvalue
**/
public void Var010()
{

    String pcmlString =
      "<pcml version=\"6.0\">\n"+
      "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
      "<data name=\"time1\" type=\"time\" usage=\"input\" timeformat=\"ISO\" timeseparator=\"colon\" init=\"13:14:15\"/>\n" +
      "</program>\n"+
      "</pcml>\n"; 

    String expectedXpcmlString =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
    "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
    "\n"+
    "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
    "<parameterList> \n"+
    "<timeParm name=\"time1\" passDirection=\"in\" timeFormat=\"ISO\" timeSeparator=\"colon\">13:14:15</timeParm>\n"+
    "</parameterList> \n"+
    "</program>\n"+
    "</xpcml>";
    
    checkTransform(pcmlString, expectedXpcmlString); 
}


/**
Test ProgramCallDocument.transformPCMLtoXPCML() timestamp parameter
**/
public void Var011()
{
       String pcmlString =
         "<pcml version=\"6.0\">\n"+
         "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
         "<data name=\"timestamp1\" type=\"timestamp\" usage=\"input\"  init=\"2010-01-01T23:59:59.999999999\" />\n"+
         "</program>\n"+
         "</pcml>\n"; 

       String expectedXpcmlString =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
       "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
       "\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<parameterList> \n"+
       "<timestampParm name=\"timestamp1\" passDirection=\"in\">2010-01-01T23:59:59.999999999</timestampParm>\n"+
       "</parameterList> \n"+
       "</program>\n"+
       "</xpcml>";
       
       checkTransform(pcmlString, expectedXpcmlString); 

}



/**
Test ProgramCallDocument.transformPCMLtoXPCML() date/time/timestamp parameter
**/
public void Var012()
{
       String pcmlString =
         "<pcml version=\"6.0\">\n"+
         "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
         "<data name=\"date1\" type=\"date\" usage=\"input\" dateformat=\"USA\" dateseparator=\"slash\" init=\"2010-12-15\"/>\n"+
         "<data name=\"time1\" type=\"time\" usage=\"input\" timeformat=\"HMS\" timeseparator=\"colon\" init=\"13:14:15\"/>\n"+
         "<data name=\"timestamp1\" type=\"timestamp\" usage=\"input\" init=\"2010-01-01T23:59:59.999999999\"/>\n"+
         "</program>\n"+
         "</pcml>\n"; 

       String expectedXpcmlString =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
       "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
       "\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<parameterList> \n"+
       "<dateParm name=\"date1\" passDirection=\"in\" dateFormat=\"USA\" dateSeparator=\"slash\">2010-12-15</dateParm>\n"+
       "<timeParm name=\"time1\" passDirection=\"in\" timeFormat=\"HMS\" timeSeparator=\"colon\">13:14:15</timeParm>\n"+
       "<timestampParm name=\"timestamp1\" passDirection=\"in\">2010-01-01T23:59:59.999999999</timestampParm>\n"+
       "</parameterList> \n"+
       "</program>\n"+
       "</xpcml>";
       
       checkTransform(pcmlString, expectedXpcmlString); 
}


/**
Test ProgramCallDocument.transformPCMLtoXPCML() date/time/timestamp array parameter
**/
public void Var013()
{
       String pcmlString =
         "<pcml version=\"6.0\">\n"+
         "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
         "<data name=\"date1\" type=\"date\" count=\"10\" usage=\"input\" dateformat=\"USA\" dateseparator=\"slash\" init=\"2010-12-15\"/>\n"+
         "<data name=\"time1\" type=\"time\" count=\"10\" usage=\"input\" timeformat=\"HMS\" timeseparator=\"colon\" init=\"13:14:15\"/>\n"+
         "<data name=\"timestamp1\" type=\"timestamp\"  count=\"10\" usage=\"input\" init=\"2010-01-01T23:59:59.999999999\"/>\n"+
         "</program>\n"+
         "</pcml>\n"; 

       String expectedXpcmlString =    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
       "<xpcml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xpcml.xsd\" version=\"6.0\">\n"+
       "\n"+
       "<program name=\"prog1\" path=\"/QGPL.LIB/PROG1.PGM\"> \n"+
       "<parameterList> \n"+
       "<arrayOfDateParm name=\"date1\" count=\"10\" passDirection=\"in\" dateFormat=\"USA\" dateSeparator=\"slash\"><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i><i>2010-12-15</i></arrayOfDateParm>\n"+
       "<arrayOfTimeParm name=\"time1\" count=\"10\" passDirection=\"in\" timeFormat=\"HMS\" timeSeparator=\"colon\"><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i><i>13:14:15</i></arrayOfTimeParm>\n"+
       "<arrayOfTimestampParm name=\"timestamp1\" count=\"10\" passDirection=\"in\"><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i><i>2010-01-01T23:59:59.999999999</i></arrayOfTimestampParm>\n"+
       "</parameterList> \n"+
       "</program>\n"+
       "</xpcml>";
       
       
       checkTransform(pcmlString, expectedXpcmlString); 
}


   
   
}
