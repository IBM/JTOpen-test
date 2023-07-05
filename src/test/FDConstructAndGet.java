///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FDConstructAndGet.java
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
import com.ibm.as400.access.AS400Array;
import com.ibm.as400.access.AS400Bin2;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400Bin8;
import com.ibm.as400.access.AS400ByteArray;
import com.ibm.as400.access.AS400Float4;
import com.ibm.as400.access.AS400Float8;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400UnsignedBin2;
import com.ibm.as400.access.AS400UnsignedBin4;
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
import com.ibm.as400.access.TimeFieldDescription;
import com.ibm.as400.access.TimestampFieldDescription;
import com.ibm.as400.access.ZonedDecimalFieldDescription;

/**
  Testcase FDConstructAndGet.  This test class verifes valid usage of the
  constructors and getters for the FieldDescription subclasses.  This test class also
  verifies the isVariableLength() and setVariableLength() methods for those
  classes which implement the VariableLengthFieldDescription interface.
**/
public class FDConstructAndGet extends Testcase
{
  /**
  Constructor.  This is called from the FDTest constructor.
  **/
  public FDConstructAndGet(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "FDConstructAndGet", 18,
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
}


  /**
   *Verify the following for BinaryFieldDescription.
   *<ul>
   *<li>Verify valid uses of the BinaryFieldDescription constructor.
   *<ul>
   *<li>BinaryFieldDescription(AS400Bin4, String)
   *<li>BinaryFieldDescription(AS400Bin4, String, String, int)
   *<li>BinaryFieldDescription(AS400Bin2, String)
   *<li>BinaryFieldDescription(AS400Bin2, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var001()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    // Integer dftVal = new Integer(0);
    // String[] keyFunctions = {"UNIQUE"};

    BinaryFieldDescription b1 = new BinaryFieldDescription(new AS400Bin4(), fieldName1);
    BinaryFieldDescription b2 = new BinaryFieldDescription(new AS400Bin4(), fieldName1,
                                                           ddsName1, 9);
    BinaryFieldDescription b4 = new BinaryFieldDescription(new AS400Bin4(), fieldName2);
    BinaryFieldDescription b5 = new BinaryFieldDescription(new AS400Bin2(), fieldName1);
    BinaryFieldDescription b6 = new BinaryFieldDescription(new AS400Bin2(), fieldName1,
                                                           ddsName1, 3); //@A1C
    BinaryFieldDescription b8 = new BinaryFieldDescription(new AS400Bin2(), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Bin4))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Bin4))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Bin4))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b5 object
    if (!(b5.getDataType() instanceof AS400Bin2))
    {
      failMsg += "b5: Wrong data type object returned on getDataType\n";
    }
    else if (!b5.getFieldName().equals(fieldName1))
    {
      failMsg += "b5: Wrong field name returned on getFieldName\n";
    }
    else if (!b5.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b5: Wrong field name returned on getDDSName\n";
    }
    else if (!(b5.getDFT() == null))
    {
      failMsg += "b5: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b5", b5, 4); //@A1C
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b6 object
    if (!(b6.getDataType() instanceof AS400Bin2))
    {
      failMsg += "b6: Wrong data type object returned on getDataType\n";
    }
    else if (!b6.getFieldName().equals(fieldName1))
    {
      failMsg += "b6: Wrong field name returned on getFieldName\n";
    }
    else if (!b6.getDDSName().equals(ddsName1))
    {
      failMsg += "b6: Wrong field name returned on getDDSName\n";
    }
    else if (!(b6.getDFT() == null))
    {
      failMsg += "b6: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b6", b6, 3); //@A1C
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b8 object
    if (!(b8.getDataType() instanceof AS400Bin2))
    {
      failMsg += "b8: Wrong data type object returned on getDataType\n";
    }
    else if (!b8.getFieldName().equals(fieldName2))
    {
      failMsg += "b8: Wrong field name returned on getFieldName\n";
    }
    else if (!b8.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b8: Wrong field name returned on getDDSName\n";
    }
    else if (!(b8.getDFT() == null))
    {
      failMsg += "b8: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b8", b8, 4); //@A1C
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following for CharacterFieldDescription.
   *<ul>
   *<li>Verify valid uses of the CharacterFieldDescription constructor.
   *<ul>
   *<li>CharacterFieldDescription(AS400Text, String)
   *<li>CharacterFieldDescription(AS400Text, String, String)
   *<li>CharacterFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var002()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    // String dftVal = "John Doe";
    // String[] keyFunctions = {"UNIQUE"};

    CharacterFieldDescription b1 = new CharacterFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName1);
    CharacterFieldDescription b2 = new CharacterFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    CharacterFieldDescription b4 = new CharacterFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getVARLEN() == 0))
    {
      failMsg += "b1: Wrong variable length on getVARLEN\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getVARLEN() == 0))
    {
      failMsg += "b2: Wrong variable length on getVARLEN\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, b2.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getVARLEN() == 0))
    {
      failMsg += "b4: Wrong variable length on getVARLEN\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following DateFieldDescription.
   *<ul>
   *<li>Verify valid uses of the DateFieldDescription constructor.
   *<ul>
   *<li>DateFieldDescription(AS400Text, String)
   *<li>DateFieldDescription(AS400Text, String, String)
   *<li>DateFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, String, String)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var003()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //String dftVal = "10/11/65";
    //String datFmt = "*ISO";
    //String datSep = "/";
    //String[] keyFunctions = {"UNIQUE"};

    DateFieldDescription b1 = new DateFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), fieldName1);
    DateFieldDescription b2 = new DateFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    DateFieldDescription b4 = new DateFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    else if (!b1.getDATFMT().equals(""))
    {
      failMsg += "b1: Wrong date format returned from getDATFMT\n";
    }
    else if (!b1.getDATSEP().equals(""))
    {
      failMsg += "b1: Wrong date separator returned from getDATSEP\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    else if (!b2.getDATFMT().equals(""))
    {
      failMsg += "b2: Wrong date format returned from getDATFMT\n";
    }
    else if (!b2.getDATSEP().equals(""))
    {
      failMsg += "b2: Wrong date separator returned from getDATSEP\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 8);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    else if (!b4.getDATFMT().equals(""))
    {
      failMsg += "b4: Wrong date format returned from getDATFMT\n";
    }
    else if (!b4.getDATSEP().equals(""))
    {
      failMsg += "b4: Wrong date separator returned from getDATSEP\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following DBCSEitherFieldDescription.
   *<ul>
   *<li>Verify valid uses of the DBCSEitherFieldDescription constructor.
   *<ul>
   *<li>DBCSEitherFieldDescription(AS400Text, String)
   *<li>DBCSEitherFieldDescription(AS400Text, String, String)
   *<li>DBCSEitherFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var004()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //String dftVal = "John Doe";
    //String[] keyFunctions = {"UNIQUE"};

    DBCSEitherFieldDescription b1 = new DBCSEitherFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName1);
    DBCSEitherFieldDescription b2 = new DBCSEitherFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    DBCSEitherFieldDescription b4 = new DBCSEitherFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getVARLEN() == 0))
    {
      failMsg += "b1: Wrong variable length on getVARLEN\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getVARLEN() == 0))
    {
      failMsg += "b2: Wrong variable length on getVARLEN\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, b2.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getVARLEN() == 0))
    {
      failMsg += "b4: Wrong variable length on getVARLEN\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following DBCSGraphicFieldDescription.
   *<ul>
   *<li>Verify valid uses of the DBCSGraphicFieldDescription constructor.
   *<ul>
   *<li>DBCSGraphicFieldDescription(AS400Text, String)
   *<li>DBCSGraphicFieldDescription(AS400Text, String, String)
   *<li>DBCSGraphicFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var005()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //String dftVal = "John Doe";
    //String[] keyFunctions = {"UNIQUE"};

    DBCSGraphicFieldDescription b1 = new DBCSGraphicFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName1);
    DBCSGraphicFieldDescription b2 = new DBCSGraphicFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    DBCSGraphicFieldDescription b4 = new DBCSGraphicFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getVARLEN() == 0))
    {
      failMsg += "b1: Wrong variable length on getVARLEN\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getVARLEN() == 0))
    {
      failMsg += "b2: Wrong variable length on getVARLEN\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, b2.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getVARLEN() == 0))
    {
      failMsg += "b4: Wrong variable length on getVARLEN\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following DBCSOnlyFieldDescription.
   *<ul>
   *<li>Verify valid uses of the DBCSOnlyFieldDescription constructor.
   *<ul>
   *<li>DBCSOnlyFieldDescription(AS400Text, String)
   *<li>DBCSOnlyFieldDescription(AS400Text, String, String)
   *<li>DBCSOnlyFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var006()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    DBCSOnlyFieldDescription b1 = new DBCSOnlyFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName1);
    DBCSOnlyFieldDescription b2 = new DBCSOnlyFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    DBCSOnlyFieldDescription b4 = new DBCSOnlyFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getVARLEN() == 0))
    {
      failMsg += "b1: Wrong variable length on getVARLEN\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getVARLEN() == 0))
    {
      failMsg += "b2: Wrong variable length on getVARLEN\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, b2.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getVARLEN() == 0))
    {
      failMsg += "b4: Wrong variable length on getVARLEN\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following DBCSOpenFieldDescription.
   *<ul>
   *<li>Verify valid uses of the DBCSOpenFieldDescription constructor.
   *<ul>
   *<li>DBCSOpenFieldDescription(AS400Text, String)
   *<li>DBCSOpenFieldDescription(AS400Text, String, String)
   *<li>DBCSOpenFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var007()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //String dftVal = "John Doe";
    //String[] keyFunctions = {"UNIQUE"};

    DBCSOpenFieldDescription b1 = new DBCSOpenFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName1);
    DBCSOpenFieldDescription b2 = new DBCSOpenFieldDescription(new AS400Text(50, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    DBCSOpenFieldDescription b4 = new DBCSOpenFieldDescription(new AS400Text(25, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getVARLEN() == 0))
    {
      failMsg += "b1: Wrong variable length on getVARLEN\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getVARLEN() == 0))
    {
      failMsg += "b2: Wrong variable length on getVARLEN\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, b2.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getVARLEN() == 0))
    {
      failMsg += "b4: Wrong variable length on getVARLEN\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following FloatFieldDescription.
   *<ul>
   *<li>Verify valid uses of the FloatFieldDescription constructor.
   *<ul>
   *<li>FloatFieldDescription(AS400Float8, String)
   *<li>FloatFieldDescription(AS400Float8, String, String, int)
   *<li>FloatFieldDescription(AS400Float8, String, String, int, int)
   *<li>FloatFieldDescription(AS400Float8, String, String, int, String, boolean, String,
   *    String, Integer, String, String, String, String)
   *<li>FloatFieldDescription(AS400Float4, String)
   *<li>FloatFieldDescription(AS400Float4, String, String, int)
   *<li>FloatFieldDescription(AS400Float4, String, String, int, int)
   *<li>FloatFieldDescription(AS400Float4, String, String, int, String, boolean, String,
   *    String, Integer, String, String, String, String)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var008()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //Double dftVal1 = new Double(0.00);
    //Float dftVal2 = new Float(0.00);
    //String[] keyFunctions = {"UNIQUE"};

    FloatFieldDescription b1 = new FloatFieldDescription(new AS400Float8(), fieldName1);
    FloatFieldDescription b2 = new FloatFieldDescription(new AS400Float8(), fieldName1,
                                                           ddsName1, 16);
    FloatFieldDescription b4 = new FloatFieldDescription(new AS400Float8(), fieldName2);
    FloatFieldDescription b5 = new FloatFieldDescription(new AS400Float4(), fieldName1);
    FloatFieldDescription b6 = new FloatFieldDescription(new AS400Float4(), fieldName1,
                                                           ddsName1, 4);
    FloatFieldDescription b8 = new FloatFieldDescription(new AS400Float4(), fieldName2);
    FloatFieldDescription b9 = new FloatFieldDescription(new AS400Float8(), fieldName1,
                                                           ddsName1, 16, 5);
    FloatFieldDescription b10 = new FloatFieldDescription(new AS400Float4(), fieldName1,
                                                           ddsName1, 4, 2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Float8))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    else if (!b1.getFLTPCN().equals(""))
    {
      failMsg += "b1: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b1.getDecimalPositions() == 0))
    {
      failMsg += "b1: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Float8))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    else if (!b2.getFLTPCN().equals(""))
    {
      failMsg += "b2: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b2.getDecimalPositions() == 0))
    {
      failMsg += "b2: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 16);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Float8))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    else if (!b4.getFLTPCN().equals(""))
    {
      failMsg += "b4: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b4.getDecimalPositions() == 0))
    {
      failMsg += "b4: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b5 object
    if (!(b5.getDataType() instanceof AS400Float4))
    {
      failMsg += "b5: Wrong data type object returned on getDataType\n";
    }
    else if (!b5.getFieldName().equals(fieldName1))
    {
      failMsg += "b5: Wrong field name returned on getFieldName\n";
    }
    else if (!b5.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b5: Wrong field name returned on getDDSName\n";
    }
    else if (!(b5.getDFT() == null))
    {
      failMsg += "b5: Wrong default value returned from getDFT\n";
    }
    else if (!b5.getFLTPCN().equals(""))
    {
      failMsg += "b5: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b5.getDecimalPositions() == 0))
    {
      failMsg += "b5: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b5", b5, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b6 object
    if (!(b6.getDataType() instanceof AS400Float4))
    {
      failMsg += "b6: Wrong data type object returned on getDataType\n";
    }
    else if (!b6.getFieldName().equals(fieldName1))
    {
      failMsg += "b6: Wrong field name returned on getFieldName\n";
    }
    else if (!b6.getDDSName().equals(ddsName1))
    {
      failMsg += "b6: Wrong field name returned on getDDSName\n";
    }
    else if (!(b6.getDFT() == null))
    {
      failMsg += "b6: Wrong default value returned from getDFT\n";
    }
    else if (!b6.getFLTPCN().equals(""))
    {
      failMsg += "b6: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b6.getDecimalPositions() == 0))
    {
      failMsg += "b6: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b6", b6, 4);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b8 object
    if (!(b8.getDataType() instanceof AS400Float4))
    {
      failMsg += "b8: Wrong data type object returned on getDataType\n";
    }
    else if (!b8.getFieldName().equals(fieldName2))
    {
      failMsg += "b8: Wrong field name returned on getFieldName\n";
    }
    else if (!b8.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b8: Wrong field name returned on getDDSName\n";
    }
    else if (!(b8.getDFT() == null))
    {
      failMsg += "b8: Wrong default value returned from getDFT\n";
    }
    else if (!b8.getFLTPCN().equals(""))
    {
      failMsg += "b8: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b8.getDecimalPositions() == 0))
    {
      failMsg += "b8: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b8", b8, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b9 object
    if (!(b9.getDataType() instanceof AS400Float8))
    {
      failMsg += "b9: Wrong data type object returned on getDataType\n";
    }
    else if (!b9.getFieldName().equals(fieldName1))
    {
      failMsg += "b9: Wrong field name returned on getFieldName\n";
    }
    else if (!b9.getDDSName().equals(ddsName1))
    {
      failMsg += "b9: Wrong field name returned on getDDSName\n";
    }
    else if (!(b9.getDFT() == null))
    {
      failMsg += "b9: Wrong default value returned from getDFT\n";
    }
    else if (!b9.getFLTPCN().equals(""))
    {
      failMsg += "b9: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b9.getDecimalPositions() == 5))
    {
      failMsg += "b9: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b9", b9, 16);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b10 object
    if (!(b10.getDataType() instanceof AS400Float4))
    {
      failMsg += "b10: Wrong data type object returned on getDataType\n";
    }
    else if (!b10.getFieldName().equals(fieldName1))
    {
      failMsg += "b10: Wrong field name returned on getFieldName\n";
    }
    else if (!b10.getDDSName().equals(ddsName1))
    {
      failMsg += "b10: Wrong field name returned on getDDSName\n";
    }
    else if (!(b10.getDFT() == null))
    {
      failMsg += "b10: Wrong default value returned from getDFT\n";
    }
    else if (!b10.getFLTPCN().equals(""))
    {
      failMsg += "b10: Wrong value returned on getFLTPCN\n";
    }
    else if (!(b10.getDecimalPositions() == 2))
    {
      failMsg += "b10: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b10", b10, 4);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following HexFieldDescription.
   *<ul>
   *<li>Verify valid uses of the HexFieldDescription constructor.
   *<ul>
   *<li>HexFieldDescription(AS400ByteArray, String)
   *<li>HexFieldDescription(AS400ByteArray, String, String)
   *<li>HexFieldDescription(AS400ByteArray, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var009()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //byte[] dftVal = {0x00, 0x01, 0x0A};
    //String[] keyFunctions = {"UNIQUE"};

    HexFieldDescription b1 = new HexFieldDescription(new AS400ByteArray(25), fieldName1);
    HexFieldDescription b2 = new HexFieldDescription(new AS400ByteArray(50), fieldName1,
                                                           ddsName1);
    HexFieldDescription b4 = new HexFieldDescription(new AS400ByteArray(25), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400ByteArray))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getVARLEN() == 0))
    {
      failMsg += "b1: Wrong variable length on getVARLEN\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400ByteArray))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getVARLEN() == 0))
    {
      failMsg += "b2: Wrong variable length on getVARLEN\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, b2.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400ByteArray))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getVARLEN() == 0))
    {
      failMsg += "b4: Wrong variable length on getVARLEN\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following PackedDecimalFieldDescription.
   *<ul>
   *<li>Verify valid uses of the PackedDecimalFieldDescription constructor.
   *<ul>
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var010()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //BigDecimal dftVal1 = new BigDecimal("1");
    //String[] keyFunctions = {"UNIQUE"};

    PackedDecimalFieldDescription b1 = new PackedDecimalFieldDescription(new AS400PackedDecimal(31, 0), fieldName1);
    PackedDecimalFieldDescription b2 = new PackedDecimalFieldDescription(new AS400PackedDecimal(31, 0), fieldName1,
                                                           ddsName1);
    PackedDecimalFieldDescription b4 = new PackedDecimalFieldDescription(new AS400PackedDecimal(15, 2), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400PackedDecimal))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    else if (!(b1.getDecimalPositions() == 0))
    {
      failMsg += "b1: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, 31);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400PackedDecimal))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    else if (!(b2.getDecimalPositions() == 0))
    {
      failMsg += "b2: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 31);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400PackedDecimal))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    else if (!(b4.getDecimalPositions() == 2))
    {
      failMsg += "b4: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, 15);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following TimeFieldDescription.
   *<ul>
   *<li>Verify valid uses of the TimeFieldDescription constructor.
   *<ul>
   *<li>TimeFieldDescription(AS400Text, String)
   *<li>TimeFieldDescription(AS400Text, String, String)
   *<li>TimeFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String, String, String)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var011()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //String dftVal = "14:21:56";
    //String timFmt = "*JUL";
    //String timSep = ":";
    //String[] keyFunctions = {"UNIQUE"};

    TimeFieldDescription b1 = new TimeFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), fieldName1);
    TimeFieldDescription b2 = new TimeFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    TimeFieldDescription b4 = new TimeFieldDescription(new AS400Text(8, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    else if (!b1.getTIMFMT().equals(""))
    {
      failMsg += "b1: Wrong time format returned from getTIMFMT\n";
    }
    else if (!b1.getTIMSEP().equals(""))
    {
      failMsg += "b1: Wrong time separator returned from getTIMSEP\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    else if (!b2.getTIMFMT().equals(""))
    {
      failMsg += "b2: Wrong time format returned from getTIMFMT\n";
    }
    else if (!b2.getTIMSEP().equals(""))
    {
      failMsg += "b2: Wrong time separator returned from getTIMSEP\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 8);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    else if (!b4.getTIMFMT().equals(""))
    {
      failMsg += "b4: Wrong time format returned from getTIMFMT\n";
    }
    else if (!b4.getTIMSEP().equals(""))
    {
      failMsg += "b4: Wrong time separator returned from getTIMSEP\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following TimestampFieldDescription.
   *<ul>
   *<li>Verify valid uses of the TimestampFieldDescription constructor.
   *<ul>
   *<li>TimestampFieldDescription(AS400Text, String)
   *<li>TimestampFieldDescription(AS400Text, String, String)
   *<li>TimestampFieldDescription(AS400Text, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var012()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //String dftVal = "10.11.65.14:21:56";
    //String[] keyFunctions = {"UNIQUE"};

    TimestampFieldDescription b1 = new TimestampFieldDescription(new AS400Text(17, systemObject_.getCcsid(), systemObject_), fieldName1);
    TimestampFieldDescription b2 = new TimestampFieldDescription(new AS400Text(17, systemObject_.getCcsid(), systemObject_), fieldName1,
                                                           ddsName1);
    TimestampFieldDescription b4 = new TimestampFieldDescription(new AS400Text(17, systemObject_.getCcsid(), systemObject_), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Text))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, b1.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }


    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Text))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 17);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Text))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, b4.getDataType().getByteLength());
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the following ZonedDecimalFieldDescription.
   *<ul>
   *<li>Verify valid uses of the ZonedDecimalFieldDescription constructor.
   *<ul>
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String, String, String, boolean, String,
   *    String, Integer, String, String, String, String)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var013()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //BigDecimal dftVal1 = new BigDecimal("1");
    //String[] keyFunctions = {"UNIQUE"};

    ZonedDecimalFieldDescription b1 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(31, 0), fieldName1);
    ZonedDecimalFieldDescription b2 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(31, 0), fieldName1,
                                                           ddsName1);
    ZonedDecimalFieldDescription b4 = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(15, 2), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400ZonedDecimal))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    else if (!(b1.getDecimalPositions() == 0))
    {
      failMsg += "b1: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, 31);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400ZonedDecimal))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    else if (!(b2.getDecimalPositions() == 0))
    {
      failMsg += "b2: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 31);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400ZonedDecimal))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    else if (!(b4.getDecimalPositions() == 2))
    {
      failMsg += "b4: Wrong value returned on getDecimalPositions\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, 15);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the isVariableLength() method.<br>
   *<ul compact>
   *<li>Verify for CharacterFieldDescription
   *<li>Verify for DBCSEitherFiedDescription
   *<li>Verify for DBCSGraphicFiedDescription
   *<li>Verify for DBCSOnlyFiedDescription
   *<li>Verify for DBCSOpenFiedDescription
   *<li>Verify for HexFiedDescription
   *</ul>
   *<b>Expected results:</b>
   *<ul compact>
   *<li>false will be returned when setVariableLength() has not been set
   *to true and the VARLEN keyword was not specified on the constructor
   *<li>true wil be returned when the VARLEN keyword has been specified on
   *the constructor
   *<li>true will be returned when VARLEN has not been specified on the
   *constructor but the setVariableLength() method has been called with true.
   *<li>true will be returned when both VARLEN has been specified and
   *setVariableLength() has been called with true.
   *</ul>
  **/
  public void Var014()
  {
    String failMsg = "";
    CharacterFieldDescription f1 = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSEitherFieldDescription f2 = new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSGraphicFieldDescription f3 = new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSOnlyFieldDescription f4 = new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSOpenFieldDescription f5 = new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    HexFieldDescription f6 = new HexFieldDescription(new AS400ByteArray(10), "name");

    // Verify false returned when VARLEN not specified and no set has been done.
    if (f1.isVariableLength() != false)
    {
      failMsg += "false not returned for character field description\n";
    }
    if (f2.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS either field description\n";
    }
    if (f3.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS graphic field description\n";
    }
    if (f4.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS only field description\n";
    }
    if (f5.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS open field description\n";
    }
    if (f6.isVariableLength() != false)
    {
      failMsg += "false not returned for hex field description\n";
    }

    // Verify that true is retured when VARLEN has been specified and no set done
    f1.setVARLEN(50);
    f2.setVARLEN(50);
    f3.setVARLEN(50);
    f4.setVARLEN(50);
    f5.setVARLEN(50);
    f6.setVARLEN(50);
    if (f1.isVariableLength() != true)
    {
      failMsg += "true not returned for character field description\n";
    }
    if (f2.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS either field description\n";
    }
    if (f3.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS graphic field description\n";
    }
    if (f4.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS only field description\n";
    }
    if (f5.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS open field description\n";
    }
    if (f6.isVariableLength() != true)
    {
      failMsg += "true not returned for hex field description\n";
    }

    // Verify that true is retured when VARLEN has not been specified and
    // set is done
    f1 = new CharacterFieldDescription(new AS400Text(100, systemObject_.getCcsid(), systemObject_), "fieldName1",
                                                           "ddsName1");
    f1.setVariableLength(true);
    f2 = new DBCSEitherFieldDescription(new AS400Text(100, systemObject_.getCcsid(), systemObject_), "fieldName1",
                                                           "ddsName1");
    f2.setVariableLength(true);
    f3 = new DBCSGraphicFieldDescription(new AS400Text(100, systemObject_.getCcsid(), systemObject_), "fieldName1",
                                                           "ddsName1");
    f3.setVariableLength(true);
    f4 = new DBCSOnlyFieldDescription(new AS400Text(100, systemObject_.getCcsid(), systemObject_), "fieldName1",
                                                           "ddsName1");
    f4.setVariableLength(true);
    f5 = new DBCSOpenFieldDescription(new AS400Text(100, systemObject_.getCcsid(), systemObject_), "fieldName1",
                                                           "ddsName1");
    f5.setVariableLength(true);
    f6 = new HexFieldDescription(new AS400ByteArray(100), "fieldName1",
                                                           "ddsName1");
    f6.setVariableLength(true);
    if (f1.isVariableLength() != true)
    {
      failMsg += "true not returned for character field description\n";
    }
    if (f2.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS either field description\n";
    }
    if (f3.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS graphic field description\n";
    }
    if (f4.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS only field description\n";
    }
    if (f5.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS open field description\n";
    }
    if (f6.isVariableLength() != true)
    {
      failMsg += "true not returned for hex field description\n";
    }

    // Verify that true is retured when VARLEN has been specified and
    // set is done
    f1.setVARLEN(50);
    f2.setVARLEN(50);
    f3.setVARLEN(50);
    f4.setVARLEN(50);
    f5.setVARLEN(50);
    f6.setVARLEN(50);
    f1.setVariableLength(true);
    f2.setVariableLength(true);
    f3.setVariableLength(true);
    f4.setVariableLength(true);
    f5.setVariableLength(true);
    f6.setVariableLength(true);
    if (f1.isVariableLength() != true)
    {
      failMsg += "true not returned for character field description\n";
    }
    if (f2.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS either field description\n";
    }
    if (f3.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS graphic field description\n";
    }
    if (f4.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS only field description\n";
    }
    if (f5.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS open field description\n";
    }
    if (f6.isVariableLength() != true)
    {
      failMsg += "true not returned for hex field description\n";
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify the setVariableLength() method.<br>
   *<ul compact>
   *<li>Verify for CharacterFieldDescription
   *<li>Verify for DBCSEitherFiedDescription
   *<li>Verify for DBCSGraphicFiedDescription
   *<li>Verify for DBCSOnlyFiedDescription
   *<li>Verify for DBCSOpenFiedDescription
   *<li>Verify for HexFiedDescription
   *</ul>
   *<b>Expected results:</b>
   *<ul compact>
   *<li>When true is specifed on setVariableLength, isVariableLength()
   *returns true.
   *<li>When flase is specifed on setVariableLength, isVariableLength()
   *returns false.
   *</ul>
  **/
  public void Var015()
  {
    String failMsg = "";
    CharacterFieldDescription f1 = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSEitherFieldDescription f2 = new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSGraphicFieldDescription f3 = new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSOnlyFieldDescription f4 = new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    DBCSOpenFieldDescription f5 = new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name");
    HexFieldDescription f6 = new HexFieldDescription(new AS400ByteArray(10), "name");

    // Verify setting to true
    f1.setVariableLength(true);
    f2.setVariableLength(true);
    f3.setVariableLength(true);
    f4.setVariableLength(true);
    f5.setVariableLength(true);
    f6.setVariableLength(true);
    if (f1.isVariableLength() != true)
    {
      failMsg += "true not returned for character field description\n";
    }
    if (f2.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS either field description\n";
    }
    if (f3.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS graphic field description\n";
    }
    if (f4.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS only field description\n";
    }
    if (f5.isVariableLength() != true)
    {
      failMsg += "true not returned for DBCS open field description\n";
    }
    if (f6.isVariableLength() != true)
    {
      failMsg += "true not returned for hex field description\n";
    }

    // Verify setting to false
    f1.setVariableLength(false);
    f2.setVariableLength(false);
    f3.setVariableLength(false);
    f4.setVariableLength(false);
    f5.setVariableLength(false);
    f6.setVariableLength(false);
    if (f1.isVariableLength() != false)
    {
      failMsg += "false not returned for character field description\n";
    }
    if (f2.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS either field description\n";
    }
    if (f3.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS graphic field description\n";
    }
    if (f4.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS only field description\n";
    }
    if (f5.isVariableLength() != false)
    {
      failMsg += "false not returned for DBCS open field description\n";
    }
    if (f6.isVariableLength() != false)
    {
      failMsg += "false not returned for hex field description\n";
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  /**
   *Verify ArrayFieldDescription constructors and getters.
   *<ul compact>
   *<li>Null constructor.
   *<li>ArrayFieldDescription(AS400Array, String)
   *<li>Getters return null of false when not set.
   *<li>Geters return values set on constructor when values have been
   *set on the constructor.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Object will be constructed correctly.
   *<li>Getters will return the correct values.
   *</ul>
  **/
  public void Var016()
  {
    ArrayFieldDescription f = new ArrayFieldDescription();
    ArrayFieldDescription f2 = new ArrayFieldDescription(new AS400Array(new AS400Bin4(), 5), "arrayFieldDescription");
    if (f.getDataType() != null)
    {
      failed("getDataType not returning null");
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
    if (!(f2.getDataType() instanceof AS400Array))
    {
      failed("data type not an instance of AS400Array");
      return;
    }
    if (!f2.getFieldName().equals("arrayFieldDescription"))
    {
      failed("field name");
      return;
    }
    if (!f2.getDDSName().equals("arrayField".toUpperCase()))
    {
      failed("dds name");
      return;
    }
    if (f2.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    chk = checkKeywordsNone("", f2, 20);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    succeeded();
  }

  /**
   *Verify the null constructors.
   *<ul compact>
   *<li>BinaryFieldDescription()
   *<li>CharacterFieldDescription()
   *<li>DBCSEitherFieldDescription()
   *<li>DBCSGraphicFieldDescription()
   *<li>DBCSOnlyFieldDescription()
   *<li>DBCSOpenFieldDescription()
   *<li>DateFieldDescription()
   *<li>FloatFieldDescription()
   *<li>HexFieldDescription()
   *<li>PackedDecimalFieldDescription()
   *<li>TimeFieldDescription()
   *<li>TimestampFieldDescription()
   *<li>ZonedDecimalFieldDescription()
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>String getters will return empty strings
   *<li>Object getters will return null.
   *<li>getLength() will return 0
  **/
  public void Var017()
  {
    BinaryFieldDescription f1 = new BinaryFieldDescription();
    CharacterFieldDescription f2 = new CharacterFieldDescription();
    DBCSEitherFieldDescription f3 = new DBCSEitherFieldDescription();
    DBCSGraphicFieldDescription f4 = new DBCSGraphicFieldDescription();
    DBCSOnlyFieldDescription f5 = new DBCSOnlyFieldDescription();
    DBCSOpenFieldDescription f6 = new DBCSOpenFieldDescription();
    DateFieldDescription f7 = new DateFieldDescription();
    FloatFieldDescription f8 = new FloatFieldDescription();
    HexFieldDescription f9 = new HexFieldDescription();
    PackedDecimalFieldDescription f10 = new PackedDecimalFieldDescription();
    TimeFieldDescription f11 = new TimeFieldDescription();
    TimestampFieldDescription f12 = new TimestampFieldDescription();
    ZonedDecimalFieldDescription f13 = new ZonedDecimalFieldDescription();

    String chk = "";
    if (f1.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f1.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f1.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f1.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    chk = checkKeywordsNone("", f1, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f2.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f2.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f2.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f2.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f2.getCCSID().equals(""))
    {
      failed("getCCSID not returning null");
      return;
    }
    if (f2.getVARLEN() != 0)
    {
      failed("getVARLEN not returning false");
      return;
    }
    chk = checkKeywordsNone("", f2, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f3.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f3.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f3.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f3.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f3.getCCSID().equals(""))
    {
      failed("getCCSID not returning null");
      return;
    }
    if (f3.getVARLEN() != 0)
    {
      failed("getVARLEN not returning false");
      return;
    }
    chk = checkKeywordsNone("", f3, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f4.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f4.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f4.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f4.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f4.getCCSID().equals(""))
    {
      failed("getCCSID not returning null");
      return;
    }
    if (f4.getVARLEN() != 0)
    {
      failed("getVARLEN not returning false");
      return;
    }
    chk = checkKeywordsNone("", f4, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f5.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f5.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f5.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f5.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f5.getCCSID().equals(""))
    {
      failed("getCCSID not returning null");
      return;
    }
    if (f5.getVARLEN() != 0)
    {
      failed("getVARLEN not returning false");
      return;
    }
    chk = checkKeywordsNone("", f5, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f6.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f6.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f6.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f6.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f6.getCCSID().equals(""))
    {
      failed("getCCSID not returning null");
      return;
    }
    if (f6.getVARLEN() != 0)
    {
      failed("getVARLEN not returning false");
      return;
    }
    chk = checkKeywordsNone("", f6, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f7.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f7.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f7.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f7.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f7.getDATFMT().equals(""))
    {
      failed("Empty string not returned for DATFMT");
      return;
    }
    if (!f7.getDATSEP().equals(""))
    {
      failed("Empty string not returned for DATSEP");
      return;
    }
    chk = checkKeywordsNone("", f7, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f8.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f8.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f8.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f8.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    chk = checkKeywordsNone("", f8, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f9.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f9.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f9.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f9.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (f9.getVARLEN() != 0)
    {
      failed("getVARLEN not returning false");
      return;
    }
    chk = checkKeywordsNone("", f9, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f10.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f10.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f10.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f10.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    chk = checkKeywordsNone("", f10, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f11.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f11.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f11.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f11.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    if (!f11.getTIMFMT().equals(""))
    {
      failed("Empty string not returned for TIMFMT");
      return;
    }
    if (!f11.getTIMSEP().equals(""))
    {
      failed("Empty string not returned for TIMSEP");
      return;
    }
    chk = checkKeywordsNone("", f11, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f12.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f12.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f12.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f12.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    chk = checkKeywordsNone("", f12, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    if (f13.getDataType() != null)
    {
      failed("getDataType not returning null");
      return;
    }
    if (!f13.getFieldName().equals(""))
    {
      failed("Empty string not returned for field name");
      return;
    }
    if (!f13.getDDSName().equals(""))
    {
      failed("Empty string not returned for dds name");
      return;
    }
    if (f13.getDFT() != null)
    {
      failed("getDFT not returning null");
      return;
    }
    chk = checkKeywordsNone("", f13, 0);
    if (!chk.equals(""))
    {
      failed(chk);
      return;
    }
    succeeded();
  }

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

  /**
   *Verify the following for BinaryFieldDescription.
   *<ul>
   *<li>Verify valid uses of the BinaryFieldDescription constructor.
   *<ul>
   *<li>BinaryFieldDescription(AS400Bin8, String)
   *<li>BinaryFieldDescription(AS400Bin8, String, String, int)
   *<li>BinaryFieldDescription(AS400UnsignedBin2, String)
   *<li>BinaryFieldDescription(AS400UnsignedBin2, String, String, int)
   *<li>BinaryFieldDescription(AS400UnsignedBin4, String)
   *<li>BinaryFieldDescription(AS400UnsignedBin4, String, String, int)
   *</ul>
   *<li>Verify getters return null when no value has been set.
   *<li>Verify getters return value set on the constructor when a value has been set.
   *</ul>
  **/
  public void Var018()
  {
    String failMsg = "";
    String checkMsg;
    String fieldName1 = "javaField";
    String fieldName2 = "myNewJavaField";
    String ddsName1 = "EMPNUM";
    String ddsName2 = "myNewJavaF";
    //Integer dftVal = new Integer(0);
    //String[] keyFunctions = {"UNIQUE"};

    BinaryFieldDescription b1 = new BinaryFieldDescription(new AS400Bin8(), fieldName1);
    BinaryFieldDescription b2 = new BinaryFieldDescription(new AS400Bin8(), fieldName1,
                                                           ddsName1, 10);
    BinaryFieldDescription b4 = new BinaryFieldDescription(new AS400Bin8(), fieldName2);
    BinaryFieldDescription b5 = new BinaryFieldDescription(new AS400UnsignedBin2(), fieldName1);
    BinaryFieldDescription b6 = new BinaryFieldDescription(new AS400UnsignedBin2(), fieldName1,
                                                           ddsName1, 3);
    BinaryFieldDescription b8 = new BinaryFieldDescription(new AS400UnsignedBin2(), fieldName2);
    BinaryFieldDescription b9 = new BinaryFieldDescription(new AS400UnsignedBin4(), fieldName1);
    BinaryFieldDescription b10 = new BinaryFieldDescription(new AS400UnsignedBin4(), fieldName1,
                                                           ddsName1, 5);
    BinaryFieldDescription b12 = new BinaryFieldDescription(new AS400UnsignedBin4(), fieldName2);

    // Verify b1 object
    if (!(b1.getDataType() instanceof AS400Bin8))
    {
      failMsg += "b1: Wrong data type object returned on getDataType\n";
    }
    else if (!b1.getFieldName().equals(fieldName1))
    {
      failMsg += "b1: Wrong field name returned on getFieldName\n";
    }
    else if (!b1.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b1: Wrong field name returned on getDDSName\n";
    }
    else if (!(b1.getDFT() == null))
    {
      failMsg += "b1: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b1", b1, 18);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b2 object
    if (!(b2.getDataType() instanceof AS400Bin8))
    {
      failMsg += "b2: Wrong data type object returned on getDataType\n";
    }
    else if (!b2.getFieldName().equals(fieldName1))
    {
      failMsg += "b2: Wrong field name returned on getFieldName\n";
    }
    else if (!b2.getDDSName().equals(ddsName1))
    {
      failMsg += "b2: Wrong field name returned on getDDSName\n";
    }
    else if (!(b2.getDFT() == null))
    {
      failMsg += "b2: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b2", b2, 10);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b4 object
    if (!(b4.getDataType() instanceof AS400Bin8))
    {
      failMsg += "b4: Wrong data type object returned on getDataType\n";
    }
    else if (!b4.getFieldName().equals(fieldName2))
    {
      failMsg += "b4: Wrong field name returned on getFieldName\n";
    }
    else if (!b4.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b4: Wrong field name returned on getDDSName\n";
    }
    else if (!(b4.getDFT() == null))
    {
      failMsg += "b4: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b4", b4, 18);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b5 object
    if (!(b5.getDataType() instanceof AS400UnsignedBin2))
    {
      failMsg += "b5: Wrong data type object returned on getDataType\n";
    }
    else if (!b5.getFieldName().equals(fieldName1))
    {
      failMsg += "b5: Wrong field name returned on getFieldName\n";
    }
    else if (!b5.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b5: Wrong field name returned on getDDSName\n";
    }
    else if (!(b5.getDFT() == null))
    {
      failMsg += "b5: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b5", b5, 4);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b6 object
    if (!(b6.getDataType() instanceof AS400UnsignedBin2))
    {
      failMsg += "b6: Wrong data type object returned on getDataType\n";
    }
    else if (!b6.getFieldName().equals(fieldName1))
    {
      failMsg += "b6: Wrong field name returned on getFieldName\n";
    }
    else if (!b6.getDDSName().equals(ddsName1))
    {
      failMsg += "b6: Wrong field name returned on getDDSName\n";
    }
    else if (!(b6.getDFT() == null))
    {
      failMsg += "b6: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b6", b6, 3);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b8 object
    if (!(b8.getDataType() instanceof AS400UnsignedBin2))
    {
      failMsg += "b8: Wrong data type object returned on getDataType\n";
    }
    else if (!b8.getFieldName().equals(fieldName2))
    {
      failMsg += "b8: Wrong field name returned on getFieldName\n";
    }
    else if (!b8.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b8: Wrong field name returned on getDDSName\n";
    }
    else if (!(b8.getDFT() == null))
    {
      failMsg += "b8: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b8", b8, 4);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b9 object
    if (!(b9.getDataType() instanceof AS400UnsignedBin4))
    {
      failMsg += "b9: Wrong data type object returned on getDataType\n";
    }
    else if (!b9.getFieldName().equals(fieldName1))
    {
      failMsg += "b9: Wrong field name returned on getFieldName\n";
    }
    else if (!b9.getDDSName().equals(fieldName1.toUpperCase()))
    {
      failMsg += "b9: Wrong field name returned on getDDSName\n";
    }
    else if (!(b9.getDFT() == null))
    {
      failMsg += "b9: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b9", b9, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b10 object
    if (!(b10.getDataType() instanceof AS400UnsignedBin4))
    {
      failMsg += "b10: Wrong data type object returned on getDataType\n";
    }
    else if (!b10.getFieldName().equals(fieldName1))
    {
      failMsg += "b10: Wrong field name returned on getFieldName\n";
    }
    else if (!b10.getDDSName().equals(ddsName1))
    {
      failMsg += "b10: Wrong field name returned on getDDSName\n";
    }
    else if (!(b10.getDFT() == null))
    {
      failMsg += "b10: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b10", b10, 5);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    // Verify b12 object
    if (!(b12.getDataType() instanceof AS400UnsignedBin4))
    {
      failMsg += "b12: Wrong data type object returned on getDataType\n";
    }
    else if (!b12.getFieldName().equals(fieldName2))
    {
      failMsg += "b12: Wrong field name returned on getFieldName\n";
    }
    else if (!b12.getDDSName().equals(ddsName2.toUpperCase()))
    {
      failMsg += "b12: Wrong field name returned on getDDSName\n";
    }
    else if (!(b12.getDFT() == null))
    {
      failMsg += "b12: Wrong default value returned from getDFT\n";
    }
    checkMsg = checkKeywordsNone("b12", b12, 9);
    if (checkMsg != "")
    {
      failMsg += checkMsg;
    }

    if (failMsg == "")
    {
      succeeded();
    }
    else
    {
      failed(failMsg);
    }
  }

  
  
  String checkKeywordsSpecified(String name, FieldDescription f, int len)
  {
    if (!(f.getALIAS().equals("anAlias")))
    {
      return name + ": Alias keyword in error\n";
    }
    else if (!(f.getALWNULL() == false))
    {
      return name + ": Allow null keyword in error\n";
    }
    else if (!(f.getCOLHDG().equals("'Employee xxx'")))
    {
      return name + ": COLHDG keyword in error\n";
    }
    else if (!(f.getLength() == len))
    {
      return name + ": Length in error\n";
    }
    else if (!(f.getREFFLD().equals("reffld")))
    {
      return name + ": REFFLD keyword in error\n";
    }
    else if (!(f.getTEXT().equals("Employee xxxx")))
    {
      return name + ": TEXT keyword in error\n";
    }
    String[] kFunctions = f.getKeyFieldFunctions();
    if (kFunctions.length != 1 || !kFunctions[0].equals("UNIQUE"))
    {
      return name + ": Key field keywords in error\n";
    }

    return "";
  }
}



