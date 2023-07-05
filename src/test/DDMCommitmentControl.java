///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMCommitmentControl.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.FileOutputStream;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
 *Testcase DDMCommitmentControl.  This test class verifies valid and invalid usage of:
 *<ul compact>
 *<li>startCommitmentControl()
 *<li>endCommitmentControl()
 *<li>isCommitmentControlStarted()
 *<li>getCommitLockLevel()
 *<li>commit()
 *<li>rollback()
 *</ul>
**/
public class DDMCommitmentControl extends Testcase
{
  CommandCall cmd_;
  Record[] records_;
  String testLib_ = null;
  AS400 pwrSys_;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMCommitmentControl(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      String testLib,
                      AS400 sys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMCommitmentControl", 90,
          variationsToRun, runMode, fileOutputStream);
    cmd_ = new CommandCall(systemObject_);
    testLib_ = testLib;
    pwrSys_ = sys;
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

    if ((allVariations || variationsToRun_.contains("87")) &&
        runMode_ != ATTENDED)
    {
      setVariation(87);
      Var087();
    }

    if ((allVariations || variationsToRun_.contains("88")) &&
        runMode_ != ATTENDED)
    {
      setVariation(88);
      Var088();
    }

    if ((allVariations || variationsToRun_.contains("89")) &&
        runMode_ != ATTENDED)
    {
      setVariation(89);
      Var089();
    }

    if ((allVariations || variationsToRun_.contains("90")) &&
        runMode_ != ATTENDED)
    {
      setVariation(90);
      Var090();
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


String runCommand(String command, StringBuffer sb)
{
  String msg = null;

  try
  {
    // Run the command.
      
      boolean rc = cmd_.run(command);
      sb.append("COMMAND: "+command+" rc="+rc+"\n"); 
    // If there are any messages then save the ones that potentially
    // indicate problems.
    AS400Message[] msgs = cmd_.getMessageList();
    if (msgs.length > 0 && msgs[0].getID().toUpperCase().startsWith("CPF"))
    {
      msg = msgs[0].getID().toUpperCase();
      sb.append("...COMMAND: "+command+" msg="+msg+"\n"); 
    }
  }
  catch(Exception e)
  {
    msg = e.toString();
    e.printStackTrace(output_);
      sb.append("...COMMAND: exeption="+msg+"\n"); 
  }

  return msg;
}



/**
 @exception  Exception  If an exception occurs.
 **/
  protected void setup()
    throws Exception
  {
    StringBuffer sb = new StringBuffer();
    String command = "";
    boolean rc; 
    try
    {

      if (pwrSys_ == null)
      {
        throw new Exception("-misc parameter not specified. Need a power user.");
      }

      // Delete the library first
      CommandCall cc = new CommandCall(pwrSys_);

      deleteLibrary(cc, testLib_); 

      command = "DLTJRN QGPL/JT4*"; 
      rc = cc.run(command);
      sb.append("COMMAND:"+command+" rc="+rc+"\n");

    // Create library DDMTEST
    String msg = runCommand("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)", sb);
    if (msg != null && !msg.equals("CPF2111"))
    {
      output_.println("Failure executing 'CRTLIB LIB(" + testLib_ + ") AUT(*ALL)'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }

    // Create the necessary files
    SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
    sb.append("Creating f1\n"); 
    f1.create(new DDMCmtCtlNoKeyFormat(systemObject_), "Two fields, CHAR(10), no key");
    
    SequentialFile f2 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC2.FILE/MBR1.MBR");
    sb.append("Creating f2\n"); 
    f2.create(new DDMCmtCtlNoKeyFormat(systemObject_), "Two fields, CHAR(10), no key");
    
    KeyedFile f3 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
    sb.append("Creating f3\n"); 
    f3.create(new DDMCmtCtlKeyFormat(systemObject_), "Two fields, CHAR(10), key field1");
    
    KeyedFile f4 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK2.FILE/MBR1.MBR");
    sb.append("Creating f4\n"); 
    f4.create(new DDMCmtCtlKeyFormat(systemObject_), "Two fields, CHAR(10), key field1");
    
    SequentialFile f5 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC3.FILE/MBR1.MBR");
    sb.append("Creating f5\n"); 
    f5.create(new DDMCmtCtlNoKeyFormat(systemObject_), "Two fields, CHAR(10), no key");

    // Create an array of records to write to the files
    records_ = new Record[9];
    for (short i = 1; i < 10; ++i)
    {
      records_[i-1] = f1.getRecordFormat().getNewRecord();
      records_[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
      records_[i-1].setField(1, "Descrip" + String.valueOf(i) + "  ");
    }

    // Populate the files
    sb.append("Populate the files\n"); 
    f1.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f2.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f3.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f4.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f5.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
    f1.write(records_);
    f2.write(records_);
    f3.write(records_);
    f4.write(records_);
    f5.write(records_);
    f1.close();
    f2.close();
    f3.close();
    f4.close();
    f5.close();


    // Cleanup the old journal.
    msg = runCommand("ENDJRNPF FILE(*ALL) JRN(QGPL/JT4DDMJRN) ", sb); 
    msg = runCommand("dltjrn JRN(QGPL/JT4DDMJRN) ", sb); 
    msg = runCommand("DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV)  DLTOPT(*IGNINQMSG) ", sb);


    // Create journal receiver and journal if it does not already exist

    msg = runCommand("CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')", sb);
    if (msg != null && !msg.equals("CPF7010"))
    {
      output_.println("Failure executing 'CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }
    msg = runCommand("CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')", sb);
    if (msg != null && !msg.equals("CPF7010"))
    {
      output_.println("Failure executing 'CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')'");
      output_.println(msg);
      output_.println(sb.toString());
      output_.flush(); 
      throw new Exception("");
    }

    // Start journaling
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/DDMCC1) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMCC1) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/DDMCC2) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMCC2) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/DDMCCK1) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMCCK1) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/DDMCCK2) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMCCK2) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }
    msg = runCommand("STRJRNPF FILE(" + testLib_ + "/DDMCC3) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'STRJRNPF FILE(" + testLib_ + "/DDMCC3) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      output_.println(sb.toString()); 
      throw new Exception("");
    }
  }
  catch(Exception e)
  {
    e.printStackTrace(output_);
    output_.println(sb.toString()); 
    throw e;
  }
}

  /**
   @exception  Exception  If an exception occurs.
   **/
