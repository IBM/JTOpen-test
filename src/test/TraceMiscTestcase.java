///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  TraceMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.PrintWriter;
import java.util.*;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Trace;
import com.ibm.as400.access.Copyright;

/**
Test methods not covered by other testcases.
**/
public class TraceMiscTestcase extends Testcase
{
   private static final int HIGHEST = Trace.JDBC; //@E0A


   // As of 12/10/2010, the trace now include the list of JVM properties. 
   private static String sampleJvmProperties = "java.home= java.vm.version= java.version= os.name= os.version=    "; 

   private static String propertiesInfo = ""; 
   private static int JVM_PROPERTIES_LENGTH = 0;  
   static { 
     StringBuffer sb = new StringBuffer(); 
     JVM_PROPERTIES_LENGTH = sampleJvmProperties.length();
     sb.append("Added "+JVM_PROPERTIES_LENGTH+" for "+sampleJvmProperties+"\n"); 
     String [] properties = {
	 "java.home",
	 "java.vm.version",
	 "os.name", 
	 "os.version", 
         "java.vm.vendor",
         "java.vm.info", 
         "java.runtime.version",
         "java.fullversion", 
     }; 
     for (int i = 0; i < properties.length; i++) { 
       String value = System.getProperty(properties[i]);
       
       if (value == null) {
         int add = properties[i].length()+6; 
         JVM_PROPERTIES_LENGTH += add;
         sb.append("Added "+add+" for "+properties[i]+"=null\n"); 

       } else { 
         int add = properties[i].length()+value.length()+4; 
         JVM_PROPERTIES_LENGTH += add; 
         sb.append("Added "+add+" for "+properties[i]+"="+value+"\n"); 
          
       }
     }
     propertiesInfo = sb.toString(); 
   }
   
   
   private static int TRACE_HEADER_LENGTH = Copyright.version.length()+40+JVM_PROPERTIES_LENGTH;

   byte[] aByteArray = { 20, 21, 22, 23, 24, 25, 26};

/**
Constructor.
**/
  public TraceMiscTestcase(AS400            systemObject,
                           Vector           variationsToRun,
                           int              runMode,
                           FileOutputStream fileOutputStream)
                           
  {
    super(systemObject, "TraceMiscTestcase", 395, variationsToRun, runMode,
          fileOutputStream);

  }


  boolean deleteFile(String fileName)
  {
    try
    {
      Trace.setTraceOn(false);
      Trace.setFileName(null);

      RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
      raf.close();

      File file = new File(fileName);
      if (!file.delete())
      {
        output_.println("Could not delete " + fileName );
        return false;
      }
      return true;
    }
    catch (Exception e)
    {
      output_.println("Could not delete " + fileName );
      e.printStackTrace(output_);
      return false;
    }
  }

  boolean deleteFile(Object component, String fileName)
  {
    try
    {
      Trace.setTraceOn(false);
      Trace.setFileName(component, null);

      RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
      raf.close();

      File file = new File(fileName);
      if (!file.delete())
      {
        output_.println("Could not delete "+fileName+".");
        return false;
      }
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      return false;
    }
  }


/**
Run variations.
**/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if (runMode_ != ATTENDED)
    {
      if (allVariations || variationsToRun_.contains("1"))
        variation001();
      if (allVariations || variationsToRun_.contains("2"))
        variation002();
      if (allVariations || variationsToRun_.contains("3"))
        variation003();
      if (allVariations || variationsToRun_.contains("4"))
        variation004();
      if (allVariations || variationsToRun_.contains("5"))
        variation005();
      if (allVariations || variationsToRun_.contains("6"))
        variation006();
      if (allVariations || variationsToRun_.contains("7"))
        variation007();
      if (allVariations || variationsToRun_.contains("8"))
        variation008();
      if (allVariations || variationsToRun_.contains("9"))
        variation009();
      if (allVariations || variationsToRun_.contains("10"))
        variation010();
      if (allVariations || variationsToRun_.contains("11"))
        variation011();
      if (allVariations || variationsToRun_.contains("12"))
        variation012();
      if (allVariations || variationsToRun_.contains("13"))
        variation013();
      if (allVariations || variationsToRun_.contains("14"))
        variation014();
      if (allVariations || variationsToRun_.contains("15"))
        variation015();
      if (allVariations || variationsToRun_.contains("16"))
        variation016();
      if (allVariations || variationsToRun_.contains("17"))
        variation017();
      if (allVariations || variationsToRun_.contains("18"))
        variation018();
      if (allVariations || variationsToRun_.contains("19"))
        variation019();
      if (allVariations || variationsToRun_.contains("20"))
        variation020();
      if (allVariations || variationsToRun_.contains("21"))
        variation021();
      if (allVariations || variationsToRun_.contains("22"))
        variation022();
      if (allVariations || variationsToRun_.contains("23"))
        variation023();
      if (allVariations || variationsToRun_.contains("24"))
        variation024();
      if (allVariations || variationsToRun_.contains("25"))
        variation025();
      if (allVariations || variationsToRun_.contains("26"))
        variation026();
      if (allVariations || variationsToRun_.contains("27"))
        variation027();
      if (allVariations || variationsToRun_.contains("28"))
        variation028();
      if (allVariations || variationsToRun_.contains("29"))
        variation029();
      if (allVariations || variationsToRun_.contains("30"))
        variation030();
      if (allVariations || variationsToRun_.contains("31"))
        variation031();
      if (allVariations || variationsToRun_.contains("32"))
        variation032();
      if (allVariations || variationsToRun_.contains("33"))
        variation033();
      if (allVariations || variationsToRun_.contains("34"))
        variation034();
      if (allVariations || variationsToRun_.contains("35"))
        variation035();
      if (allVariations || variationsToRun_.contains("36"))
        variation036();
      if (allVariations || variationsToRun_.contains("37"))
        variation037();
      if (allVariations || variationsToRun_.contains("38"))
        variation038();
      if (allVariations || variationsToRun_.contains("39"))
        variation039();
      if (allVariations || variationsToRun_.contains("40"))
        variation040();
      if (allVariations || variationsToRun_.contains("41"))
        variation041();
      if (allVariations || variationsToRun_.contains("42"))
        variation042();
      if (allVariations || variationsToRun_.contains("43"))
        variation043();
      if (allVariations || variationsToRun_.contains("44"))
        variation044();
      if (allVariations || variationsToRun_.contains("45"))
        variation045();
      if (allVariations || variationsToRun_.contains("46"))
        variation046();
      if (allVariations || variationsToRun_.contains("47"))
        variation047();
      if (allVariations || variationsToRun_.contains("48"))
        variation048();
      if (allVariations || variationsToRun_.contains("49"))
        variation049();
      if (allVariations || variationsToRun_.contains("50"))
        variation050();
      if (allVariations || variationsToRun_.contains("51"))
        variation051();
      if (allVariations || variationsToRun_.contains("52"))
        variation052();
      if (allVariations || variationsToRun_.contains("53"))
        variation053();
      if (allVariations || variationsToRun_.contains("54"))
        variation054();
      if (allVariations || variationsToRun_.contains("55"))
        variation055();
      if (allVariations || variationsToRun_.contains("56"))
        variation056();
      if (allVariations || variationsToRun_.contains("57"))
        variation057();
      if (allVariations || variationsToRun_.contains("58"))
        variation058();
      if (allVariations || variationsToRun_.contains("59"))
        variation059();
      if (allVariations || variationsToRun_.contains("60"))
        variation060();
      if (allVariations || variationsToRun_.contains("61"))
        variation061();
      if (allVariations || variationsToRun_.contains("62"))
        variation062();
      if (allVariations || variationsToRun_.contains("63"))
        variation063();
      if (allVariations || variationsToRun_.contains("64"))
        variation064();
      if (allVariations || variationsToRun_.contains("65"))
        variation065();
      if (allVariations || variationsToRun_.contains("66"))
        variation066();
      if (allVariations || variationsToRun_.contains("67"))
        variation067();
      if (allVariations || variationsToRun_.contains("68"))
        variation068();
      if (allVariations || variationsToRun_.contains("69"))
        variation069();
      if (allVariations || variationsToRun_.contains("70"))
        variation070();
      if (allVariations || variationsToRun_.contains("71"))
        variation071();
      if (allVariations || variationsToRun_.contains("72"))
        variation072();
      if (allVariations || variationsToRun_.contains("73"))
        variation073();
      if (allVariations || variationsToRun_.contains("74"))
        variation074();
      if (allVariations || variationsToRun_.contains("75"))
        variation075();
      if (allVariations || variationsToRun_.contains("76"))
        variation076();
      if (allVariations || variationsToRun_.contains("77"))
        variation077();
      if (allVariations || variationsToRun_.contains("78"))
        variation078();
      if (allVariations || variationsToRun_.contains("79"))
        variation079();
      if (allVariations || variationsToRun_.contains("80"))
        variation080();
      if (allVariations || variationsToRun_.contains("81"))
        variation081();
      if (allVariations || variationsToRun_.contains("82"))
        variation082();
      if (allVariations || variationsToRun_.contains("83"))
        variation083();
      if (allVariations || variationsToRun_.contains("84"))
        variation084();
      if (allVariations || variationsToRun_.contains("85"))
        variation085();
      if (allVariations || variationsToRun_.contains("86"))
        variation086();
      if (allVariations || variationsToRun_.contains("87"))
        variation087();
      if (allVariations || variationsToRun_.contains("88"))
        variation088();
      if (allVariations || variationsToRun_.contains("89"))
        variation089();
      if (allVariations || variationsToRun_.contains("90"))
        variation090();
      if (allVariations || variationsToRun_.contains("91"))
        variation091();
      if (allVariations || variationsToRun_.contains("92"))
        variation092();
      if (allVariations || variationsToRun_.contains("93"))
        variation093();
      if (allVariations || variationsToRun_.contains("94"))
        variation094();
      if (allVariations || variationsToRun_.contains("95"))
        variation095();
      if (allVariations || variationsToRun_.contains("96"))
        variation096();
      if (allVariations || variationsToRun_.contains("97"))
        variation097();
      if (allVariations || variationsToRun_.contains("98"))
        variation098();
      if (allVariations || variationsToRun_.contains("99"))
        variation099();
      if (allVariations || variationsToRun_.contains("100"))
        variation100();
      if (allVariations || variationsToRun_.contains("101"))
        variation101();
      if (allVariations || variationsToRun_.contains("102"))
        variation102();
      if (allVariations || variationsToRun_.contains("103"))
        variation103();
      if (allVariations || variationsToRun_.contains("104"))
        variation104();
      if (allVariations || variationsToRun_.contains("105"))
        variation105();
      if (allVariations || variationsToRun_.contains("106"))
        variation106();
      if (allVariations || variationsToRun_.contains("107"))
        variation107();
      if (allVariations || variationsToRun_.contains("108"))
        variation108();
      if (allVariations || variationsToRun_.contains("109"))
        variation109();
      if (allVariations || variationsToRun_.contains("110"))
        variation110();
      if (allVariations || variationsToRun_.contains("111"))
        variation111();
      if (allVariations || variationsToRun_.contains("112"))
        variation112();
      if (allVariations || variationsToRun_.contains("113"))
        variation113();
      if (allVariations || variationsToRun_.contains("114"))
        variation114();
      if (allVariations || variationsToRun_.contains("115"))
        variation115();
      if (allVariations || variationsToRun_.contains("116"))
        variation116();
      if (allVariations || variationsToRun_.contains("117"))
        variation117();
      if (allVariations || variationsToRun_.contains("118"))
        variation118();
      if (allVariations || variationsToRun_.contains("119"))
        variation119();
      if (allVariations || variationsToRun_.contains("120"))
        variation120();
      if (allVariations || variationsToRun_.contains("121"))
        variation121();
      if (allVariations || variationsToRun_.contains("122"))
        variation122();
      if (allVariations || variationsToRun_.contains("123"))
        variation123();
      if (allVariations || variationsToRun_.contains("124"))
        variation124();
      if (allVariations || variationsToRun_.contains("125"))
        variation125();
      if (allVariations || variationsToRun_.contains("126"))
        variation126();
      if (allVariations || variationsToRun_.contains("127"))
        variation127();
      if (allVariations || variationsToRun_.contains("128"))
        variation128();
      if (allVariations || variationsToRun_.contains("129"))
        variation129();
      if (allVariations || variationsToRun_.contains("130"))
        variation130();
      if (allVariations || variationsToRun_.contains("131"))
        variation131();
      if (allVariations || variationsToRun_.contains("132"))
        variation132();
      if (allVariations || variationsToRun_.contains("133"))
        variation133();
      if (allVariations || variationsToRun_.contains("134"))
        variation134();
      if (allVariations || variationsToRun_.contains("135"))
        variation135();
      if (allVariations || variationsToRun_.contains("136"))
        variation136();
      if (allVariations || variationsToRun_.contains("137"))
        variation137();
      if (allVariations || variationsToRun_.contains("138"))
        variation138();
      if (allVariations || variationsToRun_.contains("139"))
        variation139();
      if (allVariations || variationsToRun_.contains("140"))
        variation140();
      if (allVariations || variationsToRun_.contains("141"))
        variation141();
      if (allVariations || variationsToRun_.contains("142"))
        variation142();
      if (allVariations || variationsToRun_.contains("143"))
        variation143();
      if (allVariations || variationsToRun_.contains("144"))
        variation144();
      if (allVariations || variationsToRun_.contains("145"))
        variation145();
      if (allVariations || variationsToRun_.contains("146"))
        variation146();
      if (allVariations || variationsToRun_.contains("147"))
        variation147();
      if (allVariations || variationsToRun_.contains("148"))
        variation148();
      if (allVariations || variationsToRun_.contains("149"))
        variation149();
      if (allVariations || variationsToRun_.contains("150"))
        variation150();
      if (allVariations || variationsToRun_.contains("151"))
        variation151();
      if (allVariations || variationsToRun_.contains("152"))
        variation152();
      if (allVariations || variationsToRun_.contains("153"))
        variation153();
      if (allVariations || variationsToRun_.contains("154"))
        variation154();
      if (allVariations || variationsToRun_.contains("155"))
        variation155();
      if (allVariations || variationsToRun_.contains("156"))
        variation156();
      if (allVariations || variationsToRun_.contains("157"))
        variation157();
      if (allVariations || variationsToRun_.contains("158"))
        variation158();
      if (allVariations || variationsToRun_.contains("159"))
        variation159();
      if (allVariations || variationsToRun_.contains("160"))
        variation160();
      if (allVariations || variationsToRun_.contains("161"))
        variation161();
      if (allVariations || variationsToRun_.contains("162"))
        variation162();
      if (allVariations || variationsToRun_.contains("163"))
        variation163();
      if (allVariations || variationsToRun_.contains("164"))
        variation164();
      if (allVariations || variationsToRun_.contains("165"))
        variation165();
      if (allVariations || variationsToRun_.contains("166"))
        variation166();
      if (allVariations || variationsToRun_.contains("167"))
        variation167();
      if (allVariations || variationsToRun_.contains("168"))
        variation168();
      if (allVariations || variationsToRun_.contains("169"))
        variation169();
      if (allVariations || variationsToRun_.contains("170"))
        variation170();
      if (allVariations || variationsToRun_.contains("171"))
        variation171();
      if (allVariations || variationsToRun_.contains("172"))
        variation172();
      if (allVariations || variationsToRun_.contains("173"))
        variation173();
      if (allVariations || variationsToRun_.contains("174"))
        variation174();
      if (allVariations || variationsToRun_.contains("175"))
        variation175();
      if (allVariations || variationsToRun_.contains("176"))
        variation176();
      if (allVariations || variationsToRun_.contains("177"))
        variation177();
      if (allVariations || variationsToRun_.contains("178"))
        variation178();
      if (allVariations || variationsToRun_.contains("179"))
        variation179();
      if (allVariations || variationsToRun_.contains("180"))
        variation180();
      if (allVariations || variationsToRun_.contains("181"))
        variation181();
      if (allVariations || variationsToRun_.contains("182"))
        variation182();
      if (allVariations || variationsToRun_.contains("183"))
        variation183();
      if (allVariations || variationsToRun_.contains("184"))
        variation184();
      if (allVariations || variationsToRun_.contains("185"))
        variation185();
      if (allVariations || variationsToRun_.contains("186"))
        variation186();
      if (allVariations || variationsToRun_.contains("187"))
        variation187();
      if (allVariations || variationsToRun_.contains("188"))
        variation188();
      if (allVariations || variationsToRun_.contains("189"))
        variation189();
      if (allVariations || variationsToRun_.contains("190"))
        variation190();
      if (allVariations || variationsToRun_.contains("191"))
        variation191();
      if (allVariations || variationsToRun_.contains("192"))
        variation192();
      if (allVariations || variationsToRun_.contains("193"))
        variation193();
      if (allVariations || variationsToRun_.contains("194"))
        variation194();
      if (allVariations || variationsToRun_.contains("195"))
        variation195();
      if (allVariations || variationsToRun_.contains("196"))
        variation196();
      if (allVariations || variationsToRun_.contains("197"))
        variation197();
      if (allVariations || variationsToRun_.contains("198"))
        variation198();
      if (allVariations || variationsToRun_.contains("199"))
        variation199();
      if (allVariations || variationsToRun_.contains("200"))
        variation200();
      if (allVariations || variationsToRun_.contains("201"))
        variation201();
      if (allVariations || variationsToRun_.contains("202"))
        variation202();
      if (allVariations || variationsToRun_.contains("203"))
        variation203();
      if (allVariations || variationsToRun_.contains("204"))
        variation204();
      if (allVariations || variationsToRun_.contains("205"))
        variation205();
      if (allVariations || variationsToRun_.contains("206"))
        variation206();
      if (allVariations || variationsToRun_.contains("207"))
        variation207();
      if (allVariations || variationsToRun_.contains("208"))
        variation208();
      if (allVariations || variationsToRun_.contains("209"))
        variation209();
      if (allVariations || variationsToRun_.contains("210"))
        variation210();
      if (allVariations || variationsToRun_.contains("211"))
        variation211();
      if (allVariations || variationsToRun_.contains("212"))
        variation212();
      if (allVariations || variationsToRun_.contains("213"))
        variation213();
      if (allVariations || variationsToRun_.contains("214"))
        variation214();
      if (allVariations || variationsToRun_.contains("215"))
        variation215();
      if (allVariations || variationsToRun_.contains("216"))
        variation216();
      if (allVariations || variationsToRun_.contains("217"))
        variation217();
      if (allVariations || variationsToRun_.contains("218"))
        variation218();
      if (allVariations || variationsToRun_.contains("219"))
        variation219();
      if (allVariations || variationsToRun_.contains("220"))
        variation220();
      if (allVariations || variationsToRun_.contains("221"))
        variation221();
      if (allVariations || variationsToRun_.contains("222"))
        variation222();
      if (allVariations || variationsToRun_.contains("223"))
        variation223();
      if (allVariations || variationsToRun_.contains("224"))
        variation224();
      if (allVariations || variationsToRun_.contains("225"))
        variation225();
      if (allVariations || variationsToRun_.contains("226"))
        variation226();
      if (allVariations || variationsToRun_.contains("227"))
        variation227();
      if (allVariations || variationsToRun_.contains("228"))
        variation228();
      if (allVariations || variationsToRun_.contains("229"))
        variation229();
      if (allVariations || variationsToRun_.contains("230"))
        variation230();
      if (allVariations || variationsToRun_.contains("231"))
        variation231();
      if (allVariations || variationsToRun_.contains("232"))
        variation232();
      if (allVariations || variationsToRun_.contains("233"))
        variation233();
      if (allVariations || variationsToRun_.contains("234"))
        variation234();
      if (allVariations || variationsToRun_.contains("235"))
        variation235();
      if (allVariations || variationsToRun_.contains("236"))
        variation236();
      if (allVariations || variationsToRun_.contains("237"))
        variation237();
      if (allVariations || variationsToRun_.contains("238"))
        variation238();
      if (allVariations || variationsToRun_.contains("239"))
        variation239();
      if (allVariations || variationsToRun_.contains("240"))
        variation240();
      if (allVariations || variationsToRun_.contains("241"))
        variation241();
      if (allVariations || variationsToRun_.contains("242"))
        variation242();
      if (allVariations || variationsToRun_.contains("243"))
        variation243();
      if (allVariations || variationsToRun_.contains("244"))
        variation244();
      if (allVariations || variationsToRun_.contains("245"))
        variation245();
      if (allVariations || variationsToRun_.contains("246"))
        variation246();
      if (allVariations || variationsToRun_.contains("247"))
        variation247();
      if (allVariations || variationsToRun_.contains("248"))
        variation248();
      if (allVariations || variationsToRun_.contains("249"))
        variation249();
      if (allVariations || variationsToRun_.contains("250"))
        variation250();
      if (allVariations || variationsToRun_.contains("251"))
        variation251();
      if (allVariations || variationsToRun_.contains("252"))
        variation252();
      if (allVariations || variationsToRun_.contains("253"))
        variation253();
      if (allVariations || variationsToRun_.contains("254"))
        variation254();
      if (allVariations || variationsToRun_.contains("255"))
        variation255();
      if (allVariations || variationsToRun_.contains("256"))
        variation256();
      if (allVariations || variationsToRun_.contains("257"))
        variation257();
      if (allVariations || variationsToRun_.contains("258"))
        variation258();
      if (allVariations || variationsToRun_.contains("259"))
        variation259();
      if (allVariations || variationsToRun_.contains("260"))
        variation260();
      if (allVariations || variationsToRun_.contains("261"))
        variation261();
      if (allVariations || variationsToRun_.contains("262"))
        variation262();
      if (allVariations || variationsToRun_.contains("263"))
        variation263();
      if (allVariations || variationsToRun_.contains("264"))
        variation264();
      if (allVariations || variationsToRun_.contains("265"))
        variation265();
      if (allVariations || variationsToRun_.contains("266"))
        variation266();
      if (allVariations || variationsToRun_.contains("267"))
        variation267();
      if (allVariations || variationsToRun_.contains("268"))
        variation268();
      if (allVariations || variationsToRun_.contains("269"))
        variation269();
      if (allVariations || variationsToRun_.contains("270"))
        variation270();
      if (allVariations || variationsToRun_.contains("271"))
        variation271();
      if (allVariations || variationsToRun_.contains("272"))
        variation272();
      if (allVariations || variationsToRun_.contains("273"))
        variation273();
      if (allVariations || variationsToRun_.contains("274"))
        variation274();
      if (allVariations || variationsToRun_.contains("275"))
        variation275();
      if (allVariations || variationsToRun_.contains("276"))
        variation276();
      if (allVariations || variationsToRun_.contains("277"))
        variation277();
      if (allVariations || variationsToRun_.contains("278"))
        variation278();
      if (allVariations || variationsToRun_.contains("279"))
        variation279();
      if (allVariations || variationsToRun_.contains("280"))
        variation280();
      if (allVariations || variationsToRun_.contains("281"))
        variation281();
      if (allVariations || variationsToRun_.contains("282"))
        variation282();
      if (allVariations || variationsToRun_.contains("283"))
        variation283();
      if (allVariations || variationsToRun_.contains("284"))
        variation284();
      if (allVariations || variationsToRun_.contains("285"))
        variation285();
      if (allVariations || variationsToRun_.contains("286"))
        variation286();
      if (allVariations || variationsToRun_.contains("287"))
        variation287();
      if (allVariations || variationsToRun_.contains("288"))
        variation288();
      if (allVariations || variationsToRun_.contains("289"))
        variation289();
      if (allVariations || variationsToRun_.contains("290"))
        variation290();
      if (allVariations || variationsToRun_.contains("291"))
        variation291();
      if (allVariations || variationsToRun_.contains("292"))
        variation292();
      if (allVariations || variationsToRun_.contains("293"))
        variation293();
      if (allVariations || variationsToRun_.contains("294"))
        variation294();
      if (allVariations || variationsToRun_.contains("295"))
        variation295();
      if (allVariations || variationsToRun_.contains("296"))
        variation296();
      if (allVariations || variationsToRun_.contains("297"))
        variation297();
      if (allVariations || variationsToRun_.contains("298"))
        variation298();
      if (allVariations || variationsToRun_.contains("299"))
        variation299();
      if (allVariations || variationsToRun_.contains("300"))
        variation300();
      if (allVariations || variationsToRun_.contains("301"))
        variation301();
      if (allVariations || variationsToRun_.contains("302"))
        variation302();
      if (allVariations || variationsToRun_.contains("303"))
        variation303();
      if (allVariations || variationsToRun_.contains("304"))
        variation304();
      if (allVariations || variationsToRun_.contains("305"))
        variation305();
      if (allVariations || variationsToRun_.contains("306"))
        variation306();
      if (allVariations || variationsToRun_.contains("307"))
        variation307();
      if (allVariations || variationsToRun_.contains("308"))
        variation308();
      if (allVariations || variationsToRun_.contains("309"))
        variation309();
      if (allVariations || variationsToRun_.contains("310"))
        variation310();
      if (allVariations || variationsToRun_.contains("311"))
        variation311();
      if (allVariations || variationsToRun_.contains("312"))
        variation312();
      if (allVariations || variationsToRun_.contains("313"))
        variation313();
      if (allVariations || variationsToRun_.contains("314"))
        variation314();
      if (allVariations || variationsToRun_.contains("315"))
        variation315();
      if (allVariations || variationsToRun_.contains("316"))
        variation316();
      if (allVariations || variationsToRun_.contains("317"))
        variation317();
      if (allVariations || variationsToRun_.contains("318"))
        variation318();
      if (allVariations || variationsToRun_.contains("319"))
        variation319();
      if (allVariations || variationsToRun_.contains("320"))
        variation320();
      if (allVariations || variationsToRun_.contains("321"))
        variation321();
      if (allVariations || variationsToRun_.contains("322"))
        variation322();
      if (allVariations || variationsToRun_.contains("323"))
        variation323();
      if (allVariations || variationsToRun_.contains("324"))
        variation324();
      if (allVariations || variationsToRun_.contains("325"))
        variation325();
      if (allVariations || variationsToRun_.contains("326"))
        variation326();
      if (allVariations || variationsToRun_.contains("327"))
        variation327();
      if (allVariations || variationsToRun_.contains("328"))
        variation328();
      if (allVariations || variationsToRun_.contains("329"))
        variation329();
      if (allVariations || variationsToRun_.contains("330"))
        variation330();
      if (allVariations || variationsToRun_.contains("331"))
        variation331();
      if (allVariations || variationsToRun_.contains("332"))
        variation332();
      if (allVariations || variationsToRun_.contains("333"))
        variation333();
      if (allVariations || variationsToRun_.contains("334"))
        variation334();
      if (allVariations || variationsToRun_.contains("335"))
        variation335();
      if (allVariations || variationsToRun_.contains("336"))
        variation336();
      if (allVariations || variationsToRun_.contains("337"))
        variation337();
      if (allVariations || variationsToRun_.contains("338"))
        variation338();
      if (allVariations || variationsToRun_.contains("339"))
        variation339();
      if (allVariations || variationsToRun_.contains("340"))
        variation340();
      if (allVariations || variationsToRun_.contains("341"))
        variation341();
      if (allVariations || variationsToRun_.contains("342"))
        variation342();
      if (allVariations || variationsToRun_.contains("343"))
        variation343();
      if (allVariations || variationsToRun_.contains("344"))
        variation344();
      if (allVariations || variationsToRun_.contains("345"))
        variation345();
      if (allVariations || variationsToRun_.contains("346"))
        variation346();
      if (allVariations || variationsToRun_.contains("347"))
        variation347();
      if (allVariations || variationsToRun_.contains("348"))
        variation348();
      if (allVariations || variationsToRun_.contains("349"))
        variation349();
      if (allVariations || variationsToRun_.contains("350"))
        variation350();
      if (allVariations || variationsToRun_.contains("351"))
        variation351();
      if (allVariations || variationsToRun_.contains("352"))
        variation352();
      if (allVariations || variationsToRun_.contains("353"))
        variation353();
      if (allVariations || variationsToRun_.contains("354"))
        variation354();
      if (allVariations || variationsToRun_.contains("355"))
        variation355();
      if (allVariations || variationsToRun_.contains("356"))
        variation356();
      if (allVariations || variationsToRun_.contains("357"))
        variation357();
      if (allVariations || variationsToRun_.contains("358"))
        variation358();
      if (allVariations || variationsToRun_.contains("359"))
        variation359();
      if (allVariations || variationsToRun_.contains("360"))
        variation360();
      if (allVariations || variationsToRun_.contains("361"))
        variation361();
      if (allVariations || variationsToRun_.contains("362"))
        variation362();
      if (allVariations || variationsToRun_.contains("363"))
        variation363();
      if (allVariations || variationsToRun_.contains("364"))
        variation364();
      if (allVariations || variationsToRun_.contains("365"))
        variation365();
      if (allVariations || variationsToRun_.contains("366"))
        variation366();
      if (allVariations || variationsToRun_.contains("367"))
        variation367();
      if (allVariations || variationsToRun_.contains("368"))
        variation368();
      if (allVariations || variationsToRun_.contains("369"))
        variation369();
      if (allVariations || variationsToRun_.contains("370"))
        variation370();
      if (allVariations || variationsToRun_.contains("371"))
        variation371();
      if (allVariations || variationsToRun_.contains("372"))
        variation372();
      if (allVariations || variationsToRun_.contains("373"))
        variation373();
      if (allVariations || variationsToRun_.contains("374"))
        variation374();
      if (allVariations || variationsToRun_.contains("375"))
        variation375();
      if (allVariations || variationsToRun_.contains("376"))
        variation376();
      if (allVariations || variationsToRun_.contains("377"))
        variation377();
      if (allVariations || variationsToRun_.contains("378"))
        variation378();
      if (allVariations || variationsToRun_.contains("379"))
        variation379();
      if (allVariations || variationsToRun_.contains("380"))
        variation380();
      if (allVariations || variationsToRun_.contains("381"))
        variation381();
      if (allVariations || variationsToRun_.contains("382"))
        variation382();
      if (allVariations || variationsToRun_.contains("383"))
        variation383();
      if (allVariations || variationsToRun_.contains("384"))
        variation384();
      if (allVariations || variationsToRun_.contains("385"))
        variation385();
      if (allVariations || variationsToRun_.contains("386"))
        variation386();
      if (allVariations || variationsToRun_.contains("387"))
        variation387();
      if (allVariations || variationsToRun_.contains("388"))
        variation388();
      if (allVariations || variationsToRun_.contains("389"))
        variation389();
      if (allVariations || variationsToRun_.contains("390"))
        variation390();
      if (allVariations || variationsToRun_.contains("391"))
        variation391();
      if (allVariations || variationsToRun_.contains("392"))
        variation392();
      if (allVariations || variationsToRun_.contains("393"))
        variation393();
      if (allVariations || variationsToRun_.contains("394"))
        variation394();
      if (allVariations || variationsToRun_.contains("395"))
        variation395();

      deleteFile("Bit.Bucket");
    }
  }

