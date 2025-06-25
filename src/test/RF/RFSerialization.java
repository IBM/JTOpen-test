///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFSerialization.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.RF;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.PackedDecimalFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;

import test.PasswordVault;
import test.Testcase;
import test.DDM.DDMChar10NoKeyFormat;
import test.DDM.DDMPropertyChangeListener;
import test.DDM.DDMVetoableChangeListener;

/**
 *Testcase RFSerialization.  This test class verifies the abillity to
 *serialize and deserialize RecordFormat and Record objects.
**/
public class RFSerialization extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RFSerialization";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RFTest.main(newArgs); 
   }
  /**
   *Constructor.  This is called from RFTest::createTestcases().
  **/
  public RFSerialization(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String           password)
  {
    super(systemObject, "RFSerialization", 6,
          variationsToRun, runMode, fileOutputStream);
    // Add cache entry to prevent deserialization routine from opening
    // a prompt window when trying to get the CCSID. 
    encryptedPassword_ = PasswordVault.getEncryptedPassword(password);
    
    try { 
      char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      AS400.addPasswordCacheEntry(systemObject.getSystemName(), systemObject.getUserId(), passwordChars);
      Arrays.fill(passwordChars, ' '); 
    } catch (Exception e) {
      e.printStackTrace(); 
    }
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
  }

  /**
   *Verify the ability to serialize and deserialize a RecordFormat object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The RecordFormat object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The name can be set for the object.
   *<li>The object can be used.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      RecordFormat rf = new RecordFormat();
      // Serialize
      FileOutputStream ros = new FileOutputStream("rfser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(rf);
      rout.flush();
      ros.close();
      // Deserialize
      FileInputStream ris = new FileInputStream("rfser.ser");
      ObjectInputStream rin = new ObjectInputStream(ris);
      RecordFormat deserrf = (RecordFormat)rin.readObject();
      rin.close(); 
      // Verify state
      if (deserrf.getFieldDescriptions().length != 0)
      {
        failed("getFieldDescriptions");
        return;
      }
      if (deserrf.getKeyFieldDescriptions().length != 0)
      {
        failed("getKeyFieldDescriptions");
        return;
      }
      if (deserrf.getFieldNames().length != 0)
      {
        failed("getFieldNames");
        return;
      }
      if (deserrf.getKeyFieldNames().length != 0)
      {
        failed("getKeyFieldNames");
        return;
      }
      if (!deserrf.getName().equals(""))
      {
        failed("getName");
        return;
      }
      if (deserrf.getNumberOfFields() != 0)
      {
        failed("getNumberOfFields");
        return;
      }
      if (deserrf.getNumberOfKeyFields() != 0)
      {
        failed("getNumberOfKeyFields");
        return;
      }
      // Verify usability
      // Add some listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      // For property change and vetoable change listeners use
      // the ones created for the DDM testcases.
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      deserrf.addRecordDescriptionListener(rl1);
      deserrf.addPropertyChangeListener(rl2);
      deserrf.addVetoableChangeListener(rl3);
      deserrf.setName("FORMAT");
      if (!deserrf.getName().equals("FORMAT"))
      {
        failed("setName() not setting correctly");
        return;
      }
      if (!rl2.propertyChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!rl3.vetoableChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD1"));
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD2"));
      deserrf.addKeyFieldDescription("FIELD1");
      if (!rl1.fieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!rl1.keyFieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      Record r = deserrf.getNewRecord();
      if (r.getRecordLength() != 20)
      {
        failed("getNewRecord");
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
   *Verify the ability to serialize and deserialize a RecordFormat object.
   *<ul compact>
   *<li>When the object was constructed with the null constructor and
   *the state has been set:
   *<ul compact>
   *<li>Name set
   *<li>Listeners added
   *<li>Field and key field descriptions added
   *<li>Length and offset dependencies set
   *</ul>
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The RecordFormat object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The object can be used.
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      RecordFormat rf = new RecordFormat();
      // Set some of the state
      rf.setName("FORMAT1");

      CharacterFieldDescription f1 = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD1");
      BinaryFieldDescription f2 = new BinaryFieldDescription(new AS400Bin4(), "FIELD2");
      BinaryFieldDescription f3 = new BinaryFieldDescription(new AS400Bin4(), "FIELD3");
      PackedDecimalFieldDescription f4 = new PackedDecimalFieldDescription(new AS400PackedDecimal(15,5), "FIELD4");
      CharacterFieldDescription f5 = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD5");
      rf.addFieldDescription(f1);
      rf.addFieldDescription(f2);
      rf.addFieldDescription(f3);
      rf.addFieldDescription(f4);
      rf.addFieldDescription(f5);

      rf.addKeyFieldDescription("FIELD1");
      rf.addKeyFieldDescription("FIELD4");

      rf.setLengthDependency(4,1);
      rf.setOffsetDependency(4,2);

      RFRecordDescriptionListener bsl1 = new RFRecordDescriptionListener();
      DDMPropertyChangeListener bsl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener bsl3 = new DDMVetoableChangeListener();
      rf.addRecordDescriptionListener(bsl1);
      rf.addPropertyChangeListener(bsl2);
      rf.addVetoableChangeListener(bsl3);

      // Serialize
      FileOutputStream ros = new FileOutputStream("rfser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(rf);
      rout.flush();
      ros.close();
      // Deserialize
      FileInputStream ris = new FileInputStream("rfser.ser");
      ObjectInputStream rin = new ObjectInputStream(ris);
      RecordFormat deserrf = (RecordFormat)rin.readObject();
      rin.close(); 
     // Verify state
      if (deserrf.getFieldDescriptions().length != 5)
      {
        failed("getFieldDescriptions");
        return;
      }
      if (deserrf.getKeyFieldDescriptions().length != 2)
      {
        failed("getKeyFieldDescriptions");
        return;
      }
      String[] names = deserrf.getFieldNames();
      if (names.length != 5)
      {
        failed("getFieldNames");
        return;
      }
      else
      {
        for (int i = 0; i < 5; ++i)
        {
          if (!names[i].equals("FIELD" + String.valueOf(i+1)))
          {
            failed("Wrong field names returned.");
            return;
          }
        }
      }
      names = deserrf.getKeyFieldNames();
      if (names.length != 2)
      {
        failed("getKeyFieldNames");
        return;
      }
      else
      {
        if (!names[0].equals("FIELD1"))
        {
          failed("Wrong key field names returned");
          return;
        }
        if (!names[1].equals("FIELD4"))
        {
          failed("Wrong key field names returned");
          return;
        }
      }
      if (!deserrf.getName().equals("FORMAT1"))
      {
        failed("getName");
        return;
      }
      if (deserrf.getNumberOfFields() != 5)
      {
        failed("getNumberOfFields");
        return;
      }
      if (deserrf.getNumberOfKeyFields() != 2)
      {
        failed("getNumberOfKeyFields");
        return;
      }
      if (deserrf.getLengthDependency(4) != 1)
      {
        failed("getLengthDependency");
        return;
      }
      if (deserrf.getOffsetDependency(4) != 2)
      {
        failed("getOffsetDependency");
        return;
      }
      // Verify usability
      // Add some listeners
      RFRecordDescriptionListener adl1 = new RFRecordDescriptionListener();
      // For property change and vetoable change listeners use
      // the ones created for the DDM testcases.
      DDMPropertyChangeListener adl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener adl3 = new DDMVetoableChangeListener();
      deserrf.addRecordDescriptionListener(adl1);
      deserrf.addPropertyChangeListener(adl2);
      deserrf.addVetoableChangeListener(adl3);
      deserrf.setName("FORMAT");
      if (!deserrf.getName().equals("FORMAT"))
      {
        failed("setName() not setting correctly");
        return;
      }
      if (bsl2.propertyChangeFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (bsl3.vetoableChangeFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (!adl2.propertyChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!adl3.vetoableChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD6"));
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD7"));
      deserrf.addKeyFieldDescription("FIELD6");
      if (bsl1.fieldDescriptionAddedFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (bsl1.keyFieldDescriptionAddedFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (!adl1.fieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!adl1.keyFieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      Record r = deserrf.getNewRecord();
      if (r.getRecordLength() != rf.getNewRecord().getRecordLength() + 20)
      {
        failed("getNewRecord");
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
   *Verify the ability to serialize and deserialize a RecordFormat object.
   *<ul compact>
   *<li>When the object was constructed with the RecordFormat(String) constructor and
   *no other methods have been called.
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The RecordFormat object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The name can be set for the object.
   *<li>The object can be used.
   *</ul>
  **/
  public void Var003()
  {
    try
    {
      RecordFormat rf = new RecordFormat("FORMATB");
      // Serialize
      FileOutputStream ros = new FileOutputStream("rfser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(rf);
      rout.flush();
      ros.close();
      // Deserialize
      FileInputStream ris = new FileInputStream("rfser.ser");
      ObjectInputStream rin = new ObjectInputStream(ris);
      RecordFormat deserrf = (RecordFormat)rin.readObject();
      rin.close(); 
      // Verify state
      if (deserrf.getFieldDescriptions().length != 0)
      {
        failed("getFieldDescriptions");
        return;
      }
      if (deserrf.getKeyFieldDescriptions().length != 0)
      {
        failed("getKeyFieldDescriptions");
        return;
      }
      if (deserrf.getFieldNames().length != 0)
      {
        failed("getFieldNames");
        return;
      }
      if (deserrf.getKeyFieldNames().length != 0)
      {
        failed("getKeyFieldNames");
        return;
      }
      if (!deserrf.getName().equals("FORMATB"))
      {
        failed("getName");
        return;
      }
      if (deserrf.getNumberOfFields() != 0)
      {
        failed("getNumberOfFields");
        return;
      }
      if (deserrf.getNumberOfKeyFields() != 0)
      {
        failed("getNumberOfKeyFields");
        return;
      }
      // Verify usability
      // Add some listeners
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      // For property change and vetoable change listeners use
      // the ones created for the DDM testcases.
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      deserrf.addRecordDescriptionListener(rl1);
      deserrf.addPropertyChangeListener(rl2);
      deserrf.addVetoableChangeListener(rl3);
      deserrf.setName("FORMAT");
      if (!deserrf.getName().equals("FORMAT"))
      {
        failed("setName() not setting correctly");
        return;
      }
      if (!rl2.propertyChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!rl3.vetoableChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD1"));
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD2"));
      deserrf.addKeyFieldDescription("FIELD1");
      if (!rl1.fieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!rl1.keyFieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      Record r = deserrf.getNewRecord();
      if (r.getRecordLength() != 20)
      {
        failed("getNewRecord");
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
   *Verify the ability to serialize and deserialize a RecordFormat object.
   *<ul compact>
   *<li>When the object was constructed with the RecordFormat(String) constructor and
   *the state has been set:
   *<ul compact>
   *<li>Name set
   *<li>Listeners added
   *<li>Field and key field descriptions added
   *<li>Length and offset dependencies set
   *</ul>
   *</ul>
   *<br>
   *Expected results:
   *<ul compact>
   *<li>The RecordFormat object will be saved and will be able to
   *be de-serialized.
   *<li>The state of the object will be the same as the state
   *prior to serialization.
   *<li>The object can be used.
   *</ul>
  **/
  public void Var004()
  {
    try
    {
      RecordFormat rf = new RecordFormat("FORMAT1");
      // Set some of the state
      CharacterFieldDescription f1 = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD1");
      BinaryFieldDescription f2 = new BinaryFieldDescription(new AS400Bin4(), "FIELD2");
      BinaryFieldDescription f3 = new BinaryFieldDescription(new AS400Bin4(), "FIELD3");
      PackedDecimalFieldDescription f4 = new PackedDecimalFieldDescription(new AS400PackedDecimal(15,5), "FIELD4");
      CharacterFieldDescription f5 = new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD5");
      rf.addFieldDescription(f1);
      rf.addFieldDescription(f2);
      rf.addFieldDescription(f3);
      rf.addFieldDescription(f4);
      rf.addFieldDescription(f5);

      rf.addKeyFieldDescription("FIELD1");
      rf.addKeyFieldDescription("FIELD4");

      rf.setLengthDependency(4,1);
      rf.setOffsetDependency(4,2);

      RFRecordDescriptionListener bsl1 = new RFRecordDescriptionListener();
      DDMPropertyChangeListener bsl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener bsl3 = new DDMVetoableChangeListener();
      rf.addRecordDescriptionListener(bsl1);
      rf.addPropertyChangeListener(bsl2);
      rf.addVetoableChangeListener(bsl3);

      // Serialize
      FileOutputStream ros = new FileOutputStream("rfser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(rf);
      rout.flush();
      ros.close();
      // Deserialize
      FileInputStream ris = new FileInputStream("rfser.ser");
      ObjectInputStream rin = new ObjectInputStream(ris);
      RecordFormat deserrf = (RecordFormat)rin.readObject();
      rin.close(); 
     // Verify state
      if (deserrf.getFieldDescriptions().length != 5)
      {
        failed("getFieldDescriptions");
        return;
      }
      if (deserrf.getKeyFieldDescriptions().length != 2)
      {
        failed("getKeyFieldDescriptions");
        return;
      }
      String[] names = deserrf.getFieldNames();
      if (names.length != 5)
      {
        failed("getFieldNames");
        return;
      }
      else
      {
        for (int i = 0; i < 5; ++i)
        {
          if (!names[i].equals("FIELD" + String.valueOf(i+1)))
          {
            failed("Wrong field names returned.");
            return;
          }
        }
      }
      names = deserrf.getKeyFieldNames();
      if (names.length != 2)
      {
        failed("getKeyFieldNames");
        return;
      }
      else
      {
        if (!names[0].equals("FIELD1"))
        {
          failed("Wrong key field names returned");
          return;
        }
        if (!names[1].equals("FIELD4"))
        {
          failed("Wrong key field names returned");
          return;
        }
      }
      if (!deserrf.getName().equals("FORMAT1"))
      {
        failed("getName");
        return;
      }
      if (deserrf.getNumberOfFields() != 5)
      {
        failed("getNumberOfFields");
        return;
      }
      if (deserrf.getNumberOfKeyFields() != 2)
      {
        failed("getNumberOfKeyFields");
        return;
      }
      if (deserrf.getLengthDependency(4) != 1)
      {
        failed("getLengthDependency");
        return;
      }
      if (deserrf.getOffsetDependency(4) != 2)
      {
        failed("getOffsetDependency");
        return;
      }
      // Verify usability
      // Add some listeners
      RFRecordDescriptionListener adl1 = new RFRecordDescriptionListener();
      // For property change and vetoable change listeners use
      // the ones created for the DDM testcases.
      DDMPropertyChangeListener adl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener adl3 = new DDMVetoableChangeListener();
      deserrf.addRecordDescriptionListener(adl1);
      deserrf.addPropertyChangeListener(adl2);
      deserrf.addVetoableChangeListener(adl3);
      deserrf.setName("FORMAT");
      if (!deserrf.getName().equals("FORMAT"))
      {
        failed("setName() not setting correctly");
        return;
      }
      if (bsl2.propertyChangeFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (bsl3.vetoableChangeFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (!adl2.propertyChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!adl3.vetoableChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD6"));
      deserrf.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, systemObject_.getCcsid(), systemObject_), "FIELD7"));
      deserrf.addKeyFieldDescription("FIELD6");
      if (bsl1.fieldDescriptionAddedFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (bsl1.keyFieldDescriptionAddedFired_)
      {
        failed("Old listeners being notified");
        return;
      }
      if (!adl1.fieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!adl1.keyFieldDescriptionAddedFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      Record r = deserrf.getNewRecord();
      if (r.getRecordLength() != rf.getNewRecord().getRecordLength() + 20)
      {
        failed("getNewRecord");
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
   *Verify the ability to serialize and deserialize a Record object.
   *<ul compact>
   *<li>When the Record has been constructed with the null constructor and
   *no other methods have been called.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object should be the same as the state prior to
   *serialization.
   *<li>The deserialized object should be usable.
   *</ul>
  **/
  public void Var005()
  {
    try
    {
      Record r = new Record();
      // Serialize
      FileOutputStream ros = new FileOutputStream("rfser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(r);
      rout.flush();
      ros.close();
      // Deserialize
      FileInputStream ris = new FileInputStream("rfser.ser");
      ObjectInputStream rin = new ObjectInputStream(ris);
      Record deserr = (Record)rin.readObject();
      rin.close(); 
     // Verify state
      if (deserr.getFields().length != 0)
      {
        failed("getFields");
        return;
      }
      if (deserr.getKeyFields().length != 0)
      {
        failed("getKeyFields");
        return;
      }
      if (deserr.getNumberOfFields() != 0)
      {
        failed("getNumberFields");
        return;
      }
      if (deserr.getNumberOfKeyFields() != 0)
      {
        failed("getNumberOfKeyFields");
        return;
      }
      if (deserr.getRecordFormat() != null)
      {
        failed("getRecordFormat");
        return;
      }
      if (!deserr.getRecordName().equals(""))
      {
        failed("getRecordName");
        return;
      }
      if (deserr.getRecordNumber() != 0)
      {
        failed("getRecordNumber");
        return;
      }
      // Verify usability
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      deserr.addRecordDescriptionListener(rl1);
      deserr.addPropertyChangeListener(rl2);
      deserr.addVetoableChangeListener(rl3);
      deserr.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      if (!rl2.propertyChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!rl3.vetoableChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      deserr.setField(0, "BLAH");
      if (!((String)deserr.getField(0)).equals("BLAH"))
      {
        failed("setField");
        return;
      }
      if (!rl1.fieldModifiedFired_)
      {
        failed("Listeners not being notified");
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
   *Verify the ability to serialize and deserialize a Record object.
   *<ul compact>
   *<li>When the Record has been constructed and its state has been set.
   *<ul compact>
   *<li>Record format is set.
   *<li>Field values have been set.
   *<li>There are contents to get.
   *<li>Record name has been set.
   *<li>Listeners have been added
   *<li>Record number set
   *<li>Some fields are null
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The state of the object should be the same as the state prior to
   *serialization.
   *<li>The deserialized object should be usable.
   *</ul>
  **/
  public void Var006()
  {
    // Do any necessary setup work for the variations
    try
    {
      setup();
    }
    catch (Exception e)
    {
      // Testcase setup did not complete successfully
      failed("Unable to complete setup");
      return;
    }
    try
    {
      // Create a Record that has format, name and contents set.
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/DDMTESTSAV.LIB/ALLFLDSKEY.FILE");
      RecordFormat rf = rd.retrieveRecordFormat()[0];
      Record testRec = rf.getNewRecord();
      testRec.setField(3, "RECORD0001");
      byte[] testContents = testRec.getContents();
      Record r = new Record(rf, testContents, "myFirstRecord");
      // Set some more state
      r.setRecordNumber(22);
      r.setField(1, Integer.valueOf(22));
      r.setField("PACKEDDEC", null);
      Record matchRec = rf.getNewRecord(testContents);
      matchRec.setField(3, "RECORD0001");
      matchRec.setField(1, Integer.valueOf(22));
      matchRec.setField("PACKEDDEC", null);
      byte[] matchContents = r.getContents();
      // Add some listeners
      RFRecordDescriptionListener bsl1 = new RFRecordDescriptionListener();
      DDMPropertyChangeListener bsl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener bsl3 = new DDMVetoableChangeListener();
      r.addRecordDescriptionListener(bsl1);
      r.addPropertyChangeListener(bsl2);
      r.addVetoableChangeListener(bsl3);
      // Serialize
      FileOutputStream ros = new FileOutputStream("rfser.ser");
      ObjectOutputStream rout = new ObjectOutputStream(ros);
      rout.writeObject(r);
      rout.flush();
      ros.close();
      // Deserialize
      FileInputStream ris = new FileInputStream("rfser.ser");
      ObjectInputStream rin = new ObjectInputStream(ris);
      Record deserr = (Record)rin.readObject();
      rin.close(); 
      // Verify state
      if (deserr.getFields().length != testRec.getFields().length)
      {
        failed("getFields");
        return;
      }
      if (deserr.getKeyFields().length != testRec.getKeyFields().length)
      {
        failed("getKeyFields");
        return;
      }
      if (deserr.getNumberOfFields() != testRec.getNumberOfFields())
      {
        failed("getNumberFields");
        return;
      }
      if (deserr.getNumberOfKeyFields() != testRec.getNumberOfKeyFields())
      {
        failed("getNumberOfKeyFields");
        return;
      }
      if (!deserr.getRecordFormat().getName().equals(rf.getName()))
      {
        failed("getRecordFormat");
        return;
      }
      if (!deserr.getRecordName().equals("myFirstRecord"))
      {
        failed("getRecordName");
        return;
      }
      if (deserr.getRecordNumber() != 22)
      {
        failed("getRecordNumber");
        return;
      }
      if (!deserr.toString().equals(matchRec.toString()))
      {
        System.out.println("deserr: " + deserr.toString() + ".");
        System.out.println("match : " + matchRec.toString() + ".");
        failed("toString()");
        return;
      }
      byte[] contents = deserr.getContents();
      for (int i = 0; i < matchContents.length; ++i)
      {
        if (matchContents[i] != contents[i])
        {
          failed("byte contents don't match");
          return;
        }
      }
      // Verify usability
      RFRecordDescriptionListener rl1 = new RFRecordDescriptionListener();
      DDMPropertyChangeListener rl2 = new DDMPropertyChangeListener();
      DDMVetoableChangeListener rl3 = new DDMVetoableChangeListener();
      deserr.addRecordDescriptionListener(rl1);
      deserr.addPropertyChangeListener(rl2);
      deserr.addVetoableChangeListener(rl3);
      deserr.setRecordFormat(new DDMChar10NoKeyFormat(systemObject_));
      if (!rl2.propertyChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (!rl3.vetoableChangeFired_)
      {
        failed("Listeners not being notified");
        return;
      }
      if (bsl2.propertyChangeFired_)
      {
        failed("Old Listeners being notified");
        return;
      }
      if (bsl3.vetoableChangeFired_)
      {
        failed("Old Listeners being notified");
        return;
      }
      deserr.setField(0, "BLAH");
      if (!((String)deserr.getField(0)).equals("BLAH"))
      {
        failed("setField");
        return;
      }
      if (bsl1.fieldModifiedFired_)
      {
        failed("Old Listeners being notified");
        return;
      }
      if (!rl1.fieldModifiedFired_)
      {
        failed("Listeners not being notified");
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
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    try
    {
      // Verify the existence of library DDMTESTSAV on the system
      CommandCall c = new CommandCall(systemObject_, "CHKOBJ OBJ(DDMTESTSAV) OBJTYPE(*LIB)");
      boolean ran = c.run();
      AS400Message[] msgs = c.getMessageList();
      if (msgs.length != 0)
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        System.out.println("Either library DDMTESTSAV does not exist or you");
        System.out.println("do not have authority to it.");
        System.out.println("ftp DDMTESTSAV.SAVF in binary from");
        System.out.println("the test/ git directory");
        System.out.println("to the AS/400 system to which you are running.");
        System.out.println("Use RSTLIB to restore library DDMTESTSAV to the system.");
        System.out.println("ran = "+ran); 
        throw new Exception("No DDMTESTSAV available");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }
}