protected void cleanup()
  throws Exception
{
  StringBuffer sb = new StringBuffer(); 
  boolean success = true;
  try
  {
    // Stop journaling
    String msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMCC1) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMCC1) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMCC2) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMCC2) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMCCK1) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMCCK1) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMCCK2) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMCCK2) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }
    msg = runCommand("ENDJRNPF FILE(" + testLib_ + "/DDMCC3) JRN(QGPL/JT4DDMJRN)", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'ENDJRNPF FILE(" + testLib_ + "/DDMCC3) JRN(QGPL/JT4DDMJRN)'");
      output_.println(msg);
      success = false;
    }

    // Delete the files created during setup()
    SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
    f1.delete();
    SequentialFile f2 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC2.FILE/MBR1.MBR");
    f2.delete();
    KeyedFile f3 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
    f3.delete();
    KeyedFile f4 = new KeyedFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK2.FILE/MBR1.MBR");
    f4.delete();
    SequentialFile f5 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC3.FILE/MBR1.MBR");
    f5.delete();

    // Delete the DDMTest library and the journal file.  The journal
    // receive should be automatically deleted due to the way that the
    // journal and receiver were deleted.
    msg = deleteLibrary(cmd_, testLib_); 
    if (msg != null)
    {
      output_.println("Failure executing 'DeleteLibrary " + testLib_ + "'");
      output_.println(msg);
      output_.println(sb.toString()); 
      success = false;
    }
    msg = runCommand("DLTJRN QGPL/JT4DDMJRN", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'DLTJRN QGPL/JT4DDMJRN'");
      output_.println(msg);
      output_.println(sb.toString()); 
      success = false;
    }

  }
  catch(Exception e)
  {
    System.out.println("Cleanup unsuccessful.  Some files may have been left in " + testLib_ + " and QGPL");
    e.printStackTrace(output_);
    throw e;
  }
  if (!success)
  {
    throw new Exception("Cleanup was unsuccessful");
  }
}

