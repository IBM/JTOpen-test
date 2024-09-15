///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMGetSet.java
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
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.CharacterFieldDescription;

/**
 *Testcase DDMGetSet.  This test class verifies valid and invalid usage of
 *the AS400File getter and setter methods:
 *<ul compact>
 *<li>getBlockingFactor()
 *<li>getCommitLockLevel()
 *<li>getExplicitLocks()
 *<li>getFileName()
 *<li>getMemberName()
 *<li>getPath()
 *<li>getRecordFormat()
 *<li>getSystem()
 *<li>isCommitmentControlStarted()
 *<li>isOpen()
 *<li>isReadOnly()
 *<li>isReadWrite()
 *<li>isWriteOnly()
 *<li>setPath(String)
 *<li>setRecordFormat(RecordFormat)
 *<li>setSystem(AS400)
 *</ul>
**/
public class DDMGetSet extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMGetSet";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  CommandCall cmd_;
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMGetSet(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMGetSet", 129,
          variationsToRun, runMode, fileOutputStream);
    cmd_ = new CommandCall(systemObject_);
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
      e.printStackTrace(System.out); 
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

    if ((allVariations || variationsToRun_.contains("91")) &&
        runMode_ != ATTENDED)
    {
      setVariation(91);
      Var091();
    }

    if ((allVariations || variationsToRun_.contains("92")) &&
        runMode_ != ATTENDED)
    {
      setVariation(92);
      Var092();
    }

    if ((allVariations || variationsToRun_.contains("93")) &&
        runMode_ != ATTENDED)
    {
      setVariation(93);
      Var093();
    }

    if ((allVariations || variationsToRun_.contains("94")) &&
        runMode_ != ATTENDED)
    {
      setVariation(94);
      Var094();
    }

    if ((allVariations || variationsToRun_.contains("95")) &&
        runMode_ != ATTENDED)
    {
      setVariation(95);
      Var095();
    }

    if ((allVariations || variationsToRun_.contains("96")) &&
        runMode_ != ATTENDED)
    {
      setVariation(96);
      Var096();
    }

    if ((allVariations || variationsToRun_.contains("97")) &&
        runMode_ != ATTENDED)
    {
      setVariation(97);
      Var097();
    }

    if ((allVariations || variationsToRun_.contains("98")) &&
        runMode_ != ATTENDED)
    {
      setVariation(98);
      Var098();
    }

    if ((allVariations || variationsToRun_.contains("99")) &&
        runMode_ != ATTENDED)
    {
      setVariation(99);
      Var099();
    }

    if ((allVariations || variationsToRun_.contains("100")) &&
        runMode_ != ATTENDED)
    {
      setVariation(100);
      Var100();
    }

    if ((allVariations || variationsToRun_.contains("101")) &&
        runMode_ != ATTENDED)
    {
      setVariation(101);
      Var101();
    }

    if ((allVariations || variationsToRun_.contains("102")) &&
        runMode_ != ATTENDED)
    {
      setVariation(102);
      Var102();
    }

    if ((allVariations || variationsToRun_.contains("103")) &&
        runMode_ != ATTENDED)
    {
      setVariation(103);
      Var103();
    }

    if ((allVariations || variationsToRun_.contains("104")) &&
        runMode_ != ATTENDED)
    {
      setVariation(104);
      Var104();
    }

    if ((allVariations || variationsToRun_.contains("105")) &&
        runMode_ != ATTENDED)
    {
      setVariation(105);
      Var105();
    }

    if ((allVariations || variationsToRun_.contains("106")) &&
        runMode_ != ATTENDED)
    {
      setVariation(106);
      Var106();
    }

    if ((allVariations || variationsToRun_.contains("107")) &&
        runMode_ != ATTENDED)
    {
      setVariation(107);
      Var107();
    }

    if ((allVariations || variationsToRun_.contains("108")) &&
        runMode_ != ATTENDED)
    {
      setVariation(108);
      Var108();
    }

    if ((allVariations || variationsToRun_.contains("109")) &&
        runMode_ != ATTENDED)
    {
      setVariation(109);
      Var109();
    }

    if ((allVariations || variationsToRun_.contains("110")) &&
        runMode_ != ATTENDED)
    {
      setVariation(110);
      Var110();
    }

    if ((allVariations || variationsToRun_.contains("111")) &&
        runMode_ != ATTENDED)
    {
      setVariation(111);
      Var111();
    }

    if ((allVariations || variationsToRun_.contains("112")) &&
        runMode_ != ATTENDED)
    {
      setVariation(112);
      Var112();
    }

    if ((allVariations || variationsToRun_.contains("113")) &&
        runMode_ != ATTENDED)
    {
      setVariation(113);
      Var113();
    }

    if ((allVariations || variationsToRun_.contains("114")) &&
        runMode_ != ATTENDED)
    {
      setVariation(114);
      Var114();
    }

    if ((allVariations || variationsToRun_.contains("115")) &&
        runMode_ != ATTENDED)
    {
      setVariation(115);
      Var115();
    }

    if ((allVariations || variationsToRun_.contains("116")) &&
        runMode_ != ATTENDED)
    {
      setVariation(116);
      Var116();
    }

    if ((allVariations || variationsToRun_.contains("117")) &&
        runMode_ != ATTENDED)
    {
      setVariation(117);
      Var117();
    }

    if ((allVariations || variationsToRun_.contains("118")) &&
        runMode_ != ATTENDED)
    {
      setVariation(118);
      Var118();
    }

    if ((allVariations || variationsToRun_.contains("119")) &&
        runMode_ != ATTENDED)
    {
      setVariation(119);
      Var119();
    }

    if ((allVariations || variationsToRun_.contains("120")) &&
        runMode_ != ATTENDED)
    {
      setVariation(120);
      Var120();
    }

    if ((allVariations || variationsToRun_.contains("121")) &&
        runMode_ != ATTENDED)
    {
      setVariation(121);
      Var121();
    }

    if ((allVariations || variationsToRun_.contains("122")) &&
        runMode_ != ATTENDED)
    {
      setVariation(122);
      Var122();
    }

    if ((allVariations || variationsToRun_.contains("123")) &&
        runMode_ != ATTENDED)
    {
      setVariation(123);
      Var123();
    }

    if ((allVariations || variationsToRun_.contains("124")) &&
        runMode_ != ATTENDED)
    {
      setVariation(124);
      Var124();
    }

    if ((allVariations || variationsToRun_.contains("125")) &&
        runMode_ != ATTENDED)
    {
      setVariation(125);
      Var125();
    }

    if ((allVariations || variationsToRun_.contains("126")) &&
        runMode_ != ATTENDED)
    {
      setVariation(126);
      Var126();
    }

    if ((allVariations || variationsToRun_.contains("127")) &&
        runMode_ != ATTENDED)
    {
      setVariation(127);
      Var127();
    }

    if ((allVariations || variationsToRun_.contains("128")) &&
        runMode_ != ATTENDED)
    {
      setVariation(128);
      Var128();
    }

    if ((allVariations || variationsToRun_.contains("129")) &&
        runMode_ != ATTENDED)
    {
      setVariation(129);
      Var129();
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
      sb.append("COMMAND: "+command+" rc = "+rc+"\n");

    // If there are any messages then save the ones that potentially
    // indicate problems.
    AS400Message[] msgs = cmd_.getMessageList();
    if (msgs.length > 0 && msgs[0].getID().toUpperCase().startsWith("CPF"))
    {
      msg = msgs[0].getID().toUpperCase();
      sb.append("...COMMAND: "+command+" msg = "+msg+"\n");
    }
  }
  catch(Exception e)
  {
    msg = e.toString();
    e.printStackTrace(output_);
    sb.append("...COMMAND: "+command+" exception = "+msg+"\n");

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
    try
    {
    // Create library DDMTEST
    String msg = runCommand("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)", sb);
    if (msg != null && !msg.equals("CPF2111"))
    {
      output_.println("Failure executing 'CRTLIB LIB(" + testLib_ + ") AUT(*ALL)'");
      output_.println(msg);
      throw new Exception("");
    }

    // Cleanup the old journal.
    msg = runCommand("ENDJRNPF FILE(*ALL) JRN(QGPL/JT4DDMJRN) ", sb); 
    msg = runCommand("dltjrn JRN(QGPL/JT4DDMJRN) ", sb); 
    msg = runCommand("DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV)  DLTOPT(*IGNINQMSG) ", sb);

    // Create journal receiver and journal if it does not already exist
    msg = runCommand("CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')",sb);
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
      output_.println(sb.toString()); 
      throw new Exception("");
    }


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
    StringBuffer sb = new StringBuffer(); 
    boolean success = true;
    try
    {

    // Delete the DDMTest library and the journal file.  The journal
    // receive should be automatically deleted due to the way that the
    // journal and receiver were deleted.
    String msg = deleteLibrary(cmd_, testLib_); 
    if (msg != null)
    {
      output_.println("Failure deleting library " + testLib_ ); 
      output_.println(msg);
      success = false;
    }
    msg = runCommand("DLTJRN QGPL/JT4DDMJRN", sb);
    if (msg != null)
    {
      output_.println("Failure executing 'DLTJRN QGPL/JT4DDMJRN'");
      output_.println(msg);
      success = false;
    }
  }
  catch(Exception e)
  {
    System.out.println("Cleanup unsuccessful.  Some files may have been left in " + testLib_ + " and QGPL");
    e.printStackTrace(output_);
    output_.print(sb.toString()); 
    throw e;
  }

  if (!success)
  {
    output_.print(sb.toString()); 
    throw new Exception ("Cleanup was unsuccessful.");
  }
}



  /**
   *Verify valid usage of getBlockingFactor() on a Sequential File.
   *<ul>
   *<li>Invoke getBlockingFactor() before file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return a default value of 0.
   *</ul>
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      int gbf = 1;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gbf != 0)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getBlockingFactor() on a Sequential File.
   *<ul>
   *<li>Open the file, specifying a blocking factor > 0.
   *<li>Verify getBlockingFactor() returns correct blocking factor.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return the specified blocking factor.
   *</ul>
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gbf = 1;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gbf != 3)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getBlockingFactor() on a Sequential File.
   *<ul>
   *<li>Open the file, specifying a blocking factor of 0.
   *<li>Verify getBlockingFactor() returns a calculated blocking factor.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return 2048/(16+record length).
   *</ul>
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gbf = 1;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gbf != (2048/(16+10)))
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getBlockingFactor() on a Sequential File.
   *<ul>
   *<li>Open the file, specifying a blocking factor.
   *<li>Close the file and then invoke getBlockingFactor().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return a default value of 0.
   *</ul>
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gbf = 1;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (gbf != 3)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
      }

      file.close();

      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (gbf != 0)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Invoke getCommitLockLevel() before commitment control has been started
   *and before the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Open the file, specifying a commitment lock level.
   *<li>Invoke getCommitLockLevel() before commitment control has been started.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control, specifying a commitment lock level.
   *<li>Invoke getCommitLockLevel() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_ALL.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_ALL)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_CHANGE.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_DEFAULT.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_NONE.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_NONE)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Sequential File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a commitment lock level.
   *<li>Close the file, end commitment control, and then invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
      }

      file.close();
      file.endCommitmentControl();

      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getExplicitLocks() on a Sequential File.
   *<ul>
   *<li>Invoke getExplicitLocks() when no locks have been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getExplicitLocks() will return a default array size of 0.
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GEL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        int[] gel = file.getExplicitLocks();
        if (gel.length != 0)
        {
          failMsg.append("getExplicitLocks() failed: "+gel.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getExplicitLocks() on a Sequential File.
   *<ul>
   *<li>Set locks on the file by calling lock() and specifying one of
   *the following:
   *<ul>
   *<li>READ_EXCLUSIVE_LOCK
   *<li>READ_ALLOW_SHARED_READ_LOCK
   *<li>READ_ALLOW_SHARED_WRITE_LOCK
   *<li>WRITE_EXCLUSIVE_LOCK
   *<li>WRITE_ALLOW_SHARED_READ_LOCK
   *<li>WRITE_ALLOW_SHARED_WRITE_LOCK
   *</ul>
   *<li>Verify that getExplicitLocks() returns the specified locks.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getExplicitLocks() will return the specified locks.
   *</ul>
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GEL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
      try
      {
        int[] gel = file.getExplicitLocks();
        if (gel.length != 1 ||
            gel[0] != AS400File.READ_ALLOW_SHARED_WRITE_LOCK)
        {
          failMsg.append("getExplicitLocks() failed: "+gel[0]+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getExplicitLocks() on a Sequential File.
   *<ul>
   *<li>Set locks on the file, then release them by calling releaseExplicitLocks().
   *<li>Verify that getExplicitLocks() returns no locks.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getExplicitLocks() will return a default array size of 0.
   *</ul>
  **/
  public void Var016()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GEL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
      file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
      file.releaseExplicitLocks();
      try
      {
        int[] gel = file.getExplicitLocks();
        if (gel.length != 0)
        {
          failMsg.append("getExplicitLocks() failed: "+gel.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getFileName() on a Sequential File.
   *<ul>
   *<li>Verify that getFileName() returns an empty string when the
   *IFS filename has not yet been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFileName() will return a default empty string.
   *</ul>
  **/
  public void Var017()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      String name = new String();
      try
      {
        name = file.getFileName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getFileName() failed: "+name+"\n");
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
   *Verify valid usage of getFileName() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid IFS filename.
   *<li>Verify that getFileName() returns the specified filename.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFileName() will return the specified filename.
   *</ul>
  **/
  public void Var018()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GFNAME.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getFileName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("GFNAME"))
      {
        failMsg.append("getFileName() failed: "+name+"\n");
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
   *Verify valid usage of getFileName() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid IFS filename.
   *<li>Change the name of the file using setPath().
   *<li>Verify that getFileName() returns the new filename.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFileName() will return the specified filename.
   *</ul>
  **/
  public void Var019()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GFNAME.FILE/MBR1.MBR");
      file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/GETFILEN.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getFileName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("GETFILEN"))
      {
        failMsg.append("getFileName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Sequential File.
   *<ul>
   *<li>Verify that getMemberName() returns an empty string when the
   *IFS path has not yet been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return a default empty string.
   *</ul>
  **/
  public void Var020()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Sequential File.
   *<ul>
   *<li>Create a file with a valid IFS pathname, not using a special member value.
   *<li>Invoke getMemberName() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the specified member name.
   *</ul>
  **/
  public void Var021()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("MBR1"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Sequential File.
   *<ul>
   *<li>Create a file with a valid IFS pathname, using a special member value.
   *<li>Invoke getMemberName() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the specified special member value.
   *</ul>
  **/
  public void Var022()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/%FIRST%.MBR");
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("*FIRST"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Sequential File.
   *<ul>
   *<li>Create a file with a valid IFS pathname, using a special member value.
   *<li>Invoke getMemberName() after opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the corresponding member name.
   *</ul>
  **/
  public void Var023()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
    }
    catch(Exception e) {}
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/%FIRST%.MBR");
      file.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("MBR1"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Sequential File.
   *<ul>
   *<li>Create a file with a valid IFS pathname.
   *<li>Change the member name by calling setPath().
   *<li>Verify getMemberName() returns the new member name.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the specified member name.
   *</ul>
  **/
  public void Var024()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/%FIRST%.MBR");
      file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/MBR2.MBR");
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("MBR2"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getPath() on a Sequential File.
   *<ul>
   *<li>Verify getPath() returns an empty string when the
   *IFS pathname has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() will return a default empty string.
   *</ul>
  **/
  public void Var025()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      String name = new String();
      try
      {
        name = file.getPath();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getPath() failed: "+name+"\n");
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
   *Verify valid usage of getPath() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid IFS pathname on the constructor.
   *<li>Verify getPath() returns the specified pathname.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() will return the specified pathname.
   *</ul>
  **/
  public void Var026()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR";
      file = new SequentialFile(systemObject_, path);
      String name = new String();
      try
      {
        name = file.getPath();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(path))
      {
        failMsg.append("getPath() failed: "+name+"\n");
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
   *Verify valid usage of getPath() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid IFS pathname.
   *<li>Change the pathname by calling setPath().
   *<li>Verify getPath() returns the new pathname.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() will return the specified pathname.
   *</ul>
  **/
  public void Var027()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH2.FILE/MBR2.MBR";
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR");
      file.setPath(path);
      String name = new String();
      try
      {
        name = file.getPath();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(path))
      {
        failMsg.append("getPath() failed: "+name+"\n");
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
   *Verify valid usage of getRecordFormat() on a Sequential File.
   *<ul>
   *<li>Create a file without specifying a record format.
   *<li>Verify getRecordFormat() returns null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() will return a default value of null.
   *</ul>
  **/
  public void Var028()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETRF.FILE/MBR1.MBR");
      RecordFormat rf = new RecordFormat();
      try
      {
        rf = file.getRecordFormat();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (rf != null)
      {
        failMsg.append("getRecordFormat() failed: "+rf.toString()+"\n");
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
   *Verify valid usage of getRecordFormat() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a record format on the constructor.
   *<li>Verify getRecordFormat() returns the specified record format.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() will return the specified record format.
   *</ul>
  **/
  public void Var029()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETRF.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      RecordFormat rf = new RecordFormat();
      RecordFormat rf2 = new DDMChar10NoKeyFormat(systemObject_);
      try
      {
        rf = file.getRecordFormat();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (rf.equals(rf2))
      {
        failMsg.append("getRecordFormat() failed: "+rf.toString()+"\n");
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
   *Verify valid usage of getRecordFormat() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid record format.
   *<li>Change the record format by calling setRecordFormat().
   *<li>Verify getRecordFormat() returns the new record format.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() will return the specified record format.
   *</ul>
  **/
  public void Var030()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETRF.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      RecordFormat rec = new RecordFormat();
      rec.addFieldDescription(new CharacterFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "Field01"));
      rec.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field02"));
      file.setRecordFormat(rec);
      RecordFormat rf = new RecordFormat();
      try
      {
        rf = file.getRecordFormat();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (rf != rec)
      {
        failMsg.append("getRecordFormat() failed: "+rf.toString()+"\n");
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
   *Verify valid usage of getSystem() on a Sequential File.
   *<ul>
   *<li>Verify getSystem() returns null when no system has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return a default value of null.
   *</ul>
  **/
  public void Var031()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      AS400 sys = new AS400();
      try
      {
        sys = file.getSystem();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (sys != null)
      {
        failMsg.append("getSystem() failed: "+sys.toString()+"\n");
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
   *Verify valid usage of getSystem() on a Sequential File.
   *<ul>
   *<li>Verify getSystem() returns the current AS/400 system object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return the connected AS/400 system object.
   *</ul>
  **/
  public void Var032()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETSYS.FILE/MBR1.MBR");
      try
      {
        AS400 sys = new AS400();
        sys = file.getSystem();
        if (!sys.equals(systemObject_))
        {
          failMsg.append("getSystem() failed: "+sys.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getSystem() on a Sequential File.
   *<ul>
   *<li>Change the system by calling setSystem().
   *<li>Verify getSystem() returns the new AS/400 system object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return the connected AS/400 system object.
   *</ul>
  **/
  public void Var033()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETSYS.FILE/MBR1.MBR");
      AS400 newsys = new AS400();
      newsys = systemObject_;
      file.setSystem(newsys);
      try
      {
        AS400 sys = file.getSystem();
        if (sys != newsys)
        {
          failMsg.append("getSystem() failed: "+sys.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of isCommitmentControlStarted() on a Sequential File.
   *<ul>
   *<li>Invoke isCommitmentControlStarted() without starting commitment control.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isCommitmentControlStarted() will return false.
   *</ul>
  **/
  public void Var034()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISCCS.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
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
   *Verify valid usage of isCommitmentControlStarted() on a Sequential File.
   *<ul>
   *<li>Invoke isCommitmentControlStarted() after starting commitment control.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isCommitmentControlStarted() will return true.
   *</ul>
  **/
  public void Var035()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISCCS.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      boolean yesno = false;
      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
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
      file.close();
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted() on a Sequential File.
   *<ul>
   *<li>Invoke isCommitmentControlStarted() after starting and then
   *ending commitment control.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isCommitmentControlStarted() will return false.
   *</ul>
  **/
  public void Var036()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISCCS.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      boolean yesno = false;
      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }

      file.endCommitmentControl();

      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
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
      file.close();
      file.endCommitmentControl();
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
   *Verify valid usage of isOpen() on a Sequential File.
   *<ul>
   *<li>Invoke isOpen() without opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isOpen() will return false.
   *</ul>
  **/
  public void Var037()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISOPEN.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isOpen() failed.\n");
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
   *Verify valid usage of isOpen() on a Sequential File.
   *<ul>
   *<li>Invoke isOpen() after opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isOpen() will return true.
   *</ul>
  **/
  public void Var038()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISOPEN.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isOpen() failed.\n");
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
   *Verify valid usage of isOpen() on a Sequential File.
   *<ul>
   *<li>Invoke isOpen() after opening and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isOpen() will return false.
   *</ul>
  **/
  public void Var039()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISOPEN.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isOpen() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isOpen() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isReadOnly() before opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return false.
   *</ul>
  **/
  public void Var040()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isReadOnly() after opening the file as read only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return true.
   *</ul>
  **/
  public void Var041()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isReadOnly() after opening the file as read only
   *and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return false.
   *</ul>
  **/
  public void Var042()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isReadOnly() after opening the file as something other than read only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return false.
   *</ul>
  **/
  public void Var043()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Sequential File.
   *<ul>
   *<li>Invoke isReadWrite() before opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return false.
   *</ul>
  **/
  public void Var044()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Sequential File.
   *<ul>
   *<li>Invoke isReadWrite() after opening the file as read-write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return true.
   *</ul>
  **/
  public void Var045()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Sequential File.
   *<ul>
   *<li>Invoke isReadWrite() after opening the file as read-write
   *and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return false.
   *</ul>
  **/
  public void Var046()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Sequential File.
   *<ul>
   *<li>Invoke isReadWrite() after opening the file as something other than read-write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return false.
   *</ul>
  **/
  public void Var047()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isWriteOnly() before opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return false.
   *</ul>
  **/
  public void Var048()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isWriteOnly() after opening the file as write only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return true.
   *</ul>
  **/
  public void Var049()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isWriteOnly() after opening the file as write only
   *and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return false.
   *</ul>
  **/
  public void Var050()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Sequential File.
   *<ul>
   *<li>Invoke isWriteOnly() after opening the file as something other than write only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return false.
   *</ul>
  **/
  public void Var051()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify invalid usage of setPath() on a Sequential File.
   *<ul>
   *<li>Invoke setPath(String), specifying null for String.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setPath() throws a NullPointerException with text of "name".
   *</ul>
  **/
  public void Var052()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      try
      {
        file.setPath(null);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "name"))
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
   *Verify invalid usage of setPath() on a Sequential File.
   *<ul>
   *<li>Invoke setPath(String) after a connection has already been made.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setPath() throws an ExtendedIllegalStateException with text of "name"
   *and value of PROPERTY_NOT_CHANGED.
   *</ul>
  **/
  public void Var053()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/SPATH.FILE/MBR1.MBR");
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                         ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
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
   *Verify invalid usage of setPath() on a Sequential File.
   *<ul>
   *<li>Invoke setPath(String), specifying an invalid object type.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setPath() throws an IllegalPathNameException with text
   *consisting of the invalid pathname and value of OBJECT_TYPE_NOT_VALID.
   *</ul>
  **/
  public void Var054()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.BLAH";
      try
      {
        file.setPath(path);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "IllegalPathNameException", path,
                         IllegalPathNameException.OBJECT_TYPE_NOT_VALID))
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
   *Verify valid usage of setPath() on a Sequential File.
   *<ul>
   *<li>Invoke setPath(String), specifying a valid IFS pathname.
   *<li>Verify using getPath().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The pathname will be set.
   *</ul>
  **/
  public void Var055()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR";
      try
      {
        file.setPath(path);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (!file.getPath().equals(path))
      {
        failMsg.append("setPath() failed: "+file.getPath()+"\n");
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
   *Verify invalid usage of setRecordFormat() on a Sequential File.
   *<ul>
   *<li>Invoke setRecordFormat(format), specifying null for format.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setRecordFormat() throws a NullPointerException with text of "recordFormat".
   *</ul>
  **/
  public void Var056()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      try
      {
        file.setRecordFormat((RecordFormat)null);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "recordFormat"))
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
   *Verify valid usage of setRecordFormat() on a Sequential File.
   *<ul>
   *<li>Invoke setRecordFormat(format), specifying a valid format, before
   *the file is created.
   *<li>Verify using getRecordFormat().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format will be set.
   *</ul>
  **/
  public void Var057()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      RecordFormat rf = null;
      try
      {
        rf = new DDMChar10NoKeyFormat(systemObject_);
        file.setRecordFormat(rf);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception info.\n");
        e.printStackTrace(output_);
      }
      if (rf != file.getRecordFormat())
      {
        failMsg.append("setRecordFormat() failed.\n");
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
   *Verify valid usage of setRecordFormat() on a Sequential File.
   *<ul>
   *<li>Create the file, specifying a record format.
   *<li>Invoke setRecordFormat(format), specifying a different format.
   *<li>Verify using getRecordFormat().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format will be set to the new format.
   *</ul>
  **/
  public void Var058()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETRF.FILE/MBR1.MBR");
      file.create(new DDMChar10NoKeyFormat(systemObject_), "*BLANK");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "Field01"));
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field02"));
      try
      {
        file.setRecordFormat(rf);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception info.\n");
        e.printStackTrace(output_);
      }
      if (!rf.equals(file.getRecordFormat()))
      {
        failMsg.append("setRecordFormat() failed.\n");
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
   *Verify invalid usage of setSystem() on a Sequential File.
   *<ul>
   *<li>Invoke setSystem(AS400), specifying null for AS400.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setSystem() throws a NullPointerException with text of "system".
   *</ul>
  **/
  public void Var059()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      try
      {
        file.setSystem(null);
        failMsg.append("Expected exception didn't occur.\n");
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
   *Verify invalid usage of setSystem() on a Sequential File.
   *<ul>
   *<li>Invoke setSystem(AS400) after a connection has already been made.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setSystem() throws a ExtendedIllegalStateException with text of "system"
   *and value of PROPERTY_NOT_CHANGED.
   *</ul>
  **/
  public void Var060()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETSYS.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      AS400 sys = new AS400();
      try
      {
        file.setSystem(sys);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                         ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
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
   *Verify valid usage of setSystem() on a Sequential File.
   *<ul>
   *<li>Invoke setSystem(AS400) specifying a valid AS400 system
   *before a connection has been made.
   *<li>Verify using getSystem().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The system will be set.
   *</ul>
  **/
  public void Var061()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      AS400 sys = new AS400();
      sys = systemObject_;
      try
      {
        file.setSystem(sys);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!sys.equals(file.getSystem()))
      {
        failMsg.append("setSystem() failed.\n");
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
   *Verify valid usage of getBlockingFactor() on a Keyed File.
   *<ul>
   *<li>Invoke getBlockingFactor() before file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return a default value of 0.
   *</ul>
  **/
  public void Var062()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      int gbf = 1;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gbf != 0)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getBlockingFactor() on a Keyed File.
   *<ul>
   *<li>Open the file, specifying a blocking factor > 0.
   *<li>Verify getBlockingFactor() returns correct blocking factor.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return the specified blocking factor.
   *</ul>
  **/
  public void Var063()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gbf = 0;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gbf != 3)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getBlockingFactor() on a Keyed File.
   *<ul>
   *<li>Open the file, specifying a blocking factor of 0.
   *<li>Verify getBlockingFactor() returns a calculated blocking factor.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return 2048/(16+record length).
   *</ul>
  **/
  public void Var064()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gbf = 0;
      int block = 2048/(file.getRecordFormat().getNewRecord().getRecordLength() + 16);
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gbf != block)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getBlockingFactor() on a Keyed File.
   *<ul>
   *<li>Open the file, specifying a blocking factor.
   *<li>Close the file and then invoke getBlockingFactor().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getBlockingFactor() will return a default value of 0.
   *</ul>
  **/
  public void Var065()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GBFACT.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gbf = 0;
      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (gbf != 3)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
      }

      file.close();

      try
      {
        gbf = file.getBlockingFactor();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (gbf != 0)
      {
        failMsg.append("getBlockingFactor() failed: "+gbf+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Invoke getCommitLockLevel() before commitment control has been started
   *and before the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var066()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Open the file, specifying a commitment lock level.
   *<li>Invoke getCommitLockLevel() before commitment control has been started.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var067()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control, specifying a commitment lock level.
   *<li>Invoke getCommitLockLevel() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var068()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_ALL.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var069()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_ALL)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_CHANGE.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var070()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_CURSOR_STABILITY.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var071()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_CURSOR_STABILITY)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_DEFAULT.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var072()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_DEFAULT);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_DEFAULT)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a
   *lock level of COMMIT_LOCK_LEVEL_NONE.
   *<li>Invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return the specified commitment lock level.
   *</ul>
  **/
  public void Var073()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_NONE)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getCommitLockLevel() on a Keyed File.
   *<ul>
   *<li>Start commitment control and open the file, specifying a commitment lock level.
   *<li>Close the file, end commitment control, and then invoke getCommitLockLevel().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getCommitLockLevel() will return a default value of -1.
   *</ul>
  **/
  public void Var074()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    CommandCall c = new CommandCall(systemObject_);
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      c.run("STRJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // Start journaling
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GCLL.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_CHANGE);
      int gcll = -5;
      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != AS400File.COMMIT_LOCK_LEVEL_CHANGE)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
      }

      file.close();
      file.endCommitmentControl();

      try
      {
        gcll = file.getCommitLockLevel();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (gcll != -1)
      {
        failMsg.append("getCommitLockLevel() failed: "+gcll+"\n");
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
      c.run("ENDJRNPF FILE(" + testLib_ + "/GCLL) JRN(QGPL/JT4DDMJRN)"); // End journaling
      file.endCommitmentControl();
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
   *Verify valid usage of getExplicitLocks() on a Keyed File.
   *<ul>
   *<li>Invoke getExplicitLocks() when no locks have been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getExplicitLocks() will return a default array size of 0.
   *</ul>
  **/
  public void Var075()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GEL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        int[] gel = file.getExplicitLocks();
        if (gel.length != 0)
        {
          failMsg.append("getExplicitLocks() failed: "+gel.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getExplicitLocks() on a Keyed File.
   *<ul>
   *<li>Set locks on the file by calling lock() and specifying one of
   *the following:
   *<ul>
   *<li>READ_EXCLUSIVE_LOCK
   *<li>READ_ALLOW_SHARED_READ_LOCK
   *<li>READ_ALLOW_SHARED_WRITE_LOCK
   *<li>WRITE_EXCLUSIVE_LOCK
   *<li>WRITE_ALLOW_SHARED_READ_LOCK
   *<li>WRITE_ALLOW_SHARED_WRITE_LOCK
   *</ul>
   *<li>Verify that getExplicitLocks() returns the specified locks.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getExplicitLocks() will return the specified locks.
   *</ul>
  **/
  public void Var076()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GEL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
      try
      {
        int[] gel = file.getExplicitLocks();
        if (gel.length != 1 ||
            gel[0] != AS400File.READ_ALLOW_SHARED_WRITE_LOCK)
        {
          failMsg.append("getExplicitLocks() failed: "+gel[0]+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getExplicitLocks() on a Keyed File.
   *<ul>
   *<li>Set locks on the file, then release them by calling releaseExplicitLocks().
   *<li>Verify that getExplicitLocks() returns no locks.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getExplicitLocks() will return a default array size of 0.
   *</ul>
  **/
  public void Var077()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GEL.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
      file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
      file.releaseExplicitLocks();
      try
      {
        int[] gel = file.getExplicitLocks();
        if (gel.length != 0)
        {
          failMsg.append("getExplicitLocks() failed: "+gel.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getFileName() on a Keyed File.
   *<ul>
   *<li>Verify that getFileName() returns an empty string when the
   *IFS filename has not yet been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFileName() will return a default empty string.
   *</ul>
  **/
  public void Var078()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      String name = new String();
      try
      {
        name = file.getFileName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getFileName() failed: "+name+"\n");
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
   *Verify valid usage of getFileName() on a Keyed File.
   *<ul>
   *<li>Create a file, specifying a valid IFS filename.
   *<li>Verify that getFileName() returns the specified filename.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFileName() will return the specified filename.
   *</ul>
  **/
  public void Var079()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GFNAME.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getFileName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("GFNAME"))
      {
        failMsg.append("getFileName() failed: "+name+"\n");
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
   *Verify valid usage of getFileName() on a Keyed File.
   *<ul>
   *<li>Create a file, specifying a valid IFS filename.
   *<li>Change the name of the file using setPath().
   *<li>Verify that getFileName() returns the new filename.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFileName() will return the specified filename.
   *</ul>
  **/
  public void Var080()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GFNAME.FILE/MBR1.MBR");
      file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/GETFILEN.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getFileName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("GETFILEN"))
      {
        failMsg.append("getFileName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Keyed File.
   *<ul>
   *<li>Verify that getMemberName() returns an empty string when the
   *IFS path has not yet been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return a default empty string.
   *</ul>
  **/
  public void Var081()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Keyed File.
   *<ul>
   *<li>Create a file with a valid IFS pathname, not using a special member value.
   *<li>Invoke getMemberName() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the specified member name.
   *</ul>
  **/
  public void Var082()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("MBR1"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Keyed File.
   *<ul>
   *<li>Create a file with a valid IFS pathname, using a special member value.
   *<li>Invoke getMemberName() prior to opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the specified special member value.
   *</ul>
  **/
  public void Var083()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/%FIRST%.MBR");
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("*FIRST"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Keyed File.
   *<ul>
   *<li>Create a file with a valid IFS pathname, using a special member value.
   *<li>Invoke getMemberName() after opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the corresponding member name.
   *</ul>
  **/
  public void Var084()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
    }
    catch(Exception e) {}
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/%FIRST%.MBR");
      file.setRecordFormat(new DDMChar10KeyFormat(systemObject_));
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("MBR1"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getMemberName() on a Keyed File.
   *<ul>
   *<li>Create a file with a valid IFS pathname.
   *<li>Change the member name by calling setPath().
   *<li>Verify getMemberName() returns the new member name.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getMemberName() will return the specified member name.
   *</ul>
  **/
  public void Var085()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/%FIRST%.MBR");
      file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/GMNAME.FILE/MBR2.MBR");
      String name = new String();
      try
      {
        name = file.getMemberName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals("MBR2"))
      {
        failMsg.append("getMemberName() failed: "+name+"\n");
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
   *Verify valid usage of getPath() on a Keyed File.
   *<ul>
   *<li>Verify getPath() returns an empty string when the
   *IFS pathname has not been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() will return a default empty string.
   *</ul>
  **/
  public void Var086()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      String name = new String();
      try
      {
        name = file.getPath();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getPath() failed: "+name+"\n");
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
   *Verify valid usage of getPath() on a Keyed File.
   *<ul>
   *<li>Create a file, specifying a valid IFS pathname on the constructor.
   *<li>Verify getPath() returns the specified pathname.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() will return the specified pathname.
   *</ul>
  **/
  public void Var087()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR";
      file = new KeyedFile(systemObject_, path);
      String name = new String();
      try
      {
        name = file.getPath();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(path))
      {
        failMsg.append("getPath() failed: "+name+"\n");
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
   *Verify valid usage of getPath() on a Keyed File.
   *<ul>
   *<li>Create a file, specifying a valid IFS pathname.
   *<li>Change the pathname by calling setPath().
   *<li>Verify getPath() returns the new pathname.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() will return the specified pathname.
   *</ul>
  **/
  public void Var088()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH2.FILE/MBR2.MBR";
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR");
      file.setPath(path);
      String name = new String();
      try
      {
        name = file.getPath();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(path))
      {
        failMsg.append("getPath() failed: "+name+"\n");
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
   *Verify valid usage of getRecordFormat() on a Keyed File.
   *<ul>
   *<li>Create a file without specifying a record format.
   *<li>Verify getRecordFormat() returns null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() will return a default value of null.
   *</ul>
  **/
  public void Var089()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETRF.FILE/MBR1.MBR");
      RecordFormat rf = new RecordFormat();
      try
      {
        rf = file.getRecordFormat();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (rf != null)
      {
        failMsg.append("getRecordFormat() failed: "+rf.toString()+"\n");
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
   *Verify valid usage of getRecordFormat() on a Keyed File.
   *<ul>
   *<li>Create a file, specifying a record format on the constructor.
   *<li>Verify getRecordFormat() returns the specified record format.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() will return the specified record format.
   *</ul>
  **/
  public void Var090()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETRF.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      RecordFormat rf = new RecordFormat();
      RecordFormat rf2 = new DDMChar10KeyFormat(systemObject_);
      try
      {
        rf = file.getRecordFormat();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (rf.equals(rf2))
      {
        failMsg.append("getRecordFormat() failed: "+rf.toString()+"\n");
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
   *Verify valid usage of getRecordFormat() on a Keyed File.
   *<ul>
   *<li>Create a file, specifying a valid record format.
   *<li>Change the record format by calling setRecordFormat().
   *<li>Verify getRecordFormat() returns the specified record format.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getRecordFormat() will return the specified record format.
   *</ul>
  **/
  public void Var091()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETRF.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      RecordFormat rec = new RecordFormat();
      rec.addFieldDescription(new CharacterFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "Field01"));
      rec.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field02"));
      rec.addKeyFieldDescription("Field02");
      file.setRecordFormat(rec);
      RecordFormat rf = new RecordFormat();
      try
      {
        rf = file.getRecordFormat();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (rf != rec)
      {
        failMsg.append("getRecordFormat() failed: "+rf.toString()+"\n");
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
   *Verify valid usage of getSystem() on a Keyed File.
   *<ul>
   *<li>Verify getSystem() returns null when no system has been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return a default value of null.
   *</ul>
  **/
  public void Var092()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      AS400 sys = new AS400();
      try
      {
        sys = file.getSystem();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (sys != null)
      {
        failMsg.append("getSystem() failed: "+sys.toString()+"\n");
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
   *Verify valid usage of getSystem() on a Keyed File.
   *<ul>
   *<li>Verify getSystem() returns the current AS/400 system object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return the connected AS/400 system object.
   *</ul>
  **/
  public void Var093()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETSYS.FILE/MBR1.MBR");
      try
      {
        AS400 sys = new AS400();
        sys = file.getSystem();
        if (!sys.equals(systemObject_))
        {
          failMsg.append("getSystem() failed: "+sys.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of getSystem() on a Keyed File.
   *<ul>
   *<li>Change the system by calling setSystem().
   *<li>Verify getSystem() returns the new AS/400 system object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return the connected AS/400 system object.
   *</ul>
  **/
  public void Var094()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GETSYS.FILE/MBR1.MBR");
      AS400 newsys = new AS400();
      newsys = systemObject_;
      file.setSystem(newsys);
      try
      {
        AS400 sys = file.getSystem();
        if (sys != newsys)
        {
          failMsg.append("getSystem() failed: "+sys.toString()+"\n");
        }
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
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
   *Verify valid usage of isCommitmentControlStarted() on a Keyed File.
   *<ul>
   *<li>Invoke isCommitmentControlStarted() without starting commitment control.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isCommitmentControlStarted() will return false.
   *</ul>
  **/
  public void Var095()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISCCS.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
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
   *Verify valid usage of isCommitmentControlStarted() on a Keyed File.
   *<ul>
   *<li>Invoke isCommitmentControlStarted() after starting commitment control.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isCommitmentControlStarted() will return true.
   *</ul>
  **/
  public void Var096()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISCCS.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      boolean yesno = false;
      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
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
      file.close();
      file.endCommitmentControl();
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
   *Verify valid usage of isCommitmentControlStarted() on a Keyed File.
   *<ul>
   *<li>Invoke isCommitmentControlStarted() after starting and
   *ending commitment control.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isCommitmentControlStarted() will return true.
   *</ul>
  **/
  public void Var097()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISCCS.FILE/MBR1.MBR");
      file.startCommitmentControl(AS400File.COMMIT_LOCK_LEVEL_ALL);
      boolean yesno = false;
      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isCommitmentControlStarted() failed.\n");
      }

      file.endCommitmentControl();

      try
      {
        yesno = file.isCommitmentControlStarted();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
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
      file.close();
      file.endCommitmentControl();
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
   *Verify valid usage of isOpen() on a Keyed File.
   *<ul>
   *<li>Invoke isOpen() without opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isOpen() will return false.
   *</ul>
  **/
  public void Var098()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISOPEN.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isOpen() failed.\n");
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
   *Verify valid usage of isOpen() on a Keyed File.
   *<ul>
   *<li>Invoke isOpen() after opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isOpen() will return true.
   *</ul>
  **/
  public void Var099()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISOPEN.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isOpen() failed.\n");
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
   *Verify valid usage of isOpen() on a Keyed File.
   *<ul>
   *<li>Invoke isOpen() after opening and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isOpen() will return false.
   *</ul>
  **/
  public void Var100()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISOPEN.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isOpen() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isOpen();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isOpen() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isReadOnly() before opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return false.
   *</ul>
  **/
  public void Var101()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isReadOnly() after opening the file as read only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return true.
   *</ul>
  **/
  public void Var102()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isReadOnly() after opening the file as read only
   *and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return false.
   *</ul>
  **/
  public void Var103()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isReadOnly() after opening the file as something other than read only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadOnly() will return false.
   *</ul>
  **/
  public void Var104()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Keyed File.
   *<ul>
   *<li>Invoke isReadWrite() before opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return false.
   *</ul>
  **/
  public void Var105()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Keyed File.
   *<ul>
   *<li>Invoke isReadWrite() after opening the file as read-write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return true.
   *</ul>
  **/
  public void Var106()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Keyed File.
   *<ul>
   *<li>Invoke isReadWrite() after opening the file as read-write
   *and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return false.
   *</ul>
  **/
  public void Var107()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isReadWrite() on a Keyed File.
   *<ul>
   *<li>Invoke isReadWrite() after opening the file as something other than read-write.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadWrite() will return false.
   *</ul>
  **/
  public void Var108()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISRW.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadWrite();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadWrite() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isWriteOnly() before opening the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return false.
   *</ul>
  **/
  public void Var109()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isWriteOnly() after opening the file as write only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return true.
   *</ul>
  **/
  public void Var110()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isWriteOnly() after opening the file as write only
   *and then closing the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return false.
   *</ul>
  **/
  public void Var111()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.WRITE_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
      }

      file.close();

      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify valid usage of isWriteOnly() on a Keyed File.
   *<ul>
   *<li>Invoke isWriteOnly() after opening the file as something other than write only.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isWriteOnly() will return false.
   *</ul>
  **/
  public void Var112()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISWRITE.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isWriteOnly();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isWriteOnly() failed.\n");
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
   *Verify invalid usage of setPath() on a Keyed File.
   *<ul>
   *<li>Invoke setPath(String), specifying null for String.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setPath() throws a NullPointerException with text of "name".
   *</ul>
  **/
  public void Var113()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      try
      {
        file.setPath(null);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "name"))
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
   *Verify invalid usage of setPath() on a Keyed File.
   *<ul>
   *<li>Invoke setPath(String) after a connection has already been made.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setPath() throws an ExtendedIllegalStateException with text of "name"
   *and value of PROPERTY_NOT_CHANGED.
   *</ul>
  **/
  public void Var114()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      try
      {
        file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/SPATH.FILE/MBR1.MBR");
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                         ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
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
   *Verify invalid usage of setPath() on a Keyed File.
   *<ul>
   *<li>Invoke setPath(String), specifying an invalid object type.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setPath() throws an IllegalPathNameException with text
   *consisting of the invalid pathname and value of OBJECT_TYPE_NOT_VALID.
   *</ul>
  **/
  public void Var115()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.BLAH";
      try
      {
        file.setPath(path);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "IllegalPathNameException", path,
                         IllegalPathNameException.OBJECT_TYPE_NOT_VALID))
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
   *Verify valid usage of setPath() on a Keyed File.
   *<ul>
   *<li>Invoke setPath(String), specifying a valid IFS pathname.
   *<li>Verify using getPath().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The pathname will be set.
   *</ul>
  **/
  public void Var116()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      String path = "/QSYS.LIB/" + testLib_ + ".LIB/SETPATH.FILE/MBR1.MBR";
      try
      {
        file.setPath(path);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }

      if (!file.getPath().equals(path))
      {
        failMsg.append("setPath() failed: "+file.getPath()+"\n");
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
   *Verify invalid usage of setRecordFormat() on a Keyed File.
   *<ul>
   *<li>Invoke setRecordFormat(format), specifying null for format.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setRecordFormat() throws a NullPointerException with text of "recordFormat".
   *</ul>
  **/
  public void Var117()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      try
      {
        file.setRecordFormat((RecordFormat)null);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "recordFormat"))
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
   *Verify valid usage of setRecordFormat() on a Keyed File.
   *<ul>
   *<li>Invoke setRecordFormat(format), specifying a valid format, before
   *the file is created.
   *<li>Verify using getRecordFormat().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format will be set.
   *</ul>
  **/
  public void Var118()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    RecordFormat rf = null;
    try
    {
      file = new KeyedFile();
      try
      {
        rf = new DDMChar10KeyFormat(systemObject_);
        file.setRecordFormat(rf);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception info.\n");
        e.printStackTrace(output_);
      }
      if (rf != file.getRecordFormat())
      {
        failMsg.append("setRecordFormat() failed.\n");
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
   *Verify valid usage of setRecordFormat() on a Keyed File.
   *<ul>
   *<li>Create the file, specifying a record format.
   *<li>Invoke setRecordFormat(format), specifying a different format.
   *<li>Verify using getRecordFormat().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format will be set to the new format.
   *</ul>
  **/
  public void Var119()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETRF.FILE/MBR1.MBR");
      file.create(new DDMChar10KeyFormat(systemObject_), "*BLANK");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "Field01"));
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(15, systemObject_.getCcsid(), systemObject_), "Field02"));
      rf.addKeyFieldDescription("Field02");
      try
      {
        file.setRecordFormat(rf);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception info.\n");
        e.printStackTrace(output_);
      }
      if (!rf.equals(file.getRecordFormat()))
      {
        failMsg.append("setRecordFormat() failed.\n");
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
   *Verify invalid usage of setSystem() on a Keyed File.
   *<ul>
   *<li>Invoke setSystem(AS400), specifying null for AS400.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setSystem() throws a NullPointerException with text of "system".
   *</ul>
  **/
  public void Var120()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      try
      {
        file.setSystem(null);
        failMsg.append("Expected exception didn't occur.\n");
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
   *Verify invalid usage of setSystem() on a Keyed File.
   *<ul>
   *<li>Invoke setSystem(AS400) after a connection has already been made.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>setSystem() throws a ExtendedIllegalStateException with text of "system"
   *and value of PROPERTY_NOT_CHANGED.
   *</ul>
  **/
  public void Var121()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/SETSYS.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      AS400 sys = new AS400();
      try
      {
        file.setSystem(sys);
        failMsg.append("Expected exception didn't occur.\n");
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                         ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
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
   *Verify valid usage of setSystem() on a Keyed File.
   *<ul>
   *<li>Invoke setSystem(AS400) specifying a valid AS400 system
   *before a connection has been made.
   *<li>Verify using getSystem().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The system will be set.
   *</ul>
  **/
  public void Var122()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedFile file = null;
    try
    {
      file = new KeyedFile();
      AS400 sys = new AS400();
      sys = systemObject_;
      try
      {
        file.setSystem(sys);
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!sys.equals(file.getSystem()))
      {
        failMsg.append("setSystem() failed.\n");
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
   *Verify valid usage of isReadNoUpdate() on a Sequential File.
   *<ul>
   *<li>Invoke isReadNoUpdate() before opening a file without calling setReadNoUpdate().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadNoUpdate() will return false.
   *</ul>
  **/
  public void Var123()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      boolean yesno = false;
      try
      {
        yesno = file.isReadNoUpdate();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadNoUpdate() on a Sequential File.
   *<ul>
   *<li>Invoke isReadNoUpdate() after opening a file in read/write mode 
   * without calling setReadNoUpdate().
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadNoUpdate() will return false.
   *</ul>
  **/
  public void Var124()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.create(10, "*DATA", "*BLANK");
      file.open(AS400File.READ_WRITE, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      boolean yesno = false;
      try
      {
        yesno = file.isReadNoUpdate();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadNoUpdate() on a Sequential File.
   *<ul>
   *<li>Invoke isReadNoUpdate() after calling setReadNoUpdate(true).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadNoUpdate() will return true.
   *</ul>
  **/
  public void Var125()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.setReadNoUpdate(true);
      boolean yesno = false;
      try
      {
        yesno = file.isReadNoUpdate();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of isReadNoUpdate() on a Sequential File.
   *<ul>
   *<li>Invoke isReadNoUpdate() after calling setReadNoUpdate(true) & then
   * setReadNoUpdate(false).
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>isReadNoUpdate() will return false.
   *</ul>
  **/
  public void Var126()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/ISREAD.FILE/MBR1.MBR");
      file.setReadNoUpdate(true);
      file.setReadNoUpdate(false);
      boolean yesno = false;
      try
      {
        yesno = file.isReadNoUpdate();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (yesno)
      {
        failMsg.append("isReadOnly() failed.\n");
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
   *Verify valid usage of getLibraryName() on a Sequential File.
   *<ul>
   *<li>Verify that getLibraryName() returns an empty string when the
   *IFS filename has not yet been set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getLibraryName() will return a default empty string.
   *</ul>
  **/
  public void Var127()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile();
      String name = new String();
      try
      {
        name = file.getLibraryName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(""))
      {
        failMsg.append("getLibraryName() failed: "+name+"\n");
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
   *Verify valid usage of getLibraryName() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid IFS filename.
   *<li>Verify that getLibraryName() returns the specified filename.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getLibraryName() will return the specified filename.
   *</ul>
  **/
  public void Var128()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GFNAME.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getLibraryName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(testLib_))
      {
        failMsg.append("getLibraryName() failed: "+name+"\n");
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
   *Verify valid usage of getLibraryName() on a Sequential File.
   *<ul>
   *<li>Create a file, specifying a valid IFS filename.
   *<li>Change the name of the file using setPath().
   *<li>Verify that getLibraryName() returns the new filename.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getLibraryName() will return the specified filename.
   *</ul>
  **/
  public void Var129()
  {
    StringBuffer failMsg = new StringBuffer();
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/GFNAME.FILE/MBR1.MBR");
      file.setPath("/QSYS.LIB/" + testLib_ + ".LIB/GETFILEN.FILE/MBR1.MBR");
      String name = new String();
      try
      {
        name = file.getLibraryName();
      }
      catch(Exception e)
      {
        failMsg.append("Unexpected exception.\n");
        e.printStackTrace(output_);
      }
      if (!name.equals(testLib_))
      {
        failMsg.append("getLibraryName() failed: "+name+"\n");
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

}
