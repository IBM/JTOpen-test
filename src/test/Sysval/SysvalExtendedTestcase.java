///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalExtendedTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sysval;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueList;

import test.Testcase;

// For V4R2, there are 125 system values and 35 network attributes.
// For V4R3, there are an additional 5 system values.

// For V5R1, there are an additional n system values (see access\SystemValueList.java for the actual number)
// For V5R2, there are an additional 2 system values.

/**
The SysvalExtendedTestcase class provides testcases to test all defined system values.

<p>This tests the SystemValue methods setValue() and getValue().
**/
public class SysvalExtendedTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SysvalExtendedTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SysvalTestDriver.main(newArgs); 
   }
    int vrm;
    static String trash = "TRASHTRASHTRASHTRASHTRASHTRASHTRASHTRASHTRASH";


    /**
    Runs the variations.
    **/
    public void run()
    {
       try
       {
         vrm = pwrSys_.getVRM();
       }
       catch(Exception e)
       {
         output_.println("Unable to setup testcase");
         output_.println(e.getMessage());
         e.printStackTrace();
         return;
       }

       boolean allVariations = (variationsToRun_.size () == 0);

       if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED)
       {
         setVariation(1);
         Var001();
       }
       if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
       {
         setVariation(2);
         Var002();
       }
       if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
       {
         setVariation(3);
         Var003();
       }
       if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
       {
         setVariation(4);
         Var004();
       }
       if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
       {
         setVariation(5);
         Var005();
       }
       if ((allVariations || variationsToRun_.contains("6")) && runMode_ != ATTENDED)
       {
         setVariation(6);
         Var006();
       }
       if ((allVariations || variationsToRun_.contains("7")) && runMode_ != ATTENDED)
       {
         setVariation(7);
         Var007();
       }
       if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED)
       {
         setVariation(8);
         Var008();
       }
       if ((allVariations || variationsToRun_.contains("9")) && runMode_ != ATTENDED)
       {
         setVariation(9);
         Var009();
       }
       if ((allVariations || variationsToRun_.contains("10")) && runMode_ != ATTENDED)
       {
         setVariation(10);
         Var010();
       }
       if ((allVariations || variationsToRun_.contains("11")) && runMode_ != ATTENDED)
       {
         setVariation(11);
         Var011();
       }
       if ((allVariations || variationsToRun_.contains("12")) && runMode_ != ATTENDED)
       {
         setVariation(12);
         Var012();
       }
       if ((allVariations || variationsToRun_.contains("13")) && runMode_ != ATTENDED)
       {
         setVariation(13);
         Var013();
       }
       if ((allVariations || variationsToRun_.contains("14")) && runMode_ != ATTENDED)
       {
         setVariation(14);
         Var014();
       }
       if ((allVariations || variationsToRun_.contains("15")) && runMode_ != ATTENDED)
       {
         setVariation(15);
         Var015();
       }
       if ((allVariations || variationsToRun_.contains("16")) && runMode_ != ATTENDED)
       {
         setVariation(16);
         Var016();
       }
       if ((allVariations || variationsToRun_.contains("17")) && runMode_ != ATTENDED)
       {
         setVariation(17);
         Var017();
       }
       if ((allVariations || variationsToRun_.contains("18")) && runMode_ != ATTENDED)
       {
         setVariation(18);
         Var018();
       }
       if ((allVariations || variationsToRun_.contains("19")) && runMode_ != ATTENDED)
       {
         setVariation(19);
         Var019();
       }
       if ((allVariations || variationsToRun_.contains("20")) && runMode_ != ATTENDED)
       {
         setVariation(20);
         Var020();
       }
       if ((allVariations || variationsToRun_.contains("21")) && runMode_ != ATTENDED)
       {
         setVariation(21);
         Var021();
       }
       if ((allVariations || variationsToRun_.contains("22")) && runMode_ != ATTENDED)
       {
         setVariation(22);
         Var022();
       }
       if ((allVariations || variationsToRun_.contains("23")) && runMode_ != ATTENDED)
       {
         setVariation(23);
         Var023();
       }
       if ((allVariations || variationsToRun_.contains("24")) && runMode_ != ATTENDED)
       {
         setVariation(24);
         Var024();
       }
       if ((allVariations || variationsToRun_.contains("25")) && runMode_ != ATTENDED)
       {
         setVariation(25);
         Var025();
       }
       if ((allVariations || variationsToRun_.contains("26")) && runMode_ != ATTENDED)
       {
         setVariation(26);
         Var026();
       }
       if ((allVariations || variationsToRun_.contains("27")) && runMode_ != ATTENDED)
       {
         setVariation(27);
         Var027();
       }
       if ((allVariations || variationsToRun_.contains("28")) && runMode_ != ATTENDED)
       {
         setVariation(28);
         Var028();
       }
       if ((allVariations || variationsToRun_.contains("29")) && runMode_ != ATTENDED)
       {
         setVariation(29);
         Var029();
       }
       if ((allVariations || variationsToRun_.contains("30")) && runMode_ != ATTENDED)
       {
         setVariation(30);
         Var030();
       }
       if ((allVariations || variationsToRun_.contains("31")) && runMode_ != ATTENDED)
       {
         setVariation(31);
         Var031();
       }
       if ((allVariations || variationsToRun_.contains("32")) && runMode_ != ATTENDED)
       {
         setVariation(32);
         Var032();
       }
       if ((allVariations || variationsToRun_.contains("33")) && runMode_ != ATTENDED)
       {
         setVariation(33);
         Var033();
       }
       if ((allVariations || variationsToRun_.contains("34")) && runMode_ != ATTENDED)
       {
         setVariation(34);
         Var034();
       }
       if ((allVariations || variationsToRun_.contains("35")) && runMode_ != ATTENDED)
       {
         setVariation(35);
         Var035();
       }
       if ((allVariations || variationsToRun_.contains("36")) && runMode_ != ATTENDED)
       {
         setVariation(36);
         Var036();
       }
       if ((allVariations || variationsToRun_.contains("37")) && runMode_ != ATTENDED)
       {
         setVariation(37);
         Var037();
       }
       if ((allVariations || variationsToRun_.contains("38")) && runMode_ != ATTENDED)
       {
         setVariation(38);
         Var038();
       }
       if ((allVariations || variationsToRun_.contains("39")) && runMode_ != ATTENDED)
       {
         setVariation(39);
         Var039();
       }
       if ((allVariations || variationsToRun_.contains("40")) && runMode_ != ATTENDED)
       {
         setVariation(40);
         Var040();
       }
       if ((allVariations || variationsToRun_.contains("41")) && runMode_ != ATTENDED)
       {
         setVariation(41);
         Var041();
       }
       if ((allVariations || variationsToRun_.contains("42")) && runMode_ != ATTENDED)
       {
         setVariation(42);
         Var042();
       }
       if ((allVariations || variationsToRun_.contains("43")) && runMode_ != ATTENDED)
       {
         setVariation(43);
         Var043();
       }
       if ((allVariations || variationsToRun_.contains("44")) && runMode_ != ATTENDED)
       {
         setVariation(44);
         Var044();
       }
       if ((allVariations || variationsToRun_.contains("45")) && runMode_ != ATTENDED)
       {
         setVariation(45);
         Var045();
       }
       if ((allVariations || variationsToRun_.contains("46")) && runMode_ != ATTENDED)
       {
         setVariation(46);
         Var046();
       }
       if ((allVariations || variationsToRun_.contains("47")) && runMode_ != ATTENDED)
       {
         setVariation(47);
         Var047();
       }
       if ((allVariations || variationsToRun_.contains("48")) && runMode_ != ATTENDED)
       {
         setVariation(48);
         Var048();
       }
       if ((allVariations || variationsToRun_.contains("49")) && runMode_ != ATTENDED)
       {
         setVariation(49);
         Var049();
       }
       if ((allVariations || variationsToRun_.contains("50")) && runMode_ != ATTENDED)
       {
         setVariation(50);
         Var050();
       }
       if ((allVariations || variationsToRun_.contains("51")) && runMode_ != ATTENDED)
       {
         setVariation(51);
         Var051();
       }
       if ((allVariations || variationsToRun_.contains("52")) && runMode_ != ATTENDED)
       {
         setVariation(52);
         Var052();
       }
       if ((allVariations || variationsToRun_.contains("53")) && runMode_ != ATTENDED)
       {
         setVariation(53);
         Var053();
       }
       if ((allVariations || variationsToRun_.contains("54")) && runMode_ != ATTENDED)
       {
         setVariation(54);
         Var054();
       }
       if ((allVariations || variationsToRun_.contains("55")) && runMode_ != ATTENDED)
       {
         setVariation(55);
         Var055();
       }
       if ((allVariations || variationsToRun_.contains("56")) && runMode_ != ATTENDED)
       {
         setVariation(56);
         Var056();
       }
       if ((allVariations || variationsToRun_.contains("57")) && runMode_ != ATTENDED)
       {
         setVariation(57);
         Var057();
       }
       if ((allVariations || variationsToRun_.contains("58")) && runMode_ != ATTENDED)
       {
         setVariation(58);
         Var058();
       }
       if ((allVariations || variationsToRun_.contains("59")) && runMode_ != ATTENDED)
       {
         setVariation(59);
         Var059();
       }
       if ((allVariations || variationsToRun_.contains("60")) && runMode_ != ATTENDED)
       {
         setVariation(60);
         Var060();
       }
       if ((allVariations || variationsToRun_.contains("61")) && runMode_ != ATTENDED)
       {
         setVariation(61);
         Var061();
       }
       if ((allVariations || variationsToRun_.contains("62")) && runMode_ != ATTENDED)
       {
         setVariation(62);
         Var062();
       }
       if ((allVariations || variationsToRun_.contains("63")) && runMode_ != ATTENDED)
       {
         setVariation(63);
         Var063();
       }
       if ((allVariations || variationsToRun_.contains("64")) && runMode_ != ATTENDED)
       {
         setVariation(64);
         Var064();
       }
       if ((allVariations || variationsToRun_.contains("65")) && runMode_ != ATTENDED)
       {
         setVariation(65);
         Var065();
       }
       if ((allVariations || variationsToRun_.contains("66")) && runMode_ != ATTENDED)
       {
         setVariation(66);
         Var066();
       }
       if ((allVariations || variationsToRun_.contains("67")) && runMode_ != ATTENDED)
       {
         setVariation(67);
         Var067();
       }
       if ((allVariations || variationsToRun_.contains("68")) && runMode_ != ATTENDED)
       {
         setVariation(68);
         Var068();
       }
       if ((allVariations || variationsToRun_.contains("69")) && runMode_ != ATTENDED)
       {
         setVariation(69);
         Var069();
       }
       if ((allVariations || variationsToRun_.contains("70")) && runMode_ != ATTENDED)
       {
         setVariation(70);
         Var070();
       }
       if ((allVariations || variationsToRun_.contains("71")) && runMode_ != ATTENDED)
       {
         setVariation(71);
         Var071();
       }
       if ((allVariations || variationsToRun_.contains("72")) && runMode_ != ATTENDED)
       {
         setVariation(72);
         Var072();
       }
       if ((allVariations || variationsToRun_.contains("73")) && runMode_ != ATTENDED)
       {
         setVariation(73);
         Var073();
       }
       if ((allVariations || variationsToRun_.contains("74")) && runMode_ != ATTENDED)
       {
         setVariation(74);
         Var074();
       }
       if ((allVariations || variationsToRun_.contains("75")) && runMode_ != ATTENDED)
       {
         setVariation(75);
         Var075();
       }
       if ((allVariations || variationsToRun_.contains("76")) && runMode_ != ATTENDED)
       {
         setVariation(76);
         Var076();
       }
       if ((allVariations || variationsToRun_.contains("77")) && runMode_ != ATTENDED)
       {
         setVariation(77);
         Var077();
       }
       if ((allVariations || variationsToRun_.contains("78")) && runMode_ != ATTENDED)
       {
         setVariation(78);
         Var078();
       }
       if ((allVariations || variationsToRun_.contains("79")) && runMode_ != ATTENDED)
       {
         setVariation(79);
         Var079();
       }
       if ((allVariations || variationsToRun_.contains("80")) && runMode_ != ATTENDED)
       {
         setVariation(80);
         Var080();
       }
       if ((allVariations || variationsToRun_.contains("81")) && runMode_ != ATTENDED)
       {
         setVariation(81);
         Var081();
       }
       if ((allVariations || variationsToRun_.contains("82")) && runMode_ != ATTENDED)
       {
         setVariation(82);
         Var082();
       }
       if ((allVariations || variationsToRun_.contains("83")) && runMode_ != ATTENDED)
       {
         setVariation(83);
         Var083();
       }
       if ((allVariations || variationsToRun_.contains("84")) && runMode_ != ATTENDED)
       {
         setVariation(84);
         Var084();
       }
       if ((allVariations || variationsToRun_.contains("85")) && runMode_ != ATTENDED)
       {
         setVariation(85);
         Var085();
       }
       if ((allVariations || variationsToRun_.contains("86")) && runMode_ != ATTENDED)
       {
         setVariation(86);
         Var086();
       }
       if ((allVariations || variationsToRun_.contains("87")) && runMode_ != ATTENDED)
       {
         setVariation(87);
         Var087();
       }
       if ((allVariations || variationsToRun_.contains("88")) && runMode_ != ATTENDED)
       {
         setVariation(88);
         Var088();
       }
       if ((allVariations || variationsToRun_.contains("89")) && runMode_ != ATTENDED)
       {
         setVariation(89);
         Var089();
       }
       if ((allVariations || variationsToRun_.contains("90")) && runMode_ != ATTENDED)
       {
         setVariation(90);
         Var090();
       }
       if ((allVariations || variationsToRun_.contains("91")) && runMode_ != ATTENDED)
       {
         setVariation(91);
         Var091();
       }
       if ((allVariations || variationsToRun_.contains("92")) && runMode_ != ATTENDED)
       {
         setVariation(92);
         Var092();
       }
       if ((allVariations || variationsToRun_.contains("93")) && runMode_ != ATTENDED)
       {
         setVariation(93);
         Var093();
       }
       if ((allVariations || variationsToRun_.contains("94")) && runMode_ != ATTENDED)
       {
         setVariation(94);
         Var094();
       }
       if ((allVariations || variationsToRun_.contains("95")) && runMode_ != ATTENDED)
       {
         setVariation(95);
         Var095();
       }
       if ((allVariations || variationsToRun_.contains("96")) && runMode_ != ATTENDED)
       {
         setVariation(96);
         Var096();
       }
       if ((allVariations || variationsToRun_.contains("97")) && runMode_ != ATTENDED)
       {
         setVariation(97);
         Var097();
       }
       if ((allVariations || variationsToRun_.contains("98")) && runMode_ != ATTENDED)
       {
         setVariation(98);
         Var098();
       }
       if ((allVariations || variationsToRun_.contains("99")) && runMode_ != ATTENDED)
       {
         setVariation(99);
         Var099();
       }
       if ((allVariations || variationsToRun_.contains("100")) && runMode_ != ATTENDED)
       {
         setVariation(100);
         Var100();
       }
       if ((allVariations || variationsToRun_.contains("101")) && runMode_ != ATTENDED)
       {
         setVariation(101);
         Var101();
       }
       if ((allVariations || variationsToRun_.contains("102")) && runMode_ != ATTENDED)
       {
         setVariation(102);
         Var102();
       }
       if ((allVariations || variationsToRun_.contains("103")) && runMode_ != ATTENDED)
       {
         setVariation(103);
         Var103();
       }
       if ((allVariations || variationsToRun_.contains("104")) && runMode_ != ATTENDED)
       {
         setVariation(104);
         Var104();
       }
       if ((allVariations || variationsToRun_.contains("105")) && runMode_ != ATTENDED)
       {
         setVariation(105);
         Var105();
       }
       if ((allVariations || variationsToRun_.contains("106")) && runMode_ != ATTENDED)
       {
         setVariation(106);
         Var106();
       }
       if ((allVariations || variationsToRun_.contains("107")) && runMode_ != ATTENDED)
       {
         setVariation(107);
         Var107();
       }
       if ((allVariations || variationsToRun_.contains("108")) && runMode_ != ATTENDED)
       {
         setVariation(108);
         Var108();
       }
       if ((allVariations || variationsToRun_.contains("109")) && runMode_ != ATTENDED)
       {
         setVariation(109);
         Var109();
       }
       if ((allVariations || variationsToRun_.contains("110")) && runMode_ != ATTENDED)
       {
         setVariation(110);
         Var110();
       }
       if ((allVariations || variationsToRun_.contains("111")) && runMode_ != ATTENDED)
       {
         setVariation(111);
         Var111();
       }
       if ((allVariations || variationsToRun_.contains("112")) && runMode_ != ATTENDED)
       {
         setVariation(112);
         Var112();
       }
       if ((allVariations || variationsToRun_.contains("113")) && runMode_ != ATTENDED)
       {
         setVariation(113);
         Var113();
       }
       if ((allVariations || variationsToRun_.contains("114")) && runMode_ != ATTENDED)
       {
         setVariation(114);
         Var114();
       }
       if ((allVariations || variationsToRun_.contains("115")) && runMode_ != ATTENDED)
       {
         setVariation(115);
         Var115();
       }
       if ((allVariations || variationsToRun_.contains("116")) && runMode_ != ATTENDED)
       {
         setVariation(116);
         Var116();
       }
       if ((allVariations || variationsToRun_.contains("117")) && runMode_ != ATTENDED)
       {
         setVariation(117);
         Var117();
       }
       if ((allVariations || variationsToRun_.contains("118")) && runMode_ != ATTENDED)
       {
         setVariation(118);
         Var118();
       }
       if ((allVariations || variationsToRun_.contains("119")) && runMode_ != ATTENDED)
       {
         setVariation(119);
         Var119();
       }
       if ((allVariations || variationsToRun_.contains("120")) && runMode_ != ATTENDED)
       {
         setVariation(120);
         Var120();
       }
       if ((allVariations || variationsToRun_.contains("121")) && runMode_ != ATTENDED)
       {
         setVariation(121);
         Var121();
       }
       if ((allVariations || variationsToRun_.contains("122")) && runMode_ != ATTENDED)
       {
         setVariation(122);
         Var122();
       }
       if ((allVariations || variationsToRun_.contains("123")) && runMode_ != ATTENDED)
       {
         setVariation(123);
         Var123();
       }
       if ((allVariations || variationsToRun_.contains("124")) && runMode_ != ATTENDED)
       {
         setVariation(124);
         Var124();
       }
       if ((allVariations || variationsToRun_.contains("125")) && runMode_ != ATTENDED)
       {
         setVariation(125);
         Var125();
       }
       if ((allVariations || variationsToRun_.contains("126")) && runMode_ != ATTENDED)
       {
         setVariation(126);
         Var126();
       }
       if ((allVariations || variationsToRun_.contains("127")) && runMode_ != ATTENDED)
       {
         setVariation(127);
         Var127();
       }
       if ((allVariations || variationsToRun_.contains("128")) && runMode_ != ATTENDED)
       {
         setVariation(128);
         Var128();
       }
       if ((allVariations || variationsToRun_.contains("129")) && runMode_ != ATTENDED)
       {
         setVariation(129);
         Var129();
       }
       if ((allVariations || variationsToRun_.contains("130")) && runMode_ != ATTENDED)
       {
         setVariation(130);
         Var130();
       }
       if ((allVariations || variationsToRun_.contains("131")) && runMode_ != ATTENDED)
       {
         setVariation(131);
         Var131();
       }
       if ((allVariations || variationsToRun_.contains("132")) && runMode_ != ATTENDED)
       {
         setVariation(132);
         Var132();
       }
       if ((allVariations || variationsToRun_.contains("133")) && runMode_ != ATTENDED)
       {
         setVariation(133);
         Var133();
       }
       if ((allVariations || variationsToRun_.contains("134")) && runMode_ != ATTENDED)
       {
         setVariation(134);
         Var134();
       }
       if ((allVariations || variationsToRun_.contains("135")) && runMode_ != ATTENDED)
       {
         setVariation(135);
         Var135();
       }
       if ((allVariations || variationsToRun_.contains("136")) && runMode_ != ATTENDED)
       {
         setVariation(136);
         Var136();
       }
       if ((allVariations || variationsToRun_.contains("137")) && runMode_ != ATTENDED)
       {
         setVariation(137);
         Var137();
       }
       if ((allVariations || variationsToRun_.contains("138")) && runMode_ != ATTENDED)
       {
         setVariation(138);
         Var138();
       }
       if ((allVariations || variationsToRun_.contains("139")) && runMode_ != ATTENDED)
       {
         setVariation(139);
         Var139();
       }
       if ((allVariations || variationsToRun_.contains("140")) && runMode_ != ATTENDED)
       {
         setVariation(140);
         Var140();
       }
       if ((allVariations || variationsToRun_.contains("141")) && runMode_ != ATTENDED)
       {
         setVariation(141);
         Var141();
       }
       if ((allVariations || variationsToRun_.contains("142")) && runMode_ != ATTENDED)
       {
         setVariation(142);
         Var142();
       }
       if ((allVariations || variationsToRun_.contains("143")) && runMode_ != ATTENDED)
       {
         setVariation(143);
         Var143();
       }
       if ((allVariations || variationsToRun_.contains("144")) && runMode_ != ATTENDED)
       {
         setVariation(144);
         Var144();
       }
       if ((allVariations || variationsToRun_.contains("145")) && runMode_ != ATTENDED)
       {
         setVariation(145);
         Var145();
       }
       if ((allVariations || variationsToRun_.contains("146")) && runMode_ != ATTENDED)
       {
         setVariation(146);
         Var146();
       }
       if ((allVariations || variationsToRun_.contains("147")) && runMode_ != ATTENDED)
       {
         setVariation(147);
         Var147();
       }
       if ((allVariations || variationsToRun_.contains("148")) && runMode_ != ATTENDED)
       {
         setVariation(148);
         Var148();
       }
       if ((allVariations || variationsToRun_.contains("149")) && runMode_ != ATTENDED)
       {
         setVariation(149);
         Var149();
       }
       if ((allVariations || variationsToRun_.contains("150")) && runMode_ != ATTENDED)
       {
         setVariation(150);
         Var150();
       }
       if ((allVariations || variationsToRun_.contains("151")) && runMode_ != ATTENDED)
       {
         setVariation(151);
         Var151();
       }
       if ((allVariations || variationsToRun_.contains("152")) && runMode_ != ATTENDED)
       {
         setVariation(152);
         Var152();
       }
       if ((allVariations || variationsToRun_.contains("153")) && runMode_ != ATTENDED)
       {
         setVariation(153);
         Var153();
       }
       if ((allVariations || variationsToRun_.contains("154")) && runMode_ != ATTENDED)
       {
         setVariation(154);
         Var154();
       }
       if ((allVariations || variationsToRun_.contains("155")) && runMode_ != ATTENDED)
       {
         setVariation(155);
         Var155();
       }
       if ((allVariations || variationsToRun_.contains("156")) && runMode_ != ATTENDED)
       {
         setVariation(156);
         Var156();
       }
       if ((allVariations || variationsToRun_.contains("157")) && runMode_ != ATTENDED)
       {
         setVariation(157);
         Var157();
       }
       if ((allVariations || variationsToRun_.contains("158")) && runMode_ != ATTENDED)
       {
         setVariation(158);
         Var158();
       }
       if ((allVariations || variationsToRun_.contains("159")) && runMode_ != ATTENDED)
       {
         setVariation(159);
         Var159();
       }
       if ((allVariations || variationsToRun_.contains("160")) && runMode_ != ATTENDED)
       {
         setVariation(160);
         Var160();
       }
       if ((allVariations || variationsToRun_.contains("161")) && runMode_ != ATTENDED)
       {
         setVariation(161);
         Var161();
       }
       if ((allVariations || variationsToRun_.contains("162")) && runMode_ != ATTENDED)
       {
         setVariation(162);
         Var162();
       }
       if ((allVariations || variationsToRun_.contains("163")) && runMode_ != ATTENDED)
       {
         setVariation(163);
         Var163();
       }
       if ((allVariations || variationsToRun_.contains("164")) && runMode_ != ATTENDED)
       {
         setVariation(164);
         Var164();
       }
       if ((allVariations || variationsToRun_.contains("165")) && runMode_ != ATTENDED)
       {
         setVariation(165);
         Var165();
       }
       if ((allVariations || variationsToRun_.contains("166")) && runMode_ != ATTENDED)
       {
         setVariation(166);
         Var166();
       }
       if ((allVariations || variationsToRun_.contains("167")) && runMode_ != ATTENDED)
       {
         setVariation(167);
         Var167();
       }
       if ((allVariations || variationsToRun_.contains("168")) && runMode_ != ATTENDED)
       {
         setVariation(168);
         Var168();
       }
       if ((allVariations || variationsToRun_.contains("169")) && runMode_ != ATTENDED)
       {
         setVariation(169);
         Var169();
       }
       if ((allVariations || variationsToRun_.contains("170")) && runMode_ != ATTENDED)
       {
         setVariation(170);
         Var170();
       }
       if ((allVariations || variationsToRun_.contains("171")) && runMode_ != ATTENDED)
       {
         setVariation(171);
         Var171();
       }
       if ((allVariations || variationsToRun_.contains("172")) && runMode_ != ATTENDED)
       {
         setVariation(172);
         Var172();
       }
       if ((allVariations || variationsToRun_.contains("173")) && runMode_ != ATTENDED)
       {
         setVariation(173);
         Var173();
       }
       if ((allVariations || variationsToRun_.contains("174")) && runMode_ != ATTENDED)
       {
         setVariation(174);
         Var174();
       }
       if ((allVariations || variationsToRun_.contains("175")) && runMode_ != ATTENDED)
       {
         setVariation(175);
         Var175();
       }
       if ((allVariations || variationsToRun_.contains("176")) && runMode_ != ATTENDED)
       {
         setVariation(176);
         Var176();
       }
       if ((allVariations || variationsToRun_.contains("177")) && runMode_ != ATTENDED)
       {
         setVariation(177);
         Var177();
       }
       if ((allVariations || variationsToRun_.contains("178")) && runMode_ != ATTENDED)
       {
         setVariation(178);
         Var178();
       }
       if ((allVariations || variationsToRun_.contains("179")) && runMode_ != ATTENDED)
       {
         setVariation(179);
         Var179();
       }
       if ((allVariations || variationsToRun_.contains("180")) && runMode_ != ATTENDED)
       {
         setVariation(180);
         Var180();
       }
       if ((allVariations || variationsToRun_.contains("181")) && runMode_ != ATTENDED)
       {
         setVariation(181);
         Var181();
       }
       if ((allVariations || variationsToRun_.contains("182")) && runMode_ != ATTENDED)
       {
         setVariation(182);
         Var182();
       }
       if ((allVariations || variationsToRun_.contains("183")) && runMode_ != ATTENDED)
       {
         setVariation(183);
         Var183();
       }
       if ((allVariations || variationsToRun_.contains("184")) && runMode_ != ATTENDED)
       {
         setVariation(184);
         Var184();
       }
       if ((allVariations || variationsToRun_.contains("185")) && runMode_ != ATTENDED)
       {
         setVariation(185);
         Var185();
       }
       if ((allVariations || variationsToRun_.contains("186")) && runMode_ != ATTENDED)
       {
         setVariation(186);
         Var186();
       }
       if ((allVariations || variationsToRun_.contains("187")) && runMode_ != ATTENDED)
       {
         setVariation(187);
         Var187();
       }
       if ((allVariations || variationsToRun_.contains("188")) && runMode_ != ATTENDED)
       {
         setVariation(188);
         Var188();
       }
       if ((allVariations || variationsToRun_.contains("189")) && runMode_ != ATTENDED)
       {
         setVariation(189);
         Var189();
       }
       if ((allVariations || variationsToRun_.contains("190")) && runMode_ != ATTENDED)
       {
         setVariation(190);
         Var190();
       }
       if ((allVariations || variationsToRun_.contains("191")) && runMode_ != ATTENDED)
       {
         setVariation(191);
         Var191();
       }
       if ((allVariations || variationsToRun_.contains("192")) && runMode_ != ATTENDED)
       {
         setVariation(192);
         Var192();
       }
       if ((allVariations || variationsToRun_.contains("193")) && runMode_ != ATTENDED)
       {
         setVariation(193);
         Var193();
       }
       if ((allVariations || variationsToRun_.contains("194")) && runMode_ != ATTENDED)
       {
         setVariation(194);
         Var194();
       }
       if ((allVariations || variationsToRun_.contains("195")) && runMode_ != ATTENDED)
       {
         setVariation(195);
         Var195();
       }
       if ((allVariations || variationsToRun_.contains("196")) && runMode_ != ATTENDED)
       {
         setVariation(196);
         Var196();
       }
       if ((allVariations || variationsToRun_.contains("197")) && runMode_ != ATTENDED)
       {
         setVariation(197);
         Var197();
       }
       if ((allVariations || variationsToRun_.contains("198")) && runMode_ != ATTENDED)
       {
         setVariation(198);
         Var198();
       }
       if ((allVariations || variationsToRun_.contains("199")) && runMode_ != ATTENDED)
       {
         setVariation(199);
         Var199();
       }
       if ((allVariations || variationsToRun_.contains("200")) && runMode_ != ATTENDED)
       {
         setVariation(200);
         Var200();
       }
       if ((allVariations || variationsToRun_.contains("201")) && runMode_ != ATTENDED)
       {
         setVariation(201);
         Var201();
       }
       if ((allVariations || variationsToRun_.contains("202")) && runMode_ != ATTENDED)
       {
         setVariation(202);
         Var202();
       }
       if ((allVariations || variationsToRun_.contains("203")) && runMode_ != ATTENDED)
       {
         setVariation(203);
         Var203();
       }
       if ((allVariations || variationsToRun_.contains("204")) && runMode_ != ATTENDED)
       {
         setVariation(204);
         Var204();
       }
       if ((allVariations || variationsToRun_.contains("205")) && runMode_ != ATTENDED)
       {
         setVariation(205);
         Var205();
       }
       if ((allVariations || variationsToRun_.contains("206")) && runMode_ != ATTENDED)
       {
         setVariation(206);
         Var206();
       }
       if ((allVariations || variationsToRun_.contains("207")) && runMode_ != ATTENDED)
       {
         setVariation(207);
         Var207();
       }
       if ((allVariations || variationsToRun_.contains("208")) && runMode_ != ATTENDED)
       {
         setVariation(208);
         Var208();
       }
       if ((allVariations || variationsToRun_.contains("209")) && runMode_ != ATTENDED)
       {
         setVariation(209);
         Var209();
       }
       if ((allVariations || variationsToRun_.contains("210")) && runMode_ != ATTENDED)
       {
         setVariation(210);
         Var210();
       }
       if ((allVariations || variationsToRun_.contains("211")) && runMode_ != ATTENDED)
       {
         setVariation(211);
         Var211();
       }
       if ((allVariations || variationsToRun_.contains("212")) && runMode_ != ATTENDED)
       {
         setVariation(212);
         Var212();
       }
       if ((allVariations || variationsToRun_.contains("213")) && runMode_ != ATTENDED)
       {
         setVariation(213);
         Var213();
       }
       if ((allVariations || variationsToRun_.contains("214")) && runMode_ != ATTENDED)
       {
         setVariation(214);
         Var214();
       }
       if ((allVariations || variationsToRun_.contains("215")) && runMode_ != ATTENDED)
       {
         setVariation(215);
         Var215();
       }
       if ((allVariations || variationsToRun_.contains("216")) && runMode_ != ATTENDED)
       {
         setVariation(216);
         Var216();
       }
       if ((allVariations || variationsToRun_.contains("217")) && runMode_ != ATTENDED)
       {
         setVariation(217);
         Var217();
       }
       if ((allVariations || variationsToRun_.contains("218")) && runMode_ != ATTENDED)
       {
         setVariation(218);
         Var218();
       }
       if ((allVariations || variationsToRun_.contains("219")) && runMode_ != ATTENDED)
       {
         setVariation(219);
         Var219();
       }
       if ((allVariations || variationsToRun_.contains("220")) && runMode_ != ATTENDED)
       {
         setVariation(220);
         Var220();
       }
       if ((allVariations || variationsToRun_.contains("221")) && runMode_ != ATTENDED)
       {
         setVariation(221);
         Var221();
       }
       if ((allVariations || variationsToRun_.contains("222")) && runMode_ != ATTENDED)
       {
         setVariation(222);
         Var222();
       }
       if ((allVariations || variationsToRun_.contains("223")) && runMode_ != ATTENDED)
       {
         setVariation(223);
         Var223();
       }
       if ((allVariations || variationsToRun_.contains("224")) && runMode_ != ATTENDED)
       {
         setVariation(224);
         Var224();
       }
       if ((allVariations || variationsToRun_.contains("225")) && runMode_ != ATTENDED)
       {
         setVariation(225);
         Var225();
       }
       if ((allVariations || variationsToRun_.contains("226")) && runMode_ != ATTENDED)
       {
         setVariation(226);
         Var226();
       }
       if ((allVariations || variationsToRun_.contains("227")) && runMode_ != ATTENDED)
       {
         setVariation(227);
         Var227();
       }
       if ((allVariations || variationsToRun_.contains("228")) && runMode_ != ATTENDED)
       {
         setVariation(228);
         Var228();
       }
       if ((allVariations || variationsToRun_.contains("229")) && runMode_ != ATTENDED)
       {
         setVariation(229);
         Var229();
       }
       if ((allVariations || variationsToRun_.contains("230")) && runMode_ != ATTENDED)
       {
         setVariation(230);
         Var230();
       }
       if ((allVariations || variationsToRun_.contains("231")) && runMode_ != ATTENDED)
       {
         setVariation(231);
         Var231();
       }
       if ((allVariations || variationsToRun_.contains("232")) && runMode_ != ATTENDED)
       {
         setVariation(232);
         Var232();
       }
       if ((allVariations || variationsToRun_.contains("233")) && runMode_ != ATTENDED)
       {
         setVariation(233);
         Var233();
       }
       if ((allVariations || variationsToRun_.contains("234")) && runMode_ != ATTENDED)
       {
         setVariation(234);
         Var234();
       }
       if ((allVariations || variationsToRun_.contains("235")) && runMode_ != ATTENDED)
       {
         setVariation(235);
         Var235();
       }
       if ((allVariations || variationsToRun_.contains("236")) && runMode_ != ATTENDED)
       {
         setVariation(236);
         Var236();
       }
       if ((allVariations || variationsToRun_.contains("237")) && runMode_ != ATTENDED)
       {
         setVariation(237);
         Var237();
       }
       if ((allVariations || variationsToRun_.contains("238")) && runMode_ != ATTENDED)
       {
         setVariation(238);
         Var238();
       }
       if ((allVariations || variationsToRun_.contains("239")) && runMode_ != ATTENDED)
       {
         setVariation(239);
         Var239();
       }
       if ((allVariations || variationsToRun_.contains("240")) && runMode_ != ATTENDED)
       {
         setVariation(240);
         Var240();
       }
       if ((allVariations || variationsToRun_.contains("241")) && runMode_ != ATTENDED)
       {
         setVariation(241);
         Var241();
       }
       if ((allVariations || variationsToRun_.contains("242")) && runMode_ != ATTENDED)
       {
         setVariation(242);
         Var242();
       }
       if ((allVariations || variationsToRun_.contains("243")) && runMode_ != ATTENDED)
       {
         setVariation(243);
         Var243();
       }
       if ((allVariations || variationsToRun_.contains("244")) && runMode_ != ATTENDED)
       {
         setVariation(244);
         Var244();
       }
       if ((allVariations || variationsToRun_.contains("245")) && runMode_ != ATTENDED)
       {
         setVariation(245);
         Var245();
       }
       if ((allVariations || variationsToRun_.contains("246")) && runMode_ != ATTENDED)
       {
         setVariation(246);
         Var246();
       }
       if ((allVariations || variationsToRun_.contains("247")) && runMode_ != ATTENDED)
       {
         setVariation(247);
         Var247();
       }
       if ((allVariations || variationsToRun_.contains("248")) && runMode_ != ATTENDED)
       {
         setVariation(248);
         Var248();
       }
       if ((allVariations || variationsToRun_.contains("249")) && runMode_ != ATTENDED)
       {
         setVariation(249);
         Var249();
       }
       if ((allVariations || variationsToRun_.contains("250")) && runMode_ != ATTENDED)
       {
         setVariation(250);
         Var250();
       }
       if ((allVariations || variationsToRun_.contains("251")) && runMode_ != ATTENDED)
       {
         setVariation(251);
         Var251();
       }
       if ((allVariations || variationsToRun_.contains("252")) && runMode_ != ATTENDED)
       {
         setVariation(252);
         Var252();
       }
       if ((allVariations || variationsToRun_.contains("253")) && runMode_ != ATTENDED)
       {
         setVariation(253);
         Var253();
       }
       if ((allVariations || variationsToRun_.contains("254")) && runMode_ != ATTENDED)
       {
         setVariation(254);
         Var254();
       }
       if ((allVariations || variationsToRun_.contains("255")) && runMode_ != ATTENDED)
       {
         setVariation(255);
         Var255();
       }
       if ((allVariations || variationsToRun_.contains("256")) && runMode_ != ATTENDED)
       {
         setVariation(256);
         Var256();
       }
       if ((allVariations || variationsToRun_.contains("257")) && runMode_ != ATTENDED)
       {
         setVariation(257);
         Var257();
       }
       if ((allVariations || variationsToRun_.contains("258")) && runMode_ != ATTENDED)
       {
         setVariation(258);
         Var258();
       }
       if ((allVariations || variationsToRun_.contains("259")) && runMode_ != ATTENDED)
       {
         setVariation(259);
         Var259();
       }
       if ((allVariations || variationsToRun_.contains("260")) && runMode_ != ATTENDED)
       {
         setVariation(260);
         Var260();
       }
       if ((allVariations || variationsToRun_.contains("261")) && runMode_ != ATTENDED)
       {
         setVariation(261);
         Var261();
       }
       if ((allVariations || variationsToRun_.contains("262")) && runMode_ != ATTENDED)
       {
         setVariation(262);
         Var262();
       }
       if ((allVariations || variationsToRun_.contains("263")) && runMode_ != ATTENDED)
       {
         setVariation(263);
         Var263();
       }
       if ((allVariations || variationsToRun_.contains("264")) && runMode_ != ATTENDED)
       {
         setVariation(264);
         Var264();
       }
       if ((allVariations || variationsToRun_.contains("265")) && runMode_ != ATTENDED)
       {
         setVariation(265);
         Var265();
       }
       if ((allVariations || variationsToRun_.contains("266")) && runMode_ != ATTENDED)
       {
         setVariation(266);
         Var266();
       }
       if ((allVariations || variationsToRun_.contains("267")) && runMode_ != ATTENDED)
       {
         setVariation(267);
         Var267();
       }
       if ((allVariations || variationsToRun_.contains("268")) && runMode_ != ATTENDED)
       {
         setVariation(268);
         Var268();
       }
       if ((allVariations || variationsToRun_.contains("269")) && runMode_ != ATTENDED)
       {
         setVariation(269);
         Var269();
       }
       if ((allVariations || variationsToRun_.contains("270")) && runMode_ != ATTENDED)
       {
         setVariation(270);
         Var270();
       }
       if ((allVariations || variationsToRun_.contains("271")) && runMode_ != ATTENDED)
       {
         setVariation(271);
         Var271();
       }
       if ((allVariations || variationsToRun_.contains("272")) && runMode_ != ATTENDED)
       {
         setVariation(272);
         Var272();
       }
       if ((allVariations || variationsToRun_.contains("273")) && runMode_ != ATTENDED)
       {
         setVariation(273);
         Var273();
       }
       if ((allVariations || variationsToRun_.contains("274")) && runMode_ != ATTENDED)
       {
         setVariation(274);
         Var274();
       }
       if ((allVariations || variationsToRun_.contains("275")) && runMode_ != ATTENDED)
       {
         setVariation(275);
         Var275();
       }
       if ((allVariations || variationsToRun_.contains("276")) && runMode_ != ATTENDED)
       {
         setVariation(276);
         Var276();
       }
       if ((allVariations || variationsToRun_.contains("277")) && runMode_ != ATTENDED)
       {
         setVariation(277);
         Var277();
       }
       if ((allVariations || variationsToRun_.contains("278")) && runMode_ != ATTENDED)
       {
         setVariation(278);
         Var278();
       }
       if ((allVariations || variationsToRun_.contains("279")) && runMode_ != ATTENDED)
       {
         setVariation(279);
         Var279();
       }
       if ((allVariations || variationsToRun_.contains("280")) && runMode_ != ATTENDED)
       {
         setVariation(280);
         Var280();
       }
       if ((allVariations || variationsToRun_.contains("281")) && runMode_ != ATTENDED)
       {
         setVariation(281);
         Var281();
       }
       if ((allVariations || variationsToRun_.contains("282")) && runMode_ != ATTENDED)
       {
         setVariation(282);
         Var282();
       }
       if ((allVariations || variationsToRun_.contains("283")) && runMode_ != ATTENDED)
       {
         setVariation(283);
         Var283();
       }
       if ((allVariations || variationsToRun_.contains("284")) && runMode_ != ATTENDED)
       {
         setVariation(284);
         Var284();
       }
       if ((allVariations || variationsToRun_.contains("285")) && runMode_ != ATTENDED)
       {
         setVariation(285);
         Var285();
       }
       if ((allVariations || variationsToRun_.contains("286")) && runMode_ != ATTENDED)
       {
         setVariation(286);
         Var286();
       }
       if ((allVariations || variationsToRun_.contains("287")) && runMode_ != ATTENDED)
       {
         setVariation(287);
         Var287();
       }
       if ((allVariations || variationsToRun_.contains("288")) && runMode_ != ATTENDED)
       {
         setVariation(288);
         Var288();
       }
       if ((allVariations || variationsToRun_.contains("289")) && runMode_ != ATTENDED)
       {
         setVariation(289);
         Var289();
       }
       if ((allVariations || variationsToRun_.contains("290")) && runMode_ != ATTENDED)
       {
         setVariation(290);
         Var290();
       }
       if ((allVariations || variationsToRun_.contains("291")) && runMode_ != ATTENDED)
       {
         setVariation(291);
         Var291();
       }
       if ((allVariations || variationsToRun_.contains("292")) && runMode_ != ATTENDED)
       {
         setVariation(292);
         Var292();
       }
       if ((allVariations || variationsToRun_.contains("293")) && runMode_ != ATTENDED)
       {
         setVariation(293);
         Var293();
       }
       if ((allVariations || variationsToRun_.contains("294")) && runMode_ != ATTENDED)
       {
         setVariation(294);
         Var294();
       }
       if ((allVariations || variationsToRun_.contains("295")) && runMode_ != ATTENDED)
       {
         setVariation(295);
         Var295();
       }
       if ((allVariations || variationsToRun_.contains("296")) && runMode_ != ATTENDED)
       {
         setVariation(296);
         Var296();
       }
       if ((allVariations || variationsToRun_.contains("297")) && runMode_ != ATTENDED)
       {
         setVariation(297);
         Var297();
       }
       if ((allVariations || variationsToRun_.contains("298")) && runMode_ != ATTENDED)
       {
         setVariation(298);
         Var298();
       }
       if ((allVariations || variationsToRun_.contains("299")) && runMode_ != ATTENDED)
       {
         setVariation(299);
         Var299();
       }
       if ((allVariations || variationsToRun_.contains("300")) && runMode_ != ATTENDED)
       {
         setVariation(300);
         Var300();
       }
       if ((allVariations || variationsToRun_.contains("301")) && runMode_ != ATTENDED)
       {
         setVariation(301);
         Var301();
       }
       if ((allVariations || variationsToRun_.contains("302")) && runMode_ != ATTENDED)
       {
         setVariation(302);
         Var302();
       }
       if ((allVariations || variationsToRun_.contains("303")) && runMode_ != ATTENDED)
       {
         setVariation(303);
         Var303();
       }
       if ((allVariations || variationsToRun_.contains("304")) && runMode_ != ATTENDED)
       {
         setVariation(304);
         Var304();
       }
       if ((allVariations || variationsToRun_.contains("305")) && runMode_ != ATTENDED)
       {
         setVariation(305);
         Var305();
       }
       if ((allVariations || variationsToRun_.contains("306")) && runMode_ != ATTENDED)
       {
         setVariation(306);
         Var306();
       }
       if ((allVariations || variationsToRun_.contains("307")) && runMode_ != ATTENDED)
       {
         setVariation(307);
         Var307();
       }
       if ((allVariations || variationsToRun_.contains("308")) && runMode_ != ATTENDED)
       {
         setVariation(308);
         Var308();
       }
       if ((allVariations || variationsToRun_.contains("309")) && runMode_ != ATTENDED)
       {
         setVariation(309);
         Var309();
       }
       if ((allVariations || variationsToRun_.contains("310")) && runMode_ != ATTENDED)
       {
         setVariation(310);
         Var310();
       }
       if ((allVariations || variationsToRun_.contains("311")) && runMode_ != ATTENDED)
       {
         setVariation(311);
         Var311();
       }
       if ((allVariations || variationsToRun_.contains("312")) && runMode_ != ATTENDED)
       {
         setVariation(312);
         Var312();
       }
       if ((allVariations || variationsToRun_.contains("313")) && runMode_ != ATTENDED)
       {
         setVariation(313);
         Var313();
       }
       if ((allVariations || variationsToRun_.contains("314")) && runMode_ != ATTENDED)
       {
         setVariation(314);
         Var314();
       }
       if ((allVariations || variationsToRun_.contains("315")) && runMode_ != ATTENDED)
       {
         setVariation(315);
         Var315();
       }
       if ((allVariations || variationsToRun_.contains("316")) && runMode_ != ATTENDED)
       {
         setVariation(316);
         Var316();
       }
       if ((allVariations || variationsToRun_.contains("317")) && runMode_ != ATTENDED)
       {
         setVariation(317);
         Var317();
       }
       if ((allVariations || variationsToRun_.contains("318")) && runMode_ != ATTENDED)
       {
         setVariation(318);
         Var318();
       }
       if ((allVariations || variationsToRun_.contains("319")) && runMode_ != ATTENDED)
       {
         setVariation(319);
         Var319();
       }
       if ((allVariations || variationsToRun_.contains("320")) && runMode_ != ATTENDED)
       {
         setVariation(320);
         Var320();
       }
       if ((allVariations || variationsToRun_.contains("321")) && runMode_ != ATTENDED)
       {
         setVariation(321);
         Var321();
       }
       if ((allVariations || variationsToRun_.contains("322")) && runMode_ != ATTENDED)
       {
         setVariation(322);
         Var322();
       }
       if ((allVariations || variationsToRun_.contains("323")) && runMode_ != ATTENDED)
       {
         setVariation(323);
         Var323();
       }
       if ((allVariations || variationsToRun_.contains("324")) && runMode_ != ATTENDED)
       {
         setVariation(324);
         Var324();
       }
       if ((allVariations || variationsToRun_.contains("325")) && runMode_ != ATTENDED)
       {
         setVariation(325);
         Var325();
       }
       if ((allVariations || variationsToRun_.contains("326")) && runMode_ != ATTENDED)
       {
         setVariation(326);
         Var326();
       }
       if ((allVariations || variationsToRun_.contains("327")) && runMode_ != ATTENDED)
       {
         setVariation(327);
         Var327();
       }
       if ((allVariations || variationsToRun_.contains("328")) && runMode_ != ATTENDED)
       {
         setVariation(328);
         Var328();
       }
       if ((allVariations || variationsToRun_.contains("329")) && runMode_ != ATTENDED)
       {
         setVariation(329);
         Var329();
       }
       if ((allVariations || variationsToRun_.contains("330")) && runMode_ != ATTENDED)
       {
         setVariation(330);
         Var330();
       }
       if ((allVariations || variationsToRun_.contains("331")) && runMode_ != ATTENDED)
       {
         setVariation(331);
         Var331();
       }
       if ((allVariations || variationsToRun_.contains("332")) && runMode_ != ATTENDED)
       {
         setVariation(332);
         Var332();
       }
       if ((allVariations || variationsToRun_.contains("333")) && runMode_ != ATTENDED)
       {
         setVariation(333);
         Var333();
       }
       if ((allVariations || variationsToRun_.contains("334")) && runMode_ != ATTENDED)
       {
         setVariation(334);
         Var334();
       }
       if ((allVariations || variationsToRun_.contains("335")) && runMode_ != ATTENDED)
       {
         setVariation(335);
         Var335();
       }
       if ((allVariations || variationsToRun_.contains("336")) && runMode_ != ATTENDED)
       {
         setVariation(336);
         Var336();
       }
       if ((allVariations || variationsToRun_.contains("337")) && runMode_ != ATTENDED)
       {
         setVariation(337);
         Var337();
       }
       if ((allVariations || variationsToRun_.contains("338")) && runMode_ != ATTENDED)
       {
         setVariation(338);
         Var338();
       }
       if ((allVariations || variationsToRun_.contains("339")) && runMode_ != ATTENDED)
       {
         setVariation(339);
         Var339();
       }
       if ((allVariations || variationsToRun_.contains("340")) && runMode_ != ATTENDED)
       {
         setVariation(340);
         Var340();
       }
       if ((allVariations || variationsToRun_.contains("341")) && runMode_ != ATTENDED)
       {
         setVariation(341);
         Var341();
       }
       if ((allVariations || variationsToRun_.contains("342")) && runMode_ != ATTENDED)
       {
         setVariation(342);
         Var342();
       }
       if ((allVariations || variationsToRun_.contains("343")) && runMode_ != ATTENDED)
       {
         setVariation(343);
         Var343();
       }
       if ((allVariations || variationsToRun_.contains("344")) && runMode_ != ATTENDED)
       {
         setVariation(344);
         Var344();
       }
       if ((allVariations || variationsToRun_.contains("345")) && runMode_ != ATTENDED)
       {
         setVariation(345);
         Var345();
       }
       if ((allVariations || variationsToRun_.contains("346")) && runMode_ != ATTENDED)
       {
         setVariation(346);
         Var346();
       }
       if ((allVariations || variationsToRun_.contains("347")) && runMode_ != ATTENDED)
       {
         setVariation(347);
         Var347();
       }
       if ((allVariations || variationsToRun_.contains("348")) && runMode_ != ATTENDED)
       {
         setVariation(348);
         Var348();
       }
       if ((allVariations || variationsToRun_.contains("349")) && runMode_ != ATTENDED)
       {
         setVariation(349);
         Var349();
       }
       if ((allVariations || variationsToRun_.contains("350")) && runMode_ != ATTENDED)
       {
         setVariation(350);
         Var350();
       }
       if ((allVariations || variationsToRun_.contains("351")) && runMode_ != ATTENDED)
       {
         setVariation(351);
         Var351();
       }
       if ((allVariations || variationsToRun_.contains("352")) && runMode_ != ATTENDED)
       {
         setVariation(352);
         Var352();
       }
       if ((allVariations || variationsToRun_.contains("353")) && runMode_ != ATTENDED)
       {
         setVariation(353);
         Var353();
       }
       if ((allVariations || variationsToRun_.contains("354")) && runMode_ != ATTENDED)
       {
         setVariation(354);
         Var354();
       }
       if ((allVariations || variationsToRun_.contains("355")) && runMode_ != ATTENDED)
       {
         setVariation(355);
         Var355();
       }
       if ((allVariations || variationsToRun_.contains("356")) && runMode_ != ATTENDED)
       {
         setVariation(356);
         Var356();
       }
       if ((allVariations || variationsToRun_.contains("357")) && runMode_ != ATTENDED)
       {
         setVariation(357);
         Var357();
       }
       if ((allVariations || variationsToRun_.contains("358")) && runMode_ != ATTENDED)
       {
         setVariation(358);
         Var358();
       }
    }


    /**
    Tests for a CPF1076: SPECIFIED VALUE NOT ALLOWED FOR SYSTEM VALUE %1.
    @param name The system value to test.
    @param toSet The value to set it to. The value should NOT be valid so
                 that the above error gets thrown.
    **/
    public void testBadParm(String name)
    {
      Object obj = null;
      Object toSet = null;
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable("System value not supported under this release.");
          return;
        }
        if (s.isReadOnly())
        {
          notApplicable("System value is read-only.");
          return;
        }
        type = s.getType();
        switch(type)
        {
          case SystemValueList.TYPE_STRING:
          case SystemValueList.TYPE_DATE:
            if (s.getSize() <= trash.length())
              toSet = trash.substring(0, s.getSize());
            else
              toSet = trash;
            break;
          case SystemValueList.TYPE_INTEGER:
            toSet = new Integer(-3);
            break;
          case SystemValueList.TYPE_ARRAY:
            toSet = new String[] { "TRASH" };
            break;
          case SystemValueList.TYPE_DECIMAL:
            toSet = new BigDecimal("-4");
            break;
          default:
            break;
        }
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur.";
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0084")) // value not allowed
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }


    /**
    Tests for valid getValue() and setValue() operations.
    @param name The system value to test.
    @param toSet The value to set it to. The value should be valid.
    **/
    public void testGetSet(String name, Object toSet)
    {
      testGetSet(name, toSet, null);
    }


    /**
    Tests for valid getValue() and setValue() operations.
    @param name The system value to test.
    @param toSet The value to set it to. The value should be valid.
    **/
    public void testGetSet(String name, Object toSet, String errorIdToTolerate)
    {
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable("System value not supported under this release.");
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (s.getType() != SystemValueList.TYPE_ARRAY)
          {
            if (s.getType() == SystemValueList.TYPE_STRING &&
                !((String)obj2).trim().equals(((String)toSet).trim()))
            {
              failMsg += " Real strings not equal: '"+((String)obj2).trim()+"' != '"+((String)toSet).trim()+"'.";
            }
            else if(s.getType() != SystemValueList.TYPE_STRING &&
                    !obj2.equals(toSet) &&
                    !name.equals("QLEAPADJ")) // QLEAPADJ cannot be changed (diagnostic msg CPF1030)
            {
              failMsg += " Real objects not equal: '"+obj2.toString().trim()+"' != '"+toSet.toString().trim()+"'.";
            }
          }
          else // type is TYPE_ARRAY
          {
            String[] arr1 = (String[])toSet;
            String[] arr2 = (String[])obj2;
            for (int x=0; x<(arr1.length < arr2.length ? arr1.length : arr2.length); ++x)
            {
                if (!arr1[x].trim().equals(arr2[x].trim()))
                {
                  failMsg += "\n Real objects at ["+x+"] not equal: "+arr1[x]+" != "+arr2[x];
                }
            }
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        if ((errorIdToTolerate != null) &&
            (e instanceof AS400Exception) &&
            ((AS400Exception)e).getAS400Message().getID().equals(errorIdToTolerate))
        {
          AS400Message msg = ((AS400Exception)e).getAS400Message();
          succeeded("Tolerating " + msg.getID() + ": " + msg.getText());
          return; // the 'set' failed, so don't bother restoring original value
        }

        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      finally
      {
        // Restore to original value.
        try
        {
          if (s != null && obj != null && !s.isReadOnly())
            s.setValue(obj);
        }
        catch(Exception e)
        {
          failMsg += " Unable to reset "+name+" to original value: '"+obj.toString()+"'";
          failMsg += "\n "+e.getMessage();
        }
      }

      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }



    /**
    Tests for valid getValue() operation.
    @param name The system value to test.
    **/
    public void testGet(String name)
    {
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable("System value not supported under this release.");
          return;
        }
        obj = s.getValue();
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        failed(e, failMsg);
        return;
      }

      System.out.println("  " + name + "\'s value is " + obj.toString());
      succeeded();
    }