/**
Ensure that setTraceAllOn(boolean) and isTraceXXXOn() agree.
**/
  public void variation001()
  {
    setVariation(1);
    Trace.setTraceAllOn(true);
    if (Trace.isTraceErrorOn() &&
        Trace.isTraceConversionOn() &&
        Trace.isTraceDatastreamOn() &&
        Trace.isTraceDiagnosticOn() &&
        Trace.isTraceInformationOn() &&
        Trace.isTraceProxyOn() &&
        Trace.isTraceThreadOn() &&
        Trace.isTraceWarningOn() &&
        Trace.isTraceAllOn())
    {
      Trace.setTraceAllOn(false);
      assertCondition(!Trace.isTraceAllOn() &&
             !Trace.isTraceErrorOn() &&
             !Trace.isTraceConversionOn() &&
             !Trace.isTraceDatastreamOn() &&
             !Trace.isTraceDiagnosticOn() &&
             !Trace.isTraceInformationOn() &&
             !Trace.isTraceProxyOn() &&
             !Trace.isTraceThreadOn() &&
             !Trace.isTraceWarningOn());
    }
    else
    {
      failed("Trace.isTraceAllOn() returns incorrect value.");
    }
  }

/**
Ensure that setTraceErrorOn(boolean) and isTraceErrorOn() agree.
**/
  public void variation002()
  {
    setVariation(2);
    Trace.setTraceErrorOn(true);
    if (Trace.isTraceErrorOn())
    {
      Trace.setTraceErrorOn(false);
      assertCondition(!Trace.isTraceErrorOn());
    }
    else
    {
      failed("Trace.isTraceErrorOn() returns incorrect value.");
    }
  }

/**
Ensure that setTraceDatastreamOn(boolean) and isTraceDatastreamOn() agree.
**/
  public void variation003()
  {
    setVariation(3);
    Trace.setTraceDatastreamOn(true);
    if (Trace.isTraceDatastreamOn())
    {
      Trace.setTraceDatastreamOn(false);
      assertCondition(!Trace.isTraceDatastreamOn());
    }
    else
    {
      failed("Trace.isTraceDatastreamOn() returns incorrect value.");
    }
  }


/**
Ensure that setTraceDiagnosticOn(boolean) and isTraceDiagnosticOn() agree.
**/
  public void variation004()
  {
    setVariation(4);
    Trace.setTraceDiagnosticOn(true);
    if (Trace.isTraceDiagnosticOn())
    {
      Trace.setTraceDiagnosticOn(false);
      assertCondition(!Trace.isTraceDiagnosticOn());
    }
    else
    {
      failed("Trace.isTraceDiagnosticOn() returns incorrect value.");
    }
  }


/**
Ensure that setTraceInformationOn(boolean) and isTraceInformationOn() agree.
**/
  public void variation005()
  {
    setVariation(5);
    Trace.setTraceInformationOn(true);
    if (Trace.isTraceInformationOn())
    {
      Trace.setTraceInformationOn(false);
      assertCondition(!Trace.isTraceInformationOn());
    }
    else
    {
      failed("Trace.isTraceInformationOn() returns incorrect value.");
    }
  }


/**
Ensure that setTraceWarningOn(boolean) and isTraceWarningOn() agree.
**/
  public void variation006()
  {
    setVariation(6);
    Trace.setTraceWarningOn(true);
    if (Trace.isTraceWarningOn())
    {
      Trace.setTraceWarningOn(false);
      assertCondition(!Trace.isTraceWarningOn());
    }
    else
    {
      failed("Trace.isTraceWarningOn() returns incorrect value.");
    }
  }


