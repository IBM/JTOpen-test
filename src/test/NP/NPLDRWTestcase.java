///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPLDRWTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.LineDataRecordWriter;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintParameterList;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SpooledFileOutputStream;
import com.ibm.as400.access.ZonedDecimalFieldDescription;

import test.Testcase;

/**
 Testcase NPLDRWTestcase.
 **/
public class NPLDRWTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPLDRWTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // the printer device name
    String printer_ = null;

    private static int ccsid_           = -1;       // local ccsid variable
    private static AS400 system_        = null;     // the AS/400 system

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPLDRWTestcase(AS400            systemObject,
                          Vector<String> variationsToRun,
                          int              runMode,
                          FileOutputStream fileOutputStream,
                          
                          String           printer)
      throws IOException
    {
        // $$$ TO DO $$$
        // Replace the third parameter with the total number of variations
        // in this testcase.
        super(systemObject, "NPLDRWTestcase", 15,
              variationsToRun, runMode, fileOutputStream);
              
        system_ = systemObject;

    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPLDRWTestcase");
   	    boolean allVariations = (variationsToRun_.size() == 0);
        
        try
        {
            // run variation 1
            if ((allVariations || variationsToRun_.contains("1")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(1);
                Var001();
            }

            // run variation 2
            if ((allVariations || variationsToRun_.contains("2")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(2);
                Var002();
            }

            // run variation 3
            if ((allVariations || variationsToRun_.contains("3")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(3);
                Var003();
            }

            // run variation 4
            if ((allVariations || variationsToRun_.contains("4")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(4);
                Var004();
            }

            // run variation 5
            if ((allVariations || variationsToRun_.contains("5")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(5);
                Var005();
            }

            // run variation 6
            if ((allVariations || variationsToRun_.contains("6")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(6);
                Var006();
            }

            // run variation 7
            if ((allVariations || variationsToRun_.contains("7")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(7);
                Var007();
            }

            // run variation 8
            if ((allVariations || variationsToRun_.contains("8")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(8);
                Var008();
            }

            // run variation 9
            if ((allVariations || variationsToRun_.contains("9")) &&
                runMode_ == ATTENDED) // Note: This is an attended variation.
            {
                setVariation(9);
                Var009();
            }

            // run variation 10
            if ((allVariations || variationsToRun_.contains("10")) &&
                runMode_ != ATTENDED) // Note: This is an unattended variation.
            {
                setVariation(10);
                Var010();
            }

            // run variation 11
            if ((allVariations || variationsToRun_.contains("11")) &&
                runMode_ == ATTENDED) // Note: This is an attended variation.
            {
                setVariation(11);
                Var011();
            }

            // run variation 12
            if ((allVariations || variationsToRun_.contains("12")) &&
                runMode_ == ATTENDED) // Note: This is an attended variation.
            {
                setVariation(12);
                Var012();
            }

            // run variation 13
            if ((allVariations || variationsToRun_.contains("13")) &&
                runMode_ == ATTENDED) // Note: This is an attended variation.
            {
                setVariation(13);
                Var013();
            }

            // run variation 14
            if ((allVariations || variationsToRun_.contains("14")) &&
                runMode_ == ATTENDED) // Note: This is an attended variation.
            {
                setVariation(14);
                Var014();
            }

            // run variation 15
            if ((allVariations || variationsToRun_.contains("15")) &&
                runMode_ == ATTENDED) // Note: This is an attended variation.
            {
                setVariation(15);
                Var015();
            }

            // $$$ TO DO $$$
	        // Add an 'if' block using the following as a template for each variation.
	        // Make sure you correctly set the variation number
	        // and runmode in the 'if' condition, and the
	        // variation number in the setVariation call.
	        //
	        // replace the X with the variation number you are adding
	        //
            /* $$$ TO DO $$$ - delete this line
	        if ((allVariations || variationsToRun_.contains("X")) &&
		    runMode_ != ATTENDED) // Note: This is an unattended variation.
	        {
		       setVariation(X);
		       VarXXX();
	        }
            $$$ TO DO $$$ - delete this line */

        } // end try block

        catch( Exception e )
        {
            failed(e, "Unexpected exception");
        }

    } // end run method


    /**
     * Functional test instantiation of the lineDataRecordWriter class and writing of the line data
    **/
    public void Var001()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // close the outputstream 
          os.close();
          
          // got this far so must have succeeded
          succeeded();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var001
    
    /**
     * Diagnostic test instantiation of the lineDataRecordWriter class with no output
       stream defined
    **/
    public void Var002()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          // try { 
          //     // create the output spooled file to hold the record data
          //     os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          // }
          // catch (Exception e) {
          //     System.out.println("Error occurred creating spooled file");
          //     e.printStackTrace();
          // }
           
          // if (os != null)
          LineDataRecordWriter ldw;
               
          // create the line data record writer
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionIs(e, "IOException", "Stream closed"))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }

    } // end Var002
    
    /**
     * Diagnostic test instantiation of the lineDataRecordWriter class with invalid CSID specified
    **/
    public void Var003()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // trash the ccsid
          ccsid_ = 0;
          
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          // close the outputstream 
          os.close();
          
          failed("Expected exception did not occur.");
        }

        catch(Exception e)
        {
          if (exceptionIs(e, "UnsupportedEncodingException"))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }

    } // end Var003
    
    /**
     * Functional test which tests the getCcsid() method
    **/
    public void Var004()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat4();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the ccsid value
          if (ldw.getCcsid() == ccsid_)
          {
            succeeded();
          }  

          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var004
    
    /**
     * Diagnostic Test which forces the no format id exception
    **/
    public void Var005()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat5();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          // close the outputstream 
          os.close();
          
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionIs(e, "ExtendedIllegalStateException", "recordFormatID: Property is not set."))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }

    } // end Var005
   
    /**
     * Diagnostic Test which forces the no format type exception
    **/
    public void Var006()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat6();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          // close the outputstream 
          os.close();
         
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {  
          if (exceptionIs(e, "ExtendedIllegalStateException", "recordFormatType for record format TESTREC6  : Property is not set."))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }

    } // end Var006
       
    /**
     * Diagnostic Test which forces the no delimeter exception
    **/
    public void Var007()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat7();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          // close the outputstream 
          os.close();
          
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {  
          if (exceptionIs(e, "ExtendedIllegalStateException", "delimiter for record format TESTREC7  : Property is not set."))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }

    } // end Var007
       
    /**
     * Diagnostic Test which tests field description length less than data
    **/
    public void Var008()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat8();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          // close the outputstream 
          os.close();
          
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {

          if (exceptionIs(e, "ExtendedIllegalArgumentException", "Field description CUSNUM: Length is not valid."))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }

    } // end Var008
       
    /**
     * Functional Test which tests field description length equal to data
    **/
    public void Var009()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat9();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);
          
          // close the outputstream 
          os.close();
          
          succeeded();
        }
        
        catch(Exception e)
        {
          failed(e, "Unexpected exception");
        }

    } // end Var009
       
    /**
     * Functional test which tests the getEncoding() method
    **/
    public void Var010()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat10();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the encoding value
          String strEncode = ldw.getEncoding();
          Integer intCcsid = new Integer(ccsid_);
          
          if (strEncode.endsWith(intCcsid.toString()))
          {
            succeeded();
          }  
          else
          {
            failed("Encoding value not equal");
          } 
 
          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var010
    
    /**
     * Functional test which creates fixed length fields in a record and contains
     data that is the same length as the fields
    **/
    public void Var011()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat11();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord11(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the encoding value
          Integer intCcsid = new Integer(ccsid_);
          String strEncode = ldw.getEncoding();
          String strEncode2 = "Cp0"+ intCcsid.toString();
          
          if (strEncode.equals(strEncode2))
          {
            succeeded();
          }  
          else
          {
            failed("Encoding value not equal");
          }
          
          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var011
    
       /**
     * Functional test which creates fixed length fields in a record and contains
     data that is the one character less than the field and is justified left. This 
     result in a space between each field except the first and second.
    **/
    public void Var012()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat12();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord12(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the encoding value
          Integer intCcsid = new Integer(ccsid_);
          String strEncode = ldw.getEncoding();
          String strEncode2 = "Cp0"+ intCcsid.toString();
          
          if (strEncode.equals(strEncode2))
          {
            succeeded();
          }  
          else
          {
            failed("Encoding value not equal");
          }  
          
          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var012

       /**
     * Functional test which creates fixed length fields in a record and contains
     data that is the one character less than the field and is justified right. This 
     result in a space between each field.
    **/
    public void Var013()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat13();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord12(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the encoding value
          Integer intCcsid = new Integer(ccsid_);
          String strEncode = ldw.getEncoding();
          String strEncode2 = "Cp0"+ intCcsid.toString();
          
          if (strEncode.equals(strEncode2))
          {
            succeeded();
          }  
          else
          {
            failed("Encoding value not equal");
          } 
          
          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var013
    
       /**
     * Functional test which creates variable length fields in a record and contains
     data that is the one character in length
    **/
    public void Var014()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat14();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord14(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the encoding value
          Integer intCcsid = new Integer(ccsid_);
          String strEncode = ldw.getEncoding();
          String strEncode2 = "Cp0"+ intCcsid.toString();
          
          if (strEncode.equals(strEncode2))
          {
            succeeded();
          }  
          else
          {
            failed("Encoding value not equal");
          }
          
          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var014
    
       /**
     * Functional test which creates variable length fields in a record and contains
     data that is the one hundred characters in length
    **/
    public void Var015()
    {
        try
        {
          // create a ccsid
          ccsid_ = system_.getCcsid();
          
          // create output queue and specify spooled file data to be *LINE
          OutputQueue outQ = new OutputQueue(system_, "/QSYS.LIB/RLPLIB.LIB/LDRW.OUTQ");
          PrintParameterList parms = new PrintParameterList();
          parms.setParameter(PrintObject.ATTR_PRTDEVTYPE, "*LINE");
          
          // initialize the record format for writing data 
          RecordFormat recfmt = initializeRecordFormat15();
        
          // create a record and assign data to be printed...
          Record record = new Record(recfmt);
          createRecord15(record);
         
          SpooledFileOutputStream os = null;
          try { 
              // create the output spooled file to hold the record data
              os = new SpooledFileOutputStream(system_, parms, null, outQ);   
          }
          catch (Exception e) {
              System.out.println("Error occurred creating spooled file");
              e.printStackTrace();
          }
           
          // create the line data record writer
          LineDataRecordWriter ldw;
          ldw = new LineDataRecordWriter(os, ccsid_, system_);
          
          // write the record of data
          ldw.writeRecord(record);

          // check the encoding value
          Integer intCcsid = new Integer(ccsid_);
          String strEncode = ldw.getEncoding();
          String strEncode2 = "Cp0"+ intCcsid.toString();
          
          if (strEncode.equals(strEncode2))
          {
            succeeded();
          }  
          else
          {
            failed("Encoding value not equal");
          } 
          
          // close the outputstream 
          os.close();
        }

        catch(Exception e)
        {
          failed(e, "Exception occurred.");
        }

    } // end Var015
    
    
/* $$$ TO DO $$$ - delete this line
    public void VarXXX()
    {
	try
	{
	    // $$$ TO DO $$$
	    // Put your test variation code here.
	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end VarXXX
$$$ TO DO $$$ - delete this line */

  public static RecordFormat initializeRecordFormat()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // set the record format ID
        String d = "CUSTRECID";
        qcustcdt.setRecordFormatID(d);
        
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat4()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // set the record format ID
        String d = "TESTREC4";
        qcustcdt.setRecordFormatID(d);
        
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }
    
    public static RecordFormat initializeRecordFormat5()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // set the record format ID
        //String d = "CUSTRECID";
        //qcustcdt.setRecordFormatID(d);
        
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }

  public static RecordFormat initializeRecordFormat6()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        String d = "TESTREC6";
        qcustcdt.setRecordFormatID(d);
        
        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }

  public static RecordFormat initializeRecordFormat7()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // Create record field descriptions for the record format.
        // set the record format ID
        String d = "TESTREC7";
        qcustcdt.setRecordFormatID(d);
        
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        //qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat8()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        String d = "TESTREC8";
        qcustcdt.setRecordFormatID(d);
        
        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        //customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat9()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        String d = "TestRec9";
        qcustcdt.setRecordFormatID(d);

        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(3,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

     
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat10()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        String d = "TESTREC10";
        qcustcdt.setRecordFormatID(d);
        
        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription customerNumber =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,0),
                                                           "CUSNUM");
        CharacterFieldDescription lastName =
                          new CharacterFieldDescription(new AS400Text(8, ccsid_, system_), "LSTNAM");

        CharacterFieldDescription initials =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "INIT");

        CharacterFieldDescription street =
                          new CharacterFieldDescription(new AS400Text(13, ccsid_, system_), "STREET");

        CharacterFieldDescription city =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "CITY");

        CharacterFieldDescription state =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "STATE");

        ZonedDecimalFieldDescription zipCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(5,0),
                                                           "ZIPCOD");
        ZonedDecimalFieldDescription creditLimit =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(4,0),
                                                           "CDTLMT");
        ZonedDecimalFieldDescription chargeCode =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "CHGCOD");
        ZonedDecimalFieldDescription balanceDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "BALDUE");
        ZonedDecimalFieldDescription creditDue =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2),
                                                           "CDTDUE");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        customerNumber.setLayoutAttributes(10,justLeft);
        lastName.setLayoutAttributes(10,justLeft);
        initials.setLayoutAttributes(4,justLeft);
        street.setLayoutAttributes(15,justLeft);
        city.setLayoutAttributes(10,justLeft);
        state.setLayoutAttributes(3,justLeft);
        zipCode.setLayoutAttributes(5,justLeft);
        creditLimit.setLayoutAttributes(10,justRight);
        chargeCode.setLayoutAttributes(3,justRight);
        balanceDue.setLayoutAttributes(10,justRight);
        creditDue.setLayoutAttributes(10,justRight);

        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(customerNumber);
        qcustcdt.addFieldDescription(lastName);
        qcustcdt.addFieldDescription(initials);
        qcustcdt.addFieldDescription(street);
        qcustcdt.addFieldDescription(city);
        qcustcdt.addFieldDescription(state);
        qcustcdt.addFieldDescription(zipCode);
        qcustcdt.addFieldDescription(creditLimit);
        qcustcdt.addFieldDescription(chargeCode);
        qcustcdt.addFieldDescription(balanceDue);
        qcustcdt.addFieldDescription(creditDue);
        
        return qcustcdt;
    }

  public static RecordFormat initializeRecordFormat11()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        qcustcdt.setRecordFormatID("TESTREC11");

        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription field1 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "FIELD1");
        CharacterFieldDescription field2 =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "FIELD2");

        CharacterFieldDescription field3 =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "FIELD3");

        CharacterFieldDescription field4 =
                          new CharacterFieldDescription(new AS400Text(4, ccsid_, system_), "FIELD4");

        CharacterFieldDescription field5 =
                          new CharacterFieldDescription(new AS400Text(5, ccsid_, system_), "FIELD5");

        CharacterFieldDescription field6  =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "FIELD6");

        ZonedDecimalFieldDescription field7 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(7,0),
                                                           "FIELD7");
        ZonedDecimalFieldDescription field8 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(8,0),
                                                           "FIELD8");
        ZonedDecimalFieldDescription field9 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(9,0),
                                                           "FIELD9");
        ZonedDecimalFieldDescription field10 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(10,0),
                                                           "FIELD10");
        CharacterFieldDescription field11 =
                          new CharacterFieldDescription(new AS400Text(11, ccsid_, system_), "FIELD11");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        field1.setLayoutAttributes(1,justLeft);
        field2.setLayoutAttributes(2,justLeft);
        field3.setLayoutAttributes(3,justLeft);
        field4.setLayoutAttributes(4,justLeft);
        field5.setLayoutAttributes(5,justLeft);
        field6.setLayoutAttributes(6,justLeft);
        field7.setLayoutAttributes(7,justLeft);
        field8.setLayoutAttributes(8,justRight);
        field9.setLayoutAttributes(9,justRight);
        field10.setLayoutAttributes(10,justRight);
        field11.setLayoutAttributes(11,justRight);

     
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(field1);
        qcustcdt.addFieldDescription(field2);
        qcustcdt.addFieldDescription(field3);
        qcustcdt.addFieldDescription(field4);
        qcustcdt.addFieldDescription(field5);
        qcustcdt.addFieldDescription(field6);
        qcustcdt.addFieldDescription(field7);
        qcustcdt.addFieldDescription(field8);
        qcustcdt.addFieldDescription(field9);
        qcustcdt.addFieldDescription(field10);
        qcustcdt.addFieldDescription(field11);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat12()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        qcustcdt.setRecordFormatID("TESTREC12");

        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription field1 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "FIELD1");
        CharacterFieldDescription field2 =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "FIELD2");

        CharacterFieldDescription field3 =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "FIELD3");

        CharacterFieldDescription field4 =
                          new CharacterFieldDescription(new AS400Text(4, ccsid_, system_), "FIELD4");

        CharacterFieldDescription field5 =
                          new CharacterFieldDescription(new AS400Text(5, ccsid_, system_), "FIELD5");

        CharacterFieldDescription field6  =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "FIELD6");

        ZonedDecimalFieldDescription field7 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(7,0),
                                                           "FIELD7");
        ZonedDecimalFieldDescription field8 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(8,0),
                                                           "FIELD8");
        ZonedDecimalFieldDescription field9 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(9,0),
                                                           "FIELD9");
        ZonedDecimalFieldDescription field10 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(10,0),
                                                           "FIELD10");
        CharacterFieldDescription field11 =
                          new CharacterFieldDescription(new AS400Text(11, ccsid_, system_), "FIELD11");
      
        // assign constants from FieldDescription class
        int justLeft = FieldDescription.ALIGN_LEFT;
        // int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        field1.setLayoutAttributes(1,justLeft);
        field2.setLayoutAttributes(2,justLeft);
        field3.setLayoutAttributes(3,justLeft);
        field4.setLayoutAttributes(4,justLeft);
        field5.setLayoutAttributes(5,justLeft);
        field6.setLayoutAttributes(6,justLeft);
        field7.setLayoutAttributes(7,justLeft);
        field8.setLayoutAttributes(8,justLeft);
        field9.setLayoutAttributes(9,justLeft);
        field10.setLayoutAttributes(10,justLeft);
        field11.setLayoutAttributes(11,justLeft);

     
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(field1);
        qcustcdt.addFieldDescription(field2);
        qcustcdt.addFieldDescription(field3);
        qcustcdt.addFieldDescription(field4);
        qcustcdt.addFieldDescription(field5);
        qcustcdt.addFieldDescription(field6);
        qcustcdt.addFieldDescription(field7);
        qcustcdt.addFieldDescription(field8);
        qcustcdt.addFieldDescription(field9);
        qcustcdt.addFieldDescription(field10);
        qcustcdt.addFieldDescription(field11);
        
        return qcustcdt;
    }

  public static RecordFormat initializeRecordFormat13()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        qcustcdt.setRecordFormatID("TESTREC13");

        // Create record field descriptions for the record format.
        ZonedDecimalFieldDescription field1 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(1,0),
                                                           "FIELD1");
        CharacterFieldDescription field2 =
                          new CharacterFieldDescription(new AS400Text(2, ccsid_, system_), "FIELD2");

        CharacterFieldDescription field3 =
                          new CharacterFieldDescription(new AS400Text(3, ccsid_, system_), "FIELD3");

        CharacterFieldDescription field4 =
                          new CharacterFieldDescription(new AS400Text(4, ccsid_, system_), "FIELD4");

        CharacterFieldDescription field5 =
                          new CharacterFieldDescription(new AS400Text(5, ccsid_, system_), "FIELD5");

        CharacterFieldDescription field6  =
                          new CharacterFieldDescription(new AS400Text(6, ccsid_, system_), "FIELD6");

        ZonedDecimalFieldDescription field7 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(7,0),
                                                           "FIELD7");
        ZonedDecimalFieldDescription field8 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(8,0),
                                                           "FIELD8");
        ZonedDecimalFieldDescription field9 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(9,0),
                                                           "FIELD9");
        ZonedDecimalFieldDescription field10 =
                          new ZonedDecimalFieldDescription(new AS400ZonedDecimal(10,0),
                                                           "FIELD10");
        CharacterFieldDescription field11 =
                          new CharacterFieldDescription(new AS400Text(11, ccsid_, system_), "FIELD11");
      
        // assign constants from FieldDescription class
        // int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        field1.setLayoutAttributes(1,justRight);
        field2.setLayoutAttributes(2,justRight);
        field3.setLayoutAttributes(3,justRight);
        field4.setLayoutAttributes(4,justRight);
        field5.setLayoutAttributes(5,justRight);
        field6.setLayoutAttributes(6,justRight);
        field7.setLayoutAttributes(7,justRight);
        field8.setLayoutAttributes(8,justRight);
        field9.setLayoutAttributes(9,justRight);
        field10.setLayoutAttributes(10,justRight);
        field11.setLayoutAttributes(11,justRight);

     
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        //qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(field1);
        qcustcdt.addFieldDescription(field2);
        qcustcdt.addFieldDescription(field3);
        qcustcdt.addFieldDescription(field4);
        qcustcdt.addFieldDescription(field5);
        qcustcdt.addFieldDescription(field6);
        qcustcdt.addFieldDescription(field7);
        qcustcdt.addFieldDescription(field8);
        qcustcdt.addFieldDescription(field9);
        qcustcdt.addFieldDescription(field10);
        qcustcdt.addFieldDescription(field11);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat14()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        qcustcdt.setRecordFormatID("TESTREC14");

        // Create record field descriptions for the record format.
        CharacterFieldDescription field1 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD1");

        CharacterFieldDescription field2 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD2");

        CharacterFieldDescription field3 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD3");

        CharacterFieldDescription field4 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD4");

        CharacterFieldDescription field5 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD5");

        CharacterFieldDescription field6  =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD6");

        CharacterFieldDescription field7 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD7");

        CharacterFieldDescription field8 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD8");
        
        CharacterFieldDescription field9 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD9");
        
        CharacterFieldDescription field10 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD10");
        
        CharacterFieldDescription field11 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD11");
      
        // assign constants from FieldDescription class
        // int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        field1.setLayoutAttributes(1,justRight);
        field2.setLayoutAttributes(2,justRight);
        field3.setLayoutAttributes(3,justRight);
        field4.setLayoutAttributes(4,justRight);
        field5.setLayoutAttributes(5,justRight);
        field6.setLayoutAttributes(6,justRight);
        field7.setLayoutAttributes(7,justRight);
        field8.setLayoutAttributes(8,justRight);
        field9.setLayoutAttributes(9,justRight);
        field10.setLayoutAttributes(10,justRight);
        field11.setLayoutAttributes(11,justRight);

     
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(field1);
        qcustcdt.addFieldDescription(field2);
        qcustcdt.addFieldDescription(field3);
        qcustcdt.addFieldDescription(field4);
        qcustcdt.addFieldDescription(field5);
        qcustcdt.addFieldDescription(field6);
        qcustcdt.addFieldDescription(field7);
        qcustcdt.addFieldDescription(field8);
        qcustcdt.addFieldDescription(field9);
        qcustcdt.addFieldDescription(field10);
        qcustcdt.addFieldDescription(field11);
        
        return qcustcdt;
    }
    
  public static RecordFormat initializeRecordFormat15()
    {
        // Create the record format. 
        RecordFormat qcustcdt = new RecordFormat();
        
        // set the record format ID
        qcustcdt.setRecordFormatID("TESTREC15");

        // Create record field descriptions for the record format.
        CharacterFieldDescription field1 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD1");

        CharacterFieldDescription field2 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD2");

        CharacterFieldDescription field3 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD3");

        CharacterFieldDescription field4 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD4");

        CharacterFieldDescription field5 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD5");

        CharacterFieldDescription field6  =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD6");

        CharacterFieldDescription field7 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD7");

        CharacterFieldDescription field8 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD8");
        
        CharacterFieldDescription field9 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD9");
        
        CharacterFieldDescription field10 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD10");
        
        CharacterFieldDescription field11 =
                          new CharacterFieldDescription(new AS400Text(100, ccsid_, system_), "FIELD11");
      
        // assign constants from FieldDescription class
        // int justLeft = FieldDescription.ALIGN_LEFT;
        int justRight = FieldDescription.ALIGN_RIGHT;
       
        // set the length and alignment attributes for writing the fields
        // The length indicates how many characters the field is, and
        // justification indicates where in the layout field the data
        // should be placed.
        field1.setLayoutAttributes(1,justRight);
        field2.setLayoutAttributes(2,justRight);
        field3.setLayoutAttributes(3,justRight);
        field4.setLayoutAttributes(4,justRight);
        field5.setLayoutAttributes(5,justRight);
        field6.setLayoutAttributes(6,justRight);
        field7.setLayoutAttributes(7,justRight);
        field8.setLayoutAttributes(8,justRight);
        field9.setLayoutAttributes(9,justRight);
        field10.setLayoutAttributes(10,justRight);
        field11.setLayoutAttributes(11,justRight);

     
        // if this were a variable field length record,
        // we would set the type and delimiter accordingly.  We
        // also would not have needed to specify layoutLength and
        // layoutAlignment values.
        qcustcdt.setRecordFormatType(RecordFormat.VARIABLE_LAYOUT_LENGTH);
        qcustcdt.setDelimiter(';');
 
        // set the record type to fixed field length 
        //qcustcdt.setRecordFormatType(RecordFormat.FIXED_LAYOUT_LENGTH);
        
        // add the field descriptions to the record format.
        qcustcdt.addFieldDescription(field1);
        qcustcdt.addFieldDescription(field2);
        qcustcdt.addFieldDescription(field3);
        qcustcdt.addFieldDescription(field4);
        qcustcdt.addFieldDescription(field5);
        qcustcdt.addFieldDescription(field6);
        qcustcdt.addFieldDescription(field7);
        qcustcdt.addFieldDescription(field8);
        qcustcdt.addFieldDescription(field9);
        qcustcdt.addFieldDescription(field10);
        qcustcdt.addFieldDescription(field11);
        
        return qcustcdt;
    }
    
    /**
     ** Creates the actual record with data
     **/
    public static void createRecord(Record record)
    {
        record.setField("CUSNUM", new BigDecimal(323));
        record.setField("LSTNAM", "Johnson");
        record.setField("INIT", "B E");
        record.setField("STREET", "5234 Elm St");
        record.setField("CITY", "Rchstr");
        record.setField("STATE", "MN");
        record.setField("ZIPCOD", new BigDecimal(55901));
        record.setField("CDTLMT", new BigDecimal(5000.00));
        record.setField("CHGCOD", new BigDecimal(3));
        record.setField("BALDUE", new BigDecimal(25.00));
        record.setField("CDTDUE", new BigDecimal(0.00));
    }

    /**
     ** Creates the actual record with data
     **/
    public static void createRecord11(Record record)
    {
        record.setField("FIELD1", new BigDecimal(1));
        record.setField("FIELD2", "12");
        record.setField("FIELD3", "123");
        record.setField("FIELD4", "1234");
        record.setField("FIELD5", "12345");  
        record.setField("FIELD6", "123456");
        record.setField("FIELD7", new BigDecimal(1234567));
        record.setField("FIELD8", new BigDecimal(12345678));
        record.setField("FIELD9", new BigDecimal(123456789));
        record.setField("FIELD10", new BigDecimal(1234567890));
        record.setField("FIELD11", "12345678901");
    }

    /**
     ** Creates the actual record with data
     **/
    public static void createRecord12(Record record)
    {
        record.setField("FIELD1", new BigDecimal(1));
        record.setField("FIELD2", "1");
        record.setField("FIELD3", "12");
        record.setField("FIELD4", "123");
        record.setField("FIELD5", "1234");  
        record.setField("FIELD6", "12345");
        record.setField("FIELD7", new BigDecimal(123456));
        record.setField("FIELD8", new BigDecimal(1234567));
        record.setField("FIELD9", new BigDecimal(12345678));
        record.setField("FIELD10", new BigDecimal(123456789));
        record.setField("FIELD11", "1234567890");
    }
    /**
     ** Creates the actual record with data
     **/
    public static void createRecord14(Record record)
    {
        record.setField("FIELD1", "1");
        record.setField("FIELD2", "1");
        record.setField("FIELD3", "1");
        record.setField("FIELD4", "1");
        record.setField("FIELD5", "1");  
        record.setField("FIELD6", "1");
        record.setField("FIELD7", "1");
        record.setField("FIELD8", "1");
        record.setField("FIELD9", "1");
        record.setField("FIELD10", "1");
        record.setField("FIELD11", "1");
    }

    /**
     ** Creates the actual record with data
     **/
    public static void createRecord15(Record record)
    {
        record.setField("FIELD1", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD2", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD3", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD4", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD5", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD6", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD7", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD8", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD9", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD10", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
        record.setField("FIELD11", "123456789112345678921234567893123456789412345678951234567896123456789712345678981234567899123456789");
    }

} // end NPLDRWTestcase class