// Note: There are no variations for testing bad parms on system values
//       that are read only.


    /**
    Test QABNORMSW with a good parameter.
    **/
    public void Var001()
    {
      testGetSet("QABNORMSW", "T");
    }

    /**
    Test QACGLVL with a bad parameter.
    **/
    public void Var002()
    {
      testBadParm("QACGLVL");
    }

    /**
    Test QACGLVL with a good parameter.
    **/
    public void Var003()
    {
      testGetSet("QACGLVL", new String[] { "*NONE" });
    }

    /**
    Test QACTJOB with a bad parameter.
    **/
    public void Var004()
    {
      testBadParm("QACTJOB");
    }

    /**
    Test QACTJOB with a good parameter.
    **/
    public void Var005()
    {
      testGetSet("QACTJOB", new Integer(100));
    }

    /**
    Test QADLACTJ with a bad parameter.
    **/
    public void Var006()
    {
      testBadParm("QADLACTJ");
    }

    /**
    Test QADLACTJ with a good parameter.
    **/
    public void Var007()
    {
      testGetSet("QADLACTJ", new Integer(100));
    }

    /**
    Test QADLTOTJ with a bad parameter.
    **/
    public void Var008()
    {
      testBadParm("QADLTOTJ");
    }

    /**
    Test QADLTOTJ with a good parameter.
    **/
    public void Var009()
    {
      testGetSet("QADLTOTJ", new Integer(100));
    }

    /**
    Test QADLSPLA with a bad parameter.
    **/
    public void Var010()
    {
      testBadParm("QADLSPLA");
    }

    /**
    Test QADLSPLA with a good parameter.
    **/
    public void Var011()
    {
      testGetSet("QADLSPLA", new Integer(1024));
    }

    /**
    Test QALWOBJRST with a bad parameter.
    **/
    public void Var012()
    {
      testBadParm("QALWOBJRST");
    }

    /**
    Test QALWOBJRST with a good parameter.
    **/
    public void Var013()
    {
      testGetSet("QALWOBJRST", new String[] { "*ALWSYSSTT" });
    }

    /**
    Test QALWUSRDMN with a bad parameter.
    **/
    public void Var014()
    {
//      testBadParm("QALWUSRDMN");
      // Since QALWUSRDMN takes a list of libraries, the normal
      // testBadParm method won't work.
      // We expect a CPF1078
      String name = "QALWUSRDMN";
      Object obj = null;
      Object toSet = "*NONE";
      String failMsg = "";
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur.";
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1078"))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QALWUSRDMN with a good parameter.
    **/
    public void Var015()
    {
      testGetSet("QALWUSRDMN", new String[] { "QSYS", "QTEMP" });
    }

    /**
    Test QASTLVL with a bad parameter.
    **/
    public void Var016()
    {
      testBadParm("QASTLVL");
    }

    /**
    Test QASTLVL with a good parameter.
    **/
    public void Var017()
    {
      testGetSet("QASTLVL", "*ADVANCED");
    }

    /**
    Test QATNPGM with a bad parameter.
    **/
    public void Var018()
    {
//      testBadParm("QATNPGM");
      // Since QATNPGM takes a program name, the
      // testBadParm method won't work.
      // We expect a CPF1813
      String name = "QATNPGM";
      Object obj = null;
      Object toSet = "*TRASH";
      String failMsg = "";
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur.";
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813"))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QATNPGM with a good parameter.
    **/
    public void Var019()
    {
//      testGetSet("QATNPGM", "*NONE");
      String name = "QATNPGM";
      Object toSet = "*ASSIST";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();    // "01234567890123456789"
          if (!((String)obj2).trim().equals("QEZMAIN   QSYS"))
          {
            failMsg += " Unexpected value returned: "+((String)obj2).trim();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QAUDCTL with a bad parameter.
    **/
    public void Var020()
    {
      testBadParm("QAUDCTL");
    }

    /**
    Test QAUDCTL with a good parameter.
    **/
    public void Var021()
    {
      testGetSet("QAUDCTL", new String[] { "*OBJAUD" });
    }

    /**
    Test QAUDENDACN with a bad parameter.
    **/
    public void Var022()
    {
      testBadParm("QAUDENDACN");
    }

    /**
    Test QAUDENDACN with a good parameter.
    **/
    public void Var023()
    {
      testGetSet("QAUDENDACN", "*PWRDWNSYS");
    }

    /**
    Test QAUDFRCLVL with a bad parameter.
    **/
    public void Var024()
    {
      testBadParm("QAUDFRCLVL");
    }

    /**
    Test QAUDFRCLVL with a good parameter.
    **/
    public void Var025()
    {
      testGetSet("QAUDFRCLVL", new Integer(100));
    }

    /**
    Test QAUDFRCLVL with a good parameter.
    **/
    public void Var026()
    {
//      testGetSet("QAUDFRCLVL", "*SYS");
      String name = "QAUDFRCLVL";
      Object toSet = "*SYS";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((Integer)obj2).intValue() != 0)
          {
            failMsg += " Unexpected value returned: "+obj2.toString();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QAUDLVL with a bad parameter.
    **/
    public void Var027()
    {
      testBadParm("QAUDLVL");
    }

    /**
    Test QAUDLVL with a good parameter.
    **/
    public void Var028()
    {
      testGetSet("QAUDLVL", new String[] { "*SYSMGT" });
    }

    /**
    Test QAUTOCFG with a bad parameter.
    **/
    public void Var029()
    {
      testBadParm("QAUTOCFG");
    }

    /**
    Test QAUTOCFG with a good parameter.
    **/
    public void Var030()
    {
      testGetSet("QAUTOCFG", "0");
    }

    /**
    Test QAUTORMT with a bad parameter.
    **/
    public void Var031()
    {
      testBadParm("QAUTORMT");
    }

    /**
    Test QAUTORMT with a good parameter.
    **/
    public void Var032()
    {
      testGetSet("QAUTORMT", "0");
    }

    /**
    Test QAUTOSPRPT with a bad parameter.
    **/
    public void Var033()
    {
      testBadParm("QAUTOSPRPT");
    }

    /**
    Test QAUTOSPRPT with a good parameter.
    **/
    public void Var034()
    {
      testGetSet("QAUTOSPRPT", "1");
    }

    /**
    Test QAUTOVRT with a bad parameter.
    **/
    public void Var035()
    {
      testBadParm("QAUTOVRT");
    }

    /**
    Test QAUTOVRT with a good parameter.
    **/
    public void Var036()
    {
      testGetSet("QAUTOVRT", new Integer(100));
    }

    /**
    Test QAUTOVRT with a good parameter.
    **/
    public void Var037()
    {
//      testGetSet("QAUTOVRT", "*NOMAX");
      String name = "QAUTOVRT";
      Object toSet = new Integer(32767); // The chgsysval command won't let us use *NOMAX.
                                         // *NOMAX is equivalent to 32767 on this command.
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((Integer)obj2).intValue() != 32767)
          {
            failMsg += " Unexpected value returned: "+obj2.toString();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QBASACTLVL with a bad parameter.
    **/
    public void Var038()
    {
      testBadParm("QBASACTLVL");
    }

    /**
    Test QBASACTLVL with a good parameter.
    **/
    public void Var039()
    {
      testGetSet("QBASACTLVL", new Integer(100));
    }

    /**
    Test QBASPOOL with a bad parameter.
    **/
    public void Var040()
    {
      testBadParm("QBASPOOL");
    }

    /**
    Test QBASPOOL with a good parameter.
    **/
    public void Var041()
    {
      testGetSet("QBASPOOL", new Integer(256));
    }

    /**
    Test QBOOKPATH with a bad parameter.
    **/
    public void Var042()
    {
//      testBadParm("QBOOKPATH");
      String name = "QBOOKPATH";  // Need to test for CPF1078... value not changed.
      Object obj = null;
      Object toSet = new String[] { "TRASH" };
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly() || vrm >= 0x00050300)
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1078"))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QBOOKPATH with a good parameter.
    **/
    public void Var043()
    {
      testGetSet("QBOOKPATH", new String[] { "/QDLS/QBKBOOKS/BOOKS" });
    }

    /**
    Test QCCSID with a bad parameter.
    **/
    public void Var044()
    {
      testBadParm("QCCSID");
    }

    /**
    Test QCCSID with a good parameter.
    **/
    public void Var045()
    {
      testGetSet("QCCSID", new Integer(37));
    }

    /**
    Test QCENTURY with a good parameter.
    **/
    public void Var046()
    {
      testBadParm("QCENTURY");
    }

    /**
    Test QCENTURY with a good parameter.
    **/
    public void Var047()
    {
      testGetSet("QCENTURY", "1");      // 0 for pre-2000, 1 for 2000 and beyond
    }

    /**
    Test QCHRID with a bad parameter.
    **/
    public void Var048()
    {
      testBadParm("QCHRID");
    }

    /**
    Test QCHRID with a good parameter.
    **/
    public void Var049()
    {
      // First need to set QCCSID to 65535, otherwise we might get a CPF1852 when attempting to change QCHRID. ("not a valid combination")
      SystemValue sysVal_QCCSID = null;
      Object originalValue = null;
      try
      {
        sysVal_QCCSID = new SystemValue(pwrSys_, "QCCSID");
        originalValue = sysVal_QCCSID.getValue();
        sysVal_QCCSID.setValue(new Integer(65535));

        testGetSet("QCHRID", new String[] { "0000001172", "0000001027" });
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception occurred.");
      }
      finally
      {
        if (sysVal_QCCSID != null && originalValue != null) {
          try
          {
            sysVal_QCCSID.setValue(originalValue);
            if (!sysVal_QCCSID.getValue().equals(originalValue))
            {
              failed("Unable to set QCCSID to original value.");
              return;
            }
          }
          catch(Exception f)
          {
            failed("Unable to reset QCCSID.");
            return;
          }
        }
      }
    }

    /**
    Test QCMNARB with a bad parameter.
    **/
    public void Var050()
    {
      testBadParm("QCMNARB");
    }

    /**
    Test QCMNARB with a good parameter.
    **/
    public void Var051()
    {
      testGetSet("QCMNARB", "0000000012");
    }

    /**
    Test QCMNRCYLMT with a bad parameter.
    **/
    public void Var052()
    {
      testBadParm("QCMNRCYLMT");
    }

    /**
    Test QCMNRCYLMT with a good parameter.
    **/
    public void Var053()
    {
//      testGetSet("QCMNRCYLMT", new String[] { "10", "12" });
      String name = "QCMNRCYLMT";
      Object toSet = new String[] { "10", "12" };
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          String[] arr = (String[])obj2;
          if (!arr[0].equals("0000000010"))
          {
            failMsg += " QCMNRCYLMT[0] wrong: "+arr[0];
          }
          if (!arr[1].equals("0000000012"))
          {
            failMsg += " QCMNRCYLMT[1] wrong: "+arr[1];
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QCNTRYID with a bad parameter.
    **/
    public void Var054()
    {
//      testBadParm("QCNTRYID");
      String name = "QCNTRYID";
      Object obj = null;
      Object toSet = "00";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF3FC1")) // Test for Country identifier is not valid
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QCNTRYID with a good parameter.
    **/
    public void Var055()
    {
      testGetSet("QCNTRYID", "AF"); // Afghanistan
    }

    /**
    Test QCONSOLE with a good parameter.
    **/
    public void Var056()
    {
      testGetSet("QCONSOLE", "QCONSOLE");
    }

    /**
    Test QCRTAUT with a bad parameter.
    **/
    public void Var057()
    {
      testBadParm("QCRTAUT");
    }

    /**
    Test QCRTAUT with a good parameter.
    **/
    public void Var058()
    {
      testGetSet("QCRTAUT", "*ALL");
    }

    /**
    Test QCRTOBJAUD with a bad parameter.
    **/
    public void Var059()
    {
      testBadParm("QCRTOBJAUD");
    }

    /**
    Test QCRTOBJAUD with a good parameter.
    **/
    public void Var060()
    {
      testGetSet("QCRTOBJAUD", "*ALL");
    }

    /**
    Test QCTLSBSD with a bad parameter.
    **/
    public void Var061()
    {
//      testBadParm("QCTLSBSD");
      String name = "QCTLSBSD";
      Object obj = null;
      Object toSet = "TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for TRASH in *LIBL not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QCTLSBSD with a good parameter.
    **/
    public void Var062()
    {
      //                      01234567890123456789
      testGetSet("QCTLSBSD", "QBASE     QSYS");
    }

    /**
    Test QCURSYM with a bad parameter.
    **/
    public void Var063()
    {
//      testBadParm("QCURSYM");
      String name = "QCURSYM";
      Object obj = null;
      Object toSet = " "; // not allowed for a currency symbol
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QCURSYM with a good parameter.
    **/
    public void Var064()
    {
      testGetSet("QCURSYM", "X");
    }

    /**
    Test QDATE with a bad parameter.
    **/
    public void Var065()
    {
//      testBadParm("QDATE");
      String failMsg = "";
      try
      {
        String name = "QDATE";
        Object obj = null;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.YEAR, 0);
        Object toSet = (java.util.Date)cal.getTime();
        int type;
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
    	
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException") &&
            e.getMessage().toUpperCase().startsWith("QYEAR")) // You can't pass in an invalid Date
                                                              // without our code intercepting it before
                                                              // it gets to the 400.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
      
    }

    /**
    Test QDATE with a good parameter.
    **/
    public void Var066()
    {
//      testGetSet("QDATE", Calendar.getInstance().getTime());
      String name = "QDATE";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        Object toSet = Calendar.getInstance().getTime();
        Date curDate = Calendar.getInstance().getTime();
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
      
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          Calendar cal1 = Calendar.getInstance();
          Calendar cal2 = Calendar.getInstance();
          cal1.clear();
          cal2.clear();
          Date d1 = (Date)obj2;
          Date d2 = (Date)curDate;
          cal1.setTime(d1);
          cal2.setTime(d2);
          if ((cal1.get(Calendar.YEAR) % 2000) != (cal2.get(Calendar.YEAR) % 2000))
          {
            failMsg += "\n Years not equal: "+cal1.get(Calendar.YEAR)+" != "+cal2.get(Calendar.YEAR);
          }
          if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH))
          {
            failMsg += "\n Months not equal: "+cal1.get(Calendar.MONTH)+" != "+cal2.get(Calendar.MONTH);
          }
          if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH))
          {
            failMsg += "\n Days not equal: "+cal1.get(Calendar.DAY_OF_MONTH)+" != "+cal2.get(Calendar.DAY_OF_MONTH);
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+f.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
        failMsg += " "+e.getMessage();
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QDATFMT with a bad parameter.
    **/
    public void Var067()
    {
      testBadParm("QDATFMT");
    }

    /**
    Test QDATFMT with a good parameter.
    **/
    public void Var068()
    {
      testGetSet("QDATFMT", "JUL");
    }

    /**
    Test QDATSEP with a bad parameter.
    **/
    public void Var069()
    {
      testBadParm("QDATSEP");
    }

    /**
    Test QDATSEP with a good parameter.
    **/
    public void Var070()
    {
      testGetSet("QDATSEP", ",");
    }

    /**
    Test QDAY with a bad parameter.
    **/
    public void Var071()
    {
//      testBadParm("QDAY");
      String name = "QDAY";
      Object obj = null;
      Object toSet = "XX";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QDAY with a good parameter.
    **/
    public void Var072()
    {
      testGetSet("QDAY", "18");
    }

    /**
    Test QDAYOFWEEK with a good parameter.
    **/
    public void Var073()
    {
      testGetSet("QDAYOFWEEK", "*SUN");
    }

    /**
    Test QDBRCVYWT with a bad parameter.
    **/
    public void Var074()
    {
      testBadParm("QDBRCVYWT");
    }

    /**
    Test QDBRCVYWT with a good parameter.
    **/
    public void Var075()
    {
      testGetSet("QDBRCVYWT", "1");
    }

    /**
    Test QDECFMT with a bad parameter.
    **/
    public void Var076()
    {
      testBadParm("QDECFMT");
    }

    /**
    Test QDECFMT with a good parameter.
    **/
    public void Var077()
    {
      testGetSet("QDECFMT", "I");
    }

    /**
    Test QDEVNAMING with a bad parameter.
    **/
    public void Var078()
    {
      testBadParm("QDEVNAMING");
    }

    /**
    Test QDEVNAMING with a good parameter.
    **/
    public void Var079()
    {
      testGetSet("QDEVNAMING", "*S36");
    }

    /**
    Test QDEVRCYACN with a bad parameter.
    **/
    public void Var080()
    {
      testBadParm("QDEVRCYACN");
    }

    /**
    Test QDEVRCYACN with a good parameter.
    **/
    public void Var081()
    {
      testGetSet("QDEVRCYACN", "*ENDJOB");
    }

    /**
    Test QDSCJOBITV with a bad parameter.
    **/
    public void Var082()
    {
      testBadParm("QDSCJOBITV");
    }

    /**
    Test QDSCJOBITV with a good parameter.
    **/
    public void Var083()
    {                       //  0123456789
      testGetSet("QDSCJOBITV", "0000000100");
    }

    /**
    Test QDSPSGNINF with a bad parameter.
    **/
    public void Var084()
    {
      testBadParm("QDSPSGNINF");
    }

    /**
    Test QDSPSGNINF with a good parameter.
    **/
    public void Var085()
    {
      testGetSet("QDSPSGNINF", "1");
    }

    /**
    Test QDYNPTYSCD with a bad parameter.
    **/
    public void Var086()
    {
      testBadParm("QDYNPTYSCD");
    }

    /**
    Test QDYNPTYSCD with a good parameter.
    **/
    public void Var087()
    {
      testGetSet("QDYNPTYSCD", "0");
    }

    /**
    Test QFRCCVNRST with a bad parameter.
    **/
    public void Var088()
    {
      testBadParm("QFRCCVNRST");
    }

    /**
    Test QFRCCVNRST with a good parameter.
    **/
    public void Var089()
    {
      testGetSet("QFRCCVNRST", "1");
    }

    /**
    Test QHOUR with a bad parameter.
    **/
    public void Var090()
    {
      testBadParm("QHOUR");
    }

    /**
    Test QHOUR with a good parameter.
    **/
    public void Var091()
    {
      testGetSet("QHOUR", "07");
    }

    /**
    Test QHSTLOGSIZ with a bad parameter.
    **/
    public void Var092()
    {
      testBadParm("QHSTLOGSIZ");
    }

    /**
    Test QHSTLOGSIZ with a good parameter.
    **/
    public void Var093()
    {
      testGetSet("QHSTLOGSIZ", new Integer(100));
    }

    /**
    Test QIGC with a good parameter.
    **/
    public void Var094()
    {
      testGetSet("QIGC", "0");
    }

    /**
    Test QIGCCDEFNT with a bad parameter.
    **/
    public void Var095()
    {
//      testBadParm("QIGCCDEFNT");
      String name = "QIGCCDEFNT";
      Object obj = null;
      Object toSet = new String[] { "*TRASH" };
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1132")) // Check for name specified not valid
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QIGCCDEFNT with a good parameter.
    **/
    public void Var096()
    {
      testGetSet("QIGCCDEFNT", new String[] { "SOMEFONT", "SOMELIB" });
    }

    /**
    Test QINACTITV with a bad parameter.
    **/
    public void Var097()
    {
      testBadParm("QINACTITV");
    }

    /**
    Test QINACTITV with a good parameter.
    **/
    public void Var098()
    {              //          0123456789
      testGetSet("QINACTITV", "0000000100");
    }

    /**
    Test QINACTMSGQ with a bad parameter.
    **/
    public void Var099()
    {
//      testBadParm("QINACTMSGQ");
      String name = "QINACTMSGQ";
      Object obj = null;
      Object toSet = "*TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for *TRASH not found
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QINACTMSGQ with a good parameter.
    **/
    public void Var100()
    {
      testGetSet("QINACTMSGQ", new String[] { "*DSCJOB" });
    }

    /**
    Test QIPLDATTIM with a bad parameter.
    **/
    public void Var101()
    {
//      testBadParm("QIPLDATTIM");
      String name = "QIPLDATTIM";
      Object obj = null;
      Object toSet = "TRASH TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QIPLDATTIM with a good parameter.
    **/
    public void Var102()
    {
      // Date must be within 11 months
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, 1);
      String month = ""+(cal.get(Calendar.MONTH)+1);
      String day = ""+cal.get(Calendar.DAY_OF_MONTH);
      String year = ""+cal.get(Calendar.YEAR);
      if (month.length() < 2) month="0"+month;
      if (day.length() < 2) day="0"+day;
      if (year.length() > 2) year=year.substring(year.length()-2, year.length());

      String name = "QIPLDATTIM";
      Object toSet = ""+month+day+year+" 090000";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          String expected = year+month+day+"090000";
          if (cal.get(Calendar.YEAR) > 1999)
          {
            expected = "1"+expected;
          }
          else
          {
            expected = "0"+expected;
          }
          if (!((String)obj2).trim().equals(expected))
          {
            failMsg += " Unexpected data returned: "+obj2.toString()+" != "+expected;
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred using '"+toSet+"'.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      String newDate = "";
      try
      {
        if (((String)obj).indexOf("*") >= 0)
        {
          newDate = (String)obj;
        }
        else
        {
          String left = ((String)obj).substring(0,7);
          String right = ((String)obj).substring(7, ((String)obj).length());
          newDate += left.substring(3,5); //month
          newDate += left.substring(5,7); //day
          newDate += left.substring(1,3); //year
          newDate += " "+right; //time
        }
        s.setValue(newDate);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
        failMsg += "\n "+e.getMessage();
        failMsg += "\n Object: "+newDate;
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QIPLSTS with a good parameter.
    **/
    public void Var103()
    {
      testGetSet("QIPLSTS", "3");
    }

    /**
    Test QIPLTYPE with a bad parameter.
    **/
    public void Var104()
    {
      testBadParm("QIPLTYPE");
    }

    /**
    Test QIPLTYPE with a good parameter.
    **/
    public void Var105()
    {
      testGetSet("QIPLTYPE", "1");
    }

    /**
    Test QJOBMSGQFL with a bad parameter.
    **/
    public void Var106()
    {
      testBadParm("QJOBMSGQFL");
    }

    /**
    Test QJOBMSGQFL with a good parameter.
    **/
    public void Var107()
    {
      testGetSet("QJOBMSGQFL", "*WRAP");
    }

    /**
    Test QJOBMSGQMX with a bad parameter.
    **/
    public void Var108()
    {
      testBadParm("QJOBMSGQMX");
    }

    /**
    Test QJOBMSGQMX with a good parameter.
    **/
    public void Var109()
    {
      testGetSet("QJOBMSGQMX", new Integer(32));
    }

    /**
    Test QJOBMSGQSZ with a bad parameter.
    **/
    public void Var110()
    {
      testBadParm("QJOBMSGQSZ");
    }

    /**
    Test QJOBMSGQSZ with a good parameter.
    **/
    public void Var111()
    {
      testGetSet("QJOBMSGQSZ", new Integer(100));
    }

    /**
    Test QJOBMSGQTL with a bad parameter.
    **/
    public void Var112()
    {
      testBadParm("QJOBMSGQTL");
    }

    /**
    Test QJOBMSGQTL with a good parameter.
    **/
    public void Var113()
    {
      testGetSet("QJOBMSGQTL", new Integer(100));
    }

    /**
    Test QJOBSPLA with a bad parameter.
    **/
    public void Var114()
    {
      testBadParm("QJOBSPLA");
    }

    /**
    Test QJOBSPLA with a good parameter.
    **/
    public void Var115()
    {
      testGetSet("QJOBSPLA", new Integer(9600));
    }

    /**
    Test QKBDBUF with a bad parameter.
    **/
    public void Var116()
    {
      testBadParm("QKBDBUF");
    }

    /**
    Test QKBDBUF with a good parameter.
    **/
    public void Var117()
    {
      testGetSet("QKBDBUF", "*NO");
    }

    /**
    Test QKBDTYPE with a bad parameter.
    **/
    public void Var118()
    {
//      testBadParm("QKBDTYPE");
      String name = "QKBDTYPE";
      Object obj = null;
      Object toSet = "TRA"; // not a valid keyboard type
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1203")) // Check for keyboard identifier not correct
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QKBDTYPE with a good parameter.
    **/
    public void Var119()
    {
      testGetSet("QKBDTYPE", "ALI"); // Albanian
    }

    /**
    Test QLANGID with a bad parameter.
    **/
    public void Var120()
    {
//      testBadParm("QLANGID");
      String name = "QLANGID";
      Object obj = null;
      Object toSet = "TRA"; // Not a valid lang id
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF3FC0")) // Check for language identifier not valid
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QLANGID with a good parameter.
    **/
    public void Var121()
    {
      testGetSet("QLANGID", "AFR"); // Afrikaans
    }

    /**
    Test QLEAPADJ with a bad parameter.
    **/
    public void Var122()
    {
      testBadParm("QLEAPADJ");
    }

    /**
    Test QLEAPADJ with a good parameter.
    **/
    public void Var123()
    {
      testGetSet("QLEAPADJ", new Integer(1));
    }

    /**
    Test QLMTDEVSSN with a bad parameter.
    **/
    public void Var124()
    {
      testBadParm("QLMTDEVSSN");
    }

    /**
    Test QLMTDEVSSN with a good parameter.
    **/
    public void Var125()
    {
      testGetSet("QLMTDEVSSN", "1");
    }

    /**
    Test QLMTSECOFR with a bad parameter.
    **/
    public void Var126()
    {
      testBadParm("QLMTSECOFR");
    }

    /**
    Test QLMTSECOFR with a good parameter.
    **/
    public void Var127()
    {
      testGetSet("QLMTSECOFR", "0");
    }

    /**
    Test QLOCALE with a bad parameter.
    **/
    public void Var128()
    {
//      testBadParm("QLOCALE");
      String name = "QLOCALE";
      Object obj = null;
      Object toSet = "TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1078")) // Check for system value not changed.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QLOCALE with a good parameter.
    **/
    public void Var129()
    {
      testGetSet("QLOCALE", "/QSYS.LIB/AR_AA.LOCALE");
    }

    /**
    Test QLOCALE with a good parameter.
    **/
    public void Var130()
    {
      testGetSet("QLOCALE", "*NONE");
    }

    /**
    Test QLOCALE with a good parameter.
    **/
    public void Var131()
    {
      testGetSet("QLOCALE", "*POSIX");
    }

    /**
    Test QMAXACTLVL with a bad parameter.
    **/
    public void Var132()
    {
      testBadParm("QMAXACTLVL");
    }

    /**
    Test QMAXACTLVL with a good parameter.
    **/
    public void Var133()
    {
      testGetSet("QMAXACTLVL", new Integer(100));
    }

    /**
    Test QMAXACTLVL with a good parameter.
    **/
    public void Var134()
    {
//      testGetSet("QMAXACTLVL", "*NOMAX");
      String name = "QMAXACTLVL";
      Object toSet = "*NOMAX"; // *NOMAX means 32767
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((Integer)obj2).intValue() != 32767)
          {
            failMsg += " Unexpected return value: "+((Integer)obj2).intValue();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QMAXSGNACN with a bad parameter.
    **/
    public void Var135()
    {
      testBadParm("QMAXSGNACN");
    }

    /**
    Test QMAXSGNACN with a good parameter.
    **/
    public void Var136()
    {
      testGetSet("QMAXSGNACN", "2");
    }

    /**
    Test QMAXSIGN with a bad parameter.
    **/
    public void Var137()
    {
//      testBadParm("QMAXSIGN");
      String name = "QMAXSIGN";
      Object obj = null;
      Object toSet = "TRASHT";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD2266")) // Check for TRASHT password attempts not valid.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QMAXSIGN with a good parameter.
    **/
    public void Var138()
    {
      testGetSet("QMAXSIGN", "*NOMAX");
    }

    /**
    Test QMAXSIGN with a good parameter.
    **/
    public void Var139()
    {
      testGetSet("QMAXSIGN", "000025");
    }

    /**
    Test QMCHPOOL with a bad parameter.
    **/
    public void Var140()
    {
      testBadParm("QMCHPOOL");
    }

    /**
    Test QMCHPOOL with a good parameter.
    **/
    public void Var141()
    {
      try
      {
        // Get current value, and change it to (currentValue + 1).
        // That way we avoid having to guess what the minimum valid value is.
        SystemValue sysVal_QMCHPOOL = new SystemValue(pwrSys_, "QMCHPOOL");
        int originalValue = ((Integer)sysVal_QMCHPOOL.getValue()).intValue();

        testGetSet("QMCHPOOL", new Integer(originalValue+1));
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception occurred.");
      }
    }

    /**
    Test QMINUTE with a bad parameter.
    **/
    public void Var142()
    {
      testBadParm("QMINUTE");
    }

    /**
    Test QMINUTE with a good parameter.
    **/
    public void Var143()
    {
      testGetSet("QMINUTE", "30");
    }

    /**
    Test QMODEL with a good parameter.
    **/
    public void Var144()
    {
      testGetSet("QMODEL", "1234");
    }

    /**
    Test QMONTH with a bad parameter.
    **/
    public void Var145()
    {
      testBadParm("QMONTH");
    }

    /**
    Test QMONTH with a good parameter.
    **/
    public void Var146()
    {
      testGetSet("QMONTH", "07");
    }

    /**
    Test QPASTHRSVR with a bad parameter.
    **/
    public void Var147()
    {
      testBadParm("QPASTHRSVR");
    }

    /**
    Test QPASTHRSVR with a good parameter.
    **/
    public void Var148()
    {                         //0123456789
      testGetSet("QPASTHRSVR", "0000000100");
    }

    /**
    Test QPFRADJ with a bad parameter.
    **/
    public void Var149()
    {
      testBadParm("QPFRADJ");
    }

    /**
    Test QPFRADJ with a good parameter.
    **/
    public void Var150()
    {
      testGetSet("QPFRADJ", "0");
    }

    /**
    Test QPRBFTR with a bad parameter.
    **/
    public void Var151()
    {
//      testBadParm("QPRBFTR");
      String name = "QPRBFTR";
      Object obj = null;
      Object toSet = new String[] { "TRASH", "QSYS" };
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF9801")) // Check for object not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPRBFTR with a good parameter.
    **/
    public void Var152()
    {
      testGetSet("QPRBFTR", new String[] {"*NONE"});
    }

    /**
    Test QPRBHLDITV with a bad parameter.
    **/
    public void Var153()
    {
      testBadParm("QPRBHLDITV");
    }

    /**
    Test QPRBHLDITV with a good parameter.
    **/
    public void Var154()
    {
      testGetSet("QPRBHLDITV", new Integer(100));
    }

    /**
    Test QPRTDEV with a bad parameter.
    **/
    public void Var155()
    {
//      testBadParm("QPRTDEV");
      String name = "QPRTDEV";
      Object obj = null;
      Object toSet = "TRASHTRASHT"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1059")) // too long
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPRTDEV with a good parameter.
    **/
    public void Var156()
    {
      testGetSet("QPRTDEV", "PRT01");
    }

    /**
    Test QPRTKEYFMT with a bad parameter.
    **/
    public void Var157()
    {
      testBadParm("QPRTKEYFMT");
    }

    /**
    Test QPRTKEYFMT with a good parameter.
    **/
    public void Var158()
    {
      testGetSet("QPRTKEYFMT", "*PRTALL");
    }

    /**
    Test QPRTTXT with a bad parameter.
    **/
    public void Var159()
    {
//      testBadParm("QPRTTXT");
      String name = "QPRTTXT";
      Object obj = null;
      Object toSet = "This is more than 30 characters";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1059")) // Check for length not valid.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPRTTXT with a good parameter.
    **/
    public void Var160()
    {
      testGetSet("QPRTTXT", "Maximum number of chars is 30.");
    }

    /**
    Test QPRTTXT with a good parameter.
    **/
    public void Var161()
    {
//      testGetSet("QPRTTXT", "*BLANK");
      String name = "QPRTTXT";
      Object toSet = "*BLANK";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((String)obj2).trim().length() != 0)
          {
            failMsg += "\n Unexpected data retrieved: '"+(String)obj2+"'";
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QPWDEXPITV with a bad parameter.
    **/
    public void Var162()
    {
//      testBadParm("QPWDEXPITV");
      String name = "QPWDEXPITV";
      Object obj = null;
      Object toSet = "TRASHT";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD2281")) // Check for expiration interval not valid.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPWDEXPITV with a good parameter.
    **/
    public void Var163()
    {
      testGetSet("QPWDEXPITV", "000366");
    }

    /**
    Test QPWDEXPITV with a good parameter.
    **/
    public void Var164()
    {
      testGetSet("QPWDEXPITV", "*NOMAX");
    }

    /**
    Test QPWDLMTAJC with a bad parameter.
    **/
    public void Var165()
    {
      testBadParm("QPWDLMTAJC");
    }

    /**
    Test QPWDLMTAJC with a good parameter.
    **/
    public void Var166()
    {
      testGetSet("QPWDLMTAJC", "1");
    }

    /**
    Test QPWDLMTCHR with a bad parameter.
    **/
    public void Var167()
    {
//      testBadParm("QPWDLMTCHR");
      String name = "QPWDLMTCHR";
      Object obj = null;
      Object toSet = "ABCDEFGHIJK"; // too long
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1059")) // Check for length not valid.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPWDLMTCHR with a good parameter.
    **/
    public void Var168()
    {
      testGetSet("QPWDLMTCHR", "AEIOUY9876");
    }

    /**
    Test QPWDLMTCHR with a good parameter.
    **/
    public void Var169()
    {
      testGetSet("QPWDLMTCHR", "*NONE");
    }

    /**
    Test QPWDLMTREP with a bad parameter.
    **/
    public void Var170()
    {
      testBadParm("QPWDLMTREP");
    }

    /**
    Test QPWDLMTREP with a good parameter.
    **/
    public void Var171()
    {
      testGetSet("QPWDLMTREP", "1");
    }

    /**
    Test QPWDMAXLEN with a bad parameter.
    **/
    public void Var172()
    {
      testBadParm("QPWDMAXLEN");
    }

    /**
    Test QPWDMAXLEN with a good parameter.
    **/
    public void Var173()
    {
      testGetSet("QPWDMAXLEN", new Integer(10));
    }

    /**
    Test QPWDMINLEN with a bad parameter.
    **/
    public void Var174()
    {
      testBadParm("QPWDMINLEN");
    }

    /**
    Test QPWDMINLEN with a good parameter.
    **/
    public void Var175()
    {
      testGetSet("QPWDMINLEN", new Integer(4));
    }

    /**
    Test QPWDPOSDIF with a bad parameter.
    **/
    public void Var176()
    {
      testBadParm("QPWDPOSDIF");
    }

    /**
    Test QPWDPOSDIF with a good parameter.
    **/
    public void Var177()
    {
      testGetSet("QPWDPOSDIF", "1");
    }

    /**
    Test QPWDRQDDGT with a bad parameter.
    **/
    public void Var178()
    {
      testBadParm("QPWDRQDDGT");
    }

    /**
    Test QPWDRQDDGT with a good parameter.
    **/
    public void Var179()
    {
      testGetSet("QPWDRQDDGT", "1");
    }

    /**
    Test QPWDRQDDIF with a bad parameter.
    **/
    public void Var180()
    {
//      testBadParm("QPWDRQDDIF");
      String name = "QPWDRQDDIF";
      Object obj = null;
      Object toSet = "T";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD2280")) // Check for duplicate password control not valid.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPWDRQDDIF with a good parameter.
    **/
    public void Var181()
    {
      testGetSet("QPWDRQDDIF", "8");
    }

    /**
    Test QPWDVLDPGM with a bad parameter.
    **/
    public void Var182()
    {
//      testBadParm("QPWDVLDPGM");
      String name = "QPWDVLDPGM";
      Object obj = null;
      Object toSet = "VALIDPGM PGMLIB"; // bad parms
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QPWDVLDPGM with a good parameter.
    **/
    public void Var183()
    {
      testGetSet("QPWDVLDPGM", "*NONE");
    }

    /**
    Test QPWDVLDPGM with a good parameter.
    **/
    public void Var184()
    {                         //01234567890123456789
      testGetSet("QPWDVLDPGM", "D510W3C   QSYS");
    }

    /**
    Test QPWRDWNLMT with a bad parameter.
    **/
    public void Var185()
    {
      testBadParm("QPWRDWNLMT");
    }

    /**
    Test QPWRDWNLMT with a good parameter.
    **/
    public void Var186()
    {
      testGetSet("QPWRDWNLMT", new Integer(1000));
    }

    /**
    Test QPWRRSTIPL with a bad parameter.
    **/
    public void Var187()
    {
      testBadParm("QPWRRSTIPL");
    }

    /**
    Test QPWRRSTIPL with a good parameter.
    **/
    public void Var188()
    {
      if (vrm <= 0x00050400 && vrm >= 0x00050300) {
        testGetSet("QPWRRSTIPL", "1", "CPF1221");
        // Tolerate CPF1221, "Service processor failed".
        // This error happens only on V5R3 and V5R4.
        // See IBM Software Technical Document number 404684428.
      }
      else {
        testGetSet("QPWRRSTIPL", "1", "CPF18B4");
        // Tolerate CPF18B4, "System value cannot be changed in a secondary partition"
      }
    }

    /**
    Test QQRYDEGREE with a bad parameter.
    **/
    public void Var189()
    {
      testBadParm("QQRYDEGREE");
    }

    /**
    Test QQRYDEGREE with a good parameter.
    **/
    public void Var190()
    {
      testGetSet("QQRYDEGREE", "*MAX");
    }

    /**
    Test QQRYTIMLMT with a bad parameter.
    **/
    public void Var191()
    {
      testBadParm("QQRYTIMLMT");
    }

    /**
    Test QQRYTIMLMT with a good parameter.
    **/
    public void Var192()
    {                         //0123456789
      testGetSet("QQRYTIMLMT", "0000010000");
    }

    /**
    Test QRCLSPLSTG with a bad parameter.
    **/
    public void Var193()
    {
      testBadParm("QRCLSPLSTG");
    }

    /**
    Test QRCLSPLSTG with a good parameter.
    **/
    public void Var194()
    {
      testGetSet("QRCLSPLSTG", "*NOMAX");
    }

    /**
    Test QRETSVRSEC with a bad parameter.
    **/
    public void Var195()
    {
      testBadParm("QRETSVRSEC");
    }

    /**
    Test QRETSVRSEC with a good parameter.
    **/
    public void Var196()
    {
      testGetSet("QRETSVRSEC", "1");
    }

    /**
    Test QRMTIPL with a bad parameter.
    **/
    public void Var197()
    {
      testBadParm("QRMTIPL");
    }

    /**
    Test QRMTIPL with a good parameter.
    **/
    public void Var198()
    {
      if (vrm <= 0x00050400 && vrm >= 0x00050300) {
        testGetSet("QRMTIPL", "1", "CPF1221");
        // Tolerate CPF1221, "Service processor failed".
        // This error happens only on V5R3 and V5R4.
        // See IBM Software Technical Document number 404684428.
      }
      else {
        testGetSet("QRMTIPL", "1", "CPF18B4");
        // Tolerate CPF18B4, "System value cannot be changed in a secondary partition"
      }
    }

    /**
    Test QRMTSIGN with a bad parameter.
    **/
    public void Var199()
    {
//      testBadParm("QRMTSIGN");
      String name = "QRMTSIGN";
      Object obj = null;
      Object toSet = "VALIDPGM VALIDLIB"; // bad parms
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QRMTSIGN with a good parameter.
    **/
    public void Var200()
    {
      testGetSet("QRMTSIGN", "*VERIFY");
    }

    /**
    Test QRMTSRVATR with a bad parameter.
    **/
    public void Var201()
    {
      testBadParm("QRMTSRVATR");
    }

    /**
    Test QRMTSRVATR with a good parameter.
    **/
    public void Var202()
    {
      testGetSet("QRMTSRVATR", "1");
    }

    /**
    Test QSCPFCONS with a bad parameter.
    **/
    public void Var203()
    {
      testBadParm("QSCPFCONS");
    }

    /**
    Test QSCPFCONS with a good parameter.
    **/
    public void Var204()
    {
      testGetSet("QSCPFCONS", "0");
    }

    /**
    Test QSECOND with a bad parameter.
    **/
    public void Var205()
    {
      testBadParm("QSECOND");
    }

    /**
    Test QSECOND with a good parameter.
    **/
    public void Var206()
    {
//      testGetSet("QSECOND", "30");
      String name = "QSECOND";
      Object toSet = "30";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          // The seconds should be close... within 8 seconds.
          int retSec = (new Integer((String)obj2)).intValue();
          if ((retSec-30) > 8)
          {
            failMsg += " Unexpected data returned: '"+(String)obj2+"'";
            failMsg += "\n Value should be between 30 and 38 seconds.";
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QSECURITY with a bad parameter.
    **/
    public void Var207()
    {
//      testBadParm("QSECURITY");
      String name = "QSECURITY";
      Object obj = null;
      Object toSet = "TR";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD2271")) // Check for security active value not valid.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QSECURITY with a good parameter.
    **/
    public void Var208()
    {
      testGetSet("QSECURITY", "30");
    }

    /**
    Test QSETJOBATR with a bad parameter.
    **/
    public void Var209()
    {
      testBadParm("QSETJOBATR");
    }

    /**
    Test QSETJOBATR with a good parameter.
    **/
    public void Var210()
    {
      testGetSet("QSETJOBATR", new String[] { "*CCSID", "*DATFMT", "*DECFMT" });
    }

    /**
    Test QSFWERRLOG with a bad parameter.
    **/
    public void Var211()
    {
      testBadParm("QSFWERRLOG");
    }

    /**
    Test QSFWERRLOG with a good parameter.
    **/
    public void Var212()
    {
      testGetSet("QSFWERRLOG", "*NOLOG");
    }

    /**
    Test QSPCENV with a bad parameter.
    **/
    public void Var213()
    {
      testBadParm("QSPCENV");
    }

    /**
    Test QSPCENV with a good parameter.
    **/
    public void Var214()
    {
      testGetSet("QSPCENV", "*S36");
    }

    /**
    Test QSRLNBR with a good parameter.
    **/
    public void Var215()
    {
      testGetSet("QSRLNBR", "12345678");
    }

    /**
    Test QSRTSEQ with a bad parameter.
    **/
    public void Var216()
    {
//      testBadParm("QSRTSEQ");
      String name = "QSRTSEQ";
      Object obj = null;
      Object toSet = "VALIDNAME VALIDLIB"; // bad parms
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for not found
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QSRTSEQ with a good parameter.
    **/
    public void Var217()
    {
      testGetSet("QSRTSEQ", "*LANGIDSHR");
    }

    /**
    Test QSTGLOWACN with a bad parameter.
    **/
    public void Var218()
    {
      testBadParm("QSTGLOWACN");
    }

    /**
    Test QSTGLOWACN with a good parameter.
    **/
    public void Var219()
    {
      testGetSet("QSTGLOWACN", "*CRITMSG");
    }

    /**
    Test QSTGLOWLMT with a bad parameter.
    **/
    public void Var220()
    {
      testBadParm("QSTGLOWLMT");
    }

    /**
    Test QSTGLOWLMT with a good parameter.
    **/
    public void Var221()
    {
      testGetSet("QSTGLOWLMT", (new BigDecimal(6.1122)).setScale(4, BigDecimal.ROUND_HALF_UP)); // is a DECIMAL(7 4)
    }

    /**
    Test QSTRPRTWTR with a good parameter.
    **/
    public void Var222()
    {
      testGetSet("QSTRPRTWTR", "0");
    }

    /**
    Test QSTRUPPGM with a bad parameter.
    **/
    public void Var223()
    {
//      testBadParm("QSTRUPPGM");
      String name = "QSTRUPPGM";
      Object obj = null;
      Object toSet = "VALIDPGM VALIDLIB"; // bad parms
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for not found
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QSTRUPPGM with a good parameter.
    **/
    public void Var224()
    {
      testGetSet("QSTRUPPGM", "*NONE");
    }

    /**
    Test QSTRUPPGM with a good parameter.
    **/
    public void Var225()
    {                       // 01234567890123456789
      testGetSet("QSTRUPPGM", "QSTRUP    QSYS");
    }

    /**
    Test QSRVDMP with a bad parameter.
    **/
    public void Var226()
    {
      testBadParm("QSRVDMP");
    }

    /**
    Test QSRVDMP with a good parameter.
    **/
    public void Var227()
    {
      testGetSet("QSRVDMP", "*DMPALLJOB");
    }

    /**
    Test QSTSMSG with a bad parameter.
    **/
    public void Var228()
    {
      testBadParm("QSTSMSG");
    }

    /**
    Test QSTSMSG with a good parameter.
    **/
    public void Var229()
    {
      testGetSet("QSTSMSG", "*NONE");
    }

    /**
    Test QSVRAUTITV with a bad parameter.
    **/
    public void Var230()
    {
      testBadParm("QSVRAUTITV");
    }

    /**
    Test QSVRAUTITV with a good parameter.
    **/
    public void Var231()
    {
      testGetSet("QSVRAUTITV", new Integer(3600));
    }

    /**
    Test QSYSLIBL with a bad parameter.
    **/
    public void Var232()
    {
//      testBadParm("QSYSLIBL");
      String name = "QSYSLIBL";
      Object obj = null;
      Object toSet = new String[] { "TRASH" };
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1812")) // Check for object not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QSYSLIBL with a good parameter.
    **/
    public void Var233()
    {
      testGetSet("QSYSLIBL", new String[] { "QSYS", "QUSRSYS" });
    }

    /**
    Test QTIME with a bad parameter.
    **/
    public void Var234()
    {
//      testBadParm("QTIME");
      String name = "QTIME";
      Object obj = null;
//      Object toSet = new java.sql.Date(25,60,60);
      Object toSet = "*TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
/*        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("QYEAR")) // You can't pass in an invalid Date
                                                              // without our code intercepting it before
                                                              // it gets to the 400.
*/
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QTIME with a good parameter.
    **/
    public void Var235()
    {
//      testGetSet("QTIME", Calendar.getInstance().getTime());
      String name = "QTIME";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        Object toSet = Calendar.getInstance().getTime();
        Date curTime = Calendar.getInstance().getTime();
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          Calendar cal1 = Calendar.getInstance();
          Calendar cal2 = Calendar.getInstance();
          cal1.clear();
          cal2.clear();
          Date d1 = (Date)obj2;
          Date d2 = (Date)curTime;
          cal1.setTime(d1);
          cal2.setTime(d2);
          if (cal1.get(Calendar.HOUR) != cal2.get(Calendar.HOUR))
          {
            failMsg += "\n Hours not equal: "+cal1.get(Calendar.HOUR)+" != "+cal2.get(Calendar.HOUR);
          }
          if (cal1.get(Calendar.MINUTE) != cal2.get(Calendar.MINUTE))
          {
            failMsg += "\n Minutes not equal: "+cal1.get(Calendar.MINUTE)+" != "+cal2.get(Calendar.MINUTE);
          }
          // Seconds should be within 5 seconds of each other...
          if ((cal1.get(Calendar.SECOND)-cal2.get(Calendar.SECOND)) > 5)
          {
            failMsg += "\n Seconds not close: "+cal1.get(Calendar.SECOND)+" != "+cal2.get(Calendar.SECOND);
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+f.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
        failMsg += " "+e.getMessage();
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test QTIMSEP with a bad parameter.
    **/
    public void Var236()
    {
      testBadParm("QTIMSEP");
    }

    /**
    Test QTIMSEP with a good parameter.
    **/
    public void Var237()
    {
      testGetSet("QTIMSEP", ",");
    }

    /**
    Test QTOTJOB with a bad parameter.
    **/
    public void Var238()
    {
      testBadParm("QTOTJOB");
    }

    /**
    Test QTOTJOB with a good parameter.
    **/
    public void Var239()
    {
      testGetSet("QTOTJOB", new Integer(100));
    }

    /**
    Test QTSEPOOL with a bad parameter.
    **/
    public void Var240()
    {
      testBadParm("QTSEPOOL");
    }

    /**
    Test QTSEPOOL with a good parameter.
    **/
    public void Var241()
    {
      testGetSet("QTSEPOOL", "*BASE");
    }

    /**
    Test QUPSDLYTIM with a bad parameter.
    **/
    public void Var242()
    {
      testBadParm("QUPSDLYTIM");
    }

    /**
    Test QUPSDLYTIM with a good parameter.
    **/
    public void Var243()
    {                                     //   0123456789    0123456789
      testGetSet("QUPSDLYTIM", new String[] { "0000000120", "0000000120" }, "CPF18B4"); // 2nd parm is ignored on the set, but is used here for comparison on the retrieve.
      // Tolerate CPF18B4, "System value cannot be changed in a secondary partition"
    }

    /**
    Test QUPSMSGQ with a bad parameter.
    **/
    public void Var244()
    {
//      testBadParm("QUPSMSGQ");
      String name = "QUPSMSGQ";
      Object obj = null;
      Object toSet = "VALIDQ VALIDLIB"; // bad parms
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // Check for not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QUPSMSGQ with a good parameter.
    **/
    public void Var245()
    {                       //01234567890123456789
      testGetSet("QUPSMSGQ", "QSYSOPR   QSYS");
    }

    /**
    Test QUSEADPAUT with a bad parameter.
    **/
    public void Var246()
    {
//      testBadParm("QUSEADPAUT");
      String name = "QUSEADPAUT";
      Object obj = null;
      Object toSet = "TRASHAUTL"; // bad parm
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") && (e.getMessage().toUpperCase().startsWith("CPF1813") // Check for not found.
            || e.getMessage().toUpperCase().startsWith("CPF1078")))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QUSEADPAUT with a good parameter.
    **/
    public void Var247()
    {
      testGetSet("QUSEADPAUT", "*NONE");
    }

    /**
    Test QUSRLIBL with a bad parameter.
    **/
    public void Var248()
    {
//      testBadParm("QUSRLIBL");
      String name = "QUSRLIBL";
      Object obj = null;
      Object toSet = new String[] { "TRASH" };
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1812")) // Check for object not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QUSRLIBL with a good parameter.
    **/
    public void Var249()
    {
      CommandCall cmd = new CommandCall(pwrSys_);
      try { cmd.run("QSYS/CRTLIB SVT249"); } catch(Exception e) {}
      testGetSet("QUSRLIBL", new String[] { "SVT249", "QTEMP" });
      try { deleteLibrary(cmd, "SVT249"); } catch(Exception e) {}
    }

    /**
    Test QUTCOFFSET with a bad parameter.
    **/
    public void Var250()
    {
        if (vrm >= 0x00050300)
        {
            notApplicable();
        }
        else
        {
            testBadParm("QUTCOFFSET");
        }
    }

    /**
    Test QUTCOFFSET with a good parameter.
    **/
    public void Var251()
    {
        if (vrm >= 0x00050300)
        {
            notApplicable();
        }
        else
        {
            testGetSet("QUTCOFFSET", "-0500");
        }
    }

    /**
    Test QYEAR with a bad parameter.
    **/
    public void Var252()
    {
      testBadParm("QYEAR");  
    }

    /**
    Test QYEAR with a good parameter.
    **/
    public void Var253()
    {     
      testGetSet("QYEAR", "07");
    }

  //------------------------
  // V4R2 Network attributes
  //------------------------

    /**
    Test ALRBCKFP with a bad parameter.
    **/
    public void Var254()
    {
//      testBadParm("ALRBCKFP");
      String name = "ALRBCKFP";
      Object obj = null;
      Object toSet = new String[] { "TRASHTRASH", "TRASH" }; // 1st parm too long
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }

        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for value exceeds X characters...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test ALRBCKFP with a good parameter.
    **/
    public void Var255()
    {
      testGetSet("ALRBCKFP", new String[] {"*NONE"});
    }

    /**
    Test ALRCTLD with a bad parameter.
    **/
    public void Var256()
    {
//      testBadParm("ALRCTLD");
      String name = "ALRCTLD";
      Object obj = null;
      Object toSet = "TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0905")) // Check for alert controller description not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test ALRCTLD with a good parameter.
    **/
    public void Var257()
    {
      testGetSet("ALRCTLD", "*NONE");
    }

    /**
    Test ALRDFTFP with a bad parameter.
    **/
    public void Var258()
    {
      testBadParm("ALRDFTFP");
    }

    /**
    Test ALRDFTFP with a good parameter.
    **/
    public void Var259()
    {
      testGetSet("ALRDFTFP", "*NO");
    }

    /**
    Test ALRFTR with a bad parameter.
    **/
    public void Var260()
    {
//      testBadParm("ALRFTR");
      String name = "ALRFTR";
      Object obj = null;
      Object toSet = "TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF9801")) // Check for object not found.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test ALRFTR with a good parameter.
    **/
    public void Var261()
    {
      testGetSet("ALRFTR", "*NONE");
    }

    /**
    Test ALRHLDCNT with a bad parameter.
    **/
    public void Var262()
    {
//      testBadParm("ALRHLDCNT");
      String name = "ALRHLDCNT";
      Object obj = null;
      Object toSet = new Integer("-3");
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test ALRHLDCNT with a good parameter.
    **/
    public void Var263()
    {
//      testGetSet("ALRHLDCNT", "*NOMAX");
      String name = "ALRHLDCNT";
      Object toSet = "*NOMAX";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((Integer)obj2).intValue() != -2) // -2 means *NOMAX
          {
            failMsg += " Unexpected data returned: "+((Integer)obj2).intValue();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value.";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test ALRLOGSTS with a bad parameter.
    **/
    public void Var264()
    {
      testBadParm("ALRLOGSTS");
    }

    /**
    Test ALRLOGSTS with a good parameter.
    **/
    public void Var265()
    {
      testGetSet("ALRLOGSTS", "*ALL");
    }

    /**
    Test ALRPRIFP with a bad parameter.
    **/
    public void Var266()
    {
      testBadParm("ALRPRIFP");
    }

    /**
    Test ALRPRIFP with a good parameter.
    **/
    public void Var267()
    {
      testGetSet("ALRPRIFP", "*YES");
    }

    /**
    Test ALRRQSFP with a bad parameter.
    **/
    public void Var268()
    {
//      testBadParm("ALRRQSFP");
      String name = "ALRRQSFP";
      Object obj = null;
      Object toSet = new String[] { "TRASHTRASH", "TRASH" };
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test ALRRQSFP with a good parameter.
    **/
    public void Var269()
    {
      testGetSet("ALRRQSFP", new String[] { "*NONE" });
    }

    /**
    Test ALRSTS with a bad parameter.
    **/
    public void Var270()
    {
      testBadParm("ALRSTS");
    }

    /**
    Test ALRSTS with a good parameter.
    **/
    public void Var271()
    {
      testGetSet("ALRSTS", "*UNATTEND");
    }

    /**
    Test ALWANYNET with a bad parameter.
    **/
    public void Var272()
    {
      testBadParm("ALWANYNET");
    }

    /**
    Test ALWANYNET with a good parameter.
    **/
    public void Var273()
    {
      testGetSet("ALWANYNET", "*YES");
    }

    /**
    Test ALWHPRTWR with a bad parameter.
    **/
    public void Var274()
    {
      testBadParm("ALWHPRTWR");
    }

    /**
    Test ALWHPRTWR with a good parameter.
    **/
    public void Var275()
    {
      testGetSet("ALWHPRTWR", "*NO");
    }

    /**
    Test ALWVRTAPPN with a bad parameter.
    **/
    public void Var276()
    {
      testBadParm("ALWVRTAPPN");
    }

    /**
    Test ALWVRTAPPN with a good parameter.
    **/
    public void Var277()
    {
      testGetSet("ALWVRTAPPN", "*NO");
    }

    /**
    Test VRTAUTODEV with a bad parameter.
    **/
    public void Var278()
    {
//      testBadParm("VRTAUTODEV");
      String name = "VRTAUTODEV";
      Object obj = null;
      Object toSet = new Integer("-3");
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include ...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test VRTAUTODEV with a good parameter.
    **/
    public void Var279()
    {
      testGetSet("VRTAUTODEV", new Integer(100));
    }

    /**
    Test DDMACC with a bad parameter.
    **/
    public void Var280()
    {
//      testBadParm("DDMACC");
      String name = "DDMACC";
      Object obj = null;
      Object toSet = "TRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            (e.getMessage().toUpperCase().startsWith("CPF9801") // Check for object not found.
             || e.getMessage().toUpperCase().startsWith("CPD1680")))
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test DDMACC with a good parameter.
    **/
    public void Var281()
    {
      testGetSet("DDMACC", "*OBJAUT");
    }

    /**
    Test DFTCNNLST with a bad parameter.
    **/
    public void Var282()
    {
//      testBadParm("DFTCNNLST");
      String name = "DFTCNNLST";
      Object obj = null;
      Object toSet = "TRASHTRASHT"; // too long
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test DFTCNNLST with a good parameter.
    **/
    public void Var283()
    {
      testGetSet("DFTCNNLST", "QDCCNNLANY");
    }

    /**
    Test DFTMODE with a bad parameter.
    **/
    public void Var284()
    {
//      testBadParm("DFTMODE");
      String name = "DFTMODE";
      Object obj = null;
      Object toSet = "TRASHTRASH";
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test DFTMODE with a good parameter.
    **/
    public void Var285()
    {
      testGetSet("DFTMODE", "BLANK");
    }

    /**
    Test DFTNETTYPE with a bad parameter.
    **/
    public void Var286()
    {
      testBadParm("DFTNETTYPE");
    }

    /**
    Test DFTNETTYPE with a good parameter.
    **/
    public void Var287()
    {
      testGetSet("DFTNETTYPE", "*ETSI");
    }

    /**
    Test DTACPR with a bad parameter.
    **/
    public void Var288()
    {
//      testBadParm("DTACPR");
      String name = "DTACPR";
      Object obj = null;
      Object toSet = new Integer("-3");
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test DTACPR with a good parameter.
    **/
    public void Var289()
    {
//      testGetSet("DTACPR", "*ALLOW");
      String name = "DTACPR";
      Object toSet = "*ALLOW";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((Integer)obj2).intValue() != -2) // -2 is *ALLOW
          {
            failMsg += " Unexpected data returned: "+((Integer)obj2).intValue();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
        {
          int i = ((Integer)obj).intValue();
          switch(i)
          {
            case 0:
              obj = "*NONE";
              break;
            case -1:
              obj = "*REQUEST";
              break;
            case -2:
              obj = "*ALLOW";
              break;
            case -3:
              obj = "*REQUIRE";
              break;
            default:
              break;
          }
          s.setValue(obj);
        }
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test DTACPR with a good parameter.
    **/
    public void Var290()
    {
      String name = "DTACPR";
      Object toSet = new Integer(100);
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (s.getType() != SystemValueList.TYPE_ARRAY)
          {
            if (s.getType() == SystemValueList.TYPE_STRING &&
                !((String)obj2).trim().equals(((String)toSet).trim()))
            {
              failMsg += " Real strings not equal: '"+((String)obj2).trim()+"' != '"+((String)toSet).trim()+"'.";
            }
            else if(s.getType() != SystemValueList.TYPE_STRING && !obj2.equals(toSet))
            {
              failMsg += " Real objects not equal: '"+obj2.toString().trim()+"' != '"+toSet.toString().trim()+"'.";
            }
          }
          else
          {
            String[] arr1 = (String[])toSet;
            String[] arr2 = (String[])obj2;
            for (int x=0; x<(arr1.length < arr2.length ? arr1.length : arr2.length); ++x)
            {
                if (!arr1[x].trim().equals(arr2[x].trim()))
                {
                  failMsg += "\n Real objects at ["+x+"] not equal: "+arr1[x]+" != "+arr2[x];
                }
            }
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
        {
          int i = ((Integer)obj).intValue();
          switch(i)
          {
            case 0:
              obj = "*NONE";
              break;
            case -1:
              obj = "*REQUEST";
              break;
            case -2:
              obj = "*ALLOW";
              break;
            case -3:
              obj = "*REQUIRE";
              break;
            default:
              break;
          }
          s.setValue(obj);
        }
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
        failMsg += "\n "+e.getMessage();
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test DTACPRINM with a bad parameter.
    **/
    public void Var291()
    {
//      testBadParm("DTACPRINM");
      String name = "DTACPRINM";
      Object obj = null;
      Object toSet = new Integer("-3");
      String failMsg = "";
      int type;
      try
      {
        SystemValue s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test DTACPRINM with a good parameter.
    **/
    public void Var292()
    {
//      testGetSet("DTACPRINM", "*REQUEST");
      String name = "DTACPRINM";
      Object toSet = "*REQUEST";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (((Integer)obj2).intValue() != -1) // -1 is *REQUEST
          {
            failMsg += " Unexpected data returned: "+((Integer)obj2).intValue();
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        Object temp = obj;
        try
        {
          if (((Integer)obj).intValue() == 0)
            temp = "*NONE";
          if (((Integer)obj).intValue() == -1)
            temp = "*REQUEST";
        }
        catch(ClassCastException cce) {}
        if (!s.isReadOnly())
          s.setValue(temp);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test DTACPRINM with a good parameter.
    **/
    public void Var293()
    {
//      testGetSet("DTACPRINM", new Integer(200));
      String name = "DTACPRINM";
      Object toSet = new Integer(200);
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (s.getType() != SystemValueList.TYPE_ARRAY)
          {
            if (s.getType() == SystemValueList.TYPE_STRING &&
                !((String)obj2).trim().equals(((String)toSet).trim()))
            {
              failMsg += " Real strings not equal: '"+((String)obj2).trim()+"' != '"+((String)toSet).trim()+"'.";
            }
            else if(s.getType() != SystemValueList.TYPE_STRING && !obj2.equals(toSet))
            {
              failMsg += " Real objects not equal: '"+obj2.toString().trim()+"' != '"+toSet.toString().trim()+"'.";
            }
          }
          else
          {
            String[] arr1 = (String[])toSet;
            String[] arr2 = (String[])obj2;
            for (int x=0; x<(arr1.length < arr2.length ? arr1.length : arr2.length); ++x)
            {
                if (!arr1[x].trim().equals(arr2[x].trim()))
                {
                  failMsg += "\n Real objects at ["+x+"] not equal: "+arr1[x]+" != "+arr2[x];
                }
            }
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        Object temp = obj;
        try
        {
          if (((Integer)obj).intValue() == 0)
          {
            temp = "*NONE";
          }
          if (((Integer)obj).intValue() == -1)
          {
            temp = "*REQUEST";
          }
        }
        catch(ClassCastException cce) {}
        if (!s.isReadOnly())
          s.setValue(temp);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test HPRPTHTMR with a bad parameter.
    **/
    public void Var294()
    {
//      testBadParm("HPRPTHTMR");
      String name = "HPRPTHTMR";
      Object obj = null;
      Object toSet = new String[] { "TRASH" };
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0076")) // Check for must be numeric.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test HPRPTHTMR with a good parameter.
    **/
    public void Var295()
    {
      testGetSet("HPRPTHTMR", new String[] { "*NONE", "*NONE", "*NONE", "*NONE" });
    }

    /**
    Test HPRPTHTMR with a good parameter.
    **/
    public void Var296()
    {
      testGetSet("HPRPTHTMR", new String[] { "1", "20", "300", "4000" });
    }

    /**
    Test JOBACN with a bad parameter.
    **/
    public void Var297()
    {
      testBadParm("JOBACN");
    }

    /**
    Test JOBACN with a good parameter.
    **/
    public void Var298()
    {
      testGetSet("JOBACN", "*SEARCH");
    }

    /**
    Test LCLCPNAME with a bad parameter.
    **/
    public void Var299()
    {
//      testBadParm("LCLCPNAME");
      String name = "LCLCPNAME";
      Object obj = null;
      Object toSet = "11111";
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0134")) // Value not allowed
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0916")) // Check for changes not allowed while varied on.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test LCLCPNAME with a good parameter.
    **/
    public void Var300()
    {
      if (pwrSys_ == null)
      {
        failed("PwrSys not specified. Use -misc parameter.");
        return;
      }
      String sysname = pwrSys_.getSystemName().toUpperCase();
      if (sysname.trim().equals("LOCALHOST")) // In case we are running natively
      {
        try
        {
          sysname = InetAddress.getLocalHost().getHostName().toUpperCase().trim();
          sysname = sysname.substring(0, sysname.indexOf("."));
        }
        catch(UnknownHostException e)
        {
          failed(e, "Unable to retrieve real host name.");
          return;
        }
      }

      testGetSet("LCLCPNAME", sysname);
    }

    /**
    Test LCLLOCNAME with a bad parameter.
    **/
    public void Var301()
    {
//      testBadParm("LCLLOCNAME");
      String name = "LCLLOCNAME";
      Object obj = null;
      Object toSet = "TRASHTRASH"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // too long
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test LCLLOCNAME with a good parameter.
    **/
    public void Var302()
    {
      if (pwrSys_ == null)
      {
        failed("PwrSys not specified. Use -misc parameter.");
        return;
      }
      String sysname = pwrSys_.getSystemName().toUpperCase();
      if (sysname.trim().equals("LOCALHOST")) // In case we are running natively
      {
        try
        {
          sysname = InetAddress.getLocalHost().getHostName().toUpperCase().trim();
          sysname = sysname.substring(0, sysname.indexOf("."));
        }
        catch(UnknownHostException e)
        {
          failed(e, "Unable to retrieve real host name.");
          return;
        }
      }

      testGetSet("LCLLOCNAME", sysname);
    }

    /**
    Test LCLNETID with a bad parameter.
    **/
    public void Var303()
    {
//      testBadParm("LCLNETID");
      String name = "LCLNETID";
      Object obj = null;
      Object toSet = "11111";
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPD0134")) // Value not allowed
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0916")) // Check for changes not allowed while varied on.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test LCLNETID with a good parameter.
    **/
    public void Var304()
    {
      testGetSet("LCLNETID", "APPN");
    }

    /**
    Test MAXINTSSN with a bad parameter.
    **/
    public void Var305()
    {
//      testBadParm("MAXINTSSN");
      String name = "MAXINTSSN";
      Object obj = null;
      Object toSet = new Integer(-5);
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test MAXINTSSN with a good parameter.
    **/
    public void Var306()
    {
      testGetSet("MAXINTSSN", new Integer(100));
    }

    /**
    Test MAXHOP with a bad parameter.
    **/
    public void Var307()
    {
//      testBadParm("MAXHOP");
      String name = "MAXHOP";
      Object obj = null;
      Object toSet = new Integer(-5);
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test MAXHOP with a good parameter.
    **/
    public void Var308()
    {
      testGetSet("MAXHOP", new Integer(200));
    }

    /**
    Test MSGQ with a bad parameter.
    **/
    public void Var309()
    {
//      testBadParm("MSGQ");
      String name = "MSGQ";
      Object obj = null;
      Object toSet = "TRASHTRASHT/TRASH"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          obj = "QSYS/QSYSOPR";
          s.setValue(obj);
          if (((String)s.getValue()).indexOf("QSYS") == -1)
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+": '"+obj.toString()+"'";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test MSGQ with a good parameter.
    **/
    public void Var310()
    {                  // 01234567890123456789
//      testGetSet("MSGQ", "QSYS/QSYSOPR");
      String name = "MSGQ";
      Object toSet = "QSYS/QSYSOPR";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (!((String)obj2).trim().equals("QSYSOPR   QSYS"))
          {
            failMsg += " Unexpected data returned: '"+(String)obj2+"'";
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!((String)obj).equals("QSYSOPR   QSYS      "))
        {
          output_.println("Please do a CHGNETA MSGQ to reset it to be: '"+(String)obj+"'");
        }
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test NETSERVER with a bad parameter.
    **/
    public void Var311()
    {
//      testBadParm("NETSERVER");
      String name = "NETSERVER";
      Object obj = null;
      Object toSet = new String[] { "(TRASHTRAS TRASH2)" }; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          obj = "(*LCLNETID *ANY)";
          s.setValue(obj);
          if (!((String)s.getValue()).equals("*LCLNETID*ANY    "))
          {
            failMsg += " Unable to set "+name+" to original value: '"+(String)s.getValue()+"'";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test NETSERVER with a good parameter.
    **/
    public void Var312()
    {
//      testGetSet("NETSERVER", new String[] { "(*LCLNETID *ANY)", "(*LCLNETID *ANY)" });
      String name = "NETSERVER";
      Object toSet = new String[] { "(*LCLNETID *ANY)", "(*LCLNETID *ANY)" };
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          String[] arr1 = new String[] { "*LCLNETID*ANY    ", "*LCLNETID*ANY    " };
          String[] arr2 = (String[])obj2;
          for (int x=0; x<(arr1.length < arr2.length ? arr1.length : arr2.length); ++x)
          {
              if (!arr1[x].trim().equals(arr2[x].trim()))
              {
                failMsg += "\n Real objects at ["+x+"] not equal: "+arr1[x]+" != "+arr2[x];
              }
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test NODETYPE with a bad parameter.
    **/
    public void Var313()
    {
      testBadParm("NODETYPE");
    }

    /**
    Test NODETYPE with a good parameter.
    **/
    public void Var314()
    {
      testGetSet("NODETYPE", "*ENDNODE");
    }

    /**
    Test NWSDOMAIN with a bad parameter.
    **/
    public void Var315()
    {
//      testBadParm("NWSDOMAIN");
      String name = "NWSDOMAIN";
      Object obj = null;
      Object toSet = "TRASHTRAS"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test NWSDOMAIN with a good parameter.
    **/
    public void Var316()
    {
      if (pwrSys_ == null)
      {
        failed("PwrSys not specified. Use -misc parameter.");
        return;
      }
//      testGetSet("NWSDOMAIN", "*SYSNAME");
      String name = "NWSDOMAIN";
      Object toSet = "*SYSNAME";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          String sysname = pwrSys_.getSystemName().toUpperCase();
          if (sysname.trim().equals("LOCALHOST")) // In case we are running natively
          {
            sysname = InetAddress.getLocalHost().getHostName().toUpperCase().trim();
            sysname = sysname.substring(0, sysname.indexOf("."));
          }
          if (!((String)obj2).trim().toUpperCase().equals(sysname))
          {
            failMsg += " Unexpected data returned: '"+((String)obj2).trim().toUpperCase()+"' != '"+pwrSys_.getSystemName().trim().toUpperCase()+"'";
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!s.isReadOnly())
          s.setValue(obj);
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test OUTQ with a bad parameter.
    **/
    public void Var317()
    {
//      testBadParm("OUTQ");
      String name = "OUTQ";
      Object obj = null;
      Object toSet = "TRASHTRASHT/TRASH"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          obj = "QSYS/QSYSOPR";
          s.setValue(obj);
          if (((String)s.getValue()).indexOf("QSYS") == -1)
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+": '"+obj.toString()+"'";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // Check for too long.
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test OUTQ with a good parameter.
    **/
    public void Var318()
    {
//      testGetSet("OUTQ", );
      String name = "OUTQ";
      Object toSet = "QGPL/QPRINT";
      String failMsg = "";
      Object obj = null;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable();
          return;
        }
        obj = s.getValue();
        if (!s.isReadOnly())
        {
          s.setValue(toSet);
          if (!s.getValue().equals(toSet))
          {
            failMsg += "Cached objects not equal.";
          }
          s.clear();
          Object obj2 = s.getValue();
          if (!((String)obj2).trim().equals("QPRINT    QGPL"))
          {
            failMsg += " Unexpected data returned: '"+(String)obj2+"'";
          }
        }
        else
        {
          SystemValue t = new SystemValue(pwrSys_, name);
          Object obj2 = t.getValue();
          if (!obj2.equals(obj))
          {
            failMsg += "Read-only object: Objects not equal.";
          }
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception occurred.";
        try
        {
          s.setValue(obj);
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failMsg += " "+e.getMessage();
        }
        failed(e, failMsg);
        return;
      }
      try
      {
        if (!((String)obj).equals("QPRINT    QGPL      "))
        {
          output_.println("Please do a CHGNETA OUTQ to reset it to be: '"+(String)obj+"'");
        }
      }
      catch(Exception e)
      {
        failMsg += " Unable to set "+name+" to original value: '"+obj.toString()+"'";
      }
      if (failMsg.length() > 0)
      {
        failed(failMsg);
      }
      else
      {
        succeeded();
      }
    }

    /**
    Test PNDSYSNAME with a good parameter.
    **/
    public void Var319()
    {
      testGetSet("PNDSYSNAME", pwrSys_.getSystemName().toUpperCase());
    }

    /**
    Test PCSACC with a bad parameter.
    **/
    public void Var320()
    {
//      testBadParm("PCSACC");
      String name = "PCSACC";
      Object obj = null;
      Object toSet = "TRASHTRASHT/TRASH"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // value not allowed
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test PCSACC with a good parameter.
    **/
    public void Var321()
    {
      testGetSet("PCSACC", "*OBJAUT");
    }

    /**
    Test RAR with a bad parameter.
    **/
    public void Var322()
    {
//      testBadParm("RAR");
      String name = "RAR";
      Object obj = null;
      Object toSet = new Integer(-5);
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0085")) // Check for range does not include...
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test RAR with a good parameter.
    **/
    public void Var323()
    {
      testGetSet("RAR", new Integer(100));
    }

    /**
    Test SYSNAME with a bad parameter.
    **/
    public void Var324()
    {
//      testBadParm("SYSNAME");
      String name = "SYSNAME";
      Object obj = null;
      Object toSet = "TRASHTRAS"; // too long
      String failMsg = "";
      int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease() || s.isReadOnly())
        {
          notApplicable();
          return;
        }
        type = s.getType();
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur."+type;
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0074")) // too long
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test SYSNAME with a good parameter.
    **/
    public void Var325()
    {
      if (pwrSys_ == null)
      {
        failed("PwrSys not specified. Use -misc parameter.");
        return;
      }
      String sysname = pwrSys_.getSystemName().toUpperCase();
      if (sysname.trim().equals("LOCALHOST")) // In case we are running natively
      {
        try
        {
          sysname = InetAddress.getLocalHost().getHostName().toUpperCase().trim();
          sysname = sysname.substring(0, sysname.indexOf("."));
        }
        catch(UnknownHostException e)
        {
          failed(e, "Unable to retrieve real host name.");
          return;
        }
      }
      testGetSet("SYSNAME", sysname);
    }

  //-------------------
  // V4R3 system values
  //-------------------
    /**
    Test QCHRIDCTL with a bad parameter.
    **/
    public void Var326()
    {
      testBadParm("QCHRIDCTL");
    }

    /**
    Test QCHRIDCTL with a good parameter.
    **/
    public void Var327()
    {
      testGetSet("QCHRIDCTL", "*DEVD");
    }

    /**
    Test QDYNPTYADJ with a bad parameter.
    **/
    public void Var328()
    {
      testBadParm("QDYNPTYADJ");
    }

    /**
    Test QDYNPTYADJ with a good parameter.
    **/
    public void Var329()
    {
      testGetSet("QDYNPTYADJ", "0");
    }

    /**
    Test QIGCFNTSIZ with a bad parameter.
    **/
    public void Var330()
    {
      testBadParm("QIGCFNTSIZ");
    }

    /**
    Test QIGCFNTSIZ with a good parameter.
    **/
    public void Var331()
    {
      testGetSet("QIGCFNTSIZ", (new BigDecimal(123.4)).setScale(1, BigDecimal.ROUND_HALF_UP));
    }

    /**
    Test QPRCMLTTSK with a bad parameter.
    **/
    public void Var332()
    {
      testBadParm("QPRCMLTTSK");
    }

    /**
    Test QPRCMLTTSK with a good parameter.
    **/
    public void Var333()
    {
      testGetSet("QPRCMLTTSK", "0", "CPF18B4");
      // Tolerate CPF18B4, "System value cannot be changed in a secondary partition"
    }

    /**
    Test QPRCFEAT with a good parameter.
    **/
    public void Var334()
    {
      testGetSet("QPRCFEAT", new Integer(1234));
    }

  //-------------------
  // V4R4 system values
  //-------------------
    /**
    Test QCFGMSGQ with a bad parameter.
    **/
    public void Var335()
    {
      String name = "QCFGMSGQ";
      Object obj = null;
      Object toSet = "NOSUCHUSER";
      String failMsg = "";
      // int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable("System value not supported under this release.");
          return;
        }
        if (s.isReadOnly())
        {
          notApplicable("System value is read-only.");
          return;
        }
        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur.";
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1813")) // NOSUCHUSER not in *LIBL
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0084")) // value not allowed
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
    }

    /**
    Test QCFGMSGQ with a good parameter.
    **/
    public void Var336()
    {
      testGetSet("QCFGMSGQ", "QSYSOPR   QSYS");
    }

    /**
    Test QMLTTHDACN with a bad parameter.
    **/
    public void Var337()
    {
      testBadParm("QMLTTHDACN");
    }

    /**
    Test QMLTTHDACN with a good parameter.
    **/
    public void Var338()
    {
      testGetSet("QMLTTHDACN", "2");
    }

    /**
    Test ALWADDCLU with a bad parameter.
    **/
    public void Var339()
    {
      testBadParm("ALWADDCLU");
    }

    /**
    Test ALWADDCLU with a good parameter.
    **/
    public void Var340()
    {
      testGetSet("ALWADDCLU", "*ANY");
    }

    /**
    Test MDMCNTRYID with a bad parameter.
    **/
    public void Var341()
    {
      String name = "MDMCNTRYID";
      Object obj = null;
      Object toSet = "AZ"; // not a valid country code
      String failMsg = "";
      //int type;
      SystemValue s = null;
      try
      {
        s = new SystemValue(pwrSys_, name);
        if (vrm < s.getRelease())
        {
          notApplicable("System value not supported under this release.");
          return;
        }
        if (s.isReadOnly())
        {
          notApplicable("System value is read-only.");
          return;
        }

        obj = s.getValue();
        s.setValue(toSet);
        failMsg += "Exception didn't occur.";
        try
        {
          s.setValue(obj);
          if (!s.getValue().equals(obj))
          {
            failMsg += " Unable to set "+name+" to original value.";
          }
        }
        catch(Exception f)
        {
          failMsg += " Unable to reset "+name+".";
          failed(f, failMsg);
          return;
        }
        failed(failMsg);
        return;
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "AS400Exception") &&
            e.getMessage().toUpperCase().startsWith("CPF1076"))
          succeeded();
        else if (exceptionIs(e, "AS400Exception") &&
            s.getGroup() == SystemValueList.GROUP_NET &&
            e.getMessage().toUpperCase().startsWith("CPD0084")) // value not allowed
          succeeded();
        else
          failed(e, failMsg + " Unexpected exception occurred.");
      }
   }

    /**
    Test MDMCNTRYID with a good parameter.
    **/
    public void Var342()
    {
      testGetSet("MDMCNTRYID", "AR"); // Argentina
    }

    /**
    Test QMAXJOB with a bad parameter.
    **/
    public void Var343()
    {
      testBadParm("QMAXJOB");
    }

    /**
    Test QMAXJOB with a good parameter.
    **/
    public void Var344()
    {
      testGetSet("QMAXJOB", new Integer(100000));
    }

    /**
    Test QMAXSPLF with a bad parameter.
    **/
    public void Var345()
    {
      testBadParm("QMAXSPLF");
    }

    /**
    Test QMAXSPLF with a good parameter.
    **/
    public void Var346()
    {
      testGetSet("QMAXSPLF", new Integer(10000));
    }

    /**
    Test QVFYOBJRST with a bad parameter.
    **/
    public void Var347()
    {
      testBadParm("QVFYOBJRST");
    }

    /**
    Test QVFYOBJRST with a good parameter.
    **/
    public void Var348()
    {
      testGetSet("QVFYOBJRST", "2");
    }

    /**
    Test QLIBLCKLVL with a bad parameter.
    **/
    public void Var349()
    {
      testBadParm("QLIBLCKLVL");
    }

    /**
    Test QLIBLCKLVL with a good parameter.
    **/
    public void Var350()
    {
      testGetSet("QLIBLCKLVL", "0");
    }

    /**
    Test QSHRMEMCTL with a bad parameter.
    **/
    public void Var351()
    {
      testBadParm("QSHRMEMCTL");
    }

    /**
    Test QSHRMEMCTL with a good parameter.
    **/
    public void Var352()
    {
      testGetSet("QSHRMEMCTL", "0");
    }

    /**
    Test QPWDLVL with a bad parameter.
    **/
    public void Var353()
    {
      testBadParm("QPWDLVL");
    }

    /**
    Test QPWDLVL with a good parameter.
    **/
    public void Var354()
    {
      testGet("QPWDLVL");
    }

    /**
    Test QDBFSTCCOL with a bad parameter.
    **/
    public void Var355()
    {
      testBadParm("QDBFSTCCOL");
    }

    /**
    Test QDBFSTCCOL with a good parameter.
    **/
    public void Var356()
    {
      testGetSet("QDBFSTCCOL", "*SYSTEM");
    }

    /**
    Test QSPLFACN with a bad parameter.
    **/
    public void Var357()
    {
      testBadParm("QSPLFACN");
    }

    /**
    Test QSPLFACN with a good parameter.
    **/
    public void Var358()
    {
      testGetSet("QSPLFACN", "*DETACH");
    }

}

