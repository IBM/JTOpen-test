///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RMRecordConstructorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RM;


import java.io.FileOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;


import com.ibm.as400.access.AS400;
import com.ibm.as400.data.*;

import test.RMTest;
import test.Testcase;

import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.ZonedDecimalFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.DBCSGraphicFieldDescription;
import com.ibm.as400.access.DateFieldDescription;
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.DBCSOnlyFieldDescription;
import com.ibm.as400.access.ArrayFieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.AS400Array;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueEntry;
import com.ibm.as400.access.CommandCall;
import java.math.BigDecimal;
import com.ibm.as400.access.BinaryConverter;



/**
 The RMRecordConstructorTestcase class tests the following methods of the RecordFormatDocument class:
 <li>RecordFormatDocument(Record)
 <li>RecordFormatDocument(RecordFormat).
 Question: I'm having difficulty envisioning how the offset and offsetfrom attributes apply with Records.
 Any suggestions?  offset and offsetfrom are output only concepts.  
 **/
public class RMRecordConstructorTestcase extends Testcase
{
    /**
     Constructor.
     **/
    public RMRecordConstructorTestcase(AS400 systemObject, Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream)
    {
        super(systemObject, "RMRecordConstructorTestcase", (Vector)namesAndVars.get("RMRecordConstructorTestcase"), runMode, fileOutputStream);
    }


    /**
     Test RecordFormatDocument constructor with Record parameter.  Pass an empty Record, i.e., a record
     constructed with the default constructor. (No format). This should cause an exception since
     record format not set.
     **/
    public void Var001()
    {
        try
        {
           Record newRec = new Record();
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(newRec);
       
           assertCondition(false, "Did not throw exception but got "+rfmlDoc); 
        }
        catch (Exception e)
        {
          assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }
    }