/**
Ensure that setTraceThreadOn(boolean) and isTraceThreadOn() agree.
**/
  public void variation007()
  {
    setVariation(7);
    Trace.setTraceThreadOn(true);
    if (Trace.isTraceThreadOn())
    {
      Trace.setTraceThreadOn(false);
      assertCondition(!Trace.isTraceThreadOn());
    }
    else
    {
      failed("Trace.isTraceThreadOn() returns incorrect value.");
    }
  }


/**
Ensure that setFileName(String) creates a file if it doesn't exist.
**/
  public void variation008()
  {
    setVariation(8);
    try
    {
      File file = new File("TraceMiscTestcase.v6");
      file.delete();
      if (!file.exists())
      {
        Trace.setFileName("TraceMiscTestcase.v6");
        assertCondition(file.exists());
      }
      else
      {
        failed("Unable to delete '" + file.toString() + "' for setup.");
      }
    }
    catch (Exception e)
    {
      failed(e);
    }
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(int, String) throws IllegalArgumentException if the category is invalid.
**/
  public void variation009()
  {
    setVariation(9);
    Trace.setTraceOn(true);
    try
    {
      Trace.log(Trace.DATASTREAM - 1, "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello"); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

/**
Ensure that log(int, String) throws IllegalArgumentException if the category is Trace.ALL.
**/
  public void variation010()
  {
    setVariation(10);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.ALL, "hello");
      Trace.log(100,       "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello"); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

/**
Ensure that log(int, String) throws IllegalArgumentException if the category is Trace.THREAD.
**/
  public void variation011()
  {
    setVariation(11);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.THREAD, "hello");
      Trace.log(99, "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello"); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

/**
Ensure that log(int, String) throws no Exception if the message is null.
**/
  public void variation012()
  {
    setVariation(12);
    try
    {
      Trace.log(Trace.ERROR, (String) null);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


/**
Ensure that log(XXX, String) does nothing if tracing is off but all of the categories are on.
**/
  public void variation013()
  {
    setVariation(13);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v9", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v9");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation9");
      Trace.log(Trace.DATASTREAM, "variation9");
      Trace.log(Trace.ERROR, "variation9");
      Trace.log(Trace.INFORMATION, "variation9");
      Trace.log(Trace.WARNING, "variation9");
      Trace.log(Trace.CONVERSION, "variation9");
      Trace.log(Trace.PROXY, "variation9");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(XXX, String) does nothing if tracing is on but all the categories are off.
**/
  public void variation014()
  {
    setVariation(14);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v10", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v10");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation10");
      Trace.log(Trace.DATASTREAM, "variation10");
      Trace.log(Trace.ERROR, "variation10");
      Trace.log(Trace.INFORMATION, "variation10");
      Trace.log(Trace.WARNING, "variation10");
      Trace.log(Trace.CONVERSION, "variation10");
      Trace.log(Trace.PROXY, "variation10");
      assertCondition(file.length() <= (lengthBefore + TRACE_HEADER_LENGTH), "file.length()="+file.length()+" sb <= "+(lengthBefore + TRACE_HEADER_LENGTH)+" lengthBefore="+lengthBefore+ " trace_header_length="+TRACE_HEADER_LENGTH+" file--------->\n"+propertiesInfo+dumpFile("TraceMiscTestcase.v10"));  // add length of version string @D2C @E1C
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String) logs a message when all tracing is enabled.
**/
  public void variation015()
  {
    setVariation(15);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v17");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation17";
      Trace.log(Trace.DIAGNOSTIC, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String) does nothing if tracing is off but the diagnostic category is on.
**/
  public void variation016()
  {
    setVariation(16);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v9", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v9");
      Trace.setTraceOn(false);
      Trace.setTraceDiagnosticOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation9");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String) does nothing if tracing is on but the diagnostic category is off.
**/
  public void variation017()
  {
    setVariation(17);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v17", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v17");
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation17");
      assertCondition(file.length() <= lengthBefore + TRACE_HEADER_LENGTH, "file.length()="+file.length()+" sb <= "+(lengthBefore + TRACE_HEADER_LENGTH)+" lengthBefore="+lengthBefore+ " trace_header_length="+TRACE_HEADER_LENGTH);  // add length of version string @D2C @E1C
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String) does nothing if tracing is off but the error category is on.
**/
  public void variation018()
  {
    setVariation(18);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v11", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v11");
      Trace.setTraceOn(false);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "variation11");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String) does nothing if tracing is on but the error category is off.
**/
  public void variation019()
  {
    setVariation(19);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v12", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v12");
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(false);
      Trace.log(Trace.ERROR, "variation12");
      assertCondition(file.length() <= lengthBefore + TRACE_HEADER_LENGTH,"file.length()="+file.length()+" sb <= "+(lengthBefore + TRACE_HEADER_LENGTH)+" lengthBefore="+lengthBefore+ " trace_header_length="+TRACE_HEADER_LENGTH);  // add length of version string @D2C @E1C
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String) does nothing if tracing is off but the information category is on.
**/
  public void variation020()
  {
    setVariation(20);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v13", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v13");
      Trace.setTraceOn(false);
      Trace.setTraceInformationOn(true);
      Trace.log(Trace.INFORMATION, "variation13");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String) does nothing if tracing is on but the information category is off.
**/
  public void variation021()
  {
    setVariation(21);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v14", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v14");
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(false);
      Trace.log(Trace.INFORMATION, "variation14");
      assertCondition(file.length() <= lengthBefore + TRACE_HEADER_LENGTH,"file.length()="+file.length()+" sb <= "+(lengthBefore + TRACE_HEADER_LENGTH)+" lengthBefore="+lengthBefore+ " trace_header_length="+TRACE_HEADER_LENGTH );  // add length of version string @D2C @E1C
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String) does nothing if tracing is off but the warning category is on.
**/
  public void variation022()
  {
    setVariation(22);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v15", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v15");
      Trace.setTraceOn(false);
      Trace.setTraceWarningOn(true);
      Trace.log(Trace.WARNING, "variation15");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String) does nothing if tracing is on but the warning category is off.
**/
  public void variation023()
  {
    setVariation(23);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v16", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v16");               // @C0C
      Trace.setTraceWarningOn(false);
      Trace.log(Trace.INFORMATION, "variation16");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String) logs a message when diagnostic tracing is enabled.
