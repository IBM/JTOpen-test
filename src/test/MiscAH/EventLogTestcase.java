
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EventLogTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.MiscAH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.EventLog;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;

import test.Testcase;


/**
Test methods not covered by other testcases.
**/
public class EventLogTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "EventLogTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.EventLogTest.main(newArgs); 
   }

   
/**
Constructor.
**/
  public EventLogTestcase(AS400            systemObject,
                           Vector<String> variationsToRun,
                           int              runMode,
                           FileOutputStream fileOutputStream)
  throws IOException
  {
    super(systemObject, "EventLogTestcase", 11, variationsToRun, runMode,
          fileOutputStream);

  }


  boolean deleteFile(String fileName)
  {
    try
    {
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
    }
  }


/**
Construct a default EventLog.
**/
  public void variation001()
  {
    setVariation(1);
    
    try
    {
       EventLog log = new EventLog();
       assertCondition(true,"log created "+log);
    }
    catch(Exception e)
    {
       failed("Unexpected Failure." + e);
    }
    
  }


  /**
Construct an EventLog with an OutputStream.
**/
  public void variation002()
  {
    setVariation(2);
    
    try
    {
       FileOutputStream stream = new FileOutputStream("logFile");
       EventLog log = new EventLog(stream);
       assertCondition(true,"log created "+log);
       
       stream.close();
       deleteFile("logFile");
    }
    catch(Exception e)
    {
       failed("Unexpected Failure." + e);
    }
    
  }

  /**
Construct an EventLog with a PrintWriter.
**/
  public void variation003()
  {
    setVariation(3);
    
    try
    {
       FileOutputStream stream = new FileOutputStream("logFile2");
       PrintWriter pw = new PrintWriter(stream);
       EventLog log = new EventLog(pw);
       assertCondition(true,"log created "+log);
       
       pw.close();
       deleteFile("logFile2");
    }
    catch(Exception e)
    {
       failed("Unexpected Failure." + e);
    }
    
  }

  /**
Construct an EventLog with a String.
**/
  public void variation004()
  {
    setVariation(4);
    
    try
    {
       EventLog log = new EventLog("logFile3");
       assertCondition(true,"log created "+log);
       
       // need to do some garbage collection so that the log file can be delete.
       log = null;
       System.gc();
       Thread.sleep(10000);
       deleteFile("logFile3");
    }
    catch(Exception e)
    {
       failed("Unexpected Failure." + e);
    }
    
  }

  /**
Ensure that log(String) logs a message to a file.
**/
  public void variation005()
  {
    setVariation(5);
    try
    {
      String msg = "variation5";
      FileOutputStream stream = new FileOutputStream("logFile4");
      PrintWriter pw = new PrintWriter(stream);
      EventLog log = new EventLog(pw);
      log.log(msg);
      
      String data = "";
      String data2 = null;
      RandomAccessFile file = new RandomAccessFile("logFile4", "r");
      while ((data2 = file.readLine()) != null)
      { 
        data += data2;
      }
      
      assertCondition(data.indexOf(msg) != -1);
      
      pw.close();
      file.close();
      deleteFile("logFile4");

    }
    catch (Exception e)
    {
      failed(e);
    }
  }

  /**
Ensure that log(String, Throwable) logs a message and exception to a file.
**/
  public void variation006()
  {
    setVariation(6);
    try
    {
      String msg = "variation6";
      FileOutputStream stream = new FileOutputStream("logFile5");
      PrintWriter pw = new PrintWriter(stream);
      EventLog log = new EventLog(pw);
      log.log(msg, new NullPointerException("test"));
      
      String data = "";
      String data2 = null;
      BufferedReader reader = new BufferedReader(new FileReader("logFile5"));
      while ((data2 = reader.readLine()) != null)
      { 
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1 &
             data.indexOf("java.lang.NullPointerException: test") != -1);
      
      pw.close();
      reader.close();
      deleteFile("logFile5");

    }
    catch (Exception e)
    {
      failed(e);
    }
  }

  /**
Ensure that log(String) logs a message to standard out.
**/
  public void variation007()
  {
    setVariation(7);
    try
    {
      String msg = "This is only a test.";
      EventLog log = new EventLog();
      log.log(msg);
      
      succeeded();

    }
    catch (Exception e)
    {
      failed(e);
    }
  }

  /**
Ensure that using a null string throws a NullPointerException.
**/
  public void variation008()
  {
    setVariation(8);
    try
    {
      String path = null;
      EventLog log = new EventLog(path);
      failed("No Exception."+log);

    }
    catch (Exception e)
    {
      if (exceptionIs(e, "NullPointerException", "pathname"))
      {
         succeeded();
      }
      else
      {
         failed(e, "Wrong exception info.");
      }
    }
  }

  /**
Ensure that using a Null OutputStream throws a NullPointerException.
**/
  public void variation009()
  {
    setVariation(9);
    try
    {
      FileOutputStream stream = null;

      EventLog log = new EventLog(stream);
      failed("No Exception."+log);

    }
    catch (Exception e)
    {
      if (exceptionIs(e, "NullPointerException", "stream"))
      {
         succeeded();
      }
      else
      {
         failed(e, "Wrong exception info.");
      }
    }
  }

  /**
Ensure that passing a null parameter to EventLog(PrintWriter) 
throws a NullPointerException.
**/
  public void variation010()
  {
    setVariation(10);
    try
    {
      PrintWriter pw = null;

      EventLog log = new EventLog(pw);
      failed("No Exception."+log);

    }
    catch (Exception e)
    {
      if (exceptionIs(e, "NullPointerException", "out"))
      {
         succeeded();
      }
      else
      {
         failed(e, "Wrong exception info.");
      }
    }
  }

  /**
Ensure that log(String) logs a message to an IFSFileOutputStream.
**/
  public void variation011()
  {
    setVariation(11);
    try
    {
      // AS400Message[] msglist_;
      String msg = "variation11";
      
      IFSFile dir = new IFSFile(systemObject_, "/LOGTEST");
      dir.mkdir();

      IFSFileOutputStream stream = new IFSFileOutputStream(systemObject_, "/LOGTEST/IFSFILE");
      
      PrintWriter pw = new PrintWriter(stream);
      EventLog log = new EventLog(pw);
      log.log(msg);
      
      String data = "";
      String data2;
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(new IFSFileInputStream(systemObject_, "/LOGTEST/IFSFile")));
      while ((data2 = reader.readLine()) != null)
      { 
        data += data2;
      }
      assertCondition(data.indexOf(msg) != -1);
      
      reader.close();
      stream.close();
      dir.delete();

    }
    catch (Exception e)
    {
      failed(e);
    }
  }
  

}
