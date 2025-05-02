///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMCaching.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.ZonedDecimalFieldDescription;

import test.Testcase;

/**
 *Testcase DDMCaching.  This test class verifies the results of reading and
 *positioning when caching is in effect.  The following methods are verified:
 *<ul>
 *<li>AS400File.positionCursorAfterLast()
 *<li>AS400File.positionCursorBeforeFirst()
 *<li>AS400File.positionCursorToFirst()
 *<li>AS400File.positionCursorToLast()
 *<li>AS400File.positionCursorToNext()
 *<li>AS400File.positionCursorToPrevious()
 *<li>AS400File.read()
 *<li>AS400File.readFirst()
 *<li>AS400File.readLast()
 *<li>AS400File.readNext()
 *<li>AS400File.readPrevious()
 *<li>AS400File.readFirst()
 *<li>SequentialFile.positionCursor(int)
 *<li>SequentialFile.positionCursorAfter(int)
 *<li>SequentialFile.positionCursorBefore(int)
 *<li>SequentialFile.read(int)
 *<li>SequentialFile.readAfter(int)
 *<li>SequentialFile.readBefore(int)
 *<li>KeyedFile.positionCursor(Object[], int)
 *<li>KeyedFile.positionCursorAfter(Object[], int)
 *<li>KeyedFile.positionCursorBefore(Object[], int)
 *<li>KeyedFile.readNextEqual()
 *<li>KeyedFile.readPreviousEqual()
 *<li>KeyedFile.readNextEqual(Object[])
 *<li>KeyedFile.readPreviousEqual(Object[])
 *</ul>
**/
public class DDMCaching extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMCaching";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  long start;
  long time;
  String blanks10 = "          ";
  int bf_;
  String fileName_ = null;
  String fileName2_ = null;
  String testLib_ = null;
  AS400 pwrSys_;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMCaching(AS400            systemObject,
                      Vector<String> variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      AS400 pwrSys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMCaching", 70,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
    pwrSys_ = pwrSys;
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMCaching(AS400            systemObject,
                      Vector<String> variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      int              blockingFactor,
                      AS400 pwrSys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, (blockingFactor == 1)? "DDMCaching" : "DDMCaching" + String.valueOf(blockingFactor), 70,
          variationsToRun, runMode, fileOutputStream);
    bf_ = blockingFactor;
    testLib_ = testLib;
    pwrSys_ = pwrSys;
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Connect to the AS/400 for record the record level access service
//    try
//    {
//      systemObject_.connectService(AS400.RECORDACCESS);
//    }
//    catch(Exception e)
//    {
//      System.out.println("Unable to connect to the AS/400");
//      e.printStackTrace();
//      return;
//    }

   // Do any necessary setup work for the variations
   try
   {
     setup();
   }
   catch (Exception e)
   {
     // Testcase setup did not complete successfully
     System.out.println("Unable to complete setup; variations not run");
     return;
   }
//   System.gc();

   if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
//    System.gc();
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
//    System.gc();

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }
//    System.gc();

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }
//    System.gc();

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }
//    System.gc();

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("35")) &&
        runMode_ != ATTENDED)
    {
      setVariation(35);
      Var035();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("36")) &&
        runMode_ != ATTENDED)
    {
      setVariation(36);
      Var036();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("37")) &&
        runMode_ != ATTENDED)
    {
      setVariation(37);
      Var037();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("38")) &&
        runMode_ != ATTENDED)
    {
      setVariation(38);
      Var038();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("39")) &&
        runMode_ != ATTENDED)
    {
      setVariation(39);
      Var039();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("40")) &&
        runMode_ != ATTENDED)
    {
      setVariation(40);
      Var040();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("41")) &&
        runMode_ != ATTENDED)
    {
      setVariation(41);
      Var041();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("42")) &&
        runMode_ != ATTENDED)
    {
      setVariation(42);
      Var042();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("43")) &&
        runMode_ != ATTENDED)
    {
      setVariation(43);
      Var043();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("44")) &&
        runMode_ != ATTENDED)
    {
      setVariation(44);
      Var044();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("45")) &&
        runMode_ != ATTENDED)
    {
      setVariation(45);
      Var045();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("46")) &&
        runMode_ != ATTENDED)
    {
      setVariation(46);
      Var046();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("47")) &&
        runMode_ != ATTENDED)
    {
      setVariation(47);
      Var047();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("48")) &&
        runMode_ != ATTENDED)
    {
      setVariation(48);
      Var048();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("49")) &&
        runMode_ != ATTENDED)
    {
      setVariation(49);
      Var049();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("50")) &&
        runMode_ != ATTENDED)
    {
      setVariation(50);
      Var050();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("51")) &&
        runMode_ != ATTENDED)
    {
      setVariation(51);
      Var051();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("52")) &&
        runMode_ != ATTENDED)
    {
      setVariation(52);
      Var052();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("53")) &&
        runMode_ != ATTENDED)
    {
      setVariation(53);
      Var053();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("54")) &&
        runMode_ != ATTENDED)
    {
      setVariation(54);
      Var054();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("55")) &&
        runMode_ != ATTENDED)
    {
      setVariation(55);
      Var055();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("56")) &&
        runMode_ != ATTENDED)
    {
      setVariation(56);
      Var056();
    }
    // System.gc();

    if ((allVariations || variationsToRun_.contains("57")) &&
        runMode_ != ATTENDED)
    {
      setVariation(57);
      Var057();
    }
    if ((allVariations || variationsToRun_.contains("58")) &&
        runMode_ != ATTENDED)
    {
      setVariation(58);
      Var058();
    }

    if ((allVariations || variationsToRun_.contains("59")) &&
        runMode_ != ATTENDED)
    {
      setVariation(59);
      Var059();
    }

    if ((allVariations || variationsToRun_.contains("60")) &&
        runMode_ != ATTENDED)
    {
      setVariation(60);
      Var060();
    }

    if ((allVariations || variationsToRun_.contains("61")) &&
        runMode_ != ATTENDED)
    {
      setVariation(61);
      Var061();
    }

    if ((allVariations || variationsToRun_.contains("62")) &&
        runMode_ != ATTENDED)
    {
      setVariation(62);
      Var062();
    }

    if ((allVariations || variationsToRun_.contains("63")) &&
        runMode_ != ATTENDED)
    {
      setVariation(63);
      Var063();
    }

    if ((allVariations || variationsToRun_.contains("64")) &&
        runMode_ != ATTENDED)
    {
      setVariation(64);
      Var064();
    }

    if ((allVariations || variationsToRun_.contains("65")) &&
        runMode_ != ATTENDED)
    {
      setVariation(65);
      Var065();
    }

    if ((allVariations || variationsToRun_.contains("66")) &&
        runMode_ != ATTENDED)                         
    {
      setVariation(66);
      Var066();
    }

    if ((allVariations || variationsToRun_.contains("67")) &&
        runMode_ != ATTENDED)
    {
      setVariation(67);
      Var067();
    }

    if ((allVariations || variationsToRun_.contains("68")) &&
        runMode_ != ATTENDED)
    {
      setVariation(68);
      Var068();
    }

    if ((allVariations || variationsToRun_.contains("69")) &&
        runMode_ != ATTENDED)
    {
      setVariation(69);
      Var069();
    }

    if ((allVariations || variationsToRun_.contains("70")) &&
        runMode_ != ATTENDED)
    {
      setVariation(70);
      Var070();
    }

    // System.gc();

    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to complete cleanup.");
    }
    // System.gc();

    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
    // System.gc();
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
protected void setup()
  throws Exception
{
  try
  {
    Record[] dupKeyRecs_;
    Record[] records_;
    //Record[] recordsByKey_;
    // Record[] recordsByKey2_;
    // Delete and recreate test library
    CommandCall c = new CommandCall(pwrSys_);
    deleteLibrary(c, testLib_);
    c.run("QSYS/CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
    AS400Message[] msgs = c.getMessageList();
    if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
    {
      for (int i = 0; i < msgs.length; ++i)
      {
        System.out.println(msgs[i]);
      }
      throw new Exception("");
    }
    // Create the necessary files; note fileName_ is set here and is used
    // in each variation.
    fileName_ = "/QSYS.LIB/" + testLib_ + ".LIB/CACHING.FILE/%FILE%.MBR";
    fileName2_ = "/QSYS.LIB/" + testLib_ + ".LIB/CACHING2.FILE/%FILE%.MBR";
    SequentialFile f1 = new SequentialFile(systemObject_, fileName_);
    f1.create(new DDMFormatCaching(systemObject_), null);
    SequentialFile f2 = new SequentialFile(systemObject_, fileName2_);
    f2.create(new DDMFormatCaching(systemObject_), null);

    // Create an array of records to write to the f1
    records_ = new Record[500];
    dupKeyRecs_ = new Record[500];
    String field0 = null;
    RecordFormat rf = f1.getRecordFormat();
    for (short i = 0; i < 500; ++i)
    {
      records_[i] = rf.getNewRecord();
      field0 = "Record " + String.valueOf(i) + "   ";
      records_[i].setField(0, field0.substring(0, 10));
      records_[i].setField(1, new Integer(i));
      records_[i].setField(2, new BigDecimal(String.valueOf(i) + ".00000"));
      records_[i].setField(3, new BigDecimal(String.valueOf(i) + ".00000"));
      dupKeyRecs_[i] = rf.getNewRecord();
      field0 = "Record " + String.valueOf(i) + "   ";
      dupKeyRecs_[i].setField(0, field0.substring(0, 10));
      dupKeyRecs_[i].setField(1, new Integer(i));
      dupKeyRecs_[i].setField(2, new BigDecimal(String.valueOf(i) + ".11111"));
      dupKeyRecs_[i].setField(3, new BigDecimal(String.valueOf(i) + ".00000"));
    }

    // Populate f1
    f1.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f1.write(records_);
    f1.close();
    f2.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f2.write(records_);
    f2.write(dupKeyRecs_);
    f2.close();
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    throw e;
  }
}

/**
 @exception  Exception  If an exception occurs.
 **/
protected void cleanup()
  throws Exception
{
  try
  {
    // Delete the files created during setup()
    SequentialFile f1 = new SequentialFile(pwrSys_, "/QSYS.LIB/" + testLib_ + ".LIB/CACHING.FILE");
    f1.delete();
    f1.close(); 
  }
  catch(Exception e)
  {
    System.out.println("Cleanup unsuccessful. Clear library " + testLib_ + " if it exists: CLRLIB " + testLib_);
    e.printStackTrace(output_);
    throw e;
  }
}


  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>readFirst()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[0]).
   *</ul>
  **/
  public void Var001()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.readFirst();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>readFirst() when the first record is in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[0]).
   *</ul>
  **/
  public void Var002()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst(); // Causes cache to contain the first record
      file.readNext(); // The next record will be in the cache
      Record r = file.readFirst(); // First record should still be in the cache
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>readLast()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[499]).
   *</ul>
  **/
  public void Var003()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.readLast();
      if (r == null || !r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>readLast() when the last record is in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[499]).
   *</ul>
  **/
  public void Var004()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast(); // Causes cache to contain the last record
      file.readPrevious(); // The previous record will be in the cache
      Record r = file.readLast(); // Last record should still be in the cache
      if (r == null || !r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>readNext() through the entire file.  Note that the
   *cache will not be refreshed in the case where bf_ is greater than or
   *equal to the number of records in the file (500).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned for each readNext().
   *</ul>
  **/
  public void Var005()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.readNext();
      int i = 0;
      while (r != null && i < 500)
      {
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 59  59 59.00000 59.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 141 141 141.00000 141.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 378 378 378.00000 378.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 499 499 499.00000 499.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }

        r = file.readNext();
        ++i;
      }
      if (r == null && i != 500)
      {
        failed("null record returned on iteration: " + String.valueOf(i));
        file.close();
        return;
      }
      if (r != null && i == 500)
      {
        failed("null record not returned on last readNext");
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>readPrevious() through the entire file.  Note that the
   *cache will not be refreshed in the case where bf_ is greater than or
   *equal to the number of records in the file (500).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned for each readPrevious().
   *</ul>
  **/
  public void Var006()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      Record r = file.readPrevious();
      int i = 499;
      while (r != null && i >= 0)
      {
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 59  59 59.00000 59.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 141 141 141.00000 141.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 378 378 378.00000 378.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 499 499 499.00000 499.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        r = file.readPrevious();
        --i;
      }
      if (r == null && i != -1)
      {
        failed("null record returned on i = " + String.valueOf(i));
        file.close();
        return;
      }
      if (r != null && i == -1)
      {
        failed("null record not returned on last readPrevious");
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>Verify read() after readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[0]).
   *</ul>
  **/
  public void Var007()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readNext();
      Record r = file.read();
      if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>Verify read() after readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[0]).
   *</ul>
  **/
  public void Var008()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readNext();
      file.readNext();
      file.readPrevious();
      Record r = file.read();
      if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>Verify read() after readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[0]).
   *</ul>
  **/
  public void Var009()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst();
      Record r = file.read();
      if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>SequentialFile, reading sequentially
   *<li>Verify read() after readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (records_[499]).
   *</ul>
  **/
  public void Var010()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast();
      Record r = file.read();
      if (!r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>readFirst()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[0]).
   *</ul>
  **/
  public void Var011()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.readFirst();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>readFirst() when the first record is in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[0]).
   *</ul>
  **/
  public void Var012()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst(); // Causes cache to contain the first record
      file.readNext(); // The next record will be in the cache
      Record r = file.readFirst(); // First record should still be in the cache
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>readLast()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[499]).
   *</ul>
  **/
  public void Var013()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.readLast();
      if (r == null || !r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>readLast() when the last record is in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[499]).
   *</ul>
  **/
  public void Var014()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast(); // Causes cache to contain the last record
      file.readPrevious(); // The previous record will be in the cache
      Record r = file.readLast(); // Last record should still be in the cache
      if (r == null || !r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>readNext() through the entire file.  Note that the
   *cache will not be refreshed in the case where bf_ is greater than or
   *equal to the number of records in the file (500).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned for each readNext().
   *</ul>
  **/
  public void Var015()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = file.readNext();
      int i = 0;
      while (r != null && i < 500)
      {
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 151 151 151.00000 151.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 188 188 188.00000 188.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 225 225 225.00000 225.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 439 439 439.00000 439.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        r = file.readNext();
        ++i;
      }
      if (r == null && i != 500)
      {
        failed("null record returned on iteration: " + String.valueOf(i));
        file.close();
        return;
      }
      if (r != null && i == 500)
      {
        failed("null record not returned on last readNext");
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>readPrevious() through the entire file.  Note that the
   *cache will not be refreshed in the case where bf_ is greater than or
   *equal to the number of records in the file (500).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned for each readPrevious().
   *</ul>
  **/
  public void Var016()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      Record r = file.readPrevious();
      int i = 499;
      while (r != null && i >= 0)
      {
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 151 151 151.00000 151.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 188 188 188.00000 188.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 225 225 225.00000 225.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 439 439 439.00000 439.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        r = file.readPrevious();
        --i;
      }
      if (r == null && i != -1)
      {
        failed("null record returned on i = " + String.valueOf(i));
        file.close();
        return;
      }
      if (r != null && i == -1)
      {
        failed("null record not returned on last readPrevious");
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>Verify read() after readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[0]).
   *</ul>
  **/
  public void Var017()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readNext();
      Record r = file.read();
      if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>Verify read() after readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[0]).
   *</ul>
  **/
  public void Var018()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readNext();
      file.readNext();
      file.readPrevious();
      Record r = file.read();
      if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>Verify read() after readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[0]).
   *</ul>
  **/
  public void Var019()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst();
      Record r = file.read();
      if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of read with caching on.
   *<ul>
   *<li>KeyedFile, reading sequentially
   *<li>Verify read() after readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned (recordsByKey_[499]).
   *</ul>
  **/
  public void Var020()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast();
      Record r = file.read();
      if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        failed("Wrong record returned: " + r.toString());
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorBeforeFirst(), first record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readNext will
   *result in records_[0] being returned.
   *</ul>
  **/
  public void Var021()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBeforeFirst();
      Record r = file.readNext();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorBeforeFirst() when the first record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readNext will
   *result in records_[0] being returned.
   *</ul>
  **/
  public void Var022()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst();
      file.readNext();
      file.positionCursorBeforeFirst(); // First record should be in the cache
      Record r = file.readNext();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorAfterLast(), last record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readPrevious will
   *result in records_[499] being returned.
   *</ul>
  **/
  public void Var023()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      Record r = file.readPrevious();
      if (r == null || !r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorAfterLast() when the last record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readPrevious will
   *result in records_[499] being returned.
   *</ul>
  **/
  public void Var024()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast();
      file.readPrevious();
      file.positionCursorAfterLast(); // Last record should be in the cache
      Record r = file.readPrevious();
      if (r == null || !r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorToFirst(), first record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  read will
   *result in records_[0] being returned.
   *</ul>
  **/
  public void Var025()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorToFirst() when the first record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  read will
   *result in records_[0] being returned.
   *</ul>
  **/
  public void Var026()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst();
      file.readNext();
      file.positionCursorToFirst(); // First record should be in the cache
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorToLast(), last record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  read will
   *result in records_[499] being returned.
   *</ul>
  **/
  public void Var027()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorToLast() when the last record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readPrevious will
   *result in records_[499] being returned.
   *</ul>
  **/
  public void Var028()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast();
      file.readPrevious();
      file.positionCursorToLast(); // Last record should be in the cache
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 499 499 499.00000 499.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorToNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the positionCursorToNext().
   *</ul>
  **/
  public void Var029()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = null;
      for (int i = 0; i < 500; ++i)
      {
        file.positionCursorToNext();
        r = file.read();
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 59  59 59.00000 59.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 141 141 141.00000 141.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 378 378 378.00000 378.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 499 499 499.00000 499.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>SequentialFile, positioning sequentially
   *<li>positionCursorToPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the positionCursorToPrevious().
   *</ul>
  **/
  public void Var030()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = null;
      for (int i = 499; i <= 0; --i)
      {
        file.positionCursorToPrevious();
        r = file.read();
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 59  59 59.00000 59.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 141 141 141.00000 141.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 378 378 378.00000 378.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 499 499 499.00000 499.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorBeforeFirst(), first record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readNext will
   *result in recordsByKey_[0] being returned.
   *</ul>
  **/
  public void Var031()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBeforeFirst();
      Record r = file.readNext();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorBeforeFirst() when the first record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readNext will
   *result in recordsByKey_[0] being returned.
   *</ul>
  **/
  public void Var032()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst();
      file.readNext();
      file.positionCursorBeforeFirst(); // First record should be in the cache
      Record r = file.readNext();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorAfterLast(), last record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readPrevious will
   *result in recordsByKey_[499] being returned.
   *</ul>
  **/
  public void Var033()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      Record r = file.readPrevious();
      if (r == null || !r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorAfterLast() when the last record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readPrevious will
   *result in recordsByKey_[499] being returned.
   *</ul>
  **/
  public void Var034()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast();
      file.readPrevious();
      file.positionCursorAfterLast(); // Last record should be in the cache
      Record r = file.readPrevious();
      if (r == null || !r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorToFirst(), first record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  read will
   *result in recordsByKey_[0] being returned.
   *</ul>
  **/
  public void Var035()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorToFirst() when the first record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  read will
   *result in recordsByKey_[0] being returned.
   *</ul>
  **/
  public void Var036()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readFirst();
      file.readNext();
      file.positionCursorToFirst(); // First record should be in the cache
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 0   0 0.00000 0.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorToLast(), last record not in the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  read will
   *result in recordsByKey_[499] being returned.
   *</ul>
  **/
  public void Var037()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorToLast() when the last record is in
   *the cache.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  readPrevious will
   *result in recordsByKey_[499] being returned.
   *</ul>
  **/
  public void Var038()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.readLast();
      file.readPrevious();
      file.positionCursorToLast(); // Last record should be in the cache
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        String rstr = (r == null)? "null" : r.toString();
        failed("Wrong record returned: " + rstr);
        file.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorToNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the positionCursorToNext().
   *</ul>
  **/
  public void Var039()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = null;
      for (int i = 0; i < 500; ++i)
      {
        file.positionCursorToNext();
        r = file.read();
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 151 151 151.00000 151.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 188 188 188.00000 188.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 225 225 225.00000 225.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 439 439 439.00000 439.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of positioning with caching on.
   *<ul>
   *<li>KeyedFile, positioning sequentially
   *<li>positionCursorToPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the positionCursorToPrevious().
   *</ul>
  **/
  public void Var040()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record r = null;
      for (int i = 499; i <= 0; --i)
      {
        file.positionCursorToPrevious();
        r = file.read();
        // Randomly check records
        if (i == 0)
        {
          if (!r.toString().equals("Record 0   0 0.00000 0.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 59)
        {
          if (!r.toString().equals("Record 151 151 151.00000 151.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 99)
        {
          if (!r.toString().equals("Record 188 188 188.00000 188.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 141)
        {
          if (!r.toString().equals("Record 188 188 188.00000 188.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 378)
        {
          if (!r.toString().equals("Record 439 439 439.00000 439.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
        else if (i == 499)
        {
          if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
          {
            failed("Wrong record returned: " + r.toString() + "\nIteration: " + String.valueOf(i));
            file.close();
            return;
          }
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>positionCursorToNext(), positionCursorToPrevious
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var041()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.positionCursorToNext();
      file.positionCursorToPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>readNext(), readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var042()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.readNext();
      file.readPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>positionCursorToPrevious(), positionCursorToNext
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var043()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.positionCursorToPrevious();
      file.positionCursorToNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>readPrevious(), readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var044()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.readPrevious();
      file.readNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>positionCursorToNext(), readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var045()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.positionCursorToNext();
      file.readPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>readNext(), positionCursorToPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var046()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.readNext();
      file.positionCursorToPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>positionCursorToPrevious(), readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var047()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.positionCursorToPrevious();
      file.readNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>SequentialFile, reversing directions
   *<li>readPrevious(), positionCursorToNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var048()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.read(5); // Get into the file
      file.readPrevious();
      file.positionCursorToNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 4   4 4.00000 4.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToNext(), positionCursorToPrevious
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var049()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.positionCursorToNext();
      file.positionCursorToPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readNext(), readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var050()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.readNext();
      file.readPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToPrevious(), positionCursorToNext
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var051()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.positionCursorToPrevious();
      file.positionCursorToNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readPrevious(), readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var052()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.readPrevious();
      file.readNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToNext(), readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var053()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.positionCursorToNext();
      file.readPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readNext(), positionCursorToPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var054()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.readNext();
      file.positionCursorToPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToPrevious(), readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var055()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.positionCursorToPrevious();
      file.readNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readPrevious(), positionCursorToNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var056()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};
      file.read(key); // Get into the file
      file.readPrevious();
      file.positionCursorToNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul>
   *<li>Position to record and read the next equal record
   *</ul>
   *Expected results:
   *<ul compact>
   *<liThe correct record will be returned.
   *</ul>
  **/
  public void Var057()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName2_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = {"Record 10 ", new Integer(10), new BigDecimal("10.00000")};
      file.read(key); // Get into the file
      Record r = file.readNextEqual();
      if (r == null || !r.toString().equals("Record 10  10 10.11111 10.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of readNext().
   *<ul>
   *<li>Do enough readNext()s to get past the last record in the cache, then
        do a readFirst() followed be a readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<liThe correct record will be returned.
   *</ul>
  **/
  public void Var058()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName2_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      for (int i = 0; (i < bf_ + 3) && (file.readNext() != null); ++i)
      {
      }
      file.readFirst();
      Record r = file.readNext();
      if (!r.toString().equals("Record 0   0 0.11111 0.00000"))
      {
        failed("Wrong record returned after readNext(): " + r.toString());
        file.close();
        return;
      }
      file.readLast();
      r = file.readPrevious();
      if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        failed("Wrong record returned after readPrevious: " + r.toString());
        file.close();
        return;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      try
      {
        file.close();
      }
      catch(Exception e1){}
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPrevious().
   *<ul>
   *<li>Do enough readPrevious()s to get past the last record in the cache, then
        do a readLast() followed be a readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<liThe correct record will be returned.
   *</ul>
  **/
  public void Var059()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName2_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      for (int i = 0; (i < bf_ + 3) && (file.readPrevious() != null); ++i)
      {
      }
      file.readLast();
      Record r = file.readPrevious();
      if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        failed("Wrong record returned after readPrevious: " + r.toString());
        file.close();
        return;
      }
      file.readFirst();
      r = file.readNext();
      if (!r.toString().equals("Record 0   0 0.11111 0.00000"))
      {
        failed("Wrong record returned readNext: " + r.toString());
        file.close();
        return;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      try
      {
        file.close();
      }
      catch(Exception e1){}
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNext().
   *<ul>
   *<li>Do enough readNext()s to get past the last record in the cache,         do a readFirst() followed be a readNext() then a readLast() followed
        by a readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<liNull will be returned.
   *</ul>
  **/
  public void Var060()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName2_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      for (int i = 0; (i < bf_ + 3) && (file.readNext() != null); ++i)
      {
      }
      file.readFirst();
      Record r = file.readNext();
      if (!r.toString().equals("Record 0   0 0.11111 0.00000"))
      {
        failed("Wrong record returned after readNext: " + r.toString());
        file.close();
        return;
      }
      file.readLast();
      r = file.readNext();
      if (r != null)
      {
        failed("Null not returned: " + r.toString());
        file.close();
        return;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      try
      {
        file.close();
      }
      catch(Exception e1){}
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPrevious().
   *<ul>
   *<li>Do enough readPrevious()s to get past the last record in the cache,         do a readLast() followed be a readPrevious() then a readFirst followed
      by a readPrevious.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Null will be returned.
   *</ul>
  **/
  public void Var061()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName2_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      for (int i = 0; (i < bf_ + 3) && (file.readPrevious() != null); ++i)
      {
      }
      file.readLast();
      Record r = file.readPrevious();
      if (!r.toString().equals("Record 99  99 99.00000 99.00000"))
      {
        failed("Wrong record returned after readPrevious: " + r.toString());
        file.close();
        return;
      }
      file.readFirst();
      r = file.readPrevious();
      if (r != null)
      {
        failed("Null not returned: " + r.toString());
        file.close();
        return;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      try
      {
        file.close();
      }
      catch(Exception e1){}
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToNext(), positionCursorToPrevious
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var062()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.positionCursorToNext();
      file.positionCursorToPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readNext(), readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var063()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.readNext();
      file.readPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToPrevious(), positionCursorToNext
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var064()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.positionCursorToPrevious();
      file.positionCursorToNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readPrevious(), readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var065()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.readPrevious();
      file.readNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToNext(), readPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var066()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.positionCursorToNext();
      file.readPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readNext(), positionCursorToPrevious()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var067()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.readNext();
      file.positionCursorToPrevious();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>positionCursorToPrevious(), readNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var068()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.positionCursorToPrevious();
      file.readNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of reversing directions with caching on.
   *<ul>
   *<li>KeyedFile, reversing directions
   *<li>readPrevious(), positionCursorToNext()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The cursor will be positioned correctly.  The correct record will
   *be returned for each read() performed after the direction change.
   *</ul>
  **/
  public void Var069()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 102", new Integer(102), new BigDecimal("102.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 102"), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(102)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("102.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      file.readPrevious();
      file.positionCursorToNext();
      Record r = file.read();
      if (r == null || !r.toString().equals("Record 102 102 102.00000 102.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul>
   *<li>Position to record and read the next equal record
   *</ul>
   *Expected results:
   *<ul compact>
   *<liThe correct record will be returned.
   *</ul>
  **/
  public void Var070()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_, fileName2_);
      file.setRecordFormat(new DDMFormatCaching(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = {"Record 10 ", new Integer(10), new BigDecimal("10.00000")};

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 10 "), 0, 10);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(10)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15,5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("10.00000")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      file.read(key, 3); // Get into the file
      Record r = file.readNextEqual();
      if (r == null || !r.toString().equals("Record 10  10 10.11111 10.00000"))
      {
        if (r != null)
        {
          failed("Wrong record returned: " + r.toString());
        }
        else
        {
          failed("Null record returned");
        }
        file.close();
        return;
      }
   }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.\n");
      return;
    }
    try
    {
      file.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close a file");
      return;
    }

    succeeded();
  }

}


class DDMFormatCaching extends RecordFormat
{
  private static final long serialVersionUID = -8744990032319910277L;

  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMCaching";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  static AS400Bin4 bin = new AS400Bin4();
  static AS400ZonedDecimal zon = new AS400ZonedDecimal(15,5);
  static AS400PackedDecimal pac = new AS400PackedDecimal(15,5);

  DDMFormatCaching(AS400 sys)
  {
    super("FMT1");
    addFieldDescription(new CharacterFieldDescription(new AS400Text(10, sys.getCcsid(), sys), "Field1"));
    addFieldDescription(new BinaryFieldDescription(bin, "Field2"));
    addFieldDescription(new ZonedDecimalFieldDescription(zon, "Field3"));
    addFieldDescription(new PackedDecimalFieldDescription(pac, "Field5"));
    addKeyFieldDescription(0);
    addKeyFieldDescription(1);
    addKeyFieldDescription(3);
  }
}
