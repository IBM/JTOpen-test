///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.NP;


import java.io.FileOutputStream;
import java.io.PipedInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import com.ibm.as400.access.*;

import java.util.Vector;
import com.ibm.as400.access.AS400;

import test.misc.ThreadedTestcase;


class NPThreadTestcase
  extends ThreadedTestcase
{
  
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPThreadTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }

 /**
  * Creates a new NPThreadTestcase.
  * This is called from ThreadTest::createTestcases().
  */
  public NPThreadTestcase (AS400 systemObject,
                             Vector variationsToRun,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
  {
    super (systemObject, "NPThreadTestcase", 7, variationsToRun,
           runMode, fileOutputStream, password);
    //turnTraceOn();
  }


  void turnTraceOn()
  {
    //Trace.setTraceDatastreamOn(true);
    Trace.setTraceDiagnosticOn(true);
    //Trace.setTraceErrorOn(true);
    //Trace.setTraceInformationOn(true);
    //Trace.setTraceWarningOn(true);
    Trace.setTraceOn(true);
  }


 /**
  * Runs the variations requested.
  */
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      setup();
    }
    catch (Exception e)
    {
      System.out.println ("Setup failed");
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

    try
    {
      cleanup();
    }
    catch (Exception e){}
  }


  protected void setup()
    throws Exception
  {
    try
    {
	    // make sure JAVAPRINT printer device description exists
	    CommandCall cmd = new CommandCall(systemObject_);
	    cmd.run("crtdevprt devd(javaprint) devcls(*lcl) type(*ipds) " +
	            "model(0) port(5) swtset(0) online(*no) font(011)");
	    systemObject_.disconnectService(AS400.COMMAND);
    }
    catch (Exception e)
    {
      output_.println("Unable to do setup:" + e);
      throw e;
    }
  }


  protected void cleanup()
    throws Exception
  {
    try
    {
output_.println("in cleanup");
	    // delete the printer device description that we created
	    CommandCall cmd = new CommandCall(systemObject_);
output_.println("constructed cmdcall object");
	    cmd.run("dltdevd javaprint");
	    systemObject_.disconnectService(AS400.COMMAND);
	    systemObject_.disconnectService(AS400.PRINT);
output_.println("end of cleanup");
    }
    catch (Exception e)
    {
      output_.println("Cleanup failed:" + e);
      throw e;
    }
  }


 /**
  * One thread synchronously opening and retrieving a PrintObject from
  * a PrintObjectList.  Another thread closing this same list.
  */
  public void Var001()
  {
    try
    {
      pipeInput_        = new PipedInputStream();
	    PrinterList prtDList = new PrinterList(systemObject_);
	    prtDList.setPrinterFilter("*ALL");
      NPThread  t1 = new NPThread(pipeInput_, output_, this,
                                  NPThread.OPEN_SYNC_PL, prtDList);
      NPThread  t2 = new NPThread(pipeInput_, output_, this,
                                  NPThread.CLOSE_SYNC_PL, prtDList);
      objectInput_      = new ObjectInputStream(pipeInput_);
//      t1.setNumLoops(30);
//      t2.setNumLoops(30);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      prtDList.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception in var");
    }
  }



 /**
  * One thread asynchronously opening and retrieving a PrintObject from
  * a PrintObjectList.  Another thread closing this same list.
  */
  public void Var002()
  {
    try
    {
      pipeInput_        = new PipedInputStream();
	    PrinterList prtDList = new PrinterList(systemObject_);
	    prtDList.setPrinterFilter("*ALL");
      NPThread  t1 = new NPThread(pipeInput_, output_, this,
                                  NPThread.OPEN_ASYNC_PL, prtDList);
      NPThread  t2 = new NPThread(pipeInput_, output_, this,
                                  NPThread.CLOSE_ASYNC_PL, prtDList);
      objectInput_      = new ObjectInputStream(pipeInput_);
      t1.setNumLoops(30);
      t2.setNumLoops(30);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      prtDList.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception in var");
    }
  }



 /**
  * Multiple Threads calling update and getStringAtrib on the same
  * PrintObject.
  */
  public void Var003()
  {
    try
    {
      pipeInput_        = new PipedInputStream();
      // create a printer list object
      PrinterList prtDList = new PrinterList(systemObject_);
      prtDList.setPrinterFilter("*ALL");
      prtDList.openSynchronously();
      // get the JAVAPRINT Printer object
      int size   = prtDList.size();
      Printer prtD = null;
      for (int i=0; i<size; i++)
      {
        prtD = (Printer)prtDList.getObject(i);
        if (prtD != null && prtD.getName().equals("JAVAPRINT"))
          break;
        else
          prtD = null;
      }

      if ( prtD == null )
      {
        output_.println("PrintObject contained no items");
        notApplicable();
        return;
      }

      String[] attribs = new String[16];
      attribs[0]       = prtD.getStringAttribute(PrintObject.ATTR_CODEPAGE);
      attribs[1]       = prtD.getStringAttribute(PrintObject.ATTR_DEVCLASS);
      attribs[2]       = prtD.getStringAttribute(PrintObject.ATTR_DEVMODEL);
      attribs[3]       = prtD.getStringAttribute(PrintObject.ATTR_DEVTYPE);
      attribs[4]       = prtD.getStringAttribute(PrintObject.ATTR_FONTID);
      //attribs[6]       = prtD.getStringAttribute(PrintObject.ATTR_FORM_DEFINITION);
      attribs[5]       = prtD.getStringAttribute(PrintObject.ATTR_FORMFEED);
      attribs[6]       = prtD.getStringAttribute(PrintObject.ATTR_CHAR_ID);
      attribs[7]       = prtD.getStringAttribute(PrintObject.ATTR_MFGTYPE);
      attribs[8]       = prtD.getStringAttribute(PrintObject.ATTR_MESSAGE_QUEUE);
      attribs[9]       = prtD.getStringAttribute(PrintObject.ATTR_PRINTER);
      attribs[10]      = prtD.getStringAttribute(PrintObject.ATTR_DESCRIPTION);
      attribs[11]      = prtD.getStringAttribute(PrintObject.ATTR_USRDEFOPT);
      //attribs[14]      = prtD.getStringAttribute(PrintObject.ATTR_USER_DEFINED_OBJECT);
     // attribs[15]      = prtD.getStringAttribute(PrintObject.ATTR_USER_TRANSFORM_PROG);
    //  attribs[16]      = prtD.getStringAttribute(PrintObject.ATTR_USER_DRIVER_PROG);
      attribs[12]      = prtD.getStringAttribute(PrintObject.ATTR_SCS2ASCII);
      attribs[13]       = prtD.getStringAttribute(PrintObject.ATTR_AFP);

      NPThread  t1 = new NPThread(pipeInput_, output_, this,
                                      NPThread.UPDATE, prtD, attribs);
      NPThread  t2 = new NPThread(pipeInput_, output_, this,
                                      NPThread.UPDATE, prtD, attribs);
      objectInput_      = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      prtDList.close();
      System.gc();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed(e, "Unexpected Exception in var");
    }
  }


 /**
  * Multiple threads creating and writing to SpooledFileOutputStreams
  * and then deleting the associated SpooledFile.
  */
  public void Var004()
  {
    try
    {
      pipeInput_        = new PipedInputStream();
      NPThread  t1  = new NPThread(pipeInput_, output_, this,
                                  NPThread.CREATE_SPLF, systemObject_, null, null);
      NPThread  t2  = new NPThread(pipeInput_, output_, this,
                                  NPThread.CREATE_SPLF, systemObject_, null, null);
      NPThread  t3  = new NPThread(pipeInput_, output_, this,
                                  NPThread.CREATE_SPLF, systemObject_, null, null);
      NPThread  t4  = new NPThread(pipeInput_, output_, this,
                                  NPThread.CREATE_SPLF, systemObject_, null, null);
      objectInput_      = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      t4.start();
      go();
      handleError();
      stopThreads();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception in var");
    }
  }


 /**
  * Multiple threads write data to a shared SpooledFileOutputStream and then
  * read back and verify the the data using un-shared PrintObjectInputStreams
  * to the SpooledFile.
  */
  public void Var005()
  {
    try
    {
output_.println("in var 5");
      pipeInput_        = new PipedInputStream();
      SpooledFileOutputStream splfOS  = new SpooledFileOutputStream(systemObject_, null, null, null);

      // write one record to the file to open it.
      byte[] dataToWrite = new byte[20];  // length must be same as used by NPThread.SPLF_RD_WR
      byte[] nameBytes   = Thread.currentThread().getName().getBytes();
      int i=0;
      for (; i<nameBytes.length; i++)
        dataToWrite[i] = nameBytes[i];
      // fill rest of array with blanks
      for (; i<dataToWrite.length; i++)
        dataToWrite[i] = (byte)' '; //btw added conversion
      splfOS.write(dataToWrite);
      splfOS.flush();

      NPThread  t1 = new NPThread(pipeInput_, output_, this,
                                  NPThread.SPLF_RD_WR, splfOS);
//      NPThread  t2 = new NPThread(pipeInput_, output_, this,
//                                  NPThread.SPLF_RD_WR, splfOS);
      objectInput_      = new ObjectInputStream(pipeInput_);
      t1.start();
//      t2.start();
      go();
      handleError();
      stopThreads();
      splfOS.close();
      splfOS.getSpooledFile().delete();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception in var");
    }
  }

 /**
  * One thread synchronously opening and retrieving a SpooledFile from
  * a SpooledFileList.  Another thread closing this same list.
  */
  public void Var006()
  {
    try
    {
      pipeInput_               = new PipedInputStream();
	    SpooledFileList splfList = new SpooledFileList(systemObject_);
      NPThread  t1 = new NPThread(pipeInput_, output_, this,
                                  NPThread.OPEN_SYNC_SL, splfList);
      NPThread  t2 = new NPThread(pipeInput_, output_, this,
                                  NPThread.CLOSE_SYNC_SL, splfList);
      objectInput_      = new ObjectInputStream(pipeInput_);
//      t1.setNumLoops(30);
//      t2.setNumLoops(30);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      splfList.close();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failed(e, "Unexpected Exception in var");
    }
  }



 /**
  * One thread asynchronously opening and retrieving a SpooledFile from
  * a SpooledFileList.  Another thread closing this same list.
  */
  public void Var007()
  {
    try
    {
      pipeInput_        = new PipedInputStream();
	    SpooledFileList splfList = new SpooledFileList(systemObject_);
      NPThread  t1 = new NPThread(pipeInput_, output_, this,
                                  NPThread.OPEN_ASYNC_SL, splfList);
      NPThread  t2 = new NPThread(pipeInput_, output_, this,
                                  NPThread.CLOSE_ASYNC_SL, splfList);
      objectInput_      = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      splfList.close();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception in var");
    }
  }

}



