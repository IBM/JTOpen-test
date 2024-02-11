///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FDSet.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.io.FileOutputStream;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.*;

import test.Testcase;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
  Testcase FDSet.  This test class verifes valid and invalid usage of the
  setters for the FieldDescription subclasses.  Note: This test class does not
  verify the isVariableLength() and setVariableLength() methods for those
  classes which implement the VariableLengthFieldDescription interface.  These
  methods are verified by the FDConstructAndGet test class.
**/
public class FDSet extends Testcase
{
  /**
  Constructor.  This is called from the FDTest constructor.
  **/
  public FDSet(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "FDSet", 170,
          variationsToRun, runMode, fileOutputStream);
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
    if ((allVariations || variationsToRun_.contains("130")) &&
        runMode_ != ATTENDED)
    {
      setVariation(130);
      Var130();
    }
    if ((allVariations || variationsToRun_.contains("131")) &&
        runMode_ != ATTENDED)
    {
      setVariation(131);
      Var131();
    }
    if ((allVariations || variationsToRun_.contains("132")) &&
        runMode_ != ATTENDED)
    {
      setVariation(132);
      Var132();
    }
    if ((allVariations || variationsToRun_.contains("133")) &&
        runMode_ != ATTENDED)
    {
      setVariation(133);
      Var133();
    }
    if ((allVariations || variationsToRun_.contains("134")) &&
        runMode_ != ATTENDED)
    {
      setVariation(134);
      Var134();
    }
    if ((allVariations || variationsToRun_.contains("135")) &&
        runMode_ != ATTENDED)
    {
      setVariation(135);
      Var135();
    }
    if ((allVariations || variationsToRun_.contains("136")) &&
        runMode_ != ATTENDED)
    {
      setVariation(136);
      Var136();
    }
    if ((allVariations || variationsToRun_.contains("137")) &&
        runMode_ != ATTENDED)
    {
      setVariation(137);
      Var137();
    }
    if ((allVariations || variationsToRun_.contains("138")) &&
        runMode_ != ATTENDED)
    {
      setVariation(138);
      Var138();
    }
    if ((allVariations || variationsToRun_.contains("139")) &&
        runMode_ != ATTENDED)
    {
      setVariation(139);
      Var139();
    }
    if ((allVariations || variationsToRun_.contains("140")) &&
        runMode_ != ATTENDED)
    {
      setVariation(140);
      Var140();
    }
    if ((allVariations || variationsToRun_.contains("141")) &&
        runMode_ != ATTENDED)
    {
      setVariation(141);
      Var141();
    }
    if ((allVariations || variationsToRun_.contains("142")) &&
        runMode_ != ATTENDED)
    {
      setVariation(142);
      Var142();
    }
    if ((allVariations || variationsToRun_.contains("143")) &&
        runMode_ != ATTENDED)
    {
      setVariation(143);
      Var143();
    }
    if ((allVariations || variationsToRun_.contains("144")) &&
        runMode_ != ATTENDED)
    {
      setVariation(144);
      Var144();
    }
    if ((allVariations || variationsToRun_.contains("145")) &&
        runMode_ != ATTENDED)
    {
      setVariation(145);
      Var145();
    }
    if ((allVariations || variationsToRun_.contains("146")) &&
        runMode_ != ATTENDED)
    {
      setVariation(146);
      Var146();
    }
    if ((allVariations || variationsToRun_.contains("147")) &&
        runMode_ != ATTENDED)
    {
      setVariation(147);
      Var147();
    }
    if ((allVariations || variationsToRun_.contains("148")) &&
        runMode_ != ATTENDED)
    {
      setVariation(148);
      Var148();
    }
    if ((allVariations || variationsToRun_.contains("149")) &&
        runMode_ != ATTENDED)
    {
      setVariation(149);
      Var149();
    }
    if ((allVariations || variationsToRun_.contains("150")) &&
        runMode_ != ATTENDED)
    {
      setVariation(150);
      Var150();
    }
    if ((allVariations || variationsToRun_.contains("151")) &&
        runMode_ != ATTENDED)
    {
      setVariation(151);
      Var151();
    }
    if ((allVariations || variationsToRun_.contains("152")) &&
        runMode_ != ATTENDED)
    {
      setVariation(152);
      Var152();
    }
    if ((allVariations || variationsToRun_.contains("153")) &&
        runMode_ != ATTENDED)
    {
      setVariation(153);
      Var153();
    }
    if ((allVariations || variationsToRun_.contains("154")) &&
        runMode_ != ATTENDED)
    {
      setVariation(154);
      Var154();
    }
    if ((allVariations || variationsToRun_.contains("155")) &&
        runMode_ != ATTENDED)
    {
      setVariation(155);
      Var155();
    }
    if ((allVariations || variationsToRun_.contains("156")) &&
        runMode_ != ATTENDED)
    {
      setVariation(156);
      Var156();
    }
    if ((allVariations || variationsToRun_.contains("157")) &&
        runMode_ != ATTENDED)
    {
      setVariation(157);
      Var157();
    }
    if ((allVariations || variationsToRun_.contains("158")) &&
        runMode_ != ATTENDED)
    {
      setVariation(158);
      Var158();
    }
    if ((allVariations || variationsToRun_.contains("159")) &&
        runMode_ != ATTENDED)
    {
      setVariation(159);
      Var159();
    }
    if ((allVariations || variationsToRun_.contains("160")) &&
        runMode_ != ATTENDED)
    {
      setVariation(160);
      Var160();
    }
    if ((allVariations || variationsToRun_.contains("161")) &&
        runMode_ != ATTENDED)
    {
      setVariation(161);
      Var161();
    }
    if ((allVariations || variationsToRun_.contains("162")) &&
        runMode_ != ATTENDED)
    {
      setVariation(162);
      Var162();
    }
    if ((allVariations || variationsToRun_.contains("163")) &&
        runMode_ != ATTENDED)
    {
      setVariation(163);
      Var163();
    }
    if ((allVariations || variationsToRun_.contains("164")) &&
        runMode_ != ATTENDED)
    {
      setVariation(164);
      Var164();
    }
    if ((allVariations || variationsToRun_.contains("165")) &&
        runMode_ != ATTENDED)
    {
      setVariation(165);
      Var165();
    }
    if ((allVariations || variationsToRun_.contains("166")) &&
        runMode_ != ATTENDED)
    {
      setVariation(166);
      Var166();
    }
    if ((allVariations || variationsToRun_.contains("167")) &&
        runMode_ != ATTENDED)
    {
      setVariation(167);
      Var167();
    }
    if ((allVariations || variationsToRun_.contains("168")) &&
        runMode_ != ATTENDED)
    {
      setVariation(168);
      Var168();
    }
    if ((allVariations || variationsToRun_.contains("169")) &&
        runMode_ != ATTENDED)
    {
      setVariation(169);
      Var169();
    }
    if ((allVariations || variationsToRun_.contains("170")) &&
        runMode_ != ATTENDED)
    {
      setVariation(170);
      Var170();
    }
  }

  /**
   *Verify valid usage of setTEXT().
   *<ul compact>
   *<li>Text of 50 characters
   *<li>Text of 1 character
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getText() will return the text set.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      String txt1 = "B";
      String txt50 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
      FieldDescription f = new BinaryFieldDescription();
      f.setTEXT(txt1);
      if (!f.getTEXT().equals(txt1))
      {
        failed("Text not set as expected: " + f.getTEXT());
        return;
      }
      f.setTEXT(txt50);
      if (!f.getTEXT().equals(txt50))
      {
        failed("Text not set as expected: " + f.getTEXT());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setTEXT().
   *<ul compact>
   *<li>Text of 51 characters
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "text" and
   *LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      String txt51 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
      FieldDescription f = new BinaryFieldDescription();
      f.setTEXT(txt51);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "text", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setTEXT().
   *<ul compact>
   *<li>Specify null for text
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "text"
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setTEXT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "text"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setREFFLD().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getREFFLD() will return the REFFLD string  set.
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      String refFld = "";
      FieldDescription f = new BinaryFieldDescription();
      f.setREFFLD(refFld);
      if (!f.getREFFLD().equals(refFld))
      {
        failed("REFFLD not set as expected: " + f.getREFFLD());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setREFFLD().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "refFld"
   *</ul>
  **/
  public void Var005()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setREFFLD(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "refFld"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setKeyFieldFunctions().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getKeyFieldFunctions() will return the array of key
   *field functions set.
   *</ul>
  **/
  public void Var006()
  {
    try
    {
      String[] kf = new String[3];
      kf[0] = "FIFO";
      kf[1] = "ZONED";
      kf[2] = "SIGNED";
      FieldDescription f = new BinaryFieldDescription();
      f.setKeyFieldFunctions(kf);
      String[] chk = f.getKeyFieldFunctions();
      for (int i = 0; i < kf.length; ++i)
      {
        if (!chk[i].equals(kf[i]))
        {
          failed("key field functions not set as expected");
          return;
        }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setKeyFieldFunctions().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "keyFunctions"
   *</ul>
  **/
  public void Var007()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setKeyFieldFunctions(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "keyFunctions"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setKeyFieldFunctions().
   *<ul compact>
   *<li>Specify an array of length 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "keyFunctions" and
   *LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var008()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setKeyFieldFunctions(new String[0]);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "keyFunctions", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setFieldName().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getFieldName() will return the field name set.
   *</ul>
  **/
  public void Var009()
  {
    try
    {
      String fieldName = "binaryFieldName";
      FieldDescription f = new BinaryFieldDescription();
      f.setFieldName(fieldName);
      if (!f.getFieldName().equals(fieldName))
      {
        failed("Field name not set as expected: " + f.getFieldName());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setFieldName().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "fieldName"
   *</ul>
  **/
  public void Var010()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setFieldName(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "fieldName"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setDDSName().
   *<ul compact>
   *<li>Specify name of length 10
   *<li>Specify name of length 1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getDDSName() will return the field name set in uppercase.
   *</ul>
  **/
  public void Var011()
  {
    try
    {
      String ddsName1 = "binaryFiel";
      String ddsName2 = "b";
      FieldDescription f = new BinaryFieldDescription();
      f.setDDSName(ddsName1);
      if (!f.getDDSName().equals(ddsName1.toUpperCase()))
      {
        failed("DDS name not set as expected: " + f.getDDSName());
        return;
      }
      f.setDDSName(ddsName2);
      if (!f.getDDSName().equals(ddsName2.toUpperCase()))
      {
        failed("DDS name not set as expected: " + f.getDDSName());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setDDSName().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "ddsName"
   *</ul>
  **/
  public void Var012()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setDDSName(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setDDSName().
   *<ul compact>
   *<li>Specify a String of length 11
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "ddsName" and
   *LENGTH_NOT_VALID.
   *</ul>
  **/
  public void Var013()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setDDSName("12345678901");
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "ddsName", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setCOLHDG().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getCOLHDG() will return the column heading set.
   *</ul>
  **/
  public void Var014()
  {
    try
    {
      String colHdg = "'Employee' 'Number'";
      FieldDescription f = new BinaryFieldDescription();
      f.setCOLHDG(colHdg);
      if (!f.getCOLHDG().equals(colHdg))
      {
        failed("COLHDG not set as expected: " + f.getCOLHDG());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setCOLHDG().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "colHdg"
   *</ul>
  **/
  public void Var015()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setCOLHDG(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "colHdg"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setALWNULL().
   *<ul compact>
   *<li>Verify true value
   *<li>Verify false value
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getALWNULL() will return true when true was specified.
   *<li>getALWNULL() will return false when false was specified.
   *</ul>
  **/
  public void Var016()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setALWNULL(true);
      if (f.getALWNULL() != true)
      {
        failed("ALWNULL not set as expected: " + String.valueOf(f.getALWNULL()));
        return;
      }
      f.setALWNULL(false);
      if (f.getALWNULL() != false)
      {
        failed("ALWNULL not set as expected: " + String.valueOf(f.getALWNULL()));
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setALIAS().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getALIAS() will return the alias set.
   *</ul>
  **/
  public void Var017()
  {
    try
    {
      String alias = "binaryFieldName";
      FieldDescription f = new BinaryFieldDescription();
      f.setALIAS(alias);
      if (!f.getALIAS().equals(alias))
      {
        failed("ALIAS not set as expected: " + f.getALIAS());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setALIAS().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "alias"
   *</ul>
  **/
  public void Var018()
  {
    try
    {
      FieldDescription f = new BinaryFieldDescription();
      f.setALIAS(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "alias"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of ArrayFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var019()
  {
    try
    {
      AS400Array dt = new AS400Array(new AS400Bin4(), 5);
      ArrayFieldDescription f = new ArrayFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of ArrayFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var020()
  {
    try
    {
      ArrayFieldDescription f = new ArrayFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of (ArrayFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var021()
  {
    try
    {
      ArrayFieldDescription f = new ArrayFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (ArrayFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var022()
  {
    try
    {
      ArrayFieldDescription f = new ArrayFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (ArrayFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var023()
  {
    try
    {
      ArrayFieldDescription f = new ArrayFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of BinaryFieldDescription.setDataType().
   *<ul compact>
   *<li>setDataType(AS400Bin4)
   *<li>setDataType(AS400Bin2)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var024()
  {
    try
    {
      AS400Bin2 dt2 = new AS400Bin2();
      AS400Bin4 dt4 = new AS400Bin4();
      AS400Bin8 dt8 = new AS400Bin8(); //@D0A
      AS400UnsignedBin2 dtu2 = new AS400UnsignedBin2(); //@D0A
      AS400UnsignedBin4 dtu4 = new AS400UnsignedBin4(); //@D0A

      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDataType(dt2);
      if (f.getDataType() != dt2)
      {
        failed("Data type not set as expected, bin2");
        return;
      }
      f.setDataType(dt4);
      if (f.getDataType() != dt4)
      {
        failed("Data type not set as expected, bin4");
        return;
      }
      
      //@D0A

      f.setDataType(dt8);
      if (f.getDataType() != dt8)
      {
        failed("Data type not set as expected, bin8");
        return;
      }
      f.setDataType(dtu2);
      if (f.getDataType() != dtu2)
      {
        failed("Data type not set as expected, unsigned bin2");
        return;
      }
      f.setDataType(dtu4);
      if (f.getDataType() != dtu4)
      {
        failed("Data type not set as expected, unsigned bin4");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of BinaryFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null for setDataType(AS400Bin2)
   *<li>Specify null for setDataType(AS400Bin4)
   *<li>Specify null for setDataType(AS400Bin8)
   *<li>Specify null for setDataType(AS400UnsignedBin2)
   *<li>Specify null for setDataType(AS400UnsignedBin4)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var025()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDataType((AS400Bin2)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, bin2");
        return;
      }
    }
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDataType((AS400Bin4)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, bin4");
        return;
      }
    }
    
    //@D0A

    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDataType((AS400Bin8)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, bin4");
        return;
      }
    }
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDataType((AS400UnsignedBin2)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, bin4");
        return;
      }
    }
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDataType((AS400UnsignedBin4)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, bin4");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of BinaryFieldDescription.setDFT().
   *<ul compact>
   *<li>setDFT(Integer)
   *<li>setDFT(Short)
   *<li>setDFT(Long)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var026()
  {
    try
    {
      Integer i = new Integer(5555);
      Short s = new Short((short)1);
      Long l = new Long(123456789L); //@D0A
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDFT(i);
      if (f.getDFT() != i)
      {
        failed("Default value not set as expected, Integer");
        return;
      }
      f.setDFT(s);
      if (f.getDFT() != s)
      {
        failed("Default value not set as expected, Short");
        return;
      }
      f.setDFT(l);
      if (f.getDFT() != l)
      {
        failed("Default value not set as expected, Long");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of BinaryFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null for setDFT(Integer)
   *<li>Specify null for setDFT(Short)
   *<li>Specify null for setDFT(Long)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var027()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDFT((Integer)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception, Integer");
        return;
      }
    }
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDFT((Short)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception, Short");
        return;
      }
    }
    //@D0A
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setDFT((Long)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception, Long");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of BinaryFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var028()
  {
    boolean failed = false;
    String msg = "";
    try
    {
      Integer i = new Integer(5555);
      Short s = new Short((short)1);
      Long l = new Long(123456789L); //@D0A
      BinaryFieldDescription f = new BinaryFieldDescription();

      // set to Integer first
      f.setDFT(i);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "Integer: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "Integer: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "Integer: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "Integer: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

      // set to Short first
      f.setDFT(s);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "Short: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "Short: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "Short: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "Short: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

      //@D0A
      // set to Long first
      f.setDFT(l);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "Long: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "Long: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "Long: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "Long: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    if (failed)
    {
      failed(msg);
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify valid usage of (BinaryFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var029()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (BinaryFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var030()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (BinaryFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var031()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of BinaryFieldDescription.setLength().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getLength() will return the default value set.
   *</ul>
  **/
  public void Var032()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setLength(9);
      if (f.getLength() != 9)
      {
        failed("Length not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of BinaryFieldDescription.setLength().
   *<ul compact>
   *<li>Specify 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "length" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var033()
  {
    try
    {
      BinaryFieldDescription f = new BinaryFieldDescription();
      f.setLength(0);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of CharacterFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var034()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of CharacterFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var035()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of CharacterFieldDescription.setCCSID().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getCCSID() will return the ccsid set.
   *</ul>
  **/
  public void Var036()
  {
    try
    {
      String ccsid = "65535";
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setCCSID(ccsid);
      if (!f.getCCSID().equals(ccsid))
      {
        failed("CCSID not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of CharacterFieldDescription.setCCSID().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "ccsid"
   *</ul>
  **/
  public void Var037()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setCCSID(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ccsid"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of CharacterFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var038()
  {
    try
    {
     String dft = "default";
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of CharacterFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var039()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of CharacterFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var040()
  {
    String msg = "";
    boolean failed = false;
    try
    {
      String dft = "default";
      CharacterFieldDescription f = new CharacterFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    if (failed)
    {
      failed(msg);
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify valid usage of (CharacterFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var041()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (CharacterFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var042()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (CharacterFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var043()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of CharacterFieldDescription.setVARLEN().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getVARLEN() will return the value set.
   *</ul>
  **/
  public void Var044()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setVARLEN(10);
      if (f.getVARLEN() != 10)
      {
        failed("VARLEN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of CharacterFieldDescription.setVARLEN().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "varLen" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var045()
  {
    try
    {
      CharacterFieldDescription f = new CharacterFieldDescription();
      f.setVARLEN(-1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "varLen", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSEitherFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var046()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSEitherFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var047()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSEitherFieldDescription.setCCSID().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getCCSID() will return the ccsid set.
   *</ul>
  **/
  public void Var048()
  {
    try
    {
      String ccsid = "65535";
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setCCSID(ccsid);
      if (!f.getCCSID().equals(ccsid))
      {
        failed("CCSID not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSEitherFieldDescription.setCCSID().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "ccsid"
   *</ul>
  **/
  public void Var049()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setCCSID(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ccsid"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSEitherFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var050()
  {
    try
    {
     String dft = "default";
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSEitherFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var051()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSEitherFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var052()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (DBCSEitherFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var053()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSEitherFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var054()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSEitherFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var055()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSEitherFieldDescription.setVARLEN().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getVARLEN() will return the value set.
   *</ul>
  **/
  public void Var056()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setVARLEN(10);
      if (f.getVARLEN() != 10)
      {
        failed("VARLEN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSEitherFieldDescription.setVARLEN().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "varLen" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var057()
  {
    try
    {
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();
      f.setVARLEN(-1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "varLen", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSGraphicFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var058()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSGraphicFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var059()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSGraphicFieldDescription.setCCSID().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getCCSID() will return the ccsid set.
   *</ul>
  **/
  public void Var060()
  {
    try
    {
      String ccsid = "65535";
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setCCSID(ccsid);
      if (!f.getCCSID().equals(ccsid))
      {
        failed("CCSID not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSGraphicFieldDescription.setCCSID().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "ccsid"
   *</ul>
  **/
  public void Var061()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setCCSID(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ccsid"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSGraphicFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var062()
  {
    try
    {
     String dft = "default";
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSGraphicFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var063()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSGraphicFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var064()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (DBCSGraphicFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var065()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSGraphicFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var066()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSGraphicFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var067()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSGraphicFieldDescription.setVARLEN().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getVARLEN() will return the value set.
   *</ul>
  **/
  public void Var068()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setVARLEN(10);
      if (f.getVARLEN() != 10)
      {
        failed("VARLEN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSGraphicFieldDescription.setVARLEN().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "varLen" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var069()
  {
    try
    {
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();
      f.setVARLEN(-1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "varLen", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOnlyFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var070()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOnlyFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var071()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOnlyFieldDescription.setCCSID().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getCCSID() will return the ccsid set.
   *</ul>
  **/
  public void Var072()
  {
    try
    {
      String ccsid = "65535";
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setCCSID(ccsid);
      if (!f.getCCSID().equals(ccsid))
      {
        failed("CCSID not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOnlyFieldDescription.setCCSID().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "ccsid"
   *</ul>
  **/
  public void Var073()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setCCSID(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ccsid"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOnlyFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var074()
  {
    try
    {
     String dft = "default";
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOnlyFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var075()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOnlyFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var076()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (DBCSOnlyFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var077()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSOnlyFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var078()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSOnlyFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var079()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOnlyFieldDescription.setVARLEN().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getVARLEN() will return the value set.
   *</ul>
  **/
  public void Var080()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setVARLEN(10);
      if (f.getVARLEN() != 10)
      {
        failed("VARLEN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOnlyFieldDescription.setVARLEN().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "varLen" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var081()
  {
    try
    {
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();
      f.setVARLEN(-1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "varLen", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOpenFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var082()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOpenFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var083()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOpenFieldDescription.setCCSID().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getCCSID() will return the ccsid set.
   *</ul>
  **/
  public void Var084()
  {
    try
    {
      String ccsid = "65535";
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setCCSID(ccsid);
      if (!f.getCCSID().equals(ccsid))
      {
        failed("CCSID not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOpenFieldDescription.setCCSID().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "ccsid"
   *</ul>
  **/
  public void Var085()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setCCSID(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ccsid"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOpenFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var086()
  {
    try
    {
     String dft = "default";
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOpenFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var087()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOpenFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var088()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (DBCSOpenFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var089()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSOpenFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var090()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DBCSOpenFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var091()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of DBCSOpenFieldDescription.setVARLEN().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getVARLEN() will return the value set.
   *</ul>
  **/
  public void Var092()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setVARLEN(10);
      if (f.getVARLEN() != 10)
      {
        failed("VARLEN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DBCSOpenFieldDescription.setVARLEN().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "varLen" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var093()
  {
    try
    {
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();
      f.setVARLEN(-1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "varLen", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DateFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var094()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      DateFieldDescription f = new DateFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DateFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var095()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      f.setDataType((AS400Text)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DateFieldDescription.setDATFMT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDATFMT() will return the date format set.
   *</ul>
  **/
  public void Var096()
  {
    try
    {
      String datfmt = "*ISO";
      DateFieldDescription f = new DateFieldDescription();
      f.setDATFMT(datfmt);
      if (!f.getDATFMT().equals(datfmt))
      {
        failed("DATFMT not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DateFieldDescription.setDATFMT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dateFormat"
   *</ul>
  **/
  public void Var097()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      f.setDATFMT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dateFormat"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DateFieldDescription.setDATSEP().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDATSEP() will return the date separator set.
   *</ul>
  **/
  public void Var098()
  {
    try
    {
      String datsep = "/";
      DateFieldDescription f = new DateFieldDescription();
      f.setDATSEP(datsep);
      if (!f.getDATSEP().equals(datsep))
      {
        failed("DATSEP not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DateFieldDescription.setDATSEP().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dateSeparator"
   *</ul>
   *
   * Changed 11/2010:  Null means no separator can can be set. 
  **/
  public void Var099()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      f.setDATSEP(null);
      assertCondition(true); 
    }
    catch(Exception e)
    {
        failed(e, "Incorrect exception");
    }
  }

  /**
   *Verify valid usage of DateFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var100()
  {
    try
    {
     String dft = "default";
      DateFieldDescription f = new DateFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of DateFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var101()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of DateFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var102()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      DateFieldDescription f = new DateFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of DateFieldDescription.setDFTCurrent().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return false.
   *<li>isDFTCurrent() will return true.
   *<li>getDFTCurrentValue() will return the correct value.
   *</ul>
  **/
  public void Var103()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      DateFieldDescription f = new DateFieldDescription();
      String currentValue = null;

      // set to String first
      f.setDFT(dft);
      currentValue = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
      f.setDFTCurrent();
      if (f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (!f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (!f.getDFTCurrentValue().equals(currentValue))
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

      // set to NULL first
      f.setDFTNull();
      currentValue = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
      f.setDFTCurrent();
      if (f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (!f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (!f.getDFTCurrentValue().equals(currentValue))
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (DateFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var104()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DateFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var105()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (DateFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var106()
  {
    try
    {
      DateFieldDescription f = new DateFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of FloatFieldDescription.setDataType().
   *<ul compact>
   *<li>setDataType(AS400Float8)
   *<li>setDataType(AS400Float4)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var107()
  {
    try
    {
      AS400Float4 dt4 = new AS400Float4();
      AS400Float8 dt8 = new AS400Float8();
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDataType(dt4);
      if (f.getDataType() != dt4)
      {
        failed("Data type not set as expected, float4");
        return;
      }
      f.setDataType(dt8);
      if (f.getDataType() != dt8)
      {
        failed("Data type not set as expected, float4");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of FloatFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null for setDataType(AS400Float4)
   *<li>Specify null for setDataType(AS400Float8)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var108()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDataType((AS400Float4)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, float4");
        return;
      }
    }
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDataType((AS400Float8)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception, float8");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of FloatFieldDescription.setDFT().
   *<ul compact>
   *<li>setDFT(Float)
   *<li>setDFT(Double)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var109()
  {
    try
    {
      Float fv = new Float("2.1");
      Double d = new Double("555.55");
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDFT(fv);
      if (f.getDFT() != fv)
      {
        failed("Default value not set as expected, Float");
        return;
      }
      f.setDFT(d);
      if (f.getDFT() != d)
      {
        failed("Default value not set as expected, Double");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of FloatFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null for setDFT(Integer)
   *<li>Specify null for setDFT(Short)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var110()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDFT((Float)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception, Float");
        return;
      }
    }
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDFT((Double)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception, Double");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of FloatFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var111()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      Float fv = new Float("2.1");
      Double d = new Double("555.55");

      // set to Float first
      f.setDFT(fv);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "Float: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "Float: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "Float: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "Float: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

      // set to Double first
      f.setDFT(d);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "Double: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "Double: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "Double: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "Double: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (FloatFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var112()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (FloatFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var113()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (FloatFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var114()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of FloatFieldDescription.setLength().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getLength() will return the default value set.
   *</ul>
  **/
  public void Var115()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setLength(9);
      if (f.getLength() != 9)
      {
        failed("Length not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of FloatFieldDescription.setLength().
   *<ul compact>
   *<li>Specify 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "length" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var116()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setLength(0);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "length", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of FloatFieldDescription.setDecimalPositions().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDecimalPositions() will return the value set.
   *</ul>
  **/
  public void Var117()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDecimalPositions(2);
      if (f.getDecimalPositions() != 2)
      {
        failed("Decimal positions not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of FloatFieldDescription.setDecimalPositions().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "decimalPositions" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var118()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setDecimalPositions(-1);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "decimalPositions", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of FloatFieldDescription.setFLTPCN().
   *<ul compact>
   *<LI>Specify *SINGLE
   *<li>Specify *DOUBLE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFLTPCN() will return the value set.
   *</ul>
  **/
  public void Var119()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setFLTPCN("*SINGLE");
      if (!f.getFLTPCN().equals("*SINGLE"))
      {
        failed("FLTPCN not set as expected");
        return;
      }
      f.setFLTPCN("*DOUBLE");
      if (!f.getFLTPCN().equals("*DOUBLE"))
      {
        failed("FLTPCN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of FloatFieldDescription.setFLTPCN().
   *<ul compact>
   *<li>Specify something other than *SINGLE or *DOUBLE
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "floatPrecision" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var120()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setFLTPCN("*BLAH");
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "floatPrecision", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of FloatFieldDescription.setFLTPCN().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerlArgumentException indicating "floatPrecision"
   *</ul>
  **/
  public void Var121()
  {
    try
    {
      FloatFieldDescription f = new FloatFieldDescription();
      f.setFLTPCN(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "floatPrecision"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of HexFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var122()
  {
    try
    {
      AS400ByteArray dt = new AS400ByteArray(10);
      HexFieldDescription f = new HexFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of HexFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var123()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of HexFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var124()
  {
    try
    {
      byte[] dft = new byte[10];
      HexFieldDescription f = new HexFieldDescription();
      f.setDFT(dft);
      if (f.getDFT() != dft)
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of HexFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var125()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of HexFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify array of length 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "defaultValue" and
   *LENGTH_NOT_VALID
   *</ul>
  **/
  public void Var126()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      f.setDFT(new byte[0]);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "defaultValue", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of HexFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var127()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      byte[] dft = new byte[10];

      // set to byte[] first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "byte: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "byte: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "byte: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "byte: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (HexFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var128()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (HexFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var129()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (HexFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var130()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of HexFieldDescription.setVARLEN().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getVARLEN() will return the value set.
   *</ul>
  **/
  public void Var131()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      f.setVARLEN(10);
      if (f.getVARLEN() != 10)
      {
        failed("VARLEN not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of HexFieldDescription.setVARLEN().
   *<ul compact>
   *<li>Specify -1
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "varLen" and
   *PARAMETER_VALUE_NOT_VALID
   *</ul>
  **/
  public void Var132()
  {
    try
    {
      HexFieldDescription f = new HexFieldDescription();
      f.setVARLEN(-1);
      failed("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "varLen", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of PackedDecimalFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var133()
  {
    try
    {
      AS400PackedDecimal dt = new AS400PackedDecimal(10,5);
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of PackedDecimalFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var134()
  {
    try
    {
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of PackedDecimalFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var135()
  {
    try
    {
      BigDecimal dft = new BigDecimal("1234567.123");
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      f.setDFT(dft);
      if (f.getDFT() != dft)
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of PackedDecimalFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var136()
  {
    try
    {
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of PackedDecimalFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var137()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      BigDecimal dft = new BigDecimal("1234567.123");

      // set to BigDecimal first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "BigDecimal: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "BigDecimal: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "BigDecimal: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "BigDecimal: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (PackedDecimalFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var138()
  {
    try
    {
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (PackedDecimalFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var139()
  {
    try
    {
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (PackedDecimalFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var140()
  {
    try
    {
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimeFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var141()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      TimeFieldDescription f = new TimeFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of TimeFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var142()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      f.setDataType((AS400Text)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimeFieldDescription.setTIMFMT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getTIMFMT() will return the Time format set.
   *</ul>
  **/
  public void Var143()
  {
    try
    {
      String timfmt = "*ISO";
      TimeFieldDescription f = new TimeFieldDescription();
      f.setTIMFMT(timfmt);
      if (!f.getTIMFMT().equals(timfmt))
      {
        failed("TIMFMT not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of TimeFieldDescription.setTIMFMT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "timeFormat"
   *</ul>
  **/
  public void Var144()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      f.setTIMFMT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "timeFormat"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimeFieldDescription.setTIMSEP().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getTIMSEP() will return the Time separator set.
   *</ul>
  **/
  public void Var145()
  {
    try
    {
      String timsep = "/";
      TimeFieldDescription f = new TimeFieldDescription();
      f.setTIMSEP(timsep);
      if (!f.getTIMSEP().equals(timsep))
      {
        failed("TIMSEP not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of TimeFieldDescription.setTIMSEP().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "timeSeparator"
   *</ul>
  **/
  public void Var146()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      f.setTIMSEP(null);
      assertCondition(true);
    }
    catch(Exception e)
    {
        failed(e, "Incorrect exception");
    }
  }

  /**
   *Verify valid usage of TimeFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var147()
  {
    try
    {
     String dft = "default";
      TimeFieldDescription f = new TimeFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of TimeFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var148()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimeFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var149()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      TimeFieldDescription f = new TimeFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of TimeFieldDescription.setDFTCurrent().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return false.
   *<li>isDFTCurrent() will return true.
   *<li>getDFTCurrentValue() will return the correct value.
   *</ul>
  **/
  public void Var150()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      TimeFieldDescription f = new TimeFieldDescription();
      String currentValue = null;

      // set to String first
      f.setDFT(dft);
      currentValue = (new SimpleDateFormat("HH.mm.ss")).format(new Date());
      f.setDFTCurrent();
      if (f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (!f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      // The 2 times should be within a second
      // (depending on the speed of the JVM...)
      if (!f.getDFTCurrentValue().equals(currentValue))
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

      // set to NULL first
      f.setDFTNull();
      currentValue = (new SimpleDateFormat("HH.mm.ss")).format(new Date());
      f.setDFTCurrent();
      if (f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (!f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      // The 2 times should be within a second
      // (depending on the speed of the JVM...)
      if (!f.getDFTCurrentValue().equals(currentValue))
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (TimeFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var151()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (TimeFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var152()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (TimeFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var153()
  {
    try
    {
      TimeFieldDescription f = new TimeFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimestampFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var154()
  {
    try
    {
      AS400Text dt = new AS400Text(10, systemObject_.getCcsid(), systemObject_);
      TimestampFieldDescription f = new TimestampFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of TimestampFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var155()
  {
    try
    {
      TimestampFieldDescription f = new TimestampFieldDescription();
      f.setDataType((AS400Text)null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimestampFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var156()
  {
    try
    {
     String dft = "default";
      TimestampFieldDescription f = new TimestampFieldDescription();
      f.setDFT(dft);
      if (!f.getDFT().equals(dft))
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of TimestampFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var157()
  {
    try
    {
      TimestampFieldDescription f = new TimestampFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of TimestampFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var158()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      String dft = "default";
      TimestampFieldDescription f = new TimestampFieldDescription();

      // set to String first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of TimestampFieldDescription.setDFTCurrent().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return false.
   *<li>isDFTCurrent() will return true.
   *<li>getDFTCurrentValue() will return the correct value.
   *</ul>
  **/
  public void Var159()
  {
    String msg = "";
    boolean failed = false;
    try
    {
      String dft = "default";
      TimestampFieldDescription f = new TimestampFieldDescription();
      String currentValue = null;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");

      // set to String first
      f.setDFT(dft);
      currentValue = sdf.format(new Date()); // This will work no matter the locale
      f.setDFTCurrent();
      if (f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (!f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      // These should be close... the system date will be a second or so
      // after the current one. (depending on the speed of the JVM...)
      // Compare the dates to make sure.
      
      // Use the US locale to do the conversion because...
      sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS", new Locale("en", "US")); // so we use the US locale to convert the string
      // ...the following 2 lines won't work if the current locale doesn't know how to
      // parse out "yyyy-MM-dd-HH.mm.ss.SSS"  --CRS
      Date sysDate = sdf.parse(f.getDFTCurrentValue());
      Date curDate = sdf.parse(currentValue);
      
      Calendar sysCal = Calendar.getInstance();
      Calendar curCal = Calendar.getInstance();
      sysCal.setTime(sysDate);
      curCal.setTime(curDate);
      curCal.add(Calendar.SECOND, 1); // Within 1 second
      if (curCal.before(sysCal)) // Difference is more than 1 second
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: \n"+f.getDFTCurrentValue()+" !=\n"+currentValue+"\n";
        failed = true;
      }

      // set to NULL first
      f.setDFTNull();
      currentValue = sdf.format(new Date());
      f.setDFTCurrent();
      if (f.isDFTNull())
      {
        msg += "String: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "String: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (!f.isDFTCurrent())
      {
        msg += "String: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      // These should be close... the system date will be a second or so
      // after the current one. (depending on the speed of the JVM...)
      // Compare the dates to make sure.
      sysDate = sdf.parse(f.getDFTCurrentValue());
      curDate = sdf.parse(currentValue);
      sysCal.clear();
      curCal.clear();
      sysCal.setTime(sysDate);
      curCal.setTime(curDate);
      curCal.add(Calendar.SECOND, 1); // Within 1 second
      if (curCal.before(sysCal)) // Difference is more than 1 second
      {
        msg += "String: getDFTCurrentValue() returned incorrect value: \n"+f.getDFTCurrentValue()+" !=\n"+currentValue+"\n";
        failed = true;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (TimestampFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var160()
  {
    try
    {
      TimestampFieldDescription f = new TimestampFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (TimestampFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var161()
  {
    try
    {
      TimestampFieldDescription f = new TimestampFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (TimestampFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var162()
  {
    try
    {
      TimestampFieldDescription f = new TimestampFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of ZonedDecimalFieldDescription.setDataType().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDataType() will return the data type set.
   *</ul>
  **/
  public void Var163()
  {
    try
    {
      AS400ZonedDecimal dt = new AS400ZonedDecimal(10,5);
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      f.setDataType(dt);
      if (f.getDataType() != dt)
      {
        failed("Data type not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of ZonedDecimalFieldDescription.setDataType().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "dataType"
   *</ul>
  **/
  public void Var164()
  {
    try
    {
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      f.setDataType(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of ZonedDecimalFieldDescription.setDFT().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return the default value set.
   *</ul>
  **/
  public void Var165()
  {
    try
    {
      BigDecimal dft = new BigDecimal("1234567.123");
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      f.setDFT(dft);
      if (f.getDFT() != dft)
      {
        failed("Default value not set as expected");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of ZonedDecimalFieldDescription.setDFT().
   *<ul compact>
   *<li>Specify null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "defaultValue"
   *</ul>
  **/
  public void Var166()
  {
    try
    {
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      f.setDFT(null);
      failed("No exception.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "defaultValue"))
      {
        failed(e, "Incorrect exception");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of ZonedDecimalFieldDescription.setDFTNull().
   *Expected results:
   *<ul compact>
   *<li>getDFT() will return null.
   *<li>isDFTNull() will return true.
   *<li>isDFTCurrent() will return false.
   *<li>getDFTCurrentValue() will return null.
   *</ul>
  **/
  public void Var167()
  {
      String msg = "";
      boolean failed = false;
    try
    {
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      BigDecimal dft = new BigDecimal("1234567.123");

      // set to BigDecimal first
      f.setDFT(dft);
      f.setDFTNull();
      if (!f.isDFTNull())
      {
        msg += "BigDecimal: isDFTNull() returned incorrect value: "+f.isDFTNull()+"\n";
        failed = true;
      }
      if (f.getDFT() != null)
      {
        msg += "BigDecimal: getDFT() returned incorrect value: "+f.getDFT()+"\n";
        failed = true;
      }
      if (f.isDFTCurrent())
      {
        msg += "BigDecimal: isDFTCurrent() returned incorrect value: "+f.isDFTCurrent()+"\n";
        failed = true;
      }
      if (f.getDFTCurrentValue() != null)
      {
        msg += "BigDecimal: getDFTCurrentValue() returned incorrect value: "+f.getDFTCurrentValue()+"\n";
        failed = true;
      }

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
      if (failed)
      {
        failed(msg);
      }
      else
      {
        succeeded();
      }
  }

  /**
   *Verify valid usage of (ZonedDecimalFieldDescription)FieldDescription.isDFTNull().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTNull() will return the default value of false.
   *</ul>
  **/
  public void Var168()
  {
    try
    {
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      if (f.isDFTNull())
      {
        failed("DFT (NULL) not set as expected: " + f.isDFTNull());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (ZonedDecimalFieldDescription)FieldDescription.isDFTCurrent().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>isDFTCurrent() will return the default value of false.
   *</ul>
  **/
  public void Var169()
  {
    try
    {
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      if (f.isDFTCurrent())
      {
        failed("DFT (CURRENT) not set as expected: " + f.isDFTCurrent());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of (ZonedDecimalFieldDescription)FieldDescription.getDFTCurrentValue().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getDFTCurrentValue() will return the default value of null.
   *</ul>
  **/
  public void Var170()
  {
    try
    {
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();
      if (f.getDFTCurrentValue() != null)
      {
        failed("Current value not set as expected: " + f.getDFTCurrentValue());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }
}



