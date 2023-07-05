///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSDDMTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.*;
import com.ibm.as400.access.ObjectDescription; ///

/**
 *Testcase NLSDDMTestcase.  This test class verifies the use of DBCS Strings
 *in selected DDM testcase variations.
**/
public class NLSDDMTestcase extends Testcase
{
  long start;
  long time;
//  boolean failed;  // Keeps track of failure in multi-part tests.
//  String msg;      // Keeps track of reason for failure in multi-part tests.
//  CommandCall cmd = null;
  // Blocking factor to be used for opens.  This is used to check
  // that caching acts the same as when we always retrieve from the system.
  // Default indicate to not use caching
  static int bf_ = 1;
  Record[] records_;
  Record[] records2_;
  ///String blanks50_ = "                                                  ";


  // Note:
  //   The minimum actual length of a DBCS string is 4: (1 char = 2 bytes)
  //      SI + upperByte + lowerByte + SO
  //   The numbers below indicate the number of SB chars in the
  //   resource string. E.g. ddm_string4 contains 2 DBCS chars or 4 SBCS chars.

  String ddm_string2 = getResource("DDM_STRING2");
  String ddm_string4 = getResource("DDM_STRING4");
  String ddm_string10 = getResource("DDM_STRING10");
  String ddm_string50 = getResource("DDM_STRING50");
  String ddm_string12 = ddm_string10+ddm_string2;
  String ddm_string48 = ddm_string12+ddm_string12+ddm_string12+ddm_string12;
  String ddm_string10_arr[] = new String[10];

