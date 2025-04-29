///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP3666842.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMP3666842.  Verify fix for P3666842.
**/
public class DDMP3666842 extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP3666842";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP3666842(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP3666842", 3,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

  public KeyedFile createAndPopulate(boolean varLen) throws AS400Exception,Exception
  {
      KeyedFile myFile = new KeyedFile(systemObject_, "/QSYS.LIB/QGPL.LIB/DDMFIX.FILE/%FILE%.MBR");

      RecordFormat rf = new RecordFormat("DDMTEST");
      CharacterFieldDescription charField = new CharacterFieldDescription(new AS400Text(10,37,systemObject_), "CHAR");

      if (varLen)
          charField.setVariableLength(true);

      rf.addFieldDescription(charField);      
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "INT"));
      rf.addKeyFieldDescription("CHAR");
      rf.addKeyFieldDescription("INT");
      myFile.setRecordFormat(rf);

      try {                  
          myFile.create(rf, "Just a test");
          myFile.open(AS400File.WRITE_ONLY, 50, AS400File.COMMIT_LOCK_LEVEL_NONE);

          Record[] records = new Record[2];
          int i=0;
          records[i] = rf.getNewRecord();
          records[i].setField("CHAR", "test");
          records[i++].setField("INT", new Integer(22));

          records[i] = rf.getNewRecord();
          records[i].setField("CHAR", "test1");
          records[i++].setField("INT", new Integer(23));

          myFile.write(records);
          myFile.close();
      }
      catch (Exception e) {
          throw e;
      }

      return myFile;
  }


  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    systemObject_.disconnectAllServices();
  }

  /**
   *Verify SequentialFile(AS400, String) constructor.
   *<ul compact>
   *<li>Specifying no member: /qsys.lib/" + testLib_ + ".lib/file2.file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() returns: /qsys.lib/" + testLib_ + ".lib/file2.file
   *<li>getFileName() returns: FILE2
   *<li>getMemberName() returns: *FIRST
   *<li>getSystem() returns: the AS400 object that was passed in
   *</ul>
  **/
  public void Var001()
  {
    SequentialFile f = null;
    try
    {
      RecordFormat rf = new RecordFormat("F1");
      rf.addFieldDescription(new FloatFieldDescription(new AS400Float8(), "FLOAT_D"));
      rf.addFieldDescription(new FloatFieldDescription(new AS400Float4(), "FLOAT_S"));
      ((FloatFieldDescription)rf.getFieldDescription(0)).setDecimalPositions(2);
      ((FloatFieldDescription)rf.getFieldDescription(0)).setLength(7);
      ((FloatFieldDescription)rf.getFieldDescription(0)).setFLTPCN("*DOUBLE");
      ((FloatFieldDescription)rf.getFieldDescription(1)).setDecimalPositions(2);
      ((FloatFieldDescription)rf.getFieldDescription(1)).setLength(7);

      f = new SequentialFile(systemObject_, "/QSYS.LIB/QGPL.LIB/DDMFIX.FILE/%FILE%.MBR");
      f.create(rf, null);

      RecordFormat rf2 = new AS400FileRecordDescription(f.getSystem(), f.getPath()).retrieveRecordFormat()[0];
      if (!(rf2.getFieldDescription(0).getDataType() instanceof AS400Float8))
      {
        failed("data type not AS400Float8: " + rf2.getFieldDescription(0).getDataType().getClass().getName());
      }
      else
      {
        succeeded();
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
    try
    {
      f.delete();
    }
    catch(Exception x){}
  }

  /*
   * Case: A variable length field key is used to find a record.
   * Expected result: The correct record is found.
   */
  public void Var002()
  {
    try {
        KeyedFile testFile = createAndPopulate(true);
        testFile.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

        Object[] key = new Object[2];
        key[0] = "test";
        key[1] = new Integer(22);

        Record keyedRecord = testFile.read(key);
        if (keyedRecord != null)
            succeeded();
        else
            failed("Record not found");
        try {
            testFile.close();
            testFile.delete();
        }
        catch (Exception e) { }
    }
    catch (Exception e) {
        failed(e, "Unexpected exception");
    }
  }

  /*
   * Case: A fixed length field key is used to find a record.
   * Expected result: The correct record is found.
   */
  public void Var003()
  {
    try
    {
        KeyedFile testFile = createAndPopulate(false);
        testFile.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

        Object[] key = new Object[2];
        key[0] = "test";
        key[1] = new Integer(22);

        //CRS: Since the field is fixed length, we need to pad the key to be the length of the field
        while (((String)key[0]).length() < testFile.getRecordFormat().getKeyFieldDescription(0).getLength())
          key[0] = ((String)key[0]) + " ";
          
        Record keyedRecord = testFile.read(key);
        if (keyedRecord != null)
            succeeded();
        else
            failed("Record not found");
        try {
            testFile.close();
            testFile.delete();
        }
        catch (Exception e) {}
    }
    catch (Exception e) {
        failed(e, "Unexpected exception");
    }
  }

}
