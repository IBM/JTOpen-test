///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FDInvUsage.java
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
import com.ibm.as400.access.*; //@B0A

/**
  Testcase FDInvUsage.  This class verifies invalid usage of the
  constructors of the FieldDescription subclasses. Note: There is no
  invalid usage of the getters for the FieldDescription subclasses.
**/
public class FDInvUsage extends Testcase
{
  /**
  Constructor.  This is called from the FDTest constructor.
  **/
  public FDInvUsage(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "FDInvUsage", 15,
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
  }

  /**
   *Verify NullPointerException error for BinaryFieldDescription
   *<ol>
   *<li>BinaryFieldDescription((AS400Bin4)null, String)
   *<li>BinaryFieldDescription(AS400Bin4, null)
   *<li>BinaryFieldDescription((AS400Bin4)null, String, String, int)
   *<li>BinaryFieldDescription(AS400Bin4, null, String, int)
   *<li>BinaryFieldDescription(AS400Bin4, String, null, int)
   *<li>BinaryFieldDescription((AS400Bin4)null, String, String, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400Bin4, null, String, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400Bin4, String, null, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription((AS400Bin2)null, String)
   *<li>BinaryFieldDescription(AS400Bin2, null)
   *<li>BinaryFieldDescription((AS400Bin2)null, String, String, int)
   *<li>BinaryFieldDescription(AS400Bin2, null, String, int)
   *<li>BinaryFieldDescription(AS400Bin2, String, null, int)
   *<li>BinaryFieldDescription((AS400Bin2)null, String, String, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400Bin2, null, String, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400Bin2, String, null, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *</ol>
   *<b>Expected results:</b><br>
   *<ul compact>
   *<li>NullPointerException with getMssage() returning the appropriate parameter
   *name.
   *</ul>
  **/
  public void Var001()
  {
    String failMsg = "";

    // AS400Bin4: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400Bin4)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin4: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin4: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400Bin4)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin4: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin4: Verify NullPointerException when null passed for ddsName
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin2: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400Bin2)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "9: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin2: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin2(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "10: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin2: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400Bin2)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "11: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin2: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin2(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "12: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin2: Verify NullPointerException when null passed for ddsName
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin2(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "13: Wrong parameter returned on NullPointerException\n";
      }
    }

    //@B0A

    // AS400Bin8: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400Bin8)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin8: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin8(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin8: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400Bin8)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin8: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin8(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Bin8: Verify NullPointerException when null passed for ddsName
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin8(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin2: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400UnsignedBin2)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin2: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin2(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin2: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400UnsignedBin2)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin2: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin2(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin2: Verify NullPointerException when null passed for ddsName
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin2(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin4: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400UnsignedBin4)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin4: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin4(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin4: Verify NullPointerException when null passed for data type
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription((AS400UnsignedBin4)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin4: Verify NullPointerException when null passed for name
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin4(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400UnsignedBin4: Verify NullPointerException when null passed for ddsName
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin4(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for CharacterFieldDescription.
   *<ol>
   *<li>CharacterFieldDescription((AS400Text)null, String)
   *<li>CharacterFieldDescription(AS400Text, null)
   *<li>CharacterFieldDescription((AS400Text)null, String, String)
   *<li>CharacterFieldDescription(AS400Text, null, String)
   *<li>CharacterFieldDescription(AS400Text, String, null)
   *<li>CharacterFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>CharacterFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>CharacterFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *</ol>
   *<b>Expected results:</b><br>
   *<ul compact>
   *<li>NullPointerException with getMssage() returning the appropriate parameter
   *name.
   *</ul>
  **/
  public void Var002()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      CharacterFieldDescription b = new CharacterFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      CharacterFieldDescription b = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      CharacterFieldDescription b = new CharacterFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      CharacterFieldDescription b = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      CharacterFieldDescription b = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for DateFieldDescription.
   *<ol>
   *<li>DateFieldDescription((AS400Text)null, String)
   *<li>DateFieldDescription(AS400Text, null)
   *<li>DateFieldDescription((AS400Text)null, String, String)
   *<li>DateFieldDescription(AS400Text, null, String)
   *<li>DateFieldDescription(AS400Text, String, null)
   *<li>DateFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *<li>DateFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *<li>DateFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *</ol>
  **/
  public void Var003()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      DateFieldDescription b = new DateFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DateFieldDescription b = new DateFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      DateFieldDescription b = new DateFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DateFieldDescription b = new DateFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      DateFieldDescription b = new DateFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for DBCSEitherFieldDescription.
   *<ol>
   *<li>DBCSEitherFieldDescription((AS400Text)null, String)
   *<li>DBCSEitherFieldDescription(AS400Text, null)
   *<li>DBCSEitherFieldDescription((AS400Text)null, String, String)
   *<li>DBCSEitherFieldDescription(AS400Text, null, String)
   *<li>DBCSEitherFieldDescription(AS400Text, String, null)
   *<li>DBCSEitherFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSEitherFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSEitherFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *</ol>
  **/
  public void Var004()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSEitherFieldDescription b = new DBCSEitherFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSEitherFieldDescription b = new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSEitherFieldDescription b = new DBCSEitherFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSEitherFieldDescription b = new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      DBCSEitherFieldDescription b = new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for DBCSGraphicFieldDescription.
   *<ol>
   *<li>DBCSGraphicFieldDescription((AS400Text)null, String)
   *<li>DBCSGraphicFieldDescription(AS400Text, null)
   *<li>DBCSGraphicFieldDescription((AS400Text)null, String, String)
   *<li>DBCSGraphicFieldDescription(AS400Text, null, String)
   *<li>DBCSGraphicFieldDescription(AS400Text, String, null)
   *<li>DBCSGraphicFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSGraphicFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSGraphicFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *</ol>
  **/
  public void Var005()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSGraphicFieldDescription b = new DBCSGraphicFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSGraphicFieldDescription b = new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSGraphicFieldDescription b = new DBCSGraphicFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSGraphicFieldDescription b = new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      DBCSGraphicFieldDescription b = new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for DBCSOnlyFieldDescription.
   *<ol>
   *<li>DBCSOnlyFieldDescription((AS400Text)null, String)
   *<li>DBCSOnlyFieldDescription(AS400Text, null)
   *<li>DBCSOnlyFieldDescription((AS400Text)null, String, String)
   *<li>DBCSOnlyFieldDescription(AS400Text, null, String)
   *<li>DBCSOnlyFieldDescription(AS400Text, String, null)
   *<li>DBCSOnlyFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSOnlyFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSOnlyFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *</ol>
  **/
  public void Var006()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSOnlyFieldDescription b = new DBCSOnlyFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSOnlyFieldDescription b = new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSOnlyFieldDescription b = new DBCSOnlyFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSOnlyFieldDescription b = new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      DBCSOnlyFieldDescription b = new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for DBCSOpenFieldDescription.
   *<ol>
   *<li>DBCSOpenFieldDescription((AS400Text)null, String)
   *<li>DBCSOpenFieldDescription(AS400Text, null)
   *<li>DBCSOpenFieldDescription((AS400Text)null, String, String)
   *<li>DBCSOpenFieldDescription(AS400Text, null, String)
   *<li>DBCSOpenFieldDescription(AS400Text, String, null)
   *<li>DBCSOpenFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSOpenFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSOpenFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *</ol>
  **/
  public void Var007()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSOpenFieldDescription b = new DBCSOpenFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSOpenFieldDescription b = new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      DBCSOpenFieldDescription b = new DBCSOpenFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      DBCSOpenFieldDescription b = new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      DBCSOpenFieldDescription b = new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for FloatFieldDescription
   *<ol>
   *<li>FloatFieldDescription((AS400Float8)null, String)
   *<li>FloatFieldDescription(AS400Float8, null)
   *<li>FloatFieldDescription((AS400Float8)null, String, String, int)
   *<li>FloatFieldDescription(AS400Float8, null, String, int)
   *<li>FloatFieldDescription(AS400Float8, String, null, int)
   *<li>FloatFieldDescription((AS400Float8)null, String, String, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription(AS400Float8, null, String, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription(AS400Float8, String, null, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription((AS400Float4)null, String)
   *<li>FloatFieldDescription(AS400Float4, null)
   *<li>FloatFieldDescription((AS400Float4)null, String, String, int)
   *<li>FloatFieldDescription(AS400Float4, null, String, int)
   *<li>FloatFieldDescription(AS400Float4, String, null, int)
   *<li>FloatFieldDescription((AS400Float4)null, String, String, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription(AS400Float4, null, String, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription(AS400Float4, String, null, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription((AS400Float8)null, String, String, int, int)
   *<li>FloatFieldDescription(AS400Float8, null, String, int, int)
   *<li>FloatFieldDescription(AS400Float8, String, null, int, int)
   *<li>FloatFieldDescription((AS400Float4)null, String, String, int, int)
   *<li>FloatFieldDescription(AS400Float4, null, String, int, int)
   *<li>FloatFieldDescription(AS400Float4, String, null, int, int)
   *</ol>
  **/
  public void Var008()
  {
    String failMsg = "";

    // AS400Float8: Verify NullPointerException when null passed for data type
    try
    {
      FloatFieldDescription b = new FloatFieldDescription((AS400Float8)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for name
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for data type
    try
    {
      FloatFieldDescription b = new FloatFieldDescription((AS400Float8)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for name
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for ddsName
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for data type
    try
    {
      FloatFieldDescription b = new FloatFieldDescription((AS400Float4)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "9: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for name
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "10: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for data type
    try
    {
      FloatFieldDescription b = new FloatFieldDescription((AS400Float4)null, "name", "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "11: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for name
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), null, "ddsName", 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "12: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for ddsName
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), "name", null, 3);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "13: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for data type
    try
    {
      FloatFieldDescription b = new FloatFieldDescription((AS400Float8)null, "name", "ddsName", 3, 1);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "17: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for name
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), null, "ddsName", 3, 1);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "18: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float8: Verify NullPointerException when null passed for ddsName
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), "name", null, 3, 1);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "19: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for data type
    try
    {
      FloatFieldDescription b = new FloatFieldDescription((AS400Float4)null, "name", "ddsName", 3, 1);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "20: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for name
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), null, "ddsName", 3, 1);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "21: Wrong parameter returned on NullPointerException\n";
      }
    }

    // AS400Float4: Verify NullPointerException when null passed for ddsName
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), "name", null, 3, 1);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "22: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for HexFieldDescription.
   *<ol>
   *<li>HexFieldDescription((AS400ByteArray)null, String)
   *<li>HexFieldDescription(AS400ByteArray, null)
   *<li>HexFieldDescription((AS400ByteArray)null, String, String)
   *<li>HexFieldDescription(AS400ByteArray, null, String)
   *<li>HexFieldDescription(AS400ByteArray, String, null)
   *<li>HexFieldDescription((AS400ByteArray)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>HexFieldDescription(AS400ByteArray, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>HexFieldDescription(AS400ByteArray, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *</ol>
  **/
  public void Var009()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      HexFieldDescription b = new HexFieldDescription((AS400ByteArray)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      HexFieldDescription b = new HexFieldDescription(new AS400ByteArray(10), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      HexFieldDescription b = new HexFieldDescription((AS400ByteArray)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      HexFieldDescription b = new HexFieldDescription(new AS400ByteArray(10), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      HexFieldDescription b = new HexFieldDescription(new AS400ByteArray(10), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for PackedDecimalFieldDescription
   *<ol>
   *<li>PackedDecimalFieldDescription((AS400PackedDecimal)null, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, null)
   *<li>PackedDecimalFieldDescription((AS400PackedDecimal)null, String, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, null, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String, null)
   *<li>PackedDecimalFieldDescription((AS400PackedDecimal)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, null, String, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String, null, String, boolean, String,
   *String, Integer, String, String, String, String)
   *</ol>
  **/
  public void Var010()
  {
    String failMsg = "";

    //Verify NullPointerException when null passed for data type
    try
    {
      PackedDecimalFieldDescription b = new PackedDecimalFieldDescription((AS400PackedDecimal)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for name
    try
    {
      PackedDecimalFieldDescription b = new PackedDecimalFieldDescription(new AS400PackedDecimal(27, 3), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for data type
    try
    {
      PackedDecimalFieldDescription b = new PackedDecimalFieldDescription((AS400PackedDecimal)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for name
    try
    {
      PackedDecimalFieldDescription b = new PackedDecimalFieldDescription(new AS400PackedDecimal(27, 3), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for ddsName
    try
    {
      PackedDecimalFieldDescription b = new PackedDecimalFieldDescription(new AS400PackedDecimal(27, 3), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for TimeFieldDescription.
   *<ol>
   *<li>TimeFieldDescription((AS400Text)null, String)
   *<li>TimeFieldDescription(AS400Text, null)
   *<li>TimeFieldDescription((AS400Text)null, String, String)
   *<li>TimeFieldDescription(AS400Text, null, String)
   *<li>TimeFieldDescription(AS400Text, String, null)
   *<li>TimeFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *<li>TimeFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *<li>TimeFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *</ol>
  **/
  public void Var011()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      TimeFieldDescription b = new TimeFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      TimeFieldDescription b = new TimeFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      TimeFieldDescription b = new TimeFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      TimeFieldDescription b = new TimeFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      TimeFieldDescription b = new TimeFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for TimestampFieldDescription.
   *<ol>
   *<li>TimestampFieldDescription((AS400Text)null, String)
   *<li>TimestampFieldDescription(AS400Text, null)
   *<li>TimestampFieldDescription((AS400Text)null, String, String)
   *<li>TimestampFieldDescription(AS400Text, null, String)
   *<li>TimestampFieldDescription(AS400Text, String, null)
   *<li>TimestampFieldDescription((AS400Text)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>TimestampFieldDescription(AS400Text, null, String, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>TimestampFieldDescription(AS400Text, String, null, String, boolean, String,
   *String, Integer, String, String, String, String)
   *</ol>
  **/
  public void Var012()
  {
    String failMsg = "";

    // Verify NullPointerException when null passed for data type
    try
    {
      TimestampFieldDescription b = new TimestampFieldDescription((AS400Text)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      TimestampFieldDescription b = new TimestampFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for data type
    try
    {
      TimestampFieldDescription b = new TimestampFieldDescription((AS400Text)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for name
    try
    {
      TimestampFieldDescription b = new TimestampFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    // Verify NullPointerException when null passed for ddsName
    try
    {
      TimestampFieldDescription b = new TimestampFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify NullPointerException error for ZonedDecimalFieldDescription
   *<ol>
   *<li>ZonedDecimalFieldDescription((AS400ZonedDecimal)null, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, null)
   *<li>ZonedDecimalFieldDescription((AS400ZonedDecimal)null, String, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, null, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String, null)
   *<li>ZonedDecimalFieldDescription((AS400ZonedDecimal)null, String, String, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, null, String, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String, null, String, boolean, String,
   *String, Integer, String, String, String, String)
   *</ol>
  **/
  public void Var013()
  {
    String failMsg = "";

    //Verify NullPointerException when null passed for data type
    try
    {
      ZonedDecimalFieldDescription b = new ZonedDecimalFieldDescription((AS400ZonedDecimal)null, "name");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "1: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for name
    try
    {
      ZonedDecimalFieldDescription b = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(27, 3), null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "2: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for data type
    try
    {
      ZonedDecimalFieldDescription b = new ZonedDecimalFieldDescription((AS400ZonedDecimal)null, "name", "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "dataType"))
      {
        failMsg += "3: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for name
    try
    {
      ZonedDecimalFieldDescription b = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(27, 3), null, "ddsName");
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg += "4: Wrong parameter returned on NullPointerException\n";
      }
    }

    //Verify NullPointerException when null passed for ddsName
    try
    {
      ZonedDecimalFieldDescription b = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(27, 3), "name", null);
      failMsg += "Able to pass null on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "ddsName"))
      {
        failMsg += "5: Wrong parameter returned on NullPointerException\n";
      }
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
   *Verify IllegalArgumentException when ddsName is greater than 10 characters
   *for:
   *<ol>
   *<li>BinaryFieldDescription(AS400Bin4, String, ddsName, int)
   *<li>BinaryFieldDescription(AS400Bin4, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400Bin2, String, ddsName, int)
   *<li>BinaryFieldDescription(AS400Bin2, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400Bin8, String, ddsName, int)
   *<li>BinaryFieldDescription(AS400Bin8, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400UnsignedBin2, String, ddsName, int)
   *<li>BinaryFieldDescription(AS400UnsignedBin2, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>BinaryFieldDescription(AS400UnsignedBin4, String, ddsName, int)
   *<li>BinaryFieldDescription(AS400UnsignedBin4, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>CharacterFieldDescription(AS400Text, String, ddsName)
   *<li>CharacterFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DateFieldDescription(AS400Text, String, ddsName)
   *<li>DateFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *<li>DBCSEitherFieldDescription(AS400Text, String, ddsName)
   *<li>DBCSEitherFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSGraphicFieldDescription(AS400Text, String, ddsName)
   *<li>DBCSGraphicFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSOnlyFieldDescription(AS400Text, String, ddsName)
   *<li>DBCSOnlyFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>DBCSOpenFieldDescription(AS400Text, String, ddsName)
   *<li>DBCSOpenFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>FloatFieldDescription(AS400Float8, String, ddsName, int)
   *<li>FloatFieldDescription(AS400Float8, String, ddsName, int, int)
   *<li>FloatFieldDescription(AS400Float8, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>FloatFieldDescription(AS400Float4, String, ddsName, int)
   *<li>FloatFieldDescription(AS400Float4, String, ddsName, int, int)
   *<li>FloatFieldDescription(AS400Float4, String, ddsName, int, String, boolean, String,
   *String, Integer, String, String, String, String, String, int)
   *<li>HexFieldDescription(AS400Text, String, ddsName)
   *<li>HexFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, int)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String, ddsName)
   *<li>PackedDecimalFieldDescription(AS400PackedDecimal, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>TimeFieldDescription(AS400Text, String, ddsName)
   *<li>TimeFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String, String, String)
   *<li>TimestampFieldDescription(AS400Text, String, ddsName)
   *<li>TimestampFieldDescription(AS400Text, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String, ddsName)
   *<li>ZonedDecimalFieldDescription(AS400ZonedDecimal, String, ddsName, String, boolean, String,
   *String, Integer, String, String, String, String)
  **/
  public void Var014()
  {
    String failMsg = "";
    String ddsName = "My Very Long Name";

    //BinaryFieldDescription
    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin4(), "name" , ddsName, 9);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "1: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin2(), "name" , ddsName, 9);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "3: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    //@B0A

    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400Bin8(), "name" , ddsName, 9);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "1: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin2(), "name" , ddsName, 2);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "3: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    try
    {
      BinaryFieldDescription b = new BinaryFieldDescription(new AS400UnsignedBin4(), "name" , ddsName, 9);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "3: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // CharacterFieldDescription
    try
    {
      CharacterFieldDescription b = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "5: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // DateFieldDescription
    try
    {
      DateFieldDescription b = new DateFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "7: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // DBCSEitherFieldDescription
    try
    {
      DBCSEitherFieldDescription b = new DBCSEitherFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "9: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // DBCSGraphicFieldDescription
    try
    {
      DBCSGraphicFieldDescription b = new DBCSGraphicFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "11: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // DBCSOnlyFieldDescription
    try
    {
      DBCSOnlyFieldDescription b = new DBCSOnlyFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "13: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // DBCSOpenFieldDescription
    try
    {
      DBCSOpenFieldDescription b = new DBCSOpenFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "15: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    //FloatFieldDescription
    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), "name" , ddsName, 9);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "17: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float8(), "name" , ddsName, 9, 5);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "18: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), "name" , ddsName, 9);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "20: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    try
    {
      FloatFieldDescription b = new FloatFieldDescription(new AS400Float4(), "name" , ddsName, 9, 5);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "21: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // HexFieldDescription
    try
    {
      HexFieldDescription b = new HexFieldDescription(new AS400ByteArray(10), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "23: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // PackedDecimalFieldDescription
    try
    {
      PackedDecimalFieldDescription b = new PackedDecimalFieldDescription(new AS400PackedDecimal(27, 3), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "25: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // TimeFieldDescription
    try
    {
      TimeFieldDescription b = new TimeFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "27: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // TimestampFieldDescription
    try
    {
      TimestampFieldDescription b = new TimestampFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "29: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
    }

    // ZonedDecimalFieldDescription
    try
    {
      ZonedDecimalFieldDescription b = new ZonedDecimalFieldDescription(new AS400ZonedDecimal(27, 3), "name" , ddsName);
      failMsg += "Able to pass long ddsName on constructor\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "ddsName: Length is not valid."))
      {
        failMsg += "31: Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
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
   *Verify ExtendedIllegalArgumentException when text is greater than 50 characters
   *for FieldDescription.setTEXT().
  **/
  public void Var015()
  {
    String failMsg = "";
    String text = "A";
    for (int i = 1; i < 51; ++i)
    {
      text += "A";
    }

    CharacterFieldDescription c = new CharacterFieldDescription();
    try
    {
      c.setTEXT(text);
      failMsg += "No exception when setting text with greater than 50 characters.\n";
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "ExtendedIllegalArgumentException", "text: Length is not valid."))
      {
        failMsg += "Wrong parameter returned on ExtendedIllegalArgumentException\n";
      }
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
}