// Variations 001 through 027 are for SequentialFile objects.
// Variations 028 through 054 are for KeyedFile objects. - CRS
// Variations 055 through 082 test the static methods. - CRS

  /**
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_ALL<br>
   *Verify the locking level by reading a record, and attempting to end
   *commitment control file without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        file.readFirst();
        file.endCommitmentControl();
        file.close();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
    }
    catch (AS400Exception e) {
	System.out.println("Warning:  Exception from testcase");
	System.out.println("Exception text="+e.getAS400Message().getText()); 
	e.printStackTrace(); 

    }
    catch(Exception e) {
	System.out.println("Warning:  Exception from testcase");
	e.printStackTrace(); 
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_CHANGE<br>
   *Verify the locking level by writing a record to a file, and attempting to
   *end commitment control without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "record002 ");
        rec.setField(1, "descrip002");
        file.write(rec);
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_CURSOR_STABILITY
   *Verify the locking level by updating a record to a file, and attempting to
   *end commitment control without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "record003 ");
        rec.setField(1, "descrip003");
        file.update(3,rec);
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "commitLockLevel"
   *and PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      try
      {
        file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_NONE);

        failMsg.append("No exception specifying COMMIT_LOCK_LEVEL_DEFAULT\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalArgumentException",                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating
   *COMMITMENT_CONTROL_ALREADY_STARTED
   *</ul>
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      try
      {
        file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
        failMsg.append("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.COMMITMENT_CONTROL_ALREADY_STARTED))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Specify an invalid value for the commitLockLevel.
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_DEFAULT
   *<li>-1
   *<li>6
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "commitLockLevel" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      failMsg.append("No exception specifying COMMIT_LOCK_LEVEL_DEFAULT\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      file.startCommitmentControl(-1);
      failMsg.append("No exception specifying -1\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      file.startCommitmentControl(6);
      failMsg.append("No exception specifying 6\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of endCommitmentControl().
   *<ul compact>
   *<li>Start commitment control with COMMIT_LOCK_LEVEL_ALL..
   *<li>End commitment control.
   *<li>Open the file, write a record to the file and close the file.
   *<li>Open the file and make sure the record written is in the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record written will be in the file.
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.endCommitmentControl();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record007a");
      rec.setField(1, "descrip007");
      file.write(rec);
      file.close();

      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec2 = file.readLast();
      if (!rec.toString().equals(rec2.toString()))
      {
        failMsg.append("Record does not appear to be in the file\n");
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
   *Verify valid usage of endCommitmentControl().
   *<ul compact>
   *<li>Call endCommitmentControl() when commitment control has not been
   *started for the job.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception should be thrown.
   *</ul>
  **/
  public void Var008()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.endCommitmentControl();
      succeeded();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unexpected exception.\n");
    }
  }

  /**
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *ALL
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record009 ");
      rec.setField(1, "descrip009");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *CHG
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record010 ");
      rec.setField(1, "descrip010");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *CS
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record011 ");
      rec.setField(1, "descrip011");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when no changes have been made
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when one change has been made
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record013 ");
      rec.setField(1, "descrip013");
      file.update(6,rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.read(6).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.read(6).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when several changes have been made
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec1 = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      Record rec3 = file.getRecordFormat().getNewRecord();

      rec1.setField(0, "record014a");
      rec1.setField(1, "descrip014");
      file.write(rec1);

      rec2.setField(0, "record014b");
      rec2.setField(1, "descrip014");
      file.update(7,rec2);

      rec3.setField(0, "record014c");
      rec3.setField(1, "descrip014");
      file.write(rec3);

      file.deleteRecord(1);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec3.toString()))
      {
        failMsg.append("commit() failed. last record: "+file.readLast().toString()+"\n");
      }
      if (!file.read(7).toString().equals(rec2.toString()))
      {
        failMsg.append("commit() failed. record 7: "+file.readFirst().toString()+"\n");
      }
      if (!file.readFirst().toString().equals(records_[1].toString()))
      {
        failMsg.append("commit() failed. first record: "+file.readFirst().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when commitment control has not been started
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record015 ");
      rec.setField(1, "descrip015");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when commitment control is *NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var016()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record016 ");
      rec.setField(1, "descrip016");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *ALL
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var017()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record017 ");
      rec.setField(1, "descrip017");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *CHG
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var018()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      String oldRec = file.read(8).toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record018 ");
      rec.setField(1, "descrip018");
      file.update(8,rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      if (!file.read(8).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.read(8).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *CS
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var019()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      String oldRec = file.readLast().toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record019 ");
      rec.setField(1, "descrip019");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when no changes have been made
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var020()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when one change has been made
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var021()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.read(3).toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record021 ");
      rec.setField(1, "descrip021");
      file.update(3,rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.read(3).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.read(3).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when several changes have been made
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var022()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRecLast = file.readLast().toString();
      String oldRec4 = file.read(4).toString();
      String oldRec2 = file.read(2).toString();
      Record rec1 = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      Record rec3 = file.getRecordFormat().getNewRecord();

      rec1.setField(0, "record022a");
      rec1.setField(1, "descrip022");
      file.write(rec1);

      rec2.setField(0, "record022b");
      rec2.setField(1, "descrip022");
      file.update(4,rec2);

      rec3.setField(0, "record022c");
      rec3.setField(1, "descrip022");
      file.write(rec3);

      file.deleteRecord(2);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.read(4).toString().equals(oldRec4))
      {
        failMsg.append("rollback failed. record 4 is: " + file.read(4).toString() + "\n");
      }
      if (!file.readLast().toString().equals(oldRecLast))
      {
        failMsg.append("rollback failed. last record is: " + file.readLast().toString() + "\n");
      }
      if (!(file.readFirst().toString().equals(oldRec2) ||
          file.readNext().toString().equals(oldRec2)))
      {
        failMsg.append("rollback failed. first record is: " + file.readFirst().toString() + "\n");
        failMsg.append("next record is: "+file.readNext().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when commitment control has not been started
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var023()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record023 ");
      rec.setField(1, "descrip023");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when commitment control is *NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var024()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record024 ");
      rec.setField(1, "descrip024");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify false returned when commitment control has not been started
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>false is returned.
   *</ul>
  **/
  public void Var025()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      if (file.isCommitmentControlStarted())
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify true returned when commitment control has been started via
   *the startCommitmentControl method on the AS400File object being queried.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>true is returned.
   *</ul>
  **/
  public void Var026()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.isCommitmentControlStarted())
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify true returned when commitment control was started on a different
   *AS400File object within this application.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>true is returned.
   *</ul>
  **/
  public void Var027()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    SequentialFile file2 = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file2 = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC2.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file2.isCommitmentControlStarted())
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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


// These variation are duplicates of the above 27, only for KeyedFile objects.


