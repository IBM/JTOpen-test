///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RF;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.math.BigDecimal;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.DateFieldDescription;
import com.ibm.as400.access.DBCSEitherFieldDescription;
import com.ibm.as400.access.DBCSGraphicFieldDescription;
import com.ibm.as400.access.DBCSOnlyFieldDescription;
import com.ibm.as400.access.DBCSOpenFieldDescription;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.HexFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.TimestampFieldDescription;
import com.ibm.as400.access.ZonedDecimalFieldDescription;

import test.Testcase;

import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
  Testcase RFMisc.  This test class verifes valid and invalid usage of
  the RecordFormat constructors and methods with the exception of the
  getNewRecord() methods.  See RFNewRecord for testing of the getNewRecord()
  methods.
**/
public class RFMisc extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RFMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RFTest.main(newArgs); 
   }
  /**
  Constructor.  This is called from the RFTest constructor.
  **/
  public RFMisc(AS400            systemObject,
                Vector<String> variationsToRun,
                int              runMode,
                FileOutputStream fileOutputStream)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    // @B1C - changed to 95.
    super(systemObject, "RFMisc", 95,
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
    
    // @B1A - Added variations below
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
}

  /**
   *Verify valid usage of constructor RecordFormat(String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getName() should return the name specified on the constructor in
   *uppercase.
   *</ul>
  **/
  public void Var001()
  {
    String name = "blah";

    RecordFormat r = new RecordFormat(name);
    if (!r.getName().equals(name.toUpperCase()))
    {
      failed("Wrong name returned after constructing object: " + r.getName());
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify invalid usage of constructor RecordFormat(String).
   *<ul>
   *<li>RecordFormat(null)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "name"..
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      RecordFormat r = new RecordFormat(null);
      failed("Able to construct object with null parameter.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong parameter in null pointer exception.");
      }
      else
      {
        succeeded();
      }
    }
  }

  /**
   *Verify valid usage of constructor RecordFormat().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getName() should return "".
   *</ul>
  **/
  public void Var003()
  {
    RecordFormat r = new RecordFormat();
    if (!r.getName().equals(""))
    {
      failed("Wrong name returned after constructing object: " + r.getName());
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify valid usage of addFieldDescription(FieldDescription).
   *<ul compact>
   *<li>Add one, two and three field descriptions.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfFields() should return the number of field descriptions that
   *have been added.
   *</ul>
  **/
  public void Var004()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    if (r.getNumberOfFields() != 1)
    {
      failed("Wrong number of fields after one field description added: " + r.getNumberOfFields());
      return;
    }
    r.addFieldDescription(c);
    if (r.getNumberOfFields() != 2)
    {
      failed("Wrong number of fields after two field descriptions added: " + r.getNumberOfFields());
      return;
    }
    r.addFieldDescription(f);
    if (r.getNumberOfFields() != 3)
    {
      failed("Wrong number of fields after threee field description added: " + r.getNumberOfFields());
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of addFieldDescription(FieldDescription).
   *<ul compact>
   *<li>addFieldDescription(null)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "field"
   *<li>getNumberOfFields() should return 0.
   *</ul>
  **/
  public void Var005()
  {
    RecordFormat r = new RecordFormat();

    try
    {
      r.addFieldDescription(null);
      failed("No exception specifying null for FieldDescription.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "field"))
      {
        failed(e, "Wrong parameter in null pointer exception.");
        return;
      }
      if (r.getNumberOfFields() != 0)
      {
        failed("Number of fields incorrect after failed add: " + r.getNumberOfFields());
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify valid usage of addKeyFieldDescription(int).
   *<ul compact>
   *<li>Add one, two and three key fields.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfKeyFields() should return the number of key fields added.
   *</ul>
  **/
  public void Var006()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    // Verify  adding by index and by name
    r.addKeyFieldDescription(0);
    if (r.getNumberOfKeyFields() != 1)
    {
      failed("Wrong number of fields after addKeyFieldDescription(): " + r.getNumberOfKeyFields());
      return;
    }
    r.addKeyFieldDescription(1);
    if (r.getNumberOfKeyFields() != 2)
    {
      failed("Wrong number of fields after addKeyFieldDescription(): " + r.getNumberOfKeyFields());
      return;
    }
    r.addKeyFieldDescription(2);
    if (r.getNumberOfKeyFields() != 3)
    {
      failed("Wrong number of fields after addKeyFieldDescription(): " + r.getNumberOfKeyFields());
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of addKeyFieldDescription(String).
   *<ul compact>
   *<li>Add one, two and three key fields.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfKeyFields() should return the number of key fields added.
   *</ul>
  **/
  public void Var007()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    // Verify  adding by index and by name
    r.addKeyFieldDescription("b");
    if (r.getNumberOfKeyFields() != 1)
    {
      failed("Wrong number of fields after addKeyFieldDescription(): " + r.getNumberOfKeyFields());
      return;
    }
    r.addKeyFieldDescription("c");
    if (r.getNumberOfKeyFields() != 2)
    {
      failed("Wrong number of fields after addKeyFieldDescription(): " + r.getNumberOfKeyFields());
      return;
    }
    r.addKeyFieldDescription("f");
    if (r.getNumberOfKeyFields() != 3)
    {
      failed("Wrong number of fields after addKeyFieldDescription(): " + r.getNumberOfKeyFields());
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of addKeyFieldDescription(int).
   *<ul compact>
   *<li>Specify an index of less than 0
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *<li>getNumberOfKeyFields() should return 0.
   *</ul>
  **/
  public void Var008()
  {
    RecordFormat r = new RecordFormat();
    try
    {
      r.addKeyFieldDescription(-1);
      failed("No exception specifying -1 for index.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      if (r.getNumberOfKeyFields() != 0)
      {
        failed("Number of key fields incorrect after failed add: " + r.getNumberOfKeyFields());
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify invalid usage of addKeyFieldDescription(int).
   *<ul compact>
   *<li>Specify an index greater than the number of fields already added.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *<li>getNumberOfKeyFields() should return 0.
   *</ul>
  **/
  public void Var009()
  {
    RecordFormat r = new RecordFormat();
    try
    {
      r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
      r.addKeyFieldDescription(1);
      failed("No exception specifying greater than number of fields - 1 for index.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      if (r.getNumberOfKeyFields() != 0)
      {
        failed("Number of key fields incorrect after failed add: " + r.getNumberOfKeyFields());
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify invalid usage of addKeyFieldDescription(String).
   *<ul compact>
   *<li>addKeyFieldDescription(null)
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "name"
   *<li>getNumberOfKeyFields() should return 0.
   *</ul>
  **/
  public void Var010()
  {
    RecordFormat r = new RecordFormat();
    try
    {
      r.addKeyFieldDescription(null);
      failed("No exception specifying null for name.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong parameter in null pointer exception.");
        return;
      }
      if (r.getNumberOfKeyFields() != 0)
      {
        failed("Number of key fields incorrect after failed add: " + r.getNumberOfKeyFields());
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify invalid usage of addKeyFieldDescription(String).
   *<ul compact>
   *<li>Specify the name of a field that does not exist in the record format
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
   *<li>getNumberOfKeyFields() should return 0.
   *</ul>
  **/
  public void Var011()
  {
    RecordFormat r = new RecordFormat();
    try
    {
      r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
      r.addKeyFieldDescription("blah");
      failed("No exception specifying non existent name.");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      if (r.getNumberOfKeyFields() != 0)
      {
        failed("Number of key fields incorrect after failed add: " + r.getNumberOfKeyFields());
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify valid usage of getFieldDescription(int).
   *<ul compact>
   *<li>Add three field descriptions to the record fromat object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFieldDescription(int) should return the FieldDescription object
   *specified on the addFieldDescription() method.
   *</ul>
  **/
  public void Var012()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    if (r.getFieldDescription(0) != b)
    {
      failed("Wrong field description returned from getFieldDescription(index)");
      return;
    }
    if (r.getFieldDescription(1) != c)
    {
      failed("Wrong field description returned from getFieldDescription(index)");
      return;
    }
    if (r.getFieldDescription(2) != f)
    {
      failed("Wrong field description returned from getFieldDescription(index)");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getFieldDescription(int).
   *<ul compact>
   *<li>Pass an index of -1.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var013()
  {
    RecordFormat r = new RecordFormat();

    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    try
    {
      r.getFieldDescription(-1);
      failed("No exception specifying -1 for index");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify invalid usage of getFieldDescription(int).
   *<ul compact>
   *<li>Pass an index greater than the number of fields - 1.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var014()
  {
    RecordFormat r = new RecordFormat();

    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    try
    {
      r.getFieldDescription(1);
      failed("No exception specifying greater number of fields for index");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify valid usage of getFieldDescription(String).
   *<ul compact>
   *<li>Add three field descriptions to the record fromat object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getFieldDescription(String) should return the FieldDescription object
   *specified on the addFieldDescription() method.
   *</ul>
  **/
  public void Var015()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    if (r.getFieldDescription("b") != b)
    {
      failed("Wrong field description returned from getFieldDescription(String).");
      return;
    }
    if (r.getFieldDescription("c") != c)
    {
      failed("Wrong field description returned from getFieldDescription(String).");
      return;
    }
    if (r.getFieldDescription("f") != f)
    {
      failed("Wrong field description returned from getFieldDescription(String).");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getFieldDescription(String).
   *<ul>
   *<li>Pass null parameter
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "name".
   *</ul>
  **/
  public void Var016()
  {
    RecordFormat r = new RecordFormat();

    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    // Verify error when passing null
    try
    {
      r.getFieldDescription(null);
      failed("No exception passing null");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong parameter in null pointer exception.");
      }
      else
      {
        succeeded();
      }
    }
  }


  /**
   *Verify invalid usage of getFieldDescription(String).
   *<ul>
   *<li>Pass non existent field name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var017()
  {
    RecordFormat r = new RecordFormat();

    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    // Verify error when passing non-existent field name
    try
    {
      r.getFieldDescription("blah");
      failed("No exception with invalid field name");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (blah)", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong parameter in illegal argument exception.");
      }
      else
      {
        succeeded();
      }
    }
  }

  /**
   *Verify valid usage of getKeyFieldDescription(int).
   *<ul compact>
   *<li>Add three key field descriptions to the record fromat object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getKeyFieldDescription(int) should return the FieldDescription object
   *specified on the addKeyFieldDescription() method.
   *</ul>
  **/
  public void Var018()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    r.addKeyFieldDescription("b");
    r.addKeyFieldDescription("c");
    r.addKeyFieldDescription("f");

    if (r.getKeyFieldDescription(0) != b)
    {
      failed("Wrong field description returned from getKeyFieldDescription(index).");
      return;
    }
    if (r.getKeyFieldDescription(1) != c)
    {
      failed("Wrong field description returned from getKeyFieldDescription(index).");
      return;
    }
    if (r.getKeyFieldDescription(2) != f)
    {
      failed("Wrong field description returned from getKeyFieldDescription(index).");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getKeyFieldDescription(int).
   *<ul compact>
   *<li>Pass -1 for index.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var019()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    r.addKeyFieldDescription("b");
    try
    {
      r.getKeyFieldDescription(-1);
      failed("No exception specifying -1 for index");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify invalid usage of getKeyFieldDescription(int).
   *<ul compact>
   *<li>Pass greater than number of key fields - 1 for index.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var020()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    r.addKeyFieldDescription("b");
    try
    {
      r.getKeyFieldDescription(1);
      failed("No exception specifying greater than numbe rof key fields - 1 for index");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Wrong exception/info.");
        return;
      }
      succeeded();
    }
  }

  /**
   *Verify valid usage of getKeyFieldDescription(String).
   *<ul compact>
   *<li>Add three field descriptions to the record fromat object.
   *<li>Add three key field descriptions to the record fromat object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getKeyFieldDescription(String) should return the FieldDescription object
   *specified on the addKeyFieldDescription() method.
   *</ul>
  **/
  public void Var021()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "b");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "c");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "f");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    r.addKeyFieldDescription("b");
    r.addKeyFieldDescription(1);
    r.addKeyFieldDescription(2);

    if (r.getKeyFieldDescription("b") != b)
    {
      failed("Wrong field description returned from getKeyFieldDescription(String)");
      return;
    }
    if (r.getKeyFieldDescription("c") != c)
    {
      failed("Wrong field description returned from getKeyFieldDescription(String)");
      return;
    }
    if (r.getKeyFieldDescription("f") != f)
    {
      failed("Wrong field description returned from getKeyFieldDescription(String)");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getKeyFieldDescription(String).
   *<ul>
   *<li>Pass null parameter
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "name".
   *</ul>
  **/
  public void Var022()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    r.addKeyFieldDescription(0);

    // Verify error when passing null
    try
    {
      r.getKeyFieldDescription(null);
      failed("No exception passing null");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e,"NullPointerException", "name"))
      {
        failed(e, "Wrong parameter in null pointer exception.");
      }
      else
      {
        succeeded();
      }
    }
  }

  /**
   *Verify invalid usage of getKeyFieldDescription(String).
   *<ul>
   *<li>Pass non existent field name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var023()
  {
    RecordFormat r = new RecordFormat();
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    r.addKeyFieldDescription(0);

    // Verify error when passing non-existent field name
    try
    {
      r.getKeyFieldDescription("blah");
      failed("No exception passing invalid field name");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (blah)", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong parameter in illegal argument exception.");
      }
      else
      {
        succeeded();
      }
    }
  }

  /**
   *Verify getFieldDescriptions() when no fields have been added yet.
   *<ul compact>
   *<li>Null constructor used
   *<li>RecordFormat(String) used
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 will be returned.
   *</ul>
  **/
  public void Var024()
  {
    RecordFormat r = new RecordFormat();
    // Verify array of size 0 returned on empty record format
    if (r.getFieldDescriptions().length != 0)
    {
      failed("Field descriptions returned on empty record format");
      return;
    }

    r = new RecordFormat("blah");
    // Verify array of size 0 returned on empty record format
    if (r.getFieldDescriptions().length != 0)
    {
      failed("Field descriptions returned on empty record format");
      return;
    }
    succeeded();
  }

  /**
   *Verify getFieldDescriptions() when fields have been added.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>An array of the field descriptions added will be returned.
   *</ul>
  **/
  public void Var025()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    FieldDescription[] fields = r.getFieldDescriptions();

    //Verify the names
    if (fields[0] != b)
    {
      failed("Wrong field description returned");
      return;
    }
    if (fields[1] != c)
    {
      failed("Wrong field description returned");
      return;
    }
    if (fields[2] != f)
    {
      failed("Wrong field description returned");
      return;
    }
    // Verify number of names returned
    if (fields.length != 3)
    {
      failed("Wrong number of descriptions returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify getFieldNames() when no fields have been added yet.
   *<ul compact>
   *<li>Null constructor used
   *<li>RecordFormat(String) used
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 will be returned.
   *</ul>
  **/
  public void Var026()
  {
    RecordFormat r = new RecordFormat();
    // Verify array of size 0 returned on empty record format
    if (r.getFieldNames().length != 0)
    {
      failed("Names returned on empty record format");
      return;
    }

    r = new RecordFormat("blah");
    // Verify array of size 0 returned on empty record format
    if (r.getFieldNames().length != 0)
    {
      failed("Names returned on empty record format");
      return;
    }
    succeeded();
  }

  /**
   *Verify getFieldNames() when fields have been added.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>An the field names of the added fields will be returned..
   *</ul>
  **/
  public void Var027()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    String[] names = r.getFieldNames();

    //Verify the names
    if (!names[0].equals("binaryField"))
    {
      failed("Wrong name returned for binaryField: " + names[0]);
      return;
    }
    if (!names[1].equals("char"))
    {
      failed("Wrong name returned for binaryField: " + names[1]);
      return;
    }
    if (!names[2].equals("flotilla Of Floats"))
    {
      failed("Wrong name returned for binaryField: " + names[2]);
      return;
    }
    // Verify number of names returned
    if (names.length != 3)
    {
      failed("Wrong number of names returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getIndexOfFieldName(String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The index of the field description added will be returned.
   *</ul>
  **/
  public void Var028()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);


    //Verify the indices of fields
    if (r.getIndexOfFieldName("binaryField") != 0)
    {
      failed("Wrong index returned");
      return;
    }
    if (r.getIndexOfFieldName("char") != 1)
    {
      failed("Wrong index returned");
      return;
    }
    if (r.getIndexOfFieldName("flotilla Of Floats") != 2)
    {
      failed("Wrong index returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getIndexOfFieldName(String)
   *<ul>
   *<li>Pass null parameter
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "name".
   *</ul>
  **/
  public void Var029()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);


    // Verify error when passing null
    try
    {
      r.getIndexOfFieldName(null);
      failed("No exception passing null");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong parameter returned with NullPointerException");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getIndexOfFieldName(String)
   *<ul>
   *<li>Pass non existent name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND
   *</ul>
  **/
  public void Var030()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);


    // Verify error when passing non-existent field name
    try
    {
      r.getIndexOfFieldName("blah");
      failed("No exception passing invalid field name");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (blah)", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong parameter returned with ExtendedIllegalArgumentException");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getIndexOfKeyFieldName(String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The index of the key field description added will be returned.
   *</ul>
  **/
  public void Var031()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    r.addKeyFieldDescription(0);
    r.addKeyFieldDescription(1);
    r.addKeyFieldDescription(2);

    //Verify the indices of fields
    if (r.getIndexOfKeyFieldName("binaryField") != 0)
    {
      failed("Wrong index returned");
      return;
    }
    if (r.getIndexOfKeyFieldName("char") != 1)
    {
      failed("Wrong index returned");
      return;
    }
    if (r.getIndexOfKeyFieldName("flotilla Of Floats") != 2)
    {
      failed("Wrong index returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getIndexOfKeyFieldName(String).
   *<ul>
   *<li>Pass null parameter
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() returning "name".
   *</ul>
  **/
  public void Var032()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    r.addKeyFieldDescription(0);
    r.addKeyFieldDescription(1);
    r.addKeyFieldDescription(2);


    // Verify error when passing null
    try
    {
      r.getIndexOfKeyFieldName(null);
      failed("No exception passing null");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong parameter returned with NullPointerException");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getIndexOfKeyFieldName(String).
   *<ul>
   *<li>Pass non existent name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND
   *</ul>
  **/
  public void Var033()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    r.addKeyFieldDescription(0);
    r.addKeyFieldDescription(1);
    r.addKeyFieldDescription(2);

    // Verify error when passing non-existent field name
    try
    {
      r.getIndexOfKeyFieldName("blah");
      failed("No exception passing invalid field name");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (blah)", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Wrong parameter returned with ExtendedIllegalArgumentException");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify getKeyFieldNames() when no fields have been added yet.
   *<ul compact>
   *<li>Null constructor used
   *<li>RecordFormat(String) used
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 will be returned.
   *</ul>
  **/
  public void Var034()
  {
    RecordFormat r = new RecordFormat();
    // Verify array of size 0 returned on empty record format
    if (r.getKeyFieldNames().length != 0)
    {
      failed("Names returned on empty record format");
      return;
    }

    r = new RecordFormat("blah");
    // Verify array of size 0 returned on empty record format
    if (r.getKeyFieldNames().length != 0)
    {
      failed("Names returned on empty record format");
      return;
    }
    succeeded();
  }

  /**
   *Verify getKeyFieldNames() when fields have been added.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>An the field names of the added fields will be returned..
   *</ul>
  **/
  public void Var035()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");
    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);

    r.addKeyFieldDescription(0);
    r.addKeyFieldDescription(1);
    r.addKeyFieldDescription(2);

    String[] names = r.getKeyFieldNames();

    //Verify the names
    if (!names[0].equals("binaryField"))
    {
      failed("Wrong name returned");
      return;
    }
    if (!names[1].equals("char"))
    {
      failed("Wrong name returned");
      return;
    }
    if (!names[2].equals("flotilla Of Floats"))
    {
      failed("Wrong name returned");
      return;
    }
    // Verify number of names returned
    if (names.length != 3)
    {
      failed("Wrong number of names returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify getKeyFieldDescriptions() when no fields have been added yet.
   *<ul compact>
   *<li>Null constructor used
   *<li>RecordFormat(String) used
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>An array of size 0 will be returned.
   *</ul>
  **/
  public void Var036()
  {
    RecordFormat r = new RecordFormat();
    // Verify array of size 0 returned on empty record format
    if (r.getKeyFieldDescriptions().length != 0)
    {
      failed("Key field descriptions returned on empty record format");
      return;
    }

    r = new RecordFormat("blah");
    // Verify array of size 0 returned on empty record format
    if (r.getKeyFieldDescriptions().length != 0)
    {
      failed("Key field descriptions returned on empty record format");
      return;
    }
    succeeded();
  }

  /**
   *Verify getKeyFieldDescriptions() when fields have been added.
   *<br>
   *Expected results:
   *<ul compact>
   *<li>An array of the key field descriptions added will be returned.
   *</ul>
  **/
  public void Var037()
  {
    RecordFormat r = new RecordFormat();
    BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "binaryField");
    CharacterFieldDescription c = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "char");
    FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "flotilla Of Floats");

    r.addFieldDescription(b);
    r.addFieldDescription(c);
    r.addFieldDescription(f);
    r.addKeyFieldDescription(0);
    r.addKeyFieldDescription(1);
    r.addKeyFieldDescription(2);

    FieldDescription[] fields = r.getKeyFieldDescriptions();

    //Verify the names
    if (fields[0] != b)
    {
      failed("Wrong field description returned");
      return;
    }
    if (fields[1] != c)
    {
      failed("Wrong field description returned");
      return;
    }
    if (fields[2] != f)
    {
      failed("Wrong field description returned");
      return;
    }
    // Verify number of names returned
    if (fields.length != 3)
    {
      failed("Wrong number of descriptions returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify getName().
   *<ul compact>
   *<li>Null constructor.
   *Expected results:
   *<ul compact>
   *<li>Empty string will be returned.
   *</ul>
  **/
  public void Var038()
  {
    RecordFormat r = new RecordFormat();
    //Verify empty string returned when no name was specified
    if (!r.getName().equals(""))
    {
      failed("Empty string not returned when no name was specified");
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify getName().
   *<ul compact>
   *<li>Name set on the constructor.
   *Expected results:
   *<ul compact>
   *<li>The name, in uppercase, will be returned.
   *</ul>
  **/
  public void Var039()
  {
    RecordFormat r2 = new RecordFormat("format");

    // Verify name when name was specified
    if (!r2.getName().equals("FORMAT"))
    {
      failed("Incorrect name returned from getName(): " + r2.getName());
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify getName().
   *<ul compact>
   *<li>Name set with setName().
   *Expected results:
   *<ul compact>
   *<li>The name, in uppercase, will be returned.
   *</ul>
  **/
  public void Var040()
  {
    RecordFormat r2 = new RecordFormat();
    try
    {
      r2.setName("format");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    // Verify name when name was specified
    if (!r2.getName().equals("FORMAT"))
    {
      failed("Incorrect name returned from getName(): " + r2.getName());
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify getNumberOfFields().
   *<ul compact>
   *<li>No field decsriptions have been added.
   *<ul compact>
   *<li>null constructor
   *<li>RecordFormat(String)
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfFields() will return 0.
   *</ul>
  **/
  public void Var041()
  {
    RecordFormat r = new RecordFormat();
    //Verify that we start out with no fields
    if (r.getNumberOfFields() != 0)
    {
      failed("Wrong number of fields returned when record format is empty");
      return;
    }
    r = new RecordFormat("blah");
    //Verify that we start out with no fields
    if (r.getNumberOfFields() != 0)
    {
      failed("Wrong number of fields returned when record format is empty");
      return;
    }
    succeeded();
  }

  /**
   *Verify getNumberOfFields().
   *<ul compact>
   *<li>Add three field decsriptions to the record format object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfFields() will return 3.
   *</ul>
  **/
  public void Var042()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields and then verify the number of fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    if (r.getNumberOfFields() != 3)
    {
      failed("Wrong number of fields returned");
    }
    else
    {
      succeeded();
    }
  }

  /**
   *Verify getNumberOfKeyFields().
   *<ul compact>
   *<li>No field decsriptions have been added.
   *<ul compact>
   *<li>null constructor
   *<li>RecordFormat(String)
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfKeyFields() will return 0.
   *</ul>
  **/
  public void Var043()
  {
    RecordFormat r = new RecordFormat();
    //Verify that we start out with no fields
    if (r.getNumberOfKeyFields() != 0)
    {
      failed("Wrong number of key fields returned when record format is empty");
      return;
    }
    r = new RecordFormat("blah");
    //Verify that we start out with no fields
    if (r.getNumberOfKeyFields() != 0)
    {
      failed("Wrong number of key fields returned when record format is empty");
      return;
    }
    succeeded();
  }

  /**
   *Verify getNumberOfKeyFields().
   *<ul compact>
   *<li>Add three field decsriptions to the record format object.
   *<li>Add three key field decsriptions to the record format object.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getNumberOfKeyFields() will return 3.
   *</ul>
  **/
  public void Var044()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));

    // Add three key fields and then verify the number of fields
    r.addKeyFieldDescription("b");
    r.addKeyFieldDescription("b");
    r.addKeyFieldDescription("b");

    if (r.getNumberOfKeyFields() != 3)
    {
      failed("Wrong number of key fields returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getLengthDependency(int).
   *<ul compact>
   *<li>Field has no length dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>-1 will be returned.
   *</ul>
  **/
  public void Var045()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    if (r.getLengthDependency(0) != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getLengthDependency(1) != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getLengthDependency(2) != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getLengthDependency(int).
   *<ul compact>
   *<li>Field has a length dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The expected dependency info will be returned.
   *</ul>
  **/
  public void Var046()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    r.setLengthDependency(2,1);

    if (r.getLengthDependency(2) != 1)
    {
      failed("Incorrect dependent field index returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getLengthDependency(int).
   *<ul compact>
   *<li>index < 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var047()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getLengthDependency(-1);
      failed("Expected exception did not occur for getLengthDependency(-1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getLengthDependency(int).
   *<ul compact>
   *<li>index > number of fields - 1.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var048()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getLengthDependency(3);
      failed("Expected exception did not occur for getLengthDependency(-1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getLengthDependency(String).
   *<ul compact>
   *<li>Field has no length dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>-1 will be returned.
   *</ul>
  **/
  public void Var049()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    if (r.getLengthDependency("a") != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getLengthDependency("b") != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getLengthDependency("c") != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getLengthDependency(String).
   *<ul compact>
   *<li>Field has a length dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The expected dependency info will be returned.
   *</ul>
  **/
  public void Var050()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    r.setLengthDependency(2,1);

    if (r.getLengthDependency("c") != 1)
    {
      failed("Incorrect dependent field index returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getLengthDependency(String).
   *<ul compact>
   *<li>name = null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "name"
   *</ul>
  **/
  public void Var051()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getLengthDependency(null);
      failed("Expected exception did not occur for getLengthDependency(null)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getLengthDependency(String).
   *<ul compact>
   *<li>Non existent name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var052()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getLengthDependency("blah");
      failed("Expected exception did not occur for getLengthDependency()");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getOffsetDependency(int).
   *<ul compact>
   *<li>Field has no offset dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>-1 will be returned.
   *</ul>
  **/
  public void Var053()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    if (r.getOffsetDependency(0) != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getOffsetDependency(1) != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getOffsetDependency(2) != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getOffsetDependency(int).
   *<ul compact>
   *<li>Field has a offset dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The expected dependency info will be returned.
   *</ul>
  **/
  public void Var054()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    r.setOffsetDependency(2,1);

    if (r.getOffsetDependency(2) != 1)
    {
      failed("Incorrect dependent field index returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getOffsetDependency(int).
   *<ul compact>
   *<li>index < 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var055()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getOffsetDependency(-1);
      failed("Expected exception did not occur for getOffsetDependency(-1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getOffsetDependency(int).
   *<ul compact>
   *<li>index > number of fields - 1.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "index" and
   *RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var056()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getOffsetDependency(3);
      failed("Expected exception did not occur for getOffsetDependency(-1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "index", ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of getOffsetDependency(String).
   *<ul compact>
   *<li>Field has no offset dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>-1 will be returned.
   *</ul>
  **/
  public void Var057()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    if (r.getOffsetDependency("a") != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getOffsetDependency("b") != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    if (r.getOffsetDependency("c") != -1)
    {
      failed("-1 not returned for non dependent field");
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of getOffsetDependency(String).
   *<ul compact>
   *<li>Field has a offset dependency
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The expected dependency info will be returned.
   *</ul>
  **/
  public void Var058()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    r.setOffsetDependency(2,1);

    if (r.getOffsetDependency("c") != 1)
    {
      failed("Incorrect dependent field index returned");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getOffsetDependency(String).
   *<ul compact>
   *<li>name = null
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "name"
   *</ul>
  **/
  public void Var059()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getOffsetDependency(null);
      failed("Expected exception did not occur for getOffsetDependency(null)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of getOffsetDependency(String).
   *<ul compact>
   *<li>Non existent name
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating "name" and
   *FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var060()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.getOffsetDependency("blah");
      failed("Expected exception did not occur for getOffsetDependency()");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name", ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Incorrect exception/info.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setName().
   *<ul compact>
   *<li>Pass no name on the constructor, use setName() to set it.
   *Expected results:
   *<ul compact>
   *<li>The name specified by setName() will be returned.
   *</ul>
  **/
  public void Var061()
  {
    RecordFormat r = new RecordFormat();
    try
    {
      r.setName("myRF1");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }

    if (!r.getName().equals("MYRF1"))
    {
      failed("Incorrect name returned from getName() for myRF1: " + r.getName());
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setName().
   *<ul compact>
   *<li>Pass name on the constructor, use setName() to set it.
   *Expected results:
   *<ul compact>
   *<li>The name specified by setName() will be returned.
   *</ul>
  **/
  public void Var062()
  {
    RecordFormat r2 = new RecordFormat("format");
    try
    {
      r2.setName("myRF2");
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }

    if (!r2.getName().equals("MYRF2"))
    {
      failed("Incorrect name returned from getName() for myRF2: " + r2.getName());
      return;
    }
    succeeded();
  }

  /**
   *Verify valid usage of setName().
   *<ul compact>
   *<li>Use setName() multiple time to set the name.
   *Expected results:
   *<ul compact>
   *<li>The name specified by the last setName() will be returned each time.
   *</ul>
  **/
  public void Var063()
  {
    RecordFormat r2 = new RecordFormat("format");
    try
    {
      r2.setName("myRF2");
      if (!r2.getName().equals("MYRF2"))
      {
        failed("Incorrect name returned from getName() for myRF2: " + r2.getName());
        return;
      }
      r2.setName("agAIN");
      if (!r2.getName().equals("AGAIN"))
      {
        failed("Incorrect name returned from getName() for AGAIN: " + r2.getName());
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }

    succeeded();
  }

  /**
   *Verify invalid usage of setName().<br>
   *<ul compact>
   *<li>Pass null for the name.
   *Expected results:
   *<ul compact>
   *<li>NullPointerException specifying "name".
   *</ul>
  **/
  public void Var064()
  {
    RecordFormat r = new RecordFormat();
    try
    {
      r.setName(null);
      failed("Expected exception did not occur on setName(null).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failed(e, "Wrong parameter in null pointer exception.");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setLengthDependency(int, int).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getLengthDependency() will return the correct index of the depended
   *on field for the dependent field.
   *</ul>
  **/
  public void Var065()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));

    r.setLengthDependency(1,0);
    if (r.getLengthDependency(1) != 0)
    {
      failed("Incorrect field for setLengthDependency(1,0).");
      return;
    }
    r.setLengthDependency(2,1);
    if (r.getLengthDependency(2) != 1)
    {
      failed("Incorrect field for setLengthDependency(2,1).");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(int, int).
   *<ul compact>
   *<li>With fieldDependedOn > dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var066()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency(0,1);
      failed("Failed to throw exception on setLengthDependency(0,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed("Incorrect exception info on setLengthDependency(0,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(int, int).
   *<ul compact>
   *<li>With fieldDependedOn == dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var067()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency(1,1);
      failed("Failed to throw exception on setLengthDependency(1,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed("Incorrect exception info on setLengthDependency(1,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(int, int).
   *<ul compact>
   *<li>With fieldDependedOn < 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var068()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency(1,-1);
      failed("Failed to throw exception on setLengthDependency(1,-1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Incorrect exception info on setLengthDependency(1,-1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(int, int).
   *<ul compact>
   *<li>With dependentField < 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "dependentField"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var069()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency(-1,1);
      failed("Failed to throw exception on setLengthDependency(-1,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dependentField",                      ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Incorrect exception info on setLengthDependency(-1,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(int, int).
   *<ul compact>
   *<li>With dependentField > number of fields - 1.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "dependentField"
   *and value of RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var070()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency(3,1);
      failed("Failed to throw exception on setLengthDependency(3,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dependentField",                       ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Incorrect exception info on setLengthDependency(3,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setLengthDependency(String, String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The expected dependency info for the depndent field will be returned.
   *</ul>
  **/
  public void Var071()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));

    r.setLengthDependency("b","a");
    if (r.getLengthDependency("b") != 0)
    {
      failed("Incorrect field for setLengthDependency(b,a).");
      return;
    }
    r.setLengthDependency("c","b");
    if (r.getLengthDependency("c") != 1)
    {
      failed("Incorrect field for setLengthDependency(b,a).");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(String, String).
   *<ul compact>
   *<li>With dependentField as null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException specifying "dependentField"
   *</ul>
  **/
  public void Var072()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency(null,"b");
      failed("Failed to throw exception on setLengthDependency(null,b).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dependentField"))
      {
        failed(e, "Incorrect exception info on setLengthDependency(null,b).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn as null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException specifying "fieldDependedOn"
   *</ul>
  **/
  public void Var073()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency("b", null);
      failed("Failed to throw exception on setLengthDependency(b, null).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "fieldDependedOn"))
      {
        failed(e, "Incorrect exception info on setLengthDependency(b, null).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn not a valid field name.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var074()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency("hello","c");
      failed("Failed to throw exception on setLengthDependency(noexist, c).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (hello)",                       ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Incorrect exception info on setLengthDependency(noexist, c).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(String, String).
   *<ul compact>
   *<li>With dependentField not a valid field name.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "dependentField"
   *and value of FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var075()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency("c", "hello");
      failed("Failed to throw exception on setLengthDependency(c, noexist).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (hello)",                       ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Incorrect exception info on setLengthDependency(c, noexist).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn > dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var076()
  {
    RecordFormat r = new RecordFormat();

    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency("b","c");
      failed("Failed to throw exception on setLengthDependency(b, c).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception info on setLengthDependency(b, c).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setLengthDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn == dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var077()
  {
    RecordFormat r = new RecordFormat();

    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setLengthDependency("b","b");
      failed("Failed to throw exception on setLengthDependency(b, b).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception info on setLengthDependency(b, b).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setOffsetDependency(int, int).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getOffsetDependency() will return the correct index of the depended
   *on field for the dependent field.
   *</ul>
  **/
  public void Var078()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));

    r.setOffsetDependency(1,0);
    if (r.getOffsetDependency(1) != 0)
    {
      failed("Incorrect field for setOffsetDependency(1,0).");
      return;
    }
    r.setOffsetDependency(2,1);
    if (r.getOffsetDependency(2) != 1)
    {
      failed("Incorrect field for setOffsetDependency(2,1).");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(int, int).
   *<ul compact>
   *<li>With fieldDependedOn > dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var079()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency(0,1);
      failed("Failed to throw exception on setOffsetDependency(0,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                      ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed("Incorrect exception info on setOffsetDependency(0,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(int, int).
   *<ul compact>
   *<li>With fieldDependedOn == dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var080()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency(1,1);
      failed("Failed to throw exception on setOffsetDependency(1,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed("Incorrect exception info on setOffsetDependency(1,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(int, int).
   *<ul compact>
   *<li>With fieldDependedOn < 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var081()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency(1,-1);
      failed("Failed to throw exception on setOffsetDependency(1,-1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Incorrect exception info on setOffsetDependency(1,-1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(int, int).
   *<ul compact>
   *<li>With dependentField < 0.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "dependentField"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var082()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency(-1,1);
      failed("Failed to throw exception on setOffsetDependency(-1,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dependentField",                      ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Incorrect exception info on setOffsetDependency(-1,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(int, int).
   *<ul compact>
   *<li>With dependentField > number of fields - 1.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "dependentField"
   *and value of RANGE_NOT_VALID.
   *</ul>
  **/
  public void Var083()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency(3,1);
      failed("Failed to throw exception on setOffsetDependency(3,1)");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "dependentField",                       ExtendedIllegalArgumentException.RANGE_NOT_VALID))
      {
        failed("Incorrect exception info on setOffsetDependency(3,1).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify valid usage of setOffsetDependency(String, String).
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The expected dependency info for the depndent field will be returned.
   *</ul>
  **/
  public void Var084()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));

    r.setOffsetDependency("b","a");
    if (r.getOffsetDependency("b") != 0)
    {
      failed("Incorrect field for setOffsetDependency(b,a).");
      return;
    }
    r.setOffsetDependency("c","b");
    if (r.getOffsetDependency("c") != 1)
    {
      failed("Incorrect field for setOffsetDependency(b,a).");
      return;
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(String, String).
   *<ul compact>
   *<li>With dependentField as null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException specifying "dependentField"
   *</ul>
  **/
  public void Var085()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency(null,"b");
      failed("Failed to throw exception on setOffsetDependency(null,b).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dependentField"))
      {
        failed(e, "Incorrect exception info on setOffsetDependency(null,b).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn as null.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException specifying "fieldDependedOn"
   *</ul>
  **/
  public void Var086()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency("b", null);
      failed("Failed to throw exception on setOffsetDependency(b, null).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "fieldDependedOn"))
      {
        failed(e, "Incorrect exception info on setOffsetDependency(b, null).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn not a valid field name.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var087()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency("hello","c");
      failed("Failed to throw exception on setOffsetDependency(noexist, c).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (hello)",                       ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Incorrect exception info on setOffsetDependency(noexist, c).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(String, String).
   *<ul compact>
   *<li>With dependentField not a valid field name.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "dependentField"
   *and value of FIELD_NOT_FOUND.
   *</ul>
  **/
  public void Var088()
  {
    RecordFormat r = new RecordFormat();
    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency("c", "hello");
      failed("Failed to throw exception on setOffsetDependency(c, noexist).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "name (hello)",                       ExtendedIllegalArgumentException.FIELD_NOT_FOUND))
      {
        failed(e, "Incorrect exception info on setOffsetDependency(c, noexist).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn > dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var089()
  {
    RecordFormat r = new RecordFormat();

    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency("b","c");
      failed("Failed to throw exception on setOffsetDependency(b, c).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception info on setOffsetDependency(b, c).");
        return;
      }
    }
    succeeded();
  }

  /**
   *Verify invalid usage of setOffsetDependency(String, String).
   *<ul compact>
   *<li>With fieldDependedOn == dependentField.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException specifying "fieldDependedOn"
   *and value of PARAMETER_VALUE_NOT_VALID.
   *</ul>
  **/
  public void Var090()
  {
    RecordFormat r = new RecordFormat();

    // Add three fields
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "a"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "b"));
    r.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "c"));
    try
    {
      r.setOffsetDependency("b","b");
      failed("Failed to throw exception on setOffsetDependency(b, b).");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "fieldDependedOn",                       ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
      {
        failed(e, "Incorrect exception info on setOffsetDependency(b, b).");
        return;
      }
    }
    succeeded();
  }
  
  
  /**
   * Verify incorrect usage of setRecordFormatType(int).
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException specifying "type"
   * and value of PARAMETER_VALUE_NOT_VALID.
   * </ul>
  **/
  public void Var091()
  {
    RecordFormat r = new RecordFormat();

    // create a new 'layout length'
    int MIXED_LAYOUT_LENGTH = 4;
    
    try {
        r.setRecordFormatType(MIXED_LAYOUT_LENGTH);
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "type",
                                ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)) {
        failed(e, "Incorrect exception generated.");
        return;
      }
      succeeded();
    }
  }
  
   
  /**
   * Tests setRecordFormatType(int) and getRecordFormatType() methods.
   **/
  public void Var092()
  {
    RecordFormat r = new RecordFormat();
   
    r.setRecordFormatType(r.FIXED_LAYOUT_LENGTH);
    
    int rftype = r.getRecordFormatType();
    if (rftype != r.FIXED_LAYOUT_LENGTH) {
        failed("Failed to set record type to FIXED_LAYOUT_LENGTH");
    }
    else {
        r.setRecordFormatType(r.VARIABLE_LAYOUT_LENGTH);
    
        rftype = r.getRecordFormatType();
        if (rftype != r.VARIABLE_LAYOUT_LENGTH) {
            failed("Failed to set record type to VARIABLE_LAYOUT_LENGTH");
        }
        else {
            succeeded();
        }
    }
  }

  
  /**
   * Verify incorrect usage of setRecordFormatID(String).
   * Expected results:
   * <ul compact>
   * <li>ExtendedIllegalArgumentException specifying "id"
   * and value of LENGTH_NOT_VALID.
   * </ul>
   **/
  public void Var093()
  {
    RecordFormat r = new RecordFormat();

    // create a new 'layout length'
    String id = "THISISTOLONG";
    
    try {
        r.setRecordFormatID(id);
    }
    catch(Exception e)
    {
      if (!exceptionStartsWith(e, "ExtendedIllegalArgumentException", "id", 
                            ExtendedIllegalArgumentException.LENGTH_NOT_VALID)) {
        failed(e, "Incorrect exception generated.");
        return;
      }
      succeeded();
    }
  }
  

  
  /**
   * Test the setRecordFormatID(String) and getRecordFormatID() methods.
   **/
  public void Var094()
  {
    RecordFormat r = new RecordFormat();
   
    String id = "CUSREC";
    
    r.setRecordFormatID(id);
    
    String rfid = r.getRecordFormatID();
    if (rfid.equals("CUSREC    ")) {  // remember, its gets padded less than 10
        r.setRecordFormatID("THISISTENN");
        rfid = r.getRecordFormatID();
        if (rfid.equals("THISISTENN")) {
            succeeded();
        }
        else {
            failed("Failed to retrieve correct Record Format ID - THISISTENN.");
        }
    }
    else {
        failed("Failed to retrieve correct Record Format ID - CUSREC.");
    }
  }
    
    
  /**
   * Tests setDelimiter(char) and getDelimiter().
   **/
  public void Var095()
  {
    RecordFormat r = new RecordFormat();
   
    char delimiter = ' ';
    
    r.setDelimiter(delimiter);
    r.setDelimiter(';');
    if (r.getDelimiter() == ';') {
        succeeded();
    }
    else {
        failed("Failed to retrieve Record delimiter.");
    }
  }    
  
}



