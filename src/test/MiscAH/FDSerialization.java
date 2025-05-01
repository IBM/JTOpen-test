///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FDSerialization.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

//@B0A
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Array;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400ZonedDecimal;
import com.ibm.as400.access.ArrayFieldDescription;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.DBCSEitherFieldDescription;
import com.ibm.as400.access.DBCSGraphicFieldDescription;
import com.ibm.as400.access.DBCSOnlyFieldDescription;
import com.ibm.as400.access.DBCSOpenFieldDescription;
import com.ibm.as400.access.DateFieldDescription;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.FloatFieldDescription;
import com.ibm.as400.access.HexFieldDescription;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.TimestampFieldDescription;
import com.ibm.as400.access.ZonedDecimalFieldDescription;

import test.Testcase;

/**
 *Testcase FDSerialization.  This test class verifies the abillity to
 *serialize and deserialize FieldDescription objects.
**/
public class FDSerialization extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "FDSerialization";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FDTest.main(newArgs); 
   }
  // Values for the various properties of a FieldDescription.
  // Use to set the base state of a field description object and
  // to verify the state after de-serialization.
  String alias = "theAlias";
  boolean alwnull = true;
  String colhdg = "'The' 'First' 'Column'";
  String ddsName = "ddsNAME";
  String[] keyFunctions = {"LIFO", "ZONED", "DIGIT"};
  String reffld = "QSYS/MYFILE FORMAT";
  String text = "theTextDescription";

  private static final String serFile_ = "fdser.ser"; //@B0A

  /**
   *Constructor.  This is called from FDTest::createTestcases().
  **/
  public FDSerialization(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    super(systemObject, "FDSerialization", 28,
          variationsToRun, runMode, fileOutputStream);
  }

  //@B0A
  /**
   * Cleans up any of the .ser files leftover from previous
   * runs of this testcase.
  **/
  private void cleanupFiles()
  {
    try
    {
      File f = new File(serFile_);
      f.delete();
    }
    catch(Exception e)
    {
    }
  }
  
  /**
  Runs the variations requested.
  **/
  public void run()
  {
    cleanupFiles(); //@B0A

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
    cleanupFiles(); //@B0A
  }

  /**
   *Verify the ability to serialize and deserialize an ArrayFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      // Use null constructor to get no state
      ArrayFieldDescription f = new ArrayFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      ArrayFieldDescription deserf = (ArrayFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Array(new AS400Bin4(), 5));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a ArrayFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      // Construct and set the state
      ArrayFieldDescription f = new ArrayFieldDescription(new AS400Array(new AS400Bin2(), 10), "field1");
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      ArrayFieldDescription deserf = (ArrayFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Array) ||
          ((AS400Array)dt).getNumberOfElements() != 10 ||
          ((AS400Array)dt).getByteLength() != 20)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Array(new AS400Bin4(), 5));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an BinaryFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      // Use null constructor to get no state
      BinaryFieldDescription f = new BinaryFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      BinaryFieldDescription deserf = (BinaryFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Bin4());
      f.setFieldName("field");
      f.setLength(9);
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a BinaryFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      // Construct and set the state
      BinaryFieldDescription f = new BinaryFieldDescription(new AS400Bin2(), "field1", "blah", 9);
      f.setDFT(new Short((short)5));
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      BinaryFieldDescription deserf = (BinaryFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Bin2))
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getLength() != 9)
      {
        failed("length in error");
        return;
      }
      if (((Short)f.getDFT()).intValue() != 5)
      {
        failed("DFT in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Bin4());
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an CharacterFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var005()
  {
    try
    {
      // Use null constructor to get no state
      CharacterFieldDescription f = new CharacterFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      CharacterFieldDescription deserf = (CharacterFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getCCSID().equals(""))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      if (f.getVARLEN() != 0)
      {
        failed("0 not returned for VARLEN");
        return;
      }
      if (f.isVariableLength())
      {
        failed("false not returned for isVariableLength");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, 37, systemObject_));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a CharacterFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var006()
  {
    try
    {
      // Construct and set the state
      CharacterFieldDescription f = new CharacterFieldDescription(new AS400Text(15, 37, systemObject_), "field1");
      f.setDFT("default");
      f.setCCSID("65535");
      f.setVARLEN(50);
      f.setVariableLength(true);
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      CharacterFieldDescription deserf = (CharacterFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 15)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getVARLEN() != 50)
      {
        failed("VARLEN in error");
        return;
      }
      if (!f.isVariableLength())
      {
        failed("isVariableLength in error");
        return;
      }
      if (!f.getDFT().equals("default"))
      {
        failed("DFT in error");
        return;
      }
      if (!f.getCCSID().equals("65535"))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Text(2, 37, systemObject_));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSEitherFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var007()
  {
    try
    {
      // Use null constructor to get no state
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSEitherFieldDescription deserf = (DBCSEitherFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getCCSID().equals(""))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      if (f.getVARLEN() != 0)
      {
        failed("0 not returned for VARLEN");
        return;
      }
      if (f.isVariableLength())
      {
        failed("false not returned for isVariableLength");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, 37, systemObject_));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSEitherFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var008()
  {
    try
    {
      // Construct and set the state
      DBCSEitherFieldDescription f = new DBCSEitherFieldDescription(new AS400Text(15, 37, systemObject_), "field1");
      f.setDFT("default");
      f.setCCSID("65535");
      f.setVARLEN(50);
      f.setVariableLength(true);
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSEitherFieldDescription deserf = (DBCSEitherFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 15)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getVARLEN() != 50)
      {
        failed("VARLEN in error");
        return;
      }
      if (!f.isVariableLength())
      {
        failed("isVariableLength in error");
        return;
      }
      if (!f.getDFT().equals("default"))
      {
        failed("DFT in error");
        return;
      }
      if (!f.getCCSID().equals("65535"))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Text(2, 37, systemObject_));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSGraphicFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var009()
  {
    try
    {
      // Use null constructor to get no state
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSGraphicFieldDescription deserf = (DBCSGraphicFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getCCSID().equals(""))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      if (f.getVARLEN() != 0)
      {
        failed("0 not returned for VARLEN");
        return;
      }
      if (f.isVariableLength())
      {
        failed("false not returned for isVariableLength");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, 37, systemObject_));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSGraphicFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var010()
  {
    try
    {
      // Construct and set the state
      DBCSGraphicFieldDescription f = new DBCSGraphicFieldDescription(new AS400Text(15, 37, systemObject_), "field1");
      f.setDFT("default");
      f.setCCSID("65535");
      f.setVARLEN(50);
      f.setVariableLength(true);
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSGraphicFieldDescription deserf = (DBCSGraphicFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 15)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getVARLEN() != 50)
      {
        failed("VARLEN in error");
        return;
      }
      if (!f.isVariableLength())
      {
        failed("isVariableLength in error");
        return;
      }
      if (!f.getDFT().equals("default"))
      {
        failed("DFT in error");
        return;
      }
      if (!f.getCCSID().equals("65535"))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Text(2, 37, systemObject_));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSOnlyFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var011()
  {
    try
    {
      // Use null constructor to get no state
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSOnlyFieldDescription deserf = (DBCSOnlyFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getCCSID().equals(""))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      if (f.getVARLEN() != 0)
      {
        failed("0 not returned for VARLEN");
        return;
      }
      if (f.isVariableLength())
      {
        failed("false not returned for isVariableLength");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, 37, systemObject_));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSOnlyFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var012()
  {
    try
    {
      // Construct and set the state
      DBCSOnlyFieldDescription f = new DBCSOnlyFieldDescription(new AS400Text(15, 37, systemObject_), "field1");
      f.setDFT("default");
      f.setCCSID("65535");
      f.setVARLEN(50);
      f.setVariableLength(true);
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSOnlyFieldDescription deserf = (DBCSOnlyFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 15)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getVARLEN() != 50)
      {
        failed("VARLEN in error");
        return;
      }
      if (!f.isVariableLength())
      {
        failed("isVariableLength in error");
        return;
      }
      if (!f.getDFT().equals("default"))
      {
        failed("DFT in error");
        return;
      }
      if (!f.getCCSID().equals("65535"))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Text(2, 37, systemObject_));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an DBCSOpenFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var013()
  {
    try
    {
      // Use null constructor to get no state
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSOpenFieldDescription deserf = (DBCSOpenFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getCCSID().equals(""))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      if (f.getVARLEN() != 0)
      {
        failed("0 not returned for VARLEN");
        return;
      }
      if (f.isVariableLength())
      {
        failed("false not returned for isVariableLength");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, 37, systemObject_));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DBCSOpenFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var014()
  {
    try
    {
      // Construct and set the state
      DBCSOpenFieldDescription f = new DBCSOpenFieldDescription(new AS400Text(15, 37, systemObject_), "field1");
      f.setDFT("default");
      f.setCCSID("65535");
      f.setVARLEN(50);
      f.setVariableLength(true);
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DBCSOpenFieldDescription deserf = (DBCSOpenFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 15)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getVARLEN() != 50)
      {
        failed("VARLEN in error");
        return;
      }
      if (!f.isVariableLength())
      {
        failed("isVariableLength in error");
        return;
      }
      if (!f.getDFT().equals("default"))
      {
        failed("DFT in error");
        return;
      }
      if (!f.getCCSID().equals("65535"))
      {
        failed("Empty string not returned for CCSID");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Text(2, 37, systemObject_));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an DateFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var015()
  {
    try
    {
      // Use null constructor to get no state
      DateFieldDescription f = new DateFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DateFieldDescription deserf = (DateFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getDATFMT().equals(""))
      {
        failed("Empty string not returned for DATFMT");
        return;
      }
      if (!f.getDATSEP().equals(""))
      {
        failed("Empty string not returned for DATSEP");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, systemObject_.getCcsid(), systemObject_));
      f.setFieldName("field");
      f.setDATFMT("*ISO");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a DateFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var016()
  {
    try
    {
      // Construct and set the state
      DateFieldDescription f = new DateFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "field1");
      f.setDFT("1997/01/01");
      f.setDATFMT("*ISO");
      f.setDATSEP("/");
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      DateFieldDescription deserf = (DateFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 10)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (!f.getDFT().equals("1997/01/01"))
      {
        failed("DFT in error");
        return;
      }
      if(!f.getDATFMT().equals("*ISO"))
      {
        failed("DATFMT in error");
        return;
      }
      if(!f.getDATSEP().equals("/"))
      {
        failed("DATSEP in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDATFMT("*USA");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an FloatFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var017()
  {
    try
    {
      // Use null constructor to get no state
      FloatFieldDescription f = new FloatFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      FloatFieldDescription deserf = (FloatFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (!f.getFLTPCN().equals(""))
      {
        failed("Empty string not returned for FLTPCN");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (f.getDecimalPositions() != 0)
      {
        failed("getDecimalPositions not returning 0");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Float4());
      f.setFieldName("field");
      f.setLength(9);
      f.setDecimalPositions(5);
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a FloatFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var018()
  {
    try
    {
      // Construct and set the state
      FloatFieldDescription f = new FloatFieldDescription(new AS400Float8(), "field1", "blah", 17, 5);
      f.setDFT(new Double("15467.23456"));
      f.setFLTPCN("*DOUBLE");
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      FloatFieldDescription deserf = (FloatFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Float8))
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getLength() != 17)
      {
        failed("length in error");
        return;
      }
      if (f.getDecimalPositions() != 5)
      {
        failed("decimal positions in error");
        return;
      }
      if (!((Double)f.getDFT()).toString().equals("15467.23456"))
      {
        failed("DFT in error");
        return;
      }
      if (!f.getFLTPCN().equals("*DOUBLE"))
      {
        failed("FLTPCN in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Float4());
      f.setLength(5);
      f.setDecimalPositions(2);
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an HexFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var019()
  {
    try
    {
      // Use null constructor to get no state
      HexFieldDescription f = new HexFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      HexFieldDescription deserf = (HexFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (f.getVARLEN() != 0)
      {
        failed("0 not returned for VARLEN");
        return;
      }
      if (f.isVariableLength())
      {
        failed("false not returned for isVariableLength");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400ByteArray(10));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a HexFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var020()
  {
    try
    {
      // Construct and set the state
      HexFieldDescription f = new HexFieldDescription(new AS400ByteArray(15), "field1");
      f.setDFT(new byte[15]);
      f.setVARLEN(50);
      f.setVariableLength(true);
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      HexFieldDescription deserf = (HexFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400ByteArray) ||
          ((AS400ByteArray)dt).getByteLength() != 15)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getVARLEN() != 50)
      {
        failed("VARLEN in error");
        return;
      }
      if (!f.isVariableLength())
      {
        failed("isVariableLength in error");
        return;
      }
      for (int i = 0; i < ((byte[])f.getDFT()).length; ++i)
      {
        if (((byte[])f.getDFT())[i] != 0x00)
        {
          failed("DFT in error");
          return;
        }
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400ByteArray(2));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an PackedDecimalFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var021()
  {
    try
    {
      // Use null constructor to get no state
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      PackedDecimalFieldDescription deserf = (PackedDecimalFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (f.getDecimalPositions() != 0)
      {
        failed("getDecimalPositions not returning 0");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400PackedDecimal(15,5));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a PackedDecimalFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var022()
  {
    try
    {
      // Construct and set the state
      PackedDecimalFieldDescription f = new PackedDecimalFieldDescription(new AS400PackedDecimal(20,3), "field1");
      f.setDFT(new BigDecimal("15555555.123"));
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      PackedDecimalFieldDescription deserf = (PackedDecimalFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400PackedDecimal))
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getLength() != 20)
      {
        failed("length in error");
        return;
      }
      if (f.getDecimalPositions() != 3)
      {
        failed("decimal positions in error");
        return;
      }
      if (!((BigDecimal)f.getDFT()).toString().equals("15555555.123"))
      {
        failed("DFT in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400PackedDecimal(10,2));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an TimeFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var023()
  {
    try
    {
      // Use null constructor to get no state
      TimeFieldDescription f = new TimeFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      TimeFieldDescription deserf = (TimeFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (!f.getTIMFMT().equals(""))
      {
        failed("Empty string not returned for TIMFMT");
        return;
      }
      if (!f.getTIMSEP().equals(""))
      {
        failed("Empty string not returned for TIMSEP");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(10, systemObject_.getCcsid(), systemObject_));
      f.setFieldName("field");
      f.setTIMFMT("*ISO");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a TimeFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var024()
  {
    try
    {
      // Construct and set the state
      TimeFieldDescription f = new TimeFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), "field1");
      f.setDFT("12:59:23");
      f.setTIMFMT("*ISO");
      f.setTIMSEP(":");
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      TimeFieldDescription deserf = (TimeFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 8)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (!f.getDFT().equals("12:59:23"))
      {
        failed("DFT in error");
        return;
      }
      if(!f.getTIMFMT().equals("*ISO"))
      {
        failed("TIMFMT in error");
        return;
      }
      if(!f.getTIMSEP().equals(":"))
      {
        failed("TIMSEP in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setTIMFMT("*USA");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an TimestampFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var025()
  {
    try
    {
      // Use null constructor to get no state
      TimestampFieldDescription f = new TimestampFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      TimestampFieldDescription deserf = (TimestampFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400Text(26, systemObject_.getCcsid(), systemObject_));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a TimestampFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var026()
  {
    try
    {
      // Construct and set the state
      TimestampFieldDescription f = new TimestampFieldDescription(new AS400Text(26, systemObject_.getCcsid(), systemObject_), "field1");
      f.setDFT("1997-01-01.12.59.23.000000");
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      TimestampFieldDescription deserf = (TimestampFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400Text) ||
          ((AS400Text)dt).getByteLength() != 26)
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (!f.getDFT().equals("1997-01-01.12.59.23.000000"))
      {
        failed("DFT in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400Text(26, systemObject_.getCcsid(), systemObject_));
      f.setDFT("1998-01-01.12.00.23.000000");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize an ZonedDecimalFieldDescription
   *object.
   *<ul compact>
   *<li>When no state has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *<li>The object is usable.
   *</ul>
  **/
  public void Var027()
  {
    try
    {
      // Use null constructor to get no state
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription();

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      ZonedDecimalFieldDescription deserf = (ZonedDecimalFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      if (f.getDataType() != null)
      {
        failed("getDataType not returning null"+deserf);
        return;
      }
      if (!f.getFieldName().equals(""))
      {
        failed("Empty string not returned for field name");
        return;
      }
      if (!f.getDDSName().equals(""))
      {
        failed("Empty string not returned for dds name");
        return;
      }
      if (f.getDFT() != null)
      {
        failed("getDFT not returning null");
        return;
      }
      if (f.getDecimalPositions() != 0)
      {
        failed("getDecimalPositions not returning 0");
        return;
      }
      String chk = checkKeywordsNone("", f, 0);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }

      // Verify usability
      f.setDataType(new AS400ZonedDecimal(15,5));
      f.setFieldName("field");
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
      return;
    }
    succeeded();
  }

  /**
   *Verify the ability to serialize and deserialize a ZonedDecimalFieldDescription
   *object.
   *<ul compact>
   *<li>When the state of the object has been set
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object will be that of the object prior to
   *serialization.
   *</ul>
  **/
  public void Var028()
  {
    try
    {
      // Construct and set the state
      ZonedDecimalFieldDescription f = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(20,3), "field1");
      f.setDFT(new BigDecimal("15555555.123"));
      setBaseState(f);

      // Serialize
      FileOutputStream ros = new FileOutputStream(serFile_);
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(f);
      rout.flush();
      ros.close();

      // Deserialize
      FileInputStream ris = new FileInputStream(serFile_);
      ObjectInputStream rin = new ObjectInputStream(ris);
      ZonedDecimalFieldDescription deserf = (ZonedDecimalFieldDescription)rin.readObject();
      ris.close(); //@B0A

      // Verify state
      AS400DataType dt = f.getDataType();
      if (!(dt instanceof AS400ZonedDecimal))
      {
        failed("data type in error"+deserf);
        return;
      }
      if (!f.getFieldName().equals("field1"))
      {
        failed("field name in error");
        return;
      }
      if (f.getLength() != 20)
      {
        failed("length in error");
        return;
      }
      if (f.getDecimalPositions() != 3)
      {
        failed("decimal positions in error");
        return;
      }
      if (!((BigDecimal)f.getDFT()).toString().equals("15555555.123"))
      {
        failed("DFT in error");
        return;
      }
      String chk = verifyBaseState(f);
      if (!chk.equals(""))
      {
        failed(chk);
        return;
      }
      // Verify usability
      f.setDataType(new AS400ZonedDecimal(10,2));
      RecordFormat rf = new RecordFormat();
      rf.addFieldDescription(f);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      return;
    }
    succeeded();
  }

  // Verifies that the properties of the field description set in the
  // base class are correct for when the null constructor is used
  String checkKeywordsNone(String name, FieldDescription f, int len)
  {
    if (!f.getALIAS().equals(""))
    {
      return name + ": Alias keyword in error\n";
    }
    else if (!(f.getALWNULL() == false))
    {
      return name + ": Allow null keyword in error\n";
    }
    else if (!f.getCOLHDG().equals(""))
    {
      return name + ": COLHDG keyword in error\n";
    }
    else if (!(f.getKeyFieldFunctions() == null))
    {
      return name + ": Key field keywords in error\n";
    }
    else if (!(f.getLength() == len))
    {
      return name + ": Length in error\n";
    }
    else if (!f.getREFFLD().equals(""))
    {
      return name + ": REFFLD keyword in error\n";
    }
    else if (!f.getTEXT().equals(""))
    {
      return name + ": TEXT keyword in error\n";
    }
    return "";
  }

  // Set the properties of the field description that are set in the
  // base class.
  void setBaseState(FieldDescription f)
  {
    f.setALIAS(alias);
    f.setALWNULL(alwnull);
    f.setCOLHDG(colhdg);
    f.setDDSName(ddsName);
    f.setKeyFieldFunctions(keyFunctions);
    f.setREFFLD(reffld);
    f.setTEXT(text);
  }

  // Verify the properties of the field description that are set in the
  // base class (that were set by setBaseState()).
  String verifyBaseState(FieldDescription f)
  {
    if (!f.getALIAS().equals(alias))
    {
      return "ALIAS not correct";
    }
    if (f.getALWNULL() != alwnull)
    {
      return "ALWNULL not correct";
    }
    if (!f.getCOLHDG().equals(colhdg))
    {
      return "COLHDG not correct";
    }
    if (!f.getDDSName().equals(ddsName.toUpperCase()))
    {
      return "DDSName not correct";
    }
    for (int i = 0; i < f.getKeyFieldFunctions().length; ++i)
    {
      if (!f.getKeyFieldFunctions()[i].equals(keyFunctions[i]))
      {
        return "keyFieldFunctions not correct";
      }
    }
    if (!f.getREFFLD().equals(reffld))
    {
      return "REFFLD not correct";
    }
    if (!f.getTEXT().equals(text))
    {
      return "TEXT not correct";
    }
    return "";
  }
}