  /**
  Constructor.  This is called from the NLSTest constructor.
  **/
  public NLSDDMTestcase(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream
                      )
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSDDMTestcase", 34,
          variationsToRun, runMode, fileOutputStream);
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    for (int i=0; i<10; i++)
    {
      ddm_string10_arr[i] = getResource("DDM_STRING_ARRAY"+i);
    }

    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      output_.println("Unable to connect to the AS/400");
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
      output_.println("Unable to complete setup; variations not run");
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

    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      output_.println("Unable to complete cleanup.");
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
  // DDMCreateAndAdd setup()
    try
    {
      // Delete NLSDDMT if it exists
      CommandCall c = new CommandCall(NLSTest.PwrSys);
      deleteLibrary(c, "NLSDDMT");

      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191") || msgs[0].getID().equals("CPF2110")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }

      // Create lib NLSDDMT
      c = new CommandCall(systemObject_);
      c.run("CRTLIB LIB(NLSDDMT) AUT(*ALL)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
      String src1 = "                R FMT";
      String src2 = "                  FIELD1       130A";
      SequentialFile f = new SequentialFile(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/%FILE%.MBR");
      String fSrcDBCSDesc = ddm_string10+ddm_string4+ddm_string2;
      try
      {
        f.create(132, "*SRC", fSrcDBCSDesc);
      }
      catch(Exception e)
      {
        output_.println("Unable to create necessary dds source files.");
        e.printStackTrace();
        throw e;
      }
      Record[] r = new Record[2];
      r[0] = f.getRecordFormat().getNewRecord();
      r[0].setField(0, new BigDecimal(1));
      r[0].setField(2, src1);
      r[1] = f.getRecordFormat().getNewRecord();
      r[1].setField(0, new BigDecimal(2));
      r[1].setField(2, src2);
      f.open(AS400File.WRITE_ONLY, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.write(r);
      f.close();
      c.run("CHGPF FILE(NLSDDMT/QDDSSRC) MAXMBRS(*NOMAX)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC7303")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
      c.run("CPYF FROMFILE(NLSDDMT/QDDSSRC) TOFILE(NLSDDMT/QDDSSRC) TOMBR(SRC1) MBROPT(*REPLACE)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2889")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
      c.run("CPYF FROMFILE(NLSDDMT/QDDSSRC) TOFILE(NLSDDMT/QDDSSRC) TOMBR(\"qddssrc\") MBROPT(*REPLACE)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2889")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }

  // DDMReadRN setup()
      {
      // Create the necessary files
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/READRN.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyNoKeyFormat(systemObject_), "NLS, One field, CHAR(10), no key");

      // Create an array of records to write to the files
      records_ = new Record[10];
      StringBuffer fieldValue = new StringBuffer();
      for (short i = 0; i < 10; ++i)
      {
        records_[i] = f1.getRecordFormat().getNewRecord();
        // Blank pad the field to 10; this is necessary for validating
        // what is returned from the AS/400.
        fieldValue.setLength(0);
        fieldValue.append(ddm_string10_arr[i]);
        records_[i].setField(0, fieldValue.toString());
      }

      // Populate the files
      f1.open(AS400File.WRITE_ONLY, 10, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
      records_ = f1.readAll(); //@B0A
      }
/*
  // Extra setup()
      {
      // Create the necessary files
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/DDMTESTSAV.LIB/CHRIS.FILE");
      RecordFormat myRF = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/DDMTESTSAV.LIB/CHRIS.FILE").retrieveRecordFormat()[0];
      f1.setRecordFormat(myRF);

      // Create an array of records to write to the files
      Record[] recordsX_ = new Record[10];
      StringBuffer fieldValue = new StringBuffer();
      for (short i = 0; i < 10; ++i)
      {
        recordsX_[i] = f1.getRecordFormat().getNewRecord();
        // Blank pad the field to 10; this is necessary for validating
        // what is returned from the AS/400.
        fieldValue.setLength(0);
        fieldValue.append(ddm_string10_arr[i]);
        recordsX_[i].setField(1, fieldValue.toString());
      }

      // Populate the files
      f1.open(AS400File.READ_WRITE, 10, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(recordsX_);
      f1.positionCursorToFirst();
      Record myRec = f1.read();
      f1.close();
      }
*/
  // DDMReadKey setup()
      {
      // Create the necessary files
      KeyedFile f1 = new KeyedFile(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/READKEY1.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyKeyFormat(systemObject_), "NLS, One field, CHAR(10), one key");

      // Create an array of records to write to the files
      records2_ = new Record[10];
      RecordFormat rf = f1.getRecordFormat();
      for (short i = 0; i < 10; ++i)
      {
        records2_[i] = rf.getNewRecord();
        records2_[i].setField(0, ddm_string10_arr[i]);
      }

      // Populate the files
      f1.open(AS400File.WRITE_ONLY, 10, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records2_);
      f1.close();
      records2_ = f1.readAll(); //@B0A
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
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
    // QDDSSRC cleanup()
      {
      // Delete the files created during setup()
      //@B0D SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/");
      //@B0D f1.close();
      //@B0D f1.delete();
        CommandCall cc = new CommandCall(systemObject_);
        cc.run("DLTF NLSDDMT/QDDSSRC");
      }


    // DDMReadRN cleanup()
      {
      // Delete the files created during setup()
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/READRN.FILE/MBR1.MBR");
      f1.delete();
      }

    // DDMReadKey cleanup()
      {
      // Delete the files created during setup()
      KeyedFile f1 = new KeyedFile(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/READKEY1.FILE/MBR1.MBR");
      f1.delete();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw e;
    }

    try
    {
	CommandCall c = new CommandCall(systemObject_);
	deleteLibrary("NLSDDMT");

      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  private static String getTextDescription(ObjectDescription objDesc)
    throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
  {
    return (String)objDesc.getValue(ObjectDescription.TEXT_DESCRIPTION);
  }

  private static String getTextDescription(MemberDescription objDesc)
    throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
  {
    return (String)objDesc.getValue(MemberDescription.MEMBER_TEXT_DESCRIPTION);
  }

  /**
   *Verify valid usage of create(int recordLength, String fileType, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 2 (i.e. 1 DBCS character)
   *<li>textDescription = string of length 10
   *<li>textDescription = string of length 50
   *</ul>
  <i>Taken from:</i> DDMCreateAndAdd::Var003
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      file.create(1, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      MemberDescription objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for *BLANK: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create(1, "*DATA", null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for null: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create(1, "*DATA", "");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for empty string: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create(1, "*DATA", ddm_string2);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string2))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string2+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create(1, "*DATA", ddm_string10);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string10))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string10+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create(1, "*DATA", ddm_string50);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR.MBR");
      if(!getTextDescription(objDesc).trim().equals(ddm_string50))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string50+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
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
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of create(String ddsSourceFile, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = *SRCMBRTXT
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 2 (i.e. 1 DBCS character)
   *<li>textDescription = string of length 10
   *<li>textDescription = string of length 50
   *</ul>
  <i>Taken from:</i> DDMCreateAndAdd::Var008
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", "*BLANK");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR").retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      MemberDescription objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for *BLANK: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", "*SRCMBRTXT");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string10+ddm_string4+ddm_string2))
      {
        failMsg.append("  Wrong description for *SRCMBRTXT:\n");
        failMsg.append("    Original: "+ddm_string10+ddm_string4+ddm_string2+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for null: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", "");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for empty string: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string2);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string2))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string2+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string10);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string10))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string10+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string50);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V8.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string50))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string50+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
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
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of create(RecordFormat recordFormat, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 2 (i.e. 1 DBCS character)
   *<li>textDescription = string of length 10
   *<li>textDescription = string of length 50
   *</ul>
  <i>Taken from:</i> DDMCreateAndAdd::Var012
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      RecordFormat format = new DDMFormat1Field0Key(systemObject_);
      file.create(format, "*BLANK");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR").retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      MemberDescription objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for *BLANK: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", null);
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR").retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for null: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", "");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for empty string: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string2);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string2))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string2+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string10);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string10))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string10+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string50);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string50))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string50+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
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
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of create(RecordFormat recordFormat, String textDescription,
   *String altSeq, String ccsid, String order, String ref, boolean unique, String format
   *String text).
   *<br>Specify a variety of values for textDescription:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 2 (i.e. 1 DBCS character)
   *<li>textDescription = string of length 10
   *<li>textDescription = string of length 50
   *</ul>
  <i>Taken from:</i> DDMCreateAndAdd::Var016
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      RecordFormat format = new DDMFormat1Field0Key(systemObject_);
      file.create(format, "*BLANK", null, null, null, null,
                  false, null, null);
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR").retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      MemberDescription objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for *BLANK: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", null);
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR").retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for null: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", "");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for empty string: "+getTextDescription(objDesc)+".\n");

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string2);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string2))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string2+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string10);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string10))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string10+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.close();
      file.delete();
      file.create("/QSYS.LIB/NLSDDMT.LIB/QDDSSRC.FILE/SRC1.MBR", ddm_string50);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V16.FILE/MBR.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string50))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string50+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
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
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of addPhysicalFileMember(String member, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 2 (i.e. 1 DBCS character)
   *<li>textDescription = string of length 10
   *<li>textDescription = string of length 50
   *</ul>
  <i>Taken from:</i> DDMCreateAndAdd::Var022
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/MBR.MBR");
      RecordFormat format = new DDMFormat1Field0Key(systemObject_);
      file.create(1, "*DATA", "*BLANK");

      // Issue a change physical file command to allow more than one member.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("CHGPF FILE(NLSDDMT/"+file.getFileName()+") MAXMBRS(*NOMAX)");

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      file.addPhysicalFileMember("M1", "*BLANK");
      MemberDescription objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/M1.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for *BLANK: "+getTextDescription(objDesc)+".\n");

      file.addPhysicalFileMember("M2", null);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/M2.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for null: "+getTextDescription(objDesc)+".\n");

      file.addPhysicalFileMember("M3", "");
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/M3.MBR");
      if (!getTextDescription(objDesc).equals(""))
        failMsg.append("  Wrong text description for null: "+getTextDescription(objDesc)+".\n");

      file.addPhysicalFileMember("M4", ddm_string2);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/M4.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string2))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string2+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.addPhysicalFileMember("M5", ddm_string10);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/M5.MBR");
      if (!getTextDescription(objDesc).trim().equals(ddm_string10))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string10+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
      }

      file.addPhysicalFileMember("M6", ddm_string50);
      objDesc = new MemberDescription(systemObject_, "/QSYS.LIB/NLSDDMT.LIB/V22.FILE/M6.MBR");
      if (getTextDescription(objDesc) != null)
      if (!getTextDescription(objDesc).trim().equals(ddm_string50))
      {
        failMsg.append("  Wrong text description:\n");
        failMsg.append("    Original: "+ddm_string50+".\n");
        failMsg.append("    Returned: "+getTextDescription(objDesc).trim()+".\n");
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
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
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
  <i>Taken from:</i> DDMReadKey::Var001
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records2_[0].getField(0);
      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(key);
        rec2 = file.read(key);
        if (!rec1.toString().equals(rec2.toString()))
        {
          failMsg.append("records don't match.\n");
          failMsg.append("rec1: "+rec1.toString()+"\n");
          failMsg.append("rec2: "+rec2.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("read(key) failed.\n");
        e.printStackTrace(output_);
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
    catch(Exception e) {}

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
  <i>Taken from:</i> DDMReadKey::Var003
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records2_[3].getField(0);

      Record rec = new Record();
      Record match = records2_[3];
      Record before = records2_[2];
      Record after = records2_[4];

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
        failMsg.append("  Records (EQ) don't match.\n");
        failMsg.append("    rec  : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    match: "+match.toString()+toUni(match.toString())+"\n");
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
        failMsg.append("  Records (LT) don't match.\n");
        failMsg.append("    rec   : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    before: "+before.toString()+toUni(before.toString())+"\n");
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
        failMsg.append("  Records (LE) don't match.\n");
        failMsg.append("    rec  : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    match: "+match.toString()+toUni(match.toString())+"\n");
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
        failMsg.append("  Records (GT) don't match.\n");
        failMsg.append("    rec  : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    after: "+after.toString()+toUni(after.toString())+"\n");
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
        failMsg.append("  Records (GE) don't match.\n");
        failMsg.append("    rec  : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    match: "+match.toString()+toUni(match.toString())+"\n");
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
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS key comparison failure.\n"+failMsg.toString());
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
  <i>Taken from:</i> DDMReadKey::Var005
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records2_[3].getField(0);
      Record rec = new Record();
      Record after = records2_[4];
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
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec  : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    after: "+after.toString()+toUni(after.toString())+"\n");
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
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS key comparison failure.\n"+failMsg.toString());
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
  <i>Taken from:</i> DDMReadKey::Var007
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/READKEY1.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] key = new Object[1];
      key[0] = records2_[3].getField(0);
      Record rec = new Record();
      Record before = records2_[2];
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
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec   : "+rec.toString()+toUni(rec.toString())+"\n");
        failMsg.append("    before: "+before.toString()+toUni(before.toString())+"\n");
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
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS key comparison failure.\n"+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read(recordNumber).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke read(recordNumber).
   *<li>Invoke read(recordNumber) again with the same record number.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The record <i>recordNumber</i> will be returned.
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadRN::Var001
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyNoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = new Record();
      try
      {
        rec1 = file.read(1);
        rec2 = file.read(1);
      }
      catch(Exception e)
      {
        failMsg.append("read() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec1: "+rec1.toString()+toUni(rec1.toString())+"\n");
        failMsg.append("    rec2: "+rec2.toString()+toUni(rec2.toString())+"\n");
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
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS record comparison failure.\n"+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readAfter(recordNumber).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readAfter(recordNumber).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record after record <i>recordNumber</i> will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadRN::Var003
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyNoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = null;
      Record rec2 = records_[3];
      try
      {
        rec1 = file.readAfter(3);
      }
      catch(Exception e)
      {
        failMsg.append("readAfter() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec1: "+rec1.toString()+toUni(rec1.toString())+".\n");
        failMsg.append("    rec2: "+rec2.toString()+toUni(rec2.toString())+".\n");
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
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS record comparison failure.\n"+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of readBefore(recordNumber).
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Invoke readBefore(recordNumber).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>The first record before record <i>recordNumber</i> will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadRN::Var005
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/READRN.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyNoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = new Record();
      Record rec2 = records_[3];
      try
      {
        rec1 = file.readBefore(5);
      }
      catch(Exception e)
      {
        failMsg.append("readBefore() failed.\n");
        e.printStackTrace(output_);
      }
      if (!rec1.toString().equals(rec2.toString()))
      {
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec1: "+rec1.toString()+toUni(rec1.toString())+".\n");
        failMsg.append("    rec2: "+rec2.toString()+toUni(rec2.toString())+".\n");
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
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS record comparison failure.\n"+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of read() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position cursor to the first record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to the last record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to record number 3 and invoke read().
   *<li>Invoke read() again.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>First record
   *<li>Last record
   *<li>Last record
   *<li>Record number three
   *<li>Record number three
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var001
  **/
  public void Var013()
  {
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V1.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec" + ddm_string4 + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
      {
        record = file.read();
        if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
        {
          file.positionCursorToLast();
          record = file.read();
          if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
          {
            record = file.read();
            if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
            {
              file.positionCursor(3);
              record = file.read();
              if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"3"))
              {
                record = file.read();
                assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"3"));
              }
            }
            else
            {
              failed("Incorrect record read from file.");
            }
          }
          else
          {
            failed("Incorrect record read from file.");
          }
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of read() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position cursor to the first record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to the last record and invoke read().
   *<li>Invoke read() again.
   *<li>Position cursor to record by key and invoke read().
   *<li>Invoke read() again.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>First record
   *<li>Last record
   *<li>Last record
   *<li>Record matching the key
   *<li>Same record
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var003
  **/
  public void Var014()
  {
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/V3.FILE/MBR1.MBR");
      RecordFormat format = new NLSDBCSOnlyKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var003()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec" + ddm_string4 + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      record = file.read();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
      {
        record = file.read();
        if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
        {
          file.positionCursorToLast();
          record = file.read();
          if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
          {
            record = file.read();
            if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
            {
              String[] key = new String[1];
              key[0] = "Rec"+ddm_string4+"3  ";
              file.positionCursor(key);
              record = file.read();
              if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"3"))
              {
                record = file.read();
                assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"3"));
              }
            }
            else
            {
              failed("Incorrect record read from file.");
            }
          }
          else
          {
            failed("Incorrect record read from file.");
          }
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readNext() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<liInvoke readNext().
   *<li>Invoke readNext() again.
   *<li>Position cursor to record number 3 and invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>Second record
   *<li>Fourth record
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var005
  **/
  public void Var015()
  {
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V5.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec" + ddm_string4 + Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
      {
        record = file.readNext();
        if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"2"))
        {
          file.positionCursor(3);
          record = file.readNext();
          assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readNext() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<liInvoke readNext().
   *<li>Invoke readNext() again.
   *<li>Position cursor to a record by key and invoke readNext().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>First record
   *<li>Second record
   *<li>Record after the record matching the key
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var007
  **/
  public void Var016()
  {
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/V7.FILE/MBR1.MBR");
      RecordFormat format = new NLSDBCSOnlyKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var007)");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readNext();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
      {
        record = file.readNext();
        if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"2"))
        {
          String[] key = new String[1];
          key[0] = "Rec"+ddm_string4+"3  ";
          file.positionCursor(key);
          record = file.readNext();
          assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readPrevious() using a SequentialFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position the cursor to after the last record.
   *<liInvoke readPrevious().
   *<li>Invoke readPrevious() again.
   *<li>Position cursor to record number 3 and invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>Last record
   *<li>Second to last record
   *<li>Second record
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var009
  **/
  public void Var017()
  {
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V9.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
      {
        record = file.readPrevious();
        if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"3"))
        {
          file.positionCursor(3);
          record = file.readPrevious();
          assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"2"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readPrevious() using a KeyedFile object.
   *<ul>
   *<li>Open file for READ_ONLY.
   *<li>Position the cursor to after the last record.
   *<liInvoke readPrevious().
   *<li>Invoke readPrevious() again.
   *<li>Position cursor to a record by key and invoke readPrevious().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be returned in all cases.
   *<ul compact>
   *<li>Last record
   *<li>Second to last record
   *<li>Record before the record matching the key
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var011
  **/
  public void Var018()
  {
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/V11.FILE/MBR1.MBR");
      RecordFormat format = new NLSDBCSOnlyKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var011()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorAfterLast();
      record = file.readPrevious();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
      {
        record = file.readPrevious();
        if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"3"))
        {
          String[] key = new String[1];
          key[0] = "Rec"+ddm_string4+"3  ";
          file.positionCursor(key);
          record = file.readPrevious();
          assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"2"));
        }
        else
        {
          failed("Incorrect record read from file.");
        }
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readFirst() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readFirst().
   *<li>Position the cursor to the last record.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The first record will be returned.
   *<li>The first record will be returned.
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var013
  **/
  public void Var019()
  {
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V10.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readFirst();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
      {
        file.positionCursorToLast();
        record = file.readFirst();
        assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readFirst() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readFirst().
   *<li>Position the cursor to the last record.
   *<li>Invoke readFirst().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The first record will be returned.
   *<li>The first record will be returned.
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var015
  **/
  public void Var020()
  {
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/V15.FILE/MBR1.MBR");
      RecordFormat format = new NLSDBCSOnlyKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var015()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readFirst();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"))
      {
        file.positionCursorToLast();
        record = file.readFirst();
        assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"1"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readLast() using a SequentialFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readLast().
   *<li>Position the cursor to the first record.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The last record will be returned.
   *<li>The last record will be returned.
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var017
  **/
  public void Var021()
  {
    SequentialFile file = null;
    try
    {
      // Create a file having four records.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/V17.FILE/MBR1.MBR");
      file.create(132, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readLast();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
      {
        file.positionCursorToFirst();
        record = file.readLast();
        assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of readLast() using a KeyedFile object.
   *<ul compact>
   *<li>Open the file for READ_ONLY.
   *<li>Invoke readLast().
   *<li>Position the cursor to the first record.
   *<li>Invoke readLast().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The last record will be returned.
   *<li>The last record will be returned.
   *</ul>
  <i>Taken from:</i> DDMReadSeq::Var019
  **/
  public void Var022()
  {
    KeyedFile file = null;
    try
    {
      // Create a file having four records.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/V19.FILE/MBR1.MBR");
      RecordFormat format = new NLSDBCSOnlyKeyFormat(systemObject_);
      file.create(format, "DDMReqSeq.Var019()");
      file.open(AS400File.READ_WRITE, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record record = file.getRecordFormat().getNewRecord();
      for (int i = 1; i <= 4; i++)
      {
        record.setField(0, "Rec"+ddm_string4+ Integer.toString(i));
        file.write(record);
      }
      file.close();

      file.open(AS400File.READ_ONLY, bf_, AS400File.COMMIT_LOCK_LEVEL_NONE);
      record = file.readLast();
      if (((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"))
      {
        file.positionCursorToFirst();
        record = file.readLast();
        assertCondition(((String) record.getField(0)).startsWith("Rec"+ddm_string4+"4"));
      }
      else
      {
        failed("Incorrect record read from file.");
      }
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
  }

  /**
   *Verify valid usage of update(record) on a SequentialFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position the cursor to the first record
   *<li>Invoke update(record).
   *<li>Invoke read() and verify the update.
   *<li>Invoke readLast().
   *<li>Invoke update(record)
   *<li>Invoke readLast() and verify the update
   *<li>Position the cursor to a record in the middle of the file by
   *record number.
   *<li>Invoke update(record)
   *<li>Invoke read(recordNumber) on the record updated and verify
   *the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The records will be updated
   *<ul compact>
   *<li>The first record will be updated
   *<li>The last record will be updated.
   *<li>The record accessed by record number will be updated.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMUpdate::Var001
  **/
  public void Var023()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/NLSDDMT.LIB/UPDATE1.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyNoKeyFormat(systemObject_), "NLS, One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Rec00" + String.valueOf(i)+ddm_string4);
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var001.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/UPDATE1.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyNoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE " + ddm_string2 + "1");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.update(rec);
      Record verify = file.read();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on first record: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      file.readLast();
      file.update(rec);
      verify = file.readLast();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on last record: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      file.positionCursor(4);
      file.update(rec);
      verify = file.read(4);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on middle record: "+verify.toString()+toUni(verify.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(record) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Position the cursor to the first record
   *<li>Invoke update(record).
   *<li>Invoke read() and verify the update.
   *<li>Invoke readLast().
   *<li>Invoke update(record)
   *<li>Invoke readLast() and verify the update
   *<li>Position the cursor to a record in the middle of the file by key.
   *<li>Invoke update(record)
   *<li>Invoke read(key) on the record updated and verify
   *the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The records will be updated
   *<ul compact>
   *<li>The first record will be updated
   *<li>The last record will be updated.
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMUpdate::Var002
  **/
  public void Var024()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/NLSDDMT.LIB/UPDATE2.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyKeyFormat(systemObject_), "NLS, One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Rec00" + String.valueOf(i)+ddm_string4);
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var002.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/UPDATE2.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE "+ddm_string2+"2");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.positionCursorToFirst();
      file.update(rec);
      file.close();
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record verify = file.readLast();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on first record: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      file.readLast();
      file.update(rec);
      verify = file.readLast();
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on last record: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      Object[] key = new Object[1];
      key[0] = "Rec004"+ddm_string4;
      file.positionCursor(key);
      file.update(rec);
      key[0] = "UPDATE "+ddm_string2+"2";
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update() failed on middle record: "+verify.toString()+toUni(verify.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(key, record) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(key, record).
   *<li>Invoke read(key) and verify the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMUpdate::Var011
  **/
  public void Var025()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/NLSDDMT.LIB/UPDATE11.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyKeyFormat(systemObject_), "NLS, One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Rec00" + String.valueOf(i)+ddm_string4);
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var011.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/UPDATE11.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE "+ddm_string2+"1");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] key = new Object[1];
      key[0] = "Rec005"+ddm_string4;
      file.update(key, rec);
      key[0] = "UPDATE "+ddm_string2+"1";
      Record verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(rec, key) failed: "+verify.toString()+toUni(verify.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(key, record, searchType) on a KeyedFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(key, record, searchType), testing all search types.
   *<li>Invoke read(key) and verify the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by key will be updated.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMUpdate::Var018
  **/
  public void Var026()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    // Create file for use in this variation
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_,
                                   "/QSYS.LIB/NLSDDMT.LIB/UPDATE18.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyKeyFormat(systemObject_), "NLS, One field, CHAR(10), key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Rec00" + String.valueOf(i)+ddm_string4);
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var018.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/NLSDDMT.LIB/UPDATE18.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE"+ddm_string2+"EQ");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);

      Object[] key = new Object[1];
      key[0] = "Rec003"+ddm_string4;
      file.update(key, rec, KeyedFile.KEY_EQ);
      key[0] = rec.getField(0);
      Record verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_EQ) failed: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      key[0] = "Rec004"+ddm_string4;
      rec.setField(0, "UPDATE"+ddm_string2+"GE");
      file.update(key, rec, KeyedFile.KEY_GE);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_GE) failed: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      key[0] = "Rec005"+ddm_string4;
      rec.setField(0, "UPDATE"+ddm_string2+"GT");
      file.update(key, rec, KeyedFile.KEY_GT);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_GT) failed: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      key[0] = "Rec006"+ddm_string4;
      rec.setField(0, "UPDATE"+ddm_string2+"LE");
      file.update(key, rec, KeyedFile.KEY_LE);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_LE) failed: "+verify.toString()+toUni(verify.toString())+"\n");
      }

      key[0] = "Rec007"+ddm_string4;
      rec.setField(0, "UPDATE"+ddm_string2+"LT");
      file.update(key, rec, KeyedFile.KEY_LT);
      key[0] = rec.getField(0);
      verify = file.read(key);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(KEY_LT) failed: "+verify.toString()+toUni(verify.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of update(recordNumber, record) on a SequentialFile.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke update(recordNumber, record).
   *<li>Invoke read(recordNumber) on the record updated and verify
   *the update.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record will be updated.
   *<ul compact>
   *<li>The record accessed by record number will be updated.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMUpdate::Var026
  **/
  public void Var027()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // Create file for use in this variation
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_,
                                             "/QSYS.LIB/NLSDDMT.LIB/UPDATE26.FILE/MBR1.MBR");
      f1.create(new NLSDBCSOnlyNoKeyFormat(systemObject_), "NLS, One field, CHAR(10), no key");
      Record[] records_ = new Record[9];
      for (short i = 1; i < 10; ++i)
      {
        records_[i-1] = f1.getRecordFormat().getNewRecord();
        records_[i-1].setField(0, "Rec00" + String.valueOf(i)+ddm_string4);
      }
      f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f1.write(records_);
      f1.close();
    }
    catch(Exception e)
    {
      failMsg.append("Unable to complete setup for Var026.\n");
      e.printStackTrace(output_);
    }

    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/UPDATE26.FILE/MBR1.MBR");
      file.setRecordFormat(new NLSDBCSOnlyNoKeyFormat(systemObject_));
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "UPDATE "+ddm_string2+"6");

      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.update(4, rec);
      Record verify = file.read(4);
      if (!verify.toString().equals(rec.toString()))
      {
        failMsg.append("update(4) failed: "+verify.toString()+toUni(verify.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of write(Record) on a Sequential File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMWrite::Var001
  **/
  public void Var028()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/WRITEV1.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Rec "+ddm_string4+" 1");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec1: "+rec1.toString()+toUni(rec1.toString())+"\n");
        failMsg.append("    rec2: "+rec2.toString()+toUni(rec2.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMWrite::Var003
  **/
  public void Var029()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/WRITEV3.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Rcrd "+ddm_string4 + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("  Mismatch: "+rec2[i-1].toString()+toUni(rec2[i-1].toString())+" != \n");
          failMsg.append("            "+recs[i-1].toString()+toUni(recs[i-1].toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS write failure."+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of write(Record[]) on a Sequential File.
   *<ul>
   *<li>Open file, specifying blocking factor = 1.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMWrite::Var011
  **/
  public void Var030()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/WRITEV11.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Rcrd "+ddm_string4 + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("  Mismatch: "+rec2[i-1].toString()+toUni(rec2[i-1].toString())+" != \n");
          failMsg.append("            "+recs[i-1].toString()+toUni(recs[i-1].toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS write failure."+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of write(Record) on a Keyed File.
   *<ul>
   *<li>Open file for READ_WRITE.
   *<li>Invoke write(Record).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMWrite::Var019
  **/
  public void Var031()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/WRITEK2.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec1 = file.getRecordFormat().getNewRecord();
      rec1.setField(0, "Rec "+ddm_string4+" 2");
      try
      {
        file.write(rec1);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec2 = file.readFirst();

      if (!rec2.toString().equals(rec1.toString()))
      {
        failMsg.append("  Records don't match.\n");
        failMsg.append("    rec1: "+rec1.toString()+toUni(rec1.toString())+"\n");
        failMsg.append("    rec2: "+rec2.toString()+toUni(rec2.toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

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
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file for WRITE_ONLY.
   *<li>Invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct records will be written.
   *<ul compact>
   *<li>The same records will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMWrite::Var020
  **/
  public void Var032()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/WRITEK3.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Rcrd "+ddm_string4 + Integer.toString(i));
      }
      try
      {
        file.write(recs);
      }
      catch(Exception e)
      {
        failMsg.append("write([]) failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] rec2 = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!rec2[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("  Mismatch: "+rec2[i-1].toString()+toUni(rec2[i-1].toString())+" != \n");
          failMsg.append("            "+recs[i-1].toString()+toUni(recs[i-1].toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS write failure."+failMsg.toString());
    }
  }

  /**
   *Verify valid usage of write(Record[]) on a Keyed File.
   *<ul>
   *<li>Open file with pre-existing records, invoke write(Record[]).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The correct record will be written.
   *<ul compact>
   *<li>The same record will be returned.
   *</ul>
   *</ul>
  <i>Taken from:</i> DDMWrite::Var034
  **/
  public void Var033()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/NLSDDMT.LIB/WRITEK17.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Populate file
      Record[] recs = new Record[9];
      RecordFormat rf = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        recs[i-1] = rf.getNewRecord();
        recs[i-1].setField(0, "Rcrd "+ddm_string4 + Integer.toString(i));
      }
      file.write(recs);
      file.close();

      // Write extra records
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record[] extras = new Record[9];
      RecordFormat rf2 = file.getRecordFormat();
      for (int i = 1; i <= 9; i++)
      {
        extras[i-1] = rf2.getNewRecord();
        extras[i-1].setField(0, "RECORD " + Integer.toString(i)+ddm_string2);
      }
      try
      {
        file.write(extras);
      }
      catch(Exception e)
      {
        failMsg.append("write() failed.\n");
        e.printStackTrace(output_);
      }
      file.close();

      Record[] allrecs = file.readAll();
      for (int i = 1; i<= 9; i++)
      {
        if (!allrecs[i-1].toString().equals(recs[i-1].toString()))
        {
          failMsg.append("  Mismatch recs: "+allrecs[i-1].toString()+toUni(allrecs[i-1].toString())+" != \n");
          failMsg.append("                 "+recs[i-1].toString()+toUni(recs[i-1].toString())+"\n");
        }
        if (!allrecs[i+8].toString().equals(extras[i-1].toString()))
        {
          failMsg.append("  Mismatch extras: "+allrecs[i+8].toString()+toUni(allrecs[i+8].toString())+" != \n");
          failMsg.append("                   "+extras[i-1].toString()+toUni(extras[i-1].toString())+"\n");
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
      file.delete();
    }
    catch(Exception e) {}

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS write failure."+failMsg.toString());
    }
  }


  /**
  * Test NLS characters in the following way, assuming DBCS signon:
  *<ul>
  *<li>Retrieve the record format for the file.
  *<li>Read both records and verify their contents.
  *<li>Write the records back as new records in the file, verify contents.
  *<li>Update record 4 with new data, verify contents.
  *<li>Delete the records.
  *</ul>
  **/
  public void Var034()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    // The sign-on names look like JAVA??? where ??? is JAP, KOR, TCH, or SCH
    // for the double byte languages.
    String specificName = systemObject_.getUserId().substring(4);
    if (!specificName.equals("JAP") &&
        !specificName.equals("KOR") &&
        !specificName.equals("TCH") &&
        !specificName.equals("SCH"))
    {
      notApplicable();
      output_.println("  Var34 was not attempted: not using DBCS signon: "+systemObject_.getUserId());
      return;
    }

    try
    {
      // Get the "real" strings from the property file.
      String dbcsopen_1 = getResource(specificName+"OPEN_1");
      String dbcsgraph_1 = getResource(specificName+"GRAPH_1");
      String dbcsonly_1 = getResource(specificName+"ONLY_1");
      String dbcseither_1 = getResource(specificName+"EITHER_1");
      String dbcsopen_2 = getResource(specificName+"OPEN_2");
      String dbcsgraph_2 = getResource(specificName+"GRAPH_2");
      String dbcsonly_2 = getResource(specificName+"ONLY_2");
      String dbcseither_2 = getResource(specificName+"EITHER_2");

      // The filename is based on the language.
      // It looks like this: SMPDBCS??? where ??? is JAP, KOR, TCH or SCH.
      String dbcsFile = "/QSYS.LIB/JAVANLS.LIB/SMPDBCS"+specificName+".FILE";
      file = new SequentialFile(systemObject_, dbcsFile);
 
      // Set the record format
      if (specificName.equals("JAP"))
        file.setRecordFormat(new NLSDBCSJAPTestingFormat(systemObject_));
      if (specificName.equals("KOR")) 
        file.setRecordFormat(new NLSDBCSKORTestingFormat(systemObject_));
      if (specificName.equals("TCH"))
        file.setRecordFormat(new NLSDBCSTCHTestingFormat(systemObject_));
      if (specificName.equals("SCH")) 
        file.setRecordFormat(new NLSDBCSSCHTestingFormat(systemObject_));
        
      // Get the record format
      RecordFormat dbcsRF = file.getRecordFormat();
        
      // Create an array of records (strings from the property file) to use 
      // for comparison purposes.
      Record[] recs = new Record[2];
      RecordFormat rf = dbcsRF;
      recs[0] = rf.getNewRecord();
      recs[0].setField(0, dbcsopen_1);
      recs[0].setField(1, dbcsgraph_1);
      recs[0].setField(2, dbcsonly_1);
      recs[0].setField(3, dbcseither_1);
      recs[1] = rf.getNewRecord();
      recs[1].setField(0, dbcsopen_2);
      recs[1].setField(1, dbcsgraph_2);
      recs[1].setField(2, dbcsonly_2);
      recs[1].setField(3, dbcseither_2);

      // Read in all the records
      Record[] dbcsRecs = file.readAll();
      if (dbcsRecs.length != 2)
        failMsg.append("  Wrong number of records (read): "+dbcsRecs.length+"\n");

      // Verify contents
      for (int i = 0; i<=1; i++)
      {
        if (!dbcsRecs[i].toString().equals(recs[i].toString()))
        {
          failMsg.append("  Read Mismatch: "+dbcsRecs[i].toString()+toUni(dbcsRecs[i].toString())+" != \n");
          failMsg.append("                 "+recs[i].toString()+toUni(recs[i].toString())+"\n");
        }
      }

      // Write them back as extras
      Record[] extras = new Record[2];

      for (int i = 0; i<=1; i++)
      {
        extras[i] = dbcsRF.getNewRecord();
        for (int j=0; j<dbcsRecs[i].getNumberOfFields(); j++)
        {
          extras[i].setField(j, dbcsRecs[i].getField(j));
        }
      }

      file.open(AS400File.READ_WRITE, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.write(extras);

      // Verify contents
      file.close();
      Record[] dbcsRecs2 = file.readAll();
      if (dbcsRecs2.length != 4)
        failMsg.append("  Wrong number of records (write): "+dbcsRecs2.length+"\n");

      for (int i = 2; i<=3; i++)
      {
        if (!dbcsRecs2[i].toString().equals(recs[i-2].toString()))
        {
          failMsg.append("  Write Mismatch: "+dbcsRecs2[i].toString()+toUni(dbcsRecs2[i].toString())+" != \n");
          failMsg.append("                  "+recs[i-2].toString()+toUni(recs[i-2].toString())+"\n");
        }
      }

      // Update record 4
      int updateNum = dbcsRecs2[dbcsRecs2.length-1].getRecordNumber();
      file.close();
      file.open(AS400File.READ_WRITE, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record newRec = dbcsRF.getNewRecord();
      Object[] recFields = dbcsRecs2[0].getFields();
      for (int j=0; j<recFields.length; j++)
      {
        newRec.setField(j, recFields[j]);
      }
      if (updateNum == 0)
      {
        failMsg.append("Record number was not assigned.\n");
      }
      else
      {
        file.update(updateNum,newRec);
      }

      // Verify contents
      file.close();
      Record[] dbcsRecs3 = file.readAll();
      if (dbcsRecs3.length != 4)
        failMsg.append("  Wrong number of records (update): "+dbcsRecs3.length+"\n");

      if (!dbcsRecs3[3].toString().equals(newRec.toString()))
      {
        failMsg.append("  Update Mismatch: "+dbcsRecs3[3].toString()+toUni(dbcsRecs3[3].toString())+" != \n");
        failMsg.append("                   "+newRec.toString()+toUni(newRec.toString())+"\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    int i=0;
    try
    {
      file.close();
      // Set the record format for cleanup
      if (specificName.equals("JAP"))
        file.setRecordFormat(new NLSDBCSJAPTestingFormat(systemObject_));
      if (specificName.equals("KOR")) 
        file.setRecordFormat(new NLSDBCSKORTestingFormat(systemObject_));
      if (specificName.equals("TCH"))
        file.setRecordFormat(new NLSDBCSTCHTestingFormat(systemObject_));
      if (specificName.equals("SCH")) 
        file.setRecordFormat(new NLSDBCSSCHTestingFormat(systemObject_));
      Record[] dbcsRecs = file.readAll();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      // Delete all records except the first two originals.
      for (i=2; i<dbcsRecs.length; i++)
      {
        file.deleteRecord(dbcsRecs[i].getRecordNumber());
      }
      file.close();
    }
    catch(Exception e)
    {
      output_.println("Cleanup failed: "+i);
      e.printStackTrace(output_);
    }

    if (failMsg.length() == 0)
    {
      succeeded();
    }
    else
    {
      failed("DBCS field failure.\n"+failMsg.toString());
    }
  }




  public String toUni(String i)
  {
    return " Unicode: "+NLSTest.stringToUnicode(i);
  }

  class NLSDBCSOnlyNoKeyFormat extends RecordFormat
  {
    NLSDBCSOnlyNoKeyFormat(AS400 sys)
    {
      super("KEYFMT");
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, sys.getCcsid(), sys), "field1"));
    }
  }

  class NLSDBCSJAPTestingFormat extends RecordFormat
  {

    NLSDBCSJAPTestingFormat(AS400 sys)
    {
      super("SIMPLEDBCS");
      // Add field descriptions to this record format
      addFieldDescription(new DBCSOpenFieldDescription(new AS400Text(10, 5026, sys), "JAP open field"));
      addFieldDescription(new DBCSGraphicFieldDescription(new AS400Text(20, 300, sys), "JAP graphic field"));
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, 5026, sys), "JAP only field"));
      addFieldDescription(new DBCSEitherFieldDescription(new AS400Text(10, 5026, sys), "JAP either field"));
    }
  }

  class NLSDBCSKORTestingFormat extends RecordFormat
  {

    NLSDBCSKORTestingFormat(AS400 sys)
    {
      super("SMPDBCSKOR");
      // Add field descriptions to this record format
      addFieldDescription(new DBCSOpenFieldDescription(new AS400Text(10, 933, sys), "KOR open field"));
      addFieldDescription(new DBCSGraphicFieldDescription(new AS400Text(20, 834, sys), "KOR graphic field"));
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, 933, sys), "KOR only field"));
      addFieldDescription(new DBCSEitherFieldDescription(new AS400Text(10, 933, sys), "KOR either field"));
    }
  }

  class NLSDBCSTCHTestingFormat extends RecordFormat
  {

    NLSDBCSTCHTestingFormat(AS400 sys)
    {
      super("SMPDBCSTCH");
      // Add field descriptions to this record format
      addFieldDescription(new DBCSOpenFieldDescription(new AS400Text(10, 937, sys), "TCH open field"));
      addFieldDescription(new DBCSGraphicFieldDescription(new AS400Text(20, 835, sys), "TCH graphic field"));
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, 937, sys), "TCH only field"));
      addFieldDescription(new DBCSEitherFieldDescription(new AS400Text(10, 937, sys), "TCH either field"));
    }
  }

  class NLSDBCSSCHTestingFormat extends RecordFormat
  {

    NLSDBCSSCHTestingFormat(AS400 sys)
    {
      super("SMPDBCSSCH");
      // Add field descriptions to this record format
      addFieldDescription(new DBCSOpenFieldDescription(new AS400Text(10, 935, sys), "SCH open field"));
      addFieldDescription(new DBCSGraphicFieldDescription(new AS400Text(20, 837, sys), "SCH graphic field"));
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, 935, sys), "SCH only field"));
      addFieldDescription(new DBCSEitherFieldDescription(new AS400Text(10, 935, sys), "SCH either field"));
    }
  }

  class NLSDBCSOnlyKeyFormat extends RecordFormat
  {
    NLSDBCSOnlyKeyFormat(AS400 sys)
    {
      super("KEYFMT");
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10, sys.getCcsid(), sys), "field1"));
      addKeyFieldDescription("field1");
    }
  }
}
