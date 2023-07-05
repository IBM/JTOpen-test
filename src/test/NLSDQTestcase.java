///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSDQTestcase.java
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
import com.ibm.as400.access.*;
import com.ibm.as400.access.ObjectDescription; ///

/**
 *Testcase NLSDQTestcase.  This test class verifies the use of DBCS Strings
 *in selected DataQueue testcase variations.
**/
public class NLSDQTestcase extends Testcase
{
  long start;
  long time;
  boolean failed;  // Keeps track of failure in multi-part tests.
  String msg;      // Keeps track of reason for failure in multi-part tests.
  CommandCall cmd = null;


  String dq_desc10 = getResource("DQ_DESC10");
  String dq_desc50 = getResource("DQ_DESC50");
  String dq_key10  = getResource("DQ_KEY10");

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public NLSDQTestcase(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream
                      )
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSDQTestcase", 19,
          variationsToRun, runMode, fileOutputStream);
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.DATAQUEUE);
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
      output_.println("Unable to complete setup; variations not run");
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

    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      output_.println("Unable to complete cleanup.");
    }

    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.DATAQUEUE);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    try
    {
      // Delete NLSDQTST if it exists
      CommandCall c = new CommandCall(systemObject_);
      deleteLibrary(c, "NLSDQTEST");

      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191") || msgs[0].getID().equals("CPF2110")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
      // Create lib NLSDQTST
      c.run("CRTLIB LIB(NLSDQTEST) AUT(*ALL)");
      msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void cleanup()
    throws Exception
  {
    try
    {
      CommandCall c = new CommandCall(systemObject_);
      deleteLibrary(c, "NLSDQTEST");

      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPC2194") || msgs[0].getID().equals("CPC2191")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        throw new Exception("");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  private static String getTextDescription(ObjectDescription objDesc)
    throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
  {
    return (String)objDesc.getValue(ObjectDescription.TEXT_DESCRIPTION);
  }


/**
  DataQueue::getDescription().

  <i>Taken from:</i> DQAttributesTestcase::Var001
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    DataQueue dq = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/ATTRTEST.DTAQ");
    DataQueue dq2 = new DataQueue(systemObject_,
                                  "/QSYS.LIB/NLSDQTEST.LIB/ATTRTEST.DTAQ");
    try
    {
        dq2.create(80, "*USE", false, true, false, dq_desc10);
    }
    catch (Exception e)
    {
        e.printStackTrace(output_);
        failMsg.append("Unable to create dq with dbcs description.");
        failed(failMsg.toString());
        return;
    }

    try
    {
      dq.refreshAttributes();
      String getDesc = dq.getDescription().trim();
      ObjectDescription dqObj = new ObjectDescription(systemObject_,
                                                        "/QSYS.LIB/NLSDQTEST.LIB/ATTRTEST.DTAQ");
      String objDesc = getTextDescription(dqObj).trim();

      if (!getDesc.equals(dq_desc10) || !getDesc.equals(objDesc))
      {
        failMsg.append("Wrong object description: \n");
        failMsg.append("  Original         : " + dq_desc10 + ".\n");
        failMsg.append("  getDescription() : " + getDesc + ".\n");
        failMsg.append("  ObjectDescription: " + objDesc + ".\n");
      }
    }
    catch(Exception e)
    {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception.");
    }

    try
    {
      dq2.delete();
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
    }

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }

/**
  KeyedDataQueue::getDescription().
  <i>Taken from:</i> DQAttributesTestcase::Var002
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/ATTRTEST.DTAQ");
    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/ATTRTEST.DTAQ");
    try
    {
        dq2.create(10, 80, "*USE", false, false, dq_desc10);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to create data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
      dq.refreshAttributes();
      String getDesc = dq.getDescription().trim();
      ObjectDescription dqObj = new ObjectDescription(systemObject_,
                                                        "/QSYS.LIB/NLSDQTEST.LIB/ATTRTEST.DTAQ");
      String objDesc = getTextDescription(dqObj).trim();

      if (!getDesc.equals(dq_desc10) || !getDesc.equals(objDesc))
      {
        failMsg.append("Wrong object description: \n");
        failMsg.append("  Original         : " + dq_desc10 + ".\n");
        failMsg.append("  getDescription() : " + getDesc + ".\n");
        failMsg.append("  ObjectDescription: " + objDesc + ".\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.");
    }

    try
    {
      dq2.delete();
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
    }

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }

/**
  Verify description values with boundary lengths using
  DataQueue::create(int, String, boolean, boolean, boolean, String).

  <i>Taken from:</i> DQCreateTestcase::Var039
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    DataQueue dq = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
    DataQueue dq2 = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
    try
    {
      // true
      dq2.create(80, "*USE", false, false, false, "");
      if (!dq.getDescription().trim().equals(""))
      {
        failMsg.append("Wrong description (length 0).\n");
      }
      dq2.delete();
      // false
      dq2.create(80, "*USE", false, false, false, dq_desc50);

      dq = new DataQueue(systemObject_, "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
      dq.refreshAttributes();
      String getDesc = dq.getDescription().trim();
      ObjectDescription dqObj = new ObjectDescription(systemObject_,
                                                      "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
      String objDesc = getTextDescription(dqObj).trim();

      if (!getDesc.equals(dq_desc50) || !getDesc.equals(objDesc))
      {
        failMsg.append("Wrong object description: \n");
        failMsg.append("  Original         : " + dq_desc50 + "\n");
        failMsg.append("  getDescription() : " + getDesc + "\n");
        failMsg.append("  ObjectDescription: " + objDesc + "\n");
      }
    }
    catch(Exception e)
    {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify description values with boundary lengths using
  KeyedDataQueue::create(int, int, String, boolean, boolean, String).

  <i>Taken from:</i> DQCreateTestcase::Var040
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
    try
    {
      // true
      dq2.create(10, 80, "*USE", false, false, "");
      if (!dq.getDescription().trim().equals(""))
      {
        failMsg.append("Wrong description (length 0).");
      }
      dq2.delete();
      // false
      dq2.create(10, 80, "*USE", false, false, dq_desc50);
      dq = new KeyedDataQueue(systemObject_,
                              "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
      dq.refreshAttributes();
      String getDesc = dq.getDescription().trim();
      ObjectDescription dqObj = new ObjectDescription(systemObject_,
                                                      "/QSYS.LIB/NLSDQTEST.LIB/CRTTEST.DTAQ");
      String objDesc = getTextDescription(dqObj).trim();

      if (!getDesc.equals(dq_desc50) || !getDesc.equals(objDesc))
      {
        failMsg.append("Wrong object description: \n");
        failMsg.append("  Original         : " + dq_desc50 + "\n");
        failMsg.append("  getDescription() : " + getDesc + "\n");
        failMsg.append("  ObjectDescription: " + objDesc + "\n");
      }
    }
    catch(Exception e)
    {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Successful KeyedDataQueue::peek(byte[], int, "EQ").

  <i>Taken from:</i> DQKeyedPeekTestcase::Var013
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/KPEEKTST.DTAQ");
    String expected = new String(dq_desc50);
    byte[] key = null;
    try
    {
      key = (new String(dq_key10)).getBytes();
      dq.create(10, 80, "*USE", true, false, "");
      dq.write(key, expected.getBytes());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] peekKey = (new String(dq_key10)).getBytes();
        // do peek
        dq.refreshAttributes();
        KeyedDataQueueEntry data = dq.peek(peekKey, 0, "EQ");
        if (!expected.equals(new String(data.getData())))
        {
          failMsg.append("Incorrect peek data: " + new String(data.getData()));
        }
        if (!(new String(key)).equals((new String(data.getKey()))))
        {
          failMsg.append("Incorrect peek key: " + new String(data.getKey()));
        }

        // verify peek did not remove entry from queue
        data = dq.read(peekKey, 0, "EQ");
        if (!expected.equals(new String(data.getData())))
        {
          failMsg.append("Incorrect read data: " + new String(data.getData()));
        }
        if (!(new String(key)).equals((new String(data.getKey()))))
        {
          failMsg.append("Incorrect read key: " + new String(data.getKey()));
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify the byte data in the return object from a successful
  KeyedDataQueue::peek(byte[]).
  <i>Taken from:</i> DQKeyedPeekTestcase::Var036
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/KPEEKTST.DTAQ");
    String expected = new String(dq_desc50);
    byte[] expectedData = null;
    byte[] expectedKey = null;
    try
    {
      expectedData = expected.getBytes();
      expectedKey = (new String(dq_key10)).getBytes();
      dq.create(10, 80);
      dq.write(expectedKey, expected.getBytes());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        KeyedDataQueueEntry data = dq.peek(expectedKey);
        byte[] byteData = data.getData();
        if (byteData.length != expectedData.length)
        {
          failMsg.append("Incorrect data length.\n");
        }
        else
        {
          for (int i=0; i<byteData.length; i++)
          {
            if (byteData[i] != expectedData[i])
            {
                failMsg.append("Bad byte data.\n");
                i=byteData.length;
            }
          }
        }
        byte[] byteKey = data.getKey();
        if (byteKey.length != expectedKey.length)
        {
          failMsg.append("Incorrect key length.\n");
        }
        else
        {
          for (int i=0; i<byteKey.length; i++)
          {
            if (byteKey[i] != expectedKey[i])
            {
                failMsg.append("Bad byte key.\n");
                i=byteKey.length;
            }
          }
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Successful KeyedDataQueue::read(byte[], int, "EQ").

  <i>Taken from:</i> DQKeyedReadTestcase::Var013
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/KREADTST.DTAQ");
    String expected = new String(dq_desc50);
    byte[] key = null;
    try
    {
      key = (new String(dq_key10)).getBytes();
      dq.create(10, 80, "*USE", true, false, "");
      dq.write(key, expected.getBytes());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    byte[] readKey = null;
    try
    {
        readKey = (new String(dq_key10)).getBytes();
        // do read
        KeyedDataQueueEntry data = dq.read(readKey, 0, "EQ");
        if (!expected.equals(new String(data.getData())))
        {
          failMsg.append("Incorrect data: " + new String(data.getData()));
        }
        if (!(new String(key)).equals((new String(data.getKey()))))
        {
          failMsg.append("Incorrect key: " + new String(data.getKey()));
        }

        // verify entry removed from queue
        data = dq.read(readKey, 0, "EQ");
        if (data != null)
        {
            failMsg.append("Entry not removed from queue.\n");
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify the byte data in the return object from a successful
  KeyedDataQueue::read(byte[]).
  <i>Taken from:</i> DQKeyedReadTestcase::Var036
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/KREADTST.DTAQ");
    String expected = new String(dq_desc50);
    byte[] expectedData = null;
    byte[] expectedKey = null;
    try
    {
      expectedData = expected.getBytes();
      expectedKey = (new String(dq_key10)).getBytes();
      dq.create(10, 80);
      dq.write((new String(dq_key10)).getBytes(), expected.getBytes());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] key = (new String(dq_key10)).getBytes();
        KeyedDataQueueEntry data = dq.read(key);
        byte[] byteData = data.getData();
        if (byteData.length != expectedData.length)
        {
          failMsg.append("Incorrect data length.\n");
        }
        else
        {
          for (int i=0; i<byteData.length; i++)
          {
            if (byteData[i] != expectedData[i])
            {
                failMsg.append("Bad byte data.\n");
                i=byteData.length;
            }
          }
        }
        byte[] byteKey = data.getKey();
        if (byteKey.length != expectedKey.length)
        {
          failMsg.append("Incorrect key length.\n");
        }
        else
        {
          for (int i=0; i<byteKey.length; i++)
          {
            if (byteKey[i] != expectedKey[i])
            {
                failMsg.append("Bad byte key.\n");
                i=byteKey.length;
            }
          }
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  KeyedDataQueue::peek(byte[], int, String) on a queue that
  does have sender information.
  The return entry should contain the sender information.
  <i>Taken from:</i> DQPeekTestcase::Var010
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/KPEEKTST.DTAQ");
    String expected = new String(dq_desc50);
    byte[] key = null;
    try
    {
      key = (new String(dq_key10)).getBytes();
      dq.create(10, 80, "*USE", true, false, "");
      dq.write(key, expected.getBytes());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        // do peek
        KeyedDataQueueEntry data = dq.peek(key, 0, "EQ");
        if (!expected.equals(new String(data.getData())))
        {
          failMsg.append("Incorrect peek data: " + new String(data.getData()));
        }
        // JLW: removed sender info check, not correct when native.
        if (data.getSenderInformation().length() != 36 /*||
            !data.getSenderInformation().substring(0,20).equals("QZHQSSRV  QUSER     ")*/)
        {
          failMsg.append("Incorrect peek sender info: " + data.getSenderInformation());
        }

        // verify peek did not remove entry from queue
        data = dq.read(key, 0, "EQ");
        if (!expected.equals(new String(data.getData())))
        {
          failMsg.append("Incorrect read data: " + new String(data.getData()));
        }
        if (data.getSenderInformation().length() != 36 /*||
            !data.getSenderInformation().substring(0,20).equals("QZHQSSRV  QUSER     ")*/)
        {
          failMsg.append("Incorrect read sender info: " + data.getSenderInformation());
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  KeyedDataQueue::read(byte[], int, String) on a queue that
  does have sender information.
  The return entry should contain the sender information.
  <i>Taken from:</i> DQReadTestcase::Var010
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/KREADTST.DTAQ");
    String expected = new String(dq_desc50);
    byte[] key = null;
    try
    {
      key = (new String(dq_key10)).getBytes();
      dq.create(10, 80, "*USE", true, false, "");
      dq.write(key, expected.getBytes());
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        // do read
        KeyedDataQueueEntry data = dq.read(key, 0, "EQ");
        if (!expected.equals(new String(data.getData())))
        {
          failMsg.append("Incorrect data: " + new String(data.getData()));
        }
        if (data.getSenderInformation() == null ||
            data.getSenderInformation().length() != 36 /*||
           !data.getSenderInformation().substring(0,20).equals("QZHQSSRV  QUSER     ")*/)
        {
          failMsg.append("Incorrect sender info: " + data.getSenderInformation());
        }

        // verify entry removed from queue
        data = dq.read(key, 0, "EQ");
        if (data != null)
        {
            failMsg.append("Entry not removed from queue.\n");
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify a successful DataQueue::refreshAttributes() updates the object's attributes
  by refreshing the attributes of a queue whose attributes have changed.

  <i>Taken from:</i> DQRefreshAttributesTestcase::Var003
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    DataQueue dq = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/OPENTEST.DTAQ");
    DataQueue dq2 = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/OPENTEST.DTAQ");
    try
    {
      dq2.create(80, "*USE", true, false, false, dq_desc10);
      dq.getDescription();
      dq2.delete();
      dq2.create(65, "*EXCLUDE", false, true, true, dq_desc50);
      dq.refreshAttributes();

      if (dq.getMaxEntryLength() != 65)
      {
        failMsg.append("Wrong entry length.\n");
      }
      // Note that there is no way to easily verify the authority.
      if (dq.getSaveSenderInformation() != false)
      {
        failMsg.append("Wrong sender info value.\n");
      }
      if (dq.isFIFO() != true)
      {
        failMsg.append("Wrong FIFO value.\n");
      }
      if (dq.getForceToAuxiliaryStorage() != true)
      {
        failMsg.append("Wrong force value.\n");
      }
      String getDesc = dq.getDescription().trim();
      ObjectDescription dqObj = new ObjectDescription(systemObject_,
                                                      "/QSYS.LIB/NLSDQTEST.LIB/OPENTEST.DTAQ");
      String objDesc = getTextDescription(dqObj).trim();

      if (!getDesc.equals(dq_desc50) || !getDesc.equals(objDesc))
      {
        failMsg.append("Wrong object description: \n");
        failMsg.append("  Original         : " + dq_desc50 + "\n");
        failMsg.append("  getDescription() : " + getDesc + "\n");
        failMsg.append("  ObjectDescription: " + objDesc + "\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq2.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify a successful KeyedDataQueue::refreshAttributes() updates the object's attributes
  by refreshing the attributes of a queue whose attributes have changed.

  <i>Taken from:</i> DQRefreshAttributesTestcase::Var004
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/OPENTEST.DTAQ");
    KeyedDataQueue dq2 = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/OPENTEST.DTAQ");
    try
    {
      dq2.create(10, 80, "*USE", true, false, dq_desc10);
      dq.getDescription();
      dq2.delete();
      dq2.create(35, 65, "*EXCLUDE", false, true, dq_desc50);
      dq.refreshAttributes();
      if (dq.getKeyLength() != 35)
      {
        failMsg.append("Wrong key length.\n");
      }
      if (dq.getMaxEntryLength() != 65)
      {
        failMsg.append("Wrong entry length.\n");
      }
      // Note that there is no way to easily verify the authority.
      if (dq.getSaveSenderInformation() != false)
      {
        failMsg.append("Wrong sender info value.\n");
      }
      if (dq.isFIFO() != true)
      {
        failMsg.append("Wrong FIFO value.\n");
      }
      if (dq.getForceToAuxiliaryStorage() != true)
      {
        failMsg.append("Wrong force value.\n");
      }
      String getDesc = dq.getDescription().trim();
      ObjectDescription dqObj = new ObjectDescription(systemObject_, "/QSYS.LIB/NLSDQTEST.LIB/OPENTEST.DTAQ");
      String objDesc = getTextDescription(dqObj).trim();
      if (!getDesc.equals(dq_desc50) || !getDesc.equals(objDesc))
      {
        failMsg.append("Wrong object description: \n");
        failMsg.append("  Original         : " + dq_desc50 + "\n");
        failMsg.append("  getDescription() : " + getDesc + "\n");
        failMsg.append("  ObjectDescription: " + objDesc + "\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq2.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify a successful DataQueue::write(byte[]).
  <i>Taken from:</i> DQWriteTestcase::Var006
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    DataQueue dq = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/WRITETEST.DTAQ");
    try
    {
        dq.create(80);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] data = (new String(dq_desc50)).getBytes();
        dq.write(data);
        // Verify write worked by reading.
        DataQueueEntry entry = dq.read();
        if (entry == null)
          failMsg.append("Write failed.\n");
        else
        {
          if (!new String(entry.getData()).equals(dq_desc50))
            failMsg.append("Wrong data read.\n");
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify a successful KeyedDataQueue::write(byte[], byte[]).
  <i>Taken from:</i> DQWriteTestcase::Var007
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/WRITETEST.DTAQ");
    try
    {
        dq.create(10, 80);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] key = (new String(dq_key10)).getBytes();
        byte[] data = (new String(dq_desc50)).getBytes();
        dq.write(key, data);
        // Verify write worked by reading.
        KeyedDataQueueEntry entry = dq.read(key);
        if (entry == null)
          failMsg.append("Write failed.\n");
        else
        {
          if (!new String(entry.getData()).equals(dq_desc50))
            failMsg.append("Wrong data read.\n");
        }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  KeyedDataQueue::write(byte[], byte[]) passing a key argument
  which is not long enough.
  An AS400Exception should be thrown.
  <i>Taken from:</i> DQWriteTestcase::Var008
  **/
  public void Var015()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/WRITETEST.DTAQ");
    try
    {
        dq.create(11, 80);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] key = (new String(dq_key10)).getBytes();
        byte[] data = (new String(dq_desc50)).getBytes();
        dq.write(key, data);
        failMsg.append("Expected exception did not occur.\n");
    }
    catch(Exception e)
    {
        if (!exceptionStartsWith(e, "AS400Exception",
            "CPF9506", ErrorCompletingRequestException.AS400_ERROR))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception info.\n");
        }
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  KeyedDataQueue::write(byte[], byte[]) with a key argument that is too
  long.  An AS400Exception should be thrown.
  <i>Taken from:</i> DQWriteTestcase::Var009
  **/
  public void Var016()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/WRITETEST.DTAQ");
    try
    {
        dq.create(9, 80);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] key = (new String(dq_key10)).getBytes();
        byte[] data = (new String(dq_desc50)).getBytes();
        dq.write(key, data);
        failMsg.append("Expected exception did not occur.\n");
    }
    catch(Exception e)
    {
        if (!exceptionStartsWith(e, "AS400Exception",
            "CPF9506", ErrorCompletingRequestException.AS400_ERROR))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception info.\n");
        }
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  DataQueue::write(byte[]) on a queue with a entry which is too long.
  An AS400Exception should be thrown.
  <i>Taken from:</i> DQWriteTestcase::Var010
  **/
  public void Var017()
  {
    StringBuffer failMsg = new StringBuffer();
    DataQueue dq = new DataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/WRITETEST.DTAQ");
    try
    {
        dq.create(47);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] data = (new String(dq_desc50)).getBytes();
        dq.write(data);
        failMsg.append("Expected exception did not occur.\n");
    }
    catch(Exception e)
    {
        if (!exceptionStartsWith(e, "AS400Exception",
            "CPF2498", ErrorCompletingRequestException.AS400_ERROR))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception info.\n");
        }
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  KeyedDataQueue::write(byte[], byte[]) on a queue with a entry which is too long.
  An AS400Exception should be thrown.
  <i>Taken from:</i> DQWriteTestcase::Var011
  **/
  public void Var018()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/WRITETEST.DTAQ");
    try
    {
        dq.create(10, 47);
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        byte[] key = (new String(dq_key10)).getBytes();
        byte[] data = (new String(dq_desc50)).getBytes();
        dq.write(key, data);
        failMsg.append("Expected exception did not occur.\n");
    }
    catch(Exception e)
    {
        if (!exceptionStartsWith(e, "AS400Exception",
            "CPF2498", ErrorCompletingRequestException.AS400_ERROR))
        {
          e.printStackTrace(output_);
          failMsg.append("Wrong exception info.\n");
        }
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
  Verify a successful KeyedDataQueue::clear(byte[]).
  <i>Taken from:</i> DQClearTestcase::Var008
  **/
  public void Var019()
  {
    StringBuffer failMsg = new StringBuffer();
    KeyedDataQueue dq = new KeyedDataQueue(systemObject_,
                                 "/QSYS.LIB/NLSDQTEST.LIB/CLEARTEST.DTAQ");
    byte[] key1 = null;
    byte[] key2 = null;
    try
    {
        key1 = new String("Char10Keyy").getBytes();
        key2 = new String(dq_key10).getBytes();
        dq.create(10, 80);
        dq.write(key1, new String(dq_desc50).getBytes());
        dq.write(key2, new String(dq_desc10).getBytes());
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to setup the data queue.\n");
      failed(failMsg.toString());
      return;
    }

    try
    {
        dq.clear(key1);
        // Verify the clear worked.
        if (dq.read(key1) != null  ||
            dq.read(key2) == null)
          failMsg.append("Clear did not work.\n");
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    try
    {
      dq.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unable to delete data queue.\n");
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
}
