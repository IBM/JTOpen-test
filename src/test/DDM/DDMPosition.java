///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMPosition.java
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
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.Record;

import com.ibm.as400.access.AS400Text;
import java.io.ByteArrayOutputStream;

/**
 * Testcase DDMPosition. Verify valid and invalid usages of the positionCursor
 * methods for AS400File, KeyedFile and SequentialFile.
 * <ul compact>
 * <li>AS400File.positionCursorAfterLast()
 * <li>AS400File.positionCursorBeforeFirst()
 * <li>AS400File.positionCursorToFirst()
 * <li>AS400File.positionCursorToLast()
 * <li>AS400File.positionCursorToNext()
 * <li>AS400File.positionCursorToPrevious()
 * <li>KeyedFile.positionCursor(key)
 * <li>KeyedFile.positionCursor(key, searchType)
 * <li>KeyedFile.positionCursorAfter(key)
 * <li>KeyedFile.positionCursorBefore(key)
 * <li>SequentialFile.positionCursor(recordNumber)
 * <li>SequentialFile.positionCursorAfter(recordNumber)
 * <li>SequentialFile.positionCursorBefore(recordNumber)
 * </ul>
 * The positionCursor methods will be tested again for consistency in the
 * DDMPositionCaching(n) testcases which call this test case with a specified
 * blocking factor.
 **/