**/
  public void variation024()
  {
    setVariation(24);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v17");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(true);
      String msg = "variation17";
      Trace.log(Trace.DIAGNOSTIC, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String) logs a message when error tracing is enabled.
**/
  public void variation025()
  {
    setVariation(25);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v18");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      String msg = "variation18";
      Trace.log(Trace.ERROR, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String) logs a message when all tracing is enabled.
**/
  public void variation026()
  {
    setVariation(26);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v18");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation18";
      Trace.log(Trace.ERROR, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String) logs a message when information tracing is enabled.
**/
  public void variation027()
  {
    setVariation(27);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v19");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(true);
      String msg = "variation19";
      Trace.log(Trace.INFORMATION, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String) logs a message when all tracing is enabled.
**/
  public void variation028()
  {
    setVariation(28);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v19");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation19";
      Trace.log(Trace.INFORMATION, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String) logs a message when warning tracing is enabled.
**/
  public void variation029()
  {
    setVariation(29);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v20");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceWarningOn(true);
      String msg = "variation20";
      Trace.log(Trace.WARNING, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String) logs a message when all tracing is enabled.
**/
  public void variation030()
  {
    setVariation(30);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v20");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation20";
      Trace.log(Trace.WARNING, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(int, String, int) throws IllegalArgumentException if the category is invalid.
**/
  public void variation031()
  {
    setVariation(31);
    Trace.setTraceOn(true);
    try
    {
      Trace.log(Trace.DATASTREAM - 1, "hello", 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", 1);           // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

/**
Ensure that log(int, String, int) throws IllegalArgumentException if the category is Trace.ALL.
**/
  public void variation032()
  {
    setVariation(32);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.ALL, "hello", 1);
      Trace.log(100,       "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", 1);           // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

/**
Ensure that log(int, String, int) throws IllegalArgumentException if the category is Trace.THREAD.
**/
  public void variation033()
  {
    setVariation(33);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.THREAD, "hello", 1);
      Trace.log(99, "hello", 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", 1);           // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

/**
Ensure that log(int, String, int) throws no Exception if the message is null.
**/
  public void variation034()
  {
    setVariation(34);
    try
    {
      Trace.log(Trace.ERROR, (String) null, 1);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


/**
Ensure that log(XXX, String, int) does nothing if tracing is off but all categories are on.
**/
  public void variation035()
  {
    setVariation(35);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v23", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v23");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variationall", 1);
      Trace.log(Trace.DATASTREAM, "variationall", 1);
      Trace.log(Trace.ERROR, "variationall", 1);
      Trace.log(Trace.INFORMATION, "variationall", 1);
      Trace.log(Trace.WARNING, "variationall", 1);
      Trace.log(Trace.CONVERSION, "variationall", 1);
      Trace.log(Trace.PROXY, "variationall", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, int) does nothing if tracing is off but the diagnostic category is on.
**/
  public void variation036()
  {
    setVariation(36);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v23", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v23");
      Trace.setTraceOn(false);
      Trace.setTraceDiagnosticOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation23", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, int) does nothing if tracing is on but all categories are off.
**/
  public void variation037()
  {
    setVariation(37);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v24", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v24");               // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variationall", 1);
      Trace.log(Trace.DATASTREAM, "variationall", 1);
      Trace.log(Trace.ERROR, "variationall", 1);
      Trace.log(Trace.INFORMATION, "variationall", 1);
      Trace.log(Trace.WARNING, "variationall", 1);
      Trace.log(Trace.CONVERSION, "variationall", 1);
      Trace.log(Trace.PROXY, "variationall", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, int) does nothing if tracing is on but the diagnostic category is off.
**/
  public void variation038()
  {
    setVariation(38);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v24", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v24");               // @C0C
      Trace.setTraceDiagnosticOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation24", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, int) does nothing if tracing is off but the error category is on.
**/
  public void variation039()
  {
    setVariation(39);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v25", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v25");
      Trace.setTraceOn(false);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "variation25", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, int) does nothing if tracing is on but the error category is off.
**/
  public void variation040()
  {
    setVariation(40);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v26", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v26");           // @C0C
      Trace.setTraceErrorOn(false);
      Trace.log(Trace.ERROR, "variation26", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, int) does nothing if tracing is off but the information category is on.
**/
  public void variation041()
  {
    setVariation(41);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v27", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v27");
      Trace.setTraceOn(false);
      Trace.setTraceInformationOn(true);
      Trace.log(Trace.INFORMATION, "variation27", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, int) does nothing if tracing is on but the information category is off.
**/
  public void variation042()
  {
    setVariation(42);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v28", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                           // @C0C
      Trace.setFileName("TraceMiscTestcase.v28");                       // @C0C
      Trace.setTraceInformationOn(false);
      Trace.log(Trace.INFORMATION, "variation28", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, int) does nothing if tracing is off but the warning category is on.
**/
  public void variation043()
  {
    setVariation(43);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v29", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v29");
      Trace.setTraceOn(false);
      Trace.setTraceWarningOn(true);
      Trace.log(Trace.WARNING, "variation29", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, int) does nothing if tracing is on but the warning category is off.
**/
  public void variation044()
  {
    setVariation(44);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v30", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v30");           // @C0C
      Trace.setTraceWarningOn(false);
      Trace.log(Trace.INFORMATION, "variation30", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, int) logs a message when diagnostic tracing is enabled.
**/
  public void variation045()
  {
    setVariation(45);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v31");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(true);
      String msg = "variation31";
      int value = 31313131;
      Trace.log(Trace.DIAGNOSTIC, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, int) logs a message when all tracing is enabled.
**/
  public void variation046()
  {
    setVariation(46);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v31");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation31";
      int value = 31313131;
      Trace.log(Trace.DIAGNOSTIC, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, int) logs a message when error tracing is enabled.
**/
  public void variation047()
  {
    setVariation(47);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v32");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      String msg = "variation32";
      int value = 32323232;
      Trace.log(Trace.ERROR, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, int) logs a message when all tracing is enabled.
**/
  public void variation048()
  {
    setVariation(48);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v32");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation32";
      int value = 32323232;
      Trace.log(Trace.ERROR, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, int) logs a message when information tracing is enabled.
**/
  public void variation049()
  {
    setVariation(49);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v33");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(true);
      String msg = "variation33";
      int value = 33333333;
      Trace.log(Trace.INFORMATION, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, int) logs a message when all tracing is enabled.
**/
  public void variation050()
  {
    setVariation(50);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v33");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation33";
      int value = 33333333;
      Trace.log(Trace.INFORMATION, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, int) logs a message when warning tracing is enabled.
**/
  public void variation051()
  {
    setVariation(51);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v34");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceWarningOn(true);
      String msg = "variation34";
      int value = 34343434;
      Trace.log(Trace.WARNING, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, int) logs a message when all tracing is enabled.
**/
  public void variation052()
  {
    setVariation(52);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v34");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation34";
      int value = 34343434;
      Trace.log(Trace.WARNING, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(int, String, boolean) throws IllegalArgumentException if the category is invalid.
**/
  public void variation053()
  {
    setVariation(53);
    Trace.setTraceOn(true);
    try
    {
      Trace.log(Trace.DATASTREAM - 1, "hello", true);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", true);                // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, boolean) throws IllegalArgumentException if the category is Trace.ALL.
**/
  public void variation054()
  {
    setVariation(54);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.ALL, "hello", true);
      Trace.log(100,       "hello", true);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", true);                // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, boolean) throws IllegalArgumentException if the category is Trace.THREAD.
**/
  public void variation055()
  {
    setVariation(55);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.THREAD, "hello", true);
      Trace.log(99, "hello", true);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", true);                // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, boolean) throws no Exception if the message is null.
**/
  public void variation056()
  {
    setVariation(56);
    try
    {
      Trace.log(Trace.ERROR, (String) null, true);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


/**
Ensure that log(DIAGNOSTIC, String, boolean) does nothing if tracing is off but the diagnostic category is on.
**/
  public void variation057()
  {
    setVariation(57);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v37", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v37");
      Trace.setTraceOn(false);
      Trace.setTraceDiagnosticOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation37", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, boolean) does nothing if tracing is off but all categories are on.
**/
  public void variation058()
  {
    setVariation(58);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v37", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v37");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation37", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, boolean) does nothing if tracing is on but the diagnostic category is off.
**/
  public void variation059()
  {
    setVariation(59);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v38", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v38");               // @C0C
      Trace.setTraceDiagnosticOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation38", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, boolean) does nothing if tracing is on but the all categories are off.
**/
  public void variation060()
  {
    setVariation(60);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v38", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v38");               // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation38", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, boolean) does nothing if tracing is off but the error category is on.
**/
  public void variation061()
  {
    setVariation(61);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v39", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v39");
      Trace.setTraceOn(false);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "variation39", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, boolean) does nothing if tracing is off but all categories are on.
**/
  public void variation062()
  {
    setVariation(62);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v39", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v39");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.ERROR, "variation39", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, boolean) does nothing if tracing is on but the error category is off.
**/
  public void variation063()
  {
    setVariation(63);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v40", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v40");               // @C0C
      Trace.setTraceErrorOn(false);
      Trace.log(Trace.ERROR, "variation40", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, boolean) does nothing if tracing is on but all categories are off.
**/
  public void variation064()
  {
    setVariation(64);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v40", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v40");               // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.ERROR, "variation40", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, boolean) does nothing if tracing is off but the information category is on.
**/
  public void variation065()
  {
    setVariation(65);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v41", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v41");
      Trace.setTraceOn(false);
      Trace.setTraceInformationOn(true);
      Trace.log(Trace.INFORMATION, "variation41", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, boolean) does nothing if tracing is off but all categories are on.
**/
  public void variation066()
  {
    setVariation(66);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v41", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v41");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.INFORMATION, "variation41", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, boolean) does nothing if tracing is on but the information category is off.
**/
  public void variation067()
  {
    setVariation(67);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v42", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v42");           // @C0C
      Trace.setTraceInformationOn(false);
      Trace.log(Trace.INFORMATION, "variation42", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, boolean) does nothing if tracing is on but all categories are off.
**/
  public void variation068()
  {
    setVariation(68);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v42", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v42");           // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.INFORMATION, "variation42", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, boolean) does nothing if tracing is off but the warning category is on.
**/
  public void variation069()
  {
    setVariation(69);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v43", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v43");
      Trace.setTraceOn(false);
      Trace.setTraceWarningOn(true);
      Trace.log(Trace.WARNING, "variation43", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, boolean) does nothing if tracing is off but all categories are on.
**/
  public void variation070()
  {
    setVariation(70);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v43", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v43");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.WARNING, "variation43", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, boolean) does nothing if tracing is on but the warning category is off.
**/
  public void variation071()
  {
    setVariation(71);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v44", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setTraceAllOn(false);                               // @C1A
      Trace.setFileName("TraceMiscTestcase.v44");               // @C0C
      Trace.setTraceWarningOn(false);
      Trace.log(Trace.INFORMATION, "variation44", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, boolean) does nothing if tracing is on but all categories are off.
**/
  public void variation072()
  {
    setVariation(72);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v44", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v44");               // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.INFORMATION, "variation44", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, boolean) logs a message when diagnostic tracing is enabled.
**/
  public void variation073()
  {
    setVariation(73);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v45");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(true);
      String msg = "variation45";
      boolean value = true;
      Trace.log(Trace.DIAGNOSTIC, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, boolean) logs a message when all tracing is enabled.
**/
  public void variation074()
  {
    setVariation(74);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v45");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation45";
      boolean value = true;
      Trace.log(Trace.DIAGNOSTIC, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, boolean) logs a message when error tracing is enabled.
**/
  public void variation075()
  {
    setVariation(75);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v46");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      String msg = "variation32";
      boolean value = false;
      Trace.log(Trace.ERROR, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, boolean) logs a message when all tracing is enabled.
**/
  public void variation076()
  {
    setVariation(76);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v46");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation32";
      boolean value = false;
      Trace.log(Trace.ERROR, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, boolean) logs a message when information tracing is enabled.
**/
  public void variation077()
  {
    setVariation(77);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v47");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(true);
      String msg = "variation33";
      boolean value = true;
      Trace.log(Trace.INFORMATION, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, boolean) logs a message when all tracing is enabled.
**/
  public void variation078()
  {
    setVariation(78);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v47");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation33";
      boolean value = true;
      Trace.log(Trace.INFORMATION, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, boolean) logs a message when warning tracing is enabled.
**/
  public void variation079()
  {
    setVariation(79);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v48");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceWarningOn(true);
      String msg = "variation34";
      boolean value = false;
      Trace.log(Trace.WARNING, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, boolean) logs a message when all tracing is enabled.
**/
  public void variation080()
  {
    setVariation(80);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v48");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation34";
      boolean value = false;
      Trace.log(Trace.WARNING, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(int, String, byte[]) throws IllegalArgumentException if the category is invalid.
**/
  public void variation081()
  {
    setVariation(81);
    Trace.setTraceOn(true);
    try
    {
      Trace.log(Trace.DATASTREAM - 1, "hello", new byte[1]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new byte[1]);             // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, byte[]) throws IllegalArgumentException if the category is Trace.ALL.
**/
  public void variation082()
  {
    setVariation(82);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.ALL, "hello", new byte[1]);
      Trace.log(100, "hello", new byte[1]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new byte[1]);             // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, byte[]) throws IllegalArgumentException if the category is Trace.THREAD.
**/
  public void variation083()
  {
    setVariation(83);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.THREAD, "hello", new byte[1]);
      Trace.log(99, "hello", new byte[1]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new byte[1]);             // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, byte[]) throws no Exception if the message is null.
**/
  public void variation084()
  {
    setVariation(84);
    try
    {
      Trace.log(Trace.ERROR, (String) null, new byte[0]);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


/**
Ensure that log(int, String, byte[]) throws NullPointerException if the data is null.
**/
  public void variation085()
  {
    setVariation(85);
    try
    {
      Trace.log(Trace.ERROR, "hello", (byte[]) null);
      succeeded();
    }
    catch (Exception e)
    {
      failed(e);
    }
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[]) does nothing if tracing is off but the diagnostic category is on.
**/
  public void variation086()
  {
    setVariation(86);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v52", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v52");
      Trace.setTraceOn(false);
      Trace.setTraceDiagnosticOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation52", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[]) does nothing if tracing is off but all categories are on.
**/
  public void variation087()
  {
    setVariation(87);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v52", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v52");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation52", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[]) does nothing if tracing is on but the diagnostic category is off.
**/
  public void variation088()
  {
    setVariation(88);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v53", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v53");               // @C0C
      Trace.setTraceDiagnosticOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation53", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[]) does nothing if tracing is on but all categories are off.
**/
  public void variation089()
  {
    setVariation(89);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v53", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v53");               // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation53", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[]) does nothing if tracing is off but the error category is on.
**/
  public void variation090()
  {
    setVariation(90);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v54", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v54");
      Trace.setTraceOn(false);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "variation54", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[]) does nothing if tracing is off but all categories are on.
**/
  public void variation091()
  {
    setVariation(91);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v54", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v54");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.ERROR, "variation54", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[]) does nothing if tracing is on but the error category is off.
**/
  public void variation092()
  {
    setVariation(92);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v55", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                           // @C0C
      Trace.setFileName("TraceMiscTestcase.v55");       // @C0C
      Trace.setTraceErrorOn(false);
      Trace.log(Trace.ERROR, "variation55", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[]) does nothing if tracing is on but all categories are off.
**/
  public void variation093()
  {
    setVariation(93);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v55", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                           // @C0C
      Trace.setFileName("TraceMiscTestcase.v55");       // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.ERROR, "variation55", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[]) does nothing if tracing is off but the information category is on.
**/
  public void variation094()
  {
    setVariation(94);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v56", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v56");
      Trace.setTraceOn(false);
      Trace.setTraceInformationOn(true);
      Trace.log(Trace.INFORMATION, "variation56", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[]) does nothing if tracing is off but all categories are on.
**/
  public void variation095()
  {
    setVariation(95);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v56", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v56");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.INFORMATION, "variation56", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[]) does nothing if tracing is on but the information category is off.
**/
  public void variation096()
  {
    setVariation(96);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v57", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v57");               // @C0C
      Trace.setTraceInformationOn(false);
      Trace.log(Trace.INFORMATION, "variation57", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[]) does nothing if tracing is on but all categories are off.
**/
  public void variation097()
  {
    setVariation(97);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v57", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                   // @C0C
      Trace.setFileName("TraceMiscTestcase.v57");               // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.INFORMATION, "variation57", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[]) does nothing if tracing is off but the warning category is on.
**/
  public void variation098()
  {
    setVariation(98);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v58", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v58");
      Trace.setTraceOn(false);
      Trace.setTraceWarningOn(true);
      Trace.log(Trace.WARNING, "variation58", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[]) does nothing if tracing is off but all categories are on.
**/
  public void variation099()
  {
    setVariation(99);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v58", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v58");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.WARNING, "variation58", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[1]) does nothing if tracing is on but the warning category is off.
**/
  public void variation100()
  {
    setVariation(100);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v59", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                           // @C0C
      Trace.setTraceAllOn(false);                               // @C1A
      Trace.setFileName("TraceMiscTestcase.v59");       // @C0C
      Trace.setTraceWarningOn(false);
      Trace.log(Trace.INFORMATION, "variation59", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[1]) does nothing if tracing is on but all categories are off.
**/
  public void variation101()
  {
    setVariation(101);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v59", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                           // @C0C
      Trace.setFileName("TraceMiscTestcase.v59");       // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.INFORMATION, "variation59", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[]) logs a message when diagnostic tracing is enabled.
**/
  public void variation102()
  {
    setVariation(102);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v60");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(true);
      String msg = "variation60";
      byte[] data = { 16, 17, 18, 19};
      Trace.log(Trace.DIAGNOSTIC, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[]) logs a message when all tracing is enabled.
**/
  public void variation103()
  {
    setVariation(103);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v60");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation60";
      byte[] data = { 16, 17, 18, 19};
      Trace.log(Trace.DIAGNOSTIC, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[]) logs a message when error tracing is enabled.
**/
  public void variation104()
  {
    setVariation(104);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v61");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      String msg = "variation61";
      byte[] data = { 20, 21, 22, 23};
      Trace.log(Trace.ERROR, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[]) logs a message when all tracing is enabled.
**/
  public void variation105()
  {
    setVariation(105);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v61");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation61";
      byte[] data = { 20, 21, 22, 23};
      Trace.log(Trace.ERROR, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[]) logs a message when information tracing is enabled.
**/
  public void variation106()
  {
    setVariation(106);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v62");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(true);
      String msg = "variation62";
      byte[] data = { 24, 25, 26, 27};
      Trace.log(Trace.INFORMATION, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[]) logs a message when all tracing is enabled.
**/
  public void variation107()
  {
    setVariation(107);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v62");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation62";
      byte[] data = { 24, 25, 26, 27};
      Trace.log(Trace.INFORMATION, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[]) logs a message when warning tracing is enabled.
**/
  public void variation108()
  {
    setVariation(108);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v63");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceWarningOn(true);
      String msg = "variation63";
      byte[] data = { 28, 29, 30, 31};
      Trace.log(Trace.WARNING, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[]) logs a message when all tracing is enabled.
**/
  public void variation109()
  {
    setVariation(109);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v63");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation63";
      byte[] data = { 28, 29, 30, 31};
      Trace.log(Trace.WARNING, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(int, String, byte[], int ,int) throws IllegalArgumentException if the category is invalid.
**/
  public void variation110()
  {
    setVariation(110);
    Trace.setTraceOn(true);
    try
    {
      Trace.log(Trace.DATASTREAM - 1, "hello", new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new byte[1], 0, 1);       // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, byte[], int ,int) throws IllegalArgumentException if the category is Trace.ALL.
**/
  public void variation111()
  {
    setVariation(111);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.ALL, "hello", new byte[1], 0, 1);
      Trace.log(100, "hello", new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new byte[1], 0, 1);       // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, byte[], int ,int) throws IllegalArgumentException if the category is Trace.THREAD.
**/
  public void variation112()
  {
    setVariation(112);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.THREAD, "hello", new byte[1], 0, 1);
      Trace.log(99, "hello", new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new byte[1], 0, 1);       // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information.\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }


/**
Ensure that log(int, String, byte[], int, int) throws no Exception if the message is null.
**/
  public void variation113()
  {
    setVariation(113);
    try
    {
      Trace.log(Trace.ERROR, (String) null, new byte[1], 0, 1);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


/**
Ensure that log(int, String, byte[], int, int) throws no NullPointerException if the data is null.
**/
  public void variation114()
  {
    setVariation(114);
    try
    {
      Trace.log(Trace.ERROR, "hello", (byte[]) null, 0, 1);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[], int, int) does nothing if tracing is off but the diagnostic category is on.
**/
  public void variation115()
  {
    setVariation(115);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v67", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v67");
      Trace.setTraceOn(false);
      Trace.setTraceDiagnosticOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation67", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[], int, int) does nothing if tracing is off but all categories are on.
**/
  public void variation116()
  {
    setVariation(116);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v67", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v67");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation67", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[], int, int) does nothing if tracing is on but the diagnostic category is off.
**/
  public void variation117()
  {
    setVariation(117);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v68", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                           // @C0C
      Trace.setFileName("TraceMiscTestcase.v68");       // @C0C
      Trace.setTraceDiagnosticOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation68", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[], int, int) does nothing if tracing is on but all categories are off.
**/
  public void variation118()
  {
    setVariation(118);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v68", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                           // @C0C
      Trace.setFileName("TraceMiscTestcase.v68");       // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation68", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[], int, int) does nothing if tracing is off but the error category is on.
**/
  public void variation119()
  {
    setVariation(119);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v69", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v69");
      Trace.setTraceOn(false);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "variation69", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[], int, int) does nothing if tracing is off but all categories are on.
**/
  public void variation120()
  {
    setVariation(120);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v69", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v69");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.ERROR, "variation69", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[], int, int) does nothing if tracing is on but the error category is off.
**/
  public void variation121()
  {
    setVariation(121);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v70", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v70");           // @C0C
      Trace.setTraceErrorOn(false);
      Trace.log(Trace.ERROR, "variation70", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[], int, int) does nothing if tracing is on but all categories are off.
**/
  public void variation122()
  {
    setVariation(122);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v70", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v70");           // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.ERROR, "variation70", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[], int, int) does nothing if tracing is off but the information category is on.
**/
  public void variation123()
  {
    setVariation(123);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v71", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v71");
      Trace.setTraceOn(false);
      Trace.setTraceInformationOn(true);
      Trace.log(Trace.INFORMATION, "variation71", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[], int, int) does nothing if tracing is off but all categories are on.
**/
  public void variation124()
  {
    setVariation(124);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v71", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v71");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.INFORMATION, "variation71", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[], int, int) does nothing if tracing is on but the information category is off.
**/
  public void variation125()
  {
    setVariation(125);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v72", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v72");           // @C0C
      Trace.setTraceInformationOn(false);
      Trace.log(Trace.INFORMATION, "variation72", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[], int, int) does nothing if tracing is on but all categories are off.
**/
  public void variation126()
  {
    setVariation(126);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v72", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                               // @C0C
      Trace.setFileName("TraceMiscTestcase.v72");           // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.INFORMATION, "variation72", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[], int, int) does nothing if tracing is off but the warning category is on.
**/
  public void variation127()
  {
    setVariation(127);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v73", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v73");
      Trace.setTraceOn(false);
      Trace.setTraceWarningOn(true);
      Trace.log(Trace.WARNING, "variation73", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[], int, int) does nothing if tracing is off but all categories are on.
**/
  public void variation128()
  {
    setVariation(128);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v73", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v73");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(Trace.WARNING, "variation73", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[], int, int) does nothing if tracing is on but the warning category is off.
**/
  public void variation129()
  {
    setVariation(129);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v74", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                       // @C0C
      Trace.setTraceAllOn(false);                               // @C1A
      Trace.setFileName("TraceMiscTestcase.v74");                   // @C0C
      Trace.setTraceWarningOn(false);
      Trace.log(Trace.INFORMATION, "variation74", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[], int, int) does nothing if tracing is on but all categories are off.
**/
  public void variation130()
  {
    setVariation(130);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v74", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                       // @C0C
      Trace.setFileName("TraceMiscTestcase.v74");                   // @C0C
      Trace.setTraceAllOn(false);
      Trace.log(Trace.INFORMATION, "variation74", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[], int, int) logs a message when diagnostic tracing is enabled.
**/
  public void variation131()
  {
    setVariation(131);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v75");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(true);
      String msg = "variation75";
      byte[] data = { 32, 33, 34, 35};
      Trace.log(Trace.DIAGNOSTIC, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(DIAGNOSTIC, String, byte[], int, int) logs a message when all tracing is enabled.
**/
  public void variation132()
  {
    setVariation(132);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v75");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation75";
      byte[] data = { 32, 33, 34, 35};
      Trace.log(Trace.DIAGNOSTIC, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[], int, int) logs a message when error tracing is enabled.
**/
  public void variation133()
  {
    setVariation(133);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v76");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      String msg = "variation76";
      byte[] data = { 36, 37, 38, 39};
      Trace.log(Trace.ERROR, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(ERROR, String, byte[], int, int) logs a message when all tracing is enabled.
**/
  public void variation134()
  {
    setVariation(134);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v76");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation76";
      byte[] data = { 36, 37, 38, 39};
      Trace.log(Trace.ERROR, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[], int, int) logs a message when information tracing is enabled.
**/
  public void variation135()
  {
    setVariation(135);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v77");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(true);
      String msg = "variation77";
      byte[] data = { 40, 41, 42, 43};
      Trace.log(Trace.INFORMATION, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(INFORMATION, String, byte[], int, int) logs a message when all tracing is enabled.
**/
  public void variation136()
  {
    setVariation(136);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v77");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation77";
      byte[] data = { 40, 41, 42, 43};
      Trace.log(Trace.INFORMATION, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[], int, int) logs a message when warning tracing is enabled.
**/
  public void variation137()
  {
    setVariation(137);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v78");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceWarningOn(true);
      String msg = "variation78";
      byte[] data = { 44, 45, 46, 47};
      Trace.log(Trace.WARNING, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that log(WARNING, String, byte[], int, int) logs a message when all tracing is enabled.
**/
  public void variation138()
  {
    setVariation(138);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v78");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(true);
      String msg = "variation78";
      byte[] data = { 44, 45, 46, 47};
      Trace.log(Trace.WARNING, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that setFileName(String) appends to an existing file.
**/
  public void variation139()
  {
    setVariation(139);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v79",
                                                   "rw");
      String msg = "The quick brown fox jumped over the lazy dog.";
      file.writeBytes(msg);
      file.close();
      file = new RandomAccessFile("TraceMiscTestcase.v79", "rw");
      int lengthBefore = (int) file.length();
      Trace.setFileName("TraceMiscTestcase.v79");
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "hello");
      String line = file.readLine();
      assertCondition(line.indexOf(msg) != -1 && file.length() > lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that setTraceConversionOn(boolean) and isTraceConversionOn() agree.
   **/
  public void variation140()
  {
    setVariation(140);

    Trace.setTraceConversionOn(true);
    if (Trace.isTraceConversionOn())
    {
      Trace.setTraceConversionOn(false);
      assertCondition(!Trace.isTraceConversionOn());
    }
    else
    {
      failed("Trace.isTraceConversionOn() returns incorrect value.");
    }
  }

  /**
    Ensure that log(int, String, Throwable) throws IllegalArgumentException if the category is invalid.
   **/
  public void variation141()
  {
    setVariation(141);
    Trace.setTraceOn(true);
    try
    {
      Trace.log(Trace.DATASTREAM - 1, "hello", new Throwable());
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new Throwable());     // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  /**
    Ensure that log(int, String, Throwable) throws IllegalArgumentException if the category is Trace.ALL.
   **/
  public void variation142()
  {
    setVariation(142);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.ALL, "hello", new Throwable());
      Trace.log(100, "hello", new Throwable());
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new Throwable());     // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  /**
    Ensure that log(int, String, Throwable) throws IllegalArgumentException if the category is Trace.THREAD.
   **/
  public void variation143()
  {
    setVariation(143);
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(Trace.THREAD, "hello", new Throwable());
      Trace.log(99, "hello", new Throwable());
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(HIGHEST + 1, "hello", new Throwable());     // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  /**
    Ensure that log(int, String, Throwable) throws no Exception if the message is null.
   **/
  public void variation144()
  {
    setVariation(144);
    try
    {
      Trace.log(Trace.ERROR, (String) null, new Throwable());
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  /**
    Ensure that log(int, String, Throwable) throws no Exception if the throwable is null.
   **/
  public void variation145()
  {
    setVariation(145);
    try
    {
      Trace.log(Trace.ERROR, "Hello", (Throwable)null);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  /**
    Ensure that log(CONVERSION, String) does nothing if tracing is off but the conversion category is on.
   **/
  public void variation146()
  {
    setVariation(146);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v84", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v84");
      Trace.setTraceOn(false);
      Trace.setTraceConversionOn(true);
      Trace.log(Trace.CONVERSION, "variation84");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String) does nothing if tracing is on but the conversion category is off.
   **/
  public void variation147()
  {
    setVariation(147);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v85", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v85");          // @C0C
      Trace.setTraceConversionOn(false);
      Trace.log(Trace.CONVERSION, "variation85");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String) logs a message when conversion tracing is enabled.
   **/
  public void variation148()
  {
    setVariation(148);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v86");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(true);
      String msg = "variation86";
      Trace.log(Trace.CONVERSION, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, int) does nothing if tracing is off but the conversion category is on.
   **/
  public void variation149()
  {
    setVariation(149);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v87", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v87");
      Trace.setTraceOn(false);
      Trace.setTraceConversionOn(true);
      Trace.log(Trace.CONVERSION, "variation87", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, int) does nothing if tracing is on but the conversion category is off.
   **/
  public void variation150()
  {
    setVariation(150);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v88", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v88");          // @C0C
      Trace.setTraceConversionOn(false);
      Trace.log(Trace.CONVERSION, "variation88", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, int) logs a message when conversion tracing is enabled.
   **/
  public void variation151()
  {
    setVariation(151);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v89");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(true);
      String msg = "variation89";
      int value = 31313131;
      Trace.log(Trace.CONVERSION, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 && data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, boolean) does nothing if tracing is off but the conversion category is on.
   **/
  public void variation152()
  {
    setVariation(152);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v90", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v90");
      Trace.setTraceOn(false);
      Trace.setTraceConversionOn(true);
      Trace.log(Trace.CONVERSION, "variation90", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, boolean) does nothing if tracing is on but the conversion category is off.
   **/
  public void variation153()
  {
    setVariation(153);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v91", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v91");          // @C0C
      Trace.setTraceConversionOn(false);
      Trace.log(Trace.CONVERSION, "variation91", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, boolean) logs a message when conversion tracing is enabled.
   **/
  public void variation154()
  {
    setVariation(154);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v92");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(true);
      String msg = "variation92";
      boolean value = true;
      Trace.log(Trace.CONVERSION, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 && data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, byte[]) does nothing if tracing is off but the conversion category is on.
   **/
  public void variation155()
  {
    setVariation(155);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v93", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v93");
      Trace.setTraceOn(false);
      Trace.setTraceConversionOn(true);
      Trace.log(Trace.CONVERSION, "variation93", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, byte[]) does nothing if tracing is on but the conversion category is off.
   **/
  public void variation156()
  {
    setVariation(156);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v94", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                      // @C0C
      Trace.setFileName("TraceMiscTestcase.v94");                  // @C0C
      Trace.setTraceConversionOn(false);
      Trace.log(Trace.CONVERSION, "variation94", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, byte[]) logs a message when conversion tracing is enabled.
   **/
  public void variation157()
  {
    setVariation(157);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v95");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(true);
      String msg = "variation95";
      byte[] data = { 16, 17, 18, 19};
      Trace.log(Trace.CONVERSION, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 && lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 && lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, byte[], int, int) does nothing if tracing is off but the conversion category is on.
   **/
  public void variation158()
  {
    setVariation(158);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v96", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v96");
      Trace.setTraceOn(false);
      Trace.setTraceConversionOn(true);
      Trace.log(Trace.CONVERSION, "variation96", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, byte[], int, int) does nothing if tracing is on but the conversion category is off.
   **/
  public void variation159()
  {
    setVariation(159);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v97", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v97");          // @C0C
      Trace.setTraceConversionOn(false);
      Trace.log(Trace.CONVERSION, "variation97", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, byte[], int, int) logs a message when conversion tracing is enabled.
   **/
  public void variation160()
  {
    setVariation(160);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v98");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(true);
      String msg = "variation98";
      byte[] data = { 32, 33, 34, 35};
      Trace.log(Trace.CONVERSION, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 && lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 && lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(DIAGNOSTIC, String, Throwable) does nothing if tracing is off but the diagnostic category is on.
   **/
  public void variation161()
  {
    setVariation(161);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v99", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v99");
      Trace.setTraceOn(false);
      Trace.setTraceDiagnosticOn(true);
      Trace.log(Trace.DIAGNOSTIC, "variation99", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(DIAGNOSTIC, String, Throwable) does nothing if tracing is on but the diagnostic category is off.
   **/
  public void variation162()
  {
    setVariation(162);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v100", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                      // @C0C
      Trace.setFileName("TraceMiscTestcase.v100");                 // @C0C
      Trace.setTraceDiagnosticOn(false);
      Trace.log(Trace.DIAGNOSTIC, "variation100", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(ERROR, String, Throwable) does nothing if tracing is off but the error category is on.
   **/
  public void variation163()
  {
    setVariation(163);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v101", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v101");
      Trace.setTraceOn(false);
      Trace.setTraceErrorOn(true);
      Trace.log(Trace.ERROR, "variation101", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(ERROR, String, Throwable) does nothing if tracing is on but the error category is off.
   **/
  public void variation164()
  {
    setVariation(164);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v102", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v102");                         // @C0C
      Trace.setTraceErrorOn(false);
      Trace.log(Trace.ERROR, "variation102", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(INFORMATION, String, Throwable) does nothing if tracing is off but the information category is on.
   **/
  public void variation165()
  {
    setVariation(165);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v103", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v103");
      Trace.setTraceOn(false);
      Trace.setTraceInformationOn(true);
      Trace.log(Trace.INFORMATION, "variation103", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(INFORMATION, String, Throwable) does nothing if tracing is on but the information category is off.
   **/
  public void variation166()
  {
    setVariation(166);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v104", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v104");         // @C0C
      Trace.setTraceInformationOn(false);
      Trace.log(Trace.INFORMATION, "variation104", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(WARNING, String, Throwable) does nothing if tracing is off but the warning category is on.
   **/
  public void variation167()
  {
    setVariation(167);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v105", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v105");
      Trace.setTraceOn(false);
      Trace.setTraceWarningOn(true);
      Trace.log(Trace.WARNING, "variation105", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(WARNING, String, Throwable) does nothing if tracing is on but the warning category is off.
   **/
  public void variation168()
  {
    setVariation(168);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v106", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);                              // @C0C
      Trace.setFileName("TraceMiscTestcase.v106");         // @C0C
      Trace.setTraceWarningOn(false);
      Trace.log(Trace.INFORMATION, "variation106", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, Throwable) does nothing if tracing is off but the conversion category is on.
   **/
  public void variation169()
  {
    setVariation(169);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v107", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v107");
      Trace.setTraceOn(false);
      Trace.setTraceConversionOn(true);
      Trace.log(Trace.CONVERSION, "variation107", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, Throwable) does nothing if tracing is on but the conversion category is off.
   **/
  public void variation170()
  {
    setVariation(170);
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceMiscTestcase.v108", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v108");
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(false);
      Trace.log(Trace.CONVERSION, "variation108", new Throwable());
      assertCondition(file.length() <= lengthBefore + TRACE_HEADER_LENGTH, "file.length()="+file.length()+" sb <= "+(lengthBefore + TRACE_HEADER_LENGTH)+" lengthBefore="+lengthBefore+ " trace_header_length="+TRACE_HEADER_LENGTH);    // add length of version string @D2C @E1C
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(DIAGNOSTIC, String, Throwable) logs a message when diagnostic tracing is enabled.
   **/
  public void variation171()
  {
    setVariation(171);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v109");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceDiagnosticOn(true);
      String msg = "variation109";
      Trace.log(Trace.DIAGNOSTIC, msg, new Throwable());
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(ERROR, String, Throwable) logs a message when error tracing is enabled.
   **/
  public void variation172()
  {
    setVariation(172);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v110");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceErrorOn(true);
      String msg = "variation110";
      Trace.log(Trace.ERROR, msg, new Throwable());
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(INFORMATION, String, Throwable) logs a message when information tracing is enabled.
   **/
  public void variation173()
  {
    setVariation(173);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v111");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceInformationOn(true);
      String msg = "variation111";
      Trace.log(Trace.INFORMATION, msg, new Throwable());
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(WARNING, String, Throwable) logs a message when warning tracing is enabled.
   **/
  public void variation174()
  {
    setVariation(174);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v112");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceWarningOn(true);
      String msg = "variation112";
      Trace.log(Trace.WARNING, msg, new Throwable());
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that log(CONVERSION, String, Throwable) logs a message when conversion tracing is enabled.
   **/
  public void variation175()
  {
    setVariation(175);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v113");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceConversionOn(true);
      String msg = "variation113";
      Trace.log(Trace.CONVERSION, msg, new Throwable());
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

  /**
    Ensure that setPrintWriter and getPrintWriter agree.
   **/
  public void variation176()
  {
    setVariation(176);
    try
    {
      PrintWriter myPW = new PrintWriter(System.err, true);
      Trace.setPrintWriter(myPW);
      assertCondition(myPW == Trace.getPrintWriter());
    }
    catch (Exception e)
    {
      failed(e);
    }
  }



// @C0A
/**
Ensure that setTraceProxyOn(boolean) and isTraceProxyOn() agree.
**/
  public void variation177()
  {
    setVariation(177);
    Trace.setTraceProxyOn(true);
    if (Trace.isTraceProxyOn())
    {
      Trace.setTraceProxyOn(false);
      assertCondition(!Trace.isTraceProxyOn());
    }
    else
    {
      failed("Trace.isTraceProxyOn() returns incorrect value.");
    }
  }



// @C0A
/**
Ensure that log(PROXY, String) does nothing if tracing is off but the proxy category is on.
**/
  public void variation178()
  {
    setVariation(178);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v116", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v116");
      Trace.setTraceOn(false);
      Trace.setTraceProxyOn(true);
      Trace.log(Trace.PROXY, "variation116");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String) does nothing if tracing is on but the proxy category is off.
**/
  public void variation179()
  {
    setVariation(179);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v117", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);
      Trace.setFileName("TraceMiscTestcase.v117");
      Trace.setTraceProxyOn(false);
      Trace.log(Trace.PROXY, "variation117");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String) logs a message when proxy tracing is enabled.
**/
  public void variation180()
  {
    setVariation(180);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v118");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "rw");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceProxyOn(true);
      String msg = "variation118";
      Trace.log(Trace.PROXY, msg);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }
    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, int) does nothing if tracing is off but the proxy category is on.
**/
  public void variation181()
  {
    setVariation(181);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v119", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v119");
      Trace.setTraceOn(false);
      Trace.setTraceProxyOn(true);
      Trace.log(Trace.PROXY, "variation119", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, int) does nothing if tracing is on but the proxy category is off.
**/
  public void variation182()
  {
    setVariation(182);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v120", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);
      Trace.setFileName("TraceMiscTestcase.v120");
      Trace.setTraceProxyOn(false);
      Trace.log(Trace.PROXY, "variation120", 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, int) logs a message when proxy tracing is enabled.
**/
  public void variation183()
  {
    setVariation(183);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v121");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceProxyOn(true);
      String msg = "variation121";
      int value = -121121;
      Trace.log(Trace.PROXY, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(Integer.toString(value)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }

// @C0A
/**
Ensure that log(PROXY, String, boolean) does nothing if tracing is off but the proxy category is on.
**/
  public void variation184()
  {
    setVariation(184);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v122", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v122");
      Trace.setTraceOn(false);
      Trace.setTraceProxyOn(true);
      Trace.log(Trace.PROXY, "variation122", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, boolean) does nothing if tracing is on but the proxy category is off.
**/
  public void variation185()
  {
    setVariation(185);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v123", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);
      Trace.setFileName("TraceMiscTestcase.v123");
      Trace.setTraceProxyOn(false);
      Trace.log(Trace.PROXY, "variation123", true);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, boolean) logs a message when proxy tracing is enabled.
**/
  public void variation186()
  {
    setVariation(186);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v124");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceProxyOn(true);
      String msg = "variation124";
      boolean value = false;
      Trace.log(Trace.PROXY, msg, value);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &&
             data.indexOf(new Boolean(value).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }



// @C0A
/**
Ensure that log(PROXY, String, byte[]) does nothing if tracing is off but the proxy category is on.
**/
  public void variation187()
  {
    setVariation(187);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v125", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v125");
      Trace.setTraceOn(false);
      Trace.setTraceProxyOn(true);
      Trace.log(Trace.PROXY, "variation125", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, byte[]) does nothing if tracing is on but the proxy category is off.
**/
  public void variation188()
  {
    setVariation(188);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v126", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);
      Trace.setFileName("TraceMiscTestcase.v126");
      Trace.setTraceProxyOn(false);
      Trace.log(Trace.PROXY, "variation126", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, byte[]) logs a message when proxy tracing is enabled.
**/
  public void variation189()
  {
    setVariation(189);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v127");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceProxyOn(true);
      String msg = "variation127";
      byte[] data = { 20, 21, 22, 23};
      Trace.log(Trace.PROXY, msg, data);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }



// @C0A
/**
Ensure that log(PROXY, String, byte[], int, int) does nothing if tracing is off but the proxy category is on.
**/
  public void variation190()
  {
    setVariation(190);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v128", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v128");
      Trace.setTraceOn(false);
      Trace.setTraceProxyOn(true);
      Trace.log(Trace.PROXY, "variation128", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, byte[], int, int) does nothing if tracing is on but the proxy category is off.
**/
  public void variation191()
  {
    setVariation(191);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v129", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);
      Trace.setFileName("TraceMiscTestcase.v129");
      Trace.setTraceProxyOn(false);
      Trace.log(Trace.PROXY, "variation129", new byte[1], 0, 1);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, byte[], int, int) logs a message when proxy tracing is enabled.
**/
  public void variation192()
  {
    setVariation(192);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v130");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceProxyOn(true);
      String msg = "variation130";
      byte[] data = { 20, 21, 22, 23};
      Trace.log(Trace.PROXY, msg, data, 0, 4);
      String lines = "";
      String line = null;
      while ((line = file.readLine()) != null)
      {
        lines += line;
      }
      assertCondition(lines.indexOf(msg) != -1 &&
             lines.indexOf(Integer.toHexString(data[0]).toUpperCase()) != -1 &&
             lines.indexOf(Integer.toHexString(data[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }



// @C0A
/**
Ensure that log(PROXY, String, Throwable) does nothing if tracing is off but the proxy category is on.
**/
  public void variation193()
  {
    setVariation(193);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v131", "rw");
      long lengthBefore = file.length();
      Trace.setFileName("TraceMiscTestcase.v131");
      Trace.setTraceOn(false);
      Trace.setTraceProxyOn(true);
      Trace.log(Trace.PROXY, "variation131", new Throwable ());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, Throwable) does nothing if tracing is on but the proxy category is off.
**/
  public void variation194()
  {
    setVariation(194);
    try
    {
      RandomAccessFile file =
          new RandomAccessFile("TraceMiscTestcase.v132", "rw");
      long lengthBefore = file.length();
      Trace.setTraceOn(true);
      Trace.setFileName("TraceMiscTestcase.v132");
      Trace.setTraceProxyOn(false);
      Trace.log(Trace.PROXY, "variation132", new byte[1]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


// @C0A
/**
Ensure that log(PROXY, String, Thorwable) logs a message when proxy tracing is enabled.
**/
  public void variation195()
  {
    setVariation(195);
    try
    {
      Trace.setFileName("TraceMiscTestcase.v133");
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);
      Trace.setTraceProxyOn(true);
      String msg = "variation127";
      Trace.log(Trace.PROXY, msg, new Throwable());
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(Trace.getFileName());
  }


/**
Ensure that Trace.ALL and Trace.CONVERSION agree.
**/
  public void variation196()
  {
    setVariation(196);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceConversionOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceConversionOn(true);
      if (Trace.isTraceConversionOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceConversionOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceConversionOn(false);
          if (!Trace.isTraceConversionOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.CONVERSION do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.DIAGNOSTIC agree.
**/
  public void variation197()
  {
    setVariation(197);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceDiagnosticOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceDiagnosticOn(true);
      if (Trace.isTraceDiagnosticOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceDiagnosticOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceDiagnosticOn(false);
          if (!Trace.isTraceDiagnosticOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.DIAGNOSTIC do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.DATASTREAM agree.
**/
  public void variation198()
  {
    setVariation(198);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceDatastreamOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceDatastreamOn(true);
      if (Trace.isTraceDatastreamOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceDatastreamOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceDatastreamOn(false);
          if (!Trace.isTraceDatastreamOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.DATASTREAM do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.ERROR agree.
**/
  public void variation199()
  {
    setVariation(199);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceErrorOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceErrorOn(true);
      if (Trace.isTraceErrorOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceErrorOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceErrorOn(false);
          if (!Trace.isTraceErrorOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.ERROR do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.INFORMATION agree.
**/
  public void variation200()
  {
    setVariation(200);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceInformationOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceInformationOn(true);
      if (Trace.isTraceInformationOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceInformationOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceInformationOn(false);
          if (!Trace.isTraceInformationOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.INFORMATION do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.THREAD agree.
**/
  public void variation201()
  {
    setVariation(201);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceThreadOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceThreadOn(true);
      if (Trace.isTraceThreadOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceThreadOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceThreadOn(false);
          if (!Trace.isTraceThreadOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.THREAD do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.WARNING agree.
**/
  public void variation202()
  {
    setVariation(202);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceWarningOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceWarningOn(true);
      if (Trace.isTraceWarningOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceWarningOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceWarningOn(false);
          if (!Trace.isTraceWarningOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.WARNING do not agree.");
  }


/**
Ensure that Trace.ALL and Trace.PROXY agree.
**/
  public void variation203()
  {
    setVariation(203);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceProxyOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceProxyOn(true);
      if (Trace.isTraceProxyOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceProxyOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceProxyOn(false);
          if (!Trace.isTraceProxyOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.PROXY do not agree.");
  }


  //
  // Trace.getFileName(component) tests
  // ----------------------------------
  //
  public void variation204()
  {
    setVariation(204);                     // null should be returned if not set

    try
    {
       if (Trace.getFileName("Fred") != null)
          failed("string not null");
       else
          succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation205()
  {
    setVariation(205);                     // null should be returned if not set

    try
    {
       Trace.setFileName("Martha", "TraceVar205");

       if (Trace.getFileName("Fred") != null)
          failed("string not null");
       else
          succeeded();

       deleteFile("Martha", "TraceVar205");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }

  }

  public void variation206()
  {
    setVariation(206);                     // should get name back

    try
    {
       Trace.setFileName("Fred", "TraceVar206");

       if (Trace.getFileName("Fred").equalsIgnoreCase("TraceVar206"))
          succeeded();
       else
          failed("different string");

       deleteFile("Fred", "TraceVar206");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation207()
  {
    setVariation(207);                     // should get null after set then remove

    try
    {
       Trace.setFileName("Hoss",       "TraceVar207a");
       Trace.setFileName("Ben",        "TraceVar207b");
       Trace.setFileName("Little Joe", "TraceVar207c");

       Trace.setFileName("Ben",        null);

       if (Trace.getFileName("Ben") != null)
          failed("string not null");
       else
          succeeded();

       deleteFile("Hoss",       "TraceVar207a");
       deleteFile("Ben",        "TraceVar207b");
       deleteFile("Little Joe", "TraceVar207c");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation208()
  {
    setVariation(208);                     // should get null pointer exception

    try
    {
       Trace.getFileName(null);
    }
    catch (Throwable e)
    {
       if (exceptionIs((Exception)e, "NullPointerException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  public void variation209()
  {
    setVariation(209);                     // Object other than a string

    try
    {
       Calendar c = new GregorianCalendar();

       Trace.setFileName(c, "TraceVar209");

       if (Trace.getFileName(c).equalsIgnoreCase("TraceVar209"))
          succeeded();
       else
          failed("different string");

       deleteFile(c, "TraceVar209");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }



  //
  // Trace.getPrintWriter(component) tests
  // -------------------------------------
  //
  public void variation210()
  {
    setVariation(210);                     // null should be returned if not set

    try
    {
       if (Trace.getPrintWriter("Fred2") != null)
          failed("pw not null");
       else
          succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation211()
  {
    setVariation(211);                     // null should be returned if not set

    try
    {
       Trace.setFileName("Martha2", "TraceVar211");

       if (Trace.getPrintWriter("Fred2") != null)
          failed("pw not null");
       else
          succeeded();

       deleteFile("Martha2", "TraceVar211");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }

  }

  public void variation212()
  {
    setVariation(212);                     // should get name back

    try
    {
       Trace.setFileName("Fred", "TraceVar212");

       if (Trace.getPrintWriter("Fred") != null)
          succeeded();
       else
          failed("no pw");

       deleteFile("Fred", "TraceVar212");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation213()
  {
    setVariation(213);                     // should get pw after set then remove

    try
    {
       Trace.setFileName("Hoss",       "TraceVar213a");
       Trace.setFileName("Ben",        "TraceVar213b");
       Trace.setFileName("Little Joe", "TraceVar213c");

       Trace.setFileName("Ben",        null);

       if (Trace.getPrintWriter("Ben") != null)
          succeeded();
       else
          failed("pw not null");

       deleteFile("Hoss", "TraceVar213a");
       deleteFile("Ben", "TraceVar213b");
       deleteFile("Little Joe", "TraceVar213c");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation214()
  {
    setVariation(214);                     // should get null pointer exception

    try
    {
       PrintWriter pw = new PrintWriter(System.out, true);
       Trace.getPrintWriter(null);
       assertCondition(false, "Did not throw exception.  pw="+pw); 
    }
    catch (Throwable e)
    {
       if (exceptionIs((Exception)e, "NullPointerException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  //
  // Trace.setFileName(component) tests not tested in getFileName(component)
  // -----------------------------------------------------------------------
  //
  public void variation215()
  {
    setVariation(215);                     // should get null pointer exception

    try
    {
       Trace.setFileName(null, "c:\\a.a");
    }
    catch (Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  public void variation216()
  {
    setVariation(216);                     // should get second name back

    try
    {
       Trace.setFileName("Fred", "TraceVar216a");
       Trace.setFileName("Fred", "TraceVar216b");

       if (Trace.getFileName("Fred").equalsIgnoreCase("TraceVar216b"))
          succeeded();
       else
          failed("different string");

       deleteFile("Fred", "TraceVar216a");
       deleteFile("Fred", "TraceVar216b");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation217()
  {
    setVariation(217);                     // should get second name back

    try
    {
       Trace.setFileName("Fred", "TraceVar217a");
       Trace.setFileName("Fred", "TraceVar217b");
       Trace.setFileName("Fred", null);

       if (Trace.getFileName("Fred") == null)
          succeeded();
       else
          failed("not null");

       deleteFile("Fred", "TraceVar217a");
       deleteFile("Fred", "TraceVar217b");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  //
  // Trace.setPrintWriter(component) tests not tested in getPrintWriter(component)
  // -----------------------------------------------------------------------------
  //
  public void variation218()
  {
    setVariation(218);                     // should get null pointer exception

    try
    {
       PrintWriter pw = new PrintWriter(System.out, true);
       Trace.setPrintWriter(null, pw);
    }
    catch (Exception e)
    {
       if (exceptionIs(e, "NullPointerException"))
          succeeded();
       else
          failed("wrong exception");
    }
  }

  public void variation219()
  {
    setVariation(219);                     // should get null pointer exception

    try
    {
       Trace.setPrintWriter("Fred", null);
       succeeded();
    }
    catch (Exception e)
    {
       failed("wrong exception");
    }
  }

  public void variation220()
  {
    setVariation(220);

    try
    {
       PrintWriter pw  = new PrintWriter(System.out, true);
       PrintWriter pw2 = new PrintWriter(System.out, true);
       Trace.setPrintWriter("Fred", pw);
       Trace.setPrintWriter("Fred", pw2);

       if (Trace.getPrintWriter("Fred") == pw2)
          succeeded();
       else
          failed("wrong print writer");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation221()
  {
    setVariation(221);

    try
    {
       PrintWriter pw  = new PrintWriter(System.out, true);
       Trace.setFileName("Fred", "TraceVar221");
       Trace.setPrintWriter("Fred", pw);

       if (Trace.getPrintWriter("Fred") == pw)
          succeeded();
       else
          failed("wrong print writer");

       deleteFile("Fred", "TraceVar221");
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }


  //
  // Misc test cases
  // ---------------
  //
  public void variation222()
  {
    setVariation(222);
    String traceCmp = "var222";

    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar222", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(traceCmp, "TraceVar222");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(traceCmp, Trace.DIAGNOSTIC, "variation222");
      Trace.log(traceCmp, Trace.DATASTREAM, "variation222");
      Trace.log(traceCmp, Trace.ERROR, "variation222");
      Trace.log(traceCmp, Trace.INFORMATION, "variation222");
      Trace.log(traceCmp, Trace.WARNING, "variation222");
      Trace.log(traceCmp, Trace.CONVERSION, "variation222");
      Trace.log(traceCmp, Trace.PROXY, "variation222");

      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(traceCmp, Trace.getFileName(traceCmp));
  }

  public void variation223()
  {
    setVariation(223);
    String traceCmp = "var223";

    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar223", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(traceCmp, "TraceVar223");
      Trace.log(traceCmp, Trace.DIAGNOSTIC, "variation223");
      Trace.log(traceCmp, Trace.DATASTREAM, "variation223");
      Trace.log(traceCmp, Trace.ERROR, "variation223");
      Trace.log(traceCmp, Trace.INFORMATION, "variation223");
      Trace.log(traceCmp, Trace.WARNING, "variation223");
      Trace.log(traceCmp, Trace.CONVERSION, "variation223");
      Trace.log(traceCmp, Trace.PROXY, "variation223");

      assertCondition(file.length() == lengthBefore);

      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(traceCmp, Trace.getFileName(traceCmp));
  }


  //
  // Trace.log(component, category, string) test cases;
  // --------------------------------------------------
  //

  public void variation224()
  {
    setVariation(224);
    String cmp = "var224";
    Trace.setTraceOn(true);

    try
    {
      Trace.log(cmp, Trace.DATASTREAM - 1, "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello"); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation225()
  {
    setVariation(225);
    String cmp = "var225";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.ALL, "hello");
      Trace.log(cmp, 100, "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello"); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation226()
  {
    setVariation(226);
    String cmp = "var226";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.THREAD, "hello");
      Trace.log(cmp, 99, "hello");
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello"); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation227()
  {
    setVariation(227);
    String cmp = "var227";
    try
    {
      Trace.log(cmp, Trace.ERROR, (String) null);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation228()
  {
    setVariation(228);
    // String cmp = "var228";
    try
    {
      Trace.log(null, Trace.ERROR, "Hi Mom");
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation229()
  {
    setVariation(229);
    String cmp = "var229";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar229", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar229");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation229");
      Trace.log(cmp, Trace.DATASTREAM, "variation229");
      Trace.log(cmp, Trace.ERROR, "variation229");
      Trace.log(cmp, Trace.INFORMATION, "variation229");
      Trace.log(cmp, Trace.WARNING, "variation229");
      Trace.log(cmp, Trace.CONVERSION, "variation229");
      Trace.log(cmp, Trace.PROXY, "variation229");
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation230()
  {
    setVariation(230);
    String cmp = "var230";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar230", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar230");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation230");
      Trace.log(cmp, Trace.DATASTREAM, "variation230");
      Trace.log(cmp, Trace.ERROR, "variation230");
      Trace.log(cmp, Trace.INFORMATION, "variation230");
      Trace.log(cmp, Trace.WARNING, "variation230");
      Trace.log(cmp, Trace.CONVERSION, "variation230");
      Trace.log(cmp, Trace.PROXY, "variation230");
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, String message, boolean traceAll)
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, message);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation231()
  {
    setVariation(231);
    String cmp = "var231";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar231", false);
  }

  void traceOnCmpOff(Object cmp, int category, String variation)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, String variation)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation232()
  {
    setVariation(232);
    String cmp = "var232";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, "TraceVar232");
  }


  public void variation233()
  {
    setVariation(233);
    String cmp = "var233";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, "TraceVar233");
  }


  public void variation234()
  {
    setVariation(234);
    String cmp = "var234";
    traceOffCmpOn(cmp, Trace.ERROR, "TraceVar234");
  }


  public void variation235()
  {
    setVariation(235);
    String cmp = "var235";
    traceOnCmpOff(cmp, Trace.ERROR, "TraceVar235");
  }

  public void variation236()
  {
    setVariation(236);
    String cmp = "var236";
    traceOffCmpOn(cmp, Trace.INFORMATION, "TraceVar236");
  }


  public void variation237()
  {
    setVariation(237);
    String cmp = "var237";
    traceOnCmpOff(cmp, Trace.INFORMATION, "TraceVar237");
  }

  public void variation238()
  {
    setVariation(238);
    String cmp = "var238";
    traceOffCmpOn(cmp, Trace.WARNING, "TraceVar238");
  }


  public void variation239()
  {
    setVariation(239);
    String cmp = "var239";
    traceOnCmpOff(cmp, Trace.WARNING, "TraceVar239");
  }

  public void variation240()
  {
    setVariation(240);
    String cmp = "var240";
    logIt(cmp, Trace.ERROR, "TraceVar240", false);
  }

  public void variation241()
  {
    setVariation(241);
    String cmp = "var241";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar241", false);
  }

  public void variation242()
  {
    setVariation(242);
    String cmp = "var242";
    logIt(cmp, Trace.INFORMATION, "TraceVar242", false);
  }

  public void variation243()
  {
    setVariation(243);
    String cmp = "var243";
    logIt(cmp, Trace.WARNING, "TraceVar243", false);
  }

  public void variation244()
  {
    setVariation(244);
    String cmp = "var244";
    logIt(cmp, Trace.ERROR, "TraceVar244", true);
  }

  public void variation245()
  {
    setVariation(245);
    String cmp = "var245";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar245", true);
  }

  public void variation246()
  {
    setVariation(246);
    String cmp = "var246";
    logIt(cmp, Trace.INFORMATION, "TraceVar246", true);
  }

  public void variation247()
  {
    setVariation(247);
    String cmp = "var247";
    logIt(cmp, Trace.WARNING, "TraceVar247", true);
  }




  //
  // Trace.log(component, category, string, int) test cases;
  // --------------------------------------------------
  //

  public void variation248()
  {
    setVariation(248);
    String cmp = "var248";
    Trace.setTraceOn(true);

    try
    {
      Trace.log(cmp, Trace.DATASTREAM - 1, "hello", 123);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", 234); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation249()
  {
    setVariation(249);
    String cmp = "var249";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.ALL, "hello", 0);
      Trace.log(cmp, 100, "hello", 0);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", 1000); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation250()
  {
    setVariation(250);
    String cmp = "var250";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.THREAD, "hello", 111);
      Trace.log(cmp, 99, "hello", 111);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", 222); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation251()
  {
    setVariation(251);
    String cmp = "var2517";
    try
    {
      Trace.log(cmp, Trace.ERROR, (String) null, 12220);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation252()
  {
    setVariation(252);
    // String cmp = "var252";
    try
    {
      Trace.log(null, Trace.ERROR, "Hi Mom", 2222);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation253()
  {
    setVariation(253);
    String cmp = "var253";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar253", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar253");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation253", 111);
      Trace.log(cmp, Trace.DATASTREAM, "variation253", 111);
      Trace.log(cmp, Trace.ERROR, "variation253", 111);
      Trace.log(cmp, Trace.INFORMATION, "variation253", 111);
      Trace.log(cmp, Trace.WARNING, "variation253", 111);
      Trace.log(cmp, Trace.CONVERSION, "variation253", 111);
      Trace.log(cmp, Trace.PROXY, "variation253", 111);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation254()
  {
    setVariation(254);
    String cmp = "var254";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar254", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar254");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation254", 222);
      Trace.log(cmp, Trace.DATASTREAM, "variation254", 222);
      Trace.log(cmp, Trace.ERROR, "variation254", 222);
      Trace.log(cmp, Trace.INFORMATION, "variation254", 222);
      Trace.log(cmp, Trace.WARNING, "variation254", 222);
      Trace.log(cmp, Trace.CONVERSION, "variation254", 222);
      Trace.log(cmp, Trace.PROXY, "variation254", 222);
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, String message, boolean traceAll, int number )
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, message, number);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1 &&
             data.indexOf(Integer.toString(number)) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOnCmpOff(Object cmp, int category, String variation, int number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, String variation, int number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation255()
  {
    setVariation(255);
    String cmp = "var255";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, "TraceVar255", 222);
  }


  public void variation256()
  {
    setVariation(256);
    String cmp = "var256";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, "TraceVar256", 3333);
  }


  public void variation257()
  {
    setVariation(257);
    String cmp = "var257";
    traceOffCmpOn(cmp, Trace.ERROR, "TraceVar257", 4444);
  }


  public void variation258()
  {
    setVariation(258);
    String cmp = "var258";
    traceOnCmpOff(cmp, Trace.ERROR, "TraceVar258", 5555);
  }

  public void variation259()
  {
    setVariation(259);
    String cmp = "var259";
    traceOffCmpOn(cmp, Trace.INFORMATION, "TraceVar259", 6666);
  }


  public void variation260()
  {
    setVariation(260);
    String cmp = "var260";
    traceOnCmpOff(cmp, Trace.INFORMATION, "TraceVar260", 777);
  }

  public void variation261()
  {
    setVariation(261);
    String cmp = "var261";
    traceOffCmpOn(cmp, Trace.WARNING, "TraceVar261", 888);
  }


  public void variation262()
  {
    setVariation(262);
    String cmp = "var262";
    traceOnCmpOff(cmp, Trace.WARNING, "TraceVar262", 999);
  }

  public void variation263()
  {
    setVariation(263);
    String cmp = "var263";
    logIt(cmp, Trace.ERROR, "TraceVar263", false, 1111);
  }

  public void variation264()
  {
    setVariation(264);
    String cmp = "var264";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar264", false, 0);
  }

  public void variation265()
  {
    setVariation(265);
    String cmp = "var265";
    logIt(cmp, Trace.INFORMATION, "TraceVar265", false, -19);
  }

  public void variation266()
  {
    setVariation(266);
    String cmp = "var266";
    logIt(cmp, Trace.WARNING, "TraceVar266", false, 222);
  }

  public void variation267()
  {
    setVariation(267);
    String cmp = "var267";
    logIt(cmp, Trace.ERROR, "TraceVar267", true, 292929);
  }

  public void variation268()
  {
    setVariation(268);
    String cmp = "var268";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar268", true, 888);
  }

  public void variation269()
  {
    setVariation(269);
    String cmp = "var269";
    logIt(cmp, Trace.INFORMATION, "TraceVar269", true, 9);
  }

  public void variation270()
  {
    setVariation(270);
    String cmp = "var270";
    logIt(cmp, Trace.WARNING, "TraceVar270", true, 6666);
  }




  //
  // Trace.log(component, category, string, boolean) test cases;
  // ----------------------------------------------------------
  //

  public void variation271()
  {
    setVariation(271);
    String cmp = "var271";
    Trace.setTraceOn(true);

    try
    {
      Trace.log(cmp, Trace.DATASTREAM - 1, "hello", true);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", true); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation272()
  {
    setVariation(272);
    String cmp = "var272";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.ALL, "hello", false);
      Trace.log(cmp, 100, "hello", false);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", true); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation273()
  {
    setVariation(273);
    String cmp = "var273";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.THREAD, "hello", true);
      Trace.log(cmp, 99, "hello", true);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", true); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation274()
  {
    setVariation(274);
    String cmp = "var274";
    try
    {
      Trace.log(cmp, Trace.ERROR, (String) null, false);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation275()
  {
    setVariation(275);
    // String cmp = "var275";
    try
    {
      Trace.log(null, Trace.ERROR, "Hi Mom", false);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation276()
  {
    setVariation(276);
    String cmp = "var276";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar276", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar276");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation276",  false);
      Trace.log(cmp, Trace.DATASTREAM, "variation276",  false);
      Trace.log(cmp, Trace.ERROR, "variation276",       false);
      Trace.log(cmp, Trace.INFORMATION, "variation276", false);
      Trace.log(cmp, Trace.WARNING, "variation276",     false);
      Trace.log(cmp, Trace.CONVERSION, "variation276",  false);
      Trace.log(cmp, Trace.PROXY, "variation276",       false);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation277()
  {
    setVariation(277);
    String cmp = "var277";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar277", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar277");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation277",  false);
      Trace.log(cmp, Trace.DATASTREAM, "variation277",  false);
      Trace.log(cmp, Trace.ERROR, "variation277",       false);
      Trace.log(cmp, Trace.INFORMATION, "variation277", false);
      Trace.log(cmp, Trace.WARNING, "variation277",     false);
      Trace.log(cmp, Trace.CONVERSION, "variation277",  false);
      Trace.log(cmp, Trace.PROXY, "variation277",       false);
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, String message, boolean traceAll, boolean number )
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, message, number);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1 &&
             data.indexOf(new Boolean(number).toString()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOnCmpOff(Object cmp, int category, String variation, boolean number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, String variation, boolean number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation278()
  {
    setVariation(278);
    String cmp = "var278";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, "TraceVar278", false);
  }


  public void variation279()
  {
    setVariation(279);
    String cmp = "var279";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, "TraceVar279", false);
  }


  public void variation280()
  {
    setVariation(280);
    String cmp = "var280";
    traceOffCmpOn(cmp, Trace.ERROR, "TraceVar280", true);
  }


  public void variation281()
  {
    setVariation(281);
    String cmp = "var281";
    traceOnCmpOff(cmp, Trace.ERROR, "TraceVar281", true);
  }

  public void variation282()
  {
    setVariation(282);
    String cmp = "var282";
    traceOffCmpOn(cmp, Trace.INFORMATION, "TraceVar282", false);
  }


  public void variation283()
  {
    setVariation(283);
    String cmp = "var283";
    traceOnCmpOff(cmp, Trace.INFORMATION, "TraceVar283", false);
  }

  public void variation284()
  {
    setVariation(284);
    String cmp = "var284";
    traceOffCmpOn(cmp, Trace.WARNING, "TraceVar284", true);
  }


  public void variation285()
  {
    setVariation(285);
    String cmp = "var285";
    traceOnCmpOff(cmp, Trace.WARNING, "TraceVar285", true);
  }

  public void variation286()
  {
    setVariation(286);
    String cmp = "var286";
    logIt(cmp, Trace.ERROR, "TraceVar286", false, false);
  }

  public void variation287()
  {
    setVariation(287);
    String cmp = "var287";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar287", false, false);
  }

  public void variation288()
  {
    setVariation(288);
    String cmp = "var288";
    logIt(cmp, Trace.INFORMATION, "TraceVar288", false, true);
  }

  public void variation289()
  {
    setVariation(289);
    String cmp = "var289";
    logIt(cmp, Trace.WARNING, "TraceVar289", false, false);
  }

  public void variation290()
  {
    setVariation(290);
    String cmp = "var290";
    logIt(cmp, Trace.ERROR, "TraceVar290", true, true);
  }

  public void variation291()
  {
    setVariation(291);
    String cmp = "var291";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar291", true, true);
  }

  public void variation292()
  {
    setVariation(292);
    String cmp = "var292";
    logIt(cmp, Trace.INFORMATION, "TraceVar292", true, true);
  }

  public void variation293()
  {
    setVariation(293);
    String cmp = "var293";
    logIt(cmp, Trace.WARNING, "TraceVar293", true, true);
  }





  //
  // Trace.log(component, category, string, byte[]) test cases;
  // ---------------------------------------------------------
  //

  public void variation294()
  {
    setVariation(294);
    String cmp = "var294";
    Trace.setTraceOn(true);

    try
    {
      Trace.log(cmp, Trace.DATASTREAM - 1, "hello", new byte[1]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", new byte[100]); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation295()
  {
    setVariation(295);
    String cmp = "var295";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.ALL, "hello", false);
      Trace.log(cmp, 100, "hello", new byte[2]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", new byte[9]); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation296()
  {
    setVariation(296);
    String cmp = "var296";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.THREAD, "hello", true);
      Trace.log(cmp, 99, "hello", new byte[9]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", new byte[10]); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation297()
  {
    setVariation(297);
    String cmp = "var297";
    try
    {
      Trace.log(cmp, Trace.ERROR, (String) null, (byte []) null);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation298()
  {
    setVariation(298);
    // String cmp = "var298";
    try
    {
      Trace.log(null, Trace.ERROR, "Hi Mom", new byte[100]);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  public void variation299()
  {
    setVariation(299);
    String cmp = "var299";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar299", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar299");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation299",  new byte[100]);
      Trace.log(cmp, Trace.DATASTREAM, "variation299",  new byte[100]);
      Trace.log(cmp, Trace.ERROR, "variation299",       new byte[100]);
      Trace.log(cmp, Trace.INFORMATION, "variation299", new byte[100]);
      Trace.log(cmp, Trace.WARNING, "variation299",     new byte[100]);
      Trace.log(cmp, Trace.CONVERSION, "variation299",  new byte[100]);
      Trace.log(cmp, Trace.PROXY, "variation299",       new byte[100]);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation300()
  {
    setVariation(300);
    String cmp = "var300";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar300", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar300");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation300",  new byte[100]);
      Trace.log(cmp, Trace.DATASTREAM, "variation300",  new byte[100]);
      Trace.log(cmp, Trace.ERROR, "variation300",       new byte[100]);
      Trace.log(cmp, Trace.INFORMATION, "variation300", new byte[100]);
      Trace.log(cmp, Trace.WARNING, "variation300",     new byte[100]);
      Trace.log(cmp, Trace.CONVERSION, "variation300",  new byte[100]);
      Trace.log(cmp, Trace.PROXY, "variation300",       new byte[100]);
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, String message, boolean traceAll, byte[] number )
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, message, number);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1 &&
             data.indexOf(Integer.toHexString(number[0]).toUpperCase()) != -1 &&
             data.indexOf(Integer.toHexString(number[3]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOnCmpOff(Object cmp, int category, String variation, byte[] number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, String variation, byte[] number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation301()
  {
    setVariation(301);
    String cmp = "var301";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, "TraceVar301", new byte[10]);
  }


  public void variation302()
  {
    setVariation(302);
    String cmp = "var302";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, "TraceVar302", new byte[100]);
  }


  public void variation303()
  {
    setVariation(303);
    String cmp = "var303";
    traceOffCmpOn(cmp, Trace.ERROR, "TraceVar303", new byte[90]);
  }


  public void variation304()
  {
    setVariation(304);
    String cmp = "var304";
    traceOnCmpOff(cmp, Trace.ERROR, "TraceVar304", new byte[80]);
  }

  public void variation305()
  {
    setVariation(305);
    String cmp = "var305";
    traceOffCmpOn(cmp, Trace.INFORMATION, "TraceVar305", new byte[80]);
  }


  public void variation306()
  {
    setVariation(306);
    String cmp = "var306";
    traceOnCmpOff(cmp, Trace.INFORMATION, "TraceVar306", new byte[10]);
  }

  public void variation307()
  {
    setVariation(307);
    String cmp = "var307";
    traceOffCmpOn(cmp, Trace.WARNING, "TraceVar307", new byte[1000]);
  }


  public void variation308()
  {
    setVariation(308);
    String cmp = "var308";
    traceOnCmpOff(cmp, Trace.WARNING, "TraceVar308", new byte[9]);
  }

  public void variation309()
  {
    setVariation(309);
    String cmp = "var309";
    logIt(cmp, Trace.ERROR, "TraceVar309", false, aByteArray);
  }

  public void variation310()
  {
    setVariation(310);
    String cmp = "var310";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar310", false, aByteArray);
  }

  public void variation311()
  {
    setVariation(311);
    String cmp = "var311";
    logIt(cmp, Trace.INFORMATION, "TraceVar311", false, aByteArray);
  }

  public void variation312()
  {
    setVariation(312);
    String cmp = "var312";
    logIt(cmp, Trace.WARNING, "TraceVar312", false, aByteArray);
  }

  public void variation313()
  {
    setVariation(313);
    String cmp = "var313";
    logIt(cmp, Trace.ERROR, "TraceVar313", true, new byte[700]);
  }

  public void variation314()
  {
    setVariation(314);
    String cmp = "var314";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar314", true, new byte[17]);
  }

  public void variation315()
  {
    setVariation(315);
    String cmp = "var315";
    logIt(cmp, Trace.INFORMATION, "TraceVar315", true, new byte[15]);
  }

  public void variation316()
  {
    setVariation(316);
    String cmp = "var316";
    logIt(cmp, Trace.WARNING, "TraceVar316", true, new byte[16]);
  }







  //
  // Trace.log(component, category, string, byte[], offset, length) test cases;
  // -------------------------------------------------------------------------
  //

  public void variation317()
  {
    setVariation(317);
    String cmp = "var317";
    Trace.setTraceOn(true);

    try
    {
      Trace.log(cmp, Trace.DATASTREAM - 1, "hello", new byte[1], 0, 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", new byte[100], 10, 20); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation318()
  {
    setVariation(318);
    String cmp = "var318";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.ALL, "hello", false);
      Trace.log(cmp, 100, "hello", new byte[2], 1, 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", new byte[9], 0, 9); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation319()
  {
    setVariation(319);
    String cmp = "var319";
    Trace.setTraceOn(true);
    try
    {
      // Trace.log(cmp, Trace.THREAD, "hello", true);
      Trace.log(cmp, 99, "hello", new byte[9], 3, 5);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      try
      {
        if (exceptionIs(e, "ExtendedIllegalArgumentException"))
        {
          Trace.log(cmp, HIGHEST + 1, "hello", new byte[10], 2, 6); // @C0C
          failed("Exception didn't occur.");
        }
        else
        {
          failed("Incorrect exception information\n" + e.toString());
        }
      }
      catch (Exception e2)
      {
        assertExceptionIs(e2, "ExtendedIllegalArgumentException");
      }
    }
    Trace.setTraceOn(false);
  }

  public void variation320()
  {
    setVariation(320);
    String cmp = "var320";
    try
    {
      Trace.log(cmp, Trace.ERROR, (String) null, (byte []) null, 0, 1);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation321()
  {
    setVariation(321);
    // String cmp = "var321";
    try
    {
      Trace.log(null, Trace.ERROR, "Hi Mom", new byte[100], 10, 1);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  public void variation322()
  {
    setVariation(322);
    String cmp = "var322";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar322", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar322");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation322",  new byte[100], 10, 5);
      Trace.log(cmp, Trace.DATASTREAM, "variation322",  new byte[100], 10, 5);
      Trace.log(cmp, Trace.ERROR, "variation322",       new byte[100], 10, 5);
      Trace.log(cmp, Trace.INFORMATION, "variation322", new byte[100], 10, 5);
      Trace.log(cmp, Trace.WARNING, "variation322",     new byte[100], 10, 5);
      Trace.log(cmp, Trace.CONVERSION, "variation322",  new byte[100], 10, 5);
      Trace.log(cmp, Trace.PROXY, "variation322",       new byte[100], 10, 5);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation323()
  {
    setVariation(323);
    String cmp = "var323";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar323", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar323");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation323",  new byte[100], 10, 5);
      Trace.log(cmp, Trace.DATASTREAM, "variation323",  new byte[100], 10, 5);
      Trace.log(cmp, Trace.ERROR, "variation323",       new byte[100], 10, 5);
      Trace.log(cmp, Trace.INFORMATION, "variation323", new byte[100], 10, 5);
      Trace.log(cmp, Trace.WARNING, "variation323",     new byte[100], 10, 5);
      Trace.log(cmp, Trace.CONVERSION, "variation323",  new byte[100], 10, 5);
      Trace.log(cmp, Trace.PROXY, "variation323",       new byte[100], 10, 5);
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, String message, boolean traceAll, byte[] number, int offset, int length)
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, message, number);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1 &&
             data.indexOf(Integer.toHexString(number[offset]).toUpperCase()) != -1 &&
             data.indexOf(Integer.toHexString(number[offset + length]).toUpperCase()) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOnCmpOff(Object cmp, int category, String variation, byte[] number, int offset, int length)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number, offset, length);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, String variation, byte[] number, int offset, int length)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number, offset, length);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation324()
  {
    setVariation(324);
    String cmp = "var324";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, "TraceVar324", new byte[10], 2, 5);
  }


  public void variation325()
  {
    setVariation(325);
    String cmp = "var325";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, "TraceVar325", new byte[100], 10, 5);
  }


  public void variation326()
  {
    setVariation(326);
    String cmp = "var326";
    traceOffCmpOn(cmp, Trace.ERROR, "TraceVar326", new byte[90], 3, 7);
  }


  public void variation327()
  {
    setVariation(327);
    String cmp = "var327";
    traceOnCmpOff(cmp, Trace.ERROR, "TraceVar327", new byte[80], 0, 20);
  }

  public void variation328()
  {
    setVariation(328);
    String cmp = "var328";
    traceOffCmpOn(cmp, Trace.INFORMATION, "TraceVar328", new byte[80], 5, 10);
  }


  public void variation329()
  {
    setVariation(329);
    String cmp = "var329";
    traceOnCmpOff(cmp, Trace.INFORMATION, "TraceVar329", new byte[10], 10, 10);
  }

  public void variation330()
  {
    setVariation(330);
    String cmp = "var330";
    traceOffCmpOn(cmp, Trace.WARNING, "TraceVar330", new byte[1000], 999, 1);
  }


  public void variation331()
  {
    setVariation(331);
    String cmp = "var331";
    traceOnCmpOff(cmp, Trace.WARNING, "TraceVar331", new byte[9], 0, 9);
  }

  public void variation332()
  {
    setVariation(332);
    String cmp = "var332";
    logIt(cmp, Trace.ERROR, "TraceVar332", false, aByteArray, 3, 1);
  }

  public void variation333()
  {
    setVariation(333);
    String cmp = "var333";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar333", false, aByteArray, 1, 1);
  }

  public void variation334()
  {
    setVariation(334);
    String cmp = "var334";
    logIt(cmp, Trace.INFORMATION, "TraceVar334", false, aByteArray, 0, 2);
  }

  public void variation335()
  {
    setVariation(335);
    String cmp = "var335";
    logIt(cmp, Trace.WARNING, "TraceVar335", false, aByteArray, 2, 2);
  }

  public void variation336()
  {
    setVariation(336);
    String cmp = "var336";
    logIt(cmp, Trace.ERROR, "TraceVar336", true, new byte[700], 698, 1);
  }

  public void variation337()
  {
    setVariation(337);
    String cmp = "var337";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar337", true, new byte[18], 0, 17);
  }

  public void variation338()
  {
    setVariation(338);
    String cmp = "var338";
    logIt(cmp, Trace.INFORMATION, "TraceVar338", true, new byte[15], 12, 2);
  }

  public void variation339()
  {
    setVariation(339);
    String cmp = "var339";
    logIt(cmp, Trace.WARNING, "TraceVar339", true, new byte[16], 10, 1);
  }






  //
  // A couple conversion, proxy and data stream test cases
  // -----------------------------------------------------
  //


  public void variation340()
  {
    setVariation(340);
    String cmp = "var340";
    traceOffCmpOn(cmp, Trace.CONVERSION, "TraceVar340", new byte[10], 2, 5);
  }

  public void variation341()
  {
    setVariation(341);
    String cmp = "var341";
    traceOffCmpOn(cmp, Trace.PROXY, "TraceVar341", new byte[10], 2, 5);
  }

  public void variation342()
  {
    setVariation(342);
    String cmp = "var342";
    traceOffCmpOn(cmp, Trace.DATASTREAM, "TraceVar342", new byte[10], 2, 5);
  }


  public void variation343()
  {
    setVariation(343);
    String cmp = "var343";
    traceOnCmpOff(cmp, Trace.CONVERSION, "TraceVar343");
  }

  public void variation344()
  {
    setVariation(344);
    String cmp = "var344";
    traceOnCmpOff(cmp, Trace.PROXY, "TraceVar344");
  }

  public void variation345()
  {
    setVariation(345);
    String cmp = "var345";
    traceOnCmpOff(cmp, Trace.DATASTREAM, "TraceVar345");
  }


  public void variation346()
  {
    setVariation(346);
    String cmp = "var346";
    logIt(cmp, Trace.CONVERSION, "TraceVar346", false, 5);
  }

  public void variation347()
  {
    setVariation(347);
    String cmp = "var347";
    logIt(cmp, Trace.PROXY, "TraceVar347", true, 5);
  }

  public void variation348()
  {
    setVariation(348);
    String cmp = "var348";
    logIt(cmp, Trace.DATASTREAM, "TraceVar348", false, 5);
  }

  public void variation349()
  {
    setVariation(349);
    String cmp = "var349";
    logIt(cmp, Trace.CONVERSION, "TraceVar349", false, false);
  }

  public void variation350()
  {
    setVariation(350);
    String cmp = "var350";
    logIt(cmp, Trace.PROXY, "TraceVar350", true, true);
  }

  public void variation351()
  {
    setVariation(351);
    String cmp = "var351";
    logIt(cmp, Trace.DATASTREAM, "TraceVar351", true, false);
  }





  //
  // Trace.log(component, category, string, Throwable) test cases;
  // ------------------------------------------------------------
  //

  public void variation352()
  {
    setVariation(352);
    String cmp = "var352";
    try
    {
      Trace.log(cmp, Trace.ERROR, "TraceVar352", (Throwable) null);
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation353()
  {
    setVariation(353);
    // String cmp = "var353";
    try
    {
      Trace.log(null, Trace.ERROR, "Hi Mom", new Throwable());
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation354()
  {
    setVariation(354);
    String cmp = "var354";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar354", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar354");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation354", new Throwable());
      Trace.log(cmp, Trace.DATASTREAM, "variation354", new Throwable());
      Trace.log(cmp, Trace.ERROR, "variation354", new Throwable());
      Trace.log(cmp, Trace.INFORMATION, "variation354", new Throwable());
      Trace.log(cmp, Trace.WARNING, "variation354", new Throwable());
      Trace.log(cmp, Trace.CONVERSION, "variation354", new Throwable());
      Trace.log(cmp, Trace.PROXY, "variation354", new Throwable());
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation355()
  {
    setVariation(355);
    String cmp = "var355";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar355", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar355");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC, "variation355", new Throwable());
      Trace.log(cmp, Trace.DATASTREAM, "variation355", new Throwable());
      Trace.log(cmp, Trace.ERROR, "variation355", new Throwable());
      Trace.log(cmp, Trace.INFORMATION, "variation355", new Throwable());
      Trace.log(cmp, Trace.WARNING, "variation355", new Throwable());
      Trace.log(cmp, Trace.CONVERSION, "variation355", new Throwable());
      Trace.log(cmp, Trace.PROXY, "variation355", new Throwable());
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, String message, boolean traceAll, Throwable number )
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, message, number);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOnCmpOff(Object cmp, int category, String variation, Throwable number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, String variation, Throwable number)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation356()
  {
    setVariation(356);
    String cmp = "var356";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, "TraceVar356", new Throwable());
  }


  public void variation357()
  {
    setVariation(357);
    String cmp = "var357";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, "TraceVar357", new Throwable());
  }


  public void variation358()
  {
    setVariation(358);
    String cmp = "var358";
    traceOffCmpOn(cmp, Trace.ERROR, "TraceVar358", new Throwable());
  }


  public void variation359()
  {
    setVariation(359);
    String cmp = "var359";
    traceOnCmpOff(cmp, Trace.ERROR, "TraceVar359", new Throwable());
  }

  public void variation360()
  {
    setVariation(360);
    String cmp = "var360";
    traceOffCmpOn(cmp, Trace.INFORMATION, "TraceVar360", new Throwable());
  }


  public void variation361()
  {
    setVariation(361);
    String cmp = "var361";
    traceOnCmpOff(cmp, Trace.INFORMATION, "TraceVar361", new Throwable());
  }

  public void variation362()
  {
    setVariation(362);
    String cmp = "var362";
    traceOffCmpOn(cmp, Trace.WARNING, "TraceVar362", new Throwable());
  }


  public void variation363()
  {
    setVariation(363);
    String cmp = "var363";
    traceOnCmpOff(cmp, Trace.WARNING, "TraceVar363", new Throwable());
  }

  public void variation364()
  {
    setVariation(364);
    String cmp = "var364";
    logIt(cmp, Trace.ERROR, "TraceVar364", false, new Throwable());
  }

  public void variation365()
  {
    setVariation(365);
    String cmp = "var365";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar365", false, new Throwable());
  }

  public void variation366()
  {
    setVariation(366);
    String cmp = "var366";
    logIt(cmp, Trace.INFORMATION, "TraceVar366", false, new Throwable());
  }

  public void variation367()
  {
    setVariation(367);
    String cmp = "var367";
    logIt(cmp, Trace.WARNING, "TraceVar367", false, new Throwable());
  }

  public void variation368()
  {
    setVariation(368);
    String cmp = "var368";
    logIt(cmp, Trace.ERROR, "TraceVar368", true, new Throwable());
  }

  public void variation369()
  {
    setVariation(369);
    String cmp = "var369";
    logIt(cmp, Trace.DIAGNOSTIC, "TraceVar369", true, new Throwable());
  }

  public void variation370()
  {
    setVariation(370);
    String cmp = "var370";
    logIt(cmp, Trace.INFORMATION, "TraceVar370", true, new Throwable());
  }

  public void variation371()
  {
    setVariation(371);
    String cmp = "var371";
    logIt(cmp, Trace.WARNING, "TraceVar371", true, new Throwable());
  }


  public void variation372()
  {
    setVariation(372);
    String cmp = "var372";
    try
    {
      Trace.log(cmp, Trace.ERROR, (String) null, new Throwable());
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }




  //
  // Trace.log(component, category, Throwable) test cases;
  // ------------------------------------------------------------
  //


  public void variation373()
  {
    setVariation(373);
    // String cmp = "var373";
    try
    {
      Trace.log(null, Trace.ERROR, new Throwable("TraceVar373"));
      succeeded();
    }
    catch (Exception e) { failed(e, "Unexpected exception"); }
  }

  public void variation374()
  {
    setVariation(374);
    String cmp = "var374";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar374", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar374");
      Trace.setTraceOn(false);
      Trace.setTraceAllOn(true);
      Trace.log(cmp, Trace.DIAGNOSTIC,   new Throwable("variation374"));
      Trace.log(cmp, Trace.DATASTREAM,   new Throwable("variation374"));
      Trace.log(cmp, Trace.ERROR,        new Throwable("variation374"));
      Trace.log(cmp, Trace.INFORMATION,  new Throwable("variation374"));
      Trace.log(cmp, Trace.WARNING,      new Throwable("variation374"));
      Trace.log(cmp, Trace.CONVERSION,   new Throwable("variation374"));
      Trace.log(cmp, Trace.PROXY,        new Throwable("variation374"));
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation375()
  {
    setVariation(375);
    String cmp = "var375";
    try
    {
      RandomAccessFile file = new RandomAccessFile("TraceVar375", "rw");
      long lengthBefore = file.length();

      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, "TraceVar375");
      Trace.setTraceOn(true);
      Trace.setTraceAllOn(false);
      Trace.log(cmp, Trace.DIAGNOSTIC,   new Throwable("variation375"));
      Trace.log(cmp, Trace.DATASTREAM,   new Throwable("variation375"));
      Trace.log(cmp, Trace.ERROR,        new Throwable("variation375"));
      Trace.log(cmp, Trace.INFORMATION,  new Throwable("variation375"));
      Trace.log(cmp, Trace.WARNING,      new Throwable("variation375"));
      Trace.log(cmp, Trace.CONVERSION,   new Throwable("variation375"));
      Trace.log(cmp, Trace.PROXY,        new Throwable("variation375"));
      assertCondition(file.length() <= (lengthBefore + 50));  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void logIt(Object cmp, int category, boolean traceAll, Throwable number, String message)
  {
    try
    {
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, message);
      RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
      file.seek(file.length() == 0 ? 0 : file.length() - 1);
      Trace.setTraceOn(true);

      if (traceAll)
         Trace.setTraceAllOn(true);
      else
         switch (category)
         {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
         }

      Trace.log(cmp, category, number);
      String data = "";
      String data2 = null;
      while ((data2 = file.readLine()) != null)
      {
        data += data2;
      }
      assertCondition(data.indexOf(message) != -1);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOnCmpOff(Object cmp, int category, Throwable number, String variation)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(true);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(false);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(false);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(false);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(false);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(false);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(false);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(false);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() <= lengthBefore + 50);  // 50 for version string
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  void traceOffCmpOn(Object cmp, int category, Throwable number, String variation)
  {
    try
    {
      RandomAccessFile file = new RandomAccessFile(variation, "rw");
      long lengthBefore = file.length();
      Trace.setFileName("Bit.Bucket");
      Trace.setFileName(cmp, variation);
      Trace.setTraceOn(false);

      switch (category)
      {
        case Trace.CONVERSION:
          Trace.setTraceConversionOn(true);
          break;
        case Trace.DATASTREAM:
          Trace.setTraceDatastreamOn(true);
          break;
        case Trace.DIAGNOSTIC:
          Trace.setTraceDiagnosticOn(true);
          break;
        case Trace.ERROR:
          Trace.setTraceErrorOn(true);
          break;
        case Trace.INFORMATION:
          Trace.setTraceInformationOn(true);
          break;
        case Trace.PROXY:
          Trace.setTraceProxyOn(true);
          break;
        case Trace.WARNING:
          Trace.setTraceWarningOn(true);
          break;
        default:
          failed("testcase error");
          return;
      }

      Trace.log(cmp, category, variation, number);
      assertCondition(file.length() == lengthBefore);
      file.close();
    }
    catch (Exception e)
    {
      failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }

  public void variation376()
  {
    setVariation(376);
    String cmp = "var376";
    traceOffCmpOn(cmp, Trace.DIAGNOSTIC, new Throwable("TraceVar376"), "TraceVar376");
  }


  public void variation377()
  {
    setVariation(377);
    String cmp = "var377";
    traceOnCmpOff(cmp, Trace.DIAGNOSTIC, new Throwable("TraceVar377"), "TraceVar377");
  }


  public void variation378()
  {
    setVariation(378);
    String cmp = "var378";
    traceOffCmpOn(cmp, Trace.ERROR, new Throwable("TraceVar378"), "TraceVar378");
  }


  public void variation379()
  {
    setVariation(379);
    String cmp = "var379";
    traceOnCmpOff(cmp, Trace.ERROR, new Throwable("TraceVar379"), "TraceVar379");
  }

  public void variation380()
  {
    setVariation(380);
    String cmp = "var380";
    traceOffCmpOn(cmp, Trace.INFORMATION, new Throwable("TraceVar380"), "TraceVar380");
  }


  public void variation381()
  {
    setVariation(381);
    String cmp = "var381";
    traceOnCmpOff(cmp, Trace.INFORMATION, new Throwable("TraceVar381"), "TraceVar381");
  }

  public void variation382()
  {
    setVariation(382);
    String cmp = "var382";
    traceOffCmpOn(cmp, Trace.WARNING, new Throwable("TraceVar382"), "TraceVar382");
  }


  public void variation383()
  {
    setVariation(383);
    String cmp = "var383";
    traceOnCmpOff(cmp, Trace.WARNING, new Throwable("TraceVar383"), "TraceVar383");
  }

  public void variation384()
  {
    setVariation(384);
    String cmp = "var384";
    logIt(cmp, Trace.ERROR, false, new Throwable("TraceVar384"), "TraceVar384");
  }

  public void variation385()
  {
    setVariation(385);
    String cmp = "var385";
    logIt(cmp, Trace.DIAGNOSTIC, false, new Throwable("TraceVar385"), "TraceVar385");
  }

  public void variation386()
  {
    setVariation(386);
    String cmp = "var386";
    logIt(cmp, Trace.INFORMATION, false, new Throwable("TraceVar386"), "TraceVar386");
  }

  public void variation387()
  {
    setVariation(387);
    String cmp = "var387";
    logIt(cmp, Trace.WARNING, false, new Throwable("TraceVar387"), "TraceVar387");
  }

  public void variation388()
  {
    setVariation(388);
    String cmp = "var388";
    logIt(cmp, Trace.ERROR, true, new Throwable("TraceVar388"), "TraceVar388");
  }

  public void variation389()
  {
    setVariation(389);
    String cmp = "var389";
    logIt(cmp, Trace.DIAGNOSTIC, true, new Throwable("TraceVar389"), "TraceVar389");
  }

  public void variation390()
  {
    setVariation(390);
    String cmp = "var390";
    logIt(cmp, Trace.INFORMATION, true, new Throwable("TraceVar390"), "TraceVar390");
  }

  public void variation391()
  {
    setVariation(391);
    String cmp = "var391";
    logIt(cmp, Trace.WARNING,  true, new Throwable("TraceVar391"), "TraceVar391");
  }

  public void variation392()
  {
    setVariation(392);
    String cmp = "var392";
    try
    {
      Trace.log(cmp, Trace.ERROR, (Throwable) null);
      failed("Exception didn't occur.");
    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }


  //
  // One more Misc test case -- make sure [component] is in the trace


  public void variation393()
  {
    setVariation(393);
    String cmp = "var393";
    try
    {
       Trace.setFileName("Bit.Bucket");
       Trace.setFileName(cmp, cmp);
       RandomAccessFile file = new RandomAccessFile(Trace.getFileName(cmp), "r");
       file.seek(file.length() == 0 ? 0 : file.length() - 1);
       Trace.setTraceAllOn(true);
       Trace.setTraceOn(true);
       Trace.log(cmp, Trace.DATASTREAM, "Hi Mom");
       String data = "";
       String data2 = null;
       while ((data2 = file.readLine()) != null)
       {
         data += data2;
       }
       assertCondition(data.indexOf("[" + cmp + "]") != -1);
       file.close();
    }
    catch (Exception e)
    {
       failed(e);
    }

    // Delete the file.
    deleteFile(cmp, Trace.getFileName(cmp));
  }


  /**
Ensure that Trace.ALL and Trace.JDBC agree.
**/
  public void variation394()     // @D3A
  {
    setVariation(394);
    Trace.setTraceAllOn(false);
    if (!Trace.isTraceJDBCOn() && !Trace.isTraceAllOn())
    {
      Trace.setTraceJDBCOn(true);
      if (Trace.isTraceJDBCOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTraceJDBCOn() && Trace.isTraceAllOn())
        {
          Trace.setTraceJDBCOn(false);
          if (!Trace.isTraceJDBCOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.JDBC do not agree.");
  }


  /**
Ensure that Trace.ALL and Trace.PCML agree.
**/
  public void variation395()    // @D3A
  {
    setVariation(395);
    Trace.setTraceAllOn(false);
    if (!Trace.isTracePCMLOn() && !Trace.isTraceAllOn())
    {
      Trace.setTracePCMLOn(true);
      if (Trace.isTracePCMLOn() && !Trace.isTraceAllOn())
      {
        Trace.setTraceAllOn(true);
        if (Trace.isTracePCMLOn() && Trace.isTraceAllOn())
        {
          Trace.setTracePCMLOn(false);
          if (!Trace.isTracePCMLOn() && !Trace.isTraceAllOn())
          {
            succeeded();
            return;
          }
        }
      }
    }
    failed("Trace.ALL and Trace.PCML do not agree.");
  }


}