/**
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_ALL<br>
   *Verify the locking level by reading a record, and attempting to end
   *commitment control file without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var028()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_ALL)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        file.readFirst();
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_CHANGE<br>
   *Verify the locking level by writing a record to a file, and attempting to
   *end commitment control without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var029()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "record029 ");
        rec.setField(1, "descrip029");
        file.write(rec);
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_CURSOR_STABILITY
   *Verify the locking level by updating a record to a file, and attempting to
   *end commitment control without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var030()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "record030 ");
        rec.setField(1, "descrip030");
        Object[] k = new Object[1];
        k[0] = records_[2].getField(0);
        file.update(k,rec);
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException with PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var031()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      try
      {
        file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_NONE);
        failMsg.append("No exception specifying COMMIT_LOCK_LEVEL_DEFAULT\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalArgumentException",                         ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating
   *COMMITMENT_CONTROL_ALREADY_STARTED
   *</ul>
  **/
  public void Var032()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      try
      {
        file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
        failMsg.append("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.COMMITMENT_CONTROL_ALREADY_STARTED))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Specify an invalid value for the commitLockLevel.
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_DEFAULT
   *<li>-1
   *<li>6
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "commitLockLevel" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var033()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      file.close();
      file.endCommitmentControl();
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
   *Verify valid usage of endCommitmentControl().
   *<ul compact>
   *<li>Start commitment control with COMMIT_LOCK_LEVEL_ALL..
   *<li>End commitment control.
   *<li>Open the file, write a record to the file and close the file.
   *<li>Open the file and make sure the record written is in the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record written will be in the file.
   *</ul>
  **/
  public void Var034()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.endCommitmentControl();

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record034a");
      rec.setField(1, "descrip34a");
      file.write(rec);
      file.close();

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      Record rec2 = file.read(k);
      if (!rec2.toString().equals(rec.toString()))
      {
        failMsg.append("Record soesn't appear to have been written.\n");
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
   *Verify valid usage of endCommitmentControl().
   *<ul compact>
   *<li>Call endCommitmentControl() when commitment control has not been
   *started for the job.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception should be thrown.
   *</ul>
  **/
  public void Var035()
  {
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.endCommitmentControl();
      succeeded();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unexpected exception.\n");
    }
  }

  /**
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *ALL
   *</ul>
  **/
  public void Var036()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record036 ");
      rec.setField(1, "descrip036");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      Object[] k = new Object[1];
      k[0] = rec.getField(0);

      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.read(k).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *CHG
   *</ul>
  **/
  public void Var037()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record037 ");
      rec.setField(1, "descrip037");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.read(k).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *CS
   *</ul>
  **/
  public void Var038()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record038 ");
      rec.setField(1, "descrip038");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.read(k).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when no changes have been made
   *</ul>
  **/
  public void Var039()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String rec = file.readLast().toString();

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }
      if (!file.readLast().toString().equals(rec))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when one change has been made
   *</ul>
  **/
  public void Var040()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record040 ");
      rec.setField(1, "descrip040");
      Object[] k = new Object[1];
      k[0] = records_[5].getField(0);
      file.update(k,rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when several changes have been made
   *</ul>
  **/
  public void Var041()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec1 = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      Record rec3 = file.getRecordFormat().getNewRecord();
      Object[] k1 = new Object[1];
      Object[] k2 = new Object[1];
      k1[0] = records_[6].getField(0);
      k2[0] = records_[0].getField(0);

      rec1.setField(0, "record041a");
      rec1.setField(1, "descrip41a");
      file.write(rec1);

      rec2.setField(0, "record041b");
      rec2.setField(1, "descrip41b");
      file.update(k1,rec2);

      rec3.setField(0, "record041c");
      rec3.setField(1, "descrip41c");
      file.write(rec3);

      file.deleteRecord(k2);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }
      k1[0] = rec3.getField(0);
      if (!file.read(k1).toString().equals(rec3.toString()))
      {
        failMsg.append("commit() failed. record041c: "+file.read(k1).toString()+"\n");
      }
      k1[0] = rec2.getField(0);
      if (!file.read(k1).toString().equals(rec2.toString()))
      {
        failMsg.append("commit() failed. record041b: "+file.read(k1).toString()+"\n");
      }
      if (file.read(k2) != null)
      {
        failMsg.append("commit() failed. record: "+file.read(k2).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of commit().
   *<ul compact>
   *<li>commit() when commitment control has not been started
   *</ul>
  **/
  public void Var042()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record042 ");
      rec.setField(1, "descrip042");
      file.write(rec);

      try
      {
        // commit() takes no action in this case -> no exception should be thrown
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of commit().
   *<ul compact>
   *<li>commit() when commitment control is *NONE
   *</ul>
  **/
  public void Var043()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record043 ");
      rec.setField(1, "descrip043");
      file.write(rec);

      try
      {
        // commit() takes no action in this case -> no exception should be thrown
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *ALL
   *</ul>
  **/
  public void Var044()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record044 ");
      rec.setField(1, "descrip044");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (file.read(k) != null)
      {
        failMsg.append("rollback failed. record is: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *CHG
   *</ul>
  **/
  public void Var045()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                           "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      Object[] k = new Object[1];
      k[0] = records_[7].getField(0);
      String oldRec = file.read(k).toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record045 ");
      rec.setField(1, "descrip045");
      file.update(k,rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      if (!file.read(k).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *CS
   *</ul>
  **/
  public void Var046()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record019 ");
      rec.setField(1, "descrip019");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (file.read(k) != null)
      {
        failMsg.append("rollback failed. record is: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when no changes have been made
   *</ul>
  **/
  public void Var047()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when one change has been made
   *</ul>
  **/
  public void Var048()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] k = new Object[1];
      k[0] = records_[8].getField(0);
      String oldRec = file.read(k).toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record048 ");
      rec.setField(1, "descrip048");
      file.update(k,rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.read(k).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when several changes have been made
   *</ul>
  **/
  public void Var049()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK2.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] k1 = new Object[1];
      Object[] k2 = new Object[1];
      k1[0] = records_[3].getField(0);
      k2[0] = records_[1].getField(0);

      String oldRec = file.read(k2).toString();
      String oldRec2 = file.read(k1).toString();
      Record rec1 = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      Record rec3 = file.getRecordFormat().getNewRecord();

      rec1.setField(0, "record049a");
      rec1.setField(1, "descrip49a");
      file.write(rec1);

      rec2.setField(0, "record049b");
      rec2.setField(1, "descrip49b");
      file.update(k1,rec2);

      rec3.setField(0, "record049c");
      rec3.setField(1, "descrip49c");
      file.write(rec3);

      file.deleteRecord(k2);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      k1[0] = rec1.getField(0);
      if (file.read(k1) != null)
      {
        failMsg.append("rollback failed. rec1: " + file.read(k1).toString() + "\n");
      }
      k1[0] = rec3.getField(0);
      if (file.read(k1) != null)
      {
        failMsg.append("rollback failed. rec3: " + file.read(k1).toString() + "\n");
      }
      k1[0] = records_[3].getField(0);
      if (!file.read(k1).toString().equals(oldRec2))
      {
        failMsg.append("rollback failed. rec2: " + file.read(k1).toString() + "\n");
      }
      if (!file.read(k2).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record: "+file.read(k2).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of rollback().
   *<ul compact>
   *<li>rollback() when commitment control has not been started
   *</ul>
  **/
  public void Var050()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK2.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record050 ");
      rec.setField(1, "descrip050");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("rollback failed. record is: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of rollback().
   *<ul compact>
   *<li>rollback() when commitment control is *NONE
   *</ul>
  **/
  public void Var051()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK2.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record051 ");
      rec.setField(1, "descrip051");
      file.write(rec);

      try
      {
        file.rollback();
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] k = new Object[1];
      k[0] = rec.getField(0);
      if (!file.read(k).toString().equals(rec.toString()))
      {
        failMsg.append("rollback failed. record is: " + file.read(k).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify false returned when commitment control has not been started
   *</ul>
  **/
  public void Var052()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      if (file.isCommitmentControlStarted())
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify true returned when commitment control has been started via
   *the startCommitmentControl method on the AS400File object being wueried.
   *</ul>
  **/
  public void Var053()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.isCommitmentControlStarted())
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify true returned when commitment control was started on a different
   *AS400File object within this application.
   *</ul>
  **/
  public void Var054()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    KeyedFile file2 = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK1.FILE/MBR1.MBR");
      file2 = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCCK2.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file2.isCommitmentControlStarted())
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_ALL<br>
   *Verify the locking level by reading a record, and attempting to end
   *commitment control file without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var055()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        file.readFirst();
        file.endCommitmentControl();
        file.close();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_CHANGE<br>
   *Verify the locking level by writing a record to a file, and attempting to
   *end commitment control without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var056()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "record002 ");
        rec.setField(1, "descrip002");
        file.write(rec);
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_CURSOR_STABILITY
   *Verify the locking level by updating a record to a file, and attempting to
   *end commitment control without calling commit() or rollback().
   * Open the file with COMMIT_LOCK_LEVEL_DEFAULT.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>AS400Exception indicating CPF8355.
   *</ul>
  **/
  public void Var057()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      if (file.getCommitLockLevel() != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed.\n");
      }
      try
      {
        Record rec = file.getRecordFormat().getNewRecord();
        rec.setField(0, "record003 ");
        rec.setField(1, "descrip003");
        file.update(3,rec);
        file.endCommitmentControl();
        failMsg.append("Expected exception didn't occur.");
      }
      catch(AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if((msg.getID().toUpperCase().indexOf("CPF8355") == -1) &&
           (!msg.getText().startsWith("ENDCMTCTL not allowed. Pending changes active.")))  // the returned msg ID may be blank, so also check text
        {
          failMsg.append("CPF8355 not thrown in exception\n");
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>null system
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "system"
   *</ul>
  **/
  public void Var058()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      try
      {
        AS400File.startCommitmentControl(null, AS400File.COMMIT_LOCK_LEVEL_ALL);

        failMsg.append("No exception specifying NullPointerException 'system'\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "system"))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "commitLockLevel"
   *and PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var059()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      try
      {
        AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_NONE);

        failMsg.append("No exception specifying COMMIT_LOCK_LEVEL_DEFAULT\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalArgumentException",                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating
   *COMMITMENT_CONTROL_ALREADY_STARTED
   *</ul>
  **/
  public void Var060()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      try
      {
        AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
        failMsg.append("Expected exception didn't occur.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                     ExtendedIllegalStateException.COMMITMENT_CONTROL_ALREADY_STARTED))
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
      file.endCommitmentControl();
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
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Specify an invalid value for the commitLockLevel.
   *<ul compact>
   *<li>COMMIT_LOCK_LEVEL_DEFAULT
   *<li>-1
   *<li>6
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "commitLockLevel" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var061()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      failMsg.append("No exception specifying COMMIT_LOCK_LEVEL_DEFAULT\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      AS400File.startCommitmentControl(systemObject_, -1);
      failMsg.append("No exception specifying -1\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      AS400File.startCommitmentControl(systemObject_, 6);
      failMsg.append("No exception specifying 6\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException",
                   ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failMsg.append("Incorrect exception info.\n");
        e.printStackTrace(output_);
      }
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of endCommitmentControl().
   *<ul compact>
   *<li>Start commitment control with COMMIT_LOCK_LEVEL_ALL..
   *<li>End commitment control.
   *<li>Open the file, write a record to the file and close the file.
   *<li>Open the file and make sure the record written is in the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record written will be in the file.
   *</ul>
  **/
  public void Var062()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      AS400File.endCommitmentControl(systemObject_);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record007a");
      rec.setField(1, "descrip007");
      file.write(rec);
      file.close();

      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec2 = file.readLast();
      if (!rec.toString().equals(rec2.toString()))
      {
        failMsg.append("Record does not appear to be in the file\n");
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
   *Verify valid usage of endCommitmentControl().
   *<ul compact>
   *<li>Call endCommitmentControl() when commitment control has not been
   *started for the job.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception should be thrown.
   *</ul>
  **/
  public void Var063()
  {
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      AS400File.endCommitmentControl(systemObject_);
      assertCondition(true, "passed with file "+file); 
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed("Unexpected exception.\n");
    }
  }

  /**
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *ALL
   *</ul>
  **/
  public void Var064()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record009 ");
      rec.setField(1, "descrip009");
      file.write(rec);

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *CHG
   *</ul>
  **/
  public void Var065()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record010 ");
      rec.setField(1, "descrip010");
      file.write(rec);

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      AS400File.endCommitmentControl(systemObject_);
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() with commitment control level of *CS
   *</ul>
  **/
  public void Var066()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record011 ");
      rec.setField(1, "descrip011");
      file.write(rec);

      try
      {
        file.commit();
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when no changes have been made
   *</ul>
  **/
  public void Var067()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("commit() failed. record: "+file.readLast().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when one change has been made
   *</ul>
  **/
  public void Var068()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record013 ");
      rec.setField(1, "descrip013");
      file.update(6,rec);

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.read(6).toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: "+file.read(6).toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when several changes have been made
   *</ul>
  **/
  public void Var069()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC3.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec1 = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      Record rec3 = file.getRecordFormat().getNewRecord();

      rec1.setField(0, "record069a");
      rec1.setField(1, "descrip069");
      file.write(rec1);

      rec2.setField(0, "record069b");
      rec2.setField(1, "descrip069");
      file.update(7,rec2);

      rec3.setField(0, "record069c");
      rec3.setField(1, "descrip069");
      file.write(rec3);

      file.deleteRecord(1);

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      if (!file.readLast().toString().equals(rec3.toString()))
      {
        failMsg.append("commit() failed. last record: "+file.readLast().toString()+"\n");
      }
      if (!file.read(7).toString().equals(rec2.toString()))
      {
        failMsg.append("commit() failed. record 7: "+file.readFirst().toString()+"\n");
      }
      if (!file.readFirst().toString().equals(records_[1].toString()))
      {
        failMsg.append("commit() failed. first record: "+file.readFirst().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when commitment control has not been started
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var070()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record015 ");
      rec.setField(1, "descrip015");
      file.write(rec);

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: " + file.readLast().toString() + "\n");
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
      AS400File.endCommitmentControl(systemObject_);
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
   *Verify valid usage of commit().
   *<ul compact>
   *<li>commit() when commitment control is *NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var071()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record016 ");
      rec.setField(1, "descrip016");
      file.write(rec);

      try
      {
        AS400File.commit(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("commit() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("commit() failed. record: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *ALL
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var072()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record017 ");
      rec.setField(1, "descrip017");
      file.write(rec);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *CHG
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var073()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      String oldRec = file.read(8).toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record018 ");
      rec.setField(1, "descrip018");
      file.update(8,rec);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      if (!file.read(8).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.read(8).toString() + "\n");
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
      AS400File.endCommitmentControl(systemObject_);
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() with commitment control level of *CS
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var074()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      String oldRec = file.readLast().toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record019 ");
      rec.setField(1, "descrip019");
      file.write(rec);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when no changes have been made
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var075()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.readLast().toString();

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when one change has been made
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var076()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRec = file.read(3).toString();
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record021 ");
      rec.setField(1, "descrip021");
      file.update(3,rec);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.read(3).toString().equals(oldRec))
      {
        failMsg.append("rollback failed. record is: " + file.read(3).toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when several changes have been made
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No changes are made to the file.
   *</ul>
  **/
  public void Var077()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      String oldRecLast = file.readLast().toString();
      String oldRec4 = file.read(4).toString();
      String oldRec2 = file.read(2).toString();
      Record rec1 = file.getRecordFormat().getNewRecord();
      Record rec2 = file.getRecordFormat().getNewRecord();
      Record rec3 = file.getRecordFormat().getNewRecord();

      rec1.setField(0, "record022a");
      rec1.setField(1, "descrip022");
      file.write(rec1);

      rec2.setField(0, "record022b");
      rec2.setField(1, "descrip022");
      file.update(4,rec2);

      rec3.setField(0, "record022c");
      rec3.setField(1, "descrip022");
      file.write(rec3);

      file.deleteRecord(2);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.read(4).toString().equals(oldRec4))
      {
        failMsg.append("rollback failed. record 4 is: " + file.read(4).toString() + "\n");
      }
      if (!file.readLast().toString().equals(oldRecLast))
      {
        failMsg.append("rollback failed. last record is: " + file.readLast().toString() + "\n");
      }
      if (!(file.readFirst().toString().equals(oldRec2) ||
          file.readNext().toString().equals(oldRec2)))
      {
        failMsg.append("rollback failed. first record is: " + file.readFirst().toString() + "\n");
        failMsg.append("next record is: "+file.readNext().toString()+"\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when commitment control has not been started
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var078()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));

      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record023 ");
      rec.setField(1, "descrip023");
      file.write(rec);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      AS400File.endCommitmentControl(systemObject_);
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
   *Verify valid usage of rollback().
   *<ul compact>
   *<li>rollback() when commitment control is *NONE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exception is thrown.
   *</ul>
  **/
  public void Var079()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMCmtCtlNoKeyFormat(systemObject_));
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = file.getRecordFormat().getNewRecord();
      rec.setField(0, "record024 ");
      rec.setField(1, "descrip024");
      file.write(rec);

      try
      {
        AS400File.rollback(systemObject_);
      }
      catch(Exception e)
      {
        failMsg.append("rollback() failed.\n");
        e.printStackTrace(output_);
      }

      file.close();
      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      if (!file.readLast().toString().equals(rec.toString()))
      {
        failMsg.append("rollback failed. record is: " + file.readLast().toString() + "\n");
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
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify false returned when commitment control has not been started
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>false is returned.
   *</ul>
  **/
  public void Var080()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      if (AS400File.isCommitmentControlStarted(systemObject_))
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify true returned when commitment control has been started via
   *the startCommitmentControl method on the AS400File object being queried.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>true is returned.
   *</ul>
  **/
  public void Var081()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      AS400File.startCommitmentControl(systemObject_, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (!AS400File.isCommitmentControlStarted(systemObject_))
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted().
   *<ul compact>
   *<li>Verify false returned when commitment control was started then ended.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>true is returned.
   *</ul>
  **/
  public void Var082()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    SequentialFile file2 = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC1.FILE/MBR1.MBR");
      file2 = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMCC2.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file2.endCommitmentControl();
      if (AS400File.isCommitmentControlStarted(systemObject_))
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception.\n");
      e.printStackTrace(output_);
    }
    try
    {
      AS400File.endCommitmentControl(systemObject_);
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
   *Verify invalid usage of endCommitmentControl(AS400).
   *<ul compact>
   *<li>Verify passing null throws an exception.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>A NullPointerException with "system" should be thrown.
   *</ul>
  **/
  public void Var083()
  {
    try
    {
      AS400File.endCommitmentControl(null);
      failed("Expected exception did not occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "system"))
      {
        failed(e, "Incorrect exception info.");
      }
      else
      {
        succeeded();
      }
    }
  }
  
  /**
   *Verify invalid usage of isCommitmentControlStarted(AS400).
   *<ul compact>
   *<li>Verify passing null throws an exception.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>A NullPointerException with "system" should be thrown.
   *</ul>
  **/
  public void Var084()
  {
    try
    {
      AS400File.isCommitmentControlStarted(null);
      failed("Expected exception did not occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "system"))
      {
        failed(e, "Incorrect exception info.");
      }
      else
      {
        succeeded();
      }
    }
  }
  
  /**
   *Verify invalid usage of commit(AS400).
   *<ul compact>
   *<li>Verify passing null throws an exception.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>A NullPointerException with "system" should be thrown.
   *</ul>
  **/
  public void Var085()
  {
    try
    {
      AS400File.commit(null);
      failed("Expected exception did not occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "system"))
      {
        failed(e, "Incorrect exception info.");
      }
      else
      {
        succeeded();
      }
    }
  }
  
  /**
   *Verify invalid usage of rollback(AS400).
   *<ul compact>
   *<li>Verify passing null throws an exception.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>A NullPointerException with "system" should be thrown.
   *</ul>
  **/
  public void Var086()
  {
    try
    {
      AS400File.rollback(null);
      failed("Expected exception did not occur.");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "system"))
      {
        failed(e, "Incorrect exception info.");
      }
      else
      {
        succeeded();
      }
    }
  }
  
  /**
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started (natively).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating
   *COMMITMENT_CONTROL_ALREADY_STARTED
   *</ul>
  **/
  public void Var087()
  {
    boolean onAS400_ = false;
    try
    {
      String s = System.getProperty("os.name");
      if (s != null && s.equalsIgnoreCase("OS/400"))
      {
        onAS400_ = true;
      }
      else
      {
        onAS400_ = false;
      }
    }
    catch(SecurityException e)
    {
      onAS400_ = false;
    }
    
    if (!onAS400_)
    {
      notApplicable("native only"); // This variation must be run natively.
      return;
    }

    if (!isNative_)
    {
	notApplicable("native only");
	return; 
    } 
    AS400 x = null;
    AS400 y = null;
    AS400 z = null;
    try
    {
      AS400File.endCommitmentControl(systemObject_); // end it first, just in case
      
      x = new AS400();
      y = new AS400();
      y.setMustUseSockets(true);
      AS400File.startCommitmentControl(x, AS400File.COMMIT_LOCK_LEVEL_ALL);
      AS400File.startCommitmentControl(y, AS400File.COMMIT_LOCK_LEVEL_ALL);
      try
      {
        z = new AS400();
        AS400File.startCommitmentControl(z, AS400File.COMMIT_LOCK_LEVEL_ALL);
        failed("Expected exception did not occur when starting commitment control.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.COMMITMENT_CONTROL_ALREADY_STARTED))
        {
          failed(e, "Incorrect exception info.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    try
    {
      AS400File.endCommitmentControl(x);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(y);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(z);
    }
    catch(Exception e) {}

  }

  /**
   *Verify invalid usage of startCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started (natively).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalStateException indicating
   *COMMITMENT_CONTROL_ALREADY_STARTED
   *</ul>
  **/
  public void Var088()
  {
    boolean onAS400_ = false;
    try
    {
      String s = System.getProperty("os.name");
      if (s != null && s.equalsIgnoreCase("OS/400"))
      {
        onAS400_ = true;
      }
      else
      {
        onAS400_ = false;
      }
    }
    catch(SecurityException e)
    {
      onAS400_ = false;
    }
    
    if (!onAS400_)
    {
      notApplicable("native only"); // This variation must be run natively.
      return;
    }
    
    AS400 x = null;
    AS400 y = null;
    try
    {
      x = new AS400();
      y = new AS400();
      y.setMustUseSockets(true);
      AS400File.startCommitmentControl(x, AS400File.COMMIT_LOCK_LEVEL_ALL);
      AS400File.startCommitmentControl(y, AS400File.COMMIT_LOCK_LEVEL_ALL);
      try
      {
        AS400File.startCommitmentControl(y, AS400File.COMMIT_LOCK_LEVEL_ALL);
        failed("Expected exception did not occur.");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException",
                         ExtendedIllegalStateException.COMMITMENT_CONTROL_ALREADY_STARTED))
        {
          failed(e, "Incorrect exception info.");
        }
        else
        {
          succeeded();
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    try
    {
      AS400File.endCommitmentControl(x);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(y);
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of startCommitmentControl() and endCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started and then stopped (natively).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Variation should be successful.
   *</ul>
  **/
  public void Var089()
  {
    boolean onAS400_ = false;
    try
    {
      String s = System.getProperty("os.name");
      if (s != null && s.equalsIgnoreCase("OS/400"))
      {
        onAS400_ = true;
      }
      else
      {
        onAS400_ = false;
      }
    }
    catch(SecurityException e)
    {
      onAS400_ = false;
    }
    
    if (!onAS400_ || ! isNative_)
    {
      notApplicable("native only"); // This variation must be run natively.
      return;
    }
    
    AS400 x = null;
    AS400 y = null;
    AS400 x2 = null;
    AS400 z = null;
    try
    {
      x = new AS400();
      y = new AS400();
      y.setMustUseSockets(true);
      AS400File.startCommitmentControl(x, AS400File.COMMIT_LOCK_LEVEL_ALL);
      AS400File.startCommitmentControl(y, AS400File.COMMIT_LOCK_LEVEL_ALL);
      x2 = new AS400();
      AS400File.endCommitmentControl(x2);
        
      z = new AS400();
      AS400File.startCommitmentControl(z, AS400File.COMMIT_LOCK_LEVEL_ALL);
      if (AS400File.isCommitmentControlStarted(x) &&
          AS400File.isCommitmentControlStarted(y) &&
          AS400File.isCommitmentControlStarted(x2) &&
          AS400File.isCommitmentControlStarted(z))
      {
        succeeded();
      }
      else
      {
        failed("Commitment control failed for 4 connections.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred.");
    }
    try
    {
      AS400File.endCommitmentControl(x);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(x2);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(y);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(z);
    }
    catch(Exception e) {}

  }

  /**
   *Verify valid usage of startCommitmentControl() and endCommitmentControl().
   *<ul compact>
   *<li>Attempt to start commitment control after it has already been started and then stopped (natively).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Variation should be successful.
   *</ul>
  **/
  public void Var090()
  {
    StringBuffer sb = new StringBuffer(); 
    boolean onAS400_ = false;
    try
    {
      String s = System.getProperty("os.name");
      if (s != null && s.equalsIgnoreCase("OS/400"))
      {
        onAS400_ = true;
      }
      else
      {
        onAS400_ = false;
      }
    }
    catch(SecurityException e)
    {
      onAS400_ = false;
    }
    
    if (!onAS400_)
    {
      notApplicable("native only"); // This variation must be run natively.
      return;
    }
    
    AS400 x = null;
    AS400 y = null;
    AS400 z = null;
    try
    {
      x = new AS400();
      x.connectService(AS400.RECORDACCESS); 
      Job[] xJobs = x.getJobs(AS400.RECORDACCESS); 
      dumpJobInfo(sb, "xJobs are", xJobs);
      
      y = new AS400();
      y.setMustUseSockets(true);
      y.connectService(AS400.RECORDACCESS); 
      Job[] yJobs = y.getJobs(AS400.RECORDACCESS); 
      dumpJobInfo(sb, "yJobs are", yJobs);
      
      z = new AS400();
      z.setMustUseSockets(true);
      z.connectService(AS400.RECORDACCESS); 
      Job[] zJobs = z.getJobs(AS400.RECORDACCESS); 
      dumpJobInfo(sb, "zJobs are", zJobs);
      
      // System.out.println(sb.toString());
      sb.append("y starting commitment control\n"); 
      AS400File.startCommitmentControl(y, AS400File.COMMIT_LOCK_LEVEL_ALL);
      sb.append("x starting commitment control\n"); 
      AS400File.startCommitmentControl(x, AS400File.COMMIT_LOCK_LEVEL_ALL);
      sb.append("a starting commitment control\n"); 
      AS400File.startCommitmentControl(z, AS400File.COMMIT_LOCK_LEVEL_ALL);
      sb.append("y ending commitment control\n"); 
      AS400File.endCommitmentControl(y);
      sb.append("y starting commitment control\n"); 
      AS400File.startCommitmentControl(y, AS400File.COMMIT_LOCK_LEVEL_ALL);
      sb.append("checking isCommitmentControlStarted\n"); 
      
      if (AS400File.isCommitmentControlStarted(z) &&
          AS400File.isCommitmentControlStarted(x) &&
          AS400File.isCommitmentControlStarted(y))
      {
        succeeded();
      }
      else
      {
        failed("Commitment control failed for 3 connections.  "+sb.toString());
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception occurred. " + sb.toString());
    }
    try
    {
      AS400File.endCommitmentControl(x);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(y);
    }
    catch(Exception e) {}
    try
    {
      AS400File.endCommitmentControl(z);
    }
    catch(Exception e) {}

  }

  static  void dumpJobInfo(StringBuffer sb, String string, Job[] jobs) {
    sb.append(string); 
    for (int i = 0; i < jobs.length; i++) { 
      sb.append(" "); 
      sb.append(jobs[i].getNumber());
      sb.append("/");
      sb.append(jobs[i].getUser());
      sb.append("/");
      sb.append(jobs[i].getName());
    }
    sb.append("\n"); 
  }

}


