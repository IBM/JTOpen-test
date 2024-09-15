///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMReadKey.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.*;

import test.Testcase;

import java.io.ByteArrayOutputStream;

/**
 *Testcase DDMReadKey.  This test class verifies valid and invalid usage of
 *the KeyedFile read methods:
 *<ul compact>
 *<li>read(key)
 *<li>read(key, searchType)
 *<li>readAfter(key)
 *<li>readBefore(key)
 *<li>readNextEqual()
 *<li>readPreviousEqual()
 *<li>readNextEqual(key)
 *<li>readPreviousEqual(key)
 *</ul>
 *The read methods will be tested again for consistency in the
 *DDMReadKeyCaching(n) testcases which call this test case with a
 *specified blocking factor.
**/
public class DDMReadKey extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMReadKey";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  long start;
  long time;
  Record[] records_;
  Record[] dupKeyRecs_;
  Record[] dupKey3Recs_;
  int bf_;
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMReadKey(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMReadKey", 86,
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMReadKey(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      int              blockingFactor)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, (blockingFactor == 1)? "DDMReadKey" : "DDMReadKeyCaching" + String.valueOf(blockingFactor), 86,
          variationsToRun, runMode, fileOutputStream);
    bf_ = blockingFactor;
    testLib_ = testLib;
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Connect to the AS/400 for record the record level access service
    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      System.out.println("Unable to connect to the AS/400");
      e.printStackTrace();
      return;
    }

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

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }

    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }

    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }

    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }

    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }

    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }

    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }

    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }

    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }

    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }

    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }

    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }

    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }

    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }

    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }

    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }

    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }

    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }

    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }

    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }

    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }

    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }

    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }

    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }

    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }

    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }

    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }

    if ((allVariations || variationsToRun_.contains("35")) &&
        runMode_ != ATTENDED)
    {
      setVariation(35);
      Var035();
    }

    if ((allVariations || variationsToRun_.contains("36")) &&
        runMode_ != ATTENDED)
    {
      setVariation(36);
      Var036();
    }

    if ((allVariations || variationsToRun_.contains("37")) &&
        runMode_ != ATTENDED)
    {
      setVariation(37);
      Var037();
    }

    if ((allVariations || variationsToRun_.contains("38")) &&
        runMode_ != ATTENDED)
    {
      setVariation(38);
      Var038();
    }

    if ((allVariations || variationsToRun_.contains("39")) &&
        runMode_ != ATTENDED)
    {
      setVariation(39);
      Var039();
    }

    if ((allVariations || variationsToRun_.contains("40")) &&
        runMode_ != ATTENDED)
    {
      setVariation(40);
      Var040();
    }

    if ((allVariations || variationsToRun_.contains("41")) &&
        runMode_ != ATTENDED)
    {
      setVariation(41);
      Var041();
    }

    if ((allVariations || variationsToRun_.contains("42")) &&
        runMode_ != ATTENDED)
    {
      setVariation(42);
      Var042();
    }

    if ((allVariations || variationsToRun_.contains("43")) &&
        runMode_ != ATTENDED)
    {
      setVariation(43);
      Var043();
    }

    if ((allVariations || variationsToRun_.contains("44")) &&
        runMode_ != ATTENDED)
    {
      setVariation(44);
      Var044();
    }

    if ((allVariations || variationsToRun_.contains("45")) &&
        runMode_ != ATTENDED)
    {
      setVariation(45);
      Var045();
    }

    if ((allVariations || variationsToRun_.contains("46")) &&
        runMode_ != ATTENDED)
    {
      setVariation(46);
      Var046();
    }

    if ((allVariations || variationsToRun_.contains("47")) &&
        runMode_ != ATTENDED)
    {
      setVariation(47);
      Var047();
    }

    if ((allVariations || variationsToRun_.contains("48")) &&
        runMode_ != ATTENDED)
    {
      setVariation(48);
      Var048();
    }

    if ((allVariations || variationsToRun_.contains("49")) &&
        runMode_ != ATTENDED)
    {
      setVariation(49);
      Var049();
    }

    if ((allVariations || variationsToRun_.contains("50")) &&
        runMode_ != ATTENDED)
    {
      setVariation(50);
      Var050();
    }

    if ((allVariations || variationsToRun_.contains("51")) &&
        runMode_ != ATTENDED)
    {
      setVariation(51);
      Var051();
    }

    if ((allVariations || variationsToRun_.contains("52")) &&
        runMode_ != ATTENDED)
    {
      setVariation(52);
      Var052();
    }

    if ((allVariations || variationsToRun_.contains("53")) &&
        runMode_ != ATTENDED)
    {
      setVariation(53);
      Var053();
    }

    if ((allVariations || variationsToRun_.contains("54")) &&
        runMode_ != ATTENDED)
    {
      setVariation(54);
      Var054();
    }

    if ((allVariations || variationsToRun_.contains("55")) &&
        runMode_ != ATTENDED)
    {
      setVariation(55);
      Var055();
    }

    if ((allVariations || variationsToRun_.contains("56")) &&
        runMode_ != ATTENDED)
    {
      setVariation(56);
      Var056();
    }

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

    if ((allVariations || variationsToRun_.contains("71")) &&
        runMode_ != ATTENDED)
    {
      setVariation(71);
      Var071();    
    }

    if ((allVariations || variationsToRun_.contains("72")) &&
        runMode_ != ATTENDED)
    {
      setVariation(72);
      Var072();
    }

    if ((allVariations || variationsToRun_.contains("73")) &&
        runMode_ != ATTENDED)
    {
      setVariation(73);
      Var073();
    }

    if ((allVariations || variationsToRun_.contains("74")) &&
        runMode_ != ATTENDED)
    {
      setVariation(74);
      Var074();
    }

    if ((allVariations || variationsToRun_.contains("75")) &&
        runMode_ != ATTENDED)
    {
      setVariation(75);
      Var075();
    }

    if ((allVariations || variationsToRun_.contains("76")) &&
        runMode_ != ATTENDED)
    {
      setVariation(76);
      Var076();
    }

    if ((allVariations || variationsToRun_.contains("77")) &&
        runMode_ != ATTENDED)
    {
      setVariation(77);
      Var077();
    }

    if ((allVariations || variationsToRun_.contains("78")) &&
        runMode_ != ATTENDED)
    {
      setVariation(78);
      Var078();
    }

    if ((allVariations || variationsToRun_.contains("79")) &&
        runMode_ != ATTENDED)
    {
      setVariation(79);
      Var079();
    }

    if ((allVariations || variationsToRun_.contains("80")) &&
        runMode_ != ATTENDED)
    {
      setVariation(80);
      Var080();
    }

    if ((allVariations || variationsToRun_.contains("81")) &&
        runMode_ != ATTENDED)
    {
      setVariation(81);
      Var081();
    }

    if ((allVariations || variationsToRun_.contains("82")) &&
        runMode_ != ATTENDED)
    {
      setVariation(82);
      Var082();
    }

    if ((allVariations || variationsToRun_.contains("83")) &&
        runMode_ != ATTENDED)
    {
      setVariation(83);
      Var083();
    }

    if ((allVariations || variationsToRun_.contains("84")) &&
        runMode_ != ATTENDED)
    {
      setVariation(84);
      Var084();
    }

    if ((allVariations || variationsToRun_.contains("85")) &&
        runMode_ != ATTENDED)
    {
      setVariation(85);
      Var085();
    }

    if ((allVariations || variationsToRun_.contains("86")) &&
        runMode_ != ATTENDED)
    {
      setVariation(86);
      Var086();
    }


  // Do any necessary cleanup work for the variations
  try
  {
    cleanup();
  }
  catch (Exception e)
  {
    System.out.println("Unable to complete cleanup.");
  }

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
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
protected void setup()
  throws Exception
{
  try
  {
    // Delete and recreate library DDMTEST
    CommandCall c = new CommandCall(systemObject_);
    deleteLibrary(testLib_);;

    c.run("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
    AS400Message[] msgs = c.getMessageList();
    if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
    {
      for (int i = 0; i < msgs.length; ++i)
      {
        System.out.println(msgs[i]);
      }
      throw new Exception("");
    }
    // Create the necessary files
    KeyedFile f1 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
    f1.create(new DDMChar10KeyFormat(systemObject_), "One field, CHAR(10), one key");

    KeyedFile f2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE/%FILE%.MBR");
    f2.create(new DDMFormat3Field1Key(systemObject_), "3 fields, 1 keys");

    KeyedFile f3 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE/%FILE%.MBR");
    f3.create(new DDMFormatReadKey3(systemObject_), "5 fields, 3 keys");

    // Create an array of records to write to the f1
    records_ = new Record[9];
    RecordFormat rf = f1.getRecordFormat();
    for (short i = 1; i < 10; ++i)
    {
      records_[i-1] = rf.getNewRecord();
      records_[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
    }

    // Populate f1
    f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f1.write(records_);
    f1.close();

    // Populate f2
    f2.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    dupKeyRecs_ = new Record[9];
    RecordFormat f2rf = f2.getRecordFormat();
    dupKeyRecs_[0] = f2rf.getNewRecord();
    dupKeyRecs_[0].setField(0, "A");
    dupKeyRecs_[0].setField(1, "B");
    dupKeyRecs_[0].setField(2, "C");
    dupKeyRecs_[1] = f2rf.getNewRecord();
    dupKeyRecs_[1].setField(0, "A");
    dupKeyRecs_[1].setField(1, "C");
    dupKeyRecs_[1].setField(2, "C");
    dupKeyRecs_[2] = f2rf.getNewRecord();
    dupKeyRecs_[2].setField(0, "A");
    dupKeyRecs_[2].setField(1, "D");
    dupKeyRecs_[2].setField(2, "C");
    dupKeyRecs_[3] = f2rf.getNewRecord();
    dupKeyRecs_[3].setField(0, "B");
    dupKeyRecs_[3].setField(1, "B");
    dupKeyRecs_[3].setField(2, "C");
    dupKeyRecs_[4] = f2rf.getNewRecord();
    dupKeyRecs_[4].setField(0, "B");
    dupKeyRecs_[4].setField(1, "C");
    dupKeyRecs_[4].setField(2, "C");
    dupKeyRecs_[5] = f2rf.getNewRecord();
    dupKeyRecs_[5].setField(0, "B");
    dupKeyRecs_[5].setField(1, "D");
    dupKeyRecs_[5].setField(2, "C");
    dupKeyRecs_[6] = f2rf.getNewRecord();
    dupKeyRecs_[6].setField(0, "C");
    dupKeyRecs_[6].setField(1, "B");
    dupKeyRecs_[6].setField(2, "C");
    dupKeyRecs_[7] = f2rf.getNewRecord();
    dupKeyRecs_[7].setField(0, "C");
    dupKeyRecs_[7].setField(1, "C");
    dupKeyRecs_[7].setField(2, "C");
    dupKeyRecs_[8] = f2rf.getNewRecord();
    dupKeyRecs_[8].setField(0, "D");
    dupKeyRecs_[8].setField(1, "B");
    dupKeyRecs_[8].setField(2, "C");
    f2.write(dupKeyRecs_);
    f2.close();

    // Populate f3
    f3.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    dupKey3Recs_ = new Record[9];

    RecordFormat f3rf = f3.getRecordFormat();
    dupKey3Recs_[0] = f3rf.getNewRecord();
    dupKey3Recs_[0].setField(0, "Record 1   ");
    dupKey3Recs_[0].setField(1, new Integer(1));
    dupKey3Recs_[0].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[0].setField(3, "1 Record   ");
    dupKey3Recs_[0].setField(4, new BigDecimal("1.10000"));

    dupKey3Recs_[1] = f3rf.getNewRecord();
    dupKey3Recs_[1].setField(0, "Record 5   ");
    dupKey3Recs_[1].setField(1, new Integer(5));
    dupKey3Recs_[1].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[1].setField(3, "1 Record   ");
    dupKey3Recs_[1].setField(4, new BigDecimal("20.10000"));

    dupKey3Recs_[2] = f3rf.getNewRecord();
    dupKey3Recs_[2].setField(0, "Record 2   ");
    dupKey3Recs_[2].setField(1, new Integer(2));
    dupKey3Recs_[2].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[2].setField(3, "1 Record   ");
    dupKey3Recs_[2].setField(4, new BigDecimal("221.10000"));

    dupKey3Recs_[3] = f3rf.getNewRecord();
    dupKey3Recs_[3].setField(0, "Record 1   ");
    dupKey3Recs_[3].setField(1, new Integer(1));
    dupKey3Recs_[3].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[3].setField(3, "1 Record   ");
    dupKey3Recs_[3].setField(4, new BigDecimal("21.10000"));

    dupKey3Recs_[4] = f3rf.getNewRecord();
    dupKey3Recs_[4].setField(0, "Record 3   ");
    dupKey3Recs_[4].setField(1, new Integer(3));
    dupKey3Recs_[4].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[4].setField(3, "1 Record   ");
    dupKey3Recs_[4].setField(4, new BigDecimal("301.10000"));

    dupKey3Recs_[5] = f3rf.getNewRecord();
    dupKey3Recs_[5].setField(0, "Record 5   ");
    dupKey3Recs_[5].setField(1, new Integer(5));
    dupKey3Recs_[5].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[5].setField(3, "1 Record   ");
    dupKey3Recs_[5].setField(4, new BigDecimal("551.10000"));

    dupKey3Recs_[6] = f3rf.getNewRecord();
    dupKey3Recs_[6].setField(0, "Record 3   ");
    dupKey3Recs_[6].setField(1, new Integer(3));
    dupKey3Recs_[6].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[6].setField(3, "1 Record   ");
    dupKey3Recs_[6].setField(4, new BigDecimal("991.10000"));

    dupKey3Recs_[7] = f3rf.getNewRecord();
    dupKey3Recs_[7].setField(0, "Record 3   ");
    dupKey3Recs_[7].setField(1, new Integer(3));
    dupKey3Recs_[7].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[7].setField(3, "1 Record   ");
    dupKey3Recs_[7].setField(4, new BigDecimal("991.10000"));

    dupKey3Recs_[8] = f3rf.getNewRecord();
    dupKey3Recs_[8].setField(0, "Record 3   ");
    dupKey3Recs_[8].setField(1, new Integer(3));
    dupKey3Recs_[8].setField(2, new BigDecimal("0.00000"));
    dupKey3Recs_[8].setField(3, "1 Record   ");
    dupKey3Recs_[8].setField(4, new BigDecimal("21.10000"));

    f3.write(dupKey3Recs_);
    f3.close();
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
    KeyedFile f1 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE");
    f1.delete();
    KeyedFile f2 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
    f2.delete();
    KeyedFile f3 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
    f3.delete();
  }
  catch(Exception e)
  {
    System.out.println("Cleanup unsuccessful. Delete files " + testLib_ + "/READKEY1 and " + testLib_ + "/READKEY2 if they exist");
    e.printStackTrace(output_);
    throw e;
  }
}


  /**
   *Verify valid usage of read(key).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke read(key).
   *<li>Invoke read(key) again with the same key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[0].getField(0);
      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(key);
        rec2 = file.read(key);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(key).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke read(key).
   *<li>Invoke read(key) again with the same key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[0].getField(0);
      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(key);
        rec2 = file.read(key);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(key, searchType).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke read(key, KEY_EQ).
   *<li>Invoke read(key, KEY_LT).
   *<li>Invoke read(key, KEY_LE).
   *<li>Invoke read(key, KEY_GT).
   *<li>Invoke read(key, KEY_GE).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The record before the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *<li>The record after the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      Record match = records_[3];
      Record before = records_[2];
      Record after = records_[4];
      try
      {
        rec = file.read(key, KeyedFile.KEY_EQ);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_EQ) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (EQ) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LT);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records (LT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LE);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (LE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GT);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records (GT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GE);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (GE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(key, searchType).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke read(key, KEY_EQ).
   *<li>Invoke read(key, KEY_LT).
   *<li>Invoke read(key, KEY_LE).
   *<li>Invoke read(key, KEY_GT).
   *<li>Invoke read(key, KEY_GE).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The record before the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *<li>The record after the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      Record match = records_[3];
      Record before = records_[2];
      Record after = records_[4];
      try
      {
        rec = file.read(key, KeyedFile.KEY_EQ);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_EQ) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (EQ) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LT);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records (LT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LE);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (LE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GT);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records (GT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GE);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (GE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readAfter(key).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readAfter(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      Record after = records_[4];
      try
      {
        rec = file.readAfter(key);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readAfter(key).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke readAfter(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      Record after = records_[4];
      try
      {
        rec = file.readAfter(key);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readBefore(key).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readBefore(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      Record before = records_[2];
      try
      {
        rec = file.readBefore(key);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readBefore(key).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke readBefore(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      Record before = records_[2];
      try
      {
        rec = file.readBefore(key);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key).
   *<ul>
   *<li>Invoke read(key) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.read(key);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key, searchType).
   *<ul>
   *<li>Invoke read(key, searchType) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.read(key, KeyedFile.KEY_LT);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readAfter(key).
   *<ul>
   *<li>Invoke readAfter(key) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.readAfter(key);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readBefore(key).
   *<ul>
   *<li>Invoke readBefore(key) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

      Object[] key = new Object[1];
      key[0] = records_[3].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.readBefore(key);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *<li>Specify an array of length > 0 with null elements
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[0];
      Record rec = new Record();
      try
      {
        rec = file.read(null);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.read(key);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
      
      try
      {
        rec = file.read(new Object[1]);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
//        if (!exceptionIs(e, "NullPointerException", "key"))
//        {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key, searchType).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *<li>Specify an array of length > 0 with null elements
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[0];
      Record rec = new Record();
      try
      {
        rec = file.read((Object[])null, KeyedFile.KEY_LE);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GT);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
      
      try
      {
        rec = file.read(new Object[1], KeyedFile.KEY_GT);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
//        if (!exceptionIs(e, "NullPointerException", "key"))
//        {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readAfter(key).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *<li>Specify an array of length > 0 with null elements
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[0];
      Record rec = new Record();
      try
      {
        rec = file.readAfter(null);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.readAfter(key);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
      
      try
      {
        rec = file.readAfter(new Object[1]);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
//        if (!exceptionIs(e, "NullPointerException", "key"))
//        {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readBefore(key).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *<li>Specify an array of length > 0 with null elements
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var016()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[0];
      Record rec = new Record();
      try
      {
        rec = file.readBefore(null);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.readBefore(key);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
      
      try
      {
        rec = file.readBefore(new Object[1]);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
//        if (!exceptionIs(e, "NullPointerException", "key"))
//        {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key, searchType).
   *<ul>
   *<li>Specify an invalid searchType.
   *<ul compact>
   *<li>searchType < 0
   *searchType > 4
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "searchType" and PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var017()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[5].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.read(key, -1);
        failMsg.append("Expected searchtype too small exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.read(key, 5);
        failMsg.append("Expected searchtype too big exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that null is returned when trying to read(key) with key that does
   *not exist in the file.
   *<ul compact>
   *<li>Invoke read(key) with a key value that does not exist in the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var018()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = "RECORD10  ";
      Record rec = new Record();
      try
      {
        rec = file.read(key);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (rec != null)
      {
        failMsg.append("null not returned on non-existent key read: "+rec.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that the first record before <i>key</i> in the file is returned
   *when trying to read(key, KEY_LE) with key that does
   *not exist in the file for which there are records with keys that would be
   *before the key specified.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The appropriate record is returned.
   *</ul>
  **/
  public void Var019()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = "RECORD 99 ";
      Record rec = new Record();
      Record before = records_[8];
      try
      {
        rec = file.read(key, KeyedFile.KEY_LE);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that the first record after <i>key</i> in the file is returned
   *when trying to read(key, KEY_GE) with key that does
   *not exist in the file for which there are records with keys that would be
   *after the key specified.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The appropriate record is returned.
   *</ul>
  **/
  public void Var020()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = "RECORD 0  ";
      Record rec = new Record();
      Record after = records_[0];
      try
      {
        rec = file.read(key, KeyedFile.KEY_GE);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that null is returned when readAfter(key) is specified and no record
   *exists after the record matching <i>key</i>.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var021()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[8].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.readAfter(key);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (rec != null)
      {
        failMsg.append("null not returned on readAfter: "+rec.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that null is returned when readBefore(key) is specified and no record
   *exists before the record matching <i>key</i>.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var022()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records_[0].getField(0);
      Record rec = new Record();
      try
      {
        rec = file.readBefore(key);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (rec != null)
      {
        failMsg.append("null not returned on readBefore: "+rec.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul compact>
   *<li>readFirst() then readNextEqual()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will have field 0 = "A", field 1 = "C", field 2 = "C"
   *</ul>
  **/
  public void Var023()
  {
    KeyedFile f = null;
    KeyedFile fchk = null;
    try
    {
      fchk  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      fchk.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      fchk.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      fchk.readFirst();
      Record match = fchk.readNext();
      Record r = f.readNextEqual();
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        fchk.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        fchk.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
      fchk.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul compact>
   *<li>positionCursorToFirst() then readNextEqual()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will have field 0 = "A", field 1 = "C", field 2 = "C"
   *</ul>
  **/
  public void Var024()
  {
    KeyedFile f = null;
    KeyedFile fchk = null;
    try
    {
      fchk  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      fchk.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      fchk.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorToFirst();
      Record r = f.readNextEqual();
      fchk.readNext();
      Record match = fchk.readNext();
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        fchk.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        fchk.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
      fchk.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual().
   *<ul compact>
   *<li>read(key), readNextEqual() then readPreviousEqual() where
   *key is a key that has duplicates within the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned from read(key) will be returned from
   *readPreviousEqual()
   *</ul>
  **/
  public void Var025()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[1];
      StringBuffer k = new StringBuffer();
      k.append("C");
      for (int i = 0; i < 10; ++i)
      {
        k.append(" ");
      }
      key[0] = k.toString();
      Record match = f.read(key);
      f.readNextEqual();
      Record r = f.readPreviousEqual();
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual().
   *<ul compact>
   *<li>positionCursor(key), positionCursorToNext() then readPreviousEqual() where
   *key is a key that has duplicates within the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will have field 0 = "C", field 1 = "B", field 2 = "C"
   *</ul>
  **/
  public void Var026()
  {
    KeyedFile f = null;
    KeyedFile fchk = null;
    try
    {
      fchk  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      fchk.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      fchk.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[1];
      StringBuffer k = new StringBuffer();
      k.append("C");
      for (int i = 0; i < 10; ++i)
      {
        k.append(" ");
      }
      key[0] = k.toString();
      f.positionCursor(key);
      f.positionCursorToNext();
      Record r = f.readPreviousEqual();
      Record match = fchk.read(key);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        fchk.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        fchk.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
      fchk.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul compact>
   *<li>null is returned when readNextEqual() results in no match.  I.e
   *there is no record after the current record which has a key that matches the
   *current record's key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var027()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      // There are two more records that match this key.  Get to the last one
      f.readNext();
      f.readNext();
      Record r = f.readNextEqual();
      if (r != null)
      {
        failed("null record not returned: ");
        System.out.println(r);
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual().
   *<ul compact>
   *<li>null is returned when readPreviousEqual() results in no match.  I.e
   *there is no record before the current record which has a key that matches the
   *current record's key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var028()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readLast();
      Record r = f.readPreviousEqual();
      if (r != null)
      {
        failed("null record not returned: ");
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul compact>
   *<li>null is returned when readNextEqual() is invoked after a readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var029()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readLast();
      Record r = f.readNextEqual();
      if (r != null)
      {
        failed("null record not returned: ");
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual().
   *<ul compact>
   *<li>null is returned when readPreviousEqual() is invoked after a readFirst()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var030()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      Record r = f.readPreviousEqual();
      if (r != null)
      {
        failed("null record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readNextEqual().
   *<ul compact>
   *<li>Cursor not positioned on an active record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5147
   *</ul>
  **/
  public void Var031()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorAfterLast();
      Record r = f.readNextEqual();
      if (!(f.getBlockingFactor() > 1 && r == null))
      {
        failed("No exception and r != null: " + r.toString());
        f.close();
        return;
      }
    }
    catch(AS400Exception e)
    {
      if (!e.getAS400Message().getID().equals("CPF5147"))
      {
        failed(e, "Incorrect exception.");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file.");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readPreviousEqual().
   *<ul compact>
   *<li>Cursor not positioned on an active record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5147
   *</ul>
  **/
  public void Var032()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorBeforeFirst();
      Record r = f.readPreviousEqual();
      if (!(f.getBlockingFactor() > 1 && r == null))
      {
        failed("No exception and r != null: " + r.toString());
        f.close();
        return;
      }
    }
    catch(AS400Exception e)
    {
      if (!e.getAS400Message().getID().equals("CPF5147"))
      {
        failed(e, "Incorrect exception.");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file.");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readNextEqual().
   *<ul compact>
   *<li>File not open
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var033()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      Record r = f.readNextEqual();
      failed("No exception");
      f.close();
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException",
                       ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Incorrect exception");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readPreviousEqual().
   *<ul compact>
   *<li>File not open
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var034()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      Record r = f.readPreviousEqual();
      failed("No exception");
      f.close();
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException",
                       ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Incorrect exception");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>readNextEqual(key) using partial key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned 
   *</ul>
  **/
  public void Var035()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      Record match = dupKey3Recs_[3];
      
      Object[] key = new Object[2];
      key[0] = "Record 1   ";
      key[1] = new Integer(1);
      Record r = f.readNextEqual(key);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>readNextEqual(key) with full key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned
   *</ul>
  **/
  public void Var036()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      Record match = dupKey3Recs_[4];
      
      Object[] key = new Object[3];
      key[0] = "Record 3   ";
      key[1] = new Integer(3);
      key[2] = new BigDecimal("301.1");
      Record r = f.readNextEqual(key);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
      key[2] = new BigDecimal("991.1");
      r = f.read(key);
      if (!r.toString().equals(dupKey3Recs_[7].toString()))
      {
        failed("read(key) record not correct: " + r.toString());
        return;
      }
      r = f.readNextEqual();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>readPreviousEqual(key) with partial key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned
   *</ul>
  **/
  public void Var037()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[1];
      key[0] = "Record 1   ";
      Record match = dupKey3Recs_[3];
      f.readNext();
      f.readNext();
      f.readNext();
      Record r = f.readPreviousEqual(key);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>readPreviousEqual(key) with full key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned
   *</ul>
  **/
  public void Var038()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[3];
      key[0] = "Record 1   ";
      key[1] = new Integer(1);
      key[2] = new BigDecimal("1.1");
      f.positionCursorToNext();
      f.positionCursorToNext();
      f.positionCursorToNext();
      Record r = f.readPreviousEqual(key);
      Record match = dupKey3Recs_[0];
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>null is returned when readNextEqual(key) results in no match.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var039()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[1];
      key[0] = "blah";
      Record r = f.readNextEqual(key);
      if (r != null)
      {
        failed("null record not returned: ");
        System.out.println(r);
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>null is returned when readPreviousEqual(key) results in no match.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var040()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readLast();
      Object[] key = new Object[2];
      key[0] = "Record 1   ";
      key[1] = new Integer(12);
      Record r = f.readPreviousEqual(key);
      if (r != null)
      {
        failed("null record not returned: ");
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>null is returned when readNextEqual(key) is invoked after a readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var041()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readLast();
      Object[] key = new Object[1];
      key[0] = "Record 1   ";
      Record r = f.readNextEqual(key);
      if (r != null)
      {
        failed("null record not returned: ");
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>null is returned when readPreviousEqual(key) is invoked after a readFirst()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var042()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      Object[] key = new Object[1];
      key[0] = "Record 5   ";
      Record r = f.readPreviousEqual(key);
      if (r != null)
      {
        failed("null record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul compact>
   *<li>Cursor positioned before first; key in file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record is returned.
   *</ul>
  **/
  public void Var043()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] key = new Object[1];
      key[0] = "Record 2   ";
      Record r = f.readNextEqual(key);
      Record match = dupKey3Recs_[2];
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>Cursor positioned after last record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record is returned
   *</ul>
  **/
  public void Var044()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorAfterLast();
      Object[] key = new Object[1];
      key[0] = "Record 3   ";
      Record r = f.readPreviousEqual(key);
      Record match = dupKey3Recs_[7];
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readNextEqual(key).
   *<ul compact>
   *<li>File not open
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var045()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      Record r = f.readNextEqual(new Object[] { "" });
      failed("No exception");
      f.close();
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException",
                       ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Incorrect exception");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>File not open
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var046()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      Record r = f.readPreviousEqual(new Object[] { "" });
      failed("No exception");
      f.close();
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException",
                       ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Incorrect exception");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readNextEqual(key).
   *<ul compact>
   *<li>Cursor positioned after last record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5147
   *</ul>
  **/
  public void Var047()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorAfterLast();
      Record r = f.readNextEqual(new Object[] { "" });
      if (r != null)
      {
        failed("r != null: " + r.toString());
        f.close();
        return;
      }
    }
    catch(AS400Exception e)
    {
      failed(e, "Unexpected exception.");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file.");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>Cursor positioned before first record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5147
   *</ul>
  **/
  public void Var048()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_,                         "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorBeforeFirst();
      Record r = f.readPreviousEqual(new Object[] { "" });
      if (r != null)
      {
        failed("r != null: " + r.toString());
        f.close();
        return;
      }
    }
    catch(AS400Exception e)
    {
      failed(e, "Unexpected exception.");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file.");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of read(key).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke read(key).
   *<li>Invoke read(key) again with the same key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var049()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[0].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[0].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(key, 1);
        rec2 = file.read(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(key).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke read(key).
   *<li>Invoke read(key) again with the same key.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  **/
  public void Var050()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[0].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[0].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(key, 1);
        rec2 = file.read(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec1: "+rec1.toString()+"\n");
        failMsg.append("rec2: "+rec2.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(key, searchType).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke read(key, KEY_EQ).
   *<li>Invoke read(key, KEY_LT).
   *<li>Invoke read(key, KEY_LE).
   *<li>Invoke read(key, KEY_GT).
   *<li>Invoke read(key, KEY_GE).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The record before the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *<li>The record after the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var051()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record match = records_[3];
      Record before = records_[2];
      Record after = records_[4];
      try
      {
        rec = file.read(key, KeyedFile.KEY_EQ, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_EQ) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (EQ) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LT, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records (LT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LE, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (LE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GT, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records (GT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GE, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (GE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(key, searchType).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke read(key, KEY_EQ).
   *<li>Invoke read(key, KEY_LT).
   *<li>Invoke read(key, KEY_LE).
   *<li>Invoke read(key, KEY_GT).
   *<li>Invoke read(key, KEY_GE).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record matching key will be returned.
   *<li>The record before the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *<li>The record after the record matching key will be returned.
   *<li>The first record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var052()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record match = records_[3];
      Record before = records_[2];
      Record after = records_[4];
      try
      {
        rec = file.read(key, KeyedFile.KEY_EQ, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_EQ) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (EQ) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LT, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records (LT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_LE, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_LE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (LE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GT, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GT) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records (GT) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GE, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key,KEY_GE) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(match.toString()))
      {
        failMsg.append("records (GE) don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("match: "+match.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readAfter(key).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readAfter(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var053()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record after = records_[4];
      try
      {
        rec = file.readAfter(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readAfter(key).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke readAfter(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var054()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record after = records_[4];
      try
      {
        rec = file.readAfter(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readBefore(key).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readBefore(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var055()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record before = records_[2];
      try
      {
        rec = file.readBefore(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readBefore(key).
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke readBefore(key).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before the record matching key will be returned.
   *</ul>
   *</ul>
  **/
  public void Var056()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record before = records_[2];
      try
      {
        rec = file.readBefore(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key).
   *<ul>
   *<li>Invoke read(key) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var057()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.read(key, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key, searchType).
   *<ul>
   *<li>Invoke read(key, searchType) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var058()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.read(key, KeyedFile.KEY_LT, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readAfter(key).
   *<ul>
   *<li>Invoke readAfter(key) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var059()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.readAfter(key, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readBefore(key).
   *<ul>
   *<li>Invoke readBefore(key) prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var060()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));

//      Object[] key = new Object[1];
//      key[0] = records_[3].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[3].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();
      
      Record rec = new Record();
      try
      {
        rec = file.readBefore(key, 1);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var061()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[0];
      byte[] key = new byte[0];

      Record rec = new Record();
      try
      {
        rec = file.read(null);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.read(key, 1);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key, searchType).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var062()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[0];
      byte[] key = new byte[0];

      Record rec = new Record();
      try
      {
        rec = file.read(null, KeyedFile.KEY_LE, 1);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.read(key, KeyedFile.KEY_GT, 1);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readAfter(key).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var063()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[0];
      byte[] key = new byte[0];

      Record rec = new Record();
      try
      {
        rec = file.readAfter(null, 1);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.readAfter(key, 1);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of readBefore(key).
   *<ul>
   *<li>Specify null for the key
   *<li>Specify an array of length 0 for the key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "key" for the text.
   *<li>ExtendedIllegalArgumentException indicating "key"  and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var064()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[0];
      byte[] key = new byte[0];

      Record rec = new Record();
      try
      {
        rec = file.readBefore(null, 1);
        failMsg.append("Expected null pointer exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "key"))
        {
          failMsg.append("Not null pointer exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.readBefore(key, 1);
        failMsg.append("Expected illegal argument exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "key",
                                 ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
        {
          failMsg.append("Not illegal argument exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify invalid usage of read(key, searchType).
   *<ul>
   *<li>Specify an invalid searchType.
   *<ul compact>
   *<li>searchType < 0
   *searchType > 4
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "searchType" and PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var065()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[5].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[5].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.read(key, -1, 1);
        failMsg.append("Expected searchtype too small exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }

      try
      {
        rec = file.read(key, 5, 1);
        failMsg.append("Expected searchtype too big exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "searchType",
                                 ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
        {
          failMsg.append("Incorrect exception info.\n");
          e.printStackTrace(output_);
        }
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that null is returned when trying to read(key) with key that does
   *not exist in the file.
   *<ul compact>
   *<li>Invoke read(key) with a key value that does not exist in the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var066()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = "RECORD10  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("RECORD10  "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.read(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (rec != null)
      {
        failMsg.append("null not returned on non-existent key read: "+rec.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that the first record before <i>key</i> in the file is returned
   *when trying to read(key, KEY_LE) with key that does
   *not exist in the file for which there are records with keys that would be
   *before the key specified.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The appropriate record is returned.
   *</ul>
  **/
  public void Var067()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = "RECORD 99 ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("RECORD 99 "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record before = records_[8];
      try
      {
        rec = file.read(key, KeyedFile.KEY_LE, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(before.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("before: "+before.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that the first record after <i>key</i> in the file is returned
   *when trying to read(key, KEY_GE) with key that does
   *not exist in the file for which there are records with keys that would be
   *after the key specified.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The appropriate record is returned.
   *</ul>
  **/
  public void Var068()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = "RECORD 0  ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("RECORD 0  "), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      Record after = records_[0];
      try
      {
        rec = file.read(key, KeyedFile.KEY_GE, 1);
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec.toString().equals(after.toString()))
      {
        failMsg.append("records don't match.\n");
        failMsg.append("rec: "+rec.toString()+"\n");
        failMsg.append("after: "+after.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that null is returned when readAfter(key) is specified and no record
   *exists after the record matching <i>key</i>.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var069()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[8].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[8].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.readAfter(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (rec != null)
      {
        failMsg.append("null not returned on readAfter: "+rec.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }

  /**
   *Verify that null is returned when readBefore(key) is specified and no record
   *exists before the record matching <i>key</i>.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>null is returned for the record.
   *</ul>
  **/
  public void Var070()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_ALL);
//      Object[] key = new Object[1];
//      key[0] = records_[0].getField(0);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes((String)records_[0].getField(0)), 0, 10);
      byte[] key = keyAsBytes.toByteArray();

      Record rec = new Record();
      try
      {
        rec = file.readBefore(key, 1);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore(key) failed.\n");
        e.printStackTrace(output_);
      }
      if (rec != null)
      {
        failMsg.append("null not returned on readBefore: "+rec.toString()+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
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

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed(failMsg.toString());
    }
  }


  /**
   *Verify valid usage of readPreviousEqual().
   *<ul compact>
   *<li>read(key), readNextEqual() then readPreviousEqual() where
   *key is a key that has duplicates within the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned from read(key) will be returned from
   *readPreviousEqual()
   *</ul>
  **/
  public void Var071()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = new Object[1];
      StringBuffer k = new StringBuffer();
      k.append("C");
      for (int i = 0; i < 10; ++i)
      {
        k.append(" ");
      }
//      key[0] = k.toString();

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes(k.toString()), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record match = f.read(key, 1);
      f.readNextEqual();
      Record r = f.readPreviousEqual();
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual().
   *<ul compact>
   *<li>positionCursor(key), positionCursorToNext() then readPreviousEqual() where
   *key is a key that has duplicates within the file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will have field 0 = "C", field 1 = "B", field 2 = "C"
   *</ul>
  **/
  public void Var072()
  {
    KeyedFile f = null;
    KeyedFile fchk = null;
    try
    {
      fchk  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      fchk.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      fchk.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY2.FILE");
      f.setRecordFormat(new DDMFormat3Field1Key(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = new Object[1];
      StringBuffer k = new StringBuffer();
      k.append("C");
      for (int i = 0; i < 10; ++i)
      {
        k.append(" ");
      }
//      key[0] = k.toString();

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes(k.toString()), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      f.positionCursor(key, 1);
      f.positionCursorToNext();
      Record r = f.readPreviousEqual();
      Record match = fchk.read(key, 1);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        fchk.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
        fchk.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
      fchk.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }


  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>readNextEqual(key) using partial key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned 
   *</ul>
  **/
  public void Var073()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      Record match = dupKey3Recs_[3];
      
//      Object[] key = new Object[2];
//      key[0] = "Record 1   ";
//      key[1] = new Integer(1);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1   "), 0, 11);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(1)), 0, 4);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readNextEqual(key, 2);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>readNextEqual(key) with full key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned
   *</ul>
  **/
  public void Var074()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
      Record match = dupKey3Recs_[4];
      
//      Object[] key = new Object[3];
//      key[0] = "Record 3   ";
//      key[1] = new Integer(3);
//      key[2] = new BigDecimal("301.1");

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 3   "), 0, 11);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(3)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15, 5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("301.1")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readNextEqual(key, 3);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
//      key[2] = new BigDecimal("991.1");
      byte[] newKey = new byte[key.length];
      System.arraycopy(key, 0, newKey, 0, 15);
      System.arraycopy(decimal.toBytes(new BigDecimal("991.1")), 0, newKey, 15, decimal.getByteLength());

      r = f.read(newKey, 3);
      if (!r.toString().equals(dupKey3Recs_[7].toString()))
      {
        failed("read(key) record not correct: " + r.toString());
        return;
      }
      r = f.readNextEqual();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>readPreviousEqual(key) with partial key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned
   *</ul>
  **/
  public void Var075()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = new Object[1];
//      key[0] = "Record 1   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1   "), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record match = dupKey3Recs_[3];
      f.readNext();
      f.readNext();
      f.readNext();
      Record r = f.readPreviousEqual(key, 1);
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>readPreviousEqual(key) with full key
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned
   *</ul>
  **/
  public void Var076()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = new Object[3];
//      key[0] = "Record 1   ";
//      key[1] = new Integer(1);
//      key[2] = new BigDecimal("1.1");

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1   "), 0, 11);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(1)), 0, 4);
      AS400PackedDecimal decimal = new AS400PackedDecimal(15, 5);
      keyAsBytes.write(decimal.toBytes(new BigDecimal("1.1")), 0, decimal.getByteLength());
      byte[] key = keyAsBytes.toByteArray();

      f.positionCursorToNext();
      f.positionCursorToNext();
      f.positionCursorToNext();
      Record r = f.readPreviousEqual(key, 3);
      Record match = dupKey3Recs_[0];
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>null is returned when readNextEqual(key) results in no match.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var077()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = new Object[1];
//      key[0] = "blah";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("blah"), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readNextEqual(key, 1);
      if (r != null)
      {
        failed("null record not returned: ");
        System.out.println(r);
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>null is returned when readPreviousEqual(key) results in no match.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var078()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readLast();
//      Object[] key = new Object[2];
//      key[0] = "Record 1   ";
//      key[1] = new Integer(12);

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1   "), 0, 11);
      AS400Bin4 bin4 = new AS400Bin4();
      keyAsBytes.write(bin4.toBytes(new Integer(12)), 0, 4);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readPreviousEqual(key, 2);
      if (r != null)
      {
        failed("null record not returned: ");
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual(key).
   *<ul compact>
   *<li>null is returned when readNextEqual(key) is invoked after a readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var079()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readLast();
//      Object[] key = new Object[1];
//      key[0] = "Record 1   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 1   "), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readNextEqual(key, 1);
      if (r != null)
      {
        failed("null record not returned: ");
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>null is returned when readPreviousEqual(key) is invoked after a readFirst()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record returned will equal null.
   *</ul>
  **/
  public void Var080()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.readFirst();
//      Object[] key = new Object[1];
//      key[0] = "Record 5   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 5   "), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readPreviousEqual(key, 1);
      if (r != null)
      {
        failed("null record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readNextEqual().
   *<ul compact>
   *<li>Cursor positioned before first; key in file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record is returned.
   *</ul>
  **/
  public void Var081()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
//      Object[] key = new Object[1];
//      key[0] = "Record 2   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 2   "), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readNextEqual(key, 1);
      Record match = dupKey3Recs_[2];
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>Cursor positioned after last record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Record is returned
   *</ul>
  **/
  public void Var082()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorAfterLast();
//      Object[] key = new Object[1];
//      key[0] = "Record 3   ";

      ByteArrayOutputStream keyAsBytes = new ByteArrayOutputStream();
      AS400Text text = new AS400Text(11, systemObject_.getCcsid(), systemObject_);
      keyAsBytes.write(text.toBytes("Record 3   "), 0, 11);
      byte[] key = keyAsBytes.toByteArray();

      Record r = f.readPreviousEqual(key, 1);
      Record match = dupKey3Recs_[7];
      if (!match.toString().equals(r.toString()))
      {
        failed("Expected record not returned: " + r.toString());
        f.close();
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readNextEqual(key).
   *<ul compact>
   *<li>File not open
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var083()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
//      Record r = f.readNextEqual(new Object[1]);
      Record r = f.readNextEqual(new byte[1], 1);
      failed("No exception");
      f.close();
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException",
                       ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Incorrect exception");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>File not open
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating OBJECT_MUST_BE_OPEN
   *</ul>
  **/
  public void Var084()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
//      Record r = f.readPreviousEqual(new Object[1]);
      Record r = f.readPreviousEqual(new byte[1], 1);
      failed("No exception");
      f.close();
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalStateException",
                       ExtendedIllegalStateException.OBJECT_MUST_BE_OPEN))
      {
        failed(e, "Incorrect exception");
        try
        {
          f.close();
        }
        catch(Exception e1)
        {
          failed(e, "Unable to close a file");
          return;
        }
        return;
      }
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readNextEqual(key).
   *<ul compact>
   *<li>Cursor positioned after last record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5147
   *</ul>
  **/
  public void Var085()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorAfterLast();
//      Record r = f.readNextEqual(new Object[1]);
      Record r = f.readPreviousEqual(new byte[1], 1);
      if (r != null)
      {
        failed("r != null: " + r.toString());
        f.close();
        return;
      }
    }
    catch(AS400Exception e)
    {
      failed(e, "Unexpected exception.");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file.");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of readPreviousEqual(key).
   *<ul compact>
   *<li>Cursor positioned before first record.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF5147
   *</ul>
  **/
  public void Var086()
  {
    KeyedFile f = null;
    try
    {
      f  = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/READKEY3.FILE");
      f.setRecordFormat(new DDMFormatReadKey3(systemObject_));
      f.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.positionCursorBeforeFirst();
//      Record r = f.readPreviousEqual(new Object[1]);
      Record r = f.readPreviousEqual(new byte[1], 1);
      if (r != null)
      {
        failed("r != null: " + r.toString());
        f.close();
        return;
      }
    }
    catch(AS400Exception e)
    {
      failed(e, "Unexpected exception.");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file");
        return;
      }
      return;
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        f.close();
      }
      catch(Exception e1)
      {
        failed(e, "Unable to close a file.");
        return;
      }
      return;
    }
    try
    {
      f.close();
    }
    catch(Exception e)
    {
      failed(e, "Unable to close files");
      return;
    }
    succeeded();
  }

}


class DDMFormat3Field1Key extends RecordFormat
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMReadKey";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

  DDMFormat3Field1Key(AS400 sys)
  {
    super("Fld3Key3");
    AS400Text txt = new AS400Text(11, sys.getCcsid(), sys);
    addFieldDescription(new CharacterFieldDescription(txt, "Field1"));
    addFieldDescription(new CharacterFieldDescription(txt, "Field2"));
    addFieldDescription(new CharacterFieldDescription(txt, "Field3"));
    addKeyFieldDescription(0);
  }
}

class DDMFormatReadKey3 extends RecordFormat
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMReadKey";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  static AS400Bin4 bin = new AS400Bin4();
  static AS400ZonedDecimal zon = new AS400ZonedDecimal(15,5);
  static AS400PackedDecimal pac = new AS400PackedDecimal(15,5);

  DDMFormatReadKey3(AS400 sys)
  {
    super("Fld5Key3");
    AS400Text txt = new AS400Text(11, sys.getCcsid(), sys);
    addFieldDescription(new CharacterFieldDescription(txt, "Field1"));
    addFieldDescription(new BinaryFieldDescription(bin, "Field2"));
    addFieldDescription(new ZonedDecimalFieldDescription(zon, "Field3"));
    addFieldDescription(new CharacterFieldDescription(txt, "Field4"));
    addFieldDescription(new PackedDecimalFieldDescription(pac, "Field5"));
    addKeyFieldDescription(0);
    addKeyFieldDescription(1);
    addKeyFieldDescription(4);
  }
}
