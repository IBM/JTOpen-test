///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMCreateAndAdd.java
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
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.MemberDescription;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

/**
  Testcase DDMCreateAndAdd.  This test class verifies valid and invalid usage of
  the create() methods and the addPhysicalFileMember() method.
**/
public class DDMCreateAndAdd extends Testcase
{
  long start;
  long time;
  ///String blankText_;
  String testLib_ = null;
  
  DDMFormat1Field0Key f1f0_ = null;
  DDMFormat1Field1Key f1f1_ = null;
  DDMFormat3Field0Key f3f0_ = null;
  DDMFormat3Field3Key f3f3_ = null;
  
  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMCreateAndAdd(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         AS400 pwrsys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMCreateAndAdd", /*31,*/ /*27,*/ 28,
          variationsToRun, runMode, fileOutputStream);
///    // Initialize blankText_ to 50 blanks
///    StringBuffer bt = new StringBuffer();
///    for (int i = 0; i < 50; ++i)
///    {
///      bt.append(" ");
///    }
///    blankText_ = bt.toString();
    testLib_ = testLib;
    pwrSys_ = pwrsys;
    f1f0_ = new DDMFormat1Field0Key(systemObject_);
    f1f1_ = new DDMFormat1Field1Key(systemObject_);
    f3f0_ = new DDMFormat3Field0Key(systemObject_);
    f3f3_ = new DDMFormat3Field3Key(systemObject_);    
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

    if (runMode_ != ATTENDED)
    {
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
///      if (allVariations || variationsToRun_.contains("28"))
///        Var028();
///      if (allVariations || variationsToRun_.contains("29"))
///        Var029();
///      if (allVariations || variationsToRun_.contains("30"))
///        Var030();
///      if (allVariations || variationsToRun_.contains("31"))
///        Var031();
      if (allVariations || variationsToRun_.contains("28"))
        Var028();
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


    // Disconnect from the AS/400 for record the record level access service
    systemObject_.disconnectService(AS400.RECORDACCESS);
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    try
    {
      // Delete DDMTEST and "DDMTest" if they exist
      CommandCall c = new CommandCall(pwrSys_);
      deleteLibrary(c, testLib_);

      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191") || msgs[0].getID().equals("CPF2110")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      deleteLibrary(c, "\"" + testLib_ + "\"");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191") || msgs[0].getID().equals("CPF2110")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        //throw new Exception("");   // Tolerate this failure - it will always fail for Turkish
      }

      // Create libs DDMTEST and "DDMTest"
      c = new CommandCall(systemObject_);
      c.run("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      c.run("CRTLIB LIB(\"" + testLib_ + "\") AUT(*ALL)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        //throw new Exception("");   // Tolerate this failure - it will always fail for Turkish
      }

      String src1 = "                R FMT";
      String src2 = "                  FIELD1       130A";
      SequentialFile f = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/%FILE%.MBR");
      try
      {
        f.create(132, "*SRC", "DDS source file for CreateAndAdd");
      }
      catch(Exception e)
      {
        System.out.println("Unable to create necessary dds source files.");
        e.printStackTrace();
        throw e;
      }
      Record[] r = new Record[2];
      r[0] = f.getRecordFormat().getNewRecord();
      r[0].setField(0, new BigDecimal(String.valueOf(1)));
      r[0].setField(2, src1);
      r[1] = f.getRecordFormat().getNewRecord();
      r[1].setField(0, new BigDecimal(String.valueOf(2)));
      r[1].setField(2, src2);
      f.open(AS400File.WRITE_ONLY, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
      f.write(r);
      f.close();
      c.run("CHGPF FILE(" + testLib_ + "/QDDSSRC) MAXMBRS(*NOMAX)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC7303")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      c.run("CPYF FROMFILE(" + testLib_ + "/QDDSSRC) TOFILE(" + testLib_ + "/QDDSSRC) TOMBR(SRC1) MBROPT(*REPLACE)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2889")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      c.run("CPYF FROMFILE(" + testLib_ + "/QDDSSRC) TOFILE(" + testLib_ + "/QDDSSRC) TOMBR(\"qddssrc\") MBROPT(*REPLACE)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2889")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        //throw new Exception("");   // Tolerate this failure - it will always fail for Turkish
      }
      c.run("CPYF FROMFILE(" + testLib_ + "/QDDSSRC) TOFILE(\"" + testLib_ + "\"/\"qddssrc\") FROMMBR(*ALL) TOMBR(*FROMMBR) MBROPT(*REPLACE) CRTFILE(*YES)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2880")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        //throw new Exception("");   // Tolerate this failure - it will always fail for Turkish
      }
      c.run("CPYF FROMFILE(" + testLib_ + "/QDDSSRC) TOFILE(\"" + testLib_ + "\"/\"qddssrc\") FROMMBR(SRC1) TOMBR(\"src1\") MBROPT(*REPLACE)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2889")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        //throw new Exception("");   // Tolerate this failure - it will always fail for Turkish
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
      CommandCall c = new CommandCall(pwrSys_);
      deleteLibrary(testLib_);

