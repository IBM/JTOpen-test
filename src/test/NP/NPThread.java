///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.PipedInputStream;
import java.io.PrintWriter;
import com.ibm.as400.access.*;

import test.ComponentThread;
import test.Testcase;
import test.ThreadedTestcase;

class NPThread
  extends ComponentThread
{
  // Functions that this thread can perform.  One is specified at object construction.
  public static final int   OPEN_SYNC_PL       = 1; // open PrinterList synchronously
  public static final int   CLOSE_SYNC_PL      = 2; // close PrinterList synchronously
  public static final int   CREATE_SPLF        = 3;
  public static final int   UPDATE             = 4;
  public static final int   OPEN_ASYNC_PL      = 5;
  public static final int   CLOSE_ASYNC_PL     = 6;
  public static final int   SPLF_RD_WR         = 7;
  public static final int   OPEN_SYNC_SL       = 8; // open SpooledFileList synchronously
  public static final int   CLOSE_SYNC_SL      = 9; // close SpooledFileList synchronously
  public static final int   OPEN_ASYNC_SL      = 10;
  public static final int   CLOSE_ASYNC_SL     = 11;


  private AS400           system          = null;
  private PrintObject     printObject     = null;
  private String[]        attributes      = null;
  private PrinterList     printerList     = null;
  private SpooledFileOutputStream splfOS  = null;
  private byte[]          dataToWrite     = null;
  private SpooledFileList splfList        = null;
  private OutputQueue     outputq         = null;
  private PrinterFile     printerFile     = null;

 /**
  * Constructor
  */
  public NPThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function)
  {
    super(pipeReader, output, testcase, function);
  }


 /**
  * Constructor for OPEN_[A]SYNC_PL & CLOSE_[A]SYNC_PL
  */
  public NPThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, PrinterList list)
  {
    super(pipeReader, output, testcase, function);
    printerList = list;
  }


 /**
  * Constructor for OPEN_[A]SYNC_SL & CLOSE_[A]SYNC_SL
  */
  public NPThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, SpooledFileList list)
  {
    super(pipeReader, output, testcase, function);
    splfList = list;
  }


 /**
  * Constructor for UPDATE
  */
  public NPThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, PrintObject po,
                   String[] attribs)
  {
    super(pipeReader, output, testcase, function);
    printObject = po;
    attributes = attribs;
  }


 /**
  * Constructor for CREATE_SPLF
  */
  public NPThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, AS400 sys, PrinterFile pf, OutputQueue outq)
  {
    super(pipeReader, output, testcase, function);
    system  = sys;
    outputq = outq;
    printerFile = pf;
  }


 /**
  * Constructor for SPLF_RD_WR
  */
  public NPThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function,
                   SpooledFileOutputStream os)
  {
    super(pipeReader, output, testcase, function);
    splfOS = os;
    // create the data to write to spooled file.  It is a blank
    // filled byte array containing this thread's name.
    dataToWrite = new byte[20];
    byte[] nameBytes = getName().getBytes();
    int i=0;
    for (; i<nameBytes.length; i++)
      dataToWrite[i] = nameBytes[i];
    // fill rest of array with blanks
    for (; i<dataToWrite.length; i++)
      dataToWrite[i] = (byte)' ';    // btw added conversion
  }


  public void run()
  {
    // notify testcase that we are ready to start.
    testcase_.ready();

    // perform the task specified by function, numLoops_
    // times unless the stop flag is set.
    for (int i = 0; i < numLoops_ && !stop_ ; i++)
    {
      // don't be a selfish thread...
      try { sleep(1); } catch (InterruptedException e) {}

output_.println(getName()+" loop "+i);
      switch (function_)
      {
        case OPEN_SYNC_PL:
          performOpenSyncPL();
          break;
        case OPEN_SYNC_SL:
          performOpenSyncSL();
          break;
        case OPEN_ASYNC_PL:
          performOpenAsyncPL();
          break;
        case OPEN_ASYNC_SL:
          performOpenAsyncSL();
          break;
        case CLOSE_SYNC_PL:
          performCloseSyncPL();
          break;
        case CLOSE_SYNC_SL:
          performCloseSyncSL();
          break;
        case CLOSE_ASYNC_PL:
          performCloseSyncPL();
          break;
        case CLOSE_ASYNC_SL:
          performCloseSyncSL();
          break;
        case UPDATE:
           performUpdate();
           break;
        case CREATE_SPLF:
           performCreateSplf();
           break;
        case SPLF_RD_WR:
           performSplfRdWr();
           break;
      }
    }

    // done: close our end of pipe and stop.
    this.kill();
  }


 /**
  * Opens a PrinterList synchronously and prints out the first item.
  * If the list has been closed by another thread (CLOSE_SYNC) then
  * we expect a ExtendedIllegalStateException to be thrown when getObject
  * is called.
  */
  private void performOpenSyncPL()
  {
    try
    {
      printerList.setPrinterFilter("*ALL");
      printerList.openSynchronously();
      Printer prtD = (Printer)printerList.getObject(0);
      if (prtD != null)
      {
        output_.println(" prtD = " + prtD.getName());
      }
      else
        output_.println("prtD == null");
   }
    catch (ExtendedIllegalStateException e1)
    {
      output_.println(getName()+" OPEN_SYNC_PL's list was closed");
    }
    catch (Exception e2)
    {
      e2.printStackTrace(output_);
      error(getName()+" Unexpected Exception");
    }
  }


 /**
  * Opens a SpooledFileList synchronously and prints out the first item.
  * If the list has been closed by another thread (CLOSE_SYNC) then
  * we expect a ExtendedIllegalStateException to be thrown when getObject
  * is called.
  */
  private void performOpenSyncSL()
  {
    try
    {
	    splfList.setUserFilter("*ALL");
	    splfList.setQueueFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.OUTQ");
      SpooledFile splf = (SpooledFile)splfList.getObject(0);
      if (splf != null)
      {
        System.out.println(splf.getStringAttribute(PrintObject.ATTR_SPOOLFILE));
      }
   }
    catch (ExtendedIllegalStateException e1)
    {
      output_.println(getName()+" OPEN_SYNC_SL's list was closed");
    }
    catch (Exception e2)
    {
      e2.printStackTrace(output_);
      error(getName()+" Unexpected Exception");
    }
  }


 /**
  * Opens a PrinterList asynchronously and prints out the first item.
  * If the list has been closed by another thread (CLOSE_ASYNC) then
  * we expect a ExtendedIllegalStateException to be thrown when getObject
  * is called.
  */
  private void performOpenAsyncPL()
  {
    try
    {
      printerList.setPrinterFilter("*ALL");
      printerList.openAsynchronously();
      printerList.waitForListToComplete();
      Printer prtD = (Printer)printerList.getObject(0);
      if (prtD != null)
      {
        System.out.println(" prtD = " + prtD.getName());
      }
   }
    catch (ExtendedIllegalStateException e1)
    {
      output_.println(getName()+" OPEN_ASYNC's list was closed");
    }
    catch (Exception e2)
    {
      e2.printStackTrace(output_);
      error(getName()+" Unexpected Exception");
    }
  }



 /**
  * Opens a SpooledFileList asynchronously and prints out the first item.
  * If the list has been closed by another thread (CLOSE_ASYNC) then
  * we expect a ExtendedIllegalStateException to be thrown when getObject
  * is called.
  */
  private void performOpenAsyncSL()
  {
    try
    {
	    splfList.setUserFilter("*ALL");
	    splfList.setQueueFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.OUTQ");
      splfList.openAsynchronously();
      splfList.waitForListToComplete();
      PrintObject  splf = splfList.getObject(0);
      if (splf != null)
      {
        System.out.println(splf.getStringAttribute(PrintObject.ATTR_SPOOLFILE));
      }
    }
    catch (ExtendedIllegalStateException e1)
    {
      output_.println(getName()+" OPEN_ASYNC's list was closed");
    }
    catch (Exception e2)
    {
      e2.printStackTrace(output_);
      error(getName()+" Unexpected Exception");
    }
  }



 /**
  * Calls close on a PrinterList. Used in conjunction with OPEN_[A]SYNC_SP
  * running in another thread.
  */
  private void performCloseSyncPL()
  {
    try
    {
    	output_.println(getName()+" is CLOSER");
      // call close 15 times to increase chance of collision with OPEN_SYNC Thread.
      for(int i=0; i<15; i++)
	      printerList.close();
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      error(getName()+" Unexpected Error");
    }
  }


 /**
  * Calls close on a SpooledFileList. Used in conjunction with OPEN_[A]SYNC_SP
  * running in another thread.
  */
  private void performCloseSyncSL()
  {
    try
    {
    	output_.println(getName()+" is CLOSER");
      // call close 15 times to increase chance of collision with OPEN_SYNC_SP Thread.
      for(int i=0; i<15; i++)
	      splfList.close();
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      error(getName()+" Unexpected Error");
    }
  }



 /**
  * Updates the printObject and retrieves and verifies the its string attribs.
  */
  private void performUpdate()
  {
    try
    {
      printObject.update();
      String[] attribs = new String[16];
      attribs[0]       = printObject.getStringAttribute(PrintObject.ATTR_CODEPAGE);
      attribs[1]       = printObject.getStringAttribute(PrintObject.ATTR_DEVCLASS);
      attribs[2]       = printObject.getStringAttribute(PrintObject.ATTR_DEVMODEL);
      attribs[3]       = printObject.getStringAttribute(PrintObject.ATTR_DEVTYPE);
      attribs[4]       = printObject.getStringAttribute(PrintObject.ATTR_FONTID);
      //attribs[6]       = printObject.getStringAttribute(PrintObject.ATTR_FORM_DEFINITION);
      attribs[5]       = printObject.getStringAttribute(PrintObject.ATTR_FORMFEED);
      attribs[6]       = printObject.getStringAttribute(PrintObject.ATTR_CHAR_ID);
      attribs[7]       = printObject.getStringAttribute(PrintObject.ATTR_MFGTYPE);
      attribs[8]       = printObject.getStringAttribute(PrintObject.ATTR_MESSAGE_QUEUE);
      attribs[9]       = printObject.getStringAttribute(PrintObject.ATTR_PRINTER);
      attribs[10]      = printObject.getStringAttribute(PrintObject.ATTR_DESCRIPTION);
      attribs[11]      = printObject.getStringAttribute(PrintObject.ATTR_USRDEFOPT);
      //attribs[14]      = printObject.getStringAttribute(PrintObject.ATTR_USER_DEFINED_OBJECT);
      //attribs[15]      = printObject.getStringAttribute(PrintObject.ATTR_USER_TRANSFORM_PROG);
      //attribs[16]      = printObject.getStringAttribute(PrintObject.ATTR_USER_DRIVER_PROG);
      attribs[12]      = printObject.getStringAttribute(PrintObject.ATTR_SCS2ASCII);
      attribs[13]       = printObject.getStringAttribute(PrintObject.ATTR_AFP);

      // perform comparison to verify we retrieved correct attributes
      for(int i=0; i<attribs.length; i++)
      {
      	if ( attribs[i] == null || attributes[i] == null )
      	{
      	  if ( attributes[i] != attribs[i])
      	  {
      	    error(getName()+" attributes not equal");
      	    return;
      	  }
        }
        else if ( ! attribs[i].trim().equals( attributes[i] ) )
        {
          error(getName()+" String attributes don't match: "+
                attribs[i]+" != "+attributes[i]);
          return;
        } // end of else if ( ! attribs[i].equals( attributes[i] ) )
      } // end of for(int i=0; i<attribs.length; i++)
    } // end of try
    catch (Exception e)
    {
      e.printStackTrace(output_);
      error(getName() + "Unexpected Exception" + e);
    }
  }


 /**
  * creates a SpooledFileOutpputStream and writes data to the stream.
  * After the data has been written the spooledfile is deleted.
  */
  private void performCreateSplf()
  {
    try
    {
      // buffer for data
      byte[] buf = new byte[2048];

      // fill the buffer
      for (int i=0; i<2048; i++)
        buf[i] = (byte)'9'; //btw added conversion

      // create a spooled file output stream
      SpooledFileOutputStream outStream = new SpooledFileOutputStream(system, null, printerFile, outputq);

      // check to see that we got a spooled file output stream reference
      if (outStream == null)
      {
        error(getName()+" unable to create a SpooledFileOutputStream object");
        return;
      }

      // write buf 20 times to spooled file
      for (int i=0; i<20; i++)
        outStream.write(buf);

      // close the spooled file
      outStream.close();
      // delete the spooled file
      SpooledFile splf = outStream.getSpooledFile();


      output_.println(getName()+" SplF: " + splf.getName() +
                                     " " + splf.getNumber() +
                                     " " + splf.getJobName() +
                                     " " + splf.getJobNumber() +
                                     " " + splf.getJobUser() );

      splf.delete();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      error(getName()+" Unexpected Exception "+e,e);
    }
  }


 /**
  * Writes data to a shared SpooledFileOutputStream and then,
  * reads back and verifies the data.
  */
  private void performSplfRdWr()
  {
    try
    {
      splfOS.write(dataToWrite);
      splfOS.flush();
      // get the input stream to the spooledFile
      PrintObjectInputStream splfIS = splfOS.getSpooledFile().getInputStream();
      byte[] readData = new byte[dataToWrite.length];
      // read back entry.
      if ( splfIS.read(readData) < dataToWrite.length )
      {
        error(getName()+" didn't read back a full record: " + (new String(readData)));
        return;
      }
output_.println("dataToWrite = " + (new String(dataToWrite)) + " readData = " + (new String(readData)));
      // find the data this thread just wrote.
      while( ! Testcase.isEqual(dataToWrite, readData) )
      {
        long  skipped = 0;
        // if we can't skip a full record length something is wrong.
        if ( (skipped = splfIS.skip((long) dataToWrite.length) ) < dataToWrite.length )
        {
	    error(getName()+" skipped less than the records length:" + skipped);
          return;
        }
        if ( splfIS.read(readData) < dataToWrite.length )
        {
          error(getName()+" didn't read back a full record");
          return;
        }
      }

      // close the inputstream
      splfIS.close();
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      error(getName()+" Unexpected Exception", e);
    }
  }

 } // end of class NPThread