public class DDMPosition extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMPosition";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  // Blocking factor to be used for opens. This is used to check
  // that caching acts the same as when we always retrieve from the system.
  // Default indicate to not use caching
  int bf_ = 1;
  static final String STR16 = "                ";
  /* private */static final boolean DEBUG = false;

  /**
   * Constructor. This is called from the DDMTest constructor.
   **/
  public DDMPosition(AS400 systemObject, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream,  String testLib) {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMPosition", 109, // @A1C @A2C
        variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

  public DDMPosition(AS400 systemObject, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream,  String testLib,
      int blockingFactor, AS400 pwrsys) // Added pwrsys @A1C
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, (blockingFactor == 1) ? "DDMPosition"
        : "DDMPositionCaching" + String.valueOf(blockingFactor), 109,// @A1C
                                                                     // @A2C
        variationsToRun, runMode, fileOutputStream);
    bf_ = blockingFactor;
    testLib_ = testLib;
    pwrSys_ = pwrsys; // Added for @A1A
  }

 
  /**
   * Runs the variations requested.
   **/
  public void run() {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Do any necessary setup work for the variations
    try {
      setup();
    } catch (Exception e) {
      // Testcase setup did not complete successfully
      System.out.println("Unable to complete setup; variations not run");
      return;
    }
    System.out.println("Blocking factor of " + String.valueOf(bf_)
        + " was specified for " + name_ + ".");
    if (runMode_ != ATTENDED) {
      // Unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
      if (allVariations || variationsToRun_.contains("2"))
        Var002();
      if (allVariations || variationsToRun_.contains("3"))
        Var003();
      if (allVariations || variationsToRun_.contains("4"))
        Var004();
      if (allVariations || variationsToRun_.contains("5"))
        Var005();
      if (allVariations || variationsToRun_.contains("6"))
        Var006();
      if (allVariations || variationsToRun_.contains("7"))
        Var007();
      if (allVariations || variationsToRun_.contains("8"))
        Var008();
      if (allVariations || variationsToRun_.contains("9"))
        Var009();
      if (allVariations || variationsToRun_.contains("10"))
        Var010();
      if (allVariations || variationsToRun_.contains("11"))
        Var011();
      if (allVariations || variationsToRun_.contains("12"))
        Var012();
      if (allVariations || variationsToRun_.contains("13"))
        Var013();
      if (allVariations || variationsToRun_.contains("14"))
        Var014();
      if (allVariations || variationsToRun_.contains("15"))
        Var015();
      if (allVariations || variationsToRun_.contains("16"))
        Var016();
      if (allVariations || variationsToRun_.contains("17"))
        Var017();
      if (allVariations || variationsToRun_.contains("18"))
        Var018();
      if (allVariations || variationsToRun_.contains("19"))
        Var019();
      if (allVariations || variationsToRun_.contains("20"))
        Var020();
      if (allVariations || variationsToRun_.contains("21"))
        Var021();
      if (allVariations || variationsToRun_.contains("22"))
        Var022();
      if (allVariations || variationsToRun_.contains("23"))
        Var023();
      if (allVariations || variationsToRun_.contains("24"))
        Var024();
      if (allVariations || variationsToRun_.contains("25"))
        Var025();
      if (allVariations || variationsToRun_.contains("26"))
        Var026();
      if (allVariations || variationsToRun_.contains("27"))
        Var027();
      if (allVariations || variationsToRun_.contains("28"))
        Var028();
      if (allVariations || variationsToRun_.contains("29"))
        Var029();
      if (allVariations || variationsToRun_.contains("30"))
        Var030();
      if (allVariations || variationsToRun_.contains("31"))
        Var031();
      if (allVariations || variationsToRun_.contains("32"))
        Var032();
      if (allVariations || variationsToRun_.contains("33"))
        Var033();
      if (allVariations || variationsToRun_.contains("34"))
        Var034();
      if (allVariations || variationsToRun_.contains("35"))
        Var035();
      if (allVariations || variationsToRun_.contains("36"))
        Var036();
      if (allVariations || variationsToRun_.contains("37"))
        Var037();
      if (allVariations || variationsToRun_.contains("38"))
        Var038();
      if (allVariations || variationsToRun_.contains("39"))
        Var039();
      if (allVariations || variationsToRun_.contains("40"))
        Var040();
      if (allVariations || variationsToRun_.contains("41"))
        Var041();
      if (allVariations || variationsToRun_.contains("42"))
        Var042();
      if (allVariations || variationsToRun_.contains("43"))
        Var043();
      if (allVariations || variationsToRun_.contains("44"))
        Var044();
      if (allVariations || variationsToRun_.contains("45"))
        Var045();
      if (allVariations || variationsToRun_.contains("46"))
        Var046();
      if (allVariations || variationsToRun_.contains("47"))
        Var047();
      if (allVariations || variationsToRun_.contains("48"))
        Var048();
      if (allVariations || variationsToRun_.contains("49"))
        Var049();
      if (allVariations || variationsToRun_.contains("50"))
        Var050();
      if (allVariations || variationsToRun_.contains("51"))
        Var051();
      if (allVariations || variationsToRun_.contains("52"))
        Var052();
      if (allVariations || variationsToRun_.contains("53"))
        Var053();
      if (allVariations || variationsToRun_.contains("54"))
        Var054();
      if (allVariations || variationsToRun_.contains("55"))
        Var055();
      if (allVariations || variationsToRun_.contains("56"))
        Var056();
      if (allVariations || variationsToRun_.contains("57"))
        Var057();
      if (allVariations || variationsToRun_.contains("58"))
        Var058();
      if (allVariations || variationsToRun_.contains("59"))
        Var059();
      if (allVariations || variationsToRun_.contains("60"))
        Var060();
      if (allVariations || variationsToRun_.contains("61"))
        Var061();
      if (allVariations || variationsToRun_.contains("62"))
        Var062();
      if (allVariations || variationsToRun_.contains("63"))
        Var063();
      if (allVariations || variationsToRun_.contains("64"))
        Var064();
      if (allVariations || variationsToRun_.contains("65"))
        Var065();
      if (allVariations || variationsToRun_.contains("66"))
        Var066();
      if (allVariations || variationsToRun_.contains("67"))
        Var067();
      if (allVariations || variationsToRun_.contains("68"))
        Var068();
      if (allVariations || variationsToRun_.contains("69"))
        Var069();
      if (allVariations || variationsToRun_.contains("70"))
        Var070();
      if (allVariations || variationsToRun_.contains("71"))
        Var071();
      if (allVariations || variationsToRun_.contains("72"))
        Var072();
      if (allVariations || variationsToRun_.contains("73"))
        Var073();
      if (allVariations || variationsToRun_.contains("74"))
        Var074();
      if (allVariations || variationsToRun_.contains("75"))
        Var075();
      if (allVariations || variationsToRun_.contains("76"))
        Var076();
      if (allVariations || variationsToRun_.contains("77"))
        Var077();
      if (allVariations || variationsToRun_.contains("78"))
        Var078();
      if (allVariations || variationsToRun_.contains("79"))
        Var079();
      if (allVariations || variationsToRun_.contains("80"))
        Var080();
      if (allVariations || variationsToRun_.contains("81"))
        Var081();
      if (allVariations || variationsToRun_.contains("82"))
        Var082();
      if (allVariations || variationsToRun_.contains("83"))
        Var083();
      if (allVariations || variationsToRun_.contains("84"))
        Var084();
      if (allVariations || variationsToRun_.contains("85"))
        Var085();
      if (allVariations || variationsToRun_.contains("86"))
        Var086();
      if (allVariations || variationsToRun_.contains("87"))
        Var087();
      if (allVariations || variationsToRun_.contains("88"))
        Var088();
      if (allVariations || variationsToRun_.contains("89"))
        Var089();
      if (allVariations || variationsToRun_.contains("90"))
        Var090();
      if (allVariations || variationsToRun_.contains("91"))
        Var091();
      if (allVariations || variationsToRun_.contains("92"))
        Var092();
      if (allVariations || variationsToRun_.contains("93"))
        Var093();
      if (allVariations || variationsToRun_.contains("94"))
        Var094();
      if (allVariations || variationsToRun_.contains("95"))
        Var095();
      if (allVariations || variationsToRun_.contains("96"))
        Var096();
      if (allVariations || variationsToRun_.contains("97"))
        Var097();
      if (allVariations || variationsToRun_.contains("98"))
        Var098();
      if (allVariations || variationsToRun_.contains("99"))
        Var099();
      if (allVariations || variationsToRun_.contains("100"))
        Var100();
      if (allVariations || variationsToRun_.contains("101"))
        Var101();
      if (allVariations || variationsToRun_.contains("102"))
        Var102();
      if (allVariations || variationsToRun_.contains("103"))
        Var103();
      if (allVariations || variationsToRun_.contains("104"))
        Var104();
      if (allVariations || variationsToRun_.contains("105"))
        Var105();
      if (allVariations || variationsToRun_.contains("106"))
        Var106();
      if (allVariations || variationsToRun_.contains("107")) // @A1A
        Var107(); // @A1A
      if (allVariations || variationsToRun_.contains("108")) // @A1A
        Var108(); // @A1A
      if (allVariations || variationsToRun_.contains("109")) // @A2A
        Var109(); // @A2A
    }
  }

  /**
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    try {
     
      // Delete and recreate library DDMTEST
      CommandCall c = new CommandCall(pwrSys_);
      deleteLibrary(c, testLib_);

      c.run("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals(
          "CPC2102"))) {
        for (int i = 0; i < msgs.length; ++i) {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      // Added following several lines for a file used in var108 @A1A
      c.run("CLRPFM FILE(DDMTESTSAV/FIELDS3) MBR(FIELDS3)"); // @A1A
      msgs = c.getMessageList();
      // CPC3101 - Member &2 file &1 in &3 cleared. (Success msg)
      if (!(msgs[0].getID().equals("CPC3101"))) {
        for (int i = 0; i < msgs.length; ++i) {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      // Added following several lines for a file used in var109 @A2A
      c.run("CLRPFM FILE(DDMTESTSAV/FIELDS2) MBR(FIELDS2)"); // @A2A
      msgs = c.getMessageList();
      // CPC3101 - Member &2 file &1 in &3 cleared. (Success msg)
      if (!(msgs[0].getID().equals("CPC3101"))) {
        for (int i = 0; i < msgs.length; ++i) {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
    } catch (Exception e) {
      e.printStackTrace(output_);
      throw e;
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorAfterLast() with a
   * SequentialFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>readPrevious() returns the last record in the file.
   * </ul>
   **/
  public void Var001() {
    setVariation(1);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V1.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Record2")) {
        file.positionCursorBeforeFirst();
        file.positionCursorAfterLast();
        record = file.readPrevious();
        assertCondition(((String) record.getField(0)).startsWith("Record2"));
      } else {
        failed();
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorBeforeFirst() with a
   * SequentialFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>readNext() returns the first record in the file.
   * </ul>
   **/
  public void Var002() {
    setVariation(2);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V2.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBeforeFirst();
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Record1")) {
        file.positionCursorAfterLast();
        file.positionCursorBeforeFirst();
        record = file.readNext();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      } else {
        failed();
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToFirst() with a
   * SequentialFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>read() returns the first record in the file.
   * </ul>
   **/
  public void Var003() {
    setVariation(3);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V3.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1")) {
        file.positionCursorAfterLast();
        file.positionCursorToFirst();
        record = file.read();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      } else {
        failed();
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToLast() with a
   * SequentialFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>read() returns the last record in the file.
   * </ul>
   **/
  public void Var004() {
    setVariation(4);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V4.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record2")) {
        file.positionCursorBeforeFirst();
        file.positionCursorToLast();
        record = file.read();
        assertCondition(((String) record.getField(0)).startsWith("Record2"));
      } else {
        failed();
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToNext() with a
   * SequentialFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>When done as first thing after opening file, the first record in the
   * file is returned.
   * <li>read() returns the next (next prior to positioning cursor) record in
   * the file.
   * </ul>
   **/
  public void Var005() {
    setVariation(5);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V5.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToNext();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1")) {
        file.positionCursorBeforeFirst();
        file.positionCursorToNext();
        record = file.read();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      } else {
        failed("Incorrect record returned: " + record.toString());
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToPrevious() with a
   * SequentialFile.
   * <ul compact>
   * <li>After having done some other positioning operation which results in the
   * cursor being positioned after a valid record.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>read() returns the previous (previous prior to positioning cursor)
   * record in the file.
   * </ul>
   **/
  public void Var006() {
    setVariation(6);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V6.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      file.positionCursorToPrevious();
      record = file.read();
      if ((record != null)
          && ((String) record.getField(0)).startsWith("Record2")) {
        succeeded();
      } else {
        if (record != null) {
          failed("Wrong record returned: " + record.toString());
        } else {
          failed("null record returned.");
        }
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorAfterLast().
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var007() {
    setVariation(7);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V7.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorAfterLast();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorBeforeFirst() with a
   * SequentialFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var008() {
    setVariation(8);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V8.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorBeforeFirst();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToFirst() with a
   * SequentialFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var009() {
    setVariation(9);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V9.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorToFirst();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToFirst() with a
   * SequentialFile.
   * <ul compact>
   * <li>The file contains no records.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001
   * </ul>
   **/
  public void Var010() {
    setVariation(10);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V10.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToLast() with a
   * SequentialFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var011() {
    setVariation(11);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V11.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorToLast();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToLast() with a
   * SequentialFile.
   * <ul compact>
   * <li>The file has no records.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var012() {
    setVariation(12);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V12.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e);
    }

    try {
      if (file != null) { 
      file.close();
      file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToNext() with a
   * SequentialFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var013() {
    setVariation(13);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V13.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorToNext();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToNext() with a
   * SequentialFile.
   * <ul compact>
   * <li>The file has no records
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var014() {
    setVariation(14);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V12.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToNext();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToNext() with a
   * SequentialFile.
   * <ul compact>
   * <li>The cursor is positioned on the last record.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var015() {
    setVariation(15);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V15.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      file.positionCursorToLast();
      file.positionCursorToNext();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }

  }

  /**
   * Verify invalid usage of AS400File.positionCursorToPrevious() with a
   * SequentialFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var016() {
    setVariation(16);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V16.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorToPrevious();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToPrevious() with a
   * SequentialFile.
   * <ul compact>
   * <li>The file has no records
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5025.
   * </ul>
   **/
  public void Var017() {
    setVariation(17);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V17.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToPrevious();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5025") != -1);
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToPrevious() with a
   * SequentialFile.
   * <ul compact>
   * <li>The cursor is positioned on the first record.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var018() {
    setVariation(18);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V18.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      file.positionCursorToFirst();
      file.positionCursorToPrevious();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }

  }

  /**
   * Verify valid usage of AS400File.positionCursorAfterLast() with a KeyedFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>readPrevious() returns the last record in the file.
   * </ul>
   **/
  public void Var019() {
    setVariation(19);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V19.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Record2")) {
        file.positionCursorToFirst();
        file.positionCursorAfterLast();
        record = file.readPrevious();
        assertCondition(((String) record.getField(0)).startsWith("Record2"));
      } else {
        failed("Incorrect record.");
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorBeforeFirst() with a
   * KeyedFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>readNext() returns the first record in the file.
   * </ul>
   **/
  public void Var020() {
    setVariation(20);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V20.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBeforeFirst();
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Record1")) {
        file.positionCursorAfterLast();
        file.positionCursorBeforeFirst();
        record = file.readNext();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      } else {
        failed("Incorrect record.");
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToFirst() with a KeyedFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>read() returns the first record in the file.
   * </ul>
   **/
  public void Var021() {
    setVariation(21);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V21.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1")) {
        file.positionCursorAfterLast();
        file.positionCursorToFirst();
        record = file.read();
        assertCondition(((String) record.getField(0)).startsWith("Record1"));
      } else {
        failed("Incorrect record.");
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToLast() with a KeyedFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>read() returns the last record in the file.
   * </ul>
   **/
  public void Var022() {
    setVariation(22);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V22.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record2")) {
        file.positionCursorToFirst();
        file.positionCursorToLast();
        record = file.read();
        assertCondition(((String) record.getField(0)).startsWith("Record2"));
      } else {
        failed("Incorrect record.");
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToNext() with a KeyedFile.
   * <ul compact>
   * <li>As the first thing done after opening the file.
   * <li>After having done some other positioning operation.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>When done as first thing after opening file, the first record in the
   * file is returned.
   * <li>read() returns the next (next prior to positioning cursor) record in
   * the file.
   * </ul>
   **/
  public void Var023() {
    setVariation(23);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V23.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToNext();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Record1")) {
        file.positionCursorToFirst();
        file.positionCursorToNext();
        record = file.read();
        if (!((String) record.getField(0)).startsWith("Record2")) {
          failed("Incorrect record after second positionCursorToNext(). "
              + record.toString());
        } else {
          succeeded();
        }
      } else {
        failed("Incorrect record after first positionCursorToNext(). "
            + record.toString());
      }
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of AS400File.positionCursorToPrevious() with a
   * KeyedFile.
   * <ul compact>
   * <li>After having done some other positioning operation which results in the
   * cursor being positioned after a valid record.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>read() returns the previous (previous prior to positioning cursor)
   * record in the file.
   * </ul>
   **/
  public void Var024() {
    setVariation(24);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V24.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      file.positionCursorToPrevious();
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record2"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorAfterLast().
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var025() {
    setVariation(25);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V25.FILE/MBR1.MBR");
      file.positionCursorAfterLast();
      failed("Exception didn't occur.");
    } catch (Exception e) {

      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorBeforeFirst() with a
   * KeyedFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var026() {
    setVariation(26);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V26.FILE/MBR1.MBR");
      file.positionCursorBeforeFirst();
      failed("Exception didn't occur.");
    } catch (Exception e) {

      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToFirst() with a KeyedFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var027() {
    setVariation(27);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V27.FILE/MBR1.MBR");
      file.positionCursorToFirst();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToFirst() with a KeyedFile.
   * <ul compact>
   * <li>The file contains no records.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001
   * </ul>
   **/
  public void Var028() {
    setVariation(28);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V28.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5001") == -1) {
        failed(e, "Wrong exception.");
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToLast() with a KeyedFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var029() {
    setVariation(29);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V29.FILE/MBR1.MBR");
      file.positionCursorToLast();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToLast() with a KeyedFile.
   * <ul compact>
   * <li>The file has no records.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var030() {
    setVariation(30);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V30.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToLast();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToNext() with a KeyedFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var031() {
    setVariation(31);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V31.FILE/MBR1.MBR");
      file.positionCursorToNext();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToNext() with a KeyedFile.
   * <ul compact>
   * <li>The file has no records
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var032() {
    setVariation(32);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V32.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToNext();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToNext() with a KeyedFile.
   * <ul compact>
   * <li>The cursor is positioned on the last record.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var033() {
    setVariation(33);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V33.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      file.positionCursorToLast();
      file.positionCursorToNext();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToPrevious() with a
   * KeyedFile.
   * <ul compact>
   * <li>Prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var034() {
    setVariation(34);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V31.FILE/MBR1.MBR");
      file.positionCursorToPrevious();
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToPrevious() with a
   * KeyedFile.
   * <ul compact>
   * <li>The file has no records
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5025.
   * </ul>
   **/
  public void Var035() {
    setVariation(35);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V35.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToPrevious();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5025") != -1);
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of AS400File.positionCursorToPrevious() with a
   * KeyedFile.
   * <ul compact>
   * <li>The cursor is positioned on the first record.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var036() {
    setVariation(36);
    KeyedFile file = null;
    try {
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V36.FILE/MBR1.MBR");
      DDMFormat1Field1Key format = new DDMFormat1Field1Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      file.positionCursorToFirst();
      file.positionCursorToPrevious();
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5001") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var037() {
    setVariation(37);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V37.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record3   ";
      file.positionCursor(key);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_EQ).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_EQ for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var038() {
    setVariation(38);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V38.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record3   ";
      file.positionCursor(key, KeyedFile.KEY_EQ);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_LT).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_LT for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the record before the first record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var039() {
    setVariation(39);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V39.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record3   ";
      file.positionCursor(key, KeyedFile.KEY_LT);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record2"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_LE) when a record
   * matching <i>key</i> exists in the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_LE for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var040() {
    setVariation(40);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V40.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record3   ";
      file.positionCursor(key, KeyedFile.KEY_LE);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_LE) when a record
   * matching <i>key</i> does not exist in the file and there is a record that
   * comes before <i>key</i> in the key sequence of the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify a <i>key</i> that does not match a record in the file but does come
   * after an existing record in the file by key. Specify KEY_LE for the
   * searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the last record in the file that would come
   * before a record having <i>key</i> for its key, in the file.
   * </ul>
   **/
  public void Var041() {
    setVariation(41);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V41.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record5   ";
      file.positionCursor(key, KeyedFile.KEY_LE);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record4"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_GT).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_GT for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the record after the first record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var042() {
    setVariation(42);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V42.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record3   ";
      file.positionCursor(key, KeyedFile.KEY_GT);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record4"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_GE) when a record
   * matching <i>key</i> exists in the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_GE for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var043() {
    setVariation(43);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V43.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record3   ";
      file.positionCursor(key, KeyedFile.KEY_GE);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_GE) when a record
   * matching <i>key</i> does not exist in the file and there is a record that
   * comes after <i>key</i> in the key sequence of the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify a <i>key</i> that does not match a record in the file but does come
   * before an existing record in the file by key. Specify KEY_GE for the
   * searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record in the file that would come
   * after a record having <i>key</i> for its key, in the file.
   * </ul>
   **/
  public void Var044() {
    setVariation(44);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V44.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record0   ";
      file.positionCursor(key, KeyedFile.KEY_GE);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record1"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Open the file and invoke positionCursorAfter(key).
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read is the record immediately following the record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var045() {
    setVariation(45);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V45.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record1   ";
      file.positionCursorAfter(key);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record2"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Open the file and invoke positionCursorBefore(key).
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read is the record immediately preceding the record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var046() {
    setVariation(46);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V46.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record2   ";
      file.positionCursorBefore(key);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record1"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var047() {
    setVariation(47);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V47.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      String[] key = new String[1];
      key[0] = "Record2   ";
      file.positionCursor(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key".
   * </ul>
   **/
  public void Var048() {
    setVariation(48);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V48.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor((Object[]) null);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var049() {
    setVariation(49);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V49.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[0];
      file.positionCursor(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID
   * </ul>
   **/
  public void Var050() {
    setVariation(50);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V50.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[2];
      key[0] = null;
      file.positionCursor(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var051() {
    setVariation(51);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V51.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      String[] key = new String[1];
      key[0] = "Record2   ";
      file.positionCursor(key, KeyedFile.KEY_LT);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>NullPointerException indicating "key".
   * </ul>
   **/
  public void Var052() {
    setVariation(52);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V52.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor((Object[]) null, KeyedFile.KEY_LT);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var053() {
    setVariation(53);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V53.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[0];
      file.positionCursor(key, KeyedFile.KEY_LT);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID.
   * </ul>
   **/
  public void Var054() {
    setVariation(54);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V54.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[2];
      key[0] = "Record2   ";
      key[1] = null;
      file.positionCursor(key, KeyedFile.KEY_GT);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify invalid value for searchType.
   * <ul compact>
   * <li>-1
   * <li>5
   * </ul>
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>Not sure.
   * </ul>
   **/
  public void Var055() {
    setVariation(55);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V55.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record2   ";
      file.positionCursor(key, -1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "searchType",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        try {
          String[] key = new String[1];
          key[0] = "Record2   ";
          file.positionCursor(key, 5);
          failed("Exception didn't occur.");
        } catch (Exception e1) {
          assertExceptionStartsWith(e, "ExtendedIllegalArgumentException",
              "searchType",
              ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      } else {
        failed(e, "Incorrect exception information.");
      }
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var056() {
    setVariation(56);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V56.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      String[] key = new String[1];
      key[0] = "Record2   ";
      file.positionCursorAfter(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key".
   * </ul>
   **/
  public void Var057() {
    setVariation(57);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V57.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfter((Object[]) null);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var058() {
    setVariation(58);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V58.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[0];
      file.positionCursorAfter(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID.
   * </ul>
   **/
  public void Var059() {
    setVariation(59);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V59.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[2];
      key[0] = "Record2   ";
      key[1] = null;
      file.positionCursorAfter(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify <i>key</i> such that an attempt is made to position the cursor
   * past the last record of the file..
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5006
   * </ul>
   **/
  public void Var060() {
    setVariation(60);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V60.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record4   ";
      file.positionCursorAfter(key);
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5006") != -1) {
        succeeded();
      } else {
        failed(e, "Wrong AS400Exception.");
      }
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var061() {
    setVariation(61);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V61.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      String[] key = new String[1];
      key[0] = "Record2   ";
      file.positionCursorBefore(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key".
   * </ul>
   **/
  public void Var062() {
    setVariation(62);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V62.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBefore((Object[]) null);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var063() {
    setVariation(63);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V63.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[0];
      file.positionCursorBefore(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID.
   * </ul>
   **/
  public void Var064() {
    setVariation(64);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V62.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[2];
      key[0] = "Record2   ";
      key[1] = null;
      file.positionCursorBefore(key);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify <i>key</i> such that an attempt is made to position the cursor
   * before the first record of the file..
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001
   * </ul>
   **/
  public void Var065() {
    setVariation(65);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V65.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record1   ";
      // The key field is not variable length, so we have to pad our key to get
      // a match.
      while (key[0].length() < file.getRecordFormat().getKeyFieldDescription(0)
          .getLength())
        key[0] += " ";
      file.positionCursorBefore(key);
      // failed("Exception didn't occur.");
      succeeded(); // This should work now, after changes in JTOpen.
    }
    /*
     * catch(AS400Exception e) { AS400Message msg = e.getAS400Message();
     * 
     * if (msg.getID().toUpperCase().indexOf("CPF5001") != -1) { succeeded(); }
     * else { failed(e, "Wrong AS400Exception"); } }
     */
    catch (Exception e) {
      // failed(e, "Incorrect exception information.");
      failed(e, "Unexpected exception.");
    }

    try {
      if (file != null) {
        file.close();
        file.delete();
      }
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of SequentialFile.positionCursor(recordNumber).
   * <ul compact>
   * <li>Open the file and position the cursor by recordNumber to a record in
   * the file.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be record <i>recordNumber</i>.
   * </ul>
   **/
  public void Var066() {
    setVariation(66);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V66.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor(1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record1"));
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }

  }

  /**
   * Verify invalid usage of SequentialFile.positionCursor(recordNumber).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var067() {
    setVariation(67);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V67.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursor(1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursor(recordNumber).
   * <ul compact>
   * <li>Specify 0 for recordNumber.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "recordNumber" and
   * PARAMETER_VALUE_NOT_VALID
   * </ul>
   **/
  public void Var068() {
    setVariation(68);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V68.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor(0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "recordNumber",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursor(recordNumber).
   * <ul compact>
   * <li>Specify a recordNumber that does not exist in the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5006.
   * </ul>
   **/
  public void Var069() {
    setVariation(69);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V69.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor(3);
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5006") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorAfter(recordNumber).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var070() {
    setVariation(70);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V70.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorAfter(1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorAfter(recordNumber).
   * <ul compact>
   * <li>Specify 0 for recordNumber.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "recordNumber" and
   * PARAMETER_VALUE_NOT_VALID
   * </ul>
   **/
  public void Var071() {
    setVariation(71);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V71.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfter(0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "recordNumber",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorAfter(recordNumber).
   * <ul compact>
   * <li>Specify a recordNumber that does not exist in the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var072() {
    setVariation(72);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V72.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfter(3);
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5006") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorAfter(recordNumber).
   * <ul compact>
   * <li>Specify <i>recordNumber</i> such that an attempt is made to position
   * the cursor past the last record of the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001
   * </ul>
   **/
  public void Var073() {
    setVariation(73);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V73.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfter(2);
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5001") == -1) {
        failed(e, "Wrong AS400 message returned.");
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorBefore(recordNumber).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var074() {
    setVariation(74);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V70.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.positionCursorBefore(1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorBefore(recordNumber).
   * <ul compact>
   * <li>Specify 0 for recordNumber.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "recordNumber" and
   * PARAMETER_VALUE_NOT_VALID
   * </ul>
   **/
  public void Var075() {
    setVariation(75);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V75.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBefore(0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "recordNumber",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorBefore(recordNumber).
   * <ul compact>
   * <li>Specify a recordNumber that does not exist in the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001.
   * </ul>
   **/
  public void Var076() {
    setVariation(76);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V76.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBefore(3);
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF5006") != -1);
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of SequentialFile.positionCursorBefore(recordNumber).
   * <ul compact>
   * <li>Specify <i>recordNumber</i> such that an attempt is made to position
   * the cursor before the first record of the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5001
   * </ul>
   **/
  public void Var077() {
    setVariation(77);
    SequentialFile file = null;
    try {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V77.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      record.setField(0, "Record1");
      file.write(record);
      record.setField(0, "Record2");
      file.write(record);
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBefore(1);
      /*
       * Record r = file.read(); if (r != null) {
       * System.out.println("Record at current cursor position is: " +
       * r.toString()); } else {
       * System.out.println("null record returned from read"); }
       */
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5001") == -1) {
        failed(e, "Incorrect exception");
      } else {
        succeeded();
      }
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var078() {
    setVariation(78);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V37.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record3   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_EQ).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_EQ for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var079() {
    setVariation(79);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V38.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record3   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_EQ, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_LT).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_LT for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the record before the first record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var080() {
    setVariation(80);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V39.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record3   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_LT, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record2"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_LE) when a record
   * matching <i>key</i> exists in the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_LE for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var081() {
    setVariation(81);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V40.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record3   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_LE, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_LE) when a record
   * matching <i>key</i> does not exist in the file and there is a record that
   * comes before <i>key</i> in the key sequence of the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify a <i>key</i> that does not match a record in the file but does come
   * after an existing record in the file by key. Specify KEY_LE for the
   * searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the last record in the file that would come
   * before a record having <i>key</i> for its key, in the file.
   * </ul>
   **/
  public void Var082() {
    setVariation(82);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V41.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record5   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record5   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_LE, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record4"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_GT).
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_GT for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the record after the first record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var083() {
    setVariation(83);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V42.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record3   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_GT, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record4"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_GE) when a record
   * matching <i>key</i> exists in the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify KEY_GE for the searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record matching <i>key</i> in the
   * file.
   * </ul>
   **/
  public void Var084() {
    setVariation(84);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V43.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record3   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_GE, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record3"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursor(key, KEY_GE) when a record
   * matching <i>key</i> does not exist in the file and there is a record that
   * comes after <i>key</i> in the key sequence of the file.
   * <ul compact>
   * <li>Open the file and position the cursor by key to a record in the file.
   * Specify a <i>key</i> that does not match a record in the file but does come
   * before an existing record in the file by key. Specify KEY_GE for the
   * searchType.
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read should be the first record in the file that would come
   * after a record having <i>key</i> for its key, in the file.
   * </ul>
   **/
  public void Var085() {
    setVariation(85);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V44.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record0   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record0   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_GE, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record1"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Open the file and invoke positionCursorAfter(key).
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read is the record immediately following the record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var086() {
    setVariation(86);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V45.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record1   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record1   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursorAfter(key, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record2"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify valid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Open the file and invoke positionCursorBefore(key).
   * <li>Invoke read().
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>The record read is the record immediately preceding the record matching
   * <i>key</i> in the file.
   * </ul>
   **/
  public void Var087() {
    setVariation(87);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V46.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursorBefore(key, 1);
      record = file.read();
      assertCondition(((String) record.getField(0)).startsWith("Record1"));
    } catch (Exception e) {
      failed(e);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var088() {
    setVariation(88);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V47.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      // String[] key = new String[1];
      // key[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key".
   * </ul>
   **/
  public void Var089() {
    setVariation(89);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V48.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor((byte[]) null, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var090() {
    setVariation(90);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V49.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);

      // String[] key = new String[0];
      byte[] key = new byte[0];

      file.positionCursor(key, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key).
   * <ul compact>
   * <li>Specify an invalid value for the number of key fields.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID
   * </ul>
   **/
  public void Var091() {
    setVariation(91);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V50.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[2];
      // key[0] = null;

      byte[] key = new byte[2];

      file.positionCursor(key, 0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "numberOfKeyFields",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        try {
          byte[] key = new byte[2];

          file.positionCursor(key, 4);
          failed("Exception didn't occur.");
        } catch (Exception e1) {
          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
              "numberOfKeyFields",
              ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      } else {
        failed("Incorrect exception information.");
      }
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var092() {
    setVariation(92);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V51.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      // String[] key = new String[1];
      // key[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(132, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 132);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, KeyedFile.KEY_LT, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>NullPointerException indicating "key".
   * </ul>
   **/
  public void Var093() {
    setVariation(93);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V52.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursor((byte[]) null, KeyedFile.KEY_LT);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var094() {
    setVariation(94);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V53.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[0];
      byte[] key = new byte[0];

      file.positionCursor(key, KeyedFile.KEY_LT);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID.
   * </ul>
   **/
  public void Var095() {
    setVariation(95);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V54.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[2];
      // key[0] = "Record2   ";
      // key[1] = null;

      byte[] key = new byte[2];

      file.positionCursor(key, KeyedFile.KEY_GT, 0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "numberOfKeyFields",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        try {
          byte[] key = new byte[2];

          file.positionCursor(key, KeyedFile.KEY_GT, 4);
          failed("Exception didn't occur.");
        } catch (Exception e1) {
          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
              "numberOfKeyFields",
              ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      } else {
        failed(e, "Incorrect exception information.");
      }
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursor(key, searchType).
   * <ul compact>
   * <li>Specify invalid value for searchType.
   * <ul compact>
   * <li>-1
   * <li>5
   * </ul>
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>Not sure.
   * </ul>
   **/
  public void Var096() {
    setVariation(96);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V55.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursor(key, -1, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "searchType",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        try {
          // String[] key = new String[1];
          // key[0] = "Record2   ";

          ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
          AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
              systemObject_);
          keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
          byte[] key = keyAsBytes.toByteArray();

          file.positionCursor(key, 5, 1);
          failed("Exception didn't occur.");
        } catch (Exception e1) {
          assertExceptionStartsWith(e, "ExtendedIllegalArgumentException",
              "searchType",
              ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      } else {
        failed(e, "Incorrect exception information.");
      }
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var097() {
    setVariation(97);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V56.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      // String[] key = new String[1];
      // key[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursorAfter(key, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key".
   * </ul>
   **/
  public void Var098() {
    setVariation(98);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V57.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfter((byte[]) null, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var099() {
    setVariation(99);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V58.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[0];

      byte[] key = new byte[0];

      file.positionCursorAfter(key, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID.
   * </ul>
   **/
  public void Var100() {
    setVariation(100);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V59.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[2];
      // key[0] = "Record2   ";
      // key[1] = null;

      byte[] key = new byte[2];

      file.positionCursorAfter(key, 0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "numberOfKeyFields",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        try {
          byte[] key = new byte[2];

          file.positionCursorAfter(key, 4);
          failed("Exception didn't occur.");
        } catch (Exception e1) {
          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
              "numberOfKeyFields",
              ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      } else {
        failed(e, "Incorrect exception information.");
      }
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorAfter(key).
   * <ul compact>
   * <li>Specify <i>key</i> such that an attempt is made to position the cursor
   * past the last record of the file..
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5006
   * </ul>
   **/
  public void Var101() {
    setVariation(101);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V60.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record4   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record4   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursorAfter(key, 1);
      failed("Exception didn't occur.");
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5006") != -1) {
        succeeded();
      } else {
        failed(e, "Wrong AS400Exception.");
      }
    } catch (Exception e) {
      failed(e, "Incorrect exception information.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Attempt to position the cursor prior to opening the file.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN.
   * </ul>
   **/
  public void Var102() {
    setVariation(102);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V61.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      // String[] key = new String[1];
      // key[0] = "Record2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record2   "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursorBefore(key, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "ExtendedIllegalStateException",
          ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify null for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key".
   * </ul>
   **/
  public void Var103() {
    setVariation(103);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V62.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorBefore((byte[]) null, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "key");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify an array of size 0 for key.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var104() {
    setVariation(104);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V63.FILE/MBR1.MBR");
      DDMReadSeqKeyFormat format = new DDMReadSeqKeyFormat(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[0];

      byte[] key = new byte[0];

      file.positionCursorBefore(key, 1);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
          ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify null for one of the key field values in <i>key</i>.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException indicating "key" and
   * PARAMETER_VALUE_NOT_VALID.
   * </ul>
   **/
  public void Var105() {
    setVariation(105);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V62.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[2];
      // key[0] = "Record2   ";
      // key[1] = null;

      byte[] key = new byte[2];

      file.positionCursorBefore(key, 0);
      failed("Exception didn't occur.");
    } catch (Exception e) {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
          "numberOfKeyFields",
          ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        try {
          byte[] key = new byte[2];

          file.positionCursorBefore(key, 4);
          failed("Exception didn't occur.");
        } catch (Exception e1) {
          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
              "numberOfKeyFields",
              ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      } else {
        failed(e, "Incorrect exception information.");
      }
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify invalid usage of KeyedFile.positionCursorBefore(key).
   * <ul compact>
   * <li>Specify <i>key</i> such that an attempt is made to position the cursor
   * before the first record of the file..
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>AS400Exception indicating CPF5006
   * </ul>
   **/
  public void Var106() {
    setVariation(106);
    KeyedFile file = null;
    try {
      // Create a file having four records.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V65.FILE/MBR1.MBR");
      DDMFormat3Field3Key format = new DDMFormat3Field3Key(systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++) {
        record.setField(0, "Record" + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // String[] key = new String[1];
      // key[0] = "Record1   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(132, systemObject_.getCcsid(),
          systemObject_);
      keyAsBytes.write(text.toBytes("Record1   "), 0, 132);
      byte[] key = keyAsBytes.toByteArray();

      file.positionCursorBefore(key, 1);
      // failed("Exception didn't occur.");
      succeeded(); // This should work now, after changes in JTOpen.
    }
    /*
     * catch(AS400Exception e) { AS400Message msg = e.getAS400Message();
     * 
     * // if (msg.getID().toUpperCase().indexOf("CPF5006") != -1) if
     * (msg.getID().toUpperCase().indexOf("CPF5001") != -1) { succeeded(); }
     * else { failed(e, "Wrong AS400Exception"); } }
     */
    catch (Exception e) {
      // failed(e, "Incorrect exception information.");
      failed(e, "Unexpected exception.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify "valid" usage of KeyedFile.positionCursorxxx() methods. Performing
   * operations on a physical file only.
   * <ul compact>
   * <li>Specify <i>key</i> using several of the positionCursor methods.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>Read a record after each positionCursor, and verify that the correct
   * records are returned.
   * </ul>
   **/
  public void Var107() // Method added for @A1A
  {
    setVariation(107);
    KeyedFile file = null;
    boolean varSuccess = true;
    String failureString = new String("");
    try {
      // Create a physical file having 3 fields and all 3 fields keyed.
      // This variation verifies various positionCursor methods... some
      // using keys and some not.
      file = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_
          + ".LIB/V107.FILE/MBR1.MBR");
      // 3 Fields each 10 chars in length: All 3 fields are keyed
      DDMFormat3Field3KeyShort format = new DDMFormat3Field3KeyShort(
          systemObject_);
      file.create(format, "");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 9; i++) {
        record.setField(0, "Record__" + Integer.toString(i));
        record.setField(1, "Fld2____" + Integer.toString(i));
        record.setField(2, "Fld3____" + Integer.toString(i));
        file.write(record);
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
      }
      file.close();

      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key = new String[1];
      key[0] = "Record__1 ";
      file.positionCursorToFirst();
      record = file.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "1A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____1 ")) {
        failureString += "1B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3____1 ")) {
        failureString += "1C ";
        varSuccess = false;
      }
      record = file.readNext();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__2 ")) {
        failureString += "2A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____2 ")) {
        failureString += "2B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3____2 ")) {
        failureString += "2C ";
        varSuccess = false;
      }

      String[] key2 = new String[2];
      key2[0] = "Record__3 ";
      key2[1] = "Fld2____3 ";
      file.positionCursorAfter(key2);
      record = file.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__4 ")) {
        failureString += "3A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____4 ")) {
        failureString += "3B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3____4 ")) {
        failureString += "3C ";
        varSuccess = false;
      }

      String[] key3 = new String[3];
      key3[0] = "Record__6 ";
      key3[1] = "Fld2____6 ";
      key3[2] = "Fld3____6 ";
      file.positionCursorBefore(key3);
      record = file.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__5 ")) {
        failureString += "4A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____5 ")) {
        failureString += "4B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3____5 ")) {
        failureString += "4C ";
        varSuccess = false;
      }

      // Now try an update(key,record)
      key2[0] = "Record__6 ";
      key2[1] = "Fld2____6 ";
      record.setField(0, key2[0]); // keep same 1st field
      record.setField(1, key2[1]); // keep same 2nd field
      record.setField(2, "Fld3new_6"); // New 3rd field
      file.update(key2, record); // Update this record
      file.positionCursor(key2); // position cursor to the updated record
      record = file.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__6 ")) {
        failureString += "5A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____6 ")) {
        failureString += "5B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3new_6 ")) {
        failureString += "5C ";
        varSuccess = false;
      }

      if (varSuccess == true)
        succeeded();
      else {
        System.out.println("failureString='" + failureString + "'");
        failed("Incorrect record returned: " + record.toString());
      }
    } catch (AS400Exception e) {
      failed(e, "Unexpected AS400Exception");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }

    try {
      file.close();
      file.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify "valid" usage of KeyedFile.positionCursorxxx() methods. Performing
   * operations on a physical file -AND- a multi-format logical file where the
   * multi-format logical file has one of the formats which contains a *NONE
   * key. The *NONE key is ignored by the toolbox code (but is used by DB code
   * to build a common/single search index for the logical file that is usable
   * for all record formats in the logical file.)
   * <ul compact>
   * <li>Specify <i>key</i> using several of the positionCursor methods.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>Read a record after each positionCursor, and verify that the correct
   * records are returned.
   * </ul>
   **/
  public void Var108() // Method added for @A1A
  {
    setVariation(108);
    KeyedFile pf1 = null;
    KeyedFile lf1 = null;
    boolean varSuccess = true;
    String failureString = new String("");
    try {
      pf1 = new KeyedFile(systemObject_,
          "/QSYS.LIB/DDMTESTSAV.LIB/FIELDS3.FILE/FIELDS3.MBR");
      lf1 = new KeyedFile(systemObject_,
          "/QSYS.LIB/DDMTESTSAV.LIB/MLTFMT3.FILE");
      pf1.setRecordFormat();

      pf1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);

      // 3 Fields each 10 chars in length: All 3 fields are keyed
      Record record = pf1.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 9; i++) {
        record.setField(0, "Record__" + Integer.toString(i));
        record.setField(1, "Fld2____" + Integer.toString(i));
        record.setField(2, "Fld3___" + Integer.toString(i) + "A");
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
        pf1.write(record);
        record.setField(2, "Fld3___" + Integer.toString(i) + "B");
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
        pf1.write(record); // Write rec 2nd time with fld3 different
      }
      pf1.close(); // Now have 18 records in the Physical file

      // Now open PF1 in Read-only and read/compare records written above
      pf1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key1 = new String[1];
      key1[0] = "Record__1 ";
      pf1.positionCursorToFirst();
      record = pf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "1A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____1 ")) {
        failureString += "1B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___1A ")) {
        failureString += "1C ";
        varSuccess = false;
      }
      record = pf1.readNext();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "2A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____1 ")) {
        failureString += "2B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___1B ")) {
        failureString += "2C ";
        varSuccess = false;
      }

      String[] key2 = new String[2];
      key2[0] = "Record__3 ";
      key2[1] = "Fld2____3 ";
      pf1.positionCursorAfter(key2);
      record = pf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__3 ")) {
        failureString += "3A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____3 ")) {
        failureString += "3B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___3B ")) {
        failureString += "3C ";
        varSuccess = false;
      }

      String[] key3 = new String[3];
      key3[0] = "Record__6 ";
      key3[1] = "Fld2____6 ";
      key3[2] = "Fld3___6A ";
      pf1.positionCursorBefore(key3);
      record = pf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__5 ")) {
        failureString += "4A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____5 ")) {
        failureString += "4B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___5B ")) {
        failureString += "4C ";
        varSuccess = false;
      }
      pf1.close();

      // Now open Multi-Format LF1 - Set First Record Format and read records
      lf1.setRecordFormat(0); // Format: field1, field2, field3

      lf1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      key1[0] = "Record__1 ";
      lf1.positionCursorToFirst();
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "5A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____1 ")) {
        failureString += "5B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___1A ")) {
        failureString += "5C ";
        varSuccess = false;
      }

      key2[0] = "Record__3 ";
      key2[1] = "Fld2____3 ";
      lf1.positionCursorAfter(key2);
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__3 ")) {
        failureString += "6A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____3 ")) {
        failureString += "6B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___3B ")) {
        failureString += "6C ";
        varSuccess = false;
      }

      key3[0] = "Record__6 ";
      key3[1] = "Fld2____6 ";
      key3[2] = "Fld3___6A ";
      lf1.positionCursorBefore(key3);
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__5 ")) {
        failureString += "7A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2____5 ")) {
        failureString += "7B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___5B ")) {
        failureString += "7C ";
        varSuccess = false;
      }

      lf1.close();
      lf1.setRecordFormat(1); // Format: field2, field1, field3

      lf1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      key1[0] = "Record__1 ";
      lf1.positionCursorToFirst();
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Fld2____1 ")) {
        failureString += "8A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Record__1 ")) {
        failureString += "8B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___1A ")) {
        failureString += "8C ";
        varSuccess = false;
      }

      key2[0] = "Record__3 "; // Field1
      key2[1] = "Fld3___3A "; // Field3 (Since 2nd key field is *NONE)
      lf1.positionCursorAfter(key2);
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Fld2____3 ")) {
        failureString += "9A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Record__3 ")) {
        failureString += "9B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___3B ")) {
        failureString += "9C ";
        varSuccess = false;
      }

      // Now try an update(key,record)
      key2[0] = "Record__6 ";
      key2[1] = "Fld3___6A ";
      // Remember field 2 and field 1 are swapped in the QDDSSRC for the
      // second record format of the logical file that we are working on.
      record.setField(0, "Fld2new_6 "); // New 2nd field (really 1st in Physical
                                        // file)
      record.setField(1, key2[0]); // keep same 1st field (really 2nd in
                                   // Physical file)
      record.setField(2, key2[1]); // keep same 3rd field
      lf1.update(key2, record); // Update this record
      lf1.positionCursor(key2); // position cursor to the updated record
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Fld2new_6 ")) {
        failureString += "10A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Record__6 ")) {
        failureString += "10B ";
        varSuccess = false;
      }
      if (!((String) record.getField(2)).startsWith("Fld3___6A ")) {
        failureString += "10C ";
        varSuccess = false;
      }

      lf1.close();

      if (varSuccess == true)
        succeeded();
      else {
        System.out.println("failureString='" + failureString + "'");
        failed("Incorrect record returned: " + record.toString());
      }
    } catch (AS400Exception e) {
      failed(e, "Unexpected AS400Exception");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }

    try {
      pf1.close();
      pf1.delete();
    } catch (Exception e) {
    }
  }

  /**
   * Verify "valid" usage of KeyedFile.positionCursorxxx() methods. Performing
   * operations on a physical file -AND- a multi-format logical file where the
   * multi-format logical file has formats with a different number of fields per
   * format.
   * <ul compact>
   * <li>Specify <i>key</i> using several of the positionCursor methods.
   * </ul>
   * Expected results:
   * <ul compact>
   * <li>Read a record after each positionCursor, and verify that the correct
   * records are returned.
   * </ul>
   **/
  public void Var109() // Method added for @A2A
  {
    setVariation(109);
    KeyedFile pf1 = null;
    KeyedFile lf1 = null;
    boolean varSuccess = true;
    String failureString = new String("");
    try {
      pf1 = new KeyedFile(systemObject_,
          "/QSYS.LIB/DDMTESTSAV.LIB/FIELDS2.FILE/FIELDS2.MBR");
      lf1 = new KeyedFile(systemObject_,
          "/QSYS.LIB/DDMTESTSAV.LIB/MLTFMT4.FILE");
      pf1.setRecordFormat();

      pf1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);

      // 2 Fields each 10 chars in length: Both fields are keyed
      Record record = pf1.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 9; i++) {
        record.setField(0, "Record__" + Integer.toString(i));
        record.setField(1, "Fld2___" + Integer.toString(i) + "A");
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
        pf1.write(record);
        record.setField(1, "Fld2___" + Integer.toString(i) + "B");
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
        pf1.write(record); // Write rec 2nd time with fld2 different
      }
      pf1.close(); // Now have 18 records in the Physical file

      // Now open PF1 in Read-only and read/compare records written above
      pf1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String[] key1 = new String[1];
      key1[0] = "Record__1 ";
      pf1.positionCursorToFirst();
      record = pf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "1A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2___1A ")) {
        failureString += "1B ";
        varSuccess = false;
      }
      record = pf1.readNext();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "2A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2___1B ")) {
        failureString += "2B ";
        varSuccess = false;
      }

      String[] key2 = new String[2];
      key2[0] = "Record__3 ";
      key2[1] = "Fld2___3A";
      pf1.positionCursorAfter(key2);
      record = pf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__3 ")) {
        failureString += "3A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2___3B ")) {
        failureString += "3B ";
        varSuccess = false;
      }
      pf1.close();

      // Now open Multi-Format LF1 - Set First Record Format and read records
      lf1.setRecordFormat("FMT1"); // Format: field1, field2

      lf1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      lf1.positionCursorAfterLast();

      // 2 Fields each 10 chars in length: Both fields are keyed
      record = lf1.getRecordFormat().getNewRecord();
      for (int i = 10; i <= 12; i++) {
        record.setField(0, "Record_" + Integer.toString(i));
        record.setField(1, "Fld2__" + Integer.toString(i) + "A");
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
        // The next write() is where the failure occured prior to the fix
        // made for [ jt400-Bugs-1796143 ] Writing to a multiple format file
        // fails
        // Fixes in DDMObjectDataStream.java and AS400FileImplRemote.java
        lf1.write(record);
        record.setField(1, "Fld2__" + Integer.toString(i) + "B");
        if (DEBUG)
          System.out.println("Record='" + record.toString() + "'");
        lf1.write(record); // Write rec 2nd time with fld2 different
      }
      // Now have extra 6 records in the Physical file

      key1[0] = "Record__1 ";
      lf1.positionCursorToFirst();
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record__1 ")) {
        failureString += "5A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2___1A ")) {
        failureString += "5B ";
        varSuccess = false;
      }

      key2[0] = "Record_11 ";
      key2[1] = "Fld2__11A ";
      lf1.positionCursorAfter(key2);
      record = lf1.read();
      if (DEBUG)
        System.out.println("Record='" + record.toString() + "'");
      if (!((String) record.getField(0)).startsWith("Record_11 ")) {
        failureString += "6A ";
        varSuccess = false;
      }
      if (!((String) record.getField(1)).startsWith("Fld2__11B ")) {
        failureString += "6B ";
        varSuccess = false;
      }

      lf1.close();
      if (varSuccess == true)
        succeeded();
      else {
        System.out.println("failureString='" + failureString + "'");
        failed("Incorrect record returned: " + record.toString());
      }
    } catch (AS400Exception e) {
      failed(e, "Unexpected AS400Exception");
    } catch (Exception e) {
      failed(e, "Unexpected exception.");
    }

    try {
      pf1.close();
      pf1.delete();
    } catch (Exception e) {
    }
  }

}