		AS400Message[] msgs = c.getMessageList();
		if (msgs.length > 0) {
			if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191")
					|| msgs[0].getID().equals("CPF2110"))) {
				for (int i = 0; i < msgs.length; ++i) {
					System.out.println(msgs[i]);
				}
			}
		}
    }
    catch(Exception e)
    {
      e.printStackTrace();
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
   *<br>Specify a variety of record lengths:
   *<ul>
   *<li>recordLength = 1
   *<li>recordLength = 32766
   *<li>recordLength = 500
   *</ul>
  **/
  public void Var001()
  {
    setVariation(1);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V1.FILE/MBR.MBR");
      file.create(1, "*DATA", "DDMCreateAndAdd.Var001()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(500, "*DATA", "DDMCreateAndAdd.Var001()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(32766, "*DATA", "DDMCreateAndAdd.Var001()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      succeeded();
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
   *Verify valid usage of create(int recordLength, String fileType, String textDescription).
   *<br>Specify all possible values for fileType:
   *<ul>
   *<li>fileType = *DATA
   *<li>fileType = *SRC
   *</ul>
  **/
  public void Var002()
  {
    setVariation(2);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V2.FILE/MBR.MBR");
      file.create(1, "*DATA", "DDMCreateAndAdd.Var002()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(500, "*SRC", "DDMCreateAndAdd.Var002()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      succeeded();
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
   *Verify valid usage of create(int recordLength, String fileType, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 1
   *<li>textDescription = string of length 26
   *<li>textDescription = string of length 50
   *</ul>
  **/
  public void Var003()
  {
    setVariation(3);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V3.FILE/MBR.MBR");
      file.create(1, "*DATA", "*BLANK");
      ObjectDescription od = new ObjectDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V3.FILE");
      ///if (!getTextDescription(od).equals(blankText_))
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text description when *BLANK specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(1, "*DATA", null);
      od.refresh();
      ///if (!getTextDescription(od).equals(blankText_))
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text description when null specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(1, "*DATA", "");
      od.refresh();
      ///if (!getTextDescription(od).equals(blankText_))
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text description when empty string specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                    AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(1, "*DATA", "A");
      od.refresh();
      if (!getTextDescription(od).trim().equals("A"))
      {
        failed("Wrong text description when A specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(1, "*DATA", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      od.refresh();
      if (!getTextDescription(od).trim().equals("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
      {
        failed("Wrong text description when ABCDEFGHIJKLMNOPQRSTUVWXYZ specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(1, "*DATA", "A123456789B123456789C123456789D123456789E123456789");
      od.refresh();
      if (!getTextDescription(od).trim().equals("A123456789B123456789C123456789D123456789E123456789"))
      {
        failed("Wrong text description when A123456789B123456789C123456789D123456789E123456789 specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
    succeeded();
  }

  /**
   *Verify invalid usage of create(int recordLength, String fileType, String textDescription).  Ensure that an ExtendedIllegalArgumentException is
   thrown, indicating LENGTH_NOT_VALID.
   *<br>Specify invalid values for recordLength:
   *<ul>
   *<li>recordLength = 0
   *<li>recordLength = 32767
   *</ul>
  **/
  public void Var004()
  {
    setVariation(4);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V4.FILE/MBR.MBR");
      file.create(0, "*DATA", "*BLANK");
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                              "recordLength",
                              ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        try
        {
          file.create(32767, "*DATA", "*BLANK");
          failed("Exception didn't occur.");
          file.delete();
        }
        catch(Exception e1)
        {
          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
                                    "recordLength",
                              ExtendedIllegalArgumentException.RANGE_NOT_VALID);
        }
      }
      else
      {
        failed(e, "Incorrect exception information.");
      }
    }
  }

  /**
   *Verify invalid usage of create(int recordLength, String fileType, String textDescription).  Verify the exception type.
   *<br>Specify invalid values for fileType:
   *<ul>
   *<li>fileType = null
   *<li>fileType = "*FOOBAR"
   *</ul>
  **/
  public void Var005()
  {
    setVariation(5);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V5.FILE/MBR.MBR");
      file.create(1, null, "*BLANK");
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      if (exceptionIs(e, "NullPointerException"))
      {
        try
        {
          file.create(1, "*FOOBAR", "*BLANK");
          failed("Exception didn't occur.");
          file.delete();
        }
        catch(Exception e1)
        {
          assertExceptionIs(e1, "ExtendedIllegalArgumentException",
                            ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
        }
      }
      else
      {
        failed(e, "Incorrect exception information.");
      }
    }
  }

  /**
   *Verify invalid usage of create(int recordLength, String fileType, String textDescription).  Ensure that ExtendedIllegalArgumentException is
   thrown, indicating LENGTH_NOT_VALID.
   *<br>Specify invalid values for textDescription:
   *<ul>
   *<li>textDescription = string of length 51
   *</ul>
  **/
  public void Var006()
  {
    setVariation(6);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V6.FILE/MBR.MBR");
      file.create(1, "*DATA",
                  "A123456789B123456789C123456789D123456789E1234567890");
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }
  }

  /**
   *Verify valid usage of create(String ddsSourceFile, String textDescription).
   *<br>Specify a variety of DDS source files:
   *<ul>
   *<li>Use a normal library name (not a quoted file name)
   *<li>Use a special library name (quoted)
   *<li>Use a normal file name (not a quoted file name)
   *<li>Use a special file name (quoted)
   *<li>Use a normal member name (not a quoted file name)
   *<li>Use a special member name (quoted)
   *</ul>
  **/
  public void Var007()
  {
    setVariation(7);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V7.FILE/A.MBR");
      // Using a normal library, normal file, and normal member.
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "*BLANK");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      // Using a quoted library, quoted file, and quoted member.
      file.create("/QSYS.LIB/\"" + testLib_ + "\".LIB/\"qddssrc\".FILE/\"src1\".MBR",
                  "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
      try
      {
        file.close();
        file.delete();
      }
      catch(Exception e1) { e1.printStackTrace(); }
    }

  }

  /**
   *Verify valid usage of create(String ddsSourceFile, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = *SRCMBRTXT
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 1
   *<li>textDescription = string of length 26
   *<li>textDescription = string of length 50
   *</ul>
  **/
  public void Var008()
  {
    setVariation(8);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V8.FILE/MBR.MBR");
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "*BLANK");
      ObjectDescription od = new ObjectDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V8.FILE");
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when *BLANK specified");
        return;
      }
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR",
                  "*SRCMBRTXT");
      od.refresh();
      if (!getTextDescription(od).trim().equals("DDS source file for CreateAndAdd"))
      {
        failed("Wrong text returned when DDS source file for CreateAndAdd specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", null);
      od.refresh();
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when null specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "");
      od.refresh();
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when empty string specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "A");
      od.refresh();
      if (!getTextDescription(od).trim().equals("A"))
      {
        failed("Wrong text returned when A specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR",
                  "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      od.refresh();
      if (!getTextDescription(od).trim().equals("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
      {
        failed("Wrong text returned when ABCDEFGHIJKLMNOPQRSTUVWXYZ specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "A123456789B123456789C123456789D123456789E123456789");
      od.refresh();
      if (!getTextDescription(od).trim().equals("A123456789B123456789C123456789D123456789E123456789"))
      {
        failed("Wrong text returned when A123456789B123456789C123456789D123456789E123456789 specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
    succeeded();
  }

  /**
   *Verify invalid usage of create(String ddsSourceFile, String textDescription).
   *<br>Specify invalid values for ddsSourceFile
   *<ul>
   *<li>Invalid object type for library
   *<li>Invalid object type for file
   *<li>Invalid object type for member
   *<li>No member specified.
   *<li>Valid IFS file name, but file does not exist
   *</ul>
  **/
  public void Var009()
  {
    setVariation(9);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V9.FILE/MBR.MBR");
      file.create("/QSYS.lib/" + testLib_ + ".foo/QDDSSRC.FILE/SRC1.MBR", "*BLANK");
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      try
      {
        file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FOO/SRC1.MBR", "*BLANK");
        failed("Exception didn't occur.");
        file.delete();
      }
      catch(Exception e2)
      {
        try
        {
          file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.FOO",
                      "*BLANK");
          failed("Exception didn't occur.");
          file.delete();
        }
        catch(Exception e3)
        {
          try
          {
            file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE", "*BLANK");
            failed("Exception didn't occur.");
            file.delete();
          }
          catch(Exception e4)
          {
            try
            {
              file.create("/QSYS.LIB/" + testLib_ + ".LIB/NO.FILE/NONE.MBR",
                          "*BLANK");
              failed("Exception didn't occur.");
              file.delete();
            }
            catch(Exception e5)
            {
              assertExceptionIs(e5, "AS400Exception");
            }
          }
        }
      }
    }
  }

  /**
   *Verify invalid usage of create(String ddsSourceFile, String textDescription).
   *<br>Specify invalid values for textDescription:
   *<ul>
   *<li>textDescription = string of length 51
   *</ul>
  **/
  public void Var010()
  {
    setVariation(10);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V10.FILE/MBR.MBR");
      file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR",
                  "A123456789B123456789C123456789D123456789E1234567890");
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }
  }

  /**
   *Verify valid usage of create(RecordFormat recordFormat, String textDescription).
   *<br>Specify variety of values for recordFormat:
   *<ul>
   *<li>recordFormat with one field, no key field
   *<li>recordFormat with one field, with key field
   *<li>recordFormat with several fields, no key fields
   *<li>recordFormat with several fields, with key fields
   *</ul>
  **/
  public void Var011()
  {
    setVariation(11);
    SequentialFile file = null;
    try
    {
      // One field, no keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V11.FILE/MBR.MBR");
      file.create(f1f0_, "DDMCreateAndAdd.Var011()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();

      // One field, one key.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V11.FILE/MBR.MBR");
      file.create(f1f1_, "DDMCreateAndAdd.Var011()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();

      // Several fields, no keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V11.FILE/MBR.MBR");
      file.create(f3f0_, "DDMCreateAndAdd.Var011()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();

      // Several fields, multiple keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V11.FILE/MBR.MBR");
      file.create(f3f3_, "DDMCreateAndAdd.Var011()");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
      try
      {
        file.close();
        file.delete();
      }
      catch(Exception e1) { }
    }

  }

  /**
   *Verify valid usage of create(RecordFormat recordFormat, String textDescription).
   *<br>Specify a variety of text descriptions:
   *<ul>
   *<li>textDescription = *BLANK
   *<li>textDescription = null
   *<li>textDescription = ""
   *<li>textDescription = string of length 1
   *<li>textDescription = string of length 26
   *<li>textDescription = string of length 50
   *</ul>
  **/
  public void Var012()
  {
    setVariation(12);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V12.FILE/MBR.MBR");
      RecordFormat format = f1f0_;
      file.create(format, "*BLANK");
      MemberDescription od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V12.FILE/MBR.MBR");
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when *BLANK specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, null);
      od.refresh();
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when null specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "");
      od.refresh();
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when empty string specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "A");
      od.refresh();
      if (!getTextDescription(od).trim().equals("A"))
      {
        failed("Wrong text returned when A specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      od.refresh();
      if (!getTextDescription(od).trim().equals("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
      {
        failed("Wrong text returned when ABCDEFGHIJKLMNOPQRSTUVWXYZ specified");
        return;
      }
      file.close();
      file.delete();
      file.create(format, "A123456789B123456789C123456789D123456789E123456789");
      od.refresh();
      if (!getTextDescription(od).trim().equals("A123456789B123456789C123456789D123456789E123456789"))
      {
        failed("Wrong text returned when A123456789B123456789C123456789D123456789E123456789 specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
    succeeded();
  }

  /**
   *Verify invalid usage of create(RecordFormat recordFormat, String textDescription).
   *<br>Specify invalid recordFormat:
   *<ul>
   *<li>recordFormat = null
   *</ul>
   **/
  public void Var013()
  {
    setVariation(13);
    SequentialFile file = null;
    try
    {
      // One field, no keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V13.FILE/MBR.MBR");
      file.create((RecordFormat) null, "DDMCreateAndAdd.Var013()");
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "recordFormat");
    }
  }

  /**
   *Verify invalid usage of create(RecordFormat recordFormat, String textDescription).
   *<br>Specify invalid text description:
   *<ul>
   *<li>textDescription = string of length 51
   *</ul>
   **/
  public void Var014()
  {
    setVariation(14);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V14.FILE/MBR.MBR");
      file.create(f1f0_,
                  "A123456789B123456789C123456789D123456789E1234567890");
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }
  }

  /**
   *Verify valid usage of create(RecordFormat recordFormat, String textDescription,
   *String altSeq, String ccsid, String order, String ref, boolean unique, String format
   *String text).
   *<br>Specify variety of values for recordFormat:
   *<ul>
   *<li>recordFormat with one field, no key field
   *<li>recordFormat with one field, with key field
   *<li>recordFormat with several fields, no key fields
   *<li>recordFormat with several fields, with key fields
   *</ul>
   **/
  public void Var015()
  {
    setVariation(15);
    SequentialFile file = null;
    try
    {
      // One field, no keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V15.FILE/MBR.MBR");
      file.create(f1f0_, "DDMCreateAndAdd.Var015()", null,
                  null, null, null, false, null, null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();

      // One field, one key.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V15.FILE/MBR.MBR");
      file.create(f1f1_, "DDMCreateAndAdd.Var015()", null,
                  null, null, null, false, null, null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();

      // Many fields, no keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V15.FILE/MBR.MBR");
      file.create(f3f0_, "DDMCreateAndAdd.Var015()", null,
                  null, null, null, false, null, null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();

      // Many fields, many keys.
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V15.FILE/MBR.MBR");
      file.create(f3f3_, "DDMCreateAndAdd.Var015()", null, null, null,
                  null, false, null, null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e);
      try
      {
        file.close();
        file.delete();
      }
      catch(Exception e1) { }
    }

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
   *<li>textDescription = string of length 1
   *<li>textDescription = string of length 26
   *<li>textDescription = string of length 50
   *</ul>
  **/
  public void Var016()
  {
    setVariation(16);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V16.FILE/MBR.MBR");
      RecordFormat format = f1f0_;
      file.create(format, "*BLANK", null, null, null, null,
                  false, null, null);
      ObjectDescription od = new ObjectDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V16.FILE");
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when *BLANK specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, null, null, null, null, null, false, null, null);
      od.refresh();
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when null specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "", null, null, null, null, false, null, null);
      od.refresh();
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when empty string specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "A", null, null, null, null, false, null, null);
      od.refresh();
      if (!getTextDescription(od).trim().equals("A"))
      {
        failed("Wrong text returned when A specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", null, null,
                  null, null, false, null, null);
      od.refresh();
      if (!getTextDescription(od).trim().equals("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
      {
        failed("Wrong text returned when ABCDEFGHIJKLMNOPQRSTUVWXYZ specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format,
                  "A123456789B123456789C123456789D123456789E123456789", null, null,
                  null, null, false, null, null);
      od.refresh();
      if (!getTextDescription(od).trim().equals("A123456789B123456789C123456789D123456789E123456789"))
      {
        failed("Wrong text returned when A123456789B123456789C123456789D123456789E123456789 specified");
        return;
      }
      file.open(AS400File.READ_ONLY, 0,
                AS400File.COMMIT_LOCK_LEVEL_NONE);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
    succeeded();
  }

  /**
   *Verify valid usage of create(RecordFormat recordFormat, String textDescription,
   *String altSeq, String ccsid, String order, String ref, boolean unique, String format
   *String text).
   *<br>Specify a variety of value combinations for the keyword parameters:
   *<ul>
   *<li>Specify valid value for keywords: CCSID, order, unique, text.
   *<li>Specify null for all keywords except unique
   *</ul>
  **/
  public void Var017()
  {
    setVariation(17);
    KeyedFile file = null;
    try
    {
      // Valid value for all keywords.
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/V17.FILE/MBR.MBR");
      RecordFormat format = f1f1_;
      file.create(format, "DDMCreateAndAdd.Var017()", null,
                  null, null, null, false, null, null);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create(format, "DDMCreateAndAdd.Var017()", null,
                  "37", "LIFO", null, false, null, "text");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      succeeded();
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
    catch(Exception e1) {}
  }

  /**
   *Verify invalid usage of create(RecordFormat recordFormat, String textDescription,
   *String altSeq, String ccsid, String order, String ref, boolean unique, String format
   *String text).
   *<br>Specify invalid recordFormat:
   *<ul>
   *<li>recordFormat = null
   *</ul>
  **/
  public void Var018()
  {
    setVariation(18);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V18.FILE/MBR.MBR");
      file.create((RecordFormat) null, "DDMCreateAndAdd.Var018()", null,
                  null, null, null, false, null, null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "recordFormat");
    }
  }

  /**
   *Verify invalid usage of create(RecordFormat recordFormat, String textDescription,
   *String altSeq, String ccsid, String order, String ref, boolean unique, String format
   *String text).
   *<br>Specify invalid textDescription:
   *<ul>
   *<li>textDescription = string of length 51
   *</ul>
  **/
  public void Var019()
  {
    setVariation(19);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V19.FILE/MBR.MBR");
      file.create(f1f0_,
                  "A123456789B123456789C123456789D123456789E1234567890",
                  null, null, null, null, false, null, null);
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }
  }

  /**
   *Verify invalid usage of create(RecordFormat recordFormat, String textDescription,
   *String altSeq, String ccsid, String order, String ref, boolean unique, String format
   *String text).
   *<br>Specify invalid keyword values:
   *<ul>
   *<li>Specify keyword values that will cause the CRTPF command to fail.
   *</ul>
  **/
  public void Var020()
  {
    setVariation(20);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V20.FILE/MBR.MBR");
      file.create(f1f0_, "DDMCreateAndAdd.Var020()", null,
                  null, "FOOFO", null, false, null, null);
      failed("Exception didn't occur.");
      file.delete();
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      assertCondition(msg.getID().toUpperCase().indexOf("CPF7311") != -1);
    } 
    catch(Exception e)
    {
      failed(e, "Incorrect exception information.");
    }
  }

  /**
   *Verify valid usage of addPhysicalFileMember(String member, String textDescription).
   *<br>Specify variety of values for member:
   *<ul>
   *<li>member = string of length 1, file opened
   *<li>member = string of length 10, file opened
   *<li>member = special member name (a quoted name), file not opened
   *</ul>
  **/
  public void Var021()
  {
    setVariation(21);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V21.FILE/MBR.MBR");
      file.create(1, "*DATA", "*BLANK");

      // Issue a change physical file command to allow more than one member.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("CHGPF FILE(" + testLib_ + "/V21) MAXMBRS(*NOMAX)");

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.addPhysicalFileMember("M", "Length of 1");
      file.close();
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V21.FILE/M.MBR");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.addPhysicalFileMember("A123456789", "Length of 10");
      file.close();
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V21.FILE/A123456789.MBR");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.addPhysicalFileMember("\"Mixed\"", "Mixed case");
      file.close();
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V21.FILE/\"Mixed\".MBR");
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      succeeded();
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
   *<li>textDescription = string of length 1
   *<li>textDescription = string of length 26
   *<li>textDescription = string of length 50
   *</ul>
  **/
  public void Var022()
  {
    setVariation(22);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/MBR.MBR");
      RecordFormat format = f1f0_;
      file.create(1, "*DATA", "*BLANK");
      // Issue a change physical file command to allow more than one member.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("CHGPF FILE(" + testLib_ + "/" + file.getFileName() +
                      ") MAXMBRS(*NOMAX)");

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.addPhysicalFileMember("M1", "*BLANK");
      MemberDescription od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M1.MBR");
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when *BLANK specified");
        return;
      }
      file.addPhysicalFileMember("M2", null);
      ///od.setPath("/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M2.MBR");
      od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M2.MBR");
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when null specified");
        return;
      }
      file.addPhysicalFileMember("M3", "");
      ///od.setPath("/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M3.MBR");
      od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M3.MBR");
      if (!getTextDescription(od).equals(""))
      {
        failed("Wrong text returned when empty string specified");
        return;
      }
      file.addPhysicalFileMember("M4", "A");
      ///od.setPath("/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M4.MBR");
      od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M4.MBR");
      if (!getTextDescription(od).trim().equals("A"))
      {
        failed("Wrong text returned when A specified");
        return;
      }
      file.addPhysicalFileMember("M5", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      ///od.setPath("/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M5.MBR");
      od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M5.MBR");
      if (!getTextDescription(od).trim().equals("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
      {
        failed("Wrong text returned when ABCDEFGHIJKLMNOPQRSTUVWXYZ specified");
        return;
      }
      file.addPhysicalFileMember("M6",
                                 "A123456789B123456789C123456789D123456789E123456789");
      ///od.setPath("/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M6.MBR");
      od = new MemberDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V22.FILE/M6.MBR");
      if (!getTextDescription(od).trim().equals("A123456789B123456789C123456789D123456789E123456789"))
      {
        failed("Wrong text returned when A123456789B123456789C123456789D123456789E123456789 specified");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}
    succeeded();
  }

  /**
   *Verify invalid usage of addPhysicalFileMember(String member, String textDescription).
   *<br>Specify invalid member:
   *<ul>
   *<li>member = null
   *<li>member = string of length 11
   *<li>member = name of member that already exists
   *</ul>
  **/
  public void Var023()
  {
    setVariation(23);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V23.FILE/MBR.MBR");
      RecordFormat format = f1f0_;
      file.create(1, "*DATA", "*BLANK");

      // Issue a change physical file command to allow more than one member.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("CHGPF FILE(" + testLib_ + "/" + file.getFileName() +
                      ") MAXMBRS(*NOMAX)");

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.addPhysicalFileMember("A1234567890", "");
      failed("Exception didn't occur.");
      file.close();
    }
    catch(Exception e)
    {
      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                              "name",
                              ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        try
        {
          file.addPhysicalFileMember("Member", "");
          file.addPhysicalFileMember("Member", "");
          failed("Exception didn't occur.");
        }
        catch(AS400Exception e1)
        {
          AS400Message msg = e1.getAS400Message();
          assertCondition(msg.getID().toUpperCase().indexOf("CPF5812") != -1);
        } 
        catch(Exception e1)
        {
          failed(e, "Incorrect exception information.");
        }
      }
      else
      {
        failed(e, "Incorrect exception information.");
      }
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}  
  }

  /**
   *Verify invalid usage of addPhysicalFileMember(String member, String textDescription).
   *<br>Specify invalid text description:
   *<ul>
   *<li>textDescription = string of length 51
   *</ul>
  **/
  public void Var024()
  {
    setVariation(24);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V24.FILE/MBR.MBR");
      file.create(1, "*DATA", "*BLANK");

      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.addPhysicalFileMember("A123456789B123456789C123456789D123456789E123456789F", "");
      failed("Exception didn't occur.");
      file.close();
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException",
                        ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}  
  }

  /**
   *Verify AS400Exception indicating CPF5813 when open() has been done and
   *create() is attempted.
   *<ul>
   *<li>create(int, String)
   *<li>create(String, String)
   *<li>create(RecordFormat, String)
   *<li>create(RecordFormat, String, String, String, String, String, boolean, String, String)
   *</ul>
  **/
  public void Var025()
  {
    setVariation(25);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V25.FILE/MBR.MBR");
      file.create(1, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.create(1, "*DATA", "*BLANK");
      failed("Exception didn't occur.");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5813") != -1)
      {
        try
        {
          file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "");
          failed("Exception didn't occur.");
        }
        catch(AS400Exception e1)
        {
          msg = e1.getAS400Message();
          if (msg.getID().toUpperCase().indexOf("CPF5813") != -1)
          {
            try
            {
              RecordFormat format = f1f1_;
              file.create(format, "DDMCreateAndAdd.Var025()");
              failed("Exception didn't occur.");
            }
            catch(AS400Exception e2)
            {
              msg = e2.getAS400Message();
              if (msg.getID().toUpperCase().indexOf("CPF5813") != -1)
              {
                try
                {
                  RecordFormat format = f1f1_;
                  file.create(format, "DDMCreateAndAdd.Var025()", null,
                              null, null, null, false, null, null);
                  failed("Exception didn't occur.");
                }
                catch(AS400Exception e3)
                {
                  msg = e3.getAS400Message();
                  assertCondition(msg.getID().toUpperCase().indexOf("CPF5813") != -1);
                }
                catch(Exception e3)
                {
                  failed(e3, "Incorrect exception information.");
                }
              }
              else
              {
                failed(e2, "Incorrect exception information.");
              }
            }
            catch(Exception e2)
            {
              failed(e2, "Incorrect exception information.");
            }
          }
          else
          {
            failed(e1, "Incorrect exception information.");
          }
        }
        catch(Exception e1)
        {
          failed(e1, "Incorrect exception information.");
        }
      }
      else
      {
        failed(e, "Incorrect exception information.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Incorrect exception information.");
    }

    try
    {
      file.close();
      file.delete();
    }
    catch(Exception e) {}  
  }

  /**
   *Verify AS400Exception when create() is attempted on an existing AS/400 file.
   *<ul>
   *<li>create(int, String)
   *<li>create(String, String)
   *<li>create(RecordFormat, String)
   *<li>create(RecordFormat, String, String, String, String, String, boolean, String, String)
   *</ul>
  **/
  public void Var026()
  {
    setVariation(26);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V26.FILE/MBR.MBR");
      file.create(1, "*DATA", "*BLANK");
      file.create(1, "*DATA", "*BLANK");
      failed("Exception didn't occur.");
    }
    catch(AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF5813") != -1)
      {
        try
        {
          file.create("/QSYS.LIB/" + testLib_ + ".LIB/QDDSSRC.FILE/SRC1.MBR", "");
          failed("Exception didn't occur.");
        }
        catch(AS400Exception e1)
        {
          msg = e1.getAS400Message();
          if (msg.getID().toUpperCase().indexOf("CPF5813") != -1)
          {
            try
            {
              RecordFormat format = f1f1_;
              file.create(format, "DDMCreateAndAdd.Var026()");
              failed("Exception didn't occur.");
            }
            catch(AS400Exception e2)
            {
              msg = e2.getAS400Message();
              if (msg.getID().toUpperCase().indexOf("CPF5813") != -1)
              {
                try
                {
                  RecordFormat format = f1f1_;
                  file.create(format, "DDMCreateAndAdd.Var025()", null,
                              null, null, null, false, null, null);
                  failed("Exception didn't occur.");
                }
                catch(AS400Exception e3)
                {
                  msg = e3.getAS400Message();
                  assertCondition(msg.getID().toUpperCase().indexOf("CPF5813") != -1);
                }
                catch(Exception e3)
                {
                  failed(e3, "Incorrect exception information.");
                }
              }
              else
              {
                failed(e2, "Incorrect exception information.");
              }
            }
            catch(Exception e2)
            {
              failed(e2, "Incorrect exception information.");
            }
          }
          else
          {
            failed(e1, "Incorrect exception information.");
          }
        }
        catch(Exception e1)
        {
          failed(e1, "Incorrect exception information.");
        }
      }
      else
      {
        failed(e, "Incorrect exception information.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Incorrect exception information.");
    }
  }

  /**
   *Verify valid usage of create(String ddsSourceFile, String textDescription).
   *<br>Specify a variety of DDS source files:
   *<ul>
   *<li>Use %LIBL% for library
   *<li>Use %CURLIB% for library
   *</ul>
  **/
  public void Var027()
  {
    setVariation(27);

    SequentialFile file = null;
    try
    {
      // Create the DDS file.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("CPYF FROMFILE(" + testLib_ + "/QDDSSRC) TOFILE(QGPL/FOOBAR) " +
                      "MBROPT(*REPLACE) CRTFILE(*YES)");

      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/V27.FILE/MBR.MBR");
      file.create("/QSYS.LIB/%LIBL%.LIB/FOOBAR.FILE/QDDSSRC.MBR", "FOOBAR");
      ObjectDescription od = new ObjectDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/V27.FILE");
      if (!getTextDescription(od).trim().equals("FOOBAR"))
      {
        failed("Wrong text returned when FOOBAR specified");
        return;
      }
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.close();
      file.delete();
      file.create("/QSYS.LIB/%CURLIB%.LIB/FOOBAR.FILE/QDDSSRC.MBR",
                  "GLARCH");
      od.refresh();
      if (!getTextDescription(od).trim().equals("GLARCH"))
      {
        failed("Wrong text returned when GLARCH specified");
        return;
      }
      file.setRecordFormat(new AS400FileRecordDescription(systemObject_, file.getPath()).retrieveRecordFormat()[0]);
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }

    try
    {
      // Delete the DDS file.
      CommandCall interpreter = new CommandCall(systemObject_);
      interpreter.run("DLTF FILE(QGPL/FOOBAR)");

      file.close();
      file.delete();
    }
    catch(Exception e) {}
    succeeded();
  }


  // Variations to test create(int recordLength, String fileType, String textDescription, int maxMembers, int memberSize)

  // Note to tester: To see the CRTPF commands that are actually issued to the server (and visually verify the MAXMBRS and SIZE values composed by AS400FileImplBase.create), edit file AS400FileImplRemote.java, set its DEBUG variable to 'true', and recompile.

/// TBD: Add these variations when we've integrated the new AS400File.create() method.

///  /**
///   *Verify valid usage of create(int recordLength, String fileType, String textDescription, int maxMembers, int memberSize).
///   *<br>Specify a variety of values for maxMembers and memberSize:
///   *<ul>
///   *<li>maxMembers = 1, memberSize = 1
///   *<li>maxMembers = NO_MAXIMUM, memberSize = NO_MAXIMUM
///   *<li>maxMembers = 500, memberSize = 200
///   *</ul>
///  **/
///  public void Var028()
///  {
///    setVariation(28);
///    SequentialFile file = null;
///    try
///    {
///      file = new SequentialFile(systemObject_,
///                                "/QSYS.LIB/" + testLib_ + ".LIB/V1.FILE/MBR.MBR");
///      file.create(1, "*DATA", "DDMCreateAndAdd.Var028()", 1, 1);
///      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
///      file.close();
///      file.delete();
///      file.create(500, "*DATA", "DDMCreateAndAdd.Var028()", AS400File.NO_MAXIMUM, AS400File.NO_MAXIMUM);
///      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
///      file.close();
///      file.delete();
///      file.create(32766, "*DATA", "DDMCreateAndAdd.Var028()", 500, 200);
///      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
///      file.close();
///      file.delete();
///      succeeded();
///    }
///    catch(Exception e)
///    {
///      failed(e);
///    }
///
///    try
///    {
///      file.close();
///      file.delete();
///    }
///    catch(Exception e) {}
///  }
///
///  /**
///   *Verify valid usage of create(int recordLength, String fileType, String textDescription, int maxMembers, int memberSize).
///   *<br>Specify maxMembers = NO_MAXIMUM, memberSize = NO_MAXIMUM
///   *<br>Specify all possible values for fileType:
///   *<ul>
///   *<li>fileType = *DATA
///   *<li>fileType = *SRC
///   *</ul>
///  **/
///  public void Var029()
///  {
///    setVariation(29);
///    SequentialFile file = null;
///    try
///    {
///      file = new SequentialFile(systemObject_,
///                                "/QSYS.LIB/" + testLib_ + ".LIB/V2.FILE/MBR.MBR");
///      file.create(1, "*DATA", "DDMCreateAndAdd.Var029()", AS400File.NO_MAXIMUM, AS400File.NO_MAXIMUM);
///      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
///      file.close();
///      file.delete();
///      file.create(500, "*SRC", "DDMCreateAndAdd.Var029()", AS400File.NO_MAXIMUM, AS400File.NO_MAXIMUM);
///      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
///      file.close();
///      file.delete();
///      succeeded();
///    }
///    catch(Exception e)
///    {
///      failed(e);
///    }
///
///    try
///    {
///      file.close();
///      file.delete();
///    }
///    catch(Exception e) {}
///  }
///
///
///  /**
///   *Verify invalid usage of create(int recordLength, String fileType, String textDescription, int maxMembers, int memberSize).  Ensure that an ExtendedIllegalArgumentException is
///   thrown, indicating LENGTH_NOT_VALID.
///   *<br>Specify invalid values for maxMembers:
///   *<ul>
///   *<li>maxMembers = 0
///   *<li>maxMembers = 32768
///   *</ul>
///  **/
///  public void Var030()
///  {
///    setVariation(30);
///    SequentialFile file = null;
///    try
///    {
///      file = new SequentialFile(systemObject_,
///                                "/QSYS.LIB/" + testLib_ + ".LIB/V4.FILE/MBR.MBR");
///      file.create(1, "*DATA", "*BLANK", 0, 10);
///      failed("Exception didn't occur.");
///      file.delete();
///    }
///    catch(Exception e)
///    {
///      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
///                              "maxMembers",
///                              ExtendedIllegalArgumentException.RANGE_NOT_VALID))
///      {
///        try
///        {
///          file.create(1, "*DATA", "*BLANK", 32768, 10);
///          failed("Exception didn't occur.");
///          file.delete();
///        }
///        catch(Exception e1)
///        {
///          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
///                                    "maxMembers",
///                              ExtendedIllegalArgumentException.RANGE_NOT_VALID);
///        }
///      }
///      else
///      {
///        failed(e, "Incorrect exception information.");
///      }
///    }
///  }
///
///  /**
///   *Verify invalid usage of create(int recordLength, String fileType, String textDescription, int maxMembers, int memberSize).  Verify the exception type.
///   *<br>Specify invalid values for memberSize:
///   *<ul>
///   *<li>memberSize = 0
///   *<li>memberSize = 2147483647
///   *</ul>
///  **/
///  public void Var031()
///  {
///    setVariation(31);
///    SequentialFile file = null;
///    try
///    {
///      file = new SequentialFile(systemObject_,
///                                "/QSYS.LIB/" + testLib_ + ".LIB/V5.FILE/MBR.MBR");
///      file.create(1, "*DATA", "*BLANK", 1, 0);
///      failed("Exception didn't occur.");
///      file.delete();
///    }
///    catch(Exception e)
///    {
///      if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
///                              "memberSize",
///                              ExtendedIllegalArgumentException.RANGE_NOT_VALID))
///      {
///        try
///        {
///          file.create(1, "*DATA", "*BLANK", 1, 2147483647);
///          failed("Exception didn't occur.");
///          file.delete();
///        }
///        catch(Exception e1)
///        {
///          assertExceptionStartsWith(e1, "ExtendedIllegalArgumentException",
///                                    "memberSize",
///                              ExtendedIllegalArgumentException.RANGE_NOT_VALID);
///        }
///      }
///      else
///      {
///        failed(e, "Incorrect exception information.");
///      }
///    }
///  }


  /**
   *Verify that retrieveRecordFormat() against a nonexistent file, returns CPF3012, "File &1 in library &2 not found".
   *This verifies the DDM Server fix that was published in V6R1 PTF SI37532 and in V7R1 PTF SI37533. The bug (introduced in V6R1) was that the DDM Server returned the message ID as a blank field.
  **/
  public void Var028()
  {
    setVariation(28);
    final String filePath = "/QSYS.LIB/QGPL.LIB/NOSUCHFILE.FILE";
    try
    {
      String recordFormatName = new AS400FileRecordDescription(systemObject_, filePath).retrieveRecordFormat() [0].getName();
      failed("Exception didn't occur.");
    }
    catch (AS400Exception ex)
    {
      AS400Message msg = ex.getAS400Message();
      if (msg.getID().equals("CPF3012"))
      {
        succeeded();
      }
      else
      {
        failed("Incorrect message ID returned: |" + msg.getID());
      }
    }
    catch(Exception e)
    {
      failed(e, "Exception is not as expected.");
    }
  }



}