    /**
     Test RecordFormatDocument constructor with Record parameter.  Pass an empty Record, i.e., a record
     with a valid format but whose fields all are blank.
     **/
    public void Var002()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {

           RecordFormat recFmt = new RecordFormat("format1");
           CharacterFieldDescription char1 =
               new CharacterFieldDescription(new AS400Text(8, systemObject_), "char1");
           ZonedDecimalFieldDescription zoned =
               new ZonedDecimalFieldDescription(new AS400ZonedDecimal(6,2), "zoned");
           BinaryFieldDescription bin2 =
               new BinaryFieldDescription(new AS400Bin2(), "bin2");
           BinaryFieldDescription ubin2 =
               new BinaryFieldDescription(new AS400UnsignedBin2(), "ubin2");
           BinaryFieldDescription bin4 =
               new BinaryFieldDescription(new AS400Bin4(), "bin4");
           BinaryFieldDescription ubin4 =
               new BinaryFieldDescription(new AS400UnsignedBin4(), "ubin4");
           BinaryFieldDescription bin8=
               new BinaryFieldDescription(new AS400Bin8(), "bin8");
           PackedDecimalFieldDescription packed =
               new PackedDecimalFieldDescription(new AS400PackedDecimal(7,0), "packed");
           FloatFieldDescription float4 =
               new FloatFieldDescription(new AS400Float4(), "float4");
           FloatFieldDescription float8 =
               new FloatFieldDescription(new AS400Float8(), "float8");

           recFmt.addFieldDescription(char1);
           recFmt.addFieldDescription(zoned);
           recFmt.addFieldDescription(bin2);
           recFmt.addFieldDescription(ubin2);
           recFmt.addFieldDescription(bin4);
           recFmt.addFieldDescription(ubin4);
           recFmt.addFieldDescription(bin8);
           recFmt.addFieldDescription(packed);
           recFmt.addFieldDescription(float4);
           recFmt.addFieldDescription(float8);

           Record newRec = new Record(recFmt);
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(newRec);

           Descriptor root = rfmlDoc.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
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
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
               //System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("char1"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

               grandChild1NumAttrs++;
             }
           }           
            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad precision returned. Precision = " + attrVal);
                       return;
                   }
               } 


                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));

            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("bin2"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("15"))
                   {
                       failed("Bad precisoin returned. Precision = " + attrVal);
                       return;
                   }
               } 

                grandChild3NumAttrs++;
              }
            }

            Descriptor grandChild4 = (Descriptor)(grandChildren.elementAt(3));

            String[] grandChild4AttList = grandChild4.getAttributeList();
            int grandChild4NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild4AttList.length; i++) {
              String attrVal = grandChild4.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild4AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("ubin2"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("16"))
                   {
                       failed("Bad precisoin returned. Precision = " + attrVal);
                       return;
                   }
               } 

                grandChild4NumAttrs++;
              }
            }

            Descriptor grandChild5 = (Descriptor)(grandChildren.elementAt(4));

            String[] grandChild5AttList = grandChild5.getAttributeList();
            int grandChild5NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild5AttList.length; i++) {
              String attrVal = grandChild5.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
               //System.out.println("grandChild5Attr: " + grandChild5AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("bin4"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("31"))
                   {
                       failed("Bad precisoin returned. Precision = " + attrVal);
                       return;
                   }
               } 
               
                grandChild5NumAttrs++;
              }
            }

            Descriptor grandChild6= (Descriptor)(grandChildren.elementAt(5));

            String[] grandChild6AttList = grandChild6.getAttributeList();
            int grandChild6NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild6AttList.length; i++) {
              String attrVal = grandChild6.getAttributeValue(grandChild6AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild6AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("ubin4"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("32"))
                   {
                       failed("Bad precisoin returned. Precision = " + attrVal);
                       return;
                   }
               } 
               
                grandChild6NumAttrs++;
              }
            }


            Descriptor grandChild7= (Descriptor)(grandChildren.elementAt(6));

            String[] grandChild7AttList = grandChild7.getAttributeList();
            int grandChild7NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild7AttList.length; i++) {
              String attrVal = grandChild7.getAttributeValue(grandChild7AttList[i]);
              if (attrVal != null) {
               //System.out.println("grandChild4Attr: " + grandChild7AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("bin8"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("63"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild7NumAttrs++;
              }
            }

            Descriptor grandChild8= (Descriptor)(grandChildren.elementAt(7));

            String[] grandChild8AttList = grandChild8.getAttributeList();
            int grandChild8NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild8AttList.length; i++) {
              String attrVal = grandChild8.getAttributeValue(grandChild8AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild8AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("packed"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("packed"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("7"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }
                grandChild8NumAttrs++;
              }
            }

//            Descriptor grandChild9= (Descriptor)(grandChildren.elementAt(8));

//            String[] grandChild9AttList = grandChild9.getAttributeList();
//            int grandChild9NumAttrs = 0;  // number of non-null attributes
//            for (int i=0; i<grandChild9AttList.length; i++) {
//              String attrVal = grandChild9.getAttributeValue(grandChild9AttList[i]);
//              if (attrVal != null) {
//                //System.out.println("grandChild9Attr: " + grandChild9AttList[i] + " = " + attrVal);
//               if (i== 0)
//               {
//                   if (!attrVal.equals("array"))
//                  {
//                       failed("Bad field name returned. Name = " + attrVal);
//                       return;
//                   }
//               } else if (i==1)
//               {
//                   if (!attrVal.equals("10"))
//                   {
//                       failed("Bad count returned. Count = " + attrVal);
//                       return;
//                   }
//               } else if (i==2)
//               {
//                   if (!attrVal.equals("char"))
//                   {
//                       failed("Bad type returned. Type = " + attrVal);
//                       return;
//                  }
//               } else if (i==3)
//               {
//                   if (!attrVal.equals("5"))
//                   {
//                       failed("Bad length returned. Length = " + attrVal);
//                       return;
//                   }
//                }

//                grandChild9NumAttrs++;
//              }
//            }

            Descriptor grandChild9= (Descriptor)(grandChildren.elementAt(8));

            String[] grandChild9AttList = grandChild9.getAttributeList();
            int grandChild9NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild9AttList.length; i++) {
              String attrVal = grandChild9.getAttributeValue(grandChild9AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild9Attr: " + grandChild9AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("float4"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("float"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }
                grandChild9NumAttrs++;
              }
            }
            Descriptor grandChild10= (Descriptor)(grandChildren.elementAt(9));

            String[] grandChild10AttList = grandChild10.getAttributeList();
            int grandChild10NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild10AttList.length; i++) {
              String attrVal = grandChild10.getAttributeValue(grandChild10AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild9Attr: " + grandChild9AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("float8"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("float"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild10NumAttrs++;
              }
            }

          file1 = new File("RFFile");
          file1.createNewFile();  // Note: This method is new in Java2.
          raFile1 = new RandomAccessFile(file1, "r");
          rfmlDoc.toXml(file1);


           Record newRec2 = rfmlDoc.toRecord("FORMAT1");
           if (newRec2.getNumberOfFields() != 10)
           {
             failed("Bad number of fields returned. raFile1="+raFile1);
             return;
           }
           if (!newRec2.getField(0).equals("        ")) // or ""?
           {
             failed("Bad value returned for first field.");
             return;
           }
           if (( (BigDecimal) newRec2.getField(1)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for second field.");
             return;
           }
           if (( (Short) newRec2.getField(2)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for third field.");
             return;
           }
           if (( (Integer) newRec2.getField(3)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for fourth field.");
             return;
           }
           if (( (Integer) newRec2.getField(4)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for fifth field.");
             return;
           }
           if (( (Long) newRec2.getField(5)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for sixth field.");
             return;
           }

           if (( (Long) newRec2.getField(6)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for seventh field.");
             return;
           }
           if (( (BigDecimal) newRec2.getField(7)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for eighth field.");
             return;
           }

           if (( (Float) newRec2.getField(8)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for ninth field.");
             return;
           }
           if (( (Double) newRec2.getField(9)).intValue() != 0) // or ""?
           {
             failed("Bad value returned for tenth field.");
             return;
           }

           succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test RecordFormatDocument constructor with Record parameter.  Pass a Record that contains 3 fields
     each of whose value is null. Ensure proper nodes created and proper values set for the record.
     Note: This variation requires that library RMLIB exist and be populated on the server.  FTP the file "rmlib.savf" from the testcase source directory to the server, and then do a RSTLIB.
     NOTE: PROBLEM READING IN TESTFILE3 WITH recFmtAll3.rfml. THIS FILE HAS A NUMBER OF FIELD
     THAT HAVE BEEN INITIALIZED TO DEFAULT VALUES.  PROBLEM SIMILAR TO PROBLEM LOADING PACKED
     VALUES AFTER INT 4 VALUES ALTHOUGH THE PACKED FIELD FOLLOWS AN INT 2 IN THIS FILE/
     **/
    public void Var003()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {
           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtAllTypesNoInit");
           RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");

           // Create an AS400 object for the AS/400 system that has the file.
           AS400 as400 = new AS400(systemObject_);

           // Create the sequential file object that represents the
           // file on the AS/400.  We use a QSYSObjectPathName object
           // to get the name of the file into the correct format.

           QSYSObjectPathName fileName = new QSYSObjectPathName("RMLIB", "ALLTYPESF", "FILE");

           SequentialFile file = new SequentialFile(as400, fileName.getPath());

           // Let the file object know the format of the records.
           file.setRecordFormat(recFmt);

           // Open the file for read-only access.  Specify a blocking
           // factor of 10 (the file object will get 10 records when
           // it accesses the AS/400 for data).  Do not use commitment
           // control.
           file.open(SequentialFile.READ_ONLY,
                     10,
                     SequentialFile.COMMIT_LOCK_LEVEL_NONE);


           // Read the first record of the file.
           Record newRec = file.readNext();

           // Create an RFML document based on the record
           RecordFormatDocument rfmlDoc2 = new RecordFormatDocument(newRec);

           file1 = new File("emptyFile");
           file1.createNewFile();  // Note: This method is new in Java2.
           raFile1 = new RandomAccessFile(file1, "r");
           rfmlDoc.toXml(file1);

           Descriptor root = rfmlDoc2.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
           }

           Descriptor child1 = (Descriptor)(children.elementAt(0));
           String[] child1AttList = child1.getAttributeList();

           int child1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<child1AttList.length; i++) {
             String attrVal = child1.getAttributeValue(child1AttList[i]);
             if (attrVal != null) {
               //System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
               if (i==0)
                  if (!attrVal.equals("FORMAT1"))
                  {
                     failed("Bad format name raFile1="+raFile1);
                     return; 
                  } 
               child1NumAttrs++;
             }
           }

           enumeration =child1.getChildren();
           Vector grandChildren = new Vector();
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
               //System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field1"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("float"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
               grandChild1NumAttrs++;
             }
           }

            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field2"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("float"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));

            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field3"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("25"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

                grandChild3NumAttrs++;
              }
            }


            Descriptor grandChild4 = (Descriptor)(grandChildren.elementAt(3));

            String[] grandChild4AttList = grandChild4.getAttributeList();
            int grandChild4NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild4AttList.length; i++) {
              String attrVal = grandChild4.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild4AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field4"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

                grandChild4NumAttrs++;
              }
            }

            Descriptor grandChild5 = (Descriptor)(grandChildren.elementAt(4));

            String[] grandChild5AttList = grandChild5.getAttributeList();
            int grandChild5NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild5AttList.length; i++) {
              String attrVal = grandChild5.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
               // System.out.println("grandChild5Attr: " + grandChild5AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field5"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild5NumAttrs++;
              }
            }

            Descriptor grandChild6= (Descriptor)(grandChildren.elementAt(5));

            String[] grandChild6AttList = grandChild6.getAttributeList();
            int grandChild6NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild6AttList.length; i++) {
              String attrVal = grandChild6.getAttributeValue(grandChild6AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild6Attr: " + grandChild6AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field6"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild6NumAttrs++;
              }
            }

            Descriptor grandChild7= (Descriptor)(grandChildren.elementAt(6));

            String[] grandChild7AttList = grandChild7.getAttributeList();
            int grandChild7NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild7AttList.length; i++) {
              String attrVal = grandChild7.getAttributeValue(grandChild7AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild7Attr: " + grandChild7AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field7"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("packed"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("10"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("5"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild7NumAttrs++;
              }
            }

            Descriptor grandChild8= (Descriptor)(grandChildren.elementAt(7));

            String[] grandChild8AttList = grandChild8.getAttributeList();
            int grandChild8NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild8AttList.length; i++) {
              String attrVal = grandChild8.getAttributeValue(grandChild8AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild8Attr: " + grandChild8AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field8"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("22"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("7"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild8NumAttrs++;
              }
            }

            Descriptor grandChild9= (Descriptor)(grandChildren.elementAt(8));

            String[] grandChild9AttList = grandChild9.getAttributeList();
            int grandChild9NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild9AttList.length; i++) {
              String attrVal = grandChild9.getAttributeValue(grandChild9AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild9Attr: " + grandChild9AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field9"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("byte"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("10"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild9NumAttrs++;
              }
            }

           Record newRec2 = rfmlDoc2.toRecord("FORMAT1");
           if (newRec2.getNumberOfFields() != 9) 
           {
             failed("Bad number of fields returned.");
             return;
           }
           if (!rfmlDoc2.getValue("FORMAT1.field3").equals("Testing again            ")) // or ""?
           {
             failed("Bad value returned for char field.");
             return;
           }
           if (!newRec2.getField("field3").equals("Testing again            ")) // or ""?
           {
             failed("Bad value returned for char field.");
             return;
           }
           Float flt4 = (Float) rfmlDoc2.getValue("FORMAT1.field1");
           float flt4v = flt4.floatValue();         
           if (flt4v != 0) 
           {
             failed("Bad value returned for float4 field.");
             return;
           }
           Double flt8 = (Double) rfmlDoc2.getValue("FORMAT1.field2");
           double flt8v = flt8.doubleValue();         
           if (flt8v != 0) 
           {
             failed("Bad value returned for float8 field.");
             return;
           }
           Short sht = (Short) rfmlDoc2.getValue("FORMAT1.field4");
           int shtv = sht.intValue();         
           if (shtv != 9) 
           {
             failed("Bad value returned for int 2  field.");
             return;
           }
           Integer int4 = (Integer) rfmlDoc2.getValue("FORMAT1.field5");
           int int4v = int4.intValue();         
           if (int4v != 9) 
           {
             failed("Bad value returned for int 4 field.");
             return;
           }
           Long int8 = (Long) rfmlDoc2.getValue("FORMAT1.field6");
           int int8v = int8.intValue();         
           if (int8v != 0) 
           {
             failed("Bad value returned for int 8 field.");
             return;
           }
           BigDecimal pck = (BigDecimal) rfmlDoc2.getValue("FORMAT1.field7");
           double pckv = pck.doubleValue();         
           if (pckv != .00009) 
           {
             failed("Bad value returned for packed  field.");
             return;
           }
           BigDecimal zon = (BigDecimal) rfmlDoc2.getValue("FORMAT1.field8");
           double zonv = zon.doubleValue();         
           if (zonv != .0000009) 
           {
             failed("Bad value returned for zoned  field.");
             return;
           }


           // Read the second record of the file. This record contains null
           // values - ensure they are handled correctly.
           // Note: RFML cannot handle nulls.   When a null is encountered on 
           // construction a Warning message will be logged and the default
           // value for the field is returned instead of null.  In this
           // case the default value is the empty string.  If the default
           // value is null, a null will be returned.
           newRec = file.readNext();

           // Create an RFML document based on the record
           RecordFormatDocument rfmlDoc3 = new RecordFormatDocument(newRec);
           if (rfmlDoc3.getValue("FORMAT1.field3") != null) // or ""?
           {
             failed("Non empty string value returned for char field. Value =" + rfmlDoc3.getValue("FORMAT1.field3") + "..");
             return;
           }
           flt4 = (Float) rfmlDoc3.getValue("FORMAT1.field1");
           flt4v = flt4.floatValue();         
           if (flt4v != 0) 
           {
             failed("Bad value returned for float4 field.");
             return;
           }
           flt8 = (Double) rfmlDoc3.getValue("FORMAT1.field2");
           flt8v = flt8.doubleValue();         
           if (flt8v != 0) 
           {
             failed("Bad value returned for float8 field.");
             return;
           }
           sht = (Short) rfmlDoc3.getValue("FORMAT1.field4");
           shtv = sht.intValue();         
           if (shtv != 10) 
           {
             failed("Bad value returned for int 2  field.");
             return;
           }
           int4 = (Integer) rfmlDoc3.getValue("FORMAT1.field5");
           if (int4 != null)         
           {
             failed("Non null value returned for int 4 field. Value = " + int4);
             return;
           }
           int8 = (Long) rfmlDoc3.getValue("FORMAT1.field6");
           int8v = int8.intValue();         
           if (int8v != 0) 
           {
             failed("Bad value returned for int 8 field.");
             return;
           }
           pck = (BigDecimal) rfmlDoc3.getValue("FORMAT1.field7");
           pckv = pck.doubleValue();         
           if (pckv != .00099) 
           {
             failed("Bad value returned for packed  field.");
             return;
           }
           zon = (BigDecimal) rfmlDoc3.getValue("FORMAT1.field8");
           zonv = zon.doubleValue();         
           if (zonv != .0000099) 
           {
             failed("Bad value returned for zoned  field.");
             return;
           }

           succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test RecordFormatDocument constructor with Record parameter.  Pass a valid Record 
     representing QCUSTCDT format with valid data. Ensure parsed correctly and ensure correct
     rfml values set.
     **/
    public void Var004()
    {
        try
        {
//           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.qcustcdts");
//           RecordFormat recFmt = rfmlDoc.toRecordFormat("cusrec");

           // Create an AS400 object for the AS/400 system that has the file.
           AS400 as400 = new AS400(systemObject_);

           // Create the sequential file object that represents the
           // file on the AS/400.  We use a QSYSObjectPathName object
           // to get the name of the file into the correct format.
           QSYSObjectPathName fileName = new QSYSObjectPathName("QIWS", "QCUSTCDT", "FILE");

           SequentialFile file = new SequentialFile(as400, fileName.getPath());

           // Let the file object know the format of the records.
           file.setRecordFormat();

           // Open the file for read-only access.  Specify a blocking
           // factor of 10 (the file object will get 10 records when
           // it accesses the AS/400 for data).  Do not use commitment
           // control.
           file.open(SequentialFile.READ_ONLY,
                     10,
                     SequentialFile.COMMIT_LOCK_LEVEL_NONE);


           // Write the first record of the file.
           Record newRec = file.readNext();

           // Create an RFML document based on the record
           RecordFormatDocument rfmlDoc2 = new RecordFormatDocument(newRec);

           Descriptor root = rfmlDoc2.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
           }

           Descriptor child1 = (Descriptor)(children.elementAt(0));
           String[] child1AttList = child1.getAttributeList();

           int child1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<child1AttList.length; i++) {
             String attrVal = child1.getAttributeValue(child1AttList[i]);
             if (attrVal != null) {
               //System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
               if (i==0)
                  if (!attrVal.equals("CUSREC"))
                  {
                     failed("Bad format name");
                     return; 
                  } 
               child1NumAttrs++;
             }
           }

           enumeration =child1.getChildren();
           Vector grandChildren = new Vector();
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
               //System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("CUSNUM"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

               grandChild1NumAttrs++;
             }
           }           
            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("LSTNAM"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));

            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("INIT"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("3"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               }
                grandChild3NumAttrs++;
              }
            }

            Descriptor grandChild4 = (Descriptor)(grandChildren.elementAt(3));

            String[] grandChild4AttList = grandChild4.getAttributeList();
            int grandChild4NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild4AttList.length; i++) {
              String attrVal = grandChild4.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild4AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("STREET"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("13"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild4NumAttrs++;
              }
            }

            Descriptor grandChild5 = (Descriptor)(grandChildren.elementAt(4));

            String[] grandChild5AttList = grandChild5.getAttributeList();
            int grandChild5NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild5AttList.length; i++) {
              String attrVal = grandChild5.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild5Attr: " + grandChild5AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("CITY"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild5NumAttrs++;
              }
            }

            Descriptor grandChild6= (Descriptor)(grandChildren.elementAt(5));

            String[] grandChild6AttList = grandChild6.getAttributeList();
            int grandChild6NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild6AttList.length; i++) {
              String attrVal = grandChild6.getAttributeValue(grandChild6AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild6AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("STATE"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               }
                grandChild6NumAttrs++;
              }
            }


            Descriptor grandChild7= (Descriptor)(grandChildren.elementAt(6));

            String[] grandChild7AttList = grandChild7.getAttributeList();
            int grandChild7NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild7AttList.length; i++) {
              String attrVal = grandChild7.getAttributeValue(grandChild7AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild7AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("ZIPCOD"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("5"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild7NumAttrs++;
              }
            }

            Descriptor grandChild8= (Descriptor)(grandChildren.elementAt(7));

            String[] grandChild8AttList = grandChild8.getAttributeList();
            int grandChild8NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild8AttList.length; i++) {
              String attrVal = grandChild8.getAttributeValue(grandChild8AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild8AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("CDTLMT"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }
                grandChild8NumAttrs++;
              }
            }

            Descriptor grandChild9= (Descriptor)(grandChildren.elementAt(8));

            String[] grandChild9AttList = grandChild9.getAttributeList();
            int grandChild9NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild9AttList.length; i++) {
              String attrVal = grandChild9.getAttributeValue(grandChild9AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild9Attr: " + grandChild9AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("CHGCOD"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("1"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("0"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild9NumAttrs++;
              }
            }

            Descriptor grandChild10= (Descriptor)(grandChildren.elementAt(9));

            String[] grandChild10AttList = grandChild10.getAttributeList();
            int grandChild10NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild10AttList.length; i++) {
              String attrVal = grandChild10.getAttributeValue(grandChild10AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild10Attr: " + grandChild10AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("BALDUE"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("2"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }
                grandChild10NumAttrs++;
              }
            }
            Descriptor grandChild11= (Descriptor)(grandChildren.elementAt(10));

            String[] grandChild11AttList = grandChild11.getAttributeList();
            int grandChild11NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild11AttList.length; i++) {
              String attrVal = grandChild11.getAttributeValue(grandChild11AttList[i]);
              if (attrVal != null) {
               if (i== 0)
               {
                   if (!attrVal.equals("CDTDUE"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("2"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                }

                grandChild11NumAttrs++;
              }
            }

           Record newRec2 = rfmlDoc2.toRecord("CUSREC");
           if (newRec2.getNumberOfFields() != 11) 
           {
             failed("Bad number of fields returned.");
             return;
           }
           if (( (BigDecimal) newRec2.getField(0)).intValue() != 938472) 
           {
             failed("Bad value returned for first field.");
             return;
           }
           if (!newRec2.getField(1).equals("Henning ")) // or ""?
           {
             failed("Bad value returned for second field.");
             return;
           }
           if (!newRec2.getField(2).equals("G K")) // or ""?
           {
             failed("Bad value returned for third field.");
             return;
           }
           if (!newRec2.getField(3).equals("4859 Elm Ave ")) // or ""?
           {
             failed("Bad value returned for fourth field.");
             return;
           }
           if (!newRec2.getField(4).equals("Dallas")) // or ""?
           {
             failed("Bad value returned for fifth field.");
             return;
           }
           if (!newRec2.getField(5).equals("TX")) // or ""?
           {
             failed("Bad value returned for six field.");
             return;
           }
           if ( ( (BigDecimal) newRec2.getField(6)).intValue() != 75217) // or ""?
           {
             failed("Bad value returned for seventh field.");
             return;
           }
           if ( ( (BigDecimal) newRec2.getField(7)).intValue() != 5000) // or ""?
           {
             failed("Bad value returned for eighth field.");
             return;
           }
           if ( ( (BigDecimal)newRec2.getField(8)).intValue() != 3) // or ""?
           {
             failed("Bad value returned for ninth field.");
             return;
           }
           if ( ( (BigDecimal) newRec2.getField(9)).doubleValue() != 37.00) // or ""?
           {
             failed("Bad value returned for tenth field.");
             return;
           }
           if ( ((BigDecimal)newRec2.getField(10)).doubleValue() != 0.00) // or ""?
           {
             failed("Bad value returned for eleventh field.");
             return;
           }
        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     Test RecordFormatDocument constructor with Record parameter.  Try to create an RFML document
     from a record format that consists of two fields, a DateFieldDescription and a DBCSGraphicFieldDescription.
     Ensure character fields are generated.
     **/
    public void Var005()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {

           RecordFormat recFmt = new RecordFormat("format1");
           DateFieldDescription date1 =
               new DateFieldDescription(new AS400Text(8, 833, systemObject_), "datef");
           date1.setDATFMT("YYMMDD");
           date1.setDATSEP("-");
           DBCSGraphicFieldDescription dbcs1 =
               new DBCSGraphicFieldDescription(new AS400Text(12, 37, systemObject_), "dbcsg");


           recFmt.addFieldDescription(date1);
           recFmt.addFieldDescription(dbcs1);


           Record newRec = new Record(recFmt);
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(newRec);

           Descriptor root = rfmlDoc.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
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
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
               //System.out.println("grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("datef"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

               grandChild1NumAttrs++;
             }
           }           


           Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

           String[] grandChild2AttList = grandChild1.getAttributeList();
           int grandChild2NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild2AttList.length; i++) {
             String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
             if (attrVal != null) {
               //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("dbcsg"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("12"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

               grandChild2NumAttrs++;
             }
           }           



          file1 = new File("RFFile");
          file1.createNewFile();  // Note: This method is new in Java2.
          raFile1 = new RandomAccessFile(file1, "r");
          rfmlDoc.toXml(file1);

           String curdate = "09/05/01";
           String dbcsstr = "DBCSDATA";
           rfmlDoc.setValue("FORMAT1.datef",curdate);
           rfmlDoc.setValue("FORMAT1.dbcsg",dbcsstr);

           Record newRec2 = rfmlDoc.toRecord("FORMAT1");
           if (newRec2.getNumberOfFields() != 2)
           {
             failed("Bad number of fields returned."+raFile1);
             return;
           }
           if (!newRec2.getField(0).equals("09/05/01")) // or ""?
           {
             failed("Bad value returned for first field.");
             return;
           }
           if (!newRec2.getField(1).equals("DBCSDATA    ")) // or ""?
           {
             failed("Bad value returned for second field.");
             return;
           }


        succeeded();

        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }


    /**
     Test RecordFormatDocument constructor with RecordFormat parameter.  Pass an empty RecordFormat, i.e.,   	
     a RecordFormat constructed with default constructor, RecordFormat()
     **/
    public void Var006()
    {
        try
        {
           RecordFormat newRecFmt = new RecordFormat();
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(newRecFmt);
           assertCondition(false, "Did not throw exception but got "+rfmlDoc); 
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.data.XmlException");
        }
    }


    /**
     Test RecordFormatDocument constructor with RecordFormat parameter.  Read in a RecordFormat    
     that contains a number of different fields types, precisions, lengths,
     keys, ccsid, etc. Ensure all attributes are covered with this testcase and the previous.
     Ensure proper node tree generated.
     Note: This variation requires that library RMLIB exist and be populated on the server.  FTP the file "rmlib.savf" from the testcase source directory to the server, and then do a RSTLIB.
     **/

    public void Var007()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {
           // RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.recFmtAllTypes");
           // RecordFormat recFmt = rfmlDoc.toRecordFormat("format1");

           // Create an AS400 object for the AS/400 system that has the file.
           AS400 as400 = new AS400(systemObject_);

           // Create the sequential file object that represents the
           // file on the AS/400.  We use a QSYSObjectPathName object
           // to get the name of the file into the correct format.
           QSYSObjectPathName fileName = new QSYSObjectPathName("RMLIB", "ALLTYPESF", "FILE");

           SequentialFile file = new SequentialFile(as400, fileName.getPath());

           // Let the file object know the format of the records.
           file.setRecordFormat();

           // Open the file for read-only access.  Specify a blocking
           // factor of 10 (the file object will get 10 records when
           // it accesses the AS/400 for data).  Do not use commitment
           // control.
           file.open(SequentialFile.READ_ONLY,
                     10,
                     SequentialFile.COMMIT_LOCK_LEVEL_NONE);


           // Write the first record of the file.
           Record newRec = file.readNext();

           // Create an RFML document based on the record
           RecordFormatDocument rfmlDoc2 = new RecordFormatDocument(newRec.getRecordFormat());

           file1 = new File("emptyFile");
           file1.createNewFile();  // Note: This method is new in Java2.
           raFile1 = new RandomAccessFile(file1, "r");
           rfmlDoc2.toXml(file1);

           Descriptor root = rfmlDoc2.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
           }

           Descriptor child1 = (Descriptor)(children.elementAt(0));
           String[] child1AttList = child1.getAttributeList();

           int child1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<child1AttList.length; i++) {
             String attrVal = child1.getAttributeValue(child1AttList[i]);
             if (attrVal != null) {
               //System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
               if (i==0)
                  if (!attrVal.equals("FORMAT1"))
                  {
                     failed("Bad format name"+raFile1);
                     return; 
                  } 
               child1NumAttrs++;
             }
           }

           enumeration =child1.getChildren();
           Vector grandChildren = new Vector();
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
               // System.out.println("i=" + i + " grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD1"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("float"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 

               grandChild1NumAttrs++;
             }
           }

            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD2"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("float"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));

            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                //System.out.println("i = " + i + " grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD3"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("25"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==6)
               {
                   if (!attrVal.equals("HI"))
                   {
                       failed("Bad init value returned. Value = " + attrVal);
                       return;
                   }
               } 


                grandChild3NumAttrs++;
              }
            }


            Descriptor grandChild4 = (Descriptor)(grandChildren.elementAt(3));

            String[] grandChild4AttList = grandChild4.getAttributeList();
            int grandChild4NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild4AttList.length; i++) {
              String attrVal = grandChild4.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild4Attr: " + grandChild4AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD4"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==6)
               {
                   if (!attrVal.equals("15"))
                   {
                       failed("Bad in init value returned. Value = " + attrVal);
                       return;
                   }
               } 


                grandChild4NumAttrs++;
              }
            }

            Descriptor grandChild5 = (Descriptor)(grandChildren.elementAt(4));

            String[] grandChild5AttList = grandChild5.getAttributeList();
            int grandChild5NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild5AttList.length; i++) {
              String attrVal = grandChild5.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild5Attr: " + grandChild5AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD5"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==6)
               {
                   if (!attrVal.equals("150"))
                   {
                       failed("Bad in init value returned. Value = " + attrVal);
                       return;
                   }
               } 

                grandChild5NumAttrs++;
              }
            }

            Descriptor grandChild6= (Descriptor)(grandChildren.elementAt(5));

            String[] grandChild6AttList = grandChild6.getAttributeList();
            int grandChild6NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild6AttList.length; i++) {
              String attrVal = grandChild6.getAttributeValue(grandChild6AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild6Attr: " + grandChild6AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD6"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==6)
               {
                   if (!attrVal.equals("99"))
                   {
                       failed("Bad in init value returned. Value = " + attrVal);
                       return;
                   }
               } 

                grandChild6NumAttrs++;
              }
            }

            Descriptor grandChild7= (Descriptor)(grandChildren.elementAt(6));

            String[] grandChild7AttList = grandChild7.getAttributeList();
            int grandChild7NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild7AttList.length; i++) {
              String attrVal = grandChild7.getAttributeValue(grandChild7AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild7Attr: " + grandChild7AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD7"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("packed"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("10"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("5"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                } else if (i==6)
                {
                   if (!attrVal.equals("88.888"))
                   {
                       failed("Bad in init value returned. Value = " + attrVal);
                       return;
                   }
               } 


                grandChild7NumAttrs++;
              }
            }

            Descriptor grandChild8= (Descriptor)(grandChildren.elementAt(7));

            String[] grandChild8AttList = grandChild8.getAttributeList();
            int grandChild8NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild8AttList.length; i++) {
              String attrVal = grandChild8.getAttributeValue(grandChild8AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild8Attr: " + grandChild8AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD8"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("22"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("7"))
                   {
                      failed("Bad precision returned. Precision = " + attrVal);
                      return;
                   }
                } else if (i==6)
               {
                   if (!attrVal.equals("22.21"))
                   {
                       failed("Bad in init value returned. Value = " + attrVal);
                       return;
                   }
               } 


                grandChild8NumAttrs++;
              }
            }

            Descriptor grandChild9= (Descriptor)(grandChildren.elementAt(8));

            String[] grandChild9AttList = grandChild9.getAttributeList();
            int grandChild9NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild9AttList.length; i++) {
              String attrVal = grandChild9.getAttributeValue(grandChild9AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild9Attr: " + grandChild9AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("FIELD9"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("byte"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("10"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 
                grandChild9NumAttrs++;
              }
            }

        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test RecordFormatDocument constructor with Record parameter.  Read in a RecordFormat    
     that contains a number a date field.  Ensure this is converted to a char field
     and that value is preserved.  
     Note: This variation requires that library RMLIB exist and be populated on the server.  FTP the file "rmlib.savf" from the testcase source directory to the server, and then do a RSTLIB.
     **/


    public void Var008()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {
           // Create an AS400 object for the AS/400 system that has the file.
           AS400 as400 = new AS400(systemObject_);

           // Create the sequential file object that represents the
           // file on the AS/400.  We use a QSYSObjectPathName object
           // to get the name of the file into the correct format.
           QSYSObjectPathName fileName = new QSYSObjectPathName("RMLIB", "TESTFILEB", "FILE");

           SequentialFile file = new SequentialFile(as400, fileName.getPath());

           // Let the file object know the format of the records.
           file.setRecordFormat();

           // Open the file for read-only access.  Specify a blocking
           // factor of 10 (the file object will get 10 records when
           // it accesses the AS/400 for data).  Do not use commitment
           // control.
           file.open(SequentialFile.READ_ONLY,
                     10,
                     SequentialFile.COMMIT_LOCK_LEVEL_NONE);


           // Write the first record of the file.
           Record newRec = file.readNext();

           // Create an RFML document based on the record
           RecordFormatDocument rfmlDoc2 = new RecordFormatDocument(newRec);

          file1 = new File("emptyFile");
          file1.createNewFile();  // Note: This method is new in Java2.
          raFile1 = new RandomAccessFile(file1, "r");
          rfmlDoc2.toXml(file1);

           Record newRec2 = rfmlDoc2.toRecord("FORMAT1");

           if (newRec2.getNumberOfFields() != 10) 
           {
             failed("Bad number of fields returned."+raFile1);
             return;
           }
           if (!rfmlDoc2.getValue("FORMAT1.FIELD3").equals("Testing                  ")) // or ""?
           {
             failed("Bad value returned for char field.");
             return;
           }
           if (!rfmlDoc2.getValue("FORMAT1.FIELD10").equals("0001-08-01")) // or ""?
           {
             failed("Bad value returned for date field.");
             return;
           }

        succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Test RecordFormatDocument constructor with a Date field a Time field and a DBCS only field.    
     that contains a number a date field.  Ensure this is converted to a char field
     and that value is preserved.  
     **/
    public void Var009()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {
           RecordFormat recFmt =  new RecordFormat("format1");
           DateFieldDescription dateFld = new DateFieldDescription(new AS400Text(10), "field1");
           dateFld.setDFT("2001-08-20");
           TimeFieldDescription timeFld = new TimeFieldDescription(new AS400Text(8), "field2");
           timeFld.setDFT("12:12:12");
           DBCSOnlyFieldDescription dbcsFld = new DBCSOnlyFieldDescription(new AS400Text(10), "field3");
           dbcsFld.setCCSID("65535");

           recFmt.addFieldDescription(dateFld);
           recFmt.addFieldDescription(timeFld);
           recFmt.addFieldDescription(dbcsFld);

           RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

           file1 = new File("emptyFile");
           file1.createNewFile();  // Note: This method is new in Java2.
           raFile1 = new RandomAccessFile(file1, "r");
           rfmlDoc.toXml(file1);

           Descriptor root = rfmlDoc.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
           }

           Descriptor child1 = (Descriptor)(children.elementAt(0));
           String[] child1AttList = child1.getAttributeList();

           int child1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<child1AttList.length; i++) {
             String attrVal = child1.getAttributeValue(child1AttList[i]);
             if (attrVal != null) {
               //System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
               if (i==0)
                  if (!attrVal.equals("FORMAT1"))
                  {
                     failed("Bad format name"+raFile1);
                     return; 
                  } 
               child1NumAttrs++;
             }
           }

           enumeration =child1.getChildren();
           Vector grandChildren = new Vector();
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
               // System.out.println("i=" + i + " grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field1"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("10"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==6)
               {
                   if (!attrVal.equals("2001-08-20"))
                   {
                       failed("Bad init value returned. Value = " + attrVal);
                       return;
                   }
               } 


               grandChild1NumAttrs++;
             }
           }

            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field2"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==6)
               {
                   if (!attrVal.equals("12:12:12"))
                   {
                       failed("Bad init value returned. Value = " + attrVal);
                       return;
                   }
               } 

                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));

            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                //System.out.println("i = " + i + " grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field3"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("10"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } 


                grandChild3NumAttrs++;
              }
            }
           if (!rfmlDoc.getValue("FORMAT1.field1").equals("2001-08-20")) // or ""?
           {
             failed("Bad value returned for date field. Value =" + rfmlDoc.getValue("FORMAT1.field1"));
             return;
           }
           if (!rfmlDoc.getValue("FORMAT1.field2").equals("12:12:12")) // or ""?
           {
             failed("Bad value returned for time field.");
             return;
           }
           if (rfmlDoc.getValue("FORMAT1.field3") != null) 
           {
             failed("Non null value returned for dbcs field.");
             return;
           }
           succeeded();
        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
	    failed(e); 
        }
   }

    /**
     Test count attribute in a simple data queue test with char data.
     **/

    public void Var010()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        // File file1 = null;
        // RandomAccessFile raFile1 = null;

        try
        {
           // Create an AS400 object for the AS/400 system that has the file.
           AS400 as400 = new AS400(systemObject_);

            // Get record format from the rfml document -- contains counts
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.counteasy");
 
            // Create the data queue
            CommandCall cmd = new CommandCall(as400);
            try {
               cmd.run("DLTDTAQ DTAQ(RMLIB/TESTDQ)");
               cmd.run("CRTDTAQ DTAQ(RMLIB/TESTDQ) MAXLEN(100)");
            }
            catch (Exception e)
            {
            }

            // Create a data area that we will use to access data
            DataQueue dataQ = new DataQueue(as400, "/QSYS.LIB/RMLIB.LIB/TESTDQ.DTAQ");

            byte[] byteA = rfmlDoc.toByteArray("format1");

            // Write to the data queue
             dataQ.write(byteA);

           // Read an entry from the queue
             DataQueueEntry dataQE = dataQ.read();

            assertCondition(dataQE.getString().equals("BooBooBooBooBRBRBR"));
        }
        catch (Exception e)
        {
	    System.out.println("Exception = " + e);
	    failed(e); 
        }
   }

    /**
     Test count attribute in a simple data queue test with integer data.
     **/
    public void Var011()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }

        // File file1 = null;
        // RandomAccessFile raFile1 = null;

        try
        {
           // Create an AS400 object for the AS/400 system that has the file.
           AS400 as400 = new AS400(systemObject_);

            // Get record format from the rfml document -- contains counts
            RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.count");

            // Create the data queue
            CommandCall cmd = new CommandCall(as400);
            try {
               cmd.run("DLTDTAQ DTAQ(RMLIB/TESTDQ)");
               cmd.run("CRTDTAQ DTAQ(RMLIB/TESTDQ) MAXLEN(100)");
            }
            catch (Exception e)
            {
            }
 
            // Create a data area that we will use to access data
            DataQueue dataQ = new DataQueue(as400, "/QSYS.LIB/RMLIB.LIB/TESTDQ.DTAQ");
            byte[] byteA = new byte[12];
            BinaryConverter.intToByteArray(3, byteA, 0);
            BinaryConverter.intToByteArray(3, byteA, 4);
            BinaryConverter.intToByteArray(3, byteA, 8); 

            dataQ.write(byteA);

           // Read an entry from the queue
            DataQueueEntry dataQE = dataQ.read();
            byte[] byteA2 = dataQE.getData();
            rfmlDoc.setValues("format1", byteA2);

            int[] index = new int[1];
            index[0] = 0;
            if (rfmlDoc.getIntValue("format1.field1",index) != 3) 
            {
                failed("Bad int value returned.");
                return;
            } 
            index[0] = 1;
            if (rfmlDoc.getIntValue("format1.field1",index) != 3) 
            {
                failed("Bad int value returned.");
                return;
            } 
            index[0] = 2;
            if (rfmlDoc.getIntValue("format1.field1",index) != 3) 
            {
                failed("Bad int value returned.");
                return;
            } 
        succeeded();
        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
	failed(e); 
        }
   }

    /**
     Test count attribute by ensuring ArrayFieldDescriptions are accurately converted to the proper
     count values
    **/
    public void Var012()
    {
        File file1 = null;
        RandomAccessFile raFile1 = null;

        try
        {
         // Create an AS400 object for the AS/400 system that has the file.
         AS400 as400 = new AS400(systemObject_);

         RecordFormat recFmt =  new RecordFormat("format1");
         AS400Array arrayfd = new AS400Array(new AS400Text(5,833,as400), 2);
         ArrayFieldDescription arrayFld = new ArrayFieldDescription(arrayfd, "field1");
         recFmt.addFieldDescription(arrayFld);

         AS400Array arrayfd2 = new AS400Array(new AS400Bin4(), 3);
         ArrayFieldDescription arrayFld2 = new ArrayFieldDescription(arrayfd2, "field2");
         recFmt.addFieldDescription(arrayFld2);

         AS400Array arrayfd4 = new AS400Array(new AS400PackedDecimal(6,1), 6);  
         ArrayFieldDescription arrayFld4 = new ArrayFieldDescription(arrayfd4,"field3");
         recFmt.addFieldDescription(arrayFld4);

         AS400ZonedDecimal zd1= new AS400ZonedDecimal(11,2);
         AS400Array arrayfd5 = new AS400Array(zd1, 8);  
         ArrayFieldDescription arrayFld5 = new ArrayFieldDescription();
         arrayFld5.setDataType(arrayfd5);
         arrayFld5.setFieldName("field4");
         recFmt.addFieldDescription(arrayFld5);

         RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

           file1 = new File("xmlFile");
           file1.createNewFile();  // Note: This method is new in Java2.
           raFile1 = new RandomAccessFile(file1, "r");
           rfmlDoc.toXml(file1);

           Descriptor root = rfmlDoc.getDescriptor();
           String[] rootAttList = root.getAttributeList();
           int rootNumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<rootAttList.length; i++) {
             String attrVal = root.getAttributeValue(rootAttList[i]);
             if (attrVal != null) rootNumAttrs++;
           }
           Enumeration enumeration = root.getChildren();
           Vector children = new Vector();
           int numChildren = 0;
           while (enumeration.hasMoreElements()) {
              Descriptor child = (Descriptor)(enumeration.nextElement());
              children.addElement(child);
              ++numChildren;
           }

           Descriptor child1 = (Descriptor)(children.elementAt(0));
           String[] child1AttList = child1.getAttributeList();

           int child1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<child1AttList.length; i++) {
             String attrVal = child1.getAttributeValue(child1AttList[i]);
             if (attrVal != null) {
               //System.out.println("child1Attr: " + child1AttList[i] + " = " + attrVal);
               if (i==0)
                  if (!attrVal.equals("FORMAT1"))
                  {
                     failed("Bad format name"+raFile1);
                     return; 
                  } 
               child1NumAttrs++;
             }
           }

           enumeration =child1.getChildren();
           Vector grandChildren = new Vector();
           numChildren = 0;
           while (enumeration.hasMoreElements()) {
             Descriptor child = (Descriptor)(enumeration.nextElement());
             grandChildren.addElement(child);
             ++numChildren;
           }

           Descriptor grandChild1 = (Descriptor)(grandChildren.elementAt(0));

           String[] grandChild1AttList = grandChild1.getAttributeList();
           int grandChild1NumAttrs = 0;  // number of non-null attributes
           for (int i=0; i<grandChild1AttList.length; i++) {
             String attrVal = grandChild1.getAttributeValue(grandChild1AttList[i]);
             if (attrVal != null) {
                //System.out.println("i=" + i + " grandChild1Attr: " + grandChild1AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field1"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("char"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("5"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==1)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad count value returned. Value = " + attrVal);
                       return;
                   }
               } else if (i==5)
               {
                   if (!attrVal.equals("833"))
                   {
                       failed("Bad ccsid value returned. Value = " + attrVal);
                       return;
                   }
               } 

              grandChild1NumAttrs++;
             }
           }

            Descriptor grandChild2 = (Descriptor)(grandChildren.elementAt(1));

            String[] grandChild2AttList = grandChild2.getAttributeList();
            int grandChild2NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild2AttList.length; i++) {
              String attrVal = grandChild2.getAttributeValue(grandChild2AttList[i]);
              if (attrVal != null) {
                //System.out.println("grandChild2Attr: " + grandChild2AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field2"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("int"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("4"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==1)
               {
                   if (!attrVal.equals("3"))
                   {
                       failed("Bad count value returned. Value = " + attrVal);
                       return;
                   }
               } 

                grandChild2NumAttrs++;
              }
            }

            Descriptor grandChild3 = (Descriptor)(grandChildren.elementAt(2));

            String[] grandChild3AttList = grandChild3.getAttributeList();
            int grandChild3NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild3AttList.length; i++) {
              String attrVal = grandChild3.getAttributeValue(grandChild3AttList[i]);
              if (attrVal != null) {
                //System.out.println("i = " + i + " grandChild3Attr: " + grandChild3AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field3"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("packed"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==1)
               {
                   if (!attrVal.equals("6"))
                   {
                       failed("Bad count returned. Count = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("1"))
                   {
                       failed("Bad precision returned. Precision = " + attrVal);
                       return;
                   }
               } 
                grandChild3NumAttrs++;
              }
            }

            Descriptor grandChild4 = (Descriptor)(grandChildren.elementAt(3));

            String[] grandChild4AttList = grandChild4.getAttributeList();
            // int grandChild4NumAttrs = 0;  // number of non-null attributes
            for (int i=0; i<grandChild4AttList.length; i++) {
              String attrVal = grandChild4.getAttributeValue(grandChild4AttList[i]);
              if (attrVal != null) {
                //System.out.println("i = " + i + " grandChild4Attr: " + grandChild4AttList[i] + " = " + attrVal);
               if (i== 0)
               {
                   if (!attrVal.equals("field4"))
                   {
                       failed("Bad field name returned. Name = " + attrVal);
                       return;
                   }
               } else if (i==2)
               {
                   if (!attrVal.equals("zoned"))
                   {
                       failed("Bad type returned. Type = " + attrVal);
                       return;
                   }
               } else if (i==3)
               {
                   if (!attrVal.equals("11"))
                   {
                       failed("Bad length returned. Length = " + attrVal);
                       return;
                   }
               } else if (i==1)
               {
                   if (!attrVal.equals("8"))
                   {
                       failed("Bad count returned. Count = " + attrVal);
                       return;
                   }
               } else if (i==4)
               {
                   if (!attrVal.equals("2"))
                   {
                       failed("Bad precision returned. Precision = " + attrVal);
                       return;
                   }
               } 
                grandChild3NumAttrs++;
              }
            }
            succeeded();

        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
        }
   }

    /**
     Test init attribute to see if invalid init attribute is caught
    **/
    public void Var013()
    {

        try
        {

           RecordFormat recFmt =  new RecordFormat("format1");
           PackedDecimalFieldDescription packed =
               new PackedDecimalFieldDescription(new AS400PackedDecimal(7,2), "packed");

           packed.setDFT(new BigDecimal("99.99"));
           recFmt.addFieldDescription(packed);
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

           

           if (rfmlDoc.getDoubleValue("FORMAT1.packed") != 99.99)
              failed("Bad value returned for packed");
           else
              succeeded();


        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
        }
   }

    /**
     Test init attribute to see if invalid init attribute is caught
    **/
    public void Var014()
    {
	if (RMTest.jvmIsSunOrIBM_142_) { notApplicable("JVM is 142"); return; }


        try
        {

           RecordFormatDocument rfmlDoc = new RecordFormatDocument("test.rfml.badinit");
           if (rfmlDoc.getDoubleValue("format1.field1") != 77.9)
              failed("Bad value returned for field1");
           else
               succeeded();        
        }
        catch (Exception e)
        {
	    failed(e); 
        System.out.println("Exception = " + e);
        }
   }


    /**
     Test init attribute to see if invalid init attribute is caught
    **/
    public void Var015()
    {

        try
        {

           RecordFormat recFmt =  new RecordFormat("format1");

           PackedDecimalFieldDescription packed =
               new PackedDecimalFieldDescription(new AS400PackedDecimal(4,0), "packed");

           packed.setDFT(new BigDecimal("99999999"));
           recFmt.addFieldDescription(packed);
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);

           if (rfmlDoc.getDoubleValue("FORMAT1.packed") != 99999999)
              failed("Bad value returned for packed");
           else
              succeeded();
        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
        }
   }


    /**
     Test null values in RecordFormat.  Ensure value set to null properly.
    **/
    public void Var016()
    {

        try
        {


           RecordFormat recFmt =  new RecordFormat("FORMAT1");
           CharacterFieldDescription charFld = new CharacterFieldDescription(new AS400Text(10), "field3");
           charFld.setDFTNull();
           recFmt.addFieldDescription(charFld);
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);
           if (rfmlDoc.getValue("FORMAT1.field3") != null)
               failed("Non null value returned for field3");
           else
               succeeded();
        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
        }
   }

    /**
     Test null values in Record.  Ensure value set to null properly.
    **/
    public void Var017()
    {

        try
        {


           RecordFormat recFmt =  new RecordFormat("FORMAT1");
           CharacterFieldDescription charFld = new CharacterFieldDescription(new AS400Text(10), "field3");
           charFld.setDFTNull();
           recFmt.addFieldDescription(charFld);
           Record newRec = new Record(recFmt);
           newRec.setField("field3", null);
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(newRec);

           if (rfmlDoc.getValue("FORMAT1.field3") != null)
               failed("Non null value returned for field3");
           else
               succeeded();

        }
        catch (Exception e)
        {
        System.out.println("Exception = " + e);
        }
   }

    /**
     Test RecordFormatDocument constructor with Record parameter.  Pass a null Record.
     **/
    public void Var018()
    {
        try
        {
           Record newRec = null;
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(newRec);
           assertCondition(false, "exception not thrown for "+rfmlDoc); 
        }
        catch (Exception e)
        {
          assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Test RecordFormatDocument constructor with null RecordFormat parameter.
     **/
    public void Var019()
    {
        try
        {
           RecordFormat recFmt = null;
           RecordFormatDocument rfmlDoc = new RecordFormatDocument(recFmt);
           assertCondition(false, "exception not thrown for "+rfmlDoc); 
       
        }
        catch (Exception e)
        {
          assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }



}




